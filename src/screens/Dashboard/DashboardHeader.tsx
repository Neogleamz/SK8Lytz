import React, { useMemo } from 'react';
import { View, Text, TouchableOpacity, Platform, Linking } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { ThemePalette } from '../../theme/theme';

interface DashboardHeaderBannersProps {
  batteryTier?: string;
  isBluetoothEnabled: boolean;
  Colors: ThemePalette;
  styles: Record<string, any>;
}

export const DashboardHeaderBanners = React.memo(({
  batteryTier,
  isBluetoothEnabled,
  Colors,
  styles,
}: DashboardHeaderBannersProps) => {
  const BatteryWarningBanner = useMemo(() => {
    if (batteryTier !== 'PAUSED') return null;
    return (
      <View style={styles.btBanner}>
        <MaterialCommunityIcons name="battery-alert" size={24} color="#FFF" />
        <Text style={styles.btBannerText}>
          Scanning paused. Battery below 15%.
        </Text>
      </View>
    );
  }, [batteryTier, styles]);

  const BluetoothWarningBanner = useMemo(() => {
    if (isBluetoothEnabled || Platform.select({ web: true, default: false })) return null;
    return (
      <TouchableOpacity 
        onPress={() => Linking.openSettings()}
        style={styles.btBanner}
        activeOpacity={0.9}
      >
        <MaterialCommunityIcons name="alert-circle" size={24} color="#FFF" />
        <Text style={styles.btBannerText}>
          Bluetooth Disabled or Permissions Denied!
        </Text>
        <MaterialCommunityIcons name="chevron-right" size={20} color="#FFF" />
      </TouchableOpacity>
    );
  }, [isBluetoothEnabled, styles]);

  return (
    <>
      {BluetoothWarningBanner}
      {BatteryWarningBanner}
    </>
  );
});
