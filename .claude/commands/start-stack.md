# /start-stack — Start the SK8Lytz Scraper Stack

**Description:** Start the entire SK8Lytz Scraper Stack — CCTower API, Vite Dashboard, and Discord Bridge via Docker Compose.
**Persona:** 🌙 Ops — Alex

> **🌙 Ops — Alex | Stack Startup Active**
> *Alex orchestrates the full Docker stack. CCTower, Dashboard, Discord Bridge — all up or none up. Verify health before declaring success.*

1. Rebuild and start the Docker container

```powershell
docker compose up -d --build
```

2. Verify the stack is online

```powershell
Start-Sleep -Seconds 5; docker compose ls; Invoke-RestMethod -Uri "http://localhost:5999/status" -Method GET | ConvertTo-Json -Depth 2
```
