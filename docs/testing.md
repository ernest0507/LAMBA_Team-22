# Testing

## Critical Modules and Coverage

The team selected the Assignment 4 critical modules from the backend areas that
are directly covered by the automated quality requirement tests and current
coverage evidence. These modules support the AI-assisted maintenance record
workflow, where provider failures or invalid extracted data would materially
affect the product.

| Critical module | Why critical | Required line coverage | Current line coverage | Evidence |
|---|---|---:|---:|---|
| `backend/app/services/assistant.py` | Handles AI-assisted record extraction, AI provider fallback behavior, and validation of extracted record data before it is accepted by the product. | 30% | 75% | [Successful backend coverage job](https://github.com/ernest0507/LAMBA_Team-22/actions/runs/28321982450/job/83905371955) |
| `backend/app/schemas/assistant.py` | Defines assistant request, response, action, and extracted-record structures used by the AI chat flow. | 30% | 85% | [Successful backend coverage job](https://github.com/ernest0507/LAMBA_Team-22/actions/runs/28321982450/job/83905371955) |
| `backend/app/core/database.py` | Provides backend database session and engine setup used by persistence-related backend flows. | 30% | 69% | [Successful backend coverage job](https://github.com/ernest0507/LAMBA_Team-22/actions/runs/28321982450/job/83905371955) |

The successful backend coverage job uses `pytest tests --cov=app
--cov-report=term-missing` and reports total backend application coverage of
33%. The registration response-time test is intended to run in CI with
PostgreSQL, Alembic migrations, and the backend server started by the workflow.
Local runs without that CI-like backend environment may fail the response-time
test even though the coverage report is still produced.

## Automated Test Status

| Test type | Scope | Command or CI check | Latest result | Evidence |
|---|---|---|---|---|
| Unit tests | AI assistant provider-failure handling and invalid extracted record validation. | `pytest tests/test_returns_clarifictation_ai_provider_unavailable.py tests/test_returns_clarification_negative_cost.py` | Passing when the `backend-tests` CI job passes. | [Backend tests workflow](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml) |
| Integration tests | Registration API with PostgreSQL service, Alembic migrations, and running FastAPI backend. | `pytest tests/test_registration_response_time.py` | Passing when the `backend-tests` CI job passes. | [Registration response-time test](https://github.com/ernest0507/LAMBA_Team-22/blob/main/backend/tests/test_registration_response_time.py) |
| Automated QRTs | QR-001, QR-002, QR-003. | `pytest tests --cov=app --cov-report=term-missing` | Passing when the `backend-tests` CI job passes. | [Quality requirement tests](https://github.com/ernest0507/LAMBA_Team-22/blob/main/docs/quality-requirement-tests.md) |
| Coverage reporting | Backend application modules under `backend/app`. | `pytest tests --cov=app --cov-report=term-missing` | Reported in the `backend-tests` CI output. | [Backend tests workflow](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml) |

## CI and QA Check Status

| Gate or check | Required for Done? | Latest protected-branch status | Evidence |
|---|---|---|---|
| Backend linting | Yes | Passing when `backend-lint` passes. | `ruff check app tests` in [Backend tests workflow](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml) |
| Backend dependency installation | Yes | Passing when `backend-tests` passes. | [Backend tests workflow](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml) |
| Database migrations | Yes | Passing when `backend-tests` passes. | `alembic upgrade head` in [Backend tests workflow](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml) |
| Backend startup check | Yes | Passing when `backend-tests` passes. | `curl http://127.0.0.1:8000/docs` in CI |
| Unit and integration tests | Yes | Passing when `backend-tests` passes. | `pytest tests --cov=app --cov-report=term-missing` |
| Automated QRTs | Yes | Passing when `backend-tests` passes. | [Quality requirement tests](https://github.com/ernest0507/LAMBA_Team-22/blob/main/docs/quality-requirement-tests.md) |
| Coverage report | Yes | Reported when `backend-tests` passes. | `pytest-cov` output in CI |
| Additional QA check | Yes | Passing when `additional-test-backend-dependency-audit` passes. | `pip-audit -r requirements.txt` in CI |
| Lychee link checking | Yes for Markdown/link changes | Passing when `Links` workflow passes. | [Links workflow](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/lychee.yml) |

## Additional QA Check Rationale

| QA objective or risk | Additional QA check | Scope | Latest result | Evidence | Limitations or follow-up |
|---|---|---|---|---|---|
| Backend dependencies with known vulnerabilities may expose user accounts, authentication, vehicle data, or deployment environments to avoidable security risk. | Automated dependency vulnerability scan with `pip-audit`. | `backend/requirements.txt` and backend Python dependencies. | Passing when `additional-test-backend-dependency-audit` passes. | [Backend tests workflow](https://github.com/ernest0507/LAMBA_Team-22/actions/workflows/backend-tests.yml) | `pip-audit` checks dependency vulnerability databases. It does not replace code review, secret scanning, runtime security testing, or manual triage of vulnerabilities that require upstream fixes. |

Link checking is maintained separately through Lychee and does not count as the
Assignment 4 additional QA check.

## Automated Quality Requirement Tests

| QRT | Linked QR | Automated test or CI command | Expected result | Evidence |
|---|---|---|---|---|
| QRT-001 | QR-001 | `pytest tests/test_registration_response_time.py` | At least 95% of 20 registration requests return successful responses within 2 seconds. | [Quality requirement tests](https://github.com/ernest0507/LAMBA_Team-22/blob/main/docs/quality-requirement-tests.md) |
| QRT-002 | QR-002 | `pytest tests/test_returns_clarifictation_ai_provider_unavailable.py` | Assistant returns a controlled clarification response when the AI provider is unavailable. | [Quality requirement tests](https://github.com/ernest0507/LAMBA_Team-22/blob/main/docs/quality-requirement-tests.md) |
| QRT-003 | QR-003 | `pytest tests/test_returns_clarification_negative_cost.py` | Assistant rejects invalid extracted record data with a negative cost value. | [Quality requirement tests](https://github.com/ernest0507/LAMBA_Team-22/blob/main/docs/quality-requirement-tests.md) |

## Manual Evidence That Does Not Count as QRT

| Evidence | Scope | Result | Follow-up PBI or issue |
|---|---|---|---|
| Customer UAT scenarios | Registration, sign-in, vehicle digital twin creation, and AI-chat expense entry. | Results are recorded in `docs/user-acceptance-tests.md` after customer execution. | Follow-up issues are created when UAT reveals defects, product gaps, or changed expectations. |
| Sprint Review discussion | Delivered increment, customer feedback, UAT results, and quality evidence. | Summarized in the Week 4 report and customer review materials. | Follow-up issues or roadmap updates are created as needed. |

Manual evidence supports release and customer validation, but it does not count
as an automated QRT.

## Maintained Assignment 4 Gates

The following Assignment 4 gates remain active for later project work:

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
