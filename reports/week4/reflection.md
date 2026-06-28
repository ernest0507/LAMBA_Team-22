# Week 3 Reflection

## Learning points

The team learned that migrating from Week 2 user stories to a Product Backlog is not only a formatting task. User stories describe user value and explain why a feature matters, while acceptance criteria define what success looks like and turn that value into explicit and testable requirements. Because of this, Product Backlog Items should be written in a way that helps the team understand both the product goal and the implementation conditions.

During backlog refinement, the team learned that a Product Backlog should stay detailed enough, estimated, emergent, and prioritized. Items that are planned for the next Sprint must be clear enough for developers to start working on them. In practice, this means that broad ideas such as “AI assistant”, “car history”, or “digital twin” need to be split into smaller backlog items and implementation tasks.

The lab on acceptance criteria helped the team understand how to write clearer pass/fail conditions for work. For MVP v1, acceptance criteria should be technically testable and can be written in Gherkin-style form: Given a precondition, When the user performs an action, Then the system produces an expected result. The customer meeting showed why this matters: the team described validation of car history input, AI-agent error handling, screen navigation, and saved car data as acceptance conditions for sprint tasks.

The team also learned that Sprint Planning should connect the Sprint Goal, selected PBIs, story-point estimates, Sprint Backlog Items, responsible people, accountable reviewers, and acceptance criteria. In the meeting, the team presented sprint tasks for storing car history, integrating the AI assistant chat, creating the digital twin, implementing navigation, and improving chat message bubbles. This helped the team understand the difference between Product Backlog Items and smaller Sprint Backlog tasks.

The customer review helped the team learn that the product flow should start earlier than the team initially planned. The customer pointed out that user registration and car registration were missing as a separate flow. As a result, the team learned that the onboarding flow should be represented in the Product Backlog and Sprint Backlog instead of being assumed implicitly.

The MVP v1 work also taught the team that delivery quality depends on consistent implementation practices. The team found that different frontend screens had inconsistent spacing, font sizes, and styles because they were implemented separately. The team decided to rebuild the interface using shared themes and styles in Android Studio, which turned visual consistency into a concrete development and quality task.

From the UML lecture, the team learned that UML is useful for visualizing and documenting software at a higher level of abstraction. This is especially relevant for the requested database UML diagram because it can show the main entities, attributes, and relationships before the backend is fully finalized.

From the documentation lecture, the team learned that documentation should support developers, users, and reviewers. README files, meeting notes, API documentation, changelogs, and architecture documents are not only formal deliverables; they make the project easier to understand, review, troubleshoot, and continue.

## Validated assumptions

The team assumed that the highest-priority user stories could be transformed into Sprint tasks. This was confirmed during Sprint Planning: the team selected tasks based on car history storage, AI-agent interaction, digital twin creation, navigation, and UI improvements.

The team assumed that acceptance criteria are necessary for both user stories and smaller implementation tasks. This was confirmed through the lab material and the customer meeting. The team discussed acceptance criteria for validation errors, successful saving of car history, AI-agent connection failures, correct AI responses, and navigation behavior.

The team assumed that the MVP v1 scope should include backend integration. This was partially confirmed. The backend was reported as mostly ready except for AI integration, but the AI part remained dependent on access to DeepSeek tokens and URL from the customer.

The team assumed that registration could be treated as part of the digital twin flow. This assumption was rejected by the customer. The customer explicitly asked the team to add registration for both the user and the car as a separate product flow or user story.

The team assumed that the first MVP v0 interface could be improved incrementally. This assumption was rejected by the team during implementation. After testing the first version, the team found that inconsistent styles and independently developed screens made the interface difficult to maintain. The team decided to rebuild the frontend with shared style definitions.

The team assumed that the AI agent could provide prediction-like functionality. This assumption remains only partially validated. The customer suggested that a simpler and more reliable first approach would be to use known statistics about commonly failing car parts and typical mileage intervals, then use this information to generate notifications or recommendations.

The team assumed that external APIs such as map/navigation APIs and OBD data could become useful future integrations. This was not rejected, but it was not confirmed for MVP v1 either. The customer accepted the idea conceptually, but the implementation details and priority remain unresolved.

The team assumed that visual explanations are important for customer communication. This was confirmed. The customer requested more visualization, including a concise summary of what was completed across frontend, backend, AI/ML, database, and other development areas.

## Friction and gaps

The first major gap is registration. The team initially focused on the car history, AI chat, and digital twin features, but the customer pointed out that the product needs a clear registration flow for both the user and the car. This needs to be added to the backlog and connected to acceptance criteria.

The second gap is AI-agent integration. The backend team reported that the backend was mostly ready except for the AI part. Work on AI integration was blocked by missing DeepSeek access credentials. The customer agreed to send the token and URL, but until that access is available, the AI-agent flow cannot be fully tested.

The third gap is uncertainty around AI prediction. DeepSeek may be able to analyze context, but it is unclear whether it can reliably predict failures. The team needs structured data sources, such as maintenance documentation, common failure statistics, or community information, before presenting prediction as a dependable feature.

The fourth gap is frontend consistency. The team discovered that splitting screens between developers without shared design rules caused inconsistent spacing, fonts, and visual style. This created rework and forced the team to rebuild parts of the frontend.

The fifth gap is release readiness. MVP v1 requires not only code completion but also clear runnable artifacts, smoke-check instructions, and evidence that the main user flow works. The team still needs to ensure that the release can be installed, opened, and tested by reviewers without hidden assumptions.

The sixth gap is workflow enforcement. The team needs to keep Product Backlog Items, Sprint Backlog Items, estimates, assigned responsible people, accountable reviewers, and acceptance criteria synchronized. Without this, sprint tasks may become unclear or difficult to review.

The seventh gap is architecture documentation. The customer asked for a database UML diagram to confirm that the team understands the database structure. The team needs to define the main entities, such as user, car, chat, messages, car history, parts, documents, and achievements, and show their relationships.

The eighth gap is communication timing. The customer noted that the current meeting schedule is not ideal because assignments arrive after the proposed Monday planning time. The team and customer discussed using Monday in-person planning and short Thursday recorded check-ins, but this workflow still needs to be tested.

## Planned response

The team will update the Product Backlog to include registration as a separate PBI or as a clearly defined part of the onboarding flow. This item should include user registration, car registration, validation rules, and acceptance criteria.

The team will refine the highest-priority MVP v1 PBIs so that each selected item has clear acceptance criteria. For the most important stories, the team will use testable Gherkin-style criteria where appropriate. This will be applied to car history storage, AI chat integration, digital twin creation, registration, and navigation.

The team will update the Sprint Backlog so that selected PBIs are broken into smaller Sprint Backlog Items. Each SBI should include a short task description, responsible person, accountable reviewer, acceptance criteria, and enough context for implementation.

The team will continue rebuilding the frontend with shared themes and styles. The goal is to avoid inconsistent screens and make the interface easier to maintain. This affects the MVP v1 frontend implementation and the prototype/interface artifacts.

The team will integrate the AI assistant after receiving the DeepSeek token and URL from the customer. If full prediction remains unreliable, the team will start with a simpler data-driven recommendation mechanism based on known failure statistics and maintenance intervals.

The team will prepare the database UML diagram requested by the customer. The diagram should focus on the main entities and relationships needed for MVP v1 and should be linked from the Week 3 report or relevant documentation.

The team will prepare a concise visual progress summary for the next customer meeting. This should show what was completed in frontend, backend, AI integration, database work, documentation, and testing.

The team will continue developing the achievements list as a lightweight product feature. The achievements should be treated as a future or secondary backlog item unless the core MVP v1 flow is stable.

The team will improve release preparation by documenting runnable artifacts, installation or execution instructions, and a repeatable smoke-check scenario. The MVP v1 report should explain what works, what is mocked, and what remains incomplete.
