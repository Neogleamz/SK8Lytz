---
trigger: always_on
---

# Kanban Constitution & Task Schema

**North Star**: Every pattern the app shows on screen is byte-for-byte what the skates play. No guesswork. No baked-in firmware mystery. Full color control. One BLE packet. Forever.
**Hardware Model**: The controller is a PLAYBACK ENGINE. You send it a pixel array or effect config ONCE. It animates forever with zero further BLE. The only exception is music mode (`0x74` magnitude stream — unavoidable). Everything else is fire-and-forget.

## Hard Rules (Always Active)

**1. The Active Sprint Mandate:**
- STRICTLY FORBIDDEN from writing code for tasks outside `🚧 ACTIVE SPRINT`.
- ONE task at a time. Every task MUST use a Git Worktree.
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
