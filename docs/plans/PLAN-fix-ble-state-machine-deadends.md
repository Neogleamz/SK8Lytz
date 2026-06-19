# Implementation Plan — Fix BLE State Machine Dead-Ends

> **Slug:** `fix-ble-state-machine-deadends`
> **Batch:** connection-pipeline-fix
> **Wave:** 1
> **Risk:** H-RISK
> **Size:** Meal
> **Cognitive Load:** High

---

## Problem Statement

The BleMachine XState FSM has multiple dead-end states, missing event handlers, and type weaknesses that cause the connection pipeline to hang or silently drop user actions.

### Audit Findings Covered

| ID | Severity | Summary | File | Lines |
|----|----------|---------|------|-------|
| **H1** | HIGH | DISCONNECTING is a dead-end — no service performs GATT teardown, no code sends `DISCONNECT_COMPLETE` | [BleMachine.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L236-L243) | 236–243 |
| **H4** | HIGH | `HEARTBEAT_FAIL` during RECOVERING silently drops second device | [BleMachine.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L244-L294) | 244–294 |
| **H5** | HIGH | `CONNECT_REQUEST` during CONNECTING is silently dropped | [BleMachine.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L154-L189) | 154–189 |
| **M2** | MEDIUM | RECOVERING has no timeout — stuck forever on unhandled crash | [BleMachine.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L244-L258) | 244–258 |
| **M3** | MEDIUM | DISCONNECTING has no timeout | [BleMachine.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L236-L243) | 236–243 |
| **M6** | MEDIUM | `RECOVERY_PERMANENTLY_FAILED` does NOT remove failed device from `connectedDevices` | [BleMachine.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L282-L284) | 282–284 |
| **M8** | MEDIUM | `CONNECT_REQUEST.targetMacs` typed optional — enables silent no-ops | [BleMachine.types.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.types.ts#L34) | 34 |
| **M9** | MEDIUM | `SCAN_START` only accepted in IDLE — cannot scan while connected | [BleMachine.ts](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L76-L79) | 76–79 |

---

## Files to Create/Modify

| File | Action | Reason |
|------|--------|--------|
| `src/services/ble/BleMachine.ts` | MODIFY | Add DisconnectService invocation, timeouts, missing event handlers |
| `src/services/ble/BleMachine.types.ts` | MODIFY | Harden `targetMacs` to required, remove dead events |
| `src/hooks/useBLE.ts` | MODIFY | Wire DisconnectService callback via machine context |

---

## Step-by-Step Fix

### Step 1: Create `disconnectService` actor (inline in BleMachine.ts)

**Source:** [BleMachine.ts L236–243](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L236-L243) — DISCONNECTING state is a bare listener with no invoked service.

**Change:** Import `fromPromise` from xstate. Define a `disconnectService` actor that:
1. Reads `connectedDevices` from input
2. Iterates each device, calling `bleManager.cancelDeviceConnection(device.id)`
3. Removes all `disconnectListeners` subscriptions
4. Returns `{ success: true }`

Wire it as an invoked actor in the `DISCONNECTING` state:
```typescript
DISCONNECTING: {
  invoke: {
    id: 'disconnectService',
    src: 'disconnectService',
    input: ({ context }) => ({
      bleManager: context.bleManager,
      connectedDevices: context.connectedDevices,
      disconnectListeners: context.disconnectListeners,
    }),
    onDone: {
      target: 'IDLE',
      actions: ['clearConnectedDevices', 'clearGhostedMacs', { type: 'logTransition', params: { from: 'DISCONNECTING', to: 'IDLE' } }]
    },
    onError: {
      target: 'IDLE',
      actions: ['clearConnectedDevices', 'clearGhostedMacs', { type: 'logTransition', params: { from: 'DISCONNECTING', to: 'IDLE', reason: 'disconnect_error' } }]
    }
  },
  after: {
    10000: {
      target: 'IDLE',
      actions: ['clearConnectedDevices', 'clearGhostedMacs', { type: 'logTransition', params: { from: 'DISCONNECTING', to: 'IDLE', reason: 'disconnect_timeout' } }]
    }
  },
  on: {
    DISCONNECT_COMPLETE: {
      target: 'IDLE',
      actions: ['clearConnectedDevices', 'clearGhostedMacs', { type: 'logTransition', params: { from: 'DISCONNECTING', to: 'IDLE' } }]
    }
  }
}
```

**Verify:** `DISCONNECT_REQUEST` → DISCONNECTING → invoked `disconnectService` runs → `onDone` fires → IDLE. Also: 10s timeout catches hangs.

---

### Step 2: Add RECOVERING timeout (M2)

**Source:** [BleMachine.ts L244–258](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L244-L258) — RECOVERING state has `invoke` with `onDone: undefined, onError: undefined`.

**Change:** Add safety timeout and proper `onDone`/`onError` handlers:
```typescript
RECOVERING: {
  invoke: { ... },  // keep existing
  after: {
    90000: {
      target: 'IDLE',
      actions: ['clearGhostedMacs', { type: 'logTransition', params: { from: 'RECOVERING', to: 'IDLE', reason: 'recovery_timeout_90s' } }]
    }
  },
  on: { ... }  // keep existing handlers
}
```
Also: Replace `onDone: undefined, onError: undefined` with proper no-op entries or remove them entirely (XState v5 `fromCallback` actors don't use `onDone` — they `sendBack` instead).

**Verify:** If RecoveryService hangs or crashes silently, machine transitions to IDLE after 90s.

---

### Step 3: Add HEARTBEAT_FAIL handler in RECOVERING (H4)

**Source:** [BleMachine.ts L244–294](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L244-L294) — RECOVERING state has no `HEARTBEAT_FAIL` handler.

**Change:** Add `HEARTBEAT_FAIL` to the RECOVERING `on` block:
```typescript
HEARTBEAT_FAIL: {
  actions: [
    assign({
      ghostedDeviceIds: ({ context, event }) => {
        if (event.type !== 'HEARTBEAT_FAIL') return context.ghostedDeviceIds;
        const existing = context.ghostedDeviceIds ?? [];
        return existing.includes(event.deviceId) ? existing : [...existing, event.deviceId];
      }
    }),
    { type: 'logTransition', params: { from: 'RECOVERING', to: 'RECOVERING', reason: 'additional_heartbeat_fail' } }
  ]
}
```

**Verify:** In RECOVERING state, a second `HEARTBEAT_FAIL` for device B appends B's MAC to `ghostedDeviceIds` without disrupting the active recovery for device A.

---

### Step 4: Add CONNECT_REQUEST handler in CONNECTING (H5)

**Source:** [BleMachine.ts L154–189](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L154-L189) — CONNECTING state handles `RECOVERY_START` and `DISCONNECT_REQUEST` but NOT `CONNECT_REQUEST`.

**Change:** Add `CONNECT_REQUEST` handler that cancels the running service and re-enters CONNECTING with the new targets:
```typescript
CONNECT_REQUEST: {
  target: 'CONNECTING',
  actions: ['setTargetMacs', { type: 'logTransition', params: { from: 'CONNECTING', to: 'CONNECTING', reason: 'connect_request_override' } }]
}
```

Self-transition to CONNECTING will: (a) exit CONNECTING (cancelling the invoked `connectService` via XState behavior), and (b) re-enter CONNECTING with the new `targetMacs`.

**Verify:** Dispatching `CONNECT_REQUEST` while in CONNECTING terminates the first actor and starts a new one with the updated target list.

---

### Step 5: Remove failed device from connectedDevices on RECOVERY_PERMANENTLY_FAILED (M6)

**Source:** [BleMachine.ts L282–284](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L282-L284) — `RECOVERY_PERMANENTLY_FAILED` clears `ghostedMacs` but leaves the dead device in `connectedDevices`.

**Change:** Add an `assign` action to filter the permanently failed device from `connectedDevices`:
```typescript
RECOVERY_PERMANENTLY_FAILED: {
  target: 'IDLE',
  actions: [
    assign({
      connectedDevices: ({ context, event }) => {
        if (event.type !== 'RECOVERY_PERMANENTLY_FAILED') return context.connectedDevices;
        return context.connectedDevices.filter(d => d.id !== event.deviceId);
      }
    }),
    'setDeviceUnreachable',
    'notifyUserDeviceFailed',
    'clearGhostedMacs',
    { type: 'logTransition', params: { from: 'RECOVERING', to: 'IDLE', reason: 'permanent_fail' } }
  ]
}
```

**Verify:** After permanent failure, `connectedDevices.length` accurately reflects only live devices.

---

### Step 6: Add SCAN_START handler in READY (M9)

**Source:** [BleMachine.ts L191–234](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.ts#L191-L234) — READY state has no `SCAN_START` handler. The sweeper bypasses the machine but this is undocumented.

**Change:** Add `SCAN_START` to READY's `on` block. Since the device is connected and we want to scan without disconnecting, this is a self-transition that stores the sweeperId without changing state:
```typescript
SCAN_START: {
  actions: ['setSweeperId', { type: 'logTransition', params: { from: 'READY', to: 'READY', reason: 'scan_while_ready' } }]
}
```

> Note: This is a documentation/completeness fix. The scanner currently bypasses the machine via `bleManager.startDeviceScan()` directly in `useBLEScanner`. Adding the handler ensures future FSM-routed scan requests don't get silently dropped.

**Verify:** `SCAN_START` event received while in READY does not crash or transition away from READY.

---

### Step 7: Make `targetMacs` required in CONNECT_REQUEST type (M8)

**Source:** [BleMachine.types.ts L34](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.types.ts#L34) — `targetMacs?: string[]` is optional.

**Change:**
```diff
-  | { type: 'CONNECT_REQUEST'; targetMacs?: string[] }
+  | { type: 'CONNECT_REQUEST'; targetMacs: string[] }
```

**Verify:** TSC compilation succeeds. All existing `CONNECT_REQUEST` dispatch sites already pass `targetMacs` (checked: [useBLE.ts L468](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L468)).

---

### Step 8: Remove dead CONNECT_SUCCESS / CONNECT_FAIL events (L7, type cleanup)

**Source:** [BleMachine.types.ts L35–36](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/BleMachine.types.ts#L35-L36) — These events are never dispatched by any code. `connectService` uses `onDone`/`onError` (xstate done events), not manual event dispatch.

**Change:**
```diff
-  | { type: 'CONNECT_SUCCESS'; devices: Device[] }
-  | { type: 'CONNECT_FAIL' }
```

**Verify:** grep confirms no dispatch site for either event. `setConnectedDevices` action at L28 references `CONNECT_SUCCESS` — update its guard to remove that branch.

---

### Step 9: Wire DisconnectService in useBLE.ts

**Source:** [useBLE.ts L516–518](file:///c:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/useBLE.ts#L516-L518) — `disconnectFromDevice` sends `DISCONNECT_REQUEST` but no service exists to handle it.

**Change:** No change needed in useBLE.ts — the `disconnectService` is invoked by the machine itself via `BleMachine.ts`. The machine's context already has `bleManager`, `connectedDevices`, and `disconnectListeners`. Verify the machine input wiring is complete.

**Verify:** Call `disconnectFromDevice()` → machine enters DISCONNECTING → invoked service runs → GATT teardown → machine returns to IDLE.

---

## Out of Scope

- **ConnectService internals** (retry logic, MTU, handshake) — covered by Plan 2
- **RecoveryService internals** (Phase 1/2/3 loop) — covered by Plan 2
- **DashboardScreen.tsx** — covered by Plan 3
- **useDashboardAutoConnect.ts** — covered by Plan 3
- **M4 (multi-device RECOVERY_START routing)** — intentional design decision; document, don't change
- **HeartbeatService internals** — not audited for this batch

---

## Verification Matrix

| Step | What to verify | Method |
|------|----------------|--------|
| 1 | DISCONNECTING invokes service and transitions to IDLE | `npm run verify` + manual test |
| 2 | RECOVERING times out after 90s if service hangs | XState inspector or unit test |
| 3 | Second HEARTBEAT_FAIL appends to ghostedDeviceIds | Unit test |
| 4 | CONNECT_REQUEST during CONNECTING cancels and restarts | Manual test |
| 5 | Permanently failed device removed from connectedDevices | Unit test |
| 6 | SCAN_START in READY doesn't error or transition | Unit test |
| 7 | TSC passes with required targetMacs | `npm run verify` |
| 8 | No references to CONNECT_SUCCESS/CONNECT_FAIL remain | `grep -r "CONNECT_SUCCESS\|CONNECT_FAIL"` |
| 9 | Full disconnect cycle works end-to-end | Manual BLE test |
