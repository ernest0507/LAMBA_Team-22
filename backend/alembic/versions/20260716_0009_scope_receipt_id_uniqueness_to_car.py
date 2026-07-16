"""scope receipt id uniqueness to car

Revision ID: 20260716_0009
Revises: 20260716_0008
Create Date: 2026-07-16
"""
from typing import Sequence, Union

from alembic import op


revision: str = "20260716_0009"
down_revision: Union[str, None] = "20260716_0008"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.drop_constraint(
        "uq_maintenance_records_receipt_id",
        "maintenance_records",
        type_="unique",
    )
    op.create_unique_constraint(
        "uq_maintenance_records_car_receipt_id",
        "maintenance_records",
        ["car_id", "receipt_id"],
    )


def downgrade() -> None:
    op.drop_constraint(
        "uq_maintenance_records_car_receipt_id",
        "maintenance_records",
        type_="unique",
    )
    op.create_unique_constraint(
        "uq_maintenance_records_receipt_id",
        "maintenance_records",
        ["receipt_id"],
    )
