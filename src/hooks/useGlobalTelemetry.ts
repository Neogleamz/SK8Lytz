import * as Location from 'expo-location';
import { Accelerometer } from 'expo-sensors';
import { useCallback, useEffect, useRef, useState } from 'react';
import { Platform } from 'react-native';
import { checkPermission, openGlobalPermissionsModal } from '../services/PermissionService';
import { AppLogger } from '../services/AppLogger';
import { crewService } from '../services/CrewService';
import { SpeedTrackingService, ISessionSnapshot } from '../services/SpeedTrackingService';

export interface GlobalTelemetryState {
  gpsSpeed: number;
  peakGForce: number;
  sessionDistanceMiles: number;
  sessionDurationSec: number;
}

export function useGlobalTelemetry(isActuallyConnected: boolean): GlobalTelemetryState {
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

  const lastGpsTimeRef = useRef<number | null>(null);
  const locationSubRef = useRef<Location.LocationSubscription | null>(null);
  const prevGRef = useRef(1.0);
  const timerRef = useRef<NodeJS.Timeout | null>(null);

  // Commit session helper
  const commitSession = useCallback(async () => {
    if (!sessionStartTimeRef.current) return;
    
    const durationSec = (Date.now() - sessionStartTimeRef.current) / 1000;
    const distanceMiles = sessionDistanceMilesRef.current;
    
    // Only save if meaningful distance traveled
    if (distanceMiles > 0.2) {
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
      };

      try {
        await SpeedTrackingService.saveSession(snapshot);
        AppLogger.log('GLOBAL_SESSION_SAVED', { action: 'AUTO_SAVED_TO_DB', durationSec, distanceMiles });
      } catch (err) {
        AppLogger.error('[useGlobalTelemetry] Failed to persist auto-session', err);
      }
    } else {
       AppLogger.log('GLOBAL_SESSION_DISCARDED', { reason: 'distance < 0.2 miles', distanceMiles });
    }

    // Reset accumulators
    sessionStartTimeRef.current = null;
    sessionDistanceMilesRef.current = 0;
    sessionPeakGForceRef.current = 1.0;
    sessionPeakSpeedRef.current = 0;
    sessionSpeedSamplesRef.current = [];
    setSessionDistanceMiles(0);
    setSessionDurationSec(0);
    setSessionPeakSpeed(0);
    setSessionAvgSpeed(0);
  }, []);

  useEffect(() => {
    if (Platform.OS === 'web') return;

    if (isActuallyConnected) {
      // Start Session
      if (!sessionStartTimeRef.current) {
        sessionStartTimeRef.current = Date.now();
        sessionDistanceMilesRef.current = 0;
        sessionPeakGForceRef.current = 1.0;
        sessionPeakSpeedRef.current = 0;
        sessionSpeedSamplesRef.current = [];
        setSessionDistanceMiles(0);
        setSessionDurationSec(0);
        setSessionPeakSpeed(0);
        setSessionAvgSpeed(0);
        AppLogger.log('GLOBAL_TELEMETRY_STARTED');
      }

      // Start duration ticker for UI
      timerRef.current = setInterval(() => {
        if (sessionStartTimeRef.current) {
          setSessionDurationSec((Date.now() - sessionStartTimeRef.current) / 1000);
        }
      }, 1000);

      // Start Location Tracking
      (async () => {
        try {
          const isGranted = await checkPermission('LOCATION');
          if (!isGranted) {
             await openGlobalPermissionsModal();
             const reG = await checkPermission('LOCATION');
             if (!reG) throw new Error('Location permission denied via modal');
          }
          
          locationSubRef.current = await Location.watchPositionAsync(
              { accuracy: Location.Accuracy.Balanced, timeInterval: 1000, distanceInterval: 1 },
              (pos) => {
                const spdMpS = pos.coords.speed || 0;
                const spdMph = Math.max(0, spdMpS * 2.23694);
                setGpsSpeed(spdMph);

                const now = pos.timestamp;
                if (lastGpsTimeRef.current) {
                  const hoursDelta = (now - lastGpsTimeRef.current) / 3600000;
                  if (hoursDelta > 0 && hoursDelta < 1) {
                    const distDelta = spdMph * hoursDelta;
                    sessionDistanceMilesRef.current += distDelta;
                    setSessionDistanceMiles(sessionDistanceMilesRef.current);
                    
                    // Inject to Crew Service if active
                    if (crewService.currentSessionId) {
                      crewService.sessionTelemetry.distanceMiles += distDelta;
                    }
                  }
                }
                lastGpsTimeRef.current = now;

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
            );
        } catch (e) {
          AppLogger.error('[useGlobalTelemetry] Location permission denied or unavailable', e);
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
        
        if (Math.abs(decayed - peakGForce) > 0.05) {
          setPeakGForce(decayed);
        }

        if (decayed > sessionPeakGForceRef.current) {
          sessionPeakGForceRef.current = decayed;
        }
      });

      return () => {
        sub.remove();
        if (locationSubRef.current) {
          locationSubRef.current.remove();
          locationSubRef.current = null;
        }
        if (timerRef.current) clearInterval(timerRef.current);
      };
    } else {
      // Disconnected: stop tracking and evaluate commit
      if (sessionStartTimeRef.current) {
        commitSession();
      }
    }
  }, [isActuallyConnected, peakGForce, commitSession]);

  return {
    gpsSpeed,
    peakGForce,
    sessionDistanceMiles,
    sessionDurationSec,
    sessionPeakSpeed,
    sessionAvgSpeed,
  };
}
