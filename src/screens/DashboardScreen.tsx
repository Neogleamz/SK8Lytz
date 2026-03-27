import React, { useEffect, useState, useMemo, useCallback, useRef } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, SafeAreaView, FlatList, ActivityIndicator, Switch, Platform, Image, Linking, Animated, StatusBar, Dimensions, Modal, TextInput, BackHandler, PanResponder } from 'react-native';
import { Typography, Layout } from '../theme/theme';
import { useTheme } from '../context/ThemeContext';
import DeviceItem from '../components/DeviceItem';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import useBLE from '../hooks/useBLE';
import { ZenggeProtocol, ZENGGE_SERVICE_UUID } from '../protocols/ZenggeProtocol';

import Sk8lytzController from '../components/Sk8lytzController';
import DockedController from '../components/DockedController';
import DeviceSettingsModal from '../components/DeviceSettingsModal';
import GroupSettingsModal from '../components/GroupSettingsModal';
import Sk8LytzProgrammerModal from '../components/Sk8LytzProgrammerModal';
import AsyncStorage from '@react-native-async-storage/async-storage';
import ScannerAnimation from '../components/ScannerAnimation';
import { AppLogger } from '../services/AppLogger';
import LogViewerModal from '../components/LogViewerModal';
import ProtocolSnifferModal from '../components/ProtocolSnifferModal';

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

export default function DashboardScreen() {
  const { Colors, isDark, toggleTheme, controlUITheme, toggleControlUITheme } = useTheme();
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
    isBluetoothSupported,
    isBluetoothEnabled,
    requestPermissions,
    setOnDataReceived
  } = useBLE();

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
  const [showHintText, setShowHintText] = useState(true);
  const [isSupportModalVisible, setIsSupportModalVisible] = useState(false);
  const [isProgrammerVisible, setIsProgrammerVisible] = useState(false);
  const [isSnifferVisible, setIsSnifferVisible] = useState(false);
  const lastProcessedRef = React.useRef<string>('');
  const allDevicesRef = React.useRef(allDevices);
  const customGroupsRef = React.useRef(customGroups);
  const isProvisioningTriggered = React.useRef(false);

  // Refs are now updated manually  const [isProvisioning, setIsProvisioning] = useState(false);
  const [demoHaloQueued, setDemoHaloQueued] = useState(false);
  const [demoSoulQueued, setDemoSoulQueued] = useState(false);

  // Bind BLE Notification Hardware Sync Hook
  useEffect(() => {
    setOnDataReceived((deviceId: string, payload: number[]) => {
      setLastRawNotification({ deviceId, payloadHex: payload.map(b => b.toString(16).padStart(2,'0').toUpperCase()).join(' ') });
      const config = ZenggeProtocol.parseHardwareConfig(payload);
      if (config) {
        console.log('[Dashboard] Intercepted Hardware Sync:', config);
        setAllDevices(prev => prev.map(d => {
          if (d.id === deviceId) {
            const newD = { ...d, points: config.points, sorting: config.sorting, stripType: config.stripType, segments: config.segments } as any as typeof d;
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
            const haloIds = ['sim-halo-1', 'sim-halo-2'];
            const soulIds = ['sim-soul-1', 'sim-soul-2'];
            
            if (demoHaloQueued) {
              haloIds.forEach((id, idx) => {
                if (!newDevices.some(d => d.id === id)) {
                  const device = { 
                    id, 
                    name: `HALOZ ${idx === 0 ? 'Left' : 'Right'} Skate`, 
                    type: 'HALOZ',
                    points: 16, 
                    segments: 1,
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
    if (!mockConnected) return connectedDevices;

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
  }, [mockConnected, mockConnectedDevice, mockConnectedGroup, allDevices, connectedDevices, updateTrigger, customGroups]);

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

  useEffect(() => {
    if (setOnDataReceived) {
      setOnDataReceived((deviceId, data) => {
        const parsed = ZenggeProtocol.parseHardwareConfig(data);
        if (parsed) {
          console.log(`[HW Sync] Decoded live configuration from ${deviceId}:`, parsed);
          setAllDevices((prev: any[]) => {
            let morphed = false;
            const next = prev.map(d => {
              if (d.id === deviceId) {
                const anyD = d as any;
                if (anyD.points !== parsed.points || anyD.segments !== parsed.segments || anyD.stripType !== parsed.stripType || anyD.sorting !== parsed.sorting) {
                  morphed = true;
                  return { ...d, points: parsed.points, segments: parsed.segments, stripType: parsed.stripType, sorting: parsed.sorting };
                }
              }
              return d;
            });
            if (morphed) allDevicesRef.current = next as any;
            return morphed ? next : prev;
          });
          
          AsyncStorage.getItem('ng_device_configs').then(stored => {
             const configs = stored ? JSON.parse(stored) : {};
             if (!configs[deviceId]) configs[deviceId] = {};
             configs[deviceId].points = parsed.points;
             configs[deviceId].segments = parsed.segments;
             configs[deviceId].stripType = parsed.stripType;
             configs[deviceId].sorting = parsed.sorting;
             AsyncStorage.setItem('ng_device_configs', JSON.stringify(configs)).catch(()=>{});
          });
        }
      });
    }
  }, [setOnDataReceived, setAllDevices]);

  const [isSettingsVisible, setIsSettingsVisible] = useState(false);
  const [selectedDeviceForSettingsId, setSelectedDeviceForSettingsId] = useState<string | null>(null);

  const selectedDeviceForSettings = useMemo(() => {
    if (!selectedDeviceForSettingsId) return null;
    return allDevices.find(d => d.id === selectedDeviceForSettingsId) || null;
  }, [selectedDeviceForSettingsId, allDevices]);

  const openSettings = (device: any) => {
    setSelectedDeviceForSettingsId(device.id);
    
    // Explicitly query the hardware config upon modal invoke if connected
    if (connectedDevices.some(d => d.id === device.id)) {
       writeToDevice(ZenggeProtocol.queryHardwareConfig(), device.id);
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
           id: selectedDeviceForSettings.id,
           name: settings.name,
           type: settings.type,
           points: settings.points,
           segments: settings.segments,
           sorting: settings.sorting,
           stripType: settings.stripType,
         });
      } catch (e) { console.error('Failed to persist settings', e); }
    }
    setIsSettingsVisible(false);
  };

  const MemoizedSk8lytzController = useMemo(() => {
    if (!isActuallyConnected) return null;

    if (isTestModeActive) {
      return null;
    }

    return (
      <Animated.View {...edgePanResponder.panHandlers} style={{ flex: 1, backgroundColor: 'transparent' }}>
          {controlUITheme === 'DOCKED' ? (
              <DockedController
                lockedProduct={
                  (displayConnectedDevices[0] as any)?.type || 
                  ((displayConnectedDevices[0] as any)?.points 
                    ? ((displayConnectedDevices[0] as any).points < 20 ? 'HALOZ' : 'SOULZ') 
                    : ((displayConnectedDevices[0] as any)?.name?.toLowerCase().includes('soul') ? 'SOULZ' : 'HALOZ'))
                }
                isPaired={isGrouped}
                points={(displayConnectedDevices[0] as any).points}
                devices={displayConnectedDevices}
                onLongPressDevice={openSettings}
                writeToDevice={writeToDevice}
                isPoweredOn={displayConnectedDevices.some(d => powerStates[d.id] ?? true)}
                onDisconnect={handleDisconnect}
              />
          ) : (
              <Sk8lytzController
                lockedProduct={
                  (displayConnectedDevices[0] as any)?.type || 
                  ((displayConnectedDevices[0] as any)?.points 
                    ? ((displayConnectedDevices[0] as any).points < 20 ? 'HALOZ' : 'SOULZ') 
                    : ((displayConnectedDevices[0] as any)?.name?.toLowerCase().includes('soul') ? 'SOULZ' : 'HALOZ'))
                }
                isPaired={isGrouped}
                points={(displayConnectedDevices[0] as any).points}
                devices={displayConnectedDevices}
                onLongPressDevice={openSettings}
                writeToDevice={writeToDevice}
                isPoweredOn={displayConnectedDevices.some(d => powerStates[d.id] ?? true)}
                onDisconnect={handleDisconnect}
              />
          )}
      </Animated.View>
    );
  }, [isActuallyConnected, isGrouped, displayConnectedDevices, writeToDevice, powerStates, isTestModeActive, controlUITheme]);

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
          
          const configPoints = (mergedItem as any).points || (mergedItem.name?.toLowerCase().includes('soul') ? 43 : 8);
          const configSorting = (mergedItem as any).sorting || 'GRB';
          const configStripType = (mergedItem as any).stripType || 'WS2812B';
          const configSegments = (mergedItem as any).segments || (mergedItem.name?.toLowerCase().includes('soul') ? 1 : 2);
          writeToDevice(ZenggeProtocol.setHardwareConfig(configPoints, configSorting, configStripType, configSegments));

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
        paddingTop: (Platform.OS === 'android' ? (StatusBar.currentHeight || 20) : 0) + (isActuallyConnected ? 12 : 20), 
        paddingBottom: isActuallyConnected ? 2 : 8,
        position: 'relative',
      }}>
        {!isActuallyConnected && (
          <View style={{ position: 'absolute', right: 0, top: (Platform.OS === 'android' ? (StatusBar.currentHeight || 20) : 0) + 20, zIndex: 10, flexDirection: 'row' }}>
            <TouchableOpacity onPress={toggleControlUITheme} style={{ padding: 10, flexDirection: 'row', alignItems: 'center' }}>
              <Text style={{ color: Colors.primary, marginRight: 6, fontSize: 10, fontWeight: 'bold' }}>{controlUITheme}</Text>
              <MaterialCommunityIcons name={controlUITheme === 'DOCKED' ? 'dock-bottom' : (controlUITheme === 'MODERN' ? 'view-dashboard-variant-outline' : 'view-list-outline')} size={22} color={Colors.primary} />
            </TouchableOpacity>
            <TouchableOpacity onPress={toggleTheme} style={{ padding: 10 }}>
              <MaterialCommunityIcons name={isDark ? 'white-balance-sunny' : 'moon-waning-crescent'} size={22} color={Colors.primary} />
            </TouchableOpacity>
          </View>
        )}

        <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: isActuallyConnected ? 'flex-start' : 'center', width: '100%' }}>
          <TouchableOpacity activeOpacity={1} onPress={handleLogoPress} style={{ position: 'relative' }}>
            <Image source={require('../../assets/logo.png')} style={{ width: isActuallyConnected ? 70 : 130, height: isActuallyConnected ? 20 : 38 }} resizeMode="contain" tintColor={Colors.text} />
            {countdown !== null && (
              <Animated.View style={[styles.countdownBadge, { transform: [{ scale: pulseAnimConfig }] }]}>
                <Text style={styles.countdownText}>{countdown}</Text>
              </Animated.View>
            )}
          </TouchableOpacity>

          {isActuallyConnected && (
            <View style={{ marginLeft: 16 }}>
              {(() => {
                const connectedCount = displayConnectedDevices.length;
                let expectedCount = 1;
                const firstDevice = displayConnectedDevices[0] as any;
                if (firstDevice?.grouped && firstDevice?.groupId) {
                  const group = customGroups.find(g => g.id === firstDevice.groupId);
                  if (group) expectedCount = group.deviceIds.length;
                }
                
                let statusColor = Colors.success;
                if (connectedCount === 0) statusColor = Colors.error;
                else if (connectedCount < expectedCount) statusColor = '#FFA500';
                else statusColor = Colors.success;

                return (
                  <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                    <View style={{ width: 6, height: 6, borderRadius: 3, backgroundColor: statusColor, marginRight: 6 }} />
                    <Text style={[Typography.caption, { color: statusColor, fontSize: 10, fontWeight: 'bold' }]}>DISCOVERED ({connectedCount})</Text>
                  </View>
                );
              })()}
            </View>
          )}

          {isActuallyConnected && <View style={{ flex: 1 }} />}

          {!isActuallyConnected && (
            <TouchableOpacity style={{ position: 'absolute', left: 0, top: 0, paddingHorizontal: 12, paddingVertical: 6, borderRadius: 16, borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', zIndex: 100 }} onPress={() => setIsSupportModalVisible(true)}>
               <MaterialCommunityIcons name="help-circle-outline" size={20} color={Colors.text} />
            </TouchableOpacity>
          )}

          {isActuallyConnected && !isTestModeActive && (
            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
              <TouchableOpacity style={[styles.disconnectButtonSmall, { paddingVertical: 4, paddingHorizontal: 8, marginRight: 8 }]} onPress={handleDisconnect} activeOpacity={0.8}>
                <Text style={[styles.disconnectButtonTextSmall, { fontSize: 10 }]}>DISCONNECT</Text>
              </TouchableOpacity>

              {(() => {
                const allIds = displayConnectedDevices.map(d => d.id);
                const isGlobalPoweredOn = allIds.every(id => powerStates[id] ?? true);
                return (
                  <TouchableOpacity 
                    style={{ width: 32, height: 32, borderRadius: 16, backgroundColor: isGlobalPoweredOn ? 'rgba(0, 240, 255, 0.15)' : 'rgba(255, 255, 255, 0.1)', justifyContent: 'center', alignItems: 'center', borderWidth: 1, borderColor: isGlobalPoweredOn ? 'rgba(0, 240, 255, 0.3)' : 'rgba(255,255,255,0.2)' }}
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
        {!isActuallyConnected && (
          <View style={{ height: 2, width: 30, backgroundColor: Colors.secondary, marginTop: 4, borderRadius: 1, alignSelf: 'center' }} />
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
                         onPress={handleScan}
                      />
                  </View>
                ) : null}
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
                            const configPoints = (firstDev as any).points || (firstDev.name?.toLowerCase().includes('soul') ? 43 : 16);
                            const configSorting = (firstDev as any).sorting || 'GRB';
                            const configStripType = (firstDev as any).stripType || 'WS2812B';
                            writeToDevice(ZenggeProtocol.setHardwareConfig(configPoints, configSorting, configStripType));
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
            data={!isDeviceListCollapsed ? allDevices : []}
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
                style={[styles.groupButton, { backgroundColor: 'rgba(255, 61, 0, 0.1)', borderColor: Colors.secondary, borderWidth: 1, marginBottom: 24, paddingVertical: 12 }]}
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
      />
      <Sk8LytzProgrammerModal 
        visible={isProgrammerVisible} 
        onClose={() => setIsProgrammerVisible(false)} 
        onExitToLogs={() => {
            setIsProgrammerVisible(false);
            setLogsVisible(true);
        }}
        allDevices={allDevices}
        writeToDevice={writeToDevice}
        isScanning={isScanning}
        handleScan={scanForPeripherals}
      />
      <ProtocolSnifferModal 
        visible={isSnifferVisible}
        onClose={() => {
            setIsSnifferVisible(false);
            setLogsVisible(true);
        }}
        allDevices={allDevices}
        connectedDevices={connectedDevices as any[]}
        isScanning={isScanning}
        handleScan={scanForPeripherals}
        connectToDevice={async (d) => { await connectToDevice(d); }}
        handleDisconnect={disconnectFromDevice}
        writeToDevice={writeToDevice}
        liveRxPayload={lastRawNotification}
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
