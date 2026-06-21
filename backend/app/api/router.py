from fastapi import APIRouter

from app.api.routes import auth, cars


api_router = APIRouter()
api_router.include_router(auth.router)
api_router.include_router(cars.router)
