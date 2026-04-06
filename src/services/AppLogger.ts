/**
 * AppLogger.ts — SK8Lytz Telemetry & Analytics Engine
 *
 * Singleton service that captures all significant app events and uploads
 * them to Supabase for remote diagnostics and usage analytics.
 *
 * Architecture:
 *  - Local buffer: compact LogEntry[] ({ t, e, d }) stored in AsyncStorage
 *    under '@sk8lytz_logs'. Rotates at MAX_ENTRIES (10,000) to cap storage.
 *  - Session ID: generated once at instantiation (telemetry_TIMESTAMP),
 *    stable for the entire app lifetime to prevent duplicate Supabase rows.
 *  - uploadLogsToSupabase(): merges with cloud Storage JSON, deduplicates,
 *    pushes current delta to 6 normalized Postgres tables, then CLEARS the
 *    local buffer (rotation) on confirmed success.
 *  - Group event unrolling: group-targeted events (deviceIds[]) are flatMapped
 *    into individual per-device rows in Supabase, enabling per-device diagnostics.
 *  - Custom device names: resolved from 'ng_device_configs' AsyncStorage key
 *    (same key DashboardScreen writes) and injected into all DB payloads.
 *  - RAW_PAYLOAD events are intentionally EXCLUDED from Supabase DB writes
 *    (hardware tester sniffer data only — kept in Storage/local only).
 *
 * Supabase tables written:
 *  parsed_session_stats, parsed_session_devices, parsed_logs,
 *  parsed_mode_usage, parsed_pattern_usage, parsed_color_usage
 *
 * Event types: see EventType union below
 * Platform: React Native (Android + Web)
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
  | 'DEVICE_RENAMED'
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
  | 'PERFORMANCE_METRIC'
  // ── Crew Sync ─────────────────────────────────────────────
  | 'CREW_SESSION_CREATED'
  | 'CREW_SESSION_JOINED'
  | 'CREW_SESSION_LEFT'
  | 'CREW_SESSION_ENDED'
  | 'CREW_SESSION_SHARED'
  | 'CREW_LEADERSHIP_TRANSFERRED'
  | 'CREW_SCENE_BROADCAST'
  | 'CREW_SCENE_RECEIVED'
  | 'CREW_AUTO_REJOINED'
  | 'CREW_ERROR'
  // ── Street Mode ───────────────────────────────────────────
  | 'STREET_MODE_ACTIVATED'
  | 'STREET_MODE_DEACTIVATED'
  | 'STREET_JERK_DETECTED'
  | 'STREET_SENSITIVITY_CHANGED'
  // ── Push Notifications ────────────────────────────────────
  | 'PUSH_TOKEN_REGISTERED'
  | 'PUSH_NOTIFICATION_TAPPED'
  | 'PUSH_NOTIFICATION_SENT'
  // ── Profile & Crew Management ─────────────────────────────
  | 'PROFILE_UPDATED'
  | 'CREW_PERMANENT_CREATED'
  | 'CREW_PERMANENT_JOINED'
  | 'CREW_PERMANENT_LEFT'
  | 'PUSH_TOKEN_UNREGISTERED';

export interface LogEntry {
  t: number;        // timestamp ms
  e: EventType;     // event type
  d: Record<string, any>; // payload
}

class AppLoggerService {
  private buffer: LogEntry[] = [];
  private activeDevices: any[] = [];
  private loaded = false;
  private sessionId = `telemetry_${Date.now()}`;

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

  /** Collect ALL available host device + power data from expo-device and expo-battery */
  private async getHostDeviceInfo(): Promise<Record<string, any>> {
    let batteryLevel = -1;
    let batteryState = 'UNKNOWN';
    let isLowPowerMode = false;

    try {
      if (await Battery.isAvailableAsync()) {
        batteryLevel = await Battery.getBatteryLevelAsync();
        isLowPowerMode = await Battery.isLowPowerModeEnabledAsync();
        const stateEnum = await Battery.getBatteryStateAsync();
        const stateMap: Record<number, string> = {
          0: 'UNKNOWN', 1: 'UNPLUGGED', 2: 'CHARGING', 3: 'FULL'
        };
        batteryState = stateMap[stateEnum] ?? 'UNKNOWN';
      }
    } catch(_e) {}

    const deviceTypeMap: Record<number, string> = {
      0: 'UNKNOWN', 1: 'PHONE', 2: 'TABLET', 3: 'DESKTOP', 4: 'TV'
    };

    return {
      // Identity
      brand:                Device.brand,
      manufacturer:         Device.manufacturer,
      model_name:           Device.modelName,
      model_id:             Device.modelId,
      design_name:          Device.designName,
      product_name:         Device.productName,
      device_name:          Device.deviceName,
      // Hardware profile
      device_type:          deviceTypeMap[Device.deviceType ?? 0] ?? 'UNKNOWN',
      device_year_class:    Device.deviceYearClass,
      total_memory_mb:      Device.totalMemory ? Math.round(Device.totalMemory / 1024 / 1024) : null,
      is_physical_device:   Device.isDevice,
      // OS
      os_name:              Device.osName,
      os_version:           Device.osVersion,
      os_build_id:          Device.osBuildId,
      os_internal_build_id: Device.osInternalBuildId,
      os_build_fingerprint: Device.osBuildFingerprint,
      platform_api_level:   Device.platformApiLevel,
      // Power
      battery_level:        batteryLevel,
      battery_state:        batteryState,
      is_low_power_mode:    isLowPowerMode,
    };
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
              parsed.logs.forEach((l: any) => {
                  if (l.e !== 'RAW_PAYLOAD') logMap.set(l.t + '_' + l.e, l);
              });
              this.buffer.forEach((l: any) => {
                  if (l.e !== 'RAW_PAYLOAD') logMap.set(l.t + '_' + l.e, l);
              });
              
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
        // INSERTS ONLY THE CURRENT DELTA BUFFER - Prevents doubling up historical merged logs
        try {
          console.log('[AppLogger] Pushing native telemetry structures to Postgres Timeseries...');
          const sessionId = this.sessionId;
          const currentRunLogs = [...this.buffer].filter(l => l.e !== 'RAW_PAYLOAD');
          
          // 1. Session Stats — full host device profile + session metrics
          const hostInfo = await this.getHostDeviceInfo();
          await supabase.from('parsed_session_stats').upsert([{
            session_id: sessionId,
            device_id: primaryMacRaw,
            host_device_id: deviceId,
            timestamp_ms: currentRunLogs.length > 0 ? currentRunLogs[currentRunLogs.length - 1].t : Date.now(),
            devices_discovered: currentStats.devicesDiscovered || 0,
            total_events: currentRunLogs.length || 0,
            storage_bytes_estimate: currentStats.storageBytesEstimate || 0,
            total_storage_estimate: currentStats.totalStorageEstimate || 0,
            average_load_time_ms: currentStats.averageLoadTimeMs || 0,
            last_app_opened_time: currentStats.lastAppOpenedTime || 0,
            primary_ble_mac: currentStats.primaryBleMac || primaryMacRaw,
            mode_usage: currentStats.modeUsage || {},
            color_usage: currentStats.colorUsage || {},
            // ── Host Device Identity ──────────────────────────────────────
            device_brand:         hostInfo.brand,
            device_model:         hostInfo.model_name,
            device_manufacturer:  hostInfo.manufacturer,
            device_model_id:      hostInfo.model_id,
            device_name:          hostInfo.device_name,
            device_type:          hostInfo.device_type,
            device_year_class:    hostInfo.device_year_class,
            is_physical_device:   hostInfo.is_physical_device,
            // ── OS ───────────────────────────────────────────────────────
            os_name:              hostInfo.os_name,
            os_version:           hostInfo.os_version,
            os_build_id:          hostInfo.os_build_id,
            platform_api_level:   hostInfo.platform_api_level,
            // ── Hardware ─────────────────────────────────────────────────
            total_memory_mb:      hostInfo.total_memory_mb,
            // ── Power ────────────────────────────────────────────────────
            battery_level:        hostInfo.battery_level,
            battery_state:        hostInfo.battery_state,
            is_low_power_mode:    hostInfo.is_low_power_mode,
            // ── Full blob (all fields including rare/platform-specific) ──
            host_device_info:     hostInfo,
          }], { onConflict: 'session_id' });

          // Extract Custom Hardware Names from ng_device_configs (keyed by device MAC)
          const customNameMap = new Map<string, string>();
          try {
            const stored = await AsyncStorage.getItem('ng_device_configs');
            if (stored) {
              const configs = JSON.parse(stored);
              for (const d of this.activeDevices) {
                const cfg = configs[d.id];
                if (cfg?.name) customNameMap.set(d.id, cfg.name);
              }
            }
          } catch(e) {
            console.warn('[AppLogger] Failed to read device configs for name resolution', e);
          }

          // 2. Devices
          if (this.activeDevices.length > 0) {
            const devPayload = this.activeDevices.map((d: any) => ({
                session_id: sessionId,
                device_id: d.id,
                timestamp_ms: Date.now(),
                host_device_id: deviceId,
                name: customNameMap.get(d.id) || d.name,
                rssi: d.rssi,
                firmware_ver: d.firmwareVer ? { fw: d.firmwareVer, led: d.ledVersion, ble: d.bleVersion } : (d.manufacturerData || {}),
                ic_type: d.hardwareSettings?.icType,
                led_points: d.hardwareSettings?.ledPoints,
                segments: d.hardwareSettings?.segments,
                color_sorting: d.hardwareSettings?.colorSorting
            }));
            await supabase.from('parsed_session_devices').upsert(devPayload, { onConflict: 'session_id, device_id' });
          }

          // 3. New Usage Tracking Matrix directly from current logs
          const modeMap = new Map();
          const patternMap = new Map();
          const colorMap = new Map();
          
          currentRunLogs.forEach((item: any) => {
              const isGroup = item.d?.target === 'group' || (item.d?.deviceIds && item.d.deviceIds.length > 1);
              const groupId = isGroup ? item.d?.deviceIds?.join('_') : null;
              const groupName = isGroup ? `Group (${item.d?.groupSize || item.d?.deviceIds?.length} Devices)` : null;
              const targets = isGroup && Array.isArray(item.d?.deviceIds) ? item.d.deviceIds : [item.d?.deviceId || primaryMacRaw];
              
              targets.forEach((tgtDev: string) => {
                  if (item.e === 'MODE_CHANGED' && item.d?.mode) {
                      const hash = `${groupId || tgtDev}|${item.d.mode}`;
                      modeMap.set(hash, { mName: item.d.mode, count: (modeMap.get(hash)?.count || 0) + 1, gId: groupId, gName: groupName, tgtDev });
                  }
                  if (item.e === 'PATTERN_CHANGED' && item.d?.pattern) {
                      const pName = item.d.name || item.d.pattern;
                      const hash = `${groupId || tgtDev}|${pName}`;
                      patternMap.set(hash, { pName, count: (patternMap.get(hash)?.count || 0) + 1, gId: groupId, gName: groupName, tgtDev });
                  }
                  if (item.e === 'COLOR_CHANGED' && item.d?.hex) {
                      const hash = `${groupId || tgtDev}|${item.d.hex}`;
                      colorMap.set(hash, { hex: item.d.hex, count: (colorMap.get(hash)?.count || 0) + 1, gId: groupId, gName: groupName, tgtDev });
                  }
              });
          });

          const modePayload = Array.from(modeMap.values()).map(v => ({ session_id: sessionId, device_id: v.tgtDev, device_name: customNameMap.get(v.tgtDev), mode_name: v.mName, usage_count: v.count, group_id: v.gId, group_name: v.gName, timestamp_ms: Date.now() }));
          if (modePayload.length > 0) await supabase.from('parsed_mode_usage').insert(modePayload);

          const patternPayload = Array.from(patternMap.values()).map(v => ({ session_id: sessionId, device_id: v.tgtDev, device_name: customNameMap.get(v.tgtDev), pattern_idx: v.pName, usage_count: v.count, group_id: v.gId, group_name: v.gName, timestamp_ms: Date.now() }));
          if (patternPayload.length > 0) await supabase.from('parsed_pattern_usage').insert(patternPayload);

          const colorPayload = Array.from(colorMap.values()).map(v => ({ session_id: sessionId, device_id: v.tgtDev, device_name: customNameMap.get(v.tgtDev), hex_color: v.hex, usage_count: v.count, group_id: v.gId, group_name: v.gName, timestamp_ms: Date.now() }));
          if (colorPayload.length > 0) await supabase.from('parsed_color_usage').insert(colorPayload);

          // 4. Trace Log Push (Batched)
          const CHUNK = 1000;
          for (let i = 0; i < currentRunLogs.length; i += CHUNK) {
             const chunk = currentRunLogs.slice(i, i + CHUNK);
             const dbLogPayload = chunk.flatMap((item: any) => {
                const isGroup = item.d?.target === 'group' || (item.d?.deviceIds && item.d.deviceIds.length > 1);
                const targets = isGroup && Array.isArray(item.d?.deviceIds) ? item.d.deviceIds : [item.d?.deviceId || primaryMacRaw];
                
                return targets.map((explicitDeviceId: string) => ({
                  session_id: sessionId,
                  host_device_id: deviceId,
                  timestamp_ms: item.t,
                  event_type: item.e,
                  direction: item.d?.dir || null,
                  hex_payload: item.d?.hex || null,
                  device_id: explicitDeviceId,
                  device_name: customNameMap.get(explicitDeviceId) || null,
                  group_id: isGroup ? item.d?.deviceIds?.join('_') : null,
                  group_name: isGroup ? `Group (${item.d?.groupSize || item.d?.deviceIds?.length} Devices)` : null,
                  raw_data: item.d || {} 
                }));
             });
             await supabase.from('parsed_logs').insert(dbLogPayload);
          }
          
          console.log('[AppLogger] Postgres Native Insertion Complete!');

          // Buffer rotation: clear local log buffer after a confirmed clean push
          // This prevents old events from re-uploading on subsequent calls
          this.buffer = [];
          await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify([]));
          console.log('[AppLogger] Local buffer cleared after successful push.');
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
