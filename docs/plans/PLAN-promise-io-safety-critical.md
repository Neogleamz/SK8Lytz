# Implementation Plan

## Task: fix/promise-io-safety-critical
**Cluster:** C — Promise & IO Safety (HIGH severity subset)  
**Batch:** `[BATCH:promise-io-safety-sweep]`  
**Rule Violated:** R-11  
**Severity:** HIGH × 5  
**Risk:** L-RISK  
**Size:** Snack (5 targeted async wrap additions)

## Cited Truth
- `src/hooks/useAdminTelemetry.ts:49` — `clearLogs()` calls `AsyncStorage.removeItem()` fire-and-forget (audit R11-002)
- `src/hooks/useAdminTelemetry.ts:55` — `AppLogger.exportJSON()` called outside try block (audit R11-003)
- `src/context/SessionContext.tsx:240` — `setupNotification()` async call with no `.catch()` (audit R11-010)
- `src/screens/Onboarding/HardwareSetupWizardScreen.tsx:64` — `fireOrientationTest` loops `pingDevice` without catch (audit R11-016)
- `src/screens/Onboarding/HardwareSetupWizardScreen.tsx:601` — Setup complete `onPress` has `try/finally` but no `catch` (audit R11-017)
- Source: `artifacts/system_audit_report.md` Cluster C lines 172–194

## Source of Truth
- Audit: `artifacts/system_audit_report.md` — Cluster C, HIGH findings
- R-11 Raw: `artifacts/deepdive_raw/R-11_findings.json`

## Problem
5 async operations in critical user flows (admin log clear, session notification setup, hardware orientation test, setup completion) have no error handling. An unhandled rejection in any of these paths either:
1. Crashes the async flow silently (user sees a stuck spinner)
2. Causes app-level "Unhandled promise rejection" warnings visible in production crash reporters

## Implementation Steps

### Fix 1 — `useAdminTelemetry.ts:49` — clearLogs AsyncStorage
**Before (conceptual):** `AsyncStorage.removeItem(key)` without catch  
**After:** Wrap in `try/catch`; log failure via `AppLogger.warn('[AdminTelemetry] Failed to clear logs', ...)`

### Fix 2 — `useAdminTelemetry.ts:55` — exportJSON outside try
**After:** Move `AppLogger.exportJSON()` inside an existing or new `try/catch` block with `AppLogger.error` on catch.

### Fix 3 — `SessionContext.tsx:240` — setupNotification fire-and-forget
**After:** `setupNotification().catch(err => AppLogger.error('[SessionContext] setupNotification failed', err instanceof Error ? err.message : String(err)));`

### Fix 4 — `HardwareSetupWizardScreen.tsx:64` — pingDevice loop
**After:** Wrap `pingDevice` call inside the orientation test loop in `try/catch`; on catch set an error state that shows user feedback ("Device not responding, retrying...").

### Fix 5 — `HardwareSetupWizardScreen.tsx:601` — Setup onPress missing catch
**After:** Add `catch (err: unknown)` block that: (1) logs via `AppLogger.error`, (2) sets a local `setupError` state string displayed to the user.

## Verification Plan
- `view_file` each target line BEFORE editing to confirm current state
- After each fix: `git diff HEAD` to verify only the targeted lines changed
- `npm run verify` — TSC must pass, no new lint errors
- Manual: trigger each code path in dev mode and confirm no "Unhandled promise rejection" in Metro logs
