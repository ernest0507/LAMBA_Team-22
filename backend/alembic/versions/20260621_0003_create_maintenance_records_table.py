"""create maintenance records table

Revision ID: 20260621_0003
Revises: 20260621_0002
Create Date: 2026-06-21
"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


revision: str = "20260621_0003"
down_revision: Union[str, None] = "20260621_0002"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.create_table(
        "maintenance_records",
        sa.Column("id", sa.Integer(), nullable=False),
        sa.Column("car_id", sa.Integer(), nullable=False),
        sa.Column("category", sa.String(length=32), nullable=True),
        sa.Column("title", sa.String(length=200), nullable=True),
        sa.Column("description", sa.Text(), nullable=True),
        sa.Column("occurred_at", sa.Date(), nullable=True),
        sa.Column("mileage_km", sa.Integer(), nullable=True),
        sa.Column("cost_amount", sa.Numeric(precision=12, scale=2), nullable=False),
        sa.Column("vendor", sa.String(length=200), nullable=True),
        sa.Column("created_at", sa.DateTime(timezone=True), server_default=sa.text("now()"), nullable=False),
        sa.Column("updated_at", sa.DateTime(timezone=True), server_default=sa.text("now()"), nullable=False),
        sa.ForeignKeyConstraint(["car_id"], ["cars.id"], ondelete="CASCADE"),
        sa.PrimaryKeyConstraint("id"),
    )
    op.create_index(
        op.f("ix_maintenance_records_car_id"),
        "maintenance_records",
        ["car_id"],
        unique=False,
    )
    op.create_index(
        op.f("ix_maintenance_records_category"),
        "maintenance_records",
        ["category"],
        unique=False,
    )
    op.create_index(
        op.f("ix_maintenance_records_id"),
        "maintenance_records",
        ["id"],
        unique=False,
    )
    op.create_index(
        op.f("ix_maintenance_records_occurred_at"),
        "maintenance_records",
        ["occurred_at"],
        unique=False,
    )


def downgrade() -> None:
    op.drop_index(op.f("ix_maintenance_records_occurred_at"), table_name="maintenance_records")
    op.drop_index(op.f("ix_maintenance_records_id"), table_name="maintenance_records")
    op.drop_index(op.f("ix_maintenance_records_category"), table_name="maintenance_records")
    op.drop_index(op.f("ix_maintenance_records_car_id"), table_name="maintenance_records")
    op.drop_table("maintenance_records")
