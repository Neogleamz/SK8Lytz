# Implementation Plan: sweep-hooks-useCuratedPicks.ts

## Goal
Fix static audit findings for the `sweep-hooks-useCuratedPicks.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useCuratedPicks.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCuratedPicks.ts)
- **Line:** 13
- **Rule:** R-24
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Curated picks cache key relies on dynamic suffix prefixing 'PicksCache' but the template is defined locally instead of being centrally cataloged.
- **Suggested Fix:** Register the PicksCache key prefix or pattern in storageKeys.ts.

### [MODIFY] [useCuratedPicks.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCuratedPicks.ts)
- **Line:** 105
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
