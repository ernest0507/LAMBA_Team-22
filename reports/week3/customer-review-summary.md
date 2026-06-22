# Customer Meeting Summary

**Language:** English  
**Related transcript:** `reports/week2/customer-meeting-transcript.md`

**Date:** 12.06.2026   

**Publication decision:** The customer approved publishing the sanitized transcript in the repository.

## Artifacts Demonstrated

During the meeting, the team discussed and/or demonstrated the following artifacts:

- User Stories with prioritization into must-have, should-have, and could-have categories
- MVP v0 scope
- Prototype sketches and Figma-based screen designs
- Main application screen concept with AI-agent interaction
- Timeline concept for car-related events and expenses
- Expense statistics concept
- Ideas for a unique playful feature
- Possible future integration with external APIs, including maps/navigation services and vehicle diagnostics

## Discussion Points

### User Stories and MVP Scope

The team presented ten User Stories for the digital car twin application. The main must-have stories focused on storing car information, communicating with an AI agent, keeping a timeline of expenses and events, and showing spending statistics.

The customer clarified that User Stories should describe usage scenarios and asked the team to explain the current roles, the state of MVP v0, and what functionality would realistically be ready by the deadline.

The team explained that MVP v0 would focus mainly on a frontend prototype without backend integration. The backend and more complete functionality are expected to be introduced in MVP v1.

### Prototype Demonstration

The team demonstrated early prototype screens based on sketches and Figma work. The prototype included a main screen with the car model, reminders, events, statistics, and access to the AI agent.

The customer gave positive feedback on the visual direction and emphasized that the AI interaction should be highly visible and easy to access. The customer suggested reducing the number of taps needed to reach the AI chat and making the chat a more central part of the application.

The team discussed possible UI changes, including dedicating more screen space to the chat and allowing the user to swipe up into a full chat view.

### Unique Feature Selection

The team proposed several playful feature ideas, including achievements, social comparison, a complaint journal where the car “complains” about poor maintenance, simulated theft of a neglected digital car, and notifications to friends when the car breaks down.

The customer noted that the prototype already looked strong and warned that the unique feature should not interfere with building the core application. The customer recommended choosing a lightweight feature that would not require complex social functionality.

The team and customer agreed that achievements would be the most reasonable unique feature for the current MVP scope. The idea of a car birthday or anniversary summary was also discussed as a possible extension.

### External API and Extended Functionality

The team discussed possible future integrations with external APIs, including maps/navigation APIs, speed data, driving behavior analysis, and vehicle diagnostic data through OBD.

The customer considered these ideas promising but noted that they may be too complex for the current MVP. The customer recommended postponing speed and driving-style integrations until after the core MVP is completed.

The customer also suggested researching whether traffic fine data can be accessed through public services or government APIs. The team was advised to spend only a limited amount of time researching this, because such APIs may be difficult to access or require complex authorization and security documentation.

### Priority Tasks and Next Steps

The customer asked the team to define the plan for the upcoming week. The team identified the main tasks as completing MVP v0, preparing the transcript and weekly analysis, and continuing documentation work.

The customer asked the team to prepare an architecture diagram, such as an HLD-style diagram or UML diagram, showing how the mobile application, backend, AI agent, database, and other services will interact.

The team also discussed available infrastructure resources. The customer suggested using an external LLM API instead of hosting a model, because the project does not require training or running a custom model at this stage.

## Decisions

- MVP v0 will focus on the frontend prototype and simulation of core functionality.
- MVP v1 will introduce backend integration.
- The AI agent should be a central part of the user experience.
- The chat should be easier to access from the main screen.
- The application will initially support one car profile to reduce MVP complexity.
- Achievements were selected as the most realistic unique feature for the current scope.
- Complex social mechanics should be avoided for MVP v0.
- External API integrations should be researched but not prioritized before the core MVP is finished.
- Architecture documentation should be prepared for the next report.
- The customer approved publishing the sanitized transcript in the repository.

## Action Points

| Action Point | Responsible Role | Priority |
|---|---|---|
| Finish MVP v0 frontend prototype | Frontend Developers / Team Lead | High |
| Keep the AI chat visible and easy to access | Frontend Developers | High |
| Update the prototype according to customer feedback | Frontend Developers / Team Lead | High |
| Add or design the achievements feature | Team | Medium |
| Prepare the sanitized meeting transcript | Documentation Lead | High |
| Prepare the meeting summary | Documentation Lead | High |
| Prepare weekly analysis/report | Documentation Lead / Team | High |
| Prepare HLD or UML architecture diagram | Team | High |
| Research access to traffic fine APIs | Team | Medium |
| Postpone complex external integrations until after core MVP | Team | Medium |

## Risks

- The deadline for MVP v0 is very tight.
- Backend integration is not part of MVP v0 and may become a risk for MVP v1.
- Social features may be too complex for the current scope.
- External APIs may be difficult to access because of authorization, documentation, or security requirements.
- Focusing too much on experimental features may delay the core product functionality.

## Customer Feedback

The customer reacted positively to the prototype and said that it looked strong. The customer especially supported the idea of emphasizing the AI agent and making it more central to the application flow.

The customer advised the team to keep the MVP focused, avoid unnecessary multi-car or social functionality at this stage, and prioritize completing the core product experience.

## Customer Approvals

The customer approved the general MVP direction, including the frontend-first MVP v0 approach, the prioritization of AI interaction, and the lightweight achievements idea as the unique feature.

The customer approved publishing the sanitized meeting transcript in the repository.

The customer also approved continuing with the current prototype direction while making the AI chat more accessible.

## Resulting Changes

As a result of the meeting, the team should:

- Update the prototype so the AI chat is more prominent.
- Keep MVP v0 focused on one car profile.
- Treat achievements as the selected unique feature.
- Avoid complex social functionality in the first MVP.
- Prepare architecture documentation for the next report.
- Research external APIs only as a secondary task.
- Use the sanitized transcript and this summary as Week 2 meeting evidence.
