Write-Host "🐳 SK8Lytz Scraper Stack - Booting via Docker Compose..." -ForegroundColor Cyan

Write-Host "   🚀 Spinning up containers..." -ForegroundColor Yellow
docker compose up -d --build

Write-Host "   ✅ Stack is ONLINE!" -ForegroundColor Green
Write-Host "      Dashboard: http://localhost:5998" -ForegroundColor DarkGray
Write-Host "      API: http://localhost:5999" -ForegroundColor DarkGray
