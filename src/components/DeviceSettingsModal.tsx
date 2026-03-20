import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, Modal, TouchableOpacity, TextInput, ScrollView, Platform } from 'react-native';
import { Colors, Typography, Layout } from '../theme/theme';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';

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

interface DeviceSettingsModalProps {
  isVisible: boolean;
  onClose: () => void;
  onSave: (settings: DeviceSettings) => void;
  initialSettings: DeviceSettings;
  groups?: any[];
  writeToDevice?: (payload: number[]) => Promise<void>;
}

export default function DeviceSettingsModal({ isVisible, onClose, onSave, initialSettings, groups, writeToDevice }: DeviceSettingsModalProps) {
  const [settings, setSettings] = useState<DeviceSettings>(initialSettings);
  const [namePreset, setNamePreset] = useState<string>(
    initialSettings.name.includes('Left') ? 'Left Skate' : (initialSettings.name.includes('Right') ? 'Right Skate' : 'Manual')
  );

  useEffect(() => {
    setSettings(initialSettings);
    setNamePreset(initialSettings.name.includes('Left') ? 'Left Skate' : (initialSettings.name.includes('Right') ? 'Right Skate' : 'Manual'));
  }, [initialSettings]);

  const handleSave = () => {
    onSave(settings);
    
    // Sync to hardware
    if (writeToDevice) {
      const payload = ZenggeProtocol.setHardwareConfig(
        settings.points,
        settings.sorting,
        settings.stripType
      );
      writeToDevice(payload);
    }
    
    onClose();
  };

  const handlePresetChange = (preset: string) => {
    setNamePreset(preset);
    if (preset === 'Manual') {
      // Keep existing name or clear if it was a preset
      if (settings.name.includes('Left') || settings.name.includes('Right')) {
        setSettings({ ...settings, name: '' });
      }
    } else {
      // Convert "Left Skate" preset to just "Left" for the actual device name 
      const cleanSuffix = preset.replace(' Skate', '');
      setSettings({ ...settings, name: `${settings.type} ${cleanSuffix}` });
    }
  };

  const setType = (type: 'HALOZ' | 'SOULZ') => {
    // If not manual, preserve the directional suffix
    const cleanSuffix = namePreset !== 'Manual' ? namePreset.replace(' Skate', '') : '';
    const newName = namePreset !== 'Manual' ? `${type} ${cleanSuffix}` : settings.name;
    setSettings({ ...settings, type, name: newName });
  };

  return (
    <Modal
      visible={isVisible}
      animationType="slide"
      transparent={true}
      onRequestClose={onClose}
    >
      <View style={styles.overlay}>
        <View style={styles.content}>
          <View style={styles.header}>
            <Text style={Typography.title}>Hardware Settings</Text>
            <TouchableOpacity onPress={onClose}>
              <Text style={{ color: Colors.textMuted, fontSize: 18 }}>✕</Text>
            </TouchableOpacity>
          </View>
          <ScrollView style={styles.form}>
            <View style={styles.inputGroup}>
              <Text style={styles.label}>Identify Position</Text>
              <View style={styles.buttonGroup}>
                {['Left Skate', 'Right Skate', 'Manual'].map((preset) => (
                  <TouchableOpacity 
                    key={preset}
                    style={[
                      styles.groupButton, 
                      namePreset === preset && styles.groupButtonActive
                    ]}
                    onPress={() => handlePresetChange(preset)}
                  >
                    <Text style={[
                      styles.groupButtonText,
                      namePreset === preset && styles.groupButtonTextActive
                    ]}>{preset}</Text>
                  </TouchableOpacity>
                ))}
              </View>
            </View>

            {namePreset === 'Manual' && (
              <View style={styles.inputGroup}>
                <Text style={styles.label}>Custom Device Name</Text>
                <TextInput 
                  style={styles.input}
                  value={settings.name}
                  onChangeText={(text) => setSettings({ ...settings, name: text })}
                  placeholder="e.g. My Custom HALO"
                  placeholderTextColor={Colors.textMuted}
                />
              </View>
            )}

            <View style={styles.inputGroup}>
              <Text style={styles.label}>Product Type</Text>
              <View style={styles.buttonGroup}>
                {['HALOZ', 'SOULZ'].map((type) => (
                  <TouchableOpacity 
                    key={type}
                    style={[
                      styles.groupButton, 
                      settings.type === type && styles.groupButtonActive
                    ]}
                    onPress={() => setType(type as 'HALOZ' | 'SOULZ')}
                  >
                    <Text style={[
                      styles.groupButtonText,
                      settings.type === type && styles.groupButtonTextActive
                    ]}>{type}</Text>
                  </TouchableOpacity>
                ))}
              </View>
            </View>

            <View style={styles.inputGroup}>
              <Text style={styles.label}>Group / Pair Mode</Text>
              <View style={styles.buttonGroup}>
                <TouchableOpacity 
                  style={[styles.groupButton, !settings.grouped && styles.groupButtonActive]}
                  onPress={() => setSettings({ ...settings, grouped: false })}
                >
                  <Text style={[styles.groupButtonText, !settings.grouped && styles.groupButtonTextActive]}>Single</Text>
                </TouchableOpacity>
                <TouchableOpacity 
                  style={[styles.groupButton, settings.grouped && styles.groupButtonActive]}
                  onPress={() => setSettings({ ...settings, grouped: true })}
                >
                  <Text style={[styles.groupButtonText, settings.grouped && styles.groupButtonTextActive]}>Paired / Grouped</Text>
                </TouchableOpacity>
              </View>
              {settings.grouped && (
                <View style={{ marginTop: 12 }}>
                  <Text style={styles.label}>Assign to Group</Text>
                  
                  {groups && groups.filter(g => g.isGroup).length > 0 && (
                    <ScrollView style={{ maxHeight: 120, marginBottom: 12 }} nestedScrollEnabled>
                      {groups.filter(g => g.isGroup).map(g => (
                        <TouchableOpacity 
                          key={g.id}
                          style={[styles.input, { paddingVertical: 12, marginBottom: 4 }, settings.groupId === g.id && { borderColor: Colors.primary }]}
                          onPress={() => setSettings({ ...settings, groupId: g.id, groupName: g.name })}
                        >
                          <Text style={{ color: settings.groupId === g.id ? Colors.primary : Colors.text }}>{g.name}</Text>
                        </TouchableOpacity>
                      ))}
                    </ScrollView>
                  )}

                  <Text style={styles.label}>{settings.groupId ? 'Rename Selected Group' : 'Or Create New Group...'}</Text>
                  <TextInput 
                    style={styles.input}
                    value={settings.groupName}
                    onChangeText={(text) => {
                       // If they start typing and had a group selected but change the text, it will rename the selected group.
                       // If they clear the groupId, it makes a new one. But let's just let it rename.
                       setSettings({ ...settings, groupName: text });
                    }}
                    placeholder="Enter Group Name"
                    placeholderTextColor={Colors.textMuted}
                  />
                  {settings.groupId && (
                    <TouchableOpacity onPress={() => setSettings({...settings, groupId: undefined, groupName: 'My Roller Skates'})} style={{ marginTop: 8 }}>
                       <Text style={{ color: Colors.primary, fontSize: 12 }}>+ Deselect/Create New</Text>
                    </TouchableOpacity>
                  )}
                </View>
              )}
              <Text style={[Typography.caption, { marginTop: 6, color: Colors.textMuted }]}>
                Enabling Paired mode allows you to control both Left and Right skates simultaneously.
              </Text>
            </View>

            <View style={styles.row}>
              <View style={[styles.inputGroup, { flex: 1, marginRight: 8 }]}>
                <Text style={styles.label}>LED Points</Text>
                <TextInput 
                  style={styles.input}
                  value={settings.points.toString()}
                  onChangeText={(text) => setSettings({ ...settings, points: parseInt(text) || 0 })}
                  keyboardType="numeric"
                />
              </View>
              <View style={[styles.inputGroup, { flex: 1, marginLeft: 8 }]}>
                <Text style={styles.label}>Segments</Text>
                <TextInput 
                  style={styles.input}
                  value={settings.segments.toString()}
                  onChangeText={(text) => setSettings({ ...settings, segments: parseInt(text) || 0 })}
                  keyboardType="numeric"
                />
              </View>
            </View>

            <View style={styles.inputGroup}>
              <Text style={styles.label}>LED Strip Type</Text>
              <ScrollView horizontal showsHorizontalScrollIndicator={false} style={{ marginTop: 8 }}>
                {['RGB (WS2811)', 'RGBW', 'GRB (WS2812B)', 'BRG'].map((st) => (
                  <TouchableOpacity 
                    key={st}
                    style={[
                      styles.pill, 
                      settings.stripType === st && styles.pillActive
                    ]}
                    onPress={() => setSettings({ ...settings, stripType: st })}
                  >
                    <Text style={[
                      styles.pillText,
                      settings.stripType === st && styles.pillTextActive
                    ]}>{st}</Text>
                  </TouchableOpacity>
                ))}
              </ScrollView>
            </View>

            <View style={styles.inputGroup}>
              <Text style={styles.label}>Color Sorting</Text>
              <View style={styles.buttonGroup}>
                {['RGB', 'RBG', 'GRB', 'GBR', 'BRG', 'BGR'].map((sort) => (
                  <TouchableOpacity 
                    key={sort}
                    style={[
                      styles.miniPill, 
                      settings.sorting === sort && styles.miniPillActive
                    ]}
                    onPress={() => setSettings({ ...settings, sorting: sort })}
                  >
                    <Text style={[
                      styles.miniPillText,
                      settings.sorting === sort && styles.miniPillTextActive
                    ]}>{sort}</Text>
                  </TouchableOpacity>
                ))}
              </View>
            </View>
          </ScrollView>

          <View style={styles.footer}>
            <TouchableOpacity style={styles.cancelButton} onPress={onClose}>
              <Text style={styles.cancelButtonText}>CANCEL</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.saveButton} onPress={handleSave}>
              <Text style={styles.saveButtonText}>SAVE HARDWARE CONFIG</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    </Modal>
  );
}

const styles = StyleSheet.create({
  overlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.85)',
    justifyContent: 'flex-end',
  },
  content: {
    backgroundColor: Colors.surface,
    borderTopLeftRadius: 24,
    borderTopRightRadius: 24,
    padding: 24,
    maxHeight: '85%',
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 24,
  },
  form: {
    marginBottom: 24,
  },
  inputGroup: {
    marginBottom: 20,
  },
  label: {
    ...Typography.caption,
    color: Colors.textMuted,
    marginBottom: 8,
    textTransform: 'uppercase',
    letterSpacing: 1,
  },
  input: {
    backgroundColor: Colors.background,
    borderRadius: 8,
    padding: 12,
    color: Colors.text,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
    fontSize: 16,
  },
  row: {
    flexDirection: 'row',
  },
  buttonGroup: {
    flexDirection: 'row',
    gap: 8,
    flexWrap: 'wrap',
  },
  groupButton: {
    flex: 1,
    paddingVertical: 10,
    alignItems: 'center',
    borderRadius: 8,
    backgroundColor: Colors.background,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
  },
  groupButtonActive: {
    backgroundColor: Colors.primary,
    borderColor: Colors.primary,
  },
  groupButtonText: {
    color: Colors.textMuted,
    fontWeight: 'bold',
  },
  groupButtonTextActive: {
    color: Colors.text,
  },
  pill: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: 20,
    backgroundColor: Colors.background,
    marginRight: 8,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
  },
  pillActive: {
    backgroundColor: Colors.secondary,
    borderColor: Colors.secondary,
  },
  pillText: {
    color: Colors.textMuted,
    fontSize: 12,
    fontWeight: '600',
  },
  pillTextActive: {
    color: Colors.background,
    fontWeight: 'bold',
  },
  miniPill: {
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 4,
    backgroundColor: Colors.background,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
  },
  miniPillActive: {
    backgroundColor: Colors.secondary,
    borderColor: Colors.secondary,
  },
  miniPillText: {
    color: Colors.textMuted,
    fontSize: 11,
    fontWeight: 'bold',
  },
  miniPillTextActive: {
    color: Colors.background,
  },
  footer: {
    flexDirection: 'row',
    gap: 12,
    marginBottom: Platform.OS === 'ios' ? 20 : 0,
  },
  cancelButton: {
    flex: 1,
    paddingVertical: 14,
    alignItems: 'center',
    borderRadius: 12,
    backgroundColor: Colors.surfaceHighlight,
  },
  cancelButtonText: {
    color: Colors.textMuted,
    fontWeight: 'bold',
  },
  saveButton: {
    flex: 2,
    paddingVertical: 14,
    alignItems: 'center',
    borderRadius: 12,
    backgroundColor: Colors.primary,
  },
  saveButtonText: {
    color: Colors.text,
    fontWeight: 'bold',
  }
});
