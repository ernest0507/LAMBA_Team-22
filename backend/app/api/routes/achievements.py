from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.ext.asyncio import AsyncSession

from app.api.deps import get_current_user
from app.core.database import get_db
from app.crud.achievements import list_user_achievements, unlock_user_achievement
from app.models.user import User
from app.models.user_achievement import UserAchievement
from app.schemas.achievement import AchievementRead, AchievementUnlockType
from app.services.achievements import (
    ACHIEVEMENTS,
    AchievementDefinition,
    get_achievement_definition,
)


router = APIRouter(prefix="/achievements", tags=["achievements"])


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
