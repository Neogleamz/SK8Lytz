---
description: Starts the Cultural Enrichment Daemon in a standalone background process
---

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
