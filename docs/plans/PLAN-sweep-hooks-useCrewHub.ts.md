# Implementation Plan: sweep-hooks-useCrewHub.ts

## Goal
Fix static audit findings for the `sweep-hooks-useCrewHub.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useCrewHub.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewHub.ts)
- **Line:** 75
- **Rule:** R-12
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** GPS/Session polling interval captures the React state directly in setInterval closures without useRef backings, risking stale closures.
- **Suggested Fix:** Wrap periodic polling callbacks in a mutable ref or ensure state variables accessed are derived safely.

### [MODIFY] [useCrewHub.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewHub.ts)
- **Line:** 142
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** GPS-gated discovery executes Location.getCurrentPositionAsync without any enclosing try-catch wrappers.
- **Suggested Fix:** Wrap Location API calls in try-catch blocks to prevent unhandled promise rejections on permission changes.

### [MODIFY] [useCrewHub.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewHub.ts)
- **Line:** 11
- **Rule:** R-24
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Radius preference key '@Sk8lytz_RadiusPreference' defined as a local constant instead of registering in central storageKeys.ts.
- **Suggested Fix:** Move key definition to storageKeys.ts and import it.

### [MODIFY] [useCrewHub.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewHub.ts)
- **Line:** 48
- **Rule:** R-26
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** The effect retrieves the current user's crew list asynchronously from Supabase. When dependencies (`visible`, `step`) change rapidly, multiple async fetches are triggered. With no cleanup/boolean guard to reject stale responses, resolving queries can race to update `myCrews` and `permanentCrews` state out of order, or write state after the component has unmounted.
- **Suggested Fix:** Declare `let active = true;` inside `useEffect`. Check `if (active)` in the `.then` callback before calling `setMyCrews` and `setPermanentCrews`. Return `() => { active = false; }` in the cleanup block.

### [MODIFY] [useCrewHub.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewHub.ts)
- **Line:** 59
- **Rule:** R-26
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** The effect loops through the user's crews and fetches crew members for display asynchronously. If dependencies change or the component unmounts while these fetches are in-flight, it will continue to call `setCrewMemberCounts` on an unmounted component or with stale values, causing memory leaks and state drift.
- **Suggested Fix:** Declare `let active = true;` inside the `useEffect`. Check `if (active)` in the `.then` callback before calling `setCrewMemberCounts`. Return `() => { active = false; }` in the cleanup block.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
