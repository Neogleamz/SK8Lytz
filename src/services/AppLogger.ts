/**
 * AppLogger - SK8Lytz Analytics & Event Logger
 * Stores compact structured logs locally with rotation.
 * Export-ready for future webhook/database upload.
 */
import AsyncStorage from '@react-native-async-storage/async-storage';

const STORAGE_KEY = '@sk8lytz_logs';
const MAX_ENTRIES = 500;

export type EventType =
  | 'APP_OPENED'
  | 'SCAN_STARTED'
  | 'SCAN_COMPLETED'
  | 'DEVICE_DISCOVERED'
  | 'DEVICE_CONNECTED'
  | 'DEVICE_DISCONNECTED'
  | 'MODE_CHANGED'
  | 'PATTERN_CHANGED'
  | 'COLOR_CHANGED'
  | 'BRIGHTNESS_CHANGED'
  | 'SPEED_CHANGED';

export interface LogEntry {
  t: number;        // timestamp ms
  e: EventType;     // event type
  d: Record<string, any>; // payload
}

class AppLoggerService {
  private buffer: LogEntry[] = [];
  private loaded = false;

  private async ensureLoaded() {
    if (this.loaded) return;
    try {
      const raw = await AsyncStorage.getItem(STORAGE_KEY);
      this.buffer = raw ? JSON.parse(raw) : [];
    } catch {
      this.buffer = [];
    }
    this.loaded = true;
  }

  private async persist() {
    try {
      // Rotate — keep last MAX_ENTRIES
      if (this.buffer.length > MAX_ENTRIES) {
        this.buffer = this.buffer.slice(-MAX_ENTRIES);
      }
      await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(this.buffer));
    } catch (e) {
      console.warn('[AppLogger] persist failed', e);
    }
  }

  async log(event: EventType, payload: Record<string, any> = {}) {
    await this.ensureLoaded();
    const entry: LogEntry = { t: Date.now(), e: event, d: payload };
    this.buffer.push(entry);
    this.persist(); // fire-and-forget
    if (__DEV__) console.log(`[AppLogger] ${event}`, payload);
  }

  async getLogs(): Promise<LogEntry[]> {
    await this.ensureLoaded();
    return [...this.buffer].reverse(); // newest first
  }

  async clearLogs() {
    this.buffer = [];
    await AsyncStorage.removeItem(STORAGE_KEY);
  }

  async exportJSON(): Promise<string> {
    await this.ensureLoaded();
    return JSON.stringify({
      app: 'SK8Lytz',
      exported: new Date().toISOString(),
      count: this.buffer.length,
      logs: this.buffer,
    }, null, 2);
  }

  /** Aggregate usage stats for the Stats tab */
  async getStats(): Promise<{
    modeUsage: Record<string, number>;
    patternUsage: Record<string, number>;
    colorUsage: Record<string, number>;
    devicesDiscovered: number;
    totalEvents: number;
  }> {
    await this.ensureLoaded();
    const modeUsage: Record<string, number> = {};
    const patternUsage: Record<string, number> = {};
    const colorUsage: Record<string, number> = {};
    let devicesDiscovered = 0;

    for (const entry of this.buffer) {
      if (entry.e === 'MODE_CHANGED' && entry.d.mode) {
        modeUsage[entry.d.mode] = (modeUsage[entry.d.mode] || 0) + 1;
      }
      if (entry.e === 'PATTERN_CHANGED' && entry.d.pattern) {
        patternUsage[entry.d.pattern] = (patternUsage[entry.d.pattern] || 0) + 1;
      }
      if (entry.e === 'COLOR_CHANGED' && entry.d.hex) {
        colorUsage[entry.d.hex] = (colorUsage[entry.d.hex] || 0) + 1;
      }
      if (entry.e === 'DEVICE_DISCOVERED') {
        devicesDiscovered++;
      }
    }
    return { modeUsage, patternUsage, colorUsage, devicesDiscovered, totalEvents: this.buffer.length };
  }
}

export const AppLogger = new AppLoggerService();
