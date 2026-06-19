# /tsc-check — TypeScript Audit

**Description:** Run the TypeScript compiler in no-emit mode and report all errors in a clean, scannable format.
**Persona:** ⚒️ Dev — Sage

> **⚒️ Dev — Sage | TypeScript Audit Active**
> *Sage treats type errors like compile errors — because they are. Zero `any` casts. Zero ignored errors. Fix the type, ship clean code.*

> ⚠️ **S7 NOTE**: This workflow previously used `npx tsc --noEmit` directly. Per S7, raw `tsc`/`jest` commands are banned. All checks now route through `npm run verify`.

1. Run the unified check suite and capture TypeScript errors

```powershell
Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
$verifyOutput = npm run verify 2>&1
$errorLines = $verifyOutput | Select-String "error TS"
$count = ($errorLines | Measure-Object).Count
Write-Host "=== TypeScript Audit ===" -ForegroundColor Cyan
Write-Host "Total TS errors found: $count" -ForegroundColor $(if ($count -eq 0) { 'Green' } else { 'Red' })
$errorLines | Select-Object -First 30
```

2. Group errors by file for targeted fixing

```powershell
$verifyOutput | Select-String "error TS" |
  ForEach-Object { $_.Line -replace "\(.*", "" } |
  Sort-Object -Unique |
  ForEach-Object { Write-Host "  FILE: $_" -ForegroundColor Yellow }
```

3. If errors exist, report them to the chat with grouped file paths and propose targeted fixes using `replace_file_content` — one file at a time, smallest error count first.
