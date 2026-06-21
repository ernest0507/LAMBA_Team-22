from datetime import date, datetime
from decimal import Decimal

from pydantic import BaseModel, ConfigDict, Field, field_validator

from app.models.maintenance_record import RecordCategory


class MaintenanceRecordBase(BaseModel):
    category: RecordCategory | None = None
    title: str | None = Field(default=None, min_length=1, max_length=200)
    description: str | None = None
    occurred_at: date | None = None
    mileage_km: int | None = Field(default=None, ge=0)
    cost_amount: Decimal = Field(ge=0, max_digits=12, decimal_places=2)
    vendor: str | None = Field(default=None, max_length=200)


class MaintenanceRecordCreate(MaintenanceRecordBase):
    pass


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
    created_at: datetime
    updated_at: datetime


class TimelineItem(BaseModel):
    id: int
    category: str | None
    title: str | None
    occurred_at: date | None
    mileage_km: int | None
    cost_amount: Decimal
