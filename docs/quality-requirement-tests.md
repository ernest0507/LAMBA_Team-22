# Quality Requirement Tests

## QRT-001: Registration API response time

**Linked quality requirement:** [QR-001](quality-requirements.md#qr-001-registration-api-response-time)

**Verification method:** Automated backend integration and response-time test.

**Test data, setup, or environment:** Standard backend CI environment with PostgreSQL service, Alembic migrations applied, and the FastAPI backend running through Uvicorn. The test sends 20 concurrent registration requests with unique email addresses.

**Automated command or CI check:** `pytest tests/test_registration_response_time.py`

**Expected measurable result:** At least 95% of 20 registration requests return successful responses within 2 seconds.

**Evidence link:** [Latest protected default-branch Backend tests CI run](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml?query=branch%3Amain) showing the job result and test output.

## QRT-002: AI assistant provider failure handling

**Linked quality requirement:** [QR-002](quality-requirements.md#qr-002-ai-assistant-provider-failure-handling)

**Verification method:** Automated backend unit test with a mocked unavailable AI provider.

**Test data, setup, or environment:** Standard backend test environment. The test replaces the external AI provider with a fake client that raises an unavailable-provider error. No real AI credentials or network calls are used.

**Automated command or CI check:** `pytest tests/test_returns_clarifictation_ai_provider_unavailable.py`

**Expected measurable result:** The assistant service returns a controlled clarification response, does not create a maintenance record, and does not expose an unhandled provider error.

**Evidence link:** [Latest protected default-branch Backend tests CI run](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml?query=branch%3Amain) showing the job result and test output.

## QRT-003: AI assistant extracted record data validation

**Linked quality requirement:** [QR-003](quality-requirements.md#qr-003-ai-assistant-extracted-record-data-validation)

**Verification method:** Automated backend unit test with mocked AI-extracted maintenance record data.

**Test data, setup, or environment:** Standard backend test environment. The test replaces the external AI provider with a fake response containing invalid extracted car record data, including a negative cost value.

**Automated command or CI check:** `pytest tests/test_returns_clarification_negative_cost.py`

**Expected measurable result:** The assistant service rejects the invalid extracted record data before it can be accepted as a maintenance record and returns a controlled clarification response.

**Evidence link:** [Latest protected default-branch Backend tests CI run](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml?query=branch%3Amain) showing the job result and test output.

## QRT-004: Maintenance record persistence integrity

**Linked quality requirement:** [QR-004](quality-requirements.md#qr-004-maintenance-record-persistence-integrity)

**Verification method:** Automated backend integration test for the authenticated car and maintenance-record workflow.

**Test data, setup, or environment:** Standard backend CI environment with PostgreSQL service, Alembic migrations applied, and the FastAPI backend running through Uvicorn. The test registers a unique user, logs in, creates a car, creates a maintenance record for that car, reads the record back, and checks the car timeline.

**Automated command or CI check:** `pytest tests/test_database_persists_car_and_maintenance_record_workflow.py`

**Expected measurable result:** The created maintenance record is returned by the record detail endpoint with the expected title, category, mileage, and cost fields, and the same record appears in the car timeline.

**Evidence link:** [Latest protected default-branch Backend tests CI run](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml?query=branch%3Amain) showing the job result and test output.

## QRT-005: Registration event-loop responsiveness

**Linked quality requirement:** [QR-005](quality-requirements.md#qr-005-registration-event-loop-responsiveness)

**Verification method:** Automated backend unit tests with monkeypatched slow password hashing and password verification functions.

**Test data, setup, or environment:** Standard backend test environment. The tests replace password hashing and verification with slow fake functions, start an event-loop probe, and verify that the password work runs outside the event-loop thread.

**Automated command or CI check:** `pytest tests/test_password_hash_threadpool.py`

**Expected measurable result:** Password hashing and password verification complete on worker threads, while the event-loop probe completes during the simulated slow password operation.

**Evidence link:** [Latest protected default-branch Backend tests CI run](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml?query=branch%3Amain) showing the job result and test output.

## QRT-006: Registration database connection release

**Linked quality requirement:** [QR-006](quality-requirements.md#qr-006-registration-database-connection-release)

**Verification method:** Automated backend unit test with a fake async database session and monkeypatched user lookup and creation functions.

**Test data, setup, or environment:** Standard backend test environment. The test uses a fake session that records database events, then verifies the order of user lookup, rollback, and user creation during registration.

**Automated command or CI check:** `pytest tests/test_registration_releases_connection.py`

**Expected measurable result:** The registration route performs the existing-user lookup, releases the lookup transaction with rollback, and only then creates the new user.

**Evidence link:** [Latest protected default-branch Backend tests CI run](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml?query=branch%3Amain) showing the job result and test output.

## QRT-007: Record photo upload validation

**Linked quality requirement:** [QR-007](quality-requirements.md#qr-007-record-photo-upload-validation)

**Verification method:** Automated backend unit tests with mocked record ownership, photo counting, and photo persistence helpers.

**Test data, setup, or environment:** Standard backend test environment. The tests use in-memory upload files to verify valid image upload handling, non-image rejection, and maximum-photo-count enforcement without storing real files or requiring external services.

**Automated command or CI check:** `pytest tests/test_record_photo_uploads.py`

**Expected measurable result:** A valid image upload returns photo metadata and a retrievable API URL, a non-image upload is rejected with HTTP 415, and uploads that would exceed three total photos for one record are rejected with HTTP 400.

**Evidence link:** [Latest protected default-branch Backend tests CI run](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml?query=branch%3Amain) showing the job result and test output.


## QRT-008: Receipt QR scan backend handling

**Linked quality requirement:** [QR-008](quality-requirements.md#qr-008-receipt-qr-scan-reliability-and-access-control)

**Verification method:** Automated backend unit tests with mocked car ownership lookup, mocked receipt QR provider behavior, and provider-response normalization checks.

**Test data, setup, or environment:** Standard backend test environment. The tests use valid raw receipt QR payloads, in-memory QR image uploads, unsupported file uploads, fake owned and unowned car lookups, simulated missing provider configuration, simulated provider error codes, and representative successful Proverkacheka receipt payloads. No real receipt provider credentials or network calls are used.

**Automated command or CI check:** `pytest tests/test_receipts_api.py tests/test_proverkacheka_service.py`

**Expected measurable result:** Receipt QR scan tests pass: the backend accepts owned-car raw QR and QR image inputs, forwards valid QR data to the provider layer without modification, rejects unsupported files and unowned cars, maps missing provider configuration and provider error codes to controlled HTTP responses, and normalizes successful provider receipt payloads into the API receipt response.

**Evidence link:** [Latest protected default-branch Backend tests CI run](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml?query=branch%3Amain) showing the job result and test output.

## QRT-009: Trip distance tracking integrity

**Linked quality requirement:** [QR-009](quality-requirements.md#qr-009-trip-distance-tracking-integrity)

**Verification method:** Automated backend unit and API tests for trip metric calculation, trip endpoint behavior, ownership checks, and mileage persistence.

**Test data, setup, or environment:** Standard backend test environment. The tests use representative valid trip location points, invalid coordinate values, low-accuracy GPS points, unrealistic location jumps, out-of-order points, empty and single-point trips, active and finished trip states, owned and inaccessible cars or trips, and final mileage values. The tests verify trip distance calculation and API behavior without requiring a real mobile device GPS session.

**Automated command or CI check:** `pytest tests/test_trip_metrics.py tests/test_trip_api.py`

**Expected measurable result:** Trip tracking tests pass: distance, duration, average speed, and max speed are calculated consistently from valid submitted points; invalid coordinates, low-accuracy points, and unrealistic jumps are ignored; points are processed chronologically; empty and single-point trips return safe zero metrics; trip operations enforce ownership and active/finished state rules; finishing a trip saves calculated metrics and persists valid final mileage; and invalid final mileage below the current vehicle mileage is rejected with a controlled HTTP 400 response.

**Evidence link:** [Latest protected default-branch Backend tests CI run](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml?query=branch%3Amain) showing the job result and test output.
