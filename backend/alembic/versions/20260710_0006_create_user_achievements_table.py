"""create user achievements table

Revision ID: 20260710_0006
Revises: 20260706_0005
Create Date: 2026-07-10
"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


revision: str = "20260710_0006"
down_revision: Union[str, None] = "20260706_0005"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.create_table(
        "user_achievements",
        sa.Column("id", sa.Integer(), nullable=False),
        sa.Column("user_id", sa.Integer(), nullable=False),
        sa.Column("achievement_key", sa.String(length=80), nullable=False),
        sa.Column(
            "unlocked_at",
            sa.DateTime(timezone=True),
            server_default=sa.text("now()"),
            nullable=False,
        ),
        sa.ForeignKeyConstraint(["user_id"], ["users.id"], ondelete="CASCADE"),
        sa.PrimaryKeyConstraint("id"),
        sa.UniqueConstraint(
            "user_id",
            "achievement_key",
            name="uq_user_achievements_user_key",
        ),
    )
    op.create_index(
        op.f("ix_user_achievements_achievement_key"),
        "user_achievements",
        ["achievement_key"],
        unique=False,
    )
    op.create_index(
        op.f("ix_user_achievements_id"),
        "user_achievements",
        ["id"],
        unique=False,
    )
    op.create_index(
        op.f("ix_user_achievements_user_id"),
        "user_achievements",
        ["user_id"],
        unique=False,
    )


def downgrade() -> None:
    op.drop_index(op.f("ix_user_achievements_user_id"), table_name="user_achievements")
    op.drop_index(op.f("ix_user_achievements_id"), table_name="user_achievements")
    op.drop_index(op.f("ix_user_achievements_achievement_key"), table_name="user_achievements")
    op.drop_table("user_achievements")
