# Implementation Plan
# PLAN-BLE-GATT-QUEUE-HARDENING

## Summary
Multiple concurrent BLE GATT operations (Promise.all writes, disconnect, heartbeat, RSSI reads) are racing on the Android GATT stack, directly causing GATT 133 collisions. Simultaneously, a conditional bypass in BleConnectionManager allows writes to escape the BleWriteQueue under specific handshake conditions, eliminating queue protection entirely. This plan serializes all GATT operations and closes the bypass escape hatch.

**Batch:** `BATCH:ble-gatt-hardening`
**Status:** `[✅ VERIFIED]`

---

## Source of Truth Files
- `src/services/BleWriteDispatcher.ts` — Lines 141, 237 (R-13-003, R-13-004)
- `src/services/BleConnectionManager.ts` — Lines 252 (R-01-001)
- `src/services/BleLifecycleManager.ts` — Line 45 (R-13-002)
- `src/hooks/useBLE.ts` — Line 279 (R-13-001)
- `src/hooks/ble/useBLEHeartbeat.ts` — Line 109 (R-13-005)
- `src/hooks/ble/useBLERSSIMonitor.ts` — Line 87 (R-13-006)
- Raw audit: `R-01_findings.json`, `R-13_findings.json`

---

## Findings (Verified, Non-False-Positive)

### CRITICAL — BleWriteQueue Bypass (R-01)
| ID | File | Line | Issue |
|----|------|------|-------|
| R-01-001 | `src/services/BleConnectionManager.ts` | 252 | Conditional `if (enqueueWrite)` bypass allows direct `writeOp()` execution without queue |

**Fix:** Remove the `else` branch. Require `enqueueWrite` in `BleConnectionRequest` types. Always route writes through `await enqueueWrite('critical', writeOp)`.

### HIGH — Concurrent BLE Writes (R-13)
| ID | File | Line | Issue |
|----|------|------|-------|
| R-13-003 | `src/services/BleWriteDispatcher.ts` | 141 | `Promise.all` concurrent multi-device characteristic writes → GATT 133 |
| R-13-004 | `src/services/BleWriteDispatcher.ts` | 237 | `Promise.all` chunked writes across devices → GATT 133 |
| R-13-002 | `src/services/BleLifecycleManager.ts` | 45 | `Promise.all` disconnect operations → GATT contention |
| R-13-001 | `src/hooks/useBLE.ts` | 279 | `Promise.all(isDeviceConnected)` concurrent status checks |
| R-13-005 | `src/hooks/ble/useBLEHeartbeat.ts` | 109 | `Promise.allSettled` concurrent heartbeat pings |
| R-13-006 | `src/hooks/ble/useBLERSSIMonitor.ts` | 87 | `Promise.allSettled` concurrent RSSI reads |

**Fix pattern for all:** Replace `Promise.all(devices.map(...))` with sequential `for...of` loop. Add `await new Promise(res => setTimeout(res, 50))` between each device for write operations.

---

## Implementation Steps

### Step 1 — Close the BleWriteQueue bypass
**File:** `src/services/BleConnectionManager.ts:252`
```typescript
// BEFORE
if (enqueueWrite) {
  await enqueueWrite('critical', writeOp);
} else {
  await writeOp();
}

// AFTER — always queue
if (!enqueueWrite) throw new Error('[BLE] enqueueWrite not provided to connection request');
await enqueueWrite('critical', writeOp);
```

### Step 2 — Serialize BleWriteDispatcher multi-device writes
**File:** `src/services/BleWriteDispatcher.ts:141`
```typescript
// BEFORE
await Promise.all(liveTargets.map(async (device) => { ... }));

// AFTER
for (const device of liveTargets) {
  try {
    await device.writeCharacteristicWithoutResponseForService(...);
  } catch (writeError) { ... }
  await new Promise(res => setTimeout(res, 50)); // inter-device GATT gap
}
```

### Step 3 — Serialize BleWriteDispatcher chunked writes
**File:** `src/services/BleWriteDispatcher.ts:237`
Same pattern as Step 2 — `for...of` with 50ms gap between devices.

### Step 4 — Serialize BleLifecycleManager disconnects
**File:** `src/services/BleLifecycleManager.ts:45`
```typescript
// BEFORE
await Promise.all(disconnectPromises);
// AFTER
for (const device of staleDevices) {
  try { await bleManager.cancelDeviceConnection(device.id).catch(...); }
  catch (e) { ... }
}
```

### Step 5 — Serialize useBLE connection checks
**File:** `src/hooks/useBLE.ts:279`
```typescript
// BEFORE
const liveChecks = await Promise.all(connectedDevicesRef.current.map(...));
// AFTER
const liveChecks: { id: string; connected: boolean }[] = [];
for (const d of connectedDevicesRef.current) {
  liveChecks.push({ id: d.id, connected: await bleManager.isDeviceConnected(d.id) });
}
```

### Step 6 — Serialize useBLEHeartbeat pings
**File:** `src/hooks/ble/useBLEHeartbeat.ts:109`
```typescript
// BEFORE
await Promise.allSettled(devices.map(device => pingConnectedDevice(...)));
// AFTER
for (const device of devices) {
  await pingConnectedDevice(device.id, bleManager, adapterMapRef.current.get(device.id), onStaleLinkDetected);
}
```

### Step 7 — Serialize RSSI reads
**File:** `src/hooks/ble/useBLERSSIMonitor.ts:87`
Sequential `for...of` replacing `Promise.allSettled`.

---

## Verification
- Run `npm run verify` (TSC must pass)
- Run BLE lab test with 2 devices — no GATT 133 errors in 10-minute soak
- Confirm `adb logcat | grep "GATT_ERROR"` is clean during multi-device write operations

## Kanban Task Tags
- `[Status: 🔥 ON DECK]`
- `[Verification Status: ✅ VERIFIED]`
- `[Layer: BLE]`
- `[Risk: H-RISK]`
- `[Size: Meal]`
- `[Cognitive Load: Medium]`
- `[BATCH: ble-gatt-hardening]`
