import * as Location from 'expo-location';
import { AppState, Platform } from 'react-native';
import { AppLogger } from './appLogger';

const GLOBAL_NOTIFICATION_CHANNEL_ID = 'sk8lytz-global';
const GLOBAL_NOTIFICATION_ID = 'sk8lytz-global-service';

class GlobalForegroundService {
  private isRunning = false;
  private hasInitialized = false;

  async start() {
    if (this.isRunning) return;
    if (Platform.OS !== 'android') return;

    try {
      const notifeeModule = require('@notifee/react-native');
      const notifee = notifeeModule.default;
      const { AndroidImportance, AndroidForegroundServiceType } = notifeeModule;

      const { status } = await Location.getForegroundPermissionsAsync();
      if (status !== 'granted') return;

      if (!this.hasInitialized) {
        await notifee.createChannel({
          id: GLOBAL_NOTIFICATION_CHANNEL_ID,
          name: 'SK8Lytz Background Engine',
          importance: AndroidImportance.LOW,
        });
        this.hasInitialized = true;
      }

      await notifee.displayNotification({
        id: GLOBAL_NOTIFICATION_ID,
        title: 'SK8Lytz Gateway Active',
        body: 'Running in pocket mode for Crewz and Watch Sync',
        android: {
          channelId: GLOBAL_NOTIFICATION_CHANNEL_ID,
          asForegroundService: true,
          foregroundServiceTypes: [AndroidForegroundServiceType.FOREGROUND_SERVICE_TYPE_LOCATION],
          color: '#00F0FF',
          ongoing: true,
        },
      });

      this.isRunning = true;
      AppLogger.log('APP_LOG', { level: 'info', message: '[GlobalForegroundService] Started', payload_size: 0, ssi: 0 });
    } catch (e: unknown) {
      AppLogger.error('[GlobalForegroundService] Failed to start', e instanceof Error ? e : new Error(String(e)), {
        payload_size: 0,
        ssi: 0,
      });
    }
  }

  async stop() {
    if (!this.isRunning) return;
    try {
      if (Platform.OS === 'android') {
        const notifee = require('@notifee/react-native').default;
        await notifee.stopForegroundService();
      }
      this.isRunning = false;
      AppLogger.log('APP_LOG', { level: 'info', message: '[GlobalForegroundService] Stopped', payload_size: 0, ssi: 0 });
    } catch (e: unknown) {
      AppLogger.warn('[GlobalForegroundService] Failed to stop', {
        error: e instanceof Error ? e.message : String(e),
        payload_size: 0,
        ssi: 0,
      });
    }
  }
}

export const globalForegroundService = new GlobalForegroundService();

// Start it when the app boots (or when permissions are granted)
if (Platform.OS === 'android' && process.env.NODE_ENV !== 'test') {
  AppState.addEventListener('change', (state) => {
    if (state === 'active') {
      globalForegroundService.start();
    }
  });
  // Try to start immediately in case permissions are already granted
  setTimeout(() => globalForegroundService.start(), 1000);
}
