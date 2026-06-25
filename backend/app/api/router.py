from fastapi import APIRouter

from app.api.routes import auth, cars, maintenance_records


api_router = APIRouter()
api_router.include_router(auth.router)
api_router.include_router(cars.router)
api_router.include_router(maintenance_records.router)
