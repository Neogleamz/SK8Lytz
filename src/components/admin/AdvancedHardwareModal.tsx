import React, { useState, useEffect } from 'react';
import { Modal, View, Text, TouchableOpacity, ScrollView, Alert } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../../context/ThemeContext';
import { Spacing } from '../../theme/theme';
import { IC_TYPE_NAMES, COLOR_SORTING_RGB, ZenggeProtocol } from '../../protocols/ZenggeProtocol';
import CustomSlider from '../CustomSlider';
import { useBLE } from '../../hooks/useBLE';

export interface AdvancedHardwareModalProps {
  visible: boolean;
  onClose: () => void;
  targetDeviceId: string | null;
  currentPoints?: number;
  currentSegments?: number;
  currentIcType?: string;
  currentSorting?: string;
}

export const AdvancedHardwareModal: React.FC<AdvancedHardwareModalProps> = ({
  visible, onClose, targetDeviceId, currentPoints = 30, currentSegments = 10, currentIcType = 'WS2812B', currentSorting = 'GRB'
}) => {
  const { Colors, isDark } = useTheme();
  const { writeToDevice } = useBLE();

  const [points, setPoints] = useState(currentPoints);
  const [segments, setSegments] = useState(currentSegments);
  const [icType, setIcType] = useState(currentIcType);
  const [sorting, setSorting] = useState(currentSorting);

  // Sync to props on open
  useEffect(() => {
    if (visible) {
      setPoints(currentPoints || 30);
      setSegments(currentSegments || 10);
      setIcType(currentIcType || 'WS2812B');
      setSorting(currentSorting || 'GRB');
    }
  }, [visible, currentPoints, currentSegments, currentIcType, currentSorting]);

  const handleWriteEEPROM = async () => {
    if (!targetDeviceId) return;

    // Validate limits
    if (points * segments > 2048) {
      Alert.alert('Limit Exceeded', 'Total pixels (Points * Segments) cannot exceed 2048 for ZENGGE hardware.');
      return;
    }

    const payload = ZenggeProtocol.writeHardwareSettingsByName(
      points, segments, icType, sorting
    );

    Alert.alert(
      '⚠️ Overwrite Hardware EEPROM',
      `You are writing directly to the non-volatile memory of ${targetDeviceId.slice(-4)}. The device will reboot.\n\nPoints: ${points}\nSegments: ${segments}\nIC: ${icType}\nSort: ${sorting}`,
      [
        { text: 'Cancel', style: 'cancel' },
        { 
          text: 'Flash Device', 
          style: 'destructive',
          onPress: async () => {
            const success = await writeToDevice(payload, targetDeviceId);
            if (success) {
               Alert.alert('Success', 'EEPROM written successfully. Device should reboot momentarily.');
               onClose();
            } else {
               Alert.alert('Failed', 'Failed to dispatch write command.');
            }
          }
        }
      ]
    );
  };

  return (
    <Modal visible={visible} animationType="slide" presentationStyle="pageSheet" onRequestClose={onClose}>
      <View style={{ flex: 1, backgroundColor: isDark ? '#121212' : '#F5F5F5' }}>
        {/* Header */}
        <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', padding: Spacing.md, backgroundColor: isDark ? '#1E1E1E' : '#FFF', borderBottomWidth: 1, borderBottomColor: isDark ? '#333' : '#E0E0E0' }}>
          <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}>
            <MaterialCommunityIcons name="chip" size={24} color={Colors.primary} />
            <View>
              <Text style={{ fontSize: 18, fontWeight: 'bold', color: Colors.text }}>Advanced Settings</Text>
              <Text style={{ fontSize: 12, color: Colors.textMuted }}>{targetDeviceId || 'No Device Selected'}</Text>
            </View>
          </View>
          <TouchableOpacity onPress={onClose} style={{ padding: Spacing.sm }}>
            <MaterialCommunityIcons name="close" size={24} color={Colors.text} />
          </TouchableOpacity>
        </View>

        <ScrollView contentContainerStyle={{ padding: Spacing.md, gap: Spacing.lg }}>
          {/* LED Dimensions */}
          <View style={{ backgroundColor: isDark ? '#1E1E1E' : '#FFF', padding: Spacing.md, borderRadius: 12 }}>
            <Text style={{ color: Colors.text, fontWeight: 'bold', marginBottom: Spacing.sm }}>Matrix Dimensions</Text>
            
            <View style={{ marginBottom: Spacing.md }}>
              <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginBottom: Spacing.xs }}>
                <Text style={{ color: Colors.textMuted }}>LED Points (Per Segment)</Text>
                <Text style={{ color: Colors.primary, fontWeight: 'bold' }}>{points}</Text>
              </View>
              <CustomSlider 
                minimumValue={1} maximumValue={300} step={1}
                value={points} onValueChange={setPoints} 
              />
            </View>

            <View>
              <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginBottom: Spacing.xs }}>
                <Text style={{ color: Colors.textMuted }}>Segments (Chains)</Text>
                <Text style={{ color: Colors.primary, fontWeight: 'bold' }}>{segments}</Text>
              </View>
              <CustomSlider 
                minimumValue={1} maximumValue={100} step={1}
                value={segments} onValueChange={setSegments} 
              />
            </View>

            <Text style={{ fontSize: 11, color: Colors.textMuted, marginTop: Spacing.sm, fontStyle: 'italic' }}>
              Max: 300 Points. Total Virtual Pixels (Pts x Seg) must not exceed 2048.
            </Text>
          </View>

          {/* IC Type */}
          <View style={{ backgroundColor: isDark ? '#1E1E1E' : '#FFF', padding: Spacing.md, borderRadius: 12 }}>
            <Text style={{ color: Colors.text, fontWeight: 'bold', marginBottom: Spacing.sm }}>Protocol (IC Type)</Text>
            <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.xs }}>
              {IC_TYPE_NAMES.map((name) => (
                <TouchableOpacity 
                  key={name}
                  onPress={() => setIcType(name)}
                  style={{
                    paddingHorizontal: Spacing.md, paddingVertical: Spacing.sm,
                    borderRadius: 8, borderWidth: 1,
                    borderColor: icType === name ? Colors.primary : (isDark ? '#333' : '#E0E0E0'),
                    backgroundColor: icType === name ? (isDark ? 'rgba(0,255,255,0.1)' : '#E0F7FA') : 'transparent',
                  }}
                >
                  <Text style={{ color: icType === name ? Colors.primary : Colors.text, fontSize: 13, fontWeight: icType === name ? 'bold' : 'normal' }}>{name}</Text>
                </TouchableOpacity>
              ))}
            </View>
          </View>

          {/* Color Sorting */}
          <View style={{ backgroundColor: isDark ? '#1E1E1E' : '#FFF', padding: Spacing.md, borderRadius: 12 }}>
            <Text style={{ color: Colors.text, fontWeight: 'bold', marginBottom: Spacing.sm }}>Color Wiring Sequence</Text>
            <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.xs }}>
              {Object.values(COLOR_SORTING_RGB).map((sort) => (
                <TouchableOpacity 
                  key={sort}
                  onPress={() => setSorting(sort)}
                  style={{
                    paddingHorizontal: Spacing.md, paddingVertical: Spacing.sm,
                    borderRadius: 8, borderWidth: 1,
                    borderColor: sorting === sort ? Colors.primary : (isDark ? '#333' : '#E0E0E0'),
                    backgroundColor: sorting === sort ? (isDark ? 'rgba(0,255,255,0.1)' : '#E0F7FA') : 'transparent',
                  }}
                >
                  <Text style={{ color: sorting === sort ? Colors.primary : Colors.text, fontSize: 13, fontWeight: sorting === sort ? 'bold' : 'normal' }}>{sort}</Text>
                </TouchableOpacity>
              ))}
            </View>
            <Text style={{ fontSize: 11, color: '#FF9800', marginTop: Spacing.sm, fontStyle: 'italic' }}>
              Warning: Incorrect color sequence will swap R/G/B colors physically.
            </Text>
          </View>

        </ScrollView>

        <View style={{ padding: Spacing.md, borderTopWidth: 1, borderTopColor: isDark ? '#333' : '#E0E0E0', backgroundColor: isDark ? '#1E1E1E' : '#FFF' }}>
           <TouchableOpacity 
              onPress={handleWriteEEPROM}
              disabled={!targetDeviceId}
              style={{
                 backgroundColor: targetDeviceId ? '#FF4444' : Colors.surfaceHighlight,
                 paddingVertical: Spacing.md,
                 borderRadius: 8,
                 alignItems: 'center',
                 flexDirection: 'row',
                 justifyContent: 'center',
                 gap: Spacing.sm
              }}
           >
              <MaterialCommunityIcons name="flash-alert" size={20} color="#FFF" />
              <Text style={{ color: '#FFF', fontWeight: 'bold', fontSize: 16 }}>FLASH EEPROM</Text>
           </TouchableOpacity>
        </View>

      </View>
    </Modal>
  );
};
