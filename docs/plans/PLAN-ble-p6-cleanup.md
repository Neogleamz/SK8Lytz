# Implementation Plan — BLE Phase 6: Delete the Dead Code

**Slug:** `refactor/ble-p6-cleanup`
**Source of Truth:** `BLE_AUDIT_REPORT.md` §6 File-by-File Rebuild Verdict, `useBLE.ts`
**Prerequisite:** `refactor/ble-p5-heartbeat-service` merged ✅

## Goal
Delete files made obsolete by Phases 1–5. Gut `useBLE.ts` of all the scaffold that XState now owns. Result: ~300 lines removed from useBLE.ts, 5 files deleted, codebase becomes the system that was always intended.

## Out of Scope
- Protocol adapters (untouched)
- UI components (untouched)
- Write pipeline (BleWriteQueue, BleWriteDispatcher — these are NOT deleted)

---

## Deletion List (confirmed by audit, safe after Phases 1–5)

| File | Reason Safe to Delete |
|---|---|
| `src/services/BleLifecycleManager.ts` | Disconnect teardown now in machine DISCONNECTING state |
| `src/hooks/ble/useBLEAutoRecovery.ts` | Absorbed by `RecoveryService.ts` (Phase 4) |
| `src/hooks/ble/useBLEHeartbeat.ts` | Absorbed by `HeartbeatService.ts` (Phase 5) |
| `src/hooks/ble/useBLEGattMutex.ts` | Machine state prevents concurrent GATT ops by design |

> [!WARNING]
> Do NOT delete `useBLEScanner.ts` or `useBLEBatterySweep.ts` yet — they still own `allDevices[]` accumulation and battery monitoring. Mark for future consolidation sweep.

---

## Steps

### Step 1 — Verify Zero References Before Each Delete
For each file to delete, run:
```
grep -r "BleLifecycleManager\|useBLEAutoRecovery\|useBLEHeartbeat\|useBLEGattMutex" src/
```
**Must return 0 results** before deletion. If any remain, trace and remove the import first.

---

### Step 2 — Delete the 4 Files
```
del src\services\BleLifecycleManager.ts
del src\hooks\ble\useBLEAutoRecovery.ts
del src\hooks\ble\useBLEHeartbeat.ts
del src\hooks\ble\useBLEGattMutex.ts
```

**Verify:** `tsc --noEmit` passes. Zero import errors.

---

### Step 3 — Gut useBLE.ts (~300 lines removed)
**Source:** `src/hooks/useBLE.ts`

**DELETE these blocks:**
- `bleGateRef` / `getGate` / `setGate` — machine state IS the gate
- `isSweeperActiveRef` — deleted in Phase 2
- `keepaliveTimerRef` / keepalive management — machine READY invoke owns this
- `handleOrganicDisconnect → initiateRecovery` wiring — machine RECOVERY_START event
- All manual `bleSend({ type: 'CONNECT_SUCCESS' })` calls — machine onDone handles
- All manual `bleSend({ type: 'RECOVERY_START' })` calls — BleLifecycleManager gone
- `retriggerAutoConnectRef` — audit verdict: dead code (`useDashboardAutoConnect.ts:198`)

**KEEP:**
- `bleManager` singleton creation
- `bleActor` creation + `bleActor.start()`
- `allDevices[]` React state (populated by scanCallback)
- `connectedDevices` React state (populated by machine READY onDone)
- `writeToDevice(mac, payload)`
- `connectToDevices(devices)` → `bleSend({ type: 'CONNECT_REQUEST', targetMacs })`
- `disconnectFromDevice(mac)` → `bleSend({ type: 'DISCONNECT_REQUEST', mac })`
- `forceDisconnect()` → `bleSend({ type: 'FORCE_IDLE' })`
- BLE state subscription for UI (bleState, bleSend exposed to consumers)

**Verify:** useBLE.ts line count <200. `tsc --noEmit` passes.

---

### Step 4 — Full Regression Test on Device
1. Cold start → scan runs → hardware setup wizard identifies all 5 devices ✅
2. Tap group card → CONNECTING → READY <3s ✅
3. LED pattern write → color changes on skates ✅
4. One skate drops → RECOVERING → reconnects ✅
5. Both skates drop → CONNECTING group reconnect → READY ✅
6. 45s → heartbeat fires → stays connected ✅
7. Background app → scan stops → no scan client leak ✅
8. Foreground app → scan resumes ✅
9. Power cycle BT → IDLE → re-tap group → CONNECTING → READY ✅

**Verify:** All 9 scenarios pass on physical Android device.
