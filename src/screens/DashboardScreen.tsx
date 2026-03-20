import React, { useEffect, useState, useMemo } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, SafeAreaView, FlatList, ActivityIndicator } from 'react-native';
import { Colors, Typography, Layout } from '../theme/theme';
import Header from '../components/Header';
import DeviceItem from '../components/DeviceItem';
import useBLE from '../hooks/useBLE';

import Sk8lytzController from '../components/Sk8lytzController';
import DeviceSettingsModal from '../components/DeviceSettingsModal';
import GroupSettingsModal from '../components/GroupSettingsModal';

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
    connectToDevice,
    connectedDevice,
    disconnectFromDevice,
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

  const displayConnectedDevices = useMemo(() => {
    if (!mockConnected) return connectedDevice ? [connectedDevice] : [];

    // In Browser Demo / Mock mode, we pull the actual device objects from allDevices 
    // to ensure settings like 'points' are reflected after being edited.
    if (mockConnectedGroup) {
      const g = customGroups.find(x => x.id === mockConnectedGroup);
      if (g) {
        return allDevices
          .filter(d => g.deviceIds.includes(d.id))
          .map(d => ({ ...d, grouped: true, groupId: g.id, groupName: g.name, points: (d as any).points || (d.name?.toLowerCase().includes('soul') ? 43 : 24) }));
      }
    }

    const singleId = mockConnectedDevice || 'sim-soul-1';
    const single = allDevices.find(d => d.id === singleId);
    return single ? [{ ...single, grouped: false, points: (single as any).points || (single.name?.toLowerCase().includes('soul') ? 43 : 24) }] : [];
  }, [mockConnected, mockConnectedDevice, mockConnectedGroup, allDevices, connectedDevice, updateTrigger, customGroups]);

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

  const saveGroup = (name: string, deviceIds: string[]) => {
    if (groupModalMode === 'create') {
      const existing = customGroups.find(g => g.name.toLowerCase() === name.toLowerCase());
      if (existing) {
        setCustomGroups(prev => prev.map(g => g.id === existing.id ? { ...g, deviceIds: Array.from(new Set([...g.deviceIds, ...deviceIds])) } : g));
      } else {
        const newGroupId = `group-${Date.now()}`;
        setCustomGroups(prev => [...prev, { id: newGroupId, name, isGroup: true, deviceIds }]);
      }
      setIsSelectionMode(false);
      setSelectedIds([]);
    } else if (groupModalMode === 'rename' && editingGroupId) {
      if (deviceIds.length === 0) {
        deleteGroup(editingGroupId);
        return;
      }
      setCustomGroups(prev => prev.map(g => g.id === editingGroupId ? { ...g, name, deviceIds } : g));
    }
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

  const saveSettings = (settings: DeviceSettings) => {
    console.log('Saved settings for', selectedDeviceForSettings?.id, ':', settings);
    // In a real app, this would persist to AsyncStorage or the device via BLE
    // In demo, we update the local device name if it's a mock
    if (IS_BROWSER_DEMO && selectedDeviceForSettings) {
      if (settings.grouped && settings.groupName) {
        let targetGroupId = settings.groupId;

        if (!targetGroupId) {
          const existingGroup = customGroups.find(g => g.name.toLowerCase() === settings.groupName?.toLowerCase());
          if (existingGroup) {
            targetGroupId = existingGroup.id;
          }
        }

        if (targetGroupId) {
          setCustomGroups(prev => prev.map(g => g.id === targetGroupId ? { ...g, name: settings.groupName } : g));
          setCustomGroups(prev => prev.map(g => g.id === targetGroupId && !g.deviceIds.includes(selectedDeviceForSettings.id)
            ? { ...g, deviceIds: [...g.deviceIds, selectedDeviceForSettings.id] }
            : g));
          settings.groupId = targetGroupId;
        } else {
          const newGroupId = `group-${Date.now()}`;
          setCustomGroups([...customGroups, { id: newGroupId, name: settings.groupName, isGroup: true, deviceIds: [selectedDeviceForSettings.id] }]);
          settings.groupId = newGroupId;
        }
      } else if (!settings.grouped) {
        setCustomGroups(prev => prev.map(g => ({
          ...g,
          deviceIds: g.deviceIds.filter((id: string) => id !== selectedDeviceForSettings.id)
        })).filter(g => g.deviceIds.length > 0));
      }

      selectedDeviceForSettings.name = settings.name;
      selectedDeviceForSettings.type = settings.type;
      selectedDeviceForSettings.points = settings.points;
      selectedDeviceForSettings.grouped = settings.grouped;
      selectedDeviceForSettings.groupId = settings.groupId;
      setUpdateTrigger(prev => prev + 1);
    }
    setIsSettingsVisible(false);
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      <Header />
      <View style={styles.container}>

        <FlatList
          ListHeaderComponent={
            <View style={{ paddingBottom: 16 }}>
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
                )}
              </View>

              {isActuallyConnected && (
                <Sk8lytzController
                  lockedProduct={(displayConnectedDevices[0] as any)?.type || ((displayConnectedDevices[0] as any)?.name?.toLowerCase().includes('soul') ? 'SOULZ' : 'HALOZ')}
                  isPaired={isGrouped}
                  points={(displayConnectedDevices[0] as any).points}
                  devices={displayConnectedDevices}
                  onLongPressDevice={(device) => openSettings(device)}
                />
              )}

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
          }
          data={!isActuallyConnected ? allDevices : []}
          extraData={updateTrigger}
          keyExtractor={(item) => item.id}
          renderItem={({ item }: { item: any }) => (
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
    padding: Layout.padding,
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
