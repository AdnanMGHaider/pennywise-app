### [Milestone 1 – Database Config]

- Removed PostgreSQL dependency and added H2 so the app starts without an external database.
- Replaced `application.properties` contents to configure H2 in-memory database.

### [Milestone 2 – GitHub, Postgres & Test Folder Restoration]

- Created GitHub repo and pushed initial scaffold.
- Provisioned free PostgreSQL on Render (`pennywise-db`) and noted host/port/credentials.
- Added Postgres driver to `pom.xml` and created `application-postgres.properties` with placeholders for env vars.
- Restored `backend/src/test/java/com/pennywise/backend` after it was accidentally moved.
