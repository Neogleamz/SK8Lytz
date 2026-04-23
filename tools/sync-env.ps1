# Syncs .env from worktree to master
param(
    [string]$WorktreeDir
)
if (-not $WorktreeDir) {
    $WorktreeDir = (Get-Location).Path
}

$MasterEnvPath = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\.env"
$WorktreeEnvPath = Join-Path $WorktreeDir ".env"

if (-not (Test-Path $WorktreeEnvPath)) {
    Write-Host "No .env found in worktree."
    exit
}
if (-not (Test-Path $MasterEnvPath)) {
    Copy-Item $WorktreeEnvPath $MasterEnvPath
    Write-Host "Copied new .env to master."
    exit
}

$MasterLines = Get-Content $MasterEnvPath
$WorktreeLines = Get-Content $WorktreeEnvPath

$MasterKeys = @{}
foreach ($line in $MasterLines) {
    if ($line -match "^([^#=]+)=(.*)$") {
        $MasterKeys[$matches[1]] = $matches[2]
    }
}

$AddedCount = 0
foreach ($line in $WorktreeLines) {
    if ($line -match "^([^#=]+)=(.*)$") {
        $key = $matches[1]
        if (-not $MasterKeys.ContainsKey($key)) {
            Add-Content -Path $MasterEnvPath -Value $line
            Write-Host "Added $key to master .env" -ForegroundColor Green
            $AddedCount++
        }
    }
}

Write-Host "✅ .env sync complete. $AddedCount keys added."
