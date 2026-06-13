# Implementation Plan: audit-fixes-ble-signal

**Slug:** `fix/audit-fixes-ble-signal`
**Batch:** `[BATCH:audit-fixes-ble-signal]`
**Source Audit:** [functional_audit_report.md](file:///C:/Users/Magma/.gemini/antigravity/brain/8a264849-d4ac-4256-8a34-6d95511cb1d0/functional_audit_report.md)
**Findings Addressed:** M1 (FSM dead states), M2 (RSSI stale prune), L0 (wrong power opcode), L3 (duplicate useMemo dep), L6 (onWeakSignal not wired)

---

## Background

Five related BLE signal/state issues found in the core BLE stack — all in the same service/hook cluster. Combined into one batch because they all touch `useBLE.ts`, `BleMachine.ts`, `BleConnectionManager.ts`, `useBLERSSIMonitor.ts`, and `BleWriteQueue.ts`.

---

## Cited Truth

```
Source: src/services/ble/BleMachine.ts
  // CONNECT_SUCCESS event defined but never fired
  // DISCONNECT_COMPLETE event defined but never fired
  // FSM transitions: IDLE → CONNECTING → IDLE (via FORCE_IDLE)
  // READY state: unreachable in normal flow

Source: src/services/BleConnectionManager.ts:295-310
  // setGate('IDLE') called directly after handshake — CONNECT_SUCCESS never fired
  // No bleSend({ type: 'CONNECT_SUCCESS' }) anywhere in file

Source: src/hooks/ble/useBLERSSIMonitor.ts:120-134
  // Prune useEffect depends on [bleManager]
  // bleManager is a singleton — never changes after mount
  // Prune fires ONCE on mount and never again

Source: src/hooks/ble/useBLERSSIMonitor.ts:99-104
  // onCriticalSignal callback fired (wired in useBLE.ts:469-475)
  // onWeakSignal callback fired but NOT wired at useBLE.ts:466-476

Source: src/services/BleWriteQueue.ts:52
  // resolveWritePriority maps 0xCC as power opcode → 'critical'
  // Actual Zengge power opcode: 0x71 (ZenggeProtocol.ts:829,833)
  // 0xCC is never emitted anywhere in the protocol

Source: src/hooks/useBLE.ts:667,673
  // bleGateState appears TWICE in the useMemo deps array
  // React deduplicates before diff — harmless at runtime but violates react-hooks/exhaustive-deps
```

---

## Step 1 — Fix M1: Fire `CONNECT_SUCCESS` from BleConnectionManager

**Source:** `src/services/BleConnectionManager.ts` — view lines 290-320 first

After `setGate('IDLE')` is called following a successful handshake batch, fire the FSM event:

```typescript
// After: setGate('IDLE');
bleSend({ type: 'CONNECT_SUCCESS', devices: connectedGroup });
```

`bleSend` must be added to the `BleConnectionRequest` interface in `src/types/ble.types.ts`. Check if it's already there before adding.

**Verify:** After connection, `bleActorRef.getSnapshot().value` === `'CONNECTED'` (or `'READY'` per state naming in BleMachine).

---

## Step 2 — Fix M1: Fire `DISCONNECT_COMPLETE` from BleLifecycleManager

**Source:** `src/services/BleLifecycleManager.ts` — view full file first

After `executeRealDisconnect` completes its cleanup (cancel connections, clear refs), fire:
```typescript
bleSend({ type: 'DISCONNECT_COMPLETE' });
```

**Verify:** After disconnect, FSM reaches `DISCONNECTING` state and transitions to `IDLE`.

---

## Step 3 — Fix M2: RSSI stale prune wrong dependency

**Source:** `src/hooks/ble/useBLERSSIMonitor.ts:120-134`

The prune `useEffect` currently depends on `[bleManager]` (stable singleton — never changes). Change to depend on `connectedDeviceIds` — a derived string array of connected device MACs.

```typescript
// BEFORE:
useEffect(() => { ... prune rssiMap ... }, [bleManager]);

// AFTER:
// Accept connectedDeviceIds as a prop (string[]):
useEffect(() => {
  setRssiMap(prev => {
    const pruned = { ...prev };
    Object.keys(pruned).forEach(mac => {
      if (!connectedDeviceIds.includes(mac)) delete pruned[mac];
    });
    return pruned;
  });
}, [connectedDeviceIds]);
```

Add `connectedDeviceIds: string[]` to `UseBLERSSIMonitorParams`. Pass it from `useBLE.ts` where the hook is called (the call site already has `connectedDevices`).

**Verify:** After device disconnect, RSSI badge clears within one render cycle.

---

## Step 4 — Fix L6: Wire `onWeakSignal` at useBLE call site

**Source:** `src/hooks/useBLE.ts:466-476`

The `useBLERSSIMonitor` call at this location passes `onCriticalSignal` but leaves `onWeakSignal` undefined. Wire the weak signal to a logger call (no auto-recovery — weak is not critical):

```typescript
onWeakSignal: (mac: string) => {
  AppLogger.warn('[BLE RSSI] Weak signal detected — consider moving closer', { mac: scrubPII(mac) });
},
```

**Verify:** `onWeakSignal` defined at call site; no undefined reference TS errors.

---

## Step 5 — Fix L0: Correct power opcode in BleWriteQueue

**Source:** `src/services/BleWriteQueue.ts:52`

```typescript
// BEFORE:
0xCC: 'critical', // Power ON/OFF

// AFTER:
0x71: 'critical', // Power ON/OFF (ZenggeProtocol.ts:829,833)
```

Remove the `0xCC` entry, add `0x71`.

**Note:** This changes power writes from `normal` → `critical` priority. This is CORRECT behaviour — power commands should preempt pattern writes in the queue.

**Verify:** `resolveWritePriority(0x71)` === `'critical'`. Grep confirms `0xCC` no longer appears in priority map.

---

## Step 6 — Fix L3: Remove duplicate `bleGateState` in useMemo deps

**Source:** `src/hooks/useBLE.ts:667,673`

Delete the second occurrence of `bleGateState` from the `useMemo` dependency array. View the exact lines before editing.

**Verify:** `rg "bleGateState" src/hooks/useBLE.ts` — appears exactly once in the deps array.

---

## Step 7 — Verify & Commit

```bash
npm run verify
```
- TSC: 0 errors ✅
- Jest: 126/126 ✅
- Commit: `fix(ble): fire FSM events, fix RSSI prune dep, wire weak signal, correct power opcode, remove dup dep`

---

## Out of Scope
- Changing BanlanX or Zengge protocol internals
- Changing BleMachine state names or transitions beyond firing missing events
- RSSI thresholds (not a finding)
- `DashboardScreen.tsx` or `DockedController.tsx` (God Object — blocked)
