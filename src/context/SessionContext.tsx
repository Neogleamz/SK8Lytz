import React, { createContext, useContext, useState, useEffect, useCallback, ReactNode } from 'react';
import { AppState, Platform } from 'react-native';
import * as Location from 'expo-location';
import notifee, { AndroidImportance, EventType, AndroidForegroundServiceType } from '@notifee/react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useGlobalTelemetry, GlobalTelemetryState } from '../hooks/useGlobalTelemetry';
import { useHealthTelemetry, HealthTelemetry } from '../hooks/useHealthTelemetry';
import { AppLogger } from '../services/AppLogger';
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

const NOTIFICATION_CHANNEL_ID = 'sk8lytz-session';
const NOTIFICATION_ID = 'active-skate-session';

/** Persisted session phase for crash recovery. */
type PersistedSessionPhase = 'active' | 'paused' | 'idle';
const SESSION_PHASE_KEY = '@sk8lytz_session_phase';
const SESSION_PAUSE_TIME_KEY = '@sk8lytz_session_pause_time';

async function persistSessionPhase(phase: PersistedSessionPhase, pauseTimeMs?: number): Promise<void> {
  try {
    const pairs: [string, string][] = [
      [SESSION_PHASE_KEY, phase],
      ['@sk8lytz_session_active', phase === 'active' || phase === 'paused' ? 'true' : 'false'],
    ];
    if (phase === 'paused' && pauseTimeMs) {
      pairs.push([SESSION_PAUSE_TIME_KEY, String(pauseTimeMs)]);
    } else if (phase !== 'paused') {
      pairs.push([SESSION_PAUSE_TIME_KEY, '']);
    }
    await AsyncStorage.multiSet(pairs);
  } catch (err: unknown) {
      const safeErr = err instanceof Error ? err : new Error(String(err));
    AppLogger.error('Failed to persist session phase', err instanceof Error ? err.message : String(err));
  }
}

export function SessionProvider({ children }: { children: ReactNode }) {
  const [sessionPhase, setSessionPhase] = useState<'IDLE' | 'ACTIVE' | 'PAUSED' | 'ENDING'>('IDLE');
  const [recoveredStartTimeMs, setRecoveredStartTimeMs] = useState<number | null>(null);
  const sessionPhaseRef = React.useRef(sessionPhase);
  useEffect(() => { sessionPhaseRef.current = sessionPhase; }, [sessionPhase]);
  const isSkateSessionActive = sessionPhase === 'ACTIVE' || sessionPhase === 'PAUSED' || sessionPhase === 'ENDING';
  // Ref for the delayed STOPPED push that follows the 10-second SUMMARY card
  const summaryTimeoutRef = React.useRef<ReturnType<typeof setTimeout> | null>(null);

  // Refs to always-current session functions — avoids stale closures in stable listeners
  const startSessionRef = React.useRef<(externalStartTimeMs?: number) => void>(() => {});
  const endSessionRef = React.useRef<() => void>(() => {});

  // 1. Hook up the core telemetry
  const health = useHealthTelemetry(isSkateSessionActive);
  const telemetry = useGlobalTelemetry(sessionPhase, {
    avgBpm: health.avgBpm,
    peakBpm: health.peakBpm,
    activeCalories: health.activeCalories
  }, recoveredStartTimeMs);

  const telemetryRef = React.useRef(telemetry);
  useEffect(() => { telemetryRef.current = telemetry; }, [telemetry]);

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
      if (command === 'START_SESSION') startSessionRef.current();
      else if (command === 'STOP_SESSION') endSessionRef.current();
    });

    // Listen for health telemetry relayed from the watch's HealthKit/Health Services
    const unsubscribeHealth = WatchBridge.addWatchHealthListener((update: WatchHealthUpdate) => {
      AppLogger.log('APP_LOG', {
        event: 'watch_health_received',
        heartRate: update.heartRate,
        calories: update.calories,
        status: update.status
      });
      // Merge into phone-side health telemetry — watch data takes precedence when available
      if (update.heartRate > 0) {
        health.mergeWatchHealth?.(update.heartRate, update.calories);
      }
      
      // Auto-recover session if watch is active but phone is idle
      if (update.status === 'ACTIVE' && sessionPhaseRef.current === 'IDLE') {
        AppLogger.log('APP_LOG', { event: 'auto_recovering_session_from_watch_health' });
        startSessionRef.current(update.startTimeMs);
      }
    });

    return () => {
      unsubscribeCmd();
      unsubscribeHealth();
    };
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // 3. Synchronize React state with AsyncStorage on mount & App foreground transitions
  const isSyncingSessionState = React.useRef(false);
  useEffect(() => {
    const syncSessionState = async () => {
      if (isSyncingSessionState.current) return;
      isSyncingSessionState.current = true;
      try {
        // Check if a background end was queued while we were backgrounded
        const pendingBgEnd = await AsyncStorage.getItem(STORAGE_PENDING_BG_END);
        if (pendingBgEnd === 'true') {
          await AsyncStorage.removeItem(STORAGE_PENDING_BG_END);
          if (sessionPhaseRef.current !== 'IDLE') {
            AppLogger.log('APP_LOG', { event: 'deferred_bg_end_session' });
            // Fire the full endSession teardown (Supabase save, GPS cleanup, etc.)
            endSessionRef.current();
          }
          return; // Skip normal sync — endSession handles everything
        }

        const phase = await AsyncStorage.getItem(SESSION_PHASE_KEY);
        if (phase === 'active') {
          if (sessionPhaseRef.current === 'IDLE') {
            setSessionPhase('ACTIVE');
          }
        } else if (phase === 'paused') {
          if (sessionPhaseRef.current === 'IDLE') {
            setSessionPhase('PAUSED');
            // Recover pause timestamp for duration correction
            const pauseTimeStr = await AsyncStorage.getItem(SESSION_PAUSE_TIME_KEY);
            if (pauseTimeStr) {
              AppLogger.log('APP_LOG', { event: 'recovered_paused_session', pauseTimeMs: Number(pauseTimeStr) });
            }
          }
        } else {
          if (sessionPhaseRef.current !== 'IDLE') {
            setSessionPhase('IDLE');
          }
        }
      } catch (err: unknown) {
        AppLogger.error('Failed to sync session state from AsyncStorage', err instanceof Error ? err.message : String(err));
      } finally {
        isSyncingSessionState.current = false;
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
  }, []);

  // Auto-pause timer and setting check based on GPS speed
  const isCheckingAutoPause = React.useRef(false);
  useEffect(() => {
    if (sessionPhase !== 'ACTIVE' && sessionPhase !== 'PAUSED') return;

    let timer: ReturnType<typeof setTimeout> | null = null;

    const checkAutoPause = async () => {
      if (isCheckingAutoPause.current) return;
      isCheckingAutoPause.current = true;
      try {
        const enabled = await AsyncStorage.getItem(STORAGE_AUTO_PAUSE_ENABLED);
        if (enabled === 'false') {
          if (sessionPhase === 'PAUSED') {
            setSessionPhase('ACTIVE');
            // Watch sync handled by useGlobalTelemetry PAUSED→ACTIVE transition
            persistSessionPhase('active');
          }
          return;
        }

        if (telemetry.gpsSpeed < 0.2) {
          if (sessionPhase === 'ACTIVE') {
            timer = setTimeout(async () => {
              setSessionPhase('PAUSED');
              AppLogger.log('APP_LOG', { event: 'auto_pause_triggered' });
              await persistSessionPhase('paused', Date.now());
              try {
                await WatchBridge.syncSessionState({ status: 'PAUSED' });
              } catch (err: unknown) {
                AppLogger.warn('WATCH_BRIDGE', { event: 'sync_failed_on_pause', error: err instanceof Error ? err.message : String(err)  });
              }
            }, 10000);
          }
        } else {
          if (sessionPhase === 'PAUSED') {
            setSessionPhase('ACTIVE');
            AppLogger.log('APP_LOG', { event: 'auto_resume_triggered' });
            persistSessionPhase('active');
          }
        }
      } catch (err: unknown) {
        AppLogger.error('Failed to run auto-pause check', err instanceof Error ? err.message : String(err));
      } finally {
        isCheckingAutoPause.current = false;
      }
    };

    checkAutoPause();

    return () => {
      if (timer) clearTimeout(timer);
    };
  }, [sessionPhase, telemetry.gpsSpeed]);

  // 4. Manage the Foreground Service (Android) / Background Notification (iOS)
  useEffect(() => {
    let updateInterval: ReturnType<typeof setInterval> | null = null;

    const setupNotification = async () => {
      if (!isSkateSessionActive) {
        if (updateInterval) clearInterval(updateInterval);
        if (Platform.OS === 'android') {
          try { await notifee.stopForegroundService(); } catch { /* no FGS running — safe */ }
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

      // Android 14+ crashes with SecurityException if we start a FGS with
      // FOREGROUND_SERVICE_TYPE_LOCATION before the user has granted location.
      // Check first — if not granted, show a plain notification (no FGS).
      let hasLocationPermission = false;
      if (Platform.OS === 'android') {
        try {
          const { status } = await Location.getForegroundPermissionsAsync();
          hasLocationPermission = status === 'granted';
        } catch {
          hasLocationPermission = false;
        }
      }

      let isForegroundServiceStarted = false;
      let isDisplaying = false;

      const displayNotification = async () => {
        if (isDisplaying) return;
        isDisplaying = true;
        try {
          await notifee.displayNotification({
            id: NOTIFICATION_ID,
            title: sessionPhaseRef.current === 'PAUSED' ? 'Skate Session Paused ⏸' : sessionPhaseRef.current === 'ENDING' ? 'Saving Session...' : 'Skate Session Active 🟢',
            body: `Distance: ${telemetryRef.current.sessionDistanceMiles.toFixed(2)} mi | Speed: ${telemetryRef.current.gpsSpeed.toFixed(1)} mph`,
            android: {
              channelId: NOTIFICATION_CHANNEL_ID,
              // Only trigger the native startForegroundService ONCE.
              // Subsequent updates just update the notification visually.
              // CRITICAL (Android 14+): FGS can ONLY be started when the app is visibly in the foreground.
              asForegroundService: !isForegroundServiceStarted && hasLocationPermission && AppState.currentState === 'active',
              ...(hasLocationPermission && {
                foregroundServiceTypes: [AndroidForegroundServiceType.FOREGROUND_SERVICE_TYPE_LOCATION],
              }),
              color: '#00F0FF',
              ongoing: true,
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
          
          
          if (hasLocationPermission && AppState.currentState === 'active') {
            isForegroundServiceStarted = true;
          }
        } catch (err: unknown) {
      const safeErr = err instanceof Error ? err : new Error(String(err));
          AppLogger.error('Failed to display foreground service notification', err instanceof Error ? err.message : String(err));
        } finally {
          isDisplaying = false;
        }
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
  }, [isSkateSessionActive, sessionPhase]);

  const startSession = useCallback(async (externalStartTimeMs?: number) => {
    // Cancel any pending STOPPED push from a prior session summary
    if (summaryTimeoutRef.current) {
      clearTimeout(summaryTimeoutRef.current);
      summaryTimeoutRef.current = null;
    }
    if (externalStartTimeMs) {
      setRecoveredStartTimeMs(externalStartTimeMs);
    }
    setSessionPhase('ACTIVE');
    await persistSessionPhase('active');
    AppLogger.log('APP_LOG', { event: 'session_started', externalStartTimeMs });
    
    const isoStart = externalStartTimeMs 
      ? new Date(externalStartTimeMs).toISOString() 
      : new Date().toISOString();

    // Notify both watches — fire-and-forget, safe if no watch paired
    WatchBridge.syncSessionState({
      status: 'ACTIVE',
      startTime: isoStart,
    }).catch((err: unknown) =>
      AppLogger.warn('WATCH_BRIDGE', { event: 'sync_failed_on_start', error: String(err) })
    );
  }, []);
  startSessionRef.current = startSession;

  const endSession = useCallback(async () => {
    // ── 1. Capture final metrics before state resets ──────────────────────────
    const finalDurationSec = telemetry.sessionDurationSec ?? 0;
    const finalDistanceMiles = telemetry.sessionDistanceMiles ?? 0;
    const finalAvgSpeed =
      finalDistanceMiles > 0 && finalDurationSec > 0
        ? finalDistanceMiles / (finalDurationSec / 3600)
        : 0;
    const finalCalories = health.activeCalories ?? 0;
    const finalPeakHR = health.peakBpm ?? 0;

    // ── 2. Transition to ENDING — phone HUD clears, but FGS stays alive ──────
    setSessionPhase('ENDING');
    setRecoveredStartTimeMs(null);
    await persistSessionPhase('idle');
    AppLogger.log('APP_LOG', { event: 'session_ending' });

    // ── 3. Push SUMMARY → wait for delivery before tearing down ──────────────
    try {
      await WatchBridge.syncSessionState({
        status: 'SUMMARY',
        totalDuration: finalDurationSec,
        distance: finalDistanceMiles,
        avgSpeed: finalAvgSpeed,
        calories: finalCalories,
        peakHR: finalPeakHR,
      });
    } catch (err: unknown) {
      const safeErr = err instanceof Error ? err : new Error(String(err));
      AppLogger.warn('WATCH_BRIDGE', { event: 'summary_push_failed', error: String(err) });
    }

    // ── 4. NOW transition to IDLE — safe to tear down FGS ────────────────────
    setSessionPhase('IDLE');
    AppLogger.log('APP_LOG', { event: 'session_ended' });
    if (Platform.OS === 'android') {
      notifee.stopForegroundService();
    } else {
      notifee.cancelNotification(NOTIFICATION_ID);
    }

    // ── 5. Push STOPPED after 10s (matches watch card auto-dismiss timer) ────
    summaryTimeoutRef.current = setTimeout(() => {
      WatchBridge.syncSessionState({ status: 'STOPPED' }).catch(() => {});
      summaryTimeoutRef.current = null;
    }, 10000);
  }, [telemetry, health]);
  endSessionRef.current = endSession;

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
    <SessionContext.Provider value={{ isSkateSessionActive, sessionPhase, startSession, endSession, telemetry, health }}>
      {children}
    </SessionContext.Provider>
  );
}

export function useSession() {
  const context = useContext(SessionContext);
  if (!context) throw new Error('useSession must be used within a SessionProvider');
  return context;
}
