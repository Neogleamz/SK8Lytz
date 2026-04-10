import React, { useEffect, useState } from 'react';
import { StatusBar } from 'expo-status-bar';
import { StyleSheet, View, AppState, Text } from 'react-native';
import { Session, AuthChangeEvent } from '@supabase/supabase-js';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import DashboardScreen from './src/screens/DashboardScreen';
import AuthScreen from './src/screens/AuthScreen';
import { ThemeProvider, useTheme } from './src/context/ThemeContext';
import { useFonts, Righteous_400Regular } from '@expo-google-fonts/righteous';
import * as SplashScreen from 'expo-splash-screen';
import { AppLogger } from './src/services/AppLogger';
import { supabase } from './src/services/supabaseClient';
import AsyncStorage from '@react-native-async-storage/async-storage';
import * as Linking from 'expo-linking';

const STORAGE_OFFLINE_SKIP   = '@Sk8lytz_offline_skip';
const STORAGE_REMEMBER_CREDS = '@Sk8lytz_remember_creds';

if (typeof global.ErrorUtils !== 'undefined') {
  const defaultHandler = (global.ErrorUtils as any).getGlobalHandler();
  (global.ErrorUtils as any).setGlobalHandler(async (error: any, isFatal: boolean) => {
    await AppLogger.log('ERROR_CAUGHT', { message: error?.message || 'Unhandled JS Exception', stack: error?.stack, isFatal });
    await AppLogger.uploadLogsToSupabase();
    if (defaultHandler) defaultHandler(error, isFatal);
  });
}

/** ── Global Error Boundary to prevent White Screens ────────────────────────────────── */
class SafeErrorBoundary extends React.Component<{ children: React.ReactNode }, { hasError: boolean; error: Error | null }> {
  constructor(props: { children: React.ReactNode }) {
    super(props);
    this.state = { hasError: false, error: null };
  }
  static getDerivedStateFromError(error: Error) {
    return { hasError: true, error };
  }
  componentDidCatch(error: Error, errorInfo: React.ErrorInfo) {
    AppLogger.log('ERROR_CAUGHT', { message: error.message, stack: error.stack, info: errorInfo }).then(() => {
        AppLogger.uploadLogsToSupabase();
    });
    console.error('FATAL_RECOVERY', error, errorInfo);
  }
  render() {
    if (this.state.hasError) {
      return (
        <View style={{ flex: 1, backgroundColor: '#0A0E14', justifyContent: 'center', alignItems: 'center', padding: 20 }}>
          <Text style={{ fontSize: 40, marginBottom: 20 }}>⚠️</Text>
          <Text style={{ color: '#FFF', fontSize: 18, fontWeight: 'bold', marginBottom: 12 }}>Something went wrong</Text>
          <Text style={{ color: 'rgba(255,255,255,0.6)', fontSize: 14, textAlign: 'center', marginBottom: 30 }}>
            An unexpected error occurred. Tap below to reset the connection.
          </Text>
          <View style={{ backgroundColor: 'rgba(255,107,107,0.1)', padding: 10, borderRadius: 8, marginBottom: 30, borderWidth: 1, borderColor: 'rgba(255,107,107,0.2)' }}>
             <Text style={{ color: '#FF6B6B', fontSize: 12, fontStyle: 'italic' }}>{this.state.error?.message?.slice(0, 100)}...</Text>
          </View>
          <View style={{ gap: 12, width: '100%' }}>
            <View style={{ backgroundColor: '#00F0FF', borderRadius: 12, overflow: 'hidden' }}>
              <View style={{ backgroundColor: 'transparent', padding: 16, alignItems: 'center' }}>
                <Text 
                  style={{ color: '#000', fontWeight: 'bold' }}
                  onPress={async () => {
                    await AsyncStorage.multiRemove([STORAGE_OFFLINE_SKIP, '@Sk8lytz_logs', 'ng_device_configs']);
                    // Reload app (native reload is preferred but this serves as a reset)
                    this.setState({ hasError: false });
                  }}
                >
                  RESET & RE-SYNC APP
                </Text>
              </View>
            </View>
          </View>
        </View>
      );
    }
    return this.props.children;
  }
}

SplashScreen.preventAutoHideAsync().catch(() => {});

function AppContent() {
  const { Colors, isDark } = useTheme();
  const [session, setSession] = useState<Session | null>(null);
  const [sessionLoaded, setSessionLoaded] = useState(false);
  const [offlineMode, setOfflineMode] = useState(false);

  useEffect(() => {
    if (!supabase) {
      console.warn('[SK8Lytz] Supabase not configured — offline-only mode.');
      setSessionLoaded(true);
      return;
    }

    const handleDeepLink = async ({ url }: { url: string }) => {
      if (!url) return;
      try {
        if (url.includes('#access_token=')) {
          const hashString = url.split('#')[1];
          const params = hashString.split('&').reduce((acc, current) => {
            const [key, value] = current.split('=');
            acc[key] = decodeURIComponent(value);
            return acc;
          }, {} as Record<string, string>);

          if (params.access_token && params.refresh_token) {
            const { error } = await supabase.auth.setSession({ 
              access_token: params.access_token, 
              refresh_token: params.refresh_token 
            });
            if (error) {
              AppLogger.log('AUTH_ERROR', { context: 'deep_link', message: error.message });
            }
          }
        }
      } catch (err) {
        console.error('Deep link parsed error', err);
      }
    };

    const linkSubscription = Linking.addEventListener('url', handleDeepLink);
    Linking.getInitialURL().then(url => {
      if (url) handleDeepLink({ url });
    });

    const init = async () => {
      try {
        // ── 1. Check if user previously chose Continue Offline ──
        const offlineSkip = await AsyncStorage.getItem(STORAGE_OFFLINE_SKIP);
        if (offlineSkip === 'true') {
          setOfflineMode(true);
          return;
        }

        // ── 2. Check active Supabase session ──
        const { data } = await supabase.auth.getSession();
        const existing = data?.session;
        if (existing) {
          setSession(existing);
          return;
        }

        // ── 3. Try auto-login with remembered credentials ──
        const raw = await AsyncStorage.getItem(STORAGE_REMEMBER_CREDS);
        if (raw) {
          try {
            const saved = JSON.parse(raw);
            if (saved.rememberMe && saved.email && saved.password) {
              const { data, error } = await supabase.auth.signInWithPassword({
                email: saved.email,
                password: saved.password,
              });
              if (!error && data.session) {
                setSession(data.session);
                return;
              } else {
                // Credentials stale — clear password but keep email
                await AsyncStorage.setItem(STORAGE_REMEMBER_CREDS, JSON.stringify({ email: saved.email, rememberMe: false }));
              }
            }
          } catch {}
        }
      } catch (err) {
        console.error('Initialization error:', err);
        AppLogger.log('ERROR_CAUGHT', { message: 'Initialization failed', info: err, context: 'App.init' });
      } finally {
        setSessionLoaded(true);
      }
    };

    init();

    const { data } = supabase.auth.onAuthStateChange((_event: AuthChangeEvent, session: Session | null) => {
      const subscription = data?.subscription;
      AppLogger.log('SYNC', { context: 'auth_change', event: _event, hasSession: !!session });
      setSession(session);
      if (!session) {
        setOfflineMode(false);
        AsyncStorage.removeItem(STORAGE_OFFLINE_SKIP);
      }
    });

    return () => {
      if (data?.subscription) data.subscription.unsubscribe();
      linkSubscription.remove();
    };
  }, []);

  if (!sessionLoaded) return null;

  const isAuthenticated = (session && session.user) || !supabase || offlineMode;

  return (
    <View style={[styles.container, { backgroundColor: Colors.background }]}>
      <StatusBar style={isDark ? 'light' : 'dark'} />
      {isAuthenticated ? (
        <DashboardScreen isOfflineMode={offlineMode} onLogout={() => setOfflineMode(false)} />
      ) : (
        <AuthScreen
          onAuthSuccess={() => {/* session change auto-handles via onAuthStateChange */}}
          onOfflineMode={() => setOfflineMode(true)}
        />
      )}
    </View>
  );
}

const appStartTime = Date.now();

export default function App() {
  const [fontsLoaded] = useFonts({
    'Righteous': Righteous_400Regular,
  });

  useEffect(() => {
    if (fontsLoaded) {
      SplashScreen.hideAsync().catch(() => {});
      AppLogger.log('APP_OPENED', { loadTimeMs: Date.now() - appStartTime });
      AppLogger.uploadLogsToSupabase();
    }
  }, [fontsLoaded]);

  useEffect(() => {
    const subscription = AppState.addEventListener('change', nextAppState => {
      if (nextAppState === 'background' || nextAppState === 'inactive') {
        AppLogger.uploadLogsToSupabase();
      }
    });
    return () => subscription.remove();
  }, []);

  if (!fontsLoaded) return null;

  return (
    <SafeErrorBoundary>
      <SafeAreaProvider>
        <ThemeProvider>
          <AppContent />
        </ThemeProvider>
      </SafeAreaProvider>
    </SafeErrorBoundary>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
});
