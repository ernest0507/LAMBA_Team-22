from app.models.assistant_chat import AssistantChat, AssistantChatMessage
from app.models.car import Car
from app.models.maintenance_record import MaintenanceRecord, MaintenanceRecordReceiptItem
from app.models.maintenance_record_photo import MaintenanceRecordPhoto
from app.models.trip import Trip, TripPoint
from app.models.user import User
from app.models.user_achievement import UserAchievement

__all__ = [
    "AssistantChat",
    "AssistantChatMessage",
    "Car",
    "MaintenanceRecord",
    "MaintenanceRecordReceiptItem",
    "MaintenanceRecordPhoto",
    "Trip",
    "TripPoint",
    "User",
    "UserAchievement",
]
