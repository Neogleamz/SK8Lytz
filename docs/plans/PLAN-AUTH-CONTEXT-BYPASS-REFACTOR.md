# Implementation Plan
# PLAN-AUTH-CONTEXT-BYPASS-REFACTOR

## Summary
25 direct `supabase.auth.getUser()` calls in service layer files bypass the `AuthContext`, firing individual network requests to the Supabase auth server on every operation. Since services are called frequently, this creates 25 redundant auth fetch paths and breaks the offline-first pattern when auth state is already available in context.

**Batch:** `BATCH:auth-context-cleanup`
**Status:** `[✅ VERIFIED]`

---

## Source of Truth Files
- `src/services/CrewProfileService.ts` — Lines 31, 68, 111, 136, 161, 211, 245, 257 (R-15-001 to R-15-008)
- `src/services/CrewService.ts` — Lines 76, 157, 201, 319, 427, 496, 597 (R-15-009 to R-15-015)
- `src/services/DeviceRepository.ts` — Lines 187, 329, 452, 559, 598 (R-15-016 to R-15-020)
- `src/services/GroupRepository.ts` — Line 228 (R-15-021)
- `src/services/NotificationService.ts` — Lines 84, 111 (R-15-022, R-15-023)
- `src/services/ScenesService.ts` — Lines 91, 113 (R-15-024, R-15-025)
- Raw audit: `R-15_findings.json`

---

## Findings

| Files Affected | Instance Count | Pattern |
|---------------|---------------|---------|
| `CrewProfileService.ts` | 8 | `supabase.auth.getUser()` |
| `CrewService.ts` | 7 | `supabase.auth.getUser()` |
| `DeviceRepository.ts` | 5 | `supabase.auth.getUser()` |
| `GroupRepository.ts` | 1 | `supabase.auth.getUser()` |
| `NotificationService.ts` | 2 | `supabase.auth.getUser()` |
| `ScenesService.ts` | 2 | `supabase.auth.getUser()` |

**Total: 25 call sites**

---

## Implementation Approach

Services cannot directly use React hooks (`useAuth()`). The correct pattern is **dependency injection** — pass the `user` object as a parameter from the calling hook or context.

### Option A — Parameter Injection (Preferred, Surgical)
Add optional `userId?: string` parameter to service methods. Callers (hooks with AuthContext) pass it in. Services fall back to `getUser()` only when `userId` is not provided.

```typescript
// BEFORE
async createCrew(inviteCode: string) {
  const { data: { user } } = await supabase.auth.getUser();
  if (!user) throw new Error('Not authenticated');
  ...
}

// AFTER
async createCrew(inviteCode: string, userId: string) {
  // userId provided by caller via useAuth()
  ...
}
```

### Option B — Singleton Auth Cache (Alternative)
Create `AuthCacheService` that mirrors the auth state from `AuthContext` into a module-level singleton. Services read from the cache. Cache is populated by `AuthContext.onAuthStateChange`.

> [!NOTE]
> Option A is preferred for surgical scope. Option B requires larger architectural change. Sage should use Option A unless a calling site makes it impractical.

---

## Implementation Steps

### Step 1 — Audit callers of each affected service method
For each service method with `getUser()`, grep for its callers to understand what hook/component calls it:
```powershell
grep -rn "CrewProfileService\.\|CrewService\." src/hooks/ src/components/
```

### Step 2 — Add userId parameter to service methods
Starting with the lowest-callers-per-method files (GroupRepository, NotificationService, ScenesService → then DeviceRepository → then Crew services).

### Step 3 — Update callers to pass user.id from useAuth()
Each hook that calls a service now passes `const { user } = useAuth(); service.method(data, user?.id)`.

### Step 4 — Add guard at service boundary
```typescript
async createCrew(inviteCode: string, userId: string) {
  if (!userId) throw new Error('[CrewProfileService] userId required — auth context missing');
  ...
}
```

### Step 5 — Remove `supabase.auth.getUser()` from each service method
After Step 3-4 are complete for all callers of a given method.

---

## Batching
Batch by service file — each service file is one sub-task. Execute sequentially:
1. `ScenesService.ts` (2 instances — smallest)
2. `NotificationService.ts` (2 instances)
3. `GroupRepository.ts` (1 instance)
4. `DeviceRepository.ts` (5 instances)
5. `CrewService.ts` (7 instances)
6. `CrewProfileService.ts` (8 instances — largest)

---

## Verification
- `npm run verify`
- `grep -rn "supabase.auth.getUser()" src/services/` → should return 0
- Auth context test: Sign in → trigger crew creation → confirm no redundant auth fetch in network inspector

## Kanban Task Tags
- `[Status: 🔥 ON DECK]`
- `[Verification Status: ✅ VERIFIED]`
- `[Layer: Services]`
- `[Risk: M-RISK]`
- `[Size: Banquet]`
- `[Cognitive Load: Medium]`
- `[BATCH: auth-context-cleanup]`
