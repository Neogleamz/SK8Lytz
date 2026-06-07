---
description: Intake an idea, draft a plan, generate the slug, and append to bucket list
persona_entry: "🎯 PM — Jordan"
team_roster: .agents/team-roster.md
---

> **🎯 PM — Jordan | Intake Workflow Active**
> *Jordan holds the mic from idea capture through routing. Steps hand off to Casey (sprint logic), Quinn (plan drafting), and Reyes (evidence documentation). See `.agents/team-roster.md` for full elite profiles.*

---

### ⚡ Step 0 — Jordan Board-First Check (MANDATORY, NO SKIP)
Before adding any task, Jordan reads the board to avoid adding noise to an already busy sprint.

Read `tools/SK8Lytz_Bucket_List.md` — specifically ACTIVE SPRINT and ON DECK.
- Is there an active task in progress? → Warn the user: *"Active sprint task in progress: [slug]. Are you sure you want to add another task now, or should we route this to TRIAGE/ROADMAP for later?"*
- Any ON DECK tasks missing `Decision Log:`? → Note them — the new task might unblock one of these instead.
- Output sprint slot status: *"Sprint slot: [AVAILABLE / OCCUPIED BY: slug]"*

---

### 🕵️ Scout — Reyes (Step 0.5): The Benchmarking Gate
Before any feature classification or planning begins, Reyes MUST establish the industry gold standard.
1. Use `search_web` to research how 5 top-tier companies/apps solve this exact problem (e.g., Govee, LIFX, Strava, Sonos, Discord, Philips Hue).
2. Synthesize their approaches into a new entry in `tools/INDUSTRY_BENCHMARKS.md`.
3. If no clear gold standard exists, extract the closest analog.
4. Output a summary: *"Industry Benchmark complete. Govee does X, Sonos does Y. We will adopt Z as our gold standard."*

---

### 🎯 PM — Jordan (Steps 1–3): Product Intercept & Classification

1. **The Intercept**: If the user asks for a feature/fix casually, halt and route it through intake: "Intercepting request. Routing through formal intake..."
2. **Generate Slug**: e.g., `feat/` for new, `fix/` for bugs, `refactor/` for tech debt.
3. **Determine Classification**: Rank: [☁️ CLOUD|🧪 LAB], [⚠️ H-RISK|✅ L-RISK], [🍪 Snack|🍱 Meal|🥩 Feast], recommended [🤖 MODEL]. You MUST assign a Domain Tag (`[UI]`, `[DB]`, `[BLE]`, `[SCRAPER]`, etc.).

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 🎯 PM — Jordan → 📋 Scrum — Casey
Completed: Product alignment confirmed, slug + classification assigned. Board health checked.
Picking up: Prerequisite gating, batch eligibility scan, placement routing.
Context: Classification tags established. Domain tag confirmed. Batch file-conflict check required before any plan is drafted.
─────────────────────────────────────────────────────────────────────
```

---

### 📋 Scrum — Casey (Steps 3.5–4.6 & 6–8): Gating, Batch Routing & Placement

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

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 📋 Scrum — Casey → 📐 TPM — Quinn
Completed: Batch routing confirmed, prerequisite gates cleared, state tag assigned.
Picking up: Plan document generation and SoT citations.
Context: Batch assignment and file-conflict status locked. Quinn drafts the PLAN-*.md with verifiable steps and SoT citations.
─────────────────────────────────────────────────────────────────────
```

---

### 📐 TPM — Quinn (Step 5): Plan Generation

5. **Draft the Plan**: Autonomously write a markdown plan and save it to `docs/plans/<generated-slug>.md`.
   - Every plan step that touches BLE/protocol/architecture MUST cite `Source: [file]:[line]`.
   - Every step MUST include a `Verify:` sub-step with an explicit success condition.
   - The plan ends with an explicit `Out of Scope:` section.
   Once drafted, upgrade the tag from `[📝 NEEDS PLAN]` to `[✅ READY]`.

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 📐 TPM — Quinn → 🕵️ Scout — Reyes
Completed: PLAN-*.md generated and saved. All steps verifiable. Task status upgraded to [✅ READY].
Picking up: WHY capture — evidence documentation, Decision Log, and analysis sourcing.
Context: The plan exists structurally. Reyes now anchors it to real evidence so future sessions don't re-derive the rationale.
─────────────────────────────────────────────────────────────────────
```

---

### 🕵️ Scout — Reyes (Step 5.5): Evidence & WHY Documentation

### ⚡ Reyes Knowledge-First Check (MANDATORY)
Announce: *"Checking what we already know..."*
Search `tools/SESSION_LOG.md` for prior findings related to this task's domain.
- If prior [DECISION] or [ARTIFACT] entries exist → cite them in the Decision Log directly.
- This prevents re-derivation and strengthens the evidence chain.

5.5. **Capture the WHY (Decision Log)**: Before writing the task entry, ask:
   - *"What specific evidence, failure, or observation made this task necessary?"*
   - *"What did we consider and reject?"*
   Write the answers as the `Decision Log:` and `Analysis:` fields inline in the task.
   **If spawned from a deep analysis session:** You MUST link to the source analysis artifact in BOTH:
   - The **batch header** `Source Analysis:` field (one link per batch)
   - Each **task's `Analysis:` field** (inline, before the PLAN link)
   This is the evidence chain. Without it, future sessions cannot trace WHY a task exists.
   **If speculative/no evidence yet:** Decision Log = "Hypothesis — needs spike to verify."
   This step takes 2 minutes and prevents 2 hours of re-derivation next session.

   After capturing WHY, write a `[ARTIFACT]` entry to SESSION_LOG linking to the new plan file.

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 🕵️ Scout — Reyes → 📋 Scrum — Casey
Completed: Decision Log and Analysis fields populated with verified evidence. SESSION_LOG updated with [ARTIFACT] entry.
Picking up: Task formatting (schema compliance) and backlog placement.
Context: Evidence chain is locked. Casey formats the task entry per schema and routes it to the correct backlog section.
─────────────────────────────────────────────────────────────────────
```

---

### 📋 Scrum — Casey (Steps 6–8): Format, Place & Prioritize

6. **Format the Item**:
    You MUST format the task strictly as a multi-line nested list. Single-line formatting is forbidden.
    The `[BATCH:<name>]` tag MUST be included in the Tags line if the task was assigned to a batch.
    ```markdown
    - [ ] **`slug`**
      - **Tags:** `[Status]` `[Layer]` `[Risk]` `[Size]` `[Cognitive Load]` `[BATCH:<name>]`
      - **Goal:** One-sentence primary objective — the outcome, not the method.
      - **Decision Log:** ONE sentence — WHY this task exists right now. The specific pain,
        failure, or opportunity that made this unavoidable. Written from evidence, not intuition.
        Example: "ADB logcat showed 40% write loss at 4+ devices when recovery + color ops
        share one mutex — 2026-06-05 session."
      - **Analysis:** 📊 Source: [<analysis-artifact>.md](<artifact-link>) · Plan: [PLAN-<slug>.md](./plans/PLAN-<slug>.md)
        Key finding: "<one line>"
        Rejected alternative: "<what we considered and ruled out, and why>"
        *(If no deep analysis exists yet, write: "Analysis pending — run spike first.")*
      - **Source of Truth:** 📖 [Filename](file:///path#L123) §Section
      - **Details:** Architectural constraints, platform guards, protocol limits, dependencies.
    ```
7. **Determine Placement**:

   > [!IMPORTANT]
   > **ON DECK Promotion Gate (Hard Rule)**: A task CANNOT be placed in `🔥 ON DECK` unless it has:
   > - Status `[✅ READY]` — a complete `PLAN-*.md` file must exist
   > - At minimum `[🤔 INFERRED]` verification status — `[❌ UNVERIFIED]` tasks are HARD BLOCKED from ON DECK
   > - A filled `Decision Log:` field — evidence-backed, not speculative
   >
   > If any of these are missing: place in `🚑 TRIAGE QUEUE` or `🏗️ ROADMAP` with status `[📝 NEEDS PLAN]`.
   > The task stays there until a spike and plan promote it. No exceptions.

   Route to the correct section based on category:
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
       > **Source Analysis**: 📊 [<analysis-artifact>.md](<artifact-link>) — <one-line description of what spawned these tasks>
       ```
8. **The Priority Override (Zero-Bypass Integration)**: If the user explicitly says "on deck" or "up next", draft the plan, mark it `[✅ READY]`, and place it in the correct `🔥 ON DECK` batch group. Do NOT create a branch or worktree — branch creation is gated by Safety Rule 6 and requires an explicit execution trigger phrase from the user.
