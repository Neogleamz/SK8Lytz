import React, { useEffect, useState, useRef } from 'react';
import { Platform } from 'react-native';
import type { Device } from 'react-native-ble-plx';
import { AppLogger } from '../../services/AppLogger';

// ── RSSI signal-quality thresholds ───────────────────────────────────────────
/** Below this: show weak-signal badge (yellow). */
export const RSSI_WEAK_THRESHOLD = -75;
/** Below this: trigger preemptive reconnect to pick a better radio channel. */
export const RSSI_CRITICAL_THRESHOLD = -82;
/** Polling interval — long enough to stay lightweight, short enough for timely UI updates. */
export const RSSI_POLL_INTERVAL_MS = 30_000;

export interface UseBLERSSIMonitorParams {
  bleManager: any;
  /** Stable ref to the current connected device list — read lazily inside interval. */
  connectedDevicesRef: React.MutableRefObject<Device[]>;
  /** Array of active device IDs to trigger prune when disconnected */
  connectedDeviceIds: string[];
  /**
   * Called when a device's RSSI drops below RSSI_WEAK_THRESHOLD (-75 dBm).
   * Purely informational — use for UI badge updates or logging.
   */
  onWeakSignal?: (deviceId: string, rssi: number) => void;
  /**
   * Called when a device's RSSI drops below RSSI_CRITICAL_THRESHOLD (-82 dBm).
   * Intended for proactive reconnect to select a better radio channel.
   */
  onCriticalSignal?: (deviceId: string, rssi: number) => void;
}

/**
 * readDeviceRSSI — Pure async liveness + signal-quality probe.
 *
 * Exported for direct unit-testing (no React context required).
 * Calls bleManager.readRSSIForDevice and extracts the rssi number.
 * Returns null on any GATT error — never throws.
 *
 * NOTE: react-native-ble-plx's readRSSIForDevice returns `Promise<Device>`
 * with the `.rssi` field populated from the live GATT read, NOT from the
 * stale advertisement cache. This is the correct API for post-connect probing.
 */
export async function readDeviceRSSI(
  mac: string,
  bleManager: any,
): Promise<number | null> {
  try {
    const device: Device = await bleManager.readRSSIForDevice(mac);
    const rssi = device.rssi ?? null;
    AppLogger.log('DEVICE_DISCOVERED', { context: 'rssi_poll_ok', deviceId: mac, rssi });
    return rssi;
  } catch (err: unknown) {
    const message = err instanceof Error ? err.message : String(err);
    AppLogger.warn('[BLE RSSI Monitor] readRSSIForDevice failed', { deviceId: mac, error: message });
    return null;
  }
}

/**
 * useBLERSSIMonitor — Post-Connect Signal Quality Monitor
 *
 * Polls RSSI every 30s on all connected devices. Surfaces live signal
 * strength as a `Record<string, number>` map keyed by device MAC.
 *
 * Fires onWeakSignal / onCriticalSignal callbacks for UI and recovery
 * integration. UI components read `rssiMap[mac]` to determine badge color.
 *
 * Separated from useBLEHeartbeat intentionally:
 *   - Heartbeat = connectivity gate (fail → recovery)
 *   - RSSI monitor = signal quality sensor (threshold → UI + proactive reconnect)
 *
 * Issue ref: BAT-02 — ble_risk_analysis.md
 */
export function useBLERSSIMonitor({
  bleManager,
  connectedDevicesRef,
  connectedDeviceIds,
  onWeakSignal,
  onCriticalSignal,
}: UseBLERSSIMonitorParams): Record<string, number> {
  const [rssiMap, setRssiMap] = useState<Record<string, number>>({});
  const _isRunningRef = useRef(false);

  useEffect(() => {
    if (Platform.OS === 'web' || !bleManager) return;

    const intervalId = setInterval(async () => {
      if (_isRunningRef.current) return;
      _isRunningRef.current = true;
      try {
        const devices = connectedDevicesRef.current;
        if (devices.length === 0) return;

        for (const device of devices) {
          const mac = device.id;
          const rssi = await readDeviceRSSI(mac, bleManager);

          if (rssi === null) continue; // GATT error — heartbeat handles dead links

          setRssiMap(prev => ({ ...prev, [mac]: rssi }));

          if (rssi < RSSI_CRITICAL_THRESHOLD) {
            AppLogger.warn('[BLE RSSI] Critical signal — proactive reconnect', { mac, rssi });
            onCriticalSignal?.(mac, rssi);
          } else if (rssi < RSSI_WEAK_THRESHOLD) {
            AppLogger.warn('[BLE RSSI] Weak signal detected', { mac, rssi });
            onWeakSignal?.(mac, rssi);
          }
        }
      } finally {
        _isRunningRef.current = false;
      }
    }, RSSI_POLL_INTERVAL_MS);

    return () => clearInterval(intervalId);
  // connectedDevicesRef is a stable ref — intentionally omitted from deps.
  // onWeakSignal / onCriticalSignal are optional callbacks; callers must
  // memoize them (useCallback) if referential stability matters.
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [bleManager, onWeakSignal, onCriticalSignal]);

  // Clear rssiMap when devices disconnect to avoid stale entries in the badge.
  useEffect(() => {
    setRssiMap(prev => {
      const pruned = { ...prev };
      Object.keys(pruned).forEach(mac => {
        if (!connectedDeviceIds.includes(mac)) delete pruned[mac];
      });
      return pruned;
    });
  }, [connectedDeviceIds]);

  return rssiMap;
}
