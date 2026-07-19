# Customer Handover

**Project:** LAMBA  
**Document status:** Final customer handover version  
**Last updated:** 2026-07-17

This document describes the final customer handover state of the LAMBA project. It is intended for the customer, teaching assistants, and future maintainers who need to understand what has been delivered, how the product can be accessed, how it can be deployed on customer-managed infrastructure, and which responsibilities remain with the customer after transition.

This document reflects the actual final delivery state.

## 1. Product summary

LAMBA is a mobile application for car owners. It helps users maintain a digital car profile, record car-related expenses and events, interact with an AI assistant, review statistics, and use trip/refueling-related flows.

The delivered product consists of:

- an Android mobile client;
- a FastAPI backend;
- PostgreSQL-based persistence;
- Alembic migrations;
- AI provider integration;
- maintained project documentation;
- weekly public reports;
- GitHub-based issue, review, testing, release, and documentation workflows.

## 2. Final handover state

| Item | Final state |
|---|---|
| Handover stage | Final delivery package prepared. |
| Target handover level | Ready for independent use and customer-side deployment. |
| Source-code handover | Complete backend and frontend source archive delivered through Google Drive. |
| APK access | Final APK delivered through Google Drive. |
| Backend operation | The customer is responsible for deployment and operation on customer-managed infrastructure. |
| Team server access | Access to the team's temporary server is not transferred and is not required for final operation. |
| Test account | A prepared test account is available. Credentials are delivered privately. |
| Documentation | Customer handover and deployment guidance are included in the final delivery package. |
| Customer confirmation status | To be completed during the final handover meeting. |

## 3. Delivered materials

### 3.1 Source-code archive

**Filename:**

```text
LAMBA_Team22.zip
```

**Google Drive:**

<https://drive.google.com/file/d/1g7qxBIab1TvOp1CIxZWTWt7mmh-FeFiH/view?usp=drive_link>

The archive contains the project source materials required for independent customer-side deployment and future maintenance, including the Android client and backend source code.

### 3.2 Final Android APK

**Filename:**

```text
LAMBA_Team22_v0.4.0.apk
```

**Google Drive:**

<https://drive.google.com/file/d/1u8FaDVWf6Ru-q_9zywEEDJ5L6XM3hpDv/view?usp=drive_link>

This is the final APK included in the handover package.

### 3.3 Test account

A prepared test account is available and its credentials are delivered privately.

The credentials must not be committed to the public repository, included in public reports, or copied into publicly accessible documentation.

### 3.4 Documentation

The final documentation package includes:

- this customer handover document;
- deployment instructions;
- repository documentation entry points;
- source archive information;
- private test-account access information delivered separately;
- PDF exports where required by the customer or assignment submission process.

## 4. Transition scope and ownership

### 4.1 Transferred to the customer

| Area | Final arrangement |
|---|---|
| Android application | Final APK delivered. |
| Product source code | Complete source archive delivered. |
| Backend source code | Included in the source archive. |
| Android source code | Included in the source archive. |
| Deployment knowledge | Documented for customer-side deployment. |
| Test access | Prepared test account provided privately. |
| Documentation | Final handover and deployment materials delivered. |

### 4.2 Retained by the team

| Area | Final arrangement |
|---|---|
| Team-managed temporary server | Not transferred. |
| Existing server credentials | Not transferred. |
| Private team infrastructure access | Not transferred and not required. |
| Development-history ownership | The public repository remains available as a project history and reference source. |
| Real secrets and private credentials | Not stored in public documentation or committed to the repository. |

### 4.3 Customer responsibilities after handover

The customer is responsible for:

- preparing and maintaining customer-managed infrastructure;
- configuring PostgreSQL and backend environment variables;
- configuring customer-owned credentials and API keys;
- deploying and operating the backend;
- applying database migrations;
- configuring the Android client to use the customer backend for future builds;
- maintaining backups, secrets, and service availability;
- signing and distributing future Android releases if the application is rebuilt or published.

## 5. Repository, service, deployment, account, access, and ownership arrangements

| Area | Final arrangement |
|---|---|
| Product repository | [ernest0507/LAMBA_Team-22](https://github.com/ernest0507/LAMBA_Team-22) |
| Public repository visibility | Publicly inspectable through GitHub. |
| Source-code archive | Delivered separately through Google Drive. |
| APK storage | Delivered through Google Drive. |
| Final APK version | `v0.4.0`. |
| Backend operation | Customer-side deployment and operation. |
| Customer server access | The customer deploys to its own infrastructure. |
| Team server access | Not transferred. |
| Backend secrets | Created, stored, and maintained on the customer side. |
| Test account | Available; credentials delivered privately. |

## 6. Main documentation entry points

| Purpose | Entry point |
|---|---|
| Repository entry point | [README.md](../README.md) |
| Customer handover | [docs/customer-handover.md](customer-handover.md) |
| Hosted documentation site | [LAMBA hosted documentation](https://ernest0507.github.io/LAMBA_Team-22/) |
| Roadmap | [docs/roadmap.md](roadmap.md) |
| User Acceptance Tests | [docs/user-acceptance-tests.md](user-acceptance-tests.md) |
| Testing documentation | [docs/testing.md](testing.md) |
| Quality requirements | [docs/quality-requirements.md](quality-requirements.md) |
| Quality requirement tests | [docs/quality-requirement-tests.md](quality-requirement-tests.md) |
| Development process | [docs/development-process.md](development-process.md) |
| Definition of Done | [docs/definition-of-done.md](definition-of-done.md) |
| User stories | [docs/user-stories.md](user-stories.md) |
| Backend setup | [backend/README.md](../backend/README.md) |
| Backend environment template | [backend/.env.example](../backend/.env.example) |
| Changelog | [CHANGELOG.md](../CHANGELOG.md) |

## 7. Product access and APK installation

### 7.1 Current APK backend configuration

The delivered APK contains the following backend base URL:

```text
http://5.180.174.64:8000/
```

Cleartext HTTP traffic is enabled in this build.

The address belongs to the team's temporary deployment and should not be treated as permanent customer infrastructure. For long-term customer-side operation, the Android application should be rebuilt with the customer's backend base URL.

### 7.2 APK installation

1. Open the Google Drive link for `LAMBA_Team22_v0.4.0.apk`.
2. Download the APK.
3. Open the APK on an Android device or emulator.
4. If Android blocks installation from unknown sources, allow installation for the browser or file manager used to open the APK.
5. Install the application.
6. Launch LAMBA.
7. Sign in with the privately delivered test account or create a new account.
8. Add or select a car and verify the available product flows.

### 7.3 Test account handling

The prepared test account may be used for product verification and demonstration.

Its credentials:

- are delivered privately;
- must not be committed to GitHub;
- must not be included in public documentation;
- should be changed, reset, or removed by the customer when no longer needed.

## 8. Backend deployment and operation

The backend is designed to be deployed and maintained by the customer on customer-managed infrastructure.

### 8.1 Reference deployment environment

The team's reference deployment uses:

- Ubuntu 24.04;
- FastAPI;
- Uvicorn;
- systemd for backend service management;
- PostgreSQL 17.10;
- Alembic migrations;
- Docker Compose for PostgreSQL and pgAdmin only;
- pgAdmin for database administration.

The backend application itself is not containerized in the reference deployment.

### 8.2 Local backend setup command reference

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

API documentation:

```text
http://127.0.0.1:8000/docs
```

pgAdmin:

```text
http://127.0.0.1:5050
```

### 8.3 Customer-side production deployment flow

A typical customer-side deployment should include:

1. Prepare an Ubuntu server or equivalent supported environment.
2. Install Python and project dependencies.
3. Configure PostgreSQL.
4. Create a private backend `.env` file using `backend/.env.example`.
5. Configure customer-owned secrets and external-service credentials.
6. Apply Alembic migrations.
7. Start the FastAPI application with Uvicorn.
8. Configure a systemd service or another suitable process manager.
9. Verify the `/health` endpoint.
10. Verify API documentation and authentication flows.
11. Rebuild the Android client with the customer backend URL.
12. Perform the smoke test described in this document.

## 9. Environment variables, configuration, external services, and secrets

Do not commit real secrets, tokens, API keys, passwords, or private access instructions to the repository.

The backend environment template is:

```text
backend/.env.example
```

| Variable | Purpose / note |
|---|---|
| `APP_NAME` | Backend application name. |
| `API_PREFIX` | API route prefix. |
| `DEBUG` | Debug mode flag. Disable for production. |
| `POSTGRES_DB` | PostgreSQL database name. |
| `POSTGRES_USER` | PostgreSQL username. |
| `POSTGRES_PASSWORD` | PostgreSQL password. Must be changed for real deployment. |
| `POSTGRES_HOST` | PostgreSQL host. |
| `POSTGRES_PORT` | PostgreSQL port. |
| `PGADMIN_DEFAULT_EMAIL` | pgAdmin login email. Must be changed for real deployment. |
| `PGADMIN_DEFAULT_PASSWORD` | pgAdmin login password. Must be changed for real deployment. |
| `PGADMIN_PORT` | pgAdmin port. |
| `SECRET_KEY` | Backend authentication secret. Must be generated and stored securely. |
| `ACCESS_TOKEN_EXPIRE_MINUTES` | Access token lifetime. |
| `AI_PROVIDER` | AI provider name. |
| `AI_API_KEY` | AI provider API key. Must be stored privately. |
| `AI_BASE_URL` | AI provider base URL. |
| `AI_AGENT_ID` | AI agent identifier. |
| `AI_MODEL` | AI model configuration. |
| `AI_REQUEST_TIMEOUT_SECONDS` | AI request timeout. |

Secrets-handling rules:

- use real values only in private environment configuration;
- do not commit real `.env` files;
- do not put real passwords or API keys into public documentation;
- share credentials only through private customer/team channels;
- replace all placeholder credentials before production deployment;
- use customer-owned API credentials for long-term operation.

## 10. Recovery, verification, and smoke testing

### 10.1 Backend recovery checks

If backend-dependent functionality fails:

1. Check whether the backend service is running.
2. Check the health endpoint.
3. Check whether PostgreSQL is running.
4. Check whether the configured database values match the environment.
5. Apply pending migrations:

```powershell
cd backend
alembic upgrade head
```

6. Check the API documentation endpoint.
7. Check logs from Uvicorn or systemd.
8. Check external AI and receipt-processing credentials if the corresponding integrations fail.

### 10.2 Application smoke test

After installation or deployment:

1. Open the Android application.
2. Register or sign in.
3. Add or select a car.
4. Create or inspect a car-related record.
5. Check the history or timeline.
6. Check statistics.
7. Interact with the AI assistant when the backend and AI configuration are available.
8. Test trip mode.
9. Test the QR or refueling flow.
10. Test achievements.
11. Verify sign-out.
12. Record any remaining deployment-specific issues.

## 11. Week 6 feedback incorporated into the final handover

The Week 6 transition-readiness discussion established the following final handover requirements:

| Area | Final handover response |
|---|---|
| Final handover level | The package is prepared for independent customer-side deployment and use. |
| Source code | Complete project archive delivered through Google Drive. |
| APK access | Final APK delivered through Google Drive. |
| Test account | Prepared account available; credentials delivered privately. |
| Backend deployment | Customer-side operation documented. |
| Team server access | Not required and not transferred. |
| Documentation | Final handover and deployment information included. |
| PDF export | Documentation may be exported and stored as PDF for submission or customer use. |

Product feedback and implementation work from earlier reviews remain documented in the weekly reports, issue history, test documentation, and repository history.

## 12. Support expectations

The final delivery is prepared for independent customer-side use. Any post-handover assistance should be agreed separately with the customer.

The final meeting should clarify:

- whether short-term deployment support is required;
- the communication channel for such support;
- the duration of the support period;
- whether the temporary backend should remain available for a limited transition period;
- whether the prepared test account should be preserved, reset, or deleted.

No ongoing support period is assumed unless explicitly agreed.

## 13. Final transition confirmation checklist

The following questions should be answered during the final handover meeting.

### 13.1 Delivery and access

- [ ] Does the customer have access to the final APK?
- [ ] Does the customer have access to the complete source-code archive?
- [ ] Has the prepared test-account information been received privately?
- [ ] Are the delivered files accessible without additional team permissions?

### 13.2 Deployment and ownership

- [ ] Is it clear that the customer will deploy and operate the backend on customer-managed infrastructure?
- [ ] Is it clear that access to the team's temporary server is not part of the handover?
- [ ] Is the deployment documentation sufficient for an independent first deployment?
- [ ] Does the customer understand which environment variables and secrets must be created on the customer side?
- [ ] Does the customer understand that the Android application must be rebuilt with the customer backend URL for long-term operation?

## 14. Handover level and customer confirmation

### Handover level

Select one:

- [ ] Ready for independent use
- [ ] Independently used by customer
- [ ] Deployed or operated on customer side

### Customer confirmation

Select one:

- [ ] Accepted
- [ ] Accepted with follow-up items
- [ ] Not yet accepted

### Follow-up items, if any

| Item | Owner | Target date | Status |
|---|---|---|---|
|  |  |  |  |

### Confirmation notes

```text
To be completed during or immediately after the final customer handover meeting.
```

## 15. Update policy

After the final meeting, update this document only to record:

- selected handover level;
- customer confirmation status;
- agreed follow-up items;
- agreed temporary-backend availability period, if any;
- agreed post-handover support period, if any;
- any corrected delivery links.
