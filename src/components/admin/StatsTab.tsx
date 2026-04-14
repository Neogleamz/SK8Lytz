import * as Device from 'expo-device';
import React from 'react';
import { Platform, ScrollView, Text, View } from 'react-native';
import { TelemetryStats } from '../../hooks/useAdminTelemetry';
import { adminStyles as styles } from './adminStyles';
import { TabProps } from './DeviceTab';

export interface StatsTabProps extends TabProps {
  stats: TelemetryStats | null;
}

const StatRow = ({ label, value, color, muted }: { label: string, value: string, color: string, muted: string }) => (
  <View style={styles.statRow}>
    <Text style={[styles.statLabel, { color: muted }]}>{label}</Text>
    <Text style={[styles.statValue, { color }]}>{value}</Text>
  </View>
);

export const StatsTab = React.memo(({ stats, textMuted, textPrimary, cardBg, borderColor }: StatsTabProps) => {
  if (!stats) return null;
  const gbMem = Device.totalMemory ? (Device.totalMemory / 1024 ** 3).toFixed(1) + ' GB' : 'Unknown';
  const osDisplay = `${Device.osName || Platform.OS} ${Device.osVersion || ''}`.trim();

  return (
    <ScrollView style={styles.tabContent}>
      <Text style={[styles.statSection, { color: textPrimary }]}>📱 Device & App Telemetry</Text>
      <View style={[styles.statCard, { backgroundColor: cardBg, borderColor }]}>
        <StatRow label="Brand / Model" value={`${Device.brand || ''} ${Device.modelName || 'Unknown'}`.trim()} color={textPrimary} muted={textMuted} />
        <StatRow label="Manufacturer" value={Device.manufacturer || 'Unknown'} color={textPrimary} muted={textMuted} />
        <StatRow label="Operating System" value={osDisplay} color={textPrimary} muted={textMuted} />
        <StatRow label="Battery Level" value={stats.batteryLevel !== undefined && stats.batteryLevel !== -1 ? `${Math.round(stats.batteryLevel * 100)}%` : 'Unknown'} color={textPrimary} muted={textMuted} />
        <StatRow label="BLE Target MAC" value={stats.primaryBleMac || 'N/A'} color="#00f0ff" muted={textMuted} />
        <StatRow label="Total RAM" value={gbMem} color={textPrimary} muted={textMuted} />
        <StatRow label="Log Storage" value={`${(stats.storageBytesEstimate / 1024).toFixed(2)} KB`} color={textPrimary} muted={textMuted} />
      </View>
      <Text style={[styles.statSection, { color: textPrimary }]}>📊 Analytics Overview</Text>
      <View style={[styles.statCard, { backgroundColor: cardBg, borderColor }]}>
        <StatRow label="Total Events Logged" value={String(stats.totalEvents)} color={textPrimary} muted={textMuted} />
      </View>
      <Text style={[styles.statSection, { color: textPrimary }]}>🕹️ Mode Usage</Text>
      <View style={[styles.statCard, { backgroundColor: cardBg, borderColor }]}>
        {Object.entries(stats.modeUsage).sort((a,b) => (b[1] as number) - (a[1] as number)).map(([mode, count]) => (
          <StatRow key={mode} label={mode} value={`${count}×`} color={textPrimary} muted={textMuted} />
        ))}
      </View>
    </ScrollView>
  );
});
