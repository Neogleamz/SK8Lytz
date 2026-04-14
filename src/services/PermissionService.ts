import { Audio } from 'expo-av';
import { Camera } from 'expo-camera';
import * as Location from 'expo-location';
import * as Notifications from 'expo-notifications';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { PermissionsAndroid, Platform } from 'react-native';
import { AppLogger } from './AppLogger';

export type PermissionType = 'CAMERA' | 'MIC' | 'LOCATION' | 'NOTIFICATIONS' | 'BLUETOOTH';

export const OPTOUT_LEDGER_KEY = '@sk8lytz_permissions_optout';

export const DEFAULT_LEDGER: Record<PermissionType, boolean> = {
  CAMERA: false,
  MIC: false,
  LOCATION: false,
  NOTIFICATIONS: false,
  BLUETOOTH: false,
};

export const getOptOutLedger = async (): Promise<Record<PermissionType, boolean>> => {
  try {
    const data = await AsyncStorage.getItem(OPTOUT_LEDGER_KEY);
    return data ? JSON.parse(data) : DEFAULT_LEDGER;
  } catch {
    return DEFAULT_LEDGER;
  }
};

export const setPermissionOptOut = async (type: PermissionType, isOptedOut: boolean): Promise<void> => {
  const ledger = await getOptOutLedger();
  ledger[type] = isOptedOut;
  await AsyncStorage.setItem(OPTOUT_LEDGER_KEY, JSON.stringify(ledger));
  
  // Immutably log to Cloud Ledger
  AppLogger.log(isOptedOut ? 'PERMISSION_OPT_OUT' : 'PERMISSION_OPT_IN', { feature: type, source: 'app_toggle' });
};

export const syncSystemPermissions = async (): Promise<void> => {
  const ledger = await getOptOutLedger();
  const types: PermissionType[] = ['CAMERA', 'MIC', 'LOCATION', 'NOTIFICATIONS', 'BLUETOOTH'];
  let changed = false;

  for (const type of types) {
    const isNativelyGranted = await checkPermissionNative(type);
    const isLexicallyOptedOut = !!ledger[type];

    // OS Desync Sweep: App thought "Opt-In", but Native OS is strongly "Denied"
    if (!isNativelyGranted && !isLexicallyOptedOut) {
      ledger[type] = true;
      changed = true;
      AppLogger.log('PERMISSION_OPT_OUT', { feature: type, source: 'native_os_sync' });
    }
  }

  if (changed) {
    await AsyncStorage.setItem(OPTOUT_LEDGER_KEY, JSON.stringify(ledger));
  }
};

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

const checkPermissionNative = async (type: PermissionType): Promise<boolean> => {
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
    console.warn(`Error checking ${type} permission natively:`, error);
    return false;
  }
};

export const checkPermission = async (type: PermissionType): Promise<boolean> => {
  const ledger = await getOptOutLedger();
  if (ledger[type]) return false; // App-Level Soft Revoke wins completely

  return await checkPermissionNative(type);
};
