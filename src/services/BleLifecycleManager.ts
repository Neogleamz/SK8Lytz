import type React from 'react';
import { Platform } from 'react-native';
import type { BleManager, Subscription, Device } from 'react-native-ble-plx';
import { AppLogger } from './AppLogger';
import { BLEPhaseTag, BleMachineEvent } from './ble/BleMachine.types';
import { BLE_TIMING } from '../constants/bleTimingConstants';

/**
 * executeRealDisconnect — The actual GATT connection cancellation and state resets.
 */
export async function executeRealDisconnect(
  bleManager: BleManager | null,
  connectedDevicesRef: React.MutableRefObject<Device[]>,
  disconnectListeners: React.MutableRefObject<Record<string, Subscription>>,
  mtuMapRef: React.MutableRefObject<Map<string, number>>,
  adapterMapRef: React.MutableRefObject<Map<string, any>>,

  getGate: () => BLEPhaseTag,
  setConnectedDevices: React.Dispatch<React.SetStateAction<Device[]>>,
  setGate: (phase: BLEPhaseTag) => void,
  bleSend: (event: BleMachineEvent) => void
): Promise<void> {
  if (getGate() === 'DISCONNECTING') return;
  setGate('DISCONNECTING');


  Object.values(disconnectListeners.current).forEach((sub: any) => {
    try {
      sub.remove();
    } catch (e: unknown) {
      AppLogger.warn('[BLE] Failed to remove disconnect listener', { error: e instanceof Error ? e.message : String(e)  });
    }
  });
  disconnectListeners.current = {};

  const staleDevices = [...connectedDevicesRef.current];

  if (staleDevices.length > 0 && Platform.OS !== 'web') {
    for (const device of staleDevices) {
      try {
        await bleManager?.cancelDeviceConnection(device.id).catch((e: unknown) =>
          AppLogger.warn(`[BLE] Disconnect soft fail for ${device.id}`, e instanceof Error ? e.message : String(e))
        );
      } catch (e: unknown) {
        AppLogger.error(`[BLE] Fatal disconnect fault for ${device.id}`, e instanceof Error ? e.message : String(e));
      }
    }
    await new Promise(resolve => setTimeout(resolve, BLE_TIMING.DISCONNECT_SETTLE_MS));
  }

  mtuMapRef.current.clear();
  adapterMapRef.current.clear();
  setConnectedDevices([]);
  setGate('IDLE');
  bleSend({ type: 'DISCONNECT_COMPLETE' });
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
