# Implementation Plan: sweep-src-hooks-ble

## Goal
Fix bugs and rule violations identified during the deep-dive code hunt for the `sweep-src-hooks-ble` domain cluster.

## Batch & Wave
- **Wave:** 1
- **Prerequisite:** None

## Proposed Changes
### [MODIFY] useBLERSSIMonitor.ts
- Line 29: Fix `R-08` violation. Uses 'any' type for the bleManager parameter in the UseBLERSSIMonitorParams interface definition. (Suggested: Provide a concrete interface or import BleManager from react-native-ble-plx.)
### [MODIFY] useBLEScanner.ts
- Line 269: Fix `R-09` violation. Logs raw device MAC address (mac) in AppLogger warning payload when failing to parse firmware from advertisement. (Suggested: Wrap mac in scrubPII(mac).)
- Line 130: Fix `R-05` violation. Orphaned Telemetry Sync Queue: Ambient scan telemetry that fails to upload is saved to STORAGE_SCANNER_TELEMETRY_QUEUE in AsyncStorage, but there is no mechanism or worker to ever read, retry, or flush this queue when online. This leads to permanent storage leakage and lost diagnostic data. (Suggested: Integrate the flushing of STORAGE_SCANNER_TELEMETRY_QUEUE into useOfflineSyncWorker.ts or add a flush routine inside useBLEScanner.ts that runs whenever a connection becomes active or periodically during scanner operation.)
### [MODIFY] useBLEInterrogator.ts
- Line 57: Fix `R-12` violation. Defines stableQueueDeviceForInterrogation with a dependency array that omits queueDeviceForInterrogation (bypassed with eslint-disable-next-line), leading to potential stale closures when onDeviceInterrogated changes. (Suggested: Add queueDeviceForInterrogation to dependency array or resolve the callback recreation.)
### [MODIFY] ble-simulator.test.ts
- Line 39: Fix `R-16` violation. Hardcoded setTimeout delay of 500ms for simulated server boot bind timing. (Suggested: None (intentional boot time delay required for bind socket setup and noted as exempt in source comments).)
### [MODIFY] useBLEBatterySweep.ts
- Line 103: Fix `R-12` violation. A setTimeout callback is scheduled for startSweeper budget reset deferral but is not tracked via a ref or cleared upon hook unmount/re-creation. If the hook unmounts or startSweeper is re-created (due to bleManager changes), the stale callback executes, causing resource leaks and state inconsistency. (Suggested: Track the budget reset timer with a useRef variable and clear it during the useEffect cleanup and when re-scheduling.)

## Verification Plan
- Run `npm run verify` to ensure type safety and tests pass.
- Run AST parser to ensure no regressions.
