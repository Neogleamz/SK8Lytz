import * as Location from 'expo-location';
import { Accelerometer } from 'expo-sensors';
import { useCallback, useEffect, useRef, useState } from 'react';
import { Platform } from 'react-native';
import { checkPermission, openGlobalPermissionsModal } from '../services/PermissionService';
import { AppLogger } from '../services/AppLogger';
import { useAuth } from '../context/AuthContext';
import { crewService } from '../services/CrewService';
import { SpeedTrackingService, ISessionSnapshot } from '../services/SpeedTrackingService';
import { WatchBridge } from 'sk8lytz-watch-bridge';

export interface GlobalTelemetryState {
  gpsSpeed: number;
  peakGForce: number;
  sessionDistanceMiles: number;
  sessionDurationSec: number;
  sessionPeakSpeed: number;
  sessionAvgSpeed: number;
}

/**
 * useGlobalTelemetry — GPS + Accelerometer session tracker.
 *
 * ARCHITECTURE NOTE (v3.3.2):
 * `isSkateSessionActive` is a LOGICAL session flag, NOT the raw BLE `isActuallyConnected` boolean.
 * The caller (DashboardScreen) sets this to `true` when the user initiates a skate session
 * and `false` ONLY when the user explicitly disconnects (handleDisconnect / forceDisconnect).
 *
 * This decouples the GPS/timer session from the fragile BLE radio link so that brief
 * signal drops or OS-level connection management do NOT zero out the in-progress session.
 */
export function useGlobalTelemetry(
  sessionPhase: 'IDLE' | 'ACTIVE' | 'PAUSED' | 'ENDING',
  healthMetrics?: { avgBpm: number | null; peakBpm: number | null; activeCalories: number | null },
  externalStartTimeMs?: number | null
): GlobalTelemetryState {
  const { user } = useAuth();
  const isSkateSessionActive = sessionPhase === 'ACTIVE' || sessionPhase === 'PAUSED' || sessionPhase === 'ENDING';
  const [gpsSpeed, setGpsSpeed] = useState<number>(0);
  const [peakGForce, setPeakGForce] = useState<number>(1.0);
  const [sessionDistanceMiles, setSessionDistanceMiles] = useState<number>(0);
  const [sessionDurationSec, setSessionDurationSec] = useState<number>(0);
  const [sessionPeakSpeed, setSessionPeakSpeed] = useState<number>(0);
  const [sessionAvgSpeed, setSessionAvgSpeed] = useState<number>(0);

  const sessionStartTimeRef = useRef<number | null>(null);
  const sessionDistanceMilesRef = useRef<number>(0);
  const sessionPeakGForceRef = useRef<number>(1.0);
  const sessionPeakSpeedRef = useRef<number>(0);
  const sessionSpeedSamplesRef = useRef<number[]>([]);
  const healthMetricsRef = useRef(healthMetrics);
  
  const sessionStartCoordsRef = useRef<{ lat: number; lng: number } | null>(null);
  const sessionEndCoordsRef = useRef<{ lat: number; lng: number } | null>(null);
  const sessionPathCoordsRef = useRef<Array<{ lat: number; lng: number }>>([]);

  useEffect(() => {
    healthMetricsRef.current = healthMetrics;
  }, [healthMetrics]);

  const userIdRef = useRef<string | null>(null);
  useEffect(() => {
    userIdRef.current = user?.id ?? null;
  }, [user]);

  const lastGpsTimeRef = useRef<number | null>(null);
  const locationSubRef = useRef<Location.LocationSubscription | null>(null);
  const prevGRef = useRef(1.0);
  const timerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  // Stable ref so commitSession never closes over stale peakGForce state
  const peakGForceRef = useRef(1.0);

  const sessionPauseTimeRef = useRef<number | null>(null);
  const prevSessionPhaseRef = useRef<'IDLE' | 'ACTIVE' | 'PAUSED' | 'ENDING'>('IDLE');

  useEffect(() => {
    const prevPhase = prevSessionPhaseRef.current;
    if (prevPhase === 'ACTIVE' && sessionPhase === 'PAUSED') {
      sessionPauseTimeRef.current = Date.now();
      AppLogger.log('GLOBAL_TELEMETRY', { event: 'session_paused' });
    } else if (prevPhase === 'PAUSED' && sessionPhase === 'ACTIVE') {
      if (sessionPauseTimeRef.current && sessionStartTimeRef.current) {
        const pauseDuration = Date.now() - sessionPauseTimeRef.current;
        sessionStartTimeRef.current += pauseDuration;
        AppLogger.log('GLOBAL_TELEMETRY', { event: 'anchor_shifted', pauseDurationMs: pauseDuration });

        const newStartTimeIso = new Date(sessionStartTimeRef.current).toISOString();
        WatchBridge.syncSessionState({
          status: 'ACTIVE',
          startTime: newStartTimeIso,
        }).catch((err: unknown) =>
          AppLogger.warn('WATCH_BRIDGE', { event: 'sync_failed_on_resume', error: (err instanceof Error ? err.message : String(err)) })
        );
      }
      sessionPauseTimeRef.current = null;
    } else if (sessionPhase === 'IDLE') {
      sessionPauseTimeRef.current = null;
    }
    prevSessionPhaseRef.current = sessionPhase;
  }, [sessionPhase]);

  // Commit session helper — called ONLY on explicit user disconnect
  const isCommittingRef = useRef(false);
  const commitSession = useCallback(async () => {
    if (!sessionStartTimeRef.current || isCommittingRef.current) return;
    isCommittingRef.current = true;

    let durationSec = (Date.now() - sessionStartTimeRef.current) / 1000;
    if (sessionPauseTimeRef.current) {
      const currentPauseDuration = Date.now() - sessionPauseTimeRef.current;
      durationSec -= (currentPauseDuration / 1000);
    }
    const distanceMiles = sessionDistanceMilesRef.current;

    // Only save if meaningful distance traveled or duration elapsed
    if (distanceMiles > 0.1 || durationSec > 60) {
      const samples = sessionSpeedSamplesRef.current;
      const avgSpeedMph = samples.length > 0
        ? samples.reduce((acc, s) => acc + s, 0) / samples.length
        : 0;

      const snapshot: ISessionSnapshot = {
        durationSec,
        distanceMiles,
        peakSpeedMph: sessionPeakSpeedRef.current,
        avgSpeedMph: parseFloat(avgSpeedMph.toFixed(2)),
        peakGForce: sessionPeakGForceRef.current,
        healthBpm: healthMetricsRef.current?.avgBpm ?? undefined,
        healthPeakBpm: healthMetricsRef.current?.peakBpm ?? undefined,
        healthCalories: healthMetricsRef.current?.activeCalories ?? undefined,
        startCoords: sessionStartCoordsRef.current ?? undefined,
        endCoords: sessionEndCoordsRef.current ?? undefined,
        locationCoords: sessionStartCoordsRef.current ?? undefined,
        pathCoords: sessionPathCoordsRef.current.length > 0 ? sessionPathCoordsRef.current : undefined,
      };

      try {
        await SpeedTrackingService.saveSession(snapshot, userIdRef.current);
        AppLogger.log('GLOBAL_SESSION_SAVED', { action: 'AUTO_SAVED_TO_DB', durationSec, distanceMiles });
      } catch (err: unknown) {
        AppLogger.error('[useGlobalTelemetry] Failed to persist auto-session', err instanceof Error ? err.message : String(err));
      }
    } else {
       AppLogger.log('GLOBAL_SESSION_DISCARDED', { reason: 'insufficient distance/duration', distanceMiles, durationSec });
    }

    // Reset accumulators
    sessionStartTimeRef.current = null;
    sessionDistanceMilesRef.current = 0;
    sessionPeakGForceRef.current = 1.0;
    sessionPeakSpeedRef.current = 0;
    sessionSpeedSamplesRef.current = [];
    sessionStartCoordsRef.current = null;
    sessionEndCoordsRef.current = null;
    sessionPathCoordsRef.current = [];
    setSessionDistanceMiles(0);
    setSessionDurationSec(0);
    setSessionPeakSpeed(0);
    setSessionAvgSpeed(0);
    
    isCommittingRef.current = false;
  }, []);

  // ── Effect 1: Isolated 1-second UI timer ──────────────────────────────────
  // Separated from the GPS/Accelerometer effect so it is NOT torn down and
  // recreated every 80ms when the accelerometer updates peakGForce state.
  useEffect(() => {
    if (Platform.OS === 'web') return;
    if (!isSkateSessionActive) {
      if (timerRef.current) clearInterval(timerRef.current);
      timerRef.current = null;
      return;
    }

    timerRef.current = setInterval(() => {
      if (sessionStartTimeRef.current && sessionPhase === 'ACTIVE') {
        setSessionDurationSec(Math.floor((Date.now() - sessionStartTimeRef.current) / 1000));
      }
    }, 1000);

    return () => {
      if (timerRef.current) clearInterval(timerRef.current);
      timerRef.current = null;
    };
  }, [isSkateSessionActive, sessionPhase]);

  // ── Effect 2: GPS + Accelerometer sensors ────────────────────────────────
  // Dependency array NO LONGER includes peakGForce — removing the accelerometer
  // as a dependency prevents this effect from being destroyed/recreated every 80ms.
  useEffect(() => {
    if (Platform.OS === 'web') return;

    if (isSkateSessionActive) {
      // Start Session accumulators (idempotent — guards against double-fire)
      if (!sessionStartTimeRef.current) {
        sessionStartTimeRef.current = externalStartTimeMs || Date.now();
        sessionDistanceMilesRef.current = 0;
        sessionPeakGForceRef.current = 1.0;
        sessionPeakSpeedRef.current = 0;
        sessionSpeedSamplesRef.current = [];
        sessionStartCoordsRef.current = null;
        sessionEndCoordsRef.current = null;
        sessionPathCoordsRef.current = [];
        setSessionDistanceMiles(0);
        setSessionDurationSec(0);
        setSessionPeakSpeed(0);
        setSessionAvgSpeed(0);
        AppLogger.log('GLOBAL_TELEMETRY_STARTED');
      }

      let isActive = true;
      // Start Location Tracking
      (async () => {
        try {
          const isGranted = await checkPermission('LOCATION');
          if (!isGranted) {
             await openGlobalPermissionsModal();
             const reG = await checkPermission('LOCATION');
             if (!reG) throw new Error('Location permission denied via modal');
          }

          const watchSub = await Location.watchPositionAsync(
              { accuracy: Location.Accuracy.Balanced, timeInterval: 1000, distanceInterval: 1 },
              (pos) => {
                const spdMpS = pos.coords.speed || 0;
                const spdMph = Math.max(0, spdMpS * 2.23694);
                setGpsSpeed(spdMph);

                const coords = { lat: pos.coords.latitude, lng: pos.coords.longitude };
                if (sessionPhase === 'ACTIVE') {
                  if (!sessionStartCoordsRef.current) {
                    sessionStartCoordsRef.current = coords;
                  }
                  sessionEndCoordsRef.current = coords;
                  // Throttle path storage to avoid giant JSON arrays (e.g. only 1 point per 3 seconds if interval is 1s)
                  // but Location.watchPositionAsync distanceInterval: 1 means it only triggers on movement > 1m.
                  sessionPathCoordsRef.current.push(coords);
                }

                const now = pos.timestamp;
                if (lastGpsTimeRef.current) {
                  const hoursDelta = (now - lastGpsTimeRef.current) / 3600000;
                  if (hoursDelta > 0 && hoursDelta < 1) {
                    const distDelta = spdMph * hoursDelta;
                    if (sessionPhase === 'ACTIVE') {
                      sessionDistanceMilesRef.current += distDelta;
                      setSessionDistanceMiles(sessionDistanceMilesRef.current);

                      // Inject to Crew Service if active
                      if (crewService.currentSessionId) {
                        crewService.sessionTelemetry.distanceMiles += distDelta;
                      }
                    }
                  }
                }
                lastGpsTimeRef.current = now;

                if (sessionPhase === 'ACTIVE') {
                  sessionSpeedSamplesRef.current.push(spdMph);
                  if (spdMph > sessionPeakSpeedRef.current) {
                    sessionPeakSpeedRef.current = spdMph;
                    setSessionPeakSpeed(spdMph);
                  }

                  // Update average speed
                  const samples = sessionSpeedSamplesRef.current;
                  const avgSpeedMph = samples.length > 0
                    ? samples.reduce((acc, s) => acc + s, 0) / samples.length
                    : 0;
                  setSessionAvgSpeed(avgSpeedMph);

                  if (crewService.currentSessionId) {
                     if (spdMph > crewService.sessionTelemetry.topSpeedMph) {
                       crewService.sessionTelemetry.topSpeedMph = spdMph;
                     }
                     crewService.sessionTelemetry.avgSpeedSamples.push(spdMph);
                  }
                }

                // Push live speed to connected watches (throttled internally to max 1/3s)
                SpeedTrackingService.pushSpeedToWatch(
                  spdMph,
                  healthMetricsRef.current?.activeCalories ?? undefined,
                  healthMetricsRef.current?.avgBpm ?? undefined,
                );
              }
            );
          if (!isActive) {
            watchSub.remove();
          } else {
            locationSubRef.current = watchSub;
          }
        } catch (e: unknown) {
          AppLogger.error('[useGlobalTelemetry] Location permission denied or unavailable', e instanceof Error ? e.message : String(e));
        }
      })();

      // Start Accelerometer Tracking
      Accelerometer.setUpdateInterval(80);
      const sub = Accelerometer.addListener(({ x, y, z }) => {
        const newG = Math.sqrt(x * x + y * y + z * z);
        const decayed = newG > prevGRef.current
          ? parseFloat(newG.toFixed(2))
          : parseFloat((prevGRef.current * 0.95 + 1.0 * 0.05).toFixed(2));
        prevGRef.current = decayed;

        // Write to ref first (no setState) to avoid triggering effect re-runs
        peakGForceRef.current = decayed;

        // Only push to React state when the change is perceptible to avoid excessive re-renders
        setPeakGForce(prev => Math.abs(decayed - prev) > 0.05 ? decayed : prev);

        if (decayed > sessionPeakGForceRef.current) {
          sessionPeakGForceRef.current = decayed;
        }
      });

      return () => {
        isActive = false;
        sub.remove();
        if (locationSubRef.current) {
          locationSubRef.current.remove();
          locationSubRef.current = null;
        }
      };
    } else {
      // isSkateSessionActive just flipped false — user explicitly disconnected.
      // Commit and zero out the session.
      if (sessionStartTimeRef.current) {
        commitSession();
      }
      setGpsSpeed(0);
    }
  // NOTE: peakGForce intentionally NOT in this dependency array.
  // The accelerometer updates its own state internally. Including peakGForce
  // here would destroy and recreate this effect 12x/second while skating.
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isSkateSessionActive, sessionPhase, commitSession]);

  return {
    gpsSpeed,
    peakGForce,
    sessionDistanceMiles,
    sessionDurationSec,
    sessionPeakSpeed,
    sessionAvgSpeed,
  };
}
