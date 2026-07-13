"""create car achievements table

Revision ID: 20260710_0007
Revises: 20260710_0006
Create Date: 2026-07-10
"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


revision: str = "20260710_0007"
down_revision: Union[str, None] = "20260710_0006"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.create_table(
        "car_achievements",
        sa.Column("id", sa.Integer(), nullable=False),
        sa.Column("car_id", sa.Integer(), nullable=False),
        sa.Column("achievement_id", sa.Integer(), nullable=False),
        sa.Column("unlocked_at", sa.DateTime(timezone=True), server_default=sa.text("now()"), nullable=False),
        sa.Column("source", sa.String(length=32), nullable=True),
        sa.ForeignKeyConstraint(["car_id"], ["cars.id"], ondelete="CASCADE"),
        sa.PrimaryKeyConstraint("id"),
        sa.UniqueConstraint("car_id", "achievement_id", name="uq_car_achievement"),
    )
    op.create_index(op.f("ix_car_achievements_achievement_id"), "car_achievements", ["achievement_id"], unique=False)
    op.create_index(op.f("ix_car_achievements_car_id"), "car_achievements", ["car_id"], unique=False)
    op.create_index(op.f("ix_car_achievements_id"), "car_achievements", ["id"], unique=False)


def downgrade() -> None:
    op.drop_index(op.f("ix_car_achievements_id"), table_name="car_achievements")
    op.drop_index(op.f("ix_car_achievements_car_id"), table_name="car_achievements")
    op.drop_index(op.f("ix_car_achievements_achievement_id"), table_name="car_achievements")
    op.drop_table("car_achievements")
