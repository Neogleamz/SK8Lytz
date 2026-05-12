import { requestRecordingPermissionsAsync, getRecordingPermissionsAsync } from 'expo-audio';
import * as Location from 'expo-location';

import AsyncStorage from '@react-native-async-storage/async-storage';
import { PermissionsAndroid, Platform, DeviceEventEmitter } from 'react-native';
import { AppLogger } from './AppLogger';

export const SHOW_GLOBAL_PERMISSIONS_EVENT = 'SHOW_GLOBAL_PERMISSIONS_EVENT';
export const GLOBAL_PERMISSIONS_CLOSED_EVENT = 'GLOBAL_PERMISSIONS_CLOSED_EVENT';
export const openGlobalPermissionsModal = (): Promise<void> => {
  return new Promise((resolve) => {
    const listener = DeviceEventEmitter.addListener(GLOBAL_PERMISSIONS_CLOSED_EVENT, () => {
      listener.remove();
      resolve();
    });
    DeviceEventEmitter.emit(SHOW_GLOBAL_PERMISSIONS_EVENT);
  });
};

export type PermissionType = 'CAMERA' | 'MIC' | 'LOCATION' | 'NOTIFICATIONS' | 'BLUETOOTH' | 'HEALTH';

export const OPTOUT_LEDGER_KEY = '@sk8lytz_permissions_optout';

export const DEFAULT_LEDGER: Record<PermissionType, boolean> = {
  CAMERA: false,
  MIC: false,
  LOCATION: false,
  NOTIFICATIONS: false,
  BLUETOOTH: false,
  HEALTH: false,
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
  // DEPRECATED: This used to aggressively sweep OS permissions and write to the
  // opt-out ledger if the OS said 'Denied'. However, on a fresh install, OS stats
  // are 'Undetermined' which evaluates as false, causing the sweep to lock users OUT
  // of all permissions before they were even prompted.
};

export const requestPermission = async (type: PermissionType): Promise<boolean> => {
  try {
    switch (type) {
      case 'CAMERA': {
        if (Platform.OS === 'android') {
          const result = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.CAMERA);
          return result === PermissionsAndroid.RESULTS.GRANTED;
        }
        return true; // iOS: Camera permission handled natively on first use
      }
      case 'MIC': {
        const { status } = await requestRecordingPermissionsAsync();
        return status === 'granted';
      }
      case 'LOCATION': {
        const { status } = await Location.requestForegroundPermissionsAsync();
        return status === 'granted';
      }
      case 'NOTIFICATIONS': {
        if (Platform.OS === 'web') return false;
        const Notifications = require('expo-notifications');
        const { status } = await Notifications.requestPermissionsAsync();
        if (status === 'granted') {
          // Import inline to avoid circular dependency issues at the module level
          const { notificationService } = require('./NotificationService');
          notificationService.init(true).catch(() => {});
        }
        return status === 'granted';
      }
      case 'BLUETOOTH': {
        if (Platform.OS === 'android' && Platform.Version >= 31) {
          // FIX: On Android 12+, we no longer request ACCESS_FINE_LOCATION for BLE.
          // Requesting it without ACCESS_COARSE_LOCATION causes an instant rejection
          // and the system prompt will never appear.
          const result = await PermissionsAndroid.requestMultiple([
            PermissionsAndroid.PERMISSIONS.BLUETOOTH_SCAN,
            PermissionsAndroid.PERMISSIONS.BLUETOOTH_CONNECT,
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
      case 'HEALTH': {
        if (Platform.OS === 'web') return false;
        if (Platform.OS === 'ios') {
          return new Promise<boolean>((resolve) => {
            const AppleHealthKit = require('react-native-health').default;
            const options = {
              permissions: {
                read: [AppleHealthKit.Constants.Permissions.HeartRate, AppleHealthKit.Constants.Permissions.ActiveEnergyBurned],
              },
            };
            AppleHealthKit.initHealthKit(options, (err: string) => {
              if (err) {
                AppLogger.error('PERMISSION_SERVICE', { event: 'health_request_failed', error: err });
                resolve(false);
              } else {
                resolve(true);
              }
            });
          });
        } else if (Platform.OS === 'android') {
          // Android: Request Activity Recognition
          const result = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.ACTIVITY_RECOGNITION);
          return result === PermissionsAndroid.RESULTS.GRANTED;
        }
        return false;
      }
      default:
        return false;
    }
  } catch (error) {
    AppLogger.error('PERMISSION_SERVICE', { event: 'request_failed', type, error: String(error) });
    return false;
  }
};

const checkPermissionNative = async (type: PermissionType): Promise<boolean> => {
  try {
    switch (type) {
      case 'CAMERA': {
        if (Platform.OS === 'android') {
          return await PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.CAMERA);
        }
        return true; // iOS: Camera permission handled natively on first use
      }
      case 'MIC': {
        const { status } = await getRecordingPermissionsAsync();
        return status === 'granted';
      }
      case 'LOCATION': {
        const { status } = await Location.getForegroundPermissionsAsync();
        return status === 'granted';
      }
      case 'NOTIFICATIONS': {
        if (Platform.OS === 'web') return false;
        const Notifications = require('expo-notifications');
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
      case 'HEALTH': {
        if (Platform.OS === 'web') return false;
        if (Platform.OS === 'ios') {
          return true; // Assume granted if not opted out
        } else if (Platform.OS === 'android') {
          return await PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.ACTIVITY_RECOGNITION);
        }
        return false;
      }
      default:
        return false;
    }
  } catch (error) {
    AppLogger.warn('PERMISSION_SERVICE', { event: 'check_failed', type, error: String(error) });
    return false;
  }
};

export const checkPermission = async (type: PermissionType): Promise<boolean> => {
  const ledger = await getOptOutLedger();
  if (ledger[type]) return false; // App-Level Soft Revoke wins completely

  return await checkPermissionNative(type);
};
