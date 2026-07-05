from datetime import datetime, timezone
from decimal import Decimal
from types import SimpleNamespace

import pytest

from app.api.routes import assistant as assistant_route
from app.models.assistant_chat import AssistantMessageRole
from app.models.car import Car
from app.models.maintenance_record import RecordCategory
from app.models.user import User
from app.schemas.assistant import AssistantAction, AssistantMessageRequest, AssistantMessageResponse
from app.services.assistant_context import build_assistant_context


def make_user() -> User:
    return User(id=7, email="owner@example.com", password_hash="secret-hash", full_name="Owner")


def make_car() -> Car:
    return Car(
        id=3,
        owner_id=7,
        make="Toyota",
        model="Corolla",
        year=2020,
        current_mileage_km=41000,
        color="Silver",
        body_type="Sedan",
        notes="Family car",
    )


@pytest.mark.asyncio
async def test_assistant_message_creates_chat_and_persists_messages(monkeypatch):
    car = make_car()
    current_user = make_user()
    saved_messages = []

    async def fake_get_car(db, owner_id, car_id):
        assert owner_id == current_user.id
        assert car_id == car.id
        return car

    async def fake_create_assistant_chat(db, car_id, title):
        assert car_id == car.id
        assert title == "How much did I spend this month?"
        return SimpleNamespace(id=55, car_id=car_id, title=title)

    async def fake_list_messages(db, chat_id):
        assert chat_id == 55
        return []

    async def fake_create_chat_message(db, **kwargs):
        saved_messages.append(kwargs)
        return SimpleNamespace(id=len(saved_messages), **kwargs)

    async def fake_build_assistant_context(db, user, context_car, recent_messages):
        assert user is current_user
        assert context_car is car
        assert recent_messages == []
        return {"car": {"id": context_car.id}, "recent_chat_messages": []}

    async def fake_extract_record_from_message(data, car_context):
        assert car_context["car"]["id"] == car.id
        return AssistantMessageResponse(
            assistant_message="You spent 10 000 RUB this month.",
            action=AssistantAction.MESSAGE,
        )

    monkeypatch.setattr(assistant_route, "get_car", fake_get_car)
    monkeypatch.setattr(assistant_route, "create_assistant_chat", fake_create_assistant_chat)
    monkeypatch.setattr(assistant_route, "list_messages", fake_list_messages)
    monkeypatch.setattr(assistant_route, "create_chat_message", fake_create_chat_message)
    monkeypatch.setattr(assistant_route, "build_assistant_context", fake_build_assistant_context)
    monkeypatch.setattr(assistant_route, "extract_record_from_message", fake_extract_record_from_message)

    result = await assistant_route.create_assistant_message(
        car_id=car.id,
        data=AssistantMessageRequest(
            car_id=car.id,
            message="How much did I spend this month?",
        ),
        db=object(),
        current_user=current_user,
    )

    assert result.chat_id == 55
    assert result.action == AssistantAction.MESSAGE
    assert saved_messages[0]["role"] == AssistantMessageRole.USER
    assert saved_messages[0]["content"] == "How much did I spend this month?"
    assert saved_messages[1]["role"] == AssistantMessageRole.ASSISTANT
    assert saved_messages[1]["content"] == "You spent 10 000 RUB this month."
    assert saved_messages[1]["action"] == AssistantAction.MESSAGE.value


@pytest.mark.asyncio
async def test_build_assistant_context_excludes_sensitive_data(monkeypatch):
    car = make_car()
    current_user = make_user()
    record = SimpleNamespace(
        id=11,
        category=RecordCategory.REPAIR,
        title="Brake repair",
        description="Changed pads",
        occurred_at=datetime(2026, 7, 1, tzinfo=timezone.utc).date(),
        mileage_km=42000,
        cost_amount=Decimal("6500.00"),
        vendor="Local service",
        created_at=datetime(2026, 7, 1, tzinfo=timezone.utc),
    )
    recent_message = SimpleNamespace(
        role=AssistantMessageRole.USER,
        content="What did I repair?",
        action=None,
        record_id=None,
        created_at=datetime(2026, 7, 2, tzinfo=timezone.utc),
    )

    async def fake_list_records(db, car_id, skip, limit):
        assert car_id == car.id
        assert skip == 0
        assert limit == 500
        return [record]

    monkeypatch.setattr("app.services.assistant_context.list_records", fake_list_records)

    context = await build_assistant_context(
        db=object(),
        current_user=current_user,
        car=car,
        recent_messages=[recent_message],
    )

    assert context["user"] == {"id": current_user.id, "full_name": current_user.full_name}
    assert "password_hash" in context["privacy_rules"]["excluded"]
    assert current_user.password_hash not in str(context)
    assert context["car"]["id"] == car.id
    assert context["records_summary"]["total_cost_amount"] == "6500.00"
    assert context["recent_chat_messages"][0]["content"] == "What did I repair?"
