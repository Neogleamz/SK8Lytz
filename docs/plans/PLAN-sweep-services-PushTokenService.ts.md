# Implementation Plan: sweep-services-PushTokenService.ts

## Goal
Fix static audit findings for the `sweep-services-PushTokenService.ts` domain cluster.

## Proposed Changes

### [MODIFY] [PushTokenService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/PushTokenService.ts)
- **Line:** 24
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Swallowing database error on push token registration. The query output { error } is not destructured or checked. Upsert failures are swallowed, and the catch block does not run, failing to log critical token registration failures to AppLogger.
- **Suggested Fix:** Destructure the result, check for database errors, and throw them if present: const { error } = await supabase.from(...); if (error) throw error;

### [MODIFY] [PushTokenService.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/PushTokenService.ts)
- **Line:** 43
- **Rule:** R-11
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** Swallowing database error on push token deletion during unregistration. The delete query does not check returned { error }, leaving database failures completely unmonitored.
- **Suggested Fix:** Destructure and check { error } from the delete query, throwing on error to let the catch block handle logging: const { error } = await supabase.from(...); if (error) throw error;

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
