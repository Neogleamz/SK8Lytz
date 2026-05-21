---
description: Build the release APK and install it to a connected Android device
---

# Deploy Device Engine

// turbo-all

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
