import { Platform, Alert } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { STORAGE_DEMO_MODE } from '../constants/storageKeys';
import { Buffer } from 'buffer';
import type { Device } from 'react-native-ble-plx';
import { AppLogger } from './AppLogger';
import { createGattSession } from './BleSessionFactory';
import { scrubPII } from '../utils/piiScrubber';
import { acquireGattLock } from '../hooks/ble/useBLEGattMutex';
import type { BleConnectionRequest } from '../types/ble.types';
import { jitteredDelay } from '../utils/backoff';
import { BLE_TIMING } from '../constants/bleTimingConstants';

/**
 * executeConnectToDevices — Group connection manager.
 * Serializes connection acquisition to protect Android stack, then parallelizes
 * MTU and Time-Sync handshakes for sub-second boot times.
 */
export async function executeConnectToDevices({
  devices,
  bleManager,
  connectedDevicesRef,
  blacklistedMacsRef,
  keepaliveTimerRef,
  disconnectListeners,
  scanner,
  autoRecovery: _autoRecovery,
  getGate,
  mtuMapRef,
  adapterMapRef,
  dataReceivedCallbackRef,
  handleNotificationRef,
  handleOrganicDisconnect,
  setConnectedDevices,
  setGate,
  enqueueWrite,
  bleSend,
}: BleConnectionRequest): Promise<void> {
  if (devices.length === 0) return;

  // ── HARDWARE BLACKLIST GUARD ──────────────────────────────────────────────
  const blockedDevices = devices.filter(d => blacklistedMacsRef.current.includes(d.id.toUpperCase()));
  if (blockedDevices.length > 0) {
    AppLogger.warn('[BLE] Hardware Blacklist Blocked Connection', { blockedDevices: blockedDevices.map(d => d.id) });
    Alert.alert('Connection Blocked', 'One or more devices have been restricted and cannot be connected.');
    return;
  }

  // ── CONNECTION CACHING GUARD (Optimistic UI) ──────────────────────────────
  // Moved inside the lock below to prevent TOCTOU race conditions.

  // ── PRE-LOCK GATE CHECK (optimization) ─────────────────────────────────────
  // Skip the entire GATT lock acquisition if the gate is already non-IDLE.
  // Saves the 8s poll cycle that acquireGattLock(1) would spend waiting.
  // The post-lock gate check at L97 remains as a TOCTOU safety net.
  if (getGate() !== 'IDLE') {
    AppLogger.warn('[BLE] connectToDevices SKIPPED (pre-lock) — gate is ' + getGate(), { requestedDevices: devices.map(d => d.id) });
    return;
  }

  // ── GATT LOCK ACQUISITION: Priority 1 (User Action) ──
  const lockHandle = await acquireGattLock(1);
  if (!lockHandle) {
    AppLogger.warn('[BLE] connectToDevices REJECTED — could not acquire GATT lock', { requestedDevices: devices.map(d => d.id) });
    return;
  }
  const { release } = lockHandle;

  try {
    const allRequestedAlreadyConnected = devices.every(requested => 
      connectedDevicesRef.current.some(connected => connected.id === requested.id)
    );

    if (allRequestedAlreadyConnected) {
      AppLogger.log('BLE_STATE_CHANGE', { event: 'connectToDevices_cached_hit_skip' });
      if (keepaliveTimerRef.current) {
        clearTimeout(keepaliveTimerRef.current);
        keepaliveTimerRef.current = null;
      }
      return;
    }

    const retainedDevices = connectedDevicesRef.current.filter(c => devices.some(d => d.id === c.id));
    setConnectedDevices(retainedDevices);

    if (keepaliveTimerRef.current) {
      clearTimeout(keepaliveTimerRef.current);
      keepaliveTimerRef.current = null;
      AppLogger.log('BLE_STATE_CHANGE', { event: 'keepalive_cancelled_reconnect' });
    }

    const staleDevices = connectedDevicesRef.current.filter(c => !devices.some(d => d.id === c.id));
    if (staleDevices.length > 0) {
      for (const stale of staleDevices) {
        if (disconnectListeners.current[stale.id]) {
           try {
             disconnectListeners.current[stale.id].remove();
           } catch (e: unknown) {
             AppLogger.warn('[BLE] Failed to remove disconnect listener during stale device flush', e instanceof Error ? e.message : String(e));
           }
           delete disconnectListeners.current[stale.id];
        }
        try {
          await bleManager.cancelDeviceConnection(stale.id);
          AppLogger.log('BLE_STATE_CHANGE', { event: 'stale_device_flushed', deviceId: scrubPII(stale.id) });
        } catch (e: unknown) {
          AppLogger.warn('Failed to flush stale device', { deviceId: scrubPII(stale.id), error: e instanceof Error ? e.message : String(e)  });
        }
      }
      await new Promise(resolve => setTimeout(resolve, BLE_TIMING.STALE_FLUSH_SETTLE_MS));
    }

    if (getGate() !== 'IDLE') {
      AppLogger.warn('[BLE] connectToDevices REJECTED — gate is ' + getGate(), { requestedDevices: devices.map(d => d.id) });
      return;
    }
    setGate('CONNECTING');
    const wasSweeperActive = scanner.isSweeperActive;
    try {
      let isMock = 'false';
      if (__DEV__) {
         isMock = await AsyncStorage.getItem(STORAGE_DEMO_MODE) || 'false';
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
      scanner.stopScanner();
      // Sweeper is handled by scanner.stopScanner()

      const rawConns: Device[] = [];
      for (const device of devices) {
        if (retainedDevices.some(r => r.id === device.id)) {
          continue;
        }

        let conn: Device | null = null;
        let lastErr: Error | null = null;
        // Exponential backoff delays for GATT 133 recovery.
        // Attempt 2: 500ms + refreshGatt (clears stale GATT cache via BluetoothGatt.refresh())
        // Attempt 3: 1500ms + refreshGatt (gives Android BT controller time to fully reset)
        // Attempt 4: 4000ms + refreshGatt (last resort — controller may need a full reset cycle)
        const GATT_BACKOFF_MS = [500, 1500, 4000] as const;
        for (let attempt = 1; attempt <= 3; attempt++) {
          try {
            const isConnected = await bleManager.isDeviceConnected(device.id);
            conn = isConnected ? device : await bleManager.connectToDevice(
              device.id,
              attempt > 1 ? { refreshGatt: 'OnConnected' } : undefined,
            );
            break;
          } catch (e: unknown) {
            lastErr = e instanceof Error ? e : new Error(String(e));
            const errStr = String(e);
            const isTransient = errStr.includes('133') || errStr.includes('133 (0x85)')
              || errStr.includes('connection failed') || errStr.includes('timed out')
              || errStr.includes('Peer removed');
            if (isTransient && attempt < 3) {
              const baseDelay = GATT_BACKOFF_MS[attempt - 1];
              const delay = jitteredDelay(baseDelay, 500);
              AppLogger.warn(`[BLE] GATT 133 on ${device.id}. Attempt ${attempt}/3 — retrying in ${delay}ms with refreshGatt`, { error: errStr });
              await bleManager.cancelDeviceConnection(device.id).catch(() => {});
              await new Promise(resolve => setTimeout(resolve, delay));
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

      const handshakeDevice = async (conn: Device): Promise<Device | null> => {
        try {
          if (Platform.OS === 'android') {
            await bleManager.requestConnectionPriorityForDevice(conn.id, 1).catch((e: unknown) => {
              AppLogger.warn('[BLE] requestConnectionPriorityForDevice failed', e instanceof Error ? e.message : String(e));
            });
          }
          const { adapter } = await createGattSession(bleManager, conn.id, {
            timeout: 6000,
            retries: 1,
            context: 'handshakeDevice',
            manufacturerData: conn.manufacturerData ?? undefined,
          });

          // MTU negotiation: Android requests explicitly; iOS Core Bluetooth auto-negotiates.
          // requestMTU() is a documented no-op on iOS (react-native-ble-plx v2) — reading
          // conn.mtu directly gives the actual auto-negotiated value without a wasted call.
          if (Platform.OS === 'android') {
            let negotiatedMtu = 23;
            for (let mtuAttempt = 1; mtuAttempt <= 2; mtuAttempt++) {
              try {
                const negotiated = await conn.requestMTU(512);
                negotiatedMtu = negotiated.mtu;
                if (negotiatedMtu > 23) break;
                AppLogger.warn(`[BLE] MTU glitch (23) for ${conn.id}. Retrying...`);
                await new Promise(res => setTimeout(res, BLE_TIMING.MTU_RETRY_SETTLE_MS));
              } catch {
                await new Promise(res => setTimeout(res, BLE_TIMING.MTU_RETRY_SETTLE_MS));
              }
            }
            mtuMapRef.current.set(conn.id, negotiatedMtu > 23 ? negotiatedMtu : 186);
          } else {
            // iOS: read Core Bluetooth\u2019s auto-negotiated MTU from conn.mtu.
            // Typed as `number` in ble-plx Device \u2014 no cast required.
            mtuMapRef.current.set(conn.id, conn.mtu > 23 ? conn.mtu : 186);
          }
          AppLogger.log('DEVICE_CONNECTED', {
            context: 'mtu_negotiated',
            mtu: mtuMapRef.current.get(conn.id),
            deviceId: conn.id,
          });

          adapterMapRef.current.set(conn.id, adapter);
          AppLogger.log('DEVICE_CONNECTED', { context: 'adapter_resolved', deviceId: conn.id, protocolId: adapter.protocolId });

          if (disconnectListeners.current[conn.id]) disconnectListeners.current[conn.id].remove();
          disconnectListeners.current[conn.id] = bleManager.onDeviceDisconnected(conn.id, (error: any) => {
            handleOrganicDisconnect(error, conn.id);
          });
          conn.monitorCharacteristicForService(
            adapter.serviceUUID,
            adapter.notifyCharacteristicUUID,
            (error: any, characteristic: any) => handleNotificationRef.current(error, characteristic, conn.id)
          );

          try {
            const handshake = adapter.getHandshakePayloads();
            for (let i = 0; i < handshake.packets.length; i++) {
              const b64 = Buffer.from(handshake.packets[i]).toString('base64');
              const writeOp = async () => {
                await conn.writeCharacteristicWithoutResponseForService(
                  adapter.serviceUUID, adapter.writeCharacteristicUUID, b64
                );
                return true;
              };
              
              if (!enqueueWrite) throw new Error('[BLE] enqueueWrite not provided to connection request');
              await enqueueWrite('critical', writeOp);
              
              if (i < handshake.packets.length - 1 && handshake.interPacketDelayMs > 0) {
                await new Promise(res => setTimeout(res, handshake.interPacketDelayMs));
              }
            }
            if (handshake.packets.length > 0) {
              AppLogger.log('BLE_TIME_SYNC', { deviceId: conn.id, protocolId: adapter.protocolId, timestamp: Date.now() });
            }
          } catch (handshakeErr: unknown) {
            AppLogger.warn('[BLE] Handshake write failed (non-fatal)', { error: handshakeErr instanceof Error ? handshakeErr.message : String(handshakeErr), deviceId: conn.id  });
          }

          AppLogger.log('DEVICE_CONNECTED', { id: conn.id, name: conn.name });
          // Downgrade HIGH \u2192 BALANCED after all handshake writes are complete.
          // HIGH (11.25ms poll interval) is 2\u20133\u00d7 battery drain vs BALANCED (30ms).
          // Fire-and-forget BLE writes don\u2019t need HIGH after initial setup.
          // Apple HomeKit mandates BALANCED within 5s of connection establishment.
          if (Platform.OS === 'android') {
            bleManager.requestConnectionPriorityForDevice(conn.id, 0).catch((e: unknown) => {
              AppLogger.warn('[BLE] Priority BALANCED downgrade failed (non-fatal)', e instanceof Error ? e.message : String(e));
            });
          }
          return conn;
        } catch (deviceError: unknown) {
          const errMsg = deviceError instanceof Error ? deviceError.message : String(deviceError);
          if (errMsg.includes('was disconnected') || errMsg.includes('is not connected') || errMsg.includes('not connected') || errMsg.includes('Device disconnected')) {
            AppLogger.warn(`[BLE] Connection dropout for ${conn.id} (ignoring VIP error)`);
          } else {
            AppLogger.error(`FAILED TO CONNECT TO INDIVIDUAL DEVICE ${conn.id}`, deviceError instanceof Error ? deviceError.message : String(deviceError));
            AppLogger.log('BLE_CONNECTION_ERROR', { error: errMsg, deviceId: conn.id, context: 'group_sync_fail' });
          }
          return null;
        }
      };

      const handshakeResults: (Device | null)[] = [];
      for (const conn of rawConns) {
        handshakeResults.push(await handshakeDevice(conn));
      }
      const connectedGroup = handshakeResults.filter((c): c is Device => c !== null);

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
      bleSend({ type: 'CONNECT_SUCCESS', devices: connectedGroup });
      if (wasSweeperActive && bleManager) scanner.startSweeper?.();
    } catch (e: unknown) {
      const errMsg = e instanceof Error ? e.message : String(e);
      if (errMsg.includes('was disconnected') || errMsg.includes('is not connected') || errMsg.includes('not connected') || errMsg.includes('Device disconnected')) {
         AppLogger.warn(`[BLE] Group connection dropout (ignoring VIP error)`);
      } else {
         AppLogger.error('FAILED TO CONNECT TO GROUP', e instanceof Error ? e.message : String(e));
         AppLogger.log('BLE_CONNECTION_ERROR', { error: errMsg, context: 'group' });
      }
      setGate('IDLE'); // Error recovery — inner connection failed
      if (wasSweeperActive && bleManager) scanner.startSweeper?.();
    }
  } catch (outerErr: unknown) {
    AppLogger.error('[BLE] connectToDevices outer failed', outerErr instanceof Error ? outerErr.message : String(outerErr));
    setGate('IDLE'); // Error recovery — outer connection failed
  } finally {
    release();
  }
}
