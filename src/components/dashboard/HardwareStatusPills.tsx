import { MaterialCommunityIcons } from '@expo/vector-icons';
import React from 'react';
import { StyleSheet, Text, View } from 'react-native';
import { useTheme } from '../../context/ThemeContext';
import { Spacing, Typography } from '../../theme/theme';
import { useProductCatalog } from '../../hooks/useProductCatalog';

type PillDeviceType = {
  id?: string;
  type?: string;
  name?: string | null;
  points?: number;
  ledPoints?: number;
  led_points?: number;
  segments?: number;
  firmware?: string;
  firmware_ver?: number | string;
  rfMode?: string;
  rf_mode?: string;
  hwRfMode?: string;
  rfPairedCount?: number;
  rf_paired_count?: number;
  hwRfPairedCount?: number;
  rssi?: number | null;
  rssiList?: number[];
  isGroup?: boolean;
  ic_type?: string;
  color_sorting?: string;
};

interface HardwareStatusPillsProps {
  device: PillDeviceType;
  small?: boolean;
}

export const HardwareStatusPills = ({ device }: HardwareStatusPillsProps) => {
  const { Colors } = useTheme();
  const { getProfileById } = useProductCatalog();
  
  // Safe extraction with fallbacks
  const profile = getProfileById(device.type || device.id || '');
  
  const ledPointsRaw = device.points ?? device.ledPoints ?? device.led_points;
  const pts = typeof ledPointsRaw === 'number' ? ledPointsRaw : (profile?.defaultLedPoints ?? 8);

  const segsRaw = device.segments;
  const segs = typeof segsRaw === 'number' ? segsRaw : (profile?.defaultSegments ?? 2);

  const fwRaw = device.firmware ?? device.firmware_ver;
  const firmware = typeof fwRaw === 'string' ? `v${fwRaw.replace(/^v/, '')}` : (typeof fwRaw === 'number' ? `v${fwRaw}` : null);

  const rfModeRaw = device.rfMode ?? device.rf_mode ?? device.hwRfMode;
  const rfMode = typeof rfModeRaw === 'string' ? rfModeRaw : null;

  const rfPairedRaw = device.rfPairedCount ?? device.rf_paired_count ?? device.hwRfPairedCount;
  const rfPairedCount = typeof rfPairedRaw === 'number' ? rfPairedRaw : 0;

  const renderPill = (icon: keyof typeof import('@expo/vector-icons').MaterialCommunityIcons.glyphMap, text: string, color: string = Colors.textMuted) => (
    <View style={[styles.pill, { borderColor: color, backgroundColor: `${color}1A` }]}>
       <MaterialCommunityIcons name={icon} size={10} color={color} style={{ marginRight: 4 }} />
       <Text style={[styles.pillText, { color }]}>{text}</Text>
    </View>
  );

  return (
    <View style={styles.container}>
      {renderPill('led-strip', `${pts} L`, Colors.primary)}
      {renderPill('view-dashboard-outline', `${segs}S`, Colors.primary)}
      {firmware && renderPill('cellphone-arrow-down', firmware, '#4ade80')}
      
      {rfMode && (
         renderPill(
            rfMode === 'ALLOW_ALL' ? 'remote' : rfMode === 'ALLOW_PAIRED' ? 'lock' : 'remote-off',
            rfMode === 'ALLOW_ALL' ? 'All RF' : rfMode === 'ALLOW_PAIRED' ? `${rfPairedCount} RF Paired` : 'RF Blocked',
            rfMode === 'ALLOW_ALL' ? Colors.success : rfMode === 'ALLOW_PAIRED' ? '#FFA500' : Colors.error
         )
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: Spacing.xs,
    marginTop: Spacing.xs,
  },
  pill: {
    flexDirection: 'row',
    alignItems: 'center',
    borderWidth: 1,
    borderRadius: 8,
    paddingHorizontal: 6,
    paddingVertical: 2,
  },
  pillText: {
    fontSize: 9,
    fontWeight: 'bold',
    letterSpacing: 0.5,
  }
});
