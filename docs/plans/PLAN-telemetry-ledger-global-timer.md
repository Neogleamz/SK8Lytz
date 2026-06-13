# Implementation Plan

## Task: fix/telemetry-ledger-global-timer
**Cluster:** D — Memory Leaks (Global Timer Instance Leak)  
**Batch:** `[BATCH:memory-leak-sweep]`  
**Rule Violated:** R-17  
**Severity:** MEDIUM  
**Risk:** M-RISK  
**Size:** Meal (instance counter refactor — moderate logic change)

## Cited Truth
- Audit R17-003: `src/hooks/useTelemetryLedger.ts` — Module-level `globalFlushTimer` is shared across multiple hook instances. When any single instance unmounts (e.g., `DockedController`), it clears the interval for ALL instances, breaking the 15-minute background telemetry flush permanently.
- Source: `artifacts/system_audit_report.md` Cluster D lines 212

## Source of Truth
- Audit: `artifacts/system_audit_report.md` — Cluster D, finding R17-003
- File to read: `src/hooks/useTelemetryLedger.ts` (identify `globalFlushTimer`, instance lifecycle)

## Problem
Module-level timer shared across all instances. When `DockedController` unmounts, it calls `clearInterval(globalFlushTimer)`, killing the flush for `SessionContext` which is still mounted. Background telemetry stops uploading permanently for the session.

## Implementation Steps

### Step 1 — Read target file (P1 mandatory)
`view_file` `src/hooks/useTelemetryLedger.ts` — identify:
- Where `globalFlushTimer` is declared and started
- The `useEffect` cleanup that clears it
- Whether an instance counter ref already exists

### Step 2 — Add instance reference counter
```ts
// Module-level (outside the hook)
let _instanceCount = 0;
let _sharedFlushTimer: ReturnType<typeof setInterval> | null = null;

function startSharedTimer(callback: () => void) {
  if (_sharedFlushTimer === null) {
    _sharedFlushTimer = setInterval(callback, FLUSH_INTERVAL_MS);
  }
}

function stopSharedTimer() {
  _instanceCount--;
  if (_instanceCount === 0 && _sharedFlushTimer !== null) {
    clearInterval(_sharedFlushTimer);
    _sharedFlushTimer = null;
  }
}
```

### Step 3 — Update hook lifecycle
In the hook's `useEffect`:
- On mount: `_instanceCount++; startSharedTimer(flush);`
- On unmount: `stopSharedTimer();`

### Step 4 — Remove old `globalFlushTimer` module variable
Replace with the `_sharedFlushTimer` pattern above.

## Risk Note
M-RISK because the global `flush` callback must be stable (same reference across all instances) or the timer must call a stable module-level flush aggregator. Read `useTelemetryLedger.ts` carefully — if `flush` closes over hook-local state, the shared timer pattern needs adaptation (e.g., each instance registers its own flush and the timer calls all registered flushes).

## Verification Plan
- TSC no-emit passes
- Manual: Open DockedController (mounts hook) → navigate away (unmounts) → verify SessionContext still triggers telemetry flush after 15 minutes in dev mode
- `npm run verify`
