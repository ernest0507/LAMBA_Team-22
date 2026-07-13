# Week 6 Transition-Readiness Meeting and Customer Trial Transcript

**Project:** LAMBA  
**Meeting type:** Week 6 transition-readiness discussion, customer-facing documentation review, and customer trial  
**Language:** English translation of the Russian meeting transcript  


## 00:00-01:30 — Handover checklist and current product scope

**Product Owner:** This document is basically our checklist for how ready the project is to be handed over for your use. To fill it in, we need to ask a set of questions about what is ready, what is not ready, and what needs to happen before transition.

**Product Owner:** Let us start with what we have already implemented. We have user registration, authentication, digital car profile registration, AI chat customization, adding expenses through chat and forms, statistics display, expense and record history, and trip mode.

**Product Owner:** What we are working on now is achievements. The system-generated achievements are still being developed. The achievements that the user selects manually can already be selected and marked as completed.

**Customer:** Completed?

**Product Owner:** Yes, completed. The user profile is planned for the next sprint. We also have smaller fixes, such as application-version details and additional AI chat context work.

## 01:30-03:40 — APK access, test accounts, and source-code handover

**Product Owner:** The questions are split into several categories. The first one is simple: is it enough for you to get access to the Android APK through a GitHub Release and Google Drive? In other words, how should we provide access to the application itself?

**Customer:** I think yes. Keeping it in cloud storage is fine.

**Product Owner:** Do you need test accounts for checking the application?

**Customer:** Yes.

**Product Owner:** Test accounts with some pre-filled data?

**Customer:** Yes, I think so. Imagine that later I work with this project with another team. It would be useful for them to have prepared test accounts so they can understand the product faster.

**Product Owner:** Who should have access to logins, passwords, keys, if they are needed?

**Customer:** Actually, my question is a bit different. I thought you would give me the source code.

**Product Owner:** We currently store it on our side. But yes, that is exactly what we are clarifying here.

**Customer:** I mean the whole project source code.

**Product Owner:** The whole project?

**Customer:** Yes.

**Product Owner:** Okay. Is public read-only access to GitHub enough?

**Customer:** Not really. I do not want to make changes in your repository. Since this GitHub setup may not be permanent, the most basic option would be to export everything as an archive and upload it to Google Drive.

**Product Owner:** Okay.

**Customer:** The archive should include both backend and frontend, basically everything together.

**Product Owner:** Okay. So we can show that as an archive, and then you can decide what to do with it, add collaborators, and so on. That also closes the ownership-transfer question: the codebase should be available to you.

## 03:40-05:45 — Backend location, maintenance, and target transition level

**Product Owner:** Should the backend run on the team server or on your server? Should we move the backend to your server, or leave it on the one we currently maintain?

**Customer:** I think you can leave it on that one. Actually, wait. I honestly have not thought much about it. I assumed this was not a turnkey managed solution. I thought you were making the product, and support would not remain on you. So in terms of where the server should be, it should probably be on my side. Let us do it that way.

**Product Owner:** So do you need to run and maintain the backend independently after the course?

**Customer:** Yes, as I said.

**Product Owner:** Okay. Server access would probably just be through SSH and console.

**Customer:** No, I do not even need access to your server.

**Product Owner:** Okay. Then backend secrets will also be stored on your side.

**Customer:** Right. My next question is already on the slide.

**Product Owner:** Do we need to rebuild the application for your backend now?

**Customer:** No. The main thing is to write very detailed documentation. That is a must-have for me.

**Product Owner:** So we need to provide deployment steps. We will also discuss the README, where we will describe step by step how to deploy and run the project, including Android application launch instructions.

**Product Owner:** Which final transition level do you expect by the end? There are three options: first, the documentation is ready and you can follow it to run the project on your side; second, you already run the project independently on your side; third, the product is already deployed or operated on your side.

**Customer:** Then it is the third option.

**Product Owner:** The third option. Okay.

## 05:45-08:55 — Customer handover documentation review

**Product Owner:** This is the Customer Handover document. It is a checklist where we store the current handover status, the transition scope, and repository information. It also explains how the documentation is organized.

**Product Owner:** We have a hosted documentation site. It contains the necessary information about architecture, reports, testing, roadmap, and user acceptance tests.

**Product Owner:** It also describes the current access status. For now, we are referencing the latest Assignment 5 release. It includes backend URL information, the available functionality, architecture, ADRs, environment variables, local access, Android setup, endpoints, backend verification, and user acceptance tests.

**Product Owner:** In the repository README we describe how to run the Android application. The backend also has a README that describes how to launch the backend. We are still improving it. The main information is also available on the hosted documentation site.

**Customer:** It should also be exportable, roughly speaking.

**Product Owner:** As a PDF file?

**Customer:** I think yes. The documentation is large.

**Product Owner:** Okay.

## 08:55-13:05 — APK trial, installation issues, registration, and car profile flow

**Product Owner:** I will send the new version now. The APK file.

**Customer:** Should I install it?

**Product Owner:** Yes. Our user test is adding a refueling record through the QR code of a receipt.

**Customer:** Okay. Wait a second.

**Customer:** Did you change the icon?

**Product Owner:** It needs to be converted in XML.

**Customer:** I have four applications with the same names and similar icons. This is a problem. I cannot install your package. I even know which team it conflicts with. I do not know why, but I see this for the first time. Maybe it is not because of the name, but it is still confusing.

**Product Owner:** It would be better to register again to check the cars.

**Customer:** Should I use another email?

**Product Owner:** Any temporary email is fine.

**Customer:** Okay. This is good. Do they all work?

**Product Owner:** Most of them do.

**Customer:** Cool. That is good. What about missing models, like 2106 and so on? Fine, I am being picky. Manual input is still possible, right?

**Product Owner:** Yes, manual input remains.

**Product Owner:** We will make the car images a bit larger. The current background is too large, so we need to crop it a bit.

**Customer:** Do colors work too?

**Product Owner:** Yes.

**Customer:** Did you fix the history issue?

**Product Owner:** It should be fixed.

**Product Owner:** The QR code is in the sidebar.

**Customer:** That is not very clear. I assume it is here, but it is not obvious.

## 13:05-17:30 — QR/refueling test, achievements, and UX feedback

**Product Owner:** I sent another build.

**Customer:** Okay.

**Product Owner:** While you are checking, you can look at achievements. The achievements that cannot be opened manually are unlocked by the system. Lower in the list, the user can choose some achievements manually.

**Customer:** On the image, is that supposed to be a rotated wheel?

**Product Owner:** Yes.

**Customer:** This is not clear at all.

**Customer:** There are no liters. Wait, how does this work overall? We have refueling as a separate thing, right? But this records it as an expense. It does not record it as refueling; it records it as a spending entry.

**Product Owner:** Yes, currently it records it specifically as an expense. It is also immediately shown in statistics.

**Customer:** Ideally, it should be changed to an actual refueling record.

**Product Owner:** Yes, that should be changed.

## 17:30-18:58 — Sprint status and follow-up items

**Product Owner:** The main Sprint task is refueling through a QR code. We also have a fix related to trip mode. We added achievements, and we are still working on them until Sunday. We will also fix the QR code flow.

**Product Owner:** Everything else remains for the next sprint: adding a user profile/account page, storing the token in the phone cache so the user does not need to log in every time, logout/sign-out, and smaller fixes. We will meet again on Tuesday and discuss them.

**Product Owner:** For receipt checks, we also need to create and store a separate identifier, because the same receipt can currently be added again.

**Product Owner:** The car images are too small because of the car/background layout. We need to crop them so they scale better.

## 18:58-20:15 — App identity and visual polish

**Customer:** From the cosmetic side, add an icon and a proper application name. I understand these are small things, but they affect perception.

**Product Owner:** I am not sending another APK; I just want to show the icon.

**Customer:** I remember it, yes.

**Product Owner:** It just needs to be converted into XML, because otherwise Android will not display it properly.

**Customer:** It is probably SVG, not XML.

**Product Owner:** Android Studio asks for XML in this setup.

**Customer:** One more optional request: can you rotate the wheel back in the images? It is very unclear now.

**Product Owner:** Okay, I will pass that to Maya.

**Customer:** Overall, that is all.
