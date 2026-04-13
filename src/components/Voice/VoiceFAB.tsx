import { Spacing } from '../../theme/theme';
import React from 'react';
import {
  TouchableOpacity,
  StyleSheet,
  View,
  Dimensions,
  Platform
} from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { LinearGradient } from 'expo-linear-gradient';

const { width } = Dimensions.get('window');

interface Props {
  onPress: () => void;
  isListening?: boolean;
}

export default function VoiceFAB({ onPress, isListening }: Props) {
  return (
    <TouchableOpacity
      style={styles.container}
      onPress={onPress}
      activeOpacity={0.8}
    >
      <LinearGradient
        colors={isListening ? ['#FF00E5', '#7000FF'] : ['#00F0FF', '#7000FF']}
        start={{ x: 0, y: 0 }}
        end={{ x: 1, y: 1 }}
        style={styles.gradient}
      >
        <View style={styles.inner}>
          <MaterialCommunityIcons 
            name={isListening ? "microphone" : "microphone-outline"} 
            size={28} 
            color="#000" 
          />
        </View>
      </LinearGradient>
      
      {/* Dynamic Pulse Ring (Visual anchor) */}
      <View style={[styles.pulseRing, isListening && styles.pulseActive]} />
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  container: {
    position: 'absolute',
    bottom: 30,
    left: 20,
    width: 60,
    height: 60,
    borderRadius: 30,
    justifyContent: 'center',
    alignItems: 'center',
    zIndex: 9999,
    // Shadow for elevation
    shadowColor: '#00F0FF',
    shadowOffset: { width: 0, height: 10 },
    shadowOpacity: 0.3,
    shadowRadius: 15,
    elevation: 8,
  },
  gradient: {
    width: '100%',
    height: '100%',
    borderRadius: 30,
    padding: Spacing.xxs, // Border effect
  },
  inner: {
    flex: 1,
    borderRadius: 28,
    backgroundColor: '#00F0FF', // Solid core or translucent? keeping it solid for visibility
    justifyContent: 'center',
    alignItems: 'center',
  },
  pulseRing: {
    position: 'absolute',
    width: 66,
    height: 66,
    borderRadius: 33,
    borderWidth: 2,
    borderColor: 'rgba(0, 240, 255, 0.4)',
    zIndex: -1,
  },
  pulseActive: {
    borderColor: '#FF00E5',
    transform: [{ scale: 1.1 }],
  }
});
