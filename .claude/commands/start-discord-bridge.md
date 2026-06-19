# /start-discord-bridge — Start Discord Agent Bridge

**Description:** Starts the Discord Agent Bridge daemon — verifies it is running inside the Docker stack.
**Persona:** 🌙 Ops — Alex

> **🌙 Ops — Alex | Discord Bridge Launch Active**
> *Alex keeps the pipes running. The Discord bridge is the team's voice to the outside world. Launch it clean, confirm it's alive.*

> **DEPRECATION NOTICE:** The Discord Bridge is now automatically launched inside the Docker Scraper Stack container (`sk8lytz-scraper-stack` service running `CCTower.ts`) when the stack starts. Running it as a host process is deprecated.

1. Verify that the Docker Scraper Stack is online
```powershell
docker compose ps
```

2. Confirm that the Discord Bridge is active by checking the logs
```powershell
docker compose logs cctower | Select-String "Booting Discord Bridge"
```
