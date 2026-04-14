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
import { MaterialCommunityIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import React, { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { ActivityIndicator, Alert, Animated, AppState, AppStateStatus, BackHandler, Dimensions, Image, Linking, Modal, PanResponder, Platform, SafeAreaView, ScrollView, Text, TouchableOpacity, View, useWindowDimensions } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import DeviceItem from '../components/DeviceItem';
import { useTheme } from '../context/ThemeContext';
import useBLE from '../hooks/useBLE';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { Layout, Typography } from '../theme/theme';

import DeviceSettingsModal from '../components/DeviceSettingsModal';
import DockedController, { DockedControllerHandle } from '../components/DockedController';
import GroupSettingsModal from '../components/GroupSettingsModal';

import AdminToolsModal from '../components/admin/AdminToolsModal';
import { CrewModal } from '../components/CrewModal';
import VoiceCommandModal from '../components/Voice/VoiceCommandModal';
import VoiceFAB from '../components/Voice/VoiceFAB';
import VoiceTutorialModal from '../components/Voice/VoiceTutorialModal';
import { AppLogger } from '../services/AppLogger';
import { CrewRole, crewService, CrewSession } from '../services/CrewService';

import AccountModal from '../components/AccountModal';
import { getDefaultGroupName } from '../utils/NamingUtils';
import CrewMemberDashboard from '../components/CrewMemberDashboard';
import { getLocalProfileByPoints, LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import { RegisteredDevice, useRegistration } from '../hooks/useRegistration';
import { supabase } from '../services/supabaseClient';
import HardwareSetupWizardScreen from './Onboarding/HardwareSetupWizardScreen';

// ─── Phase 1 Domain Hooks ──────────────────────────────────────────────────────
import { useDashboardAutoConnect } from '../hooks/useDashboardAutoConnect';
import { useDashboardGroups } from '../hooks/useDashboardGroups';
import { useDashboardProfile } from '../hooks/useDashboardProfile';
import { useDashboardVoice } from '../hooks/useDashboardVoice';
import { useHardwareNotifications } from '../hooks/useHardwareNotifications';
import type { DashboardViewState, DeviceSettings } from '../types/dashboard.types';

// DeviceSettings and CustomGroup are now imported from '../types/dashboard.types'
// — migrated as part of Phase 1 Domain-Driven Refactor

import { SkateGroupCard } from '../components/dashboard/SkateGroupCard';
import { createDashboardStyles, getPatternColors } from '../styles/DashboardStyles';

export default function DashboardScreen({ isOfflineMode = false, onLogout }: { isOfflineMode?: boolean; onLogout?: () => void } = {}) {
  const { Colors, isDark, toggleTheme } = useTheme();
  const insets = useSafeAreaInsets();
  const { height: windowHeight, width: windowWidth } = useWindowDimensions();
  const styles = createDashboardStyles(Colors, windowHeight, windowWidth);
  const {
    scanForPeripherals,
    allDevices,
    setAllDevices,
    connectToDevice,
    connectToDevices,
    connectedDevices,
    disconnectFromDevice,
    writeToDevice,
    // isScanning / isScanProbing removed — bleState FSM is the canonical source of truth
    isBluetoothSupported,
    isBluetoothEnabled,
    requestPermissions,
    setOnDataReceived,
    setOnHardwareProbed,
    setOnDeviceRecovered,
    droppedOutDeviceIds,
    pendingRegistrations,
    clearPendingRegistrations,
    bleState
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

  // ── Hardware BLE callbacks (extracted to useHardwareNotifications) ───────────

  const [updateTrigger, setUpdateTrigger] = useState(0);
  const [isTestModeActive, setIsTestModeActive] = useState(false);
  // isDisconnecting removed — bleState === 'DISCONNECTING' is the canonical FSM gate
  const [lastRawNotification, setLastRawNotification] = useState<{deviceId: string, payloadHex: string} | null>(null);
  const [isDiagnosticsMode, setIsDiagnosticsMode] = useState(false);

  // ── Phase 1: Fleet Groups, Device Configs, Power States → useDashboardGroups ───────
  // Declare refs before domain hooks that consume them
  const allDevicesRef = React.useRef(allDevices);
  const lastProcessedRef = React.useRef<string>('');

  const [viewState, setViewState] = useState<DashboardViewState>('LOADING_REGS');
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
    runAutoProvisioning,
    isProvisioningTriggered,
    saveGroup,
    handleGroupDelete,
  } = useDashboardGroups({
    registeredDevices,
    saveAllRegisteredDevices,
    saveRegisteredDevice,
    migrateLegacyGroups,
    clearPendingRegistrations,
    getAllScannedDevices: () => allDevicesRef.current,
    setAllDevices,
    allDevicesRef,
    onRegistrationComplete: () => {
      setViewState('DASHBOARD');
      wizardCheckedRef.current = false;
    },
  });

  // ── Derived connection state (hoisted above all consumers) ────────────────
  // CRITICAL: These must be declared before useDashboardAutoConnect, handleScan,
  // and any useEffect/useCallback that closes over isActuallyConnected.
  // Having two separate definitions of this concept was the root cause of the
  // 'Rendered more hooks than previous render' crash.
  const displayConnectedDevices = useMemo(() => {
    return connectedDevices.map(d => {
      const cfg = deviceConfigs[(d as any).id] || {};
      return { ...d, ...cfg };
    });
  }, [connectedDevices, deviceConfigs]);

  const isActuallyConnected = displayConnectedDevices.length > 0;
  const isGrouped = displayConnectedDevices.length > 1 && displayConnectedDevices.every(d => (d as any).grouped);

  // ── Crew Hub state (stays in DashboardScreen — feeds BLE write dispatch) ───────
  const [crewSession, setCrewSession] = useState<CrewSession | null>(null);
  const [crewRole, setCrewRole] = useState<CrewRole>(null);
  const [isCrewModalVisible, setIsCrewModalVisible] = useState(false);
  const [crewModeSummary, setCrewModeSummary] = useState<string | undefined>(undefined);
  const [lastLeaderScene, setLastLeaderScene] = useState<Record<string, any> | null>(null);
  const [_pendingJoinCrewId, setPendingJoinCrewId] = useState<string | null>(null);
  const dockedControllerRef = React.useRef<DockedControllerHandle>(null);

  // Relay Soft Disconnect recoveries down to the DockedController for silent payload blasting
  useEffect(() => {
    setOnDeviceRecovered((deviceId: string) => {
      dockedControllerRef.current?.replayStateToDevice(deviceId);
    });
  }, [setOnDeviceRecovered]);

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

  // NOTE: auto-scan on mount is handled by the hasAutoScanned effect below (requires viewState === 'DASHBOARD')

  // 1. Check FTUE state on mount
  useEffect(() => {
    hasCloudRegistrations().then(hasAny => {
      if (!hasAny) {
        setViewState('SETUP_WIZARD');
      } else {
        setViewState('DASHBOARD');
      }
    });
  }, []);

  // 2. Continuous listener for new devices beyond FTUE
  useEffect(() => {
    if (pendingRegistrations.length === 0) return;
    if (viewState === 'SETUP_WIZARD') return; // Ignore if wizard is already active
    
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

  // The Drop-out UI Alert has been excised per user mandate.
  // We no longer spam the user when hardware connections drop out organically.

  // User Profile
  const [authUsername, setAuthUsername] = useState<string | null>(null);

  // Load cached username on mount for instant UI feedback
  useEffect(() => {
    AsyncStorage.getItem('@Sk8lytz_auth_username').then(val => {
      if (val && !authUsername) setAuthUsername(val);
    }).catch(() => {});
  }, []);

  // Derive username reactively from userProfile context
  useEffect(() => {
    if (supabase) {
      supabase.auth.getSession().then(({ data: { session } }: { data: { session: any } }) => {
        const dbDisplay = userProfile?.display_name?.trim();
        const dbUser = userProfile?.username?.trim();
        
        const sessionEmailPrefix = session?.user?.email?.split('@')[0];
        const fallback = dbDisplay || dbUser || sessionEmailPrefix || 'GUEST';
        
        setAuthUsername(fallback);
        AsyncStorage.setItem('@Sk8lytz_auth_username', fallback).catch(() => {});
      }).catch(() => {});
    }
  }, [userProfile, supabase]);

  const handleLogout = async () => {
     try {
       await supabase.auth.signOut();
       // App.tsx onAuthStateChange will detect session=null and redirect to AuthScreen automatically
     } catch (e) {
       AppLogger.error('Logout error:', e);
     }
  };

  // ── Cloud Sync & BLE Auto-Connect (extracted to useDashboardAutoConnect) ──
  useDashboardAutoConnect({
    isBluetoothSupported,
    isBluetoothEnabled,
    isActuallyConnected, // now canonical — hoisted displayConnectedDevices above
    allDevices,
    connectedDevices,
    connectToDevices,
    scanForPeripherals,
    requestPermissions,
    refreshProfile,
    registeredDevices,
  });

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

  useHardwareNotifications({
    isDiagnosticsMode,
    setOnDataReceived,
    setOnHardwareProbed,
    allDevices,
    setAllDevices,
    setDeviceConfigs,
    deviceConfigs,
    setLastRawNotification,
  });


  // Analytics / Dev tools hooks migrated to AuthScreen / removed

  const handleScan = () => {
    if (bleState === 'SCANNING' || isActuallyConnected) return;
    
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
    if (viewState === 'DASHBOARD' && !hasAutoScanned.current) {
      if (connectedDevices.length === 0 && bleState !== 'SCANNING') {
        hasAutoScanned.current = true;
        // Wait 1 second to let dashboard natively render before hammering BLE stack
        setTimeout(() => {
          handleScan();
        }, 1000);
      }
    }
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [viewState, connectedDevices.length, bleState]);

  useEffect(() => {
    customGroupsRef.current = customGroups;
  }, [customGroups]); // Keep ref in sync with hook-managed state

  // One-shot startup cleanup: prune simulator entries from AsyncStorage.
  // NOTE: customGroups and deviceConfigs state are now fully owned by useDashboardGroups.
  // This effect ONLY cleans sim- prefixed entries and ng_processed_devices — it does NOT
  // call setDeviceConfigs to avoid a race condition with useDashboardGroups' own load.
  useEffect(() => {
    // Prune sim-device entries from device configs (without overwriting hook state)
    AsyncStorage.getItem('@Sk8lytz_device_configs')
      .then(res => {
        if (!res) return;
        try {
          const configs = JSON.parse(res) || {};
          const hasSim = Object.keys(configs).some(k => k.startsWith('sim-'));
          if (hasSim) {
            for (const key in configs) { if (key.startsWith('sim-')) delete configs[key]; }
            AsyncStorage.setItem('@Sk8lytz_device_configs', JSON.stringify(configs)).catch(() => {});
          }
        } catch (e: any) { AppLogger.warn('JSON parse error configs cleanup', { error: String(e) }); }
      })
      .catch((e: any) => AppLogger.warn('AsyncStorage error configs cleanup', { error: String(e) }));

    // Prune sim-device entries from processed devices log
    AsyncStorage.getItem('@Sk8lytz_processed_devices')
      .then(res => {
        if (!res) return;
        try {
          const processed = JSON.parse(res) || [];
          const cleaned = processed.filter((id: string) => !id.startsWith('sim-'));
          if (cleaned.length !== processed.length) {
            AsyncStorage.setItem('@Sk8lytz_processed_devices', JSON.stringify(cleaned)).catch(() => {});
          }
        } catch (e) {}
      });
  }, []);

  // displayConnectedDevices, isActuallyConnected, isGrouped hoisted to top of component (after useDashboardGroups).

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
    if (bleState === 'DISCONNECTING') return;
    await disconnectFromDevice();
  }, [disconnectFromDevice, bleState]);

  useEffect(() => {
    const handleBackPress = () => {
      if (isTestModeActive) {
        setIsTestModeActive(false);
        return true; // intercept
      }
      if (bleState === 'DISCONNECTING') {
        return true; // intercept and block multiple back presses during teardown
      }
      if (isActuallyConnected) {
        handleDisconnect();
        return true; // intercept and exit to scanner
      }
      return false; // allow native exit
    };

    const backHandler = BackHandler.addEventListener('hardwareBackPress', handleBackPress);
    return () => backHandler.remove();
  }, [isTestModeActive, isActuallyConnected, handleDisconnect, bleState]);

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
          else if (isActuallyConnected && bleState !== 'DISCONNECTING') handleDisconnect();
        }
      },
      onPanResponderTerminate: (evt, gestureState) => {
        if (gestureState.dx > 60) {
          if (isTestModeActive) setIsTestModeActive(false);
          else if (isActuallyConnected && bleState !== 'DISCONNECTING') handleDisconnect();
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

  // ── handleGroupDelete and saveGroup now live in useDashboardGroups ────────────


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
         const stored = await AsyncStorage.getItem('@Sk8lytz_device_configs');
         const configs = stored ? JSON.parse(stored) : {};
         configs[selectedDeviceForSettings.id] = { ...settings, groupId: finalGroupId };
         await AsyncStorage.setItem('@Sk8lytz_device_configs', JSON.stringify(configs));
         
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
            points={(displayConnectedDevices[0] as any).points}
            devices={displayConnectedDevices as any}
            onLongPressDevice={openSettings}
            writeToDevice={writeToDevice}
            isPoweredOn={displayConnectedDevices.some(d => powerStates[d.id] ?? true)}
            onDisconnect={handleDisconnect}
            crewRole={crewRole}
            appSettings={appSettings}
            onCrewSceneChange={(scene: Record<string, any>) => crewService.broadcastScene(scene)}
            bleState={bleState}
            onPatternChanged={(patternName: string) => {
              // Ensure we only bind this to a physical hardware controller view, not when
              // we don't have a specific group/skate selected.
              const targetGroupId = displayConnectedDevices[0]?.groupId || displayConnectedDevices[0]?.id;
              if (targetGroupId && patternName !== lastGroupPatterns[targetGroupId]) {
                setLastGroupPattern(targetGroupId, patternName);
              }
            }}
          />
          {/* Disconnection Teardown Overlay — driven by FSM bleState */}
          {bleState === 'DISCONNECTING' && (
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
  }, [isActuallyConnected, isGrouped, displayConnectedDevices, writeToDevice, powerStates, isTestModeActive, activeHwSettings, crewRole, crewSession, lastLeaderScene, bleState]);

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
                AsyncStorage.setItem('@Sk8lytz_device_configs', JSON.stringify(next)).catch(() => {});
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

  const mappedRegisteredDevicesForModal = useMemo(() => registeredDevices.map((d) => ({
    id: d.id || '',
    mac: d.device_mac || '',
    name: d.device_name || '',
    customName: d.custom_name || '',
    groupName: d.group_name || '',
    type: d.product_type as any,
    registeredAt: d.registered_at,
  })), [registeredDevices]);

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

  switch (viewState) {
    case 'LOADING_REGS':
      return (
        <View style={[styles.container, { backgroundColor: Colors.background, justifyContent: 'center', alignItems: 'center' }]}>
          <ActivityIndicator color={Colors.primary} size="large" />
        </View>
      );
    case 'SETUP_WIZARD':
      return <HardwareSetupWizardScreen onSetupComplete={async (devices) => { await handleRegistrationComplete(devices, allDevices); }} />;
    default:
      break;
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

             <ScrollView 
               style={{ flex: 1 }} 
               contentContainerStyle={{ paddingBottom: insets.bottom + 60, flexGrow: 1 }}
               showsVerticalScrollIndicator={false}
             >
                {/* SLAB 2: CREW HUB (Sessions) */}
                <View style={[styles.slabContainer, { marginTop: 12 }]}>
                  <View style={[styles.glassSlab, { 
                    borderColor: isOfflineMode ? 'rgba(255,255,255,0.05)' : 'rgba(255,170,0,0.2)', 
                    paddingVertical: isOfflineMode ? 16 : (windowHeight < 720 ? 16 : 40) 
                  }]}>
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
                          onPress={() => setViewState('SETUP_WIZARD')}
                          style={[styles.scanButton, { marginTop: 16, width: '70%', backgroundColor: Colors.primary }]}
                        >
                          <Text style={styles.scanButtonText}>SET UP YOUR SKATES</Text>
                        </TouchableOpacity>
                      )}
                    </View>
                  )}
                </View>

                {/* Flexible spacer — only pushes content on large screens */}
                <View style={{ flex: 1, minHeight: windowHeight < 720 ? 0 : 20 }} />

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
                       onPress={() => setViewState('SETUP_WIZARD')}
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
                          onPress={() => setViewState('SETUP_WIZARD')}
                          style={[styles.scanButton, { marginTop: 16, width: '60%' }]}
                        >
                          <Text style={styles.scanButtonText}>START SETUP</Text>
                        </TouchableOpacity>
                      </View>
                    )
                  )}
                </View>
              </ScrollView>
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
            groupName: customGroups.find(g => g.id === (deviceConfigs[selectedDeviceForSettings?.id || '']?.groupId || (selectedDeviceForSettings as any)?.groupId))?.name || getDefaultGroupName((selectedDeviceForSettings as any)?.product_type || (selectedDeviceForSettings as any)?.type),
            firmware: deviceConfigs[selectedDeviceForSettings?.id || '']?.firmware || (selectedDeviceForSettings as any)?.firmware || ((selectedDeviceForSettings as any)?.id?.startsWith('sim-') ? 'v2.0.1.DEMO' : 'Unknown')
          }}
          groups={customGroups}
        />
        <GroupSettingsModal
          isVisible={groupModalState !== 'HIDDEN'}
          onClose={closeGroupModal}
          onSave={saveGroup}
          onDelete={groupModalState === 'RENAME' && editingGroupId ? () => handleGroupDelete(editingGroupId) : undefined}
          initialName={groupModalState === 'RENAME' ? customGroups.find(g => g.id === editingGroupId)?.name : getDefaultGroupName()}
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
        registeredDevices={mappedRegisteredDevicesForModal}
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
        allDevices={allDevices}
        connectedDevices={connectedDevices as any[]}
        bleState={bleState}
        handleScan={() => scanForPeripherals()}
        writeToDevice={writeToDevice}
        liveRxPayload={lastRawNotification}
        liveDeviceConfigs={deviceConfigs}
        onConnectToDevice={async (d: any) => { await connectToDevice(d); }}
        onDisconnectFromDevice={async (_id: string) => { disconnectFromDevice(); }}
        isDiagnosticsMode={isDiagnosticsMode}
        onToggleDiagnostics={() => setIsDiagnosticsMode(!isDiagnosticsMode)}
        hwSettings={activeHwSettings}
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


