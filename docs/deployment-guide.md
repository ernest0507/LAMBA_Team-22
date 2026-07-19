# LAMBA Deployment and Operations Guide

## 1. Purpose

This guide explains how the customer can deploy, verify, operate, back up, and update the LAMBA backend on customer-controlled infrastructure.

The final handover model is an **independent customer-side deployment**:

- the customer selects and pays for the hosting infrastructure;
- the customer creates and administers the server;
- the customer stores all production credentials and secrets;
- the customer deploys and operates the backend and PostgreSQL database;
- the team transfers the source code, configuration template, APK/build instructions, database migrations, and this guide.

The current team-operated HOSTKEY server is a temporary reference environment. It is **not transferred to the customer**, and the customer does not require SSH access to it.

## 2. Verified reference environment

The following environment was used by the team and serves as a working reference:

| Item | Verified value |
|---|---|
| Hosting provider | HOSTKEY |
| Operating system | Ubuntu 24.04 |
| Temporary backend address | `http://5.180.174.64:8000` |
| HTTPS | Not configured |
| Backend process | Uvicorn managed by `systemd` |
| Backend service | `lamba-backend` |
| Backend directory | `/opt/lamba-backend/LAMBA_Team-22/backend` |
| Environment file | `/opt/lamba-backend/LAMBA_Team-22/backend/.env` |
| PostgreSQL | 17.10, operated through Docker Compose |
| pgAdmin | Operated through Docker Compose |
| Python version used by CI | 3.12 |

The Android project currently uses:

```text
http://5.180.174.64:8000/
```

as `BACKEND_BASE_URL`. This is the temporary team endpoint and must be replaced when the customer deploys the backend at a different address.

## 3. Target architecture

The recommended customer-side deployment consists of:

1. an Ubuntu server controlled by the customer;
2. PostgreSQL running through the repository's Docker Compose configuration;
3. the FastAPI application running in a Python virtual environment;
4. a `systemd` service managing Uvicorn;
5. a customer-controlled domain and HTTPS reverse proxy for production use;
6. an Android APK rebuilt with the customer backend URL.

The repository Docker Compose file starts PostgreSQL and pgAdmin. It does not start the FastAPI backend.

## 4. Prerequisites

The customer server should provide:

- Ubuntu 24.04 or another supported Linux distribution;
- SSH access controlled by the customer;
- Git;
- Python 3.12 with `venv`;
- Docker Engine and Docker Compose;
- a firewall;
- enough disk space for source code, database data, backups, and logs;
- optional domain and DNS control for HTTPS.

Example package installation on Ubuntu:

```bash
sudo apt update
sudo apt install -y git python3 python3-venv python3-pip
```

Install Docker using the official Docker instructions for the selected operating system.

Verify the tools:

```bash
python3 --version
git --version
docker --version
docker compose version
```

## 5. Obtain the source code

The customer should receive a complete handover archive containing frontend, backend, migrations, documentation, and configuration examples.

Example destination:

```bash
sudo mkdir -p /opt/lamba
sudo chown "$USER":"$USER" /opt/lamba
cd /opt/lamba
```

Extract the transferred archive so the repository structure contains:

```text
LAMBA_Team-22/
├── backend/
├── frontend/
├── docs/
└── ...
```

For the commands below:

```bash
export LAMBA_REPO=/opt/lamba/LAMBA_Team-22
export LAMBA_BACKEND="$LAMBA_REPO/backend"
cd "$LAMBA_BACKEND"
```

The customer may choose another path. All service and configuration paths must then be updated consistently.

## 6. Configure environment variables

Create the production environment file from the supplied example:

```bash
cd "$LAMBA_BACKEND"
cp .env.example .env
chmod 600 .env
```

Do not commit `.env` to Git and do not place real credentials in public documentation.

Recommended structure:

```dotenv
APP_NAME=Lamba Car Care API
API_PREFIX=/api/v1
DEBUG=false

POSTGRES_DB=lamba_db
POSTGRES_USER=lamba_user
POSTGRES_PASSWORD=<GENERATE_A_STRONG_DATABASE_PASSWORD>
POSTGRES_HOST=127.0.0.1
POSTGRES_PORT=5433

PGADMIN_DEFAULT_EMAIL=<CUSTOMER_ADMIN_EMAIL>
PGADMIN_DEFAULT_PASSWORD=<GENERATE_A_STRONG_PGADMIN_PASSWORD>
PGADMIN_PORT=5050

SECRET_KEY=<GENERATE_A_LONG_RANDOM_SECRET>
ACCESS_TOKEN_EXPIRE_MINUTES=1440

AI_PROVIDER=timeweb
AI_API_KEY=<CUSTOMER_AI_API_KEY>
AI_BASE_URL=<CUSTOMER_AI_BASE_URL>
AI_AGENT_ID=<CUSTOMER_AI_AGENT_ID>
AI_MODEL=default
AI_REQUEST_TIMEOUT_SECONDS=30

PROVERKACHEKA_API_TOKEN=<CUSTOMER_PROVERKACHEKA_TOKEN>
PROVERKACHEKA_API_URL=https://proverkacheka.com/api/v1/check/get
PROVERKACHEKA_REQUEST_TIMEOUT_SECONDS=20
```

`DATABASE_URL` is optional. When it is not provided, the backend builds the connection string from the individual PostgreSQL variables.

Generate a secret locally, for example:

```bash
python3 -c "import secrets; print(secrets.token_urlsafe(64))"
```

Use the result as `SECRET_KEY`.

### Credential ownership

For the customer deployment:

- the customer controls the server and SSH keys;
- the customer creates or stores the PostgreSQL password;
- the customer creates the application `SECRET_KEY`;
- the customer controls the AI credentials;
- the customer obtains and controls the Proverkacheka token;
- credentials are transferred only through a private channel;
- credentials should be rotated if they have previously been shared.

## 7. Start PostgreSQL and pgAdmin

The supplied Docker Compose configuration uses PostgreSQL 17 and binds its host port to `127.0.0.1`. Start the database services:

```bash
cd "$LAMBA_BACKEND"
docker compose up -d postgres pgadmin
```

Check their state:

```bash
docker compose ps
docker compose logs --tail=100 postgres
```

The FastAPI backend runs on the host, so its `.env` should normally use:

```dotenv
POSTGRES_HOST=127.0.0.1
POSTGRES_PORT=5433
```

Inside pgAdmin, the Docker network connection uses:

```text
Host: postgres
Port: 5432
Database: value of POSTGRES_DB
Username: value of POSTGRES_USER
Password: value of POSTGRES_PASSWORD
```

pgAdmin should not be exposed publicly. Access it through a secure tunnel or another customer-approved restricted method.

## 8. Install backend dependencies

Create a virtual environment and install the backend requirements:

```bash
cd "$LAMBA_BACKEND"
python3 -m venv .venv
source .venv/bin/activate
python -m pip install --upgrade pip
pip install -r requirements.txt
```

The backend includes FastAPI, Uvicorn, SQLAlchemy, asyncpg, Alembic, Pydantic, HTTPX, and the OpenAI client.

## 9. Apply database migrations

With PostgreSQL running and `.env` configured:

```bash
cd "$LAMBA_BACKEND"
source .venv/bin/activate
alembic upgrade head
```

Check the migration state:

```bash
alembic current
alembic heads
```

Migrations must be applied:

- during the initial deployment;
- after pulling or extracting a backend update that adds a migration;
- before starting a new backend version that depends on the changed schema.

Create a database backup before applying migrations to an existing production database.

## 10. Test the backend manually

Before creating the service, run Uvicorn in the foreground:

```bash
cd "$LAMBA_BACKEND"
source .venv/bin/activate
python -m uvicorn app.main:app --host 0.0.0.0 --port 8000
```

In another terminal, verify:

```bash
curl -fsS http://127.0.0.1:8000/health
```

Open the API documentation locally:

```text
http://SERVER_IP:8000/docs
```

Stop the foreground process with `Ctrl+C` after verification.

## 11. Configure the systemd service

A dedicated non-root service account is recommended:

```bash
sudo useradd --system --home /opt/lamba --shell /usr/sbin/nologin lamba
sudo chown -R lamba:lamba /opt/lamba/LAMBA_Team-22
```

Create:

```text
/etc/systemd/system/lamba-backend.service
```

Example customer-side service:

```ini
[Unit]
Description=LAMBA Backend API
After=network-online.target docker.service
Wants=network-online.target
Requires=docker.service

[Service]
Type=simple
User=lamba
Group=lamba
WorkingDirectory=/opt/lamba/LAMBA_Team-22/backend
EnvironmentFile=/opt/lamba/LAMBA_Team-22/backend/.env
ExecStart=/opt/lamba/LAMBA_Team-22/backend/.venv/bin/python -m uvicorn app.main:app --host 127.0.0.1 --port 8000
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
```

If no reverse proxy is used and port 8000 must temporarily be reached from another device, use `--host 0.0.0.0`. Direct public exposure is not recommended for the final production deployment.

Load and start the service:

```bash
sudo systemctl daemon-reload
sudo systemctl enable --now lamba-backend
sudo systemctl status lamba-backend
```

Management commands:

```bash
sudo systemctl start lamba-backend
sudo systemctl stop lamba-backend
sudo systemctl restart lamba-backend
sudo systemctl status lamba-backend
```

## 12. Network, domain, and HTTPS

The team reference deployment uses HTTP on an IP address. The customer production deployment should use a customer-controlled domain and HTTPS.

Recommended production arrangement:

```text
Android application
        |
      HTTPS
        |
Reverse proxy on ports 443/80
        |
Uvicorn on 127.0.0.1:8000
        |
PostgreSQL on 127.0.0.1:5433
```

Security rules:

- expose only SSH, HTTP, and HTTPS as required;
- do not expose PostgreSQL publicly;
- do not expose pgAdmin publicly;
- bind Uvicorn to `127.0.0.1` when using a reverse proxy;
- configure a valid TLS certificate;
- redirect HTTP to HTTPS;
- restrict SSH by key authentication and customer policy.

After HTTPS is configured, the API URLs should have the form:

```text
https://api.customer-domain.example/health
https://api.customer-domain.example/docs
```

## 13. Deployment verification

### 13.1 Health check

```bash
curl -fsS https://CUSTOMER_BACKEND_URL/health
```

For a temporary HTTP deployment:

```bash
curl -fsS http://SERVER_IP:8000/health
```

### 13.2 Swagger/OpenAPI

Open:

```text
https://CUSTOMER_BACKEND_URL/docs
```

### 13.3 Registration and authentication

Using Swagger:

1. execute `POST /api/v1/auth/register`;
2. register a new test user;
3. execute `POST /api/v1/auth/login`;
4. authorize Swagger with the returned access token;
5. execute `GET /api/v1/auth/me`.

Use non-production test credentials.

### 13.4 Database persistence

1. Register a test user.
2. Create a car through `POST /api/v1/cars`.
3. Restart the backend:

   ```bash
   sudo systemctl restart lamba-backend
   ```

4. Log in again.
5. Confirm that the test user and car still exist.

This verifies API access and PostgreSQL persistence.

### 13.5 AI assistant

After authentication and car creation, use the assistant endpoints under:

```text
/api/v1/cars/{car_id}/assistant
```

For example, execute the assistant message endpoint from Swagger and confirm that a controlled response is returned. If the provider is unavailable, inspect the backend logs and verify `AI_API_KEY`, `AI_BASE_URL`, and `AI_AGENT_ID`.

### 13.6 Receipt QR integration

The receipt service is available under:

```text
POST /api/v1/cars/{car_id}/receipts/scan
POST /api/v1/cars/{car_id}/receipts/scan/file
```

The preferred end-to-end verification is through the Android QR scanner:

1. install the APK connected to the customer backend;
2. register or log in;
3. create a car;
4. scan a valid receipt QR code;
5. confirm that receipt data is returned;
6. save the resulting record;
7. confirm that the record appears in history;
8. scan the same receipt again and confirm that a duplicate record is not created.

## 14. Configure and rebuild the Android application

The backend URL is defined in:

```text
frontend/app/build.gradle.kts
```

Current configuration:

```kotlin
buildConfigField(
    "String",
    "BACKEND_BASE_URL",
    "\"http://5.180.174.64:8000/\""
)
```

Replace it with the customer backend URL, including the trailing slash:

```kotlin
buildConfigField(
    "String",
    "BACKEND_BASE_URL",
    "\"https://api.customer-domain.example/\""
)
```

Changing the backend URL requires rebuilding the APK.

Example build:

```bash
cd /opt/lamba/LAMBA_Team-22/frontend
./gradlew clean assembleDebug
```

The resulting debug APK is normally created under:

```text
frontend/app/build/outputs/apk/debug/
```

For a production release build, the customer must configure and securely control Android signing credentials. Signing keys must never be stored in the public repository.

After rebuilding:

1. install the new APK;
2. verify registration;
3. verify car creation;
4. verify history and expenses;
5. verify the AI assistant;
6. verify QR receipt scanning;
7. verify trip mode and achievements.

## 15. Logs and diagnostics

Backend logs are stored in the systemd journal.

Recent logs:

```bash
sudo journalctl -u lamba-backend -n 200 --no-pager
```

Follow logs in real time:

```bash
sudo journalctl -u lamba-backend -f
```

Logs since the current boot:

```bash
sudo journalctl -u lamba-backend -b
```

PostgreSQL logs:

```bash
cd "$LAMBA_BACKEND"
docker compose logs --tail=200 postgres
docker compose logs -f postgres
```

Check listening ports:

```bash
sudo ss -lntp
```

Check service state:

```bash
sudo systemctl status lamba-backend
docker compose ps
```

## 16. Backup and restore

The customer hosting provider may offer complete server snapshots. Provider snapshots should not be the only database backup mechanism.

### 16.1 Create a PostgreSQL backup

```bash
cd "$LAMBA_BACKEND"
set -a
source .env
set +a

mkdir -p backups
docker compose exec -T postgres \
  pg_dump -U "$POSTGRES_USER" -d "$POSTGRES_DB" \
  > "backups/lamba_$(date +%Y%m%d_%H%M%S).sql"
```

Protect backup files because they may contain user data:

```bash
chmod 600 backups/*.sql
```

Copy backups to customer-controlled storage separate from the server.

### 16.2 Restore a PostgreSQL backup

Stop the backend before restoration:

```bash
sudo systemctl stop lamba-backend
```

Load the environment:

```bash
cd "$LAMBA_BACKEND"
set -a
source .env
set +a
```

Restore the selected backup:

```bash
cat backups/SELECTED_BACKUP.sql | \
  docker compose exec -T postgres \
  psql -U "$POSTGRES_USER" -d "$POSTGRES_DB"
```

Apply migrations and restart:

```bash
source .venv/bin/activate
alembic upgrade head
sudo systemctl start lamba-backend
```

Verify `/health`, authentication, and persisted data after restoration.

Restoring into a database containing conflicting data may fail. The customer should test the restore process in a non-production environment.

## 17. Update procedure

Before an update:

1. create a database backup;
2. record the currently deployed release or commit;
3. keep the previous source archive or release available for rollback.

Update sequence:

```bash
sudo systemctl stop lamba-backend

cd "$LAMBA_BACKEND"
source .venv/bin/activate
pip install -r requirements.txt
alembic upgrade head

sudo systemctl start lamba-backend
sudo systemctl status lamba-backend
```

Then verify:

```bash
curl -fsS http://127.0.0.1:8000/health
sudo journalctl -u lamba-backend -n 100 --no-pager
```

If the backend address changes, rebuild and redistribute the Android APK.

## 18. Common problems

### Backend service does not start

Inspect:

```bash
sudo systemctl status lamba-backend
sudo journalctl -u lamba-backend -n 200 --no-pager
```

Check:

- `WorkingDirectory`;
- `.venv/bin/python`;
- `.env` path and permissions;
- missing Python dependencies;
- invalid environment variable values;
- port 8000 conflicts.

### Database connection fails

Check:

```bash
docker compose ps
docker compose logs --tail=200 postgres
```

Confirm:

- PostgreSQL container is healthy;
- `POSTGRES_HOST=127.0.0.1`;
- `POSTGRES_PORT=5433`;
- database name, user, and password match Docker Compose;
- no incorrect `DATABASE_URL` overrides the individual values.

### Migration fails

Check:

```bash
source .venv/bin/activate
alembic current
alembic heads
alembic upgrade head
```

Create a database backup before manual repair. Do not delete migration files from a deployed environment.

### AI assistant is unavailable

Confirm:

- `AI_PROVIDER=timeweb`;
- `AI_API_KEY` is valid;
- `AI_BASE_URL` is correct;
- `AI_AGENT_ID` is correct;
- the server can reach the external provider;
- provider usage limits have not been exceeded.

Inspect the journal without exposing tokens.

### Receipt scanning is unavailable

Confirm:

- `PROVERKACHEKA_API_TOKEN` is configured;
- `PROVERKACHEKA_API_URL` is correct;
- the server has outbound internet access;
- the provider account and limits are active.

### Android cannot connect

Confirm:

- the APK contains the correct `BACKEND_BASE_URL`;
- the URL includes the scheme and trailing slash;
- the server firewall allows the required port;
- HTTPS certificate and domain are valid;
- the backend health check is reachable from the Android device;
- the APK was rebuilt after changing the URL.

## 19. Security checklist

Before production acceptance:

- [ ] Infrastructure is controlled by the customer.
- [ ] SSH uses customer-controlled keys.
- [ ] Backend runs under a dedicated non-root account.
- [ ] `.env` permissions are restricted.
- [ ] Default passwords have been replaced.
- [ ] `SECRET_KEY` is long, random, and customer-controlled.
- [ ] PostgreSQL is not publicly exposed.
- [ ] pgAdmin is not publicly exposed.
- [ ] Domain and HTTPS are configured.
- [ ] HTTP is redirected to HTTPS.
- [ ] AI and Proverkacheka credentials are customer-controlled.
- [ ] Android signing credentials are customer-controlled.
- [ ] Backups are encrypted or access-restricted.
- [ ] A restore test has been completed.
- [ ] Logs do not contain passwords or tokens.
- [ ] The final APK uses the customer backend URL.

## 20. Final customer acceptance checklist

- [ ] Complete frontend and backend source archive received.
- [ ] Archive checksum verified.
- [ ] Configuration template received.
- [ ] Customer server prepared.
- [ ] PostgreSQL started.
- [ ] Backend dependencies installed.
- [ ] Database migrations applied.
- [ ] Backend service enabled and running.
- [ ] Health check passed.
- [ ] Swagger/OpenAPI opened.
- [ ] Registration and login passed.
- [ ] Database persistence verified.
- [ ] AI assistant verified.
- [ ] Receipt QR flow verified.
- [ ] Android APK rebuilt for the customer backend.
- [ ] Customer confirmed control of all credentials.
- [ ] Backup and restore procedure reviewed.
- [ ] Known limitations reviewed.
- [ ] Customer confirmed that no access to the team's HOSTKEY server is required.

## Appendix A. Team reference systemd unit

The verified temporary team deployment uses:

```ini
[Unit]
Description=LAMBA Backend API
After=network-online.target docker.service
Wants=network-online.target
Requires=docker.service

[Service]
Type=simple
WorkingDirectory=/opt/lamba-backend/LAMBA_Team-22/backend
EnvironmentFile=/opt/lamba-backend/LAMBA_Team-22/backend/.env
ExecStart=/opt/lamba-backend/LAMBA_Team-22/backend/.venv/bin/python -m uvicorn app.main:app --host 0.0.0.0 --port 8000
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
```

This unit documents the working reference configuration. For a customer production deployment, use a dedicated non-root service account, customer-controlled paths, and a reverse proxy with HTTPS.
