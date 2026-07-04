from datetime import datetime

from pydantic import BaseModel, ConfigDict


class RecordPhotoRead(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: int
    record_id: int
    filename: str
    content_type: str
    size_bytes: int
    created_at: datetime
    url: str
