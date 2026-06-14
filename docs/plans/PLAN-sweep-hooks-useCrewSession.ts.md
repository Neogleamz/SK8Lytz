# Implementation Plan: sweep-hooks-useCrewSession.ts

## Goal
Fix static audit findings for the `sweep-hooks-useCrewSession.ts` domain cluster.

## Proposed Changes

### [MODIFY] [useCrewSession.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewSession.ts)
- **Line:** 70
- **Rule:** R-05, R-21
- **Severity:** HIGH | **Confidence:** CONFIRMED
- **Description:** Direct Supabase database queries and updates to the user_profiles table for lifetime_top_speed_mph and lifetime_distance_miles are implemented independently in two locations: once inside useCrewSession.ts (triggered when ending a crew session) and twice inside SpeedTrackingService.ts (once inside saveSession and once inside flushPendingSessionQueue). This creates a risk of race conditions, inconsistent calculation patterns (e.g. useCrewSession does not use toFixed/parseFloat formatting for new coordinates, causing potential decimal precision drift, whereas SpeedTrackingService does), and violates the single source of truth principle for data mutations.
- **Suggested Fix:** Consolidate the profile stats update logic into a single method (e.g., profileService.updateLifetimeStats(...) or SpeedTrackingService.updateLifetimeStats(...)) and call it from both useCrewSession.ts and SpeedTrackingService.ts. This will unify the calculation schema, decimal formatting, and database operation.

### [MODIFY] [useCrewSession.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useCrewSession.ts)
- **Line:** 79
- **Rule:** R-11
- **Severity:** MEDIUM | **Confidence:** CONFIRMED
- **Description:** Unawaited floating promise for storage/network IO without chained .catch: "supabase.from('user_profiles').update({
                 lifetime_distance_miles: newDistance,
                 lifetime_top_speed_mph: newTopSpeed
               }).eq('user_id', user.id).then(({ error }) => {
                 if (error) AppLogger.warn('[useCrewSession] Telemetry sync failed', { error: error.message, payload_size: 0, ssi: 0 });
               }, (e: Error) => {
                 AppLogger.warn('[useCrewSession] Telemetry sync exception', { error: e.message, payload_size: 0, ssi: 0 });
               })"
- **Suggested Fix:** Append .catch() or await/wrap with try-catch.

## Verification Plan
- Run `npm run verify` to ensure AST and Type checking passes.
- Verify wave boundaries are respected.
