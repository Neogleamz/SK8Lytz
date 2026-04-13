/**
 * AppLogger.ts — SK8Lytz Telemetry & Analytics Engine
 *
 * Singleton service that captures all significant app events and uploads
 * them to Supabase for remote diagnostics and usage analytics.
 *
 * Architecture:
 *  - Local buffer: compact LogEntry[] ({ t, e, d }) stored in AsyncStorage
 *    under '@Sk8lytz_logs'. Rotates at MAX_ENTRIES (500) to cap storage.
 *  - Session ID: generated once at instantiation (telemetry_TIMESTAMP),
 *    stable for the entire app lifetime to prevent duplicate Supabase rows.
 *  - uploadLogsToSupabase(): merges with cloud Storage JSON, deduplicates,
 *    pushes current delta to 6 normalized Postgres tables, then CLEARS the
 *    local buffer (rotation) on confirmed success.
 *  - Group event unrolling: group-targeted events (deviceIds[]) are flatMapped
 *    into individual per-device rows in Supabase, enabling per-device diagnostics.
 *  - Custom device names: resolved from 'ng_device_configs' AsyncStorage key
 *    (same key DashboardScreen writes) and injected into all DB payloads.
 *  - RAW_PAYLOAD events (Diagnostic TX Payloads) are explicitly EXCLUDED from general 
 *    parsed_logs to prevent noise, but are fast-tracked into the `led_diagnostics` table.
 *
 * Supabase tables written:
 *  parsed_session_stats, parsed_session_devices, parsed_logs,
 *  parsed_mode_usage, parsed_pattern_usage, parsed_color_usage, led_diagnostics
 *
 * Event types: see EventType union below
 * Platform: React Native (Android + Web)
 */
import AsyncStorage from '@react-native-async-storage/async-storage';
import { supabase } from './supabaseClient';
import * as Device from 'expo-device';
import * as Battery from 'expo-battery';

const STORAGE_KEY = '@Sk8lytz_logs';         // canonical casing — matches @Sk8lytz_ convention
const LEGACY_KEY  = '@sk8lytz_logs';          // old lowercase key — migrated on first load
const MAX_ENTRIES = 500; // ~1MB of compact log data before rotation

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
  | 'CREW_SESSION_CREATED'
  | 'CREW_SESSION_JOINED'
  | 'CREW_SESSION_LEFT'
  | 'CREW_LEFT'
  | 'CREW_DELETED'
  | 'CREW_ERROR'
  | 'PROTOCOL_ERROR'
  | 'BLE_WRITE_ERROR'
  | 'BLE_CONNECTION_ERROR'
  | 'RAW_PAYLOAD'
  | 'SCREEN_OPENED'
  | 'APP_BACKGROUNDED'
  | 'APP_FOREGROUNDED'
  | 'ERROR_CAUGHT'
  | 'PERFORMANCE_METRIC'
  | 'MOUNT'
  | 'UNMOUNT'
  | 'SYNC'
  | 'REJOIN'
  | 'FTUE'
  | 'CREW_PERMANENT_DELETED'
  // ── Crew Extended Events (unique — SESSION_CREATED/JOINED/LEFT/ERROR declared above) ──
  | 'CREW_SESSION_SCHEDULED'
  | 'CREW_SESSION_ENDED'
  | 'CREW_DISCOVERY_FETCH'
  | 'CREW_SESSION_SHARED'
  | 'CREW_LEADERSHIP_TRANSFERRED'
  | 'CREW_SCENE_BROADCAST'
  | 'CREW_SCENE_RECEIVED'
  | 'CREW_AUTO_REJOINED'
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
  | 'CREW_PERMANENT_UPDATED'
  | 'CREW_MEMBERS_ADDED'
  | 'PUSH_TOKEN_UNREGISTERED'
  // ── Speed Tracking ────────────────────────────────────────
  | 'SESSION_SAVED'
  | 'SPEED_REACTIVE_ENABLED'
  | 'SPEED_REACTIVE_DISABLED'
  // ── Voice & Audio ─────────────────────────────────────────
  | 'VOICE_RESULT'
  | 'VOICE_ERROR'
  | 'VOICE_SPATIAL_APPLIED'
  | 'MUSIC_CONFIG_REQUESTED'
  // ── Builder & Favorites ───────────────────────────────────
  | 'BUILDER_PRESET_SAVED'
  | 'BUILDER_PRESET_DELETED'
  | 'BUILDER_PRESET_LOADED'
  | 'BUILDER_UI_TOGGLED'
  | 'FAVORITE_LOADED'
  | 'FAVORITE_RENDERED'
  | 'PICK_LOADED'
  | 'PICK_SELECTED'
  | 'MIC_SENSITIVITY_CHANGED'
  // ── Map/Locations ─────────────────────────────────────────
  | 'ERROR'
  // ── Telemetry & System ────────────────────────────────────
  | 'APP_LOG';


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
      let raw = await AsyncStorage.getItem(STORAGE_KEY);
      // \u2500\u2500 One-time migration from legacy lowercase key \u2500\u2500
      if (!raw) {
        const legacy = await AsyncStorage.getItem(LEGACY_KEY);
        if (legacy) {
          await AsyncStorage.setItem(STORAGE_KEY, legacy);
          await AsyncStorage.removeItem(LEGACY_KEY);
          raw = legacy;
        }
      }
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
  /** Last-logged timestamp per high-frequency event — prevents slider drag from flooding the buffer */
  private readonly throttleMap = new Map<string, number>();

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

  // ── Black Box Standard: Structured Payload Formatting ──
  private formatPayload(payload: Record<string, any>): Record<string, any> {
    // 1. JSON enforcement - strip non-serializable objects (functions, symbols)
    let clean = {};
    try {
      clean = JSON.parse(JSON.stringify(payload || {}));
    } catch {
      clean = { _unparseable: true };
    }

    // 2. Hardware Injection
    if (this.activeDevices && this.activeDevices.length > 0) {
      const primary = this.activeDevices.find(d => d.id === (clean as any).deviceId || d.id === (clean as any).device_id) || this.activeDevices[0];
      if (primary) {
        if (!(clean as any).rssi && primary.rssi !== undefined) (clean as any).rssi = primary.rssi;
        if (!(clean as any).mtu && primary.mtu !== undefined) (clean as any).mtu = primary.mtu;
        if (!(clean as any).battery_level && primary.batteryLevel !== undefined) (clean as any).battery_level = primary.batteryLevel;
      }
    }

    // 3. PII Scrubbing
    const piiKeys = ['email', 'name', 'password', 'token', 'phone', 'address', 'fullname'];
    const obfuscate = (obj: any) => {
      if (typeof obj !== 'object' || obj === null) return;
      for (const key in obj) {
        if (piiKeys.includes(key.toLowerCase()) && typeof obj[key] === 'string') {
          obj[key] = '[REDACTED]';
        } else if (typeof obj[key] === 'object') {
          obfuscate(obj[key]);
        }
      }
    };
    obfuscate(clean);

    return clean;
  }

  async log(event: EventType, rawPayload: Record<string, any> = {}) {
    // Throttle high-frequency slider events — max 1 entry per 500ms to prevent buffer bloat
    const HIGH_FREQ_EVENTS: EventType[] = ['BRIGHTNESS_CHANGED', 'SPEED_CHANGED', 'COLOR_CHANGED'];
    if (HIGH_FREQ_EVENTS.includes(event)) {
      const now = Date.now();
      const lastLogged = this.throttleMap.get(event) ?? 0;
      if (now - lastLogged < 500) return;
      this.throttleMap.set(event, now);
    }
    await this.ensureLoaded();
    
    // Apply Black Box Standardization early
    const payload = this.formatPayload(rawPayload);

    // Non-hardware events bypass correlation
    if (['APP_LOG', 'APP_OPENED', 'SCAN_STARTED', 'SCAN_COMPLETED', 'DEVICE_DISCOVERED', 'DEVICE_CONNECTED', 'DEVICE_DISCONNECTED', 'PROTOCOL_ERROR'].includes(event)) {
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

  // ── Black Box Standard: Semantic Log Levels ──
  
  debug(message: string, context?: Record<string, any>) {
    if (__DEV__) this.log('APP_LOG', { level: 'debug', message, ...context });
  }

  info(message: string, context?: Record<string, any>) {
    this.log('APP_LOG', { level: 'info', message, ...context });
  }

  warn(message: string, context?: Record<string, any> | any) {
    const safeContext = context && typeof context === 'object' && !Array.isArray(context)
      ? context as Record<string, any>
      : { value: String(context) };
    this.log('APP_LOG', { level: 'warn', message, ...safeContext });
  }

  error(message: string, errorObj?: any, context?: Record<string, any>) {
    this.log('ERROR_CAUGHT', { 
      level: 'error', 
      message, 
      error: errorObj?.message || String(errorObj),
      ...context 
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
            const stored = await AsyncStorage.getItem('@Sk8lytz_device_configs');
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
          const modeMap = new Map<string, any>();
          const patternMap = new Map<string, any>();
          const colorMap = new Map<string, any>();
          
          currentRunLogs.forEach((item: any) => {
              const isGroup = item.d?.target === 'group' || (item.d?.deviceIds && item.d.deviceIds.length > 1);
              const groupId = isGroup ? item.d?.deviceIds?.join('_') : null;
              const groupName = isGroup ? `Group (${item.d?.groupSize || item.d?.deviceIds?.length} Devices)` : null;
              // Optimization: Unrolling targets is a major redundancy. Store only primary or null mapping.
              const tgtDev = isGroup ? null : (item.d?.deviceId || primaryMacRaw);
              
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

          const modePayload = Array.from(modeMap.values()).map(v => ({ session_id: sessionId, device_id: v.tgtDev, device_name: v.tgtDev ? customNameMap.get(v.tgtDev) : null, mode_name: v.mName, usage_count: v.count, group_id: v.gId, group_name: v.gName, timestamp_ms: Date.now() }));
          if (modePayload.length > 0) await supabase.from('parsed_mode_usage').insert(modePayload);

          const patternPayload = Array.from(patternMap.values()).map(v => ({ session_id: sessionId, device_id: v.tgtDev, device_name: v.tgtDev ? customNameMap.get(v.tgtDev) : null, pattern_idx: v.pName, usage_count: v.count, group_id: v.gId, group_name: v.gName, timestamp_ms: Date.now() }));
          if (patternPayload.length > 0) await supabase.from('parsed_pattern_usage').insert(patternPayload);

          const colorPayload = Array.from(colorMap.values()).map(v => ({ session_id: sessionId, device_id: v.tgtDev, device_name: v.tgtDev ? customNameMap.get(v.tgtDev) : null, hex_color: v.hex, usage_count: v.count, group_id: v.gId, group_name: v.gName, timestamp_ms: Date.now() }));
          if (colorPayload.length > 0) await supabase.from('parsed_color_usage').insert(colorPayload);

          // 4. Trace Log Push (Batched)
          const CHUNK = 1000;
          for (let i = 0; i < currentRunLogs.length; i += CHUNK) {
             const chunk = currentRunLogs.slice(i, i + CHUNK);
             const dbLogPayload = chunk.map((item: any) => {
                const isGroup = item.d?.target === 'group' || (item.d?.deviceIds && item.d.deviceIds.length > 1);
                const tgtDev = isGroup ? null : (item.d?.deviceId || primaryMacRaw);
                
                return {
                  session_id: sessionId,
                  host_device_id: deviceId,
                  timestamp_ms: item.t,
                  event_type: item.e,
                  direction: item.d?.dir || null,
                  hex_payload: item.d?.hex || null,
                  device_id: tgtDev,
                  device_name: tgtDev ? customNameMap.get(tgtDev) || null : null,
                  group_id: isGroup ? item.d?.deviceIds?.join('_') : null,
                  group_name: isGroup ? `Group (${item.d?.groupSize || item.d?.deviceIds?.length} Devices)` : null,
                  raw_data: (({ dir: _d, hex: _h, deviceId: _di, ...r }) => r)(item.d || {})
                };
             });
             await supabase.from('parsed_logs').insert(dbLogPayload);
          }
          
          // 5. Critical Error Telemetry
          const CRITICAL_EVENTS = ['ERROR_CAUGHT', 'PROTOCOL_ERROR', 'BLE_WRITE_ERROR', 'BLE_CONNECTION_ERROR', 'CREW_ERROR'];
          const errorLogs = currentRunLogs.filter((item: any) => CRITICAL_EVENTS.includes(item.e));
          if (errorLogs.length > 0) {
              const errorPayload = errorLogs.map((item: any) => ({
                  session_id: sessionId,
                  event_type: item.e,
                  error_message: String(item.d?.message || item.d?.error || item.d?.errorMessage || JSON.stringify(item.d) || 'Unknown error').substring(0, 500),
                  stack_trace: item.d?.stack || item.d?.stackTrace || null,
                  raw_context: {
                      ...item.d,
                      host_device_id: deviceId,
                      target_device_id: item.d?.deviceId || primaryMacRaw,
                      os_name: hostInfo.os_name,
                      os_version: hostInfo.os_version
                  }
              }));
              await supabase.from('telemetry_errors').insert(errorPayload);
              console.log(`[AppLogger] Pushed ${errorPayload.length} critical errors to telemetry tracker.`);
          }

          // 6. LED Diagnostics Fast-Track
          const labLogs = [...this.buffer].filter((l: any) => l.e === 'RAW_PAYLOAD' && l.d?.dir === 'TX');
          if (labLogs.length > 0) {
              const diagPayload = labLogs.map((item: any) => ({
                  device_id: item.d?.deviceId || primaryMacRaw,
                  payload_hex: item.d?.hex,
                  protocol: item.d?.hex ? '0x' + item.d.hex.substring(0, 2) : 'UNKNOWN',
                  test_label: item.d?.note || 'Diagnostic Lab TX',
                  session_notes: sessionId
              }));
              await supabase.from('led_diagnostics').insert(diagPayload);
              console.log(`[AppLogger] Pushed ${diagPayload.length} diagnostic payloads to telemetry track.`);
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

