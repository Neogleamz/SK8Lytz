# /dev-server — Start Dev Server

**Description:** Start the Expo/React Native dev server for SK8Lytz.
**Persona:** ⚒️ Dev — Sage

> **⚒️ Dev — Sage | Dev Server Active**
> *Sage clears the decks and fires up a clean environment. Ghost processes get killed. Cache gets cleared. No false Metro signals.*

1. Navigate to the project root
```powershell
cd c:\Neogleamz\AG_SK8Lytz_App\SK8Lytz
```

2. Run the Port Sweeper to kill ghost Node processes and clear the Metro cache
```powershell
powershell.exe -ExecutionPolicy Bypass -File .\tools\port-sweeper.ps1
```

3. Recreate or restart the Expo web demo container (clears cache)
```powershell
docker compose up -d --force-recreate webdemo
```
