# Implementation Plan: sweep-services-GradientsService.ts

## Goal
Fix static audit findings for the `sweep-services-GradientsService.ts` domain cluster.

## Proposed Changes

### [MODIFY] [GradientsService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GradientsService.ts)
- **Line:** 118
- **Rule:** R-08
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** Type safety bypass. The service uses 'as unknown as' double casting to map preset nodes to Database nodes to avoid recursive JSON constraints.
- **Suggested Fix:** Declare shared TypeScript interfaces or define a mapper utility that structurally validates or safely parses the nodes.

### [MODIFY] [GradientsService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GradientsService.ts)
- **Line:** 10
- **Rule:** R-24
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Locally defined preset gradients key '@Sk8lytz_Builder_Presets' bypassing central registry.
- **Suggested Fix:** Move key to storageKeys.ts and import.

### [MODIFY] [GradientsService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GradientsService.ts)
- **Line:** 78
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Unhandled Promise Rejection risk in fire-and-forget sync. Errors inside syncCloud() (e.g. Map operations or destructuring of null fields) will cause unhandled rejections.
- **Suggested Fix:** Add a .catch() handler: syncCloud().catch(err => AppLogger.warn('GRADIENT_SYNC_FAILED', err));

### [MODIFY] [GradientsService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GradientsService.ts)
- **Line:** 44
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [GradientsService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GradientsService.ts)
- **Line:** 60
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [GradientsService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GradientsService.ts)
- **Line:** 74
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [GradientsService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GradientsService.ts)
- **Line:** 128
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [GradientsService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/GradientsService.ts)
- **Line:** 152
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
