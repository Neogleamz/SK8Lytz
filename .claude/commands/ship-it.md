# /ship-it — Release Pipeline Orchestrator

**Description:** Orchestrate the comprehensive release pipeline enforcing the Test Before Merge strategy.
**Lead Persona:** 🚀 RM — Taylor (orchestrated)

> **🚀 RM — Taylor | Release Pipeline Active**
> *Taylor's verdict is final. The pipeline enforces Test Before Merge. If any phase fails, the sequence halts. Master only gets green code.*

When invoked via `/ship-it`, you act as a strict state machine orchestrating the release pipeline. It enforces "Test Before Merge" — `master` is only updated with hardware-verified builds.

**CRITICAL RULE:** If ANY phase or child workflow fails or raises errors, HALT immediately. Do not proceed until the user confirms the issues are resolved.

---

## 🪃 Orchestration Model (read first)

`/ship-it` runs on the **main thread as the orchestrator**, led by Taylor. Phases delegate to subagents in `.claude/agents/`:

- **▶ DELEGATE** — spawn a subagent for autonomous, scoped work (audits, QA sweeps, the gatekeeper merge).
- **▶ MAIN THREAD** — keep on the orchestrator for the three user gates that subagents cannot handle: the **manual-QA halt** (Phase 3), the **version-bump choice** (Phase 4), and the **push consent** (Phase 4→5).

Never delegate a phase that waits on the user — subagents can't prompt.

---

### Phase 1: Worktree Hardening (The Crucible)
*Location: inside the isolated feature worktree (`../SK8Lytz-worktrees/<slug>`)*

1. **Health Sweep:** **▶ DELEGATE to `taylor`** → run `/health-sweep` (DB scanner + `npm audit`).
2. **Type Check & Test Verification (PoE):** **▶ DELEGATE to `taylor`** → `npm run verify` (compilation + Jest + signed proof of execution).
   ```powershell
   npm run verify
   ```
3. **Automated Smoke Test:** **▶ DELEGATE to `blake`** → run `/smoke-test` (headless render check; no white screen / fatal crash).
   > Note: Detox E2E has no config yet — `/smoke-test` is the current working alternative.
4. **Codebase & Bundle Audit:** **▶ DELEGATE to `reyes`** → run `/audit-codebase` (includes bundle weight check).
5. **Codebase Deep Dive (Regression Hunt):** **▶ DELEGATE in two stages, using the per-agent model split:**
   - Spawn `reyes` to run `/deepdive-code-hunt` — deploy the QA nodes across all domains and collect raw findings (high-context breadth).
   - Then spawn `morgan` (opus) to run `/deepdive-code-synthesis` — compile the findings and certify zero active regressions or guardrail violations (reasoning).

*(Pause and verify no errors before proceeding.)*

### Phase 2: The Master Fortress Merge (Automated Gatekeeper)
*Location: the master fortress (`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz`)*
**▶ DELEGATE to the `taylor` subagent.**

1. **Prepare Master:** change to the master fortress directory.
2. **Validate and Merge:** run the gatekeeper to verify the attestation, confirm commit correlation + freshness, fast-forward merge into master, dismantle the worktree, and prune the branch.
   > **CRITICAL CWD REQUIREMENT:** working directory MUST be exactly `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz`. Running from inside the worktree causes a silent failure loop.
   ```powershell
   powershell.exe -ExecutionPolicy Bypass -File .\tools\fortress-gatekeeper.ps1
   ```
   > Reminder (VS-001): never merge two worktrees built from the same base commit in one pass. The patched gatekeeper rebases each branch onto master HEAD first and only tears down on merge success.
3. **Write SESSION_LOG [MERGE] entry (mandatory):**
   ```markdown
   ### [MERGE] YYYY-MM-DDTHH:MM — <slug> → master @ <commit-hash>
   **What merged:** (summary of what shipped)
   **Verify result:** TSC ✅/❌, Jest ✅/❌, gates ✅/❌
   **Files touched:** (list from gatekeeper output)
   ```

### Phase 3: The Physical Proof (Test Before Push)
*Location: the `master` branch.*

1. **Build & Deploy:** **▶ DELEGATE to `taylor`** → run `/deploy-device` to compile master into a physical `.apk` and install it on the connected Android device.
2. **The QA Halt:** **▶ MAIN THREAD.** Enter a strict wait state and ask the user:
   > "Build deployed to physical device from master. Please perform manual QA. Type 'approve' to proceed to versioning and push, or 'reject' to halt the pipeline."
3. **Fix-Forward Halt Logic:** if the user types 'reject', HALT entirely. Do NOT revert the merge. Instruct: *"Release aborted. The flawed code remains safely on local master. Spin up a new worktree from master to fix the issues, gatekeeper it back in, then restart `/ship-it`."*

### Phase 4: Versioning & Paperwork
*Prerequisite: user typed 'approve' in Phase 3. Location: `master`.*

1. **Bump versions:** **▶ MAIN THREAD** — ask the user `patch` / `minor` / `major`. Once confirmed, increment `package.json` + `app.json` (`versionCode`, `buildNumber`, semver).
2. **Documentation:** **▶ DELEGATE to `avery`** → run `/release-notes` (CHANGELOG entry + PR description).
3. **Staging:** **▶ DELEGATE to `blake`** → run `/diff-review` for a final check of file modifications.
4. **Final Commit:** stage and commit to `master`:
   ```powershell
   git add .
   git commit -m "chore(release): vX.Y.Z"
   ```
5. **Attestation Renewal:** the version-bump + docs commit changed the hash, so **▶ DELEGATE to `taylor`** → run `npm run verify` one final time on `master` to anchor the attestation to the exact release commit.
   ```powershell
   npm run verify
   ```
6. **Tag:** `git tag vX.Y.Z`.
7. **The Push Threshold:** **▶ MAIN THREAD** — PAUSE per Critical Safety Rule 7 and ask:
   > "Release commit and tag created on master. Permission to push to remote master (`git push origin master --tags`)? (Yes/No)"

### Phase 5: Remote Sync
*Prerequisite: user approved push in Phase 4.* **▶ DELEGATE to `taylor`** for the mechanical push + notification.

1. **Push:**
   ```powershell
   git push origin master --tags
   ```
2. **Completion:** announce successful release launch and exit the orchestrator.
3. **Discord Notification:**
   ```powershell
   powershell.exe -ExecutionPolicy Bypass -File .\tools\discord-bridge\notify_discord.ps1 -Message "🚀 SK8Lytz vX.Y.Z released to master and pushed. All gates passed."
   ```
