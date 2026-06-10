/**
 * useControllerAnalytics.ts — Debounced telemetry logging for DockedController.
 *
 * Owns: MODE_CHANGED, PATTERN_CHANGED, COLOR_CHANGED, BRIGHTNESS_CHANGED,
 *       SPEED_CHANGED, STREET_SENSITIVITY_CHANGED event dispatch.
 *
 * Extracted from DockedController.tsx to isolate analytics side-effects.
 */
import { useEffect, useRef } from 'react';
import { AppLogger } from '../services/AppLogger';
import type { ModeType } from '../types/dashboard.types';
import { useTelemetryLedger } from './useTelemetryLedger';

interface DeviceContext {
  target: string;
  deviceId?: string;
  deviceIds?: string[];
  groupSize?: number;
}

interface UseControllerAnalyticsParams {
  activeMode: ModeType;
  selectedPatternId: number;
  selectedColor: string;
  brightness: number;
  speed: number;
  streetSensitivity: number;
  deviceContext: DeviceContext;
}

/**
 * Side-effect-only hook that dispatches debounced telemetry events for
 * mode, pattern, color, brightness, speed, and street sensitivity changes.
 */
export function useControllerAnalytics({
  activeMode,
  selectedPatternId,
  selectedColor,
  brightness,
  speed,
  streetSensitivity,
  deviceContext,
}: UseControllerAnalyticsParams) {
  const logTimers = useRef<Record<string, ReturnType<typeof setTimeout>>>({});
  const telemetry = useTelemetryLedger();
  // Fix 6: Serialize deviceContext to a stable string key so useEffect deps don't
  // fire spuriously when the devices[] array reference changes without content change.
  const deviceContextKey = JSON.stringify(deviceContext);

  // Mode change logger
  useEffect(() => {
    telemetry.trackMode(activeMode);
    AppLogger.log('MODE_CHANGED', { mode: activeMode, ...deviceContext });
  }, [activeMode, deviceContextKey, telemetry]); // eslint-disable-line react-hooks/exhaustive-deps

  // Pattern change logger
  useEffect(() => {
    const name = `Pattern ${selectedPatternId}`;
    
    // Inject into God-Tier telemetry accumulator
    if (activeMode !== 'FAVORITES') {
      telemetry.trackPattern(selectedPatternId);
    }
    
    AppLogger.log('PATTERN_CHANGED', {
      pattern: `ID:${selectedPatternId}`,
      name,
      mode: activeMode,
      color: selectedColor,
      ...deviceContext
    });
  }, [selectedPatternId, activeMode, deviceContextKey, telemetry]); // eslint-disable-line react-hooks/exhaustive-deps

  // Color change logger
  useEffect(() => {
    // Inject into God-Tier telemetry accumulator
    // Track color in all modes where user is explicitly picking a color
    // (MUSIC uses its own dual-color tracking; CAMERA has no color picking)
    if (activeMode !== 'MUSIC' && activeMode !== 'CAMERA') {
      telemetry.trackColor(selectedColor);
    }
    
    AppLogger.log('COLOR_CHANGED', { hex: selectedColor, ...deviceContext });
  }, [selectedColor, activeMode, deviceContextKey, telemetry]); // eslint-disable-line react-hooks/exhaustive-deps

  // Brightness change logger (debounced 600ms)
  useEffect(() => {
    clearTimeout(logTimers.current['brightness']);
    const timerId = setTimeout(() => {
      AppLogger.log('BRIGHTNESS_CHANGED', { value: brightness, mode: activeMode, ...deviceContext });
    }, 600);
    logTimers.current['brightness'] = timerId;
    return () => clearTimeout(timerId);
  }, [brightness, activeMode, deviceContextKey]); // eslint-disable-line react-hooks/exhaustive-deps

  // Speed change logger (debounced 600ms)
  useEffect(() => {
    clearTimeout(logTimers.current['speed']);
    const timerId = setTimeout(() => {
      AppLogger.log('SPEED_CHANGED', { value: speed, mode: activeMode, ...deviceContext });
    }, 600);
    logTimers.current['speed'] = timerId;
    return () => clearTimeout(timerId);
  }, [speed, activeMode, deviceContextKey]); // eslint-disable-line react-hooks/exhaustive-deps

  // Street sensitivity change logger (debounced 800ms)
  useEffect(() => {
    if (activeMode !== 'STREET') return;
    clearTimeout(logTimers.current['streetSens']);
    const timerId = setTimeout(() => {
      AppLogger.log('STREET_SENSITIVITY_CHANGED', { sensitivity: streetSensitivity, ...deviceContext });
    }, 800);
    logTimers.current['streetSens'] = timerId;
    return () => clearTimeout(timerId);
  }, [streetSensitivity, activeMode, deviceContextKey]); // eslint-disable-line react-hooks/exhaustive-deps
}
