import React, { useState } from 'react';
import { Text, TouchableOpacity, View } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useTheme } from '../../context/ThemeContext';
import { Spacing } from '../../theme/theme';
import { useAuthStyles } from './AuthStyles';

import { STORAGE_OFFLINE_SKIP } from '../../constants/storageKeys';

interface AuthFooterActionsProps {
  mode: 'LOGIN' | 'SIGNUP' | 'FORGOT_PASSWORD' | 'MAGIC_LINK';
  onOfflineMode?: () => void;
  setErrorMessage: (msg: string) => void;
}

export function AuthFooterActions({ mode, onOfflineMode, setErrorMessage }: AuthFooterActionsProps) {
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
              try {
                if (rememberOffline) {
                  await AsyncStorage.setItem(STORAGE_OFFLINE_SKIP, 'true');
                }
                onOfflineMode();
              } catch (e: unknown) {
                const msg = e instanceof Error ? e.message : String(e);
                setErrorMessage('Failed to set offline skip: ' + msg);
              }
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

    </>
  );
}
