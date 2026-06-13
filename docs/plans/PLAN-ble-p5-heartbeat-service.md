# Implementation Plan — BLE Phase 5: Heartbeat as XState Service

**Slug:** `refactor/ble-p5-heartbeat-service`
**Source of Truth:** `BLE_AUDIT_REPORT.md` §3 Async Path Map, `useBLEHeartbeat.ts:12,44,62`, `BleMachine.ts`
**Prerequisite:** `refactor/ble-p4-recovery-service` merged ✅

## Goal
Move the 45-second heartbeat ping into a `fromCallback` XState actor invoked by the READY state. The heartbeat auto-starts when the machine enters READY and auto-cancels when it exits. No manual `clearInterval` needed.

## Out of Scope
- Cleanup/deletes (Phase 6)
- Any change to the ping payload format or timing value

---

## Steps

### Step 1 — Create `src/services/ble/HeartbeatService.ts`
**Source:** `src/hooks/ble/useBLEHeartbeat.ts:12,44,62` (lastHeartbeat, interval start/clear)

```typescript
import { fromCallback } from 'xstate';

export const heartbeatService = fromCallback(({ input, sendBack }) => {
  const { bleManager, connectedDevices, adapterMap } = input;

  const interval = setInterval(async () => {
    for (const device of connectedDevices) {
      // Ping logic from useBLEHeartbeat.ts:44 — verbatim copy
      // If ping fails or times out:
      //   sendBack({ type: 'HEARTBEAT_FAIL', deviceId: device.id })
    }
  }, 45_000); // bleTimingConstants.HEARTBEAT_INTERVAL_MS

  return () => clearInterval(interval); // XState calls this when machine exits READY
});
```

**Verify:** `tsc --noEmit` passes. `HEARTBEAT_FAIL` event is a valid BleMachineEvent.

---

### Step 2 — Wire HeartbeatService into BleMachine.ts READY State
**Source:** `src/services/ble/BleMachine.ts` (READY state)

```typescript
READY: {
  invoke: [{
    src: 'heartbeatService',
    input: ({ context }) => ({
      bleManager: context.bleManager,
      connectedDevices: context.connectedDevices,
      adapterMap: context.adapterMap,
    })
  }],
  on: {
    HEARTBEAT_FAIL: {
      target: 'RECOVERING',
      actions: assign({ ghostedDeviceIds: ({ event }) => [event.deviceId] })
    },
    DISCONNECT_REQUEST: { target: 'DISCONNECTING' },
    RECOVERY_START:     { target: 'RECOVERING', actions: 'setGhostedMacs' },
  }
}
```

**Verify:** Machine in READY → heartbeat fires every 45s in logcat. Machine transitions DISCONNECTING → interval cleared (no more heartbeat pings in logcat).

---

### Step 3 — Remove Manual Heartbeat Wiring from `useBLE.ts`
**Source:** `src/hooks/useBLE.ts` (wherever `useBLEHeartbeat` is called and its `onStaleLinkDetected` callback is wired)

Remove the `useBLEHeartbeat(...)` call. The machine now owns heartbeat lifecycle.

Remove `onStaleLinkDetected` callback → replace with machine's `HEARTBEAT_FAIL` event handling.

**Verify:** `grep -r "useBLEHeartbeat\|onStaleLinkDetected" src/` returns 0 results.

---

### Step 4 — Device Test
1. Connect → wait 45s → logcat shows ping → connection stays alive ✅
2. Physically block BT signal mid-session → stale link detected → RECOVERING starts ✅
3. Disconnect while heartbeat is mid-ping → no crash, ping cancelled cleanly ✅
