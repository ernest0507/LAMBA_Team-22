from decimal import Decimal

from pydantic import BaseModel


class StatisticsMetric(BaseModel):
    title: str
    value: str
    delta: str
    type: str


class StatisticsChartPoint(BaseModel):
    label: str
    value: int


class StatisticsCategory(BaseModel):
    title: str
    percent: int
    amount: str
    key: str


class StatisticsPeriod(BaseModel):
    period_key: str
    period_title: str
    metrics: list[StatisticsMetric]
    dynamics: list[StatisticsChartPoint]
    dynamics_style: str
    categories: list[StatisticsCategory]
    total_amount: Decimal
    total_label: str


class CarStatistics(BaseModel):
    month: list[StatisticsPeriod]
    half_year: list[StatisticsPeriod]
    year: list[StatisticsPeriod]
