# Implementation Plan: sweep-hooks-useSkateStats.ts

## Goal
Fix static audit findings for the `sweep-hooks-useSkateStats.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useSkateStats.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useSkateStats.ts)
- **Line:** 23
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Promise.all call has no catch block. While underlying service methods currently catch internal exceptions, using Promise.all without a catch block is a code smell and could trigger unhandled rejections if service implementations change.
- **Suggested Fix:** Append a .catch() handler to the Promise.all chain to log any unhandled exceptions during stats load.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
