# Crew Subsystem E2E Audit

> Analyst: Reyes (Scout)
> Date: 2026-06-22
> Scope: 7 flows end-to-end (UI action -> service -> Supabase -> realtime -> back to UI)
> Prior artifact: `docs/analysis/crew-broadcast-scene-redundancy.md` — treat as known. Not re-derived here.
> Active repair plan: `docs/plans/PLAN-fix-crew-broadcast-scene.md` — covers flows 3 (member receive side) and implicitly 5 (rejoin scene restore).

---

## Verdict Table

| # | Flow | Status | One-line Finding | Key Evidence |
|---|------|--------|-----------------|--------------|
| 1 | Create crew / session | WORKING | Session persists to DB, invite_code generated server-side, role set, storage written, emit fires. One gap: `subscribeAsLeader` is NOT called on creation — requires the CrewModal `onSessionReady` path, which works when the modal is open. | `CrewSessionManager.ts:17-67`, `CrewService.ts:118-128`, `DashboardCrewPanel.tsx:109-130` |
| 2 | Scheduled crews | BROKEN | `scheduled_at` is stored correctly but there is zero activation mechanism — no push notification, no cron flip from `status:'scheduled'` to `'active'`, no local timer. The UI subtitle promises "a 15-min reminder" — that promise is never fulfilled. The feature is cosmetic persistence only. | `CrewScheduleScreen.tsx:57-59`, `CrewScheduleScreen.tsx:83` |
| 3 | Join session | WORKING (partial) | `joinSession` (invite code) and `joinSessionById` both write a membership row and set service state. `handleSessionJoined` loads members and calls `onSessionReady`. `onSessionReady` in `DashboardCrewPanel` wires `subscribeAsMember` with the scene callback. SUSPECT: the scene callback routes to `applyCloudScene` not `applyCrewPayload` — covered by PLAN-fix-crew-broadcast-scene.md. | `CrewSessionManager.ts:115-161`, `CrewJoinScreen.tsx:58-70`, `DashboardCrewPanel.tsx:116-129` |
| 4 | Membership / presence | BROKEN (two independent bugs) | (a) `member_update` event has NO sender anywhere in the source tree — leader's member list never updates reactively. (b) `subscribeAsLeader` callback in `DashboardCrewPanel:115` is `() => {}` — even if a sender existed, the leader UI would not react. Leader sees only the snapshot loaded at `handleSessionJoined`. Additionally, `CrewMemberDashboard` queries `crew_members.role` (not in schema) and joins `user_profiles` (no FK declared) — both silently return null. | `CrewRealtime.ts:38` (sole receiver, zero senders in codebase), `DashboardCrewPanel.tsx:115`, `CrewMemberDashboard.tsx:202-208`, `supabase.ts:107-144` |
| 5 | Session restore / auto-rejoin | SUSPECT | `tryAutoRejoin` correctly reads AsyncStorage, checks DB liveness, re-upserts membership, sets service state, and calls `emit()`. `useDashboardCrew` then calls `subscribeAsLeader`/`subscribeAsMember`. Member path: applies `fetchLastScene` result via `onApplySceneRef.current(lastScene)` — but `lastScene` is a `number[]` (stored by `_persistLastPayload`) and `onApplyScene` routes it to `applyCloudScene` which expects `CloudScenePayload`. This is the same receiver type-mismatch already covered by PLAN-fix-crew-broadcast-scene.md Step 14. The rejoin storage/DB path itself is correct. | `CrewAutoRejoin.ts:19-83`, `useDashboardCrew.ts:67-124`, `DashboardScreen.tsx:355-357` |
| 6 | End / leave session | WORKING | `endSession` updates DB, broadcasts `session_ended`, deletes members (fire-and-forget), nulls service state, removes AsyncStorage, defers `removeChannel` by 600ms. Member `session_ended` receiver at `subscribeAsMember` unsubscribes, clears state, fires `onSessionEnded`. `leaveSession` deletes the membership row and calls `unsubscribe()`. Channel and heartbeat timers are cleaned up by `unsubscribeRealtime()`. | `CrewSessionManager.ts:304-400`, `CrewSessionManager.ts:417-441`, `CrewRealtime.ts:61-78`, `CrewService.ts:113-116` |
| 7 | Heartbeat / liveness | SUSPECT (write-only) | `startHeartbeat` fires every 60s writing `updated_at` to `crew_sessions`. It IS started on `subscribeAsLeader` (L34) — so it runs for the leader after both initial join and auto-rejoin. However, NOTHING in the app-side source reads `updated_at` for expiry detection. Expiry enforcement relies entirely on `expires_at` (a DB-default column) and the `cleanupExpiredSessions` method which is called on `fetchActiveSessions`. The heartbeat's `updated_at` touch may extend a server-side cron window (not visible in local migrations) but produces zero observable effect in the source tree as audited. | `CrewRealtime.ts:112-124`, `CrewSessionManager.ts:91-113` (reads `expires_at`, not `updated_at`), `useCrewHub.ts:168` (no `updatedSince` arg passed) |

---

## Detailed Findings

### Flow 2 — Scheduled Crews: Cosmetic Persistence, No Activation

**Class of bug:** Stub / deferred feature never implemented.

**What the UI promises:**
`CrewScheduleScreen.tsx:83` renders: "Your crew gets a push notification immediately and a 15-min reminder."

**What actually happens:**
`CrewScheduleScreen.tsx:handleCreate` (L27-69) calls `crewService.createSession` with `opts.scheduledAt = scheduled.toISOString()`. The `createSession` implementation at `CrewSessionManager.ts:35` correctly stores `scheduled_at` in the DB row and sets `status: 'scheduled'`. After the insert, the code at `CrewScheduleScreen.tsx:57-59` reads:

```ts
if (scheduled && scheduled > new Date()) {
  // Optional notifications logic can be added later
  setStep('landing');
```

There is no push notification call. There is no local timer to flip `status` from `'scheduled'` to `'active'`. There is no Supabase Edge Function or DB trigger in any local migration. The scheduled session sits in the DB indefinitely with `status: 'scheduled'` and `is_active: false` (the default before activation). `cleanupExpiredSessions` will eventually mark it `is_active: false` / `status: 'ended'` once `expires_at` passes — without it ever activating.

**Evidence:**
- `CrewScheduleScreen.tsx:57-59` — explicit "add later" comment
- `CrewSessionManager.ts:31` — `status: opts?.scheduledAt ? 'scheduled' : 'active'` — row created with `status:'scheduled'`
- `CrewSessionManager.ts:102` — `cleanupExpiredSessions` only acts on `is_active: true` rows — a `status:'scheduled'` row may never even be cleaned up correctly
- No migration in `supabase/migrations/` creates a trigger or Edge Function for schedule activation
- No push notification service call exists in any crew file

**Runtime implication:** A user who schedules a session and shares the invite code will have members attempting to join via `joinSession` which queries `.eq('is_active', true)` — the scheduled session has `is_active` defaulting to `false` (or whatever the DB default is), so members will receive "Crew not found or session expired."

**Confidence: VERIFIED** — all claims backed by file:line above. The only UNVERIFIED element is whether `is_active` defaults to `false` in the DB for new inserts (not in local migrations; supabase.ts Insert shows `is_active?: boolean` = optional). Runtime behavior of scheduled joins depends on this default.

---

### Flow 4a — member_update: No Sender Exists

**Class of bug:** Dead subscriber — event receiver wired but transmitter never built.

`CrewRealtime.ts:38` subscribes to `{ event: 'member_update' }`. A ripgrep of the entire `src/` tree for `member_update` returns exactly one hit: this subscriber. There is no call anywhere in the source to:

```ts
this.service.channel?.send({ type: 'broadcast', event: 'member_update', ... })
```

The intended trigger would be: when a member joins (`joinSession`/`joinSessionById`), broadcast `member_update` on the crew channel so the leader refreshes. `joinSession` and `joinSessionById` in `CrewSessionManager.ts` do not send any broadcast after the upsert. The leader's member list is therefore a static snapshot from `handleSessionJoined` and only updates when the user manually navigates away and back.

**Evidence:** `CrewRealtime.ts:38` (sole mention of `member_update` in source). `CrewSessionManager.ts:115-161` — no channel send after upsert.

---

### Flow 4b — subscribeAsLeader: No-Op Callback

**Class of bug:** Callback wired to `() => {}` — even if a sender existed, the leader UI would not respond.

`DashboardCrewPanel.tsx:115`:
```ts
crewUnsubRef.current = crewService.subscribeAsLeader(session.id, () => {});
```

`subscribeAsLeader` at `CrewRealtime.ts:29-44` passes `onMemberChange` to the `member_update` handler. With `() => {}`, no UI update fires. The correct callback should call `loadMembers` (available in `useCrewSession`) to refresh the member list state.

`useDashboardCrew.ts:80` has the same issue:
```ts
const u = await crewService.subscribeAsLeader(session.id, () => {});
```

Both callsites are no-ops. Even if a sender is added (fixing 4a), the leader UI still will not update until this callback is fixed.

**Evidence:** `DashboardCrewPanel.tsx:115`, `useDashboardCrew.ts:80`, `CrewRealtime.ts:38-40`.

---

### Flow 4c — CrewMemberDashboard: Schema Drift (role column + user_profiles join)

**Class of bug:** Column queried in application does not exist in `supabase.ts` schema; FK for join not declared.

`CrewMemberDashboard.tsx:202-208` runs:
```ts
const { data, error: supaError } = await supabase
  .from('crew_members')
  .select(`
    user_id, role, joined_at,
    user_profiles ( display_name, avatar_color )
  `)
  .eq('session_id', session.id)
  .returns<CrewMemberRow[]>();
```

The `crew_members` table Row type in `supabase.ts:107-114` has columns: `display_name`, `id`, `joined_at`, `session_id`, `user_id`. There is no `role` column. Supabase will silently return `null` for `role` on every row.

The `Relationships` block at `supabase.ts:129-144` only declares a FK to `crew_sessions`. There is no declared FK from `crew_members.user_id` to `user_profiles`. Supabase's implicit join relies on FK declarations in the schema — without one, the `user_profiles ( display_name, avatar_color )` join will return `null` for all members, and `profile?.display_name` will always be null.

Result: the `CrewMemberDashboard` member list renders every skater with a null display name and a null avatar color, falling back to `'?'` initials and default colors.

**Evidence:** `supabase.ts:107-114` (no `role` column), `supabase.ts:129-144` (no FK to `user_profiles`), `CrewMemberDashboard.tsx:202-208` (query), `CrewMemberDashboard.tsx:217-225` (consumption).

**Confidence: VERIFIED** statically. UNVERIFIED at runtime: the Supabase project may have an implicit join path via `user_id` if the column name matches a known convention (`user_id` referencing `auth.users` which then joins `user_profiles`). This requires a runtime/Supabase dashboard check to confirm actual join behavior.

---

### Flow 7 — Heartbeat: Write-Only Telemetry

**Class of bug:** Telemetry written but not consumed for expiry enforcement.

`CrewRealtime.startHeartbeat` writes `updated_at = now()` every 60s. `cleanupExpiredSessions` (`CrewSessionManager.ts:91-113`) uses `.lt('expires_at', now)` — not `updated_at`. There is no "mark expired if updated_at is stale" logic anywhere in source. The heartbeat may serve a server-side Supabase cron (not in local migrations) but has zero observable effect in client source.

The heartbeat is properly cleaned up in `unsubscribeRealtime()` (`CrewRealtime.ts:146-155`). No timer leak.

**Evidence:** `CrewRealtime.ts:112-124`, `CrewSessionManager.ts:91-113`, `useCrewHub.ts:168` (no `updatedSince` passed).

---

## Repair Surface

### Flow 2 — Schedule Activation (cosmetic → real feature)

To make scheduled sessions functional, ALL of the following are needed:

| File / Layer | Change Required |
|---|---|
| `src/components/crew/CrewScheduleScreen.tsx` L57-64 | Remove placeholder comment; trigger push notification via a notification service (Notifee or Expo Notifications) at scheduled time |
| `src/services/CrewService/CrewSessionManager.ts` | `joinSession` must query `is_active: false, status: 'scheduled'` in addition to active sessions, OR a mechanism must flip `status` → `active` and `is_active` → `true` at `scheduled_at` time |
| New: push notification scheduler | Call Expo Notifications `scheduleNotificationAsync` with `scheduledTime = scheduled_at` and a 15-min-before trigger for crew members |
| Supabase (optional) | A DB trigger or Edge Function on `crew_sessions` where `scheduled_at <= now()` and `status = 'scheduled'` auto-flips `is_active = true, status = 'active'` — this is the cleanest activation path |
| `src/services/CrewService/CrewSessionManager.ts:125` | `joinSession` query filter needs to allow `status IN ('active', 'scheduled')` OR only query when activation has occurred |

**Note:** This is a feature gap, not a bug fix. Scope is medium-to-large. No existing plan covers it.

---

### Flow 4a — Add member_update Sender

| File | Change Required |
|---|---|
| `src/services/CrewService/CrewSessionManager.ts` `joinSession` L141-147 | After the `upsert`, call `this.service.channel?.send({ type: 'broadcast', event: 'member_update', payload: {} })` — requires the joining member to already be subscribed to the channel. Note: joining members subscribe AFTER `joinSession` returns (in `DashboardCrewPanel.tsx:116-118`), so the send would need to be triggered from the member side post-subscribe OR the leader should subscribe to Supabase Postgres Changes on `crew_members` instead of relying on a client broadcast |
| `src/services/CrewService/CrewSessionManager.ts` `joinSessionById` L184-188 | Same — add channel send after upsert |

**Cleaner alternative:** Replace the `member_update` broadcast approach with a Supabase `postgres_changes` subscription on `crew_members` for the leader. This fires automatically when any row is inserted, regardless of client-side send. Would require modifying `subscribeAsLeader` in `CrewRealtime.ts:29-44`.

---

### Flow 4b — Fix subscribeAsLeader No-Op Callback

| File | Change Required |
|---|---|
| `src/components/dashboard/DashboardCrewPanel.tsx:115` | Replace `() => {}` with a callback that triggers member list refresh. Requires access to `loadMembers` from `useCrewSession` — currently not passed into `DashboardCrewPanel`. Either pass it as a prop or use the `crewService.fetchMembers` directly in the callback |
| `src/hooks/useDashboardCrew.ts:80` | Same — replace `() => {}`. On auto-rejoin as leader, the callback should trigger `crewService.fetchMembers(session.id)` and update some local state, or call a prop callback |

---

### Flow 4c — Fix Schema Drift in CrewMemberDashboard

| File | Change Required |
|---|---|
| `src/components/CrewMemberDashboard.tsx:202-208` | Remove `role` from select (column does not exist in schema). Remove `user_profiles` join (no FK). Replace with a two-step fetch: (1) `crew_members` for membership data, (2) separate `user_profiles` query by `user_id` array |
| `src/types/supabase.ts` (INFERRED) | If `role` column genuinely exists at DB level but is missing from the generated types, run `supabase gen types typescript` to regenerate. If it does not exist, the query must be fixed. This requires a runtime/dashboard check. |

---

### Flow 5 — Rejoin Scene Restore (type mismatch)

Already fully covered by `docs/plans/PLAN-fix-crew-broadcast-scene.md` Steps 14-15. No additional repair surface beyond what that plan specifies.

---

## Confidence

| Finding | Confidence | Gap (if any) |
|---|---|---|
| Flow 1 (create) working | VERIFIED | None |
| Flow 2 (schedule) broken — no activation | VERIFIED | Whether `is_active` DB default is `false` for new rows — needs runtime/dashboard check |
| Flow 3 (join) working except scene sink | VERIFIED (scene sink covered by broadcast plan) | None |
| Flow 4a (member_update no sender) | VERIFIED | None — ripgrep conclusive |
| Flow 4b (subscribeAsLeader no-op) | VERIFIED | None |
| Flow 4c (crew_members.role + user_profiles join) | VERIFIED statically | Actual Supabase join runtime behavior for `user_profiles` — UNVERIFIED; needs runtime or dashboard check |
| Flow 5 (auto-rejoin) suspect — scene type mismatch | VERIFIED (same root cause as broadcast bug) | None beyond broadcast plan |
| Flow 6 (end/leave) working | VERIFIED | None |
| Flow 7 (heartbeat write-only) | VERIFIED in source | Whether server-side cron uses `updated_at` — UNVERIFIED; not in local migrations |

---

*Audit complete. No source files were modified. All findings are read-only static analysis.*
