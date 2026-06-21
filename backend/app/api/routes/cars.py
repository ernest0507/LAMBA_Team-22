from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy.ext.asyncio import AsyncSession

from app.api.deps import get_current_user
from app.core.database import get_db
from app.crud.cars import create_car, delete_car, get_car, list_cars, update_car
from app.models.user import User
from app.schemas.car import CarCreate, CarRead, CarUpdate


router = APIRouter(prefix="/cars", tags=["cars"])


@router.get("", response_model=list[CarRead])
async def read_cars(
    skip: int = Query(default=0, ge=0),
    limit: int = Query(default=100, ge=1, le=500),
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> list[CarRead]:
    return await list_cars(db, current_user.id, skip, limit)


@router.post("", response_model=CarRead, status_code=status.HTTP_201_CREATED)
async def create_new_car(
    data: CarCreate,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> CarRead:
    return await create_car(db, current_user.id, data)


@router.get("/{car_id}", response_model=CarRead)
async def read_car(
    car_id: int,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> CarRead:
    car = await get_car(db, current_user.id, car_id)
    if not car:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Car not found")
    return car


@router.patch("/{car_id}", response_model=CarRead)
async def update_existing_car(
    car_id: int,
    data: CarUpdate,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> CarRead:
    car = await get_car(db, current_user.id, car_id)
    if not car:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Car not found")
    return await update_car(db, car, data)


@router.delete("/{car_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_existing_car(
    car_id: int,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> None:
    car = await get_car(db, current_user.id, car_id)
    if not car:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Car not found")
    await delete_car(db, car)
