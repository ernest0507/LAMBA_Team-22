# Testing

## Critical Modules and Coverage

| Critical module | Why critical | Required line coverage | Current eфvidence |
|---|---|---:|---|
| `backend/app/services/assistant.py` | Handles AI-assisted record extraction, AI provider fallback behavior, extracted-record validation, and mileage-update interpretation. | 30% | Covered by backend tests and reported by `pytest tests --cov=app --cov-report=term-missing` in CI. |
| `backend/app/schemas/assistant.py` | Defines assistant request, response, action, mileage-update, and extracted-record structures used by the AI chat flow. | 30% | Covered by backend tests and reported by `pytest tests --cov=app --cov-report=term-missing` in CI. |
| `backend/app/core/database.py` | Provides backend database session and engine setup used by persistence-related backend flows. | 30% | Covered by backend tests and reported by `pytest tests --cov=app --cov-report=term-missing` in CI. |
| `backend/app/api/routes/auth.py` | Owns registration and login routes, including the registration transaction boundary verified by QRT-006. | 30% | Covered by backend tests and reported by `pytest tests --cov=app --cov-report=term-missing` in CI. |
| `backend/app/crud/users.py` | Owns user lookup, creation, authentication, and password hashing/verification helpers verified by QRT-005. | 30% | Covered by backend tests and reported by `pytest tests --cov=app --cov-report=term-missing` in CI. |
| `backend/app/api/routes/maintenance_records.py` | Owns maintenance record routes and record-photo upload validation verified by QRT-007. | 30% | Covered by backend tests and reported by `pytest tests --cov=app --cov-report=term-missing` in CI. |
| `backend/app/api/routes/receipts.py` | Owns receipt QR scan routes, car ownership checks, QR file validation, and provider error mapping verified by QRT-008. | 30% | 96% line coverage in the latest protected default-branch backend CI run. |
| `backend/app/api/routes/trips.py` | Owns trip start, point append, finish, read, ownership checks, active/finished state handling, and final-mileage validation verified by QRT-009. | 30% | 96% line coverage in the latest protected default-branch backend CI run. |
| `backend/app/services/trip_metrics.py` | Calculates trip distance, duration, average speed, max speed, GPS-point filtering, chronological processing, and safe default metrics verified by QRT-009. | 30% | 92% line coverage in the latest protected default-branch backend CI run. |

The backend CI job runs `pytest tests --cov=app --cov-report=term-missing`.
The coverage output from the latest protected default-branch run is the source
of truth for current line coverage. Local runs without PostgreSQL, Alembic
migrations, and a running backend server may fail integration tests that expect
the same environment created by CI.

## Automated Test Status

| Test type | Scope | Command or CI check | Latest result | Evidence |
|---|---|---|---|---|
| Backend unit tests | AI provider fallback, invalid AI-extracted record data, mileage-update handling, password hashing/verification threadpool behavior, registration transaction release, record photo upload validation, receipt QR scan backend handling, and trip metric calculation/API behavior. | `pytest tests/test_returns_clarifictation_ai_provider_unavailable.py tests/test_returns_clarification_negative_cost.py tests/test_assistant_mileage_updates.py tests/test_password_hash_threadpool.py tests/test_registration_releases_connection.py tests/test_record_photo_uploads.py tests/test_receipts_api.py tests/test_trip_metrics.py tests/test_trip_api.py` | Passing when the `backend-tests` CI job passes. | [Backend tests workflow](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml) |
| Backend integration tests | Registration response time and persisted car plus maintenance-record workflow with PostgreSQL, Alembic migrations, and a running FastAPI backend. | `pytest tests/test_registration_response_time.py tests/test_database_persists_car_and_maintenance_record_workflow.py` | Passing when the `backend-tests` CI job passes. | [Backend tests workflow](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml) |
| Automated QRTs | QR-001 through QR-009. | `pytest tests --cov=app --cov-report=term-missing` | Passing when the `backend-tests` CI job passes. | [Quality requirement tests](quality-requirement-tests.md) |
| Coverage reporting | Backend application modules under `backend/app`. | `pytest tests --cov=app --cov-report=term-missing` | Reported in the `backend-tests` CI output. | [Backend tests workflow](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml) |


## CI and QA Check Status

| Gate or check | Required for Done? | Latest protected-branch status | Evidence |
|---|---|---|---|
| Backend linting | Yes | Passing when `backend-lint` passes. | `ruff check app tests` in [Backend tests workflow](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml) |
| Backend dependency installation | Yes | Passing when `backend-tests` passes. | [Backend tests workflow](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml) |
| Database migrations | Yes | Passing when `backend-tests` passes. | `alembic upgrade head` in [Backend tests workflow](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml) |
| Backend startup check | Yes | Passing when `backend-tests` passes. | `curl http://127.0.0.1:8000/docs` in CI |
| Backend unit and integration tests | Yes | Passing when `backend-tests` passes. | `pytest tests --cov=app --cov-report=term-missing` |
| Automated QRTs | Yes | Passing when `backend-tests` passes. | [Quality requirement tests](quality-requirement-tests.md) |
| Coverage report | Yes | Reported when `backend-tests` passes. | `pytest-cov` output in CI |
| Additional QA check | Yes | Passing when `additional-test-backend-dependency-audit` passes. | `pip-audit -r requirements.txt` in CI |
| Lychee link checking | Yes for Markdown/link changes | Passing when `Links` workflow passes. | [Links workflow](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/lychee.yml) |

## Additional QA Check Rationale

| QA objective or risk | Additional QA check | Scope | Latest result | Evidence | Limitations or follow-up |
|---|---|---|---|---|---|
| Backend dependencies with known vulnerabilities may expose user accounts, authentication, vehicle data, uploaded record photos, or deployment environments to avoidable security risk. | Automated dependency vulnerability scan with `pip-audit`. | `backend/requirements.txt` and backend Python dependencies. | Passing when `additional-test-backend-dependency-audit` passes. | [Backend tests workflow](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml) | `pip-audit` checks dependency vulnerability databases. It does not replace code review, secret scanning, runtime security testing, upload security review, or manual triage of vulnerabilities that require upstream fixes. |

Link checking is maintained separately through Lychee and does not count as the
Assignment 4 additional QA check.

## Automated Quality Requirement Tests

| QRT | Linked QR | Automated test or CI command | Expected result | Evidence |
|---|---|---|---|---|
| QRT-001 | QR-001 | `pytest tests/test_registration_response_time.py` | At least 95% of 20 registration requests return successful responses within 2 seconds. | [QRT-001](quality-requirement-tests.md#qrt-001-registration-api-response-time) |
| QRT-002 | QR-002 | `pytest tests/test_returns_clarifictation_ai_provider_unavailable.py` | Assistant returns a controlled clarification response when the AI provider is unavailable. | [QRT-002](quality-requirement-tests.md#qrt-002-ai-assistant-provider-failure-handling) |
| QRT-003 | QR-003 | `pytest tests/test_returns_clarification_negative_cost.py` | Assistant rejects invalid extracted record data with a negative cost value. | [QRT-003](quality-requirement-tests.md#qrt-003-ai-assistant-extracted-record-data-validation) |
| QRT-004 | QR-004 | `pytest tests/test_database_persists_car_and_maintenance_record_workflow.py` | A created maintenance record is persisted, can be read back, and appears in the car timeline. | [QRT-004](quality-requirement-tests.md#qrt-004-maintenance-record-persistence-integrity) |
| QRT-005 | QR-005 | `pytest tests/test_password_hash_threadpool.py` | Password hashing and verification run outside the event-loop thread. | [QRT-005](quality-requirement-tests.md#qrt-005-registration-event-loop-responsiveness) |
| QRT-006 | QR-006 | `pytest tests/test_registration_releases_connection.py` | Registration performs the existing-user lookup, releases the lookup transaction with rollback, and only then starts user creation. | [QRT-006](quality-requirement-tests.md#qrt-006-registration-database-connection-release) |
| QRT-007 | QR-007 | `pytest tests/test_record_photo_uploads.py` | Valid image upload returns photo metadata with an API URL; invalid upload cases return controlled HTTP 415 or HTTP 400 errors. | [QRT-007](quality-requirement-tests.md#qrt-007-record-photo-upload-validation) |
| QRT-008 | QR-008 | `pytest tests/test_receipts_api.py` | All receipt QR scan route tests pass: owned-car raw QR scans return the provider response, owned-car QR image uploads are forwarded to the provider with a safe filename and content type, unsupported file types return HTTP 415, inaccessible vehicle IDs return HTTP 404 without calling the provider, missing provider configuration returns HTTP 503, and provider error codes are mapped to controlled HTTP 400, 202, 429, or 502 responses. | [QRT-008](quality-requirement-tests.md#qrt-008-receipt-qr-scan-backend-handling) |
| QRT-009 | QR-009 | `pytest tests/test_trip_metrics.py tests/test_trip_api.py` | Trip tracking tests pass: distance, duration, average speed, and max speed are calculated consistently from valid submitted points; invalid coordinates, low-accuracy points, and unrealistic jumps are ignored; points are processed chronologically; empty and single-point trips return safe zero metrics; trip operations enforce ownership and active/finished state rules; finishing a trip saves calculated metrics and persists valid final mileage; and invalid final mileage below the current vehicle mileage is rejected with a controlled HTTP 400 response. | [QRT-009](quality-requirement-tests.md#qrt-009-trip-distance-tracking-integrity) |

## Manual Evidence That Does Not Count as QRT

| Evidence | Scope | Result | Follow-up PBI or issue |
|---|---|---|---|
| Customer UAT scenarios | Registration, sign-in, vehicle digital twin creation, AI-chat expense entry, mileage update, maintenance-record history, record photos, and statistics. | Results are recorded in `docs/user-acceptance-tests.md` after customer execution. | Follow-up issues are created when UAT reveals defects, product gaps, or changed expectations. |
| Sprint Review discussion | Delivered increment, customer feedback, UAT results, and quality evidence. | Summarized in the Week 5 report and customer review materials. | Follow-up issues or roadmap updates are created as needed. |

Manual evidence supports release and customer validation, but it does not count
as an automated QRT.

## Maintained Quality Gates

The following maintained quality gates remain active for Assignment 6, Week 7,
and MVP v3 work:

- Backend linting must run in CI for pull requests and protected default-branch
  updates.
- Backend tests must run in CI for pull requests and protected default-branch
  updates.
- Automated QRTs must remain linked to current quality requirements.
- Coverage reporting must remain active for tested backend code.
- Critical backend modules must keep at least 30% automated line coverage unless
  a TA-approved exception is documented.
- The additional QA check must remain active or be replaced with an equivalent
  or stronger documented check.
- Lychee remains active for Markdown link checking, but it does not replace the
  additional QA check.
