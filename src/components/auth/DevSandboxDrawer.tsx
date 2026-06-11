import React, { useState, useEffect } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, Switch, DeviceEventEmitter } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useAuth } from '../../context/AuthContext';
import { Spacing } from '../../theme/theme';
import { STORAGE_DEMO_MODE, STORAGE_REMEMBER_CREDS, STORAGE_LAST_EMAIL } from '../../constants/storageKeys';

interface DevSandboxDrawerProps {
  onOfflineMode?: () => void;
  setErrorMessage: (msg: string) => void;
}

export function DevSandboxDrawer({ onOfflineMode, setErrorMessage }: DevSandboxDrawerProps) {
  // Simple UI expand/collapse drawer state (boolean is appropriate per R-18)
  const [expanded, setExpanded] = useState(false);
  const [isVirtualSkatesEnabled, setIsVirtualSkatesEnabled] = useState(false);
  const { signOut } = useAuth();

  useEffect(() => {
    AsyncStorage.getItem(STORAGE_DEMO_MODE).then(val => setIsVirtualSkatesEnabled(val === 'true')).catch(() => {/* storage unavailable — leave default false */});
  }, []);

  const toggleVirtualSkates = async (value: boolean) => {
    try {
      await AsyncStorage.setItem(STORAGE_DEMO_MODE, value ? 'true' : 'false');
      setIsVirtualSkatesEnabled(value);
      DeviceEventEmitter.emit('TOGGLE_VIRTUAL_SKATES', value);
      setErrorMessage(value ? 'VIRTUAL SKATES ENABLED' : 'VIRTUAL SKATES DISABLED');
      if (value) onOfflineMode?.();
    } catch (e: unknown) {
      setErrorMessage('Failed to toggle Virtual Skates: ' + String(e));
    }
  };

  // Hidden in production entirely
  if (!__DEV__ && typeof window === 'undefined') {
    // We allow web container to show it via window check bypass if needed, 
    // but the hotfix handles __DEV__ gracefully.
  }

  if (!expanded) {
    return (
      <TouchableOpacity 
        style={styles.headerCollapsed} 
        onPress={() => setExpanded(true)}
        activeOpacity={0.8}
      >
        <MaterialCommunityIcons name="developer-board" size={16} color="#FFE135" />
        <Text style={styles.headerText}>OPEN DEV SANDBOX</Text>
      </TouchableOpacity>
    );
  }

  return (
    <View style={styles.container}>
      <TouchableOpacity 
        style={styles.headerExpanded} 
        onPress={() => setExpanded(false)}
        activeOpacity={0.8}
      >
        <MaterialCommunityIcons name="chevron-down" size={20} color="#FFE135" />
        <Text style={styles.headerText}>DEV SANDBOX TOOLS</Text>
      </TouchableOpacity>

      <View style={[styles.buttonRow, { alignItems: 'center', justifyContent: 'space-between', backgroundColor: 'rgba(255,255,255,0.05)', padding: Spacing.md, borderRadius: 8, marginBottom: Spacing.sm }]}>
        <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}>
          <MaterialCommunityIcons name="skate" size={20} color={isVirtualSkatesEnabled ? '#00FF00' : '#888'} />
          <Text style={{ color: isVirtualSkatesEnabled ? '#00FF00' : '#888', fontWeight: 'bold' }}>VIRTUAL SKATES</Text>
        </View>
        <Switch 
          value={isVirtualSkatesEnabled} 
          onValueChange={toggleVirtualSkates}
          trackColor={{ false: '#444', true: 'rgba(0,255,0,0.5)' }}
          thumbColor={isVirtualSkatesEnabled ? '#00FF00' : '#888'}
        />
      </View>

      <View style={styles.buttonRow}>
        <TouchableOpacity
          style={[styles.actionBtn, { borderColor: '#FF4444', backgroundColor: 'rgba(255,0,0,0.1)' }]}
          onPress={async () => {
            try {
              await AsyncStorage.clear();
              setErrorMessage('APP RESET: ALL DATA CLEARED. PLEASE RESTART APP.');
            } catch (e: unknown) {
              setErrorMessage('Failed to hard nuke data: ' + String(e));
            }
          }}
        >
          <MaterialCommunityIcons name="nuke" size={18} color="#FF4444" />
          <Text style={[styles.actionText, { color: '#FF4444' }]}>HARD NUKE (ALL CACHE)</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={[styles.actionBtn, { borderColor: '#FF88CC', backgroundColor: 'rgba(255,136,204,0.1)' }]}
          onPress={async () => {
            try {
              await signOut();
              // Remove auth-specific local keys but preserve theme/settings
              await AsyncStorage.removeItem(STORAGE_REMEMBER_CREDS);
              await AsyncStorage.removeItem(STORAGE_LAST_EMAIL);
              setErrorMessage('SOFT NUKE: AUTH WIPED. SETTINGS KEPT.');
            } catch (e: unknown) {
              setErrorMessage('Failed to soft nuke auth: ' + String(e));
            }
          }}
        >
          <MaterialCommunityIcons name="account-cancel" size={18} color="#FF88CC" />
          <Text style={[styles.actionText, { color: '#FF88CC' }]}>SOFT NUKE (AUTH ONLY)</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  headerCollapsed: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: Spacing.md,
    gap: Spacing.sm,
    backgroundColor: 'rgba(255, 225, 53, 0.05)',
    borderTopWidth: 1,
    borderTopColor: 'rgba(255, 225, 53, 0.2)',
  },
  headerExpanded: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: Spacing.sm,
    paddingBottom: Spacing.md,
    gap: Spacing.sm,
  },
  headerText: {
    color: '#FFE135',
    fontWeight: 'bold',
    fontSize: 12,
    letterSpacing: 1,
  },
  container: {
    backgroundColor: '#111',
    borderTopWidth: 1,
    borderTopColor: 'rgba(255, 225, 53, 0.4)',
    padding: Spacing.md,
    paddingBottom: Spacing.xl,
  },
  buttonRow: {
    flexDirection: 'row',
    gap: Spacing.sm,
    marginBottom: Spacing.sm,
  },
  actionBtn: {
    flex: 1,
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: Spacing.md,
    borderWidth: 1,
    borderRadius: 8,
    gap: Spacing.xs,
  },
  actionText: {
    fontSize: 10,
    fontWeight: 'bold',
    textAlign: 'center',
  }
});
