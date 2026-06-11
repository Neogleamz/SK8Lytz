import React, { useCallback, useMemo } from 'react';
import { FlatList, StyleSheet, Text, TouchableOpacity, View, Platform } from 'react-native';
import { Spacing } from '../../../../theme/theme';
import { useDiagnosticLabStyles } from './DiagnosticLabStyles';
import { BleLog } from '../../../../hooks/useDiagnosticLog';

interface SnifferTabProps {
  logs: BleLog[];
  clearLogs: () => void;
}

const BleLogCard = React.memo(({
  item,
  border,
  cyan,
  txtMuted,
  txtPri,
}: {
  item: BleLog;
  border: string;
  cyan: string;
  txtMuted: string;
  txtPri: string;
}) => {
  const isTX = item.dir === 'TX';
  const badgeStyle = useMemo(() => [
    styles.dirBadge,
    {
      backgroundColor: isTX ? '#FF404022' : cyan + '22',
      borderColor: isTX ? '#FF4040' : cyan,
    }
  ], [isTX, cyan]);

  const dirTextStyle = useMemo(() => [
    styles.dirText,
    { color: isTX ? '#FF4040' : cyan }
  ], [isTX, cyan]);

  return (
    <View style={[styles.card, { borderBottomColor: border }]}>
      <View style={styles.row}>
        <View style={badgeStyle}>
          <Text style={dirTextStyle}>{item.dir}</Text>
        </View>
        <Text style={[styles.timeText, { color: txtMuted }]}>{new Date(item.t).toLocaleTimeString()}</Text>
        {item.dev && <Text style={[styles.timeText, { color: txtMuted }]}>{item.dev.slice(-6)}</Text>}
      </View>
      {item.note && <Text style={styles.noteText}>{item.note}</Text>}
      <Text style={[styles.hexText, { color: txtPri, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }]} numberOfLines={2}>
        {item.hex}
      </Text>
    </View>
  );
});

export function DiagnosticLabSnifferTab({
  logs,
  clearLogs,
}: SnifferTabProps) {
  const { S, txtPri, txtMuted, border, isDark, cyan } = useDiagnosticLabStyles();

  const keyExtractorLog = useCallback((_: BleLog, i: number) => i.toString(), []);

  const renderItem = useCallback(({ item }: { item: BleLog }) => (
    <BleLogCard
      item={item}
      border={border}
      cyan={cyan}
      txtMuted={txtMuted}
      txtPri={txtPri}
    />
  ), [border, cyan, txtMuted, txtPri]);

  const listStyle = useMemo(() => [
    styles.list,
    {
      backgroundColor: isDark ? '#05070a' : '#f9fafb',
      borderColor: border,
    }
  ], [isDark, border]);

  const emptyComponent = useCallback(() => (
    <Text style={[styles.emptyText, { color: txtMuted }]}>
      No BLE traffic yet. Connect a device and use the other tabs.
    </Text>
  ), [txtMuted]);

  return (
    <View style={styles.container}>
      <View style={styles.headerRow}>
        <Text style={[S.sectionTitle, { color: txtPri }]}>BLE TRACE</Text>
        <TouchableOpacity style={[S.chip, styles.clearBtn]} onPress={clearLogs}>
          <Text style={styles.clearText}>CLEAR</Text>
        </TouchableOpacity>
      </View>
      <FlatList removeClippedSubviews={true} initialNumToRender={12} windowSize={5}
        data={logs}
        keyExtractor={keyExtractorLog}
        style={listStyle}
        renderItem={renderItem}
        ListEmptyComponent={emptyComponent}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingBottom: Spacing.xxxl,
  },
  headerRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: Spacing.md,
  },
  clearBtn: {
    borderColor: '#ff404022',
    backgroundColor: '#ff404011',
  },
  clearText: {
    color: '#FF4040',
    fontSize: 10,
    fontWeight: '900',
  },
  card: {
    marginBottom: Spacing.sm,
    paddingBottom: Spacing.sm,
    borderBottomWidth: 1,
  },
  row: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: Spacing.sm,
    marginBottom: Spacing.xs,
  },
  dirBadge: {
    borderWidth: 1,
    paddingHorizontal: Spacing.sm,
    paddingVertical: 1,
    borderRadius: 4,
  },
  dirText: {
    fontSize: 9,
    fontWeight: '900',
  },
  timeText: {
    fontSize: 9,
  },
  noteText: {
    color: '#FF9500',
    fontSize: 10,
    marginBottom: Spacing.xxs,
    fontWeight: 'bold',
  },
  hexText: {
    fontSize: 10,
  },
  list: {
    flex: 1,
    borderRadius: 12,
    padding: Spacing.sm,
    borderWidth: 1,
  },
  emptyText: {
    padding: Spacing.lg,
    textAlign: 'center',
    fontSize: 12,
  },
});
