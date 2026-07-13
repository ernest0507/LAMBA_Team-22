# AGENTS.md

This file provides guidance for AI assistants, automation agents, and human contributors using AI support in the LAMBA repository.

## Project context

LAMBA is a mobile application for car owners. The product helps users maintain a digital car profile, record car-related expenses and events, interact with an AI assistant, review statistics, use trip/refueling-related flows, and prepare for customer handover.

The repository contains:

```text
backend/      FastAPI backend, database configuration, Alembic migrations, Docker Compose setup
frontend/     Android/Kotlin application
docs/         Maintained documentation
reports/      Weekly public reports
.github/      Pull request template and GitHub Actions workflows
```

Important files and directories:

```text
README.md
CHANGELOG.md
backend/README.md
backend/.env.example
backend/docker-compose.yml
backend/alembic.ini
backend/requirements.txt
docs/index.md
docs/customer-handover.md
docs/roadmap.md
docs/user-acceptance-tests.md
docs/testing.md
docs/quality-requirements.md
docs/quality-requirement-tests.md
docs/development-process.md
docs/definition-of-done.md
reports/
mkdocs.yml
```

## General rules for agents

Agents may help with:

- drafting documentation;
- summarizing meeting notes or transcripts;
- preparing issue and PR descriptions;
- improving report structure;
- checking consistency between documentation files;
- creating sanitized public summaries;
- suggesting acceptance criteria;
- suggesting verification steps.

Agents must not:

- invent evidence;
- claim that tests passed unless there is actual evidence;
- expose private recordings, exact private timecodes, credentials, tokens, or private customer information;
- commit or publish instructor-only evidence;
- replace human review;
- make release, handover, deployment, or acceptance claims without explicit confirmation from the team or customer evidence.

## Public/private separation

Treat the following as private unless explicitly approved for public release:

- Sprint Review or customer meeting recordings;
- exact private timecodes;
- customer-identifying details;
- credentials, tokens, and private access instructions;
- rehearsed presentation videos;
- slide decks when the assignment requires them to be Moodle-only;
- written customer confirmations that include private context.

Public repository files may include:

- sanitized English summaries;
- public report links;
- public release links;
- public demo links;
- public screenshots that do not expose secrets;
- maintained documentation.

## Documentation locations

Use these files for the relevant information:

| Information | Location |
|---|---|
| Public repository entry point | `README.md` |
| Customer handover state and access guidance | `docs/customer-handover.md` |
| Roadmap and current course outcome | `docs/roadmap.md` |
| UAT scenarios | `docs/user-acceptance-tests.md` |
| Testing strategy | `docs/testing.md` |
| Quality requirements | `docs/quality-requirements.md` |
| Quality requirement tests | `docs/quality-requirement-tests.md` |
| Development process | `docs/development-process.md` |
| Definition of Done | `docs/definition-of-done.md` |
| User stories | `docs/user-stories.md` |
| Hosted documentation entry point | `docs/index.md` |
| Backend setup | `backend/README.md` |
| Backend environment example | `backend/.env.example` |
| Weekly public reports | `reports/week*/README.md` |
| LLM usage disclosure | `reports/week*/llm-report.md` |

## GitHub Actions

The repository currently has these workflows:

```text
backend-tests.yml
docs.yml
lychee.yml
```

When suggesting or reviewing changes, reference the relevant checks:

- backend changes should consider `backend-tests.yml`;
- documentation changes should consider `docs.yml`;
- link-heavy documentation or report changes should consider `lychee.yml`.

Do not claim a workflow passed unless there is actual evidence from GitHub Actions or the user.

## Backend commands

Use `backend/README.md` as the source of truth.

Current backend local setup from the repository documentation:

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

## Backend configuration

The backend environment template is:

```text
backend/.env.example
```

It currently documents:

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

Do not expose real values for secrets or credentials. Use placeholders only.

## Assignment 6 guidance

For Assignment 6, keep the distinction between Week 6 and Week 7.

- Week 6 is the trial release and transition-readiness Sprint.
- Week 7 is the final transition and MVP v3 Sprint.

Do not put Week 7 final transition claims into Week 6 documents unless they are clearly marked as expected follow-up work.

Week 6 public files should not claim final acceptance, final transition, customer-side deployment, or MVP v3 completion unless that has actually happened.

## Handover claims

When editing `docs/customer-handover.md`, use only the real current state.

Allowed handover-level wording:

- Ready for independent use;
- Independently used by customer;
- Deployed or operated on customer side.

If the final status is not confirmed yet, say so explicitly.

Allowed customer-confirmation wording:

- Accepted;
- Accepted with follow-up items;
- Not yet accepted;
- Awaiting customer confirmation.

Do not imply acceptance when no response was received.

For Week 6, the safe wording is:

```text
Week 6 is a trial release and transition-readiness review stage.
Full transition is planned for Week 7 after Sprint 5.
```

## Issue and PR assistance

When drafting issues, include:

- description;
- expected outcome;
- acceptance criteria;
- Story Points placeholder if the value is not known yet;
- implementer and reviewer fields if known;
- milestone;
- links to related artifacts.

When drafting PR descriptions, include:

- summary;
- changes;
- testing performed;
- acceptance criteria verification;
- public/private safety check;
- related issue using `Closes #<issue-number>`.

## Report writing rules

Weekly reports should be factual and evidence-based.

Use:

- direct links to repository files;
- direct links to releases, product access artifacts, and public demo videos;
- screenshots from `reports/week*/images/`;
- clear status labels such as Done, In Progress, Deferred, Accepted with follow-up items.

Do not use vague claims like "everything is complete" without evidence.

Do not include private recording links, exact private timecodes, credentials, or private access instructions in public reports.

## Transcript and summary rules

When preparing public transcripts or summaries:

- translate into English when required;
- keep speaker roles accurate;
- use sanitized content;
- remove private recording links;
- do not include credentials or private access details;
- use broader thematic timestamp ranges for public readability when appropriate;
- keep exact private timecodes for Moodle/private submission only when required.

## LLM disclosure

If AI assistance was used, document it in the relevant weekly LLM report.

A good LLM report explains:

- which tools were used;
- what they helped with;
- what humans verified;
- what limitations or corrections were needed;
- that AI-generated content was not treated as independent evidence.

## Verification before committing generated documentation

Before committing AI-assisted documentation, check:

- links are correct;
- repository-relative links work;
- public/private separation is respected;
- names, issue numbers, PR numbers, release links, and dates are correct;
- no placeholder such as `TBD` remains unless intentionally marked as future work;
- claims match the actual repository state;
- the affected weekly report links to the new or updated artifact.

## Commands agents may reference

Use existing repository files as the source of truth before suggesting commands.

Current useful commands include:

```bash
# Documentation build
mkdocs build

# Android debug build
./gradlew assembleDebug
```

Backend setup from `backend/README.md`:

```powershell
cd backend
python -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
docker compose up -d postgres pgadmin
alembic upgrade head
python -m uvicorn app.main:app --reload
```

If a command is not verified for the current environment, state that it should be checked against `backend/README.md`, Android Studio project settings, or CI output.

## Preferred writing style

Use clear, concise English in repository documentation.

Prefer:

- short sections;
- tables for evidence and traceability;
- explicit status labels;
- direct links;
- factual descriptions.

Avoid:

- excessive marketing language;
- unsupported claims;
- hidden private evidence in public files;
- speculative post-course planning beyond the assignment scope.
