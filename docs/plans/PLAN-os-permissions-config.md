# Implementation Plan

## Task: fix/os-permissions-config
## Cluster: TC-16
## Risk: [M-RISK] | Size: [Snack] | Layer: [BUILD]
## Status: [⚪ READY]

---

## Problem Statement

Three OS permission config bugs — two crash risks and one App Store rejection risk:

1. **iOS crash (HIGH):** `app.config.js` is missing `NSHealthUpdateUsageDescription` in `infoPlist`. `PermissionService.ts` requests HealthKit write permission for Workouts. Without this key, iOS will crash the app immediately when the permission is requested.

2. **App Store rejection (MEDIUM):** `NSLocationWhenInUseUsageDescription` currently says _"SK8Lytz requires location services for Bluetooth Low Energy scanning to find your skates"_ — this is factually wrong. iOS uses `CoreBluetooth` (not CoreLocation) for BLE. This inaccurate description will trigger an App Store review rejection.

3. **AndroidManifest duplication (MEDIUM):** `ACTION_SHOW_PERMISSIONS_RATIONALE` intent-filter appears 5 times in `MainActivity` — should appear exactly once.

---

## Source of Truth

- **`artifacts/deepdive_raw/DOMAIN_OS_PERMISSIONS_findings.json`** → OS_PERM-001 (`AndroidManifest.xml:49`), OS_PERM-002 (`app.config.js:17`), OS_PERM-003 (`app.config.js:21`)
- **`app.config.js`** — iOS infoPlist section
- **`android/app/src/main/AndroidManifest.xml:49`** — 5 duplicate intent-filter blocks
- **`src/services/PermissionService.ts`** — confirms HealthKit write permission is requested

---

## Proposed Changes

### [MODIFY] `app.config.js`

**Add missing NSHealthUpdateUsageDescription (line ~17):**
```javascript
ios: {
  infoPlist: {
    NSHealthUpdateUsageDescription: 'SK8Lytz writes your skating sessions to Apple Health to track fitness activity.',
    // ... existing keys
  }
}
```

**Fix NSLocationWhenInUseUsageDescription (line ~21):**
```diff
- NSLocationWhenInUseUsageDescription: 'SK8Lytz requires location services for Bluetooth Low Energy scanning to find your skates.',
+ NSLocationWhenInUseUsageDescription: 'SK8Lytz uses your location to discover nearby skate spots and map your skating routes.',
```

### [MODIFY] `android/app/src/main/AndroidManifest.xml`
Remove 4 of the 5 duplicate `ACTION_SHOW_PERMISSIONS_RATIONALE` intent-filter blocks starting at line 49. Keep exactly one.

---

## Verification Plan

### Automated
- `npm run verify` — TSC clean (config file)
- Manual review of `app.config.js` diff to confirm both keys are present/correct

### Manual
1. **iOS**: Build to iOS simulator → trigger Health permission request → confirm no crash
2. **iOS**: Review permission strings in iOS Settings → confirm both Location and Health descriptions are accurate
3. **Android**: Build to Android → confirm app installs without duplicate intent registration warnings

---

## Worktree
`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\os-permissions-config\`
Branch: `fix/os-permissions-config`
