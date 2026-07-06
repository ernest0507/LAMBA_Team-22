from __future__ import annotations

from dataclasses import dataclass
from datetime import datetime, timezone
from decimal import Decimal, ROUND_HALF_UP
from math import asin, cos, radians, sin, sqrt
from typing import Any, Iterable


EARTH_RADIUS_M = 6_371_000
DEFAULT_MAX_ACCURACY_M = 100
DEFAULT_MAX_SEGMENT_SPEED_KMH = 250


@dataclass(frozen=True)
class TripMetrics:
    distance_m: Decimal
    duration_seconds: int
    average_speed_kmh: Decimal
    max_speed_kmh: Decimal


@dataclass(frozen=True)
class ValidTripPoint:
    latitude: float
    longitude: float
    accuracy_m: float | None
    speed_kmh: float | None
    recorded_at: datetime


def build_trip_metrics(
    points: Iterable[Any],
    *,
    started_at: datetime | None = None,
    ended_at: datetime | None = None,
    max_accuracy_m: float = DEFAULT_MAX_ACCURACY_M,
    max_segment_speed_kmh: float = DEFAULT_MAX_SEGMENT_SPEED_KMH,
) -> TripMetrics:
    valid_points = sorted(
        (
            point
            for point in (_to_valid_point(point) for point in points)
            if point is not None
            and _has_acceptable_accuracy(point, max_accuracy_m)
        ),
        key=lambda point: _timestamp_seconds(point.recorded_at),
    )

    distance_m = 0.0
    max_speed_kmh = 0.0

    for point in valid_points:
        if point.speed_kmh is not None and 0 <= point.speed_kmh <= max_segment_speed_kmh:
            max_speed_kmh = max(max_speed_kmh, point.speed_kmh)

    previous: ValidTripPoint | None = None
    for point in valid_points:
        if previous is None:
            previous = point
            continue

        elapsed_seconds = _timestamp_seconds(point.recorded_at) - _timestamp_seconds(previous.recorded_at)
        if elapsed_seconds <= 0:
            previous = point
            continue

        segment_distance_m = _haversine_distance_m(previous, point)
        segment_speed_kmh = (segment_distance_m / elapsed_seconds) * 3.6
        if segment_speed_kmh > max_segment_speed_kmh:
            continue

        distance_m += segment_distance_m
        max_speed_kmh = max(max_speed_kmh, segment_speed_kmh)
        previous = point

    duration_seconds = _duration_seconds(valid_points, started_at=started_at, ended_at=ended_at)
    average_speed_kmh = 0.0
    if duration_seconds > 0 and distance_m > 0:
        average_speed_kmh = (distance_m / 1000) / (duration_seconds / 3600)

    return TripMetrics(
        distance_m=_decimal(distance_m, "0.01"),
        duration_seconds=duration_seconds,
        average_speed_kmh=_decimal(average_speed_kmh, "0.01"),
        max_speed_kmh=_decimal(max_speed_kmh, "0.01"),
    )


def _to_valid_point(point: Any) -> ValidTripPoint | None:
    latitude = _optional_float(_point_value(point, "latitude"))
    longitude = _optional_float(_point_value(point, "longitude"))
    recorded_at = _point_value(point, "recorded_at")

    if latitude is None or longitude is None or not isinstance(recorded_at, datetime):
        return None
    if not (-90 <= latitude <= 90 and -180 <= longitude <= 180):
        return None

    accuracy_m = _optional_float(_point_value(point, "accuracy_m"))
    speed_kmh = _optional_float(_point_value(point, "speed_kmh"))
    return ValidTripPoint(
        latitude=latitude,
        longitude=longitude,
        accuracy_m=accuracy_m,
        speed_kmh=speed_kmh,
        recorded_at=recorded_at,
    )


def _point_value(point: Any, name: str) -> Any:
    if isinstance(point, dict):
        return point.get(name)
    return getattr(point, name, None)


def _optional_float(value: Any) -> float | None:
    if value is None:
        return None
    try:
        return float(value)
    except (TypeError, ValueError):
        return None


def _has_acceptable_accuracy(point: ValidTripPoint, max_accuracy_m: float) -> bool:
    return point.accuracy_m is None or 0 <= point.accuracy_m <= max_accuracy_m


def _duration_seconds(
    points: list[ValidTripPoint],
    *,
    started_at: datetime | None,
    ended_at: datetime | None,
) -> int:
    if started_at is not None and ended_at is not None:
        return max(int(_timestamp_seconds(ended_at) - _timestamp_seconds(started_at)), 0)
    if len(points) < 2:
        return 0
    return max(
        int(_timestamp_seconds(points[-1].recorded_at) - _timestamp_seconds(points[0].recorded_at)),
        0,
    )


def _timestamp_seconds(value: datetime) -> float:
    if value.tzinfo is None:
        value = value.replace(tzinfo=timezone.utc)
    return value.timestamp()


def _haversine_distance_m(start: ValidTripPoint, end: ValidTripPoint) -> float:
    lat1 = radians(start.latitude)
    lat2 = radians(end.latitude)
    delta_lat = radians(end.latitude - start.latitude)
    delta_lon = radians(end.longitude - start.longitude)

    haversine = sin(delta_lat / 2) ** 2 + cos(lat1) * cos(lat2) * sin(delta_lon / 2) ** 2
    return 2 * EARTH_RADIUS_M * asin(sqrt(haversine))


def _decimal(value: float, quant: str) -> Decimal:
    return Decimal(str(value)).quantize(Decimal(quant), rounding=ROUND_HALF_UP)
