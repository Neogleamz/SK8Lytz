import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ActivityIndicator, SafeAreaView, Linking, Platform } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../../context/ThemeContext';
import { Typography } from '../../theme/theme';
import useBLE from '../../hooks/useBLE';
import { ZenggeProtocol } from '../../protocols/ZenggeProtocol';

interface HardwareSetupWizardScreenProps {
  onSetupComplete: () => void;
}

export default function HardwareSetupWizardScreen({ onSetupComplete }: HardwareSetupWizardScreenProps) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const { scanForPeripherals, isScanning, requestPermissions, isBluetoothSupported, isBluetoothEnabled, pendingRegistrations, writeToDevice } = useBLE();
  const [hasStartedScan, setHasStartedScan] = useState(false);
  const [step, setStep] = useState<1 | 2>(1);
  const [isBlinking, setIsBlinking] = useState<string | null>(null);

  useEffect(() => {
    // Transition to step 2 when devices are successfully probed and classified
    if (hasStartedScan && pendingRegistrations.length > 0 && !isScanning) {
      console.log('[FTUE] Phase 1 Scanner discovered devices. Transitioning to Phase 2 (Discovery List)');
      setStep(2);
    }
  }, [pendingRegistrations, hasStartedScan, isScanning]);

  const handleStartScan = async () => {
    const granted = await requestPermissions();
    if (granted && !isScanning) {
      setHasStartedScan(true);
      scanForPeripherals();
    }
  };

  const handleBlinkDevice = async (deviceMac: string) => {
    if (isBlinking) return;
    setIsBlinking(deviceMac);
    try {
      // 0x31 solid color mode: Green, 100% bright, 100% speed
      const blinkPayload = ZenggeProtocol.setSymphonyColor(0, 255, 0); 
      await writeToDevice(blinkPayload, deviceMac);
      
      // Wait half a second, then send Off command
      setTimeout(async () => {
        const offPayload = ZenggeProtocol.turnOff();
        await writeToDevice(offPayload, deviceMac);
        setIsBlinking(null);
      }, 500);
    } catch (e) {
      console.warn("Blink test failed", e);
      setIsBlinking(null);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
  const renderStep1 = () => (
    <View style={styles.content}>
      <MaterialCommunityIcons name="roller-skate" size={64} color={Colors.primary || '#00f0ff'} style={{ marginBottom: 20 }} />
      <Text style={[Typography.title, styles.title]}>Welcome to SK8Lytz</Text>
      
      <Text style={styles.subtitle}>
        Before you can light up the streets, we need to locate and claim your hardware.
      </Text>

      <View style={styles.instructionCard}>
        <Text style={styles.instructionHeader}>Step 1: Power On</Text>
        <Text style={styles.instructionBody}>
          Ensure your SK8Lytz (HALOZ or SOULZ) are plugged in and actively powered on.
        </Text>
      </View>

      <View style={styles.instructionCard}>
        <Text style={styles.instructionHeader}>Step 2: Proximity</Text>
        <Text style={styles.instructionBody}>
           Keep your mobile device within 5 feet of the controllers for the initial claim process.
        </Text>
      </View>

      <TouchableOpacity 
        style={styles.helpLink} 
        onPress={() => Linking.openURL('https://neogleamz.com/sk8lytz-installation')}
      >
        <MaterialCommunityIcons name="help-circle-outline" size={16} color={Colors.textMuted} />
        <Text style={styles.helpText}>View Installation Guide</Text>
      </TouchableOpacity>
    </View>
  );

  const renderStep2 = () => {
    const haloz = pendingRegistrations.filter(r => r.product_type === 'HALOZ');
    const soulz = pendingRegistrations.filter(r => r.product_type === 'SOULZ');

    const renderDeviceGroup = (title: string, devices: typeof pendingRegistrations, color: string) => {
      if (devices.length === 0) return null;
      return (
        <View style={styles.groupContainer}>
          <Text style={[styles.groupTitle, { color }]}>{title} DISCOVERED</Text>
          {devices.map((device, index) => (
            <View key={device.device_mac} style={styles.deviceRow}>
              <View style={styles.deviceInfo}>
                <Text style={styles.deviceName}>{device.device_name}</Text>
                <Text style={styles.deviceMeta}>MAC: {device.device_mac} • RSSI: {device.rssi}</Text>
                <Text style={styles.deviceMeta}>LEDs: {device.led_points} • IC: {device.ic_type}</Text>
              </View>
              <TouchableOpacity 
                style={[styles.blinkBtn, isBlinking === device.device_mac && styles.blinkBtnActive]}
                onPress={() => handleBlinkDevice(device.device_mac)}
                disabled={isBlinking !== null}
              >
                <MaterialCommunityIcons 
                  name="shield-sun" 
                  size={18} 
                  color={isBlinking === device.device_mac ? '#000' : Colors.text} 
                />
                <Text style={[styles.blinkBtnText, isBlinking === device.device_mac && { color: '#000' }]}>
                  {isBlinking === device.device_mac ? 'BLINKING' : 'BLINK'}
                </Text>
              </TouchableOpacity>
            </View>
          ))}
        </View>
      );
    };

    return (
      <View style={styles.content}>
        <View style={styles.successIconHeader}>
          <MaterialCommunityIcons name="check-circle" size={48} color="#4ade80" />
        </View>
        <Text style={[Typography.title, styles.title]}>Hardware Found</Text>
        <Text style={styles.subtitle}>
          Tap "BLINK" to physically identify each controller. Make sure it's the correct product style.
        </Text>

        <View style={styles.scrollViewWrapper}>
          {renderDeviceGroup('HALOZ', haloz, '#00f0ff')}
          {renderDeviceGroup('SOULZ', soulz, '#a855f7')}
        </View>
      </View>
    );
  };

  return (
    <SafeAreaView style={styles.container}>
      {step === 1 ? renderStep1() : renderStep2()}

      <View style={styles.footer}>
        {(!isBluetoothSupported || !isBluetoothEnabled) ? (
          <View style={styles.errorBox}>
            <MaterialCommunityIcons name="bluetooth-off" size={20} color="#ff4444" />
            <Text style={styles.errorText}>Please enable Bluetooth to continue.</Text>
          </View>
        ) : step === 1 ? (
          <TouchableOpacity 
            style={[styles.primaryBtn, isScanning && styles.primaryBtnDisabled]} 
            onPress={handleStartScan}
            disabled={isScanning}
          >
            {isScanning ? (
              <View style={styles.scanningRow}>
                <ActivityIndicator color="#000" size="small" />
                <Text style={styles.primaryBtnText}>SCANNING...</Text>
              </View>
            ) : (
              <Text style={styles.primaryBtnText}>PROBE FOR DEVICES →</Text>
            )}
          </TouchableOpacity>
        ) : (
          <TouchableOpacity 
            style={styles.primaryBtn} 
            onPress={() => {
               console.log('[FTUE] Phase 2 Complete (Mock). Transitioning to Phase 3/4.');
               // For now, auto-complete to unblock until Phase 3/4 is merged.
               onSetupComplete();
            }}
          >
            <Text style={styles.primaryBtnText}>CONTINUE TO SETUP →</Text>
          </TouchableOpacity>
        )}
      </View>
    </SafeAreaView>
  );
}

function createStyles(Colors: any) {
  return StyleSheet.create({
    container: { flex: 1, backgroundColor: Colors.background || '#0D0D0D' },
    content: { flex: 1, padding: 24, justifyContent: 'center', alignItems: 'center' },
    title: { color: Colors.text || '#fff', fontSize: 26, textAlign: 'center', marginBottom: 12 },
    subtitle: { color: Colors.textMuted || '#888', fontSize: 14, textAlign: 'center', marginBottom: 32, lineHeight: 22 },
    instructionCard: {
      backgroundColor: 'rgba(255,255,255,0.03)',
      borderWidth: 1, borderColor: 'rgba(255,255,255,0.08)',
      borderRadius: 12, padding: 16, width: '100%', marginBottom: 12,
    },
    instructionHeader: { color: Colors.primary || '#00f0ff', fontWeight: 'bold', fontSize: 13, marginBottom: 4 },
    instructionBody: { color: Colors.textMuted || '#888', fontSize: 13, lineHeight: 18 },
    helpLink: { flexDirection: 'row', alignItems: 'center', marginTop: 24, gap: 6 },
    helpText: { color: Colors.textMuted, fontSize: 13, textDecorationLine: 'underline' },
    footer: { padding: 24, paddingBottom: Platform.OS === 'ios' ? 0 : 24 },
    primaryBtn: {
      backgroundColor: Colors.primary || '#00f0ff',
      paddingVertical: 16, borderRadius: 14, alignItems: 'center', width: '100%'
    },
    primaryBtnDisabled: { opacity: 0.7 },
    primaryBtnText: { color: '#000', fontWeight: '900', fontSize: 15, letterSpacing: 1 },
    scanningRow: { flexDirection: 'row', alignItems: 'center', gap: 10 },
    errorBox: { flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: 8, padding: 16, backgroundColor: 'rgba(255,68,68,0.1)', borderRadius: 12 },
    errorText: { color: '#ff4444', fontSize: 13, fontWeight: 'bold' },
    
    // Step 2 Styles
    successIconHeader: { marginBottom: 16, alignItems: 'center' },
    scrollViewWrapper: { flex: 1, width: '100%', marginTop: 8 },
    groupContainer: { marginBottom: 24, width: '100%' },
    groupTitle: { fontSize: 12, fontWeight: '900', letterSpacing: 1.5, marginBottom: 12, marginLeft: 4 },
    deviceRow: { 
      flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between',
      backgroundColor: 'rgba(255,255,255,0.03)', borderWidth: 1, borderColor: 'rgba(255,255,255,0.06)',
      borderRadius: 12, padding: 14, marginBottom: 10 
    },
    deviceInfo: { flex: 1 },
    deviceName: { color: '#fff', fontSize: 16, fontWeight: 'bold', marginBottom: 4 },
    deviceMeta: { color: Colors.textMuted || '#888', fontSize: 11, marginBottom: 2 },
    blinkBtn: {
      flexDirection: 'row', alignItems: 'center', gap: 6,
      backgroundColor: 'rgba(255,255,255,0.08)', paddingHorizontal: 12, paddingVertical: 8,
      borderRadius: 8, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)'
    },
    blinkBtnActive: { backgroundColor: '#4ade80', borderColor: '#4ade80' },
    blinkBtnText: { color: Colors.text || '#fff', fontSize: 12, fontWeight: '800' }
  });
}
