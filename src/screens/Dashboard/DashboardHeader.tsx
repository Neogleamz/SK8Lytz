// NOTE: This file exports DashboardHeaderBanners (bluetooth/battery status banners for the
// dashboard top chrome). It is NOT the main page header component. The main DashboardHeader
// lives at src/components/dashboard/DashboardHeader.tsx. The similar name is intentional
// (scoped to the Dashboard/ sub-folder) but can cause confusion — do not rename this file
// as it would create import churn; see plan chore/teardown-dead-code-sweep step 8.
import React, { useMemo } from 'react';
import { StyleSheet, View, Text, TouchableOpacity, Platform, Linking } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { ThemePalette } from '../../theme/theme';

interface DashboardHeaderBannersProps {
  batteryTier?: string;
  isBluetoothEnabled: boolean;
  Colors: ThemePalette;
  styles: Record<string, ReturnType<typeof StyleSheet.create>[string]>;
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
