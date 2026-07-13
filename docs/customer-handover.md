# Customer Handover

**Project:** LAMBA  
**Document status:** Week 6 transition-readiness version  
**Last updated:** after publication of the Week 6 trial release and APK

This document describes the current customer handover state of the LAMBA project. It is intended for the customer, teaching assistants, and future maintainers who need to understand what is currently available, what still remains with the team, how the product can be accessed, and what must be completed before the final transition.

This document describes the actual current state. It does not claim final product transfer before it is completed.

## 1. Product summary

LAMBA is a mobile application for car owners. It helps users maintain a digital car profile, record car-related expenses and events, interact with an AI assistant, review statistics, and use trip/refueling-related flows.

The product currently consists of:

- an Android mobile client;
- a FastAPI backend;
- PostgreSQL-based persistence;
- Alembic migrations;
- AI provider integration;
- maintained documentation;
- weekly public reports;
- GitHub-based issue, review, testing, release, and documentation workflows.

## 2. Current handover state

| Item | Current state |
|---|---|
| Week 6 handover level | Trial release and transition-readiness review stage. |
| Final target handover level | Deployed or operated on customer side. |
| Full product transfer | Planned for Week 7 after Sprint 5. |
| Customer confirmation status | Accepted as a direction, with follow-up items before final transition. |
| Week 6 release | Published as [`v0.3.1 - Week 6 Trial Release`](https://github.com/ernest0507/LAMBA_Team-22/releases/tag/v0.3.1). |
| Final MVP v3 release | Planned for Week 7 after Sprint 5. |
| APK access | Available through [Google Drive](https://drive.google.com/file/d/1GJE0OONoq9pnoVfVhQG0RGEbBOTZllVh/view?usp=drive_link) and linked from the GitHub Release. |
| Source-code handover | Customer expects a complete source-code archive in Google Drive. Because development is still in progress, archive preparation is deferred to Week 7. |
| Backend operation | Final backend operation is expected on the customer side. |
| Test accounts | Customer requested prepared test accounts with example data. Because development is still in progress, preparation is deferred to Week 7. Credentials must be shared privately if accounts are created. |
| Documentation | Detailed deployment and launch documentation is a must-have. Hosted documentation should also be exportable as PDF. |

Week 6 is not the final transfer. The product is still being discussed and extended with customer feedback. The goal of Week 6 is to make transition blockers visible before Week 7.

Because active development is still continuing, the complete source-code archive and prepared test accounts are intentionally left for Week 7 after Sprint 5 scope is finalized.

## 3. Concrete transition scope

### 3.1 Transferred or made available during Week 6

| Area | Week 6 state |
|---|---|
| Public repository access | The repository is publicly inspectable through GitHub. |
| Trial application access | The Week 6 APK is available through [Google Drive](https://drive.google.com/file/d/1GJE0OONoq9pnoVfVhQG0RGEbBOTZllVh/view?usp=drive_link) and linked from the [`v0.3.1` GitHub Release](https://github.com/ernest0507/LAMBA_Team-22/releases/tag/v0.3.1). |
| Customer-facing documentation | Maintained documentation is available in the repository and hosted documentation site. |
| Handover guidance | This document describes current handover status, access, setup, deployment, verification, limitations, and support needs. |
| Customer feedback | Week 6 feedback was collected and should be converted into Sprint 5 follow-up work. |

### 3.2 Intentionally retained by the team during Week 6

| Area | Reason |
|---|---|
| Final product transfer | The product is still in active development and final transition is planned for Week 7. |
| Final MVP v3 release | MVP v3 is planned after Sprint 5. |
| Final source-code archive | The customer requested a complete archive; it should be prepared for the final handover. |
| Final customer-side backend operation | Backend deployment on the customer side is expected for final operation but is not completed in Week 6. |
| Private credentials and secrets | Secrets must not be committed publicly and must be shared privately only when needed. |
| Private evidence | Recordings, exact private timecodes, private access instructions, and credentials belong only in private submission or direct private communication. |

### 3.3 Expected Week 7 transfer or confirmation

The following items should be completed or confirmed during Week 7:

- final MVP v3 release;
- updated APK access link;
- complete source-code archive in Google Drive after Sprint 5 scope is finalized;
- backend deployment instructions sufficient for customer-side operation;
- prepared test accounts with example data, if still needed after Sprint 5 scope is finalized;
- exported hosted documentation as PDF, if required;
- final handover status;
- final customer confirmation status.

## 4. Repository, service, deployment, account, access, and ownership arrangements

| Area | Current arrangement |
|---|---|
| Product repository | [ernest0507/LAMBA_Team-22](https://github.com/ernest0507/LAMBA_Team-22) |
| Repository ownership | Retained by the student team during Week 6. |
| Public repository visibility | Publicly inspectable through GitHub. |
| Source-code handover | Customer expects a full project archive in Google Drive, including backend and frontend. This is deferred to Week 7 because active development is still continuing. |
| APK storage | Google Drive, linked from the relevant GitHub Release. |
| Week 6 release | Published as [`v0.3.1 - Week 6 Trial Release`](https://github.com/ernest0507/LAMBA_Team-22/releases/tag/v0.3.1). |
| Backend service | Current trial operation may remain team-side, but final operation is expected customer-side. |
| Customer server access | Customer does not need access to the team's current server. |
| Backend secrets | For customer-side operation, secrets should be stored on the customer side. |
| Test accounts | Requested by the customer, with example data. Preparation is deferred to Week 7 because active development is still continuing. Credentials must be shared privately, not committed publicly. |

## 5. Main documentation entry points

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

The hosted documentation should also be exportable as PDF for customer-side storage and future use.

## 6. Product access

### 6.1 Week 6 trial release

The Week 6 trial / handover-candidate release has been published:

- **Release:** [`v0.3.1 - Week 6 Trial Release`](https://github.com/ernest0507/LAMBA_Team-22/releases/tag/v0.3.1)
- **APK:** [Google Drive product access artifact](https://drive.google.com/file/d/1GJE0OONoq9pnoVfVhQG0RGEbBOTZllVh/view?usp=drive_link)
- **Release date:** 12.07.2026
- **Milestone:** [Sprint 4 - MVP v3](https://github.com/ernest0507/LAMBA_Team-22/milestone/4)

This is the Week 6 trial release. It is not the final Week 7 customer-side transition.

### 6.2 APK installation

1. Open the [Google Drive APK link](https://drive.google.com/file/d/1GJE0OONoq9pnoVfVhQG0RGEbBOTZllVh/view?usp=drive_link).
2. Download the APK.
3. Open the APK on an Android device or Android emulator.
4. If Android blocks installation from unknown sources, allow installation for the browser or file manager used to open the APK.
5. Install and launch LAMBA.
6. Register a new account or sign in.
7. Add or select a car and test the available product flows.

No fixed test credentials are required for the Week 6 trial build.

### 6.3 Test accounts

The customer requested prepared test accounts with example data so that future maintainers or another team can understand the product faster.

Current state:

```text
Prepared test accounts are requested, but their preparation is deferred to Week 7 because active development is still continuing.
```

If test accounts are created after Sprint 5 scope is finalized, their credentials must be shared only through private channels. They must not be committed to the public repository or included in public reports.

## 7. Backend deployment and operation

The backend is expected to be deployable and maintainable by the customer for final independent operation.

The customer does not need access to the team's current server. Instead, the final handover should provide enough documentation and project materials for customer-side deployment.

### 7.1 Backend stack

The backend uses:

- FastAPI;
- PostgreSQL;
- pgAdmin for local database inspection;
- Alembic for migrations;
- Uvicorn for local API execution;
- external AI provider configuration.

### 7.2 Current backend setup command reference

The backend README currently documents the local setup as:

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

### 7.3 Customer-side deployment requirement

For final transition, the backend documentation should explain:

- how to prepare the server environment;
- how to configure environment variables;
- how to start PostgreSQL and pgAdmin if needed;
- how to apply Alembic migrations;
- how to start the FastAPI backend;
- how to verify the health endpoint;
- how to connect the Android application to the backend base URL;
- how secrets should be stored securely on the customer side.

## 8. Environment variables, configuration, external services, and secrets

Do not commit real secrets, tokens, API keys, passwords, or private access instructions to the repository.

The backend environment template is:

```text
backend/.env.example
```

The customer or maintainer should understand the following configuration values:

| Variable | Purpose / note |
|---|---|
| `APP_NAME` | Backend application name. |
| `API_PREFIX` | API route prefix. |
| `DEBUG` | Debug mode flag. |
| `POSTGRES_DB` | PostgreSQL database name. |
| `POSTGRES_USER` | PostgreSQL username. |
| `POSTGRES_PASSWORD` | PostgreSQL password. Must be changed for real deployment. |
| `POSTGRES_HOST` | PostgreSQL host. |
| `POSTGRES_PORT` | PostgreSQL port. |
| `PGADMIN_DEFAULT_EMAIL` | pgAdmin login email. Must be changed for real deployment. |
| `PGADMIN_DEFAULT_PASSWORD` | pgAdmin login password. Must be changed for real deployment. |
| `PGADMIN_PORT` | pgAdmin port. |
| `SECRET_KEY` | Backend secret key for authentication. Must be changed and stored securely. |
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
- rotate placeholder values such as `change_me` before real deployment.

## 9. Recovery and verification steps

### 9.1 Backend recovery checks

If backend-dependent functionality fails:

1. Check whether the backend process is running.
2. Check the health endpoint:

```text
GET http://127.0.0.1:8000/health
```

3. Check whether PostgreSQL is running.
4. Check whether the configured database values match the environment.
5. Check whether migrations were applied:

```powershell
cd backend
alembic upgrade head
```

6. Check API docs:

```text
http://127.0.0.1:8000/docs
```

7. Check AI provider credentials if AI assistant functionality fails.

### 9.2 Application smoke test

After installation or deployment, verify the product with a small smoke test:

1. Open the Android application.
2. Register or sign in.
3. Add or select a car.
4. Create or inspect a car-related record.
5. Check history/timeline.
6. Check statistics if available in the current release.
7. Interact with the AI assistant if backend and AI configuration are available.
8. Test trip mode if included in the release.
9. Test QR/refueling flow if included in the release.
10. Test achievements if included in the release.
11. Verify sign-out/logout if included in the release.
12. Record blockers as Sprint 5 issues or final handover follow-up items.

## 10. Week 6 customer feedback and follow-up items

The Week 6 customer trial and transition-readiness discussion produced these follow-up items:

| Area | Customer feedback / finding | Follow-up |
|---|---|---|
| Final handover level | Customer expects the final product to be deployed or operated on the customer side. | Prepare Week 7 final transition around customer-side operation. |
| Source code | Customer wants the full project source code, not only application access. | Defer archive preparation to Week 7 because active development is still continuing. |
| APK access | Cloud storage is acceptable for the APK. | Link Google Drive APK from the release. |
| Test accounts | Customer requested prepared accounts with example data. | Defer preparation to Week 7 because active development is still continuing. Share credentials privately only if accounts are created. |
| Backend deployment | Backend should ultimately run on the customer side. | Improve backend deployment documentation. |
| Documentation | Detailed documentation is a must-have. | Update README/backend README/handover docs and export hosted docs as PDF if practical. |
| APK installation | Package identity conflict occurred during trial. | Fix package/application identity before final delivery. |
| App identity | Similar names/icons caused confusion. | Add clear app name and icon. |
| QR/refueling | QR receipt was recorded as an expense, but should be a refueling record. | Update QR flow. |
| QR navigation | QR entry point in sidebar was not obvious. | Improve discoverability. |
| Achievements | Achievements are still being completed. | Finish and polish achievements. |
| Visual assets | Rotated wheel image was unclear. | Adjust or replace image. |
| Car images | Car images appeared too small. | Improve image cropping/scaling. |
| Duplicate receipts | Same receipt could be added again. | Store receipt identifiers to prevent duplicates. |

## 11. Known limitations

Current known limitations:

- Week 6 is not the final transfer.
- Final MVP v3 is planned for Week 7.
- The Week 6 trial release and APK are available through [`v0.3.1`](https://github.com/ernest0507/LAMBA_Team-22/releases/tag/v0.3.1) and [Google Drive](https://drive.google.com/file/d/1GJE0OONoq9pnoVfVhQG0RGEbBOTZllVh/view?usp=drive_link).
- Full source-code archive is expected but intentionally deferred to Week 7 because active development is still continuing.
- Customer-side backend deployment is expected but not completed in Week 6.
- Test accounts with example data are requested but intentionally deferred to Week 7 because active development is still continuing.
- Hosted documentation PDF export is requested but not yet linked here.
- QR/refueling behavior needs correction so receipt data becomes a refueling record rather than only a generic expense.
- Application package/name/icon identity should be improved before final delivery.
- Additional visual polish and Sprint 5 follow-up work remain.

## 12. Documentation sufficiency and remaining support

Current documentation is sufficient for Week 6 transition-readiness review, but not yet sufficient for final customer-side operation without Week 7 follow-up.

The customer specifically requested more detailed documentation for deployment and launch. Therefore, before final handover, the team should ensure that the documentation covers:

- backend deployment on the customer side;
- environment variables and secrets handling;
- Android launch/access instructions;
- product verification steps;
- troubleshooting;
- source-code archive access;
- hosted documentation export.

Support still required before final transition:

- decide whether prepared test accounts are still needed after Sprint 5 and create them privately if needed;
- prepare the full source-code archive after Sprint 5 scope is finalized;
- keep the APK access link current for the final Week 7 release;
- improve backend deployment instructions;
- fix or document known product limitations;
- confirm final transition status with the customer in Week 7.

## 13. Update policy

Keep this document current during Week 6 and Week 7.

Update this document when any of the following changes:

- release link;
- APK Google Drive link;
- source-code archive link;
- backend deployment instructions;
- environment variable list;
- AI provider configuration;
- test account availability;
- customer-side operation status;
- handover level;
- customer confirmation status;
- known limitations;
- troubleshooting steps;
- Sprint 5 follow-up items;
- final transition status.
