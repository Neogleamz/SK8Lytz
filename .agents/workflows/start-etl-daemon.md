---
description: Starts the Cultural Enrichment Daemon in a standalone background process
persona_entry: "🌙 Ops — Alex"
team_roster: .agents/team-roster.md
---

> **🌙 Ops — Alex | ETL Daemon Launch Active**
> *Alex fires up the enrichment pipeline. PM2 handles persistence. Confirm it's running silently before closing the terminal.*

// turbo-all

1. Launch the Cultural Enrichment Daemon using PM2 in the background
```powershell
Set-Location -Path "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\tools\scraper"
npx -y pm2 start node_modules/tsx/dist/cli.mjs --name "CulturalDaemon" -- CulturalDaemon.ts
Set-Location -Path "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
```

2. Save the PM2 process list to persist across reboots (optional but recommended)
```powershell
npx -y pm2 save
```

3. Confirm to the user that the daemon has been successfully launched and is running silently to enrich skate spots.
