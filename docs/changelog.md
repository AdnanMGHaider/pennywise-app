### [Milestone 1 – Database Config]

- Removed PostgreSQL dependency and added H2 so the app starts without an external database.
- Replaced application.properties contents to configure H2 in-memory database.

### [Milestone 2 – GitHub, Postgres & Test Folder Restoration]

- Created GitHub repo and pushed initial scaffold.
- Provisioned free PostgreSQL on Render (pennywise-db) and noted host/port/credentials.
- Added Postgres driver to pom.xml and created application-postgres.properties with placeholders for env vars.
- Restored backend/src/test/java/com/pennywise/backend after it was accidentally moved.

### [Milestone 2 – Supabase Integration]

- Replaced Render-hosted PostgreSQL with Supabase Postgres.
- Updated application-postgres.properties to read `${PENNYWISE_DB_URL}`, `${PENNYWISE_DB_USER}`, `${PENNYWISE_DB_PASSWORD}` from environment.
- Verified successful connection to Supabase (HikariPool-1 added a PgConnection).
- Confirmed that the Spring Boot app starts against Supabase without errors.

### [Milestone 3 – User Authentication & JWT Filters]

- Created `User` JPA entity (`com.pennywise.model.User`) and confirmed `users` table appears in Supabase.
- Added `UserRepository` interface (extends `JpaRepository<User, UUID>`) with `findByEmail(...)`.
- Implemented `UserService` (handles password hashing, `registerUser(...)`, `findByEmail(...)`).
- Exposed `PasswordEncoder` via `PasswordConfig.java` and wired it into `UserService`.
- Added Spring Security setup:
  - `CustomUserDetailsService` to load users by email.
  - `JwtUtil` to generate/validate tokens (reads `app.jwt.secret` and `app.jwt.expiration-ms` from env).
  - `JwtAuthenticationFilter` to intercept `POST /api/auth/login` and issue a JWT on successful login.
  - `JwtAuthorizationFilter` to check Bearer token on every other `/api/**`.
  - `SecurityConfig` to disable CSRF, make sessions stateless, permit `/api/auth/register` & `/api/auth/login`, and protect all other `/api/**`.
- Confirmed:
  - `POST /api/auth/login` returns 401 for nonexistent user.
  - Registration endpoint (`POST /api/auth/register`) creates a new user (201).
  - Login with valid credentials returns 200 + JWT.
  - Calling a protected endpoint (e.g. `/api/transactions`) with that JWT yields 404 (authenticated but no handler yet).
