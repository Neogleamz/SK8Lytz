# Safety Protocol
*Source: `.agents/rules/safety-protocol.md`*

> Several of these rules are now also enforced mechanically by hooks in `.claude/settings.json`
> (guard-push, guard-fortress, guard-strikes, check-any-cast). The hooks are the hard backstop;
> these rules are the behavioral contract. Both apply.

## 1. Git & Branching Safeguards
- **⛔ Rule 1: Master Fortress Lock**: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz` MUST stay on `master`. Editing master source files while a worktree is active is STRICTLY FORBIDDEN. *(Enforced by `guard-fortress.ps1`.)*
- **⛔ Rule 2: Worktree Isolation**: All work MUST occur in `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\<slug>\`.
- **⛔ Rule 2.5: Subagent Isolation**: Subagents are strictly forbidden from inheriting the master workspace unless executing read-only audits.
- **⛔ Rule 3: Fast-Forward Merges Only**: Use `tools/fortress-gatekeeper.ps1`. Direct manual merges prohibited.
- **⛔ Rule 4: Worktree Commit Is Not Done**: Task is incomplete until worktree is merged, folder removed, and branch deleted.
- **⛔ Rule 5: Version-Controlled Brain**: `docs/SK8Lytz_Bucket_List.md` is tracked by Git. Always ensure your updates to the board are staged and committed.

## 2. Push & Release Gates
- **⛔ Rule 6: Zero-Bypass Push Gate**: FORBIDDEN from `git push` without a fresh `npm run verify` attestation for the current commit. *(Enforced by `guard-push.ps1`.)*
- **⛔ Rule 7: Pre-Push Consent**: MUST ask user permission before running `git push`.
- **⛔ Rule 8: Health Sweep Gate**: Run `npm audit` (enforced via pre-push Husky hook) before any push.
- **⛔ Rule 9: No git/hooks Modification**: Never edit, parse, or delete `.git/hooks/` files. *(Also denied in settings.json permissions.)*

## 3. Operations Guard
- **⛔ Rule 10: Three-Strike Debugging Lockout**: Bug fix fails 3× → `git reset --hard` → consultative mode only. *(Enforced by `guard-strikes.ps1`.)*
- **⛔ Rule 11: The Override Key**: Bypassing Rule 10 requires passphrase: `COWBOY MODE ACTIVATED`.

## 4. Machine Constants (Quick Reference)

```powershell
$ADB  = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\.local-builder\android-sdk\platform-tools\adb.exe"
$ROOT = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
$APK  = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\android\app\build\outputs\apk\release\SK8Lytz.apk"
```
- ADB devices: `& $ADB devices` | Stream logs: `& $ADB logcat -s "ReactNativeJS:V"`
- Kill Metro: `Get-Process -Name node -ErrorAction SilentlyContinue | Stop-Process -Force`
- Discord: `powershell.exe -ExecutionPolicy Bypass -File .\tools\discord-bridge\notify_discord.ps1 -Message "..."`

## 5. Knowledge Base Integrity Guards
- **⛔ Rule 13: No-Assert-Without-KB-Check**: Strictly forbidden from making any assertion about an external library, API, or hardware protocol without first checking `tools/knowledge-base/INDEX.md`.
- **⛔ Rule 14: Stale KB = Warning, Not Block**: A stale or critical KB entry surfaces as a ⚠️ WARNING to the user. It is NOT a hard sprint block.
- **⛔ Rule 15: No KB Auto-Update**: The agent is strictly forbidden from silently updating KB entry content without presenting the re-validated content to the user and receiving acknowledgment.

## 6. Bucket List Archival Guard (VS-004)
- **⛔ Rule 12: Mandatory Archival Execution**: Strictly forbidden from handing off to another workflow or continuing to the next task if the SK8Lytz_Bucket_List.md contains ANY tasks marked [x]. The active persona MUST run:
  ```
  node tools/auto-archiver.js --task <slug>
  ```
- **⛔ Rule 12.1: SESSION_LOG [MERGE] Companion**: Immediately after every successful gatekeeper merge, BEFORE running the auto-archiver, append a `[MERGE]` entry to `docs/SESSION_LOG.md`.
- **⛔ Rule 12.2: ACTIVE SPRINT Header Sync (FRICTION-020 fix)**: Immediately after every successful gatekeeper merge, Taylor MUST edit `docs/SK8Lytz_Bucket_List.md` to:
  1. Set `Currently executing:` to the next pending task slug (or `none` if sprint is empty)
  2. Append `Completed: <slug> @ <hash> ✅` on the line below it
  This step is a HARD STOP — "master is green" cannot be declared until the board header reflects reality. The auto-archiver removes the completed task entry but does NOT update the header. That is Taylor's job, every time, no exceptions.

## Tool Playbook (Proven PowerShell Commands)

```powershell
# Build & Deploy
powershell.exe -ExecutionPolicy Bypass -File .\tools\build-apk.ps1
powershell.exe -ExecutionPolicy Bypass -File .\tools\install-apk.ps1
powershell.exe -ExecutionPolicy Bypass -File .\tools\fortress-gatekeeper.ps1
powershell.exe -ExecutionPolicy Bypass -File .\tools\port-sweeper.ps1
powershell.exe -ExecutionPolicy Bypass -File .\tools\backup_database.ps1

# Verify & Check
npm run verify
node tools/verifiable-check-runner.js
node tools/auto-archiver.js --task <slug>
node tools/kb-validator.js --summary
node tools/ast-parser.js --collision-matrix artifacts/domain_clusters.json

# Docker Stack
docker compose up -d
docker compose up -d --build
docker compose up -d --force-recreate webdemo
docker compose ps
docker compose logs cctower | Select-String "Booting Discord Bridge"

# Git Worktrees
git worktree add ../SK8Lytz-worktrees/<slug> -b <slug>
git worktree list
New-Item -ItemType Junction -Path ../SK8Lytz-worktrees/<slug>/node_modules -Target <root>/node_modules

# Discord
powershell.exe -ExecutionPolicy Bypass -File .\tools\discord-bridge\notify_discord.ps1 -Message "✅ <slug> merged. Master is green."
```

## Learned Failure Patterns — Victory Snapshots
*Baked-in rules from resolved friction events. Full detail in `docs/KNOWN_ISSUES.md`.*

- **VS-001 (Parallel Worktree Divergence)**: Always create worktrees sequentially. Never two worktrees from the same base commit through the gatekeeper in one pass.
- **VS-002 (Gitignore Shadow Zone)**: Always run `git status --short` before committing.
- **VS-003 (Documentation Drift)**: Always update Master Reference before running gatekeeper. This failure caused 16 commits of documentation drift on 2026-06-06.
- **VS-004 (Archival Guard)**: Strictly forbidden from proceeding to the next task if any task marked `[x]` hasn't been archived via `node tools/auto-archiver.js --task <slug>`.
- **VS-013 (No-Placeholder Plan Law)**: Tasks MUST NOT be appended to the Bucket List with `*(pending)*` plans or `[❌ UNVERIFIED]` status.
- **VS-014 (Parallel Wave Collision)**: Always run `ast-parser.js --collision-matrix` before writing parallel wave tasks to the Bucket List. Manual collision calculation is FORBIDDEN.
- **FRICTION-020 (Board Sync After Merge)**: After every merge, immediately edit `docs/SK8Lytz_Bucket_List.md` to update the ACTIVE SPRINT header. The gatekeeper auto-archiver removes the task entry but does NOT update the header.

## Discord Bridge

The Discord Bridge is automatically launched inside the Docker Scraper Stack container (`sk8lytz-scraper-stack` service running `CCTower.ts`) when the stack starts. Running it as a host process is deprecated.

**Verify bridge is active:**
```powershell
docker compose ps
docker compose logs cctower | Select-String "Booting Discord Bridge"
```

**Send a notification:**
```powershell
powershell.exe -ExecutionPolicy Bypass -File .\tools\discord-bridge\notify_discord.ps1 -Message "Your message here"
```

**Standard notification messages:**
- After merge: `"✅ <slug> merged to master. Tests passed. Master is green."`
- After session: `"🌙 SK8Lytz session complete. Master is stable. Bucket list groomed. See you next time."`
- After goal complete: `"Goal complete: [BATCH:<name>] — all <N> tasks merged. Master is green."`
- After release: `"🚀 SK8Lytz vX.Y.Z released to master and pushed. All gates passed."`
