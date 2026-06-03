import React, { createContext, useContext, useState, useEffect, useCallback, ReactNode } from 'react';
import { AppState, Platform } from 'react-native';
import notifee, { AndroidImportance, EventType, AndroidForegroundServiceType } from '@notifee/react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useGlobalTelemetry, GlobalTelemetryState } from '../hooks/useGlobalTelemetry';
import { useHealthTelemetry, HealthTelemetry } from '../hooks/useHealthTelemetry';
import { AppLogger } from '../services/AppLogger';

interface SessionContextValue {
  isSkateSessionActive: boolean;
  startSession: () => void;
  endSession: () => void;
  telemetry: GlobalTelemetryState;
  health: HealthTelemetry;
}

const SessionContext = createContext<SessionContextValue | null>(null);

const NOTIFICATION_CHANNEL_ID = 'sk8lytz-session';
const NOTIFICATION_ID = 'active-skate-session';

export function SessionProvider({ children }: { children: ReactNode }) {
  const [isSkateSessionActive, setIsSkateSessionActive] = useState(false);

  // 1. Hook up the core telemetry
  const health = useHealthTelemetry(isSkateSessionActive);
  const telemetry = useGlobalTelemetry(isSkateSessionActive, {
    avgBpm: health.avgBpm,
    peakBpm: health.peakBpm,
    activeCalories: health.activeCalories
  });

  // 2. Initialize iOS categories
  useEffect(() => {
    if (Platform.OS === 'ios') {
      notifee.setNotificationCategories([
        {
          id: 'session-actions',
          actions: [
            {
              id: 'end-session',
              title: '🛑 End Session',
              foreground: true,
            },
          ],
        },
      ]);
    }
  }, []);

  // 3. Synchronize React state with AsyncStorage on mount & App foreground transitions
  useEffect(() => {
    const syncSessionState = async () => {
      try {
        const val = await AsyncStorage.getItem('@sk8lytz_session_active');
        const isActive = val === 'true';
        if (isActive !== isSkateSessionActive) {
          setIsSkateSessionActive(isActive);
        }
      } catch (err) {
        AppLogger.error('Failed to sync session state from AsyncStorage', err);
      }
    };

    syncSessionState();

    const subscription = AppState.addEventListener('change', (nextAppState) => {
      if (nextAppState === 'active') {
        syncSessionState();
      }
    });

    return () => {
      subscription.remove();
    };
  }, [isSkateSessionActive]);

  // 4. Manage the Foreground Service (Android) / Background Notification (iOS)
  useEffect(() => {
    let updateInterval: ReturnType<typeof setInterval> | null = null;

    const setupNotification = async () => {
      if (!isSkateSessionActive) {
        if (updateInterval) clearInterval(updateInterval);
        if (Platform.OS === 'android') {
          await notifee.stopForegroundService();
        } else {
          await notifee.cancelNotification(NOTIFICATION_ID);
        }
        return;
      }

      if (Platform.OS === 'android') {
        // Create channel (idempotent)
        await notifee.createChannel({
          id: NOTIFICATION_CHANNEL_ID,
          name: 'Active Skate Session',
          importance: AndroidImportance.LOW,
        });
      }

      const displayNotification = async () => {
        await notifee.displayNotification({
          id: NOTIFICATION_ID,
          title: 'Skate Session Active 🟢',
          body: `Distance: ${telemetry.sessionDistanceMiles.toFixed(2)} mi | Speed: ${telemetry.gpsSpeed.toFixed(1)} mph`,
          android: {
            channelId: NOTIFICATION_CHANNEL_ID,
            asForegroundService: true,
            foregroundServiceTypes: [AndroidForegroundServiceType.FOREGROUND_SERVICE_TYPE_LOCATION],
            color: '#00F0FF',
            ongoing: true, // Cannot be dismissed by user swiping
            pressAction: {
              id: 'default',
              launchActivity: 'default',
            },
            actions: [
              {
                title: '🛑 END SESSION',
                pressAction: { id: 'end-session' }
              }
            ]
          },
          ios: {
            categoryId: 'session-actions',
            foregroundPresentationOptions: {
              badge: true,
              sound: true,
              banner: true,
              list: true,
            },
          }
        });
      };

      // Initial display
      await displayNotification();

      // Update the notification every 5 seconds with new stats
      updateInterval = setInterval(() => {
        displayNotification();
      }, 5000);
    };

    setupNotification();

    return () => {
      if (updateInterval) clearInterval(updateInterval);
    };
  }, [isSkateSessionActive, telemetry.sessionDistanceMiles, telemetry.gpsSpeed]);

  const startSession = useCallback(async () => {
    setIsSkateSessionActive(true);
    try {
      await AsyncStorage.setItem('@sk8lytz_session_active', 'true');
    } catch (err) {
      AppLogger.error('Failed to save session state to AsyncStorage', err);
    }
    AppLogger.log('APP_LOG', { event: 'session_started' });
  }, []);

  const endSession = useCallback(async () => {
    setIsSkateSessionActive(false);
    try {
      await AsyncStorage.setItem('@sk8lytz_session_active', 'false');
    } catch (err) {
      AppLogger.error('Failed to save session state to AsyncStorage', err);
    }
    AppLogger.log('APP_LOG', { event: 'session_ended' });
    if (Platform.OS === 'android') {
      notifee.stopForegroundService();
    }
  }, []);

  // 5. Handle Foreground Event Buttons from Notifee
  useEffect(() => {
    return notifee.onForegroundEvent(({ type, detail }) => {
      if (type === EventType.ACTION_PRESS && detail.pressAction?.id === 'end-session') {
        AppLogger.log('APP_LOG', { event: 'end_session_from_notification' });
        endSession();
      }
    });
  }, [endSession]);



  return (
    <SessionContext.Provider value={{ isSkateSessionActive, startSession, endSession, telemetry, health }}>
      {children}
    </SessionContext.Provider>
  );
}

export function useSession() {
  const context = useContext(SessionContext);
  if (!context) throw new Error('useSession must be used within a SessionProvider');
  return context;
}
