---
description: Start the Expo/React Native dev server for SK8Lytz
---

// turbo-all

1. Navigate to the project root
```powershell
cd c:\Neogleamz\AG_SK8Lytz_App\SK8Lytz
```

2. Run the Port Sweeper to kill ghost Node processes and clear the Metro cache
```powershell
powershell.exe -ExecutionPolicy Bypass -File .\tools\port-sweeper.ps1
```

3. Start the Expo dev server (clears cache)
```powershell
npx expo start --clear
```
