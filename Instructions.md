# LAMBA_Team-22

## Assignment 3
for part 1
- [Current user-story index](./docs/user-stories.md)
- [Historical Week 2 user stories](./reports/week2/user-stories.md)

## Assignment 2 Reports

- [Week 2 Submission Index](./reports/week2/README.md)
- [MVP v0 Report and Smoke Check](./reports/week2/mvp-v0-report.md)

## Backend

- [Backend local setup and authentication API](./backend/README.md)

## Android and Backend Integration

The Android emulator uses `http://10.0.2.2:8000/` to reach the backend running
on the host machine. Start the backend before testing registration or login
from the Android app.


## Local setup instructions:

### Reqirements 
- [Git](https://git-scm.com/downloads)
- [Android Studio](https://developer.android.com/studio)
- Android SDK 36
- An Android emulator (provided in the Android Studio) or physical device supported Android 8.0 or later

### 1. Clone the repository 
Open terminal and paste: 
``` 
git clone https://github.com/ernest0507/LAMBA_Team-22.git
```
or in another way just download the ZIP archive and extract project into the local repository

### 2. Open project in the Android Studio

1. Open Android Studio
2. Select **Open** on the welcome screen
3. Select the root `LAMBA_MVPv0` directory
4. Click **Open**
5. For the first time the Android Studio may ask whether trust to the project, choose **Trust Project**

### 3. Synchronize the Gradle project
1. Open **File > Sync Project with Gradle Files**

### 4. Build the project
1. Click on button **Assemble 'app' Run Configuration** or use hot key CTRL + F9

### 5. Create an emulator
1. Click on the button **Device Manager** (usually on the right toolbar)
2. Click on **+** and select **Create a virtual device**
3. Select the appropriate virtual device and click **Next > Finish**

## 6. Run an emulator
1. Click on the button **Running device** (usually on the right toolbar)
2. Click on **+** or use hot key CTRL+SHIFT+T
3. Choose the added device
4. Wait for connection, for the first time it may take up to 2 minutes

## 7. Run the project
1. Click **Run app** or use hot key SHIFT+F10

