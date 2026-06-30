## Static Component Diagram

In the diagram there is a starting point from the Client Layer. In the Client Layer there is a component called Android Mobile Application. This component represents the frontend side of the product. This component is based on Kotlin and Jetpack Compose.

The Android Mobile Application has a connection with the Backend Layer. This connection is represented as a lollipop connection through the REST/JSON API. The Backend Layer provides the REST/JSON API, and the Android Mobile Application uses this interface to communicate with the backend.

In the Backend Layer there are API routes run by FastAPI. The backend implements several product workflows, so it is reasonable to describe them as Backend Application Logic: Auth, Cars, Records, and Assistant. The Backend Application Logic uses the Persistence API to access stored product data.

The Data Layer has three main components: CRUD / Repository Layer, PostgreSQL Database, and Alembic Migrations. The CRUD / Repository Layer provides the Persistence API for the Backend Application Logic. The CRUD / Repository Layer also uses the SQL Database Interface provided by PostgreSQL to read and write data.

PostgreSQL also provides the Schema Migration Interface, which is used by Alembic Migrations to control and version database schema changes. PostgreSQL also provides a Database Admin API used by pgAdmin from the External Ecosystem.

In the External Ecosystem there is an OpenAI-compatible AI Provider. It provides the AI Provider API, which is used by the Backend Application Logic, especially by the Assistant-related logic.

#### Coupling and coheasion
The Android Application does not directly depend on the database. Instead of this Android Application communicates with the backend side through the REST/JSON API to get or post data related to the database. Backend Application Logic depends on the Persistence API. This approach excludes direct communication with the database from backend application logic because database access is handled by the CRUD. The AI-provider is also isolated by the AI Provider API, so any AI-related changes can be implemented within this interface scope without breaking independent parts of the code.

#### Maintability Applications
Changes in the Android UI can usually be made inside the mobile application without changing PostgreSQL or migration code. Changes in database structure are managed through Alembic migrations, so schema updates can be reviewed, repeated, and applied in the same way on different environments. The AI integration is also isolated in assistant-related backend logic, so replacing or reconfiguring the AI provider should not require changes in unrelated car, record, or authentication code.

#### Quality requirements supported/constrained
Maintainability is achieved by separating client, backend, and data responsibilities. The frontend and backend are compatible because they communicate through the REST API. This provides the ability to integrate data from the backend without direct access to the database. Alembic supports safe and controlled database schema changes and helps keep PostgreSQL in a stable state.




