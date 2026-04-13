# [PLAN] chore/redesign-parsed-data-storage

## Design Decisions & Rationale

Currently, our telemetry pipeline is aggressively pushing massive amounts of raw BLE notifications and redundant parsed states (`parsed_*`) to Supabase. This creates two problems:
1. **Cloud Cost Inflation:** Constant writes and un-optimized tables drain the database budget quickly.
2. **Data Duplication:** We are storing the same data multiple times (e.g., both raw and parsed arrays spread out rather than unified).

Our strategy must shift from a "Firehose Logging" model to a "Constraint-Based Auditing" model.

We will achieve this by:
1. **Telemetry Batching**: Instead of un-gated writes, we will spool non-critical telemetry into local memory and batch upload them (e.g., once every 5 minutes) via a single REST call.
2. **Schema Redesign**: Consolidate `device_diagnostics` and any `parsed_*` tables into a single tightly-structured JSONB table (`telemetry_snapshots`) that efficiently packs relevant state changes mapped to devices, ignoring redundant 'no-change' polling ticks.
3. **Lab-Only Restrictions**: Heavily detailed raw byte arrays will only be uploaded if the user is explicitly opted-in via the Diagnostics Lab. Normal users will only upload crash metrics and initial config registers.

---

## Proposed Changes

### [NEW] `src/services/TelemetryBatcher.ts`
- Implement an in-memory queue that collects mapped telemetry objects.
- Implement an interval flush (e.g., every 60 seconds) that executes a single `.insert()` array into Supabase instead of dozens of single row inserts.

### [MODIFY] `src/hooks/useHardwareNotifications.ts`
- Redirect all Supabase logging calls from direct `supabase.from()` to `TelemetryBatcher.enqueue()`.
- Add local delta-checking so telemetry is ONLY enqueued when actual values change (color mode shift, point mapping change, unhandled error).

### [MODIFY] Supabase Schema (via MCP migration)
- Deprecate old high-volume tables.
- Create single `telemetry_snapshots` table utilizing Postgres `JSONB` to compactly store multi-variable diagnostic info in a single column to save index overhead.

---

## Verification Plan

### Manual Verification
1. Connect hardware and engage the application for 10 minutes.
2. Monitor terminal for batch events. Ensure we see only 1 network request per minute for telemetry rather than 100+.
3. Check Supabase dashboard to verify table row count increases efficiently and logically.
