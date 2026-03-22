import { StatusBar } from 'expo-status-bar';
import { StyleSheet, View } from 'react-native';
import DashboardScreen from './src/screens/DashboardScreen';
import { ThemeProvider, useTheme } from './src/context/ThemeContext';

function AppContent() {
  const { Colors, isDark } = useTheme();
  return (
    <View style={[styles.container, { backgroundColor: Colors.background }]}>
      <StatusBar style={isDark ? 'light' : 'dark'} />
      <DashboardScreen />
    </View>
  );
}

export default function App() {
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
