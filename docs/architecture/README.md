### Static Component Diagram

In the diagram there is a starting point from the Client Layer. In the Client Layer there is a component called Android Mobile Application. This component represents the frontend side of the product. This component is based on Kotlin and Jetpack Compose.

The Android Mobile Application has a connection with the Backend Layer. This connection is represented as a lollipop connection through the REST/JSON API. The Backend Layer provides the REST/JSON API, and the Android Mobile Application uses this interface to communicate with the backend.

In the Backend Layer there are API routes run by FastAPI. The backend implements several product workflows, so it is reasonable to describe them as Backend Application Logic: Auth, Cars, Records, and Assistant. The Backend Application Logic uses the Persistence API to access stored product data.

The Data Layer has three main components: CRUD / Repository Layer, PostgreSQL Database, and Alembic Migrations. The CRUD / Repository Layer provides the Persistence API for the Backend Application Logic. The CRUD / Repository Layer also uses the SQL Database Interface provided by PostgreSQL to read and write data.

PostgreSQL also provides the Schema Migration Interface, which is used by Alembic Migrations to control and version database schema changes. PostgreSQL also provides a Database Admin API used by pgAdmin from the External Ecosystem.

In the External Ecosystem there is an OpenAI-compatible AI Provider. It provides the AI Provider API, which is used by the Backend Application Logic, especially by the Assistant-related logic.
