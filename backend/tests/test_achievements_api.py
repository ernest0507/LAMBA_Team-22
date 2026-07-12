from datetime import datetime, timezone
from types import SimpleNamespace

import pytest
from fastapi import HTTPException, status

from app.api.routes import achievements
from app.crud.achievements import unlock_user_achievement
from app.main import app
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


def test_android_achievement_routes_are_registered():
    paths = app.openapi()["paths"]

    assert "/api/v1/cars/{car_id}/achievements" in paths
    assert "/api/v1/cars/{car_id}/achievements/{achievement_id}/unlock" in paths


@pytest.mark.asyncio
async def test_read_achievements_returns_user_specific_unlocked_state(monkeypatch):
    async def fake_list_user_achievements(db, user_id):
        assert user_id == 7
        return [make_unlocked("snow_king", user_id)]

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
    assert by_key["snow_king"].is_unlocked is True
    assert by_key["snow_king"].unlocked_at is not None
    assert by_key["road_tow"].is_unlocked is False
    assert by_key["road_tow"].unlocked_at is None


@pytest.mark.asyncio
async def test_unlock_achievement_marks_manual_achievement_for_current_user(monkeypatch):
    calls = []
    unlocked = make_unlocked("snow_king")

    async def fake_unlock_user_achievement(db, user_id, achievement_key):
        calls.append((user_id, achievement_key))
        return unlocked

    monkeypatch.setattr(
        achievements,
        "unlock_user_achievement",
        fake_unlock_user_achievement,
    )

    result = await achievements.unlock_achievement(
        achievement_key="snow_king",
        db=object(),
        current_user=make_user(),
    )

    assert calls == [(7, "snow_king")]
    assert result.key == "snow_king"
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
            achievement_key="fuel_eater",
            db=object(),
            current_user=make_user(),
        )

    assert exc_info.value.status_code == status.HTTP_409_CONFLICT
    assert exc_info.value.detail == "Achievement cannot be unlocked manually"


@pytest.mark.asyncio
async def test_unlock_user_achievement_is_idempotent_for_existing_record():
    existing = make_unlocked("snow_king")
    db = ExistingAchievementSession(existing)

    result = await unlock_user_achievement(
        db,
        user_id=7,
        achievement_key="snow_king",
    )

    assert result is existing
    assert db.added == []
    assert db.commit_count == 0


@pytest.mark.asyncio
async def test_read_car_achievements_matches_android_contract(monkeypatch):
    async def fake_get_car(db, owner_id, car_id):
        assert (owner_id, car_id) == (7, 42)
        return SimpleNamespace(id=car_id, owner_id=owner_id)

    async def fake_list_user_achievements(db, user_id):
        return [make_unlocked("snow_king", user_id)]

    monkeypatch.setattr(achievements, "get_car", fake_get_car)
    monkeypatch.setattr(
        achievements,
        "list_user_achievements",
        fake_list_user_achievements,
    )

    result = await achievements.read_car_achievements(
        car_id=42,
        db=object(),
        current_user=make_user(),
    )

    by_id = {achievement.id: achievement for achievement in result}
    assert len(result) == 20
    assert by_id[9].name == "Снежный король"
    assert by_id[9].category == "road"
    assert by_id[9].unlocked is True
    assert by_id[10].unlocked is False


@pytest.mark.asyncio
async def test_unlock_car_achievement_persists_numeric_android_id(monkeypatch):
    calls = []
    unlocked = make_unlocked("snow_king")

    async def fake_get_car(db, owner_id, car_id):
        return SimpleNamespace(id=car_id, owner_id=owner_id)

    async def fake_unlock_user_achievement(db, user_id, achievement_key):
        calls.append((user_id, achievement_key))
        return unlocked

    monkeypatch.setattr(achievements, "get_car", fake_get_car)
    monkeypatch.setattr(
        achievements,
        "unlock_user_achievement",
        fake_unlock_user_achievement,
    )

    result = await achievements.unlock_car_achievement(
        car_id=42,
        achievement_id=9,
        db=object(),
        current_user=make_user(),
    )

    assert calls == [(7, "snow_king")]
    assert result.id == 9
    assert result.name == "Снежный король"
    assert result.unlocked is True
    assert result.unlocked_at == unlocked.unlocked_at


@pytest.mark.asyncio
async def test_car_achievements_rejects_car_owned_by_another_user(monkeypatch):
    async def fake_get_car(db, owner_id, car_id):
        return None

    monkeypatch.setattr(achievements, "get_car", fake_get_car)

    with pytest.raises(HTTPException) as exc_info:
        await achievements.read_car_achievements(
            car_id=42,
            db=object(),
            current_user=make_user(),
        )

    assert exc_info.value.status_code == status.HTTP_404_NOT_FOUND
    assert exc_info.value.detail == "Car not found"


@pytest.mark.asyncio
async def test_unlock_car_achievement_rejects_unknown_id(monkeypatch):
    async def fake_get_car(db, owner_id, car_id):
        return SimpleNamespace(id=car_id, owner_id=owner_id)

    monkeypatch.setattr(achievements, "get_car", fake_get_car)

    with pytest.raises(HTTPException) as exc_info:
        await achievements.unlock_car_achievement(
            car_id=42,
            achievement_id=999,
            db=object(),
            current_user=make_user(),
        )

    assert exc_info.value.status_code == status.HTTP_404_NOT_FOUND
    assert exc_info.value.detail == "Achievement not found"


@pytest.mark.asyncio
async def test_unlock_car_achievement_rejects_automatic_id(monkeypatch):
    async def fake_get_car(db, owner_id, car_id):
        return SimpleNamespace(id=car_id, owner_id=owner_id)

    monkeypatch.setattr(achievements, "get_car", fake_get_car)

    with pytest.raises(HTTPException) as exc_info:
        await achievements.unlock_car_achievement(
            car_id=42,
            achievement_id=1,
            db=object(),
            current_user=make_user(),
        )

    assert exc_info.value.status_code == status.HTTP_409_CONFLICT
    assert exc_info.value.detail == "Achievement cannot be unlocked manually"


@pytest.mark.asyncio
async def test_unlock_user_achievement_commits_new_record():
    db = NewAchievementSession()

    result = await unlock_user_achievement(
        db,
        user_id=7,
        achievement_key="snow_king",
    )

    assert result is db.added[0]
    assert result.user_id == 7
    assert result.achievement_key == "snow_king"
    assert db.commit_count == 1
    assert db.refresh_count == 1


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


class NewAchievementSession:
    def __init__(self):
        self.added = []
        self.commit_count = 0
        self.refresh_count = 0

    async def execute(self, _statement):
        return ExistingAchievementResult(None)

    def add(self, item):
        self.added.append(item)

    async def commit(self):
        self.commit_count += 1

    async def rollback(self):
        pass

    async def refresh(self, item):
        self.refresh_count += 1
        item.id = 1
        item.unlocked_at = datetime(2026, 7, 12, 12, 0, tzinfo=timezone.utc)
