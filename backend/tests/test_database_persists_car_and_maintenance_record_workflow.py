import os 
from uuid import uuid4

import httpx 
import pytest


BASE_URL = os.getenv("BACKEND_BASE_URL", "http://127.0.0.1:8000").rstrip("/")

def unique_email() -> str:
    return f"{uuid4().hex}@test.com"

@pytest.mark.asyncio
async def test_database_persists_car_and_maintenance_record_workflow():
    user_email = unique_email()
    user_password = "12345678"

    async with httpx.AsyncClient(base_url=BASE_URL, timeout=10) as client:
        registre_response = await client.post(
            "/api/v1/auth/register", 
            json={

                "email": user_email, 
                "password": user_password, 
                "full_name": "Persistence Test"
            }
        )

        assert registre_response.status_code == 201, registre_response.text


        login_response = await client.post(
            "/api/v1/auth/login", 
            json={
                "email": user_email, 
                "password": user_password
            }
        )

        assert login_response.status_code == 200, login_response.text

        token = login_response.json()["access_token"]
        headers = {"Authorization": f"Bearer {token}"}


        car_response = await client.post(
            "/api/v1/cars", 
            json={
                "make": "ВАЗ", 
                "model": "2103",
                "year": 2026, 
                "current_mileage_km": 67000, 
                "color": "white",
                "body_type": "sedan", 
                "notes": "Integration test"
            }, 
            headers=headers
        )
        assert car_response.status_code == 201, car_response.text

        car_id = car_response.json()["id"]
        record_payload = {
            "category": "maintenance", 
            "title": "Oil change", 
            "description": "Changed engine oil", 
            "occurred_at": "2026-05-30", 
            "mileage_km": 67000, 
            "cost_amount": "3500.00", 
            "vendor": "Local service"
        }


        records_response = await client.post(
            f"/api/v1/cars/{car_id}/records", 
            headers=headers, 
            json=record_payload
        )

        assert records_response.status_code == 201, records_response.text


        record_id = records_response.json()["id"]

        records_read_response = await client.get(
            f"/api/v1/cars/{car_id}/records/{record_id}", 
            headers=headers
        )

        persisted_record = records_read_response.json()

        assert persisted_record["title"] == record_payload["title"]
        assert persisted_record["category"] == record_payload["category"]
        assert persisted_record["mileage_km"] == record_payload["mileage_km"]
        assert persisted_record["cost_amount"] == record_payload["cost_amount"]

        timeline_response = await client.get(
            f"/api/v1/cars/{car_id}/timeline", 
            headers=headers
        )
        assert timeline_response.status_code == 200, timeline_response.text

        timeline = timeline_response.json()
        assert any(item["id"] == record_id for item in timeline)

