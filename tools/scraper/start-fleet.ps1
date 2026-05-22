# SK8Lytz Scraper Fleet Launcher
# This script initializes the PM2 process manager with the Scraper, Dashboard, and Discord Bridge.

Write-Host "🛹 Initializing SK8Lytz Scraper Fleet..." -ForegroundColor Cyan

# Check for PM2
if (!(Get-Command pm2 -ErrorAction SilentlyContinue)) {
    Write-Host "❌ Error: PM2 is not installed. Please run 'npm install -g pm2' first." -ForegroundColor Red
    exit 1
}

# Ensure logs directory exists
if (!(Test-Path "./logs")) {
    New-Item -ItemType Directory -Path "./logs" | Out-Null
}

# Start ONLY infrastructure (CCTower + Dashboard + Discord Bridge)
# Daemons (Indexer, Photographer, Publisher) are NEVER auto-started.
# Start them from the Dashboard UI at http://localhost:5173.
pm2 start ecosystem.config.js --only sk8lytz-cctower
pm2 start ecosystem.config.js --only scraper-dashboard
pm2 start ecosystem.config.js --only discord-bridge

Write-Host "`n✅ Fleet deployed! Use 'pm2 status' to monitor." -ForegroundColor Green
Write-Host "Dashboard: http://localhost:5173" -ForegroundColor Yellow
Write-Host "API: http://localhost:5999" -ForegroundColor Yellow
Write-Host "⚠️  Daemons are STOPPED — start them from the Dashboard UI." -ForegroundColor DarkGray
