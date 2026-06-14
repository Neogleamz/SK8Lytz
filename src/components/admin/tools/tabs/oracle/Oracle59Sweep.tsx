import React, { useState } from 'react';
import { View, Text, TouchableOpacity, Platform } from 'react-native';
import { Spacing } from '../../../../../theme/theme';
import { useDiagnosticLabStyles } from '../DiagnosticLabStyles';
import { TRANSITION_TYPES } from '../DiagnosticLabConstants';
import { QuickColorGrid } from '../DiagnosticLabQuickColorGrid';
import { ZenggeProtocol } from '../../../../../protocols/ZenggeProtocol';

interface Oracle59SweepProps {
  hwPts: number;
  transmit: (cmd: number[], label: string, opcode?: string) => void;
}

export function Oracle59Sweep({ hwPts, transmit }: Oracle59SweepProps) {
  const { S, txtMuted, border } = useDiagnosticLabStyles();
  
  const [ttSweepResults, setTtSweepResults] = useState<Record<string, 'WORKS'|'NO_EFFECT'|'CRASHED'>>({});
  const [p41Color1, setP41Color1] = useState<{r:number;g:number;b:number}>({r:255,g:0,b:0});

  return (
    <View style={[S.diagBox, { borderColor: '#FBBF24', borderWidth: 1 }]}>
      <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.sm }}>
        Sends a solid RED 0x59 array ({hwPts} LEDs) with each transitionType byte.{'\n'}
        Tap SEND → observe hardware → log result. 0x06 is the key unknown.
      </Text>
      {/* Summary grid */}
      <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.sm, marginBottom: Spacing.md }}>
        {TRANSITION_TYPES.map(tt => {
          const key = `0x0${tt.byte.toString(16).toUpperCase()}`;
          const res = ttSweepResults[key];
          const cellColor = res === 'WORKS' ? '#00CC88' : res === 'NO_EFFECT' ? '#FF9500' : res === 'CRASHED' ? '#FF4040' : border;
          return (
            <View key={tt.byte} style={{ alignItems: 'center', width: 44 }}>
              <View style={{ width: 36, height: 36, borderRadius: 8, backgroundColor: cellColor + '33', borderWidth: 1.5, borderColor: cellColor, justifyContent: 'center', alignItems: 'center' }}>
                <Text style={{ color: cellColor, fontSize: 9, fontWeight: '900', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>{key}</Text>
              </View>
              <Text style={{ color: cellColor, fontSize: 8, marginTop: 2 }}>{res ?? '—'}</Text>
            </View>
          );
        })}
      </View>
      <Text style={{ color: '#FBBF24', fontSize: 10, fontWeight: '900', marginBottom: Spacing.xs }}>FG COLOR FOR SWEEP</Text>
      <QuickColorGrid activeColor={p41Color1} onSelect={setP41Color1} />
      {TRANSITION_TYPES.map(tt => {
        const key = `0x0${tt.byte.toString(16).toUpperCase()}`;
        const res = ttSweepResults[key];
        const setByte = (r: 'WORKS' | 'NO_EFFECT' | 'CRASHED') =>
          setTtSweepResults(prev => ({ ...prev, [key]: r }));
        return (
          <View key={tt.byte} style={[S.diagBox, { borderColor: tt.color, borderWidth: 1, marginBottom: Spacing.sm }]}>
            <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: Spacing.sm }}>
              <View>
                <Text style={{ color: tt.color, fontWeight: '900', fontSize: 12 }}>
                  {key}  {tt.label}  {tt.confirmed ? '✅' : '❓'}
                </Text>
                <Text style={{ color: txtMuted, fontSize: 10, marginTop: 2 }}>{tt.desc}</Text>
              </View>
              <TouchableOpacity
                onPress={() => {
                  const numPoints = Math.max(12, hwPts);
                  const pixels = Array(numPoints).fill(p41Color1);
                  const payload = ZenggeProtocol.setMultiColor(pixels, numPoints, 50, 1, tt.byte);
                  transmit(payload, `0x59 transType=${key} ${tt.label}`, '0x59');
                }}
                style={{ backgroundColor: tt.color + '22', borderWidth: 1, borderColor: tt.color, paddingHorizontal: Spacing.lg, paddingVertical: Spacing.md, borderRadius: 8 }}
              >
                <Text style={{ color: tt.color, fontWeight: '900', fontSize: 11 }}>SEND</Text>
              </TouchableOpacity>
            </View>
            <View style={{ flexDirection: 'row', gap: Spacing.sm }}>
              {(['WORKS', 'NO_EFFECT', 'CRASHED'] as const).map(r => (
                <TouchableOpacity key={r} onPress={() => setByte(r)}
                  style={{ flex: 1, paddingVertical: Spacing.sm, borderRadius: 6, alignItems: 'center',
                    backgroundColor: res === r ? (r === 'WORKS' ? '#00CC8822' : r === 'CRASHED' ? '#FF404022' : '#FF950022') : 'transparent',
                    borderWidth: 1, borderColor: res === r ? (r === 'WORKS' ? '#00CC88' : r === 'CRASHED' ? '#FF4040' : '#FF9500') : border }}
                >
                  <Text style={{ color: res === r ? (r === 'WORKS' ? '#00CC88' : r === 'CRASHED' ? '#FF4040' : '#FF9500') : txtMuted, fontSize: 9, fontWeight: '900' }}>
                    {r === 'WORKS' ? '✅ WORKS' : r === 'NO_EFFECT' ? '⚠️ NO FX' : '💀 CRASH'}
                  </Text>
                </TouchableOpacity>
              ))}
            </View>
          </View>
        );
      })}
      <TouchableOpacity onPress={() => setTtSweepResults({})} style={{ alignSelf: 'flex-end', marginTop: Spacing.sm }}>
        <Text style={{ color: '#FF4040', fontSize: 10, fontWeight: '900' }}>RESET RESULTS</Text>
      </TouchableOpacity>
    </View>
  );
}
