import { BleError, BleManager, Device, ScanMode } from 'react-native-ble-plx';

export interface BleMachineContext {
  bleManager: BleManager;
  scanCallback: (error: BleError | null, device: Device | null) => void;
  scanMode: ScanMode;
  scanServiceUUIDs: string[];
  connectedDevices: Device[];
  ghostedDeviceIds: string[];
  sweeperId?: number;
  targetMacs?: string[];
}

export type BleMachineEvent =
  | { type: 'SCAN_START'; sweeperId?: number }
  | { type: 'SCAN_STOP' }
  | { type: 'SCAN_PAUSE' }
  | { type: 'SCAN_RESUME' }
  | { type: 'CONNECT_REQUEST'; targetMacs?: string[] }
  | { type: 'CONNECT_SUCCESS'; devices: Device[] }
  | { type: 'CONNECT_FAIL' }
  | { type: 'DISCONNECT_REQUEST' }
  | { type: 'DISCONNECT_COMPLETE' }
  | { type: 'RECOVERY_START'; ghostedMacs?: string[] }
  | { type: 'RECOVERY_COMPLETE'; recoveredMacs?: string[] }
  | { type: 'RECOVERY_FAIL' }
  | { type: 'UPDATE_CONNECTED_DEVICES'; devices: Device[] }
  | { type: 'HEARTBEAT_FAIL'; deviceId: string }
  | { type: 'FORCE_IDLE' };

export type BleMachineState =
  | { value: 'IDLE'; context: BleMachineContext }
  | { value: 'SCANNING'; context: BleMachineContext }
  | { value: 'CONNECTING'; context: BleMachineContext }
  | { value: 'READY'; context: BleMachineContext }
  | { value: 'DISCONNECTING'; context: BleMachineContext }
  | { value: 'RECOVERING'; context: BleMachineContext };

export type BLEPhaseTag = BleMachineState['value'];
