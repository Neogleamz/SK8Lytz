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
import { ProtocolBuilderContext } from '../../../../hooks/useProtocolBuilder';
import { DiagnosticDevice, DiagnosticHwSettings } from './DiagnosticLabTypes';
import { Builder51Mode } from './builder/Builder51Mode';
import { Builder59Mode } from './builder/Builder59Mode';
import { Builder61Mode } from './builder/Builder61Mode';
import { Builder73Mode } from './builder/Builder73Mode';
import { Builder62Mode } from './builder/Builder62Mode';

interface BuilderTabProps {
  targetDeviceId: string | null;
  connectedDevices: DiagnosticDevice[];
  hwSettings: DiagnosticHwSettings | undefined;
  hwPts: number;
  builderCtx: ProtocolBuilderContext;
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

  const { bldProtocol, bldResult } = builderCtx;

  // Manual override for edited hex
  const [bldHexOverride, setBldHexOverride] = useState('');

  // Sync override with result whenever result changes (unless manually edited)
  useEffect(() => {
    if (bldResult?.hex) setBldHexOverride(bldResult.hex);
  }, [bldResult]);

  const sendSolid = (r: number, g: number, b: number, pts: number, trans: number, note: string) => {
    const safePts = Math.max(12, pts);
    const pixels = Array(safePts).fill({ r, g, b });
    const payload = ZenggeProtocol.setMultiColor(pixels, safePts, 1, 1, trans);
    transmit(payload, note);
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
             onPress={() => builderCtx.setBldProtocol(p.id as Parameters<typeof builderCtx.setBldProtocol>[0])}>
             <Text style={{ color: bldProtocol === p.id ? cyan : txtMuted, fontWeight: '900', fontSize: 11 }}>{p.label}</Text>
           </TouchableOpacity>
        ))}
      </View>

      {bldProtocol === '0x51' && <Builder51Mode builderCtx={builderCtx} transmit={transmit} />}
      {bldProtocol === '0x59' && <Builder59Mode builderCtx={builderCtx} hwPts={hwPts} />}
      {bldProtocol === '0x61' && <Builder61Mode builderCtx={builderCtx} />}
      {bldProtocol === '0x73' && <Builder73Mode builderCtx={builderCtx} transmit={transmit} />}
      {bldProtocol === '0x62' && <Builder62Mode builderCtx={builderCtx} />}

      <Text style={[S.subTitle, { color: txtMuted }]}>GENERATED PAYLOAD — BYTE ANNOTATIONS</Text>
      {bldResult && (
        <View style={[S.diagBox, { backgroundColor: isDark ? '#05070a' : '#f9fafb', borderColor: border, padding: Spacing.md }]}>
          {bldResult.annotations.map((a: string, i: number) => (
            <Text key={i} style={{ color: i === 4 ? cyan : txtPri, fontSize: 11, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', marginBottom: Spacing.xxs }}>
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
          onPress={() => sendRawHex(bldHexOverride, `Builder Custom Payload`)}>
          <Text style={{ color: '#000', fontWeight: '900', fontSize: 12 }}>TX PAYLOAD</Text>
        </TouchableOpacity>
        <TouchableOpacity style={[S.txBtn, { flex: 1, backgroundColor: border, borderColor: border }]}
          onPress={() => bldResult && setBldHexOverride(bldResult.hex)}>
          <Text style={{ color: txtPri, fontWeight: '900', fontSize: 12 }}>RESET HEX</Text>
        </TouchableOpacity>
      </View>

      {/* 0x41 BLE LAB SPIKE */}
      <Text style={[S.subTitle, { color: '#FF9500', marginTop: Spacing.xl }]}>0x41 BLE LAB SPIKE (HARDWARE SCALING)</Text>
      <View style={{ gap: Spacing.sm, marginBottom: Spacing.xl }}>
        <TouchableOpacity style={[S.presetBtn, { backgroundColor: '#FF950022', borderColor: '#FF9500' }]}
          onPress={() => {
            transmit(ZenggeProtocol.setSettledMode(1, {r:255,g:0,b:0}, {r:0,g:0,b:255}, 50, 0), 'Spike Test 1: ID 1 (50% Size Check)', '0x41');
          }}>
          <Text style={{ color: '#FF9500', fontSize: 12, fontWeight: '900' }}>TEST 1: ID 201 (Size Check)</Text>
          <Text style={{ color: txtMuted, fontSize: 10, marginTop: Spacing.xxs }}>Should draw 50% Red chunk on Blue bg. Does it scale to HALOZ (4) vs SOULZ (8)?</Text>
        </TouchableOpacity>
        
        <TouchableOpacity style={[S.presetBtn, { backgroundColor: '#FF950022', borderColor: '#FF9500' }]}
          onPress={() => {
            transmit(ZenggeProtocol.setSettledMode(24, {r:255,g:0,b:0}, {r:0,g:0,b:255}, 50, 0), 'Spike Test 2: ID 24 (Stacker Limit)', '0x41');
          }}>
          <Text style={{ color: '#FF9500', fontSize: 12, fontWeight: '900' }}>TEST 2: ID 224 (Stacker Limit Check)</Text>
          <Text style={{ color: txtMuted, fontSize: 10, marginTop: Spacing.xxs }}>Let it run. Does the stack stop at 8 LEDs even on the 16-LED SOULZ strip?</Text>
        </TouchableOpacity>

        <TouchableOpacity style={[S.presetBtn, { backgroundColor: '#FF950022', borderColor: '#FF9500' }]}
          onPress={() => {
            transmit(ZenggeProtocol.setSettledMode(31, {r:255,g:0,b:0}, {r:0,g:0,b:255}, 50, 0), 'Spike Test 3: ID 31 (Matrix Precision)', '0x41');
          }}>
          <Text style={{ color: '#FF9500', fontSize: 12, fontWeight: '900' }}>TEST 3: ID 231 (Pattern Matrix Check)</Text>
          <Text style={{ color: txtMuted, fontSize: 10, marginTop: Spacing.xxs }}>Observe the marquee sequence. Does it exactly match [Red, Blue, Black, Blue, Red] (5px)?</Text>
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
                const pts = hwPts || 16;
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
