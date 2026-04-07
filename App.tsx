import React, { useEffect, useState } from 'react';
import { StatusBar } from 'expo-status-bar';
import { StyleSheet, View, AppState } from 'react-native';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import DashboardScreen from './src/screens/DashboardScreen';
import AuthScreen from './src/screens/AuthScreen';
import { ThemeProvider, useTheme } from './src/context/ThemeContext';
import { useFonts, Righteous_400Regular } from '@expo-google-fonts/righteous';
import * as SplashScreen from 'expo-splash-screen';
import { AppLogger } from './src/services/AppLogger';
import { supabase } from './src/services/supabaseClient';
import AsyncStorage from '@react-native-async-storage/async-storage';

const STORAGE_OFFLINE_SKIP   = '@Sk8lytz_offline_skip';
const STORAGE_REMEMBER_CREDS = '@Sk8lytz_remember_creds';

SplashScreen.preventAutoHideAsync().catch(() => {});

import { Session, AuthChangeEvent } from '@supabase/supabase-js';

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

    const init = async () => {
      // ── 1. Check if user previously chose Continue Offline ──
      const offlineSkip = await AsyncStorage.getItem(STORAGE_OFFLINE_SKIP);
      if (offlineSkip === 'true') {
        setOfflineMode(true);
        setSessionLoaded(true);
        return;
      }

      // ── 2. Check active Supabase session ──
      const { data: { session: existing } } = await supabase.auth.getSession();
      if (existing) {
        setSession(existing);
        setSessionLoaded(true);
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
              setSessionLoaded(true);
              return;
            } else {
              // Credentials stale — clear password but keep email
              await AsyncStorage.setItem(STORAGE_REMEMBER_CREDS, JSON.stringify({ email: saved.email, rememberMe: false }));
            }
          }
        } catch {}
      }

      setSessionLoaded(true);
    };

    init();

    const { data: { subscription } } = supabase.auth.onAuthStateChange((_event: AuthChangeEvent, session: Session | null) => {
      setSession(session);
      if (!session) {
        setOfflineMode(false);
        AsyncStorage.removeItem(STORAGE_OFFLINE_SKIP);
      }
    });

    return () => subscription.unsubscribe();
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
    <SafeAreaProvider>
      <ThemeProvider>
        <AppContent />
      </ThemeProvider>
    </SafeAreaProvider>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
});
