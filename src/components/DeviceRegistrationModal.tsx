/**
 * DeviceRegistrationModal.tsx — SK8Lytz
 *
 * Slides up when a new (unregistered) BLE device connects for the first time.
 * User assigns:
 *   - A friendly name (defaults to device BLE name)
 *   - Product type  (HALOZ / SOULZ)
 *   - Position      (Left / Right / null)
 *   - Group name    (free text)
 *
 * Saves to local AsyncStorage + Supabase registered_devices via useRegistration hook.
 */

import React, { useState, useEffect } from 'react';
import {
  View, Text, StyleSheet, Modal, TouchableOpacity, TextInput,
  ActivityIndicator, Platform, ScrollView, KeyboardAvoidingView,
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../context/ThemeContext';
import { RegisteredDevice } from '../hooks/useRegistration';

// ─── Props ───────────────────────────────────────────────────────────────────

interface DeviceRegistrationModalProps {
  /** The raw BLE device to register */
  device: {
    id: string;           // MAC address
    name: string;         // BLE advertisement name
    rssi?: number;
    /** Parsed firmware fingerprint if available */
    firmware_ver?: number;
    led_version?: number;
    product_id?: number;
    led_points?: number;
    segments?: number;
    ic_type?: string;
    color_sorting?: string;
  } | null;
  /** Existing registered group names for quick-pick */
  existingGroups?: string[];
  onDismiss: () => void;
  onRegistered: (device: RegisteredDevice) => void;
}

// ─── Component ───────────────────────────────────────────────────────────────

export default function DeviceRegistrationModal({
  device, existingGroups = [], onDismiss, onRegistered,
}: DeviceRegistrationModalProps) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const insets = useSafeAreaInsets();

  const [deviceName,   setDeviceName]   = useState('');
  const [productType,  setProductType]  = useState<'HALOZ' | 'SOULZ'>('SOULZ');
  const [position,     setPosition]     = useState<'Left' | 'Right' | null>(null);
  const [groupName,    setGroupName]    = useState('My Skates');
  const [isSaving,     setIsSaving]     = useState(false);
  const [errorMsg,     setErrorMsg]     = useState('');

  // Derive defaults from BLE advertisement name / product_id
  useEffect(() => {
    if (!device) return;
    const n = device.name || '';
    setDeviceName(n || 'My SK8Lytz Device');

    // Heuristic: HALOZ vs SOULZ from name or product_id
    if (n.toUpperCase().includes('HALO') || device.product_id === 0x33) {
      setProductType('HALOZ');
    } else {
      setProductType('SOULZ');
    }

    // Guess position from name
    if (n.toLowerCase().includes('left') || n.toLowerCase().includes(' l ')) {
      setPosition('Left');
    } else if (n.toLowerCase().includes('right') || n.toLowerCase().includes(' r ')) {
      setPosition('Right');
    }

    // Pre-fill group from existing groups
    if (existingGroups.length > 0) setGroupName(existingGroups[0]);
  }, [device]);

  if (!device) return null;

  const handleSave = async () => {
    if (!deviceName.trim()) { setErrorMsg('Enter a device name'); return; }
    if (!groupName.trim())  { setErrorMsg('Enter a group name'); return; }

    setIsSaving(true); setErrorMsg('');
    try {
      const rd: RegisteredDevice = {
        device_mac:      device.id,
        device_name:     deviceName.trim(),
        product_type:    productType,
        position:        position,
        group_name:      groupName.trim(),
        led_points:      device.led_points,
        segments:        device.segments,
        ic_type:         device.ic_type,
        color_sorting:   device.color_sorting,
        firmware_ver:    device.firmware_ver,
        led_version:     device.led_version,
        product_id:      device.product_id,
        rssi_at_register: device.rssi,
      };
      // Let DashboardScreen handle the actual save via onRegistered callback
      onRegistered(rd);
    } catch (e: any) {
      setErrorMsg(e.message || 'Failed to register device');
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <Modal visible={!!device} transparent animationType="slide" statusBarTranslucent>
      <View style={styles.overlay}>
        <KeyboardAvoidingView
          behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
          style={styles.sheet}>
          <ScrollView
            contentContainerStyle={[styles.body, { paddingBottom: insets.bottom + 24 }]}
            showsVerticalScrollIndicator={false}
            keyboardShouldPersistTaps="handled">

            {/* Header */}
            <View style={styles.handle} />
            <MaterialCommunityIcons name="bluetooth-connect" size={36} color={Colors.primary} style={{ marginBottom: 6 }} />
            <Text style={styles.title}>Register New Device</Text>
            <Text style={styles.subtitle}>
              {device.name || device.id} connected for the first time.{'\n'}
              Give it a name and assign it to your setup.
            </Text>

            {/* Name */}
            <Text style={styles.label}>DEVICE NAME</Text>
            <TextInput style={styles.input} value={deviceName} onChangeText={setDeviceName}
              placeholder="e.g. Left SOULZ" placeholderTextColor={Colors.textMuted}
              maxLength={32} autoFocus />

            {/* Product type */}
            <Text style={styles.label}>PRODUCT TYPE</Text>
            <View style={styles.segRow}>
              {(['SOULZ', 'HALOZ'] as const).map(pt => (
                <TouchableOpacity key={pt}
                  style={[styles.segBtn, productType === pt && styles.segBtnActive]}
                  onPress={() => setProductType(pt)}>
                  <MaterialCommunityIcons
                    name={pt === 'HALOZ' ? 'circle-outline' : 'roller-skate'}
                    size={16} color={productType === pt ? '#000' : Colors.primary} />
                  <Text style={[styles.segBtnText, productType === pt && { color: '#000' }]}>{pt}</Text>
                </TouchableOpacity>
              ))}
            </View>

            {/* Position */}
            <Text style={styles.label}>POSITION</Text>
            <View style={styles.segRow}>
              {([null, 'Left', 'Right'] as const).map(pos => (
                <TouchableOpacity key={String(pos)}
                  style={[styles.segBtn, position === pos && styles.segBtnActive]}
                  onPress={() => setPosition(pos)}>
                  <MaterialCommunityIcons
                    name={pos === 'Left' ? 'arrow-left' : pos === 'Right' ? 'arrow-right' : 'equal'}
                    size={16} color={position === pos ? '#000' : Colors.primary} />
                  <Text style={[styles.segBtnText, position === pos && { color: '#000' }]}>
                    {pos ?? 'None'}
                  </Text>
                </TouchableOpacity>
              ))}
            </View>

            {/* Group */}
            <Text style={styles.label}>GROUP / FLEET NAME</Text>
            {existingGroups.length > 0 && (
              <View style={styles.groupChipRow}>
                {existingGroups.slice(0, 5).map(g => (
                  <TouchableOpacity key={g}
                    style={[styles.groupChip, groupName === g && styles.groupChipActive]}
                    onPress={() => setGroupName(g)}>
                    <Text style={[styles.groupChipText, groupName === g && { color: '#000' }]}>{g}</Text>
                  </TouchableOpacity>
                ))}
              </View>
            )}
            <TextInput style={styles.input} value={groupName} onChangeText={setGroupName}
              placeholder="e.g. My Skates" placeholderTextColor={Colors.textMuted} maxLength={32} />

            {/* Hardware info summary */}
            {(device.led_points || device.ic_type) && (
              <View style={styles.hwInfo}>
                <MaterialCommunityIcons name="chip" size={13} color={Colors.textMuted} />
                <Text style={styles.hwInfoText}>
                  {[
                    device.led_points && `${device.led_points} LEDs`,
                    device.ic_type && device.ic_type,
                    device.color_sorting && device.color_sorting,
                  ].filter(Boolean).join(' · ')}
                </Text>
              </View>
            )}

            {errorMsg ? <Text style={styles.errorText}>{errorMsg}</Text> : null}

            {/* Actions */}
            <TouchableOpacity style={[styles.saveBtn, isSaving && { opacity: 0.6 }]}
              onPress={handleSave} disabled={isSaving}>
              {isSaving
                ? <ActivityIndicator color="#000" />
                : <>
                    <MaterialCommunityIcons name="check-bold" size={18} color="#000" />
                    <Text style={styles.saveBtnText}>Register Device</Text>
                  </>}
            </TouchableOpacity>

            <TouchableOpacity style={styles.skipBtn} onPress={onDismiss}>
              <Text style={styles.skipBtnText}>Skip for now</Text>
            </TouchableOpacity>

          </ScrollView>
        </KeyboardAvoidingView>
      </View>
    </Modal>
  );
}

// ─── Styles ──────────────────────────────────────────────────────────────────

function createStyles(Colors: any) {
  return StyleSheet.create({
    overlay: {
      flex: 1, justifyContent: 'flex-end',
      backgroundColor: 'rgba(0,0,0,0.7)',
    },
    sheet: {
      backgroundColor: Colors.background || '#111',
      borderTopLeftRadius: 24, borderTopRightRadius: 24,
      maxHeight: '90%',
      borderTopWidth: 1, borderColor: 'rgba(255,255,255,0.08)',
    },
    body: {
      alignItems: 'center', padding: 24,
    },
    handle: {
      width: 40, height: 4, borderRadius: 2,
      backgroundColor: 'rgba(255,255,255,0.2)', marginBottom: 20,
    },
    title: {
      color: Colors.text || '#FFF', fontSize: 20, fontWeight: '800', marginBottom: 4,
    },
    subtitle: {
      color: Colors.textMuted || '#888', fontSize: 13, textAlign: 'center',
      marginBottom: 20, lineHeight: 18,
    },
    label: {
      color: Colors.textMuted || '#888', fontSize: 10, fontWeight: '700',
      letterSpacing: 1.2, alignSelf: 'flex-start', marginBottom: 8, marginTop: 14,
    },
    input: {
      backgroundColor: 'rgba(255,255,255,0.07)', borderRadius: 12,
      borderWidth: 1, borderColor: 'rgba(255,255,255,0.12)',
      color: Colors.text || '#FFF', fontSize: 15,
      paddingHorizontal: 14, paddingVertical: 11, width: '100%', marginBottom: 2,
    },
    segRow:    { flexDirection: 'row', gap: 10, width: '100%', marginBottom: 4 },
    segBtn:    { flex: 1, flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 6, borderWidth: 1.5, borderColor: 'rgba(255,255,255,0.18)', borderRadius: 12, paddingVertical: 10 },
    segBtnActive:  { backgroundColor: Colors.primary || '#FFAA00', borderColor: Colors.primary || '#FFAA00' },
    segBtnText:    { color: Colors.primary || '#FFAA00', fontSize: 13, fontWeight: '700' },
    groupChipRow:  { flexDirection: 'row', flexWrap: 'wrap', gap: 8, marginBottom: 10, alignSelf: 'flex-start' },
    groupChip:     { borderWidth: 1.5, borderColor: 'rgba(255,255,255,0.18)', borderRadius: 20, paddingHorizontal: 12, paddingVertical: 6 },
    groupChipActive: { backgroundColor: Colors.primary || '#FFAA00', borderColor: Colors.primary || '#FFAA00' },
    groupChipText: { color: '#bbb', fontSize: 12, fontWeight: '700' },
    hwInfo: {
      flexDirection: 'row', alignItems: 'center', gap: 6,
      backgroundColor: 'rgba(255,255,255,0.04)', borderRadius: 8,
      paddingHorizontal: 10, paddingVertical: 6, marginTop: 10, alignSelf: 'flex-start',
    },
    hwInfoText:  { color: Colors.textMuted || '#888', fontSize: 11 },
    errorText:   { color: '#FF4444', fontSize: 12, marginTop: 8, textAlign: 'center' },
    saveBtn: {
      flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 8,
      backgroundColor: Colors.primary || '#FFAA00', borderRadius: 14,
      paddingVertical: 14, width: '100%', marginTop: 20,
    },
    saveBtnText: { color: '#000', fontSize: 15, fontWeight: '800' },
    skipBtn:     { marginTop: 12, paddingVertical: 8 },
    skipBtnText: { color: Colors.textMuted || '#888', fontSize: 13 },
  });
}
