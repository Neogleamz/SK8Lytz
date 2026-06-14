# Implementation Plan: sweep-components-CrewModal.tsx

## Goal
Fix static audit findings for the `sweep-components-CrewModal.tsx` domain cluster.

## Proposed Changes

### [MODIFY] [CrewModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CrewModal.tsx)
- **Line:** 29
- **Rule:** R-08
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Uses raw 'any' type inside the CrewModalProps definition for the lastScene parameter.
- **Suggested Fix:** Replace Record<string, any> with Record<string, unknown> or a defined Scene type constraint.

### [MODIFY] [CrewModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CrewModal.tsx)
- **Line:** 40
- **Rule:** R-18
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Relies on multiple scattered boolean flags instead of a unified finite state machine state union for route and screen changes.
- **Suggested Fix:** Define a clean state union (e.g. 'landing' | 'controller' | 'join' | 'create' | 'schedule' | 'detail') and use it in a structured state machine.

### [MODIFY] [CrewModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CrewModal.tsx)
- **Line:** 100
- **Rule:** R-26
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Queries the database for user profile details when the `user` prop changes. If the user object changes rapidly or the component unmounts, the callback updates `setDisplayName` on an unmounted component or with stale data out of order.
- **Suggested Fix:** Declare `let active = true;` inside `useEffect`, check `if (active) setDisplayName(...)`, and return a cleanup function setting `active = false;`.

### [MODIFY] [CrewModal.tsx](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CrewModal.tsx)
- **Line:** 105
- **Rule:** R-04
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** Error/Warning logged via AppLogger without telemetry context (missing payload_size and/or ssi).
- **Suggested Fix:** Add payload_size and ssi properties (e.g., payload_size: 0, ssi: 0 or dynamic values) to the log context object.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
