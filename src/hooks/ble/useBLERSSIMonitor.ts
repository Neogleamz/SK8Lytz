/**
 * useBLERSSIMonitor.ts — Post-Connect Signal Quality Monitor (thin wrapper)
 *
 * React hook wrapper around RSSIService.startRSSIPolling.
 * Owns the rssiMap React state that UI components read for badge colors.
 * The core polling logic lives in services/ble/RSSIService.ts (pure, testable).
 *
 * Separated from useBLEHeartbeat intentionally:
 *   - Heartbeat = connectivity gate (fail → recovery)
 *   - RSSI monitor = signal quality sensor (threshold → UI + proactive reconnect)
 *
 * Issue ref: BAT-02 — ble_risk_analysis.md
 */
import React, { useEffect, useState } from 'react';
import { Platform } from 'react-native';
import type { Device, BleManager } from 'react-native-ble-plx';
import {
  startRSSIPolling,
  readDeviceRSSI,
  RSSI_WEAK_THRESHOLD,
  RSSI_CRITICAL_THRESHOLD,
  RSSI_POLL_INTERVAL_MS,
} from '../../services/ble/RSSIService';

// Re-export constants AND readDeviceRSSI so existing test imports don't break
export { RSSI_WEAK_THRESHOLD, RSSI_CRITICAL_THRESHOLD, RSSI_POLL_INTERVAL_MS, readDeviceRSSI };

export interface UseBLERSSIMonitorParams {
  bleManager: BleManager | null;
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

export function useBLERSSIMonitor({
  bleManager,
  connectedDevicesRef,
  connectedDeviceIds,
  onWeakSignal,
  onCriticalSignal,
}: UseBLERSSIMonitorParams): Record<string, number> {
  const [rssiMap, setRssiMap] = useState<Record<string, number>>({});

  useEffect(() => {
    if (Platform.OS === 'web' || !bleManager) return;

    const stopPolling = startRSSIPolling(bleManager, {
      getConnectedDevices: () => connectedDevicesRef.current,
      onRssiUpdated: (mac, rssi) => setRssiMap(prev => ({ ...prev, [mac]: rssi })),
      onWeakSignal,
      onCriticalSignal,
    });

    return stopPolling;
  // connectedDevicesRef is a stable ref — intentionally omitted from deps.
  // onWeakSignal / onCriticalSignal are optional callbacks; callers must
  // memoize them (useCallback) if referential stability matters.
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [bleManager, onWeakSignal, onCriticalSignal]);

  // Clear rssiMap when devices disconnect to avoid stale entries in the badge.
  useEffect(() => {
    setRssiMap(prev => {
      let changed = false;
      const pruned = { ...prev };
      Object.keys(pruned).forEach(mac => {
        if (!connectedDeviceIds.includes(mac)) {
          delete pruned[mac];
          changed = true;
        }
      });
      return changed ? pruned : prev;
    });
  }, [connectedDeviceIds]);

  return rssiMap;
}
