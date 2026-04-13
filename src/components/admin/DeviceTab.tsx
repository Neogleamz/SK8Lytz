import React from 'react';
import { ScrollView, View, Text } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Spacing } from '../../theme/theme';
import { adminStyles as styles } from './adminStyles';
import { formatLogTime } from '../../hooks/useAdminTelemetry';
import { LogEntry } from '../../services/AppLogger';

export interface TabProps {
  textMuted: string;
  textPrimary: string;
  cardBg: string;
  borderColor: string;
}

export interface DeviceTabProps extends TabProps {
  logs: LogEntry[];
  deviceConfigs: Record<string, any>;
}

export const DeviceTab = React.memo(({ logs, deviceConfigs, textMuted, textPrimary, cardBg, borderColor }: DeviceTabProps) => {
  const earliest = new Map<string, number>();
  const latest = new Map<string, number>();
  const uniqueMeta = new Map<string, any>();
  const firmwares = new Map<string, string>();

  logs.forEach((l: LogEntry) => {
    if (l.e === 'DEVICE_CONNECTED' && l.d?.id && l.d?.firmware) {
      if (!firmwares.has(l.d.id)) firmwares.set(l.d.id, l.d.firmware);
    }
    if (l.e === 'DEVICE_DISCOVERED' || l.e === 'DEVICE_CONNECTED') {
      const id = l.d?.id;
      if (!id) return;
      if (!latest.has(id)) latest.set(id, l.t);
      earliest.set(id, l.t);
      if (!uniqueMeta.has(id) && l.e === 'DEVICE_DISCOVERED') uniqueMeta.set(id, l.d);
    }
  });

  const uniqueIds = Array.from(latest.keys());

  return (
    <ScrollView style={styles.tabContent}>
      {uniqueIds.length === 0 && (
        <Text style={[styles.emptyText, { color: textMuted }]}>No devices logged yet.</Text>
      )}
      {uniqueIds.map((id, i) => {
        const meta = uniqueMeta.get(id) || {};
        const config = deviceConfigs[id] || {};
        return (
          <View key={i} style={[styles.deviceCard, { backgroundColor: cardBg, borderColor }]}>
            <MaterialCommunityIcons name="bluetooth-connect" size={24} color="#9D4EFF" />
            <View style={{ marginLeft: Spacing.md, flex: 1 }}>
              <Text style={[styles.deviceName, { color: textPrimary }]}>{config.name || meta.name || 'Unknown'}</Text>
              <Text style={[styles.deviceDetail, { color: textMuted }]}>MAC Address: {id}</Text>
              {firmwares.has(id) && <Text style={[styles.deviceDetail, { color: textMuted }]}>Firmware: {firmwares.get(id)}</Text>}
              <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.sm, marginTop: Spacing.xs }}>
                <Text style={[styles.deviceDetail, { color: textMuted }]}>Type: {config.type || meta?.type || '?'}</Text>
                {meta?.rssi && <Text style={[styles.deviceDetail, { color: textMuted }]}>· RSSI: {meta.rssi}</Text>}
              </View>
              <View style={{ borderTopWidth: 1, borderTopColor: borderColor, marginTop: Spacing.sm, paddingTop: Spacing.xs }}>
                <Text style={[styles.deviceDetail, { color: textMuted }]}>First seen: {formatLogTime(earliest.get(id)!)}</Text>
                <Text style={[styles.deviceDetail, { color: textMuted }]}>Last seen: {formatLogTime(latest.get(id)!)}</Text>
              </View>
            </View>
          </View>
        );
      })}
    </ScrollView>
  );
});
