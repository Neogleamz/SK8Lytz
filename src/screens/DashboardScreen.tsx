import React, { useEffect, useState, useMemo, useCallback } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, SafeAreaView, FlatList, ActivityIndicator, Switch, Platform, Image, Linking } from 'react-native';
import { Typography, Layout } from '../theme/theme';
import { useTheme } from '../context/ThemeContext';
import DeviceItem from '../components/DeviceItem';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import useBLE from '../hooks/useBLE';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';

import Sk8lytzController from '../components/Sk8lytzController';
import DeviceSettingsModal from '../components/DeviceSettingsModal';
import GroupSettingsModal from '../components/GroupSettingsModal';
import AsyncStorage from '@react-native-async-storage/async-storage';

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
    isBluetoothSupported,
    isBluetoothEnabled,
    requestPermissions
  } = useBLE();

  const [selectedIds, setSelectedIds] = useState<string[]>([]);
  const [isSelectionMode, setIsSelectionMode] = useState(false);
  const [powerStates, setPowerStates] = useState<Record<string, boolean>>({});

  // TEMP MOCK for browser verification
  const IS_BROWSER_DEMO = true;
  const [mockConnected, setMockConnected] = useState(false);
  const [mockConnectedDevice, setMockConnectedDevice] = useState<string | null>(null);
  const [mockConnectedGroup, setMockConnectedGroup] = useState<string | null>(null);
  const [updateTrigger, setUpdateTrigger] = useState(0);

  const [customGroups, setCustomGroups] = useState<any[]>([]);
  const [isGroupModalVisible, setIsGroupModalVisible] = useState(false);
  const [groupModalMode, setGroupModalMode] = useState<'create' | 'rename'>('create');
  const [editingGroupId, setEditingGroupId] = useState<string | null>(null);
  const [isDeviceListCollapsed, setIsDeviceListCollapsed] = useState(false);
  const lastProcessedRef = React.useRef<string>('');
  const allDevicesRef = React.useRef(allDevices);
  const customGroupsRef = React.useRef(customGroups);
  const isProvisioningTriggered = React.useRef(false);

  // Refs are now updated manually in setters to avoid reactive loops

  const [demoHaloQueued, setDemoHaloQueued] = useState(false);
  const [demoSoulQueued, setDemoSoulQueued] = useState(false);

  const handleScan = () => {
    requestPermissions().then((granted) => {
      if (granted) {
        console.log('[SK8Lytz] Manual Scan Initiated');
        isProvisioningTriggered.current = true;
        AsyncStorage.removeItem('ng_processed_devices');
        lastProcessedRef.current = '';
        allDevicesRef.current = []; // Clear ref immediately
        
        scanForPeripherals();
        
        setTimeout(() => {
          setAllDevices((prev: any[]) => {
            let newDevices = [...prev];
            const haloIds = ['sim-halo-1', 'sim-halo-2'];
            const soulIds = ['sim-soul-1', 'sim-soul-2'];
            
            if (demoHaloQueued) {
              haloIds.forEach((id, idx) => {
                if (!newDevices.some(d => d.id === id)) {
                  newDevices.push({ id, name: `HALOZ ${idx === 0 ? 'Left' : 'Right'} Skate`, points: 16, rssi: -45 - Math.floor(Math.random() * 20) } as any);
                }
              });
            }
            if (demoSoulQueued) {
              soulIds.forEach((id, idx) => {
                if (!newDevices.some(d => d.id === id)) {
                  newDevices.push({ id, name: `SOULZ ${idx === 0 ? 'Left' : 'Right'} Skate`, points: 43, rssi: -42 - Math.floor(Math.random() * 20) } as any);
                }
              });
            }
            allDevicesRef.current = newDevices as any;
            return newDevices;
          });
        }, 150);

        const waitTime = Platform.OS === 'web' ? 1800 : 7000;
        setTimeout(() => {
          runAutoProvisioning();
        }, waitTime);
      }
    });
  };


  useEffect(() => {
    AsyncStorage.getItem('ng_custom_groups')
      .then(res => {
        if (res) {
          try { 
            setCustomGroups(JSON.parse(res)); 
          } catch(e) { console.warn('JSON parse error custom groups', e); }
        }
      })
      .catch(e => console.warn('AsyncStorage error custom groups', e));

    AsyncStorage.getItem('ng_device_configs')
      .then(res => {
        if (res) {
          try {
            const configs = JSON.parse(res);
          } catch(e) { console.warn('JSON parse error configs', e); }
        }
      })
      .catch(e => console.warn('AsyncStorage error configs', e));
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

    checkAndGroup(soulzDevices, 'SOULZ Roller Skate Lights', 'SOULZ', 43);
    checkAndGroup(halozDevices, 'HALOZ Roller Skate Lights', 'HALOZ', 16);

    const storagePromises = [];
    if (didUpdateProcessed) storagePromises.push(AsyncStorage.setItem('ng_processed_devices', JSON.stringify(processed)));
    if (didUpdateGroups) storagePromises.push(AsyncStorage.setItem('ng_custom_groups', JSON.stringify(updatedGroups)));
    if (didUpdateConfigs) storagePromises.push(AsyncStorage.setItem('ng_device_configs', JSON.stringify(configs)));
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

  const handleDisconnect = () => {
    if (IS_BROWSER_DEMO) {
      setMockConnected(false);
      setMockConnectedDevice(null);
      setMockConnectedGroup(null);
    }
    disconnectFromDevice();
  };

  const handleGlobalPowerToggle = (deviceIds: string[], forceState?: boolean) => {
    // If forceState is provided, use it, else default to toggling based on first device's state
    const targetState = forceState !== undefined ? forceState : !(powerStates[deviceIds[0]] ?? true);
    
    // Update local state optimistic
    const newStates = { ...powerStates };
    deviceIds.forEach(id => { newStates[id] = targetState; });
    setPowerStates(newStates);

    // Broadcast BLE command
    if (targetState) {
        writeToDevice(ZenggeProtocol.turnOn());
    } else {
        writeToDevice(ZenggeProtocol.turnOff());
    }
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

  const [isSettingsVisible, setIsSettingsVisible] = useState(false);
  const [selectedDeviceForSettings, setSelectedDeviceForSettings] = useState<any>(null);


  const openSettings = (device: any) => {
    setSelectedDeviceForSettings(device);
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
          const newGroups = customGroups.map(g => ({...g, deviceIds: g.deviceIds.filter(id => id !== selectedDeviceForSettings.id)}));
          setCustomGroups(newGroups);
          AsyncStorage.setItem('ng_custom_groups', JSON.stringify(newGroups)).catch(() => {});
      }

      setAllDevices((prev: any[]) => {
        const next = prev.map(d => 
          d.id === selectedDeviceForSettings.id 
            ? { ...d, name: settings.name, type: settings.type, points: settings.points, sorting: settings.sorting, stripType: settings.stripType, groupId: finalGroupId } 
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
      } catch (e) { console.error('Failed to persist settings', e); }
    }
    setIsSettingsVisible(false);
  };

  const MemoizedSk8lytzController = useMemo(() => {
    if (!isActuallyConnected) return null;
    return (
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
      />
    );
  }, [isActuallyConnected, isGrouped, displayConnectedDevices, writeToDevice, powerStates]);

  const renderItem = useCallback(({ item }: { item: any }) => (
    <View style={{ paddingHorizontal: Layout.padding }}>
      <DeviceItem
        device={item}
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
            return;
          }
          await connectToDevice(item);
          
          const configPoints = (item as any).points || (item.name?.toLowerCase().includes('soul') ? 43 : 16);
          const configSorting = (item as any).sorting || 'GRB';
          const configStripType = (item as any).stripType || 'WS2812B';
          writeToDevice(ZenggeProtocol.setHardwareConfig(configPoints, configSorting, configStripType));

          if (IS_BROWSER_DEMO) {
            setMockConnected(true);
            setMockConnectedDevice(item.id);
          }
        }}
        onLongPress={() => {
          openSettings(item);
        }}
        showGroupIcon={false}
        isPoweredOn={powerStates[item.id] ?? true}
        onPowerToggle={() => handleGlobalPowerToggle([item.id])}
      />
    </View>
  ), [displayConnectedDevices, isSelectionMode, selectedIds]);

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

  return (
    <SafeAreaView style={styles.safeArea}>
      {BluetoothWarningBanner}
      <View style={styles.container}>

        <FlatList
          style={{ flex: 1 }}
          ListHeaderComponent={
            <View style={{ paddingBottom: 16 }}>
              {/* COMBINED HEADER & STATUS */}
              <View style={{ 
                paddingHorizontal: Layout.padding, 
                paddingTop: isActuallyConnected ? 16 : 40, 
                paddingBottom: isActuallyConnected ? 20 : 30,
                position: 'relative',
              }}>
                {/* Theme toggle: landing page only, absolutely pinned top-left */}
                {!isActuallyConnected && (
                  <TouchableOpacity 
                    onPress={toggleTheme} 
                    style={{ position: 'absolute', left: 0, top: 40, zIndex: 10, padding: 10 }}
                  >
                    <MaterialCommunityIcons 
                      name={isDark ? 'white-balance-sunny' : 'moon-waning-crescent'} 
                      size={22} 
                      color={Colors.primary} 
                    />
                  </TouchableOpacity>
                )}

                {/* Logo row — logo always centered, plus connected-state chips on right */}
                <View style={{ 
                  flexDirection: 'row', 
                  alignItems: 'center', 
                  justifyContent: isActuallyConnected ? 'flex-start' : 'center',
                  width: '100%'
                }}>
                  <Image 
                    source={require('../../assets/logo.png')} 
                    style={{ 
                      width: isActuallyConnected ? 100 : 180, 
                      height: isActuallyConnected ? 30 : 54 
                    }} 
                    resizeMode="contain"
                    tintColor={Colors.text}
                  />

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
                            <View style={{ 
                              width: 6, 
                              height: 6, 
                              borderRadius: 3, 
                              backgroundColor: statusColor, 
                              marginRight: 6 
                            }} />
                            <Text style={[Typography.caption, { 
                              color: statusColor, 
                              fontSize: 10, 
                              fontWeight: 'bold' 
                            }]}>
                              PAIRED ({connectedCount})
                            </Text>
                          </View>
                        );
                      })()}
                    </View>
                  )}

                  <View style={{ flex: 1 }} />

                  {isActuallyConnected && (
                    <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                      <TouchableOpacity
                        style={[styles.disconnectButtonSmall, { paddingVertical: 4, paddingHorizontal: 8, marginRight: 8 }]}
                        onPress={handleDisconnect}
                        activeOpacity={0.8}
                      >
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
                  <View style={{ height: 2, width: 30, backgroundColor: Colors.secondary, marginTop: 6, borderRadius: 1, alignSelf: 'center' }} />
                )}
              </View>

              <View style={{ paddingHorizontal: Layout.padding }}>
              {!isActuallyConnected ? (
                <View style={[styles.card, { padding: 16 }]}>
                  <View>
                    <TouchableOpacity
                      style={[styles.scanButton, { marginTop: 0 }, isScanning && { opacity: 0.7 }]}
                      onPress={handleScan}
                      activeOpacity={0.8}
                      disabled={isScanning}
                    >
                      {isScanning ? (
                        <ActivityIndicator color={Colors.text} />
                      ) : (
                        <Text style={styles.scanButtonText}>SCAN FOR SK8LYTZ</Text>
                      )}
                    </TouchableOpacity>
                    
                    <View style={{ flexDirection: 'column', alignItems: 'center', marginTop: 16, gap: 12 }}>
                      <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center' }}>
                        <Text style={{ color: Colors.text, marginRight: 12, fontWeight: 'bold' }}>Demo HALOZ</Text>
                        <Switch
                          value={demoHaloQueued}
                          onValueChange={setDemoHaloQueued}
                          trackColor={{ false: 'rgba(255,255,255,0.1)', true: Colors.primary }}
                          thumbColor={demoHaloQueued ? '#000' : '#888'}
                        />
                      </View>
                      
                      <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center' }}>
                        <Text style={{ color: Colors.text, marginRight: 12, fontWeight: 'bold' }}>Demo SOULZ</Text>
                        <Switch
                          value={demoSoulQueued}
                          onValueChange={setDemoSoulQueued}
                          trackColor={{ false: 'rgba(255,255,255,0.1)', true: Colors.secondary }}
                          thumbColor={demoSoulQueued ? '#000' : '#888'}
                        />
                      </View>
                    </View>
                  </View>
                </View>
              ) : null}
              </View>

              {MemoizedSk8lytzController}

              <View style={{ paddingHorizontal: Layout.padding }}>


              {!isActuallyConnected && customGroups.length > 0 && (
                <View style={{ marginTop: 20 }}>
                  <Text style={[Typography.title, { marginBottom: 12, paddingHorizontal: 4, color: Colors.primary }]}>Groups</Text>
                  {customGroups.map((group) => (
                    <DeviceItem
                      key={group.id}
                      device={group}
                      isConnected={mockConnectedGroup === group.id}
                      isSelectionMode={false}
                      isSelected={false}
                      onPress={async () => {
                        setMockConnected(true);
                        setMockConnectedGroup(group.id);
                        
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
                  ))}
                </View>
              )}

              {!isActuallyConnected && (
                <>
                  <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginTop: 24, marginBottom: 12, paddingHorizontal: 4 }}>
                    <Text style={[Typography.title, { color: Colors.primary }]}>Available Devices</Text>
                    {allDevices.length > 0 && (
                      <TouchableOpacity onPress={() => setIsDeviceListCollapsed(!isDeviceListCollapsed)}>
                        <Text style={{ color: Colors.primary, fontWeight: 'bold', fontSize: 12 }}>
                          {isDeviceListCollapsed ? 'SHOW ALL' : 'HIDE'}
                        </Text>
                      </TouchableOpacity>
                    )}
                  </View>
                </>
              )}
              </View>
            </View>
          }
          data={(!isActuallyConnected && !isDeviceListCollapsed) ? allDevices : []}
          keyExtractor={(item) => item.id}
          renderItem={renderItem}
          contentContainerStyle={{ paddingBottom: 100 }}

          ListEmptyComponent={
            (!isActuallyConnected && !isDeviceListCollapsed) ? (
              <View style={styles.emptyStateContainer}>
                <Text style={Typography.caption}>
                  {isScanning ? 'Scanning...' : 'No devices found.'}
                </Text>
              </View>
            ) : null
          }
        />
        <DeviceSettingsModal
          isVisible={isSettingsVisible}
          onClose={() => setIsSettingsVisible(false)}
          onSave={saveSettings}
          writeToDevice={writeToDevice}
          initialSettings={{
            name: selectedDeviceForSettings?.name || 'SOULZ',
            type: (selectedDeviceForSettings?.name?.toLowerCase().includes('soul') ? 'SOULZ' : 'HALOZ'),
            points: selectedDeviceForSettings?.points || (selectedDeviceForSettings?.name?.toLowerCase().includes('soul') ? 43 : 24),
            segments: 1,
            stripType: selectedDeviceForSettings?.stripType || 'GRB (WS2812B)',
            sorting: selectedDeviceForSettings?.sorting || 'GRB',
            grouped: selectedDeviceForSettings?.grouped || false,
            groupId: selectedDeviceForSettings?.groupId,
            groupName: customGroups.find(g => g.id === selectedDeviceForSettings?.groupId)?.name || 'My Roller Skates'
          }}
          groups={customGroups}
        />
        <GroupSettingsModal
          isVisible={isGroupModalVisible}
          onClose={() => setIsGroupModalVisible(false)}
          onSave={saveGroup}
          onDelete={groupModalMode === 'rename' && editingGroupId ? () => deleteGroup(editingGroupId) : undefined}
          initialName={groupModalMode === 'rename' ? customGroups.find(g => g.id === editingGroupId)?.name : 'My Roller Skates'}
          initialDeviceIds={groupModalMode === 'rename' ? customGroups.find(g => g.id === editingGroupId)?.deviceIds : selectedIds}
          allDevices={allDevices}
        />
      </View>
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
    padding: 24,
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
    color: Colors.background,
    fontSize: 16,
    fontWeight: '800',
    letterSpacing: 1,
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
