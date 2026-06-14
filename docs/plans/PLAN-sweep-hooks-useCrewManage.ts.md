# Implementation Plan: sweep-hooks-useCrewManage.ts

## Goal
Fix static audit findings for the `sweep-hooks-useCrewManage.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useCrewManage.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewManage.ts)
- **Line:** 65
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [useCrewManage.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewManage.ts)
- **Line:** 70
- **Rule:** R-26
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Loads crew statistics when `selectedCrewDetail` changes. If the selection changes or the hook is unmounted before the async query resolves, stats are written to state on an unmounted or stale component.
- **Suggested Fix:** Declare `let active = true;` inside `useEffect`, check `if (active) setCrewStats(...)`, and return a cleanup function setting `active = false;`.

### [MODIFY] [useCrewManage.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewManage.ts)
- **Line:** 76
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

### [MODIFY] [useCrewManage.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewManage.ts)
- **Line:** 92
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
