# Chore: Optimize Supabase Logging Strategy

## Design Decisions & Rationale

The current Supabase schema contains redundant high-volume tables (`parsed_logs`, `device_diagnostics`) that capture full BLE hex payloads during normal operation. This results in excessive row counts (41k+) and potential performance degradation. I am proposing a "Tiered Logging" strategy where detailed diagnostics are strictly gated by a Lab-only feature flag, while production telemetry focuses purely on high-level events and critical errors.

## User Review Required

> [!IMPORTANT]
> This plan involves changing how data is stored in Supabase. We must determine if we need to migrate the existing 41k logs or if we can start fresh with a new schema.

## Proposed Changes

### [Database]

#### [MODIFY] Supabase Schema

- Consolidate `device_diagnostics` and `parsed_logs` into a single, highly-indexed `system_logs` table with a `log_level` or `category`.
- Implement a TTL (Time-To-Live) using pg_cron or similar for non-critical logs.

### [Frontend]

#### [MODIFY] [AppLogger.ts](file:///src/services/AppLogger.ts)

- Implement a `isDiagnosticsEnabled` check before calling `logToCloud`.
- Default detail level to `MINIMAL`.

#### [MODIFY] [AdminToolsModal.tsx](file:///src/components/AdminToolsModal.tsx)

- Add a "Diagnostics Mode" master switch to toggle detailed cloud logging.

## Open Questions

- Is there any legacy dependency on the exact structure of `parsed_logs` for any administrative dashboards outside the app?

## Verification Plan

### Automated Tests

- `npm test src/services/AppLogger.test.ts` (to be created): Ensure logging intensity shifts based on config.

### Manual Verification

- Toggle "Diagnostics Mode" in the app and verify Supabase row activity in the dashboard.
