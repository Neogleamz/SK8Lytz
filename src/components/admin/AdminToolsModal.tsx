import { MaterialCommunityIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { CONFIGS_KEY } from '../../constants/storageKeys';
/* eslint-disable unused-imports/no-unused-vars */
import React, { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import {
    Alert,
    FlatList,
    Modal,
    Platform,
    StyleSheet,
    Text,
    TouchableOpacity,
    View
} from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useTheme } from '../../context/ThemeContext';
import { useAdminSettings } from '../../hooks/useAdminSettings';
import { EVENT_META, formatLogTime, getPayloadSummary, useAdminTelemetry } from '../../hooks/useAdminTelemetry';
import { useProductManager } from '../../hooks/useProductManager';
import { EventType, LogEntry } from '../../services/appLogger';
import { Spacing } from '../../theme/theme';
import { adminStyles as styles } from './adminStyles';

import { AdminTab } from './AdminTab';
import { ConfirmDeleteModal } from './ConfirmDeleteModal';
import { DeviceTab } from './DeviceTab';
import { StatsTab } from './StatsTab';
import AdminPicksScheduler from './tools/AdminPicksScheduler';
import { AppManager } from './tools/AppManager';
import { ProductManager } from './tools/ProductManager';
import Sk8LytzDiagnosticLab from './tools/Sk8LytzDiagnosticLab';
import Sk8LytzProgrammer from './tools/Sk8LytzProgrammer';
import { UserManagementPanel } from './tools/UserManagementPanel';
import { AdminRosterPanel } from './tools/AdminRosterPanel';
import { AdminAuditLogViewer } from './tools/AdminAuditLogViewer';
import { HardwareBlacklistPanel } from './tools/HardwareBlacklistPanel';
import { FeatureFlagsPanel } from './tools/FeatureFlagsPanel';
import GlobalAnalyticsPanel from './tools/GlobalAnalyticsPanel';


type Tab = 'timeline' | 'stats' | 'device' | 'tools';

export type AdminPanel =
  | 'productManager'
  | 'picksScheduler'
  | 'appManager'
  | 'userManagement'
  | 'programmer'
  | 'lab'
  | 'roster'
  | 'audit'
  | 'blacklist'
  | 'featureFlags'
  | 'globalAnalytics';

export interface AdminToolsModalProps {
  visible: boolean;
  onClose: () => void;
  isDiagnosticsMode?: boolean;
  onToggleDiagnostics?: () => void;
  hwSettings?: import('../../protocols/ZenggeProtocol').HardwareSettings;
  onDisconnectFromDevice?: (id: string) => Promise<void>;
  liveRxPayload?: { deviceId: string; payloadHex: string; timestamp?: number } | null;
  connectedDevices?: { id: string, name: string | null }[];
  allDevices?: import('react-native-ble-plx').Device[];
  bleState?: string;
  handleScan?: () => void;
  onClearAll?: () => void;
  onConnectToDevice?: (device: { id: string; name: string | null; rssi?: number | null }) => Promise<void>;
  liveDeviceConfigs?: Record<string, import('../../types/dashboard.types').DeviceSettings>;
}

const LogItemCard = React.memo(({
  item,
  isDark,
  textMuted,
}: {
  item: LogEntry;
  isDark: boolean;
  textMuted: string;
}) => {
  const meta = EVENT_META[item.e as EventType] || { icon: 'information', color: '#888', label: item.e };
  return (
    <View style={[styles.logRow, isDark ? localStyles.listItemContainerDark : localStyles.listItemContainerLight]}>
      <MaterialCommunityIcons name={meta.icon as keyof typeof MaterialCommunityIcons.glyphMap} size={18} color={meta.color} style={styles.logIcon} />
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
});

export default function AdminToolsModal({ 
  visible, onClose, onClearAll, liveRxPayload, 
  connectedDevices, allDevices, bleState, handleScan, onConnectToDevice, 
  liveDeviceConfigs, isDiagnosticsMode, onToggleDiagnostics, hwSettings, onDisconnectFromDevice 
}: AdminToolsModalProps) {
  const { isDark } = useTheme();
  const [tab, setTab] = useState<Tab>('timeline');
  const [activePanel, setActivePanel] = useState<AdminPanel | null>(null);
  const [confirmDeleteVisible, setConfirmDeleteVisible] = useState(false);

  // ── Domain Hooks ────────────────────────────────────────────────────────────
  const { logs, stats, isUploading, clearLogs, uploadLogs, exportLogs } = useAdminTelemetry(visible);
  
  const { 
    allProfiles, editingProfile, isSaving: productSaving, 
    startEditing, createNew, patchEdit, saveProduct 
  } = useProductManager();

  const { appSettings, updateSetting } = useAdminSettings(activePanel === 'appManager');

  const handlePolicyToggle = useCallback((key: string, nextValue: boolean, title: string, message: string) => {
    if (nextValue) {
      Alert.alert(title, message, [
        { text: "Cancel", style: "cancel" },
        { text: "Enforce", style: "destructive", onPress: () => updateSetting(key, nextValue) }
      ]);
    } else {
      updateSetting(key, nextValue);
    }
  }, [updateSetting]);

  const [deviceConfigs, setDeviceConfigs] = useState<Record<string, import('../../types/dashboard.types').DeviceSettings>>({});
  const loadingConfigsRef = useRef(false);

  useEffect(() => {
    if (!visible || loadingConfigsRef.current) return;
    loadingConfigsRef.current = true;
    let isActive = true;
    const loadConfigs = async () => {
      try {
        const stored = await AsyncStorage.getItem(CONFIGS_KEY);
        if (stored && isActive) setDeviceConfigs(JSON.parse(stored) || {});
      } catch (e: unknown) {
      } finally {
        loadingConfigsRef.current = false;
      }
    };
    loadConfigs();
    return () => { isActive = false; };
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
    } catch (err: unknown) {
      Alert.alert('Upload Failed', err instanceof Error ? err.message : String(err));
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
    return <LogItemCard item={item} isDark={isDark} textMuted={textMuted} />;
  }, [isDark, textMuted]);

  const keyExtractor = useCallback((item: LogEntry, index: number) => item.t.toString() + index, []);

  const renderContent = () => {
    switch (tab) {
      case 'timeline':
        return (
          <FlatList
            data={memoizedTimelineLogs}
            renderItem={renderLogItem}
            keyExtractor={keyExtractor}
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
            onOpenProgrammer={() => setActivePanel('programmer')} 
            onOpenLab={() => setActivePanel('lab')} 
            setIsPicksSchedulerVisible={(v) => setActivePanel(v ? 'picksScheduler' : null)}
            setIsProductManagerVisible={(v) => setActivePanel(v ? 'productManager' : null)}
            setIsAppManagerVisible={(v) => setActivePanel(v ? 'appManager' : null)}
            setIsUserManagementVisible={(v) => setActivePanel(v ? 'userManagement' : null)}
            setIsRosterVisible={(v) => setActivePanel(v ? 'roster' : null)}
            setIsAuditVisible={(v) => setActivePanel(v ? 'audit' : null)}
            setIsBlacklistVisible={(v) => setActivePanel(v ? 'blacklist' : null)}
            setIsFeatureFlagsVisible={(v) => setActivePanel(v ? 'featureFlags' : null)}
            setIsGlobalAnalyticsVisible={(v) => setActivePanel(v ? 'globalAnalytics' : null)}
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
      
      {activePanel === 'appManager' ? (
        <AppManager 
          visible={true} 
          onClose={() => setActivePanel(null)}
          appSettings={appSettings}
          handlePolicyToggle={handlePolicyToggle}
          updateSetting={updateSetting}
          bg={bg} cardBg={cardBg} borderColor={borderColor} textPrimary={textPrimary} textMuted={textMuted}
        />
      ) : activePanel === 'userManagement' ? (
        <UserManagementPanel
          visible={true}
          onClose={() => setActivePanel(null)}
          bg={bg} cardBg={cardBg} borderColor={borderColor} textPrimary={textPrimary} textMuted={textMuted}
        />
      ) : activePanel === 'roster' ? (
        <AdminRosterPanel
          visible={true}
          onClose={() => setActivePanel(null)}
          bg={bg} cardBg={cardBg} borderColor={borderColor} textPrimary={textPrimary} textMuted={textMuted}
        />
      ) : activePanel === 'audit' ? (
        <AdminAuditLogViewer
          visible={true}
          onClose={() => setActivePanel(null)}
          bg={bg} cardBg={cardBg} borderColor={borderColor} textPrimary={textPrimary} textMuted={textMuted}
        />
      ) : activePanel === 'blacklist' ? (
        <HardwareBlacklistPanel
          visible={true}
          onClose={() => setActivePanel(null)}
          bg={bg} cardBg={cardBg} borderColor={borderColor} textPrimary={textPrimary} textMuted={textMuted}
        />
      ) : activePanel === 'featureFlags' ? (
        <FeatureFlagsPanel
          visible={true}
          onClose={() => setActivePanel(null)}
          bg={bg} cardBg={cardBg} borderColor={borderColor} textPrimary={textPrimary} textMuted={textMuted}
        />
      ) : activePanel === 'globalAnalytics' ? (
        <GlobalAnalyticsPanel Colors={Colors} />
      ) : activePanel === 'productManager' ? (
         <ProductManager 
           visible={true} 
           onClose={() => setActivePanel(null)}
           allProfiles={allProfiles}
           editingProfile={editingProfile}
           startEditing={startEditing}
           createNew={createNew}
           patchEdit={patchEdit}
           saveProduct={saveProduct}
           productSaving={productSaving}
           bg={bg} cardBg={cardBg} borderColor={borderColor} textPrimary={textPrimary} textMuted={textMuted}
         />
      ) : activePanel === 'picksScheduler' ? (
        <AdminPicksScheduler visible={true} onClose={() => setActivePanel(null)} />
      ) : activePanel === 'programmer' ? (
        <Sk8LytzProgrammer 
          visible={true} 
          onClose={() => setActivePanel(null)} 
          onExitToLogs={() => setActivePanel(null)}
          allDevices={allDevices || []}
          deviceConfigs={liveDeviceConfigs}
          connectToDevice={async (d) => { if (onConnectToDevice) await onConnectToDevice(d); }}
          disconnectFromDevice={async (id: string) => { if (onDisconnectFromDevice) await onDisconnectFromDevice(id); }}
          bleState={bleState || 'IDLE'}
          handleScan={handleScan || (() => {})}
        />
      ) : activePanel === 'lab' ? (
        <Sk8LytzDiagnosticLab
          visible={true}
          isDiagnosticsMode={isDiagnosticsMode}
          onToggleDiagnostics={onToggleDiagnostics}
          onClose={() => setActivePanel(null)}
          connectedDevices={connectedDevices}
          liveRxPayload={liveRxPayload}
          hwSettings={hwSettings}
          allDevices={allDevices}
          bleState={bleState || 'IDLE'}
          handleScan={handleScan || (() => {})}
          connectToDevice={async (d) => { if (onConnectToDevice) await onConnectToDevice(d); }}
          liveDeviceConfigs={liveDeviceConfigs}
        />
      ) : (
        <SafeAreaView style={[styles.root, { backgroundColor: bg }]}>
          {/* Header */}
          <View style={[styles.modalHeader, { borderBottomColor: borderColor }]}>
            <View style={{ flexShrink: 1, paddingRight: Spacing.sm }}>
              <Text style={[styles.title, { color: textPrimary }]} numberOfLines={1}>Admin Tools</Text>
              <Text style={[styles.subtitle, { color: textMuted }]} numberOfLines={1}>{memoizedTimelineLogs.length} events stored</Text>
            </View>
            <View style={styles.headerActions}>
              <TouchableOpacity onPress={handleExport} style={styles.actionBtn} accessibilityRole="button" accessibilityLabel="Export logs">
                <MaterialCommunityIcons name="download" size={22} color="#00f0ff" />
              </TouchableOpacity>
              <TouchableOpacity
                onPress={handleUpload}
                style={[styles.actionBtn, isUploading && { opacity: 0.5 }]}
                disabled={isUploading}
                accessibilityRole="button"
                accessibilityLabel={isUploading ? 'Uploading logs' : 'Upload logs to cloud'}
              >
                <MaterialCommunityIcons
                  name={isUploading ? 'cloud-sync' : 'cloud-upload'}
                  size={22}
                  color="#00E676"
                />
              </TouchableOpacity>
              <TouchableOpacity onPress={handleClear} style={styles.actionBtn} accessibilityRole="button" accessibilityLabel="Clear all logs">
                <MaterialCommunityIcons name="delete-sweep" size={22} color="#ff4040" />
              </TouchableOpacity>
              <TouchableOpacity onPress={onClose} style={styles.actionBtn} accessibilityRole="button" accessibilityLabel="Close admin tools">
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
                accessibilityRole="button"
                accessibilityLabel={`${t} tab`}
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
      )}

      <ConfirmDeleteModal 
        visible={confirmDeleteVisible} 
        onClose={() => setConfirmDeleteVisible(false)} 
        onConfirm={executeClear} 
        isDark={isDark} 
      />

    </Modal>
  );
}

const localStyles = StyleSheet.create({
  listItemContainerLight: {
    borderBottomColor: '#E0E0E0',
  },
  listItemContainerDark: {
    borderBottomColor: '#252c47',
  },
});
