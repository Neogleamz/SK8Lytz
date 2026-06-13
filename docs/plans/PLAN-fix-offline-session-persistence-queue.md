# Implementation Plan

## Goal
Add an AsyncStorage queue for skate sessions saved while offline/unauthenticated, and flush them to Supabase when auth becomes available.

## Source of Truth
- `src/services/SpeedTrackingService.ts`:108 — `if (!user) return null` confirms sessions are silently discarded when unauthenticated
- `src/services/ScenesService.ts`:334-383 — `flushSyncQueue()` structural template (read in full 2026-06-06)
- `src/hooks/cloud/useOfflineSyncWorker.ts`:21-28 — exact wiring point (`runSync` async function, L23)

## Steps

### Step 1 — Read SpeedTrackingService fully ✅ DONE
- Confirmed: `saveSession()` inserts to `skate_sessions`. 10 columns. Only `user_id` comes from `getUser()` — all others from `ISessionSnapshot` + computed `calories`.

### Step 2 — Read ScenesService flushSyncQueue pattern ✅ DONE
- Confirmed: queue stored as JSON array in AsyncStorage. Auth check via `getSession()` L342-346. Per-job try/catch with remainingQueue pattern. No in-progress guard (upsert is idempotent so not needed there — but INSERT is not, so we add one).

### Step 3 — Define PendingSessionRecord type and storage key
- Action: Add `export const PENDING_SESSION_QUEUE_KEY = '@SK8Lytz_PendingSession_Queue'` constant.
  Define `PendingSessionRecord` interface with fields:
  - All `ISessionSnapshot` fields spread inline (durationSec, distanceMiles, avgSpeedMph, peakSpeedMph, peakGForce, locationLabel?, crewSessionId?, healthBpm?, healthPeakBpm?, healthCalories?)
  - `calories: number` (pre-computed at queue time, NOT re-derived at flush)
  - `queued_at: string` (ISO timestamp)
  - NO `user_id` field — stamped at flush time from live `getSession()`
- Source: `src/services/SpeedTrackingService.ts`:28-39 (`ISessionSnapshot`), L110-128 (insert columns)
- Verify: `PendingSessionRecord` compiles with no `any` casts

### Step 4 — Queue session data when user is null
- Action: In `saveSession()`, replace `if (!user) return null` (L108) with:
  1. Read current queue from AsyncStorage (`PENDING_SESSION_QUEUE_KEY`)
  2. Soft cap: if `queue.length >= 50`, log `AppLogger.warn('[SpeedTrackingService] Pending queue at capacity (50+)')` — still push (do NOT discard)
  3. Compute `calories` (same logic as L110-112)
  4. Push `PendingSessionRecord` entry to queue array
  5. Write updated array to AsyncStorage
  6. Call `Alert.alert('Session Saved Locally', 'You\'re offline — this session will sync when you sign in.')` for user feedback
  7. Return `null`
- Source: `src/services/SpeedTrackingService.ts`:106-108
- Verify: Call `saveSession()` with mocked `getUser()` returning null → AsyncStorage contains 1-entry queue array

### Step 5 — Implement flushPendingSessionQueue()
- Action: Add `private _isFlushingSessionQueue = false` instance field to `SpeedTrackingServiceClass`.
  Add `async flushPendingSessionQueue(): Promise<void>` method:
  1. `if (this._isFlushingSessionQueue) return;` → `this._isFlushingSessionQueue = true;`
  2. Wrap body in `try { ... } finally { this._isFlushingSessionQueue = false; }`
  3. Read queue from AsyncStorage; if empty, return
  4. `const { data: sessionData } = await supabase.auth.getSession()` — if `!sessionData?.session?.user`, return (keep queue, mirror ScenesService L342-346)
  5. `const userId = sessionData.session.user.id`
  6. Loop queue: for each record, call `supabase.from('skate_sessions').insert({ user_id: userId, duration_sec: Math.round(r.durationSec), distance_miles: parseFloat(r.distanceMiles.toFixed(3)), avg_speed_mph: parseFloat(r.avgSpeedMph.toFixed(2)), peak_speed_mph: parseFloat(r.peakSpeedMph.toFixed(2)), peak_gforce: parseFloat(r.peakGForce.toFixed(2)), calories: r.calories, avg_bpm: r.healthBpm ?? null, peak_bpm: r.healthPeakBpm ?? null, location_label: r.locationLabel ?? null, crew_session_id: r.crewSessionId ?? null })`
  7. Success → skip from remainingQueue; failure → keep in remainingQueue
  8. Write `remainingQueue` back to AsyncStorage
  9. Log `AppLogger.info('[SpeedTrackingService] Flushed pending sessions', { successCount, remaining })`
- Source: `src/services/ScenesService.ts`:334-383 structural template
- Verify: Double-call returns immediately on second invocation; flushed rows appear in Supabase; failed rows stay in queue

### Step 6 — Wire into useOfflineSyncWorker 60s loop
- Action: In `src/hooks/cloud/useOfflineSyncWorker.ts`:
  1. Add import at top: `import { SpeedTrackingService } from '../../services/SpeedTrackingService';`
  2. Inside `runSync()` after L23 `ScenesService.flushSyncQueue()`, add: `await SpeedTrackingService.flushPendingSessionQueue();`
- Source: `src/hooks/cloud/useOfflineSyncWorker.ts`:1-4 (imports), L21-28 (runSync body)
- Verify: `npm run verify` passes clean; file compiles with new import

### Step 7 — Write Jest tests
- Action: Create `__tests__/services/SpeedTrackingService.offline.test.ts` with 4 test cases:
  - **Test A (queue):** Mock `getUser()` → null. Call `saveSession(mockSnapshot)`. Assert `AsyncStorage.setItem` called with `PENDING_SESSION_QUEUE_KEY` containing 1 entry with snapshot fields + `calories` + `queued_at`. Return value === null.
  - **Test B (flush happy path):** Pre-populate AsyncStorage with 1 pending record. Mock `getSession()` → valid session. Mock `supabase.from('skate_sessions').insert()` → `{ error: null }`. Call `flushPendingSessionQueue()`. Assert insert called once with `user_id` stamped from session. Assert AsyncStorage written with `[]`.
  - **Test C (flush re-entrancy guard):** Start `flushPendingSessionQueue()` without await, immediately call again. Assert insert called exactly once.
  - **Test D (flush no session):** Mock `getSession()` → no session. Call `flushPendingSessionQueue()`. Assert AsyncStorage unchanged (queue preserved).
- Verify: `npx jest SpeedTrackingService.offline` — all 4 green

## Design Decisions (Morgan — 2026-06-06)
- **No user_id in queue record**: Stamped at flush from live session. Queue user === flush user always.
- **Soft cap, never discard**: Warn at 50+ but push anyway. User telemetry > memory hygiene.
- **_isFlushingSessionQueue guard**: INSERT is non-idempotent. upsert-based ScenesService doesn't need this; we do.

## Out of Scope
- Session recording UI changes, Watch sync, HealthKit writes, history view changes, BLE layer
- Any BLE or device layer changes
