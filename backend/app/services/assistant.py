from __future__ import annotations

import json
import re
from collections.abc import Mapping
from datetime import date
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
    return json.dumps(
        {
            "current_date": str(date.today()),
            "car_id": request.car_id,
            "safe_database_context": context,
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
