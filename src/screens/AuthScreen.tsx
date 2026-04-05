import React, { useState, useEffect } from 'react';
import { 
  View, 
  Text, 
  TextInput, 
  TouchableOpacity, 
  StyleSheet, 
  Alert, 
  ActivityIndicator,
  KeyboardAvoidingView,
  Platform,
  Dimensions,
  Image,
  ScrollView
} from 'react-native';
import { supabase } from '../services/supabaseClient';
import { Typography, Layout } from '../theme/theme';
import { useTheme } from '../context/ThemeContext';
import * as WebBrowser from 'expo-web-browser';
import * as Linking from 'expo-linking';
import { makeRedirectUri } from 'expo-auth-session';

WebBrowser.maybeCompleteAuthSession();

export default function AuthScreen({ onAuthSuccess }: { onAuthSuccess: () => void }) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [username, setUsername] = useState('');
  const [loading, setLoading] = useState(false);
  const [mode, setMode] = useState<'LOGIN' | 'SIGNUP' | 'FORGOT_PASSWORD'>('LOGIN');

  // Supabase native OAuth configuration
  const redirectTo = makeRedirectUri({
    // scheme: 'sk8lytz' // Ensure app.json matches this if configured natively
  });
  const handleOAuth = async (provider: 'google' | 'apple' | 'facebook') => {
    try {
      setLoading(true);
      const isNative = Platform.OS !== 'web';
      
      const { data, error } = await supabase.auth.signInWithOAuth({
        provider,
        options: {
          redirectTo,
          skipBrowserRedirect: isNative, // Must be true on iOS/Android, false on Web
        },
      });
      if (error) throw error;
      
      // On native, we must manually open the Auth Session
      if (isNative && data?.url) {
          const res = await WebBrowser.openAuthSessionAsync(data.url, redirectTo);
          if (res.type === 'success' && res.url) {
             // Extract the session payload from the deep link URL returned by WebBrowser
             await supabase.auth.getSessionFromUrl(res.url);
          }
      }
    } catch (error: any) {
      Alert.alert('OAuth Error', error.message);
    } finally {
      setLoading(false);
    }
  };

  async function signInWithEmail() {
    setLoading(true);
    const { error } = await supabase.auth.signInWithPassword({
      email: email,
      password: password,
    });

    if (error) {
      Alert.alert('Sign In Failed', error.message);
      setLoading(false);
    } else {
      onAuthSuccess();
    }
  }

  async function signUpWithEmail() {
    setLoading(true);
    if (!username) {
        Alert.alert('Missing Field', 'Please enter a username.');
        setLoading(false);
        return;
    }
    const { error } = await supabase.auth.signUp({
      email: email,
      password: password,
      options: {
          data: {
              username: username
          }
      }
    });

    if (error) {
      Alert.alert('Sign Up Failed', error.message);
    } else {
      Alert.alert('Success', 'Please check your email for a verification link.');
      setMode('LOGIN');
    }
    setLoading(false);
  }

  async function handleForgotPassword() {
    if (!email) {
      Alert.alert('Email Required', 'Please enter your email address to reset your password.');
      return;
    }
    setLoading(true);
    const { error } = await supabase.auth.resetPasswordForEmail(email, {
      redirectTo: redirectTo,
    });
    
    if (error) {
      Alert.alert('Error', error.message);
    } else {
      Alert.alert('Success', 'Password reset instructions have been sent to your email.');
      setMode('LOGIN');
    }
    setLoading(false);
  }

  return (
    <KeyboardAvoidingView 
      style={styles.container}
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
    >
      <ScrollView contentContainerStyle={styles.scrollContent} keyboardShouldPersistTaps="handled">
        
        <View style={styles.headerContainer}>
          <Text style={styles.title}>SK8<Text style={styles.titleHighlight}>Lytz</Text></Text>
          <Text style={styles.subtitle}>
             {mode === 'LOGIN' ? 'Welcome back. Sync your fleet.' : mode === 'SIGNUP' ? 'Create an account to backup your setups.' : 'Reset your password'}
          </Text>
        </View>

        <View style={styles.formContainer}>
          {mode === 'SIGNUP' && (
             <TextInput
                style={styles.input}
                onChangeText={(text) => setUsername(text)}
                value={username}
                placeholder="Username"
                placeholderTextColor={Colors.textMuted}
                autoCapitalize={'none'}
             />
          )}

          <TextInput
            style={styles.input}
            onChangeText={(text) => setEmail(text)}
            value={email}
            placeholder="email@address.com"
            placeholderTextColor={Colors.textMuted}
            autoCapitalize={'none'}
            keyboardType="email-address"
          />
          
          {mode !== 'FORGOT_PASSWORD' && (
             <TextInput
               style={styles.input}
               onChangeText={(text) => setPassword(text)}
               value={password}
               secureTextEntry={true}
               placeholder="Password"
               placeholderTextColor={Colors.textMuted}
               autoCapitalize={'none'}
             />
          )}

          {mode === 'LOGIN' && (
            <TouchableOpacity onPress={() => setMode('FORGOT_PASSWORD')} style={styles.forgotPasswordContainer}>
               <Text style={styles.forgotPasswordText}>Forgot Password?</Text>
            </TouchableOpacity>
          )}

          <TouchableOpacity 
             style={styles.primaryButton} 
             disabled={loading} 
             onPress={mode === 'LOGIN' ? signInWithEmail : mode === 'SIGNUP' ? signUpWithEmail : handleForgotPassword}
          >
             {loading ? (
                <ActivityIndicator color="#fff" />
             ) : (
                <Text style={styles.primaryButtonText}>
                   {mode === 'LOGIN' ? 'Log In' : mode === 'SIGNUP' ? 'Sign Up' : 'Send Reset Link'}
                </Text>
             )}
          </TouchableOpacity>

          <View style={styles.toggleContainer}>
              <Text style={styles.toggleText}>
                 {mode === 'LOGIN' ? "Don't have an account? " : mode === 'SIGNUP' ? "Already have an account? " : "Remembered your password? "}
              </Text>
              <TouchableOpacity onPress={() => setMode(mode === 'LOGIN' ? 'SIGNUP' : 'LOGIN')}>
                  <Text style={styles.toggleLink}>
                      {mode === 'LOGIN' ? "Sign Up" : "Log In"}
                  </Text>
              </TouchableOpacity>
          </View>
        </View>

        {mode === 'LOGIN' && (
           <View style={styles.oauthContainer}>
              <View style={styles.dividerContainer}>
                 <View style={styles.divider} />
                 <Text style={styles.dividerText}>OR CONTINUE WITH</Text>
                 <View style={styles.divider} />
              </View>
              
              <View style={styles.oauthButtonGroup}>
                 <TouchableOpacity style={styles.oauthButton} onPress={() => handleOAuth('google')}>
                    <Text style={styles.oauthButtonText}>Google</Text>
                 </TouchableOpacity>
                 {Platform.OS === 'ios' && (
                    <TouchableOpacity style={styles.oauthButton} onPress={() => handleOAuth('apple')}>
                       <Text style={styles.oauthButtonText}>Apple</Text>
                    </TouchableOpacity>
                 )}
                 <TouchableOpacity style={styles.oauthButton} onPress={() => handleOAuth('facebook')}>
                    <Text style={styles.oauthButtonText}>Facebook</Text>
                 </TouchableOpacity>
              </View>
           </View>
        )}

      </ScrollView>
    </KeyboardAvoidingView>
  );
}

const createStyles = (Colors: any) => StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.background,
  },
  scrollContent: {
    flexGrow: 1,
    justifyContent: 'center',
    padding: 24,
  },
  headerContainer: {
    alignItems: 'center',
    marginBottom: 40,
  },
  title: {
    ...Typography.header,
    fontSize: 42,
    color: Colors.text,
  },
  titleHighlight: {
    color: Colors.primary,
  },
  subtitle: {
    ...Typography.body,
    color: Colors.textMuted,
    marginTop: 10,
    textAlign: 'center',
  },
  formContainer: {
    width: '100%',
  },
  input: {
    backgroundColor: Colors.surface,
    color: Colors.text,
    ...Typography.body,
    borderRadius: Layout.borderRadius,
    paddingHorizontal: 20,
    paddingVertical: 16,
    marginBottom: 16,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
  },
  forgotPasswordContainer: {
    alignSelf: 'flex-end',
    marginBottom: 24,
  },
  forgotPasswordText: {
    ...Typography.caption,
    color: Colors.secondary,
  },
  primaryButton: {
    backgroundColor: Colors.primary,
    borderRadius: Layout.borderRadius,
    paddingVertical: 16,
    alignItems: 'center',
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 8,
    elevation: 5,
  },
  primaryButtonText: {
    ...Typography.title,
    color: '#FFF',
  },
  toggleContainer: {
    flexDirection: 'row',
    justifyContent: 'center',
    marginTop: 24,
  },
  toggleText: {
    ...Typography.body,
    color: Colors.textMuted,
  },
  toggleLink: {
    ...Typography.title,
    color: Colors.primary,
  },
  oauthContainer: {
    marginTop: 40,
  },
  dividerContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    marginBottom: 24,
  },
  divider: {
    flex: 1,
    height: 1,
    backgroundColor: Colors.surfaceHighlight,
  },
  dividerText: {
    ...Typography.caption,
    color: Colors.textMuted,
    paddingHorizontal: 16,
  },
  oauthButtonGroup: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    gap: 12,
  },
  oauthButton: {
    flex: 1,
    backgroundColor: Colors.surface,
    paddingVertical: 12,
    borderRadius: Layout.borderRadius,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
  },
  oauthButtonText: {
    ...Typography.body,
    color: Colors.text,
  }
});
