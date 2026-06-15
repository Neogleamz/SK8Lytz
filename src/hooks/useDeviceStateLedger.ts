/**
 * useDeviceStateLedger.ts — Unified Per-Device Pattern State Ledger
 *
 * Single source of truth for per-device LED pattern state, replacing the
 * fragmented dual-storage system (volatile React state + miskeyed AsyncStorage).
 *
 * Architecture:
 *  - In-memory Map<MAC, DevicePatternState> for synchronous reads (dashboard cards, lazy useState)
 *  - Debounced AsyncStorage writes (500ms) — rapid slider changes don't hammer storage
 *  - All keys normalized via normalizeMac() — kills the Supabase-composite vs BLE-MAC key mismatch
 *
 * Storage key: `@SK8Lytz_DeviceState_v2_{MAC_UPPERCASE}`
 * 
 * CONTRACT BOUNDARY:
 * This hook ONLY tracks actual hardware dispatch state (what was sent over BLE).
 * It does NOT track the UI slider positions or temporary controller modes.
 * For UI widget state, see `useControllerPersistence.ts`.
 */
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useCallback, useEffect, useRef } from 'react';
import { AppState, AppStateStatus } from 'react-native';
import { AppLogger } from '../services/appLogger';
import type { DevicePatternState } from '../types/dashboard.types';
import { scrubPII } from '../utils/piiScrubber';


import { STORAGE_DEVICE_STATE_V2_PREFIX } from '../constants/storageKeys';

const KEY_PREFIX = STORAGE_DEVICE_STATE_V2_PREFIX;

/**
 * Normalize any device ID to a clean uppercase MAC address.
 * Strips Supabase composite suffixes (e.g. 'AA:BB:CC:DD:EE:FF_userId123' → 'AA:BB:CC:DD:EE:FF').
 * Safe to call with a raw BLE MAC — it passes through unchanged.
 */
export const normalizeMac = (rawId: string): string => {
  if (!rawId) return '';
  return String(rawId).split('_')[0].toUpperCase().replace(/[^A-F0-9:]/g, '');
};

/**
 * Returns true if the ledger entry is older than 24 hours.
 * Used to reduce opacity of stale pattern previews on device cards.
 */
export const isStale = (ts: number): boolean =>
  Date.now() - ts > 24 * 60 * 60 * 1000;

declare global {
  var __sk8lytz_ledger_cache: Map<string, DevicePatternState> | undefined;
  var __sk8lytz_ledger_timers: Map<string, NodeJS.Timeout> | undefined;
}

/**
 * Module-level in-memory cache shared across all hook instances.
 * Populated on load(), updated on save(). Synchronous — no async penalty.
 */
const memoryCache: Map<string, DevicePatternState> = global.__sk8lytz_ledger_cache || new Map<string, DevicePatternState>();

/**
 * Module-level debounce timer map — shared across ALL hook instances.
 * Ensures only ONE AsyncStorage write is in-flight per MAC at any time,
 * even when DashboardScreen and DockedController both call save() simultaneously.
 * Previously this was a per-instance useRef, causing independent timers to race.
 */
const debounceTimers: Map<string, NodeJS.Timeout> = global.__sk8lytz_ledger_timers || new Map<string, NodeJS.Timeout>();

if (__DEV__) {
  global.__sk8lytz_ledger_cache = memoryCache;
  global.__sk8lytz_ledger_timers = debounceTimers;
}

// Moved to useEffect inside useDeviceStateLedger

/**
 * Warm the in-memory cache from AsyncStorage on app boot.
 * MUST be called once at startup (e.g. DashboardScreen mount) so that
 * loadSync() returns valid data for lazy useState initializers in
 * useDockedControllerState. Without this, cache is always empty on cold
 * start and pre-warm never fires even though data exists in storage.
 */
let _warmPromise: Promise<void> | null = null;

export async function warmLedgerCache(): Promise<void> {
  if (_warmPromise) return _warmPromise;

  _warmPromise = (async () => {
    try {
      const allKeys = await AsyncStorage.getAllKeys();
      const ledgerKeys = allKeys.filter(k => k.startsWith(KEY_PREFIX));
      if (ledgerKeys.length === 0) return;
      const pairs = await AsyncStorage.multiGet(ledgerKeys);
      pairs.forEach(([, raw]) => {
        if (!raw) return;
        try {
          const parsed: DevicePatternState = JSON.parse(raw);
          if (parsed?.deviceMac) {
            memoryCache.set(normalizeMac(parsed.deviceMac), parsed);
          }
        } catch (e: unknown) {
          AppLogger.warn('Failed to parse ledger entry from storage during cache warm', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
        }
      });
    } catch (e: unknown) {
      AppLogger.error('Failed to warm ledger cache from AsyncStorage', e instanceof Error ? e.message : String(e), { payload_size: 0, ssi: 0 });
    }
  })();

  return _warmPromise.finally(() => {
    _warmPromise = null;
  });
}

const _loadLocks = new Map<string, Promise<DevicePatternState | null>>();

export function useDeviceStateLedger() {
  // debounceTimers is now module-level (shared singleton) — see top of file.

  useEffect(() => {
    const sub = AppState.addEventListener('change', (next: AppStateStatus) => {
      if (next === 'background') {
        for (const [key, timer] of debounceTimers.entries()) {
          clearTimeout(timer);
          const entry = memoryCache.get(key);
          if (entry) {
            AsyncStorage.setItem(`${KEY_PREFIX}${key}`, JSON.stringify(entry)).catch(e => AppLogger.error('Failed to write device state ledger entry on background', e instanceof Error ? e.message : String(e), { payload_size: 0, ssi: 0 }));
          }
        }
        debounceTimers.clear();
      }
    });
    return () => sub.remove();
  }, []);

  /**
   * Save device pattern state to ledger.
   * Immediately updates in-memory cache; debounces AsyncStorage write by 500ms.
   */
  const save = useCallback((mac: string, state: DevicePatternState): void => {
    const key = normalizeMac(mac);
    const entry: DevicePatternState = { ...state, deviceMac: key };

    // Synchronous in-memory update — available immediately for loadSync()
    memoryCache.set(key, entry);

    // Debounced AsyncStorage write — module-level timer map prevents dual-instance race
    const LEDGER_WRITE_DEBOUNCE_MS = 500;
    const existing = debounceTimers.get(key);
    if (existing) clearTimeout(existing);

    const timer = setTimeout(() => {
      AsyncStorage.setItem(`${KEY_PREFIX}${key}`, JSON.stringify(entry)).catch((e) => {
        AppLogger.warn('PERSISTENCE', { key: '[REDACTED]', event: 'ledger_write_failed', error: (e instanceof Error ? e.message : String(e)), payload_size: 0, ssi: 0 });
      });
      debounceTimers.delete(key);
    }, LEDGER_WRITE_DEBOUNCE_MS);

    debounceTimers.set(key, timer);
  }, []);

  /**
   * Load device pattern state from ledger.
   * Checks in-memory cache first, falls back to AsyncStorage.
   * Updates in-memory cache on AsyncStorage hit.
   */
  const load = useCallback(async (mac: string): Promise<DevicePatternState | null> => {
    const key = normalizeMac(mac);

    // Fast path: in-memory cache hit
    if (memoryCache.has(key)) {
      return memoryCache.get(key)!;
    }

    if (_loadLocks.has(key)) {
      return _loadLocks.get(key)!;
    }

    // Slow path: AsyncStorage lookup
    const loadPromise = (async () => {
      try {
        const raw = await AsyncStorage.getItem(`${KEY_PREFIX}${key}`);
        if (!raw) return null;
        const parsed: DevicePatternState = JSON.parse(raw);
        // Warm the cache for future synchronous reads
        memoryCache.set(key, parsed);
        return parsed;
      } catch (e: unknown) {
        AppLogger.error('Failed to read device state ledger entry from storage', e instanceof Error ? e.message : String(e), { deviceId: scrubPII(key), payload_size: 0, ssi: 0 });
        return null;
      }
    })();

    _loadLocks.set(key, loadPromise);

    try {
      return await loadPromise;
    } finally {
      _loadLocks.delete(key);
    }
  }, []);

  /**
   * Synchronous in-memory read. Returns null if not cached yet.
   * Use this for lazy useState initializers and dashboard card renders.
   * Call load() first to warm the cache if needed.
   */
  const loadSync = useCallback((mac: string): DevicePatternState | null => {
    const key = normalizeMac(mac);
    return memoryCache.get(key) ?? null;
  }, []);

  /**
   * Remove a device's state from both in-memory cache and AsyncStorage.
   */
  const clear = useCallback(async (mac: string): Promise<void> => {
    const key = normalizeMac(mac);

    // Cancel any pending debounced write before wiping from cache
    const timer = debounceTimers.get(key);
    if (timer) {
      clearTimeout(timer);
      debounceTimers.delete(key);
    }

    memoryCache.delete(key);
    await AsyncStorage.removeItem(`${KEY_PREFIX}${key}`).catch((e) => AppLogger.warn('PERSISTENCE', { key: '[REDACTED]', event: 'ledger_clear_failed', error: (e instanceof Error ? e.message : String(e)), payload_size: 0, ssi: 0 }));
  }, []);

  return { save, load, loadSync, clear };
}
