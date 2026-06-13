# Redesign Parsed Data Storage (Telemetry Consolidation)

This plan outlines the refactoring of the SK8Lytz telemetry ingestion pipeline to transition from an un-gated firehose and fragmented relational tables into a unified JSONB-backed constraint model.

## User Review Required

> [!WARNING]
> This plan proposes **dropping the legacy `parsed_*` tables** (e.g., `parsed_logs`, `parsed_mode_usage`, `parsed_color_usage`) and replacing them with a single `telemetry_snapshots` JSONB table to save cloud costs. I need your confirmation that we don't have external analytics dashboards hard-locked to those specific SQL tables before we migrate. 

## Proposed Changes

---

### Supabase Database Migration

Execute a SQL migration using the MCP `apply_migration` tool to establish the new unified schema:

#### [NEW] `telemetry_snapshots` Table
- Create table `telemetry_snapshots` (id UUID, created_at TIMESTAMPTZ, session_id TEXT, device_id TEXT, event_type TEXT, metadata JSONB).
- Implement a GIN Index `CREATE INDEX idx_telemetry_snapshots_metadata ON telemetry_snapshots USING GIN (metadata);` to ensure lightning-fast querying on deeply nested `ic_type` or `ble_rx_time` properties.
- Enable RLS and add a secure insert policy for the `service_role` and `anon` keys.

---

### AppLogger (The VIP Fast-Lane & Batcher)

Modify the core telemetry ingestion service to use the bounded JS-memory and `AsyncStorage` disk limits alongside the VIP fast-lane.

#### [MODIFY] `src/services/AppLogger.ts`
- **VIP Fast-Lane Implementation**: Intercept `['ERROR_CAUGHT', 'PROTOCOL_ERROR', 'BLE_WRITE_ERROR', 'BLE_CONNECTION_ERROR', 'CREW_ERROR']` in the `log()` function. Fire an immediate asynchronous `supabase.from('telemetry_errors').insert(...)` bypassing the queue entirely so crashes are never lost.
- **Unified Snapshot Push**: Rewriting `uploadLogsToSupabase()` to map the `this.buffer` directly into the new `telemetry_snapshots` JSONB structure in chunked batch inserts.
- **Storage Abolition**: Remove the massive computational overhead of downloading, parsing, and re-uploading JSON blobs to the `sk8lytz-logs` Supabase Storage Bucket. The Postgres JSONB table handles this natively now.

---

### Firehose Mitigation (Diagnostic Lab)

Target the un-gated Supabase database inserts that are flooding the network during diagnostics.

#### [MODIFY] `src/hooks/useHardwareNotifications.ts`
- Delete the direct `supabase.from('device_diagnostics').insert(...)` block inside the BLE receiver loop.
- Replace it with `AppLogger.log('DIAGNOSTIC_PACKET', { ...parsedData })` to effectively spool it through the new batched architecture.

## Open Questions

> [!IMPORTANT]  
> 1. Do we need to retain the historical data located in `sk8lytz-logs` Storage buckets, or can we safely ignore the legacy blobs now that we are migrating to Postgres JSONB natively?
> 2. Are we authorized to DROP all `parsed_*` tables during this migration rollout to enforce schema cleanliness?

## Verification Plan

### Automated Tests
1. Generate the TypeScript Definitions via MCP from Supabase to guarantee parity with the new schema.
2. Compile via `npx tsc --noEmit` to ensure no orphaned relational inserts are hanging in the codebase.

### Manual Verification
1. I will execute an isolated crash test (simulating a `try/catch` UI exception) and verify the `telemetry_errors` table receives the VIP un-gated insert.
2. I will trigger the `uploadLogsToSupabase()` batcher and verify `telemetry_snapshots` cleanly inserts the JSONB records.
