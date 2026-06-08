import { useEffect, useRef, useCallback } from 'react';
import { AppState, AppStateStatus } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { supabase } from '../services/supabaseClient';
import { AppLogger } from '../services/AppLogger';

const TELEMETRY_BUFFER_KEY = '@sk8lytz_telemetry_buffer';

export interface TelemetryPayload {
  total_app_time_sec?: number;
  total_distance_meters?: number;
  lifetime_top_speed_mph?: number;
  total_street_sessions?: number;
  pattern_time_map?: Record<string, number>;
  color_time_map?: Record<string, number>;
  engagement_counters?: Record<string, number>;
}

let globalFlushTimer: ReturnType<typeof setInterval> | null = null;

/**
 * God-Tier Telemetry Engine
 * Tracks time-in-state and caches offline to AsyncStorage.
 */
export function useTelemetryLedger() {
  const payloadBuffer = useRef<TelemetryPayload>({});
  const activeState = useRef<{ type: 'pattern' | 'color' | 'mode', id: string, startTime: number } | null>(null);
  const sessionStartTime = useRef<number>(Date.now());
  const _isFlushingRef = useRef(false);

  // Helper to merge payloads locally
  const mergeIntoBuffer = (incoming: TelemetryPayload) => {
    const current = payloadBuffer.current;
    
    if (incoming.total_app_time_sec) current.total_app_time_sec = (current.total_app_time_sec || 0) + incoming.total_app_time_sec;
    if (incoming.total_distance_meters) current.total_distance_meters = (current.total_distance_meters || 0) + incoming.total_distance_meters;
    if (incoming.lifetime_top_speed_mph) current.lifetime_top_speed_mph = Math.max(current.lifetime_top_speed_mph || 0, incoming.lifetime_top_speed_mph);
    if (incoming.total_street_sessions) current.total_street_sessions = (current.total_street_sessions || 0) + incoming.total_street_sessions;
    
    if (incoming.pattern_time_map) {
      if (!current.pattern_time_map) current.pattern_time_map = {};
      Object.keys(incoming.pattern_time_map).forEach(k => {
        current.pattern_time_map![k] = (current.pattern_time_map![k] || 0) + incoming.pattern_time_map![k];
      });
    }

    if (incoming.color_time_map) {
      if (!current.color_time_map) current.color_time_map = {};
      Object.keys(incoming.color_time_map).forEach(k => {
        current.color_time_map![k] = (current.color_time_map![k] || 0) + incoming.color_time_map![k];
      });
    }

    if (incoming.engagement_counters) {
      if (!current.engagement_counters) current.engagement_counters = {};
      Object.keys(incoming.engagement_counters).forEach(k => {
        current.engagement_counters![k] = (current.engagement_counters![k] || 0) + incoming.engagement_counters![k];
      });
    }
  };

  // Close the current stopwatch and add to buffer
  const closeCurrentState = useCallback(() => {
    if (!activeState.current) return;
    const { type, id, startTime } = activeState.current;
    const elapsedSec = Math.round((Date.now() - startTime) / 1000);
    
    if (elapsedSec > 0) {
      if (type === 'pattern') {
        mergeIntoBuffer({ pattern_time_map: { [id]: elapsedSec } });
      } else if (type === 'color') {
        mergeIntoBuffer({ color_time_map: { [id]: elapsedSec } });
      } else if (type === 'mode') {
        mergeIntoBuffer({ engagement_counters: { [`mode_${id}_sec`]: elapsedSec } });
      }
    }
    activeState.current = null;
  }, []);

  const trackPattern = useCallback((patternId: number | string) => {
    closeCurrentState();
    activeState.current = { type: 'pattern', id: `pattern_${patternId}`, startTime: Date.now() };
  }, [closeCurrentState]);

  const trackColor = useCallback((hexCode: string) => {
    closeCurrentState();
    activeState.current = { type: 'color', id: hexCode, startTime: Date.now() };
  }, [closeCurrentState]);

  const trackMode = useCallback((modeId: string) => {
    closeCurrentState();
    activeState.current = { type: 'mode', id: modeId, startTime: Date.now() };
  }, [closeCurrentState]);

  const incrementCounter = useCallback((counterKey: string, count: number = 1) => {
    mergeIntoBuffer({ engagement_counters: { [counterKey]: count } });
  }, []);

  const injectStreetSummary = useCallback((distanceMeters: number, topSpeedMph: number) => {
    mergeIntoBuffer({
      total_distance_meters: distanceMeters,
      lifetime_top_speed_mph: topSpeedMph,
      total_street_sessions: 1
    });
  }, []);

  const flushToDatabase = useCallback(async () => {
    if (_isFlushingRef.current) return;
    if (!supabase) return;
    
    _isFlushingRef.current = true;
    try {
      // 1. Close any active stopwatch
      closeCurrentState();

    // 2. Add total app time since last flush
    const elapsedAppTime = Math.round((Date.now() - sessionStartTime.current) / 1000);
    if (elapsedAppTime > 0) {
      mergeIntoBuffer({ total_app_time_sec: elapsedAppTime });
      sessionStartTime.current = Date.now();
    }

    // 3. Load offline buffer from AsyncStorage and merge
    try {
      const offlineRaw = await AsyncStorage.getItem(TELEMETRY_BUFFER_KEY);
      if (offlineRaw) {
        const offlineData: TelemetryPayload = JSON.parse(offlineRaw);
        mergeIntoBuffer(offlineData);
      }
    } catch (e: unknown) {
      // Ignore parse error
    }

    // If buffer is completely empty, skip network call
    if (Object.keys(payloadBuffer.current).length === 0) return;

    // Snapshot payload for upload
    const payloadToUpload = { ...payloadBuffer.current };

    try {
      // Push to Supabase RPC
      const { error } = await supabase.rpc('flush_telemetry', { payload: payloadToUpload });
      if (error) throw error;

      // Success! Clear memory buffer and AsyncStorage
      payloadBuffer.current = {};
      await AsyncStorage.removeItem(TELEMETRY_BUFFER_KEY);
      AppLogger.debug('Telemetry flushed successfully');
      
    } catch (err: unknown) {
      // Failed (e.g., offline). Save back to AsyncStorage to retry later.
      AppLogger.warn('Telemetry flush failed, buffering locally', err instanceof Error ? err.message : String(err));
      try {
        await AsyncStorage.setItem(TELEMETRY_BUFFER_KEY, JSON.stringify(payloadToUpload));
        // Reset memory buffer so we don't accumulate duplicates if it stays running
        payloadBuffer.current = {};
      } catch (storageErr: unknown) {
        // Fatal storage error
      }
    }
    } finally {
      _isFlushingRef.current = false;
    }
  }, [closeCurrentState]);

  // Hook into AppState to flush on backgrounding
  useEffect(() => {
    const subscription = AppState.addEventListener('change', (nextAppState: AppStateStatus) => {
      if (nextAppState.match(/inactive|background/)) {
        flushToDatabase();
      }
    });

    // 15-Minute Heartbeat flush
    if (!globalFlushTimer) {
      globalFlushTimer = setInterval(() => {
        flushToDatabase();
      }, 15 * 60 * 1000);
    }

    return () => {
      subscription.remove();
      if (globalFlushTimer) {
        clearInterval(globalFlushTimer);
        globalFlushTimer = null;
      }
    };
  }, [flushToDatabase]);

  return { trackPattern, trackColor, trackMode, incrementCounter, injectStreetSummary, flushToDatabase };
}
