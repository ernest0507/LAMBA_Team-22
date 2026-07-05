# ADR-001: Use the FastAPI for backend implementation


**Status:** Accepted

**Context:** The product needs stable backend implementation to recieve from the client side requests for authentication, digital twin management, maintenance records, and AI assistant workflows. Also the backend side must validate input data, support asynchronious communication with database to get access.   

**Decision:** For solving the problem with backend implementation was chosen a FastAPI framework. Such approach helped to organised API routes, Pydentic requests and response schemas, dependency injected database sessions.

**Consequences and tradeoffs**: The FastAPI backend is used for authentication logic, API routes, database-backed workflows, and request or response validation. FastAPI supports assync database access and dependency injection for authentication. The tradeof is the backend requires to maintain routes, schemas, dependencies 

**Quality requirements addressed where applicable** 
- [QR-001: Registration API response time](../../quality-requirements.md#qr-001-registration-api-response-time)
- [QR-005: Registration event-loop responsiveness](../../quality-requirements.md#qr-005-registration-event-loop-responsiveness)
- [QR-006: Registration database connection release](../../quality-requirements.md#qr-006-registration-database-connection-release)
- [QR-007: Record photo upload validation](../../quality-requirements.md#qr-007-record-photo-upload-validation)
