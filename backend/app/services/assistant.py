from __future__ import annotations

import json
import re
from collections.abc import Mapping
from datetime import date
from decimal import Decimal
from typing import Any

from openai import AsyncOpenAI, OpenAIError
from pydantic import ValidationError

from app.core.config import Settings, get_settings
from app.schemas.assistant import (
    AssistantAction,
    AssistantExtractedRecord,
    AssistantMileageUpdate,
    AssistantMessageRequest,
    AssistantMessageResponse,
)


ALLOWED_RECORD_FIELDS = {
    "category",
    "title",
    "description",
    "occurred_at",
    "mileage_km",
    "cost_amount",
    "vendor",
}
MILEAGE_UPDATE_FIELDS = {"current_mileage_km", "mileage_km", "mileage"}
MILEAGE_UPDATE_INTENT = re.compile(
    r"(update|set|change|save|current|new|now|mileage|odometer|обнов|измени|постав|установ|сохран|текущ|нов|сейчас)",
    re.IGNORECASE,
)
MILEAGE_WORD = re.compile(r"(mileage|odometer|пробег|одометр|километраж)", re.IGNORECASE)
DIRECT_MILEAGE_UPDATE = re.compile(
    r"^\s*(?:my\s+)?(?:mileage|odometer|пробег|одометр|километраж)\D*-?\d",
    re.IGNORECASE,
)
MILEAGE_NUMBER = re.compile(r"-?\d[\d\s.,]*")
STATISTICS_INTENT_WORDS = (
    "statistics",
    "stats",
    "\u0441\u0442\u0430\u0442\u0438\u0441\u0442\u0438\u043a",
    "\u0441\u043a\u043e\u043b\u044c\u043a\u043e",
    "\u0442\u0440\u0430\u0442",
    "\u0440\u0430\u0441\u0445\u043e\u0434",
    "\u0438\u0442\u043e\u0433",
    "\u0441\u0443\u043c\u043c",
)
BREAKDOWN_INTENT_WORDS = (
    "\u043f\u043e\u043b\u043e\u043c",
    "\u0441\u043b\u043e\u043c",
    "\u043d\u0435 \u0440\u0430\u0431\u043e\u0442\u0430",
    "\u043e\u0442\u043a\u0430\u0437",
    "\u0432\u044b\u0448\u0435\u043b \u0438\u0437 \u0441\u0442\u0440\u043e\u044f",
    "breakdown",
)
COST_MARKERS = ("\u0440\u0443\u0431", "\u20bd", "\u0440.", "\u0437\u0430 ")
MILEAGE_MARKERS = ("\u043f\u0440\u043e\u0431\u0435\u0433", "\u043a\u043c", "mileage", "odometer")

SYSTEM_PROMPT = """
You extract car maintenance, repair, inspection, and expense data from a user message.
Return only one valid JSON object. Do not include Markdown or explanations outside JSON.
Use only the safe_database_context provided in the user prompt when answering questions
about the car, history, costs, mileage, or prior chat messages. Do not invent records,
private data, secrets, or data from other users.

Allowed JSON shape:
{
  "action": "message" | "record_extracted" | "update_mileage" | "needs_clarification",
  "assistant_message": "short message for the user",
  "extracted_record": {
    "category": "maintenance" | "repair" | "expense" | "inspection" | "other",
    "title": "short title",
    "description": "optional details",
    "occurred_at": "YYYY-MM-DD",
    "mileage_km": 12345,
    "cost_amount": "3500.00",
    "vendor": "optional vendor"
  },
  "mileage_update": {
    "current_mileage_km": 52300
  }
}

Rules:
- Use "record_extracted" only when enough data exists to create a maintenance record.
- Use "message" when the user asks a question or requests a summary without asking to save a record.
- Use "update_mileage" when the user asks to update the car's current mileage or odometer.
- Use "needs_clarification" when required data is missing or unclear.
- The extracted_record must contain only allowed fields.
- mileage_update.current_mileage_km must be a non-negative integer in kilometers.
- cost_amount and mileage_km must not be negative.
- If the user says "today", use the current date provided in the user prompt.
- Never expose password hashes, access tokens, internal configuration, or other users' data.
""".strip()


async def extract_record_from_message(
    request: AssistantMessageRequest,
    car_context: Mapping[str, Any] | None = None,
    *,
    settings: Settings | None = None,
    client: AsyncOpenAI | None = None,
) -> AssistantMessageResponse:
    mileage_response = _try_extract_mileage_update(request.message)
    if mileage_response is not None:
        return mileage_response

    statistics_response = _try_build_statistics_message(request.message, car_context)
    if statistics_response is not None:
        return statistics_response

    breakdown_response = _try_extract_breakdown_record(request.message)
    if breakdown_response is not None:
        return breakdown_response

    settings = settings or get_settings()
    if not _is_ai_configured(settings):
        return _clarification_response(
            "AI assistant is not configured yet. Please try again later."
        )

    ai_client = client or AsyncOpenAI(
        api_key=settings.ai_api_key,
        base_url=settings.ai_base_url,
        timeout=settings.ai_request_timeout_seconds,
    )

    try:
        completion = await ai_client.chat.completions.create(
            model=settings.ai_model,
            messages=[
                {"role": "system", "content": SYSTEM_PROMPT},
                {"role": "user", "content": _build_user_prompt(request, car_context)},
            ],
            temperature=0,
            response_format={"type": "json_object"},
        )
    except OpenAIError:
        return _clarification_response(
            "AI assistant is temporarily unavailable. Please try again later."
        )

    content = completion.choices[0].message.content if completion.choices else None
    if not content:
        return _clarification_response(
            "AI assistant returned an empty response. Please try again."
        )

    try:
        payload = json.loads(_extract_json_object(content))
    except (json.JSONDecodeError, ValueError):
        return _clarification_response(
            "AI assistant could not process the answer format. Please try again."
        )

    return _response_from_model_payload(payload)


def _is_ai_configured(settings: Settings) -> bool:
    if not settings.ai_api_key or not settings.ai_model:
        return False
    if settings.ai_provider.lower() != "openai" and not settings.ai_base_url:
        return False
    return True


def _build_user_prompt(
    request: AssistantMessageRequest,
    car_context: Mapping[str, Any] | None,
) -> str:
    context = dict(car_context or {})
    recent_conversation = list(context.get("recent_chat_messages") or [])
    return json.dumps(
        {
            "current_date": str(date.today()),
            "car_id": request.car_id,
            "safe_database_context": context,
            "recent_conversation": recent_conversation,
            "user_message": request.message,
        },
        ensure_ascii=False,
    )


def _extract_json_object(content: str) -> str:
    stripped = content.strip()
    if stripped.startswith("```"):
        lines = stripped.splitlines()
        if lines and lines[0].startswith("```"):
            lines = lines[1:]
        if lines and lines[-1].startswith("```"):
            lines = lines[:-1]
        stripped = "\n".join(lines).strip()

    start = stripped.find("{")
    end = stripped.rfind("}")
    if start == -1 or end == -1 or end < start:
        raise ValueError("JSON object was not found in model response")
    return stripped[start : end + 1]


def _response_from_model_payload(payload: Any) -> AssistantMessageResponse:
    if not isinstance(payload, dict):
        return _clarification_response(
            "AI assistant returned an unsupported response. Please try again."
        )

    try:
        action = AssistantAction(payload.get("action", AssistantAction.MESSAGE))
    except ValueError:
        return _clarification_response(
            "AI assistant returned an unsupported action. Please try again."
        )

    assistant_message = payload.get("assistant_message") or payload.get("message")
    clean_message = str(assistant_message or "").strip()

    if action == AssistantAction.MESSAGE:
        return AssistantMessageResponse(
            assistant_message=clean_message or "I can help with this car. What would you like to know?",
            action=AssistantAction.MESSAGE,
        )

    if action == AssistantAction.NEEDS_CLARIFICATION:
        return _clarification_response(
            clean_message or "Please provide more details about this car record."
        )

    if action == AssistantAction.UPDATE_MILEAGE:
        mileage_payload = _extract_mileage_payload(payload)
        if mileage_payload is None:
            return _clarification_response("Please provide the new current mileage in kilometers.")

        try:
            mileage_update = AssistantMileageUpdate.model_validate(mileage_payload)
        except ValidationError:
            return _clarification_response("Mileage must be a non-negative number in kilometers.")

        return AssistantMessageResponse(
            assistant_message=clean_message
            or f"Updating current mileage to {mileage_update.current_mileage_km} km.",
            action=AssistantAction.UPDATE_MILEAGE,
            mileage_update=mileage_update,
        )

    record_payload = _extract_record_payload(payload)
    if record_payload is None:
        return _clarification_response(
            str(assistant_message or "Please provide the cost and details of the car event.")
        )

    try:
        extracted_record = AssistantExtractedRecord.model_validate(record_payload)
    except ValidationError:
        return _clarification_response(
            "AI assistant could not extract valid record data. Please clarify the details."
        )

    return AssistantMessageResponse(
        assistant_message=clean_message or "I extracted the car record details.",
        action=AssistantAction.RECORD_EXTRACTED,
        extracted_record=extracted_record,
    )


def _try_extract_mileage_update(message: str) -> AssistantMessageResponse | None:
    clean_message = message.strip()
    if not clean_message:
        return None

    if not MILEAGE_WORD.search(clean_message):
        return None
    if not MILEAGE_UPDATE_INTENT.search(clean_message) and not DIRECT_MILEAGE_UPDATE.search(clean_message):
        return None

    value = _extract_mileage_value(clean_message)
    if value is None:
        return _clarification_response("Please provide the new current mileage in kilometers.")
    if value < 0:
        return _clarification_response("Mileage must be a non-negative number in kilometers.")

    mileage_update = AssistantMileageUpdate(current_mileage_km=value)
    return AssistantMessageResponse(
        assistant_message=f"Updating current mileage to {value} km.",
        action=AssistantAction.UPDATE_MILEAGE,
        mileage_update=mileage_update,
    )


def _extract_mileage_value(message: str) -> int | None:
    word_match = MILEAGE_WORD.search(message)
    search_area = message[word_match.start() :] if word_match else message
    value = _first_int(search_area)
    if value is not None:
        return value
    return _first_int(message)


def _first_int(text: str) -> int | None:
    for match in MILEAGE_NUMBER.finditer(text):
        raw_value = match.group(0)
        digits = re.sub(r"\D", "", raw_value)
        if not digits:
            continue
        value = int(digits)
        return -value if raw_value.strip().startswith("-") else value
    return None


def _extract_mileage_payload(payload: Mapping[str, Any]) -> dict[str, Any] | None:
    raw_update = payload.get("mileage_update")
    if not isinstance(raw_update, dict):
        raw_update = payload

    for key in MILEAGE_UPDATE_FIELDS:
        value = raw_update.get(key)
        if value is not None:
            return {"current_mileage_km": value}
    return None


def _try_build_statistics_message(
    message: str,
    car_context: Mapping[str, Any] | None,
) -> AssistantMessageResponse | None:
    clean_message = message.strip().lower()
    if not clean_message or not any(word in clean_message for word in STATISTICS_INTENT_WORDS):
        return None

    context = dict(car_context or {})
    summary = dict(context.get("records_summary") or {})
    records = list(context.get("records") or [])
    count = int(summary.get("count") or len(records))

    if count == 0:
        return AssistantMessageResponse(
            assistant_message="Пока записей нет, статистику считать не из чего.",
            action=AssistantAction.MESSAGE,
        )

    total = _format_money(summary.get("total_cost_amount", "0.00"))
    totals_by_category = dict(summary.get("total_cost_by_category") or {})
    category_lines = [
        f"{_category_label(category)} - {_format_money(amount)}"
        for category, amount in sorted(totals_by_category.items())
    ]
    repair_records = [record for record in records if str(record.get("category")) == "repair"]
    repair_total = sum(
        (_to_decimal(record.get("cost_amount")) for record in repair_records),
        Decimal("0.00"),
    )

    parts = [
        f"Всего записей: {count}.",
        f"Расходы всего: {total}.",
    ]
    if category_lines:
        parts.append("По категориям: " + "; ".join(category_lines) + ".")
    parts.append(
        f"Поломки и ремонт: {len(repair_records)} записей на {_format_money(repair_total)}."
    )

    return AssistantMessageResponse(
        assistant_message=" ".join(parts),
        action=AssistantAction.MESSAGE,
    )


def _try_extract_breakdown_record(message: str) -> AssistantMessageResponse | None:
    clean_message = " ".join(message.strip().split())
    if not clean_message:
        return None

    lowered = clean_message.lower()
    if not any(word in lowered for word in BREAKDOWN_INTENT_WORDS):
        return None

    cost_amount = _extract_cost_amount(lowered) or Decimal("0.00")
    mileage_km = _extract_breakdown_mileage(lowered)
    title = _breakdown_title(clean_message)

    extracted_record = AssistantExtractedRecord(
        category="repair",
        title=title,
        description=clean_message,
        occurred_at=date.today(),
        mileage_km=mileage_km,
        cost_amount=cost_amount,
        vendor=None,
    )
    return AssistantMessageResponse(
        assistant_message=f"Записываю поломку в историю: {title}.",
        action=AssistantAction.RECORD_EXTRACTED,
        extracted_record=extracted_record,
    )


def _extract_cost_amount(message: str) -> Decimal | None:
    for marker in COST_MARKERS:
        marker_index = message.find(marker)
        if marker_index == -1:
            continue
        number = _first_int(message[marker_index:])
        if number is not None and number >= 0:
            return Decimal(number).quantize(Decimal("0.01"))
    return None


def _extract_breakdown_mileage(message: str) -> int | None:
    for marker in MILEAGE_MARKERS:
        marker_index = message.find(marker)
        if marker_index == -1:
            continue
        value = _first_int(message[marker_index:])
        if value is not None and value >= 0:
            return value
    return None


def _breakdown_title(message: str) -> str:
    clean = message.strip(" .,!?:;")
    prefixes = (
        "\u0437\u0430\u043f\u0438\u0448\u0438",
        "\u0434\u043e\u0431\u0430\u0432\u044c",
        "\u0443 \u043c\u0435\u043d\u044f",
        "\u043f\u043e\u043b\u043e\u043c\u043a\u0430",
        "\u043f\u043e\u043b\u043e\u043c\u043a\u0443",
    )
    lowered = clean.lower()
    for prefix in prefixes:
        if lowered.startswith(prefix):
            clean = clean[len(prefix) :].strip(" .,!?:;")
            lowered = clean.lower()
    if not clean.lower().startswith(("\u0441\u043b\u043e\u043c", "\u043d\u0435 \u0440\u0430\u0431\u043e\u0442\u0430")):
        clean = f"Поломка: {clean}"
    return clean[:200]


def _format_money(value: Any) -> str:
    amount = _to_decimal(value)
    return f"{amount:,.2f} ₽".replace(",", " ").replace(".00", "")


def _to_decimal(value: Any) -> Decimal:
    if value is None:
        return Decimal("0.00")
    return Decimal(str(value)).quantize(Decimal("0.01"))


def _category_label(category: str) -> str:
    labels = {
        "expense": "Расходы",
        "fuel": "Топливо",
        "maintenance": "ТО",
        "repair": "Ремонт",
        "inspection": "Осмотры",
        "other": "Другое",
    }
    return labels.get(str(category), str(category))


def _extract_record_payload(payload: Mapping[str, Any]) -> dict[str, Any] | None:
    raw_record = payload.get("extracted_record")
    if not isinstance(raw_record, dict):
        raw_record = payload

    filtered_record = {
        key: value for key, value in raw_record.items() if key in ALLOWED_RECORD_FIELDS
    }
    if not filtered_record:
        return None
    return filtered_record


def _clarification_response(message: str) -> AssistantMessageResponse:
    return AssistantMessageResponse(
        assistant_message=message,
        action=AssistantAction.NEEDS_CLARIFICATION,
        record_id=None,
        extracted_record=None,
    )
