import AsyncStorage from '@react-native-async-storage/async-storage';
import { AppLogger } from './appLogger';
import { scrubPII } from '../utils/piiScrubber';
import { getGattCacheKey } from '../constants/storageKeys';

export interface BleCacheEntry {
  mac: string;
  protocolId: string;
  timestamp: number;
}

const TTL_MS = 24 * 60 * 60 * 1000; // 24 hours
// R-24: GATT adapter cache key. Built via getGattCacheKey() from the central
// storageKeys.ts registry. Format: '@sk8_gatt_<MAC_UPPERCASE>'.

/**
 * Persists discovered GATT adapter types to skip discovery on reconnect.
 */
export class BleCharacteristicCache {
  static async get(mac: string): Promise<BleCacheEntry | null> {
    try {
      const val = await AsyncStorage.getItem(getGattCacheKey(mac));
      if (!val) return null;
      
      const entry: BleCacheEntry = JSON.parse(val);
      if (Date.now() - entry.timestamp > TTL_MS) {
        // Stale cache — force a fresh discovery
        await AsyncStorage.removeItem(getGattCacheKey(mac));
        return null;
      }
      return entry;
    } catch (e: unknown) {
      AppLogger.warn('[BleCache] Failed to read GATT cache', { deviceId: scrubPII(mac), error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
      return null;
    }
  }

  static async set(mac: string, protocolId: string): Promise<void> {
    try {
      const entry: BleCacheEntry = {
        mac: mac.toUpperCase(),
        protocolId,
        timestamp: Date.now(),
      };
      await AsyncStorage.setItem(getGattCacheKey(mac), JSON.stringify(entry));
    } catch (e: unknown) {
      AppLogger.warn('[BleCache] Failed to write GATT cache', { deviceId: scrubPII(mac), error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
    }
  }
}
