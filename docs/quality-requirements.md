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

## QR-003: AI assistant extracted record data validation

**ISO/IEC 25010 sub-characteristic:** Integrity

**Scenario:** When the external AI provider returns extracted car record data
under the standard backend runtime environment, the assistant service shall
reject invalid record data such as negative cost values before it can be
accepted as a maintenance record.

**Why this matters:** AI output is not trusted product data. Invalid extracted
records could corrupt maintenance history, expense statistics, and future
recommendations if the backend accepted them without validation.

**Linked quality requirement tests:** [QRT-003](quality-requirement-tests.md#qrt-003-ai-assistant-negative-cost-validation)
