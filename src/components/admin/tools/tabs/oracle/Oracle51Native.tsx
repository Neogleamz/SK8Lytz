import React, { useState } from 'react';
import { View, Text, TouchableOpacity, TextInput } from 'react-native';
import { Spacing } from '../../../../../theme/theme';
import { useDiagnosticLabStyles } from '../DiagnosticLabStyles';
import { QuickColorGrid } from '../DiagnosticLabQuickColorGrid';
import { ZenggeProtocol } from '../../../../../protocols/ZenggeProtocol';
import { SK8LYTZ_TEMPLATES } from '../../../../../protocols/PatternEngine';

interface Oracle51NativeProps {
  transmit: (cmd: number[], label: string, opcode?: string) => void;
}

export function Oracle51Native({ transmit }: Oracle51NativeProps) {
  const { S, txtPri, txtMuted, isDark, border } = useDiagnosticLabStyles();

  const [p41Speed, setP41Speed] = useState(50);
  const [p41Color1, setP41Color1] = useState<{r:number;g:number;b:number}>({r:255,g:0,b:0});
  const [p41Color2, setP41Color2] = useState<{r:number;g:number;b:number}>({r:0,g:0,b:255});
  const [p41SweepResults, setP41SweepResults] = useState<Record<string, 'WORKS'|'NO_EFFECT'|'CRASHED'>>({});
  const [oracle51Format, setOracle51Format] = useState<'compact' | 'extended' | 'wrapped_extended'>('wrapped_extended');

  return (
    <View style={[S.diagBox, { borderColor: '#9D4EFF', borderWidth: 1 }]}>
      <Text style={{ color: '#00CC88', fontSize: 10, fontWeight: '700', marginBottom: Spacing.md }}>
        ✅ PRODUCTION READY — 0x51 is the stable Scene Mode opcode.{'\n'}
        Testing authentic strings 1-44 via setCustomModeCompact.
      </Text>

      {/* Controls row */}
      <View style={{ flexDirection: 'row', gap: Spacing.sm, marginBottom: Spacing.md }}>
        <View style={{ flex: 1 }}>
          <Text style={{ color: txtMuted, fontSize: 10, fontWeight: '900', marginBottom: Spacing.xs }}>SPEED (1–100)</Text>
          <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]}
            value={String(p41Speed)} keyboardType="numeric"
            onChangeText={v => setP41Speed(Math.max(1, Math.min(100, parseInt(v) || 1)))} />
        </View>
      </View>

      {/* ── ORACLE 0x51 FORMAT TOGGLE ──────────────────────────────────── */}
      <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.sm, fontWeight: '900' }}>TEST FORMAT (0x51)</Text>
      <View style={{ flexDirection: 'row', gap: Spacing.sm, marginBottom: Spacing.lg }}>
        <TouchableOpacity
          style={[S.chip, oracle51Format === 'compact' && { backgroundColor: '#00E67633', borderColor: '#00E676' }, { flex: 1, paddingVertical: Spacing.md }]}
          onPress={() => setOracle51Format('compact')}
        >
          <Text style={{ color: oracle51Format === 'compact' ? '#00E676' : txtMuted, fontWeight: '900', fontSize: 11, textAlign: 'center' }}>COMPACT (9B)</Text>
          <Text style={{ color: oracle51Format === 'compact' ? '#00E676' : txtMuted, fontSize: 9, textAlign: 'center', opacity: 0.8 }}>9B Slots</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[S.chip, oracle51Format === 'extended' && { backgroundColor: '#FF950033', borderColor: '#FF9500' }, { flex: 1, paddingVertical: Spacing.md }]}
          onPress={() => setOracle51Format('extended')}
        >
          <Text style={{ color: oracle51Format === 'extended' ? '#FF9500' : txtMuted, fontWeight: '900', fontSize: 11, textAlign: 'center' }}>EXTENDED (323B)</Text>
          <Text style={{ color: oracle51Format === 'extended' ? '#FF9500' : txtMuted, fontSize: 9, textAlign: 'center', opacity: 0.8 }}>10B slots · chunked</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[S.chip, oracle51Format === 'wrapped_extended' && { backgroundColor: '#00F0FF33', borderColor: '#00F0FF' }, { flex: 1, paddingVertical: Spacing.md }]}
          onPress={() => setOracle51Format('wrapped_extended')}
        >
          <Text style={{ color: oracle51Format === 'wrapped_extended' ? '#00F0FF' : txtMuted, fontWeight: '900', fontSize: 11, textAlign: 'center' }}>WRAPPED EXT (331B)</Text>
          <Text style={{ color: oracle51Format === 'wrapped_extended' ? '#00F0FF' : txtMuted, fontSize: 9, textAlign: 'center', opacity: 0.8 }}>10B slots · un-chunked</Text>
        </TouchableOpacity>
      </View>

      <Text style={{ color: txtMuted, fontSize: 10, fontWeight: '900', marginBottom: Spacing.xs }}>FG COLOR</Text>
      <QuickColorGrid activeColor={p41Color1} onSelect={setP41Color1} />
      <Text style={{ color: txtMuted, fontSize: 10, fontWeight: '900', marginBottom: Spacing.xs }}>BG COLOR</Text>
      <QuickColorGrid activeColor={p41Color2} onSelect={setP41Color2} />

      {/* 44-ID grid — tap to send + verdict buttons inline */}
      <Text style={{ color: txtMuted, fontSize: 10, fontWeight: '900', marginTop: Spacing.md, marginBottom: Spacing.xs }}>TAP TO SEND EACH EFFECT</Text>
      {SK8LYTZ_TEMPLATES.filter((t: { id: number; name: string }) => t.id >= 201 && t.id <= 244).map((template: { id: number; name: string }) => {
        const id = template.id - 200; // 1-44
        const res = p41SweepResults[String(id)];
        const setRes = (r: 'WORKS' | 'NO_EFFECT' | 'CRASHED') =>
          setP41SweepResults(prev => ({ ...prev, [String(id)]: r }));
        const resColor = res === 'WORKS' ? '#00CC88' : res === 'NO_EFFECT' ? '#FF9500' : res === 'CRASHED' ? '#FF4040' : border;
        return (
          <View key={id} style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, marginBottom: Spacing.sm,
            borderBottomWidth: 1, borderBottomColor: border, paddingBottom: Spacing.sm }}>
            <TouchableOpacity
              onPress={() => {
                if (oracle51Format === 'compact') {
                  transmit(
                    ZenggeProtocol.setCustomModeCompact([{ mode: id, speed: p41Speed, color1: p41Color1, color2: p41Color2 }]),
                    `0x51 compact id=${id} spd=${p41Speed}`, '0x51'
                  );
                } else if (oracle51Format === 'extended') {
                  transmit(
                    ZenggeProtocol.setCustomModeExtended([{ mode: id, speed: p41Speed, color1: p41Color1, color2: p41Color2, dir: 0x81 }]),
                    `0x51 extended id=${id} spd=${p41Speed} dir=0x81`, '0x51'
                  );
                } else {
                  const rawPayload = ZenggeProtocol.setCustomModeExtended([{ mode: id, speed: p41Speed, color1: p41Color1, color2: p41Color2, dir: 0x81 }]);
                  const wrappedPayload = ZenggeProtocol.wrapCommand(rawPayload);
                  transmit(
                    wrappedPayload,
                    `0x51 wrapped extended id=${id} spd=${p41Speed} dir=0x81`, '0x51'
                  );
                }
              }}
              style={{ width: 48, height: 40, borderRadius: 8, backgroundColor: resColor + '22',
                borderWidth: 1.5, borderColor: resColor, justifyContent: 'center', alignItems: 'center' }}
            >
              <Text style={{ color: resColor, fontWeight: '900', fontSize: 12 }}>{id}</Text>
              <Text style={{ color: resColor, fontSize: 8 }}>SEND</Text>
            </TouchableOpacity>
            <View style={{ flex: 1, paddingLeft: 4 }}>
              <Text style={{ color: txtPri, fontSize: 11, fontWeight: '600' }}>
                {template.name}
              </Text>
            </View>
            {(['WORKS', 'NO_EFFECT', 'CRASHED'] as const).map(r => (
              <TouchableOpacity key={r} onPress={() => setRes(r)}
                style={{ width: 36, paddingVertical: Spacing.sm, borderRadius: 6, alignItems: 'center',
                  backgroundColor: res === r ? (r === 'WORKS' ? '#00CC8822' : r === 'CRASHED' ? '#FF404022' : '#FF950022') : 'transparent',
                  borderWidth: 1, borderColor: res === r ? (r === 'WORKS' ? '#00CC88' : r === 'CRASHED' ? '#FF4040' : '#FF9500') : border }}
              >
                <Text style={{ color: res === r ? (r === 'WORKS' ? '#00CC88' : r === 'CRASHED' ? '#FF4040' : '#FF9500') : txtMuted, fontSize: 9, fontWeight: '900' }}>
                  {r === 'WORKS' ? '✅' : r === 'NO_EFFECT' ? '⚠️' : '💀'}
                </Text>
              </TouchableOpacity>
            ))}
          </View>
        );
      })}
      <TouchableOpacity onPress={() => setP41SweepResults({})} style={{ alignSelf: 'flex-end', marginTop: Spacing.sm }}>
        <Text style={{ color: '#FF4040', fontSize: 10, fontWeight: '900' }}>RESET RESULTS</Text>
      </TouchableOpacity>
    </View>
  );
}
