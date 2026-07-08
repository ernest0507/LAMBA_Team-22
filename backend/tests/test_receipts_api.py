from datetime import datetime
from decimal import Decimal
from io import BytesIO
from types import SimpleNamespace

import pytest
from fastapi import HTTPException, UploadFile, status
from starlette.datastructures import Headers

from app.api.routes import receipts
from app.models.user import User
from app.schemas.receipt import ReceiptRead, ReceiptScanRequest
from app.services.proverkacheka import ProverkachekaError, ProverkachekaNotConfiguredError


def make_user() -> User:
    return User(id=7, email="owner@example.com", password_hash="hash")


def make_receipt() -> ReceiptRead:
    return ReceiptRead(
        provider_code=1,
        status="Receipt data received",
        first=True,
        seller_name="Fuel Station",
        seller_inn="1234567890",
        ticket_date=datetime(2026, 7, 8, 12, 30),
        total_amount=Decimal("349.93"),
        items=[],
        raw={"code": 1},
    )


def make_upload(filename: str, content_type: str, content: bytes = b"qr-image") -> UploadFile:
    return UploadFile(
        file=BytesIO(content),
        filename=filename,
        headers=Headers({"content-type": content_type}),
    )


@pytest.mark.asyncio
async def test_scan_receipt_returns_provider_response_for_owned_car(monkeypatch):
    calls = []
    expected_receipt = make_receipt()

    async def fake_get_car(db, owner_id, car_id):
        calls.append(("car", owner_id, car_id))
        return SimpleNamespace(id=car_id)

    async def fake_scan_receipt_qrraw(qrraw):
        calls.append(("scan", qrraw))
        return expected_receipt

    monkeypatch.setattr(receipts, "get_car", fake_get_car)
    monkeypatch.setattr(receipts, "scan_receipt_qrraw", fake_scan_receipt_qrraw)

    result = await receipts.scan_receipt(
        car_id=3,
        data=ReceiptScanRequest(qrraw="t=20200924T1837&s=349.93&fn=9282440300682838&i=46534&fp=1273019065&n=1"),
        db=object(),
        current_user=make_user(),
    )

    assert result is expected_receipt
    assert calls == [
        ("car", 7, 3),
        ("scan", "t=20200924T1837&s=349.93&fn=9282440300682838&i=46534&fp=1273019065&n=1"),
    ]


@pytest.mark.asyncio
async def test_scan_receipt_file_passes_qr_image_to_provider(monkeypatch):
    calls = []
    expected_receipt = make_receipt()

    async def fake_get_car(db, owner_id, car_id):
        calls.append(("car", owner_id, car_id))
        return SimpleNamespace(id=car_id)

    async def fake_scan_receipt_qrfile(*, filename, content_type, data):
        calls.append(("scan-file", filename, content_type, data))
        return expected_receipt

    monkeypatch.setattr(receipts, "get_car", fake_get_car)
    monkeypatch.setattr(receipts, "scan_receipt_qrfile", fake_scan_receipt_qrfile)

    result = await receipts.scan_receipt_file(
        car_id=3,
        file=make_upload("../receipt.png", "image/png", b"png-bytes"),
        db=object(),
        current_user=make_user(),
    )

    assert result is expected_receipt
    assert calls == [
        ("car", 7, 3),
        ("scan-file", "receipt.png", "image/png", b"png-bytes"),
    ]


@pytest.mark.asyncio
async def test_scan_receipt_file_rejects_unsupported_type(monkeypatch):
    async def fake_get_car(db, owner_id, car_id):
        return SimpleNamespace(id=car_id)

    async def fake_scan_receipt_qrfile(*, filename, content_type, data):
        raise AssertionError("unsupported files must not be sent to provider")

    monkeypatch.setattr(receipts, "get_car", fake_get_car)
    monkeypatch.setattr(receipts, "scan_receipt_qrfile", fake_scan_receipt_qrfile)

    with pytest.raises(HTTPException) as exc_info:
        await receipts.scan_receipt_file(
            car_id=3,
            file=make_upload("receipt.txt", "text/plain", b"not-a-qr"),
            db=object(),
            current_user=make_user(),
        )

    assert exc_info.value.status_code == status.HTTP_415_UNSUPPORTED_MEDIA_TYPE
    assert exc_info.value.detail == "Only images and PDFs are allowed"


@pytest.mark.asyncio
async def test_scan_receipt_rejects_unowned_car(monkeypatch):
    async def fake_get_car(db, owner_id, car_id):
        return None

    async def fake_scan_receipt_qrraw(qrraw):
        raise AssertionError("unowned car must not scan receipts")

    monkeypatch.setattr(receipts, "get_car", fake_get_car)
    monkeypatch.setattr(receipts, "scan_receipt_qrraw", fake_scan_receipt_qrraw)

    with pytest.raises(HTTPException) as exc_info:
        await receipts.scan_receipt(
            car_id=3,
            data=ReceiptScanRequest(qrraw="t=20200924T1837&s=349.93&fn=9282440300682838&i=46534&fp=1273019065&n=1"),
            db=object(),
            current_user=make_user(),
        )

    assert exc_info.value.status_code == status.HTTP_404_NOT_FOUND
    assert exc_info.value.detail == "Car not found"


@pytest.mark.asyncio
async def test_scan_receipt_maps_missing_token_to_service_unavailable(monkeypatch):
    async def fake_get_car(db, owner_id, car_id):
        return SimpleNamespace(id=car_id)

    async def fake_scan_receipt_qrraw(qrraw):
        raise ProverkachekaNotConfiguredError("Proverkacheka API token is not configured")

    monkeypatch.setattr(receipts, "get_car", fake_get_car)
    monkeypatch.setattr(receipts, "scan_receipt_qrraw", fake_scan_receipt_qrraw)

    with pytest.raises(HTTPException) as exc_info:
        await receipts.scan_receipt(
            car_id=3,
            data=ReceiptScanRequest(qrraw="t=20200924T1837&s=349.93&fn=9282440300682838&i=46534&fp=1273019065&n=1"),
            db=object(),
            current_user=make_user(),
        )

    assert exc_info.value.status_code == status.HTTP_503_SERVICE_UNAVAILABLE
    assert exc_info.value.detail == "Proverkacheka API token is not configured"


@pytest.mark.asyncio
@pytest.mark.parametrize(
    ("error_code", "expected_status"),
    [
        (0, status.HTTP_400_BAD_REQUEST),
        (2, status.HTTP_202_ACCEPTED),
        (3, status.HTTP_429_TOO_MANY_REQUESTS),
        (5, status.HTTP_502_BAD_GATEWAY),
    ],
)
async def test_scan_receipt_maps_provider_error_codes(monkeypatch, error_code, expected_status):
    async def fake_get_car(db, owner_id, car_id):
        return SimpleNamespace(id=car_id)

    async def fake_scan_receipt_qrraw(qrraw):
        raise ProverkachekaError("provider error", code=error_code)

    monkeypatch.setattr(receipts, "get_car", fake_get_car)
    monkeypatch.setattr(receipts, "scan_receipt_qrraw", fake_scan_receipt_qrraw)

    with pytest.raises(HTTPException) as exc_info:
        await receipts.scan_receipt(
            car_id=3,
            data=ReceiptScanRequest(qrraw="t=20200924T1837&s=349.93&fn=9282440300682838&i=46534&fp=1273019065&n=1"),
            db=object(),
            current_user=make_user(),
        )

    assert exc_info.value.status_code == expected_status
    assert exc_info.value.detail == "provider error"
