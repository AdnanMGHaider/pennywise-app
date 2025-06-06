Milestone 4 – Transaction Feed has been completed.

**Next:** Milestone 5 – Savings Goals

- Create `Goal` JPA entity with fields: `id`, `user_id`, `target_amount`, `category_focus`, `start_date`, `end_date`.
- Add `GoalRepository`, `GoalService`, and `GoalController`.
- Expose:
  - `POST /api/goals` – set or update a user’s savings goal.
  - `GET /api/goals` – retrieve the authenticated user’s current goal.
- Ensure these endpoints are protected by JWT.
