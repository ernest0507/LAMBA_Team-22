from datetime import date, datetime
from decimal import Decimal
from enum import StrEnum

from sqlalchemy import (
    Date,
    DateTime,
    ForeignKey,
    Integer,
    Numeric,
    String,
    Text,
    UniqueConstraint,
    func,
)
from sqlalchemy.orm import Mapped, mapped_column

from app.core.database import Base


class RecordCategory(StrEnum):
    MAINTENANCE = "maintenance"
    REPAIR = "repair"
    EXPENSE = "expense"
    INSPECTION = "inspection"
    OTHER = "other"


class MaintenanceRecord(Base):
    __tablename__ = "maintenance_records"
    __table_args__ = (
        UniqueConstraint(
            "car_id",
            "receipt_id",
            name="uq_maintenance_records_car_receipt_id",
        ),
    )

    id: Mapped[int] = mapped_column(primary_key=True, index=True)
    car_id: Mapped[int] = mapped_column(ForeignKey("cars.id", ondelete="CASCADE"), index=True)
    category: Mapped[str | None] = mapped_column(String(32), index=True, nullable=True)
    title: Mapped[str | None] = mapped_column(String(200), nullable=True)
    description: Mapped[str | None] = mapped_column(Text, nullable=True)
    occurred_at: Mapped[date | None] = mapped_column(Date, index=True, nullable=True)
    mileage_km: Mapped[int | None] = mapped_column(Integer, nullable=True)
    cost_amount: Mapped[Decimal] = mapped_column(Numeric(12, 2))
    vendor: Mapped[str | None] = mapped_column(String(200), nullable=True)
    receipt_id: Mapped[str | None] = mapped_column(String(64), nullable=True)
    created_at: Mapped[datetime] = mapped_column(DateTime(timezone=True), server_default=func.now())
    updated_at: Mapped[datetime] = mapped_column(
        DateTime(timezone=True), server_default=func.now(), onupdate=func.now()
    )
