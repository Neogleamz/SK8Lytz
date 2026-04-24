import React, { useState } from 'react';
import { ActivityIndicator, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useTheme } from '../../context/ThemeContext';
import { supabase } from '../../services/supabaseClient';
import { Spacing } from '../../theme/theme';
import { useAuthStyles } from './AuthStyles';

const STORAGE_REMEMBER_CREDS = '@Sk8lytz_remember_creds';
const STORAGE_OFFLINE_SKIP = '@Sk8lytz_offline_skip';

const isValidEmail = (str: string) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(str.trim());

interface AuthFormSignInProps {
  initialEmail: string;
  initialRememberMe: boolean;
  onAuthSuccess: () => void;
  onModeChange: (mode: 'SIGNUP' | 'FORGOT_PASSWORD' | 'MAGIC_LINK') => void;
}

export function AuthFormSignIn({ initialEmail, initialRememberMe, onAuthSuccess, onModeChange }: AuthFormSignInProps) {
  const { Colors } = useTheme();
  const styles = useAuthStyles();

  const [email, setEmail] = useState(initialEmail);
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [rememberMe, setRememberMe] = useState(initialRememberMe);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const showError = (msg: string) => setErrorMessage(msg);

  const handleSignIn = async () => {
    const input = email.trim();

    if (!input) { showError('Please enter your email or username.'); return; }
    if (!password) { showError('Please enter your password.'); return; }

    if (input.includes('@') && !isValidEmail(input)) {
      showError('Please enter a valid email address (e.g. you@example.com).');
      return;
    }

    setErrorMessage('');
    setLoading(true);

    let loginEmail = input;

    if (!input.includes('@')) {
      try {
        const { data, error: rpcErr } = await supabase.rpc('get_email_by_username', { p_username: input });
        if (rpcErr || !data) {
          setLoading(false);
          showError('No account found with that username. Try signing in with your email instead.');
          return;
        }
        loginEmail = data as string;
      } catch {
        setLoading(false);
        showError('Could not look up username. Please use your email to sign in.');
        return;
      }
    }

    const { error } = await supabase.auth.signInWithPassword({ email: loginEmail, password });
    setLoading(false);

    if (error) {
      const msg = error.message.toLowerCase().includes('invalid login')
        ? 'Incorrect email or password. Please try again.'
        : error.message;
      showError(msg);
    } else {
      setErrorMessage('');
      if (rememberMe) {
        await AsyncStorage.setItem(STORAGE_REMEMBER_CREDS, JSON.stringify({ email: loginEmail, rememberMe: true }));
      } else {
        await AsyncStorage.setItem(STORAGE_REMEMBER_CREDS, JSON.stringify({ email: loginEmail, rememberMe: false }));
      }
      await AsyncStorage.removeItem(STORAGE_OFFLINE_SKIP);
      onAuthSuccess();
    }
  };

  return (
    <View style={styles.formContainer}>
      <TextInput
        style={styles.input}
        placeholder="Email or username"
        placeholderTextColor={Colors.textMuted}
        value={email}
        onChangeText={t => { setEmail(t); setErrorMessage(''); }}
        autoCapitalize="none"
        keyboardType="default"
        autoComplete="off"
      />

      <View style={{ position: 'relative' }}>
        <TextInput
          style={[styles.input, { paddingRight: Spacing.huge }]}
          placeholder="Password"
          placeholderTextColor={Colors.textMuted}
          value={password}
          onChangeText={t => { setPassword(t); setErrorMessage(''); }}
          secureTextEntry={!showPassword}
          autoCapitalize="none"
        />
        <TouchableOpacity
          onPress={() => setShowPassword(v => !v)}
          style={{ position: 'absolute', right: 14, top: 14 }}
        >
          <Text style={{ color: Colors.textMuted, fontSize: 12 }}>{showPassword ? 'HIDE' : 'SHOW'}</Text>
        </TouchableOpacity>
      </View>

      <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: Spacing.lg, marginTop: -4 }}>
        <TouchableOpacity
          onPress={() => setRememberMe(v => !v)}
          style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}
          activeOpacity={0.7}
        >
          <View style={[
            styles.checkbox,
            rememberMe && { backgroundColor: Colors.primary, borderColor: Colors.primary }
          ]}>
            {rememberMe && <MaterialCommunityIcons name="check" size={12} color="#000" />}
          </View>
          <Text style={{ color: Colors.textMuted, fontSize: 13 }}>Remember my email</Text>
        </TouchableOpacity>
        <TouchableOpacity onPress={() => onModeChange('FORGOT_PASSWORD')}>
          <Text style={styles.forgotPasswordText}>Forgot Password?</Text>
        </TouchableOpacity>
      </View>

      {!!errorMessage && (
        <View style={styles.errorBanner}>
          <MaterialCommunityIcons name="alert-circle-outline" size={15} color="#FF6B6B" style={{ marginRight: Spacing.sm }} />
          <Text style={styles.errorBannerText}>{errorMessage}</Text>
        </View>
      )}

      <TouchableOpacity
        style={styles.primaryButton}
        disabled={loading}
        onPress={handleSignIn}
      >
        {loading ? (
          <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.md }}>
            <ActivityIndicator color="#000" size="small" />
            <Text style={styles.primaryButtonText}>Loading...</Text>
          </View>
        ) : (
          <Text style={styles.primaryButtonText}>Log In</Text>
        )}
      </TouchableOpacity>

      <View style={styles.toggleContainer}>
        <Text style={styles.toggleText}>Don't have an account? </Text>
        <TouchableOpacity onPress={() => onModeChange('SIGNUP')}>
          <Text style={styles.toggleLink}>Sign Up</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}
