from fastapi import APIRouter

from app.api.routes import achievements, assistant, auth, cars, maintenance_records, receipts, trips


api_router = APIRouter()
api_router.include_router(auth.router)
api_router.include_router(achievements.router)
api_router.include_router(cars.router)
api_router.include_router(maintenance_records.router)
api_router.include_router(receipts.router)
api_router.include_router(trips.router)
api_router.include_router(assistant.router)
