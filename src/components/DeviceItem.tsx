import React from 'react';
import { TouchableOpacity, Text, StyleSheet, View } from 'react-native';
import { Colors, Typography, Layout } from '../theme/theme';

interface DeviceItemProps {
  device: { name: string | null; id: string; rssi?: number | null; isGroup?: boolean };
  onPress: () => void;
  onLongPress?: () => void;
  isConnected: boolean;
  showGroupIcon?: boolean;
  isSelectionMode?: boolean;
  isSelected?: boolean;
}

export default function DeviceItem({ device, onPress, onLongPress, isConnected, showGroupIcon, isSelectionMode, isSelected }: DeviceItemProps) {
  const isZengge = device.name?.toLowerCase().includes('led') || device.name?.toLowerCase().includes('zengge') || device.name?.toLowerCase().includes('magic');

  return (
    <TouchableOpacity 
      style={[
        styles.container, 
        isConnected && styles.connectedContainer,
        isZengge && !isConnected && styles.zenggeContainer,
        showGroupIcon && { borderLeftWidth: 4, borderLeftColor: Colors.secondary },
        isSelectionMode && isSelected && { borderColor: Colors.primary }
      ]} 
      onPress={onPress}
      onLongPress={onLongPress}
      delayLongPress={300}
      activeOpacity={0.7}
    >
      <View style={styles.info}>
        <View style={{ flexDirection: 'row', alignItems: 'center' }}>
          {isSelectionMode && (
             <View style={{ width: 22, height: 22, borderRadius: 11, borderWidth: 2, borderColor: isSelected ? Colors.primary : Colors.textMuted, marginRight: 12, backgroundColor: isSelected ? Colors.primary : 'transparent', alignItems: 'center', justifyContent: 'center' }}>
               {isSelected && <Text style={{color: 'white', fontSize: 12, fontWeight: 'bold'}}>✓</Text>}
             </View>
          )}
          {showGroupIcon && <Text style={{ fontSize: 18, marginRight: 8 }}>👥</Text>}
          <Text style={Typography.title}>{`SK8 - ${(device.id || '').replace(/:/g, '').slice(-6).toUpperCase()}`}</Text>
        </View>
        <Text style={[Typography.caption, isSelectionMode && { marginLeft: 34 }]}>{device.id} {device.rssi ? `• ${device.rssi} dBm` : ''}</Text>
      </View>
      <View style={styles.status}>
        <Text style={[
          Typography.body, 
          { color: isConnected ? (showGroupIcon ? Colors.secondary : Colors.success) : Colors.primary, fontWeight: 'bold' }
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
