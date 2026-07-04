from sqlalchemy import func, select
from sqlalchemy.ext.asyncio import AsyncSession

from app.models.maintenance_record_photo import MaintenanceRecordPhoto


async def count_photos(db: AsyncSession, record_id: int) -> int:
    result = await db.execute(
        select(func.count(MaintenanceRecordPhoto.id)).where(
            MaintenanceRecordPhoto.record_id == record_id
        )
    )
    return int(result.scalar_one())


async def create_photo(
    db: AsyncSession,
    record_id: int,
    filename: str,
    content_type: str,
    data: bytes,
) -> MaintenanceRecordPhoto:
    photo = MaintenanceRecordPhoto(
        record_id=record_id,
        filename=filename,
        content_type=content_type,
        size_bytes=len(data),
        data=data,
    )
    db.add(photo)
    await db.commit()
    await db.refresh(photo)
    return photo


async def list_photos(db: AsyncSession, record_id: int) -> list[MaintenanceRecordPhoto]:
    result = await db.execute(
        select(MaintenanceRecordPhoto)
        .where(MaintenanceRecordPhoto.record_id == record_id)
        .order_by(MaintenanceRecordPhoto.created_at.asc(), MaintenanceRecordPhoto.id.asc())
    )
    return list(result.scalars().all())


async def get_photo(
    db: AsyncSession,
    record_id: int,
    photo_id: int,
) -> MaintenanceRecordPhoto | None:
    result = await db.execute(
        select(MaintenanceRecordPhoto).where(
            MaintenanceRecordPhoto.id == photo_id,
            MaintenanceRecordPhoto.record_id == record_id,
        )
    )
    return result.scalar_one_or_none()


async def delete_photo(db: AsyncSession, photo: MaintenanceRecordPhoto) -> None:
    await db.delete(photo)
    await db.commit()
