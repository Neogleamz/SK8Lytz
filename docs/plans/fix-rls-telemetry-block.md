# Implementation Plan - Fix RLS Telemetry Block

Resolve 403 Forbidden errors preventing telemetry ingestion into Supabase tables (`parsed_session_stats`, `device_diagnostics`, `telemetry_errors`, etc.).

## Design Decisions & Rationale

- **Explicit Role Grants**: We will explicitly grant `INSERT` permissions to both `anon` and `authenticated` roles for all telemetry tables. This eliminates the "Authenticated Paradox" where a logged-in user is forbidden from performing actions previously allowed for anonymous guests.
- **Service Security**: Since telemetry is "blind" data (non-sensitive system health), and we want 100% ingestion reliability, we will use permissive `INSERT` policies without requiring a `user_id` match for these specific tables, as they are used for global system health and diagnostic tracking.
- **Session Continuity**: Tables like `parsed_session_stats` require `UPDATE` (via `UPSERT`) to maintain session state. We will extend the policies to allow updates on these tables for both roles.

## Proposed Changes

### [Supabase Database]

#### [MODIFY] RLS Policies via Migration

We will apply a migration to fix the following tables:

1. **`device_diagnostics`**:
    - Update `allow_anon_insert` to include `{anon, authenticated}` roles.
    - Update `allow_anon_select` to include `{anon, authenticated}` roles (for local verification).
2. **`telemetry_errors`**:
    - Add `anon` support to the insert policy (currently only `authenticated`).
3. **`parsed_session_stats` / `parsed_session_devices`**:
    - Ensure both `anon` and `authenticated` roles have `INSERT` and `UPDATE` permissions.
4. **`parsed_logs`**, **`parsed_mode_usage`**, **`parsed_pattern_usage`**, **`parsed_color_usage`**, **`led_diagnostics`**:
    - Explicitly allow both roles for `INSERT`.

### [Documentation]

#### [MODIFY] [Master Reference](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/tools/SK8Lytz_App_Master_Reference.md)

- Update Section 2 (Database Schema & Permissions) with the new mandatory Telemetry RLS pattern.

## Verification Plan

### Automated Tests

- Execute a test script that attempts to insert dummy telemetry data into `device_diagnostics` and `telemetry_errors` using both an unauthenticated and (simulated) authenticated Supabase client.
- Monitor `get_logs('api')` to verify 201 Created and 204 No Content responses instead of 403 Forbidden.

### Manual Verification

- Launch the app locally.
- Trigger a hardware sync (e.g. by starting a scan or connecting to a device).
- Observe the console log for: `[Diagnostics] uploaded ... bytes for ...` successfully.
- Log in to the app and repeat the hardware sync to verify it still works when `authenticated`.
