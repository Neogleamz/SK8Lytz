---
description: Intake an idea, draft a plan, generate the slug, and append to bucket list
---

1. **The Intercept**: If the user asks for a feature/fix casually, halt and route it through intake: "Intercepting request. Routing through formal intake..."
2. **Generate Slug**: e.g., `feat/` for new, `fix/` for bugs, `refactor/` for tech debt.
3. **Determine Classification**: Rank: [☁️ CLOUD|🧪 LAB], [⚠️ H-RISK|✅ L-RISK], [🍪 Snack|🍱 Meal|🥩 Feast], recommended [🤖 MODEL]. You MUST assign a Domain Tag (`[UI]`, `[DB]`, `[BLE]`, `[SCRAPER]`, etc.).
4. **Determine State Tag**:
   - Assign `[📝 NEEDS PLAN]` to standard tasks.
   - Assign `[🕵️ SPIKE]` to time-boxed exploratory R&D tasks.
   - Assign `[Friction: 1]` to tech debt/cleanup chores.
   - Assign `[⏳ BLOCKED BY: <slug>]` if the task relies on a prerequisite.
5. **Draft the Plan**: Autonomously write a markdown plan and save it to `docs/plans/<generated-slug>.md`. Once drafted, you may upgrade the tag from `[📝 NEEDS PLAN]` to `[✅ READY]`.
6. **Format the Item**:
    You MUST format the task strictly as a multi-line nested list. Single-line formatting is forbidden:
    ```markdown
    - [ ] **`slug`**
      - **Tags:** `[Status]` `[Layer]` `[Risk]` `[Size]` `[Cognitive Load]`
      - **Plan:** 📎 [PLAN-<slug>.md](./plans/PLAN-<slug>.md)
      - **Goal:** One-sentence primary objective.
      - **Details:** Comprehensive architectural details, constraints, or context.
    ```
7. **Determine Placement**: Append the item to `tools/SK8Lytz_Bucket_List.md` based on category:
   - `🚑 TRIAGE QUEUE`: Bugs and broken functionality.
   - `🏗️ THE ROADMAP`: New features and Epics.
   - `🧹 TECH DEBT`: Architectural issues and friction items.
8. **The Priority Override (Zero-Bypass Integration)**: If the user explicitly says "on deck" or "up next", draft the plan, mark it `[✅ READY]`, and place it in the `🔥 ON DECK` queue. Do NOT create a branch or worktree — branch creation is gated by Safety Rule 6 and requires an explicit execution trigger phrase from the user.
