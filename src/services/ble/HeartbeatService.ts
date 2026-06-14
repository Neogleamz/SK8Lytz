import { fromCallback } from 'xstate';
import { Buffer } from 'buffer';
import type { Device, BleManager } from 'react-native-ble-plx';
import { AppLogger } from '../../services/AppLogger';
import { scrubPII } from '../../utils/piiScrubber';
import type { IControllerProtocol } from '../../protocols/IControllerProtocol';
import { enqueueWrite, isWriteQueueActive } from '../BleWriteQueue';

/** 45 seconds — long enough not to spam, short enough to catch stale links before the user notices. */
const HEARTBEAT_INTERVAL_MS = 45_000;

export interface HeartbeatServiceInput {
  bleManager: Pick<BleManager, 'writeCharacteristicWithoutResponseForDevice' | 'readRSSIForDevice' | 'cancelDeviceConnection'>;
  connectedDevices: Device[];
  getConnectedDevices?: () => Device[];
  connectedDevicesRef?: { current: Device[] };
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
      const currentDevices = input.getConnectedDevices 
        ? input.getConnectedDevices() 
        : input.connectedDevicesRef?.current ?? connectedDevices;
      
      if (currentDevices.length === 0) return;

      await Promise.all(currentDevices.map(async (device) => {
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
              AppLogger.log('DEVICE_DISCOVERED', { context: 'heartbeat_ping_ok', deviceId: scrubPII(mac) });
              return;
            }
          }
          // BanlanX / unknown adapter fallback: RSSI read as a liveness probe
          await enqueueWrite('normal', async () => {
            await bleManager.readRSSIForDevice(mac);
            return true;
          });
          AppLogger.log('DEVICE_DISCOVERED', { context: 'heartbeat_rssi_ok', deviceId: scrubPII(mac) });
        } catch (err: unknown) {
          const message = err instanceof Error ? err.message : String(err);
          AppLogger.warn('[BLE Heartbeat] Stale link detected — initiating recovery', {
            deviceId: scrubPII(mac),
            error: message,
            payload_size: 0,
            ssi: 0
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
