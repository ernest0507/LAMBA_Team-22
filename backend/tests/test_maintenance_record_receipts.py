from datetime import datetime
from decimal import Decimal
from types import SimpleNamespace

import pytest
from fastapi import HTTPException, status
from sqlalchemy.exc import IntegrityError

from app.api.routes import maintenance_records
from app.crud import maintenance_records as maintenance_records_crud
from app.crud.maintenance_records import DuplicateReceiptError
from app.models.maintenance_record import MaintenanceRecord
from app.models.user import User
from app.schemas.maintenance_record import (
    MaintenanceRecordCreate,
    MaintenanceRecordReceiptCreate,
    MaintenanceRecordReceiptItemCreate,
)
from app.services.receipt_identity import (
    receipt_id_from_qrraw,
    receipt_id_from_record_description,
)


RECEIPT_ID = "a" * 64
QRRAW = "t=20260708T1230&s=349.93&fn=9282440300682838&i=46534&fp=1273019065&n=1"


def make_user() -> User:
    return User(id=7, email="owner@example.com", password_hash="hash")


def make_receipt_payload() -> MaintenanceRecordReceiptCreate:
    return MaintenanceRecordReceiptCreate(
        receipt_id=RECEIPT_ID,
        seller_name="Fuel Station",
        seller_inn="1234567890",
        retail_place_address="Main street 1",
        ticket_date=datetime(2026, 7, 8, 12, 30),
        total_amount="349.93",
        fiscal_drive_number="9282440300682838",
        fiscal_document_number="46534",
        fiscal_sign="1273019065",
        items=[
            MaintenanceRecordReceiptItemCreate(
                name="AI-95 fuel",
                quantity="10.500",
                price_amount="33.33",
                total_amount="349.93",
            )
        ],
    )


def make_record_data(
    *,
    receipt: MaintenanceRecordReceiptCreate | None = None,
) -> MaintenanceRecordCreate:
    return MaintenanceRecordCreate(
        category="expense",
        title="Receipt expense",
        description=f"Receipt scanned from QR\nQR: {QRRAW}",
        cost_amount="349.93",
        receipt=receipt,
    )


def test_receipt_id_is_extracted_from_existing_record_description():
    assert receipt_id_from_record_description(make_record_data().description) == (
        receipt_id_from_qrraw(QRRAW)
    )


@pytest.mark.asyncio
async def test_create_record_rejects_existing_receipt_before_insert(monkeypatch):
    async def fake_get_record_by_receipt_id(db, car_id, receipt_id):
        assert car_id == 3
        assert receipt_id == RECEIPT_ID
        return SimpleNamespace(id=42)

    monkeypatch.setattr(
        maintenance_records_crud,
        "get_record_by_receipt_id",
        fake_get_record_by_receipt_id,
    )

    with pytest.raises(DuplicateReceiptError):
        await maintenance_records_crud.create_record(
            object(),
            3,
            make_record_data(),
            receipt_id=RECEIPT_ID,
        )


@pytest.mark.asyncio
async def test_create_record_handles_concurrent_duplicate(monkeypatch):
    lookup_count = 0

    async def fake_get_record_by_receipt_id(db, car_id, receipt_id):
        nonlocal lookup_count
        assert car_id == 3
        assert receipt_id == RECEIPT_ID
        lookup_count += 1
        return None if lookup_count == 1 else SimpleNamespace(id=42)

    class FakeDb:
        rolled_back = False

        def add(self, record):
            pass

        async def commit(self):
            raise IntegrityError("insert", {}, Exception("unique violation"))

        async def rollback(self):
            self.rolled_back = True

    db = FakeDb()
    monkeypatch.setattr(
        maintenance_records_crud,
        "get_record_by_receipt_id",
        fake_get_record_by_receipt_id,
    )

    with pytest.raises(DuplicateReceiptError):
        await maintenance_records_crud.create_record(
            db,
            3,
            make_record_data(),
            receipt_id=RECEIPT_ID,
        )

    assert db.rolled_back is True


@pytest.mark.asyncio
async def test_create_record_allows_same_receipt_for_different_car(monkeypatch):
    existing_receipts = {(3, RECEIPT_ID): SimpleNamespace(id=42)}
    lookups = []

    async def fake_get_record_by_receipt_id(db, car_id, receipt_id):
        lookups.append((car_id, receipt_id))
        return existing_receipts.get((car_id, receipt_id))

    class FakeDb:
        added_record = None

        def add(self, record):
            self.added_record = record

        async def commit(self):
            pass

        async def refresh(self, record):
            pass

    db = FakeDb()
    monkeypatch.setattr(
        maintenance_records_crud,
        "get_record_by_receipt_id",
        fake_get_record_by_receipt_id,
    )

    record = await maintenance_records_crud.create_record(
        db,
        4,
        make_record_data(),
        receipt_id=RECEIPT_ID,
    )

    assert lookups == [(4, RECEIPT_ID)]
    assert record is db.added_record
    assert record.car_id == 4
    assert record.receipt_id == RECEIPT_ID


def test_receipt_unique_constraint_is_scoped_to_car():
    constraints = {
        constraint.name: {column.name for column in constraint.columns}
        for constraint in MaintenanceRecord.__table__.constraints
    }

    assert constraints["uq_maintenance_records_car_receipt_id"] == {
        "car_id",
        "receipt_id",
    }
    assert "uq_maintenance_records_receipt_id" not in constraints


@pytest.mark.asyncio
async def test_create_record_persists_receipt_metadata_and_items(monkeypatch):
    lookups = []

    async def fake_get_record_by_receipt_id(db, car_id, receipt_id):
        lookups.append((car_id, receipt_id))
        return None

    class FakeDb:
        added_record = None

        def add(self, record):
            self.added_record = record

        async def commit(self):
            pass

        async def refresh(self, record):
            record.id = 99
            for item in record.receipt_items:
                item.id = 100
                item.record_id = record.id

    db = FakeDb()
    monkeypatch.setattr(
        maintenance_records_crud,
        "get_record_by_receipt_id",
        fake_get_record_by_receipt_id,
    )

    record = await maintenance_records_crud.create_record(
        db,
        3,
        make_record_data(receipt=make_receipt_payload()),
    )

    assert lookups == [(3, RECEIPT_ID)]
    assert record.receipt_id == RECEIPT_ID
    assert record.receipt_seller_name == "Fuel Station"
    assert record.receipt_seller_inn == "1234567890"
    assert record.receipt_retail_place_address == "Main street 1"
    assert record.receipt_total_amount == Decimal("349.93")
    assert record.receipt_fiscal_drive_number == "9282440300682838"
    assert record.receipt_fiscal_document_number == "46534"
    assert record.receipt_fiscal_sign == "1273019065"
    assert len(record.receipt_items) == 1
    assert record.receipt_items[0].name == "AI-95 fuel"
    assert record.receipt_items[0].quantity == Decimal("10.500")
    assert record.receipt_items[0].price_amount == Decimal("33.33")
    assert record.receipt_items[0].total_amount == Decimal("349.93")


@pytest.mark.asyncio
async def test_timeline_returns_persisted_receipt_details(monkeypatch):
    async def fake_ensure_car_owner(db, current_user, car_id):
        pass

    async def fake_list_records(db, car_id, skip, limit, date_from=None, date_to=None):
        return [
            SimpleNamespace(
                id=9,
                category="expense",
                title="Receipt expense",
                description=None,
                occurred_at=None,
                mileage_km=None,
                cost_amount=Decimal("349.93"),
                vendor="Fuel Station",
                receipt={
                    "receipt_id": RECEIPT_ID,
                    "seller_name": "Fuel Station",
                    "seller_inn": "1234567890",
                    "retail_place_address": "Main street 1",
                    "ticket_date": datetime(2026, 7, 8, 12, 30),
                    "total_amount": Decimal("349.93"),
                    "fiscal_drive_number": "9282440300682838",
                    "fiscal_document_number": "46534",
                    "fiscal_sign": "1273019065",
                    "items": [
                        {
                            "id": 10,
                            "record_id": 9,
                            "name": "AI-95 fuel",
                            "quantity": Decimal("10.500"),
                            "price_amount": Decimal("33.33"),
                            "total_amount": Decimal("349.93"),
                        }
                    ],
                },
            )
        ]

    monkeypatch.setattr(maintenance_records, "ensure_car_owner", fake_ensure_car_owner)
    monkeypatch.setattr(maintenance_records, "list_records", fake_list_records)

    result = await maintenance_records.read_timeline(
        car_id=3,
        db=object(),
        current_user=make_user(),
    )

    assert result[0].receipt is not None
    assert result[0].receipt.seller_name == "Fuel Station"
    assert result[0].receipt.total_amount == Decimal("349.93")
    assert result[0].receipt.items[0].name == "AI-95 fuel"


@pytest.mark.asyncio
async def test_create_record_endpoint_returns_conflict_for_duplicate(monkeypatch):
    async def fake_ensure_car_owner(db, current_user, car_id):
        pass

    async def fake_create_record(db, car_id, data, *, receipt_id):
        assert receipt_id == receipt_id_from_qrraw(QRRAW)
        raise DuplicateReceiptError

    monkeypatch.setattr(maintenance_records, "ensure_car_owner", fake_ensure_car_owner)
    monkeypatch.setattr(maintenance_records, "create_record", fake_create_record)

    with pytest.raises(HTTPException) as exc_info:
        await maintenance_records.create_new_record(
            car_id=3,
            data=make_record_data(),
            db=object(),
            current_user=make_user(),
        )

    assert exc_info.value.status_code == status.HTTP_409_CONFLICT
    assert exc_info.value.detail == "Receipt has already been uploaded"


@pytest.mark.asyncio
async def test_create_record_endpoint_uses_nested_receipt_identity(monkeypatch):
    async def fake_ensure_car_owner(db, current_user, car_id):
        pass

    async def fake_create_record(db, car_id, data, *, receipt_id):
        assert receipt_id == RECEIPT_ID
        assert data.receipt is not None
        assert data.receipt.seller_name == "Fuel Station"
        return SimpleNamespace(id=15)

    data = make_record_data(receipt=make_receipt_payload()).model_copy(
        update={"description": "Receipt metadata is submitted separately"}
    )

    monkeypatch.setattr(maintenance_records, "ensure_car_owner", fake_ensure_car_owner)
    monkeypatch.setattr(maintenance_records, "create_record", fake_create_record)

    result = await maintenance_records.create_new_record(
        car_id=3,
        data=data,
        db=object(),
        current_user=make_user(),
    )

    assert result.id == 15
