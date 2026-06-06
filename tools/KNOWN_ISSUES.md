# Known Issues & Victory Snapshots

This file is a reference document — NOT always-on. Read it when you hit a specific failure pattern.
Moved from `safety-protocol.md` to reduce ambient context overhead.

---

## 🏆 VS-001: Parallel Worktree Gatekeeper Divergence (2026-05-26)

**Symptom**: Running the fortress-gatekeeper with 2+ active worktrees causes the second worktree's branch to be silently deleted even when its merge fails. The commit becomes an orphan recoverable only via `git reflog` + `cherry-pick`.

**Root Cause**: The old gatekeeper's `foreach` loop merged worktrees sequentially. After worktree 1 merged, master moved ahead by 1 commit. Worktree 2's branch still had the OLD parent, so `--ff-only` failed. The script then called `git worktree remove` and `git branch -D` **regardless of merge success**.

**Fix Applied**: `tools/fortress-gatekeeper.ps1` now:
1. Rebases each branch onto current master HEAD before merging
2. Captures the `--ff-only` exit code — on failure: HALT + preserve branch + skip via `continue`
3. Only tears down worktree/branch if merge **succeeded**

**⛔ Operational Rule**: NEVER create two worktrees from the same base commit and run the gatekeeper expecting both to merge cleanly in one pass. Create worktree 1 → merge it → THEN create worktree 2.

---

## 🏆 VS-002: Gitignore Shadow Zone — Lost Wear OS Files (2026-06-03)

**Symptom**: Files created in a worktree pass all tooling. But `git add .` silently ignores them. After the gatekeeper merges and deletes the worktree, files are permanently lost — zero trace in git history.

**Root Cause**: Root `.gitignore` line 44 contained `/android` — ignoring the entire `android/` directory. Git negation rules (`!android/sk8lytzWear/`) didn't override this because the parent was already excluded.

**Fix Applied**:
1. Added `!/android/sk8lytzWear/` negation to root `.gitignore`
2. Used `git add --force android/sk8lytzWear/` for the initial track
3. Future commits track normally after force-add

**⛔ Operational Rule**: Before committing in ANY worktree, ALWAYS run `git status --short` and verify file count matches changes. If files are missing, run `git check-ignore -v <path>`. NEVER assume `git add .` captured everything.

---

## 🏆 VS-003: Documentation Drift — 16 Commits of Silent Staleness (2026-06-06)

**Symptom**: `SK8Lytz_App_Master_Reference.md` declared heartbeat "DELETED" while live code had a new `useBLEHeartbeat` hook. Hook Registry claimed "18 Hooks" when 24+ existed. Auto-Recovery documented wrong backoff values.

**Root Cause**: Zero documentation gates existed in the pipeline. Every checkpoint verified CODE correctness but NONE checked documentation correctness. 16 consecutive commits of architectural changes landed with zero docs updates.

**Fix Applied**:
1. Kanban Constitution Rule 6 — docs parity pointer to `/start-task` Phase 6
2. `/start-task` Phase 5.5 — explicit documentation parity check
3. `/diff-review` — added "📖 Documentation Parity" row
4. `agent-behavior.md` — VS-003 evolved rule
5. `/scaffold-hook` — Hook Registry update step added

**⛔ Operational Rule**: When completing ANY task that creates new hooks, services, components, or modifies BLE architecture/protocol/types, update `tools/SK8Lytz_App_Master_Reference.md` BEFORE running the gatekeeper. State: `"Documentation parity check: [sections updated]"` or `"no architectural changes — docs gate skipped"`.
