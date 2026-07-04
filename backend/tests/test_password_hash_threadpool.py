import asyncio
import threading
import time

import pytest

from app.crud import users


@pytest.mark.asyncio
async def test_hash_password_runs_outside_event_loop(monkeypatch):
    event_loop_thread = threading.get_ident()
    worker_threads: list[int] = []

    def slow_hash_password(password: str) -> str:
        worker_threads.append(threading.get_ident())
        time.sleep(0.05)
        return f"hashed-{password}"

    monkeypatch.setattr(users, "hash_password", slow_hash_password)

    event_loop_probe = asyncio.create_task(asyncio.sleep(0.01))
    hashed_password = await users._hash_password("secret")

    assert hashed_password == "hashed-secret"
    assert event_loop_probe.done()
    assert worker_threads
    assert worker_threads[0] != event_loop_thread


@pytest.mark.asyncio
async def test_verify_password_runs_outside_event_loop(monkeypatch):
    event_loop_thread = threading.get_ident()
    worker_threads: list[int] = []

    def slow_verify_password(password: str, password_hash: str) -> bool:
        worker_threads.append(threading.get_ident())
        time.sleep(0.05)
        return password_hash == f"hashed-{password}"

    monkeypatch.setattr(users, "verify_password", slow_verify_password)

    event_loop_probe = asyncio.create_task(asyncio.sleep(0.01))
    is_valid = await users._verify_password("secret", "hashed-secret")

    assert is_valid is True
    assert event_loop_probe.done()
    assert worker_threads
    assert worker_threads[0] != event_loop_thread
