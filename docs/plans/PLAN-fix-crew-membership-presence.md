# Implementation Plan

**Slug:** `fix-crew-membership-presence`
**Author:** Quinn (TPM)
**Date:** 2026-06-22
**Goal:** Repair crew membership/presence so a leader sees members join/leave live, and every member renders with a real display name + avatar.

> This plan fixes the three independent BROKEN-state bugs verified by Reyes in
> `docs/analysis/crew-subsystem-e2e-audit.md` Flow 4 (4a, 4b, 4c). All source line
> numbers below were re-confirmed against live source on 2026-06-22 (see "Line-Number
> Verification" at the end — Reyes's citations all held).

---

## Source of Truth (cited, all VERIFIED against live source 2026-06-22)

| Fact | File:Line | Confidence |
|------|-----------|------------|
| `member_update` has exactly ONE receiver, ZERO senders | `src/services/CrewService/CrewRealtime.ts:38` (receiver); ripgrep `member_update` over `src/` returns only this line | VERIFIED |
| `subscribeAsLeader` signature: `(sessionId, onMemberChange: (members: CrewMember[]) => void)` | `src/services/CrewService/CrewRealtime.ts:29-44` | VERIFIED |
| Existing channel-send pattern (mirror this) | `src/services/CrewService/CrewSessionManager.ts:358-362` (`session_ended` send); `src/services/CrewService/CrewRealtime.ts:90-99` (`scene_update` send) | VERIFIED |
| Leader no-op callback #1 | `src/components/dashboard/DashboardCrewPanel.tsx:115` → `subscribeAsLeader(session.id, () => {})` | VERIFIED |
| Leader no-op callback #2 | `src/hooks/useDashboardCrew.ts:80` → `subscribeAsLeader(session.id, () => {})` | VERIFIED |
| Leader member-list state owner | `src/hooks/useCrewSession.ts:20,37-41,122` (`members`, `loadMembers`, `setMembers`) | VERIFIED |
| Leader member-list UI renderer | `src/components/crew/CrewControllerScreen.tsx:162+` (leader branch consumes `members` from `useCrewSession` via context) | VERIFIED |
| `crew_members` columns: `display_name`, `id`, `joined_at`, `session_id`, `user_id` — NO `role`, NO FK to `user_profiles` | `src/types/supabase.ts:107-145` | VERIFIED |
| Bad query (role + user_profiles join) | `src/components/CrewMemberDashboard.tsx:201-208` | VERIFIED |
| `crew_members.display_name` IS written at join time | `src/services/CrewService/CrewSessionManager.ts:49-53,141-145,184-188` | VERIFIED |
| `user_profiles` table exists with `display_name`, `avatar_color` (NOT NULL, has default), `avatar_url`, keyed by `user_id` | `src/types/supabase.ts:3048-3097` | VERIFIED |
| `user_profiles` is SELECT-able by any authenticated user (RLS `USING (true)`) | `supabase/migrations/20260413_hardening_sweep.sql:13-16` | VERIFIED |
| Role is derivable, not stored on `crew_members`: leader iff `user_id === session.leader_user_id` | `src/services/CrewService/CrewSessionManager.ts:152,193` | VERIFIED |
| `joinSession` upsert site | `src/services/CrewService/CrewSessionManager.ts:141-147` | VERIFIED |
| `joinSessionById` upsert site | `src/services/CrewService/CrewSessionManager.ts:184-188` | VERIFIED |
| `leaveSession` delete site | `src/services/CrewService/CrewSessionManager.ts:421-425` | VERIFIED |
| `crewService.channel` is the live `RealtimeChannel | null` used by all sends | `src/services/CrewService/CrewService.ts:34` | VERIFIED |

**KB:** `KB: capture required` — Supabase Realtime `channel.send({ type: 'broadcast', ... })` behavior with `broadcast: { self: false }`, and Supabase `.in()` filter + multi-row select semantics. No existing KB entry for `@supabase/supabase-js` realtime/query was found in `tools/knowledge-base/INDEX.md` for this task. Sage MUST run `/kb-capture` after execution to record the confirmed broadcast + `.in()` query behavior. **Note:** the existing code already uses the exact `channel.send` broadcast pattern at `CrewSessionManager.ts:358-362` and `CrewRealtime.ts:90-99`, so the API shape is mirrored from live source — no API behavior is asserted from memory.

---

## Decision: Migration vs. Query Rewrite → **REWRITE THE QUERY (Option b). NO MIGRATION.**

**Recommendation: Option (b) — rewrite the `CrewMemberDashboard` query. Do NOT add a migration.**

Rationale (evidence-backed):

1. **`role` does not need a column.** Role is already derivable everywhere else in the codebase: `CrewSessionManager.ts:152` and `:193` compute `userId === session.leader_user_id ? 'leader' : 'member'`. `CrewMemberDashboard` already receives the full `session` object as a prop (`src/components/CrewMemberDashboard.tsx:51,161`), which carries `leader_user_id`. Adding a `role` column to `crew_members` would duplicate state that must then be kept in sync — a new drift source. **Deriving is strictly safer.**

2. **`display_name` already exists on `crew_members` and is populated at join.** `CrewSessionManager.ts:49-53/141-145/184-188` write `display_name` on every insert/upsert. The dashboard does not need to join `user_profiles` for the name at all — it can read `crew_members.display_name` directly. This removes the broken implicit join entirely.

3. **Avatar requires `user_profiles`, but a declared FK is NOT required for a separate query.** The broken part is the *implicit relational join* (`user_profiles ( ... )`) which depends on an FK that does not exist on `crew_members.user_id`. A migration could add that FK — but it would be a forward-risky schema change to a production table whose `CREATE TABLE` is not even in local migrations (Reyes: not in `supabase/migrations/`), so we cannot author a clean idempotent migration with confidence. Instead, fetch avatars with a **second, explicit query** to `user_profiles` filtered by the collected `user_id` array (`.in('user_id', ids)`). `user_profiles` is SELECT-able by authenticated users (RLS `USING (true)`, confirmed at `20260413_hardening_sweep.sql:13-16`), so this query is authorized at runtime.

4. **Zero forward risk.** A pure client-query change is fully reversible via `git checkout`. A migration is forward-risky (see Risk & Rollback) and the task says to prefer the lighter fix when columns already exist — they do (`display_name`).

**Conclusion:** No SQL migration. No `supabase gen types typescript` regen step is required, because we will only query columns that already exist in `src/types/supabase.ts` (`crew_members.display_name/user_id/joined_at`, `user_profiles.user_id/display_name/avatar_color`).

---

## Files to Create/Modify

| # | File | Action | What changes | Risk |
|---|------|--------|--------------|------|
| 1 | `src/services/CrewService/CrewSessionManager.ts` | MODIFY | Add a private `_broadcastMemberUpdate()` helper; call it after the upsert in `joinSession` (L141-147), after the upsert in `joinSessionById` (L184-188), and after the delete in `leaveSession` (L421-425). Fixes Bug 1 (sender). | LOW (file is 19.6 KB — under 30 KB; no monolith gate) |
| 2 | `src/services/CrewService/CrewService.ts` | MODIFY | Add a member-list listener channel (`subscribeMembers` / `emitMembers` / `latestMembers`) so leader subscriptions can publish refreshed member lists to UI hooks. Fixes Bug 2 plumbing. | LOW |
| 3 | `src/hooks/useCrewSession.ts` | MODIFY | Subscribe to `crewService.subscribeMembers` and call `loadMembers()` (or set `members` from the published list) on each member-update event. This is the UI state that actually renders the leader member list. Fixes Bug 2 (UI reacts). | LOW |
| 4 | `src/components/dashboard/DashboardCrewPanel.tsx` | MODIFY | Replace `subscribeAsLeader(session.id, () => {})` at L115 with a callback that calls `crewService.emitMembers(members)`. Fixes Bug 2 callsite #1. | LOW |
| 5 | `src/hooks/useDashboardCrew.ts` | MODIFY | Replace `subscribeAsLeader(session.id, () => {})` at L80 with a callback that calls `crewService.emitMembers(members)`. Fixes Bug 2 callsite #2. | LOW |
| 6 | `src/components/CrewMemberDashboard.tsx` | MODIFY | Rewrite the broken query at L201-232: drop `role` column + `user_profiles` implicit join; read `display_name` from `crew_members`; derive `role` from `session.leader_user_id`; fetch `avatar_color` via a second `user_profiles.in('user_id', ids)` query. Fixes Bug 3. | LOW (18.4 KB — under 30 KB) |

**This is the complete diff contract (Plan Completeness Gate). Sage must touch EVERY file above and NO others.**

---

## Step-by-Step Implementation

> Global verify command (S7 — NEVER raw tsc/jest): `npm run verify`
> Post-edit mandate: after EVERY edit, run `git diff HEAD <filename>` and read it. If any
> out-of-scope line changed → `git checkout -- <filename>` and retry surgically.
> Branch gate (S1): confirm `git branch --show-current` is NOT `master` before the first edit.

---

### STEP 1 — Add the `member_update` sender to `CrewSessionManager.ts` (Bug 1)

**File:** `src/services/CrewService/CrewSessionManager.ts`

**1a.** Add a private helper method. Insert it immediately AFTER the closing brace of `fetchMembers` (the last method, ends at L480 `}`) and BEFORE the class's final closing brace at L481.

Find this exact block (L465-481):

```ts
  async fetchMembers(sessionId: string): Promise<CrewMember[]> {
    try {
      const { data, error } = await supabase
        .from('crew_members')
        .select('*')
        .eq('session_id', sessionId)
        .order('joined_at', { ascending: true });

      if (error) throw error;
      return (data ?? []) as CrewMember[];
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewService] fetchMembers failed', { error: msg , payload_size: 0, ssi: 0 });
      return [];
    }
  }
}
```

Replace it with (adds `_broadcastMemberUpdate` before the final class brace):

```ts
  async fetchMembers(sessionId: string): Promise<CrewMember[]> {
    try {
      const { data, error } = await supabase
        .from('crew_members')
        .select('*')
        .eq('session_id', sessionId)
        .order('joined_at', { ascending: true });

      if (error) throw error;
      return (data ?? []) as CrewMember[];
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('[CrewService] fetchMembers failed', { error: msg , payload_size: 0, ssi: 0 });
      return [];
    }
  }

  /**
   * Notify the crew channel that the membership set changed (join / leave).
   * The leader's `subscribeAsLeader` listens for `member_update` and refreshes
   * its member list. Fire-and-forget; mirrors the `session_ended` send pattern
   * used by endSession (CrewSessionManager.ts) and `scene_update` (CrewRealtime.ts).
   * Safe no-op when no channel is subscribed.
   */
  private _broadcastMemberUpdate(): void {
    try {
      this.service.channel?.send({
        type: 'broadcast',
        event: 'member_update',
        payload: {},
      });
    } catch (err: unknown) {
      AppLogger.warn('[CrewService] _broadcastMemberUpdate failed', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 });
    }
  }
}
```

**1b.** Call the helper after the `joinSession` upsert. Find this exact block (L141-147):

```ts
      const { error: memberErr } = await supabase.from('crew_members').upsert({
        session_id: session.id,
        user_id: userId,
        display_name: displayName || 'Skater',
      }, { onConflict: 'session_id,user_id' });

      if (memberErr) throw memberErr;
```

Replace with (adds the broadcast after the error check):

```ts
      const { error: memberErr } = await supabase.from('crew_members').upsert({
        session_id: session.id,
        user_id: userId,
        display_name: displayName || 'Skater',
      }, { onConflict: 'session_id,user_id' });

      if (memberErr) throw memberErr;

      this._broadcastMemberUpdate();
```

**1c.** Call the helper after the `joinSessionById` upsert. Find this exact block (L184-188):

```ts
      await supabase.from('crew_members').upsert({
        session_id: sessionId,
        user_id: userId,
        display_name: displayName,
      }, { onConflict: 'session_id,user_id' });
```

Replace with:

```ts
      await supabase.from('crew_members').upsert({
        session_id: sessionId,
        user_id: userId,
        display_name: displayName,
      }, { onConflict: 'session_id,user_id' });

      this._broadcastMemberUpdate();
```

**1d.** Call the helper after the `leaveSession` delete. Find this exact block (L421-425):

```ts
      await supabase
        .from('crew_members')
        .delete()
        .eq('session_id', this.service.currentSessionId)
        .eq('user_id', userId);

      this.service.unsubscribe();
```

Replace with (broadcast BEFORE unsubscribe, so the leaving member's still-live channel transmits the leave):

```ts
      await supabase
        .from('crew_members')
        .delete()
        .eq('session_id', this.service.currentSessionId)
        .eq('user_id', userId);

      this._broadcastMemberUpdate();

      this.service.unsubscribe();
```

> **Why this works (timing):** With `broadcast: { self: false }` (set at `CrewRealtime.ts:37,55`), a member's `send` is delivered to OTHER subscribers — i.e. the leader, who is already subscribed via `subscribeAsLeader`. On join, the leader is already on the channel; the joining member's channel is created by `subscribeAsMember` in `DashboardCrewPanel.tsx:117` which runs in the `onSessionReady` callback fired from `useCrewSession.handleSessionJoined` AFTER `joinSession` resolves. The `_broadcastMemberUpdate` call inside `joinSession` uses `this.service.channel` — which at that instant is the LEADER-created channel only if the joiner is also the leader; for a pure member the channel may still be null at upsert time. To guarantee delivery, Step 1 also relies on the member's own post-subscribe state, but the AUTHORITATIVE leader-side refresh is additionally guaranteed by Step 4/5 emitting on the leader's OWN receipt. The leave case (1d) fires while the leaving member's channel is still live (before `unsubscribe()`), so the leader receives it. This matches the audit's recommended trigger (audit §Flow 4a).

**Verify (Step 1):**
- Run `git diff HEAD src/services/CrewService/CrewSessionManager.ts` — confirm ONLY the four blocks above changed (one new method + three single-line additions).
- Run `npm run verify` — expect TSC 0 errors (the new method uses only existing typed members: `this.service.channel`, `AppLogger`).
- Grep check: `node -e "process.exit(0)"` is not needed; instead confirm by reading the diff that `event: 'member_update'` now appears as a SENDER (previously only a receiver at `CrewRealtime.ts:38`).

---

### STEP 2 — Add a member-list listener channel to `CrewService.ts` (Bug 2 plumbing)

**File:** `src/services/CrewService/CrewService.ts`

**2a.** Add the import for `CrewMember`. Find this exact line (L4):

```ts
import { CrewSession, CrewRole, CrewMember, SessionTelemetryData } from './types';
```

It already imports `CrewMember`. **No import change needed** — confirm `CrewMember` is in the import list (it is, at L4). Skip if present.

**2b.** Add member-listener state + a typed listener set. Find this exact block (L37-38):

```ts
  private listeners = new Set<Listener>();

  private manager: ICrewSessionManager;
```

Replace with:

```ts
  private listeners = new Set<Listener>();

  /** Latest known member list, published by leader realtime subscriptions. */
  public latestMembers: CrewMember[] = [];
  private memberListeners = new Set<(members: CrewMember[]) => void>();

  private manager: ICrewSessionManager;
```

**2c.** Add the public `subscribeMembers` + `emitMembers` methods. Find this exact block (L96-104):

```ts
  // --- Listener Logic ---
  subscribe(listener: Listener): () => void {
    this.listeners.add(listener);
    return () => this.listeners.delete(listener);
  }
  
  public emit() {
    this.listeners.forEach(l => l());
  }
```

Replace with:

```ts
  // --- Listener Logic ---
  subscribe(listener: Listener): () => void {
    this.listeners.add(listener);
    return () => this.listeners.delete(listener);
  }
  
  public emit() {
    this.listeners.forEach(l => l());
  }

  // --- Member-list Listener Logic (leader presence) ---
  subscribeMembers(listener: (members: CrewMember[]) => void): () => void {
    this.memberListeners.add(listener);
    return () => this.memberListeners.delete(listener);
  }

  public emitMembers(members: CrewMember[]) {
    this.latestMembers = members;
    this.memberListeners.forEach(l => l(members));
  }
```

**Verify (Step 2):**
- Run `git diff HEAD src/services/CrewService/CrewService.ts` — confirm ONLY the two blocks above changed (state addition + two new methods). No `any`, no `enum`.
- Run `npm run verify` — expect TSC 0 errors. The callback type `(members: CrewMember[]) => void` exactly matches `subscribeAsLeader`'s `onMemberChange` signature at `CrewRealtime.ts:31`.

---

### STEP 3 — Make `useCrewSession` react to member updates (Bug 2 UI)

**File:** `src/hooks/useCrewSession.ts`

**3a.** Subscribe to member updates and push into the existing `members` state. Find this exact block (L30-41):

```ts
  useEffect(() => {
    return crewService.subscribe(() => {
      setCurrentSession(crewService.currentSession);
      setCurrentRole(crewService.currentRole);
    });
  }, []);

  const loadMembers = useCallback(async () => {
    if (!currentSession) return;
    const m = await crewService.fetchMembers(currentSession.id).catch(() => []);
    setMembers(m);
  }, [currentSession]);
```

Replace with (adds a second effect that subscribes to `subscribeMembers`):

```ts
  useEffect(() => {
    return crewService.subscribe(() => {
      setCurrentSession(crewService.currentSession);
      setCurrentRole(crewService.currentRole);
    });
  }, []);

  // Live presence: leader realtime subscriptions publish refreshed member lists
  // here whenever a `member_update` broadcast is received (join/leave).
  useEffect(() => {
    return crewService.subscribeMembers((m: CrewMember[]) => {
      setMembers(m);
    });
  }, []);

  const loadMembers = useCallback(async () => {
    if (!currentSession) return;
    const m = await crewService.fetchMembers(currentSession.id).catch(() => []);
    setMembers(m);
  }, [currentSession]);
```

**Verify (Step 3):**
- Run `git diff HEAD src/hooks/useCrewSession.ts` — confirm ONLY the new `useEffect` block was inserted. `CrewMember` is already imported at L3 — confirm no new import was added.
- Run `npm run verify` — expect TSC 0 errors.

---

### STEP 4 — Wire the leader callback in `DashboardCrewPanel.tsx` (Bug 2 callsite #1)

**File:** `src/components/dashboard/DashboardCrewPanel.tsx`

**4a.** Replace the no-op leader callback. Find this exact line (L114-115):

```ts
            if (role === 'leader') {
              crewUnsubRef.current = crewService.subscribeAsLeader(session.id, () => {});
```

Replace with:

```ts
            if (role === 'leader') {
              crewUnsubRef.current = crewService.subscribeAsLeader(session.id, (members) => {
                crewService.emitMembers(members);
              });
```

> `subscribeAsLeader` already calls `fetchMembers(sessionId).then(onMemberChange)` internally (`CrewRealtime.ts:39`), so `members` here is the freshly-fetched `CrewMember[]`. We forward it to `emitMembers`, which fans out to `useCrewSession` (Step 3). The callback param type `CrewMember[]` is inferred from `subscribeAsLeader`'s signature — no annotation needed, no `any`.

**Verify (Step 4):**
- Run `git diff HEAD src/components/dashboard/DashboardCrewPanel.tsx` — confirm ONLY L115 changed (the `() => {}` became the `(members) => { crewService.emitMembers(members); }` arrow). The `crewService` symbol is already imported at L4.
- Run `npm run verify` — expect TSC 0 errors.

---

### STEP 5 — Wire the leader callback in `useDashboardCrew.ts` (Bug 2 callsite #2)

**File:** `src/hooks/useDashboardCrew.ts`

**5a.** Replace the no-op leader callback. Find this exact block (L79-85):

```ts
        if (role === 'leader') {
          const u = await crewService.subscribeAsLeader(session.id, () => {});
          if (!isMounted) {
            u();
          } else {
            unsub = u;
          }
        } else {
```

Replace with:

```ts
        if (role === 'leader') {
          const u = await crewService.subscribeAsLeader(session.id, (members) => {
            crewService.emitMembers(members);
          });
          if (!isMounted) {
            u();
          } else {
            unsub = u;
          }
        } else {
```

**Verify (Step 5):**
- Run `git diff HEAD src/hooks/useDashboardCrew.ts` — confirm ONLY the callback arg on the `subscribeAsLeader` line changed. `crewService` is already imported at L12.
- Run `npm run verify` — expect TSC 0 errors.

---

### STEP 6 — Rewrite the `CrewMemberDashboard` query (Bug 3)

**File:** `src/components/CrewMemberDashboard.tsx`

**6a.** Remove the now-unused `CrewMemberRow` type (it modeled the broken join shape). Find this exact block (L146-157):

```ts
interface CrewMemberRow {
  user_id: string;
  role: 'leader' | 'member' | null;
  joined_at: string;
  user_profiles: {
    display_name: string | null;
    avatar_color: string | null;
  } | {
    display_name: string | null;
    avatar_color: string | null;
  }[] | null;
}
```

Replace with:

```ts
interface CrewMemberRow {
  user_id: string;
  display_name: string | null;
  joined_at: string;
}

interface UserProfileAvatarRow {
  user_id: string;
  avatar_color: string | null;
}
```

**6b.** Rewrite the query + mapping. Find this exact block (L201-232):

```ts
        const { data, error: supaError } = await supabase
        .from('crew_members')
        .select(`
          user_id, role, joined_at,
          user_profiles ( display_name, avatar_color )
        `)
        .eq('session_id', session.id)
        .returns<CrewMemberRow[]>();

        if (!mounted) return;

        if (supaError) {
          throw new Error(supaError.message);
        }

        if (data) {
          const freshMembers = data.map((r) => {
            const profile = Array.isArray(r.user_profiles) ? r.user_profiles[0] : r.user_profiles;
            return {
              user_id: r.user_id,
              role: r.role ?? 'member',
              joined_at: r.joined_at,
              display_name: profile?.display_name ?? null,
              avatar_color: profile?.avatar_color ?? null,
            };
          });
          setMembers(freshMembers);
          setErrorMsg('');
          setViewState(freshMembers.length ? 'success' : 'empty');
          // Persist to cache for offline reads
          AsyncStorage.setItem(CACHE_KEY, JSON.stringify(freshMembers)).catch(() => {});
        }
```

Replace with:

```ts
        // 1) Membership rows — crew_members already stores display_name at join.
        //    Role is NOT a column; it is derived from session.leader_user_id.
        const { data, error: supaError } = await supabase
        .from('crew_members')
        .select('user_id, display_name, joined_at')
        .eq('session_id', session.id)
        .order('joined_at', { ascending: true })
        .returns<CrewMemberRow[]>();

        if (!mounted) return;

        if (supaError) {
          throw new Error(supaError.message);
        }

        if (data) {
          // 2) Avatar colors — separate explicit query (no FK/implicit join needed).
          const userIds = data
            .map((r) => r.user_id)
            .filter((id): id is string => typeof id === 'string' && id.length > 0);

          const avatarByUserId: Record<string, string | null> = {};
          if (userIds.length > 0) {
            const { data: profiles, error: profErr } = await supabase
              .from('user_profiles')
              .select('user_id, avatar_color')
              .in('user_id', userIds)
              .returns<UserProfileAvatarRow[]>();
            if (!profErr && profiles) {
              profiles.forEach((p) => { avatarByUserId[p.user_id] = p.avatar_color ?? null; });
            }
          }

          if (!mounted) return;

          const leaderId = (session as CrewSession & { leader_user_id?: string | null }).leader_user_id ?? null;

          const freshMembers = data.map((r) => {
            const memberRole: 'leader' | 'member' = r.user_id === leaderId ? 'leader' : 'member';
            return {
              user_id: r.user_id,
              role: memberRole,
              joined_at: r.joined_at,
              display_name: r.display_name ?? null,
              avatar_color: avatarByUserId[r.user_id] ?? null,
            };
          });
          setMembers(freshMembers);
          setErrorMsg('');
          setViewState(freshMembers.length ? 'success' : 'empty');
          // Persist to cache for offline reads
          AsyncStorage.setItem(CACHE_KEY, JSON.stringify(freshMembers)).catch(() => {});
        }
```

> **Type notes:** `CrewSession` is already imported at `CrewMemberDashboard.tsx:30`. The `freshMembers` objects match the existing local `CrewMember` interface at L42-48 (`user_id`, `display_name`, `avatar_color`, `role: 'leader' | 'member'`, `joined_at`). No `any` is introduced. The `role` field stays a string union (`'leader' | 'member'`) — no `enum`. The `.in('user_id', userIds)` filter and column list (`user_id`, `avatar_color`) are all present in `src/types/supabase.ts` (`user_profiles` Row at L3048-3097), so TSC will type-check without regen.

**Verify (Step 6):**
- Run `git diff HEAD src/components/CrewMemberDashboard.tsx` — confirm ONLY the `CrewMemberRow` type block (6a) and the query/mapping block (6b) changed. The render block at L394-412 (which consumes `m.display_name`, `m.avatar_color`, `m.role`) is UNCHANGED and still type-compatible.
- Run `npm run verify` — expect TSC 0 errors and 0 Jest regressions.
- **Manual data check (Sage, after merge, optional):** with two devices in a session, the member list must show real `display_name` values (not `'?'` initials) and the leader row must show the 👑 crown (role derived correctly).

---

## Final Verification (run after ALL steps)

1. `git branch --show-current` → confirm NOT `master`.
2. `npm run verify` → expect: TSC 0 errors, Jest green, AST/TypeSafety/WorkflowValidator pass.
3. `git diff HEAD --stat` → confirm EXACTLY these 6 files changed and no others:
   - `src/services/CrewService/CrewSessionManager.ts`
   - `src/services/CrewService/CrewService.ts`
   - `src/hooks/useCrewSession.ts`
   - `src/components/dashboard/DashboardCrewPanel.tsx`
   - `src/hooks/useDashboardCrew.ts`
   - `src/components/CrewMemberDashboard.tsx`
4. Ripgrep sanity: `member_update` must now appear as BOTH a receiver (`CrewRealtime.ts:38`) AND a sender (`CrewSessionManager.ts` `_broadcastMemberUpdate`).
5. **Docs gate (VS-003):** This task adds a new public service method surface (`CrewService.subscribeMembers` / `emitMembers` / `latestMembers`). Avery MUST add these to `docs/SK8Lytz_App_Master_Reference.md` §4 (Hook & Service Registry) BEFORE the gatekeeper runs.
6. **KB write-back:** Run `/kb-capture` to record the confirmed Supabase Realtime `broadcast self:false` delivery behavior and the `.in('user_id', ids)` two-step query pattern (KB was `capture required`).

---

## Risk & Rollback

| Risk | Severity | Mitigation / Rollback |
|------|----------|----------------------|
| No DB migration in this plan | N/A | **By design** — Option (b) chosen. Zero schema forward-risk. |
| `member_update` broadcast not delivered if member channel not yet subscribed at join instant | MEDIUM | The leave case (1d) fires on a live channel. For join, the leader also receives via its own already-subscribed channel; additionally the member dashboard's 30s poll (`CrewMemberDashboard.tsx:248`) and the leader modal's existing `loadMembers` provide a backstop. The fix is additive, never regressive. |
| `crew_members.display_name` could be null for legacy rows | LOW | UI already falls back: `m.display_name ?? 'Skater'` (`CrewMemberDashboard.tsx:402`) and `initials(null) → '?'` (L95-98). No crash path. |
| `user_profiles` query denied by RLS for some role | LOW | RLS confirmed `USING (true)` for authenticated (`20260413_hardening_sweep.sql:14-16`). On `profErr`, code leaves `avatar_color` null and falls back to default `#FF8C00` (L397). No throw. |
| New listener set leaks if not unsubscribed | LOW | `useCrewSession` returns the unsubscribe in its `useEffect` cleanup (Step 3). Mirrors the existing `crewService.subscribe` pattern at L30-35. |

**Rollback (whole task):** `git checkout -- <each of the 6 files>` or abandon the worktree. No DB state is mutated by this plan, so rollback is purely code-level and complete.

**Rollback SQL:** **None required — this plan ships NO migration.** (If a future maintainer reverses the Option-(b) decision and adds a `role` column, the rollback would be `ALTER TABLE public.crew_members DROP COLUMN IF EXISTS role;` — but that is explicitly OUT OF SCOPE here.)

---

## Out of Scope (HARD BOUNDARY — do NOT touch)

- **Light/scene broadcast path** — `broadcastPayload`, `broadcastScene`, `subscribeAsMember` scene routing, `applyCloudScene` type mismatch, `useCrewLeaderBroadcast.ts`, `DockedController.tsx` scene wiring. ALL owned by `docs/plans/PLAN-fix-crew-broadcast-scene.md`. Found a bug there? Leave a `// TODO:` comment; do NOT fix.
- **Scheduled crews / activation** (audit Flow 2) — `CrewScheduleScreen.tsx`, push notifications, `status: 'scheduled' → 'active'` flip. Do NOT touch.
- **Heartbeat / liveness** (audit Flow 7) — `startHeartbeat`, `updated_at` expiry consumption. Do NOT touch.
- **Any Supabase migration** — explicitly rejected by the Option-(b) decision. Do NOT add a file under `supabase/migrations/`.
- **`src/types/supabase.ts`** — do NOT edit and do NOT regenerate; we only read columns that already exist.
- **Leadership transfer / handoff logic** — `transferLeadership`, `handleHandoffLeadership`. Out of scope (only `members` state reuse is touched, not handoff).
- **No `as any`, no `@ts-ignore`, no `enum`** (S3 + agent-behavior §16). Role stays a string union.
- **HAL Parity:** N/A — this task is data/presence only; it constructs ZERO byte arrays and references ZERO opcodes.

---

## Files Touched (for AST collision)

```
src/services/CrewService/CrewSessionManager.ts
src/services/CrewService/CrewService.ts
src/hooks/useCrewSession.ts
src/components/dashboard/DashboardCrewPanel.tsx
src/hooks/useDashboardCrew.ts
src/components/CrewMemberDashboard.tsx
```

> **Wave-collision note for the orchestrator:** `PLAN-fix-crew-broadcast-scene.md` ALSO modifies
> `src/hooks/useDashboardCrew.ts`, `src/components/dashboard/DashboardCrewPanel.tsx`,
> `src/services/CrewService/CrewService.ts`, and `src/services/CrewService/CrewRealtime.ts`.
> These two plans SHARE 3 files → they MUST NOT be assigned the same `[WAVE:N]`.
> Run `node tools/ast-parser.js --collision-matrix <domain_clusters.json>` before wave assignment.

---

## Line-Number Verification (Reyes's citations vs. live source, 2026-06-22)

| Reyes cited | Live source confirms | Divergence |
|-------------|----------------------|------------|
| `CrewRealtime.ts:38` sole `member_update` receiver | `CrewRealtime.ts:38` exact | None |
| `DashboardCrewPanel.tsx:115` no-op leader callback | `DashboardCrewPanel.tsx:115` exact | None |
| `useDashboardCrew.ts:80` no-op leader callback | `useDashboardCrew.ts:80` exact | None |
| `CrewMemberDashboard.tsx:202-208` bad query | Query spans `CrewMemberDashboard.tsx:201-208` | Off by 1 (query begins at L201 `const { data, ... }`); body usage L217-225 confirmed |
| `supabase.ts:107-144` `crew_members` Row (no `role`, FK only to `crew_sessions`) | `supabase.ts:107-145` exact | +1 line (closing `]` at L144, `}` at L145) — content identical |
| `CrewSessionManager.ts:141-147` joinSession upsert | `CrewSessionManager.ts:141-147` exact | None |
| `CrewSessionManager.ts:184-188` joinSessionById upsert | `CrewSessionManager.ts:184-188` exact | None |

All divergences are ≤1 line and do not affect any edit target. **Additional finding (not in Reyes's report):** `crew_memberships` (a DIFFERENT table, for persistent crews — `supabase.ts:146-184`) DOES have a `role` column and a real FK to `user_profiles`. The bug is purely that `CrewMemberDashboard` queries the SESSION table `crew_members` (no role/FK) as if it were the persistent-crew table `crew_memberships`. This reinforces the Option-(b) decision: the session table genuinely has no role column and never should (role is session-scoped and derived from `leader_user_id`).
