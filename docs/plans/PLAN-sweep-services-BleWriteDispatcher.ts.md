# Implementation Plan: sweep-services-BleWriteDispatcher.ts

## Goal
Fix static audit findings for the `sweep-services-BleWriteDispatcher.ts` domain cluster.

## Proposed Changes

### [MODIFY] [BleWriteDispatcher.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteDispatcher.ts)
- **Line:** 77
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Awaited promise for storage/network IO without try-catch block or chained .catch: "await _executeWriteToDeviceInternal(
          payload,
          targetDeviceId,
          thisGeneration,
          bleManager,
          connectedDevices,
          ghostedDeviceIds,
          mtuMap,
          adapterMap,
          stateRefs
        )"
- **Suggested Fix:** Wrap the call in a try-catch block or append .catch() to handle rejection.

### [MODIFY] [BleWriteDispatcher.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteDispatcher.ts)
- **Line:** 148
- **Rule:** R-09, R-04
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** The MAC address of the device (`device.id`) is interpolated directly into a telemetry warning message, bypassing key-based scrubbing in `AppLogger.warn`.
- **Suggested Fix:** Wrap the variable with `scrubPII(device.id)` before interpolation.

### [MODIFY] [BleWriteDispatcher.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteDispatcher.ts)
- **Line:** 288
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Awaited promise for storage/network IO without try-catch block or chained .catch: "await _executeProtocolResultsInternal(
          payloads,
          thisGeneration,
          connectedDevices,
          ghostedDeviceIds,
          mtuMap,
          adapterMap,
          stateRefs
        )"
- **Suggested Fix:** Wrap the call in a try-catch block or append .catch() to handle rejection.

### [MODIFY] [BleWriteDispatcher.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteDispatcher.ts)
- **Line:** 177
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [BleWriteDispatcher.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteDispatcher.ts)
- **Line:** 122
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [BleWriteDispatcher.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteDispatcher.ts)
- **Line:** 132
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [BleWriteDispatcher.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteDispatcher.ts)
- **Line:** 241
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [BleWriteDispatcher.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteDispatcher.ts)
- **Line:** 360
- **Rule:** R-04, R-09
- **Severity:** LOW | **Confidence:** CONFIRMED
- **Description:** The target MAC address (`targetDeviceId`) is interpolated directly into a telemetry warning message, bypassing key-based scrubbing in `AppLogger.warn`.
- **Suggested Fix:** Wrap the variable with `scrubPII(targetDeviceId)` before interpolation.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
