import React from 'react';
import { View, Text, TextInput } from 'react-native';
import { Spacing } from '../../../../../theme/theme';
import { useDiagnosticLabStyles } from '../DiagnosticLabStyles';
import { ProtocolBuilderContext } from '../../../../../hooks/useProtocolBuilder';

interface Builder62ModeProps {
  builderCtx: ProtocolBuilderContext;
}

export function Builder62Mode({ builderCtx }: Builder62ModeProps) {
  const { S, txtPri, txtMuted, cardBg, border, isDark } = useDiagnosticLabStyles();

  const {
    bldIc, setBldIc,
    bldOrder, setBldOrder,
    bldPoints, setBldPoints,
    bldSegs, setBldSegs
  } = builderCtx;

  return (
    <View style={[S.diagBox, { backgroundColor: cardBg, borderColor: border }]}>
      <Text style={[S.subTitle, { color: txtPri, marginTop: 0 }]}>HARDWARE SETUP (0x62)</Text>
      <View style={{ flexDirection: 'row', gap: Spacing.md, marginBottom: Spacing.md }}>
        <View style={{ flex: 1 }}>
            <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>IC CHIP</Text>
            <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldIc} onChangeText={setBldIc} placeholder="e.g. WS2812B" />
        </View>
        <View style={{ flex: 1 }}>
            <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>COLOR ORDER</Text>
            <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldOrder} onChangeText={setBldOrder} placeholder="e.g. RGB" />
        </View>
      </View>
      <View style={{ flexDirection: 'row', gap: Spacing.md }}>
          <View style={{ flex: 1 }}>
            <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>TOTAL LEDS</Text>
            <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldPoints} keyboardType="numeric" onChangeText={setBldPoints} />
        </View>
        <View style={{ flex: 1 }}>
            <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>SEGMENTS</Text>
            <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldSegs} keyboardType="numeric" onChangeText={setBldSegs} />
        </View>
      </View>
    </View>
  );
}
