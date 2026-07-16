from datetime import date

from sqlalchemy import select
from sqlalchemy.exc import IntegrityError
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import selectinload

from app.models.maintenance_record import MaintenanceRecord, MaintenanceRecordReceiptItem
from app.schemas.maintenance_record import MaintenanceRecordCreate, MaintenanceRecordUpdate


class DuplicateReceiptError(Exception):
    pass


async def list_records(
    db: AsyncSession,
    car_id: int,
    skip: int = 0,
    limit: int = 100,
    date_from: date | None = None,
    date_to: date | None = None,
) -> list[MaintenanceRecord]:
    query = (
        select(MaintenanceRecord)
        .options(selectinload(MaintenanceRecord.receipt_items))
        .where(MaintenanceRecord.car_id == car_id)
    )
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
        select(MaintenanceRecord)
        .options(selectinload(MaintenanceRecord.receipt_items))
        .where(
            MaintenanceRecord.id == record_id,
            MaintenanceRecord.car_id == car_id,
        )
    )
    return result.scalar_one_or_none()


async def get_record_by_receipt_id(
    db: AsyncSession, car_id: int, receipt_id: str
) -> MaintenanceRecord | None:
    result = await db.execute(
        select(MaintenanceRecord)
        .options(selectinload(MaintenanceRecord.receipt_items))
        .where(
            MaintenanceRecord.car_id == car_id,
            MaintenanceRecord.receipt_id == receipt_id,
        )
    )
    return result.scalar_one_or_none()


async def create_record(
    db: AsyncSession,
    car_id: int,
    data: MaintenanceRecordCreate,
    *,
    receipt_id: str | None = None,
) -> MaintenanceRecord:
    if receipt_id is None and data.receipt is not None:
        receipt_id = data.receipt.receipt_id
    if receipt_id and await get_record_by_receipt_id(db, car_id, receipt_id):
        raise DuplicateReceiptError

    record = MaintenanceRecord(
        car_id=car_id,
        **data.model_dump(exclude={"receipt"}),
        **_receipt_model_fields(data, receipt_id),
    )
    if data.receipt:
        record.receipt_items = [
            MaintenanceRecordReceiptItem(**item.model_dump())
            for item in data.receipt.items
        ]
    db.add(record)
    try:
        await db.commit()
    except IntegrityError as exc:
        await db.rollback()
        if receipt_id and await get_record_by_receipt_id(db, car_id, receipt_id):
            raise DuplicateReceiptError from exc
        raise
    await db.refresh(record)
    return record


def _receipt_model_fields(
    data: MaintenanceRecordCreate,
    receipt_id: str | None,
) -> dict[str, object]:
    if data.receipt is None:
        return {"receipt_id": receipt_id}

    receipt = data.receipt
    return {
        "receipt_id": receipt_id,
        "receipt_seller_name": receipt.seller_name,
        "receipt_seller_inn": receipt.seller_inn,
        "receipt_retail_place_address": receipt.retail_place_address,
        "receipt_ticket_date": receipt.ticket_date,
        "receipt_total_amount": receipt.total_amount,
        "receipt_fiscal_drive_number": receipt.fiscal_drive_number,
        "receipt_fiscal_document_number": receipt.fiscal_document_number,
        "receipt_fiscal_sign": receipt.fiscal_sign,
    }


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
