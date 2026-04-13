---
trigger: always_on
---

# Tool Playbook — Proven Command Registry

**MANDATORY RULE**: Before writing any complex terminal command, PowerShell pipeline, or search query, you MUST first check this document. If an entry exists for your intent, use the exact proven syntax. Do NOT paraphrase or rewrite it from memory.

When you discover a new pattern that works well, trigger the **Victory Snapshot** protocol (RULE[meta-evolution.md]) to permanently add it here.

---

## 🔑 Machine Constants

These are environment-specific paths. Use these variables rather than hardcoding paths.

```powershell
# ADB executable (machine-specific)
$ADB = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\.local-builder\android-sdk\platform-tools\adb.exe"

# Project root
$ROOT = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"

# APK output path (release)
$APK = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\android\app\build\outputs\apk\release\SK8Lytz.apk"
```

---

## 📁 File Discovery

### Monolith Scan — Files >30KB in `src/`

```powershell
Get-ChildItem -Path src -Recurse -File |
  Where-Object { $_.Length -gt 30720 -and $_.Extension -match "\.(tsx|ts)$" } |
  Select-Object Name, @{Name="SizeKB";Expression={ [math]::round($_.Length / 1KB, 2) }} |
  Sort-Object SizeKB -Descending
```

### God Object Scan — Components with >15 hook usages

```powershell
Get-ChildItem -Path src -Recurse -Include *.tsx,*.ts |
  ForEach-Object {
    $Hooks = Select-String -Path $_.FullName -Pattern "use[A-Z]" -AllMatches
    if ($Hooks.Matches.Count -gt 15) {
      [PSCustomObject]@{ File=$_.Name; HookCount=$Hooks.Matches.Count }
    }
  } | Sort-Object HookCount -Descending
```

### TODO/FIXME/HACK Hunt

```powershell
Get-ChildItem -Path src -Recurse -Include *.tsx,*.ts |
  Select-String "TODO:|FIXME:|HACK:" |
  Select-Object Filename, LineNumber, Line
```

### Search File Contents (In-place text search, case-insensitive)

Use the native `grep_search` tool with `CaseInsensitive: true` — do NOT use PowerShell for this.

---

## 🔀 Git Operations

### Get current branch

```powershell
git branch --show-current
```

### Last 5 commits (short)

```powershell
git log -5 --oneline --decorate
```

### Full working tree diff

```powershell
git diff HEAD
```

### Short status

```powershell
git status --short
```

### Kill uncommitted changes (nuclear reset)

```powershell
git reset --hard HEAD
git clean -fd
```

### In-place file string replacement (PowerShell)

```powershell
(Get-Content "path\to\file.ts") -replace 'OLD_STRING', 'NEW_STRING' | Set-Content "path\to\file.ts"
```

---

## 📱 BLE / Hardware (ADB)

> Use `$ADB` constant from Machine Constants section above.

### Check connected devices

```powershell
& $ADB devices
```

### Install release APK (with replace flag)

```powershell
& $ADB install -r $APK
```

### Launch app on device

```powershell
& $ADB shell am start -n com.neogleamz.sk8lytz/.MainActivity
```

### Stream BLE/Bluetooth logs (live logcat)

```powershell
& $ADB logcat -s "BluetoothGatt:D BluetoothLeScanner:D ReactNativeJS:V"
```

### Clear logcat buffer

```powershell
& $ADB logcat -c
```

### Get last 200 lines of app logs

```powershell
& $ADB logcat -d | Select-Object -Last 200
```

---

## 🗄️ Database (Supabase)

### Generate TypeScript Types

Use the `mcp_supabase-mcp-server_generate_typescript_types` MCP tool.

- If project_id is unknown, call `mcp_supabase-mcp-server_list_projects` first.
- After generation, overwrite `src/types/supabase.ts` using `write_to_file` with `Overwrite: true`.

### Check recent API/Auth errors

Use the `mcp_supabase-mcp-server_get_logs` MCP tool with `service: "api"` or `service: "auth"`.

---

## 🔨 Build System

### TypeScript check (no compile)

```powershell
npx tsc --noEmit 2>&1 | Select-String "error TS" | Select-Object -First 30
```

### Expo start (clean cache)

```powershell
npx expo start --clear
```

### Gradle clean

```powershell
Set-Location android
.\gradlew.bat clean
Set-Location ..
```

### Kill Node/Metro processes

```powershell
Get-Process -Name node -ErrorAction SilentlyContinue | Stop-Process -Force
```

### Check if Metro port 8081 is in use

```powershell
netstat -ano | Select-String ":8081"
```

---

## 📊 Analysis Patterns

### Count lines in a file

```powershell
(Get-Content "path\to\file.ts" | Measure-Object -Line).Lines
```

### Find large recent APK builds

```powershell
Get-ChildItem android\app\build\outputs\apk -Recurse -Filter "*.apk" |
  Select-Object Name, LastWriteTime, @{N='Size(MB)';E={[math]::Round($_.Length/1MB,2)}}
```
