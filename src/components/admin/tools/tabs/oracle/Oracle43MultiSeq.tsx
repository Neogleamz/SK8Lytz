import React, { useState } from 'react';
import { View, Text, TouchableOpacity, TextInput } from 'react-native';
import { Spacing } from '../../../../../theme/theme';
import { useDiagnosticLabStyles } from '../DiagnosticLabStyles';
import { ZenggeProtocol } from '../../../../../protocols/ZenggeProtocol';

interface Oracle43MultiSeqProps {
  transmit: (cmd: number[], label: string, opcode?: string) => void;
}

export function Oracle43MultiSeq({ transmit }: Oracle43MultiSeqProps) {
  const { S, txtPri, txtMuted, isDark, border } = useDiagnosticLabStyles();
  
  const [p43Ids, setP43Ids] = useState<number[]>([1,5,10]);
  const [p43Speed, setP43Speed] = useState(50);
  const [p43Bright, setP43Bright] = useState(100);

  return (
    <View style={[S.diagBox, { borderColor: '#FF69B4', borderWidth: 1 }]}>
      <Text style={{ color: txtMuted, fontSize: 10, fontWeight: '900', marginBottom: Spacing.sm }}>EFFECT IDs (tap to toggle, max 50)</Text>
      <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.xs, marginBottom: Spacing.lg }}>
        {Array.from({length: 50}, (_, i) => i + 1).map(id => {
          const sel = p43Ids.includes(id);
          return (
            <TouchableOpacity key={id} onPress={() => {
              if (sel) setP43Ids(p43Ids.filter(x => x !== id));
              else if (p43Ids.length < 50) setP43Ids([...p43Ids, id]);
            }}
              style={{ width: 36, height: 36, borderRadius: 6, alignItems: 'center', justifyContent: 'center',
                backgroundColor: sel ? '#FF69B422' : border, borderWidth: 1, borderColor: sel ? '#FF69B4' : border }}
            >
              <Text style={{ color: sel ? '#FF69B4' : txtMuted, fontSize: 10, fontWeight: sel ? '900' : '400' }}>{id}</Text>
            </TouchableOpacity>
          );
        })}
      </View>
      <Text style={{ color: '#FF69B4', fontSize: 11, fontWeight: '700', marginBottom: Spacing.md }}>
        Selected: [{p43Ids.join(', ')}] ({p43Ids.length} IDs)
      </Text>
      <View style={{ flexDirection: 'row', gap: Spacing.md, marginBottom: Spacing.md }}>
        <View style={{ flex: 1 }}>
          <Text style={{ color: txtMuted, fontSize: 10, fontWeight: '900', marginBottom: Spacing.xs }}>SPEED</Text>
          <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={String(p43Speed)} keyboardType="numeric" onChangeText={v => setP43Speed(Math.max(1, Math.min(100, parseInt(v)||1)))} />
        </View>
        <View style={{ flex: 1 }}>
          <Text style={{ color: txtMuted, fontSize: 10, fontWeight: '900', marginBottom: Spacing.xs }}>BRIGHTNESS</Text>
          <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={String(p43Bright)} keyboardType="numeric" onChangeText={v => setP43Bright(Math.max(1, Math.min(100, parseInt(v)||1)))} />
        </View>
      </View>
      <TouchableOpacity
        style={[S.txBtn, { backgroundColor: p43Ids.length === 0 ? border : '#FF69B4', borderColor: '#FF69B4', opacity: p43Ids.length === 0 ? 0.4 : 1 }]}
        disabled={p43Ids.length === 0}
        onPress={() => transmit(ZenggeProtocol.setEffectSequence(p43Ids, p43Speed, p43Bright), `0x43 ${p43Ids.length} IDs speed=${p43Speed}`, '0x43')}
      >
        <Text style={{ color: '#fff', fontWeight: '900', fontSize: 12 }}>TX 0x43 MULTI-SEQUENCE</Text>
      </TouchableOpacity>
    </View>
  );
}
