"""create assistant chat history

Revision ID: 20260630_0004
Revises: 20260704_0004
Create Date: 2026-06-30
"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa


revision: str = "20260630_0004"
down_revision: Union[str, None] = "20260704_0004"
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.create_table(
        "assistant_chats",
        sa.Column("id", sa.Integer(), nullable=False),
        sa.Column("car_id", sa.Integer(), nullable=False),
        sa.Column("title", sa.String(length=120), nullable=True),
        sa.Column("created_at", sa.DateTime(timezone=True), server_default=sa.text("now()"), nullable=False),
        sa.Column("updated_at", sa.DateTime(timezone=True), server_default=sa.text("now()"), nullable=False),
        sa.ForeignKeyConstraint(["car_id"], ["cars.id"], ondelete="CASCADE"),
        sa.PrimaryKeyConstraint("id"),
    )
    op.create_index(op.f("ix_assistant_chats_car_id"), "assistant_chats", ["car_id"], unique=False)
    op.create_index(op.f("ix_assistant_chats_id"), "assistant_chats", ["id"], unique=False)

    op.create_table(
        "assistant_chat_messages",
        sa.Column("id", sa.Integer(), nullable=False),
        sa.Column("chat_id", sa.Integer(), nullable=False),
        sa.Column("role", sa.String(length=16), nullable=False),
        sa.Column("content", sa.Text(), nullable=False),
        sa.Column("action", sa.String(length=40), nullable=True),
        sa.Column("record_id", sa.Integer(), nullable=True),
        sa.Column("created_at", sa.DateTime(timezone=True), server_default=sa.text("now()"), nullable=False),
        sa.ForeignKeyConstraint(["chat_id"], ["assistant_chats.id"], ondelete="CASCADE"),
        sa.ForeignKeyConstraint(["record_id"], ["maintenance_records.id"], ondelete="SET NULL"),
        sa.PrimaryKeyConstraint("id"),
    )
    op.create_index(
        op.f("ix_assistant_chat_messages_chat_id"),
        "assistant_chat_messages",
        ["chat_id"],
        unique=False,
    )
    op.create_index(
        op.f("ix_assistant_chat_messages_id"),
        "assistant_chat_messages",
        ["id"],
        unique=False,
    )
    op.create_index(
        op.f("ix_assistant_chat_messages_role"),
        "assistant_chat_messages",
        ["role"],
        unique=False,
    )


def downgrade() -> None:
    op.drop_index(op.f("ix_assistant_chat_messages_role"), table_name="assistant_chat_messages")
    op.drop_index(op.f("ix_assistant_chat_messages_id"), table_name="assistant_chat_messages")
    op.drop_index(op.f("ix_assistant_chat_messages_chat_id"), table_name="assistant_chat_messages")
    op.drop_table("assistant_chat_messages")
    op.drop_index(op.f("ix_assistant_chats_id"), table_name="assistant_chats")
    op.drop_index(op.f("ix_assistant_chats_car_id"), table_name="assistant_chats")
    op.drop_table("assistant_chats")
