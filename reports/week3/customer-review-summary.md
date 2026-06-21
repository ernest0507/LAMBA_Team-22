# Customer Meeting Summary

**Language:** English  
**Related transcript:** `customer-meeting-transcript.md`

## Meeting Information

**Date:** 19.06.2026  
**Format:** Recorded customer meeting  
**Participants:**

- **Customer** — project customer / mentor
- **Team Lead** — presented sprint tasks, architecture ideas, MVP v0 progress, and prototype changes
- **Backend Developer A** — discussed MVP v0 and schedule availability
- **Backend Developer B** — discussed backend readiness and possible data sources
- **Frontend Developers** — mentioned as responsible for frontend design and MVP v0 implementation, but not personally identified in the sanitized transcript

## Privacy and Publication Decision

The transcript was sanitized before sharing. Real names and personally identifying details were replaced with roles. Individual phrase timestamps were removed and replaced with topic-level timestamp ranges.

## Artifacts Demonstrated

During the meeting, the team discussed and/or demonstrated the following artifacts:

- Sprint backlog and selected sprint tasks
- User Stories and acceptance criteria
- MVP v0 frontend skeleton / APK
- Redesigned prototype screens
- HLD-style architecture sketch
- Planned backend API structure
- AI-agent integration concept using DeepSeek
- Possible external integrations, including map/navigation APIs and OBD data
- Planned database UML diagram
- Planned achievements list

## Discussion Points

### Sprint backlog and acceptance criteria

The team presented the sprint tasks selected for the week. These tasks were based on previously defined User Stories and included storing car history, integrating the AI assistant chat, creating the digital twin during registration, implementing screen navigation, and improving frontend chat message bubbles.

The customer recommended adding registration as a separate User Story or task because the product flow should begin with the user entering their own data and the car data before using the rest of the system.

### Architecture and AI integration

The team presented an architecture sketch with the user, mobile application, backend API, database, and AI agent. The mobile application is planned to use Kotlin and Jetpack Compose, while the backend is planned in Python.

The team discussed a possible DeepSeek-based AI-agent workflow. The AI agent may classify user chat messages, determine intent, and send structured commands to the backend using tool calls or function calling with JSON arguments.

The customer agreed that this direction looked reasonable and promised to provide the DeepSeek token and URL.

### MVP v0 and prototype review

The team demonstrated the current MVP v0 skeleton and explained that it did not include registration yet. The team also explained that the previous implementation had inconsistent styles because different screens were developed separately.

The team decided to rebuild the frontend using shared themes and styles in Android Studio so that colors, spacing, and font sizes are consistent across the application.

The customer supported the decision to roll back and rebuild the design, noting that it is good that the team is not afraid to redo work when the current implementation does not meet the expected quality.

### Backend status

The backend was reported as mostly ready except for the AI integration. The team had not yet started working with the AI service because the token and access URL had not been provided yet.

### AI prediction and data sources

The team discussed whether DeepSeek can predict car failures. The customer suggested a simpler approach: using known statistics about commonly failing parts and typical mileage intervals, then using that information to generate notifications or recommendations.

Possible data sources include online forums, community knowledge, and official maintenance documentation.

### Meeting process and future planning

The customer proposed holding in-person planning meetings on Mondays and short recorded online check-ins on Thursdays. The purpose of the Monday meeting is to plan the week, while the Thursday meeting can provide recorded evidence and transcript material for assignment submission.

The team and customer agreed to meet in person on Monday at 3 p.m.

### Additional requests

The customer requested more visual reporting of progress, such as a short summary slide showing completed work across backend, frontend, AI/ML, and other development areas.

The customer also asked the team to prepare a UML database diagram and to begin developing a list of possible achievements for the application.

## Decisions

- Add registration as a separate product flow or User Story.
- Continue rebuilding the frontend using shared themes and styles.
- Keep the AI chat central in the product interface.
- Use a backend API with a database and AI-agent integration.
- Treat DeepSeek integration as a planned backend task once access is provided.
- Use a simpler data-driven approach for failure predictions instead of relying only on LLM prediction.
- Prepare a database UML diagram.
- Prepare a list of potential achievements.
- Move weekly planning toward Monday in-person meetings, with possible short recorded Thursday check-ins.

## Action Points

| Action Point | Responsible Role | Priority |
|---|---|---|
| Add registration as a separate User Story or sprint task | Team Lead / Team | High |
| Finish the redesigned MVP v0 frontend | Frontend Developers / Team Lead | High |
| Connect backend functionality by the end of the week | Backend Developers | High |
| Integrate DeepSeek after access is provided | Backend Developers | High |
| Send DeepSeek token and URL | Customer | High |
| Prepare a database UML diagram | Backend Developers / Team | High |
| Prepare a visual progress summary | Team | Medium |
| Develop a list of potential achievements | Team | Medium |
| Continue improving design consistency through shared styles | Frontend Developers | High |
| Hold the next in-person planning meeting on Monday at 3 p.m. | Team / Customer | Medium |

## Risks

- Backend and AI integration may be delayed until API access is provided.
- The frontend rebuild may take additional time because the team decided to roll back the previous implementation.
- The AI agent may not be able to reliably predict failures without structured external data.
- External data sources such as forums, maintenance statistics, map APIs, or OBD data require additional research and validation.
- Without shared design styles, the application may become visually inconsistent.
- The team needs to make progress visible through clearer artifacts and visual summaries.

## Customer Feedback

The customer gave positive feedback on the architecture direction and the redesigned prototype. The customer also supported the decision to rebuild the frontend instead of continuing with inconsistent screens.

The customer emphasized the importance of visual communication, database architecture understanding, and a clear list of achievements as part of the product direction.

## Customer Approvals

The customer approved the sprint direction, the architecture approach, the frontend rebuild, and the plan to continue toward MVP v1 with backend and AI integration.

## Resulting Changes

As a result of the meeting, the team should:

- Add registration to the product flow.
- Continue rebuilding the frontend with shared themes and styles.
- Prepare the database UML diagram.
- Prepare a visual summary of completed development work.
- Start designing the achievements list.
- Integrate DeepSeek once the customer provides access.
- Use Monday meetings for planning and Thursday recorded calls for short check-ins if needed.
