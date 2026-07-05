# Week 5 Sprint Review Transcript

**Project:** LAMBA  
**Team:** Team 22  
**Meeting type:** Sprint Review and customer UAT session for Assignment 5 / MVP v2  
**Date:** 03.07.2026  
**Publication note:** This is a sanitized English transcript intended for the public repository if publication permission is confirmed. Customer-identifying details are omitted.  
**Recording note:** The original meeting recording was interrupted and restarted. This transcript combines both recording parts in chronological order. Timecodes below refer to the combined recording / timestamped raw transcript.

## Participants / Roles

- Customer / stakeholder
- Product Owner
- Scrum Master
- Backend developers
- Frontend developers


## Recording Timecode Index

| Timecode | Main moment |
|---|---|
| 00:00 | Sprint Review opening and meeting plan |
| 00:35 | Sprint Goal and Sprint scope: statistics screens and feedback improvements |
| 01:36 | Addressed feedback from the previous review |
| 02:33 | Architecture documentation discussion begins |
| 02:35 | Static view: client, backend, database, AI provider |
| 04:23 | Dynamic view: authentication and record creation sequence |
| 05:50 | Deployment view: mobile client, backend container, database, migrations, AI provider |
| 06:53 | Quality requirements and automated test evidence |
| 09:22 | Remaining work, future product scope, and trip mode discussion |
| 10:39 | Additional feedback and UI polish discussion |
| 11:13 | Product scope decisions and deferred items |
| 12:47 | Achievements concept discussion |
| 15:36 | UAT: registration/account flow with improved feedback |
| 17:13 | UAT: adding a record through AI chat |
| 18:35 | UAT: statistics screens |
| 19:20 | UAT: timeline/history records |
| 19:43 | UAT closing |

## Transcript

### [00:00] Sprint Review opening and Sprint scope

**Customer:** I am ready, let us begin.

**Product Owner:** Today we will review the Sprint for this week. We will discuss the feedback you gave us, how we analyzed it, and what we did with it. Then we will go through user acceptance testing. We need to show two new UAT scenarios and two older scenarios: one that was not accepted before and was fixed, and one that was updated based on your feedback. After that, we will show what is still not done and discuss next steps.

The Sprint started at the beginning of the week and ends at the end of the week. The main focus this week was adding statistics screens. We split this into backend endpoints for the statistics screen and frontend layout for the statistics screens. Frontend developers worked on the frontend part.

Additional Sprint work included:
- saving assistant conversation history so the assistant can understand context better;
- adding loading and operation confirmation screens;
- adding a field for attaching photos to repair or maintenance records;
- adding a brand/model list in the car registration screen, with the option to enter a model manually if it is missing.

Most of the Sprint scope is closed. The statistics part is also closed, but some status was updated manually.

### [01:36] Addressed feedback from the previous review

**Product Owner:** Regarding your feedback: you asked us to add loading screens and successful operation feedback, and we implemented that. We also addressed statistics tracking. Initially we planned to track only expenses by categories, but now the statistics include money, liters, and mileage. We also added a photo attachment field.

**Customer:** So it includes expenses, money, and liters. Okay.

**Product Owner:** Yes. We split the statistics into several views. We also added photo attachment support. Last time during UAT this was not present. We also improved timeline details and made the AI assistant smarter by adding more context loading. Testing work is still being handled by the team, but we can move on to architecture.

### [02:33] Architecture documentation review

**Customer:** Let us start with architecture.

**Product Owner:** We were asked to prepare three architecture views: static view, dynamic view, and deployment view.

For the static view, we represented the project by layers and components. The flow starts with the user. The user accesses the system through the client, which is an Android mobile application built with Kotlin and Jetpack Compose. The client communicates with the backend through a REST API. The backend runs on FastAPI and delegates tasks such as authentication, database operations, and record creation.

The database layer uses PostgreSQL. CRUD operations interact with Postgres, and Alembic is used for migrations to keep database versions synchronized. As an external component, we use an AI provider; currently, we connect DeepSeek as the AI provider.

For the dynamic view, we prepared a sequence diagram for adding a user record. The driver first logs in. The request goes through an endpoint to authentication. Authentication checks the user through CRUD operations and returns whether the credentials are valid. If valid, the system returns an access token. The access token is then used to create a new record. Each time the system checks whether the token is valid.

**Customer:** That is clear. Does the token have a lifetime?

**Product Owner:** We need to clarify that with the backend team.

**Customer:** Do tokens expire? It is not a problem, just interesting. Okay.

**Product Owner:** The diagram has two branches: valid token and invalid token. If the token is valid, the record data is written to the database and the user receives a successful response. If access is denied, the user receives a 401 Unauthorized error.

For the deployment view, we showed how the system is run. The mobile client communicates through HTTP REST with the backend container running FastAPI. Alembic is separated because FastAPI handles endpoints and request processing, while Alembic handles migrations. The backend communicates with the AI provider through HTTPS and with the database through a separate database port.

### [06:53] Quality requirements and automated test evidence

**Product Owner:** We also discussed quality requirements. Last week we defined quality requirements. The first one is registration response time. The scenario is that the app should respond quickly under moderate load. For us, moderate load means 20 users trying to register at the same time. The app should respond to all of them within two seconds. We wrote a test for this, and it is already used.

The second quality requirement is AI provider failure handling. If there is no connection to the AI provider, the system should return a valid response. We wrote a test for this by using a placeholder incorrect provider address and checking that the app returns a standard message saying that it cannot complete the action.

The third quality requirement is validation of record data extracted by the AI assistant. For example, if a user says they refueled for negative 100 rubles, the AI may parse this into JSON, but our backend validates whether the values are valid. We do not allow the AI to decide everything. If the parsed data is invalid, we do not save it and return a clarification/error response.

**Customer:** Do you mean that the AI could return a value like negative 100 rubles, and you check it?

**Product Owner:** Yes. The user could enter invalid data, such as "refueled for minus 100 rubles." We check that the parsed data is valid. If it is not valid, we do not save the record and return a response that something is wrong. This test is also used.

We also worked on database-related tests. The workflow is that the user creates car data, registers it, and then the system requests the data back to check that it was stored in a valid form and was not corrupted.

### [09:22] Work not completed and future product scope

**Product Owner:** We also reviewed what is not done yet. We added internet permission to the package. We researched external APIs. There is a lot of information available, including fuel type. We also looked at trip mode.

**Backend Developer:** There is a possibility to use GPS points on a map and track the number of kilometers driven. It is free, so it may be possible to use.

**Customer:** For trip mode, because of limited time, it would already be useful to have a simpler flow: start trip with one mileage value, finish trip with another mileage value, and optionally send geolocation.

**Backend Developer:** The idea is to update mileage automatically.

**Customer:** That would be great.

**Backend Developer:** We will try to do that, but we have not started yet.

**Customer:** Research it and estimate the time. If it is too expensive, simplify it.

**Product Owner:** Regarding remaining feedback and fixes, we should also handle cosmetic polish: redraw car images, maybe add an application icon, and make the UI more consistent. Some parts of the frontend moved away from the initial unified style, so some paddings and buttons no longer match the intended design.

**Customer:** Polish it.

**Product Owner:** Yes. We also discussed user stories. We plan to keep the completed statistics-related work, but remove PDF file upload from the current scope because it is not realistic within our time. AI data analysis as a separate feature is also not planned as an independent task right now because DeepSeek already answers context-based questions in the chat. Voice chat remains a could-have item with lower priority.

Automatic text recognition from images was moved into a new user story, where the user photographs a QR code and the system parses information through open APIs. Several users for one car is also lower priority because we need to finish the MVP first. Start/finish trip mode remains planned.

**Customer:** That sounds reasonable. I agree.

### [12:47] Achievements concept discussion

**Product Owner:** Documentation-lead prepared the achievements concept. It has three cases: achievements connected to statistics, achievements connected to road situations where the user manually enters or selects something, and achievements connected to breakdowns and repairs.

**Customer:** So statistics-based achievements would be based on data I enter manually? For example, a "fuel eater" achievement if the user entered more than 15 liters?

**Product Owner:** The system tracks that the user refueled a lot and can unlock an achievement based on the statistics.

**Customer:** I understand. That can be automatic. Road situation achievements are not automatic. "Snow King" sounds good. Breakdowns and repairs also sound good. For the user, there will be no difference between the achievement types, right?

**Product Owner:** Yes, this is mostly an implementation detail. One idea is that the user opens a screen with all achievements. Some achievements can be manually selected, while statistics-based achievements stay closed until the user actually performs the required action. That could make it more engaging.

**Customer:** Okay, great. It looks good. I would like to see it soon.

**Product Owner:** Achievements still need design work. We may cut the scope and keep the most relevant part. We also need to test both automatic and manual achievement logic.

### [15:36] UAT: registration flow

**Product Owner:** Now we can move to user acceptance testing. The first UAT is the one from the previous Friday meeting: logging into or creating an account. The feedback was that after pressing the continue button the app did not clearly show that anything was happening. The user could keep pressing the button and did not know whether the application was processing the request. Now we propose going through the full registration flow.

**Customer:** How should I do it?

**Product Owner:** Since this is local, it does not matter which language is used.

The team walked through the registration flow and checked that the previous feedback about loading/operation feedback was addressed.

### [17:13] UAT: adding a record through AI chat

**Product Owner:** The next test is adding a record through the AI chat. Previously, the test failed when the user wrote a fuel entry such as "refueled for 10 liters" and the chat did not react to it. It also struggled with non-trivial messages that did not match a template. Let us try it in Russian.

**Customer:** Can we try again? Here?

**Product Owner:** Yes, copy and paste it there.

**Customer:** Okay, let us try. I would still like more human-style feedback from the assistant.

The team demonstrated the updated AI chat record flow. The customer understood the flow but requested more human and user-friendly assistant feedback.

### [18:35] UAT: statistics screens

**Product Owner:** The next UAT is viewing expense statistics. The current implementation is based on mocked or prepared frontend data. The statistics are split by month, six months, and year. Initially we thought about separate screens for fuel, kilometers, and oil, but now we split the statistics into expenses, mileage, and fuel.

**Customer:** I understand.

The customer reviewed the statistics screens and understood the current structure.

### [19:20] UAT: timeline and records

**Product Owner:** The final test is about records and rated events with the car. Let us look at the history. We have records here.

**Customer:** I see the fuel that I recorded last time, mileage, and repair.

**Product Owner:** That is all for the UAT.


