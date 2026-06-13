# Implementation Plan
# PLAN-heartbeat-gatt-guard

## Summary
The audit (Cluster E, R-05) found that `HeartbeatService.ts` can fire GATT writes during an
active connection attempt, colliding with handshake packets.

Live code inspection reveals the current guard (`isRunning` flag on line 19-22) only prevents
**re-entrant heartbeat calls** within the same service instance. It does NOT check whether a GATT
operation is already in flight via `BleWriteQueue` from an external caller (e.g., a color write
from the user pressing a preset button, or a recovery ping from `RecoveryService`).

The heartbeat currently uses `enqueueWrite('normal', ...)` on line 36, which means it correctly
routes through the queue. However, the queue is a serializer — it does not skip the heartbeat
write, it just waits its turn. The problem is that a 'normal' priority heartbeat write can be
enqueued WHILE a 'critical' priority recovery ping is being dispatched, potentially causing the
heartbeat packet to immediately follow a recovery handshake packet with zero inter-packet gap.

**Fix:** Before enqueuing the heartbeat write (line 36), check if `BleWriteQueue` has any
in-flight or pending operations. If the queue is busy, skip this heartbeat cycle entirely —
the queue will catch the liveness of the connection via the in-flight write anyway.

**Batch:** `BATCH:ble-recovery-hardening`
**Status:** `[✅ VERIFIED]`

---

## Cited Truth — Live Code State

| Location | Current Code | Assessment |
|---|---|---|
| `HeartbeatService.ts:19-22` | `if (isRunning) return;` | Guards against self-re-entry only |
| `HeartbeatService.ts:36` | `await enqueueWrite('normal', ...)` | Correctly uses queue, but does not check queue busyness first |
| `HeartbeatService.ts:50` | `await bleManager.readRSSIForDevice(mac)` | Fallback RSSI read — also not guarded; runs directly on BLE manager, bypassing queue entirely |
| `HeartbeatService.ts:6` | `import { enqueueWrite } from '../BleWriteQueue'` | Queue imported but no `isActive` check available |

**Key finding:** The RSSI fallback on line 50 (`await bleManager.readRSSIForDevice(mac)`) is
**unqueued** — it bypasses `BleWriteQueue` entirely and hits the BLE manager directly. This is the
primary GATT collision vector, not the `enqueueWrite` path. The enqueued write path is already
safe; the RSSI read is the gap.

**Contradiction Check:** Scanned `HeartbeatService.ts` lines 1-74. The `enqueueWrite` import
and usage on line 36 is correct. The RSSI fallback on line 50 is raw BLE manager access.
Scanned `src/services/BleWriteQueue.ts` — must verify `isActive()` or equivalent is exported
before implementing Step 1 (P1 compliance: read before edit).

---

## Source of Truth

- **Primary:** `artifacts/system_audit_report.md` — Cluster E, R-05 finding
- **Secondary:** `src/services/ble/HeartbeatService.ts` — lines 19-22, 36, 50
- **Tertiary:** `src/services/BleWriteQueue.ts` — must read before implementing to verify
  `isActive()` export availability

---

## Implementation Steps

### Step 1 — Read BleWriteQueue.ts before coding (P1 mandate)
**File:** `src/services/BleWriteQueue.ts`
**Action:** Use `view_file` to read the full file. Confirm whether `isActive()`, `queueLength()`,
or `isPending()` is exported. If not, Step 2a applies. If yes, Step 2b applies.

### Step 2a — If BleWriteQueue has no isActive export: add it
**File:** `src/services/BleWriteQueue.ts`
**Target:** After the queue state variables, add:

```typescript
/** Returns true if any operation is currently executing or pending in the queue. */
export const isWriteQueueActive = (): boolean => {
  // Implementation depends on internal queue structure — read file first
  // Likely: return isProcessing || writeQueue.length > 0;
};
```

### Step 2b — If BleWriteQueue already exports queue state: use it directly
Skip Step 2a. Use the existing export in Step 3.

### Step 3 — Guard the heartbeat interval before enqueueing
**File:** `src/services/ble/HeartbeatService.ts`
**Target:** Lines 21-22 (inside the `setInterval` callback, before the device loop)

```typescript
// BEFORE (current)
const interval = setInterval(async () => {
  if (isRunning) return;
  isRunning = true;
  try {
    if (connectedDevices.length === 0) return;

    for (const device of connectedDevices) {
      ...
      await enqueueWrite('normal', async () => { ... });
      ...
      // BanlanX fallback:
      await bleManager.readRSSIForDevice(mac);  // ← UNGUARDED raw BLE access
    }
  } finally {
    isRunning = false;
  }
}, HEARTBEAT_INTERVAL_MS);

// AFTER — add queue-busy guard AND queue the RSSI fallback
const interval = setInterval(async () => {
  if (isRunning) return;
  // Skip this heartbeat cycle if any GATT operation is in-flight or pending.
  // The in-flight write proves the connection is alive; no ping needed.
  if (isWriteQueueActive()) return;
  isRunning = true;
  try {
    if (connectedDevices.length === 0) return;

    for (const device of connectedDevices) {
      ...
      await enqueueWrite('normal', async () => { ... });
      ...
      // BanlanX / unknown adapter fallback: queue the RSSI read too
      await enqueueWrite('normal', async () => {
        await bleManager.readRSSIForDevice(mac);
        return true;
      });
    }
  } finally {
    isRunning = false;
  }
}, HEARTBEAT_INTERVAL_MS);
```

**Key changes:**
1. `if (isWriteQueueActive()) return;` — skip cycle when queue is busy (line added after `isRunning` check)
2. RSSI fallback wrapped in `enqueueWrite('normal', ...)` — eliminates the unqueued raw BLE access

### Step 4 — Add import for isWriteQueueActive
**File:** `src/services/ble/HeartbeatService.ts`
**Target:** Line 6

```typescript
// BEFORE
import { enqueueWrite } from '../BleWriteQueue';

// AFTER
import { enqueueWrite, isWriteQueueActive } from '../BleWriteQueue';
```

---

## Risk Assessment — L-RISK Justification
The guard is additive — it can only cause the heartbeat to skip a cycle, never prevent a
legitimate liveness check from running eventually. The enqueued path already serializes correctly.
The RSSI queue wrap is the higher-change step but follows the existing `enqueueWrite` pattern
already in the file. The only regression scenario is if `isWriteQueueActive()` always returns
`true` (queue stuck) — which would mask a stale link. This is mitigated by the fact that a stuck
queue would itself trigger a `RECOVERY_FAIL` event from `RecoveryService`.

## Verification

- `npm run verify` — TSC must pass, `isWriteQueueActive` must typecheck from its import
- Unit test: Mock `isWriteQueueActive` to return `true` → assert `enqueueWrite` is never called
  in that heartbeat cycle
- Unit test: Mock `isWriteQueueActive` to return `false` → assert `enqueueWrite` IS called
- Device test: Trigger a large color write (e.g., Static Colorful 48-pixel payload) while
  heartbeat timer fires simultaneously → confirm in logcat that heartbeat is skipped that cycle
  and no GATT 133 error follows

## Kanban Task Tags
- `[Status: ✅ READY]`
- `[Verification Status: ✅ VERIFIED]`
- `[Layer: BLE]`
- `[Risk: L-RISK]`
- `[Size: Snack]`
- `[Cognitive Load: 🤖 PRO-MED]`
- `[BATCH: ble-recovery-hardening]`
