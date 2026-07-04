# Changelog

All notable changes to the LAMBA project are documented in this file.

## [Unreleased]

### Added

- Added assistant mileage update handling with Android car refresh after successful updates. Closes #178; Related #175

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
