/**
 * DashboardScreen.tsx — SK8Lytz Root Application Screen
 *
 * S4 Acknowledgement: This file is a monolith of 51KB, exceeding the 30KB limit.
 * Per the task guidelines, we acknowledge this and are only making surgical edits
 * to specific line items outlined in the plan rather than extracting the component.
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
 *  - Crew Hub collapsed state                 → remains here
 *
 * TODO (sweep-src-screens): Extract smaller stateless sub-components or slabs (like MySkatesSlab, CrewHubSlab) and helper hooks to separate files outside the screen monolith. (Blocked by S4 plan scope limits)
 * TODO (sweep-src-screens): Fix R-08 usage of 'Record<string, any>' as the type for 'lastGroupPatterns' state hook inside useDashboardGroups. (Blocked by S4)
 * TODO (sweep-src-screens): Align DisplayDevice signature in hooks to fix type bypasses. (Blocked by S4)
 * TODO (sweep-src-screens): Define STORAGE_CREWHUB_COLLAPSED in storageKeys.ts and use it. (Blocked by S4)
 * TODO (sweep-src-screens): Add an 'ERROR' case to the DashboardViewState switch statement. (Blocked by S4)
 * TODO (sweep-src-screens): Fix R-27 context consumer depth. (Blocked by S4)
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

import { AppLogger } from '../services/appLogger';

import AccountModal from '../components/AccountModal';
import { StoredDevice } from '../components/account/account.types';
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
import { STORAGE_FAVORITES, STORAGE_CREW_HUB_COLLAPSED } from '../constants/storageKeys';

import { useHardwareNotifications, BLEDeviceMinimal, ProbedHardwareConfig } from '../hooks/useHardwareNotifications';
import { useDeviceStateLedger, normalizeMac } from '../hooks/useDeviceStateLedger';
import { useTelemetryLedger } from '../hooks/useTelemetryLedger';
import type { DashboardViewState, DeviceSettings, CustomGroup, DisplayDevice, IFavoriteState, DevicePatternState, DeviceConnectionState } from '../types/dashboard.types';

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

import { useDashboardState } from './Dashboard/useDashboardState';
import { DashboardHeaderBanners } from './Dashboard/DashboardHeader';
import { useDashboardDeviceList } from './Dashboard/DashboardDeviceList';
import { useDashboardCrewHub, useCrewDeepLink } from './Dashboard/DashboardCrewHub';
import { useDashboardPowerControls } from './Dashboard/DashboardPowerControls';
import { createDashboardStyles } from '../styles/DashboardStyles';
import { scrubPII } from '../utils/piiScrubber';
import { useScreenPerformance } from '../hooks/useScreenPerformance';

export default function DashboardScreen({ isOfflineMode = false }: { isOfflineMode?: boolean; } = {}) {
  const { markFullyDrawn } = useScreenPerformance('DashboardScreen');
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
  const [lastSeen, setLastSeen] = useState<Record<string, number>>({});

  useEffect(() => {
    if (lastRawNotification) {
      setLastSeen(prev => ({
        ...prev,
        [lastRawNotification.deviceId.toUpperCase()]: Date.now()
      }));
    }
  }, [lastRawNotification]);
  const {
    viewState, setViewState,
    isRefreshing, setIsRefreshing,
    diagnosticState, setDiagnosticState,
    isTestModeActive, isDiagnosticsMode,
    isControllerOpen, setIsControllerOpen,
  } = useDashboardState();
  // isDisconnecting removed - bleState === 'DISCONNECTING' is the canonical FSM gate

  // ── Phase 1: Fleet Groups, Device Configs, Power States → useDashboardGroups ───────
  // Declare refs before domain hooks that consume them
  const allDevicesRef = React.useRef(allDevices);
  useEffect(() => {
    allDevicesRef.current = allDevices;
  }, [allDevices]);

  

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

  const connectionStates = React.useMemo(() => {
    const states: Record<string, DeviceConnectionState> = {};
    allDevices.forEach((d) => {
      const id = d.id.toUpperCase();
      const isConn = connectedDevices.some((c) => c.id.toUpperCase() === id);
      if (isConn) {
        states[id] = 'connected';
      } else if (bleState === 'CONNECTING') {
        states[id] = 'connecting';
      } else {
        states[id] = 'disconnected';
      }
    });
    return states;
  }, [allDevices, connectedDevices, bleState]);

  // BUG FIX: `d.groupId` is NEVER set on DisplayDevice — deviceConfigs stores `groupIds` (plural array),
  // not `groupId` (singular string). The prior check always returned false, making isPaired=false
  // for ALL connections including groups, causing dispatch to only write to the first device.
  // Ground truth: if 2+ devices are in connectedDevices, we are in a multi-device session.
  const isGrouped = connectedDevices.length > 1;

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
  useCrewDeepLink(setInitialDeepLinkCode, setCrewInitialStep, setIsCrewModalVisible);

  // Load Crew Hub collapsed state on mount
  useEffect(() => {
    AsyncStorage.getItem(STORAGE_CREW_HUB_COLLAPSED)
      .then(res => { if (res !== null) setIsCrewHubCollapsed(res === 'true'); })
      .catch((e) => AppLogger.warn('PERSISTENCE', { key: STORAGE_CREW_HUB_COLLAPSED, event: 'load_failed', error: String(e), payload_size: 0, ssi: 0 }));
  }, []);

  const toggleCrewHubCollapse = useCallback(() => {
    setIsCrewHubCollapsed(prev => {
      const next = !prev;
      AsyncStorage.setItem(STORAGE_CREW_HUB_COLLAPSED, String(next)).catch((e) => AppLogger.warn('PERSISTENCE', { key: STORAGE_CREW_HUB_COLLAPSED, event: 'save_failed', error: String(e), payload_size: 0, ssi: 0 }));
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
    markFullyDrawn();
  }, [isLoading, registeredDevices.length, viewState, markFullyDrawn]);



  // 2. Continuous listener for new devices beyond FTUE
  useEffect(() => {
    if (pendingRegistrations.length === 0) return;
    if (viewState === 'SETUP_WIZARD') return; // Ignore if wizard is already active
    
    let isMounted = true;
    // Only check if we are in dashboard mode and a new untracked device appears
    const checkNewDevice = async () => {
      const first = pendingRegistrations[0];
      if (!first) return;
      const status = await checkDeviceClaimed(first.device_mac, {
        firmwareVer: first.firmware_ver,
        productId:   first.product_id,
      });
      if (!isMounted) return;
      // Allow registration for both unclaimed devices AND when we can't verify
      // claim status due to being offline (BUG: offline_unknown was blocking post-FTUE device adds)
      if (status === 'unclaimed' || status === 'offline_unknown') {
        // Wait, pendingNewDevice was used for the "New Device Found" modal. Let's just log it.
        AppLogger.log('BLE_STATE_CHANGE', { event: 'new_unclaimed_device_found', deviceId: scrubPII(first.device_mac) });
      }
    };
    
    checkNewDevice();
    return () => {
      isMounted = false;
    };
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


  
  // ── Global Telemetry from SessionProvider ──
  const { isSkateSessionActive, sessionPhase, startSession, endSession, telemetry: sessionTelemetry, health } = useSession();
  
  const {
    handlePowerToggle,
    handleGroupPowerPress,
    handleGroupMusicPress,
    handleGroupCameraPress,
    handleGroupFavoritePress,
  } = useDashboardPowerControls({
    powerStates,
    setPowerState,
    connectedDevices: displayConnectedDevices,
    allDevices: allDevices as unknown as DisplayDevice[],
    connectToDevices: connectToDevices as unknown as (devices: DisplayDevice[]) => void,
    isSkateSessionActive,
    startSession,
    setIsControllerOpen,
    dockedControllerRef,
    retriggerAutoConnect: () => retriggerAutoConnectRef.current(),
  });
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

  const handleCrewHubApplyCloudScene = useCallback((scene: Record<string, unknown>) => {
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
      AppLogger.log('BLE_STATE_CHANGE', { event: 'group_tap_no_ble_devices', groupId: group.id, expectedMacs: group.deviceIds.map(scrubPII) });
      retriggerAutoConnectRef.current();
    }
  }, [allDevices, connectToDevices, isSkateSessionActive, startSession]);

  const handleGroupLongPress = useCallback((id: string) => {
    openGroupRename(id);
  }, [openGroupRename]);

  const handleSetupWizard = useCallback(() => {
    setViewState('SETUP_WIZARD');
  }, []);






  useEffect(() => {
    // R-17 FIX: react-native is a synchronous CommonJS module in the Metro bundler —
    // dynamic import() resolves asynchronously and swallows the cleanup function returned
    // inside .then(), causing a listener leak. Use require() so cleanup is synchronous.
    // eslint-disable-next-line @typescript-eslint/no-var-requires
    const { DeviceEventEmitter } = require('react-native') as typeof import('react-native');
    const unsubMusic = DeviceEventEmitter.addListener('BACKGROUND_ACTION_TOGGLE_MUSIC', () => {
      AppLogger.log('APP_LOG', { event: 'dashboard_received_bg_music' });
      // For notifications, trigger on the first/default group (or whatever is connected).
      const defaultGroup = customGroups[0] || { id: 'default', name: 'My Skates', deviceIds: allDevices.map(d => d.id.toUpperCase()) };
      handleGroupMusicPress(defaultGroup);
    });
    const unsubFav = DeviceEventEmitter.addListener('BACKGROUND_ACTION_FIRE_FAVORITE', () => {
      AppLogger.log('APP_LOG', { event: 'dashboard_received_bg_fav' });
      const defaultGroup = customGroups[0] || { id: 'default', name: 'My Skates', deviceIds: allDevices.map(d => d.id.toUpperCase()) };
      handleGroupFavoritePress(defaultGroup, null);
    });
    return () => {
      unsubMusic.remove();
      unsubFav.remove();
    };
  }, [allDevices, customGroups, handleGroupMusicPress, handleGroupFavoritePress]);

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
    // R-25 FIX: BackHandler is Android-only. On web it throws; guard with Platform.select.
    if (Platform.OS !== 'android') return;
    const handleBackPress = () => {
      if (isTestModeActive) {
        setDiagnosticState('IDLE');
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
          if (isTestModeActive) setDiagnosticState('IDLE');
          else if ((isActuallyConnected || isSkateSessionActive) && bleState !== 'DISCONNECTING') handleCloseController();
        }
      },
      onPanResponderTerminate: (evt, gestureState) => {
        if (gestureState.dx > 60) {
          if (isTestModeActive) setDiagnosticState('IDLE');
          else if ((isActuallyConnected || isSkateSessionActive) && bleState !== 'DISCONNECTING') handleCloseController();
        }
      }
    })
  ).current;

  const dispatch = useProtocolDispatch();



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
    sessionPeakSpeed,
    sessionPhase,
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
    setAllDevices: setAllDevices,
    allDevicesRef: allDevicesRef,
    registeredDevices,
    saveRegisteredDevice: (device) => saveRegisteredDevice(device as RegisteredDevice),
    setUpdateTrigger,
  });

  /**
   * Renders a single device item card, merging registration data 
   * with live discovered BLE configs.
   */
  const handleDeviceItemPress = useCallback((mac: string) => {
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
  }, [isSelectionMode, toggleDeviceSelection, allDevices, isSkateSessionActive, startSession, connectToDevices, scanForPeripherals]);

  const handleDeviceItemPowerToggle = useCallback((mac: string) => {
    handlePowerToggle([mac]);
  }, [handlePowerToggle]);
  const { renderItem } = useDashboardDeviceList({
    displayConnectedDevices,
    isSelectionMode,
    selectedIds,
    powerStates,
    deviceConfigs: deviceConfigs as Record<string, DeviceSettings>,
    ledgerLoadSync,
    rssiMap,
    connectionStates,
    handleDeviceItemPress,
    openSettings,
    handleDeviceItemPowerToggle,
  });


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
    <View testID="dashboard-screen" style={styles.safeArea}>
      <DashboardHeaderBanners batteryTier={ble?.batteryTier} isBluetoothEnabled={isBluetoothEnabled} Colors={Colors} styles={styles} />

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
                    sessionPhase={sessionPhase}
                  />
                </View>

                {/* SLAB 3: MY SKATES */}
                <MySkatesSlab
                  customGroups={customGroups}
                  lastGroupPatterns={lastGroupPatterns}
                  allDevices={allDevices as unknown as DisplayDevice[]}
                  connectedDevices={connectedDevices as unknown as DisplayDevice[]}
                  registeredDevices={registeredDevices as unknown as DisplayDevice[]}
                  connectionStates={connectionStates}
                  lastSeen={lastSeen}
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
          onConnectToDevice={async (d) => {
            const fullDevice = allDevices.find(dev => dev.id === d.id);
            if (fullDevice) {
              await connectToDevices([fullDevice]);
            }
          }}
          onDisconnectFromDevice={async (_id: string) => { handleDisconnect(); }}
          isDiagnosticsMode={isDiagnosticsMode}
          onToggleDiagnostics={() => setDiagnosticState(isDiagnosticsMode ? 'IDLE' : 'DIAGNOSTICS')}
          hwSettings={activeHwSettings}
        />
      )}


    </View>
  );
}




