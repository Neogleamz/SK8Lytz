import { useState, useRef, useEffect } from 'react';
import { Platform } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import type { Device } from 'react-native-ble-plx';
import { Buffer } from 'buffer';
import { AppLogger } from '../../services/AppLogger';
import { supabase } from '../../services/supabaseClient';
import { ZENGGE_SERVICE_UUID, ZenggeProtocol } from '../../protocols/ZenggeProtocol';
import { LOCAL_PRODUCT_CATALOG, getLocalProfileByPoints } from '../../constants/ProductCatalog';
import type { PendingRegistration } from '../../types/dashboard.types';

export interface UseBLEScannerProps {
  bleManager: any;
  allDevices: Device[];
  setAllDevices: React.Dispatch<React.SetStateAction<Device[]>>;
  probeDevice: (mac: string) => Promise<any>;
  hardwareProbedCallbackRef: React.MutableRefObject<((deviceId: string, config: any) => void) | undefined>;
}

export function useBLEScanner({
  bleManager,
  allDevices,
  setAllDevices,
  probeDevice,
  hardwareProbedCallbackRef
}: UseBLEScannerProps) {
  const [isScanning, setIsScanning] = useState(false);
  const [isScanProbing, setIsScanProbing] = useState(false);
  const [pendingRegistrations, setPendingRegistrations] = useState<PendingRegistration[]>([]);

  const allDevicesRef = useRef<Device[]>([]);
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

      return {
        device_mac:   d.id,
        device_name:  isUnknown ? `SK8LYTZ (${d.id.slice(-5)})` : `${type} ${i + 1}`,
        product_type: type as any,
        position:     pos,
        group_name:   isUnknown ? 'Identifying...' : `My SK8Lytz ${type}`,
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
      setIsScanProbing(false);
      return;
    }

    setIsScanProbing(true);
    console.log(`[BLE Probe] Round-Robin Probing ${pending.length} device(s). Retries left: ${retriesLeft}`);

    const failedIds: string[] = [];

    for (const device of pending) {
      try {
        const alreadyConn = await bleManager.isDeviceConnected(device.id).catch(() => false);
        const hasHwInfo = (device as any).hwPoints != null;
        if (alreadyConn && hasHwInfo) continue;

        console.log(`[BLE Probe] Probing ${device.name || device.id}...`);
        const conn = await bleManager.connectToDevice(device.id, { timeout: 3500 });
        await conn.discoverAllServicesAndCharacteristics();

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
        }
        
        await bleManager.cancelDeviceConnection(device.id).catch(() => {});
        await new Promise(r => setTimeout(r, 600));

      } catch (probeErr: any) {
        AppLogger.warn(`[BLE Probe] Failed ${device.id}:`, probeErr?.message);
        failedIds.push(device.id);
        try { await bleManager.cancelDeviceConnection(device.id); } catch (e) { }
        await new Promise(r => setTimeout(r, 500));
      }
    }

    if (failedIds.length > 0 && retriesLeft > 0) {
       console.log(`[BLE Probe] Retrying ${failedIds.length} failed probes in 2s...`);
       await new Promise(r => setTimeout(r, 2000));
       return probeAllDiscoveredDevices(retriesLeft - 1);
    }

    setIsScanProbing(false);
    console.log('[BLE Probe] All rounds complete.');
  };

  const scanForPeripherals = (options?: { keepAlive?: boolean }) => {
    if (isScanning) return;
    setIsScanning(true);
    if (!options?.keepAlive) {
      setPendingRegistrations([]);
    }
    
    const knownMacs = new Set<string>();
    AsyncStorage.getItem('ng_registered_devices').then(cached => {
      if (cached) {
        try {
          JSON.parse(cached).forEach((d: any) => knownMacs.add(d.device_mac));
        } catch (e) {}
      }
    }).catch(() => {});

    if (__DEV__) {
      AsyncStorage.getItem('@Sk8lytz_demo_mode').then((isMock) => {
        if (Platform.OS === 'web' || isMock === 'true') {
          console.log('[useBLE] Sandbox Mode Active: Injecting Mock Peripherals');
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
            setIsScanning(false);
          }, 500);
        } else if (!bleManager) {
          setTimeout(() => setIsScanning(false), 500);
        }
      });
      if (Platform.OS === 'web') return;
    } else {
      if (!bleManager) {
        setTimeout(() => setIsScanning(false), 500);
        return;
      }
    }

    bleManager.startDeviceScan(null, null, (error: any, device: any) => {
      if (error) {
        AppLogger.error(error);
        setIsScanning(false);
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
          } catch (e) { }
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
                } catch (e) { }
              }
              
              AppLogger.log('DEVICE_DISCOVERED', { ...logData, firmware: advFirmware, firmwareVer, ledVersion, bleVersion, productId });
              
              if (supabase) {
                (async () => {
                   try {
                     const { data: existingRows } = await supabase.from('registered_devices').select('user_id').eq('device_mac', device.id);
                     const ownerIds = (existingRows || []).map((r: any) => r.user_id).filter(Boolean);
                     const telemetryPayload: any = {
                       device_mac: device.id,
                       device_name: device.name || 'Unknown SK8Lytz',
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
                   } catch(e) {}
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

    setTimeout(() => {
      bleManager.stopDeviceScan();
      setIsScanning(false);
      probeAllDiscoveredDevices();
    }, 5000);
  };

  return {
    isScanning,
    isScanProbing,
    pendingRegistrations,
    scanForPeripherals,
    setPendingRegistrations,
    classifyProbeResults
  };
}
