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
import React, { useState, useEffect, useCallback } from 'react';
import {
  View, Text, StyleSheet, Modal, TouchableOpacity, ScrollView,
  Alert, FlatList, Platform, SafeAreaView, Switch, TextStyle, ViewStyle, TextInput
} from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import * as Device from 'expo-device';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useAdminTelemetry, EVENT_META, formatLogTime, getPayloadSummary, TelemetryStats } from '../hooks/useAdminTelemetry';
import { useProductManager } from '../hooks/useProductManager';
import { useAdminSettings } from '../hooks/useAdminSettings';
import { useTheme } from '../context/ThemeContext';
import { LogEntry, EventType } from '../services/AppLogger';
import AdminPicksScheduler from './AdminPicksScheduler';

type Tab = 'timeline' | 'stats' | 'device' | 'tools';

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
  writeToDevice?: (data: number[], deviceId?: string) => Promise<void | boolean>;
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
  const [isPicksSchedulerVisible, setIsPicksSchedulerVisible] = useState(false);
  const [isAppManagerVisible, setIsAppManagerVisible] = useState(false);
  const [confirmDeleteVisible, setConfirmDeleteVisible] = useState(false);

  // ── Domain Hooks ────────────────────────────────────────────────────────────
  const { 
    logs, stats, isUploading, clearLogs, uploadLogs, exportLogs 
  } = useAdminTelemetry(visible);
  
  const { 
    allProfiles, editingProfile, isSaving: productSaving, 
    startEditing, createNew, patchEdit, saveProduct 
  } = useProductManager();

  const { 
    appSettings, updateSetting 
  } = useAdminSettings(isAppManagerVisible);

  const handlePolicyToggle = (key: string, nextValue: boolean, title: string, message: string) => {
    if (nextValue) {
      Alert.alert(title, message, [
        { text: "Cancel", style: "cancel" },
        { text: "Enforce", style: "destructive", onPress: () => updateSetting(key, nextValue) }
      ]);
    } else {
      updateSetting(key, nextValue);
    }
  };

  const [deviceConfigs, setDeviceConfigs] = useState<Record<string, any>>({});

  useEffect(() => {
    const loadConfigs = async () => {
      try {
        const stored = await AsyncStorage.getItem('ng_device_configs');
        if (stored) setDeviceConfigs(JSON.parse(stored) || {});
      } catch(e) {}
    };
    if (visible) loadConfigs();
  }, [visible]);

  const handleExport = async () => {
    await exportLogs();
  };

  const handleClear = () => {
    setConfirmDeleteVisible(true);
  };

  const executeClear = async () => {
    await clearLogs();
    if (onClearAll) onClearAll();
    setConfirmDeleteVisible(false);
  };

  const handleUpload = async () => {
    try {
      await uploadLogs();
      Alert.alert('Upload Complete', 'Logs successfully uploaded to sk8lytz-logs bucket on Supabase.');
    } catch (err: any) {
      Alert.alert('Upload Failed', err?.message || String(err));
    }
  };

  const Colors = {
    background: isDark ? '#0A0C12' : '#F5F5F5',
    surface: isDark ? '#141829' : '#FFFFFF',
    surfaceHighlight: isDark ? '#252c47' : '#E0E0E0',
    text: isDark ? '#FFFFFF' : '#111111',
    textMuted: isDark ? '#8a96b3' : '#555555',
    primary: '#00f0ff',
    secondary: '#FF5A00',
    error: '#ff4040',
  };
  const bg = Colors.background;
  const cardBg = Colors.surface;
  const textPrimary = Colors.text;
  const textMuted = Colors.textMuted;
  const borderColor = Colors.surfaceHighlight;

  const memoizedTimelineLogs = React.useMemo(() => logs.filter(l => l.e !== 'RAW_PAYLOAD'), [logs]);

  const renderLogItem = useCallback(({ item }: { item: LogEntry }) => {
    const meta = EVENT_META[item.e as EventType] || { icon: 'information', color: '#888', label: item.e };
    return (
      <View style={[styles.logRow, { borderBottomColor: borderColor }]}>
        <MaterialCommunityIcons name={meta.icon as any} size={18} color={meta.color} style={styles.logIcon} />
        <View style={styles.logBody}>
          <View style={styles.logHeader}>
            <Text style={[styles.logType, { color: meta.color }]}>{meta.label}</Text>
            <Text style={[styles.logTime, { color: textMuted }]}>{formatLogTime(item.t)}</Text>
          </View>
          <Text style={[styles.logPayload, { color: textMuted }]} numberOfLines={1}>
            {getPayloadSummary(item)}
          </Text>
          {item.e === 'COLOR_CHANGED' && item.d.hex && (
            <View style={[styles.colorSwatch, { backgroundColor: item.d.hex }]} />
          )}
        </View>
      </View>
    );
  }, [borderColor, textMuted]);

  const renderContent = () => {
    switch (tab) {
      case 'timeline':
        return (
          <FlatList
            data={memoizedTimelineLogs}
            renderItem={renderLogItem}
            keyExtractor={(item, index) => item.t.toString() + index}
            initialNumToRender={20}
            maxToRenderPerBatch={10}
            windowSize={5}
            removeClippedSubviews={Platform.OS === 'android'}
            ListEmptyComponent={<Text style={[styles.emptyText, { color: textMuted }]}>No events captured.</Text>}
          />
        );
      case 'stats':
        return <StatsTab stats={stats} textMuted={textMuted} textPrimary={textPrimary} cardBg={cardBg} borderColor={borderColor} />;
      case 'device':
        return <DeviceTab logs={logs} deviceConfigs={deviceConfigs} textMuted={textMuted} textPrimary={textPrimary} cardBg={cardBg} borderColor={borderColor} />;
      case 'tools':
        return (
          <AdminTab 
            onOpenProgrammer={onOpenProgrammer} 
            onOpenLab={onOpenLab} 
            setIsPicksSchedulerVisible={setIsPicksSchedulerVisible}
            setIsProductManagerVisible={setIsProductManagerVisible}
            setIsAppManagerVisible={setIsAppManagerVisible}
            textMuted={textMuted} 
            textPrimary={textPrimary} 
            cardBg={cardBg} 
            borderColor={borderColor} 
          />
        );
      default:
        return null;
    }
  };

  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen" onRequestClose={onClose}>
      <SafeAreaView style={[styles.root, { backgroundColor: bg }]}>
        {/* Header */}
        <View style={[styles.modalHeader, { borderBottomColor: borderColor }]}>
          <View style={{ flexShrink: 1, paddingRight: 8 }}>
            <Text style={[styles.title, { color: textPrimary }]} numberOfLines={1}>Admin Tools</Text>
            <Text style={[styles.subtitle, { color: textMuted }]} numberOfLines={1}>{memoizedTimelineLogs.length} events stored</Text>
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
              style={[styles.tabBtn, tab === t && { borderBottomColor: Colors.primary }]}
            >
              <Text style={[styles.tabLabel, { color: tab === t ? Colors.primary : textMuted }]}>
                {t === 'device' ? 'Device' : t === 'tools' ? 'Tools' : t.charAt(0).toUpperCase() + t.slice(1)}
              </Text>
            </TouchableOpacity>
          ))}
        </View>

        {/* Content */}
        <View style={{ flex: 1 }}>
          {renderContent()}
        </View>
      </SafeAreaView>

      {/* ── Confirm Delete Modal ── */}
      <Modal visible={confirmDeleteVisible} transparent animationType="fade">
        <View style={{ flex: 1, backgroundColor: 'rgba(0,0,0,0.6)', justifyContent: 'center', alignItems: 'center', padding: 20 }}>
          <View style={{ backgroundColor: isDark ? '#1A1A1A' : '#FFF', padding: 24, borderRadius: 12, width: '100%', maxWidth: 400, borderColor: '#ff4040', borderWidth: 1 }}>
            <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 16 }}>
              <MaterialCommunityIcons name="alert" size={24} color="#ff4040" />
              <Text style={{ fontSize: 18, fontWeight: '800', color: isDark ? '#FFF' : '#000', marginLeft: 8 }}>Purge Telemetry Logs</Text>
            </View>
            <Text style={{ fontSize: 14, color: isDark ? '#CCC' : '#444', marginBottom: 24, lineHeight: 20 }}>
              Are you sure you want to completely erase all timeline, device, and analytics stats from local memory? This action cannot be reversed.
            </Text>
            <View style={{ flexDirection: 'row', justifyContent: 'flex-end', gap: 12 }}>
              <TouchableOpacity onPress={() => setConfirmDeleteVisible(false)} style={{ paddingVertical: 10, paddingHorizontal: 16, borderRadius: 6, backgroundColor: isDark ? '#333' : '#EEE' }}>
                <Text style={{ fontWeight: '700', color: isDark ? '#FFF' : '#000' }}>Cancel</Text>
              </TouchableOpacity>
              <TouchableOpacity onPress={executeClear} style={{ paddingVertical: 10, paddingHorizontal: 16, borderRadius: 6, backgroundColor: '#ff4040' }}>
                <Text style={{ fontWeight: '700', color: '#FFF' }}>Erase Everything</Text>
              </TouchableOpacity>
            </View>
          </View>
        </View>
      </Modal>

      {/* ── App Manager Modal ── */}
      <Modal visible={isAppManagerVisible} animationType="slide" presentationStyle="fullScreen" onRequestClose={() => setIsAppManagerVisible(false)}>
        <SafeAreaView style={{ flex: 1, backgroundColor: bg }}>
          <View style={{ flexDirection: 'row', alignItems: 'center', padding: 16, borderBottomWidth: 1, borderBottomColor: borderColor, backgroundColor: cardBg }}>
            <TouchableOpacity onPress={() => setIsAppManagerVisible(false)} style={{ marginRight: 16, padding: 4 }}>
              <MaterialCommunityIcons name="arrow-left" size={24} color="#9D4EFF" />
            </TouchableOpacity>
            <View style={{ flex: 1 }}>
              <Text style={[styles.title, { color: textPrimary }]}>🛡️ APP MANAGER</Text>
              <Text style={{ color: textMuted, fontSize: 11, marginTop: 2 }}>Master governance engine & policy overrides</Text>
            </View>
          </View>
          <ScrollView style={{ flex: 1, padding: 16 }}>
            <Text style={{ color: textMuted, fontSize: 12, marginBottom: 16 }}>
              These policies are globally enforced. Safety locks are required for critical changes.
            </Text>

            <Text style={{ color: textPrimary, fontWeight: 'bold', marginBottom: 8, marginTop: 8 }}>Crew Hub Governance</Text>
            <View style={{ backgroundColor: cardBg, borderWidth: 1, borderColor: borderColor, borderRadius: 12, marginBottom: 16, overflow: 'hidden' }}>
              <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: 16, borderBottomWidth: 1, borderBottomColor: borderColor }}>
                <View style={{ flex: 1, marginRight: 16 }}>
                  <Text style={{ color: textPrimary, fontSize: 15, fontWeight: '700' }}>Global Lock</Text>
                  <Text style={{ color: textMuted, fontSize: 12, marginTop: 2 }}>Completely disable the Crew Hub for everyone.</Text>
                </View>
                <Switch
                  value={appSettings['global_crew_hub_locked'] === true}
                  onValueChange={(v) => handlePolicyToggle('global_crew_hub_locked', v, 'Lock Crew Hub?', 'This disables the entire social network. Proceed?')}
                  trackColor={{ false: '#444', true: '#FF4444' }}
                />
              </View>
              <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: 16 }}>
                <View style={{ flex: 1, marginRight: 16 }}>
                  <Text style={{ color: textPrimary, fontSize: 15, fontWeight: '700' }}>Hide When Offline</Text>
                  <Text style={{ color: textMuted, fontSize: 12, marginTop: 2 }}>Hide the tab completely if the device drops connection.</Text>
                </View>
                <Switch
                  value={appSettings['offline_crew_hub_hidden'] === true}
                  onValueChange={(v) => updateSetting('offline_crew_hub_hidden', v)}
                  trackColor={{ false: '#444', true: '#00f0ff' }}
                />
              </View>
            </View>

            <Text style={{ color: textPrimary, fontWeight: 'bold', marginBottom: 8 }}>Community Hub Governance</Text>
            <View style={{ backgroundColor: cardBg, borderWidth: 1, borderColor: borderColor, borderRadius: 12, marginBottom: 16, overflow: 'hidden' }}>
              <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: 16, borderBottomWidth: 1, borderBottomColor: borderColor }}>
                <View style={{ flex: 1, marginRight: 16 }}>
                  <Text style={{ color: textPrimary, fontSize: 15, fontWeight: '700' }}>Global Lock</Text>
                  <Text style={{ color: textMuted, fontSize: 12, marginTop: 2 }}>Disable Community Picks and Favorites.</Text>
                </View>
                <Switch
                  value={appSettings['global_community_hub_locked'] === true}
                  onValueChange={(v) => handlePolicyToggle('global_community_hub_locked', v, 'Lock Community?', 'This disables global community picks. Proceed?')}
                  trackColor={{ false: '#444', true: '#FF4444' }}
                />
              </View>
              <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: 16 }}>
                <View style={{ flex: 1, marginRight: 16 }}>
                  <Text style={{ color: textPrimary, fontSize: 15, fontWeight: '700' }}>Hide When Offline</Text>
                  <Text style={{ color: textMuted, fontSize: 12, marginTop: 2 }}>Remove access to cached Community picks.</Text>
                </View>
                <Switch
                  value={appSettings['offline_community_hub_hidden'] === true}
                  onValueChange={(v) => updateSetting('offline_community_hub_hidden', v)}
                  trackColor={{ false: '#444', true: '#00f0ff' }}
                />
              </View>
            </View>

            <Text style={{ color: textPrimary, fontWeight: 'bold', marginBottom: 8 }}>Maps & Infrastructure</Text>
            <View style={{ backgroundColor: cardBg, borderWidth: 1, borderColor: borderColor, borderRadius: 12, marginBottom: 16, overflow: 'hidden' }}>
              <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: 16, borderBottomWidth: 1, borderBottomColor: borderColor }}>
                <View style={{ flex: 1, marginRight: 16 }}>
                  <Text style={{ color: textPrimary, fontSize: 15, fontWeight: '700' }}>Lock Skate Maps</Text>
                  <Text style={{ color: textMuted, fontSize: 12, marginTop: 2 }}>Disable all mapping functionality.</Text>
                </View>
                <Switch
                  value={appSettings['global_maps_locked'] === true}
                  onValueChange={(v) => handlePolicyToggle('global_maps_locked', v, 'Lock Skate Maps?', 'This disables location and map features. Proceed?')}
                  trackColor={{ false: '#444', true: '#FF4444' }}
                />
              </View>
              <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: 16, borderBottomWidth: 1, borderBottomColor: borderColor }}>
                <View style={{ flex: 1, marginRight: 16 }}>
                  <Text style={{ color: textPrimary, fontSize: 15, fontWeight: '700' }}>Hide When Offline</Text>
                  <Text style={{ color: textMuted, fontSize: 12, marginTop: 2 }}>Remove Maps access when offline.</Text>
                </View>
                <Switch
                  value={appSettings['offline_maps_hidden'] === true}
                  onValueChange={(v) => updateSetting('offline_maps_hidden', v)}
                  trackColor={{ false: '#444', true: '#00f0ff' }}
                />
              </View>
              <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', padding: 16 }}>
                <View style={{ flex: 1, marginRight: 16 }}>
                  <Text style={{ color: textPrimary, fontSize: 15, fontWeight: '700' }}>Telemetry Uploads</Text>
                  <Text style={{ color: textMuted, fontSize: 12, marginTop: 2 }}>Enable or disable crash telemetry ingest.</Text>
                </View>
                <Switch
                  value={appSettings['global_telemetry_enabled'] === true}
                  onValueChange={(v) => updateSetting('global_telemetry_enabled', v)}
                  trackColor={{ false: '#444', true: '#00f0ff' }}
                />
              </View>
            </View>

            <Text style={{ color: textPrimary, fontWeight: 'bold', marginBottom: 8 }}>Legal Compliance</Text>
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: cardBg, borderWidth: 1, borderColor: borderColor, padding: 16, borderRadius: 12, marginBottom: 32 }}>
              <View style={{ flex: 1, marginRight: 16 }}>
                <Text style={{ color: textPrimary, fontSize: 15, fontWeight: '700' }}>Required EULA Version</Text>
                <Text style={{ color: textMuted, fontSize: 12, marginTop: 2 }}>Current required target: v{appSettings['required_eula_version'] || '1'}. Users with lower versions will be gated.</Text>
              </View>
              <TouchableOpacity 
                style={{ backgroundColor: '#FF4444', paddingVertical: 10, paddingHorizontal: 12, borderRadius: 8, alignItems: 'center' }}
                onPress={() => {
                  const current = parseInt(appSettings['required_eula_version'] || '1', 10);
                  const next = current + 1;
                  Alert.alert(
                    "Bump EULA Version?",
                    `Are you sure you want to enforce v${next}? All active users must re-accept the terms.`,
                    [
                      { text: "Cancel", style: "cancel" },
                      { text: "Enforce Version", style: "destructive", onPress: () => updateSetting('required_eula_version', next.toString()) }
                    ]
                  );
                }}
              >
                <Text style={{ color: '#FFF', fontWeight: 'bold', fontSize: 13 }}>BUMP v{parseInt(appSettings['required_eula_version'] || '1', 10) + 1}</Text>
              </TouchableOpacity>
            </View>
          </ScrollView>
        </SafeAreaView>
      </Modal>

      {/* ── Picks Scheduler Modal ── */}
      <AdminPicksScheduler visible={isPicksSchedulerVisible} onClose={() => setIsPicksSchedulerVisible(false)} />

      {/* ── Product Manager Modal ── */}
      <Modal visible={isProductManagerVisible} animationType="slide" presentationStyle="fullScreen" onRequestClose={() => setIsProductManagerVisible(false)}>
        <SafeAreaView style={{ flex: 1, backgroundColor: bg }}>
          <View style={{ flexDirection: 'row', alignItems: 'center', padding: 16, borderBottomWidth: 1, borderBottomColor: borderColor, backgroundColor: cardBg }}>
            <TouchableOpacity onPress={() => setIsProductManagerVisible(false)} style={{ marginRight: 16, padding: 4 }}>
              <MaterialCommunityIcons name="arrow-left" size={24} color="#FF5A00" />
            </TouchableOpacity>
            <View style={{ flex: 1 }}>
              <Text style={[styles.title, { color: textPrimary }]}>📦 PRODUCT MANAGER</Text>
              <Text style={{ color: textMuted, fontSize: 11, marginTop: 2 }}>Edit hardware product catalog entries</Text>
            </View>
          </View>
          <ProductsTab 
            allProfiles={allProfiles}
            editingProfile={editingProfile}
            startEditing={startEditing}
            createNew={createNew}
            patchEdit={patchEdit}
            saveProduct={saveProduct}
            productSaving={productSaving}
            textMuted={textMuted}
            textPrimary={textPrimary}
            cardBg={cardBg}
            borderColor={borderColor}
          />
        </SafeAreaView>
      </Modal>
    </Modal>
  );
}


interface TabProps {
  textMuted: string;
  textPrimary: string;
  cardBg: string;
  borderColor: string;
}

/**
 * DeviceTab — Renders a list of all devices ever seen by the app with connection metadata.
 */
interface DeviceTabProps extends TabProps {
  logs: LogEntry[];
  deviceConfigs: Record<string, any>;
}

const DeviceTab = React.memo(({ logs, deviceConfigs, textMuted, textPrimary, cardBg, borderColor }: DeviceTabProps) => {
  const earliest = new Map<string, number>();
  const latest = new Map<string, number>();
  const uniqueMeta = new Map<string, any>();
  const firmwares = new Map<string, string>();

  logs.forEach((l: any) => {
    if (l.e === 'DEVICE_CONNECTED' && l.d.id && l.d.firmware) {
      if (!firmwares.has(l.d.id)) firmwares.set(l.d.id, l.d.firmware);
    }
    if (l.e === 'DEVICE_DISCOVERED' || l.e === 'DEVICE_CONNECTED') {
      const id = l.d.id;
      if (!id) return;
      if (!latest.has(id)) latest.set(id, l.t);
      earliest.set(id, l.t);
      if (!uniqueMeta.has(id) && l.e === 'DEVICE_DISCOVERED') uniqueMeta.set(id, l.d);
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
                {meta?.rssi && <Text style={[styles.deviceDetail, { color: textMuted }]}>\u00b7 RSSI: {meta.rssi}</Text>}
              </View>
              <View style={{ borderTopWidth: 1, borderTopColor: borderColor, marginTop: 6, paddingTop: 4 }}>
                <Text style={[styles.deviceDetail, { color: textMuted }]}>First seen: {formatLogTime(earliest.get(id)!)}</Text>
                <Text style={[styles.deviceDetail, { color: textMuted }]}>Last seen: {formatLogTime(latest.get(id)!)}</Text>
              </View>
            </View>
          </View>
        );
      })}
    </ScrollView>
  );
});

/**
 * StatsTab — Renders aggregated telemetry charts and usage maps.
 */
interface StatsTabProps extends TabProps {
  stats: TelemetryStats | null;
}

const StatsTab = React.memo(({ stats, textMuted, textPrimary, cardBg, borderColor }: StatsTabProps) => {
  if (!stats) return null;
  const gbMem = Device.totalMemory ? (Device.totalMemory / 1024 ** 3).toFixed(1) + ' GB' : 'Unknown';
  const osDisplay = `${Device.osName || Platform.OS} ${Device.osVersion || ''}`.trim();
  const sessionMins = stats.lastAppOpenedTime ? Math.round((Date.now() - stats.lastAppOpenedTime) / 60000) : 0;

  return (
    <ScrollView style={styles.tabContent}>
      <Text style={[styles.statSection, { color: textPrimary }]}>\ud83d\udcf1 Device & App Telemetry</Text>
      <View style={[styles.statCard, { backgroundColor: cardBg, borderColor }]}>
        <StatRow label="Brand / Model" value={`${Device.brand || ''} ${Device.modelName || 'Unknown'}`.trim()} color={textPrimary} muted={textMuted} />
        <StatRow label="Manufacturer" value={Device.manufacturer || 'Unknown'} color={textPrimary} muted={textMuted} />
        <StatRow label="Operating System" value={osDisplay} color={textPrimary} muted={textMuted} />
        <StatRow label="Battery Level" value={stats.batteryLevel !== undefined && stats.batteryLevel !== -1 ? `${Math.round(stats.batteryLevel * 100)}%` : 'Unknown'} color={textPrimary} muted={textMuted} />
        <StatRow label="BLE Target MAC" value={stats.primaryBleMac || 'N/A'} color="#00f0ff" muted={textMuted} />
        <StatRow label="Total RAM" value={gbMem} color={textPrimary} muted={textMuted} />
        <StatRow label="Log Storage" value={`${(stats.storageBytesEstimate / 1024).toFixed(2)} KB`} color={textPrimary} muted={textMuted} />
      </View>
      <Text style={[styles.statSection, { color: textPrimary }]}>\ud83d\udcca Analytics Overview</Text>
      <View style={[styles.statCard, { backgroundColor: cardBg, borderColor }]}>
        <StatRow label="Total Events Logged" value={String(stats.totalEvents)} color={textPrimary} muted={textMuted} />
      </View>
      <Text style={[styles.statSection, { color: textPrimary }]}>\ud83d\udd9b\ufe0f Mode Usage</Text>
      <View style={[styles.statCard, { backgroundColor: cardBg, borderColor }]}>
        {Object.entries(stats.modeUsage).sort((a,b) => (b[1] as number) - (a[1] as number)).map(([mode, count]) => (
          <StatRow key={mode} label={mode} value={`${count}\u00d7`} color={textPrimary} muted={textMuted} />
        ))}
      </View>
    </ScrollView>
  );
});

/**
 * AdminTab — Renders developer tools and navigation links.
 */
interface AdminTabProps extends TabProps {
  onOpenProgrammer?: () => void;
  onOpenLab?: () => void;
  setIsPicksSchedulerVisible: (v: boolean) => void;
  setIsProductManagerVisible: (v: boolean) => void;
  setIsAppManagerVisible: (v: boolean) => void;
}

const AdminTab = React.memo(({ 
  onOpenProgrammer, onOpenLab, setIsPicksSchedulerVisible, 
  setIsProductManagerVisible, setIsAppManagerVisible, 
  textMuted, textPrimary, cardBg, borderColor 
}: AdminTabProps) => {
  return (
    <ScrollView style={styles.tabContent}>
      <Text style={[styles.statSection, { color: textPrimary }]}>\ud83d\udee0\ufe0f Engineering Tools</Text>
      <TouchableOpacity onPress={onOpenLab} style={[styles.statCard, { backgroundColor: cardBg, borderColor, flexDirection: 'row', alignItems: 'center' }]}>
        <MaterialCommunityIcons name="flask" size={24} color="#FF7000" />
        <View style={{ marginLeft: 12 }}>
          <Text style={{ color: textPrimary, fontWeight: '700' }}>Diagnostic Lab</Text>
          <Text style={{ color: textMuted, fontSize: 12 }}>Low-level GATT explorer & payload builder</Text>
        </View>
      </TouchableOpacity>
      <TouchableOpacity onPress={onOpenProgrammer} style={[styles.statCard, { backgroundColor: cardBg, borderColor, flexDirection: 'row', alignItems: 'center', marginTop: 12 }]}>
        <MaterialCommunityIcons name="chip" size={24} color="#00f0ff" />
        <View style={{ marginLeft: 12 }}>
          <Text style={{ color: textPrimary, fontWeight: '700' }}>Firmware Over-The-Air</Text>
          <Text style={{ color: textMuted, fontSize: 12 }}>Register-level EEPROM flash (0x62)</Text>
        </View>
      </TouchableOpacity>

      <Text style={[styles.statSection, { color: textPrimary, marginTop: 24 }]}>⚙️ Remote Configuration</Text>
      <TouchableOpacity onPress={() => setIsAppManagerVisible(true)} style={[styles.statCard, { backgroundColor: cardBg, borderColor, flexDirection: 'row', alignItems: 'center' }]}>
        <MaterialCommunityIcons name="shield-lock-outline" size={24} color="#9D4EFF" />
        <View style={{ marginLeft: 12 }}>
          <Text style={{ color: textPrimary, fontWeight: '700' }}>App Manager</Text>
          <Text style={{ color: textMuted, fontSize: 12 }}>Master governance and policy compliance</Text>
        </View>
        <MaterialCommunityIcons name="chevron-right" size={20} color={textMuted} style={{ marginLeft: 'auto' }} />
      </TouchableOpacity>
      <TouchableOpacity onPress={() => setIsProductManagerVisible(true)} style={[styles.statCard, { backgroundColor: cardBg, borderColor, flexDirection: 'row', alignItems: 'center', marginTop: 12 }]}>
        <MaterialCommunityIcons name="package-variant-closed" size={24} color="#FF7000" />
        <View style={{ marginLeft: 12 }}>
          <Text style={{ color: textPrimary, fontWeight: '700' }}>Product Manager</Text>
          <Text style={{ color: textMuted, fontSize: 12 }}>Hardware profile catalog editor</Text>
        </View>
        <MaterialCommunityIcons name="chevron-right" size={20} color={textMuted} style={{ marginLeft: 'auto' }} />
      </TouchableOpacity>
      <TouchableOpacity onPress={() => setIsPicksSchedulerVisible(true)} style={[styles.statCard, { backgroundColor: cardBg, borderColor, flexDirection: 'row', alignItems: 'center', marginTop: 12 }]}>
        <MaterialCommunityIcons name="calendar-clock" size={24} color="#00D4AA" />
        <View style={{ marginLeft: 12 }}>
          <Text style={{ color: textPrimary, fontWeight: '700' }}>Picks Scheduler</Text>
          <Text style={{ color: textMuted, fontSize: 12 }}>Schedule and manage community picks</Text>
        </View>
        <MaterialCommunityIcons name="chevron-right" size={20} color={textMuted} style={{ marginLeft: 'auto' }} />
      </TouchableOpacity>
    </ScrollView>
  );
});

function StatRow({ label, value, color, muted }: { label: string; value: string; color: string; muted: string }) {
  return (
    <View style={styles.statRow}>
      <Text style={[styles.statLabel, { color: muted }]}>{label}</Text>
      <Text style={[styles.statValue, { color }]}>{value}</Text>
    </View>
  );
}


/**
 * ProductsTab — Renders the hardware profile catalog with local override editor.
 */
interface ProductsTabProps extends TabProps {
  allProfiles: any[];
  editingProfile: any;
  startEditing: (p: any) => void;
  createNew: () => void;
  patchEdit: (d: any) => void;
  saveProduct: () => Promise<any>;
  productSaving: boolean;
}

const ProductsTab = React.memo(({ 
  allProfiles, editingProfile, startEditing, createNew, patchEdit, saveProduct, productSaving,
  textMuted, textPrimary, cardBg, borderColor 
}: ProductsTabProps) => {

  const fieldWrapperStyle: ViewStyle = { marginBottom: 16, backgroundColor: cardBg, padding: 12, borderRadius: 8, borderLeftWidth: 3, borderLeftColor: '#9D4EFF' };
  const fieldLabelStyle: TextStyle = { color: textMuted, fontSize: 11, fontWeight: '700', textTransform: 'uppercase', marginBottom: 6, letterSpacing: 0.5 };
  const fieldInputStyle: TextStyle = { color: textPrimary, fontSize: 16, padding: 0, fontWeight: '600' };

  const renderField = (label: string, key: string, placeholder: string, keyboardType: any = 'default') => (
    <View style={fieldWrapperStyle}>
      <Text style={fieldLabelStyle}>{label}</Text>
      <TextInput
        style={fieldInputStyle}
        value={editingProfile?.[key]?.toString() || ''}
        onChangeText={v => {
          if (keyboardType === 'numeric') {
            patchEdit({ [key]: v ? Number(v.replace(/[^0-9.-]/g, '')) : 0 });
          } else {
            patchEdit({ [key]: v });
          }
        }}
        placeholder={placeholder}
        placeholderTextColor={textMuted}
        keyboardType={keyboardType}
        autoCapitalize={keyboardType === 'default' ? 'characters' : 'none'}
      />
    </View>
  );

  const renderToggle = (label: string, key: string, desc: string) => (
    <View style={[fieldWrapperStyle, { flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }]}>
      <View style={{ flex: 1, paddingRight: 12 }}>
        <Text style={fieldLabelStyle}>{label}</Text>
        <Text style={{ color: textMuted, fontSize: 11 }}>{desc}</Text>
      </View>
      <Switch 
        value={!!editingProfile?.[key]} 
        onValueChange={v => patchEdit({ [key]: v })}
        trackColor={{ false: '#333', true: '#FF5A00' }}
      />
    </View>
  );

  return (
    <View style={{ flex: 1 }}>
      {/* ── PILL SELECTOR ── */}
      <View style={{ borderBottomWidth: 1, borderBottomColor: borderColor }}>
        <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={{ paddingHorizontal: 16, paddingVertical: 12, gap: 10 }}>
          {allProfiles.map((p: any) => {
            const isActive = editingProfile?.id === p.id;
            return (
              <TouchableOpacity key={p.id} onPress={() => startEditing(p)}
                style={{ paddingHorizontal: 16, paddingVertical: 8, borderRadius: 20, backgroundColor: isActive ? '#9D4EFF' : '#333' }}>
                <View style={{ flexDirection: 'row', alignItems: 'center', gap: 6 }}>
                  {p.brandIcon && <MaterialCommunityIcons name={p.brandIcon as any} size={14} color="#FFF" />}
                  <Text style={{ color: '#FFF', fontWeight: '600' }}>{p.displayName || p.id}</Text>
                </View>
              </TouchableOpacity>
            )
          })}
          <TouchableOpacity onPress={createNew}
            style={{ paddingHorizontal: 16, paddingVertical: 8, borderRadius: 20, borderWidth: 1, borderColor: '#FF5A00', borderStyle: 'dashed', backgroundColor: (editingProfile && !editingProfile.id) ? 'rgba(255,90,0,0.2)' : 'transparent' }}>
            <Text style={{ color: '#FF5A00', fontWeight: '800' }}>+ ADD NEW</Text>
          </TouchableOpacity>
        </ScrollView>
      </View>

      {/* ── EDITOR ── */}
      {editingProfile ? (
        <ScrollView style={{ flex: 1, padding: 16 }}>
          <Text style={{ color: textPrimary, fontSize: 18, fontWeight: '800', marginBottom: 16 }}>
            {editingProfile.id ? 'EDIT PROFILE' : 'NEW HARDWARE PROFILE'}
          </Text>

          {renderField('Product ID (Hardware Ref)', 'id', 'e.g. SK8LYTZ_V2')}
          {renderField('Display Name', 'displayName', 'e.g. SK8Lytz Pro')}
          {renderField('Brand Icon', 'brandIcon', 'e.g. circle-double')}
          {renderField('Brand Color Hex', 'vizThemeColor', 'e.g. #FF5A00')}

          <Text style={{ color: textMuted, fontWeight: '800', marginTop: 12, marginBottom: 8 }}>HARDWARE DEFAULTS</Text>
          {renderField('Default LEDs', 'defaultLedPoints', '16', 'numeric')}
          {renderField('Virtual Segments', 'defaultSegments', '1', 'numeric')}
          {renderField('IC Type (1=WS2812B)', 'defaultIcType', '1', 'numeric')}
          {renderField('Color Sorting (2=GRB)', 'defaultColorSorting', '2', 'numeric')}
          {renderField('Battery Capacity (mAh)', 'batteryCapacityMilliAmpereHour', '3000', 'numeric')}
          {renderToggle('Customizable Profile', 'hardwareAllowsCustomPoints', 'Allow user to cut and set custom length')}

          <Text style={{ color: textMuted, fontWeight: '800', marginTop: 12, marginBottom: 8 }}>AUTODETECT THRESHOLDS</Text>
          {renderField('Min HW Points', 'detectMinPoints', '1', 'numeric')}
          {renderField('Max HW Points', 'detectMaxPoints', '99', 'numeric')}

          <Text style={{ color: textMuted, fontWeight: '800', marginTop: 12, marginBottom: 8 }}>VISUALIZER GEOMETRY</Text>
          {renderField('Canvas Shape', 'vizShape', 'OVAL | RING | DUAL_STRIP')}
          {renderField('Base Width', 'vizBaseWidth', '55', 'numeric')}
          {renderField('Base Height', 'vizBaseHeight', '115', 'numeric')}
          {renderField('Blob Diameter (mm)', 'vizBlobDiameterMm', '5.7', 'numeric')}
          {renderField('Visualizer Default Points', 'vizDefaultPoints', '16', 'numeric')}
          {renderField('Strip Count (Railz)', 'vizStripCount', '2', 'numeric')}
          {renderField('Strip Separation (Railz)', 'vizStripSeparation', '32', 'numeric')}
          {renderField('Strip Orientation', 'vizStripOrientation', 'VERTICAL | HORIZONTAL')}
          {renderToggle('Mirrored Render', 'vizIsMirrored', 'Mirror Seg2 over Seg1')}

          <TouchableOpacity 
            onPress={saveProduct} 
            disabled={productSaving}
            style={{ backgroundColor: '#9D4EFF', padding: 16, borderRadius: 12, alignItems: 'center', marginTop: 20, marginBottom: 40 }}
          >
            <Text style={{ color: '#FFF', fontWeight: '800', fontSize: 15 }}>
              {productSaving ? 'SAVING...' : '\ud83d\udcbe  SAVE TO CATALOG'}
            </Text>
          </TouchableOpacity>
        </ScrollView>
      ) : (
        <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', padding: 32 }}>
          <MaterialCommunityIcons name="cube-outline" size={48} color={borderColor} style={{ marginBottom: 16 }} />
          <Text style={{ color: textMuted, textAlign: 'center', fontWeight: '600' }}>
            Select a hardware profile from the top menu to view or edit its settings.
          </Text>
        </View>
      )}
    </View>
  );
});

const styles = StyleSheet.create({
  root: { flex: 1 },
  modalHeader: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
    paddingHorizontal: 20, paddingBottom: 12, borderBottomWidth: 1,
  },
  title: { fontSize: 18, fontWeight: '900', letterSpacing: 1, textTransform: 'uppercase' },
  subtitle: { fontSize: 11, marginTop: 2 },
  headerActions: { flexDirection: 'row', alignItems: 'center' },
  actionBtn: { padding: 8, marginLeft: 2 },
  tabs: { flexDirection: 'row', borderBottomWidth: 1 },
  tabBtn: { flex: 1, paddingVertical: 12, alignItems: 'center', borderBottomWidth: 2, borderBottomColor: 'transparent' },
  tabLabel: { fontSize: 12, fontWeight: '700', letterSpacing: 0.5, textTransform: 'uppercase' },
  tabContent: { flex: 1, padding: 16 },
  logRow: {
    flexDirection: 'row', alignItems: 'flex-start',
    paddingVertical: 10, paddingHorizontal: 16, borderBottomWidth: StyleSheet.hairlineWidth,
  },
  logIcon: { marginTop: 2, marginRight: 12, width: 20 },
  logBody: { flex: 1 },
  logHeader: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' },
  logType: { fontSize: 13, fontWeight: '700' },
  logTime: { fontSize: 10 },
  logPayload: { fontSize: 12, marginTop: 2 },
  colorSwatch: { width: 16, height: 8, borderRadius: 3, marginTop: 4, borderWidth: 1, borderColor: '#000' },
  emptyText: { textAlign: 'center', fontSize: 14, marginTop: 16 },
  deviceCard: {
    flexDirection: 'row', alignItems: 'center', borderRadius: 12,
    padding: 14, marginBottom: 10, borderWidth: 1,
  },
  deviceName: { fontSize: 15, fontWeight: '700' },
  deviceDetail: { fontSize: 12, marginTop: 2 },
  statSection: { fontSize: 13, fontWeight: '900', marginTop: 16, marginBottom: 8, letterSpacing: 1, textTransform: 'uppercase' },
  statCard: { borderRadius: 12, padding: 14, marginBottom: 4, borderWidth: 1 },
  statRow: { flexDirection: 'row', justifyContent: 'space-between', paddingVertical: 5 },
  statLabel: { fontSize: 13 },
  statValue: { fontSize: 13, fontWeight: '700' },
});

