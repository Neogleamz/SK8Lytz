import { fromCallback } from 'xstate';
import { Platform } from 'react-native';
import { Buffer } from 'buffer';
import type { BleManager, Device, BleError, Characteristic } from 'react-native-ble-plx';
import type { IControllerProtocol } from '../../protocols/IControllerProtocol';
import type { BleMachineEvent } from './BleMachine.types';
import { AppLogger } from '../appLogger';
import { scrubPII } from '../../utils/piiScrubber';
import { createGattSession } from '../BleSessionFactory';
import { enqueueWrite, clearWriteQueue, enqueueDelay } from '../BleWriteQueue';
import { BLE_TIMING } from '../../constants/bleTimingConstants';

/** Total recovery budget: Phase 1 (12) + Phase 2 (5) = 17 */
export const MAX_RECOVERY_ATTEMPTS = BLE_TIMING.RECOVERY_PHASE_1_MAX_ATTEMPTS + BLE_TIMING.RECOVERY_PHASE_2_MAX_ATTEMPTS;

export const getRecoveryBackoffMs = (attempts: number): number => {
  const exponential = Math.min(BLE_TIMING.RECOVERY_BASE_MS * Math.pow(1.5, attempts), BLE_TIMING.RECOVERY_MAX_MS);
  const jitter = Math.random() * exponential * 0.3;
  return Math.round(exponential + jitter);
};

export const hasExceededMaxRecovery = (attempts: number): boolean => {
  return attempts >= MAX_RECOVERY_ATTEMPTS;
};

interface RecoveryInput {
  bleManager: BleManager;
  ghostedDeviceIds: string[];
  adapterMapRef: { current: Map<string, IControllerProtocol> };
  mtuMapRef: { current: Map<string, number> };
  disconnectListeners: { current: Record<string, import('react-native-ble-plx').Subscription> };
  /** H2 fix: notification monitor subscriptions keyed by device ID for cleanup on reconnect */
  notificationListeners?: { current: Record<string, import('react-native-ble-plx').Subscription> };
  handleOrganicDisconnect: (error: BleError | null, deviceId: string) => void;
  /**
   * onOrganicDisconnect — fires when a recovered device drops again.
   * Wired by useBLE.ts to send RECOVERY_START back to the machine.
   */
  onOrganicDisconnect: (deviceId: string) => void;
  handleNotification: (error: BleError | null, characteristic: Characteristic | null, deviceId: string) => void;
  getSweepedDevice?: (deviceId: string) => Device | undefined;
}

export const recoveryService = fromCallback<BleMachineEvent, RecoveryInput>(({ input, sendBack }) => {
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
    const TOTAL_MAX_ATTEMPTS = BLE_TIMING.RECOVERY_PHASE_1_MAX_ATTEMPTS + BLE_TIMING.RECOVERY_PHASE_2_MAX_ATTEMPTS;
    while (!cancelled && attempts < TOTAL_MAX_ATTEMPTS && !hasExceededMaxRecovery(attempts)) {
      try {
        const backoff = attempts <= BLE_TIMING.RECOVERY_PHASE_1_MAX_ATTEMPTS
          ? getRecoveryBackoffMs(attempts)
          : BLE_TIMING.RECOVERY_PHASE_2_BACKOFF_MS + Math.random() * BLE_TIMING.RECOVERY_BASE_MS;
        // R-01: route the reconnect backoff through the BLE write queue.
        await enqueueDelay('critical', backoff);

        if (cancelled) break;
        attempts++;

        if (hasExceededMaxRecovery(attempts)) {
          AppLogger.warn(`[RecoveryService] '${scrubPII(deviceId)}' failed after ${attempts} attempts`, { payload_size: 0, ssi: 0 });
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
          const msg = e instanceof Error ? e.message : String(e);
          AppLogger.warn('[RecoveryService] MTU negotiation failed', { deviceId: scrubPII(deviceId), error: msg, payload_size: 0, ssi: 0 });
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
          handleOrganicDisconnect((contextError as unknown) as BleError | null, conn.id);
          onOrganicDisconnect(conn.id);
        });

        // H2 FIX: Clean up stale notification subscription before re-registering
        if (input.notificationListeners?.current[conn.id]) {
          input.notificationListeners.current[conn.id].remove();
          delete input.notificationListeners.current[conn.id];
        }
        const notifSub = conn.monitorCharacteristicForService(
          recoveryAdapter.serviceUUID,
          recoveryAdapter.notifyCharacteristicUUID,
          (error: Error | null, characteristic: import('react-native-ble-plx').Characteristic | null) =>
            handleNotification(error as BleError | null, characteristic, conn.id)
        );
        if (input.notificationListeners) {
          input.notificationListeners.current[conn.id] = notifSub;
        }

        await new Promise(r => setTimeout(r, BLE_TIMING.RECOVERY_PING_SETTLE_MS));
        if (signal.aborted || cancelled) break;
        const pingResult = recoveryAdapter.buildQuerySettings(false);
        if (pingResult.packets.length > 0) {
          await enqueueWrite('critical', async () => {
            await conn.writeCharacteristicWithoutResponseForService(
              recoveryAdapter.serviceUUID, recoveryAdapter.writeCharacteristicUUID,
              Buffer.from(pingResult.packets[0]).toString('base64')
            ).catch((e: unknown) => {
              const msg = e instanceof Error ? e.message : String(e);
              AppLogger.warn('[RecoveryService] Recovery ping failed', { error: msg, payload_size: 0, ssi: 0 });
            });
            return true;
          });
        }
        if (signal.aborted || cancelled) break;

        reconnectedDevice = conn;
        AppLogger.log('AUTO_RECOVERY_SUCCESS', { deviceId: scrubPII(deviceId), attempts });
        break; // Success! Exit while loop.

      } catch (e: unknown) {
        const msg = e instanceof Error ? e.message : String(e);
        AppLogger.warn(`[RecoveryService] Connection attempt failed for '${scrubPII(deviceId)}'`, { error: msg, payload_size: 0, ssi: 0 });
      }
    }

    // --- Phase 3: Passive sweeper-watch mode ---
    if (!reconnectedDevice && !cancelled) {
      AppLogger.log('AUTO_RECOVERY', { phase: 3, deviceId: scrubPII(deviceId), event: 'entering_passive_mode' });
      let phase3Polls = 0;
      while (!cancelled && phase3Polls < BLE_TIMING.RECOVERY_PHASE_3_MAX_POLLS) {
        phase3Polls++;
        await new Promise(r => setTimeout(r, BLE_TIMING.RECOVERY_PHASE_3_POLL_INTERVAL_MS));
        if (cancelled) break;

        const sweepedDevice = getSweepedDevice?.(deviceId);
        if (!sweepedDevice) continue;

        AppLogger.log('AUTO_RECOVERY', { phase: 3, deviceId: scrubPII(deviceId), event: 'mac_reappeared_attempting_reconnect' });
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
          } catch (e: unknown) {
            const msg = e instanceof Error ? e.message : String(e);
            AppLogger.warn('[RecoveryService] Phase 3 MTU negotiation failed (non-fatal)', { deviceId: scrubPII(deviceId), error: msg, payload_size: 0, ssi: 0 });
          }

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
            handleOrganicDisconnect((contextError as unknown) as BleError | null, conn.id);
            onOrganicDisconnect(conn.id);
          });
          // H2 FIX: Clean up stale notification subscription before re-registering
          if (input.notificationListeners?.current[conn.id]) {
            input.notificationListeners.current[conn.id].remove();
            delete input.notificationListeners.current[conn.id];
          }
          const notifSub = conn.monitorCharacteristicForService(
            recoveryAdapter.serviceUUID,
            recoveryAdapter.notifyCharacteristicUUID,
            (error: Error | null, characteristic: import('react-native-ble-plx').Characteristic | null) =>
              handleNotification(error as BleError | null, characteristic, conn.id)
          );
          if (input.notificationListeners) {
            input.notificationListeners.current[conn.id] = notifSub;
          }

          reconnectedDevice = conn;
          AppLogger.log('AUTO_RECOVERY_SUCCESS', { deviceId: scrubPII(deviceId), phase: 3 });
          break; // Success! Exit while loop.
        } catch (e: unknown) {
          const msg = e instanceof Error ? e.message : String(e);
          AppLogger.warn('[RecoveryService] Phase 3 reconnect failed', { deviceId: scrubPII(deviceId), error: msg, payload_size: 0, ssi: 0 });
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
