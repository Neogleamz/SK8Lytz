import React from 'react';
import { View, Text, TouchableOpacity, TextInput } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Spacing } from '../../../../../theme/theme';
import { useDiagnosticLabStyles } from '../DiagnosticLabStyles';
import { TRANSITION_TYPES } from '../DiagnosticLabConstants';
import { ProtocolBuilderContext } from '../../../../../hooks/useProtocolBuilder';

interface Builder59ModeProps {
  builderCtx: ProtocolBuilderContext;
  hwPts: number;
}

export function Builder59Mode({ builderCtx, hwPts }: Builder59ModeProps) {
  const { S, txtPri, txtMuted, cardBg, border, isDark, cyan } = useDiagnosticLabStyles();

  const {
    bldColors, setBldColors,
    bldTrans, setBldTrans,
    bldPoints, setBldPoints,
    bldSpeed, setBldSpeed,
    bldDir, setBldDir
  } = builderCtx;

  return (
    <View style={[S.diagBox, { backgroundColor: cardBg, borderColor: border }]}>
      <Text style={[S.subTitle, { color: txtPri, marginTop: 0 }]}>PIXEL COLORS (0x59)</Text>
      <Text style={[S.hint, { color: txtMuted }]}>Colors will be repeated to fill the LED count.</Text>
      
      {bldColors.map((c: {r: number, g: number, b: number}, i: number) => (
          <View key={i} style={{ flexDirection: 'row', alignItems: 'center', marginBottom: Spacing.sm, gap: Spacing.sm }}>
            <View style={{ width: 24, height: 24, backgroundColor: `rgb(${c.r},${c.g},${c.b})`, borderRadius: 6, borderWidth: 1, borderColor: border }} />
            <TextInput style={[{flex:1, backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri}, S.numInput]} value={c.r.toString()} keyboardType="numeric" onChangeText={v => { const cur = [...bldColors]; cur[i].r = parseInt(v)||0; setBldColors(cur); }} placeholder="R" placeholderTextColor={txtMuted} />
            <TextInput style={[{flex:1, backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri}, S.numInput]} value={c.g.toString()} keyboardType="numeric" onChangeText={v => { const cur = [...bldColors]; cur[i].g = parseInt(v)||0; setBldColors(cur); }} placeholder="G" placeholderTextColor={txtMuted} />
            <TextInput style={[{flex:1, backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri}, S.numInput]} value={c.b.toString()} keyboardType="numeric" onChangeText={v => { const cur = [...bldColors]; cur[i].b = parseInt(v)||0; setBldColors(cur); }} placeholder="B" placeholderTextColor={txtMuted} />
            <TouchableOpacity onPress={() => setBldColors(bldColors.filter((_: {r:number, g:number, b:number}, idx: number)=>idx!==i))} style={{ padding: Spacing.sm }}>
              <MaterialCommunityIcons name="delete" color="#ff4040" size={18} />
            </TouchableOpacity>
          </View>
      ))}
      <TouchableOpacity onPress={() => setBldColors([...bldColors, {r:0,g:0,b:0}])} style={{ alignSelf: 'flex-start', paddingHorizontal: Spacing.lg, paddingVertical: Spacing.md, backgroundColor: border, borderRadius: 8, marginTop: Spacing.xs }}>
          <Text style={{ color: txtPri, fontSize: 11, fontWeight: '900' }}>+ ADD COLOR</Text>
      </TouchableOpacity>

      <Text style={[S.subTitle, { color: txtMuted, marginTop: Spacing.xl }]}>TRANSITION TYPE</Text>
      <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.sm, marginBottom: Spacing.lg }}>
        {TRANSITION_TYPES.map(tt => (
          <TouchableOpacity key={tt.byte}
            style={[S.chip, bldTrans === tt.byte && { backgroundColor: tt.color + '22', borderColor: tt.color }, { height: 40 }]}
            onPress={() => setBldTrans(tt.byte)}>
            <Text style={{ color: bldTrans === tt.byte ? tt.color : txtMuted, fontWeight: '900', fontSize: 11 }}>
              0x0{tt.byte.toString(16).toUpperCase()} {tt.label}
            </Text>
          </TouchableOpacity>
        ))}
      </View>

      <View style={{ flexDirection: 'row', gap: Spacing.md, marginBottom: Spacing.sm }}>
        <View style={{ flex: 1 }}>
          <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>NUM POINTS</Text>
          <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldPoints} keyboardType="numeric"
            onChangeText={v => setBldPoints(v)} placeholder={hwPts.toString()} placeholderTextColor={txtMuted} />
          <Text style={{ color: cyan, fontSize: 10, marginTop: Spacing.xs, fontWeight: 'bold' }}>HW probe: {hwPts}</Text>
        </View>
        <View style={{ flex: 1 }}>
          <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>SPEED</Text>
          <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldSpeed.toString()} keyboardType="numeric"
            onChangeText={v => setBldSpeed(Math.max(1, Math.min(255, parseInt(v)||1)))} />
        </View>
        <View style={{ flex: 1 }}>
          <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>DIR</Text>
          <View style={{ flexDirection: 'row', gap: Spacing.xs }}>
            {[0, 1].map(d => (
              <TouchableOpacity key={d} style={[S.chip, bldDir === d && { backgroundColor: cyan + '22', borderColor: cyan }, {flex: 1, height: 40}]} onPress={() => setBldDir(d)}>
                <Text style={{ color: bldDir === d ? cyan : txtMuted, fontSize: 11, textAlign: 'center', fontWeight: '900' }}>{d === 0 ? 'REV' : 'FWD'}</Text>
              </TouchableOpacity>
            ))}
          </View>
        </View>
      </View>
    </View>
  );
}
