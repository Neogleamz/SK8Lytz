# Plugin Schema & Layout Validator - Prevents IDE parser crashes
# Audits all folders under C:\Users\Magma\.gemini\config\plugins\

$ErrorActionPreference = "Stop"

$pluginsDir = Join-Path $env:USERPROFILE ".gemini\config\plugins"

if (-not (Test-Path $pluginsDir)) {
    Write-Host "Plugins directory does not exist: $pluginsDir. Skipping." -ForegroundColor Yellow
    exit 0
}

$pluginFolders = Get-ChildItem -Path $pluginsDir -Directory

Write-Host "Starting audit of loaded Antigravity plugins..." -ForegroundColor Cyan
$failures = 0

foreach ($folder in $pluginFolders) {
    $folderName = $folder.Name
    $pluginPath = $folder.FullName
    Write-Host "Auditing plugin: $folderName" -ForegroundColor White

    # 1. Check for plugin.json
    $jsonPath = Join-Path $pluginPath "plugin.json"
    if (-not (Test-Path $jsonPath)) {
        Write-Host "  [ERROR] Missing plugin.json in root!" -ForegroundColor Red
        $failures++
        continue
    }

    # 2. Parse plugin.json to check validity
    try {
        $jsonContent = Get-Content $jsonPath -Raw | ConvertFrom-Json
        
        $requiredFields = @("name", "version", "description")
        foreach ($field in $requiredFields) {
            if (-not $jsonContent.$field) {
                Write-Host "  [ERROR] plugin.json is missing required field: $field" -ForegroundColor Red
                $failures++
            }
        }
    }
    catch {
        Write-Host "  [ERROR] Failed to parse plugin.json as valid JSON!" -ForegroundColor Red
        Write-Host "  Parsing Error: $_" -ForegroundColor Red
        $failures++
        continue
    }

    # 3. Check and audit skills directory
    $skillsDir = Join-Path $pluginPath "skills"
    if (Test-Path $skillsDir) {
        $skillFiles = Get-ChildItem -Path $skillsDir -Filter "SKILL.md" -File
        if ($skillFiles.Count -eq 0) {
            Write-Host "  [WARNING] 'skills' directory exists but has no SKILL.md!" -ForegroundColor Yellow
        }
        else {
            foreach ($skillFile in $skillFiles) {
                # Validate SKILL.md YAML frontmatter trigger
                $skillContent = Get-Content $skillFile.FullName -Raw
                $hasFrontmatter = $skillContent -match "(?s)^---\r?\nname:\s*([^\r\n]+)\r?\ndescription:\s*([^\r\n]+)\r?\n---"
                if (-not $hasFrontmatter) {
                    Write-Host "  [ERROR] Skill file $($skillFile.Name) has invalid/missing YAML frontmatter!" -ForegroundColor Red
                    $failures++
                } else {
                    Write-Host "  [OK] Valid skill layout verified for $($skillFile.Name)." -ForegroundColor Green
                }
            }
        }
    }
    else {
        Write-Host "  [INFO] No skills directory declared. Checking if SKILL.md exists in root (legacy format)." -ForegroundColor Gray
        $rootSkill = Join-Path $pluginPath "SKILL.md"
        if (Test-Path $rootSkill) {
            Write-Host "  [HEAL] Found legacy root-level SKILL.md. Relocating to proper skills/ folder structure..." -ForegroundColor Yellow
            $newSkillsDir = Join-Path $pluginPath "skills"
            New-Item -ItemType Directory -Path $newSkillsDir -Force | Out-Null
            Move-Item -Path $rootSkill -Destination (Join-Path $newSkillsDir "SKILL.md") -Force
            Write-Host "  [HEALED] Restructured plugin folder cleanly to plugins/$folderName/skills/SKILL.md." -ForegroundColor Green
        }
    }
}

if ($failures -gt 0) {
    Write-Host "Audit completed with $failures errors. Clean them up to prevent settings errors." -ForegroundColor Red
    exit 1
} else {
    Write-Host "Audit completed successfully. All plugins are fully schema-compliant!" -ForegroundColor Green
}
