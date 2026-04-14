import { MaterialCommunityIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import React, { useEffect, useRef, useState } from 'react';
import {
    ActivityIndicator,
    Alert,
    Animated,
    Image,
    KeyboardAvoidingView,
    Linking,
    Platform,
    StyleSheet,
    Text, TextInput, TouchableOpacity,
    View
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { makeRedirectUri } from 'expo-auth-session';
import EulaModal from '../components/modals/EulaModal';
import { useTheme } from '../context/ThemeContext';
import {
    checkHIBP,
    checkPasswordComplexity,
    containsProfanity,
    isCommonPassword,
    PasswordStrength
} from '../services/AuthUtils';
import { supabase } from '../services/supabaseClient';
import { Layout, Spacing, ThemePalette } from '../theme/theme';

const STORAGE_LAST_EMAIL    = '@Sk8lytz_auth_last_email';
const STORAGE_REMEMBER_CREDS = '@Sk8lytz_remember_creds';  // { email, rememberMe }
const STORAGE_OFFLINE_SKIP  = '@Sk8lytz_offline_skip';     // 'true' if user chose Continue Offline

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
  const [mode, setMode] = useState<AuthMode>('SIGNUP'); // Default FTUE to Sign Up
  const [showPassword, setShowPassword] = useState(false);
  const [magicSent, setMagicSent] = useState(false);
  const [passwordStrength, setPasswordStrength] = useState<PasswordStrength | null>(null);
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [rememberMe, setRememberMe] = useState(false);
  const [rememberOffline, setRememberOffline] = useState(false);
  const [isSandboxEnabled, setIsSandboxEnabled] = useState(false);
  const [eulaAccepted, setEulaAccepted] = useState(false);
  const [showEulaModal, setShowEulaModal] = useState(false);
  const strengthAnim = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    AsyncStorage.getItem('@Sk8lytz_demo_mode').then(val => {
      setIsSandboxEnabled(val === 'true');
    });
  }, []);

  // Derive styles reactively from Colors so theme toggle re-renders instantly
  const styles = createStyles(Colors, insets);

  // ─── Load saved credentials / remember-me on mount ──────────────────────────────────────
  useEffect(() => {
    AsyncStorage.getItem(STORAGE_REMEMBER_CREDS).then(raw => {
      if (raw) {
        try {
          const saved = JSON.parse(raw);
          if (saved.email) {
             // If we have any history of this user, default back to Login mode
             setMode('LOGIN');
          }
          if (saved.rememberMe) {
            setEmail(saved.email || '');
            setRememberMe(true);
          } else {
            // Only pre-fill email, not password
            setEmail(saved.email || '');
          }
        } catch {}
      } else {
        // Fall back to legacy last-email key
        AsyncStorage.getItem(STORAGE_LAST_EMAIL).then(saved => {
          if (saved) {
             setEmail(saved);
             setMode('LOGIN'); // Same logic, they are returning if they have an email saved
          }
        });
      }
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
    const redirectUrl = makeRedirectUri({ path: 'auth' });
    const { error } = await supabase.auth.signInWithOtp({
      email: email.trim(),
      options: { emailRedirectTo: redirectUrl },
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
      const msg = error.message.toLowerCase().includes('invalid login')
        ? 'Incorrect email or password. Please try again.'
        : error.message;
      showError(msg);
    } else {
      setErrorMessage('');
      // Save/clear remember-me credentials
      if (rememberMe) {
        await AsyncStorage.setItem(STORAGE_REMEMBER_CREDS, JSON.stringify({ email: loginEmail, rememberMe: true }));
      } else {
        await AsyncStorage.setItem(STORAGE_REMEMBER_CREDS, JSON.stringify({ email: loginEmail, rememberMe: false }));
      }
      // Clear offline skip if logging in properly
      await AsyncStorage.removeItem(STORAGE_OFFLINE_SKIP);
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
    if (!eulaAccepted) { showError('You must accept the End User License Agreement to sign up.'); return; }

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
    const redirectUrl = makeRedirectUri({ path: 'auth' });
    const { error } = await supabase.auth.signUp({
      email: email.trim(),
      password,
      options: { 
        data: { 
          username: username.trim(), 
          display_name: username.trim(), 
          accepted_eula_version: 1 
        },
        emailRedirectTo: redirectUrl
      },
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
    const redirectUrl = makeRedirectUri({ path: 'auth' });
    const { error } = await supabase.auth.resetPasswordForEmail(email.trim(), { redirectTo: redirectUrl });
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
      <View style={[styles.container, { justifyContent: 'center', alignItems: 'center', padding: Spacing.xxl }]}>
        <Text style={{ fontSize: 60, marginBottom: Spacing.xl }}>📬</Text>
        <Text style={[styles.title, { marginBottom: Spacing.md }]}>Check Your Email</Text>
        <Text style={{ color: Colors.textMuted, textAlign: 'center', fontSize: 15, lineHeight: 22, marginBottom: Spacing.xxl }}>
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
      {/* Top Left Sandbox Toggle */}
      {__DEV__ && (
        <View style={styles.topLeftButtons}>
          <TouchableOpacity
            onPress={async () => {
              const nextState = !isSandboxEnabled;
              setIsSandboxEnabled(nextState);
              await AsyncStorage.setItem('@Sk8lytz_demo_mode', String(nextState));
              import('react-native').then(rn => {
                rn.Alert.alert(
                  'Developer Sandbox', 
                  `Virtual Skates & Demo features are now ${nextState ? 'ENABLED' : 'DISABLED'}. Restart Bluetooth or refresh to apply.`
                );
              });
            }}
            style={[styles.topLeftBtn, { 
              borderColor: isSandboxEnabled ? 'rgba(0,255,0,0.5)' : 'rgba(255,255,0,0.5)', 
              backgroundColor: isSandboxEnabled ? 'rgba(0,255,0,0.1)' : 'rgba(255,255,0,0.1)'
            }]}
            activeOpacity={0.7}
          >
            <MaterialCommunityIcons name={isSandboxEnabled ? "checkbox-marked" : "checkbox-blank-outline"} size={14} color={isSandboxEnabled ? "#00FF00" : "#FFE135"} />
            <Text style={{ color: isSandboxEnabled ? '#00FF00' : '#FFE135', fontSize: 11, fontWeight: 'bold' }}>DEV SANDBOX</Text>
          </TouchableOpacity>
        </View>
      )}

      {/* FIX 1.1.5: Theme toggle — styles now computed from Colors in render, so toggle re-renders correctly */}
      {/* FIX 1.1.6: Help button uses web-safe showHelp() instead of Alert.alert() */}
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

        {/* Logo and Welcome */}
        <View style={styles.headerContainer}>
          <Image 
            source={require('../../assets/logo.png')} 
            style={{ width: '80%', maxWidth: 300, height: 80, tintColor: isDark ? '#FFFFFF' : undefined }} 
            resizeMode="contain" 
          />
          <TouchableOpacity 
            onPress={() => Linking.openURL('https://neogleamz.com')}
            style={{ alignSelf: 'flex-end', marginTop: -15, marginRight: '16%', padding: Spacing.xs }}
          >
            <Text style={{ fontSize: 9, fontWeight: '800', color: Colors.textMuted, letterSpacing: 1 }}>
              by neogleamz.com
            </Text>
          </TouchableOpacity>
          <Text style={[styles.subtitle, { marginTop: Spacing.md, letterSpacing: 1.5, fontSize: 13, color: Colors.textMuted }]}>
            Glow your way.
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

              {/* Strength bar (signup only) */}
              {mode === 'SIGNUP' && passwordStrength && (
                <View style={{ marginBottom: Spacing.md }}>
                  <View style={{ height: 4, backgroundColor: 'rgba(255,255,255,0.08)', borderRadius: 2, overflow: 'hidden', marginBottom: Spacing.sm }}>
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
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: Spacing.lg, marginTop: -4 }}>
              {/* Remember me checkbox */}
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
              <TouchableOpacity onPress={() => resetState('FORGOT_PASSWORD')}>
                <Text style={styles.forgotPasswordText}>Forgot Password?</Text>
              </TouchableOpacity>
            </View>
          )}

          {/* ── Inline error message (replaces Alert.alert for all modes) ── */}
          {!!errorMessage && (
            <View style={styles.errorBanner}>
              <MaterialCommunityIcons name="alert-circle-outline" size={15} color="#FF6B6B" style={{ marginRight: Spacing.sm }} />
              <Text style={styles.errorBannerText}>{errorMessage}</Text>
            </View>
          )}

          {/* ── Inline success message ── */}
          {!!successMessage && (
            <View style={styles.successBanner}>
              <MaterialCommunityIcons name="check-circle-outline" size={15} color="#4ADE80" style={{ marginRight: Spacing.sm }} />
              <Text style={styles.successBannerText}>{successMessage}</Text>
            </View>
          )}

          {/* EULA Checkbox (signup only) */}
          {mode === 'SIGNUP' && (
            <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: Spacing.lg, marginTop: Spacing.xs, paddingHorizontal: Spacing.xs }}>
              <TouchableOpacity
                onPress={() => setEulaAccepted(v => !v)}
                style={[styles.checkbox, eulaAccepted && { backgroundColor: Colors.primary, borderColor: Colors.primary }, { marginRight: Spacing.md } ]}
              >
                {eulaAccepted && <MaterialCommunityIcons name="check" size={12} color="#000" />}
              </TouchableOpacity>
              <View style={{ flex: 1, flexDirection: 'row', flexWrap: 'wrap' }}>
                <Text style={{ color: Colors.textMuted, fontSize: 13 }}>By creating an account, you agree to the </Text>
                <TouchableOpacity onPress={() => setShowEulaModal(true)}>
                  <Text style={{ color: Colors.primary, fontSize: 13, fontWeight: 'bold' }}>SK8Lytz EULA</Text>
                </TouchableOpacity>
              </View>
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
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.md }}>
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

      </View>

      {/* DOCKED BOTTOM ACTIONS */}
      <View style={styles.bottomDock}>
        {/* Offline mode option */}
        {mode !== 'FORGOT_PASSWORD' && onOfflineMode && (
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
            <Text style={styles.offlineButtonSub}>No account needed. Cloud Sync, Crew Hub, Live Sessions, SK8Lytz Picks, and Global Presets will be disabled.</Text>
            
            {/* Remember Checkbox inside button */}
            <TouchableOpacity
              onPress={() => setRememberOffline(v => !v)}
              style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, marginTop: Spacing.md, paddingVertical: Spacing.xs }}
              activeOpacity={0.7}
            >
              <View style={[
                styles.checkbox,
                rememberOffline && { backgroundColor: Colors.primary, borderColor: Colors.primary }
              ]}>
                {rememberOffline && <MaterialCommunityIcons name="check" size={12} color="#000" />}
              </View>
              <Text style={{ color: Colors.textMuted, fontSize: 13, fontWeight: 'bold' }}>Remember my choice</Text>
            </TouchableOpacity>
          </TouchableOpacity>
        )}

        {/* DEV MODE - Virtual Skates Bypass */}
        {isSandboxEnabled && (mode === 'LOGIN') && onOfflineMode && (
          <TouchableOpacity
            style={{ 
              marginTop: Spacing.lg, marginHorizontal: Spacing.xl, paddingVertical: Spacing.md, 
              borderWidth: 1, borderColor: 'rgba(0,240,255,0.4)', borderRadius: 12, 
              backgroundColor: 'rgba(0,240,255,0.05)', alignItems: 'center',
              flexDirection: 'row', justifyContent: 'center', gap: Spacing.sm
            }}
            onPress={async () => {
              // Enable the virtual skates flags in storage so the dashboard picks them up
              await AsyncStorage.setItem('@Sk8lytz_demo_halo', 'true');
              await AsyncStorage.setItem('@Sk8lytz_demo_soul', 'true');
              // Bypass Auth
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
      </View>

      {showEulaModal && (
        <EulaModal 
          visible={showEulaModal}
          onAccept={() => {
            setEulaAccepted(true);
            setShowEulaModal(false);
          }}
          onDecline={() => setShowEulaModal(false)}
        />
      )}
    </KeyboardAvoidingView>
  );
}

const createStyles = (Colors: ThemePalette, insets: { top: number; bottom: number; left: number; right: number }) => StyleSheet.create({
  container: { flex: 1, backgroundColor: Colors.background },
  scrollContent: {
    flex: 1, justifyContent: 'center',
    paddingHorizontal: Spacing.xl,
    paddingTop: Math.max(insets.top + 60, 80),
    paddingBottom: Spacing.xl,
  },
  bottomDock: {
    width: '100%',
    backgroundColor: Colors.surface,
    borderTopWidth: 1,
    borderTopColor: Colors.surfaceHighlight,
    paddingHorizontal: Spacing.xl,
    paddingTop: Spacing.lg,
    paddingBottom: Math.max(insets.bottom + 16, 24),
  },
  topButtons: {
    position: 'absolute', top: Math.max(insets.top + 10, 20), right: 16,
    flexDirection: 'row', gap: Spacing.sm, zIndex: 10,
  },
  topLeftButtons: {
    position: 'absolute', top: Math.max(insets.top + 10, 20), left: 16,
    flexDirection: 'row', gap: Spacing.sm, zIndex: 10,
  },
  topLeftBtn: {
    flexDirection: 'row', alignItems: 'center', justifyContent: 'center', gap: Spacing.sm,
    paddingVertical: Spacing.sm, paddingHorizontal: Spacing.md, borderRadius: 20, borderWidth: 1,
  },
  topBtn: {
    width: 34, height: 34, borderRadius: 17,
    backgroundColor: 'rgba(255,255,255,0.07)',
    borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)',
    alignItems: 'center', justifyContent: 'center',
  },
  headerContainer: { alignItems: 'center', marginBottom: Spacing.xxxl },
  title: { fontSize: 42, fontWeight: '900', color: Colors.text, letterSpacing: -1, marginBottom: Spacing.sm },
  subtitle: { color: Colors.textMuted, fontSize: 14, textAlign: 'center' },
  formContainer: { marginBottom: Spacing.xl },
  input: {
    backgroundColor: Colors.surface,
    color: Colors.text,
    padding: Spacing.lg,
    borderRadius: Layout.borderRadius,
    fontSize: 15,
    marginBottom: Spacing.md,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
  },
  forgotPasswordContainer: { alignItems: 'flex-end', marginBottom: Spacing.lg, marginTop: -4 },
  forgotPasswordText: { color: Colors.primary, fontSize: 13 },
  checkbox: {
    width: 18, height: 18, borderRadius: 4,
    borderWidth: 1.5, borderColor: Colors.textMuted,
    alignItems: 'center', justifyContent: 'center',
    backgroundColor: 'transparent',
  },
  errorBanner: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    backgroundColor: 'rgba(255, 107, 107, 0.12)',
    borderWidth: 1,
    borderColor: 'rgba(255, 107, 107, 0.3)',
    borderRadius: 10,
    paddingHorizontal: Spacing.md,
    paddingVertical: Spacing.md,
    marginBottom: Spacing.md,
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
    paddingHorizontal: Spacing.md,
    paddingVertical: Spacing.md,
    marginBottom: Spacing.md,
  },
  successBannerText: {
    color: '#4ADE80',
    fontSize: 13,
    lineHeight: 18,
    flex: 1,
  },
  primaryButton: {
    backgroundColor: Colors.primary,
    padding: Spacing.lg,
    borderRadius: Layout.borderRadius,
    alignItems: 'center',
    marginBottom: Spacing.md,
  },
  primaryButtonText: { color: '#000', fontWeight: 'bold', fontSize: 16 },
  secondaryButton: {
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
    padding: Spacing.lg,
    borderRadius: Layout.borderRadius,
    alignItems: 'center',
  },
  secondaryButtonText: { color: Colors.text, fontWeight: 'bold', fontSize: 15 },
  magicLinkButton: {
    alignItems: 'center',
    paddingVertical: Spacing.md,
    marginBottom: Spacing.xs,
  },
  magicLinkText: { color: Colors.primary, fontSize: 13, fontWeight: '600' },
  toggleContainer: { flexDirection: 'row', justifyContent: 'center', marginTop: Spacing.sm },
  toggleText: { color: Colors.textMuted, fontSize: 14 },
  toggleLink: { color: Colors.primary, fontSize: 14, fontWeight: 'bold' },
  offlineButton: {
    alignSelf: 'center',
    alignItems: 'center',
    width: '50%',
    paddingVertical: Spacing.sm,
    borderRadius: Layout.borderRadius,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
    backgroundColor: 'rgba(255,255,255,0.04)',
  },
  offlineButtonText: { color: Colors.textMuted, fontSize: 12, fontWeight: '600', marginBottom: 2 },
  offlineButtonSub: { color: Colors.textMuted, fontSize: 9, opacity: 0.7, textAlign: 'center' },
});
