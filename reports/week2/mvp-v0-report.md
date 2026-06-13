
# MVP v0 Report

## Purpose and description of the MVP v0 foundation

LAMBA MVP v0 is an Android application built with Kotlin in Android Studio. It is only frontend version without backend, database, or external AI services.

The purpose of MVP v0 is to check the feasibility of the the highest-priority features (Must Have). The application includes digital car twin onboarding, a main dashboard, a mock AI assistant, and a timeline with expense information. These flows use placeholder data.

## Runnable artifact

The APK file is available by link:

[LAMBA MVP v0 APK](https://drive.google.com/drive/folders/19qWz340HO7KuXygsQeKWR5n9QI-SV4m2?usp=sharing)

To install it, download the `.apk` file to an Android device running Android 8.0 or later. If Android requests permission to install applications from an unknown source, grant it for the application used to download the file, then open the APK and complete the installation.

## Public video demonstration

[Video demonstration](https://drive.google.com/drive/folders/16ioINn3AaXiOIoyrjYcjZ_teW2S9cDDQ?usp=sharing)

## Relationship to the prototype and proposed MVP v1 stories

The application's visual style and main navigation flows are based on the interactive prototype which was designed in Figma. MVP v0 represents the four Must Have user stories:

US-01 - Storing car's data -> The user can enter basic car information and complete the digital twin creation flow. 
US-02 - Interact with AI-agent -> The user can open the assistant, send a message, and receive a fake response
US-03 - Main expenses and events timeline -> The user can open a timeline containing car expenses and maint events. 
US-04 - Expenses statistics -> The dashboard and expense screen display sample totals and expense summary information. 

## Current limitations, placeholders, and mocks

- The application has no backend or database.
- Car data isn't saved
- The AI assistant isn't real

## Local setup instructions

See local build instructions in the [root README](../../README.md#local-setup).

## Repeatable smoke-check scenario

### Access requirements

- An Android device or emulator running 
- The APK file from the [public artifact folder](https://drive.google.com/drive/folders/19qWz340HO7KuXygsQeKWR5n9QI-SV4m2?usp=sharing)

### Steps and expected results

1. Install and open the application.
   Expected result: The LAMBA welcome screen opens and shows the button for creating a digital car twin.
2. Start the creation flow, enter sample car details, and continue to the second step.
   Expected result: The application accepts the entered values and opens the car customization screen.
3. Select a car color and body type, create the twin, and open the main screen.
   Expected result: A success screen appears, followed by the main dashboard when its primary button is pressed.
4. Tap the expense card on the dashboard.
   Expected result: The expense history screen opens and displays a total and a timeline of sample expenses.
5. Return to the main screen, enter a question in the AI assistant field, and send it.
   Expected result: The chat screen displays the user's message and a simulated response.

