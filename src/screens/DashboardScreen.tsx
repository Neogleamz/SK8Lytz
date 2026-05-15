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
import React, { startTransition, useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { ActivityIndicator, Alert, Animated, AppState, AppStateStatus, BackHandler, Dimensions, Image, Linking, Modal, PanResponder, Platform, SafeAreaView, ScrollView, Text, TouchableOpacity, View, useWindowDimensions } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import * as ExpoLinking from 'expo-linking';
import DeviceItem from '../components/DeviceItem';
import { useTheme } from '../context/ThemeContext';
import useBLE from '../hooks/useBLE';
import { Layout, Typography, Spacing } from '../theme/theme';

import { DockedControllerHandle } from '../components/DockedController';
import GroupSettingsModal from '../components/GroupSettingsModal';
import { BLEErrorBoundary } from '../components/shared/BLEErrorBoundary';

import AdminToolsModal from '../components/admin/AdminToolsModal';

import { AppLogger } from '../services/AppLogger';
import { CrewRole, crewService, CrewSession } from '../services/CrewService';

import AccountModal, { StoredDevice } from '../components/AccountModal';
import { getDefaultGroupName } from '../utils/NamingUtils';
import { getLocalProfileByPoints, LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import { RegisteredDevice, useRegistration } from '../hooks/useRegistration';
import HardwareSetupWizardScreen from './Onboarding/HardwareSetupWizardScreen';
import { useGlobalTelemetry } from '../hooks/useGlobalTelemetry';
import { useHealthTelemetry } from '../hooks/useHealthTelemetry';
import { DashboardTelemetryHero } from '../components/dashboard/DashboardTelemetryHero';

// ─── Phase 1 Domain Hooks ──────────────────────────────────────────────────────
import { useDashboardAutoConnect } from '../hooks/useDashboardAutoConnect';
import { useDashboardGroups } from '../hooks/useDashboardGroups';
import { useDashboardProfile } from '../hooks/useDashboardProfile';
import { useDashboardCrew } from '../hooks/useDashboardCrew';
import { useDashboardDeviceConfig } from '../hooks/useDashboardDeviceConfig';

import { useHardwareNotifications } from '../hooks/useHardwareNotifications';
import { useDeviceStateLedger, normalizeMac } from '../hooks/useDeviceStateLedger';
import { useTelemetryLedger } from '../hooks/useTelemetryLedger';
import type { DashboardViewState, DeviceSettings, CustomGroup, DisplayDevice, IDeviceState } from '../types/dashboard.types';

// DeviceSettings and CustomGroup are now imported from '../types/dashboard.types'
// — migrated as part of Phase 1 Domain-Driven Refactor

import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { SkateGroupCard } from '../components/dashboard/SkateGroupCard';
import DashboardCrewPanel from '../components/dashboard/DashboardCrewPanel';
import { useDashboardController } from '../hooks/useDashboardController';
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
  // ── Device State Ledger — unified per-device pattern state ────────────────────
  // Destructure to STABLE function refs (each is a useCallback with [] deps inside the hook).
  // Do NOT pass `ledger` as an object — it's a new object reference on every render,
  // which would break FlatList performance by invalidating renderItem's useCallback.
  const { save: ledgerSave, loadSync: ledgerLoadSync } = useDeviceStateLedger();
  const {
    scanForPeripherals,
    allDevices,
    setAllDevices,
    connectToDevices,
    connectedDevices,
    disconnectFromDevice,
    forceDisconnect,
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
    setPendingRegistrations,
    bleState,
    bleGateRef,
    pingDevice,
    startSweeper,
    stopSweeper,
    isSweeperActive,
    burstScan,
    ghostedDeviceIds,
    // NOTE: registeredMacs is passed to useBLE after useRegistration() via registeredMacsRef below
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

  // NOTE: useBLE's Sweeper receives registeredMacs as a plain array parameter.
  // Since useBLE is called once per component lifecycle (no conditional re-call),
  // we keep registeredMacs in a ref and notify the Sweeper to skip Fleet MACs
  // using a side-channel. The Sweeper already re-reads registeredMacs on every
  // Interrogator invocation, so this ref stays live without re-instantiating useBLE.
  // (registeredMacs initial value = [] on first render — safe because Interrogator
  // has a 2s queue delay before any probe fires.)

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

  // ── Deep Link Handling ─────────────────────────────────────────────────────
  const [initialDeepLinkCode, setInitialDeepLinkCode] = useState<string | null>(null);
  useEffect(() => {
    const handleDeepLink = ({ url }: { url: string }) => {
      if (!url) return;
      try {
        const parsed = ExpoLinking.parse(url) as { path?: string; queryParams?: Record<string, string> };
        if (parsed.path === 'crew/join' && parsed.queryParams?.code) {
          const inviteCode = String(parsed.queryParams.code).toUpperCase();
          AppLogger.log('DEEP_LINK', { action: 'crew_join', inviteCode });
          setInitialDeepLinkCode(inviteCode);
          setCrewInitialStep('join');
          setIsCrewModalVisible(true);
        }
      } catch (err) {
        AppLogger.error('DEEP_LINK', { event: 'parse_failed', url, error: String(err) });
      }
    };
    const linkSubscription = Linking.addEventListener('url', handleDeepLink);
    Linking.getInitialURL().then(url => { if (url) handleDeepLink({ url }); });
    return () => linkSubscription.remove();
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
  // Stable ref to retriggerAutoConnect — bridges the forward-reference since
  // useDashboardAutoConnect is declared after useDashboardGroups (hook order constraint).
  const retriggerAutoConnectRef = React.useRef<() => void>(() => {});
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
      // FIX: Re-trigger auto-connect after Wizard completes.
      // Resets the one-shot gate so the newly registered device is queued
      // for connection the moment it appears in the BLE scan — no user tap needed.
      retriggerAutoConnectRef.current();
    },
  });

  // ── Derived connection state (hoisted above all consumers) ────────────────
  // CRITICAL: These must be declared before useDashboardAutoConnect, handleScan,
  // and any useEffect/useCallback that closes over isActuallyConnected.
  // Having two separate definitions of this concept was the root cause of the
  // 'Rendered more hooks than previous render' crash.
  const displayConnectedDevices = useMemo((): DisplayDevice[] => {
    return connectedDevices.map(d => {
      const mac = d.id?.toUpperCase() ?? '';
      const cfg = (deviceConfigs[mac] || deviceConfigs[d.id] || {}) as Partial<DeviceSettings>;
      // GAP 1 FIX: Inject `type` from registeredDevices.product_type when not present
      // in the live BLE device object or deviceConfigs. On cold start before EEPROM
      // probe fires, cfg.type is undefined — causing SOULZ to silently resolve to HALOZ
      // (RING visualizer) because getLocalProfileByPoints(0) returns the first catalog entry.
      // registeredDevices is populated from AsyncStorage via useRegistration → DeviceRepository
      // on mount (always available offline) and is the canonical post-wizard product type source.
      const rd = registeredDevices.find(
        (r: RegisteredDevice) => r.device_mac?.toUpperCase() === mac
      );
      const resolvedType = cfg.type || (d as DisplayDevice).type || rd?.product_type || undefined;
      return { ...d, ...cfg, ...(resolvedType ? { type: resolvedType } : {}) } as DisplayDevice;
    });
  }, [connectedDevices, deviceConfigs, registeredDevices]);

  const isActuallyConnected = displayConnectedDevices.length > 0;
  // BUG FIX: was `every(d => d.grouped)` — fails for re-provisioned devices missing the `.grouped` flag.
  // Fix: check `groupId` presence (the canonical group membership token from DeviceRepository).
  const isGrouped = displayConnectedDevices.length > 1 && displayConnectedDevices.every(d => !!d.groupId);

  const prevIsConnectedRef = useRef(false);
  const telemetry = useTelemetryLedger();
  useEffect(() => {
    if (isActuallyConnected && !prevIsConnectedRef.current) {
      telemetry.incrementCounter('hardware_connections');
    }
    prevIsConnectedRef.current = isActuallyConnected;
  }, [isActuallyConnected, telemetry]);

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
  const dockedControllerRef = React.useRef<DockedControllerHandle>(null);

  const [crewInitialStep, setCrewInitialStep] = useState<any>('landing');
  const [isCrewHubCollapsed, setIsCrewHubCollapsed] = useState(false);

  // Load Crew Hub collapsed state on mount
  useEffect(() => {
    AsyncStorage.getItem('@Sk8lytz_crewHubCollapsed')
      .then(res => { if (res !== null) setIsCrewHubCollapsed(res === 'true'); })
      .catch((e) => AppLogger.warn('PERSISTENCE', { key: '@Sk8lytz_crewHubCollapsed', event: 'load_failed', error: String(e) }));
  }, []);

  const toggleCrewHubCollapse = useCallback(() => {
    setIsCrewHubCollapsed(prev => {
      const next = !prev;
      AsyncStorage.setItem('@Sk8lytz_crewHubCollapsed', String(next)).catch((e) => AppLogger.warn('PERSISTENCE', { key: '@Sk8lytz_crewHubCollapsed', event: 'save_failed', error: String(e) }));
      return next;
    });
  }, []);

  // Relay Soft Disconnect recoveries down to the DockedController for silent payload blasting
  useEffect(() => {
    setOnDeviceRecovered((deviceId: string) => {
      dockedControllerRef.current?.replayStateToDevice(deviceId);
    });
  }, [setOnDeviceRecovered]);




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

  // ── Overwatch: Silent Sweeper Lifecycle ──────────────────────────────────
  // Start on Dashboard mount once BT is confirmed ready.
  // Stop on background (battery safety) and resume on foreground.
  useEffect(() => {
    if (!isBluetoothEnabled || !isBluetoothSupported || Platform.OS === 'web') return;
    // Start sweeper on first render when BT is already ready,
    // or when BT transitions from disabled to enabled.
    startSweeper();
  }, [isBluetoothEnabled, isBluetoothSupported]);

  useEffect(() => {
    if (Platform.OS === 'web') return;
    const sub = AppState.addEventListener('change', (nextState: AppStateStatus) => {
      if (nextState === 'background' || nextState === 'inactive') {
        stopSweeper();
        // Option A: We no longer force disconnect on background.
        // This allows GATT sessions to stay alive for Music/Street mode
        // and enables AutoRecovery if they drop organically.
      } else if (nextState === 'active') {
        if (isBluetoothEnabled && isBluetoothSupported) startSweeper();
        // Re-arm auto-connect on foreground return. If GATT dropped while backgrounded,
        // the one-shot gate is closed and the MAC queue is drained — retrigger refills both.
        retriggerAutoConnectRef.current();
      }
    });
    return () => sub.remove();
  }, [isBluetoothEnabled, isBluetoothSupported, startSweeper, stopSweeper, forceDisconnect]);

  // Cleanup on Dashboard unmount
  useEffect(() => {
    return () => { stopSweeper(); };
  }, [stopSweeper]);




  const wizardCheckedRef = React.useRef(false);
  const [pendingNewDevice, setPendingNewDevice] = React.useState<any | null>(null);

  // NOTE: auto-scan on mount is handled by the hasAutoScanned effect below (requires viewState === 'DASHBOARD')

  // 1. Check FTUE state on mount
  useEffect(() => {
    if (isLoading) return;
    
    // Only route if we are still waiting in the initial loading state
    if (viewState === 'LOADING_REGS') {
      if (registeredDevices.length === 0) {
        setViewState('SETUP_WIZARD');
      } else {
        setViewState('DASHBOARD');
      }
    }
  }, [isLoading, registeredDevices.length, viewState]);

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
      // Allow registration for both unclaimed devices AND when we can't verify
      // claim status due to being offline (BUG: offline_unknown was blocking post-FTUE device adds)
      if (status === 'unclaimed' || status === 'offline_unknown') setPendingNewDevice(first);
    };
    
    checkNewDevice();
  }, [pendingRegistrations]);

  // handleRegistrationComplete is now provided by useDashboardGroups hook

  // The Drop-out UI Alert has been excised per user mandate.
  // We no longer spam the user when hardware connections drop out organically.

  // ── Cloud Sync & BLE Auto-Connect (extracted to useDashboardAutoConnect) ──
  const { clearAutoConnectQueue, retriggerAutoConnect } = useDashboardAutoConnect({
    isBluetoothSupported,
    isBluetoothEnabled,
    isActuallyConnected,
    allDevices,
    connectedDevices,
    connectToDevices,
    scanForPeripherals,
    requestPermissions,
    refreshProfile,
    registeredDevices,
    bleGateRef,
    isWizardActive: viewState === 'SETUP_WIZARD',
    burstScan,
  });
  // Wire the real function into the ref now that the hook has been called.
  retriggerAutoConnectRef.current = retriggerAutoConnect;

  const [isControllerOpen, setIsControllerOpen] = useState(false);
  // Logical session flag — NOT the raw BLE connection state.
  // Starts when the user taps a group/device to connect, ends ONLY on explicit disconnect.
  // This ensures a brief BLE hiccup never wipes the in-progress skate session.
  const [isSkateSessionActive, setIsSkateSessionActive] = useState(false);

  const { latestBpm, avgBpm, peakBpm, activeCalories } = useHealthTelemetry(isSkateSessionActive);

  // ── Global Telemetry ──
  // Pass `isSkateSessionActive` (logical) NOT `isActuallyConnected` (raw BLE).
  // Session persists through BLE drops — only ends on explicit user disconnect.
  const {
    gpsSpeed,
    peakGForce,
    sessionDistanceMiles,
    sessionDurationSec,
    sessionPeakSpeed,
    sessionAvgSpeed
  } = useGlobalTelemetry(isSkateSessionActive, { avgBpm, peakBpm, activeCalories });


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



  // displayConnectedDevices, isActuallyConnected, isGrouped hoisted to top of component (after useDashboardGroups).

  const sortedAllDevices = useMemo(() => {
    return [...allDevices]
      .sort((a, b) => (b.rssi ?? -999) - (a.rssi ?? -999))
      .map(d => {
        const cfg = (deviceConfigs[d.id] || {}) as Partial<DeviceSettings>;
        return { ...d, ...cfg } as DisplayDevice;
      });
  }, [allDevices, deviceConfigs]);

  const registeredDevicesData = useMemo(() => {
    const macs = new Set(registeredDevices.map((d: RegisteredDevice) => d.device_mac?.toUpperCase() ?? ''));
    return sortedAllDevices.filter((d: DisplayDevice) => macs.has(d.id?.toUpperCase() ?? ''));
  }, [sortedAllDevices, registeredDevices]);

  const availableDevicesData = useMemo(() => {
    const macs = new Set(registeredDevices.map((d: RegisteredDevice) => d.device_mac?.toUpperCase() ?? ''));
    return sortedAllDevices.filter((d: DisplayDevice) => !macs.has(d.id?.toUpperCase() ?? ''));
  }, [sortedAllDevices, registeredDevices]);

  const handleDisconnect = useCallback(async () => {
    setIsSkateSessionActive(false);  // End the logical session → commits GPS data
    setIsControllerOpen(false);      // Close UI instantly — feels snappy
    disconnectFromDevice();          // Fire-and-forget BLE teardown
  }, [disconnectFromDevice]);

  const handleCrewHubApplyCloudScene = useCallback((scene: Record<string, any>) => {
    dockedControllerRef.current?.applyCloudScene(scene);
  }, []);

  const handleGroupPress = useCallback((group: CustomGroup) => {
    const devicesToConnect = allDevices.filter(d => group.deviceIds.includes(d.id.toUpperCase()));
    if (devicesToConnect.length > 0) {
      // Optimistic UI: Defer heavy controller mount by one frame to allow tap animation.
      requestAnimationFrame(() => {
        setIsSkateSessionActive(true);
        startTransition(() => {
          setIsControllerOpen(true);
        });
        connectToDevices(devicesToConnect);
      });
    } else {
      // No BLE devices discovered yet — re-arm auto-connect + burst scan silently
      AppLogger.log('BLE_STATE_CHANGE', { event: 'group_tap_no_ble_devices', groupId: group.id, expectedMacs: group.deviceIds });
      retriggerAutoConnectRef.current();
    }
  }, [allDevices, connectToDevices]);

  const handleGroupLongPress = useCallback((id: string) => {
    openGroupRename(id);
  }, [openGroupRename]);

  const handleSetupWizard = useCallback(() => {
    setViewState('SETUP_WIZARD');
  }, []);



  const handleGroupMusicPress = useCallback((group: CustomGroup) => {
    const devicesToConnect = allDevices.filter(d => group.deviceIds.includes(d.id.toUpperCase()));
    if (devicesToConnect.length > 0) {
      connectToDevices(devicesToConnect);
      setIsSkateSessionActive(true);
      startTransition(() => {
        setIsControllerOpen(true);
      });
      // Small delay so the controller mounts before we switch mode
      setTimeout(() => dockedControllerRef.current?.setActiveMode('MUSIC'), 300);
    } else {
      retriggerAutoConnectRef.current();
    }
  }, [allDevices, connectToDevices]);

  const handleGroupCameraPress = useCallback((group: CustomGroup) => {
    const devicesToConnect = allDevices.filter(d => group.deviceIds.includes(d.id.toUpperCase()));
    if (devicesToConnect.length > 0) {
      connectToDevices(devicesToConnect);
      setIsSkateSessionActive(true);
      startTransition(() => {
        setIsControllerOpen(true);
      });
      setTimeout(() => dockedControllerRef.current?.setActiveMode('CAMERA'), 300);
    } else {
      retriggerAutoConnectRef.current();
    }
  }, [allDevices, connectToDevices]);

  const handleGroupFavoritePress = useCallback(async (group: CustomGroup, _snapshot: any) => {
    // Load the last-used IFavoriteState from AsyncStorage (same key as useFavorites)
    let lastFav: any = null;
    try {
      const raw = await AsyncStorage.getItem('@Sk8lytz_Favorites');
      if (raw) {
        const favs = JSON.parse(raw);
        if (Array.isArray(favs) && favs.length > 0) {
          // Use the most recently saved favorite (last in array)
          lastFav = favs[favs.length - 1];
        }
      }
    } catch (e) { /* ignore parse errors */ }

    if (!lastFav) {
      Alert.alert('No Favorites', 'You haven\'t saved any favorites yet. Open the controller and tap the ❤️ to save one.');
      return;
    }

    const devicesToConnect = allDevices.filter(d => group.deviceIds.includes(d.id.toUpperCase()));
    if (devicesToConnect.length > 0) {
      connectToDevices(devicesToConnect);
      setIsSkateSessionActive(true);
      startTransition(() => {
        setIsControllerOpen(true);
      });
      setTimeout(() => {
        dockedControllerRef.current?.loadFavorite(lastFav);
      }, 300);
    } else {
      retriggerAutoConnectRef.current();
    }
  }, [allDevices, connectToDevices]);

  const handleToggleRegisteredCollapse = useCallback(() => {
    setIsRegisteredCollapsed(!isRegisteredCollapsed);
  }, [isRegisteredCollapsed, setIsRegisteredCollapsed]);

  // Option A Fallback: Prevent getting stranded on a blue screen
  useEffect(() => {
    // If the controller is open, but all devices have completely disconnected
    // AND AutoRecovery has finally given up (no ghosts), kick the user to the Scanner UI.
    if (isControllerOpen && connectedDevices.length === 0 && ghostedDeviceIds.length === 0) {
      // Do not fallback while the BLE engine is actively negotiating — SCANNING is included
      // because a brief scan blip (e.g. sweeper probe) can momentarily zero connectedDevices
      // and would incorrectly trigger this guard while the user is actively using the controller.
      if (bleState === 'CONNECTING' || bleState === 'DISCONNECTING' || bleState === 'SCANNING') return;

      AppLogger.log('DASHBOARD_STATE', { event: 'auto_closed_no_devices' });
      setIsControllerOpen(false);
    }
  }, [isControllerOpen, connectedDevices.length, ghostedDeviceIds.length, bleState]);

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

  const handlePowerToggle = useCallback(async (deviceIds: string[], forceState?: boolean) => {
    // 1. Determine target state (if not forced, toggle based on first device)
    const targetState = forceState !== undefined ? forceState : !(powerStates[deviceIds[0]] ?? true);
    
    // 2. Set React State
    setPowerState(deviceIds, targetState);

    // 3. Dispatch BLE command to each device
    const payload = ZenggeProtocol.setPower(targetState);
    for (const mac of deviceIds) {
      if (writeToDevice) await writeToDevice(payload, mac);
    }
  }, [powerStates, setPowerState, writeToDevice]);

  const handleGroupPowerPress = useCallback((group: CustomGroup) => {
    handlePowerToggle(group.deviceIds);
  }, [handlePowerToggle]);

  const handleDeviceTap = (id: string) => {
    toggleDeviceSelection(id);
  };

  // toggleSelect replaced by toggleDeviceSelection from useDashboardGroups

  const openCreateGroup = () => {
    if (selectedIds.length === 0) return;
    const selectedDevices = allDevices.filter(d => selectedIds.includes(d.id));
    // Catalog-driven type resolution — replaces name.includes('soul') heuristic.
    const resolveType = (d: DisplayDevice) => getLocalProfileByPoints(d.points ?? 0).id;
    const firstType = resolveType(selectedDevices[0]);
    const allSame = selectedDevices.every(d => resolveType(d) === firstType);

    if (!allSame) {
      alert(`Please only group devices of the same type (e.g. two ${firstType}).`);
      return;
    }

    openGroupCreate();
  };

  // ── handleGroupDelete and saveGroup now live in useDashboardGroups ────────────




    const {
    MemoizedSk8lytzController,
    activeHwSettings,
    isSettingsVisible,
    setIsSettingsVisible,
    selectedDeviceForSettings,
    openSettings,
    saveSettings
  } = useDashboardController({
    isActuallyConnected,
    isTestModeActive,
    crewSession,
    crewRole,
    lastLeaderScene,
    setCrewSession,
    setCrewRole,
    setLastLeaderScene,
    setCrewModeSummary,
    dockedControllerRef,
    displayConnectedDevices,
    deviceConfigs,
    isGrouped,
    powerStates,
    handlePowerToggle,
    handleDisconnect,
    appSettings,
    bleState,
    gpsSpeed,
    peakGForce,
    sessionDistanceMiles,
    sessionDurationSec,
    customGroups,
    lastGroupPatterns,
    setLastGroupPattern,
    ledgerSave,
    writeToDevice,
    edgePanResponder,
    allDevices,
    setAllDevices,
    allDevicesRef,
    registeredDevices,
    saveRegisteredDevice,
    setUpdateTrigger,
  });

  /**
   * Renders a single device item card, merging registration data 
   * with live discovered BLE configs.
   */
  const renderItem = useCallback(({ item }: { item: RegisteredDevice | any }) => {
    // IDENTITY FIX: Always resolve to BLE MAC address for all lookups.
    // RegisteredDevice.id is a Supabase composite key (MAC+userId).
    const mac = (item.device_mac || item.id || '').toUpperCase();
      const cachedConfig = (deviceConfigs[item.id] || {}) as Partial<DeviceSettings>;
      const mergedItem = {
        ...item,
        ...cachedConfig,
        name: item.device_name || cachedConfig.name || item.name // Map DB field to component prop
    };
    // Read last known pattern state from ledger for preview swatch (synchronous, in-memory only).
    const ledgerState = ledgerLoadSync(normalizeMac(mac));

    return (
    <View style={{ paddingHorizontal: Layout.padding }}>
      <DeviceItem
        device={mergedItem}
        isConnected={displayConnectedDevices.some(d => d.id.toUpperCase() === mac)}
        isSelectionMode={isSelectionMode}
        isSelected={selectedIds.includes(mac)}
        ledgerState={ledgerState ?? undefined}
        onPress={() => {
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
          // Optimistic UI: Defer heavy controller mount by one frame to allow tap animation.
          // Fire-and-forget the BLE connection so JS thread is not blocked.
          requestAnimationFrame(() => {
            setIsSkateSessionActive(true);
            startTransition(() => {
              setIsControllerOpen(true);
            });
            connectToDevices([bleDevice]);
          });
          // NOTE: Hardware probe (0x63) intentionally NOT fired here.
          // hwSettings are loaded from DeviceRepository on mount — registered devices
          // already have their ledPoints/segments persisted from setup wizard.
          // Probing on every tap created an async race that corrupted deviceLedCount.
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
  }, [displayConnectedDevices, isSelectionMode, selectedIds, powerStates, deviceConfigs, allDevices, connectToDevices, scanForPeripherals, writeToDevice, ledgerLoadSync]);

  const mappedRegisteredDevicesForModal = useMemo(() => registeredDevices.map((d) => ({
    // IDENTITY KEY: always use device_mac (BLE MAC address), NOT d.id (Supabase UUID).
    // AccountModal receives this array directly via the registeredDevices prop —
    // there is no independent Supabase query. All views share one state authority.
    id: d.device_mac || '',
    mac: d.device_mac || '',
    name: d.device_name || '',
    customName: d.custom_name || '',
    groupName: d.group_name || '',
    type: d.product_type as StoredDevice['type'],
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
      return (
        <HardwareSetupWizardScreen
          onSetupComplete={async (devices) => { await handleRegistrationComplete(devices, allDevices); }}
          scanForPeripherals={scanForPeripherals}
          bleState={bleState}
          requestPermissions={requestPermissions}
          isBluetoothSupported={isBluetoothSupported}
          isBluetoothEnabled={isBluetoothEnabled}
          pendingRegistrations={pendingRegistrations}
          setPendingRegistrations={setPendingRegistrations}
          writeToDevice={writeToDevice}
          pingDevice={pingDevice}
        />
      );
    default:
      break;
  }

  return (
    <SafeAreaView style={styles.safeArea}>
      {BluetoothWarningBanner}
      <View style={styles.container}>

        {isControllerOpen && (
          <View style={{ flex: 1 }}>
            <View pointerEvents="box-none" style={{ paddingBottom: Spacing.lg, zIndex: 100, elevation: 100 }}>
              <DashboardHeader
                isActuallyConnected={true}
                isOfflineMode={isOfflineMode}
                isTestModeActive={isTestModeActive}
                isDark={isDark}
                displayConnectedDevices={displayConnectedDevices}
                customGroups={customGroups}
                powerStates={powerStates}
                handleDisconnect={handleDisconnect}
                handlePowerToggle={handlePowerToggle}
                isAdmin={userProfile?.role === 'admin'}
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
        {!isControllerOpen && (
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
                  isAdmin={userProfile?.role === 'admin'}
                  onPressAdminTools={() => setIsAdminToolsVisible(true)}
                  onPressSupport={() => setIsSupportModalVisible(true)}
                  onPressTheme={toggleTheme}
                  authUsername={authUsername}
                  onPressAccount={() => setIsAccountModalVisible(true)}
                  showBackButton={false}
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
                <DashboardCrewPanel
                  crewSession={crewSession}
                  setCrewSession={setCrewSession}
                  crewRole={crewRole}
                  setCrewRole={setCrewRole}
                  isCrewModalVisible={isCrewModalVisible}
                  setIsCrewModalVisible={setIsCrewModalVisible}
                  crewModeSummary={crewModeSummary}
                  setCrewModeSummary={setCrewModeSummary}
                  lastLeaderScene={lastLeaderScene}
                  setLastLeaderScene={setLastLeaderScene}
                  initialDeepLinkCode={initialDeepLinkCode}
                  isOfflineMode={isOfflineMode}
                  appSettings={appSettings}
                  windowHeight={windowHeight}
                  Colors={Colors}
                  styles={styles}
                  onApplyCloudScene={handleCrewHubApplyCloudScene}
                  crewInitialStep={crewInitialStep}
                  setCrewInitialStep={setCrewInitialStep}
                  isCrewHubCollapsed={isCrewHubCollapsed}
                  toggleCrewHubCollapse={toggleCrewHubCollapse}
                />


                {/* SLAB 2.5: LIVE TELEMETRY HUD */}
                <View>
                  <DashboardTelemetryHero 
                    gpsSpeed={gpsSpeed} 
                    peakGForce={peakGForce} 
                    sessionDistanceMiles={sessionDistanceMiles} 
                    sessionDurationSec={sessionDurationSec} 
                    sessionPeakSpeed={sessionPeakSpeed}
                    sessionAvgSpeed={sessionAvgSpeed}
                    healthBpm={latestBpm}
                    healthCalories={activeCalories}
                  />
                </View>

                {/* SLAB 3: MY SKATES */}
                <MySkatesSlab
                  customGroups={customGroups}
                  lastGroupPatterns={lastGroupPatterns}
                  allDevices={allDevices}
                  connectedDevices={connectedDevices}
                  registeredDevices={registeredDevices}
                  powerStates={powerStates}
                  userProfile={userProfile}
                  onGroupPress={handleGroupPress}
                  onGroupLongPress={handleGroupLongPress}
                  onSetupWizard={handleSetupWizard}
                  onGroupPowerPress={handleGroupPowerPress}
                  onGroupMusicPress={handleGroupMusicPress}
                  onGroupCameraPress={handleGroupCameraPress}
                  onGroupFavoritePress={handleGroupFavoritePress}
                  Colors={Colors}
                  styles={styles}
                />

                {/* Flexible spacer — only pushes content on large screens */}
                <View style={{ flex: 1, minHeight: windowHeight < 720 ? 0 : 20 }} />

                {/* SLAB 4: REGISTERED FLEET */}
                <RegisteredFleetSlab
                  registeredDevices={registeredDevices}
                  isRegisteredCollapsed={isRegisteredCollapsed}
                  onToggleCollapse={handleToggleRegisteredCollapse}
                  onSetupWizard={handleSetupWizard}
                  renderItem={renderItem}
                  Colors={Colors}
                  styles={styles}
                />
              </ScrollView>
          </View>
        )}
        

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
      


        {isSupportModalVisible && (
          <SupportModal
            visible={isSupportModalVisible}
            onClose={() => setIsSupportModalVisible(false)}
            Colors={Colors}
            styles={styles}
          />
        )}

      {/* HardwareSetupWizardScreen is conditionally returned at the top level instead of here */}



      

      {/* Account Management Modal */}
      {isAccountModalVisible && (
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
          writeToDevice={writeToDevice}
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
            // NOTE: Do NOT add AsyncStorage.setItem here — customGroups state is
            // owned by DeviceRepository/useDashboardGroups. setCustomGroups triggers
            // the hook's persistence logic. A second raw write here is a race condition.
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
      )}
      
      {/* Admin Tools Hub (Replaces LogViewerModal) */}
      {isAdminToolsVisible && userProfile?.role === 'admin' && (
        <AdminToolsModal
          visible={isAdminToolsVisible}
          onClose={() => setIsAdminToolsVisible(false)}
          allDevices={allDevices}
          connectedDevices={connectedDevices as DisplayDevice[]}
          bleState={bleState}
          handleScan={() => scanForPeripherals()}
          writeToDevice={writeToDevice}
          liveRxPayload={lastRawNotification}
          liveDeviceConfigs={deviceConfigs}
          onConnectToDevice={async (d: any) => { await connectToDevices([d]); }}
          onDisconnectFromDevice={async (_id: string) => { forceDisconnect(); }}
          isDiagnosticsMode={isDiagnosticsMode}
          onToggleDiagnostics={() => setIsDiagnosticsMode(!isDiagnosticsMode)}
          hwSettings={activeHwSettings}
        />
      )}


    </SafeAreaView>
  );
}


