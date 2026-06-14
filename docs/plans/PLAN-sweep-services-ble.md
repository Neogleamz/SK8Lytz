# Implementation Plan: sweep-services-ble

## Goal
Fix static audit findings for the `sweep-services-ble` domain cluster.

## Proposed Changes

### [MODIFY] [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- **Line:** 94
- **Rule:** R-09, R-04
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** Warnings and errors are logged during device connection/disconnect sequence without context fields payload_size or ssi.
- **Suggested Fix:** Include payload_size and ssi context properties: AppLogger.warn('[ConnectService] Disconnecting dirty device', { deviceId: scrubPII(device.id), error: String(e), payload_size: 0, ssi: 0 });

### [MODIFY] [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- **Line:** 277
- **Rule:** R-09
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** The 'id' parameter logs a raw physical MAC address (`conn.id`). Since the key 'id' is not registered in AppLogger's `PII_KEY_PATTERNS` whitelist, the physical MAC address is logged raw to telemetry.
- **Suggested Fix:** Wrap `conn.id` with `scrubPII(conn.id)`, or register 'id' as a scrubbed key in AppLogger.

### [MODIFY] [HeartbeatService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/HeartbeatService.ts)
- **Line:** 118
- **Rule:** R-09
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Raw MAC address is logged directly to AppLogger when a heartbeat fails.
- **Suggested Fix:** Apply scrubPII: AppLogger.warn('[Heartbeat] Device connection lost', { deviceId: scrubPII(d.id) });

### [MODIFY] [InterrogatorService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/InterrogatorService.ts)
- **Line:** 141
- **Rule:** R-24
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Dynamic key construction function HW_CACHE_KEY(mac) uses locally defined string prefix '@sk8_hw_' which duplicates the key structure in HardwareSetupWizardScreen.tsx.
- **Suggested Fix:** Move HW_CACHE_KEY generator or prefix constant to storageKeys.ts and share it.

### [MODIFY] [RecoveryService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts)
- **Line:** 114
- **Rule:** R-09, R-04
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** Telemetry warning logs for MTU and ping failures omit the required payload_size and ssi context parameters.
- **Suggested Fix:** Add payload_size and ssi properties to the telemetry log payload.

### [MODIFY] [RSSIService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RSSIService.ts)
- **Line:** 81
- **Rule:** R-09, R-04
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [BleMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts)
- **Line:** 80
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** AsyncStorage.setItem is called within a synchronous try/catch block without awaiting or chaining catch, causing unhandled promise rejection risk.
- **Suggested Fix:** Chain a .catch() handler: AsyncStorage.setItem(RESTORE_KEY, JSON.stringify(ids)).catch(e => AppLogger.warn('[BleMachine] Failed to save restore state', { error: String(e) }));

### [MODIFY] [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- **Line:** 132
- **Rule:** R-09
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** The 'id' parameter logs a raw mock MAC address (`d.id`). Since the key 'id' is not registered in AppLogger's `PII_KEY_PATTERNS` whitelist, the MAC address is logged raw. Lower impact since it is demo/mock data.
- **Suggested Fix:** Wrap `d.id` with `scrubPII(d.id)`, or register 'id' as a scrubbed key in AppLogger.

### [MODIFY] [HeartbeatService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/HeartbeatService.ts)
- **Line:** 87
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Heartbeat errors are logged without payload_size or ssi context fields.
- **Suggested Fix:** Include payload_size and ssi context properties.

### [MODIFY] [InterrogatorService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/InterrogatorService.ts)
- **Line:** 50
- **Rule:** R-06
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Uses 'String(err)' inside an AppLogger.warn context payload without checking if it is an instance of Error. The standard project format requires 'err instanceof Error ? err.message : String(err)'.
- **Suggested Fix:** Use standard unwrapping check:

const errorMsg = e instanceof Error ? e.message : String(e);
AppLogger.warn(..., { error: errorMsg });

### [MODIFY] [InterrogatorService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/InterrogatorService.ts)
- **Line:** 149
- **Rule:** R-06
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Uses 'String(err)' inside an AppLogger.warn context payload without checking if it is an instance of Error. The standard project format requires 'err instanceof Error ? err.message : String(err)'.
- **Suggested Fix:** Use standard unwrapping check:

const errorMsg = err instanceof Error ? err.message : String(err);
AppLogger.warn(..., { error: errorMsg });

### [MODIFY] [InterrogatorService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/InterrogatorService.ts)
- **Line:** 186
- **Rule:** R-16
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** Hardcoded setTimeout delay is used to stagger sequential interrogation tasks instead of pacing through queue delays.
- **Suggested Fix:** Schedule pacing gaps using BleWriteQueue delays or queue scheduler.

### [MODIFY] [RecoveryService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts)
- **Line:** 222
- **Rule:** R-06
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Uses 'String(err)' inside an AppLogger.warn context payload without checking if it is an instance of Error. The standard project format requires 'err instanceof Error ? err.message : String(err)'.
- **Suggested Fix:** Use standard unwrapping check:

const errorMsg = e instanceof Error ? e.message : String(e);
AppLogger.warn(..., { error: errorMsg });

### [MODIFY] [BleMachine.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/__tests__/BleMachine.test.ts)
- **Line:** 131
- **Rule:** R-16
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Uses hardcoded `setTimeout` of 0 inside a Promise structure to flush microtask transitions.
- **Suggested Fix:** Flush microtask queues using `await Promise.resolve()` repeatedly, or use standard Jest fake clock advancements.

### [MODIFY] [ConnectService.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/__tests__/ConnectService.test.ts)
- **Line:** 122
- **Rule:** R-19
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Mock return value for getHandshakePayloads constructs a mock packet array with raw bytes [[0x10, 0x20]] outside src/protocols/.
- **Suggested Fix:** Move raw test payloads to a test helper inside src/protocols/ or mock the return value using protocol-defined constants.

### [MODIFY] [ConnectService.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/__tests__/ConnectService.test.ts)
- **Line:** 133
- **Rule:** R-19
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Mock return value for getHandshakePayloads constructs a mock packet array with raw bytes [[0x10, 0x20]] outside src/protocols/.
- **Suggested Fix:** Move raw test payloads to a test helper inside src/protocols/ or mock the return value using protocol-defined constants.

### [MODIFY] [HeartbeatService.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/__tests__/HeartbeatService.test.ts)
- **Line:** 130
- **Rule:** R-19
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Mock return value for buildQuerySettings constructs a mock packet array with raw bytes [[0x63, 0x12, 0x21, 0x0f, 0xa5]] outside src/protocols/.
- **Suggested Fix:** Move raw test payloads to a test helper inside src/protocols/ or mock the return value using protocol-defined constants.

### [MODIFY] [HeartbeatService.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/__tests__/HeartbeatService.test.ts)
- **Line:** 288
- **Rule:** R-19
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Mock return value for buildQuerySettings constructs a mock packet array with raw bytes [[0x63, 0x12]] outside src/protocols/.
- **Suggested Fix:** Move raw test payloads to a test helper inside src/protocols/ or mock the return value using protocol-defined constants.

### [MODIFY] [HeartbeatService.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/__tests__/HeartbeatService.test.ts)
- **Line:** 312
- **Rule:** R-19
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Mock return value for buildQuerySettings constructs a mock packet array with raw bytes [[0x63, 0x12]] outside src/protocols/.
- **Suggested Fix:** Move raw test payloads to a test helper inside src/protocols/ or mock the return value using protocol-defined constants.

### [MODIFY] [InterrogatorService.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/__tests__/InterrogatorService.test.ts)
- **Line:** 128
- **Rule:** R-19
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Mock return value for buildQuerySettings constructs a mock packet array with raw bytes [[0x63, 0x12]] outside src/protocols/.
- **Suggested Fix:** Move raw test payloads to a test helper inside src/protocols/ or mock the return value using protocol-defined constants.

### [MODIFY] [RecoveryService.test.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/__tests__/RecoveryService.test.ts)
- **Line:** 135
- **Rule:** R-19
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Mock return value for buildQuerySettings constructs a mock packet array with raw bytes [[0x63, 0x12]] outside src/protocols/.
- **Suggested Fix:** Move raw test payloads to a test helper inside src/protocols/ or mock the return value using protocol-defined constants.

### [MODIFY] [BleMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts)
- **Line:** 128
- **Rule:** R-08
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Usage of explicit 'any' cast in initialization.
- **Suggested Fix:** Use type union: bleManager: null as BleManager | null

### [MODIFY] [BleMachine.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts)
- **Line:** 193
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Dynamic error is caught and its message property is read directly without confirming it is an instance of Error, risking runtime TypeError if error is a string or null.
- **Suggested Fix:** Unwrap error standardly: AppLogger.warn('[BleMachine] State restoration failed', err instanceof Error ? err.message : String(err));

### [MODIFY] [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- **Line:** 65
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- **Line:** 95
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- **Line:** 103
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- **Line:** 107
- **Rule:** R-06, R-08
- **Severity:** LOW | **Confidence:** CONFIRMED
- **Description:** Catch block reads error.message on dynamic parameter without checking if it is an instance of Error.
- **Suggested Fix:** Use e instanceof Error ? e.message : String(e)

### [MODIFY] [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- **Line:** 116
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- **Line:** 177
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- **Line:** 198
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- **Line:** 217
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- **Line:** 274
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- **Line:** 280
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [ConnectService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts)
- **Line:** 317
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Passes raw error object directly to AppLogger.error. While AppLogger.error performs standard unwrapping internally, call-site unwrapping is preferred for consistency and safety if AppLogger.error API changes.
- **Suggested Fix:** AppLogger.error('...', outerErr instanceof Error ? outerErr : new Error(String(outerErr)));

### [MODIFY] [HeartbeatService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/HeartbeatService.ts)
- **Line:** 60
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [InterrogatorService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/InterrogatorService.ts)
- **Line:** 47
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [InterrogatorService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/InterrogatorService.ts)
- **Line:** 108
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [InterrogatorService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/InterrogatorService.ts)
- **Line:** 123
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [InterrogatorService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/InterrogatorService.ts)
- **Line:** 136
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [InterrogatorService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/InterrogatorService.ts)
- **Line:** 142
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [InterrogatorService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/InterrogatorService.ts)
- **Line:** 150
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [RecoveryService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts)
- **Line:** 84
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [RecoveryService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts)
- **Line:** 149
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [RecoveryService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts)
- **Line:** 160
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [RecoveryService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts)
- **Line:** 223
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [RSSIService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RSSIService.ts)
- **Line:** 41
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [RSSIService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RSSIService.ts)
- **Line:** 84
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
