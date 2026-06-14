# Implementation Plan: sweep-services-BlePingService.ts

## Goal
Fix static audit findings for the `sweep-services-BlePingService.ts` domain cluster.

## Proposed Changes

### [MODIFY] [BlePingService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BlePingService.ts)
- **Line:** 47
- **Rule:** R-08
- **Severity:** LOW | **Confidence:** CONFIRMED
- **Description:** Double casting of a BleManagerLike structure to the third-party BleManager type to bypass interface mismatch warnings.
- **Suggested Fix:** Refactor interface specifications or align types directly to avoid laundering through 'as unknown'.

### [MODIFY] [BlePingService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BlePingService.ts)
- **Line:** 61
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [BlePingService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BlePingService.ts)
- **Line:** 74
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [BlePingService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BlePingService.ts)
- **Line:** 108
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [BlePingService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BlePingService.ts)
- **Line:** 126
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [BlePingService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BlePingService.ts)
- **Line:** 139
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [BlePingService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BlePingService.ts)
- **Line:** 162
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [BlePingService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BlePingService.ts)
- **Line:** 169
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
