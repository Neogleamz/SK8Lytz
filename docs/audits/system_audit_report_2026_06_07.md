# 🗂️ SK8Lytz Orthogonal QA Fleet: System Audit Report

**Date**: 2026-06-07
**Audit Scope**: Entire `src/`, `e2e/`, `__tests__/`, and `supabase/` directory.
**Methodology**: Dual Vector Fleet (16 Domain Agents + 20 Rule Snipers) evaluating against the 20 Constitutional Guardrails.

> [!WARNING]
> This document synthesizes deduplicated findings. All identified anti-patterns require formal `/intake` planning before being added to the active Triage Queue.

## 🚨 CRITICAL SEVERITY (Architecture & Hardware Safety)

### 1. BLE Queue Bypassing & Collisions (R-01, R-10, R-13)
- **Direct Queue Bypass:** `useBLEAutoRecovery.ts` calls `writeCharacteristicWithoutResponseForService` directly for the 0x63 ping, skipping `BleWriteQueue`.
- **GATT 133 Collisions:** `BleConnectionManager.ts` uses `Promise.all` to run handshake/MTU negotiations concurrently across multiple devices. MUST be sequential.
- **Concurrent Polling:** `useBLEHeartbeat.ts` and `useBLERSSIMonitor.ts` use `Promise.all` to ping/poll RSSI across the group, risking GATT saturation.
- **Missing Jitter (R-03):** Reconnect loops in `useDashboardAutoConnect.ts`, `BleSessionFactory.ts`, and `BleWriteQueue.ts` use static linear delays without randomization.
- **Note:** [R-02] Fire-and-Forget is 100% compliant.

### 2. Auth Context Bypassing (R-15)
- **Direct Queries:** `DeviceRepository`, `CrewProfileService`, `GroupRepository`, `NotificationService`, and the `notify-crew-session` Edge Function bypass the reactive React Auth Context. They invoke `await supabase.auth.getUser()` directly, creating duplicate network requests and fragmenting state.

### 3. Promise & IO Safety (R-11)
- **Unhandled Rejections:** `AsyncStorage.getItem().then(...)` chains in `useBLE.ts`, `useBLEScanner.ts` lack `.catch()` blocks.
- **Silent Failures:** `App.tsx` swallows promise rejections on app boot. `Clipboard.setStringAsync` lacks error boundaries. `AuthProfileService.ts` ignores Supabase insert errors.

### 4. OS Variance Parity (R-20)
- **iOS Crash Risks:** `app.config.js` is missing `NSCameraUsageDescription` and `NSHealthShareUsageDescription`. `useBLEAutoRecovery.ts` blindly assumes Android MTU negotiation APIs for iOS.
- **Prebuild Divergence:** `AndroidManifest.xml` contains permissions missing from `app.config.js`. `index.ts` blindly calls `notifee.registerForegroundService` without a Platform guard. `SessionContext.tsx` fails to clean up iOS notifications.

### 5. PII Scrubbing Leaks (R-09)
- **AppLogger Vulnerability:** `formatPayload` relies on an exact-match blocklist (`piiKeys`) which misses non-standard keys (`newLeaderName` in `useCrewSession.ts`, `data` in `UserManagementPanel.tsx`) and fails to sanitize string values containing PII like `error.message` in deep link failures.

## 🟡 HIGH SEVERITY (Stability & Code Quality)

### 6. Error Unwrapping & Telemetry (R-06, R-04)
- **Contaminated Catches:** 30+ files use `catch (e: any)` or log `String(e)` instead of the safe `e instanceof Error ? e.message : String(e)`.
- **Missing Context:** `TelemetryService.ts` logs BLE errors but fails to attach `rssi` to the telemetry payload.

### 7. Type Safety & Any Bypasses (R-08)
- **Domain Interfaces:** `dashboard.types.ts` and `ble.types.ts` use `[key: string]: any` and `Record<string, any>`, defeating TS compilation.
- **Mock Contamination:** `SpeedTrackingService.offline.test.ts` uses `unknown` casts to bypass strict test boundaries. `react-native-worklets.web.js` is untyped.

### 8. Offline-First Mandate (R-05)
- **Cloud-First Blocking:** `ScenesService.ts` and `CrewService.ts` await Supabase responses *before* returning `AsyncStorage` cache, breaking optimistic UI.
- **Network Gating:** `CrewJoinScreen` and others fire Supabase mutations without checking `isOfflineMode`.

### 9. State Matrix & Boolean Traps (R-14, R-18)
- **Missing FSMs:** `SessionContext.tsx`, `HardwareSetupWizardScreen.tsx`, `CrewLandingScreen`, `CrewJoinScreen`, and `AuthScreen` rely on scattered variables (`isLoadingNearby`, `hasStartedScan`) instead of rigid state machines.
- **Empty/Error States:** `CrewMemberDashboard.tsx` and `AdminPicksScheduler.tsx` map over empty arrays without a `Loading`, `Error`, or `Empty` UI state.

### 10. Stale Closures (R-12)
- **Interval Captures:** `useAppMicrophone.ts`, `useStreetMode.ts`, and `useOfflineSyncWorker.ts` capture stale state snapshots inside `setInterval` and `addListener` because they lack `useRef` persistence.

### 11. HAL Enclosure (R-19)
- **UI Bit-banging:** Diagnostic Lab tabs manually construct `0x59` raw payloads instead of routing through `ZenggeProtocol.ts`.
