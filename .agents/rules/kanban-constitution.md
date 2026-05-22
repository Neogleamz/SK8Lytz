---
trigger: always_on
---

# Kanban Constitution & Task Schema

**North Star**: Every pattern the app shows on screen is byte-for-byte what the skates play. No guesswork. No baked-in firmware mystery. Full color control. One BLE packet. Forever.
**Hardware Model**: The controller is a PLAYBACK ENGINE. You send it a pixel array or effect config ONCE. It animates forever with zero further BLE. The only exception is music mode (`0x74` magnitude stream — unavoidable). Everything else is fire-and-forget.

**1. The Active Sprint Mandate:**
- Agents are STRICTLY FORBIDDEN from writing code for tasks outside the `🚧 ACTIVE SPRINT`.
- Limit: ONE task at a time. Every task MUST use a Git Worktree.
- Zero-Collateral Damage: No unsolicited refactoring. Log bugs to Triage; do not fix silently.

**2. The Kanban Flow (Definitions):**
- **🚑 TRIAGE QUEUE**: New bugs/ideas. Nothing skips the line.
- **🚧 ACTIVE SPRINT**: The single active worktree.
- **🔥 ON DECK**: The ordered queue of ready tasks.
- **🏗️ ROADMAP**: Epics and planned feature work.
- **🧹 TECH DEBT**: Chore-level tasks and refactors.
- **📦 ARCHIVE**: Completed `[x]` tasks.

**3. Task Definition Schema (The 6 Tags):**
Every task MUST include exactly one tag from each of the following 6 categories:

**A. [Status]: Current pipeline gate.**
- `[✅ READY]`: Fully planned, cleared for the Senior Developer to execute.
- `[📝 NEEDS PLAN]`: If plan doesnt exist,TPM must draft an AI-First Plan before any code is written.
- `[🕵️ SPIKE]`: Research, prototyping, or feasibility check required.
- `[⏳ BLOCKED]`: Cannot proceed until dependency is cleared.

**B. [Layer]: Primary domain affected.**
- `[UI]`: Visual components, layouts, styling (Triggers Apple Designer persona).
- `[CORE]`: Local state, hooks, utility math, or local DB.
- `[CLOUD]`: Touches Supabase, Auth, remote database schema, or APIs.
- `[LAB]`: Touches Hardware, BLE, or requires physical ADB device testing.

**C. [Risk]: Collateral damage potential.**
- `[L-RISK]`: Isolated changes, highly unlikely to break other systems.
- `[M-RISK]`: Touches shared state, hooks, or heavily used components.
- `[H-RISK]`: High blast radius (e.g., auth, BLE protocol, core architecture). Triggers Surgeon Persona.

**D. [Size]: Estimated time to complete.**
- `[Snack]`: Quick fix, UI tweak, or simple bug (Minutes/Hours).
- `[Meal]`: Standard feature or refactor (Days).
- `[Feast]`: Multi-day Epic requiring deep architectural focus. Triggers Devil's Advocate.

**E. [Cognitive Load]: Mental capacity required.**
- `[🤖 FLASH]`: Routine execution, boilerplate, simple logic (Autopilot enabled).
- `[🤖 PRO-HIGH]`: Standard feature execution, complex state management.
- `[🤖 THINK]`: Deep architectural planning, complex math. Triggers Rubber Duck ELI5.

**F. [Verification Status]: Source of Truth verification level.**
- `[✅ VERIFIED]`: The Source of Truth is fully verified by direct physical device testing, BLE HCI sniffs, or codebase lines.
- `[🤔 INFERRED]`: The Source of Truth is logically inferred but lacks physical validation or direct source-of-truth file lines.
- `[❌ UNVERIFIED]`: No verified file-backed or physical evidence exists yet. The task MUST run as a `[🕵️ SPIKE]` first.

**4. Batch Execution Protocol:**
- A **Batch** is a named group of tasks sharing an execution context. Tag: `[BATCH:<name>]`.
- **⚡ Parallel Batch**: Tasks touch DIFFERENT files → agent spawns MULTIPLE worktrees simultaneously (one for each task slug).
- **📋 Sequential Batch**: Tasks touch SHARED files → agent runs ONE task at a time, in listed order. Each task gets its OWN worktree based on its slug.
- The agent determines parallel vs. sequential via file-conflict scan at intake. Never assume.
- **Blocked Rule**: Tasks tagged `[⏳ BLOCKED BY: <name>]` are HARD GATED — agents MUST refuse to start them until the named dependency is merged. No exceptions.
- **Sprint Complete Rule**: Active Sprint is DONE only when ALL tasks nested under the active batch are `[x]`.

**5. ON DECK Structure Law:**
- ON DECK is organized by **batch group headers** (`### [BATCH:<name>]`), NOT a flat task list.
- Each header MUST show: batch name, ⚡ (parallel-safe) or ⏳ (blocked), and prerequisite.
- Agents MUST insert new tasks under their batch group header — standalone flat inserts are FORBIDDEN.
- The **Batch Strategy Table** (in Triage Queue) is the authoritative execution order map. Agents MUST update it whenever a new batch is created or a task is assigned to a batch.
- **Multi-Worktree Exception**: The "ONE active sprint" rule allows MULTIPLE simultaneous worktrees ONLY IF the agent has verified zero file overlap between all tasks in the batch.

**6. Strict Task Schema (No Single-Line Tasks):**
EVERY task must strictly follow this nested structure:
- [ ] **`domain/slug-name`**
  - **Tags:** `[Status]` `[Verification Status]` `[Layer]` `[Risk]` `[Size]` `[Cognitive Load]`
  - **Plan:** 📎 [PLAN-slug-name.md](./plans/PLAN-slug-name.md)
  - **Source of Truth:** 📖 [Filename](file:///absolute/path/to/file#L123) §Section or [Reference](file:///absolute/path/to/doc.md#L45)
  - **Goal:** Human-readable detailed description of the user's goal.
  - **Details:** Comprehensive architectural details, constraints, or context.

**7. Completion Stamp Law (Mandatory on `[x]`):**
When marking any task `[x]`, the agent MUST append the short merge commit hash and a one-line outcome summary inline on the slug line:
- [x] **`domain/slug-name`** — `<one-line outcome>`. Merged `<7-char hash>`.
  - **Details:** *(update to "COMPLETE — ..." with key decisions, files changed, and any follow-up items logged to backlog.)*
The hash is obtained via `git log -1 --format="%h"` immediately after the merge commit.

**8. The Source of Truth (SoT) Law:**
- EVERY active task MUST have a dedicated `Source of Truth:` field.
- The SoT must cite exact files, line numbers, API references, or hardware protocol sections (e.g., `ZENGGE_PROTOCOL_BIBLE.md` §11 or `src/hooks/useBLE.ts#L706`) that prove the technical necessity or payload correctness of the task.
- Agents are STRICTLY FORBIDDEN from starting work on a task if its `Source of Truth` contains placeholders like `[PENDING]` or generic references (e.g., "the internet").
- This field serves as the execution anchor for our "Self-Doubt Protocol," ensuring zero hallucinations occur during implementation.
