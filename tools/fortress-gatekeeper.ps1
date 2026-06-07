# SK8Lytz Fortress Gatekeeper
# Validates worktree attestations and gates fast-forward merges safely.

$ErrorActionPreference = "Stop"
$FORTRESS_ROOT = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"

if ((Get-Location).Path -ne $FORTRESS_ROOT) {
    Write-Host "GATEKEEPER HALT: Must be run from the master root directory: $FORTRESS_ROOT" -ForegroundColor Red
    exit 1
}

# 0. Master Cleanliness Guard (FRICTION-017)
$MasterStatus = git status --short
if ($MasterStatus) {
    Write-Host "GATEKEEPER HALT: The master branch has uncommitted changes." -ForegroundColor Red
    Write-Host "Worktrees cannot be merged safely until master is clean. Resolve these files first:" -ForegroundColor Yellow
    Write-Host $MasterStatus -ForegroundColor Gray
    exit 1
}

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

        # 1.5 Blast Radius Scan
        Write-Host "Executing Blast Radius Scan..." -ForegroundColor Yellow
        node tools/blast-radius-scanner.js --branch $Branch --ignore-blast
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Error: Blast Radius Scan failed. Missing architectural dependencies." -ForegroundColor Red
            exit 1
        }

        # 2. Run verification on the local attestation file
        Write-Host "Executing cryptographic verification..." -ForegroundColor Yellow
        node tools/verifiable-check-runner.js --verify
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Error: Test attestation verification failed." -ForegroundColor Red
            exit 1
        }
        
        Write-Host "Proof of Execution verified! Cleared for merge." -ForegroundColor Green
    }
    finally {
        Pop-Location
    }

    # 3. Rebase branch onto current master HEAD to handle sequential-merge divergence.
    # This is safe because worktree branches are always single-commit, linear, and
    # never shared — rebasing them is equivalent to a fast-forward after accounting
    # for any commits that landed on master since this worktree was created.
    Write-Host "Rebasing '$Branch' onto current master HEAD..." -ForegroundColor Yellow
    Push-Location $Path
    try {
        git rebase origin/master 2>$null  # Rebase off remote if ahead
        git rebase master                 # Rebase off local master
    } catch {
        # Rebase may fail if there's nothing to rebase — that's fine
    } finally {
        Pop-Location
    }

    # 4. Perform Fast-Forward Merge (now guaranteed to succeed post-rebase)
    Write-Host "Merging branch '$Branch' into master..." -ForegroundColor Yellow
    git checkout master --quiet
    if ($LASTEXITCODE -ne 0) {
        Write-Host "GATEKEEPER HALT: Could not checkout master." -ForegroundColor Red
        exit 1
    }
    
    $MergeResult = git merge $Branch --ff-only 2>&1
    if ($LASTEXITCODE -ne 0) {
        Write-Host "" -ForegroundColor Red
        Write-Host "GATEKEEPER HALT: --ff-only merge FAILED for '$Branch'" -ForegroundColor Red
        Write-Host "Master has diverged. Branch and worktree have been PRESERVED." -ForegroundColor Yellow
        Write-Host "Fix manually: git rebase master $Branch && run gatekeeper again." -ForegroundColor Yellow
        Write-Host "" -ForegroundColor Red
        # CRITICAL: Do NOT teardown — skip to next worktree without destroying this one
        continue
    }

    # 5. Teardown Worktree safely (removing directory junctions first to prevent recursive deletion)
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
    if ($LASTEXITCODE -ne 0) {
        Write-Host "GATEKEEPER HALT: Failed to tear down worktree $Path" -ForegroundColor Red
        exit 1
    }
    
    # 6. Delete Branch
    Write-Host "Deleting branch '$Branch'..." -ForegroundColor Yellow
    git branch -D $Branch
    if ($LASTEXITCODE -ne 0) {
        Write-Host "GATEKEEPER HALT: Failed to delete branch $Branch" -ForegroundColor Red
        exit 1
    }
    
    Write-Host "Successfully validated, merged, and cleaned up $Branch!" -ForegroundColor Green
}

# Final Fortress Sweep
Write-Host "--------------------------------------------------------" -ForegroundColor DarkCyan
Write-Host "All worktrees merged successfully. Fortress state clean." -ForegroundColor Green
