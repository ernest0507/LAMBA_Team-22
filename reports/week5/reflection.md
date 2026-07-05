# Week 5 Reflection

**Project:** LAMBA  
**Team:** Team 22  
**Sprint:** Sprint 3 / Assignment 5  
**Focus:** MVP v2, architecture documentation, ADRs, quality requirements, and maintained development process

## Learning points

This week, we learned that software architecture is not only a set of diagrams. Architecture is a way to reason about the system, its components, the relations between them, and the quality attributes that matter to stakeholders. Before this assignment, we mostly thought about the product from the implementation side: screens, backend endpoints, database models, and user-facing features. After working on the architecture documentation, we understood that the structure of the system should explain why the product can be maintained, tested, changed, and operated.

For LAMBA, this was important because the product already includes several interacting parts: an Android mobile client, a FastAPI backend, a PostgreSQL database, Alembic migrations, and an external AI provider. Documenting the architecture helped us see the product as one connected system instead of separate frontend and backend tasks.

We also learned that each architecture view answers a different question:

- the static view explains what the system is made of and how components depend on each other;
- the dynamic view explains how an important runtime flow works across several components;
- the deployment view explains how the product is operated at runtime and what external dependencies exist.

The static view helped us reason about coupling, cohesion, and maintainability. For our project, this meant showing how the Android client communicates with the backend through REST API, how the backend separates authentication, record handling, database access, and AI-related responsibilities, and how PostgreSQL and the external AI provider fit into the system.

The dynamic view helped us understand that architecture is also about behavior, not only structure. We used the record creation flow with authentication and token validation because it involves several important components: the mobile client, backend endpoint, authentication logic, database operations, and response handling. This made it easier to reason about reliability, validation, and unauthorized-request handling.

The deployment view helped us think about the Android client, backend container, database, migrations, network communication, and the external AI provider as runtime elements. We learned that deployment documentation should make it clear where the main services run, how they communicate, and what should be considered when operating the product for a customer.

We also learned that important architecture decisions should be recorded, not only discussed verbally. ADRs are useful because they explain what decision was made, why it was made, what alternatives existed, and what consequences the decision has. This is important for LAMBA because future work may change the AI provider, backend boundaries, database model, deployment model, or quality strategy.

The most difficult part of MVP v2 was working with the AI agent. It was not enough to simply connect the assistant to the backend. The assistant had to understand user messages, return structured data, handle invalid input, preserve useful context, and give feedback that feels clear to the user. This was challenging because the AI agent is connected to several product areas at once: backend validation, record creation, frontend chat UI, user feedback, and quality requirements.

## Validated assumptions

We validated that the AI agent remains an important and valuable part of the product direction. The customer was satisfied with the AI agent concept and the direction of the assistant-based workflow. This confirmed that the team should continue improving the assistant, especially its validation, context handling, and user-facing feedback.

We also validated that statistics are valuable for the product. The team created a strong statistics feature for expenses and related car data. The statistics screens gave the customer a clearer view of how LAMBA can move from basic record storage toward useful analysis and insights.

Another validated assumption was that frontend-backend communication is critical for the quality of the increment. This week, communication between frontend and backend was more coordinated than before. The team was able to connect product screens, backend data, and customer-facing flows more smoothly, especially around statistics and record-related functionality.

The architecture work also validated that the system has become complex enough to require maintained documentation. The Android client, backend, database, migrations, and AI provider are no longer isolated implementation details. They need to be explained together so the team and reviewers can reason about quality, maintainability, and future change.

## Friction and gaps

The main friction was the complexity of the AI agent. The assistant touches many parts of the product at once, so testing and polishing it is harder than testing a regular screen or endpoint. It is not enough to check that the backend responds. The team also needs to check that the assistant understands the message, extracts valid data, rejects invalid input correctly, stores the right record, and gives understandable feedback to the user.

A serious issue also appeared right before the customer meeting. One of the main application features broke because a frontend change affected how the expense history was displayed. The name of the corresponding screen had been changed, and because of that the frontend displayed the wrong field in the history view. The team had to fix this urgently before the customer meeting.

This showed that small naming or UI changes can break an important flow if the flow is not checked end-to-end. It also showed that the team needs a stronger pre-review verification step before demonstrating the product to the customer.

Another gap was that some architecture and documentation work happened late in the Sprint. Some decisions were already present in the implementation, but documenting them later required the team to reconstruct the reasoning. In future work, architecture diagrams and ADRs should be updated closer to the moment when important implementation decisions are made.

## Planned response

The team plans to introduce a short pre-review smoke test checklist before each customer meeting. This checklist should be completed shortly before the Sprint Review and should cover the main customer-facing flows:

- registration and login;
- car data creation or selection;
- AI chat record creation;
- expense, fuel, and mileage history display;
- statistics screen display;
- any new MVP feature planned for demonstration;
- confirmation that screen names, navigation routes, and displayed fields match the expected data.

This process change directly addresses the last-minute expense history problem. It should reduce the risk that a main product flow breaks because of a small frontend, naming, or data-mapping change.

The team also plans to keep improving AI agent quality. This includes clearer assistant feedback, stronger validation of extracted data, better handling of invalid input, and more reliable connection between assistant responses and created records.

For architecture and documentation, the team plans to update diagrams and ADRs when important decisions are made, not only at the end of the assignment. This should make the documentation more accurate and reduce the need to reconstruct reasoning later.

The main takeaway of the week is that product quality depends on both implementation and process. Good frontend-backend communication helped the team deliver useful statistics and continue improving the AI agent, but the last-minute expense history issue showed that the team needs stronger pre-review smoke testing and earlier documentation updates.
