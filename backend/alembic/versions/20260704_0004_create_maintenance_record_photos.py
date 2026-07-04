"""create maintenance record photos

Revision ID: 20260704_0004
Revises: 20260621_0003
Create Date: 2026-07-04
"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


revision: str = "20260704_0004"
down_revision: Union[str, None] = "20260621_0003"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.create_table(
        "maintenance_record_photos",
        sa.Column("id", sa.Integer(), nullable=False),
        sa.Column("record_id", sa.Integer(), nullable=False),
        sa.Column("filename", sa.String(length=255), nullable=False),
        sa.Column("content_type", sa.String(length=100), nullable=False),
        sa.Column("size_bytes", sa.Integer(), nullable=False),
        sa.Column("data", sa.LargeBinary(), nullable=False),
        sa.Column("created_at", sa.DateTime(timezone=True), server_default=sa.text("now()"), nullable=False),
        sa.ForeignKeyConstraint(["record_id"], ["maintenance_records.id"], ondelete="CASCADE"),
        sa.PrimaryKeyConstraint("id"),
    )
    op.create_index(
        op.f("ix_maintenance_record_photos_id"),
        "maintenance_record_photos",
        ["id"],
        unique=False,
    )
    op.create_index(
        op.f("ix_maintenance_record_photos_record_id"),
        "maintenance_record_photos",
        ["record_id"],
        unique=False,
    )


def downgrade() -> None:
    op.drop_index(op.f("ix_maintenance_record_photos_record_id"), table_name="maintenance_record_photos")
    op.drop_index(op.f("ix_maintenance_record_photos_id"), table_name="maintenance_record_photos")
    op.drop_table("maintenance_record_photos")
