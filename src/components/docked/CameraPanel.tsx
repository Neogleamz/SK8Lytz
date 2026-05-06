import React, { useState, useCallback } from 'react';
import { View, Text, TouchableOpacity, StyleSheet } from 'react-native';
import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';
import * as Haptics from 'expo-haptics';
import CameraTracker from '../CameraTracker';
import { Spacing } from '../../theme/theme';

interface CameraPanelProps {
  onColorDetected: (hex: string) => void;
}

const CameraPanel = React.memo(({ onColorDetected }: CameraPanelProps) => {
  const [liveHex, setLiveHex] = useState<string>('#FF0080');
  const [isTracking, setIsTracking] = useState(false);

  const handleColorDetected = useCallback((hex: string) => {
    setLiveHex(hex);
    if (isTracking) {
      onColorDetected(hex);
    }
  }, [isTracking, onColorDetected]);

  const handlePressIn = useCallback(() => {
    setIsTracking(true);
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Heavy);
    onColorDetected(liveHex); // Instantly apply current color
  }, [liveHex, onColorDetected]);

  const handlePressOut = useCallback(() => {
    setIsTracking(false);
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
  }, []);

  return (
    <View style={styles.container}>
      <CameraTracker
        isActive
        onColorDetected={handleColorDetected}
      />

      <View style={styles.controlPanel}>
        <View style={styles.hexPill}>
          <Text style={styles.hexText}>{liveHex}</Text>
        </View>
        <Text style={styles.instructionText}>
          Hold button to stream live color to skates
        </Text>

        <TouchableOpacity
          activeOpacity={0.8}
          onPressIn={handlePressIn}
          onPressOut={handlePressOut}
          style={[
            styles.captureFab,
            { backgroundColor: liveHex },
            isTracking && styles.captureFabActive,
          ]}
        >
          <MaterialCommunityIcons
            name="crosshairs-gps"
            size={36}
            color="#FFF"
          />
        </TouchableOpacity>
      </View>
    </View>
  );
});

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  controlPanel: {
    padding: Spacing.xl,
    backgroundColor: '#0a0a0a',
    alignItems: 'center',
    borderTopWidth: 1,
    borderTopColor: '#222',
  },
  hexPill: {
    backgroundColor: 'rgba(255,255,255,0.1)',
    paddingHorizontal: Spacing.lg,
    paddingVertical: 8,
    borderRadius: 20,
    marginBottom: Spacing.xs,
  },
  hexText: {
    color: '#FFF',
    fontSize: 14,
    fontFamily: 'Inter-Bold',
    letterSpacing: 1,
  },
  instructionText: {
    color: 'rgba(255,255,255,0.6)',
    fontSize: 12,
    fontFamily: 'Inter-Medium',
    textAlign: 'center',
    marginBottom: Spacing.lg,
  },
  captureFab: {
    width: 80,
    height: 80,
    borderRadius: 40,
    justifyContent: 'center',
    alignItems: 'center',
    borderWidth: 4,
    borderColor: 'rgba(255,255,255,0.2)',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.3,
    shadowRadius: 10,
    elevation: 8,
  },
  captureFabActive: {
    transform: [{ scale: 0.95 }],
    opacity: 0.9,
    borderColor: '#FFF',
    shadowOpacity: 0.8,
    shadowRadius: 20,
  },
});

export default CameraPanel;
