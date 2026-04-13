/**
 * FirstTimeSetupModal.tsx — SK8Lytz First-Time Device Registration Wizard
 *
 * Auto-shown after first login probe when:
 *   - pendingRegistrations.length > 0 (probe found classifiable devices)
 *   - registeredDevices.length === 0 (no prior cloud registrations)
 *
 * 3-step wizard:
 *   Step 1: Welcome + device discovery summary
 *   Step 2: Review/edit device names, group names, swap Left/Right
 *   Step 3: Confirm + "SAVE & REGISTER DEVICES"
 */

import React, { useState, useRef, useEffect } from 'react';
import {
  View, Text, StyleSheet, Modal, TouchableOpacity, ScrollView,
  TextInput, Animated, Platform, ActivityIndicator, KeyboardAvoidingView,
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../context/ThemeContext';
import { Typography, Spacing } from '../theme/theme';
import type { PendingRegistration } from '../hooks/useBLE';
import type { RegisteredDevice } from '../hooks/useRegistration';
import { LOCAL_PRODUCT_CATALOG, getLocalProfileById } from '../constants/ProductCatalog';

interface FirstTimeSetupModalProps {
  visible: boolean;
  pendingRegistrations: PendingRegistration[];
  onComplete: (devices: RegisteredDevice[]) => Promise<void>;
  onDismiss: () => void;
}

type Step = 1 | 2 | 3;

export default function FirstTimeSetupModal({
  visible,
  pendingRegistrations,
  onComplete,
  onDismiss,
}: FirstTimeSetupModalProps) {
  const { Colors } = useTheme();
  const insets = useSafeAreaInsets();
  const [step, setStep] = useState<Step>(1);
  const [devices, setDevices] = useState<PendingRegistration[]>([]);
  const [isSaving, setIsSaving] = useState(false);
  const slideAnim = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    if (visible) {
      setDevices(pendingRegistrations.map(d => ({ ...d })));
      setStep(1);
    }
  }, [visible, pendingRegistrations]);

  const animateStep = (toStep: Step) => {
    Animated.timing(slideAnim, {
      toValue: 1,
      duration: 180,
      useNativeDriver: true,
    }).start(() => {
      setStep(toStep);
      slideAnim.setValue(0);
    });
  };

  const updateDevice = (mac: string, update: Partial<PendingRegistration>) => {
    setDevices(prev => prev.map(d => d.device_mac === mac ? { ...d, ...update } : d));
  };

  /** Swap Left/Right between two devices of the same product type */
  const swapPositions = (mac1: string, mac2: string) => {
    setDevices(prev => {
      const next = [...prev];
      const i1 = next.findIndex(d => d.device_mac === mac1);
      const i2 = next.findIndex(d => d.device_mac === mac2);
      if (i1 < 0 || i2 < 0) return prev;
      const tmp = next[i1].position;
      next[i1] = { ...next[i1], position: next[i2].position };
      next[i2] = { ...next[i2], position: tmp };
      // Rebuild names
      next[i1].device_name = `${next[i1].product_type} ${next[i1].position}`;
      next[i2].device_name = `${next[i2].product_type} ${next[i2].position}`;
      return next;
    });
  };

  const handleSaveAndRegister = async () => {
    setIsSaving(true);
    const toRegister: RegisteredDevice[] = devices.map(d => ({
      device_mac:    d.device_mac,
      device_name:   d.device_name,
      product_type:  d.product_type,
      position:      d.position,
      group_name:    d.group_name,
      group_id:      d.group_name.replace(/\s+/g, '-').toLowerCase(),
      led_points:    d.led_points,
      segments:      d.segments,
      ic_type:       d.ic_type,
      color_sorting: d.color_sorting,
      rssi_at_register: d.rssi,
      firmware_ver:  d.firmware_ver,
      led_version:   d.led_version,
      product_id:    d.product_id,
      is_pending_sync: false,
    }));
    await onComplete(toRegister);
    setIsSaving(false);
  };

  const rssiToSignalBars = (rssi: number) => {
    if (rssi >= -50) return 4;
    if (rssi >= -65) return 3;
    if (rssi >= -75) return 2;
    return 1;
  };

  // Group devices by product type for rendering
  const deviceGroups = LOCAL_PRODUCT_CATALOG.map(profile => ({
    profile,
    items: devices.filter(d => d.product_type === profile.id)
  })).filter(g => g.items.length > 0);

  const renderSignalBars = (rssi: number) => {
    const bars = rssiToSignalBars(rssi);
    return (
      <View style={{ flexDirection: 'row', alignItems: 'flex-end', gap: Spacing.xxs, marginLeft: Spacing.sm }}>
        {[1, 2, 3, 4].map(i => (
          <View key={i} style={{
            width: 4,
            height: 4 + i * 3,
            borderRadius: 1,
            backgroundColor: i <= bars ? '#00e887' : 'rgba(255,255,255,0.15)',
          }} />
        ))}
        <Text style={{ color: Colors.textMuted, fontSize: 9, marginLeft: Spacing.xs }}>{rssi}dBm</Text>
      </View>
    );
  };

  // ── Step 1: Welcome ──────────────────────────────────────────────────────────
  const renderStep1 = () => (
    <View style={styles.stepContainer}>
      <View style={{ alignItems: 'center', marginBottom: Spacing.xl }}>
        <Text style={{ fontSize: 48, marginBottom: Spacing.sm }}>🛼</Text>
        <Text style={[Typography.title, { color: '#00f0ff', fontSize: 22, textAlign: 'center' }]}>
          SK8Lytz Detected!
        </Text>
        <Text style={{ color: Colors.textMuted, textAlign: 'center', marginTop: Spacing.sm, lineHeight: 20 }}>
          {devices.length === 0 ? "Ensure your skates are powered on!" : `We found ${devices.length} controller${devices.length !== 1 ? 's' : ''} nearby.\nLet's register them to your account.`}
        </Text>
      </View>

      {/* Device type summary */}
      {deviceGroups.map(group => (
        <View key={group.profile.id} style={styles.summaryCard}>
          <View>
            <Text style={{ color: group.profile.vizThemeColor, fontWeight: 'bold', fontSize: 14 }}>{group.profile.displayName} × {group.items.length}</Text>
            <Text style={{ color: Colors.textMuted, fontSize: 11 }}>{group.profile.vizShape} controllers</Text>
          </View>
        </View>
      ))}

      {devices.length === 0 && (
        <View style={{ alignItems: 'center', marginVertical: Spacing.xl }}>
          <ActivityIndicator size="large" color="#00f0ff" />
          <Text style={{ color: Colors.textMuted, fontSize: 13, textAlign: 'center', marginTop: Spacing.lg }}>
            Searching aggressively...
          </Text>
        </View>
      )}

      <Text style={{ color: Colors.textMuted, fontSize: 11, textAlign: 'center', marginTop: Spacing.lg }}>
        Pro tip: make sure only YOUR skates are powered on nearby for best auto-assignment accuracy.
      </Text>

      {devices.length > 0 && (
        <TouchableOpacity style={styles.primaryBtn} onPress={() => animateStep(2)}>
          <Text style={styles.primaryBtnText}>SET UP DEVICES →</Text>
        </TouchableOpacity>
      )}
    </View>
  );

  // ── Step 2: Review & Edit ────────────────────────────────────────────────────
  const renderDeviceEditor = (d: PendingRegistration, pairMac?: string) => {
    const pProfile = getLocalProfileById(d.product_type) || LOCAL_PRODUCT_CATALOG[0];
    const accentColor = pProfile.vizThemeColor || '#00f0ff';
    return (
      <View key={d.device_mac} style={[styles.deviceCard, { borderColor: `${accentColor}40` }]}>
        <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: Spacing.md }}>
          <View style={[styles.typeBadge, { backgroundColor: `${accentColor}20`, borderColor: accentColor }]}>
            <Text style={{ color: accentColor, fontWeight: 'bold', fontSize: 11 }}>{d.product_type}</Text>
          </View>
          <View style={{ flexDirection: 'row', alignItems: 'center' }}>
            <Text style={{ color: Colors.textMuted, fontSize: 11, marginLeft: Spacing.sm }}>
              {d.led_points}pts · {d.segments}seg · {d.ic_type}
            </Text>
            {renderSignalBars(d.rssi)}
          </View>
          {pairMac && (
            <TouchableOpacity
              onPress={() => swapPositions(d.device_mac, pairMac)}
              style={styles.swapBtn}
            >
              <MaterialCommunityIcons name="swap-horizontal" size={14} color={accentColor} />
              <Text style={{ color: accentColor, fontSize: 10, fontWeight: 'bold', marginLeft: Spacing.xxs }}>SWAP L/R</Text>
            </TouchableOpacity>
          )}
        </View>

        <Text style={styles.fieldLabel}>DEVICE NAME</Text>
        <TextInput
          style={[styles.input, { borderColor: `${accentColor}40` }]}
          value={d.device_name}
          onChangeText={name => updateDevice(d.device_mac, { device_name: name })}
          placeholderTextColor={Colors.textMuted}
        />

        <Text style={styles.fieldLabel}>GROUP NAME</Text>
        <TextInput
          style={[styles.input, { borderColor: `${accentColor}40` }]}
          value={d.group_name}
          onChangeText={name => {
            // Update group name on ALL devices in same product type group
            const groupDevices = devices.filter(x => x.product_type === d.product_type);
            groupDevices.forEach(x => updateDevice(x.device_mac, { group_name: name }));
          }}
          placeholderTextColor={Colors.textMuted}
        />

        <View style={[styles.positionBadge, { backgroundColor: `${accentColor}15` }]}>
          <Text style={{ color: accentColor, fontWeight: 'bold', fontSize: 12 }}>
            {d.position === 'Left' ? '◀' : '▶'} {d.position} Skate
          </Text>
        </View>
      </View>
    );
  };

  const renderStep2 = () => (
    <ScrollView showsVerticalScrollIndicator={false}>
      <Text style={[Typography.title, { color: '#fff', marginBottom: Spacing.xs }]}>Review & Customize</Text>
      <Text style={{ color: Colors.textMuted, fontSize: 12, marginBottom: Spacing.xl }}>
        Tap any field to edit. Update group names to change how devices are grouped.
      </Text>

      {/* Device Groups */}
      {deviceGroups.map(group => (
        <View key={group.profile.id} style={{ marginBottom: Spacing.lg }}>
          <Text style={[styles.groupHeader, { color: group.profile.vizThemeColor }]}>💿 {group.profile.displayName} Controllers</Text>
          {group.items.map((d, i) =>
            renderDeviceEditor(d, group.items[i === 0 ? 1 : 0]?.device_mac)
          )}
        </View>
      ))}

      <View style={{ flexDirection: 'row', gap: Spacing.md, marginTop: Spacing.sm, paddingBottom: Spacing.xl }}>
        <TouchableOpacity style={styles.secondaryBtn} onPress={() => animateStep(1)}>
          <Text style={styles.secondaryBtnText}>← BACK</Text>
        </TouchableOpacity>
        <TouchableOpacity style={[styles.primaryBtn, { flex: 1 }]} onPress={() => animateStep(3)}>
          <Text style={styles.primaryBtnText}>REVIEW →</Text>
        </TouchableOpacity>
      </View>
    </ScrollView>
  );

  // ── Step 3: Confirm ──────────────────────────────────────────────────────────
  const renderStep3 = () => (
    <View style={styles.stepContainer}>
      <Text style={[Typography.title, { color: '#fff', marginBottom: Spacing.xs }]}>Confirm Registration</Text>
      <Text style={{ color: Colors.textMuted, fontSize: 12, marginBottom: Spacing.xl }}>
        These devices will be registered to your account.
      </Text>

      {devices.map(d => {
        const pProfile = getLocalProfileById(d.product_type) || LOCAL_PRODUCT_CATALOG[0];
        const accent = pProfile.vizThemeColor || '#00f0ff';
        return (
          <View key={d.device_mac} style={[styles.confirmRow, { borderLeftColor: accent }]}>
            <View style={{ flex: 1 }}>
              <Text style={{ color: '#fff', fontWeight: 'bold' }}>{d.device_name}</Text>
              <Text style={{ color: Colors.textMuted, fontSize: 11 }}>{d.group_name}</Text>
            </View>
            <View style={[styles.typeBadge, { backgroundColor: `${accent}20`, borderColor: accent }]}>
              <Text style={{ color: accent, fontSize: 10, fontWeight: 'bold' }}>{d.product_type}</Text>
            </View>
          </View>
        );
      })}

      <Text style={{ color: Colors.textMuted, fontSize: 10, textAlign: 'center', marginTop: Spacing.md, marginBottom: Spacing.xl }}>
        Registration links these controllers to your SK8Lytz account for cloud sync.
        {'\n'}You can unregister at any time in Hardware Settings.
      </Text>

      <View style={{ flexDirection: 'row', gap: Spacing.md }}>
        <TouchableOpacity style={styles.secondaryBtn} onPress={() => animateStep(2)}>
          <Text style={styles.secondaryBtnText}>← EDIT</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[styles.primaryBtn, { flex: 1, backgroundColor: '#00e887' }]}
          onPress={handleSaveAndRegister}
          disabled={isSaving}
        >
          {isSaving
            ? <ActivityIndicator color="#000" />
            : <Text style={[styles.primaryBtnText, { color: '#000' }]}>💾 SAVE & REGISTER</Text>
          }
        </TouchableOpacity>
      </View>

      <TouchableOpacity onPress={onDismiss} style={{ marginTop: Spacing.lg, alignItems: 'center' }}>
        <Text style={{ color: Colors.textMuted, fontSize: 12 }}>Skip for now</Text>
      </TouchableOpacity>
    </View>
  );

  return (
    <Modal visible={visible} animationType="slide" transparent onRequestClose={onDismiss}>
      <KeyboardAvoidingView
        style={styles.overlay}
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        keyboardVerticalOffset={Platform.OS === 'ios' ? 0 : 24}>
        <View style={[styles.sheet, { paddingBottom: insets.bottom + 16 }]}>
          {/* Progress dots */}
          <View style={styles.progressDots}>
            {([1, 2, 3] as Step[]).map(s => (
              <View key={s} style={[styles.dot, step >= s && styles.dotActive]} />
            ))}
          </View>

          <Animated.View style={{ opacity: slideAnim.interpolate({ inputRange: [0, 1], outputRange: [1, 0] }) }}>
            {step === 1 && renderStep1()}
            {step === 2 && renderStep2()}
            {step === 3 && renderStep3()}
          </Animated.View>
        </View>
      </KeyboardAvoidingView>
    </Modal>
  );
}

const styles = StyleSheet.create({
  overlay: { flex: 1, backgroundColor: 'rgba(0,0,0,0.88)', justifyContent: 'flex-end' },
  sheet: {
    backgroundColor: '#0D0D0D',
    borderTopLeftRadius: 24, borderTopRightRadius: 24,
    padding: Spacing.xl, maxHeight: '90%',
    borderWidth: 1, borderColor: 'rgba(0,240,255,0.12)',
  },
  progressDots: { flexDirection: 'row', justifyContent: 'center', gap: Spacing.sm, marginBottom: Spacing.xl },
  dot: { width: 8, height: 8, borderRadius: 4, backgroundColor: 'rgba(255,255,255,0.15)' },
  dotActive: { backgroundColor: '#00f0ff', width: 20 },
  stepContainer: { paddingBottom: Spacing.sm },
  summaryCard: {
    flexDirection: 'row', alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderRadius: 12, padding: Spacing.lg, marginBottom: Spacing.md,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)',
  },
  deviceCard: {
    backgroundColor: 'rgba(255,255,255,0.03)',
    borderRadius: 14, padding: Spacing.lg, marginBottom: Spacing.md,
    borderWidth: 1,
  },
  typeBadge: {
    paddingHorizontal: Spacing.sm, paddingVertical: Spacing.xxs,
    borderRadius: 6, borderWidth: 1,
  },
  positionBadge: {
    paddingHorizontal: Spacing.md, paddingVertical: Spacing.sm,
    borderRadius: 8, alignSelf: 'flex-start', marginTop: Spacing.md,
  },
  swapBtn: {
    flexDirection: 'row', alignItems: 'center',
    marginLeft: 'auto', paddingHorizontal: Spacing.md, paddingVertical: Spacing.xs,
    borderRadius: 8, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)',
    backgroundColor: 'rgba(255,255,255,0.04)',
  },
  fieldLabel: {
    color: 'rgba(255,255,255,0.35)',
    fontSize: 9, fontWeight: 'bold', letterSpacing: 1.2,
    textTransform: 'uppercase', marginBottom: Spacing.xs, marginTop: Spacing.md,
  },
  input: {
    backgroundColor: 'rgba(255,255,255,0.05)',
    borderRadius: 8, borderWidth: 1,
    padding: Spacing.md, color: '#fff', fontSize: 14,
  },
  groupHeader: {
    fontWeight: 'bold', fontSize: 13,
    letterSpacing: 0.5, marginBottom: Spacing.md,
  },
  confirmRow: {
    flexDirection: 'row', alignItems: 'center',
    padding: Spacing.md, marginBottom: Spacing.sm,
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderRadius: 10, borderLeftWidth: 3,
  },
  primaryBtn: {
    backgroundColor: '#00f0ff',
    borderRadius: 12, paddingVertical: Spacing.lg,
    alignItems: 'center', justifyContent: 'center',
    marginTop: Spacing.xs,
  },
  primaryBtnText: { color: '#000', fontWeight: 'bold', fontSize: 14 },
  secondaryBtn: {
    paddingVertical: Spacing.lg, paddingHorizontal: Spacing.lg,
    borderRadius: 12, alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.06)',
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.12)',
  },
  secondaryBtnText: { color: 'rgba(255,255,255,0.7)', fontWeight: 'bold', fontSize: 13 },
});
