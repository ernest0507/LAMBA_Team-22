from datetime import date
from pathlib import Path

from fastapi import APIRouter, Depends, File, HTTPException, Query, Response, UploadFile, status
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
from app.crud.record_photos import (
    count_photos,
    create_photo,
    delete_photo,
    get_photo,
    list_photos,
)
from app.models.maintenance_record import MaintenanceRecord
from app.models.maintenance_record_photo import MaintenanceRecordPhoto
from app.models.user import User
from app.schemas.maintenance_record import (
    MaintenanceRecordCreate,
    MaintenanceRecordRead,
    MaintenanceRecordUpdate,
    TimelineItem,
)
from app.schemas.record_photo import RecordPhotoRead
from app.schemas.statistics import CarStatistics
from app.services.statistics import build_car_statistics


router = APIRouter(prefix="/cars/{car_id}", tags=["maintenance records"])
MAX_RECORD_PHOTOS = 3
MAX_PHOTO_SIZE_BYTES = 5 * 1024 * 1024


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


@router.post("/records/{record_id}/photos", response_model=list[RecordPhotoRead], status_code=status.HTTP_201_CREATED)
async def upload_record_photos(
    car_id: int,
    record_id: int,
    files: list[UploadFile] = File(...),
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> list[RecordPhotoRead]:
    await _get_owned_record(db, current_user, car_id, record_id)
    if not files:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="At least one photo is required")

    existing_count = await count_photos(db, record_id)
    if existing_count + len(files) > MAX_RECORD_PHOTOS:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail=f"A record can have at most {MAX_RECORD_PHOTOS} photos",
        )

    prepared_files: list[tuple[str, str, bytes]] = []
    for file in files:
        content_type = file.content_type or "application/octet-stream"
        if not content_type.startswith("image/"):
            raise HTTPException(status_code=status.HTTP_415_UNSUPPORTED_MEDIA_TYPE, detail="Only images are allowed")

        data = await file.read()
        if not data:
            raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Photo file is empty")
        if len(data) > MAX_PHOTO_SIZE_BYTES:
            raise HTTPException(status_code=status.HTTP_413_REQUEST_ENTITY_TOO_LARGE, detail="Photo is too large")

        filename = Path(file.filename or "photo").name[:255] or "photo"
        prepared_files.append((filename, content_type, data))

    created_photos = [
        await create_photo(
            db,
            record_id=record_id,
            filename=filename,
            content_type=content_type,
            data=data,
        )
        for filename, content_type, data in prepared_files
    ]
    return [_photo_read(car_id, record_id, photo) for photo in created_photos]


@router.get("/records/{record_id}/photos", response_model=list[RecordPhotoRead])
async def read_record_photos(
    car_id: int,
    record_id: int,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> list[RecordPhotoRead]:
    await _get_owned_record(db, current_user, car_id, record_id)
    photos = await list_photos(db, record_id)
    return [_photo_read(car_id, record_id, photo) for photo in photos]


@router.get("/records/{record_id}/photos/{photo_id}")
async def read_record_photo_file(
    car_id: int,
    record_id: int,
    photo_id: int,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> Response:
    await _get_owned_record(db, current_user, car_id, record_id)
    photo = await get_photo(db, record_id, photo_id)
    if photo is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Photo not found")
    return Response(content=photo.data, media_type=photo.content_type)


@router.delete("/records/{record_id}/photos/{photo_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_record_photo(
    car_id: int,
    record_id: int,
    photo_id: int,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> None:
    await _get_owned_record(db, current_user, car_id, record_id)
    photo = await get_photo(db, record_id, photo_id)
    if photo is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Photo not found")
    await delete_photo(db, photo)


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


@router.get("/statistics", response_model=CarStatistics)
async def read_statistics(
    car_id: int,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> CarStatistics:
    await ensure_car_owner(db, current_user, car_id)
    records = await list_records(db, car_id, 0, 500)
    return build_car_statistics(records)


async def _get_owned_record(
    db: AsyncSession,
    current_user: User,
    car_id: int,
    record_id: int,
) -> MaintenanceRecord:
    await ensure_car_owner(db, current_user, car_id)
    record = await get_record(db, car_id, record_id)
    if record is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Record not found")
    return record


def _photo_read(car_id: int, record_id: int, photo: MaintenanceRecordPhoto) -> RecordPhotoRead:
    return RecordPhotoRead(
        id=photo.id,
        record_id=photo.record_id,
        filename=photo.filename,
        content_type=photo.content_type,
        size_bytes=photo.size_bytes,
        created_at=photo.created_at,
        url=f"/api/v1/cars/{car_id}/records/{record_id}/photos/{photo.id}",
    )
