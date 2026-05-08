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
import { useCallback } from 'react';
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
/**
 * Module-level debounce timer map — shared across ALL hook instances.
 * Ensures only ONE AsyncStorage write is in-flight per MAC at any time,
 * even when DashboardScreen and DockedController both call save() simultaneously.
 * Previously this was a per-instance useRef, causing independent timers to race.
 */
const debounceTimers = new Map<string, ReturnType<typeof setTimeout>>();

/**
 * Warm the in-memory cache from AsyncStorage on app boot.
 * MUST be called once at startup (e.g. DashboardScreen mount) so that
 * loadSync() returns valid data for lazy useState initializers in
 * useDockedControllerState. Without this, cache is always empty on cold
 * start and pre-warm never fires even though data exists in storage.
 */
export async function warmLedgerCache(): Promise<void> {
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
      } catch { /* corrupt entry — skip */ }
    });
  } catch { /* storage unavailable — silent */ }
}

export function useDeviceStateLedger() {
  // debounceTimers is now module-level (shared singleton) — see top of file.

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
    const existing = debounceTimers.get(key);
    if (existing) clearTimeout(existing);

    const timer = setTimeout(() => {
      AsyncStorage.setItem(`${KEY_PREFIX}${key}`, JSON.stringify(entry)).catch(() => {
        // Silent — storage write failure is non-fatal; in-memory cache still valid
      });
      debounceTimers.delete(key);
    }, 500);

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

    // Cancel any pending debounced write before wiping from cache
    const timer = debounceTimers.get(key);
    if (timer) {
      clearTimeout(timer);
      debounceTimers.delete(key);
    }

    memoryCache.delete(key);
    await AsyncStorage.removeItem(`${KEY_PREFIX}${key}`).catch(() => {});
  }, []);

  return { save, load, loadSync, clear };
}
