from collections import defaultdict
from dataclasses import dataclass
from datetime import date, datetime, timedelta
from typing import Iterable

from app.models.maintenance_record import MaintenanceRecord
from app.schemas.achievement import AchievementUnlockType


@dataclass(frozen=True)
class AchievementDefinition:
    id: int
    key: str
    title: str
    description: str
    category: str
    unlock_type: AchievementUnlockType


ACHIEVEMENTS: tuple[AchievementDefinition, ...] = (
    AchievementDefinition(
        id=1,
        key="fuel_eater",
        title="Пожиратель топлива",
        description="Пользователь ввел 15+ литров расхода бензина",
        category="statistics",
        unlock_type=AchievementUnlockType.AUTOMATIC,
    ),
    AchievementDefinition(
        id=2,
        key="art_object",
        title="Арт-объект",
        description="Нет разницы в пробеге между его обновлениями через месяц",
        category="statistics",
        unlock_type=AchievementUnlockType.AUTOMATIC,
    ),
    AchievementDefinition(
        id=3,
        key="desperate",
        title="Отчаянный",
        description="Дата последнего ТО больше года назад",
        category="statistics",
        unlock_type=AchievementUnlockType.AUTOMATIC,
    ),
    AchievementDefinition(
        id=4,
        key="perfect_luck",
        title="Удача 100%",
        description="Отсутствуют записи о поломках на протяжении 6 месяцев",
        category="statistics",
        unlock_type=AchievementUnlockType.AUTOMATIC,
    ),
    AchievementDefinition(
        id=5,
        key="perpetual_motion",
        title="Вечный двигатель",
        description="Полтора года без замены масла",
        category="statistics",
        unlock_type=AchievementUnlockType.AUTOMATIC,
    ),
    AchievementDefinition(
        id=6,
        key="small_time_trucker",
        title="Дальнобойщик на минималках",
        description="Пробег за год > 45000",
        category="statistics",
        unlock_type=AchievementUnlockType.AUTOMATIC,
    ),
    AchievementDefinition(
        id=7,
        key="gas_station_regular",
        title="Старожил колонки",
        description="Количество заправок за год превысило 70",
        category="statistics",
        unlock_type=AchievementUnlockType.AUTOMATIC,
    ),
    AchievementDefinition(
        id=8,
        key="borrowed_fuel",
        title="Не глотай",
        description="Заправился от другой машины на дороге",
        category="road",
        unlock_type=AchievementUnlockType.MANUAL,
    ),
    AchievementDefinition(
        id=9,
        key="snow_king",
        title="Снежный король",
        description="Смог выехать из сугроба",
        category="road",
        unlock_type=AchievementUnlockType.MANUAL,
    ),
    AchievementDefinition(
        id=10,
        key="road_tow",
        title="Дорожная тяга",
        description="Отбуксировал другой автомобиль",
        category="road",
        unlock_type=AchievementUnlockType.MANUAL,
    ),
    AchievementDefinition(
        id=11,
        key="asphalt_prisoner",
        title="Пленник асфальта",
        description="Пришлось вызывать эвакуатор или просить отбуксироваться другого водителя",
        category="road",
        unlock_type=AchievementUnlockType.MANUAL,
    ),
    AchievementDefinition(
        id=12,
        key="wrong_fuel",
        title="ДТ-это ведь дорогое топливо?",
        description="Залил не тот бензин",
        category="road",
        unlock_type=AchievementUnlockType.MANUAL,
    ),
    AchievementDefinition(
        id=13,
        key="moon_rover",
        title="Луноход",
        description="Наехал на яму, хрустнула подвеска",
        category="road",
        unlock_type=AchievementUnlockType.MANUAL,
    ),
    AchievementDefinition(
        id=14,
        key="boiler",
        title="Кипятильник",
        description="Перегрев двигателя",
        category="road",
        unlock_type=AchievementUnlockType.MANUAL,
    ),
    AchievementDefinition(
        id=15,
        key="push_start",
        title="Как дед учил",
        description="Завел машину с толкача",
        category="repair",
        unlock_type=AchievementUnlockType.MANUAL,
    ),
    AchievementDefinition(
        id=16,
        key="energy_vampire",
        title="Энергетический вампир",
        description="Прикурил аккумулятор от другой машины",
        category="repair",
        unlock_type=AchievementUnlockType.MANUAL,
    ),
    AchievementDefinition(
        id=17,
        key="pit_stop_master",
        title="Мастер пит стопа",
        description="Сам поставил запаску",
        category="repair",
        unlock_type=AchievementUnlockType.MANUAL,
    ),
    AchievementDefinition(
        id=18,
        key="one_eyed_joe",
        title="Одноглазый Джо",
        description="Заменил фару",
        category="repair",
        unlock_type=AchievementUnlockType.MANUAL,
    ),
    AchievementDefinition(
        id=19,
        key="handy_owner",
        title="Руки из плеч",
        description="Ремонт любой поломки своими руками",
        category="repair",
        unlock_type=AchievementUnlockType.MANUAL,
    ),
    AchievementDefinition(
        id=20,
        key="parts_settling_in",
        title="Да это просто детали притираются",
        description="Отсутствие ремонта после сообщения о стуке в двигателе",
        category="repair",
        unlock_type=AchievementUnlockType.MANUAL,
    ),
)

ACHIEVEMENTS_BY_KEY = {achievement.key: achievement for achievement in ACHIEVEMENTS}
ACHIEVEMENTS_BY_ID = {achievement.id: achievement for achievement in ACHIEVEMENTS}


def get_achievement_definition(key: str) -> AchievementDefinition | None:
    return ACHIEVEMENTS_BY_KEY.get(key)


def get_achievement_definition_by_id(achievement_id: int) -> AchievementDefinition | None:
    return ACHIEVEMENTS_BY_ID.get(achievement_id)


def evaluate_statistics_achievement_keys(
    records: Iterable[MaintenanceRecord],
    today: date | None = None,
) -> set[str]:
    evaluation_date = today or date.today()
    dated_records = [
        (record, record_date)
        for record in records
        if (record_date := _record_date(record)) <= evaluation_date
    ]
    if not dated_records:
        return set()

    unlocked: set[str] = set()
    record_dates = [record_date for _, record_date in dated_records]
    history_start = min(record_dates)
    six_months_ago = _subtract_months(evaluation_date, 6)
    twelve_months_ago = _subtract_months(evaluation_date, 12)
    eighteen_months_ago = _subtract_months(evaluation_date, 18)

    if _has_unchanged_mileage_for_month(dated_records):
        unlocked.add("art_object")

    maintenance_dates = [
        record_date
        for record, record_date in dated_records
        if _category(record) == "maintenance"
    ]
    if maintenance_dates and max(maintenance_dates) < twelve_months_ago:
        unlocked.add("desperate")

    recent_repairs = [
        record
        for record, record_date in dated_records
        if record_date >= six_months_ago and _category(record) == "repair"
    ]
    if history_start <= six_months_ago and not recent_repairs:
        unlocked.add("perfect_luck")

    recent_oil_changes = [
        record
        for record, record_date in dated_records
        if record_date >= eighteen_months_ago and _is_oil_change(record)
    ]
    if history_start <= eighteen_months_ago and not recent_oil_changes:
        unlocked.add("perpetual_motion")

    year_mileage_records = sorted(
        (
            (record_date, record.mileage_km)
            for record, record_date in dated_records
            if record_date >= twelve_months_ago and record.mileage_km is not None
        ),
        key=lambda item: item[0],
    )
    if (
        len(year_mileage_records) >= 2
        and year_mileage_records[-1][1] - year_mileage_records[0][1] > 45_000
    ):
        unlocked.add("small_time_trucker")

    refueling_count = sum(
        1
        for record, record_date in dated_records
        if record_date >= twelve_months_ago and _is_refueling(record)
    )
    if refueling_count > 70:
        unlocked.add("gas_station_regular")

    return unlocked


def _has_unchanged_mileage_for_month(
    dated_records: list[tuple[MaintenanceRecord, date]],
) -> bool:
    dates_by_mileage: dict[int, list[date]] = defaultdict(list)
    for record, record_date in dated_records:
        if record.mileage_km is not None:
            dates_by_mileage[record.mileage_km].append(record_date)

    return any(
        max(record_dates) - min(record_dates) >= timedelta(days=30)
        for record_dates in dates_by_mileage.values()
        if len(record_dates) >= 2
    )


def _category(record: MaintenanceRecord) -> str | None:
    category = record.category
    return str(category) if category is not None else None


def _record_text(record: MaintenanceRecord) -> str:
    return " ".join(
        str(value).lower()
        for value in (record.category, record.title, record.description, record.vendor)
        if value
    )


def _is_oil_change(record: MaintenanceRecord) -> bool:
    text = _record_text(record)
    return any(token in text for token in ("oil change", "engine oil", "замена масла", "масло"))


def _is_refueling(record: MaintenanceRecord) -> bool:
    text = _record_text(record)
    return any(token in text for token in ("fuel", "gas", "petrol", "бенз", "заправ"))


def _record_date(record: MaintenanceRecord) -> date:
    if record.occurred_at is not None:
        return record.occurred_at
    if isinstance(record.created_at, datetime):
        return record.created_at.date()
    return record.created_at


def _subtract_months(value: date, months: int) -> date:
    month_index = value.year * 12 + value.month - 1 - months
    year, zero_based_month = divmod(month_index, 12)
    month = zero_based_month + 1
    next_month = date(year + (month == 12), month % 12 + 1, 1)
    last_day = (next_month - timedelta(days=1)).day
    return date(year, month, min(value.day, last_day))
