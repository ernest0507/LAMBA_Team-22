from fastapi.testclient import TestClient

from app.api.deps import get_current_user
from app.main import app
from app.models.user import User


def test_logout_requires_access_token():
    with TestClient(app) as client:
        response = client.post("/api/v1/auth/logout")

    assert response.status_code == 401
    assert response.json()["detail"] == "Not authenticated"


def test_logout_returns_confirmation_for_authenticated_user():
    async def fake_current_user() -> User:
        return User(id=7, email="user@example.com", password_hash="hash")

    app.dependency_overrides[get_current_user] = fake_current_user
    try:
        with TestClient(app) as client:
            response = client.post("/api/v1/auth/logout")
    finally:
        app.dependency_overrides.pop(get_current_user, None)

    assert response.status_code == 200
    assert response.json() == {"message": "Logged out"}
