---
description: Kill Metro bundler, wipe all caches (Metro, Watchman, Gradle), and restart Expo clean
---

// turbo-all

1. Kill any running Node/Metro processes

```powershell
Get-Process -Name node -ErrorAction SilentlyContinue | Stop-Process -Force
Write-Host "Metro/Node processes terminated."
```

2. Clear Metro bundler temp cache

```powershell
Remove-Item -Recurse -Force "$env:TEMP\metro-*" -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force "$env:TEMP\react-*" -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force "$env:TEMP\haste-*" -ErrorAction SilentlyContinue
Write-Host "Metro cache cleared."
```

3. Clear the local .expo cache

```powershell
Remove-Item -Recurse -Force "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\.expo" -ErrorAction SilentlyContinue
Write-Host ".expo directory cleared."
```

4. Clean Android Gradle build artifacts

```powershell
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
