from datetime import datetime, timezone
from io import BytesIO
from types import SimpleNamespace

import pytest
from fastapi import HTTPException, UploadFile, status
from starlette.datastructures import Headers

from app.api.routes import maintenance_records
from app.models.user import User


def make_upload(filename: str, content_type: str, content: bytes = b"image-bytes") -> UploadFile:
    return UploadFile(
        file=BytesIO(content),
        filename=filename,
        headers=Headers({"content-type": content_type}),
    )


def make_user() -> User:
    return User(id=7, email="owner@example.com", password_hash="hash")


@pytest.mark.asyncio
async def test_upload_record_photos_returns_photo_metadata(monkeypatch):
    created_payloads = []

    async def fake_get_owned_record(db, current_user, car_id, record_id):
        assert current_user.id == 7
        assert car_id == 3
        assert record_id == 42
        return object()

    async def fake_count_photos(db, record_id):
        assert record_id == 42
        return 0

    async def fake_create_photo(db, record_id, filename, content_type, data):
        created_payloads.append((record_id, filename, content_type, data))
        return SimpleNamespace(
            id=11,
            record_id=record_id,
            filename=filename,
            content_type=content_type,
            size_bytes=len(data),
            data=data,
            created_at=datetime(2026, 7, 4, tzinfo=timezone.utc),
        )

    monkeypatch.setattr(maintenance_records, "_get_owned_record", fake_get_owned_record)
    monkeypatch.setattr(maintenance_records, "count_photos", fake_count_photos)
    monkeypatch.setattr(maintenance_records, "create_photo", fake_create_photo)

    result = await maintenance_records.upload_record_photos(
        car_id=3,
        record_id=42,
        files=[make_upload("receipt.png", "image/png", b"receipt-image")],
        db=object(),
        current_user=make_user(),
    )

    assert len(result) == 1
    assert result[0].filename == "receipt.png"
    assert result[0].content_type == "image/png"
    assert result[0].size_bytes == len(b"receipt-image")
    assert result[0].url == "/api/v1/cars/3/records/42/photos/11"
    assert created_payloads == [(42, "receipt.png", "image/png", b"receipt-image")]


@pytest.mark.asyncio
async def test_upload_record_photos_rejects_non_image_upload(monkeypatch):
    async def fake_get_owned_record(db, current_user, car_id, record_id):
        return object()

    async def fake_count_photos(db, record_id):
        return 0

    monkeypatch.setattr(maintenance_records, "_get_owned_record", fake_get_owned_record)
    monkeypatch.setattr(maintenance_records, "count_photos", fake_count_photos)

    with pytest.raises(HTTPException) as exc_info:
        await maintenance_records.upload_record_photos(
            car_id=3,
            record_id=42,
            files=[make_upload("notes.txt", "text/plain", b"not-image")],
            db=object(),
            current_user=make_user(),
        )

    assert exc_info.value.status_code == status.HTTP_415_UNSUPPORTED_MEDIA_TYPE
    assert exc_info.value.detail == "Only images are allowed"


@pytest.mark.asyncio
async def test_upload_record_photos_rejects_more_than_three_total(monkeypatch):
    async def fake_get_owned_record(db, current_user, car_id, record_id):
        return object()

    async def fake_count_photos(db, record_id):
        return 2

    monkeypatch.setattr(maintenance_records, "_get_owned_record", fake_get_owned_record)
    monkeypatch.setattr(maintenance_records, "count_photos", fake_count_photos)

    with pytest.raises(HTTPException) as exc_info:
        await maintenance_records.upload_record_photos(
            car_id=3,
            record_id=42,
            files=[
                make_upload("first.png", "image/png"),
                make_upload("second.png", "image/png"),
            ],
            db=object(),
            current_user=make_user(),
        )

    assert exc_info.value.status_code == status.HTTP_400_BAD_REQUEST
    assert "at most 3 photos" in exc_info.value.detail
