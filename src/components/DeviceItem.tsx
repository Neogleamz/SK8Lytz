import { MaterialCommunityIcons } from '@expo/vector-icons';
import { LinearGradient } from 'expo-linear-gradient';
import React from 'react';
import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { useTheme } from '../context/ThemeContext';
import { Layout, Spacing } from '../theme/theme';
import { HardwareStatusPills } from './dashboard/HardwareStatusPills';

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
        styles.deviceCardWrapper,
        isSelectionMode && isSelected && { shadowColor: Colors.primary, elevation: 15 }
      ]} 
      onPress={onPress}
      onLongPress={onLongPress}
      delayLongPress={300}
      activeOpacity={0.7}
    >
      <LinearGradient 
        colors={isConnected 
          ? [isSelectionMode && isSelected ? Colors.primary : 'rgba(0, 240, 255, 0.5)', isSelectionMode && isSelected ? Colors.accent : 'rgba(112, 0, 255, 0.5)'] 
          : isZengge ? ['rgba(255, 0, 85, 0.3)', 'rgba(255, 0, 85, 0.1)'] : ['rgba(255,255,255,0.08)', 'rgba(255,255,255,0.02)']}
        start={{ x: 0, y: 0 }} end={{ x: 1, y: 1 }}
        style={styles.deviceCardGradient}
      >
        <View style={styles.deviceCardInner}>
          {/* Glass Refraction */}
          <View style={styles.deviceCardRefraction} />

          <View style={styles.headerRow}>
            {/* Left side: Avatar + Name */}
            <View style={styles.leftContent}>
              {isSelectionMode && (
                <View style={[styles.selectionCircle, isSelected && { borderColor: 'transparent' }]}>
                  {isSelected && <LinearGradient colors={[Colors.primary, Colors.accent]} style={StyleSheet.absoluteFill} />}
                  {isSelected && <Text style={styles.selectionCheck}>✓</Text>}
                </View>
              )}

              <View style={styles.avatarPillSmall}>
                <MaterialCommunityIcons 
                  name={showGroupIcon ? "skate" : "roller-skate"} 
                  size={14} 
                  color={isConnected ? Colors.primary : Colors.textMuted} 
                />
                {isConnected && <View style={[styles.avatarStatusDot, showGroupIcon && { backgroundColor: Colors.secondary }]} />}
              </View>

              <Text style={[styles.deviceName, { color: isConnected ? Colors.text : Colors.textMuted }]} numberOfLines={1}>
                {displayName}
              </Text>
            </View>

            {/* Right side: RSSI & Power */}
            <View style={styles.rightContent}>
              {(device.rssiList && device.rssiList.length > 0) ? (
                <View style={styles.rssiContainer}>
                  {device.rssiList.map((r, i) => (
                    <MaterialCommunityIcons 
                      key={i}
                      name={r === null ? 'wifi-off' : r > -60 ? 'wifi-strength-4' : r > -80 ? 'wifi-strength-2' : 'wifi-strength-1'} 
                      size={14} 
                      color={r === null ? Colors.error : r > -60 ? Colors.success : r > -80 ? '#FFA500' : Colors.error}
                      style={{ marginLeft: i > 0 ? 2 : 0, opacity: 0.8 }}
                    />
                  ))}
                </View>
              ) : (
                <MaterialCommunityIcons 
                  name={!device.rssi ? 'wifi-off' : device.rssi > -60 ? 'wifi-strength-4' : device.rssi > -80 ? 'wifi-strength-2' : 'wifi-strength-1'} 
                  size={14} 
                  color={!device.rssi ? Colors.error : device.rssi > -60 ? Colors.success : device.rssi > -80 ? '#FFA500' : Colors.error}
                  style={{ opacity: 0.8 }}
                />
              )}

              {onPowerToggle && (
                <TouchableOpacity 
                  style={[
                    styles.powerIconCircleSmall, 
                    { backgroundColor: isPoweredOn ? 'rgba(0, 240, 255, 0.1)' : 'rgba(255,255,255,0.05)' }
                  ]}
                  onPress={(e) => { e.stopPropagation(); onPowerToggle(); }}
                  activeOpacity={0.6}
                >
                  <MaterialCommunityIcons 
                    name="power" 
                    size={14} 
                    color={isPoweredOn ? Colors.primary : Colors.textMuted} 
                  />
                </TouchableOpacity>
              )}
            </View>
          </View>
          
          {/* Bottom: Hardware Pills */}
          {!device.isGroup && (
            <View style={styles.footerRow}>
              {isSelectionMode && <View style={{ width: 22 + Spacing.sm }} />}
              <HardwareStatusPills device={device} />
            </View>
          )}
        </View>
      </LinearGradient>
    </TouchableOpacity>
  );
}

const createStyles = (Colors: import('../theme/theme').ThemePalette) => StyleSheet.create({
  deviceCardWrapper: {
    marginBottom: Spacing.sm,
    borderRadius: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.2,
    shadowRadius: 8,
    elevation: 4,
  },
  deviceCardGradient: {
    borderRadius: 16,
    padding: 1, // Border thickness
  },
  deviceCardInner: {
    backgroundColor: Colors.isDark ? 'rgba(20, 25, 35, 0.85)' : 'rgba(255, 255, 255, 0.95)',
    borderRadius: 15,
    padding: Spacing.md,
    overflow: 'hidden',
  },
  deviceCardRefraction: {
    position: 'absolute',
    top: -30,
    left: -30,
    width: 100,
    height: 100,
    backgroundColor: 'rgba(255, 255, 255, 0.02)',
    transform: [{ rotate: '45deg' }],
  },
  headerRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    zIndex: 2,
  },
  leftContent: {
    flexDirection: 'row',
    alignItems: 'center',
    flex: 1,
  },
  selectionCircle: {
    width: 18,
    height: 18,
    borderRadius: 9,
    borderWidth: 1.5,
    borderColor: Colors.textMuted,
    marginRight: Spacing.sm,
    backgroundColor: 'transparent',
    alignItems: 'center',
    justifyContent: 'center',
    overflow: 'hidden',
  },
  selectionCheck: {
    color: 'white',
    fontSize: 10,
    fontWeight: '900',
    zIndex: 1,
  },
  avatarPillSmall: {
    backgroundColor: 'rgba(255,255,255,0.05)',
    paddingHorizontal: Spacing.sm,
    paddingVertical: 4,
    borderRadius: 12,
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
    marginRight: Spacing.sm,
  },
  avatarStatusDot: {
    width: 4,
    height: 4,
    borderRadius: 2,
    backgroundColor: Colors.success,
    marginLeft: 4,
    borderWidth: 0.5,
    borderColor: Colors.background,
  },
  deviceName: {
    fontSize: 14,
    fontWeight: '800',
    letterSpacing: 0.5,
    fontFamily: 'Righteous',
    flexShrink: 1,
  },
  rightContent: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Spacing.md,
  },
  rssiContainer: {
    flexDirection: 'row',
  },
  powerIconCircleSmall: {
    width: 24,
    height: 24,
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
  },
  footerRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: Spacing.xs,
    zIndex: 2,
  }
});
