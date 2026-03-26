import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, Modal, SafeAreaView, TouchableOpacity, ScrollView, Platform, TextInput } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useTheme } from '../context/ThemeContext';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Typography, Layout } from '../theme/theme';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import DeviceItem from './DeviceItem';

interface Sk8LytzProgrammerModalProps {
  visible: boolean;
  onClose: () => void;
  allDevices: any[];
  writeToDevice: (data: number[], deviceId?: string) => Promise<void>;
  isScanning: boolean;
  handleScan: () => void;
}

export default function Sk8LytzProgrammerModal({ visible, onClose, allDevices, writeToDevice, isScanning, handleScan }: Sk8LytzProgrammerModalProps) {
  const { Colors, isDark } = useTheme();
  const [selectedIds, setSelectedIds] = useState<string[]>([]);
  const bg = isDark ? '#0f111a' : '#f0f2f5';
  const cardBg = isDark ? '#1a1d2d' : '#ffffff';
  const textPrimary = isDark ? '#ffffff' : '#111827';
  const textMuted = isDark ? '#9ca3af' : '#6b7280';
  const borderColor = isDark ? '#2d3348' : '#e5e7eb';

  const [halozConfig, setHalozConfig] = useState({ points: 16, segments: 2, colorOrder: 'GRB', stripType: 'WS2812B' });
  const [soulzConfig, setSoulzConfig] = useState({ points: 43, segments: 2, colorOrder: 'GRB', stripType: 'WS2812B' });

  const [isEditingConfig, setIsEditingConfig] = useState<'HALOZ' | 'SOULZ' | null>(null);
  const [tempConfig, setTempConfig] = useState({ points: 16, segments: 2, colorOrder: 'GRB', stripType: 'WS2812B' });

  useEffect(() => {
    const loadPresets = async () => {
      try {
        const h = await AsyncStorage.getItem('@sk8_program_haloz');
        const s = await AsyncStorage.getItem('@sk8_program_soulz');
        if (h) setHalozConfig({ segments: 2, ...JSON.parse(h) });
        if (s) setSoulzConfig({ segments: 2, ...JSON.parse(s) });
      } catch (e) {
        // fail silently
      }
    };
    loadPresets();
  }, [visible]);

  const saveConfig = async () => {
    try {
      if (isEditingConfig === 'HALOZ') {
        setHalozConfig(tempConfig);
        await AsyncStorage.setItem('@sk8_program_haloz', JSON.stringify(tempConfig));
      } else if (isEditingConfig === 'SOULZ') {
        setSoulzConfig(tempConfig);
        await AsyncStorage.setItem('@sk8_program_soulz', JSON.stringify(tempConfig));
      }
    } catch (e) {}
    setIsEditingConfig(null);
  };

  const toggleSelect = (id: string) => {
    if (selectedIds.includes(id)) {
      setSelectedIds(selectedIds.filter(i => i !== id));
    } else {
      setSelectedIds([...selectedIds, id]);
    }
  };

  const selectAll = () => {
    setSelectedIds(allDevices.map(d => d.id));
  };

  const clearSelection = () => {
    setSelectedIds([]);
  };

  const flashHaloz = async () => {
    for (const id of selectedIds) {
      await writeToDevice(ZenggeProtocol.setHardwareConfig(halozConfig.points, halozConfig.colorOrder, halozConfig.stripType, halozConfig.segments || 2), id);
    }
    alert(`Successfully flashed HALOZ config to ${selectedIds.length} devices.`);
  };

  const flashSoulz = async () => {
    for (const id of selectedIds) {
      await writeToDevice(ZenggeProtocol.setHardwareConfig(soulzConfig.points, soulzConfig.colorOrder, soulzConfig.stripType, soulzConfig.segments || 2), id);
    }
    alert(`Successfully flashed SOULZ config to ${selectedIds.length} devices.`);
  };

  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen" onRequestClose={onClose}>
      <SafeAreaView style={[styles.root, { backgroundColor: bg }]}>
        <View style={[styles.header, { borderBottomColor: borderColor }]}>
          <View>
            <Text style={[styles.title, { color: textPrimary }]}>⚡ SK8Lytz Programmer</Text>
            <Text style={[styles.subtitle, { color: textMuted }]}>Batch Flash Hardware Configs</Text>
          </View>
          <TouchableOpacity onPress={onClose} style={styles.closeBtn}>
            <MaterialCommunityIcons name="close" size={24} color={textPrimary} />
          </TouchableOpacity>
        </View>

        <ScrollView style={styles.content}>
          <View style={[styles.actionCard, { backgroundColor: cardBg, borderColor }]}>
            <Text style={[styles.sectionTitle, { color: textPrimary }]}>Target Configuration</Text>
            <Text style={{ color: textMuted, fontSize: 13, marginBottom: 16 }}>
              Select target payload. This overwrites {selectedIds.length} connected devices instantly.
            </Text>
            
            <View style={styles.buttonRow}>
              <TouchableOpacity 
                style={[styles.flashBtn, { backgroundColor: 'rgba(0, 240, 255, 0.15)', borderColor: '#00f0ff' }]}
                onPress={flashHaloz}
                onLongPress={() => {
                  setTempConfig(halozConfig);
                  setIsEditingConfig('HALOZ');
                }}
                disabled={selectedIds.length === 0}
              >
                <Text style={[styles.flashBtnText, { color: '#00f0ff', opacity: selectedIds.length === 0 ? 0.4 : 1 }]}>
                  FLASH HALOZ ({halozConfig.points} Pts)
                </Text>
                <Text style={{ color: '#00f0ff', fontSize: 9, opacity: selectedIds.length === 0 ? 0.3 : 0.6, marginTop: 4 }}>
                  {halozConfig.segments} Seg • {halozConfig.colorOrder} • {halozConfig.stripType}
                </Text>
              </TouchableOpacity>
              
              <TouchableOpacity 
                style={[styles.flashBtn, { backgroundColor: 'rgba(255, 61, 0, 0.15)', borderColor: Colors.secondary }]}
                onPress={flashSoulz}
                onLongPress={() => {
                  setTempConfig(soulzConfig);
                  setIsEditingConfig('SOULZ');
                }}
                disabled={selectedIds.length === 0}
              >
                <Text style={[styles.flashBtnText, { color: Colors.secondary, opacity: selectedIds.length === 0 ? 0.4 : 1 }]}>
                  FLASH SOULZ ({soulzConfig.points} Pts)
                </Text>
                <Text style={{ color: Colors.secondary, fontSize: 9, opacity: selectedIds.length === 0 ? 0.3 : 0.6, marginTop: 4 }}>
                  {soulzConfig.segments} Seg • {soulzConfig.colorOrder} • {soulzConfig.stripType}
                </Text>
              </TouchableOpacity>
            </View>
          </View>

          <View style={styles.selectionHeader}>
            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
              <Text style={[styles.sectionTitle, { color: textPrimary, marginBottom: 0 }]}>
                Discovered Hardware ({allDevices.length})
              </Text>
              <TouchableOpacity onPress={handleScan} style={{ marginLeft: 12, paddingHorizontal: 12, paddingVertical: 4, backgroundColor: isScanning ? 'transparent' : 'rgba(0, 240, 255, 0.1)', borderRadius: 12, borderWidth: 1, borderColor: isScanning ? 'transparent' : '#00f0ff' }}>
                 <Text style={{ color: isScanning ? textMuted : '#00f0ff', fontSize: 10, fontWeight: '900' }}>{isScanning ? 'SCANNING...' : 'RESCAN'}</Text>
              </TouchableOpacity>
            </View>
            <View style={{ flexDirection: 'row' }}>
              <TouchableOpacity onPress={selectAll} style={styles.textBtn}>
                <Text style={{ color: Colors.primary, fontWeight: 'bold', fontSize: 12 }}>ALL</Text>
              </TouchableOpacity>
              <View style={{ width: 12 }} />
              <TouchableOpacity onPress={clearSelection} style={styles.textBtn}>
                <Text style={{ color: Colors.error, fontWeight: 'bold', fontSize: 12 }}>NONE</Text>
              </TouchableOpacity>
            </View>
          </View>

          <View style={styles.listContainer}>
            {allDevices.length === 0 ? (
              <Text style={{ color: textMuted, textAlign: 'center', marginVertical: 32 }}>No devices discovered.</Text>
            ) : (
              allDevices.map((d) => (
                <DeviceItem
                  key={d.id}
                  device={d}
                  isConnected={true}
                  isSelectionMode={true}
                  isSelected={selectedIds.includes(d.id)}
                  onPress={() => toggleSelect(d.id)}
                  onLongPress={() => toggleSelect(d.id)}
                />
              ))
            )}
          </View>
          <View style={{ height: 40 }} />
        </ScrollView>

        <Modal
          visible={isEditingConfig !== null}
          transparent={true}
          animationType="fade"
          onRequestClose={() => setIsEditingConfig(null)}
        >
          <View style={{ flex: 1, backgroundColor: 'rgba(0,0,0,0.85)', justifyContent: 'center', alignItems: 'center' }}>
            <View style={{ backgroundColor: cardBg, padding: 24, borderRadius: 16, width: '90%', borderWidth: 1, borderColor: isEditingConfig === 'HALOZ' ? '#00f0ff' : Colors.secondary }}>
              <Text style={{ ...Typography.title, color: isEditingConfig === 'HALOZ' ? '#00f0ff' : Colors.secondary, marginBottom: 16 }}>
                Edit {isEditingConfig} Payload
              </Text>
              
              <Text style={{ color: textMuted, fontSize: 13, marginBottom: 8, fontWeight: 'bold' }}>PIXEL COUNT</Text>
              <TextInput
                style={{ backgroundColor: bg, borderColor, borderWidth: 1, borderRadius: 8, padding: 12, color: textPrimary, fontSize: 16, marginBottom: 16, textAlign: 'center' }}
                value={String(tempConfig.points || 0)}
                onChangeText={(t) => setTempConfig({ ...tempConfig, points: parseInt(t.replace(/[^0-9]/g, '')) || 0 })}
                keyboardType="number-pad"
                maxLength={4}
              />

              <Text style={{ color: textMuted, fontSize: 13, marginBottom: 8, fontWeight: 'bold' }}>SEGMENT COUNT</Text>
              <TextInput
                style={{ backgroundColor: bg, borderColor, borderWidth: 1, borderRadius: 8, padding: 12, color: textPrimary, fontSize: 16, marginBottom: 16, textAlign: 'center' }}
                value={String(tempConfig.segments || 1)}
                onChangeText={(t) => setTempConfig({ ...tempConfig, segments: parseInt(t.replace(/[^0-9]/g, '')) || 1 })}
                keyboardType="number-pad"
                maxLength={2}
              />

              <Text style={{ color: textMuted, fontSize: 13, marginBottom: 8, fontWeight: 'bold' }}>COLOR ORDER</Text>
              <View style={{ flexDirection: 'row', flexWrap: 'wrap', marginBottom: 16 }}>
                {['RGB', 'RBG', 'GRB', 'GBR', 'BRG', 'BGR'].map(co => (
                  <TouchableOpacity
                    key={co}
                    onPress={() => setTempConfig({ ...tempConfig, colorOrder: co })}
                    style={{ paddingVertical: 8, paddingHorizontal: 12, borderRadius: 6, borderWidth: 1, borderColor: tempConfig.colorOrder === co ? Colors.primary : borderColor, backgroundColor: tempConfig.colorOrder === co ? 'rgba(0, 240, 255, 0.1)' : 'transparent', marginRight: 8, marginBottom: 8 }}
                  >
                     <Text style={{ color: tempConfig.colorOrder === co ? Colors.primary : textMuted, fontSize: 12, fontWeight: 'bold' }}>{co}</Text>
                  </TouchableOpacity>
                ))}
              </View>

              <Text style={{ color: textMuted, fontSize: 13, marginBottom: 8, fontWeight: 'bold' }}>IC STRIP TYPE</Text>
              <View style={{ flexDirection: 'row', flexWrap: 'wrap', marginBottom: 24 }}>
                {['WS2812B', 'SM16703', 'WS2811', 'SK6812'].map(ic => (
                  <TouchableOpacity
                    key={ic}
                    onPress={() => setTempConfig({ ...tempConfig, stripType: ic })}
                    style={{ paddingVertical: 8, paddingHorizontal: 12, borderRadius: 6, borderWidth: 1, borderColor: tempConfig.stripType === ic ? Colors.secondary : borderColor, backgroundColor: tempConfig.stripType === ic ? 'rgba(255, 61, 0, 0.1)' : 'transparent', marginRight: 8, marginBottom: 8 }}
                  >
                     <Text style={{ color: tempConfig.stripType === ic ? Colors.secondary : textMuted, fontSize: 12, fontWeight: 'bold' }}>{ic}</Text>
                  </TouchableOpacity>
                ))}
              </View>

              <View style={{ flexDirection: 'row', justifyContent: 'flex-end' }}>
                <TouchableOpacity onPress={() => setIsEditingConfig(null)} style={{ padding: 16 }}>
                  <Text style={{ color: textMuted, fontWeight: 'bold' }}>CANCEL</Text>
                </TouchableOpacity>
                <TouchableOpacity onPress={saveConfig} style={{ padding: 16 }}>
                  <Text style={{ color: Colors.primary, fontWeight: 'bold' }}>SAVE OVERRIDE</Text>
                </TouchableOpacity>
              </View>
            </View>
          </View>
        </Modal>
      </SafeAreaView>
    </Modal>
  );
}

const styles = StyleSheet.create({
  root: { flex: 1 },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 20,
    paddingVertical: 16,
    borderBottomWidth: 1,
  },
  title: { fontSize: 20, fontWeight: '900', letterSpacing: 0.5 },
  subtitle: { fontSize: 13, marginTop: 4, fontWeight: '600' },
  closeBtn: { padding: 4 },
  content: { flex: 1, padding: 16 },
  actionCard: {
    borderWidth: 1,
    borderRadius: 12,
    padding: 16,
    marginBottom: 24,
  },
  sectionTitle: { fontSize: 16, fontWeight: '800', marginBottom: 8, letterSpacing: 0.5 },
  buttonRow: { flexDirection: 'row', justifyContent: 'space-between' },
  flashBtn: {
    flex: 1,
    borderWidth: 1,
    borderRadius: 8,
    paddingVertical: 14,
    marginHorizontal: 4,
    alignItems: 'center',
  },
  flashBtnText: { fontWeight: '800', fontSize: 13, letterSpacing: 0.5 },
  selectionHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 12,
    paddingHorizontal: 4,
  },
  textBtn: { padding: 4 },
  listContainer: { paddingBottom: 20 },
});
