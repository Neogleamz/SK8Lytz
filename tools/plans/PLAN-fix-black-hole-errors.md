# Fix The Black Hole of Errors (Silent Catches)

## Goal
The recent Codebase Audit revealed ~27 locations across 15 files where errors are being silently swallowed using the `.catch(() => {})` pattern. These silent catches are incredibly dangerous, especially in hardware-adjacent hooks (`useBLE.ts`, `useBLESweeper.ts`), because they mask ghost device states, persistence failures, and critical BLE disconnect exceptions. 

This plan details the structured replacement of these empty catches with proper `AppLogger.error()` or `AppLogger.warn()` routing to ensure production visibility.

> [!WARNING]
> **User Review Required**: This epic touches 15 core domain hooks and BLE connection logic. The `[H-RISK]` tag is applied. Please confirm that we should spawn the `fix/black-hole-errors` worktree and begin surgical execution.

## Proposed Changes

### 1. Persistence Layer Hooks (`AsyncStorage`)
*AsyncStorage failures to save user preferences or cached device configurations.*

#### [MODIFY] [useAccountOverview.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useAccountOverview.ts)
- Replace empty catch with `AppLogger.warn('Failed to persist notification preferences', e)`

#### [MODIFY] [useControllerPersistence.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerPersistence.ts)
- Replace empty catch with `AppLogger.warn('Failed to save controller state blob', e)`

#### [MODIFY] [useCuratedPicks.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCuratedPicks.ts)
- Replace empty catch with `AppLogger.warn('Failed to cache curated picks', e)`

#### [MODIFY] [useDeviceStateLedger.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDeviceStateLedger.ts)
- Replace empty catch with `AppLogger.error('Failed to write device state ledger entry', e)`

#### [MODIFY] [useDiagnosticLog.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDiagnosticLog.ts)
- Replace 3 empty catches with `AppLogger.warn` for writing/removing diagnostic logs.

#### [MODIFY] [SkaterStatsPanel.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/account/SkaterStatsPanel.tsx)
- Replace empty catch on AsyncStorage cache write with `AppLogger.warn('Failed to cache skater stats', e)`.

#### [MODIFY] [HardwareSetupWizardScreen.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/screens/Onboarding/HardwareSetupWizardScreen.tsx)
- Replace empty catch on HW Config save with `AppLogger.error('Failed to save hardware setup config', e)`.

---

### 2. The Core Hardware & BLE Hooks
*Critical BLE disconnects, recovery errors, and Sweeper queues.*

#### [MODIFY] [useBLE.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)
- **Line 523**: `requestConnectionPriorityForDevice` failure → `AppLogger.warn('Failed to set connection priority', e)`
- **Line 731**: `previousMutex` await failure → `AppLogger.warn('Previous mutex chain rejected', e)`

#### [MODIFY] [useBLESweeper.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLESweeper.ts)
- 5 instances of empty catches covering `updateConfig`, `bleManager.cancelDeviceConnection()`, and the sweeper queue lock.
- Replace with `AppLogger.error('BLESweeper error', e, { mac })` for criticals, and `warn` for non-critical cache failures.

#### [MODIFY] [useBLEAutoRecovery.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEAutoRecovery.ts)
- Replace empty catch in recovery chain with `AppLogger.error('AutoRecovery chain failed', e)`.

#### [MODIFY] [useBLEGattMutex.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEGattMutex.ts)
- Replace `await prevChain.catch(() => {})` with `AppLogger.warn('GATT Mutex previous chain rejected', e)`.

#### [MODIFY] [useHardwareNotifications.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useHardwareNotifications.ts)
- 3 instances of `DeviceRepository.getInstance().updateConfig(deviceId, ...).catch(() => {})`
- Replace with `AppLogger.error('HardwareNotification sync to DeviceRepository failed', e, { deviceId })`.

#### [MODIFY] [useDashboardAutoConnect.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardAutoConnect.ts)
- Replace `(scanResult as Promise<void>).catch(() => {})` with `AppLogger.warn('AutoConnect background scan rejected', e)`.

---

### 3. API & Social Hooks
*External API requests, Haptics, and Crew Management.*

#### [MODIFY] [PermissionService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/PermissionService.ts)
- Replace `notificationService.init(true).catch(() => {})` with `AppLogger.warn('NotificationService init failed during permission grant', e)`.

#### [MODIFY] [useCrewManage.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewManage.ts)
- Replace empty catch on API request with `AppLogger.error('useCrewManage error swallowed', e)`.

#### [MODIFY] [useDashboardController.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardController.tsx)
- Replace `crewService.leaveSession().catch(() => {})` with `AppLogger.error('Failed to leave Crew Session on dashboard unmount', e)`.

#### [MODIFY] [useOptimisticBLE.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useOptimisticBLE.ts)
- Replace empty catches on `Haptics.impactAsync` and `notificationAsync` with `console.warn` (since Haptics failing is truly low-priority and shouldn't bloat Supabase telemetry).

## Verification Plan

### Automated Tests
- Run `npx tsc --noEmit` from the master fortress to ensure no TS drift when adding the `e` error parameters.
- Run `npm test` to ensure `AppLogger` mocking in the test files does not break due to the new invocations.

### Manual Verification
- Trigger an invalid BLE disconnect to ensure the error successfully populates in the local `AppLogger` buffer.
- Open the Diagnostic Lab UI to visually verify the error is appended rather than silently failing.
