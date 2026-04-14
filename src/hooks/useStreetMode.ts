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
import { LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { AppLogger } from '../services/AppLogger';
import { crewService } from '../services/CrewService';
import { normalizeUISpeedToHardware } from '../utils/NormalizationUtils';

export type MotionState = 'STOPPED' | 'ACCELERATING' | 'CRUISING' | 'SLOWING_DOWN' | 'HARD_BRAKING';

export interface UseStreetModeOptions {
  /** Must be 'STREET' to activate — hook self-deactivates on any other mode */
  activeMode: string;
  /** BLE write delegate — injected to keep hook decoupled from BLE layer */
  writeToDevice: ((payload: number[]) => Promise<void | boolean>) | undefined;
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

    const factor = brtFactor(brt);
    const pts = hwSettings?.ledPoints || points || 16;

    // Resolve product profile for mirroring logic (replaces legacy isHalozRing heuristic)
    const profile = LOCAL_PRODUCT_CATALOG.find(p => p.id === activeProduct) || LOCAL_PRODUCT_CATALOG[0];
    const isMirrored = profile.vizIsMirrored;
    const hwSpeed = clampSpeed(spd);

    let cruiseHex = streetCruiseColor;
    if (currMotionState === 'STOPPED') cruiseHex = '#FF0000';
    else if (currMotionState === 'SLOWING_DOWN') cruiseHex = '#FFFF00';
    else if (currMotionState === 'HARD_BRAKING') cruiseHex = '#FF0000';

    const isBraking = currMotionState === 'HARD_BRAKING' || currMotionState === 'STOPPED';

    const cr = parseInt(cruiseHex.slice(1, 3), 16);
    const cg = parseInt(cruiseHex.slice(3, 5), 16);
    const cb = parseInt(cruiseHex.slice(5, 7), 16);

    // Tail lights: ABSOLUTE brightness — 100% (255) braking, 50% (127) cruising.
    // Street Mode has no brightness slider — these are fixed car safety values.
    const tailR = isBraking ? 255 : 127;
    const tail = { r: tailR, g: 0, b: 0 };

    // Headlights: warm white, always steady
    const headVal = Math.round(255 * factor);
    const head = { r: headVal, g: Math.round(headVal * 0.95), b: Math.round(headVal * 0.85) };

    // Dashboard Cruise Color
    const crR = Math.round(cr * factor);
    const crG = Math.round(cg * factor);
    const crB = Math.round(cb * factor);
    const crDim = { r: Math.round(crR * 0.3), g: Math.round(crG * 0.3), b: Math.round(crB * 0.3) };
    const cruise = { r: crR, g: crG, b: crB };

    // DO NOT apply applyColorSorting here.
    // Hardware auto-remaps GRB internally via 0x62 EEPROM config. Send pure RGB.
    let arr: { r: number; g: number; b: number }[];

    // #9 — Cruise bounce chase animation (triangle wave 0→1→0)
    const isCruising = currMotionState === 'CRUISING' || currMotionState === 'ACCELERATING';
    if (isCruising) {
      cruiseChaseRef.current = (cruiseChaseRef.current + 0.07) % 2;
    } else {
      cruiseChaseRef.current = 0;
    }
    const chaseTick = cruiseChaseRef.current <= 1
      ? cruiseChaseRef.current
      : 2 - cruiseChaseRef.current;

    if (isMirrored) {
      const segLeds = Math.ceil(pts / 2);
      const frameHalf = Array.from({ length: segLeds }, (_, i) => {
        const fract = i / segLeds;
        if (fract < 0.3) return tail;
        if (fract > 0.7) return head;
        const midZoneI = i - Math.round(segLeds * 0.3);
        const midZoneSize = Math.round(segLeds * 0.4);
        const dist = Math.abs(midZoneI - Math.round(chaseTick * midZoneSize));
        if (dist === 0) return cruise;
        return crDim;
      });
      arr = [...frameHalf, ...[...frameHalf].reverse()];
    } else {
      const ledCount = Math.max(10, hwSettings?.ledPoints || pts);
      const rearCount = Math.max(1, Math.round(ledCount * 0.3));
      const frontCount = Math.max(1, Math.round(ledCount * 0.3));
      const midCount = Math.max(1, ledCount - rearCount - frontCount);
      const chasePos = Math.round(chaseTick * (midCount - 1));
      const midSection = Array.from({ length: midCount }, (_, i) => {
        const dist = Math.abs(i - chasePos);
        if (dist === 0) return cruise;
        if (dist === 1) return { r: Math.round(crR * 0.6), g: Math.round(crG * 0.6), b: Math.round(crB * 0.6) };
        return crDim;
      });
      arr = [
        ...Array(rearCount).fill(tail),
        ...midSection,
        ...Array(frontCount).fill(head),
      ];
    }

    // 0x01 = FREEZE (hardware locks array in place — static car lights, no scrolling)
    // 0x02 = STROBE (urgent flashing for hard braking)
    // NOTE: 0x00 is CASCADE (scrolling) — NOT static. Never use 0x00 for car lights.
    const transType = currMotionState === 'HARD_BRAKING' ? 0x02 : 0x01;
    writeToDevice(ZenggeProtocol.setMultiColor(arr, hwSpeed, 1, transType));
  }, [writeToDevice, hwSettings, points, activeProduct, streetCruiseColor, brightness, speed, clampSpeed]);

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
        const { status } = await Location.requestForegroundPermissionsAsync();
        if (status === 'granted') {
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
        }
      } catch (e) {
        console.warn('[useStreetMode] Location permission denied or unavailable', e);
      }
    })();

    // Accelerometer tracking
    Accelerometer.setUpdateInterval(80);
    const sub = Accelerometer.addListener(({ x, y, z }) => {
      const prev = lastAccelRef.current;
      const gMag = Math.sqrt(x * x + y * y + z * z);

      // Update peak G-Force display with exponential decay back to 1.0
      setPeakGForce(prevG => {
        if (gMag > prevG) return parseFloat(gMag.toFixed(2));
        return parseFloat((prevG * 0.95 + 1.0 * 0.05).toFixed(2));
      });

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
  };
}
