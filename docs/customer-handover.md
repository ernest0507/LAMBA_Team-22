# Customer Handover

**Project:** LAMBA  
**Document status:** Final customer handover version  
**Last updated:** 2026-07-19

This document records the final delivery and transition state of the LAMBA project. It is intended for the customer, teaching assistants, and future maintainers.

## 1. Product summary

LAMBA is an Android application for car owners. It provides a digital vehicle profile, car-related history and expenses, statistics, an AI assistant, trip tracking, receipt QR scanning, achievements, profile management, and application settings.

The delivered product consists of:

- Android mobile client;
- FastAPI backend;
- PostgreSQL persistence;
- Alembic migrations;
- AI provider integration;
- receipt-processing integration;
- maintained repository and hosted documentation;
- final release, APK, source archive, and handover materials.

## 2. Final handover state

| Item | Final state |
|---|---|
| Handover level | **Ready for independent use** |
| Customer confirmation | **Accepted** |
| Remaining blockers | **None** |
| Final release | `v0.4.0` |
| Source-code handover | Complete Android and backend source archive delivered through Google Drive |
| APK access | Final APK delivered through Google Drive |
| Archive integrity | SHA-256 checksum file delivered separately with the archive |
| Backend operation | Customer responsibility on customer-managed infrastructure |
| Team server access | Not transferred and not required for final operation |
| Test account | Available; credentials delivered privately |
| Documentation | Final handover, deployment, testing, and maintenance entry points delivered |

Private customer-confirmation evidence is supplied through the final Moodle submission and is intentionally excluded from the public repository.

## 3. Delivered materials

### 3.1 Source-code archive

**Filename:** `LAMBA_Team22.zip`  
**Google Drive:** <https://drive.google.com/file/d/1g7qxBIab1TvOp1CIxZWTWt7mmh-FeFiH/view?usp=drive_link>

The archive contains the complete Android and backend source materials required for independent deployment and future maintenance. A SHA-256 checksum file is delivered separately with the archive and is not published in the public report.

### 3.2 Final Android APK

**Filename:** `LAMBA_Team22_v0.4.0.apk`  
**Google Drive:** <https://drive.google.com/file/d/1u8FaDVWf6Ru-q_9zywEEDJ5L6XM3hpDv/view?usp=drive_link>

### 3.3 Final release and demo

- Release: <https://github.com/ernest0507/LAMBA_Team-22/releases/tag/v0.4.0>
- Public demo: <https://drive.google.com/file/d/1UUY_8EvetCl70JN_CmoVUoLNK4dsDsgT/view?usp=drive_link>

### 3.4 Test account

A prepared test account is available. Credentials are delivered privately and must not be committed to GitHub, included in public reports, or copied into public documentation.

## 4. Transition scope and ownership

### 4.1 Transferred to the customer

| Area | Final arrangement |
|---|---|
| Android application | Final APK delivered |
| Product source code | Complete source archive delivered |
| Backend source code | Included in the archive |
| Android source code | Included in the archive |
| Deployment knowledge | Documented for customer-side deployment |
| Test access | Prepared account provided privately |
| Documentation | Handover, deployment, setup, testing, and maintenance information delivered |

### 4.2 Not transferred

| Area | Final arrangement |
|---|---|
| Team-managed temporary server | Not transferred |
| Existing server credentials | Not transferred |
| Private team infrastructure | Not transferred and not required |
| Real secrets and API keys | Not stored in the repository or public documentation |

### 4.3 Customer responsibilities

The customer is responsible for:

- preparing and maintaining customer-managed infrastructure;
- configuring PostgreSQL and backend environment variables;
- creating and protecting customer-owned secrets and API credentials;
- deploying and operating the backend;
- applying Alembic migrations;
- configuring backups, monitoring, recovery, and service availability;
- rebuilding the Android client with the customer backend URL;
- signing and distributing future Android releases.

## 5. Product access and APK installation

1. Download `LAMBA_Team22_v0.4.0.apk`.
2. Open it on an Android device or emulator.
3. Allow installation from the browser or file manager if Android requests it.
4. Install and launch LAMBA.
5. Sign in with the privately delivered test account or create a new account.
6. Add or select a vehicle and verify the main product flows.

The delivered APK currently references the team's temporary backend. Long-term customer operation requires rebuilding the Android application with the customer-managed backend URL.

## 6. Reference deployment environment

The documented reference deployment uses:

- Ubuntu 24.04;
- Python, FastAPI, and Uvicorn;
- systemd for backend process management;
- PostgreSQL 17.10;
- Alembic migrations;
- Docker Compose for PostgreSQL and pgAdmin only;
- pgAdmin for database administration.

The backend application itself is not containerized in the reference deployment.

Detailed deployment steps are in [deployment-guide.md](deployment-guide.md) and [backend/README.md](../backend/README.md).

## 7. Configuration and secrets

The backend environment template is `backend/.env.example`.

Customer deployment must use private values for database credentials, `SECRET_KEY`, AI provider credentials, and any external-service credentials. Real `.env` files, passwords, API keys, private recording links, and test-account credentials must not be committed.

## 8. Verification and smoke testing

After installation or deployment:

1. verify the backend health endpoint;
2. register or sign in;
3. add or select a car;
4. inspect profile and vehicle editing;
5. create or inspect history records;
6. scan a supported receipt QR code and verify duplicate protection;
7. inspect statistics;
8. test the AI assistant;
9. test trip mode;
10. inspect achievements;
11. verify logout;
12. record any deployment-specific findings.

## 9. Known limitations

- customer-side production deployment has not been demonstrated and is not claimed;
- the delivered APK references the team's temporary backend;
- receipt parsing depends on data available from the receipt provider;
- fuel volume in liters may not always be present in receipt data;
- achievement unlock notifications are outside the delivered scope;
- AI assistant responses depend on available stored context and configured external services.

These limitations do not block the achieved **Ready for independent use** handover level.

## 10. Main documentation entry points

| Purpose | Entry point |
|---|---|
| Repository overview | [README.md](../README.md) |
| Week 7 report | [reports/week7/README.md](../reports/week7/README.md) |
| Deployment guide | [docs/deployment-guide.md](deployment-guide.md) |
| Backend setup | [backend/README.md](../backend/README.md) |
| Environment template | [backend/.env.example](../backend/.env.example) |
| Roadmap | [docs/roadmap.md](roadmap.md) |
| Testing | [docs/testing.md](testing.md) |
| Quality requirements | [docs/quality-requirements.md](quality-requirements.md) |
| UAT | [docs/user-acceptance-tests.md](user-acceptance-tests.md) |
| Development process | [docs/development-process.md](development-process.md) |
| Definition of Done | [docs/definition-of-done.md](definition-of-done.md) |
| Changelog | [CHANGELOG.md](../CHANGELOG.md) |
| Hosted documentation | [LAMBA Documentation](https://ernest0507.github.io/LAMBA_Team-22/) |

## 11. Final transition confirmation checklist

### Delivery and access

- [x] Customer has access to the final APK.
- [x] Customer has access to the complete source-code archive.
- [x] Archive SHA-256 checksum file was prepared and delivered separately.
- [x] Prepared test-account information is available privately.
- [x] Public delivery files are accessible without additional team permissions.

### Deployment and ownership

- [x] Customer-side backend deployment and operation responsibility is documented.
- [x] Team temporary server access is not part of the handover.
- [x] Deployment documentation supports an independent first deployment.
- [x] Customer-owned variables, secrets, and external-service credentials are documented.
- [x] Future Android builds must use the customer backend URL.

### Final decision

- [x] Handover level: **Ready for independent use**
- [x] Customer confirmation: **Accepted**
- [x] Follow-up blockers: **None**

## 12. Support expectations

No ongoing support period is assumed. Any post-handover assistance must be agreed separately with the customer. Customer-owned deployment, infrastructure, secrets, backups, monitoring, future builds, and service availability are outside the completed course scope.

## 13. Private confirmation evidence

The full Sprint Review recording and the private customer-confirmation screenshot are supplied through the final Moodle submission. They are intentionally excluded from the public repository because they are private assignment evidence.

The public repository contains the sanitized transcript, Sprint Review summary, delivery status, implementation traceability, screenshots, and public product links.
