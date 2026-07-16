from types import SimpleNamespace

import pytest
from fastapi import HTTPException, status
from sqlalchemy.exc import IntegrityError

from app.api.routes import maintenance_records
from app.crud import maintenance_records as maintenance_records_crud
from app.crud.maintenance_records import DuplicateReceiptError
from app.models.maintenance_record import MaintenanceRecord
from app.models.user import User
from app.schemas.maintenance_record import MaintenanceRecordCreate
from app.services.receipt_identity import (
    receipt_id_from_qrraw,
    receipt_id_from_record_description,
)


RECEIPT_ID = "a" * 64
QRRAW = "t=20260708T1230&s=349.93&fn=9282440300682838&i=46534&fp=1273019065&n=1"


def make_user() -> User:
    return User(id=7, email="owner@example.com", password_hash="hash")


def make_record_data() -> MaintenanceRecordCreate:
    return MaintenanceRecordCreate(
        category="expense",
        title="Receipt expense",
        description=f"Receipt scanned from QR\nQR: {QRRAW}",
        cost_amount="349.93",
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
