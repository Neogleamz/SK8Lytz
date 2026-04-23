---
description: Starts the Discord Agent Bridge daemon in a standalone background process
---

// turbo-all

1. Launch the Discord Bridge microservice using PM2 in the background
Start-Process node -ArgumentList "index.js" -WorkingDirectory "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\tools\discord-bridge" -WindowStyle Hidden

2. Confirm to the user that the bridge has been successfully launched.
