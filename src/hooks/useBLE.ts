import { useState, useMemo, useEffect } from 'react';
import { PermissionsAndroid, Platform } from 'react-native';
import type { Device } from 'react-native-ble-plx';
import * as ExpoDevice from 'expo-device';

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
  disconnectFromDevice: () => void;
  connectedDevice: Device | null;
  allDevices: Device[];
  isScanning: boolean;
  isBluetoothSupported: boolean;
}

export default function useBLE(): BluetoothLowEnergyApi {
  const bleManager = useMemo(() => {
    if (Platform.OS === 'web') return null;
    return new BleManager();
  }, []);

  const [allDevices, setAllDevices] = useState<Device[]>([]);
  const [connectedDevice, setConnectedDevice] = useState<Device | null>(null);
  const [isScanning, setIsScanning] = useState(false);
  const [isBluetoothSupported, setIsBluetoothSupported] = useState(Platform.OS !== 'web');

  useEffect(() => {
    if (!bleManager || Platform.OS === 'web') return;
    const subscription = bleManager.onStateChange((state: any) => {
      if (state === State.Unsupported) {
        setIsBluetoothSupported(false);
      }
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
    
    if (Platform.OS === 'web') {
      setTimeout(() => {
        setAllDevices([
          { id: 'sim-haloz-1', name: 'SK8Lytz HALOZ (Demo)' } as Device,
          { id: 'sim-soulz-1', name: 'SK8Lytz SOULZ (Demo)' } as Device,
          { id: 'sim-unknown-1', name: 'Zengge App-110' } as Device,
        ]);
        setIsScanning(false);
      }, 1500);
      return;
    }

    bleManager.startDeviceScan(null, null, (error: any, device: any) => {
      if (error) {
        console.error(error);
        setIsScanning(false);
        return;
      }
      if (device) {
        setAllDevices((prevState) => {
          if (!isDuplicateDevice(prevState, device)) {
            return [...prevState, device];
          }
          return prevState;
        });
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
        setConnectedDevice(device);
        return;
      }
      const deviceConnection = await bleManager.connectToDevice(device.id);
      setConnectedDevice(deviceConnection);
      await deviceConnection.discoverAllServicesAndCharacteristics();
      bleManager.stopDeviceScan();
      setIsScanning(false);
    } catch (e) {
      console.error('FAILED TO CONNECT', e);
    }
  };

  const disconnectFromDevice = () => {
    if (connectedDevice) {
      if (Platform.OS !== 'web') {
        bleManager.cancelDeviceConnection(connectedDevice.id);
      }
      setConnectedDevice(null);
    }
  };

  return {
    scanForPeripherals,
    requestPermissions,
    connectToDevice,
    allDevices,
    connectedDevice,
    disconnectFromDevice,
    isScanning,
    isBluetoothSupported,
  };
}
