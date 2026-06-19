import AsyncStorage from '@react-native-async-storage/async-storage';
import { Buffer } from 'buffer';
import React, { useCallback, useEffect, useRef, useState } from 'react';
import { InteractionManager, Platform } from 'react-native';
import type { Device, BleManager, BleError } from 'react-native-ble-plx';
import { ZENGGE_SERVICE_UUID, ZenggeProtocol } from '../../protocols/ZenggeProtocol';
import { BANLANX_SERVICE_UUID } from '../../protocols/BanlanxAdapter';
import { AppLogger } from '../../services/appLogger';
import { supabase } from '../../services/supabaseClient';
import { locationService } from '../../services/LocationService';
import type { PendingRegistration } from '../../types/dashboard.types';
import { mapDeviceToRegistration } from '../../utils/classifyBLEDevice';
import type { Database } from '../../types/supabase';
import type { EventFrom } from 'xstate';
import type { bleMachine } from '../../services/ble/BleMachine';
import { STORAGE_SCANNER_TELEMETRY_QUEUE, STORAGE_APP_SETTINGS } from '../../constants/storageKeys';
import { scrubPII } from '../../utils/piiScrubber';
import { BLE_TIMING } from '../../constants/bleTimingConstants';

import { useBLEBatterySweep } from './useBLEBatterySweep';
import { useBLEInterrogator } from './useBLEInterrogator';

type TelemetryInsert = Database['public']['Tables']['discovered_devices_telemetry']['Insert'];

const ZENGGE_NAME_PREFIXES = ['lednet', 'sk8', 'zg', 'halo', 'soul'];
const RSSI_THRESHOLD = -80;

function determineFactoryName(device: Device): string | undefined {
  const FFD5_UUID = '0000ffd5-0000-1000-8000-00805f9b34fb';
  if (device.serviceUUIDs?.includes(ZENGGE_SERVICE_UUID) || device.serviceUUIDs?.includes(FFD5_UUID)) {
    return 'Zengge';
  }
  if (device.serviceUUIDs?.includes(BANLANX_SERVICE_UUID)) {
    if (device.manufacturerData) {
      try {
        const buf = Buffer.from(device.manufacturerData, 'base64');
        if (buf.toString('hex').includes('5053')) return 'BanlanX';
      } catch (e: unknown) { AppLogger.error('[useBLEScanner] Failed to parse manufacturerData base64 in determineFactoryName', e instanceof Error ? e.message : String(e), { payload_size: 0, ssi: 0 }); }
    }
    return 'BanlanX';
  }
  return undefined;
}

export interface UseBLEScannerProps {
  bleManager: BleManager | null;
  allDevices: Device[];
  setAllDevices: React.Dispatch<React.SetStateAction<Device[]>>;
  bleSend: (event: EventFrom<typeof bleMachine>) => void;
  registeredMacs: string[];
  isSandboxEnabled?: boolean;
}

export function useBLEScanner({
  bleManager,
  allDevices,
  setAllDevices,
  bleSend,
  registeredMacs,
  isSandboxEnabled,
}: UseBLEScannerProps) {
  const [pendingRegistrations, setPendingRegistrations] = useState<PendingRegistration[]>([]);

  const allDevicesRef = useRef<Device[]>([]);
  const rejectedMacsRef = useRef<Set<string>>(new Set());
  const setupRssiThresholdRef = useRef<number>(-70);

  const seenMacsRef = useRef<Set<string>>(new Set());
  const lastSeenRef = useRef<Map<string, number>>(new Map());
  const pendingStagedRef = useRef<Device[]>([]);
  // Scan stage debounce timer (not GATT write timing) — intentionally preserved per R-16
  const debounceTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  useEffect(() => {
    // R-24: Shared global settings AsyncStorage key read.
    // Multiple modules read distinct settings properties from this central key.
    AsyncStorage.getItem(STORAGE_APP_SETTINGS).then(cached => {
      if (cached) {
        try {
          const parsed = JSON.parse(cached);
          if (parsed.hw_setup_rssi_threshold !== undefined) {
             setupRssiThresholdRef.current = parseInt(String(parsed.hw_setup_rssi_threshold), 10);
          }
        } catch (e: unknown) { AppLogger.error('[useBLEScanner] Failed to parse cached RSSI threshold from AsyncStorage JSON', e instanceof Error ? e.message : String(e), { payload_size: 0, ssi: 0 }); }
      }
    }).catch((e: unknown) => {
      AppLogger.warn('[useBLEScanner] AsyncStorage.getItem failed', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
    });

    const flushOfflineTelemetry = async () => {
      try {
        if (!supabase) return;
        const raw = await AsyncStorage.getItem(STORAGE_SCANNER_TELEMETRY_QUEUE);
        if (!raw) return;
        const queue: TelemetryInsert[] = JSON.parse(raw);
        if (queue.length > 0) {
          const { error } = await supabase.from('discovered_devices_telemetry').insert(queue);
          if (error) throw new Error(error.message);
          await AsyncStorage.removeItem(STORAGE_SCANNER_TELEMETRY_QUEUE);
        }
      } catch (e: unknown) {
        AppLogger.warn('[Scanner] Failed to flush offline telemetry queue', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
      }
    };
    // Flush offline queue shortly after mount
    setTimeout(() => {
      InteractionManager.runAfterInteractions(flushOfflineTelemetry);
    }, BLE_TIMING.SCAN_OFFLINE_FLUSH_DEFER_MS);
  }, []);

  const telemetryCacheRef = useRef<Map<string, number>>(new Map());
  const telemetryBatchRef = useRef<Record<string, unknown>[]>([]);
  // Ambient telemetry flush timer (not GATT write timing) — intentionally preserved per R-16
  const telemetryTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  const flushTelemetry = () => {
    if (telemetryBatchRef.current.length === 0) return;
    const batch = [...telemetryBatchRef.current];
    telemetryBatchRef.current = [];

    InteractionManager.runAfterInteractions(async () => {
      let payloads: TelemetryInsert[] = [];
      try {
        if (!supabase) return;
        let locString = null;
        const loc = await locationService.getSilentLocation();
        if (loc) {
          locString = `SRID=4326;POINT(${loc.lng} ${loc.lat})`;
        }
        
        payloads = batch.map((d: Record<string, unknown>): TelemetryInsert => ({
          device_mac: String(d.id),
          rssi: Number(d.rssi),
          product_type: String(d.type),
          manufacturer_data: d.manufacturerData ? String(d.manufacturerData) : null,
          firmware_ver: d.firmwareVer ? Number(d.firmwareVer) : null,
          ble_version: d.bleVersion ? Number(d.bleVersion) : null,
          product_id: d.productId ? Number(d.productId) : null,
          led_version: d.ledVersion ? Number(d.ledVersion) : null,
          location: locString,
          is_claimed: Boolean(d.is_claimed)
        }));

        const { error } = await supabase.from('discovered_devices_telemetry').insert(payloads as TelemetryInsert[]);
        if (error) throw new Error(error.message);
      } catch (_e: unknown) {
        AppLogger.warn('[Scanner] Ambient telemetry flush failed', { error: (_e instanceof Error ? _e.message : String(_e)), payload_size: payloads.length, ssi: 0 });
        if (payloads.length > 0) {
          try {
            const raw = await AsyncStorage.getItem(STORAGE_SCANNER_TELEMETRY_QUEUE);
            const queue = raw ? JSON.parse(raw) : [];
            queue.push(...payloads);
            await AsyncStorage.setItem(STORAGE_SCANNER_TELEMETRY_QUEUE, JSON.stringify(queue));
          } catch (storageErr: unknown) {
            AppLogger.warn('[Scanner] Failed to enqueue telemetry offline', { error: (storageErr instanceof Error ? storageErr.message : String(storageErr)), payload_size: payloads.length, ssi: 0 });
          }
        }
      }
    });
  };

  useEffect(() => { allDevicesRef.current = allDevices; }, [allDevices]);

  const { hwCache, hwCacheRef, queueDeviceForInterrogation } = useBLEInterrogator({
    bleManager,
    registeredMacs,
    onDeviceInterrogated: () => {
      setAllDevices(current => {
        classifyProbeResults(current);
        return current;
      });
    }
  });

  const classifyProbeResults = useCallback((forceList?: Device[]) => {
    const devices = forceList || allDevicesRef.current;
    if (devices.length === 0) return;

    const results: PendingRegistration[] = devices.map((d, i) =>
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
  }, [hwCacheRef]);

  const flushStagedDevices = useCallback(() => {
    const staged = [...pendingStagedRef.current];
    pendingStagedRef.current = [];
    if (staged.length === 0) return;

    const staleThreshold = Date.now() - 15000;

    setAllDevices(prev => {
      const live = prev.filter(d => {
        const lastSeen = lastSeenRef.current.get((d.id || '').toUpperCase());
        return lastSeen !== undefined && lastSeen > staleThreshold;
      });
      const merged = [...live];
      for (const d of staged) {
        const existingIdx = merged.findIndex(p => p.id === d.id);
        if (existingIdx >= 0) {
          merged[existingIdx] = d;  // Upsert: replace stale Device with fresh BLE handle
        } else {
          merged.push(d);
        }
      }
      return merged;
    });

    classifyProbeResults(staged);
  }, [setAllDevices, classifyProbeResults]);

  const scheduleFlush = useCallback(() => {
    if (debounceTimerRef.current) clearTimeout(debounceTimerRef.current);
    const delay = registeredMacs.length === 0 ? BLE_TIMING.SCAN_DEBOUNCE_MS_FTUE : BLE_TIMING.SCAN_DEBOUNCE_MS;
    // Debounce timer for staged devices (not GATT write timing) — intentionally preserved per R-16
    debounceTimerRef.current = setTimeout(flushStagedDevices, delay);
  }, [flushStagedDevices, registeredMacs.length]);

  const scanCallback = useCallback((error: BleError | null, device: Device | null) => {
    if (error) { AppLogger.warn('[Sweeper] Scan error', { error: (error instanceof Error ? error.message : String(error)), payload_size: 0, ssi: 0 }); return; }
    if (!device) return;

    const nameLower = device.name?.toLowerCase() || '';
    const hasZenggeService = device.serviceUUIDs?.includes(ZENGGE_SERVICE_UUID);
    const hasBanlanxService = device.serviceUUIDs?.includes(BANLANX_SERVICE_UUID);
    const FCF1_UUID = '0000fcf1-0000-1000-8000-00805f9b34fb';
    const fcf1Data = device.serviceData ? device.serviceData[FCF1_UUID] : null;
    const hasFcf1Service = !!fcf1Data;

    const mfData = device.manufacturerData || fcf1Data;
    let isSymphony = false;
    
    if (mfData) {
      try {
        const buf = Buffer.from(mfData, 'base64');
        if (buf.length > 9 && (buf[9] === 0x33 || buf[9] === 0xBF)) isSymphony = true;
      } catch (e: unknown) { AppLogger.warn('[useBLEScanner] Failed parsing manufacturerData base64', { error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 }); }
    }

    const isKnownPrefix = ZENGGE_NAME_PREFIXES.some(p => nameLower.startsWith(p));
    if (!isSymphony && !isKnownPrefix && !hasZenggeService && !hasBanlanxService && !hasFcf1Service) {
      if (!rejectedMacsRef.current.has(device.id)) {
        rejectedMacsRef.current.add(device.id);
        if (__DEV__) {
          AppLogger.log('SCAN_FILTER_REJECT', { id: '[REDACTED]', name: '[REDACTED]', reason: 'No matching signature' });
        }
      }
      return;
    }

    const deviceRssi = device.rssi ?? -99;
    const isRegistered = hwCacheRef.current[device.id.toUpperCase()] !== undefined;
    const targetThreshold = isRegistered ? RSSI_THRESHOLD : setupRssiThresholdRef.current;
    
    if (deviceRssi < targetThreshold) {
      if (!rejectedMacsRef.current.has(device.id)) {
        rejectedMacsRef.current.add(device.id);
        AppLogger.log('SCAN_FILTER_REJECT', { id: '[REDACTED]', reason: `RSSI too low (${deviceRssi} < ${targetThreshold})` });
      }
      return;
    }

    const mac = device.id.toUpperCase();
    lastSeenRef.current.set(mac, Date.now());

    if (!seenMacsRef.current.has(mac)) {
      seenMacsRef.current.add(mac);
      let firmwareVer, ledVersion, bleVersion, productId;
      if (mfData) {
        try {
          const fwInfo = ZenggeProtocol.parseFirmwareFromAdvertisement(mfData);
          if (fwInfo) {
            firmwareVer = fwInfo.firmwareVer;
            ledVersion = fwInfo.ledVersion;
            bleVersion = fwInfo.bleVersion;
            productId = fwInfo.productId;
            Object.assign(device, {
              firmware: `v${fwInfo.firmwareVer}.${fwInfo.ledVersion} (BLE ${fwInfo.bleVersion})`,
              firmwareVer, ledVersion, bleVersion, productId
            });
          }
        } catch (e: unknown) { AppLogger.warn('[Scanner] Failed to parse firmware', { mac: scrubPII(mac), error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 }); }
      }

      Object.assign(device, { factoryName: determineFactoryName(device) });

      const now = Date.now();
      const lastSeenTel = telemetryCacheRef.current.get(mac);
      if (!lastSeenTel || now - lastSeenTel > 30 * 60 * 1000) {
        telemetryCacheRef.current.set(mac, now);
        telemetryBatchRef.current.push({
           id: mac, rssi: deviceRssi,
           type: nameLower.includes('halo') ? 'HALOZ' : (nameLower.includes('soul') ? 'SOULZ' : 'UNKNOWN'),
           manufacturerData: mfData || null, firmwareVer: firmwareVer || null, bleVersion: bleVersion || null,
           productId: productId || null, ledVersion: ledVersion || null, is_claimed: isRegistered
        });
        if (telemetryBatchRef.current.length >= 25) {
           flushTelemetry();
        } else if (!telemetryTimerRef.current) {
           // Telemetry batch flush timer (not GATT write timing) — intentionally preserved per R-16
           telemetryTimerRef.current = setTimeout(() => { flushTelemetry(); telemetryTimerRef.current = null; }, BLE_TIMING.SCAN_TELEMETRY_FLUSH_MS);
        }
      }

      pendingStagedRef.current.push(device);
      scheduleFlush();
      queueDeviceForInterrogation(mac);
    } else {
      // Already-seen MAC: re-stage Device to refresh stale BLE peripheral handle in allDevices.
      // Telemetry, firmware parse, and GATT interrogation are NOT re-triggered — only the
      // Device object reference is refreshed so group card taps get a valid GATT handle.
      pendingStagedRef.current.push(device);
      scheduleFlush();
    }
  }, [scheduleFlush, queueDeviceForInterrogation, hwCacheRef]);

  const { isSweeperActive, startSweeper, stopSweeper: _stopSweeper, burstScan: _burstScan, batteryTier } = useBLEBatterySweep({
    bleManager,
    bleSend
  });

  const stopScanner = useCallback(() => {
    _stopSweeper();
  }, [_stopSweeper]);

  const burstScan = useCallback((durationMs?: number) => {
    seenMacsRef.current = new Set();
    _burstScan(durationMs);
  }, [_burstScan]);

  const scanForPeripherals = useCallback((options?: { keepAlive?: boolean }) => {
    if (!options?.keepAlive) {
      setPendingRegistrations([]);
      rejectedMacsRef.current.clear();
      seenMacsRef.current.clear();
      setAllDevices([]);
      allDevicesRef.current = [];
    }

    const isSandboxMocking = ((typeof __DEV__ !== 'undefined' && __DEV__) || Platform.OS === 'web') && isSandboxEnabled;

    if (isSandboxMocking) {
      bleSend({ type: 'SCAN_START' });

      // Sandbox mock device discovery timer (not GATT write timing) — intentionally preserved per R-16
      setTimeout(() => {
        const halozL = { id: 'VIRTUAL-HALOZ-L', name: 'SK8-HALOZ-L-DEV', rssi: -50, manufacturerData: 'MwHwMwEBKwE=', serviceUUIDs: [ZENGGE_SERVICE_UUID], product_type: 'HALOZ', hwPoints: 10 } as Partial<Device> & { product_type: string; hwPoints: number } as Device;
        const halozR = { id: 'VIRTUAL-HALOZ-R', name: 'SK8-HALOZ-R-DEV', rssi: -52, manufacturerData: 'MwHwMwEBKwE=', serviceUUIDs: [ZENGGE_SERVICE_UUID], product_type: 'HALOZ', hwPoints: 10 } as Partial<Device> & { product_type: string; hwPoints: number } as Device;
        const soulzL = { id: 'VIRTUAL-SOULZ-L', name: 'SK8-SOULZ-L-DEV', rssi: -55, manufacturerData: 'MwHwMwEBKwE=', serviceUUIDs: [ZENGGE_SERVICE_UUID], product_type: 'SOULZ', hwPoints: 43 } as Partial<Device> & { product_type: string; hwPoints: number } as Device;
        const soulzR = { id: 'VIRTUAL-SOULZ-R', name: 'SK8-SOULZ-R-DEV', rssi: -57, manufacturerData: 'MwHwMwEBKwE=', serviceUUIDs: [ZENGGE_SERVICE_UUID], product_type: 'SOULZ', hwPoints: 43 } as Partial<Device> & { product_type: string; hwPoints: number } as Device;

        scanCallback(null, halozL);
        scanCallback(null, halozR);
        scanCallback(null, soulzL);
        scanCallback(null, soulzR);
      }, BLE_TIMING.SANDBOX_MOCK_DISCOVERY_DELAY_MS);

      // Sandbox mock scan stop timer (not GATT write timing) — intentionally preserved per R-16
      setTimeout(() => {
        bleSend({ type: 'SCAN_STOP' });
      }, BLE_TIMING.SANDBOX_MOCK_SCAN_STOP_MS);

      if (Platform.OS === 'web') return; // Early return for web so it doesn't hit native sweeper
    }

    // FTUE path: no registered devices — bypass isSweeperActive race.
    // startSweeper() is async (Battery.getBatteryLevelAsync) so isSweeperActive
    // is still false when the wizard calls scanForPeripherals() on mount.
    // Route through startSweeper() directly — it is idempotent (early-returns
    // if already running) and runs persistently until hardware is found.
    if (registeredMacs.length === 0 && !options?.keepAlive) {
      AppLogger.log('BLE_STATE_CHANGE', { event: 'ftue_persistent_scan_start' });
      startSweeper();
      return;
    }

    if (isSweeperActive) {
      burstScan(options?.keepAlive ? 10000 : 5000);
    } else if (!isSandboxMocking) {
      bleSend({ type: 'SCAN_START' });
      // Non-sweeper manual scan timeout fallback (not GATT write timing) — intentionally preserved per R-16
      setTimeout(() => {
        bleSend({ type: 'SCAN_STOP' });
      }, BLE_TIMING.MANUAL_SCAN_TIMEOUT_MS);
    }
  }, [registeredMacs.length, startSweeper, isSweeperActive, burstScan, scanCallback, bleSend, setAllDevices, isSandboxEnabled]);

  useEffect(() => {
    return () => {
      if (debounceTimerRef.current) clearTimeout(debounceTimerRef.current);
      if (telemetryTimerRef.current) clearTimeout(telemetryTimerRef.current);
    };
  }, []);

  return {
    pendingRegistrations,
    scanForPeripherals,
    stopScanner,
    setPendingRegistrations,
    classifyProbeResults,
    isSweeperActive,
    batteryTier,
    startSweeper,
    burstScan,
    hwCache,
    scanCallback
  };
}
