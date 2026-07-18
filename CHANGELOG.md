# Changelog

All notable changes to the LAMBA project are documented in this file.

## [Unreleased]

## [v0.4.0] - 2026-07-18

### Added

- Added ability to edit information about digital twin in profile screen. Closes #273
- Added icons for locked and unlocked achievements. Closes #285

### Fixed

- Fixed size of the car images in registration and main screens. Closes #267
- Fixed bug when the same receipt can be added multiple times using QR scanning. Closes #275
- Fixed bug when navigation breaks after QR scanning and correctness of statistics display when large numbers append. Closes #277
- Fixed ability of adding receipt for different accounts or cars, but only once. Closes #279

## [v0.3.1] - 2026-07-12

This release maps to the Assignment 6 final course increment, MVP v3.

### Added

- Added receipt QR-code scanning flow between Android and backend, including the scanner screen, receipt scan API integration, parsed receipt data handling, and history display support. Closes #251; Related #176
- Added the Achievements screen on Android with category sections, icons, and locked/unlocked achievement cards. Closes #238
- Added backend achievement APIs and persistence for manual user achievements. Closes #250
- Added trip backend integration and trip history flow in the Android application. Closes #242

### Changed

- Improved vehicle setup dropdown fields for mark and model selection. Closes #247
- Updated achievements so road and repair achievements are visible as cards and can support manual unlock behavior. Related #238; Related #250

### Fixed

- Fixed manual achievement persistence so unlocked manual achievements remain saved for the user. Closes #254

## [v0.3.0] - 2026-07-05

### Added

- Added persistent assistant chat history with chat-specific message storage and active chat support. Closes #190
- Connected the Android statistics screen to the backend statistics endpoint. Closes #181; Related #34; Related #166
- Connected Android record forms and history to record photo upload and display flows. Closes #180; Related #179
- Added backend persistence and API endpoints for maintenance record photos. Closes #179
- Added assistant mileage update handling with Android car refresh after successful updates. Closes #178; Related #175
- Enhanced vehicle setup with suggested mark and model selection, while allowing manual model entry when the desired model is unavailable. Related #164
- Enhanced the Statistics screen with monthly, 6-month, and yearly views, period navigation, summary cards, charts, and responsive layout improvements. Related #165
- Added loading and success states for registration and record creation flows to provide clear progress feedback during backend requests. Related #162
- Added image picker support to all history record forms, allowing users to select photos directly from their device. Related #163

### Fixed

- Fixed Android history compile errors caused by the missing wagon drawable and composable bitmap decoding. Closes #177
- Fixed registration response-time failures under concurrent load by releasing the email lookup connection before password hashing and running password hashing and verification outside the async event loop. Closes #187

## [v0.2.0] - 2026-06-28

### Added

- Added an expandable AI chat panel on the Home screen with swipe gestures, message bubbles, input field, and automatic scrolling to the latest messages. Related #138
- Added Record Type selection screen for creating new history entries, allowing users to choose the type of record before adding event details. Related #118
- Added Expense Record screen with a form for entering expense information and navigation from the Record Type selection screen. Related #118
- Added Maintenance and Repair Record screens for creating vehicle service history entries using the shared application interface. Related #116

## [v0.1.0] - 2026-06-21

### Added

- Added Android-to-backend authentication integration and local pgAdmin support for auth verification. Related #97
- Added authentication backend API with FastAPI, including user registration, login, JWT Bearer authentication, current-user endpoint, PostgreSQL configuration, and database migrations. Related #80
- Added Login and Registration screens for Android application with email, password, password confirmation fields, navigation links, and reusable design-system components. Related #61
- Added shared LAMBA UI styling tokens for colors, spacing, radii, and typography. Related #54
- Added reusable UI components for the digital twin creation flow, including a back button and text field component. Related #54
- Added the first screen structure for creating a new digital twin with a step indicator and input field. Related #54
- Added digital vehicle creation flow with onboarding and data-entry screens for creating a vehicle digital twin. Related #50
- Added Profile screen UI with vehicle information section, notifications, settings, help section, and localized Russian interface. Related #48
- Added Statistics screen UI with expense summaries, period selection, category breakdown, and maintenance/fuel expense overview. Related #42
- Added History screen UI with grouped timeline sections, maintenance records, fuel expense records, and AI insight card. Related #26
- Added Expenses screen for MVP v1 with expense-entry form and validation support. Related #51
- Added application-wide navigation flow between screens, including forward and back navigation and transitions to History and Statistics screens. Related #51

### Changed

- Introduced shared LAMBA UI design tokens for colors, typography, spacing, corner radii, and reusable styling components to ensure consistent visual appearance across the application. Related #54
- Improved authentication experience by adding validation rules for Login and Registration screens, including email format validation, password length validation, password confirmation matching, and inline error messages. Related #69

### Fixed

- Fixed password-field validation issues during registration flow. Related #79
- Fixed validation behavior in the digital vehicle creation flow to correctly display validation errors for invalid or incomplete input data. Related #66
