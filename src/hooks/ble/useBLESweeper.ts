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
import { ZENGGE_SERVICE_UUID } from '../../protocols/ZenggeProtocol';
import { BANLANX_SERVICE_UUID } from '../../protocols/BanlanxAdapter';
import { resolveProtocol, getDefaultProtocol, getProtocolById } from '../../protocols/ControllerRegistry';
import type { IControllerProtocol } from '../../protocols/IControllerProtocol';
import { AppLogger } from '../../services/AppLogger';
import { BleCharacteristicCache } from '../../services/BleCharacteristicCache';
import type { PendingRegistration } from '../../types/dashboard.types';
import { mapDeviceToRegistration } from '../../utils/classifyBLEDevice';
import { acquireGattLock } from './useBLEGattMutex';

/** AsyncStorage key prefix for per-device HW cache */
const HW_CACHE_KEY = (mac: string) => `@sk8_hw_${mac.toUpperCase()}`;

/** Min RSSI to consider a device live (not an OS-cache ghost) */
const RSSI_THRESHOLD = -80;

/** How long to wait between debounced React state flushes (ms) */
const DEBOUNCE_MS = 1500;
/** Faster flush during FTUE (no registered devices) — devices appear sooner in wizard */
const DEBOUNCE_MS_FTUE = 800;

/** Interrogator probe timeout (ms) — same as pingDevice */
const PROBE_TIMEOUT_MS = 3500;

/** How long to wait before starting a queued probe (ms) — lets user actions settle */
const PROBE_QUEUE_DELAY_MS = 2000;
/** Faster probe during FTUE — no user BLE actions to interfere with, radio is idle */
const PROBE_QUEUE_DELAY_MS_FTUE = 500;

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
  burstScan(durationMs?: number): Promise<void>;
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
  const lastSeenRef = useRef<Map<string, number>>(new Map()); // MAC → last-seen epoch ms
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
          const mac = key.replace('@sk8_hw_', '').toUpperCase();
          try {
            loaded[mac] = JSON.parse(val);
          } catch (e) {
            AppLogger.warn('[useBLESweeper] Malformed HW cache', { mac, error: String(e) });
          }
        }
        if (Object.keys(loaded).length > 0) {
          hwCacheRef.current = { ...hwCacheRef.current, ...loaded };
          setHwCache(prev => ({ ...prev, ...loaded }));
          AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_cache_loaded', count: Object.keys(loaded).length });
        }
      });
    }).catch(e => AppLogger.warn('[useBLESweeper] Failed to load sweeper hardware cache', { error: String(e) }));
  }, []);

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

  // ── Flush staged devices to React state (debounced) ─────────────────────
  const flushStagedDevices = useCallback(() => {
    const staged = [...pendingStagedRef.current];
    pendingStagedRef.current = [];
    if (staged.length === 0) return;

    const staleThreshold = Date.now() - 15000;

    setAllDevices(prev => {
      // Prune stale devices (not seen in >15s) then merge new ones in.
      // This replaces the destructive setAllDevices([]) wipe in burstScan —
      // existing devices stay visible until they go cold, preventing the
      // group-tap-finds-nothing race condition.
      const live = prev.filter(d => {
        const lastSeen = lastSeenRef.current.get((d.id || '').toUpperCase());
        return lastSeen !== undefined && lastSeen > staleThreshold;
      });
      const merged = [...live];
      for (const d of staged) {
        if (!merged.some(p => p.id === d.id)) merged.push(d);
      }
      return merged;
    });

    // [NEW] Instantly populate the Wizard UI before background interrogation finishes
    classifyForRegistrations(staged);
  }, [setAllDevices, classifyForRegistrations]);

  const scheduleFlush = useCallback(() => {
    if (debounceTimerRef.current) clearTimeout(debounceTimerRef.current);
    // FTUE mode: shorter debounce so devices appear faster in the wizard
    const delay = registeredMacs.length === 0 ? DEBOUNCE_MS_FTUE : DEBOUNCE_MS;
    debounceTimerRef.current = setTimeout(flushStagedDevices, delay);
  }, [flushStagedDevices, registeredMacs.length]);

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

      const conn = await bleManager.connectToDevice(mac, { timeout: 6000 }).catch((e: any) => {
        if (!String(e).includes('already')) throw e;
        return bleManager.deviceForDevice(mac);
      });

      // ── P1 preemption check: bail after connect, before service discovery ─
      if (signal.aborted) {
        AppLogger.log('BLE_STATE_CHANGE', { event: 'interrogator_preempted_post_connect', mac });
        return;
      }

      // ── HAL: Resolve adapter from service UUIDs & Caching ─────────────────
      let interrogatorAdapter: IControllerProtocol | null = null;
      let usedCache = false;

      const cachedGatt = await BleCharacteristicCache.get(mac);
      if (cachedGatt) {
        interrogatorAdapter = getProtocolById(cachedGatt.protocolId);
        if (interrogatorAdapter) usedCache = true;
      }

      if (!interrogatorAdapter) {
        await conn.discoverAllServicesAndCharacteristics();
        try {
          const svcs = await conn.services();
          const svcUUIDs = svcs.map((s: any) => s.uuid as string);
          interrogatorAdapter = resolveProtocol(svcUUIDs) ?? getDefaultProtocol();
        } catch (_e) {
          interrogatorAdapter = getDefaultProtocol();
        }
        await BleCharacteristicCache.set(mac, interrogatorAdapter.protocolId);
      } else {
        AppLogger.log('BLE_STATE_CHANGE', { event: 'gatt_cache_hit', context: 'interrogateDevice', mac });
      }

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
          mac, interrogatorAdapter.serviceUUID, interrogatorAdapter.notifyCharacteristicUUID,
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
              const hwParsed = interrogatorAdapter.parseSettingsResponse(raw);
              if (hwParsed) accumulated = { ...accumulated, ...hwParsed };
              const rfParsed = interrogatorAdapter.parseRfRemoteState(raw);
              if (rfParsed) accumulated = { ...accumulated, rfMode: rfParsed.mode, rfPairedCount: rfParsed.pairedCount };
              if (accumulated?.detected && accumulated?.rfMode) {
                clearTimeout(timer);
                sub.remove();
                resolve(accumulated);
              }
            } catch (e) {
              AppLogger.warn('[useBLESweeper] Protocol parse failed', { mac, error: String(e) });
            }
          }
        );

        setTimeout(() => {
          if (signal.aborted) { clearTimeout(timer); sub.remove(); resolve(null); return; }
          const hwQuery = interrogatorAdapter.buildQuerySettings(false);
          if (hwQuery.packets.length > 0) {
            const b64HW = Buffer.from(hwQuery.packets[0]).toString('base64');
            bleManager.writeCharacteristicWithoutResponseForDevice(
              mac, interrogatorAdapter.serviceUUID, interrogatorAdapter.writeCharacteristicUUID, b64HW
            ).catch((e: any) => AppLogger.warn('[useBLESweeper] Interrogator HW query failed', { error: String(e) }));
          }
          setTimeout(() => {
            if (signal.aborted) return;
            const rfQuery = interrogatorAdapter.buildQueryRfRemoteState();
            if (rfQuery.packets.length > 0) {
              const b64RF = Buffer.from(rfQuery.packets[0]).toString('base64');
              bleManager.writeCharacteristicWithoutResponseForDevice(
                mac, interrogatorAdapter.serviceUUID, interrogatorAdapter.writeCharacteristicUUID, b64RF
              ).catch((e: any) => AppLogger.warn('[useBLESweeper] Interrogator RF query failed', { error: String(e) }));
            }
          }, 200);
        }, 400);
      });

      if (hwConfig && !signal.aborted) {
        AsyncStorage.setItem(HW_CACHE_KEY(mac), JSON.stringify(hwConfig)).catch(e => AppLogger.warn('[useBLESweeper] Failed to cache interrogator hw config', { error: String(e) }));
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
      await bleManager.cancelDeviceConnection(mac).catch((e: any) => AppLogger.warn('[useBLESweeper] Interrogator disconnect failed', { error: String(e) }));
    }
  }, [bleManager, registeredMacs, classifyForRegistrations]);

  // ── Probe queue processor ───────────────────────────────────────────────
  const processProbeQueue = useCallback(() => {
    if (probeQueueTimerRef.current) clearTimeout(probeQueueTimerRef.current);
    // FTUE mode: shorter delay — no user BLE actions to wait for, radio is idle
    const delay = registeredMacs.length === 0 ? PROBE_QUEUE_DELAY_MS_FTUE : PROBE_QUEUE_DELAY_MS;
    probeQueueTimerRef.current = setTimeout(async () => {
      while (probeQueueRef.current.length > 0) {
        const mac = probeQueueRef.current.shift()!;
        await interrogateDevice(mac);
        await new Promise(r => setTimeout(r, 500));
      }
    }, delay);
  }, [interrogateDevice, registeredMacs.length]);

  // ── Shared scan callback (used by both startSweeper and burstScan) ───────
  const createScanCallback = useCallback(() => {
    return (error: any, device: any) => {
      if (error) { AppLogger.warn('[Sweeper] Scan error', { error: String(error) }); return; }
      if (!device) return;

      const nameLower = device.name?.toLowerCase() || '';
      const hasZenggeService = device.serviceUUIDs?.includes(ZENGGE_SERVICE_UUID);
      const hasBanlanxService = device.serviceUUIDs?.includes(BANLANX_SERVICE_UUID);
      const mfData = device.manufacturerData;
      let isSymphony = false;
      if (mfData) {
        try {
          const buf = Buffer.from(mfData, 'base64');
          if (buf.length > 9 && (buf[9] === 0x33 || buf[9] === 0xBF)) isSymphony = true;
        } catch (e) {}
      }
      const isKnownPrefix = ZENGGE_NAME_PREFIXES.some(p => nameLower.startsWith(p));
      if (!isSymphony && !isKnownPrefix && !hasZenggeService && !hasBanlanxService) return;

      const rssi = device.rssi ?? -99;
      if (rssi < RSSI_THRESHOLD) return;

      const mac = device.id.toUpperCase();
      // Always stamp last-seen time regardless of whether the MAC is new.
      // flushStagedDevices uses this to prune devices cold for >15s.
      lastSeenRef.current.set(mac, Date.now());
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
  const burstScan = useCallback((durationMs: number = 5000): Promise<void> => {
    return new Promise((resolve) => {
      if (Platform.OS === 'web' || !bleManager) {
        resolve();
        return;
      }
      AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_burst_start', durationMs });

      // Stop existing sweep
      bleManager.stopDeviceScan();
      isSweeperActiveRef.current = false;

      // FIX: Do NOT wipe allDevices or seenMacs on burst.
      // Instead, staleness-based pruning in flushStagedDevices removes devices
      // that haven't been heard in >15s. This eliminates the race condition where
      // group tap fires connectToDevices([]) because the list was just cleared.
      // seenMacsRef is cleared ONLY so devices re-heard during burst get re-staged
      // (otherwise the dedup gate would silently drop them as "already seen").
      seenMacsRef.current = new Set();
      // NOTE: lastSeenRef is preserved — timestamps from the previous low-power sweep
      // ensure devices that burst can't hear yet aren't pruned prematurely.

      bleManager.startDeviceScan(null, { scanMode: 2 }, createScanCallback());

      burstTimerRef.current = setTimeout(() => {
        burstTimerRef.current = null;
        AppLogger.log('BLE_STATE_CHANGE', { event: 'sweeper_burst_end_revert' });
        startSweeper();
        resolve();
      }, durationMs);
    });
  }, [bleManager, createScanCallback, startSweeper]);

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
