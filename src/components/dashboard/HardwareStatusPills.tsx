import { MaterialCommunityIcons } from '@expo/vector-icons';
import React from 'react';
import { StyleSheet, Text, View } from 'react-native';
import { useTheme } from '../../context/ThemeContext';
import { Spacing, Typography } from '../../theme/theme';

interface HardwareStatusPillsProps {
  device: any;
}

export const HardwareStatusPills = ({ device }: HardwareStatusPillsProps) => {
  const { Colors } = useTheme();
  
  // Safe extraction with fallbacks
  const pts = device.points ?? device.led_points ?? (device.name?.toLowerCase().includes('soul') ? 43 : 8);
  const segs = device.segments ?? (device.name?.toLowerCase().includes('soul') ? 1 : 2);
  const strip = device.stripType ?? device.ic_type ?? 'WS2812B';
  const sort = device.sorting ?? device.color_sorting ?? 'GRB';
  const firmware = device.firmware_ver ? `v${device.firmware_ver}` : null;
  const rfMode = device.rf_mode || device.hwRfMode || null;
  const rfPairedCount = device.rf_paired_count ?? device.hwRfPairedCount ?? 0;

  const renderPill = (icon: any, text: string, color: string = Colors.textMuted) => (
    <View style={[styles.pill, { borderColor: color, backgroundColor: `${color}1A` }]}>
       <MaterialCommunityIcons name={icon} size={10} color={color} style={{ marginRight: 4 }} />
       <Text style={[styles.pillText, { color }]}>{text}</Text>
    </View>
  );

  return (
    <View style={styles.container}>
      {renderPill('led-strip', `${pts} L`, Colors.primary)}
      {renderPill('view-dashboard-outline', `${segs}S`, Colors.primary)}
      {renderPill('chip', strip, Colors.textMuted)}
      {renderPill('palette-swatch-outline', sort, Colors.textMuted)}
      {firmware && renderPill('cellphone-arrow-down', firmware, '#4ade80')}
      
      {rfMode && (
         renderPill(
            rfMode === 'ALLOW_ALL' ? 'remote' : rfMode === 'ALLOW_PAIRED' ? 'remote-lock' : 'remote-off',
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
