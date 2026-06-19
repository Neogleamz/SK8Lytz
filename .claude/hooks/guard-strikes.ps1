# PreToolUse guard — Three-Strike Debug Lockout (Safety Rule 10)
# Blocks further code edits once .debug-strikes.json shows attempts >= 3 (in root or any worktree).
$ErrorActionPreference = 'SilentlyContinue'
$raw = [Console]::In.ReadToEnd()
try { $data = $raw | ConvertFrom-Json } catch { exit 0 }

$root = (Resolve-Path "$PSScriptRoot\..\..").Path
$candidates = @((Join-Path $root '.debug-strikes.json'))

$wtBase = Join-Path (Split-Path $root -Parent) 'SK8Lytz-worktrees'
if (Test-Path $wtBase) {
  Get-ChildItem $wtBase -Directory -ErrorAction SilentlyContinue | ForEach-Object {
    $candidates += (Join-Path $_.FullName '.debug-strikes.json')
  }
}

foreach ($c in $candidates) {
  if (Test-Path $c) {
    try { $s = Get-Content $c -Raw | ConvertFrom-Json } catch { continue }
    if ($s.attempts -ge 3) {
      [Console]::Error.WriteLine("BLOCKED — Three-Strike Lockout (Safety Rule 10): debug attempts reached $($s.attempts) for bug '$($s.bug)'. HALT, run 'git reset --hard', and drop into consultative /brainstorming mode. Delete .debug-strikes.json to reset. Override ONLY with the passphrase: COWBOY MODE ACTIVATED.")
      exit 2
    }
  }
}
exit 0
