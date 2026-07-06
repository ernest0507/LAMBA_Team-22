from datetime import datetime, timedelta, timezone
from decimal import Decimal
from types import SimpleNamespace

from app.services.trip_metrics import build_trip_metrics


def make_point(
    *,
    latitude: float,
    longitude: float,
    minutes: int,
    accuracy_m: float | None = 10,
    speed_kmh: float | None = None,
):
    return SimpleNamespace(
        latitude=latitude,
        longitude=longitude,
        accuracy_m=accuracy_m,
        speed_kmh=speed_kmh,
        recorded_at=datetime(2026, 7, 6, 12, 0, tzinfo=timezone.utc) + timedelta(minutes=minutes),
    )


def test_build_trip_metrics_calculates_distance_duration_and_speeds():
    points = [
        make_point(latitude=55.751244, longitude=37.618423, minutes=0, speed_kmh=0),
        make_point(latitude=55.752244, longitude=37.628423, minutes=10, speed_kmh=40),
    ]

    result = build_trip_metrics(points)

    assert Decimal("630") <= result.distance_m <= Decimal("650")
    assert result.duration_seconds == 600
    assert Decimal("3.70") <= result.average_speed_kmh <= Decimal("3.90")
    assert result.max_speed_kmh == Decimal("40.00")


def test_build_trip_metrics_returns_safe_defaults_for_empty_and_single_point():
    assert build_trip_metrics([]).distance_m == Decimal("0.00")
    assert build_trip_metrics([]).duration_seconds == 0

    result = build_trip_metrics([make_point(latitude=55.75, longitude=37.61, minutes=0)])

    assert result.distance_m == Decimal("0.00")
    assert result.duration_seconds == 0
    assert result.average_speed_kmh == Decimal("0.00")
    assert result.max_speed_kmh == Decimal("0.00")


def test_build_trip_metrics_ignores_invalid_coordinates_and_poor_accuracy():
    points = [
        make_point(latitude=55.751244, longitude=37.618423, minutes=0),
        make_point(latitude=95.0, longitude=37.620000, minutes=2),
        make_point(latitude=55.751500, longitude=37.620000, minutes=4, accuracy_m=500),
        make_point(latitude=55.752244, longitude=37.628423, minutes=10),
    ]

    result = build_trip_metrics(points)

    assert Decimal("630") <= result.distance_m <= Decimal("650")
    assert result.duration_seconds == 600


def test_build_trip_metrics_ignores_unrealistic_jumps():
    points = [
        make_point(latitude=55.751244, longitude=37.618423, minutes=0),
        make_point(latitude=59.938630, longitude=30.314130, minutes=1),
        make_point(latitude=55.752244, longitude=37.628423, minutes=10),
    ]

    result = build_trip_metrics(points, max_segment_speed_kmh=250)

    assert Decimal("630") <= result.distance_m <= Decimal("650")
    assert result.duration_seconds == 600
    assert Decimal("3.70") <= result.average_speed_kmh <= Decimal("3.90")


def test_build_trip_metrics_processes_points_chronologically():
    points = [
        make_point(latitude=55.752244, longitude=37.628423, minutes=10),
        make_point(latitude=55.751244, longitude=37.618423, minutes=0),
    ]

    result = build_trip_metrics(points)

    assert Decimal("630") <= result.distance_m <= Decimal("650")
    assert result.duration_seconds == 600


def test_build_trip_metrics_uses_explicit_trip_start_and_end_for_duration():
    started_at = datetime(2026, 7, 6, 11, 55, tzinfo=timezone.utc)
    ended_at = datetime(2026, 7, 6, 12, 15, tzinfo=timezone.utc)

    result = build_trip_metrics(
        [make_point(latitude=55.751244, longitude=37.618423, minutes=0)],
        started_at=started_at,
        ended_at=ended_at,
    )

    assert result.duration_seconds == 1200
