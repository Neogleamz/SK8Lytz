/**
 * AppLogger - SK8Lytz Analytics & Event Logger
 * Stores compact structured logs locally with rotation.
 * Export-ready for future webhook/database upload.
 */
import AsyncStorage from '@react-native-async-storage/async-storage';
import { supabase } from './supabaseClient';
import * as Device from 'expo-device';

const STORAGE_KEY = '@sk8lytz_logs';
const MAX_ENTRIES = 10000; // ~1MB of compact log data before rotation

export type EventType =
  | 'APP_OPENED'
  | 'SCAN_STARTED'
  | 'SCAN_COMPLETED'
  | 'SCAN_FILTER_MATCH'
  | 'SCAN_FILTER_REJECT'
  | 'DEVICE_DISCOVERED'
  | 'DEVICE_CONNECTED'
  | 'DEVICE_DISCONNECTED'
  | 'MODE_CHANGED'
  | 'PATTERN_CHANGED'
  | 'COLOR_CHANGED'
  | 'BRIGHTNESS_CHANGED'
  | 'SPEED_CHANGED'
  | 'HARDWARE_CONFIG_CHANGED'
  | 'PROTOCOL_ERROR'
  | 'BLE_WRITE_ERROR'
  | 'BLE_CONNECTION_ERROR';

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

  async uploadLogsToSupabase() {
    if (!supabase) {
      console.log('[AppLogger] Supabase client not configured. Skipping log upload.');
      return;
    }
    await this.ensureLoaded();
    if (this.buffer.length === 0) return;

    try {
      const jsonStr = await this.exportJSON();
      const deviceId = Device.osInternalBuildId || Device.modelId || 'unknown-device';
      const timestamp = new Date().toISOString().replace(/[:.]/g, '-');
      const filename = `logs_${deviceId}_${timestamp}.json`;

      // Uploading as a string/Blob to the 'sk8lytz-logs' bucket
      const { data, error } = await supabase.storage
        .from('sk8lytz-logs')
        .upload(filename, jsonStr, {
          contentType: 'application/json',
          upsert: true,
        });

      if (error) {
        console.error('[AppLogger] Failed to upload logs to Supabase:', error);
      } else {
        console.log(`[AppLogger] Successfully uploaded logs to Supabase: ${data.path}`);
      }
    } catch (err) {
      console.error('[AppLogger] Upload exception:', err);
    }
  }

  /** Aggregate usage stats for the Stats tab */
  async getStats(): Promise<{
    modeUsage: Record<string, number>;
    patternUsage: Record<string, { count: number; colors: string[] }>;
    colorUsage: Record<string, number>;
    devicesDiscovered: number;
    totalEvents: number;
    storageBytesEstimate: number;
    totalStorageEstimate: number;
    averageLoadTimeMs: number;
    lastAppOpenedTime: number;
  }> {
    await this.ensureLoaded();
    const modeUsage: Record<string, number> = {};
    const patternUsage: Record<string, any> = {};
    const colorUsage: Record<string, number> = {};
    let devicesDiscovered = 0;

    for (const entry of this.buffer) {
      if (entry.e === 'MODE_CHANGED' && entry.d.mode) {
        modeUsage[entry.d.mode] = (modeUsage[entry.d.mode] || 0) + 1;
      }
      if (entry.e === 'PATTERN_CHANGED' && entry.d.pattern) {
        const key = entry.d.name || entry.d.pattern;
        if (!patternUsage[key]) {
          patternUsage[key] = { count: 0, colors: new Set<string>() };
        }
        (patternUsage[key] as any).count++;
        if (entry.d.color) (patternUsage[key] as any).colors.add(entry.d.color);
      }
      if (entry.e === 'COLOR_CHANGED' && entry.d.hex) {
        colorUsage[entry.d.hex] = (colorUsage[entry.d.hex] || 0) + 1;
      }
      if (entry.e === 'DEVICE_DISCOVERED') {
        devicesDiscovered++;
      }
    }

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
    } catch(e) {}

    let totalLoadTime = 0;
    let loadTimeCount = 0;
    let lastAppOpenedTime = 0;

    for (const entry of this.buffer) {
      if (entry.e === 'APP_OPENED') {
        if (entry.t > lastAppOpenedTime) lastAppOpenedTime = entry.t;
        if (entry.d.loadTimeMs) {
          totalLoadTime += entry.d.loadTimeMs;
          loadTimeCount++;
        }
      }
    }
    const averageLoadTimeMs = loadTimeCount > 0 ? Math.round(totalLoadTime / loadTimeCount) : 0;

    // Convert Sets to Arrays for JSON serialization
    const finalPatternUsage: Record<string, { count: number; colors: string[] }> = {};
    for (const [name, data] of Object.entries(patternUsage)) {
      finalPatternUsage[name] = { 
        count: (data as any).count, 
        colors: Array.from((data as any).colors) 
      };
    }

    return { 
      modeUsage, patternUsage: finalPatternUsage, colorUsage, devicesDiscovered, 
      totalEvents: this.buffer.length, storageBytesEstimate, totalStorageEstimate,
      averageLoadTimeMs, lastAppOpenedTime
    };
  }
}

export const AppLogger = new AppLoggerService();
