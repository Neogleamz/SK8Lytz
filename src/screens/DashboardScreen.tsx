/**
 * DashboardScreen.tsx — SK8Lytz Root Application Screen
 *
 * The single top-level screen for the entire SK8Lytz application.
 * This is intentionally monolithic — all BLE state must be co-located
 * to prevent race conditions between hardware events and UI re-renders.
 *
 * After Phase 1 Domain-Driven Refactor:
 *  - BLE lifecycle (scan, connect, disconnect) → remains here (Master Reference constraint)
 *  - Profile, AppSettings, modal flags        → useDashboardProfile
 *  - Fleet groups, device configs, power map  → useDashboardGroups
 *  - Voice commands, favorites, tutorial      → useDashboardVoice
 *  - Crew session state                       → remains here (feeds BLE write dispatch)
 *
 * Platform: React Native (Android + Web)
 */
import React, { useEffect, useState, useMemo, useCallback, useRef } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, SafeAreaView, FlatList, Platform, Image, Linking, Animated, Modal, TextInput, BackHandler, PanResponder, AppState, AppStateStatus, Alert, ActivityIndicator, ScrollView } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { Typography, Layout } from '../theme/theme';
import { useTheme } from '../context/ThemeContext';
import DeviceItem from '../components/DeviceItem';
import { LinearGradient } from 'expo-linear-gradient';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import useBLE from '../hooks/useBLE';
import { ZenggeProtocol, ZENGGE_SERVICE_UUID } from '../protocols/ZenggeProtocol';

import DockedController, { DockedControllerHandle, IFavoriteState } from '../components/DockedController';
import DeviceSettingsModal from '../components/DeviceSettingsModal';
import GroupSettingsModal from '../components/GroupSettingsModal';
import Sk8LytzProgrammerModal from '../components/Sk8LytzProgrammerModal';
import ScannerAnimation from '../components/ScannerAnimation';
import { AppLogger } from '../services/AppLogger';
import AdminToolsModal from '../components/AdminToolsModal';
import VoiceFAB from '../components/Voice/VoiceFAB';
import VoiceCommandModal from '../components/Voice/VoiceCommandModal';
import VoiceTutorialModal from '../components/Voice/VoiceTutorialModal';
import { CrewModal } from '../components/CrewModal';
import { crewService, CrewSession, CrewRole } from '../services/CrewService';
import Sk8LytzDiagnosticLab from '../components/Sk8LytzDiagnosticLab';

import HardwareSetupWizardScreen from './Onboarding/HardwareSetupWizardScreen';
import { supabase } from '../services/supabaseClient';
import { useRegistration, RegisteredDevice } from '../hooks/useRegistration';
import AccountModal from '../components/AccountModal';
import CrewMemberDashboard from '../components/CrewMemberDashboard';
import { getLocalProfileByPoints, LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';

// ─── Phase 1 Domain Hooks ──────────────────────────────────────────────────────
import { useDashboardProfile } from '../hooks/useDashboardProfile';
import { useDashboardGroups } from '../hooks/useDashboardGroups';
import { useDashboardVoice } from '../hooks/useDashboardVoice';
import type { DeviceSettings, CustomGroup } from '../types/dashboard.types';

// DeviceSettings and CustomGroup are now imported from '../types/dashboard.types'
// — migrated as part of Phase 1 Domain-Driven Refactor

/**
 * SkateGroupCard Helper Component
 * Renders a premium, telemetry-rich group card with morphing gradients.
 */
const SkateGroupCard = ({
  group,
  onPress,
  onLongPress,
  colors,
  lastPattern,
  userProfile,
  powerStates,
  Colors,
  styles
}: {
  group: CustomGroup;
  onPress: () => void;
  onLongPress: () => void;
  colors: string[];
  lastPattern?: string;
  userProfile: any;
  powerStates: Record<string, boolean>;
  Colors: any;
  styles: any;
}) => {
  const isPoweredOn = group.deviceIds.every(id => powerStates[id] !== false);

  return (
    <TouchableOpacity
      onPress={onPress}
      onLongPress={onLongPress}
      activeOpacity={0.85}
      style={styles.skateCardWrapper}
    >
      <LinearGradient
        colors={isPoweredOn ? (colors as any) : ['#333', '#1a1a1a']}
        start={{ x: 0, y: 0 }}
        end={{ x: 1, y: 1 }}
        style={styles.skateCardGradient}
      >
        <View style={styles.skateCardInner}>
          {/* Glassmorphism Refraction Overlay */}
          <View style={styles.skateCardRefraction} />

          {/* TOP DECOR: Avatar (Pill) + Telemetry */}
          <View style={styles.skateCardHeader}>
            {/* Z Brandmark Pill (Top-Left) */}
            <View style={styles.avatarPill}>
      <Image 
        source={require('../../assets/logo.png')} 
        style={styles.avatarPillImage} 
        resizeMode="contain"
      />
               <View style={styles.avatarStatusDot} />
            </View>

            {/* Telemetry (Top-Right) */}
            <View style={styles.telemetryContainer}>
               {/* RSSI Signal Strength Bars */}
               <View style={styles.telemetryItem}>
                 <MaterialCommunityIcons name="signal-variant" size={14} color={isPoweredOn ? colors[0] : '#666'} />
                 <View style={styles.rssiBars}>
                   <View style={[styles.rssiBar, { height: 4, backgroundColor: isPoweredOn ? colors[0] : '#444' }]} />
                   <View style={[styles.rssiBar, { height: 7, backgroundColor: isPoweredOn ? colors[0] : '#444' }]} />
                   <View style={[styles.rssiBar, { height: 10, backgroundColor: isPoweredOn ? colors[0] : '#444', opacity: 0.5 }]} />
                 </View>
               </View>

               {/* Battery Placeholder */}
               <View style={styles.telemetryItem}>
                 <MaterialCommunityIcons name="battery-60" size={16} color={isPoweredOn ? Colors.success : '#666'} />
               </View>
            </View>
          </View>

          {/* CENTER: Group Name & Pattern */}
          <View style={styles.skateCardContent}>
            <Text style={styles.skateCardGroupName}>
              {group.name.toUpperCase()}
            </Text>
            
            <View style={styles.patternPill}>
              <View style={[styles.patternDot, { backgroundColor: isPoweredOn ? colors[0] : '#555' }]} />
              <Text style={styles.patternName}>
                {isPoweredOn ? (lastPattern || 'ACTIVE') : 'POWERED OFF'}
              </Text>
            </View>
          </View>

          {/* BOTTOM DECOR: Device Count */}
          <View style={styles.skateCardFooter}>
             <Text style={styles.deviceCountText}>
               {group.deviceIds.length} {group.deviceIds.length === 1 ? 'DEVICE' : 'DEVICES'} PAIRED
             </Text>
             <View style={[styles.powerIconCircle, { backgroundColor: isPoweredOn ? 'rgba(0, 240, 255, 0.1)' : 'rgba(255,255,255,0.05)' }]}>
               <MaterialCommunityIcons 
                 name="power" 
                 size={16} 
                 color={isPoweredOn ? Colors.primary : '#666'} 
               />
             </View>
          </View>
        </View>
      </LinearGradient>
    </TouchableOpacity>
  );
};

/**
 * Utility to generate premium gradient colors based on pattern name/state
 */
const getPatternColors = (patternName?: string, Colors?: any) => {
  if (!patternName) return [Colors?.primary || '#00F0FF', Colors?.secondary || '#7000FF'];
  
  const name = patternName.toLowerCase();
  if (name.includes('fire') || name.includes('flame')) return ['#FF4D00', '#FF9E00'];
  if (name.includes('water') || name.includes('ocean')) return ['#00B2FF', '#00FFF0'];
  if (name.includes('forest') || name.includes('nature')) return ['#00FF85', '#00A3FF'];
  if (name.includes('sunset') || name.includes('gold')) return ['#FFD600', '#FF00E5'];
  if (name.includes('nebula') || name.includes('space')) return ['#7000FF', '#00FFFF'];
  if (name.includes('neon') || name.includes('cyber')) return ['#FF00E5', '#00F0FF'];
  if (name.includes('police')) return ['#FF0000', '#0000FF'];
  if (name.includes('matrix')) return ['#00FF00', '#003300'];
  
  // Default to branding colors
  return [Colors?.primary || '#00F0FF', Colors?.secondary || '#7000FF'];
};

export default function DashboardScreen({ isOfflineMode = false, onLogout }: { isOfflineMode?: boolean; onLogout?: () => void } = {}) {
  const { Colors, isDark, toggleTheme } = useTheme();
  const insets = useSafeAreaInsets();
  const styles = createStyles(Colors);
  const {
    scanForPeripherals,
    allDevices,
    setAllDevices,
    connectToDevice,
    connectToDevices,
    connectedDevices,
    disconnectFromDevice,
    writeToDevice,
    isScanning,
    isScanProbing,
    isBluetoothSupported,
    isBluetoothEnabled,
    requestPermissions,
    setOnDataReceived,
    setOnHardwareProbed,
    droppedOutDeviceIds,
    pendingRegistrations,
    clearPendingRegistrations,
  } = useBLE();

  // ── Registration system ────────────────────────────────────────────────────
  const {
    registeredDevices,
    saveRegisteredDevice,
    saveAllRegisteredDevices,
    deregisterDevice,
    checkDeviceClaimed,
    hasCloudRegistrations,
    migrateLegacyGroups,
    isLoading,
  } = useRegistration();

  // ── Phase 1: Profile, AppSettings & Modal Flags → useDashboardProfile ─────────────
  const {
    userProfile,
    appSettings,
    refreshProfile,
    isAccountModalVisible,
    setIsAccountModalVisible,
    isAdminToolsVisible,
    setIsAdminToolsVisible,
    isSupportModalVisible,
    setIsSupportModalVisible,
    isProgrammerVisible,
    setIsProgrammerVisible,
    isLabVisible,
    setIsLabVisible,
    isMapVisible,
    setIsMapVisible,
  } = useDashboardProfile({
    onCrewJoinNotification: (crewId: string) => {
      setPendingJoinCrewId(crewId);
      setIsCrewModalVisible(true);
    },
  });

  // Sync connected+discovered devices into AppLogger whenever they change
  // so that parsed_session_devices has fresh data at upload time
  useEffect(() => {
    const merged = [...connectedDevices];
    allDevices.forEach(d => { if (!merged.find(c => c.id === d.id)) merged.push(d); });
    AppLogger.updateKnownDevices(merged);
  }, [connectedDevices, allDevices]);

  // Register background probe callback — fires for each device after scan probe
  useEffect(() => {
    setOnHardwareProbed((deviceId: string, cfg: any) => {
      // Merge parsed hardware config into deviceConfigs (persisted store)
      setDeviceConfigs(prev => {
        const merged = { ...(prev[deviceId] || {}), ...cfg };
        const next = { ...prev, [deviceId]: merged };
        // Persist immediately
        AsyncStorage.setItem('ng_device_configs', JSON.stringify(next)).catch(() => {});
        return next;
      });
      // Also update allDevices so the list shows config before user connects
      setAllDevices(prev => prev.map(d =>
        (d as any).id === deviceId
          ? { ...d,
              points: cfg.ledPoints,
              segments: cfg.segments,
              stripType: cfg.icName,
              sorting: cfg.colorSortingName,
              colorSorting: cfg.colorSorting,
              icType: cfg.icType,
              detected: true
            } as any
          : d
      ));
    });
  }, [setOnHardwareProbed]);

  const [updateTrigger, setUpdateTrigger] = useState(0);
  const [isTestModeActive, setIsTestModeActive] = useState(false);
  const [isDisconnecting, setIsDisconnecting] = useState(false);
  const [lastRawNotification, setLastRawNotification] = useState<{deviceId: string, payloadHex: string} | null>(null);

  // ── Phase 1: Fleet Groups, Device Configs, Power States → useDashboardGroups ───────
  const [isSetupWizardVisible, setIsSetupWizardVisible] = useState(false);
  const {
    customGroups,
    customGroupsRef,
    deviceConfigs,
    setDeviceConfigs,
    powerStates,
    setPowerState,
    lastGroupPatterns,
    setLastGroupPattern,
    groupModalState,
    editingGroupId,
    openGroupCreate,
    openGroupRename,
    closeGroupModal,
    selectedIds,
    isSelectionMode,
    toggleDeviceSelection,
    clearSelection,
    isDeviceListCollapsed,
    setIsDeviceListCollapsed,
    isRegisteredCollapsed,
    setIsRegisteredCollapsed,
    handleRegistrationComplete,
  } = useDashboardGroups({
    registeredDevices,
    saveAllRegisteredDevices,
    migrateLegacyGroups,
    clearPendingRegistrations,
    onRegistrationComplete: () => {
      setIsSetupWizardVisible(false);
      wizardCheckedRef.current = false;
    },
  });

  // ── Crew Hub state (stays in DashboardScreen — feeds BLE write dispatch) ───────
  const [crewSession, setCrewSession] = useState<CrewSession | null>(null);
  const [crewRole, setCrewRole] = useState<CrewRole>(null);
  const [isCrewModalVisible, setIsCrewModalVisible] = useState(false);
  const [crewModeSummary, setCrewModeSummary] = useState<string | undefined>(undefined);
  const [lastLeaderScene, setLastLeaderScene] = useState<Record<string, any> | null>(null);
  const [_pendingJoinCrewId, setPendingJoinCrewId] = useState<string | null>(null);
  const dockedControllerRef = React.useRef<DockedControllerHandle>(null);

  // ── Phase 1: Voice Commands & Favorites → useDashboardVoice ───────────────────
  const {
    isVoiceModalVisible,
    setIsVoiceModalVisible,
    isVoiceTutorialVisible,
    setIsVoiceTutorialVisible,
    isVoiceTutorialDismissed,
    dismissTutorial,
    favorites,
    isListening,
    transcript,
    error: voiceError,
    isVoiceSupported,
    startListening,
    stopListening,
  } = useDashboardVoice({ dockedControllerRef });

  const [isCheckingRegistrations, setIsCheckingRegistrations] = useState(true);
  const lastProcessedRef = React.useRef<string>('');
  const allDevicesRef = React.useRef(allDevices);
  const isProvisioningTriggered = React.useRef(false);
  // customGroupsRef is now provided by useDashboardGroups hook

  // AppState Telemetry
  useEffect(() => {
    const subscription = AppState.addEventListener('change', (nextAppState: AppStateStatus) => {
      if (nextAppState === 'active') {
        AppLogger.log('APP_FOREGROUNDED');
      } else if (nextAppState === 'background') {
        AppLogger.log('APP_BACKGROUNDED');
      }
    });
    return () => {
      subscription.remove();
    };
  }, []);


  const wizardCheckedRef = React.useRef(false);
  const [pendingNewDevice, setPendingNewDevice] = React.useState<any | null>(null);

  // 0. Auto-scan on mount
  useEffect(() => {
    if (!isScanning) {
      scanForPeripherals();
    }
  }, []);

  // 1. Check FTUE state on mount
  useEffect(() => {
    hasCloudRegistrations().then(hasAny => {
      if (!hasAny) {
        setIsSetupWizardVisible(true);
        setIsCheckingRegistrations(false);
      } else {
        setIsCheckingRegistrations(false);
      }
    });
  }, []);

  // 2. Continuous listener for new devices beyond FTUE
  useEffect(() => {
    if (pendingRegistrations.length === 0) return;
    if (isSetupWizardVisible) return; // Ignore if wizard is already active
    
    // Only check if we are in dashboard mode and a new untracked device appears
    const checkNewDevice = async () => {
      const first = pendingRegistrations[0];
      if (!first) return;
      const status = await checkDeviceClaimed(first.device_mac, {
        firmwareVer: first.firmware_ver,
        productId:   first.product_id,
      });
      if (status === 'unclaimed') setPendingNewDevice(first);
    };
    
    checkNewDevice();
  }, [pendingRegistrations]);

  // handleRegistrationComplete is now provided by useDashboardGroups hook

  // Drop-out UI Alert
  useEffect(() => {
    if (droppedOutDeviceIds.length > 0) {
      Alert.alert(
        'Device Disconnected', 
        `Hardware dropout detected. Connection has been lost to a skate.`,
        [{ text: 'OK' }]
      );
    }
  }, [droppedOutDeviceIds]);

  // User Profile
  const [authUsername, setAuthUsername] = useState<string | null>(null);

  // Load cached username on mount for instant UI feedback
  useEffect(() => {
    AsyncStorage.getItem('@Sk8lytz_auth_username').then(val => {
      if (val && !authUsername) setAuthUsername(val);
    }).catch(() => {});
  }, []);

  const handleLogout = async () => {
     try {
       await supabase.auth.signOut();
       // App.tsx onAuthStateChange will detect session=null and redirect to AuthScreen automatically
     } catch (e) {
       AppLogger.error('Logout error:', e);
     }
  };

  // Cloud Sync & Auto Connect on Launch
  const hasAutoConnectedRef = useRef(false);
  const autoConnectIdsRef = useRef<string[]>([]);
  
  // Continuous Auto-Connect Observer
  useEffect(() => {
    if (autoConnectIdsRef.current.length === 0) return;
    
    // Find devices that are in our target list, currently scanned, but not yet connected
    const pendingToConnect = allDevices.filter(d => 
      autoConnectIdsRef.current.includes(d.id) && 
      !connectedDevices.some(c => c.id === d.id)
    );

    if (pendingToConnect.length > 0) {
      // Remove them from the queue so we don't spam the connection loop
      autoConnectIdsRef.current = autoConnectIdsRef.current.filter(id => !pendingToConnect.some(p => p.id === id));
      console.log('[SK8Lytz] Observer connecting to newly discovered devices:', pendingToConnect.map(d => d.name));
      connectToDevices(pendingToConnect);
    }
  }, [allDevices, connectedDevices]);

  useEffect(() => {
    async function syncCloudAndAutoConnect() {
      if (hasAutoConnectedRef.current || !isBluetoothSupported || !isBluetoothEnabled) return;
      hasAutoConnectedRef.current = true; // Prevent looping

      let groupsToProcess: any[] = [];
      let isOffline = true;
      let CloudUserId: string | null = null;

      if (supabase) {
        const { data: { session } } = await supabase.auth.getSession();
        if (session?.user) {
          CloudUserId = session.user.id;
          // Fetch profile and set auth display name
          try {
            await refreshProfile();
            const name = userProfile?.display_name || userProfile?.username || session.user.email?.split('@')[0] || 'GUEST';
            setAuthUsername(name);
            AsyncStorage.setItem('@Sk8lytz_auth_username', name).catch(() => {});
          } catch {
            const fallback = session.user.email?.split('@')[0] || 'GUEST';
            setAuthUsername(fallback);
            AsyncStorage.setItem('@Sk8lytz_auth_username', fallback).catch(() => {});
          }
          let groups: CustomGroup[] | null = null;
          try {
            const result = await supabase.from('registered_groups').select('*').eq('user_id', CloudUserId);
            groups = result.data as CustomGroup[];
            isOffline = !!result.error;
          } catch {
            isOffline = true;
          }
          if (!isOffline && groups && groups.length > 0) {
            groupsToProcess = groups;
          }
        }
      }

      if (isOffline || groupsToProcess.length === 0) {
        // Fallback to local storage
        console.log('[SK8Lytz] Cloud unreachable. Using local AsyncStorage for Auto-Connect...');
        const localDevicesStr = await AsyncStorage.getItem('@Sk8lytz_registered_devices');
        if (localDevicesStr) {
          try {
            const parsed = JSON.parse(localDevicesStr);
            const offlineGroupMap = new Map();
            parsed.forEach((d: any) => {
              if (d.group_id && d.group_id !== 'default-fleet') {
                if (!offlineGroupMap.has(d.group_id)) {
                  offlineGroupMap.set(d.group_id, {
                    id: d.group_id,
                    group_name: d.group_name || d.group_id,
                    created_at: new Date().toISOString(), // Fallback
                    deviceIds: []
                  });
                }
                offlineGroupMap.get(d.group_id).deviceIds.push(d.device_mac);
              }
            });
            groupsToProcess = Array.from(offlineGroupMap.values());
          } catch (e) {}
        }
      }

      if (groupsToProcess.length > 0) {
        console.log('[SK8Lytz] Found fleets. Initiating Auto-Connect Sequence...');
        requestPermissions().then(async (granted) => {
          if (granted && !isActuallyConnected) {
            scanForPeripherals();
            
            let presentGroups: any[] = [];
            let idsToConnect: string[] = [];

            if (!isOffline && supabase && CloudUserId) {
               const { data: devices } = await supabase.from('registered_devices').select('*').eq('user_id', CloudUserId);
               if (devices) {
                  presentGroups = groupsToProcess.sort((a: any, b: any) => new Date(b.created_at).getTime() - new Date(a.created_at).getTime());
                  
                  if (presentGroups.length > 0) {
                    idsToConnect = devices.filter((d: any) => d.group_id === presentGroups[0].id).map((d: any) => d.device_mac || d.id);
                  }
               }
            } else {
               presentGroups = groupsToProcess.sort((a: any, b: any) => new Date(b.created_at).getTime() - new Date(a.created_at).getTime());
               if (presentGroups.length > 0) {
                  idsToConnect = presentGroups[0].deviceIds;
               }
            }

            if (idsToConnect.length > 0) {
              console.log('[SK8Lytz] Queuing Auto-connect for fleet:', presentGroups[0].group_name);
              autoConnectIdsRef.current = idsToConnect;
            }
          }
        });
      }
    }
    
    // Slight delay to allow Bluetooth stack to fully initialize (1.5s)
    const timerId = setTimeout(syncCloudAndAutoConnect, 1500);
    return () => clearTimeout(timerId);
  }, [isBluetoothSupported, isBluetoothEnabled]);

  // ── Crew auto-rejoin on launch ────────────────────────────────────────────
  useEffect(() => {
    const tryRejoin = async () => {
      const { data: { user } } = await supabase.auth.getUser();
      if (!user) return;
      const displayName = user.email?.split('@')[0] || 'Skater';
      const result = await crewService.tryAutoRejoin(displayName);
      if (!result) return;

      const { session, role } = result;
      setCrewSession(session);
      setCrewRole(role);

      if (role === 'leader') {
        crewService.subscribeAsLeader(session.id, () => {});
      } else {
        crewService.subscribeAsMember(session.id, (scene) => {
          dockedControllerRef.current?.applyCloudScene(scene);
        });
        // Apply last known scene immediately
        const lastScene = await crewService.fetchLastScene(session.id).catch(() => null);
        if (lastScene) {
          setTimeout(() => dockedControllerRef.current?.applyCloudScene(lastScene), 500);
        }
      }
    };
    // Delay slightly to let auth session restore
    const t = setTimeout(tryRejoin, 2000);
    return () => clearTimeout(t);
  }, []);

  // Voice command dispatch + notification init are now handled
  // by useDashboardVoice and useDashboardProfile hooks respectively.

  // Bind BLE Notification Hardware Sync Hook
  useEffect(() => {
    setOnDataReceived((deviceId: string, payload: number[]) => {
      const payloadHex = payload.map(b => b.toString(16).padStart(2,'0').toUpperCase()).join(' ');
      setLastRawNotification({ deviceId, payloadHex });

      // ── Diagnostics: upload every raw notification to Supabase ──────────────
      // Fire-and-forget: never blocks the UI or BLE pipeline
      const v2ConfigForUpload = ZenggeProtocol.parseHardwareSettingsResponse(payload);
      const v1ConfigForUpload = ZenggeProtocol.parseHardwareConfig(payload);
      const parsedOk = !!(v2ConfigForUpload || v1ConfigForUpload);
      const pts = v2ConfigForUpload?.ledPoints ?? v1ConfigForUpload?.points;
      const ict = v2ConfigForUpload?.icType;
      const icn = v2ConfigForUpload?.icName ?? v1ConfigForUpload?.stripType;
      const cs  = v2ConfigForUpload?.colorSorting ?? undefined;
      const co  = v2ConfigForUpload?.colorSortingName ?? v1ConfigForUpload?.sorting;
      const deviceName = allDevices.find((d: any) => d.id === deviceId)?.name ?? null;
      supabase.from('device_diagnostics').insert({
        device_id:     deviceId,
        device_name:   deviceName,
        payload_hex:   payloadHex,
        payload_bytes: payload.length,
        byte_0:        payload[0] ?? null,
        byte_2:        payload[2] ?? null,
        parsed_ok:     parsedOk,
        points:        pts ?? null,
        ic_type:       ict ?? null,
        ic_name:       icn ?? null,
        color_sorting: cs ?? null,
        color_order:   co ?? null,
      }).then(({ error }: any) => {
        if (error) AppLogger.warn('[Diagnostics] upload failed:', error.message);
        else console.log('[Diagnostics] uploaded', payload.length, 'bytes for', deviceId);
      });
      // ────────────────────────────────────────────────────────────────────────
      
      const v2Config = ZenggeProtocol.parseHardwareSettingsResponse(payload);
      const v1Config = ZenggeProtocol.parseHardwareConfig(payload);
      
      let configPoints: number | undefined;
      let configSegments: number | undefined;
      let configStripType: string | undefined;
      let configSorting: string | undefined;
      let configSortingIdx: number | undefined;
      let configIcType: number | undefined;

      if (v2Config) {
        configPoints = v2Config.ledPoints;
        configSegments = v2Config.segments;
        configStripType = v2Config.icName;
        configSorting = v2Config.colorSortingName;
        configSortingIdx = v2Config.colorSorting;   // numeric index — critical for hwSettings
        configIcType = v2Config.icType;
      } else if (v1Config) {
        configPoints = v1Config.points;
        configSegments = v1Config.segments;
        configStripType = v1Config.stripType;
        configSorting = v1Config.sorting;
        // Derive idx from string for v1
        configSortingIdx = ['RGB','RBG','GRB','GBR','BRG','BGR'].indexOf(configSorting ?? 'GRB');
        if (configSortingIdx < 0) configSortingIdx = 2; // default GRB
      }

      if (configPoints !== undefined && configSorting !== undefined) {
        // Prevent telemetry flooding by NOT emitting an event for passive continuous Sync packets
        setAllDevices(prev => prev.map(d => {
          if (d.id === deviceId) {
            const newD = { 
              ...d, 
              points: configPoints, 
              sorting: configSorting,
              colorSorting: configSortingIdx,   // numeric index propagated
              colorSortingName: configSorting,
              stripType: configStripType, 
              icType: configIcType,
              segments: configSegments,
              detected: true,                    // flag that this came from real hardware
            } as any as typeof d;
            // Mirror securely directly to persistent memory
            AsyncStorage.getItem('ng_device_configs').then(str => {
               const p = JSON.parse(str || '{}');
               p[deviceId] = { ...p[deviceId], ...newD };
               AsyncStorage.setItem('ng_device_configs', JSON.stringify(p));
            }).catch(()=>{});
            setDeviceConfigs((prevConfigs: any) => ({
              ...prevConfigs,
              [deviceId]: { ...(prevConfigs[deviceId] || {}), ...newD }
            }));
            return newD;
          }
          return d;
        }));
      }
    });
  }, [setOnDataReceived, setAllDevices]);

  // Analytics / Dev tools hooks migrated to AuthScreen / removed

  const handleScan = () => {
    if (isScanning || isActuallyConnected) return;
    
    requestPermissions().then((granted) => {
      if (granted) {
        console.log('[SK8Lytz] Manual Scan Initiated');
        AppLogger.log('SCAN_STARTED');
        const _scanStartTime = Date.now();
        isProvisioningTriggered.current = true;
        AsyncStorage.removeItem('ng_processed_devices');
        lastProcessedRef.current = '';
        allDevicesRef.current = []; // Clear ref immediately
        
        scanForPeripherals();
        
        // Mock simulator logic moved to useBLE.ts

        // Ensure we scan for at least 5 seconds for visual impact
        const waitTime = Math.max(5000, Platform.OS === 'web' ? 5000 : 7000);
        setTimeout(() => {
          runAutoProvisioning();
        }, waitTime);
      }
    });
  };

  // Automatically probe for hardware upon dashboard load to eliminate user friction
  const hasAutoScanned = React.useRef(false);
  useEffect(() => {
    if (!isCheckingRegistrations && !isSetupWizardVisible && !hasAutoScanned.current) {
      if (connectedDevices.length === 0 && !isScanning) {
        hasAutoScanned.current = true;
        // Wait 1 second to let dashboard natively render before hammering BLE stack
        setTimeout(() => {
          handleScan();
        }, 1000);
      }
    }
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isCheckingRegistrations, isSetupWizardVisible, connectedDevices.length, isScanning]);

  useEffect(() => {
    customGroupsRef.current = customGroups;
  }, [customGroups]); // Keep ref in sync with hook-managed state

  // One-shot startup cleanup: prune simulator entries from AsyncStorage.
  // NOTE: customGroups and deviceConfigs state are now fully owned by useDashboardGroups.
  // This effect ONLY cleans sim- prefixed entries and ng_processed_devices — it does NOT
  // call setDeviceConfigs to avoid a race condition with useDashboardGroups' own load.
  useEffect(() => {
    // Prune sim-device entries from device configs (without overwriting hook state)
    AsyncStorage.getItem('ng_device_configs')
      .then(res => {
        if (!res) return;
        try {
          const configs = JSON.parse(res) || {};
          const hasSim = Object.keys(configs).some(k => k.startsWith('sim-'));
          if (hasSim) {
            for (const key in configs) { if (key.startsWith('sim-')) delete configs[key]; }
            AsyncStorage.setItem('ng_device_configs', JSON.stringify(configs)).catch(() => {});
          }
        } catch (e: any) { AppLogger.warn('JSON parse error configs cleanup', { error: String(e) }); }
      })
      .catch((e: any) => AppLogger.warn('AsyncStorage error configs cleanup', { error: String(e) }));

    // Prune sim-device entries from processed devices log
    AsyncStorage.getItem('ng_processed_devices')
      .then(res => {
        if (!res) return;
        try {
          const processed = JSON.parse(res) || [];
          const cleaned = processed.filter((id: string) => !id.startsWith('sim-'));
          if (cleaned.length !== processed.length) {
            AsyncStorage.setItem('ng_processed_devices', JSON.stringify(cleaned)).catch(() => {});
          }
        } catch (e) {}
      });
  }, []);


  const runAutoProvisioning = useCallback(async () => {
    if (!isProvisioningTriggered.current) return;
    isProvisioningTriggered.current = false;

    console.log('[SK8Lytz] Provisioning...');
    const currentDevices = allDevicesRef.current;
    if (currentDevices.length === 0) return;

    let updatedGroups = [...customGroupsRef.current];
    let didUpdateGroups = false;
    let didUpdateConfigs = false;
    
    const [resConfigs, resProcessed] = await Promise.all([
      AsyncStorage.getItem('@Sk8lytz_device_configs'),
      AsyncStorage.getItem('@Sk8lytz_processed_devices')
    ]);

    let configs: any = {};
    if (resConfigs) { try { configs = JSON.parse(resConfigs); } catch(e) {} }

    let processed: string[] = [];
    if (resProcessed) { try { processed = JSON.parse(resProcessed); } catch(e) {} }
    let didUpdateProcessed = false;
    
    const checkAndGroup = (devicesToProcess: any[], targetGroupName: string, typeVal: string, pointsVal: number) => {
      const unprocessed = devicesToProcess.filter(d => 
        !processed.includes(d.id) && 
        !updatedGroups.some(g => g.deviceIds.includes(d.id))
      );

      if (unprocessed.length >= 2) {
        const leftId = unprocessed[0].id;
        const rightId = unprocessed[1].id;
        processed.push(leftId, rightId);
        didUpdateProcessed = true;

        const existingGroupIndex = updatedGroups.findIndex(g => g.name === targetGroupName);
        if (existingGroupIndex > -1) {
          let target = updatedGroups[existingGroupIndex];
          if (!target.deviceIds.includes(leftId) || !target.deviceIds.includes(rightId)) {
            const newIds = Array.from(new Set([...target.deviceIds, leftId, rightId]));
            updatedGroups[existingGroupIndex] = { ...target, deviceIds: newIds };
            didUpdateGroups = true;
          }
        } else {
          updatedGroups.push({
            id: `group-${Date.now()}-${typeVal}-${Math.floor(Math.random() * 1000)}`,
            name: targetGroupName,
            deviceIds: [leftId, rightId],
            type: typeVal,
            isGroup: true
          });
          didUpdateGroups = true;
        }
        
        const names = [`${typeVal} Left Skate`, `${typeVal} Right Skate`];
        [leftId, rightId].forEach((id, idx) => {
          if (!configs[id] || configs[id].name !== names[idx]) {
            configs[id] = { ...configs[id], name: names[idx], type: typeVal, points: pointsVal };
            didUpdateConfigs = true;
          }
        });
      }
    };

    // Catalog-driven device classification: group by resolved product type via LED point range.
    // Replaces legacy binary soulzDevices / halozDevices split — RAILZ and future products auto-bucket.
    const devicesByType = new Map<string, typeof currentDevices>();
    for (const d of currentDevices) {
      const pts = (d as any).points ?? 0;
      const profile = getLocalProfileByPoints(pts);
      if (!devicesByType.has(profile.id)) devicesByType.set(profile.id, []);
      devicesByType.get(profile.id)!.push(d);
    }
    for (const [typeKey, devices] of devicesByType.entries()) {
      const profile = LOCAL_PRODUCT_CATALOG.find(p => p.id === typeKey) ?? LOCAL_PRODUCT_CATALOG[0];
      checkAndGroup(devices, `${profile.displayName} SK8Lytz`, typeKey, profile.defaultLedPoints);
    }

    const storagePromises = [];
    if (didUpdateProcessed) {
      storagePromises.push(AsyncStorage.setItem('@Sk8lytz_processed_devices', JSON.stringify(processed)));
    }
    if (didUpdateGroups) {
      storagePromises.push(AsyncStorage.setItem('@Sk8lytz_custom_groups', JSON.stringify(updatedGroups)));
    }
    if (didUpdateConfigs) {
      storagePromises.push(AsyncStorage.setItem('@Sk8lytz_device_configs', JSON.stringify(configs)));
    }
    if (storagePromises.length > 0) await Promise.all(storagePromises);

    if (didUpdateGroups) {
      customGroupsRef.current = updatedGroups;
      // Note: customGroups UI state is derived from registeredDevices via useDashboardGroups
    }
    
    // Sync to Supabase if authenticated
    if ((didUpdateGroups || didUpdateConfigs) && supabase) {
      try {
        const { data: { session } } = await supabase.auth.getSession();
        if (session && session.user) {
          const userId = session.user.id;
          
          for (const group of updatedGroups) {
            // Upsert Group
            try {
              await supabase.from('registered_groups').upsert({
                id: group.id,
                user_id: userId,
                group_name: group.name,
                created_at: new Date().toISOString()
              }, { onConflict: 'id' });
            } catch (_ge) { /* best-effort sync */ }
            
            // Upsert Devices in Group
            for (const deviceId of group.deviceIds) {
              const c = configs[deviceId];
              if (c) {
                try {
                  await supabase.from('registered_devices').upsert({
                    id: deviceId,
                    user_id: userId,
                    group_id: group.id,
                    custom_name: c.name || 'Unknown',
                    points: c.points || 0,
                    segments: c.segments || 0,
                    sorting: c.sorting || 'GRB',
                    strip_type: c.stripType || 'UNKNOWN'
                  }, { onConflict: 'id' });
                } catch (_de) { /* best-effort sync */ }
              }
            }
          }
        }
      } catch (e) {
        AppLogger.warn('Supabase sync error during provisioning:', { error: String(e) });
      }
    }
    
    if (didUpdateConfigs) {
      setAllDevices(prev => {
        let morphed = false;
        const next = prev.map(d => {
          const c = configs[d.id];
          if (c && (d.name !== c.name || (d as any).sorting !== c.sorting || (d as any).stripType !== c.stripType)) {
            morphed = true;
            return { ...d, name: c.name, points: c.points, sorting: c.sorting, stripType: c.stripType } as any;
          }
          return d;
        });
        if (morphed) allDevicesRef.current = next;
        return morphed ? next : prev;
      });
    }
    
    // Explicitly collapse to focus on grouped controls
    setIsDeviceListCollapsed(true);
    console.log('[SK8Lytz] Provisioning Complete.');
  }, []);






  const displayConnectedDevices = useMemo(() => {
    return connectedDevices.map(d => {
      const cfg = deviceConfigs[(d as any).id] || {};
      return { ...d, ...cfg };
    });
  }, [connectedDevices, deviceConfigs]);

  const isActuallyConnected = displayConnectedDevices.length > 0;
  const isGrouped = displayConnectedDevices.length > 1 && displayConnectedDevices.every(d => (d as any).grouped);

  const sortedAllDevices = useMemo(() => {
    return [...allDevices]
      .sort((a, b) => (b.rssi ?? -999) - (a.rssi ?? -999))
      .map(d => {
        const cfg = deviceConfigs[(d as any).id] || {};
        return { ...d, ...cfg };
      });
  }, [allDevices, deviceConfigs]);

  const registeredDevicesData = useMemo(() => {
    const macs = new Set(registeredDevices.map((d: any) => d.device_mac?.toLowerCase() ?? ''));
    return sortedAllDevices.filter((d: any) => macs.has(d.id?.toLowerCase() ?? ''));
  }, [sortedAllDevices, registeredDevices]);

  const availableDevicesData = useMemo(() => {
    const macs = new Set(registeredDevices.map((d: any) => d.device_mac?.toLowerCase() ?? ''));
    return sortedAllDevices.filter((d: any) => !macs.has(d.id?.toLowerCase() ?? ''));
  }, [sortedAllDevices, registeredDevices]);

  const handleDisconnect = useCallback(async () => {
    if (isDisconnecting) return;
    setIsDisconnecting(true);
    await disconnectFromDevice();
    setIsDisconnecting(false);
  }, [disconnectFromDevice, isDisconnecting]);

  useEffect(() => {
    const handleBackPress = () => {
      if (isTestModeActive) {
        setIsTestModeActive(false);
        return true; // intercept
      }
      if (isDisconnecting) {
        return true; // intercept and block multiple back presses
      }
      if (isActuallyConnected) {
        handleDisconnect();
        return true; // intercept and exit to scanner
      }
      return false; // allow native exit
    };

    const backHandler = BackHandler.addEventListener('hardwareBackPress', handleBackPress);
    return () => backHandler.remove();
  }, [isTestModeActive, isActuallyConnected, handleDisconnect, isDisconnecting]);

  // Handle Swipe-to-Back natively for Visualizer screens (IOS & Android edge swipe)
  const edgePanResponder = useRef(
    PanResponder.create({
      onMoveShouldSetPanResponder: (evt, gestureState) => {
        const startX = evt.nativeEvent.pageX - gestureState.dx;
        // Only trigger if starting from the far left edge (< 40px) moving rightwards (simulating back swipe)
        if (startX < 50 && gestureState.dx > 15 && Math.abs(gestureState.dx) > Math.abs(gestureState.dy)) {
          return true;
        }
        return false;
      },
      onPanResponderRelease: (evt, gestureState) => {
        if (gestureState.dx > 60) {
          if (isTestModeActive) setIsTestModeActive(false);
          else if (isActuallyConnected && !isDisconnecting) handleDisconnect();
        }
      },
      onPanResponderTerminate: (evt, gestureState) => {
        if (gestureState.dx > 60) {
          if (isTestModeActive) setIsTestModeActive(false);
          else if (isActuallyConnected && !isDisconnecting) handleDisconnect();
        }
      }
    })
  ).current;

  const handlePowerToggle = async (deviceIds: string[], forceState?: boolean) => {
    setPowerState(deviceIds, forceState);
  };

  const handleDeviceTap = (id: string) => {
    toggleDeviceSelection(id);
  };

  // toggleSelect replaced by toggleDeviceSelection from useDashboardGroups

  const openCreateGroup = () => {
    if (selectedIds.length === 0) return;
    const selectedDevices = allDevices.filter(d => selectedIds.includes(d.id));
    // Catalog-driven type resolution — replaces name.includes('soul') heuristic.
    const resolveType = (d: any) => getLocalProfileByPoints((d as any).points ?? 0).id;
    const firstType = resolveType(selectedDevices[0]);
    const allSame = selectedDevices.every(d => resolveType(d) === firstType);

    if (!allSame) {
      alert(`Please only group devices of the same type (e.g. two ${firstType}).`);
      return;
    }

    openGroupCreate();
  };

  const handleGroupDelete = async (id: string) => {
    const groupToDelete = customGroups.find(g => g.id === id);
    if (!groupToDelete) {
      closeGroupModal();
      return;
    }

    const devsToDelete = registeredDevices.filter(d => d.group_id === id);
    for (const d of devsToDelete) {
      await saveRegisteredDevice({ ...d, group_id: 'default-fleet', group_name: undefined, is_pending_sync: true });
    }

    if (groupToDelete && groupToDelete.deviceIds) {
      try {
        const stored = await AsyncStorage.getItem('ng_device_configs');
        if (stored) {
          const configs = JSON.parse(stored);
          let configsChanged = false;
          
          for (const mac of groupToDelete.deviceIds) {
            if (configs[mac]) {
              delete configs[mac].groupId;
              delete configs[mac].groupName;
              configs[mac].grouped = false;
              configsChanged = true;
            }
          }
          
          if (configsChanged) {
            await AsyncStorage.setItem('ng_device_configs', JSON.stringify(configs));
            setDeviceConfigs(configs);
          }
        }
      } catch (e) {
        AppLogger.warn('Failed to scrub ghost group from components: ' + e);
      }
    }

    closeGroupModal();
  };

  const saveGroup = async (name: string, deviceIds: string[]) => {
    let finalGroupId = `group-${Date.now()}`;
    let previousDeviceIds: string[] = [];

    if (groupModalState === 'CREATE') {
      const existing = customGroups.find(g => g.name.toLowerCase() === name.toLowerCase());
      if (existing) {
        finalGroupId = existing.id;
        previousDeviceIds = existing.deviceIds || [];
      }
      clearSelection();
    } else if (groupModalState === 'RENAME' && editingGroupId) {
      if (deviceIds.length === 0) {
        handleGroupDelete(editingGroupId);
        return;
      }
      finalGroupId = editingGroupId;
      const existing = customGroups.find(g => g.id === editingGroupId);
      if (existing) previousDeviceIds = existing.deviceIds || [];
    }

    try {
      const stored = await AsyncStorage.getItem('ng_device_configs');
      const configs = stored ? JSON.parse(stored) : {};
      let configsChanged = false;
      
      // Removed devices: scrub their configs
      const removedIds = previousDeviceIds.filter(id => !deviceIds.includes(id));
      for (const mac of removedIds) {
        if (configs[mac]) {
          delete configs[mac].groupId;
          delete configs[mac].groupName;
          configs[mac].grouped = false;
          configsChanged = true;
        }
        const rd = registeredDevices.find(r => r.device_mac === mac);
        if (rd) await saveRegisteredDevice({ ...rd, group_id: 'default-fleet', group_name: undefined, is_pending_sync: true });
      }

      // Added or preserved devices: enforce group config
      for (const mac of deviceIds) {
        if (!configs[mac]) configs[mac] = {};
        configs[mac] = { ...configs[mac], groupId: finalGroupId, groupName: name, grouped: true };
        configsChanged = true;
        const rd = registeredDevices.find(r => r.device_mac === mac);
        if (rd) await saveRegisteredDevice({ ...rd, group_id: finalGroupId, group_name: name, is_pending_sync: true });
      }

      if (configsChanged) {
        await AsyncStorage.setItem('ng_device_configs', JSON.stringify(configs));
        setDeviceConfigs(configs);
      }
    } catch (e) {
      AppLogger.warn('Failed to sync group cache changes', { error: String(e) });
    }
  };

  useEffect(() => {
    requestPermissions();
  }, []);

  const [isSettingsVisible, setIsSettingsVisible] = useState(false);
  const [selectedDeviceForSettingsId, setSelectedDeviceForSettingsId] = useState<string | null>(null);

  const selectedDeviceForSettings = useMemo(() => {
    if (!selectedDeviceForSettingsId) return null;
    return allDevices.find(d => d.id === selectedDeviceForSettingsId) || null;
  }, [selectedDeviceForSettingsId, allDevices]);

  const openSettings = (device: any) => {
    setSelectedDeviceForSettingsId(device.id);
    
    // Query hardware settings with correct 0x63 command (not legacy 0x10)
    if (connectedDevices.some(d => d.id === device.id)) {
       setTimeout(() => writeToDevice(ZenggeProtocol.queryHardwareSettings(false), device.id), 300);
    }
    
    setIsSettingsVisible(true);
  };

  const saveSettings = async (settings: DeviceSettings) => {
    if (selectedDeviceForSettings) {
      // Avoid direct mutations, use functional state updates instead
      let finalGroupId = settings.groupId;

      // Capture implicit group associations assigned through the Hardware settings modal
      if (settings.grouped && settings.groupName && !settings.groupId) {
        // Find existing group by name or create a fresh one natively
        const existingGroup = customGroups.find(g => g.name.toLowerCase() === settings.groupName?.toLowerCase());
        finalGroupId = existingGroup ? existingGroup.id : `group-${Date.now()}`;
      } else if (!settings.grouped) {
        finalGroupId = 'default-fleet';
      }

      // Sync via useRegistration SSOT
      const rd = registeredDevices.find(r => r.device_mac === selectedDeviceForSettings.id);
      if (rd) {
        const targetGroupName = settings.grouped ? (settings.groupName || rd.group_name) : undefined;
        saveRegisteredDevice({ ...rd, group_id: finalGroupId, group_name: targetGroupName, is_pending_sync: true }).catch(AppLogger.warn);
      }

      setAllDevices((prev: any[]) => {
        const next = prev.map(d => 
          d.id === selectedDeviceForSettings.id 
            ? { ...d, name: settings.name, type: settings.type, points: settings.points, segments: settings.segments, sorting: settings.sorting, stripType: settings.stripType, groupId: finalGroupId } 
            : d
        );
        allDevicesRef.current = next as any;
        return next;
      });
      
      setUpdateTrigger(prev => prev + 1);
 
       try {
         const stored = await AsyncStorage.getItem('ng_device_configs');
         const configs = stored ? JSON.parse(stored) : {};
         configs[selectedDeviceForSettings.id] = { ...settings, groupId: finalGroupId };
         await AsyncStorage.setItem('ng_device_configs', JSON.stringify(configs));
         
         AppLogger.log('HARDWARE_CONFIG_CHANGED', {
           deviceId: selectedDeviceForSettings.id,
           name: settings.name,
           type: settings.type,
           points: settings.points,
           segments: settings.segments,
           sorting: settings.sorting,
           stripType: settings.stripType,
         });

         // Log rename separately for device audit trail
         const previousName = selectedDeviceForSettings.name;
         if (settings.name && settings.name !== previousName) {
           AppLogger.log('DEVICE_RENAMED', {
             deviceId: selectedDeviceForSettings.id,
             oldName: previousName || 'Unknown',
             newName: settings.name,
           });
         }
      } catch (e) { AppLogger.error('Failed to persist settings', e); }
    }
    setIsSettingsVisible(false);
  };

  const activeHwSettings = useMemo(() => {
    const raw = displayConnectedDevices[0] as any;
    // Merge persisted deviceConfigs on top — ensures 0x63 ping results always surface
    // even when displayConnectedDevices comes from useBLE.connectedDevices (real hardware path)
    const cached = raw?.id ? (deviceConfigs[raw.id] || {}) : {};
    const d = { ...raw, ...cached };
    // String-derived fallback for sorting when hardware hasn't been probed yet
    const s = d?.sorting || d?.colorSortingName || 'GRB';
    const sortingIdx = s === 'RGB' ? 0 : s === 'RBG' ? 1 : s === 'GRB' ? 2 : s === 'GBR' ? 3 : s === 'BRG' ? 4 : s === 'BGR' ? 5 : 2;
    // CRITICAL: Only trust the numeric colorSorting index when hardware has been probed (detected=true).
    // Stale cached data may have colorSorting=0 (RGB) from old parses, which ?? does NOT override
    // because 0 is not null/undefined. Using detected as the gate prevents wrong cached values
    // from overriding the GRB sortingIdx fallback derived from the device name string.
    const colorSortingFinal = d?.detected ? (d.colorSorting ?? sortingIdx) : sortingIdx;
    return {
      ledPoints: d?.ledPoints || d?.points || (d?.name?.toLowerCase().includes('soul') ? 43 : 16),
      segments:  d?.segments || 1,
      icType:    d?.icType || 1,
      icName:    d?.icName || d?.stripType || 'WS2812B',
      colorSorting: colorSortingFinal,
      colorSortingName: s,
      detected:  d?.detected || false,
    };
  }, [displayConnectedDevices, deviceConfigs]);


  const MemoizedSk8lytzController = useMemo(() => {
    if (!isActuallyConnected) return null;
    if (isTestModeActive) return null;

    // ── Crew member view: show telemetry dashboard instead of full controller ──
    if (crewSession && crewRole === 'member') {
      return (
        <CrewMemberDashboard
          session={crewSession}
          role={crewRole}
          currentScene={lastLeaderScene}
          onLeave={async () => {
            await crewService.leaveSession().catch(() => {});
            setCrewSession(null);
            setCrewRole(null);
            setLastLeaderScene(null);
            setCrewModeSummary(undefined);
          }}
        />
      );
    }

    return (
      <Animated.View {...edgePanResponder.panHandlers} style={{ flex: 1, backgroundColor: 'transparent' }}>
          <DockedController
            ref={dockedControllerRef}
            hwSettings={activeHwSettings}
            lockedProduct={
              // Catalog-driven resolution: stored type takes priority, then derive from LED count.
              (displayConnectedDevices[0] as any)?.type ||
              getLocalProfileByPoints((displayConnectedDevices[0] as any)?.points ?? 0).id
            }
            isPaired={isGrouped}
            isDisconnecting={isDisconnecting}
            points={(displayConnectedDevices[0] as any).points}
            devices={displayConnectedDevices as any}
            onLongPressDevice={openSettings}
            writeToDevice={writeToDevice}
            isPoweredOn={displayConnectedDevices.some(d => powerStates[d.id] ?? true)}
            onDisconnect={handleDisconnect}
            crewRole={crewRole}
            onCrewSceneChange={(scene: Record<string, any>) => crewService.broadcastScene(scene)}
            onPatternChanged={(patternName: string) => {
              // Ensure we only bind this to a physical hardware controller view, not when
              // we don't have a specific group/skate selected.
              const targetGroupId = displayConnectedDevices[0]?.groupId || displayConnectedDevices[0]?.id;
              if (targetGroupId && patternName !== lastGroupPatterns[targetGroupId]) {
                setLastGroupPattern(targetGroupId, patternName);
              }
            }}
          />
          {/* Disconnection Teardown Overlay */}
          {isDisconnecting && (
            <Animated.View style={{
              position: 'absolute', top: 0, left: 0, right: 0, bottom: 0,
              backgroundColor: 'rgba(0,0,0,0.85)',
              justifyContent: 'center', alignItems: 'center',
              zIndex: 9999
            }}>
              <ActivityIndicator size="large" color="#00F0FF" />
              <Text style={[Typography.header, { color: '#00F0FF', marginTop: 12 }]}>Disconnecting...</Text>
            </Animated.View>
          )}
      </Animated.View>
    );
  }, [isActuallyConnected, isGrouped, displayConnectedDevices, writeToDevice, powerStates, isTestModeActive, activeHwSettings, crewRole, crewSession, lastLeaderScene, isDisconnecting]);

  /**
   * Renders a single device item card, merging registration data 
   * with live discovered BLE configs.
   */
  const renderItem = useCallback(({ item }: { item: RegisteredDevice | any }) => {
    const cachedConfig = deviceConfigs?.[item.id || item.device_mac] || {};
    const mergedItem = { ...item, ...cachedConfig };

    return (
    <View style={{ paddingHorizontal: Layout.padding }}>
      <DeviceItem
        device={mergedItem}
        isConnected={displayConnectedDevices.some(d => d.id === item.id)}
        isSelectionMode={isSelectionMode}
        isSelected={selectedIds.includes(item.id)}
        onPress={async () => {
          if (isSelectionMode) {
            toggleDeviceSelection(item.id);
            return;
          }
          const fw = await connectToDevice(item);
          if (fw) {
            setDeviceConfigs((prev: any) => {
                const next = { ...prev, [item.id]: { ...(prev?.[item.id] || {}), firmware: fw } };
                AsyncStorage.setItem('ng_device_configs', JSON.stringify(next)).catch(() => {});
                return next;
            });
          }
          
          writeToDevice(ZenggeProtocol.queryHardwareSettings(false), item.id);
        }}
        onLongPress={() => {
          openSettings(mergedItem);
        }}
        showGroupIcon={false}
        isPoweredOn={powerStates[item.id] ?? true}
        onPowerToggle={() => handlePowerToggle([item.id])}
      />
    </View>
    ); // close return
  }, [displayConnectedDevices, isSelectionMode, selectedIds, powerStates, deviceConfigs]);

  const BluetoothWarningBanner = useMemo(() => {
    if (isBluetoothEnabled || Platform.OS === 'web') return null;
    return (
      <TouchableOpacity 
        onPress={() => Linking.openSettings()}
        style={{ 
          backgroundColor: Colors.error, 
          padding: 16, 
          alignItems: 'center', 
          justifyContent: 'center', 
          flexDirection: 'row', 
          gap: 12,
          borderBottomWidth: 1,
          borderBottomColor: 'rgba(255,255,255,0.2)'
        }}
        activeOpacity={0.9}
      >
        <MaterialCommunityIcons name="alert-circle" size={24} color="#FFF" />
        <Text style={{ color: '#FFF', fontWeight: '900', fontSize: 14, textTransform: 'uppercase', letterSpacing: 0.5 }}>
          Bluetooth Disabled or Permissions Denied!
        </Text>
        <MaterialCommunityIcons name="chevron-right" size={20} color="#FFF" />
      </TouchableOpacity>
    );
  }, [isBluetoothEnabled]);

  const renderDashboardHeader = () => (
    <View style={{
      paddingHorizontal: Layout.padding,
      paddingTop: insets.top + 12,
      paddingBottom: isActuallyConnected ? 2 : 8,
    }}>
      {isActuallyConnected ? (
        /* ── Connected: Unified Header Layout ── */
        <View style={{ flexDirection: 'row', alignItems: 'center' }}>
          {/* LEFT: Back button */}
          <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center' }}>
            <TouchableOpacity
              onPress={handleDisconnect}
              style={{
                flexDirection: 'row', alignItems: 'center',
                paddingHorizontal: 6, paddingVertical: 4,
                borderRadius: 20, gap: 2,
              }}
            >
              <MaterialCommunityIcons name="chevron-left" size={24} color={Colors.primary} />
              <Text style={{ color: Colors.primary, fontSize: 13, fontWeight: '800', letterSpacing: 0.5 }}>
                Back
              </Text>
            </TouchableOpacity>
          </View>

          {/* CENTER: logo + discovered status */}
          <TouchableOpacity activeOpacity={0.7} style={{ position: 'relative', alignItems: 'center' }} onPress={() => setIsAdminToolsVisible(true)}>
            <Image source={require('../../assets/logo.png')} style={{ width: 80, height: 24 }} resizeMode="contain" tintColor={Colors.text} />
            {(() => {
              const connectedCount = displayConnectedDevices.length;
              let expectedCount = 1;
              const firstDevice = displayConnectedDevices[0] as any;
              if (firstDevice?.grouped && firstDevice?.groupId) {
                const group = customGroups.find(g => g.id === firstDevice.groupId);
                if (group) expectedCount = group.deviceIds.length;
              }
              let statusColor = connectedCount === 0 ? Colors.error : connectedCount < expectedCount ? '#FFA500' : Colors.success;
              return (
                <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: 2 }}>
                  <View style={{ width: 4, height: 4, borderRadius: 2, backgroundColor: statusColor, marginRight: 4 }} />
                  <Text style={{ color: statusColor, fontSize: 8, fontWeight: 'bold', letterSpacing: 0.5 }}>CONNECTED ({connectedCount})</Text>
                </View>
              );
            })()}
          </TouchableOpacity>

          {/* RIGHT: utilities group (matching AuthScreen style) */}
          <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'flex-end', gap: 8 }}>
            {!isTestModeActive && (
              <TouchableOpacity
                style={{ 
                  width: 34, height: 34, borderRadius: 17, 
                  backgroundColor: (displayConnectedDevices.every(id => powerStates[id.id] ?? true)) ? 'rgba(0,240,255,0.15)' : 'rgba(255,255,255,0.07)', 
                  justifyContent: 'center', alignItems: 'center', 
                  borderWidth: 1, 
                  borderColor: (displayConnectedDevices.every(id => powerStates[id.id] ?? true)) ? 'rgba(0,240,255,0.3)' : 'rgba(255,255,255,0.15)' 
                }}
                onPress={() => handlePowerToggle(displayConnectedDevices.map(d => d.id))}
                activeOpacity={0.6}
              >
                <MaterialCommunityIcons name="power" size={18} color={(displayConnectedDevices.every(id => powerStates[id.id] ?? true)) ? Colors.primary : Colors.textMuted} />
              </TouchableOpacity>
            )}
            
            <TouchableOpacity 
              onPress={() => setIsSupportModalVisible(true)}
              style={{ width: 34, height: 34, borderRadius: 17, borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)', backgroundColor: 'rgba(255,255,255,0.07)', alignItems: 'center', justifyContent: 'center' }}
            >
              <MaterialCommunityIcons name="help-circle-outline" size={18} color={Colors.textMuted} />
            </TouchableOpacity>

            <TouchableOpacity 
              onPress={toggleTheme} 
              style={{ width: 34, height: 34, borderRadius: 17, borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)', backgroundColor: 'rgba(255,255,255,0.07)', alignItems: 'center', justifyContent: 'center' }}
            >
              <MaterialCommunityIcons name={isDark ? 'weather-sunny' : 'weather-night'} size={18} color={Colors.primary} />
            </TouchableOpacity>
          </View>
        </View>
      ) : (
        /* ── Not connected: 3-column layout — user pill left, icons right ── */
        <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}>

          {/* LEFT: user pill left-justified */}
          <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center' }}>
            <TouchableOpacity
              onPress={() => setIsAccountModalVisible(true)}
              style={{
                flexDirection: 'row', alignItems: 'center',
                paddingHorizontal: 6, paddingVertical: 4,
                borderRadius: 16, borderWidth: 1, gap: 4,
                borderColor: isOfflineMode ? 'rgba(255,170,0,0.35)' : 'rgba(0,240,255,0.25)',
                backgroundColor: isOfflineMode ? 'rgba(255,170,0,0.08)' : 'rgba(0,240,255,0.06)',
              }}
            >
              <View style={{
                width: 6, height: 6, borderRadius: 3,
                backgroundColor: isOfflineMode ? '#FFA500' : Colors.success,
                shadowColor: isOfflineMode ? '#FFA500' : Colors.success,
                shadowOpacity: 0.8, shadowRadius: 4, elevation: 2,
              }} />
              <Text style={{ color: Colors.text, fontSize: 10, fontWeight: '700', maxWidth: 55, fontFamily: 'Righteous' }} numberOfLines={1}>
                {authUsername || 'GUEST'}
              </Text>
              <View style={{
                paddingHorizontal: 4, paddingVertical: 1, borderRadius: 4,
                backgroundColor: isOfflineMode ? 'rgba(255,170,0,0.2)' : 'rgba(0,200,100,0.2)',
              }}>
                <Text style={{ fontSize: 7, fontWeight: '900', letterSpacing: 0.5, color: isOfflineMode ? '#FFA500' : Colors.success }}>
                  {isOfflineMode ? 'OFF' : 'ON'}
                </Text>
              </View>
              <MaterialCommunityIcons name="account-cog" size={12} color={Colors.textMuted} style={{ opacity: 0.8 }} />
            </TouchableOpacity>
          </View>

          {/* [BUG FIX]: Replaced zIndex: -1 with pointerEvents="box-none" so logo TouchableOpacity catches touches */}
          {/* CENTER: logo perfectly absolute centered */}
          <View pointerEvents="box-none" style={{ position: 'absolute', left: 0, right: 0, bottom: 0, top: 0, justifyContent: 'center', alignItems: 'center' }}>
            <TouchableOpacity activeOpacity={0.7} style={{ position: 'relative', alignItems: 'center' }} onPress={() => setIsAdminToolsVisible(true)}>
              <Image source={require('../../assets/logo.png')} style={{ width: 85, height: 26 }} resizeMode="contain" tintColor={Colors.text} />
            </TouchableOpacity>
          </View>

          {/* RIGHT: grouped icons */}
          <View style={{ flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'flex-end', gap: 6 }}>
            <TouchableOpacity
              style={{ width: 32, height: 32, borderRadius: 16, borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)', backgroundColor: 'rgba(255,255,255,0.07)', alignItems: 'center', justifyContent: 'center' }}
              onPress={() => setIsSupportModalVisible(true)}
            >
              <MaterialCommunityIcons name="help-circle-outline" size={16} color={Colors.textMuted} />
            </TouchableOpacity>

            <TouchableOpacity onPress={toggleTheme} style={{ width: 32, height: 32, borderRadius: 16, borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)', backgroundColor: 'rgba(255,255,255,0.07)', alignItems: 'center', justifyContent: 'center' }}>
              <MaterialCommunityIcons name={isDark ? 'weather-sunny' : 'weather-night'} size={16} color={Colors.primary} />
            </TouchableOpacity>

          </View>
        </View>
      )}

      {/* Accent line under logo when not connected */}
      {!isActuallyConnected && (
        <View style={{ height: 2, width: 30, backgroundColor: Colors.secondary, marginTop: 6, borderRadius: 1, alignSelf: 'center' }} />
      )}
    </View>
  );

  if (isCheckingRegistrations) {
    return (
      <View style={[styles.container, { backgroundColor: Colors.background, justifyContent: 'center', alignItems: 'center' }]}>
        <ActivityIndicator color={Colors.primary} size="large" />
      </View>
    );
  }

  if (isSetupWizardVisible) {
    return <HardwareSetupWizardScreen onSetupComplete={async (devices) => { await handleRegistrationComplete(devices, allDevices); }} />;
  }

  return (
    <SafeAreaView style={styles.safeArea}>
      {BluetoothWarningBanner}
      <View style={styles.container}>

        {isActuallyConnected && (
          <View style={{ flex: 1 }}>
            <View pointerEvents="box-none" style={{ paddingBottom: 16, zIndex: 100, elevation: 100 }}>
              {renderDashboardHeader()}
            </View>
            <View style={{ flex: 1 }}>
              {MemoizedSk8lytzController}
            </View>
          </View>
        )}
        {!isActuallyConnected && (
          /* ── 4-SLAB VERTICAL HIERARCHY ── */
          <View style={{ flex: 1, backgroundColor: Colors.background }}>
             {/* SLAB 1: HEADER (Logo + Pulse) */}
             <View style={styles.headerSlab}>
                {renderDashboardHeader()}
             </View>

             <View style={{ flex: 1, paddingBottom: 40 }}>
                {/* SLAB 2: CREW HUB (Sessions) */}
                <View style={[styles.slabContainer, { marginTop: 12 }]}>
                  <View style={[styles.glassSlab, { borderColor: isOfflineMode ? 'rgba(255,255,255,0.05)' : 'rgba(255,170,0,0.2)', paddingVertical: isOfflineMode ? 16 : 40 }]}>
                    <View style={[styles.slabHeader, isOfflineMode && { marginBottom: 8 }]}>
                      <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
                        <MaterialCommunityIcons name={isOfflineMode ? "cloud-off-outline" : "account-group"} size={18} color={isOfflineMode ? Colors.textMuted : "#FFAA00"} />
                        <Text style={[styles.slabTitle, { color: isOfflineMode ? Colors.textMuted : '#FFAA00' }]}>CREW HUB</Text>
                      </View>
                      {!crewSession && !isOfflineMode && (
                        <TouchableOpacity onPress={() => setIsCrewModalVisible(true)} style={styles.slabAction}>
                          <Text style={styles.slabActionText}>OPEN HUB</Text>
                        </TouchableOpacity>
                      )}
                    </View>

                    {appSettings['global_crew_hub_locked'] ? (
                      <View style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(255,255,255,0.02)', padding: 12, borderRadius: 8, borderWidth: 1, borderColor: 'rgba(255,255,255,0.05)' }}>
                        <MaterialCommunityIcons name="lock-outline" size={16} color={Colors.textMuted} style={{ marginRight: 8 }} />
                        <Text style={[styles.slabEmptyText, { color: Colors.textMuted, flex: 1, fontSize: 11 }]}>FEATURE TEMPORARILY DISABLED BY ADMIN</Text>
                      </View>
                    ) : isOfflineMode ? (
                      <View style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(255,255,255,0.02)', padding: 12, borderRadius: 8, borderWidth: 1, borderColor: 'rgba(255,255,255,0.05)' }}>
                        <MaterialCommunityIcons name="cloud-off-outline" size={16} color={Colors.textMuted} style={{ marginRight: 8 }} />
                        <Text style={[styles.slabEmptyText, { color: Colors.textMuted, flex: 1, fontSize: 11 }]}>Go online to sync lights with nearby skaters.</Text>
                      </View>
                    ) : crewSession ? (
                      <TouchableOpacity
                        style={[styles.activeCrewPill, { paddingVertical: 24 }]}
                        onPress={() => setIsCrewModalVisible(true)}
                      >
                        <View style={[styles.statusDot, { backgroundColor: crewRole === 'leader' ? '#FFAA00' : '#00AAFF' }]} />
                        <Text style={[styles.activeCrewText, { color: crewRole === 'leader' ? '#FFAA00' : '#00AAFF' }]}>
                          {crewSession.name.toUpperCase()} · LIVE
                        </Text>
                        <MaterialCommunityIcons name="chevron-right" size={16} color={crewRole === 'leader' ? '#FFAA00' : '#00AAFF'} />
                      </TouchableOpacity>
                    ) : (
                      <View style={{ gap: 16 }}>
                        <Text style={styles.slabEmptyText}>No active sessions nearby. Launch a crew to sync lights.</Text>
                        <TouchableOpacity 
                          style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center', backgroundColor: 'rgba(255,170,0,0.1)', paddingVertical: 12, borderRadius: 8, borderWidth: 1, borderColor: '#FFAA00', marginTop: 8 }}
                          onPress={() => Alert.alert("Coming Soon", "The Interactive Skate Spot Map is currently in development!")}
                        >
                          <MaterialCommunityIcons name="map-marker-radius" size={18} color="#FFAA00" style={{ marginRight: 8 }} />
                          <Text style={{ color: '#FFAA00', fontWeight: '800', letterSpacing: 1, fontSize: 13 }}>EXPLORE SKATE MAP</Text>
                        </TouchableOpacity>
                      </View>
                    )}
                  </View>
                </View>


                {/* SLAB 3: SKATES (Groups) */}
                <View style={styles.slabContainer}>
                  <View style={styles.slabHeader}>
                     <Text style={styles.slabTitle}>MY SKATES</Text>
                     <MaterialCommunityIcons name="lightning-bolt" size={14} color={Colors.primary} />
                  </View>
                  {customGroups.length > 0 ? (
                    <View style={{ gap: 12 }}>
                      {customGroups.map((group) => {
                        const lastPattern = lastGroupPatterns[group.id];
                        const cardColors = getPatternColors(lastPattern, Colors);
                        
                        return (
                          <SkateGroupCard
                            key={group.id}
                            group={group}
                            colors={cardColors}
                            lastPattern={lastPattern}
                            userProfile={userProfile}
                            powerStates={powerStates}
                            Colors={Colors}
                            styles={styles}
                            onPress={() => {
                              const devicesToConnect = allDevices.filter(d => group.deviceIds.includes(d.id));
                              if (devicesToConnect.length > 0) connectToDevices(devicesToConnect);
                            }}
                            onLongPress={() => {
                              openGroupRename(group.id);
                            }}
                          />
                        );
                      })}
                    </View>
                  ) : (
                    <View style={[styles.glassSlab, { alignItems: 'center', paddingVertical: 24 }]}>
                      <Text style={styles.slabEmptyText}>
                        {registeredDevices.length === 0 
                          ? "No skates detected. Time to link your hardware!" 
                          : "Create a group to control both skates at once."}
                      </Text>
                      {registeredDevices.length === 0 && (
                        <TouchableOpacity 
                          onPress={() => setIsSetupWizardVisible(true)}
                          style={[styles.scanButton, { marginTop: 16, width: '70%', backgroundColor: Colors.primary }]}
                        >
                          <Text style={styles.scanButtonText}>SET UP YOUR SKATES</Text>
                        </TouchableOpacity>
                      )}
                    </View>
                  )}
                </View>

                <View style={{ flex: 1 }} />

                {/* SLAB 4: REGISTERED FLEET (Devices) */}
                <View style={styles.slabContainer}>
                  <TouchableOpacity 
                    style={styles.slabHeader} 
                    onPress={() => setIsRegisteredCollapsed(!isRegisteredCollapsed)}
                    activeOpacity={0.7}
                  >
                     <View style={{ flexDirection: 'row', alignItems: 'center', gap: 6 }}>
                       <MaterialCommunityIcons name={isRegisteredCollapsed ? "chevron-down" : "chevron-up"} size={16} color={Colors.textMuted} />
                       <Text style={styles.slabTitle}>REGISTERED DEVICES</Text>
                     </View>
                     <TouchableOpacity 
                       onPress={() => setIsSetupWizardVisible(true)}
                       style={{ flexDirection: 'row', alignItems: 'center', gap: 4 }}
                     >
                       <MaterialCommunityIcons name="plus-circle-outline" size={14} color={Colors.primary} />
                       <Text style={[styles.slabActionText, { color: Colors.primary }]}>ADD DEVICE</Text>
                     </TouchableOpacity>
                  </TouchableOpacity>
                  
                  {!isRegisteredCollapsed && (
                    registeredDevices.length > 0 ? (
                      <View style={styles.deviceListFixed}>
                        {registeredDevices.map((d: RegisteredDevice) => (
                          <View key={d.id || d.device_mac} style={{ marginBottom: 8 }}>
                            {renderItem({ item: d } as any)}
                          </View>
                        ))}
                      </View>
                    ) : (
                      <View style={[styles.glassSlab, { alignItems: 'center', paddingVertical: 32 }]}>
                        <MaterialCommunityIcons name="bluetooth-connect" size={32} color={Colors.textMuted} style={{ marginBottom: 12 }} />
                        <Text style={styles.slabEmptyText}>No registered skates found.</Text>
                        <TouchableOpacity 
                          onPress={() => setIsSetupWizardVisible(true)}
                          style={[styles.scanButton, { marginTop: 16, width: '60%' }]}
                        >
                          <Text style={styles.scanButtonText}>START SETUP</Text>
                        </TouchableOpacity>
                      </View>
                    )
                  )}
                </View>
             </View>
          </View>
        )}
        <DeviceSettingsModal
          isVisible={isSettingsVisible}
          onClose={() => setIsSettingsVisible(false)}
          onSave={saveSettings}
          writeToDevice={writeToDevice}
          initialSettings={{
            name: selectedDeviceForSettings?.name || LOCAL_PRODUCT_CATALOG[0].displayName,
            // Catalog-driven: resolve from stored points rather than name-string heuristic.
            type: getLocalProfileByPoints((selectedDeviceForSettings as any)?.points ?? 0).id,
            points: deviceConfigs[selectedDeviceForSettings?.id || '']?.points || (selectedDeviceForSettings as any)?.points || getLocalProfileByPoints((selectedDeviceForSettings as any)?.points ?? 0).defaultLedPoints,
            segments: deviceConfigs[selectedDeviceForSettings?.id || '']?.segments || (selectedDeviceForSettings as any)?.segments || getLocalProfileByPoints((selectedDeviceForSettings as any)?.points ?? 0).defaultSegments,
            stripType: deviceConfigs[selectedDeviceForSettings?.id || '']?.stripType || (selectedDeviceForSettings as any)?.stripType || 'WS2812B',
            sorting: deviceConfigs[selectedDeviceForSettings?.id || '']?.sorting || (selectedDeviceForSettings as any)?.sorting || 'GRB',
            grouped: !!deviceConfigs[selectedDeviceForSettings?.id || '']?.groupId || (selectedDeviceForSettings as any)?.grouped || false,
            groupId: deviceConfigs[selectedDeviceForSettings?.id || '']?.groupId || (selectedDeviceForSettings as any)?.groupId,
            groupName: customGroups.find(g => g.id === (deviceConfigs[selectedDeviceForSettings?.id || '']?.groupId || (selectedDeviceForSettings as any)?.groupId))?.name || 'My SK8Lytz',
            firmware: deviceConfigs[selectedDeviceForSettings?.id || '']?.firmware || (selectedDeviceForSettings as any)?.firmware || ((selectedDeviceForSettings as any)?.id?.startsWith('sim-') ? 'v2.0.1.DEMO' : 'Unknown')
          }}
          groups={customGroups}
        />
        <GroupSettingsModal
          isVisible={groupModalState !== 'HIDDEN'}
          onClose={closeGroupModal}
          onSave={saveGroup}
          onDelete={groupModalState === 'RENAME' && editingGroupId ? () => handleGroupDelete(editingGroupId) : undefined}
          initialName={groupModalState === 'RENAME' ? customGroups.find(g => g.id === editingGroupId)?.name : 'My SK8Lytz'}
          initialDeviceIds={groupModalState === 'RENAME' ? customGroups.find(g => g.id === editingGroupId)?.deviceIds : selectedIds}
          allDevices={allDevices}
        />
      </View>
      


        <Modal
          visible={isSupportModalVisible}
          transparent={true}
          animationType="fade"
          onRequestClose={() => setIsSupportModalVisible(false)}
        >
          <View style={{ flex: 1, backgroundColor: 'rgba(0,0,0,0.8)', justifyContent: 'center', alignItems: 'center' }}>
            <View style={{ backgroundColor: Colors.surface, padding: 24, borderRadius: 16, width: '85%', borderWidth: 1, borderColor: Colors.surfaceHighlight }}>
              <View style={{ alignItems: 'center', marginBottom: 20 }}>
                <MaterialCommunityIcons name="lifebuoy" size={48} color={Colors.primary} />
                <Text style={{ ...Typography.title, color: Colors.primary, marginTop: 12 }}>Support Portal</Text>
                <Text style={{ color: Colors.textMuted, fontSize: 13, textAlign: 'center', marginTop: 8 }}>Need help configuring your hardware? Browse our official guides below.</Text>
              </View>
               <TouchableOpacity
                style={[styles.groupButton, { backgroundColor: 'rgba(0, 240, 255, 0.1)', borderColor: Colors.primary, borderWidth: 1, marginBottom: 16, paddingVertical: 12 }]}
                onPress={() => Linking.openURL('https://neogleamz.com/pages/getting-started')}
              >
                <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                  <MaterialCommunityIcons name="book-open-page-variant" size={20} color={Colors.primary} style={{ marginRight: 8 }} />
                  <Text style={[styles.groupButtonText, { color: Colors.primary, fontSize: 14 }]}>Installation Guides</Text>
                </View>
              </TouchableOpacity>

              <TouchableOpacity
                style={[styles.groupButton, { backgroundColor: 'rgba(255, 170, 0, 0.1)', borderColor: '#FFAA00', borderWidth: 1, marginBottom: 16, paddingVertical: 12 }]}
                onPress={() => Linking.openURL('https://neogleamz.com')}
              >
                <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                  <MaterialCommunityIcons name="cart" size={20} color="#FFAA00" style={{ marginRight: 8 }} />
                  <Text style={[styles.groupButtonText, { color: '#FFAA00', fontSize: 14 }]}>Visit Store</Text>
                </View>
              </TouchableOpacity>

              <TouchableOpacity
                style={[styles.groupButton, { backgroundColor: 'rgba(255, 61, 0, 0.1)', borderColor: Colors.secondary, borderWidth: 1, marginBottom: 16, paddingVertical: 12 }]}
                onPress={() => Linking.openURL('https://neogleamz.com/pages/contact')}
              >
                <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                  <MaterialCommunityIcons name="email-fast" size={20} color={Colors.secondary} style={{ marginRight: 8 }} />
                  <Text style={[styles.groupButtonText, { color: Colors.secondary, fontSize: 14 }]}>Contact Support</Text>
                </View>
              </TouchableOpacity>



              <TouchableOpacity
                style={{ paddingVertical: 12, alignItems: 'center' }}
                onPress={() => setIsSupportModalVisible(false)}
              >
                <Text style={{ color: Colors.textMuted, fontWeight: 'bold' }}>CLOSE</Text>
              </TouchableOpacity>
            </View>
          </View>
        </Modal>

      {/* Developer Modals migrated to Sandbox Auth logic */}
      <Sk8LytzProgrammerModal 
        visible={isProgrammerVisible} 
        onClose={() => {
            setIsProgrammerVisible(false);
            setIsAdminToolsVisible(true);
        }} 
        onExitToLogs={() => {
            setIsProgrammerVisible(false);
            setIsAdminToolsVisible(true);
        }}
        allDevices={(allDevices as any)}
        deviceConfigs={deviceConfigs as any}
        connectToDevice={async (d: any) => { await connectToDevice(d); }}
        disconnectFromDevice={async (_id: string) => { disconnectFromDevice(); }}
        writeToDevice={writeToDevice}
        isScanning={isScanning}
        isScanProbing={isScanProbing}
        handleScan={scanForPeripherals}
      />
      {/* LED Diagnostic Lab — long-press the SNIFFER button to open */}
      <Sk8LytzDiagnosticLab
        visible={isLabVisible ?? false}
        onClose={() => { 
            setIsLabVisible(false); 
            setIsAdminToolsVisible(true);
        }}
        connectedDevices={connectedDevices as any[]}
        writeToDevice={writeToDevice}
        liveRxPayload={lastRawNotification}
        hwSettings={activeHwSettings ?? undefined}
        allDevices={allDevices}
        isScanning={isScanning}
        handleScan={scanForPeripherals}
        connectToDevice={async (d: any) => { await connectToDevice(d); }}
        liveDeviceConfigs={deviceConfigs}
      />

      {/* HardwareSetupWizardScreen is conditionally returned at the top level instead of here */}



      {/* Crew Hub Modal */}
      <CrewModal
        visible={isCrewModalVisible}
        onClose={() => setIsCrewModalVisible(false)}
        activeSession={crewSession}
        activeRole={crewRole}
        currentModeSummary={crewModeSummary}
        lastLeaderScene={lastLeaderScene}
        onSessionReady={(session: CrewSession, role: CrewRole, lastScene: Record<string, any> | null) => {
          setCrewSession(session);
          setCrewRole(role);
          if (role === 'leader') {
            crewService.subscribeAsLeader(session.id, () => {});
          } else {
            crewService.subscribeAsMember(session.id, (scene) => {
              dockedControllerRef.current?.applyCloudScene(scene);
              setLastLeaderScene(scene); // track for member dashboard
            }, () => {
              // session_ended callback — leader ended the session
              setCrewSession(null);
              setCrewRole(null);
              setCrewModeSummary(undefined);
              setIsCrewModalVisible(false);
              Alert.alert('Session Ended', 'The crew leader has ended this session. Your skates will keep the current pattern.');
            });
            if (lastScene) {
              setLastLeaderScene(lastScene); // seed dashboard immediately from persisted DB scene
              setTimeout(() => dockedControllerRef.current?.applyCloudScene(lastScene), 300);
            }
          }
        }}
        onSessionLeft={() => {
          // Member left voluntarily — clear session and return to hub landing
          setCrewSession(null);
          setCrewRole(null);
          setCrewModeSummary(undefined);
          // Don't close modal — CrewModal will reset step to 'landing' via its useEffect
          // so the user lands back at the hub page naturally
        }}
        onSessionEnded={() => {
          // Leader ended the session — clear all session state
          setCrewSession(null);
          setCrewRole(null);
          setCrewModeSummary(undefined);
          // Don't force-close — CrewModal resets step to 'landing' via activeSession→null
        }}
      />

      {/* Account Management Modal */}
      <AccountModal
        visible={isAccountModalVisible}
        onClose={() => setIsAccountModalVisible(false)}
        onSignOut={handleLogout}
        onJoinCrewSession={(crewId) => {
          setPendingJoinCrewId(crewId);
          setIsCrewModalVisible(true);
          setIsAccountModalVisible(false);
        }}
        registeredDevices={registeredDevices.map((d) => ({
          id: d.id || '',
          mac: d.device_mac || '',
          name: d.device_name || '',
          customName: d.custom_name || '',
          groupName: d.group_name || '',
          type: d.product_type as any,
          registeredAt: d.registered_at,
        }))}
        onDeviceRenamed={async (deviceId, newName) => {
          setAllDevices((prev: any[]) => prev.map((d: any) =>
            d.id === deviceId ? { ...d, customName: newName } : d
          ));
          const rd = registeredDevices.find(r => r.device_mac === deviceId);
          if (rd) {
            await saveRegisteredDevice({ ...rd, custom_name: newName, is_pending_sync: true });
          }
        }}
        onDeviceForgotten={async (deviceId) => {
          setAllDevices((prev: any[]) => prev.filter((d: any) => d.id !== deviceId));
          await deregisterDevice(deviceId);
        }}
        onGroupRenamed={async (oldGroupName, newGroupName) => {
          const devs = registeredDevices.filter(d => d.group_name === oldGroupName);
          for (const d of devs) {
            await saveRegisteredDevice({ ...d, group_name: newGroupName, is_pending_sync: true });
          }
        }}
        onGroupForgotten={async (groupName) => {
          const devs = registeredDevices.filter(d => d.group_name === groupName);
          for (const d of devs) {
            await deregisterDevice(d.device_mac);
          }
        }}
      />
      
      {/* Admin Tools Hub (Replaces LogViewerModal) */}
      <AdminToolsModal
        visible={isAdminToolsVisible}
        onClose={() => setIsAdminToolsVisible(false)}
        onOpenProgrammer={() => {
          setIsAdminToolsVisible(false);
          setIsProgrammerVisible(true);
        }}
        onOpenLab={() => {
          setIsAdminToolsVisible(false);
          setIsLabVisible(true);
        }}
        allDevices={allDevices}
        connectedDevices={connectedDevices as any[]}
        isScanning={isScanning}
        handleScan={scanForPeripherals}
        writeToDevice={writeToDevice}
        liveRxPayload={lastRawNotification}
        liveDeviceConfigs={deviceConfigs}
        onConnectToDevice={async (d: any) => { await connectToDevice(d); }}
      />

      {/* ──── VOICE COMMAND ENGINE UI ──── */}
      <VoiceFAB 
        onPress={() => {
          if (!isVoiceTutorialDismissed) {
             setIsVoiceTutorialVisible(true);
          } else {
             setIsVoiceModalVisible(true);
          }
        }} 
        isListening={isListening}
      />
      
      <VoiceTutorialModal
        isVisible={isVoiceTutorialVisible}
        onDismiss={async () => {
          setIsVoiceTutorialVisible(false);
          dismissTutorial();
          // Smooth transition: open the actual voice modal after tutorial
          setTimeout(() => setIsVoiceModalVisible(true), 400);
        }}
      />

      <VoiceCommandModal
        isVisible={isVoiceModalVisible}
        onClose={() => setIsVoiceModalVisible(false)}
        isListening={isListening}
        transcript={transcript}
        error={voiceError}
      />
    </SafeAreaView>
  );
}

const createStyles = (Colors: import('../theme/theme').ThemePalette) => StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: Colors.background,
  },
  container: {
    flex: 1,
  },
  card: {
    backgroundColor: Colors.surface,
    borderRadius: Layout.borderRadius,
    padding: 16,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.1,
    shadowRadius: 12,
    elevation: 10,
  },
  scanButton: {
    marginTop: 24,
    backgroundColor: Colors.primary,
    paddingVertical: 14,
    borderRadius: Layout.borderRadius,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.6,
    shadowRadius: 8,
    elevation: 8,
  },
  scanButtonText: {
    color: Colors.isDark ? Colors.text : '#FFF',
    fontSize: 16,
    fontWeight: '800',
    letterSpacing: 1,
  },
  emptyStateContainer: {
    marginTop: 40,
    alignItems: 'center',
  },
  disconnectButtonSmall: {
    backgroundColor: Colors.surfaceHighlight,
    paddingVertical: 6,
    paddingHorizontal: 12,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: Colors.error + '44',
  },
  disconnectButtonTextSmall: {
    color: Colors.error,
    fontSize: 12,
    fontWeight: 'bold',
  },
  groupButton: {
    backgroundColor: Colors.secondary,
    paddingVertical: 14,
    borderRadius: Layout.borderRadius,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: Colors.secondary,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.6,
    shadowRadius: 8,
    elevation: 8,
  },
  groupButtonText: {
    color: Colors.isDark ? '#FFF' : Colors.background,
    fontSize: 16,
    fontWeight: '800',
    letterSpacing: 1,
  },
  countdownBadge: {
    position: 'absolute',
    right: -10,
    top: -10,
    width: 26,
    height: 26,
    borderRadius: 13,
    backgroundColor: '#FF7000',
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: '#FF7000',
    shadowRadius: 8,
    shadowOpacity: 1,
    zIndex: 50,
  },
  countdownText: {
    color: '#FFFFFF',
    fontSize: 14,
    fontWeight: '900',
  },
  errorContainer: {
    marginTop: 16,
    padding: 12,
    backgroundColor: 'rgba(255, 61, 0, 0.1)',
    borderRadius: 8,
    borderWidth: 1,
    borderColor: Colors.error
  },
  /* ──── 4-SLAB DASHBOARD STYLES ──── */
  headerSlab: {
    paddingBottom: 4,
    backgroundColor: 'rgba(0,0,0,0.5)',
    borderBottomWidth: 1,
    borderBottomColor: 'rgba(0,240,255,0.1)',
  },
  slabContainer: {
    paddingHorizontal: Layout.padding,
    marginBottom: 24,
  },
  slabHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 12,
    paddingHorizontal: 4,
  },
  slabTitle: {
    fontSize: 13,
    fontWeight: '900',
    color: 'rgba(255,255,255,0.6)',
    letterSpacing: 1.5,
    fontFamily: 'Righteous',
  },
  slabAction: {
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 4,
    backgroundColor: 'rgba(255,170,0,0.1)',
    borderWidth: 1,
    borderColor: 'rgba(255,170,0,0.3)',
  },
  slabActionText: {
    fontSize: 10,
    fontWeight: '900',
    color: '#FFAA00',
    letterSpacing: 0.5,
  },
  glassSlab: {
    backgroundColor: 'rgba(255,255,255,0.03)',
    borderRadius: 16,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.08)',
    padding: 16,
  },
  slabEmptyText: {
    fontSize: 12,
    color: 'rgba(255,255,255,0.4)',
    textAlign: 'center',
    lineHeight: 18,
  },
  activeCrewPill: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.05)',
    borderRadius: 12,
    padding: 12,
    gap: 10,
  },
  statusDot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    shadowOpacity: 1,
    shadowRadius: 4,
    elevation: 4,
  },
  activeCrewText: {
    fontSize: 13,
    fontWeight: '800',
    flex: 1,
    letterSpacing: 0.5,
  },
  skateCardWrapper: {
    marginBottom: 16,
    borderRadius: 20,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 10 },
    shadowOpacity: 0.3,
    shadowRadius: 15,
    elevation: 12,
  },
  skateCardGradient: {
    borderRadius: 20,
    padding: 2, // Border thickness
  },
  skateCardInner: {
    backgroundColor: Colors.isDark ? 'rgba(35, 42, 55, 0.85)' : 'rgba(255, 255, 255, 0.95)',
    borderRadius: 18,
    padding: 16,
    overflow: 'hidden',
  },
  skateCardRefraction: {
    position: 'absolute',
    top: -50,
    left: -50,
    width: 200,
    height: 200,
    backgroundColor: 'rgba(255, 255, 255, 0.03)',
    transform: [{ rotate: '45deg' }],
  },
  skateCardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 16,
  },
  avatarPill: {
    backgroundColor: 'rgba(255,255,255,0.05)',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 20,
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
  },
  avatarPillImage: {
    width: 20,
    height: 20,
  },
  avatarStatusDot: {
    width: 6,
    height: 6,
    borderRadius: 3,
    backgroundColor: Colors.success,
    marginLeft: 6,
    borderWidth: 1,
    borderColor: Colors.background,
  },
  telemetryContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
  },
  telemetryItem: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
  },
  rssiBars: {
    flexDirection: 'row',
    alignItems: 'flex-end',
    gap: 2,
  },
  rssiBar: {
    width: 3,
    borderRadius: 1,
  },
  skateCardContent: {
    marginBottom: 16,
  },
  skateCardGroupName: {
    fontSize: 22,
    fontWeight: '900',
    color: Colors.text,
    fontFamily: 'Righteous',
    letterSpacing: 0.5,
  },
  patternPill: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 4,
    backgroundColor: 'rgba(255,255,255,0.03)',
    alignSelf: 'flex-start',
    paddingHorizontal: 8,
    paddingVertical: 2,
    borderRadius: 10,
  },
  patternDot: {
    width: 6,
    height: 6,
    borderRadius: 3,
    marginRight: 6,
  },
  patternName: {
    fontSize: 10,
    fontWeight: '800',
    color: Colors.textMuted,
    letterSpacing: 1,
    textTransform: 'uppercase',
  },
  skateCardFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    borderTopWidth: 1,
    borderTopColor: 'rgba(255,255,255,0.05)',
    paddingTop: 12,
  },
  deviceCountText: {
    fontSize: 10,
    fontWeight: '700',
    color: Colors.textMuted,
    letterSpacing: 1,
  },
  powerIconCircle: {
    width: 28,
    height: 28,
    borderRadius: 14,
    alignItems: 'center',
    justifyContent: 'center',
  },
  deviceListFixed: {
    backgroundColor: 'rgba(255,255,255,0.02)',
    borderRadius: 16,
    padding: 8,
  }
});
