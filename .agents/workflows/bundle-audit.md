---
description: Expo bundle size audit — analyze JS bundle size, find large modules, flag regressions
---

# Bundle Audit Engine — "/bundle-audit"

// turbo-all

When invoked via `/bundle-audit` or as Phase 1 Step 4 of `/ship-it`, analyze the JavaScript bundle to catch size regressions before release.

### Step 1: Run the TypeScript & Module Size Check

```powershell
Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"

# Scan for largest source files (proxy for bundle weight)
Get-ChildItem -Path src -Recurse -File -Include *.ts,*.tsx |
  Sort-Object Length -Descending |
  Select-Object -First 15 |
  Select-Object Name, @{Name="SizeKB";Expression={ [math]::Round($_.Length/1KB, 1) }}, DirectoryName |
  Format-Table -AutoSize
```

### Step 2: Check node_modules for Heavyweight Packages

```powershell
# Flag any single package directory over 5MB
Get-ChildItem -Path node_modules -Directory |
  Where-Object { (Get-ChildItem $_.FullName -Recurse -File | Measure-Object -Property Length -Sum).Sum -gt 5MB } |
  Select-Object Name, @{Name="SizeMB";Expression={
    [math]::Round((Get-ChildItem $_.FullName -Recurse -File | Measure-Object -Property Length -Sum).Sum/1MB, 1)
  }} |
  Sort-Object SizeMB -Descending |
  Select-Object -First 10 |
  Format-Table -AutoSize
```

### Step 3: Scan for Duplicate / Redundant Dependencies

```powershell
# Check if any library solving the same problem is imported twice (e.g. two date libs)
Get-Content "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\package.json" |
  ConvertFrom-Json |
  Select-Object -ExpandProperty dependencies |
  Format-List
```

### Step 4: God Object Scan (files >30KB that could be split)

```powershell
Get-ChildItem -Path src -Recurse -File |
  Where-Object { $_.Length -gt 30720 -and $_.Extension -match "\.(tsx|ts)$" } |
  Select-Object Name, @{Name="SizeKB";Expression={ [math]::Round($_.Length/1KB, 2) }} |
  Sort-Object SizeKB -Descending
```

### Step 5: Report

Output a structured summary in chat:

```
## 📦 Bundle Audit Report

| Check | Status | Notes |
|-------|--------|-------|
| Top 15 largest src files | ✅/⚠️ | <largest file + size> |
| Heavy node_modules (>5MB) | ✅/⚠️ | <list> |
| God Objects (>30KB) | ✅/⚠️ | <list or "none"> |

**Verdict: SHIP IT ✅ / FLAG ⚠️**
```

- Flag anything over 100KB as a tech debt item.
- Do NOT block the release for bundle size alone unless a single source file exceeds 200KB.
