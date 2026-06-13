# Implementation Plan: audit-fixes-auth

**Slug:** `fix/audit-fixes-auth`
**Batch:** `[BATCH:audit-fixes-auth]`
**Source Audit:** [functional_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/8a264849-d4ac-4256-8a34-6d95511cb1d0/functional_audit_report.md)
**Findings Addressed:** H1, M5, L1, L2

---

## Background

A full-stack functional audit on 2026-06-09 traced the complete auth path (user creation → sign in → sign up → password reset → profile creation). Four bugs found:
- **H1 (HIGH):** Offline stub missing `rpc` method — crashes on username login offline
- **M5 (MEDIUM):** `AuthProfileService.fetchOrCreateProfile` swallows errors silently
- **L1 (LOW):** Dead `safeErr` variable assignments never referenced
- **L2 (LOW):** Duplicated dead ternary `error ? error.message : String(error)` where `error` is already a string

---

## Step 1 — Fix H1: Add `rpc` stub to offline fallback client

**Source:** `src/services/supabaseClient.ts:48–64` (offline stub definition)
**Source:** `src/components/auth/AuthFormSignIn.tsx:57–62` (call site)

### Action
In `supabaseClient.ts`, inside the offline stub object (the one returned when `SUPABASE_URL` is falsy or offline), add the `rpc` method immediately after the existing `auth` stub block.

```typescript
rpc: async (_fn: string, _args?: object) => ({
  data: null,
  error: new Error('Offline mode — network unavailable. Please use email to sign in.'),
}),
```

**Rationale:** The call at `AuthFormSignIn.tsx:57` is `supabase.rpc('get_email_by_username', { username })`. Without `rpc` on the stub, this throws `TypeError: supabase.rpc is not a function`. The stub must return the same `{ data, error }` shape as the real Supabase client.

**Verify:**
- `tsc --noEmit` — 0 errors
- Manually confirm: put the device in airplane mode, attempt username login → should see "Offline mode" error toast, NOT a crash
- Jest: run `__tests__/auth/supabaseClient.test.ts` if it exists; otherwise confirm no TS error on the new stub

---

## Step 2 — Fix M5: Add AppLogger call to AuthProfileService catch block

**Source:** `src/services/AuthProfileService.ts:77–82`

The catch block already imports AppLogger at L11 (`import { AppLogger } from './AppLogger'`). The comment on L79 says "I should import it" — it's already imported, just never called.

### Action
Before the `throw new Error(msg)` at line 82, add:
```typescript
AppLogger.error('[AuthProfileService] fetchOrCreateProfile failed', { error: msg });
```

**Verify:**
- `tsc --noEmit` — 0 errors
- Grep confirms AppLogger.error is called in the catch block

---

## Step 3 — Fix L1: Remove dead `safeErr` variables

**Source:** `src/services/supabaseClient.ts:21,30` — `const safeErr = e instanceof Error ? e : new Error(String(e));` — assigned, never used
**Source:** `src/context/SessionContext.tsx:44, 323, 401` — same pattern

### Action
In each location, delete the `const safeErr = ...` assignment. The variable is not referenced by anything in the catch block. The `String(e)` or `e instanceof Error ? e.message : String(e)` pattern can be inlined directly at the AppLogger call site.

**Verify:**
- `tsc --noEmit` — 0 errors (safeErr should produce `no-unused-vars` lint if eslint is active)
- Grep: `rg "safeErr" src/` returns 0 results in non-test files

---

## Step 4 — Fix L2: Remove duplicated dead ternary in auth forms

**Source:** `src/components/auth/AuthFormSignUp.tsx` — audit flagged duplicated ternary `error ? error.message : String(error)` where `error` is already typed as `string`
**Source:** `src/components/auth/AuthFormSignIn.tsx` — same pattern

### Action
Read the exact lines in both files (Look-Before-Leap). Where `error` is already a `string` type and the ternary `error ? error.message : String(error)` is used, simplify to just `error`. Apply Boy Scout rule: do not change any other lines.

**Verify:**
- `tsc --noEmit` — 0 errors
- No regressions in existing auth error display paths

---

## Step 5 — Verify & Commit

```bash
npm run verify   # TSC + Jest + AST + TypeSafety + WorkflowValidator
```
- TSC: 0 errors ✅
- Jest: 126/126 ✅
- Commit message: `fix(auth): add offline rpc stub, fix silent profile error, remove dead safeErr vars`

---

## Out of Scope
- Any changes to Supabase RLS or database functions
- Refactoring AuthContext or AuthScreen beyond the targeted lines
- Username login UX redesign (that's a product decision, not a bug fix)
- `DashboardScreen.tsx` or `DockedController.tsx` (God Object — blocked by `refactor/god-object-split`)
