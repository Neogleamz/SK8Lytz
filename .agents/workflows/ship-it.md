# Ship It Orchestrator -- "/ship-it"

When invoked via `/ship-it`, you must act as a strict state machine orchestrating the comprehensive release pipeline. This pipeline enforces the "Test Before Merge" strategy, guaranteeing that the `master` branch is only updated with hardware-verified builds.

**CRITICAL RULE:** If ANY phase or child workflow fails or raises errors, you must HALT the sequence immediately. Do not proceed to the next phase until the user confirms the issues are resolved.

---

### Phase 1: Worktree Hardening (The Crucible)
*Location: Must be executed inside the isolated feature worktree (`../SK8Lytz-worktrees/<slug>`)*
1. **Health Sweep:** Execute the `/health-sweep` workflow to run the DB scanner and npm audit.
2. **Type Check:** Execute the `/tsc-check` workflow.
3. **Smoke Test:** Execute the `/smoke-test` workflow to visually guarantee the app won't fatal exception/white screen.
4. **Bundle Audit:** Execute the `/bundle-audit` workflow.
*(Pause and verify no errors exist before proceeding).*

### Phase 2: The Physical Proof (Test Before Merge)
*Location: Still inside the worktree.*
1. **Compile:** Execute the `/build-apk` workflow to compile the worktree's code into a physical `.apk`.
2. **Deploy:** Execute the `/install-apk` workflow to push the build via ADB to the connected physical device.
3. **The QA Halt:** You must enter a Strict Wait State. Ask the user: 
   > "Build deployed to physical device. Please perform manual QA. Type 'approve' to proceed to merge, or 'reject' to abort."

### Phase 3: Versioning & Paperwork
*Prerequisite: User explicitly typed 'approve' in Phase 2.*
1. **Bump versions:** Ask the user if this is a `patch`, `minor`, or `major` release. Once confirmed, manually increment the version in `package.json` and `app.json` (update `versionCode`, `buildNumber`, and semver string).
2. **Documentation:** Execute the `/changelog` and `/pr-summary` workflows to generate release notes based on the worktree's commits.
3. **Staging:** Execute `/diff-review` for a final check of the file modifications.
4. **Final Commit:** Stage all changes and commit directly to the worktree branch: `git commit -m "chore(release): vX.Y.Z"`

### Phase 4: The Master Fortress Merge
*Location: Context switch back to the main directory (`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz`)*
1. **Prepare Master:** Change directory to the master fortress and run `git pull origin master`.
2. **Merge:** Run `git merge <slug> --no-ff -m "Merge branch '<slug>' for release vX.Y.Z"`. (Replace `<slug>` with the worktree branch name).
3. **Tag:** Create the git tag: `git tag vX.Y.Z`.
4. **The Push Threshold:** You must PAUSE to satisfy Critical Safety Rule 7. Ask the user:
   > "Merge complete and tagged on master. Permission to push to remote master (`git push origin master --tags`)? (Yes/No)"

### Phase 5: Cleanup
*Prerequisite: User approved push in Phase 4 and the push command was successfully executed.*
1. **Nuke Worktree:** Run `git worktree remove ../SK8Lytz-worktrees/<slug> --force`.
2. **Delete Branch:** Run `git branch -D <slug>`.
3. **Completion:** Announce successful launch and exit the orchestrator.
