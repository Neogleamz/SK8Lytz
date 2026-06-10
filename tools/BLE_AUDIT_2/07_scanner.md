# BLE Scanner Audit (useBLEScanner.ts & useBLEBatterySweep.ts)

## 1. bleManager.startDeviceScan() calls in useBLEBatterySweep.ts
Does useBLEBatterySweep.ts call bleManager.startDeviceScan() directly ANYWHERE?
**No.** It does not call `bleManager.startDeviceScan()` anywhere. It communicates exclusively by sending events to the BLE state machine using `bleSend(...)`. For example, it sends `{ type: 'SCAN_START' }`, `{ type: 'SCAN_STOP' }`, `{ type: 'SCAN_PAUSE' }`, and `{ type: 'SCAN_RESUME' }`.

## 2. SCAN_STOP call in useBLEBatterySweep.ts
Does useBLEBatterySweep.ts call bleSend({type:'SCAN_STOP'}) to stop scanning? Quote the line.
**Yes.** It calls `bleSend({ type: 'SCAN_STOP' })` in the `stopSweeper` callback on line 114:
```typescript
bleSend({ type: 'SCAN_STOP' });
```

## 3. Battery THROTTLED duty-cycle handling
Does useBLEBatterySweep handle the THROTTLED battery tier with a duty-cycle approach? Describe it.
**Yes.** When the battery level is between 15% and 30% (threshold constants defined at lines 10-11), it classifies the battery tier as `'THROTTLED'` and runs `startThrottleCycle()`.
The throttle cycle operates as a recursive timeout loop:
1. It resumes scanning by calling `bleSend({ type: 'SCAN_RESUME' })`.
2. It keeps scanning active for 10 seconds (`THROTTLE_SCAN_ON_MS = 10_000`).
3. After 10 seconds, it pauses scanning via `bleSend({ type: 'SCAN_PAUSE' })`.
4. It waits in the paused state for 20 seconds (`THROTTLE_SCAN_OFF_MS = 20_000`) before recursively running the cycle again.

## 4. Android scan budget (4 starts per 30s)
Does useBLEBatterySweep handle the Android scan budget (max 4 starts per 30s)?
**Yes.** If the platform is Android and version >= 31, it enforces the budget within `startSweeper` (lines 79-95):
- It filters a rolling window of recent scan start timestamps (`scanStartTimestampsRef.current`) to keep only those within the last 30 seconds (`SCAN_BUDGET_WINDOW_MS = 30_000`).
- If the count of recent scans has reached `SCAN_BUDGET_MAX` (4), it defers starting the scan by setting `isSweeperActiveRef.current = false` and schedules a retry for `startSweeper()` using a `setTimeout` with a calculated delay (`msUntilBudgetResets`) until the oldest start timestamp is no longer in the window.
- If the budget is not exceeded, it records the current timestamp `Date.now()` and starts scanning.

## 5. bleManager.startDeviceScan() calls in useBLEScanner.ts
Does useBLEScanner.ts call bleManager.startDeviceScan() directly ANYWHERE?
**No.** `useBLEScanner.ts` does not call `bleManager.startDeviceScan()` directly anywhere. All scanning starts and stops are initiated through either `useBLEBatterySweep` or `bleSend` state machine events.

## 6. Wiring scanCallback through to the machine
Does useBLEScanner wire scanCallback through to the machine via bleSend or a ref?
**No.** `useBLEScanner` does not wire `scanCallback` to the machine via `bleSend` or a ref within `useBLEScanner.ts`. It merely defines `scanCallback` as a hook callback and returns it in the returned hook object, so that the parent hook/context orchestrator (which houses the state machine) can wire it directly to the native `startDeviceScan` callback.

## 7. SCAN_START for non-sweeper paths in scanForPeripherals()
Does useBLEScanner.scanForPeripherals() use bleSend({type:'SCAN_START'}) for non-sweeper paths?
**Yes.** In `scanForPeripherals` (lines 304-356), when `isSweeperActive` is false and it is not sandbox mocking, it starts scanning on line 351:
```typescript
bleSend({ type: 'SCAN_START' });
```
It then registers a timeout to stop it after 5 seconds:
```typescript
setTimeout(() => {
  bleSend({ type: 'SCAN_STOP' });
}, 5000);
```

## 8. useBLEInterrogator import
Does useBLEScanner use useBLEInterrogator for hardware probing? Quote the import.
**Yes.** The import is:
```typescript
import { useBLEInterrogator } from './useBLEInterrogator';
```

## 9. useBLEBatterySweep import
Does useBLEScanner use useBLEBatterySweep for the sweeper? Quote the import.
**Yes.** The import is:
```typescript
import { useBLEBatterySweep } from './useBLEBatterySweep';
```

## 10. `any` casts in either file
Are there any `any` casts in either file?
**No.** Neither file contains any `any` casts. Both files use strict typing and explicit type casting using `as unknown as Type` or type narrowing with `unknown` / `Record<string, unknown>`.
