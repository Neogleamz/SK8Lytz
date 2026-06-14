import { STORAGE_PERMISSIONS_OPTOUT } from '../constants/storageKeys';
import { requestRecordingPermissionsAsync, getRecordingPermissionsAsync } from 'expo-audio';
import * as Location from 'expo-location';

import AsyncStorage from '@react-native-async-storage/async-storage';
import { PermissionsAndroid, Platform, DeviceEventEmitter } from 'react-native';
import { AppLogger } from './AppLogger';

export const SHOW_GLOBAL_PERMISSIONS_EVENT = 'SHOW_GLOBAL_PERMISSIONS_EVENT';
export const GLOBAL_PERMISSIONS_CLOSED_EVENT = 'GLOBAL_PERMISSIONS_CLOSED_EVENT';
/** Emitted after any permission toggle — listeners (e.g. DockedDock) reactively show/hide gated modes. */
export const PERMISSION_STATUS_CHANGED_EVENT = 'PERMISSION_STATUS_CHANGED_EVENT';
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

export const OPTOUT_LEDGER_KEY = STORAGE_PERMISSIONS_OPTOUT;

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
  try {
    await AsyncStorage.setItem(OPTOUT_LEDGER_KEY, JSON.stringify(ledger));
  } catch (error: unknown) {
    AppLogger.error('PERMISSION_SERVICE', { event: 'opt_out_save_failed', error: (error instanceof Error ? error.message : String(error)) });
  }
  
  // Immutably log to Cloud Ledger
  AppLogger.log(isOptedOut ? 'PERMISSION_OPT_OUT' : 'PERMISSION_OPT_IN', { feature: type, source: 'app_toggle' });

  // Notify reactive listeners (DockedDock icon visibility, etc.)
  DeviceEventEmitter.emit(PERMISSION_STATUS_CHANGED_EVENT, { type, granted: !isOptedOut });
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
          notificationService.init(true, undefined).catch(() => {});
        }
        return status === 'granted';
      }
      case 'BLUETOOTH': {
        if (Platform.OS === 'android' && Platform.Version >= 31) {
          // Reverted VS-005: Android 12+ MUST request ACCESS_FINE_LOCATION
          // because FCF1 devices advertise their UUID in mServiceData instead of mServiceUuids.
          // The native OS scanner cannot filter by mServiceData. Thus, we MUST perform an
          // unfiltered (null) scan, which strictly requires location permissions.
          const result = await PermissionsAndroid.requestMultiple([
            PermissionsAndroid.PERMISSIONS.BLUETOOTH_SCAN,
            PermissionsAndroid.PERMISSIONS.BLUETOOTH_CONNECT,
            PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
          ]);
          return (
            result['android.permission.BLUETOOTH_CONNECT'] === PermissionsAndroid.RESULTS.GRANTED &&
            result['android.permission.BLUETOOTH_SCAN'] === PermissionsAndroid.RESULTS.GRANTED &&
            result['android.permission.ACCESS_FINE_LOCATION'] === PermissionsAndroid.RESULTS.GRANTED
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
                write: [AppleHealthKit.Constants.Permissions.Workout],
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
          // 1. Request Activity Recognition permission first
          const activityResult = await PermissionsAndroid.request(
            PermissionsAndroid.PERMISSIONS.ACTIVITY_RECOGNITION
          );
          if (activityResult !== PermissionsAndroid.RESULTS.GRANTED) {
            return false;
          }

          // 2. Request Health Connect permissions
          try {
            const { initialize: initHC, requestPermission: requestHC } = require('react-native-health-connect');

            // CRITICAL: initialize() registers the ActivityResultLauncher that
            // the native HealthConnectPermissionDelegate stores in a lateinit var.
            // Without this call, requestPermission() crashes with
            // UninitializedPropertyAccessException on a native coroutine thread.
            await initHC();

            const permissionsToRequest = [
              { accessType: 'read', recordType: 'HeartRate' },
              { accessType: 'read', recordType: 'ActiveCaloriesBurned' },
              { accessType: 'write', recordType: 'ExerciseSession' },
              { accessType: 'write', recordType: 'TotalCaloriesBurned' },
              { accessType: 'write', recordType: 'Distance' },
            ];

            const result = await requestHC(permissionsToRequest);
            
            // Check if both read permissions were granted
            const hasReadHR = result.some((p: { accessType: string; recordType: string }) => p.accessType === 'read' && p.recordType === 'HeartRate');
            const hasReadCal = result.some((p: { accessType: string; recordType: string }) => p.accessType === 'read' && p.recordType === 'ActiveCaloriesBurned');
            
            return hasReadHR && hasReadCal;
          } catch (err: unknown) {
            AppLogger.error('PERMISSION_SERVICE', { event: 'health_connect_request_failed', error: (err instanceof Error ? err.message : String(err)) });
            return false;
          }
        }
        return false;
      }
      default:
        return false;
    }
  } catch (error: unknown) {
    AppLogger.error('PERMISSION_SERVICE', { event: 'request_failed', type, error: (error instanceof Error ? error.message : String(error)) });
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
          const location = await PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION);
          return scan && connect && location;
        } else if (Platform.OS === 'android') {
          return await PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION);
        }
        return true;
      }
      case 'HEALTH': {
        if (Platform.OS === 'web') return false;
        if (Platform.OS === 'ios') {
          // PLATFORM PARITY NOTE (RISK-4): Apple HealthKit deliberately does NOT
          // expose an API to query authorization status for individual data types.
          // HKAuthorizationStatus only distinguishes "not determined" vs "sharing denied/authorized",
          // and READ permission is always reported as "not determined" for privacy.
          // Returning true here is the accepted pragmatic approach — if the user denied HealthKit,
          // the actual read calls in useHealthTelemetry will silently return empty results.
          // This is functionally safe: no crash, no error — just missing data.
          return true;
        } else if (Platform.OS === 'android') {
          // 1. Check Activity Recognition permission
          const activityGranted = await PermissionsAndroid.check(PermissionsAndroid.PERMISSIONS.ACTIVITY_RECOGNITION);
          if (!activityGranted) return false;

          // 2. Check Health Connect permissions
          try {
            const { initialize: initHC, getGrantedPermissions } = require('react-native-health-connect');
            await initHC();
            
            const granted = await getGrantedPermissions();
            const hasReadHR = granted.some((p: { accessType: string; recordType: string }) => p.accessType === 'read' && p.recordType === 'HeartRate');
            const hasReadCal = granted.some((p: { accessType: string; recordType: string }) => p.accessType === 'read' && p.recordType === 'ActiveCaloriesBurned');
            
            return hasReadHR && hasReadCal;
          } catch (err: unknown) {
            AppLogger.warn('PERMISSION_SERVICE', { event: 'health_connect_check_failed', error: (err instanceof Error ? err.message : String(err)) });
            return false;
          }
        }
        return false;
      }
      default:
        return false;
    }
  } catch (error: unknown) {
    AppLogger.warn('PERMISSION_SERVICE', { event: 'check_failed', type, error: (error instanceof Error ? error.message : String(error)) });
    return false;
  }
};

export const checkPermission = async (type: PermissionType): Promise<boolean> => {
  const ledger = await getOptOutLedger();
  if (ledger[type]) return false; // App-Level Soft Revoke wins completely

  return await checkPermissionNative(type);
};
