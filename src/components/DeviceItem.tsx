import React from 'react';
import { TouchableOpacity, Text, StyleSheet, View } from 'react-native';
import { Colors, Typography, Layout } from '../theme/theme';
import { LinearGradient } from 'expo-linear-gradient';

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
      {isConnected && (
         <LinearGradient 
            colors={[Colors.primary, Colors.accent]} 
            start={{x: 0, y: 0}} end={{x: 1, y: 0}} 
            style={[StyleSheet.absoluteFill, { opacity: 0.15 }]} 
         />
      )}

      <View style={styles.info}>
        <View style={{ flexDirection: 'row', alignItems: 'center' }}>
          {isSelectionMode && (
             <View style={{ width: 22, height: 22, borderRadius: 11, borderWidth: 2, borderColor: isSelected ? Colors.primary : Colors.textMuted, marginRight: 12, backgroundColor: 'transparent', alignItems: 'center', justifyContent: 'center', overflow: 'hidden' }}>
               {isSelected && <LinearGradient colors={[Colors.primary, Colors.accent]} style={StyleSheet.absoluteFill} />}
               {isSelected && <Text style={{color: 'white', fontSize: 12, fontWeight: 'bold', zIndex: 1}}>✓</Text>}
             </View>
          )}
          {showGroupIcon && <Text style={{ fontSize: 18, marginRight: 8 }}>👥</Text>}
          <Text style={Typography.title}>{device.name || `SK8 - ${(device.id || '').replace(/:/g, '').slice(-6).toUpperCase()}`}</Text>
        </View>
        <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: 4 }}>
          {isSelectionMode && <View style={{ width: 34 }} />}
          <Text style={[Typography.caption, { color: Colors.textMuted }]}>
            {device.isGroup 
              ? `${(device as any).deviceIds?.length || 0} Devices` 
              : `MAC: ${device.id.toUpperCase()} ${device.rssi ? ` | RSSI: ${device.rssi} dBm` : ''}`
            }
          </Text>
        </View>
      </View>
      <View style={styles.status}>
        <Text style={[
          Typography.body, 
          { color: isConnected ? (showGroupIcon ? Colors.secondary : Colors.success) : Colors.primary, fontWeight: '800', letterSpacing: 1 }
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
    padding: 18,
    backgroundColor: 'rgba(15, 19, 29, 0.7)',
    borderRadius: Layout.borderRadius,
    marginBottom: 16,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.05)',
    overflow: 'hidden',
  },
  connectedContainer: {
    borderColor: 'rgba(0, 240, 255, 0.4)',
    borderWidth: 1,
  },
  zenggeContainer: {
    borderColor: 'rgba(255, 0, 85, 0.3)',
  },
  info: {
    flex: 1,
    zIndex: 2,
  },
  status: {
    marginLeft: 16,
    zIndex: 2,
  }
});
