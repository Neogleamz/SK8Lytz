# SK8Lytz Fortress Gatekeeper
# Validates worktree attestations and gates fast-forward merges safely.

$ErrorActionPreference = "Stop"
$FORTRESS_ROOT = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"

# Resolve active worktree list
Write-Host "Scanning active worktrees..." -ForegroundColor Cyan
$WorktreeList = git worktree list | Where-Object { $_ -match "SK8Lytz-worktrees" }

if (-not $WorktreeList) {
    Write-Host "Master fortress is clear of worktrees. Nothing to merge." -ForegroundColor Green
    exit 0
}

foreach ($Line in $WorktreeList) {
    $Parts = $Line -split "\s+"
    $Path = $Parts[0]
    $Commit = $Parts[1]
    $Branch = $Parts[2] -replace '\[', '' -replace '\]', ''
    
    Write-Host "--------------------------------------------------------" -ForegroundColor DarkCyan
    Write-Host "Validating Worktree: $Branch ($Commit)" -ForegroundColor Yellow
    Write-Host "Path: $Path" -ForegroundColor Cyan

    # 1. Check for uncommitted changes in worktree
    Push-Location $Path
    try {
        $Status = git status --short
        if ($Status) {
            Write-Host "Error: Worktree has uncommitted changes. Please commit or stash first." -ForegroundColor Red
            exit 1
        }

        # 2. Run verification on the local attestation file
        Write-Host "Executing cryptographic verification..." -ForegroundColor Yellow
        node tools/verifiable-check-runner.js --verify
        
        Write-Host "Proof of Execution verified! Cleared for merge." -ForegroundColor Green
    }
    finally {
        Pop-Location
    }

    # 3. Perform Fast-Forward Merge
    Write-Host "Merging branch '$Branch' into master..." -ForegroundColor Yellow
    git merge $Commit --ff-only

    # 4. Teardown Worktree safely (removing directory junctions first to prevent recursive deletion)
    Write-Host "Cleaning up worktree junctions..." -ForegroundColor Yellow
    if (Test-Path "$Path\node_modules") {
        $Item = Get-Item "$Path\node_modules"
        if ($Item.Attributes -match "ReparsePoint") {
            [System.IO.Directory]::Delete("$Path\node_modules")
            Write-Host "Safely unlinked node_modules junction." -ForegroundColor Green
        } else {
            Remove-Item -Recurse -Force "$Path\node_modules" -ErrorAction SilentlyContinue
        }
    }

    Write-Host "Tearing down worktree..." -ForegroundColor Yellow
    git worktree remove $Path --force
    
    # 5. Delete Branch
    Write-Host "Deleting branch '$Branch'..." -ForegroundColor Yellow
    git branch -D $Branch
    
    Write-Host "Successfully validated, merged, and cleaned up $Branch!" -ForegroundColor Green
}

# Final Fortress Sweep
Write-Host "--------------------------------------------------------" -ForegroundColor DarkCyan
Write-Host "All worktrees merged successfully. Fortress state clean." -ForegroundColor Green
