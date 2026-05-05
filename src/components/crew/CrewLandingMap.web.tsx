/**
 * CrewLandingMap.web.tsx — Web Platform Stub
 * react-native-maps uses codegenNativeComponent which is not available
 * in react-native-web. Metro auto-picks this .web.tsx on web builds.
 */
import React from 'react';
import { StyleSheet, Text, View } from 'react-native';
import { Spacing } from '../../theme/theme';

interface CrewLandingMapProps {
  [key: string]: any;
}

export default function CrewLandingMap(_props: CrewLandingMapProps) {
  return (
    <View style={styles.container}>
      <Text style={styles.icon}>🗺️</Text>
      <Text style={styles.text}>Crew Map not available on web.</Text>
      <Text style={styles.sub}>Use the SK8Lytz mobile app to see nearby crew members.</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    minHeight: 200,
    backgroundColor: 'rgba(0,0,0,0.15)',
    borderRadius: 12,
    justifyContent: 'center',
    alignItems: 'center',
    padding: Spacing.xl,
    gap: Spacing.sm,
  },
  icon: {
    fontSize: 32,
  },
  text: {
    color: 'rgba(255,255,255,0.5)',
    fontSize: 15,
    fontWeight: 'bold',
    textAlign: 'center',
  },
  sub: {
    color: 'rgba(255,255,255,0.3)',
    fontSize: 12,
    textAlign: 'center',
  },
});
