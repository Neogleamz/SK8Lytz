# analyze-friction.ps1
# PowerShell launcher for SDE Friction Analyzer rule-evolution.

$ErrorActionPreference = "Stop"

$workspaceDir = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
$analyzerScript = Join-Path $workspaceDir "tools\sentinel\friction_analyzer.py"
$rulesGuardScript = Join-Path $workspaceDir "tools\sentinel-rules-guard.ps1"
$amendmentFile = Join-Path $workspaceDir ".agents\rules\friction-rule-amendments.md"

Write-Host "Starting SDE Closed-Loop Friction Analysis..." -ForegroundColor Cyan

if (Test-Path $amendmentFile) {
    Remove-Item $amendmentFile -Force | Out-Null
}

# Run python script
& python $analyzerScript

# If rule evolution was generated, let the user know
if (Test-Path $amendmentFile) {
    Write-Host "`n[SDE EVOLVED] Proposed rule amendments have been drafted in: $amendmentFile" -ForegroundColor Green
    Write-Host "To lock in these rules, append them to .agents\rules\agent-behavior.md and run tools\sentinel-rules-guard.ps1." -ForegroundColor Green
    
    # Run the guard to sync current rules
    & powershell.exe -ExecutionPolicy Bypass -File $rulesGuardScript
} else {
    Write-Host "`nWorkspace remains optimized. No new rules generated." -ForegroundColor Cyan
}
