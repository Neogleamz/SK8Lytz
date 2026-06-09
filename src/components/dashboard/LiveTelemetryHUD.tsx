import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../../context/ThemeContext';
import { Spacing } from '../../theme/theme';

interface LiveTelemetryHUDProps {
  gpsSpeed: number;
  peakGForce: number;
  sessionDistanceMiles: number;
  sessionDurationSec: number;
}

export const LiveTelemetryHUD: React.FC<LiveTelemetryHUDProps> = ({
  gpsSpeed,
  peakGForce,
  sessionDistanceMiles,
  sessionDurationSec
}) => {
  const { Colors } = useTheme();

  // Format duration mm:ss
  const formatDuration = (secs: number) => {
    const m = Math.floor(secs / 60);
    const s = secs % 60;
    return `${m}:${s.toString().padStart(2, '0')}`;
  };

  return (
    <View style={[styles.container, { backgroundColor: Colors.surface, borderColor: Colors.border }]}>
      <View style={styles.metric}>
        <MaterialCommunityIcons name="speedometer" size={16} color={Colors.primary} />
        <Text style={[styles.value, { color: Colors.text }]}>{Math.round(gpsSpeed)}<Text style={[styles.unit, { color: Colors.textDim }]}> mph</Text></Text>
      </View>
      <View style={styles.divider} />
      <View style={styles.metric}>
        <MaterialCommunityIcons name="gauge" size={16} color={Colors.accent} />
        <Text style={[styles.value, { color: Colors.text }]}>{peakGForce.toFixed(1)}<Text style={[styles.unit, { color: Colors.textDim }]}> g</Text></Text>
      </View>
      <View style={styles.divider} />
      <View style={styles.metric}>
        <MaterialCommunityIcons name="map-marker-distance" size={16} color={Colors.success} />
        <Text style={[styles.value, { color: Colors.text }]}>{sessionDistanceMiles.toFixed(1)}<Text style={[styles.unit, { color: Colors.textDim }]}> mi</Text></Text>
      </View>
      <View style={styles.divider} />
      <View style={styles.metric}>
        <MaterialCommunityIcons name="timer-outline" size={16} color={Colors.warning} />
        <Text style={[styles.value, { color: Colors.text }]}>{formatDuration(sessionDurationSec)}</Text>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: Spacing.md,
    paddingVertical: Spacing.sm,
    marginHorizontal: Spacing.md,
    marginBottom: Spacing.sm,
    borderRadius: 16,
    borderWidth: 1,
    ...require('react-native').Platform.select({
      web: { boxShadow: '0px 2px 4px rgba(0,0,0,0.1)' } as any, // MIGRATION-SHIM
      default: {
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 4,
        elevation: 2,
      }
    }),
  },
  metric: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
  },
  value: {
    fontFamily: 'monospace',
    fontSize: 14,
    fontWeight: '700',
  },
  unit: {
    fontSize: 10,
    fontWeight: '500',
  },
  divider: {
    width: 1,
    height: 16,
    backgroundColor: 'rgba(255,255,255,0.1)',
  }
});
