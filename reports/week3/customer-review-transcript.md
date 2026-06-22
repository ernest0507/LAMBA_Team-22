# Customer Meeting Transcript

**Language:** English

**Privacy note:** This transcript was sanitized for repository publication. Real names and other personally identifying details were replaced with roles or `[redacted]`. No email addresses or phone numbers were present in the source transcript.

**Timestamp note:** The source transcript did not include timestamps. No timestamps were inferred; each transcript block is marked with `[timestamp unavailable]` on a separate line.

**Publication decision:** The customer confirmed that the team had permission to create a public GitHub repository and store project materials there. The sanitized transcript is therefore prepared for repository use without an additional review round.

---

[timestamp unavailable]
**Team Lead:** We were asked to write User Stories — these are basically sentences, in general, that would help us understand what the user actually wants and what features we need.

[timestamp unavailable]
**Customer:** User Stories are usage scenarios.

[timestamp unavailable]
**Team Lead:** Ah, well, something like that, yes.

[timestamp unavailable]
**Customer:** So, like, how they got into the situation, all that stuff. Well, okay, no, let's — sorry — go ahead and tell me, in general, what you did.

[timestamp unavailable]
**Team Lead:** Yes, I'll open it now to show you.

[timestamp unavailable]
**Customer:** No, by the way, you also had to do market analysis, right? That's what you just said. You are Team [redacted], right?

[timestamp unavailable]
**Team Lead:** Yes.

[timestamp unavailable]
**Customer:** Okay, got it. So, remind me who is doing what right now?

[timestamp unavailable]
**Backend Developer B:** At the moment, we don't really have fixed core roles, but overall: Backend Developer A is doing backend, Documentation Lead is doing documentation, I am doing backend, Team Lead is our team lead, Frontend Developer A is doing frontend, and Frontend Developer B is also doing frontend.

[timestamp unavailable]
**Backend Developer A:** Well, specifically for this assignment, we all did everything that needed to be done.

[timestamp unavailable]
**Customer:** Tell me, how is your MVP — is it ready? I am asking not even for myself, more because I am still a bit shocked that you were told to make MVP version 0 by Sunday. As I understand it, that was unexpected for you as well?

[timestamp unavailable]
**Team Lead:** We did not expect it at all.

[timestamp unavailable]
**Backend Developer B:** Because they posted it on Wednesday, I think.

[timestamp unavailable]
**Customer:** That is just awful. I know this is being recorded, but that is my opinion. Okay.

[timestamp unavailable]
**Team Lead:** In general, we also needed to ask for permission. We got it, so that we could create a repository and store everything on GitHub in public access.

[timestamp unavailable]
**Customer:** As I understand it, my message is enough for you.

[timestamp unavailable]
**Team Lead:** Yes, overall, yes. They are all in English. We came up with 10 User Stories. Just briefly translating them now: as a car owner, I would like to store all car data in one place, which could help predict possible breakdowns and make them easier to debug in the future. We put this into must-have, meaning it is something that definitely has to be there. The next one is the same — must-have.

As a car owner, I want, basically, to receive up-to-date information through an AI agent, which would allow all of this to be processed faster somehow.

Also, as a car owner, I want to see a timeline of all expenses and important events, simply to keep the car's history. This is must-have. The fourth one: what goes into must-have is that, as a car owner, I want to see all the statistics on how much I have spent, in order to plan my budget for the future.

The fifth one, already in should-have, is something that could be there. As a car owner, I want to upload receipts, some documents in PDF format, so that later they can be confirmed, so that this would somehow be stored somewhere. Then: as a car owner, I want the AI agent to analyze all information and warn me about future breakdowns.

Seventh. As a car owner, I want to customize my digital twin: change the color there, choose the body type, for example, add more personalization and customization. This is in could-have, but less important.

The eighth one is could-have. As a car owner, I want to create families where several users own one digital twin and have access to it.

Could-have. The ninth one is that, as a car owner, I want the app to automatically recognize text from document images, for example, from receipts, so that all the information is simply digitized rather than stored as pictures.

And also in could-have, the tenth: as a car owner, I want to send a voice message to the agent in order to communicate with it not only through text, but just to communicate. For MVP version 0, we chose, as a result, to store data, communicate with the agent, basically store the timeline of main purchases and some events, and statistics on expenses.

[timestamp unavailable]
**Customer:** You want to do all of this by Sunday?

[timestamp unavailable]
**Backend Developer A:** For us, all of this is still at the frontend level by Sunday. Next week there will be version 1. What functions will be there?

[timestamp unavailable]
**Customer:** With the backend already, yes.

[timestamp unavailable]
**Team Lead:** The thing is that for MVP version 0, we are doing everything I listed, those four points, but without the backend. The goal is kind of to test it and understand whether the hypothesis works or not, and then this goes into MVP version 1, where maybe we pull in the backend and try to put everything together. So. And what we have played around with in the prototype: for these four User Stories, we made a scenario — how the screen would look and what the user would do now. We'll open it now. How did it turn out? We simply drew sketches, threw them into an AI tool, and asked it to make them more or less acceptable to the eye. These are sketches, basically what we managed to draw. What could be there. And we took all of that stuff and put it in so that Figma could play around with it.

[timestamp unavailable]
**Backend Developer A:** We decided that it was important for the chat with the agent to be on the main screen, so that the user immediately notices it, so that it is kind of the priority use case for the application. Also, here is the car model; we may still change the colors so that the car can be customized and so that it does not get lost on this dark-blue background. The name is written there as well. Also, here we have reminders, and most importantly, let's say, events — for example, changing the oil — and I think the user will be able to add this themselves. Some kind of statistics.

[timestamp unavailable]
**Team Lead:** To look, tap on the main page with the timeline of what is happening, go back, communicate with the AI agent. We were also asked about, for example, some emergency situation, a scenario if some error happens. Overall, for now the whole functionality is communication with the agent, we store information, then what we had as mandatory — we communicate with it, the timeline of all expenses and what happens. For now, it looks like this. So now, today, Friday, Saturday, and part of Sunday, we are working on the frontend, trying to turn this into something like a skeleton that simulates the work. We upload all of this, and then after that we will connect the backend. Those are the plans for now, and that is where we are.

[timestamp unavailable]
**Customer:** It looks very good. It really looks good. Plus, I made a note for every team that they really need to emphasize AI, that it should be more of a priority. So what you said is good. Here. The only question is: in what form? Let's say I write a question there and click "send" — then, basically, what happens next?

[timestamp unavailable]
**Team Lead:** Next, we are planning a transition to a chat with a window, meaning the dialogue window will not be right here. By the way, we were thinking about this: when the user creates a digital twin for the first time, how should that happen? We had two options: letting the user do everything through an AI chat, or the second option — simply filling in some field themselves, filling in the fields, and then moving on.

[timestamp unavailable]
**Customer:** And what did you settle on? Both. In general, let's say if we need to customize something, if I want to choose — it is not hard, for example, to write "red" now... In general, where we choose the car color, I would want to click buttons and see how it would look. If I write "red" or "blue" in the chat, then overall it is probably quite difficult for the user to figure out which color would suit better if they want to think about it. So we thought: both options are possible. Well, the priority was on the AI agent, so...

[timestamp unavailable]
**Customer:** It seems to me that at the registration stage, you can really fill out a form. That is, yes, for now just skip it and fill out the form, and then already focus on communication with the assistant. Moreover, I would probably even risk suggesting that it would be good to make it a chat overall. That is, the person opens the app and immediately sees a chat. A chat right away. Basically, like ChatGPT, except in the sidebar you do not have chat history, for example, but all these menus, roughly speaking, and you move to that same page. What I mean is that I would like there to be less depth to the AI, to the chat, than to this kind of statistics, and for the user to make fewer taps to get to the chat.

[timestamp unavailable]
**Team Lead:** For example, we can divide this: everything that is lower can be the chat, allocate space for it, and everything above can be some kind of just...

[timestamp unavailable]
**Customer:** I think that would be excellent.

[timestamp unavailable]
**Team Lead:** And, if needed, you could swipe up and fully go into the chat, basically.

[timestamp unavailable]
**Customer:** Yes, super, super, that sounds excellent overall.

[timestamp unavailable]
**Team Lead:** And we also thought that swiping right and left could switch between car profiles if there are several of them. And...

[timestamp unavailable]
**Customer:** Well, for the MVP, let's have one car. That is extra work; you do not need it, so let's have one for now, and that's it.

[timestamp unavailable]
**Team Lead:** And that is how it is for the prototype so far.

[timestamp unavailable]
**Customer:** This is about the cringe feature. Overall, did you come up with anything?

[timestamp unavailable]
**Team Lead:** Yes, several ideas.

[timestamp unavailable]
**Backend Developer B:** First, we wanted to add achievements, 100%. Maybe it is not exactly cringe, but it is a good idea. In general, if you drove a million kilometers — veteran, or something like that. Also, comparison with some other users: if you are a BMW owner and you have violated traffic rules 50 times more than others, then you are cool, and you get messages saying you did great, something like that. Then a complaints journal, so that the car could complain: it sits there whining that its tires are not being changed, the oil.

[timestamp unavailable]
**Customer:** You mean the car whines?

[timestamp unavailable]
**Backend Developer B:** Yes, yes, yes, that it is in a lot of pain and something like that. There was also an idea of theft...

[timestamp unavailable]
**Documentation Lead:** There was the theft idea, but we thought it could interfere with using the application itself, and it would be hard to implement.

[timestamp unavailable]
**Backend Developer B:** If, basically, the car whines often, then for some tokens or internal currency, your friend or just a user can buy out your twin, and, like, your car has been stolen, and you get a message saying that you do not take care of your car, and it was stolen from you, and so on. Well, yes, that could interfere somehow.

[timestamp unavailable]
**Documentation Lead:** We also thought about the idea that if some breakdown happens and, for example, the user books a service appointment, their friends get notifications and can make fun of them, like: "Ha-ha, the person broke down again."

[timestamp unavailable]
**Team Lead:** About fines: if you exceeded the speed limit somewhere, it still needs to be recorded, because, well, fines are also expenses for the car. So that friends can tease you: you exceeded the speed limit somewhere or violated something.

[timestamp unavailable]
**Backend Developer B:** Did not buckle up again.

[timestamp unavailable]
**Team Lead:** Yes, yes, yes.

[timestamp unavailable]
**Customer:** Of all the ideas, which one did you like the most? Look, in general, what I wanted to say. Basically, the prototype at least looks very sound, genuinely good, I really like it. About the cringe feature: if you do not want to do it, okay, I will understand. I just think that you proposed a lot of different cool ideas; choose one that you like. I am simply afraid that I overdid it a little with the cringe feature last time. So what I mean is that you are building it very well. If implementing the cringe feature interferes with good development of the application, then let's not do it. But of course, I am in favor of adding some kind of twist to the application. On the contrary, you threw out ideas. I realized that you got the vibe a little bit. So that is cool too. Here. Well, basically, what do you think? Let's do it this way.

[timestamp unavailable]
**Team Lead:** Overall, we are not against adding it.

[timestamp unavailable]
**Backend Developer A:** Yes, I think we can integrate it into the application in a reasonable way, if it is not too massive or large-scale. We can choose something, for example, achievements: they will not interfere at all.

[timestamp unavailable]
**Customer:** I think so too: doing something with friends is cool, but it will be hard to implement. That is, the friends would also have to download the app. We definitely will not manage to do that within the MVP right now. So achievements sound good. Let's probably do achievements, yes. That is, basically, we need to think about it. You can play around with it; for example, either you or the car receives the achievement — split it somehow like that. What I mean is yes, it is a good idea, I like it. Then let's try to develop it somehow.

[timestamp unavailable]
**Backend Developer B:** There was also an idea with the car's birthday: the user enters the purchase date, and they get a notification, like an anniversary.

[timestamp unavailable]
**Customer:** That is good too.

[timestamp unavailable]
**Backend Developer A:** Yes, possibly some short summary with information: what you have done by the 10th year of the car's life, and so on.

[timestamp unavailable]
**Customer:** The repairs you have gone through together, all that stuff, yes, good.

[timestamp unavailable]
**Team Lead:** Also, not about the prototype, but I was talking about external APIs: we can try integrating with Yandex. Yandex Maps or Navigator.

[timestamp unavailable]
**Customer:** Yes, tell me about that. I just do not fully understand how this integration looks, in the sense that it is effectively a mobile application. How does it transmit this phone data, even more, this information, and how does it work? Like, does Yandex have the user's trip history, roughly speaking, and speed at a given moment in time?

[timestamp unavailable]
**Team Lead:** Essentially, yes, it is possible to get speed at a given moment in time, and they have something specifically for this. I'll find it now.

[timestamp unavailable]
**Customer:** Go ahead, go ahead. Because honestly, I am a little shocked; I did not even know such a thing existed. I thought the only thing there was route building, that kind of thing.

[timestamp unavailable]
**Team Lead:** Here, you can choose several things, I think this is all here. And as I understood it, it can be integrated into a mobile application for usage. For example, they have some kind of MapKit. And I saw there directly that you can get information about speed in real time, even about violations if we are driving; there are some cameras there. I tried to get a free token, but I indicated that we are using this purely for hobby purposes.

[timestamp unavailable]
**Customer:** Well, in general, right now that is partly true. But here the question is for the mobile developers: in terms of whether you can implement it. It sounds difficult. Again, regarding speed — no, this is actually a good point, even so that you do not have to mess around with the accelerometer or, let's say, these coordinates. You can simply get an actual scalar value in the form of speed. But maybe... I do not know, this needs to be researched in general. So you can research with your small frontend team whether it is difficult to fit this in or not. Although wait, why do we need it, by the way? Let's think about that.

[timestamp unavailable]
**Team Lead:** To automate the process a little. Because if you enter everything manually, it is not very good, but this way all the information kind of collects and analyzes itself. It is a bit more pleasant, because every time you have to bring something together. Well, and, for example, to learn your driving style.

[timestamp unavailable]
**Customer:** That is also not bad, by the way. It is just that, of course, it is hard to determine driving style from speed alone.

[timestamp unavailable]
**Team Lead:** Also, about... If OBD is involved, that is an absolutely great thing: you can make everything from there, learn some car errors if something is wrong, and gather information about the oil, engine RPM, and so on.

[timestamp unavailable]
**Customer:** No, I absolutely agree, but that is extra, that is already... I just want us to definitely manage to make at least the minimum.

[timestamp unavailable]
**Documentation Lead:** I think that by the end of this month we can finish the MVP, and then, if...

[timestamp unavailable]
**Customer:** If we finish the MVP — yes, then, damn, I do not know, we can really try a little. Maybe if the project really takes off, and we try to do something with hardware, I would personally love to work on this with you; it would be a huge pleasure overall. Let's finish this first. So first we finish this. The only other thing: okay, let's do this — for now we will not look at speed, we will wait a month and then look at it. Right now, I think we need to research fines. Let's look, because usually government services are very bad with APIs. Even if they have an API, tokens are issued with such difficulty there; you have to fill out a billion documents, so this needs to be checked now. I mean, please check it. I think research it for an hour or two at most, roughly speaking, and that's it. If it is total trash there and you need to describe all kinds of application security and so on, then we drop it entirely and will not even look at it. Then, for now, we will just have fines as another feature. So. And overall, yes. Good. Good, guys. Achievements, prototype. For now, I like everything. This... Also make the architecture. Specifically, take a look at a diagram in draw.io. It is generally called an HLD diagram. That is, it is a diagram that is basically like a flow diagram, roughly speaking. You need to understand what you have under the hood. You do not just have code written in Python; you will have some services interacting. There is, roughly speaking, prediction, the AI, the database, let's say, and so on. Here. I want to understand how you came up with all of this. And the mobile app too, of course, absolutely.

[timestamp unavailable]
**Backend Developer B:** I think we will actually have this kind of assignment in the next task.

[timestamp unavailable]
**Customer:** Excellent, even better then.

[timestamp unavailable]
**Backend Developer A:** They are asking us for a UML diagram.

[timestamp unavailable]
**Customer:** UML is also, by the way, excellent. Data — you see, on the other side of the board we tried to sketch something out. So yes, that too. Basically, send the reports to the chat, okay. I am also interested to look at them.

[timestamp unavailable]
**Team Lead:** If needed, we can share the GitHub repository.

[timestamp unavailable]
**Customer:** You are not on GitLab?

[timestamp unavailable]
**Team Lead:** There was a choice.

[timestamp unavailable]
**Customer:** Okay, okay. No, not yet; if needed, I will ask. So, then let's decide on the plan for the coming week. What will you be doing now?

[timestamp unavailable]
**Team Lead:** Well, basically, we need to... We made the prototype, discussed the User Stories. What is left is to finish MVP 0. And the analysis of our meeting, we will need to write the transcript. And overall, write the analysis of the week: what we learned and what we did. Overall, the main thing right now, the biggest headache, is MVP 0.

[timestamp unavailable]
**Customer:** Well, I understand, yes, of course, these deadlines are awful. But by the way, this is very similar to reality, just so you know. What I mean is, when you start working, be ready that this will happen often. This... Okay. Then from you, I need the architecture. You said that, one way or another, you also need to write it in the report, so that is great — you will kill two birds with one stone. Finish MVP 0. When you make it, you can also send at least a demo just so I can look at it. Well, what else? The AI, resources. Regarding resources — I do not know, have you found out anything?

[timestamp unavailable]
**Team Lead:** What resources specifically?

[timestamp unavailable]
**Customer:** Servers, GPUs, like, I do not know, and so on.

[timestamp unavailable]
**Backend Developer B:** They provide a VM.

[timestamp unavailable]
**Customer:** Yes, I know that they provide a VM. In general, there is also an option with GPUs, but it seems we will not have any models, so I just... Well, we already discussed this last time; basically, we will not bother. I will provide access to an LLM API endpoint and a suitable low-cost model. [redacted] So, that is probably everything. Do you have any questions?

[timestamp unavailable]
**Documentation Lead:** Probably not. Not for now.

[timestamp unavailable]
**Customer:** Then keep going in the same spirit. Everything looks good.

[timestamp unavailable]
**Backend Developer A:** Thank you.

[timestamp unavailable]
**Documentation Lead:** Thank you very much.
