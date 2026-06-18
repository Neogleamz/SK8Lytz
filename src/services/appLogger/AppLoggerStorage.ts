import AsyncStorage from '@react-native-async-storage/async-storage';
import { APP_LOGGER_STORAGE_KEY } from '../../constants/storageKeys';
import { LogEntry } from './types';

const STORAGE_KEY = APP_LOGGER_STORAGE_KEY;
const LEGACY_KEY  = '@Sk8lytz_logs';
const MAX_ENTRIES = 500;

export class AppLoggerStorage {
  private buffer: LogEntry[] = [];
  private loaded = false;
  private loadPromise: Promise<void> | null = null;
  private persistTimeout: ReturnType<typeof setTimeout> | null = null;
  public lastPersistedLength = 0;
  public lastPersistTime = 0;

  async ensureLoaded(): Promise<void> {
    if (this.loaded) return;
    if (this.loadPromise) return this.loadPromise;

    this.loadPromise = (async () => {
      try {
        let raw = await AsyncStorage.getItem(STORAGE_KEY);
        if (!raw) {
          const legacy = await AsyncStorage.getItem(LEGACY_KEY);
          if (legacy) {
            await AsyncStorage.setItem(STORAGE_KEY, legacy);
            await AsyncStorage.removeItem(LEGACY_KEY);
            raw = legacy;
          }
        }
        this.buffer = raw ? JSON.parse(raw) : [];
      } catch (e: unknown) {
        this.buffer = [];
      }
      this.loaded = true;
      this.loadPromise = null;
    })();

    return this.loadPromise;
  }

  persist(force = false) {
    if (force) {
      if (this.persistTimeout) {
        clearTimeout(this.persistTimeout);
        this.persistTimeout = null;
      }
      this.executePersist().catch(e => {
        if (__DEV__) console.warn('[AppLogger] persist failed', e instanceof Error ? e.message : String(e));
      });
      return;
    }

    if (this.persistTimeout) return;
    this.persistTimeout = setTimeout(() => {
      this.persistTimeout = null;
      this.executePersist().catch(e => {
        if (__DEV__) console.warn('[AppLogger] persist failed', e instanceof Error ? e.message : String(e));
      });
    }, 500);
  }

  private async executePersist() {
    if (this.buffer.length > MAX_ENTRIES) {
      this.buffer = this.buffer.slice(-MAX_ENTRIES);
    }
    try {
      await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(this.buffer));
      this.lastPersistedLength = this.buffer.length;
      this.lastPersistTime = Date.now();
    } catch (e: unknown) {
      if (__DEV__) console.warn('[AppLogger] executePersist setItem failed', e instanceof Error ? e.message : String(e));
    }
  }

  push(entry: LogEntry) {
    this.buffer.push(entry);
    this.persist();
  }

  getBuffer(): LogEntry[] {
    return this.buffer;
  }

  setBuffer(newBuffer: LogEntry[]) {
    this.buffer = newBuffer;
  }

  async getStorageStats() {
    let storageBytesEstimate = 0;
    let totalStorageEstimate = 0;
    try {
      const keys = await AsyncStorage.getAllKeys();
      const pairs = await AsyncStorage.multiGet(keys);
      pairs.forEach(([key, val]) => {
        const size = val ? val.length * 2 : 0;
        totalStorageEstimate += size;
        if (key === STORAGE_KEY) storageBytesEstimate = size;
      });
    } catch(e: unknown) {
      if (__DEV__) console.warn('[AppLogger] getStorageStats failed:', e instanceof Error ? e.message : String(e));
    }
    return { storageBytesEstimate, totalStorageEstimate };
  }

  async clear() {
    this.buffer = [];
    try {
      await AsyncStorage.removeItem(STORAGE_KEY);
    } catch (e: unknown) {
      if (__DEV__) console.warn('[AppLogger] clearLogs remove failed', e instanceof Error ? e.message : String(e));
    }
  }
}
