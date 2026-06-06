import React, { useEffect, useRef } from 'react';
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
 * useBLEHeartbeat — Stale GATT Link Detector
 *
 * Periodically pings each connected device every 45s to verify the BLE link is still
 * alive. On Samsung Galaxy A-series (and similar) Android devices, the OS silently
 * keeps a stale GATT handle alive for minutes after the physical device goes out of
 * range or powers off. Without this heartbeat, the stale link is only detected when
 * the user tries to write a colour — causing visible lag and a slow recovery cycle.
 *
 * Ping strategy (in order):
 *   1. Zengge devices  → sends the 0x63 hardware-settings query (5-byte EEPROM probe).
 *      Same payload used by the recovery ping — proven safe, side-effect-free.
 *   2. BanlanX / no adapter → `readRSSIForDevice` — a single GATT read that costs
 *      ~1ms on wire and reliably surfaces a broken link with GATT status 133 / 8.
 *
 * On any GATT error the hook:
 *   a) Cancels the device connection (clears the stale OS handle immediately).
 *   b) Calls `onStaleLinkDetected(deviceId)` so useBLEAutoRecovery can begin
 *      Phase 1 aggressive reconnection before the user ever opens the controller.
 *
 * Issue ref: MISS-03 — ble_risk_analysis.md
 */
export function useBLEHeartbeat({
  bleManager,
  connectedDevicesRef,
  adapterMapRef,
  onStaleLinkDetected,
}: UseBLEHeartbeatParams): void {
  // Capture stable callback ref to prevent stale closures in the interval.
  const onStaleLinkRef = useRef(onStaleLinkDetected);
  useEffect(() => {
    onStaleLinkRef.current = onStaleLinkDetected;
  }, [onStaleLinkDetected]);

  useEffect(() => {
    if (Platform.OS === 'web' || !bleManager) return;

    const intervalId = setInterval(async () => {
      const devices = connectedDevicesRef.current;
      if (devices.length === 0) return;

      // Ping all connected devices in parallel — failures are independent.
      await Promise.allSettled(
        devices.map(async (device) => {
          const mac = device.id;
          const adapter = adapterMapRef.current.get(mac);

          try {
            if (adapter) {
              // ── Zengge path: 0x63 hardware-settings query ──────────────────
              const queryResult = adapter.buildQuerySettings(false);
              if (queryResult.packets.length > 0) {
                const b64 = Buffer.from(queryResult.packets[0]).toString('base64');
                await bleManager.writeCharacteristicWithoutResponseForDevice(
                  mac,
                  adapter.serviceUUID,
                  adapter.writeCharacteristicUUID,
                  b64,
                );
                AppLogger.log('DEVICE_DISCOVERED', {
                  context: 'heartbeat_ping_ok',
                  deviceId: mac,
                });
                return;
              }
            }

            // ── BanlanX / unknown adapter fallback: RSSI read ───────────────
            // readRSSIForDevice is a single-packet GATT read — if the device is
            // unreachable the stack throws synchronously with GATT 133 / 8.
            await bleManager.readRSSIForDevice(mac);
            AppLogger.log('DEVICE_DISCOVERED', {
              context: 'heartbeat_rssi_ok',
              deviceId: mac,
            });
          } catch (err: unknown) {
            const message = err instanceof Error ? err.message : String(err);
            AppLogger.warn('[BLE Heartbeat] Stale link detected — initiating recovery', {
              deviceId: mac,
              error: message,
            });

            // Cancel the stale GATT handle so the OS releases it immediately.
            // Swallow cancellation errors — we are already in a broken state.
            await bleManager.cancelDeviceConnection(mac).catch(() => undefined);

            // Signal the recovery engine. useBLEAutoRecovery will begin Phase 1
            // aggressive reconnection immediately.
            onStaleLinkRef.current(mac);
          }
        }),
      );
    }, HEARTBEAT_INTERVAL_MS);

    return () => clearInterval(intervalId);
  // connectedDevicesRef and adapterMapRef are stable React refs — intentionally
  // not in deps array; interval callback always reads .current at fire time.
  // bleManager is stable after mount (BleManager singleton).
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [bleManager]);
}
