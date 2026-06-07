import type React from 'react';
import { Platform } from 'react-native';
import { AppLogger } from './AppLogger';
import { BLEPhaseTag } from './ble/BleMachine.types';

/**
 * executeRealDisconnect — The actual GATT connection cancellation and state resets.
 */
export async function executeRealDisconnect(
  bleManager: any,
  connectedDevicesRef: React.MutableRefObject<any[]>,
  disconnectListeners: React.MutableRefObject<Record<string, any>>,
  mtuMapRef: React.MutableRefObject<Map<string, number>>,
  adapterMapRef: React.MutableRefObject<Map<string, any>>,
  autoRecovery: any,
  getGate: () => BLEPhaseTag,
  setConnectedDevices: React.Dispatch<React.SetStateAction<any[]>>,
  setGate: (phase: BLEPhaseTag) => void
): Promise<void> {
  if (getGate() === 'DISCONNECTING') return;
  setGate('DISCONNECTING');
  await autoRecovery.cancelAllRecoveries();

  Object.values(disconnectListeners.current).forEach((sub: any) => {
    try {
      sub.remove();
    } catch (e) {
      AppLogger.warn('[BLE] Failed to remove disconnect listener', { error: String(e) });
    }
  });
  disconnectListeners.current = {};

  const staleDevices = [...connectedDevicesRef.current];

  if (staleDevices.length > 0 && Platform.OS !== 'web') {
    for (const device of staleDevices) {
      try {
        await bleManager.cancelDeviceConnection(device.id).catch((e: any) =>
          AppLogger.warn(`[BLE] Disconnect soft fail for ${device.id}`, e)
        );
      } catch (e) {
        AppLogger.error(`[BLE] Fatal disconnect fault for ${device.id}`, e);
      }
    }
    await new Promise(resolve => setTimeout(resolve, 250));
  }

  mtuMapRef.current.clear();
  adapterMapRef.current.clear();
  setConnectedDevices([]);
  setGate('IDLE');
  AppLogger.log('BLE_STATE_CHANGE', { event: 'keepalive_expired_disconnect' });
}

/**
 * disconnectFromDevice — Keepalive-aware public disconnect API.
 * Defers physical disconnect by keepaliveDurationMs.
 */
export function disconnectFromDevice(
  keepaliveTimerRef: React.MutableRefObject<ReturnType<typeof setTimeout> | null>,
  keepaliveDurationMs: number,
  executeRealDisconnectFn: () => Promise<void>
): void {
  if (keepaliveTimerRef.current) return;
  AppLogger.log('BLE_STATE_CHANGE', { event: 'keepalive_started', durationMs: keepaliveDurationMs });
  keepaliveTimerRef.current = setTimeout(() => {
    keepaliveTimerRef.current = null;
    executeRealDisconnectFn();
  }, keepaliveDurationMs);
}

/**
 * forceDisconnect — Immediately cancel all keepalives and teardown GATT sessions.
 */
export function forceDisconnect(
  keepaliveTimerRef: React.MutableRefObject<ReturnType<typeof setTimeout> | null>,
  executeRealDisconnectFn: () => Promise<void>
): void {
  if (keepaliveTimerRef.current) {
    clearTimeout(keepaliveTimerRef.current);
    keepaliveTimerRef.current = null;
    AppLogger.log('BLE_STATE_CHANGE', { event: 'keepalive_force_cancelled' });
  }
  executeRealDisconnectFn();
}
