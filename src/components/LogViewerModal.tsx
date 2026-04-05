/**
 * LogViewerModal.tsx — SK8Lytz In-App Analytics Dashboard
 *
 * Full-screen modal that renders the current session's telemetry data
 * sourced from the AppLogger local buffer.
 *
 * Four-tab layout:
 *  LOGS     — Virtualized event timeline (newest first), color-coded by event type
 *             Each event type maps to an icon, color, and human-readable summary
 *  STATS    — Aggregated usage: top modes, top patterns, most-used colors
 *  DEVICES  — All BLE devices seen this session with hardware config details
 *  EXPORT   — JSON dump of the full local log buffer (shareable via OS sheet)
 *
 * Event type registry: EVENT_META maps every EventType to { icon, color, label }
 * Custom rendering: payloadSummary() formats each event's data field for display
 *
 * Note: Reads LOCAL AppLogger buffer only. Historical Supabase data requires
 * a separate authenticated query endpoint (not yet implemented).
 *
 * Depends on: AppLogger (singleton), MaterialCommunityIcons
 * Platform: React Native (Android + Web)
 */
import React, { useState, useEffect, useCallback } from 'react';
import {
  View, Text, StyleSheet, Modal, TouchableOpacity, ScrollView,
  Share, Alert, FlatList, Platform, SafeAreaView, TextInput
} from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import * as Device from 'expo-device';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { AppLogger, LogEntry, EventType } from '../services/AppLogger';
import { useTheme } from '../context/ThemeContext';

type Tab = 'timeline' | 'devices' | 'stats' | 'admin';

const EVENT_META: Record<EventType, { icon: string; color: string; label: string }> = {
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
};

function formatTime(ms: number): string {
  const d = new Date(ms);
  return `${d.toLocaleDateString('en-US', { month: 'short', day: 'numeric' })} ${d.toLocaleTimeString('en-US', { hour12: false })}`;
}

function payloadSummary(entry: LogEntry): string {
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
    case 'DEVICE_RENAMED':    return `"${d.oldName}" → "${d.newName}"`;
    case 'PROTOCOL_ERROR':    return `[${d.context}] ${d.error}${d.deviceId ? ` on ${d.deviceId}` : ''}`;
    case 'BLE_WRITE_ERROR':   return `TX Failed: ${d.error}${d.target ? ` on ${d.target}` : ''}`;
    case 'BLE_CONNECTION_ERROR': return `${d.error}${d.deviceId ? ` (ID: ${d.deviceId})` : ''}`;
    case 'SCAN_FILTER_MATCH':
      return `${d.name} (${d.id}) RSSI: ${d.rssi} [${d.isSymphony ? 'SPI' : ''}${d.isKnownPrefix ? ' PREFIX' : ''}${d.hasZenggeService ? ' UUID' : ''}]`;
    case 'SCAN_FILTER_REJECT':
      return `${d.name || '?'}: ${d.reason || 'Unknown'} (ID: ${d.id})`;
    case 'SCAN_COMPLETED':    return `${d.devicesFound ?? 0} device(s) found`;
    case 'SCREEN_OPENED':     return `${d.screenName || 'Unknown Screen'} Opened`;
    case 'APP_BACKGROUNDED':  return `App paused/backgrounded`;
    case 'APP_FOREGROUNDED':  return `App resumed/foregrounded`;
    case 'ERROR_CAUGHT':      return `${d.message || String(d.error || 'Unknown Exception')}`;
    case 'PERFORMANCE_METRIC':return `${d.metricName}: ${d.value}${d.unit ? ` ${d.unit}` : ''}`;
    default: return JSON.stringify(d).slice(0, 60);
  }
}

interface LogViewerModalProps {
  visible: boolean;
  onClose: () => void;
  onOpenProgrammer?: () => void;
  onOpenSniffer?: () => void;
  writeToDevice?: (data: number[], deviceId?: string) => Promise<void>;
  liveRxPayload?: { deviceId: string; payloadHex: string; timestamp?: number } | null;
  connectedDevices?: { id: string, name: string | null }[];
  allDevices?: any[];
  isScanning?: boolean;
  handleScan?: () => void;
  onClearAll?: () => void;
  onConnectToDevice?: (device: any) => Promise<any>;
  liveDeviceConfigs?: Record<string, any>;
}

export default function LogViewerModal({ visible, onClose, onOpenProgrammer, onOpenSniffer, writeToDevice, liveRxPayload, connectedDevices, allDevices, isScanning, handleScan, onClearAll, onConnectToDevice, liveDeviceConfigs }: LogViewerModalProps) {
  const { Colors, isDark } = useTheme();
  const [tab, setTab] = useState<Tab>('timeline');
  const [logs, setLogs] = useState<LogEntry[]>([]);
  const [stats, setStats] = useState<any>(null);
  const [deviceConfigs, setDeviceConfigs] = useState<Record<string, any>>({});
  const [isUploading, setIsUploading] = useState(false);
  const [simpleScannerMode, setSimpleScannerMode] = useState(false);
  const [confirmDeleteVisible, setConfirmDeleteVisible] = useState(false);
  const [connectingId, setConnectingId] = useState<string | null>(null);

  useEffect(() => {
    if (visible) AppLogger.log('SCREEN_OPENED', { screenName: 'Analytics' });
  }, [visible]);
  
  const load = useCallback(async () => {
    const l = await AppLogger.getLogs();
    setLogs(l);
    const s = await AppLogger.getStats();
    setStats(s);
    try {
      const storedConfigs = await AsyncStorage.getItem('ng_device_configs');
      if (storedConfigs) setDeviceConfigs(JSON.parse(storedConfigs) || {});
    } catch(e) {}
  }, []);

  useEffect(() => {
    if (visible) load();
  }, [visible, load]);

  const handleExport = async () => {
    const json = await AppLogger.exportJSON();
    try {
      await Share.share({ message: json, title: 'SK8Lytz Logs' });
    } catch (e) {
      Alert.alert('Export failed', String(e));
    }
  };

  const handleClear = () => {
    setConfirmDeleteVisible(true);
  };

  const executeClear = async () => {
    await AppLogger.clearLogs();
    load();
    if (onClearAll) onClearAll();
    setConfirmDeleteVisible(false);
  };

  const handleUpload = async () => {
    if (isUploading) return;
    setIsUploading(true);
    try {
      await AppLogger.uploadLogsToSupabase();
      Alert.alert('Upload Complete', 'Logs successfully uploaded to sk8lytz-logs bucket on Supabase.');
    } catch (err: any) {
      Alert.alert('Upload Failed', err?.message || String(err));
    } finally {
      setIsUploading(false);
    }
  };

  const bg = '#FFFFFF';
  const cardBg = '#F8F8F8';
  const textPrimary = '#000000';
  const textMuted = '#444444';
  const borderColor = '#CCCCCC';

  const renderLogItem = ({ item }: { item: LogEntry }) => {
    const meta = EVENT_META[item.e] || { icon: 'information', color: '#888', label: item.e };
    return (
      <View style={[styles.logRow, { borderBottomColor: borderColor }]}>
        <MaterialCommunityIcons name={meta.icon as any} size={18} color={meta.color} style={styles.logIcon} />
        <View style={styles.logBody}>
          <View style={styles.logHeader}>
            <Text style={[styles.logType, { color: meta.color }]}>{meta.label}</Text>
            <Text style={[styles.logTime, { color: textMuted }]}>{formatTime(item.t)}</Text>
          </View>
          <Text style={[styles.logPayload, { color: textMuted }]} numberOfLines={1}>
            {payloadSummary(item)}
          </Text>
          {item.e === 'COLOR_CHANGED' && item.d.hex && (
            <View style={[styles.colorSwatch, { backgroundColor: item.d.hex }]} />
          )}
        </View>
      </View>
    );
  };

  const renderDeviceTab = () => {
    const earliest = new Map<string, number>();
    const latest = new Map<string, number>();
    const uniqueMeta = new Map<string, any>();
    const firmwares = new Map<string, string>();

    // logs are sorted NEWEST first.
    logs.forEach(l => {
      if (l.e === 'DEVICE_CONNECTED' && l.d.id && l.d.firmware) {
         if (!firmwares.has(l.d.id)) firmwares.set(l.d.id, l.d.firmware);
      }
      if (l.e === 'DEVICE_DISCOVERED' || l.e === 'DEVICE_CONNECTED') {
         const id = l.d.id;
         if (!id) return;
         if (!latest.has(id)) latest.set(id, l.t);    // first element encountered is latest
         earliest.set(id, l.t);                       // will continually overwrite until it points to the oldest one
         if (!uniqueMeta.has(id) && l.e === 'DEVICE_DISCOVERED') uniqueMeta.set(id, l.d); // store newest payload meta
      }
    });

    const uniqueIds = Array.from(latest.keys());

    return (
      <ScrollView style={styles.tabContent}>
        {uniqueIds.length === 0 && (
          <Text style={[styles.emptyText, { color: textMuted }]}>No devices logged yet.</Text>
        )}
        {uniqueIds.map((id, i) => {
          const meta = uniqueMeta.get(id) || {};
          const config = deviceConfigs[id] || {};
          const sortingLabel = config.sorting === 'leftToRight' ? 'L→R' : config.sorting === 'rightToLeft' ? 'R→L' : config.sorting;

          return (
            <View key={i} style={[styles.deviceCard, { backgroundColor: cardBg, borderColor }]}>
              <MaterialCommunityIcons name="bluetooth-connect" size={24} color="#9D4EFF" />
              <View style={{ marginLeft: 12, flex: 1 }}>
                <Text style={[styles.deviceName, { color: textPrimary }]}>{config.name || meta.name || 'Unknown'}</Text>
                <Text style={[styles.deviceDetail, { color: textMuted }]}>MAC Address: {id}</Text>
                {firmwares.has(id) && <Text style={[styles.deviceDetail, { color: textMuted }]}>Firmware: {firmwares.get(id)}</Text>}
                
                <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 6, marginTop: 4 }}>
                  <Text style={[styles.deviceDetail, { color: textMuted }]}>Type: {config.type || meta?.type || '?'}</Text>
                  {meta?.rssi && <Text style={[styles.deviceDetail, { color: textMuted }]}>· RSSI: {meta.rssi}</Text>}
                  {(config.points || meta?.points) && (
                    <Text style={[styles.deviceDetail, { color: textMuted }]}>
                      · LEDs: {config.points || meta.points} ({config.segments || meta.segments || 1} seg)
                    </Text>
                  )}
                  {(config.stripType || meta.stripType) && (
                    <Text style={[styles.deviceDetail, { color: textMuted }]}>
                      · Format: {config.stripType || meta.stripType}
                    </Text>
                  )}
                  {(config.sorting || meta.sorting) && (
                    <Text style={[styles.deviceDetail, { color: textMuted }]}>
                      · Sort: {config.sorting === 'leftToRight' ? 'L→R' : 
                               config.sorting === 'rightToLeft' ? 'R→L' : 
                               (config.sorting || meta.sorting)}
                    </Text>
                  )}
                </View>

                <View style={{ borderTopWidth: 1, borderTopColor: borderColor, marginTop: 6, paddingTop: 4 }}>
                  <Text style={[styles.deviceDetail, { color: textMuted }]}>First seen: {formatTime(earliest.get(id)!)}</Text>
                  <Text style={[styles.deviceDetail, { color: textMuted }]}>Last seen: {formatTime(latest.get(id)!)}</Text>
                </View>
              </View>
            </View>
          );
        })}
      </ScrollView>
    );
  };

  const renderStatsTab = () => {
    if (!stats) return null;

    const gbMem = Device.totalMemory ? (Device.totalMemory / 1024 ** 3).toFixed(1) + ' GB' : 'Unknown';
    const osDisplay = `${Device.osName || Platform.OS} ${Device.osVersion || ''}`.trim();
    const sessionMins = stats.lastAppOpenedTime ? Math.round((Date.now() - stats.lastAppOpenedTime) / 60000) : 0;

    return (
      <ScrollView style={styles.tabContent}>
        <Text style={[styles.statSection, { color: textPrimary }]}>📱 Device & App Telemetry</Text>
        <View style={[styles.statCard, { backgroundColor: cardBg, borderColor }]}>
          <StatRow label="Brand / Model" value={`${Device.brand || ''} ${Device.modelName || 'Unknown'}`.trim()} color={textPrimary} muted={textMuted} />
          <StatRow label="Manufacturer" value={Device.manufacturer || 'Unknown'} color={textPrimary} muted={textMuted} />
          <StatRow label="Operating System" value={osDisplay} color={textPrimary} muted={textMuted} />
          <StatRow label="Environment" value={Device.isDevice ? 'Physical Device' : 'Simulator'} color={textPrimary} muted={textMuted} />
          <StatRow label="Battery Level" value={stats.batteryLevel !== -1 ? `${Math.round(stats.batteryLevel * 100)}%` : 'Unknown'} color={textPrimary} muted={textMuted} />
          <StatRow label="Power State" value={stats.isLowPowerMode ? 'Low Power Mode' : 'Normal'} color={stats.isLowPowerMode ? '#FFB84D' : textPrimary} muted={textMuted} />
          <StatRow label="BLE Target MAC" value={stats.primaryBleMac || 'N/A'} color="#00f0ff" muted={textMuted} />
          <StatRow label="Total RAM" value={gbMem} color={textPrimary} muted={textMuted} />
          <StatRow label="Avg Boot Time" value={stats.averageLoadTimeMs ? `${stats.averageLoadTimeMs}ms` : 'N/A'} color={textPrimary} muted={textMuted} />
          <StatRow label="Current Session" value={`${sessionMins} mins`} color={textPrimary} muted={textMuted} />
          <StatRow label="Log Storage" value={`${(stats.storageBytesEstimate / 1024).toFixed(2)} KB`} color={textPrimary} muted={textMuted} />
          <StatRow label="App Storage" value={`${(stats.totalStorageEstimate / 1024).toFixed(2)} KB`} color={textPrimary} muted={textMuted} />
        </View>

        <Text style={[styles.statSection, { color: textPrimary }]}>📊 Analytics Overview</Text>
        <View style={[styles.statCard, { backgroundColor: cardBg, borderColor }]}>
          <StatRow label="Total Events Logged" value={String(stats.totalEvents)} color={textPrimary} muted={textMuted} />
          <StatRow label="Devices Discovered" value={String(stats.devicesDiscovered)} color={textPrimary} muted={textMuted} />
        </View>

        <Text style={[styles.statSection, { color: textPrimary }]}>🎛️ Mode Usage</Text>
        <View style={[styles.statCard, { backgroundColor: cardBg, borderColor }]}>
          {Object.entries(stats.modeUsage).sort((a, b) => (b[1] as number) - (a[1] as number)).map(([mode, count]) => (
            <StatRow key={mode} label={mode} value={`${count}×`} color={textPrimary} muted={textMuted} />
          ))}
          {Object.keys(stats.modeUsage).length === 0 && (
            <Text style={[styles.emptyText, { color: textMuted }]}>No mode changes yet.</Text>
          )}
        </View>

        <Text style={[styles.statSection, { color: textPrimary }]}>🎨 Pattern Usage</Text>
        <View style={[styles.statCard, { backgroundColor: cardBg, borderColor }]}>
          {Object.entries(stats.patternUsage).length > 0 ? (
            Object.entries(stats.patternUsage)
              .sort((a, b) => (b[1] as any).count - (a[1] as any).count)
              .slice(0, 10)
              .map(([name, data]) => (
                <View key={name} style={{ marginBottom: 8 }}>
                  <StatRow 
                    label={name} 
                    value={`${(data as any).count}×`} 
                    color={textPrimary} 
                    muted={textMuted} 
                  />
                  {(data as any).colors.length > 0 && (
                    <View style={{ flexDirection: 'row', gap: 4, marginTop: 2, marginLeft: 4 }}>
                      {(data as any).colors.map((c: string) => (
                        <View key={c} style={{ width: 10, height: 10, borderRadius: 5, backgroundColor: c, borderWidth: 0.5, borderColor }} />
                      ))}
                    </View>
                  )}
                </View>
              ))
          ) : (
            <Text style={[styles.emptyText, { color: textMuted }]}>No patterns used yet.</Text>
          )}
        </View>

        <Text style={[styles.statSection, { color: textPrimary }]}>🟡 Top Colors</Text>
        <View style={[styles.statCard, { backgroundColor: cardBg, borderColor }]}>
          {Object.entries(stats.colorUsage).sort((a, b) => (b[1] as number) - (a[1] as number)).slice(0, 8).map(([hex, count]) => (
            <View key={hex} style={styles.colorRow}>
              <View style={[styles.colorDot, { backgroundColor: hex }]} />
              <Text style={[styles.colorHex, { color: textPrimary }]}>{hex}</Text>
              <Text style={[{ color: textMuted, fontSize: 13 }]}>{count as number}×</Text>
            </View>
          ))}
          {Object.keys(stats.colorUsage).length === 0 && (
            <Text style={[styles.emptyText, { color: textMuted }]}>No color changes yet.</Text>
          )}
        </View>
        <View style={{ height: 40 }} />
      </ScrollView>
    );
  };

  const renderAdminTab = () => {
    if (simpleScannerMode) {
      return (
        <ScrollView style={styles.tabContent}>
          <TouchableOpacity style={{ marginBottom: 16 }} onPress={() => setSimpleScannerMode(false)}>
            <Text style={{ color: '#00f0ff', fontSize: 16, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>← Back to Admin Tools</Text>
          </TouchableOpacity>
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
             <Text style={[styles.statSection, { color: textPrimary, marginTop: 0, marginBottom: 0 }]}>📡 Simple Scanner</Text>
             <TouchableOpacity 
               style={{ backgroundColor: isScanning ? '#555' : '#00E676', paddingHorizontal: 16, paddingVertical: 8, borderRadius: 6 }} 
               onPress={() => { if (handleScan) handleScan(); }}
               disabled={isScanning}
             >
               <Text style={{ color: '#000', fontWeight: 'bold' }}>{isScanning ? 'SCANNING...' : 'START SCAN'}</Text>
             </TouchableOpacity>
          </View>
          
          {(!allDevices || allDevices.length === 0) && (
            <Text style={[styles.emptyText, { color: textMuted }]}>No devices found. Tap START SCAN to begin.</Text>
          )}

          {allDevices?.map((d: any, idx) => {
             // Merge: live prop (reactive, from DashboardScreen state) > local AsyncStorage cache
             const cfg = { ...(deviceConfigs[d.id] || {}), ...(liveDeviceConfigs?.[d.id] || {}) };
             const points   = cfg.points    || d.points    || null;
             const segments = cfg.segments  || d.segments  || 1;
             const sorting  = cfg.sorting   || d.sorting   || d.colorSortingName || null;
             const stripType= cfg.stripType || d.stripType || d.icName           || null;
             const isConn   = connectedDevices?.some(c => c.id === d.id);
             const isConnecting = connectingId === d.id;

             const handleConnect = async () => {
               if (!onConnectToDevice || isConnecting || isConn) return;
               setConnectingId(d.id);
               try {
                 await onConnectToDevice(d);
                 // liveDeviceConfigs prop will auto-update via DashboardScreen state —
                 // no need to re-read AsyncStorage here
               } catch (e) { console.warn('Connect failed', e); }
               finally { setConnectingId(null); }
             };
             return (
             <View key={d.id || idx} style={[styles.statCard, { backgroundColor: cardBg, borderColor }]}>
                <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 4 }}>
                  <Text style={{ color: textPrimary, fontWeight: 'bold', fontSize: 15, flex: 1 }} numberOfLines={1}>{cfg.name || d.name || 'Unknown Device'}</Text>
                  {isConn
                    ? <Text style={{ color: '#00E676', fontSize: 11, fontWeight: '700', marginLeft: 8 }}>● CONNECTED</Text>
                    : <TouchableOpacity onPress={handleConnect} disabled={isConnecting} style={{ backgroundColor: isConnecting ? '#555' : '#9D4EFF', paddingHorizontal: 10, paddingVertical: 5, borderRadius: 4, marginLeft: 8 }}>
                        <Text style={{ color: '#FFF', fontSize: 11, fontWeight: '700' }}>{isConnecting ? 'CONNECTING...' : 'CONNECT'}</Text>
                      </TouchableOpacity>
                  }
                </View>
                <Text style={{ color: textMuted, fontSize: 11, marginBottom: 6, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>{d.id}</Text>
                <StatRow label="Firmware"     value={d.firmware || cfg.firmware || '—'}       color="#00E676" muted={textMuted} />
                <StatRow label="RSSI"         value={d.rssi ? `${d.rssi} dBm` : '—'}         color="#FF7000" muted={textMuted} />
                <StatRow label="Points (LEDs)"value={points ? String(points) : '— connect'}   color="#00f0ff" muted={textMuted} />
                <StatRow label="Segments"     value={String(segments)}                         color="#00f0ff" muted={textMuted} />
                <StatRow label="IC / Strip"   value={stripType || '— connect'}                color="#FFD700" muted={textMuted} />
                <StatRow label="Color Order"  value={sorting   || '— connect'}                color="#FF69B4" muted={textMuted} />
             </View>
             );
          })}


          <View style={{ height: 40 }} />
        </ScrollView>
      );
    }

    return (
      <ScrollView style={styles.tabContent}>
        <Text style={[styles.statSection, { color: textPrimary }]}>🛠️ Admin Tools</Text>
        <View style={[styles.statCard, { backgroundColor: cardBg, borderColor, padding: 20 }]}>
          <Text style={{ color: textMuted, fontSize: 13, marginBottom: 16, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>
            Restricted diagnostics payload for low-level protocol debugging.
          </Text>

          <TouchableOpacity 
            style={{ backgroundColor: 'rgba(0, 240, 255, 0.1)', borderColor: '#00f0ff', borderWidth: 1, paddingVertical: 14, borderRadius: 8, marginBottom: 16 }}
            onPress={() => setSimpleScannerMode(true)}
          >
            <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center' }}>
              <Text style={{ fontSize: 16, marginRight: 8 }}>📡</Text>
              <Text style={{ color: '#00f0ff', fontSize: 15, fontWeight: '700', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>Launch Simple Scanner</Text>
            </View>
          </TouchableOpacity>

          <TouchableOpacity 
            style={{ backgroundColor: 'rgba(255, 61, 0, 0.1)', borderColor: '#ff4040', borderWidth: 1, paddingVertical: 14, borderRadius: 8, marginBottom: 16 }}
            onPress={() => {
              if (onOpenProgrammer) onOpenProgrammer();
            }}
          >
            <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center' }}>
              <Text style={{ fontSize: 16, marginRight: 8 }}>⚡</Text>
              <Text style={{ color: '#ff4040', fontSize: 15, fontWeight: '700', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>Launch SK8Lytz Programmer</Text>
            </View>
          </TouchableOpacity>

          <TouchableOpacity 
            style={{ backgroundColor: 'rgba(152, 251, 152, 0.1)', borderColor: '#98FB98', borderWidth: 1, paddingVertical: 14, borderRadius: 8 }}
            onPress={() => {
              if (onOpenSniffer) onOpenSniffer();
            }}
          >
            <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center' }}>
              <Text style={{ fontSize: 16, marginRight: 8 }}>💉</Text>
              <Text style={{ color: '#98FB98', fontSize: 15, fontWeight: '700', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>Launch Hardware Tester</Text>
            </View>
          </TouchableOpacity>
        </View>
      </ScrollView>
    );
  };

  const timelineLogs = logs.filter(l => l.e !== 'RAW_PAYLOAD');

  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen" onRequestClose={onClose}>
      <SafeAreaView style={[styles.root, { backgroundColor: bg }]}>
        {/* Header */}
        <View style={[styles.modalHeader, { borderBottomColor: borderColor }]}>
          <View style={{ flexShrink: 1, paddingRight: 8 }}>
            <Text style={[styles.title, { color: textPrimary }]} numberOfLines={1}>SK8Lytz Analytics</Text>
            <Text style={[styles.subtitle, { color: textMuted }]} numberOfLines={1}>{timelineLogs.length} events stored</Text>
          </View>
          <View style={styles.headerActions}>
            <TouchableOpacity onPress={handleExport} style={styles.actionBtn}>
              <MaterialCommunityIcons name="download" size={22} color="#00f0ff" />
            </TouchableOpacity>
            <TouchableOpacity
              onPress={handleUpload}
              style={[styles.actionBtn, isUploading && { opacity: 0.5 }]}
              disabled={isUploading}
            >
              <MaterialCommunityIcons
                name={isUploading ? 'cloud-sync' : 'cloud-upload'}
                size={22}
                color="#00E676"
              />
            </TouchableOpacity>
            <TouchableOpacity onPress={handleClear} style={styles.actionBtn}>
              <MaterialCommunityIcons name="delete-sweep" size={22} color="#ff4040" />
            </TouchableOpacity>
            <TouchableOpacity onPress={onClose} style={styles.actionBtn}>
              <MaterialCommunityIcons name="close" size={22} color={textPrimary} />
            </TouchableOpacity>
          </View>
        </View>

        {/* Tabs */}
        <View style={[styles.tabs, { borderBottomColor: borderColor }]}>
          {(['timeline', 'devices', 'stats', 'admin'] as Tab[]).map(t => (
            <TouchableOpacity
              key={t}
              onPress={() => setTab(t)}
              style={[styles.tabBtn, tab === t && styles.tabBtnActive]}
            >
              <Text style={[styles.tabLabel, { color: tab === t ? '#00f0ff' : textMuted }]}>
                {t.charAt(0).toUpperCase() + t.slice(1)}
              </Text>
            </TouchableOpacity>
          ))}
        </View>

        {/* Content */}
        {tab === 'timeline' && (
          <FlatList
            data={timelineLogs}
            keyExtractor={(_, i) => String(i)}
            renderItem={renderLogItem}
            ListEmptyComponent={
              <Text style={[styles.emptyText, { color: textMuted, margin: 24 }]}>No events logged yet.</Text>
            }
          />
        )}
        {tab === 'devices' && renderDeviceTab()}
        {tab === 'stats' && renderStatsTab()}
        {tab === 'admin' && renderAdminTab()}
      </SafeAreaView>

      <Modal visible={confirmDeleteVisible} transparent animationType="fade">
        <View style={{ flex: 1, backgroundColor: 'rgba(0,0,0,0.6)', justifyContent: 'center', alignItems: 'center', padding: 20 }}>
          <View style={{ backgroundColor: isDark ? '#1A1A1A' : '#FFF', padding: 24, borderRadius: 12, width: '100%', maxWidth: 400, borderColor: '#ff4040', borderWidth: 1 }}>
            <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 16 }}>
              <MaterialCommunityIcons name="alert" size={24} color="#ff4040" />
              <Text style={{ fontSize: 18, fontWeight: '800', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', color: isDark ? '#FFF' : '#000', marginLeft: 8 }}>Purge Telemetry Logs</Text>
            </View>
            <Text style={{ fontSize: 14, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', color: isDark ? '#CCC' : '#444', marginBottom: 24, lineHeight: 20 }}>
              Are you sure you want to completely erase all timeline, device, and analytics stats from local memory? This action cannot be reversed.
            </Text>
            <View style={{ flexDirection: 'row', justifyContent: 'flex-end', gap: 12 }}>
              <TouchableOpacity onPress={() => setConfirmDeleteVisible(false)} style={{ paddingVertical: 10, paddingHorizontal: 16, borderRadius: 6, backgroundColor: isDark ? '#333' : '#EEE' }}>
                <Text style={{ fontWeight: '700', color: isDark ? '#FFF' : '#000', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>Cancel</Text>
              </TouchableOpacity>
              <TouchableOpacity onPress={executeClear} style={{ paddingVertical: 10, paddingHorizontal: 16, borderRadius: 6, backgroundColor: '#ff4040' }}>
                <Text style={{ fontWeight: '700', color: '#FFF', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>Erase Everything</Text>
              </TouchableOpacity>
            </View>
          </View>
        </View>
      </Modal>
    </Modal>
  );
}

function StatRow({ label, value, color, muted }: { label: string; value: string; color: string; muted: string }) {
  return (
    <View style={styles.statRow}>
      <Text style={[styles.statLabel, { color: muted }]}>{label}</Text>
      <Text style={[styles.statValue, { color }]}>{value}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  root: { flex: 1, paddingTop: Platform.OS === 'ios' ? 50 : 24 },
  modalHeader: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
    paddingHorizontal: 20, paddingBottom: 12, borderBottomWidth: 1,
  },
  title: { fontSize: 20, fontWeight: '800', letterSpacing: 0.5, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' },
  subtitle: { fontSize: 12, marginTop: 2, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' },
  headerActions: { flexDirection: 'row', alignItems: 'center' },
  actionBtn: { padding: 8, marginLeft: 2 },
  tabs: { flexDirection: 'row', borderBottomWidth: 1 },
  tabBtn: { flex: 1, paddingVertical: 12, alignItems: 'center' },
  tabBtnActive: { borderBottomWidth: 2, borderBottomColor: '#000000' },
  tabLabel: { fontSize: 13, fontWeight: '600', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' },
  tabContent: { flex: 1, padding: 16 },
  logRow: {
    flexDirection: 'row', alignItems: 'flex-start',
    paddingVertical: 10, paddingHorizontal: 16, borderBottomWidth: StyleSheet.hairlineWidth,
  },
  logIcon: { marginTop: 2, marginRight: 12, width: 20 },
  logBody: { flex: 1 },
  logHeader: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' },
  logType: { fontSize: 13, fontWeight: '700', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' },
  logTime: { fontSize: 10, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' },
  logPayload: { fontSize: 12, marginTop: 2, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' },
  colorSwatch: { width: 16, height: 8, borderRadius: 3, marginTop: 4, borderWidth: 1, borderColor: '#000' },
  emptyText: { textAlign: 'center', fontSize: 14, marginTop: 16, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' },
  deviceCard: {
    flexDirection: 'row', alignItems: 'center', borderRadius: 0,
    padding: 14, marginBottom: 10, borderWidth: 1,
  },
  deviceName: { fontSize: 15, fontWeight: '700', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' },
  deviceDetail: { fontSize: 12, marginTop: 2, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' },
  statSection: { fontSize: 14, fontWeight: '700', marginTop: 16, marginBottom: 8, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' },
  statCard: { borderRadius: 0, padding: 14, marginBottom: 4, borderWidth: 1 },
  statRow: { flexDirection: 'row', justifyContent: 'space-between', paddingVertical: 5 },
  statLabel: { fontSize: 13, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' },
  statValue: { fontSize: 13, fontWeight: '700', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' },
  colorRow: { flexDirection: 'row', alignItems: 'center', paddingVertical: 5 },
  colorDot: { width: 18, height: 18, borderRadius: 0, marginRight: 10, borderWidth: 1, borderColor: '#000' },
  colorHex: { flex: 1, fontSize: 12, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' },
});
