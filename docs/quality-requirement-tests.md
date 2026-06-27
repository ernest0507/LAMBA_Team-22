# Quality Requirement Tests

## QRT-001: Registration API response time

**Linked quality requirement:** [QR-001](quality-requirements.md#qr-001-registration-api-response-time)

**Verification method:** Automated backend integration and response-time test.

**Test data, setup, or environment:** Standard backend CI environment with PostgreSQL service, Alembic migrations applied, and the FastAPI backend running through Uvicorn. The test sends 20 concurrent registration requests with unique email addresses.

**Automated command or CI check:** `pytest tests/test_registration_response_time.py`

**Expected measurable result:** At least 95% of 20 registration requests return successful responses within 2 seconds.

**Evidence link:** Latest protected default-branch Backend tests CI run showing the job result and test output.

## QRT-002: AI assistant provider failure handling

**Linked quality requirement:** [QR-002](quality-requirements.md#qr-002-ai-assistant-provider-failure-handling)

**Verification method:** Automated backend unit test with a mocked unavailable AI provider.

**Test data, setup, or environment:** Standard backend test environment. The test replaces the external AI provider with a fake or mocked provider that raises an unavailable-provider error. No real AI credentials or network calls are used.

**Automated command or CI check:** `pytest tests/test_returns_clarification_ai_provider_unavailable.py`

**Expected measurable result:** The assistant service returns a controlled clarification response instead of crashing or exposing an unhandled provider error.

**Evidence link:** Latest protected default-branch Backend tests CI run showing the job result and test output.

## QRT-003: AI assistant extracted record data validation

**Linked quality requirement:** [QR-003](quality-requirements.md#qr-003-ai-assistant-extracted-record-data-validation)

**Verification method:** Automated backend unit test with mocked AI-extracted maintenance record data.

**Test data, setup, or environment:** Standard backend test environment. The test replaces the external AI provider with a fake or mocked response containing invalid extracted car record data, including a negative cost value.

**Automated command or CI check:** `pytest tests/test_returns_clarification_negative_cost.py`

**Expected measurable result:** The assistant service rejects the invalid extracted record data before it can be accepted as a maintenance record and returns a controlled clarification response.

**Evidence link:** Latest protected default-branch Backend tests CI run showing the job result and test output.
