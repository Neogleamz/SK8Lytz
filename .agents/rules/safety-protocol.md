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

