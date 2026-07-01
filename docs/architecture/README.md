## Static View - Component Diagram

In the diagram there is a starting point from the Client Layer. In the Client Layer there is a component called Android Mobile Application. This component represents the frontend side of the product. This component is based on Kotlin and Jetpack Compose.

The Android Mobile Application has a connection with the Backend Layer. This connection is represented as a lollipop connection through the REST/JSON API. The Backend Layer provides the REST/JSON API, and the Android Mobile Application uses this interface to communicate with the backend.

In the Backend Layer there are API routes run by FastAPI. The backend implements several product workflows, so it is reasonable to describe them as Backend Application Logic: Auth, Cars, Records, and Assistant. The Backend Application Logic uses the Persistence API to access stored product data.

The Data Layer has three main components: CRUD / Repository Layer, PostgreSQL Database, and Alembic Migrations. The CRUD / Repository Layer provides the Persistence API for the Backend Application Logic. The CRUD / Repository Layer also uses the SQL Database Interface provided by PostgreSQL to read and write data.

PostgreSQL also provides the Schema Migration Interface, which is used by Alembic Migrations to control and version database schema changes. PostgreSQL also provides a Database Admin API used by pgAdmin from the External Ecosystem.

In the External Ecosystem there is an OpenAI-compatible AI Provider. It provides the AI Provider API, which is used by the Backend Application Logic, especially by the Assistant-related logic.

#### Coupling and coheasion

The Android Application does not directly depend on the database. Instead of this Android Application communicates with the backend side through the REST/JSON API to get or post data related to the database. Backend Application Logic depends on the Persistence API. This approach excludes direct communication with the database from backend application logic because database access is handled by the CRUD. The AI-provider is also isolated by the AI Provider API, so any AI-related changes can be implemented within this interface scope without breaking independent parts of the code. In terms of coheasion the Android Application Layes focused on user interactions; Backend Layer focuses on handling API routes; Data Layer focuses on data persistant.

#### Maintability Applications

Changes in database structure are managed through Alembic migrations, so schema updates can be reviewed, repeated, and applied in the same way on different environments. The AI integration is also isolated in assistant-related backend logic, so replacing or reconfiguring the AI provider should not require changes in unrelated car, record, or authentication code.

#### Quality requirements supported/constrained

Maintainability is achieved by separating client, backend, and data responsibilities. The frontend and backend are interoperable because they communicate through the REST API. This provides the ability to integrate data from the backend without direct access to the database. Alembic supports safe and controlled database schema changes and helps keep PostgreSQL in a stable and reliable state. The time behavior of the response AI agent is limited by performance of AI provider

### Dynamic View - Sequence Diagram

First of all, the user goes through the authentication flow. After successful login and getting the access token, the user gets access to the personal account. After tha the user opens a special screen fills the maintenance record form, and clicks on the save button.
The Android Mobile App sends a request to the Record API routes with the form data and the access token. Then the Record API routes request the Auth Logic to check the token. If the token is correct the Record API routes request CRUD to make a new record in the database. The CRUD makes a new record in PostgreSQL. After the successful operation of adding the new record, the user gets a screen confirmation.
If the token is invalid the Record API routes return `401 Unauthorized` and the new record is not created

#### Scenario explanation

The sequence diagram represents the scenario of creating a new maintenance record by the user after successful login.

#### Importance of the scenario

The scenario is important to the product because it is the main feature of the application. The main goal of the application is saving car data in one place, and this feature covers the main requirement.

#### Architecture decisions, integration boundaries, or quality requirements

Architecture decision: the frontend does not directly communicate with the database. Instead of this, the Android Mobile App makes a request to the backend by using the API. In turn, the backend gets or posts data through CRUD, which allows it to read or modify data. The diagram also shows the integration boundary between the Record API routes and Auth Logic. This boundary is important for security, because the maintenance record can be created only after successful token validation. In terms of quality requirements, the diagram mainly supports reasoning about security, data integrity, and maintainability.





