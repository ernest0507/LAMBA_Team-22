# Week 5 Sprint Review Summary

## Meeting Information

**Project:** LAMBA  
**Team:** Team 22  
**Meeting type:** Sprint Review and customer UAT session for Assignment 5 / MVP v2  
**Date:** 03.07.2026  

## Participants / Roles

- Customer / stakeholder
- Product Owner
- Scrum Master
- Backend developers
- Frontend developers

## Sprint Goal Reviewed

The Sprint Goal was to deliver the MVP v2 increment with a focus on statistics screens and selected customer feedback improvements.

The main Sprint focus was:

- statistics screens for expenses, mileage, and fuel;
- backend endpoints for statistics data;
- frontend implementation of the statistics views;
- assistant conversation history / context improvement;
- loading and operation confirmation feedback;
- photo attachment fields for maintenance and repair records;
- improved car registration input using brand/model selection with a manual fallback option.

## Delivered MVP v2 Increment Discussed

The team presented the following delivered or updated product areas:

- statistics screens with different periods, including month, half-year, and year views;
- statistics categories for expenses, mileage, and fuel;
- improved operation feedback and loading/success states;
- improved AI assistant context handling through conversation history;
- timeline/record detail improvements;
- photo attachment support for repair and maintenance records;
- brand/model selection improvements for car registration;
- architecture documentation covering static, dynamic, and deployment views;
- quality requirement tests for registration response time, AI provider failure handling, AI assistant data validation, and car data persistence.

## Addressed Customer Feedback

| Customer feedback / previous issue | Team response | Status |
|---|---|---|
| The user did not understand whether registration/login actions were processing. | Added loading and successful operation feedback. | Addressed |
| The customer wanted statistics beyond only money tracking. | Statistics were extended to include expenses, fuel/liters, and mileage. | Addressed |
| The customer requested photo support for maintenance or repair records. | Added a photo attachment field for repair and maintenance forms. | Addressed |
| The AI assistant needed better context. | Added conversation history/context loading to improve assistant responses. | Addressed |
| The car registration flow should be easier than only manual text input. | Added brand/model selection with manual input fallback if the model is missing. | Addressed |
| UI still needs visual polish. | The team identified this as a remaining product polish task. | Follow-up |
| Trip mode / mileage automation would be useful. | The team will research feasibility and simplify the feature if full GPS automation is too costly. | Follow-up |
| PDF upload and several car owners are not central to the immediate MVP. | These features were deprioritized or deferred to focus on MVP completion. | Deferred |

## UAT Results

The meeting included customer-executed UAT for old and new scenarios.

| Timecode | UAT scenario | Result | Notes |
|---|---|---|---|
| 15:36 | Registration / account flow with improved feedback | Passed with comments | The team demonstrated the updated full registration flow addressing the previous lack of feedback. |
| 17:13 | Add record through AI chat | Passed with comments | The updated flow was shown, but the customer requested more human-style assistant feedback. |
| 18:35 | View statistics screens | Passed | The customer reviewed the expense, mileage, and fuel statistics structure and understood the approach. |
| 19:20 | View history / timeline records | Passed | The customer saw fuel, mileage, and repair records in the history/timeline. |

## Architecture Evidence Discussed

The team presented three architecture views:

- **Static view:** Android client, REST API, FastAPI backend, PostgreSQL database, Alembic migrations, and external AI provider.
- **Dynamic view:** sequence flow for authentication and record creation using access tokens.
- **Deployment view:** mobile client communicating with a backend container, database, Alembic migration component, and AI provider.

The customer understood the architecture explanation and asked about token lifetime/expiration. The team marked this as a detail to clarify.

ADR details were not discussed deeply during the recorded review, so the Week 5 public report should link the ADR directory and architecture documentation separately.

## Quality Requirement and CI Evidence Discussed

The team discussed several automated quality checks and quality requirement tests:

- registration response time under moderate load, defined as 20 simultaneous registration attempts with a target response time of two seconds;
- AI provider unavailable handling, where the system should return a valid user-facing response instead of failing silently;
- validation of AI-extracted record data, including rejecting invalid negative expense values;
- database workflow validation for creating and retrieving car data without corrupting stored data.

The team should link the corresponding testing documentation, quality requirement tests, backend tests, and CI evidence in the Week 5 public report.

## Product Backlog and Scope Updates

The Sprint Review led to the following product backlog decisions:

| Scope item / feedback point | Decision | Reason |
|---|---|---|
| Statistics for expenses, mileage, and fuel | Keep / delivered in MVP v2 | High customer value and directly visible in the product. |
| AI assistant context/history | Keep / improve further | Supports more useful assistant responses. |
| Photo attachment for maintenance/repair | Keep | Requested in previous review and useful for car history. |
| Trip start/finish mode | Research and estimate | Valuable, but full GPS automation may be too costly for the current timeframe. |
| Achievements | Keep as future product direction | Customer liked the concept, but design and implementation scope must be controlled. |
| PDF file upload | Deferred | Not feasible within current time constraints. |
| Standalone AI data analysis with extra resources | Deferred | Current AI chat already provides contextual answers; extra integrations are not planned now. |
| Voice chat with AI agent | Deferred / could-have | Lower priority for MVP completion. |
| Multiple owners for one car | Deferred | Not central to finishing the MVP. |
| QR/image text recognition | Reframed as a future QR/receipt parsing story | More focused and potentially more useful than broad OCR. |

## Customer Feedback and Decisions

The customer agreed that the updated scope sounded reasonable. The customer positively reacted to the achievements concept and said it looked promising. The customer also asked the team to continue polishing the interface, including car visuals, icons, spacing, and consistency of buttons and screens.

Important customer comments:

- statistics structure was understandable;
- achievements concept looked good and should be explored;
- trip mode is useful, but a simpler mileage-start/mileage-end flow may be acceptable;
- AI assistant feedback should feel more human;
- UI consistency and visual polish remain important.

## Remaining Gaps and Risks

- The UI still needs visual polish and consistency improvements.
- Token expiration/lifetime should be clarified in the architecture/auth documentation.
- Trip mode requires feasibility research and scope control.
- Achievements need design refinement and may need scope reduction.
- PDF upload, standalone AI analysis, voice chat, and multiple car owners are deferred.
- Week 5 report must link architecture documentation, ADRs, testing evidence, hosted documentation, and CI evidence.

## Action Points

- Update the Product Backlog with follow-up items from the Sprint Review.
- Link UAT results and resulting PBIs/issues in the Week 5 public report.
- Continue polishing the UI and product visuals.
- Research trip mode implementation options and estimate effort.
- Clarify token expiration/lifetime behavior.
- Continue maintaining quality requirement tests and backend validation.
- Link architecture views, ADRs, testing documentation, CI evidence, and hosted documentation in the Week 5 report.
