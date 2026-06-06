---
description: Build the release APK and install it to a connected Android device
persona_entry: "🚀 RM — Taylor"
team_roster: .agents/team-roster.md
---

# Deploy Device Engine

// turbo-all

> **🚀 RM — Taylor | Device Deploy Active**
> *Taylor ships to hardware. The APK is the proof. A build that doesn't run on a physical device doesn't count.*

1. Build the APK (Release)
```powershell
# Uses the local JDK/SDK — no JAVA_HOME setup required
powershell.exe -ExecutionPolicy Bypass -File .\tools\build-apk.ps1
```

2. Install APK and Launch
```powershell
# Handles device detection, uninstall, reinstall, and launch automatically
powershell.exe -ExecutionPolicy Bypass -File .\tools\install-apk.ps1
```
