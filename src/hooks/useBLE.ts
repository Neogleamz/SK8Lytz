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
        let accumulatedTelemetry: any = null;

        const timer = setTimeout(() => {
          sub.remove();
          if (accumulatedTelemetry) {
             AppLogger.warn(`[BLE Probe Single] Partial telemetry for ${mac} — RF or HW missing. Returning partial.`);
             resolve(accumulatedTelemetry);
          } else {
             AppLogger.warn(`[BLE Probe Single] Probe empty timed out for ${mac} after 3500ms`);
             resolve(null);
          }
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
              
              const hwParsed = ZenggeProtocol.parseHardwareSettingsResponse(raw);
              if (hwParsed) {
                 accumulatedTelemetry = { ...accumulatedTelemetry, ...hwParsed };
              }

              const rfParsed = ZenggeProtocol.parseRfRemoteState(raw);
              if (rfParsed) {
                 accumulatedTelemetry = { 
                   ...accumulatedTelemetry, 
                   rfMode: rfParsed.mode, 
                   rfPairedCount: rfParsed.pairedCount 
                 };
              }

              if (accumulatedTelemetry?.detected && accumulatedTelemetry?.rfMode) {
                clearTimeout(timer);
                sub.remove();
                AppLogger.log('DEVICE_DISCOVERED', { context: 'probe_success_full', deviceId: mac });
                resolve(accumulatedTelemetry);
              }
            } catch (e) { /* ignore */ }
          }
        );

        setTimeout(() => {
          // Fire HW Query
          const qpHW = ZenggeProtocol.queryHardwareSettings(false);
          const b64HW = Buffer.from(qpHW).toString('base64');
          bleManager.writeCharacteristicWithoutResponseForDevice(
            mac, ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64HW
          ).catch((e: any) => AppLogger.warn('[BLE Probe Single] hw query write failed', { error: String(e) }));

          // Fire RF Query 200ms later to avoid baseband collision
          setTimeout(() => {
             const qpRF = ZenggeProtocol.queryRfRemoteState();
             const b64RF = Buffer.from(qpRF).toString('base64');
             bleManager.writeCharacteristicWithoutResponseForDevice(
               mac, ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64RF
             ).catch((e: any) => AppLogger.warn('[BLE Probe Single] rf query write failed', { error: String(e) }));
          }, 200);

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
        let conn: any = null;
        let lastErr: any = null;
        
        // FIX: The GATT 133 Retry Bumper
        for (let attempt = 1; attempt <= 2; attempt++) {
          try {
            const isConnected = await bleManager.isDeviceConnected(device.id);
            conn = isConnected 
              ? device 
              : await bleManager.connectToDevice(device.id);
            break; // Connection acquired
          } catch (e: any) {
            lastErr = e;
            if (String(e).includes('133')) {
              AppLogger.warn(`[BLE] GATT 133 congestion linking ${device.id}. Attempt ${attempt}/2...`);
              await new Promise(resolve => setTimeout(resolve, 200));
            } else {
              break; // Hard fault, don't retry
            }
          }
        }

        if (!conn) {
          AppLogger.error(`FAILED TO CONNECT TO INDIVIDUAL DEVICE ${device.id}`, lastErr);
          AppLogger.log('BLE_CONNECTION_ERROR', { error: lastErr?.message || String(lastErr), deviceId: device.id, context: 'group_sync_fail' });
          continue;
        }

        try {
          // FIX: Escalate Android connection interval to 'High' (~11.25ms) to beat RF interference
          if (Platform.OS === 'android') {
            await bleManager.requestConnectionPriorityForDevice(conn.id, 1).catch(() => {});
          }

          await conn.discoverAllServicesAndCharacteristics();

          try {
            let negotiatedMtu = 23;
            // Retry MTU negotiation up to 2 times if the Android stack botches it and returns 23
            for (let mtuAttempt = 1; mtuAttempt <= 2; mtuAttempt++) {
              try {
                const negotiated = await conn.requestMTU(512);
                negotiatedMtu = negotiated.mtu;
                if (negotiatedMtu > 23) break;
                AppLogger.warn(`[BLE] MTU glitch (23) for ${conn.id}. Retrying...`);
                await new Promise(res => setTimeout(res, 200));
              } catch (e) {
                await new Promise(res => setTimeout(res, 200));
              }
            }
            if (negotiatedMtu > 23) {
              mtuMapRef.current.set(conn.id, negotiatedMtu);
              AppLogger.log('DEVICE_CONNECTED', { context: 'mtu_negotiated', mtu: negotiatedMtu, deviceId: conn.id });
            } else {
              AppLogger.warn(`[BLE] MTU stuck at 23 for ${conn.id}. Assuming Android GATT stack limit/glitch and forcing safe default 186.`);
              mtuMapRef.current.set(conn.id, 186);
            }
          } catch (mtuErr: any) {
            AppLogger.warn(`[BLE] MTU throw for ${conn.id}. Forcing default 186.`, mtuErr);
            mtuMapRef.current.set(conn.id, 186);
          }

          if (disconnectListeners.current[conn.id]) disconnectListeners.current[conn.id].remove();
          disconnectListeners.current[conn.id] = bleManager.onDeviceDisconnected(conn.id, (error: any) => {
            handleOrganicDisconnect(error, conn.id);
          });

          conn.monitorCharacteristicForService(
            ZENGGE_SERVICE_UUID,
            ZENGGE_NOTIFY_UUID,
            (error: any, characteristic: any) => handleNotificationRef.current(error, characteristic, conn.id)
          );

          AppLogger.log('DEVICE_CONNECTED', { id: conn.id, name: conn.name });

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

    const maxSafeSize = targetDeviceId ? getDeviceMtu(targetDeviceId) - 3 : Math.min(...targets.map(d => getDeviceMtu(d.id))) - 3;
    
    if (payload.length > maxSafeSize) {
      AppLogger.warn(`[BLE] PAYLOAD TOO LARGE: ${payload.length} bytes exceeds safe MTU window of ${maxSafeSize} bytes. Rejecting to prevent hardware lockup.`);
      return false;
    }

    const executeWrite = async (): Promise<boolean | 'partial'> => {
      const liveTargets = targets.filter(device => {
        if (autoRecovery.ghostedDeviceIds.includes(device.id)) {
          AppLogger.warn(`[BLE] Write SKIPPED ghosted device ${device.id}`);
          return false;
        }
        return true;
      });
      
      const skippedGhosted = targets.length - liveTargets.length;
      if (liveTargets.length === 0) return skippedGhosted > 0 ? 'partial' : true;

      let allSucceeded = true;
      const base64Full = Buffer.from(payload).toString('base64');
      
      // Sending payload atomically in a single write packet without chunking or mid-transmission delays
      // This matches hardware constraint: each write packet is treated as an independent complete command
      for (const device of liveTargets) {
        try {
          await device.writeCharacteristicWithoutResponseForService(
            ZENGGE_SERVICE_UUID,
            ZENGGE_CHARACTERISTIC_UUID,
            base64Full
          );
        } catch (writeError: any) {
          AppLogger.warn(`[BLE] Write failed for ${device.id}`, writeError?.message);
          allSucceeded = false;
        }
      }

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
    setOnDataReceived: (callback: (deviceId: string, data: number[]) => void) => { 
        dataReceivedCallbackRef.current = callback;
    },
    setOnHardwareProbed: (callback: (deviceId: string, config: any) => void) => { 
        hardwareProbedCallbackRef.current = callback; 
    },
    setOnDeviceRecovered: (callback: (deviceId: string) => void) => {
        deviceRecoveredCallbackRef.current = callback;
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
    droppedOutDeviceIds,
    autoRecovery.ghostedDeviceIds,
    bleGateState
  ]);
}
