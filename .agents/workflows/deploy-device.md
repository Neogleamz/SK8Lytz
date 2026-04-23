---
description: Build the release APK and install it to a connected Android device
---

# Deploy Device Engine

// turbo-all

1. Build the APK (Release)
```powershell
cd android
./gradlew assembleRelease
cd ..
```

2. Check Connected Devices
```powershell
$ADB = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\.local-builder\android-sdk\platform-tools\adb.exe"
& $ADB devices
```

3. Install APK
```powershell
$APK = "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\android\app\build\outputs\apk\release\SK8Lytz.apk"
& $ADB install -r $APK
```

4. Launch App
```powershell
& $ADB shell am start -n com.neogleamz.sk8lytz/.MainActivity
```
