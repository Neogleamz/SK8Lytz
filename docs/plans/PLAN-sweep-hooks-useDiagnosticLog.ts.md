# Implementation Plan: sweep-hooks-useDiagnosticLog.ts

## Goal
Fix static audit findings for the `sweep-hooks-useDiagnosticLog.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useDiagnosticLog.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDiagnosticLog.ts)
- **Line:** 11
- **Rule:** R-24
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Diagnostic verdict log key '@sk8lytz_diag_test_log' is defined locally instead of using central storageKeys.ts registry.
- **Suggested Fix:** Move to constants/storageKeys.ts and import it.

### [MODIFY] [useDiagnosticLog.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDiagnosticLog.ts)
- **Line:** 90
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [useDiagnosticLog.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDiagnosticLog.ts)
- **Line:** 173
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [useDiagnosticLog.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDiagnosticLog.ts)
- **Line:** 192
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
