/**
 * useBLE.ts — SK8Lytz Bluetooth Low Energy Engine
 *
 * Custom React hook that wraps react-native-ble-plx to provide all
 * BLE hardware interactions for the SK8Lytz LED controller ecosystem.
 *
 * Refactored using Domain-Driven Architecture (DDA):
 * This hook is now a Thin Orchestrator, dynamically routing scanning to useBLEScanner
 * and auto-recovery to useBLEAutoRecovery.
 */
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Buffer } from 'buffer';
import { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { Alert, Platform } from 'react-native';
import type { Device } from 'react-native-ble-plx';
import { ZENGGE_CHARACTERISTIC_UUID, ZENGGE_NOTIFY_UUID, ZENGGE_SERVICE_UUID, ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { AppLogger } from '../services/AppLogger';
import type { BleConnectionState, PendingRegistration } from '../types/dashboard.types';

import { checkPermission, openGlobalPermissionsModal } from '../services/PermissionService';
import { useBLEScanner } from './ble/useBLEScanner';
import { useBLEAutoRecovery } from './ble/useBLEAutoRecovery';

let BleManager: any;
let State: any;

if (Platform.OS !== 'web') {
  const blePlx = require('react-native-ble-plx');
  BleManager = blePlx.BleManager;
  State = blePlx.State;
}

let writeMutex: Promise<any> = Promise.resolve();

export interface BluetoothLowEnergyApi {
  requestPermissions(): Promise<boolean>;
  scanForPeripherals(options?: { keepAlive?: boolean, disableProbing?: boolean }): void;
  connectToDevices: (devices: Device[]) => Promise<void>;
  disconnectFromDevice: () => void;
  writeToDevice: (payload: number[], targetDeviceId?: string) => Promise<boolean | 'partial'>;
  probeDevice: (mac: string) => Promise<void>;
  connectedDevices: Device[];
  allDevices: Device[];
  setAllDevices: React.Dispatch<React.SetStateAction<Device[]>>;
  isBluetoothSupported: boolean;
  isBluetoothEnabled: boolean;
  setOnDataReceived: (callback: (deviceId: string, data: number[]) => void) => void;
  setOnHardwareProbed: (callback: (deviceId: string, config: any) => void) => void;
  onDeviceRecovered?: (deviceId: string) => void;
  setOnDeviceRecovered: (callback: (deviceId: string) => void) => void;
  droppedOutDeviceIds: string[];
  setDroppedOutDeviceIds: React.Dispatch<React.SetStateAction<string[]>>;
  pendingRegistrations: PendingRegistration[];
  clearPendingRegistrations: () => void;
  ghostedDeviceIds: string[];
  bleState: BleConnectionState;
  /** Global connection gate semaphore — exposed for consumers that need gate-awareness */
  bleGateRef: React.MutableRefObject<'IDLE' | 'SCANNING' | 'CONNECTING' | 'DISCONNECTING' | 'RECOVERING'>;
}

export default function useBLE(): BluetoothLowEnergyApi {
  const bleManager = useMemo(() => {
    if (Platform.OS === 'web') return null;
    return new BleManager();
  }, []);

  const [allDevices, setAllDevices] = useState<Device[]>([]);
  const [connectedDevices, setConnectedDevices] = useState<Device[]>([]);
  const [isBluetoothSupported, setIsBluetoothSupported] = useState(Platform.OS !== 'web');
  const [isBluetoothEnabled, setIsBluetoothEnabled] = useState(Platform.OS === 'web');
  const [dataReceivedCallback, setDataReceivedCallback] = useState<((deviceId: string, data: number[]) => void) | undefined>();
  const [deviceRecoveredCallback, setDeviceRecoveredCallback] = useState<((deviceId: string) => void) | undefined>();
  const [droppedOutDeviceIds, setDroppedOutDeviceIds] = useState<string[]>([]);
  // ── Connection Gate Semaphore ──────────────────────────────────────────────
  // ALL BLE operations (scan, connect, disconnect, recovery) must acquire this
  // gate before touching the radio. Prevents the "stampeding herd" of competing
  // GATT operations that cause Android GATT 133 errors.
  const bleGateRef = useRef<'IDLE' | 'SCANNING' | 'CONNECTING' | 'DISCONNECTING' | 'RECOVERING'>('IDLE');
  const [bleGateState, setBleGateState] = useState<'IDLE' | 'SCANNING' | 'CONNECTING' | 'DISCONNECTING' | 'RECOVERING'>('IDLE');

  // Helper: set gate in both ref (for sync checks) and state (for React re-renders)
  const setGate = useCallback((phase: 'IDLE' | 'SCANNING' | 'CONNECTING' | 'DISCONNECTING' | 'RECOVERING') => {
    bleGateRef.current = phase;
    setBleGateState(phase);
  }, []);

  useEffect(() => {
    if (__DEV__) {
      AsyncStorage.getItem('@Sk8lytz_demo_mode').then((isMock) => {
        if (Platform.OS === 'web' || isMock === 'true') {
          setIsBluetoothSupported(true);
          setIsBluetoothEnabled(true);
        }
      });
    }
  }, []);

  const disconnectListeners = useRef<Record<string, import('react-native-ble-plx').Subscription>>({});
  const dataReceivedCallbackRef = useRef<((deviceId: string, data: number[]) => void) | undefined>(undefined);
  const deviceRecoveredCallbackRef = useRef<((deviceId: string) => void) | undefined>(undefined);
  const hardwareProbedCallbackRef = useRef<((deviceId: string, config: any) => void) | undefined>(undefined);
  const connectedDevicesRef = useRef<Device[]>([]);
  const droppedOutDeviceIdsRef = useRef<string[]>([]);

  useEffect(() => {
    droppedOutDeviceIdsRef.current = droppedOutDeviceIds;
  }, [droppedOutDeviceIds]);

  useEffect(() => {
    AppLogger.updateKnownDevices(allDevices);
  }, [allDevices]);

  useEffect(() => { 
    connectedDevicesRef.current = connectedDevices; 
  }, [connectedDevices]);

  const handleNotification = (error: any, characteristic: any, deviceId: string) => {
    if (error) {
      AppLogger.warn('Notification Error', error);
      AppLogger.log('PROTOCOL_ERROR', { error: error?.message || String(error), deviceId, context: 'notification' });
      return;
    }
    if (characteristic?.value) {
      try {
        const data = Array.from(Buffer.from(characteristic.value, 'base64')) as number[];
        if (dataReceivedCallbackRef.current) {
            dataReceivedCallbackRef.current(deviceId, data);
        }
      } catch (e: any) {
        AppLogger.error('Failed to parse notification', e);
        AppLogger.log('PROTOCOL_ERROR', { error: e?.message || String(e), deviceId, context: 'parse' });
      }
    }
  };

  // Wrap handleNotification in a stable ref callback to prevent stale closures
  // when AutoRecovery re-registers notification monitors after reconnect.
  const handleNotificationRef = useRef(handleNotification);
  handleNotificationRef.current = handleNotification;

  useEffect(() => {
    if (!bleManager || Platform.OS === 'web') return;
    const subscription = bleManager.onStateChange((state: any) => {
      AppLogger.log('BLE_STATE_CHANGE', { state });
      if (state === State.Unsupported) {
        setIsBluetoothSupported(false);
      }
      setIsBluetoothEnabled(state === State.PoweredOn);
    }, true);
    return () => subscription.remove();
  }, [bleManager]);

  const probeDevice = async (mac: string): Promise<any> => {
    if (Platform.OS === 'web' || !bleManager) return null;
    
    try {
      await bleManager.connectToDevice(mac, { timeout: 5000 }).catch((e: any) => {
         if (!String(e).includes('already')) throw e;
      });
      await bleManager.discoverAllServicesAndCharacteristicsForDevice(mac);
      
      const hwConfig = await new Promise<any>((resolve) => {
        const timer = setTimeout(() => {
          sub.remove();
          AppLogger.warn(`[BLE Probe Single] Probe timed out for ${mac} after 3500ms`);
          Alert.alert('Probe Timeout', `No hardware telemetry received from ${mac} within 3500ms.`);
          resolve(null);
        }, 3500);

        const sub = bleManager.monitorCharacteristicForDevice(
          mac,
          ZENGGE_SERVICE_UUID,
          ZENGGE_NOTIFY_UUID,
          (err: any, char: any) => {
            if (err) {
              AppLogger.warn(`[BLE Probe Single] Monitor error for ${mac}`, { error: String(err) });
              return;
            }
            if (!char?.value) return;
            try {
              const raw = Array.from(Buffer.from(char.value, 'base64')) as number[];
              const parsed = ZenggeProtocol.parseHardwareSettingsResponse(raw);
              if (parsed) {
                clearTimeout(timer);
                sub.remove();
                AppLogger.log('DEVICE_DISCOVERED', { context: 'probe_success', deviceId: mac });
                resolve(parsed);
              }
            } catch (e) { /* ignore */ }
          }
        );

        setTimeout(() => {
          const qp = ZenggeProtocol.queryHardwareSettings(false);
          const b64 = Buffer.from(qp).toString('base64');
          bleManager.writeCharacteristicWithoutResponseForDevice(
            mac, ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64
          ).catch((e: any) => AppLogger.warn('[BLE Probe Single] query write failed', { error: String(e) }));
        }, 600);
      });

      return hwConfig;
    } catch (err: any) {
      AppLogger.warn(`[BLE Probe Single] Failed to probe ${mac}:`, { error: String(err) });
      return null;
    } finally {
      await bleManager.cancelDeviceConnection(mac).catch(() => {});
    }
  };

  // --- Sub-Hooks ---
  const handleOrganicDisconnect = (error: any, deviceId: string) => {
    AppLogger.warn(`[BLE] Organic disconnect/dropout for ${deviceId}`);
    AppLogger.log('DEVICE_DISCONNECTED', { id: deviceId, reason: 'dropout', error: error?.message });
    autoRecovery.initiateRecovery(deviceId);
  };

  const autoRecovery = useBLEAutoRecovery({
    bleManager,
    setConnectedDevices,
    disconnectListeners,
    handleNotification: (error: any, characteristic: any, deviceId: string) => handleNotificationRef.current(error, characteristic, deviceId),
    onOrganicDisconnect: handleOrganicDisconnect,
    bleGateRef,
  });

  const scanner = useBLEScanner({
    bleManager,
    allDevices,
    setAllDevices,
    probeDevice,
    hardwareProbedCallbackRef
  });

  // ─── Per-device MTU map ────────────────────────────────────────────────────
  // Each device may negotiate a different MTU. Using a single shared ref caused
  // the "last device wins" bug where payload chunking used the wrong MTU.
  const mtuMapRef = useRef<Map<string, number>>(new Map());
  const getDeviceMtu = (deviceId: string) => mtuMapRef.current.get(deviceId) ?? 186;

  const connectToDevices = async (devices: Device[]) => {
    if (devices.length === 0) return;

    // ── CONNECTION GATE: Reject if another BLE operation is in-flight ────────
    if (bleGateRef.current !== 'IDLE') {
      AppLogger.warn('[BLE] connectToDevices REJECTED — gate is ' + bleGateRef.current, { requestedDevices: devices.map(d => d.id) });
      return;
    }
    setGate('CONNECTING');
    try {
      let isMock = 'false';
      if (__DEV__) {
         isMock = await AsyncStorage.getItem('@Sk8lytz_demo_mode') || 'false';
      }

      if (Platform.OS === 'web' || isMock === 'true') {
        setConnectedDevices(devices);
        devices.forEach(d => {
          AppLogger.log('DEVICE_CONNECTED', { id: d.id, name: d.name, firmware: 'v2.0.1.DEMO' });
        });
        
        if (dataReceivedCallbackRef.current) {
          setTimeout(() => {
             devices.forEach(d => {
               const mockPacket = [0x66, 0x14, 0x22, 0x01, 0x01, 0x33, 0x01, 0x55, 0x66, 0x99];
               dataReceivedCallbackRef.current!(d.id, mockPacket);
             });
          }, 1000);
        }
        setGate('IDLE');
        return;
      }
      // FIX: Android thoroughly forbids GATT connections during high-duty LE scans. Must stop before connect.
      scanner.stopScanner();

      for (const device of devices) {
        try {
          const isConnected = await bleManager.isDeviceConnected(device.id);
          const conn = isConnected 
            ? device 
            : await bleManager.connectToDevice(device.id);
            
          await conn.discoverAllServicesAndCharacteristics();

          try {
            const negotiated = await conn.requestMTU(512);
            mtuMapRef.current.set(conn.id, negotiated.mtu);
            AppLogger.log('DEVICE_CONNECTED', { context: 'mtu_negotiated', mtu: negotiated.mtu, deviceId: conn.id });
          } catch (mtuErr: any) {}

          if (disconnectListeners.current[conn.id]) disconnectListeners.current[conn.id].remove();
          disconnectListeners.current[conn.id] = bleManager.onDeviceDisconnected(conn.id, (error: any) => {
            handleOrganicDisconnect(error, conn.id);
          });

          conn.monitorCharacteristicForService(
            ZENGGE_SERVICE_UUID,
            ZENGGE_NOTIFY_UUID,
            (error: any, characteristic: any) => handleNotificationRef.current(error, characteristic, conn.id)
          );

          let firmware = undefined;
          try {
            const fwChar = await conn.readCharacteristicForService(
              '0000180a-0000-1000-8000-00805f9b34fb',
              '00002a26-0000-1000-8000-00805f9b34fb'
            );
            if (fwChar && fwChar.value) {
              const rawFw = Buffer.from(fwChar.value, 'base64').toString('ascii');
              firmware = rawFw.replace(/[^\x20-\x7E]/g, '');
            }
          } catch (e) { AppLogger.warn('[BLE] Failed to read firmware characteristic', { deviceId: conn.id, error: String(e) }); }

          AppLogger.log('DEVICE_CONNECTED', { id: conn.id, name: conn.name, firmware });

          // FIX: Strictly sequence the 600ms latency before sending the 0x63 query
          await new Promise(resolve => setTimeout(resolve, 600));

          try {
            const qp = ZenggeProtocol.queryHardwareSettings(false);
            const b64 = Buffer.from(qp).toString('base64');
            await conn.writeCharacteristicWithoutResponseForService(
              ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64
            );
          } catch (e: any) {}

          // FIX: Mandatory 250ms buffer to allow Android OS to settle before next connection
          await new Promise(resolve => setTimeout(resolve, 250));

          // FIX: Add to React state ONLY AFTER GATT is fully booted to block the UI from blasting animation payloads during MTU queries
          setConnectedDevices(prev => {
            if (!prev.find(c => c.id === conn.id)) return [...prev, conn];
            return prev;
          });

        } catch (deviceError: any) {
          AppLogger.error(`FAILED TO CONNECT TO INDIVIDUAL DEVICE ${device.id}`, deviceError);
          AppLogger.log('BLE_CONNECTION_ERROR', { error: deviceError?.message || String(deviceError), deviceId: device.id, context: 'group_sync_fail' });
        }
      }
      setGate('IDLE');
    } catch (e: any) {
      AppLogger.error('FAILED TO CONNECT TO GROUP', e);
      AppLogger.log('BLE_CONNECTION_ERROR', { error: e?.message || String(e), context: 'group' });
      setGate('IDLE');
    }
  };

  const writeToDevice = async (payload: number[], targetDeviceId?: string): Promise<boolean | 'partial'> => {
    const hexString = payload.map(x => x.toString(16).toUpperCase().padStart(2, '0')).join(' ');
    AppLogger.log('RAW_PAYLOAD', { bytes: payload.length, targetDeviceId: targetDeviceId?.slice(-4), hex: hexString.substring(0, 80) });
    AppLogger.setLastTxPayload(hexString);

    // Web / no-op path: return true so optimisticWrite sees success
    if (connectedDevicesRef.current.length === 0 || Platform.OS === 'web') return true;

    const targets = targetDeviceId
      ? connectedDevicesRef.current.filter(d => d.id === targetDeviceId)
      : connectedDevicesRef.current;

    if (targets.length === 0 && targetDeviceId) {
      AppLogger.warn(`Target device ${targetDeviceId} not found in connected devices`);
      return false;
    }

    const chunkSize = Math.max(20, (targetDeviceId ? getDeviceMtu(targetDeviceId) : Math.min(...targets.map(d => getDeviceMtu(d.id)))) - 3);

    const executeWrite = async (): Promise<boolean | 'partial'> => {
      const writePromises = targets.map(async (device) => {
        // Skip ghosted (recovering) devices — track for partial write report
        if (autoRecovery.ghostedDeviceIds.includes(device.id)) {
          AppLogger.warn(`[BLE] Write SKIPPED ghosted device ${device.id}`);
          return 'ghosted';
        }
        
        const deviceMtu = getDeviceMtu(device.id);
        const deviceChunk = Math.max(20, deviceMtu - 3);
        
        try {
          for (let i = 0; i < payload.length; i += deviceChunk) {
            const chunk = payload.slice(i, i + deviceChunk);
            const base64Chunk = Buffer.from(chunk).toString('base64');
            await device.writeCharacteristicWithoutResponseForService(
              ZENGGE_SERVICE_UUID,
              ZENGGE_CHARACTERISTIC_UUID,
              base64Chunk
            );
            if (i + deviceChunk < payload.length) {
              await new Promise(resolve => setTimeout(resolve, 5));
            }
          }
          return 'success';
        } catch (writeError: any) {
          AppLogger.warn(`[BLE] Write failed for ${device.id}`, writeError?.message);
          AppLogger.log('BLE_WRITE_ERROR', { error: writeError?.message || String(writeError), target: device.id, payloadLen: payload.length });
          return 'failure';
        }
      });

      const results = await Promise.all(writePromises);
      const skippedGhosted = results.filter(r => r === 'ghosted').length;
      const allSucceeded = results.every(r => r === 'success' || r === 'ghosted') && results.some(r => r === 'success');

      // Report partial write when some devices were ghosted but others succeeded
      if (skippedGhosted > 0 && allSucceeded) return 'partial';
      return allSucceeded;
    };

    const previousMutex = writeMutex;
    const currentWrite = (async () => {
      await previousMutex.catch(() => {});
      return executeWrite();
    })();
    
    writeMutex = currentWrite;
    return currentWrite;
  };


  const disconnectFromDevice = async () => {
    // ── CONNECTION GATE: Acquire DISCONNECTING phase ─────────────────────────
    if (bleGateRef.current === 'DISCONNECTING') return; // Already tearing down
    setGate('DISCONNECTING');
    await autoRecovery.cancelAllRecoveries();

    Object.values(disconnectListeners.current).forEach(sub => {
      try { sub.remove(); } catch (e) { AppLogger.warn('[BLE] Failed to remove disconnect listener', { error: String(e) }); }
    });
    disconnectListeners.current = {};

    const staleDevices = [...connectedDevicesRef.current];

    if (staleDevices.length > 0 && Platform.OS !== 'web') {
      const disconnectPromises = staleDevices.map(async (device) => {
        try {
          await bleManager.cancelDeviceConnection(device.id).catch((e: any) => AppLogger.warn(`[BLE] Disconnect soft fail for ${device.id}`, e));
        } catch (e: any) {
          AppLogger.error(`[BLE] Fatal disconnect fault for ${device.id}`, e);
        }
      });
      await Promise.all(disconnectPromises);
      await new Promise(resolve => setTimeout(resolve, 250));
    }
    
    // Clear per-device MTU cache
    mtuMapRef.current.clear();
    setConnectedDevices([]);
    setGate('IDLE');
  };

  const derivedBleState: BleConnectionState = 
    bleGateState === 'DISCONNECTING' ? 'DISCONNECTING' :
    bleGateState === 'CONNECTING' ? 'CONNECTING' :
    scanner.scannerState === 'SCANNING' ? 'SCANNING' :
    scanner.scannerState === 'PROBING' ? 'PROBING' :
    connectedDevices.length > 0 ? 'READY' : 'IDLE';

  return useMemo(() => ({
    requestPermissions: async () => {
      const g = await checkPermission('BLUETOOTH');
      if (!g) {
        await openGlobalPermissionsModal();
        return await checkPermission('BLUETOOTH');
      }
      return true;
    },
    scanForPeripherals: scanner.scanForPeripherals,
    connectToDevices,
    writeToDevice,
    allDevices,
    setAllDevices,
    connectedDevices,
    disconnectFromDevice,
    isBluetoothSupported,
    isBluetoothEnabled,
    onDataReceived: dataReceivedCallback,
    setOnDataReceived: (callback: (deviceId: string, data: number[]) => void) => { 
        dataReceivedCallbackRef.current = callback;
        setDataReceivedCallback(() => callback);
    },
    setOnHardwareProbed: (callback: (deviceId: string, config: any) => void) => { 
        hardwareProbedCallbackRef.current = callback; 
    },
    onDeviceRecovered: deviceRecoveredCallback,
    setOnDeviceRecovered: (callback: (deviceId: string) => void) => {
        deviceRecoveredCallbackRef.current = callback;
        setDeviceRecoveredCallback(() => callback);
    },
    droppedOutDeviceIds,
    setDroppedOutDeviceIds,
    pendingRegistrations: scanner.pendingRegistrations,
    clearPendingRegistrations: () => scanner.setPendingRegistrations([]),
    probeDevice,
    ghostedDeviceIds: autoRecovery.ghostedDeviceIds,
    bleState: derivedBleState,
    bleGateRef,
  }), [
    allDevices,
    connectedDevices,
    scanner.scannerState,
    scanner.pendingRegistrations,
    isBluetoothSupported,
    isBluetoothEnabled,
    dataReceivedCallback,
    deviceRecoveredCallback,
    droppedOutDeviceIds,
    autoRecovery.ghostedDeviceIds,
    bleGateState
  ]);
}
