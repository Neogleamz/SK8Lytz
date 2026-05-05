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
 */
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useCallback, useRef } from 'react';
import type { DevicePatternState } from '../types/dashboard.types';

const KEY_PREFIX = '@SK8Lytz_DeviceState_v2_';

/**
 * Normalize any device ID to a clean uppercase MAC address.
 * Strips Supabase composite suffixes (e.g. 'AA:BB:CC:DD:EE:FF_userId123' → 'AA:BB:CC:DD:EE:FF').
 * Safe to call with a raw BLE MAC — it passes through unchanged.
 */
export const normalizeMac = (rawId: string): string =>
  rawId.split('_')[0].toUpperCase().replace(/[^A-F0-9:]/g, '');

/**
 * Returns true if the ledger entry is older than 24 hours.
 * Used to reduce opacity of stale pattern previews on device cards.
 */
export const isStale = (ts: number): boolean =>
  Date.now() - ts > 24 * 60 * 60 * 1000;

/**
 * Module-level in-memory cache shared across all hook instances.
 * Populated on load(), updated on save(). Synchronous — no async penalty.
 */
const memoryCache = new Map<string, DevicePatternState>();

export function useDeviceStateLedger() {
  // Per-instance debounce timers keyed by MAC
  const debounceTimers = useRef<Map<string, ReturnType<typeof setTimeout>>>(new Map());

  /**
   * Save device pattern state to ledger.
   * Immediately updates in-memory cache; debounces AsyncStorage write by 500ms.
   */
  const save = useCallback((mac: string, state: DevicePatternState): void => {
    const key = normalizeMac(mac);
    const entry: DevicePatternState = { ...state, deviceMac: key };

    // Synchronous in-memory update — available immediately for loadSync()
    memoryCache.set(key, entry);

    // Debounced AsyncStorage write — prevents hammering storage on slider drags
    const existing = debounceTimers.current.get(key);
    if (existing) clearTimeout(existing);

    const timer = setTimeout(() => {
      AsyncStorage.setItem(`${KEY_PREFIX}${key}`, JSON.stringify(entry)).catch(() => {
        // Silent — storage write failure is non-fatal; in-memory cache still valid
      });
      debounceTimers.current.delete(key);
    }, 500);

    debounceTimers.current.set(key, timer);
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

    // Slow path: AsyncStorage lookup
    try {
      const raw = await AsyncStorage.getItem(`${KEY_PREFIX}${key}`);
      if (!raw) return null;
      const parsed: DevicePatternState = JSON.parse(raw);
      // Warm the cache for future synchronous reads
      memoryCache.set(key, parsed);
      return parsed;
    } catch {
      return null;
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

    // Cancel any pending debounced write
    const timer = debounceTimers.current.get(key);
    if (timer) {
      clearTimeout(timer);
      debounceTimers.current.delete(key);
    }

    memoryCache.delete(key);
    await AsyncStorage.removeItem(`${KEY_PREFIX}${key}`).catch(() => {});
  }, []);

  return { save, load, loadSync, clear };
}
