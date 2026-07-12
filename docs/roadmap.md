# Roadmap

## Sprint 1 — MVP v1 Foundation

**Milestone:** Sprint 1 - MVP v1  
**Dates:** 15.06.2026 – 21.06.2026  
**Status:** Completed

### Sprint Goal

Deliver the MVP v1 mobile flow that lets a car owner create an account, create a basic digital twin, and navigate between the main MVP screens.

### Focus / Expected Outcome

Sprint 1 focused on moving from the earlier prototype/MVP v0 direction toward a more integrated MVP v1. The expected outcome was a mobile product increment with backend-connected product foundations, basic digital twin data flow, AI-assistant chat integration work, and navigation between the main MVP screens.

### Planned / Delivered Items

- US-01: Storing car's data
- PBI: Integrating the chat with AI-assistant
- PBI: Implement MVP v1 navigation between screens
- Registration and sign-in foundation
- Basic car digital twin creation flow

---

## Sprint 2 — AI Assistant, Expenses, and Quality Stabilization

**Milestone:** Sprint 2  
**Dates:** 22.06.2026 – 28.06.2026  
**Status:** Completed

### Sprint Goal

Add and demonstrate the AI-agent chat as the main product improvement for the Sprint, while continuing to stabilize the MVP v1 foundation and extend the car history / expense tracking flow.

### Focus / Expected Outcome

Sprint 2 focused on connecting the AI assistant to the core user flow and making the product increment more useful for real car ownership scenarios. The team also worked on expenses and events, maintenance and repair forms, backend assistant validation, and quality documentation.

The expected outcome was a more integrated product increment where the user can:

- register and sign in;
- create or view a car digital twin;
- interact with the AI assistant;
- record expenses through the assistant;
- view expenses and events in the history/timeline;
- use forms for expenses, maintenance, and repair records;
- rely on documented testing and quality requirements.

### Planned / Delivered Items

- US-02: Interact with AI-agent
- US-03: Main expenses and events timeline
- PBI: Integrating the chat with AI-assistant
- PBI: Implement maintenance and repair forms
- PBI: Implement note type selection screen and expenses form
- PBI: Implemented the UI design for the AI chat on the home screen
- PBI: AI assistant backend configuration and dependencies
- PBI: AI assistant message schemas
- PBI: AI assistant record extraction service
- PBI: Assistant message endpoint for record creation
- PBI: AI assistant validation tests
- PBI: Registration response time automated test
- Quality requirements, quality requirement tests, testing documentation, and backend test workflow

### Customer Review and UAT Outcome

During the Week 4 customer review and UAT session, the customer accepted the tested flows from the user-story perspective:

- user registration;
- car digital twin creation;
- AI chat / expense recording;
- user login.

The customer confirmed that the product foundation was moving in the right direction, but requested further usability and polish improvements for the next Sprint.

---

## Sprint 3 — MVP v2 Statistics, Feedback Response, and Documentation

**Milestone:** [Milestone 3](https://github.com/ernest0507/LAMBA_Team-22/milestone/3)  
**Dates:** 29.06.2026 – 05.07.2026  
**Status:** Completed / MVP v2 released  
**Release:** [v0.3.0 — MVP v2 Assignment 5 Sprint Increment](https://github.com/ernest0507/LAMBA_Team-22/releases/tag/v0.3.0)

### Sprint Goal

Deliver MVP v2 by adding statistics screens and improving the product based on customer feedback, while also making the project easier to understand and maintain through architecture, quality, and process documentation.

### Focus / Expected Outcome

Sprint 3 focused on the statistics increment and on customer feedback from the previous Sprint Review. The expected outcome was an MVP v2 increment where the user can better understand car-related data and where reviewers can inspect maintained project documentation through a hosted documentation site.

The Sprint focused on:

- statistics screens for expenses, mileage, and fuel-related information;
- backend support for statistics data;
- frontend statistics screens and integration;
- clearer loading and success feedback after user actions;
- photo attachment support for breakdown and maintenance records;
- improved car make/model entry through selectable lists and manual fallback;
- better AI assistant context through preserved chat history;
- continued timeline/history improvements;
- architecture, ADR, quality, testing, UAT, and development-process documentation;
- hosted documentation through GitHub Pages.

### Planned / Delivered Items

- US-04 / Statistics foundation: statistics for expenses, mileage, and fuel-related information.
- PBI: Add loading and success confirmation screens.
- PBI: Add photo attachment support for breakdown and maintenance records.
- PBI: Replace manual vehicle model input with selectable brand/model options and manual fallback.
- PBI: Persist assistant chat history to preserve more context.
- PBI: Add/extend backend statistics endpoints and Android statistics integration.
- PBI: Improve history cards and timeline details for car records.
- PBI: Add or update quality requirement tests, including registration response time and database persistence workflow.
- PBI: Update architecture documentation, ADRs, development-process documentation, UAT documentation, and hosted documentation site.
- PBI: Prepare MVP v2 release, APK/product access artifact, and demo evidence.

### Customer Review and UAT Outcome

During the Week 5 Sprint Review and UAT session, the customer reviewed the MVP v2 direction and accepted the main product direction with follow-up comments.

Accepted or validated areas:

- registration/login flow with clearer feedback;
- AI assistant record creation flow, with a request for more human-like confirmation messages;
- statistics screens for expenses, mileage, and fuel/liters;
- timeline/history records for fuel, mileage, and repair-related events;
- the general direction of achievements as a future engagement feature.

Follow-up feedback:

- improve AI assistant response tone and user-facing confirmation messages;
- continue polishing the statistics and history screens;
- investigate trip mode with a realistic implementation path;
- continue UI polish for icons, car drawings, spacing, and button consistency;
- defer lower-priority features such as broad AI data analysis, voice interaction, PDF upload, and multi-user car ownership.

---

## Sprint 4 — Week 6 Trial Increment for MVP v3

**Milestone:** [Sprint 4 — MVP v3](https://github.com/ernest0507/LAMBA_Team-22/milestone/4)  
**Sprint Backlog:** [Sprint 4 — MVP v3 board](https://github.com/users/ernest0507/projects/8/views/1)  
**Dates:** 06.07.2026 – 12.07.2026  
**Status:** Completed  
**Sprint size:** 61 Story Points  
**Trial release:** [v0.3.1 - Week 6 Trial Release](https://github.com/ernest0507/LAMBA_Team-22/releases/tag/v0.3.1)

### Sprint Goal

Deliver an increment with QR-code-based petrol expense creation, drive mode with real-time mileage tracking, and an achievement system so users can track car ownership activity more conveniently and receive feedback on their progress.

### Focus / Delivered Outcome

Sprint 4 significantly expanded the product beyond the original MVP expectations. The most important improvement was the trip mode feature, which made LAMBA more useful as an active car-ownership assistant rather than only a static digital garage.

The Sprint delivered:

- trip start/finish flow with mileage update;
- Android foreground trip tracking;
- trip point synchronization;
- backend trip write/read APIs and tests;
- QR receipt scan backend support;
- frontend-backend QR scan integration;
- driver-mode UI and backend API integration;
- achievement screen and backend achievement support;
- backend logout endpoint;
- assistant memory and breakdown-handling fixes;
- frontend-backend integration improvements.

### Delivered Sprint 4 Items

- [#175 — US-12: Start and Finish Trip Mode with Refueling and Mileage Update](https://github.com/ernest0507/LAMBA_Team-22/issues/175)
- [#176 — US-13: Create refueling record from fuel receipt QR code](https://github.com/ernest0507/LAMBA_Team-22/issues/176)
- [#235 — PBI: Implementing UI for driver mode](https://github.com/ernest0507/LAMBA_Team-22/issues/235)
- [#238 — US-14: Achievement system](https://github.com/ernest0507/LAMBA_Team-22/issues/238)
- [#242 — PBI: Implementation frontend and connection with backend API](https://github.com/ernest0507/LAMBA_Team-22/issues/242)
- [#251 — PBI: Connect receipt QR scan flow between frontend and backend](https://github.com/ernest0507/LAMBA_Team-22/issues/251)
- [#219 — Bug: Fix assistant memory and breakdown handling](https://github.com/ernest0507/LAMBA_Team-22/issues/219)
- [#226 — PBI: Add backend trip tracking write API](https://github.com/ernest0507/LAMBA_Team-22/issues/226)
- [#227 — PBI: Add backend trip tracking read API and tests](https://github.com/ernest0507/LAMBA_Team-22/issues/227)
- [#230 — PBI: Add Android foreground trip tracking service](https://github.com/ernest0507/LAMBA_Team-22/issues/230)
- [#231 — PBI: Add Android trip point sync data layer](https://github.com/ernest0507/LAMBA_Team-22/issues/231)
- [#240 — PBI: Add backend logout endpoint](https://github.com/ernest0507/LAMBA_Team-22/issues/240)
- [#245 — PBI: Add backend receipt QR scan API](https://github.com/ernest0507/LAMBA_Team-22/issues/245)
- [#250 — PBI: Implement backend API for manually unlocked achievements](https://github.com/ernest0507/LAMBA_Team-22/issues/250)

### Week 6 Customer Trial and Transition-Readiness Outcome

The customer reviewed the current product, customer-facing documentation, APK access approach, backend-operation expectations, and the planned final handover level.

The Week 6 outcome is:

- the product is already usable as a trial increment;
- the customer tried the product flow during the meeting;
- the current product is not yet treated as fully transitioned;
- the final target is customer-side operation after Week 7 follow-up work;
- detailed deployment and launch documentation remains important for final transition;
- the final source-code archive and optional prepared test accounts are intentionally deferred until Sprint 5 scope is finalized.

Customer feedback identified follow-up needs around:

- application package/name/icon identity;
- QR/refueling record semantics;
- QR entry-point discoverability;
- duplicate receipt prevention;
- visual polish and image scaling;
- final deployment and maintenance documentation;
- exportable hosted documentation.

---

## Sprint 5 — Final Maintenance, Transition, and MVP v3 Delivery

**Milestone:** To be created  
**Sprint Backlog:** To be created  
**Dates:** 13.07.2026 – 19.07.2026  
**Status:** Planned

### Sprint Goal

Complete the final MVP v3 delivery by resolving Week 6 customer-trial findings, improving product polish and reliability, finalizing customer-facing documentation, and completing the agreed product transition.

### Focus / Expected Outcome

Sprint 5 is a formal maintenance and transition Sprint. It may contain fewer large new features than earlier Sprints because its main purpose is to remove final blockers and make the delivered product usable, understandable, and maintainable after handover.

Expected outcomes:

- final visual polish and clearer product identity;
- improved QR/refueling behavior and duplicate-receipt handling;
- improved frontend-backend synchronization for remaining fixes;
- finalized customer-facing and backend deployment documentation;
- final MVP v3 SemVer release and product access artifact;
- final source-code archive after Sprint 5 scope is finalized;
- prepared test accounts only if they are still useful;
- customer-side deployment or operation guidance;
- final customer confirmation of the achieved handover level.

### Planned Follow-Up Items

- Fix or improve application package, name, and icon identity.
- Improve QR/refueling record behavior and QR entry-point discoverability.
- Prevent duplicate receipt creation by storing receipt identifiers.
- Complete remaining visual polish for vehicle and achievement assets.
- Finalize customer-facing documentation and backend deployment instructions.
- Update the root README and CHANGELOG for final delivery.
- Publish the final MVP v3 release and product access artifact.
- Prepare the complete source-code archive after the development scope is finalized.
- Decide whether prepared test accounts are still needed and share them privately if created.
- Complete the final transition meeting and record customer confirmation.

### Expected Final Transition Outcome

The intended final handover level is **deployed or operated on the customer side**.

The final outcome must be confirmed during Week 7. The team should not claim completed customer-side operation until the customer has actually confirmed or demonstrated it.

---

## Later Product Direction

After the final MVP v3 transition, possible future product work includes:

- more advanced statistics and analytics for expenses, fuel, mileage, maintenance, and trip history;
- richer trip history, route visualization, and trip-quality insights;
- broader OCR/receipt recognition beyond the implemented QR flow;
- PDF/document upload for receipts, insurance, and service documents;
- broader AI-based analysis of car history and expenses after the assistant has enough reliable context;
- voice interaction with the AI agent;
- support for several owners of one car;
- deeper achievements and gamification after the core user flows are stable;
- stronger deployment reliability, backup, monitoring, and recovery support.

Lower-priority items should remain outside the final academic MVP scope unless they are required to remove a transition blocker.

---

## Quality and Automation Roadmap

Quality and automation work should continue alongside feature development and final transition.

The team should keep improving:

- backend automated tests;
- quality requirement tests;
- testing documentation;
- CI checks for backend tests and documentation links;
- hosted documentation deployment checks;
- coverage tracking for critical modules;
- review of quality gates before merge;
- consistency between Definition of Done, testing documentation, quality requirements, ADRs, and implemented checks;
- deployment verification and recovery instructions for customer-side operation.

The goal is to make sure that final and future increments are not only functional, but also verified through repeatable tests and clear quality evidence.
