# LAMBA

LAMBA is an Android application for car owners. It provides a digital vehicle
profile, car-related history and expenses, statistics, an AI assistant, trip
tracking, receipt QR scanning, achievements, profile management, and
application settings.

## Final Assignment 6 delivery

The final course increment is published as release `v0.4.0`.

Assignment 6 refers to the final course increment as **MVP v3**. The repository
uses the internal label **MVP v4** because the team's internal MVP sequence
advanced earlier. Both names refer to the same final Assignment 6 delivery.

| Artifact | Link |
|---|---|
| Final release | [v0.4.0](https://github.com/ernest0507/LAMBA_Team-22/releases/tag/v0.4.0) |
| Final Android APK | [LAMBA_Team22_v0.4.0.apk](https://drive.google.com/file/d/1u8FaDVWf6Ru-q_9zywEEDJ5L6XM3hpDv/view?usp=drive_link) |
| Complete source archive | [LAMBA_Team22.zip](https://drive.google.com/file/d/1g7qxBIab1TvOp1CIxZWTWt7mmh-FeFiH/view?usp=drive_link) |
| Final public demo | [MVP v4 demo](https://drive.google.com/file/d/1UUY_8EvetCl70JN_CmoVUoLNK4dsDsgT/view?usp=drive_link) |
| Week 7 report | [reports/week7/README.md](reports/week7/README.md) |
| Customer handover | [docs/customer-handover.md](docs/customer-handover.md) |
| Deployment guide | [docs/deployment-guide.md](docs/deployment-guide.md) |
| Hosted documentation | [LAMBA Documentation](https://ernest0507.github.io/LAMBA_Team-22/) |

## Final transition status

- **Handover level:** Ready for independent use
- **Customer confirmation:** Accepted
- **Remaining blockers:** None

The customer is responsible for deploying and operating the backend on
customer-managed infrastructure. The team's temporary backend is not
transferred as customer infrastructure.

Private customer-confirmation evidence is supplied through the final Moodle
submission and is intentionally excluded from the public repository.

## Install the APK

1. Download `LAMBA_Team22_v0.4.0.apk`.
2. Open it on an Android device or Android emulator.
3. Allow installation from the browser or file manager if Android requests it.
4. Install and launch LAMBA.
5. Sign in with the privately delivered test account or create a new account.

The delivered APK currently references the team's temporary backend. Long-term
customer operation requires rebuilding the Android application with the
customer-managed backend URL.

## Main product flows

- registration and sign-in;
- digital vehicle profile creation and editing;
- profile management, logout, and application settings;
- expense, maintenance, repair, receipt, and history records;
- receipt QR scanning with duplicate protection;
- statistics;
- AI assistant interaction;
- trip mode and trip history;
- achievements;
- light and dark themes.

## Run from source

### Requirements

- Git;
- Android Studio;
- Android SDK 36;
- Android 8.0 or later on a physical device or emulator;
- Python and Docker for local backend work.

### Clone

```bash
git clone https://github.com/ernest0507/LAMBA_Team-22.git
cd LAMBA_Team-22
```

### Android

1. Open the repository in Android Studio.
2. Sync Gradle files.
3. Start an Android emulator or connect a physical Android device.
4. Build and run the `app` configuration.

When using a locally running backend from the Android emulator, use
`http://10.0.2.2:8000/` as the host address.

### Backend

See [backend/README.md](backend/README.md) and
[docs/deployment-guide.md](docs/deployment-guide.md) for environment
configuration, PostgreSQL/Alembic setup, startup, deployment, and verification
instructions.

## Documentation

- [Customer handover](docs/customer-handover.md)
- [Deployment guide](docs/deployment-guide.md)
- [Roadmap](docs/roadmap.md)
- [Architecture](docs/architecture/README.md)
- [Testing](docs/testing.md)
- [Quality requirements](docs/quality-requirements.md)
- [Quality requirement tests](docs/quality-requirement-tests.md)
- [User Acceptance Tests](docs/user-acceptance-tests.md)
- [Development process](docs/development-process.md)
- [Definition of Done](docs/definition-of-done.md)
- [CHANGELOG](CHANGELOG.md)
- [CONTRIBUTING](CONTRIBUTING.md)
- [AI/agent guidance](AGENTS.md)

Do not commit real credentials, API keys, private recording links, private
customer-confirmation screenshots, or private access instructions.
