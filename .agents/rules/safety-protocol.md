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


### Worktree Isolation Protocol (Mandatory)

The main repo directory (`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz`) is the **master fortress**. It NEVER leaves the `master` branch. All feature/fix work happens in isolated worktree directories.

**Worktree root**: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\`

**To start work on a branch:**
```powershell
# From the main repo directory (always master):
cd C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz
git pull origin master
git worktree add ../SK8Lytz-worktrees/<slug> -b <slug>
# **Worktree Manifest Audit (MANDATORY)**:
# Copy untracked config files (.env) and ignored source files (e.g. ZenggeAdapter.ts) manually.
Copy-Item .env ../SK8Lytz-worktrees/<slug>/.env -ErrorAction SilentlyContinue
# Then do ALL work inside the worktree directory:
cd C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\<slug>
```

**To resume work on an existing branch:**
```powershell
# The worktree already exists — just cd into it:
cd C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\<slug>
```

**To merge and clean up when done — MANDATORY FINAL STEP (RULE 9):**
```powershell
# Return to master fortress:
cd C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz
# Try fast-forward first; fall back to --no-ff if branches diverged:
git merge <slug> --ff-only || git merge <slug> --no-ff -m "chore(merge): <slug> -> master"
# Clean up immediately — do NOT leave open:
git worktree remove ../SK8Lytz-worktrees/<slug>
git branch -d <slug>
# Confirm clean:
git worktree list
# Only after explicit user approval to push:
# git push origin master
```

> ⛔ **RULE 9 CHECKPOINT**: After running these commands, `git worktree list` must show ONLY the master fortress. If any worktree remains, the task is NOT complete.

> ⛔ **RULE 10 COROLLARY — TSC Discipline**: `npx tsc --noEmit` MUST be run from the master directory (it has `node_modules`). When TSC reports errors in worktree files, **fix them inside the worktree directory**, then re-run TSC from master to verify. NEVER fix a TSC error by directly editing the master copy of a file while the worktree branch is still open. The mental model: TSC is a **read** tool run from master; all **writes** go to the worktree.

> ❌ **What NEVER to do (the exact PR-C mistake)**:
> ```
> # ✅ Correct: edit the worktree file
> write_to_file("SK8Lytz-worktrees/<slug>/src/services/AppLogger.ts")
>
> # ❌ WRONG: editing master while worktree is active — causes divergence
> write_to_file("SK8Lytz/src/services/AppLogger.ts")  # ← DO NOT DO THIS
> ```

**Why this is safe**: Multiple conversations can work on different branches simultaneously because each has its own physical directory. No conversation can ever clobber another's uncommitted work.


### Pre-Flight Checks (Before ANY worktree creation)

Before creating a new worktree, the agent MUST:
1. Confirm the user used an explicit execution trigger phrase: "start building", "execute", "let's build", "go ahead", "start coding", "begin implementation", or invoked `/focus` or `/bucketlist`.
2. Answering planning questions, approving a plan, or saying "yes" to a non-execution question does NOT count.
3. When in doubt, ASK: "Ready for me to create the worktree and start building?"

### Security Boundaries
1. **Zero Hardcoding**: NEVER hardcode API keys, DB URIs, passwords, or device MAC addresses in code.
2. Use `process.env.VARIABLE_NAME`. Add dummies to `.env.example`. Never touch the local `.env` file.
3. Automatically correct detected hardcoded secrets by extracting them into env logic.
