import { STORAGE_SESSION_PHASE } from '../constants/storageKeys';
import React, { createContext, useContext, useState, useEffect, useCallback, ReactNode, useRef, useMemo } from 'react';
import { AppState, Platform } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import notifee, { EventType } from '@notifee/react-native';
import { useAuth } from './AuthContext';
import { useMachine } from '@xstate/react';
import { sessionMachine } from '../services/session/SessionMachine';
import { SessionBridge } from '../services/session/SessionBridge';
import { TelemetrySnapshot, HealthSnapshot } from '../services/session/SessionMachine.types';

export interface GlobalTelemetryState {
  gpsSpeed: number;
  peakGForce: number;
  sessionDistanceMiles: number;
  sessionDurationSec: number;
  sessionPeakSpeed: number;
  sessionAvgSpeed: number;
}

export interface HealthTelemetry {
  latestBpm: number | null;
  avgBpm: number | null;
  peakBpm: number | null;
  activeCalories: number | null;
  mergeWatchHealth?: (heartRate: number, calories: number) => void;
}

import { AppLogger } from '../services/appLogger';
import { WatchBridge, WatchCommand, WatchHealthUpdate } from 'sk8lytz-watch-bridge';
import { STORAGE_AUTO_PAUSE_ENABLED, STORAGE_PENDING_BG_END } from '../constants/storageKeys';

interface SessionContextValue {
  isSkateSessionActive: boolean;
  sessionPhase: 'IDLE' | 'ACTIVE' | 'PAUSED' | 'ENDING';
  startSession: () => void;
  endSession: () => void;
  telemetry: GlobalTelemetryState;
  health: HealthTelemetry;
}

const SessionContext = createContext<SessionContextValue | null>(null);

export function SessionProvider({ children }: { children: ReactNode }) {
  const [initialDataLoaded, setInitialDataLoaded] = useState(false);
  const [autoPauseEnabled, setAutoPauseEnabled] = useState(false);

  useEffect(() => {
    let active = true;
    const load = async () => {
      try {
        const enabled = await AsyncStorage.getItem(STORAGE_AUTO_PAUSE_ENABLED);
        if (active) {
          setAutoPauseEnabled(enabled !== 'false');
          setInitialDataLoaded(true);
        }
      } catch (e) {
        if (active) {
          setAutoPauseEnabled(false);
          setInitialDataLoaded(true);
        }
      }
    };
    load();
    return () => {
      active = false;
    };
  }, []);

  if (!initialDataLoaded) {
    return (
      <SessionContext.Provider
        value={{
          isSkateSessionActive: false,
          sessionPhase: 'IDLE',
          startSession: () => {},
          endSession: () => {},
          telemetry: {
            gpsSpeed: 0,
            peakGForce: 1.0,
            sessionDistanceMiles: 0,
            sessionDurationSec: 0,
            sessionPeakSpeed: 0,
            sessionAvgSpeed: 0,
          },
          health: {
            latestBpm: null,
            avgBpm: null,
            peakBpm: null,
            activeCalories: null,
          },
        }}
      >
        {children}
      </SessionContext.Provider>
    );
  }

  return (
    <SessionMachineWrapper
      key={initialDataLoaded ? 'ready' : 'loading'}
      autoPauseEnabled={autoPauseEnabled}
      initialDataLoaded={initialDataLoaded}
    >
      {children}
    </SessionMachineWrapper>
  );
}

function SessionMachineWrapper({
  children,
  autoPauseEnabled,
  initialDataLoaded,
}: {
  children: ReactNode;
  autoPauseEnabled: boolean;
  initialDataLoaded: boolean;
}) {
  const { user } = useAuth();
  const userIdRef = useRef<string | null>(null);
  useEffect(() => {
    userIdRef.current = user?.id ?? null;
  }, [user]);

  const gpsSpeedRef = useRef(0);
  const telemetryRef = useRef<TelemetrySnapshot>({
    gpsSpeed: 0,
    peakGForce: 1.0,
    sessionDistanceMiles: 0,
    sessionDurationSec: 0,
    sessionPeakSpeed: 0,
    sessionAvgSpeed: 0,
  });
  const healthRef = useRef<HealthSnapshot>({
    latestBpm: null,
    avgBpm: null,
    peakBpm: null,
    activeCalories: null,
  });

  const [telemetry, setTelemetry] = useState<GlobalTelemetryState>({
    gpsSpeed: 0,
    peakGForce: 1.0,
    sessionDistanceMiles: 0,
    sessionDurationSec: 0,
    sessionPeakSpeed: 0,
    sessionAvgSpeed: 0,
  });

  const [health, setHealth] = useState<HealthTelemetry>({
    latestBpm: null,
    avgBpm: null,
    peakBpm: null,
    activeCalories: null,
  });

  const invalidateStatsCache = () => {
    AppLogger.log('APP_LOG', { event: 'stats_cache_invalidated' });
  };

  const [snapshot, send, actorRef] = useMachine(sessionMachine, {
    input: {
      autoPauseEnabled,
      gpsSpeedRef,
      telemetryRef,
      healthRef,
      userIdRef,
      onTelemetryUpdate: (t) => {
        telemetryRef.current = t;
        gpsSpeedRef.current = t.gpsSpeed;
      },
      onHealthUpdate: (h) => {
        healthRef.current = h;
      },
      onSessionSaved: () => invalidateStatsCache(),
    },
  });

  // Keep SessionBridge in sync
  useEffect(() => {
    SessionBridge.register(send);
    return () => SessionBridge.unregister();
  }, [send]);

  const sessionPhase = snapshot.value as 'IDLE' | 'ACTIVE' | 'PAUSED' | 'ENDING';
  const isSkateSessionActive = sessionPhase === 'ACTIVE' || sessionPhase === 'PAUSED' || sessionPhase === 'ENDING';

  // UI timer tick (1s interval)
  useEffect(() => {
    if (!snapshot.matches('ACTIVE') && !snapshot.matches('PAUSED')) return;
    const id = setInterval(() => {
      const now = snapshot.value === 'PAUSED' && snapshot.context.pauseStartTimeMs
        ? snapshot.context.pauseStartTimeMs
        : Date.now();
      const elapsed = snapshot.context.startTimeMs
        ? Math.floor((now - snapshot.context.startTimeMs - snapshot.context.pausedMsAccum) / 1000)
        : 0;
      setTelemetry({
        gpsSpeed: gpsSpeedRef.current,
        peakGForce: telemetryRef.current.peakGForce,
        sessionDistanceMiles: telemetryRef.current.sessionDistanceMiles,
        sessionDurationSec: elapsed,
        sessionPeakSpeed: telemetryRef.current.sessionPeakSpeed,
        sessionAvgSpeed: telemetryRef.current.sessionAvgSpeed,
      });
      setHealth({
        latestBpm: healthRef.current.latestBpm,
        avgBpm: healthRef.current.avgBpm,
        peakBpm: healthRef.current.peakBpm,
        activeCalories: healthRef.current.activeCalories,
      });
    }, 1000);
    return () => clearInterval(id);
  }, [snapshot.value, snapshot.context.startTimeMs, snapshot.context.pausedMsAccum, snapshot.context.pauseStartTimeMs]);

  // Reset telemetry & health when IDLE
  useEffect(() => {
    if (snapshot.matches('IDLE')) {
      setTelemetry({
        gpsSpeed: 0,
        peakGForce: 1.0,
        sessionDistanceMiles: 0,
        sessionDurationSec: 0,
        sessionPeakSpeed: 0,
        sessionAvgSpeed: 0,
      });
      setHealth({
        latestBpm: null,
        avgBpm: null,
        peakBpm: null,
        activeCalories: null,
      });
    }
  }, [snapshot.value]);

  // Crash recovery
  useEffect(() => {
    let active = true;
    const recover = async () => {
      const currentSnapshot = actorRef.getSnapshot();
      const pendingEnd = await AsyncStorage.getItem(STORAGE_PENDING_BG_END).catch(() => null);
      if (!active) return;

      if (pendingEnd === 'true') {
        await AsyncStorage.removeItem(STORAGE_PENDING_BG_END).catch(() => null);
        if (!active) return;

        if (!currentSnapshot.matches('IDLE')) {
          send({ type: 'END' });
        }
        return;
      }
      const phase = await AsyncStorage.getItem(STORAGE_SESSION_PHASE).catch(() => null);
      if (!active) return;

      if (phase === 'active' && currentSnapshot.matches('IDLE')) {
        send({ type: 'START' });
      }
      if (phase === 'paused' && currentSnapshot.matches('IDLE')) {
        send({ type: 'START' });
        send({ type: 'PAUSE' });
      }
    };
    
    let sub: { remove: () => void } | null = null;
    if (initialDataLoaded) {
      recover();
      sub = AppState.addEventListener('change', (s) => {
        if (s === 'active') recover();
      });
    }
    return () => {
      active = false;
      if (sub) sub.remove();
    };
  }, [actorRef, send, initialDataLoaded]);

  // Watch listeners
  useEffect(() => {
    const unsubCmd = WatchBridge.addWatchCommandListener((command: WatchCommand) => {
      AppLogger.log('APP_LOG', { event: 'watch_command_received', command });
      if (command === 'START_SESSION') send({ type: 'START' });
      if (command === 'STOP_SESSION') send({ type: 'END' });
    });
    const unsubHealth = WatchBridge.addWatchHealthListener((update: WatchHealthUpdate) => {
      AppLogger.log('APP_LOG', {
        event: 'watch_health_received',
        heartRate: update.heartRate,
        calories: update.calories,
        status: update.status,
      });
      if (update.heartRate > 0) {
        healthRef.current = {
          ...healthRef.current,
          latestBpm: update.heartRate,
          activeCalories: update.calories > 0 ? update.calories : healthRef.current.activeCalories,
        };
      }
      const currentSnapshot = actorRef.getSnapshot();
      if (update.status === 'ACTIVE' && currentSnapshot.matches('IDLE')) {
        AppLogger.log('APP_LOG', { event: 'auto_recovering_session_from_watch_health' });
        send({ type: 'START', externalStartTimeMs: update.startTimeMs });
      }
    });
    return () => {
      unsubCmd();
      unsubHealth();
    };
  }, [actorRef, send]);

  // Foreground Service/Notification configuration on iOS categories mount
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
            {
              id: 'pause-session',
              title: '⏸ Pause Session',
              foreground: true,
            },
            {
              id: 'resume-session',
              title: '▶️ Resume Session',
              foreground: true,
            },
          ],
        },
      ]);
    }
  }, []);

  // Handle Foreground Event Buttons from Notifee
  useEffect(() => {
    return notifee.onForegroundEvent(({ type, detail }) => {
      if (type === EventType.ACTION_PRESS) {
        if (detail.pressAction?.id === 'end-session') {
          AppLogger.log('APP_LOG', { event: 'end_session_from_notification' });
          send({ type: 'END' });
        } else if (detail.pressAction?.id === 'pause-session') {
          AppLogger.log('APP_LOG', { event: 'pause_session_from_notification' });
          send({ type: 'PAUSE' });
        } else if (detail.pressAction?.id === 'resume-session') {
          AppLogger.log('APP_LOG', { event: 'resume_session_from_notification' });
          send({ type: 'RESUME' });
        }
      }
    });
  }, [send]);

  const mergeWatchHealth = useCallback((watchHR: number, watchCal: number) => {
    if (watchHR > 0) {
      healthRef.current = {
        ...healthRef.current,
        latestBpm: watchHR,
        activeCalories: watchCal > 0 ? watchCal : healthRef.current.activeCalories,
      };
    }
  }, []);

  const healthValue = useMemo(() => ({
    latestBpm: health.latestBpm,
    avgBpm: health.avgBpm,
    peakBpm: health.peakBpm,
    activeCalories: health.activeCalories,
    mergeWatchHealth,
  }), [health, mergeWatchHealth]);

  const startSession = useCallback(() => {
    send({ type: 'START' });
  }, [send]);

  const endSession = useCallback(() => {
    send({ type: 'END' });
  }, [send]);

  return (
    <SessionContext.Provider value={{ isSkateSessionActive, sessionPhase, startSession, endSession, telemetry, health: healthValue }}>
      {children}
    </SessionContext.Provider>
  );
}

export function useSession() {
  const context = useContext(SessionContext);
  if (!context) throw new Error('useSession must be used within a SessionProvider');
  return context;
}

