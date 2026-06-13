# Implementation Plan: refactor/deep-dive-regressions

This plan addresses the 50+ critical Rule 16 violations (`any` casts, missing `try/catch` blocks, inline styles/functions) and offline telemetry dropping regressions identified by the 16-node Map-Reduce Code Audit. 

## Proposed Changes

We will execute the fixes in domain-based chunks, focusing on the highest risk regressions first.

---

### Phase 1: Core System & Telemetry Resilience (DATA_LAYER & SESSION_TRACKING)

#### [MODIFY] [AppLogger.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AppLogger.ts)
- **Fix:** Replace `any` casts with strict types for log payloads.
- **Fix:** Wrap Supabase pushes in `try/catch`. 
- **Fix:** If Supabase fails, append the failed logs back to the front of `@sk8lytz_logs` rather than unconditionally clearing the buffer.

#### [MODIFY] [DeviceRepository.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Fix:** Add `.catch()` to `AsyncStorage` methods.
- **Fix:** Remove `any` casts in filter/catch blocks.

#### [MODIFY] [useSessionTracking.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useSessionTracking.ts)
- **Fix:** Ensure offline telemetry data is written to the `PENDING_SESSION_QUEUE_KEY` local queue if the Supabase network call fails, applying the re-entrancy guard we defined previously.

#### [MODIFY] [LocationService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/LocationService.ts)
- **Fix:** Remove `any` casts in map/filter operations.

---

### Phase 2: BLE & Hardware Guardrails (BLE_CORE, UI_CONTROLS, PROTOCOLS)

#### [MODIFY] [useBLEAutoRecovery.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEAutoRecovery.ts)
- **Fix:** Wrap the disconnection handling logic in `useCallback` to prevent stale closures.

#### [MODIFY] [useBLEScanner.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts)
- **Fix:** Ensure discovered devices are pushed to an offline telemetry queue if the network is unavailable.

#### [MODIFY] [useBLESweeper.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLESweeper.ts)
- **Fix:** Remove `any` casts in the Interrogator hardware queries.

#### [MODIFY] [ControllerRegistry.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ControllerRegistry.ts) & [ZenggeProtocol.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/protocols/ZenggeProtocol.ts)
- **Fix:** Remove `any` casts for `_appLogger`.

#### [MODIFY] [DockedController.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/DockedController.tsx)
- **Fix:** Strip out inline styles and define them in `StyleSheet.create`.
- **Fix:** Fix the dropped cleanup function in the `forEach` loop.
- **Fix:** Type the HW settings and scene payloads instead of casting to `any`.

#### [MODIFY] [DashboardScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/DashboardScreen.tsx)
- **Fix:** Strip inline styles.
- **Fix:** Fix auto-close premature firing on RECOVERING state (L656).
- **Fix:** Add try/catch around `saveRegisteredDevice` and `deregisterDevice`.

---

### Phase 3: Identity & Group Sync (IDENTITY & GROUP_SYNC)

#### [MODIFY] [AuthContext.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/context/AuthContext.tsx)
- **Fix:** Wrap Linking/AsyncStorage I/O in `try/catch`.

#### [MODIFY] [AuthProfileService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/AuthProfileService.ts) & [CrewProfileService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewProfileService.ts)
- **Fix:** Replace `any` parameter casts with explicit types.

#### [MODIFY] [CrewService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts) & [GroupRepository.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GroupRepository.ts)
- **Fix:** Implement offline telemetry queueing for session events.
- **Fix:** Wrap `AsyncStorage.multiRemove` and `setItem` in try/catch wrappers.

#### [MODIFY] [CrewDetailScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewDetailScreen.tsx) & [CrewLandingScreen.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/crew/CrewLandingScreen.tsx)
- **Fix:** Remove explicit `any` casts in catch blocks by using `if (error instanceof Error)`.

---

### Phase 4: Native & OS Permissions

#### [MODIFY] [AndroidManifest.xml](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/app/src/main/AndroidManifest.xml)
- **Fix:** Add missing Android 14+ FOREGROUND_SERVICE_*.
- **Fix:** Resolve conflicting location permission declarations and redundant intent-filters.

#### [MODIFY] [PermissionService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/PermissionService.ts)
- **Fix:** Add `try/catch` around `AsyncStorage.setItem()` to prevent offline telemetry drops.

#### [MODIFY] [WatchConnectivityManager.swift](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/targets/watch/WatchConnectivityManager.swift)
- **Fix:** Use `transferUserInfo` or queue updates to ensure `updateApplicationContext` doesn't overwrite pending payloads.

#### [MODIFY] [WearMessageSender.kt](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/android/sk8lytzWear/src/main/java/com/sk8lytz/wear/WearMessageSender.kt)
- **Fix:** Implement local persistence fallback for health telemetry if the phone is disconnected.

---

### Phase 5: Cloud Functions

#### [MODIFY] [notify-crew-session/index.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/functions/notify-crew-session/index.ts)
- **Fix:** Add `try/catch` around network fetch.

#### [MODIFY] [20260414_consolidate_telemetry.sql](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/migrations/20260414_consolidate_telemetry.sql)
- **Fix:** Add `ON DELETE CASCADE` for user_id foreign keys.

#### [MODIFY] [20260506000001_god_tier_telemetry.sql](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/supabase/migrations/20260506000001_god_tier_telemetry.sql)
- **Fix:** Replace direct `::INT` cast on JSON text with a safer cast or rounding strategy.

---

## Verification Plan

### Automated Tests
- Run `npm run verify` to execute TSC, Jest, and TypeSafety gates after every phase.

### Manual Verification
- Re-run the AST compiler checks to ensure Rule 16 zero-violations.
- Turn off network on device and trigger a telemetry push to confirm the local queue captures the payload without crashing.
