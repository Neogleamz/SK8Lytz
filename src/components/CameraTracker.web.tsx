/**
 * CameraTracker.web.tsx — Web Platform Stub
 *
 * react-native-vision-camera and react-native-worklets-core are native-only
 * packages. Metro/Expo automatically picks this .web.tsx override instead of
 * CameraTracker.tsx when bundling for the web platform, preventing a crash.
 */
import MaterialCommunityIcons from '@expo/vector-icons/MaterialCommunityIcons';
import React from 'react';
import { StyleSheet, Text, View } from 'react-native';

import { Colors, Spacing } from '../theme/theme';

interface CameraTrackerProps {
  onColorDetected: (hex: string) => void;
  isActive: boolean;
}

export default function CameraTracker(_props: CameraTrackerProps) {
  return (
    <View style={styles.container}>
      <MaterialCommunityIcons name="camera-off" size={36} color="rgba(255,255,255,0.3)" />
      <Text style={styles.title}>Camera Not Available</Text>
      <Text style={styles.subtitle}>
        The Sniper Dropper requires a physical device.{'\n'}
        Use the SK8Lytz mobile app to detect colors live.
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    minHeight: 200,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0,0,0,0.2)',
    borderRadius: 12,
    padding: Spacing.xl,
    gap: Spacing.md,
  },
  title: {
    color: 'rgba(255,255,255,0.6)',
    fontSize: 16,
    fontWeight: 'bold',
    fontFamily: 'Inter-Bold',
  },
  subtitle: {
    color: 'rgba(255,255,255,0.35)',
    fontSize: 13,
    textAlign: 'center',
    lineHeight: 20,
    fontFamily: 'Inter-Medium',
  },
});
