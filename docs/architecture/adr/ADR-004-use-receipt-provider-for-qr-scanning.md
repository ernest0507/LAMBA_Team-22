# ADR-004: Use receipt provider for QR receipt scanning

**Status:** Accepted

**Context:** The product must support adding refueling expenses from fuel receipt QR codes. The Android application reads the QR code, but the backend must validate ownership of the selected vehicle, communicate with an external receipt provider, normalize the provider response, and return receipt data in a format that can be saved in the vehicle history.

Receipt QR scanning also needs predictable error handling. If the external provider cannot process a QR code, the backend should return a controlled response instead of exposing an unhandled provider failure to the client.

**Decision:** The backend integrates with the Proverkacheka receipt provider for QR receipt scanning. The backend receives raw QR data or a QR file from the Android client, checks that the requested vehicle belongs to the authenticated user, sends the QR data to the receipt provider, and normalizes the provider response into the internal receipt API schema.

The backend stores receipt-related data together with maintenance records, including the stable receipt identifier, seller details, receipt date, fiscal fields, total amount, and receipt items where available. A receipt identifier is used to prevent adding the same receipt more than once for the same vehicle.

Provider errors are mapped to controlled HTTP responses. In particular, provider error code `5` is treated as an unprocessable receipt input and mapped to HTTP `422`, because the request reached the backend successfully but the receipt data could not be processed as a valid supported receipt.

**Consequences and tradeoffs:** This decision allows users to add fuel-related expenses from QR receipts without manually entering all receipt details. It also keeps receipt processing behind the backend boundary, so the Android client does not need direct access to provider credentials or provider-specific response formats.

The tradeoff is that the receipt QR flow depends on an external provider. Provider downtime, invalid provider configuration, unsupported QR data, or incomplete provider responses can still block full receipt recognition. To reduce this risk, the backend uses controlled error mapping and fallback parsing from QR fields where appropriate. The team must maintain provider configuration, receipt normalization logic, duplicate receipt protection, and automated tests for provider response handling.

**Quality requirements addressed where applicable**
- [QR-008: Receipt QR scan reliability and access control](../../quality-requirements.md#qr-008-receipt-qr-scan-reliability-and-access-control)

**Related verification**
- [QRT-008: Receipt QR scan backend handling](../../quality-requirement-tests.md#qrt-008-receipt-qr-scan-backend-handling)
