import React from 'react';
import { ScrollView, Text, TouchableOpacity, View, Platform } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Spacing } from '../../../../theme/theme';
import { useDiagnosticLabStyles } from './DiagnosticLabStyles';
import { DiagnosticLabHwBadge } from './DiagnosticLabHwBadge';
import { TRANSITION_TYPES } from './DiagnosticLabConstants';
import { ZenggeProtocol } from '../../../../protocols/ZenggeProtocol';

import { DiagnosticDevice, DiagnosticHwSettings } from './DiagnosticLabTypes';

interface TransitionTabProps {
  targetDeviceId: string | null;
  connectedDevices: DiagnosticDevice[];
  hwSettings: DiagnosticHwSettings | undefined;
  hwPts: number;
  bldColors: { r: number; g: number; b: number }[];
  bldSpeed: number;
  lastSent: string | null;
  lastNote: string | null;
  transmit: (cmd: number[], label: string, opcode?: string) => void;
}

export function DiagnosticLabTransitionTab({
  targetDeviceId,
  connectedDevices,
  hwSettings,
  hwPts,
  bldColors,
  bldSpeed,
  lastSent,
  lastNote,
  transmit,
}: TransitionTabProps) {
  const { S, txtPri, txtMuted, cardBg, border, isDark, cyan } = useDiagnosticLabStyles();

  const MonoText = ({ children, color = cyan }: {children:string; color?:string}) => (
    <Text style={{ color, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', fontSize: 11 }}>
      {children}
    </Text>
  );

  return (
    <ScrollView contentContainerStyle={{ paddingBottom: Spacing.xxxl }}>
      <Text style={[S.sectionTitle, { color: txtPri }]}>TRANSITION TYPE PROBE</Text>
      <Text style={[S.hint, { color: txtMuted }]}>
        Each button sends the same color with a different transitionType byte.{'\n'}
        Observe what the hardware actually does — we need to confirm 0x00 vs 0x01 behavior.
      </Text>
      
      <DiagnosticLabHwBadge 
        targetDeviceId={targetDeviceId}
        connectedDevices={connectedDevices}
        hwSettings={hwSettings}
      />

      <Text style={[S.subTitle, { color: txtMuted }]}>COLOR TO SEND (change in BUILDER tab first)</Text>
      <View style={{ flexDirection: 'row', gap: Spacing.md, marginBottom: Spacing.lg, alignItems: 'center' }}>
        <View style={{ width: 40, height: 40, backgroundColor: `rgb(${bldColors[0]?.r||0},${bldColors[0]?.g||0},${bldColors[0]?.b||0})`, borderRadius: 8, borderWidth: 1, borderColor: border }} />
        <Text style={{ color: txtMuted, fontSize: 12 }}>R:{bldColors[0]?.r||0} G:{bldColors[0]?.g||0} B:{bldColors[0]?.b||0} · {hwPts} LEDs · Speed:{bldSpeed}</Text>
      </View>

      {TRANSITION_TYPES.map(tt => (
        <TouchableOpacity key={tt.byte}
          style={[S.transBtn, { borderColor: tt.color, backgroundColor: cardBg }]}
          onPress={() => {
            const numPoints = Math.max(12, hwPts);
            const r = bldColors[0]?.r || 0;
            const g = bldColors[0]?.g || 0;
            const b = bldColors[0]?.b || 0;
            const pixels = Array(numPoints).fill({ r, g, b });
            const payload = ZenggeProtocol.setMultiColor(pixels, numPoints, bldSpeed, 1, tt.byte);
            transmit(payload, `transitionType=0x0${tt.byte.toString(16).toUpperCase()} ${tt.label}`);
          }}>
          <View style={[S.transByteBadge, { backgroundColor: tt.color + '22', borderColor: tt.color }]}>
            <Text style={{ color: tt.color, fontWeight: '900', fontSize: 14 }}>0x0{tt.byte.toString(16).toUpperCase()}</Text>
          </View>
          <View style={{ flex: 1, marginLeft: Spacing.md }}>
            <Text style={{ color: tt.color, fontWeight: '900', fontSize: 13 }}>{tt.label}</Text>
            <Text style={{ color: txtMuted, fontSize: 11, marginTop: Spacing.xxs }}>{tt.desc}</Text>
          </View>
          <MaterialCommunityIcons name="send" size={18} color={tt.color} />
        </TouchableOpacity>
      ))}

      <View style={[S.diagBox, { backgroundColor: cardBg, borderColor: border }]}>
        <Text style={[S.diagLine, { color: txtPri, fontWeight: 'bold' }]}>📝 WHAT WE EXPECT (from Master Reference):</Text>
        <Text style={[S.diagLine, { color: txtPri }]}>0x00 = CASCADE — LEDs should scroll/chase</Text>
        <Text style={[S.diagLine, { color: txtPri }]}>0x01 = FREEZE — LEDs should sit perfectly still (SOLID)</Text>
        <Text style={[S.diagLine, { color: txtPri }]}>0x02 = STROBE — LEDs should flash on/off</Text>
        <Text style={[S.diagLine, { color: txtPri }]}>0x03 = UNKNOWN — document what this actually does</Text>
        <Text style={[S.diagLine, { color: '#FF9500', marginTop: Spacing.sm }]}>👉 If SOLID looks wrong, it's 0x00 vs 0x01 confusion.</Text>
      </View>

      <Text style={[S.subTitle, { color: txtMuted }]}>LAST SENT</Text>
      {lastSent ? (
        <View style={[S.sentBox, { backgroundColor: isDark ? '#05070a' : '#f9fafb', borderColor: border }]}>
          <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs }}>{lastNote}</Text>
          <MonoText color={cyan}>{lastSent}</MonoText>
        </View>
      ) : <Text style={{ color: txtMuted, fontSize: 12, marginTop: Spacing.sm }}>Nothing sent yet.</Text>}
    </ScrollView>
  );
}
