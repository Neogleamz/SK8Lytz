---
trigger: always_on
---

# Kanban Constitution & Task Schema

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
Full tag definitions and task schema → see `/intake` workflow.
Completion stamp protocol + pre-merge verification matrix → see `/start-task` workflow Phase 6.

**7. The No-Placeholder Plan Law (VS-013):**
- Tasks MUST NOT be appended to the Bucket List with `*(pending)*` plans or `[❌ UNVERIFIED]` status.
- Every new task MUST go through an explicit planning workflow (like `/intake`) to generate an approved `PLAN-*.md` BEFORE it enters the `TRIAGE QUEUE`. No unverified brain-dumps.

**8. Parallel Wave Safety (VS-014):**
- When a synthesis workflow (e.g., `/deepdive-code-synthesis`) generates multiple task clusters from the same codebase sweep, the agent MUST perform a **file-collision analysis** before writing tasks to the Bucket List.
- **The Collision Check:** For every pair of clusters, compute the intersection of their `Affected Files` lists. Any pair sharing ≥1 file is a **collision pair** and CANNOT run in parallel worktrees.
- **Wave Assignment:** Assign each cluster a `[WAVE:N]` tag using graph coloring (greedy): collision pairs must be in different waves. Tasks with zero collisions may share Wave 1.
- **The Wave Tag Format:** Add `[WAVE:N]` to the Tags line of every synthesized task. Add `Prerequisite: Wave N-1 fully merged` to the task's Details field for any Wave N > 1.
- **The Batch Strategy Table:** Every multi-cluster synthesis MUST output a Batch Strategy Table in the Bucket List anchor block:
  ```
  | Wave | Tasks | Parallel-Safe? | Prerequisite |
  |------|-------|---------------|-------------------|
  | 1    | A, B  | ✅ Yes         | None              |
  | 2    | C     | N/A (solo)     | Wave 1 merged     |
  ```
- ⛔ FORBIDDEN: Assigning `[WAVE:1]` to two tasks that share a file. This is the sub-agent equivalent of VS-001 (parallel worktree gatekeeper divergence).
