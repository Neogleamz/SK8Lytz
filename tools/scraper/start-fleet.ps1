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

# Start the fleet
pm2 start ecosystem.config.js

Write-Host "`n✅ Fleet deployed! Use 'pm2 status' to monitor." -ForegroundColor Green
Write-Host "Dashboard: http://localhost:5173" -ForegroundColor Yellow
Write-Host "API: http://localhost:3001" -ForegroundColor Yellow
