from sqlalchemy import select
from sqlalchemy.exc import IntegrityError
from sqlalchemy.ext.asyncio import AsyncSession

from app.models.user_achievement import UserAchievement


async def list_user_achievements(db: AsyncSession, user_id: int) -> list[UserAchievement]:
    result = await db.execute(
        select(UserAchievement).where(UserAchievement.user_id == user_id)
    )
    return list(result.scalars().all())


async def get_user_achievement(
    db: AsyncSession,
    user_id: int,
    achievement_key: str,
) -> UserAchievement | None:
    result = await db.execute(
        select(UserAchievement).where(
            UserAchievement.user_id == user_id,
            UserAchievement.achievement_key == achievement_key,
        )
    )
    return result.scalar_one_or_none()


async def unlock_user_achievement(
    db: AsyncSession,
    user_id: int,
    achievement_key: str,
) -> UserAchievement:
    existing = await get_user_achievement(db, user_id, achievement_key)
    if existing is not None:
        return existing

    user_achievement = UserAchievement(
        user_id=user_id,
        achievement_key=achievement_key,
    )
    db.add(user_achievement)
    try:
        await db.commit()
    except IntegrityError:
        await db.rollback()
        existing = await get_user_achievement(db, user_id, achievement_key)
        if existing is not None:
            return existing
        raise
    await db.refresh(user_achievement)
    return user_achievement
