from datetime import date, datetime
from decimal import Decimal
from enum import StrEnum
from typing import Self

from pydantic import BaseModel, ConfigDict, Field, field_validator, model_validator

from app.models.assistant_chat import AssistantMessageRole
from app.models.maintenance_record import RecordCategory


class AssistantAction(StrEnum):
    MESSAGE = "message"
    RECORD_EXTRACTED = "record_extracted"
    RECORD_CREATED = "record_created"
    UPDATE_MILEAGE = "update_mileage"
    MILEAGE_UPDATED = "mileage_updated"
    NEEDS_CLARIFICATION = "needs_clarification"


class AssistantMessageRequest(BaseModel):
    car_id: int = Field(ge=1)
    chat_id: int | None = Field(default=None, ge=1)
    message: str = Field(min_length=1, max_length=4000)

    @field_validator("message")
    @classmethod
    def validate_message(cls, value: str) -> str:
        value = value.strip()
        if not value:
            raise ValueError("message cannot be empty")
        return value


class AssistantExtractedRecord(BaseModel):
    category: RecordCategory | None = None
    title: str | None = Field(default=None, min_length=1, max_length=200)
    description: str | None = None
    occurred_at: date | None = None
    mileage_km: int | None = Field(default=None, ge=0)
    cost_amount: Decimal = Field(ge=0, max_digits=12, decimal_places=2)
    vendor: str | None = Field(default=None, max_length=200)


class AssistantMileageUpdate(BaseModel):
    current_mileage_km: int = Field(ge=0)


class AssistantMessageResponse(BaseModel):
    assistant_message: str = Field(min_length=1, max_length=4000)
    action: AssistantAction = AssistantAction.MESSAGE
    chat_id: int | None = Field(default=None, ge=1)
    record_id: int | None = Field(default=None, ge=1)
    extracted_record: AssistantExtractedRecord | None = None
    mileage_update: AssistantMileageUpdate | None = None

    @field_validator("assistant_message")
    @classmethod
    def validate_assistant_message(cls, value: str) -> str:
        value = value.strip()
        if not value:
            raise ValueError("assistant_message cannot be empty")
        return value

    @model_validator(mode="after")
    def validate_action_payload(self) -> Self:
        if self.action == AssistantAction.RECORD_EXTRACTED:
            if self.extracted_record is None:
                raise ValueError("extracted_record is required when action is record_extracted")
            if self.record_id is not None:
                raise ValueError("record_id must be empty when action is record_extracted")
        if self.action == AssistantAction.RECORD_CREATED and self.record_id is None:
            raise ValueError("record_id is required when action is record_created")
        if self.action == AssistantAction.NEEDS_CLARIFICATION and self.record_id is not None:
            raise ValueError("record_id must be empty when action is needs_clarification")
        if self.action == AssistantAction.UPDATE_MILEAGE and self.mileage_update is None:
            raise ValueError("mileage_update is required when action is update_mileage")
        if self.action == AssistantAction.MILEAGE_UPDATED and self.mileage_update is None:
            raise ValueError("mileage_update is required when action is mileage_updated")
        return self


class AssistantChatCreate(BaseModel):
    title: str | None = Field(default=None, max_length=120)

    @field_validator("title")
    @classmethod
    def validate_title(cls, value: str | None) -> str | None:
        if value is None:
            return None
        value = value.strip()
        return value or None


class AssistantChatRead(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: int
    car_id: int
    title: str | None
    created_at: datetime
    updated_at: datetime


class AssistantChatMessageRead(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: int
    chat_id: int
    role: AssistantMessageRole
    content: str
    action: AssistantAction | None = None
    record_id: int | None = None
    created_at: datetime
