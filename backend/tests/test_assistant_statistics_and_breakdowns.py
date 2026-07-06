from decimal import Decimal

import pytest

from app.schemas.assistant import AssistantAction, AssistantMessageRequest
from app.services.assistant import extract_record_from_message


@pytest.mark.asyncio
async def test_assistant_returns_statistics_from_context_without_ai():
    result = await extract_record_from_message(
        AssistantMessageRequest(car_id=1, message="Покажи статистику по машине"),
        car_context={
            "records_summary": {
                "count": 3,
                "total_cost_amount": "15000.00",
                "total_cost_by_category": {
                    "maintenance": "5000.00",
                    "repair": "10000.00",
                },
            },
            "records": [
                {"category": "maintenance", "cost_amount": "5000.00"},
                {"category": "repair", "cost_amount": "7000.00"},
                {"category": "repair", "cost_amount": "3000.00"},
            ],
        },
    )

    assert result.action == AssistantAction.MESSAGE
    assert result.record_id is None
    assert "15 000" in result.assistant_message
    assert "Поломки и ремонт: 2" in result.assistant_message


@pytest.mark.asyncio
async def test_assistant_extracts_breakdown_record_without_ai():
    result = await extract_record_from_message(
        AssistantMessageRequest(
            car_id=1,
            message="Запиши поломку: сломался кондиционер, пробег 120000 км",
        ),
        car_context={},
    )

    assert result.action == AssistantAction.RECORD_EXTRACTED
    assert result.extracted_record is not None
    assert result.extracted_record.category == "repair"
    assert "кондиционер" in result.extracted_record.title.lower()
    assert result.extracted_record.mileage_km == 120000
    assert result.extracted_record.cost_amount == Decimal("0.00")
