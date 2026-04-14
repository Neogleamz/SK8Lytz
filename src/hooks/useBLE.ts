/**
 * useBLE.ts — SK8Lytz Bluetooth Low Energy Engine
 *
 * Custom React hook that wraps react-native-ble-plx to provide all
 * BLE hardware interactions for the SK8Lytz LED controller ecosystem.
 *
 * Refactored using Domain-Driven Architecture (DDA):
 * This hook is now a Thin Orchestrator, dynamically routing scanning to useBLEScanner
 * and watchdog connection health to useBLEWatchdog.
 */
import { useState, useMemo, useEffect, useRef } from 'react';
import { Platform } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { Buffer } from 'buffer';
import type { Device } from 'react-native-ble-plx';
import { AppLogger } from '../services/AppLogger';
import { ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, ZENGGE_NOTIFY_UUID, ZenggeProtocol } from '../protocols/ZenggeProtocol';
import type { PendingRegistration } from '../types/dashboard.types';

import { requestPermissions } from '../utils/blePermissions';
import { useBLEScanner } from './ble/useBLEScanner';
import { useBLEWatchdog } from './ble/useBLEWatchdog';

let BleManager: any;
let State: any;

if (Platform.OS !== 'web') {
  const blePlx = require('react-native-ble-plx');
  BleManager = blePlx.BleManager;
  State = blePlx.State;
}

export interface BluetoothLowEnergyApi {
  requestPermissions(): Promise<boolean>;
  scanForPeripherals(options?: { keepAlive?: boolean }): void;
  connectToDevice: (device: Device) => Promise<string | undefined>;
  connectToDevices: (devices: Device[]) => Promise<void>;
  disconnectFromDevice: () => void;
  writeToDevice: (payload: number[], targetDeviceId?: string) => Promise<void>;
  probeDevice: (mac: string) => Promise<void>;
  connectedDevices: Device[];
  allDevices: Device[];
  setAllDevices: React.Dispatch<React.SetStateAction<Device[]>>;
  isScanning: boolean;
  isScanProbing: boolean;
  isBluetoothSupported: boolean;
  isBluetoothEnabled: boolean;
  onDataReceived?: (deviceId: string, data: number[]) => void;
  setOnDataReceived: (callback: (deviceId: string, data: number[]) => void) => void;
  setOnHardwareProbed: (callback: (deviceId: string, config: any) => void) => void;
  droppedOutDeviceIds: string[];
  setDroppedOutDeviceIds: React.Dispatch<React.SetStateAction<string[]>>;
  pendingRegistrations: PendingRegistration[];
  clearPendingRegistrations: () => void;
  isWatchdogActive: boolean;
  bleState: 'IDLE' | 'SCANNING' | 'PROBING' | 'CONNECTED';
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
  const [droppedOutDeviceIds, setDroppedOutDeviceIds] = useState<string[]>([]);

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
  const hardwareProbedCallbackRef = useRef<((deviceId: string, config: any) => void) | undefined>(undefined);
  const connectedDevicesRef = useRef<Device[]>([]);

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
    const alreadyConn = await bleManager.isDeviceConnected(mac).catch(() => false);
    
    try {
      if (!alreadyConn) {
        await bleManager.connectToDevice(mac, { timeout: 5000 });
        await bleManager.discoverAllServicesAndCharacteristicsForDevice(mac);
      }
      
      const hwConfig = await new Promise<any>((resolve) => {
        const timer = setTimeout(() => {
          sub.remove();
          resolve(null);
        }, 2500);

        const sub = bleManager.monitorCharacteristicForDevice(
          mac,
          ZENGGE_SERVICE_UUID,
          ZENGGE_NOTIFY_UUID,
          (err: any, char: any) => {
            if (err || !char?.value) return;
            try {
              const raw = Array.from(Buffer.from(char.value, 'base64')) as number[];
              const parsed = ZenggeProtocol.parseHardwareSettingsResponse(raw);
              if (parsed) {
                clearTimeout(timer);
                sub.remove();
                resolve(parsed);
              }
            } catch (e) { /* ignore */ }
          }
        );

        const qp = ZenggeProtocol.queryHardwareSettings(false);
        const b64 = Buffer.from(qp).toString('base64');
        bleManager.writeCharacteristicWithoutResponseForDevice(
          mac, ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64
        ).catch((e: any) => AppLogger.warn('[BLE Probe Single] query write failed', { error: String(e) }));
      });

      return hwConfig;
    } catch (err: any) {
      AppLogger.warn(`[BLE Probe Single] Failed to probe ${mac}:`, { error: String(err) });
      return null;
    } finally {
      if (!alreadyConn) {
        await bleManager.cancelDeviceConnection(mac).catch(() => {});
      }
    }
  };

  // --- Sub-Hooks ---
  const watchdog = useBLEWatchdog({
    bleManager,
    connectedDevicesRef,
    setConnectedDevices,
    setDroppedOutDeviceIds,
    disconnectListeners,
    handleNotification
  });

  const scanner = useBLEScanner({
    bleManager,
    allDevices,
    setAllDevices,
    probeDevice,
    hardwareProbedCallbackRef
  });

  // --- Connection & Writes ---
  const connectToDevice = async (device: Device): Promise<string | undefined> => {
    try {
      const connectStartTime = Date.now();
      
      let isMock = 'false';
      if (__DEV__) {
         isMock = await AsyncStorage.getItem('@Sk8lytz_demo_mode') || 'false';
      }

      if (Platform.OS === 'web' || isMock === 'true') {
        setConnectedDevices(prev => {
          if (!prev.find(d => d.id === device.id)) return [...prev, device];
          return prev;
        });
        AppLogger.log('DEVICE_CONNECTED', { id: device.id, name: device.name, firmware: 'v2.0.1.DEMO' });
        
        if (dataReceivedCallbackRef.current) {
          setTimeout(() => {
             const mockPacket = [0x66, 0x14, 0x22, 0x01, 0x01, 0x33, 0x01, 0x55, 0x66, 0x99];
             dataReceivedCallbackRef.current!(device.id, mockPacket);
          }, 1000);
        }
        return 'v2.0.1.DEMO';
      }
      const deviceConnection = await bleManager.connectToDevice(device.id);
      setConnectedDevices([deviceConnection]);
      await deviceConnection.discoverAllServicesAndCharacteristics();

      try {
        const negotiated = await deviceConnection.requestMTU(512);
        negotiatedMtuRef.current = negotiated.mtu;
        console.log(`[BLE] MTU negotiated: ${negotiated.mtu} bytes for ${device.id}`);
      } catch (mtuErr: any) {
        AppLogger.warn('[BLE] MTU negotiation failed (continuing with default 186):', mtuErr?.message);
      }

      if (disconnectListeners.current[device.id]) disconnectListeners.current[device.id].remove();
      disconnectListeners.current[device.id] = bleManager.onDeviceDisconnected(device.id, (error: any, _d: any) => {
        AppLogger.warn(`[BLE] Device dropout detected for ${device.id}`);
        AppLogger.log('DEVICE_DISCONNECTED', { id: device.id, reason: 'dropout', error: error?.message });
        setDroppedOutDeviceIds(prev => [...prev, device.id]);
        setConnectedDevices(prev => prev.filter(c => c.id !== device.id));
        if (disconnectListeners.current[device.id]) {
          disconnectListeners.current[device.id].remove();
          delete disconnectListeners.current[device.id];
        }
      });
      
      const latencyMs = Date.now() - connectStartTime;
      AppLogger.log('PERFORMANCE_METRIC', { metricName: 'BLE_Auth_Latency', value: latencyMs, unit: 'ms', deviceId: device.id });
      
      deviceConnection.monitorCharacteristicForService(
        ZENGGE_SERVICE_UUID,
        ZENGGE_NOTIFY_UUID,
        (error: any, characteristic: any) => handleNotification(error, characteristic, device.id)
      );

      let firmware: string | undefined;
      const advFw = (device as any).firmware;
      if (advFw && typeof advFw === 'string' && advFw.length > 0) {
        firmware = advFw;
      }

      if (!firmware) {
        try {
          const fwChar = await deviceConnection.readCharacteristicForService(
            '0000180a-0000-1000-8000-00805f9b34fb',
            '00002a26-0000-1000-8000-00805f9b34fb'
          );
          if (fwChar && fwChar.value) {
            const rawFw = Buffer.from(fwChar.value, 'base64').toString('ascii');
            const clean = rawFw.replace(/[^\x20-\x7E]/g, '');
            if (clean.length > 0) firmware = clean;
          }
        } catch (e) {
          console.log(`[BLE] No standard firmware for ${device.id}`);
        }
      }

      setTimeout(async () => {
        try {
          const queryPayload = ZenggeProtocol.queryHardwareSettings(false);
          const b64 = Buffer.from(queryPayload).toString('base64');
          await deviceConnection.writeCharacteristicWithoutResponseForService(
            ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64
          );
          console.log(`[BLE] 0x63 hw query sent to ${device.id}`);
        } catch (e: any) {
          AppLogger.warn('[BLE] hw query write failed', { error: String(e) });
        }
      }, 600);

      AppLogger.log('DEVICE_CONNECTED', { id: device.id, name: device.name, firmware });

      bleManager.stopDeviceScan();
      scanner.scanForPeripherals({ keepAlive: true }); // force state off inside sub hook
      
      watchdog.startWatchdog();
      return firmware;
    } catch (e: any) {
      AppLogger.error('FAILED TO CONNECT', e);
      AppLogger.log('BLE_CONNECTION_ERROR', { error: e?.message || String(e), deviceId: device.id });
    }
  };

  const connectToDevices = async (devices: Device[]) => {
    if (devices.length === 0) return;
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
        return;
      }
      
      const connections = [];
      for (const device of devices) {
        try {
          const conn = await bleManager.connectToDevice(device.id);
          connections.push(conn);
          await conn.discoverAllServicesAndCharacteristics();

          try {
            const negotiated = await conn.requestMTU(512);
            negotiatedMtuRef.current = negotiated.mtu;
          } catch (mtuErr: any) {}

          if (disconnectListeners.current[conn.id]) disconnectListeners.current[conn.id].remove();
          disconnectListeners.current[conn.id] = bleManager.onDeviceDisconnected(conn.id, (error: any) => {
            AppLogger.warn(`[BLE] Device dropout detected for ${conn.id} in group`);
            AppLogger.log('DEVICE_DISCONNECTED', { id: conn.id, reason: 'dropout', context: 'group', error: error?.message });
            setDroppedOutDeviceIds(prev => [...prev, conn.id]);
            setConnectedDevices(prev => prev.filter(c => c.id !== conn.id));
            if (disconnectListeners.current[conn.id]) {
              disconnectListeners.current[conn.id].remove();
              delete disconnectListeners.current[conn.id];
            }
          });

          conn.monitorCharacteristicForService(
            ZENGGE_SERVICE_UUID,
            ZENGGE_NOTIFY_UUID,
            (error: any, characteristic: any) => handleNotification(error, characteristic, conn.id)
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
          } catch (e) { }

          AppLogger.log('DEVICE_CONNECTED', { id: conn.id, name: conn.name, firmware });

          const connCapture = conn;
          setTimeout(async () => {
            try {
              const qp = ZenggeProtocol.queryHardwareSettings(false);
              const b64 = Buffer.from(qp).toString('base64');
              await connCapture.writeCharacteristicWithoutResponseForService(
                ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64
              );
            } catch (e: any) {}
          }, 600);
        } catch (deviceError: any) {
          AppLogger.error(`FAILED TO CONNECT TO INDIVIDUAL DEVICE ${device.id}`, deviceError);
          AppLogger.log('BLE_CONNECTION_ERROR', { error: deviceError?.message || String(deviceError), deviceId: device.id, context: 'group_sync_fail' });
        }
      }

      setConnectedDevices(connections);
      watchdog.startWatchdog();
      bleManager.stopDeviceScan();
      scanner.scanForPeripherals({ keepAlive: true }); // force stop scan internally
    } catch (e: any) {
      AppLogger.error('FAILED TO CONNECT TO GROUP', e);
      AppLogger.log('BLE_CONNECTION_ERROR', { error: e?.message || String(e), context: 'group' });
    }
  };

  const negotiatedMtuRef = useRef<number>(186);

  const writeToDevice = async (payload: number[], targetDeviceId?: string) => {
    const hexString = payload.map(x => x.toString(16).toUpperCase().padStart(2, '0')).join(' ');
    console.log(`[BLE WRITE ${payload.length}B | MTU=${negotiatedMtuRef.current}]${targetDeviceId ? ` [→${targetDeviceId.slice(-4)}]` : ''}`, hexString.substring(0, 80));
    AppLogger.setLastTxPayload(hexString);

    if (connectedDevicesRef.current.length === 0 || Platform.OS === 'web') return;

    const targets = targetDeviceId
      ? connectedDevicesRef.current.filter(d => d.id === targetDeviceId)
      : connectedDevicesRef.current;

    if (targets.length === 0 && targetDeviceId) {
      AppLogger.warn(`Target device ${targetDeviceId} not found in connected devices`);
      return;
    }

    const chunkSize = Math.max(20, negotiatedMtuRef.current - 3);

    for (const device of targets) {
      try {
        for (let i = 0; i < payload.length; i += chunkSize) {
          const chunk = payload.slice(i, i + chunkSize);
          const base64Chunk = Buffer.from(chunk).toString('base64');
          
          await device.writeCharacteristicWithoutResponseForService(
            ZENGGE_SERVICE_UUID,
            ZENGGE_CHARACTERISTIC_UUID,
            base64Chunk
          );
          
          if (i + chunkSize < payload.length) {
            await new Promise(resolve => setTimeout(resolve, 10));
          }
        }
      } catch (writeError: any) {
        AppLogger.warn(`[BLE] Write failed for ${device.id}`, writeError?.message);
        AppLogger.log('BLE_WRITE_ERROR', { error: writeError?.message || String(writeError), target: device.id, payloadLen: payload.length });
      }
    }
  };

  const disconnectFromDevice = async () => {
    watchdog.stopWatchdog();

    Object.values(disconnectListeners.current).forEach(sub => {
      try { sub.remove(); } catch (e) {}
    });
    disconnectListeners.current = {};

    const staleDevices = [...connectedDevicesRef.current];

    if (staleDevices.length > 0 && Platform.OS !== 'web') {
      for (const device of staleDevices) {
        try {
          await bleManager.cancelDeviceConnection(device.id).catch((e: any) => AppLogger.warn(`[BLE] Disconnect soft fail for ${device.id}`, e));
        } catch (e: any) {
          AppLogger.error(`[BLE] Fatal disconnect fault for ${device.id}`, e);
        }
      }
      await new Promise(resolve => setTimeout(resolve, 250));
    }
    
    setConnectedDevices([]);
  };

  return useMemo(() => ({
    requestPermissions,
    scanForPeripherals: scanner.scanForPeripherals,
    connectToDevice,
    connectToDevices,
    writeToDevice,
    allDevices,
    setAllDevices,
    connectedDevices,
    disconnectFromDevice,
    isScanning: scanner.isScanning,
    isScanProbing: scanner.isScanProbing,
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
    droppedOutDeviceIds,
    setDroppedOutDeviceIds,
    pendingRegistrations: scanner.pendingRegistrations,
    clearPendingRegistrations: () => scanner.setPendingRegistrations([]),
    probeDevice,
    isWatchdogActive: watchdog.isWatchdogActive,
    bleState: scanner.isScanning ? 'SCANNING' : scanner.isScanProbing ? 'PROBING' : connectedDevices.length > 0 ? 'CONNECTED' : 'IDLE',
  }), [
    allDevices,
    connectedDevices,
    scanner.isScanning,
    scanner.isScanProbing,
    scanner.pendingRegistrations,
    isBluetoothSupported,
    isBluetoothEnabled,
    dataReceivedCallback,
    droppedOutDeviceIds,
    watchdog.isWatchdogActive
  ]);
}
