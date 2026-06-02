import React, { createContext, useContext, useState, useEffect, useCallback, ReactNode } from 'react';
import { AppState, Platform } from 'react-native';
import notifee, { AndroidImportance, EventType } from '@notifee/react-native';
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

  // 2. Manage the Android Foreground Service (Notifee)
  useEffect(() => {
    if (Platform.OS !== 'android') return;

    let updateInterval: NodeJS.Timeout | null = null;

    const setupNotification = async () => {
      if (!isSkateSessionActive) {
        if (updateInterval) clearInterval(updateInterval);
        await notifee.stopForegroundService();
        return;
      }

      // Create channel (idempotent)
      await notifee.createChannel({
        id: NOTIFICATION_CHANNEL_ID,
        name: 'Active Skate Session',
        importance: AndroidImportance.LOW,
      });

      const displayNotification = async () => {
        await notifee.displayNotification({
          id: NOTIFICATION_ID,
          title: 'Skate Session Active 🟢',
          body: `Distance: ${telemetry.sessionDistanceMiles.toFixed(2)} mi | Speed: ${telemetry.gpsSpeed.toFixed(1)} mph`,
          android: {
            channelId: NOTIFICATION_CHANNEL_ID,
            asForegroundService: true,
            color: '#00F0FF',
            ongoing: true, // Cannot be dismissed by user swiping
            actions: [
              {
                title: '🛑 END SESSION',
                pressAction: { id: 'end-session' }
              }
            ]
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

  // 3. Handle Background Action Buttons from Notifee
  useEffect(() => {
    return notifee.onForegroundEvent(({ type, detail }) => {
      if (type === EventType.ACTION_PRESS && detail.pressAction?.id === 'end-session') {
        AppLogger.log('APP_LOG', { event: 'end_session_from_notification' });
        endSession();
      }
    });
  }, []);

  const startSession = useCallback(() => {
    setIsSkateSessionActive(true);
    AppLogger.log('APP_LOG', { event: 'session_started' });
  }, []);

  const endSession = useCallback(() => {
    setIsSkateSessionActive(false);
    AppLogger.log('APP_LOG', { event: 'session_ended' });
    if (Platform.OS === 'android') {
      notifee.stopForegroundService();
    }
  }, []);

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
