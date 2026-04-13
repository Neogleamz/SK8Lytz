import * as Location from 'expo-location';
import { Camera } from 'expo-camera';
import { Audio } from 'expo-av';
import * as Notifications from 'expo-notifications';
import { Platform, PermissionsAndroid } from 'react-native';

export type PermissionType = 'CAMERA' | 'MIC' | 'LOCATION' | 'NOTIFICATIONS' | 'BLUETOOTH';

export const requestPermission = async (type: PermissionType): Promise<boolean> => {
  try {
    switch (type) {
      case 'CAMERA': {
        const { status } = await Camera.requestCameraPermissionsAsync();
        return status === 'granted';
      }
      case 'MIC': {
        const { status } = await Audio.requestPermissionsAsync();
        return status === 'granted';
      }
      case 'LOCATION': {
        const { status } = await Location.requestForegroundPermissionsAsync();
        return status === 'granted';
      }
      case 'NOTIFICATIONS': {
        const { status } = await Notifications.requestPermissionsAsync();
        return status === 'granted';
      }
      case 'BLUETOOTH': {
        if (Platform.OS === 'android' && Platform.Version >= 31) {
          const result = await PermissionsAndroid.requestMultiple([
            PermissionsAndroid.PERMISSIONS.BLUETOOTH_SCAN,
            PermissionsAndroid.PERMISSIONS.BLUETOOTH_CONNECT,
            PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
          ]);
          return (
            result['android.permission.BLUETOOTH_CONNECT'] === PermissionsAndroid.RESULTS.GRANTED &&
            result['android.permission.BLUETOOTH_SCAN'] === PermissionsAndroid.RESULTS.GRANTED
          );
        } else if (Platform.OS === 'android') {
          const result = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION);
          return result === PermissionsAndroid.RESULTS.GRANTED;
        }
        // iOS handles BLE permission automatically upon first use, but we can assume true for onboarding if they click allow
        return true; 
      }
      default:
        return false;
    }
  } catch (error) {
    console.error(`Error requesting ${type} permission:`, error);
    return false;
  }
};

export const checkPermission = async (type: PermissionType): Promise<boolean> => {
  try {
    switch (type) {
      case 'CAMERA': {
        const { status } = await Camera.getCameraPermissionsAsync();
        return status === 'granted';
      }
      case 'MIC': {
        const { status } = await Audio.getPermissionsAsync();
        return status === 'granted';
      }
      case 'LOCATION': {
        const { status } = await Location.getForegroundPermissionsAsync();
        return status === 'granted';
      }
      case 'NOTIFICATIONS': {
        const { status } = await Notifications.getPermissionsAsync();
        return status === 'granted';
      }
      case 'BLUETOOTH': {
        if (Platform.OS === 'android' && Platform.Version >= 31) {
          const scan = await PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.BLUETOOTH_SCAN);
          const connect = await PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.BLUETOOTH_CONNECT);
          return scan && connect;
        } else if (Platform.OS === 'android') {
          return await PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION);
        }
        return true;
      }
      default:
        return false;
    }
  } catch (error) {
    console.warn(`Error checking ${type} permission:`, error);
    return false;
  }
};
