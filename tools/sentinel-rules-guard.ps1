# Sentinel Rules Guard - The Immune System for SK8Lytz Agentic OS Rules
# Saves active rules to a global backup and restores them if they are corrupted or missing.

$ErrorActionPreference = "Stop"

$workspaceDir = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
$workspaceRulesDir = Join-Path $workspaceDir ".agents\rules"
$globalBackupDir = Join-Path $workspaceDir "artifacts\rules-backup"

$rules = @("agent-behavior.md", "safety-protocol.md", "prime-directive.md", "kanban-constitution.md", "sub-agent-behavior.md")

# Ensure folders exist
if (-not (Test-Path $globalBackupDir)) {
    New-Item -ItemType Directory -Path $globalBackupDir -Force | Out-Null
    Write-Host "Created global rules backup directory: $globalBackupDir" -ForegroundColor Cyan
}

if (-not (Test-Path $workspaceRulesDir)) {
    New-Item -ItemType Directory -Path $workspaceRulesDir -Force | Out-Null
    Write-Host "Created workspace rules directory: $workspaceRulesDir" -ForegroundColor Cyan
}

function Validate-RuleHeader {
    param (
        [string]$filePath
    )
    if (-not (Test-Path $filePath)) { return $false }
    $content = Get-Content $filePath -Raw
    
    # Check for the YAML trigger always_on frontmatter
    $hasTrigger = $content -match "(?s)^---\r?\ntrigger:\s*always_on\r?\n---"
    return $hasTrigger
}

# Process each rule
foreach ($rule in $rules) {
    $workspaceFile = Join-Path $workspaceRulesDir $rule
    $backupFile = Join-Path $globalBackupDir $rule

    $workspaceExists = Test-Path $workspaceFile
    $backupExists = Test-Path $backupFile

    Write-Host "Auditing Rule: $rule..." -ForegroundColor White

    if (-not $workspaceExists -and $backupExists) {
        # Restore from backup
        Copy-Item -Path $backupFile -Destination $workspaceFile -Force
        Write-Host "  [HEALED] Restored rule from global backup to workspace." -ForegroundColor Green
    }
    elseif ($workspaceExists) {
        $isValid = Validate-RuleHeader $workspaceFile
        if (-not $isValid) {
            Write-Host "  [WARNING] Rule trigger is missing/malformed in workspace!" -ForegroundColor Yellow
            if ($backupExists) {
                # Restore to clean backup state
                Copy-Item -Path $backupFile -Destination $workspaceFile -Force
                Write-Host "  [HEALED] Replaced invalid workspace rule with clean global backup." -ForegroundColor Green
            } else {
                # Inject trigger block manually
                $rawContent = Get-Content $workspaceFile -Raw
                $injected = "---`ntrigger: always_on`n---`n`n" + $rawContent.TrimStart()
                $injected | Out-File -FilePath $workspaceFile -Encoding utf8 -Force
                Write-Host "  [HEALED] Injected always_on trigger into workspace rule." -ForegroundColor Green
            }
        }
        
        # Backup the validated workspace rule
        Copy-Item -Path $workspaceFile -Destination $backupFile -Force
        Write-Host "  [SYNC] Rule backed up to global secure storage." -ForegroundColor Cyan
    }
    else {
        # Total failure case - neither exists
        Write-Error "CRITICAL: Rule $rule does not exist in workspace or backup! Restore failed."
    }
}

# -------------------------------------------------------------
# Bucket List Guard
# -------------------------------------------------------------
$bucketListFile = Join-Path $workspaceDir "tools\SK8Lytz_Bucket_List.md"
$globalBucketBackupDir = Join-Path $workspaceDir "artifacts\bucket-list-backup"
$bucketListBackupFile = Join-Path $globalBucketBackupDir "SK8Lytz_Bucket_List.md"

if (-not (Test-Path $globalBucketBackupDir)) {
    New-Item -ItemType Directory -Path $globalBucketBackupDir -Force | Out-Null
}

Write-Host "Auditing Bucket List: SK8Lytz_Bucket_List.md..." -ForegroundColor White

$bucketWorkspaceExists = Test-Path $bucketListFile
$bucketBackupExists = Test-Path $bucketListBackupFile

if (-not $bucketWorkspaceExists -and $bucketBackupExists) {
    Copy-Item -Path $bucketListBackupFile -Destination $bucketListFile -Force
    Write-Host "  [HEALED] Restored SK8Lytz_Bucket_List.md from global backup to workspace." -ForegroundColor Green
}
elseif ($bucketWorkspaceExists) {
    # Check if the file is empty or too small (which indicates corruption)
    $fileSize = (Get-Item $bucketListFile).Length
    if ($fileSize -lt 100 -and $bucketBackupExists) {
        Copy-Item -Path $bucketListBackupFile -Destination $bucketListFile -Force
        Write-Host "  [HEALED] Restored corrupted/truncated SK8Lytz_Bucket_List.md from global backup." -ForegroundColor Green
    } else {
        Copy-Item -Path $bucketListFile -Destination $bucketListBackupFile -Force
        Write-Host "  [SYNC] SK8Lytz_Bucket_List.md backed up to global secure storage." -ForegroundColor Cyan
    }
}
else {
    Write-Host "  [WARNING] SK8Lytz_Bucket_List.md not found in workspace or backup." -ForegroundColor Yellow
}

Write-Host "Sentinel Rules Guard Execution Complete. 0 failures." -ForegroundColor Green
