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
import { Typography } from '../theme/theme';
import type { PendingRegistration } from '../hooks/useBLE';
import type { RegisteredDevice } from '../hooks/useRegistration';

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
  const haloz = devices.filter(d => d.product_type === 'HALOZ');
  const soulz = devices.filter(d => d.product_type === 'SOULZ');

  const renderSignalBars = (rssi: number) => {
    const bars = rssiToSignalBars(rssi);
    return (
      <View style={{ flexDirection: 'row', alignItems: 'flex-end', gap: 2, marginLeft: 6 }}>
        {[1, 2, 3, 4].map(i => (
          <View key={i} style={{
            width: 4,
            height: 4 + i * 3,
            borderRadius: 1,
            backgroundColor: i <= bars ? '#00e887' : 'rgba(255,255,255,0.15)',
          }} />
        ))}
        <Text style={{ color: Colors.textMuted, fontSize: 9, marginLeft: 4 }}>{rssi}dBm</Text>
      </View>
    );
  };

  // ── Step 1: Welcome ──────────────────────────────────────────────────────────
  const renderStep1 = () => (
    <View style={styles.stepContainer}>
      <View style={{ alignItems: 'center', marginBottom: 24 }}>
        <Text style={{ fontSize: 48, marginBottom: 8 }}>🛼</Text>
        <Text style={[Typography.title, { color: '#00f0ff', fontSize: 22, textAlign: 'center' }]}>
          SK8Lytz Detected!
        </Text>
        <Text style={{ color: Colors.textMuted, textAlign: 'center', marginTop: 8, lineHeight: 20 }}>
          We found {devices.length} controller{devices.length !== 1 ? 's' : ''} nearby.{'\n'}
          Let's register them to your account.
        </Text>
      </View>

      {/* Device type summary */}
      {haloz.length > 0 && (
        <View style={styles.summaryCard}>
          <Text style={{ fontSize: 24, marginRight: 10 }}>💿</Text>
          <View>
            <Text style={{ color: '#00f0ff', fontWeight: 'bold', fontSize: 14 }}>HALOZ × {haloz.length}</Text>
            <Text style={{ color: Colors.textMuted, fontSize: 11 }}>Halo ring LED controllers</Text>
          </View>
        </View>
      )}
      {soulz.length > 0 && (
        <View style={styles.summaryCard}>
          <Text style={{ fontSize: 24, marginRight: 10 }}>⚡</Text>
          <View>
            <Text style={{ color: '#a855f7', fontWeight: 'bold', fontSize: 14 }}>SOULZ × {soulz.length}</Text>
            <Text style={{ color: Colors.textMuted, fontSize: 11 }}>Blade LED controllers</Text>
          </View>
        </View>
      )}

      <Text style={{ color: Colors.textMuted, fontSize: 11, textAlign: 'center', marginTop: 16 }}>
        Pro tip: make sure only YOUR skates are powered on nearby for best auto-assignment accuracy.
      </Text>

      <TouchableOpacity style={styles.primaryBtn} onPress={() => animateStep(2)}>
        <Text style={styles.primaryBtnText}>SET UP DEVICES →</Text>
      </TouchableOpacity>
    </View>
  );

  // ── Step 2: Review & Edit ────────────────────────────────────────────────────
  const renderDeviceEditor = (d: PendingRegistration, pairMac?: string) => {
    const accentColor = d.product_type === 'HALOZ' ? '#00f0ff' : '#a855f7';
    return (
      <View key={d.device_mac} style={[styles.deviceCard, { borderColor: `${accentColor}40` }]}>
        <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 12 }}>
          <View style={[styles.typeBadge, { backgroundColor: `${accentColor}20`, borderColor: accentColor }]}>
            <Text style={{ color: accentColor, fontWeight: 'bold', fontSize: 11 }}>{d.product_type}</Text>
          </View>
          <View style={{ flexDirection: 'row', alignItems: 'center' }}>
            <Text style={{ color: Colors.textMuted, fontSize: 11, marginLeft: 8 }}>
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
              <Text style={{ color: accentColor, fontSize: 10, fontWeight: 'bold', marginLeft: 3 }}>SWAP L/R</Text>
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
      <Text style={[Typography.title, { color: '#fff', marginBottom: 4 }]}>Review & Customize</Text>
      <Text style={{ color: Colors.textMuted, fontSize: 12, marginBottom: 20 }}>
        Tap any field to edit. Update group names to change how devices are grouped.
      </Text>

      {/* HALOZ group */}
      {haloz.length > 0 && (
        <View style={{ marginBottom: 16 }}>
          <Text style={[styles.groupHeader, { color: '#00f0ff' }]}>💿 HALOZ Controllers</Text>
          {haloz.map((d, i) =>
            renderDeviceEditor(d, haloz[i === 0 ? 1 : 0]?.device_mac)
          )}
        </View>
      )}

      {/* SOULZ group */}
      {soulz.length > 0 && (
        <View style={{ marginBottom: 16 }}>
          <Text style={[styles.groupHeader, { color: '#a855f7' }]}>⚡ SOULZ Controllers</Text>
          {soulz.map((d, i) =>
            renderDeviceEditor(d, soulz[i === 0 ? 1 : 0]?.device_mac)
          )}
        </View>
      )}

      <View style={{ flexDirection: 'row', gap: 10, marginTop: 8, paddingBottom: 24 }}>
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
      <Text style={[Typography.title, { color: '#fff', marginBottom: 4 }]}>Confirm Registration</Text>
      <Text style={{ color: Colors.textMuted, fontSize: 12, marginBottom: 20 }}>
        These devices will be registered to your account.
      </Text>

      {devices.map(d => {
        const accent = d.product_type === 'HALOZ' ? '#00f0ff' : '#a855f7';
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

      <Text style={{ color: Colors.textMuted, fontSize: 10, textAlign: 'center', marginTop: 12, marginBottom: 20 }}>
        Registration links these controllers to your SK8Lytz account for cloud sync.
        {'\n'}You can unregister at any time in Hardware Settings.
      </Text>

      <View style={{ flexDirection: 'row', gap: 10 }}>
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

      <TouchableOpacity onPress={onDismiss} style={{ marginTop: 16, alignItems: 'center' }}>
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
    padding: 24, maxHeight: '90%',
    borderWidth: 1, borderColor: 'rgba(0,240,255,0.12)',
  },
  progressDots: { flexDirection: 'row', justifyContent: 'center', gap: 8, marginBottom: 20 },
  dot: { width: 8, height: 8, borderRadius: 4, backgroundColor: 'rgba(255,255,255,0.15)' },
  dotActive: { backgroundColor: '#00f0ff', width: 20 },
  stepContainer: { paddingBottom: 8 },
  summaryCard: {
    flexDirection: 'row', alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderRadius: 12, padding: 14, marginBottom: 10,
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)',
  },
  deviceCard: {
    backgroundColor: 'rgba(255,255,255,0.03)',
    borderRadius: 14, padding: 16, marginBottom: 12,
    borderWidth: 1,
  },
  typeBadge: {
    paddingHorizontal: 8, paddingVertical: 3,
    borderRadius: 6, borderWidth: 1,
  },
  positionBadge: {
    paddingHorizontal: 10, paddingVertical: 6,
    borderRadius: 8, alignSelf: 'flex-start', marginTop: 10,
  },
  swapBtn: {
    flexDirection: 'row', alignItems: 'center',
    marginLeft: 'auto', paddingHorizontal: 10, paddingVertical: 4,
    borderRadius: 8, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)',
    backgroundColor: 'rgba(255,255,255,0.04)',
  },
  fieldLabel: {
    color: 'rgba(255,255,255,0.35)',
    fontSize: 9, fontWeight: 'bold', letterSpacing: 1.2,
    textTransform: 'uppercase', marginBottom: 4, marginTop: 10,
  },
  input: {
    backgroundColor: 'rgba(255,255,255,0.05)',
    borderRadius: 8, borderWidth: 1,
    padding: 10, color: '#fff', fontSize: 14,
  },
  groupHeader: {
    fontWeight: 'bold', fontSize: 13,
    letterSpacing: 0.5, marginBottom: 10,
  },
  confirmRow: {
    flexDirection: 'row', alignItems: 'center',
    padding: 12, marginBottom: 8,
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderRadius: 10, borderLeftWidth: 3,
  },
  primaryBtn: {
    backgroundColor: '#00f0ff',
    borderRadius: 12, paddingVertical: 14,
    alignItems: 'center', justifyContent: 'center',
    marginTop: 4,
  },
  primaryBtnText: { color: '#000', fontWeight: 'bold', fontSize: 14 },
  secondaryBtn: {
    paddingVertical: 14, paddingHorizontal: 16,
    borderRadius: 12, alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.06)',
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.12)',
  },
  secondaryBtnText: { color: 'rgba(255,255,255,0.7)', fontWeight: 'bold', fontSize: 13 },
});
