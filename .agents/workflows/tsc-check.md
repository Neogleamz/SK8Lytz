---
description: Run the TypeScript compiler in no-emit mode and report all errors in a clean, scannable format
---

// turbo-all

1. Run TSC in no-emit mode and capture all errors

```powershell
Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
$errors = npx tsc --noEmit 2>&1
$errorLines = $errors | Select-String "error TS"
$count = ($errorLines | Measure-Object).Count
Write-Host "=== TypeScript Audit ===" -ForegroundColor Cyan
Write-Host "Total errors found: $count" -ForegroundColor $(if ($count -eq 0) { 'Green' } else { 'Red' })
$errorLines | Select-Object -First 30
```

2. Group errors by file for targeted fixing

```powershell
$errors | Select-String "error TS" |
  ForEach-Object { $_.Line -replace "\(.*", "" } |
  Sort-Object -Unique |
  ForEach-Object { Write-Host "  FILE: $_" -ForegroundColor Yellow }
```

3. If errors exist, report them to the chat with grouped file paths and propose targeted fixes using `replace_file_content` — one file at a time, smallest error count first.
