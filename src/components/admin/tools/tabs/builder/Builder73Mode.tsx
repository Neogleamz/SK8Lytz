import React, { useState } from 'react';
import { View, Text, TouchableOpacity, TextInput, Platform } from 'react-native';
import { Spacing } from '../../../../../theme/theme';
import { useDiagnosticLabStyles } from '../DiagnosticLabStyles';
import { QuickColorGrid } from '../DiagnosticLabQuickColorGrid';
import { ZenggeProtocol } from '../../../../../protocols/ZenggeProtocol';
import { ProtocolBuilderContext } from '../../../../../hooks/useProtocolBuilder';

interface Builder73ModeProps {
  builderCtx: ProtocolBuilderContext;
  transmit: (cmd: number[], label: string, opcode?: string) => void;
}

export function Builder73Mode({ builderCtx, transmit }: Builder73ModeProps) {
  const { S, txtPri, txtMuted, cardBg, border, isDark, cyan } = useDiagnosticLabStyles();
  const [bldMusicIsOn, setBldMusicIsOn] = useState(true);

  const {
    bldMusicMode, setBldMusicMode,
    bldMic, setBldMic,
    bldSens, setBldSens,
    bldBright, setBldBright,
    bldColors, setBldColors,
    bldC2, setBldC2
  } = builderCtx;

  return (
    <View style={[S.diagBox, { backgroundColor: cardBg, borderColor: '#FF4040', borderWidth: 1 }]}>
      <Text style={[S.subTitle, { color: '#FF9500', marginTop: 0 }]}>SYMPHONY MODE (0x73) — 13B FORMAT</Text>
      <Text style={{ color: '#FF4040', fontSize: 10, marginBottom: Spacing.md, fontWeight: '700' }}>
        ⚠️ VERIFIED: 13B format · mic=0x26 (APP) / 0x27 (DEVICE) · isOn byte at position [3]
      </Text>

      <View style={{ flexDirection: 'row', gap: Spacing.md, marginBottom: Spacing.md }}>
        <View style={{ flex: 1 }}>
          <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>MUSIC MODE (1–13)</Text>
          <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}>
            <TouchableOpacity onPress={() => setBldMusicMode(String(Math.max(1, (parseInt(bldMusicMode)||1) - 1)))} style={{ backgroundColor: border, borderRadius: 6, width: 36, height: 36, justifyContent: 'center', alignItems: 'center' }}>
              <Text style={{ color: txtPri, fontSize: 18, fontWeight: 'bold' }}>‒</Text>
            </TouchableOpacity>
            <TextInput style={[S.numInput, { flex: 1, backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri, textAlign: 'center' }]} value={bldMusicMode} keyboardType="numeric" onChangeText={v => setBldMusicMode(String(Math.max(1, Math.min(13, parseInt(v)||1))))} />
            <TouchableOpacity onPress={() => setBldMusicMode(String(Math.min(13, (parseInt(bldMusicMode)||1) + 1)))} style={{ backgroundColor: border, borderRadius: 6, width: 36, height: 36, justifyContent: 'center', alignItems: 'center' }}>
              <Text style={{ color: txtPri, fontSize: 18, fontWeight: 'bold' }}>+</Text>
            </TouchableOpacity>
          </View>
        </View>
        <View style={{ flex: 1 }}>
          <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>isOn [BYTE 3]</Text>
          <TouchableOpacity
            style={[S.chip, bldMusicIsOn && { backgroundColor: '#00E67633', borderColor: '#00E676' }, { paddingVertical: Spacing.md, height: 40 }]}
            onPress={() => setBldMusicIsOn(!bldMusicIsOn)}
          >
            <Text style={{ color: bldMusicIsOn ? '#00E676' : '#FF4040', textAlign: 'center', fontSize: 11, fontWeight: '900' }}>
              {bldMusicIsOn ? '0x01 ON' : '0x00 OFF'}
            </Text>
          </TouchableOpacity>
        </View>
      </View>

      {/* MIC SOURCE: 0x26 (APP) vs 0x27 (DEVICE) */}
      <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.sm, fontWeight: '900' }}>MIC SOURCE [BYTE 2] — PROTOCOL TRUTH</Text>
      <View style={{ flexDirection: 'row', gap: Spacing.sm, marginBottom: Spacing.lg }}>
        <TouchableOpacity
          onPress={() => setBldMic(false)}
          style={{ flex: 1, paddingVertical: Spacing.md, borderRadius: 8, alignItems: 'center',
            backgroundColor: !bldMic ? '#00f0ff22' : border,
            borderWidth: 1.5, borderColor: !bldMic ? cyan : border }}
        >
          <Text style={{ color: !bldMic ? cyan : txtMuted, fontWeight: '900', fontSize: 11 }}>APP MIC</Text>
          <Text style={{ color: !bldMic ? cyan : txtMuted, fontSize: 9, opacity: 0.8 }}>0x26</Text>
        </TouchableOpacity>
        <TouchableOpacity
          onPress={() => setBldMic(true)}
          style={{ flex: 1, paddingVertical: Spacing.md, borderRadius: 8, alignItems: 'center',
            backgroundColor: bldMic ? '#FF950033' : border,
            borderWidth: 1.5, borderColor: bldMic ? '#FF9500' : border }}
        >
          <Text style={{ color: bldMic ? '#FF9500' : txtMuted, fontWeight: '900', fontSize: 11 }}>DEVICE MIC</Text>
          <Text style={{ color: bldMic ? '#FF9500' : txtMuted, fontSize: 9, opacity: 0.8 }}>0x27</Text>
        </TouchableOpacity>
      </View>

      <View style={{ flexDirection: 'row', gap: Spacing.md, marginBottom: Spacing.md }}>
        <View style={{ flex: 1 }}>
          <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>SENSITIVITY</Text>
          <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldSens} keyboardType="numeric" onChangeText={setBldSens} />
        </View>
        <View style={{ flex: 1 }}>
          <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>BRIGHTNESS</Text>
          <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldBright} keyboardType="numeric" onChangeText={setBldBright} />
        </View>
      </View>

      <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.sm, fontWeight: '900' }}>COLOR 1 (PRIMARY)</Text>
      <QuickColorGrid activeColor={bldColors[0]} onSelect={c => setBldColors([c])} />

      <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.sm, fontWeight: '900' }}>COLOR 2 (SECONDARY)</Text>
      <QuickColorGrid activeColor={bldC2} onSelect={setBldC2} />

      {/* Byte preview */}
      <View style={{ backgroundColor: isDark ? '#05070a' : '#f9fafb', borderRadius: 8, padding: Spacing.md, marginTop: Spacing.md, borderColor: border, borderWidth: 1 }}>
        <Text style={{ color: cyan, fontSize: 10, fontFamily: Platform.select({ ios: 'Menlo', default: 'monospace' }) }}>
          {'[0x73, '}
          {`0x${(parseInt(bldMusicMode)||1).toString(16).padStart(2,'0').toUpperCase()}, `}
          {`${bldMic ? '0x27' : '0x26'}, `}
          {`${bldMusicIsOn ? '0x01' : '0x00'}, `}
          {'R1, G1, B1, R2, G2, B2, SENS, BRIGHT, CS]'}
        </Text>
        <Text style={{ color: txtMuted, fontSize: 9, marginTop: Spacing.xs }}>13 bytes (0xA3 format) — was 12B (broken, no isOn)</Text>
      </View>

      <TouchableOpacity
        style={[S.txBtn, { backgroundColor: '#FF9500', borderColor: '#FF9500', marginTop: Spacing.md }]}
        onPress={() => {
          const mic: 0x26 | 0x27 = bldMic ? 0x27 : 0x26;
          transmit(
            ZenggeProtocol.setMusicConfig(
              parseInt(bldMusicMode) || 1,
              mic,
              bldMusicIsOn,
              bldColors[0] || {r:255,g:0,b:0},
              bldC2 || {r:0,g:0,b:255},
              parseInt(bldSens) || 128,
              parseInt(bldBright) || 100
            ),
            `0x73 mode=${bldMusicMode} mic=${bldMic?'0x27':'0x26'} isOn=${bldMusicIsOn}`,
            '0x73'
          );
        }}
      >
        <Text style={{ color: '#000', fontWeight: '900', fontSize: 12 }}>TX 0x73 (13B FORMAT)</Text>
      </TouchableOpacity>
    </View>
  );
}
