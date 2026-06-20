# PostToolUse check - No-any-cast Law (agent-behavior)
# Warns (exit 2 -> fed back to Claude) when a .ts/.tsx edit contains `as any` or @ts-ignore.
$ErrorActionPreference = 'SilentlyContinue'
$raw = [Console]::In.ReadToEnd()
try { $data = $raw | ConvertFrom-Json } catch { exit 0 }

$fp = $data.tool_input.file_path
if (-not $fp) { exit 0 }
if ($fp -notmatch '\.(ts|tsx)$') { exit 0 }

$root = (Resolve-Path "$PSScriptRoot\..\..").Path
if (-not [System.IO.Path]::IsPathRooted($fp)) { $fp = Join-Path $root $fp }
if (-not (Test-Path $fp)) { exit 0 }

$hits = Select-String -Path $fp -Pattern 'as\s+any\b', '@ts-ignore' -AllMatches
if ($hits) {
  $lines = ($hits | ForEach-Object { "  L$($_.LineNumber): $($_.Line.Trim())" }) -join "`n"
  [Console]::Error.WriteLine("No-any-cast Law violation in $fp :`n$lines`nFix the type signature or import correctly. Do NOT ship 'as any' or '@ts-ignore' (agent-behavior rule).")
  exit 2
}
exit 0
