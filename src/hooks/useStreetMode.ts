/**
 * useStreetMode.ts — Street Mode (Accelerometer + GPS Reactive LED) Domain Hook
 *
 * Owns the full lifecycle of Street Mode:
 *   - GPS watchPositionAsync subscription (speed tracking, distance, telemetry)
 *   - Accelerometer listener (jerk detection → brake light trigger)
 *   - applyStreetPattern() — the car-light LED pattern engine
 *   - motionState FSM transitions
 *
 * GPS session accumulation refs are injected from useSessionTracking so that
 * a single GPS subscription also populates the session recording data.
 *
 * Design constraints:
 *   - applyStreetPattern uses injected writeToDevice — never stored in closure.
 *   - ZenggeProtocol commands here are car-light patterns only (0x01 FREEZE / 0x02 STROBE).
 *   - deviceContext is passed in (not re-derived) to keep this hook framework-agnostic.
 *
 * Depends on: ZenggeProtocol, AppLogger, Accelerometer, expo-location, crewService
 * Platform: React Native (Android only for sensors — web returns early)
 */
import * as Location from 'expo-location';
import { Accelerometer } from 'expo-sensors';
import { useCallback, useEffect, useRef, useState } from 'react';
import { Platform } from 'react-native';
import { checkPermission, openGlobalPermissionsModal } from '../services/PermissionService';
import { LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { AppLogger } from '../services/AppLogger';
import { crewService } from '../services/CrewService';
import { normalizeUISpeedToHardware } from '../utils/NormalizationUtils';
// Fix 4: Static import replaces the dynamic require() inside applyStreetPattern callback.
// Dynamic require() triggers a CommonJS module registry lookup on every invocation.
import { buildPatternPayload } from '../protocols/PatternEngine';

export type MotionState = 'STOPPED' | 'ACCELERATING' | 'CRUISING' | 'SLOWING_DOWN' | 'HARD_BRAKING';

export interface UseStreetModeOptions {
  /** Must be 'STREET' to activate — hook self-deactivates on any other mode */
  activeMode: string;
  /** BLE write delegate — injected to keep hook decoupled from BLE layer */
  writeToDevice: ((payload: number[]) => Promise<void | boolean | 'partial'>) | undefined;
  hwSettings: any;
  points: number | undefined;
  activeProduct: string;
  brightness: number;
  speed: number;
  /** deviceContext for analytics logging */
  deviceContext: Record<string, any>;
  // ── Session accumulator refs (injected from useSessionTracking) ──────────
  sessionStartTimeRef: React.MutableRefObject<number | null>;
  sessionSpeedSamplesRef: React.MutableRefObject<number[]>;
  sessionDistanceMilesRef: React.MutableRefObject<number>;
  sessionPeakSpeedRef: React.MutableRefObject<number>;
}

export interface UseStreetModeResult {
  streetSensitivity: number;
  setStreetSensitivity: (v: number) => void;
  streetCruiseColor: string;
  setStreetCruiseColor: (v: string) => void;
  streetBrakeColor: string;
  setStreetBrakeColor: (v: string) => void;
  streetDistribution: [number, number, number];
  setStreetDistribution: (v: [number, number, number]) => void;
  isStreetBraking: boolean;
  motionState: MotionState;
  /** Ref to current motion state — safe for use inside callbacks without stale closures */
  motionStateRef: React.MutableRefObject<MotionState>;
  gpsSpeed: number;
  peakGForce: number;
  /** Trigger the car-light pattern manually (e.g. after color change) */
  applyStreetPattern: (currMotionState: MotionState, brt?: number, spd?: number) => void;
}

export function useStreetMode({
  activeMode,
  writeToDevice,
  hwSettings,
  points,
  activeProduct,
  brightness,
  speed,
  deviceContext,
  sessionStartTimeRef,
  sessionSpeedSamplesRef,
  sessionDistanceMilesRef,
  sessionPeakSpeedRef,
}: UseStreetModeOptions): UseStreetModeResult {

  const [streetSensitivity, setStreetSensitivity] = useState<number>(30);
  const [streetCruiseColor, setStreetCruiseColor] = useState<string>('#FF8C00');
  const [streetBrakeColor, setStreetBrakeColor] = useState<string>('#FF0000');
  const [streetDistribution, setStreetDistribution] = useState<[number, number, number]>([0.3, 0.4, 0.3]);
  const [isStreetBraking, setIsStreetBraking] = useState<boolean>(false);
  const [motionState, setMotionState] = useState<MotionState>('STOPPED');
  const [gpsSpeed, setGpsSpeed] = useState<number>(0);
  const [peakGForce, setPeakGForce] = useState<number>(1.0);

  const streetBrakingRef = useRef(false);
  const lastAccelRef = useRef({ x: 0, y: 0, z: 0 });
  const motionStateRef = useRef<MotionState>('STOPPED');
  const lastGpsSpeeds = useRef<number[]>([]);
  const lastGpsTimeRef = useRef<number | null>(null);
  const locationSubRef = useRef<Location.LocationSubscription | null>(null);
  const cruiseChaseRef = useRef(0);
  // Fix 5: prevGRef stores the exponential decay accumulator so setPeakGForce is only
  // called when the value changes by >0.05, preventing 12.5 re-renders/sec at idle.
  const prevGRef = useRef(1.0);

  /** Maps UI speed slider (0–100) to Zengge hardware speed range (1–31) */
  const clampSpeed = useCallback((uiSpeed: number): number =>
    normalizeUISpeedToHardware(uiSpeed), []);

  /**
   * Perceptual brightness factor — lifts the floor so LEDs stay visible at low %.
   * Matches the brtFactor in DockedController exactly.
   */
  const brtFactor = (brt: number): number =>
    brt > 0 ? 0.10 + 0.90 * (brt / 100) : 0;

  /**
   * Car-light LED pattern engine.
   * SOULZ (linear): Rear 30% = red tail, Mid 40% = cruise color, Front 30% = white head.
   * HALOZ (mirrored ring): 8-LED frame mirrored to 16-LED array.
   * #9 — Cruise bounce chase: bright spot oscillates through mid zone.
   *
   * Source of Truth: SK8Lytz_App_Master_Reference.md § Street Mode Protocol
   */
  const applyStreetPattern = useCallback((
    currMotionState: MotionState,
    brt: number = brightness,
    spd: number = speed,
  ) => {
    if (!writeToDevice) return;

    const pts = hwSettings?.ledPoints || points || 16;
    const profile = LOCAL_PRODUCT_CATALOG.find(p => p.id === activeProduct) || LOCAL_PRODUCT_CATALOG[0];
    const segments = profile.vizIsMirrored && hwSettings?.segments ? hwSettings.segments : 1;

    let cruiseHex = streetCruiseColor;
    if (currMotionState === 'STOPPED') cruiseHex = '#FF0000';
    else if (currMotionState === 'SLOWING_DOWN') cruiseHex = '#FFFF00';
    else if (currMotionState === 'HARD_BRAKING') cruiseHex = '#FF0000';

    let pid = 102; // CRUISING
    if (currMotionState === 'HARD_BRAKING' || isStreetBraking) pid = 103;
    else if (currMotionState === 'STOPPED') pid = 101;
    else if (currMotionState === 'SLOWING_DOWN') pid = 104;
    else if (currMotionState === 'ACCELERATING') pid = 105;

    const fgRgb = { r: 255, g: 34, b: 0 }; // #FF2200 Tail
    const bgRgb = {
      r: parseInt(cruiseHex.slice(1, 3), 16) || 0,
      g: parseInt(cruiseHex.slice(3, 5), 16) || 0,
      b: parseInt(cruiseHex.slice(5, 7), 16) || 0,
    };

    // buildPatternPayload is now a static import at the top of this file (Fix 4).
    
    // We only pass pts/segments as activeSegmentLeds. buildPatternPayload will automatically duplicate/mirror 
    // the payload across the segments based on the options.segments value.
    const activeSegmentLeds = pts;
    const payload = buildPatternPayload(
      pid,
      fgRgb,
      bgRgb,
      activeSegmentLeds,
      spd,
      1,
      brt,
      { distribution: streetDistribution, segments: segments }
    );

    if (payload) {
      writeToDevice(payload);
    }
  }, [writeToDevice, hwSettings, points, activeProduct, streetCruiseColor, brightness, speed, streetDistribution, isStreetBraking]);

  /** Transition motion state and trigger pattern update */
  const updateMotion = useCallback((newState: MotionState) => {
    if (newState !== motionStateRef.current) {
      motionStateRef.current = newState;
      setMotionState(newState);
      applyStreetPattern(newState);
    }
  }, [applyStreetPattern]);

  // ── Main Street Mode subscription effect ──────────────────────────────────
  useEffect(() => {
    if (activeMode !== 'STREET') {
      if (Platform.OS !== 'web') Accelerometer.removeAllListeners();
      if (locationSubRef.current) {
        locationSubRef.current.remove();
        locationSubRef.current = null;
      }
      if (streetBrakingRef.current) {
        streetBrakingRef.current = false;
        setIsStreetBraking(false);
      }
      AppLogger.log('STREET_MODE_DEACTIVATED', { lastMotionState: motionStateRef.current, peakGForce, ...deviceContext });
      return;
    }

    // Initialize
    updateMotion('STOPPED');
    lastGpsTimeRef.current = null;
    AppLogger.log('STREET_MODE_ACTIVATED', { sensitivity: streetSensitivity, cruiseColor: streetCruiseColor, brakeColor: streetBrakeColor, ...deviceContext });

    if (Platform.OS === 'web') return;

    // Start Location tracking
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

              // ── Session accumulation (if recording) ──────────────────────
              if (sessionStartTimeRef.current !== null) {
                sessionSpeedSamplesRef.current.push(spdMph);
                if (spdMph > sessionPeakSpeedRef.current) {
                  sessionPeakSpeedRef.current = spdMph;
                }
              }

              // Telemetry calculus
              const now = pos.timestamp;
              if (lastGpsTimeRef.current) {
                const hoursDelta = (now - lastGpsTimeRef.current) / 3600000;
                if (hoursDelta > 0 && hoursDelta < 1) {
                  crewService.sessionTelemetry.distanceMiles += spdMph * hoursDelta;
                  if (sessionStartTimeRef.current !== null) {
                    sessionDistanceMilesRef.current += spdMph * hoursDelta;
                  }
                }
              }
              lastGpsTimeRef.current = now;

              if (spdMph > crewService.sessionTelemetry.topSpeedMph) {
                crewService.sessionTelemetry.topSpeedMph = spdMph;
              }
              crewService.sessionTelemetry.avgSpeedSamples.push(spdMph);

              // Add to rolling history
              lastGpsSpeeds.current.push(spdMph);
              if (lastGpsSpeeds.current.length > 3) lastGpsSpeeds.current.shift();

              // If not hard braking, evaluate soft states
              if (motionStateRef.current !== 'HARD_BRAKING') {
                if (spdMph < 1.0 && peakGForce < 1.1) {
                  updateMotion('STOPPED');
                } else if (lastGpsSpeeds.current.length >= 2) {
                  const oldSpd = lastGpsSpeeds.current[0];
                  if (oldSpd - spdMph > 1.0) {
                    updateMotion('SLOWING_DOWN');
                  } else if (spdMph >= 1.0) {
                    updateMotion('CRUISING');
                  }
                } else if (spdMph >= 1.0) {
                  updateMotion('CRUISING');
                }
              }
            }
          );
      } catch (e) {
        AppLogger.error('[useStreetMode] Location permission denied or unavailable', e);
      }
    })();

    // Accelerometer tracking
    Accelerometer.setUpdateInterval(80);
    const sub = Accelerometer.addListener(({ x, y, z }) => {
      const prev = lastAccelRef.current;
      const gMag = Math.sqrt(x * x + y * y + z * z);

      // Update peak G-Force display with exponential decay back to 1.0
      // Fix 5: Use ref-based accumulator + delta guard to avoid 12.5 re-renders/sec at rest.
      const newG = Math.sqrt(x * x + y * y + z * z);
      const decayed = newG > prevGRef.current
        ? parseFloat(newG.toFixed(2))
        : parseFloat((prevGRef.current * 0.95 + 1.0 * 0.05).toFixed(2));
      prevGRef.current = decayed;
      // Only trigger a React re-render if the display value changed materially (>0.05)
      if (Math.abs(decayed - peakGForce) > 0.05) {
        setPeakGForce(decayed);
      }

      const jerkMag = Math.sqrt(
        Math.pow(x - prev.x, 2) + Math.pow(y - prev.y, 2) + Math.pow(z - prev.z, 2)
      );
      lastAccelRef.current = { x, y, z };
      const threshold = ((100 - streetSensitivity) / 100) * 2.5 + 0.3;

      const isBrakingJerk = jerkMag > threshold;
      const isActivePush = jerkMag > 0.4 && !isBrakingJerk;

      if (isBrakingJerk) {
        if (!streetBrakingRef.current) {
          // Only log on the leading edge of a brake event, not every accelerometer tick
          AppLogger.log('STREET_JERK_DETECTED', { jerkMag: parseFloat(jerkMag.toFixed(3)), threshold: parseFloat(threshold.toFixed(3)), gpsSpeedMph: gpsSpeed, ...deviceContext });
        }
        streetBrakingRef.current = true;
        setIsStreetBraking(true);
        updateMotion('HARD_BRAKING');
      } else {
        if (streetBrakingRef.current) {
          streetBrakingRef.current = false;
          setIsStreetBraking(false);
          // Re-evaluate state gracefully
          if (lastGpsSpeeds.current[lastGpsSpeeds.current.length - 1] < 1.0) updateMotion('STOPPED');
          else updateMotion('CRUISING');
        } else if (isActivePush && motionStateRef.current !== 'SLOWING_DOWN') {
          updateMotion('ACCELERATING');
        }
      }
    });

    return () => {
      sub.remove();
      if (locationSubRef.current) {
        locationSubRef.current.remove();
        locationSubRef.current = null;
      }
    };
  // intentional deps: activeMode + sensor config triggers re-mount; writeToDevice stability relied on
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activeMode, streetSensitivity, brightness, speed]);

  return {
    streetSensitivity,
    setStreetSensitivity,
    streetCruiseColor,
    setStreetCruiseColor,
    streetBrakeColor,
    setStreetBrakeColor,
    isStreetBraking,
    motionState,
    motionStateRef,
    gpsSpeed,
    peakGForce,
    applyStreetPattern,
    streetDistribution,
    setStreetDistribution,
  };
}
