from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.models.car import Car
from app.schemas.car import CarCreate, CarUpdate


async def list_cars(db: AsyncSession, owner_id: int, skip: int = 0, limit: int = 100) -> list[Car]:
    result = await db.execute(
        select(Car)
        .where(Car.owner_id == owner_id)
        .order_by(Car.created_at.desc(), Car.id.desc())
        .offset(skip)
        .limit(limit)
    )
    return list(result.scalars().all())


async def get_car(db: AsyncSession, owner_id: int, car_id: int) -> Car | None:
    result = await db.execute(select(Car).where(Car.id == car_id, Car.owner_id == owner_id))
    return result.scalar_one_or_none()


async def create_car(db: AsyncSession, owner_id: int, data: CarCreate) -> Car:
    car = Car(owner_id=owner_id, **data.model_dump())
    db.add(car)
    await db.commit()
    await db.refresh(car)
    return car


async def update_car(db: AsyncSession, car: Car, data: CarUpdate) -> Car:
    for field, value in data.model_dump(exclude_unset=True).items():
        setattr(car, field, value)
    await db.commit()
    await db.refresh(car)
    return car


async def delete_car(db: AsyncSession, car: Car) -> None:
    await db.delete(car)
    await db.commit()
