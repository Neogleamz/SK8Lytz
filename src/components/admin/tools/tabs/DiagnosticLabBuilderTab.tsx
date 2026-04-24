import React, { useState, useEffect } from 'react';
import { ScrollView, Text, TouchableOpacity, View, TextInput, Platform } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Spacing } from '../../../../theme/theme';
import { useDiagnosticLabStyles } from './DiagnosticLabStyles';
import { DiagnosticLabHwBadge } from './DiagnosticLabHwBadge';
import { TRANSITION_TYPES } from './DiagnosticLabConstants';
import { QuickColorGrid } from './DiagnosticLabQuickColorGrid';
import { ZenggeProtocol } from '../../../../protocols/ZenggeProtocol';
import CustomEffectVisualizer from '../../../CustomEffectVisualizer';

interface BuilderTabProps {
  targetDeviceId: string | null;
  connectedDevices: any[];
  hwSettings: any;
  hwPts: number;
  builderCtx: any;
  transmit: (cmd: number[], label: string, opcode?: string) => void;
  sendRawHex: (hex: string, label?: string) => void;
}

export function DiagnosticLabBuilderTab({
  targetDeviceId,
  connectedDevices,
  hwSettings,
  hwPts,
  builderCtx,
  transmit,
  sendRawHex,
}: BuilderTabProps) {
  const { S, txtPri, txtMuted, cardBg, border, isDark, cyan } = useDiagnosticLabStyles();

  const {
    bldProtocol, setBldProtocol,
    bldColors, setBldColors,
    bldTrans, setBldTrans,
    bldSpeed, setBldSpeed,
    bldPoints, setBldPoints,
    bldDir, setBldDir,
    bldPatternId, setBldPatternId,
    bldBright, setBldBright,
    bld51Mode, setBld51Mode,
    bld51Speed, setBld51Speed,
    bld51Color1, setBld51Color1,
    bld51Color2, setBld51Color2,
    bld51Dir, setBld51Dir,
    bld51Seg, setBld51Seg,
    bldMic, setBldMic,
    bldMusicMode, setBldMusicMode,
    bldSens, setBldSens,
    bldC2, setBldC2,
    bldMatrixStyle, setBldMatrixStyle,
    bldIc, setBldIc,
    bldOrder, setBldOrder,
    bldSegs, setBldSegs,
    bldResult
  } = builderCtx;

  // Manual override for edited hex
  const [bldHexOverride, setBldHexOverride] = useState('');
  
  // 0x73 isOn state for builder + Oracle fix
  const [bldMusicIsOn, setBldMusicIsOn] = useState(true);

  // Sync override with result whenever result changes (unless manually edited)
  useEffect(() => {
    if (bldResult?.hex) setBldHexOverride(bldResult.hex);
  }, [bldResult]);

  const sendSolid = (r: number, g: number, b: number, pts: number, trans: number, note: string) => {
    const pixels = Array(pts).fill({ r, g, b });
    const numPoints = pixels.length;
    const totalLen = numPoints * 3 + 9;
    const raw = new Array(totalLen).fill(0);
    raw[0] = 0x59; raw[1] = (totalLen >> 8) & 0xFF; raw[2] = totalLen & 0xFF;
    let idx = 3;
    for (const p of pixels) { raw[idx++] = p.r; raw[idx++] = p.g; raw[idx++] = p.b; }
    raw[idx++] = (numPoints >> 8) & 0xFF; raw[idx++] = numPoints & 0xFF;
    raw[idx++] = trans & 0xFF; raw[idx++] = 1; raw[idx++] = 1;
    raw[idx] = ZenggeProtocol.calculateChecksum(raw.slice(0, totalLen - 1));
    transmit(ZenggeProtocol.wrapCommand(raw), note);
  };

  return (
    <ScrollView contentContainerStyle={{ paddingBottom: Spacing.xxxl }}>
      <Text style={[S.sectionTitle, { color: txtPri }]}>PROTOCOL BUILDER</Text>
      <Text style={[S.hint, { color: txtMuted }]}>Select a protocol and build the exact hex packet.</Text>
      
      <DiagnosticLabHwBadge 
        targetDeviceId={targetDeviceId}
        connectedDevices={connectedDevices}
        hwSettings={hwSettings}
      />

      <Text style={[S.subTitle, { color: txtMuted }]}>PROTOCOL</Text>
      <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.sm, marginBottom: Spacing.lg }}>
        {[
          { id: '0x51', label: '0x51 DIY Mode' },
          { id: '0x59', label: '0x59 Segments' },
          { id: '0x61', label: '0x61 RBM Pattern' },
          { id: '0x73', label: '0x73 Symphony' },
          { id: '0x62', label: '0x62 Setup' }
        ].map(p => (
           <TouchableOpacity key={p.id}
             style={[S.chip, bldProtocol === p.id && S.chipActive, { backgroundColor: bldProtocol === p.id ? cyan + '22' : cardBg, borderColor: bldProtocol === p.id ? cyan : border }]}
             onPress={() => setBldProtocol(p.id as any)}>
             <Text style={{ color: bldProtocol === p.id ? cyan : txtMuted, fontWeight: '900', fontSize: 11 }}>{p.label}</Text>
           </TouchableOpacity>
        ))}
      </View>

      {bldProtocol === '0x51' && (
        <View style={[S.diagBox, { backgroundColor: cardBg, borderColor: border }]}>
          <Text style={[S.subTitle, { color: txtPri, marginTop: 0 }]}>DIY MODE MATH SIMULATOR</Text>
          <View style={{ alignItems: 'center', marginBottom: Spacing.lg }}>
            <CustomEffectVisualizer 
              effectId={parseInt(bld51Mode) || 1} 
              fgColorHex={`#${bld51Color1.r.toString(16).padStart(2,'0')}${bld51Color1.g.toString(16).padStart(2,'0')}${bld51Color1.b.toString(16).padStart(2,'0')}`}
              bgColorHex={`#${bld51Color2.r.toString(16).padStart(2,'0')}${bld51Color2.g.toString(16).padStart(2,'0')}${bld51Color2.b.toString(16).padStart(2,'0')}`}
              speed={Math.max(1, Math.min(31, parseInt(bld51Speed) || 16))}
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
              <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>SPEED (1–31)</Text>
              <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bld51Speed.toString()} keyboardType="numeric" onChangeText={v => setBld51Speed(Math.max(1, Math.min(31, parseInt(v)||1)).toString())} />
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
        </View>
      )}

      {bldProtocol === '0x59' && (
        <View style={[S.diagBox, { backgroundColor: cardBg, borderColor: border }]}>
          <Text style={[S.subTitle, { color: txtPri, marginTop: 0 }]}>PIXEL COLORS (0x59)</Text>
          <Text style={[S.hint, { color: txtMuted }]}>Colors will be repeated to fill the LED count.</Text>
          
          {bldColors.map((c: any, i: number) => (
             <View key={i} style={{ flexDirection: 'row', alignItems: 'center', marginBottom: Spacing.sm, gap: Spacing.sm }}>
                <View style={{ width: 24, height: 24, backgroundColor: `rgb(${c.r},${c.g},${c.b})`, borderRadius: 6, borderWidth: 1, borderColor: border }} />
                <TextInput style={[{flex:1, backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri}, S.numInput]} value={c.r.toString()} keyboardType="numeric" onChangeText={v => { const cur = [...bldColors]; cur[i].r = parseInt(v)||0; setBldColors(cur); }} placeholder="R" placeholderTextColor={txtMuted} />
                <TextInput style={[{flex:1, backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri}, S.numInput]} value={c.g.toString()} keyboardType="numeric" onChangeText={v => { const cur = [...bldColors]; cur[i].g = parseInt(v)||0; setBldColors(cur); }} placeholder="G" placeholderTextColor={txtMuted} />
                <TextInput style={[{flex:1, backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri}, S.numInput]} value={c.b.toString()} keyboardType="numeric" onChangeText={v => { const cur = [...bldColors]; cur[i].b = parseInt(v)||0; setBldColors(cur); }} placeholder="B" placeholderTextColor={txtMuted} />
                <TouchableOpacity onPress={() => setBldColors(bldColors.filter((_: any, idx: number)=>idx!==i))} style={{ padding: Spacing.sm }}>
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
      )}

      {bldProtocol === '0x61' && (
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
      )}

      {bldProtocol === '0x73' && (
        <View style={[S.diagBox, { backgroundColor: cardBg, borderColor: '#FF4040', borderWidth: 1 }]}>
          <Text style={[S.subTitle, { color: '#FF9500', marginTop: 0 }]}>SYMPHONY MODE (0x73) — APK 13B FORMAT</Text>
          <Text style={{ color: '#FF4040', fontSize: 10, marginBottom: Spacing.md, fontWeight: '700' }}>
            ⚠️ APK-VERIFIED: 13B format · mic=0x26 (APP) / 0x27 (DEVICE) · isOn byte at position [3]
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
          <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.sm, fontWeight: '900' }}>MIC SOURCE [BYTE 2] — APK TRUTH</Text>
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
            <Text style={{ color: cyan, fontSize: 10, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>
              {'[0x73, '}
              {`0x${(parseInt(bldMusicMode)||1).toString(16).padStart(2,'0').toUpperCase()}, `}
              {`${bldMic ? '0x27' : '0x26'}, `}
              {`${bldMusicIsOn ? '0x01' : '0x00'}, `}
              {'R1, G1, B1, R2, G2, B2, SENS, BRIGHT, CS]'}
            </Text>
            <Text style={{ color: txtMuted, fontSize: 9, marginTop: Spacing.xs }}>13 bytes (APK 0xA3 format) — was 12B (broken, no isOn)</Text>
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
            <Text style={{ color: '#000', fontWeight: '900', fontSize: 12 }}>TX 0x73 (13B APK FORMAT)</Text>
          </TouchableOpacity>
        </View>
      )}

      {bldProtocol === '0x62' && (
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
      )}

      <Text style={[S.subTitle, { color: txtMuted }]}>GENERATED PAYLOAD — BYTE ANNOTATIONS</Text>
      {bldResult && (
        <View style={[S.diagBox, { backgroundColor: isDark ? '#05070a' : '#f9fafb', borderColor: border, padding: Spacing.md }]}>
          {bldResult.annotations.map((a: string, i: number) => (
            <Text key={i} style={{ color: i === 4 ? TRANSITION_TYPES.find(t=>t.byte===bldTrans)?.color ?? cyan : txtPri, fontSize: 11, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', marginBottom: Spacing.xxs }}>
              {a}
            </Text>
          ))}
        </View>
      )}

      <Text style={[S.subTitle, { color: txtMuted }]}>FULL HEX (EDITABLE)</Text>
      <TextInput
        style={[S.hexInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]}
        defaultValue={bldResult?.hex || ''}
        onChangeText={setBldHexOverride}
        placeholder="59 00 0F FF 00 00 ..."
        placeholderTextColor={txtMuted}
        multiline
      />
      <View style={{ flexDirection: 'row', gap: Spacing.sm, marginTop: Spacing.md }}>
        <TouchableOpacity style={[S.txBtn, { flex: 1, backgroundColor: cyan, borderColor: cyan }]}
          onPress={() => sendRawHex(bldHexOverride, `Builder: trans=0x0${bldTrans.toString(16).toUpperCase()} pts=${bldPoints}`)}>
          <Text style={{ color: '#000', fontWeight: '900', fontSize: 12 }}>TX PAYLOAD</Text>
        </TouchableOpacity>
        <TouchableOpacity style={[S.txBtn, { flex: 1, backgroundColor: border, borderColor: border }]}
          onPress={() => bldResult && setBldHexOverride(bldResult.hex)}>
          <Text style={{ color: txtPri, fontWeight: '900', fontSize: 12 }}>RESET HEX</Text>
        </TouchableOpacity>
      </View>

      {/* Quick presets */}
      <Text style={[S.subTitle, { color: txtMuted }]}>QUICK PRESETS</Text>
      <View style={{ gap: Spacing.sm }}>
        {[
          { label: 'FULL RED — FREEZE', r:255,g:0,b:0, trans:0x01, note:'Pure red, FREEZE — should be solid red' },
          { label: 'FULL GREEN — FREEZE', r:0,g:255,b:0, trans:0x01, note:'Pure green, FREEZE — should be solid green' },
          { label: 'FULL RED — CASCADE', r:255,g:0,b:0, trans:0x00, note:'Pure red, CASCADE — should scroll/chase' },
          { label: 'FULL WHITE — FREEZE', r:255,g:255,b:255, trans:0x01, note:'Full white FREEZE — all LEDs max brightness' },
          { label: 'POLL 0x63', note:'Query hardware config', isCmd: true, cmd: ZenggeProtocol.queryHardwareSettings?.() },
        ].map((preset, i) => (
          <TouchableOpacity key={i} style={[S.presetBtn, { backgroundColor: cardBg, borderColor: border }]}
            onPress={() => {
              if ('isCmd' in preset && preset.isCmd && preset.cmd) {
                transmit(ZenggeProtocol.wrapCommand(preset.cmd), preset.note);
              } else if ('r' in preset) {
                const p = preset as { r: number; g: number; b: number; trans: number; note: string };
                const pts = parseInt(bldPoints) || hwPts || 16;
                sendSolid(p.r, p.g, p.b, pts, p.trans ?? 0x01, p.note);
              }
            }}>
            <Text style={{ color: cyan, fontSize: 12, fontWeight: '900' }}>{preset.label}</Text>
            <Text style={{ color: txtMuted, fontSize: 10, marginTop: Spacing.xxs }}>{preset.note}</Text>
          </TouchableOpacity>
        ))}
      </View>
    </ScrollView>
  );
}
