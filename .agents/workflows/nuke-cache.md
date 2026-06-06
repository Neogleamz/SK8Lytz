---
description: Kill Metro bundler, wipe all caches (Metro, Watchman, Gradle), and restart Expo clean
persona_entry: "🩺 SRE — River"
team_roster: .agents/team-roster.md
---

> **🩺 SRE — River | Cache Nuke Active**
> *When the environment is lying to you, River burns it down and starts fresh. This is not a soft restart. Every cache goes. Every ghost process dies.*

// turbo-all

1. Kill any running Node/Metro processes

```powershell
Get-Process -Name node -ErrorAction SilentlyContinue | Stop-Process -Force
Write-Host "Metro/Node processes terminated."
```

2. Clear Metro bundler temp cache & Node Modules

```powershell
Remove-Item -Recurse -Force "$env:TEMP\metro-*" -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force "$env:TEMP\react-*" -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force "$env:TEMP\haste-*" -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force "node_modules" -ErrorAction SilentlyContinue
Write-Host "Metro cache and node_modules cleared."
```

3. Clear the local .expo cache

```powershell
Remove-Item -Recurse -Force "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\.expo" -ErrorAction SilentlyContinue
Write-Host ".expo directory cleared."
```

4. Clean Android Gradle build artifacts

```powershell
Remove-Item -Recurse -Force "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\android\app\build" -ErrorAction SilentlyContinue
Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\android"
.\gradlew.bat clean
Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
Write-Host "Gradle build artifacts cleaned."
```

5. Restart Expo with a clean slate

```powershell
Set-Location "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz"
npx expo start --clear
```
