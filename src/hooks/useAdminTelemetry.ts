import { useState, useCallback, useEffect } from 'react';
import { Alert, Share } from 'react-native';
import { AppLogger, LogEntry, EventType } from '../services/AppLogger';

export interface TelemetryStats {
  totalEvents: number;
  devicesDiscovered: number;
  modeUsage: Record<string, number>;
  patternUsage: Record<string, { count: number; colors: string[] }>;
  colorUsage: Record<string, number>;
  averageLoadTimeMs?: number;
  lastAppOpenedTime?: number;
  batteryLevel?: number;
  isLowPowerMode?: boolean;
  primaryBleMac?: string;
  storageBytesEstimate: number;
  totalStorageEstimate: number;
}

export const EVENT_META: Partial<Record<EventType, { icon: string; color: string; label: string }>> = {
  APP_OPENED:         { icon: 'cellphone-check', color: '#00f0ff', label: 'App Opened' },
  SCAN_STARTED:       { icon: 'radar',           color: '#FF7000', label: 'Scan Started' },
  SCAN_COMPLETED:     { icon: 'check-circle',    color: '#00ff80', label: 'Scan Completed' },
  SCAN_FILTER_MATCH:  { icon: 'filter-check',    color: '#00f0ff', label: 'Filter Match' },
  SCAN_FILTER_REJECT: { icon: 'filter-remove',   color: '#ff4040', label: 'Filter Reject' },
  DEVICE_DISCOVERED:  { icon: 'bluetooth-connect', color: '#9D4EFF', label: 'Device Found' },
  DEVICE_CONNECTED:   { icon: 'link-variant',    color: '#00ff80', label: 'Connected' },
  DEVICE_DISCONNECTED:{ icon: 'link-variant-off',color: '#ff4040', label: 'Disconnected' },
  DEVICE_RENAMED:     { icon: 'pencil-circle',   color: '#FFA500', label: 'Device Renamed' },
  MODE_CHANGED:       { icon: 'tune',            color: '#FFD700', label: 'Mode Changed' },
  PATTERN_CHANGED:    { icon: 'shape',           color: '#FF69B4', label: 'Pattern Changed' },
  COLOR_CHANGED:      { icon: 'palette',         color: '#FF7000', label: 'Color Changed' },
  BRIGHTNESS_CHANGED: { icon: 'brightness-5',   color: '#AAFFAA', label: 'Brightness' },
  SPEED_CHANGED:      { icon: 'speedometer',     color: '#AADDFF', label: 'Speed' },
  HARDWARE_CONFIG_CHANGED: { icon: 'memory',     color: '#00E676', label: 'Hardware Settings' },
  PROTOCOL_ERROR:          { icon: 'alert-circle', color: '#ff4040', label: 'Protocol Fault' },
  BLE_WRITE_ERROR:         { icon: 'bluetooth-audio', color: '#ff4040', label: 'TX Error' },
  BLE_CONNECTION_ERROR:    { icon: 'bluetooth-off', color: '#ff4040', label: 'Connection Fault' },
  RAW_PAYLOAD:             { icon: 'matrix',       color: '#00f0ff', label: 'Data Trace' },
  SCREEN_OPENED:           { icon: 'dock-window',  color: '#c255ff', label: 'Screen View' },
  APP_BACKGROUNDED:        { icon: 'flip-to-back', color: '#888888', label: 'App Suspended' },
  APP_FOREGROUNDED:        { icon: 'flip-to-front',color: '#00E676', label: 'App Resumed' },
  ERROR_CAUGHT:            { icon: 'bug',          color: '#ff2020', label: 'Exception Trapped' },
  PERFORMANCE_METRIC:      { icon: 'chart-line',   color: '#FFD700', label: 'Metric' },
  STREET_MODE_ACTIVATED:   { icon: 'run-fast',      color: '#FF8C00', label: 'Street Mode ON' },
  STREET_MODE_DEACTIVATED: { icon: 'walk',          color: '#888888', label: 'Street Mode OFF' },
  STREET_JERK_DETECTED:    { icon: 'gesture-swipe', color: '#FFC107', label: 'Jerk Detected' },
  SYNC:                    { icon: 'sync',           color: '#00E676', label: 'Cloud Sync' },
};

export function formatLogTime(ms: number): string {
  const d = new Date(ms);
  return `${d.toLocaleDateString('en-US', { month: 'short', day: 'numeric' })} ${d.toLocaleTimeString('en-US', { hour12: false })}`;
}

export function getPayloadSummary(entry: LogEntry): string {
  const { d, e } = entry;
  switch (e) {
    case 'DEVICE_DISCOVERED': 
      return `${d.name || 'Unknown'} (${d.type || '?'}) RSSI: ${d.rssi ?? '?'}${d.sorting ? ` [${d.sorting}/${d.stripType}]` : ''}${d.points ? ` ${d.points}L/${d.segments || 1}S` : ''}`;
    case 'DEVICE_CONNECTED':  return `${d.name || d.id}${d.firmware ? ` (FW: ${d.firmware})` : ''}`;
    case 'MODE_CHANGED':      return `→ ${d.mode}${d.device ? ` on ${d.device}` : ''}`;
    case 'PATTERN_CHANGED':   return `${d.pattern}${d.mode ? ` [${d.mode}]` : ''}`;
    case 'COLOR_CHANGED':     return `${d.hex}${d.device ? ` on ${d.device}` : ''}`;
    case 'BRIGHTNESS_CHANGED':return `${d.value}%`;
    case 'SPEED_CHANGED':     return `${d.value}%`;
    case 'HARDWARE_CONFIG_CHANGED': return `${d.name || d.deviceId}: ${d.points} LEDs (${d.segments || 1} seg), ${d.stripType}, ${d.sorting}`;
    case 'ERROR_CAUGHT':      return `${d.message || String(d.error || 'Unknown Exception')}`;
    default: return typeof d === 'string' ? d : JSON.stringify(d).slice(0, 60);
  }
}

/**
 * useAdminTelemetry — Domain hook for managing system logs, metrics, and cloud upload logic.
 */
export function useAdminTelemetry(visible: boolean) {
  const [logs, setLogs] = useState<LogEntry[]>([]);
  const [stats, setStats] = useState<TelemetryStats | null>(null);
  const [isUploading, setIsUploading] = useState(false);

  const load = useCallback(async () => {
    try {
      const [allLogs, allStats] = await Promise.all([
        AppLogger.getLogs(),
        AppLogger.getStats()
      ]);
      setLogs(allLogs);
      setStats(allStats);
    } catch (err) {
      console.error('Failed to load telemetry:', err);
    }
  }, []);

  useEffect(() => {
    if (visible) load();
  }, [visible, load]);

  const clearLogs = useCallback(async () => {
    await AppLogger.clearLogs();
    await load();
  }, [load]);

  const uploadLogs = useCallback(async () => {
    if (isUploading) return;
    setIsUploading(true);
    try {
      await AppLogger.uploadLogsToSupabase();
      Alert.alert('Upload Complete', 'Telemetry synced to Supabase.');
    } catch (err: any) {
      Alert.alert('Upload Failed', err?.message || String(err));
    } finally {
      setIsUploading(false);
    }
  }, [isUploading]);

  const exportLogs = useCallback(async () => {
    const json = await AppLogger.exportJSON();
    try {
      await Share.share({ message: json, title: 'SK8Lytz Logs' });
    } catch (e) {
      Alert.alert('Export failed', String(e));
    }
  }, []);

  return {
    logs,
    stats,
    isUploading,
    load,
    clearLogs,
    uploadLogs,
    exportLogs
  };
}
