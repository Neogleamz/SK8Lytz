# Implementation Plan — BLE Phase 2: Scanner Simplified

**Slug:** `refactor/ble-p2-scanner-simplify`
**Source of Truth:** `BLE_AUDIT_REPORT.md` §4 Bloat Inventory, `useBLEBatterySweep.ts`, `useBLEScanner.ts`
**Prerequisite:** `refactor/ble-p1-machine-radio` merged ✅

## Goal
Delete all direct radio calls from `useBLEBatterySweep.ts` and `useBLEScanner.ts`. Both files become advisors that send machine events — they never touch `startDeviceScan` or `stopDeviceScan` directly.

## Out of Scope
- Deleting `useBLEBatterySweep.ts` or `useBLEScanner.ts` entirely (Phase 6)
- Connect/recovery services (Phases 3–5)

---

## Steps

### Step 1 — useBLEBatterySweep.ts: Delete Radio Calls
**Source:** `src/hooks/ble/useBLEBatterySweep.ts:110` (startDeviceScan), `:180` (stopDeviceScan)

- **DELETE** `bleManager.startDeviceScan(...)` line 110
- **DELETE** `bleManager.stopDeviceScan()` line 180
- **DELETE** `isSweeperActiveRef` — source `useBLEBatterySweep.ts:22`. Verdict from audit: DELETE
- **CHANGE** `startSweeper()` body → `bleSend({ type: 'SCAN_START' })`
- **CHANGE** `stopSweeper()` body → `bleSend({ type: 'SCAN_STOP' })`
- **CHANGE** battery duty pause → `bleSend({ type: 'SCAN_PAUSE' })`
- **CHANGE** battery duty resume → `bleSend({ type: 'SCAN_RESUME' })`

**Verify:** `grep -r "startDeviceScan\|stopDeviceScan" src/hooks/ble/useBLEBatterySweep.ts` returns 0 results.

---

### Step 2 — useBLEScanner.ts: Delete Radio Calls
**Source:** `src/hooks/ble/useBLEScanner.ts:371` (fallback startDeviceScan), `:368` (stopDeviceScan)

- **DELETE** direct `startDeviceScan` fallback call (line 371) — this was the primary leak source
- **DELETE** direct `stopDeviceScan` calls — now handled by machine exit action
- **DELETE** `scannerStateRef` — audit verdict: DELETE (`src/hooks/ble/useBLEScanner.ts:16`)
- **CHANGE** `scanForPeripherals()` → `bleSend({ type: 'SCAN_START' })`
- **CHANGE** `stopScanner()` → `bleSend({ type: 'SCAN_STOP' })`
- **CHANGE** `burstScan(durationMs)` → `bleSend({ type: 'SCAN_PAUSE' })` + setTimeout(durationMs) + `bleSend({ type: 'SCAN_RESUME' })`

**Keep:** `allDevices[]` accumulation, `scanCallback`, `queueDeviceForInterrogation`.

**Verify:** `grep -r "startDeviceScan\|stopDeviceScan" src/hooks/ble/useBLEScanner.ts` returns 0 results.

---

### Step 3 — Smoke Test
`adb logcat | grep ScanClient` — confirm max 1 scan client across the full flow:
1. App launch → scan starts (1 client)
2. Group tap → scan stops (0 clients) → GATT connect → scan restarts (1 client)
3. Battery drops below 30% → SCAN_PAUSE → 0 clients → SCAN_RESUME → 1 client
4. Background → SCAN_STOP → 0 clients
5. Foreground → SCAN_START → 1 client

**Verify:** Scan client count never exceeds 1 at any observed moment.

---

### Step 4 — Confirm the Fix Is Structural
After this phase, the scan client leak is **physically impossible** to reintroduce because:
- `startDeviceScan` only exists in `BleMachine.ts` SCANNING `entry` action
- `stopDeviceScan` only exists in `BleMachine.ts` SCANNING `exit` action
- No guard, no ref, no race condition can bypass a state machine entry/exit

Write `[DECISION]` entry to `tools/SESSION_LOG.md` confirming structural fix complete.
