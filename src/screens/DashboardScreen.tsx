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
import { Layout, Typography, Spacing } from '../theme/theme';

import DeviceSettingsModal from '../components/DeviceSettingsModal';
import DockedController, { DockedControllerHandle } from '../components/DockedController';
import GroupSettingsModal from '../components/GroupSettingsModal';
import { BLEErrorBoundary } from '../components/shared/BLEErrorBoundary';

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
import HardwareSetupWizardScreen from './Onboarding/HardwareSetupWizardScreen';

// ─── Phase 1 Domain Hooks ──────────────────────────────────────────────────────
import { useDashboardAutoConnect } from '../hooks/useDashboardAutoConnect';
import { useDashboardGroups } from '../hooks/useDashboardGroups';
import { useDashboardProfile } from '../hooks/useDashboardProfile';
import { useDashboardCrew } from '../hooks/useDashboardCrew';
import { useDashboardDeviceConfig } from '../hooks/useDashboardDeviceConfig';
import { useDashboardVoice } from '../hooks/useDashboardVoice';
import { useHardwareNotifications } from '../hooks/useHardwareNotifications';
import type { DashboardViewState, DeviceSettings, CustomGroup } from '../types/dashboard.types';

// DeviceSettings and CustomGroup are now imported from '../types/dashboard.types'
// — migrated as part of Phase 1 Domain-Driven Refactor

import { SkateGroupCard } from '../components/dashboard/SkateGroupCard';
import CrewHubSlab from '../components/dashboard/CrewHubSlab';
import DashboardHeader from '../components/dashboard/DashboardHeader';
import MySkatesSlab from '../components/dashboard/MySkatesSlab';
import RegisteredFleetSlab from '../components/dashboard/RegisteredFleetSlab';
import SupportModal from '../components/dashboard/SupportModal';
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
    bleState,
    bleGateRef,
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
    authUsername,
    handleLogout,
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

  // ── Screen Navigation Telemetry ────────────────────────────────────────────
  useEffect(() => {
    AppLogger.log('SCREEN_OPENED', { screen: 'DashboardScreen' });
  }, []);

  // ── Hardware BLE callbacks (extracted to useHardwareNotifications) ───────────

  const [updateTrigger, setUpdateTrigger] = useState(0);
  const [isTestModeActive, setIsTestModeActive] = useState(false);
  // isDisconnecting removed — bleState === 'DISCONNECTING' is the canonical FSM gate
  const [lastRawNotification, setLastRawNotification] = useState<{deviceId: string, payloadHex: string} | null>(null);
  const [isDiagnosticsMode, setIsDiagnosticsMode] = useState(false);

  // ── Phase 1: Fleet Groups, Device Configs, Power States → useDashboardGroups ───────
  // Declare refs before domain hooks that consume them
  const allDevicesRef = React.useRef(allDevices);

  const [viewState, setViewState] = useState<DashboardViewState>('LOADING_REGS');
  const {
    customGroups,
    setCustomGroups,
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
    // runAutoProvisioning: removed — useDashboardAutoConnect owns auto-connect on boot.
    // runAutoProvisioning is preserved in useDashboardGroups for future manual-scan UI.
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
    deregisterDevice,
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

  // 🔶 Crew Hub state & auto-rejoin → useDashboardCrew
  const {
    crewSession,
    setCrewSession,
    crewRole,
    setCrewRole,
    isCrewModalVisible,
    setIsCrewModalVisible,
    crewModeSummary,
    setCrewModeSummary,
    lastLeaderScene,
    setLastLeaderScene,
    pendingJoinCrewId: _pendingJoinCrewId,
    setPendingJoinCrewId,
  } = useDashboardCrew({
    onApplyScene: (scene) => dockedControllerRef.current?.applyCloudScene(scene),
  });
  const [crewInitialStep, setCrewInitialStep] = useState<any>('landing');
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
    bleGateRef,
  });


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
  // handleScan + hasAutoScanned auto-fire useEffect removed:
  //   useDashboardAutoConnect (t+1500ms) owns the boot-time scan and auto-connect sequence.
  //   The inline auto-scan was a duplicate from before the domain-hook refactor:
  //   it fired its own scanForPeripherals() + runAutoProvisioning() at t+1s/t+8s,
  //   racing against useDashboardAutoConnect and mutating group configs mid-connection.

  useEffect(() => {
    customGroupsRef.current = customGroups;
  }, [customGroups]); // Keep ref in sync with hook-managed state

  // One-shot startup cleanup: prune simulator entries from @Sk8lytz_device_configs.
  // NOTE: customGroups and deviceConfigs state are now fully owned by useDashboardGroups.
  // This effect ONLY cleans sim- prefixed entries — it does NOT call setDeviceConfigs
  // to avoid a race condition with useDashboardGroups' own load.
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
    const macs = new Set(registeredDevices.map((d: any) => d.device_mac?.toUpperCase() ?? ''));
    return sortedAllDevices.filter((d: any) => macs.has(d.id?.toUpperCase() ?? ''));
  }, [sortedAllDevices, registeredDevices]);

  const availableDevicesData = useMemo(() => {
    const macs = new Set(registeredDevices.map((d: any) => d.device_mac?.toUpperCase() ?? ''));
    return sortedAllDevices.filter((d: any) => !macs.has(d.id?.toUpperCase() ?? ''));
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

  // 🔶 Device config mutation → useDashboardDeviceConfig
  const { saveSettings } = useDashboardDeviceConfig({
    selectedDeviceForSettings,
    customGroups,
    registeredDevices,
    saveRegisteredDevice,
    setAllDevices,
    allDevicesRef,
    setUpdateTrigger,
    setIsSettingsVisible,
  });

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
          <BLEErrorBoundary componentName="DockedController">
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
          </BLEErrorBoundary>
          {/* Disconnection Teardown Overlay — driven by FSM bleState */}
          {bleState === 'DISCONNECTING' && (
            <Animated.View style={{
              position: 'absolute', top: 0, left: 0, right: 0, bottom: 0,
              backgroundColor: 'rgba(0,0,0,0.85)',
              justifyContent: 'center', alignItems: 'center',
              zIndex: 9999
            }}>
              <ActivityIndicator size="large" color="#00F0FF" />
              <Text style={[Typography.header, { color: '#00F0FF', marginTop: Spacing.md }]}>Disconnecting...</Text>
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
    // IDENTITY FIX: Always resolve to BLE MAC address for all lookups.
    // RegisteredDevice.id is a Supabase composite key (MAC+userId).
    const mac = (item.device_mac || item.id || '').toUpperCase();
    const cachedConfig = deviceConfigs?.[mac] || {};
    const mergedItem = { 
      ...item, 
      ...cachedConfig,
      name: item.device_name || cachedConfig.name || item.name // Map DB field to component prop
    };

    return (
    <View style={{ paddingHorizontal: Layout.padding }}>
      <DeviceItem
        device={mergedItem}
        isConnected={displayConnectedDevices.some(d => d.id.toUpperCase() === mac)}
        isSelectionMode={isSelectionMode}
        isSelected={selectedIds.includes(mac)}
        onPress={async () => {
          if (isSelectionMode) {
            toggleDeviceSelection(mac);
            return;
          }
          // Resolve the live BLE peripheral by MAC.
          const bleDevice = allDevices.find(
            (d: any) => (d.id || '').toUpperCase() === mac
          );
          if (!bleDevice) {
            // Device not yet discovered — trigger a scan. useDashboardAutoConnect
            // observer will connect it automatically when it appears.
            AppLogger.log('BLE_STATE_CHANGE', { event: 'manual_connect_scan_triggered', mac });
            scanForPeripherals();
            return;
          }
          // connectToDevices (gated) — additive connection, preserves existing group members.
          await connectToDevices([bleDevice]);
          // Store firmware under MAC key — same key used by probe/0x63 responses.
          setDeviceConfigs((prev: any) => {
              const next = { ...prev, [mac]: { ...(prev?.[mac] || {}) } };
              AsyncStorage.setItem('@Sk8lytz_device_configs', JSON.stringify(next)).catch(() => {});
              return next;
          });
          writeToDevice(ZenggeProtocol.queryHardwareSettings(false), bleDevice.id);
        }}
        onLongPress={() => {
          openSettings(mergedItem);
        }}
        showGroupIcon={false}
        isPoweredOn={powerStates[mac] ?? true}
        onPowerToggle={() => handlePowerToggle([mac])}
      />
    </View>
    ); // close return
  }, [displayConnectedDevices, isSelectionMode, selectedIds, powerStates, deviceConfigs, allDevices, connectToDevices, scanForPeripherals, writeToDevice]);

  const mappedRegisteredDevicesForModal = useMemo(() => registeredDevices.map((d) => ({
    // IDENTITY KEY: always use device_mac (BLE MAC address), NOT d.id (Supabase UUID).
    // useDeviceFleet.loadDevices maps id: d.device_mac from the DB. The initialDevices
    // fallback path must be consistent or onDeviceForgotten/onDeviceRenamed receive a
    // UUID that deregisterDevice cannot match — causing silent, invisible no-ops.
    id: d.device_mac || '',
    mac: d.device_mac || '',
    name: d.device_name || '',
    customName: d.custom_name || '',
    groupName: d.group_name || '',
    type: d.product_type as any,
    registeredAt: d.registered_at,
    // Hardware fields — required for pill display in AccountModal Devices tab
    led_points: d.led_points,
    segments: d.segments,
    ic_type: d.ic_type,
    color_sorting: d.color_sorting,
  })), [registeredDevices]);



  const BluetoothWarningBanner = useMemo(() => {
    if (isBluetoothEnabled || Platform.OS === 'web') return null;
    return (
      <TouchableOpacity 
        onPress={() => Linking.openSettings()}
        style={{ 
          backgroundColor: Colors.error, 
          padding: Spacing.lg, 
          alignItems: 'center', 
          justifyContent: 'center', 
          flexDirection: 'row', 
          gap: Spacing.md,
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
            <View pointerEvents="box-none" style={{ paddingBottom: Spacing.lg, zIndex: 100, elevation: 100 }}>
              <DashboardHeader
                isActuallyConnected={isActuallyConnected}
                isOfflineMode={isOfflineMode}
                isTestModeActive={isTestModeActive}
                isDark={isDark}
                displayConnectedDevices={displayConnectedDevices}
                customGroups={customGroups}
                powerStates={powerStates}
                handleDisconnect={handleDisconnect}
                handlePowerToggle={handlePowerToggle}
                onPressAdminTools={() => setIsAdminToolsVisible(true)}
                onPressSupport={() => setIsSupportModalVisible(true)}
                onPressTheme={toggleTheme}
                authUsername={authUsername}
                onPressAccount={() => setIsAccountModalVisible(true)}
                insetTop={insets.top}
                Colors={Colors}
              />
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
                <DashboardHeader
                  isActuallyConnected={isActuallyConnected}
                  isOfflineMode={isOfflineMode}
                  isTestModeActive={isTestModeActive}
                  isDark={isDark}
                  displayConnectedDevices={displayConnectedDevices}
                  customGroups={customGroups}
                  powerStates={powerStates}
                  handleDisconnect={handleDisconnect}
                  handlePowerToggle={handlePowerToggle}
                  onPressAdminTools={() => setIsAdminToolsVisible(true)}
                  onPressSupport={() => setIsSupportModalVisible(true)}
                  onPressTheme={toggleTheme}
                  authUsername={authUsername}
                  onPressAccount={() => setIsAccountModalVisible(true)}
                  insetTop={insets.top}
                  Colors={Colors}
                />
             </View>

             <ScrollView 
               style={{ flex: 1 }} 
               contentContainerStyle={{ paddingBottom: insets.bottom + 60, flexGrow: 1 }}
               showsVerticalScrollIndicator={false}
             >
                {/* SLAB 2: CREW HUB */}
                <CrewHubSlab
                  crewSession={crewSession}
                  crewRole={crewRole}
                  isOfflineMode={isOfflineMode}
                  appSettings={appSettings}
                  windowHeight={windowHeight}
                  onOpenHub={() => { setCrewInitialStep('landing'); setIsCrewModalVisible(true); }}
                  onOpenMap={() => { setCrewInitialStep('map'); setIsCrewModalVisible(true); }}
                  Colors={Colors}
                  styles={styles}
                />


                {/* SLAB 3: MY SKATES */}
                <MySkatesSlab
                  customGroups={customGroups}
                  lastGroupPatterns={lastGroupPatterns}
                  allDevices={allDevices}
                  registeredDevices={registeredDevices}
                  powerStates={powerStates}
                  userProfile={userProfile}
                  onGroupPress={(group: CustomGroup) => {
                    const devicesToConnect = allDevices.filter(d => group.deviceIds.includes(d.id.toUpperCase()));
                    if (devicesToConnect.length > 0) {
                      connectToDevices(devicesToConnect);
                    } else {
                      // No BLE devices discovered yet — trigger scan and inform user
                      AppLogger.log('BLE_STATE_CHANGE', { event: 'group_tap_no_ble_devices', groupId: group.id, expectedMacs: group.deviceIds });
                      scanForPeripherals();
                      Alert.alert('Scanning...', 'Your skates aren\'t visible yet. Scanning now — tap again in a few seconds.');
                    }
                  }}
                  onGroupLongPress={(id: string) => openGroupRename(id)}
                  onSetupWizard={() => setViewState('SETUP_WIZARD')}
                  Colors={Colors}
                  styles={styles}
                />

                {/* Flexible spacer — only pushes content on large screens */}
                <View style={{ flex: 1, minHeight: windowHeight < 720 ? 0 : 20 }} />

                {/* SLAB 4: REGISTERED FLEET */}
                <RegisteredFleetSlab
                  registeredDevices={registeredDevices}
                  isRegisteredCollapsed={isRegisteredCollapsed}
                  onToggleCollapse={() => setIsRegisteredCollapsed(!isRegisteredCollapsed)}
                  onSetupWizard={() => setViewState('SETUP_WIZARD')}
                  renderItem={renderItem}
                  Colors={Colors}
                  styles={styles}
                />
              </ScrollView>
          </View>
        )}
        <DeviceSettingsModal
          isVisible={isSettingsVisible}
          onClose={() => setIsSettingsVisible(false)}
          onSave={saveSettings}
          writeToDevice={writeToDevice}
          initialSettings={(() => {
            const sDef = selectedDeviceForSettings as any;
            const targetMac = (sDef?.device_mac || sDef?.id || '').toUpperCase();
            const dCfg = deviceConfigs[targetMac] || {};
            
            const hasProbeData = (sDef?.points > 0 && sDef?.stripType !== 'UNKNOWN' && sDef?.stripType);
            const hasManualData = !!dCfg?.userConfiguredAt;
            const provenance = hasManualData ? 'MANUALLY_CONFIGURED' : (hasProbeData ? 'PROBED' : 'UNCONFIGURED');

            return {
              name: selectedDeviceForSettings?.name || LOCAL_PRODUCT_CATALOG[0].displayName,
              type: getLocalProfileByPoints(sDef?.points ?? 0).id,
              provenance,
              points: provenance === 'UNCONFIGURED' ? undefined : (dCfg?.points || sDef?.points),
              segments: provenance === 'UNCONFIGURED' ? undefined : (dCfg?.segments || sDef?.segments),
              stripType: provenance === 'UNCONFIGURED' ? undefined : (dCfg?.stripType || sDef?.stripType),
              sorting: provenance === 'UNCONFIGURED' ? undefined : (dCfg?.sorting || sDef?.sorting),
              grouped: !!dCfg?.groupId || sDef?.grouped || false,
              groupId: dCfg?.groupId || sDef?.groupId,
              groupName: customGroups.find(g => g.id === (dCfg?.groupId || sDef?.groupId))?.name || getDefaultGroupName(sDef?.product_type || sDef?.type),
              firmware: dCfg?.firmware || sDef?.firmware || (sDef?.id?.startsWith('sim-') ? 'v2.0.1.DEMO' : 'Unknown')
            };
          })()}
          groups={customGroups}
          deviceName={(() => {
            const sDef = selectedDeviceForSettings as any;
            const targetMac = (sDef?.device_mac || sDef?.id || '').toUpperCase();
            const rd = registeredDevices.find(r => r.device_mac.toUpperCase() === targetMac);
            return rd?.custom_name || rd?.device_name || sDef?.name;
          })()}
          onDeregister={(() => {
            // Only provide onDeregister if the device is actually registered
            const sDef = selectedDeviceForSettings as any;
            const targetMac = (sDef?.device_mac || sDef?.id || '').toUpperCase();
            const isRegistered = registeredDevices.some(r => r.device_mac.toUpperCase() === targetMac);
            if (!isRegistered) return undefined;
            return async () => {
              setIsSettingsVisible(false);
              setAllDevices((prev: any[]) => prev.filter((d: any) => d.id.toUpperCase() !== targetMac));
              await deregisterDevice(targetMac);
            };
          })()}
        />

        <GroupSettingsModal
          isVisible={groupModalState !== 'HIDDEN'}
          onClose={closeGroupModal}
          onSave={saveGroup}
          onDelete={groupModalState === 'RENAME' && editingGroupId ? () => handleGroupDelete(editingGroupId) : undefined}
          initialName={groupModalState === 'RENAME' ? customGroups.find(g => g.id === editingGroupId)?.name : getDefaultGroupName()}
          initialDeviceIds={groupModalState === 'RENAME' ? customGroups.find(g => g.id === editingGroupId)?.deviceIds : selectedIds}
          allDevices={registeredDevices.map(rd => ({
            // Use the registered fleet as the pool — NOT the BLE scan.
            // BLE scan (allDevices) only has currently-connected/discovered devices.
            // Group membership is a persistent concept that must survive being offline.
            id: rd.device_mac.toUpperCase(),
            name: rd.custom_name || rd.device_name || rd.device_mac,
            // Show connection status as a hint — doesn't gate visibility
            connected: allDevices.some(d => d.id.toUpperCase() === rd.device_mac.toUpperCase()),
          }))}
        />

      </View>
      


        <SupportModal
          visible={isSupportModalVisible}
          onClose={() => setIsSupportModalVisible(false)}
          Colors={Colors}
          styles={styles}
        />

      {/* HardwareSetupWizardScreen is conditionally returned at the top level instead of here */}



      {/* Crew Hub Modal */}
      <CrewModal
        visible={isCrewModalVisible}
        onClose={() => setIsCrewModalVisible(false)}
        initialStep={crewInitialStep}
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
        isOfflineMode={isOfflineMode}
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
          // Sync local customGroups array so the dashboard UI instantly re-renders
          const updatedGroups = customGroupsRef.current.map(g => 
            g.name === oldGroupName ? { ...g, name: newGroupName } : g
          );
          setCustomGroups(updatedGroups);
          AsyncStorage.setItem('@Sk8lytz_custom_groups', JSON.stringify(updatedGroups)).catch(() => {});
        }}
        onGroupForgotten={async (groupName) => {
          const devs = registeredDevices.filter(d => d.group_name === groupName);
          for (const d of devs) {
            await deregisterDevice(d.device_mac);
          }
          // Also forcibly scrub the group from the dashboard cache if it exists there
          const group = customGroups.find(g => g.name === groupName);
          if (group) {
            await handleGroupDelete(group.id);
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
        onConnectToDevice={async (d: any) => { await connectToDevices([d]); }}
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


