import AsyncStorage from '@react-native-async-storage/async-storage';
import { Buffer } from 'buffer';
import React, { useCallback, useEffect, useRef, useState } from 'react';
import { InteractionManager } from 'react-native';
import type { Device, BleManager, BleError } from 'react-native-ble-plx';
import { ZENGGE_SERVICE_UUID, ZenggeProtocol } from '../../protocols/ZenggeProtocol';
import { BANLANX_SERVICE_UUID } from '../../protocols/BanlanxAdapter';
import { AppLogger } from '../../services/AppLogger';
import { supabase } from '../../services/supabaseClient';
import { locationService } from '../../services/LocationService';
import type { PendingRegistration } from '../../types/dashboard.types';
import { mapDeviceToRegistration } from '../../utils/classifyBLEDevice';
import type { Database } from '../../types/supabase';
import type { EventFrom } from 'xstate';
import type { bleMachine } from '../../services/ble/BleMachine';
import { STORAGE_SCANNER_TELEMETRY_QUEUE } from '../../constants/storageKeys';

import { useBLEBatterySweep } from './useBLEBatterySweep';
import { useBLEInterrogator } from './useBLEInterrogator';

type TelemetryInsert = Database['public']['Tables']['discovered_devices_telemetry']['Insert'];

const ZENGGE_NAME_PREFIXES = ['lednet', 'sk8', 'zg', 'halo', 'soul'];
const RSSI_THRESHOLD = -80;
const DEBOUNCE_MS = 1500;
const DEBOUNCE_MS_FTUE = 800;

export interface UseBLEScannerProps {
  bleManager: BleManager | null;
  allDevices: Device[];
  setAllDevices: React.Dispatch<React.SetStateAction<Device[]>>;
  bleSend: (event: EventFrom<typeof bleMachine>) => void;
  registeredMacs: string[];
}

export function useBLEScanner({
  bleManager,
  allDevices,
  setAllDevices,
  bleSend,
  registeredMacs,
}: UseBLEScannerProps) {
  const [pendingRegistrations, setPendingRegistrations] = useState<PendingRegistration[]>([]);

  const allDevicesRef = useRef<Device[]>([]);
  const scannerStateRef = useRef<'IDLE' | 'SCANNING'>('IDLE');
  const rejectedMacsRef = useRef<Set<string>>(new Set());
  const setupRssiThresholdRef = useRef<number>(-70);

  const seenMacsRef = useRef<Set<string>>(new Set());
  const lastSeenRef = useRef<Map<string, number>>(new Map());
  const pendingStagedRef = useRef<Device[]>([]);
  const debounceTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  useEffect(() => {
    AsyncStorage.getItem('@sk8lytz_app_settings').then(cached => {
      if (cached) {
        try {
          const parsed = JSON.parse(cached);
          if (parsed.hw_setup_rssi_threshold !== undefined) {
             setupRssiThresholdRef.current = parseInt(String(parsed.hw_setup_rssi_threshold), 10);
          }
        } catch {}
      }
    });
  }, []);

  const telemetryCacheRef = useRef<Map<string, number>>(new Map());
  const telemetryBatchRef = useRef<Record<string, unknown>[]>([]);
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
        
        payloads = batch.map((d: Record<string, unknown>) => ({
          device_mac: String(d.id),
          rssi: Number(d.rssi),
          product_type: String(d.type),
          manufacturer_data: d.manufacturerData ? String(d.manufacturerData) : null,
          firmware_ver: d.firmwareVer ? Number(d.firmwareVer) : null,
          ble_version: d.bleVersion ? Number(d.bleVersion) : null,
          product_id: d.productId ? Number(d.productId) : null,
          led_version: d.ledVersion ? Number(d.ledVersion) : null,
          location: locString
        })) as unknown as TelemetryInsert[];

        const { error } = await supabase.from('discovered_devices_telemetry').insert(payloads as TelemetryInsert[]);
        if (error) throw new Error(error.message);
      } catch (_e: unknown) {
        AppLogger.warn('[Scanner] Ambient telemetry flush failed', { error: (_e instanceof Error ? _e.message : String(_e)) });
        if (payloads.length > 0) {
          try {
            const raw = await AsyncStorage.getItem(STORAGE_SCANNER_TELEMETRY_QUEUE);
            const queue = raw ? JSON.parse(raw) : [];
            queue.push(...payloads);
            await AsyncStorage.setItem(STORAGE_SCANNER_TELEMETRY_QUEUE, JSON.stringify(queue));
          } catch (storageErr: unknown) {
            AppLogger.warn('[Scanner] Failed to enqueue telemetry offline', { error: (storageErr instanceof Error ? storageErr.message : String(storageErr)) });
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
        if (!merged.some(p => p.id === d.id)) merged.push(d);
      }
      return merged;
    });

    classifyProbeResults(staged);
  }, [setAllDevices, classifyProbeResults]);

  const scheduleFlush = useCallback(() => {
    if (debounceTimerRef.current) clearTimeout(debounceTimerRef.current);
    const delay = registeredMacs.length === 0 ? DEBOUNCE_MS_FTUE : DEBOUNCE_MS;
    debounceTimerRef.current = setTimeout(flushStagedDevices, delay);
  }, [flushStagedDevices, registeredMacs.length]);

  const scanCallback = useCallback((error: BleError | null, device: Device | null) => {
    if (error) { AppLogger.warn('[Sweeper] Scan error', { error: (error instanceof Error ? error.message : String(error)) }); return; }
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
      } catch (e: unknown) { AppLogger.warn('[useBLEScanner] Failed parsing manufacturerData base64', e instanceof Error ? e.message : String(e)); }
    }

    const isKnownPrefix = ZENGGE_NAME_PREFIXES.some(p => nameLower.startsWith(p));
    if (!isSymphony && !isKnownPrefix && !hasZenggeService && !hasBanlanxService && !hasFcf1Service) {
      if (!rejectedMacsRef.current.has(device.id)) {
        rejectedMacsRef.current.add(device.id);
        if (device.name || (device.serviceUUIDs && device.serviceUUIDs.length > 0)) {
          AppLogger.log('SCAN_FILTER_REJECT', { id: device.id, name: device.name, reason: 'No matching signature' });
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
        AppLogger.log('SCAN_FILTER_REJECT', { id: device.id, reason: `RSSI too low (${deviceRssi} < ${targetThreshold})` });
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
        } catch (e: unknown) { AppLogger.warn('[Scanner] Failed to parse firmware', { mac, error: e instanceof Error ? e.message : String(e) }); }
      }

      const now = Date.now();
      const lastSeenTel = telemetryCacheRef.current.get(mac);
      if (!lastSeenTel || now - lastSeenTel > 30 * 60 * 1000) {
        telemetryCacheRef.current.set(mac, now);
        telemetryBatchRef.current.push({
           id: mac, rssi: deviceRssi,
           type: nameLower.includes('halo') ? 'HALOZ' : (nameLower.includes('soul') ? 'SOULZ' : 'UNKNOWN'),
           manufacturerData: mfData || null, firmwareVer: firmwareVer || null, bleVersion: bleVersion || null,
           productId: productId || null, ledVersion: ledVersion || null
        });
        if (telemetryBatchRef.current.length >= 25) {
           flushTelemetry();
        } else if (!telemetryTimerRef.current) {
           telemetryTimerRef.current = setTimeout(() => { flushTelemetry(); telemetryTimerRef.current = null; }, 60000);
        }
      }

      pendingStagedRef.current.push(device);
      scheduleFlush();
      queueDeviceForInterrogation(mac);
    }
  }, [scheduleFlush, queueDeviceForInterrogation, hwCacheRef]);

  const { isSweeperActive, startSweeper, stopSweeper: _stopSweeper, burstScan: _burstScan } = useBLEBatterySweep({
    bleManager,
    scanCallback
  });

  const stopScanner = useCallback(() => {
    _stopSweeper();
    bleSend({ type: 'SCAN_STOP' });
    scannerStateRef.current = 'IDLE';
  }, [_stopSweeper, bleSend]);

  const burstScan = useCallback((durationMs?: number) => {
    seenMacsRef.current = new Set();
    _burstScan(durationMs, () => {
      bleSend({ type: 'SCAN_START' });
      scannerStateRef.current = 'SCANNING';
    }).then(() => {
      bleSend({ type: 'SCAN_STOP' });
      scannerStateRef.current = 'IDLE';
    });
  }, [_burstScan, bleSend]);

  const scanForPeripherals = useCallback((options?: { keepAlive?: boolean }) => {
    if (!options?.keepAlive) {
      setPendingRegistrations([]);
      rejectedMacsRef.current.clear();
      setAllDevices([]);
      allDevicesRef.current = [];
    }
    
    if (isSweeperActive) {
      burstScan(options?.keepAlive ? 10000 : 5000);
    } else {
      bleSend({ type: 'SCAN_START' });
      scannerStateRef.current = 'SCANNING';
      bleManager?.startDeviceScan(null, null, scanCallback);
      setTimeout(() => {
        bleManager?.stopDeviceScan();
        bleSend({ type: 'SCAN_STOP' });
        scannerStateRef.current = 'IDLE';
      }, 5000);
    }
  }, [isSweeperActive, burstScan, bleManager, scanCallback, bleSend, setAllDevices]);

  return {
    pendingRegistrations,
    scanForPeripherals,
    stopScanner,
    setPendingRegistrations,
    classifyProbeResults,
    isSweeperActive,
    startSweeper,
    burstScan,
    hwCache
  };
}
