import { MaterialCommunityIcons } from '@expo/vector-icons';
import { LinearGradient } from 'expo-linear-gradient';
import React from 'react';
import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { useTheme } from '../context/ThemeContext';
import { isStale } from '../hooks/useDeviceStateLedger';
import { useProductCatalog } from '../hooks/useProductCatalog';
import { Layout, Spacing } from '../theme/theme';
import type { DevicePatternState } from '../types/dashboard.types';
import { HardwareStatusPills } from './dashboard/HardwareStatusPills';
import { ConnectionStateBadge } from './ConnectionStateBadge';
import type { DeviceConnectionState } from '../types/dashboard.types';

interface DeviceItemProps {
  device: { name: string | null; id: string; rssi?: number | null; rssiList?: number[]; isGroup?: boolean; type?: string; product_type?: string; };
  onPress: () => void;
  onLongPress?: () => void;
  isConnected: boolean;
  showGroupIcon?: boolean;
  isSelectionMode?: boolean;
  isSelected?: boolean;
  onPowerToggle?: () => void;
  isPoweredOn?: boolean;
  /** Optional: last known pattern state from useDeviceStateLedger — drives preview swatch. */
  ledgerState?: DevicePatternState;
  connectionState?: DeviceConnectionState;
}

function DeviceItem({ device, onPress, onLongPress, isConnected, showGroupIcon, isSelectionMode, isSelected, onPowerToggle, isPoweredOn = true, ledgerState, connectionState }: DeviceItemProps) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const { allProfiles } = useProductCatalog();
  
  const handlePowerToggle = React.useCallback((e: import('react-native').GestureResponderEvent) => {
    e.stopPropagation();
    onPowerToggle?.();
  }, [onPowerToggle]);

  const profileId = device.type || device.product_type;
  let isZengge = true;
  if (profileId && allProfiles.some(p => p.id === profileId)) {
    isZengge = false;
  } else if (device.name && allProfiles.some(p => device.name!.toUpperCase().includes(p.id.toUpperCase()))) {
    isZengge = false;
  }
  
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
      accessibilityRole="button"
      accessibilityLabel={`Device ${displayName}, ${isConnected ? 'Connected' : 'Disconnected'}`}
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

              <View style={{ flexDirection: 'column', justifyContent: 'center' }}>
                <Text style={[styles.deviceName, { color: isConnected ? Colors.text : Colors.textMuted }]} numberOfLines={1}>
                  {displayName}
                </Text>
                {connectionState && <View style={{ marginTop: 2 }}><ConnectionStateBadge state={connectionState} /></View>}
              </View>
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
                      style={i > 0 ? styles.rssiIconMargin : styles.rssiIcon}
                    />
                  ))}
                </View>
              ) : (
                <MaterialCommunityIcons 
                  name={!device.rssi ? 'wifi-off' : device.rssi > -60 ? 'wifi-strength-4' : device.rssi > -80 ? 'wifi-strength-2' : 'wifi-strength-1'} 
                  size={14} 
                  color={!device.rssi ? Colors.error : device.rssi > -60 ? Colors.success : device.rssi > -80 ? '#FFA500' : Colors.error}
                  style={styles.rssiIcon}
                />
              )}

              {onPowerToggle && (
                <TouchableOpacity 
                  style={[
                    styles.powerIconCircleSmall, 
                    { backgroundColor: isPoweredOn ? 'rgba(0, 240, 255, 0.1)' : 'rgba(255,255,255,0.05)' }
                  ]}
                  onPress={handlePowerToggle}
                  activeOpacity={0.6}
                  accessibilityRole="button"
                  accessibilityLabel="Toggle device power"
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
          
          {/* Bottom: Hardware Pills + Pattern Preview */}
          {!device.isGroup && (
            <View style={styles.footerRow}>
              {isSelectionMode && <View style={{ width: 22 + Spacing.sm }} />}
              <HardwareStatusPills device={device} />
            </View>
          )}
          {/* Pattern Preview — only when ledger state exists */}
          {!device.isGroup && ledgerState && (
            <View style={styles.patternPreviewRow}>
              <View
                style={[
                  styles.patternSwatch,
                  {
                    backgroundColor: ledgerState.fgColor ?? '#888888',
                    opacity: isStale(ledgerState.ts) ? 0.4 : 1,
                  },
                ]}
              />
              <Text
                style={[
                  styles.patternLabelText,
                  { opacity: isStale(ledgerState.ts) ? 0.4 : 0.8 },
                ]}
                numberOfLines={1}
              >
                {ledgerState.patternLabel}
              </Text>
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
  rssiIcon: {
    opacity: 0.8,
  },
  rssiIconMargin: {
    marginLeft: 2,
    opacity: 0.8,
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
  },
  patternPreviewRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 4,
    gap: 6,
  },
  patternSwatch: {
    width: 10,
    height: 10,
    borderRadius: 5,
  },
  patternLabelText: {
    fontSize: 10,
    color: '#AAAAAA',
    fontFamily: 'Inter-Medium',
    flex: 1,
  },
});

export default React.memo(DeviceItem);
