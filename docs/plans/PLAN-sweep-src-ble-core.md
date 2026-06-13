# Implementation Plan: sweep-src-ble-core

This is a synthesized sweep plan addressing all rule violations identified in the **BLE_CORE** domain cluster.

## User Review Required

> [!IMPORTANT]
> Verify that the files modified match your expectations and that you've approved the wave ordering before commencing.

## Open Questions

None.

## Proposed Changes

### BLE_CORE Domain File Sector Sweep

Grouped by affected files:

#### [MODIFY] [ble-simulator.test.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/__tests__/ble-simulator.test.ts)
- **Line 82 [HIGH]:** Catch variable 'error' is used without an 'instanceof Error' check. (Rule: R-06)
- **Line 92 [HIGH]:** Catch variable 'error' is used without an 'instanceof Error' check. (Rule: R-06)
- **Line 201 [LOW]:** Constructs a raw byte array manually using new Array() for the 0x59 command type instead of utilizing a protocol adapter. (Rule: R-19)
- **Line 226 [LOW]:** Constructs a raw byte array manually using new Array() for the 0x59 command type instead of utilizing a protocol adapter. (Rule: R-19)

#### [MODIFY] [useBLEBatterySweep.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEBatterySweep.ts)
- **Line 76 [HIGH]:** Battery check is async, but its .then callback does not check if the sweeper was stopped while the query was in flight. If stopSweeper is called, the sweeper is started anyway, leading to ghost scans. (Rule: R-26)
- **Line 95 [LOW]:** Throttled battery check intervals are scheduled inside a useEffect. The hook's return function correctly clears the interval. (Rule: R-22)

#### [MODIFY] [useBLEScanner.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts)
- **Line 38 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)
- **Line 77 [LOW]:** AsyncStorage.getItem(STORAGE_APP_SETTINGS) promise query lacks a .catch() handler, risking unhandled rejection on mount. (Rule: R-11)
- **Line 78 [LOW]:** Scanning sweeper timeout and battery heartbeat poll loops are tracked via local React refs. The primary cleanSweep and useEffect cleanup paths thoroughly clear timeouts and intervals via clearTimeout/clearInterval, resulting in zero leak risk. (Rule: R-22)
- **Line 84 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)
- **Line 230 [HIGH]:** The device MAC address (device.id) is logged under the key 'id' in the payload. Since 'id' is not in PII_KEY_PATTERNS, the MAC address escapes telemetry scrubbing. (Rule: R-09)
- **Line 243 [HIGH]:** The device MAC address (device.id) is logged under the key 'id' in the payload, escaping scrubbing because 'id' is not in the scrub patterns. (Rule: R-09)
- **Line 325 [LOW]:** Mock sandbox timers and manual scan fallback timeouts are not cleared if the scanner is unmounted mid-scan, leading to leaks and execution on unmounted contexts. (Rule: R-22)

#### [MODIFY] [useBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts)
- **Line 217 [LOW]:** AsyncStorage.getItem(STORAGE_DEMO_MODE) hook query lacks a .catch() handler, risking unhandled rejection during app initialization. (Rule: R-11)
- **Line 238 [MEDIUM]:** Centralized key registry bypass. The key '@Sk8lytz_hardware_blacklist' is defined locally inside a hook as CACHE_KEY instead of being imported from storageKeys.ts. (Rule: R-24)
- **Line 306 [MEDIUM]:** The raw MAC address (contained in deviceId) is interpolated directly into the warning log message string. Since the log message is stored in the 'message' key (which is not in PII_KEY_PATTERNS), the MAC address bypasses the scrubber and is logged raw. (Rule: R-09)
- **Line 396-397 [HIGH]:** The raw MAC address (deviceId) is passed in the payload using the key name 'id'. Since 'id' is not in PII_KEY_PATTERNS, AppLogger does not scrub it, leaking the raw MAC address in telemetry. (Rule: R-09)
- **Line 486 [LOW]:** The writeToDevice options signature accepts writeType: 'Response' | 'NoResponse', but the underlying dispatcher implementation (executeWriteToDevice in BleWriteDispatcher.ts) does not support or receive writeType. It exclusively uses writeCharacteristicWithoutResponseForService. While this enforces fire-and-forget writes and avoids GATT blocking, the writeType option in the type signatures of useBLE.ts and dashboard.types.ts is misleading dead code. (Rule: R-02)

#### [MODIFY] [useOptimisticBLE.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useOptimisticBLE.ts)
- **Line 108 [LOW]:** Confirmation and reconciliation UI reset timeouts are not tracked in a ref and not cleared on unmount, risking state updates on unmounted components. (Rule: R-22)

#### [MODIFY] [BleMachine.test.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/__tests__/BleMachine.test.ts)
- **Line 113 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)
- **Line 211 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)

#### [MODIFY] [ConnectService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- **Line 65 [MEDIUM]:** The raw MAC address list is logged to telemetry via AppLogger.warn during blacklist check, leaking PII. (Rule: R-09)
- **Line 112 [MEDIUM]:** AsyncStorage.getItem for demo mode is not wrapped inside a try-catch block, risking unhandled promise rejection if storage fails. (Rule: R-11)
- **Line 229 [MEDIUM]:** Raw MAC address is logged directly inside AppLogger.log telemetry payloads as deviceId/id during connection success/retry events. (Rule: R-09)
- **Line 313 [HIGH]:** Catch variable 'outerErr' is used without an 'instanceof Error' check. (Rule: R-06)

#### [MODIFY] [HeartbeatService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/HeartbeatService.ts)
- **Line 60 [MEDIUM]:** Stale link warnings log raw device MAC address as deviceId directly to telemetry. (Rule: R-09)

#### [MODIFY] [InterrogatorService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/InterrogatorService.ts)
- **Line 46 [MEDIUM]:** Interrogator malformed HW cache warning logs raw MAC address inside the warning context payload. (Rule: R-09)
- **Line 49 [MEDIUM]:** Catch variable 'e' is used without an 'instanceof Error' check. It uses String(e) but does not unwrap instanceof Error. (Rule: R-06)
- **Line 148 [MEDIUM]:** Catch variable 'err' is used without an 'instanceof Error' check. It uses String(err) but does not unwrap instanceof Error. (Rule: R-06)
- **Line 164 [MEDIUM]:** createProbeQueue returns queue methods but does not expose any cleanup callback to clear the scheduled probeQueueTimerRef timer, causing memory/timer leaks on component unmount. (Rule: R-22)
- **Line 174 [HIGH]:** The queue processor processQueue has no boolean lock (e.g. isProcessing), allowing multiple rapid calls to queueDeviceForInterrogation to schedule concurrent async while-loops, causing concurrent interrogateDevice connection attempts and Android GATT 133 collisions. (Rule: R-26)
- **Line 181 [MEDIUM]:** Hardcoded 500ms raw setTimeout delay used to stagger sequential device interrogations inside a loop. (Rule: R-16)

#### [MODIFY] [RecoveryService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts)
- **Line 84 [MEDIUM]:** Automatic recovery logs output raw MAC addresses (deviceId) directly to AppLogger warnings/logs during failure, success, and connection phases. (Rule: R-09)
- **Line 160 [MEDIUM]:** The raw MAC address (contained in deviceId) is concatenated directly into the warning log message string, bypassing scrubbing. (Rule: R-09)
- **Line 196 [MEDIUM]:** Empty catch block swallowing errors completely without logging or unwrapping. (Rule: R-06)
- **Line 222 [MEDIUM]:** Catch variable 'e' is used without an 'instanceof Error' check. It uses String(e) but does not unwrap instanceof Error. (Rule: R-06)

#### [MODIFY] [RSSIService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RSSIService.ts)
- **Line 37 [MEDIUM]:** RSSI poll status logs, critical signal proactive warnings, and weak signal warning payloads output raw device MAC address/deviceId directly to telemetry. (Rule: R-09)

## Verification Plan

### Automated Tests
- Run `npm run verify` to verify TSC, Jest, AST constraints, type-safety, and workflow validations.
