---
description: Universal Git Operations (Snapshots, Reverts, and Worktree Teardowns)
persona_entry: "📋 Scrum — Casey"
team_roster: .agents/team-roster.md
---

# Git Ops Engine

> **📋 Scrum — Casey | Git Ops Active**
> *Casey enforces the worktree discipline. Snapshots are checkpoints. Teardowns go through the gatekeeper — no manual merges, no exceptions.*

### /git-snapshot
1. Check `git status`.
2. Stage and commit changes as a checkpoint: `git commit -m "chore(checkpoint): <description>"`.
3. Output the commit hash.

### /gitcleanup (Worktree Teardown)
1. Verify no uncommitted changes exist in the worktree.
2. Ensure you are currently IN the master fortress directory (`cd C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz`).
3. Execute the automated gatekeeper to verify cryptographic attestations, merge cleanly, and tear down:
   ```powershell
   powershell.exe -ExecutionPolicy Bypass -File .\tools\fortress-gatekeeper.ps1
   ```
4. Verify clean master state with `git worktree list`.
