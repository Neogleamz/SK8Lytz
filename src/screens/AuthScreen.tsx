import React, { useState, useEffect, useRef } from 'react';
import {
  View, Text, TextInput, TouchableOpacity, StyleSheet, Alert,
  ActivityIndicator, KeyboardAvoidingView, Platform, ScrollView, Animated, Linking
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { supabase } from '../services/supabaseClient';
import { Typography, Layout } from '../theme/theme';
import { useTheme } from '../context/ThemeContext';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import {
  checkPasswordComplexity,
  isCommonPassword,
  checkHIBP,
  containsProfanity,
  PasswordStrength
} from '../services/AuthUtils';

const STORAGE_LAST_EMAIL = 'ng_auth_last_email';

type AuthMode = 'LOGIN' | 'SIGNUP' | 'FORGOT_PASSWORD' | 'MAGIC_LINK';

/** Simple email format validator */
const isValidEmail = (str: string) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(str.trim());

/** Show help info — uses Linking on web, Alert on native */
const showHelp = () => {
  if (Platform.OS === 'web') {
    // On web, open support page rather than a blocking window.alert()
    Linking.openURL('mailto:support@sk8lytz.com?subject=SK8Lytz%20Support');
  } else {
    Alert.alert(
      'SK8Lytz Support',
      'Need help?\n\n• Email: support@sk8lytz.com\n• Docs: sk8lytz.com/help\n• Discord: discord.gg/sk8lytz'
    );
  }
};

export default function AuthScreen({ onAuthSuccess, onOfflineMode }: { onAuthSuccess: () => void; onOfflineMode?: () => void }) {
  const { Colors, toggleTheme, isDark } = useTheme();
  const insets = useSafeAreaInsets();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [username, setUsername] = useState('');
  const [loading, setLoading] = useState(false);
  const [hibpChecking, setHibpChecking] = useState(false);
  const [mode, setMode] = useState<AuthMode>('LOGIN');
  const [showPassword, setShowPassword] = useState(false);
  const [magicSent, setMagicSent] = useState(false);
  const [passwordStrength, setPasswordStrength] = useState<PasswordStrength | null>(null);
  const [errorMessage, setErrorMessage] = useState('');   // << inline error state
  const [successMessage, setSuccessMessage] = useState(''); // << inline success state
  const strengthAnim = useRef(new Animated.Value(0)).current;

  // Derive styles reactively from Colors so theme toggle re-renders instantly
  const styles = createStyles(Colors, insets);

  // ─── Load saved email on mount ───────────────────────────────────────────────
  useEffect(() => {
    AsyncStorage.getItem(STORAGE_LAST_EMAIL).then(saved => {
      if (saved) setEmail(saved);
    });
  }, []);

  // ─── Clear inline messages when mode changes ─────────────────────────────────
  useEffect(() => {
    setErrorMessage('');
    setSuccessMessage('');
  }, [mode]);

  // ─── Password strength meter ─────────────────────────────────────────────────
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

  /** Show an error inline — replaces Alert.alert() for validation/auth errors */
  const showError = (msg: string) => {
    setErrorMessage(msg);
    setSuccessMessage('');
  };

  /** Show a success message inline */
  const showSuccess = (msg: string) => {
    setSuccessMessage(msg);
    setErrorMessage('');
  };

  // ─── MAGIC LINK ─────────────────────────────────────────────────────────────
  const handleMagicLink = async () => {
    if (!email.trim()) { showError('Enter your email to receive a magic link.'); return; }
    if (!isValidEmail(email)) { showError('Please enter a valid email address.'); return; }
    setLoading(true);
    const { error } = await supabase.auth.signInWithOtp({
      email: email.trim(),
      options: { emailRedirectTo: 'sk8lytz://auth' },
    });
    setLoading(false);
    if (error) {
      showError(error.message);
    } else {
      setMagicSent(true);
    }
  };

  // ─── SIGN IN ─────────────────────────────────────────────────────────────────
  const handleSignIn = async () => {
    const input = email.trim();

    // FIX 1.1.2 + 1.2.2: Client-side validation with inline error
    if (!input) { showError('Please enter your email or username.'); return; }
    if (!password) { showError('Please enter your password.'); return; }

    // If it looks like an email, validate the format
    if (input.includes('@') && !isValidEmail(input)) {
      showError('Please enter a valid email address (e.g. you@example.com).');
      return;
    }

    setErrorMessage('');
    setLoading(true);

    let loginEmail = input;

    // If input has no '@', treat it as username and look up the email
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

    // FIX 1.1.3: Display Supabase auth error inline instead of Alert
    const { error } = await supabase.auth.signInWithPassword({ email: loginEmail, password });
    setLoading(false);
    if (error) {
      // Make auth errors more user-friendly
      const msg = error.message.toLowerCase().includes('invalid login')
        ? 'Incorrect email or password. Please try again.'
        : error.message;
      showError(msg);
    } else {
      setErrorMessage('');
      await AsyncStorage.setItem(STORAGE_LAST_EMAIL, loginEmail);
      onAuthSuccess();
    }
  };

  // ─── SIGN UP ─────────────────────────────────────────────────────────────────
  const handleSignUp = async () => {
    // FIX 1.2.2: All validation errors shown inline
    if (!username.trim()) { showError('Please enter a username.'); return; }
    if (!email.trim()) { showError('Please enter your email.'); return; }
    if (!isValidEmail(email)) { showError('Please enter a valid email address.'); return; }
    if (!password) { showError('Please enter a password.'); return; }

    if (containsProfanity(username)) {
      showError('Your username contains inappropriate language. Please choose a different name.');
      return;
    }

    const strength = checkPasswordComplexity(password);
    if (strength.errors.length > 0) {
      showError('Your password must include:\n• ' + strength.errors.join('\n• '));
      return;
    }
    if (isCommonPassword(password)) {
      showError('This password is too common. Please choose something more unique.');
      return;
    }

    setErrorMessage('');
    setHibpChecking(true);
    const hibp = await checkHIBP(password);
    setHibpChecking(false);

    if (hibp.pwned) {
      showError(`⚠️ This password has appeared in ${hibp.count.toLocaleString()} data breaches. Please choose a different password.`);
      return;
    }

    setLoading(true);
    const { error } = await supabase.auth.signUp({
      email: email.trim(),
      password,
      options: { data: { username: username.trim() } },
    });
    setLoading(false);
    if (error) {
      showError(error.message);
    } else {
      showSuccess('✅ Account created! Check your email for a verification link, then log in.');
      setTimeout(() => setMode('LOGIN'), 3000);
    }
  };

  // ─── FORGOT PASSWORD ─────────────────────────────────────────────────────────
  const handleForgotPassword = async () => {
    // FIX 1.3.2: Blank email guard shown inline
    if (!email.trim()) { showError('Please enter your email address.'); return; }
    if (!isValidEmail(email)) { showError('Please enter a valid email address.'); return; }
    setErrorMessage('');
    setLoading(true);
    const { error } = await supabase.auth.resetPasswordForEmail(email.trim(), { redirectTo: 'sk8lytz://auth' });
    setLoading(false);
    if (error) {
      showError(error.message);
    } else {
      showSuccess('📧 Password reset link sent! Check your inbox.');
    }
  };

  const resetState = (newMode: AuthMode) => {
    setMode(newMode);
    setMagicSent(false);
    setPasswordStrength(null);
    setErrorMessage('');
    setSuccessMessage('');
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
      {/* FIX 1.1.5: Theme toggle — styles now computed from Colors in render, so toggle re-renders correctly */}
      {/* FIX 1.1.6: Help button uses web-safe showHelp() instead of Alert.alert() */}
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
              onChangeText={t => { setUsername(t); setErrorMessage(''); }}
              autoCapitalize="none"
            />
          )}

          {/* Email or Username */}
          <TextInput
            style={styles.input}
            placeholder={mode === 'LOGIN' ? 'Email or username' : 'email@address.com'}
            placeholderTextColor={Colors.textMuted}
            value={email}
            onChangeText={t => { setEmail(t); setErrorMessage(''); }}
            autoCapitalize="none"
            keyboardType={mode === 'LOGIN' ? 'default' : 'email-address'}
            autoComplete={mode === 'LOGIN' ? 'off' : 'email'}
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

          {/* ── Inline error message (replaces Alert.alert for all modes) ── */}
          {!!errorMessage && (
            <View style={styles.errorBanner}>
              <MaterialCommunityIcons name="alert-circle-outline" size={15} color="#FF6B6B" style={{ marginRight: 6 }} />
              <Text style={styles.errorBannerText}>{errorMessage}</Text>
            </View>
          )}

          {/* ── Inline success message ── */}
          {!!successMessage && (
            <View style={styles.successBanner}>
              <MaterialCommunityIcons name="check-circle-outline" size={15} color="#4ADE80" style={{ marginRight: 6 }} />
              <Text style={styles.successBannerText}>{successMessage}</Text>
            </View>
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

const createStyles = (Colors: any, insets: { top: number; bottom: number; left: number; right: number }) => StyleSheet.create({
  container: { flex: 1, backgroundColor: Colors.background },
  scrollContent: {
    flexGrow: 1, justifyContent: 'center',
    paddingHorizontal: 24,
    paddingTop: Math.max(insets.top + 60, 80),
    paddingBottom: 24,
  },
  topButtons: {
    position: 'absolute', top: Math.max(insets.top + 10, 20), right: 16,
    flexDirection: 'row', gap: 8, zIndex: 10,
  },
  topBtn: {
    width: 34, height: 34, borderRadius: 17,
    backgroundColor: 'rgba(255,255,255,0.07)',
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)',
    alignItems: 'center', justifyContent: 'center',
  },
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
  errorBanner: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    backgroundColor: 'rgba(255, 107, 107, 0.12)',
    borderWidth: 1,
    borderColor: 'rgba(255, 107, 107, 0.3)',
    borderRadius: 10,
    paddingHorizontal: 12,
    paddingVertical: 10,
    marginBottom: 12,
  },
  errorBannerText: {
    color: '#FF6B6B',
    fontSize: 13,
    lineHeight: 18,
    flex: 1,
  },
  successBanner: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    backgroundColor: 'rgba(74, 222, 128, 0.12)',
    borderWidth: 1,
    borderColor: 'rgba(74, 222, 128, 0.3)',
    borderRadius: 10,
    paddingHorizontal: 12,
    paddingVertical: 10,
    marginBottom: 12,
  },
  successBannerText: {
    color: '#4ADE80',
    fontSize: 13,
    lineHeight: 18,
    flex: 1,
  },
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
