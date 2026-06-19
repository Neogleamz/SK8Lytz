import { BleError, BleManager, Characteristic, Device, ScanMode } from 'react-native-ble-plx';
import { WritePriority } from '../BleWriteQueue';
import type { IControllerProtocol } from '../../protocols/IControllerProtocol';

export interface BleMachineContext {
  bleManager: BleManager;
  scanCallback: (error: BleError | null, device: Device | null) => void;
  scanMode: ScanMode;
  scanServiceUUIDs: string[] | null;
  connectedDevices: Device[];
  ghostedDeviceIds: string[];
  sweeperId?: number;
  targetMacs?: string[];
  registeredMacs?: string[];
  adapterMapRef: { current: Map<string, IControllerProtocol> };
  mtuMapRef: { current: Map<string, number> };
  disconnectListeners: { current: Record<string, import('react-native-ble-plx').Subscription> };
  /** H2 fix: notification monitor subscriptions keyed by device ID for cleanup on reconnect */
  notificationListeners: { current: Record<string, import('react-native-ble-plx').Subscription> };
  blacklistedMacsRef: { current: string[] };
  handleOrganicDisconnect: (error: BleError | null, deviceId: string) => void;
  /** Fires when a device drops unexpectedly — wired by useBLE.ts to send RECOVERY_START */
  onOrganicDisconnect: (deviceId: string) => void;
  handleNotification: (error: BleError | null, characteristic: Characteristic | null, deviceId: string) => void;
  enqueueWrite: (
    priority: WritePriority,
    op: () => Promise<boolean | 'partial'>,
    generation?: number
  ) => Promise<boolean | 'partial'>;
  /** Lookup a device from the scan cache by MAC — wired by useBLE for RecoveryService Phase 3 */
  getSweepedDevice?: (deviceId: string) => Device | undefined;
}

export type BleMachineEvent =
  | { type: 'SCAN_START'; sweeperId?: number }
  | { type: 'SCAN_STOP' }
  | { type: 'SCAN_PAUSE' }
  | { type: 'SCAN_RESUME' }
  | { type: 'CONNECT_REQUEST'; targetMacs: string[] }
  | { type: 'DISCONNECT_REQUEST' }
  | { type: 'DISCONNECT_COMPLETE' }
  | { type: 'RECOVERY_START'; ghostedMacs?: string[] }
  | { type: 'RECOVERY_COMPLETE'; devices?: Device[] }
  | { type: 'RECOVERY_FAIL' }
  | { type: 'UPDATE_CONNECTED_DEVICES'; devices: Device[] }
  | { type: 'HEARTBEAT_FAIL'; deviceId: string }
  | { type: 'RECOVERY_PERMANENTLY_FAILED'; deviceId: string }
  | { type: 'FORCE_IDLE' }
  | { type: 'RESTORE_PERIPHERALS'; peripherals: Device[] }
  | { type: 'xstate.done.actor.connectService'; output: { devices: Device[] } };

export type BleMachineState =
  | { value: 'IDLE'; context: BleMachineContext }
  | { value: 'SCANNING'; context: BleMachineContext }
  | { value: 'CONNECTING'; context: BleMachineContext }
  | { value: 'READY'; context: BleMachineContext }
  | { value: 'DISCONNECTING'; context: BleMachineContext }
  | { value: 'RESTORING'; context: BleMachineContext }
  | { value: 'RECOVERING'; context: BleMachineContext };

export type BLEPhaseTag = BleMachineState['value'];
