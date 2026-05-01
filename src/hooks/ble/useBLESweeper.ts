/**
 * useBLESweeper.ts — Silent Sweeper + Interrogator Queue
 *
 * Part of the Overwatch BLE Engine architecture.
 *
 * The Sweeper replaces the on-demand "press to scan" model with an
 * always-on, battery-safe passive BLE listener. When a new SK8Lytz
 * device is heard for the first time, the Interrogator Queue silently
 * connects, probes EEPROM, caches the result locally, and disconnects.
 *
 * By the time the user opens the Setup Wizard, all nearby hardware is
 * already identified — the Wizard is a read-only mirror of this state.
 *
 * Design Decisions:
 *  - ScanMode.LowPower (not LowLatency) — battery safety
 *  - Raw MAC tracking in Ref, NOT React state — prevents render thrashing
 *  - Debounced setAllDevices — fires at most every 1500ms on new discovery
 *  - AppState kill — Sweeper stops the moment app backgrounds
 *  - Interrogator respects bleGateRef — yields to user actions
 *  - HW cache persisted to AsyncStorage (@sk8_hw_<mac>) — survives restarts
 *  - Fleet MACs skipped by Interrogator — already registered, no re-probe needed
 */

import AsyncStorage from '@react-native-async-storage/async-storage';
import { Buffer } from 'buffer';
import { useCallback, useEffect, useRef, useState } from 'react';
import { AppState, Platform } from 'react-native';
import type { Device } from 'react-native-ble-plx';
import { LOCAL_PRODUCT_CATALOG, getLocalProfileByPoints } from '../../constants/ProductCatalog';
import {
  ZENGGE_CHARACTERISTIC_UUID,
  ZENGGE_NOTIFY_UUID,
  ZENGGE_SERVICE_UUID,
  ZenggeProtocol,
} from '../../protocols/ZenggeProtocol';
import { AppLogger } from '../../services/AppLogger';
import type { PendingRegistration } from '../../types/dashboard.types';
import { acquireGattLock } from './useBLEGattMutex';

/** AsyncStorage key prefix for per-device HW cache */
const HW_CACHE_KEY = (mac: string) => `@sk8_hw_${mac.toUpperCase()}`;

/** Min RSSI to consider a device live (not an OS-cache ghost) */
const RSSI_THRESHOLD = -80;

/** How long to wait between debounced React state flushes (ms) */
const DEBOUNCE_MS = 1500;

/** Interrogator probe timeout (ms) — same as pingDevice */
const PROBE_TIMEOUT_MS = 3500;

/** How long to wait before starting a queued probe (ms) — lets user actions settle */
const PROBE_QUEUE_DELAY_MS = 2000;

export interface UseBLESweeperProps {
  bleManager: any;
  setAllDevices: React.Dispatch<React.SetStateAction<Device[]>>;
  setPendingRegistrations: React.Dispatch<React.SetStateAction<PendingRegistration[]>>;
  /** The global BLE connection gate from useBLE — Interrogator checks this before probing */
  bleGateRef: React.MutableRefObject<string>;
  /** Registered Fleet MACs (uppercase) — Interrogator skips these, they're already known */
  registeredMacs: string[];
}

export interface UseBLESweeperReturn {
  isSweeperActive: boolean;
  startSweeper(): void;
  stopSweeper(): void;
  /** Elevate to LowLatency scan for a short burst, then revert to LowPower. Use instead of startDeviceScan() directly. */
  burstScan(durationMs?: number): void;
  /** In-memory HW cache — keyed by uppercase MAC, value is EEPROM hwConfig */
  hwCache: Record<string, any>;
}

export function useBLESweeper({
  bleManager,
  setAllDevices,
  setPendingRegistrations,
  bleGateRef,
  registeredMacs,
}: UseBLESweeperProps): UseBLESweeperReturn {
  const [isSweeperActive, setIsSweeperActive] = useState(false);

  // ── Raw state (Ref-only — never triggers React renders directly) ────────
  /** All MACs the Sweeper has seen this session (deduplication) */
  const seenMacsRef = useRef<Set<string>>(new Set());
  /** Staged devices waiting for the next debounce flush to React state */
  const pendingStagedRef = useRef<Device[]>([]);
  /** Debounce timer for React state flush */
  const debounceTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  /** MACs currently being probed by the Interrogator (prevent duplicate probes) */
  const probingMacsRef = useRef<Set<string>>(new Set());
  /** Probe queue — MACs waiting for Interrogator attention */
  const probeQueueRef = useRef<string[]>([]);
  /** Probe queue processor timer */
  const probeQueueTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  /** Tracks whether sweep is running (for AppState listener) */
  const isSweeperActiveRef = useRef(false);

  // ── In-memory HW cache (React state so consumers re-render on enrichment) ─
  const [hwCache, setHwCache] = useState<Record<string, any>>({});
  const hwCacheRef = useRef<Record<string, any>>({});

  // ── Boot: load persisted HW cache from AsyncStorage ─────────────────────
  useEffect(() => {
    if (Platform.OS === 'web') return;
    AsyncStorage.getAllKeys().then(keys => {
      const hwKeys = keys.filter(k => k.startsWith('@sk8_hw_'));
      if (hwKeys.length === 0) return;
      AsyncStorage.multiGet(hwKeys).then(pairs => {
        const loaded: Record<string, any> = {};
        for (const [key, val] of pairs) {
          if (!val) continue;
          try {
            const mac = key.replace('@sk8_hw_', '').toUpperCase();
            loaded[mac] = JSON.parse(val);
          } catch (e) {}
        }
        if (Object.keys(loaded).length > 0) {
          hwCacheRef.current = { ...hwCacheRef.current, ...loaded };
          setHwCache(prev => ({ ...prev, ...loaded }));
          AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_cache_loaded', count: Object.keys(loaded).length });
        }
      });
    }).catch(() => {});
  }, []);

  // ── Flush staged devices to React state (debounced) ─────────────────────
  const flushStagedDevices = useCallback(() => {
    const staged = [...pendingStagedRef.current];
    pendingStagedRef.current = [];
    if (staged.length === 0) return;

    setAllDevices(prev => {
      const merged = [...prev];
      for (const d of staged) {
        const alreadyIn = merged.some(p => p.id === d.id);
        if (!alreadyIn) merged.push(d);
      }
      return merged;
    });

    // Classify for pendingRegistrations (Setup Wizard card list)
    setAllDevices(current => {
      classifyForRegistrations(current);
      return current;
    });
  }, [setAllDevices]);

  const scheduleFlush = useCallback(() => {
    if (debounceTimerRef.current) clearTimeout(debounceTimerRef.current);
    debounceTimerRef.current = setTimeout(flushStagedDevices, DEBOUNCE_MS);
  }, [flushStagedDevices]);

  // ── Classify all known devices into pendingRegistrations ─────────────────
  const classifyForRegistrations = useCallback((devices: Device[]) => {
    if (devices.length === 0) return;
    const results: PendingRegistration[] = [];

    for (const d of devices) {
      const mac = d.id.toUpperCase();
      const cached = hwCacheRef.current[mac];
      const profile = getLocalProfileByPoints(cached?.ledPoints ?? (d as any).hwPoints ?? 0);
      const deviceIdShort = mac.replace(/:/g, '').slice(-4);

      results.push({
        device_mac:    mac,
        device_name:   `SK8Lytz-${deviceIdShort}`,
        factory_name:  d.name || 'Unknown',
        manufacturer_data: (d as any).manufacturerData,
        ble_version:   (d as any).bleVersion,
        product_type:  (cached ? profile.id : ((d as any).product_type || 'UNKNOWN')) as any,
        position:      results.length % 2 === 0 ? 'Left' : 'Right',
        group_name:    profile.id,
        // Use cached EEPROM data if available, otherwise fall back to advertisement/profile defaults
        led_points:    cached?.ledPoints   ?? (d as any).hwPoints ?? profile.vizDefaultPoints,
        segments:      cached?.segments    ?? (d as any).hwSegments ?? profile.defaultSegments,
        ic_type:       cached?.icName      ?? (d as any).hwStripType ?? (profile.defaultIcType === 1 ? 'WS2812B' : 'SM16703'),
        color_sorting: cached?.colorSortingName ?? (d as any).hwSorting ?? (profile.defaultColorSorting === 2 ? 'GRB' : 'RGB'),
        rssi:          d.rssi ?? -99,
        firmware_ver:  (d as any).firmwareVer,
        led_version:   (d as any).ledVersion,
        product_id:    (d as any).productId,
        rf_mode:       cached?.rfMode,
        rf_paired_count: cached?.rfPairedCount,
      });
    }

    if (results.length > 0) {
      setPendingRegistrations(prev => {
        // Merge: update existing entries, append new ones
        const merged = [...prev];
        for (const r of results) {
          const idx = merged.findIndex(p => p.device_mac === r.device_mac);
          if (idx >= 0) {
            merged[idx] = { ...merged[idx], ...r };
          } else {
            merged.push(r);
          }
        }
        return merged;
      });
    }
  }, [setPendingRegistrations]);

  // ── Interrogator: silently probe a single device EEPROM ─────────────────
  const interrogateDevice = useCallback(async (mac: string) => {
    if (Platform.OS === 'web' || !bleManager) return;
    if (probingMacsRef.current.has(mac)) return; // already probing
    if (hwCacheRef.current[mac]) return; // already cached

    // Skip registered Fleet devices — they're already known
    if (registeredMacs.map(m => m.toUpperCase()).includes(mac.toUpperCase())) return;

    // Yield to user actions — respect Traffic Cop
    const release = await acquireGattLock(2);
    if (!release) {
      AppLogger.log('BLE_STATE_CHANGE', { event: 'interrogator_yield_p1_lock', mac });
      probeQueueRef.current.push(mac); // re-queue for later
      return;
    }

    probingMacsRef.current.add(mac);
    AppLogger.log('BLE_STATE_CHANGE', { event: 'interrogator_start', mac });

    try {
      await bleManager.connectToDevice(mac, { timeout: 6000 }).catch((e: any) => {
        if (!String(e).includes('already')) throw e;
      });
      await bleManager.discoverAllServicesAndCharacteristicsForDevice(mac);

      const hwConfig = await new Promise<any>(resolve => {
        let accumulated: any = null;
        const timer = setTimeout(() => {
          sub.remove();
          resolve(accumulated);
        }, PROBE_TIMEOUT_MS);

        const sub = bleManager.monitorCharacteristicForDevice(
          mac, ZENGGE_SERVICE_UUID, ZENGGE_NOTIFY_UUID,
          (err: any, char: any) => {
            if (err || !char?.value) return;
            try {
              const raw = Array.from(Buffer.from(char.value, 'base64')) as number[];
              const hwParsed = ZenggeProtocol.parseHardwareSettingsResponse(raw);
              if (hwParsed) accumulated = { ...accumulated, ...hwParsed };
              const rfParsed = ZenggeProtocol.parseRfRemoteState(raw);
              if (rfParsed) accumulated = { ...accumulated, rfMode: rfParsed.mode, rfPairedCount: rfParsed.pairedCount };
              if (accumulated?.detected && accumulated?.rfMode) {
                clearTimeout(timer);
                sub.remove();
                resolve(accumulated);
              }
            } catch (e) {}
          }
        );

        // Fire HW query after 400ms (let monitor settle)
        setTimeout(() => {
          const b64HW = Buffer.from(ZenggeProtocol.queryHardwareSettings(false)).toString('base64');
          bleManager.writeCharacteristicWithoutResponseForDevice(
            mac, ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64HW
          ).catch(() => {});
          setTimeout(() => {
            const b64RF = Buffer.from(ZenggeProtocol.queryRfRemoteState()).toString('base64');
            bleManager.writeCharacteristicWithoutResponseForDevice(
              mac, ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64RF
            ).catch(() => {});
          }, 200);
        }, 400);
      });

      if (hwConfig) {
        // Persist to AsyncStorage
        AsyncStorage.setItem(HW_CACHE_KEY(mac), JSON.stringify(hwConfig)).catch(() => {});
        // Update in-memory cache and React state
        hwCacheRef.current[mac] = hwConfig;
        setHwCache(prev => ({ ...prev, [mac]: hwConfig }));
        AppLogger.log('DEVICE_DISCOVERED', { event: 'interrogator_complete', mac, ledPoints: hwConfig.ledPoints });

        // Re-classify with enriched data
        setAllDevices(current => {
          classifyForRegistrations(current);
          return current;
        });
      }
    } catch (err: any) {
      AppLogger.warn(`[Sweeper] Interrogator failed for ${mac}`, { error: String(err) });
    } finally {
      probingMacsRef.current.delete(mac);
      release();
      await bleManager.cancelDeviceConnection(mac).catch(() => {});
    }
  }, [bleManager, registeredMacs, classifyForRegistrations]);

  // ── Probe queue processor ───────────────────────────────────────────────
  const processProbeQueue = useCallback(() => {
    if (probeQueueTimerRef.current) clearTimeout(probeQueueTimerRef.current);
    probeQueueTimerRef.current = setTimeout(async () => {
      while (probeQueueRef.current.length > 0) {
        const mac = probeQueueRef.current.shift()!;
        await interrogateDevice(mac);
        await new Promise(r => setTimeout(r, 500)); // breathe between probes
      }
    }, PROBE_QUEUE_DELAY_MS);
  }, [interrogateDevice]);

  // ── Start Sweeper ────────────────────────────────────────────────────────
  const startSweeper = useCallback(() => {
    if (Platform.OS === 'web' || !bleManager) return;
    if (isSweeperActiveRef.current) return; // already running

    // ── CRITICAL: Stop any existing scan before starting a new one ─────────
    // Android/iOS only support ONE active startDeviceScan() at a time.
    // A prior scan (e.g. from useBLEScanner.scanForPeripherals) must be
    // killed before we can start the LowPower sweep. Calling start without
    // stopping first silently orphans the previous callback.
    bleManager.stopDeviceScan();

    isSweeperActiveRef.current = true;
    setIsSweeperActive(true);
    AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_start' });

    bleManager.startDeviceScan(
      null,
      { scanMode: 0 }, // ScanMode.LowPower — battery safe
      (error: any, device: any) => {
        if (error) {
          AppLogger.warn('[Sweeper] Scan error', { error: String(error) });
          return;
        }
        if (!device) return;

        // ── Same filter logic as useBLEScanner ─────────────────────────
        const nameLower = device.name?.toLowerCase() || '';
        const hasZenggeService = device.serviceUUIDs?.includes(ZENGGE_SERVICE_UUID);
        const mfData = device.manufacturerData;
        let isSymphony = false;
        if (mfData) {
          try {
            const buf = Buffer.from(mfData, 'base64');
            if (buf.length > 9 && (buf[9] === 0x33 || buf[9] === 0xBF)) isSymphony = true;
          } catch (e) {}
        }
        const isKnownPrefix = nameLower.startsWith('lednet') || nameLower.startsWith('sk8') ||
          nameLower.startsWith('zg') || nameLower.startsWith('halo') || nameLower.startsWith('soul');
        const isMatch = isSymphony || isKnownPrefix || hasZenggeService;
        if (!isMatch) return;

        // RSSI ghost gate
        const rssi = device.rssi ?? -99;
        if (rssi < RSSI_THRESHOLD) return;

        const mac = device.id.toUpperCase();

        // New device — stage for debounced React flush
        if (!seenMacsRef.current.has(mac)) {
          seenMacsRef.current.add(mac);
          pendingStagedRef.current.push(device);
          scheduleFlush();

          // Queue for Interrogator if not already cached or being probed
          if (!hwCacheRef.current[mac] && !probingMacsRef.current.has(mac)) {
            probeQueueRef.current.push(mac);
            processProbeQueue();
          }
        }
      }
    );
  }, [bleManager, scheduleFlush, processProbeQueue]);

  /**
   * burstScan — Temporarily elevate to LowLatency scan for `durationMs`, then revert to LowPower.
   *
   * This is what scanForPeripherals() delegates to when the Sweeper is active,
   * preventing the dual-scan conflict where two concurrent startDeviceScan() calls
   * stomp on each other. One scan loop feeds all consumers.
   *
   * @param durationMs  Duration of the LowLatency burst. Default: 5000ms.
   */
  const burstScan = useCallback((durationMs: number = 5000) => {
    if (Platform.OS === 'web' || !bleManager) return;
    AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_burst_start', durationMs });

    // Stop existing LowPower sweep
    bleManager.stopDeviceScan();
    isSweeperActiveRef.current = false;

    // Start LowLatency burst (same callback as startSweeper — shared consumer)
    bleManager.startDeviceScan(
      null,
      { scanMode: 2 }, // ScanMode.LowLatency — aggressive, short burst
      (error: any, device: any) => {
        if (error || !device) return;
        const nameLower = device.name?.toLowerCase() || '';
        const hasZenggeService = device.serviceUUIDs?.includes(ZENGGE_SERVICE_UUID);
        const mfData = device.manufacturerData;
        let isSymphony = false;
        if (mfData) {
          try {
            const buf = Buffer.from(mfData, 'base64');
            if (buf.length > 9 && (buf[9] === 0x33 || buf[9] === 0xBF)) isSymphony = true;
          } catch (e) {}
        }
        const isKnownPrefix = nameLower.startsWith('lednet') || nameLower.startsWith('sk8') ||
          nameLower.startsWith('zg') || nameLower.startsWith('halo') || nameLower.startsWith('soul');
        if (!isSymphony && !isKnownPrefix && !hasZenggeService) return;
        const rssi = device.rssi ?? -99;
        if (rssi < RSSI_THRESHOLD) return;
        const mac = device.id.toUpperCase();
        if (!seenMacsRef.current.has(mac)) {
          seenMacsRef.current.add(mac);
          pendingStagedRef.current.push(device);
          scheduleFlush();
          if (!hwCacheRef.current[mac] && !probingMacsRef.current.has(mac)) {
            probeQueueRef.current.push(mac);
            processProbeQueue();
          }
        }
      }
    );

    // After burst, revert to LowPower sweep
    setTimeout(() => {
      AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_burst_end_revert' });
      startSweeper();
    }, durationMs);
  }, [bleManager, scheduleFlush, processProbeQueue, startSweeper]);

  // ── Stop Sweeper ─────────────────────────────────────────────────────────
  const stopSweeper = useCallback(() => {
    if (!isSweeperActiveRef.current) return;
    bleManager?.stopDeviceScan();
    isSweeperActiveRef.current = false;
    setIsSweeperActive(false);
    if (debounceTimerRef.current) clearTimeout(debounceTimerRef.current);
    if (probeQueueTimerRef.current) clearTimeout(probeQueueTimerRef.current);
    AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_stop' });
  }, [bleManager]);

  // ── AppState kill switch (battery safety) ────────────────────────────────
  useEffect(() => {
    if (Platform.OS === 'web') return;
    const sub = AppState.addEventListener('change', nextState => {
      if (nextState === 'background' || nextState === 'inactive') {
        stopSweeper();
      }
      // Note: Resume is managed by DashboardScreen via startSweeper() on 'active'
    });
    return () => sub.remove();
  }, [stopSweeper]);

  // ── Cleanup on unmount ───────────────────────────────────────────────────
  useEffect(() => {
    return () => { stopSweeper(); };
  }, [stopSweeper]);

  return { isSweeperActive, startSweeper, stopSweeper, burstScan, hwCache };
}
