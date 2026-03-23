import React, { useState, useEffect, useCallback } from 'react';
import {
  View, Text, StyleSheet, Modal, TouchableOpacity, ScrollView,
  Share, Alert, FlatList, Platform,
} from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import * as Device from 'expo-device';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { AppLogger, LogEntry, EventType } from '../services/AppLogger';
import { useTheme } from '../context/ThemeContext';

type Tab = 'timeline' | 'devices' | 'stats';

const EVENT_META: Record<EventType, { icon: string; color: string; label: string }> = {
  APP_OPENED:         { icon: 'cellphone-check', color: '#00f0ff', label: 'App Opened' },
  SCAN_STARTED:       { icon: 'radar',           color: '#FF7000', label: 'Scan Started' },
  SCAN_COMPLETED:     { icon: 'check-circle',    color: '#00ff80', label: 'Scan Completed' },
  DEVICE_DISCOVERED:  { icon: 'bluetooth-connect', color: '#9D4EFF', label: 'Device Found' },
  DEVICE_CONNECTED:   { icon: 'link-variant',    color: '#00ff80', label: 'Connected' },
  DEVICE_DISCONNECTED:{ icon: 'link-variant-off',color: '#ff4040', label: 'Disconnected' },
  MODE_CHANGED:       { icon: 'tune',            color: '#FFD700', label: 'Mode Changed' },
  PATTERN_CHANGED:    { icon: 'shape',           color: '#FF69B4', label: 'Pattern Changed' },
  COLOR_CHANGED:      { icon: 'palette',         color: '#FF7000', label: 'Color Changed' },
  BRIGHTNESS_CHANGED: { icon: 'brightness-5',   color: '#AAFFAA', label: 'Brightness' },
  SPEED_CHANGED:      { icon: 'speedometer',     color: '#AADDFF', label: 'Speed' },
  HARDWARE_CONFIG_CHANGED: { icon: 'memory',     color: '#00E676', label: 'Hardware Settings' },
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
    case 'DEVICE_CONNECTED':  return `${d.name || d.id}`;
    case 'MODE_CHANGED':      return `→ ${d.mode}${d.device ? ` on ${d.device}` : ''}`;
    case 'PATTERN_CHANGED':   return `${d.pattern}${d.mode ? ` [${d.mode}]` : ''}`;
    case 'COLOR_CHANGED':     return `${d.hex}${d.device ? ` on ${d.device}` : ''}`;
    case 'BRIGHTNESS_CHANGED':return `${d.value}%`;
    case 'SPEED_CHANGED':     return `${d.value}%`;
    case 'HARDWARE_CONFIG_CHANGED': return `${d.name || d.id}: ${d.points} LEDs (${d.segments || 1} seg), ${d.stripType}, ${d.sorting}`;
    case 'SCAN_COMPLETED':    return `${d.devicesFound ?? 0} device(s) found`;
    default: return JSON.stringify(d).slice(0, 60);
  }
}

interface LogViewerModalProps {
  visible: boolean;
  onClose: () => void;
}

export default function LogViewerModal({ visible, onClose }: LogViewerModalProps) {
  const { Colors, isDark } = useTheme();
  const [tab, setTab] = useState<Tab>('timeline');
  const [logs, setLogs] = useState<LogEntry[]>([]);
  const [stats, setStats] = useState<any>(null);
  const [deviceConfigs, setDeviceConfigs] = useState<Record<string, any>>({});

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
    Alert.alert('Clear Logs', 'Delete all stored analytics logs?', [
      { text: 'Cancel', style: 'cancel' },
      {
        text: 'Delete', style: 'destructive',
        onPress: async () => { await AppLogger.clearLogs(); load(); }
      },
    ]);
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

    // logs are sorted NEWEST first.
    logs.forEach(l => {
      if (l.e === 'DEVICE_DISCOVERED' || l.e === 'DEVICE_CONNECTED') {
         const id = l.d.id;
         if (!id) return;
         if (!latest.has(id)) latest.set(id, l.t);    // first element encountered is latest
         earliest.set(id, l.t);                       // will continually overwrite until it points to the oldest one
         if (!uniqueMeta.has(id)) uniqueMeta.set(id, l.d); // store newest payload meta
      }
    });

    const uniqueIds = Array.from(uniqueMeta.keys());

    return (
      <ScrollView style={styles.tabContent}>
        {uniqueIds.length === 0 && (
          <Text style={[styles.emptyText, { color: textMuted }]}>No devices logged yet.</Text>
        )}
        {uniqueIds.map((id, i) => {
          const meta = uniqueMeta.get(id);
          const config = deviceConfigs[id] || {};
          const sortingLabel = config.sorting === 'leftToRight' ? 'L→R' : config.sorting === 'rightToLeft' ? 'R→L' : config.sorting;

          return (
            <View key={i} style={[styles.deviceCard, { backgroundColor: cardBg, borderColor }]}>
              <MaterialCommunityIcons name="bluetooth-connect" size={24} color="#9D4EFF" />
              <View style={{ marginLeft: 12, flex: 1 }}>
                <Text style={[styles.deviceName, { color: textPrimary }]}>{config.name || meta.name || 'Unknown'}</Text>
                <Text style={[styles.deviceDetail, { color: textMuted }]}>ID: {id}</Text>
                
                <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 6, marginTop: 4 }}>
                  <Text style={[styles.deviceDetail, { color: textMuted }]}>Type: {config.type || meta.type || '?'}</Text>
                  {meta.rssi && <Text style={[styles.deviceDetail, { color: textMuted }]}>· RSSI: {meta.rssi}</Text>}
                  {(config.points || meta.points) && (
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

  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen" onRequestClose={onClose}>
      <View style={[styles.root, { backgroundColor: bg }]}>
        {/* Header */}
        <View style={[styles.modalHeader, { borderBottomColor: borderColor }]}>
          <View>
            <Text style={[styles.title, { color: textPrimary }]}>SK8Lytz Analytics</Text>
            <Text style={[styles.subtitle, { color: textMuted }]}>{logs.length} events stored</Text>
          </View>
          <View style={styles.headerActions}>
            <TouchableOpacity onPress={handleExport} style={styles.actionBtn}>
              <MaterialCommunityIcons name="export-variant" size={22} color="#00f0ff" />
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
          {(['timeline', 'devices', 'stats'] as Tab[]).map(t => (
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
            data={logs}
            keyExtractor={(_, i) => String(i)}
            renderItem={renderLogItem}
            ListEmptyComponent={
              <Text style={[styles.emptyText, { color: textMuted, margin: 24 }]}>No events logged yet.</Text>
            }
          />
        )}
        {tab === 'devices' && renderDeviceTab()}
        {tab === 'stats' && renderStatsTab()}
      </View>
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
  headerActions: { flexDirection: 'row', gap: 4 },
  actionBtn: { padding: 8 },
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
