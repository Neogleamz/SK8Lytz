# Implementation Plan: sweep-hooks-useProductManager.ts

## Goal
Fix static audit findings for the `sweep-hooks-useProductManager.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useProductManager.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useProductManager.ts)
- **Line:** 62
- **Rule:** R-12
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** saveProduct uses reactive state 'session' inside the async callback but does not include 'session' in the useCallback dependency array. This can result in admin operations executing with stale session states or failing validation checks incorrectly.
- **Suggested Fix:** Add 'session' to the dependency array of saveProduct.

### [MODIFY] [useProductManager.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useProductManager.ts)
- **Line:** 86
- **Rule:** R-06
- **Severity:** LOW | **Confidence:** PROBABLE
- **Description:** AppLogger.error is called with a string unwrapped message (e.message) instead of the raw error instance, discarding stack trace.
- **Suggested Fix:** Pass the raw error object 'err' as the second parameter to AppLogger.error.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
