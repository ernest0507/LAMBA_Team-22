from datetime import date, datetime, timezone
from decimal import Decimal
from types import SimpleNamespace

import pytest

from app.api.routes import maintenance_records
from app.models.maintenance_record import RecordCategory
from app.models.user import User
from app.services.statistics import build_car_statistics


def make_user() -> User:
    return User(id=7, email="owner@example.com", password_hash="hash")


def make_record(
    *,
    category: RecordCategory,
    title: str,
    cost_amount: str,
    mileage_km: int,
    occurred_at: date,
):
    return SimpleNamespace(
        id=mileage_km,
        category=category,
        title=title,
        description=None,
        occurred_at=occurred_at,
        mileage_km=mileage_km,
        cost_amount=Decimal(cost_amount),
        vendor=None,
        created_at=datetime.combine(occurred_at, datetime.min.time(), tzinfo=timezone.utc),
    )


@pytest.mark.asyncio
async def test_read_statistics_returns_car_statistics(monkeypatch):
    records = [
        make_record(
            category=RecordCategory.MAINTENANCE,
            title="Oil service",
            cost_amount="3500.00",
            mileage_km=41000,
            occurred_at=date.today(),
        )
    ]
    build_calls = []

    async def fake_ensure_car_owner(db, current_user, car_id):
        assert current_user.id == 7
        assert car_id == 3

    async def fake_list_records(db, car_id, skip, limit):
        assert car_id == 3
        assert skip == 0
        assert limit == 500
        return records

    def fake_build_car_statistics(records_arg):
        build_calls.append(records_arg)
        return build_car_statistics(records_arg)

    monkeypatch.setattr(maintenance_records, "ensure_car_owner", fake_ensure_car_owner)
    monkeypatch.setattr(maintenance_records, "list_records", fake_list_records)
    monkeypatch.setattr(maintenance_records, "build_car_statistics", fake_build_car_statistics)

    result = await maintenance_records.read_statistics(
        car_id=3,
        db=object(),
        current_user=make_user(),
    )

    assert build_calls == [records]
    assert result.month
    assert result.half_year
    assert result.year


def test_build_car_statistics_groups_expenses_by_category():
    today = date.today()
    records = [
        make_record(
            category=RecordCategory.MAINTENANCE,
            title="Oil service",
            cost_amount="3500.00",
            mileage_km=41000,
            occurred_at=today,
        ),
        make_record(
            category=RecordCategory.REPAIR,
            title="Brake repair",
            cost_amount="6500.00",
            mileage_km=43000,
            occurred_at=today,
        ),
    ]

    result = build_car_statistics(records)
    current_month = result.month[-1]

    assert current_month.total_amount == Decimal("10000.00")
    assert current_month.metrics[0].value == "10 000 ₽"
    assert current_month.metrics[1].value == "2 000 км"
    assert [category.key for category in current_month.categories] == ["repair", "maintenance"]
