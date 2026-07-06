from datetime import datetime, timezone
from typing import Iterable

from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.models.car import Car
from app.models.trip import Trip, TripPoint
from app.schemas.trip import TripCreate, TripListFilter, TripPointCreate
from app.services.trip_metrics import build_trip_metrics


async def get_active_trip(db: AsyncSession, car_id: int) -> Trip | None:
    result = await db.execute(
        select(Trip)
        .where(Trip.car_id == car_id, Trip.ended_at.is_(None))
        .order_by(Trip.started_at.desc(), Trip.id.desc())
        .limit(1)
    )
    return result.scalar_one_or_none()


async def get_trip_for_user(db: AsyncSession, owner_id: int, trip_id: int) -> Trip | None:
    result = await db.execute(
        select(Trip)
        .join(Car, Trip.car_id == Car.id)
        .where(Trip.id == trip_id, Car.owner_id == owner_id)
    )
    return result.scalar_one_or_none()


async def list_trips(
    db: AsyncSession,
    car_id: int,
    state: TripListFilter = TripListFilter.ALL,
    skip: int = 0,
    limit: int = 100,
) -> list[Trip]:
    query = select(Trip).where(Trip.car_id == car_id)
    if state == TripListFilter.ACTIVE:
        query = query.where(Trip.ended_at.is_(None))
    elif state == TripListFilter.FINISHED:
        query = query.where(Trip.ended_at.is_not(None))

    result = await db.execute(
        query.order_by(Trip.started_at.desc(), Trip.id.desc()).offset(skip).limit(limit)
    )
    return list(result.scalars().all())


async def create_trip(db: AsyncSession, car_id: int, data: TripCreate) -> Trip:
    trip = Trip(
        car_id=car_id,
        started_at=data.started_at or datetime.now(timezone.utc),
    )
    db.add(trip)
    await db.commit()
    await db.refresh(trip)
    return trip


async def append_trip_points(
    db: AsyncSession,
    trip_id: int,
    points: Iterable[TripPointCreate],
) -> list[TripPoint]:
    created_points = [
        TripPoint(trip_id=trip_id, **point.model_dump())
        for point in points
    ]
    db.add_all(created_points)
    await db.commit()
    for point in created_points:
        await db.refresh(point)
    return created_points


async def list_trip_points(db: AsyncSession, trip_id: int) -> list[TripPoint]:
    result = await db.execute(
        select(TripPoint)
        .where(TripPoint.trip_id == trip_id)
        .order_by(TripPoint.recorded_at.asc(), TripPoint.id.asc())
    )
    return list(result.scalars().all())


async def finish_trip(db: AsyncSession, trip: Trip, ended_at: datetime | None = None) -> Trip:
    trip.ended_at = ended_at or datetime.now(timezone.utc)
    points = await list_trip_points(db, trip.id)
    metrics = build_trip_metrics(points, started_at=trip.started_at, ended_at=trip.ended_at)
    trip.distance_m = metrics.distance_m
    trip.duration_seconds = metrics.duration_seconds
    trip.average_speed_kmh = metrics.average_speed_kmh
    trip.max_speed_kmh = metrics.max_speed_kmh
    await db.commit()
    await db.refresh(trip)
    return trip
