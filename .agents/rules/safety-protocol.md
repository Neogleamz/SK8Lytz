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


### Worktree Isolation Protocol (Mandatory)

The main repo directory (`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz`) is the **master fortress**. It NEVER leaves the `master` branch. All feature/fix work happens in isolated worktree directories.

**Worktree root**: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\`

**To start work on a branch:**
```powershell
# From the main repo directory (always master):
cd C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz
git pull origin master
git worktree add ../SK8Lytz-worktrees/<slug> -b <slug>
# Then do ALL work inside the worktree directory:
cd C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\<slug>
```

**To resume work on an existing branch:**
```powershell
# The worktree already exists — just cd into it:
cd C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\<slug>
```

**To merge and clean up when done:**
```powershell
# Return to master fortress:
cd C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz
git merge <slug>
# Only after explicit user approval to push:
git push origin master
# Clean up the worktree:
git worktree remove ../SK8Lytz-worktrees/<slug>
git branch -d <slug>
```

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
