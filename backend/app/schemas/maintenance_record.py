from datetime import date, datetime
from decimal import Decimal

from pydantic import BaseModel, ConfigDict, Field, field_validator

from app.models.maintenance_record import RecordCategory


class MaintenanceRecordReceiptItemBase(BaseModel):
    name: str | None = Field(default=None, max_length=500)
    quantity: Decimal | None = Field(default=None, ge=0, max_digits=12, decimal_places=3)
    price_amount: Decimal | None = Field(default=None, ge=0, max_digits=12, decimal_places=2)
    total_amount: Decimal | None = Field(default=None, ge=0, max_digits=12, decimal_places=2)


class MaintenanceRecordReceiptItemCreate(MaintenanceRecordReceiptItemBase):
    pass


class MaintenanceRecordReceiptItemRead(MaintenanceRecordReceiptItemBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
    record_id: int


class MaintenanceRecordReceiptBase(BaseModel):
    receipt_id: str | None = Field(default=None, pattern=r"^[0-9a-f]{64}$")
    seller_name: str | None = Field(default=None, max_length=200)
    seller_inn: str | None = Field(default=None, max_length=32)
    retail_place_address: str | None = Field(default=None, max_length=500)
    ticket_date: datetime | None = None
    total_amount: Decimal | None = Field(default=None, ge=0, max_digits=12, decimal_places=2)
    fiscal_drive_number: str | None = Field(default=None, max_length=64)
    fiscal_document_number: str | None = Field(default=None, max_length=64)
    fiscal_sign: str | None = Field(default=None, max_length=64)


class MaintenanceRecordReceiptCreate(MaintenanceRecordReceiptBase):
    receipt_id: str = Field(pattern=r"^[0-9a-f]{64}$")
    items: list[MaintenanceRecordReceiptItemCreate] = Field(default_factory=list)


class MaintenanceRecordReceiptRead(MaintenanceRecordReceiptBase):
    model_config = ConfigDict(from_attributes=True)

    items: list[MaintenanceRecordReceiptItemRead] = Field(default_factory=list)


class MaintenanceRecordBase(BaseModel):
    category: RecordCategory | None = None
    title: str | None = Field(default=None, min_length=1, max_length=200)
    description: str | None = None
    occurred_at: date | None = None
    mileage_km: int | None = Field(default=None, ge=0)
    cost_amount: Decimal = Field(ge=0, max_digits=12, decimal_places=2)
    vendor: str | None = Field(default=None, max_length=200)


class MaintenanceRecordCreate(MaintenanceRecordBase):
    receipt: MaintenanceRecordReceiptCreate | None = None


class MaintenanceRecordUpdate(BaseModel):
    category: RecordCategory | None = None
    title: str | None = Field(default=None, min_length=1, max_length=200)
    description: str | None = None
    occurred_at: date | None = None
    mileage_km: int | None = Field(default=None, ge=0)
    cost_amount: Decimal | None = Field(default=None, ge=0, max_digits=12, decimal_places=2)
    vendor: str | None = Field(default=None, max_length=200)

    @field_validator("cost_amount")
    @classmethod
    def validate_cost_amount(cls, value: Decimal | None) -> Decimal:
        if value is None:
            raise ValueError("cost_amount cannot be null")
        return value


class MaintenanceRecordRead(MaintenanceRecordBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
    car_id: int
    receipt_id: str | None = None
    receipt: MaintenanceRecordReceiptRead | None = None
    created_at: datetime
    updated_at: datetime


class TimelineItem(BaseModel):
    id: int
    category: str | None
    title: str | None
    description: str | None = None
    occurred_at: date | None
    mileage_km: int | None
    cost_amount: Decimal
    vendor: str | None = None
    receipt: MaintenanceRecordReceiptRead | None = None
