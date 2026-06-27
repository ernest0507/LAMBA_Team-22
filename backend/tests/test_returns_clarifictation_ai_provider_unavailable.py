from types import SimpleNamespace

import pytest
from openai import OpenAIError

from app.core.config import Settings
from app.schemas.assistant import AssistantAction, AssistantMessageRequest
from app.services.assistant import extract_record_from_message

class FailingClient:
    def __init__(self):
        self.chat = SimpleNamespace(
            completions=SimpleNamespace(create=self.create)
        )

    async def create(self, **kwargs):
        raise OpenAIError("AI provider is unavailable")


@pytest.mark.asyncio
async def test_returns_clarifictation_ai_provider_unavailable():
    settings = Settings()
    settings.ai_api_key = "fake-ai"
    settings.ai_base_url = "https://fake-ai.example"
    settings.ai_provider = "timeweb"
    settings.ai_model = "default"

    request = AssistantMessageRequest(
        car_id=1, 
        message="Смена масла сегодня на 3500 рублей"
    )

    result = await extract_record_from_message(
        request, 
        car_context={"make": "Toyota", "model": "Corolla"}, 
        settings=settings, 
        client=FailingClient()
    )


    assert result.action == AssistantAction.NEEDS_CLARIFICATION
    assert result.record_id is None
    assert result.extracted_record is None
    assert "temporarily unavailable" in result.assistant_message.lower()
