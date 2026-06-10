import React, { useCallback } from 'react';
import { FlatList, Text, TouchableOpacity, View, Platform } from 'react-native';
import { Spacing } from '../../../../theme/theme';
import { useDiagnosticLabStyles } from './DiagnosticLabStyles';
import { BleLog } from '../../../../hooks/useDiagnosticLog';

interface SnifferTabProps {
  logs: BleLog[];
  clearLogs: () => void;
}

export function DiagnosticLabSnifferTab({
  logs,
  clearLogs,
}: SnifferTabProps) {
  const { S, txtPri, txtMuted, border, isDark, cyan } = useDiagnosticLabStyles();

  const keyExtractorLog = useCallback((_: BleLog, i: number) => i.toString(), []);

  const renderItem = useCallback(({ item }: { item: BleLog }) => (
    <View style={{ marginBottom: Spacing.sm, paddingBottom: Spacing.sm, borderBottomWidth: 1, borderBottomColor: border }}>
      <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, marginBottom: Spacing.xs }}>
        <View style={{ backgroundColor: item.dir === 'TX' ? '#FF404022' : cyan + '22', borderWidth: 1, borderColor: item.dir === 'TX' ? '#FF4040' : cyan, paddingHorizontal: Spacing.sm, paddingVertical: 1, borderRadius: 4 }}>
          <Text style={{ color: item.dir === 'TX' ? '#FF4040' : cyan, fontSize: 9, fontWeight: '900' }}>{item.dir}</Text>
        </View>
        <Text style={{ color: txtMuted, fontSize: 9 }}>{new Date(item.t).toLocaleTimeString()}</Text>
        {item.dev && <Text style={{ color: txtMuted, fontSize: 9 }}>{item.dev.slice(-6)}</Text>}
      </View>
      {item.note && <Text style={{ color: '#FF9500', fontSize: 10, marginBottom: Spacing.xxs, fontWeight: 'bold' }}>{item.note}</Text>}
      <Text style={{ color: txtPri, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', fontSize: 10 }} numberOfLines={2}>
        {item.hex}
      </Text>
    </View>
  ), [border, cyan, txtMuted, txtPri]);

  return (
    <View style={{ flex: 1, paddingBottom: Spacing.xxxl }}>
      <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: Spacing.md }}>
        <Text style={[S.sectionTitle, { color: txtPri }]}>BLE TRACE</Text>
        <TouchableOpacity style={[S.chip, { borderColor: '#ff404022', backgroundColor: '#ff404011' }]} onPress={() => clearLogs()}>
          <Text style={{ color: '#FF4040', fontSize: 10, fontWeight: '900' }}>CLEAR</Text>
        </TouchableOpacity>
      </View>
      <FlatList removeClippedSubviews={true} initialNumToRender={12} windowSize={5}
        data={logs}
        keyExtractor={keyExtractorLog}
        style={{ flex: 1, backgroundColor: isDark ? '#05070a' : '#f9fafb', borderRadius: 12, padding: Spacing.sm, borderWidth: 1, borderColor: border }}
        renderItem={renderItem}
        ListEmptyComponent={<Text style={{ color: txtMuted, padding: Spacing.lg, textAlign: 'center', fontSize: 12 }}>No BLE traffic yet. Connect a device and use the other tabs.</Text>}
      />
    </View>
  );
}
