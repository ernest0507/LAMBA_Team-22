from dataclasses import dataclass

from app.schemas.achievement import AchievementUnlockType


@dataclass(frozen=True)
class AchievementDefinition:
    key: str
    title: str
    description: str
    category: str
    unlock_type: AchievementUnlockType


ACHIEVEMENTS: tuple[AchievementDefinition, ...] = (
    AchievementDefinition(
        key="road_situation_reported",
        title="Road Situation Reporter",
        description="Marked a road situation in the application.",
        category="road_situation",
        unlock_type=AchievementUnlockType.MANUAL,
    ),
    AchievementDefinition(
        key="careful_driver",
        title="Careful Driver",
        description="Manually confirmed careful driving behavior.",
        category="road_situation",
        unlock_type=AchievementUnlockType.MANUAL,
    ),
    AchievementDefinition(
        key="maintenance_ready",
        title="Maintenance Ready",
        description="Prepared the car for a maintenance-related situation.",
        category="road_situation",
        unlock_type=AchievementUnlockType.MANUAL,
    ),
    AchievementDefinition(
        key="first_statistics_review",
        title="First Statistics Review",
        description="Reviewed vehicle statistics for the first time.",
        category="statistics",
        unlock_type=AchievementUnlockType.AUTOMATIC,
    ),
)

ACHIEVEMENTS_BY_KEY = {achievement.key: achievement for achievement in ACHIEVEMENTS}


def get_achievement_definition(key: str) -> AchievementDefinition | None:
    return ACHIEVEMENTS_BY_KEY.get(key)
