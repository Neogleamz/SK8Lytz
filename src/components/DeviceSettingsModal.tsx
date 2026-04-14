import React, { useEffect, useState } from 'react';
import { Alert, Modal, ScrollView, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { AppLogger } from '../services/AppLogger';
import { Colors, Spacing, Typography } from '../theme/theme';
import { getDefaultGroupName } from '../utils/NamingUtils';

interface DeviceSettings {
  name: string;
  type: string;
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
  writeToDevice?: (payload: number[]) => Promise<void | boolean>;
}

// Derives device name + group name from type + position
const deriveNames = (type: string, position: 'Left' | 'Right' | null) => {
  const deviceName = position ? `${type} ${position}` : type;
  const groupName = getDefaultGroupName(type);
  return { deviceName, groupName };
};

export default function DeviceSettingsModal({ isVisible, onClose, onSave, initialSettings, groups, writeToDevice }: DeviceSettingsModalProps) {
  const insets = useSafeAreaInsets();
  const [type, setTypeState] = useState<string>(initialSettings.type || 'SOULZ');
  const [position, setPosition] = useState<'Left' | 'Right' | null>(
    initialSettings.name?.includes('Left') ? 'Left' : initialSettings.name?.includes('Right') ? 'Right' : null
  );
  const [customName, setCustomName] = useState<string | null>(null); // null = use auto-name
  const [pointsText, setPointsText] = useState(initialSettings.points?.toString() || '43');
  const [segmentsText, setSegmentsText] = useState(initialSettings.segments?.toString() || '1');
  const [stripType, setStripType] = useState(initialSettings.stripType || 'WS2812B');
  const [sorting, setSorting] = useState(initialSettings.sorting || 'GRB');
  const [rfMode, setRfMode] = useState<'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED'>(
    (initialSettings as any).rfMode || 'ALLOW_PAIRED'
  );

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
      setRfMode((initialSettings as any).rfMode || 'ALLOW_PAIRED');
    }
  }, [isVisible]);

  const handleTypeChange = (t: string) => {
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
      writeToDevice(ZenggeProtocol.writeHardwareSettingsByName(finalPoints, finalSegments, stripType, sorting));
    }

    onClose();
  };

  const handleSetRfMode = (mode: 'ALLOW_ALL' | 'ALLOW_NONE' | 'ALLOW_PAIRED') => {
    setRfMode(mode);
    if (writeToDevice) {
      writeToDevice(ZenggeProtocol.setRfRemoteState(mode, false));
    }
  };

  const handleClearRfRemotes = () => {
    Alert.alert(
      'Clear Paired Remotes',
      'This will unpair ALL RF remotes from this device. The remote must be physically re-paired after this. Continue?',
      [
        { text: 'Cancel', style: 'cancel' },
        {
          text: 'Clear Remotes', style: 'destructive',
          onPress: () => {
            if (writeToDevice) {
              writeToDevice(ZenggeProtocol.clearRfRemotes(rfMode));
            }
          },
        },
      ]
    );
  };

  const handleQueryRfState = () => {
    if (writeToDevice) {
      writeToDevice(ZenggeProtocol.queryRfRemoteState());
    }
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
                {LOCAL_PRODUCT_CATALOG.map(t => (
                  <TouchableOpacity
                    key={t.id}
                    style={[styles.groupButton, type === t.id && styles.groupButtonActive, {marginHorizontal: Spacing.xs}]}
                    onPress={() => handleTypeChange(t.id)}
                  >
                    <Text style={[styles.groupButtonText, type === t.id && styles.groupButtonTextActive]}>{t.id}</Text>
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
            <View style={[styles.inputGroup, { backgroundColor: 'rgba(0,240,255,0.04)', borderRadius: 10, padding: Spacing.md }]}>
              <Text style={styles.label}>Device Name</Text>
              <Text style={{ color: Colors.primary, fontWeight: 'bold', fontSize: 16, marginBottom: Spacing.xs }}>
                {finalName}
              </Text>
              <Text style={styles.label}>Auto Group</Text>
              <Text style={{ color: Colors.secondary, fontWeight: 'bold', fontSize: 14, marginBottom: Spacing.sm }}>
                {autoGroupName}
              </Text>
              {/* Optional manual override */}
              <TextInput
                style={[styles.input, { marginTop: Spacing.xs, fontSize: 13 }]}
                value={customName ?? ''}
                onChangeText={t => setCustomName(t.length > 0 ? t : null)}
                placeholder={`Override name (default: ${autoName})`}
                placeholderTextColor={Colors.textMuted}
              />
            </View>



            {/* POINTS + SEGMENTS */}
            <View style={styles.row}>
              <View style={[styles.inputGroup, { flex: 1, marginRight: Spacing.sm }]}>
                <Text style={styles.label}>LED Points</Text>
                <TextInput style={styles.input} value={pointsText} onChangeText={setPointsText} keyboardType="numeric" placeholder="43" placeholderTextColor="#444" />
              </View>
              <View style={[styles.inputGroup, { flex: 1, marginLeft: Spacing.sm }]}>
                <Text style={styles.label}>Segments</Text>
                <TextInput style={styles.input} value={segmentsText} onChangeText={setSegmentsText} keyboardType="numeric" placeholder="1" placeholderTextColor="#444" />
              </View>
            </View>

            {/* STRIP TYPE */}
            <View style={styles.inputGroup}>
              <Text style={styles.label}>LED Strip Type</Text>
              <ScrollView horizontal showsHorizontalScrollIndicator={false} style={{ marginTop: Spacing.sm }}>
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

            {/* RF REMOTE CONTROL */}
            <View style={[styles.inputGroup, { marginTop: Spacing.sm }]}>
              <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: Spacing.md }}>
                <Text style={styles.label}>RF Remote Control</Text>
                <TouchableOpacity
                  onPress={handleQueryRfState}
                  style={{ paddingHorizontal: Spacing.md, paddingVertical: Spacing.xs, borderRadius: 8, borderWidth: 1, borderColor: 'rgba(0,240,255,0.3)', backgroundColor: 'rgba(0,240,255,0.06)' }}
                >
                  <Text style={{ color: '#00f0ff', fontSize: 10, fontWeight: 'bold' }}>QUERY STATE</Text>
                </TouchableOpacity>
              </View>

              {/* Auth mode selector */}
              <View style={{ gap: Spacing.sm, marginBottom: Spacing.md }}>
                {([
                  { key: 'ALLOW_PAIRED', label: '🔒 Paired Remote Only', desc: 'Only the exclusively paired remote works', color: '#00e887' },
                  { key: 'ALLOW_ALL',    label: '🌐 Allow All Remotes',   desc: 'Any RF remote in range can control device', color: '#FFA500' },
                  { key: 'ALLOW_NONE',   label: '🚫 Block All Remotes',   desc: 'RF remote input fully disabled', color: '#FF3D71' },
                ] as const).map(({ key, label, desc, color }) => (
                  <TouchableOpacity
                    key={key}
                    onPress={() => handleSetRfMode(key)}
                    style={[
                      styles.rfModeBtn,
                      rfMode === key && { borderColor: color, backgroundColor: `${color}14` },
                    ]}
                    activeOpacity={0.75}
                  >
                    <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}>
                      {rfMode === key && (
                        <View style={{ width: 8, height: 8, borderRadius: 4, backgroundColor: color,
                          shadowColor: color, shadowOpacity: 1, shadowRadius: 6, elevation: 4 }} />
                      )}
                      <View style={{ flex: 1 }}>
                        <Text style={{ color: rfMode === key ? color : Colors.textMuted, fontWeight: '800', fontSize: 13 }}>{label}</Text>
                        <Text style={{ color: Colors.textMuted, fontSize: 11, marginTop: Spacing.xxs }}>{desc}</Text>
                      </View>
                      {rfMode === key && <Text style={{ color: color, fontSize: 11, fontWeight: 'bold' }}>ACTIVE ✓</Text>}
                    </View>
                  </TouchableOpacity>
                ))}
              </View>

              {/* Clear paired remotes — destructive action */}
              <TouchableOpacity
                onPress={handleClearRfRemotes}
                style={styles.clearRemotesBtn}
                activeOpacity={0.75}
              >
                <Text style={{ color: '#FF3D71', fontWeight: '800', fontSize: 13 }}>⚡ Clear Paired Remotes</Text>
                <Text style={{ color: Colors.textMuted, fontSize: 10, marginTop: Spacing.xxs }}>Unlinks all paired RF remotes. Re-pair via power-cycle.</Text>
              </TouchableOpacity>
            </View>

            {/* FIRMWARE (read-only) */}
            <View style={[styles.inputGroup, { marginTop: Spacing.xs }]}>
              <Text style={styles.label}>Firmware Version</Text>
              <TextInput
                style={[styles.input, { color: Colors.textMuted, backgroundColor: 'rgba(255,255,255,0.02)' }]}
                value={initialSettings.firmware || 'Unknown'}
                editable={false}
              />
            </View>

          </ScrollView>

          <View style={[styles.footer, { paddingBottom: insets.bottom }]}>
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
  content: { backgroundColor: Colors.surface, borderTopLeftRadius: 24, borderTopRightRadius: 24, padding: Spacing.xl, maxHeight: '88%', borderWidth: 1, borderColor: Colors.surfaceHighlight },
  header: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: Spacing.xl },
  form: { marginBottom: Spacing.lg },
  inputGroup: { marginBottom: Spacing.lg },
  label: { ...Typography.caption, color: Colors.textMuted, marginBottom: Spacing.sm, textTransform: 'uppercase', letterSpacing: 1 },
  input: { backgroundColor: Colors.background, borderRadius: 8, padding: Spacing.md, color: Colors.text, borderWidth: 1, borderColor: Colors.surfaceHighlight, fontSize: 15 },
  row: { flexDirection: 'row' },
  buttonGroup: { flexDirection: 'row', gap: Spacing.sm, flexWrap: 'wrap' },
  groupButton: { flex: 1, paddingVertical: Spacing.md, alignItems: 'center', borderRadius: 8, backgroundColor: Colors.background, borderWidth: 1, borderColor: Colors.surfaceHighlight },
  groupButtonActive: { backgroundColor: Colors.primary, borderColor: Colors.primary },
  groupButtonText: { color: Colors.textMuted, fontWeight: 'bold' },
  groupButtonTextActive: { color: '#000' },
  pill: { paddingHorizontal: Spacing.lg, paddingVertical: Spacing.sm, borderRadius: 20, backgroundColor: Colors.background, marginRight: Spacing.sm, borderWidth: 1, borderColor: Colors.surfaceHighlight },
  pillActive: { backgroundColor: Colors.secondary, borderColor: Colors.secondary },
  pillText: { color: Colors.textMuted, fontSize: 12, fontWeight: '600' },
  pillTextActive: { color: Colors.background, fontWeight: 'bold' },
  miniPill: { paddingHorizontal: Spacing.md, paddingVertical: Spacing.sm, borderRadius: 4, backgroundColor: Colors.background, borderWidth: 1, borderColor: Colors.surfaceHighlight },
  miniPillActive: { backgroundColor: Colors.secondary, borderColor: Colors.secondary },
  miniPillText: { color: Colors.textMuted, fontSize: 11, fontWeight: 'bold' },
  miniPillTextActive: { color: Colors.background },
  footer: { flexDirection: 'row', gap: Spacing.md, marginBottom: 0, paddingBottom: 0 },
  cancelButton: { flex: 1, paddingVertical: Spacing.lg, alignItems: 'center', borderRadius: 12, backgroundColor: Colors.surfaceHighlight },
  cancelButtonText: { color: Colors.textMuted, fontWeight: 'bold' },
  saveButton: { flex: 2, paddingVertical: Spacing.lg, alignItems: 'center', borderRadius: 12, backgroundColor: Colors.primary },
  saveButtonText: { color: '#000', fontWeight: 'bold' },
  rfModeBtn: {
    padding: Spacing.md, borderRadius: 10, borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
    backgroundColor: 'rgba(255,255,255,0.02)',
  },
  clearRemotesBtn: {
    padding: Spacing.md, borderRadius: 10, borderWidth: 1,
    borderColor: 'rgba(255,61,113,0.35)',
    backgroundColor: 'rgba(255,61,113,0.06)',
  },
});
