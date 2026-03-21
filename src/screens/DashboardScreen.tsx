import React, { useEffect, useState, useMemo } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, SafeAreaView, FlatList, ActivityIndicator, Switch } from 'react-native';
import { Colors, Typography, Layout } from '../theme/theme';
import Header from '../components/Header';
import DeviceItem from '../components/DeviceItem';
import useBLE from '../hooks/useBLE';

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
    requestPermissions
  } = useBLE();

  const [selectedIds, setSelectedIds] = useState<string[]>([]);
  const [isSelectionMode, setIsSelectionMode] = useState(false);

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

  useEffect(() => {
    const soulzDevices = allDevices.filter(d => {
      const p = (d as any).points || (d.name?.toLowerCase().includes('soul') ? 43 : 16);
      return p >= 20;
    });

    if (soulzDevices.length >= 2) {
      AsyncStorage.getItem('ng_auto_grouped').then(hasGrouped => {
        if (!hasGrouped) {
          const leftId = soulzDevices[0].id;
          const rightId = soulzDevices[1].id;

          // Auto-Group "my roller skates"
          const newGroupId = `group-${Date.now()}`;
          const newGroup = {
            id: newGroupId,
            name: 'my roller skates',
            deviceIds: [leftId, rightId],
            type: 'SOULZ'
          };
          
          const updatedGroups = [...customGroups, newGroup];
          setCustomGroups(updatedGroups);
          AsyncStorage.setItem('ng_custom_groups', JSON.stringify(updatedGroups));

          // Set Configs to Left / Right
          AsyncStorage.getItem('ng_device_configs').then(res => {
            let configs: any = {};
            if (res) {
              try { configs = JSON.parse(res); } catch(e) {}
            }
            
            configs[leftId] = { ...configs[leftId], name: 'Left', type: 'SOULZ', points: 43 };
            configs[rightId] = { ...configs[rightId], name: 'Right', type: 'SOULZ', points: 43 };
            
            AsyncStorage.setItem('ng_device_configs', JSON.stringify(configs));
            AsyncStorage.setItem('ng_auto_grouped', 'true');
            
            setAllDevices(allDevices.map(d => {
              if (d.id === leftId) return { ...d, name: 'Left', points: 43 } as any;
              if (d.id === rightId) return { ...d, name: 'Right', points: 43 } as any;
              return d;
            }));
          });
        }
      });
    }
  }, [allDevices, customGroups, setAllDevices]);

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
    setCustomGroups(prev => prev.filter(g => g.id !== id));
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

  const handleScan = () => {
    requestPermissions().then((granted) => {
      if (granted) {
        scanForPeripherals();
      }
    });
  };

  const openSettings = (device: any) => {
    setSelectedDeviceForSettings(device);
    setIsSettingsVisible(true);
  };

  const saveSettings = async (settings: DeviceSettings) => {
    if (selectedDeviceForSettings) {
      selectedDeviceForSettings.name = settings.name;
      selectedDeviceForSettings.type = settings.type;
      selectedDeviceForSettings.points = settings.points;
      selectedDeviceForSettings.grouped = settings.grouped;
      
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

      setAllDevices(prev => prev.map(d => 
        d.id === selectedDeviceForSettings.id 
          ? { ...d, name: settings.name, type: settings.type, points: settings.points, groupId: finalGroupId } 
          : d
      ));
      
      selectedDeviceForSettings.groupId = finalGroupId;
      selectedDeviceForSettings.name = settings.name;
      selectedDeviceForSettings.type = settings.type;
      selectedDeviceForSettings.points = settings.points;
      
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

  return (
    <SafeAreaView style={styles.safeArea}>
      <Header />
      <View style={styles.container}>

        <FlatList
          style={{ flex: 1 }}
          ListHeaderComponent={
            <View style={{ paddingBottom: 16 }}>
              <View style={{ paddingHorizontal: Layout.padding }}>
              <View style={[styles.card, isActuallyConnected ? { paddingVertical: 12, paddingHorizontal: 16 } : { padding: 16 }]}>
                {isActuallyConnected ? (
                  <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}>
                    <View style={{ flex: 1 }}>
                      {isGrouped ? (
                        <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: 2 }}>
                          <View style={{ width: 8, height: 8, borderRadius: 4, backgroundColor: Colors.secondary, marginRight: 6 }} />
                          <Text style={[Typography.caption, { color: Colors.secondary, fontWeight: 'bold' }]}>
                            SYNCED PAIR ({displayConnectedDevices.length} DEVICES)
                          </Text>
                        </View>
                      ) : (
                        <Text style={Typography.body}>
                          Connected: <Text style={{ color: Colors.primary, fontWeight: 'bold' }}>
                            {displayConnectedDevices[0]?.name}
                          </Text>
                        </Text>
                      )}
                    </View>
                    <TouchableOpacity
                      style={styles.disconnectButtonSmall}
                      onPress={handleDisconnect}
                      activeOpacity={0.8}
                    >
                      <Text style={styles.disconnectButtonTextSmall}>DISCONNECT ALL</Text>
                    </TouchableOpacity>
                  </View>
                ) : (
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
                          value={allDevices.some(d => d.id.startsWith('sim-halo'))}
                          onValueChange={(val) => {
                            if (!val) {
                              setAllDevices(allDevices.filter(d => !d.id.startsWith('sim-halo')));
                            } else {
                              setAllDevices([...allDevices, 
                                { id: 'sim-halo-1', name: 'HALOZ', points: 16 } as any,
                                { id: 'sim-halo-2', name: 'HALOZ', points: 16 } as any,
                              ]);
                            }
                          }}
                          trackColor={{ false: 'rgba(255,255,255,0.1)', true: Colors.primary }}
                          thumbColor={allDevices.some(d => d.id.startsWith('sim-halo')) ? '#000' : '#888'}
                        />
                      </View>
                      
                      <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center' }}>
                        <Text style={{ color: Colors.text, marginRight: 12, fontWeight: 'bold' }}>Demo SOULZ</Text>
                        <Switch
                          value={allDevices.some(d => d.id.startsWith('sim-soul'))}
                          onValueChange={(val) => {
                            if (!val) {
                              setAllDevices(allDevices.filter(d => !d.id.startsWith('sim-soul')));
                            } else {
                              setAllDevices([...allDevices, 
                                { id: 'sim-soul-1', name: 'SOULZ', points: 43 } as any,
                                { id: 'sim-soul-2', name: 'SOULZ', points: 43 } as any,
                              ]);
                            }
                          }}
                          trackColor={{ false: 'rgba(255,255,255,0.1)', true: Colors.secondary }}
                          thumbColor={allDevices.some(d => d.id.startsWith('sim-soul')) ? '#000' : '#888'}
                        />
                      </View>
                    </View>
                  </View>
                )}
              </View>
              </View>

              {isActuallyConnected && (
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
                  onLongPressDevice={(device) => openSettings(device)}
                  writeToDevice={writeToDevice}
                />
              )}

              <View style={{ paddingHorizontal: Layout.padding }}>
              {!isBluetoothSupported && (
                <View style={styles.errorContainer}>
                  <Text style={{ color: Colors.error, textAlign: 'center' }}>Bluetooth is not supported or powered on this device. (Are you running on a simulator?)</Text>
                </View>
              )}

              {!isActuallyConnected && customGroups.length > 0 && (
                <View style={{ marginTop: 20 }}>
                  <Text style={[Typography.title, { marginBottom: 12, paddingHorizontal: 4 }]}>Groups</Text>
                  {customGroups.map((group) => (
                    <DeviceItem
                      key={group.id}
                      device={group}
                      isConnected={mockConnectedGroup === group.id}
                      isSelectionMode={false}
                      isSelected={false}
                      onPress={() => {
                        setMockConnected(true);
                        setMockConnectedGroup(group.id);
                        
                        const devicesToConnect = allDevices.filter(d => group.deviceIds.includes(d.id));
                        if (devicesToConnect.length > 0) {
                          connectToDevices(devicesToConnect);
                        }
                      }}
                      onLongPress={() => {
                        setEditingGroupId(group.id);
                        setGroupModalMode('rename');
                        setIsGroupModalVisible(true);
                      }}
                      showGroupIcon={true}
                    />
                  ))}
                </View>
              )}

              {!isActuallyConnected && (
                <>
                  <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginTop: 20, marginBottom: 12, paddingHorizontal: 4 }}>
                    <Text style={Typography.title}>Available Devices</Text>
                  </View>
                </>
              )}
              </View>
            </View>
          }
          data={!isActuallyConnected ? allDevices : []}
          extraData={updateTrigger}
          keyExtractor={(item) => item.id}
          renderItem={({ item }: { item: any }) => (
            <View style={{ paddingHorizontal: Layout.padding }}>
              <DeviceItem
                device={item}
                isConnected={displayConnectedDevices.some(d => d.id === item.id)}
                isSelectionMode={isSelectionMode}
                isSelected={selectedIds.includes(item.id)}
                onPress={() => {
                  if (isSelectionMode) {
                    toggleSelect(item.id);
                    return;
                  }
                  if (item.id.startsWith('sim-')) {
                    setMockConnected(true);
                    setMockConnectedDevice(item.id);
                    return;
                  }
                  connectToDevice(item);
                  if (IS_BROWSER_DEMO) {
                    setMockConnected(true);
                    setMockConnectedDevice(item.id);
                  }
                }}
                onLongPress={() => {
                  openSettings(item);
                }}
                showGroupIcon={false}
              />
            </View>
          )}
          contentContainerStyle={{ paddingBottom: 100 }}

          ListEmptyComponent={
            !isActuallyConnected ? (
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
            stripType: 'GRB (WS2812B)',
            sorting: 'GRB',
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

const styles = StyleSheet.create({
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
    color: Colors.text,
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
