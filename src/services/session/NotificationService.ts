import * as Location from 'expo-location';
import { AppState, Platform } from 'react-native';
import { fromCallback } from 'xstate';
import { AppLogger } from '../appLogger';
import '../GlobalForegroundService'; // Ensure it initializes on boot
import { TelemetrySnapshot, SessionPhase, SessionMachineEvent } from './SessionMachine.types';

export interface NotificationServiceInput {
  sessionPhase: SessionPhase;
  startTimeMs: number | null;
  pausedMsAccum: number;
  telemetryRef: { current: TelemetrySnapshot };
}

const NOTIFICATION_CHANNEL_ID = 'sk8lytz-session';
const NOTIFICATION_ID = 'active-skate-session';

export const notificationService = fromCallback<SessionMachineEvent, NotificationServiceInput>(({ input }) => {
  if (Platform.OS === 'web') return () => {};

  let isActive = true;
  let updateInterval: NodeJS.Timeout | null = null;
  let isDisplaying = false;

  const displayNotification = async () => {
    if (!isActive) return;
    if (isDisplaying) return;
    isDisplaying = true;

    try {
      const notifeeModule = require('@notifee/react-native');
      const notifee = notifeeModule.default;

      let hasLocationPermission = false;
      if (Platform.OS === 'android') {
        try {
          const { status } = await Location.getForegroundPermissionsAsync();
          hasLocationPermission = status === 'granted';
        } catch {
          hasLocationPermission = false;
        }
      }

      const isPaused = input.sessionPhase === 'PAUSED';
      const isEnding = input.sessionPhase === 'ENDING';

      const title = isPaused
        ? 'Skate Session Paused ⏸'
        : isEnding
        ? 'Saving Session...'
        : 'Skate Session Active 🟢';

      const actions = isEnding ? [] : [
        {
          title: '🛑 END',
          pressAction: { id: 'end-session' },
        },
        {
          title: '🎵 MUSIC',
          pressAction: { id: 'toggle-music' },
        },
        {
          title: '🔥 FAVORITE',
          pressAction: { id: 'fire-favorite' },
        },
      ];

      await notifee.displayNotification({
        id: NOTIFICATION_ID,
        title,
        body: `Distance: ${input.telemetryRef.current.sessionDistanceMiles.toFixed(2)} mi | Speed: ${input.telemetryRef.current.gpsSpeed.toFixed(1)} mph`,
        android: {
          channelId: NOTIFICATION_CHANNEL_ID,
          color: '#00F0FF',
          ongoing: true,
          pressAction: {
            id: 'default',
            launchActivity: 'default',
          },
          actions,
        },
        ios: {
          categoryId: 'session-actions',
          foregroundPresentationOptions: {
            badge: true,
            sound: true,
            banner: true,
            list: true,
          },
        },
      });

      if (!isActive) return;
    } catch (err: unknown) {
      AppLogger.error('[NotificationService] Failed to display foreground notification', err, {
        payload_size: 0,
        ssi: 0,
      });
    } finally {
      isDisplaying = false;
    }
  };

  const setupNotification = async () => {
    const notifeeModule = require('@notifee/react-native');
    const notifee = notifeeModule.default;
    const { AndroidImportance } = notifeeModule;

    if (Platform.OS === 'android') {
      await notifee.createChannel({
        id: NOTIFICATION_CHANNEL_ID,
        name: 'Active Skate Session',
        importance: AndroidImportance.LOW,
      });
    }

    await displayNotification();

    updateInterval = setInterval(() => {
      if (!isActive) return;
      displayNotification().catch((err: unknown) =>
        AppLogger.warn('[NotificationService] displayNotification interval failed', {
          error: err instanceof Error ? err.message : String(err),
          payload_size: 0,
          ssi: 0,
        })
      );
    }, 5000);
  };

  setupNotification().catch((err) =>
    AppLogger.error('[NotificationService] setupNotification failed', err, {
      payload_size: 0,
      ssi: 0,
    })
  );

  return () => {
    isActive = false;
    if (updateInterval) clearInterval(updateInterval);

    (async () => {
      try {
        if (Platform.OS !== 'web') {
          const notifee = require('@notifee/react-native').default;
          await notifee.cancelNotification(NOTIFICATION_ID);
        }
      } catch (e: unknown) {
        AppLogger.warn('[NotificationService] notification teardown failed', {
          error: e instanceof Error ? e.message : String(e),
          payload_size: 0,
          ssi: 0,
        });
      }
    })();
  };
});
