# Implementation Plan — BLE Phase 1: XState Drives the Radio

**Slug:** `refactor/ble-p1-machine-radio`
**Source of Truth:** `BLE_AUDIT_REPORT.md` §1 Call Graph, `BleMachine.ts`, `useBLEBatterySweep.ts:110`, `useBLEScanner.ts:371`
**Prerequisite:** None

## Goal
Make `BleMachine.ts` the single owner of `startDeviceScan` and `stopDeviceScan`. SCANNING state `entry` starts the radio. SCANNING state `exit` stops it. No other code path in the codebase ever calls these methods directly.

## Out of Scope
- Deleting `useBLEBatterySweep.ts` or `useBLEScanner.ts` (Phase 2)
- Moving connect/recovery into XState services (Phases 3–5)
- Any UI changes

---

## Steps

### Step 1 — Expand `BleMachine.types.ts`
**Source:** `src/services/ble/BleMachine.types.ts:1-32`

Add to `BleMachineContext`:
```typescript
bleManager: BleManager;
scanCallback: (error: BleError | null, device: Device | null) => void;
scanMode: ScanMode;
scanServiceUUIDs: string[];
```

Add new events to `BleMachineEvent` union:
```typescript
| { type: 'SCAN_PAUSE' }
| { type: 'SCAN_RESUME' }
| { type: 'HEARTBEAT_FAIL'; deviceId: string }
```

**Verify:** `npx tsc --noEmit` passes. No `any` casts introduced.

---

### Step 2 — Add Entry/Exit Actions to SCANNING State
**Source:** `src/services/ble/BleMachine.ts:77-91` (SCANNING state)

```typescript
SCANNING: {
  entry: [
    ({ context }) => {
      context.bleManager.startDeviceScan(
        context.scanServiceUUIDs,
        { allowDuplicates: false, scanMode: context.scanMode },
        context.scanCallback
      );
    },
    { type: 'logTransition', params: { from: 'ANY', to: 'SCANNING' } }
  ],
  exit: [
    ({ context }) => {
      context.bleManager.stopDeviceScan();
    }
  ],
  on: {
    SCAN_STOP:          { target: 'IDLE', ... },
    CONNECT_REQUEST:    { target: 'CONNECTING', ... },
    SCAN_PAUSE:         { actions: ({ context }) => context.bleManager.stopDeviceScan() },
    SCAN_RESUME:        { actions: ({ context }) => context.bleManager.startDeviceScan(...) },
    DISCONNECT_REQUEST: { target: 'DISCONNECTING', ... },
    RECOVERY_START:     { target: 'RECOVERING', ... },
  }
}
```

**Verify:** Transition IDLE → SCANNING → IDLE: logcat shows exactly 1 `startDeviceScan` and 1 `stopDeviceScan`. Never 2.

---

### Step 3 — Wire `bleManager` into the Actor at Creation
**Source:** `src/hooks/useBLE.ts` (actor creation — grep for `createActor`)

Pass `bleManager`, `scanCallback`, `scanMode`, `scanServiceUUIDs` as input to the actor:
```typescript
const bleActor = createActor(bleMachine, {
  input: {
    bleManager,
    scanCallback,
    scanMode: ScanMode.LowPower,
    scanServiceUUIDs: [ZENGGE_SERVICE_UUID, BANLANX_SERVICE_UUID],
    ...existingContextFields
  }
});
```

**Verify:** Actor creates without TypeScript errors. Machine context contains `bleManager`.

---

### Step 4 — Comment Out Direct Radio Calls (Do NOT Delete Yet)
**Source:**
- `src/hooks/ble/useBLEBatterySweep.ts:110` — `bleManager.startDeviceScan()`
- `src/hooks/ble/useBLEBatterySweep.ts:180` — `bleManager.stopDeviceScan()`
- `src/hooks/ble/useBLEScanner.ts:371` — `bleManager.startDeviceScan()` (fallback path)

Comment each with: `// PHASE-1: radio now owned by BleMachine.ts SCANNING entry/exit`

**Verify:** App builds. `adb logcat | grep "startDeviceScan\|stopDeviceScan"` shows calls originating only from machine actions.

---

### Step 5 — Smoke Test on Device
1. Open app → BT enabled → logcat shows 1 scan start
2. Tap group card → logcat shows scan stop (SCANNING exit) then GATT connect
3. Disconnect → logcat shows scan restart (SCANNING entry)
4. Background app → logcat shows scan stop (SCANNING exit via SCAN_STOP event)
5. Foreground app → logcat shows scan restart

**Verify:** Zero `ScanClient` accumulation. Max 1 scan client in `adb logcat | grep ScanClient` at any time.
