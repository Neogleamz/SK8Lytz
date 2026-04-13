# [CHORE] Release Build & Multi-Device Deployment

## Goal
Execute a pristine release build of the SK8Lytz application and deploy it to all active Android targets (Phone + Emulator).

## Proposed Changes

### 🔧 Build Phase (Local Machine)
1. **Clean**: Run `cd android; .\gradlew.bat clean`
2. **Build**: Run `.\gradlew.bat assembleRelease`
3. **Target**: `android/app/build/outputs/apk/release/SK8Lytz.apk`

### 📱 Deployment Phase (ADB)
- Detect all connected devices.
- Execute `& $ADB install -r $APK` for each device ID.
- Verify installation with `adb shell am start`.

## Rationale
Ensures the client has the absolute latest logic (Maintenance Sweep #2) on their testing hardware for real-world validation.

## Verification
- Successful build termination (Exit 0).
- ADB confirmation for both targets.
