import React, { useEffect } from 'react';
import { StatusBar } from 'expo-status-bar';
import { StyleSheet, View, AppState } from 'react-native';
import DashboardScreen from './src/screens/DashboardScreen';
import { ThemeProvider, useTheme } from './src/context/ThemeContext';
import { useFonts, Righteous_400Regular } from '@expo-google-fonts/righteous';
import * as SplashScreen from 'expo-splash-screen';
import { AppLogger } from './src/services/AppLogger';

SplashScreen.preventAutoHideAsync().catch(() => {});

function AppContent() {
  const { Colors, isDark } = useTheme();
  return (
    <View style={[styles.container, { backgroundColor: Colors.background }]}>
      <StatusBar style={isDark ? 'light' : 'dark'} />
      <DashboardScreen />
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
      // Upload logs when app is fully opened
      AppLogger.uploadLogsToSupabase();
    }
  }, [fontsLoaded]);

  useEffect(() => {
    const subscription = AppState.addEventListener('change', nextAppState => {
      // Trigger upload when app is minimized or backgrounded (effectively "closed" from the user's perspective)
      if (nextAppState === 'background' || nextAppState === 'inactive') {
        AppLogger.uploadLogsToSupabase();
      }
    });

    return () => {
      subscription.remove();
    };
  }, []);

  if (!fontsLoaded) return null;

  return (
    <ThemeProvider>
      <AppContent />
    </ThemeProvider>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});
