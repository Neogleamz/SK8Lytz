---
description: Build the release APK and install it to a connected Android device
persona_entry: "🚀 RM — Taylor"
team_roster: .agents/team-roster.md
---

# Deploy Device Engine

// turbo-all

> **🚀 RM — Taylor | Device Deploy Active**
> *Taylor ships to hardware. The APK is the proof. A build that doesn't run on a physical device doesn't count.*

---

### Phase 0 — Pre-Flight Check

Before touching anything, Taylor confirms the environment is ready:

1. Check a device is connected via ADB:
```powershell
& "C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\.local-builder\android-sdk\platform-tools\adb.exe" devices
```

- If **no device is listed** → halt immediately and report: `🔴 HALTED — No device connected. Plug in your phone and re-run /deploy-device.`
- If **a device is listed** → report: `✅ Device detected: [device ID]. Proceeding to build.`

---

### Phase 1 — Build APK

Report before starting:
> 📦 **[1/3] BUILDING** — Compiling release APK. This takes 3–5 minutes on a warm cache...

Run the build script:
```powershell
powershell.exe -ExecutionPolicy Bypass -File .\tools\build-apk.ps1
```

- If the script **exits with a non-zero code** → halt and report:
  > 🔴 **[1/3] BUILD FAILED** — Gradle exited with an error. Check the output above for the failing task. Run `/debug` if the error is unclear.
  > **Do NOT proceed to install.**

- If the script **exits successfully** → report:
  > ✅ **[1/3] BUILD COMPLETE** — APK compiled successfully. Handing off to installer.

---

### Phase 2 — Install & Launch

Report before starting:
> 📲 **[2/3] INSTALLING** — Uninstalling old build and pushing new APK to device...

Run the install script:
```powershell
powershell.exe -ExecutionPolicy Bypass -File .\tools\install-apk.ps1
```

- If the script **exits with a non-zero code** → halt and report:
  > 🔴 **[2/3] INSTALL FAILED** — ADB returned an error. Common causes: device unauthorized (check screen), APK signature mismatch, or device storage full.

- If the script **exits successfully** → report:
  > ✅ **[2/3] INSTALLED & LAUNCHED** — SK8Lytz is running on device.

---

### Phase 3 — Status Trail Summary

Always output this block at the end regardless of outcome:

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
🚀 DEPLOY COMPLETE — SK8Lytz
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
[1/3] Build APK     → ✅ / 🔴
[2/3] Install APK   → ✅ / 🔴
[3/3] Launch App    → ✅ / 🔴
Device: [device ID]
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```
