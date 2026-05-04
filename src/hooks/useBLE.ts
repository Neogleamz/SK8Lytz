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
import { Buffer } from 'buffer';
import { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { Alert, Platform } from 'react-native';
import type { Device } from 'react-native-ble-plx';
import { ZENGGE_CHARACTERISTIC_UUID, ZENGGE_NOTIFY_UUID, ZENGGE_SERVICE_UUID, ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { AppLogger } from '../services/AppLogger';
import type { BleConnectionState, PendingRegistration } from '../types/dashboard.types';

import { checkPermission, openGlobalPermissionsModal } from '../services/PermissionService';
import { useBLEScanner } from './ble/useBLEScanner';
import { useBLEAutoRecovery } from './ble/useBLEAutoRecovery';
import { useBLESweeper } from './ble/useBLESweeper';

let BleManager: any;
let State: any;

if (Platform.OS !== 'web') {
  const blePlx = require('react-native-ble-plx');
  BleManager = blePlx.BleManager;
  State = blePlx.State;
}

let writeMutex: Promise<any> = Promise.resolve();
let writeGeneration = 0; // Increments on every new write; stale debounce checks compare against this

/** AsyncStorage key for last-sent pattern payload per group/device */
const PATTERN_CACHE_KEY = (groupId: string) => `@Sk8lytz_last_pattern_${groupId.toUpperCase()}`;

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
  /**
   * Wizard-specific atomic ping: Connect → Blink → Probe EEPROM → Turn Off → Disconnect.
   * Designed for use in HardwareSetupWizardScreen only. Bypasses connectedDevices requirement.
   * Returns hwConfig (ledPoints, icName, etc.) or null if probe timed out.
   */
  pingDevice: (mac: string, blinkPayload: number[]) => Promise<any>;
  connectedDevices: Device[];
  allDevices: Device[];
  setAllDevices: React.Dispatch<React.SetStateAction<Device[]>>;
  isBluetoothSupported: boolean;
  isBluetoothEnabled: boolean;
  setOnDataReceived: (callback: (deviceId: string, data: number[]) => void) => void;
  setOnHardwareProbed: (callback: (deviceId: string, config: any) => void) => void;
  setOnDeviceRecovered: (callback: (deviceId: string) => void) => void;
  droppedOutDeviceIds: string[];
  setDroppedOutDeviceIds: React.Dispatch<React.SetStateAction<string[]>>;
  pendingRegistrations: PendingRegistration[];
  clearPendingRegistrations: () => void;
  /** Setter for pendingRegistrations — passed to HardwareSetupWizardScreen for lazy BLINK probe enrichment */
  setPendingRegistrations: React.Dispatch<React.SetStateAction<PendingRegistration[]>>;
  ghostedDeviceIds: string[];
  bleState: BleConnectionState;
  /** Global connection gate semaphore — exposed for consumers that need gate-awareness */
  bleGateRef: React.MutableRefObject<'IDLE' | 'SCANNING' | 'CONNECTING' | 'DISCONNECTING' | 'RECOVERING'>;
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
}

export default function useBLE(registeredMacs: string[] = []): BluetoothLowEnergyApi {
  const bleManager = useMemo(() => {
    if (Platform.OS === 'web') return null;
    return new BleManager();
  }, []);

  const [allDevices, setAllDevices] = useState<Device[]>([]);
  const [connectedDevices, setConnectedDevices] = useState<Device[]>([]);
  const [isBluetoothSupported, setIsBluetoothSupported] = useState(Platform.OS !== 'web');
  const [isBluetoothEnabled, setIsBluetoothEnabled] = useState(Platform.OS === 'web');
  const [droppedOutDeviceIds, setDroppedOutDeviceIds] = useState<string[]>([]);
  // ── Connection Gate Semaphore ──────────────────────────────────────────────
  // ALL BLE operations (scan, connect, disconnect, recovery) must acquire this
  // gate before touching the radio. Prevents the "stampeding herd" of competing
  // GATT operations that cause Android GATT 133 errors.
  const bleGateRef = useRef<'IDLE' | 'SCANNING' | 'CONNECTING' | 'DISCONNECTING' | 'RECOVERING'>('IDLE');
  const [bleGateState, setBleGateState] = useState<'IDLE' | 'SCANNING' | 'CONNECTING' | 'DISCONNECTING' | 'RECOVERING'>('IDLE');
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

  // Helper: set gate in both ref (for sync checks) and state (for React re-renders)
  const setGate = useCallback((phase: 'IDLE' | 'SCANNING' | 'CONNECTING' | 'DISCONNECTING' | 'RECOVERING') => {
    bleGateRef.current = phase;
    setBleGateState(phase);
  }, []);

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
  const droppedOutDeviceIdsRef = useRef<string[]>([]);

  useEffect(() => {
    droppedOutDeviceIdsRef.current = droppedOutDeviceIds;
  }, [droppedOutDeviceIds]);

  useEffect(() => {
    AppLogger.updateKnownDevices(allDevices);
  }, [allDevices]);

  useEffect(() => { 
    connectedDevicesRef.current = connectedDevices; 
  }, [connectedDevices]);

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
        AppLogger.log('PROTOCOL_ERROR', { error: e?.message || String(e), deviceId, context: 'parse' });
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
    return () => subscription.remove();
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
  const pingDevice = async (mac: string, blinkPayload: number[]): Promise<any> => {
    if (Platform.OS === 'web' || !bleManager) return null;

    try {
      // ── Step 1: Connect ───────────────────────────────────────────────────────
      await bleManager.connectToDevice(mac, { timeout: 6000 }).catch((e: any) => {
        if (!String(e).includes('already')) throw e;
      });
      await bleManager.discoverAllServicesAndCharacteristicsForDevice(mac);

      // ── Step 2: Write Blink (channel is now hot — no Phantom Blink) ───────────
      const b64Blink = Buffer.from(blinkPayload).toString('base64');
      await bleManager.writeCharacteristicWithoutResponseForDevice(
        mac, ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64Blink
      ).catch(() => {});

      // ── Step 3: Probe EEPROM (same GATT session — no collision) ──────────────
      const hwConfig = await new Promise<any>((resolve) => {
        let accumulatedTelemetry: any = null;

        const timer = setTimeout(() => {
          sub.remove();
          if (accumulatedTelemetry) {
            AppLogger.warn(`[BLE pingDevice] Partial telemetry for ${mac}. Returning partial.`);
            resolve(accumulatedTelemetry);
          } else {
            AppLogger.warn(`[BLE pingDevice] Probe timed out for ${mac} after 3500ms.`);
            resolve(null);
          }
        }, 3500);

        const sub = bleManager.monitorCharacteristicForDevice(
          mac,
          ZENGGE_SERVICE_UUID,
          ZENGGE_NOTIFY_UUID,
          (err: any, char: any) => {
            if (err) return;
            if (!char?.value) return;
            try {
              const raw = Array.from(Buffer.from(char.value, 'base64')) as number[];
              const hwParsed = ZenggeProtocol.parseHardwareSettingsResponse(raw);
              if (hwParsed) accumulatedTelemetry = { ...accumulatedTelemetry, ...hwParsed };
              const rfParsed = ZenggeProtocol.parseRfRemoteState(raw);
              if (rfParsed) accumulatedTelemetry = { ...accumulatedTelemetry, rfMode: rfParsed.mode, rfPairedCount: rfParsed.pairedCount };
              if (accumulatedTelemetry?.detected && accumulatedTelemetry?.rfMode) {
                clearTimeout(timer);
                sub.remove();
                AppLogger.log('DEVICE_DISCOVERED', { context: 'pingDevice_probe_success', deviceId: mac });
                resolve(accumulatedTelemetry);
              }
            } catch (e) { /* ignore parse errors */ }
          }
        );

        // Fire queries after giving the notification monitor 400ms to set up
        setTimeout(() => {
          const b64HW = Buffer.from(ZenggeProtocol.queryHardwareSettings(false)).toString('base64');
          bleManager.writeCharacteristicWithoutResponseForDevice(
            mac, ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64HW
          ).catch((e: any) => AppLogger.warn('[BLE pingDevice] HW query write failed', { error: String(e) }));

          setTimeout(() => {
            const b64RF = Buffer.from(ZenggeProtocol.queryRfRemoteState()).toString('base64');
            bleManager.writeCharacteristicWithoutResponseForDevice(
              mac, ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64RF
            ).catch((e: any) => AppLogger.warn('[BLE pingDevice] RF query write failed', { error: String(e) }));
          }, 200);
        }, 400);
      });

      // ── Step 4: Wait so user can see the blink (probe ran concurrently) ───────
      await new Promise(r => setTimeout(r, 8000));

      // ── Step 5: Turn Off ─────────────────────────────────────────────────────
      const b64Off = Buffer.from(ZenggeProtocol.turnOff()).toString('base64');
      await bleManager.writeCharacteristicWithoutResponseForDevice(
        mac, ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64Off
      ).catch(() => {});

      return hwConfig;
    } catch (err: any) {
      AppLogger.warn(`[BLE pingDevice] Failed for ${mac}:`, { error: String(err) });
      return null;
    } finally {
      // ── Step 6: Always disconnect cleanly ────────────────────────────────────
      await bleManager.cancelDeviceConnection(mac).catch(() => {});
    }
  };

  // --- Sub-Hooks ---
  const handleOrganicDisconnect = (error: any, deviceId: string) => {
    AppLogger.warn(`[BLE] Organic disconnect/dropout for ${deviceId}`);
    AppLogger.log('DEVICE_DISCONNECTED', { id: deviceId, reason: 'dropout', error: error?.message });
    autoRecovery.initiateRecovery(deviceId);
  };

  const autoRecovery = useBLEAutoRecovery({
    bleManager,
    setConnectedDevices,
    disconnectListeners,
    handleNotification: (error: any, characteristic: any, deviceId: string) => handleNotificationRef.current(error, characteristic, deviceId),
    onOrganicDisconnect: handleOrganicDisconnect,
    bleGateRef,
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
    bleGateRef,
    registeredMacs,
  });

  const scanner = useBLEScanner({
    bleManager,
    allDevices,
    setAllDevices,
    hwCache: sweeper.hwCache,
  });

  // Wire the real setter into the ref forwarder now that scanner is initialized.
  // Sweeper's Interrogator is always async (2s+ delay) so this is always populated in time.
  useEffect(() => {
    pendingRegistrationsSetterRef.current = scanner.setPendingRegistrations;
  }, [scanner.setPendingRegistrations]);


  // ─── Per-device MTU map ────────────────────────────────────────────────────
  // Each device may negotiate a different MTU. Using a single shared ref caused
  // the "last device wins" bug where payload chunking used the wrong MTU.
  const mtuMapRef = useRef<Map<string, number>>(new Map());
  const getDeviceMtu = (deviceId: string) => mtuMapRef.current.get(deviceId) ?? 186;

  const connectToDevices = async (devices: Device[]) => {
    if (devices.length === 0) return;

    // ── KEEPALIVE: Cancel any pending deferred disconnect ────────────────────
    // User is re-connecting (same or different group). Clear the keepalive timer
    // so the deferred disconnect doesn't fire mid-connection-handshake.
    if (keepaliveTimerRef.current) {
      clearTimeout(keepaliveTimerRef.current);
      keepaliveTimerRef.current = null;
      AppLogger.log('BLE_STATE_CHANGE', { event: 'keepalive_cancelled_reconnect' });
      // ── GROUP SWAP FIX: Flush stale GATT connections before connecting new group ──
      // Cancelling the timer does NOT disconnect the old group's devices —
      // they are still alive on the GATT stack. We must teardown first or the
      // new group's devices get appended on top, showing all devices simultaneously.
      if (connectedDevicesRef.current.length > 0) {
        await _executeRealDisconnect();
        // _executeRealDisconnect sets gate → DISCONNECTING → IDLE.
        // We must wait for IDLE before the gate check below allows CONNECTING.
      }
    }

    // ── CONNECTION GATE: Reject if another BLE operation is in-flight ────────
    if (bleGateRef.current !== 'IDLE') {
      AppLogger.warn('[BLE] connectToDevices REJECTED — gate is ' + bleGateRef.current, { requestedDevices: devices.map(d => d.id) });
      return;
    }
    setGate('CONNECTING');
    // Hoist outside try{} so it's visible in catch{} for Sweeper resume-on-failure
    const wasSweeperActive = sweeper.isSweeperActive;
    try {
      let isMock = 'false';
      if (__DEV__) {
         isMock = await AsyncStorage.getItem('@Sk8lytz_demo_mode') || 'false';
      }

      if (Platform.OS === 'web' || isMock === 'true') {
        setConnectedDevices(devices);
        devices.forEach(d => {
          AppLogger.log('DEVICE_CONNECTED', { id: d.id, name: d.name, firmware: 'v2.0.1.DEMO' });
        });
        
        if (dataReceivedCallbackRef.current) {
          setTimeout(() => {
             devices.forEach(d => {
               const mockPacket = [0x66, 0x14, 0x22, 0x01, 0x01, 0x33, 0x01, 0x55, 0x66, 0x99];
               dataReceivedCallbackRef.current!(d.id, mockPacket);
             });
          }, 1000);
        }
        setGate('IDLE');
        return;
      }
      // FIX: Android thoroughly forbids GATT connections during high-duty LE scans. Must stop before connect.
      scanner.stopScanner();
      // ── Overwatch: Also stop the Silent Sweeper during GATT connect phase ──────
      // The radio cannot actively scan AND perform GATT operations simultaneously on
      // Android without risking GATT 133. The Sweeper is resumed after connect completes.
      if (wasSweeperActive) sweeper.stopSweeper();

      // ── PARALLEL GROUP CONNECT (2-phase) ───────────────────────────────────
      // Phase 1 (serial): Acquire GATT connections one at a time.
      //   Android's BT stack serializes connectToDevice internally anyway,
      //   and firing them concurrently risks GATT 133 on congested stacks.
      // Phase 2 (parallel): Once all GATT channels are open, run the post-connect
      //   handshake (discoverServices → MTU → timeSync → notifyMonitor) for all
      //   devices simultaneously via Promise.all.
      //   For a 2-device group this cuts ~1.5s off open time (device 2's handshake
      //   overlaps with device 1's instead of waiting behind it).
      // ── Phase 1: Serial GATT acquisition ─────────────────────────────────────
      const rawConns: any[] = [];
      for (const device of devices) {
        let conn: any = null;
        let lastErr: any = null;
        for (let attempt = 1; attempt <= 2; attempt++) {
          try {
            const isConnected = await bleManager.isDeviceConnected(device.id);
            conn = isConnected ? device : await bleManager.connectToDevice(device.id);
            break;
          } catch (e: any) {
            lastErr = e;
            if (String(e).includes('133')) {
              AppLogger.warn(`[BLE] GATT 133 congestion linking ${device.id}. Attempt ${attempt}/2...`);
              await new Promise(resolve => setTimeout(resolve, 200));
            } else {
              break;
            }
          }
        }
        if (conn) {
          rawConns.push(conn);
        } else {
          AppLogger.error(`FAILED TO CONNECT TO INDIVIDUAL DEVICE ${device.id}`, lastErr);
          AppLogger.log('BLE_CONNECTION_ERROR', { error: lastErr?.message || String(lastErr), deviceId: device.id, context: 'group_sync_fail' });
        }
      }

      // ── Phase 2: Parallel post-connect handshake ──────────────────────────
      // Each device negotiates MTU, sets up notifications, and sends time sync
      // concurrently. Returns the device conn if successful, null if it failed.
      const handshakeDevice = async (conn: any): Promise<any | null> => {
        try {
          if (Platform.OS === 'android') {
            await bleManager.requestConnectionPriorityForDevice(conn.id, 1).catch(() => {});
          }
          await conn.discoverAllServicesAndCharacteristics();

          // MTU negotiation (up to 2 retries)
          let negotiatedMtu = 23;
          for (let mtuAttempt = 1; mtuAttempt <= 2; mtuAttempt++) {
            try {
              const negotiated = await conn.requestMTU(512);
              negotiatedMtu = negotiated.mtu;
              if (negotiatedMtu > 23) break;
              AppLogger.warn(`[BLE] MTU glitch (23) for ${conn.id}. Retrying...`);
              await new Promise(res => setTimeout(res, 200));
            } catch (e) {
              await new Promise(res => setTimeout(res, 200));
            }
          }
          mtuMapRef.current.set(conn.id, negotiatedMtu > 23 ? negotiatedMtu : 186);
          AppLogger.log('DEVICE_CONNECTED', {
            context: 'mtu_negotiated',
            mtu: mtuMapRef.current.get(conn.id),
            deviceId: conn.id,
          });

          // Disconnect listener + notification monitor
          if (disconnectListeners.current[conn.id]) disconnectListeners.current[conn.id].remove();
          disconnectListeners.current[conn.id] = bleManager.onDeviceDisconnected(conn.id, (error: any) => {
            handleOrganicDisconnect(error, conn.id);
          });
          conn.monitorCharacteristicForService(
            ZENGGE_SERVICE_UUID,
            ZENGGE_NOTIFY_UUID,
            (error: any, characteristic: any) => handleNotificationRef.current(error, characteristic, conn.id)
          );

          // Time sync — ZENGGE requires this before any other write
          try {
            const timeSyncPayload = ZenggeProtocol.setSessionTime();
            const b64TimeSync = Buffer.from(timeSyncPayload).toString('base64');
            await conn.writeCharacteristicWithoutResponseForService(
              ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64TimeSync
            );
            AppLogger.log('BLE_TIME_SYNC', { deviceId: conn.id, timestamp: Date.now() });
          } catch (timeSyncErr: any) {
            AppLogger.warn('[BLE] Time sync write failed (non-fatal)', { error: String(timeSyncErr), deviceId: conn.id });
          }

          AppLogger.log('DEVICE_CONNECTED', { id: conn.id, name: conn.name });
          return conn;
        } catch (deviceError: any) {
          const errMsg = deviceError?.message || String(deviceError);
          if (errMsg.includes('was disconnected') || errMsg.includes('is not connected') || errMsg.includes('not connected') || errMsg.includes('Device disconnected')) {
            AppLogger.warn(`[BLE] Connection dropout for ${conn.id} (ignoring VIP error)`);
          } else {
            AppLogger.error(`FAILED TO CONNECT TO INDIVIDUAL DEVICE ${conn.id}`, deviceError);
            AppLogger.log('BLE_CONNECTION_ERROR', { error: errMsg, deviceId: conn.id, context: 'group_sync_fail' });
          }
          return null;
        }
      };

      const handshakeResults = await Promise.all(rawConns.map(handshakeDevice));
      const connectedGroup = handshakeResults.filter(Boolean);

      // ── Single atomic state update with the fully-booted group ───────────────────
      // All GATT handshakes, MTU negotiations, and time syncs are complete for every
      // device that made it here. The UI transitions 0→N in one React commit.
      // REPLACE (not merge): all taps — group or single — replace the connected set.
      // The keepalive kept stale devices in state; additive merge caused ghost-device
      // flashes when the old group's devices appeared on top of the new connection.
      if (connectedGroup.length > 0) {
        setConnectedDevices(connectedGroup);

        // FIX: Restore last-sent pattern payload on reconnect.
        // If the user had a pattern active before disconnecting, replay it immediately
        // so skates light up without requiring user interaction.
        try {
          const groupId = connectedGroup[0]?.id?.toUpperCase() || 'default';
          const cached = await AsyncStorage.getItem(PATTERN_CACHE_KEY(groupId));
          if (cached) {
            const { payload: lastPayload, ts } = JSON.parse(cached);
            const ageMs = Date.now() - ts;
            if (Array.isArray(lastPayload) && ageMs < 3600000) { // 60 min TTL
              // Delay 300ms to let hardware settle post-connect before writing
              setTimeout(() => {
                if (connectedDevicesRef.current.some(d => d.id === connectedGroup[0]?.id)) {
                  writeToDevice(lastPayload);
                  AppLogger.log('BLE_STATE_CHANGE', { event: 'pattern_cache_restored', groupId, ageMs });
                }
              }, 300);
            }
          }
        } catch (cacheErr) {
          // Non-fatal — cache miss or parse error, user just starts fresh
        }
      }

      setGate('IDLE');
      // Resume Sweeper after successful connection
      if (wasSweeperActive && isBluetoothEnabled) sweeper.startSweeper();
    } catch (e: any) {
      const errMsg = e?.message || String(e);
      if (errMsg.includes('was disconnected') || errMsg.includes('is not connected') || errMsg.includes('not connected') || errMsg.includes('Device disconnected')) {
         AppLogger.warn(`[BLE] Group connection dropout (ignoring VIP error)`);
      } else {
         AppLogger.error('FAILED TO CONNECT TO GROUP', e);
         AppLogger.log('BLE_CONNECTION_ERROR', { error: errMsg, context: 'group' });
      }
      setGate('IDLE');
      // Resume Sweeper even after a connection failure
      if (wasSweeperActive && isBluetoothEnabled) sweeper.startSweeper();
    }
  };

  const writeToDevice = async (payload: number[], targetDeviceId?: string): Promise<boolean | 'partial'> => {
    const hexString = payload.map(x => x.toString(16).toUpperCase().padStart(2, '0')).join(' ');
    AppLogger.setLastTxPayload(hexString);

    // Web / no-op path: return true so optimisticWrite sees success
    if (connectedDevicesRef.current.length === 0 || Platform.OS === 'web') return true;

    const cmdByte = payload[0];
    // Critical writes (power 0x01, time sync 0x10, query 0x63) bypass debounce — fire immediately.
    // Pattern writes (0x59 spatial, 0x51 symphony) go through 100ms debounce to
    // prevent queue pile-up when user swipes rapidly through the pattern picker.
    const isPatternWrite = (cmdByte === 0x59 || cmdByte === 0x51 || cmdByte === 0x40);
    if (isPatternWrite) {
      const thisGeneration = ++writeGeneration;
      return new Promise((resolve) => {
        if (writeDebounceTimerRef.current) clearTimeout(writeDebounceTimerRef.current);
        writeDebounceTimerRef.current = setTimeout(async () => {
          writeDebounceTimerRef.current = null;
          // If a newer write queued up during the debounce window, drop this one.
          if (thisGeneration !== writeGeneration) { resolve(true); return; }
          const result = await _executeWriteToDevice(payload, targetDeviceId);
          resolve(result);
        }, 100);
      });
    }
    return _executeWriteToDevice(payload, targetDeviceId);
  };

  const _executeWriteToDevice = async (payload: number[], targetDeviceId?: string): Promise<boolean | 'partial'> => {
    // Capture generation at queue time. If a newer pattern write arrives while this
    // one waits in the mutex chain, the generation check inside executeWrite drops it.
    // capturedGeneration === 0 means it's a critical write — never get dropped.
    const cmdByte = payload[0];
    const isPatternWriteCmd = (cmdByte === 0x59 || cmdByte === 0x51 || cmdByte === 0x40);
    const capturedGeneration = isPatternWriteCmd ? writeGeneration : 0;

    const targets = targetDeviceId
      ? connectedDevicesRef.current.filter(d => d.id === targetDeviceId)
      : connectedDevicesRef.current;

    if (targets.length === 0 && targetDeviceId) {
      AppLogger.warn(`Target device ${targetDeviceId} not found in connected devices`);
      return false;
    }

    const maxSafeSize = targetDeviceId ? getDeviceMtu(targetDeviceId) - 3 : Math.min(...targets.map(d => getDeviceMtu(d.id))) - 3;
    
    if (payload.length > maxSafeSize) {
        const cmdId = payload[0];
        if (cmdId === 0x51 && payload.length > 200) {
          AppLogger.warn('[BLE] 0x51 Payload exceeds safe MTU. Routing to writeChunked automatically.', { length: payload.length });
          await writeChunked(payload, targetDeviceId);
          return true;
        }
      AppLogger.warn(`[BLE] PAYLOAD TOO LARGE: ${payload.length} bytes exceeds safe MTU window of ${maxSafeSize} bytes. Rejecting to prevent hardware lockup.`);
      return false;
    }

    const executeWrite = async (): Promise<boolean | 'partial'> => {
      // Write-queue cancellation: drop stale pattern writes that queued in the mutex chain.
      // If a newer pattern was selected while this one waited, silently no-op.
      // capturedGeneration === 0 means critical write — always fires.
      if (capturedGeneration !== 0 && capturedGeneration !== writeGeneration) {
        AppLogger.log('BLE_STATE_CHANGE', { event: 'write_stale_dropped', capturedGeneration, currentGeneration: writeGeneration });
        return true;
      }

      const liveTargets = targets.filter(device => {
        if (autoRecovery.ghostedDeviceIds.includes(device.id)) {
          AppLogger.warn(`[BLE] Write SKIPPED ghosted device ${device.id}`);
          return false;
        }
        return true;
      });
      
      const skippedGhosted = targets.length - liveTargets.length;
      if (liveTargets.length === 0) return skippedGhosted > 0 ? 'partial' : true;

      let allSucceeded = true;
      const base64Full = Buffer.from(payload).toString('base64');
      
      // Sending payload atomically in a single write packet without chunking or mid-transmission delays
      // This matches hardware constraint: each write packet is treated as an independent complete command
      for (const device of liveTargets) {
        try {
          await device.writeCharacteristicWithoutResponseForService(
            ZENGGE_SERVICE_UUID,
            ZENGGE_CHARACTERISTIC_UUID,
            base64Full
          );
        } catch (writeError: any) {
          AppLogger.warn(`[BLE] Write failed for ${device.id}`, writeError?.message);
          allSucceeded = false;
        }
      }

      if (skippedGhosted > 0 && allSucceeded) return 'partial';
      return allSucceeded;
    };

    const previousMutex = writeMutex;
    const currentWrite = (async () => {
      await previousMutex.catch(() => {});
      return executeWrite();
    })();
    
    writeMutex = currentWrite;
    return currentWrite;
  };

  /**
   * Send a large payload to BLE device using ZENGGE chunked framing (LowerTransportLayerEncoder).
   * Required for 0x51 Extended Scene Builder payloads (323 bytes total) and long 0x59 spatial arrays.
   *
   * Automatically calculates chunks based on the negotiated MTU (default 20 bytes data max for BLE 4.x).
   *
   * @param payload   Full raw payload bytes (e.g. 323-byte 0x51 extended packet)
   * @param targetDeviceId Optional. If provided, sends only to that device using its negotiated MTU.
   */
  const writeChunked = async (payload: number[], targetDeviceId?: string): Promise<void> => {
    if (connectedDevicesRef.current.length === 0 || Platform.OS === 'web') return;

    const targets = targetDeviceId
      ? connectedDevicesRef.current.filter(d => d.id === targetDeviceId)
      : connectedDevicesRef.current;

    if (targets.length === 0) return;

    const safeMtu = targetDeviceId ? getDeviceMtu(targetDeviceId) - 3 : Math.min(...targets.map(d => getDeviceMtu(d.id))) - 3;
    
    // Fallback if safeMtu is too small to even hold a header
    const chunkSize = Math.max(20, safeMtu); 

    const totalLen = payload.length;
    // Random session sequence byte — constant across all chunks of one transaction
    const seqByte = Math.floor(Math.random() * 256) & 0xFF;

    const chunks: number[][] = [];
    let offset = 0;
    let segmentIndex = 0;

    while (offset < totalLen) {
      const isFirstChunk = segmentIndex === 0;
      const headerSize = isFirstChunk ? 8 : 5;
      
      // Calculate how much data fits in this chunk based on negotiated MTU
      const maxDataLen = chunkSize - headerSize;
      const dataLen = Math.min(maxDataLen, totalLen - offset);
      const isLastChunk = (offset + dataLen) >= totalLen;

      // 0x40 Fragmentation Protocol (from ZENGGE_PROTOCOL_BIBLE.md)
      let indexWord = segmentIndex;
      if (isLastChunk) {
        indexWord |= 0x8000; // Terminator bit signals EOF to hardware
      }

      const chunk = [
        0x40,                          // [0] Control Byte
        seqByte,                       // [1] Sequence Counter
        indexWord & 0xFF,              // [2] Segment Index (Low)
        (indexWord >> 8) & 0xFF,       // [3] Segment Index (High)
      ];

      if (isFirstChunk) {
        chunk.push((totalLen >> 8) & 0xFF); // [4] Total Payload Length (High)
        chunk.push(totalLen & 0xFF);        // [5] Total Payload Length (Low)
        chunk.push(dataLen & 0xFF);         // [6] Payload length in THIS chunk
        chunk.push(0x0B);                   // [7] Command ID (0x0B for Control/Write)
      } else {
        chunk.push(dataLen & 0xFF);         // [4] Payload length in THIS chunk
      }

      // Append payload data for this chunk
      chunk.push(...payload.slice(offset, offset + dataLen));

      chunks.push(chunk);
      offset += dataLen;
      segmentIndex++;
    }

    AppLogger.log('BLE_CHUNKED_WRITE', { payloadLen: totalLen, numChunks: chunks.length, chunkSize });

    for (const chunk of chunks) {
      const b64 = Buffer.from(chunk).toString('base64');
      // FIX: Use writeWithoutResponse for all chunks — fire-and-forget eliminates the
      // per-chunk acknowledgment wait that caused 2-3 second pattern latency.
      // The 0x40 fragmentation protocol is self-describing; hardware reassembles without ACKs.
      await Promise.all(
        targets
          .filter(device => !autoRecovery.ghostedDeviceIds.includes(device.id))
          .map(device =>
            device.writeCharacteristicWithoutResponseForService(
              ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64
            ).catch((e: any) => {
              AppLogger.warn(`[BLE] writeChunked chunk failed for ${device.id}`, { error: String(e) });
            })
          )
      );
      // Minimal inter-chunk pacing — just enough for Android BLE stack to queue the next packet.
      // 8ms keeps throughput high while avoiding GATT congestion on older Android versions.
      await new Promise(resolve => setTimeout(resolve, 8));
    }
    // Give hardware 50ms to reassemble and execute before next command.
    await new Promise(resolve => setTimeout(resolve, 50));
  };


  // ── _executeRealDisconnect ────────────────────────────────────────────────
  // The actual GATT teardown. Called by:
  //   • keepalive timer expiry (after 60s of controller being closed)
  //   • forceDisconnect() (app backgrounded, group swap, unmount)
  // NOT called directly by disconnectFromDevice (which now defers via keepalive).
  const _executeRealDisconnect = async () => {
    if (bleGateRef.current === 'DISCONNECTING') return; // Already tearing down
    setGate('DISCONNECTING');
    await autoRecovery.cancelAllRecoveries();

    Object.values(disconnectListeners.current).forEach(sub => {
      try { sub.remove(); } catch (e) { AppLogger.warn('[BLE] Failed to remove disconnect listener', { error: String(e) }); }
    });
    disconnectListeners.current = {};

    const staleDevices = [...connectedDevicesRef.current];

    if (staleDevices.length > 0 && Platform.OS !== 'web') {
      const disconnectPromises = staleDevices.map(async (device) => {
        try {
          await bleManager.cancelDeviceConnection(device.id).catch((e: any) => AppLogger.warn(`[BLE] Disconnect soft fail for ${device.id}`, e));
        } catch (e: any) {
          AppLogger.error(`[BLE] Fatal disconnect fault for ${device.id}`, e);
        }
      });
      await Promise.all(disconnectPromises);
      await new Promise(resolve => setTimeout(resolve, 250));
    }

    // Clear per-device MTU cache
    mtuMapRef.current.clear();
    setConnectedDevices([]);
    setGate('IDLE');
    AppLogger.log('BLE_STATE_CHANGE', { event: 'keepalive_expired_disconnect' });
  };

  // ── disconnectFromDevice (keepalive-aware public API) ─────────────────────
  // Called by handleDisconnect in DashboardScreen when the controller closes.
  // Instead of immediate teardown, starts a 60s keepalive timer. The GATT
  // session stays alive so a re-open within 60s is instantaneous.
  const disconnectFromDevice = () => {
    if (keepaliveTimerRef.current) return; // Keepalive already pending — no-op
    AppLogger.log('BLE_STATE_CHANGE', { event: 'keepalive_started', durationMs: KEEPALIVE_DURATION_MS });
    keepaliveTimerRef.current = setTimeout(() => {
      keepaliveTimerRef.current = null;
      _executeRealDisconnect();
    }, KEEPALIVE_DURATION_MS);
    // bleGate intentionally stays IDLE; React state stays READY.
    // From the app's perspective, the group is still connected during the window.
  };

  // ── forceDisconnect (immediate teardown, bypasses keepalive) ─────────────
  // Used by: AppState background handler, component unmount cleanup.
  // Clears any pending keepalive timer and executes real disconnect immediately.
  const forceDisconnect = () => {
    if (keepaliveTimerRef.current) {
      clearTimeout(keepaliveTimerRef.current);
      keepaliveTimerRef.current = null;
      AppLogger.log('BLE_STATE_CHANGE', { event: 'keepalive_force_cancelled' });
    }
    _executeRealDisconnect();
  };

  // ── Keepalive cleanup on unmount ─────────────────────────────────────────
  useEffect(() => {
    return () => {
      if (keepaliveTimerRef.current) {
        clearTimeout(keepaliveTimerRef.current);
        keepaliveTimerRef.current = null;
      }
    };
  }, []);

  const derivedBleState: BleConnectionState = 
    bleGateState === 'DISCONNECTING' ? 'DISCONNECTING' :
    bleGateState === 'CONNECTING' ? 'CONNECTING' :
    scanner.scannerState === 'SCANNING' ? 'SCANNING' :
    scanner.scannerState === 'PROBING' ? 'PROBING' :
    connectedDevices.length > 0 ? 'READY' : 'IDLE';

  return useMemo(() => ({
    requestPermissions: async () => {
      const g = await checkPermission('BLUETOOTH');
      if (!g) {
        await openGlobalPermissionsModal();
        return await checkPermission('BLUETOOTH');
      }
      return true;
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
    ghostedDeviceIds: autoRecovery.ghostedDeviceIds,
    bleState: derivedBleState,
    bleGateRef,
    startSweeper: sweeper.startSweeper,
    stopSweeper: sweeper.stopSweeper,
    burstScan: sweeper.burstScan,
    isSweeperActive: sweeper.isSweeperActive,
    hwCache: sweeper.hwCache,
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
    scanner.scannerState,
    scanner.pendingRegistrations,
    isBluetoothSupported,
    isBluetoothEnabled,
    droppedOutDeviceIds,
    autoRecovery.ghostedDeviceIds,
    bleGateState,
    sweeper.isSweeperActive,
    sweeper.hwCache,
    sweeper.burstScan,
  ]);
}
