import React, { createContext, useContext, useState, useEffect, useCallback, ReactNode } from 'react';
import { AppState, Platform } from 'react-native';
import notifee, { AndroidImportance, EventType, AndroidForegroundServiceType } from '@notifee/react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useGlobalTelemetry, GlobalTelemetryState } from '../hooks/useGlobalTelemetry';
import { useHealthTelemetry, HealthTelemetry } from '../hooks/useHealthTelemetry';
import { AppLogger } from '../services/AppLogger';
import { WatchBridge, WatchCommand, WatchHealthUpdate } from 'sk8lytz-watch-bridge';

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

  // 2. Initialize iOS categories + listen for watch commands
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

    // Listen for START_SESSION / STOP_SESSION commands sent from the watch
    const unsubscribeCmd = WatchBridge.addWatchCommandListener((command: WatchCommand) => {
      AppLogger.log('APP_LOG', { event: 'watch_command_received', command });
      if (command === 'START_SESSION') startSession();
      else if (command === 'STOP_SESSION') endSession();
    });

    // Listen for health telemetry relayed from the watch's HealthKit/Health Services
    const unsubscribeHealth = WatchBridge.addWatchHealthListener((update: WatchHealthUpdate) => {
      AppLogger.log('APP_LOG', {
        event: 'watch_health_received',
        heartRate: update.heartRate,
        calories: update.calories,
      });
      // Merge into phone-side health telemetry — watch data takes precedence when available
      if (update.heartRate > 0) {
        health.mergeWatchHealth?.(update.heartRate, update.calories);
      }
    });

    return () => {
      unsubscribeCmd();
      unsubscribeHealth();
    };
  // eslint-disable-next-line react-hooks/exhaustive-deps
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
    // Notify both watches — fire-and-forget, safe if no watch paired
    WatchBridge.syncSessionState({
      status: 'ACTIVE',
      startTime: new Date().toISOString(),
    }).catch((err: unknown) =>
      AppLogger.warn('WATCH_BRIDGE', { event: 'sync_failed_on_start', error: String(err) })
    );
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
    // Notify both watches the session stopped
    WatchBridge.syncSessionState({ status: 'STOPPED' }).catch((err: unknown) =>
      AppLogger.warn('WATCH_BRIDGE', { event: 'sync_failed_on_stop', error: String(err) })
    );
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
