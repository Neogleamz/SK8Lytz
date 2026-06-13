# Implementation Plan — `fix/auth-context-bypass`

## Goal
Route all 4 direct `supabase.auth.*` calls in UI components through `AuthContext` methods, eliminating the auth bypass smell and enabling centralized auth testing/mocking.

## Source of Truth
- `artifacts/deepdive_raw/DOMAIN_IDENTITY_findings.json` lines 122–148
- `artifacts/deepdive_raw/R-15_findings.json`
- `src/context/AuthContext.tsx` — existing context API
- `artifacts/system_audit_report.md` §R-15

## Out of Scope
- Rewriting AuthContext internals
- Changing Supabase session handling
- Any non-auth-related changes in touched files

---

## Step 1 — Audit AuthContext public API
- **Action:** `view_file src/context/AuthContext.tsx` — identify all exported methods and their signatures
- **Verify:** Confirm `signIn`, `signOut`, `signUp`, `resetPassword` methods exist (or note what's missing and needs to be added).

## Step 2 — Add missing methods to AuthContext (if needed)
- **Action:** If `resetPassword` or `signUp` are not exposed, add them to `AuthContext` with the same signature as the direct supabase calls they replace.
- **Verify:** `AuthContext` exports all 4 required methods. TSC passes.

## Step 3 — Fix `AuthFormSignIn.tsx:73`
- **Action:** Replace `supabase.auth.signInWithPassword(...)` with `const { signIn } = useAuthContext(); await signIn(...)`
- **Source:** `DOMAIN_IDENTITY_findings.json` line 124
- **Verify:** No `supabase.auth.signIn` in this file. Uses `useAuthContext()`.

## Step 4 — Fix `AuthFormSignUp.tsx:106`
- **Action:** Replace `supabase.auth.signUp(...)` with `const { signUp } = useAuthContext(); await signUp(...)`
- **Source:** `DOMAIN_IDENTITY_findings.json` line 131
- **Verify:** No direct supabase auth call in this file.

## Step 5 — Fix `AuthFormForgotPassword.tsx:38`
- **Action:** Replace `supabase.auth.resetPasswordForEmail(...)` with `const { resetPassword } = useAuthContext(); await resetPassword(...)`
- **Source:** `DOMAIN_IDENTITY_findings.json` line 139
- **Verify:** No direct supabase auth call in this file.

## Step 6 — Fix `useDashboardProfile.ts:113`
- **Action:** Replace `supabase.auth.signOut()` with `const { signOut } = useAuthContext(); await signOut()`
- **Source:** `DOMAIN_IDENTITY_findings.json` line 147
- **Verify:** No direct supabase auth call in this file.

## Step 7 — Final scan
- **Action:** `grep -rn "supabase.auth\." src/components/ src/hooks/` | grep -v "AuthContext"
- **Verify:** Zero results — all auth calls in UI layer go through context.

## Step 8 — TSC check
- **Action:** `npx tsc --noEmit`
- **Verify:** Zero new errors.
