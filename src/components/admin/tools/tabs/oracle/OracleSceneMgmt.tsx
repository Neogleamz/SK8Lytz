import React, { useState } from 'react';
import { View, Text, TouchableOpacity, TextInput } from 'react-native';
import { Spacing } from '../../../../../theme/theme';
import { useDiagnosticLabStyles } from '../DiagnosticLabStyles';
import { ZenggeProtocol } from '../../../../../protocols/ZenggeProtocol';

interface OracleSceneMgmtProps {
  transmit: (cmd: number[], label: string, opcode?: string) => void;
}

export function OracleSceneMgmt({ transmit }: OracleSceneMgmtProps) {
  const { S, txtPri, txtMuted, isDark, border } = useDiagnosticLabStyles();
  const [sceneSlot, setSceneSlot] = useState(0);

  return (
    <View style={[S.diagBox, { borderColor: '#FFD700', borderWidth: 1 }]}>
      <Text style={{ color: txtMuted, fontSize: 10, fontWeight: '900', marginBottom: Spacing.sm }}>SCENE SLOT (0–31)</Text>
      <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, marginBottom: Spacing.lg }}>
        <TouchableOpacity onPress={() => setSceneSlot(Math.max(0, sceneSlot - 1))} style={{ backgroundColor: border, borderRadius: 6, width: 36, height: 36, justifyContent: 'center', alignItems: 'center' }}>
          <Text style={{ color: txtPri, fontSize: 18, fontWeight: 'bold' }}>‒</Text>
        </TouchableOpacity>
        <TextInput style={[S.numInput, { flex: 1, backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri, textAlign: 'center' }]} value={String(sceneSlot)} keyboardType="numeric" onChangeText={v => setSceneSlot(Math.max(0, Math.min(31, parseInt(v)||0)))} />
        <TouchableOpacity onPress={() => setSceneSlot(Math.min(31, sceneSlot + 1))} style={{ backgroundColor: border, borderRadius: 6, width: 36, height: 36, justifyContent: 'center', alignItems: 'center' }}>
          <Text style={{ color: txtPri, fontSize: 18, fontWeight: 'bold' }}>+</Text>
        </TouchableOpacity>
      </View>
      <View style={{ gap: Spacing.sm }}>
        <TouchableOpacity
          style={[S.txBtn, { backgroundColor: '#FFD70022', borderColor: '#FFD700' }]}
          onPress={() => {
            const raw = ZenggeProtocol.oracleSceneQuery();
            transmit(ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]), `0x58 QUERY scene state`, '0x58');
          }}
        >
          <Text style={{ color: '#FFD700', fontWeight: '900', fontSize: 12 }}>0x58 QUERY — watch RX for state bytes</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[S.txBtn, { backgroundColor: '#00CC8822', borderColor: '#00CC88' }]}
          onPress={() => {
            const raw = ZenggeProtocol.oracleSceneActivate(sceneSlot);
            transmit(ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]), `0x57 ACTIVATE slot=${sceneSlot}`, '0x57');
          }}
        >
          <Text style={{ color: '#00CC88', fontWeight: '900', fontSize: 12 }}>0x57 ACTIVATE slot {sceneSlot} (speed=50, bright=100)</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[S.txBtn, { backgroundColor: '#FF404022', borderColor: '#FF4040' }]}
          onPress={() => {
            const raw = ZenggeProtocol.oracleSceneDelete(sceneSlot);
            transmit(ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]), `0x56 DELETE slot=${sceneSlot}`, '0x56');
          }}
        >
          <Text style={{ color: '#FF4040', fontWeight: '900', fontSize: 12 }}>0x56 DELETE slot {sceneSlot}</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}
