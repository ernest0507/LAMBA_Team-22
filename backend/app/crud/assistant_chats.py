from datetime import datetime, timezone

from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.models.assistant_chat import AssistantChat, AssistantChatMessage, AssistantMessageRole


async def list_chats(db: AsyncSession, car_id: int) -> list[AssistantChat]:
    result = await db.execute(
        select(AssistantChat)
        .where(AssistantChat.car_id == car_id)
        .order_by(AssistantChat.updated_at.desc(), AssistantChat.id.desc())
    )
    return list(result.scalars().all())


async def get_chat(db: AsyncSession, car_id: int, chat_id: int) -> AssistantChat | None:
    result = await db.execute(
        select(AssistantChat).where(
            AssistantChat.id == chat_id,
            AssistantChat.car_id == car_id,
        )
    )
    return result.scalar_one_or_none()


async def create_chat(db: AsyncSession, car_id: int, title: str | None = None) -> AssistantChat:
    chat = AssistantChat(car_id=car_id, title=title)
    db.add(chat)
    await db.commit()
    await db.refresh(chat)
    return chat


async def list_messages(db: AsyncSession, chat_id: int) -> list[AssistantChatMessage]:
    result = await db.execute(
        select(AssistantChatMessage)
        .where(AssistantChatMessage.chat_id == chat_id)
        .order_by(AssistantChatMessage.created_at.asc(), AssistantChatMessage.id.asc())
    )
    return list(result.scalars().all())


async def create_message(
    db: AsyncSession,
    chat_id: int,
    role: AssistantMessageRole,
    content: str,
    action: str | None = None,
    record_id: int | None = None,
) -> AssistantChatMessage:
    message = AssistantChatMessage(
        chat_id=chat_id,
        role=role,
        content=content,
        action=action,
        record_id=record_id,
    )
    db.add(message)

    chat = await db.get(AssistantChat, chat_id)
    if chat is not None:
        chat.title = chat.title or _title_from_content(content)
        chat.updated_at = datetime.now(timezone.utc)

    await db.commit()
    await db.refresh(message)
    return message


def _title_from_content(content: str) -> str:
    clean = " ".join(content.split())
    if len(clean) <= 60:
        return clean
    return f"{clean[:57]}..."
