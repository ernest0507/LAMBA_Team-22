# Week 5 Sprint Retrospective

**Project:** LAMBA  
**Team:** Team 22  
**Sprint:** Sprint 3 / Assignment 5  
**Focus:** MVP v2 delivery, architecture documentation, quality evidence, and customer feedback response

## What went well

The team delivered a strong statistics feature for expenses and related car data. This was one of the most visible improvements of MVP v2 and helped show the customer that the product is moving beyond basic data entry toward useful analysis.

Communication between frontend and backend was more coordinated than in previous work. The team was able to connect product screens, backend logic, and customer-facing flows more smoothly, especially around statistics and record-related functionality.

The customer was satisfied with the direction of the AI agent. Even though the assistant still needs polishing, the review confirmed that the AI agent is valuable for the product and should remain an important part of the roadmap.

The team also improved the way it explains the product technically. The architecture discussion covered the static view, dynamic view, and deployment view, which helped explain how the Android client, FastAPI backend, PostgreSQL database, Alembic migrations, and external AI provider work together.

## What did not go well

The most difficult part of the Sprint was working with the AI agent. The assistant affects many parts of the product at once: backend validation, record extraction, chat UI, user feedback, and persistence of context. Because of this, it is harder to test and polish than a regular screen or endpoint.

A serious issue appeared right before the customer meeting. One of the main application features broke because a frontend change affected how the expense history was displayed. The name of the corresponding screen had been changed, and because of that the frontend displayed the wrong field in the history view. The team had to fix this urgently before the customer meeting.

This showed that small frontend changes can break an important user flow if the team does not run an end-to-end check before the review. The issue was fixed, but it created unnecessary stress and risk before the customer-facing session.

Some architecture and documentation work also happened late in the Sprint. The team was able to prepare the required artifacts, but the process would be more reliable if architecture diagrams and ADRs were updated closer to the implementation work.

## What the team changed or attempted to change based on the previous Sprint Retrospective, and what results they observed

After the previous Sprint Retrospective, the team tried to improve frontend-backend coordination and pay more attention to customer-facing polish before the review. This helped during Week 5: the statistics feature was easier to connect and demonstrate, and the team was able to explain the delivered MVP v2 increment more clearly.

The team also improved how it prepared review evidence. Compared with the previous Sprint, the Week 5 Sprint Review included a stronger explanation of architecture, quality requirements, and follow-up backlog decisions. This made the review more structured and helped connect product features with maintainability and quality evidence.

However, the team did not fully solve the problem of last-minute demo risk. The expense history display problem happened shortly before the customer meeting, which means the previous process improvement was not enough. The team needs a more explicit smoke test checklist that is executed immediately before the review.

## Action points

| Action point | Owner area | Expected result |
|---|---|---|
| Add a pre-review smoke test checklist before every customer meeting. | Scrum Master / Team | Main demo flows are checked before Sprint Review. |
| Add an explicit check for screen names, navigation routes, and displayed fields. | Frontend | UI changes do not accidentally break history or record display. |
| Continue improving AI agent validation and user feedback. | Backend + Frontend | The assistant gives clearer and more reliable responses. |
| Keep frontend-backend communication focused on shared data contracts. | Frontend + Backend | Statistics and history screens display the expected data. |
| Update architecture diagrams and ADRs closer to implementation changes. | Whole team | Architecture documentation stays accurate and easier to maintain. |

The main process improvement for the next Sprint is the pre-review smoke test checklist. It directly addresses the regression that happened before the customer meeting and should reduce the chance that a main product flow breaks because of a small frontend, navigation, or field-mapping change.
