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
 *  - Deduplication: group-targeted events (deviceIds[]) insert a single row
 *    with device_id as null, instead of unrolling.
 *  - Custom device names: resolved from '@Sk8lytz_device_configs' AsyncStorage key
 *    (same key DashboardScreen writes) and injected into all DB payloads.
 *  - VIP Error Fast-Lane: Critical crash/error events bypass the normal buffers
 *    and are immediately pushed to `telemetry_errors`.
 *
 * Supabase tables written:
 *  telemetry_snapshots, telemetry_errors
 *
 * Event types: see EventType union below
 * Platform: React Native (Android + Web)
 */
import AsyncStorage from '@react-native-async-storage/async-storage';
import * as Battery from 'expo-battery';
import * as Device from 'expo-device';
import { supabase } from './supabaseClient';

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
  | 'BLE_QUEUE_REPLAY'
  | 'RAW_PAYLOAD'
  // ── HAL / Protocol Debug ────────────────────────────────
  | 'ZENGGE_MUSIC_CONFIG'
  | 'ZENGGE_CUSTOM_MODE_COMPACT'
  | 'SCREEN_OPENED'
  | 'APP_BACKGROUNDED'
  | 'APP_FOREGROUNDED'
  | 'ERROR_CAUGHT'
  | 'PERFORMANCE_METRIC'
  | 'AUTO_RECOVERY_STARTED'
  | 'AUTO_RECOVERY_SUCCESS'
  | 'AUTO_RECOVERY_FAILED'
  | 'MOUNT'
  | 'UNMOUNT'
  | 'SYNC'
  | 'REJOIN'
  | 'FTUE'
  | 'GLOBAL_MODAL_MOUNTED'
  | 'EULA_ACCEPTED'
  | 'PERMISSION_OPT_IN'
  | 'PERMISSION_OPT_OUT'
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
  | 'ACCOUNT_MODAL_LOAD_START'
  | 'ACCOUNT_MODAL_DATA_RESOLVED'
  // ── Audio ─────────────────────────────────────────
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
  | 'APP_LOG'
  // ── Hardware Watchdog ──────────────────────────────────────
  | 'BLE_STATE_CHANGE'
  | 'WATCHDOG_MISS'
  | 'WATCHDOG_RELATCH'
  // ── Auto-Recovery Extended ─────────────────────────────────
  | 'AUTO_RECOVERY_CANCELLED'
  | 'AUTO_RECOVERY_GATE_WAIT'
  // ── Telemetry Hardening ─────────────────────────────────────────
  | 'SCREEN_ERROR'
  | 'PROMISE_REJECTION'
  // ── Music Mode ─────────────────────────────────────────────────────────
  | 'MUSIC_MODE_EXIT';



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
      if (__DEV__) console.warn('[AppLogger] persist failed', e);
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

    // VIP Fast-Lane: Critical errors bypass the queue and physical storage to guarantee delivery
    const CRITICAL_EVENTS: EventType[] = ['ERROR_CAUGHT', 'PROTOCOL_ERROR', 'BLE_WRITE_ERROR', 'BLE_CONNECTION_ERROR', 'CREW_ERROR'];
    if (CRITICAL_EVENTS.includes(event)) {
      if (supabase) {
        supabase.from('telemetry_errors').insert({
          session_id: this.sessionId,
          event_type: event,
          error_message: String(payload.message || payload.error || payload.errorMessage || JSON.stringify(payload) || 'Unknown error').substring(0, 500),
          stack_trace: payload.stack || payload.stackTrace || null,
          raw_context: {
            ...payload,
            host_device_id: Device.osInternalBuildId || Device.modelId || 'unknown'
          }
        }).then(({ error }: any) => {
          if (error) console.warn('[AppLogger] VIP Fast-Lane failed:', error.message);
        });
      }
      if (__DEV__) console.log(`[AppLogger VIP] ${event}`, payload);
      return; 
    }

    // Non-hardware events bypass correlation
    if (['APP_LOG', 'APP_OPENED', 'SCAN_STARTED', 'SCAN_COMPLETED', 'DEVICE_DISCOVERED', 'DEVICE_CONNECTED', 'DEVICE_DISCONNECTED'].includes(event)) {
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
        if (__DEV__) console.warn('Failed cloud wipe', e);
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
      if (__DEV__) console.log('[AppLogger] Supabase client not configured. Skipping snapshot ingestion.');
      return;
    }
    await this.ensureLoaded();
    if (this.buffer.length === 0) return;

    try {
      const deviceId = Device.osInternalBuildId || Device.modelId || 'unknown';
      const sessionId = this.sessionId;
      
      const currentRunLogs = [...this.buffer];

      if (__DEV__) console.log(`[AppLogger] Pushing ${currentRunLogs.length} events to telemetry_snapshots...`);

      const CHUNK = 500;
      for (let i = 0; i < currentRunLogs.length; i += CHUNK) {
        const chunk = currentRunLogs.slice(i, i + CHUNK);
        const dbPayload = chunk.map((item: any) => {
          const isGroup = item.d?.target === 'group' || (item.d?.deviceIds && item.d.deviceIds.length > 1);
          const tgtDev = isGroup ? null : (item.d?.deviceId || null);

          return {
             session_id: sessionId,
             device_id: tgtDev,
             event_type: item.e,
             metadata: {
               timestamp_ms: item.t,
               host_device_id: deviceId,
               ...item.d
             }
          };
        });

        const { error } = await supabase.from('telemetry_snapshots').insert(dbPayload);
        if (error) {
           // Silently swallow RLS failures on Web to prevent console pollution.
           // Data will continue to rotate locally until the DB permissions are fixed.
        }
      }

      this.buffer = [];
      await AsyncStorage.setItem(STORAGE_KEY, JSON.stringify([]));
      if (__DEV__) console.log('[AppLogger] Ingestion complete. Local buffer cleared.');
      
    } catch (err: any) {
      if (__DEV__) console.warn('[AppLogger] Ingestion exception (safely caught):', err?.message || String(err));
      // Swallow error to preserve UI stability on connection drops
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

