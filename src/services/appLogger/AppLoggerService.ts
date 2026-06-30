import { AppLoggerStorage } from './AppLoggerStorage';
import { AppLoggerCloud } from './AppLoggerCloud';
import { EventType, LogEntry } from './types';
import * as Battery from 'expo-battery';
import * as Device from 'expo-device';
import { TelemetryService } from '../TelemetryService';
import { FlightRecorder, Breadcrumb } from '../../utils/FlightRecorder';
import type { Device as BleDevice } from 'react-native-ble-plx';

/** Enriched device shape: react-native-ble-plx Device plus any optional fields
 * injected by displayConnectedDevices in DashboardScreen (e.g. batteryLevel). */
type KnownDevice = BleDevice & { batteryLevel?: number };

class AppLoggerService {
  private storage = new AppLoggerStorage();
  private activeDevices: KnownDevice[] = [];
  private currentUserId: string | undefined = undefined;
  private sessionId = `telemetry_${Date.now()}`;
  
  private txPayloadQueue: { hex: string, timestamp: number } | null = null;
  private pendingLogQueue: { event: EventType, payload: Record<string, unknown>, resolve: () => void } | null = null;
  private readonly throttleMap = new Map<string, number>();

  setCurrentUser(userId: string | undefined) {
    this.currentUserId = userId;
  }

  updateKnownDevices(devices: BleDevice[]) {
    this.activeDevices = devices as KnownDevice[];
  }

  private async getHostDeviceInfo(): Promise<Record<string, unknown>> {
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
    } catch(_e: unknown) {
      if (__DEV__) console.warn('[AppLogger] Failed to read battery info:', _e instanceof Error ? _e.message : String(_e));
    }

    const deviceTypeMap: Record<number, string> = {
      0: 'UNKNOWN', 1: 'PHONE', 2: 'TABLET', 3: 'DESKTOP', 4: 'TV'
    };

    return {
      brand:                Device.brand,
      manufacturer:         Device.manufacturer,
      model_name:           Device.modelName,
      model_id:             Device.modelId,
      design_name:          Device.designName,
      product_name:         Device.productName,
      device_name:          Device.deviceName,
      device_type:          deviceTypeMap[Device.deviceType ?? 0] ?? 'UNKNOWN',
      device_year_class:    Device.deviceYearClass,
      total_memory_mb:      Device.totalMemory ? Math.round(Device.totalMemory / 1024 / 1024) : null,
      is_physical_device:   Device.isDevice,
      os_name:              Device.osName,
      os_version:           Device.osVersion,
      os_build_id:          Device.osBuildId,
      os_internal_build_id: Device.osInternalBuildId,
      os_build_fingerprint: Device.osBuildFingerprint,
      platform_api_level:   Device.platformApiLevel,
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
      const correlatedHex = (tx && Math.abs(now - tx.timestamp) < 250) ? tx.hex : undefined;
      
      const payloadMeta = correlatedHex 
        ? { ...this.pendingLogQueue.payload, txPayload: correlatedHex, txState: 'SUCCESS' } 
        : this.pendingLogQueue.payload;

      const entry: LogEntry = { t: now, e: this.pendingLogQueue.event, d: payloadMeta };
      this.storage.push(entry);
      if (__DEV__) console.log(`[AppLogger] ${this.pendingLogQueue.event}`, payloadMeta);
      
      this.pendingLogQueue.resolve();
      this.pendingLogQueue = null;
    }
  }

  private formatPayload(payload: Record<string, unknown>, event?: EventType): Record<string, unknown> {
    let clean: Record<string, unknown>;
    try {
      clean = { ...(payload || {}) };
    } catch (_e: unknown) {
      return { _unparseable: true };
    }

    if (this.activeDevices && this.activeDevices.length > 0) {
      const primary = this.activeDevices.find(d => d.id === clean.deviceId || d.id === clean.device_id) || this.activeDevices[0];
      if (primary) {
        if (!clean.rssi && primary.rssi !== undefined) clean.rssi = primary.rssi;
        if (!clean.mtu && primary.mtu !== undefined) clean.mtu = primary.mtu;
        if (!clean.battery_level && primary.batteryLevel !== undefined) clean.battery_level = primary.batteryLevel;
      }
    }

    if (event && event.startsWith('BLE_')) {
      clean = TelemetryService.extractBleContext(clean);
    }

    const PII_KEY_PATTERNS = [
      'email', 'name', 'password', 'token', 'phone', 'address', 'fullname',
      'lat', 'lng', 'latitude', 'longitude', 'label',
      'auth', 'refresh', 'access', 'secret', 'credential',
      'mac', 'deviceid', 'peripheral_id',
    ];
    const seen = new WeakSet();

    const obfuscate = (obj: Record<string, unknown>) => {
      if (seen.has(obj)) return;
      seen.add(obj);
      for (const key in obj) {
        if (PII_KEY_PATTERNS.some(pattern => key.toLowerCase().includes(pattern)) && typeof obj[key] === 'string') {
          obj[key] = '[REDACTED]';
        } else if (obj[key] && typeof obj[key] === 'object') {
          if (Array.isArray(obj[key])) {
            obj[key] = (obj[key] as unknown[]).map(
              item => (item && typeof item === 'object') ? obfuscate(item as Record<string, unknown>) : item
            );
          } else {
            obfuscate(obj[key] as Record<string, unknown>);
          }
        }
      }
    };
    obfuscate(clean);

    return clean;
  }

  async log(event: EventType, rawPayload: Record<string, any> = {}) {
    const HIGH_FREQ_EVENTS: EventType[] = ['BRIGHTNESS_CHANGED', 'SPEED_CHANGED', 'COLOR_CHANGED'];
    if (HIGH_FREQ_EVENTS.includes(event)) {
      const now = Date.now();
      const lastLogged = this.throttleMap.get(event) ?? 0;
      if (now - lastLogged < 500) return;
      this.throttleMap.set(event, now);
    }
    await this.storage.ensureLoaded();
    
    const payload = this.formatPayload(rawPayload, event);

    let category: Breadcrumb['category'] = 'ACTION';
    if (event.startsWith('BLE_') || event.startsWith('DEVICE_') || event.startsWith('ZENGGE_')) category = 'BLE';
    else if (event === 'ERROR_CAUGHT' || event.includes('ERROR') || event.includes('FAIL')) category = 'ERROR';
    else if (event.includes('FETCH') || event.includes('CLOUD') || event.includes('SYNC')) category = 'NETWORK';
    else if (event.startsWith('SCREEN_') || event.includes('NAV')) category = 'NAVIGATION';
    
    FlightRecorder.leaveBreadcrumb(category, event, payload);

    const CRITICAL_EVENTS: EventType[] = ['ERROR_CAUGHT', 'PROTOCOL_ERROR', 'BLE_WRITE_ERROR', 'BLE_CONNECTION_ERROR', 'CREW_ERROR'];
    if (CRITICAL_EVENTS.includes(event)) {
      AppLoggerCloud.pushFastLaneError(event, payload, this.sessionId);
      if (__DEV__) console.log(`[AppLogger VIP] ${event}`, payload);
      this.storage.push({ t: Date.now(), e: event, d: payload });
      return; 
    }

    if (['APP_LOG', 'APP_OPENED', 'SCAN_STARTED', 'SCAN_COMPLETED', 'DEVICE_DISCOVERED', 'DEVICE_CONNECTED', 'DEVICE_DISCONNECTED', 'GLOBAL_TELEMETRY', 'AUTO_PAUSE_TOGGLED', 'SCREEN_LOAD_TTID', 'SCREEN_LOAD_TTFD'].includes(event)) {
      this.storage.push({ t: Date.now(), e: event, d: payload });
      if (__DEV__) console.log(`[AppLogger] ${event}`, payload);
      return;
    }

    return new Promise<void>((resolve) => {
      this.pendingLogQueue = { event, payload, resolve };
      setTimeout(() => {
        if (this.pendingLogQueue && this.pendingLogQueue.event === event) {
          this.flushQueues();
        }
      }, 100);
    });
  }

  debug(message: string, context?: Record<string, any>) {
    if (__DEV__) this.log('APP_LOG', { level: 'debug', message, ...context });
  }

  info(message: string, context?: Record<string, any>) {
    this.log('APP_LOG', { level: 'info', message, ...context });
  }

  warn(message: string, context?: Record<string, unknown> | unknown) {
    const safeContext = context && typeof context === 'object' && !Array.isArray(context)
      ? context as Record<string, unknown>
      : { value: String(context) };
    this.log('APP_LOG', { level: 'warn', message, ...safeContext });
  }

  error(message: string, errorObj?: unknown, context?: Record<string, unknown>) {
    const errorMessage = errorObj instanceof Error ? errorObj.message : String(errorObj);
    this.log('ERROR_CAUGHT', { 
      level: 'error', 
      message, 
      error: errorMessage,
      ...context 
    });
  }

  async getLogs(): Promise<LogEntry[]> {
    await this.storage.ensureLoaded();
    return [...this.storage.getBuffer()].reverse();
  }

  async clearLogs() {
    this.activeDevices = [];
    this.pendingLogQueue = null;
    await this.storage.clear();
    await AppLoggerCloud.clearCloudLogs(this.activeDevices);
  }

  async exportJSON(): Promise<string> {
    await this.storage.ensureLoaded();
    const stats = await this.getStats();
    // Scrub active device objects before export — BLE Device.id contains raw MAC addresses.
    const scrubbedDevices = this.activeDevices.map(d =>
      this.formatPayload(d as unknown as Record<string, unknown>)
    );
    return JSON.stringify({
      app: 'SK8Lytz',
      hostDeviceId: Device.osInternalBuildId || Device.modelId || 'unknown-device',
      exported: new Date().toISOString(),
      count: this.storage.getBuffer().length,
      stats: stats,
      devices: scrubbedDevices,
      logs: this.storage.getBuffer(),
    }, null, 2);
  }

  async uploadLogsToSupabase() {
    await this.storage.ensureLoaded();
    const currentBuffer = [...this.storage.getBuffer()];
    
    await AppLoggerCloud.uploadTelemetrySnapshots(
      currentBuffer,
      this.sessionId,
      this.currentUserId,
      (successfulCount) => {
        if (successfulCount > 0 || successfulCount === 0) {
          this.storage.setBuffer(this.storage.getBuffer().slice(successfulCount));
          this.storage.persist(true);
        }
      }
    );
  }

  async getStats() {
    await this.storage.ensureLoaded();
    const buffer = this.storage.getBuffer();
    
    const modeUsage: Record<string, number> = {};
    const patternUsage: Record<string, { count: number; colors: Set<string> }> = {};
    const colorUsage: Record<string, number> = {};
    let devicesDiscovered = 0;

    for (const entry of buffer) {
      if (entry.e === 'MODE_CHANGED' && entry.d.mode) {
        modeUsage[entry.d.mode] = (modeUsage[entry.d.mode] || 0) + 1;
      }
      if (entry.e === 'PATTERN_CHANGED' && entry.d.pattern) {
        const key = entry.d.name || entry.d.pattern;
        if (!patternUsage[key]) {
          patternUsage[key] = { count: 0, colors: new Set<string>() };
        }
        patternUsage[key].count++;
        if (entry.d.color) patternUsage[key].colors.add(entry.d.color);
      }
      if (entry.e === 'COLOR_CHANGED' && entry.d.hex) {
        colorUsage[entry.d.hex] = (colorUsage[entry.d.hex] || 0) + 1;
      }
      if (entry.e === 'DEVICE_DISCOVERED') {
        devicesDiscovered++;
      }
    }

    const { storageBytesEstimate, totalStorageEstimate } = await this.storage.getStorageStats();

    let totalLoadTime = 0;
    let loadTimeCount = 0;
    let lastAppOpenedTime = 0;

    for (const entry of buffer) {
      if (entry.e === 'APP_OPENED') {
        if (entry.t > lastAppOpenedTime) lastAppOpenedTime = entry.t;
        if (entry.d.loadTimeMs) {
          totalLoadTime += entry.d.loadTimeMs;
          loadTimeCount++;
        }
      }
    }
    const averageLoadTimeMs = loadTimeCount > 0 ? Math.round(totalLoadTime / loadTimeCount) : 0;

    const finalPatternUsage: Record<string, { count: number; colors: string[] }> = {};
    for (const [name, data] of Object.entries(patternUsage)) {
      finalPatternUsage[name] = { 
        count: data.count, 
        colors: Array.from(data.colors) 
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
    } catch(e: unknown) {
      if (__DEV__) console.warn('[AppLogger] Failed to read battery info (getStats):', e instanceof Error ? e.message : String(e));
    }

    return { 
      modeUsage, patternUsage: finalPatternUsage, colorUsage, devicesDiscovered, 
      totalEvents: buffer.length, storageBytesEstimate, totalStorageEstimate,
      averageLoadTimeMs, lastAppOpenedTime, primaryBleMac: bleMac,
      batteryLevel, isLowPowerMode
    };
  }
}

export { AppLoggerService };
