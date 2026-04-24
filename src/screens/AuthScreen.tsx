import React, { useEffect, useState } from 'react';
import {
  Alert,
  KeyboardAvoidingView,
  Linking,
  Platform,
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
import { AuthSandboxToggle } from '../components/auth/AuthSandboxToggle';

const STORAGE_LAST_EMAIL    = '@Sk8lytz_auth_last_email';
const STORAGE_REMEMBER_CREDS = '@Sk8lytz_remember_creds';

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

export default function AuthScreen({ onAuthSuccess, onOfflineMode }: { onAuthSuccess: () => void; onOfflineMode?: () => void }) {
  const { isDark, toggleTheme, Colors } = useTheme();
  const styles = useAuthStyles();

  const [mode, setMode] = useState<AuthMode>('SIGNUP');
  const [isSandboxEnabled, setIsSandboxEnabled] = useState(false);
  
  // Stored Credentials state
  const [initialEmail, setInitialEmail] = useState('');
  const [initialRememberMe, setInitialRememberMe] = useState(false);
  const [hasLoadedCreds, setHasLoadedCreds] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    AppLogger.log('SCREEN_OPENED', { screen: 'AuthScreen' });
    AsyncStorage.getItem('@Sk8lytz_demo_mode').then(val => {
      setIsSandboxEnabled(val === 'true');
    });
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
        });
      }
    });
  }, []);

  return (
    <KeyboardAvoidingView style={styles.container} behavior={Platform.OS === 'ios' ? 'padding' : undefined}>
      <AuthSandboxToggle 
        isSandboxEnabled={isSandboxEnabled} 
        setIsSandboxEnabled={setIsSandboxEnabled} 
      />

      <View style={styles.topButtons}>
        {isSandboxEnabled && (
          <TouchableOpacity 
            style={[styles.topBtn, { borderColor: 'rgba(255,0,0,0.5)', backgroundColor: 'rgba(255,0,0,0.1)' }]} 
            onPress={async () => {
               await AsyncStorage.clear();
               Alert.alert("☢️ Storage Nuked", "All persistent Sandbox/Offline state has been flushed.");
            }}
          >
            <MaterialCommunityIcons name="nuke" size={18} color="red" />
          </TouchableOpacity>
        )}
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
        <AuthHeader />

        {hasLoadedCreds && (
          <>
            {mode === 'LOGIN' && (
              <AuthFormSignIn 
                initialEmail={initialEmail} 
                initialRememberMe={initialRememberMe} 
                onAuthSuccess={onAuthSuccess} 
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
            isSandboxEnabled={isSandboxEnabled} 
            setErrorMessage={setErrorMessage}
          />
        </View>
      </View>
    </KeyboardAvoidingView>
  );
}
