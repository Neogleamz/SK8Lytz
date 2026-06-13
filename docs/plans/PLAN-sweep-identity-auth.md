# Implementation Plan

## Task: sweep-identity-auth
**Slug:** sweep-identity-auth
**Wave:** [WAVE:3] — Prerequisite: Wave 2 fully merged
**Size:** [Snack] — 5 files
**Risk:** [H-RISK] — Auth context and core type definitions
**Status:** [✅ READY]
**Source of Truth:** `artifacts/system_audit_report.md` + `artifacts/deepdive_raw/DOMAIN_IDENTITY_findings.json` + `artifacts/deepdive_raw/R-15_findings.json`
**Prerequisite:** Wave 2 fully merged

## Goal
Fix 12 findings in the identity and auth layer. Critical: `ProfileService.types.ts` declares `notif_preferences: any` in a core type — this poisons every component that uses the profile type. `AppLogger.ts` calls `supabase.auth.getUser()` directly, bypassing `AuthContext` and executing an unnecessary network request on every log flush. Fix 4 AppLogger calls in `AuthProfileService` and `useAccountOverview` where `payload_size` and `ssi` are incorrectly nested inside the message string. Fix 2 direct `supabase.auth.updateUser()` calls in `AccountModal` that bypass the AuthContext abstraction.

## Decision Log
- **`notif_preferences: any` (R-08 HIGH)**: This is a core type field used throughout the app. Keeping it as `any` means every notification preference access is untyped. Must define a proper `NotifPreferences` interface and update the type.
- **`AppLogger` direct Supabase auth call (R-15 HIGH)**: `AppLogger.ts` L674 calls `supabase.auth.getUser()` to attach user context to telemetry. This fires a network request on every log flush cycle. Must use cached user from `AuthContext` or a module-level singleton instead.
- **`AccountModal` direct auth calls (R-15 LOW)**: `supabase.auth.updateUser()` called directly for password and email updates. These must go through `AuthContext.updateUser()` to keep auth state synchronized across the app.
- **Telemetry context nesting (R-04)**: `AuthProfileService` and `useAccountOverview` pass `{ payload_size, ssi }` as a nested object inside the message, not as the second argument to `AppLogger.error()`. The Supabase schema never receives these fields.

## Files to Create/Modify

### [MODIFY] src/services/ProfileService.types.ts
- Define `NotifPreferences` interface with typed fields (mute, push, email, etc.)
- Replace `notif_preferences: any` at L21 with `notif_preferences: NotifPreferences` (R-08)

### [MODIFY] src/services/AppLogger.ts
- L674: Remove direct `supabase.auth.getUser()` call — replace with a module-level `setCurrentUser(user)` setter that AuthContext calls on login/logout, storing user in a local variable used by the logger

### [MODIFY] src/services/AuthProfileService.ts
- L79: Fix `AppLogger.error` — move `payload_size` and `ssi` to the second argument context object (R-04)
- L155: Fix `AppLogger.error` — add missing `payload_size` and `ssi` context (R-04)

### [MODIFY] src/hooks/useAccountOverview.ts
- L178, L221, L307: Fix 3× `AppLogger.error` calls — move `payload_size` and `ssi` to context object (R-04)
- L232: Fix `e as any` cast in catch block — use `e instanceof Error ? e.message : String(e)` (R-08)

### [MODIFY] src/components/AccountModal.tsx
- L344: Replace `supabase.auth.updateUser()` (password update) with `AuthContext.updateUser()` call (R-15)
- L362: Replace `supabase.auth.updateUser()` (email update) with `AuthContext.updateUser()` call (R-15)

## Out of Scope
- `AuthContext.tsx` internals beyond adding `updateUser` method if missing
- `src/components/auth/AuthFormSignIn.tsx` `isValidEmail` dedup (handled in `sweep-shared-utils`, Wave 5)
- No BLE changes

## Verification Plan
- `npm run verify` — TSC must pass; `NotifPreferences` must resolve everywhere `notif_preferences` is accessed
- Verify no direct `supabase.auth` calls remain in `AppLogger.ts` via `grep supabase.auth src/services/AppLogger.ts`
