from collections.abc import Awaitable, Callable
from pathlib import Path

from fastapi import APIRouter, Depends, File, HTTPException, UploadFile, status
from sqlalchemy.ext.asyncio import AsyncSession

from app.api.deps import get_current_user
from app.core.database import get_db
from app.crud.cars import get_car
from app.models.user import User
from app.schemas.receipt import ReceiptRead, ReceiptScanRequest
from app.services.proverkacheka import (
    ProverkachekaError,
    ProverkachekaNotConfiguredError,
    scan_receipt_qrfile,
    scan_receipt_qrraw,
)


router = APIRouter(prefix="/cars/{car_id}/receipts", tags=["receipts"])
MAX_RECEIPT_QR_FILE_SIZE_BYTES = 5 * 1024 * 1024


@router.post("/scan", response_model=ReceiptRead)
async def scan_receipt(
    car_id: int,
    data: ReceiptScanRequest,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> ReceiptRead:
    await ensure_car_owner(db, current_user, car_id)
    return await _scan_with_provider(lambda: scan_receipt_qrraw(data.qrraw))


@router.post("/scan/file", response_model=ReceiptRead)
async def scan_receipt_file(
    car_id: int,
    file: UploadFile = File(...),
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> ReceiptRead:
    await ensure_car_owner(db, current_user, car_id)
    content_type = file.content_type or "application/octet-stream"
    if not _is_allowed_receipt_file_type(content_type):
        raise HTTPException(
            status_code=status.HTTP_415_UNSUPPORTED_MEDIA_TYPE,
            detail="Only images and PDFs are allowed",
        )

    data = await file.read()
    if not data:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="QR file is empty")
    if len(data) > MAX_RECEIPT_QR_FILE_SIZE_BYTES:
        raise HTTPException(status_code=status.HTTP_413_REQUEST_ENTITY_TOO_LARGE, detail="QR file is too large")

    filename = Path(file.filename or "receipt-qr").name[:255] or "receipt-qr"
    return await _scan_with_provider(
        lambda: scan_receipt_qrfile(
            filename=filename,
            content_type=content_type,
            data=data,
        )
    )


async def ensure_car_owner(db: AsyncSession, current_user: User, car_id: int) -> None:
    car = await get_car(db, current_user.id, car_id)
    if car is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Car not found")


async def _scan_with_provider(scan_call: Callable[[], Awaitable[ReceiptRead]]) -> ReceiptRead:
    try:
        return await scan_call()
    except ProverkachekaNotConfiguredError as exc:
        raise HTTPException(
            status_code=status.HTTP_503_SERVICE_UNAVAILABLE,
            detail=str(exc),
        ) from exc
    except ProverkachekaError as exc:
        if exc.code in {2, 4}:
            status_code = status.HTTP_202_ACCEPTED
        elif exc.code == 3:
            status_code = status.HTTP_429_TOO_MANY_REQUESTS
        elif exc.code == 0:
            status_code = status.HTTP_400_BAD_REQUEST
        else:
            status_code = status.HTTP_502_BAD_GATEWAY
        raise HTTPException(status_code=status_code, detail=str(exc)) from exc


def _is_allowed_receipt_file_type(content_type: str) -> bool:
    return content_type.startswith("image/") or content_type == "application/pdf"
