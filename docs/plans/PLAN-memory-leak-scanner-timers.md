# Implementation Plan

## Task: fix/memory-leak-scanner-timers
**Cluster:** D — Memory Leaks (Timer Cleanup)  
**Batch:** `[BATCH:memory-leak-sweep]`  
**Rule Violated:** R-12, R-17  
**Severity:** HIGH  
**Risk:** L-RISK  
**Size:** Snack (clearTimeout cleanup additions)

## Cited Truth
- Audit R12-001, R17-002: `src/hooks/ble/useBLEScanner.ts` — `debounceTimerRef` and `telemetryTimerRef` `setTimeout` callbacks not cleared on unmount
- Source: `artifacts/system_audit_report.md` Cluster D lines 210 ("These attempt to call `setAllDevices` on unmounted state")

## Source of Truth
- Audit: `artifacts/system_audit_report.md` — Cluster D, finding R12-001/R17-002
- File to read: `src/hooks/ble/useBLEScanner.ts` (identify debounceTimerRef and telemetryTimerRef usage)

## Problem
`useBLEScanner.ts` uses `debounceTimerRef` and `telemetryTimerRef` for scheduled callbacks. When the hook unmounts (scanner stops), these timer refs are not cleared. The callbacks fire after unmount and call `setAllDevices()` on unmounted state, causing React state update on unmounted component errors and potential crashes.

## Implementation Steps

### Step 1 — Read target file (P1 mandatory)
- `view_file` `src/hooks/ble/useBLEScanner.ts` — identify all `setTimeout` calls that write to the two refs, and the `useEffect` structure

### Step 2 — Identify cleanup site
Locate the existing `useEffect` that manages scanner lifecycle. It should already have a `return () =>` cleanup. If it does, add to it:
```ts
return () => {
  if (debounceTimerRef.current) clearTimeout(debounceTimerRef.current);
  if (telemetryTimerRef.current) clearTimeout(telemetryTimerRef.current);
  // ... existing cleanup
};
```

If there is no cleanup in the relevant `useEffect`, add the full return cleanup.

### Step 3 — Post-edit diff check
`git diff HEAD` — verify only cleanup lines were added.

## Verification Plan
- TSC no-emit passes
- Manual: Start scanner, immediately stop it. Metro logs must NOT show "Can't call setState on unmounted component"
- `npm run verify`
