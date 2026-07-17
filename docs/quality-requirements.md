# Quality Requirements

## QR-001: Registration API response time

**ISO/IEC 25010 sub-characteristic:** Time behaviour

**Scenario:** When 20 concurrent users submit registration requests under the CI
backend test environment, the registration API shall return successful
responses within 2 seconds for at least 95% of requests.

**Why this matters:** Registration is the first backend-supported onboarding
workflow. Slow registration blocks account creation and makes the product feel
unavailable before users can create their car profile.

**Linked quality requirement tests:** [QRT-001](quality-requirement-tests.md#qrt-001-registration-api-response-time)

**Related ADRs:** [ADR-001: Backend implementation](architecture/adr/ADR-001-backend-implementation.md), [ADR-003: Use database for persistent storage](architecture/adr/ADR-003-database-for-persistent-storage.md)

## QR-002: AI assistant provider failure handling

**ISO/IEC 25010 sub-characteristic:** Fault tolerance

**Scenario:** When the external AI provider is unavailable under the standard
backend runtime environment, the assistant service shall return a controlled
clarification response instead of crashing or exposing an unhandled provider
error.

**Why this matters:** The AI assistant depends on an external provider. Users
still need a stable product response when that provider fails, and backend
failures must not break the chat workflow.

**Linked quality requirement tests:** [QRT-002](quality-requirement-tests.md#qrt-002-ai-assistant-provider-failure-handling)

**Related ADRs:** [ADR-002: Use AI agent for maintenance records](architecture/adr/ADR-002-use-ai-agent-for-maintenance-records.md)

## QR-003: AI assistant extracted record data validation

**ISO/IEC 25010 sub-characteristic:** Integrity

**Scenario:** When the external AI provider returns extracted car record data
under the standard backend runtime environment, the assistant service shall
reject invalid record data such as negative cost values before it can be
accepted as a maintenance record.

**Why this matters:** AI output is not trusted product data. Invalid extracted
records could corrupt maintenance history, expense statistics, and future
recommendations if the backend accepted them without validation.

**Linked quality requirement tests:** [QRT-003](quality-requirement-tests.md#qrt-003-ai-assistant-extracted-record-data-validation)

**Related ADRs:** [ADR-002: Use AI agent for maintenance records](architecture/adr/ADR-002-use-ai-agent-for-maintenance-records.md), [ADR-003: Use database for persistent storage](architecture/adr/ADR-003-database-for-persistent-storage.md)

## QR-004: Maintenance record persistence integrity

**ISO/IEC 25010 sub-characteristic:** Integrity

**Scenario:** When an authenticated user creates a vehicle maintenance record, the backend shall persist the record and return the same record through the record detail endpoint and the vehicle timeline endpoint without losing or corrupting the submitted data.

**Why this matters:** MVP v2 relies on maintenance and expense history being stored consistently. If a created record is not persisted correctly or does not appear in the timeline, users cannot trust the vehicle history, statistics, or future assistant recommendations.

**Linked quality requirement tests:** [QRT-004](quality-requirement-tests.md#qrt-004-maintenance-record-persistence-integrity)

**Related ADRs:** [ADR-003: Use database for persistent storage](architecture/adr/ADR-003-database-for-persistent-storage.md)

## QR-005: Registration event-loop responsiveness

**ISO/IEC 25010 sub-characteristic:** Time behaviour

**Scenario:** When users register or sign in under the backend runtime environment, the authentication workflow shall run password hashing and password verification outside the async event loop so that the backend can continue processing other requests while password operations are executing.

**Why this matters:** Password hashing is intentionally expensive. If it blocks the async event loop, registration and login can slow down unrelated backend requests and make the product feel unavailable during normal onboarding.

**Linked quality requirement tests:** [QRT-005](quality-requirement-tests.md#qrt-005-registration-event-loop-responsiveness)

**Related ADRs:** [ADR-001: Backend implementation](architecture/adr/ADR-001-backend-implementation.md)

## QR-006: Registration database connection release

**ISO/IEC 25010 sub-characteristic:** Resource utilization

**Scenario:** When a new user registration request checks whether an email already exists under the backend runtime environment, the backend shall release the lookup transaction before creating the new user so that database connections are not held longer than necessary during password hashing and user creation.

**Why this matters:** Registration is a high-risk onboarding path. Holding a database connection while expensive work continues can exhaust the connection pool and slow or block other users.

**Linked quality requirement tests:** [QRT-006](quality-requirement-tests.md#qrt-006-registration-database-connection-release)

**Related ADRs:** [ADR-001: Backend implementation](architecture/adr/ADR-001-backend-implementation.md), [ADR-003: Use database for persistent storage](architecture/adr/ADR-003-database-for-persistent-storage.md)

## QR-007: Record photo upload validation

**ISO/IEC 25010 sub-characteristic:** Integrity

**Scenario:** When an authenticated user uploads photos for a vehicle record under the backend runtime environment, the backend shall accept valid image uploads, return photo metadata with an access URL, reject non-image files, and prevent a record from having more than three uploaded photos.

**Why this matters:** MVP v2 adds record photo support. The backend must prevent invalid files and excessive uploads from corrupting record evidence, confusing the Android client, or consuming storage unexpectedly.

**Linked quality requirement tests:** [QRT-007](quality-requirement-tests.md#qrt-007-record-photo-upload-validation)

**Related ADRs:** [ADR-001: Backend implementation](architecture/adr/ADR-001-backend-implementation.md), [ADR-003: Use database for persistent storage](architecture/adr/ADR-003-database-for-persistent-storage.md)

## QR-008: Receipt QR scan reliability and access control

**ISO/IEC 25010 sub-characteristic:** Fault tolerance

**Scenario:** When an authenticated user scans a receipt QR code for a vehicle under the backend runtime environment, the backend shall process valid QR inputs only for vehicles owned by that user, reject unsupported qr-codes or unowned vehicles with controlled HTTP responses, and handle receipt-provider configuration or provider errors without exposing unhandled failures.

**Why this matters:** Receipt QR scanning depends on vehicle-specific data and an external receipt provider. Although the Android application normally shows only the current user's vehicles, the backend API must still protect direct requests with arbitrary vehicle IDs and return predictable errors when input or provider behavior is invalid.

**Linked quality requirement tests:** [QRT-008](quality-requirement-tests.md#qrt-008-receipt-qr-scan-backend-handling)

**Related ADRs:** [ADR-001: Backend implementation](architecture/adr/ADR-001-backend-implementation.md), [ADR-004: Use receipt provider for QR receipt scanning](architecture/adr/ADR-004-use-receipt-provider-for-qr-scanning.md)


## QR-009: Trip distance tracking integrity

**ISO/IEC 25010 sub-characteristic:** Integrity

**Scenario:** When an authenticated user records and finishes a vehicle trip under the backend runtime environment, the backend shall calculate trip distance and duration consistently from submitted location points, ignore invalid coordinates, low-accuracy points, and unrealistic location jumps during metric calculation, protect trip operations by vehicle or trip ownership, and persist completed trip metrics and final mileage without losing or corrupting the recorded data.

**Why this matters:** Trip mode is used to track kilometers driven by a vehicle. Incorrect distance calculation, unsafe handling of noisy GPS points, unauthorized trip access, or corrupted final mileage would make the digital twin mileage, trip history, and future maintenance planning unreliable.

**Linked quality requirement tests:** [QRT-009](quality-requirement-tests.md#qrt-009-trip-distance-tracking-integrity)

**Related ADRs:** [ADR-001: Backend implementation](architecture/adr/ADR-001-backend-implementation.md), [ADR-003: Use database for persistent storage](architecture/adr/ADR-003-database-for-persistent-storage.md)
