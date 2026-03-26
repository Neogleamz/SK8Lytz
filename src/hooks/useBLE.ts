import { useState, useMemo, useEffect } from 'react';
import { PermissionsAndroid, Platform } from 'react-native';
import type { Device } from 'react-native-ble-plx';
import * as ExpoDevice from 'expo-device';
import { ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID } from '../protocols/ZenggeProtocol';
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
        
        // Capture lowest-level inbound payload trace before ANY application decoding organically
        AppLogger.log('RAW_PAYLOAD', { dir: 'RX', hex: data.map(x => x.toString(16).padStart(2, '0').toUpperCase()).join(' '), deviceId });

        // Zengge v2 packets are wrapped: [Header, Seq, Frag, Frag, Len, Len, PayloadLen, cmdId, ...Payload]
        // We strip the wrapper for the callback for easier parsing
        if (data.length > 8) {
          const payload = data.slice(8);
          if (dataReceivedCallback) dataReceivedCallback(deviceId, payload);
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
          AppLogger.log('SCAN_FILTER_MATCH', logData);
          setAllDevices((prevState) => {
            if (!isDuplicateDevice(prevState, device)) {
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
      if (Platform.OS === 'web') {
        setConnectedDevices([device]);
        AppLogger.log('DEVICE_CONNECTED', { id: device.id, name: device.name, firmware: 'v2.0.1.DEMO' });
        return 'v2.0.1.DEMO';
      }
      const deviceConnection = await bleManager.connectToDevice(device.id);
      setConnectedDevices([deviceConnection]);
      await deviceConnection.discoverAllServicesAndCharacteristics();
      
      // Monitor for responses
      deviceConnection.monitorCharacteristicForService(
        ZENGGE_SERVICE_UUID,
        ZENGGE_CHARACTERISTIC_UUID,
        (error: any, characteristic: any) => handleNotification(error, characteristic, device.id)
      );

      // Attempt to read firmware version from standard BLE Device Information Service (180A / 2A26)
      let firmware = undefined;
      try {
        const fwChar = await deviceConnection.readCharacteristicForService(
          '0000180a-0000-1000-8000-00805f9b34fb',
          '00002a26-0000-1000-8000-00805f9b34fb'
        );
        if (fwChar && fwChar.value) {
          const rawFw = require('buffer').Buffer.from(fwChar.value, 'base64').toString('ascii');
          firmware = rawFw.replace(/[^\x20-\x7E]/g, ''); // Clean non-printable chars
        }
      } catch (e) {
        console.log(`[BLE] No standard firmware characteristic for ${device.id}`);
      }

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
      
      const connections = await Promise.all(devices.map(device => bleManager.connectToDevice(device.id)));
      setConnectedDevices(connections);
      
      await Promise.all(connections.map(conn => conn.discoverAllServicesAndCharacteristics()));

      // Monitor & Firmware check for each device
      await Promise.all(connections.map(async (conn) => {
        conn.monitorCharacteristicForService(
          ZENGGE_SERVICE_UUID,
          ZENGGE_CHARACTERISTIC_UUID,
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
      }));

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
    // Persist absolute outbound payload trace synchronously to Database Log organically
    AppLogger.log('RAW_PAYLOAD', { dir: 'TX', hex: hexString, deviceId: targetDeviceId || 'ALL' });
    
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

      await Promise.all(targets.map(device => 
        device.writeCharacteristicWithoutResponseForService(
          ZENGGE_SERVICE_UUID,
          ZENGGE_CHARACTERISTIC_UUID,
          base64Payload
        )
      ));
    } catch (e: any) {
      console.warn('Write failed', e);
      AppLogger.log('BLE_WRITE_ERROR', { error: e?.message || String(e), target: targetDeviceId, payloadHex: payload.map(x => x.toString(16).padStart(2, '0')).join(' ') });
    }
  };

  const disconnectFromDevice = () => {
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
    setOnDataReceived: (callback: (deviceId: string, data: number[]) => void) => setDataReceivedCallback(() => callback),
  }), [
    allDevices, 
    connectedDevices, 
    isScanning, 
    isBluetoothSupported, 
    isBluetoothEnabled, 
    dataReceivedCallback,
    setDataReceivedCallback
  ]);
}
