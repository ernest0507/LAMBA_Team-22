import pytest

from app.api.routes import auth
from app.schemas.auth import RegisterRequest


class FakeSession:
    def __init__(self) -> None:
        self.events: list[str] = []

    async def rollback(self) -> None:
        self.events.append("rollback")


@pytest.mark.asyncio
async def test_register_releases_lookup_connection_before_creating_user(
    monkeypatch,
):
    db = FakeSession()
    data = RegisterRequest(
        email="new-user@example.com",
        password="12345678",
        full_name="New User",
    )

    async def fake_get_user_by_email(fake_db: FakeSession, email: str) -> None:
        assert fake_db is db
        assert email == data.email
        db.events.append("lookup")
        return None

    async def fake_create_user(
        fake_db: FakeSession,
        request: RegisterRequest,
    ) -> object:
        assert fake_db is db
        assert request is data
        db.events.append("create")
        return object()

    monkeypatch.setattr(auth, "get_user_by_email", fake_get_user_by_email)
    monkeypatch.setattr(auth, "create_user", fake_create_user)

    await auth.register(data, db)  # type: ignore[arg-type]

    assert db.events == ["lookup", "rollback", "create"]
