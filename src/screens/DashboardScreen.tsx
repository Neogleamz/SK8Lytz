/**
 * DashboardScreen.tsx — SK8Lytz Root Application Screen
 *
 * The single top-level screen for the entire SK8Lytz application.
 * This is intentionally monolithic — all BLE state must be co-located
 * to prevent race conditions between hardware events and UI re-renders.
 *
 * Owns:
 *  - BLE lifecycle (scan, connect, disconnect, group sync)
 *  - Device roster (allDevices, connectedDevices)
 *  - Custom groups (name, deviceIds, persistence)
 *  - Per-device configs (name, LED type, points, segments, sorting)
 *  - Power state map, modal visibility flags
 *  - BLE write dispatch (writeToDevice → useBLE → GATT)
 *
 * AsyncStorage keys:
 *  - ng_device_configs    → per-device settings dict (keyed by MAC)
 *  - ng_custom_groups     → user-defined multi-device groups
 *  - ng_processed_devices → cached discovered device list
 *
 * Platform: React Native (Android + Web)
 */
import React, { useEffect, useState, useMemo, useCallback, useRef } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, SafeAreaView, FlatList, ActivityIndicator, Switch, Platform, Image, Linking, Animated, StatusBar, Dimensions, Modal, TextInput, BackHandler, PanResponder, AppState, AppStateStatus, Alert } from 'react-native';
import { Typography, Layout } from '../theme/theme';
import { useTheme } from '../context/ThemeContext';
import DeviceItem from '../components/DeviceItem';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import useBLE from '../hooks/useBLE';
import { ZenggeProtocol, ZENGGE_SERVICE_UUID } from '../protocols/ZenggeProtocol';

import DockedController from '../components/DockedController';
import DeviceSettingsModal from '../components/DeviceSettingsModal';
import GroupSettingsModal from '../components/GroupSettingsModal';
import Sk8LytzProgrammerModal from '../components/Sk8LytzProgrammerModal';
import AsyncStorage from '@react-native-async-storage/async-storage';
import ScannerAnimation from '../components/ScannerAnimation';
import { AppLogger } from '../services/AppLogger';
import LogViewerModal from '../components/LogViewerModal';
import CrewModal from '../components/CrewModal';
import { crewService, CrewSession, CrewRole } from '../services/CrewService';
import AdminHardwareTester from '../components/AdminHardwareTester';
import FirstTimeSetupModal from '../components/FirstTimeSetupModal';
import { supabase } from '../services/supabaseClient';
import { useRegistration, RegisteredDevice } from '../hooks/useRegistration';

interface DeviceSettings {
  name: string;
  type: 'HALOZ' | 'SOULZ';
  points: number;
  segments: number;
  stripType: string;
  sorting: string;
  grouped: boolean;
  groupId?: string;
  groupName?: string;
}

export default function DashboardScreen({ isOfflineMode = false, onLogout }: { isOfflineMode?: boolean; onLogout?: () => void } = {}) {
  const { Colors, isDark, toggleTheme } = useTheme();
  const styles = createStyles(Colors);
  const {
    scanForPeripherals,
    allDevices,
    setAllDevices,
    connectToDevice,
    connectToDevices,
    connectedDevices,
    disconnectFromDevice,
    writeToDevice,
    isScanning,
    isScanProbing,
    isBluetoothSupported,
    isBluetoothEnabled,
    requestPermissions,
    setOnDataReceived,
    setOnHardwareProbed,
    droppedOutDeviceIds,
    pendingRegistrations,
    clearPendingRegistrations,
  } = useBLE();

  // ── Registration system ────────────────────────────────────────────────────
  const {
    registeredDevices,
    saveAllRegisteredDevices,
    hasCloudRegistrations,
    migrateLegacyGroups,
    syncFromCloud,
    hasPendingSync,
  } = useRegistration();

  // Sync connected+discovered devices into AppLogger whenever they change
  // so that parsed_session_devices has fresh data at upload time
  useEffect(() => {
    const merged = [...connectedDevices];
    allDevices.forEach(d => { if (!merged.find(c => c.id === d.id)) merged.push(d); });
    AppLogger.updateKnownDevices(merged);
  }, [connectedDevices, allDevices]);

  // Register background probe callback — fires for each device after scan probe
  useEffect(() => {
    setOnHardwareProbed((deviceId: string, cfg: any) => {
      // Merge parsed hardware config into deviceConfigs (persisted store)
      setDeviceConfigs(prev => {
        const merged = { ...(prev[deviceId] || {}), ...cfg };
        const next = { ...prev, [deviceId]: merged };
        // Persist immediately
        AsyncStorage.setItem('ng_device_configs', JSON.stringify(next)).catch(() => {});
        return next;
      });
      // Also update allDevices so the list shows config before user connects
      setAllDevices(prev => prev.map(d =>
        (d as any).id === deviceId
          ? { ...d,
              points: cfg.ledPoints,
              segments: cfg.segments,
              stripType: cfg.icName,
              sorting: cfg.colorSortingName,
              colorSorting: cfg.colorSorting,
              icType: cfg.icType,
              detected: true
            } as any
          : d
      ));
    });
  }, [setOnHardwareProbed]);

  const [selectedIds, setSelectedIds] = useState<string[]>([]);
  const [isSelectionMode, setIsSelectionMode] = useState(false);
  const [powerStates, setPowerStates] = useState<Record<string, boolean>>({});
  const [deviceConfigs, setDeviceConfigs] = useState<Record<string, any>>({});

  const IS_BROWSER_DEMO = Platform.OS === 'web';
  const [mockConnected, setMockConnected] = useState(false);
  const [mockConnectedDevice, setMockConnectedDevice] = useState<string | null>(null);
  const [mockConnectedGroup, setMockConnectedGroup] = useState<string | null>(null);
  const [updateTrigger, setUpdateTrigger] = useState(0);
  const [isTestModeActive, setIsTestModeActive] = useState(false);
  const [lastRawNotification, setLastRawNotification] = useState<{deviceId: string, payloadHex: string} | null>(null);

  const [customGroups, setCustomGroups] = useState<any[]>([]);
  const [isGroupModalVisible, setIsGroupModalVisible] = useState(false);
  const [groupModalMode, setGroupModalMode] = useState<'create' | 'rename'>('create');
  const [editingGroupId, setEditingGroupId] = useState<string | null>(null);
  const [isDeviceListCollapsed, setIsDeviceListCollapsed] = useState(false);

  // ── Crew Sync state ─────────────────────────────────────────────────────
  const [crewSession, setCrewSession] = useState<CrewSession | null>(null);
  const [crewRole, setCrewRole] = useState<CrewRole>(null);
  const [isCrewModalVisible, setIsCrewModalVisible] = useState(false);
  const [scannerTab, setScannerTab] = useState<'DEVICES' | 'CREW'>('DEVICES');
  const dockedControllerRef = React.useRef<{ applyCloudScene: (s: any) => void }>(null);
  const [showHintText, setShowHintText] = useState(true);
  const [isSupportModalVisible, setIsSupportModalVisible] = useState(false);
  const [isProgrammerVisible, setIsProgrammerVisible] = useState(false);
  const [isSnifferVisible, setIsSnifferVisible] = useState(false);
  const [isSetupWizardVisible, setIsSetupWizardVisible] = useState(false);
  const lastProcessedRef = React.useRef<string>('');
  const allDevicesRef = React.useRef(allDevices);
  const customGroupsRef = React.useRef(customGroups);
  const isProvisioningTriggered = React.useRef(false);

  // Refs are now updated manually  const [isProvisioning, setIsProvisioning] = useState(false);
  const [demoHaloQueued, setDemoHaloQueued] = useState(false);
  const [demoSoulQueued, setDemoSoulQueued] = useState(false);

  // AppState Telemetry
  useEffect(() => {
    const subscription = AppState.addEventListener('change', (nextAppState: AppStateStatus) => {
      if (nextAppState === 'active') {
        AppLogger.log('APP_FOREGROUNDED');
      } else if (nextAppState === 'background') {
        AppLogger.log('APP_BACKGROUNDED');
      }
    });
    return () => {
      subscription.remove();
    };
  }, []);

  // ── First-Time Setup Wizard trigger ─────────────────────────────────────────
  // Fires once after probe: if user has 0 cloud registrations, show setup wizard.
  const wizardCheckedRef = React.useRef(false);
  useEffect(() => {
    if (pendingRegistrations.length === 0) return;
    if (wizardCheckedRef.current) return;
    wizardCheckedRef.current = true;
    hasCloudRegistrations().then(hasAny => {
      if (!hasAny) setIsSetupWizardVisible(true);
    });
  }, [pendingRegistrations]);

  const handleRegistrationComplete = async (devices: RegisteredDevice[]) => {
    const legacyDevices = await migrateLegacyGroups(allDevices, deviceConfigs);
    const allToRegister = [
      ...devices,
      ...legacyDevices.filter(l => !devices.find(d => d.device_mac === l.device_mac)),
    ];
    await saveAllRegisteredDevices(allToRegister);
    clearPendingRegistrations();
    setIsSetupWizardVisible(false);
    wizardCheckedRef.current = false;
  };

  // Drop-out UI Alert
  useEffect(() => {
    if (droppedOutDeviceIds.length > 0) {
      Alert.alert(
        'Device Disconnected', 
        `Hardware dropout detected. Connection has been lost to a skate.`,
        [{ text: 'OK' }]
      );
    }
  }, [droppedOutDeviceIds]);

  // User Profile
  const [authUsername, setAuthUsername] = useState<string | null>(null);

  const handleLogout = async () => {
     try {
       await supabase.auth.signOut();
       // App.tsx onAuthStateChange will detect session=null and redirect to AuthScreen automatically
     } catch (e) {
       console.error('Logout error:', e);
     }
  };

  // Cloud Sync & Auto Connect on Launch
  const hasAutoConnectedRef = useRef(false);
  useEffect(() => {
    async function syncCloudAndAutoConnect() {
      if (hasAutoConnectedRef.current || !isBluetoothSupported || !isBluetoothEnabled) return;
      hasAutoConnectedRef.current = true; // Prevent looping

      let groupsToProcess: any[] = [];
      let isOffline = true;
      let CloudUserId: string | null = null;

      if (supabase) {
        const { data: { session } } = await supabase.auth.getSession();
        if (session?.user) {
          CloudUserId = session.user.id;
          setAuthUsername(session.user.user_metadata?.username || session.user.email || 'Skater');
          const { data: groups, error } = await supabase.from('registered_groups').select('*').eq('user_id', CloudUserId).catch(() => ({ data: null, error: true }));
          isOffline = !!error;
          if (!error && groups && groups.length > 0) {
            groupsToProcess = groups;
          }
        }
      }

      if (isOffline || groupsToProcess.length === 0) {
        // Fallback to local storage
        console.log('[SK8Lytz] Cloud unreachable. Using local AsyncStorage for Auto-Connect...');
        const localGroupsStr = await AsyncStorage.getItem('ng_custom_groups');
        if (localGroupsStr) {
          try {
            const parsed = JSON.parse(localGroupsStr);
            groupsToProcess = parsed.map((g: any) => ({
              id: g.id,
              group_name: g.name,
              created_at: new Date().toISOString(),
              deviceIds: g.deviceIds
            }));
          } catch (e) {}
        }
      }

      if (groupsToProcess.length > 0) {
        console.log('[SK8Lytz] Found fleets. Initiating Auto-Connect Sequence...');
        requestPermissions().then((granted) => {
          if (granted && !isActuallyConnected) {
            scanForPeripherals();
            const waitTime = Math.max(5000, Platform.OS === 'web' ? 5000 : 7000);
            setTimeout(async () => {
              const currentScannedIds = allDevicesRef.current.map(d => d.id);
              let presentGroups: any[] = [];
              let idsToConnect: string[] = [];

              if (!isOffline && supabase && CloudUserId) {
                 const { data: devices } = await supabase.from('registered_devices').select('*').eq('user_id', CloudUserId);
                 if (devices) {
                    presentGroups = groupsToProcess.filter((g: any) => {
                       const groupDevices = devices.filter((d: any) => d.group_id === g.id);
                       return groupDevices.length > 0 && groupDevices.every((d: any) => currentScannedIds.includes(d.id));
                    }).sort((a: any, b: any) => new Date(b.created_at).getTime() - new Date(a.created_at).getTime());
                    
                    if (presentGroups.length > 0) {
                      idsToConnect = devices.filter((d: any) => d.group_id === presentGroups[0].id).map((d: any) => d.id);
                    }
                 }
              } else {
                 presentGroups = groupsToProcess.filter((g: any) => {
                    return g.deviceIds && g.deviceIds.length > 0 && g.deviceIds.every((id: string) => currentScannedIds.includes(id));
                 });
                 if (presentGroups.length > 0) {
                    idsToConnect = presentGroups[0].deviceIds;
                 }
              }

              if (idsToConnect.length > 0) {
                console.log('[SK8Lytz] Auto-connecting to fleet:', presentGroups[0].group_name);
                const devicesToConnect = allDevicesRef.current.filter(d => idsToConnect.includes(d.id));
                connectToDevices(devicesToConnect);
              }
            }, waitTime);
          }
        });
      }
    }
    
    // Slight delay to allow Bluetooth stack to fully initialize (1.5s)
    const timerId = setTimeout(syncCloudAndAutoConnect, 1500);
    return () => clearTimeout(timerId);
  }, [isBluetoothSupported, isBluetoothEnabled]);

  // ── Crew auto-rejoin on launch ────────────────────────────────────────────
  useEffect(() => {
    const tryRejoin = async () => {
      const { data: { user } } = await supabase.auth.getUser();
      if (!user) return;
      const displayName = user.email?.split('@')[0] || 'Skater';
      const result = await crewService.tryAutoRejoin(displayName);
      if (!result) return;

      const { session, role } = result;
      setCrewSession(session);
      setCrewRole(role);

      if (role === 'leader') {
        crewService.subscribeAsLeader(session.id, () => {});
      } else {
        crewService.subscribeAsMember(session.id, (scene) => {
          dockedControllerRef.current?.applyCloudScene(scene);
        });
        // Apply last known scene immediately
        const lastScene = await crewService.fetchLastScene(session.id).catch(() => null);
        if (lastScene) {
          setTimeout(() => dockedControllerRef.current?.applyCloudScene(lastScene), 500);
        }
      }
    };
    // Delay slightly to let auth session restore
    const t = setTimeout(tryRejoin, 2000);
    return () => clearTimeout(t);
  }, []);

  // Bind BLE Notification Hardware Sync Hook
  useEffect(() => {
    setOnDataReceived((deviceId: string, payload: number[]) => {
      const payloadHex = payload.map(b => b.toString(16).padStart(2,'0').toUpperCase()).join(' ');
      setLastRawNotification({ deviceId, payloadHex });

      // ── Diagnostics: upload every raw notification to Supabase ──────────────
      // Fire-and-forget: never blocks the UI or BLE pipeline
      const v2ConfigForUpload = ZenggeProtocol.parseHardwareSettingsResponse(payload);
      const v1ConfigForUpload = ZenggeProtocol.parseHardwareConfig(payload);
      const parsedOk = !!(v2ConfigForUpload || v1ConfigForUpload);
      const pts = v2ConfigForUpload?.ledPoints ?? v1ConfigForUpload?.points;
      const ict = v2ConfigForUpload?.icType;
      const icn = v2ConfigForUpload?.icName ?? v1ConfigForUpload?.stripType;
      const cs  = v2ConfigForUpload?.colorSorting ?? undefined;
      const co  = v2ConfigForUpload?.colorSortingName ?? v1ConfigForUpload?.sorting;
      const deviceName = allDevices.find((d: any) => d.id === deviceId)?.name ?? null;
      supabase.from('device_diagnostics').insert({
        device_id:     deviceId,
        device_name:   deviceName,
        payload_hex:   payloadHex,
        payload_bytes: payload.length,
        byte_0:        payload[0] ?? null,
        byte_2:        payload[2] ?? null,
        parsed_ok:     parsedOk,
        points:        pts ?? null,
        ic_type:       ict ?? null,
        ic_name:       icn ?? null,
        color_sorting: cs ?? null,
        color_order:   co ?? null,
      }).then(({ error }: any) => {
        if (error) console.warn('[Diagnostics] upload failed:', error.message);
        else console.log('[Diagnostics] uploaded', payload.length, 'bytes for', deviceId);
      });
      // ────────────────────────────────────────────────────────────────────────
      
      const v2Config = ZenggeProtocol.parseHardwareSettingsResponse(payload);
      const v1Config = ZenggeProtocol.parseHardwareConfig(payload);
      
      let configPoints: number | undefined;
      let configSegments: number | undefined;
      let configStripType: string | undefined;
      let configSorting: string | undefined;
      let configSortingIdx: number | undefined;
      let configIcType: number | undefined;

      if (v2Config) {
        configPoints = v2Config.ledPoints;
        configSegments = v2Config.segments;
        configStripType = v2Config.icName;
        configSorting = v2Config.colorSortingName;
        configSortingIdx = v2Config.colorSorting;   // numeric index — critical for hwSettings
        configIcType = v2Config.icType;
      } else if (v1Config) {
        configPoints = v1Config.points;
        configSegments = v1Config.segments;
        configStripType = v1Config.stripType;
        configSorting = v1Config.sorting;
        // Derive idx from string for v1
        configSortingIdx = ['RGB','RBG','GRB','GBR','BRG','BGR'].indexOf(configSorting ?? 'GRB');
        if (configSortingIdx < 0) configSortingIdx = 2; // default GRB
      }

      if (configPoints !== undefined && configSorting !== undefined) {
        // Prevent telemetry flooding by NOT emitting an event for passive continuous Sync packets
        setAllDevices(prev => prev.map(d => {
          if (d.id === deviceId) {
            const newD = { 
              ...d, 
              points: configPoints, 
              sorting: configSorting,
              colorSorting: configSortingIdx,   // numeric index propagated
              colorSortingName: configSorting,
              stripType: configStripType, 
              icType: configIcType,
              segments: configSegments,
              detected: true,                    // flag that this came from real hardware
            } as any as typeof d;
            // Mirror securely directly to persistent memory
            AsyncStorage.getItem('ng_device_configs').then(str => {
               const p = JSON.parse(str || '{}');
               p[deviceId] = { ...p[deviceId], ...newD };
               AsyncStorage.setItem('ng_device_configs', JSON.stringify(p));
            }).catch(()=>{});
            setDeviceConfigs((prevConfigs: any) => ({
              ...prevConfigs,
              [deviceId]: { ...(prevConfigs[deviceId] || {}), ...newD }
            }));
            return newD;
          }
          return d;
        }));
      }
    });
  }, [setOnDataReceived, setAllDevices]);

  // Analytics hidden trigger
  const [logsVisible, setLogsVisible] = useState(false);
  const [countdown, setCountdown] = useState<number | null>(null);
  const holdTimer = useRef<ReturnType<typeof setTimeout> | null>(null);
  const tickTimer = useRef<ReturnType<typeof setInterval> | null>(null);
  const pulseAnimConfig = useRef(new Animated.Value(1)).current;
  const tapCountRef = useRef(0);
  const tapTimerRef = useRef<NodeJS.Timeout | null>(null);
  const [isPinPromptVisible, setIsPinPromptVisible] = useState(false);
  const [pinInput, setPinInput] = useState('');

  const startLogPulse = () => {
    Animated.loop(
      Animated.sequence([
        Animated.timing(pulseAnimConfig, { toValue: 1.15, duration: 400, useNativeDriver: true }),
        Animated.timing(pulseAnimConfig, { toValue: 1, duration: 400, useNativeDriver: true }),
      ])
    ).start();
  };

  const stopLogPulse = () => {
    pulseAnimConfig.stopAnimation();
    pulseAnimConfig.setValue(1);
  };
  const handleLogoPress = () => {
    if (isActuallyConnected) return;
    
    tapCountRef.current += 1;
    
    // Reset the count if they stop tapping for 1500ms
    if (tapTimerRef.current) clearTimeout(tapTimerRef.current);
    tapTimerRef.current = setTimeout(() => {
      tapCountRef.current = 0;
      setCountdown(null);
      stopLogPulse();
    }, 1500);

    const taps = tapCountRef.current;
    if (taps === 1) {
        startLogPulse();
    }
    
    if (taps >= 5 && taps < 10) {
      setCountdown(10 - taps);
    } else if (taps >= 10) {
      if (tapTimerRef.current) clearTimeout(tapTimerRef.current);
      setCountdown(null);
      stopLogPulse();
      setPinInput('');
      setIsPinPromptVisible(true);
      tapCountRef.current = 0;
    }
  };

  const handleScan = () => {
    if (isScanning || isActuallyConnected) return;
    
    requestPermissions().then((granted) => {
      if (granted) {
        console.log('[SK8Lytz] Manual Scan Initiated');
        AppLogger.log('SCAN_STARTED');
        const scanStartTime = Date.now();
        isProvisioningTriggered.current = true;
        AsyncStorage.removeItem('ng_processed_devices');
        lastProcessedRef.current = '';
        allDevicesRef.current = []; // Clear ref immediately
        
        scanForPeripherals();
        
        // Manual/Simulator scan behavior
        setTimeout(() => {
          setAllDevices((prev: any[]) => {
            let newDevices = [...prev];
            const haloIds = ['sim-DE:M0:HA:L0:00:01', 'sim-DE:M0:HA:L0:00:02'];
            const soulIds = ['sim-DE:M0:S0:UL:00:01', 'sim-DE:M0:S0:UL:00:02'];
            
            if (demoHaloQueued) {
              haloIds.forEach((id, idx) => {
                if (!newDevices.some(d => d.id === id)) {
                  const device = { 
                    id, 
                    name: `HALOZ ${idx === 0 ? 'Left' : 'Right'} Skate`, 
                    type: 'HALOZ',
                    points: 11, 
                    segments: 2,
                    sorting: 'GRB',
                    stripType: 'WS2812B',
                    rssi: -45 - Math.floor(Math.random() * 20),
                    serviceUUIDs: [ZENGGE_SERVICE_UUID],
                    manufacturerData: 'AAAAAAAAAAAz'
                  } as any;
                  newDevices.push(device);
                  AppLogger.log('DEVICE_DISCOVERED', { 
                    id, 
                    name: device.name, 
                    type: device.type, 
                    rssi: device.rssi,
                    points: device.points,
                    segments: device.segments,
                    sorting: device.sorting,
                    stripType: device.stripType
                  });
                }
              });
            }
            if (demoSoulQueued) {
              soulIds.forEach((id, idx) => {
                if (!newDevices.some(d => d.id === id)) {
                  const device = { 
                    id, 
                    name: `SOULZ ${idx === 0 ? 'Left' : 'Right'} Skate`, 
                    type: 'SOULZ',
                    points: 43, 
                    segments: 1,
                    sorting: 'GRB',
                    stripType: 'WS2812B',
                    rssi: idx === 1 ? -85 : -42 - Math.floor(Math.random() * 20),
                    serviceUUIDs: [ZENGGE_SERVICE_UUID],
                    manufacturerData: 'AAAAAAAAAAAz'
                  } as any;
                  newDevices.push(device);
                  AppLogger.log('DEVICE_DISCOVERED', { 
                    id, 
                    name: device.name, 
                    type: device.type, 
                    rssi: device.rssi,
                    points: device.points,
                    segments: device.segments,
                    sorting: device.sorting,
                    stripType: device.stripType
                  });
                }
              });
            }
            allDevicesRef.current = newDevices as any;
            return newDevices;
          });
        }, 150);

        // Ensure we scan for at least 5 seconds for visual impact
        const waitTime = Math.max(5000, Platform.OS === 'web' ? 5000 : 7000);
        setTimeout(() => {
          setIsDeviceListCollapsed(false);
          runAutoProvisioning();
        }, waitTime);
      }
    });
  };


  useEffect(() => {
    customGroupsRef.current = customGroups;
  }, [customGroups]);

  useEffect(() => {
    // 1. Load and clean custom groups
    AsyncStorage.getItem('ng_custom_groups')
      .then(res => {
        if (res) {
          try { 
            const parsed = JSON.parse(res) || [];
            // Remove any groups containing simulated devices
            const cleanedGroups = parsed.filter((g: any) => !g.deviceIds.some((id: string) => id.startsWith('sim-')));
            if (cleanedGroups.length !== parsed.length) {
              AsyncStorage.setItem('ng_custom_groups', JSON.stringify(cleanedGroups)).catch(()=>{});
            }
            setCustomGroups(cleanedGroups);
          } catch(e) { console.warn('JSON parse error groups', e); }
        }
      })
      .catch(e => console.warn('AsyncStorage error custom groups', e));

    // 2. Load and clean device configs
    AsyncStorage.getItem('ng_device_configs')
      .then(res => {
        if (res) {
          try {
            const configs = JSON.parse(res) || {};
            let changed = false;
            for (const key in configs) {
              if (key.startsWith('sim-')) {
                delete configs[key];
                changed = true;
              }
            }
            if (changed) {
              AsyncStorage.setItem('ng_device_configs', JSON.stringify(configs)).catch(()=>{});
            }
            setDeviceConfigs(configs);
          } catch(e) { console.warn('JSON parse error configs', e); }
        }
      })
      .catch(e => console.warn('AsyncStorage error configs', e));

    // 3. Load and clean processed devices log
    AsyncStorage.getItem('ng_processed_devices')
      .then(res => {
        if (res) {
          try {
            const processed = JSON.parse(res) || [];
            const cleaned = processed.filter((id: string) => !id.startsWith('sim-'));
            if (cleaned.length !== processed.length) {
              AsyncStorage.setItem('ng_processed_devices', JSON.stringify(cleaned)).catch(()=>{});
            }
          } catch(e) {}
        }
      });
  }, []);

  const runAutoProvisioning = useCallback(async () => {
    if (!isProvisioningTriggered.current) return;
    isProvisioningTriggered.current = false;

    console.log('[SK8Lytz] Provisioning...');
    const currentDevices = allDevicesRef.current;
    if (currentDevices.length === 0) return;

    const soulzDevices = currentDevices.filter(d => ((d as any).points || (d.name?.toLowerCase().includes('soul') ? 43 : 16)) >= 20);
    const halozDevices = currentDevices.filter(d => ((d as any).points || (d.name?.toLowerCase().includes('soul') ? 43 : 16)) < 20);
    
    let updatedGroups = [...customGroupsRef.current];
    let didUpdateGroups = false;
    let didUpdateConfigs = false;
    
    const [resConfigs, resProcessed] = await Promise.all([
      AsyncStorage.getItem('ng_device_configs'),
      AsyncStorage.getItem('ng_processed_devices')
    ]);

    let configs: any = {};
    if (resConfigs) { try { configs = JSON.parse(resConfigs); } catch(e) {} }

    let processed: string[] = [];
    if (resProcessed) { try { processed = JSON.parse(resProcessed); } catch(e) {} }
    let didUpdateProcessed = false;
    
    const checkAndGroup = (devicesToProcess: any[], targetGroupName: string, typeVal: 'HALOZ' | 'SOULZ', pointsVal: number) => {
      const unprocessed = devicesToProcess.filter(d => 
        !processed.includes(d.id) && 
        !updatedGroups.some(g => g.deviceIds.includes(d.id))
      );

      if (unprocessed.length >= 2) {
        const leftId = unprocessed[0].id;
        const rightId = unprocessed[1].id;
        processed.push(leftId, rightId);
        didUpdateProcessed = true;

        const existingGroupIndex = updatedGroups.findIndex(g => g.name === targetGroupName);
        if (existingGroupIndex > -1) {
          let target = updatedGroups[existingGroupIndex];
          if (!target.deviceIds.includes(leftId) || !target.deviceIds.includes(rightId)) {
            const newIds = Array.from(new Set([...target.deviceIds, leftId, rightId]));
            updatedGroups[existingGroupIndex] = { ...target, deviceIds: newIds };
            didUpdateGroups = true;
          }
        } else {
          updatedGroups.push({
            id: `group-${Date.now()}-${typeVal}-${Math.floor(Math.random() * 1000)}`,
            name: targetGroupName,
            deviceIds: [leftId, rightId],
            type: typeVal,
            isGroup: true
          });
          didUpdateGroups = true;
        }
        
        const names = [`${typeVal} Left Skate`, `${typeVal} Right Skate`];
        [leftId, rightId].forEach((id, idx) => {
          if (!configs[id] || configs[id].name !== names[idx]) {
            configs[id] = { ...configs[id], name: names[idx], type: typeVal, points: pointsVal };
            didUpdateConfigs = true;
          }
        });
      }
    };

    checkAndGroup(soulzDevices, 'SOULZ SK8Lytz', 'SOULZ', 43);
    checkAndGroup(halozDevices, 'HALOZ SK8Lytz', 'HALOZ', 16);

    const storagePromises = [];
    if (didUpdateProcessed) {
      storagePromises.push(AsyncStorage.setItem('ng_processed_devices', JSON.stringify(processed)));
    }
    if (didUpdateGroups) {
      storagePromises.push(AsyncStorage.setItem('ng_custom_groups', JSON.stringify(updatedGroups)));
    }
    if (didUpdateConfigs) {
      storagePromises.push(AsyncStorage.setItem('ng_device_configs', JSON.stringify(configs)));
    }
    if (storagePromises.length > 0) await Promise.all(storagePromises);

    if (didUpdateGroups) {
      customGroupsRef.current = updatedGroups;
      setCustomGroups(updatedGroups);
    }
    
    // Sync to Supabase if authenticated
    if ((didUpdateGroups || didUpdateConfigs) && supabase) {
      try {
        const { data: { session } } = await supabase.auth.getSession();
        if (session && session.user) {
          const userId = session.user.id;
          
          for (const group of updatedGroups) {
            // Upsert Group
            await supabase.from('registered_groups').upsert({
              id: group.id,
              user_id: userId,
              group_name: group.name,
              type: group.type,
              created_at: new Date().toISOString()
            }, { onConflict: 'id' }).catch(() => {});
            
            // Upsert Devices in Group
            for (const deviceId of group.deviceIds) {
              const c = configs[deviceId];
              if (c) {
                await supabase.from('registered_devices').upsert({
                  id: deviceId,
                  user_id: userId,
                  group_id: group.id,
                  custom_name: c.name || 'Unknown',
                  points: c.points || 0,
                  segments: c.segments || 0,
                  sorting: c.sorting || 'GRB',
                  strip_type: c.stripType || 'UNKNOWN'
                }, { onConflict: 'id' }).catch(() => {});
              }
            }
          }
        }
      } catch (e) {
        console.warn('Supabase sync error during provisioning:', e);
      }
    }
    
    if (didUpdateConfigs) {
      setAllDevices(prev => {
        let morphed = false;
        const next = prev.map(d => {
          const c = configs[d.id];
          if (c && (d.name !== c.name || (d as any).sorting !== c.sorting || (d as any).stripType !== c.stripType)) {
            morphed = true;
            return { ...d, name: c.name, points: c.points, sorting: c.sorting, stripType: c.stripType } as any;
          }
          return d;
        });
        if (morphed) allDevicesRef.current = next;
        return morphed ? next : prev;
      });
    }
    
    // Explicitly collapse to focus on grouped controls
    setIsDeviceListCollapsed(true);
    console.log('[SK8Lytz] Provisioning Complete.');
  }, []);






  const displayConnectedDevices = useMemo(() => {
    if (!mockConnected) {
      // Real hardware path — enrich each connected BLE device with its parsed hardware config
      // (points, segments, sorting, icType, etc.) from deviceConfigs populated by 0x63 BLE response
      return connectedDevices.map(d => {
        const cfg = deviceConfigs[(d as any).id] || {};
        return { ...d, ...cfg };
      });
    }

    // In Browser Demo / Mock mode, we pull the actual device objects from allDevices 
    // to ensure settings like 'points' are reflected after being edited.
    if (mockConnectedGroup) {
      const g = customGroups.find(x => x.id === mockConnectedGroup);
      if (g) {
        return allDevices
          .filter(d => g.deviceIds.includes(d.id))
          .map(d => ({ ...d, grouped: true, groupId: g.id, groupName: g.name, points: (d as any).points || (d.name?.toLowerCase().includes('soul') ? 43 : 16) }));
      }
    }

    const singleId = mockConnectedDevice || 'sim-soul-1';
    const single = allDevices.find(d => d.id === singleId);
    return single ? [{ ...single, grouped: false, points: (single as any).points || (single.name?.toLowerCase().includes('soul') ? 43 : 16) }] : [];
  }, [mockConnected, mockConnectedDevice, mockConnectedGroup, allDevices, connectedDevices, updateTrigger, customGroups, deviceConfigs]);

  const isActuallyConnected = displayConnectedDevices.length > 0;
  const isGrouped = displayConnectedDevices.length > 1 && displayConnectedDevices.every(d => (d as any).grouped);

  const handleDisconnect = useCallback(() => {
    if (IS_BROWSER_DEMO) {
      setMockConnected(false);
      setMockConnectedDevice(null);
      setMockConnectedGroup(null);
    }
    disconnectFromDevice();
  }, [IS_BROWSER_DEMO, disconnectFromDevice]);

  useEffect(() => {
    const handleBackPress = () => {
      if (isTestModeActive) {
        setIsTestModeActive(false);
        return true; // intercept
      }
      if (isActuallyConnected) {
        handleDisconnect();
        return true; // intercept and exit to scanner
      }
      return false; // allow native exit
    };

    const backHandler = BackHandler.addEventListener('hardwareBackPress', handleBackPress);
    return () => backHandler.remove();
  }, [isTestModeActive, isActuallyConnected, handleDisconnect]);

  // Handle Swipe-to-Back natively for Visualizer screens (IOS & Android edge swipe)
  const edgePanResponder = useRef(
    PanResponder.create({
      onMoveShouldSetPanResponder: (evt, gestureState) => {
        const startX = evt.nativeEvent.pageX - gestureState.dx;
        // Only trigger if starting from the far left edge (< 40px) moving rightwards (simulating back swipe)
        if (startX < 50 && gestureState.dx > 15 && Math.abs(gestureState.dx) > Math.abs(gestureState.dy)) {
          return true;
        }
        return false;
      },
      onPanResponderRelease: (evt, gestureState) => {
        if (gestureState.dx > 60) {
          if (isTestModeActive) setIsTestModeActive(false);
          else if (isActuallyConnected) handleDisconnect();
        }
      },
      onPanResponderTerminate: (evt, gestureState) => {
        if (gestureState.dx > 60) {
          if (isTestModeActive) setIsTestModeActive(false);
          else if (isActuallyConnected) handleDisconnect();
        }
      }
    })
  ).current;

  const handleGlobalPowerToggle = (deviceIds: string[], forceState?: boolean) => {
    // If forceState is provided, use it, else default to toggling based on first device's state
    const targetState = forceState !== undefined ? forceState : !(powerStates[deviceIds[0]] ?? true);
    
    // Update local state optimistic
    const newStates = { ...powerStates };
    deviceIds.forEach(id => { 
        newStates[id] = targetState; 
        
        // Broadcast BLE command targetted to each device
        if (targetState) {
            writeToDevice(ZenggeProtocol.turnOn(), id);
        } else {
            writeToDevice(ZenggeProtocol.turnOff(), id);
        }
    });
    setPowerStates(newStates);
  };


  const toggleSelect = (id: string) => {
    if (selectedIds.includes(id)) {
      setSelectedIds(selectedIds.filter(i => i !== id));
      if (selectedIds.length === 1) setIsSelectionMode(false);
    } else {
      setSelectedIds([...selectedIds, id]);
      setIsSelectionMode(true);
    }
  };

  const openCreateGroup = () => {
    if (selectedIds.length === 0) return;
    const selectedDevices = allDevices.filter(d => selectedIds.includes(d.id));
    const firstType = selectedDevices[0]?.name?.toLowerCase().includes('soul') ? 'SOULZ' : 'HALOZ';
    const allSame = selectedDevices.every(d => (d.name?.toLowerCase().includes('soul') ? 'SOULZ' : 'HALOZ') === firstType);

    if (!allSame) {
      alert("Please only group devices of the same type (e.g. two SOULZ).");
      return;
    }

    setGroupModalMode('create');
    setIsGroupModalVisible(true);
  };

  const deleteGroup = (id: string) => {
    const updatedGroups = customGroups.filter(g => g.id !== id);
    setCustomGroups(updatedGroups);
    AsyncStorage.setItem('ng_custom_groups', JSON.stringify(updatedGroups)).catch(() => {});
    
    if (mockConnectedGroup === id) {
      setMockConnected(false);
      setMockConnectedGroup(null);
    }
    setIsGroupModalVisible(false);
  };

  const saveGroup = async (name: string, deviceIds: string[]) => {
    let newGroups = customGroups;
    if (groupModalMode === 'create') {
      const existing = customGroups.find(g => g.name.toLowerCase() === name.toLowerCase());
      if (existing) {
        newGroups = customGroups.map(g => g.id === existing.id ? { ...g, deviceIds: Array.from(new Set([...g.deviceIds, ...deviceIds])) } : g);
      } else {
        const newGroupId = `group-${Date.now()}`;
        newGroups = [...customGroups, { id: newGroupId, name, isGroup: true, deviceIds }];
      }
      setIsSelectionMode(false);
      setSelectedIds([]);
    } else if (groupModalMode === 'rename' && editingGroupId) {
      if (deviceIds.length === 0) {
        deleteGroup(editingGroupId);
        return;
      }
      newGroups = customGroups.map(g => g.id === editingGroupId ? { ...g, name, deviceIds } : g);
    }
    setCustomGroups(newGroups);
    await AsyncStorage.setItem('ng_custom_groups', JSON.stringify(newGroups));
  };

  useEffect(() => {
    requestPermissions();
  }, []);

  // (Removed redundant second setOnDataReceived binding)

  const [isSettingsVisible, setIsSettingsVisible] = useState(false);
  const [selectedDeviceForSettingsId, setSelectedDeviceForSettingsId] = useState<string | null>(null);

  const selectedDeviceForSettings = useMemo(() => {
    if (!selectedDeviceForSettingsId) return null;
    return allDevices.find(d => d.id === selectedDeviceForSettingsId) || null;
  }, [selectedDeviceForSettingsId, allDevices]);

  const openSettings = (device: any) => {
    setSelectedDeviceForSettingsId(device.id);
    
    // Query hardware settings with correct 0x63 command (not legacy 0x10)
    if (connectedDevices.some(d => d.id === device.id)) {
       setTimeout(() => writeToDevice(ZenggeProtocol.queryHardwareSettings(false), device.id), 300);
    }
    
    setIsSettingsVisible(true);
  };

  const saveSettings = async (settings: DeviceSettings) => {
    if (selectedDeviceForSettings) {
      // Avoid direct mutations, use functional state updates instead
      let finalGroupId = settings.groupId;

      // Capture implicit group associations assigned through the Hardware settings modal
      if (settings.grouped && settings.groupName && !settings.groupId) {
        // Find existing group by name or create a fresh one natively
        const existingGroup = customGroups.find(g => g.name.toLowerCase() === settings.groupName?.toLowerCase());
        if (existingGroup) {
          finalGroupId = existingGroup.id;
          if (!existingGroup.deviceIds.includes(selectedDeviceForSettings.id)) {
             const newGroups = customGroups.map(g => g.id === existingGroup.id ? {...g, deviceIds: [...g.deviceIds, selectedDeviceForSettings.id]} : g);
             setCustomGroups(newGroups);
             AsyncStorage.setItem('ng_custom_groups', JSON.stringify(newGroups)).catch(() => {});
          }
        } else {
          finalGroupId = `group-${Date.now()}`;
          const newGroups = [...customGroups, { id: finalGroupId, name: settings.groupName, isGroup: true, deviceIds: [selectedDeviceForSettings.id] }];
          setCustomGroups(newGroups);
          AsyncStorage.setItem('ng_custom_groups', JSON.stringify(newGroups)).catch(() => {});
        }
      } else if (settings.grouped && settings.groupId) {
          const existingGroup = customGroups.find(g => g.id === settings.groupId);
          if (existingGroup && !existingGroup.deviceIds.includes(selectedDeviceForSettings.id)) {
             const newGroups = customGroups.map(g => g.id === existingGroup.id ? {...g, deviceIds: [...g.deviceIds, selectedDeviceForSettings.id]} : g);
             setCustomGroups(newGroups);
             AsyncStorage.setItem('ng_custom_groups', JSON.stringify(newGroups)).catch(() => {});
          }
      } else if (!settings.grouped) {
          const newGroups = customGroups.map(g => ({...g, deviceIds: g.deviceIds.filter((id: string) => id !== selectedDeviceForSettings.id)}));
          setCustomGroups(newGroups);
          AsyncStorage.setItem('ng_custom_groups', JSON.stringify(newGroups)).catch(() => {});
      }

      setAllDevices((prev: any[]) => {
        const next = prev.map(d => 
          d.id === selectedDeviceForSettings.id 
            ? { ...d, name: settings.name, type: settings.type, points: settings.points, segments: settings.segments, sorting: settings.sorting, stripType: settings.stripType, groupId: finalGroupId } 
            : d
        );
        allDevicesRef.current = next as any;
        return next;
      });
      
      setUpdateTrigger(prev => prev + 1);
 
       try {
         const stored = await AsyncStorage.getItem('ng_device_configs');
         const configs = stored ? JSON.parse(stored) : {};
         configs[selectedDeviceForSettings.id] = { ...settings, groupId: finalGroupId };
         await AsyncStorage.setItem('ng_device_configs', JSON.stringify(configs));
         
         AppLogger.log('HARDWARE_CONFIG_CHANGED', {
           deviceId: selectedDeviceForSettings.id,
           name: settings.name,
           type: settings.type,
           points: settings.points,
           segments: settings.segments,
           sorting: settings.sorting,
           stripType: settings.stripType,
         });

         // Log rename separately for device audit trail
         const previousName = selectedDeviceForSettings.name;
         if (settings.name && settings.name !== previousName) {
           AppLogger.log('DEVICE_RENAMED', {
             deviceId: selectedDeviceForSettings.id,
             oldName: previousName || 'Unknown',
             newName: settings.name,
           });
         }
      } catch (e) { console.error('Failed to persist settings', e); }
    }
    setIsSettingsVisible(false);
  };

  const activeHwSettings = useMemo(() => {
    const raw = displayConnectedDevices[0] as any;
    // Merge persisted deviceConfigs on top — ensures 0x63 ping results always surface
    // even when displayConnectedDevices comes from useBLE.connectedDevices (real hardware path)
    const cached = raw?.id ? (deviceConfigs[raw.id] || {}) : {};
    const d = { ...raw, ...cached };
    const s = d?.sorting || d?.colorSortingName || 'GRB';
    const sortingIdx = s === 'RGB' ? 0 : s === 'RBG' ? 1 : s === 'GRB' ? 2 : s === 'GBR' ? 3 : s === 'BRG' ? 4 : s === 'BGR' ? 5 : 2;
    return {
      ledPoints: d?.points || d?.ledPoints || (d?.name?.toLowerCase().includes('soul') ? 43 : 16),
      segments:  d?.segments || 1,
      icType:    d?.icType || 1,
      icName:    d?.icName || d?.stripType || 'WS2812B',
      colorSorting: d?.colorSorting ?? sortingIdx,
      colorSortingName: s,
      detected:  d?.detected || false,
    };
  }, [displayConnectedDevices, deviceConfigs]);

  const MemoizedSk8lytzController = useMemo(() => {
    if (!isActuallyConnected) return null;

    if (isTestModeActive) {
      return null;
    }

    return (
      <Animated.View {...edgePanResponder.panHandlers} style={{ flex: 1, backgroundColor: 'transparent' }}>
          <DockedController
            ref={dockedControllerRef}
            hwSettings={activeHwSettings}
            lockedProduct={
              (displayConnectedDevices[0] as any)?.type || 
              ((displayConnectedDevices[0] as any)?.points 
                ? ((displayConnectedDevices[0] as any).points < 20 ? 'HALOZ' : 'SOULZ') 
                : ((displayConnectedDevices[0] as any)?.name?.toLowerCase().includes('soul') ? 'SOULZ' : 'HALOZ'))
            }
            isPaired={isGrouped}
            points={(displayConnectedDevices[0] as any).points}
            devices={displayConnectedDevices as any}
            onLongPressDevice={openSettings}
            writeToDevice={writeToDevice}
            isPoweredOn={displayConnectedDevices.some(d => powerStates[d.id] ?? true)}
            onDisconnect={handleDisconnect}
            crewRole={crewRole}
            onCrewSceneChange={(scene: Record<string, any>) => crewService.broadcastScene(scene)}
          />
      </Animated.View>
    );
  }, [isActuallyConnected, isGrouped, displayConnectedDevices, writeToDevice, powerStates, isTestModeActive, activeHwSettings, crewRole]);

  const renderItem = useCallback(({ item }: { item: any }) => {
    const cachedConfig = deviceConfigs?.[item.id] || {};
    const mergedItem = { ...item, ...cachedConfig };

    return (
    <View style={{ paddingHorizontal: Layout.padding }}>
      <DeviceItem
        device={mergedItem}
        isConnected={displayConnectedDevices.some(d => d.id === item.id)}
        isSelectionMode={isSelectionMode}
        isSelected={selectedIds.includes(item.id)}
        onPress={async () => {
          if (isSelectionMode) {
            toggleSelect(item.id);
            return;
          }
          if (item.id.startsWith('sim-')) {
            setMockConnected(true);
            setMockConnectedDevice(item.id);
            setDeviceConfigs(prev => {
                const fw = 'v2.0.1.DEMO';
                const next = { ...prev, [item.id]: { ...(prev?.[item.id] || {}), firmware: fw } };
                AsyncStorage.setItem('ng_device_configs', JSON.stringify(next)).catch(() => {});
                return next;
            });
            return;
          }
          const fw = await connectToDevice(item);
          if (fw) {
            setDeviceConfigs(prev => {
                const next = { ...prev, [item.id]: { ...(prev?.[item.id] || {}), firmware: fw } };
                AsyncStorage.setItem('ng_device_configs', JSON.stringify(next)).catch(() => {});
                return next;
            });
          }
          
          writeToDevice(ZenggeProtocol.queryHardwareSettings(false), item.id);

          if (IS_BROWSER_DEMO) {
            setMockConnected(true);
            setMockConnectedDevice(item.id);
          }
        }}
        onLongPress={() => {
          openSettings(mergedItem);
        }}
        showGroupIcon={false}
        isPoweredOn={powerStates[item.id] ?? true}
        onPowerToggle={() => handleGlobalPowerToggle([item.id])}
      />
    </View>
    ); // close return
  }, [displayConnectedDevices, isSelectionMode, selectedIds, powerStates, deviceConfigs]);

  const BluetoothWarningBanner = useMemo(() => {
    if (isBluetoothEnabled || Platform.OS === 'web') return null;
    return (
      <TouchableOpacity 
        onPress={() => Linking.openSettings()}
        style={{ 
          backgroundColor: Colors.error, 
          padding: 16, 
          alignItems: 'center', 
          justifyContent: 'center', 
          flexDirection: 'row', 
          gap: 12,
          borderBottomWidth: 1,
          borderBottomColor: 'rgba(255,255,255,0.2)'
        }}
        activeOpacity={0.9}
      >
        <MaterialCommunityIcons name="alert-circle" size={24} color="#FFF" />
        <Text style={{ color: '#FFF', fontWeight: '900', fontSize: 14, textTransform: 'uppercase', letterSpacing: 0.5 }}>
          Bluetooth Disabled or Permissions Denied!
        </Text>
        <MaterialCommunityIcons name="chevron-right" size={20} color="#FFF" />
      </TouchableOpacity>
    );
  }, [isBluetoothEnabled]);

  const renderDashboardHeader = () => (
    <View style={{
      paddingHorizontal: Layout.padding,
      paddingTop: (Platform.OS === 'android' ? (StatusBar.currentHeight || 20) : 0) + (isActuallyConnected ? 12 : 16),
      paddingBottom: isActuallyConnected ? 2 : 8,
    }}>
      {isActuallyConnected ? (
        /* ── Connected: compact left-aligned row ── */
        <View style={{ flexDirection: 'row', alignItems: 'center' }}>
          <TouchableOpacity activeOpacity={1} onPress={handleLogoPress} style={{ position: 'relative' }}>
            <Image source={require('../../assets/logo.png')} style={{ width: 70, height: 20 }} resizeMode="contain" tintColor={Colors.text} />
            {countdown !== null && (
              <Animated.View style={[styles.countdownBadge, { transform: [{ scale: pulseAnimConfig }] }]}>
                <Text style={styles.countdownText}>{countdown}</Text>
              </Animated.View>
            )}
          </TouchableOpacity>

          {/* Connected device status */}
          <View style={{ marginLeft: 12 }}>
            {(() => {
              const connectedCount = displayConnectedDevices.length;
              let expectedCount = 1;
              const firstDevice = displayConnectedDevices[0] as any;
              if (firstDevice?.grouped && firstDevice?.groupId) {
                const group = customGroups.find(g => g.id === firstDevice.groupId);
                if (group) expectedCount = group.deviceIds.length;
              }
              let statusColor = connectedCount === 0 ? Colors.error : connectedCount < expectedCount ? '#FFA500' : Colors.success;
              return (
                <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                  <View style={{ width: 6, height: 6, borderRadius: 3, backgroundColor: statusColor, marginRight: 6 }} />
                  <Text style={[Typography.caption, { color: statusColor, fontSize: 10, fontWeight: 'bold' }]}>DISCOVERED ({connectedCount})</Text>
                </View>
              );
            })()}
          </View>

          <View style={{ flex: 1 }} />

          {/* Disconnect + power controls */}
          {!isTestModeActive && (
            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
              <TouchableOpacity style={[styles.disconnectButtonSmall, { paddingVertical: 4, paddingHorizontal: 8, marginRight: 8 }]} onPress={handleDisconnect} activeOpacity={0.8}>
                <Text style={[styles.disconnectButtonTextSmall, { fontSize: 10 }]}>DISCONNECT</Text>
              </TouchableOpacity>
              {(() => {
                const allIds = displayConnectedDevices.map(d => d.id);
                const isGlobalPoweredOn = allIds.every(id => powerStates[id] ?? true);
                return (
                  <TouchableOpacity
                    style={{ width: 32, height: 32, borderRadius: 16, backgroundColor: isGlobalPoweredOn ? 'rgba(0,240,255,0.15)' : 'rgba(255,255,255,0.1)', justifyContent: 'center', alignItems: 'center', borderWidth: 1, borderColor: isGlobalPoweredOn ? 'rgba(0,240,255,0.3)' : 'rgba(255,255,255,0.2)' }}
                    onPress={() => handleGlobalPowerToggle(allIds)}
                    activeOpacity={0.6}
                  >
                    <MaterialCommunityIcons name="power" size={18} color={isGlobalPoweredOn ? Colors.primary : Colors.textMuted} />
                  </TouchableOpacity>
                );
              })()}
            </View>
          )}
        </View>
      ) : (
        /* ── Not connected: 3-column layout — no overlapping ── */
        <View style={{ flexDirection: 'row', alignItems: 'center' }}>

          {/* LEFT: help + user pill */}
          <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8, flex: 1 }}>
            <TouchableOpacity
              style={{ width: 34, height: 34, borderRadius: 17, borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)', alignItems: 'center', justifyContent: 'center' }}
              onPress={() => setIsSupportModalVisible(true)}
            >
              <MaterialCommunityIcons name="help-circle-outline" size={18} color={Colors.textMuted} />
            </TouchableOpacity>

            <TouchableOpacity
              onPress={handleLogout}
              style={{
                flexDirection: 'row', alignItems: 'center',
                paddingHorizontal: 8, paddingVertical: 5,
                borderRadius: 20, borderWidth: 1, gap: 5,
                borderColor: isOfflineMode ? 'rgba(255,170,0,0.35)' : 'rgba(0,240,255,0.25)',
                backgroundColor: isOfflineMode ? 'rgba(255,170,0,0.08)' : 'rgba(0,240,255,0.06)',
              }}
            >
              <View style={{
                width: 7, height: 7, borderRadius: 4,
                backgroundColor: isOfflineMode ? '#FFA500' : Colors.success,
                shadowColor: isOfflineMode ? '#FFA500' : Colors.success,
                shadowOpacity: 0.8, shadowRadius: 4, elevation: 2,
              }} />
              <Text style={{ color: Colors.text, fontSize: 11, fontWeight: '700', maxWidth: 80 }} numberOfLines={1}>
                {authUsername || 'Skater'}
              </Text>
              <View style={{
                paddingHorizontal: 4, paddingVertical: 1, borderRadius: 5,
                backgroundColor: isOfflineMode ? 'rgba(255,170,0,0.2)' : 'rgba(0,200,100,0.2)',
              }}>
                <Text style={{ fontSize: 8, fontWeight: '900', letterSpacing: 0.5, color: isOfflineMode ? '#FFA500' : Colors.success }}>
                  {isOfflineMode ? 'OFFLINE' : 'ONLINE'}
                </Text>
              </View>
              <MaterialCommunityIcons name="logout" size={12} color={Colors.error} style={{ opacity: 0.8 }} />
            </TouchableOpacity>
          </View>

          {/* CENTER: logo */}
          <TouchableOpacity activeOpacity={1} onPress={handleLogoPress} style={{ position: 'relative', alignItems: 'center' }}>
            <Image source={require('../../assets/logo.png')} style={{ width: 110, height: 32 }} resizeMode="contain" tintColor={Colors.text} />
            {countdown !== null && (
              <Animated.View style={[styles.countdownBadge, { transform: [{ scale: pulseAnimConfig }] }]}>
                <Text style={styles.countdownText}>{countdown}</Text>
              </Animated.View>
            )}
          </TouchableOpacity>

          {/* RIGHT: theme toggle */}
          <View style={{ flex: 1, alignItems: 'flex-end' }}>
            <TouchableOpacity onPress={toggleTheme} style={{ width: 34, height: 34, borderRadius: 17, borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)', alignItems: 'center', justifyContent: 'center' }}>
              <MaterialCommunityIcons name={isDark ? 'white-balance-sunny' : 'moon-waning-crescent'} size={18} color={Colors.primary} />
            </TouchableOpacity>
          </View>
        </View>
      )}

      {/* Accent line under logo when not connected */}
      {!isActuallyConnected && (
        <View style={{ height: 2, width: 30, backgroundColor: Colors.secondary, marginTop: 6, borderRadius: 1, alignSelf: 'center' }} />
      )}
    </View>
  );

  return (
    <SafeAreaView style={styles.safeArea}>
      {BluetoothWarningBanner}
      <View style={styles.container}>

        {isActuallyConnected ? (
          <View style={{ flex: 1 }}>
            <View style={{ paddingBottom: 16 }}>
              {renderDashboardHeader()}
            </View>
            <View style={{ flex: 1 }}>
              {MemoizedSk8lytzController}
            </View>
          </View>
        ) : (
          <FlatList
            style={{ flex: 1 }}
            ListHeaderComponent={
              <View style={{ paddingBottom: 16 }}>
                {renderDashboardHeader()}

                <View style={{ paddingHorizontal: Layout.padding }}>
                  {!isTestModeActive ? (
                  <View style={{ height: 380, overflow: 'hidden', alignItems: 'center', justifyContent: 'center', marginTop: 5, width: '100%' }}>
                      <ScannerAnimation 
                         deviceCount={allDevices.length} 
                         isScanning={isScanning}
                         isScanProbing={isScanProbing}
                         onPress={handleScan}
                      />
                  </View>
                ) : null}
                </View>

                {/* ── Crew status pill (shown when in an active crew) ── */}
                {crewSession && (
                  <TouchableOpacity
                    style={{
                      flexDirection: 'row', alignItems: 'center', gap: 8,
                      marginHorizontal: Layout.padding, marginTop: 8, marginBottom: 4,
                      backgroundColor: crewRole === 'leader' ? 'rgba(255,170,0,0.12)' : 'rgba(0,170,255,0.1)',
                      borderWidth: 1, borderColor: crewRole === 'leader' ? 'rgba(255,170,0,0.5)' : 'rgba(0,170,255,0.4)',
                      borderRadius: 20, paddingHorizontal: 14, paddingVertical: 7,
                    }}
                    onPress={() => setIsCrewModalVisible(true)}
                  >
                    <Text style={{ fontSize: 13 }}>{crewRole === 'leader' ? '👑' : '👥'}</Text>
                    <Text style={{ color: crewRole === 'leader' ? '#FFAA00' : '#00AAFF', fontSize: 13, fontWeight: '700', flex: 1 }}>
                      {crewRole === 'leader'
                        ? `CREW LIVE · ${crewSession.name}`
                        : `CREW SYNC · ${crewSession.name}`}
                    </Text>
                    <MaterialCommunityIcons name="chevron-right" size={16} color={crewRole === 'leader' ? '#FFAA00' : '#00AAFF'} />
                  </TouchableOpacity>
                )}

                {/* ── DEVICES / CREW tab bar ── */}
                <View style={{
                  flexDirection: 'row', marginHorizontal: Layout.padding,
                  marginTop: crewSession ? 4 : 12, marginBottom: 4,
                  gap: 8,
                }}>
                  <TouchableOpacity
                    style={[
                      { flex: 1, paddingVertical: 8, borderRadius: 10, alignItems: 'center',
                        borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' },
                      scannerTab === 'DEVICES' && { backgroundColor: Colors.primary, borderColor: Colors.primary },
                    ]}
                    onPress={() => setScannerTab('DEVICES')}
                  >
                    <Text style={{ color: scannerTab === 'DEVICES' ? '#000' : Colors.textMuted, fontWeight: '700', fontSize: 13 }}>📡  DEVICES</Text>
                  </TouchableOpacity>
                  <TouchableOpacity
                    style={[
                      { flex: 1, paddingVertical: 8, borderRadius: 10, alignItems: 'center',
                        borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' },
                      scannerTab === 'CREW' && { backgroundColor: '#FFAA00', borderColor: '#FFAA00' },
                    ]}
                    onPress={() => { setScannerTab('CREW'); setIsCrewModalVisible(true); }}
                  >
                    <Text style={{ color: scannerTab === 'CREW' ? '#000' : Colors.textMuted, fontWeight: '700', fontSize: 13 }}>👥  CREW</Text>
                  </TouchableOpacity>
                </View>

                <View style={{ paddingHorizontal: Layout.padding }}>
                {customGroups.length > 0 && (
                  <View style={{ marginTop: 20 }}>
                    <Text style={[Typography.title, { marginBottom: 12, paddingHorizontal: 4, color: Colors.primary }]}>Groups</Text>
                    {customGroups.map((group) => {
                        const matchedDevices = allDevices.filter(d => group.deviceIds.includes(d.id));
                        const groupRssis = group.deviceIds.map((id: string) => {
                          const found = allDevices.find(d => d.id === id);
                          return found ? (found.rssi ?? null) : null;
                        });
                        
                        return (
                          <DeviceItem
                            key={group.id}
                            device={{
                              ...group,
                              connectedCount: matchedDevices.length,
                              rssiList: groupRssis
                            }}
                        isConnected={mockConnectedGroup === group.id}
                        isSelectionMode={false}
                        isSelected={false}
                        onPress={async () => {
                          if (IS_BROWSER_DEMO) {
                            setMockConnected(true);
                            setMockConnectedGroup(group.id);
                          }
                          
                          const devicesToConnect = allDevices.filter(d => group.deviceIds.includes(d.id));
                          if (devicesToConnect.length > 0) {
                            await connectToDevices(devicesToConnect);
                            
                            const firstDev = devicesToConnect[0];
                            const cfg = deviceConfigs[(firstDev as any).id] || {};
                            const configPoints   = cfg.points    || (firstDev as any).points    || (firstDev.name?.toLowerCase().includes('soul') ? 43 : 16);
                            const configSegments = cfg.segments  || (firstDev as any).segments  || 1;
                            const configSorting  = cfg.sorting   || (firstDev as any).sorting   || 'GRB';
                            const configStrip    = cfg.stripType || (firstDev as any).stripType || 'WS2812B';
                            writeToDevice(ZenggeProtocol.writeHardwareSettingsByName(configPoints, configSegments, configStrip, configSorting));
                          }
                        }}
                        onLongPress={() => {
                          setEditingGroupId(group.id);
                          setGroupModalMode('rename');
                          setIsGroupModalVisible(true);
                        }}
                        showGroupIcon={true}
                        isPoweredOn={group.deviceIds.every((id: string) => powerStates[id] ?? true)}
                        onPowerToggle={() => handleGlobalPowerToggle(group.deviceIds)}
                        />
                      );
                    })}
                  </View>
                )}

                <>
                  <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginTop: 12, marginBottom: 12, paddingHorizontal: 4 }}>
                    <Text style={[Typography.title, { color: Colors.primary }]}>Available Devices</Text>
                    {allDevices.length > 0 && (
                      <TouchableOpacity onPress={() => setIsDeviceListCollapsed(!isDeviceListCollapsed)}>
                        <Text style={{ color: Colors.primary, fontWeight: 'bold', fontSize: 12 }}>
                          {isDeviceListCollapsed ? 'SHOW ALL' : 'HIDE'}
                        </Text>
                      </TouchableOpacity>
                    )}
                  </View>

                  {!isDeviceListCollapsed && (
                    <View style={{ flexDirection: 'row', justifyContent: 'space-around', marginBottom: 16, padding: 8, backgroundColor: 'rgba(255,255,255,0.03)', borderRadius: 12 }}>
                      <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                        <Text style={{ color: Colors.text, marginRight: 8, fontSize: 12, fontWeight: '600' }}>Demo HALOZ</Text>
                        <Switch
                          value={demoHaloQueued}
                          onValueChange={setDemoHaloQueued}
                          style={{ transform: [{ scaleX: 0.8 }, { scaleY: 0.8 }] }}
                          trackColor={{ false: 'rgba(255,255,255,0.1)', true: Colors.primary }}
                        />
                      </View>
                      <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                        <Text style={{ color: Colors.text, marginRight: 8, fontSize: 12, fontWeight: '600' }}>Demo SOULZ</Text>
                        <Switch
                          value={demoSoulQueued}
                          onValueChange={setDemoSoulQueued}
                          style={{ transform: [{ scaleX: 0.8 }, { scaleY: 0.8 }] }}
                          trackColor={{ false: 'rgba(255,255,255,0.1)', true: Colors.secondary }}
                        />
                      </View>
                    </View>
                  )}
                </>
                </View>
              </View>
            }
            data={!isDeviceListCollapsed ? [...allDevices]
              .sort((a, b) => (b.rssi ?? -999) - (a.rssi ?? -999))
              .map(d => {
                const cfg = deviceConfigs[(d as any).id] || {};
                // Prefer user-configured name over raw BLE advertisement name
                return { ...d, ...cfg };
              }) : []}
            keyExtractor={(item) => item.id}
            renderItem={renderItem}
            scrollEnabled={true}
            contentContainerStyle={{ paddingBottom: 40 }}

            ListEmptyComponent={
              !isDeviceListCollapsed ? (
                <View style={styles.emptyStateContainer}>
                  <Text style={[Typography.caption, { color: Colors.text, opacity: 0.7 }]}>
                    {isScanning ? 'Scanning...' : 'No devices found.'}
                  </Text>
                </View>
              ) : null
            }
        />
        )}
        <DeviceSettingsModal
          isVisible={isSettingsVisible}
          onClose={() => setIsSettingsVisible(false)}
          onSave={saveSettings}
          writeToDevice={writeToDevice}
          initialSettings={{
            name: selectedDeviceForSettings?.name || 'SOULZ',
            type: (selectedDeviceForSettings?.name?.toLowerCase().includes('soul') ? 'SOULZ' : 'HALOZ'),
            points: deviceConfigs[selectedDeviceForSettings?.id || '']?.points || (selectedDeviceForSettings as any)?.points || (selectedDeviceForSettings?.name?.toLowerCase().includes('soul') ? 43 : 8),
            segments: deviceConfigs[selectedDeviceForSettings?.id || '']?.segments || (selectedDeviceForSettings as any)?.segments || (selectedDeviceForSettings?.name?.toLowerCase().includes('soul') ? 1 : 2),
            stripType: deviceConfigs[selectedDeviceForSettings?.id || '']?.stripType || (selectedDeviceForSettings as any)?.stripType || 'WS2812B',
            sorting: deviceConfigs[selectedDeviceForSettings?.id || '']?.sorting || (selectedDeviceForSettings as any)?.sorting || 'GRB',
            grouped: !!deviceConfigs[selectedDeviceForSettings?.id || '']?.groupId || (selectedDeviceForSettings as any)?.grouped || false,
            groupId: deviceConfigs[selectedDeviceForSettings?.id || '']?.groupId || (selectedDeviceForSettings as any)?.groupId,
            groupName: customGroups.find(g => g.id === (deviceConfigs[selectedDeviceForSettings?.id || '']?.groupId || (selectedDeviceForSettings as any)?.groupId))?.name || 'My SK8Lytz',
            firmware: deviceConfigs[selectedDeviceForSettings?.id || '']?.firmware || (selectedDeviceForSettings as any)?.firmware || ((selectedDeviceForSettings as any)?.id?.startsWith('sim-') ? 'v2.0.1.DEMO' : 'Unknown')
          }}
          groups={customGroups}
        />
        <GroupSettingsModal
          isVisible={isGroupModalVisible}
          onClose={() => setIsGroupModalVisible(false)}
          onSave={saveGroup}
          onDelete={groupModalMode === 'rename' && editingGroupId ? () => deleteGroup(editingGroupId) : undefined}
          initialName={groupModalMode === 'rename' ? customGroups.find(g => g.id === editingGroupId)?.name : 'My SK8Lytz'}
          initialDeviceIds={groupModalMode === 'rename' ? customGroups.find(g => g.id === editingGroupId)?.deviceIds : selectedIds}
          allDevices={allDevices}
        />
      </View>
      
      {(!isActuallyConnected && (allDevices.length > 0 || customGroups.length > 0)) && (
          <TouchableOpacity
            onPress={() => setShowHintText(!showHintText)}
            style={{
              position: 'absolute',
              bottom: 24,
              right: 16,
              flexDirection: 'row',
              alignItems: 'center',
              justifyContent: 'center',
              opacity: 0.9,
              paddingVertical: 10,
              paddingHorizontal: showHintText ? 14 : 12,
              borderRadius: 30,
              backgroundColor: 'rgba(21, 25, 40, 0.85)',
              borderWidth: 1,
              borderColor: 'rgba(0, 240, 255, 0.2)',
              elevation: 4,
              shadowColor: '#000',
              shadowOffset: { width: 0, height: 2 },
              shadowOpacity: 0.3,
              shadowRadius: 4,
              zIndex: 100
            }}
            activeOpacity={0.7}
          >
            <MaterialCommunityIcons name="gesture-tap-hold" size={20} color={Colors.primary} style={{ marginRight: showHintText ? 8 : 0 }} />
            {showHintText && (
              <>
                <Text style={{ color: Colors.primary, fontSize: 13, fontWeight: 'bold', marginRight: 6 }}>
                  Long-press to auto-configure
                </Text>
                <MaterialCommunityIcons name="chevron-right" size={16} color={Colors.primary} style={{ opacity: 0.7 }} />
              </>
            )}
            {!showHintText && (
              <MaterialCommunityIcons name="chevron-left" size={16} color={Colors.primary} style={{ opacity: 0.7, marginLeft: 2 }} />
            )}
          </TouchableOpacity>
        )}

        <Modal
          visible={isSupportModalVisible}
          transparent={true}
          animationType="fade"
          onRequestClose={() => setIsSupportModalVisible(false)}
        >
          <View style={{ flex: 1, backgroundColor: 'rgba(0,0,0,0.8)', justifyContent: 'center', alignItems: 'center' }}>
            <View style={{ backgroundColor: Colors.surface, padding: 24, borderRadius: 16, width: '85%', borderWidth: 1, borderColor: Colors.surfaceHighlight }}>
              <View style={{ alignItems: 'center', marginBottom: 20 }}>
                <MaterialCommunityIcons name="lifebuoy" size={48} color={Colors.primary} />
                <Text style={{ ...Typography.title, color: Colors.primary, marginTop: 12 }}>Support Portal</Text>
                <Text style={{ color: Colors.textMuted, fontSize: 13, textAlign: 'center', marginTop: 8 }}>Need help configuring your hardware? Browse our official guides below.</Text>
              </View>
              <TouchableOpacity
                style={[styles.groupButton, { backgroundColor: 'rgba(0, 240, 255, 0.1)', borderColor: Colors.primary, borderWidth: 1, marginBottom: 16, paddingVertical: 12 }]}
                onPress={() => Linking.openURL('https://neogleamz.com/pages/getting-started')}
              >
                <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                  <MaterialCommunityIcons name="book-open-page-variant" size={20} color={Colors.primary} style={{ marginRight: 8 }} />
                  <Text style={[styles.groupButtonText, { color: Colors.primary, fontSize: 14 }]}>Installation Guides</Text>
                </View>
              </TouchableOpacity>

              <TouchableOpacity
                style={[styles.groupButton, { backgroundColor: 'rgba(255, 61, 0, 0.1)', borderColor: Colors.secondary, borderWidth: 1, marginBottom: 16, paddingVertical: 12 }]}
                onPress={() => Linking.openURL('https://neogleamz.com/pages/contact')}
              >
                <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                  <MaterialCommunityIcons name="email-fast" size={20} color={Colors.secondary} style={{ marginRight: 8 }} />
                  <Text style={[styles.groupButtonText, { color: Colors.secondary, fontSize: 14 }]}>Support Form</Text>
                </View>
              </TouchableOpacity>



              <TouchableOpacity
                style={{ paddingVertical: 12, alignItems: 'center' }}
                onPress={() => setIsSupportModalVisible(false)}
              >
                <Text style={{ color: Colors.textMuted, fontWeight: 'bold' }}>CLOSE</Text>
              </TouchableOpacity>
            </View>
          </View>
        </Modal>

      {/* Developer PIN Verification Modal */}
      <Modal visible={isPinPromptVisible} transparent animationType="fade">
        <View style={{ flex: 1, backgroundColor: 'rgba(0,0,0,0.85)', justifyContent: 'center', alignItems: 'center', padding: 20 }}>
          <View style={{ backgroundColor: Colors.surface, padding: 24, borderRadius: 20, width: '100%', maxWidth: 320, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}>
            <Text style={{ color: Colors.error, fontSize: 18, fontWeight: 'bold', marginBottom: 12, textAlign: 'center' }}>Developer Tools</Text>
            <Text style={{ color: Colors.textMuted, fontSize: 13, marginBottom: 20, textAlign: 'center' }}>Enter PIN verification to access underlying hardware logging subsystems.</Text>
            <TextInput
              style={{ backgroundColor: 'rgba(0,0,0,0.5)', color: '#FFF', padding: 12, borderRadius: 8, fontSize: 24, marginBottom: 24, borderWidth: 1, borderColor: 'rgba(255,100,100,0.3)', textAlign: 'center', letterSpacing: 8 }}
              placeholder="----"
              placeholderTextColor="rgba(255,255,255,0.1)"
              secureTextEntry
              keyboardType="number-pad"
              maxLength={4}
              value={pinInput}
              onChangeText={setPinInput}
              autoFocus
            />
            <View style={{ flexDirection: 'row', gap: 12 }}>
              <TouchableOpacity style={{ flex: 1, padding: 14, borderRadius: 10, backgroundColor: 'rgba(255,255,255,0.05)' }} onPress={() => setIsPinPromptVisible(false)}>
                <Text style={{ color: '#FFF', textAlign: 'center', fontWeight: 'bold' }}>Cancel</Text>
              </TouchableOpacity>
              <TouchableOpacity style={{ flex: 1, padding: 14, borderRadius: 10, backgroundColor: pinInput.length === 4 ? Colors.error : 'rgba(255,61,0,0.2)' }} disabled={pinInput.length !== 4} onPress={() => {
                 if (pinInput === '0000') {
                    setIsPinPromptVisible(false);
                    setLogsVisible(true);
                 } else {
                    alert("Invalid PIN. Access denied.");
                    setPinInput('');
                 }
              }}>
                <Text style={{ color: pinInput.length === 4 ? '#000' : 'rgba(255,255,255,0.3)', textAlign: 'center', fontWeight: 'bold' }}>Unlock</Text>
              </TouchableOpacity>
            </View>
          </View>
        </View>
      </Modal>

      <LogViewerModal 
        visible={logsVisible} 
        onClose={() => setLogsVisible(false)} 
        onOpenProgrammer={() => {
            setLogsVisible(false);
            setIsProgrammerVisible(true);
        }}
        onOpenSniffer={() => {
            setLogsVisible(false);
            setIsSnifferVisible(true);
        }}
        writeToDevice={writeToDevice}
        liveRxPayload={lastRawNotification}
        connectedDevices={connectedDevices as any[]}
        allDevices={allDevices}
        isScanning={isScanning}
        handleScan={handleScan}
        onClearAll={() => {
          setAllDevices([]);
          if (Platform.OS === 'web') {
            setMockConnected(false);
            setMockConnectedDevice(null);
          }
        }}
        onConnectToDevice={async (d: any) => { await connectToDevice(d); }}
        liveDeviceConfigs={deviceConfigs}
      />
      <Sk8LytzProgrammerModal 
        visible={isProgrammerVisible} 
        onClose={() => setIsProgrammerVisible(false)} 
        onExitToLogs={() => {
            setIsProgrammerVisible(false);
            setLogsVisible(true);
        }}
        allDevices={allDevices}
        deviceConfigs={deviceConfigs}
        connectToDevice={async (d: any) => { await connectToDevice(d); }}
        disconnectFromDevice={async (_id: string) => { disconnectFromDevice(); }}
        writeToDevice={writeToDevice}
        isScanning={isScanning}
        isScanProbing={isScanProbing}
        handleScan={scanForPeripherals}
      />
      <AdminHardwareTester 
        visible={isSnifferVisible}
        onClose={() => {
            setIsSnifferVisible(false);
            setLogsVisible(true);
        }}
        allDevices={allDevices}
        connectedDevices={connectedDevices as any[]}
        isScanning={isScanning}
        isScanProbing={isScanProbing}
        handleScan={scanForPeripherals}
        connectToDevice={async (d: any) => { await connectToDevice(d); }}
        handleDisconnect={disconnectFromDevice}
        writeToDevice={writeToDevice}
        liveRxPayload={lastRawNotification}
      />
      {/* First-Time Setup Wizard — auto-shows on first probe for new accounts */}
      <FirstTimeSetupModal
        visible={isSetupWizardVisible}
        pendingRegistrations={pendingRegistrations}
        onComplete={handleRegistrationComplete}
        onDismiss={() => {
          setIsSetupWizardVisible(false);
          clearPendingRegistrations();
        }}
      />

      {/* Crew Sync Modal */}
      <CrewModal
        visible={isCrewModalVisible}
        onClose={() => setIsCrewModalVisible(false)}
        activeSession={crewSession}
        activeRole={crewRole}
        onSessionReady={(session, role, lastScene) => {
          setCrewSession(session);
          setCrewRole(role);
          if (role === 'leader') {
            crewService.subscribeAsLeader(session.id, () => {});
          } else {
            crewService.subscribeAsMember(session.id, (scene) => {
              dockedControllerRef.current?.applyCloudScene(scene);
            });
            // Immediately apply last known scene for late-arrival sync
            if (lastScene) {
              setTimeout(() => dockedControllerRef.current?.applyCloudScene(lastScene), 300);
            }
          }
        }}
        onSessionLeft={() => {
          setCrewSession(null);
          setCrewRole(null);
          setScannerTab('DEVICES');
        }}
      />
    </SafeAreaView>
  );
}

const createStyles = (Colors: import('../theme/theme').ThemePalette) => StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: Colors.background,
  },
  container: {
    flex: 1,
    paddingTop: 8,
  },
  card: {
    backgroundColor: Colors.surface,
    borderRadius: Layout.borderRadius,
    padding: 16,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.1,
    shadowRadius: 12,
    elevation: 10,
  },
  scanButton: {
    marginTop: 24,
    backgroundColor: Colors.primary,
    paddingVertical: 14,
    borderRadius: Layout.borderRadius,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.6,
    shadowRadius: 8,
    elevation: 8,
  },
  scanButtonText: {
    color: Colors.isDark ? Colors.text : '#FFF',
    fontSize: 16,
    fontWeight: '800',
    letterSpacing: 1,
  },
  emptyStateContainer: {
    marginTop: 40,
    alignItems: 'center',
  },
  disconnectButtonSmall: {
    backgroundColor: Colors.surfaceHighlight,
    paddingVertical: 6,
    paddingHorizontal: 12,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: Colors.error + '44',
  },
  disconnectButtonTextSmall: {
    color: Colors.error,
    fontSize: 12,
    fontWeight: 'bold',
  },
  groupButton: {
    backgroundColor: Colors.secondary,
    paddingVertical: 14,
    borderRadius: Layout.borderRadius,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: Colors.secondary,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.6,
    shadowRadius: 8,
    elevation: 8,
  },
  groupButtonText: {
    color: Colors.isDark ? '#FFF' : Colors.background,
    fontSize: 16,
    fontWeight: '800',
    letterSpacing: 1,
  },
  countdownBadge: {
    position: 'absolute',
    right: -10,
    top: -10,
    width: 26,
    height: 26,
    borderRadius: 13,
    backgroundColor: '#FF7000',
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: '#FF7000',
    shadowRadius: 8,
    shadowOpacity: 1,
    zIndex: 50,
  },
  countdownText: {
    color: '#FFFFFF',
    fontSize: 14,
    fontWeight: '900',
  },
  errorContainer: {
    marginTop: 16,
    padding: 12,
    backgroundColor: 'rgba(255, 61, 0, 0.1)',
    borderRadius: 8,
    borderWidth: 1,
    borderColor: Colors.error
  }
});
