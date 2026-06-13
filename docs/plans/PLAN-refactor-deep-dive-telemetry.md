# Implementation Plan: refactor/deep-dive-telemetry

## Goal
Fix offline telemetry drops in `DATA_LAYER`, `SESSION_TRACKING`, and `AppLogger.ts`.

## Source of Truth
`src/services/AppLogger.ts`

## Proposed Changes

### src/services/AppLogger.ts
- **[MODIFY]**: Wrap all `AsyncStorage.setItem`, `AsyncStorage.multiRemove`, and `supabase.rpc` calls in `try/catch` blocks.
- **[MODIFY]**: Ensure the 10-event/2000ms debounce buffer pushes to `AsyncStorage` if `supabase` fails.
- **[MODIFY]**: Prevent unconditional buffer clearing after Supabase push; only clear upon success.

### src/hooks/useSessionTracking.ts
- **[MODIFY]**: Update `_syncTelemetry` to utilize the resilient offline buffer in `AppLogger`. Ensure `AsyncStorage` operations have `.catch()` wrappers.

## Verification Plan
1. **Automated Tests**: Run `npm run verify` to ensure TypeScript compilation succeeds.
2. **Manual Verification**: Disable network on device, generate telemetry events, re-enable network, and verify events sync properly to Supabase without data loss.
