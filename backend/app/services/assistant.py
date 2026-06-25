from __future__ import annotations

import json
from collections.abc import Mapping
from datetime import date
from typing import Any

from openai import AsyncOpenAI, OpenAIError
from pydantic import ValidationError

from app.core.config import Settings, get_settings
from app.schemas.assistant import (
    AssistantAction,
    AssistantExtractedRecord,
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

SYSTEM_PROMPT = """
You extract car maintenance, repair, inspection, and expense data from a user message.
Return only one valid JSON object. Do not include Markdown or explanations outside JSON.

Allowed JSON shape:
{
  "action": "record_extracted" | "needs_clarification",
  "assistant_message": "short message for the user",
  "extracted_record": {
    "category": "maintenance" | "repair" | "expense" | "inspection" | "other",
    "title": "short title",
    "description": "optional details",
    "occurred_at": "YYYY-MM-DD",
    "mileage_km": 12345,
    "cost_amount": "3500.00",
    "vendor": "optional vendor"
  }
}

Rules:
- Use "record_extracted" only when enough data exists to create a maintenance record.
- Use "needs_clarification" when required data is missing or unclear.
- The extracted_record must contain only allowed fields.
- cost_amount and mileage_km must not be negative.
- If the user says "today", use the current date provided in the user prompt.
""".strip()


async def extract_record_from_message(
    request: AssistantMessageRequest,
    car_context: Mapping[str, Any] | None = None,
    *,
    settings: Settings | None = None,
    client: AsyncOpenAI | None = None,
) -> AssistantMessageResponse:
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
            "car_context": context,
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

    action = payload.get("action")
    assistant_message = payload.get("assistant_message") or payload.get("message")

    if action == AssistantAction.NEEDS_CLARIFICATION:
        return _clarification_response(
            str(assistant_message or "Please provide more details about this car record.")
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
        assistant_message=str(assistant_message or "I extracted the car record details."),
        action=AssistantAction.RECORD_EXTRACTED,
        extracted_record=extracted_record,
    )


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
