from __future__ import annotations

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
from sqlalchemy.orm import Mapped, mapped_column, relationship

from app.core.database import Base


class RecordCategory(StrEnum):
    MAINTENANCE = "maintenance"
    REPAIR = "repair"
    EXPENSE = "expense"
    REFUELING = "заправка"
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
    receipt_seller_name: Mapped[str | None] = mapped_column(String(200), nullable=True)
    receipt_seller_inn: Mapped[str | None] = mapped_column(String(32), nullable=True)
    receipt_retail_place_address: Mapped[str | None] = mapped_column(String(500), nullable=True)
    receipt_ticket_date: Mapped[datetime | None] = mapped_column(
        DateTime(timezone=True), nullable=True
    )
    receipt_total_amount: Mapped[Decimal | None] = mapped_column(Numeric(12, 2), nullable=True)
    receipt_fiscal_drive_number: Mapped[str | None] = mapped_column(String(64), nullable=True)
    receipt_fiscal_document_number: Mapped[str | None] = mapped_column(String(64), nullable=True)
    receipt_fiscal_sign: Mapped[str | None] = mapped_column(String(64), nullable=True)
    created_at: Mapped[datetime] = mapped_column(DateTime(timezone=True), server_default=func.now())
    updated_at: Mapped[datetime] = mapped_column(
        DateTime(timezone=True), server_default=func.now(), onupdate=func.now()
    )
    receipt_items: Mapped[list[MaintenanceRecordReceiptItem]] = relationship(
        back_populates="record",
        cascade="all, delete-orphan",
        lazy="selectin",
    )

    @property
    def receipt(self) -> dict | None:
        if not (
            self.receipt_id
            or self.receipt_seller_name
            or self.receipt_seller_inn
            or self.receipt_retail_place_address
            or self.receipt_ticket_date
            or self.receipt_total_amount is not None
            or self.receipt_fiscal_drive_number
            or self.receipt_fiscal_document_number
            or self.receipt_fiscal_sign
            or self.receipt_items
        ):
            return None

        return {
            "receipt_id": self.receipt_id,
            "seller_name": self.receipt_seller_name,
            "seller_inn": self.receipt_seller_inn,
            "retail_place_address": self.receipt_retail_place_address,
            "ticket_date": self.receipt_ticket_date,
            "total_amount": self.receipt_total_amount,
            "fiscal_drive_number": self.receipt_fiscal_drive_number,
            "fiscal_document_number": self.receipt_fiscal_document_number,
            "fiscal_sign": self.receipt_fiscal_sign,
            "items": self.receipt_items,
        }


class MaintenanceRecordReceiptItem(Base):
    __tablename__ = "maintenance_record_receipt_items"

    id: Mapped[int] = mapped_column(primary_key=True, index=True)
    record_id: Mapped[int] = mapped_column(
        ForeignKey("maintenance_records.id", ondelete="CASCADE"), index=True
    )
    name: Mapped[str | None] = mapped_column(String(500), nullable=True)
    quantity: Mapped[Decimal | None] = mapped_column(Numeric(12, 3), nullable=True)
    price_amount: Mapped[Decimal | None] = mapped_column(Numeric(12, 2), nullable=True)
    total_amount: Mapped[Decimal | None] = mapped_column(Numeric(12, 2), nullable=True)

    record: Mapped[MaintenanceRecord] = relationship(back_populates="receipt_items")
