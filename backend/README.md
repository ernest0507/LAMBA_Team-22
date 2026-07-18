# LAMBA Backend API

FastAPI backend for the LAMBA car-care mobile application.

The backend provides user authentication, car profile storage, maintenance and expense history, receipt scanning, trip tracking, car statistics, achievements, and AI assistant flows.

## Tech Stack

- FastAPI
- SQLAlchemy async ORM
- PostgreSQL
- Alembic migrations
- JWT authentication
- pytest-based backend tests
- Ruff linting

## Local Setup

Create a local `.env` file from the template before starting the backend:

```powershell
cd backend
Copy-Item .env.example .env
```

Then install dependencies, start PostgreSQL and pgAdmin, run migrations, and start the API:

```powershell
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

Use these connection settings inside pgAdmin for the local Docker database:

```text
Host: postgres
Port: 5432
Database: lamba_db
Username: lamba_user
Password: change_me
```

From the host machine, PostgreSQL is exposed on:

```text
Host: 127.0.0.1
Port: 5433
```

unless `POSTGRES_PORT` is changed in `.env`.

## Configuration

The backend reads configuration from environment variables. Use `backend/.env.example` as the source of truth.

Current documented variables include:

```text
APP_NAME
API_PREFIX
DEBUG
POSTGRES_DB
POSTGRES_USER
POSTGRES_PASSWORD
POSTGRES_HOST
POSTGRES_PORT
PGADMIN_DEFAULT_EMAIL
PGADMIN_DEFAULT_PASSWORD
PGADMIN_PORT
SECRET_KEY
ACCESS_TOKEN_EXPIRE_MINUTES
AI_PROVIDER
AI_API_KEY
AI_BASE_URL
AI_AGENT_ID
AI_MODEL
AI_REQUEST_TIMEOUT_SECONDS
PROVERKACHEKA_API_TOKEN
PROVERKACHEKA_API_URL
PROVERKACHEKA_REQUEST_TIMEOUT_SECONDS
```

Do not commit real secrets or provider tokens. Keep local values in `.env`.

## API Prefix

Application routes are mounted under:

```text
/api/v1
```

The prefix can be changed with `API_PREFIX`.

## Authentication Endpoints

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `GET /api/v1/auth/me`
- `POST /api/v1/auth/logout`

Protected endpoints require a bearer token returned by the login endpoint.

## Car Profile Endpoints

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

Record lists support pagination and optional date filtering.

Maintenance records can include receipt metadata and receipt line items. Receipt identity is used to prevent duplicate uploaded receipts for the same car.

## Maintenance Record Photo Endpoints

- `POST /api/v1/cars/{car_id}/records/{record_id}/photos`
- `GET /api/v1/cars/{car_id}/records/{record_id}/photos`
- `GET /api/v1/cars/{car_id}/records/{record_id}/photos/{photo_id}`
- `DELETE /api/v1/cars/{car_id}/records/{record_id}/photos/{photo_id}`

A maintenance record can have up to 3 photos. Uploaded files must be images and each file must be no larger than 5 MB.

## Timeline and Statistics Endpoints

- `GET /api/v1/cars/{car_id}/timeline`
- `GET /api/v1/cars/{car_id}/statistics`

The timeline returns maintenance and expense history for a car. Statistics are built from the car's records.

## Receipt Scanning Endpoints

- `POST /api/v1/cars/{car_id}/receipts/scan`
- `POST /api/v1/cars/{car_id}/receipts/scan/file`

Receipt scanning uses the configured Proverkacheka provider settings. The file endpoint accepts images and PDFs up to 5 MB.

If the provider is not configured, the API returns `503 Service Unavailable`.

## Trip Tracking Endpoints

- `POST /api/v1/cars/{car_id}/trips/start`
- `GET /api/v1/cars/{car_id}/trips/active`
- `GET /api/v1/cars/{car_id}/trips`
- `GET /api/v1/trips/{trip_id}`
- `POST /api/v1/trips/{trip_id}/points`
- `POST /api/v1/trips/{trip_id}/finish`

Trip tracking supports active trip detection, GPS point batches, calculated trip metrics, and final mileage updates.

## Assistant Endpoints

- `GET /api/v1/cars/{car_id}/assistant/chats`
- `POST /api/v1/cars/{car_id}/assistant/chats`
- `GET /api/v1/cars/{car_id}/assistant/chats/{chat_id}/messages`
- `POST /api/v1/cars/{car_id}/assistant/chats/{chat_id}/messages`
- `POST /api/v1/cars/{car_id}/assistant/messages`

The assistant uses car context, recent chat history, and configured AI provider settings. It can help extract maintenance records from messages and update car mileage when the request is clear enough.

## Achievement Endpoints

- `GET /api/v1/achievements`
- `POST /api/v1/achievements/{achievement_key}/unlock`
- `GET /api/v1/cars/{car_id}/achievements`
- `POST /api/v1/cars/{car_id}/achievements/{achievement_id}/unlock`

Achievements include automatic statistics-based unlocks and manual unlocks.

## Database Migrations

Run all migrations:

```powershell
alembic upgrade head
```

Create a new migration after model changes:

```powershell
alembic revision --autogenerate -m "describe_change"
```

Review generated migrations before applying or committing them.

## Testing and Quality Checks

Run backend tests from the `backend` directory:

```powershell
pytest tests
```

Run linting:

```powershell
ruff check app tests
```

The GitHub Actions backend workflow also runs:

- Ruff linting
- dependency audit with `pip-audit`
- Alembic migrations
- backend tests with coverage



