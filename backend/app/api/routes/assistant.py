from typing import Any

from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.ext.asyncio import AsyncSession

from app.api.deps import get_current_user
from app.core.database import get_db
from app.crud.cars import get_car
from app.crud.maintenance_records import create_record
from app.models.car import Car
from app.models.user import User
from app.schemas.assistant import (
    AssistantAction,
    AssistantMessageRequest,
    AssistantMessageResponse,
)
from app.schemas.maintenance_record import MaintenanceRecordCreate
from app.services.assistant import extract_record_from_message


router = APIRouter(prefix="/cars/{car_id}/assistant", tags=["assistant"])


@router.post("/messages", response_model=AssistantMessageResponse)
async def create_assistant_message(
    car_id: int,
    data: AssistantMessageRequest,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> AssistantMessageResponse:
    if data.car_id != car_id:
        raise HTTPException(
            status_code=status.HTTP_422_UNPROCESSABLE_ENTITY,
            detail="Request car_id must match path car_id",
        )

    car = await get_car(db, current_user.id, car_id)
    if car is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Car not found")

    assistant_result = await extract_record_from_message(
        data,
        car_context=_build_car_context(car),
    )

    if assistant_result.action != AssistantAction.RECORD_EXTRACTED:
        return assistant_result

    if assistant_result.extracted_record is None:
        return AssistantMessageResponse(
            assistant_message="AI assistant could not extract valid record data.",
            action=AssistantAction.NEEDS_CLARIFICATION,
        )

    record_data = MaintenanceRecordCreate(
        category=assistant_result.extracted_record.category,
        title=assistant_result.extracted_record.title,
        description=assistant_result.extracted_record.description,
        occurred_at=assistant_result.extracted_record.occurred_at,
        mileage_km=assistant_result.extracted_record.mileage_km,
        cost_amount=assistant_result.extracted_record.cost_amount,
        vendor=assistant_result.extracted_record.vendor,
    )
    record = await create_record(db, car_id, record_data)

    return AssistantMessageResponse(
        assistant_message="Record has been saved.",
        action=AssistantAction.RECORD_CREATED,
        record_id=record.id,
        extracted_record=assistant_result.extracted_record,
    )


def _build_car_context(car: Car) -> dict[str, Any]:
    return {
        "id": car.id,
        "make": car.make,
        "model": car.model,
        "year": car.year,
        "current_mileage_km": car.current_mileage_km,
        "color": car.color,
        "body_type": car.body_type,
        "notes": car.notes,
    }
