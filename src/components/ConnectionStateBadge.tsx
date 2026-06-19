import React from 'react';
import { StyleSheet, View, Text } from 'react-native';
import { DeviceConnectionState } from '../types/dashboard.types';
import { useTheme } from '../context/ThemeContext';

interface ConnectionStateBadgeProps {
  state: DeviceConnectionState;
  hideLabel?: boolean;
}

export const ConnectionStateBadge: React.FC<ConnectionStateBadgeProps> = ({ state, hideLabel }) => {
  const { Colors } = useTheme();

  let dotColor = Colors.textMuted;
  let labelText = '';

  switch (state) {
    case 'connected':
      dotColor = Colors.success || '#4CAF50';
      labelText = 'Connected';
      break;
    case 'connecting':
      dotColor = Colors.warning || '#FFC107';
      labelText = 'Connecting...';
      break;
    case 'reconnecting':
      dotColor = Colors.primary || '#2196F3';
      labelText = 'Reconnecting...';
      break;
    case 'disconnected':
      dotColor = Colors.error || '#F44336';
      labelText = 'Disconnected';
      break;
    case 'out_of_range':
      dotColor = '#9E9E9E';
      labelText = 'Out of Range';
      break;
  }

  return (
    <View style={styles.container}>
      <View style={[styles.dot, { backgroundColor: dotColor }]} />
      {!hideLabel && <Text style={[styles.label, { color: dotColor }]}>{labelText}</Text>}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 4,
  },
  dot: {
    width: 6,
    height: 6,
    borderRadius: 3,
  },
  label: {
    fontSize: 10,
    fontFamily: 'Inter-Medium',
    letterSpacing: 0.2,
  },
});
