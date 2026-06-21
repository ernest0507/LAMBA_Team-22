from datetime import date

from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.models.maintenance_record import MaintenanceRecord
from app.schemas.maintenance_record import MaintenanceRecordCreate, MaintenanceRecordUpdate


async def list_records(
    db: AsyncSession,
    car_id: int,
    skip: int = 0,
    limit: int = 100,
    date_from: date | None = None,
    date_to: date | None = None,
) -> list[MaintenanceRecord]:
    query = select(MaintenanceRecord).where(MaintenanceRecord.car_id == car_id)
    if date_from:
        query = query.where(MaintenanceRecord.occurred_at >= date_from)
    if date_to:
        query = query.where(MaintenanceRecord.occurred_at <= date_to)

    result = await db.execute(
        query.order_by(MaintenanceRecord.occurred_at.desc(), MaintenanceRecord.id.desc())
        .offset(skip)
        .limit(limit)
    )
    return list(result.scalars().all())


async def get_record(db: AsyncSession, car_id: int, record_id: int) -> MaintenanceRecord | None:
    result = await db.execute(
        select(MaintenanceRecord).where(
            MaintenanceRecord.id == record_id,
            MaintenanceRecord.car_id == car_id,
        )
    )
    return result.scalar_one_or_none()


async def create_record(
    db: AsyncSession, car_id: int, data: MaintenanceRecordCreate
) -> MaintenanceRecord:
    record = MaintenanceRecord(car_id=car_id, **data.model_dump())
    db.add(record)
    await db.commit()
    await db.refresh(record)
    return record


async def update_record(
    db: AsyncSession, record: MaintenanceRecord, data: MaintenanceRecordUpdate
) -> MaintenanceRecord:
    for field, value in data.model_dump(exclude_unset=True).items():
        setattr(record, field, value)
    await db.commit()
    await db.refresh(record)
    return record


async def delete_record(db: AsyncSession, record: MaintenanceRecord) -> None:
    await db.delete(record)
    await db.commit()
