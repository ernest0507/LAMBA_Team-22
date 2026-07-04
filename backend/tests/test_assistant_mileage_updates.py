import pytest

from app.api.routes import assistant as assistant_route
from app.core.config import Settings
from app.models.car import Car
from app.models.user import User
from app.schemas.assistant import (
    AssistantAction,
    AssistantMessageRequest,
    AssistantMessageResponse,
    AssistantMileageUpdate,
)
from app.services.assistant import extract_record_from_message


class FailingAiClient:
    @property
    def chat(self):
        raise AssertionError("Mileage updates should not call the AI client")


@pytest.mark.asyncio
async def test_extracts_mileage_update_without_ai_client():
    request = AssistantMessageRequest(
        car_id=1,
        message="Поставь текущий пробег 52 300 км",
    )

    result = await extract_record_from_message(
        request,
        car_context={"make": "Toyota", "model": "Corolla"},
        settings=Settings(),
        client=FailingAiClient(),
    )

    assert result.action == AssistantAction.UPDATE_MILEAGE
    assert result.mileage_update is not None
    assert result.mileage_update.current_mileage_km == 52300
    assert result.record_id is None
    assert result.extracted_record is None


@pytest.mark.asyncio
async def test_assistant_route_updates_current_mileage(monkeypatch):
    car = Car(
        id=10,
        owner_id=7,
        make="Toyota",
        model="Corolla",
        year=2020,
        current_mileage_km=41000,
    )
    current_user = User(
        id=7,
        email="owner@example.com",
        password_hash="hash",
    )

    async def fake_get_car(db, owner_id, car_id):
        assert owner_id == current_user.id
        assert car_id == car.id
        return car

    async def fake_update_car(db, car_to_update, data):
        assert car_to_update is car
        car_to_update.current_mileage_km = data.current_mileage_km
        return car_to_update

    async def fake_extract_record_from_message(data, car_context):
        assert car_context["current_mileage_km"] == 41000
        return AssistantMessageResponse(
            assistant_message="Updating current mileage to 52300 km.",
            action=AssistantAction.UPDATE_MILEAGE,
            mileage_update=AssistantMileageUpdate(current_mileage_km=52300),
        )

    monkeypatch.setattr(assistant_route, "get_car", fake_get_car)
    monkeypatch.setattr(assistant_route, "update_car", fake_update_car)
    monkeypatch.setattr(
        assistant_route,
        "extract_record_from_message",
        fake_extract_record_from_message,
    )

    result = await assistant_route.create_assistant_message(
        car_id=car.id,
        data=AssistantMessageRequest(car_id=car.id, message="обнови пробег до 52300"),
        db=object(),
        current_user=current_user,
    )

    assert result.action == AssistantAction.MILEAGE_UPDATED
    assert result.mileage_update is not None
    assert result.mileage_update.current_mileage_km == 52300
    assert result.record_id is None
    assert result.extracted_record is None
    assert car.current_mileage_km == 52300
