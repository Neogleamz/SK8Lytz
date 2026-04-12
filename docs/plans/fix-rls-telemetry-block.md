# Implementation Plan - Fix RLS Telemetry Block

Resolve recurring 403 Forbidden errors on `parsed_session_stats`, `device_diagnostics`, and `parsed_session_devices` tables.

## Proposed Changes

### Audit RLS Policies
- Use Supabase MCP `execute_sql` to check existing policies.
- Ensure `INSERT` is allowed for `authenticated` users where `user_id = auth.uid()`.

### Code Synchronization
- Check `AppLogger.ts` and `useRegistration.ts` to ensure the `user_id` being sent matches the authenticated user's ID.

## Verification Plan
- Monitor Supabase logs after deployment to verify 403 errors cease.
