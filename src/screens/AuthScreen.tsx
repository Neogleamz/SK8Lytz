import React, { useEffect, useState } from 'react';
import {
  Alert,
  KeyboardAvoidingView,
  Linking,
  Platform,
  Text,
  TouchableOpacity,
  View
} from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';

import { useTheme } from '../context/ThemeContext';
import { Spacing } from '../theme/theme';
import { AppLogger } from '../services/AppLogger';
import { useAuthStyles } from '../components/auth/AuthStyles';
import { AuthHeader } from '../components/auth/AuthHeader';
import { AuthFormSignIn } from '../components/auth/AuthFormSignIn';
import { AuthFormSignUp } from '../components/auth/AuthFormSignUp';
import { AuthFormForgotPassword } from '../components/auth/AuthFormForgotPassword';
import { AuthFooterActions } from '../components/auth/AuthFooterActions';
import { DevSandboxDrawer } from '../components/auth/DevSandboxDrawer';

import { STORAGE_REMEMBER_CREDS, STORAGE_LAST_EMAIL } from '../constants/storageKeys';

type AuthMode = 'LOGIN' | 'SIGNUP' | 'FORGOT_PASSWORD' | 'MAGIC_LINK';

const showHelp = () => {
  if (Platform.OS === 'web') {
    Linking.openURL('mailto:support@sk8lytz.com?subject=SK8Lytz%20Support');
  } else {
    Alert.alert(
      'SK8Lytz Support',
      'Need help?\n\n• Email: support@sk8lytz.com\n• Docs: sk8lytz.com/help\n• Discord: discord.gg/sk8lytz'
    );
  }
};

export default function AuthScreen({ onOfflineMode, sessionExpired }: { onOfflineMode?: () => void; sessionExpired?: boolean }) {
  const { isDark, toggleTheme, Colors } = useTheme();
  const styles = useAuthStyles();

  const [mode, setMode] = useState<AuthMode>('SIGNUP');
  
  // Stored Credentials state
  const [initialEmail, setInitialEmail] = useState('');
  const [initialRememberMe, setInitialRememberMe] = useState(false);
  const [hasLoadedCreds, setHasLoadedCreds] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    AppLogger.log('SCREEN_OPENED', { screen: 'AuthScreen' });
  }, []);

  useEffect(() => {
    AsyncStorage.getItem(STORAGE_REMEMBER_CREDS).then(raw => {
      if (raw) {
        try {
          const saved = JSON.parse(raw);
          if (saved.email) {
             setMode('LOGIN');
          }
          if (saved.rememberMe) {
            setInitialEmail(saved.email || '');
            setInitialRememberMe(true);
          } else {
            setInitialEmail(saved.email || '');
          }
        } catch {}
        setHasLoadedCreds(true);
      } else {
        AsyncStorage.getItem(STORAGE_LAST_EMAIL).then(saved => {
          if (saved) {
             setInitialEmail(saved);
             setMode('LOGIN');
          }
          setHasLoadedCreds(true);
        }).catch((err: unknown) => {
          AppLogger.warn('[AuthScreen] STORAGE_LAST_EMAIL read failed', { error: err instanceof Error ? err.message : String(err) });
          setHasLoadedCreds(true);
        });
      }
    }).catch((err: unknown) => {
      AppLogger.warn('[AuthScreen] STORAGE_REMEMBER_CREDS read failed', { error: err instanceof Error ? err.message : String(err) });
      setHasLoadedCreds(true);
    });
  }, []);

  return (
    <KeyboardAvoidingView style={styles.container} behavior={Platform.OS === 'ios' ? 'padding' : undefined}>
      <View style={styles.topButtons}>
        <TouchableOpacity style={styles.topBtn} onPress={toggleTheme}>
          <MaterialCommunityIcons
            name={isDark ? 'weather-sunny' : 'weather-night'}
            size={18}
            color={Colors.textMuted}
          />
        </TouchableOpacity>
        <TouchableOpacity style={styles.topBtn} onPress={showHelp}>
          <MaterialCommunityIcons name="help-circle-outline" size={18} color={Colors.textMuted} />
        </TouchableOpacity>
      </View>

      <View style={styles.scrollContent}>
        {sessionExpired && (
          <View style={{
            backgroundColor: 'rgba(255, 165, 0, 0.12)',
            borderWidth: 1,
            borderColor: 'rgba(255, 165, 0, 0.4)',
            borderRadius: 10,
            paddingVertical: 10,
            paddingHorizontal: 14,
            marginBottom: Spacing.md,
            flexDirection: 'row',
            alignItems: 'center',
            gap: Spacing.sm,
          }}>
            <Text style={{ fontSize: 16 }}>⏱️</Text>
            <Text style={{ color: '#FFA500', fontSize: 13, flex: 1, lineHeight: 18 }}>
              Your session expired. Please sign in again.
            </Text>
          </View>
        )}
        <AuthHeader />

        {hasLoadedCreds && (
          <>
            {!!errorMessage && (
              <Text style={{ color: '#FF4444', textAlign: 'center', marginBottom: Spacing.md }}>
                {errorMessage}
              </Text>
            )}
            {mode === 'LOGIN' && (
              <AuthFormSignIn 
                initialEmail={initialEmail} 
                initialRememberMe={initialRememberMe} 
                onModeChange={setMode} 
              />
            )}
            {mode === 'SIGNUP' && (
              <AuthFormSignUp onModeChange={setMode} />
            )}
            {mode === 'FORGOT_PASSWORD' && (
              <AuthFormForgotPassword onModeChange={setMode} />
            )}
          </>
        )}

        <View style={{ marginTop: 'auto', alignItems: 'center', paddingTop: Spacing.xl, paddingBottom: 24 }}>
          <AuthFooterActions 
            mode={mode} 
            onOfflineMode={onOfflineMode} 
            setErrorMessage={setErrorMessage}
          />
        </View>
      </View>
      <DevSandboxDrawer onOfflineMode={onOfflineMode} setErrorMessage={setErrorMessage} />
    </KeyboardAvoidingView>
  );
}
