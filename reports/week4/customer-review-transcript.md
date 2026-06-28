# Week 4 Customer Review Transcript

**Project:** LAMBA  
**Team:** Team 22  
**Meeting type:** Customer review, Sprint Review discussion, and customer-executed UAT session  
**Date:** 26.06.2026 
**Recording link:** https://drive.google.com/file/d/1YpngS592lhOvcq16VD1R_gis7HQDLcfY/view?usp=drive_link 

## Sanitization note

This transcript was translated and cleaned from the original meeting notes. Personal information, informal side comments, and irrelevant private details were removed or generalized. The transcript preserves the meaning of the customer feedback, UAT results, Sprint Review discussion, and follow-up product decisions.

The source transcript did not include exact timestamps. If exact recording timecodes are required for Moodle, add them after the final recording link is available.

## Participants / roles

- **Customer / stakeholder** — reviewed the increment, executed UAT scenarios, and gave feedback.
- **Product Owner / Team Lead** — guided the review, explained user stories, Sprint scope, and feedback handling.
- **Backend Developer(s)** — explained backend, database, authentication, and AI integration details.
- **Frontend Developer(s)** — supported the application demo and frontend discussion.
- **Scrum Master / Documentation** — supported documentation, Sprint Review evidence, and follow-up notes.

## Transcript

### Opening and UAT explanation

**Product Owner:** For this assignment, we prepared user acceptance tests. We have four scenarios. The idea is that you read the scenario, use the app yourself, follow the intended flow, and give feedback on whether it is intuitive, understandable, or difficult. After each scenario, we record your feedback and mark whether the scenario is accepted.

**Product Owner:** At the moment, the tests are shown through the emulator because the shared build has not yet been finalized. We cannot stop the old backend before the previous grading is complete, so we are showing the current working state in this environment.

**Customer:** So I should only look at the app and try to understand what I would do as a user, not just follow the written steps?

**Product Owner:** Yes. The steps describe what we expected, but the flow should be understandable from the user perspective.

### UAT-01 — User registration [2:15-4:10]

**Product Owner:** The first user story is registration.

**Customer:** I do not have an account, so I click registration and enter the data. The app lets me input the data. Password matching is checked. Are passwords hashed?

**Backend Developer:** Yes.

**Customer:** Good. I click create account. At first, it was not completely clear whether the button worked or whether something was loading.

**Product Owner:** We have completed the registration part. What feedback do you have for this scenario?

**Customer:** The flow itself is clear: email, password, password confirmation. However, I need better feedback that the input is correct and that the registration request is being processed. For example, the button could become disabled and show that something is loading. Otherwise, it feels like I may have clicked the wrong place or nothing happened.

**Product Owner:** Should we add something like a green check or loading indication?

**Customer:** Yes, something like that. The scenario is accepted, but it needs clearer user feedback.

**Result:** Accepted with improvement requests.

### UAT-02 — Create a car digital twin [4:10-6:15]

**Product Owner:** The next scenario is creating the car digital twin.

**Customer:** I enter the car data. Some input behavior is affected by the emulator language, but numbers can be entered. I enter mileage, notes, and continue. I choose the body type and create the digital twin.

**Product Owner:** The body type does not change yet. What else should be improved?

**Customer:** It would be useful to select the car brand and model from lists instead of typing everything manually. The flow is otherwise understandable. The scenario is accepted.

**Result:** Accepted with improvement requests.

### UAT-03 — Interact with AI chat and record expenses [6:15-13:20]

**Product Owner:** The next scenario is interaction with the AI chat. The expected behavior is that the user writes a car-related expense, for example refueling or oil replacement, and the assistant saves it to the history.

**Customer:** Let us try refueling. The assistant understands that I refueled, but I also want the amount or cost to be visible. For example, it is good that it knows about ten liters, but I also want to see the cost.

**Backend Developer:** Currently, title and category are required. Cost and quantity are optional and still need improvement.

**Product Owner:** In the sidebar, the history shows the saved expense. The refueling item is visible in the history. The assistant can also answer basic questions about the car.

**Customer:** I can see that the expense is recorded, so according to the user story it technically works. However, from the user perspective, I still cannot clearly see how much I refueled or how much I spent. There should also be a confirmation message, for example “I saved your expense for this amount.” Right now it feels unfinished.

**Customer:** The date-based timeline idea is good. If I say that something happened yesterday, it should appear in the correct place in the history. That is useful.

**Product Owner:** Should this test be accepted?

**Customer:** Yes, because the user story says that the expense should be recorded, and it is recorded. But there are small usability issues. Please add clearer confirmation and make the saved expense information more visible.

**Result:** Accepted with improvement requests.

### UAT-04 — Sign in / login [13:20-14:50]

**Product Owner:** The fourth scenario is login.

**Customer:** If I remember my email and password, the app logs me in. The login works. The user story is accepted.

**Customer:** It would also be useful to add chat history in the future. I mean not only expense history, but the actual chat history.

**Result:** Accepted with improvement requests.

### Additional discussion: manual expense entry

**Product Owner:** There is also a manual “add expense” screen in the sidebar. At the moment, it is not fully connected.

**Customer:** It is still useful to see. For expenses like refueling, fuel amount and price should probably be required fields. For repair-related entries, photo attachment would be useful. Validation is also needed.

### Sprint Review discussion [14:50-19:40]

**Product Owner:** We also need to discuss the Sprint. The Sprint started at the beginning of the week and ends on Sunday. We plan another release after the Sprint. The main current tasks are AI assistant integration in chat and car history/events/expenses. We split implementation into frontend and backend parts. Tests are also required before merge.

**Customer:** The Sprint scope is fine. The AI assistant should feel more like the car itself talking to the user, not just a generic artificial intelligence that knows about the car. The history, events, and expenses direction is good.

**Customer:** The mandatory fields for refueling should be improved. Fuel amount and price are important. The car image should be improved as well. Please also make sure all important car information is pulled into the interface, because unrealistic values or placeholders affect perception.

**Customer:** Sign-out can go to the next Sprint. It should probably be in the profile, at the bottom, as a normal “Sign out” option.

**Product Owner:** Does the current Sprint direction satisfy you overall?

**Customer:** Yes. All four user stories are okay. The Sprint direction is also okay. The core is strong; now the team needs to polish the user-friendly details.

### Database and backend schema review [19:40-21:00]

**Scrum Master / Documentation:** You also asked us to show the backend/database schema.

**Backend Developer:** Here is the database schema. It includes user tables, car tables, maintenance records, fields, types, and relations.

**Customer:** This is what I wanted to see. It looks good and makes me more confident that the team is building what we agreed on. The relations are visible and the structure looks solid.

### Future work and backlog discussion [21:00-23:00]

**Customer:** Achievements or gamification can go to the next Sprint or later. It is more important now to finish the current work and polish the user experience.

**Team:** The current focus is to finish the Sprint tasks, improve the assistant flow, improve saved expense feedback, improve car information display, and handle the customer feedback from this session.

**Customer:** That is reasonable. The product foundation is strong. The remaining work is mostly in the details and user-friendly presentation.

## UAT result summary

| UAT scenario | Result | Notes |
|---|---|---|
| UAT-01 — User registration | Accepted | Needs clearer loading/success feedback and button state during submission. |
| UAT-02 — Create car digital twin | Accepted | Needs better body type support and brand/model selection lists. |
| UAT-03 — AI chat expense recording | Accepted | Expense is recorded, but saved amount/cost and confirmation feedback should be clearer. |
| UAT-04 — Sign in / login | Accepted | Login works; future improvement: add sign-out and chat history. |

## Main requested changes

- Add clearer registration feedback: loading state, disabled button, success/validation indication.
- Improve car digital twin creation: selectable brand/model lists and working body type selection.
- Improve AI expense recording: required amount/cost where appropriate, clearer saved record display, confirmation message after saving.
- Improve history screens: show relevant expense details more clearly.
- Add chat history in future work.
- Add sign-out flow in a later Sprint.
- Improve car visual placeholder and pull displayed car information from stored data.
- Keep achievements/gamification for a later Sprint while focusing on current Sprint polish and reliability.

