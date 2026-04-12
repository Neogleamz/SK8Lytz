# Plan: `feat/geofence-rink-sync`

### Design Decisions & Rationale
We use Expo's `expo-location` `startGeofencingAsync` API (already a project dependency) to define circular geofence regions around known rink coordinates stored in Supabase's `skate_spots` table. When the user enters a rink boundary, a background task fires to auto-trigger Crew Hub discovery. This is strictly opt-in with a permission prompt.

---

## Proposed Changes

### [MODIFY] Supabase: `skate_spots` table
- Add columns: `geofence_radius_m` (INT, default 150), `auto_discover` (BOOLEAN, default true).
- Apply Supabase migration.

### [NEW] `src/services/GeofenceService.ts`
- `registerRinkGeofences(spots: ISkateSpot[])` — registers top N nearby rinks as geofence regions.
- `onGeofenceEnter(spotId: string)` — fires a local push notification + triggers Crew Hub discovery.
- Uses `TaskManager.defineTask(GEOFENCE_TASK, ...)` for background processing.

### [MODIFY] `src/screens/DashboardScreen.tsx`
- On app launch, if location permission granted, call `GeofenceService.registerRinkGeofences()` with the 10 closest verified spots.
- Show a "🛼 Rink Detected" banner when geofence enters.

---

## Open Questions
- **Q:** Do we need user consent UI (permission gate) before registering geofences? Yes — required on both platforms.
- **Q:** Should geofence discovery be opt-in per-session or a persistent toggle in settings?

## Verification Plan
1. Mock a geofence boundary in the diagnostic lab using a fixed coordinate.
2. Simulate "entering" by faking a location update.
3. Confirm Crew Hub session discovery triggers automatically.
