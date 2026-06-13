# Implementation Plan: OS Variance Parity (R-20)

## 📖 Source of Truth
Source: `docs/audits/system_audit_report_2026_06_07.md`:[Line 26]

## 🎯 Goal
Ensure 100% OS variance parity (iOS vs Android) for foreground services, MTU negotiation, and permissions.

## 🛠️ Proposed Changes

### [MODIFY] `app.config.js`
- Inject `NSCameraUsageDescription` and `NSHealthShareUsageDescription` into the `ios.infoPlist` block to prevent Apple Store automated rejection.
- Sync `android.permissions` to match the exact requirements of `AndroidManifest.xml`.

### [MODIFY] `index.ts`
- Wrap `notifee.registerForegroundService` in a strict `Platform.OS === 'android'` guard.

### [MODIFY] `src/hooks/ble/useBLEAutoRecovery.ts`
- Add platform branching for MTU negotiation. Skip `conn.requestMTU(512)` on iOS (Core Bluetooth auto-negotiates), directly relying on the system default MTU property.

### [MODIFY] `src/context/SessionContext.tsx`
- In `endSession()`, add `else { notifee.cancelNotification(NOTIFICATION_ID); }` for iOS to clear the foreground HUD equivalent.

## ✅ Verify
- Run `npx expo prebuild --clean` and manually verify `ios/SK8Lytz/Info.plist` contains the injected descriptions.
- `npm run verify` type check passes.

## 🛑 Out of Scope
- Testing on physical iOS hardware (simulated verification via Prebuild output is sufficient for this plan).
