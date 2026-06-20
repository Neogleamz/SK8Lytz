# Kanban Constitution & Task Management
*Source: `.agents/rules/kanban-constitution.md`*

**North Star**: Every pattern the app shows on screen is byte-for-byte what the skates play. No guesswork. No baked-in firmware mystery. Full color control. One BLE packet. Forever.

**Hardware Model**: The controller is a PLAYBACK ENGINE. You send it a pixel array or effect config ONCE. It animates forever with zero further BLE. The only exception is music mode (`0x74` magnitude stream — unavoidable). Everything else is fire-and-forget.

## Hard Rules (Always Active)

**1. The Active Sprint & Batching Mandate:**
- STRICTLY FORBIDDEN from writing code for tasks outside `🚧 ACTIVE SPRINT`.
- By default, ONE task at a time per Git Worktree.
- **The Unified Batch Override:** If multiple tasks belong to the same `[BATCH:...]`, are tagged `[Snack]` or `[Meal]`, and share the same domain (no architectural conflicts), they MAY be executed in a single unified worktree (`<batch-slug>-batch`) and verified collectively.
- **Wave Ordering (see Rule 8):** When a `[WAVE:N]` tag is present, tasks in Wave N MUST NOT start until all Wave N-1 tasks are merged into master. Wave tags override batch grouping for execution order.
- Zero-Collateral Damage: No unsolicited refactoring. Log bugs to Triage; do not fix silently.

**2. The Kanban Flow (Vocabulary):**
- **🚑 TRIAGE QUEUE**: New bugs/ideas. Nothing skips the line.
- **🚧 ACTIVE SPRINT**: The single active worktree.
- **🔥 ON DECK**: The ordered queue of ready tasks.
- **🏗️ ROADMAP**: Epics and planned feature work.
- **🧹 TECH DEBT**: Chore-level tasks and refactors.
- **📦 ARCHIVE**: Completed `[x]` tasks.

**3. The Source of Truth (SoT) Law:**
- EVERY active task MUST have a `Source of Truth:` field citing exact files and line numbers.
- STRICTLY FORBIDDEN from starting work if SoT contains `[PENDING]` or generic references.

**4. Parallel Worktree Safety (VS-001):**
- ⛔ FORBIDDEN: Two worktrees from the same base commit through the gatekeeper in one pass.
- Safe Pattern: Create worktree 1 → merge → THEN create worktree 2 from new master HEAD.

**5. The Unverified Task Spike Gate:**
- Tasks tagged `[❌ UNVERIFIED]` are HARD BLOCKED. Run a `[🕵️ SPIKE]` first. No exceptions.

**6. The 6 Required Task Tags:**
Every task needs exactly one from each: `[Status]` `[Verification Status]` `[Layer]` `[Risk]` `[Size]` `[Cognitive Load]`

**7. The No-Placeholder Plan Law (VS-013):**
- Tasks MUST NOT be appended to the Bucket List with `*(pending)*` plans or `[❌ UNVERIFIED]` status.
- Every new task MUST go through an explicit planning workflow (like `/intake`) to generate an approved `PLAN-*.md` BEFORE it enters the `TRIAGE QUEUE`.

**8. Parallel Wave Safety (VS-014) — AST-Verified:**
- When a synthesis workflow generates multiple task clusters, the agent MUST run the AST collision tool BEFORE writing tasks to the Bucket List:
  ```
  node tools/ast-parser.js --collision-matrix <domain_clusters.json>
  ```
- **Wave Assignment:** Assign each cluster a `[WAVE:N]` tag from the tool's `wave_assignments` output. Manual guessing is FORBIDDEN.
- **The Wave Tag Format:** Add `[WAVE:N]` to the Tags line of every synthesized task.
- ⛔ FORBIDDEN: Assigning `[WAVE:1]` to two tasks that share an import dependency.

**9. The Plan Completeness Gate (Anti-Skimping Law):**
- STRICTLY FORBIDDEN from marking a task as `[x]` or merging via Gatekeeper if the worktree diff does not explicitly cover EVERY file listed in the `PLAN-*.md`.
- If a file in the plan was intentionally skipped, you MUST append a `// SKIPPED: [reason]` addendum to the plan file before merging.

**10. The Parallel Swarm Limit (CPU Protection):**
- STRICTLY FORBIDDEN from assigning more than 8 parallel tasks to a single `[WAVE:N]` tag.

**11. The Pre-Execution Intake Checklist Gate (No-Launch-Without-Clearance):**
- `/start-task` and `/goal` are STRICTLY FORBIDDEN from creating a worktree or invoking a subagent for any task that fails ANY of these checks:
  1. ✅ Task status is `[✅ READY]`
  2. ✅ `Source of Truth:` field is present and does NOT contain `[PENDING]`
  3. ✅ `PLAN-*.md` file exists at the path listed in `Source of Truth`
  4. ✅ `Decision Log:` field is filled — not empty, not `"TBD"`
  5. ✅ `[WAVE:N]` tag is present and was assigned by `ast-parser.js`
  6. ✅ For Wave N > 1: all Wave N-1 tasks are confirmed merged to master via `git log --oneline -5`
