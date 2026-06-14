# Implementation Plan: sweep-hooks-useTelemetryLedger.ts

## Goal
Fix static audit findings for the `sweep-hooks-useTelemetryLedger.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useTelemetryLedger.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useTelemetryLedger.ts)
- **Line:** 47
- **Rule:** R-26
- **Severity:** HIGH | **Confidence:** PROBABLE
- **Description:** useTelemetryLedger hook maintains separate refs for flushing lock and payload buffer per instance, but shares a single global AsyncStorage telemetry buffer key. When backgrounding, multiple active hook instances concurrently trigger flushes, causing duplicate telemetry entries to be sent to Supabase.
- **Suggested Fix:** Centralize the telemetry queue and flushing in a single React context provider, or use module-level globals rather than local component refs for the buffer and flushing state.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
