/**
 * AppLogger - SK8Lytz Analytics & Event Logger
 * Stores compact structured logs locally with rotation.
 * Export-ready for future webhook/database upload.
 */
import AsyncStorage from '@react-native-async-storage/async-storage';
import { supabase } from './supabaseClient';
import * as Device from 'expo-device';
import * as Battery from 'expo-battery';

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
  | 'BLE_CONNECTION_ERROR'
  | 'RAW_PAYLOAD'
  | 'SCREEN_OPENED'
  | 'APP_BACKGROUNDED'
  | 'APP_FOREGROUNDED'
  | 'ERROR_CAUGHT'
  | 'PERFORMANCE_METRIC';

export interface LogEntry {
  t: number;        // timestamp ms
  e: EventType;     // event type
  d: Record<string, any>; // payload
}

class AppLoggerService {
  private buffer: LogEntry[] = [];
  private activeDevices: any[] = [];
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

  private txPayloadQueue: { hex: string, timestamp: number } | null = null;
  private pendingLogQueue: { event: EventType, payload: Record<string, any>, resolve: () => void } | null = null;

  updateKnownDevices(devices: any[]) {
    this.activeDevices = devices;
  }

  setLastTxPayload(hex: string) {
    this.txPayloadQueue = { hex, timestamp: Date.now() };
    this.flushQueues();
  }

  private flushQueues() {
    if (this.pendingLogQueue) {
      const now = Date.now();
      const tx = this.txPayloadQueue;
      // If a payload happens within 250ms of the UI event, correlate them
      const correlatedHex = (tx && Math.abs(now - tx.timestamp) < 250) ? tx.hex : undefined;
      
      const payloadMeta = correlatedHex 
        ? { ...this.pendingLogQueue.payload, txPayload: correlatedHex, txState: 'SUCCESS' } 
        : this.pendingLogQueue.payload;

      const entry: LogEntry = { t: now, e: this.pendingLogQueue.event, d: payloadMeta };
      this.buffer.push(entry);
      this.persist();
      if (__DEV__) console.log(`[AppLogger] ${this.pendingLogQueue.event}`, payloadMeta);
      
      this.pendingLogQueue.resolve();
      this.pendingLogQueue = null;
    }
  }

  async log(event: EventType, payload: Record<string, any> = {}) {
    await this.ensureLoaded();
    
    // Non-hardware events bypass correlation
    if (['APP_OPENED', 'SCAN_STARTED', 'SCAN_COMPLETED', 'DEVICE_DISCOVERED', 'DEVICE_CONNECTED', 'DEVICE_DISCONNECTED', 'PROTOCOL_ERROR'].includes(event)) {
      const entry: LogEntry = { t: Date.now(), e: event, d: payload };
      this.buffer.push(entry);
      this.persist();
      if (__DEV__) console.log(`[AppLogger] ${event}`, payload);
      return;
    }

    // Queue functional hardware commands for up to 100ms to see if a TX payload fires alongside it
    return new Promise<void>((resolve) => {
      this.pendingLogQueue = { event, payload, resolve };
      
      // If the UI state updated but no Bluetooth write occurs within 100ms, log it solo
      setTimeout(() => {
        if (this.pendingLogQueue && this.pendingLogQueue.event === event) {
          this.flushQueues();
        }
      }, 100);
    });
  }

  async getLogs(): Promise<LogEntry[]> {
    await this.ensureLoaded();
    return [...this.buffer].reverse(); // newest first
  }

  async clearLogs() {
    this.buffer = [];
    this.activeDevices = [];
    this.pendingLogQueue = null;
    await AsyncStorage.removeItem(STORAGE_KEY);
    
    if (supabase) {
      // Must compute both potential filenames since state could be paired or unpaired
      const pMac = this.activeDevices.length > 0 ? this.activeDevices[0].id : 'unpaired-host';
      const bleMac = pMac.replace(/[^a-zA-Z0-9_-]/g, '');
      
      const rawId = Device.osInternalBuildId || Device.modelId || 'unknown-device';
      const deviceId = rawId.replace(/[^a-zA-Z0-9_-]/g, '_');

      try {
        await supabase.storage.from('sk8lytz-logs').remove([`logs_${bleMac}.json`, `logs_${deviceId}.json`]);
      } catch(e) {
        console.warn('Failed cloud wipe', e);
      }
    }
  }

  async exportJSON(): Promise<string> {
    await this.ensureLoaded();
    const stats = await this.getStats();
    return JSON.stringify({
      app: 'SK8Lytz',
      hostDeviceId: Device.osInternalBuildId || Device.modelId || 'unknown-device',
      exported: new Date().toISOString(),
      count: this.buffer.length,
      stats: stats,
      devices: this.activeDevices,
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
      const deviceId = Device.osInternalBuildId || Device.modelId || 'unknown-device';
      const primaryMacRaw = this.activeDevices.length > 0 ? this.activeDevices[0].id : 'unpaired-host';
      const bleMac = primaryMacRaw.replace(/[^a-zA-Z0-9_-]/g, '');
      const filename = `logs_${bleMac}.json`;

      let mergedLogs = [...this.buffer];
      let existingStats = null;
      let existingDevices = [];

      // Attempt to fetch existing cloud file for appending
      const { data: fileData, error: dlError } = await supabase.storage.from('sk8lytz-logs').download(filename);
      
      if (!dlError && fileData) {
        try {
          // Native RN safe text extraction
          const text = await new Promise<string>((resolve, reject) => {
            const fr = new FileReader();
            fr.onload = () => resolve(fr.result as string);
            fr.onerror = reject;
            fr.readAsText(fileData);
          });
          
          const parsed = JSON.parse(text);
          if (parsed) {
            existingStats = parsed.stats || null;
            existingDevices = parsed.devices || [];

            if (Array.isArray(parsed.logs)) {
              // Deduplicate logs using timestamp + event fingerprint
              const logMap = new Map();
              parsed.logs.forEach((l: any) => logMap.set(l.t + '_' + l.e, l));
              this.buffer.forEach((l: any) => logMap.set(l.t + '_' + l.e, l));
              
              mergedLogs = Array.from(logMap.values()).sort((a: any, b: any) => a.t - b.t);
              
              // Prevent critical memory faults on mobile by capping massive files
              if (mergedLogs.length > 50000) mergedLogs = mergedLogs.slice(-50000);
            }
          }
        } catch (e) {
           console.warn('[AppLogger] Existing log parser failed, overwriting safely.');
        }
      }

      // Re-compile stats for exact current bounds
      const currentStats = await this.getStats();

      // Deduplicate devices
      const deviceMap = new Map();
      existingDevices.forEach((d: any) => deviceMap.set(d.id, d));
      this.activeDevices.forEach((d: any) => deviceMap.set(d.id, d));

      const mergedPayload = {
        app: 'SK8Lytz',
        hostDeviceId: deviceId,
        bleMac: primaryMacRaw,
        exported: new Date().toISOString(),
        count: mergedLogs.length,
        stats: { ...existingStats, ...currentStats, totalEvents: mergedLogs.length },
        devices: Array.from(deviceMap.values()),
        logs: mergedLogs,
      };

      const jsonStr = JSON.stringify(mergedPayload, null, 2);

      // Uploading as a string/Blob to the 'sk8lytz-logs' bucket, force upsert
      const { data, error } = await supabase.storage
        .from('sk8lytz-logs')
        .upload(filename, jsonStr, {
          contentType: 'application/json',
          upsert: true,
        });

      if (error) {
        console.error('[AppLogger] Failed to upload logs to Supabase:', error);
        throw new Error(error.message);
      } else {
        console.log(`[AppLogger] Successfully appended and uploaded logs: ${data.path}`);
        
        // ENTERPRISE DB INJECTION STREAM
        // Directly maps telemetry to normalized raw relational Postgres tables
        try {
          console.log('[AppLogger] Pushing native telemetry structures to Postgres Timeseries...');
          const sessionId = `telemetry_${Date.now()}`;
          const finalStats = mergedPayload.stats;
          
          // 1. Session Stats
          await supabase.from('parsed_session_stats').insert([{
            session_id: sessionId,
            device_id: primaryMacRaw,
            host_device_id: deviceId,
            timestamp_ms: mergedLogs.length > 0 ? mergedLogs[mergedLogs.length - 1].t : Date.now(),
            devices_discovered: finalStats.devicesDiscovered || 0,
            total_events: finalStats.totalEvents || 0,
            storage_bytes_estimate: finalStats.storageBytesEstimate || 0,
            average_load_time_ms: finalStats.averageLoadTimeMs || 0,
            battery_level: finalStats.batteryLevel || -1,
            is_low_power_mode: finalStats.isLowPowerMode || false
          }]);

          // 2. Devices
          if (mergedPayload.devices.length > 0) {
            const devPayload = mergedPayload.devices.map((d: any) => ({
                session_id: sessionId,
                device_id: d.id,
                timestamp_ms: Date.now(),
                host_device_id: deviceId,
                name: d.name,
                rssi: d.rssi,
                firmware_ver: d.firmwareVer ? { fw: d.firmwareVer, led: d.ledVersion, ble: d.bleVersion } : (d.manufacturerData || {}),
                ic_type: d.hardwareSettings?.icType,
                led_points: d.hardwareSettings?.ledPoints,
                segments: d.hardwareSettings?.segments,
                color_sorting: d.hardwareSettings?.colorSorting
            }));
            await supabase.from('parsed_session_devices').insert(devPayload);
          }

          // 3. New Usage Tracking Matrix
          const modePayload = [];
          for (const [mName, mCount] of Object.entries(finalStats.modeUsage || {})) {
            modePayload.push({ session_id: sessionId, device_id: primaryMacRaw, mode_name: mName, usage_count: mCount, timestamp_ms: Date.now() });
          }
          if (modePayload.length > 0) await supabase.from('parsed_mode_usage').insert(modePayload);

          const patternPayload = [];
          for (const [pName, pData] of Object.entries((finalStats.patternUsage as any) || {})) {
            patternPayload.push({ session_id: sessionId, device_id: primaryMacRaw, pattern_idx: pName, usage_count: (pData as any).count, timestamp_ms: Date.now() });
          }
          if (patternPayload.length > 0) await supabase.from('parsed_pattern_usage').insert(patternPayload);

          const colorPayload = [];
          for (const [cHex, cCount] of Object.entries(finalStats.colorUsage || {})) {
            colorPayload.push({ session_id: sessionId, device_id: primaryMacRaw, hex_color: cHex, usage_count: cCount, timestamp_ms: Date.now() });
          }
          if (colorPayload.length > 0) await supabase.from('parsed_color_usage').insert(colorPayload);

          // 4. Trace Log Push (Batched)
          const CHUNK = 1000;
          for (let i = 0; i < mergedLogs.length; i += CHUNK) {
             const chunk = mergedLogs.slice(i, i + CHUNK);
             const dbLogPayload = chunk.map((item: any) => {
                const isGroup = item.d?.target === 'group' || (item.d?.deviceIds && item.d.deviceIds.length > 1);
                return {
                  session_id: sessionId,
                  host_device_id: deviceId,
                  timestamp_ms: item.t,
                  event_type: item.e,
                  direction: item.d?.dir || null,
                  hex_payload: item.d?.hex || null,
                  device_id: item.d?.deviceId || null,
                  group_id: isGroup ? item.d?.deviceIds?.join('_') : null,
                  group_name: isGroup ? `Group (${item.d?.groupSize || item.d?.deviceIds?.length} Devices)` : null,
                  raw_data: item.d || {} 
                };
             });
             await supabase.from('parsed_logs').insert(dbLogPayload);
          }
          
          console.log('[AppLogger] Postgres Native Insertion Complete!');
        } catch (dbErr) {
          console.error('[AppLogger] Non-fatal Supabase DB ingestion failure:', dbErr);
        }
      }
    } catch (err) {
      console.error('[AppLogger] Upload exception:', err);
      throw err;
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
    primaryBleMac: string;
    batteryLevel: number;
    isLowPowerMode: boolean;
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

    const pMac = this.activeDevices.length > 0 ? this.activeDevices[0].id : 'unpaired-host';
    const bleMac = pMac.replace(/[^a-zA-Z0-9_-]/g, '');

    let batteryLevel = -1;
    let isLowPowerMode = false;
    try {
      if (await Battery.isAvailableAsync()) {
        batteryLevel = await Battery.getBatteryLevelAsync();
        isLowPowerMode = await Battery.isLowPowerModeEnabledAsync();
      }
    } catch(e) {}

    return { 
      modeUsage, patternUsage: finalPatternUsage, colorUsage, devicesDiscovered, 
      totalEvents: this.buffer.length, storageBytesEstimate, totalStorageEstimate,
      averageLoadTimeMs, lastAppOpenedTime, primaryBleMac: bleMac,
      batteryLevel, isLowPowerMode
    };
  }
}

export const AppLogger = new AppLoggerService();
