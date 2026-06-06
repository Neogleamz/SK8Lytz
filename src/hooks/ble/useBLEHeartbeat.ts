import React, { useEffect } from 'react';
import { Platform } from 'react-native';
import { Buffer } from 'buffer';
import type { Device } from 'react-native-ble-plx';
import { AppLogger } from '../../services/AppLogger';
import type { IControllerProtocol } from '../../protocols/IControllerProtocol';

/** 45 seconds — long enough not to spam, short enough to catch stale links before the user notices. */
const HEARTBEAT_INTERVAL_MS = 45_000;

export interface UseBLEHeartbeatParams {
  bleManager: any;
  /**
   * Stable ref to the current connected devices list.
   * Read-only — heartbeat never mutates this.
   */
  connectedDevicesRef: React.MutableRefObject<Device[]>;
  /**
   * Stable ref to the per-device adapter map.
   * `.current` is read lazily inside the interval, so it's safe to pass even
   * if the map is populated slightly after hook call time.
   */
  adapterMapRef: React.MutableRefObject<Map<string, IControllerProtocol>>;
  /**
   * Called when a heartbeat ping fails, indicating the GATT connection is stale.
   * The hook cancels the dead connection first, then fires this callback so
   * useBLEAutoRecovery can immediately begin reconnection.
   */
  onStaleLinkDetected: (deviceId: string) => void;
}

/**
 * pingConnectedDevice — Core liveness probe for a single BLE device.
 *
 * Exported as a pure async function for direct unit-testing (no React context
 * required). Called by useBLEHeartbeat's setInterval on each tick.
 *
 * Strategy:
 *   1. Zengge: sends 0x63 hardware-settings query via writeCharacteristicWithoutResponse.
 *      Same payload used by recovery ping — proven safe, side-effect-free.
 *   2. BanlanX / no adapter: readRSSIForDevice as a lightweight liveness probe.
 *      A single GATT read that reliably surfaces broken links with GATT 133 / 8.
 *   On any GATT error: cancel the stale handle + call onStaleLinkDetected.
 */
export async function pingConnectedDevice(
  mac: string,
  bleManager: any,
  adapter: IControllerProtocol | undefined,
  onStaleLinkDetected: (deviceId: string) => void,
): Promise<void> {
  try {
    if (adapter) {
      const queryResult = adapter.buildQuerySettings(false);
      if (queryResult.packets.length > 0) {
        const b64 = Buffer.from(queryResult.packets[0]).toString('base64');
        await bleManager.writeCharacteristicWithoutResponseForDevice(
          mac,
          adapter.serviceUUID,
          adapter.writeCharacteristicUUID,
          b64,
        );
        AppLogger.log('DEVICE_DISCOVERED', { context: 'heartbeat_ping_ok', deviceId: mac });
        return;
      }
    }
    // BanlanX / unknown adapter fallback: RSSI read as a liveness probe
    await bleManager.readRSSIForDevice(mac);
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
    // Signal the recovery engine.
    onStaleLinkDetected(mac);
  }
}

/**
 * useBLEHeartbeat — Stale GATT Link Detector
 *
 * Thin orchestrator: registers a 45s setInterval and calls pingConnectedDevice
 * for each currently connected device. See pingConnectedDevice for the full
 * ping strategy and failure handling.
 *
 * Issue ref: MISS-03 — ble_risk_analysis.md
 */
export function useBLEHeartbeat({
  bleManager,
  connectedDevicesRef,
  adapterMapRef,
  onStaleLinkDetected,
}: UseBLEHeartbeatParams): void {
  useEffect(() => {
    if (Platform.OS === 'web' || !bleManager) return;

    const intervalId = setInterval(async () => {
      const devices = connectedDevicesRef.current;
      if (devices.length === 0) return;

      await Promise.allSettled(
        devices.map((device) =>
          pingConnectedDevice(
            device.id,
            bleManager,
            adapterMapRef.current.get(device.id),
            onStaleLinkDetected,
          )
        ),
      );
    }, HEARTBEAT_INTERVAL_MS);

    return () => clearInterval(intervalId);
  // connectedDevicesRef and adapterMapRef are stable React refs — intentionally
  // not in deps array; interval callback always reads .current at fire time.
  // bleManager is stable after mount (BleManager singleton).
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [bleManager, onStaleLinkDetected]);
}
