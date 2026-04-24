import React, { useState } from 'react';
import { ActivityIndicator, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { makeRedirectUri } from 'expo-auth-session';
import { useTheme } from '../../context/ThemeContext';
import { supabase } from '../../services/supabaseClient';
import { Spacing } from '../../theme/theme';
import { useAuthStyles } from './AuthStyles';

const isValidEmail = (str: string) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(str.trim());

interface AuthFormForgotPasswordProps {
  onModeChange: (mode: 'LOGIN' | 'SIGNUP') => void;
}

export function AuthFormForgotPassword({ onModeChange }: AuthFormForgotPasswordProps) {
  const { Colors } = useTheme();
  const styles = useAuthStyles();

  const [email, setEmail] = useState('');
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  const showError = (msg: string) => { setErrorMessage(msg); setSuccessMessage(''); };
  const showSuccess = (msg: string) => { setSuccessMessage(msg); setErrorMessage(''); };

  const handleForgotPassword = async () => {
    if (!email.trim()) { showError('Please enter your email address.'); return; }
    if (!isValidEmail(email)) { showError('Please enter a valid email address.'); return; }

    setErrorMessage('');
    setLoading(true);

    const redirectUrl = makeRedirectUri({ path: 'auth' });
    const { error } = await supabase.auth.resetPasswordForEmail(email.trim(), { redirectTo: redirectUrl });
    
    setLoading(false);

    if (error) {
      showError(error.message);
    } else {
      showSuccess('📧 Password reset link sent! Check your inbox.');
    }
  };

  return (
    <View style={styles.formContainer}>
      <TextInput
        style={styles.input}
        placeholder="email@address.com"
        placeholderTextColor={Colors.textMuted}
        value={email}
        onChangeText={t => { setEmail(t); setErrorMessage(''); }}
        autoCapitalize="none"
        keyboardType="email-address"
        autoComplete="email"
      />

      {!!errorMessage && (
        <View style={styles.errorBanner}>
          <MaterialCommunityIcons name="alert-circle-outline" size={15} color="#FF6B6B" style={{ marginRight: Spacing.sm }} />
          <Text style={styles.errorBannerText}>{errorMessage}</Text>
        </View>
      )}

      {!!successMessage && (
        <View style={styles.successBanner}>
          <MaterialCommunityIcons name="check-circle-outline" size={15} color="#4ADE80" style={{ marginRight: Spacing.sm }} />
          <Text style={styles.successBannerText}>{successMessage}</Text>
        </View>
      )}

      <TouchableOpacity
        style={styles.primaryButton}
        disabled={loading}
        onPress={handleForgotPassword}
      >
        {loading ? (
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
