import React from 'react';
import { ScrollView, Text, TouchableOpacity, View } from 'react-native';
import { Spacing } from '../../../../theme/theme';
import { useDiagnosticLabStyles } from './DiagnosticLabStyles';
import { DiagnosticLabHwBadge } from './DiagnosticLabHwBadge';
import { ZenggeProtocol } from '../../../../protocols/ZenggeProtocol';

import { DiagnosticDevice, DiagnosticHwSettings } from './DiagnosticLabTypes';

interface ColorTabProps {
  targetDeviceId: string | null;
  connectedDevices: DiagnosticDevice[];
  hwSettings: DiagnosticHwSettings | undefined;
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
    const safePts = Math.max(12, pts);
    const pixels = Array(safePts).fill({ r, g, b });
    const payload = ZenggeProtocol.setMultiColor(pixels, safePts, 1, 1, trans);
    transmit(payload, note);
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
