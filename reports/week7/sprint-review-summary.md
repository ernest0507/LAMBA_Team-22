# Sprint 5 Review Summary

## Meeting information

- **Project:** LAMBA
- **Sprint:** Sprint 5 / Week 7
- **Sprint period:** 13.07.2026–19.07.2026
- **Delivered increment:** Final MVP v4 / Assignment 6 MVP v3
- **Participants by role:** customer, team lead, documentation/reporter
- **Session purpose:** final Sprint Review, customer-executed UAT, delivery review, and handover confirmation

The public repository contains a sanitized English transcript. The full recording is private assignment evidence and is supplied only through Moodle.

## Sprint Goal

Complete the final delivery by resolving the remaining Week 6 customer-trial findings, improving product polish and reliability, finalizing customer-facing and deployment documentation, preparing release artifacts, and completing the agreed product transition.

## Completed work

Sprint 5 completed 24 Story Points and delivered:

- profile, vehicle editing, logout, application settings, and theme support;
- receipt duplicate protection and persisted receipt-history data;
- improved achievement and vehicle visuals;
- navigation and large-number input fixes;
- updated backend, ADR, testing, quality, changelog, deployment, and handover documentation;
- final release `v0.4.0`, Android APK, source archive, and integrity checksum;
- final Week 7 reporting and Demo Day materials.

## Demonstrated product areas

- final Android build and product access;
- authentication, persistent login, and logout;
- profile and vehicle-information management;
- light and dark themes;
- receipt QR scanning, history details, and duplicate prevention;
- achievements;
- trip functionality;
- AI assistant interaction;
- delivery package, deployment guidance, and ownership boundaries.

## Customer-executed UAT results

| Area | Result | Notes |
|---|---|---|
| Installation and access | Passed with comments | The current APK was provided after build/version clarification. |
| Authentication and logout | Passed | Persistent login and explicit logout were reviewed. |
| Profile and settings | Passed | Profile, vehicle editing, settings, and theme switching were reviewed. |
| Receipt QR scanning | Passed with known limitation | Duplicate creation was prevented; liters are not always available in receipt data. |
| Achievements | Accepted for current scope | Unlock notifications were not part of the agreed scope. |
| Trip functionality | Reviewed | Updated behavior was demonstrated and tested. |
| AI assistant | Passed with comments | Results depend on available stored context. |
| Handover documentation | Accepted | Archive, APK, deployment, access, ownership, and limitations were reviewed. |

## Customer feedback and response

| Finding or request | Response | Status |
|---|---|---|
| Prevent repeated receipt creation | Added stable identification and car-scoped duplicate checks | Completed |
| Preserve receipt details in history | Persisted normalized receipt data | Completed |
| Increase vehicle-image visibility | Updated image presentation | Completed |
| Improve achievement placeholders | Added locked and unlocked visual states | Completed |
| Add profile and settings flows | Added profile, vehicle editing, logout, and settings | Completed |
| Provide detailed deployment information | Added deployment and customer-handover documentation | Completed |
| Provide archive integrity verification | Generated and delivered a SHA-256 checksum file | Completed |

## Resulting backlog and scope decisions

- Customer-side production deployment remains outside the completed course scope and is the customer's responsibility.
- Achievement unlock notifications remain outside the delivered scope.
- Receipt fields unavailable from the source data remain a documented limitation.
- No new blocking Sprint item was created after the final handover review.

## Risks and known limitations

- The delivered APK references the team's temporary backend and must be rebuilt with a customer-managed backend URL for long-term operation.
- Customer-owned infrastructure, secrets, PostgreSQL, external-service credentials, backups, and monitoring must be configured by the customer.
- Receipt parsing depends on fields available from the receipt provider.
- AI assistant answers depend on available user and vehicle context.

## Final transition decision

- **Handover level:** Ready for independent use
- **Customer confirmation:** Accepted
- **Remaining blockers:** None

The customer confirmed that the documentation was sufficient and that the main delivery requirements were covered.

## Action points

| Action | Owner | Status |
|---|---|---|
| Deliver archive SHA-256 checksum separately with the archive | Team | Completed |
| Supply private acceptance evidence through Moodle | Team | Completed as submission evidence |
| Deploy backend on customer-managed infrastructure when required | Customer | Post-handover responsibility |

## Evidence handling

Private customer-confirmation evidence is supplied through the final Moodle submission and is intentionally excluded from the public repository.

The public repository contains only the sanitized transcript, summary, delivery status, implementation traceability, screenshots, and public product links.
