import React from 'react';
import { View, Text, ScrollView, TouchableOpacity, TextInput } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Spacing } from '../../theme/theme';
import { HardwareStatusPills } from '../dashboard/HardwareStatusPills';

function formatDate(iso: string) {
  if (!iso) return '';
  return new Date(iso).toLocaleDateString(undefined, { month: 'short', day: 'numeric', year: 'numeric' });
}

export default function AccountTabDevices({
  Colors,
  styles,
  devices,
  groupedDevices,
  editingGroupId,
  groupNewName,
  setGroupNewName,
  handleRenameGroup,
  setEditingGroupId,
  handleForgetGroup,
  editingDeviceId,
  deviceNewName,
  setDeviceNewName,
  handleRenameDevice,
  setEditingDeviceId,
  setAdvancedModalDevice,
  setAdvancedModalVisible,
  handleForgetDevice,
}: any) {
  return (
    <ScrollView contentContainerStyle={styles.body} showsVerticalScrollIndicator={false}>
      {devices.length === 0 ? (
        <View style={styles.emptyState}>
          <MaterialCommunityIcons name="bluetooth-off" size={52} color={Colors.textMuted} />
          <Text style={styles.emptyTitle}>No Registered Devices</Text>
          <Text style={styles.emptySubtitle}>Pair your SK8Lytz skates from the main screen to see them here.</Text>
        </View>
      ) : (
        Object.entries(groupedDevices).map(([groupName, groupDevs]: [string, any]) => {
          if (groupDevs.length === 0) return null;
          const isUngrouped = groupName === "_Ungrouped";

          return (
            <View key={groupName} style={{ marginBottom: Spacing.lg }}>
              {!isUngrouped && (
                <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: Spacing.sm, paddingHorizontal: Spacing.lg }}>
                  {editingGroupId === groupName ? (
                    <TextInput
                      style={[styles.input, { flex: 1, marginBottom: 0, paddingVertical: Spacing.sm, paddingHorizontal: Spacing.md, marginRight: Spacing.sm }]}
                      value={groupNewName} onChangeText={setGroupNewName}
                      placeholder={groupName} placeholderTextColor={Colors.textMuted}
                      autoFocus maxLength={32} returnKeyType="done"
                      onSubmitEditing={() => handleRenameGroup(groupName)}
                    />
                  ) : (
                    <Text style={{ fontSize: 16, fontWeight: 'bold', color: Colors.text, textTransform: 'uppercase' }}>
                      {groupName}
                    </Text>
                  )}
                  
                  <View style={{ flexDirection: 'row', gap: Spacing.sm }}>
                    {editingGroupId === groupName ? (
                      <>
                        <TouchableOpacity style={styles.deviceSaveBtn} onPress={() => handleRenameGroup(groupName)}>
                          <MaterialCommunityIcons name="check" size={16} color="#000" />
                        </TouchableOpacity>
                        <TouchableOpacity onPress={() => { setEditingGroupId(null); setGroupNewName(''); }}>
                          <MaterialCommunityIcons name="close" size={18} color={Colors.textMuted} />
                        </TouchableOpacity>
                      </>
                    ) : (
                      <>
                        <TouchableOpacity onPress={() => { setEditingGroupId(groupName); setGroupNewName(groupName); }}>
                          <MaterialCommunityIcons name="pencil" size={18} color={Colors.textMuted} />
                        </TouchableOpacity>
                        <TouchableOpacity onPress={() => handleForgetGroup(groupName)}>
                          <MaterialCommunityIcons name="trash-can-outline" size={18} color="#FF4444" />
                        </TouchableOpacity>
                      </>
                    )}
                  </View>
                </View>
              )}

              {groupDevs.map((device: any) => (
                <View key={device.id} style={styles.deviceCard}>
                  <MaterialCommunityIcons
                    name={device.type === 'SOULZ' ? 'skate' : 'lightning-bolt-circle'}
                    size={22} color={Colors.primary} style={{ marginRight: Spacing.md }} />
                  <View style={{ flex: 1 }}>
                    {editingDeviceId === device.id ? (
                      <TextInput
                        style={[styles.input, { marginBottom: 0, paddingVertical: Spacing.sm, paddingHorizontal: Spacing.md }]}
                        value={deviceNewName} onChangeText={setDeviceNewName}
                        placeholder={device.customName || device.name}
                        placeholderTextColor={Colors.textMuted}
                        autoFocus maxLength={32}
                        returnKeyType="done"
                        onSubmitEditing={() => handleRenameDevice(device)}
                      />
                    ) : (
                      <>
                        <Text style={styles.deviceName}>{device.customName || device.name}</Text>
                        {device.type && <Text style={styles.deviceMeta}>{device.type} · {device.id.slice(-8)}</Text>}
                        {device.registeredAt && <Text style={styles.deviceMeta}>Paired {formatDate(device.registeredAt)}</Text>}
                        {/* Hardware pills — shows LED count, strip type, sorting from registration data */}
                        {(device.led_points || device.ic_type) && (
                          <HardwareStatusPills device={{
                            led_points: device.led_points,
                            segments: device.segments,
                            ic_type: device.ic_type,
                            color_sorting: device.color_sorting,
                          }} />
                        )}
                      </>
                    )}
                  </View>
                  <View style={{ flexDirection: 'row', gap: Spacing.sm, alignItems: 'center' }}>
                    {editingDeviceId === device.id ? (
                      <>
                        <TouchableOpacity style={styles.deviceSaveBtn} onPress={() => handleRenameDevice(device)}>
                          <MaterialCommunityIcons name="check" size={16} color="#000" />
                        </TouchableOpacity>
                        <TouchableOpacity onPress={() => { setEditingDeviceId(null); setDeviceNewName(''); }}>
                          <MaterialCommunityIcons name="close" size={18} color={Colors.textMuted} />
                        </TouchableOpacity>
                      </>
                    ) : (
                      <>
                        <TouchableOpacity style={styles.deviceIconBtn} onPress={() => {
                          setEditingDeviceId(device.id);
                          setDeviceNewName(device.customName || device.name);
                        }}>
                          <MaterialCommunityIcons name="pencil" size={16} color={Colors.textMuted} />
                        </TouchableOpacity>
                        <TouchableOpacity style={styles.deviceIconBtn} onPress={() => {
                          setAdvancedModalDevice(device);
                          setAdvancedModalVisible(true);
                        }}>
                          <MaterialCommunityIcons name="chip" size={16} color={Colors.primary} />
                        </TouchableOpacity>
                        <TouchableOpacity style={styles.deviceIconBtn} onPress={() => handleForgetDevice(device)}>
                          <MaterialCommunityIcons name="trash-can-outline" size={16} color="#FF4444" />
                        </TouchableOpacity>
                      </>
                    )}
                  </View>
                </View>
              ))}
            </View>
          );
        })
      )}

      <Text style={[styles.hint, { marginTop: Spacing.lg }]}>
        Devices are paired from the scanner. Remove them here if you no longer use them.
      </Text>
      <View style={{ height: 20 }} />
    </ScrollView>
  );
}
