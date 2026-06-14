import React, { useState } from 'react';
import { View, Text, TouchableOpacity, TextInput, ScrollView, Platform } from 'react-native';
import { Spacing } from '../../../../../theme/theme';
import { useDiagnosticLabStyles } from '../DiagnosticLabStyles';
import CustomEffectVisualizer from '../../../../CustomEffectVisualizer';
import { QuickColorGrid } from '../DiagnosticLabQuickColorGrid';
import { ZenggeProtocol } from '../../../../../protocols/ZenggeProtocol';
import { ProtocolBuilderContext } from '../../../../../hooks/useProtocolBuilder';

interface Builder51ModeProps {
  builderCtx: ProtocolBuilderContext;
  transmit: (cmd: number[], label: string, opcode?: string) => void;
}

export function Builder51Mode({ builderCtx, transmit }: Builder51ModeProps) {
  const { S, txtPri, txtMuted, cardBg, border, isDark, cyan } = useDiagnosticLabStyles();
  const [bld51Format, setBld51Format] = useState<'compact' | 'extended' | 'wrapped_extended'>('compact');

  const {
    bld51Mode, setBld51Mode,
    bld51Speed, setBld51Speed,
    bld51Color1, setBld51Color1,
    bld51Color2, setBld51Color2,
    bld51Dir, setBld51Dir,
    bld51Seg, setBld51Seg
  } = builderCtx;

  return (
    <View style={[S.diagBox, { backgroundColor: cardBg, borderColor: border }]}>
      <Text style={[S.subTitle, { color: txtPri, marginTop: 0 }]}>DIY MODE MATH SIMULATOR</Text>
      <View style={{ alignItems: 'center', marginBottom: Spacing.lg }}>
        <CustomEffectVisualizer 
          effectId={parseInt(bld51Mode) || 1} 
          fgColorHex={`#${bld51Color1.r.toString(16).padStart(2,'0')}${bld51Color1.g.toString(16).padStart(2,'0')}${bld51Color1.b.toString(16).padStart(2,'0')}`}
          bgColorHex={`#${bld51Color2.r.toString(16).padStart(2,'0')}${bld51Color2.g.toString(16).padStart(2,'0')}${bld51Color2.b.toString(16).padStart(2,'0')}`}
          speed={Math.max(1, Math.min(100, parseInt(bld51Speed) || 50))}
          direction={bld51Dir === 1}
          segments={bld51Seg ? 2 : 1}
        />
      </View>

      <View style={{ flexDirection: 'row', gap: Spacing.md, marginBottom: Spacing.md }}>
        <View style={{ flex: 1 }}>
          <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>EFFECT ID (1-44)</Text>
          <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}>
            <TouchableOpacity onPress={() => setBld51Mode(String(Math.max(1, (parseInt(bld51Mode)||1) - 1)))} style={{ backgroundColor: border, borderRadius: 6, width: 36, height: 36, justifyContent: 'center', alignItems: 'center' }}>
              <Text style={{ color: txtPri, fontSize: 16 }}>-</Text>
            </TouchableOpacity>
            <TextInput style={[S.numInput, { flex: 1, backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri, textAlign: 'center' }]} value={bld51Mode} keyboardType="numeric" onChangeText={v => setBld51Mode(String(Math.max(1, Math.min(44, parseInt(v)||1))))} />
            <TouchableOpacity onPress={() => setBld51Mode(String(Math.min(44, (parseInt(bld51Mode)||1) + 1)))} style={{ backgroundColor: border, borderRadius: 6, width: 36, height: 36, justifyContent: 'center', alignItems: 'center' }}>
              <Text style={{ color: txtPri, fontSize: 18, fontWeight: 'bold' }}>+</Text>
            </TouchableOpacity>
          </View>
        </View>
        <View style={{ flex: 1 }}>
          <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>SPEED (1–100)</Text>
          <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bld51Speed.toString()} keyboardType="numeric" onChangeText={v => setBld51Speed(Math.max(1, Math.min(100, parseInt(v)||1)).toString())} />
        </View>
      </View>

      <View style={{ flexDirection: 'row', gap: Spacing.md, marginBottom: Spacing.lg }}>
        <View style={{ flex: 1 }}>
          <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>DIR (Bit 7)</Text>
          <View style={{ flexDirection: 'row', gap: Spacing.xs }}>
            {[0, 1].map(d => (
              <TouchableOpacity key={d} style={[S.chip, bld51Dir === d && { backgroundColor: cyan + '22', borderColor: cyan }, {flex: 1, height: 40}]} onPress={() => setBld51Dir(d)}>
                <Text style={{ color: bld51Dir === d ? cyan : txtMuted, fontSize: 11, textAlign: 'center', fontWeight: '900' }}>{d === 0 ? 'LEFT' : 'RIGHT'}</Text>
              </TouchableOpacity>
            ))}
          </View>
        </View>
        <View style={{ flex: 1 }}>
          <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>SEGS (Bit 6)</Text>
          <TouchableOpacity style={[S.chip, bld51Seg && { backgroundColor: cyan + '22', borderColor: cyan }, {paddingVertical: Spacing.md, height: 40}]} onPress={() => setBld51Seg(!bld51Seg)}>
            <Text style={{ color: bld51Seg ? cyan : txtMuted, textAlign: 'center', fontSize: 11, fontWeight: '900' }}>{bld51Seg ? 'SPLIT (1)' : 'FULL (0)'}</Text>
          </TouchableOpacity>
        </View>
      </View>

      <Text style={[S.subTitle, { color: txtMuted, marginTop: Spacing.md }]}>FOREGROUND (COLOR 1)</Text>
      <QuickColorGrid activeColor={bld51Color1} onSelect={setBld51Color1} />

      <Text style={[S.subTitle, { color: txtMuted, marginTop: Spacing.md }]}>BACKGROUND (COLOR 2)</Text>
      <QuickColorGrid activeColor={bld51Color2} onSelect={setBld51Color2} />

      {/* ── FORMAT TOGGLE ──────────────────────────────────────────────── */}
      <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.sm, marginTop: Spacing.xl, fontWeight: '900' }}>PACKET FORMAT — SELECT BEFORE TX</Text>
      <View style={{ flexDirection: 'row', gap: Spacing.sm, marginBottom: Spacing.lg }}>
        <TouchableOpacity
          style={[S.chip, bld51Format === 'compact' && { backgroundColor: '#00E67633', borderColor: '#00E676' }, { flex: 1, paddingVertical: Spacing.md }]}
          onPress={() => setBld51Format('compact')}
        >
          <Text style={{ color: bld51Format === 'compact' ? '#00E676' : txtMuted, fontWeight: '900', fontSize: 11, textAlign: 'center' }}>COMPACT (9B)</Text>
          <Text style={{ color: bld51Format === 'compact' ? '#00E676' : txtMuted, fontSize: 9, textAlign: 'center', opacity: 0.8 }}>9B Slots · Wrapped</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[S.chip, bld51Format === 'extended' && { backgroundColor: '#FF950033', borderColor: '#FF9500' }, { flex: 1, paddingVertical: Spacing.md }]}
          onPress={() => setBld51Format('extended')}
        >
          <Text style={{ color: bld51Format === 'extended' ? '#FF9500' : txtMuted, fontWeight: '900', fontSize: 11, textAlign: 'center' }}>EXTENDED (323B)</Text>
          <Text style={{ color: bld51Format === 'extended' ? '#FF9500' : txtMuted, fontSize: 9, textAlign: 'center', opacity: 0.8 }}>10B Slots · Chunked</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[S.chip, bld51Format === 'wrapped_extended' && { backgroundColor: '#00F0FF33', borderColor: '#00F0FF' }, { flex: 1, paddingVertical: Spacing.md }]}
          onPress={() => setBld51Format('wrapped_extended')}
        >
          <Text style={{ color: bld51Format === 'wrapped_extended' ? '#00F0FF' : txtMuted, fontWeight: '900', fontSize: 11, textAlign: 'center' }}>WRAPPED EXT (331B)</Text>
          <Text style={{ color: bld51Format === 'wrapped_extended' ? '#00F0FF' : txtMuted, fontSize: 9, textAlign: 'center', opacity: 0.8 }}>10B Slots · Wrapped</Text>
        </TouchableOpacity>
      </View>

      {/* ── LIVE PAYLOAD BYTE PREVIEW ──────────────────────────────────── */}
      <View style={{ backgroundColor: isDark ? '#05070a' : '#f9fafb', borderRadius: 8, padding: Spacing.md, marginBottom: Spacing.lg, borderColor: bld51Format === 'wrapped_extended' ? '#00F0FF' : bld51Format === 'extended' ? '#FF9500' : '#00E676', borderWidth: 1 }}>
        {bld51Format === 'compact' ? (
          <Text style={{ color: cyan, fontSize: 10, fontFamily: Platform.select({ ios: 'Menlo', default: 'monospace' }) }}>
            {`[0x51, 0xF0, 0x${(parseInt(bld51Mode)||1).toString(16).padStart(2,'0').toUpperCase()}, 0x${(Math.max(1,Math.min(100,parseInt(bld51Speed)||50))).toString(16).padStart(2,'0').toUpperCase()},`}
            {`\n R1=${bld51Color1.r} G1=${bld51Color1.g} B1=${bld51Color1.b},`}
            {`\n R2=${bld51Color2.r} G2=${bld51Color2.g} B2=${bld51Color2.b},`}
            {`\n 0x0F, CS]  ← 12 bytes raw, wrapped to 20 bytes`}
          </Text>
        ) : bld51Format === 'extended' ? (
          <Text style={{ color: '#FF9500', fontSize: 10, fontFamily: Platform.select({ ios: 'Menlo', default: 'monospace' }) }}>
            {`[0x51, 0xF0, 0x${(parseInt(bld51Mode)||1).toString(16).padStart(2,'0').toUpperCase()}, 0x${(Math.max(1,Math.min(100,parseInt(bld51Speed)||50))).toString(16).padStart(2,'0').toUpperCase()},`}
            {`\n R1=${bld51Color1.r} G1=${bld51Color1.g} B1=${bld51Color1.b},`}
            {`\n R2=${bld51Color2.r} G2=${bld51Color2.g} B2=${bld51Color2.b},`}
            {`\n flags=0x${bld51Dir === 1 ? '80' : '00'} (${bld51Dir === 1 ? 'FWD+SEG' : 'REV'})]`}
            {`\n + 31 empty slots + [0x0F, CS]  ← 323 bytes raw via 0x40 chunks`}
          </Text>
        ) : (
          <Text style={{ color: '#00F0FF', fontSize: 10, fontFamily: Platform.select({ ios: 'Menlo', default: 'monospace' }) }}>
            {`[0x00, Seq, 0x80, 0x00, 0x01, 0x43, 0x44, 0x0B, 0x51, 0xF0, ...]`}
            {`\n R1=${bld51Color1.r} G1=${bld51Color1.g} B1=${bld51Color1.b},`}
            {`\n R2=${bld51Color2.r} G2=${bld51Color2.g} B2=${bld51Color2.b},`}
            {`\n flags=0x${bld51Dir === 1 ? '80' : '00'} (${bld51Dir === 1 ? 'FWD+SEG' : 'REV'})]`}
            {`\n + 31 empty slots + [0x0F, CS]  ← Wrapped 331 bytes total (bypasses chunking!)`}
          </Text>
        )}
      </View>

      {/* ── TX BUTTONS ─────────────────────────────────────────────────── */}
      <View style={{ flexDirection: 'row', gap: Spacing.sm }}>
        <TouchableOpacity
          style={[S.txBtn, { flex: 1, backgroundColor: '#00E676', borderColor: '#00E676' }]}
          onPress={() => {
            const mode = Math.max(1, Math.min(44, parseInt(bld51Mode) || 1));
            const speed = Math.max(1, Math.min(100, parseInt(bld51Speed) || 50));
            transmit(
              ZenggeProtocol.setCustomModeCompact([{ mode, speed, color1: bld51Color1, color2: bld51Color2 }]),
              `0x51 compact mode=${mode} spd=${speed}`,
              '0x51'
            );
          }}
        >
          <Text style={{ color: '#000', fontWeight: '900', fontSize: 11 }}>TX COMPACT (9B)</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[S.txBtn, { flex: 1, backgroundColor: '#FF9500', borderColor: '#FF9500' }]}
          onPress={() => {
            const mode = Math.max(1, Math.min(44, parseInt(bld51Mode) || 1));
            const speed = Math.max(1, Math.min(100, parseInt(bld51Speed) || 50));
            const flags = (bld51Seg ? 0x80 : 0x00) | (bld51Dir === 1 ? 0x01 : 0x00);
            transmit(
              ZenggeProtocol.setCustomModeExtended([{ mode, speed, color1: bld51Color1, color2: bld51Color2, dir: flags }]),
              `0x51 extended mode=${mode} spd=${speed} dir=0x${flags.toString(16).toUpperCase()}`,
              '0x51'
            );
          }}
        >
          <Text style={{ color: '#000', fontWeight: '900', fontSize: 11 }}>TX EXTENDED (323B)</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[S.txBtn, { flex: 1, backgroundColor: '#00F0FF', borderColor: '#00F0FF' }]}
          onPress={() => {
            const mode = Math.max(1, Math.min(44, parseInt(bld51Mode) || 1));
            const speed = Math.max(1, Math.min(100, parseInt(bld51Speed) || 50));
            const flags = (bld51Seg ? 0x80 : 0x00) | (bld51Dir === 1 ? 0x01 : 0x00);
            const rawPayload = ZenggeProtocol.setCustomModeExtended([{ mode, speed, color1: bld51Color1, color2: bld51Color2, dir: flags }]);
            const wrappedPayload = ZenggeProtocol.wrapCommand(rawPayload);
            transmit(
              wrappedPayload,
              `0x51 wrapped extended mode=${mode} spd=${speed} dir=0x${flags.toString(16).toUpperCase()}`,
              '0x51'
            );
          }}
        >
          <Text style={{ color: '#000', fontWeight: '900', fontSize: 11 }}>TX WRAPPED (331B)</Text>
        </TouchableOpacity>
      </View>

      {/* ── MODE ID EXPLORER ─────────────────────────────────────────────────── */}
      <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.sm, marginTop: Spacing.xl, fontWeight: '900' }}>MODE ID EXPLORER (1-44)</Text>
      <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.md }}>Tap to fast-fire the active configuration with a specific Mode ID using the selected packet format.</Text>
      <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={{ gap: Spacing.xs, paddingBottom: Spacing.sm }}>
        {Array.from({length: 44}, (_, i) => i + 1).map(id => (
          <TouchableOpacity
            key={id}
            style={[S.chip, bld51Mode === String(id) && { backgroundColor: cyan + '22', borderColor: cyan }, { width: 44, height: 44, paddingHorizontal: 0, justifyContent: 'center' }]}
            onPress={() => {
              setBld51Mode(String(id));
              const speed = Math.max(1, Math.min(100, parseInt(bld51Speed) || 50));
              if (bld51Format === 'compact') {
                transmit(
                  ZenggeProtocol.setCustomModeCompact([{ mode: id, speed, color1: bld51Color1, color2: bld51Color2 }]),
                  `0x51 compact mode=${id} spd=${speed}`,
                  '0x51'
                );
              } else if (bld51Format === 'extended') {
                const flags = (bld51Seg ? 0x80 : 0x00) | (bld51Dir === 1 ? 0x01 : 0x00);
                transmit(
                  ZenggeProtocol.setCustomModeExtended([{ mode: id, speed, color1: bld51Color1, color2: bld51Color2, dir: flags }]),
                  `0x51 extended mode=${id} spd=${speed} dir=0x${flags.toString(16).toUpperCase()}`,
                  '0x51'
                );
              } else {
                const flags = (bld51Seg ? 0x80 : 0x00) | (bld51Dir === 1 ? 0x01 : 0x00);
                const rawPayload = ZenggeProtocol.setCustomModeExtended([{ mode: id, speed: bld51Color1 ? speed : speed, color1: bld51Color1, color2: bld51Color2, dir: flags }]);
                const wrappedPayload = ZenggeProtocol.wrapCommand(rawPayload);
                transmit(
                  wrappedPayload,
                  `0x51 wrapped extended mode=${id} spd=${speed} dir=0x${flags.toString(16).toUpperCase()}`,
                  '0x51'
                );
              }
            }}
          >
            <Text style={{ color: bld51Mode === String(id) ? cyan : txtMuted, fontWeight: '900', fontSize: 14, textAlign: 'center' }}>{id}</Text>
          </TouchableOpacity>
        ))}
      </ScrollView>
    </View>
  );
}
