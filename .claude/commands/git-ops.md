# /git-ops — Universal Git Operations

**Description:** Universal Git Operations (Snapshots, Reverts, and Worktree Teardowns).
**Persona:** 📋 Scrum — Casey

> **📋 Scrum — Casey | Git Ops Active**
> *Casey enforces the worktree discipline. Snapshots are checkpoints. Teardowns go through the gatekeeper — no manual merges, no exceptions.*

### /git-snapshot
1. Check `git status`.
2. Stage and commit changes as a checkpoint: `git commit -m "chore(checkpoint): <description>"`.
3. Output the commit hash.

### /gitcleanup (Worktree Teardown)
1. Verify no uncommitted changes exist in the worktree.
2. Ensure you are currently IN the master fortress directory (`cd C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz`).
3. **Run verify before gatekeeper (S7 gate order requirement):**
   ```powershell
   npm run verify
   ```
   If verify fails → HALT. Fix the issue in the worktree before proceeding.
4. Execute the automated gatekeeper to verify cryptographic attestations, merge cleanly, and tear down:
   > [!CAUTION]
   > **CRITICAL CWD REQUIREMENT:** You MUST set your working directory to exactly `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz`. Running this from inside the worktree directory will cause a silent failure loop.

   ```powershell
   powershell.exe -ExecutionPolicy Bypass -File .\tools\fortress-gatekeeper.ps1
   ```
5. **Write SESSION_LOG [MERGE] entry (mandatory per agent-behavior.md Rule 11):**
   Immediately after gatekeeper success, append to `docs/SESSION_LOG.md`:
   ```markdown
   ### [MERGE] YYYY-MM-DDTHH:MM — <slug> → master @ <commit-hash>
   **What merged:** (bullet list of what changed and why)
   **Verify result:** TSC ✅/❌, Jest ✅/❌, gates ✅/❌
   **Files touched:** (list)
   ```
6. Verify clean master state with `git worktree list`.
