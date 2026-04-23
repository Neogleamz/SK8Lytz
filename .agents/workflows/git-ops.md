---
description: Universal Git Operations (Snapshots, Reverts, and Worktree Teardowns)
---

# Git Ops Engine

### /git-snapshot
1. Check `git status`.
2. Stage and commit changes as a checkpoint: `git commit -m "chore(checkpoint): <description>"`.
3. Output the commit hash.

### /gitcleanup (Worktree Teardown)
1. Verify no uncommitted changes exist in the worktree.
2. Ensure you are currently IN the master fortress directory (`cd C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz`).
3. Merge the worktree: `git merge <slug> --ff-only || git merge <slug> --no-ff -m "chore(merge): <slug> -> master"`.
4. Destroy the worktree: `git worktree remove ../SK8Lytz-worktrees/<slug>`.
5. Delete the branch: `git branch -d <slug>`.
6. Verify clean state with `git worktree list`.
