import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ActivityIndicator, SafeAreaView, Linking, Platform } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../../context/ThemeContext';
import { Typography } from '../../theme/theme';
import useBLE from '../../hooks/useBLE';

interface HardwareSetupWizardScreenProps {
  onSetupComplete: () => void;
}

export default function HardwareSetupWizardScreen({ onSetupComplete }: HardwareSetupWizardScreenProps) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const { scanForPeripherals, isScanning, requestPermissions, isBluetoothSupported, isBluetoothEnabled, pendingRegistrations } = useBLE();
  const [hasStartedScan, setHasStartedScan] = useState(false);

  useEffect(() => {
    // Phase 1 -> Phase 2 transition mock.
    // If pendingRegistrations populate (we probed devices), we temporarily mock completing the setup
    // until Phase 2 is built.
    if (hasStartedScan && pendingRegistrations.length > 0) {
      console.log('[FTUE] Phase 1 Scanner discovered devices. Transitioning to Phase 2 (mock complete)');
      // For now, auto-complete to unblock until Phase 2 is merged.
      // onSetupComplete();
    }
  }, [pendingRegistrations, hasStartedScan, onSetupComplete]);

  const handleStartScan = async () => {
    const granted = await requestPermissions();
    if (granted && !isScanning) {
      setHasStartedScan(true);
      scanForPeripherals();
    }
  };

  return (
    <SafeAreaView style={styles.container}>
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

      <View style={styles.footer}>
        {(!isBluetoothSupported || !isBluetoothEnabled) ? (
          <View style={styles.errorBox}>
            <MaterialCommunityIcons name="bluetooth-off" size={20} color="#ff4444" />
            <Text style={styles.errorText}>Please enable Bluetooth to continue.</Text>
          </View>
        ) : (
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
    errorText: { color: '#ff4444', fontSize: 13, fontWeight: 'bold' }
  });
}
