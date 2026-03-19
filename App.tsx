import { StatusBar } from 'expo-status-bar';
import { StyleSheet, View } from 'react-native';
import DashboardScreen from './src/screens/DashboardScreen';
import { Colors } from './src/theme/theme';

export default function App() {
  return (
    <View style={styles.container}>
      <StatusBar style="light" />
      <DashboardScreen />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.background,
  },
});


