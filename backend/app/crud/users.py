import asyncio

from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.core.security import hash_password, verify_password
from app.models.user import User
from app.schemas.auth import RegisterRequest


async def get_user_by_email(db: AsyncSession, email: str) -> User | None:
    result = await db.execute(select(User).where(User.email == email.lower()))
    return result.scalar_one_or_none()


async def get_user(db: AsyncSession, user_id: int) -> User | None:
    result = await db.execute(select(User).where(User.id == user_id))
    return result.scalar_one_or_none()


async def _hash_password(password: str) -> str:
    return await asyncio.to_thread(hash_password, password)


async def _verify_password(password: str, password_hash: str) -> bool:
    return await asyncio.to_thread(verify_password, password, password_hash)


async def create_user(db: AsyncSession, data: RegisterRequest) -> User:
    password_hash = await _hash_password(data.password)
    user = User(
        email=data.email.lower(),
        full_name=data.full_name,
        password_hash=password_hash,
    )
    db.add(user)
    await db.commit()
    await db.refresh(user)
    return user


async def authenticate_user(db: AsyncSession, email: str, password: str) -> User | None:
    user = await get_user_by_email(db, email)
    if not user or not await _verify_password(password, user.password_hash):
        return None
    return user
