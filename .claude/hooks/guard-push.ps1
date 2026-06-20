# PreToolUse guard - Zero-Bypass Push Gate (Safety Rule 6 + 7)
# Blocks `git push` unless a fresh .test-attestation.json exists (anchored to HEAD).
$ErrorActionPreference = 'SilentlyContinue'
$raw = [Console]::In.ReadToEnd()
try { $data = $raw | ConvertFrom-Json } catch { exit 0 }

$cmd = $data.tool_input.command
if (-not $cmd) { exit 0 }
if ($cmd -notmatch 'git\s+push') { exit 0 }

$root   = (Resolve-Path "$PSScriptRoot\..\..").Path
$attest = Join-Path $root '.test-attestation.json'

if (-not (Test-Path $attest)) {
  [Console]::Error.WriteLine("BLOCKED - Zero-Bypass Push Gate (Safety Rule 6): .test-attestation.json not found. Run 'npm run verify' AFTER your final commit, then push. Per Rule 7, also confirm explicit user consent before pushing.")
  exit 2
}

# Freshness: attestation must be newer than the HEAD commit it claims to attest.
$attestTime = (Get-Item $attest).LastWriteTimeUtc
$headIso = (git -C $root log -1 --format=%cI 2>$null)
if ($headIso) {
  try {
    $headTime = ([datetimeoffset]$headIso).UtcDateTime
    if ($attestTime -lt $headTime) {
      [Console]::Error.WriteLine("BLOCKED - Push Gate: .test-attestation.json is STALE (older than the current HEAD commit). Re-run 'npm run verify' to re-anchor the attestation to this commit before pushing.")
      exit 2
    }
  } catch { }
}
exit 0
