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
**Status:** Completed / MVP v2 release prepared  
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
- investigate trip mode with a realistic implementation path, possibly starting from manual start/end odometer values before GPS automation;
- continue UI polish for icons, car drawings, spacing, and button consistency;
- defer lower-priority features such as broad AI data analysis, voice interaction, PDF upload, and multi-user car ownership.

---

## Next Sprint — MVP Polish, Trip Mode, and Engagement Features

**Milestone:** To be created  
**Dates:** To be confirmed  
**Status:** Planned

### Sprint Goal

Polish MVP v2 based on Sprint 3 feedback, improve the AI assistant user experience, and prepare the next product increment around trip tracking and engagement features.

### Focus / Expected Outcome

The next Sprint should focus on making the delivered MVP v2 more useful and more pleasant to use. The team should keep the working product foundation while improving the most visible customer-facing flows and preparing a realistic next feature set.

Expected outcomes:

- a clearer and more human-like AI assistant confirmation flow;
- improved statistics and history presentation;
- a first realistic version of trip tracking, starting with manual odometer-based input if GPS automation is too large;
- a validated achievements/gamification concept connected to statistics, road situations, and maintenance history;
- continued UI consistency and product polish.

### Planned Follow-up Items

- Improve AI assistant confirmation messages and response tone.
- Continue integrating statistics screens with real backend data and improve visual clarity.
- Improve timeline/history details and record cards.
- Investigate trip mode implementation options:
  - manual start/end odometer flow as a realistic MVP option;
  - GPS-based trip tracking as a possible later extension.
- Prepare the first achievements screen or achievement model:
  - statistics-based achievements;
  - manual road-situation achievements;
  - breakdown/repair-related achievements.
- Polish the UI: app icon, car illustrations, spacing, buttons, and screen consistency.
- Continue maintaining documentation, ADRs, quality requirements, UAT scenarios, and the hosted documentation site.
- Review the application before customer-facing demos to remove old placeholders or temporary UI elements.

---

## Later Product Direction

After the next Sprint, the team plans to continue extending LAMBA toward a fuller digital garage and AI-supported ownership assistant.

Potential later roadmap items include:

- more advanced statistics and analytics for expenses, fuel, mileage, and maintenance;
- automatic trip tracking with geolocation/GPS if feasible;
- OCR or QR-based recognition of data from receipts, photos, and documents;
- PDF/document upload for receipts, insurance, and service documents;
- broader AI-based analysis of car history and expenses after the assistant has enough reliable context;
- voice interaction with the AI agent;
- support for several owners of one car;
- deeper achievements and gamification after the core user flows are stable.

Lower-priority or deferred items should remain outside the MVP scope until the core product flows are reliable and useful.

---

## Quality and Automation Roadmap

Quality and automation work should continue alongside feature development.

The team should keep improving:

- backend automated tests;
- quality requirement tests;
- testing documentation;
- CI checks for backend tests and documentation links;
- hosted documentation deployment checks;
- coverage tracking for critical modules;
- review of quality gates before merge;
- consistency between Definition of Done, testing documentation, quality requirements, ADRs, and implemented checks.

The goal is to make sure that future increments are not only functional, but also verified through repeatable tests and clear quality evidence.
