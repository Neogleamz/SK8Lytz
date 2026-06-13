import { fromCallback } from 'xstate';
import { Platform } from 'react-native';
import { Buffer } from 'buffer';
import type { BleManager, Device } from 'react-native-ble-plx';
import { AppLogger } from '../AppLogger';
import { createGattSession } from '../BleSessionFactory';
import { enqueueWrite, clearWriteQueue } from '../BleWriteQueue';
import { BLE_TIMING } from '../../constants/bleTimingConstants';

export const MAX_RECOVERY_ATTEMPTS = 5;
const RECOVERY_BASE_MS = 1500;
const RECOVERY_MAX_MS = 30_000;
const PHASE_1_MAX_ATTEMPTS = 12;
const PHASE_2_MAX_ATTEMPTS = 5;
const PHASE_2_BACKOFF_MS = 20_000;
const PHASE_3_POLL_INTERVAL_MS = 5_000;
const PHASE_3_MAX_POLLS = 120;

export const getRecoveryBackoffMs = (attempts: number): number => {
  const exponential = Math.min(RECOVERY_BASE_MS * Math.pow(1.5, attempts), RECOVERY_MAX_MS);
  const jitter = Math.random() * exponential * 0.3;
  return Math.round(exponential + jitter);
};

export const hasExceededMaxRecovery = (attempts: number): boolean => {
  return attempts > MAX_RECOVERY_ATTEMPTS;
};

interface RecoveryInput {
  bleManager: BleManager;
  ghostedDeviceIds: string[];
  adapterMapRef: { current: Map<string, any> };
  mtuMapRef: { current: Map<string, number> };
  disconnectListeners: { current: Record<string, import('react-native-ble-plx').Subscription> };
  handleOrganicDisconnect: (error: any, deviceId: string) => void;
  /**
   * onOrganicDisconnect — fires when a recovered device drops again.
   * Wired by useBLE.ts to send RECOVERY_START back to the machine.
   */
  onOrganicDisconnect: (deviceId: string) => void;
  handleNotification: (error: any, characteristic: any, deviceId: string) => void;
  getSweepedDevice?: (deviceId: string) => Device | undefined;
}

export const recoveryService = fromCallback<any, RecoveryInput>(({ input, sendBack }) => {
  let cancelled = false;
  const abortController = new AbortController();
  const cancel = () => { 
    cancelled = true; 
    abortController.abort();
  };

  async function run() {
    const { 
      bleManager, ghostedDeviceIds, adapterMapRef, mtuMapRef, 
      disconnectListeners, handleOrganicDisconnect, onOrganicDisconnect,
      handleNotification, getSweepedDevice 
    } = input;

    // Clear stale pre-disconnect writes so they don't compete with recovery pings.
    clearWriteQueue();

    if (!ghostedDeviceIds || ghostedDeviceIds.length === 0) {
      sendBack({ type: 'RECOVERY_FAIL' });
      return;
    }

    const deviceId = ghostedDeviceIds[0];
    let attempts = 0;
    let reconnectedDevice: Device | null = null;

    // --- Phase 1 & 2: GATT hammering ---
    while (!cancelled && attempts <= PHASE_2_MAX_ATTEMPTS && !hasExceededMaxRecovery(attempts)) {
      try {
        const backoff = attempts <= PHASE_1_MAX_ATTEMPTS
          ? getRecoveryBackoffMs(attempts)
          : PHASE_2_BACKOFF_MS + Math.random() * RECOVERY_BASE_MS;
        await new Promise(r => setTimeout(r, backoff));

        if (cancelled) break;
        attempts++;

        if (hasExceededMaxRecovery(attempts)) {
          AppLogger.warn(`[RecoveryService] '[REDACTED]' failed after ${attempts} attempts`);
          break;
        }

        if (!bleManager) break;

        const signal = abortController.signal;
        if (signal.aborted || cancelled) break;

        const { conn, adapter: recoveryAdapter } = await createGattSession(bleManager, deviceId, {
          timeout: 3500,
          retries: 1,
          signal,
          context: 'autoRecovery',
        });
        if (signal.aborted || cancelled) break;

        adapterMapRef.current.set(conn.id, recoveryAdapter);

        try {
          if (Platform.OS === 'android') {
            const mtuResult = await conn.requestMTU(512);
            if (!signal.aborted && !cancelled) {
              const negotiatedMtu = mtuResult?.mtu ?? 186;
              mtuMapRef.current.set(conn.id, negotiatedMtu > 23 ? negotiatedMtu : 186);
            }
          } else {
            mtuMapRef.current.set(conn.id, 186);
          }
        } catch (e: unknown) {
          AppLogger.warn('[RecoveryService] MTU negotiation failed', { deviceId, error: e instanceof Error ? e.message : String(e) });
        }
        if (signal.aborted || cancelled) break;

        if (disconnectListeners.current[conn.id]) {
          disconnectListeners.current[conn.id].remove();
          delete disconnectListeners.current[conn.id];
        }

        disconnectListeners.current[conn.id] = bleManager.onDeviceDisconnected(conn.id, (error: Error | null) => {
          const contextError = error ? {
            message: error.message,
            stack: error.stack,
            name: error.name,
            status: (error && 'errorCode' in error) ? (error as {errorCode: unknown}).errorCode : (error && 'status' in error) ? (error as {status: unknown}).status : (error.message.includes('133') ? 133 : null)
          } : null;
          handleOrganicDisconnect(contextError, conn.id);
          onOrganicDisconnect(conn.id);
        });

        conn.monitorCharacteristicForService(
          recoveryAdapter.serviceUUID,
          recoveryAdapter.notifyCharacteristicUUID,
          (error: Error | null, characteristic: import('react-native-ble-plx').Characteristic | null) =>
            handleNotification(error, characteristic, conn.id)
        );

        await new Promise(r => setTimeout(r, BLE_TIMING.RECOVERY_PING_SETTLE_MS));
        if (signal.aborted || cancelled) break;
        const pingResult = recoveryAdapter.buildQuerySettings(false);
        if (pingResult.packets.length > 0) {
          await enqueueWrite('critical', async () => {
            await conn.writeCharacteristicWithoutResponseForService(
              recoveryAdapter.serviceUUID, recoveryAdapter.writeCharacteristicUUID,
              Buffer.from(pingResult.packets[0]).toString('base64')
            ).catch((e: unknown) => AppLogger.warn('[RecoveryService] Recovery ping failed', e instanceof Error ? e.message : String(e)));
            return true;
          });
        }
        if (signal.aborted || cancelled) break;

        reconnectedDevice = conn;
        AppLogger.log('AUTO_RECOVERY_SUCCESS', { deviceId, attempts });
        break; // Success! Exit while loop.

      } catch (e: unknown) {
        AppLogger.warn(`[RecoveryService] Connection attempt failed for '[REDACTED]'`, e instanceof Error ? e.message : String(e));
      }
    }

    // --- Phase 3: Passive sweeper-watch mode ---
    if (!reconnectedDevice && !cancelled) {
      AppLogger.log('AUTO_RECOVERY', { phase: 3, deviceId, event: 'entering_passive_mode' });
      let phase3Polls = 0;
      while (!cancelled && phase3Polls < PHASE_3_MAX_POLLS) {
        phase3Polls++;
        await new Promise(r => setTimeout(r, PHASE_3_POLL_INTERVAL_MS));
        if (cancelled) break;

        const sweepedDevice = getSweepedDevice?.(deviceId);
        if (!sweepedDevice) continue;

        AppLogger.log('AUTO_RECOVERY', { phase: 3, deviceId, event: 'mac_reappeared_attempting_reconnect' });
        try {
          if (cancelled) break;
          const signal = abortController.signal;

          const { conn, adapter: recoveryAdapter } = await createGattSession(bleManager, deviceId, {
            timeout: 3500, retries: 1, signal, context: 'autoRecoveryPhase3',
          });
          if (signal.aborted || cancelled) break;

          adapterMapRef.current.set(conn.id, recoveryAdapter);

          try {
            if (Platform.OS === 'android') {
              const mtuResult = await conn.requestMTU(512);
              const mtu = mtuResult?.mtu ?? 186;
              mtuMapRef.current.set(conn.id, mtu > 23 ? mtu : 186);
            } else {
              mtuMapRef.current.set(conn.id, 186);
            }
          } catch { /* non-fatal */ }

          if (disconnectListeners.current[conn.id]) {
            disconnectListeners.current[conn.id].remove();
            delete disconnectListeners.current[conn.id];
          }
          disconnectListeners.current[conn.id] = bleManager.onDeviceDisconnected(conn.id, (error: Error | null) => {
            const contextError = error ? {
              message: error.message,
              stack: error.stack,
              name: error.name,
              status: (error && 'errorCode' in error) ? (error as {errorCode: unknown}).errorCode : (error && 'status' in error) ? (error as {status: unknown}).status : (error.message.includes('133') ? 133 : null)
            } : null;
            handleOrganicDisconnect(contextError, conn.id);
            onOrganicDisconnect(conn.id);
          });
          conn.monitorCharacteristicForService(
            recoveryAdapter.serviceUUID,
            recoveryAdapter.notifyCharacteristicUUID,
            (error: Error | null, characteristic: import('react-native-ble-plx').Characteristic | null) =>
              handleNotification(error, characteristic, conn.id)
          );

          reconnectedDevice = conn;
          AppLogger.log('AUTO_RECOVERY_SUCCESS', { deviceId, phase: 3 });
          break; // Success! Exit while loop.
        } catch (e: unknown) {
          AppLogger.warn('[RecoveryService] Phase 3 reconnect failed', { deviceId, error: String(e) });
          break; // Give up
        }
      }
    }

    if (cancelled) return;

    if (reconnectedDevice) {
      sendBack({ type: 'RECOVERY_COMPLETE', devices: [reconnectedDevice] });
    } else if (hasExceededMaxRecovery(attempts)) {
      sendBack({ type: 'RECOVERY_PERMANENTLY_FAILED', deviceId });
    } else {
      sendBack({ type: 'RECOVERY_FAIL' });
    }
  }

  run();
  return cancel;
});
