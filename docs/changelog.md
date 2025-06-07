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

### [Milestone 4 – Transaction Feed]

- Created `Transaction` JPA entity (`com.pennywise.model.Transaction`) with fields: `id`, `userId`, `date`, `amount`, `category`, `merchant`, `lat`, `lng`, `description`.
- Added `TransactionRepository` (extends `JpaRepository<Transaction, UUID>`) with `findAllByUserIdOrderByDateDesc(UUID)`.
- Implemented `TransactionService`:
  - `getTransactionsForUser(UUID)` returns all transactions for a user sorted by date descending.
  - `createTransaction(Transaction)` saves a new transaction.
  - `getUserIdByEmail(String)` helper to map email → userId.
- Added `TransactionRequest` and `TransactionResponse` DTOs for JSON request/response.
- Built `TransactionController`:
  - `GET /api/transactions` → returns authenticated user’s transactions (200 OK, empty array if none).
  - `POST /api/transactions` → creates a new transaction (201 Created, returns saved record).
- Seeded mock data via `DataSeeder` (20,000 transactions per user), then disabled it once data was inserted.

### [Milestone 5 – Savings Goals]

- Created `Goal` JPA entity (`com.pennywise.model.Goal`) with fields: `id`, `userId`, `targetAmount`, `categoryFocus`, `startDate`, `endDate`.
- Added `GoalRepository` (extends `JpaRepository<Goal, UUID>`) with `findByUserId(UUID)`.
- Implemented `GoalService`:
  - `createOrUpdateGoal(UUID, BigDecimal, String, Instant, Instant)` to insert or update a user’s goal.
  - `getGoalForUser(UUID)` to fetch the current goal.
- Defined DTOs in `com.pennywise.controller`:
  - `GoalRequest` for incoming JSON (`targetAmount`, `categoryFocus`, `startDate`, `endDate`).
  - `GoalResponse` for outgoing JSON (`id`, `targetAmount`, `categoryFocus`, `startDate`, `endDate`).
- Built `GoalController`:
  - `POST /api/goals` → set or update the authenticated user’s savings goal (201 Created or 200 OK + JSON).
  - `GET  /api/goals` → retrieve the authenticated user’s current goal (200 OK + JSON or 404 if none).
- Confirmed:
  - App starts without errors and a `goals` table is created.
  - Endpoints enforce JWT protection and manual curl tests returned expected 404/201/200.

### [Milestone 6 – Dashboard Visualization]

- Defined `CategorySummary` DTO (`com.pennywise.controller.CategorySummary`) with fields `category` and `totalAmount`.
- Defined `DailySummary` DTO (`com.pennywise.controller.DailySummary`) with fields `date` and `totalAmount`.
- Extended `TransactionRepository`:
  - `findCategorySummaries(UUID, Instant)` via JPQL to sum spend per category.
  - `findDailySummariesRaw(UUID, Instant)` via native SQL to return raw rows of `(date, sum)`.
  - Restored `findAllByUserIdOrderByDateDesc(UUID)` for transactions API.
- Implemented `ChartService`:
  - `getCategoryBreakdown(UUID, int days)` to fetch and return category summaries for last N days.
  - `getDailySpending(UUID, int days)` to fetch raw daily rows and map them into `DailySummary` objects.
- Built `ChartController`:
  - `GET /api/charts/categories` → returns JSON array of category spend over last 30 days.
  - `GET /api/charts/daily` → returns JSON array of daily spend totals over last 30 days.
- Confirmed:
  - `curl` tests returned `200 OK` with appropriate JSON for both endpoints.
  - Data matches manual SQL queries and expected date ranges.
