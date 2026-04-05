import React, { useState, useEffect } from 'react';
import { View, Text, StyleSheet, Modal, TouchableOpacity, TextInput, ScrollView, Platform } from 'react-native';
import { Colors, Typography, Layout } from '../theme/theme';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { AppLogger } from '../services/AppLogger';

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
  firmware?: string;
}

interface DeviceSettingsModalProps {
  isVisible: boolean;
  onClose: () => void;
  onSave: (settings: DeviceSettings) => void;
  initialSettings: DeviceSettings;
  groups?: any[];
  writeToDevice?: (payload: number[]) => Promise<void>;
}

// Derives device name + group name from type + position
const deriveNames = (type: 'HALOZ' | 'SOULZ', position: 'Left' | 'Right' | null) => {
  const deviceName = position ? `${type} ${position}` : type;
  const groupName = `My SK8Lytz ${type}`;
  return { deviceName, groupName };
};

export default function DeviceSettingsModal({ isVisible, onClose, onSave, initialSettings, groups, writeToDevice }: DeviceSettingsModalProps) {
  const [type, setTypeState] = useState<'HALOZ' | 'SOULZ'>(initialSettings.type || 'SOULZ');
  const [position, setPosition] = useState<'Left' | 'Right' | null>(
    initialSettings.name?.includes('Left') ? 'Left' : initialSettings.name?.includes('Right') ? 'Right' : null
  );
  const [customName, setCustomName] = useState<string | null>(null); // null = use auto-name
  const [pointsText, setPointsText] = useState(initialSettings.points?.toString() || '43');
  const [segmentsText, setSegmentsText] = useState(initialSettings.segments?.toString() || '1');
  const [stripType, setStripType] = useState(initialSettings.stripType || 'WS2812B');
  const [sorting, setSorting] = useState(initialSettings.sorting || 'GRB');

  // Derived values
  const { deviceName: autoName, groupName: autoGroupName } = deriveNames(type, position);
  const finalName = customName ?? autoName;

  // Sync live hardware values (points/segments/strip/sort) when device responds
  useEffect(() => {
    if (!isVisible) return;
    const hasChange =
      initialSettings.points?.toString() !== pointsText ||
      initialSettings.segments?.toString() !== segmentsText ||
      initialSettings.sorting !== sorting ||
      initialSettings.stripType !== stripType;

    if (hasChange) {
      setPointsText(initialSettings.points?.toString() || pointsText);
      setSegmentsText(initialSettings.segments?.toString() || segmentsText);
      if (initialSettings.sorting) setSorting(initialSettings.sorting);
      if (initialSettings.stripType) setStripType(initialSettings.stripType);
    }
  }, [initialSettings.points, initialSettings.segments, initialSettings.sorting, initialSettings.stripType]);

  // Full reset when modal opens
  useEffect(() => {
    if (isVisible) {
      AppLogger.log('SCREEN_OPENED', { screenName: 'Device Settings' });
      setTypeState(initialSettings.type || 'SOULZ');
      setPosition(initialSettings.name?.includes('Left') ? 'Left' : initialSettings.name?.includes('Right') ? 'Right' : null);
      setCustomName(null); // reset to auto-name
      setPointsText(initialSettings.points?.toString() || '43');
      setSegmentsText(initialSettings.segments?.toString() || '1');
      setStripType(initialSettings.stripType || 'WS2812B');
      setSorting(initialSettings.sorting || 'GRB');
    }
  }, [isVisible]);

  const handleTypeChange = (t: 'HALOZ' | 'SOULZ') => {
    setTypeState(t);
    setCustomName(null); // reset to auto-name whenever type changes
  };

  const handlePositionChange = (p: 'Left' | 'Right') => {
    setPosition(prev => prev === p ? null : p); // toggle off if already selected
    setCustomName(null);
  };

  const handleSave = () => {
    const finalPoints = parseInt(pointsText) || initialSettings.points || 43;
    const finalSegments = parseInt(segmentsText) || initialSettings.segments || 1;

    // Find existing group by auto-name, or flag for creation
    const existingGroup = groups?.find(g => g.name === autoGroupName);

    const finalSettings: DeviceSettings = {
      name: finalName,
      type,
      points: finalPoints,
      segments: finalSegments,
      stripType,
      sorting,
      grouped: true,
      groupId: existingGroup?.id,    // undefined = will be created by saveSettings
      groupName: autoGroupName,
      firmware: initialSettings.firmware,
    };

    onSave(finalSettings);

    if (writeToDevice) {
      writeToDevice(ZenggeProtocol.setHardwareConfig(finalPoints, sorting, stripType, finalSegments));
    }

    onClose();
  };



  return (
    <Modal visible={isVisible} animationType="slide" transparent onRequestClose={onClose}>
      <View style={styles.overlay}>
        <View style={styles.content}>
          <View style={styles.header}>
            <Text style={Typography.title}>Hardware Settings</Text>
            <TouchableOpacity onPress={onClose}>
              <Text style={{ color: Colors.textMuted, fontSize: 18 }}>✕</Text>
            </TouchableOpacity>
          </View>

          <ScrollView style={styles.form} showsVerticalScrollIndicator={false}>

            {/* PRODUCT TYPE */}
            <View style={styles.inputGroup}>
              <Text style={styles.label}>Product Type</Text>
              <View style={styles.buttonGroup}>
                {(['HALOZ', 'SOULZ'] as const).map(t => (
                  <TouchableOpacity
                    key={t}
                    style={[styles.groupButton, type === t && styles.groupButtonActive]}
                    onPress={() => handleTypeChange(t)}
                  >
                    <Text style={[styles.groupButtonText, type === t && styles.groupButtonTextActive]}>{t}</Text>
                  </TouchableOpacity>
                ))}
              </View>
            </View>

            {/* POSITION */}
            <View style={styles.inputGroup}>
              <Text style={styles.label}>Skate Position</Text>
              <View style={styles.buttonGroup}>
                {(['Left', 'Right'] as const).map(p => (
                  <TouchableOpacity
                    key={p}
                    style={[styles.groupButton, position === p && styles.groupButtonActive]}
                    onPress={() => handlePositionChange(p)}
                  >
                    <Text style={[styles.groupButtonText, position === p && styles.groupButtonTextActive]}>
                      {p} Skate
                    </Text>
                  </TouchableOpacity>
                ))}
              </View>
            </View>

            {/* AUTO-GENERATED NAME + GROUP PREVIEW */}
            <View style={[styles.inputGroup, { backgroundColor: 'rgba(0,240,255,0.04)', borderRadius: 10, padding: 12 }]}>
              <Text style={styles.label}>Device Name</Text>
              <Text style={{ color: Colors.primary, fontWeight: 'bold', fontSize: 16, marginBottom: 4 }}>
                {finalName}
              </Text>
              <Text style={styles.label}>Auto Group</Text>
              <Text style={{ color: Colors.secondary, fontWeight: 'bold', fontSize: 14, marginBottom: 8 }}>
                {autoGroupName}
              </Text>
              {/* Optional manual override */}
              <TextInput
                style={[styles.input, { marginTop: 4, fontSize: 13 }]}
                value={customName ?? ''}
                onChangeText={t => setCustomName(t.length > 0 ? t : null)}
                placeholder={`Override name (default: ${autoName})`}
                placeholderTextColor={Colors.textMuted}
              />
            </View>



            {/* POINTS + SEGMENTS */}
            <View style={styles.row}>
              <View style={[styles.inputGroup, { flex: 1, marginRight: 8 }]}>
                <Text style={styles.label}>LED Points</Text>
                <TextInput style={styles.input} value={pointsText} onChangeText={setPointsText} keyboardType="numeric" placeholder="43" placeholderTextColor="#444" />
              </View>
              <View style={[styles.inputGroup, { flex: 1, marginLeft: 8 }]}>
                <Text style={styles.label}>Segments</Text>
                <TextInput style={styles.input} value={segmentsText} onChangeText={setSegmentsText} keyboardType="numeric" placeholder="1" placeholderTextColor="#444" />
              </View>
            </View>

            {/* STRIP TYPE */}
            <View style={styles.inputGroup}>
              <Text style={styles.label}>LED Strip Type</Text>
              <ScrollView horizontal showsHorizontalScrollIndicator={false} style={{ marginTop: 8 }}>
                {['WS2812B', 'WS2811', 'SM16703', 'SK6812'].map(st => (
                  <TouchableOpacity
                    key={st}
                    style={[styles.pill, stripType === st && styles.pillActive]}
                    onPress={() => setStripType(st)}
                  >
                    <Text style={[styles.pillText, stripType === st && styles.pillTextActive]}>{st}</Text>
                  </TouchableOpacity>
                ))}
              </ScrollView>
            </View>

            {/* COLOR SORTING */}
            <View style={styles.inputGroup}>
              <Text style={styles.label}>Color Sorting</Text>
              <View style={styles.buttonGroup}>
                {['RGB', 'RBG', 'GRB', 'GBR', 'BRG', 'BGR'].map(s => (
                  <TouchableOpacity
                    key={s}
                    style={[styles.miniPill, sorting === s && styles.miniPillActive]}
                    onPress={() => setSorting(s)}
                  >
                    <Text style={[styles.miniPillText, sorting === s && styles.miniPillTextActive]}>{s}</Text>
                  </TouchableOpacity>
                ))}
              </View>
            </View>

            {/* FIRMWARE (read-only) */}
            <View style={[styles.inputGroup, { marginTop: 4 }]}>
              <Text style={styles.label}>Firmware Version</Text>
              <TextInput
                style={[styles.input, { color: Colors.textMuted, backgroundColor: 'rgba(255,255,255,0.02)' }]}
                value={initialSettings.firmware || 'Unknown'}
                editable={false}
              />
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
  overlay: { flex: 1, backgroundColor: 'rgba(0,0,0,0.85)', justifyContent: 'flex-end' },
  content: { backgroundColor: Colors.surface, borderTopLeftRadius: 24, borderTopRightRadius: 24, padding: 24, maxHeight: '88%', borderWidth: 1, borderColor: Colors.surfaceHighlight },
  header: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 20 },
  form: { marginBottom: 16 },
  inputGroup: { marginBottom: 18 },
  label: { ...Typography.caption, color: Colors.textMuted, marginBottom: 8, textTransform: 'uppercase', letterSpacing: 1 },
  input: { backgroundColor: Colors.background, borderRadius: 8, padding: 12, color: Colors.text, borderWidth: 1, borderColor: Colors.surfaceHighlight, fontSize: 15 },
  row: { flexDirection: 'row' },
  buttonGroup: { flexDirection: 'row', gap: 8, flexWrap: 'wrap' },
  groupButton: { flex: 1, paddingVertical: 10, alignItems: 'center', borderRadius: 8, backgroundColor: Colors.background, borderWidth: 1, borderColor: Colors.surfaceHighlight },
  groupButtonActive: { backgroundColor: Colors.primary, borderColor: Colors.primary },
  groupButtonText: { color: Colors.textMuted, fontWeight: 'bold' },
  groupButtonTextActive: { color: '#000' },
  pill: { paddingHorizontal: 16, paddingVertical: 8, borderRadius: 20, backgroundColor: Colors.background, marginRight: 8, borderWidth: 1, borderColor: Colors.surfaceHighlight },
  pillActive: { backgroundColor: Colors.secondary, borderColor: Colors.secondary },
  pillText: { color: Colors.textMuted, fontSize: 12, fontWeight: '600' },
  pillTextActive: { color: Colors.background, fontWeight: 'bold' },
  miniPill: { paddingHorizontal: 12, paddingVertical: 6, borderRadius: 4, backgroundColor: Colors.background, borderWidth: 1, borderColor: Colors.surfaceHighlight },
  miniPillActive: { backgroundColor: Colors.secondary, borderColor: Colors.secondary },
  miniPillText: { color: Colors.textMuted, fontSize: 11, fontWeight: 'bold' },
  miniPillTextActive: { color: Colors.background },
  footer: { flexDirection: 'row', gap: 12, marginBottom: Platform.OS === 'ios' ? 20 : 0 },
  cancelButton: { flex: 1, paddingVertical: 14, alignItems: 'center', borderRadius: 12, backgroundColor: Colors.surfaceHighlight },
  cancelButtonText: { color: Colors.textMuted, fontWeight: 'bold' },
  saveButton: { flex: 2, paddingVertical: 14, alignItems: 'center', borderRadius: 12, backgroundColor: Colors.primary },
  saveButtonText: { color: '#000', fontWeight: 'bold' },
});
