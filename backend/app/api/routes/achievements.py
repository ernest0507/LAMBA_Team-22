from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.ext.asyncio import AsyncSession

from app.api.deps import get_current_user
from app.core.database import get_db
from app.crud.achievements import list_user_achievements, unlock_user_achievement
from app.crud.cars import get_car
from app.crud.maintenance_records import list_records
from app.models.user import User
from app.models.user_achievement import UserAchievement
from app.schemas.achievement import AchievementRead, AchievementUnlockType, CarAchievementRead
from app.services.achievements import (
    ACHIEVEMENTS,
    AchievementDefinition,
    evaluate_statistics_achievement_keys,
    get_achievement_definition,
    get_achievement_definition_by_id,
)


router = APIRouter(prefix="/achievements", tags=["achievements"])
car_router = APIRouter(prefix="/cars", tags=["achievements"])


@router.get("", response_model=list[AchievementRead])
async def read_achievements(
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> list[AchievementRead]:
    unlocked = await list_user_achievements(db, current_user.id)
    unlocked_by_key = {item.achievement_key: item for item in unlocked}
    return [
        achievement_to_read(achievement, unlocked_by_key.get(achievement.key))
        for achievement in ACHIEVEMENTS
    ]


@router.post("/{achievement_key}/unlock", response_model=AchievementRead)
async def unlock_achievement(
    achievement_key: str,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> AchievementRead:
    achievement = get_achievement_definition(achievement_key)
    if achievement is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Achievement not found",
        )
    if achievement.unlock_type != AchievementUnlockType.MANUAL:
        raise HTTPException(
            status_code=status.HTTP_409_CONFLICT,
            detail="Achievement cannot be unlocked manually",
        )

    unlocked = await unlock_user_achievement(db, current_user.id, achievement.key)
    return achievement_to_read(achievement, unlocked)


@car_router.get("/{car_id}/achievements", response_model=list[CarAchievementRead])
async def read_car_achievements(
    car_id: int,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> list[CarAchievementRead]:
    await require_owned_car(db, current_user.id, car_id)
    unlocked = await list_user_achievements(db, current_user.id)
    unlocked_by_key = {item.achievement_key: item for item in unlocked}
    records = await list_records(db, car_id, 0, 500)
    automatic_keys = evaluate_statistics_achievement_keys(records)
    for achievement_key in sorted(automatic_keys - unlocked_by_key.keys()):
        unlocked_by_key[achievement_key] = await unlock_user_achievement(
            db,
            current_user.id,
            achievement_key,
        )
    return [
        achievement_to_car_read(achievement, unlocked_by_key.get(achievement.key))
        for achievement in ACHIEVEMENTS
    ]


@car_router.post(
    "/{car_id}/achievements/{achievement_id}/unlock",
    response_model=CarAchievementRead,
)
async def unlock_car_achievement(
    car_id: int,
    achievement_id: int,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> CarAchievementRead:
    await require_owned_car(db, current_user.id, car_id)
    achievement = get_achievement_definition_by_id(achievement_id)
    if achievement is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail="Achievement not found",
        )
    if achievement.unlock_type != AchievementUnlockType.MANUAL:
        raise HTTPException(
            status_code=status.HTTP_409_CONFLICT,
            detail="Achievement cannot be unlocked manually",
        )

    unlocked = await unlock_user_achievement(db, current_user.id, achievement.key)
    return achievement_to_car_read(achievement, unlocked)


async def require_owned_car(db: AsyncSession, user_id: int, car_id: int) -> None:
    if await get_car(db, user_id, car_id) is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Car not found")


def achievement_to_read(
    achievement: AchievementDefinition,
    unlocked: UserAchievement | None,
) -> AchievementRead:
    return AchievementRead(
        key=achievement.key,
        title=achievement.title,
        description=achievement.description,
        category=achievement.category,
        unlock_type=achievement.unlock_type,
        is_unlocked=unlocked is not None,
        unlocked_at=unlocked.unlocked_at if unlocked else None,
    )


def achievement_to_car_read(
    achievement: AchievementDefinition,
    unlocked: UserAchievement | None,
) -> CarAchievementRead:
    return CarAchievementRead(
        id=achievement.id,
        name=achievement.title,
        description=achievement.description,
        category=achievement.category,
        unlocked=unlocked is not None,
        unlocked_at=unlocked.unlocked_at if unlocked else None,
    )
