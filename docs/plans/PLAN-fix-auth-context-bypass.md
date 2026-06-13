# PLAN-fix-auth-context-bypass

**Status:** READY
**Generated:** 2026-06-10
**Source Report:** [system_audit_report.md](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/artifacts/system_audit_report.md) — Cluster K (Auth Context Bypass)
**Supersedes / Incorporates:** PLAN-AUTH-CONTEXT-BYPASS-REFACTOR.md, PLAN-auth-context-bypass-fix.md
**Scope Note:** This plan is NARROWER than PLAN-AUTH-CONTEXT-BYPASS-REFACTOR (which covers 25 sites). This plan covers only the 2 confirmed findings from the new audit sweep (R15-001, R15-002). The full 25-site refactor is a separate Banquet-sized task.

---

## Goal

Eliminate the two direct `supabase.auth.*` calls outside AuthContext identified in Cluster K:
1. `GroupRepository.ts:153` — `supabase.auth.getSession()` in the `deleteGroup()` cloud fallback path
2. `AppLogger.ts:652` — `supabase.auth.getUser()` in `uploadLogsToSupabase()`

Both calls bypass the reactive `AuthContext` state, creating a stale session risk when a token refresh occurs mid-session. The fix is dependency injection: accept `userId?: string` as a parameter from callers who hold the AuthContext value.

## Industry Standard

Services and repositories must not call `supabase.auth.*` directly. Auth state must flow from a single reactive source (AuthContext) through the call chain via parameter injection. Direct auth calls in service layers create N parallel auth fetch paths and break the offline-first pattern.

## Source of Truth

- **Primary:** `artifacts/system_audit_report.md` — Cluster K, Findings R15-001 and R15-002
- **GroupRepository:** `src/services/GroupRepository.ts:153` (VERIFIED live 2026-06-10)

```
# Cited Truth — GroupRepository.ts:152-158 (VERIFIED 2026-06-10):
      try {
        const { data: { session } } = await supabase.auth.getSession();  // LINE 153
        if (session?.user) {
          await supabase.from('registered_groups').delete()
            .eq('id', groupId)
            .eq('user_id', session.user.id);
        }
      } catch (fe: unknown) {
```

- **AppLogger:** `src/services/AppLogger.ts:652` (VERIFIED live 2026-06-10)

```
# Cited Truth — AppLogger.ts:650-656 (VERIFIED 2026-06-10):
        let userId: string | undefined = undefined;
        try {
          const { data: { user } } = await supabase.auth.getUser();  // LINE 652
          userId = user?.id;
        } catch {
          // ignore
        }
```

---

## Implementation Steps

### Step 1: Fix GroupRepository.ts — deleteGroup() cloud fallback

- **File:** `src/services/GroupRepository.ts`
- **View first:** Lines 111–163 (full `deleteGroup` method)
- **Problem:** `deleteGroup(groupId: string)` calls `supabase.auth.getSession()` at line 153 to get the `userId` for the fallback DELETE. The `userId` should instead be passed as a parameter.
- **Change:**
  1. Update the method signature from `deleteGroup(groupId: string)` to `deleteGroup(groupId: string, userId?: string)`.
  2. In the fallback block (lines 152–161), replace the `supabase.auth.getSession()` call with a direct use of the injected `userId` parameter:
  ```typescript
  // BEFORE (lines 152-158):
  const { data: { session } } = await supabase.auth.getSession();
  if (session?.user) {
    await supabase.from('registered_groups').delete()
      .eq('id', groupId)
      .eq('user_id', session.user.id);
  }

  // AFTER:
  if (userId) {
    await supabase.from('registered_groups').delete()
      .eq('id', groupId)
      .eq('user_id', userId);
  }
  ```
  3. Remove the `supabase.auth.getSession()` import if it becomes unused (Boy Scout).
- **Update callers:** `useDashboardGroups.ts:339` and `useDashboardGroups.ts:353` both call `GroupRepository.getInstance().deleteGroup(id)`. Update both to `deleteGroup(id, user?.id)` where `user` comes from `useAuth()` or the AuthContext already available in the hook.
- **Also update:** `DeviceRepository.ts:449` calls `GroupRepository.getInstance().deleteGroup(groupId)` — update to pass `userId` if available, otherwise `undefined` (safe fallback: fallback block will no-op without userId rather than making a stale auth call).
- **Verify:** `grep -rn "supabase.auth.getSession\|supabase.auth.getUser" src/services/GroupRepository.ts` → zero results.

### Step 2: Fix AppLogger.ts — uploadLogsToSupabase() userId fetch

- **File:** `src/services/AppLogger.ts`
- **View first:** Lines 594–686 (`uploadLogsToSupabase` method)
- **Problem:** `uploadLogsToSupabase()` calls `supabase.auth.getUser()` at line 652 to stamp `user_id` on telemetry rows. The method has no way to receive userId externally today.
- **Change:** Add an optional `userId?: string` parameter to `uploadLogsToSupabase(userId?: string)` and short-circuit the `getUser()` call when `userId` is provided:
  ```typescript
  // BEFORE (lines 650-656):
  let userId: string | undefined = undefined;
  try {
    const { data: { user } } = await supabase.auth.getUser();
    userId = user?.id;
  } catch {
    // ignore
  }

  // AFTER:
  let resolvedUserId: string | undefined = userId;
  if (!resolvedUserId) {
    try {
      const { data: { user } } = await supabase.auth.getUser();
      resolvedUserId = user?.id;
    } catch {
      // ignore — offline or unauthenticated
    }
  }
  // Then replace `userId` references below with `resolvedUserId`
  ```
- **Update callers:** Search for all callers of `AppLogger.uploadLogsToSupabase()` (typically called in `useGlobalTelemetry.ts` or similar on app foreground). Update each call site to pass `user?.id` from AuthContext.
- **Verify:** `grep -rn "supabase.auth.getUser\|supabase.auth.getSession" src/services/AppLogger.ts` → zero results (or only the backward-compat fallback path when `userId` is undefined).

### Step 3: Final verification scan

- **Action:**
  ```powershell
  grep -rn "supabase.auth.getUser\(\)\|supabase.auth.getSession\(\)" src/services/GroupRepository.ts src/services/AppLogger.ts
  ```
- **Verify:** Zero results in both files. TSC clean. Jest passes.

---

## Out of Scope

- Do NOT touch `AuthContext.tsx` internals — only modify the two service files above plus their direct callers.
- Do NOT refactor the other 23 `supabase.auth.getUser()` call sites in `CrewService.ts`, `CrewProfileService.ts`, `DeviceRepository.ts`, etc. — those belong to the larger `PLAN-AUTH-CONTEXT-BYPASS-REFACTOR.md` task.
- Do NOT change the telemetry row schema or Supabase table structure.

---

## Risk Assessment

- **Risk Level:** M-RISK
- **Size:** Meal
- **Layer:** CLOUD / SERVICE
- **Blast Radius:** `GroupRepository.ts`, `AppLogger.ts`, `useDashboardGroups.ts` (2 lines), `DeviceRepository.ts` (1 line), and the `uploadLogsToSupabase()` call site.
- **Rollback:** `git checkout -- src/services/GroupRepository.ts src/services/AppLogger.ts`

---

## Verification Checklist

1. `npm run verify` → TSC ✅ Jest ✅ all gates green ✅
2. `grep -rn "supabase.auth.getSession" src/services/GroupRepository.ts` → zero results ✅
3. `grep -rn "supabase.auth.getUser" src/services/AppLogger.ts` → zero results (or fallback-only path) ✅
4. `useDashboardGroups.ts` builds — `deleteGroup(id, user?.id)` compiles without type error ✅
