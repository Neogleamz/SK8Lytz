/**
 * useStreetMode.ts — Street Mode (Accelerometer + GPS Reactive LED) Domain Hook
 *
 * Owns the full lifecycle of Street Mode:
 *   - Accelerometer listener (jerk detection → brake light trigger)
 *   - applyStreetPattern() — the car-light LED pattern engine
 *   - motionState FSM transitions reacting to injected gpsSpeed
 *
 * Design constraints:
 *   - applyStreetPattern uses injected writeToDevice — never stored in closure.
 *   - ZenggeProtocol commands here are car-light patterns only (0x01 FREEZE / 0x02 STROBE).
 *   - deviceContext is passed in (not re-derived) to keep this hook framework-agnostic.
 *
 * Depends on: ZenggeProtocol, AppLogger, Accelerometer
 * Platform: React Native (Android only for sensors — web returns early)
 */
import { Accelerometer } from 'expo-sensors';
import { scrubPII } from '../utils/piiScrubber';
import { useCallback, useEffect, useRef, useState } from 'react';
import { Platform } from 'react-native';
import { LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import { AppLogger } from '../services/AppLogger';
import { normalizeUISpeedToHardware } from '../utils/NormalizationUtils';
import { buildPatternPayload } from '../protocols/PatternEngine';
import type { IHardwareSettings } from '../types/dashboard.types';

export type MotionState = 'STOPPED' | 'ACCELERATING' | 'CRUISING' | 'SLOWING_DOWN' | 'HARD_BRAKING';

export interface UseStreetModeOptions {
  /** Must be 'STREET' to activate — hook self-deactivates on any other mode */
  activeMode: string;
  /** BLE write delegate — injected to keep hook decoupled from BLE layer */
  writeToDevice: ((payload: number[]) => Promise<void | boolean | 'partial'>) | undefined;
  hwSettings: IHardwareSettings | null;
  points: number | undefined;
  activeProduct: string;
  brightness: number;
  speed: number;
  /** deviceContext for analytics logging */
  deviceContext: Record<string, unknown>;
  /** Live GPS speed from global telemetry */
  gpsSpeed: number;
  /** Live peak G-Force from global telemetry */
  peakGForce: number;
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
  motionStateRef: React.MutableRefObject<MotionState>;
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
  gpsSpeed,
  peakGForce,
}: UseStreetModeOptions): UseStreetModeResult {

  const [streetSensitivity, setStreetSensitivity] = useState<number>(30);
  const [streetCruiseColor, setStreetCruiseColor] = useState<string>('#FF8C00');
  const [streetBrakeColor, setStreetBrakeColor] = useState<string>('#FF0000');
  const [streetDistribution, setStreetDistribution] = useState<[number, number, number]>([0.3, 0.4, 0.3]);
  const [motionState, setMotionState] = useState<MotionState>('STOPPED');
  const isStreetBraking = motionState === 'HARD_BRAKING';

  const lastAccelRef = useRef({ x: 0, y: 0, z: 0 });
  const motionStateRef = useRef<MotionState>('STOPPED');
  const lastGpsSpeeds = useRef<number[]>([]);

  const writeToDeviceRef = useRef(writeToDevice);
  writeToDeviceRef.current = writeToDevice;

  const gpsSpeedRef = useRef(gpsSpeed);
  useEffect(() => { gpsSpeedRef.current = gpsSpeed; }, [gpsSpeed]);

  const deviceContextRef = useRef(deviceContext);
  useEffect(() => { deviceContextRef.current = deviceContext; }, [deviceContext]);

  const clampSpeed = useCallback((uiSpeed: number): number =>
    normalizeUISpeedToHardware(uiSpeed), []);

  const brtFactor = (brt: number): number =>
    brt > 0 ? 0.10 + 0.90 * (brt / 100) : 0;

  const applyStreetPattern = useCallback((
    currMotionState: MotionState,
    brt: number = brightness,
    spd: number = speed,
  ) => {
    if (!writeToDeviceRef.current) return;

    const pts = (hwSettings?.ledPoints as number | undefined) || points || 16;
    const profile = LOCAL_PRODUCT_CATALOG.find(p => p.id === activeProduct) || LOCAL_PRODUCT_CATALOG[0];
    const segments = profile.vizIsMirrored && hwSettings && hwSettings.segments ? (hwSettings.segments as number) : 1;

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
      writeToDeviceRef.current(payload).catch(e => {
        AppLogger.error('[useStreetMode] Failed to write pattern to device', e instanceof Error ? e.message : String(e), { payload_size: payload.length, ssi: 0 });
      });
    }
  }, [hwSettings, points, activeProduct, streetCruiseColor, brightness, speed, streetDistribution, isStreetBraking]);

  const updateMotion = useCallback((newState: MotionState) => {
    if (newState !== motionStateRef.current) {
      motionStateRef.current = newState;
      setMotionState(newState);
      applyStreetPattern(newState);
    }
  }, [applyStreetPattern]);

  // ── On-Enter: Fire initial pattern immediately when switching to STREET ────
  // Without this, the GPS evaluator only fires when gpsSpeed *changes* — if the
  // user is stationary the value stays the same and no pattern ever gets sent.
  useEffect(() => {
    if (activeMode !== 'STREET') return;
    // Rely on BleWriteQueue internal timing instead of a hardcoded delay
    applyStreetPattern(motionStateRef.current);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activeMode]);

  // ── GPS Motion State Evaluator ──────────────────────────────────────────
  useEffect(() => {
    if (activeMode !== 'STREET') return;

    lastGpsSpeeds.current.push(gpsSpeed);
    if (lastGpsSpeeds.current.length > 3) lastGpsSpeeds.current.shift();

    if (motionStateRef.current !== 'HARD_BRAKING') {
      if (gpsSpeed < 1.0 && peakGForce < 1.1) {
        updateMotion('STOPPED');
      } else if (lastGpsSpeeds.current.length >= 2) {
        const oldSpd = lastGpsSpeeds.current[0];
        if (oldSpd - gpsSpeed > 1.0) {
          updateMotion('SLOWING_DOWN');
        } else if (gpsSpeed >= 1.0) {
          updateMotion('CRUISING');
        }
      } else if (gpsSpeed >= 1.0) {
        updateMotion('CRUISING');
      }
    }
  }, [gpsSpeed, peakGForce, activeMode, updateMotion]);

  // ── Accelerometer Jerk Detector ──────────────────────────────────────────
  useEffect(() => {
    if (activeMode !== 'STREET') {
      return;
    }

    if (Platform.OS === 'web') return;

    Accelerometer.setUpdateInterval(80);
    const sub = Accelerometer.addListener(({ x, y, z }) => {
      const prev = lastAccelRef.current;
      const jerkMag = Math.sqrt(
        Math.pow(x - prev.x, 2) + Math.pow(y - prev.y, 2) + Math.pow(z - prev.z, 2)
      );
      lastAccelRef.current = { x, y, z };
      const threshold = ((100 - streetSensitivity) / 100) * 2.5 + 0.3;

      const isBrakingJerk = jerkMag > threshold;
      const isActivePush = jerkMag > 0.4 && !isBrakingJerk;

      if (isBrakingJerk) {
        if (motionStateRef.current !== 'HARD_BRAKING') {
          const sanitizedContext = { ...deviceContextRef.current };
          if (sanitizedContext.mac) sanitizedContext.mac = scrubPII(String(sanitizedContext.mac));
          if (sanitizedContext.deviceId) sanitizedContext.deviceId = scrubPII(String(sanitizedContext.deviceId));
          if (sanitizedContext.macAddress) sanitizedContext.macAddress = scrubPII(String(sanitizedContext.macAddress));
          AppLogger.log('STREET_JERK_DETECTED', { jerkMag: parseFloat(jerkMag.toFixed(3)), threshold: parseFloat(threshold.toFixed(3)), gpsSpeedMph: gpsSpeedRef.current, ...sanitizedContext });
        }
        updateMotion('HARD_BRAKING');
      } else {
        if (motionStateRef.current === 'HARD_BRAKING') {
          if (lastGpsSpeeds.current[lastGpsSpeeds.current.length - 1] < 1.0) updateMotion('STOPPED');
          else updateMotion('CRUISING');
        } else if (isActivePush && motionStateRef.current !== 'SLOWING_DOWN') {
          updateMotion('ACCELERATING');
        }
      }
    });

    return () => {
      sub.remove();
    };
  }, [activeMode, streetSensitivity, updateMotion]);

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
    applyStreetPattern,
    streetDistribution,
    setStreetDistribution,
  };
}

