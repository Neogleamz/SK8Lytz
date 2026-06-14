# Implementation Plan: sweep-hooks-useProductCatalog.ts

## Goal
Fix static audit findings for the `sweep-hooks-useProductCatalog.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useProductCatalog.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useProductCatalog.ts)
- **Line:** 154
- **Rule:** R-12
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** saveProfile callback has an empty dependency array ([]) but invokes syncFromCloud, which is re-created on every render. This creates a stale closure capturing a stale syncFromCloud reference.
- **Suggested Fix:** Wrap syncFromCloud in useCallback and add it to saveProfile's dependency array.

### [MODIFY] [useProductCatalog.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useProductCatalog.ts)
- **Line:** 109
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** CONFIRMED
- **Description:** Warning logs in loadCachedCatalog, syncFromCloud, and saveProfile lack payload_size and ssi telemetry context keys.
- **Suggested Fix:** Add payload_size: 0, ssi: 0 keys to the warning log context parameters.

### [MODIFY] [useProductCatalog.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useProductCatalog.ts)
- **Line:** 134
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [useProductCatalog.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useProductCatalog.ts)
- **Line:** 188
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
