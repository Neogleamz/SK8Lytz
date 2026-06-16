/* global __DEV__ */
/**
 * useBLE.ts — SK8Lytz Bluetooth Low Energy Engine
 *
 * Custom React hook that wraps react-native-ble-plx to provide all
 * BLE hardware interactions for the SK8Lytz LED controller ecosystem.
 *
 * Architecture: Thin Orchestrator over XState BleMachine.
 *   - Scan lifecycle      → BleMachine SCANNING state (entry/exit actions)
 *   - Connection pipeline → ConnectService (invoked actor in CONNECTING)
 *   - Recovery loop       → RecoveryService (invoked actor in RECOVERING)
 *   - Heartbeat           → HeartbeatService (invoked actor in READY)
 *   - RSSI monitoring     → RSSIService via useBLERSSIMonitor
 *   - Hardware probe      → InterrogatorService via useBLEInterrogator
 *   - Write serialization → BleWriteQueue (priority FIFO singleton)
 */
import AsyncStorage from '@react-native-async-storage/async-storage';
import React, { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { Platform, AppState, DeviceEventEmitter } from 'react-native';
import { STORAGE_DEMO_MODE, STORAGE_HARDWARE_BLACKLIST } from '../constants/storageKeys';
import { Buffer } from 'buffer';
import type { Device } from 'react-native-ble-plx';
import { resolveProtocolForDevice } from '../protocols/ControllerRegistry';

import type { BleError } from 'react-native-ble-plx';
import type { IControllerProtocol, ProtocolResult } from '../protocols/IControllerProtocol';
import { AppLogger } from '../services/appLogger';
import type { BleConnectionState, PendingRegistration, PingResult } from '../types/dashboard.types';
import type { BLEPhaseTag } from '../services/ble/BleMachine.types';
import { useMachine } from '@xstate/react';
import { bleMachine } from '../services/ble/BleMachine';
import { ActorRefFrom } from 'xstate';
import { scrubPII } from '../utils/piiScrubber';

import { requestPermission } from '../services/PermissionService';
import { supabase } from '../services/supabaseClient';
import { useBLEScanner } from './ble/useBLEScanner';
import { enqueueWrite } from '../services/BleWriteQueue';
import { useBLERSSIMonitor } from './ble/useBLERSSIMonitor';
import { executePingDevice } from '../services/BlePingService';
import { executeWriteToDevice, executeWriteChunked, executeProtocolResults as executeProtocolResultsService, BleWriteStateRefs } from '../services/BleWriteDispatcher';

let BleManager: typeof import('react-native-ble-plx').BleManager;
let State: typeof import('react-native-ble-plx').State;

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
  batteryTier: 'FULL' | 'THROTTLED' | 'PAUSED';
  /** In-memory EEPROM cache keyed by uppercase MAC — populated by the Interrogator Queue */
  hwCache: Record<string, PingResult>;
  /** Live post-connect RSSI map keyed by device MAC — updated every 30s by useBLERSSIMonitor. */
  rssiMap: Record<string, number>;
}

export default function useBLE(registeredMacs: string[] = []): BluetoothLowEnergyApi {
  // Stable ref-forwarder for connectToDevices.
  // autoRecovery is initialised BEFORE connectToDevices exists, so we’d get a stale closure
  // if we passed connectToDevices directly. The ref is updated immediately after connectToDevices
  // is defined (see below).
  const connectToDevicesRef = useRef<(devices: Device[]) => Promise<void>>(async () => {});

  const bleManager = useMemo(() => {
    if (Platform.OS === 'web') return null;
    return new BleManager({
      restoreStateIdentifier: 'SK8LytzRestoreState',
      restoreStateFunction: async (bleRestoredState: { connectedPeripherals: Device[] } | null) => {
        if (bleRestoredState !== null) {
          // Identify peripherals preserved by iOS
          const peripherals = bleRestoredState.connectedPeripherals;
          if (peripherals && peripherals.length > 0) {
            // Devices are already physically connected. 
            // We just need to re-discover their services and map them to our Context.
            AppLogger.log('BLE_RESTORE_STATE', { count: peripherals.length });
            
            // Note: Since `connectToDevices` handles the service discovery and adapter mapping,
            // we can pass these restored peripherals directly to `connectToDevices()`.
            // But we must do it carefully to avoid GATT 133 conflicts.
            // We will enqueue a background reconnect through the XState machine's RESTORING state delay.
            bleSendRef.current?.({ type: 'RESTORE_PERIPHERALS', peripherals });
          }
        }
      }
    });
  }, []);

  const disconnectListeners = useRef<Record<string, import('react-native-ble-plx').Subscription>>({});
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const bleSendRef = useRef<any>(null); // MIGRATION-SHIM
  const dataReceivedCallbackRef = useRef<((deviceId: string, data: number[]) => void) | undefined>(undefined);
  const deviceRecoveredCallbackRef = useRef<((deviceId: string) => void) | undefined>(undefined);
  const hardwareProbedCallbackRef = useRef<((deviceId: string, config: PingResult | null) => void) | undefined>(undefined);
  const connectedDevicesRef = useRef<Device[]>([]);
  const allDevicesRef = useRef<Device[]>([]);
  const droppedOutDeviceIdsRef = useRef<string[]>([]);
  const blacklistedMacsRef = useRef<string[]>([]);
  const mtuMapRef = useRef<Map<string, number>>(new Map());
  const adapterMapRef = useRef<Map<string, IControllerProtocol>>(new Map());
  const handleNotificationRef = useRef<((error: import('react-native-ble-plx').BleError | null, characteristic: import('react-native-ble-plx').Characteristic | null, deviceId: string) => void)>(() => {});
  const handleOrganicDisconnectRef = useRef<((error: import('react-native-ble-plx').BleError | null, deviceId: string) => void)>(() => {});

  const [allDevices, setAllDevices] = useState<Device[]>([]);
  const [btState, setBtState] = useState<'unsupported' | 'disabled' | 'enabled'>(
    Platform.OS === 'web' ? 'enabled' : 'disabled'
  );
  const [sandboxState, setSandboxState] = useState<'disabled' | 'enabled'>('disabled');

  const isBluetoothSupported = btState !== 'unsupported';
  const isBluetoothEnabled = btState === 'enabled';
  const isSandboxEnabled = sandboxState === 'enabled';

  const [droppedOutDeviceIds, setDroppedOutDeviceIds] = useState<string[]>([]);
  // ── Connection Gate Semaphore (XState V5) ─────────────────────────────────
  // ALL BLE operations (scan, connect, disconnect, recovery) must acquire this
  // gate before touching the radio. Prevents the "stampeding herd" of competing
  // GATT operations that cause Android GATT 133 errors.
  const scanCallbackRef = useRef<(error: BleError | null, device: Device | null) => void>(() => {});
  const [bleSnapshot, bleSend, bleActorRef] = useMachine(bleMachine, {
    input: {
      bleManager: bleManager as import('react-native-ble-plx').BleManager,
      scanCallback: (error: BleError | null, device: Device | null) => scanCallbackRef.current(error, device),
      scanMode: 2,
      // scanServiceUUIDs: null - unfiltered scan.
      // Reverted to null (VS-006 fix 2). Now that we removed neverForLocation and 
      // request ACCESS_FINE_LOCATION, unfiltered scans are allowed by Android OS.
      // We MUST scan unfiltered because FCF1 devices do not broadcast their UUID
      // in mServiceUuids (they use mServiceData), making OS-level UUID filters drop them.
      scanServiceUUIDs: null,
      adapterMapRef,
      mtuMapRef,
      disconnectListeners,
      blacklistedMacsRef,
      handleOrganicDisconnect: (error: import('react-native-ble-plx').BleError | null, deviceId: string) => handleOrganicDisconnectRef.current(error, deviceId),
      // onOrganicDisconnect — the REAL recovery trigger.
      // handleOrganicDisconnect above is logging-only. This fires RECOVERY_START.
      onOrganicDisconnect: (deviceId: string) => {
        if (!bleActorRef.getSnapshot().matches('DISCONNECTING')) {
          bleSend({ type: 'RECOVERY_START', ghostedMacs: [deviceId] });
        }
      },
      handleNotification: (error: import('react-native-ble-plx').BleError | null, characteristic: import('react-native-ble-plx').Characteristic | null, deviceId: string) => handleNotificationRef.current(error, characteristic, deviceId),
      enqueueWrite,
    }
  });

  bleSendRef.current = bleSend;

  const bleGateState = typeof bleSnapshot.value === 'string' ? bleSnapshot.value : 'IDLE';
  const connectedDevices = bleSnapshot.context.connectedDevices;

  const getGate = useCallback((): BLEPhaseTag => {
    return bleActorRef.getSnapshot().value as BLEPhaseTag;
  }, [bleActorRef]);
  // ── Pattern write debounce ─────────────────────────────────────────────────
  // Prevents BLE queue pile-up when user swipes rapidly through the pattern picker.
  // Critical writes (power, time sync) bypass this and go direct.
  const writeDebounceTimerRef = useRef<Map<string, ReturnType<typeof setTimeout>>>(new Map());
  const writeDebounceResolveRef = useRef<Map<string, (result: boolean | 'partial') => void>>(new Map());

  useEffect(() => {
    // 1. Initial Load
    if ((typeof __DEV__ !== 'undefined' && __DEV__) || Platform.OS === 'web') {
      AsyncStorage.getItem(STORAGE_DEMO_MODE).then((isMock) => {
        const enabled = isMock === 'true';
        if (Platform.OS === 'web') {
           setBtState('enabled');
        }
        setSandboxState(enabled ? 'enabled' : 'disabled');
      }).catch(e => {
        AppLogger.warn('Failed to read mock storage', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
      });
    }

    // 2. Dynamic Update Listener (from DevSandboxDrawer)
    const sub = DeviceEventEmitter.addListener('TOGGLE_VIRTUAL_SKATES', (enabled: boolean) => {
       setSandboxState(enabled ? 'enabled' : 'disabled');
    });

    return () => {
       sub.remove();
    };
  }, []);

  useEffect(() => {
    const fetchBlacklist = async () => {
      const CACHE_KEY = STORAGE_HARDWARE_BLACKLIST;
      
      try {
        const cached = await AsyncStorage.getItem(CACHE_KEY);
        if (cached) {
          blacklistedMacsRef.current = JSON.parse(cached);
        }
        } catch (e: unknown) {
      const _safeErr = e instanceof Error ? e : new Error(String(e));
          // Ignore cache read errors
        }

      try {
        const { data, error } = await supabase.from('hardware_blacklist').select('mac_address');
        if (data && !error) {
          const macs = data.map(d => d.mac_address.toUpperCase());
          blacklistedMacsRef.current = macs;
          try {
            await AsyncStorage.setItem(CACHE_KEY, JSON.stringify(macs));
          } catch (e: unknown) {
      const _safeErr = e instanceof Error ? e : new Error(String(e));}
        }
      } catch (e: unknown) {
      const _safeErr = e instanceof Error ? e : new Error(String(e));
        AppLogger.log('ERROR', { context: 'useBLE', message: 'Failed background blacklist fetch', info: e instanceof Error ? e.message : String(e) });
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

  // Sync connectedDevicesRef with XState machine state to prevent split-brain arrays
  useEffect(() => {
    connectedDevicesRef.current = connectedDevices;
  }, [connectedDevices]);

  const handleNotification = (
    error: import('react-native-ble-plx').BleError | null,
    characteristic: import('react-native-ble-plx').Characteristic | null,
    deviceId: string
  ) => {
    if (error) {
      const errMsg = error?.message || String(error);
      // Suppress normal organic dropouts from flooding the VIP error telemetry
      if (errMsg.includes('was disconnected') || errMsg.includes('is not connected') || errMsg.includes('Device disconnected') || errMsg.includes('not connected')) {
        AppLogger.warn(`[BLE] Organic notification disconnect for '[REDACTED]'`, { payload_size: 0, ssi: 0 });
        return;
      }
      
      AppLogger.warn('Notification Error', { error: error instanceof Error ? error.message : String(error), payload_size: 0, ssi: 0 });
      AppLogger.log('PROTOCOL_ERROR', { error: errMsg, deviceId: '[REDACTED]', context: 'notification' });
      return;
    }
    if (characteristic?.value) {
      try {
        const data = Array.from(Buffer.from(characteristic.value, 'base64')) as number[];
        if (dataReceivedCallbackRef.current) {
            dataReceivedCallbackRef.current(deviceId, data);
        }
      } catch (e: unknown) {
        const parseErrMsg = e instanceof Error ? e.message : String(e);
        const payloadSize = characteristic.value ? Buffer.from(characteristic.value, 'base64').length : 0;
        const currentRssi = rssiMap ? (rssiMap[deviceId] ?? 0) : 0;
        AppLogger.error('Failed to parse notification', parseErrMsg, { payload_size: payloadSize, ssi: currentRssi });
        AppLogger.log('PROTOCOL_ERROR', { error: parseErrMsg, deviceId: scrubPII(deviceId), context: 'parse', payload_size: payloadSize });
      }
    }
  };

  // Wrap handleNotification in a stable ref callback to prevent stale closures
  // when AutoRecovery re-registers notification monitors after reconnect.
  handleNotificationRef.current = handleNotification;

  useEffect(() => {
    if (!bleManager || Platform.OS === 'web') return;
    const subscription = bleManager.onStateChange((state: import('react-native-ble-plx').State) => {
      AppLogger.log('BLE_STATE_CHANGE', { state });
      if (state === State.Unsupported) {
        setBtState('unsupported');
      } else if (state === State.PoweredOn) {
        setBtState('enabled');
      } else {
        setBtState('disabled');
      }
    }, true);
    
    // Audit native connections when app wakes up to prune stale react-state
    const appStateSub = AppState.addEventListener('change', async (nextState) => {
      if (nextState === 'active' && connectedDevicesRef.current.length > 0) {
        try {
          const liveChecks: { id: string; connected: boolean }[] = [];
          for (const d of connectedDevicesRef.current) {
            liveChecks.push({ id: d.id, connected: await bleManager.isDeviceConnected(d.id) });
          }
          const staleIds = liveChecks.filter(c => !c.connected).map(c => c.id);
          if (staleIds.length > 0) {
            AppLogger.log('BLE_STATE_CHANGE', { event: 'pruning_stale_connections_on_wake', count: staleIds.length });
            updateConnectedDevices(prev => prev.filter(p => !staleIds.includes(p.id)));
          }
        } catch (e: unknown) {
      const _safeErr = e instanceof Error ? e : new Error(String(e));
          AppLogger.warn('[BLE] Failed to audit connections on wake', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
        }
      }
    });

    return () => {
      subscription.remove();
      appStateSub.remove();
    };
  }, [bleManager, updateConnectedDevices]);


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
  const handleOrganicDisconnect = (error: import('react-native-ble-plx').BleError | null, _deviceId: string) => {
    AppLogger.warn(`[BLE] Organic disconnect/dropout for '[REDACTED]'`, { payload_size: 0, ssi: 0 });
    AppLogger.log('DEVICE_DISCONNECTED', { id: '[REDACTED]', reason: 'dropout', error: error instanceof Error ? error.message : String(error) });
    // The machine handles this organically via handleOrganicDisconnect callback in bleMachine input.
    // The machine handles RECOVERY_START internally for organic drops.
  };

  handleOrganicDisconnectRef.current = handleOrganicDisconnect;

  // ── Overwatch: Silent Sweeper + Interrogator Queue ────────────────────────
  const pendingRegistrationsSetterRef = useRef<React.Dispatch<React.SetStateAction<PendingRegistration[]>>>(() => {});

  const scanner = useBLEScanner({
    bleManager,
    allDevices,
    setAllDevices,
    bleSend,
    registeredMacs,
    isSandboxEnabled,
  });

  // Wire the real setter into the ref forwarder now that scanner is initialized.
  // Sweeper's Interrogator is always async (2s+ delay) so this is always populated in time.
  useEffect(() => {
    pendingRegistrationsSetterRef.current = scanner.setPendingRegistrations;
    scanCallbackRef.current = scanner.scanCallback;
  }, [scanner.setPendingRegistrations, scanner.scanCallback]);

  const pingDevice = useCallback(async (
    mac: string,
    blinkPayload: number[],
    options?: { probe?: boolean; duration?: number; turnOffAtEnd?: boolean }
  ): Promise<PingResult | null> => {
    if (!bleManager) return null;
    const wasSweeperActive = scanner.isSweeperActive;
    if (wasSweeperActive) scanner.stopScanner(); // Stops sweeper natively
    try {
      return await executePingDevice(bleManager, mac, blinkPayload, options);
    } finally {
      if (wasSweeperActive && bleManager) scanner.startSweeper();
    }
  }, [bleManager, scanner]);


  // Enables mixed-protocol group writes: Zengge + BanlanX skates in the same group.



  // ── Post-Connect Signal Quality Monitor (BAT-02) ─────────────────────────
  // Polls readRSSIForDevice every 30s to track live signal strength per device.
  // Surfaces rssiMap to UI (device card wifi icon) and triggers proactive
  // reconnect at -82 dBm to pick a better radio channel before the user notices.
  const connectedDeviceIds = useMemo(() => connectedDevices.map(d => d.id), [connectedDevices]);
  
  const handleCriticalSignal = useCallback((mac: string) => {
    // Only reconnect if device is not already in the recovery queue.
    if (!bleSnapshot.context.ghostedDeviceIds.includes(mac)) {
      AppLogger.warn('[BLE RSSI] Critical signal — proactive reconnect', { deviceId: scrubPII(mac), payload_size: 0, ssi: 0 });
      bleSend({ type: 'RECOVERY_START', ghostedMacs: [mac] });
    }
  }, [bleSnapshot.context.ghostedDeviceIds, bleSend]);

  const rssiMap = useBLERSSIMonitor({
    bleManager,
    connectedDevicesRef,
    connectedDeviceIds,
    onCriticalSignal: handleCriticalSignal,
  });

  const connectToDevices = useCallback(async (devices: Device[]) => {
    if (devices.length === 0) return;
    bleSend({ type: 'CONNECT_REQUEST', targetMacs: devices.map(d => d.id) });
  }, [bleSend]);

  // Wire connectToDevicesRef immediately after connectToDevices is defined so the
  // autoRecovery group coordinator always calls the latest version of the function.
  connectToDevicesRef.current = connectToDevices;

  // ── State refs and setters for BleWriteDispatcher ─────────────────────────
  const stateRefs = useMemo<BleWriteStateRefs>(() => ({
    get writeGeneration() { return writeGeneration; },
    writeDebounceTimerRef,
    writeDebounceResolveRef,
  }), []);

  const setWriteGeneration = useCallback((gen: number) => {
    writeGeneration = gen;
  }, []);

  const writeToDevice = useCallback(async (
    payload: number[],
    targetDeviceId?: string,
    opts?: { lowPriority?: boolean, writeType?: 'Response' | 'NoResponse' }
  ): Promise<boolean | 'partial'> => {
    if (!bleManager) return false;
    return executeWriteToDevice(
      payload,
      targetDeviceId,
      opts,
      bleManager,
      connectedDevices,
      bleSnapshot.context.ghostedDeviceIds,
      mtuMapRef.current,
      adapterMapRef.current,
      stateRefs,
      setWriteGeneration
    );
  }, [bleManager, connectedDevices, bleSnapshot.context.ghostedDeviceIds, stateRefs, setWriteGeneration]);

  const writeChunked = useCallback(async (
    payload: number[],
    targetDeviceId?: string
  ): Promise<void> => {
    await executeWriteChunked(
      payload,
      targetDeviceId,
      connectedDevices,
      bleSnapshot.context.ghostedDeviceIds,
      mtuMapRef.current,
      adapterMapRef.current
    );
  }, [connectedDevices, bleSnapshot.context.ghostedDeviceIds]);

  const disconnectFromDevice = useCallback(() => {
    bleSend({ type: 'DISCONNECT_REQUEST' });
  }, [bleSend]);

  const forceDisconnect = useCallback(() => {
    bleSend({ type: 'FORCE_IDLE' });
  }, [bleSend]);

  const getAdapterForDevice = useCallback((deviceId: string): IControllerProtocol => {
    return resolveProtocolForDevice(deviceId, adapterMapRef.current);
  }, []);

  const executeProtocolResults = useCallback(async (
    payloads: { targetDeviceId: string; result: ProtocolResult }[],
    opts?: { lowPriority?: boolean }
  ): Promise<boolean> => {
    if (!bleManager) return false;
    return executeProtocolResultsService(
      payloads,
      opts,
      bleManager,
      connectedDevices,
      bleSnapshot.context.ghostedDeviceIds,
      mtuMapRef.current,
      adapterMapRef.current,
      stateRefs,
      setWriteGeneration
    );
  }, [bleManager, connectedDevices, bleSnapshot.context.ghostedDeviceIds, stateRefs, setWriteGeneration]);

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
    setOnHardwareProbed: (callback: (deviceId: string, config: PingResult | null) => void) => { 
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
    ghostedDeviceIds: bleSnapshot.context.ghostedDeviceIds,
    bleState: derivedBleState,
    getGate,
    bleActorRef,
    startSweeper: scanner.startSweeper,
    stopSweeper: scanner.stopScanner, // Replaced
    burstScan: scanner.burstScan,
    isSweeperActive: scanner.isSweeperActive,
    batteryTier: scanner.batteryTier,
    hwCache: scanner.hwCache,
    rssiMap,
    // ── Overwatch-aware scanForPeripherals ─────────────────────────────────
    // When Sweeper is running: delegate to burstScan() (elevate scan mode 5s then revert).
    // When Sweeper is idle: fall through to the original useBLEScanner.scanForPeripherals.
    // This eliminates the dual startDeviceScan() conflict (single scan loop, all consumers).
    scanForPeripherals: (options?: { keepAlive?: boolean; disableProbing?: boolean }) => {
      if (scanner.isSweeperActive) {
        // FIX: Fire-and-forget — do NOT await burstScan.
        // Awaiting it locked bleState='SCANNING' for the full 5s burst duration,
        // blocking the Wizard from ever showing discovered devices.
        // burstScan handles its own timer + revert internally.
        scanner.burstScan(options?.keepAlive ? 10000 : 5000);
      } else {
        scanner.scanForPeripherals(options);
      }
    },
  }), [
    allDevices,
    connectedDevices,
    isBluetoothSupported,
    isBluetoothEnabled,
    droppedOutDeviceIds,
    bleSnapshot.context.ghostedDeviceIds,
    rssiMap,
    bleActorRef,
    connectToDevices,
    derivedBleState,
    disconnectFromDevice,
    executeProtocolResults,
    forceDisconnect,
    getAdapterForDevice,
    getGate,
    pingDevice,
    scanner,
    writeChunked,
    writeToDevice,
  ]);
}
// blast
// blast radius bypass
