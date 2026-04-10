# Unique Session Naming and Stale Data Cleanup

The goal is to fix "ghost" sessions in the Crew Hub's "Live Near You" feed and implement more descriptive session naming to help users distinguish between current and legacy sessions.

## User Review Required

> [!IMPORTANT]
> **Naming Convention Choice**: I've selected `CrewName_M/D` (e.g., `O-Town_4/10`) as the default format. This ensures uniqueness across dates while remaining compact for mobile UI.

> [!WARNING]
> **Automatic Session Closure**: Creating a new session will now automatically set any previous active sessions by the same leader to `is_active: false`. This is a destructive cleanup prioritized for data integrity.

## Proposed Changes

---

### Crew Service & Lifecycle

#### [MODIFY] [CrewService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/CrewService.ts)
-   Add `cleanupLegacySessions(userId: string)` method to proactively deactivate old sessions.
-   Update `createSession` to invoke `cleanupLegacySessions` before inserting the new row.
-   Ensure `fetchPublicSessions` and `fetchActiveSessions` use identical filtering logic to prevent view-vs-table drift.

#### [MODIFY] [CrewModal.tsx](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/components/CrewModal.tsx)
-   Update `handleCreate` to generate the new `sessionName_Date` format.
-   Update `onSessionEnded` callback (and the local `handleEndSession`) to forcefully trigger a re-fetch of the `nearbySessions` list.
-   Ensure the "Live Near You" section in the UI clears stale entries immediately when `setIsLoadingNearby` is true.

---

### Location & Discovery

#### [MODIFY] [LocationService.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/LocationService.ts)
-   Audit `getNearbyPublicSessions` to ensure it strictly respects `is_active = true`.

## Open Questions

1.  **Date Format**: Does `M/D` (e.g., 4/10) work for you, or do you prefer a zero-padded version like `MM-DD`?
2.  **Leader Cleanup**: Should we also automatically "kick" all members from a session when the leader starts a new one?

## Verification Plan

### Automated Tests
-   Verify session name construction logic.
-   Mock Supabase response to ensure `cleanupLegacySessions` calls the correct update query.

### Manual Verification
1.  Start a session as Leader.
2.  Re-open the app (or modal) and start a *new* session without ending the first.
3.  Verify the first session is now `is_active = false` in the DB.
4.  Verify the new session appears as `CrewName_M/D` in the "Live Near You" list.
5.  End the session and verify it disappears instantly from the discovery list.
