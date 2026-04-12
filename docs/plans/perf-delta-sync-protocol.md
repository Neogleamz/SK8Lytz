# [PLAN] perf/delta-sync-protocol (The Delta Standard)

### Design Decisions & Rationale
To ensure the Crew Hub and high-density telemetry remain fast even with 50+ concurrent users, we are implementing **Differential Data Sync**. Instead of monolithic fetch requests, the app will track its `last_sync_timestamp` and only request "Deltas" (changes since then). This significantly reduces payload sizes and battery consumption.

## Proposed Changes

### [Component Name] Sync Orchestrator

#### [MODIFY] [SupabaseService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/SupabaseService.ts)
- Add `since` parameter to all primary fetch methods (`fetchCrews`, `fetchDevices`, `fetchSessions`).
- Update queries to use `.gt('updated_at', lastSyncTime)`.

### [Component Name] Database Evolution

#### [MODIFY] [Supabase Migration]
- Ensure every synced table (`registered_devices`, `skate_sessions`, `crew_sessions`) has a triggered `updated_at` column.

## Verification Plan
1. **Payload Audit**: Monitor network traffic in the Admin Hub; verify that the second fetch of the Dashboard data is significantly smaller than the first.
2. **Conflict Test**: Modify a device on one phone and verify it syncs to the second phone via the Delta loop without refreshing the entire list.
3. **Stale Data Cleanup**: Ensure the Delta loop correctly handles "Hard Deletes" (tombstoning deleted rows in the DB so the client knows to remove them locally).
