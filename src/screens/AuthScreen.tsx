import React, { useState, useEffect, useRef } from 'react';
import {
  View, Text, TextInput, TouchableOpacity, StyleSheet, Alert,
  ActivityIndicator, KeyboardAvoidingView, Platform, ScrollView, Animated
} from 'react-native';
import { supabase } from '../services/supabaseClient';
import { Typography, Layout } from '../theme/theme';
import { useTheme } from '../context/ThemeContext';
import {
  checkPasswordComplexity,
  isCommonPassword,
  checkHIBP,
  containsProfanity,
  PasswordStrength
} from '../services/AuthUtils';

type AuthMode = 'LOGIN' | 'SIGNUP' | 'FORGOT_PASSWORD' | 'MAGIC_LINK';

export default function AuthScreen({ onAuthSuccess, onOfflineMode }: { onAuthSuccess: () => void; onOfflineMode?: () => void }) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [username, setUsername] = useState('');
  const [loading, setLoading] = useState(false);
  const [hibpChecking, setHibpChecking] = useState(false);
  const [mode, setMode] = useState<AuthMode>('LOGIN');
  const [showPassword, setShowPassword] = useState(false);
  const [magicSent, setMagicSent] = useState(false);
  const [passwordStrength, setPasswordStrength] = useState<PasswordStrength | null>(null);
  const strengthAnim = useRef(new Animated.Value(0)).current;


  // ─── MAGIC LINK ─────────────────────────────────────────────────────────
  useEffect(() => {
    if ((mode === 'SIGNUP') && password.length > 0) {
      const s = checkPasswordComplexity(password);
      setPasswordStrength(s);
      Animated.timing(strengthAnim, {
        toValue: s.score / 4,
        duration: 200,
        useNativeDriver: false,
      }).start();
    } else {
      setPasswordStrength(null);
    }
  }, [password, mode]);


  // ─── MAGIC LINK ─────────────────────────────────────────────────────────────
  const handleMagicLink = async () => {
    if (!email.trim()) {
      Alert.alert('Email Required', 'Enter your email to receive a magic link.');
      return;
    }
    setLoading(true);
    const { error } = await supabase.auth.signInWithOtp({
      email: email.trim(),
      options: { emailRedirectTo: 'sk8lytz://auth' },
    });
    setLoading(false);
    if (error) {
      Alert.alert('Error', error.message);
    } else {
      setMagicSent(true);
    }
  };

  // ─── SIGN IN ─────────────────────────────────────────────────────────────────
  const handleSignIn = async () => {
    if (!email.trim() || !password) {
      Alert.alert('Missing Fields', 'Please enter your email and password.');
      return;
    }
    setLoading(true);
    const { error } = await supabase.auth.signInWithPassword({ email: email.trim(), password });
    setLoading(false);
    if (error) Alert.alert('Sign In Failed', error.message);
    else onAuthSuccess();
  };

  // ─── SIGN UP ─────────────────────────────────────────────────────────────────
  const handleSignUp = async () => {
    if (!username.trim()) { Alert.alert('Missing Field', 'Please enter a username.'); return; }
    if (!email.trim()) { Alert.alert('Missing Field', 'Please enter your email.'); return; }

    // Username profanity check
    if (containsProfanity(username)) {
      Alert.alert('Invalid Username', 'Your username contains inappropriate language. Please choose a different name.');
      return;
    }

    // Complexity check
    const strength = checkPasswordComplexity(password);
    if (strength.errors.length > 0) {
      Alert.alert('Weak Password', 'Your password must include:\n• ' + strength.errors.join('\n• '));
      return;
    }
    // Common password check
    if (isCommonPassword(password)) {
      Alert.alert('Password Too Common', 'This password is in the list of most commonly used passwords. Please choose something more unique.');
      return;
    }
    // HIBP check
    setHibpChecking(true);
    const hibp = await checkHIBP(password);
    setHibpChecking(false);
    if (hibp.pwned) {
      Alert.alert(
        '⚠️ Compromised Password',
        `This exact password has appeared in ${hibp.count.toLocaleString()} data breaches. Please choose a different password.`,
        [{ text: 'Choose Another', style: 'default' }]
      );
      return;
    }

    setLoading(true);
    const { error } = await supabase.auth.signUp({
      email: email.trim(),
      password,
      options: { data: { username: username.trim() } },
    });
    setLoading(false);
    if (error) Alert.alert('Sign Up Failed', error.message);
    else {
      Alert.alert('✅ Account Created', 'Check your email for a verification link, then log in.');
      setMode('LOGIN');
    }
  };

  // ─── FORGOT PASSWORD ─────────────────────────────────────────────────────────
  const handleForgotPassword = async () => {
    if (!email.trim()) {
      Alert.alert('Email Required', 'Enter your email to reset your password.');
      return;
    }
    setLoading(true);
    const { error } = await supabase.auth.resetPasswordForEmail(email.trim(), { redirectTo: 'sk8lytz://auth' });
    setLoading(false);
    if (error) Alert.alert('Error', error.message);
    else {
      Alert.alert('Email Sent', 'Password reset instructions have been sent to your inbox.');
      setMode('LOGIN');
    }
  };

  const resetState = (newMode: AuthMode) => {
    setMode(newMode);
    setMagicSent(false);
    setPasswordStrength(null);
  };

  // ─── MAGIC LINK SENT STATE ───────────────────────────────────────────────────
  if (mode === 'MAGIC_LINK' && magicSent) {
    return (
      <View style={[styles.container, { justifyContent: 'center', alignItems: 'center', padding: 32 }]}>
        <Text style={{ fontSize: 60, marginBottom: 20 }}>📬</Text>
        <Text style={[styles.title, { marginBottom: 12 }]}>Check Your Email</Text>
        <Text style={{ color: Colors.textMuted, textAlign: 'center', fontSize: 15, lineHeight: 22, marginBottom: 32 }}>
          We sent a magic link to{'\n'}<Text style={{ color: Colors.primary, fontWeight: 'bold' }}>{email}</Text>{'\n\n'}Tap the link in your email to sign in — no password needed.
        </Text>
        <TouchableOpacity style={styles.secondaryButton} onPress={() => resetState('LOGIN')}>
          <Text style={styles.secondaryButtonText}>Back to Sign In</Text>
        </TouchableOpacity>
      </View>
    );
  }

  const strengthBarWidth = strengthAnim.interpolate({ inputRange: [0, 1], outputRange: ['0%', '100%'] });

  return (
    <KeyboardAvoidingView style={styles.container} behavior={Platform.OS === 'ios' ? 'padding' : undefined}>
      <ScrollView contentContainerStyle={styles.scrollContent} keyboardShouldPersistTaps="handled">

        {/* Logo */}
        <View style={styles.headerContainer}>
          <Text style={styles.title}>SK8<Text style={{ color: Colors.primary }}>Lytz</Text></Text>
          <Text style={styles.subtitle}>
            {mode === 'LOGIN' ? 'Welcome back. Sync your fleet.' :
             mode === 'SIGNUP' ? 'Create an account to backup your setups.' :
             mode === 'MAGIC_LINK' ? 'Sign in without a password.' :
             'Reset your password'}
          </Text>
        </View>

        <View style={styles.formContainer}>

          {/* Username (signup only) */}
          {mode === 'SIGNUP' && (
            <TextInput
              style={styles.input}
              placeholder="Username"
              placeholderTextColor={Colors.textMuted}
              value={username}
              onChangeText={setUsername}
              autoCapitalize="none"
            />
          )}

          {/* Email */}
          <TextInput
            style={styles.input}
            placeholder="email@address.com"
            placeholderTextColor={Colors.textMuted}
            value={email}
            onChangeText={setEmail}
            autoCapitalize="none"
            keyboardType="email-address"
          />

          {/* Password (not on magic link, not on forgot) */}
          {(mode === 'LOGIN' || mode === 'SIGNUP') && (
            <View>
              <View style={{ position: 'relative' }}>
                <TextInput
                  style={[styles.input, { paddingRight: 50 }]}
                  placeholder="Password"
                  placeholderTextColor={Colors.textMuted}
                  value={password}
                  onChangeText={setPassword}
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

              {/* Strength bar (signup only) */}
              {mode === 'SIGNUP' && passwordStrength && (
                <View style={{ marginBottom: 12 }}>
                  <View style={{ height: 4, backgroundColor: 'rgba(255,255,255,0.08)', borderRadius: 2, overflow: 'hidden', marginBottom: 6 }}>
                    <Animated.View style={{ height: '100%', width: strengthBarWidth, backgroundColor: passwordStrength.color, borderRadius: 2 }} />
                  </View>
                  <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' }}>
                    <Text style={{ color: passwordStrength.color, fontSize: 11, fontWeight: 'bold' }}>{passwordStrength.label}</Text>
                    {passwordStrength.errors.length > 0 && (
                      <Text style={{ color: Colors.textMuted, fontSize: 10 }}>Missing: {passwordStrength.errors[0]}{passwordStrength.errors.length > 1 ? ` +${passwordStrength.errors.length - 1}` : ''}</Text>
                    )}
                  </View>
                </View>
              )}
            </View>
          )}

          {/* Forgot password link (login only) */}
          {mode === 'LOGIN' && (
            <TouchableOpacity onPress={() => resetState('FORGOT_PASSWORD')} style={styles.forgotPasswordContainer}>
              <Text style={styles.forgotPasswordText}>Forgot Password?</Text>
            </TouchableOpacity>
          )}

          {/* Primary action button */}
          <TouchableOpacity
            style={styles.primaryButton}
            disabled={loading || hibpChecking}
            onPress={
              mode === 'LOGIN' ? handleSignIn :
              mode === 'SIGNUP' ? handleSignUp :
              mode === 'MAGIC_LINK' ? handleMagicLink :
              handleForgotPassword
            }
          >
            {loading || hibpChecking ? (
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: 10 }}>
                <ActivityIndicator color="#000" size="small" />
                <Text style={styles.primaryButtonText}>{hibpChecking ? 'Checking Security...' : 'Loading...'}</Text>
              </View>
            ) : (
              <Text style={styles.primaryButtonText}>
                {mode === 'LOGIN' ? 'Log In' :
                 mode === 'SIGNUP' ? 'Create Account' :
                 mode === 'MAGIC_LINK' ? 'Send Magic Link ✨' :
                 'Send Reset Link'}
              </Text>
            )}
          </TouchableOpacity>

          {/* Magic link option — disabled */}
          {/* {mode === 'LOGIN' && (
            <TouchableOpacity onPress={() => resetState('MAGIC_LINK')} style={styles.magicLinkButton}>
              <Text style={styles.magicLinkText}>✨ Sign in with Magic Link instead</Text>
            </TouchableOpacity>
          )} */}

          {/* Toggle login / signup */}
          <View style={styles.toggleContainer}>
            <Text style={styles.toggleText}>
              {mode === 'LOGIN' ? "Don't have an account? " :
               mode === 'SIGNUP' ? "Already have an account? " :
               'Back to '}
            </Text>
            <TouchableOpacity onPress={() => resetState(mode === 'SIGNUP' ? 'LOGIN' : 'SIGNUP')}>
              <Text style={styles.toggleLink}>
                {mode === 'LOGIN' ? 'Sign Up' : 'Log In'}
              </Text>
            </TouchableOpacity>
          </View>
        </View>


        {/* Offline mode option */}
        {(mode === 'LOGIN' || mode === 'MAGIC_LINK') && onOfflineMode && (
          <TouchableOpacity
            onPress={onOfflineMode}
            style={styles.offlineButton}
          >
            <Text style={styles.offlineButtonText}>📵 Continue Offline</Text>
            <Text style={styles.offlineButtonSub}>No account needed · Cloud sync disabled</Text>
          </TouchableOpacity>
        )}

        {/* HIBP attribution */}
        {mode === 'SIGNUP' && (
          <Text style={{ color: Colors.textMuted, fontSize: 10, textAlign: 'center', marginTop: 8, paddingHorizontal: 24 }}>
            🔒 Passwords are checked against HaveIBeenPwned's breach database using k-anonymity. Your password is never transmitted.
          </Text>
        )}

      </ScrollView>
    </KeyboardAvoidingView>
  );
}

const createStyles = (Colors: any) => StyleSheet.create({
  container: { flex: 1, backgroundColor: Colors.background },
  scrollContent: { flexGrow: 1, justifyContent: 'center', padding: 24 },
  headerContainer: { alignItems: 'center', marginBottom: 40 },
  title: { fontSize: 42, fontWeight: '900', color: Colors.text, letterSpacing: -1, marginBottom: 8 },
  subtitle: { color: Colors.textMuted, fontSize: 14, textAlign: 'center' },
  formContainer: { marginBottom: 24 },
  input: {
    backgroundColor: Colors.surface,
    color: Colors.text,
    padding: 14,
    borderRadius: Layout.borderRadius,
    fontSize: 15,
    marginBottom: 12,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
  },
  forgotPasswordContainer: { alignItems: 'flex-end', marginBottom: 16, marginTop: -4 },
  forgotPasswordText: { color: Colors.primary, fontSize: 13 },
  primaryButton: {
    backgroundColor: Colors.primary,
    padding: 16,
    borderRadius: Layout.borderRadius,
    alignItems: 'center',
    marginBottom: 12,
  },
  primaryButtonText: { color: '#000', fontWeight: 'bold', fontSize: 16 },
  secondaryButton: {
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
    padding: 14,
    borderRadius: Layout.borderRadius,
    alignItems: 'center',
  },
  secondaryButtonText: { color: Colors.text, fontWeight: 'bold', fontSize: 15 },
  magicLinkButton: {
    alignItems: 'center',
    paddingVertical: 12,
    marginBottom: 4,
  },
  magicLinkText: { color: Colors.primary, fontSize: 13, fontWeight: '600' },
  toggleContainer: { flexDirection: 'row', justifyContent: 'center', marginTop: 8 },
  toggleText: { color: Colors.textMuted, fontSize: 14 },
  toggleLink: { color: Colors.primary, fontSize: 14, fontWeight: 'bold' },
  oauthContainer: { marginTop: 8 },
  dividerContainer: { flexDirection: 'row', alignItems: 'center', marginBottom: 16 },
  divider: { flex: 1, height: 1, backgroundColor: Colors.surfaceHighlight },
  dividerText: { color: Colors.textMuted, fontSize: 10, fontWeight: 'bold', marginHorizontal: 12 },
  oauthButtonGroup: { flexDirection: 'row', gap: 10 },
  oauthButton: {
    flex: 1,
    backgroundColor: Colors.surface,
    padding: 13,
    borderRadius: Layout.borderRadius,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
  },
  oauthButtonText: { color: Colors.text, fontWeight: 'bold', fontSize: 13 },
  offlineButton: {
    alignItems: 'center',
    marginTop: 20,
    paddingVertical: 14,
    borderRadius: Layout.borderRadius,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
    backgroundColor: 'rgba(255,255,255,0.04)',
  },
  offlineButtonText: { color: Colors.textMuted, fontSize: 14, fontWeight: '600', marginBottom: 3 },
  offlineButtonSub: { color: Colors.textMuted, fontSize: 11, opacity: 0.7 },
});
