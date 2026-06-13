# [CHORE] Telemetry Efficiency & Deduplication Audit

## Design Decisions & Rationale
After inspecting the database schema and `AppLogger.ts` Native Postgres Insertion stream, several critical findings contradict the initial assumptions of the stub plan:

1.  **No `skate_sessions` overlap**: `skate_sessions` is a domain-driven fitness tracking table (workout duration, miles skated), whereas `parsed_session_stats` stores hardware telemetry (RAM usage, battery level, `os_build_id`). They serve entirely divergent purposes and do not overlap.
2.  **No `hex_payload` redundancy**: The `raw_data` JSONB payload in `parsed_logs` already strips `hex` and `dir` prior to insertion via `(({ dir: _d, hex: _h, deviceId: _di, ...r }) => r)(item.d || {})`. It is already optimized!
3.  **The True Duplication Culprit**: `AppLogger.ts` uses `.flatMap()` during its Postgres injection stream. If a `MODE_CHANGED` event is broadcast to a "**Group of 4 Devices**", it physically creates 4 identical database rows in `parsed_logs` (and usage tables) varying only by `device_id`.
4.  **Legacy Script Misnomer**: The `tools/ingestPicks.mjs` script doesn't actually ingest picks. It iterates `<SUPABASE_URL>/storage/v1/object/list/sk8lytz-logs` to sync older log files left in cloud storage buckets before native Postgres insertion was enabled.

This implementation plan focuses relentlessly on eliminating the `.flatMap()` duplication array unrolling, changing ingestion queries to rely on the established `group_id` instead, and refactoring the offline sync fallback.

> [!WARNING] User Review Required
> Modifying the telemetry array unrolling to insert `device_id: null` in favor of `group_id` means that future Supabase dashboard queries searching for a single device will need to query `WHERE device_id = 'A' OR group_id LIKE '%A%'`. Is this query compromise acceptable to reduce database bloat by over 60%+ for group skaters?

## Proposed Changes

### `src/services/AppLogger.ts`
- **[MODIFY]** Remove the `targets.map(...)` unrolling for `parsed_logs`.
- **[MODIFY]** Store Group events as a **single** row: `device_id` will be assigned `null` (or the primary host MAC if logical), while `group_id` / `group_name` retains the group identifier.
- **[MODIFY]** Apply the same unrolling removal to the `parsed_mode_usage`, `parsed_color_usage`, and `parsed_pattern_usage` tables, preventing multi-insertion of usage aggregations.

### `tools/ingestPicks.mjs`
- **[DELETE]** `tools/ingestPicks.mjs` 
- **[NEW]** Rename to `tools/syncTelemetryBuckets.mjs` to reflect its true purpose.
- **[MODIFY]** Update the batch processing in this script to reflect the new non-duplicate logic matching `AppLogger.ts`.

## Open Questions

1. Do we still actively run the bucket sync script via cron/GitHub Actions, or is the `AppLogger.ts` live Postgres stream fully replacing the need for it? Can we just delete the script entirely?

## Verification Plan

### Automated / Diagnostic Tests
- Boot the app on physical device or emulator.
- Group 2 devices together.
- Invoke a `MODE_CHANGED` event on the group.
- Using the `supabase-mcp-server_execute_sql` tool, query `SELECT * FROM parsed_logs ORDER BY created_at DESC LIMIT 5` and verify exactly 1 row was created for the group interaction rather than 2+.
