# Implementation Plan: sweep-hooks-useCrewProximityRadar.ts

## Goal
Fix static audit findings for the `sweep-hooks-useCrewProximityRadar.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useCrewProximityRadar.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewProximityRadar.ts)
- **Line:** 53
- **Rule:** R-12
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Captures stale state snapshots inside the radar scanner loop due to direct state usage inside setInterval without refs.
- **Suggested Fix:** Mirror the skaters state list in a mutable useRef to guarantee access to the latest state inside the interval.

### [MODIFY] [useCrewProximityRadar.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewProximityRadar.ts)
- **Line:** 124
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.

// SKIPPED: The finding for line 53 regarding stale state inside setInterval without refs is a false positive. useCrewProximityRadar.ts does not contain an interval, radar scanner loop, or a skaters state list.
