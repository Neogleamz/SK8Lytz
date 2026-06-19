import React, { useState } from 'react';
import { ActivityIndicator, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { makeRedirectUri } from 'expo-auth-session';
import { useTheme } from '../../context/ThemeContext';
import { useAuth } from '../../context/AuthContext';
import { AppLogger } from '../../services/appLogger';
import { Spacing } from '../../theme/theme';
import { useAuthStyles } from './AuthStyles';
import { isValidEmail } from '../../utils/validation';
import type { ViewState } from '../../types/ViewState';

interface AuthFormForgotPasswordProps {
  onModeChange: (mode: 'LOGIN' | 'SIGNUP') => void;
}

export function AuthFormForgotPassword({ onModeChange }: AuthFormForgotPasswordProps) {
  const { Colors } = useTheme();
  const styles = useAuthStyles();
  const { resetPassword } = useAuth();

  const [email, setEmail] = useState('');
  /** 4-state FSM: idle → loading → success/empty/error */
  const [viewState, setViewState] = useState<ViewState>('idle');
  const [errorMsg, setErrorMsg] = useState('');
  const [successMsg, setSuccessMsg] = useState('');

  const showError = (msg: string) => { setErrorMsg(msg); setSuccessMsg(''); setViewState('error'); };
  const showSuccess = (msg: string) => { setSuccessMsg(msg); setErrorMsg(''); setViewState('success'); };

  const handleForgotPassword = async () => {
    if (viewState === 'loading') return;
    if (!email.trim()) { showError('Please enter your email address.'); return; }
    if (!isValidEmail(email)) { showError('Please enter a valid email address.'); return; }

    setErrorMsg('');
    setViewState('loading');

    try {
      const redirectUrl = makeRedirectUri({ path: 'auth' });
      const { error } = await resetPassword(email.trim(), redirectUrl);
      
      setViewState('success'); // default, overridden below if error

      if (error) {
        showError(error.message);
      } else {
        showSuccess('📧 Password reset link sent! Check your inbox.');
      }
    } catch (e: unknown) {
      const msg = e instanceof Error ? e.message : String(e);
      AppLogger.error('AuthFormForgotPassword', 'Password reset exception', { error: msg , payload_size: 0, ssi: 0 });
      showError('A network or internal error occurred. Please try again.');
    }
  };

  return (
    <View style={styles.formContainer}>
      <TextInput
        style={styles.input}
        placeholder="email@address.com"
        placeholderTextColor={Colors.textMuted}
        value={email}
        onChangeText={t => { setEmail(t); setErrorMsg(''); }}
        autoCapitalize="none"
        keyboardType="email-address"
        autoComplete="email"
      />

      {viewState === 'error' && !!errorMsg && (
        <View style={styles.errorBanner}>
          <MaterialCommunityIcons name="alert-circle-outline" size={15} color="#FF6B6B" style={{ marginRight: Spacing.sm }} />
          <Text style={styles.errorBannerText}>{errorMsg}</Text>
        </View>
      )}

      {viewState === 'success' && !!successMsg && (
        <View style={styles.successBanner}>
          <MaterialCommunityIcons name="check-circle-outline" size={15} color="#4ADE80" style={{ marginRight: Spacing.sm }} />
          <Text style={styles.successBannerText}>{successMsg}</Text>
        </View>
      )}

      <TouchableOpacity
        style={styles.primaryButton}
        disabled={viewState === 'loading'}
        onPress={handleForgotPassword}
      >
        {viewState === 'loading' ? (
          <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.md }}>
            <ActivityIndicator color="#000" size="small" />
            <Text style={styles.primaryButtonText}>Loading...</Text>
          </View>
        ) : (
          <Text style={styles.primaryButtonText}>Send Reset Link</Text>
        )}
      </TouchableOpacity>

      <View style={styles.toggleContainer}>
        <Text style={styles.toggleText}>Back to </Text>
        <TouchableOpacity onPress={() => onModeChange('LOGIN')}>
          <Text style={styles.toggleLink}>Log In</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}
