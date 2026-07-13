# Contributing to LAMBA

This document describes the contribution workflow for the LAMBA repository. It is intended for team members, reviewers, teaching assistants, and future maintainers who need to understand how changes are planned, implemented, reviewed, verified, and merged.

## Project structure

The main repository areas are:

```text
backend/      FastAPI backend, database configuration, Alembic migrations, Docker Compose setup
frontend/     Android/Kotlin project
docs/         Maintained project documentation
reports/      Weekly public reports
.github/      Pull request template and GitHub Actions workflows
```

Important root-level files:

```text
README.md
CHANGELOG.md
CONTRIBUTING.md
AGENTS.md
mkdocs.yml
build.gradle.kts
settings.gradle.kts
```

Important backend files:

```text
backend/README.md
backend/.env.example
backend/docker-compose.yml
backend/alembic.ini
backend/requirements.txt
```

Important documentation files:

```text
docs/index.md
docs/customer-handover.md
docs/roadmap.md
docs/user-acceptance-tests.md
docs/testing.md
docs/quality-requirements.md
docs/quality-requirement-tests.md
docs/development-process.md
docs/definition-of-done.md
docs/user-stories.md
```

## Repository workflow

LAMBA uses an issue-driven workflow.

1. Create or select a GitHub issue.
2. Make sure the issue has a clear expected outcome and acceptance criteria.
3. Assign an implementer and a different reviewer.
4. Add Story Points and the current Work Status in the project board.
5. Assign the issue to the relevant Sprint milestone.
6. Create a branch from the protected default branch.
7. Implement the change.
8. Open an issue-linked pull request.
9. Wait for review and relevant checks.
10. Merge only after the acceptance criteria are satisfied.

## Issues

Every product, documentation, testing, release, handover, or infrastructure change should be tracked by an issue.

Each selected Sprint issue should include:

- expected outcome;
- acceptance criteria;
- Story Points;
- implementer;
- reviewer;
- current Work Status;
- relevant milestone;
- links to related pull requests after implementation starts.

## Branch naming

Use short branch names that start with the issue number.

Recommended pattern:

```text
<issue-number>-short-description
```

Examples:

```text
235-implement-driver-mode-ui
241-add-sign-out
```

## Pull requests

Pull requests should be linked to the relevant issue.

The PR description should include:

- summary of changes;
- related issue;
- acceptance criteria verification;
- testing performed;
- screenshots or evidence where relevant;
- changelog note when the change is user-visible;
- confirmation that no secrets, credentials, private recordings, private timecodes, or private-only evidence were committed.

Recommended closing syntax:

```text
Closes #<issue-number>
```

Use the repository pull request template when it is available:

```text
.github/pull_request_template.md
```

## Review expectations

Each PR should be reviewed by a person different from the implementer.

Reviewers should check that:

- the linked issue is correct;
- the acceptance criteria are addressed;
- the implementation is understandable and maintainable;
- tests or manual verification are described;
- documentation is updated when needed;
- user-visible changes are reflected in `CHANGELOG.md`;
- public/private separation is respected;
- no credentials, tokens, private links, private timecodes, or customer-sensitive information are committed.

## GitHub Actions checks

The repository currently has these GitHub Actions workflows:

```text
.github/workflows/backend-tests.yml
.github/workflows/docs.yml
.github/workflows/lychee.yml
```

Use them as part of PR verification where relevant:

| Workflow | Purpose |
|---|---|
| `backend-tests.yml` | Backend tests and backend verification. |
| `docs.yml` | Documentation build/deployment verification. |
| `lychee.yml` | Link checking for repository documentation and reports. |

Before merging, make sure the relevant checks are passing or explain in the PR why a check is not applicable.

## Backend verification

Follow the backend instructions in:

```text
backend/README.md
```

The backend is a FastAPI application with PostgreSQL, pgAdmin, Alembic migrations, and Uvicorn.

Official local setup from the backend README:

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

If backend code, migrations, environment configuration, or API behavior changed, verify the affected flow before requesting review.

## Backend configuration

Use `backend/.env.example` to document required backend environment variables without storing real secret values.

Current environment variables documented in `backend/.env.example`:

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
```

Do not commit real values for secrets such as `SECRET_KEY`, database passwords, pgAdmin passwords, or AI provider credentials.

## Android/frontend verification

Use the Gradle wrapper from the repository.

Typical debug build verification:

```bash
./gradlew assembleDebug
```

If frontend-specific Gradle tasks are needed, use the Android project documentation or Android Studio configuration.

For Android changes, the PR should state what was checked, for example:

- debug build completed;
- affected screen opened successfully;
- customer-critical flow was manually tested;
- screenshots or screen recordings were attached when useful.

## Documentation verification

For documentation changes, verify that Markdown links and the hosted documentation build still work where practical.

Documentation build:

```bash
mkdocs build
```

The link-check workflow is:

```text
.github/workflows/lychee.yml
```

For documentation-only changes, application tests may not be required, but the PR should still describe what was checked manually.

## Documentation updates

Update maintained documentation when a change affects product behavior, setup, testing, architecture, quality, access, release, or handover state.

Common documentation files include:

- `README.md`;
- `docs/customer-handover.md`;
- `docs/roadmap.md`;
- `docs/user-acceptance-tests.md`;
- `docs/testing.md`;
- `docs/quality-requirements.md`;
- `docs/quality-requirement-tests.md`;
- `docs/development-process.md`;
- `docs/definition-of-done.md`;
- `docs/user-stories.md`;
- `docs/architecture/README.md`, if present;
- `CHANGELOG.md`;
- weekly reports under `reports/`.

## Changelog policy

User-visible changes should be recorded in `CHANGELOG.md`.

Use the `[Unreleased]` section while work is still in progress. When a SemVer release is created, move the relevant entries into a dated release section.

Examples of user-visible changes:

- new Android screens or flows;
- changed AI assistant behavior;
- changed record creation behavior;
- changed statistics, history, trip, refueling, or achievement behavior;
- changed product access instructions;
- release or APK changes.

## Public/private separation

Do not commit private or instructor-only evidence to the public repository.

Do not commit:

- customer meeting recordings;
- private recording links;
- exact private timecodes;
- credentials or tokens;
- private access instructions;
- customer-identifying evidence that should only be shared through Moodle;
- private presentation rehearsal videos;
- slide decks when the assignment requires them to stay private.

Public reports may include sanitized summaries, public release links, public demo links, public screenshots, and links to maintained documentation.

## Secrets and configuration

Never commit real secrets.

Use placeholders and document configuration requirements without exposing values.

If environment variables are needed, document:

- variable name;
- purpose;
- where it is used;
- whether it is required or optional;
- how the customer or maintainer should provide it.

## Release process

For a release:

1. Merge release-ready changes into the protected default branch.
2. Verify the relevant checks on the default branch.
3. Update `CHANGELOG.md`.
4. Create a SemVer release tag prefixed with `v`.
5. Link the release to the relevant Sprint milestone and weekly report.
6. Include product access or run instructions.
7. Link `docs/customer-handover.md` when the release is related to Assignment 6 transition work.
8. Keep the product access artifact available until grading is complete.

## Customer handover updates

For Assignment 6, keep the following files current:

```text
README.md
docs/customer-handover.md
docs/roadmap.md
reports/week6/README.md
reports/week7/README.md
```

Update `docs/customer-handover.md` whenever any of the following changes:

- product access link;
- release version;
- APK Google Drive link;
- backend deployment method;
- environment variable list;
- customer-side operation status;
- handover level;
- customer confirmation status;
- known limitations;
- troubleshooting steps;
- transition blockers or follow-up support expectations.
