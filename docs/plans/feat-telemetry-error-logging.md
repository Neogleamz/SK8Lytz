### Design Decisions & Rationale

We need a structured way to separate actual app-breaking crashes and BLE protocol failures from the massive flood of general telemetry logs. To do this, we will implement a mini "Sentry-like" error tracking system directly in Supabase. By pushing critical exceptions to a dedicated `telemetry_errors` table with a triage status (e.g., `NEW`, `TRIAGED`, `RESOLVED`), it allows me (the AI) to query the database, analyze the latest unhandled crashes, and automatically generate new Bucket List tickets to fix them natively.

## Proposed Changes

### 1. Database Schema (Supabase Migration)
We will execute a Supabase migration to create a `telemetry_errors` table:
- **Columns**: `id`, `created_at`, `session_id`, `event_type`, `error_message`, `stack_trace`, `raw_context` (JSONB), `status` (default: 'NEW').
- **RLS**: Authenticated users can insert their own errors.

### 2. `src/services/AppLogger.ts`
- Modify `uploadLogsToSupabase()` to scan the upload buffer specifically for error events (`ERROR_CAUGHT`, `PROTOCOL_ERROR`, `BLE_WRITE_ERROR`, `BLE_CONNECTION_ERROR`).
- Extract the message and stack trace from the payload and bulk-insert these critical items into the `telemetry_errors` table, alongside the standard telemetry push.

### 3. `App.tsx` (Global Crash Handlers)
- **Component Crashes**: Enhance the recently polished `SafeErrorBoundary` to actively pipe `error.message` and `errorInfo.componentStack` into `AppLogger.log('ERROR_CAUGHT')` and immediately trigger a cloud flush so the error isn't lost if the app terminates.
- **JS Thread Crashes**: Implement React Native's global `global.ErrorUtils.setGlobalHandler` to catch non-React async crashes, log them, and force an upload before the process dies.

## Triage Workflow Integration
Once this is built, you can simply tell me to "review system errors." I will read the `telemetry_errors` table, analyze the stack traces, add the bugs to the bucket list, and update the row status to `TRIAGED` automatically.
