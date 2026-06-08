import AsyncStorage from '@react-native-async-storage/async-storage';
import { AppLogger } from './AppLogger';
import { scrubPII } from '../utils/piiScrubber';

export interface BleCacheEntry {
  mac: string;
  protocolId: string;
  timestamp: number;
}

const TTL_MS = 24 * 60 * 60 * 1000; // 24 hours
const CACHE_PREFIX = '@sk8_gatt_';

/**
 * Persists discovered GATT adapter types to skip discovery on reconnect.
 */
export class BleCharacteristicCache {
  static async get(mac: string): Promise<BleCacheEntry | null> {
    try {
      const val = await AsyncStorage.getItem(`${CACHE_PREFIX}${mac.toUpperCase()}`);
      if (!val) return null;
      
      const entry: BleCacheEntry = JSON.parse(val);
      if (Date.now() - entry.timestamp > TTL_MS) {
        // Stale cache — force a fresh discovery
        await AsyncStorage.removeItem(`${CACHE_PREFIX}${mac.toUpperCase()}`);
        return null;
      }
      return entry;
    } catch (e: unknown) {
      AppLogger.warn('[BleCache] Failed to read GATT cache', { deviceId: scrubPII(mac), error: e instanceof Error ? e.message : String(e)  });
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
      await AsyncStorage.setItem(`${CACHE_PREFIX}${mac.toUpperCase()}`, JSON.stringify(entry));
    } catch (e: unknown) {
      AppLogger.warn('[BleCache] Failed to write GATT cache', { deviceId: scrubPII(mac), error: e instanceof Error ? e.message : String(e)  });
    }
  }
}
