import React, { useEffect, useState, useRef } from 'react';
import { View, Text, TouchableOpacity, AppState, AppStateStatus, ActivityIndicator, StyleSheet } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useSharedBLE } from '../context/BLEContext';
import { checkPermission } from '../services/PermissionService';
import { Spacing } from '../theme/theme';
import { AppLogger } from '../services/AppLogger';

export function BluetoothGuard({ children }: { children: React.ReactNode }) {
  const ble = useSharedBLE();
  
  const [hasPermission, setHasPermission] = useState<boolean | null>(null);
  const [checking, setChecking] = useState(true);
  const isRequestingRef = useRef(false);

  const checkBluetoothState = async () => {
    try {
      const granted = await checkPermission('BLUETOOTH');
      setHasPermission(granted);
    } catch {
      setHasPermission(false);
    } finally {
      setChecking(false);
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
    if (isRequestingRef.current) return;
    isRequestingRef.current = true;
    try {
      const granted = await ble.requestPermissions();
      setHasPermission(granted);
    } finally {
      isRequestingRef.current = false;
    }
  };

  useEffect(() => {
    if (hasPermission === true && ble.isBluetoothEnabled && ble.isBluetoothSupported) {
      AppLogger.log('BLE_STATE_CHANGE', { event: 'bluetooth_guard_trigger_sweeper' });
      ble.startSweeper();
    }
  }, [hasPermission, ble.isBluetoothEnabled, ble.isBluetoothSupported, ble.startSweeper]);

  if (checking) {
    return (
      <View style={{ flex: 1, backgroundColor: '#0D0D0D', justifyContent: 'center', alignItems: 'center' }}>
        <ActivityIndicator size="large" color="#00f0ff" />
      </View>
    );
  }

  // Intercept if permission is missing OR Bluetooth is disabled in settings
  const needsPermission = hasPermission === false;
  const needsEnabled = ble.isBluetoothSupported && !ble.isBluetoothEnabled;

  if (needsPermission || needsEnabled) {
    return (
      <View style={{ flex: 1, backgroundColor: '#0D0D0D', justifyContent: 'center', alignItems: 'center', padding: Spacing.xl }}>
        <View style={styles.pulseContainer}>
          <MaterialCommunityIcons 
            name={needsPermission ? "bluetooth-transfer" : "bluetooth-off"} 
            size={64} 
            color="#00f0ff" 
          />
        </View>
        
        <Text style={styles.title}>Bluetooth Required</Text>
        
        <Text style={styles.subtitle}>
          {needsPermission 
            ? "SK8Lytz requires Bluetooth scan permissions to discover, sync, and control your lights."
            : "Bluetooth is currently turned off. Please enable Bluetooth in your system settings to connect to your skates."
          }
        </Text>

        {needsPermission ? (
          <TouchableOpacity style={styles.primaryBtn} onPress={handleRequestPermission}>
            <Text style={styles.primaryBtnText}>GRANT BLUETOOTH ACCESS</Text>
          </TouchableOpacity>
        ) : (
          <View style={styles.waitingContainer}>
            <ActivityIndicator size="small" color="#00f0ff" style={{ marginRight: 8 }} />
            <Text style={styles.waitingText}>Waiting for Bluetooth to be turned on...</Text>
          </View>
        )}
      </View>
    );
  }

  return <>{children}</>;
}

const styles = StyleSheet.create({
  pulseContainer: {
    marginBottom: Spacing.xl,
    padding: Spacing.lg,
    borderRadius: 50,
    backgroundColor: 'rgba(0, 240, 255, 0.06)',
    borderWidth: 1.5,
    borderColor: 'rgba(0, 240, 255, 0.25)',
  },
  title: {
    color: '#FFF',
    fontSize: 26,
    fontWeight: '900',
    textAlign: 'center',
    marginBottom: Spacing.sm,
    letterSpacing: -0.5,
  },
  subtitle: {
    color: '#888',
    fontSize: 14,
    textAlign: 'center',
    lineHeight: 22,
    marginBottom: Spacing.xxl,
    paddingHorizontal: Spacing.md,
  },
  primaryBtn: {
    backgroundColor: '#00f0ff',
    paddingVertical: Spacing.lg,
    paddingHorizontal: Spacing.xxl,
    borderRadius: 14,
    width: '100%',
    alignItems: 'center',
    shadowColor: '#00f0ff',
    shadowOpacity: 0.4,
    shadowRadius: 10,
    elevation: 5,
  },
  primaryBtnText: {
    color: '#000',
    fontWeight: '900',
    fontSize: 14,
    letterSpacing: 1,
  },
  waitingContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: 'rgba(255,255,255,0.03)',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.06)',
    padding: Spacing.md,
    borderRadius: 12,
  },
  waitingText: {
    color: '#888',
    fontSize: 13,
  },
});
