Milestone 3 – User Authentication & JWT Filters has been completed.

**Next:** Milestone 4 – Transaction Feed

- Create `Transaction` JPA entity with fields: `id`, `user_id`, `date`, `amount`, `category`, `merchant`, `lat`, `lng`, `description`.
- Add `TransactionRepository`, `TransactionService`, and `TransactionController`.
- Seed mock transaction data (20k+ entries per user).
- Expose:
  - `GET /api/transactions` – return authenticated user’s transaction list.
  - `POST /api/transactions` – add a new transaction.
- Ensure these endpoints are protected by JWT.
