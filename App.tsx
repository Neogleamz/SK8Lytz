/* global global, window */
import React, { useEffect } from 'react';
import { StatusBar } from 'expo-status-bar';
import { StyleSheet, View, AppState, Text, LogBox, Platform } from 'react-native';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import DashboardScreen from './src/screens/DashboardScreen';
import AuthScreen from './src/screens/AuthScreen';
import { ThemeProvider, useTheme } from './src/context/ThemeContext';
import { GlobalPermissionsModal } from './src/components/modals/GlobalPermissionsModal';
import { GlobalErrorBoundary } from './src/components/GlobalErrorBoundary';
import { useFonts, Righteous_400Regular } from '@expo-google-fonts/righteous';
import * as SplashScreen from 'expo-splash-screen';
import { AppLogger } from './src/services/AppLogger';
import { warmLedgerCache } from './src/hooks/useDeviceStateLedger';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { ComplianceGate } from './src/providers/ComplianceGate';
import { BLEProvider } from './src/context/BLEContext';
import { FavoritesProvider } from './src/context/FavoritesContext';
import { SessionProvider } from './src/context/SessionContext';
import { BluetoothGuard } from './src/providers/BluetoothGuard';
import { AuthProvider, useAuth } from './src/context/AuthContext';


LogBox.ignoreLogs([
  '"shadow*" style props are deprecated',
  'props.pointerEvents is deprecated'
]);

import { STORAGE_OFFLINE_SKIP } from './src/constants/storageKeys';



interface GlobalWithErrorUtils { ErrorUtils?: { getGlobalHandler: () => ((error: unknown, isFatal?: boolean) => void) | undefined; setGlobalHandler: (handler: (error: unknown, isFatal?: boolean) => void) => void; } }
const g = global as unknown as GlobalWithErrorUtils;
if (g.ErrorUtils) {
  const defaultHandler = g.ErrorUtils.getGlobalHandler();
  g.ErrorUtils.setGlobalHandler(async (error: unknown, isFatal?: boolean) => {
    const err = error as Error | undefined;
    await AppLogger.log('ERROR_CAUGHT', { message: err?.message || 'Unhandled JS Exception', stack: err?.stack, isFatal });
    await AppLogger.uploadLogsToSupabase();
    if (defaultHandler) defaultHandler(error, isFatal);
  });
}

// ── Unhandled Promise Rejection capture (web builds) ──────────────────────────
if (typeof window !== 'undefined' && typeof window.addEventListener === 'function') {
  window.addEventListener('unhandledrejection', (event) => {
    AppLogger.error('[UnhandledPromise]', event.reason);
  });
}

// ── Console.error monkey-patch: pipe third-party library errors to telemetry ──
const _originalConsoleError = console.error;
console.error = (...args: unknown[]) => {
  AppLogger.warn('[console.error]', { args: args.map((a: unknown) => String(a)).slice(0, 3).join(' ') });
  _originalConsoleError.apply(console, args);
};

/** ── Global Error Boundary to prevent White Screens ────────────────────────────────── */
SplashScreen.preventAutoHideAsync().catch(() => {});

function AppContent() {
  const { Colors, isDark } = useTheme();
  const {
    isAuthenticated,
    isOfflineMode,
    sessionLoaded,
    sessionExpired,
    setIsOfflineMode,
    clearOfflineMode,
  } = useAuth();

  useOfflineSyncWorker();

  if (!sessionLoaded) return null;

  return (
    <View style={[styles.container, { backgroundColor: Colors.background }]}>
      <StatusBar style={isDark ? 'light' : 'dark'} />
      {isAuthenticated ? (
        <ComplianceGate>
          <DashboardScreen isOfflineMode={isOfflineMode} onLogout={clearOfflineMode} />
        </ComplianceGate>
      ) : (
        <AuthScreen
          onOfflineMode={() => {
            setIsOfflineMode(true);
            AsyncStorage.setItem(STORAGE_OFFLINE_SKIP, 'true').catch(() => {});
          }}
          sessionExpired={sessionExpired}
        />
      )}
    </View>
  );
}

const appStartTime = Date.now();

import { useOfflineSyncWorker } from './src/hooks/cloud/useOfflineSyncWorker';

export default function App() {
  const [fontsLoaded] = useFonts({
    'Righteous': Righteous_400Regular,
  });



  useEffect(() => {
    if (fontsLoaded) {
      if (Platform.OS === 'web') {
        setTimeout(() => {
          SplashScreen.hideAsync().catch(() => {});
        }, 150);
      } else {
        SplashScreen.hideAsync().catch(() => {});
      }
      AppLogger.log('APP_OPENED', { loadTimeMs: Date.now() - appStartTime });
      AppLogger.uploadLogsToSupabase();
      // Pre-warm ledger cache so loadSync() has data before any screen mounts.
      // Fires once per cold start — reads all @SK8Lytz_DeviceState_v2_* keys from AsyncStorage.
      warmLedgerCache().catch(() => {});
    }
  }, [fontsLoaded]);

  useEffect(() => {
    const subscription = AppState.addEventListener('change', nextAppState => {
      if (nextAppState === 'background' || nextAppState === 'inactive') {
        AppLogger.uploadLogsToSupabase();
      }
      if (nextAppState === 'active') {
        AppLogger.log('APP_FOREGROUNDED', { timestamp: Date.now() });
      }
    });
    return () => subscription.remove();
  }, []);

  useEffect(() => {
    // FIX: Android requires Health Connect ActivityResultLauncher to be initialized early
    // before the activity is RESUMED to avoid lateinit UninitializedPropertyAccessException
    if (Platform.OS === 'android') {
      try {
        const { initialize } = require('react-native-health-connect');
        initialize().catch((err: unknown) => AppLogger.warn('HEALTH_CONNECT', { event: 'init_failed', error: String(err) }));
      } catch {
        // Fallback if library missing
      }
    }
  }, []);

  if (!fontsLoaded) return null;

  return (
    <GlobalErrorBoundary>
      <SafeAreaProvider>
        <ThemeProvider>
          <AuthProvider>
            <FavoritesProvider>
              <SessionProvider>
                <BLEProvider>
                  <BluetoothGuard>
                    <AppContent />
                    <GlobalPermissionsModal />
                  </BluetoothGuard>
                </BLEProvider>
              </SessionProvider>
            </FavoritesProvider>
          </AuthProvider>
        </ThemeProvider>
      </SafeAreaProvider>
    </GlobalErrorBoundary>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
});
