# Implementation Plan

## Task: fix/memory-leak-hardware-notifications
**Cluster:** D — Memory Leaks (Event Listener)  
**Batch:** `[BATCH:memory-leak-sweep]`  
**Rule Violated:** R-17  
**Severity:** HIGH  
**Risk:** L-RISK  
**Size:** Snack (2-line unmount cleanup addition)

## Cited Truth
- Audit R17-001: `src/hooks/useHardwareNotifications.ts` — registers `setOnDataReceived` and `setOnHardwareProbed` BLEContext callbacks on mount, never clears on unmount
- Source: `artifacts/system_audit_report.md` Cluster D lines 208 ("Every time a user navigates away from and back to DashboardScreen, a new closure is attached, and the old DashboardScreen subtree is kept alive")

## Source of Truth
- Audit: `artifacts/system_audit_report.md` — Cluster D, finding R17-001
- Files to read before editing: `src/hooks/useHardwareNotifications.ts` (full), `src/context/BLEContext.tsx` (setOnDataReceived/setOnHardwareProbed API)

## Problem
`useHardwareNotifications.ts` registers BLEContext callbacks on mount. Without a cleanup return function, every navigation away from and back to DashboardScreen registers a SECOND callback, keeping the old DashboardScreen subtree alive. This is a per-navigation memory leak that compounds indefinitely.

## Implementation Steps

### Step 1 — Read target files first (P1 mandatory)
- `view_file` `src/hooks/useHardwareNotifications.ts` — identify the `useEffect` that registers callbacks
- `view_file` `src/context/BLEContext.tsx` — identify the `setOnDataReceived` / `setOnHardwareProbed` setter API to understand how to clear (set to `null` or `() => {}`)

### Step 2 — Add cleanup return
In the `useEffect` that calls `setOnDataReceived(handler)` and `setOnHardwareProbed(handler)`, add a return cleanup function:
```ts
return () => {
  setOnDataReceived(null);      // or () => {} if null is not accepted
  setOnHardwareProbed(null);
};
```

### Step 3 — Post-edit diff check
`git diff HEAD` — verify only the cleanup return was added, no other lines touched.

## Verification Plan
- TSC no-emit passes
- Manual: Navigate away from and back to Dashboard 5× in dev mode. Metro `logcat` must NOT show "Cannot update state of an unmounted component" warnings
- `npm run verify`

## VERIFIED: pre-existing — sweep/memory-lifecycle 2026-06-30

Both cleanup returns (`setOnDataReceived(() => {})` and `setOnHardwareProbed(() => {})`) were already
present in `src/hooks/useHardwareNotifications.ts` (lines 208–210, 241–243) as of commit `c795df69`
(`fix: complete memory-leak-sweep`, 2026-06-10). No code change required. Verification run confirmed
TSC + Jest gates pass at HEAD `8421109b`.
