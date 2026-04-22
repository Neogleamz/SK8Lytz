<#
.SYNOPSIS
    Automated backup script for the SK8Lytz Supabase Database.
.DESCRIPTION
    This script reads the database password from the local .env file,
    connects directly to the Supabase Postgres instance via the pooler,
    and dumps the schema, roles, and data into the backups/ directory.
    It automatically cleans up backups older than 7 days.
#>

$ProjectID = "qefmeivpjyaukbwadgaz"
$BackupDir = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\backups"

# Ensure backup directory exists
if (!(Test-Path -Path $BackupDir)) {
    New-Item -ItemType Directory -Path $BackupDir | Out-Null
    Write-Host "Created backup directory: $BackupDir"
}

# Cleanup old backups (older than 7 days)
Write-Host "Cleaning up old backups..."
Get-ChildItem -Path $BackupDir -Filter "*.sql" | Where-Object { $_.CreationTime -lt (Get-Date).AddDays(-7) } | Remove-Item -Force

# Read password from environment or .env
if (-not $env:SUPABASE_DB_PASSWORD) {
    $envFile = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\.env"
    if (Test-Path $envFile) {
        Get-Content $envFile | Where-Object { $_ -match "^SUPABASE_DB_PASSWORD=(.*)" } | ForEach-Object {
            $env:SUPABASE_DB_PASSWORD = $matches[1].Trim()
        }
    }
}

if (-not $env:SUPABASE_DB_PASSWORD) {
    Write-Error "ERROR: SUPABASE_DB_PASSWORD is not set in environment or .env file. Backup aborted."
    exit 1
}

$DateString = Get-Date -Format "yyyy-MM-dd_HH-mm"
$SchemaFile = "$BackupDir\schema_$DateString.sql"
$DataFile = "$BackupDir\data_$DateString.sql"
$RolesFile = "$BackupDir\roles_$DateString.sql"
$DB_URL = "postgresql://postgres.$ProjectID`:$($env:SUPABASE_DB_PASSWORD)@aws-0-us-west-2.pooler.supabase.com:6543/postgres"

Write-Host "Starting Supabase backup sequence for project: $ProjectID"
Write-Host "--------------------------------------------------------"

Write-Host "1/3: Backing up roles to $RolesFile"
npx supabase db dump --db-url $DB_URL --role-only -f $RolesFile

Write-Host "2/3: Backing up schema to $SchemaFile"
npx supabase db dump --db-url $DB_URL -f $SchemaFile

Write-Host "3/3: Backing up data to $DataFile"
npx supabase db dump --db-url $DB_URL --data-only -f $DataFile

Write-Host "--------------------------------------------------------"
if ((Test-Path $DataFile) -and (Get-Item $DataFile).Length -gt 0) {
    Write-Host "✅ Backup completed successfully!"
} else {
    Write-Host "❌ Backup may have failed. Data file is empty or missing."
}
