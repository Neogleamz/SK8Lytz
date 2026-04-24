import React from 'react';
import { TouchableOpacity, Text, View, Alert } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useAuthStyles } from './AuthStyles';

interface AuthSandboxToggleProps {
  isSandboxEnabled: boolean;
  setIsSandboxEnabled: (val: boolean) => void;
}

export function AuthSandboxToggle({ isSandboxEnabled, setIsSandboxEnabled }: AuthSandboxToggleProps) {
  const styles = useAuthStyles();

  if (!__DEV__) return null;

  return (
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
  );
}
