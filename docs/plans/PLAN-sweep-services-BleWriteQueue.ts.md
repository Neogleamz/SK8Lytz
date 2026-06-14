# Implementation Plan: sweep-services-BleWriteQueue.ts

## Goal
Fix static audit findings for the `sweep-services-BleWriteQueue.ts` domain cluster.

## Proposed Changes

### [MODIFY] [BleWriteQueue.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteQueue.ts)
- **Line:** 209
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** Queue warnings for high latency and transient retries are logged without payload_size or ssi context.
- **Suggested Fix:** Include payload_size and ssi context properties.

### [MODIFY] [BleWriteQueue.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteQueue.ts)
- **Line:** 214
- **Rule:** R-06
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Uses 'String(err)' inside an AppLogger.warn context payload without checking if it is an instance of Error. The standard project format requires 'err instanceof Error ? err.message : String(err)'.
- **Suggested Fix:** Use standard unwrapping check:

const errorMsg = retryErr instanceof Error ? retryErr.message : String(retryErr);
AppLogger.warn(..., { error: errorMsg });

### [MODIFY] [BleWriteQueue.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteQueue.ts)
- **Line:** 134
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [BleWriteQueue.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteQueue.ts)
- **Line:** 195
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [BleWriteQueue.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteQueue.ts)
- **Line:** 215
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
