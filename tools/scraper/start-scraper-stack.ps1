#!/usr/bin/env pwsh
<#
.SYNOPSIS
  SK8Lytz Scraper Stack Launcher
  Starts the CCTower API, Vite Dashboard, and Discord Bridge in the background
  via PM2 (no popup windows). Daemons (Operator, Indexer, Photographer) are
  managed from the web UI on http://localhost:5173 — they do NOT auto-start.

.USAGE
  .\tools\scraper\start-scraper-stack.ps1
#>

Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\tools\scraper"

Write-Host "🚀 SK8Lytz Scraper Stack — Starting up..." -ForegroundColor Cyan

# 1. Ensure log directory exists
New-Item -ItemType Directory -Force -Path ".\logs" | Out-Null

# 2. Start CCTower + scraper daemons via PM2 from ecosystem config
#    (daemons start STOPPED - they are managed from the web UI)
Write-Host "   📡 Starting CCTower API (port 5999)..." -ForegroundColor Yellow
$pm2Result = pm2 start ecosystem.config.js --only sk8lytz-cctower 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "   ⚠️  CCTower may already be running. Reloading..." -ForegroundColor Yellow
    pm2 reload sk8lytz-cctower 2>&1 | Out-Null
}

# Register daemon processes with PM2 (stopped state - they are started from web UI)
pm2 start ecosystem.config.js --only scraper-operator --stop-exit-codes 0 2>&1 | Out-Null
pm2 start ecosystem.config.js --only scraper-indexer --stop-exit-codes 0 2>&1 | Out-Null
pm2 start ecosystem.config.js --only scraper-photographer --stop-exit-codes 0 2>&1 | Out-Null
pm2 stop scraper-operator scraper-indexer scraper-photographer 2>&1 | Out-Null
Write-Host "   🤖 Daemons registered (STOPPED) - start them from the web UI" -ForegroundColor DarkGray

# 3. Start Discord Bridge via PM2 (always-on notification relay)
Write-Host "   🔔 Starting Discord Bridge..." -ForegroundColor Yellow
pm2 start "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\tools\discord-bridge\index.js" --name "discord-bridge" --cwd "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\tools\discord-bridge" 2>&1 | Out-Null

# 4. Start Vite Dashboard (scraper-dashboard dev server) via PM2
Write-Host "   🖥️  Starting Scraper Dashboard (port 5173)..." -ForegroundColor Yellow
$dashboardRunning = pm2 list 2>&1 | Select-String "scraper-dashboard"
if (-not $dashboardRunning) {
    pm2 start "node_modules/vite/bin/vite.js" --name "scraper-dashboard" --cwd "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\tools\scraper-dashboard" 2>&1 | Out-Null
} else {
    pm2 reload scraper-dashboard 2>&1 | Out-Null
}

# 5. Save PM2 process list so it survives reboots
pm2 save 2>&1 | Out-Null

Write-Host ""
Write-Host "✅ Stack is UP!" -ForegroundColor Green
Write-Host "   CCTower API  ->  http://localhost:5999" -ForegroundColor White
Write-Host "   Dashboard    ->  http://localhost:5173" -ForegroundColor White
Write-Host ""
Write-Host "   Daemons (start from web UI):" -ForegroundColor DarkGray
Write-Host "   • Operator (Phase 2 - OSM identity)  ->  STOPPED" -ForegroundColor DarkGray
Write-Host "   • Indexer  (Phase 3 - website crawl) ->  STOPPED" -ForegroundColor DarkGray
Write-Host "   • Photographer (Phase 4 - media)     ->  STOPPED" -ForegroundColor DarkGray
Write-Host ""
Write-Host "   To view PM2 process list: pm2 list" -ForegroundColor DarkGray
Write-Host "   To view live logs:        pm2 logs sk8lytz-cctower" -ForegroundColor DarkGray
