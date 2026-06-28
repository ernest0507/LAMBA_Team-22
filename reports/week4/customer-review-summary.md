# Week 4 Customer Review Summary

## Meeting Information

**Date:** 26.06.2026  
**Sprint dates:** 22.06–28.06  
**Meeting type:** Customer Sprint Review and User Acceptance Testing session  
**Project:** LAMBA  
**Team:** Team 22  
**Sprint Review / UAT recording:** https://drive.google.com/file/d/1YpngS592lhOvcq16VD1R_gis7HQDLcfY/view?usp=drive_link

## Participants / Roles

- Customer / stakeholder
- Product Owner
- Scrum Master
- Backend developers
- Frontend developers

## Sprint Goal Reviewed

The main Sprint Goal was to add and demonstrate the AI-agent chat as the central product improvement for the Sprint.

Additional Sprint work included:

- expenses and events timeline;
- maintenance and repair forms;
- AI-agent interaction;
- AI chat integration;
- note type selection screen and expenses form;
- UI design for the AI chat on the home screen.

## Delivered Increment Discussed

During the review, the team demonstrated the current Sprint increment through the application running in an emulator. The customer reviewed the main flows from the perspective of a new user.

The delivered increment included:

- user registration flow;
- car digital twin creation flow;
- AI chat interaction;
- expense recording through the AI chat;
- expense/history view;
- user login flow;
- current backend/database structure for users, cars, and maintenance records.

The customer also reviewed the Sprint scope and confirmed that the main product foundation is moving in the expected direction.

## UAT Results

The team prepared four user acceptance testing scenarios and asked the customer to execute them from the user’s perspective.

| UAT scenario | Result | Customer notes |
|---|---|---|
| User registration | Accepted with comments | The flow is understandable, but the customer requested clearer loading and success feedback after pressing the create account button. |
| Car digital twin creation | Accepted with comments | The flow works, but the customer requested better body type selection and more convenient brand/model input, preferably through lists instead of only manual text input. |
| AI chat / expense recording | Accepted with comments | The feature records expenses, but the customer requested clearer confirmation after saving an expense and better display of amount/details in the history. |
| User login | Accepted | The login flow was accepted. The customer also suggested adding sign-out/logout in a future iteration. |

Overall, the customer accepted all four UAT scenarios from the user-story perspective. At the same time, the customer identified several usability improvements and polish items that should be addressed in further development.

## Quality Evidence Discussed

During the Sprint Review, the team discussed that unit tests are being added as part of the current Sprint workflow. The team explained that implementation work is being finalized together with test updates, because the current assignment requires stronger verification before merge.

CI and coverage evidence were not presented as finalized during this meeting. Unit test work and quality requirement test documentation are being prepared separately and linked in the Week 4 public report.

## Customer Feedback

The customer gave positive feedback on the general product direction and confirmed that the product foundation is strong. The customer especially noted that the backend/database structure gives confidence that the team is building the agreed product direction.

The customer also requested several improvements:

- add clearer loading and success feedback during registration;
- make the car digital twin creation flow more user-friendly;
- improve body type selection;
- improve brand/model input, preferably through predefined lists;
- show clearer confirmation after the AI chat saves an expense;
- display expense amount and details more clearly in the history/timeline;
- add chat history;
- add sign-out/logout flow;
- improve the car placeholder image;
- make the AI assistant feel more like the user’s car assistant rather than a generic AI assistant;
- keep achievements for a later Sprint;
- continue polishing the user-friendly interface layer over the existing product foundation.

## Approvals and Requested Changes

The customer approved the tested flows as acceptable for the current Sprint state. The customer confirmed that all four user acceptance scenarios can be accepted, but emphasized that the product still needs usability improvements.

The customer’s overall evaluation was positive: the core structure works, the main product direction is correct, and the team should now focus on polishing details and improving the user experience.

Requested changes are mainly related to usability, clarity, and product polish rather than a rejection of the demonstrated Sprint increment.

## Resulting Product Backlog Updates

The team linked the accepted customer feedback with existing Product Backlog and Sprint Backlog items. Some feedback was already covered by Week 4 implementation work, while several usability polish items were explicitly deferred to the next Sprint.

| Customer feedback / follow-up area | Related backlog item | Status |
|---|---|---|
| Account creation and sign-in | US-11: Create account and sign in #61 | Done |
| Registration behavior verification | PBI: automated Test - check registration response time #132 | Done |
| Car digital twin and car data foundation | US-01: Storing car's data #31; PBI: Implement car data entry flow #50 | Done |
| Digital twin customization | US-07: Digital twin customizing #37 | Done |
| AI-agent chat interaction | US-02: Interact with AI-agent #32; PBI: Integrating the chat with AI-assistant #49 | In Progress |
| AI assistant backend, extraction, endpoint, and validation | PBIs #121, #123, #134, #136 | Done |
| AI chat UI | PBI: Implemented the UI design for the AI chat on the home screen #138 | Done |
| Expenses and events timeline | US-03: Main expenses and events timeline #33 | In Progress |
| Expense form and note type selection | PBI: Implement note type selection screen and expenses form #118 | Done |
| Maintenance and repair forms | PBI: Implement maintenance and repair forms #116 | Done |
| Short-term assistant message persistence / chat history foundation | PBI: Persist last 3 assistant messages #52 | Todo |

The following customer-requested polish items were intentionally moved to the next Sprint: sign-out/logout flow, clearer registration loading/success UI feedback, more specific body type and brand/model selection improvements, car placeholder/image improvement, and AI assistant tone/personality refinement so that it feels more like the user's car assistant.

## Risks and Follow-up Questions

The main remaining risks are:

- some product flows work but still need clearer user feedback;
- AI expense recording requires clearer confirmation and better history display;
- unit test evidence is still being finalized by the development team;
- CI and coverage evidence were not finalized at the moment of the review;
- several customer-requested polish improvements are intentionally deferred to the next Sprint and should be tracked there.

## Action Points

- Keep the Product Backlog aligned with the customer’s requested improvements.
- Track the deferred usability polish items in the next Sprint planning.
- Continue work on unit tests and testing evidence.
- Add final unit test evidence after the development team finalizes it.
- Improve user-facing feedback in registration and expense-saving flows in the next Sprint.
- Continue planning sign-out/logout and chat history improvements.
- Link release and runnable artifact evidence after the Week 4 release is created.
