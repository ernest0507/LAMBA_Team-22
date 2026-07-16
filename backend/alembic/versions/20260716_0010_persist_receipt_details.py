"""persist receipt details

Revision ID: 20260716_0010
Revises: 20260716_0009
Create Date: 2026-07-16
"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


revision: str = "20260716_0010"
down_revision: Union[str, None] = "20260716_0009"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.add_column("maintenance_records", sa.Column("receipt_seller_name", sa.String(length=200), nullable=True))
    op.add_column("maintenance_records", sa.Column("receipt_seller_inn", sa.String(length=32), nullable=True))
    op.add_column(
        "maintenance_records",
        sa.Column("receipt_retail_place_address", sa.String(length=500), nullable=True),
    )
    op.add_column(
        "maintenance_records",
        sa.Column("receipt_ticket_date", sa.DateTime(timezone=True), nullable=True),
    )
    op.add_column("maintenance_records", sa.Column("receipt_total_amount", sa.Numeric(12, 2), nullable=True))
    op.add_column(
        "maintenance_records",
        sa.Column("receipt_fiscal_drive_number", sa.String(length=64), nullable=True),
    )
    op.add_column(
        "maintenance_records",
        sa.Column("receipt_fiscal_document_number", sa.String(length=64), nullable=True),
    )
    op.add_column("maintenance_records", sa.Column("receipt_fiscal_sign", sa.String(length=64), nullable=True))

    op.create_table(
        "maintenance_record_receipt_items",
        sa.Column("id", sa.Integer(), nullable=False),
        sa.Column("record_id", sa.Integer(), nullable=False),
        sa.Column("name", sa.String(length=500), nullable=True),
        sa.Column("quantity", sa.Numeric(12, 3), nullable=True),
        sa.Column("price_amount", sa.Numeric(12, 2), nullable=True),
        sa.Column("total_amount", sa.Numeric(12, 2), nullable=True),
        sa.ForeignKeyConstraint(["record_id"], ["maintenance_records.id"], ondelete="CASCADE"),
        sa.PrimaryKeyConstraint("id"),
    )
    op.create_index(
        op.f("ix_maintenance_record_receipt_items_id"),
        "maintenance_record_receipt_items",
        ["id"],
        unique=False,
    )
    op.create_index(
        op.f("ix_maintenance_record_receipt_items_record_id"),
        "maintenance_record_receipt_items",
        ["record_id"],
        unique=False,
    )


def downgrade() -> None:
    op.drop_index(
        op.f("ix_maintenance_record_receipt_items_record_id"),
        table_name="maintenance_record_receipt_items",
    )
    op.drop_index(op.f("ix_maintenance_record_receipt_items_id"), table_name="maintenance_record_receipt_items")
    op.drop_table("maintenance_record_receipt_items")

    op.drop_column("maintenance_records", "receipt_fiscal_sign")
    op.drop_column("maintenance_records", "receipt_fiscal_document_number")
    op.drop_column("maintenance_records", "receipt_fiscal_drive_number")
    op.drop_column("maintenance_records", "receipt_total_amount")
    op.drop_column("maintenance_records", "receipt_ticket_date")
    op.drop_column("maintenance_records", "receipt_retail_place_address")
    op.drop_column("maintenance_records", "receipt_seller_inn")
    op.drop_column("maintenance_records", "receipt_seller_name")
