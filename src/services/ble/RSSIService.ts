/**
 * RSSIService.ts — Pure RSSI polling logic extracted from useBLERSSIMonitor.
 *
 * Provides the core readDeviceRSSI function and the startRSSIPolling loop
 * as plain functions — no React hooks. Consumed by useBLERSSIMonitor (thin
 * wrapper) and testable without a React environment.
 *
 * Thresholds:
 *   WEAK     < -75 dBm → show weak-signal badge (yellow)
 *   CRITICAL < -82 dBm → trigger proactive reconnect
 */
import type { Device } from 'react-native-ble-plx';
import { AppLogger } from '../appLogger';
import { scrubPII } from '../../utils/piiScrubber';

/** Minimal BleManager surface required by RSSIService — allows partial mocks in tests. */
interface RSSIBleManager {
  readRSSIForDevice(deviceIdentifier: string): Promise<Device>;
}

export const RSSI_WEAK_THRESHOLD = -75;
export const RSSI_CRITICAL_THRESHOLD = -82;
export const RSSI_POLL_INTERVAL_MS = 30_000;

/**
 * readDeviceRSSI — Pure async RSSI probe via GATT.
 *
 * Uses bleManager.readRSSIForDevice (returns live GATT rssi, NOT stale advert cache).
 * Returns null on any GATT error — never throws.
 */
export async function readDeviceRSSI(
  mac: string,
  bleManager: RSSIBleManager,
): Promise<number | null> {
  try {
    const device: Device = await bleManager.readRSSIForDevice(mac);
    const rssi = device.rssi ?? null;
    AppLogger.log('DEVICE_DISCOVERED', { context: 'rssi_poll_ok', deviceId: scrubPII(mac), rssi });
    return rssi;
  } catch (err: unknown) {
    const message = err instanceof Error ? err.message : String(err);
    AppLogger.warn('[RSSIService] readRSSIForDevice failed', { deviceId: scrubPII(mac), error: message, payload_size: 0, ssi: 0 });
    return null;
  }
}

export interface RSSIPollingCallbacks {
  onRssiUpdated: (mac: string, rssi: number) => void;
  onWeakSignal?: (mac: string, rssi: number) => void;
  onCriticalSignal?: (mac: string, rssi: number) => void;
  /** Returns the live connected device list — called on each poll tick */
  getConnectedDevices: () => Device[];
}

/**
 * startRSSIPolling — Starts the 30s RSSI polling loop.
 *
 * Returns a cleanup function. Call it on unmount or on READY → exit.
 * Safe to call multiple times — idempotent via the `_isRunning` guard.
 */
export function startRSSIPolling(
  bleManager: RSSIBleManager,
  callbacks: RSSIPollingCallbacks,
): () => void {
  let _isRunning = false;

  const intervalId = setInterval(async () => {
    if (_isRunning) return;
    _isRunning = true;
    try {
      const devices = callbacks.getConnectedDevices();
      if (devices.length === 0) return;

      await Promise.all(devices.map(async (device) => {
        const mac = device.id;
        const rssi = await readDeviceRSSI(mac, bleManager);
        if (rssi === null) return;

        callbacks.onRssiUpdated(mac, rssi);

        if (rssi < RSSI_CRITICAL_THRESHOLD) {
          AppLogger.warn('[RSSIService] Critical signal — proactive reconnect', { deviceId: scrubPII(mac), rssi, payload_size: 0, ssi: 0 });
          callbacks.onCriticalSignal?.(mac, rssi);
        } else if (rssi < RSSI_WEAK_THRESHOLD) {
          AppLogger.warn('[RSSIService] Weak signal detected', { deviceId: scrubPII(mac), rssi, payload_size: 0, ssi: 0 });
          callbacks.onWeakSignal?.(mac, rssi);
        }
      }));
    } finally {
      _isRunning = false;
    }
  }, RSSI_POLL_INTERVAL_MS);

  return () => clearInterval(intervalId);
}
