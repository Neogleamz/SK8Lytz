import React, { useState } from 'react';
import { Text, TouchableOpacity, View, Alert } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useTheme } from '../../context/ThemeContext';
import { Spacing } from '../../theme/theme';
import { useAuthStyles } from './AuthStyles';

const STORAGE_OFFLINE_SKIP = '@Sk8lytz_offline_skip';

interface AuthFooterActionsProps {
  mode: 'LOGIN' | 'SIGNUP' | 'FORGOT_PASSWORD' | 'MAGIC_LINK';
  onOfflineMode?: () => void;
  isSandboxEnabled: boolean;
  setErrorMessage: (msg: string) => void;
}

export function AuthFooterActions({ mode, onOfflineMode, isSandboxEnabled, setErrorMessage }: AuthFooterActionsProps) {
  const { Colors } = useTheme();
  const styles = useAuthStyles();
  const [rememberOffline, setRememberOffline] = useState(false);

  return (
    <>
      {/* Offline mode option */}
      {mode !== 'FORGOT_PASSWORD' && onOfflineMode && (
        <View style={{ alignItems: 'center' }}>
          <TouchableOpacity
            onPress={async () => {
              if (rememberOffline) {
                await AsyncStorage.setItem(STORAGE_OFFLINE_SKIP, 'true');
              }
              onOfflineMode();
            }}
            style={styles.offlineButton}
            activeOpacity={0.7}
          >
            <Text style={styles.offlineButtonText}>📵 Continue Offline</Text>
          </TouchableOpacity>
          
          <TouchableOpacity
            onPress={() => setRememberOffline(v => !v)}
            style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, marginTop: Spacing.sm, paddingVertical: Spacing.xs }}
            activeOpacity={0.7}
          >
            <View style={[
              styles.checkbox,
              rememberOffline && { backgroundColor: Colors.primary, borderColor: Colors.primary }
            ]}>
              {rememberOffline && <MaterialCommunityIcons name="check" size={12} color="#000" />}
            </View>
            <Text style={{ color: Colors.textMuted, fontSize: 12 }}>Remember offline choice</Text>
          </TouchableOpacity>
        </View>
      )}

      {/* DEV MODE - Virtual Skates Bypass */}
      {isSandboxEnabled && (mode === 'LOGIN') && onOfflineMode && (
        <TouchableOpacity
          style={{ 
            marginTop: Spacing.lg, paddingHorizontal: Spacing.xl, paddingVertical: Spacing.md, 
            borderWidth: 1, borderColor: 'rgba(0,240,255,0.4)', borderRadius: 12, 
            backgroundColor: 'rgba(0,240,255,0.05)', alignItems: 'center',
            flexDirection: 'row', justifyContent: 'center', gap: Spacing.sm
          }}
          onPress={async () => {
            await AsyncStorage.setItem('@Sk8lytz_demo_halo', 'true');
            await AsyncStorage.setItem('@Sk8lytz_demo_soul', 'true');
            onOfflineMode();
          }}
        >
          <MaterialCommunityIcons name="developer-board" size={16} color="#00f0ff" />
          <Text style={{ color: '#00f0ff', fontWeight: 'bold', fontSize: 13, letterSpacing: 1 }}>DEV MODE: VIRTUAL SKATES</Text>
        </TouchableOpacity>
      )}

      {/* The Nuke Button */}
      {isSandboxEnabled && (
        <TouchableOpacity
          style={{
            marginTop: Spacing.md,
            alignSelf: 'center',
            backgroundColor: 'rgba(255, 0, 0, 0.1)',
            paddingVertical: Spacing.md,
            paddingHorizontal: Spacing.xl,
            borderRadius: 8,
            borderWidth: 1,
            borderColor: 'rgba(255, 0, 0, 0.4)'
          }}
          onPress={async () => {
            await AsyncStorage.clear();
            setErrorMessage('APP RESET: ALL DATA CLEARED. PLEASE RESTART APP.');
          }}
        >
          <Text style={{ color: '#FF4444', fontWeight: 'bold', fontSize: 12 }}>☢️ NUKE APP CACHE</Text>
        </TouchableOpacity>
      )}
    </>
  );
}
