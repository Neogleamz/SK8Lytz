---
trigger: always_on
---

# Critical Safety & Security Boundaries

## 1. Git & Branching Safeguards
- **⛔ Rule 1: Master Fortress Lock**: The main repository directory (`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz`) represents the **master fortress** and MUST remain on the `master` branch. Editing master source files while a worktree is active is STRICTLY FORBIDDEN.
- **⛔ Rule 2: Worktree Isolation**: All feature and hotfix work MUST occur inside isolated worktree paths under `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\<slug>\`.
- **⛔ Rule 3: Fast-Forward Merges Only**: Merging a worktree into master MUST enforce `--ff-only` via `tools/fortress-gatekeeper.ps1`. Direct manual merges are prohibited.
- **⛔ Rule 4: Worktree Commit Is Not Done**: A task is incomplete until the worktree is merged, the physical worktree folder is removed, and the local branch is deleted.
- **⛔ Rule 5: gitignored Bucket List**: The `tools/SK8Lytz_Bucket_List.md` is gitignored. Keep it updated. Never attempt to commit or stage it.

## 2. Push & Release Gates
- **⛔ Rule 6: Zero-Bypass Push Gate**: You are STRICTLY FORBIDDEN from running `git push` without first running `npm run verify` to generate a fresh, cryptographically signed `.test-attestation.json` file for the current commit.
- **⛔ Rule 7: Pre-Push Consent**: Immediately after committing and passing verification checks, you MUST explicitly ask the user for permission before running `git push`.
- **⛔ Rule 8: Health Sweep Gate**: Never push code without first running `npm audit` and executing the database sweep to verify there are 0 security vulnerabilities.
- **⛔ Rule 9: No git/hooks Modification**: Never edit, parse, or delete `.git/hooks/` files.

## 3. Operations Guard
- **⛔ Rule 10: Three-Strike Debugging Lockout**: If a bug fix fails 3 times, halt execution, run `git reset --hard`, and drop into consultative `/brainstorming` mode.
- **⛔ Rule 11: The Override Key**: Bypassing Rules 10 and 11 is allowed ONLY if the user explicitly initiates the prompt with the passphrase: `COWBOY MODE ACTIVATED`.

## 4. Tool Playbook — Proven Command Registry
**MANDATORY RULE**: Before writing any complex terminal command, PowerShell pipeline, or search query, you MUST first check this document. Use the exact proven syntax.

### 🔑 Machine Constants
```powershell
$ADB = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\.local-builder\android-sdk\platform-tools\adb.exe"
$ROOT = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
$APK = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\android\app\build\outputs\apk\release\SK8Lytz.apk"
```

### 📁 File Discovery
- Monolith Scan: `Get-ChildItem -Path src -Recurse -File | Where-Object { $_.Length -gt 30720 -and $_.Extension -match "\.(tsx|ts)$" } | Select-Object Name, @{Name="SizeKB";Expression={ [math]::round($_.Length / 1KB, 2) }} | Sort-Object SizeKB -Descending`
- God Object Scan: `Get-ChildItem -Path src -Recurse -Include *.tsx,*.ts | ForEach-Object { $Hooks = Select-String -Path $_.FullName -Pattern "use[A-Z]" -AllMatches; if ($Hooks.Matches.Count -gt 15) { [PSCustomObject]@{ File=$_.Name; HookCount=$Hooks.Matches.Count } } } | Sort-Object HookCount -Descending`
- TODO/FIXME Hunt: `Get-ChildItem -Path src -Recurse -Include *.tsx,*.ts | Select-String "TODO:|FIXME:|HACK:" | Select-Object Filename, LineNumber, Line`

### 📱 BLE / Hardware (ADB)
- Connect/Devices: `& $ADB devices`
- Install APK: `& $ADB install -r $APK`
- Stream Logs: `& $ADB logcat -s "BluetoothGatt:D BluetoothLeScanner:D ReactNativeJS:V"`
- Clear Logs: `& $ADB logcat -c`

### 🗄️ Database (Supabase)
- Use MCP tool `mcp_supabase-mcp-server_generate_typescript_types` then `write_to_file` to `src/types/supabase.ts` with Overwrite: true.

### 🔨 Build System
- Expo start clean: `npx expo start --clear`
- Gradle clean: `Set-Location android; .\gradlew.bat clean; Set-Location ..`
- Kill Metro: `Get-Process -Name node -ErrorAction SilentlyContinue | Stop-Process -Force`

## 5. Discord Bridge
To maintain real-time visibility, broadcast status via the Discord Webhook bridge:
- Send push notification after any workflow script, or any significant manual phase, or before pausing.
- Command: `powershell.exe -ExecutionPolicy Bypass -File .\tools\discord-bridge\notify_discord.ps1 -Message "Your short summary here."`

## 6. Victory Snapshots (Learned Patterns)

### 🏆 VS-001: Parallel Worktree Gatekeeper Divergence (2026-05-26)
**Symptom**: Running the fortress-gatekeeper with 2+ active worktrees causes the second worktree's branch to be silently deleted even when its merge fails. The commit becomes an orphan recoverable only via `git reflog` + `cherry-pick`.

**Root Cause**: The old gatekeeper's `foreach` loop merged worktrees sequentially. After worktree 1 merged, master moved ahead by 1 commit. Worktree 2's branch still had the OLD parent, so `--ff-only` failed. The script then called `git worktree remove` and `git branch -D` **regardless of merge success**.

**Fix Applied**: `tools/fortress-gatekeeper.ps1` now:
1. Rebases each branch onto current master HEAD before merging (safe — all worktree branches are single-commit linear)
2. Captures the `--ff-only` exit code — on failure: **HALT + preserve branch + skip** via `continue`
3. Only tears down worktree/branch if merge **succeeded**

**⛔ Operational Rule**: NEVER create two worktrees in the same session from the same base commit and run the gatekeeper expecting both to merge cleanly in one pass. **Create worktree 1 → merge it → THEN create worktree 2.** Or rely on the rebase-before-merge step in the patched gatekeeper.

### 🏆 VS-002: Gitignore Shadow Zone — Lost Wear OS Files (2026-06-03)
**Symptom**: Files created via `write_to_file` inside `android/sk8lytzWear/` in a worktree pass all tooling successfully. But `git add .` silently ignores them. After the gatekeeper merges the worktree and deletes it, the files are permanently lost — zero trace in git history.

**Root Cause**: Root `.gitignore` line 44 contained `/android` — a standard Expo managed workflow convention that ignores the entire generated `android/` directory. Git negation rules (`!android/sk8lytzWear/`) didn't override this because the parent directory was already excluded. The `android/.gitignore` (which had its own fine-grained rules) was never consulted because the root rule blocked the entire tree first.

**Fix Applied**:
1. Added `!/android/sk8lytzWear/` negation rule to root `.gitignore` (immediately after `/android`)
2. Used `git add --force android/sk8lytzWear/` for the initial track (negation alone wasn't sufficient)
3. Future commits track the directory normally after the initial force-add

**⛔ Operational Rule**: Before committing in ANY worktree, ALWAYS run `git status --short` and verify the expected file count matches your changes. If files are missing, run `git check-ignore -v <path>` to diagnose. NEVER assume `git add .` captured everything — gitignore rules are silent killers.

### 🏆 VS-003: Documentation Drift — 16 Commits of Silent Staleness (2026-06-06)
**Symptom**: `tools/SK8Lytz_App_Master_Reference.md` declared the heartbeat system "DELETED" while live code had a brand new `useBLEHeartbeat` hook. The Hook Registry claimed "All 18 Hooks" when 24+ existed. The Auto-Recovery section documented "5 retries with 2s,4s,8s,16s,30s" when the actual implementation used 3-phase exponential backoff with jitter. The inline Protocol Bible copy (870 lines) was silently diverging from the standalone file.

**Root Cause**: **Zero documentation gates existed** in the entire pipeline. Every checkpoint (Kanban Constitution §7, §10, `/start-task` Phase 6, `/ship-it` Phase 3, `/diff-review`, Safety Protocol §2) verified CODE correctness (TypeScript, Jest, ADB logcat) but NONE checked documentation correctness. The Boy Scout Rule applied only to code files being edited, not to docs. This allowed 16 consecutive commits of architectural changes (6 BLE sub-hooks, 6 extracted services, 4 platform guards, write queue, heartbeat, RSSI monitor) to land on master with zero documentation updates.

**Fix Applied**:
1. **Kanban Constitution Rule 12** ("Documentation Parity Gate") — hard gate on task `[x]` stamping requiring Master Reference update for any architectural change
2. **`/start-task` Phase 5.5** — explicit documentation parity check inserted between QA and Release Manager phases
3. **`/diff-review` checklist** — added "📖 Documentation Parity" row to automated code review
4. **`agent-behavior.md` Rule 10** — added VS-003 evolved rule reference for always-on visibility
5. Deduplicated the inline Protocol Bible (§12) — replaced 870 lines with canonical pointer to standalone file

**⛔ Operational Rule**: When completing ANY task that creates new hooks, services, components, or modifies BLE architecture/protocol/types, you MUST update `tools/SK8Lytz_App_Master_Reference.md` BEFORE running the fortress gatekeeper. Explicitly state: `"Documentation parity check: [sections updated]"` or `"Documentation parity check: no architectural changes — docs gate skipped"`.
