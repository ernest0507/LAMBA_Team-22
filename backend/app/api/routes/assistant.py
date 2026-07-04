from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.ext.asyncio import AsyncSession

from app.api.deps import get_current_user
from app.core.database import get_db
from app.crud.assistant_chats import (
    create_chat as create_assistant_chat,
    create_message as create_chat_message,
    get_chat,
    list_chats,
    list_messages,
)
from app.crud.cars import get_car, update_car
from app.crud.maintenance_records import create_record
from app.models.assistant_chat import AssistantChat, AssistantMessageRole
from app.models.car import Car
from app.models.user import User
from app.schemas.assistant import (
    AssistantAction,
    AssistantChatCreate,
    AssistantChatMessageRead,
    AssistantChatRead,
    AssistantMessageRequest,
    AssistantMessageResponse,
)
from app.schemas.car import CarUpdate
from app.schemas.maintenance_record import MaintenanceRecordCreate
from app.services.assistant_context import build_assistant_context
from app.services.assistant import extract_record_from_message


router = APIRouter(prefix="/cars/{car_id}/assistant", tags=["assistant"])


@router.get("/chats", response_model=list[AssistantChatRead])
async def read_assistant_chats(
    car_id: int,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> list[AssistantChatRead]:
    await _get_owned_car(db, current_user, car_id)
    return await list_chats(db, car_id)


@router.post("/chats", response_model=AssistantChatRead, status_code=status.HTTP_201_CREATED)
async def create_new_assistant_chat(
    car_id: int,
    data: AssistantChatCreate,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> AssistantChatRead:
    await _get_owned_car(db, current_user, car_id)
    return await create_assistant_chat(db, car_id, data.title)


@router.get("/chats/{chat_id}/messages", response_model=list[AssistantChatMessageRead])
async def read_assistant_chat_messages(
    car_id: int,
    chat_id: int,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> list[AssistantChatMessageRead]:
    await _get_owned_chat(db, current_user, car_id, chat_id)
    return await list_messages(db, chat_id)


@router.post("/chats/{chat_id}/messages", response_model=AssistantMessageResponse)
async def create_message_in_assistant_chat(
    car_id: int,
    chat_id: int,
    data: AssistantMessageRequest,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> AssistantMessageResponse:
    data = data.model_copy(update={"chat_id": chat_id})
    return await _handle_assistant_message(car_id, data, db, current_user)


@router.post("/messages", response_model=AssistantMessageResponse)
async def create_assistant_message(
    car_id: int,
    data: AssistantMessageRequest,
    db: AsyncSession = Depends(get_db),
    current_user: User = Depends(get_current_user),
) -> AssistantMessageResponse:
    return await _handle_assistant_message(car_id, data, db, current_user)


async def _handle_assistant_message(
    car_id: int,
    data: AssistantMessageRequest,
    db: AsyncSession,
    current_user: User,
) -> AssistantMessageResponse:
    if data.car_id != car_id:
        raise HTTPException(
            status_code=status.HTTP_422_UNPROCESSABLE_ENTITY,
            detail="Request car_id must match path car_id",
        )

    car = await _get_owned_car(db, current_user, car_id)
    chat = await _resolve_chat(db, car_id, data.chat_id, data.message)
    recent_messages = await list_messages(db, chat.id)

    await create_chat_message(
        db,
        chat_id=chat.id,
        role=AssistantMessageRole.USER,
        content=data.message,
    )

    assistant_result = await extract_record_from_message(
        data,
        car_context=await build_assistant_context(db, current_user, car, recent_messages),
    )

    if assistant_result.action == AssistantAction.UPDATE_MILEAGE:
        if assistant_result.mileage_update is None:
            response = AssistantMessageResponse(
                assistant_message="Уточни новый текущий пробег в километрах.",
                action=AssistantAction.NEEDS_CLARIFICATION,
                chat_id=chat.id,
            )
        else:
            updated_car = await update_car(
                db,
                car,
                CarUpdate(current_mileage_km=assistant_result.mileage_update.current_mileage_km),
            )
            response = AssistantMessageResponse(
                assistant_message=_mileage_updated_message(updated_car.current_mileage_km),
                action=AssistantAction.MILEAGE_UPDATED,
                chat_id=chat.id,
                mileage_update=assistant_result.mileage_update,
            )

        await create_chat_message(
            db,
            chat_id=chat.id,
            role=AssistantMessageRole.ASSISTANT,
            content=response.assistant_message,
            action=response.action.value,
        )
        return response

    if assistant_result.action != AssistantAction.RECORD_EXTRACTED:
        response = assistant_result.model_copy(update={"chat_id": chat.id})
        await create_chat_message(
            db,
            chat_id=chat.id,
            role=AssistantMessageRole.ASSISTANT,
            content=response.assistant_message,
            action=response.action.value,
        )
        return response

    if assistant_result.extracted_record is None:
        response = AssistantMessageResponse(
            assistant_message="Не получилось собрать корректную запись. Уточни детали, и я попробую ещё раз.",
            action=AssistantAction.NEEDS_CLARIFICATION,
            chat_id=chat.id,
        )
        await create_chat_message(
            db,
            chat_id=chat.id,
            role=AssistantMessageRole.ASSISTANT,
            content=response.assistant_message,
            action=response.action.value,
        )
        return response

    record_data = MaintenanceRecordCreate(
        category=assistant_result.extracted_record.category,
        title=assistant_result.extracted_record.title,
        description=assistant_result.extracted_record.description,
        occurred_at=assistant_result.extracted_record.occurred_at,
        mileage_km=assistant_result.extracted_record.mileage_km
        if assistant_result.extracted_record.mileage_km is not None
        else car.current_mileage_km,
        cost_amount=assistant_result.extracted_record.cost_amount,
        vendor=assistant_result.extracted_record.vendor,
    )
    record = await create_record(db, car_id, record_data)

    response = AssistantMessageResponse(
        assistant_message=_record_created_message(assistant_result.extracted_record.title),
        action=AssistantAction.RECORD_CREATED,
        chat_id=chat.id,
        record_id=record.id,
        extracted_record=assistant_result.extracted_record,
    )
    await create_chat_message(
        db,
        chat_id=chat.id,
        role=AssistantMessageRole.ASSISTANT,
        content=response.assistant_message,
        action=response.action.value,
        record_id=record.id,
    )
    return response


async def _get_owned_car(db: AsyncSession, current_user: User, car_id: int) -> Car:
    car = await get_car(db, current_user.id, car_id)
    if car is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Car not found")
    return car


async def _get_owned_chat(
    db: AsyncSession,
    current_user: User,
    car_id: int,
    chat_id: int,
) -> AssistantChat:
    await _get_owned_car(db, current_user, car_id)
    chat = await get_chat(db, car_id, chat_id)
    if chat is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Chat not found")
    return chat


async def _resolve_chat(
    db: AsyncSession,
    car_id: int,
    chat_id: int | None,
    first_message: str,
) -> AssistantChat:
    if chat_id is not None:
        chat = await get_chat(db, car_id, chat_id)
        if chat is None:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Chat not found")
        return chat
    return await create_assistant_chat(db, car_id, _chat_title_from_message(first_message))


def _record_created_message(title: str | None) -> str:
    if title:
        return f"Готово, записал себе в историю: {title}."
    return "Готово, записал это себе в историю."


def _mileage_updated_message(current_mileage_km: int) -> str:
    return f"Готово, обновил текущий пробег: {current_mileage_km} км."


def _chat_title_from_message(message: str) -> str:
    clean = " ".join(message.split())
    if len(clean) <= 60:
        return clean
    return f"{clean[:57]}..."
