from app.core.config import Settings
from app.schemas.assistant import AssistantAction, AssistantMessageRequest
from app.services.assistant import extract_record_from_message


import pytest
import json


class FakeNegativeCostMessage:
    content = json.dumps({
        "action": "record_extracted", 
        "assistent_message": "ok", 
        "extracted_record": {
            "category": "maintenance",
            "title": "Смена масла", 
            "description": "Смена масла",
            "cost_amount": "-100.00",
            "occurred_at": "2026-06-26",
            "mileage_km": 120000,
            "vendor": "Local service"
        }
    })


class FakeNegativeCostChoice:
    message = FakeNegativeCostMessage()


class FakeNegativeCostCompletion:
    choices = [FakeNegativeCostChoice()]


class FakeNegativeCostCompletions:
    async def create(self, **kwards):
        return FakeNegativeCostCompletion()

class FakeNegativeCostChat:
    completions = FakeNegativeCostCompletions()


class FakeNegativeCostClient:
    chat = FakeNegativeCostChat()



@pytest.mark.asyncio
async def test_returns_clarification_negative_cost():
    settings = Settings()
    settings.ai_api_key = "fake-ai"
    settings.ai_base_url = "https://fake-ai.example"
    settings.ai_provider = "timeweb"
    settings.ai_model = "default"

    request = AssistantMessageRequest(
        car_id = 1, 
        message = "Смена масла стоила -3500 рублей"
    )

    result = await extract_record_from_message(
        request, 
        car_context = {"make": "Toyta", "model": "Corolla"}, 
        settings=settings, 
        client=FakeNegativeCostClient()
    )

    assert result.action == AssistantAction.NEEDS_CLARIFICATION
    assert result.record_id is None
    assert result.extracted_record is None
    assert result.assistant_message.strip()
