from datetime import datetime
from hashlib import sha256
from urllib.parse import parse_qs


class ReceiptIdentityError(ValueError):
    pass


def build_receipt_id(*, ticket_date: datetime | None, fiscal_sign: str | None) -> str:
    """Build a stable receipt identifier from purchase time and the fiscal sign (FPD)."""
    if ticket_date is None or not fiscal_sign or not fiscal_sign.strip():
        raise ReceiptIdentityError("Receipt date and fiscal sign are required")

    canonical_date = ticket_date.strftime("%Y%m%dT%H%M%S")
    identity = f"{canonical_date}:{fiscal_sign.strip()}"
    return sha256(identity.encode("utf-8")).hexdigest()


def receipt_id_from_qrraw(qrraw: str) -> str:
    params = {key.lower(): values for key, values in parse_qs(qrraw).items()}
    raw_date = _first_param(params, "t")
    fiscal_sign = _first_param(params, "fp")
    ticket_date = _parse_qr_date(raw_date)
    if ticket_date is None or fiscal_sign is None:
        raise ReceiptIdentityError("QR code does not include a valid purchase date and FPD")
    return build_receipt_id(ticket_date=ticket_date, fiscal_sign=fiscal_sign)


def receipt_id_from_record_description(description: str | None) -> str | None:
    if not description:
        return None

    for line in reversed(description.splitlines()):
        label, separator, value = line.partition(":")
        if separator and label.strip().lower() == "qr":
            qrraw = value.strip()
            if not qrraw:
                raise ReceiptIdentityError("Receipt QR value is empty")
            return receipt_id_from_qrraw(qrraw)
    return None


def _first_param(params: dict[str, list[str]], key: str) -> str | None:
    values = params.get(key)
    if not values:
        return None
    value = values[0].strip()
    return value or None


def _parse_qr_date(value: str | None) -> datetime | None:
    if value is None:
        return None
    for fmt in ("%Y%m%dT%H%M%S", "%Y%m%dT%H%M"):
        try:
            return datetime.strptime(value, fmt)
        except ValueError:
            continue
    return None
