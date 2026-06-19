# /intake — Task Intake Workflow

**Description:** Intake an idea, draft a plan, generate the slug, and append to bucket list.
**Persona:** 🎯 PM — Jordan

> **🎯 PM — Jordan | Intake Workflow Active**
> *Jordan holds the mic from idea capture through routing. Steps hand off to Casey (sprint logic), Quinn (plan drafting), and Reyes (evidence documentation).*

---

### Step 0 — Jordan Board-First Check (MANDATORY, NO SKIP)
Before adding any task, Jordan reads the board to avoid adding noise to an already busy sprint.

Read `docs/SK8Lytz_Bucket_List.md` — specifically ACTIVE SPRINT and ON DECK.
- Is there an active task in progress? → Warn the user: *"Active sprint task in progress: [slug]. Are you sure you want to add another task now, or should we route this to TRIAGE/ROADMAP for later?"*
- Any ON DECK tasks missing `Decision Log:`? → Note them — the new task might unblock one of these instead.
- Output sprint slot status: *"Sprint slot: [AVAILABLE / OCCUPIED BY: slug]"*

---

### Step 0.5 — Reyes Benchmarking Gate (MANDATORY)
Before any feature classification or planning begins, Reyes MUST establish the industry gold standard.

**0.5a. KB-First Check (MANDATORY — before any web search):**
Reyes reads `tools/knowledge-base/INDEX.md` and searches for entries matching the task's domain tags.
- **CURRENT entry found** → cite it directly in the benchmark output. Skip the web search for that specific topic.
- **STALE/CRITICAL entry found** → note the staleness prominently, then re-search and run `/kb-capture` to update.
- **No entry found** → proceed with `search_web` AND run `/kb-capture` before handing to Quinn.

1. Use `search_web` to research how 5 top-tier companies/apps solve this exact problem. **Skip for any topic already CURRENT in the KB.**
2. Synthesize their approaches into a new entry in `docs/INDUSTRY_BENCHMARKS.md`. AND run `/kb-capture` targeting `knowledge-base/patterns/` for the raw research.
3. If no clear gold standard exists, extract the closest analog.
4. Output a summary: *"Industry Benchmark complete. [Company A] does X, [Company B] does Y. We will adopt Z as our gold standard."*

---

### Steps 1–3 — PM Jordan: Product Intercept & Classification

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

### Steps 3.5–4.6 — Casey: Gating, Batch Routing & Placement

3.5 **[PREREQUISITE GATE]** Ask: "Does this task require any other task or batch to be merged first?"
   - **YES** → assign `[⏳ BLOCKED BY: <slug-or-batch-name>]`. Record the dependency. Skip to Step 5.
   - **NO** → proceed to Step 4.5 (batch eligibility analysis).

4. **Determine State Tag**:
   - Assign `[📝 NEEDS PLAN]` to standard tasks.
   - Assign `[🕵️ SPIKE]` to time-boxed exploratory R&D tasks.
   - Assign `[Friction: 1]` to tech debt/cleanup chores.

4.5 **[BATCH ELIGIBILITY — AST Collision Check]** For every file this task primarily modifies, run:
   ```
   node tools/ast-parser.js --files <file1> <file2> ...
   ```
   Cross-reference against all existing `[WAVE:N]` tasks. Import-tree overlap → assign to lowest wave with no overlap. Manual file-name comparison is **FORBIDDEN**.

4.5b **[WAVE ASSIGNMENT]** Assign the lowest wave where no existing task shares an import dependency. Add/update the row in the Batch Strategy Table. Output: `"Wave assignment: [WAVE:N] — verified by AST analysis."`

4.6 **[BATCH GROUP ASSIGNMENT]** Determine batch membership. **UPDATE the Batch Strategy Table** — add or update the row for the assigned batch.

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 📋 Scrum — Casey → 📐 TPM — Quinn
Completed: Batch routing confirmed, prerequisite gates cleared, state tag assigned.
Picking up: Plan document generation and SoT citations.
Context: Batch assignment and file-conflict status locked. Quinn drafts the PLAN-*.md.
─────────────────────────────────────────────────────────────────────
```

---

### Step 5 — Quinn: Plan Generation

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
Context: The plan exists structurally. Reyes now anchors it to real evidence.
─────────────────────────────────────────────────────────────────────
```

---

### Step 5.5 — Reyes: Evidence & WHY Documentation

**Reyes Knowledge-First Check (MANDATORY):**
Announce: *"Checking what we already know..."*
Search `docs/SESSION_LOG.md` for prior findings related to this task's domain.
- If prior [DECISION] or [ARTIFACT] entries exist → cite them in the Decision Log directly.

5.5. **Capture the WHY (Decision Log)**: Before writing the task entry, ask:
   - *"What specific evidence, failure, or observation made this task necessary?"*
   - *"What did we consider and reject?"*
   Write the answers as the `Decision Log:` and `Analysis:` fields inline in the task.
   **If spawned from a deep analysis session:** Link to the source analysis artifact in BOTH the batch header `Source Analysis:` field AND each task's `Analysis:` field.
   **If speculative/no evidence yet:** Decision Log = "Hypothesis — needs spike to verify."
   After capturing WHY, write a `[ARTIFACT]` entry to SESSION_LOG linking to the new plan file.

```
─────────────────────────────────────────────────────────────────────
🤝 HANDOFF: 🕵️ Scout — Reyes → 📋 Scrum — Casey
Completed: Decision Log and Analysis fields populated with verified evidence. SESSION_LOG updated.
Picking up: Task formatting (schema compliance) and backlog placement.
Context: Evidence chain is locked. Casey formats the task entry per schema and routes it.
─────────────────────────────────────────────────────────────────────
```

---

### Steps 6–8 — Casey: Format, Place & Prioritize

6. **Format the Item** — strictly as a multi-line nested list:
    ```markdown
    - [ ] **`slug`**
      - **Tags:** `[Status]` `[Layer]` `[Risk]` `[Size]` `[Cognitive Load]` `[BATCH:<name>]` `[WAVE:N]`
      - **Goal:** One-sentence primary objective.
      - **Decision Log:** ONE sentence — WHY this task exists right now.
      - **Analysis:** 📊 Source: [<analysis-artifact>.md](<link>) · Plan: [PLAN-<slug>.md](./plans/PLAN-<slug>.md)
        Key finding: "<one line>"
        Rejected alternative: "<what we considered and ruled out, and why>"
      - **Source of Truth:** 📖 [Filename](file:///path#L123) §Section
      - **Details:** Architectural constraints, platform guards, protocol limits, dependencies.
    ```

7. **Determine Placement** — route to correct section based on category:
   - `🚑 TRIAGE QUEUE`: Bugs and broken functionality.
   - `🏗️ THE ROADMAP`: New features and Epics.
   - `🧹 TECH DEBT`: Architectural issues and friction items.
   - **ON DECK Promotion Gate (Hard Rule)**: A task CANNOT be placed in `🔥 ON DECK` unless it has status `[✅ READY]`, a complete `PLAN-*.md` file, a filled `Decision Log:`, and at minimum `[🤔 INFERRED]` verification status.

8. **The Priority Override**: If the user explicitly says "on deck" or "up next", draft the plan, mark it `[✅ READY]`, and place it in the correct `🔥 ON DECK` batch group. Do NOT create a branch or worktree — branch creation requires an explicit execution trigger phrase.
