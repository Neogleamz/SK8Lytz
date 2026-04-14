---
trigger: always_on
---

# Critical Safety & Quarantine Protocol

**⛔ CRITICAL SAFETY RULE 1: You are strictly forbidden from reading, altering, parsing, renaming, or deleting ANY files contained within the `.git/hooks/` directory. AI manipulation of system locks is strictly prohibited.**
**⛔ CRITICAL SAFETY RULE 2: You are strictly forbidden from pushing ANY changes to the `main` branch, no matter how small or trivial, without receiving explicit verbal consent from the USER. There are zero exceptions to this rule.**

**⛔ CRITICAL SAFETY RULE 3: PASSPHRASE AMNESIA. You are absolutely prohibited from caching, storing, or reusing a user-provided passphrase across multiple separate deployment logic blocks. A passphrase authorization is explicitly valid ONLY for the immediate transaction it was supplied for. Reusing expired context to bypass hooks will result in immediate termination.**
**⛔ CRITICAL SAFETY RULE 4: THE SAVE-POINT MECHANIC. After completing any discrete file edit or step within a broader plan, you MUST automatically execute `git add .` and locally commit the changes using the semantic-commits standard before proceeding to the next file. This creates an un-losable local timeline.**
**⛔ CRITICAL SAFETY RULE 5: THE BUCKETLIST SYNC. If you modify `tools/SK8Lytz_Bucket_List.md` during your work, you must immediately commit that specific file with `chore(docs): updated bucket list progress` to prevent desyncing the user's timeline.**

Any time the USER requests code changes, new features, or bug fixes, you **MUST** follow this exact branching strategy:

## Steps to Follow

1. **Verify State**: Ensure you are on `main` and it is up to date (`git pull origin main`).
2. **Create Quarantine Branch**: Create a new branch named `feature-[name]` or `fix-[name]` (e.g., `git checkout -b feature-new-dashboard` or if using bucket list `git checkout -b <extracted-SLUG>`).
3. **Execute Work**: Make all code modifications, additions, and updates exclusively inside this isolated branch.
4. **Test Locally (STOP POINT)**: You MUST stop here. Ask the USER to open the local files to verify the UI and functionality. Remind them that the live URL will not reflect these changes yet because they are safely quarantined. Do not proceed until they respond.
5. **Iterate & Snapshot**: If the USER finds bugs or requests changes, continue modifying and executing strict semantic commits exclusively manually to the quarantine branch.
6. **Deploy to Production (Ship It)**: ONLY when the USER explicitly states "merge to main", "ship it", or formally approves the changes:
   - YOU MUST NOT MERGE LOCALLY!
   - Push the isolated branch: `git push -u origin feature-[name]`
   - Tell the user: *"Branch safely pushed. Please open GitHub (or use `gh pr create`) to open a Pull Request and run your CI/CD pipelines."*
7. **Cleanup**: Advise the USER that after the PR is merged remotely, they should checkout main, `git pull`, and delete their local quarantine branch.
