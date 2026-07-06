from datetime import datetime, timedelta, timezone
from decimal import Decimal
from types import SimpleNamespace

import pytest
from fastapi import HTTPException

from app.api.routes import trips as trips_route
from app.crud import trips as trips_crud
from app.models.user import User
from app.schemas.trip import (
    TripCreate,
    TripFinish,
    TripPointBatchCreate,
    TripPointCreate,
)
from app.services.trip_metrics import TripMetrics


def make_user(user_id: int = 7) -> User:
    return User(id=user_id, email=f"user-{user_id}@example.com", password_hash="hash")


def make_trip(*, trip_id: int = 11, car_id: int = 3, ended: bool = False) -> SimpleNamespace:
    started_at = datetime(2026, 7, 6, 10, 0, tzinfo=timezone.utc)
    ended_at = started_at + timedelta(minutes=30) if ended else None
    return SimpleNamespace(
        id=trip_id,
        car_id=car_id,
        started_at=started_at,
        ended_at=ended_at,
        distance_m=Decimal("1234.50"),
        duration_seconds=1800 if ended else 0,
        average_speed_kmh=Decimal("2.47") if ended else Decimal("0.00"),
        max_speed_kmh=Decimal("40.00") if ended else Decimal("0.00"),
        status="finished" if ended else "active",
        created_at=started_at,
        updated_at=ended_at or started_at,
    )


def make_point(minutes: int = 0) -> TripPointCreate:
    return TripPointCreate(
        latitude=Decimal("55.7512440"),
        longitude=Decimal("37.6184230") + Decimal(minutes) / Decimal("1000"),
        accuracy_m=Decimal("8.50"),
        speed_kmh=Decimal("36.00"),
        recorded_at=datetime(2026, 7, 6, 10, minutes, tzinfo=timezone.utc),
    )


def make_stored_point(point_id: int, trip_id: int, minutes: int = 0) -> SimpleNamespace:
    point = make_point(minutes)
    return SimpleNamespace(
        id=point_id,
        trip_id=trip_id,
        latitude=point.latitude,
        longitude=point.longitude,
        accuracy_m=point.accuracy_m,
        speed_kmh=point.speed_kmh,
        recorded_at=point.recorded_at,
        created_at=point.recorded_at,
    )


@pytest.mark.asyncio
async def test_start_trip_creates_active_trip_for_owned_car(monkeypatch):
    created_trip = make_trip()
    calls = []

    async def fake_ensure_car_owner(db, current_user, car_id):
        calls.append(("owner", current_user.id, car_id))

    async def fake_get_active_trip(db, car_id):
        calls.append(("active", car_id))
        return None

    async def fake_create_trip(db, car_id, data):
        calls.append(("create", car_id, data.started_at))
        return created_trip

    monkeypatch.setattr(trips_route, "ensure_car_owner", fake_ensure_car_owner)
    monkeypatch.setattr(trips_route, "get_active_trip", fake_get_active_trip)
    monkeypatch.setattr(trips_route, "create_trip", fake_create_trip)

    result = await trips_route.start_trip(
        car_id=3,
        data=TripCreate(started_at=created_trip.started_at),
        db=object(),
        current_user=make_user(),
    )

    assert result is created_trip
    assert calls == [
        ("owner", 7, 3),
        ("active", 3),
        ("create", 3, created_trip.started_at),
    ]


@pytest.mark.asyncio
async def test_start_trip_rejects_second_active_trip(monkeypatch):
    async def fake_ensure_car_owner(db, current_user, car_id):
        return None

    async def fake_get_active_trip(db, car_id):
        return make_trip()

    monkeypatch.setattr(trips_route, "ensure_car_owner", fake_ensure_car_owner)
    monkeypatch.setattr(trips_route, "get_active_trip", fake_get_active_trip)

    with pytest.raises(HTTPException) as exc_info:
        await trips_route.start_trip(
            car_id=3,
            data=TripCreate(),
            db=object(),
            current_user=make_user(),
        )

    assert exc_info.value.status_code == 409


@pytest.mark.asyncio
async def test_start_trip_denies_unowned_car(monkeypatch):
    async def fake_get_car(db, owner_id, car_id):
        assert owner_id == 7
        assert car_id == 3
        return None

    monkeypatch.setattr(trips_route, "get_car", fake_get_car)

    with pytest.raises(HTTPException) as exc_info:
        await trips_route.start_trip(
            car_id=3,
            data=TripCreate(),
            db=object(),
            current_user=make_user(),
        )

    assert exc_info.value.status_code == 404


@pytest.mark.asyncio
async def test_append_points_stores_batch_for_active_owned_trip(monkeypatch):
    trip = make_trip()
    batch = TripPointBatchCreate(points=[make_point(0), make_point(1)])
    stored_points = []

    async def fake_get_owned_trip(db, current_user, trip_id):
        assert trip_id == trip.id
        return trip

    async def fake_append_trip_points(db, trip_id, points):
        stored_points.extend(points)
        return points

    monkeypatch.setattr(trips_route, "get_owned_trip", fake_get_owned_trip)
    monkeypatch.setattr(trips_route, "append_trip_points", fake_append_trip_points)

    result = await trips_route.append_points(
        trip_id=trip.id,
        data=batch,
        db=object(),
        current_user=make_user(),
    )

    assert result == batch.points
    assert stored_points == batch.points


@pytest.mark.asyncio
async def test_append_points_rejects_finished_trip(monkeypatch):
    async def fake_get_owned_trip(db, current_user, trip_id):
        return make_trip(ended=True)

    monkeypatch.setattr(trips_route, "get_owned_trip", fake_get_owned_trip)

    with pytest.raises(HTTPException) as exc_info:
        await trips_route.append_points(
            trip_id=11,
            data=TripPointBatchCreate(points=[make_point()]),
            db=object(),
            current_user=make_user(),
        )

    assert exc_info.value.status_code == 409


@pytest.mark.asyncio
async def test_append_points_denies_access_to_other_users_trip(monkeypatch):
    async def fake_get_trip_for_user(db, owner_id, trip_id):
        assert owner_id == 7
        assert trip_id == 11
        return None

    monkeypatch.setattr(trips_route, "get_trip_for_user", fake_get_trip_for_user)

    with pytest.raises(HTTPException) as exc_info:
        await trips_route.append_points(
            trip_id=11,
            data=TripPointBatchCreate(points=[make_point()]),
            db=object(),
            current_user=make_user(),
        )

    assert exc_info.value.status_code == 404


@pytest.mark.asyncio
async def test_finish_active_trip_saves_calculated_metrics(monkeypatch):
    trip = make_trip()
    finished_trip = make_trip(ended=True)
    requested_end = finished_trip.ended_at
    calls = []

    async def fake_get_owned_trip(db, current_user, trip_id):
        return trip

    async def fake_finish_trip(db, trip_arg, ended_at):
        calls.append((trip_arg.id, ended_at))
        return finished_trip

    monkeypatch.setattr(trips_route, "get_owned_trip", fake_get_owned_trip)
    monkeypatch.setattr(trips_route, "finish_trip", fake_finish_trip)

    result = await trips_route.finish_active_trip(
        trip_id=trip.id,
        data=TripFinish(ended_at=requested_end),
        db=object(),
        current_user=make_user(),
    )

    assert result is finished_trip
    assert calls == [(trip.id, requested_end)]


@pytest.mark.asyncio
async def test_finish_trip_uses_metric_service(monkeypatch):
    trip = make_trip()
    points = [make_stored_point(1, trip.id, 0), make_stored_point(2, trip.id, 1)]
    metrics = TripMetrics(
        distance_m=Decimal("98.70"),
        duration_seconds=60,
        average_speed_kmh=Decimal("5.92"),
        max_speed_kmh=Decimal("36.00"),
    )
    calls = []

    class FakeDb:
        async def commit(self):
            calls.append("commit")

        async def refresh(self, refreshed_trip):
            calls.append(("refresh", refreshed_trip.id))

    async def fake_list_trip_points(db, trip_id):
        calls.append(("points", trip_id))
        return points

    def fake_build_trip_metrics(points_arg, *, started_at, ended_at):
        calls.append(("metrics", points_arg, started_at, ended_at))
        return metrics

    monkeypatch.setattr(trips_crud, "list_trip_points", fake_list_trip_points)
    monkeypatch.setattr(trips_crud, "build_trip_metrics", fake_build_trip_metrics)

    ended_at = trip.started_at + timedelta(minutes=1)
    result = await trips_crud.finish_trip(FakeDb(), trip, ended_at)

    assert result is trip
    assert trip.ended_at == ended_at
    assert trip.distance_m == Decimal("98.70")
    assert trip.duration_seconds == 60
    assert trip.average_speed_kmh == Decimal("5.92")
    assert trip.max_speed_kmh == Decimal("36.00")
    assert calls == [
        ("points", trip.id),
        ("metrics", points, trip.started_at, ended_at),
        "commit",
        ("refresh", trip.id),
    ]


@pytest.mark.asyncio
async def test_finish_rejects_finished_trip(monkeypatch):
    async def fake_get_owned_trip(db, current_user, trip_id):
        return make_trip(ended=True)

    monkeypatch.setattr(trips_route, "get_owned_trip", fake_get_owned_trip)

    with pytest.raises(HTTPException) as exc_info:
        await trips_route.finish_active_trip(
            trip_id=11,
            data=TripFinish(),
            db=object(),
            current_user=make_user(),
        )

    assert exc_info.value.status_code == 409
