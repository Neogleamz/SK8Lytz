---
description: Intake an idea, draft a plan, generate the slug, and append to bucket list
---

1. **The Intercept**: If the user asks for a feature/fix casually, halt and route it through intake: "Intercepting request. Routing through formal intake..."
2. **Generate Slug**: e.g., `feat/` for new, `fix/` for bugs, `refactor/` for tech debt.
3. **Determine Classification**: Rank: [☁️ CLOUD|🧪 LAB], [⚠️ H-RISK|✅ L-RISK], [🍪 Snack|🍱 Meal|🥩 Feast], recommended [🤖 MODEL]. You MUST assign a Domain Tag (`[UI]`, `[DB]`, `[BLE]`, `[SCRAPER]`, etc.).

3.5 **[PREREQUISITE GATE]** Ask: "Does this task require any other task or batch to be merged first?"
   - **YES** → assign `[⏳ BLOCKED BY: <slug-or-batch-name>]`. Record the dependency. Skip to Step 5.
   - **NO** → proceed to Step 4.5 (batch eligibility analysis).

4. **Determine State Tag**:
   - Assign `[📝 NEEDS PLAN]` to standard tasks.
   - Assign `[🕵️ SPIKE]` to time-boxed exploratory R&D tasks.
   - Assign `[Friction: 1]` to tech debt/cleanup chores.
   - Assign `[⏳ BLOCKED BY: <slug>]` if the task relies on a prerequisite (confirmed in Step 3.5).

4.5 **[BATCH ELIGIBILITY — File Conflict Scan]** Ask: "What files does this task primarily modify?"
   - Cross-reference the **Batch Strategy Table** in the Triage Queue header.
   - **File overlap with a named batch** → this task is SEQUENTIAL to that batch:
       Assign `[⏳ BLOCKED BY: <batch-name>]`. Proceed to Step 5.
   - **No file overlap** → task is PARALLEL-SAFE. Proceed to Step 4.6.

4.6 **[BATCH GROUP ASSIGNMENT]** Determine batch membership:
   - **Parallel-safe with an existing ON DECK batch** (same epic, same worktree intention) → reuse `[BATCH:<name>]` tag.
   - **New domain/tier with likely siblings** → propose a new batch name (agent suggests, user confirms).
   - **Truly standalone** (no siblings, no shared dependency chain) → no `[BATCH]` tag. Solo worktree.
   - **UPDATE the Batch Strategy Table** — add or update the row for the assigned batch (worktree slug, files touched, task list, prerequisite).

5. **Draft the Plan**: Autonomously write a markdown plan and save it to `docs/plans/<generated-slug>.md`. Once drafted, you may upgrade the tag from `[📝 NEEDS PLAN]` to `[✅ READY]`.
6. **Format the Item**:
    You MUST format the task strictly as a multi-line nested list. Single-line formatting is forbidden.
    The `[BATCH:<name>]` tag MUST be included in the Tags line if the task was assigned to a batch.
    ```markdown
    - [ ] **`slug`**
      - **Tags:** `[Status]` `[Layer]` `[Risk]` `[Size]` `[Cognitive Load]` `[BATCH:<name>]`
      - **Plan:** 📎 [PLAN-<slug>.md](./plans/PLAN-<slug>.md)
      - **Goal:** One-sentence primary objective.
      - **Details:** Comprehensive architectural details, constraints, or context.
    ```
7. **Determine Placement**: Route to the correct section based on category:
   - `🚑 TRIAGE QUEUE`: Bugs and broken functionality.
   - `🏗️ THE ROADMAP`: New features and Epics.
   - `🧹 TECH DEBT`: Architectural issues and friction items.
   - **BATCH RULE (overrides section routing for ON DECK placement)**:
     - If the task has a `[BATCH:*]` tag → scan `🔥 ON DECK` for an existing batch group header (`### [BATCH:<name>]`) with that name.
     - **FOUND** → INSERT the task under that header. Do NOT create a standalone entry.
     - **NOT FOUND** → CREATE a new batch group header in ON DECK, then insert the task beneath it. Use this format:
       ```
       ### ⚡/⏳ [BATCH:<name>] — `<worktree-slug>` — <status>
       > **Worktree**: `<worktree-slug>` · **Type**: Sequential|Parallel · **Prerequisite**: <batch-name or None>
       ```
8. **The Priority Override (Zero-Bypass Integration)**: If the user explicitly says "on deck" or "up next", draft the plan, mark it `[✅ READY]`, and place it in the correct `🔥 ON DECK` batch group. Do NOT create a branch or worktree — branch creation is gated by Safety Rule 6 and requires an explicit execution trigger phrase from the user.
