import json

import pytest

from app.core.config import Settings
from app.schemas.assistant import AssistantMessageRequest
from app.services.assistant import extract_record_from_message


class CapturingCompletions:
    def __init__(self):
        self.messages = None

    async def create(self, **kwargs):
        self.messages = kwargs["messages"]
        return FakeCompletion()


class FakeChat:
    def __init__(self, completions):
        self.completions = completions


class FakeClient:
    def __init__(self):
        self.completions = CapturingCompletions()
        self.chat = FakeChat(self.completions)


class FakeMessage:
    content = json.dumps(
        {
            "action": "needs_clarification",
            "assistant_message": "Please provide the cost.",
        }
    )


class FakeChoice:
    message = FakeMessage()


class FakeCompletion:
    choices = [FakeChoice()]


@pytest.mark.asyncio
async def test_assistant_prompt_includes_recent_conversation():
    settings = Settings()
    settings.ai_api_key = "fake-ai"
    settings.ai_base_url = "https://fake-ai.example"
    settings.ai_provider = "timeweb"
    settings.ai_model = "default"

    client = FakeClient()

    await extract_record_from_message(
        AssistantMessageRequest(car_id=1, message="Now add the cost 5000"),
        car_context={
            "car": {"make": "Toyota", "model": "Corolla"},
            "recent_chat_messages": [
                {
                    "role": "user",
                    "content": "Changed oil yesterday",
                    "action": None,
                    "record_id": None,
                },
                {
                    "role": "assistant",
                    "content": "Please provide the cost.",
                    "action": "needs_clarification",
                    "record_id": None,
                },
            ],
        },
        settings=settings,
        client=client,
    )

    user_prompt = client.completions.messages[1]["content"]
    payload = json.loads(user_prompt)

    assert payload["recent_conversation"][0]["content"] == "Changed oil yesterday"
    assert payload["recent_conversation"][1]["action"] == "needs_clarification"
    assert payload["user_message"] == "Now add the cost 5000"
