# Implementation Plan: BATCH:sweep-hooks-core

## Proposed Changes

### Domain: hooks-core

#### [MODIFY] src/hooks/useAdminSettings.ts
- Line 28 (R-08): Use of 'any' in updateSetting signature.
- Line 10 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/hooks/useBLE.ts
- Line 45 (R-08): any type cast for BleManager and State modules
- Line 283 (R-08): any type cast in handleNotification signature
- Line 304 (R-04): Error logged without payload_size or ssi context
- Line 162 (R-18): Scattered boolean state (Boolean Trap) used instead of FSM state union
- Line 163 (R-18): Scattered boolean state (Boolean Trap) used instead of FSM state union
- Line 164 (R-18): Scattered boolean state (Boolean Trap) used instead of FSM state union
- Line 269 (R-21): Split-Brain Connected Devices: useBLE maintains a connectedDevicesRef and an updateConnectedDevices write-through setter to circumvent XState's 1-frame propagation delay from BleMachine. While documented, this creates two parallel state arrays tracking active BLE connections.

#### [MODIFY] src/hooks/useSkateStats.ts
- Line 15 (R-05): Bypassing AsyncStorage caching for data that should work offline
- Line 25 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/hooks/useScenes.ts
- Line 9 (R-18): Scattered boolean states instead of FSM state union
- Line 22 (R-04): Error logged without payload_size or ssi context
- Line 9 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/hooks/useFavorites.ts
- Line 17 (R-26): Async function called from useEffect without a boolean re-entrancy guard
- Line 19 (R-18): Single boolean state which may need FSM refactoring if complexity grows
- Line 40 (R-26): Async function called from useEffect/setInterval without a boolean re-entrancy guard.

#### [MODIFY] src/hooks/useCrewHub.ts
- Line 71 (R-11): Missing catch block on locationService.getSilentLocation().then() Promise chain.
- Line 55 (R-04): Error logged without payload_size or ssi context
- Line 65 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/hooks/useDashboardCrew.ts
- Line 86 (R-17): Race condition in useEffect cleanup for async tryRejoin() can leak Realtime channel subscriptions if unmounted before promise resolves.
- Line 39 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/hooks/useDashboardProfile.ts
- Line 100 (R-24): AsyncStorage Key Collision / Hardcoded key used outside registry.
- Line 135 (R-04): Error logged without payload_size or ssi context
- Line ? (Unknown): useDashboardProfile receives an onCrewJoinNotification prop function which is recreated every render in DashboardScreen (it captures setPendingJoinCrewId and setIsCrewModalVisible state setters, and can capture any dynamic state). However, it is captured directly in an empty-dependency useEffect that registers it to notificationService.setJoinHandler. Since it has no useRef forwarding, if the callback relied on updated state, it would execute with stale initial closure data. Standard React convention dictates forwarding dynamically-created callback props via a stable ref before passing to external event listeners.

#### [MODIFY] src/hooks/useHardwareNotifications.ts
- Line 31 (R-08): Widespread usage of any types for config objects and state updaters.
- Line 93 (R-09): deviceId logged directly to AppLogger, leaking the raw BLE MAC address on Android.

#### [MODIFY] src/hooks/useStreetMode.ts
- Line 32 (R-08): Type Safety: Usage of `any` cast for `hwSettings`.
- Line 38 (R-08): Type Safety: Usage of `any` type for `deviceContext` values.
- Line 77 (R-18): Boolean Trap: `isStreetBraking` is managed alongside the `motionState` FSM union, leading to scattered states.
- Line 140 (R-11): Promise/IO Safety: Async `writeToDevice` call lacks try/catch or `.catch()` error handling.
- Line 77 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/hooks/useAppMicrophone.ts
- Line 98 (R-11): Promise/IO Safety: Async `writeToDevice` call lacks try/catch or `.catch()` error handling.
- Line 102 (R-04): Error logged without payload_size or ssi context
- Line 44 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/hooks/useProtocolBuilder.ts
- Line 24 (R-08): any cast used for val parameter in safeParseInt
- Line 76 (R-18): Boolean trap used for mic source selection
- Line 126 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/hooks/useProductCatalog.ts
- Line 28 (R-08): any type used in Record<string, any> for Supabase row parameter

#### [MODIFY] src/hooks/useGlobalTelemetry.ts
- Line 222 (R-17): Async race condition causing Location subscription leak. If isSkateSessionActive flips to false during the await Location.watchPositionAsync() call, the cleanup function runs while locationSubRef.current is null. The subscription is then created and never removed.

#### [MODIFY] src/hooks/useDeviceStateLedger.ts
- Line 47 (R-08): Use of explicit `any` cast for global namespace pollution, violating strict type safety.
- Line 95 (R-04): Error logged without payload_size or ssi context
- Line 109 (R-04): Error logged without payload_size or ssi context
- Line 165 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/hooks/useDashboardController.tsx
- Line 17 (R-08): Multiple 'any' type definitions in UseDashboardControllerProps interface
- Line 162 (R-06): Empty catch block ignoring network promise rejection
- Line 99 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/hooks/useDockedControllerState.ts
- Line 109 (R-08): Parameter 'scenePayload' is typed as 'any'
- Line 102 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/hooks/useControllerDispatch.ts
- Line 35 (R-08): Parameter 'hwSettings' is typed as 'any'
- Line 72 (R-04): Error logged without payload_size or ssi context
- Line 104 (R-04): Error logged without payload_size or ssi context
- Line 175 (R-04): Error logged without payload_size or ssi context
- Line 230 (R-04): Error logged without payload_size or ssi context
- Line 286 (R-04): Error logged without payload_size or ssi context
- Line 340 (R-04): Error logged without payload_size or ssi context
- Line 362 (R-04): Error logged without payload_size or ssi context
- Line 45 (R-21): Split-Brain Protocol Dispatch: useControllerDispatch duplicates BLE dispatch functionality from useProtocolDispatch. It hardcodes Zengge protocol bypasses (e.g., 0x59 FREEZE) and manages its own LRU pattern payload cache, circumventing the IControllerProtocol adapter layer used across the rest of the application.

#### [MODIFY] src/hooks/dev/useWebDemoConsoleBridge.ts
- Line 23 (R-04): Error logged without payload_size or ssi context
- Line 58 (R-04): Error logged without payload_size or ssi context
- Line 66 (R-04): Error logged without payload_size or ssi context
- Line 31 (R-06): Missing standard e instanceof Error unwrapping in catch block.
- Line 43 (R-06): Missing standard e instanceof Error unwrapping in catch block.

#### [MODIFY] src/hooks/useAccountOverview.ts
- Line 165 (R-04): Error logged without payload_size or ssi context
- Line 208 (R-04): Error logged without payload_size or ssi context
- Line 248 (R-04): Error logged without payload_size or ssi context
- Line 294 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/hooks/useAdminTelemetry.ts
- Line 126 (R-04): Error logged without payload_size or ssi context
- Line 79 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/hooks/useCuratedPicks.ts
- Line 40 (R-04): Error logged without payload_size or ssi context
- Line 58 (R-04): Error logged without payload_size or ssi context
- Line 101 (R-04): Error logged without payload_size or ssi context
- Line 26 (R-26): Async function called from useEffect/setInterval without a boolean re-entrancy guard.

#### [MODIFY] src/hooks/useDashboardDeviceConfig.ts
- Line 146 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/hooks/useDiagnosticLog.ts
- Line 125 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/hooks/useGradients.ts
- Line 28 (R-04): Error logged without payload_size or ssi context
- Line 50 (R-04): Error logged without payload_size or ssi context
- Line 60 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/hooks/useProductManager.ts
- Line 76 (R-04): Error logged without payload_size or ssi context
- Line 40 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/hooks/useProtocolDispatch.ts
- Line 31 (R-04): Error logged without payload_size or ssi context
- Line 133 (R-04): Error logged without payload_size or ssi context

#### [MODIFY] src/hooks/useDashboardAutoConnect.ts
- Line 195 (R-09): Array of device names and MAC addresses passed to AppLogger under the key 'devices'. The key 'devices' bypasses the scrubber check, and the scrubber also fails to redact array elements.
- Line 207 (R-09): Array of MAC addresses passed to AppLogger under the key 'macs'. The internal PII scrubber fails to redact primitive strings within arrays, causing a MAC leak.
- Line 372 (R-09): Array of fleet names passed to AppLogger. The internal PII scrubber fails to redact arrays of strings and 'fleets' bypasses the key regex, leaking potentially identifying custom group names.
- Line 247 (R-17): Missing cleanup for timeout in useEffect

#### [MODIFY] C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useBLE.ts
- Line 139 (R-16): Hardcoded delay using setTimeout detected
- Line 202 (R-16): Hardcoded delay using setTimeout detected

#### [MODIFY] C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useControllerAnalytics.ts
- Line 44 (R-16): Hardcoded delay using setTimeout detected
- Line 89 (R-16): Hardcoded delay using setTimeout detected
- Line 97 (R-16): Hardcoded delay using setTimeout detected
- Line 106 (R-16): Hardcoded delay using setTimeout detected

#### [MODIFY] C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useCrewHub.ts
- Line 95 (R-16): Hardcoded delay using setTimeout detected

#### [MODIFY] C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useCrewManage.ts
- Line 83 (R-16): Hardcoded delay using setTimeout detected

#### [MODIFY] C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useCrewSession.ts
- Line 100 (R-16): Hardcoded delay using setTimeout detected

#### [MODIFY] C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useDashboardAutoConnect.ts
- Line 135 (R-16): Hardcoded delay using setTimeout detected
- Line 180 (R-16): Hardcoded delay using setTimeout detected
- Line 228 (R-16): Hardcoded delay using setTimeout detected
- Line 247 (R-16): Hardcoded delay using setTimeout detected
- Line 406 (R-16): Hardcoded delay using setTimeout detected
- Line 430 (R-16): Hardcoded delay using setTimeout detected

#### [MODIFY] C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useDashboardCrew.ts
- Line 76 (R-16): Hardcoded delay using setTimeout detected

#### [MODIFY] C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useDeviceStateLedger.ts
- Line 61 (R-16): Hardcoded delay using setTimeout detected
- Line 133 (R-16): Hardcoded delay using setTimeout detected

#### [MODIFY] C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useGlobalTelemetry.ts
- Line 69 (R-16): Hardcoded delay using setTimeout detected

#### [MODIFY] src/hooks/useControllerAnalytics.ts
- Line 89 (R-17): Missing cleanup for timeout in useEffect
- Line 97 (R-17): Missing cleanup for timeout in useEffect
- Line 106 (R-17): Missing cleanup for timeout in useEffect

#### [MODIFY] src/hooks/useCrewManage.ts
- Line 25 (R-18): Scattered boolean state (Boolean Trap) used instead of FSM state union
- Line 26 (R-18): Scattered boolean state (Boolean Trap) used instead of FSM state union

#### [MODIFY] src/hooks/useCrewSession.ts
- Line 19 (R-18): Single boolean state which may need FSM refactoring if complexity grows
- Line 17 (R-21): Split-Brain Crew Session State: useCrewSession duplicates currentSession and currentRole via useState from the CrewService class properties. It manually syncs React state with the singleton class property mutations.

#### [MODIFY] src/hooks/useDashboardGroups.ts
- Line 278 (R-18): Scattered boolean state (Boolean Trap) used instead of FSM state union
- Line 297 (R-18): Scattered boolean state (Boolean Trap) used instead of FSM state union
- Line 298 (R-18): Scattered boolean state (Boolean Trap) used instead of FSM state union

#### [MODIFY] src/hooks/useRecentSpots.ts
- Line 17 (R-18): Single boolean state which may need FSM refactoring if complexity grows

#### [MODIFY] src/hooks/useMapFilters.ts
- Line 25 (R-26): Async function called from useEffect/setInterval without a boolean re-entrancy guard.

#### [MODIFY] src/hooks/useRegistration.ts
- Line 88 (R-26): Async function called from useEffect/setInterval without a boolean re-entrancy guard.
