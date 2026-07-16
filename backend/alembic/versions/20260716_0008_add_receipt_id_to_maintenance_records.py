"""add receipt id to maintenance records

Revision ID: 20260716_0008
Revises: 20260710_0007
Create Date: 2026-07-16
"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


revision: str = "20260716_0008"
down_revision: Union[str, None] = "20260710_0007"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.add_column(
        "maintenance_records",
        sa.Column("receipt_id", sa.String(length=64), nullable=True),
    )
    op.create_unique_constraint(
        "uq_maintenance_records_receipt_id",
        "maintenance_records",
        ["receipt_id"],
    )


def downgrade() -> None:
    op.drop_constraint(
        "uq_maintenance_records_receipt_id",
        "maintenance_records",
        type_="unique",
    )
    op.drop_column("maintenance_records", "receipt_id")
