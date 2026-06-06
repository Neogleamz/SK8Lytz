---
description: Starts the Discord Agent Bridge daemon in a standalone background process
persona_entry: "🌙 Ops — Alex"
team_roster: .agents/team-roster.md
---

> **🌙 Ops — Alex | Discord Bridge Launch Active**
> *Alex keeps the pipes running. The Discord bridge is the team's voice to the outside world. Launch it clean, confirm it's alive.*

// turbo-all

1. Launch the Discord Bridge microservice using PM2 in the background
Start-Process node -ArgumentList "index.js" -WorkingDirectory "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\tools\discord-bridge" -WindowStyle Hidden

2. Confirm to the user that the bridge has been successfully launched.
