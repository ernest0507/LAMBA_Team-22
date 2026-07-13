from datetime import datetime

from sqlalchemy import DateTime, ForeignKey, Integer, String, UniqueConstraint, func
from sqlalchemy.orm import Mapped, mapped_column

from app.core.database import Base


class CarAchievement(Base):
    __tablename__ = "car_achievements"
    __table_args__ = (
        UniqueConstraint("car_id", "achievement_id", name="uq_car_achievement"),
    )

    id: Mapped[int] = mapped_column(primary_key=True, index=True)
    car_id: Mapped[int] = mapped_column(ForeignKey("cars.id", ondelete="CASCADE"), index=True)
    achievement_id: Mapped[int] = mapped_column(Integer, index=True)
    unlocked_at: Mapped[datetime] = mapped_column(DateTime(timezone=True), server_default=func.now())
    source: Mapped[str | None] = mapped_column(String(32), nullable=True)
