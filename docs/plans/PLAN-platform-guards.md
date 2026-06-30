# Implementation Plan

**Slug:** `platform-guards`
**Author:** 📐 Quinn (TPM)
**Date:** 2026-06-30
**Source Audit:** `artifacts/system_audit_report.md` — Wave 7 PLATFORM cluster (2H, 24M, 3L)
**Risk Profile:** LOW (3 surgical edits across 2 files; no logic changes, no architecture changes)

---

## Goal

Fix Android permission declarations for BLE scanning and Health Connect, and clean up inline `Platform.OS` requires in `LocationService.ts`:

1. Add `android:usesPermissionFlags="neverForLocation"` to the `BLUETOOTH_SCAN` permission declaration so Android 12+ (API 31+) does not treat BLE scanning as location-based.
2. Add a non-required `<uses-feature android:name="android.hardware.sensor.heartrate" android:required="false">` declaration so the Play Store does not gate the app to devices with health hardware.
3. Replace the two inline `require('react-native').Platform.OS` calls in `LocationService.ts` (lines 30, 88) with a top-level `import { Platform } from 'react-native'` and direct `Platform.OS` usage (fixes R-20 violation).

---

## Source of Truth (verified this session)

| Finding | File:Line | Verified Current State |
|---|---|---|
| BLE scan flag missing | `android/app/src/main/AndroidManifest.xml:8` | `<uses-permission android:name="android.permission.BLUETOOTH_SCAN"/>` — no `usesPermissionFlags` attribute |
| Health feature decl missing | `android/app/src/main/AndroidManifest.xml:19-21` | Three `health.READ_*` perms declared; no `<uses-feature>` for heart-rate sensor |
| Inline require #1 | `src/services/LocationService.ts:30` | `if (require('react-native').Platform.OS === 'web') {` |
| Inline require #2 | `src/services/LocationService.ts:88` | `if (require('react-native').Platform.OS === 'web') return null;` |
| Existing top-level imports | `src/services/LocationService.ts:12-16` | No `react-native` import present; `Platform` not currently imported |

**KB:** `KB: not required` — All changes target first-party Android manifest syntax and React Native's `Platform` API, both already in active use across the codebase. The `neverForLocation` flag and `<uses-feature>` element are standard AOSP manifest attributes. No external-library behavioral assertion is being made; no new dependency is introduced. (If Sage wishes to double-confirm the `usesPermissionFlags` attribute name, the AOSP manifest reference is the canonical source — but this is a documented, stable attribute since API 31.)

---

## Pre-Flight (Sage runs before any edit)

- **P0-1:** Confirm branch is NOT master:
  ```
  git branch --show-current
  ```
  - **Verify:** Output is the worktree branch (e.g. `feat/platform-guards`), NOT `master`. If `master` → HALT (S1).
- **P0-2:** Confirm both target files are unmodified at the cited lines:
  ```
  git diff HEAD android/app/src/main/AndroidManifest.xml
  git diff HEAD src/services/LocationService.ts
  ```
  - **Verify:** Both produce empty output (clean working tree for these files). If non-empty → STOP and report; the audit line numbers may be stale.

---

## Step 1 — Add `neverForLocation` flag to BLUETOOTH_SCAN permission

**File:** `android/app/src/main/AndroidManifest.xml`
**Target:** Line 8

**Current (read this exact line first — Look Before Leap):**
```xml
  <uses-permission android:name="android.permission.BLUETOOTH_SCAN"/>
```

**Replace with:**
```xml
  <uses-permission android:name="android.permission.BLUETOOTH_SCAN" android:usesPermissionFlags="neverForLocation"/>
```

**Rationale (SoT cited):** Audit `system_audit_report.md` Wave 7 finding #1 [HIGH/PROBABLE]. SK8Lytz scans for BLE controllers (Zengge/BanlanX) by service UUID, never to derive physical location. Declaring `neverForLocation` tells Android 12+ the scan results will never be used for location, so the OS does not require `ACCESS_FINE_LOCATION` at scan time.

**Note (do NOT act on — context only):** `ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION` (lines 2-3) remain declared because `LocationService.ts` legitimately uses them for crew-session venue tagging. They are NOT removed by this step.

- **Verify 1a:** Run `git diff HEAD android/app/src/main/AndroidManifest.xml` — expect EXACTLY one changed line (line 8), adding the `android:usesPermissionFlags="neverForLocation"` attribute and nothing else. If any other line changed → `git checkout -- android/app/src/main/AndroidManifest.xml` and retry surgically.
- **Verify 1b:** Confirm the line is well-formed XML (single self-closing `/>`, both attributes inside one tag). Re-`Read` line 8 to confirm.

---

## Step 2 — Add non-required heart-rate `<uses-feature>` declaration

**File:** `android/app/src/main/AndroidManifest.xml`
**Target:** Insert immediately AFTER line 21 (the last `health.READ_*` permission) and BEFORE line 22 (the existing `bluetooth_le` uses-feature).

**Current anchor (read lines 19-22 first — Look Before Leap):**
```xml
  <uses-permission android:name="android.permission.health.READ_HEART_RATE"/>
  <uses-permission android:name="android.permission.health.READ_STEPS"/>
  <uses-permission android:name="android.permission.health.READ_ACTIVE_CALORIES_BURNED"/>
  <uses-feature android:name="android.hardware.bluetooth_le" android:required="true"/>
```

**Replace that 4-line block with:**
```xml
  <uses-permission android:name="android.permission.health.READ_HEART_RATE"/>
  <uses-permission android:name="android.permission.health.READ_STEPS"/>
  <uses-permission android:name="android.permission.health.READ_ACTIVE_CALORIES_BURNED"/>
  <uses-feature android:name="android.hardware.sensor.heartrate" android:required="false"/>
  <uses-feature android:name="android.hardware.bluetooth_le" android:required="true"/>
```

**Rationale (SoT cited):** Audit Wave 7 finding #2 [HIGH/PROBABLE]. The Health Connect read permissions (lines 19-21) can cause the Play Store to infer the app requires health/sensor hardware, silently filtering the app off devices that lack a heart-rate sensor. Declaring the feature with `android:required="false"` explicitly states the hardware is optional, preserving install eligibility on all devices.

- **Verify 2a:** Run `git diff HEAD android/app/src/main/AndroidManifest.xml`. Cumulative diff (Steps 1+2) must show: (1) the modified line 8, and (2) exactly one NEW line inserted — the `android.hardware.sensor.heartrate` `<uses-feature>` with `android:required="false"`. The three `health.READ_*` lines and the `bluetooth_le` line must be UNCHANGED. If anything else changed → `git checkout --` and retry.
- **Verify 2b:** Re-`Read` lines 19-23 and confirm the new `<uses-feature>` sits between the calories permission and the `bluetooth_le` feature, with correct `android:required="false"`.

---

## Step 3 — Add top-level `Platform` import to LocationService

**File:** `src/services/LocationService.ts`
**Target:** Line 12 (import block).

**Current (read lines 12-16 first — Look Before Leap):**
```ts
import * as Location from 'expo-location';
import { AppLogger } from './appLogger';
import { supabase } from './supabaseClient';
import { openGlobalPermissionsModal, checkPermission } from './PermissionService';
import { SkateSpotsService } from './SkateSpotsService';
```

**Replace with (add the `react-native` import as the new first import line):**
```ts
import { Platform } from 'react-native';
import * as Location from 'expo-location';
import { AppLogger } from './appLogger';
import { supabase } from './supabaseClient';
import { openGlobalPermissionsModal, checkPermission } from './PermissionService';
import { SkateSpotsService } from './SkateSpotsService';
```

**Rationale (SoT cited):** Audit Wave 7 findings #4 and #5 [MEDIUM/PROBABLE], R-20 (idiomatic import) violation. Establishes the symbol consumed by Steps 4 and 5.

- **Verify 3a:** Run `git diff HEAD src/services/LocationService.ts` — expect exactly one added line (`import { Platform } from 'react-native';`) at the top of the import block; no other line changed. If anything else changed → `git checkout --` and retry.

---

## Step 4 — Replace inline require #1 (`getSessionLocation`)

**File:** `src/services/LocationService.ts`
**Target:** Line 30.

**Current (read this exact line first — Look Before Leap):**
```ts
    if (require('react-native').Platform.OS === 'web') {
```

**Replace with:**
```ts
    if (Platform.OS === 'web') {
```

- **Verify 4a:** Run `git diff HEAD src/services/LocationService.ts`. The cumulative diff (Steps 3+4) must show the new import line and exactly this one-line change at line 30. No surrounding logic touched.

---

## Step 5 — Replace inline require #2 (`getSilentLocation`)

**File:** `src/services/LocationService.ts`
**Target:** Line 88.

**Current (read this exact line first — Look Before Leap):**
```ts
      if (require('react-native').Platform.OS === 'web') return null;
```

**Replace with:**
```ts
      if (Platform.OS === 'web') return null;
```

- **Verify 5a:** Run `git diff HEAD src/services/LocationService.ts`. Cumulative diff (Steps 3+4+5) must show: 1 added import line + 2 modified one-liners (lines 30 and 88). Nothing else.
- **Verify 5b:** Confirm zero remaining inline requires:
  ```
  ```
  Grep `src/services/LocationService.ts` for the pattern `require('react-native')` — expect ZERO matches. (Sage: use the Grep tool, pattern `require\('react-native'\)`, on that file.)

---

## Final Verification (Sage runs after all 5 steps)

- **FV-1 — Type + test suite (S7 mandate; NEVER raw tsc/jest):**
  ```
  npm run verify
  ```
  - **Verify:** Exit 0. TSC = 0 errors, Jest green, AST + TypeSafety + WorkflowValidator pass. `Platform` is now an imported symbol so no "unused import" warning should appear (it is used twice).
- **FV-2 — No-`any` / no-`@ts-ignore` (S3):** The `check-any-cast` hook runs automatically post-edit. Confirm it did not flag `LocationService.ts`. This plan introduces no casts.
- **FV-3 — Cumulative diff review:**
  ```
  git diff HEAD --stat
  ```
  - **Verify:** Exactly TWO files changed: `android/app/src/main/AndroidManifest.xml` and `src/services/LocationService.ts`. If any third file appears → STOP and report.
- **FV-4 — Android manifest sanity (optional but recommended):** Visually re-`Read` `AndroidManifest.xml` lines 1-23 and confirm the file still opens with `<manifest …>` and the `<queries>`/`<application>` blocks below line 23 are byte-for-byte unchanged.

---

## Documentation Gate (Avery — before gatekeeper)

This change modifies Android permission/feature declarations (a platform-config change), not a hook/service/BLE-protocol export. Per VS-003:

- **DOCS-1:** Check `docs/SK8Lytz_App_Master_Reference.md` for an Android permissions / manifest section. If one exists, update it to reflect the `neverForLocation` flag on `BLUETOOTH_SCAN` and the new `android.hardware.sensor.heartrate` optional feature.
- **DOCS-2:** If NO manifest/permissions section exists in the Master Reference, this is a config-only change with no hook/service/component/BLE-architecture surface — the docs gate is satisfied with no edit. State this explicitly in the handoff rather than skipping silently.
- **Verify:** Either the Master Reference permissions section reflects the two new manifest attributes, OR the handoff explicitly records "no Master Reference surface — config-only change."

---

## Out of Scope (HARD BOUNDARY — Sage must NOT touch)

The following audit findings and files are explicitly OUT OF SCOPE for this plan. Do not open, edit, or "improve" them:

1. **Wear OS TileService threading** — `android/...wear/tiles/MainTileService.kt:57` (Wave 7 finding #3). This is native Kotlin in a separate Wear build target. The `Thread{}.start()` → ListenableFuture/coroutine migration requires Wear-specific testing and is tracked as its own task. Leave a `// TODO` only if you happen to open the file for an unrelated reason — otherwise do not open it.
2. **`ACTIVITY_RECOGNITION` behavior changes** — `AndroidManifest.xml:4` (Wave 7 finding #6). The permission stays declared exactly as-is. No platform-guard commentary, no removal, no SDK-version gating. Documenting the rationale is a separate docs task, not this plan.
3. **iOS changes** — No `Info.plist`, no iOS-specific code, no `ios/` directory edits.
4. **Removing `ACCESS_FINE_LOCATION` / `ACCESS_COARSE_LOCATION`** — These remain; `LocationService` uses them for venue tagging. Removing them would break crew-session location features.
5. **Any other `require(...)` calls elsewhere in the codebase** — Only the two cited lines (30, 88) in `LocationService.ts` are in scope. Do not sweep the repo for other inline requires.
6. **Health Connect business logic** — No changes to how health data is read or used; only the `<uses-feature>` install-gate declaration is added.

---

## Summary of Files to Modify

| File | Lines | Change |
|---|---|---|
| `android/app/src/main/AndroidManifest.xml` | 8 | Add `android:usesPermissionFlags="neverForLocation"` to BLUETOOTH_SCAN |
| `android/app/src/main/AndroidManifest.xml` | after 21 | Insert `<uses-feature android:name="android.hardware.sensor.heartrate" android:required="false"/>` |
| `src/services/LocationService.ts` | 12 | Add `import { Platform } from 'react-native';` |
| `src/services/LocationService.ts` | 30 | `require('react-native').Platform.OS` → `Platform.OS` |
| `src/services/LocationService.ts` | 88 | `require('react-native').Platform.OS` → `Platform.OS` |

**Total expected diff:** 2 files, ~5 changed/added lines. No `[HIGH RISK]` file-size flag — both files are well under 30KB (AndroidManifest.xml is 54 lines; LocationService.ts is 418 lines / ~16KB).
