---
description: Starts the Cultural Enrichment Daemon in a standalone background process
persona_entry: "🌙 Ops — Alex"
team_roster: .agents/rules/team-roster.md
---

> **🌙 Ops — Alex | ETL Daemon Launch Active**
> *Alex fires up the enrichment pipeline. PM2 handles persistence. Confirm it's running silently before closing the terminal.*

// turbo-all

> **⚠️ RETIREMENT NOTICE:** The `CulturalDaemon.ts` script has been retired. The other daemons in the scraper stack (website-resolver, indexer, photographer, publisher) are now run as child processes under `CCTower.ts` inside the Docker Scraper Stack container (`sk8lytz-scraper-stack`).

1. Verify that the Docker Scraper Stack is online
```powershell
docker compose ps
```

2. Confirm that the CCTower scraper daemons are online by querying the status endpoint
```powershell
Invoke-RestMethod -Uri "http://localhost:5999/status" -Method GET | ConvertTo-Json -Depth 2
```
