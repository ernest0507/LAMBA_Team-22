from datetime import datetime

from sqlalchemy import DateTime, ForeignKey, Integer, LargeBinary, String, func
from sqlalchemy.orm import Mapped, mapped_column

from app.core.database import Base


class MaintenanceRecordPhoto(Base):
    __tablename__ = "maintenance_record_photos"

    id: Mapped[int] = mapped_column(primary_key=True, index=True)
    record_id: Mapped[int] = mapped_column(
        ForeignKey("maintenance_records.id", ondelete="CASCADE"), index=True
    )
    filename: Mapped[str] = mapped_column(String(255))
    content_type: Mapped[str] = mapped_column(String(100))
    size_bytes: Mapped[int] = mapped_column(Integer)
    data: Mapped[bytes] = mapped_column(LargeBinary)
    created_at: Mapped[datetime] = mapped_column(DateTime(timezone=True), server_default=func.now())
