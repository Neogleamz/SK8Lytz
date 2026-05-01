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

export interface BluetoothLowEnergyApi {
  requestPermissions(): Promise<boolean>;
  scanForPeripherals(options?: { keepAlive?: boolean, disableProbing?: boolean }): void;
  connectToDevices: (devices: Device[]) => Promise<void>;
  disconnectFromDevice: () => void;
  writeToDevice: (payload: number[], targetDeviceId?: string) => Promise<boolean | 'partial'>;
  /** Send large payloads (>MTU) via sequential ZENGGE-framed BLE chunks. Required for 0x51 extended Scene Builder payloads. */
  writeChunked: (payload: number[], targetDeviceId?: string) => Promise<void>;
  probeDevice: (mac: string) => Promise<void>;
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

  const probeDevice = async (mac: string): Promise<any> => {
    if (Platform.OS === 'web' || !bleManager) return null;
    
    try {
      await bleManager.connectToDevice(mac, { timeout: 5000 }).catch((e: any) => {
         if (!String(e).includes('already')) throw e;
      });
      await bleManager.discoverAllServicesAndCharacteristicsForDevice(mac);
      
      const hwConfig = await new Promise<any>((resolve) => {
        let accumulatedTelemetry: any = null;

        const timer = setTimeout(() => {
          sub.remove();
          if (accumulatedTelemetry) {
             AppLogger.warn(`[BLE Probe Single] Partial telemetry for ${mac} — RF or HW missing. Returning partial.`);
             resolve(accumulatedTelemetry);
          } else {
             AppLogger.warn(`[BLE Probe Single] Probe empty timed out for ${mac} after 3500ms`);
             resolve(null);
          }
        }, 3500);

        const sub = bleManager.monitorCharacteristicForDevice(
          mac,
          ZENGGE_SERVICE_UUID,
          ZENGGE_NOTIFY_UUID,
          (err: any, char: any) => {
            if (err) {
              AppLogger.warn(`[BLE Probe Single] Monitor error for ${mac}`, { error: String(err) });
              return;
            }
            if (!char?.value) return;
            try {
              const raw = Array.from(Buffer.from(char.value, 'base64')) as number[];
              
              const hwParsed = ZenggeProtocol.parseHardwareSettingsResponse(raw);
              if (hwParsed) {
                 accumulatedTelemetry = { ...accumulatedTelemetry, ...hwParsed };
              }

              const rfParsed = ZenggeProtocol.parseRfRemoteState(raw);
              if (rfParsed) {
                 accumulatedTelemetry = { 
                   ...accumulatedTelemetry, 
                   rfMode: rfParsed.mode, 
                   rfPairedCount: rfParsed.pairedCount 
                 };
              }

              if (accumulatedTelemetry?.detected && accumulatedTelemetry?.rfMode) {
                clearTimeout(timer);
                sub.remove();
                AppLogger.log('DEVICE_DISCOVERED', { context: 'probe_success_full', deviceId: mac });
                resolve(accumulatedTelemetry);
              }
            } catch (e) { /* ignore */ }
          }
        );

        setTimeout(() => {
          // Fire HW Query
          const qpHW = ZenggeProtocol.queryHardwareSettings(false);
          const b64HW = Buffer.from(qpHW).toString('base64');
          bleManager.writeCharacteristicWithoutResponseForDevice(
            mac, ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64HW
          ).catch((e: any) => AppLogger.warn('[BLE Probe Single] hw query write failed', { error: String(e) }));

          // Fire RF Query 200ms later to avoid baseband collision
          setTimeout(() => {
             const qpRF = ZenggeProtocol.queryRfRemoteState();
             const b64RF = Buffer.from(qpRF).toString('base64');
             bleManager.writeCharacteristicWithoutResponseForDevice(
               mac, ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64RF
             ).catch((e: any) => AppLogger.warn('[BLE Probe Single] rf query write failed', { error: String(e) }));
          }, 200);

        }, 600);
      });

      return hwConfig;
    } catch (err: any) {
      AppLogger.warn(`[BLE Probe Single] Failed to probe ${mac}:`, { error: String(err) });
      return null;
    } finally {
      await bleManager.cancelDeviceConnection(mac).catch(() => {});
    }
  };

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

    // ── CONNECTION GATE: Reject if another BLE operation is in-flight ────────
    if (bleGateRef.current !== 'IDLE') {
      AppLogger.warn('[BLE] connectToDevices REJECTED — gate is ' + bleGateRef.current, { requestedDevices: devices.map(d => d.id) });
      return;
    }
    setGate('CONNECTING');
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

      // ── ATOMIC GROUP CONNECT: Stage all successful connections, update state ONCE ───
      // setConnectedDevices was previously called per-device inside the loop, causing the
      // UI to flash "1 connected" mid-loop while device 2 was still negotiating MTU.
      // Collecting into connectedGroup and calling state ONCE after the loop makes the
      // transition from 0→2 (or 0→N) devices atomic from React's perspective.
      const connectedGroup: any[] = [];

      for (const device of devices) {
        let conn: any = null;
        let lastErr: any = null;
        
        // FIX: The GATT 133 Retry Bumper
        for (let attempt = 1; attempt <= 2; attempt++) {
          try {
            const isConnected = await bleManager.isDeviceConnected(device.id);
            conn = isConnected 
              ? device 
              : await bleManager.connectToDevice(device.id);
            break; // Connection acquired
          } catch (e: any) {
            lastErr = e;
            if (String(e).includes('133')) {
              AppLogger.warn(`[BLE] GATT 133 congestion linking ${device.id}. Attempt ${attempt}/2...`);
              await new Promise(resolve => setTimeout(resolve, 200));
            } else {
              break; // Hard fault, don't retry
            }
          }
        }

        if (!conn) {
          AppLogger.error(`FAILED TO CONNECT TO INDIVIDUAL DEVICE ${device.id}`, lastErr);
          AppLogger.log('BLE_CONNECTION_ERROR', { error: lastErr?.message || String(lastErr), deviceId: device.id, context: 'group_sync_fail' });
          continue;
        }

        try {
          // FIX: Escalate Android connection interval to 'High' (~11.25ms) to beat RF interference
          if (Platform.OS === 'android') {
            await bleManager.requestConnectionPriorityForDevice(conn.id, 1).catch(() => {});
          }

          await conn.discoverAllServicesAndCharacteristics();

          try {
            let negotiatedMtu = 23;
            // Retry MTU negotiation up to 2 times if the Android stack botches it and returns 23
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
            if (negotiatedMtu > 23) {
              mtuMapRef.current.set(conn.id, negotiatedMtu);
              AppLogger.log('DEVICE_CONNECTED', { context: 'mtu_negotiated', mtu: negotiatedMtu, deviceId: conn.id });
            } else {
              AppLogger.warn(`[BLE] MTU stuck at 23 for ${conn.id}. Assuming Android GATT stack limit/glitch and forcing safe default 186.`);
              mtuMapRef.current.set(conn.id, 186);
            }
          } catch (mtuErr: any) {
            AppLogger.warn(`[BLE] MTU throw for ${conn.id}. Forcing default 186.`, mtuErr);
            mtuMapRef.current.set(conn.id, 186);
          }

          if (disconnectListeners.current[conn.id]) disconnectListeners.current[conn.id].remove();
          disconnectListeners.current[conn.id] = bleManager.onDeviceDisconnected(conn.id, (error: any) => {
            handleOrganicDisconnect(error, conn.id);
          });

          conn.monitorCharacteristicForService(
            ZENGGE_SERVICE_UUID,
            ZENGGE_NOTIFY_UUID,
            (error: any, characteristic: any) => handleNotificationRef.current(error, characteristic, conn.id)
          );

          AppLogger.log('DEVICE_CONNECTED', { id: conn.id, name: conn.name });

          // FIX: Send 0x10 session time sync immediately after GATT handshake.
          // ZENGGE app does this before any other write — hardware clock starts from
          // epoch 0 without it, causing timing-sensitive effects to drift.
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

          // Stage for atomic group state update after the loop completes.
          // (Previously: setConnectedDevices called here per-device — caused "1 of 2" flash)
          connectedGroup.push(conn);

        } catch (deviceError: any) {
          const errMsg = deviceError?.message || String(deviceError);
          if (errMsg.includes('was disconnected') || errMsg.includes('is not connected') || errMsg.includes('not connected') || errMsg.includes('Device disconnected')) {
             AppLogger.warn(`[BLE] Connection dropout for ${device.id} (ignoring VIP error)`);
          } else {
             AppLogger.error(`FAILED TO CONNECT TO INDIVIDUAL DEVICE ${device.id}`, deviceError);
             AppLogger.log('BLE_CONNECTION_ERROR', { error: errMsg, deviceId: device.id, context: 'group_sync_fail' });
          }
        }
      }

      // ── Single atomic state update with the fully-booted group ───────────────────
      // All GATT handshakes, MTU negotiations, and time syncs are complete for every
      // device that made it here. The UI transitions 0→N in one React commit.
      if (connectedGroup.length > 0) {
        setConnectedDevices(prev => {
          const merged = [...prev];
          for (const c of connectedGroup) {
            if (!merged.find(x => x.id === c.id)) merged.push(c);
          }
          return merged;
        });
      }

      setGate('IDLE');
    } catch (e: any) {
      const errMsg = e?.message || String(e);
      if (errMsg.includes('was disconnected') || errMsg.includes('is not connected') || errMsg.includes('not connected') || errMsg.includes('Device disconnected')) {
         AppLogger.warn(`[BLE] Group connection dropout (ignoring VIP error)`);
      } else {
         AppLogger.error('FAILED TO CONNECT TO GROUP', e);
         AppLogger.log('BLE_CONNECTION_ERROR', { error: errMsg, context: 'group' });
      }
      setGate('IDLE');
    }
  };

  const writeToDevice = async (payload: number[], targetDeviceId?: string): Promise<boolean | 'partial'> => {
    const hexString = payload.map(x => x.toString(16).toUpperCase().padStart(2, '0')).join(' ');
    AppLogger.setLastTxPayload(hexString);

    // Web / no-op path: return true so optimisticWrite sees success
    if (connectedDevicesRef.current.length === 0 || Platform.OS === 'web') return true;

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

    while (offset < totalLen) {
      // ZENGGE 0x40 chunk header is always 8 bytes:
      //   [0x40, seqByte, offset_lo, offset_hi, 0x01, 0x43, 0xBD, 0x0B, ...data]
      //
      // Source: Live BLE HCI sniff of official ZENGGE app (2026-04-22).
      // Bytes [4-6] = ZENGGE proprietary magic — NOT payload length.
      // Bytes [2-3] = byte offset into full payload, little-endian, for hardware reassembly.
      const HEADER_SIZE = 8;
      const dataLen = Math.min(chunkSize - HEADER_SIZE, totalLen - offset);

      const chunk = [
        0x40,                    // ZENGGE chunked frame marker
        seqByte,                 // Session sequence ID (random per transaction)
        offset & 0xFF,           // offset_lo (little-endian byte offset into full payload)
        (offset >> 8) & 0xFF,    // offset_hi
        0x01,                    // ZENGGE magic byte 1 — BLE sniff verified, DO NOT CHANGE
        0x43,                    // ZENGGE magic byte 2 — BLE sniff verified, DO NOT CHANGE
        0xBD,                    // ZENGGE magic byte 3 — BLE sniff verified, DO NOT CHANGE
        0x0B,                    // Command write ID    — BLE sniff verified, DO NOT CHANGE
        ...payload.slice(offset, offset + dataLen),
      ];

      chunks.push(chunk);
      offset += dataLen;
    }

    AppLogger.log('BLE_CHUNKED_WRITE', { payloadLen: totalLen, numChunks: chunks.length, chunkSize });

    for (const chunk of chunks) {
      const b64 = Buffer.from(chunk).toString('base64');
      for (const device of targets) {
        if (autoRecovery.ghostedDeviceIds.includes(device.id)) continue;
        try {
          await device.writeCharacteristicWithoutResponseForService(
            ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64
          );
        } catch (e: any) {
          AppLogger.warn(`[BLE] writeChunked chunk failed for ${device.id}`, { error: String(e) });
        }
      }
      // Inter-chunk delay to prevent BLE TX buffer overflow
      await new Promise(resolve => setTimeout(resolve, Platform.OS === 'ios' ? 30 : 20));
    }
  };


  const disconnectFromDevice = async () => {
    // ── CONNECTION GATE: Acquire DISCONNECTING phase ─────────────────────────
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
  };

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
    scanForPeripherals: scanner.scanForPeripherals,
    connectToDevices,
    writeToDevice,
    writeChunked,
    allDevices,
    setAllDevices,
    connectedDevices,
    disconnectFromDevice,
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
    probeDevice,
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
