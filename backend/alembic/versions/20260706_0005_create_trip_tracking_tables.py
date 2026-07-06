"""create trip tracking tables

Revision ID: 20260706_0005
Revises: 20260630_0004
Create Date: 2026-07-06
"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


revision: str = "20260706_0005"
down_revision: Union[str, None] = "20260630_0004"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.create_table(
        "trips",
        sa.Column("id", sa.Integer(), nullable=False),
        sa.Column("car_id", sa.Integer(), nullable=False),
        sa.Column("started_at", sa.DateTime(timezone=True), nullable=False),
        sa.Column("ended_at", sa.DateTime(timezone=True), nullable=True),
        sa.Column("distance_m", sa.Numeric(precision=12, scale=2), server_default="0", nullable=False),
        sa.Column("duration_seconds", sa.Integer(), server_default="0", nullable=False),
        sa.Column("average_speed_kmh", sa.Numeric(precision=8, scale=2), server_default="0", nullable=False),
        sa.Column("max_speed_kmh", sa.Numeric(precision=8, scale=2), server_default="0", nullable=False),
        sa.Column("created_at", sa.DateTime(timezone=True), server_default=sa.text("now()"), nullable=False),
        sa.Column("updated_at", sa.DateTime(timezone=True), server_default=sa.text("now()"), nullable=False),
        sa.ForeignKeyConstraint(["car_id"], ["cars.id"], ondelete="CASCADE"),
        sa.PrimaryKeyConstraint("id"),
    )
    op.create_index(op.f("ix_trips_car_id"), "trips", ["car_id"], unique=False)
    op.create_index(op.f("ix_trips_ended_at"), "trips", ["ended_at"], unique=False)
    op.create_index(op.f("ix_trips_id"), "trips", ["id"], unique=False)
    op.create_index(op.f("ix_trips_started_at"), "trips", ["started_at"], unique=False)
    op.create_index(
        op.f("ix_trips_car_id_ended_at"),
        "trips",
        ["car_id", "ended_at"],
        unique=False,
    )

    op.create_table(
        "trip_points",
        sa.Column("id", sa.Integer(), nullable=False),
        sa.Column("trip_id", sa.Integer(), nullable=False),
        sa.Column("latitude", sa.Numeric(precision=10, scale=7), nullable=False),
        sa.Column("longitude", sa.Numeric(precision=10, scale=7), nullable=False),
        sa.Column("accuracy_m", sa.Numeric(precision=8, scale=2), nullable=True),
        sa.Column("speed_kmh", sa.Numeric(precision=8, scale=2), nullable=True),
        sa.Column("recorded_at", sa.DateTime(timezone=True), nullable=False),
        sa.Column("created_at", sa.DateTime(timezone=True), server_default=sa.text("now()"), nullable=False),
        sa.ForeignKeyConstraint(["trip_id"], ["trips.id"], ondelete="CASCADE"),
        sa.PrimaryKeyConstraint("id"),
    )
    op.create_index(op.f("ix_trip_points_id"), "trip_points", ["id"], unique=False)
    op.create_index(op.f("ix_trip_points_recorded_at"), "trip_points", ["recorded_at"], unique=False)
    op.create_index(op.f("ix_trip_points_trip_id"), "trip_points", ["trip_id"], unique=False)
    op.create_index(
        op.f("ix_trip_points_trip_id_recorded_at"),
        "trip_points",
        ["trip_id", "recorded_at"],
        unique=False,
    )


def downgrade() -> None:
    op.drop_index(op.f("ix_trip_points_trip_id_recorded_at"), table_name="trip_points")
    op.drop_index(op.f("ix_trip_points_trip_id"), table_name="trip_points")
    op.drop_index(op.f("ix_trip_points_recorded_at"), table_name="trip_points")
    op.drop_index(op.f("ix_trip_points_id"), table_name="trip_points")
    op.drop_table("trip_points")

    op.drop_index(op.f("ix_trips_car_id_ended_at"), table_name="trips")
    op.drop_index(op.f("ix_trips_started_at"), table_name="trips")
    op.drop_index(op.f("ix_trips_id"), table_name="trips")
    op.drop_index(op.f("ix_trips_ended_at"), table_name="trips")
    op.drop_index(op.f("ix_trips_car_id"), table_name="trips")
    op.drop_table("trips")
