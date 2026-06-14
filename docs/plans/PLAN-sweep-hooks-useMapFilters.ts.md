# Implementation Plan: sweep-hooks-useMapFilters.ts

## Goal
Fix static audit findings for the `sweep-hooks-useMapFilters.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useMapFilters.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useMapFilters.ts)
- **Line:** 8
- **Rule:** R-24
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Map filters cache key '@Sk8lytz_MapFilters' is defined locally instead of being imported from storageKeys.ts.
- **Suggested Fix:** Move to constants/storageKeys.ts and import it.

### [MODIFY] [useMapFilters.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useMapFilters.ts)
- **Line:** 40
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [useMapFilters.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useMapFilters.ts)
- **Line:** 54
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
