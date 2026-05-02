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
 *  - AppState lifecycle owned by DashboardScreen (no duplicate listener here)
 *  - Interrogator respects bleGateRef AND checks AbortSignal for P1 preemption
 *  - HW cache persisted to AsyncStorage (@sk8_hw_<mac>) — survives restarts
 *  - Fleet MACs skipped by Interrogator — already registered, no re-probe needed
 *  - burstScan() resets seenMacsRef to clear ghost device accumulation
 */

import AsyncStorage from '@react-native-async-storage/async-storage';
import { Buffer } from 'buffer';
import { useCallback, useEffect, useRef, useState } from 'react';
import { Platform } from 'react-native';
import type { Device } from 'react-native-ble-plx';
import {
  ZENGGE_CHARACTERISTIC_UUID,
  ZENGGE_NOTIFY_UUID,
  ZENGGE_SERVICE_UUID,
  ZenggeProtocol,
} from '../../protocols/ZenggeProtocol';
import { AppLogger } from '../../services/AppLogger';
import type { PendingRegistration } from '../../types/dashboard.types';
import { mapDeviceToRegistration } from '../../utils/classifyBLEDevice';
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

/** BLE device name prefixes that identify SK8Lytz/ZENGGE hardware */
const ZENGGE_NAME_PREFIXES = ['lednet', 'sk8', 'zg', 'halo', 'soul'];

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
  /** Elevate to LowLatency burst scan for durationMs, then revert to LowPower.
   *  Also resets the seenMacs set to clear stale/ghost device accumulation. */
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
  const seenMacsRef = useRef<Set<string>>(new Set());
  const pendingStagedRef = useRef<Device[]>([]);
  const debounceTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const probingMacsRef = useRef<Set<string>>(new Set());
  const probeQueueRef = useRef<string[]>([]);
  const probeQueueTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const burstTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const isSweeperActiveRef = useRef(false);

  // ── In-memory HW cache ──────────────────────────────────────────────────
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
        if (!merged.some(p => p.id === d.id)) merged.push(d);
      }
      return merged;
    });
  }, [setAllDevices]);

  const scheduleFlush = useCallback(() => {
    if (debounceTimerRef.current) clearTimeout(debounceTimerRef.current);
    debounceTimerRef.current = setTimeout(flushStagedDevices, DEBOUNCE_MS);
  }, [flushStagedDevices]);

  // ── Classify discovered devices into pendingRegistrations ────────────────
  // Uses shared classifyBLEDevice utility — single source of truth.
  const classifyForRegistrations = useCallback((devices: Device[]) => {
    if (devices.length === 0) return;
    const results = devices.map((d, i) =>
      mapDeviceToRegistration(d, i, hwCacheRef.current)
    );

    setPendingRegistrations(prev => {
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
  }, [setPendingRegistrations]);

  // ── Interrogator: silently probe a single device EEPROM ─────────────────
  const interrogateDevice = useCallback(async (mac: string) => {
    if (Platform.OS === 'web' || !bleManager) return;
    if (probingMacsRef.current.has(mac)) return;
    if (hwCacheRef.current[mac]) return;

    // Skip registered Fleet devices
    if (registeredMacs.map(m => m.toUpperCase()).includes(mac.toUpperCase())) return;

    // ── Acquire GATT lock (P2) — gets AbortSignal for P1 preemption ────────
    const lockHandle = await acquireGattLock(2);
    if (!lockHandle) {
      AppLogger.log('BLE_STATE_CHANGE', { event: 'interrogator_yield_p1_lock', mac });
      if (!probeQueueRef.current.includes(mac)) probeQueueRef.current.push(mac); // re-queue (deduped)
      return;
    }

    const { release, signal } = lockHandle;
    probingMacsRef.current.add(mac);
    AppLogger.log('BLE_STATE_CHANGE', { event: 'interrogator_start', mac });

    try {
      // ── P1 preemption check: bail immediately if aborted ────────────────
      if (signal.aborted) {
        AppLogger.log('BLE_STATE_CHANGE', { event: 'interrogator_preempted_pre_connect', mac });
        return;
      }

      await bleManager.connectToDevice(mac, { timeout: 6000 }).catch((e: any) => {
        if (!String(e).includes('already')) throw e;
      });

      // ── P1 preemption check: bail after connect, before service discovery ─
      if (signal.aborted) {
        AppLogger.log('BLE_STATE_CHANGE', { event: 'interrogator_preempted_post_connect', mac });
        return;
      }

      await bleManager.discoverAllServicesAndCharacteristicsForDevice(mac);

      if (signal.aborted) {
        AppLogger.log('BLE_STATE_CHANGE', { event: 'interrogator_preempted_post_discover', mac });
        return;
      }

      const hwConfig = await new Promise<any>(resolve => {
        let accumulated: any = null;
        const timer = setTimeout(() => {
          sub.remove();
          resolve(accumulated);
        }, PROBE_TIMEOUT_MS);

        const sub = bleManager.monitorCharacteristicForDevice(
          mac, ZENGGE_SERVICE_UUID, ZENGGE_NOTIFY_UUID,
          (err: any, char: any) => {
            // ── P1 preemption check inside notification handler ───────────
            if (signal.aborted) {
              clearTimeout(timer);
              sub.remove();
              resolve(null);
              return;
            }
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

        setTimeout(() => {
          if (signal.aborted) { clearTimeout(timer); sub.remove(); resolve(null); return; }
          const b64HW = Buffer.from(ZenggeProtocol.queryHardwareSettings(false)).toString('base64');
          bleManager.writeCharacteristicWithoutResponseForDevice(
            mac, ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64HW
          ).catch(() => {});
          setTimeout(() => {
            if (signal.aborted) return;
            const b64RF = Buffer.from(ZenggeProtocol.queryRfRemoteState()).toString('base64');
            bleManager.writeCharacteristicWithoutResponseForDevice(
              mac, ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64RF
            ).catch(() => {});
          }, 200);
        }, 400);
      });

      if (hwConfig && !signal.aborted) {
        AsyncStorage.setItem(HW_CACHE_KEY(mac), JSON.stringify(hwConfig)).catch(() => {});
        hwCacheRef.current[mac] = hwConfig;
        setHwCache(prev => ({ ...prev, [mac]: hwConfig }));
        AppLogger.log('DEVICE_DISCOVERED', { event: 'interrogator_complete', mac, ledPoints: hwConfig.ledPoints });

        // Re-classify with enriched EEPROM data
        setAllDevices(current => {
          classifyForRegistrations(current);
          return current;
        });
      }
    } catch (err: any) {
      if (!signal.aborted) {
        AppLogger.warn(`[Sweeper] Interrogator failed for ${mac}`, { error: String(err) });
      }
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
        await new Promise(r => setTimeout(r, 500));
      }
    }, PROBE_QUEUE_DELAY_MS);
  }, [interrogateDevice]);

  // ── Shared scan callback (used by both startSweeper and burstScan) ───────
  const createScanCallback = useCallback(() => {
    return (error: any, device: any) => {
      if (error) { AppLogger.warn('[Sweeper] Scan error', { error: String(error) }); return; }
      if (!device) return;

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
      const isKnownPrefix = ZENGGE_NAME_PREFIXES.some(p => nameLower.startsWith(p));
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
    };
  }, [scheduleFlush, processProbeQueue]);

  // ── Start Sweeper ────────────────────────────────────────────────────────
  const startSweeper = useCallback(() => {
    if (Platform.OS === 'web' || !bleManager) return;
    if (isSweeperActiveRef.current) return;

    // Cancel any pending burst-revert timer to prevent double-start
    if (burstTimerRef.current) { clearTimeout(burstTimerRef.current); burstTimerRef.current = null; }

    // CRITICAL: Stop any existing scan before starting (Android single-scan constraint)
    bleManager.stopDeviceScan();

    isSweeperActiveRef.current = true;
    setIsSweeperActive(true);
    AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_start' });

    bleManager.startDeviceScan(null, { scanMode: 0 }, createScanCallback());
  }, [bleManager, createScanCallback]);

  /**
   * burstScan — Temporarily elevate to LowLatency scan for `durationMs`, then revert to LowPower.
   *
   * Also resets seenMacsRef to clear stale/ghost device accumulation from previous session.
   * This ensures powered-off devices don't persist indefinitely in allDevices.
   *
   * One scan loop, all consumers — prevents dual startDeviceScan() conflict.
   */
  const burstScan = useCallback((durationMs: number = 5000) => {
    if (Platform.OS === 'web' || !bleManager) return;
    AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_burst_start', durationMs });

    // Stop existing sweep
    bleManager.stopDeviceScan();
    isSweeperActiveRef.current = false;

    // ── Reset seenMacs to clear stale ghost devices ─────────────────────────
    // Without this, a powered-off skate's MAC stays in seenMacsRef forever.
    // Fresh devices seen during the burst re-populate the list cleanly.
    seenMacsRef.current = new Set();
    setAllDevices([]);

    bleManager.startDeviceScan(null, { scanMode: 2 }, createScanCallback());

    burstTimerRef.current = setTimeout(() => {
      burstTimerRef.current = null;
      AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_burst_end_revert' });
      startSweeper();
    }, durationMs);
  }, [bleManager, createScanCallback, startSweeper, setAllDevices]);

  // ── Stop Sweeper ─────────────────────────────────────────────────────────
  const stopSweeper = useCallback(() => {
    if (!isSweeperActiveRef.current) return;
    bleManager?.stopDeviceScan();
    isSweeperActiveRef.current = false;
    setIsSweeperActive(false);
    if (debounceTimerRef.current) clearTimeout(debounceTimerRef.current);
    if (probeQueueTimerRef.current) clearTimeout(probeQueueTimerRef.current);
    // FIX: Cancel pending burst-revert timer to prevent radio restart while app is backgrounded
    if (burstTimerRef.current) { clearTimeout(burstTimerRef.current); burstTimerRef.current = null; }
    AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_stop' });
  }, [bleManager]);

  // NOTE: AppState listener is intentionally NOT here.
  // DashboardScreen owns the lifecycle: startSweeper on foreground, stopSweeper on background.
  // Having two listeners calling stopSweeper() was a duplicate responsibility violation.

  // ── Cleanup on unmount ───────────────────────────────────────────────────
  useEffect(() => {
    return () => { stopSweeper(); };
  }, [stopSweeper]);

  return { isSweeperActive, startSweeper, stopSweeper, burstScan, hwCache };
}
