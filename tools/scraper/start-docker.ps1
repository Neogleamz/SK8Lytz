Write-Host "🐳 SK8Lytz Scraper Stack — Booting via Docker Compose..." -ForegroundColor Cyan

# 0. Validate Docker is installed
try {
    $dockerVersion = docker --version 2>&1
    Write-Host "   📦 Docker verified ($dockerVersion)" -ForegroundColor DarkGray
} catch {
    Write-Host "   ❌ Docker not found! Please install Docker Desktop first." -ForegroundColor Red
    exit 1
}

# 1. Start the stack
Write-Host "   🚀 Spinning up containers..." -ForegroundColor Yellow
docker compose up -d --build

Write-Host "   ✅ Stack is ONLINE!" -ForegroundColor Green
Write-Host "      Dashboard: http://localhost:5998" -ForegroundColor DarkGray
Write-Host "      API: http://localhost:5999" -ForegroundColor DarkGray
