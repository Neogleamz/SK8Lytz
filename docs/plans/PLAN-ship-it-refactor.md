# Implementation Plan: Ship-It Workflow Refactor

## Goal Description
The current /ship-it workflow executes the physical Android APK build (/deploy-device) and heavy integration tests inside the feature worktree (C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\<slug>). This causes two critical failures:
1. **MAX_PATH Violations:** The nested paths in the Android Gradle build exceed the 260-character Windows limit when running inside the worktree directory.
2. **Context Mismatches:** Certain test frameworks and hooks fail because they expect to be executed on the master branch.

To resolve this, we will swap the order of execution: local unit/type tests will still run in the worktree as a pre-merge gate, but the heavy physical builds, QA, and versioning will be shifted to execute **after** the local fast-forward merge to master, but **before** the remote push. This maintains the "Test Before Push" safety net while bypassing the worktree path limits.

## User Review Required
> [!WARNING]
> **Rollback Procedure on QA Failure**
> Since the physical build and QA will now happen *after* the gatekeeper merges the feature branch into local master, a failed QA session means master is now locally polluted.
> We will add an explicit rollback instruction in the workflow: if QA is rejected, the orchestrator must execute git reset --hard HEAD~1 to revert the merge, and then reconstruct the feature branch for the developer.

## Open Questions
1. **Rollback Scripting:** Should we create a new 	ools/rollback-release.ps1 script to automate the rollback process if QA fails, or just put the raw git commands (git branch <slug> HEAD; git reset --hard HEAD~1) in the /ship-it markdown?

## Proposed Changes

### Workflows

#### [MODIFY] .agents/workflows/ship-it.md
- **Phase 1: Worktree Hardening**
  - Remains in the worktree. Runs 
pm run verify to generate the cryptographic attestation.
- **Phase 2: The Master Fortress Merge (Automated Gatekeeper)** *(Moved up from Phase 4)*
  - Context switch to master. Runs ortress-gatekeeper.ps1 to perform the fast-forward merge and delete the worktree.
- **Phase 3: The Physical Proof** *(Moved down from Phase 2)*
  - Runs on master. Executes /deploy-device. No MAX_PATH errors.
  - Adds Rollback instructions if the user rejects the manual QA.
- **Phase 4: Versioning & Paperwork** *(Moved down from Phase 3)*
  - Runs on master. Bumps versions, generates /release-notes.
  - Executes git commit -am "chore(release): vX.Y.Z".
  - Executes 
pm run verify to generate the final pre-push attestation.
- **Phase 5: Remote Sync**
  - Runs git push origin master --tags.

## Verification Plan

### Automated Tests
- N/A (Documentation change).

### Manual Verification
- We will execute a dry-run of /ship-it with a dummy feature branch to ensure the orchestrator follows the new Phase order, successfully builds the APK on master, and correctly halts/reverts if QA is rejected.