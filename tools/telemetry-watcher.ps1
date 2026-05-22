# telemetry-watcher.ps1
# Runs the proactive SDE Telemetry Healer to parse crashes, log triage tickets, and spawn isolated worktrees.

$ErrorActionPreference = "Stop"

$workspaceDir = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
$healerScript = Join-Path $workspaceDir "tools\sentinel\telemetry_healer.py"

Write-Host "Triggering Proactive SDE Telemetry Healing sweep..." -ForegroundColor Cyan

# Run python script
& python $healerScript

Write-Host "Telemetry Healing sweep finalized." -ForegroundColor Cyan
