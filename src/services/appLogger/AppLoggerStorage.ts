import { Platform } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { createMMKV } from 'react-native-mmkv';
import { APP_LOGGER_STORAGE_KEY, TELEMETRY_MMKV_ID } from '../../constants/storageKeys';
import { LogEntry } from './types';

const LEGACY_KEY  = '@Sk8lytz_logs';
const MAX_ENTRIES = 5000;

// JSI-based synchronous store on native; null on web (no MMKV support).
const mmkvInstance = Platform.OS !== 'web'
  ? createMMKV({ id: TELEMETRY_MMKV_ID })
  : null;

const store = {
  get: (key: string): string | undefined =>
    mmkvInstance ? mmkvInstance.getString(key) : undefined,
  set: (key: string, value: string): void => {
    if (mmkvInstance) mmkvInstance.set(key, value);
  },
  delete: (key: string): void => {
    if (mmkvInstance) mmkvInstance.remove(key);
  },
};

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
        // Primary read: synchronous MMKV on native
        let raw: string | undefined = store.get(APP_LOGGER_STORAGE_KEY);

        // Migration: read from legacy AsyncStorage key, promote to MMKV, then delete.
        if (!raw) {
          const legacy = await AsyncStorage.getItem(LEGACY_KEY);
          if (legacy) {
            try {
              const parsed = JSON.parse(legacy) as LogEntry[];
              const serialised = JSON.stringify(parsed);
              store.set(APP_LOGGER_STORAGE_KEY, serialised);
              raw = serialised;
            } catch {
              // Corrupt legacy data — discard silently, do not write to MMKV.
              console.warn('[AppLoggerStorage] Legacy data corrupt — discarding');
            }
            // Always remove the legacy key regardless of parse success.
            await AsyncStorage.removeItem(LEGACY_KEY);
          }
        }

        this.buffer = raw ? (JSON.parse(raw) as LogEntry[]) : [];
      } catch (e: unknown) {
        if (__DEV__) console.warn('[AppLogger] ensureLoaded parse failed', e instanceof Error ? e.message : String(e));
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
      this.executePersist();
      return;
    }

    if (this.persistTimeout) return;
    this.persistTimeout = setTimeout(() => {
      this.persistTimeout = null;
      this.executePersist();
    }, 500);
  }

  private executePersist() {
    if (this.buffer.length > MAX_ENTRIES) {
      this.buffer = this.buffer.slice(-MAX_ENTRIES);
    }
    try {
      store.set(APP_LOGGER_STORAGE_KEY, JSON.stringify(this.buffer));
      this.lastPersistedLength = this.buffer.length;
      this.lastPersistTime = Date.now();
    } catch (e: unknown) {
      if (__DEV__) console.warn('[AppLogger] executePersist set failed', e instanceof Error ? e.message : String(e));
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

  getStorageStats(): { storageBytesEstimate: number; totalStorageEstimate: number } {
    try {
      const raw = store.get(APP_LOGGER_STORAGE_KEY);
      // MMKV-scoped: no cross-app key scan. storageBytesEstimate = logger bytes; totalStorageEstimate = same (single store).
      const storageBytesEstimate = raw ? new TextEncoder().encode(raw).length : 0;
      return { storageBytesEstimate, totalStorageEstimate: storageBytesEstimate };
    } catch (e: unknown) {
      if (__DEV__) console.warn('[AppLogger] getStorageStats failed:', e instanceof Error ? e.message : String(e));
      return { storageBytesEstimate: 0, totalStorageEstimate: 0 };
    }
  }

  clear() {
    this.buffer = [];
    try {
      store.delete(APP_LOGGER_STORAGE_KEY);
    } catch (e: unknown) {
      if (__DEV__) console.warn('[AppLogger] clear delete failed', e instanceof Error ? e.message : String(e));
    }
  }
}
