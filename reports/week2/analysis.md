# Week 2 Analysis

## Learning points

### User stories

During Week 2, the team learned that user stories should describe clear usage scenarios rather than only listing features. The customer clarified that the stories should help explain what the user wants to achieve and why each feature is needed.

The team also learned that user stories are more useful when they are connected to real product decisions. For example, the stories about storing car data, communicating with an AI agent, tracking the car timeline, and viewing expense statistics helped define the MVP v0 scope.

Related artifacts:

- [User stories](./user-stories.md)
- [Customer meeting transcript](./customer-meeting-transcript.md)
- [Customer meeting summary](./customer-meeting-summary.md)

### Prioritization

The team used must-have, should-have, and could-have prioritization to separate the core product from secondary ideas. The meeting showed that the most important part of the product is not the number of features, but the clarity of the main user flow.

The must-have features were confirmed as the foundation of the MVP:

- storing key vehicle information;
- communicating with the AI agent;
- showing a timeline of expenses and important events;
- showing spending statistics.

The customer also helped the team reduce MVP complexity. Multi-car profiles, social features, and advanced integrations were considered interesting, but not necessary for MVP v0.

Related user stories:

- US-01: Store vehicle data in one place
- US-02: Communicate with the AI agent
- US-03: View timeline of expenses and vehicle events
- US-04: View vehicle spending statistics
- US-07 / US-08 / US-09 / US-10: Possible future or lower-priority features

### Prototyping

The team learned that an early prototype is useful even before full backend implementation. The prototype made it easier to discuss the product with the customer and receive concrete feedback about layout, priorities, and user flow.

The customer reacted positively to the visual direction of the prototype, but also pointed out that the AI agent should be more central and easier to access. This showed that the prototype should not only demonstrate screens, but also validate whether the most important interaction is visible enough.

Related artifacts:

- [Customer meeting summary](./customer-meeting-summary.md)

### Interface design

The team learned that the interface should reduce the number of taps required to reach the AI chat. The customer suggested that the AI chat should be available directly from the main screen or be easier to open.

This feedback changed the team’s understanding of the interface. Instead of treating the AI assistant as a secondary screen, the team should treat it as one of the main interaction points of the product.

The team also learned that customization and multiple car profiles may improve the experience later, but they should not distract from the main MVP flow.

Related user stories:

- US-02: Communicate with the AI agent
- US-03: View timeline of expenses and vehicle events
- US-04: View vehicle spending statistics

### MVP v0 deployment and technical work

The team learned that MVP v0 should be a realistic foundation rather than a complete final product. Because the deadline was short, the team decided to focus on a frontend prototype that simulates the core user flow.

The meeting confirmed that this approach is acceptable for MVP v0, as long as the team clearly documents which functionality is simulated and what will be connected in MVP v1. The backend, database, AI-agent integration, and external APIs should be introduced after the basic product flow is validated.

Related artifacts:

- [Customer meeting summary](./customer-meeting-summary.md)

### Customer validation

The customer validated the general product direction and gave positive feedback on the prototype. The customer confirmed that the product should emphasize the AI assistant and that the team should keep the MVP focused.

The customer also validated the idea of adding a lightweight unique feature. Achievements were selected as the most reasonable option because they add personality to the product without requiring complex social functionality.

Related user stories and artifacts:

- US-02: Communicate with the AI agent
- US-03: View timeline of expenses and vehicle events
- US-04: View vehicle spending statistics
- [Customer meeting transcript](./customer-meeting-transcript.md)
- [Customer meeting summary](./customer-meeting-summary.md)

## Validated assumptions

| Assumption or decision | Result | Evidence / explanation | Related stories or artifacts |
|---|---|---|---|
| Users need a single place to store important vehicle information. | Confirmed | This was treated as a must-have part of the MVP scope during the user-story discussion. | US-01, [User stories](./user-stories.md) |
| The AI agent should be a central part of the application. | Confirmed | The customer explicitly recommended making the AI interaction more visible and easier to access. | US-02, [Customer meeting summary](./customer-meeting-summary.md) |
| A timeline of expenses and events is important for the core product. | Confirmed | The team and customer kept the timeline as part of the must-have MVP direction. | US-03, [User stories](./user-stories.md) |
| Expense statistics should be included in the MVP direction. | Confirmed | Spending statistics were kept as one of the main must-have features. | US-04, [User stories](./user-stories.md) |
| MVP v0 can focus on the frontend prototype without backend integration. | Confirmed | The team explained that MVP v0 would simulate functionality on the frontend, and the customer accepted this as the short-term direction. | [MVP v0 report](./mvp-v0-report.md) |
| The AI chat can be placed deeper in the interface. | Rejected / changed | The customer suggested reducing the number of taps needed to reach the chat and making it more central. | US-02, prototype artifacts |
| Multiple car profiles should be included in MVP v0. | Rejected for MVP v0 | The customer recommended keeping only one car profile at this stage to avoid unnecessary work. | US-07 / US-08, prototype artifacts |
| Social features with friends would be a good MVP feature. | Rejected for MVP v0 | The customer noted that social features would require other users and would be too complex for the current MVP. | Could-have stories, [Customer meeting summary](./customer-meeting-summary.md) |
| Achievements are a suitable unique feature. | Confirmed | The team and customer agreed that achievements are lightweight and can be integrated without blocking the core product. | Unique feature, [Customer meeting summary](./customer-meeting-summary.md) |
| External APIs such as maps, speed data, traffic fines, or OBD integrations should be implemented immediately. | Not confirmed / postponed | The customer considered these ideas promising but recommended researching them later or treating them as secondary tasks. | Future API/interface artifacts, [Customer meeting summary](./customer-meeting-summary.md) |

## Needs clarification

The following questions, assumptions, and risks remain unresolved after Week 2:

1. **AI-agent behavior and scope.**  
   The team still needs to clarify what the AI agent will actually do in MVP v1: answer questions, summarize car history, predict possible issues, analyze spending, or all of these.

2. **Data model for the digital car twin.**  
   The team needs to define which vehicle fields are required at onboarding and which can be added later. This affects the backend schema and the onboarding interface.

3. **Manual input vs. automatic data collection.**  
   It is not yet clear how much data users will enter manually and how much can be imported automatically from documents, images, APIs, or external services.

4. **Receipt and document handling.**  
   Uploading receipts and PDF documents is a should-have feature, but the team still needs to clarify whether this will be implemented in MVP v1 or later.

5. **OCR and image recognition.**  
   Automatic recognition of receipt text may be useful, but the team still needs to evaluate the technical complexity and accuracy requirements.

6. **External API availability.**  
   The team needs to research whether traffic fine data, map/navigation data, speed data, or other car-related data can be accessed through public APIs.

7. **Security and privacy constraints.**  
   The product may store sensitive vehicle and expense data. The team needs to clarify data storage, access control, and privacy requirements.

8. **Backend architecture.**  
   The customer asked for an HLD or UML-style architecture diagram. The team needs to define how the mobile app, backend, database, AI service, and possible external APIs will interact.

9. **Deployment constraints.**  
   The team needs to clarify what infrastructure will be used for MVP v1 and whether only an external LLM API is required or whether additional services are needed.

10. **Success criteria for MVP v1.**  
   The team needs to define what will count as a successful MVP v1: functional backend connection, working AI chat, persisted timeline, statistics, or a combination of these.

## Planned response

The Week 2 learning points will affect MVP v1 in the following ways:

1. **Make the AI chat more central in the interface.**  
   The team will update the prototype so that the AI agent is visible from the main screen and requires fewer taps to access. This affects US-02 and the selected prototype/interface artifacts.

2. **Keep MVP v1 focused on the core flow.**  
   MVP v1 should prioritize vehicle data storage, AI-agent interaction, timeline history, and expense statistics before adding complex secondary features. This affects US-01, US-02, US-03, and US-04.

3. **Limit MVP scope to one car profile.**  
   Multi-car support and family/group access should remain lower-priority features until the core single-car experience works reliably.

4. **Use achievements as the selected unique feature.**  
   The team will design achievements as a lightweight addition that does not require social infrastructure. This will allow the product to feel more engaging while keeping implementation realistic.

5. **Postpone complex social mechanics.**  
   Features involving friends, shared ownership, jokes, or notifications to other users should be delayed until after MVP v1 unless they can be mocked without affecting the core product.

6. **Research external APIs, but do not block MVP v1 on them.**  
   The team will spend limited time researching traffic fine APIs and possible vehicle-related integrations. If access is too difficult, these features will be documented as future work.

7. **Prepare architecture documentation.**  
   The team will create an HLD or UML diagram showing the interaction between the mobile frontend, backend, database, AI service, and possible external APIs. This should be linked from the Week 2 report and updated in future weeks.

8. **Document what is simulated in MVP v0 and what will become functional in MVP v1.**  
   The MVP v0 report should clearly explain which parts are mocked or frontend-only. MVP v1 should convert the highest-priority simulated flows into real working functionality.

9. **Update related repository artifacts.**  
   The following files should reflect the Week 2 findings:

   - [User stories](./user-stories.md)
   - [Customer meeting transcript](./customer-meeting-transcript.md)
   - [Customer meeting summary](./customer-meeting-summary.md)
   - [LLM usage report](./llm-report.md)
