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

  // Mode change logger
  useEffect(() => {
    AppLogger.log('MODE_CHANGED', { mode: activeMode, ...deviceContext });
  }, [activeMode, deviceContext]);

  // Pattern change logger
  useEffect(() => {
    const name = `Pattern ${selectedPatternId}`;
    AppLogger.log('PATTERN_CHANGED', {
      pattern: `ID:${selectedPatternId}`,
      name,
      mode: activeMode,
      color: selectedColor,
      ...deviceContext
    });
  }, [selectedPatternId, deviceContext]);

  // Color change logger
  useEffect(() => {
    AppLogger.log('COLOR_CHANGED', { hex: selectedColor, ...deviceContext });
  }, [selectedColor, deviceContext]);

  // Brightness change logger (debounced 600ms)
  useEffect(() => {
    clearTimeout(logTimers.current['brightness']);
    logTimers.current['brightness'] = setTimeout(() => {
      AppLogger.log('BRIGHTNESS_CHANGED', { value: brightness, mode: activeMode, ...deviceContext });
    }, 600);
  }, [brightness, activeMode, deviceContext]);

  // Speed change logger (debounced 600ms)
  useEffect(() => {
    clearTimeout(logTimers.current['speed']);
    logTimers.current['speed'] = setTimeout(() => {
      AppLogger.log('SPEED_CHANGED', { value: speed, mode: activeMode, ...deviceContext });
    }, 600);
  }, [speed, activeMode, deviceContext]);

  // Street sensitivity change logger (debounced 800ms)
  useEffect(() => {
    if (activeMode !== 'STREET') return;
    clearTimeout(logTimers.current['streetSens']);
    logTimers.current['streetSens'] = setTimeout(() => {
      AppLogger.log('STREET_SENSITIVITY_CHANGED', { sensitivity: streetSensitivity, ...deviceContext });
    }, 800);
  }, [streetSensitivity, activeMode, deviceContext]);
}
