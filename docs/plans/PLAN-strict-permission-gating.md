# Implementation Plan: `feat/strict-permission-gating`

> **North Star**: Every feature that requires an OS permission MUST check for that permission at the UI gate BEFORE mounting sensor/hardware resources. If denied, show an inline contextual state — NEVER crash, NEVER silently fail, NEVER navigate the user away.

---

## 1. Problem Statement

Currently the app has **inconsistent permission enforcement**. Some features (`useAppMicrophone`, `LocationService`) properly gate with `checkPermission()` → `openGlobalPermissionsModal()`, but others (`CAMERA` mode, `STREET` mode) have **no runtime permission check** at the UI layer — they mount sensors/camera and hope for the best. A denied permission silently produces a dead panel or crash.

### Critical Bugs Discovered During Research

> [!CAUTION]
> **BUG-1: Missing Android Manifest Permission — `ACTIVITY_RECOGNITION`**
> `PermissionService.ts` requests `ACTIVITY_RECOGNITION` for the `HEALTH` toggle (line 135), but **the Android Manifest does NOT declare `android.permission.ACTIVITY_RECOGNITION`**. On Android 10+ (API 29), this permission MUST be declared in the manifest or `PermissionsAndroid.request()` will silently auto-deny without showing ANY prompt. This explains the user's report: *"the permissions for physical activity is still not working correctly!!!!"*

> [!WARNING]
> **BUG-2: Camera Mode has ZERO runtime permission check.**
> `DockedDock.tsx` directly calls `onModeChange('CAMERA')` → `setActiveMode('CAMERA')` → mounts `<CameraPanel>` → mounts `<CameraTracker>` which calls `useCameraDevice()` + `useCameraPermission()` from `react-native-vision-camera`. If the camera permission is denied or the user previously soft-revoked it via the `GranularPermissionsList`, the camera silently shows a black screen or throws.

> [!WARNING]
> **BUG-3: Street Mode has ZERO location permission pre-check.**
> `handleDockModeChange('STREET')` immediately mounts `<StreetPanel>` and activates `useStreetMode` (accelerometer) + `useGlobalTelemetry` (GPS). If location is denied, the GPS watcher throws to the error handler but no user-facing state is shown. The accelerometer works without permission, but the GPS data (speed/distance) is silently missing, making the StreetPanel dashboard show zeros with no explanation.

---

## 2. Complete Permission Path Audit

### Permission Matrix (Current vs. Target)

| Feature | OS Permission | Current Gate | Target Gate | Risk if Skipped |
|:--------|:-------------|:------------|:------------|:---------------|
| **BLE Scan/Connect** | `BLUETOOTH_SCAN` + `BLUETOOTH_CONNECT` | ✅ `useBLE.requestPermissions()` + `openGlobalPermissionsModal()` | ✅ No change needed | App lockout |
| **Microphone (App Mic)** | `RECORD_AUDIO` | ✅ `useAppMicrophone.startRecording()` checks + shows modal | ✅ No change needed — already shows overlay in `SpectrumAnalyzer` | Silent dead recording |
| **Camera Mode** | `CAMERA` | ❌ **No gate — mounts immediately** | 🔧 Add `checkPermission('CAMERA')` in `handleDockModeChange` before `setActiveMode('CAMERA')` | Black screen / crash |
| **Location (Street GPS)** | `ACCESS_FINE_LOCATION` | ❌ **No pre-gate on mode entry** | 🔧 Add `checkPermission('LOCATION')` in `handleDockModeChange` before `setActiveMode('STREET')` | Zeros in dashboard, silent failure |
| **Location (Crew Hub)** | `ACCESS_FINE_LOCATION` | ✅ `locationService.getSessionLocation()` checks + modal | ✅ No change needed | No map pin |
| **Location (Telemetry)** | `ACCESS_FINE_LOCATION` | ✅ `useGlobalTelemetry` checks + opens modal | ⚠️ Race — telemetry auto-starts before STREET panel mounts, so the modal fires during mount. Keep existing + add dock pre-check | Modal timing jank |
| **Health / Biometrics** | `ACTIVITY_RECOGNITION` (Android) / HealthKit (iOS) | ⚠️ **BUG: Manifest missing `ACTIVITY_RECOGNITION`** | 🔧 Add manifest entry + verify toggle works | Toggle always shows "denied" |
| **Notifications** | `POST_NOTIFICATIONS` | ✅ `NotificationService._requestPermissions()` | ✅ No change needed | No push tokens |

---

## 3. Proposed Changes

### Component 1: Android Manifest Fix

#### [MODIFY] [AndroidManifest.xml](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/app/src/main/AndroidManifest.xml)

Add the missing `ACTIVITY_RECOGNITION` permission declaration:

```xml
<uses-permission android:name="android.permission.ACTIVITY_RECOGNITION"/>
```

**Why**: Without this, `PermissionsAndroid.request(ACTIVITY_RECOGNITION)` silently returns `denied` on Android 10+, and the Health toggle in `GranularPermissionsList` can NEVER show the native OS popup. The user reported this exact symptom.

---

### Component 2: DockedDock — Permission-Aware Mode Switching

#### [MODIFY] [DockedDock.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/DockedDock.tsx)

**Current (unsafe)**: `DockedDock` blindly calls `onModeChange(dockItem.id)` for every mode. No permission checks occur before mounting a sensor-dependent panel.

**Target**: Add two new optional callback props to the interface:

```typescript
interface DockedDockProps {
  // ... existing props ...
  /** Pre-flight permission check before switching to a permission-gated mode.
   *  Returns true if the mode switch is allowed. */
  onPermissionCheck?: (mode: DockMode) => Promise<boolean>;
}
```

In the `onPress` handler and swipe release handler, wrap permission-gated modes (`CAMERA`, `STREET`) with the new callback:

```typescript
const handleModePress = async (modeId: DockMode) => {
  if (modeId === 'HOME') {
    onDisconnect?.();
    return;
  }
  // Gate modes that require OS permissions
  if ((modeId === 'CAMERA' || modeId === 'STREET') && onPermissionCheck) {
    const allowed = await onPermissionCheck(modeId);
    if (!allowed) return; // Permission denied — don't switch
  }
  onModeChange(modeId);
};
```

**Swipe handler**: Same logic — check permission before switching to `CAMERA` or `STREET` via swipe.

> [!IMPORTANT]
> **The Mode Dock buttons STAY VISIBLE**. We do NOT hide CAMERA or STREET tabs. Instead, tapping them triggers the permission check flow. If denied, the user gets the `openGlobalPermissionsModal()` prompt. This follows the same pattern as BLE (always visible, just gates on tap).

---

### Component 3: DockedController — Permission Gate Implementation

#### [MODIFY] [DockedController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)

Add a new `handlePermissionCheck` callback that gets passed to `DockedDock`:

```typescript
const handlePermissionCheck = React.useCallback(async (mode: string): Promise<boolean> => {
  if (mode === 'CAMERA') {
    const granted = await checkPermission('CAMERA');
    if (!granted) {
      await openGlobalPermissionsModal();
      return await checkPermission('CAMERA');
    }
    return true;
  }
  if (mode === 'STREET') {
    const granted = await checkPermission('LOCATION');
    if (!granted) {
      await openGlobalPermissionsModal();
      return await checkPermission('LOCATION');
    }
    return true;
  }
  return true; // All other modes pass through
}, []);
```

Wire it to the `DockedDock`:

```tsx
<DockedDock
  activeModeRef={activeModeRef}
  activeMode={activeMode}
  onModeChange={handleDockModeChange}
  onPermissionCheck={handlePermissionCheck}
  onDisconnect={onDisconnect}
  Colors={Colors}
/>
```

**Also gate favorites restore**: When `loadFavorite()` restores a `CAMERA` or `STREET` favorite, it must also check permissions before setting `activeMode`. If denied, fall back to `MULTIMODE` with a log.

---

### Component 4: StreetPanel — Inline "Location Required" State

#### [MODIFY] [StreetPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/StreetPanel.tsx)

Add a `hasLocationPermission` prop. When `false`, render a centered overlay (identical pattern to `SpectrumAnalyzer`'s `permissionOverlay`):

```tsx
{!hasLocationPermission && (
  <View style={styles.permissionOverlay}>
    <MaterialCommunityIcons name="map-marker-off" size={24} color="#FF4444" />
    <Text style={styles.permissionText}>Location Access Required</Text>
    <Text style={styles.permissionSubText}>GPS tracking powers the speed dashboard</Text>
    <TouchableOpacity style={styles.permissionButton} onPress={onRequestLocationPermission}>
      <Text style={styles.permissionButtonText}>ENABLE GPS</Text>
    </TouchableOpacity>
  </View>
)}
```

**Important**: The accelerometer (jerk detection) works WITHOUT location permission. Street Mode still activates — only the GPS dashboard (speed, distance, motion state labels) shows the overlay. The brake light jerk detector continues to function.

---

### Component 5: CameraPanel — Inline "Camera Required" State  

#### [MODIFY] [CameraPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/docked/CameraPanel.tsx)

CameraPanel currently mounts `<CameraTracker>` unconditionally. Add a `hasCameraPermission` prop:

- If `true`: mount CameraTracker as normal
- If `false`: show overlay with `camera-off` icon + "ENABLE CAMERA" button that calls `requestPermission('CAMERA')` and retries

This prevents mounting `useCameraDevice()` when permission is denied (which can throw on some Android OEMs).

---

### Component 6: PermissionService — No code changes needed

The existing `PermissionService.ts` is architecturally sound:
- `checkPermission()` properly checks opt-out ledger → native status
- `requestPermission()` properly delegates to platform-specific APIs
- `openGlobalPermissionsModal()` properly opens the full permission sheet

**Verdict**: No modifications needed. The service layer is correct — the bug is in the **consumers** not calling it.

---

## 4. Files Modified Summary

| File | Change Type | Risk | Lines Affected |
|:-----|:-----------|:-----|:--------------|
| `AndroidManifest.xml` | ADD permission line | L-RISK | +1 line |
| `DockedDock.tsx` | ADD `onPermissionCheck` prop + async handler | M-RISK | ~15 lines |
| `DockedController.tsx` | ADD `handlePermissionCheck` + wire to dock + guard `loadFavorite` | M-RISK | ~25 lines |
| `StreetPanel.tsx` | ADD permission overlay UI | L-RISK | ~15 lines |
| `CameraPanel.tsx` | ADD permission check guard | L-RISK | ~15 lines |

**Total estimated change**: ~70 lines across 5 files. Zero new dependencies. Zero architectural changes.

---

## 5. What We Are NOT Changing (Risk Mitigation)

These work correctly and MUST NOT be touched:

- ✅ `useAppMicrophone.ts` — Already checks MIC permission with modal fallback  
- ✅ `SpectrumAnalyzer.tsx` — Already renders `permissionOverlay` when `hasMicPermission=false`  
- ✅ `LocationService.ts` — Already checks LOCATION with `openGlobalPermissionsModal()`  
- ✅ `useBLE.ts` — Already gates `requestPermissions()` properly  
- ✅ `ComplianceGate.tsx` — First-run onboarding flow is correct  
- ✅ `GranularPermissionsList.tsx` — Toggle UI is correct  
- ✅ `PermissionService.ts` — Core service layer is correct  
- ✅ `useGlobalTelemetry.ts` — Already checks LOCATION before GPS watcher  

---

## 6. Edge Cases & Regression Guards

| Scenario | Expected Behavior | How We Ensure It |
|:---------|:-----------------|:-----------------|
| User taps CAMERA dock icon with camera denied | `openGlobalPermissionsModal()` fires. If still denied, mode does NOT switch. Dock stays on previous mode. | `handlePermissionCheck` returns `false` |
| User swipes to STREET with location denied | Same flow — modal fires, if denied, swipe is cancelled | `onPermissionCheck` in swipe handler |
| User restores a CAMERA favorite while camera is denied | Favorite restore falls back to `MULTIMODE` with log | Guard in `loadFavorite()` |
| User toggles Health ON in settings | Native Android popup appears (fixed by manifest) | `ACTIVITY_RECOGNITION` in manifest |
| User in STREET mode, location denied mid-session | `useGlobalTelemetry` error handler fires, GPS data shows 0 | Existing error handling — no change needed |
| Music Mode with mic denied | Existing `SpectrumAnalyzer` overlay shows "Microphone Access Required" | ✅ Already works |
| Web platform (no native permissions) | All `checkPermission` calls return `false` for web-only APIs (Camera, Health). Street/Camera tabs still appear but show friendly fallback. | `Platform.OS === 'web'` guards in PermissionService |

---

## 7. Verification Plan

### Pre-Merge Checklist

1. **`npm run verify`** — TSC + Jest must pass clean
2. **Visual Smoke Test**: 
   - App renders without white screen
   - All 7 dock icons visible and tappable
   - CAMERA tap → permission modal if denied → camera loads if granted
   - STREET tap → permission modal if denied → GPS dashboard loads if granted
   - HEALTH toggle → native OS popup appears (Android)
   - MUSIC mode → mic overlay still works when denied
3. **Regression Guard**: Verify modes that DON'T need gating (MULTIMODE, BUILDER, FAVORITES) still switch instantly with zero delay

### ADB Verification (if device available)

```bash
adb logcat -s "ReactNativeJS:V" | grep -i "permission"
```

Confirm `ACTIVITY_RECOGNITION` popup fires on Health toggle.
