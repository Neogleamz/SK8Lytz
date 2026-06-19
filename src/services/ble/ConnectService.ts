import { fromPromise } from 'xstate';
import { Platform, Alert } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { STORAGE_DEMO_MODE } from '../../constants/storageKeys';
import { Buffer } from 'buffer';
import type { Device, BleManager, BleError, Characteristic } from 'react-native-ble-plx';
import { createGattSession } from '../BleSessionFactory';
import { AppLogger } from '../appLogger';
import { scrubPII } from '../../utils/piiScrubber';
import { jitteredDelay } from '../../utils/backoff';
import { BLE_TIMING } from '../../constants/bleTimingConstants';
import type { IControllerProtocol } from '../../protocols/IControllerProtocol';
import { WritePriority, enqueueDelay } from '../BleWriteQueue';

export interface ConnectServiceInput {
  bleManager: BleManager;
  targetMacs: string[];
  connectedDevicesRef: { current: Device[] };
  adapterMapRef: { current: Map<string, IControllerProtocol> };
  mtuMapRef: { current: Map<string, number> };
  disconnectListeners: { current: Record<string, import('react-native-ble-plx').Subscription> };
  /** H2 fix: notification monitor subscriptions keyed by device ID for cleanup on reconnect */
  notificationListeners: { current: Record<string, import('react-native-ble-plx').Subscription> };
  blacklistedMacsRef: { current: string[] };
  /**
   * handleOrganicDisconnect — legacy logging-only callback. Kept for telemetry.
   * Does NOT trigger recovery. Use onOrganicDisconnect for recovery.
   */
  handleOrganicDisconnect: (error: BleError | null, deviceId: string) => void;
  /**
   * onOrganicDisconnect — fires when BLE stack signals an unexpected device
   * disconnect. Caller (useBLE.ts) wires this to send RECOVERY_START to the
   * XState BleMachine. This is the correct recovery trigger.
   */
  onOrganicDisconnect: (deviceId: string) => void;
  handleNotification: (error: BleError | null, characteristic: Characteristic | null, deviceId: string) => void;
  enqueueWrite: (
    priority: WritePriority,
    op: () => Promise<boolean | 'partial'>,
    generation?: number
  ) => Promise<boolean | 'partial'>;
}

export const connectService = fromPromise<
  { devices: Device[] },
  ConnectServiceInput
>(async ({ input, signal }) => {
  const {
    bleManager,
    targetMacs,
    connectedDevicesRef,
    adapterMapRef,
    mtuMapRef,
    disconnectListeners,
    notificationListeners,
    blacklistedMacsRef,
    handleOrganicDisconnect,
    onOrganicDisconnect,
    handleNotification,
    enqueueWrite,
  } = input;

  if (targetMacs.length === 0) return { devices: [] };

  // ── HARDWARE BLACKLIST GUARD ──────────────────────────────────────────────
  const blockedMacs = targetMacs.filter(mac => blacklistedMacsRef.current.includes(mac.toUpperCase()));
  if (blockedMacs.length > 0) {
    AppLogger.warn('[BLE] Hardware Blacklist Blocked Connection', { blockedDevices: blockedMacs.map(scrubPII).join(', '), payload_size: 0, ssi: 0 });
    Alert.alert('Connection Blocked', 'One or more devices have been restricted and cannot be connected.');
    throw new Error('hardware_blacklist');
  }

  try {
    if (signal.aborted) throw new Error('connect_aborted');

    const allRequestedAlreadyConnected = targetMacs.every(requestedMac => 
      connectedDevicesRef.current.some(connected => connected.id === requestedMac)
    );

    // Cache hit — all requested devices are already connected, no work needed.
    // Return the FULL connected list (not just requested) to preserve group integrity.
    if (allRequestedAlreadyConnected) {
      AppLogger.log('BLE_STATE_CHANGE', { event: 'connectToDevices_cached_hit_skip' });
      return { devices: connectedDevicesRef.current };
    }

    // Retain ALL currently connected devices during incremental group assembly.
    // Stale flush was removed because it killed Device A when Device B arrived
    // in a separate auto-connect batch (A was not in B's targetMacs).
    // Fleet switching is handled upstream by DISCONNECT_REQUEST → DISCONNECTING
    // in BleMachine.ts, not here. See PLAN-fix-stale-flush-group-kill.md.
    const retainedDevices = [...connectedDevicesRef.current];


    if (signal.aborted) throw new Error('connect_aborted');

    let isMock = 'false';
    if ((typeof __DEV__ !== 'undefined' && __DEV__) || Platform.OS === 'web') {
       try {
         isMock = await AsyncStorage.getItem(STORAGE_DEMO_MODE).catch(() => 'false') || 'false';
       } catch (e: unknown) {
         AppLogger.warn('Failed to read mock storage', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
       }
    }

    if (Platform.OS === 'web' || isMock === 'true') {
      // Mock flow
      const mockDevices = targetMacs.map(mac => {
        const d: Partial<Device> = {
          id: mac,
          name: 'MOCK_DEVICE',
          rssi: -50,
        };
        return d as Device;
      });
      
      mockDevices.forEach(d => {
        AppLogger.log('DEVICE_CONNECTED', { id: d.id, name: scrubPII(d.name || ''), firmware: 'v2.0.1.DEMO' });
      });
      return { devices: mockDevices };
    }

    const rawConns: Device[] = [];
    for (const mac of targetMacs) {
      if (signal.aborted) throw new Error('connect_aborted');
      
      if (retainedDevices.some(r => r.id === mac)) {
        continue;
      }

      let conn: Device | null = null;
      let lastErr: Error | null = null;
      
      for (let attempt = 1; attempt <= 3; attempt++) {
        if (signal.aborted) throw new Error('connect_aborted');
        try {
          const isConnected = await bleManager.isDeviceConnected(mac);
          if (isConnected) {
            let devicesList = await bleManager.connectedDevices([]).catch(() => []);
            conn = devicesList.find(d => d.id === mac) || null;

            // M5 FIX: Android 12+ race — isDeviceConnected=true but connectedDevices list hasn't caught up.
            // Retry once after a short settle if the device isn't found.
            if (!conn) {
              await new Promise(r => setTimeout(r, 200));
              devicesList = await bleManager.connectedDevices([]).catch(() => []);
              conn = devicesList.find(d => d.id === mac) || null;
            }
          } else {
            conn = null;
          }
          
          if (!conn || !conn.id) {
             conn = await bleManager.connectToDevice(
              mac,
              attempt > 1 ? { refreshGatt: 'OnConnected' } : undefined,
            );
          }
          break;
        } catch (e: unknown) {
          lastErr = e instanceof Error ? e : new Error(String(e));
          const errStr = String(e);
          const isTransient = errStr.includes('133') || errStr.includes('133 (0x85)')
            || errStr.includes('connection failed') || errStr.includes('timed out')
            || errStr.includes('Peer removed');
            
          if (isTransient && attempt < 3) {
            const baseDelay = BLE_TIMING.GATT_CONNECT_BACKOFF_MS[attempt - 1];
            const delay = jitteredDelay(baseDelay, 500);
            AppLogger.warn(`[BLE] GATT 133 — transient connect error, retrying`, { attempt, maxAttempts: 3, delayMs: delay, deviceId: scrubPII(mac), error: errStr, payload_size: 0, ssi: 0 });
            await bleManager.cancelDeviceConnection(mac).catch((e: unknown) => {
              AppLogger.warn('[BLE] cancelDeviceConnection failed during transient retry', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
            });
            await new Promise(resolve => setTimeout(resolve, delay));
          } else {
            break;
          }
        }
      }
      if (conn && conn.id) {
        rawConns.push(conn);
      } else {
        AppLogger.error('FAILED TO CONNECT TO INDIVIDUAL DEVICE', lastErr, { deviceId: scrubPII(mac), payload_size: 0, ssi: 0 });
        AppLogger.log('BLE_CONNECTION_ERROR', { error: lastErr?.message || String(lastErr), deviceId: scrubPII(mac), context: 'group_sync_fail' });
      }
    }

    const handshakeDevice = async (conn: Device): Promise<Device | null> => {
      try {
        if (signal.aborted) throw new Error('connect_aborted');
        if (Platform.OS === 'android') {
          await bleManager.requestConnectionPriorityForDevice(conn.id, 1).catch((e: unknown) => {
            AppLogger.warn('[BLE] requestConnectionPriorityForDevice failed', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
          });
        }
        const { adapter } = await createGattSession(bleManager, conn.id, {
          timeout: 6000,
          retries: 1,
          context: 'handshakeDevice',
          manufacturerData: conn.manufacturerData ?? undefined,
          signal,
        });

        if (Platform.OS === 'android') {
          let negotiatedMtu = 23;
          for (let mtuAttempt = 1; mtuAttempt <= 2; mtuAttempt++) {
            if (signal.aborted) throw new Error('connect_aborted');
            try {
              const negotiated = await conn.requestMTU(512);
              negotiatedMtu = negotiated.mtu;
              if (negotiatedMtu > 23) break;
              AppLogger.warn(`[BLE] MTU glitch (23) for ${scrubPII(conn.id)}. Retrying...`, { payload_size: 0, ssi: 0 });
              const backoffMs = BLE_TIMING.MTU_RETRY_SETTLE_MS * Math.pow(2, mtuAttempt - 1);
              await new Promise(res => setTimeout(res, jitteredDelay(backoffMs, 50)));
            } catch (mtuErr: unknown) {
              AppLogger.warn('[ConnectService] MTU negotiation attempt failed', {
                error: mtuErr instanceof Error ? mtuErr.message : String(mtuErr),
                deviceId: scrubPII(conn.id),
                attempt: mtuAttempt,
                payload_size: 0,
                ssi: 0,
              });
              const backoffMs = BLE_TIMING.MTU_RETRY_SETTLE_MS * Math.pow(2, mtuAttempt - 1);
              await new Promise(res => setTimeout(res, jitteredDelay(backoffMs, 50)));
            }
          }
          mtuMapRef.current.set(conn.id, negotiatedMtu > 23 ? negotiatedMtu : 186);
        } else {
          mtuMapRef.current.set(conn.id, conn.mtu > 23 ? conn.mtu : 186);
        }
        
        AppLogger.log('DEVICE_CONNECTED', {
          context: 'mtu_negotiated',
          mtu: mtuMapRef.current.get(conn.id),
          deviceId: scrubPII(conn.id),
        });

        adapterMapRef.current.set(conn.id, adapter);
        AppLogger.log('DEVICE_CONNECTED', { context: 'adapter_resolved', deviceId: scrubPII(conn.id), protocolId: scrubPII(adapter.protocolId) });

        if (disconnectListeners.current[conn.id]) disconnectListeners.current[conn.id].remove();
        disconnectListeners.current[conn.id] = bleManager.onDeviceDisconnected(conn.id, (error: BleError | null, _device: Device | null) => {
          // 1. Log + telemetry (non-recovery)
          handleOrganicDisconnect(error, conn.id);
          // 2. Trigger XState RECOVERY_START (the actual recovery mechanism)
          onOrganicDisconnect(conn.id);
        });
        // H2 FIX: Clean up stale notification subscription before re-registering
        if (notificationListeners.current[conn.id]) {
          notificationListeners.current[conn.id].remove();
          delete notificationListeners.current[conn.id];
        }
        notificationListeners.current[conn.id] = conn.monitorCharacteristicForService(
          adapter.serviceUUID,
          adapter.notifyCharacteristicUUID,
          (error: BleError | null, characteristic: Characteristic | null) => handleNotification(error, characteristic, conn.id)
        );

        try {
          const handshake = adapter.getHandshakePayloads();
          for (let i = 0; i < handshake.packets.length; i++) {
            if (signal.aborted) throw new Error('connect_aborted');
            const b64 = Buffer.from(handshake.packets[i]).toString('base64');
            const writeOp = async () => {
              await conn.writeCharacteristicWithoutResponseForService(
                adapter.serviceUUID, adapter.writeCharacteristicUUID, b64
              );
              return true;
            };
            
            await enqueueWrite('critical', writeOp);
            
            if (i < handshake.packets.length - 1 && handshake.interPacketDelayMs > 0) {
              await enqueueDelay('critical', handshake.interPacketDelayMs);
            }
          }
          if (handshake.packets.length > 0) {
            AppLogger.log('BLE_TIME_SYNC', { deviceId: scrubPII(conn.id), protocolId: scrubPII(adapter.protocolId), timestamp: Date.now() });
          }
        } catch (handshakeErr: unknown) {
          AppLogger.warn('[BLE] Handshake write failed (non-fatal)', { error: handshakeErr instanceof Error ? handshakeErr.message : String(handshakeErr), deviceId: scrubPII(conn.id), payload_size: 0, ssi: 0 });
        }

        AppLogger.log('DEVICE_CONNECTED', { id: scrubPII(conn.id), name: scrubPII(conn.name ?? '') });
        if (Platform.OS === 'android') {
          bleManager.requestConnectionPriorityForDevice(conn.id, 0).catch((e: unknown) => {
            AppLogger.warn('[BLE] Priority BALANCED downgrade failed (non-fatal)', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
          });
        }
        return conn;
      } catch (deviceError: unknown) {
        const errMsg = deviceError instanceof Error ? deviceError.message : String(deviceError);
        if (errMsg.includes('was disconnected') || errMsg.includes('is not connected') || errMsg.includes('not connected') || errMsg.includes('Device disconnected')) {
          AppLogger.warn(`[BLE] Connection dropout for ${scrubPII(conn.id)} (ignoring VIP error)`, { payload_size: 0, ssi: 0 });
        } else {
          AppLogger.error(`FAILED TO CONNECT TO INDIVIDUAL DEVICE ${scrubPII(conn.id)}`, deviceError instanceof Error ? deviceError.message : String(deviceError), { deviceId: scrubPII(conn.id), payload_size: 0, ssi: 0 });
          AppLogger.log('BLE_CONNECTION_ERROR', { error: errMsg, deviceId: scrubPII(conn.id), context: 'group_sync_fail' });
        }
        return null;
      }
    };

    const handshakeResults: (Device | null)[] = [];
    for (const conn of rawConns) {
      if (signal.aborted) throw new Error('connect_aborted');
      handshakeResults.push(await handshakeDevice(conn));
    }
    
    const freshlyConnected = handshakeResults.filter((c): c is Device => c !== null);
    
    // Combine retained devices + freshly connected devices
    const finalGroup = [...retainedDevices];
    for (const c of freshlyConnected) {
      if (!finalGroup.find(x => x.id === c.id)) finalGroup.push(c);
    }
    
    if (finalGroup.length === 0 && targetMacs.length > 0) {
       // Everything failed
       throw new Error('all_connections_failed');
    }

    return { devices: finalGroup };

  } catch (outerErr: unknown) {
    AppLogger.error('[BLE] connectService outer failed', outerErr instanceof Error ? outerErr.message : String(outerErr), { payload_size: 0, ssi: 0 });
    throw outerErr;
  }
});
