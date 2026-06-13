# Implementation Plan — BLE Phase 3: Connect as XState Service

**Slug:** `refactor/ble-p3-connect-service`
**Source of Truth:** `BLE_AUDIT_REPORT.md` §3 Async Path Map, `BleConnectionManager.ts:42`, `BleSessionFactory.ts`, `BleMachine.ts:93-111`
**Prerequisite:** `refactor/ble-p2-scanner-simplify` merged ✅

## Goal
Move the GATT connection pipeline into a `fromPromise` XState service invoked by the CONNECTING state. `BleConnectionManager.ts` is deleted. The machine owns the full connect lifecycle — start to success/failure.

## Out of Scope
- Recovery pipeline (Phase 4)
- Heartbeat (Phase 5)
- Cleanup/deletes of other files (Phase 6)

---

## Steps

### Step 1 — Create `src/services/ble/ConnectService.ts`
**Source:** `src/services/BleConnectionManager.ts:42-88` (executeConnectToDevices)

New file as a `fromPromise` actor:
```typescript
import { fromPromise } from 'xstate';
import { createGattSession } from '../BleSessionFactory';

export const connectService = fromPromise(async ({ input, signal }) => {
  const { bleManager, targetMacs, adapterMap, mtuMap } = input;
  const connectedDevices: Device[] = [];

  for (const mac of targetMacs) {
    if (signal.aborted) throw new Error('connect_aborted');
    // 3-attempt GATT 133 backoff (from BleConnectionManager.ts:42-88)
    // createGattSession() for service discovery + adapter resolution
    // requestMTU() on Android
    // register onDeviceDisconnected listener
    // register monitorCharacteristicForService
    connectedDevices.push(conn);
    adapterMap.set(conn.id, adapter);
    mtuMap.set(conn.id, mtu);
  }
  return { devices: connectedDevices };
});
```

**Verify:** `tsc --noEmit` passes. File exports `connectService`.

---

### Step 2 — Wire ConnectService into BleMachine.ts CONNECTING State
**Source:** `src/services/ble/BleMachine.ts:93-111`

```typescript
CONNECTING: {
  invoke: {
    src: 'connectService',
    input: ({ context }) => ({
      bleManager: context.bleManager,
      targetMacs: context.targetMacs ?? [],
      adapterMap: context.adapterMap,
      mtuMap: context.mtuMap,
    }),
    onDone: {
      target: 'READY',
      actions: assign({ connectedDevices: ({ event }) => event.output.devices })
    },
    onError: {
      target: 'IDLE',
      actions: [{ type: 'logTransition', params: { from: 'CONNECTING', to: 'IDLE', reason: 'connect_failed' } }]
    }
  },
  on: {
    DISCONNECT_REQUEST: { target: 'DISCONNECTING' } // invoke auto-cancelled by XState
  }
}
```

Register `connectService` in the machine `setup()`:
```typescript
actors: { connectService }
```

**Verify:** Tap group card → machine enters CONNECTING → ConnectService runs → machine enters READY. `tsc --noEmit` passes.

---

### Step 3 — Delete `BleConnectionManager.ts`
**Source:** `src/services/BleConnectionManager.ts`

Move any pure utility functions (GATT 133 retry logic, handshake writes) into `ConnectService.ts` or `BleSessionFactory.ts`.

Delete `BleConnectionManager.ts`. Remove all imports of it across the codebase.

**Verify:** `grep -r "BleConnectionManager" src/` returns 0 results. Build passes.

---

### Step 4 — Update `useBLE.ts`
**Source:** `src/hooks/useBLE.ts` (connectToDevices function)

Remove manual `bleSend({ type: 'CONNECT_REQUEST' })` and `bleSend({ type: 'CONNECT_SUCCESS' })` calls. These are now emitted by the machine's own state transitions.

`connectToDevices(devices)` → `bleSend({ type: 'CONNECT_REQUEST', targetMacs: devices.map(d => d.id) })`

Remove `bleSend({ type: 'CONNECT_FAIL' })` — machine handles via `onError`.

**Verify:** `grep -r "CONNECT_SUCCESS\|CONNECT_FAIL" src/hooks/useBLE.ts` returns 0 results.

---

### Step 5 — Device Test
1. Tap group card → connects in <3s ✅
2. Double-tap group card rapidly → second tap is gated (machine in CONNECTING, not IDLE) ✅
3. BT off mid-connect → `onError` → IDLE → no stuck CONNECTING state ✅
4. Connect 2 devices → READY with both in `connectedDevices` ✅
