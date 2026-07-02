# ADR-003: Use database for persistent storage

**Status:** Accepted

**Context:** The product must store user accounts, vehicle profile, records, and expenses in non-violate storage, such as databas. Backend must access to database and Android Mobile Application must receive the requested data from the backend through endpoints

**Decision:** The PostgreSQL database was chosen for persistent user data storage. The database runs on the server through a Docker container. The database structure is modeled using SQLAlchemy ORM classes. Alembic is used to manage database schema versions and migrations. The backend communicates with the database through async SQLAlchemy sessions and CRUD operations.

**Consequences and tradeoffs**: As a consequences, the product stores user accounts, vehicle profile, records in a persistant database. The backend can request to access the data and client can receive it using endpoints API. Tradeoffs: database must provide stable access to data. The developers team needs to maintain access, migration and configuration. 

**Quality requirements addressed where applicable** 
TBD
