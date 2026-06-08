---
description: Orchestrate the comprehensive release pipeline enforcing the Test Before Merge strategy
persona_entry: "🚀 RM — Taylor"
team_roster: .agents/team-roster.md
---

# Ship It Orchestrator -- "/ship-it"

> **🚀 RM — Taylor | Release Pipeline Active**
> *Taylor's verdict is final. The pipeline enforces Test Before Merge. If any phase fails, the sequence halts. Master only gets green code.*

When invoked via `/ship-it`, you must act as a strict state machine orchestrating the comprehensive release pipeline. This pipeline enforces the "Test Before Merge" strategy, guaranteeing that the `master` branch is only updated with hardware-verified builds.

**CRITICAL RULE:** If ANY phase or child workflow fails or raises errors, you must HALT the sequence immediately. Do not proceed to the next phase until the user confirms the issues are resolved.

---

### Phase 1: Worktree Hardening (The Crucible)
*Location: Must be executed inside the isolated feature worktree (`../SK8Lytz-worktrees/<slug>`)*
1. **Health Sweep:** Execute the `/health-sweep` workflow to run the DB scanner and npm audit.
2. **Type Check & Test Verification (PoE):** Run the unified check runner to verify compilation, Jest tests, and compile a cryptographically signed proof of execution:
   ```powershell
   npm run verify
   ```
3. **Automated Smoke Test:** Execute the `/smoke-test` workflow to verify the app renders without a white screen or fatal crash.
   > Note: Detox E2E is a devDependency but has no config yet — `/smoke-test` (headless browser check) is the current working alternative.
4. **Codebase & Bundle Audit:** Execute the `/audit-codebase` workflow (includes bundle weight check).
5. **Codebase Deep Dive (Regression Hunt):** First execute `/deepdive-code-hunt` (using a high-context model like Gemini) to deploy the 37 QA nodes across all domains. Then switch to a reasoning model (like Claude) and execute `/deepdive-code-synthesis` to compile the findings and guarantee zero active regressions or guardrail violations.
*(Pause and verify no errors exist before proceeding).*

### Phase 2: The Master Fortress Merge (Automated Gatekeeper)
*Location: Context switch back to the main directory (`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz`)*
1. **Prepare Master:** Change directory to the master fortress.
2. **Validate and Merge:** Execute the automated gatekeeper script to verify the cryptographic attestation, confirm commit correlation and freshness, perform a fast-forward merge into master, dismantle the worktree, and prune the local feature branch:
   > [!CAUTION]
   > **CRITICAL CWD REQUIREMENT:** You MUST set the `Cwd` parameter of your `run_command` tool to exactly `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz`. Running this from inside the worktree directory will cause a silent failure loop.

   ```powershell
   powershell.exe -ExecutionPolicy Bypass -File .\tools\fortress-gatekeeper.ps1
   ```

### Phase 3: The Physical Proof (Test Before Push)
*Location: Must be executed on the `master` branch (`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz`)*
1. **Build & Deploy:** Execute the `/deploy-device` workflow to compile the `master` code into a physical `.apk` and install it on the connected Android device. (Executing this on `master` avoids MAX_PATH build failures).
2. **The QA Halt:** You must enter a Strict Wait State. Ask the user: 
   > "Build deployed to physical device from master. Please perform manual QA. Type 'approve' to proceed to versioning and push, or 'reject' to halt the pipeline."
3. **Fix-Forward Halt Logic:** If the user types 'reject', you must HALT the sequence entirely. Do NOT revert the merge. Instruct the user: *"Release aborted. The flawed code remains safely on local master. Please spin up a new worktree from master to fix the issues, gatekeeper it back in, and then restart `/ship-it`."*

### Phase 4: Versioning & Paperwork
*Prerequisite: User explicitly typed 'approve' in Phase 3.*
*Location: Still on the `master` branch.*
1. **Bump versions:** Ask the user if this is a `patch`, `minor`, or `major` release. Once confirmed, manually increment the version in `package.json` and `app.json` (update `versionCode`, `buildNumber`, and semver string).
2. **Documentation:** Execute the `/release-notes` workflow to generate the CHANGELOG entry and PR description.
3. **Staging:** Execute `/diff-review` for a final check of the file modifications.
4. **Final Commit:** Stage all changes and commit directly to `master`:
   ```powershell
   git add .
   git commit -m "chore(release): vX.Y.Z"
   ```
5. **Attestation Renewal:** Since the commit hash changed on version bump and documentation commit, you MUST execute `npm run verify` one final time on `master` to anchor the attestation to the exact release commit hash before pushing!
   ```powershell
   npm run verify
   ```
6. **Tag:** Create the git tag matching the bumped version: `git tag vX.Y.Z`.
7. **The Push Threshold:** You must PAUSE to satisfy Critical Safety Rule 7. Ask the user:
   > "Release commit and tag created on master. Permission to push to remote master (`git push origin master --tags`)? (Yes/No)"

### Phase 5: Remote Sync
*Prerequisite: User approved push in Phase 4 and the push command was successfully executed.*
1. **Push:** Execute the remote sync to deploy master and tags:
   ```powershell
   git push origin master --tags
   ```
2. **Completion:** Announce successful release launch and exit the orchestrator.
3. **Discord Notification:** Broadcast release status to the team:
   ```powershell
   powershell.exe -ExecutionPolicy Bypass -File .\tools\discord-bridge\notify_discord.ps1 -Message "🚀 SK8Lytz vX.Y.Z released to master and pushed. All gates passed."
   ```
