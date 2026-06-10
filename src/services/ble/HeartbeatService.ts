import { fromCallback } from 'xstate';
import { Buffer } from 'buffer';
import type { Device } from 'react-native-ble-plx';
import { AppLogger } from '../../services/AppLogger';
import type { IControllerProtocol } from '../../protocols/IControllerProtocol';
import { enqueueWrite, isWriteQueueActive } from '../BleWriteQueue';

/** 45 seconds — long enough not to spam, short enough to catch stale links before the user notices. */
const HEARTBEAT_INTERVAL_MS = 45_000;

export interface HeartbeatServiceInput {
  bleManager: any;
  connectedDevices: Device[];
  adapterMap: Map<string, IControllerProtocol>;
}

export const heartbeatService = fromCallback<any, HeartbeatServiceInput>(({ input, sendBack }) => {
  const { bleManager, connectedDevices, adapterMap } = input;
  let isRunning = false;

  const interval = setInterval(async () => {
    if (isRunning) return;
    // Skip this heartbeat cycle if any GATT operation is in-flight or pending.
    // The in-flight write proves the connection is alive; no ping needed.
    if (isWriteQueueActive()) return;
    isRunning = true;
    try {
      if (connectedDevices.length === 0) return;

      await Promise.all(connectedDevices.map(async (device) => {
        const mac = device.id;
        const adapter = adapterMap.get(mac);

        try {
          if (adapter) {
            const queryResult = adapter.buildQuerySettings(false);
            if (queryResult.packets.length > 0) {
              const b64 = Buffer.from(queryResult.packets[0]).toString('base64');
              await enqueueWrite('normal', async () => {
                await bleManager.writeCharacteristicWithoutResponseForDevice(
                  mac,
                  adapter.serviceUUID,
                  adapter.writeCharacteristicUUID,
                  b64,
                );
                return true;
              });
              AppLogger.log('DEVICE_DISCOVERED', { context: 'heartbeat_ping_ok', deviceId: mac });
              return;
            }
          }
          // BanlanX / unknown adapter fallback: RSSI read as a liveness probe
          await enqueueWrite('normal', async () => {
            await bleManager.readRSSIForDevice(mac);
            return true;
          });
          AppLogger.log('DEVICE_DISCOVERED', { context: 'heartbeat_rssi_ok', deviceId: mac });
        } catch (err: unknown) {
          const message = err instanceof Error ? err.message : String(err);
          AppLogger.warn('[BLE Heartbeat] Stale link detected — initiating recovery', {
            deviceId: mac,
            error: message,
          });
          // Cancel the stale GATT handle so the OS releases it immediately.
          // Swallow cancellation errors — we are already in a broken state.
          await bleManager.cancelDeviceConnection(mac).catch(() => undefined);
          
          // Signal the machine that a heartbeat failed for this specific device.
          sendBack({ type: 'HEARTBEAT_FAIL', deviceId: mac });
        }
      }));
    } finally {
      isRunning = false;
    }
  }, HEARTBEAT_INTERVAL_MS);

  // XState calls this when machine exits READY
  return () => clearInterval(interval);
});
