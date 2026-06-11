import * as Location from 'expo-location';
import { Accelerometer } from 'expo-sensors';
import { Platform } from 'react-native';
import { fromCallback } from 'xstate';
import { checkPermission, openGlobalPermissionsModal } from '../PermissionService';
import { AppLogger } from '../AppLogger';
import { crewService } from '../CrewService';
import { SpeedTrackingService } from '../SpeedTrackingService';
import { TelemetrySnapshot, HealthSnapshot } from './SessionMachine.types';

export interface SensorServiceInput {
  onTelemetryUpdate: (t: TelemetrySnapshot) => void;
  gpsSpeedRef: { current: number };
  startTimeMs: number | null;
  healthRef?: { current: HealthSnapshot };
}

export const sensorService = fromCallback<any, SensorServiceInput>(({ input }) => {
  if (Platform.OS === 'web') return () => {};

  let isActive = true;
  let locationSubscription: Location.LocationSubscription | null = null;
  let accelerometerSubscription: { remove: () => void } | null = null;
  let timer: NodeJS.Timeout | null = null;

  const sessionDistanceMilesRef = { current: 0 };
  const sessionPeakGForceRef = { current: 1.0 };
  const sessionPeakSpeedRef = { current: 0 };
  const sessionSpeedSamplesRef = { current: [] as number[] };
  const sessionStartCoordsRef = { current: null as { lat: number; lng: number } | null };
  const sessionEndCoordsRef = { current: null as { lat: number; lng: number } | null };
  const sessionPathCoordsRef = { current: [] as Array<{ lat: number; lng: number }> };
  const lastGpsTimeRef = { current: null as number | null };
  const prevGRef = { current: 1.0 };

  const sendTelemetryUpdate = () => {
    if (!isActive) return;
    const samples = sessionSpeedSamplesRef.current;
    const avgSpeedMph = samples.length > 0
      ? samples.reduce((acc, s) => acc + s, 0) / samples.length
      : 0;

    input.onTelemetryUpdate({
      gpsSpeed: input.gpsSpeedRef.current,
      peakGForce: sessionPeakGForceRef.current,
      sessionDistanceMiles: sessionDistanceMilesRef.current,
      sessionDurationSec: input.startTimeMs ? Math.floor((Date.now() - input.startTimeMs) / 1000) : 0,
      sessionPeakSpeed: sessionPeakSpeedRef.current,
      sessionAvgSpeed: avgSpeedMph,
      startCoords: sessionStartCoordsRef.current ?? undefined,
      endCoords: sessionEndCoordsRef.current ?? undefined,
      pathCoords: sessionPathCoordsRef.current.length > 0 ? sessionPathCoordsRef.current : undefined,
    });
  };

  // 1. Start UI duration timer (1s interval)
  timer = setInterval(sendTelemetryUpdate, 1000);

  // 2. Start GPS tracking
  (async () => {
    try {
      const isGranted = await checkPermission('LOCATION');
      if (!isActive) return;
      if (!isGranted) {
        await openGlobalPermissionsModal();
        if (!isActive) return;
        const reG = await checkPermission('LOCATION');
        if (!isActive) return;
        if (!reG) throw new Error('Location permission denied via modal');
      }
      if (!isActive) return;

      locationSubscription = await Location.watchPositionAsync(
        { accuracy: Location.Accuracy.Balanced, timeInterval: 1000, distanceInterval: 1 },
        (pos) => {
          if (!isActive) return;
          const spdMpS = pos.coords.speed || 0;
          const spdMph = Math.max(0, spdMpS * 2.23694);
          input.gpsSpeedRef.current = spdMph;

          const coords = { lat: pos.coords.latitude, lng: pos.coords.longitude };
          if (!sessionStartCoordsRef.current) {
            sessionStartCoordsRef.current = coords;
          }
          sessionEndCoordsRef.current = coords;
          sessionPathCoordsRef.current.push(coords);

          const now = pos.timestamp;
          if (lastGpsTimeRef.current) {
            const hoursDelta = (now - lastGpsTimeRef.current) / 3600000;
            if (hoursDelta > 0 && hoursDelta < 1) {
              const distDelta = spdMph * hoursDelta;
              sessionDistanceMilesRef.current += distDelta;

              // Inject into Crew Service if active
              if (crewService.currentSessionId) {
                crewService.sessionTelemetry.distanceMiles += distDelta;
              }
            }
          }
          lastGpsTimeRef.current = now;

          sessionSpeedSamplesRef.current.push(spdMph);
          if (spdMph > sessionPeakSpeedRef.current) {
            sessionPeakSpeedRef.current = spdMph;
          }

          if (crewService.currentSessionId) {
            if (spdMph > crewService.sessionTelemetry.topSpeedMph) {
              crewService.sessionTelemetry.topSpeedMph = spdMph;
            }
            crewService.sessionTelemetry.avgSpeedSamples.push(spdMph);
          }

          sendTelemetryUpdate();

          // Push live speed to connected watches
          SpeedTrackingService.pushSpeedToWatch(
            spdMph,
            input.healthRef?.current?.activeCalories ?? undefined,
            input.healthRef?.current?.avgBpm ?? undefined
          );
        }
      );
    } catch (e: unknown) {
      if (isActive) {
        AppLogger.error(
          '[SensorService] Location permission denied or unavailable',
          e instanceof Error ? e.message : String(e),
          { payload_size: 0, ssi: 0 }
        );
      }
    }
  })();

  // 3. Start Accelerometer tracking
  Accelerometer.setUpdateInterval(80);
  accelerometerSubscription = Accelerometer.addListener(({ x, y, z }) => {
    if (!isActive) return;
    const newG = Math.sqrt(x * x + y * y + z * z);
    const decayed = newG > prevGRef.current
      ? parseFloat(newG.toFixed(2))
      : parseFloat((prevGRef.current * 0.95 + 1.0 * 0.05).toFixed(2));
    prevGRef.current = decayed;

    if (decayed > sessionPeakGForceRef.current) {
      sessionPeakGForceRef.current = decayed;
    }

    sendTelemetryUpdate();
  });

  // Cleanup function
  return () => {
    isActive = false;
    if (timer) clearInterval(timer);
    if (locationSubscription) {
      locationSubscription.remove();
    }
    if (accelerometerSubscription) {
      accelerometerSubscription.remove();
    }
  };
});
