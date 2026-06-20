# Statusline - shows SK8Lytz | persona | model | worktree | context%
$ErrorActionPreference = 'SilentlyContinue'
$raw = [Console]::In.ReadToEnd()
try { $d = $raw | ConvertFrom-Json } catch { Write-Output "SK8Lytz"; exit 0 }

$model   = $d.model.display_name
$wt      = $d.workspace.git_worktree
$pct     = $d.context_window.used_percentage
$agent   = $d.agent.name
$persona = if ($agent) { $agent } else { "main" }

$parts = @("SK8Lytz", "[$persona]")
if ($model) { $parts += $model }
if ($wt)    { $parts += "git:$wt" }
if ($pct)   { $parts += ("ctx {0:N0}%" -f [double]$pct) }
Write-Output ($parts -join "  |  ")
