import asyncio
import os
import time
from uuid import uuid4


import httpx
import pytest

from collections import Counter


REQUEST_COUNT = 20
MAX_RESPOND_TIME_SEC = 2.0
REQUIRED_FAST_RATIO = 0.95


BASE_URL = os.getenv("BACKEND_BASE_URL", "http://127.0.0.1:8000").rstrip("/")


def make_registration_playload(index: int) -> dict:
    unique_id = uuid4().hex
    return {
        "email" : f"{index}-{unique_id}@test.com", 
        "password": "12345678",
        "full_name": f"User {index}"
    }


@pytest.mark.asyncio
async def test_registration_response_time():
    semaphore = asyncio.Semaphore(REQUEST_COUNT)
    durations = []
    status_codes = []

    async with httpx.AsyncClient(timeout=MAX_RESPOND_TIME_SEC + 5) as client:
        async def send_registration(index: int) -> None:
            payload = make_registration_playload(index)
            async with semaphore:
                start = time.perf_counter()
                response = await client.post(
                    f"{BASE_URL}/api/v1/auth/register", 
                    json=payload
                )


                durations.append(time.perf_counter() - start)
                status_codes.append(response.status_code)
        await asyncio.gather(
            *(send_registration(index) for index in range(REQUEST_COUNT))
        )

        successful_responses = [code for code in status_codes if code == 201]
        fast_responses = [
            duration for duration in durations
            if duration <= MAX_RESPOND_TIME_SEC
        ]

        success_ratio = len(successful_responses) / REQUEST_COUNT
        fast_ratio = len(fast_responses) / REQUEST_COUNT

        print(durations)
        assert success_ratio >= REQUIRED_FAST_RATIO, Counter(status_codes)
        assert fast_ratio >= REQUIRED_FAST_RATIO, durations





