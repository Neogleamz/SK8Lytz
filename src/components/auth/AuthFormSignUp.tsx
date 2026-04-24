import React, { useEffect, useRef, useState } from 'react';
import { ActivityIndicator, Animated, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { makeRedirectUri } from 'expo-auth-session';
import EulaModal from '../modals/EulaModal';
import { useTheme } from '../../context/ThemeContext';
import { supabase } from '../../services/supabaseClient';
import { AppLogger } from '../../services/AppLogger';
import {
  checkHIBP,
  checkPasswordComplexity,
  containsProfanity,
  isCommonPassword,
  PasswordStrength
} from '../../services/AuthUtils';
import { Spacing } from '../../theme/theme';
import { useAuthStyles } from './AuthStyles';

const isValidEmail = (str: string) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(str.trim());

interface AuthFormSignUpProps {
  onModeChange: (mode: 'LOGIN' | 'FORGOT_PASSWORD' | 'MAGIC_LINK') => void;
}

export function AuthFormSignUp({ onModeChange }: AuthFormSignUpProps) {
  const { Colors } = useTheme();
  const styles = useAuthStyles();

  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  
  const [loading, setLoading] = useState(false);
  const [hibpChecking, setHibpChecking] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [eulaAccepted, setEulaAccepted] = useState(false);
  const [showEulaModal, setShowEulaModal] = useState(false);
  const [passwordStrength, setPasswordStrength] = useState<PasswordStrength | null>(null);
  const strengthAnim = useRef(new Animated.Value(0)).current;

  const showError = (msg: string) => { setErrorMessage(msg); setSuccessMessage(''); };
  const showSuccess = (msg: string) => { setSuccessMessage(msg); setErrorMessage(''); };

  useEffect(() => {
    if (password.length > 0) {
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
  }, [password]);

  const handleSignUp = async () => {
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
      AppLogger.log('EULA_ACCEPTED', { policy_version: 'v1.0.0' });
      showSuccess('✅ Account created! Check your email for a verification link, then log in.');
      setTimeout(() => onModeChange('LOGIN'), 3000);
    }
  };

  const strengthBarWidth = strengthAnim.interpolate({ inputRange: [0, 1], outputRange: ['0%', '100%'] });

  return (
    <View style={styles.formContainer}>
      <TextInput
        style={styles.input}
        placeholder="Username"
        placeholderTextColor={Colors.textMuted}
        value={username}
        onChangeText={t => { setUsername(t); setErrorMessage(''); }}
        autoCapitalize="none"
      />

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

        {passwordStrength && (
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

      <TouchableOpacity
        style={styles.primaryButton}
        disabled={loading || hibpChecking}
        onPress={handleSignUp}
      >
        {loading || hibpChecking ? (
          <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.md }}>
            <ActivityIndicator color="#000" size="small" />
            <Text style={styles.primaryButtonText}>{hibpChecking ? 'Checking Security...' : 'Loading...'}</Text>
          </View>
        ) : (
          <Text style={styles.primaryButtonText}>Create Account</Text>
        )}
      </TouchableOpacity>

      <View style={styles.toggleContainer}>
        <Text style={styles.toggleText}>Already have an account? </Text>
        <TouchableOpacity onPress={() => onModeChange('LOGIN')}>
          <Text style={styles.toggleLink}>Log In</Text>
        </TouchableOpacity>
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
    </View>
  );
}
