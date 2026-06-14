# Implementation Plan: sweep-hooks-useControllerDispatch.ts

## Goal
Fix static audit findings for the `sweep-hooks-useControllerDispatch.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useControllerDispatch.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts)
- **Line:** 51
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** The async BLE command dispatches await safeWrite(...) calls without wrapping them in try-catch. Although safeWrite registers a .catch() callback for AppLogger tracking, it still forwards the rejected promise. Unhandled rejections will throw in the calling component, causing state hangs.
- **Suggested Fix:** Modify safeWrite to return a catch-wrapped promise that resolves to false/void on failure, or wrap all await calls inside the dispatch hook in try-catch structures.

### [MODIFY] [useControllerDispatch.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts)
- **Line:** 90
- **Rule:** R-10
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** All BLE write loops (sendColor, applyFixedPattern, applyStaticModePattern, applyEmergencyPattern, setPower, setMultiColor) execute sequential group writes using 'for (const device of targets) { await safeWrite(...) }'. This blocks serial execution on the main thread and introduces significant multi-device sync latency for grouped skates.
- **Suggested Fix:** Map targets to promises using targets.map(device => safeWrite(...)) and resolve them concurrently with Promise.all() to leverage parallel BLE dispatch queues.

### [MODIFY] [useControllerDispatch.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts)
- **Line:** 322
- **Rule:** R-10
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Concurrent mapped write anti-pattern in handleMusicChange: targets.forEach dispatches write commands to all devices concurrently, and result.packets.forEach dispatches music configuration packet buffers to a single device concurrently, without sequencing or awaiting execution. This violates sequential group writing safety, potentially triggering GATT 133 collisions on Android.
- **Suggested Fix:** Replace the outer targets.forEach and the inner result.packets.forEach with sequential for...of loops. Await each safeWrite call sequentially, incorporating BLE_TIMING.INTER_DEVICE_WRITE_GAP_MS between devices to ensure GATT queue stability.

### [MODIFY] [useControllerDispatch.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts)
- **Line:** 68
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Bypassing AppLogger telemetry context by using raw console.warn.
- **Suggested Fix:** Replace with AppLogger.warn and provide required telemetry context.

### [MODIFY] [useControllerDispatch.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts)
- **Line:** 87
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Bypassing AppLogger telemetry context by using raw console.error.
- **Suggested Fix:** Replace with AppLogger.error and provide required telemetry context.

### [MODIFY] [useControllerDispatch.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts)
- **Line:** 120
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Bypassing AppLogger telemetry context by using raw console.error.
- **Suggested Fix:** Replace with AppLogger.error and provide required telemetry context.

### [MODIFY] [useControllerDispatch.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts)
- **Line:** 191
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Bypassing AppLogger telemetry context by using raw console.error.
- **Suggested Fix:** Replace with AppLogger.error and provide required telemetry context.

### [MODIFY] [useControllerDispatch.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts)
- **Line:** 246
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Bypassing AppLogger telemetry context by using raw console.error.
- **Suggested Fix:** Replace with AppLogger.error and provide required telemetry context.

### [MODIFY] [useControllerDispatch.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts)
- **Line:** 302
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Bypassing AppLogger telemetry context by using raw console.error.
- **Suggested Fix:** Replace with AppLogger.error and provide required telemetry context.

### [MODIFY] [useControllerDispatch.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts)
- **Line:** 356
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Bypassing AppLogger telemetry context by using raw console.error.
- **Suggested Fix:** Replace with AppLogger.error and provide required telemetry context.

### [MODIFY] [useControllerDispatch.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useControllerDispatch.ts)
- **Line:** 378
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Bypassing AppLogger telemetry context by using raw console.error.
- **Suggested Fix:** Replace with AppLogger.error and provide required telemetry context.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
