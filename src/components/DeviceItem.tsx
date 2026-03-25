import React from 'react';
import { TouchableOpacity, Text, StyleSheet, View } from 'react-native';
import { Typography, Layout } from '../theme/theme';
import { useTheme } from '../context/ThemeContext';
import { LinearGradient } from 'expo-linear-gradient';
import { MaterialCommunityIcons } from '@expo/vector-icons';

interface DeviceItemProps {
  device: { name: string | null; id: string; rssi?: number | null; isGroup?: boolean };
  onPress: () => void;
  onLongPress?: () => void;
  isConnected: boolean;
  showGroupIcon?: boolean;
  isSelectionMode?: boolean;
  isSelected?: boolean;
  onPowerToggle?: () => void;
  isPoweredOn?: boolean;
}

export default function DeviceItem({ device, onPress, onLongPress, isConnected, showGroupIcon, isSelectionMode, isSelected, onPowerToggle, isPoweredOn = true }: DeviceItemProps) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
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
          {showGroupIcon && <Text style={{ fontSize: 18, marginRight: 8 }}>🛼</Text>}
          <Text style={[Typography.title, { color: Colors.primary }]}>{device.name || `SK8 - ${(device.id || '').replace(/:/g, '').slice(-6).toUpperCase()}`}</Text>
        </View>
          <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: 4 }}>
            {isSelectionMode && <View style={{ width: 34 }} />}
            {device.isGroup ? (
              <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                <View style={{ 
                  width: 6, 
                  height: 6, 
                  borderRadius: 3, 
                  backgroundColor: (device as any).connectedCount === 0 ? Colors.error : ((device as any).connectedCount < (device as any).deviceIds?.length ? '#FFA500' : Colors.success), 
                  marginRight: 6 
                }} />
                <Text style={[Typography.caption, { 
                  color: (device as any).connectedCount === 0 ? Colors.error : ((device as any).connectedCount < (device as any).deviceIds?.length ? '#FFA500' : Colors.success), 
                  fontSize: 10, 
                  fontWeight: 'bold' 
                }]}>
                  PAIRED ({(device as any).connectedCount || 0})
                </Text>
              </View>
            ) : (
              <Text style={[Typography.caption, { color: Colors.textMuted }]}>
                MAC: {device.id.toUpperCase()} {device.rssi ? ` | RSSI: ${device.rssi} dBm` : ''}
              </Text>
            )}
          </View>
      </View>
      <View style={{ flexDirection: 'row', alignItems: 'center', zIndex: 2 }}>
        {onPowerToggle && (
          <TouchableOpacity 
            style={{ marginRight: 12, width: 36, height: 36, borderRadius: 18, backgroundColor: isPoweredOn ? 'rgba(0, 240, 255, 0.15)' : 'rgba(255, 255, 255, 0.1)', justifyContent: 'center', alignItems: 'center', borderWidth: 1, borderColor: isPoweredOn ? 'rgba(0, 240, 255, 0.3)' : 'rgba(255,255,255,0.2)' }}
            onPress={onPowerToggle}
            activeOpacity={0.6}
          >
            <MaterialCommunityIcons name="power" size={20} color={isPoweredOn ? Colors.primary : Colors.textMuted} />
          </TouchableOpacity>
        )}
        <View style={styles.status}>
          <Text style={[
            Typography.body, 
            { color: isConnected ? (showGroupIcon ? Colors.secondary : Colors.success) : Colors.primary, fontWeight: '800', letterSpacing: 1 }
          ]}>
            {isConnected ? 'CONNECTED' : 'CONNECT'}
          </Text>
        </View>
      </View>
    </TouchableOpacity>
  );
}

const createStyles = (Colors: import('../theme/theme').ThemePalette) => StyleSheet.create({
  container: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: 18,
    backgroundColor: Colors.isDark ? 'rgba(15, 19, 29, 0.7)' : Colors.surface,
    borderRadius: Layout.borderRadius,
    marginBottom: 16,
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.08)',
    overflow: 'hidden',
  },
  connectedContainer: {
    borderColor: `${Colors.primary}66`,
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
