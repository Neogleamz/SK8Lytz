# Fix Crew Deletion Session Abandonment

When a user deletes a permanent crew, any currently active sessions tied to that `crew_id` remain orphaned. Active members stay connected to the websocket channel, and the session remains `is_active = true` in the database, resulting in ghost sessions on the map.

## Proposed Changes

### `src/services/ProfileService.ts`
We will intercept the `deleteCrew` method to perform a clean teardown of associated sessions before deleting the crew record.

#### [MODIFY] `src/services/ProfileService.ts`
- In `deleteCrew(crewId)`:
  - Query the `crew_sessions` table for any `is_active = true` sessions where `crew_id = crewId`.
  - For each active session found:
    1. Temporarily subscribe to the `crew:${session.id}` Realtime channel.
    2. Broadcast a `session_ended` event to force all connected clients to gracefully disconnect and revert to solo mode.
    3. Execute an update on `crew_sessions` to set `is_active = false`, `status = 'ended'`, and `ended_at = now()`.
  - Proceed with the deletion of the crew from the `crews` table.

## User Review Required
No breaking database schema changes are required. The fix leverages the existing Supabase Realtime `session_ended` payload that `CrewService` already listens to, ensuring a smooth UX transition.

## Verification Plan
1. Launch the app and create a new Crew.
2. Start a live session for that Crew.
3. Open the Crew management tab and delete the Crew.
4. Verify that the app immediately transitions out of the live session state back to solo mode.
