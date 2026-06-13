# BLE Pipeline Audit Report

### 1. Complete Call Graph
DashboardScreen
└── startSweeper() [C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\screens\DashboardScreen.tsx:1192]
    └── useBLEBatterySweep.startSweeper() [C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEBatterySweep.ts:51]
        └── bleManager.startDeviceScan() [C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEBatterySweep.ts:110]

DashboardScreen
└── scanForPeripherals() [C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\screens\DashboardScreen.tsx:546, 835]
    └── useBLEScanner.scanForPeripherals() [C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEScanner.ts:34]
        └── bleManager.startDeviceScan() [C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEScanner.ts:371]

DashboardScreen
└── connectToDevices() [C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\screens\DashboardScreen.tsx:543, 574, 596, 611, 645, 845, 1195]
    └── BleConnectionManager.connectToDevice() [C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\BleConnectionManager.ts:42]
        └── bleManager.connectToDevice()

DashboardScreen
└── disconnectFromDevice() [C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\screens\DashboardScreen.tsx:533]
    └── BleConnectionManager.cancelDeviceConnection() [C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\BleConnectionManager.ts:88]
        └── bleManager.cancelDeviceConnection()


### 2. State Ownership Audit
- Variable name: `isSweeperActiveRef`
  Current owner: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEBatterySweep.ts:22`
  All files that read it: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEBatterySweep.ts:51`
  All files that write it: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEBatterySweep.ts:51`
  Verdict: DELETE

- Variable name: `scannerStateRef`
  Current owner: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEScanner.ts:16`
  All files that read it: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEScanner.ts:16`
  All files that write it: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEScanner.ts:34`
  Verdict: DELETE

- Variable name: `displayConnectedDevices`
  Current owner: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\screens\DashboardScreen.tsx:251`
  All files that read it: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\screens\DashboardScreen.tsx:269, 272`
  All files that write it: None (derived)
  Verdict: MERGE INTO MACHINE

- Variable name: `isActuallyConnected`
  Current owner: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\screens\DashboardScreen.tsx:269`
  All files that read it: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\screens\DashboardScreen.tsx:276`
  All files that write it: None (derived)
  Verdict: DELETE

- Variable name: `recoveryAttempts`
  Current owner: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEAutoRecovery.ts:15`
  All files that read it: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEAutoRecovery.ts`
  All files that write it: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEAutoRecovery.ts`
  Verdict: MERGE INTO MACHINE

- Variable name: `lastHeartbeat`
  Current owner: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEHeartbeat.ts:12`
  All files that read it: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEHeartbeat.ts`
  All files that write it: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEHeartbeat.ts`
  Verdict: KEEP

- Variable name: `rssiMap`
  Current owner: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLERSSIMonitor.ts:18`
  All files that read it: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLERSSIMonitor.ts`
  All files that write it: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLERSSIMonitor.ts`
  Verdict: MERGE INTO MACHINE

- Variable name: `mutexQueue`
  Current owner: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEGattMutex.ts:8`
  All files that read it: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\BleWriteQueue.ts:45, 89`
  All files that write it: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\BleWriteQueue.ts:45, 89`
  Verdict: REPLACE WITH XSTATE SERVICE

- Variable name: `messageCounter`
  Current owner: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\protocols\ZenggeAdapter.ts:14`
  All files that read it: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\protocols\ZenggeAdapter.ts`
  All files that write it: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\protocols\ZenggeAdapter.ts`
  Verdict: KEEP


### 3. Async Path Map

- Operation name: Peripheral Scan
  Entry trigger: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useBLEScanner.ts:34`
  Exit conditions: Timeout or Manual Stop
  What cleans it up: `stopDeviceScan()` `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useBLEScanner.ts:368]`
  Verdict: MOVE TO XSTATE INVOKE

- Operation name: Battery Sweep
  Entry trigger: `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useBLEBatterySweep.ts:51`
  Exit conditions: Component unmount or manual stop
  What cleans it up: `stopSweeper()` `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useBLEBatterySweep.ts:180]`
  Verdict: MOVE TO XSTATE INVOKE

- Operation name: Device Connection
  Entry trigger: `connectToDevices()` `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\BleConnectionManager.ts:42]`
  Exit conditions: Success (`BLE_CONNECTED`), Timeout (`CONNECTION_TIMEOUT`), Failure
  What cleans it up: Connection cancellation `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\BleConnectionManager.ts:88]`
  Verdict: MOVE TO XSTATE INVOKE

- Operation name: Heartbeat Ping
  Entry trigger: Transition to 'CONNECTED' `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEHeartbeat.ts:44]`
  Exit conditions: Disconnect event
  What cleans it up: `clearInterval()` `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEHeartbeat.ts:62]`
  Verdict: MOVE TO XSTATE INVOKE

- Operation name: Auto Recovery
  Entry trigger: Disconnect event while expected connected `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEAutoRecovery.ts:57]`
  Exit conditions: Max retries reached or connection success
  What cleans it up: Timeout clearance `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEAutoRecovery.ts:82]`
  Verdict: MOVE TO XSTATE INVOKE

- Operation name: GATT Characteristic Write
  Entry trigger: Command execution `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\BleWriteDispatcher.ts:112]`
  Exit conditions: Promise resolution
  What cleans it up: Mutex release `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\BleWriteQueue.ts:89]`
  Verdict: MOVE TO XSTATE INVOKE


### 4. Bloat Inventory
- Duplicated: `bleManager.startDeviceScan()` `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useBLEScanner.ts:371]` and `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useBLEBatterySweep.ts:110]`
- Shadowed: `isActuallyConnected` check `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\screens\DashboardScreen.tsx:276]`
- Dead: `retriggerAutoConnectRef` `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useDashboardAutoConnect.ts:198]`
- Overcomplicated: `isSweeperActiveRef` `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useBLEBatterySweep.ts:22]`


### 5. XState Machine Gap Analysis
- Context fields to add: `bleManager`, `adapterMap`, `connectedDevices`, `rssiMap`, `recoveryCount` `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\ble\BleMachine.types.ts]`
- Events to add: `SCAN_START`, `SCAN_STOP`, `SWEEP_START`, `SWEEP_STOP`, `CONNECT_REQ`, `DISCONNECT_REQ`, `DEVICE_DISCOVERED`, `BATTERY_UPDATED`, `HEARTBEAT_TIMEOUT` `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\ble\BleMachine.ts]`
- States to add or rename: `idle`, `scanning`, `sweeping`, `connecting`, `connected`, `disconnecting`, `recovering` `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\ble\BleMachine.ts]`
- Actions that need to call the radio: `startDeviceScan`, `stopDeviceScan`, `connectToDevice`, `cancelDeviceConnection` `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\ble\BleMachine.ts]`
- Services (invoke) to add for: connect, recovery, heartbeat `[C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\ble\BleMachine.ts]`


### 6. File-by-File Rebuild Verdict
- `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useBLE.ts`: RENAME/RESTRUCTURE
- `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEScanner.ts`: DELETE
- `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEBatterySweep.ts`: DELETE
- `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\BleConnectionManager.ts`: REPLACE WITH XSTATE SERVICE
- `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\BleSessionFactory.ts`: KEEP AS IS
- `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\BleWriteDispatcher.ts`: REPLACE WITH XSTATE SERVICE
- `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\BleWriteQueue.ts`: DELETE
- `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\BleLifecycleManager.ts`: DELETE
- `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEAutoRecovery.ts`: REPLACE WITH XSTATE SERVICE
- `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEHeartbeat.ts`: REPLACE WITH XSTATE SERVICE
- `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLERSSIMonitor.ts`: REPLACE WITH XSTATE SERVICE
- `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEInterrogator.ts`: REPLACE WITH XSTATE SERVICE
- `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEGattMutex.ts`: DELETE
- `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\ble\BleMachine.ts`: SIMPLIFY
- `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useDashboardAutoConnect.ts`: DELETE
- `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\useDashboardGroups.ts`: KEEP AS IS
- `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\protocols\ZenggeAdapter.ts`: KEEP AS IS
- `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\protocols\BanlanxAdapter.ts`: KEEP AS IS


### 7. Suggested Phase Order
1. `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\ble\BleMachine.ts` Expansion (Add states, events, context)
2. `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEScanner.ts` & `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEBatterySweep.ts` Integration
3. `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\BleConnectionManager.ts` & `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\BleLifecycleManager.ts` Integration
4. `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEHeartbeat.ts`, `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLERSSIMonitor.ts`, `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\hooks\ble\useBLEInterrogator.ts` Integration
5. `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\BleWriteDispatcher.ts` & `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\services\BleWriteQueue.ts` Integration
6. `C:\Neogleamz\AG_SK8Lytz_App\SK8Lytz\src\screens\DashboardScreen.tsx` Cleanup
