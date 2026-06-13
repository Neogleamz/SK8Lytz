# Implementation Plan — BLE Phase 4: Recovery as XState Service

**Slug:** `refactor/ble-p4-recovery-service`
**Source of Truth:** `BLE_AUDIT_REPORT.md` §3 Async Path Map, `useBLEAutoRecovery.ts:57-82`, `BleMachine.ts`
**Prerequisite:** `refactor/ble-p3-connect-service` merged ✅

## Goal
Convert the 3-phase recovery loop into a `fromCallback` XState actor invoked by the RECOVERING state. XState's invoke lifecycle replaces all manual `AbortController` / `cancelAllRecoveries()` wiring. Recovery cannot run concurrently with connect — the machine guarantees it.

## Out of Scope
- Heartbeat service (Phase 5)
- Cleanup/deletes (Phase 6)

---

## Steps

### Step 1 — Create `src/services/ble/RecoveryService.ts`
**Source:** `src/hooks/ble/useBLEAutoRecovery.ts:57-82` (spawnRecoveryLoop, 3-phase logic)

```typescript
import { fromCallback } from 'xstate';

export const recoveryService = fromCallback(({ input, sendBack }) => {
  let cancelled = false;
  const cancel = () => { cancelled = true; };

  async function run() {
    const { bleManager, ghostedDeviceIds, adapterMap, mtuMap, onPartialReconnect } = input;

    // Phase 1: RSSI scan (from useBLEAutoRecovery.ts:57 — copy verbatim, no logic changes)
    // Phase 2: GATT re-connect attempts (from useBLEAutoRecovery.ts:70)
    // Phase 3: Final handshake + state restoration (from useBLEAutoRecovery.ts:78)
    // At each await boundary: if (cancelled) return;

    if (success) {
      sendBack({ type: 'RECOVERY_COMPLETE', devices: reconnected });
    } else {
      sendBack({ type: 'RECOVERY_FAIL' });
    }
  }

  run();
  return cancel; // XState calls this when machine exits RECOVERING
});
```

**Verify:** `tsc --noEmit` passes. All 3 phases present. Cancellation via `cancel()` return.

---

### Step 2 — Wire RecoveryService into BleMachine.ts RECOVERING State
**Source:** `src/services/ble/BleMachine.ts` (RECOVERING state)

```typescript
RECOVERING: {
  invoke: {
    src: 'recoveryService',
    input: ({ context }) => ({
      bleManager: context.bleManager,
      ghostedDeviceIds: context.ghostedDeviceIds ?? [],
      adapterMap: context.adapterMap,
      mtuMap: context.mtuMap,
    })
  },
  on: {
    RECOVERY_COMPLETE: {
      target: 'READY',
      actions: assign({ connectedDevices: ({ event }) => event.devices })
    },
    RECOVERY_FAIL:     { target: 'IDLE' },
    DISCONNECT_REQUEST: { target: 'DISCONNECTING' } // auto-cancels invoke
  }
}
```

**Verify:** Machine enters RECOVERING → `recoveryService` actor starts. Machine transitions DISCONNECTING → `cancel()` called → recovery stops.

---

### Step 3 — Remove Manual Recovery Trigger from `BleLifecycleManager.ts`
**Source:** `src/services/BleLifecycleManager.ts` (handleOrganicDisconnect → initiateRecovery)

Change `initiateRecovery(mac)` call → `bleSend({ type: 'RECOVERY_START', ghostedDeviceIds: [mac] })`

Remove `cancelAllRecoveries()` call — XState handles cancellation via DISCONNECTING transition.

**Verify:** `grep -r "cancelAllRecoveries\|initiateRecovery" src/` returns 0 results.

---

### Step 4 — Group Dropout Routing
**Source:** `src/hooks/useBLE.ts` (handleOrganicDisconnect, multiple device dropout logic)

Route in READY state:
```typescript
RECOVERY_START: [
  {
    // 2+ devices gone → full group reconnect via ConnectService (Phase 3)
    guard: ({ event }) => event.ghostedDeviceIds.length >= 2,
    target: 'CONNECTING',
    actions: assign({ targetMacs: ({ event }) => event.ghostedDeviceIds })
  },
  {
    // Single device dropout → RecoveryService 3-phase loop
    target: 'RECOVERING',
    actions: assign({ ghostedDeviceIds: ({ event }) => event.ghostedDeviceIds })
  }
]
```

**Verify:** 1 skate disconnects → RECOVERING. Both skates disconnect → CONNECTING (group reconnect).

---

### Step 5 — Device Test
1. Skate powers off mid-session → RECOVERING starts → reconnects within 30s ✅
2. Both skates power off → CONNECTING (group reconnect, not 2 competing recovery loops) ✅
3. User taps forceDisconnect during recovery → DISCONNECTING → recovery cancelled immediately ✅
4. Max retries hit → IDLE → user must re-tap group card ✅
