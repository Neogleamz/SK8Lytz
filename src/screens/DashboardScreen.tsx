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
/* eslint-disable unused-imports/no-unused-vars, react-hooks/exhaustive-deps */
/* global global, requestAnimationFrame */
import { MaterialCommunityIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import React, { useState, useEffect, useRef, useCallback, useMemo, startTransition } from 'react';
import { ActivityIndicator, Alert, AppState, AppStateStatus, BackHandler, Linking, PanResponder, Platform, ScrollView, Text, TouchableOpacity, View, useWindowDimensions, RefreshControl, InteractionManager } from 'react-native';
import { SafeAreaView, useSafeAreaInsets } from 'react-native-safe-area-context';
import * as ExpoLinking from 'expo-linking';
import DeviceItem from '../components/DeviceItem';
import { useTheme } from '../context/ThemeContext';
import { BLEContext } from '../context/BLEContext';
import type { Device } from 'react-native-ble-plx';
import { Layout } from '../theme/theme';

import { DockedControllerHandle } from '../components/DockedController';
import GroupSettingsModal from '../components/GroupSettingsModal';
import DeviceSettingsModal from '../components/DeviceSettingsModal';

import AdminToolsModal from '../components/admin/AdminToolsModal';

import { AppLogger } from '../services/AppLogger';

import AccountModal, { StoredDevice } from '../components/AccountModal';
import { getDefaultGroupName } from '../utils/NamingUtils';
// Removed getLocalProfileByPoints as it is unused
import { RegisteredDevice, useRegistration } from '../hooks/useRegistration';
import HardwareSetupWizardScreen from './Onboarding/HardwareSetupWizardScreen';
import { useSession } from '../context/SessionContext';
import { DashboardTelemetryHero } from '../components/dashboard/DashboardTelemetryHero';

import { webStyle } from '../utils/webStyles';

// ─── Phase 1 Domain Hooks ──────────────────────────────────────────────────────
import { useDashboardAutoConnect } from '../hooks/useDashboardAutoConnect';
import { useDashboardGroups } from '../hooks/useDashboardGroups';
import { useDashboardProfile } from '../hooks/useDashboardProfile';
import { useDashboardCrew } from '../hooks/useDashboardCrew';

import { useHardwareNotifications, BLEDeviceMinimal, ProbedHardwareConfig } from '../hooks/useHardwareNotifications';
import { useDeviceStateLedger, normalizeMac } from '../hooks/useDeviceStateLedger';
import { useTelemetryLedger } from '../hooks/useTelemetryLedger';
import type { DashboardViewState, DeviceSettings, CustomGroup, DisplayDevice } from '../types/dashboard.types';

// DeviceSettings and CustomGroup are now imported from '../types/dashboard.types'
// — migrated as part of Phase 1 Domain-Driven Refactor

import { useProtocolDispatch } from '../hooks/useProtocolDispatch';
import DashboardCrewPanel from '../components/dashboard/DashboardCrewPanel';
import { useDashboardController } from '../hooks/useDashboardController';
import DashboardHeader from '../components/dashboard/DashboardHeader';
import { useAppConfig } from '../context/AppConfigContext';
import MySkatesSlab from '../components/dashboard/MySkatesSlab';
import RegisteredFleetSlab from '../components/dashboard/RegisteredFleetSlab';
import SupportModal from '../components/dashboard/SupportModal';
import { createDashboardStyles } from '../styles/DashboardStyles';
import { scrubPII } from '../utils/piiScrubber';



export default function DashboardScreen({ isOfflineMode = false }: { isOfflineMode?: boolean; } = {}) {
  const { isVisibilityAllowed } = useAppConfig();
  const { Colors, isDark, toggleTheme } = useTheme();
  const insets = useSafeAreaInsets();
  const { height: windowHeight, width: windowWidth } = useWindowDimensions();
  const styles = createDashboardStyles(Colors, windowHeight, windowWidth);
  // ── Device State Ledger — unified per-device pattern state ────────────────────
  // Destructure to STABLE function refs (each is a useCallback with [] deps inside the hook).
  // Do NOT pass `ledger` as an object — it's a new object reference on every render,
  // which would break FlatList performance by invalidating renderItem's useCallback.
  const { save: ledgerSave, loadSync: ledgerLoadSync } = useDeviceStateLedger();
  const ble = React.useContext(BLEContext);
  if (!ble) {
    throw new Error('DashboardScreen must be used within a BLEProvider');
  }
  const {
    scanForPeripherals,
    allDevices,
    setAllDevices,
    connectToDevices,
    connectedDevices,
    disconnectFromDevice,
    forceDisconnect,
    writeToDevice,
    isBluetoothSupported,
    isBluetoothEnabled,
    requestPermissions,
    setOnDataReceived,
    setOnHardwareProbed,
    setOnDeviceRecovered,
    pendingRegistrations,
    clearPendingRegistrations,
    setPendingRegistrations,
    bleState,
    getGate,
    pingDevice,
    startSweeper,
    stopSweeper,
    burstScan,
    ghostedDeviceIds,
    rssiMap,
  } = ble;

  // ── Registration system ────────────────────────────────────────────────────
  const {
    registeredDevices,
    saveRegisteredDevice,
    saveAllRegisteredDevices,
    deregisterDevice,
    checkDeviceClaimed,

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

  // Deep link effect moved below useDashboardCrew

  // ── Hardware BLE callbacks (extracted to useHardwareNotifications) ───────────

  const [updateTrigger, setUpdateTrigger] = useState(0);
  const [lastRawNotification, setLastRawNotification] = useState<{deviceId: string, payloadHex: string} | null>(null);
  const [isTestModeActive, setIsTestModeActive] = useState(false);
  // isDisconnecting removed — bleState === 'DISCONNECTING' is the canonical FSM gate
  const [isDiagnosticsMode, setIsDiagnosticsMode] = useState(false);

  // ── Phase 1: Fleet Groups, Device Configs, Power States → useDashboardGroups ───────
  // Declare refs before domain hooks that consume them
  const allDevicesRef = React.useRef(allDevices);

  const [viewState, setViewState] = useState<DashboardViewState>('LOADING_REGS');
  const [isRefreshing, setIsRefreshing] = useState(false);

  const onRefresh = useCallback(() => {
    setIsRefreshing(true);
    AppLogger.log('DASHBOARD_STATE', { event: 'dashboard_pull_to_refresh_triggered' });
    
    // Retrigger the auto connect sequence, which also calls burstScan 
    retriggerAutoConnectRef.current();
    
    // Stop the spinner after interactions complete
    InteractionManager.runAfterInteractions(() => {
      setIsRefreshing(false);
    });
  }, []);

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
    openGroupRename,
    closeGroupModal,
    selectedIds,
    isSelectionMode,
    toggleDeviceSelection,
    isRegisteredCollapsed,
    setIsRegisteredCollapsed,
    handleRegistrationComplete,
    // runAutoProvisioning deleted — the Setup Wizard now owns all device claiming.
    saveGroup,
    handleGroupDelete,
  } = useDashboardGroups({
    registeredDevices,
    saveAllRegisteredDevices,
    saveRegisteredDevice,

    clearPendingRegistrations,
    getAllScannedDevices: () => allDevicesRef.current as unknown as DisplayDevice[],
    setAllDevices: setAllDevices as unknown as React.Dispatch<React.SetStateAction<DisplayDevice[]>>,
    allDevicesRef: allDevicesRef as unknown as React.MutableRefObject<DisplayDevice[]>,
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
      const resolvedType = cfg.type || (d as typeof d & { type?: string }).type || rd?.product_type || undefined;
      const result: DisplayDevice = { ...d, ...cfg, ...(resolvedType ? { type: resolvedType } : {}) };
      return result;
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
    crewRole,
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

  const [crewInitialStep, setCrewInitialStep] = useState<'landing' | 'join' | 'create' | 'map'>('landing');
  const [isCrewHubCollapsed, setIsCrewHubCollapsed] = useState(false);

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
      } catch (err: unknown) {
        AppLogger.error('DEEP_LINK', { event: 'parse_failed', url, error: (err instanceof Error ? err.message : String(err)) , payload_size: 0, ssi: 0 });
      }
    };
    const linkSubscription = Linking.addEventListener('url', handleDeepLink);
    Linking.getInitialURL().then(url => { if (url) handleDeepLink({ url }); });
    return () => linkSubscription.remove();
  }, [setIsCrewModalVisible]);

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
    if (!isBluetoothEnabled || !isBluetoothSupported || Platform.select({ web: true, default: false })) return;
    // Start sweeper on first render when BT is already ready,
    // or when BT transitions from disabled to enabled.
    startSweeper();
  }, [isBluetoothEnabled, isBluetoothSupported, startSweeper]);

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
  const hasAutoStartedSessionRef = React.useRef(false);

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
      if (status === 'unclaimed' || status === 'offline_unknown') {
        // Wait, pendingNewDevice was used for the "New Device Found" modal. Let's just log it.
        AppLogger.log('BLE_STATE_CHANGE', { event: 'new_unclaimed_device_found', deviceId: scrubPII(first.device_mac) });
      }
    };
    
    checkNewDevice();
  }, [pendingRegistrations, checkDeviceClaimed, viewState]);

  // handleRegistrationComplete is now provided by useDashboardGroups hook

  // The Drop-out UI Alert has been excised per user mandate.
  // We no longer spam the user when hardware connections drop out organically.

  // ── Cloud Sync & BLE Auto-Connect (extracted to useDashboardAutoConnect) ──
  const { retriggerAutoConnect } = useDashboardAutoConnect({
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
    getGate,
    isWizardActive: viewState === 'SETUP_WIZARD',
    burstScan,
  });
  // Wire the real function into the ref now that the hook has been called.
  retriggerAutoConnectRef.current = retriggerAutoConnect;

  const [isControllerOpen, setIsControllerOpen] = useState(false);
  
  // ── Global Telemetry from SessionProvider ──
  const { isSkateSessionActive, startSession, endSession, telemetry: sessionTelemetry, health } = useSession();
  
  const {
    gpsSpeed,
    peakGForce,
    sessionDistanceMiles,
    sessionDurationSec,
    sessionPeakSpeed,
    sessionAvgSpeed
  } = sessionTelemetry;

  // 1.5. Auto-start logical tracking session upon landing on the Dashboard
  useEffect(() => {
    if (viewState === 'DASHBOARD' && !isSkateSessionActive && !hasAutoStartedSessionRef.current) {
      hasAutoStartedSessionRef.current = true;
      startSession();
    }
  }, [viewState, isSkateSessionActive, startSession]);

  // Voice command dispatch + notification init are now handled
  // by useDashboardVoice and useDashboardProfile hooks respectively.

  useHardwareNotifications({
    isDiagnosticsMode,
    setOnDataReceived,
    setOnHardwareProbed: (cb) => setOnHardwareProbed((deviceId, cfg) => { if (cfg) cb(deviceId, cfg); }),
    allDevices,
    setAllDevices: (updater) => setAllDevices((prev) => updater(prev as unknown as BLEDeviceMinimal[]) as unknown as Device[]),
    setDeviceConfigs: (updater) => setDeviceConfigs((prev) => updater(prev as Record<string, Record<string, unknown>>) as Record<string, DeviceSettings>),
    deviceConfigs: deviceConfigs as Record<string, Record<string, unknown>>,
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
  }, [customGroups, customGroupsRef]); // Keep ref in sync with hook-managed state



  // displayConnectedDevices, isActuallyConnected, isGrouped hoisted to top of component (after useDashboardGroups).

  // sortedAllDevices removed as it was unused

  // handleCloseController only closes the UI, it does NOT drop the BLE connection or end the session.
  const handleCloseController = useCallback(() => {
    setIsControllerOpen(false);
  }, []);

  // handleDisconnect actually drops BLE and ends the session
  const handleDisconnect = useCallback(async () => {
    endSession();
    setIsControllerOpen(false);
    disconnectFromDevice();
  }, [disconnectFromDevice, endSession]);

  const handleCrewHubApplyCloudScene = useCallback((scene: Record<string, any>) => {
    dockedControllerRef.current?.applyCloudScene(scene);
  }, []);

  const handleDeviceReconnect = useCallback((mac: string) => {
    const bleDevice = allDevices.find(d => d.id.toUpperCase() === mac.toUpperCase());
    if (bleDevice) {
      connectToDevices([bleDevice]);
    } else {
      AppLogger.log('BLE_STATE_CHANGE', { event: 'manual_reconnect_scan_triggered', deviceId: scrubPII(mac) });
      scanForPeripherals();
    }
  }, [allDevices, connectToDevices, scanForPeripherals]);

  const handleGroupPress = useCallback((group: CustomGroup) => {
    let devicesToConnect = allDevices.filter(d => group.deviceIds.includes(d.id.toUpperCase()));
    
    // Web Demo Mock Fallback
    // If the user's Supabase groups contain real MAC addresses, they won't match the
    // sim-DE: mock devices in allDevices. We inject mock Device objects here so the
    // web demo can still mount the controller for UI testing.
    if (Platform.OS === 'web' && devicesToConnect.length === 0) {
      devicesToConnect = group.deviceIds.map(id => {
        const d: Partial<Device> = {
          id,
          name: `Demo Skate`,
          rssi: -50,
        };
        return d as Device;
      });
    }

    if (devicesToConnect.length > 0) {
      // Optimistic UI: Defer heavy controller mount by one frame to allow tap animation.
      global.requestAnimationFrame(() => {
        if (!isSkateSessionActive) {
          startSession();
        }
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
  }, [allDevices, connectToDevices, isSkateSessionActive, startSession]);

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
      if (!isSkateSessionActive) startSession();
      startTransition(() => {
        setIsControllerOpen(true);
      });
      // Small delay so the controller mounts before we switch mode
      setTimeout(() => dockedControllerRef.current?.setActiveMode('MUSIC'), 300);
    } else {
      retriggerAutoConnectRef.current();
    }
  }, [allDevices, connectToDevices, isSkateSessionActive, startSession]);

  const handleGroupCameraPress = useCallback((group: CustomGroup) => {
    const devicesToConnect = allDevices.filter(d => group.deviceIds.includes(d.id.toUpperCase()));
    if (devicesToConnect.length > 0) {
      connectToDevices(devicesToConnect);
      if (!isSkateSessionActive) startSession();
      startTransition(() => {
        setIsControllerOpen(true);
      });
      setTimeout(() => dockedControllerRef.current?.setActiveMode('CAMERA'), 300);
    } else {
      retriggerAutoConnectRef.current();
    }
  }, [allDevices, connectToDevices, isSkateSessionActive, startSession]);

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
    } catch (_e: unknown) {
      /* ignore parse errors */ 
    }

    if (!lastFav) {
      Alert.alert('No Favorites', 'You haven\'t saved any favorites yet. Open the controller and tap the ❤️ to save one.');
      return;
    }

    const devicesToConnect = allDevices.filter(d => group.deviceIds.includes(d.id.toUpperCase()));
    if (devicesToConnect.length > 0) {
      connectToDevices(devicesToConnect);
      if (!isSkateSessionActive) startSession();
      startTransition(() => {
        setIsControllerOpen(true);
      });
      setTimeout(() => {
        dockedControllerRef.current?.loadFavorite(lastFav);
      }, 300);
    } else {
      retriggerAutoConnectRef.current();
    }
  }, [allDevices, connectToDevices, isSkateSessionActive, startSession]);

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
      if (isActuallyConnected || isSkateSessionActive) {
        handleCloseController();
        return true; // intercept and exit to scanner
      }
      return false; // allow native exit
    };

    const backHandler = BackHandler.addEventListener('hardwareBackPress', handleBackPress);
    return () => backHandler.remove();
  }, [isTestModeActive, isActuallyConnected, isSkateSessionActive, handleCloseController, bleState]);

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
          else if ((isActuallyConnected || isSkateSessionActive) && bleState !== 'DISCONNECTING') handleCloseController();
        }
      },
      onPanResponderTerminate: (evt, gestureState) => {
        if (gestureState.dx > 60) {
          if (isTestModeActive) setIsTestModeActive(false);
          else if ((isActuallyConnected || isSkateSessionActive) && bleState !== 'DISCONNECTING') handleCloseController();
        }
      }
    })
  ).current;

  const dispatch = useProtocolDispatch();

  const handlePowerToggle = useCallback(async (deviceIds: string[], forceState?: boolean) => {
    // 1. Determine target state (if not forced, toggle based on first device)
    const targetState = forceState !== undefined ? forceState : !(powerStates[deviceIds[0]] ?? true);
    
    // 2. Set React State
    setPowerState(deviceIds, targetState);

    // 3. Dispatch BLE command to each device via HAL
    for (const mac of deviceIds) {
      dispatch.setPower(targetState, mac);
    }
  }, [powerStates, setPowerState, dispatch]);

  const handleGroupPowerPress = useCallback((group: CustomGroup) => {
    handlePowerToggle(group.deviceIds);
  }, [handlePowerToggle]);

  // toggleSelect replaced by toggleDeviceSelection from useDashboardGroups
  // handleGroupDelete and saveGroup now live in useDashboardGroups
  
  const {
    MemoizedSk8lytzController,
    activeHwSettings,
    isSettingsVisible,
    setIsSettingsVisible,
    selectedDeviceForSettings,
    openSettings,
    saveSettings
  } = useDashboardController({
    isOfflineMode,
    isActuallyConnected,
    isTestModeActive,
    crewSession,
    crewRole,
    lastLeaderScene,
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
    sessionAvgSpeed,
    sessionActive: isSkateSessionActive,
    startSession,
    stopSessionRecording: endSession,
    customGroups,
    lastGroupPatterns,
    setLastGroupPattern,
    ledgerSave,
    writeToDevice,
    edgePanResponder,
    allDevices: allDevices as unknown as DisplayDevice[],
    setAllDevices: setAllDevices as unknown as React.Dispatch<React.SetStateAction<DisplayDevice[]>>,
    allDevicesRef: allDevicesRef as unknown as React.MutableRefObject<DisplayDevice[]>,
    registeredDevices,
    saveRegisteredDevice: (device) => saveRegisteredDevice(device as RegisteredDevice),
    setUpdateTrigger,
  });

  /**
   * Renders a single device item card, merging registration data 
   * with live discovered BLE configs.
   */
  const renderItem = useCallback(({ item }: { item: RegisteredDevice }) => {
    // S4 Acknowledgement: This file is close to or exceeds 30KB. Only specific plan line items are modified surgically.
    // IDENTITY FIX: Always resolve to BLE MAC address for all lookups.
    // RegisteredDevice.id is a Supabase composite key (MAC+userId).
    const mac = (item.device_mac || item.id || '').toUpperCase();
      const cachedConfig = (deviceConfigs[item.id as string] || {}) as Partial<DeviceSettings>;
      const mergedItem = {
        ...item,
        ...cachedConfig,
        id: mac,
        name: (item.device_name || cachedConfig.name) as string | null, // Map DB field to component prop
        // Inject live post-connect RSSI so the wifi icon reflects current signal quality.
        // Falls back to scan-time rssi on the raw item (stale after connect, but better than null).
        rssi: rssiMap[mac] ?? item.rssi_at_register ?? null,
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
            (d) => (d.id || '').toUpperCase() === mac
          );
          if (!bleDevice) {
            // Device not yet discovered — trigger a scan. useDashboardAutoConnect
            // observer will connect it automatically when it appears.
            AppLogger.log('BLE_STATE_CHANGE', { event: 'manual_connect_scan_triggered', deviceId: scrubPII(mac) });
            scanForPeripherals();
            return;
          }
          // Optimistic UI: Defer heavy controller mount by one frame to allow tap animation.
          // Fire-and-forget the BLE connection so JS thread is not blocked.
          requestAnimationFrame(() => {
            if (!isSkateSessionActive) startSession();
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
  }, [displayConnectedDevices, isSelectionMode, selectedIds, powerStates, deviceConfigs, allDevices, connectToDevices, scanForPeripherals, writeToDevice, ledgerLoadSync, rssiMap]);

  const mappedRegisteredDevicesForModal = useMemo(() => registeredDevices.map((d) => ({
    // IDENTITY KEY: always use device_mac (BLE MAC address), NOT d.id (Supabase UUID).
    // AccountModal receives this array directly via the registeredDevices prop —
    // there is no independent Supabase query. All views share one state authority.
    id: d.device_mac || '',
    mac: d.device_mac || '',
    name: d.device_name || '',
    customName: d.custom_name || '',
    groupName: d.group_names && d.group_names.length > 0 ? d.group_names[0] : '',
    type: d.product_type as StoredDevice['type'],
    registeredAt: d.registered_at,
    // Hardware fields — required for pill display in AccountModal Devices tab
    led_points: d.led_points,
    segments: d.segments,
    ic_type: d.ic_type,
    color_sorting: d.color_sorting,
  })), [registeredDevices]);

  const mappedDevicesForGroupModal = useMemo(() => registeredDevices.map(rd => ({
    id: rd.device_mac.toUpperCase(),
    name: rd.custom_name || rd.device_name || rd.device_mac,
    connected: allDevices.some(d => d.id.toUpperCase() === rd.device_mac.toUpperCase()),
  })), [registeredDevices, allDevices]);


  const BatteryWarningBanner = useMemo(() => {
    if (ble?.batteryTier !== 'PAUSED') return null;
    return (
      <View style={styles.btBanner}>
        <MaterialCommunityIcons name="battery-alert" size={24} color="#FFF" />
        <Text style={styles.btBannerText}>
          Scanning paused. Battery below 15%.
        </Text>
      </View>
    );
  }, [ble?.batteryTier]);

  const BluetoothWarningBanner = useMemo(() => {
    if (isBluetoothEnabled || Platform.OS === 'web') return null;
    return (
      <TouchableOpacity 
        onPress={() => Linking.openSettings()}
        style={styles.btBanner}
        activeOpacity={0.9}
      >
        <MaterialCommunityIcons name="alert-circle" size={24} color="#FFF" />
        <Text style={styles.btBannerText}>
          Bluetooth Disabled or Permissions Denied!
        </Text>
        <MaterialCommunityIcons name="chevron-right" size={20} color="#FFF" />
      </TouchableOpacity>
    );
  }, [isBluetoothEnabled]);

  switch (viewState) {
    case 'LOADING_REGS':
      return (
        <View style={[styles.container, styles.loadingContainer]}>
          <ActivityIndicator color={Colors.primary} size="large" />
        </View>
      );
    case 'SETUP_WIZARD':
      return (
        <HardwareSetupWizardScreen
          onSetupComplete={async (devices) => { await handleRegistrationComplete(devices, allDevices as unknown as DisplayDevice[]); }}
          scanForPeripherals={scanForPeripherals}
          bleState={bleState}
          requestPermissions={requestPermissions}
          isBluetoothSupported={isBluetoothSupported}
          isBluetoothEnabled={isBluetoothEnabled}
          pendingRegistrations={pendingRegistrations}
          setPendingRegistrations={setPendingRegistrations}
          pingDevice={pingDevice}
        />
      );
    default:
      break;
  }

  return (
    <SafeAreaView testID="dashboard-screen" style={styles.safeArea}>
      {BluetoothWarningBanner}
      {BatteryWarningBanner}
      <View style={styles.container}>
        {isControllerOpen ? (
          <View style={styles.controllerWrap}>
            <View pointerEvents={Platform.OS !== 'web' ? 'box-none' : undefined} style={[styles.controllerHeaderWrap, Platform.OS === 'web' ? webStyle({ pointerEvents: 'none' }) : undefined]}>
              <DashboardHeader
                isActuallyConnected={true}
                isOfflineMode={isOfflineMode}
                isDark={isDark}
                displayConnectedDevices={displayConnectedDevices}
                customGroups={customGroups}
                powerStates={powerStates}
                handleDisconnect={handleDisconnect}
                onReconnectDevice={handleDeviceReconnect}
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
            <View style={styles.controllerBodyWrap}>
              {MemoizedSk8lytzController}
            </View>
          </View>
        ) : (
          <View style={styles.dashboardWrap}>
             <View style={styles.headerSlab}>
                <DashboardHeader
                  isActuallyConnected={false}
                  isOfflineMode={isOfflineMode}
                  isDark={isDark}
                  displayConnectedDevices={displayConnectedDevices}
                  customGroups={customGroups}
                  powerStates={powerStates}
                  handleDisconnect={handleDisconnect}
                  onReconnectDevice={handleDeviceReconnect}
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
               style={styles.scrollView} 
               contentContainerStyle={{ paddingBottom: insets.bottom + 60, flexGrow: 1 }}
               showsVerticalScrollIndicator={false}
               refreshControl={
                 <RefreshControl 
                   refreshing={isRefreshing} 
                   onRefresh={onRefresh} 
                   tintColor={Colors.primary}
                   colors={[Colors.primary]}
                 />
               }
             >
                {/* SLAB 2: CREW HUB */}
                {isVisibilityAllowed('visibility_crew_hub') && (
                  <DashboardCrewPanel
                  crewSession={crewSession}
                  crewRole={crewRole}
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
                )}


                {/* SLAB 2.5: LIVE TELEMETRY HUD */}
                <View>
                  <DashboardTelemetryHero 
                    gpsSpeed={gpsSpeed} 
                    peakGForce={peakGForce} 
                    sessionDistanceMiles={sessionDistanceMiles} 
                    sessionDurationSec={sessionDurationSec} 
                    sessionPeakSpeed={sessionPeakSpeed}
                    sessionAvgSpeed={sessionAvgSpeed}
                    healthBpm={health.latestBpm}
                    healthCalories={health.activeCalories}
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
                <View style={styles.flexibleSpacer} />

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
          allDevices={mappedDevicesForGroupModal}
        />

        <DeviceSettingsModal
          isVisible={isSettingsVisible}
          onClose={() => setIsSettingsVisible(false)}
          onSave={saveSettings}
          initialSettings={selectedDeviceForSettings ? (selectedDeviceForSettings as unknown as DeviceSettings) : { name: '', type: 'HALOZ', grouped: false }}
          groups={customGroups}
          writeToDevice={writeToDevice}
          deviceId={selectedDeviceForSettings?.id}
          deviceName={selectedDeviceForSettings?.name || selectedDeviceForSettings?.id}
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
          onProfileUpdated={refreshProfile}
          onJoinCrewSession={(crewId) => {
            setPendingJoinCrewId(crewId);
            setIsCrewModalVisible(true);
            setIsAccountModalVisible(false);
          }}
          registeredDevices={mappedRegisteredDevicesForModal}
          onDeviceRenamed={async (deviceId, newName) => {
            setAllDevices((prev) => prev.map(d =>
              d.id === deviceId ? ({ ...d, customName: newName } as Device & { customName?: string }) : d
            ));
            const rd = registeredDevices.find(r => r.device_mac === deviceId);
            if (rd) {
              await saveRegisteredDevice({ ...rd, custom_name: newName, is_pending_sync: true });
            }
          }}
          onDeviceForgotten={async (deviceId) => {
            setAllDevices((prev) => prev.filter(d => d.id !== deviceId));
            await deregisterDevice(deviceId);
          }}
          onGroupRenamed={async (oldGroupName, newGroupName) => {
            const devs = registeredDevices.filter(d => d.group_names?.includes(oldGroupName));
            for (const d of devs) {
              const updatedNames = (d.group_names || []).map(n => n === oldGroupName ? newGroupName : n);
              await saveRegisteredDevice({ ...d, group_names: updatedNames, is_pending_sync: true });
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
            const devs = registeredDevices.filter(d => d.group_names?.includes(groupName));
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
          connectedDevices={displayConnectedDevices}
          bleState={bleState}
          handleScan={() => scanForPeripherals()}
          liveRxPayload={lastRawNotification}
          liveDeviceConfigs={deviceConfigs}
          onConnectToDevice={async (d: any) => { await connectToDevices([d]); }}
          onDisconnectFromDevice={async (_id: string) => { handleDisconnect(); }}
          isDiagnosticsMode={isDiagnosticsMode}
          onToggleDiagnostics={() => setIsDiagnosticsMode(!isDiagnosticsMode)}
          hwSettings={activeHwSettings}
        />
      )}


    </SafeAreaView>
  );
}


