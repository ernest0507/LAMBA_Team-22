# Lamba Backend API

FastAPI backend for MVP v1 user authentication and vehicle digital twin storage.

## Local Setup

```powershell
cd backend
python -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
docker compose up -d postgres pgadmin
alembic upgrade head
python -m uvicorn app.main:app --reload
```

Health check:

```text
GET http://127.0.0.1:8000/health
```

API docs:

```text
http://127.0.0.1:8000/docs
```

pgAdmin:

```text
http://127.0.0.1:5050
```

The pgAdmin login is configured through `PGADMIN_DEFAULT_EMAIL` and
`PGADMIN_DEFAULT_PASSWORD` in `.env`.

Use these connection settings inside pgAdmin for the local database:

```text
Host: postgres
Port: 5432
Database: lamba_db
Username: lamba_user
Password: change_me
```

## Authentication Endpoints

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `GET /api/v1/auth/me`

## Car Digital Twin Endpoints

- `GET /api/v1/cars`
- `POST /api/v1/cars`
- `GET /api/v1/cars/{car_id}`
- `PATCH /api/v1/cars/{car_id}`
- `DELETE /api/v1/cars/{car_id}`

## Maintenance Record Endpoints

- `GET /api/v1/cars/{car_id}/records`
- `POST /api/v1/cars/{car_id}/records`
- `GET /api/v1/cars/{car_id}/records/{record_id}`
- `PATCH /api/v1/cars/{car_id}/records/{record_id}`
- `DELETE /api/v1/cars/{car_id}/records/{record_id}`
- `GET /api/v1/cars/{car_id}/timeline`
