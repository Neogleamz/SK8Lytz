/**
 * LocationPickerMap.web.tsx — Web Platform Stub
 * react-native-maps uses codegenNativeComponent which is not available
 * in react-native-web. Metro auto-picks this .web.tsx on web builds.
 */
import React from 'react';
import { StyleSheet, Text, View } from 'react-native';
import { Spacing } from '../theme/theme';

interface LocationPickerMapProps {
  style?: import('react-native').StyleProp<import('react-native').ViewStyle>;
  initialRegion?: {
    latitude: number;
    longitude: number;
    latitudeDelta: number;
    longitudeDelta: number;
  };
  onRegionChangeComplete?: (region: { latitude: number; longitude: number; }) => void;
  children?: React.ReactNode;
}

export default function LocationPickerMap(_props: LocationPickerMapProps) {
  return (
    <View style={styles.container}>
      <Text style={styles.text}>📍 Map view not available on web.</Text>
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
  },
  text: {
    color: 'rgba(255,255,255,0.4)',
    fontSize: 14,
    textAlign: 'center',
  },
});
