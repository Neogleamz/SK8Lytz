# Ship It & Release Manager Workflow

This workflow handles both branch merging ("Ship It") and production versioning ("Release Manager"). Execute the corresponding path based on my trigger phrase.

## Path A: Feature Merge (Triggers: "ship it", "merge task", "finalize branch")

1. **Verify Context**: Run `git branch --show-current` to ensure we are currently on a feature branch (e.g., `feature/...`, `fix/...`, or `chore/...`).
2. **Find Base**: If you remember the Epic Target from the bucket list context, use it. If unable to determine the base branch, explicitly ask me which branch to merge into (e.g., `epic/device-registration` or `main`).
3. **Pre-Flight Code Audit**: Before merging, act as a Senior Security & Performance Engineer. Review the critical files changed on this feature branch for:
   - Security flaws (hardcoded secrets, unhandled exceptions)
   - Performance bottlenecks (inefficient react loops, excessive re-renders, memory leaks)
   - Code cleanliness and proper architecture
   If any issues are flagged, list them out and **HALT**. Wait for me to give permission to either fix them or proceed.
4. **Knowledge Audit Gate**: Before merging, evaluate if the overarching feature branch established new critical knowledge (DB tables, Bluetooth commands, global contexts). If yes, ensure it is documented in `tools/SK8Lytz_App_Master_Reference.md`.
5. **The Safe Push**:
   - `git push -u origin <feature-branch>`
6. **PR Handoff**: Tell me the branch was safely pushed to the cloud. Instruct me to either open GitHub or run `gh pr create` to initiate a Pull Request.
7. **Halt**: Wait for me to confirm that the PR is merged remotely. Once I do, remind me to checkout `master`, `git pull`, and safely delete my local quarantine branch.

## Path B: Production Release (Triggers: "cut a release", "prepare release", "draft release")

1. **Version Bump**:
   - Ask me if this is a `major`, `minor`, or `patch` release.
   - Once I answer, automatically use your tools to update the version number in `package.json`.
2. **Generate Changelog**:
   - Parse the `tools/SK8Lytz_Bucket_List.md` file and extract all tasks marked as completed (`- [x]`) since the last release.
   - Update the `CHANGELOG.md` file in the root directory. Add a new section at the top with the new version number, the current date, and a bulleted list of these completed features and fixes.
3. **Bucket List Cleanup**:
   - Remove the completed items that were just added to the changelog from `tools/SK8Lytz_Bucket_List.md` to keep the active list clean.
4. **The Release Commit & Cloud Pipeline**:
   - Execute `git add .`
   - Execute `git commit -m "chore: release v<new-version-number>"`
   - Execute `git tag v<new-version-number>` to officially stamp the timeline.
   - Push the latest master state and tags to GitHub: `git push origin master --tags`
   - Auto-deploy by merging to the publish branch: `git checkout publish` and `git merge master`
   - Push the deployment: `git push origin publish`
   - Return safely home: `git checkout master`
5. **Halt**: Output a success message, confirm the deployment pipeline was triggered, and print the new release notes to the chat.
