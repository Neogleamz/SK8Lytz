import { useState, useMemo, useEffect } from 'react';
import { PermissionsAndroid, Platform } from 'react-native';
import type { Device } from 'react-native-ble-plx';
import * as ExpoDevice from 'expo-device';
import { ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID } from '../protocols/ZenggeProtocol';

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
  connectToDevice: (device: Device) => Promise<void>;
  connectToDevices: (devices: Device[]) => Promise<void>;
  disconnectFromDevice: () => void;
  writeToDevice: (payload: number[], targetDeviceId?: string) => Promise<void>;
  connectedDevices: Device[];
  allDevices: Device[];
  setAllDevices: React.Dispatch<React.SetStateAction<Device[]>>;
  isScanning: boolean;
  isBluetoothSupported: boolean;
  isBluetoothEnabled: boolean;
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
        const isZenggeName = nameLower.includes('led') || nameLower.includes('zengge') || nameLower.includes('magic') || nameLower.startsWith('sp') || nameLower.includes('sk8');

        if (hasZenggeService || isZenggeName) {
          setAllDevices((prevState) => {
            if (!isDuplicateDevice(prevState, device)) {
              return [...prevState, device];
            }
            return prevState;
          });
        }
      }
    });

    setTimeout(() => {
      bleManager.stopDeviceScan();
      setIsScanning(false);
    }, 5000);
  };

  const connectToDevice = async (device: Device) => {
    try {
      if (Platform.OS === 'web') {
        setConnectedDevices([device]);
        return;
      }
      const deviceConnection = await bleManager.connectToDevice(device.id);
      setConnectedDevices([deviceConnection]);
      await deviceConnection.discoverAllServicesAndCharacteristics();
      bleManager.stopDeviceScan();
      setIsScanning(false);
    } catch (e) {
      console.error('FAILED TO CONNECT', e);
    }
  };

  const connectToDevices = async (devices: Device[]) => {
    if (devices.length === 0) return;
    try {
      if (Platform.OS === 'web') {
        setConnectedDevices(devices);
        return;
      }
      
      const connections = await Promise.all(devices.map(device => bleManager.connectToDevice(device.id)));
      setConnectedDevices(connections);
      
      await Promise.all(connections.map(conn => conn.discoverAllServicesAndCharacteristics()));
      bleManager.stopDeviceScan();
      setIsScanning(false);
    } catch (e) {
      console.error('FAILED TO CONNECT TO GROUP', e);
    }
  };

  const writeToDevice = async (payload: number[], targetDeviceId?: string) => {
    // Hex trace for browser/debug purposes
    console.log(`[BLE WRITE]${targetDeviceId ? ` [Target: ${targetDeviceId}]` : ''}`, payload.map(x => x.toString(16).toUpperCase().padStart(2, '0')).join(' '));
    
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
    } catch (e) {
      console.warn('Write failed', e);
    }
  };

  const disconnectFromDevice = () => {
    if (connectedDevices.length > 0) {
      if (Platform.OS !== 'web') {
        connectedDevices.forEach(device => bleManager.cancelDeviceConnection(device.id));
      }
      setConnectedDevices([]);
    }
  };

  return {
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
  };
}
