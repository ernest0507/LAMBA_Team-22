from __future__ import annotations

from datetime import datetime
from decimal import Decimal, InvalidOperation, ROUND_HALF_UP
from typing import Any
from urllib.parse import parse_qs

import httpx

from app.core.config import Settings, get_settings
from app.schemas.receipt import ReceiptItem, ReceiptRead
from app.services.receipt_identity import ReceiptIdentityError, build_receipt_id


class ProverkachekaError(Exception):
    def __init__(self, message: str, *, code: int | None = None) -> None:
        super().__init__(message)
        self.code = code


class ProverkachekaNotConfiguredError(ProverkachekaError):
    pass


CODE_MESSAGES = {
    0: "Receipt is incorrect",
    1: "Receipt data received",
    2: "Receipt data is not ready yet",
    3: "Request limit exceeded",
    4: "Retry later",
    5: "Receipt data was not received",
}


async def scan_receipt_qrraw(
    qrraw: str,
    *,
    settings: Settings | None = None,
) -> ReceiptRead:
    settings = settings or get_settings()
    if not settings.proverkacheka_api_token:
        raise ProverkachekaNotConfiguredError("Proverkacheka API token is not configured")

    try:
        async with httpx.AsyncClient(timeout=settings.proverkacheka_request_timeout_seconds) as client:
            response = await client.post(
                settings.proverkacheka_api_url,
                data=_qrraw_request_data(
                    token=settings.proverkacheka_api_token,
                    qrraw=qrraw,
                ),
            )
            response.raise_for_status()
            payload = response.json()
            if not isinstance(payload, dict):
                raise ValueError("expected JSON object")
    except httpx.HTTPStatusError as exc:
        raise ProverkachekaError(
            f"Proverkacheka API returned HTTP {exc.response.status_code}"
        ) from exc
    except httpx.HTTPError as exc:
        raise ProverkachekaError("Could not reach Proverkacheka API") from exc
    except ValueError as exc:
        raise ProverkachekaError("Proverkacheka API returned invalid JSON") from exc

    return normalize_receipt_response(payload, qrraw=qrraw)


async def scan_receipt_qrfile(
    *,
    filename: str,
    content_type: str,
    data: bytes,
    settings: Settings | None = None,
) -> ReceiptRead:
    settings = settings or get_settings()
    if not settings.proverkacheka_api_token:
        raise ProverkachekaNotConfiguredError("Proverkacheka API token is not configured")

    try:
        async with httpx.AsyncClient(timeout=settings.proverkacheka_request_timeout_seconds) as client:
            response = await client.post(
                settings.proverkacheka_api_url,
                data={"token": settings.proverkacheka_api_token},
                files={"qrfile": (filename, data, content_type)},
            )
            response.raise_for_status()
            payload = response.json()
            if not isinstance(payload, dict):
                raise ValueError("expected JSON object")
    except httpx.HTTPStatusError as exc:
        raise ProverkachekaError(
            f"Proverkacheka API returned HTTP {exc.response.status_code}"
        ) from exc
    except httpx.HTTPError as exc:
        raise ProverkachekaError("Could not reach Proverkacheka API") from exc
    except ValueError as exc:
        raise ProverkachekaError("Proverkacheka API returned invalid JSON") from exc

    return normalize_receipt_response(payload)


def normalize_receipt_response(payload: dict[str, Any], *, qrraw: str | None = None) -> ReceiptRead:
    code = _optional_int(payload.get("code"))
    if code is None:
        raise ProverkachekaError("Proverkacheka API response does not include code")
    if code != 1:
        message = CODE_MESSAGES.get(code, "Receipt data was not received")
        provider_detail = _optional_str(payload.get("data"))
        if provider_detail and provider_detail != message:
            message = f"{message}: {provider_detail}"
        raise ProverkachekaError(message, code=code)

    receipt_json = _receipt_json(payload)
    qr_params = _parse_qr_params(qrraw)
    ticket_date = _optional_datetime(receipt_json.get("ticketDate")) or _optional_datetime(
        _first_qr_value(qr_params, "t")
    )
    fiscal_sign = _optional_str(receipt_json.get("fiscalSign")) or _first_qr_value(qr_params, "fp")
    try:
        receipt_id = build_receipt_id(ticket_date=ticket_date, fiscal_sign=fiscal_sign)
    except ReceiptIdentityError as exc:
        raise ProverkachekaError(
            "Proverkacheka API response does not include receipt date or fiscal sign"
        ) from exc
    return ReceiptRead(
        receipt_id=receipt_id,
        provider_code=code,
        status=CODE_MESSAGES[code],
        first=_optional_bool(payload.get("first")),
        seller_name=_optional_str(receipt_json.get("user")),
        seller_inn=_optional_str(receipt_json.get("userInn")),
        retail_place_address=_optional_str(
            receipt_json.get("retailPlaceAddres") or receipt_json.get("retailPlaceAddress")
        ),
        ticket_date=ticket_date,
        request_number=_optional_int(receipt_json.get("requestNumber")),
        shift_number=_optional_int(receipt_json.get("shiftNumber")),
        operator=_optional_str(receipt_json.get("operator")),
        operation_type=_optional_int(receipt_json.get("operationType")),
        total_amount=_kopecks_to_rubles(receipt_json.get("totalSum"))
        or _optional_decimal(_first_qr_value(qr_params, "s")),
        cash_total_amount=_kopecks_to_rubles(receipt_json.get("cashTotalSum")),
        ecash_total_amount=_kopecks_to_rubles(receipt_json.get("ecashTotalSum")),
        fiscal_drive_number=_optional_str(receipt_json.get("fiscalDriveNumber")) or _first_qr_value(qr_params, "fn"),
        fiscal_document_number=_optional_str(receipt_json.get("fiscalDocumentNumber"))
        or _first_qr_value(qr_params, "i", "fd"),
        fiscal_sign=fiscal_sign,
        items=_receipt_items(receipt_json.get("items")),
        raw=payload,
    )


def _qrraw_request_data(*, token: str, qrraw: str) -> dict[str, str]:
    data = {
        "token": token,
        "qrraw": qrraw,
    }
    params = _parse_qr_params(qrraw)
    manual_fields = {
        "fn": _first_qr_value(params, "fn"),
        "fd": _first_qr_value(params, "i", "fd"),
        "fp": _first_qr_value(params, "fp"),
        "t": _first_qr_value(params, "t"),
        "n": _first_qr_value(params, "n"),
        "s": _first_qr_value(params, "s"),
    }
    data.update({key: value for key, value in manual_fields.items() if value})
    if manual_fields["fn"] and manual_fields["fd"] and manual_fields["fp"]:
        data["qr"] = "1"
    return data


def _parse_qr_params(qrraw: str | None) -> dict[str, list[str]]:
    if not qrraw:
        return {}
    return {key.lower(): values for key, values in parse_qs(qrraw).items()}


def _first_qr_value(params: dict[str, list[str]], *keys: str) -> str | None:
    for key in keys:
        values = params.get(key)
        if values and values[0].strip():
            return values[0].strip()
    return None


def _receipt_json(payload: dict[str, Any]) -> dict[str, Any]:
    data = payload.get("data")
    if not isinstance(data, dict):
        return {}
    receipt_json = data.get("json")
    return receipt_json if isinstance(receipt_json, dict) else {}


def _receipt_items(value: Any) -> list[ReceiptItem]:
    if not isinstance(value, list):
        return []
    return [
        ReceiptItem(
            name=_optional_str(item.get("name")),
            price_amount=_kopecks_to_rubles(item.get("price")),
            quantity=_optional_decimal(item.get("quantity"), quant="0.001"),
            total_amount=_kopecks_to_rubles(item.get("sum")),
            raw=item,
        )
        for item in value
        if isinstance(item, dict)
    ]


def _kopecks_to_rubles(value: Any) -> Decimal | None:
    amount = _optional_decimal(value)
    if amount is None:
        return None
    return (amount / Decimal("100")).quantize(Decimal("0.01"), rounding=ROUND_HALF_UP)


def _optional_decimal(value: Any, *, quant: str = "0.01") -> Decimal | None:
    if value is None:
        return None
    try:
        return Decimal(str(value)).quantize(Decimal(quant), rounding=ROUND_HALF_UP)
    except (InvalidOperation, ValueError):
        return None


def _optional_int(value: Any) -> int | None:
    if value is None:
        return None
    try:
        return int(value)
    except (TypeError, ValueError):
        return None


def _optional_bool(value: Any) -> bool | None:
    if value is None:
        return None
    if isinstance(value, bool):
        return value
    if str(value) == "1":
        return True
    if str(value) == "0":
        return False
    return None


def _optional_str(value: Any) -> str | None:
    if value is None:
        return None
    text = str(value).strip()
    return text or None


def _optional_datetime(value: Any) -> datetime | None:
    text = _optional_str(value)
    if text is None:
        return None
    normalized = text.replace("Z", "+00:00")
    try:
        return datetime.fromisoformat(normalized)
    except ValueError:
        for fmt in ("%Y-%m-%d %H:%M:%S", "%Y%m%dT%H%M"):
            try:
                return datetime.strptime(text, fmt)
            except ValueError:
                continue
    return None
