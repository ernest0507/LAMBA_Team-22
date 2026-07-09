from datetime import datetime, timezone
from types import SimpleNamespace

import pytest
from fastapi import HTTPException, status

from app.api.routes import achievements
from app.crud.achievements import unlock_user_achievement
from app.models.user import User


def make_user(user_id: int = 7) -> User:
    return User(id=user_id, email=f"user-{user_id}@example.com", password_hash="hash")


def make_unlocked(key: str, user_id: int = 7) -> SimpleNamespace:
    return SimpleNamespace(
        id=1,
        user_id=user_id,
        achievement_key=key,
        unlocked_at=datetime(2026, 7, 10, 12, 0, tzinfo=timezone.utc),
    )


@pytest.mark.asyncio
async def test_read_achievements_returns_user_specific_unlocked_state(monkeypatch):
    async def fake_list_user_achievements(db, user_id):
        assert user_id == 7
        return [make_unlocked("road_situation_reported", user_id)]

    monkeypatch.setattr(
        achievements,
        "list_user_achievements",
        fake_list_user_achievements,
    )

    result = await achievements.read_achievements(
        db=object(),
        current_user=make_user(),
    )

    by_key = {achievement.key: achievement for achievement in result}
    assert by_key["road_situation_reported"].is_unlocked is True
    assert by_key["road_situation_reported"].unlocked_at is not None
    assert by_key["careful_driver"].is_unlocked is False
    assert by_key["careful_driver"].unlocked_at is None


@pytest.mark.asyncio
async def test_unlock_achievement_marks_manual_achievement_for_current_user(monkeypatch):
    calls = []
    unlocked = make_unlocked("careful_driver")

    async def fake_unlock_user_achievement(db, user_id, achievement_key):
        calls.append((user_id, achievement_key))
        return unlocked

    monkeypatch.setattr(
        achievements,
        "unlock_user_achievement",
        fake_unlock_user_achievement,
    )

    result = await achievements.unlock_achievement(
        achievement_key="careful_driver",
        db=object(),
        current_user=make_user(),
    )

    assert calls == [(7, "careful_driver")]
    assert result.key == "careful_driver"
    assert result.is_unlocked is True
    assert result.unlocked_at == unlocked.unlocked_at


@pytest.mark.asyncio
async def test_unlock_achievement_rejects_unknown_key():
    with pytest.raises(HTTPException) as exc_info:
        await achievements.unlock_achievement(
            achievement_key="missing_achievement",
            db=object(),
            current_user=make_user(),
        )

    assert exc_info.value.status_code == status.HTTP_404_NOT_FOUND
    assert exc_info.value.detail == "Achievement not found"


@pytest.mark.asyncio
async def test_unlock_achievement_rejects_non_manual_achievement(monkeypatch):
    async def fake_unlock_user_achievement(db, user_id, achievement_key):
        raise AssertionError("automatic achievement must not be unlocked manually")

    monkeypatch.setattr(
        achievements,
        "unlock_user_achievement",
        fake_unlock_user_achievement,
    )

    with pytest.raises(HTTPException) as exc_info:
        await achievements.unlock_achievement(
            achievement_key="first_statistics_review",
            db=object(),
            current_user=make_user(),
        )

    assert exc_info.value.status_code == status.HTTP_409_CONFLICT
    assert exc_info.value.detail == "Achievement cannot be unlocked manually"


@pytest.mark.asyncio
async def test_unlock_user_achievement_is_idempotent_for_existing_record():
    existing = make_unlocked("road_situation_reported")
    db = ExistingAchievementSession(existing)

    result = await unlock_user_achievement(
        db,
        user_id=7,
        achievement_key="road_situation_reported",
    )

    assert result is existing
    assert db.added == []
    assert db.commit_count == 0


class ExistingAchievementSession:
    def __init__(self, existing):
        self.existing = existing
        self.added = []
        self.commit_count = 0

    async def execute(self, _statement):
        return ExistingAchievementResult(self.existing)

    def add(self, item):
        self.added.append(item)

    async def commit(self):
        self.commit_count += 1

    async def rollback(self):
        pass

    async def refresh(self, _item):
        pass


class ExistingAchievementResult:
    def __init__(self, existing):
        self.existing = existing

    def scalar_one_or_none(self):
        return self.existing
