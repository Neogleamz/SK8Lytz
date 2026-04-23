---
trigger: always_on
---

# Critical Safety & Security Priorities

**⛔ CRITICAL SAFETY RULE 1: Never edit, parse, or delete `.git/hooks/` files.**
**⛔ CRITICAL SAFETY RULE 2: Never push ANY changes to `master` without explicit user consent.**
**⛔ CRITICAL SAFETY RULE 3: Passphrase amnesia: never reuse an authorized passphrase outside its immediate contextual use.**
**⛔ CRITICAL SAFETY RULE 4: Always anchor your progress: run `git add .` + `git commit` to create local checkpoints after every discrete file edit. Immediately after creating a confirmed checkpoint, you MUST explicitly ask the user for permission to push to the remote repository.**
**⛔ CRITICAL SAFETY RULE 5: Keep `tools/SK8Lytz_Bucket_List.md` updated. It is now completely untracked locally (in .gitignore) to prevent branch drift. DO NOT try to stage or commit it via git.**
**⛔ CRITICAL SAFETY RULE 6: NEVER run `git checkout -b`, `git checkout <branch>`, `git switch`, or ANY branch-changing command in the main repository directory (`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz`). The main directory MUST always stay on `master`. All feature work MUST use `git worktree` to create an isolated directory. See the Worktree Isolation Protocol below.**
**⛔ CRITICAL SAFETY RULE 7: NEVER push code to the remote repository (i.e. 'release') without first explicitly executing the `/health-sweep` workflow to run the MCP Database Scanner and npm audit.**
**⛔ CRITICAL SAFETY RULE 8: The Core Fortress Mandate. NEVER perform unsolicited architectural refactors, hook extractions, or "Boy-Scout" cleanups on core stability systems (e.g., DashboardScreen.tsx, useBLE.ts, or the BLE/Group architecture). Unless explicitly commanded by the user, you are strictly prohibited from reorganizing working core logic. If it works, DO NOT TOUCH IT.**
**⛔ CRITICAL SAFETY RULE 9: Worktree Commit Is NOT Done. After committing the final change in a worktree branch, you MUST immediately merge to master, remove the worktree, and delete the branch — in that order — before reporting completion or asking the user anything else. A committed-but-open worktree is an incomplete task. No exceptions.**
**⛔ CRITICAL SAFETY RULE 10: Master Fortress File Lock. While ANY worktree is active, you are STRICTLY FORBIDDEN from editing, writing, or modifying ANY source file (`src/`, `tools/`, `.agents/`) under the master fortress directory (`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz`). ALL code edits MUST target the worktree path (`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\<slug>\`). Violating this causes branch divergence and forces a cherry-pick instead of a clean fast-forward merge. The only exception is `tools/SK8Lytz_Bucket_List.md` (gitignored). If you discover a bug in a file that is NOT part of the current worktree's scope, add it to the worktree's commit anyway — do NOT directly edit master.**
**⛔ CRITICAL SAFETY RULE 11: The Active Sprint Mandate. HARD REFUSE any requests to write code or modify files that do not directly pertain to the single task listed in the `🚧 ACTIVE SPRINT` section of the Bucket List. If the user asks for a random fix, reply: "That is out of scope. Use `/intake` to add it to the backlog, or swap out the active task."**
**⛔ CRITICAL SAFETY RULE 12: The Three-Strike Debugging Lockout. If you attempt to fix a specific bug 3 times and it still fails, you are forbidden from writing another line of code. You MUST automatically trigger a `/git-snapshot` to revert, stop execution, and force the conversation into `/brainstorming` mode.**
**⛔ CRITICAL SAFETY RULE 13: The Override Key. You will strictly enforce Rules 11 and 12 *unless* the user's prompt begins with the exact passphrase: `COWBOY MODE ACTIVATED`. This is the ONLY way the user can force you to bypass the Iron Boundaries for a hotfix.**


### Worktree Isolation Protocol (Mandatory Constraint)

The main repo directory (`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz`) is the **master fortress**. It NEVER leaves the `master` branch. All feature/fix work happens in isolated worktree directories.

**Worktree root**: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\`

> ⛔ **WORKTREE CONSTRAINT**: NEVER run `git checkout` or `git switch` manually on master. The procedural bash scripts for creating, syncing, and destroying worktrees are strictly enforced by the `/start-task` and `/git-ops` workflows. Do not invent your own Git commands. Follow the workflows.

> ⛔ **RULE 9 CHECKPOINT**: After a worktree is merged, `git worktree list` must show ONLY the master fortress. If any worktree remains, the task is NOT complete.

> ⛔ **RULE 10 COROLLARY — TSC Discipline**: `npx tsc --noEmit` MUST be run from the master directory (it has `node_modules`). When TSC reports errors in worktree files, **fix them inside the worktree directory**, then re-run TSC from master to verify. NEVER fix a TSC error by directly editing the master copy of a file while the worktree branch is still open. The mental model: TSC is a **read** tool run from master; all **writes** go to the worktree.

**Why this is safe**: Multiple conversations can work on different branches simultaneously because each has its own physical directory. No conversation can ever clobber another's uncommitted work.


### Pre-Flight Checks (Before ANY worktree creation)

Before creating a new worktree, the agent MUST:
1. Confirm the user used an explicit execution trigger phrase: "start building", "execute", "let's build", "go ahead", "start coding", "begin implementation", or invoked `/focus` or `/bucketlist`.
2. Answering planning questions, approving a plan, or saying "yes" to a non-execution question does NOT count.
3. When in doubt, ASK: "Ready for me to create the worktree and start building?"
4. **Fortress Guard Validation**: Execute `powershell.exe -ExecutionPolicy Bypass -File .\tools\fortress-guard.ps1` before modifying any files to ensure you are not illegally editing the master fortress while a worktree is open.

### Security Boundaries
1. **Zero Hardcoding**: NEVER hardcode API keys, DB URIs, passwords, or device MAC addresses in code.
2. Use `process.env.VARIABLE_NAME`. Add dummies to `.env.example`. Never touch the local `.env` file.
3. Automatically correct detected hardcoded secrets by extracting them into env logic.
