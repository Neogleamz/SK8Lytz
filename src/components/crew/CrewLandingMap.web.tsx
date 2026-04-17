import React from 'react';
import { View, Text } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../../context/ThemeContext';
import { createStyles } from './CrewStyles';
import { Spacing } from '../../theme/theme';

export function CrewLandingMap({ 
  nearbySpots, 
  nearbySessions, 
  pulseAnim, 
  handleJoinById, 
  locationCoords, 
  discoverRadiusMi 
}: any) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  return (
    <View style={[styles.hubEmptyCard, { flex: 1, justifyContent: 'center', margin: 0, borderRadius: 0, borderWidth: 0 }]}>
      <MaterialCommunityIcons name="web" size={32} color={Colors.textMuted} />
      <Text style={styles.hubEmptyText}>Interactive map is available on iOS and Android.</Text>
      <Text style={[styles.hubEmptyText, { fontSize: 11, marginTop: Spacing.xs }]}>Please use the mobile app to view the Radar.</Text>
    </View>
  );
}
