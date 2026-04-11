/**
 * useBLE.ts — SK8Lytz Bluetooth Low Energy Engine
 *
 * Custom React hook that wraps react-native-ble-plx to provide all
 * BLE hardware interactions for the SK8Lytz LED controller ecosystem.
 *
 * Key behaviors:
 *  - Strict device filtering: Symphony (0x33/0xBF manufacturer byte),
 *    known prefixes (lednet/sk8/zg/halo/soul), or Zengge service UUID
 *  - Sequential group connection loop (for...of) — NEVER use Promise.all,
 *    Android GATT throws error 133 on concurrent pairing attempts
 *  - Firmware parsed from BLE advertisement data on discovery
 *  - Characteristic notifications start immediately on connection for RX data
 *  - write() broadcasts to all connected unless targetDeviceId is specified
 *
 * Logs: DEVICE_DISCOVERED, DEVICE_CONNECTED, BLE_CONNECTION_ERROR, BLE_WRITE_ERROR, PERFORMANCE_METRIC
 * Platform: React Native Android / Web (web returns no-op stubs)
 */
import { useState, useMemo, useEffect, useRef } from 'react';
import { PermissionsAndroid, Platform } from 'react-native';
import type { Device } from 'react-native-ble-plx';
import * as ExpoDevice from 'expo-device';
import { ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, ZENGGE_NOTIFY_UUID, ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { AppLogger } from '../services/AppLogger';
import { supabase } from '../services/supabaseClient';
import { Buffer } from 'buffer';
import AsyncStorage from '@react-native-async-storage/async-storage';

let BleManager: any;
let State: any;

if (Platform.OS !== 'web') {
  const blePlx = require('react-native-ble-plx');
  BleManager = blePlx.BleManager;
  State = blePlx.State;
}

interface BluetoothLowEnergyApi {
  requestPermissions(): Promise<boolean>;
  scanForPeripherals(): void;
  connectToDevice: (device: Device) => Promise<string | undefined>;
  connectToDevices: (devices: Device[]) => Promise<void>;
  disconnectFromDevice: () => void;
  writeToDevice: (payload: number[], targetDeviceId?: string) => Promise<void>;
  probeDevice: (mac: string) => Promise<void>;
  connectedDevices: Device[];
  allDevices: Device[];
  setAllDevices: React.Dispatch<React.SetStateAction<Device[]>>;
  isScanning: boolean;
  isScanProbing: boolean;
  isBluetoothSupported: boolean;
  isBluetoothEnabled: boolean;
  onDataReceived?: (deviceId: string, data: number[]) => void;
  setOnDataReceived: (callback: (deviceId: string, data: number[]) => void) => void;
  setOnHardwareProbed: (callback: (deviceId: string, config: any) => void) => void;
  droppedOutDeviceIds: string[];
  setDroppedOutDeviceIds: React.Dispatch<React.SetStateAction<string[]>>;
  pendingRegistrations: PendingRegistration[];
  clearPendingRegistrations: () => void;
}

// Auto-classify result — fed into FirstTimeSetupModal
export interface PendingRegistration {
  device_mac: string;
  device_name: string;
  product_type: 'HALOZ' | 'SOULZ' | 'RAILZ' | 'UNKNOWN';
  position: 'Left' | 'Right';
  group_name: string;
  led_points: number;
  segments: number;
  ic_type: string;
  color_sorting: string;
  rssi: number;
  firmware_ver?: number;
  led_version?: number;
  product_id?: number;
}

export default function useBLE(): BluetoothLowEnergyApi {
  const bleManager = useMemo(() => {
    if (Platform.OS === 'web') return null;
    return new BleManager();
  }, []);

  const [allDevices, setAllDevices] = useState<Device[]>([]);
  const [connectedDevices, setConnectedDevices] = useState<Device[]>([]);
  const [isScanning, setIsScanning] = useState(false);
  const [isBluetoothSupported, setIsBluetoothSupported] = useState(Platform.OS !== 'web');
  const [isBluetoothEnabled, setIsBluetoothEnabled] = useState(Platform.OS === 'web');
  const [dataReceivedCallback, setDataReceivedCallback] = useState<((deviceId: string, data: number[]) => void) | undefined>();
  const [droppedOutDeviceIds, setDroppedOutDeviceIds] = useState<string[]>([]);
  const [isScanProbing, setIsScanProbing] = useState(false);
  const [pendingRegistrations, setPendingRegistrations] = useState<PendingRegistration[]>([]);

  useEffect(() => {
    if (__DEV__) {
      AsyncStorage.getItem('@Sk8lytz_demo_mode').then((isMock) => {
        if (Platform.OS === 'web' || isMock === 'true') {
          setIsBluetoothSupported(true);
          setIsBluetoothEnabled(true);
        }
      });
    }
  }, []);
  const disconnectListeners = useRef<Record<string, import('react-native-ble-plx').Subscription>>({});
  // Use ref (not state) so handleNotification always reads the CURRENT callback
  // without the stale closure problem — useState captures the value at subscription time
  const dataReceivedCallbackRef = useRef<((deviceId: string, data: number[]) => void) | undefined>(undefined);
  const hardwareProbedCallbackRef = useRef<((deviceId: string, config: any) => void) | undefined>(undefined);
  // connectedDevicesRef: always tracks current connectedDevices so writeToDevice/disconnectFromDevice
  // never suffer stale-closure bugs regardless of when they were last recreated.
  // ROOT CAUSE of all BLE write failures (including Pro Effects): writeToDevice read stale
  // connectedDevices=[] from mount time and returned early at the length===0 guard.
  const connectedDevicesRef = useRef<Device[]>([]);

  useEffect(() => {
    AppLogger.updateKnownDevices(allDevices);
  }, [allDevices]);
  // Keep connectedDevicesRef in sync so writeToDevice always reads current list
  useEffect(() => { connectedDevicesRef.current = connectedDevices; }, [connectedDevices]);
  const handleNotification = (error: any, characteristic: any, deviceId: string) => {
    if (error) {
      console.warn('Notification Error', error);
      AppLogger.log('PROTOCOL_ERROR', { error: error?.message || String(error), deviceId, context: 'notification' });
      return;
    }
    if (characteristic?.value) {
      try {
        /* Buffer is imported at top of file */
        const data = Array.from(Buffer.from(characteristic.value, 'base64')) as number[];
        // Read from ref — always has the latest callback, no stale closure
        if (dataReceivedCallbackRef.current) {
            dataReceivedCallbackRef.current(deviceId, data);
        }
      } catch (e: any) {
        console.error('Failed to parse notification', e);
        AppLogger.log('PROTOCOL_ERROR', { error: e?.message || String(e), deviceId, context: 'parse' });
      }
    }
  };

  useEffect(() => {
    if (!bleManager || Platform.OS === 'web') return;
    const subscription = bleManager.onStateChange((state: any) => {
      console.log('BLE State Change:', state);
      if (state === State.Unsupported) {
        setIsBluetoothSupported(false);
      }
      setIsBluetoothEnabled(state === State.PoweredOn);
    }, true);
    return () => subscription.remove();
  }, [bleManager]);

  const requestAndroid31Permissions = async () => {
    const bluetoothScanPermission = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.BLUETOOTH_SCAN,
      {
        title: "Scan Permission",
        message: "App requires Bluetooth Scanning",
        buttonPositive: "OK",
      }
    );
    const bluetoothConnectPermission = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.BLUETOOTH_CONNECT,
      {
        title: "Connect Permission",
        message: "App requires Bluetooth Connecting",
        buttonPositive: "OK",
      }
    );
    return (
      bluetoothScanPermission === "granted" &&
      bluetoothConnectPermission === "granted"
    );
  };

  const requestPermissions = async () => {
    if (Platform.OS === 'web') return true;
    
    if (Platform.OS === 'android') {
      if ((ExpoDevice.platformApiLevel ?? -1) < 31) {
        const granted = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
          {
            title: "Location Permission",
            message: "App requires Location to find Bluetooth devices",
            buttonPositive: "OK",
          }
        );
        return granted === PermissionsAndroid.RESULTS.GRANTED;
      } else {
        return await requestAndroid31Permissions();
      }
    } else {
      return true;
    }
  };

  const isDuplicateDevice = (devices: Device[], nextDevice: Device) =>
    devices.findIndex(device => nextDevice.id === device.id) > -1;

  const scanForPeripherals = () => {
    if (isScanning) return;
    setIsScanning(true);
    setPendingRegistrations([]);
    
    if (__DEV__) {
      AsyncStorage.getItem('@Sk8lytz_demo_mode').then((isMock) => {
        if (Platform.OS === 'web' || isMock === 'true') {
          console.log('[useBLE] Sandbox Mode Active: Injecting Mock Peripherals');
          setTimeout(() => {
            const mockDevices = [
              { 
                id: 'sim-DE:M0:HA:L0:00:01', name: 'HALOZ Left Skate', type: 'HALOZ',
                points: 11, segments: 2, sorting: 'GRB', stripType: 'WS2812B', rssi: -45, serviceUUIDs: [ZENGGE_SERVICE_UUID], manufacturerData: 'AAAAAAAAAAAz'
              },
              { 
                id: 'sim-DE:M0:HA:L0:00:02', name: 'HALOZ Right Skate', type: 'HALOZ',
                points: 11, segments: 2, sorting: 'GRB', stripType: 'WS2812B', rssi: -55, serviceUUIDs: [ZENGGE_SERVICE_UUID], manufacturerData: 'AAAAAAAAAAAz'
              },
              { 
                id: 'sim-DE:M0:S0:UL:00:01', name: 'SOULZ Left Skate', type: 'SOULZ',
                points: 43, segments: 1, sorting: 'GRB', stripType: 'WS2812B', rssi: -42, serviceUUIDs: [ZENGGE_SERVICE_UUID], manufacturerData: 'AAAAAAAAAAAz'
              },
              { 
                id: 'sim-DE:M0:S0:UL:00:02', name: 'SOULZ Right Skate', type: 'SOULZ',
                points: 43, segments: 1, sorting: 'GRB', stripType: 'WS2812B', rssi: -85, serviceUUIDs: [ZENGGE_SERVICE_UUID], manufacturerData: 'AAAAAAAAAAAz'
              }
            ] as any[];
            setAllDevices(mockDevices);
            
            // Map the parsed elements so they classify correctly in the FTUE parser hook
            const pendingMocks = mockDevices.map(device => {
               AppLogger.log('DEVICE_DISCOVERED', { 
                 id: device.id, name: device.name, rssi: device.rssi, points: device.points
               });
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
        console.error(error);
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
            /* global Buffer */
            const mfBuf = Buffer.from(manufacturerData, 'base64');
            // Index 9 is the device type code in many Zengge advertisement packets.
            // 0x33 (51) is the standard for addressable / Symphony controllers.
            // 0xBF is also seen in some Symphony variants.
            if (mfBuf.length > 9 && (mfBuf[9] === 0x33 || mfBuf[9] === 0xBF)) {
              isSymphony = true;
            }
          } catch (e) {
            // Silently ignore decode errors
          }
        }

        // Strict Filter Logic:
        // We prioritizing devices that explicitly report as Symphony (SPI) 
        // or have the signature SK8 or LEDnet prefix.
        const isKnownPrefix = nameLower.startsWith('lednet') || 
                            nameLower.startsWith('sk8') ||
                            nameLower.startsWith('zg') ||
                            nameLower.startsWith('halo') ||
                            nameLower.startsWith('soul');

        const isMatch = isSymphony || isKnownPrefix || hasZenggeService;
        
        const logData = {
          id: device.id,
          name: device.name || 'Unknown',
          rssi: device.rssi,
          isSymphony,
          isKnownPrefix,
          hasZenggeService,
          serviceUUIDs: device.serviceUUIDs || [],
          manufacturerData: manufacturerData ? 'presents' : 'none'
        };

        if (isMatch) {
          setAllDevices((prevState) => {
            if (!isDuplicateDevice(prevState, device)) {
              // Parse firmware from advertisement data during scan — primary source for Zengge
              let advFirmware: string | undefined;
              let firmwareVer: number | undefined;
              let ledVersion: number | undefined;
              let bleVersion: number | undefined;
              let productId: number | undefined;
              if (manufacturerData) {
                try {
                  // ZenggeProtocol is already statically imported at the top of this file
                  const fwInfo = ZenggeProtocol.parseFirmwareFromAdvertisement(manufacturerData);
                  if (fwInfo) {
                    advFirmware = `v${fwInfo.firmwareVer}.${fwInfo.ledVersion} (BLE ${fwInfo.bleVersion})`;
                    firmwareVer = fwInfo.firmwareVer;
                    ledVersion  = fwInfo.ledVersion;
                    bleVersion  = fwInfo.bleVersion;
                    productId   = fwInfo.productId;
                    (device as any).firmware   = advFirmware;
                    (device as any).firmwareVer = firmwareVer;
                    (device as any).ledVersion  = ledVersion;
                    (device as any).bleVersion  = bleVersion;
                    (device as any).productId   = productId;
                  }
                } catch (e) { /* silently skip */ }
              }
              // Log the full discovery event with all available hardware info
              AppLogger.log('DEVICE_DISCOVERED', {
                id: device.id,
                name: device.name || 'Unknown',
                rssi: device.rssi,
                isSymphony,
                isKnownPrefix,
                hasZenggeService,
                firmware: advFirmware,
                firmwareVer,
                ledVersion,
                bleVersion,
                productId,
                serviceUUIDs: device.serviceUUIDs || [],
              });
              // Push raw telemetry to Supabase unconditionally (if session online) so we have a live map of the environment
              if (supabase) {
                // We use an asynchronous fire-and-forget sync wrapper
                (async () => {
                   try {
                     // Query ALL rows matching this device to support multi-user ownership
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
                     
                     // If it's totally new, give it some safe defaults for the NOT NULL columns
                     if (ownerIds.length === 0) {
                       telemetryPayload.product_type = nameLower.includes('halo') ? 'HALOZ' : 'SOULZ';
                       telemetryPayload.group_name = 'Unclaimed';
                       telemetryPayload.position = null;
                     }
                     // If existing is present, it maintains its user_id because we omit it in the payload.
                     
                     // Use the UPSERT operation matching device_mac
                     await supabase.from('registered_devices').upsert(telemetryPayload, { onConflict: 'device_mac', ignoreDuplicates: false }).catch(() => {
                       // Silently drop errors if constraint is purely user_id,device_mac 
                       // fallback upsert trial using the combined constraint
                       supabase.from('registered_devices').upsert(telemetryPayload, { onConflict: 'user_id,device_mac', ignoreDuplicates: false }).catch(() => {});
                     });

                     // Provide the user_id back to local state so LogParser can display exactly who owns it!
                     if (ownerIds.length > 0) {
                       setAllDevices(prev => prev.map(d => d.id === device.id ? Object.assign(d, { owner_ids: ownerIds }) : d));
                     }
                   } catch(e) {}
                })();
              }

              const updatedDevices = [...prevState, device];
              // Trigger immediate classification so they show up in Wizard as "Identifying..."
              classifyProbeResults(updatedDevices);
              return updatedDevices;
            }
            return prevState;
          });
        } else {
          // Only log rejection if it's not a complete ghost (has at least a name or service)
          if (device.name || (device.serviceUUIDs && device.serviceUUIDs.length > 0)) {
            AppLogger.log('SCAN_FILTER_REJECT', {
              ...logData,
              reason: !isSymphony && !isKnownPrefix && !hasZenggeService ? 'No matching signature' : 'Unknown'
            });
          }
        }
      }
    });

    setTimeout(() => {
      bleManager.stopDeviceScan();
      setIsScanning(false);
      // After scan ends, background-probe each discovered device for hardware settings
      probeAllDiscoveredDevices();
    }, 5000);
  };

  // Capture discovered devices at probe time via ref so the closure is fresh
  const allDevicesRef2 = useRef<Device[]>([]);
  useEffect(() => { allDevicesRef2.current = allDevices; }, [allDevices]);

  /**
   * probeAllDiscoveredDevices — runs after scan completes.
   * Sequentially connects to each device, sends 0x63 query, waits for FF02
   * notification, parses result, fires onHardwareProbed callback, disconnects.
   * Skips devices that are already connected (user is actively using them).
   */
  const probeAllDiscoveredDevices = async (retriesLeft = 3) => {
    if (Platform.OS === 'web' || !bleManager) return;
    const devices = allDevicesRef2.current;
    
    // Filter to only those that still need probing (missing hwPoints)
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
        // Skip if already connected AND we already know the hardware (don't disturb active session unnecessarily)
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
          // Update list immediately for Wizard
          setAllDevices([...allDevicesRef2.current]);
          classifyProbeResults([...allDevicesRef2.current]);
        }
        
        await bleManager.cancelDeviceConnection(device.id).catch(() => {});
        await new Promise(r => setTimeout(r, 600));

      } catch (probeErr: any) {
        console.warn(`[BLE Probe] Failed ${device.id}:`, probeErr?.message);
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

  /**
   * Probes a single device synchronously returning a promise of its hardware config.
   * Can be used to verify EEPROM saves (such as adjusting LED points).
   */
  const probeDevice = async (mac: string): Promise<any> => {
    if (Platform.OS === 'web' || !bleManager) return null;
    
    // Connect locally if not connected. Wait, if it *is* already connected, we don't disconnect.
    const alreadyConn = await bleManager.isDeviceConnected(mac).catch(() => false);
    
    try {
      if (!alreadyConn) {
        await bleManager.connectToDevice(mac, { timeout: 5000 });
        await bleManager.discoverAllServicesAndCharacteristicsForDevice(mac);
      }
      
      const hwConfig = await new Promise<any>((resolve) => {
        const timer = setTimeout(() => {
          sub.remove();
          resolve(null);
        }, 2500);

        const sub = bleManager.monitorCharacteristicForDevice(
          mac,
          ZENGGE_SERVICE_UUID,
          ZENGGE_NOTIFY_UUID,
          (err: any, char: any) => {
            if (err || !char?.value) return;
            try {
              const raw = Array.from(Buffer.from(char.value, 'base64')) as number[];
              const parsed = ZenggeProtocol.parseHardwareSettingsResponse(raw);
              if (parsed) {
                clearTimeout(timer);
                sub.remove();
                resolve(parsed);
              }
            } catch (e) { /* ignore */ }
          }
        );

        const qp = ZenggeProtocol.queryHardwareSettings(false);
        const b64 = Buffer.from(qp).toString('base64');
        bleManager.writeCharacteristicWithoutResponseForDevice(
          mac, ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64
        ).catch((e: any) => console.warn('[BLE Probe Single] query write failed', e));
      });

      return hwConfig;
    } catch (err) {
      console.warn(`[BLE Probe Single] Failed to probe ${mac}:`, err);
      return null;
    } finally {
      if (!alreadyConn) {
        await bleManager.cancelDeviceConnection(mac).catch(() => {});
      }
    }
  };

  /**
   * classifyProbeResults — called after scan finds a device OR probe finishes.
   * Groups devices by product type, sorts by RSSI, assigns identifiers.
   * Populates pendingRegistrations state.
   */
  const classifyProbeResults = (forceList?: Device[]) => {
    const devices = forceList || allDevicesRef2.current;
    if (devices.length === 0) return;

    const haloz: any[] = [];
    const soulz: any[] = [];
    const unknown: any[] = [];

    for (const d of devices) {
      const pts = (d as any).hwPoints as number | undefined;
      const name = d.name?.toUpperCase() || '';
      
      // Range-based identification per user directive (April 2026)
      // SOULZ: 28–45 LEDs (standard 43)
      // HALOZ: 10–27 LEDs (standard 16 or 11)
      if (pts != null) {
        if (pts >= 28)       soulz.push(d);
        else if (pts >= 10)  haloz.push(d);
        else                 unknown.push(d);
      } else {
        // Name-based fallback (last resort)
        if (name.includes('HALO')) haloz.push(d);
        else if (name.includes('SOUL')) soulz.push(d);
        else unknown.push(d);
      }
    }

    const mapToRegistration = (d: any, i: number, type: 'HALOZ' | 'SOULZ' | 'RAILZ' | 'UNKNOWN'): PendingRegistration => {
      const isUnknown = type === 'UNKNOWN';
      const pos = i === 0 ? 'Left' : (i === 1 ? 'Right' : null);
      return {
        device_mac:   d.id,
        device_name:  isUnknown ? `SK8LYTZ (${d.id.slice(-5)})` : `${type} ${i + 1}`,
        product_type: type as any,
        position:     pos,
        group_name:   isUnknown ? 'Identifying...' : `My SK8Lytz ${type}`,
        led_points:   d.hwPoints || (type === 'SOULZ' ? 43 : 16),
        segments:     d.hwSegments ?? 1,
        ic_type:      d.hwStripType ?? 'WS2812B',
        color_sorting: d.hwSorting ?? 'GRB',
        rssi:         d.rssi ?? -99,
        firmware_ver: d.firmwareVer,
        led_version:  d.ledVersion,
        product_id:   d.productId,
      };
    };

    const sortedHaloz = [...haloz].sort((a,b) => (b.rssi ?? -99) - (a.rssi ?? -99));
    const sortedSoulz = [...soulz].sort((a,b) => (b.rssi ?? -99) - (a.rssi ?? -99));
    const sortedUnknown = [...unknown].sort((a,b) => (b.rssi ?? -99) - (a.rssi ?? -99));

    const results: PendingRegistration[] = [
      ...sortedHaloz.map((d, i) => mapToRegistration(d, i, 'HALOZ')),
      ...sortedSoulz.map((d, i) => mapToRegistration(d, i, 'SOULZ')),
      ...sortedUnknown.map((d, i) => mapToRegistration(d, i, 'UNKNOWN')),
    ];

    if (results.length > 0) {
      setPendingRegistrations(results);
    }
  };

  const connectToDevice = async (device: Device): Promise<string | undefined> => {
    try {
      const connectStartTime = Date.now();
      
      let isMock = 'false';
      if (__DEV__) {
         isMock = await AsyncStorage.getItem('@Sk8lytz_demo_mode') || 'false';
      }

      if (Platform.OS === 'web' || isMock === 'true') {
        setConnectedDevices(prev => {
          if (!prev.find(d => d.id === device.id)) return [...prev, device];
          return prev;
        });
        AppLogger.log('DEVICE_CONNECTED', { id: device.id, name: device.name, firmware: 'v2.0.1.DEMO' });
        
        // Mock hardware trickle data to prove connection is alive
        if (dataReceivedCallbackRef.current) {
          setTimeout(() => {
             // Mock standard hardware parameters notification packet
             // [0x66, 0x14, 0x22, 0x01, ...]
             const mockPacket = [0x66, 0x14, 0x22, 0x01, 0x01, 0x33, 0x01, 0x55, 0x66, 0x99];
             dataReceivedCallbackRef.current!(device.id, mockPacket);
          }, 1000);
        }
        return 'v2.0.1.DEMO';
      }
      const deviceConnection = await bleManager.connectToDevice(device.id);
      setConnectedDevices([deviceConnection]);
      await deviceConnection.discoverAllServicesAndCharacteristics();

      // Request large MTU so payloads up to ~509 bytes go through as single writes.
      // The 0x51 Pro Effects command is 299 bytes — requires MTU > 302.
      // Android negotiates down to hardware max automatically; we just request the ceiling.
      try {
        const negotiated = await deviceConnection.requestMTU(512);
        negotiatedMtuRef.current = negotiated.mtu;
        console.log(`[BLE] MTU negotiated: ${negotiated.mtu} bytes for ${device.id}`);
      } catch (mtuErr: any) {
        console.warn('[BLE] MTU negotiation failed (continuing with default 186):', mtuErr?.message);
      }

      // Register dropout listener
      if (disconnectListeners.current[device.id]) disconnectListeners.current[device.id].remove();
      disconnectListeners.current[device.id] = bleManager.onDeviceDisconnected(device.id, (error: any, _d: any) => {
        console.warn(`[BLE] Device dropout detected for ${device.id}`);
        AppLogger.log('DEVICE_DISCONNECTED', { id: device.id, reason: 'dropout', error: error?.message });
        setDroppedOutDeviceIds(prev => [...prev, device.id]);
        setConnectedDevices(prev => prev.filter(c => c.id !== device.id));
        if (disconnectListeners.current[device.id]) {
          disconnectListeners.current[device.id].remove();
          delete disconnectListeners.current[device.id];
        }
      });
      
      const latencyMs = Date.now() - connectStartTime;
      AppLogger.log('PERFORMANCE_METRIC', { metricName: 'BLE_Auth_Latency', value: latencyMs, unit: 'ms', deviceId: device.id });
      
      // Monitor FF02 for responses — FF01 is write-only, FF02 is the notify characteristic
      deviceConnection.monitorCharacteristicForService(
        ZENGGE_SERVICE_UUID,
        ZENGGE_NOTIFY_UUID,
        (error: any, characteristic: any) => handleNotification(error, characteristic, device.id)
      );

      // Attempt to read firmware version from standard BLE Device Information Service (180A / 2A26)
      let firmware: string | undefined;

      // First try: advertisement data already parsed during scan
      const advFw = (device as any).firmware;
      if (advFw && typeof advFw === 'string' && advFw.length > 0) {
        firmware = advFw;
      }

      // Second try: GATT Device Information Service characteristic
      if (!firmware) {
        try {
          const fwChar = await deviceConnection.readCharacteristicForService(
            '0000180a-0000-1000-8000-00805f9b34fb',
            '00002a26-0000-1000-8000-00805f9b34fb'
          );
          if (fwChar && fwChar.value) {
            const rawFw = Buffer.from(fwChar.value, 'base64').toString('ascii');
            const clean = rawFw.replace(/[^\x20-\x7E]/g, '');
            if (clean.length > 0) firmware = clean;
          }
        } catch (e) {
          console.log(`[BLE] No standard firmware characteristic for ${device.id}`);
        }
      }

      // Send 0x63 hardware settings query directly via deviceConnection
      // Using deviceConnection directly — avoids ALL stale closure issues with writeToDevice/connectedDevices state
      setTimeout(async () => {
        try {
          const queryPayload = ZenggeProtocol.queryHardwareSettings(false);
          const b64 = Buffer.from(queryPayload).toString('base64');
          await deviceConnection.writeCharacteristicWithoutResponseForService(
            ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64
          );
          console.log(`[BLE] 0x63 hw query sent to ${device.id}`);
        } catch (e) {
          console.warn('[BLE] hw query write failed', e);
        }
      }, 600);

      AppLogger.log('DEVICE_CONNECTED', { id: device.id, name: device.name, firmware });

      bleManager.stopDeviceScan();
      setIsScanning(false);
      
      return firmware;
    } catch (e: any) {
      console.error('FAILED TO CONNECT', e);
      AppLogger.log('BLE_CONNECTION_ERROR', { error: e?.message || String(e), deviceId: device.id });
    }
  };

  const connectToDevices = async (devices: Device[]) => {
    if (devices.length === 0) return;
    try {
      let isMock = 'false';
      if (__DEV__) {
         isMock = await AsyncStorage.getItem('@Sk8lytz_demo_mode') || 'false';
      }

      if (Platform.OS === 'web' || isMock === 'true') {
        setConnectedDevices(devices);
        devices.forEach(d => {
          AppLogger.log('DEVICE_CONNECTED', { id: d.id, name: d.name, firmware: 'v2.0.1.DEMO' });
        });
        
        // Mock hardware trickle data to prove connection is alive
        if (dataReceivedCallbackRef.current) {
          setTimeout(() => {
             devices.forEach(d => {
               // [0x66, 0x14, 0x22, 0x01, ...]
               const mockPacket = [0x66, 0x14, 0x22, 0x01, 0x01, 0x33, 0x01, 0x55, 0x66, 0x99];
               dataReceivedCallbackRef.current!(d.id, mockPacket);
             });
          }, 1000);
        }
        return;
      }
      
      const connections = [];
      // STRICT SEQUENTIAL CONNECTION LOOP
      // WARNING: Android's native Bluetooth GATT adapter heavily penalizes concurrent `Promise.all` calls 
      // with immediate GATT 133 Exception drops. We MUST connect and discover systematically one-by-one.
      for (const device of devices) {
        try {
          const conn = await bleManager.connectToDevice(device.id);
          connections.push(conn);
          await conn.discoverAllServicesAndCharacteristics();

          // Request large MTU for large payloads (0x51 Pro Effects = 299 bytes)
          try {
            const negotiated = await conn.requestMTU(512);
            negotiatedMtuRef.current = negotiated.mtu;
            console.log(`[BLE] MTU negotiated: ${negotiated.mtu} bytes for ${conn.id} (group)`);
          } catch (mtuErr: any) {
            console.warn('[BLE] MTU negotiation failed for group device (continuing with default 186):', mtuErr?.message);
          }

          // Register dropout listener
          if (disconnectListeners.current[conn.id]) disconnectListeners.current[conn.id].remove();
          disconnectListeners.current[conn.id] = bleManager.onDeviceDisconnected(conn.id, (error: any, _d: any) => {
            console.warn(`[BLE] Device dropout detected for ${conn.id} in group`);
            AppLogger.log('DEVICE_DISCONNECTED', { id: conn.id, reason: 'dropout', context: 'group', error: error?.message });
            setDroppedOutDeviceIds(prev => [...prev, conn.id]);
            setConnectedDevices(prev => prev.filter(c => c.id !== conn.id));
            if (disconnectListeners.current[conn.id]) {
              disconnectListeners.current[conn.id].remove();
              delete disconnectListeners.current[conn.id];
            }
          });

          // Monitor FF02 for responses — FF01 is write-only, FF02 is the notify characteristic
          conn.monitorCharacteristicForService(
            ZENGGE_SERVICE_UUID,
            ZENGGE_NOTIFY_UUID,
            (error: any, characteristic: any) => handleNotification(error, characteristic, conn.id)
          );

          let firmware = undefined;
          try {
            const fwChar = await conn.readCharacteristicForService(
              '0000180a-0000-1000-8000-00805f9b34fb',
              '00002a26-0000-1000-8000-00805f9b34fb'
            );
            if (fwChar && fwChar.value) {
              const rawFw = Buffer.from(fwChar.value, 'base64').toString('ascii');
              firmware = rawFw.replace(/[^\x20-\x7E]/g, '');
            }
          } catch (e) { }

          AppLogger.log('DEVICE_CONNECTED', { id: conn.id, name: conn.name, firmware });

          // Send 0x63 hw query directly via conn — no stale closure
          const connCapture = conn;
          setTimeout(async () => {
            try {
              const qp = ZenggeProtocol.queryHardwareSettings(false);
              const b64 = Buffer.from(qp).toString('base64');
              await connCapture.writeCharacteristicWithoutResponseForService(
                ZENGGE_SERVICE_UUID, ZENGGE_CHARACTERISTIC_UUID, b64
              );
              console.log(`[BLE] 0x63 hw query sent to ${connCapture.id} (group)`);
            } catch (e) { console.warn('[BLE] group hw query write failed', e); }
          }, 600);
        } catch (deviceError: any) {
          console.error(`FAILED TO CONNECT TO INDIVIDUAL DEVICE ${device.id}`, deviceError);
          AppLogger.log('BLE_CONNECTION_ERROR', { error: deviceError?.message || String(deviceError), deviceId: device.id, context: 'group_sync_fail' });
        }
      }

      setConnectedDevices(connections);

      bleManager.stopDeviceScan();
      setIsScanning(false);
    } catch (e: any) {
      console.error('FAILED TO CONNECT TO GROUP', e);
      AppLogger.log('BLE_CONNECTION_ERROR', { error: e?.message || String(e), context: 'group' });
    }
  };

  // Negotiated MTU per device (updated after requestMTU() resolves).
  // We request MTU=512 on connect. The hardware will negotiate its actual max.
  // Write Without Response requires payload <= (negotiatedMTU - 3).
  // For the 0x51 Pro Effects payload (299 bytes), the hardware must accept MTU >= 302.
  const negotiatedMtuRef = useRef<number>(186);

  const writeToDevice = async (payload: number[], targetDeviceId?: string) => {
    const hexString = payload.map(x => x.toString(16).toUpperCase().padStart(2, '0')).join(' ');
    console.log(`[BLE WRITE ${payload.length}B | MTU=${negotiatedMtuRef.current}]${targetDeviceId ? ` [→${targetDeviceId.slice(-4)}]` : ''}`, hexString.substring(0, 80));
    AppLogger.setLastTxPayload(hexString);

    if (connectedDevicesRef.current.length === 0 || Platform.OS === 'web') return;

    const targets = targetDeviceId
      ? connectedDevicesRef.current.filter(d => d.id === targetDeviceId)
      : connectedDevicesRef.current;

    if (targets.length === 0 && targetDeviceId) {
      console.warn(`Target device ${targetDeviceId} not found in connected devices`);
      return;
    }

    // Chunk size: MTU - 3 bytes (ATT header overhead)
    const chunkSize = Math.max(20, negotiatedMtuRef.current - 3);

    for (const device of targets) {
      try {
        for (let i = 0; i < payload.length; i += chunkSize) {
          const chunk = payload.slice(i, i + chunkSize);
          const base64Chunk = Buffer.from(chunk).toString('base64');
          
          await device.writeCharacteristicWithoutResponseForService(
            ZENGGE_SERVICE_UUID,
            ZENGGE_CHARACTERISTIC_UUID,
            base64Chunk
          );
          
          // Small delay to prevent buffer overflow on hardware side
          if (i + chunkSize < payload.length) {
            await new Promise(resolve => setTimeout(resolve, 10));
          }
        }
      } catch (writeError: any) {
        console.warn(`[BLE] Write failed for ${device.id}`, writeError?.message);
        AppLogger.log('BLE_WRITE_ERROR', { error: writeError?.message || String(writeError), target: device.id, payloadLen: payload.length });
      }
    }
  };

  const disconnectFromDevice = async () => {
    // Clean up all physical listeners
    Object.values(disconnectListeners.current).forEach(sub => {
      try { sub.remove(); } catch (e) {}
    });
    disconnectListeners.current = {};

    const staleDevices = [...connectedDevicesRef.current];

    if (staleDevices.length > 0 && Platform.OS !== 'web') {
      for (const device of staleDevices) {
        try {
          await bleManager.cancelDeviceConnection(device.id).catch((e: any) => console.warn(`[BLE] Disconnect soft fail for ${device.id}`, e));
        } catch (e: any) {
          console.error(`[BLE] Fatal disconnect fault for ${device.id}`, e);
        }
      }
      
      // Provide a buffer for the host OS Bluetooth stack to cleanly complete teardown
      await new Promise(resolve => setTimeout(resolve, 250));
    }
    
    // UI trigger after native cleanup
    setConnectedDevices([]);
  };

  return useMemo(() => ({
    scanForPeripherals,
    requestPermissions,
    connectToDevice,
    connectToDevices,
    writeToDevice,
    allDevices,
    setAllDevices,
    connectedDevices,
    disconnectFromDevice,
    isScanning,
    isScanProbing,
    isBluetoothSupported,
    isBluetoothEnabled,
    onDataReceived: dataReceivedCallback,
    setOnDataReceived: (callback: (deviceId: string, data: number[]) => void) => { dataReceivedCallbackRef.current = callback; },
    setOnHardwareProbed: (callback: (deviceId: string, config: any) => void) => { hardwareProbedCallbackRef.current = callback; },
    droppedOutDeviceIds,
    setDroppedOutDeviceIds,
    pendingRegistrations,
    clearPendingRegistrations: () => setPendingRegistrations([]),
    probeDevice,
  }), [
    allDevices, 
    connectedDevices, 
    isScanning,
    isScanProbing,
    pendingRegistrations,
    isBluetoothSupported, 
    isBluetoothEnabled, 
    dataReceivedCallback,
    setDataReceivedCallback,
    droppedOutDeviceIds,
    setDroppedOutDeviceIds
  ]);
}
