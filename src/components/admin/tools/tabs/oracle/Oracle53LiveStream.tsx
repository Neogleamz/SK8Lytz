import React, { useState, useEffect } from 'react';
import { View, Text, TouchableOpacity, TextInput } from 'react-native';
import { Spacing } from '../../../../../theme/theme';
import { useDiagnosticLabStyles } from '../DiagnosticLabStyles';
import { QuickColorGrid } from '../DiagnosticLabQuickColorGrid';
import { ZenggeProtocol } from '../../../../../protocols/ZenggeProtocol';

interface Oracle53LiveStreamProps {
  hwPts: number;
  transmit: (cmd: number[], label: string, opcode?: string) => void;
}

export function Oracle53LiveStream({ hwPts, transmit }: Oracle53LiveStreamProps) {
  const { S, txtPri, txtMuted, isDark, border } = useDiagnosticLabStyles();
  
  const [p53Fps, setP53Fps] = useState(10);
  const [p53Active, setP53Active] = useState(false);
  const [p53GradStart, setP53GradStart] = useState({r:0,g:255,b:255});
  const [p53GradEnd, setP53GradEnd] = useState({r:255,g:0,b:255});

  useEffect(() => {
    if (!p53Active) return;
    const buildGradient = () => {
      const pixels = Array.from({length: hwPts}, (_, i) => {
        const t = hwPts > 1 ? i / (hwPts - 1) : 0;
        return {
          r: Math.round(p53GradStart.r + (p53GradEnd.r - p53GradStart.r) * t),
          g: Math.round(p53GradStart.g + (p53GradEnd.g - p53GradStart.g) * t),
          b: Math.round(p53GradStart.b + (p53GradEnd.b - p53GradStart.b) * t),
        };
      });
      return pixels;
    };
    const ms = Math.max(33, Math.round(1000 / Math.max(1, Math.min(60, p53Fps))));
    const id = setInterval(() => {
      transmit(ZenggeProtocol.streamPixelFrame(buildGradient()), `0x53 frame @ ${p53Fps}fps`, '0x53');
    }, ms);
    return () => clearInterval(id);
  }, [p53Active, p53Fps, p53GradStart, p53GradEnd, hwPts, transmit]);

  return (
    <View style={[S.diagBox, { borderColor: p53Active ? '#00CC88' : border, borderWidth: p53Active ? 2 : 1 }]}>
      <Text style={{ color: txtMuted, fontSize: 10, fontWeight: '900', marginBottom: Spacing.sm }}>GRADIENT START COLOR</Text>
      <QuickColorGrid activeColor={p53GradStart} onSelect={setP53GradStart} />
      <Text style={{ color: txtMuted, fontSize: 10, fontWeight: '900', marginBottom: Spacing.sm }}>GRADIENT END COLOR</Text>
      <QuickColorGrid activeColor={p53GradEnd} onSelect={setP53GradEnd} />
      <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.md, marginBottom: Spacing.md }}>
        <Text style={{ color: txtMuted, fontSize: 10, fontWeight: '900' }}>FPS (1–60):</Text>
        <TextInput style={[S.numInput, { flex: 1, backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={String(p53Fps)} keyboardType="numeric" onChangeText={v => setP53Fps(Math.max(1, Math.min(60, parseInt(v)||1)))} />
        <Text style={{ color: txtMuted, fontSize: 10 }}>= {Math.round(1000 / Math.max(1, p53Fps))}ms/frame</Text>
      </View>
      <View style={{ flexDirection: 'row', gap: Spacing.md }}>
        <TouchableOpacity onPress={() => {transmit(ZenggeProtocol.streamPixelFrame(Array.from({length:hwPts},(_,i)=>{const t=hwPts>1?i/(hwPts-1):0;return {r:Math.round(p53GradStart.r+(p53GradEnd.r-p53GradStart.r)*t),g:Math.round(p53GradStart.g+(p53GradEnd.g-p53GradStart.g)*t),b:Math.round(p53GradStart.b+(p53GradEnd.b-p53GradStart.b)*t)}})), '0x53 single frame', '0x53');}}
          style={[S.txBtn, { flex: 1, backgroundColor: border, borderColor: '#00CC88' }]}>
          <Text style={{ color: '#00CC88', fontWeight: '900', fontSize: 11 }}>SINGLE FRAME</Text>
        </TouchableOpacity>
        <TouchableOpacity onPress={() => setP53Active(!p53Active)}
          style={[S.txBtn, { flex: 1, backgroundColor: p53Active ? '#00CC88' : border, borderColor: '#00CC88' }]}>
          <Text style={{ color: p53Active ? '#000' : '#00CC88', fontWeight: '900', fontSize: 11 }}>
            {p53Active ? `STOP (${p53Fps}fps)` : `START ${p53Fps}fps`}
          </Text>
        </TouchableOpacity>
      </View>
      {p53Active && (
        <Text style={{ color: '#00CC88', fontSize: 10, marginTop: Spacing.sm, fontWeight: 'bold' }}>
          ● Streaming 0x53 frames @ {p53Fps}fps — observe LED rendering latency
        </Text>
      )}
    </View>
  );
}
