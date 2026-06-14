# Implementation Plan: sweep-utils-migrateAuthTokens.ts

## Goal
Fix static audit findings for the `sweep-utils-migrateAuthTokens.ts` domain cluster.

## Proposed Changes

### [MODIFY] [migrateAuthTokens.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/utils/migrateAuthTokens.ts)
- **Line:** 24
- **Rule:** R-06
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** Passes raw error object directly to AppLogger.error. While AppLogger.error performs standard unwrapping internally, call-site unwrapping is preferred for consistency and safety if AppLogger.error API changes.
- **Suggested Fix:** AppLogger.error('...', e instanceof Error ? e : new Error(String(e)));

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
