---
trigger: always_on
---

# Critical Safety & Security Boundaries

## 1. Git & Branching Safeguards
- **⛔ Rule 1: Master Fortress Lock**: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz` MUST stay on `master`. Editing master source files while a worktree is active is STRICTLY FORBIDDEN.
- **⛔ Rule 2: Worktree Isolation**: All work MUST occur in `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\<slug>\`.
- **⛔ Rule 2.5: Subagent Isolation**: Subagents MUST be invoked with `Workspace: "branch"` or `Workspace: "share"`. They are strictly forbidden from inheriting the master workspace unless executing read-only audits.
- **⛔ Rule 3: Fast-Forward Merges Only**: Use `tools/fortress-gatekeeper.ps1`. Direct manual merges prohibited.
- **⛔ Rule 4: Worktree Commit Is Not Done**: Task is incomplete until worktree is merged, folder removed, and branch deleted.
- **⛔ Rule 5: Version-Controlled Brain**: `tools/SK8Lytz_Bucket_List.md` is tracked by Git. Always ensure your updates to the board are staged and committed. Never use python `w` mode scripts that overwrite the file blindly.

## 2. Push & Release Gates
- **⛔ Rule 6: Zero-Bypass Push Gate**: FORBIDDEN from `git push` without a fresh `npm run verify` attestation for the current commit.
- **⛔ Rule 7: Pre-Push Consent**: MUST ask user permission before running `git push`.
- **⛔ Rule 8: Health Sweep Gate**: Run `npm audit` (enforced via pre-push Husky hook) before any push.
- **⛔ Rule 9: No git/hooks Modification**: Never edit, parse, or delete `.git/hooks/` files.

## 3. Operations Guard
- **⛔ Rule 10: Three-Strike Debugging Lockout**: Bug fix fails 3× → `git reset --hard` → consultative mode only.
- **⛔ Rule 11: The Override Key**: Bypassing Rule 10 requires passphrase: `COWBOY MODE ACTIVATED`.

## 4. Quick Reference (Machine Constants)
```powershell
$ADB  = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\.local-builder\android-sdk\platform-tools\adb.exe"
$ROOT = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
$APK  = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\android\app\build\outputs\apk\release\SK8Lytz.apk"
```
- ADB devices: `& $ADB devices` | Stream logs: `& $ADB logcat -s "ReactNativeJS:V"`
- Kill Metro: `Get-Process -Name node -ErrorAction SilentlyContinue | Stop-Process -Force`
- Discord: `powershell.exe -ExecutionPolicy Bypass -File .\tools\discord-bridge\notify_discord.ps1 -Message "..."`

## 5. Learned Failure Patterns
Three critical failure patterns with operational rules — see [`KNOWN_ISSUES.md`](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/KNOWN_ISSUES.md):
- **VS-001**: Parallel worktree gatekeeper divergence → always create worktrees sequentially
- **VS-002**: Gitignore shadow zone → always run `git status --short` before committing
- **VS-003**: Documentation drift → always update Master Reference before running gatekeeper

## 6. Bucket List Archival Guard (VS-004)
- **⛔ Rule 12: Mandatory Archival Execution**: You are strictly forbidden from handing off to another workflow or continuing to the next task if the SK8Lytz_Bucket_List.md contains ANY tasks marked [x]. The active persona MUST run:
  ```
  node tools/auto-archiver.js --task <slug>
  ```
  and verify the task is moved to the archive before proceeding.
- **⛔ Rule 12.1: SESSION_LOG [MERGE] Companion (agent-behavior.md Rule 11)**: Immediately after every successful gatekeeper merge, BEFORE running the auto-archiver, append a `[MERGE]` entry to `tools/SESSION_LOG.md`. Archival without a log entry is a P3 violation.

## 7. Knowledge Base Integrity Guards
- **⛔ Rule 13: No-Assert-Without-KB-Check**: Strictly forbidden from making any assertion about an external library, API, or hardware protocol without first checking `tools/knowledge-base/INDEX.md`. If the KB has a CURRENT entry → cite it. If STALE/CRITICAL → note it. If not found → establish via research AND run `/kb-capture` before asserting. Skipping this check is a P1 violation.
- **⛔ Rule 14: Stale KB = Warning, Not Block**: A stale or critical KB entry surfaces as a ⚠️ WARNING to the user. It is NOT a hard sprint block. The user decides whether to re-validate before proceeding. Never halt a sprint task solely because a KB entry is stale.
- **⛔ Rule 15: No KB Auto-Update**: The agent is strictly forbidden from silently updating KB entry content without presenting the re-validated content to the user and receiving acknowledgment. The INDEX.md `Last Validated` date may be updated if the re-validation confirms no changes — but substantive content edits require user awareness.

