# Implementation Plan: sweep-hooks-useRecentSpots.ts

## Goal
Fix static audit findings for the `sweep-hooks-useRecentSpots.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useRecentSpots.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useRecentSpots.ts)
- **Line:** 8
- **Rule:** R-24
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Recent locations key '@Sk8lytz_RecentLocations' defined as local constant instead of importing from storageKeys.ts.
- **Suggested Fix:** Move constant to storageKeys.ts and import it.

### [MODIFY] [useRecentSpots.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useRecentSpots.ts)
- **Line:** 42
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [useRecentSpots.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useRecentSpots.ts)
- **Line:** 73
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
