# Week 6 Transition-Readiness Meeting and Customer Trial Summary

**Project:** LAMBA  
**Meeting type:** Week 6 transition-readiness discussion, customer-facing documentation review, and customer trial  
**Purpose:** Review Week 6 handover readiness, validate documentation expectations, try the current APK, and identify Sprint 5 follow-up work.

## 1. Main outcome

The meeting confirmed that Week 6 is not a full product transfer. It is a trial release and transition-readiness stage. The full transition is expected in Week 7 after Sprint 5.

The customer expects the final handover to reach the strongest transition level: the product should be deployed or operated on the customer side. This means the backend should ultimately be deployable and maintainable by the customer rather than permanently depending on the team's server.

## 2. Access and handover expectations

The customer accepted the idea of distributing the APK through cloud storage, with the APK linked from the GitHub Release.

The customer also clarified that final handover should include the project source code, not only access to the application or public GitHub visibility. The expected artifact is a complete archive in Google Drive containing the backend, frontend, and other relevant project materials.

The customer requested prepared test accounts with example data so that future maintainers or another team can understand the product more easily.

## 3. Documentation expectations

Detailed documentation was identified as a must-have. The customer specifically wants clear instructions for deployment and launch, especially because the backend should eventually run on the customer side.

The documentation should cover:

- backend setup and deployment;
- required environment variables and secrets handling;
- Android application launch instructions;
- backend verification steps;
- product access instructions;
- troubleshooting and support expectations.

The customer also requested that the hosted documentation should be exportable, for example as a PDF.

## 4. Customer trial findings

During the APK trial and product inspection, the customer identified several issues and improvement points.

| Area | Feedback | Follow-up |
|---|---|---|
| APK installation | The APK package conflicted with another application. | Fix package identity before final delivery. |
| App identity | Similar names/icons caused confusion. | Add a clear app name and icon. |
| Car selection | The improved car selection flow was positively received. | Keep manual fallback for missing models. |
| QR/refueling | QR receipt data was recorded as a generic expense, not a refueling record. | Change QR flow to create a refueling record. |
| QR navigation | QR entry point in the sidebar was not clear enough. | Improve discoverability. |
| Achievements | Achievements are in progress and need completion/polish. | Continue achievement work before release. |
| Visual assets | The rotated wheel image was unclear. | Adjust or replace the visual. |
| Car images | Some car images appeared too small because of image/background scaling. | Crop or scale images better. |
| Duplicate receipts | The same receipt could potentially be added more than once. | Store receipt identifiers to prevent duplicates. |

## 5. Sprint 5 follow-up

The meeting produced the following likely Sprint 5 follow-up items:

- complete or fix QR-based refueling record creation;
- improve trip mode issues;
- finish and polish achievements;
- add or improve the user profile/account page;
- store the authentication token on the phone;
- finalize logout/sign-out behavior;
- fix app package/name/icon identity;
- add receipt identifier storage;
- improve car image scaling;
- export hosted documentation as PDF;
- prepare a full source-code archive for customer handover;
- improve backend deployment documentation for customer-side operation.

## 6. Handover status after the meeting

| Item | Status |
|---|---|
| Week 6 status | Trial release and transition-readiness review. |
| Full transition | Planned for Week 7 after Sprint 5. |
| Final target handover level | Deployed or operated on customer side. |
| APK access | Google Drive link from GitHub Release is acceptable. |
| Source code handover | Complete archive in Google Drive is expected. |
| Backend operation | Customer-side deployment is expected for final operation. |
| Test accounts | Requested with example data. |
| Documentation | Detailed deployment documentation is required; hosted docs should be exportable as PDF. |

## 7. Public/private handling note

This summary is sanitized for repository use. It does not include the private recording link, exact private timecodes for Moodle, credentials, private access instructions, or customer-sensitive evidence.
