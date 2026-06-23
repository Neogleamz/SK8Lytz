import { fromPromise } from 'xstate';
import { AppLogger } from '../appLogger';
import type { BleMachineContext } from './BleMachine.types';

/**
 * disconnectService — Invoked actor for DISCONNECTING state (H1 fix).
 * Performs GATT teardown: cancels all device connections and removes
 * disconnect listener subscriptions. Returns to IDLE on success or error.
 */
interface DestroyableClient {
  destroyClient: () => void;
}

function isDestroyable(manager: unknown): manager is DestroyableClient {
  return typeof manager === 'object' && manager !== null && 'destroyClient' in manager && typeof (manager as Record<string, unknown>).destroyClient === 'function';
}

const disconnectService = fromPromise<
  { success: boolean },
  {
    bleManager: BleMachineContext['bleManager'];
    connectedDevices: BleMachineContext['connectedDevices'];
    disconnectListeners: BleMachineContext['disconnectListeners'];
  }
>(async ({ input }) => {
  const { bleManager, connectedDevices, disconnectListeners } = input;
  for (const device of connectedDevices) {
    try {
      await bleManager.cancelDeviceConnection(device.id);
    } catch (e: unknown) {
      AppLogger.warn('[disconnectService] cancelDeviceConnection failed', {
        deviceId: device.id,
        error: e instanceof Error ? e.message : String(e),
        payload_size: 0,
        ssi: 0,
      });
    }
    // Remove disconnect listener subscription
    if (disconnectListeners.current[device.id]) {
      disconnectListeners.current[device.id].remove();
      delete disconnectListeners.current[device.id];
    }
  }
  // Destroy native BLE manager stack once — after ALL devices have been disconnected.
  if (isDestroyable(bleManager)) {
    bleManager.destroyClient();
  }
  return { success: true };
});

export { disconnectService };
