from datetime import datetime
from decimal import Decimal

import pytest

from app.services.proverkacheka import ProverkachekaError, normalize_receipt_response
from app.services.receipt_identity import build_receipt_id


def test_normalize_receipt_response_maps_successful_payload():
    result = normalize_receipt_response(
        {
            "code": 1,
            "first": 1,
            "data": {
                "json": {
                    "user": "Fuel Station",
                    "retailPlaceAddres": "Moscow",
                    "userInn": "1234567890",
                    "ticketDate": "2026-07-08T12:30:00",
                    "requestNumber": 42,
                    "shiftNumber": 7,
                    "operator": "Cashier",
                    "operationType": 1,
                    "totalSum": 34993,
                    "cashTotalSum": 0,
                    "ecashTotalSum": 34993,
                    "fiscalDriveNumber": "9282440300682838",
                    "fiscalDocumentNumber": "46534",
                    "fiscalSign": "1273019065",
                    "items": [
                        {
                            "name": "Gasoline",
                            "price": 5000,
                            "quantity": 6.998,
                            "sum": 34993,
                        }
                    ],
                }
            },
        }
    )

    assert result.provider_code == 1
    assert result.receipt_id == build_receipt_id(
        ticket_date=result.ticket_date,
        fiscal_sign="1273019065",
    )
    assert result.first is True
    assert result.seller_name == "Fuel Station"
    assert result.seller_inn == "1234567890"
    assert result.ticket_date is not None
    assert result.request_number == 42
    assert result.total_amount == Decimal("349.93")
    assert result.ecash_total_amount == Decimal("349.93")
    assert result.items[0].name == "Gasoline"
    assert result.items[0].price_amount == Decimal("50.00")
    assert result.items[0].quantity == Decimal("6.998")
    assert result.items[0].total_amount == Decimal("349.93")


def test_normalize_receipt_response_raises_for_provider_error_code():
    with pytest.raises(ProverkachekaError) as exc_info:
        normalize_receipt_response({"code": 0})

    assert exc_info.value.code == 0
    assert str(exc_info.value) == "Receipt is incorrect"


def test_normalize_receipt_response_requires_code():
    with pytest.raises(ProverkachekaError) as exc_info:
        normalize_receipt_response({"data": {"json": {}}})

    assert str(exc_info.value) == "Proverkacheka API response does not include code"


def test_build_receipt_id_is_stable_and_uses_date_and_fiscal_sign():
    receipt_date = datetime(2026, 7, 8, 12, 30)
    first = build_receipt_id(
        ticket_date=receipt_date,
        fiscal_sign="1273019065",
    )

    assert first == build_receipt_id(
        ticket_date=receipt_date,
        fiscal_sign="1273019065",
    )
    assert first != build_receipt_id(
        ticket_date=datetime(2026, 7, 8, 12, 31),
        fiscal_sign="1273019065",
    )
    assert first != build_receipt_id(
        ticket_date=receipt_date,
        fiscal_sign="1273019066",
    )


def test_normalize_receipt_response_requires_receipt_identity_fields():
    with pytest.raises(ProverkachekaError) as exc_info:
        normalize_receipt_response({"code": 1, "data": {"json": {}}})

    assert "receipt date or fiscal sign" in str(exc_info.value)
