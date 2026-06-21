from datetime import date

from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy.ext.asyncio import AsyncSession

from app.api.deps import get_current_user
from app.core.database import get_db
from app.crud.cars import get_car
from app.crud.maintenance_records import (
    create_record,
    delete_record,
    get_record,
    list_records,
    update_record,
)
from app.models.user import User
from app.schemas.maintenance_record import (
    MaintenanceRecordCreate,
    MaintenanceRecordRead,
    MaintenanceRecordUpdate,
    TimelineItem,
)


router = APIRouter(prefix="/cars/{car_id}", tags=["maintenance records"])


async def ensure_car_owner(db: AsyncSession, current_user: User, car_id: int) -> None:
    car = await get_car(db, current_user.id, car_id)
    if not car:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Car not found")


@router.get("/records", response_model=list[MaintenanceRecordRead])
async def read_records(
    car_id: int,
    skip: int = Query(default=0, ge=0),
    limit: int = Query(default=100, ge=1, le=500),
    date_from: date | None = None,
    date_to: date | None = None,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> list[MaintenanceRecordRead]:
    await ensure_car_owner(db, current_user, car_id)
    return await list_records(db, car_id, skip, limit, date_from, date_to)


@router.post("/records", response_model=MaintenanceRecordRead, status_code=status.HTTP_201_CREATED)
async def create_new_record(
    car_id: int,
    data: MaintenanceRecordCreate,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> MaintenanceRecordRead:
    await ensure_car_owner(db, current_user, car_id)
    return await create_record(db, car_id, data)


@router.get("/records/{record_id}", response_model=MaintenanceRecordRead)
async def read_record(
    car_id: int,
    record_id: int,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> MaintenanceRecordRead:
    await ensure_car_owner(db, current_user, car_id)
    record = await get_record(db, car_id, record_id)
    if not record:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Record not found")
    return record


@router.patch("/records/{record_id}", response_model=MaintenanceRecordRead)
async def update_existing_record(
    car_id: int,
    record_id: int,
    data: MaintenanceRecordUpdate,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> MaintenanceRecordRead:
    await ensure_car_owner(db, current_user, car_id)
    record = await get_record(db, car_id, record_id)
    if not record:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Record not found")
    return await update_record(db, record, data)


@router.delete("/records/{record_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_existing_record(
    car_id: int,
    record_id: int,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> None:
    await ensure_car_owner(db, current_user, car_id)
    record = await get_record(db, car_id, record_id)
    if not record:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Record not found")
    await delete_record(db, record)


@router.get("/timeline", response_model=list[TimelineItem])
async def read_timeline(
    car_id: int,
    date_from: date | None = None,
    date_to: date | None = None,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> list[TimelineItem]:
    await ensure_car_owner(db, current_user, car_id)
    records = await list_records(db, car_id, 0, 500, date_from, date_to)
    return [
        TimelineItem(
            id=record.id,
            category=record.category,
            title=record.title,
            occurred_at=record.occurred_at,
            mileage_km=record.mileage_km,
            cost_amount=record.cost_amount,
        )
        for record in records
    ]
