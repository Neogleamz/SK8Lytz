# Implementation Plan

## Task: fix/gatt-race-conditions
## Cluster: TC-11
## Risk: [H-RISK] | Size: [Meal] | Layer: [BLE]
## Status: [⚪ READY]

---

## Problem Statement

Three active re-entrancy race conditions in the BLE layer plus two GATT collision bugs in Diagnostic Lab:

**Race 1 (HIGH):** `BatterySweep.ts:135` — `burstTimerRef.current` is overwritten without clearing the previous timeout. Concurrent calls strand the first timeout, terminating the sweep prematurely.

**Race 2 (HIGH):** `useBLE.ts:410` — `pingDevice` saves `wasSweeperActive` as a local var. If two concurrent calls run, T1 resumes the sweeper while T2's GATT ping is still in-flight → GATT 133 error.

**Race 3 (MEDIUM):** `BleConnectionManager.ts:47` — `allRequestedAlreadyConnected` evaluated before `acquireGattLock`. Concurrent calls read stale optimistic cache → both proceed to connect → GATT collision.

**Race 4 (MEDIUM):** `useDashboardGroups.ts:235` — `setPowerState` reads React state for toggle logic. Rapid double-taps send same power command twice instead of toggling.

**DiagLab Collision (HIGH×2):** `Sk8LytzDiagnosticLab.tsx:178` and `DiagnosticLabBuilderTab.tsx:73` — `Promise.all` on group writes bypasses `BleWriteQueue`, causing GATT 133 collisions on Android.

---

## Source of Truth

- **`artifacts/deepdive_raw/R-26_findings.json`** → R-26-001 (`BatterySweep:135`, HIGH), R-26-002 (`BleConnectionManager:47`, MEDIUM), R-26-003 (`useDashboardGroups:235`, MEDIUM), R-26-004 (`useBLE:410`, HIGH)
- **`artifacts/deepdive_raw/R-10_findings.json`** → BLE_CORE-010-1 (`Sk8LytzDiagnosticLab:178`, HIGH), BLE_CORE-010-2 (`DiagnosticLabBuilderTab:73`, HIGH)
- **`tools/SK8Lytz_App_Master_Reference.md`** — BleWriteQueue as the serialized gateway for all GATT writes

---

## Proposed Changes

### [MODIFY] `src/hooks/ble/useBLEBatterySweep.ts:135`
```diff
+ if (burstTimerRef.current) clearTimeout(burstTimerRef.current);
  burstTimerRef.current = setTimeout(() => { ... }, durationMs);
```

### [MODIFY] `src/hooks/useBLE.ts:410`
Move sweeper pause/resume inside the GATT lock scope using a ref counter:
```typescript
const sweeperPauseCountRef = useRef(0);
// In pingDevice:
sweeperPauseCountRef.current++;
if (sweeperPauseCountRef.current === 1) scanner.pauseSweeper();
// ... GATT ping ...
sweeperPauseCountRef.current--;
if (sweeperPauseCountRef.current === 0) scanner.startSweeper();
```

### [MODIFY] `src/services/BleConnectionManager.ts:47`
Move the `allRequestedAlreadyConnected` check inside the try block, after `acquireGattLock`:
```typescript
const lockHandle = await acquireGattLock(1);
try {
  const allRequestedAlreadyConnected = devices.every(...); // moved here
  if (allRequestedAlreadyConnected) { ... return; }
  // ... connect logic
}
```

### [MODIFY] `src/hooks/useDashboardGroups.ts:235`
```diff
- const targetState = forceState !== undefined ? forceState : !(powerStates[deviceIds[0]] ?? true);
- const newStates = { ...powerStates };
+ setPowerStates(prev => {
+   const targetState = forceState !== undefined ? forceState : !(prev[deviceIds[0]] ?? true);
+   return { ...prev, ...Object.fromEntries(deviceIds.map(id => [id, targetState])) };
+ });
```

### [MODIFY] `src/components/admin/tools/Sk8LytzDiagnosticLab.tsx:178`
Replace `Promise.all(connectedDevices.map(...))` with sequential for…of + 50ms gap.

### [MODIFY] `src/components/admin/tools/tabs/DiagnosticLabBuilderTab.tsx:73`
Same fix as above — for…of sequential writes.

---

## Verification Plan

### Automated
- `npm run verify` — TSC clean

### Manual
1. Connect 3 devices → trigger group battery sweep rapidly (x3) → confirm sweep completes fully without premature termination
2. Trigger `pingDevice` rapidly on a connected device → confirm no GATT 133 in ADB logcat
3. Rapid double-tap power button for a group → confirm alternating power state (not same state twice)
4. Open DiagLab → trigger a group command → ADB logcat must show no GATT 133 errors

---

## Worktree
`C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz-worktrees\gatt-race-conditions\`
Branch: `fix/gatt-race-conditions`
