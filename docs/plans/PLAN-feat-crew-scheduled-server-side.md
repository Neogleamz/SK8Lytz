# Implementation Plan

**Slug:** `feat-crew-scheduled-server-side`
**Author:** Quinn (TPM)
**Date:** 2026-06-22
**Risk:** H-RISK (server-side cron + DB migration + production edge function)
**Authoritative inputs read in full:**
- `docs/analysis/crew-subsystem-e2e-audit.md` (Flow 2 + Flow 7)
- `supabase/functions/notify-crew-session/index.ts` (existing, deployed)
- `src/types/supabase.ts` L107-258 (`crew_members`, `crew_memberships`, `crew_sessions`), L1398-1421 (`push_tokens`)
- `src/services/CrewService/CrewSessionManager.ts` (createSession, joinSession, joinSessionById)
- `src/components/crew/CrewScheduleScreen.tsx` (handleCreate L27-69, placeholder L57-59)
- `src/services/NotificationService.ts` (full client push wrapper — already built)
- `src/hooks/useDashboardProfile.ts` L106-120 (join handler wiring)
- `src/screens/DashboardScreen.tsx` L172-177 (onCrewJoinNotification consumer)

---

## 0. Problem Statement (Evidence-Grounded)

Scheduling is cosmetic. `CrewSessionManager.createSession` (L31) writes `status: 'scheduled'` with `is_active` defaulting (the row is created without setting `is_active`, so it relies on the DB default). `CrewScheduleScreen.tsx:57-59` has an explicit `// Optional notifications logic can be added later` placeholder — no activation, no notification. `joinSession` (`CrewSessionManager.ts:125-126`) filters `.eq('is_active', true).gt('expires_at', now)` — a scheduled row can never be joined until something flips it active.

**What ALREADY EXISTS (do NOT rebuild):**
- `supabase/functions/notify-crew-session/index.ts` — a fully-built Expo Push edge function. It authenticates a caller JWT, verifies crew membership, fetches `push_tokens` joined to `crew_memberships` by `crew_id`, and sends via `https://exp.host/--/api/v2/push/send`. It is **caller-JWT-gated** — a cron job has no JWT, so it cannot be reused as-is for server-side activation.
- `src/services/NotificationService.ts` — client wrapper: registers Expo push tokens via `pushTokenService` into the `push_tokens` table (L96), wires a tap/response handler (L281-294) that calls `joinHandler(crewId, sessionId)`, and exposes `sendSessionStartingSoon` (L167) + `sendSessionLiveAlert` (L190).
- `push_tokens` table (`src/types/supabase.ts:1398`): columns `id, platform, token, updated_at, user_id`. **Push-token storage already exists — NO new migration needed for tokens.**
- `crew_memberships` table (L146): `crew_id, id, joined_at, role, user_id` — persistent per-crew membership, FK to `crews` and `user_profiles`.
- `crew_sessions` table (L185): has `scheduled_at`, `status`, `is_active`, `expires_at`, `updated_at`, `crew_id`, `name`, `leader_user_id`. **All columns the activation needs already exist — NO schema migration on crew_sessions needed.**

**The ONLY missing piece is the server-side activation mechanism** that periodically flips `status='scheduled' AND scheduled_at <= now()` rows to `status='active', is_active=true`, then fires a push to crew members. Plus three small client fixes so members can join the activated session and tap-deep-link into it.

---

## 1. Activation Mechanism Choice (with Rationale)

**CHOSEN: pg_cron + pg_net calling a Supabase scheduled Edge Function (`activate-scheduled-crews`).**

Two candidate mechanisms were evaluated:

| Option | Mechanism | Verdict |
|---|---|---|
| **A — pg_cron → Postgres function (pure SQL, no HTTP)** | A `cron.schedule` job calls a `plpgsql` function that UPDATEs the rows. Notifications would then require a DB trigger calling `pg_net.http_post` to the existing edge function — but the existing function is JWT-gated and cannot accept a cron call. | Rejected as sole mechanism: the UPDATE is trivial in SQL, but the push-send half MUST live in Deno (Expo Push API HTTP call + batching is already written in TS, not SQL). Splitting activation across SQL + a JWT-gated function creates two auth models. |
| **B — pg_cron → pg_net.http_post → NEW edge function `activate-scheduled-crews`** | One cron job hits one new edge function every minute. The function does BOTH: (1) the UPDATE via service-role client, and (2) the Expo push send (reusing the proven send logic from `notify-crew-session`). Auth is a shared-secret header, not a user JWT. | **CHOSEN.** Single auth model (cron secret), single place for activation + push logic, reuses the battle-tested Expo send code, and keeps all server logic in one reviewable Deno file. |

**Rationale summary:** The push-send code already exists in Deno/TS and depends on the Expo Push HTTP API + 100-message batching — re-implementing that in plpgsql is error-prone and untested. A dedicated cron-invoked edge function gives one auth boundary (a `CRON_SECRET` header, not a spoofable user JWT), one transactional update, and direct reuse of the existing send pattern. pg_cron + pg_net is the standard Supabase pattern for "scheduled HTTP work."

**Cadence:** every 1 minute (`* * * * *`). Scheduled sessions have minute-granularity (`DateTimePicker` mode `'time'`, `CrewScheduleScreen.tsx:148`), so a 60s poll bounds activation lateness to <60s. This is acceptable for a "session is live" alert.

**The 15-min reminder** is handled CLIENT-SIDE, not server-side, because the leader's device already schedules it locally and reliably: `NotificationService.sendSessionStartingSoon` (L167) already computes `scheduledAt - 15min` and schedules a local notification. The server-side cron owns ACTIVATION + the "is LIVE now" push; the client owns the 15-min "starting soon" reminder on the leader's device (Step 6). This split is deliberate: the reminder needs to fire even if the device is offline-ish, and local scheduled notifications survive without server round-trips.

> **HARD DEPENDENCY:** This entire mechanism is blocked on SPIKE-1 (pg_cron + pg_net availability) and SPIKE-2 (CRON_SECRET secret provisioning). See `## Prerequisites / SPIKES`. Do NOT begin Step 1 until both spikes return GREEN.

---

## Files to Create/Modify

| # | File Path | Type | Change Summary |
|---|---|---|---|
| 1 | `supabase/functions/activate-scheduled-crews/index.ts` | **CREATE** | New cron-invoked edge function: shared-secret auth, flips due scheduled sessions to active via service-role client, sends Expo push to each session's crew members. |
| 2 | `supabase/migrations/20260622000000_activate_scheduled_crews_cron.sql` | **CREATE** | Enables `pg_cron` + `pg_net`, stores secrets via Vault, schedules a 1-minute cron job that `pg_net.http_post`s to the new edge function. |
| 3 | `src/services/CrewService/CrewSessionManager.ts` | MODIFY | `createSession`: explicitly set `is_active: false` for scheduled inserts (Step 4). `joinSession` L121-130 + `joinSessionById` L167-176: no filter change needed once activation flips `is_active=true` — but add a clearer "not yet started" error path for `status='scheduled'` rows (Step 5). |
| 4 | `src/components/crew/CrewScheduleScreen.tsx` | MODIFY | Replace placeholder L57-59: schedule the local 15-min reminder via `notificationService.sendSessionStartingSoon` (Step 6). |
| 5 | `src/services/NotificationService.ts` | MODIFY | `JoinHandler` already carries `sessionId`; no signature change. (Confirmed already correct — see Step 7 note.) |
| 6 | `src/hooks/useDashboardProfile.ts` | MODIFY | L108-110: pass `sessionId` through the join handler so a tapped activation deep-links into the specific activated session, not just the crew (Step 7). |
| 7 | `src/screens/DashboardScreen.tsx` | MODIFY | L173-176: extend `onCrewJoinNotification` to accept + use `sessionId` for deep-link (Step 7). |
| 8 | `src/hooks/useDashboardProfile.ts` (type) | MODIFY | L29: widen `onCrewJoinNotification` prop signature to `(crewId: string, sessionId: string)` (Step 7). |

> **Diff contract:** Exactly these files. Files 5/6/7/8 are the "client join fix" cluster. Files 1/2 are the server cluster. File 3 is the insert-default correctness fix. NOTHING else may be touched.

---

## Prerequisites / SPIKES

These MUST be resolved before Step 1. They require RUNTIME / Supabase-project confirmation and CANNOT be verified statically from the repo.

### SPIKE-1 — pg_cron + pg_net availability `[BLOCKING]`
- **Unknown:** No migration in `supabase/migrations/` enables `pg_cron` or `pg_net` (grep for `pg_cron|net.http_post|cron.schedule` returned zero matches; the only `cron` hit is a code comment in `20260418061000_admin_user_management.sql:144`). The Supabase project tier and extension state are unknown.
- **Action:** In the Supabase SQL editor (or via `supabase db` CLI against the linked project), run:
  ```sql
  select * from pg_available_extensions where name in ('pg_cron','pg_net');
  select extname from pg_extension where extname in ('pg_cron','pg_net');
  ```
- **GREEN if:** both extensions are available (and ideally already installed). If `pg_cron` is NOT available on the project tier, HALT — the mechanism must change to Option A variant or an external scheduler, and this plan must be revised.
- **Note:** `pg_cron` on Supabase can only be created in the `pg_cron`/`extensions` schema and schedules run in the `postgres` database. Confirm the project allows `cron.schedule`.

### SPIKE-2 — CRON_SECRET + Expo creds in Supabase secrets `[BLOCKING]`
- **Unknown:** Whether a shared cron secret exists, and whether the edge runtime already has `SUPABASE_URL` / `SUPABASE_SERVICE_ROLE_KEY` (the existing `notify-crew-session` function reads these at L18-19, so they are almost certainly present — but confirm). Expo Push needs no credential (the existing function posts to Expo unauthenticated, L211-217), so **no Expo secret is required.**
- **Action:**
  ```bash
  supabase secrets list
  ```
  If `CRON_SECRET` is absent, set it:
  ```bash
  supabase secrets set CRON_SECRET="$(openssl rand -hex 32)"
  ```
  Record the SAME value for the Vault entry in Step 2 (the migration reads it from Vault, not from `supabase secrets`).
- **GREEN if:** `SUPABASE_URL`, `SUPABASE_SERVICE_ROLE_KEY` confirmed present in the edge runtime env, and a `CRON_SECRET` value is chosen + recorded for BOTH the function env and the Vault entry.

### SPIKE-3 — `is_active` DB default for scheduled rows `[NON-BLOCKING, confirms Step 4]`
- **Unknown (from audit L54, Flow 2 confidence gap):** whether `crew_sessions.is_active` defaults to `false` on insert. `src/types/supabase.ts:215` shows `is_active?: boolean` (optional on Insert) and L194 shows it is `boolean` (NOT NULL) on Row — implying a server default exists, but the value is unconfirmed.
- **Action:**
  ```sql
  select column_name, column_default, is_nullable
  from information_schema.columns
  where table_name='crew_sessions' and column_name='is_active';
  ```
- **Effect:** If the default is `true`, scheduled rows would be wrongly joinable immediately — Step 4 (explicitly setting `is_active:false` on scheduled insert) becomes MANDATORY and the primary correctness fix. If the default is `false`, Step 4 is defensive but still required for clarity. Either way Step 4 ships.

---

## Step-by-Step Implementation

> Branch: create worktree `feat-crew-scheduled-server-side` per Safety Protocol Rule 2. NEVER edit on `master`.

### Step 1 — Create the cron-invoked activation edge function

**File:** `supabase/functions/activate-scheduled-crews/index.ts` (CREATE)

**KB:** `KB: capture required` — Expo Push API request shape and `pg_net.http_post` invocation are external. The Expo send shape is mirrored verbatim from the proven `notify-crew-session/index.ts:192-217`; capture a KB entry for `supabase pg_cron + pg_net scheduled edge function` during execution.

Write the file with EXACTLY this content:

```ts
import "jsr:@supabase/functions-js/edge-runtime.d.ts";
import { createClient } from "https://esm.sh/@supabase/supabase-js@2";

// activate-scheduled-crews
// Invoked ONLY by pg_cron via pg_net.http_post (see migration
// 20260622000000_activate_scheduled_crews_cron.sql). NOT user-facing.
// Auth model: shared CRON_SECRET header (x-cron-secret) — NOT a user JWT,
// because cron has no authenticated user. Mirrors the Expo send logic from
// notify-crew-session/index.ts:192-217.

const EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

const adminClient = createClient(
  Deno.env.get("SUPABASE_URL")!,
  Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")!,
);

interface DueSession {
  id: string;
  name: string;
  crew_id: string | null;
  leader_user_id: string | null;
}

interface ExpoMessage {
  to: string;
  title: string;
  body: string;
  data: { crewId: string; sessionId: string };
  sound: string;
  channelId: string;
}

Deno.serve(async (req: Request): Promise<Response> => {
  // ── Auth: shared secret only ───────────────────────────────────────────────
  const provided = req.headers.get("x-cron-secret");
  const expected = Deno.env.get("CRON_SECRET");
  if (!expected || provided !== expected) {
    return new Response("Unauthorized", { status: 401 });
  }

  const nowIso = new Date().toISOString();

  // ── 1. Find due scheduled sessions ─────────────────────────────────────────
  let due: DueSession[];
  try {
    const { data, error } = await adminClient
      .from("crew_sessions")
      .select("id, name, crew_id, leader_user_id")
      .eq("status", "scheduled")
      .lte("scheduled_at", nowIso);
    if (error) {
      console.error("[activate-scheduled-crews] select error:", error.message);
      return new Response(JSON.stringify({ activated: 0, error: "select" }), {
        status: 500,
        headers: { "Content-Type": "application/json" },
      });
    }
    due = (data ?? []) as DueSession[];
  } catch (e: unknown) {
    console.error(
      "[activate-scheduled-crews] select exception:",
      e instanceof Error ? e.message : String(e),
    );
    return new Response(JSON.stringify({ activated: 0, error: "select-exc" }), {
      status: 500,
      headers: { "Content-Type": "application/json" },
    });
  }

  if (due.length === 0) {
    return new Response(JSON.stringify({ activated: 0, sent: 0 }), {
      headers: { "Content-Type": "application/json" },
    });
  }

  // ── 2. Flip them to active (single batched UPDATE) ─────────────────────────
  const dueIds = due.map((s: DueSession) => s.id);
  try {
    const { error } = await adminClient
      .from("crew_sessions")
      .update({ status: "active", is_active: true, updated_at: nowIso })
      .in("id", dueIds)
      .eq("status", "scheduled"); // guard against double-activation race
    if (error) {
      console.error("[activate-scheduled-crews] update error:", error.message);
      return new Response(JSON.stringify({ activated: 0, error: "update" }), {
        status: 500,
        headers: { "Content-Type": "application/json" },
      });
    }
  } catch (e: unknown) {
    console.error(
      "[activate-scheduled-crews] update exception:",
      e instanceof Error ? e.message : String(e),
    );
    return new Response(JSON.stringify({ activated: 0, error: "update-exc" }), {
      status: 500,
      headers: { "Content-Type": "application/json" },
    });
  }

  // ── 3. Build push messages per session for crew members ────────────────────
  const messages: ExpoMessage[] = [];
  for (const s of due) {
    if (!s.crew_id) continue; // ad-hoc sessions w/o a crew have no persistent roster to notify
    try {
      // push_tokens JOIN crew_memberships ON user_id; !inner narrows to this crew.
      // Mirror of notify-crew-session/index.ts:160-166.
      const { data, error } = await adminClient
        .from("push_tokens")
        .select("token, user_id, crew_memberships!inner(crew_id)")
        .eq("crew_memberships.crew_id", s.crew_id);
      if (error) {
        console.error(
          `[activate-scheduled-crews] token fetch error (crew ${s.crew_id}):`,
          error.message,
        );
        continue;
      }
      const rows = (data ?? []) as { token: string; user_id: string }[];
      for (const row of rows) {
        if (row.user_id === s.leader_user_id) continue; // skip the leader
        messages.push({
          to: row.token,
          title: "🛼 Crew is Live!",
          body: `${s.name} is starting now — tap to join`,
          data: { crewId: s.crew_id, sessionId: s.id },
          sound: "default",
          channelId: "crew-alerts",
        });
      }
    } catch (e: unknown) {
      console.error(
        `[activate-scheduled-crews] token fetch exception (crew ${s.crew_id}):`,
        e instanceof Error ? e.message : String(e),
      );
    }
  }

  // ── 4. Send via Expo Push API (batches of 100) ─────────────────────────────
  const batchSize = 100;
  let sent = 0;
  if (messages.length > 0) {
    const fetchPromises: Promise<void>[] = [];
    for (let i = 0; i < messages.length; i += batchSize) {
      const batch = messages.slice(i, i + batchSize);
      fetchPromises.push(
        fetch(EXPO_PUSH_URL, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
          },
          body: JSON.stringify(batch),
        }).then(async (resp: Response): Promise<void> => {
          if (resp.ok) {
            sent += batch.length;
          } else {
            console.error(
              "[activate-scheduled-crews] Expo error:",
              await resp.text(),
            );
          }
        }),
      );
    }
    const results = await Promise.allSettled(fetchPromises);
    for (const r of results) {
      if (r.status === "rejected") {
        console.error("[activate-scheduled-crews] fetch rejected:", r.reason);
      }
    }
  }

  console.log(
    `[activate-scheduled-crews] activated=${due.length} sent=${sent}`,
  );
  return new Response(JSON.stringify({ activated: due.length, sent }), {
    headers: { "Content-Type": "application/json" },
  });
});
```

**Verify (local, no deploy needed first):**
```bash
supabase functions serve activate-scheduled-crews --no-verify-jwt
```
In a second shell, simulate a cron call:
```bash
curl -i -X POST http://localhost:54321/functions/v1/activate-scheduled-crews \
  -H "x-cron-secret: WRONG"
# Expect: HTTP/1.1 401 Unauthorized

curl -i -X POST http://localhost:54321/functions/v1/activate-scheduled-crews \
  -H "x-cron-secret: <the CRON_SECRET from SPIKE-2>"
# Expect: HTTP/1.1 200 with JSON {"activated":N,"sent":M}
```
- **Expected:** wrong/absent secret → 401. Correct secret → 200 JSON. With no due rows: `{"activated":0,"sent":0}`.
- **Type check (Deno):** `deno check supabase/functions/activate-scheduled-crews/index.ts` — expect 0 errors. (This is a Deno file, NOT covered by `npm run verify`'s tsc — `npm run verify` excludes `supabase/functions`. Run `deno check` explicitly.)

**Deploy (after local verify + user consent):**
```bash
supabase functions deploy activate-scheduled-crews
```

---

### Step 2 — Create the pg_cron migration

**File:** `supabase/migrations/20260622000000_activate_scheduled_crews_cron.sql` (CREATE)

**KB:** `KB: capture required` — Supabase pg_cron + pg_net + Vault pattern is external infra; capture during execution.

> **Prerequisite:** SPIKE-1 GREEN (extensions available) and SPIKE-2 (CRON_SECRET chosen). The `<PROJECT_REF>` and the secret value below are filled in at execution time from the linked project (`supabase status` / project dashboard URL).

Write the file with EXACTLY this content (substitute `<PROJECT_REF>` and `<CRON_SECRET>` placeholders at execution):

```sql
-- Migration: server-side activation of scheduled crew sessions
-- Mechanism: pg_cron (every 1 min) -> pg_net.http_post -> edge function
--            activate-scheduled-crews. See PLAN-feat-crew-scheduled-server-side.md.
-- BLOCKED-ON: SPIKE-1 (pg_cron/pg_net available), SPIKE-2 (CRON_SECRET set).

-- 1. Extensions (idempotent). On Supabase these install into the `extensions` schema.
create extension if not exists pg_cron;
create extension if not exists pg_net;

-- 2. Store the function URL + cron secret in Vault (do NOT hardcode in cron body
--    where it would leak via cron.job inspection by lower-priv roles).
--    Replace <PROJECT_REF> and <CRON_SECRET> with the real values at deploy time.
select vault.create_secret(
  'https://<PROJECT_REF>.supabase.co/functions/v1/activate-scheduled-crews',
  'activate_scheduled_crews_url'
);
select vault.create_secret(
  '<CRON_SECRET>',
  'activate_scheduled_crews_secret'
);

-- 3. Schedule the job: every minute, POST to the edge function with the secret header.
select cron.schedule(
  'activate-scheduled-crews-every-min',
  '* * * * *',
  $$
  select net.http_post(
    url := (select decrypted_secret from vault.decrypted_secrets
            where name = 'activate_scheduled_crews_url'),
    headers := jsonb_build_object(
      'Content-Type', 'application/json',
      'x-cron-secret', (select decrypted_secret from vault.decrypted_secrets
                        where name = 'activate_scheduled_crews_secret')
    ),
    body := '{}'::jsonb
  );
  $$
);
```

**Verify:**
- **Apply locally first:** `supabase db reset` (applies all migrations to the local stack) — expect no SQL error on this migration. If `vault` schema is unavailable locally, comment the two `vault.create_secret` lines and the migration must instead be applied directly against the linked project (note this in the SESSION_LOG).
- **Apply to project:** `supabase db push` — expect success.
- **Confirm the job exists:**
  ```sql
  select jobname, schedule, active from cron.job
  where jobname = 'activate-scheduled-crews-every-min';
  -- Expect: 1 row, schedule '* * * * *', active = true
  ```
- **Confirm it runs (wait ~2 min):**
  ```sql
  select status, return_message, start_time from cron.job_run_details
  where jobid = (select jobid from cron.job where jobname='activate-scheduled-crews-every-min')
  order by start_time desc limit 3;
  -- Expect: status 'succeeded' rows appearing every minute
  ```

**Rollback (if needed):**
```sql
select cron.unschedule('activate-scheduled-crews-every-min');
select vault.delete_secret('activate_scheduled_crews_url');
select vault.delete_secret('activate_scheduled_crews_secret');
```

---

### Step 3 — Regenerate Supabase types (no schema change, defensive)

This plan adds NO new columns or tables (push_tokens, crew_memberships, crew_sessions all already typed). Therefore type regen is OPTIONAL and only run if SPIKE-3 reveals any drift.

**Command (only if drift found):**
```bash
supabase gen types typescript --linked > src/types/supabase.ts
```
**Verify:** `git diff src/types/supabase.ts` — expect NO diff (confirms no schema drift introduced by this plan). If a diff appears, STOP and report — it means the project schema diverged from the committed types independently of this work.

---

### Step 4 — Make scheduled inserts explicitly inactive

**File:** `src/services/CrewService/CrewSessionManager.ts`
**Target:** `createSession`, the `insertData` block (L28-38). Current code at L31 sets `status` but never sets `is_active`.

**Read first** (Surgical Strike Protocol): view L28-44 before editing.

**Edit:** after the existing `status` line in the `insertData` object literal (L31), add an explicit `is_active` based on whether it is scheduled:

Change:
```ts
      const insertData: Partial<Database['public']['Tables']['crew_sessions']['Insert']> & Record<string, unknown> = {
        name,
        leader_user_id: userId,
        status: opts?.scheduledAt ? 'scheduled' : 'active',
      };
```
To:
```ts
      const insertData: Partial<Database['public']['Tables']['crew_sessions']['Insert']> & Record<string, unknown> = {
        name,
        leader_user_id: userId,
        status: opts?.scheduledAt ? 'scheduled' : 'active',
        // Scheduled sessions are inactive until the server-side cron
        // (activate-scheduled-crews) flips them. Immediate sessions are live now.
        is_active: opts?.scheduledAt ? false : true,
      };
```

**Verify:**
- `git diff HEAD src/services/CrewService/CrewSessionManager.ts` — confirm ONLY the `insertData` object gained the `is_active` line + comment. No other lines changed.
- `npm run verify` — expect 0 TS errors, Jest green.

---

### Step 5 — Clearer "not started yet" error for scheduled joins

**File:** `src/services/CrewService/CrewSessionManager.ts`
**Target:** `joinSession` L121-130.

**Rationale:** Once activation flips `is_active=true`, the existing filter `.eq('is_active', true)` (L125) admits the session — so the JOIN PATH ITSELF NEEDS NO CHANGE. The only gap is the error message: a member who tries to join BEFORE activation gets the generic "Crew not found or session expired." We improve diagnosability by detecting the scheduled-but-not-yet-active case.

**Read first:** view L121-130.

**Edit:** after the existing failed-`single()` block, add a secondary lookup that distinguishes a scheduled session. Change:
```ts
      const { data: _sessionData, error: sessionErr } = await supabase
        .from('crew_sessions')
        .select('*')
        .eq('invite_code', code)
        .eq('is_active', true)
        .gt('expires_at', new Date().toISOString())
        .single();
      const session = _sessionData as CrewSession;

      if (sessionErr || !session) throw new Error('Crew not found or session expired');
```
To:
```ts
      const { data: _sessionData, error: sessionErr } = await supabase
        .from('crew_sessions')
        .select('*')
        .eq('invite_code', code)
        .eq('is_active', true)
        .gt('expires_at', new Date().toISOString())
        .single();
      const session = _sessionData as CrewSession;

      if (sessionErr || !session) {
        // Distinguish a scheduled-but-not-yet-activated session from a truly
        // missing one, so the joiner sees an actionable message. The server-side
        // cron (activate-scheduled-crews) will flip it to active at scheduled_at.
        const { data: _scheduled } = await supabase
          .from('crew_sessions')
          .select('scheduled_at,status')
          .eq('invite_code', code)
          .eq('status', 'scheduled')
          .maybeSingle();
        if (_scheduled) {
          throw new Error('This crew session has not started yet. You will be notified when it goes live.');
        }
        throw new Error('Crew not found or session expired');
      }
```

> **NOTE on `joinSessionById` (L163-176):** The audit (Repair Surface, L148) suggested also relaxing the filter, but with server-side activation that is UNNECESSARY and would be WRONG — joining a `status:'scheduled'` row before activation would let a member into a session that has not begun, splitting state. Leave `joinSessionById` filters UNCHANGED. The activation cron is the single source of truth for when a session becomes joinable.

**Verify:**
- `git diff HEAD src/services/CrewService/CrewSessionManager.ts` — confirm only the `joinSession` error branch changed (plus Step 4's insert line). `joinSessionById` MUST be untouched.
- `npm run verify` — 0 errors, Jest green.

---

### Step 6 — Wire the local 15-min reminder on schedule

**File:** `src/components/crew/CrewScheduleScreen.tsx`
**Target:** L57-62, the placeholder branch.

**Read first:** view L1-20 (imports) + L51-69.

**Edit 6a — import the notification service.** After the existing import of `crewService` (L7), add:
```ts
import { notificationService } from '../../services/NotificationService';
```

**Edit 6b — replace the placeholder.** Change:
```ts
      if (scheduled && scheduled > new Date()) {
        // Optional notifications logic can be added later
        setStep('landing');
      } else {
        setStep('controller');
      }
```
To:
```ts
      if (scheduled && scheduled > new Date()) {
        // Schedule a LOCAL 15-min "starting soon" reminder on the leader's device.
        // The server-side cron (activate-scheduled-crews) owns activation + the
        // crew-wide "is LIVE" push at scheduled_at. See PLAN-feat-crew-scheduled-server-side.md.
        const crewLabel = permanentCrews.find(c => c.id === selectedCrewId)?.name || sessionName;
        await notificationService.sendSessionStartingSoon({
          sessionId: newSession.id,
          sessionName,
          crewName: crewLabel,
          scheduledAt: scheduled,
        });
        setStep('landing');
      } else {
        setStep('controller');
      }
```

> `sendSessionStartingSoon` already exists (`NotificationService.ts:167`) and internally handles the "<15 min away → fire immediately" case (L173-178). It returns a notification id we do not need to track here (cancellation on session-cancel is out of scope — see Out of Scope).

**Verify:**
- `git diff HEAD src/components/crew/CrewScheduleScreen.tsx` — confirm only the import line + the scheduled branch changed.
- `npm run verify` — 0 errors. Confirm no unused-import lint error (the import IS used).

---

### Step 7 — Deep-link the activation tap into the specific session

The push from Step 1 carries `data: { crewId, sessionId }`. The tap handler (`NotificationService.ts:281-294`) ALREADY extracts both and calls `this.joinHandler(crewId, sessionId)` — **NotificationService needs NO change** (the `JoinHandler` type at L47 already is `(crewId, sessionId) => void`). The gap is downstream: `useDashboardProfile.ts:108` drops `sessionId`, and `DashboardScreen.tsx:173` only takes `crewId`.

**File 7a:** `src/hooks/useDashboardProfile.ts`
**Target type (L29):** `onCrewJoinNotification: (crewId: string) => void;`
**Read first:** view L26-32 and L106-120.

**Edit 7a-i — widen the prop type (L29):**
```ts
  onCrewJoinNotification: (crewId: string, sessionId: string) => void;
```

**Edit 7a-ii — pass sessionId through (L108-110).** Change:
```ts
    notificationService.setJoinHandler((crewId: string, _sessionId: string) => {
      onCrewJoinNotificationRef.current(crewId);
    });
```
To:
```ts
    notificationService.setJoinHandler((crewId: string, sessionId: string) => {
      onCrewJoinNotificationRef.current(crewId, sessionId);
    });
```

**File 7b:** `src/screens/DashboardScreen.tsx`
**Target (L173-176).**
**Read first:** view L172-177 AND grep for `setPendingJoinCrewId` / `setPendingJoinSessionId` to confirm whether a session-id setter exists.

> **Decision rule for Sage:** If a `setPendingJoinSessionId` (or equivalent session-targeting state into the CrewModal) ALREADY EXISTS, set it here. If it does NOT exist, do the MINIMUM: accept the param, keep the existing crew-modal-open behavior, and store the sessionId via the existing pending mechanism if one is plumbed into the modal. Do NOT invent new modal plumbing — that is a separate task. Adding the param without a sink is acceptable (the deep-link narrows to crew; session-precision is a follow-up).

**Edit 7b — extend the callback signature:**
```ts
    onCrewJoinNotification: (crewId: string, sessionId: string) => {
      setPendingJoinCrewId(crewId);
      // If a session-targeting setter exists in this screen, set it; otherwise the
      // crew modal opens to the crew and the user picks the now-active session.
      // (Session-precision deep-link plumbing is a follow-up, not this plan.)
      void sessionId;
      setIsCrewModalVisible(true);
    },
```

> If Sage's grep in the read-first step FINDS a `setPendingJoinSessionId`, replace `void sessionId;` with `setPendingJoinSessionId(sessionId);` and remove the `void` line + the follow-up comment.

**Verify:**
- `git diff HEAD src/hooks/useDashboardProfile.ts src/screens/DashboardScreen.tsx` — confirm only the listed lines changed.
- `npm run verify` — 0 errors. The widened signature must typecheck end-to-end (handler → hook prop → screen).

---

## Verification — Full Plan Acceptance

1. **Client gates:** `npm run verify` GREEN after Steps 4–7 (TSC + Jest + AST + TypeSafety + WorkflowValidator). This is the SOLE client verification command (S7).
2. **Edge function:** `deno check supabase/functions/activate-scheduled-crews/index.ts` → 0 errors; local `curl` returns 401 on bad secret / 200 JSON on good secret (Step 1 Verify).
3. **Migration:** `cron.job` shows 1 active row; `cron.job_run_details` shows `succeeded` runs every minute (Step 2 Verify).
4. **End-to-end smoke (manual, post-deploy):** Insert a `crew_sessions` row with `status='scheduled'`, `crew_id` set to a crew with ≥1 member holding a `push_tokens` row, and `scheduled_at = now()+90s`. Within ~2 min: confirm the row flips to `status='active', is_active=true`, and a member device receives the "🛼 Crew is Live!" push. Tapping it opens the crew modal (Step 7).
5. **Join path:** With the session now active, a member joins via invite code → success (no "not started yet" error). Before activation, the same join attempt returns the new "has not started yet" message (Step 5).

---

## Out of Scope (HARD BOUNDARY — do NOT touch)

- **Light-broadcast / scene payload routing** (`applyCloudScene` vs `applyCrewPayload`, `CrewRealtime.ts` scene sink) — covered by `PLAN-fix-crew-broadcast-scene.md`. Do NOT open `CrewRealtime.ts` for scene work.
- **Membership-presence / `member_update` sender** (Flow 4a/4b — no-op `subscribeAsLeader` callback, missing broadcast sender) — separate plan. Do NOT touch `subscribeAsLeader`, `DashboardCrewPanel.tsx:115`, or `useDashboardCrew.ts:80`.
- **`CrewMemberDashboard` schema drift** (Flow 4c — `role` column + `user_profiles` join) — separate plan. Do NOT touch `CrewMemberDashboard.tsx`.
- **Heartbeat / `updated_at` expiry consumption** (Flow 7) — separate concern. Do NOT modify `startHeartbeat` or `cleanupExpiredSessions`.
- **The existing `notify-crew-session` edge function** — leave UNCHANGED. The new function is additive; do NOT refactor or "DRY up" the shared Expo-send logic across the two functions (Surgical Before Heroic).
- **Reminder cancellation on session-cancel** — `sendSessionStartingSoon` returns an id we are not tracking; wiring cancel-on-cancel is a follow-up, not this plan.
- **New modal session-precision plumbing** in `DashboardScreen` — Step 7 stays minimal (see decision rule). Do NOT build new CrewModal props.

---

## Risk & Rollback

| Risk | Severity | Mitigation / Rollback |
|---|---|---|
| pg_cron unavailable on project tier | BLOCKING | SPIKE-1 gate. If RED, plan is revised before any code — no rollback needed because nothing shipped. |
| Cron secret leak via `cron.job` body inspection | HIGH | Secrets read from Vault `decrypted_secrets`, not inlined in the cron body. Rollback: `cron.unschedule` + `vault.delete_secret` (Step 2 rollback block). |
| Edge function double-activates a session (race across overlapping cron runs) | MED | The UPDATE is guarded with `.eq('status','scheduled')` so a second run finds 0 rows. Idempotent. |
| Wrong `is_active` default makes scheduled rows joinable early | MED | SPIKE-3 confirms; Step 4 explicitly forces `is_active:false`. Rollback: `git checkout -- src/services/CrewService/CrewSessionManager.ts`. |
| Push spam if cron interval miscalculates "due" | MED | `status` flip to `'active'` removes the row from the next run's `status='scheduled'` filter — each session is activated (and notified) exactly once. |
| Migration partially applies (extensions created, cron fails) | MED | Migration is ordered: extensions → vault → schedule. Re-running is idempotent (`if not exists`, `create_secret` will error if duplicate — wrap in spike check). Rollback via the Step 2 rollback block + drop the migration file before re-push. |
| Edge function deploy succeeds but cron not yet scheduled | LOW | No effect — function simply never fires until migration applied. Order: deploy function (Step 1) BEFORE applying migration (Step 2). |
| Client changes ship without server (or vice versa) | MED | Client Steps 4–7 are independently safe (scheduled sessions just stay inactive, same as today, plus a clearer error + a local reminder). Server can ship first. No hard coupling at deploy time. |

**Branch-level rollback:** the entire client change set reverts with `git checkout -- <files>`; the server change reverts via the Step 2 rollback SQL block + `supabase functions delete activate-scheduled-crews`.

---

## Files Touched (for AST collision)

```
src/services/CrewService/CrewSessionManager.ts
src/components/crew/CrewScheduleScreen.tsx
src/hooks/useDashboardProfile.ts
src/screens/DashboardScreen.tsx
supabase/functions/activate-scheduled-crews/index.ts
supabase/migrations/20260622000000_activate_scheduled_crews_cron.sql
```

> Note: `src/services/NotificationService.ts` is READ but NOT MODIFIED (Step 7 confirmed its `JoinHandler` type already carries `sessionId`). It is intentionally absent from this collision list.
