# Implementation Plan: refactor/deep-dive-native-cloud

## Goal
Fix telemetry overwrites in WatchConnectivityManager, dropouts in WearMessageSender, and missing try/catch in Supabase Edge Functions.

## Source of Truth
`targets/watch/WatchConnectivityManager.swift`

## Proposed Changes

### NATIVE_&_WATCH
- **[MODIFY]** `targets/watch/WatchConnectivityManager.swift`: Update `updateApplicationContext` to buffer and append rather than blindly overwrite previous payloads.
- **[MODIFY]** `android/sk8lytzWear/.../WearMessageSender.kt`: Ensure MessageClient implements a local persistence buffer for health telemetry when phone is disconnected.

### CLOUD_FUNCTIONS
- **[MODIFY]** `supabase/functions/notify-crew-session/index.ts`: Add `try/catch` wrappers around the network fetch call.
- **[MODIFY]** `supabase/migrations/20260414_consolidate_telemetry.sql`: Add `ON DELETE CASCADE` for `user_id`.
- **[MODIFY]** `supabase/migrations/20260506000001_god_tier_telemetry.sql`: Remove direct `::INT` cast on JSON text that throws exceptions if float. Safely cast to numeric first.

## Verification Plan
1. **Automated Tests**: Run `npm run verify` to ensure TypeScript compilation succeeds.
2. **Manual Verification**: Check edge functions deployments for syntax errors and native watch extensions for compilation success.
