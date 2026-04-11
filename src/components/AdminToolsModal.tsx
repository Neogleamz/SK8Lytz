/**
 * AdminToolsModal.tsx — SK8Lytz Unified Administrative Hub
 * 
 * A high-fidelity administrative interface for real-time telemetry, 
 * performance analytics, device management, and hardware diagnostic tools.
 *
 * Tab Hierarchy:
 *  1. TIMELINE - Virtualized event log (low-level protocol & app lifecycle)
 *  2. STATS    - Aggregated session analytics (mode usage, top patterns, hardware metrics)
 *  3. DEVICE   - Hardware-centric view of all discovered and configured BLE devices
 *  4. TOOLS    - Gateway to low-level Diagnostic Lab and Firmware Programmer
 *
 * @param {AdminToolsModalProps} props - Configuration for visibility and tool callbacks
 */
import React, { useState, useEffect, useCallback, useMemo } from 'react';
import {
  View, Text, StyleSheet, Modal, TouchableOpacity, ScrollView,
  Share, Alert, FlatList, Platform, SafeAreaView, TextInput, Switch
} from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import * as Device from 'expo-device';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { AppLogger, LogEntry, EventType } from '../services/AppLogger';
import { useTheme } from '../context/ThemeContext';
import AdminPicksScheduler from './AdminPicksScheduler';
import { useProductCatalog } from '../hooks/useProductCatalog';
import { LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import type { ProductProfile, VizShape } from '../types/ProductCatalog';

type Tab = 'timeline' | 'stats' | 'device' | 'tools';

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
  CREW_SESSION_CREATED:    { icon: 'account-group', color: '#FFAA00', label: 'Crew Created' },
  CREW_SESSION_JOINED:     { icon: 'account-plus',  color: '#00AAFF', label: 'Crew Joined' },
  CREW_SESSION_LEFT:       { icon: 'account-minus', color: '#FF6B6B', label: 'Crew Left' },
  CREW_SESSION_ENDED:      { icon: 'stop-circle',   color: '#FF6B6B', label: 'Session Ended' },
  CREW_SESSION_SHARED:     { icon: 'share-variant', color: '#00E5FF', label: 'Session Shared' },
  CREW_LEADERSHIP_TRANSFERRED: { icon: 'crown',     color: '#FFD700', label: 'Leader Handoff' },
  CREW_SCENE_BROADCAST:    { icon: 'broadcast',     color: '#FFAA00', label: 'Scene Broadcast' },
  CREW_SCENE_RECEIVED:     { icon: 'download-circle', color: '#00AAFF', label: 'Scene Received' },
  CREW_AUTO_REJOINED:      { icon: 'refresh-circle', color: '#00E676', label: 'Auto-Rejoined' },
  CREW_ERROR:              { icon: 'account-alert', color: '#FF4444', label: 'Crew Error' },
  STREET_MODE_ACTIVATED:   { icon: 'run-fast',      color: '#FF8C00', label: 'Street Mode ON' },
  STREET_MODE_DEACTIVATED: { icon: 'walk',          color: '#888888', label: 'Street Mode OFF' },
  STREET_JERK_DETECTED:    { icon: 'gesture-swipe', color: '#FFC107', label: 'Jerk Detected' },
  STREET_SENSITIVITY_CHANGED: { icon: 'tune-vertical', color: '#AADDFF', label: 'Sensitivity' },
  PUSH_TOKEN_REGISTERED:   { icon: 'bell-ring',     color: '#00E676', label: 'Push Registered' },
  PUSH_NOTIFICATION_TAPPED:{ icon: 'bell-check',    color: '#00AAFF', label: 'Notif Tapped' },
  PUSH_NOTIFICATION_SENT:  { icon: 'send',          color: '#FFAA00', label: 'Notif Sent' },
  PUSH_TOKEN_UNREGISTERED: { icon: 'bell-off',      color: '#888888', label: 'Push Unregistered' },
  PROFILE_UPDATED:         { icon: 'account-edit',  color: '#c255ff', label: 'Profile Updated' },
  CREW_PERMANENT_CREATED:  { icon: 'star-circle',   color: '#FFAA00', label: 'Perm Crew Created' },
  CREW_PERMANENT_JOINED:   { icon: 'star-plus',     color: '#00AAFF', label: 'Perm Crew Joined' },
  CREW_PERMANENT_LEFT:     { icon: 'star-minus',    color: '#FF6B6B', label: 'Perm Crew Left' },
  CREW_PERMANENT_DELETED:  { icon: 'star-off',      color: '#FF4040', label: 'Perm Crew Deleted' },
  CREW_PERMANENT_UPDATED:  { icon: 'star-settings', color: '#FFD700', label: 'Perm Crew Updated' },
  CREW_MEMBERS_ADDED:      { icon: 'account-multiple-plus', color: '#00AAFF', label: 'Members Added' },
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

interface AdminToolsModalProps {
  /** Whether the modal is currently visible */
  visible: boolean;
  /** Callback to close the modal */
  onClose: () => void;
  /** Callback to launch the Firmware Programmer modal */
  onOpenProgrammer?: () => void;
  /** Callback to launch the LED Diagnostic Lab modal */
  onOpenLab?: () => void;
  /** Callback to dispatch physical BLE write commands */
  writeToDevice?: (data: number[], deviceId?: string) => Promise<void>;
  /** Live telemetry feed of incoming hardware notifications */
  liveRxPayload?: { deviceId: string; payloadHex: string; timestamp?: number } | null;
  /** List of currently connected BLE peripherals */
  connectedDevices?: { id: string, name: string | null }[];
  /** Master list of all discovered peripherals */
  allDevices?: any[];
  /** BLE scanning state */
  isScanning?: boolean;
  /** Trigger to start a fresh BLE discovery cycle */
  handleScan?: () => void;
  /** Callback to flush the system event timeline */
  onClearAll?: () => void;
  /** Logic to establish a direct connection to a selected device */
  onConnectToDevice?: (device: any) => Promise<any>;
  /** Real-time hardware configuration map (strips, points, sorting) */
  liveDeviceConfigs?: Record<string, any>;
}

export default function AdminToolsModal({ visible, onClose, onOpenProgrammer, onOpenLab, liveRxPayload, connectedDevices, allDevices, isScanning, handleScan, onClearAll, onConnectToDevice, liveDeviceConfigs }: AdminToolsModalProps) {
  const { isDark } = useTheme();
  const [tab, setTab] = useState<Tab>('timeline');
  const [isProductManagerVisible, setIsProductManagerVisible] = useState(false);
  const [logs, setLogs] = useState<LogEntry[]>([]);
  const [stats, setStats] = useState<any>(null);
  const [deviceConfigs, setDeviceConfigs] = useState<Record<string, any>>({});
  const [isUploading, setIsUploading] = useState(false);
  const [confirmDeleteVisible, setConfirmDeleteVisible] = useState(false);
  const [isPicksSchedulerVisible, setIsPicksSchedulerVisible] = useState(false);

  // ── Product Catalog ─────────────────────────────────────────────────────────
  const { allProfiles, saveProfile, syncFromCloud } = useProductCatalog();
  const [editingProfile, setEditingProfile] = useState<ProductProfile | null>(null);
  const [productSaving, setProductSaving] = useState(false);

  /** Creates a blank profile template for the Add Product form. */
  const blankProfile = (): ProductProfile => ({
    id: '',
    displayName: '',
    defaultLedPoints: 16,
    defaultSegments: 1,
    defaultIcType: 1,
    defaultColorSorting: 2,
    detectMinPoints: 1,
    detectMaxPoints: 9,
    vizShape: 'OVAL',
    vizDefaultPoints: 16,
    vizBlobDiameterMm: 5.7,
    vizBaseWidth: 55,
    vizBaseHeight: 115,
    vizStripCount: 2,
    vizStripSeparation: 32,
    vizStripOrientation: 'VERTICAL',
  });

  useEffect(() => {
    if (visible) AppLogger.log('SCREEN_OPENED', { screenName: 'AdminTools' });
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
    return (
      <ScrollView style={styles.tabContent}>
        <Text style={[styles.statSection, { color: textPrimary }]}>🛠️ Admin Tools</Text>
        <View style={[styles.statCard, { backgroundColor: cardBg, borderColor, padding: 20 }]}>
          <Text style={{ color: textMuted, fontSize: 13, marginBottom: 16, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>
            Restricted diagnostics payload for low-level protocol debugging.
          </Text>

          <TouchableOpacity 
            style={{ backgroundColor: 'rgba(255, 61, 0, 0.1)', borderColor: '#ff4040', borderWidth: 1, paddingVertical: 14, borderRadius: 8, marginBottom: 16 }}
            onPress={() => { if (onOpenProgrammer) onOpenProgrammer(); }}
          >
            <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center' }}>
              <Text style={{ fontSize: 16, marginRight: 8 }}>⚡</Text>
              <Text style={{ color: '#ff4040', fontSize: 15, fontWeight: '700', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>SK8Lytz Programmer</Text>
            </View>
          </TouchableOpacity>

          <TouchableOpacity 
            style={{ backgroundColor: 'rgba(255, 165, 0, 0.1)', borderColor: '#FFA500', borderWidth: 1, paddingVertical: 14, borderRadius: 8 }}
            onPress={() => {
              if (onOpenLab) onOpenLab();
            }}
          >
            <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center' }}>
              <Text style={{ fontSize: 16, marginRight: 8 }}>🔬</Text>
              <Text style={{ color: '#FFA500', fontSize: 15, fontWeight: '700', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>LED Diagnostic Lab</Text>
            </View>
          </TouchableOpacity>

          <TouchableOpacity 
            style={{ backgroundColor: 'rgba(255, 165, 0, 0.1)', borderColor: '#FFA500', borderWidth: 1, paddingVertical: 14, borderRadius: 8, marginTop: 16 }}
            onPress={() => setIsPicksSchedulerVisible(true)}
          >
            <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center' }}>
              <Text style={{ fontSize: 16, marginRight: 8 }}>📅</Text>
              <Text style={{ color: '#FFA500', fontSize: 15, fontWeight: '700', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>SK8Lytz Picks Scheduler</Text>
            </View>
          </TouchableOpacity>
          <TouchableOpacity 
            style={{ backgroundColor: 'rgba(255, 61, 0, 0.1)', borderColor: '#ff4040', borderWidth: 1, paddingVertical: 14, borderRadius: 8, marginTop: 16 }}
            onPress={() => setIsProductManagerVisible(true)}
          >
            <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center' }}>
              <Text style={{ fontSize: 16, marginRight: 8 }}>📦</Text>
              <Text style={{ color: '#ff4040', fontSize: 15, fontWeight: '700', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>Product Manager</Text>
            </View>
          </TouchableOpacity>
        </View>

        <AdminPicksScheduler
          visible={isPicksSchedulerVisible}
          onClose={() => setIsPicksSchedulerVisible(false)}
        />
        
        <Modal visible={isProductManagerVisible} animationType="slide" presentationStyle="formSheet" onRequestClose={() => setIsProductManagerVisible(false)}>
          <SafeAreaView style={{ flex: 1, backgroundColor: isDark ? '#080808' : '#111' }}>
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', padding: 16, borderBottomWidth: 1, borderBottomColor: '#222' }}>
              <Text style={{ color: '#FFF', fontSize: 18, fontWeight: '900', letterSpacing: 1 }}>PRODUCT MANAGER</Text>
              <TouchableOpacity onPress={() => setIsProductManagerVisible(false)}>
                <MaterialCommunityIcons name="close" size={24} color="#FFF" />
              </TouchableOpacity>
            </View>
            {renderProductsTab()}
          </SafeAreaView>
        </Modal>
      </ScrollView>
    );
  };

  // ── Products Tab ─────────────────────────────────────────────────────────────

  /**
   * ShapePreviewCanvas — Renders a static dot-map of LED positions for the given profile.
   * Uses the same path-sample math as ProductVisualizer. No animations, no color logic.
   * Used to let admins visually verify geometry before saving to Supabase.
   */
  const ShapePreviewCanvas = ({ profile }: { profile: ProductProfile }) => {
    const S = 0.38;
    const numSamples = 500;
    const numLeds = profile.vizDefaultPoints;
    const pathSamples: { left: number; top: number; length: number }[] = [];
    let totalLength = 0;

    for (let i = 0; i <= numSamples; i++) {
      let left = 0;
      let top = 0;

      if (profile.vizShape === 'RING') {
        const half = Math.floor(numSamples / 2);
        const fraction = i < half
          ? i / Math.max(1, half - 1)
          : (i - half) / Math.max(1, numSamples - half - 1);
        const angle = i < half
          ? (Math.PI / 2) - fraction * Math.PI
          : -(Math.PI / 2) - fraction * Math.PI;
        const n = 4; const power = 2 / n;
        const x = 70 * Math.sign(Math.cos(angle)) * Math.pow(Math.abs(Math.cos(angle)), power);
        const y = 110 * Math.sign(Math.sin(angle)) * Math.pow(Math.abs(Math.sin(angle)), power);
        left = (80 + x) * S;
        top = (120 + y) * S;

      } else if (profile.vizShape === 'DUAL_STRIP') {
        const sep = profile.vizStripSeparation ?? 32;
        const half = Math.floor(numSamples / 2);
        const stripHeight = profile.vizBaseHeight * S;
        const centreX = (profile.vizBaseWidth / 2) * S;
        const fract = i < half
          ? i / Math.max(1, half - 1)
          : (i - half) / Math.max(1, numSamples - half - 1);
        left = i < half ? centreX - (sep / 2) * S : centreX + (sep / 2) * S;
        top = fract * stripHeight;

      } else {
        const half = Math.floor(numSamples / 2);
        const fract = i < half
          ? i / Math.max(1, half - 1)
          : (i - half) / Math.max(1, numSamples - half - 1);
        const angle = i < half
          ? (Math.PI / 2) + fract * Math.PI
          : (Math.PI / 2) - fract * Math.PI;
        top = (150 + Math.sin(angle) * 150) * S;
        const vPos = Math.sin(angle);
        const pinch = 1 - 0.3 * Math.exp(-Math.pow(vPos - 0.1, 2) * 5);
        left = (70 + (Math.cos(angle) * 70 * pinch)) * S;
      }

      if (i > 0) {
        const prev = pathSamples[i - 1];
        const dist = Math.sqrt(Math.pow(left - prev.left, 2) + Math.pow(top - prev.top, 2));
        if (dist < 50 * S) totalLength += dist;
      }
      pathSamples.push({ top, left, length: totalLength });
    }

    const dots: { top: number; left: number }[] = [];
    let lastIdx = 0;
    for (let i = 0; i < numLeds; i++) {
      const targetLen = (i / numLeds) * totalLength;
      for (let j = lastIdx; j < pathSamples.length - 1; j++) {
        if (pathSamples[j + 1].length >= targetLen) {
          const p1 = pathSamples[j], p2 = pathSamples[j + 1];
          const segLen = p2.length - p1.length;
          const tt = segLen <= 0.0001 ? 0 : (targetLen - p1.length) / segLen;
          dots.push({
            left: p1.left + (p2.left - p1.left) * tt - profile.vizBlobDiameterMm / 2,
            top: p1.top + (p2.top - p1.top) * tt - profile.vizBlobDiameterMm / 2,
          });
          lastIdx = j;
          break;
        }
      }
    }

    return (
      <View style={{
        width: profile.vizBaseWidth * S,
        height: profile.vizBaseHeight * S,
        backgroundColor: '#111',
        borderRadius: 10,
        borderWidth: 1,
        borderColor: 'rgba(255,90,0,0.4)',
        position: 'relative',
        alignSelf: 'center',
        marginVertical: 12,
        overflow: 'hidden',
      }}>
        {dots.map((dot, i) => (
          <View key={i} style={{
            position: 'absolute',
            left: dot.left,
            top: dot.top,
            width: profile.vizBlobDiameterMm,
            height: profile.vizBlobDiameterMm,
            borderRadius: profile.vizBlobDiameterMm / 2,
            backgroundColor: '#FF5A00',
            opacity: 0.9,
          }} />
        ))}
        <Text style={{ position: 'absolute', bottom: 3, left: 0, right: 0, textAlign: 'center', color: 'rgba(255,255,255,0.3)', fontSize: 9 }}>
          {profile.vizShape} · {numLeds} LEDs
        </Text>
      </View>
    );
  };

  const handleSaveProfile = async () => {
    if (!editingProfile) return;
    if (!editingProfile.id.trim()) {
      Alert.alert('Validation Error', 'Product ID is required (e.g. RAILZ).');
      return;
    }
    setProductSaving(true);

    // [MOD] Ensure session is valid to prevent 401 Unauthorized
    const { data: { session } } = await supabase.auth.getSession();
    if (!session) {
      setProductSaving(false);
      Alert.alert('Session Expired', 'You must be logged in as an administrator to save catalog changes.');
      return;
    }

    const success = await saveProfile(editingProfile);
    setProductSaving(false);
    if (success) {
      Alert.alert('Saved', `${editingProfile.displayName || editingProfile.id} updated in product catalog.`);
      setEditingProfile(null);
    } else {
      Alert.alert('Save Failed', 'Could not write to Supabase. Check your connection.');
    }
  };

  const patchEdit = (patch: Partial<ProductProfile>) =>
    setEditingProfile(prev => prev ? { ...prev, ...patch } : prev);

  const renderProductsTab = () => {
    const fieldStyle = {
      backgroundColor: '#222', borderRadius: 6, paddingHorizontal: 10,
      paddingVertical: 8, color: '#FFF', fontSize: 13, marginBottom: 8,
      borderWidth: 1, borderColor: 'rgba(255,90,0,0.3)',
    };
    const labelStyle = { color: '#AAA', fontSize: 11, fontWeight: '600' as const, marginBottom: 2, marginTop: 6 };

    // Auto-select first profile if none is active
    const activeProfile = editingProfile || allProfiles[0];

    const ProductSelectorSlider = () => (
      <View style={{ marginBottom: 12 }}>
        <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={{ paddingHorizontal: 16, paddingVertical: 12, gap: 10 }}>
          {allProfiles.map(p => {
            const isActive = activeProfile?.id === p.id;
            return (
              <TouchableOpacity key={p.id} onPress={() => setEditingProfile({ ...p })}
                style={{ paddingHorizontal: 16, paddingVertical: 8, borderRadius: 20, backgroundColor: isActive ? '#FF5A00' : '#333' }}>
                <View style={{ flexDirection: 'row', alignItems: 'center', gap: 6 }}>
                  {p.brandIcon && <MaterialCommunityIcons name={p.brandIcon as any} size={14} color="#FFF" />}
                  <Text style={{ color: '#FFF', fontWeight: isActive ? '800' : '600' }}>{p.displayName || p.id}</Text>
                </View>
              </TouchableOpacity>
            );
          })}
          <TouchableOpacity onPress={() => setEditingProfile(blankProfile())}
            style={{ paddingHorizontal: 16, paddingVertical: 8, borderRadius: 20, borderWidth: 1, borderColor: '#FF5A00', borderStyle: 'dashed' }}>
            <Text style={{ color: '#FF5A00', fontWeight: '800' }}>+ ADD PRODUCT</Text>
          </TouchableOpacity>
        </ScrollView>
      </View>
    );

    if (activeProfile) {
      return (
        <View style={{ flex: 1 }}>
          <ProductSelectorSlider />
          <ScrollView style={{ flex: 1, paddingHorizontal: 16 }}>
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 }}>
              <Text style={{ color: '#FFF', fontWeight: '800', fontSize: 17 }}>
                {activeProfile.id ? `Catalog Entry: ${activeProfile.id}` : 'New Product'}
              </Text>
            </View>

          {/* Live Shape Preview */}
          <Text style={{ color: '#FF5A00', fontWeight: '700', fontSize: 12, marginBottom: 4, letterSpacing: 1 }}>LIVE SHAPE PREVIEW</Text>
          <ShapePreviewCanvas profile={activeProfile} />

          {/* Viz Shape Selector */}
          <Text style={labelStyle}>SHAPE TYPE</Text>
          <View style={{ flexDirection: 'row', gap: 8, marginBottom: 8 }}>
            {(['RING', 'OVAL', 'DUAL_STRIP'] as VizShape[]).map(s => (
              <TouchableOpacity
                key={s}
                onPress={() => patchEdit({ vizShape: s })}
                style={{
                  paddingHorizontal: 12, paddingVertical: 7, borderRadius: 6,
                  backgroundColor: activeProfile.vizShape === s ? '#FF5A00' : '#333',
                  borderWidth: 1, borderColor: activeProfile.vizShape === s ? '#FF5A00' : '#555',
                }}
              >
                <Text style={{ color: '#FFF', fontSize: 12, fontWeight: '700' }}>{s}</Text>
              </TouchableOpacity>
            ))}
          </View>

          {/* Product Identity */}
          <Text style={labelStyle}>PRODUCT ID (e.g. RAILZ)</Text>
          <TextInput
            style={fieldStyle as any} value={activeProfile.id}
            onChangeText={v => patchEdit({ id: v.toUpperCase() })}
            placeholder="RAILZ" placeholderTextColor="#555" autoCapitalize="characters"
          />
           <TextInput
            style={fieldStyle as any} value={activeProfile.displayName}
            onChangeText={v => patchEdit({ displayName: v })}
            placeholder="RAILZ™" placeholderTextColor="#555"
          />
          <Text style={labelStyle}>BRAND ICON (MaterialCommunityIcons)</Text>
          <TextInput
            style={fieldStyle as any} value={activeProfile.brandIcon}
            onChangeText={v => patchEdit({ brandIcon: v })}
            placeholder="circle-double" placeholderTextColor="#555"
          />
          <Text style={labelStyle}>BRAND THEME COLOR (HEX)</Text>
          <TextInput
            style={fieldStyle as any} value={activeProfile.vizThemeColor}
            onChangeText={v => patchEdit({ vizThemeColor: v })}
            placeholder="#FF5A00" placeholderTextColor="#555"
          />

          {/* Hardware Defaults */}
          <Text style={[labelStyle, { color: '#FF5A00', marginTop: 14 }]}>HARDWARE DEFAULTS (0x62 FLASH)</Text>
          <Text style={labelStyle}>DEFAULT LED POINTS</Text>
          <TextInput style={fieldStyle as any} value={String(activeProfile.defaultLedPoints)}
            onChangeText={v => patchEdit({ defaultLedPoints: parseInt(v) || 0 })} keyboardType="numeric"
          />
          <Text style={labelStyle}>DEFAULT SEGMENTS</Text>
          <TextInput style={fieldStyle as any} value={String(activeProfile.defaultSegments)}
            onChangeText={v => patchEdit({ defaultSegments: parseInt(v) || 1 })} keyboardType="numeric"
          />
          <Text style={labelStyle}>IC TYPE (1=WS2812B, 2=SM16703)</Text>
          <TextInput style={fieldStyle as any} value={String(activeProfile.defaultIcType)}
            onChangeText={v => patchEdit({ defaultIcType: parseInt(v) || 1 })} keyboardType="numeric"
          />
          <Text style={labelStyle}>COLOR SORTING (2=GRB)</Text>
          <TextInput style={fieldStyle as any} value={String(activeProfile.defaultColorSorting)}
            onChangeText={v => patchEdit({ defaultColorSorting: parseInt(v) || 2 })} keyboardType="numeric"
          />

          {/* FTUE Thresholds */}
          <Text style={[labelStyle, { color: '#FF5A00', marginTop: 14 }]}>AUTO-DETECT THRESHOLDS (FTUE)</Text>
          <Text style={labelStyle}>DETECT MIN POINTS (LED count lower bound)</Text>
          <TextInput style={fieldStyle as any} value={String(activeProfile.detectMinPoints)}
            onChangeText={v => patchEdit({ detectMinPoints: parseInt(v) || 0 })} keyboardType="numeric"
          />
          <Text style={labelStyle}>DETECT MAX POINTS (LED count upper bound)</Text>
          <TextInput style={fieldStyle as any} value={String(activeProfile.detectMaxPoints)}
            onChangeText={v => patchEdit({ detectMaxPoints: parseInt(v) || 0 })} keyboardType="numeric"
          />

          {/* Visualizer Geometry */}
          <Text style={[labelStyle, { color: '#FF5A00', marginTop: 14 }]}>VISUALIZER GEOMETRY</Text>
          <Text style={labelStyle}>DEFAULT POINTS (visualizer fallback)</Text>
          <TextInput style={fieldStyle as any} value={String(activeProfile.vizDefaultPoints)}
            onChangeText={v => patchEdit({ vizDefaultPoints: parseInt(v) || 16 })} keyboardType="numeric"
          />
          <Text style={labelStyle}>BLOB DIAMETER MM (LED chip size)</Text>
          <TextInput style={fieldStyle as any} value={String(activeProfile.vizBlobDiameterMm)}
            onChangeText={v => patchEdit({ vizBlobDiameterMm: parseFloat(v) || 5.7 })} keyboardType="numeric"
          />
          <Text style={labelStyle}>CANVAS WIDTH (scale units)</Text>
          <TextInput style={fieldStyle as any} value={String(activeProfile.vizBaseWidth)}
            onChangeText={v => patchEdit({ vizBaseWidth: parseInt(v) || 55 })} keyboardType="numeric"
          />
          <Text style={labelStyle}>CANVAS HEIGHT (scale units)</Text>
          <TextInput style={fieldStyle as any} value={String(activeProfile.vizBaseHeight)}
            onChangeText={v => patchEdit({ vizBaseHeight: parseInt(v) || 115 })} keyboardType="numeric"
          />

          {/* DUAL_STRIP only */}
          {activeProfile.vizShape === 'DUAL_STRIP' && (
            <>
              <Text style={[labelStyle, { color: '#FF5A00', marginTop: 14 }]}>DUAL STRIP GEOMETRY (RAILZ)</Text>
              <Text style={labelStyle}>STRIP COUNT</Text>
              <TextInput style={fieldStyle as any} value={String(activeProfile.vizStripCount ?? 2)}
                onChangeText={v => patchEdit({ vizStripCount: parseInt(v) || 2 })} keyboardType="numeric"
              />
              <Text style={labelStyle}>STRIP SEPARATION (gap between rails, scale units)</Text>
              <TextInput style={fieldStyle as any} value={String(activeProfile.vizStripSeparation ?? 32)}
                onChangeText={v => patchEdit({ vizStripSeparation: parseFloat(v) || 32 })} keyboardType="numeric"
              />
              <Text style={labelStyle}>ORIENTATION</Text>
              <View style={{ flexDirection: 'row', gap: 8, marginBottom: 8 }}>
                {(['VERTICAL', 'HORIZONTAL'] as const).map(o => (
                  <TouchableOpacity key={o} onPress={() => patchEdit({ vizStripOrientation: o })}
                    style={{ paddingHorizontal: 12, paddingVertical: 7, borderRadius: 6,
                      backgroundColor: activeProfile.vizStripOrientation === o ? '#FF5A00' : '#333',
                      borderWidth: 1, borderColor: activeProfile.vizStripOrientation === o ? '#FF5A00' : '#555' }}
                  >
                    <Text style={{ color: '#FFF', fontSize: 12, fontWeight: '700' }}>{o}</Text>
                  </TouchableOpacity>
                ))}
              </View>
            </>
          )}

            <TouchableOpacity
              onPress={() => {
                 handleSaveProfile();
              }}
              disabled={productSaving}
              style={{ backgroundColor: '#FF5A00', paddingVertical: 14, borderRadius: 8, alignItems: 'center', marginTop: 16, marginBottom: 40, opacity: productSaving ? 0.6 : 1 }}
            >
              <Text style={{ color: '#FFF', fontWeight: '800', fontSize: 15 }}>
                {productSaving ? 'SAVING...' : '💾  SAVE TO CATALOG'}
              </Text>
            </TouchableOpacity>
          </ScrollView>
        </View>
      );
    }

    return null;
  };

  const timelineLogs = logs.filter(l => l.e !== 'RAW_PAYLOAD');

  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen" onRequestClose={onClose}>
      <SafeAreaView style={[styles.root, { backgroundColor: bg }]}>
        {/* Header */}
        <View style={[styles.modalHeader, { borderBottomColor: borderColor }]}>
          <View style={{ flexShrink: 1, paddingRight: 8 }}>
            <Text style={[styles.title, { color: textPrimary }]} numberOfLines={1}>Admin Tools</Text>
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
          {(['timeline', 'stats', 'device', 'tools'] as Tab[]).map(t => (
            <TouchableOpacity
              key={t}
              onPress={() => setTab(t)}
              style={[styles.tabBtn, tab === t && styles.tabBtnActive]}
            >
              <Text style={[styles.tabLabel, { color: tab === t ? '#FF5A00' : textMuted }]}>
                {t === 'device' ? 'Device' : t === 'tools' ? 'Tools' : t.charAt(0).toUpperCase() + t.slice(1)}
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
        {tab === 'device' && renderDeviceTab()}
        {tab === 'stats' && renderStatsTab()}
        {tab === 'tools' && renderAdminTab()}
        {tab === 'products' && renderProductsTab()}
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
  root: { flex: 1 },
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

