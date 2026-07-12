# LAMBA

LAMBA is an Android application for car owners. It provides a digital vehicle profile, car-related history and expenses, statistics, an AI assistant, trip tracking, receipt QR scanning, and achievements.

## Week 6 trial release

- **GitHub Release:** [v0.3.1 - Week 6 Trial Release](https://github.com/ernest0507/LAMBA_Team-22/releases/tag/v0.3.1)
- **APK:** [Download from Google Drive](https://drive.google.com/file/d/1GJE0OONoq9pnoVfVhQG0RGEbBOTZllVh/view?usp=drive_link)
- **Sprint 4 milestone:** [Sprint 4 - MVP v3](https://github.com/ernest0507/LAMBA_Team-22/milestone/4)
- **Week 6 report:** [reports/week6/README.md](reports/week6/README.md)
- **Hosted documentation:** [LAMBA Documentation](https://ernest0507.github.io/LAMBA_Team-22/)

The Week 6 build is a trial / handover-candidate release. Final customer-side transition is planned after Sprint 5 in Week 7.

## Install the APK

1. Download the APK from the Google Drive link above.
2. Open it on an Android device or Android emulator.
3. If Android blocks the installation, allow installation from the browser or file manager used to open the APK.
4. Install and launch the application.
5. Create a new account through the registration flow.

No fixed test credentials are required for the Week 6 trial build.

## Main product flows

- registration and sign-in;
- digital vehicle profile creation;
- expense, maintenance, repair, and history records;
- AI assistant interaction;
- statistics;
- trip mode and trip history;
- receipt QR scanning;
- achievements;
- logout support.

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

When using a locally running backend from the Android emulator, use `http://10.0.2.2:8000/` as the host address.

### Backend

See [backend/README.md](backend/README.md) for environment configuration, PostgreSQL/Alembic setup, API startup, and verification instructions.

## Documentation

- [Customer handover](docs/customer-handover.md)
- [Roadmap](docs/roadmap.md)
- [Architecture](docs/architecture/README.md)
- [Testing](docs/testing.md)
- [Quality requirements](docs/quality-requirements.md)
- [Quality requirement tests](docs/quality-requirement-tests.md)
- [User Acceptance Tests](docs/user-acceptance-tests.md)
- [Development process](docs/development-process.md)
- [Definition of Done](docs/definition-of-done.md)
- [CHANGELOG](CHANGELOG.md)
- [Hosted documentation](https://ernest0507.github.io/LAMBA_Team-22/)

## Contribution and AI-agent guidance

- [CONTRIBUTING.md](CONTRIBUTING.md)
- [AGENTS.md](AGENTS.md)

Do not commit real credentials, API keys, private recording links, or private access instructions.
