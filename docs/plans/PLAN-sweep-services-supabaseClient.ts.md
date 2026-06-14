# Implementation Plan: sweep-services-supabaseClient.ts

## Goal
Fix static audit findings for the `sweep-services-supabaseClient.ts` domain cluster.

## Proposed Changes

### [MODIFY] [supabaseClient.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/supabaseClient.ts)
- **Line:** 31
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Bypassing AppLogger telemetry context by using raw console.warn.
- **Suggested Fix:** Replace with AppLogger.warn and provide required telemetry context.

### [MODIFY] [supabaseClient.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/supabaseClient.ts)
- **Line:** 44
- **Rule:** R-04
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Bypassing AppLogger telemetry context by using raw console.warn.
- **Suggested Fix:** Replace with AppLogger.warn and provide required telemetry context.

### [MODIFY] [supabaseClient.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/supabaseClient.ts)
- **Line:** 85
- **Rule:** R-08
- **Severity:** MEDIUM | **Confidence:** PROBABLE
- **Description:** Type safety bypass. The offline client mock is cast as SupabaseClient<Database>, concealing the fact that it is a partially implemented stub.
- **Suggested Fix:** Use a clear wrapper interface/service to handle client abstraction instead of casting an incomplete mock directly to the official client type.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
