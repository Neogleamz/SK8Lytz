import React from 'react';
import { TouchableOpacity, Text, StyleSheet, View } from 'react-native';
import { Colors, Typography, Layout } from '../theme/theme';

interface DeviceItemProps {
  device: { name: string | null; id: string; rssi?: number | null };
  onPress: () => void;
  isConnected: boolean;
}

export default function DeviceItem({ device, onPress, isConnected }: DeviceItemProps) {
  const isZengge = device.name?.toLowerCase().includes('led') || device.name?.toLowerCase().includes('zengge') || device.name?.toLowerCase().includes('magic');

  return (
    <TouchableOpacity 
      style={[
        styles.container, 
        isConnected && styles.connectedContainer,
        isZengge && !isConnected && styles.zenggeContainer
      ]} 
      onPress={onPress}
      activeOpacity={0.7}
    >
      <View style={styles.info}>
        <Text style={Typography.title}>{device.name || 'Unknown Device'}</Text>
        <Text style={Typography.caption}>{device.id} {device.rssi ? `• ${device.rssi} dBm` : ''}</Text>
      </View>
      <View style={styles.status}>
        <Text style={[
          Typography.body, 
          { color: isConnected ? Colors.success : Colors.primary, fontWeight: 'bold' }
        ]}>
          {isConnected ? 'CONNECTED' : 'CONNECT'}
        </Text>
      </View>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: 16,
    backgroundColor: Colors.surface,
    borderRadius: Layout.borderRadius,
    marginBottom: 12,
    borderWidth: 1,
    borderColor: Colors.surfaceHighlight,
  },
  connectedContainer: {
    borderColor: Colors.success,
    backgroundColor: 'rgba(0, 230, 118, 0.05)',
  },
  zenggeContainer: {
    borderColor: Colors.primary,
    backgroundColor: 'rgba(255, 0, 127, 0.05)',
  },
  info: {
    flex: 1,
  },
  status: {
    marginLeft: 16,
  }
});
