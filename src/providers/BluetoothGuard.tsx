import React, { useEffect, useState, useRef } from 'react';
import { View, Text, TouchableOpacity, AppState, AppStateStatus, ActivityIndicator, StyleSheet, Alert, Linking } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useSharedBLE } from '../context/BLEContext';
import { checkPermission, requestPermission } from '../services/PermissionService';
import { Layout, Spacing, ThemePalette } from '../theme/theme';
import { AppLogger } from '../services/AppLogger';
import { useTheme } from '../context/ThemeContext';

export function BluetoothGuard({ children }: { children: React.ReactNode }) {
  const ble = useSharedBLE();
  const { Colors } = useTheme();
  
  type GuardStatus = 'checking' | 'idle' | 'requesting';
  const [status, setStatus] = useState<GuardStatus>('checking');
  const [hasPermission, setHasPermission] = useState<boolean | null>(null);

  const checkBluetoothState = async () => {
    try {
      const granted = await checkPermission('BLUETOOTH');
      setHasPermission(granted);
    } catch {
      setHasPermission(false);
    } finally {
      setStatus(prev => prev === 'checking' ? 'idle' : prev);
    }
  };

  useEffect(() => {
    checkBluetoothState();

    const appStateSub = AppState.addEventListener('change', (nextState: AppStateStatus) => {
      if (nextState === 'active') {
        checkBluetoothState();
      }
    });

    return () => appStateSub.remove();
  }, []);

  const handleRequestPermission = async () => {
    if (status === 'requesting') return;
    setStatus('requesting');
    try {
      const granted = await requestPermission('BLUETOOTH');
      if (granted) {
        setHasPermission(true);
      } else {
        setHasPermission(false);
        Alert.alert(
          'Bluetooth Required',
          'Bluetooth permission is permanently disabled. You must enable it in your device settings to use SK8Lytz.',
          [
            { text: 'Cancel', style: 'cancel' },
            { text: 'Open Settings', onPress: () => Linking.openSettings() }
          ]
        );
      }
    } finally {
      setStatus('idle');
    }
  };

  useEffect(() => {
    if (hasPermission === true && ble.isBluetoothEnabled && ble.isBluetoothSupported) {
      AppLogger.log('BLE_STATE_CHANGE', { event: 'bluetooth_guard_trigger_sweeper' });
      ble.startSweeper();
    }
  }, [hasPermission, ble.isBluetoothEnabled, ble.isBluetoothSupported, ble.startSweeper]);

  const styles = createStyles(Colors);

  if (status === 'checking') {
    return (
      <View style={{ flex: 1, backgroundColor: Colors.background, justifyContent: 'center', alignItems: 'center' }}>
        <ActivityIndicator size="large" color={Colors.primary} />
      </View>
    );
  }

  // Intercept if permission is missing OR Bluetooth is disabled in settings
  const needsPermission = hasPermission === false;
  const needsEnabled = ble.isBluetoothSupported && !ble.isBluetoothEnabled;

  if (needsPermission || needsEnabled) {
    return (
      <View style={{ flex: 1, backgroundColor: Colors.background, padding: Spacing.xl, paddingTop: 100 }}>
        
        {/* Header matching PermissionsOnboardingScreen */}
        <View style={{ marginBottom: Spacing.xxl }}>
          <MaterialCommunityIcons name="shield-check" size={48} color={Colors.primary} style={{ marginBottom: Spacing.lg }} />
          <Text style={styles.title}>Power Up SK8Lytz</Text>
          <Text style={styles.subtitle}>
            We just make cool skate apps. You have full control over what this app can access.
          </Text>
          
          <View style={styles.privacyHero}>
            <MaterialCommunityIcons name="lock-outline" size={16} color={Colors.textMuted} />
            <Text style={styles.privacyText}>
              No data sold. No trackers. Just SK8Lytz. We only use telemetry to make your lights smarter.
            </Text>
          </View>
        </View>

        {/* Card matching GranularPermissionsList required state */}
        <View style={[styles.card, styles.cardDeniedRequired]}>
          <View style={styles.cardHeader}>
            <View style={[styles.iconBox, styles.iconBoxRequired]}>
              <MaterialCommunityIcons
                name={needsPermission ? "bluetooth" : "bluetooth-off"}
                size={24}
                color="#FF6B6B"
              />
            </View>
            <View style={styles.cardInfo}>
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}>
                <Text style={styles.cardTitle}>Heartbeat (Bluetooth)</Text>
                <Text style={styles.badgeRequired}>REQUIRED</Text>
              </View>
              <Text style={styles.cardDesc}>Connects the app to your skates. The app cannot function without this.</Text>
              <Text style={styles.requiredWarning}>
                {needsPermission 
                  ? "⚠ Required to use SK8Lytz. Tap Grant Access below."
                  : "⚠ Bluetooth is currently turned off. Please enable it in system settings."
                }
              </Text>
            </View>
          </View>

          {needsPermission ? (
            <TouchableOpacity
              style={styles.grantButton}
              activeOpacity={0.8}
              onPress={handleRequestPermission}
            >
              <MaterialCommunityIcons name="lock-open-outline" size={16} color="#000" />
              <Text style={styles.grantButtonText}>Grant Access</Text>
            </TouchableOpacity>
          ) : (
            <View style={styles.waitingContainer}>
              <ActivityIndicator size="small" color="#FF6B6B" style={{ marginRight: 8 }} />
              <Text style={styles.waitingText}>Waiting for Bluetooth...</Text>
            </View>
          )}
        </View>
      </View>
    );
  }

  return <>{children}</>;
}

const createStyles = (Colors: ThemePalette) => StyleSheet.create({
  title: { fontSize: 32, fontWeight: '900', color: Colors.text, marginBottom: Spacing.sm, letterSpacing: -0.5 },
  subtitle: { fontSize: 16, color: Colors.textMuted, lineHeight: 24, marginBottom: Spacing.xl },
  privacyHero: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
    padding: Spacing.lg,
    borderRadius: Layout.borderRadius,
    gap: Spacing.md,
  },
  privacyText: { flex: 1, fontSize: 13, color: Colors.textMuted, lineHeight: 20 },
  
  card: {
    backgroundColor: Colors.surface,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
    borderRadius: Layout.borderRadius,
    padding: Spacing.lg,
  },
  cardDeniedRequired: {
    borderColor: 'rgba(255, 107, 107, 0.4)',
    backgroundColor: 'rgba(255, 107, 107, 0.04)',
  },
  cardHeader: { flexDirection: 'row', gap: Spacing.lg, marginBottom: Spacing.md },
  iconBox: {
    width: 48, height: 48,
    borderRadius: 24,
    backgroundColor: 'rgba(255,255,255,0.05)',
    justifyContent: 'center', alignItems: 'center'
  },
  iconBoxRequired: {
    backgroundColor: 'rgba(255, 107, 107, 0.12)',
  },
  cardInfo: { flex: 1 },
  cardTitle: { fontSize: 16, fontWeight: 'bold', color: Colors.text, marginBottom: Spacing.xs },
  cardDesc: { fontSize: 13, color: Colors.textMuted, lineHeight: 18, marginBottom: Spacing.sm },
  badgeRequired: {
    fontSize: 10, fontWeight: '800',
    backgroundColor: 'rgba(255, 107, 107, 0.2)', color: '#FF6B6B',
    paddingHorizontal: Spacing.sm, paddingVertical: Spacing.xxs, borderRadius: 4, overflow: 'hidden'
  },
  requiredWarning: {
    fontSize: 11, color: '#FF6B6B', fontWeight: '600', marginTop: Spacing.xs,
  },
  grantButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: Spacing.sm,
    backgroundColor: '#FF6B6B',
    paddingVertical: Spacing.md,
    borderRadius: 8,
    marginTop: Spacing.xs,
  },
  grantButtonText: {
    fontSize: 14, fontWeight: '800', color: '#000', letterSpacing: 0.5,
  },
  waitingContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'rgba(255,255,255,0.03)',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.06)',
    padding: Spacing.md,
    borderRadius: 8,
    marginTop: Spacing.xs
  },
  waitingText: {
    color: '#888',
    fontSize: 13,
  },
});
