### [Milestone 1 – Database Config]

- Removed PostgreSQL dependency and added H2 so the app starts without an external database.
- Replaced `application.properties` contents to configure H2 in-memory database.

### [Milestone 2 – GitHub, Postgres & Test Folder Restoration]

- Created GitHub repo and pushed initial scaffold.
- Provisioned free PostgreSQL on Render (`pennywise-db`) and noted host/port/credentials.
- Added Postgres driver to `pom.xml` and created `application-postgres.properties` with placeholders for env vars.
- Restored `backend/src/test/java/com/pennywise/backend` after it was accidentally moved.

### [Milestone 2 – Supabase Integration]

- Replaced Render-hosted PostgreSQL with Supabase Postgres.
- Updated `application-postgres.properties` to read `${PENNYWISE_DB_URL}`, `${PENNYWISE_DB_USER}`, `${PENNYWISE_DB_PASSWORD}` from environment.
- Verified successful connection to Supabase (HikariPool-1 added a PgConnection).
- Confirmed that the Spring Boot app starts against Supabase without errors.
