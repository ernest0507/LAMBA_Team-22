from dataclasses import dataclass

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
