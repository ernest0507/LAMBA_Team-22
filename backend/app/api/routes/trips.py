from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.ext.asyncio import AsyncSession

from app.api.deps import get_current_user
from app.core.database import get_db
from app.crud.cars import get_car
from app.crud.trips import (
    append_trip_points,
    create_trip,
    finish_trip,
    get_active_trip,
    get_trip_for_user,
)
from app.models.trip import Trip
from app.models.user import User
from app.schemas.trip import (
    TripCreate,
    TripFinish,
    TripPointBatchCreate,
    TripPointRead,
    TripRead,
)


router = APIRouter(tags=["trips"])


@router.post(
    "/cars/{car_id}/trips/start",
    response_model=TripRead,
    status_code=status.HTTP_201_CREATED,
)
async def start_trip(
    car_id: int,
    data: TripCreate | None = None,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> TripRead:
    await ensure_car_owner(db, current_user, car_id)
    active_trip = await get_active_trip(db, car_id)
    if active_trip is not None:
        raise HTTPException(
            status_code=status.HTTP_409_CONFLICT,
            detail="Car already has an active trip",
        )
    return await create_trip(db, car_id, data or TripCreate())


@router.post(
    "/trips/{trip_id}/points",
    response_model=list[TripPointRead],
    status_code=status.HTTP_201_CREATED,
)
async def append_points(
    trip_id: int,
    data: TripPointBatchCreate,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> list[TripPointRead]:
    trip = await get_owned_trip(db, current_user, trip_id)
    ensure_trip_active(trip)
    return await append_trip_points(db, trip.id, data.points)


@router.post("/trips/{trip_id}/finish", response_model=TripRead)
async def finish_active_trip(
    trip_id: int,
    data: TripFinish | None = None,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> TripRead:
    trip = await get_owned_trip(db, current_user, trip_id)
    ensure_trip_active(trip)
    ended_at = data.ended_at if data else None
    if ended_at is not None and ended_at < trip.started_at:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Trip end time cannot be before start time",
        )
    return await finish_trip(db, trip, ended_at)


async def ensure_car_owner(db: AsyncSession, current_user: User, car_id: int) -> None:
    car = await get_car(db, current_user.id, car_id)
    if not car:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Car not found")


async def get_owned_trip(db: AsyncSession, current_user: User, trip_id: int) -> Trip:
    trip = await get_trip_for_user(db, current_user.id, trip_id)
    if trip is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Trip not found")
    return trip


def ensure_trip_active(trip: Trip) -> None:
    if trip.ended_at is not None:
        raise HTTPException(
            status_code=status.HTTP_409_CONFLICT,
            detail="Trip is already finished",
        )
