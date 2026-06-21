# Customer Meeting Transcript

**Language:** English  

## Opening, Week Context, and MVP v0 Status

0:03-1:24

**Team Lead:** One second, I will open it. I will connect to Wi-Fi first. This week we discussed UML diagrams in the lecture: how to create them and how to visualize a project. We also discussed the backlog, how to build it, and what goes into a sprint. Now we have prepared what needs to be done for the sprint. I will deal with the Wi-Fi.

**Customer:** What can you show by Sunday? I remember you sent something to the conference chat.

**Team Lead:** We did a little, but we decided to redo it.

**Customer:** Okay.

**Team Lead:** Yes, overall, the first version did not really work well for us.

**Customer:** You had to make your MVP v0, right?

**Team Lead:** Yes, exactly. We made v0.

**Backend Developer A:** Yes, it is basically the frontend. We improved it during this week. By Sunday, we will also connect the backend and some functions.

## Sprint Backlog, User Stories, and Acceptance Criteria

1:25-6:42

**Team Lead:** Great, it works now. So, our sprint for this week includes the tasks that we need to implement from Monday to Sunday. We choose the tasks ourselves, and we also set the priorities ourselves.

The first task is related to storing information. This comes from the User Story where the user can store information about the car in one place, and based on this information we can later predict possible failures.

For each task, we wrote acceptance criteria. We need at least three criteria to consider the task accepted. Should we read them out, or just explain them generally?

**Customer:** Just go through them briefly. There is probably no need to go into all the details.

**Team Lead:** The user opens the history screen, adds information, and the information appears there. If the user enters something incorrectly, validation fails. We explain that the data cannot be saved because it was entered incorrectly. Also, the user can edit the saved history.

That is about storing the car history.

Next, as a car user, I want to be able to receive information through the agent and ask it questions about the car. The acceptance criteria are similar: I ask a question, and if there is no connection to the AI agent, we show an error. Another criterion is that the AI agent correctly responds to the request. Overall, the other criteria are written in the same style.

This is about the User Stories. We also had an additional task to add five tasks on top of the User Stories, breaking larger stories into subtasks.

One of these tasks is integrating the chat with the AI assistant into our application. This is a backend task. The question is whether we will get access to the neural network this week.

**Customer:** To the neural network.

**Team Lead:** Yes.

**Customer:** Yes, I will send access to it.

**Team Lead:** Great. For this task, the checks are mostly the same: whether it responds, whether it connects, and similar points.

Then we have the creation of the digital twin. When the user registers in the application, they fill in all the data: name, brand, model, year, mileage, and so on. The criteria are that the data is entered correctly, saved, and displayed correctly. This is a frontend task.

Then we are also doing screen navigation. For example, the user taps a button and moves to the correct screen, then taps back and returns to the previous screen. The criteria are written in the same way.

We are also adding message bubbles by changing the shape of the messages in the chat. They will not be rectangles anymore; they will be a little rounded.

**Customer:** Is this a task?

**Team Lead:** Yes.

**Customer:** For the frontend?

**Team Lead:** Yes, for the frontend. So, for now we have six tasks. To be fair, with the sixth task, I agree that it is small.

**Customer:** I have a question about the first User Story. Can you open it again?

**Team Lead:** Yes.

**Customer:** I am not sure. I also want to see registration. What about registration?

**Team Lead:** Car users?

**Customer:** Both users and cars. This can already be interpreted as a separate User Story: the user enters the application, enters their personal data, enters car data, and so on. It feels like this should be considered first. You immediately dive into the rest of the functionality, which is probably also correct, but registration is missing.

**Team Lead:** Yes, understood. Then we will add registration to this. First registration, and then the rest.

**Customer:** Yes, exactly.

**Team Lead:** So, that is what we have for the current sprint tasks.

**Customer:** Overall, good. This is a solid direction.

## Architecture, AI Agent, and External Integrations

6:42-9:07

**Team Lead:** We had a question about access to the AI. We also researched it. You also asked us to prepare an HLD diagram or something similar.

**Customer:** Yes, architecture, roughly speaking.

**Team Lead:** I decided to sketch it.

**Customer:** May I look?

**Team Lead:** Yes.

**Customer:** Are these all diagrams?

**Team Lead:** No, this is only one part.

**Customer:** I got scared for a second.

**Team Lead:** So, here is how it works. We have the user, then the mobile application built with Kotlin and Jetpack Compose. Then we have the backend API. The backend team is writing it in Python. Authentication is also included here. Then this is connected to the databases.

As for the AI agent, I looked at DeepSeek. For example, the user asks through the chat to enter refueling data, such as spending a certain amount of money on fuel. The request goes to DeepSeek. DeepSeek recognizes the pattern and determines the message type. Then it creates something like tool calls or function calling. It sends a command to the backend, and the arguments contain the necessary data in JSON format. This is how we can implement it.

This part is still under question because it is not directly part of the MVP.

I also checked that Yandex really provides an API. It can report the current speed and speed limits. The question is how it connects. It seems that it does not connect directly to the backend; instead, it has its own library that is integrated directly into the application.

**Customer:** That is what I understood too. Then the application can send something to the backend if needed.

**Team Lead:** And with the OBD system, if the car has OBD, we can receive the required information. Communication happens through OBD codes: we send codes, and the system recognizes them and returns the data.

**Customer:** Yes, exactly.

**Team Lead:** For now, this is how we see it.

**Customer:** It looks good. Really good.

## MVP v0 and Prototype Demonstration

9:07-15:56

**Customer:** Now show me something.

**Team Lead:** This is what we sent earlier. This is our first version that we showed before. Backend Developer B, do you still have the APK file? This version does not include registration yet; it only includes digital twin creation.

This is the old approved design. For the second version, we agreed to split the screen so that about 30% goes to the car card, and the rest goes to the chat. This is just an example of how the interaction works.

There are template questions so that it is easier for the user to ask something quickly without typing everything manually.

Overall, this is what we have. But after we built it, we realized that we did not like it. The mistake was that we split the screens between different people, and when we started implementing them, the spacing, sizes, and fonts were inconsistent. We did not communicate enough with each other while building them. As a result, one screen had slightly larger spacing than another screen, and there was no single visual style.

So now we have started rebuilding everything. In Kotlin, in Android Studio, there are special folders for themes and styles. We started defining a common style there: colors, spacing, and other shared design values, so that everything is general and consistent. This way we do not have to guess the font size every time. The previous version became messy.

Can you show it? Backend Developer B has the APK file.

**Backend Developer B:** The old one?

**Team Lead:** Yes, the old one.

**Backend Developer A:** Do you have images of the current version?

**Team Lead:** Of the new one? Yes, I also have those.

**Customer:** This is only a skeleton for now, right?

**Team Lead:** Yes.

**Customer:** I was worried I might send some requests and everything would break.

**Team Lead:** Everything there is ready to crash.

**Customer:** No, it is fine. It works as a concept.

**Team Lead:** After we started redrawing everything, we made several sketches. Frontend Developer A made some sketches, and I also drew some. We put everything into an AI tool and tried to get something more useful from it.

For the MVP, we currently imagine the design like this. These are examples of how it may look. We divide the screen roughly so that 30% is the car card and the rest is interaction with the AI. From here, the user can swipe up and move fully into the chat.

There are also statistics, a full chat screen, documents that can be added, history, and a sidebar on the side. The sidebar is for cases when the user does not want to ask through the chat and instead wants to manually open history, statistics, or other sections.

**Customer:** Yes, it looks good.

**Team Lead:** Are the colors okay overall?

**Customer:** That is up to you.

**Team Lead:** Do they fit the theme of an automotive application?

**Customer:** In that sense, yes. Honestly, I cannot say for sure whether it perfectly fits an automotive application. Personally, I like it. If we wanted to evaluate it seriously, we would need some analysis, but that may already be too much detail. In my opinion, it looks good.

**Team Lead:** We are planning to finish this by the end of the week. We are rebuilding it ourselves. The rest of the main tasks are already in the sprint that we showed.

**Customer:** So by the end of the week, by Sunday, there will be a new application and the backend will already be connected?

**Team Lead:** Yes, that is the plan.

**Customer:** Sounds good. What is ready on the backend side?

**Backend Developer B:** Everything except the AI part, roughly speaking.

**Customer:** I will provide that. I will probably generate it today and send the tokens.

**Backend Developer B:** We have not started doing anything with it yet.

**Customer:** Yes, okay. That is normal. It looks good. Great.

**Team Lead:** We are a little stressed because we rolled back.

**Customer:** That is okay. On the contrary, it is good. I do not mean that the previous version was bad. It is good that you are not afraid to roll back when needed. I think that is the right approach. Keep going.

## AI Prediction Capabilities and Data Sources

15:56-17:14

**Team Lead:** I also looked into whether DeepSeek can actually predict anything. I still do not fully understand it, but it analyzes context well and can suggest something based on that context. So for prediction, we are not sure yet, but it can make context-based suggestions. For example, if the user enters car-related data, it should be able to analyze it. It may also be able to forecast or plan the budget.

**Customer:** The simplest thing DeepSeek could do is create a list of the most commonly failing parts, roughly estimate at what mileage they tend to fail, and then we can hardcode this information and send notifications when needed.

**Team Lead:** How would DeepSeek understand at what mileage something will fail?

**Customer:** There is mileage statistics and general failure statistics. These are available data.

**Backend Developer B:** There are websites and forums. For example, for a specific Volkswagen Tiguan engine, people may write that at around 120,000 kilometers something commonly breaks.

**Customer:** Exactly. There are community sources, such as forums, and official documentation also says how often something should be replaced. So I think this can be done.

**Team Lead:** Yes, understood. Overall, that makes sense.

## Meeting Format and Weekly Planning

17:18-21:15

**Customer:** Overall, good. I understand that the meeting is probably scheduled at an inconvenient time. You submit on Sunday, and the task arrives on Tuesday or something like that. I can suggest meeting on Mondays to define the plan.

**Team Lead:** We receive the assignment on Tuesday, not right on Monday.

**Customer:** Here is what I suggest. We can meet in person on Monday and develop the plan for the week until Sunday. As I understand it, your task each week is not always to complete some very specific steps, but to make a certain amount of progress in developing the application.

**Team Lead:** Yes, for example, we need to make MVP v0, MVP v1, and so on.

**Customer:** Exactly. So if we develop the plan on Monday, then by Sunday you will probably only need to execute it. If the assignment includes a very specific requirement, we can also have a short online call on Thursday, and I will create a meeting link. Or you can decide yourselves what works best. If you understand that you cannot finish something or plans change, you just notify me.

**Team Lead:** Yes. The thing is that in previous assignments we were asked to approve the sprint and its tasks with you. On Monday, we will not have the assignment yet, so we will not know the specific topic of our communication. In general, Monday meetings would be great, but I am not sure.

**Customer:** I still support Monday. We can also have a 15-minute recorded online call. The call tool can even generate a transcript. As I remember, you mainly need the transcript. So we can do that.

**Team Lead:** On Monday? Not Thursday?

**Customer:** On Monday we meet in person and really discuss what to do during the upcoming week. We will also discuss what you managed to finish by Sunday. On Thursday, we can simply have a 15-minute recorded call so that you have the transcript needed for the assignment.

**Team Lead:** Then next Monday?

**Customer:** Yes.

**Team Lead:** I mean, what time?

**Customer:** When is it convenient for you? I assume you have classes.

**Team Lead:** We have a final.

**Customer:** Right, you have an exam.

**Team Lead:** At 10:00, and I think it lasts about an hour and a half.

**Customer:** If you do not want to meet on the day of the exam, I understand. Then we can meet on Tuesday. My Monday is currently completely free.

**Backend Developer A:** In general, on Mondays we are free after around half past two or after three. We do not have electives. Specifically this Monday, I think we are free from around 3 to 5.

**Team Lead:** Will we have a lab this Monday?

**Backend Developer A:** Even if we do, from 3 to 5 we should be guaranteed to be free.

**Team Lead:** So we can do 3 p.m.?

**Customer:** Yes, let us do that.

**Backend Developer A:** Yes.

**Customer:** Then we will meet in person at 3 p.m. on Monday and discuss everything.

## Additional Requests, UML Database Diagram, Achievements, and Action Items

21:15-24:15

**Customer:** Overall, that is probably everything. Actually, I have one more request: more visualization. I am saying this generally because both I and customers often need to see things visually. It is one thing when you just explain something; I believe you, of course. But in real work, people often ask for things to be short and to the point.

For example, at [redacted company], I can summarize my whole task pool in five minutes without going into all the details. I am not asking you to tell me everything in five minutes. What I mean is that I want to see the results. It could even be one slide showing what has been done: AI integration, database connection, and so on. Something like a summary slide in an Apple presentation, where they briefly summarize all the features. The same idea could be used for development sections: backend, frontend, ML, and so on.

This is just a personal request. If it is not too difficult, please do it.

Also, since you said you studied UML diagrams, please make a UML diagram for the database. I want to make sure we are on the same page and that you understand the database architecture. I think it will not be too large because the project is not very complex. It will probably include the user, car, chat, parts, and similar entities. I just want to understand what fields there are.

And start developing a list of achievements. Today you suggested a good idea about the “towed car” achievement. I liked it a lot. It is good that your thinking is moving in the right direction. So start developing this part too.

**Team Lead:** Okay.

**Customer:** Will you manage to do this by Monday?

**Team Lead:** The UML diagram, achievements, and MVP v1, right?

**Customer:** Yes.

**Team Lead:** That sounds manageable.

**Customer:** Deal. From my side, I will send the DeepSeek token and URL. I will send them this evening.

**Team Lead:** Great.

**Customer:** In the worst case, tomorrow morning. That is all.

**Team Lead:** Great.
