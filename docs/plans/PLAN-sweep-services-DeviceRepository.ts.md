# Implementation Plan: sweep-services-DeviceRepository.ts

## Goal
Fix static audit findings for the `sweep-services-DeviceRepository.ts` domain cluster.

## Proposed Changes

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 40
- **Rule:** R-24
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Locally defined and unregistered storage key '@Sk8lytz_registered_devices' (DEVICES_KEY) bypassing constants/storageKeys.ts. Also, CONFIGS_KEY is redundantly declared as a local string literal instead of being imported from storageKeys.ts.
- **Suggested Fix:** Export DEVICES_KEY from constants/storageKeys.ts, and import both DEVICES_KEY and CONFIGS_KEY in DeviceRepository.ts.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 341
- **Rule:** R-08
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** TypeScript type safety bypass. Using 'as any' to map active devices masks structure types and can lead to runtime issues.
- **Suggested Fix:** Define a proper type or interface for activeDevices containing the device and cast to that interface type (e.g., Device[]).

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 1
- **Rule:** R-23
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** File length exceeds 30KB. Monolithic file size increases code collision risks and makes maintenance difficult.
- **Suggested Fix:** Extract device ledger state persistence and auto-recovery coordination logic into separate services (e.g. DeviceLedgerService.ts, DeviceRecoveryService.ts).

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 712
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Unhandled Promise Rejection risk in fire-and-forget database sync call. An unhandled exception within the sync block will crash the process or leave an unhandled rejection.
- **Suggested Fix:** Attach a .catch() handler to syncDeviceLedger(): syncDeviceLedger().catch(err => AppLogger.warn('DEVICE_LEDGER_SYNC_FAILED', err));

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 27
- **Rule:** R-29
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** DeviceRepository.ts imports type RegisteredDevice from hooks/useRegistration.ts, which imports the value DeviceRepository class from services/DeviceRepository.ts. This forms a circular dependency, although because the reverse import is type-only, it is compiled away at runtime and does not cause runtime issues.
- **Suggested Fix:** Move the RegisteredDevice interface definition to a shared type declaration file (e.g. src/types/devices.types.ts) to break the static dependency loop.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 85
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 91
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 141
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 148
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 238
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 260
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 270
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 328
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 358
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 368
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 376
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 384
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 392
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 414
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 423
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 434
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 495
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 551
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 562
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 585
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 601
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 651
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 674
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 730
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 796
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 802
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 827
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [DeviceRepository.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/DeviceRepository.ts)
- **Line:** 840
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
