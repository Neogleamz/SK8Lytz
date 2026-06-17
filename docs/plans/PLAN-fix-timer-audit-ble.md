# Implementation Plan: fix/timer-audit-ble

> **Slug:** `fix/timer-audit-ble`
> **Rule:** R-16 (Hardcoded Delays)
> **Risk:** M-RISK | **Size:** Feast
> **Source Analysis:** [system_audit_report.md — CLUSTER 9](file:///C:/Users/Magma/.gemini/antigravity/brain/a2899729-4d77-4e6c-8f8c-d23919eb2b74/system_audit_report.md)

## Goal

Replace remaining hardcoded `setTimeout`/`setInterval` delay values in the BLE pipeline with named constants in `src/constants/bleTimingConstants.ts`. After this task, every millisecond value in the BLE write, connect, recovery, scan, interrogation, and heartbeat flows is centralized in a single registry with JSDoc provenance.

## Source Analysis Link

- Existing constants file: [bleTimingConstants.ts](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/constants/bleTimingConstants.ts) — 12 constants already defined (lines 16–90)
- BleWriteDispatcher already fully migrated: lines 110, 191, 260, 275, 277, 346, 388, 407 all reference `BLE_TIMING.*`  
  Source: [BleWriteDispatcher.ts:8](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteDispatcher.ts#L8)
- ConnectService partially migrated (lines 106, 218, 221 use `BLE_TIMING`); local `GATT_BACKOFF_MS` at line 147 remains.  
  Source: [ConnectService.ts:147](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L147)
- RecoveryService partially migrated (line 142 uses `BLE_TIMING`); local recovery constants at lines 12–18 remain.  
  Source: [RecoveryService.ts:12-18](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts#L12)
- useBLEScanner delays annotated `intentionally preserved per R-16` (lines 72, 114, 223, 308, 347, 360, 383) — **NOT in scope for migration**, but the numeric literals (5000, 1000, 60000) should become named constants for readability.  
  Source: [useBLEScanner.ts:107,309,348,361,384](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts#L107)
- useBLEBatterySweep local constants at lines 12–13 (`THROTTLE_SCAN_ON_MS`, `THROTTLE_SCAN_OFF_MS`) + `SCAN_BUDGET_WINDOW_MS` at line 41.  
  Source: [useBLEBatterySweep.ts:12-13](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEBatterySweep.ts#L12)

## Files to Create/Modify

| Action | File | Reason |
|--------|------|--------|
| **Modify** | `src/constants/bleTimingConstants.ts` | Add ~18 new named constants |
| **Modify** | `src/services/ble/HeartbeatService.ts` | Replace local `HEARTBEAT_INTERVAL_MS` → `BLE_TIMING.HEARTBEAT_INTERVAL_MS` |
| **Modify** | `src/services/ble/InterrogatorService.ts` | Replace `PROBE_TIMEOUT_MS`, `PROBE_QUEUE_DELAY_MS`, `PROBE_QUEUE_DELAY_MS_FTUE`, `BLE_INTERROGATION_STAGGER_MS` |
| **Modify** | `src/services/BlePingService.ts` | Replace hardcoded `3500` probe timeout (lines 71, 80) |
| **Modify** | `src/services/BleSessionFactory.ts` | Replace local `GATT_BACKOFF_MS` array (line 93) |
| **Modify** | `src/services/BleWriteQueue.ts` | Replace `jitteredDelay(100, 50)` retry constants (line 213) |
| **Modify** | `src/services/ble/ConnectService.ts` | Replace local `GATT_BACKOFF_MS` array (line 147) |
| **Modify** | `src/services/ble/RecoveryService.ts` | Replace local `RECOVERY_BASE_MS`, `RECOVERY_MAX_MS`, `PHASE_*` constants (lines 12–18) |
| **Modify** | `src/hooks/ble/useBLEScanner.ts` | Replace scanner timing literals (`DEBOUNCE_MS`, `DEBOUNCE_MS_FTUE`, `5000`, `60000`, `1000`) |
| **Modify** | `src/hooks/ble/useBLEBatterySweep.ts` | Replace `THROTTLE_SCAN_ON_MS`, `THROTTLE_SCAN_OFF_MS`, `SCAN_BUDGET_WINDOW_MS` |

## Steps

### Step 1 — Expand `bleTimingConstants.ts` with new constants

Add the following named constants to `BLE_TIMING` with JSDoc provenance comments:

```
HEARTBEAT_INTERVAL_MS: 45_000          — HeartbeatService.ts:10
PROBE_TIMEOUT_MS: 3500                 — InterrogatorService.ts:25, BlePingService.ts:71,80
PROBE_QUEUE_DELAY_MS: 2000             — InterrogatorService.ts:26
PROBE_QUEUE_DELAY_MS_FTUE: 500         — InterrogatorService.ts:27
INTERROGATION_STAGGER_MS: 500          — InterrogatorService.ts:28
GATT_RETRY_BASE_MS: 100               — BleWriteQueue.ts:213
GATT_RETRY_JITTER_MS: 50              — BleWriteQueue.ts:213
GATT_CONNECT_BACKOFF_MS: [500, 1500, 4000]  — ConnectService.ts:147
GATT_SESSION_BACKOFF_MS: [500, 1500]   — BleSessionFactory.ts:93
RECOVERY_BASE_MS: 1500                 — RecoveryService.ts:12
RECOVERY_MAX_MS: 30_000                — RecoveryService.ts:13
RECOVERY_PHASE_1_MAX_ATTEMPTS: 12      — RecoveryService.ts:14
RECOVERY_PHASE_2_MAX_ATTEMPTS: 5       — RecoveryService.ts:15
RECOVERY_PHASE_2_BACKOFF_MS: 20_000    — RecoveryService.ts:16
RECOVERY_PHASE_3_POLL_INTERVAL_MS: 5_000  — RecoveryService.ts:17
RECOVERY_PHASE_3_MAX_POLLS: 120        — RecoveryService.ts:18
SCAN_DEBOUNCE_MS: 1500                 — useBLEScanner.ts:26
SCAN_DEBOUNCE_MS_FTUE: 800             — useBLEScanner.ts:27
SCAN_TELEMETRY_FLUSH_MS: 60_000        — useBLEScanner.ts:309
SCAN_OFFLINE_FLUSH_DEFER_MS: 5000      — useBLEScanner.ts:107
SANDBOX_MOCK_DISCOVERY_DELAY_MS: 1000  — useBLEScanner.ts:348
SANDBOX_MOCK_SCAN_STOP_MS: 5000        — useBLEScanner.ts:361
MANUAL_SCAN_TIMEOUT_MS: 5000           — useBLEScanner.ts:384
THROTTLE_SCAN_ON_MS: 10_000            — useBLEBatterySweep.ts:12
THROTTLE_SCAN_OFF_MS: 20_000           — useBLEBatterySweep.ts:13
SCAN_BUDGET_WINDOW_MS: 30_000          — useBLEBatterySweep.ts:41
```

> Note: Array-type backoff constants (`GATT_CONNECT_BACKOFF_MS`, `GATT_SESSION_BACKOFF_MS`) can be typed as `readonly number[]` within the `as const` object.

**Verify:** `npm run verify` passes. New constants are exported. No existing consumers break.

### Step 2 — Migrate HeartbeatService.ts

- Remove local `HEARTBEAT_INTERVAL_MS` constant (line 10). Source: [HeartbeatService.ts:10](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/HeartbeatService.ts#L10)
- Add `import { BLE_TIMING } from '../../constants/bleTimingConstants';`
- Replace `HEARTBEAT_INTERVAL_MS` usage at line 24 → `BLE_TIMING.HEARTBEAT_INTERVAL_MS`

**Verify:** `git diff HEAD src/services/ble/HeartbeatService.ts` — only import + 2 lines changed.

### Step 3 — Migrate InterrogatorService.ts

- Remove local constants (lines 25–28). Source: [InterrogatorService.ts:25-28](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/InterrogatorService.ts#L25)
- Add `import { BLE_TIMING } from '../../constants/bleTimingConstants';`
- Replace usages: `PROBE_TIMEOUT_MS` (line 87→90), `BLE_INTERROGATION_STAGGER_MS` (line 186), `PROBE_QUEUE_DELAY_MS`/`PROBE_QUEUE_DELAY_MS_FTUE` (line 179)
- Re-export aliases for backward compatibility: `export const PROBE_QUEUE_DELAY_MS = BLE_TIMING.PROBE_QUEUE_DELAY_MS;` etc. (these are imported by `useBLEInterrogator.ts`)

**Verify:** `git diff` — only import swap + constant references changed. Grep for old names confirms no orphans.

### Step 4 — Migrate BlePingService.ts

- Replace hardcoded `3500` at lines 71 and 80 → `BLE_TIMING.PROBE_TIMEOUT_MS`. Source: [BlePingService.ts:71,80](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BlePingService.ts#L71)
- Add `import { BLE_TIMING } from '../constants/bleTimingConstants';`

**Verify:** `git diff` — import + 2 literal replacements.

### Step 5 — Migrate BleSessionFactory.ts

- Replace local `GATT_BACKOFF_MS = [500, 1500]` (line 93) → `BLE_TIMING.GATT_SESSION_BACKOFF_MS`. Source: [BleSessionFactory.ts:93](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleSessionFactory.ts#L93)
- Add import for `BLE_TIMING`.

**Verify:** `git diff` — import + 1 constant replacement.

### Step 6 — Migrate BleWriteQueue.ts

- Replace `jitteredDelay(100, 50)` at line 213 → `jitteredDelay(BLE_TIMING.GATT_RETRY_BASE_MS, BLE_TIMING.GATT_RETRY_JITTER_MS)`. Source: [BleWriteQueue.ts:213](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/BleWriteQueue.ts#L213)
- Add import for `BLE_TIMING`.

**Verify:** `git diff` — import + 1 line changed.

### Step 7 — Migrate ConnectService.ts

- Replace local `GATT_BACKOFF_MS = [500, 1500, 4000]` (line 147) → `BLE_TIMING.GATT_CONNECT_BACKOFF_MS`. Source: [ConnectService.ts:147](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/ConnectService.ts#L147)
- Already imports `BLE_TIMING` (line 11).

**Verify:** `git diff` — 1 constant replacement.

### Step 8 — Migrate RecoveryService.ts

- Remove local constants (lines 12–18). Source: [RecoveryService.ts:12-18](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/services/ble/RecoveryService.ts#L12)
- Replace all usages with `BLE_TIMING.*` equivalents throughout file.
- Already imports `BLE_TIMING` (line 9).

**Verify:** `git diff` — local const removal + reference swaps. No logic changes.

### Step 9 — Migrate useBLEScanner.ts

- Replace local `DEBOUNCE_MS`/`DEBOUNCE_MS_FTUE` (lines 26–27) → `BLE_TIMING.SCAN_DEBOUNCE_MS` / `BLE_TIMING.SCAN_DEBOUNCE_MS_FTUE`. Source: [useBLEScanner.ts:26-27](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEScanner.ts#L26)
- Replace `5000` at line 107 → `BLE_TIMING.SCAN_OFFLINE_FLUSH_DEFER_MS`
- Replace `60000` at line 309 → `BLE_TIMING.SCAN_TELEMETRY_FLUSH_MS`
- Replace `1000` at line 348 → `BLE_TIMING.SANDBOX_MOCK_DISCOVERY_DELAY_MS`
- Replace `5000` at line 361/386 → `BLE_TIMING.SANDBOX_MOCK_SCAN_STOP_MS` / `BLE_TIMING.MANUAL_SCAN_TIMEOUT_MS`
- Add import for `BLE_TIMING`.
- Preserve all R-16 comments (`intentionally preserved per R-16`).

**Verify:** `git diff` — literals replaced with named constants. R-16 annotation comments preserved.

### Step 10 — Migrate useBLEBatterySweep.ts

- Replace local `THROTTLE_SCAN_ON_MS`/`THROTTLE_SCAN_OFF_MS` (lines 12–13) and `SCAN_BUDGET_WINDOW_MS` (line 41) → `BLE_TIMING.*`. Source: [useBLEBatterySweep.ts:12-13,41](file:///C:/Neogleamz/AG_SK8Lytz_App/SK8Lytz/src/hooks/ble/useBLEBatterySweep.ts#L12)
- Add import for `BLE_TIMING`.

**Verify:** `git diff` — local const removal + reference swaps.

### Step 11 — Full verification

- `npm run verify` — TSC + Jest + AST + TypeSafety pass.
- `grep -rn "setTimeout\|setInterval" src/services/Ble*.ts src/services/ble/*.ts src/hooks/ble/*.ts` — confirm zero remaining bare numeric literals in BLE pipeline files (except `enqueueDelay` which takes runtime params).

**Verify:** Zero hardcoded ms values in scoped files. All timing flows through `BLE_TIMING.*`.

## Verification Plan

1. **TSC**: `npm run verify` — no type errors from constant type widening
2. **Jest**: All existing BLE tests pass (no behavioral change)
3. **Grep audit**: `grep -rn "[0-9]\{3,5\}" src/constants/bleTimingConstants.ts` — all magic numbers now have JSDoc
4. **Reverse grep**: `grep -rn "setTimeout.*[0-9]" src/services/Ble*.ts src/services/ble/*.ts src/hooks/ble/*.ts` — only `enqueueDelay` params remain (runtime values, not magic numbers)

## Out of Scope

- UI animation delays (`setTimeout` in components for visual effects) — R-16 exemption
- Test file delays (`__tests__/` mocks)
- Crew/cloud service delays (`CrewService`, `CrewRealtime`, `CrewSessionManager`)
- `useBLEScanner.ts` RSSI_THRESHOLD / scan budget constants (not timing)
- `useBLEBatterySweep.ts` battery tier thresholds (not timing)
- Behavioral changes to any timing value — this is a pure refactor (values preserved exactly)
- `BlePingService.ts` UX dwell delay at line 148 (`duration` param) — runtime configurable, not a magic number
