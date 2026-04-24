import React from 'react';
import { ScrollView, Text, TouchableOpacity, View } from 'react-native';
import { Spacing } from '../../../../theme/theme';
import { useDiagnosticLabStyles } from './DiagnosticLabStyles';
import { DiagnosticLabHwBadge } from './DiagnosticLabHwBadge';
import { ZenggeProtocol } from '../../../../protocols/ZenggeProtocol';

interface ColorTabProps {
  targetDeviceId: string | null;
  connectedDevices: any[];
  hwSettings: any;
  hwPts: number;
  transmit: (cmd: number[], label: string, opcode?: string) => void;
}

export function DiagnosticLabColorTab({
  targetDeviceId,
  connectedDevices,
  hwSettings,
  hwPts,
  transmit,
}: ColorTabProps) {
  const { S, txtPri, txtMuted, cardBg, border, isDark } = useDiagnosticLabStyles();

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
      <Text style={[S.sectionTitle, { color: txtPri }]}>COLOR ORDER TEST</Text>
      <Text style={[S.hint, { color: txtMuted }]}>
        Tap each button — observe what color the hardware actually shows.{'\n'}
        This identifies whether hardware auto-remaps GRB or needs pre-swapped bytes.
      </Text>

      <DiagnosticLabHwBadge 
        targetDeviceId={targetDeviceId}
        connectedDevices={connectedDevices}
        hwSettings={hwSettings}
      />

      <Text style={[S.subTitle, { color: txtMuted }]}>PRIMARY COLORS (Pure RGB bytes)</Text>
      <View style={S.colorBtnRow}>
        {[
          { label: 'SEND RED\n[255,0,0]',   r:255, g:0,   b:0,   bg: isDark ? '#401010' : '#fee2e2', border:'#FF4040', note:'Pure RED [255,0,0]' },
          { label: 'SEND GREEN\n[0,255,0]', r:0,   g:255, b:0,   bg: isDark ? '#104010' : '#f0fdf4', border:'#00CC00', note:'Pure GREEN [0,255,0]' },
          { label: 'SEND BLUE\n[0,0,255]',  r:0,   g:0,   b:255, bg: isDark ? '#101040' : '#eff6ff', border:'#4040FF', note:'Pure BLUE [0,0,255]' },
        ].map(btn => (
          <TouchableOpacity key={btn.label} style={[S.bigColorBtn, { backgroundColor: btn.bg, borderColor: btn.border }]}
            onPress={() => sendSolid(btn.r, btn.g, btn.b, hwPts, 0x01, btn.note)}>
            <Text style={{ color: btn.border, fontWeight: '900', textAlign: 'center', fontSize: 11 }}>{btn.label}</Text>
          </TouchableOpacity>
        ))}
      </View>

      <Text style={[S.subTitle, { color: txtMuted }]}>DIAGNOSIS GUIDE</Text>
      <View style={[S.diagBox, { backgroundColor: cardBg, borderColor: border }]}>
        <Text style={[S.diagLine, { color: txtPri }]}>🟢 If RED btn → RED light: hardware auto-remaps. ✅</Text>
        <Text style={[S.diagLine, { color: txtPri, opacity: 0.8 }]}>🔴 If RED btn → GREEN light: hardware is GRB-wired.</Text>
        <Text style={[S.diagLine, { color: txtMuted, fontSize: 10, fontStyle: 'italic' }]}>   → Must applyColorSorting(sortingIndex=2)</Text>
      </View>
    </ScrollView>
  );
}
