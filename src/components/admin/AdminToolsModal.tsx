import { MaterialCommunityIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import React, { useCallback, useEffect, useState } from 'react';
import {
    Alert,
    FlatList,
    Modal,
    Platform, SafeAreaView,
    Text,
    TouchableOpacity,
    View
} from 'react-native';
import { useTheme } from '../../context/ThemeContext';
import { useAdminSettings } from '../../hooks/useAdminSettings';
import { EVENT_META, formatLogTime, getPayloadSummary, useAdminTelemetry } from '../../hooks/useAdminTelemetry';
import { useProductManager } from '../../hooks/useProductManager';
import { EventType, LogEntry } from '../../services/AppLogger';
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
import { CulturalDaemonMonitor } from './tools/CulturalDaemonMonitor';


type Tab = 'timeline' | 'stats' | 'device' | 'tools';

export interface AdminToolsModalProps {
  visible: boolean;
  onClose: () => void;
  isDiagnosticsMode?: boolean;
  onToggleDiagnostics?: () => void;
  hwSettings?: any;
  onDisconnectFromDevice?: (id: string) => Promise<void>;
  writeToDevice?: (data: number[], deviceId?: string) => Promise<void | boolean | 'partial'>;
  liveRxPayload?: { deviceId: string; payloadHex: string; timestamp?: number } | null;
  connectedDevices?: { id: string, name: string | null }[];
  allDevices?: any[];
  bleState?: string;
  handleScan?: () => void;
  onClearAll?: () => void;
  onConnectToDevice?: (device: any) => Promise<any>;
  liveDeviceConfigs?: Record<string, any>;
}

export default function AdminToolsModal({ 
  visible, onClose, onClearAll, writeToDevice, liveRxPayload, 
  connectedDevices, allDevices, bleState, handleScan, onConnectToDevice, 
  liveDeviceConfigs, isDiagnosticsMode, onToggleDiagnostics, hwSettings, onDisconnectFromDevice 
}: AdminToolsModalProps) {
  const { isDark } = useTheme();
  const [tab, setTab] = useState<Tab>('timeline');
  const [isProductManagerVisible, setIsProductManagerVisible] = useState(false);
  const [isPicksSchedulerVisible, setIsPicksSchedulerVisible] = useState(false);
  const [isAppManagerVisible, setIsAppManagerVisible] = useState(false);
  const [isProgrammerVisible, setIsProgrammerVisible] = useState(false);
  const [isLabVisible, setIsLabVisible] = useState(false);
  const [isDaemonMonitorVisible, setIsDaemonMonitorVisible] = useState(false);
  const [confirmDeleteVisible, setConfirmDeleteVisible] = useState(false);

  // ── Domain Hooks ────────────────────────────────────────────────────────────
  const { logs, stats, isUploading, clearLogs, uploadLogs, exportLogs } = useAdminTelemetry(visible);
  
  const { 
    allProfiles, editingProfile, isSaving: productSaving, 
    startEditing, createNew, patchEdit, saveProduct 
  } = useProductManager();

  const { appSettings, updateSetting } = useAdminSettings(isAppManagerVisible);

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

  const [deviceConfigs, setDeviceConfigs] = useState<Record<string, any>>({});

  useEffect(() => {
    const loadConfigs = async () => {
      try {
        const stored = await AsyncStorage.getItem('@Sk8lytz_device_configs');
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
            onOpenProgrammer={() => setIsProgrammerVisible(true)} 
            onOpenLab={() => setIsLabVisible(true)} 
            setIsPicksSchedulerVisible={setIsPicksSchedulerVisible}
            setIsProductManagerVisible={setIsProductManagerVisible}
            setIsAppManagerVisible={setIsAppManagerVisible}
            setIsDaemonMonitorVisible={setIsDaemonMonitorVisible}
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
      
      {isAppManagerVisible ? (
        <AppManager 
          visible={isAppManagerVisible} 
          onClose={() => setIsAppManagerVisible(false)}
          appSettings={appSettings}
          handlePolicyToggle={handlePolicyToggle}
          updateSetting={updateSetting}
          bg={bg} cardBg={cardBg} borderColor={borderColor} textPrimary={textPrimary} textMuted={textMuted}
        />
      ) : isProductManagerVisible ? (
         <ProductManager 
           visible={isProductManagerVisible} 
           onClose={() => setIsProductManagerVisible(false)}
           allProfiles={allProfiles}
           editingProfile={editingProfile}
           startEditing={startEditing}
           createNew={createNew}
           patchEdit={patchEdit}
           saveProduct={saveProduct}
           productSaving={productSaving}
           bg={bg} cardBg={cardBg} borderColor={borderColor} textPrimary={textPrimary} textMuted={textMuted}
         />
      ) : isPicksSchedulerVisible ? (
        <AdminPicksScheduler visible={isPicksSchedulerVisible} onClose={() => setIsPicksSchedulerVisible(false)} />
      ) : isProgrammerVisible ? (
        <Sk8LytzProgrammer 
          visible={isProgrammerVisible} 
          onClose={() => setIsProgrammerVisible(false)} 
          onExitToLogs={() => setIsProgrammerVisible(false)}
          allDevices={(allDevices as any)}
          deviceConfigs={liveDeviceConfigs as any}
          connectToDevice={async (d: any) => { if (onConnectToDevice) await onConnectToDevice(d); }}
          disconnectFromDevice={async (id: string) => { if (onDisconnectFromDevice) await onDisconnectFromDevice(id); }}
          writeToDevice={writeToDevice || (async () => false)}
          bleState={bleState || 'IDLE'}
          handleScan={handleScan || (() => {})}
        />
      ) : isLabVisible ? (
        <Sk8LytzDiagnosticLab
          visible={isLabVisible}
          isDiagnosticsMode={isDiagnosticsMode}
          onToggleDiagnostics={onToggleDiagnostics}
          onClose={() => setIsLabVisible(false)}
          connectedDevices={connectedDevices as any}
          writeToDevice={writeToDevice || (async () => false)}
          liveRxPayload={liveRxPayload}
          hwSettings={hwSettings}
          allDevices={allDevices}
          bleState={bleState || 'IDLE'}
          handleScan={handleScan || (() => {})}
          connectToDevice={async (d: any) => { if (onConnectToDevice) await onConnectToDevice(d); }}
          liveDeviceConfigs={liveDeviceConfigs as any}
        />
      ) : isDaemonMonitorVisible ? (
        <CulturalDaemonMonitor
          visible={isDaemonMonitorVisible}
          onClose={() => setIsDaemonMonitorVisible(false)}
          bg={bg} cardBg={cardBg} borderColor={borderColor} textPrimary={textPrimary} textMuted={textMuted}
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
