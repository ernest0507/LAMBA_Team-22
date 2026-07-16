from datetime import datetime
from decimal import Decimal
from typing import Any

from pydantic import BaseModel, Field


class ReceiptScanRequest(BaseModel):
    qrraw: str = Field(min_length=1, max_length=2048)


class ReceiptItem(BaseModel):
    name: str | None = None
    price_amount: Decimal | None = Field(default=None, ge=0, max_digits=12, decimal_places=2)
    quantity: Decimal | None = Field(default=None, ge=0, max_digits=12, decimal_places=3)
    total_amount: Decimal | None = Field(default=None, ge=0, max_digits=12, decimal_places=2)
    raw: dict[str, Any] = Field(default_factory=dict)


class ReceiptRead(BaseModel):
    receipt_id: str = Field(pattern=r"^[0-9a-f]{64}$")
    provider_code: int
    status: str
    first: bool | None = None
    seller_name: str | None = None
    seller_inn: str | None = None
    retail_place_address: str | None = None
    ticket_date: datetime | None = None
    request_number: int | None = None
    shift_number: int | None = None
    operator: str | None = None
    operation_type: int | None = None
    total_amount: Decimal | None = Field(default=None, ge=0, max_digits=12, decimal_places=2)
    cash_total_amount: Decimal | None = Field(default=None, ge=0, max_digits=12, decimal_places=2)
    ecash_total_amount: Decimal | None = Field(default=None, ge=0, max_digits=12, decimal_places=2)
    fiscal_drive_number: str | None = None
    fiscal_document_number: str | None = None
    fiscal_sign: str | None = None
    items: list[ReceiptItem] = Field(default_factory=list)
    raw: dict[str, Any] = Field(default_factory=dict)
