from collections import defaultdict
from decimal import Decimal
from typing import Any

from sqlalchemy.ext.asyncio import AsyncSession

from app.crud.maintenance_records import list_records
from app.models.assistant_chat import AssistantChatMessage
from app.models.car import Car
from app.models.user import User


async def build_assistant_context(
    db: AsyncSession,
    current_user: User,
    car: Car,
    recent_messages: list[AssistantChatMessage] | None = None,
) -> dict[str, Any]:
    records = await list_records(db, car.id, skip=0, limit=500)
    total_cost = sum((record.cost_amount for record in records), Decimal("0.00"))
    totals_by_category: dict[str, Decimal] = defaultdict(lambda: Decimal("0.00"))

    for record in records:
        category = record.category or "other"
        totals_by_category[category] += record.cost_amount

    return {
        "privacy_rules": {
            "included": [
                "safe user display name",
                "current user's car profile",
                "current car maintenance/repair/expense history",
                "recent messages in this assistant chat",
            ],
            "excluded": [
                "password_hash",
                "access tokens",
                "secrets",
                "other users' data",
                "raw internal configuration",
            ],
        },
        "user": {
            "id": current_user.id,
            "full_name": current_user.full_name,
        },
        "car": {
            "id": car.id,
            "make": car.make,
            "model": car.model,
            "year": car.year,
            "current_mileage_km": car.current_mileage_km,
            "color": car.color,
            "body_type": car.body_type,
            "notes": car.notes,
        },
        "records_summary": {
            "count": len(records),
            "total_cost_amount": _decimal_to_string(total_cost),
            "total_cost_by_category": {
                category: _decimal_to_string(amount)
                for category, amount in sorted(totals_by_category.items())
            },
        },
        "records": [
            {
                "id": record.id,
                "category": record.category,
                "title": record.title,
                "description": record.description,
                "occurred_at": str(record.occurred_at) if record.occurred_at else None,
                "mileage_km": record.mileage_km,
                "cost_amount": _decimal_to_string(record.cost_amount),
                "vendor": record.vendor,
                "created_at": record.created_at.isoformat() if record.created_at else None,
            }
            for record in records
        ],
        "recent_chat_messages": [
            {
                "role": message.role,
                "content": message.content,
                "action": message.action,
                "record_id": message.record_id,
                "created_at": message.created_at.isoformat() if message.created_at else None,
            }
            for message in (recent_messages or [])[-20:]
        ],
    }


def _decimal_to_string(value: Decimal) -> str:
    return f"{value:.2f}"
