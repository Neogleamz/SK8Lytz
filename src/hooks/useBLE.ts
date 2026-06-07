/* global __DEV__ */
/**
 * useBLE.ts — SK8Lytz Bluetooth Low Energy Engine
 *
 * Custom React hook that wraps react-native-ble-plx to provide all
 * BLE hardware interactions for the SK8Lytz LED controller ecosystem.
 *
 * Refactored using Domain-Driven Architecture (DDA):
 * This hook is now a Thin Orchestrator, dynamically routing scanning to useBLEScanner
 * and auto-recovery to useBLEAutoRecovery.
 */
import AsyncStorage from '@react-native-async-storage/async-storage';
import React, { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { Platform, AppState } from 'react-native';
import { Buffer } from 'buffer';
import type { Device } from 'react-native-ble-plx';
import { resolveProtocolForDevice } from '../protocols/ControllerRegistry';
import type { IControllerProtocol, ProtocolResult } from '../protocols/IControllerProtocol';
import { AppLogger } from '../services/AppLogger';
import type { BleConnectionState, PendingRegistration, PingResult } from '../types/dashboard.types';
import type { BLEPhaseTag } from '../services/ble/BleMachine.types';
import { useMachine } from '@xstate/react';
import { bleMachine } from '../services/ble/BleMachine';
import { ActorRefFrom } from 'xstate';

import { requestPermission } from '../services/PermissionService';
import { supabase } from '../services/supabaseClient';
import { useBLEScanner } from './ble/useBLEScanner';
import { useBLEAutoRecovery } from './ble/useBLEAutoRecovery';
import { useBLESweeper } from './ble/useBLESweeper';
import { useBLEHeartbeat } from './ble/useBLEHeartbeat';
import { useBLERSSIMonitor } from './ble/useBLERSSIMonitor';

import { executePingDevice } from '../services/BlePingService';
import { executeWriteToDevice, executeWriteChunked, executeProtocolResults as executeProtocolResultsService, BleWriteStateRefs } from '../services/BleWriteDispatcher';
import { clearWriteQueue, enqueueWrite, resolveWritePriority } from '../services/BleWriteQueue';
import { executeRealDisconnect, disconnectFromDevice as keepaliveDisconnect, forceDisconnect as keepaliveForceDisconnect } from '../services/BleLifecycleManager';
import { executeConnectToDevices } from '../services/BleConnectionManager';

let BleManager: any;
let State: any;

if (Platform.OS !== 'web') {
  const blePlx = require('react-native-ble-plx');
  BleManager = blePlx.BleManager;
  State = blePlx.State;
}

let writeGeneration = 0; // Increments on every new write; stale debounce checks compare against this


export interface BluetoothLowEnergyApi {
  requestPermissions(): Promise<boolean>;
  scanForPeripherals(options?: { keepAlive?: boolean, disableProbing?: boolean }): void;
  connectToDevices: (devices: Device[]) => Promise<void>;
  disconnectFromDevice: () => void;
  /** Bypasses keepalive timer and immediately tears down all GATT connections. Use in AppState background handlers. */
  forceDisconnect: () => void;
  writeToDevice: (payload: number[], targetDeviceId?: string) => Promise<boolean | 'partial'>;
  /** Send large payloads (>MTU) via sequential ZENGGE-framed BLE chunks. Required for 0x51 extended Scene Builder payloads. */
  writeChunked: (payload: number[], targetDeviceId?: string) => Promise<void>;
  getAdapterForDevice: (deviceId: string) => IControllerProtocol;
  executeProtocolResults: (payloads: { targetDeviceId: string, result: ProtocolResult }[], opts?: { lowPriority?: boolean }) => Promise<boolean>;
  /**
   * Wizard-specific atomic ping: Connect → Blink → Probe EEPROM → Turn Off → Disconnect.
   * Designed for use in HardwareSetupWizardScreen only. Bypasses connectedDevices requirement.
   * Returns hwConfig (ledPoints, icName, etc.) or null if probe timed out.
   */
  pingDevice: (
    mac: string,
    blinkPayload: number[],
    options?: { probe?: boolean; duration?: number; turnOffAtEnd?: boolean }
  ) => Promise<PingResult | null>;
  connectedDevices: Device[];
  allDevices: Device[];
  setAllDevices: React.Dispatch<React.SetStateAction<Device[]>>;
  isBluetoothSupported: boolean;
  isBluetoothEnabled: boolean;
  setOnDataReceived: (callback: (deviceId: string, data: number[]) => void) => void;
  setOnHardwareProbed: (callback: (deviceId: string, config: PingResult | null) => void) => void;
  setOnDeviceRecovered: (callback: (deviceId: string) => void) => void;
  droppedOutDeviceIds: string[];
  setDroppedOutDeviceIds: React.Dispatch<React.SetStateAction<string[]>>;
  pendingRegistrations: PendingRegistration[];
  clearPendingRegistrations: () => void;
  /** Setter for pendingRegistrations — passed to HardwareSetupWizardScreen for lazy BLINK probe enrichment */
  setPendingRegistrations: React.Dispatch<React.SetStateAction<PendingRegistration[]>>;
  ghostedDeviceIds: string[];
  bleState: BleConnectionState;
  getGate: () => BLEPhaseTag;

  bleActorRef: ActorRefFrom<typeof bleMachine>;
  // ── Overwatch BLE Engine API ───────────────────────────────────────────────
  /** Start the Silent Sweeper. Call once on Dashboard mount after BT permissions confirmed. */
  startSweeper(): void;
  /** Stop the Silent Sweeper. Called by AppState listener on app background. */
  stopSweeper(): void;
  /** Elevate to LowLatency burst scan for durationMs, then revert to LowPower. Use when Sweeper is active instead of startDeviceScan(). */
  burstScan(durationMs?: number): void;
  /** True while the Silent Sweeper is actively scanning */
  isSweeperActive: boolean;
  /** In-memory EEPROM cache keyed by uppercase MAC — populated by the Interrogator Queue */
  hwCache: Record<string, any>;
  /** Live post-connect RSSI map keyed by device MAC — updated every 30s by useBLERSSIMonitor. */
  rssiMap: Record<string, number>;
}

export default function useBLE(registeredMacs: string[] = []): BluetoothLowEnergyApi {
  const bleManager = useMemo(() => {
    if (Platform.OS === 'web') return null;
    return new BleManager();
  }, []);

  const [allDevices, setAllDevices] = useState<Device[]>([]);
  const [isBluetoothSupported, setIsBluetoothSupported] = useState(Platform.OS !== 'web');
  const [isBluetoothEnabled, setIsBluetoothEnabled] = useState(Platform.OS === 'web');
  const [droppedOutDeviceIds, setDroppedOutDeviceIds] = useState<string[]>([]);
  // ── Connection Gate Semaphore (XState V5) ─────────────────────────────────
  // ALL BLE operations (scan, connect, disconnect, recovery) must acquire this
  // gate before touching the radio. Prevents the "stampeding herd" of competing
  // GATT operations that cause Android GATT 133 errors.
  const [bleSnapshot, bleSend, bleActorRef] = useMachine(bleMachine);
  const bleGateState = typeof bleSnapshot.value === 'string' ? bleSnapshot.value : 'IDLE';
  const connectedDevices = bleSnapshot.context.connectedDevices;

  const getGate = useCallback((): BLEPhaseTag => {
    return bleActorRef.getSnapshot().value as BLEPhaseTag;
  }, [bleActorRef]);
  // ── Pattern write debounce ─────────────────────────────────────────────────
  // Prevents BLE queue pile-up when user swipes rapidly through the pattern picker.
  // Critical writes (power, time sync) bypass this and go direct.
  const writeDebounceTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  // ── GATT Keepalive Timer ───────────────────────────────────────────────────
  // When the controller closes, we defer the real GATT teardown by 60s.
  // If the user re-opens within that window, we cancel the timer and reuse the
  // existing connection (zero GATT re-negotiation, zero MTU exchange, zero time-sync).
  // forceDisconnect() bypasses this for AppState background events.
  const KEEPALIVE_DURATION_MS = 60_000;
  const keepaliveTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  // Helper: transition the gate which automatically syncs to state via the listener
  const setGate = useCallback((phase: BLEPhaseTag) => {
    // Map legacy phase tags to XState events if needed by legacy callers
    if (phase === 'CONNECTING') bleSend({ type: 'CONNECT_REQUEST' });
    else if (phase === 'DISCONNECTING') bleSend({ type: 'DISCONNECT_REQUEST' });
    else if (phase === 'SCANNING') bleSend({ type: 'SCAN_START' });
    else if (phase === 'IDLE') bleSend({ type: 'FORCE_IDLE' });
  }, [bleSend]);

  useEffect(() => {
    if (__DEV__) {
      AsyncStorage.getItem('@Sk8lytz_demo_mode').then((isMock) => {
        if (Platform.OS === 'web' || isMock === 'true') {
          setIsBluetoothSupported(true);
          setIsBluetoothEnabled(true);
        }
      });
    }
  }, []);

  const disconnectListeners = useRef<Record<string, import('react-native-ble-plx').Subscription>>({});
  const dataReceivedCallbackRef = useRef<((deviceId: string, data: number[]) => void) | undefined>(undefined);
  const deviceRecoveredCallbackRef = useRef<((deviceId: string) => void) | undefined>(undefined);
  const hardwareProbedCallbackRef = useRef<((deviceId: string, config: any) => void) | undefined>(undefined);
  const connectedDevicesRef = useRef<Device[]>([]);
  const allDevicesRef = useRef<Device[]>([]);
  const droppedOutDeviceIdsRef = useRef<string[]>([]);
  const blacklistedMacsRef = useRef<string[]>([]);

  useEffect(() => {
    const fetchBlacklist = async () => {
      try {
        const { data, error } = await supabase.from('hardware_blacklist').select('mac_address');
        if (data && !error) {
          blacklistedMacsRef.current = data.map(d => d.mac_address.toUpperCase());
        }
      } catch (e) {
        AppLogger.error('[BLE] Failed to fetch hardware blacklist on boot', e);
      }
    };
    fetchBlacklist();
  }, []);

  useEffect(() => {
    droppedOutDeviceIdsRef.current = droppedOutDeviceIds;
  }, [droppedOutDeviceIds]);

  useEffect(() => {
    AppLogger.updateKnownDevices(allDevices);
  }, [allDevices]);

  // Write-through setter: updates ref synchronously, then schedules React state update.
  // Eliminates the 1-frame ref-vs-state lag that caused phantom heartbeat pings (RC-01).
  const updateConnectedDevices: React.Dispatch<React.SetStateAction<Device[]>> = useCallback(
    (action) => {
      const prev = connectedDevicesRef.current;
      const next = typeof action === 'function' ? action(prev) : action;
      connectedDevicesRef.current = next;
      bleSend({ type: 'UPDATE_CONNECTED_DEVICES', devices: next });
    },
    [bleSend],
  );

  useEffect(() => {
    allDevicesRef.current = allDevices;
  }, [allDevices]);

  const handleNotification = (error: any, characteristic: any, deviceId: string) => {
    if (error) {
      const errMsg = error?.message || String(error);
      // Suppress normal organic dropouts from flooding the VIP error telemetry
      if (errMsg.includes('was disconnected') || errMsg.includes('is not connected') || errMsg.includes('Device disconnected') || errMsg.includes('not connected')) {
        AppLogger.warn(`[BLE] Organic notification disconnect for ${deviceId}`);
        return;
      }
      
      AppLogger.warn('Notification Error', error);
      AppLogger.log('PROTOCOL_ERROR', { error: errMsg, deviceId, context: 'notification' });
      return;
    }
    if (characteristic?.value) {
      try {
        const data = Array.from(Buffer.from(characteristic.value, 'base64')) as number[];
        if (dataReceivedCallbackRef.current) {
            dataReceivedCallbackRef.current(deviceId, data);
        }
      } catch (e: any) {
        AppLogger.error('Failed to parse notification', e);
        AppLogger.log('PROTOCOL_ERROR', { error: (e instanceof Error ? e.message : String(e)) || String(e), deviceId, context: 'parse' });
      }
    }
  };

  // Wrap handleNotification in a stable ref callback to prevent stale closures
  // when AutoRecovery re-registers notification monitors after reconnect.
  const handleNotificationRef = useRef(handleNotification);
  handleNotificationRef.current = handleNotification;

  useEffect(() => {
    if (!bleManager || Platform.OS === 'web') return;
    const subscription = bleManager.onStateChange((state: any) => {
      AppLogger.log('BLE_STATE_CHANGE', { state });
      if (state === State.Unsupported) {
        setIsBluetoothSupported(false);
      }
      setIsBluetoothEnabled(state === State.PoweredOn);
    }, true);
    
    // Audit native connections when app wakes up to prune stale react-state
    const appStateSub = AppState.addEventListener('change', async (nextState) => {
      if (nextState === 'active' && connectedDevicesRef.current.length > 0) {
        try {
          const liveChecks = await Promise.all(
            connectedDevicesRef.current.map(async d => ({
              id: d.id,
              connected: await bleManager.isDeviceConnected(d.id)
            }))
          );
          const staleIds = liveChecks.filter(c => !c.connected).map(c => c.id);
          if (staleIds.length > 0) {
            AppLogger.log('BLE_STATE_CHANGE', { event: 'pruning_stale_connections_on_wake', count: staleIds.length });
            updateConnectedDevices(prev => prev.filter(p => !staleIds.includes(p.id)));
          }
        } catch (e) {
          AppLogger.warn('[BLE] Failed to audit connections on wake', e);
        }
      }
    });

    return () => {
      subscription.remove();
      appStateSub.remove();
    };
  }, [bleManager]);


  /**
   * pingDevice — Wizard-exclusive atomic GATT session.
   *
   * Fixes two bugs introduced by perf/ble-probe-on-demand:
   *  1. "Phantom Blink": writeToDevice() requires connectedDevices.length > 0. During setup
   *     there is no Fleet connection, so the payload silently vanishes. Hardware never lights up.
   *  2. GATT collision: probeDevice() connects independently and its `finally` block severs
   *     the connection, racing against the blink write. GATT 133 ensues, probe returns null.
   *
   * Solution: one GATT session owns the full lifecycle:
   *   Connect → Discover → Write Blink → Monitor Notify → Write HW Query → Await EEPROM
   *     → (user watches skate blink for ~8s) → Write Off → Disconnect → Return hwConfig
   *
   * @param mac          Target device MAC address
   * @param blinkPayload Pre-built 0x59 multi-color payload from ZenggeProtocol.setMultiColor()
   * @returns            hwConfig object (ledPoints, segments, icName, colorSortingName, rfMode)
   *                     or null if probe timed out / connection failed.
   */
  // Moved pingDevice down below sweeper definition to enable scan preemption

  // --- Sub-Hooks ---
  const handleOrganicDisconnect = (error: any, deviceId: string) => {
    AppLogger.warn(`[BLE] Organic disconnect/dropout for ${deviceId}`);
    AppLogger.log('DEVICE_DISCONNECTED', { id: deviceId, reason: 'dropout', error: error?.message });
    autoRecovery.initiateRecovery(deviceId);
  };

  // Stable ref-forwarder: the BLE disconnect listener captures this ref once,
  // but it always delegates to the latest handleOrganicDisconnect. Same pattern
  // as handleNotificationRef (L224-225). Eliminates RC-06 stale closure risk.
  const handleOrganicDisconnectRef = useRef(handleOrganicDisconnect);
  handleOrganicDisconnectRef.current = handleOrganicDisconnect;

  // Stable ref-forwarder for connectToDevices — same pattern as pendingRegistrationsSetterRef.
  // autoRecovery is initialised BEFORE connectToDevices exists, so we’d get a stale closure
  // if we passed connectToDevices directly. The ref is updated immediately after connectToDevices
  // is defined (see below).
  const connectToDevicesRef = useRef<(devices: Device[]) => Promise<void>>(async () => {});

  const autoRecovery = useBLEAutoRecovery({
    bleManager,
    setConnectedDevices: updateConnectedDevices,
    disconnectListeners,
    handleNotification: (error: any, characteristic: any, deviceId: string) => handleNotificationRef.current(error, characteristic, deviceId),
    onOrganicDisconnect: (error: any, deviceId: string) => handleOrganicDisconnectRef.current(error, deviceId),
    onAdapterResolved: (deviceId: string, adapter: IControllerProtocol) => {
      // Keep adapterMapRef in sync when AutoRecovery reconnects a device.
      // The recovery hook resolves the adapter fresh from conn.services() after
      // GATT reconnect, then reports it here so writeToDevice keeps using the
      // correct protocol UUIDs without re-discovering from scratch.
      adapterMapRef.current.set(deviceId, adapter);
    },
    onDeviceRecovered: (deviceId: string) => {
      // Relay to consumers (DashboardScreen) so they can replay the last
      // pattern/color state to the recovered device after dropout.
      deviceRecoveredCallbackRef.current?.(deviceId);
    },
    onMtuNegotiated: (deviceId: string, mtu: number) => {
      // Persist the MTU negotiated during auto-recovery back into mtuMapRef.
      // Without this, post-recovery writes silently use the 186-byte fallback
      // even if the device negotiated a higher MTU (e.g. 517 bytes on Android).
      mtuMapRef.current.set(deviceId, mtu);
      AppLogger.log('DEVICE_CONNECTED', { context: 'mtu_recovery_updated', mtu, deviceId });
    },
    connectedDevicesRef,
    onGroupDropout: async (devices: Device[]) => connectToDevicesRef.current(devices),
    getSweepedDevice: (deviceId: string) => allDevicesRef.current.find(d => d.id === deviceId),
  });

  // ── Overwatch: Silent Sweeper + Interrogator Queue ────────────────────────
  // Declared BEFORE scanner so hwCache is available to feed into scanner props.
  // setPendingRegistrations uses a ref forwarder so scanner.setPendingRegistrations can be
  // wired in after scanner is initialized without violating hook ordering rules.
  const pendingRegistrationsSetterRef = useRef<React.Dispatch<React.SetStateAction<PendingRegistration[]>>>(() => {});
  const stablePendingRegistrationsSetter: React.Dispatch<React.SetStateAction<PendingRegistration[]>> = useCallback(
    (value) => pendingRegistrationsSetterRef.current(value),
    []
  );

  const sweeper = useBLESweeper({
    bleManager,
    setAllDevices,
    setPendingRegistrations: stablePendingRegistrationsSetter,
    registeredMacs,
  });

  const scanner = useBLEScanner({
    bleManager,
    allDevices,
    setAllDevices,
    hwCache: sweeper.hwCache,
    bleSend,
  });

  // Wire the real setter into the ref forwarder now that scanner is initialized.
  // Sweeper's Interrogator is always async (2s+ delay) so this is always populated in time.
  useEffect(() => {
    pendingRegistrationsSetterRef.current = scanner.setPendingRegistrations;
  }, [scanner.setPendingRegistrations]);

  const pingDevice = useCallback(async (
    mac: string,
    blinkPayload: number[],
    options?: { probe?: boolean; duration?: number; turnOffAtEnd?: boolean }
  ): Promise<PingResult | null> => {
    const wasSweeperActive = sweeper.isSweeperActive;
    if (wasSweeperActive) sweeper.stopSweeper();
    try {
      return await executePingDevice(bleManager, mac, blinkPayload, options);
    } finally {
      if (wasSweeperActive && bleManager) sweeper.startSweeper();
    }
  }, [bleManager, sweeper]);


  // ─── Per-device MTU map ────────────────────────────────────────────────────
  // Each device may negotiate a different MTU. Using a single shared ref caused
  // the "last device wins" bug where payload chunking used the wrong MTU.
  const mtuMapRef = useRef<Map<string, number>>(new Map());

  // ─── Per-device adapter map (HAL) ─────────────────────────────────────────
  // Stores the resolved IControllerProtocol adapter for each connected device.
  // Populated during connectToDevices Phase 2 handshake (after discoverServices).
  // Cleared on disconnect. Falls back to getDefaultProtocol() if device not found.
  // Enables mixed-protocol group writes: Zengge + BanlanX skates in the same group.
  const adapterMapRef = useRef<Map<string, IControllerProtocol>>(new Map());

  // ── Connection Health Heartbeat (MISS-03) ─────────────────────────────────
  // Pings every connected device every 45s to detect stale GATT handles early.
  // Samsung Galaxy A-series can hold stale handles alive for minutes after the
  // physical device powers off. Without this, the stale link is only discovered
  // on the next user write — triggering the slow ghost→recovery cycle mid-session.
  useBLEHeartbeat({
    bleManager,
    connectedDevicesRef,
    adapterMapRef,
    onStaleLinkDetected: (mac: string) => {
      // Drop the device from connected state so the controller closes gracefully.
      updateConnectedDevices(prev => prev.filter(d => d.id !== mac));
      // Immediately start Phase 1 aggressive recovery — don't wait for the next
      // organic disconnect event which could take minutes on stale handles.
      autoRecovery.initiateRecovery(mac);
    },
  });

  // ── Post-Connect Signal Quality Monitor (BAT-02) ─────────────────────────
  // Polls readRSSIForDevice every 30s to track live signal strength per device.
  // Surfaces rssiMap to UI (device card wifi icon) and triggers proactive
  // reconnect at -82 dBm to pick a better radio channel before the user notices.
  const rssiMap = useBLERSSIMonitor({
    bleManager,
    connectedDevicesRef,
    onCriticalSignal: (mac: string) => {
      // Only reconnect if device is not already in the recovery queue.
      if (!autoRecovery.ghostedDeviceIds.includes(mac)) {
        AppLogger.warn('[BLE RSSI] Critical signal — proactive reconnect', { mac });
        autoRecovery.initiateRecovery(mac);
      }
    },
  });

  const connectToDevices = useCallback(async (devices: Device[]) => {
    await executeConnectToDevices({
      devices,
      bleManager,
      connectedDevicesRef,
      blacklistedMacsRef,
      keepaliveTimerRef,
      disconnectListeners,
      sweeper,
      scanner,
      autoRecovery,
      getGate,
      mtuMapRef,
      adapterMapRef,
      dataReceivedCallbackRef,
      handleNotificationRef,
      handleOrganicDisconnect: (error: any, deviceId: string) =>
        handleOrganicDisconnectRef.current(error, deviceId),
      enqueueWrite,
      setConnectedDevices: updateConnectedDevices,
      setGate,
    });
  }, [
    bleManager,
    sweeper,
    scanner,
    autoRecovery,
    updateConnectedDevices,
    setGate,
    getGate,
  ]);

  // Wire connectToDevicesRef immediately after connectToDevices is defined so the
  // autoRecovery group coordinator always calls the latest version of the function.
  connectToDevicesRef.current = connectToDevices;

  // ── State refs and setters for BleWriteDispatcher ─────────────────────────
  const stateRefs = useMemo<BleWriteStateRefs>(() => ({
    get writeGeneration() { return writeGeneration; },
    writeDebounceTimerRef,
  }), []);

  const setWriteGeneration = useCallback((gen: number) => {
    writeGeneration = gen;
  }, []);

  const writeToDevice = useCallback(async (
    payload: number[],
    targetDeviceId?: string,
    opts?: { lowPriority?: boolean, writeType?: 'Response' | 'NoResponse' }
  ): Promise<boolean | 'partial'> => {
    const priority = resolveWritePriority(payload[0] || 0);
    return enqueueWrite(priority, () => executeWriteToDevice(
      payload,
      targetDeviceId,
      opts,
      bleManager,
      connectedDevices,
      autoRecovery.ghostedDeviceIds,
      mtuMapRef.current,
      adapterMapRef.current,
      stateRefs,
      setWriteGeneration
    ));
  }, [bleManager, connectedDevices, autoRecovery.ghostedDeviceIds, stateRefs, setWriteGeneration]);

  const writeChunked = useCallback(async (
    payload: number[],
    targetDeviceId?: string
  ): Promise<void> => {
    await enqueueWrite('bulk', () => executeWriteChunked(
      payload,
      targetDeviceId,
      connectedDevices,
      autoRecovery.ghostedDeviceIds,
      mtuMapRef.current,
      adapterMapRef.current
    ).then(() => true));
  }, [connectedDevices, autoRecovery.ghostedDeviceIds]);

  // ── BleLifecycleManager bindings ──────────────────────────────────────────
  const realDisconnect = useCallback(async () => {
    // Flush pending writes before tearing down GATT connections
    clearWriteQueue();
    await executeRealDisconnect(
      bleManager,
      connectedDevicesRef,
      disconnectListeners,
      mtuMapRef,
      adapterMapRef,
      autoRecovery,
      getGate,
      updateConnectedDevices,
      setGate
    );
  }, [bleManager, autoRecovery, updateConnectedDevices, setGate]);

  const disconnectFromDevice = useCallback(() => {
    keepaliveDisconnect(keepaliveTimerRef, KEEPALIVE_DURATION_MS, realDisconnect);
  }, [realDisconnect]);

  const forceDisconnect = useCallback(() => {
    keepaliveForceDisconnect(keepaliveTimerRef, realDisconnect);
  }, [realDisconnect]);

  const getAdapterForDevice = useCallback((deviceId: string): IControllerProtocol => {
    return resolveProtocolForDevice(deviceId, adapterMapRef.current);
  }, []);

  const executeProtocolResults = useCallback(async (
    payloads: { targetDeviceId: string; result: ProtocolResult }[],
    opts?: { lowPriority?: boolean }
  ): Promise<boolean> => {
    return executeProtocolResultsService(
      payloads,
      opts,
      bleManager,
      connectedDevices,
      autoRecovery.ghostedDeviceIds,
      mtuMapRef.current,
      adapterMapRef.current,
      stateRefs,
      setWriteGeneration
    );
  }, [bleManager, connectedDevices, autoRecovery.ghostedDeviceIds, stateRefs, setWriteGeneration]);

  const derivedBleState: BleConnectionState = 
    bleGateState === 'DISCONNECTING' ? 'DISCONNECTING' :
    bleGateState === 'CONNECTING' ? 'CONNECTING' :
    bleGateState === 'SCANNING' ? 'SCANNING' :
    connectedDevices.length > 0 ? 'READY' : 'IDLE';

  return useMemo(() => ({
    requestPermissions: async () => {
      return await requestPermission('BLUETOOTH');
    },
    connectToDevices,
    writeToDevice,
    writeChunked,
    allDevices,
    setAllDevices,
    connectedDevices,
    disconnectFromDevice,
    forceDisconnect,
    isBluetoothSupported,
    isBluetoothEnabled,
    setOnDataReceived: (callback: (deviceId: string, data: number[]) => void) => { 
        dataReceivedCallbackRef.current = callback;
    },
    setOnHardwareProbed: (callback: (deviceId: string, config: any) => void) => { 
        hardwareProbedCallbackRef.current = callback; 
    },
    setOnDeviceRecovered: (callback: (deviceId: string) => void) => {
        deviceRecoveredCallbackRef.current = callback;
    },
    droppedOutDeviceIds,
    setDroppedOutDeviceIds,
    pendingRegistrations: scanner.pendingRegistrations,
    clearPendingRegistrations: () => scanner.setPendingRegistrations([]),
    setPendingRegistrations: scanner.setPendingRegistrations,
    pingDevice,
    getAdapterForDevice,
    executeProtocolResults,
    ghostedDeviceIds: bleSnapshot.context.ghostedDeviceIds.length > 0 ? bleSnapshot.context.ghostedDeviceIds : autoRecovery.ghostedDeviceIds,
    bleState: derivedBleState,
    getGate,
    bleActorRef,
    startSweeper: sweeper.startSweeper,
    stopSweeper: sweeper.stopSweeper,
    burstScan: sweeper.burstScan,
    isSweeperActive: sweeper.isSweeperActive,
    hwCache: sweeper.hwCache,
    rssiMap,
    // ── Overwatch-aware scanForPeripherals ─────────────────────────────────
    // When Sweeper is running: delegate to burstScan() (elevate scan mode 5s then revert).
    // When Sweeper is idle: fall through to the original useBLEScanner.scanForPeripherals.
    // This eliminates the dual startDeviceScan() conflict (single scan loop, all consumers).
    scanForPeripherals: (options?: { keepAlive?: boolean; disableProbing?: boolean }) => {
      if (sweeper.isSweeperActive) {
        // FIX: Fire-and-forget — do NOT await burstScan.
        // Awaiting it locked bleState='SCANNING' for the full 5s burst duration,
        // blocking the Wizard from ever showing discovered devices.
        // burstScan handles its own timer + revert internally.
        sweeper.burstScan(options?.keepAlive ? 10000 : 5000);
      } else {
        scanner.scanForPeripherals(options);
      }
    },
  }), [
    allDevices,
    connectedDevices,
    bleGateState,
    scanner.pendingRegistrations,
    isBluetoothSupported,
    isBluetoothEnabled,
    droppedOutDeviceIds,
    autoRecovery.ghostedDeviceIds,
    bleGateState,
    sweeper.isSweeperActive,
    sweeper.hwCache,
    sweeper.burstScan,
    rssiMap,
  ]);
}
