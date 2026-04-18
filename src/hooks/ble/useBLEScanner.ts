import AsyncStorage from '@react-native-async-storage/async-storage';
import { Buffer } from 'buffer';
import { useEffect, useRef, useState } from 'react';
import { Platform } from 'react-native';
import type { Device } from 'react-native-ble-plx';
import { LOCAL_PRODUCT_CATALOG, getLocalProfileByPoints } from '../../constants/ProductCatalog';
import { ZENGGE_SERVICE_UUID, ZenggeProtocol } from '../../protocols/ZenggeProtocol';
import { AppLogger } from '../../services/AppLogger';
import { supabase } from '../../services/supabaseClient';
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
  disableProbing = false
}: UseBLEScannerProps) {
  const [scannerState, setScannerState] = useState<'IDLE' | 'SCANNING' | 'PROBING'>('IDLE');
  const [pendingRegistrations, setPendingRegistrations] = useState<PendingRegistration[]>([]);

  const allDevicesRef = useRef<Device[]>([]);
  const scanTimerRef = useRef<NodeJS.Timeout | null>(null);
  const scannerStateRef = useRef<'IDLE' | 'SCANNING' | 'PROBING'>('IDLE');
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
        const matchedProfile = LOCAL_PRODUCT_CATALOG.find(p => name.includes(p.id));
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

      const deviceIdShort = d.id.replace(/:/g, '').slice(-4);
      return {
        device_mac:   d.id,
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

    let aborted = false;

    for (const device of pending) {
      if (scannerStateRef.current !== 'PROBING') {
        AppLogger.log('DEVICE_DISCOVERED', { context: 'probe_aborted', reason: 'scanner_cancellation' });
        aborted = true;
        break;
      }
      try {
        const alreadyConn = await bleManager.isDeviceConnected(device.id).catch(() => false);
        const hasHwInfo = (device as any).hwPoints != null;
        if (alreadyConn && hasHwInfo) continue;

        AppLogger.log('DEVICE_DISCOVERED', { context: 'probe_start', deviceName: device.name || device.id });
        
        // FIX: Remove manual connectToDevice wrap here. Let probeDevice() handle
        // its own connection, spec-query, and mandatory disconnection cleanly.
        const hwConfig = await probeDevice(device.id);
        if (hwConfig) {
          (device as any).hwPoints = hwConfig.ledPoints;
          (device as any).hwSegments = hwConfig.segments;
          (device as any).hwSorting = hwConfig.colorSortingName;
          (device as any).hwStripType = hwConfig.icName;

          if (hardwareProbedCallbackRef.current) {
            hardwareProbedCallbackRef.current(device.id, hwConfig);
          }
          setAllDevices([...allDevicesRef.current]);
          classifyProbeResults([...allDevicesRef.current]);
        } else {
          // If hwConfig is null, the 0x63 ping timed out entirely after all retries.
          AppLogger.warn(`[BLE Scanner] Device ${device.id} silently failed to yield telemetry`);
          failedIds.push(device.id);
        }
        
        await new Promise(r => setTimeout(r, 600));

      } catch (probeErr: any) {
        AppLogger.warn(`[BLE Probe] Hard crash on ${device.id}:`, probeErr?.message);
        failedIds.push(device.id);
        try { await bleManager.cancelDeviceConnection(device.id); } catch (e) { AppLogger.warn('[Scanner] Failed to cancel probe connection', { deviceId: device.id, error: String(e) }); }
        await new Promise(r => setTimeout(r, 500));
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
    }

    const shouldProbe = options?.disableProbing ?? disableProbing;

    const knownMacs = new Set<string>();

    AsyncStorage.getItem('@Sk8lytz_registered_devices').then(cached => {
      if (cached) {
        try {
          JSON.parse(cached).forEach((d: any) => knownMacs.add(d.device_mac));
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
                 product_id: 115,
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
        const isMatch = isSymphony || isKnownPrefix || hasZenggeService || knownMacs.has(device.id);
        
        const logData = { id: device.id, name: device.name || 'Unknown', rssi: device.rssi, isSymphony, isKnownPrefix, hasZenggeService, serviceUUIDs: device.serviceUUIDs || [], manufacturerData: manufacturerData ? 'presents' : 'none' };

        if (isMatch) {
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
              
              if (supabase) {
                (async () => {
                   try {
                     const { data: existingRows } = await supabase.from('registered_devices').select('user_id').eq('device_mac', device.id);
                     const ownerIds = (existingRows || []).map((r: any) => r.user_id).filter(Boolean);
                     const telemetryPayload: any = {
                       device_mac: device.id,
                       device_name: `SK8Lytz-${device.id.replace(/:/g, '').slice(-4)}`,
                       factory_name: device.name || 'Unknown',
                       manufacturer_data: manufacturerData || null,
                       ble_version: bleVersion || null,
                       firmware_ver: firmwareVer || null,
                       led_version: ledVersion || null,
                       product_id: productId || null,
                       rssi_at_register: device.rssi,
                       updated_at: new Date().toISOString()
                     };
                     if (ownerIds.length === 0) {
                       telemetryPayload.product_type = nameLower.includes('halo') ? 'HALOZ' : 'SOULZ';
                       telemetryPayload.group_name = 'Unclaimed';
                       telemetryPayload.position = null;
                     }
                     try { await supabase.from('registered_devices').upsert(telemetryPayload, { onConflict: 'device_mac', ignoreDuplicates: false }); } 
                     catch { try { await supabase.from('registered_devices').upsert(telemetryPayload, { onConflict: 'user_id,device_mac', ignoreDuplicates: false }); } catch {} }

                     if (ownerIds.length > 0) {
                       setAllDevices(prev => prev.map(d => d.id === device.id ? Object.assign(d, { owner_ids: ownerIds }) : d));
                     }
                   } catch(e) { AppLogger.warn('[Scanner] Telemetry upsert failed', { deviceId: device.id, error: String(e) }); }
                })();
              }

              const updatedDevices = [...prevState, device];
              classifyProbeResults(updatedDevices);
              return updatedDevices;
            }
            return prevState;
          });
        } else {
          if (device.name || (device.serviceUUIDs && device.serviceUUIDs.length > 0)) {
            AppLogger.log('SCAN_FILTER_REJECT', { ...logData, reason: 'No matching signature' });
          }
        }
      }
    });

    if (scanTimerRef.current) clearTimeout(scanTimerRef.current);
    scanTimerRef.current = setTimeout(() => {
      bleManager.stopDeviceScan();
      scanTimerRef.current = null;
      if (shouldProbe) {
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
