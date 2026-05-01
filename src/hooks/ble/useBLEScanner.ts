import AsyncStorage from '@react-native-async-storage/async-storage';
import { Buffer } from 'buffer';
import { useEffect, useRef, useState } from 'react';
import { Alert, Platform, InteractionManager } from 'react-native';
import type { Device } from 'react-native-ble-plx';
import { LOCAL_PRODUCT_CATALOG, getLocalProfileByPoints } from '../../constants/ProductCatalog';
import { ZENGGE_SERVICE_UUID, ZenggeProtocol } from '../../protocols/ZenggeProtocol';
import { AppLogger } from '../../services/AppLogger';
import { supabase } from '../../services/supabaseClient';
import { locationService } from '../../services/LocationService';
import type { PendingRegistration } from '../../types/dashboard.types';
import { getDefaultGroupName } from '../../utils/NamingUtils';

export interface UseBLEScannerProps {
  bleManager: any;
  allDevices: Device[];
  setAllDevices: React.Dispatch<React.SetStateAction<Device[]>>;
  probeDevice: (mac: string) => Promise<any>;
  hardwareProbedCallbackRef: React.MutableRefObject<((deviceId: string, config: any) => void) | undefined>;
  disableProbing?: boolean;
}

export function useBLEScanner({
  bleManager,
  allDevices,
  setAllDevices,
  probeDevice,
  hardwareProbedCallbackRef,
  disableProbing = true
}: UseBLEScannerProps) {
  const [scannerState, setScannerState] = useState<'IDLE' | 'SCANNING' | 'PROBING'>('IDLE');
  const [pendingRegistrations, setPendingRegistrations] = useState<PendingRegistration[]>([]);

  const allDevicesRef = useRef<Device[]>([]);
  const scanTimerRef = useRef<NodeJS.Timeout | null>(null);
  const scannerStateRef = useRef<'IDLE' | 'SCANNING' | 'PROBING'>('IDLE');
  const rejectedMacsRef = useRef<Set<string>>(new Set());

  const telemetryCacheRef = useRef<Map<string, number>>(new Map());
  const telemetryBatchRef = useRef<any[]>([]);
  const telemetryTimerRef = useRef<NodeJS.Timeout | null>(null);

  const knownMacsRef = useRef<Set<string>>(new Set());

  const flushTelemetry = () => {
    if (telemetryBatchRef.current.length === 0) return;
    const batch = [...telemetryBatchRef.current];
    telemetryBatchRef.current = [];

    InteractionManager.runAfterInteractions(async () => {
      try {
        if (!supabase) return;
        let locString = null;
        const loc = await locationService.getSilentLocation();
        if (loc) {
          locString = `SRID=4326;POINT(${loc.lng} ${loc.lat})`;
        }
        
        const payloads = batch.map(d => ({
          device_mac: d.id,
          rssi: d.rssi,
          product_type: d.type,
          manufacturer_data: d.manufacturerData,
          firmware_ver: d.firmwareVer,
          ble_version: d.bleVersion,
          product_id: d.productId,
          led_version: d.ledVersion,
          location: locString
        }));

        await supabase.from('discovered_devices_telemetry').insert(payloads);
      } catch (e) {
        AppLogger.warn('[Scanner] Ambient telemetry flush failed', { error: String(e) });
      }
    });
  };

  useEffect(() => { allDevicesRef.current = allDevices; }, [allDevices]);

  const isDuplicateDevice = (devices: Device[], nextDevice: Device) =>
    devices.findIndex(device => nextDevice.id === device.id) > -1;

  const classifyProbeResults = (forceList?: Device[]) => {
    const devices = forceList || allDevicesRef.current;
    if (devices.length === 0) return;

    const groups: Record<string, Device[]> = {};
    const unknown: Device[] = [];

    for (const d of devices) {
      const pts = (d as any).hwPoints as number | undefined;
      const name = d.name?.toUpperCase() || '';

      if (pts != null) {
        const profile = getLocalProfileByPoints(pts);
        const typeId = profile.id;
        if (!groups[typeId]) groups[typeId] = [];
        groups[typeId].push(d);
      } else {
        const matchedProfile = LOCAL_PRODUCT_CATALOG.find(p => {
          if (p.id === 'HALOZ' && name.includes('HALO')) return true;
          if (p.id === 'SOULZ' && name.includes('SOUL')) return true;
          return name.includes(p.id);
        });
        if (matchedProfile) {
          const tid = matchedProfile.id;
          if (!groups[tid]) groups[tid] = [];
          groups[tid].push(d);
        } else {
          unknown.push(d);
        }
      }
    }

    const mapToRegistration = (d: any, i: number, type: string): PendingRegistration => {
      const isUnknown = type === 'UNKNOWN';
      const pos = i % 2 === 0 ? 'Left' : 'Right';
      const profile = LOCAL_PRODUCT_CATALOG.find(p => p.id === type) || LOCAL_PRODUCT_CATALOG[0];

      const normalizedMac = d.id.toUpperCase();
      const deviceIdShort = normalizedMac.replace(/:/g, '').slice(-4);
      return {
        device_mac:   normalizedMac,
        device_name:  `SK8Lytz-${deviceIdShort}`,
        factory_name: d.name || 'Unknown',
        manufacturer_data: d.manufacturerData,
        ble_version:  d.bleVersion,
        product_type: type as any,
        position:     pos,
        group_name:   getDefaultGroupName(type),
        led_points:   d.hwPoints || profile.vizDefaultPoints,
        segments:     d.hwSegments ?? profile.defaultSegments,
        ic_type:      d.hwStripType ?? (profile.defaultIcType === 1 ? 'WS2812B' : 'SM16703'),
        color_sorting: d.hwSorting ?? (profile.defaultColorSorting === 2 ? 'GRB' : 'RGB'),
        rssi:         d.rssi ?? -99,
        firmware_ver: d.firmwareVer,
        led_version:  d.ledVersion,
        product_id:   d.productId,
        rf_mode:      d.hwRfMode,
        rf_paired_count: d.hwRfPairedCount,
      };
    };

    const results: PendingRegistration[] = [];
    LOCAL_PRODUCT_CATALOG.forEach(p => {
      if (groups[p.id]) {
        const sorted = [...groups[p.id]].sort((a,b) => (b.rssi ?? -99) - (a.rssi ?? -99));
        sorted.forEach((d, i) => results.push(mapToRegistration(d, i, p.id)));
      }
    });

    if (unknown.length > 0) {
      const sortedUnknown = [...unknown].sort((a,b) => (b.rssi ?? -99) - (a.rssi ?? -99));
      sortedUnknown.forEach((d, i) => results.push(mapToRegistration(d, i, 'UNKNOWN')));
    }

    if (results.length > 0) {
      AppLogger.warn(`[EMERGENCY-DEBUG] classifyProbeResults pushing ${results.length} devices to pendingRegistrations`);
      setPendingRegistrations(results);
    }
  };

  const probeAllDiscoveredDevices = async (retriesLeft = 3) => {
    if (Platform.OS === 'web' || !bleManager) return;
    const devices = allDevicesRef.current;
    
    const pending = devices.filter(d => (d as any).hwPoints == null);
    if (pending.length === 0) {
      setScannerState('IDLE');
      scannerStateRef.current = 'IDLE';
      return;
    }

    setScannerState('PROBING');
    scannerStateRef.current = 'PROBING';
    AppLogger.log('DEVICE_DISCOVERED', { context: 'probe_round_start', pendingCount: pending.length, retriesLeft });

    const failedIds: string[] = [];

    // Parallel probe with a concurrency cap of 2.
    // iOS/Android GATT adapters can handle 2 simultaneous connections reliably;
    // more than that causes timeouts and auth failures on most chipsets.
    const CONCURRENCY = 2;
    let aborted = false;

    const probeOne = async (device: Device): Promise<void> => {
      if (scannerStateRef.current !== 'PROBING') {
        aborted = true;
        return;
      }
      try {
        const alreadyConn = await bleManager.isDeviceConnected(device.id).catch(() => false);
        const hasHwInfo = (device as any).hwPoints != null;
        
        // --- PASSIVE TELEMETRY BYPASS ---
        // If we don't have HW info, try to reconstruct it from the registry cache.
        if (!hasHwInfo && knownMacsRef.current.has(device.id)) {
           try {
              const cachedStr = await AsyncStorage.getItem('@Sk8lytz_registered_devices');
              if (cachedStr) {
                 const cachedList = JSON.parse(cachedStr);
                 const cachedData = cachedList.find((d: any) => d.device_mac === device.id);
                 if (cachedData) {
                    (device as any).hwPoints = cachedData.led_points;
                    (device as any).hwSegments = cachedData.segments;
                    (device as any).hwStripType = cachedData.ic_type;
                    (device as any).hwSorting = cachedData.color_sorting;
                    AppLogger.log('DEVICE_DISCOVERED', { context: 'restored_passive', deviceId: device.id });
                    setAllDevices([...allDevicesRef.current]);
                    classifyProbeResults([...allDevicesRef.current]);
                    return; // BYPASS active probe
                 }
              }
           } catch(e) {}
        }

        if (alreadyConn && ((device as any).hwPoints != null)) return;

        AppLogger.log('DEVICE_DISCOVERED', { context: 'probe_start', deviceName: device.name || device.id });
        
        const hwConfig = await probeDevice(device.id);
        if (hwConfig) {
          (device as any).hwPoints = hwConfig.ledPoints;
          (device as any).hwSegments = hwConfig.segments;
          (device as any).hwSorting = hwConfig.colorSortingName;
          (device as any).hwStripType = hwConfig.icName;
          (device as any).hwRfMode = hwConfig.rfMode;
          (device as any).hwRfPairedCount = hwConfig.rfPairedCount;

          if (hardwareProbedCallbackRef.current) {
            hardwareProbedCallbackRef.current(device.id, hwConfig);
          }
          setAllDevices([...allDevicesRef.current]);
          classifyProbeResults([...allDevicesRef.current]);
        } else {
          AppLogger.warn(`[BLE Scanner] Device ${device.id} silently failed to yield telemetry`);
          failedIds.push(device.id);
        }
      } catch (probeErr: any) {
        AppLogger.warn(`[BLE Probe] Hard crash on ${device.id}:`, probeErr?.message);
        failedIds.push(device.id);
        try { await bleManager.cancelDeviceConnection(device.id); } catch (e) {
          AppLogger.warn('[Scanner] Failed to cancel probe connection', { deviceId: device.id, error: String(e) });
        }
      }
    };

    // Process in batches of CONCURRENCY
    for (let i = 0; i < pending.length; i += CONCURRENCY) {
      if (aborted || scannerStateRef.current !== 'PROBING') break;
      const batch = pending.slice(i, i + CONCURRENCY);
      await Promise.all(batch.map(probeOne));
      // Inter-batch cooldown: let the GATT adapter breathe
      if (i + CONCURRENCY < pending.length) {
        await new Promise(r => setTimeout(r, 400));
      }
    }

    if (aborted) return;

    if (failedIds.length > 0 && retriesLeft > 0) {
       AppLogger.log('DEVICE_DISCOVERED', { context: 'probe_retry', failedCount: failedIds.length, delayMs: 2000 });
       await new Promise(r => setTimeout(r, 2000));
       if (scannerStateRef.current === 'PROBING') {
         return probeAllDiscoveredDevices(retriesLeft - 1);
       } else {
         AppLogger.log('DEVICE_DISCOVERED', { context: 'probe_aborted', reason: 'retry_cooldown' });
         return;
       }
    }

    setScannerState('IDLE');
    scannerStateRef.current = 'IDLE';
    AppLogger.log('DEVICE_DISCOVERED', { context: 'probe_all_complete' });
  };


  const stopScanner = () => {
    bleManager?.stopDeviceScan();
    if (scanTimerRef.current) {
      clearTimeout(scanTimerRef.current);
      scanTimerRef.current = null;
    }
    setScannerState('IDLE');
    scannerStateRef.current = 'IDLE';
  };

  const scanForPeripherals = (options?: { keepAlive?: boolean, disableProbing?: boolean }) => {
    if (scannerStateRef.current === 'SCANNING' || scannerStateRef.current === 'PROBING') return;
    setScannerState('SCANNING');
    scannerStateRef.current = 'SCANNING';
    
    if (!options?.keepAlive) {
      setPendingRegistrations([]);
      rejectedMacsRef.current.clear();
      // ── GHOST BUST: Flush stale OS-cached device records from the previous scan session.
      // Android/iOS BLE stacks re-broadcast cached advertisement data the instant a new scan
      // begins. Without this flush, powered-off devices that were seen in a prior session
      // appear immediately in the device list as if they are live and nearby.
      // The keepAlive path (Setup Wizard continuous polling) is intentionally exempt —
      // it must retain devices discovered earlier in the same wizard session.
      setAllDevices([]);
      allDevicesRef.current = [];
    }

    const skipProbing = options?.disableProbing ?? disableProbing;

    knownMacsRef.current.clear();

    AsyncStorage.getItem('@Sk8lytz_registered_devices').then(cached => {
      if (cached) {
        try {
          JSON.parse(cached).forEach((d: any) => knownMacsRef.current.add(d.device_mac));
        } catch (e) { AppLogger.warn('[Scanner] Failed to parse registered_devices cache', { error: String(e) }); }
      }
    }).catch(() => {});

    if (__DEV__) {
      AsyncStorage.getItem('@Sk8lytz_demo_mode').then((isMock) => {
        if (Platform.OS === 'web' || isMock === 'true') {
          AppLogger.log('DEVICE_DISCOVERED', { context: 'sandbox_mode_inject' });
          setTimeout(() => {
            const mockDevices = [
              { id: 'sim-DE:M0:HA:L0:00:01', name: 'HALOZ Left Skate', type: 'HALOZ', points: 11, segments: 2, sorting: 'GRB', stripType: 'WS2812B', rssi: -45, serviceUUIDs: [ZENGGE_SERVICE_UUID], manufacturerData: 'AAAAAAAAAAAz' },
              { id: 'sim-DE:M0:HA:L0:00:02', name: 'HALOZ Right Skate', type: 'HALOZ', points: 11, segments: 2, sorting: 'GRB', stripType: 'WS2812B', rssi: -55, serviceUUIDs: [ZENGGE_SERVICE_UUID], manufacturerData: 'AAAAAAAAAAAz' },
              { id: 'sim-DE:M0:S0:UL:00:01', name: 'SOULZ Left Skate', type: 'SOULZ', points: 43, segments: 1, sorting: 'GRB', stripType: 'WS2812B', rssi: -42, serviceUUIDs: [ZENGGE_SERVICE_UUID], manufacturerData: 'AAAAAAAAAAAz' },
              { id: 'sim-DE:M0:S0:UL:00:02', name: 'SOULZ Right Skate', type: 'SOULZ', points: 43, segments: 1, sorting: 'GRB', stripType: 'WS2812B', rssi: -85, serviceUUIDs: [ZENGGE_SERVICE_UUID], manufacturerData: 'AAAAAAAAAAAz' }
            ] as any[];
            setAllDevices(mockDevices);
            
            const pendingMocks = mockDevices.map(device => {
               AppLogger.log('DEVICE_DISCOVERED', { id: device.id, name: device.name, rssi: device.rssi, points: device.points });
               return {
                 device_mac: device.id,
                 device_name: device.name,
                 product_type: device.type,
                 position: device.name.includes('Left') ? 'Left' : 'Right',
                 group_name: '',
                 led_points: device.points ?? 0,
                 segments: device.segments ?? 1,
                 ic_type: device.stripType ?? 'WS2812B',
                 color_sorting: device.sorting ?? 'GRB',
                 rssi: device.rssi ?? -50,
                 firmware_ver: 200,
                 product_id: 163,
               } as PendingRegistration;
            });
            setPendingRegistrations(pendingMocks);
            setScannerState('IDLE');
            scannerStateRef.current = 'IDLE';
          }, 500);
        } else if (!bleManager) {
          setTimeout(() => {
            setScannerState('IDLE');
            scannerStateRef.current = 'IDLE';
          }, 500);
        }
      });
      if (Platform.OS === 'web') return;
    } else {
      if (!bleManager) {
        setTimeout(() => {
          setScannerState('IDLE');
          scannerStateRef.current = 'IDLE';
        }, 500);
        return;
      }
    }

    bleManager.startDeviceScan(null, null, (error: any, device: any) => {
      if (error) {
        AppLogger.error(error);
        setScannerState('IDLE');
        scannerStateRef.current = 'IDLE';
        return;
      }
      if (device) {
        const nameLower = device.name?.toLowerCase() || '';
        const hasZenggeService = device.serviceUUIDs?.includes(ZENGGE_SERVICE_UUID);
        const manufacturerData = device.manufacturerData;

        let isSymphony = false;
        if (manufacturerData) {
          try {
            const mfBuf = Buffer.from(manufacturerData, 'base64');
            if (mfBuf.length > 9 && (mfBuf[9] === 0x33 || mfBuf[9] === 0xBF)) isSymphony = true;
          } catch (e) { AppLogger.warn('[Scanner] Failed to parse manufacturer data', { deviceId: device.id, error: String(e) }); }
        }

        const isKnownPrefix = nameLower.startsWith('lednet') || nameLower.startsWith('sk8') || nameLower.startsWith('zg') || nameLower.startsWith('halo') || nameLower.startsWith('soul');
        const isMatch = isSymphony || isKnownPrefix || hasZenggeService || knownMacsRef.current.has(device.id);
        
        const logData = { id: device.id, name: device.name || 'Unknown', rssi: device.rssi, isSymphony, isKnownPrefix, hasZenggeService, serviceUUIDs: device.serviceUUIDs || [], manufacturerData: manufacturerData ? 'presents' : 'none' };

        if (isMatch) {
          // ── RSSI GATE (Layer 2 defence against OS-cache ghosts) ────────────────────────────
          // Powered-off or out-of-range devices have no live radio. Their cached BLE
          // advertisement is replayed by the OS with rssi=null or a very low value.
          // -80 dBm is conservative (collocated devices typically read -40 to -65 dBm)
          // so valid nearby devices with momentary signal dips are never rejected.
          const RSSI_THRESHOLD = -80;
          const deviceRssi = device.rssi ?? -99;
          if (deviceRssi < RSSI_THRESHOLD) {
            if (!rejectedMacsRef.current.has(device.id)) {
              rejectedMacsRef.current.add(device.id);
              AppLogger.log('SCAN_FILTER_REJECT', {
                ...logData,
                reason: `RSSI too low (${deviceRssi} dBm < ${RSSI_THRESHOLD} dBm threshold) — likely OS-cache ghost`,
              });
            }
            return;
          }

          setAllDevices((prevState) => {
            if (!isDuplicateDevice(prevState, device)) {
              let advFirmware, firmwareVer, ledVersion, bleVersion, productId;
              if (manufacturerData) {
                try {
                  const fwInfo = ZenggeProtocol.parseFirmwareFromAdvertisement(manufacturerData);
                  if (fwInfo) {
                    advFirmware = `v${fwInfo.firmwareVer}.${fwInfo.ledVersion} (BLE ${fwInfo.bleVersion})`;
                    firmwareVer = fwInfo.firmwareVer;
                    ledVersion  = fwInfo.ledVersion;
                    bleVersion  = fwInfo.bleVersion;
                    productId   = fwInfo.productId;
                    Object.assign(device, { firmware: advFirmware, firmwareVer, ledVersion, bleVersion, productId });
                  }
                } catch (e) { AppLogger.warn('[Scanner] Failed to parse firmware from adv data', { deviceId: device.id, error: String(e) }); }
              }
              
              AppLogger.log('DEVICE_DISCOVERED', { ...logData, firmware: advFirmware, firmwareVer, ledVersion, bleVersion, productId });
              
              // Ambient Telemetry Harvester (Passive Zero-Cost)
              const now = Date.now();
              const lastSeen = telemetryCacheRef.current.get(device.id);
              if (!lastSeen || now - lastSeen > 30 * 60 * 1000) {
                telemetryCacheRef.current.set(device.id, now);
                telemetryBatchRef.current.push({
                   id: device.id.toUpperCase(),
                   rssi: device.rssi,
                   type: nameLower.includes('halo') ? 'HALOZ' : (nameLower.includes('soul') ? 'SOULZ' : 'UNKNOWN'),
                   manufacturerData: manufacturerData || null,
                   firmwareVer: firmwareVer || null,
                   bleVersion: bleVersion || null,
                   productId: productId || null,
                   ledVersion: ledVersion || null
                });

                if (telemetryBatchRef.current.length >= 25) {
                   flushTelemetry();
                } else if (!telemetryTimerRef.current) {
                   telemetryTimerRef.current = setTimeout(() => {
                     flushTelemetry();
                     telemetryTimerRef.current = null;
                   }, 60000); // flush every 60s max
                }
              }

              const updatedDevices = [...prevState, device];
              classifyProbeResults(updatedDevices);
              return updatedDevices;
            }
            return prevState;
          });
        } else {
          // Add the device to rejected tracking and only log once
          if (!rejectedMacsRef.current.has(device.id)) {
            rejectedMacsRef.current.add(device.id);
            if (device.name || (device.serviceUUIDs && device.serviceUUIDs.length > 0)) {
              AppLogger.log('SCAN_FILTER_REJECT', { ...logData, reason: 'No matching signature' });
            }
          }
        }
      }
    });

    if (scanTimerRef.current) clearTimeout(scanTimerRef.current);
    scanTimerRef.current = setTimeout(() => {
      bleManager.stopDeviceScan();
      scanTimerRef.current = null;
      if (!skipProbing) {
        probeAllDiscoveredDevices();
      } else {
        setScannerState('IDLE');
        scannerStateRef.current = 'IDLE';
      }
    }, 5000);
  };

  return {
    scannerState,
    pendingRegistrations,
    scanForPeripherals,
    stopScanner,
    setPendingRegistrations,
    classifyProbeResults
  };
}
