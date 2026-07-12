from datetime import datetime
from enum import StrEnum

from pydantic import BaseModel


class AchievementUnlockType(StrEnum):
    MANUAL = "manual"
    AUTOMATIC = "automatic"


class AchievementRead(BaseModel):
    key: str
    title: str
    description: str
    category: str
    unlock_type: AchievementUnlockType
    is_unlocked: bool
    unlocked_at: datetime | None = None


class CarAchievementRead(BaseModel):
    id: int
    name: str
    description: str
    category: str
    image_url: str | None = None
    unlocked: bool
    unlocked_at: datetime | None = None
