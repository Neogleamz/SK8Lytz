import React from 'react';
import { TouchableOpacity, Text, StyleSheet, View } from 'react-native';
import { Typography, Layout, Spacing } from '../theme/theme';
import { useTheme } from '../context/ThemeContext';
import { LinearGradient } from 'expo-linear-gradient';
import { MaterialCommunityIcons } from '@expo/vector-icons';

interface DeviceItemProps {
  device: { name: string | null; id: string; rssi?: number | null; rssiList?: number[]; isGroup?: boolean };
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
  const isZengge = (device.name?.toLowerCase().includes('led') || device.name?.toLowerCase().includes('zengge') || device.name?.toLowerCase().includes('magic')) && !device.name?.includes('HALOZ') && !device.name?.includes('SOULZ');
  
  const displayName = (!device.name || isZengge) 
    ? `SK8Lytz-${(device.id || '').replace(/:/g, '').slice(-4).toUpperCase()}` 
    : device.name;

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
             <View style={{ width: 22, height: 22, borderRadius: 11, borderWidth: 2, borderColor: isSelected ? Colors.primary : Colors.textMuted, marginRight: Spacing.md, backgroundColor: 'transparent', alignItems: 'center', justifyContent: 'center', overflow: 'hidden' }}>
               {isSelected && <LinearGradient colors={[Colors.primary, Colors.accent]} style={StyleSheet.absoluteFill} />}
               {isSelected && <Text style={{color: 'white', fontSize: 12, fontWeight: 'bold', zIndex: 1}}>✓</Text>}
             </View>
          )}
          <View style={{ flexDirection: 'row', marginRight: Spacing.sm, alignItems: 'center' }}>
            <MaterialCommunityIcons 
              name="roller-skate" 
              size={22} 
              color={Colors.primary} 
            />
            {showGroupIcon && (
              <MaterialCommunityIcons 
                name="roller-skate" 
                size={22} 
                color={Colors.primary} 
                style={{ marginLeft: -10, opacity: 0.8 }} 
              />
            )}
          </View>
          <View style={{ flexDirection: 'row', alignItems: 'center', flex: 1, flexWrap: 'wrap' }}>
            <Text style={[Typography.title, { color: Colors.primary, marginRight: Spacing.sm }]} numberOfLines={0}>{displayName}</Text>
            
            {(device.rssiList && device.rssiList.length > 0) ? (
              <View style={{ flexDirection: 'row' }}>
                {device.rssiList.map((r, i) => (
                  <MaterialCommunityIcons 
                    key={i}
                    name={r === null ? 'wifi-off' : r > -60 ? 'wifi-strength-4' : r > -80 ? 'wifi-strength-2' : 'wifi-strength-1'} 
                    size={16} 
                    color={r === null ? Colors.error : r > -60 ? Colors.success : r > -80 ? '#FFA500' : Colors.error}
                    style={{ marginLeft: i > 0 ? 4 : 0, opacity: 0.8 }}
                  />
                ))}
              </View>
            ) : (
              <MaterialCommunityIcons 
                name={!device.rssi ? 'wifi-off' : device.rssi > -60 ? 'wifi-strength-4' : device.rssi > -80 ? 'wifi-strength-2' : 'wifi-strength-1'} 
                size={16} 
                color={!device.rssi ? Colors.error : device.rssi > -60 ? Colors.success : device.rssi > -80 ? '#FFA500' : Colors.error}
                style={{ opacity: 0.8 }}
              />
            )}
          </View>
        </View>
        
        {!device.isGroup && (
          <View style={{ flexDirection: 'row', alignItems: 'center', marginTop: Spacing.xs }}>
            {isSelectionMode && <View style={{ width: 34 }} />}
            <Text style={[Typography.caption, { color: Colors.textMuted, flex: 1 }]} numberOfLines={0}>
                {(() => {
                  const devAny = device as any;
                  // If unqueried during scan, use safe defaults
                  const pts = devAny.points ?? (devAny.name?.toLowerCase().includes('soul') ? '43' : '8');
                  const segs = devAny.segments ?? (devAny.name?.toLowerCase().includes('soul') ? '1' : '2');
                  const strip = devAny.stripType ?? 'WS2812B';
                  const sort = devAny.sorting ?? 'GRB';
                  
                  const id = device.id.toUpperCase();
                  const mac4 = id.startsWith('SIM-') 
                    ? `000${id.split('-').pop()}`.slice(-4)
                    : id.replace(/:/g, '').slice(-4);

                  return `${pts}-${segs}-${strip}-${sort}-${mac4}`;
                })()}
              </Text>
          </View>
        )}
      </View>
      <View style={{ flexDirection: 'row', alignItems: 'center', zIndex: 2 }}>
        {onPowerToggle && (
          <TouchableOpacity 
            style={{ marginRight: Spacing.md, width: 36, height: 36, borderRadius: 18, backgroundColor: isPoweredOn ? 'rgba(0, 240, 255, 0.15)' : 'rgba(255, 255, 255, 0.1)', justifyContent: 'center', alignItems: 'center', borderWidth: 1, borderColor: isPoweredOn ? 'rgba(0, 240, 255, 0.3)' : 'rgba(255,255,255,0.2)' }}
            onPress={(e) => { e.stopPropagation(); onPowerToggle(); }}
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
    padding: Spacing.lg,
    backgroundColor: Colors.isDark ? 'rgba(15, 19, 29, 0.7)' : Colors.surface,
    borderRadius: Layout.borderRadius,
    marginBottom: Spacing.lg,
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
    marginLeft: Spacing.lg,
    zIndex: 2,
  }
});
