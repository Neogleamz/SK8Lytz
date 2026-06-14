# Implementation Plan: sweep-hooks-useDashboardCrew.ts

## Goal
Fix static audit findings for the `sweep-hooks-useDashboardCrew.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useDashboardCrew.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardCrew.ts)
- **Line:** 68
- **Rule:** R-26
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Auto-rejoin lifecycle triggers async rejoin logic inside useEffect without a boolean guard to block multiple concurrent rejoin queries.
- **Suggested Fix:** Implement a boolean re-entrancy guard to verify if checkActiveSession is already in progress.

### [MODIFY] [useDashboardCrew.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useDashboardCrew.ts)
- **Line:** 98
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
