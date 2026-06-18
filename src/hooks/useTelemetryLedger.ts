import { useEffect, useRef, useCallback } from 'react';
import { AppState, AppStateStatus } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { supabase } from '../services/supabaseClient';
import { AppLogger } from '../services/appLogger';

import { STORAGE_TELEMETRY_BUFFER } from '../constants/storageKeys';

const TELEMETRY_BUFFER_KEY = STORAGE_TELEMETRY_BUFFER;

export interface TelemetryPayload {
  total_app_time_sec?: number;
  total_distance_meters?: number;
  lifetime_top_speed_mph?: number;
  total_street_sessions?: number;
  pattern_time_map?: Record<string, number>;
  color_time_map?: Record<string, number>;
  engagement_counters?: Record<string, number>;
}

let _sharedFlushTimer: ReturnType<typeof setInterval> | null = null;
const _registeredFlushCallbacks = new Set<() => void>();

function startSharedTimer() {
  if (_sharedFlushTimer === null) {
    _sharedFlushTimer = setInterval(() => {
      _registeredFlushCallbacks.forEach(cb => cb());
    }, 15 * 60 * 1000);
  }
}

function stopSharedTimer() {
  if (_registeredFlushCallbacks.size === 0 && _sharedFlushTimer !== null) {
    clearInterval(_sharedFlushTimer);
    _sharedFlushTimer = null;
  }
}

// GLOBAL STATE
let _payloadBuffer: TelemetryPayload = {};
let _activeState: { type: 'pattern' | 'color' | 'mode', id: string, startTime: number } | null = null;
let _sessionStartTime: number = Date.now();

// Global Helpers
const mergeIntoBuffer = (incoming: TelemetryPayload) => {
  const current = _payloadBuffer;
  
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

const closeCurrentState = () => {
  if (!_activeState) return;
  const { type, id, startTime } = _activeState;
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
  _activeState = null;
};

let _flushPromise: Promise<void> | null = null;
let _flushPending = false;

const _doFlush = async () => {
  if (!supabase) return;
  
  try {
    // 1. Close any active stopwatch
    closeCurrentState();

    // 2. Add total app time since last flush
    const elapsedAppTime = Math.round((Date.now() - _sessionStartTime) / 1000);
    if (elapsedAppTime > 0) {
      mergeIntoBuffer({ total_app_time_sec: elapsedAppTime });
      _sessionStartTime = Date.now();
    }

    // 3. Load offline buffer from AsyncStorage and merge
    try {
      const offlineRaw = await AsyncStorage.getItem(TELEMETRY_BUFFER_KEY);
      if (offlineRaw) {
        const offlineData: TelemetryPayload = JSON.parse(offlineRaw);
        mergeIntoBuffer(offlineData);
      }
    } catch (e: unknown) {
      AppLogger.error('[useTelemetryLedger] Failed to parse offline buffer', e instanceof Error ? e.message : String(e), { payload_size: 0, ssi: 0 });
    }

    // If buffer is completely empty, skip network call
    if (Object.keys(_payloadBuffer).length === 0) return;

    // Snapshot payload for upload
    const payloadToUpload = { ..._payloadBuffer };

    try {
      // Push to Supabase RPC
      const { error } = await supabase.rpc('flush_telemetry', { payload: payloadToUpload });
      if (error) throw error;

      // Success! Clear memory buffer and AsyncStorage
      _payloadBuffer = {};
      await AsyncStorage.removeItem(TELEMETRY_BUFFER_KEY);
      AppLogger.debug('Telemetry flushed successfully', { payload_size: 0, ssi: 0 });
      
    } catch (err: unknown) {
      // Failed (e.g., offline). Save back to AsyncStorage to retry later.
      AppLogger.warn('Telemetry flush failed, buffering locally', { error: err instanceof Error ? err.message : String(err), payload_size: 0, ssi: 0 });
      try {
        await AsyncStorage.setItem(TELEMETRY_BUFFER_KEY, JSON.stringify(payloadToUpload));
        // Reset memory buffer so we don't accumulate duplicates if it stays running
        _payloadBuffer = {};
      } catch (storageErr: unknown) {
        AppLogger.error('[useTelemetryLedger] Fatal: could not buffer telemetry to AsyncStorage', storageErr instanceof Error ? storageErr.message : String(storageErr), { payload_size: 0, ssi: 0 });
      }
    }
  } catch (e: unknown) {
    AppLogger.error('[useTelemetryLedger] flushToDatabase failed', e instanceof Error ? e.message : String(e), { payload_size: 0, ssi: 0 });
  }
};

const flushToDatabase = async (): Promise<void> => {
  if (_flushPromise) {
    _flushPending = true;
    return _flushPromise.then(() => {
      if (_flushPending) {
        _flushPending = false;
        return flushToDatabase();
      }
    });
  }
  
  _flushPromise = _doFlush().finally(() => {
    _flushPromise = null;
    if (_flushPending) {
      _flushPending = false;
      flushToDatabase();
    }
  });
  return _flushPromise;
};

/**
 * God-Tier Telemetry Engine
 * Tracks time-in-state and caches offline to AsyncStorage.
 */
export function useTelemetryLedger() {
  const trackPattern = useCallback((patternId: number | string) => {
    closeCurrentState();
    _activeState = { type: 'pattern', id: `pattern_${patternId}`, startTime: Date.now() };
  }, []);

  const trackColor = useCallback((hexCode: string) => {
    closeCurrentState();
    _activeState = { type: 'color', id: hexCode, startTime: Date.now() };
  }, []);

  const trackMode = useCallback((modeId: string) => {
    closeCurrentState();
    _activeState = { type: 'mode', id: modeId, startTime: Date.now() };
  }, []);

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

  // Hook into AppState to flush on backgrounding
  useEffect(() => {
    const handleAppState = (nextAppState: AppStateStatus) => {
      if (nextAppState.match(/inactive|background/)) {
        flushToDatabase();
      }
    };
    const sub = AppState.addEventListener('change', handleAppState);

    // 15-Minute Heartbeat flush
    _registeredFlushCallbacks.add(flushToDatabase);
    startSharedTimer();

    return () => {
      sub.remove();
      _registeredFlushCallbacks.delete(flushToDatabase);
      stopSharedTimer();
    };
  }, []);

  return { trackPattern, trackColor, trackMode, incrementCounter, injectStreetSummary, flushToDatabase };
}
