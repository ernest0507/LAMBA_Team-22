from datetime import datetime
from decimal import Decimal

from pydantic import BaseModel, ConfigDict, Field


class TripPointBase(BaseModel):
    latitude: Decimal = Field(ge=-90, le=90, max_digits=10, decimal_places=7)
    longitude: Decimal = Field(ge=-180, le=180, max_digits=10, decimal_places=7)
    accuracy_m: Decimal | None = Field(default=None, ge=0, max_digits=8, decimal_places=2)
    speed_kmh: Decimal | None = Field(default=None, ge=0, max_digits=8, decimal_places=2)
    recorded_at: datetime


class TripPointCreate(TripPointBase):
    pass


class TripPointRead(TripPointBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
    trip_id: int
    created_at: datetime


class TripPointBatchCreate(BaseModel):
    points: list[TripPointCreate] = Field(min_length=1)


class TripBase(BaseModel):
    started_at: datetime
    ended_at: datetime | None = None
    distance_m: Decimal = Field(ge=0, max_digits=12, decimal_places=2)
    duration_seconds: int = Field(ge=0)
    average_speed_kmh: Decimal = Field(ge=0, max_digits=8, decimal_places=2)
    max_speed_kmh: Decimal = Field(ge=0, max_digits=8, decimal_places=2)


class TripCreate(BaseModel):
    started_at: datetime | None = None


class TripFinish(BaseModel):
    ended_at: datetime | None = None


class TripRead(TripBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
    car_id: int
    status: str
    created_at: datetime
    updated_at: datetime


class TripWithPoints(TripRead):
    points: list[TripPointRead] = Field(default_factory=list)
