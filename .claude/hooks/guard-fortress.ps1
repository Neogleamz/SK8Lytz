# PreToolUse guard — Master Fortress Lock (Safety Rules 1 & 2)
# Blocks edits to master SOURCE files (src/android/ios) while a worktree is active.
# Doc/config edits on master (docs/, .claude/, CLAUDE.md, bucket list) remain allowed.
$ErrorActionPreference = 'SilentlyContinue'
$raw = [Console]::In.ReadToEnd()
try { $data = $raw | ConvertFrom-Json } catch { exit 0 }

$fp = $data.tool_input.file_path
if (-not $fp) { exit 0 }

$root = (Resolve-Path "$PSScriptRoot\..\..").Path
if (-not [System.IO.Path]::IsPathRooted($fp)) { $fp = Join-Path $root $fp }

# Only the master fortress dir is gated. Worktrees live OUTSIDE root (../SK8Lytz-worktrees) — never matched.
if (-not $fp.StartsWith($root, [System.StringComparison]::OrdinalIgnoreCase)) { exit 0 }

$rel = $fp.Substring($root.Length).TrimStart('\','/')
if ($rel -notmatch '^(src|android|ios)[\\/]') { exit 0 }   # only source dirs are locked

# Is a worktree currently active? (more than just the main checkout)
$wt = git -C $root worktree list 2>$null
$count = ($wt | Measure-Object -Line).Lines
if ($count -gt 1) {
  [Console]::Error.WriteLine("BLOCKED - Master Fortress Lock (Safety Rules 1-2): editing master source ('$rel') while a worktree is active is forbidden. Make this change inside the worktree under ..\SK8Lytz-worktrees\, or merge/remove the worktree first.")
  exit 2
}
exit 0
