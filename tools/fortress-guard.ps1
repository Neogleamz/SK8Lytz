$RootPath = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
$CurrentPath = (Get-Location).Path

# Only check if they are in the master fortress directory
if ($CurrentPath -eq $RootPath) {
    $Worktrees = git worktree list | Select-String "SK8Lytz-worktrees"
    if ($Worktrees) {
        Write-Host ""
        Write-Host "=================================================================" -ForegroundColor Red -BackgroundColor Black
        Write-Host "🚨 MASTER FORTRESS LOCK ACTIVE 🚨" -ForegroundColor Red -BackgroundColor Black
        Write-Host "You are in the master directory ($RootPath)" -ForegroundColor Yellow
        Write-Host "BUT there is an active worktree open!" -ForegroundColor Yellow
        Write-Host "=================================================================" -ForegroundColor Red -BackgroundColor Black
        Write-Host "⛔ YOU ARE STRICTLY FORBIDDEN FROM EDITING FILES IN THIS FOLDER ⛔" -ForegroundColor Red
        Write-Host "All writes MUST target the active worktree path:" -ForegroundColor Yellow
        $Worktrees | ForEach-Object {
            Write-Host "👉 $($_.Line.Split(' ')[0])" -ForegroundColor Cyan
        }
        Write-Host "=================================================================" -ForegroundColor Red -BackgroundColor Black
        Write-Host ""
        exit 1
    }
}
exit 0
