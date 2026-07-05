# User Acceptance Tests

## UAT-001: Account Registration

**Status:** Active

**Related requirement:** [US-11: Create account and sign in](https://github.com/ernest0507/LAMBA_Team-22/issues/61)

**User goal:** As a new user, I want to create an account so that I can start
using LAMBA and continue to digital twin registration.

**Preconditions:**

- The LAMBA mobile application is installed and opens successfully.
- The registration screen is available.
- The backend authentication service or prepared demo environment is available.
- The user has an email address that is not already registered in the selected
  test environment.

**Steps:**

1. Open the application.
2. Go to the registration screen.
3. Enter an email address.
4. Enter a password.
5. Repeat the password.
6. Tap the registration button.
7. Repeat the scenario with invalid or incomplete data.

**Expected outcome:**

- The account is created successfully when valid data is entered.
- The user can continue to the digital twin registration flow.
- If the entered data is incorrect or incomplete, the application shows a clear
  validation or registration error.
- The application does not freeze, crash, or leave the user without feedback.

**Assignment 4 execution results:** Passed

**Customer comments or observed issues:** From the user side, everything is clear and intuitive 

**Resulting PBIs or issues:** The process of processing input data is a bit long and should be optimized.

## UAT-002: Vehicle Digital Twin Creation

**Status:** Active

**Related requirement:** [US-01: Storing car's data](https://github.com/ernest0507/LAMBA_Team-22/issues/31), [PBI: Implement car data entry flow](https://github.com/ernest0507/LAMBA_Team-22/issues/50)

**User goal:** As a car owner, I want to create a digital twin of my vehicle so
that the application can store and use my car information.

**Preconditions:**

- The LAMBA mobile application is installed and opens successfully.
- The user can register or sign in.
- The vehicle creation flow is available.
- The test user has basic vehicle information available, such as model,
  production year, mileage, notes, color, and body type.

**Steps:**

1. Open the application.
2. Complete registration or sign in with an existing account.
3. Go to the vehicle creation or add-car screen.
4. Enter the main vehicle data: model, production year, mileage, and notes.
5. Tap `Continue` to move to the second vehicle registration screen.
6. Select the vehicle color.
7. Select the vehicle body type.
8. Complete the vehicle creation flow.
9. Repeat the scenario with missing or invalid required fields.

**Expected outcome:**

- The user can fill in the vehicle data.
- The application creates a digital twin of the vehicle.
- The entered vehicle data is saved.
- If required fields are missing or invalid, the application shows an
  appropriate validation message.
- The application does not freeze, crash, or lose entered data without feedback.

**Assignment 4 execution results:** Passed

**Customer comments or observed issues:** From the user side, everything is clear and intuitive 

**Resulting PBIs or issues:** When the user is customizing the digital twin, the image of twin must be changed to appropriate 

## UAT-003: Sign In With Existing Account

**Status:** Active

**Related requirement:** [US-11: Create account and sign in](https://github.com/ernest0507/LAMBA_Team-22/issues/61)

**User goal:** As a returning user, I want to sign in with my existing account
so that I can access the main application screen.

**Preconditions:**

- The LAMBA mobile application is installed and opens successfully.
- The sign-in screen is available.
- A test account exists in the selected test environment.
- The backend authentication service or prepared demo environment is available.

**Steps:**

1. Open the application.
2. Go to the sign-in screen.
3. Enter the account email address.
4. Enter the account password.
5. Tap the sign-in button.
6. Wait for the application to navigate after successful sign-in.
7. Repeat the scenario with an invalid email or password.

**Expected outcome:**

- The application accepts correct sign-in data.
- The user successfully signs in to the account.
- After sign-in, the user reaches the main application screen.
- If the email or password is incorrect, the application shows an error message.
- The application does not freeze, crash, or leave the user without feedback.

**Assignment 4 execution results:** Passed

**Customer comments or observed issues:** from the user's point of view, everything is clear and intuitive.

**Resulting PBIs or issues:** The process of processing input data is a bit long and should be optimized.

## UAT-004: Add Vehicle Expense Through AI Chat

**Status:** Active

**Related requirement:** [US-02: Interact with AI-agent](https://github.com/ernest0507/LAMBA_Team-22/issues/32), [US-03: Main expenses and events timeline](https://github.com/ernest0507/LAMBA_Team-22/issues/33), [PBI: Integrating the chat with AI-assistant](https://github.com/ernest0507/LAMBA_Team-22/issues/49)

**User goal:** As a car owner, I want to add a vehicle expense through the AI
chat so that I do not have to manually fill in an expense form.

**Preconditions:**

- The LAMBA mobile application is installed and opens successfully.
- The user is signed in.
- The user has a vehicle digital twin available.
- The AI chat screen is available.
- The test environment either supports automatic expense saving or shows a
  clear fallback message when automatic saving is unavailable.

**Steps:**

1. Open the AI chat.
2. Write an expense message, for example: `I spent 1000 rubles on an oil change today.`
3. Send the message.
4. Wait for the AI assistant response.
5. Check whether a new expense record appears in the vehicle history.
6. Repeat the scenario when automatic saving is unavailable or cannot be
   completed.

**Expected outcome:**

- The user's message is displayed in the AI chat.
- If automatic saving is supported, the expense is saved as a record.
- If the expense is saved, the new record appears in the vehicle history.
- If automatic saving is not supported or fails, the AI assistant shows a clear
  error or fallback message.
- The application does not freeze, crash, or break the chat flow.

**Assignment 4 execution results:** Failed

**Customer comments or observed issues:** AI agent does not cover all possible scenario of conversation

**Resulting PBIs or issues:** More comprehensive context must be written to AI agent

## UAT-005: View Expense Statistics

**Status:** Active

**Related requirement:** [US-04: Expenses statistics](https://github.com/ernest0507/LAMBA_Team-22/issues/34)

**User goal:** As a car owner, I want to view expense statistics for my vehicle so that I can understand my car spending over time.

**Preconditions:**

- The LAMBA mobile application is installed and opens successfully.
- The user is signed in.
- The user has a vehicle digital twin available.
- The statistics screen is available.
- The selected test vehicle has several expense or maintenance records with different costs, or the test environment intentionally has no records to verify the empty state.

**Steps:**

1. Add several expense or maintenance records with different costs.
2. Open the side menu or application navigation.
3. Go to the statistics screen.
4. Review the displayed expense statistics.

**Expected outcome:**

- The statistics screen opens successfully.
- The user sees a summary of vehicle expenses.
- The statistics include the added expense or maintenance records.
- Amounts and categories are displayed clearly for the user.
- If there is no data for statistics, the application shows a clear empty state.
- The application does not freeze or crash when opening statistics.

**Assignment 5 execution results:** To be executed during the Assignment 5 customer UAT session.

**Customer comments or observed issues:** Pending customer execution.

**Resulting PBIs or issues:** Pending customer execution.

## UAT-006: View Vehicle Event Timeline

**Status:** Active

**Related requirement:** [US-03: Main expenses and events timeline](https://github.com/ernest0507/LAMBA_Team-22/issues/33)

**User goal:** As a car owner, I want to view the history of expenses, repairs, and maintenance events in a timeline so that I can track what happened to my vehicle.

**Preconditions:**

- The LAMBA mobile application is installed and opens successfully.
- The user is signed in.
- The user has a vehicle digital twin available.
- The vehicle history or timeline screen is available.
- The selected test vehicle has one or more records, or the test environment intentionally has no records to verify the empty state.

**Steps:**

1. Add one or more records, for example an expense, maintenance event, or repair.
2. Open the side menu or application navigation.
3. Go to the vehicle history screen.
4. Review the event timeline.
5. Find the previously added record in the timeline.

**Expected outcome:**

- The history screen opens successfully.
- The timeline displays events related to the selected vehicle.
- The added record appears in the timeline.
- Each record shows the main available data, such as title, date, category, cost, and mileage when those values were provided.
- If there are no records, the application shows a clear empty state.
- The application does not freeze or crash when opening the timeline.

**Assignment 5 execution results:** To be executed during the Assignment 5 customer UAT session.

**Customer comments or observed issues:** Pending customer execution.

**Resulting PBIs or issues:** Pending customer execution.
