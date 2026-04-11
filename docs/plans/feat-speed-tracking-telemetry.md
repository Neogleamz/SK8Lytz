# Speed & Telemetry Tracking (feat/speed-tracking-telemetry)

This plan integrates real-time distance and speed aggregation, fulfilling the request to store and display telemetry data for Street Mode. We are adhering to your constraints: Database persistence, sync on "End Session", UI updates in DockedController & Summary, and strict tracking ONLY when Street Mode is active.

## User Review Required

Since you appended "proceed" to your answers, I have designed this plan to execute immediately upon your review. Below is the blueprint.

## Proposed Changes

### Database & Schema (Supabase)

We need to persist the telemetry. We will write and run a migration to add these columns safely.

#### [MODIFY] `supabase_db`
- **Migration `add_telemetry_columns`**:
  ```sql
  ALTER TABLE crew_sessions ADD COLUMN top_speed_mph numeric DEFAULT 0;
  ALTER TABLE crew_sessions ADD COLUMN avg_speed_mph numeric DEFAULT 0;
  ALTER TABLE crew_sessions ADD COLUMN total_distance_miles numeric DEFAULT 0;

  ALTER TABLE user_profiles ADD COLUMN lifetime_top_speed_mph numeric DEFAULT 0;
  ALTER TABLE user_profiles ADD COLUMN lifetime_distance_miles numeric DEFAULT 0;
  ```
- **Sync**: After migration, run `npx supabase gen types...` to keep our TypeScript `.ts` local shapes in sync.

---

### Location Math & Data Storage 

We will handle the exact calculation logic client-side and only upload to Supabase when the user triggers the end action.

#### [MODIFY] `src/components/DockedController.tsx`
- **State Additions**: `streetDistanceRef`, `streetTopSpeedRef`, `streetSpeedSamplesRef`
- **Math Hook**: Inside the `Location.watchPositionAsync` callback:
  - **Only if `isStreetMode === true`**:
    - Update max speed `Math.max(streetTopSpeedRef.current, spdMph)`.
    - Accumulate distance `streetDistanceRef.current += (spdMph / 3600)`. (Since our watcher is 1s `timeInterval`).
    - Push sample to array for Average Speed.
- **UI Element**: Below the `AnalogGauge`, add two subtle text elements showing the runtime `Distance` and `Top Speed`. 
- **Context Output**: When "End Session" or "Street Mode Off" occurs, the variables are preserved in the globally accessible `useCrewStore` or `Session` bounds so they can be shown on the Summary modal.

#### [MODIFY] `src/hooks/useCrewStore.ts` (or equivalent context)
- Add global state `sessionTelemetry` to temporarily store the accumulated stats across the React tree.

---

### Dashboard Modal (Post-Session Summary)

Per constraint #3, we will add a summary screen when a session ends.

#### [MODIFY] `src/components/CrewModal.tsx`
- **End Session Flow**: When the Leader presses "End Session":
  - Call a new Supabase RPC or direct `update` via `CrewService` to save `top_speed_mph`, `avg_speed_mph`, and `total_distance_miles` to the `crew_sessions` table based on the `sessionTelemetry` store.
  - Show a small modal "Session Complete" reflecting the stats (Distance, Top Speed, Avg Speed) as a beautiful recap instead of immediately hiding the Crew Hub.
- **Profile Accumulation**: Update the user's `lifetime_` stats in `user_profiles` seamlessly behind the scenes.

## Verification Plan

### Automated Tests
- Run database lints to confirm valid index/column schema.

### Manual Verification
1. I will boot virtual street mode logic or ask you to physically trigger GPS.
2. We verify the `DockedController` text increments the Distance and Top Speed correctly.
3. You will tap "End Session", observing the new Post Session Summary Modal.
4. Verify Supabase tables (`crew_sessions` and `user_profiles`) updated safely.
