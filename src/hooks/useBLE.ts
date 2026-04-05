/**
 * useBLE.ts — SK8Lytz Bluetooth Low Energy Engine
 *
 * Custom React hook that wraps react-native-ble-plx to provide all
 * BLE hardware interactions for the SK8Lytz LED controller ecosystem.
 *
 * Key behaviors:
 *  - Strict device filtering: Symphony (0x33/0xBF manufacturer byte),
 *    known prefixes (lednet/sk8/zg/halo/soul), or Zengge service UUID
 *  - Sequential group connection loop (for...of) — NEVER use Promise.all,
 *    Android GATT throws error 133 on concurrent pairing attempts
 *  - Firmware parsed from BLE advertisement data on discovery
 *  - Characteristic notifications start immediately on connection for RX data
 *  - write() broadcasts to all connected unless targetDeviceId is specified
 *
 * Logs: DEVICE_DISCOVERED, DEVICE_CONNECTED, BLE_CONNECTION_ERROR, BLE_WRITE_ERROR, PERFORMANCE_METRIC
 * Platform: React Native Android / Web (web returns no-op stubs)
 */
import { useState, useMemo, useEffect, useRef } from 'react';
import { PermissionsAndroid, Platform } from 'react-native';
import type { Device } from 'react-native-ble-plx';
import * as ExpoDevice from 'expo-device';
import { ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, ZENGGE_NOTIFY_UUID, ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { AppLogger } from '../services/AppLogger';

let BleManager: any;
let State: any;

if (Platform.OS !== 'web') {
  const blePlx = require('react-native-ble-plx');
  BleManager = blePlx.BleManager;
  State = blePlx.State;
}

interface BluetoothLowEnergyApi {
  requestPermissions(): Promise<boolean>;
  scanForPeripherals(): void;
  connectToDevice: (device: Device) => Promise<string | undefined>;
  connectToDevices: (devices: Device[]) => Promise<void>;
  disconnectFromDevice: () => void;
  writeToDevice: (payload: number[], targetDeviceId?: string) => Promise<void>;
  connectedDevices: Device[];
  allDevices: Device[];
  setAllDevices: React.Dispatch<React.SetStateAction<Device[]>>;
  isScanning: boolean;
  isBluetoothSupported: boolean;
  isBluetoothEnabled: boolean;
  onDataReceived?: (deviceId: string, data: number[]) => void;
  setOnDataReceived: (callback: (deviceId: string, data: number[]) => void) => void;
  droppedOutDeviceIds: string[];
  setDroppedOutDeviceIds: React.Dispatch<React.SetStateAction<string[]>>;
}

export default function useBLE(): BluetoothLowEnergyApi {
  const bleManager = useMemo(() => {
    if (Platform.OS === 'web') return null;
    return new BleManager();
  }, []);

  const [allDevices, setAllDevices] = useState<Device[]>([]);
  const [connectedDevices, setConnectedDevices] = useState<Device[]>([]);
  const [isScanning, setIsScanning] = useState(false);
  const [isBluetoothSupported, setIsBluetoothSupported] = useState(Platform.OS !== 'web');
  const [isBluetoothEnabled, setIsBluetoothEnabled] = useState(Platform.OS === 'web');
  const [dataReceivedCallback, setDataReceivedCallback] = useState<((deviceId: string, data: number[]) => void) | undefined>();
  const [droppedOutDeviceIds, setDroppedOutDeviceIds] = useState<string[]>([]);
  const disconnectListeners = useRef<Record<string, import('react-native-ble-plx').Subscription>>({});
  // Use ref (not state) so handleNotification always reads the CURRENT callback
  // without the stale closure problem — useState captures the value at subscription time
  const dataReceivedCallbackRef = useRef<((deviceId: string, data: number[]) => void) | undefined>(undefined);

  useEffect(() => {
    AppLogger.updateKnownDevices(allDevices);
  }, [allDevices]);
  const handleNotification = (error: any, characteristic: any, deviceId: string) => {
    if (error) {
      console.warn('Notification Error', error);
      AppLogger.log('PROTOCOL_ERROR', { error: error?.message || String(error), deviceId, context: 'notification' });
      return;
    }
    if (characteristic?.value) {
      try {
        /* global Buffer */
        const buffer = require('buffer').Buffer;
        const data = Array.from(buffer.from(characteristic.value, 'base64')) as number[];
        // Read from ref — always has the latest callback, no stale closure
        if (dataReceivedCallbackRef.current) {
            dataReceivedCallbackRef.current(deviceId, data);
        }
      } catch (e: any) {
        console.error('Failed to parse notification', e);
        AppLogger.log('PROTOCOL_ERROR', { error: e?.message || String(e), deviceId, context: 'parse' });
      }
    }
  };

  useEffect(() => {
    if (!bleManager || Platform.OS === 'web') return;
    const subscription = bleManager.onStateChange((state: any) => {
      console.log('BLE State Change:', state);
      if (state === State.Unsupported) {
        setIsBluetoothSupported(false);
      }
      setIsBluetoothEnabled(state === State.PoweredOn);
    }, true);
    return () => subscription.remove();
  }, [bleManager]);

  const requestAndroid31Permissions = async () => {
    const bluetoothScanPermission = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.BLUETOOTH_SCAN,
      {
        title: "Scan Permission",
        message: "App requires Bluetooth Scanning",
        buttonPositive: "OK",
      }
    );
    const bluetoothConnectPermission = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.BLUETOOTH_CONNECT,
      {
        title: "Connect Permission",
        message: "App requires Bluetooth Connecting",
        buttonPositive: "OK",
      }
    );
    return (
      bluetoothScanPermission === "granted" &&
      bluetoothConnectPermission === "granted"
    );
  };

  const requestPermissions = async () => {
    if (Platform.OS === 'web') return true;
    
    if (Platform.OS === 'android') {
      if ((ExpoDevice.platformApiLevel ?? -1) < 31) {
        const granted = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
          {
            title: "Location Permission",
            message: "App requires Location to find Bluetooth devices",
            buttonPositive: "OK",
          }
        );
        return granted === PermissionsAndroid.RESULTS.GRANTED;
      } else {
        return await requestAndroid31Permissions();
      }
    } else {
      return true;
    }
  };

  const isDuplicateDevice = (devices: Device[], nextDevice: Device) =>
    devices.findIndex(device => nextDevice.id === device.id) > -1;

  const scanForPeripherals = () => {
    if (isScanning) return;
    setIsScanning(true);
    setAllDevices([]);
    
    if (!bleManager) {
      setTimeout(() => setIsScanning(false), 500);
      return;
    }

    bleManager.startDeviceScan(null, null, (error: any, device: any) => {
      if (error) {
        console.error(error);
        setIsScanning(false);
        return;
      }
      if (device) {
        const nameLower = device.name?.toLowerCase() || '';
        const hasZenggeService = device.serviceUUIDs?.includes(ZENGGE_SERVICE_UUID);
        const manufacturerData = device.manufacturerData;

        let isSymphony = false;
        if (manufacturerData) {
          try {
            /* global Buffer */
            const buffer = require('buffer').Buffer.from(manufacturerData, 'base64');
            // Index 9 is the device type code in many Zengge advertisement packets.
            // 0x33 (51) is the standard for addressable / Symphony controllers.
            // 0xBF is also seen in some Symphony variants.
            if (buffer.length > 9 && (buffer[9] === 0x33 || buffer[9] === 0xBF)) {
              isSymphony = true;
            }
          } catch (e) {
            // Silently ignore decode errors
          }
        }

        // Strict Filter Logic:
        // We prioritizing devices that explicitly report as Symphony (SPI) 
        // or have the signature SK8 or LEDnet prefix.
        const isKnownPrefix = nameLower.startsWith('lednet') || 
                            nameLower.startsWith('sk8') ||
                            nameLower.startsWith('zg') ||
                            nameLower.startsWith('halo') ||
                            nameLower.startsWith('soul');

        const isMatch = isSymphony || isKnownPrefix || hasZenggeService;
        
        const logData = {
          id: device.id,
          name: device.name || 'Unknown',
          rssi: device.rssi,
          isSymphony,
          isKnownPrefix,
          hasZenggeService,
          serviceUUIDs: device.serviceUUIDs || [],
          manufacturerData: manufacturerData ? 'presents' : 'none'
        };

        if (isMatch) {
          setAllDevices((prevState) => {
            if (!isDuplicateDevice(prevState, device)) {
              // Parse firmware from advertisement data during scan — primary source for Zengge
              let advFirmware: string | undefined;
              let firmwareVer: number | undefined;
              let ledVersion: number | undefined;
              let bleVersion: number | undefined;
              let productId: number | undefined;
              if (manufacturerData) {
                try {
                  const { ZenggeProtocol } = require('../protocols/ZenggeProtocol');
                  const fwInfo = ZenggeProtocol.parseFirmwareFromAdvertisement(manufacturerData);
                  if (fwInfo) {
                    advFirmware = `v${fwInfo.firmwareVer}.${fwInfo.ledVersion} (BLE ${fwInfo.bleVersion})`;
                    firmwareVer = fwInfo.firmwareVer;
                    ledVersion  = fwInfo.ledVersion;
                    bleVersion  = fwInfo.bleVersion;
                    productId   = fwInfo.productId;
                    (device as any).firmware   = advFirmware;
                    (device as any).firmwareVer = firmwareVer;
                    (device as any).ledVersion  = ledVersion;
                    (device as any).bleVersion  = bleVersion;
                    (device as any).productId   = productId;
                  }
                } catch (e) { /* silently skip */ }
              }
              // Log the full discovery event with all available hardware info
              AppLogger.log('DEVICE_DISCOVERED', {
                id: device.id,
                name: device.name || 'Unknown',
                rssi: device.rssi,
                isSymphony,
                isKnownPrefix,
                hasZenggeService,
                firmware: advFirmware,
                firmwareVer,
                ledVersion,
                bleVersion,
                productId,
                serviceUUIDs: device.serviceUUIDs || [],
              });
              return [...prevState, device];
            }
            return prevState;
          });
        } else {
          // Only log rejection if it's not a complete ghost (has at least a name or service)
          if (device.name || (device.serviceUUIDs && device.serviceUUIDs.length > 0)) {
            AppLogger.log('SCAN_FILTER_REJECT', {
              ...logData,
              reason: !isSymphony && !isKnownPrefix && !hasZenggeService ? 'No matching signature' : 'Unknown'
            });
          }
        }
      }
    });

    setTimeout(() => {
      bleManager.stopDeviceScan();
      setIsScanning(false);
    }, 5000);
  };

  const connectToDevice = async (device: Device): Promise<string | undefined> => {
    try {
      const connectStartTime = Date.now();
      
      if (Platform.OS === 'web') {
        setConnectedDevices([device]);
        AppLogger.log('DEVICE_CONNECTED', { id: device.id, name: device.name, firmware: 'v2.0.1.DEMO' });
        return 'v2.0.1.DEMO';
      }
      
      const deviceConnection = await bleManager.connectToDevice(device.id);
      setConnectedDevices([deviceConnection]);
      await deviceConnection.discoverAllServicesAndCharacteristics();
      
      // Register dropout listener
      if (disconnectListeners.current[device.id]) disconnectListeners.current[device.id].remove();
      disconnectListeners.current[device.id] = bleManager.onDeviceDisconnected(device.id, (error: any, d: any) => {
        console.warn(`[BLE] Device dropout detected for ${device.id}`);
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
      
      // Monitor FF02 for responses — FF01 is write-only, FF02 is the notify characteristic
      deviceConnection.monitorCharacteristicForService(
        ZENGGE_SERVICE_UUID,
        ZENGGE_NOTIFY_UUID,
        (error: any, characteristic: any) => handleNotification(error, characteristic, device.id)
      );

      // Attempt to read firmware version from standard BLE Device Information Service (180A / 2A26)
      let firmware: string | undefined;

      // First try: advertisement data already parsed during scan
      const advFw = (device as any).firmware;
      if (advFw && typeof advFw === 'string' && advFw.length > 0) {
        firmware = advFw;
      }

      // Second try: GATT Device Information Service characteristic
      if (!firmware) {
        try {
          const fwChar = await deviceConnection.readCharacteristicForService(
            '0000180a-0000-1000-8000-00805f9b34fb',
            '00002a26-0000-1000-8000-00805f9b34fb'
          );
          if (fwChar && fwChar.value) {
            const rawFw = require('buffer').Buffer.from(fwChar.value, 'base64').toString('ascii');
            const clean = rawFw.replace(/[^\x20-\x7E]/g, '');
            if (clean.length > 0) firmware = clean;
          }
        } catch (e) {
          console.log(`[BLE] No standard firmware characteristic for ${device.id}`);
        }
      }

      // Send 0x63 hardware settings query directly via deviceConnection
      // Using deviceConnection directly — avoids ALL stale closure issues with writeToDevice/connectedDevices state
      setTimeout(async () => {
        try {
          const queryPayload = ZenggeProtocol.queryHardwareSettings(false);
          const b64 = require('buffer').Buffer.from(queryPayload).toString('base64');
          await deviceConnection.writeCharacteristicWithoutResponseForService(
            ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64
          );
          console.log(`[BLE] 0x63 hw query sent to ${device.id}`);
        } catch (e) {
          console.warn('[BLE] hw query write failed', e);
        }
      }, 600);

      AppLogger.log('DEVICE_CONNECTED', { id: device.id, name: device.name, firmware });

      bleManager.stopDeviceScan();
      setIsScanning(false);
      
      return firmware;
    } catch (e: any) {
      console.error('FAILED TO CONNECT', e);
      AppLogger.log('BLE_CONNECTION_ERROR', { error: e?.message || String(e), deviceId: device.id });
    }
  };

  const connectToDevices = async (devices: Device[]) => {
    if (devices.length === 0) return;
    try {
      if (Platform.OS === 'web') {
        setConnectedDevices(devices);
        devices.forEach(d => {
          AppLogger.log('DEVICE_CONNECTED', { id: d.id, name: d.name, firmware: 'v2.0.1.DEMO' });
        });
        return;
      }
      
      const connections = [];
      // STRICT SEQUENTIAL CONNECTION LOOP
      // WARNING: Android's native Bluetooth GATT adapter heavily penalizes concurrent `Promise.all` calls 
      // with immediate GATT 133 Exception drops. We MUST connect and discover systematically one-by-one.
      for (const device of devices) {
        try {
          const conn = await bleManager.connectToDevice(device.id);
          connections.push(conn);
          await conn.discoverAllServicesAndCharacteristics();
          
          // Register dropout listener
          if (disconnectListeners.current[conn.id]) disconnectListeners.current[conn.id].remove();
          disconnectListeners.current[conn.id] = bleManager.onDeviceDisconnected(conn.id, (error: any, d: any) => {
            console.warn(`[BLE] Device dropout detected for ${conn.id} in group`);
            AppLogger.log('DEVICE_DISCONNECTED', { id: conn.id, reason: 'dropout', context: 'group', error: error?.message });
            setDroppedOutDeviceIds(prev => [...prev, conn.id]);
            setConnectedDevices(prev => prev.filter(c => c.id !== conn.id));
            if (disconnectListeners.current[conn.id]) {
              disconnectListeners.current[conn.id].remove();
              delete disconnectListeners.current[conn.id];
            }
          });

          // Monitor FF02 for responses — FF01 is write-only, FF02 is the notify characteristic
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
              const rawFw = require('buffer').Buffer.from(fwChar.value, 'base64').toString('ascii');
              firmware = rawFw.replace(/[^\x20-\x7E]/g, '');
            }
          } catch (e) { }

          AppLogger.log('DEVICE_CONNECTED', { id: conn.id, name: conn.name, firmware });

          // Send 0x63 hw query directly via conn — no stale closure
          const connCapture = conn;
          setTimeout(async () => {
            try {
              const qp = ZenggeProtocol.queryHardwareSettings(false);
              const b64 = require('buffer').Buffer.from(qp).toString('base64');
              await connCapture.writeCharacteristicWithoutResponseForService(
                ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64
              );
              console.log(`[BLE] 0x63 hw query sent to ${connCapture.id} (group)`);
            } catch (e) { console.warn('[BLE] group hw query write failed', e); }
          }, 600);
        } catch (deviceError: any) {
          console.error(`FAILED TO CONNECT TO INDIVIDUAL DEVICE ${device.id}`, deviceError);
          AppLogger.log('BLE_CONNECTION_ERROR', { error: deviceError?.message || String(deviceError), deviceId: device.id, context: 'group_sync_fail' });
        }
      }

      setConnectedDevices(connections);

      bleManager.stopDeviceScan();
      setIsScanning(false);
    } catch (e: any) {
      console.error('FAILED TO CONNECT TO GROUP', e);
      AppLogger.log('BLE_CONNECTION_ERROR', { error: e?.message || String(e), context: 'group' });
    }
  };

  const writeToDevice = async (payload: number[], targetDeviceId?: string) => {
    const hexString = payload.map(x => x.toString(16).toUpperCase().padStart(2, '0')).join(' ');
    // Hex trace for browser/debug purposes
    console.log(`[BLE WRITE]${targetDeviceId ? ` [Target: ${targetDeviceId}]` : ''}`, hexString);
    AppLogger.setLastTxPayload(hexString);
    
    if (connectedDevices.length === 0 || Platform.OS === 'web') return;
    try {
      /* global Buffer */
      const buffer = require('buffer').Buffer;
      const base64Payload = buffer.from(payload).toString('base64');
      
      const targets = targetDeviceId 
        ? connectedDevices.filter(d => d.id === targetDeviceId) 
        : connectedDevices;

      if (targets.length === 0 && targetDeviceId) {
        console.warn(`Target device ${targetDeviceId} not found in connected devices`);
      }

      const writePromises = targets.map(device => 
        device.writeCharacteristicWithoutResponseForService(
          ZENGGE_SERVICE_UUID,
          ZENGGE_CHARACTERISTIC_UUID,
          base64Payload
        ).catch((writeError: any) => {
          console.warn(`Write failed specifically for ${device.id}`, writeError);
          AppLogger.log('BLE_WRITE_ERROR', { error: writeError?.message || String(writeError), target: device.id, payloadHex: hexString });
          return { error: writeError, failedId: device.id };
        })
      );

      const results = await Promise.all(writePromises);
      
      // If any writes failed while the device is theoretically connected, we log it.
      // E.g., device moved far away or just lost connection suddenly.
      const failures = results.filter(r => r && (r as any).error);
      if (failures.length > 0) {
        console.warn(`[BLE] Silent write failures on: ${failures.map(f => (f as any).failedId).join(', ')}`);
        // We could also proactively trigger a disconnect/dropout here if we wanted.
      }
    } catch (e: any) {
      console.warn('Fatal Write catch', e);
      AppLogger.log('BLE_WRITE_ERROR', { error: e?.message || String(e), target: targetDeviceId, payloadHex: hexString });
    }
  };

  const disconnectFromDevice = () => {
    // Clean up all physical listeners
    Object.values(disconnectListeners.current).forEach(sub => {
      try { sub.remove(); } catch (e) {}
    });
    disconnectListeners.current = {};

    const staleDevices = [...connectedDevices];
    setConnectedDevices([]);

    if (staleDevices.length > 0 && Platform.OS !== 'web') {
      staleDevices.forEach(device => {
        try {
          bleManager.cancelDeviceConnection(device.id).catch((e: any) => console.warn('Disconnect soft fail', e));
        } catch (e: any) {
          console.error('Fatal disconnect fault', e);
        }
      });
    }
  };

  return useMemo(() => ({
    scanForPeripherals,
    requestPermissions,
    connectToDevice,
    connectToDevices,
    writeToDevice,
    allDevices,
    setAllDevices,
    connectedDevices,
    disconnectFromDevice,
    isScanning,
    isBluetoothSupported,
    isBluetoothEnabled,
    onDataReceived: dataReceivedCallback,
    setOnDataReceived: (callback: (deviceId: string, data: number[]) => void) => { dataReceivedCallbackRef.current = callback; },
    droppedOutDeviceIds,
    setDroppedOutDeviceIds,
  }), [
    allDevices, 
    connectedDevices, 
    isScanning, 
    isBluetoothSupported, 
    isBluetoothEnabled, 
    dataReceivedCallback,
    setDataReceivedCallback,
    droppedOutDeviceIds,
    setDroppedOutDeviceIds
  ]);
}
