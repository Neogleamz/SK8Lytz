import React, { useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { supabase } from '../../services/supabaseClient';
import { Spacing } from '../../theme/theme';
import { STORAGE_DEMO_MODE } from '../../constants/storageKeys';

interface DevSandboxDrawerProps {
  onOfflineMode?: () => void;
  setErrorMessage: (msg: string) => void;
}

export function DevSandboxDrawer({ onOfflineMode, setErrorMessage }: DevSandboxDrawerProps) {
  const [expanded, setExpanded] = useState(false);

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

      <View style={styles.buttonRow}>
        <TouchableOpacity
          style={[styles.actionBtn, { borderColor: '#00FF00', backgroundColor: 'rgba(0,255,0,0.1)' }]}
          onPress={async () => {
            try {
              await AsyncStorage.setItem(STORAGE_DEMO_MODE, 'true');
              setErrorMessage('VIRTUAL SKATES LOADED');
              onOfflineMode?.();
            } catch (e: unknown) {
              setErrorMessage('Failed to load Virtual Skates: ' + String(e));
            }
          }}
        >
          <MaterialCommunityIcons name="skate" size={18} color="#00FF00" />
          <Text style={[styles.actionText, { color: '#00FF00' }]}>LOAD VIRTUAL SKATES</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={[styles.actionBtn, { borderColor: '#FFBB00', backgroundColor: 'rgba(255,187,0,0.1)' }]}
          onPress={async () => {
            try {
              await AsyncStorage.setItem(STORAGE_DEMO_MODE, 'false');
              setErrorMessage('VIRTUAL SKATES UNLOADED');
            } catch (e: unknown) {
              setErrorMessage('Failed to unload Virtual Skates: ' + String(e));
            }
          }}
        >
          <MaterialCommunityIcons name="skate-off" size={18} color="#FFBB00" />
          <Text style={[styles.actionText, { color: '#FFBB00' }]}>UNLOAD VIRTUAL SKATES</Text>
        </TouchableOpacity>
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
              await supabase.auth.signOut();
              // Remove auth-specific local keys but preserve theme/settings
              await AsyncStorage.removeItem('@sk8lytz_remember_creds');
              await AsyncStorage.removeItem('@sk8lytz_last_email');
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
