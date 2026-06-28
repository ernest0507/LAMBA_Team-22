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
**Status:** In release preparation

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

The customer confirmed that the product foundation is moving in the right direction, but requested further usability and polish improvements for the next Sprint.

---

## Next Sprint — Customer Feedback Follow-up and UX Polish

**Milestone:** To be created  
**Dates:** To be confirmed  
**Status:** Planned

### Sprint Goal

Improve the user experience based on Week 4 customer feedback, reduce visible placeholders and rough UI points, and continue stabilizing the AI assistant and car digital twin flows.

### Focus / Expected Outcome

The next Sprint should focus on making the working MVP flows clearer and more user-friendly. The team should keep the existing product foundation while improving user feedback, visual polish, and customer-requested interaction details.

### Planned Follow-up Items

- Add sign-out/logout flow.
- Add clearer registration loading and success feedback.
- Improve body type selection in the car digital twin flow.
- Improve brand/model input, preferably through predefined options.
- Improve the car placeholder/image and overall car visual representation.
- Refine the AI assistant tone so it feels more like the user’s car assistant.
- Improve chat history beyond short-term assistant message persistence.
- Continue improving expense confirmation and expense history details.
- Review the application before customer-facing demos to remove old placeholders or temporary UI elements.

---

## Later Product Direction

After the next Sprint, the team plans to continue extending LAMBA toward a fuller digital garage and AI-supported ownership assistant.

Potential later roadmap items include:

- expense and maintenance statistics;
- PDF / document upload for receipts, insurance, and service documents;
- AI-based analysis of car history and expenses;
- voice interaction with the AI agent;
- support for several owners of one car;
- recognition of data from images or documents;
- achievements and gamification after the core user flows are stable.

---

## Quality and Automation Roadmap

Quality and automation work should continue alongside feature development.

The team should keep improving:

- backend automated tests;
- quality requirement tests;
- testing documentation;
- CI checks for backend tests;
- coverage tracking for critical modules;
- review of quality gates before merge;
- consistency between Definition of Done, testing documentation, and implemented checks.

The goal is to make sure that future increments are not only functional, but also verified through repeatable tests and clear quality evidence.
