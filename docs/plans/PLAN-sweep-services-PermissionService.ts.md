# Implementation Plan: sweep-services-PermissionService.ts

## Goal
Fix static audit findings for the `sweep-services-PermissionService.ts` domain cluster.

## Proposed Changes

### [MODIFY] [PermissionService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/PermissionService.ts)
- **Line:** 50
- **Rule:** R-04
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [PermissionService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/PermissionService.ts)
- **Line:** 132
- **Rule:** R-04
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [PermissionService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/PermissionService.ts)
- **Line:** 174
- **Rule:** R-04
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [PermissionService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/PermissionService.ts)
- **Line:** 184
- **Rule:** R-04
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [PermissionService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/PermissionService.ts)
- **Line:** 14
- **Rule:** R-24
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Permissions opt-out ledger key '@sk8lytz_permissions_optout' defined locally instead of registering in storageKeys.ts.
- **Suggested Fix:** Move to constants/storageKeys.ts and import it.

### [MODIFY] [PermissionService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/PermissionService.ts)
- **Line:** 250
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [PermissionService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/PermissionService.ts)
- **Line:** 260
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
