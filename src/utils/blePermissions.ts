import { PermissionsAndroid, Platform } from 'react-native';
import * as ExpoDevice from 'expo-device';

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

export const requestPermissions = async () => {
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
