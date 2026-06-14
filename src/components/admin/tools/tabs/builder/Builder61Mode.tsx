import React from 'react';
import { View, Text, TouchableOpacity, TextInput } from 'react-native';
import { Spacing } from '../../../../../theme/theme';
import { useDiagnosticLabStyles } from '../DiagnosticLabStyles';
import { ProtocolBuilderContext } from '../../../../../hooks/useProtocolBuilder';

interface Builder61ModeProps {
  builderCtx: ProtocolBuilderContext;
}

export function Builder61Mode({ builderCtx }: Builder61ModeProps) {
  const { S, txtPri, txtMuted, cardBg, border, isDark } = useDiagnosticLabStyles();

  const {
    bldPatternId, setBldPatternId,
    bldSpeed, setBldSpeed,
    bldBright, setBldBright
  } = builderCtx;

  return (
    <View style={[S.diagBox, { backgroundColor: cardBg, borderColor: border }]}>
      <Text style={[S.subTitle, { color: txtPri, marginTop: 0 }]}>FIXED PATTERN (0x61)</Text>
      <View style={{ flexDirection: 'row', gap: Spacing.md }}>
        <View style={{ flex: 1 }}>
          <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>PATTERN ID (1–210)</Text>
          <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}>
            <TouchableOpacity onPress={() => setBldPatternId(String(Math.max(1, (parseInt(bldPatternId)||1) - 1)))} style={{ backgroundColor: border, borderRadius: 6, width: 36, height: 36, justifyContent: 'center', alignItems: 'center' }}>
              <Text style={{ color: txtPri, fontSize: 18, fontWeight: 'bold' }}>‒</Text>
            </TouchableOpacity>
            <TextInput style={[S.numInput, { flex: 1, backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri, textAlign: 'center' }]} value={bldPatternId} keyboardType="numeric" onChangeText={v => setBldPatternId(String(Math.max(1, Math.min(210, parseInt(v)||1))))} />
            <TouchableOpacity onPress={() => setBldPatternId(String(Math.min(210, (parseInt(bldPatternId)||1) + 1)))} style={{ backgroundColor: border, borderRadius: 6, width: 36, height: 36, justifyContent: 'center', alignItems: 'center' }}>
              <Text style={{ color: txtPri, fontSize: 18, fontWeight: 'bold' }}>+</Text>
            </TouchableOpacity>
          </View>
        </View>
        <View style={{ flex: 1 }}>
          <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>SPEED (1–100)</Text>
          <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldSpeed.toString()} keyboardType="numeric" onChangeText={v => setBldSpeed(Math.max(1, Math.min(100, parseInt(v)||1)))} />
        </View>
        <View style={{ flex: 1 }}>
          <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>BRIGHTNESS</Text>
          <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldBright} keyboardType="numeric" onChangeText={setBldBright} />
        </View>
      </View>
    </View>
  );
}
