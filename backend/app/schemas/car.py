from datetime import datetime

from pydantic import BaseModel, ConfigDict, Field


class CarBase(BaseModel):
    make: str | None = Field(default=None, max_length=100)
    model: str = Field(min_length=1, max_length=100)
    year: int = Field(ge=1950, le=2100)
    current_mileage_km: int = Field(ge=0)
    color: str | None = Field(default=None, max_length=32)
    body_type: str | None = Field(default=None, max_length=32)
    notes: str | None = Field(default=None, max_length=1000)


class CarCreate(CarBase):
    pass


class CarUpdate(BaseModel):
    make: str | None = Field(default=None, max_length=100)
    model: str | None = Field(default=None, min_length=1, max_length=100)
    year: int | None = Field(default=None, ge=1950, le=2100)
    current_mileage_km: int | None = Field(default=None, ge=0)
    color: str | None = Field(default=None, max_length=32)
    body_type: str | None = Field(default=None, max_length=32)
    notes: str | None = Field(default=None, max_length=1000)


class CarRead(CarBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
    owner_id: int
    created_at: datetime
    updated_at: datetime
