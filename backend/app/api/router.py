from fastapi import APIRouter

from app.api.routes import assistant, auth, cars, maintenance_records, trips


api_router = APIRouter()
api_router.include_router(auth.router)
api_router.include_router(cars.router)
api_router.include_router(maintenance_records.router)
api_router.include_router(trips.router)
api_router.include_router(assistant.router)
