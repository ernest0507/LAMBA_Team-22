from __future__ import annotations

import calendar
from collections import defaultdict
from datetime import date
from decimal import Decimal, ROUND_HALF_UP
from typing import Iterable

from app.models.maintenance_record import MaintenanceRecord
from app.schemas.statistics import (
    CarStatistics,
    StatisticsCategory,
    StatisticsChartPoint,
    StatisticsMetric,
    StatisticsPeriod,
)


DateRecord = tuple[MaintenanceRecord, date]

MONTH_LABELS = [
    "Янв",
    "Фев",
    "Мар",
    "Апр",
    "Май",
    "Июн",
    "Июл",
    "Авг",
    "Сен",
    "Окт",
    "Ноя",
    "Дек",
]

CATEGORY_TITLES = {
    "fuel": "Бензин",
    "maintenance": "Обслуживание",
    "repair": "Ремонт",
    "inspection": "Осмотр",
    "care": "Уход",
    "other": "Прочее",
}


def build_car_statistics(records: Iterable[MaintenanceRecord]) -> CarStatistics:
    dated_records = [(record, _record_date(record)) for record in records]
    today = date.today()

    return CarStatistics(
        month=[
            _build_period_statistics(dated_records, "month", key)
            for key in _period_keys(dated_records, "month", today)
        ],
        half_year=[
            _build_period_statistics(dated_records, "half_year", key)
            for key in _period_keys(dated_records, "half_year", today)
        ],
        year=[
            _build_period_statistics(dated_records, "year", key)
            for key in _period_keys(dated_records, "year", today)
        ],
    )


def _build_period_statistics(
    records: list[DateRecord],
    period: str,
    key: str,
) -> StatisticsPeriod:
    start, end = _period_bounds(period, key)
    previous_start, previous_end = _previous_period_bounds(period, start)
    current_records = _records_between(records, start, end)
    previous_records = _records_between(records, previous_start, previous_end)

    total_amount = _total_amount(current_records)
    previous_total_amount = _total_amount(previous_records)
    mileage_km = _period_mileage(current_records)
    previous_mileage_km = _period_mileage(previous_records)
    fuel_amount = _total_amount(
        [(record, record_date) for record, record_date in current_records if _category_key(record) == "fuel"]
    )
    previous_fuel_amount = _total_amount(
        [(record, record_date) for record, record_date in previous_records if _category_key(record) == "fuel"]
    )

    return StatisticsPeriod(
        period_key=key,
        period_title=_period_title(period, start, end),
        metrics=[
            StatisticsMetric(
                title="Расходы",
                value=_format_money(total_amount),
                delta=_format_delta(total_amount, previous_total_amount),
                type="expenses",
            ),
            StatisticsMetric(
                title="Пробег",
                value=f"{_format_int(mileage_km)} км",
                delta=_format_delta_int(mileage_km, previous_mileage_km),
                type="mileage",
            ),
            StatisticsMetric(
                title="Бензин",
                value=_format_money(fuel_amount),
                delta=_format_delta(fuel_amount, previous_fuel_amount),
                type="fuel",
            ),
        ],
        dynamics=_build_dynamics(current_records, period, start, end),
        dynamics_style="line" if period == "year" else "bar",
        categories=_build_categories(current_records),
        total_amount=total_amount,
        total_label=_period_total_label(period, start, end),
    )


def _period_keys(records: list[DateRecord], period: str, today: date) -> list[str]:
    keys = {_period_key(period, record_date) for _, record_date in records}
    keys.add(_period_key(period, today))
    sorted_keys = sorted(keys, key=lambda item: _period_bounds(period, item)[0])
    limit = {"month": 12, "half_year": 6, "year": 5}[period]
    return sorted_keys[-limit:]


def _period_key(period: str, value: date) -> str:
    if period == "month":
        return f"{value.year:04d}-{value.month:02d}"
    if period == "half_year":
        half = 1 if value.month <= 6 else 2
        return f"{value.year:04d}-H{half}"
    return f"{value.year:04d}"


def _period_bounds(period: str, key: str) -> tuple[date, date]:
    if period == "month":
        year, month = (int(part) for part in key.split("-"))
        last_day = calendar.monthrange(year, month)[1]
        return date(year, month, 1), date(year, month, last_day)

    if period == "half_year":
        year_text, half_text = key.split("-H")
        year = int(year_text)
        start_month = 1 if half_text == "1" else 7
        end_month = 6 if half_text == "1" else 12
        return date(year, start_month, 1), date(year, end_month, calendar.monthrange(year, end_month)[1])

    year = int(key)
    return date(year, 1, 1), date(year, 12, 31)


def _previous_period_bounds(period: str, start: date) -> tuple[date, date]:
    if period == "month":
        previous_month = start.month - 1
        previous_year = start.year
        if previous_month == 0:
            previous_month = 12
            previous_year -= 1
        key = f"{previous_year:04d}-{previous_month:02d}"
        return _period_bounds(period, key)

    if period == "half_year":
        if start.month == 1:
            key = f"{start.year - 1:04d}-H2"
        else:
            key = f"{start.year:04d}-H1"
        return _period_bounds(period, key)

    return _period_bounds(period, f"{start.year - 1:04d}")


def _records_between(records: list[DateRecord], start: date, end: date) -> list[DateRecord]:
    return [(record, record_date) for record, record_date in records if start <= record_date <= end]


def _total_amount(records: list[DateRecord]) -> Decimal:
    return sum((record.cost_amount for record, _ in records), Decimal("0.00"))


def _period_mileage(records: list[DateRecord]) -> int:
    values = sorted(record.mileage_km for record, _ in records if record.mileage_km is not None)
    if len(values) < 2:
        return 0
    return max(values[-1] - values[0], 0)


def _build_dynamics(
    records: list[DateRecord],
    period: str,
    start: date,
    end: date,
) -> list[StatisticsChartPoint]:
    if period == "month":
        buckets = [(f"Нед {index}", Decimal("0.00")) for index in range(1, 6)]
        totals = [amount for _, amount in buckets]
        for record, record_date in records:
            week_index = min((record_date.day - 1) // 7, 4)
            totals[week_index] += record.cost_amount
        return [
            StatisticsChartPoint(label=f"Нед {index + 1}", value=_chart_value(amount))
            for index, amount in enumerate(totals)
        ]

    if period == "half_year":
        month_numbers = list(range(start.month, end.month + 1))
        totals_by_month: dict[int, Decimal] = defaultdict(lambda: Decimal("0.00"))
        for record, record_date in records:
            totals_by_month[record_date.month] += record.cost_amount
        return [
            StatisticsChartPoint(
                label=MONTH_LABELS[month - 1],
                value=_chart_value(totals_by_month[month]),
            )
            for month in month_numbers
        ]

    totals_by_month = defaultdict(lambda: Decimal("0.00"))
    for record, record_date in records:
        totals_by_month[record_date.month] += record.cost_amount
    return [
        StatisticsChartPoint(label=MONTH_LABELS[month - 1][:1], value=_chart_value(totals_by_month[month]))
        for month in range(1, 13)
    ]


def _build_categories(records: list[DateRecord]) -> list[StatisticsCategory]:
    totals: dict[str, Decimal] = defaultdict(lambda: Decimal("0.00"))
    for record, _ in records:
        totals[_category_key(record)] += record.cost_amount

    total_amount = sum(totals.values(), Decimal("0.00"))
    if total_amount <= 0:
        return []

    return [
        StatisticsCategory(
            title=CATEGORY_TITLES.get(key, CATEGORY_TITLES["other"]),
            key=key,
            percent=int(((amount / total_amount) * Decimal("100")).quantize(Decimal("1"), rounding=ROUND_HALF_UP)),
            amount=_format_money(amount),
        )
        for key, amount in sorted(totals.items(), key=lambda item: item[1], reverse=True)
    ]


def _category_key(record: MaintenanceRecord) -> str:
    text = " ".join(
        value.lower()
        for value in [record.category, record.title, record.description]
        if value
    )

    if any(token in text for token in ["fuel", "gas", "petrol", "бенз", "заправ"]):
        return "fuel"
    if any(token in text for token in ["wash", "мойк", "уход"]):
        return "care"
    if record.category in {"maintenance", "repair", "inspection"}:
        return record.category
    return "other"


def _record_date(record: MaintenanceRecord) -> date:
    if record.occurred_at is not None:
        return record.occurred_at
    return record.created_at.date()


def _period_title(period: str, start: date, end: date) -> str:
    if period == "month":
        return f"{MONTH_LABELS[start.month - 1]} {start.year}"
    if period == "half_year":
        return f"{MONTH_LABELS[start.month - 1]} - {MONTH_LABELS[end.month - 1]} {start.year}"
    return str(start.year)


def _period_total_label(period: str, start: date, end: date) -> str:
    if period == "month":
        return MONTH_LABELS[start.month - 1]
    if period == "half_year":
        return "6 мес."
    return str(start.year)


def _format_delta(current: Decimal, previous: Decimal) -> str:
    if previous <= 0:
        return "Нет данных" if current <= 0 else "Новый период"
    percent = ((current - previous) / previous * Decimal("100")).quantize(
        Decimal("1"), rounding=ROUND_HALF_UP
    )
    return f"{'+' if percent >= 0 else ''}{percent}% к пред."


def _format_delta_int(current: int, previous: int) -> str:
    if previous <= 0:
        return "Нет данных" if current <= 0 else "Новый период"
    percent = Decimal(current - previous) / Decimal(previous) * Decimal("100")
    rounded = percent.quantize(Decimal("1"), rounding=ROUND_HALF_UP)
    return f"{'+' if rounded >= 0 else ''}{rounded}% к пред."


def _format_money(value: Decimal) -> str:
    rounded = value.quantize(Decimal("1"), rounding=ROUND_HALF_UP)
    return f"{_format_int(int(rounded))} ₽"


def _format_int(value: int) -> str:
    return f"{value:,}".replace(",", " ")


def _chart_value(value: Decimal) -> int:
    rounded = (value / Decimal("1000")).quantize(Decimal("1"), rounding=ROUND_HALF_UP)
    if value > 0 and rounded == 0:
        return 1
    return int(rounded)
