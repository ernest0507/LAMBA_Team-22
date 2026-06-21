"""create cars table

Revision ID: 20260621_0002
Revises: 20260621_0001
Create Date: 2026-06-21
"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


revision: str = "20260621_0002"
down_revision: Union[str, None] = "20260621_0001"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.create_table(
        "cars",
        sa.Column("id", sa.Integer(), nullable=False),
        sa.Column("owner_id", sa.Integer(), nullable=False),
        sa.Column("make", sa.String(length=100), nullable=True),
        sa.Column("model", sa.String(length=100), nullable=False),
        sa.Column("year", sa.Integer(), nullable=False),
        sa.Column("current_mileage_km", sa.Integer(), nullable=False),
        sa.Column("color", sa.String(length=32), nullable=True),
        sa.Column("body_type", sa.String(length=32), nullable=True),
        sa.Column("notes", sa.String(length=1000), nullable=True),
        sa.Column("created_at", sa.DateTime(timezone=True), server_default=sa.text("now()"), nullable=False),
        sa.Column("updated_at", sa.DateTime(timezone=True), server_default=sa.text("now()"), nullable=False),
        sa.ForeignKeyConstraint(["owner_id"], ["users.id"], ondelete="CASCADE"),
        sa.PrimaryKeyConstraint("id"),
    )
    op.create_index(op.f("ix_cars_id"), "cars", ["id"], unique=False)
    op.create_index(op.f("ix_cars_owner_id"), "cars", ["owner_id"], unique=False)


def downgrade() -> None:
    op.drop_index(op.f("ix_cars_owner_id"), table_name="cars")
    op.drop_index(op.f("ix_cars_id"), table_name="cars")
    op.drop_table("cars")
