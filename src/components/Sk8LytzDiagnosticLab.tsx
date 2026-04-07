/**
 * Sk8LytzDiagnosticLab.tsx — SK8Lytz LED Protocol Diagnostic Lab
 *
 * Purpose-built to diagnose the three known protocol issues:
 *   1. Color order — is R↔G swapped? (RGB vs GRB hardware behavior)
 *   2. Transition types — what does 0x00/0x01/0x02/0x03 actually DO on device?
 *   3. LED count — are all LEDs addressed? (numPoints correct?)
 *
 * Tab layout:
 *   COLOR TEST  — Send isolated known colors, observe hardware behavior
 *   TRANSITION  — Fire each transitionType byte, observe what happens
 *   BUILDER     — Full control over every 0x59 byte, live hex decode
 *   SNIFFER     — Live BLE RX/TX timeline with parse annotations
 *
 * All payloads shown with full byte-level annotations so we can diagnose
 * exactly what the hardware is receiving.
 */

import React, { useState, useEffect, useCallback } from 'react';
import {
  View, Text, StyleSheet, Modal, TouchableOpacity,
  ScrollView, SafeAreaView, TextInput, FlatList, Platform,
} from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { ZenggeProtocol, IC_TYPES, COLOR_SORTING_RGB } from '../protocols/ZenggeProtocol';

// ─── Types ────────────────────────────────────────────────────────────────────

interface LabProps {
  visible: boolean;
  onClose: () => void;
  writeToDevice?: (data: number[], deviceId?: string) => Promise<void>;
  liveRxPayload?: { deviceId: string; payloadHex: string; timestamp?: number } | null;
  connectedDevices?: { id: string; name: string | null }[];
  hwSettings?: {
    ledPoints?: number;
    colorSorting?: number;
    colorSortingName?: string;
    icName?: string;
    segments?: number;
    detected?: boolean;
  };
}

type LabTab = 'COLOR' | 'TRANSITION' | 'BUILDER' | 'SNIFFER';

interface BleLog {
  dir: 'TX' | 'RX';
  hex: string;
  t: number;
  dev?: string;
  note?: string;
}

// ─── Transition type reference (confirmed on device) ─────────────────────────
const TRANSITION_TYPES = [
  { byte: 0x00, label: 'CASCADE', color: '#FF9500', desc: 'Hardware scrolls/loops array around strip (Running Water)' },
  { byte: 0x01, label: 'FREEZE',  color: '#00CC88', desc: 'Hardware locks array in place — required for solid/static output' },
  { byte: 0x02, label: 'STROBE',  color: '#FF4040', desc: 'Hardware strobes entire array on/off rapidly' },
  { byte: 0x03, label: 'UNKNOWN', color: '#888888', desc: 'Unknown — may behave like CASCADE or be undefined' },
];

// ─── Helper: build annotated 0x59 payload manually ───────────────────────────
function build0x59(
  pixels: {r:number;g:number;b:number}[],
  transitionType: number,
  speed: number,
  direction: number
): { raw: number[]; wrapped: number[]; hex: string; annotations: string[] } {
  const numPoints = pixels.length;
  const totalLen = numPoints * 3 + 9;

  const raw = new Array(totalLen).fill(0);
  raw[0] = 0x59;
  raw[1] = (totalLen >> 8) & 0xFF;
  raw[2] = totalLen & 0xFF;
  let idx = 3;
  for (const p of pixels) {
    raw[idx++] = Math.max(0, Math.min(255, p.r | 0));
    raw[idx++] = Math.max(0, Math.min(255, p.g | 0));
    raw[idx++] = Math.max(0, Math.min(255, p.b | 0));
  }
  raw[idx++] = (numPoints >> 8) & 0xFF;
  raw[idx++] = numPoints & 0xFF;
  raw[idx++] = transitionType & 0xFF;
  raw[idx++] = Math.max(1, Math.min(255, speed | 0));
  raw[idx++] = direction & 0xFF;
  raw[idx] = ZenggeProtocol.calculateChecksum(raw.slice(0, totalLen - 1));

  const wrapped = ZenggeProtocol.wrapCommand(raw);
  const hex = wrapped.map(b => b.toString(16).toUpperCase().padStart(2, '0')).join(' ');

  const annotations = [
    `[0x59] Opcode`,
    `[${raw[1].toString(16).toUpperCase().padStart(2,'0')} ${raw[2].toString(16).toUpperCase().padStart(2,'0')}] totalLen=${totalLen}`,
    `[... ${numPoints * 3} pixel bytes (${numPoints} LEDs)]`,
    `[${raw[totalLen-6].toString(16).toUpperCase().padStart(2,'0')} ${raw[totalLen-5].toString(16).toUpperCase().padStart(2,'0')}] numPoints=${numPoints}`,
    `[${raw[totalLen-4].toString(16).toUpperCase().padStart(2,'0')}] transitionType=${transitionType} (${TRANSITION_TYPES.find(t=>t.byte===transitionType)?.label ?? 'UNKNOWN'})`,
    `[${raw[totalLen-3].toString(16).toUpperCase().padStart(2,'0')}] speed=${speed}`,
    `[${raw[totalLen-2].toString(16).toUpperCase().padStart(2,'0')}] direction=${direction}`,
    `[${raw[totalLen-1].toString(16).toUpperCase().padStart(2,'0')}] checksum`,
  ];

  return { raw, wrapped, hex, annotations };
}

// ─── Main Component ───────────────────────────────────────────────────────────

export default function Sk8LytzDiagnosticLab({
  visible, onClose, writeToDevice, liveRxPayload,
  connectedDevices = [], hwSettings,
}: LabProps) {

  const [tab, setTab] = useState<LabTab>('COLOR');
  const [logs, setLogs] = useState<BleLog[]>([]);
  const [lastSent, setLastSent] = useState<string>('');
  const [lastNote, setLastNote] = useState<string>('');

  // Builder state
  const [bldR, setBldR] = useState(255);
  const [bldG, setBldG] = useState(0);
  const [bldB, setBldB] = useState(0);
  const [bldTrans, setBldTrans] = useState(0x01);
  const [bldSpeed, setBldSpeed] = useState(16);
  const [bldPoints, setBldPoints] = useState('16');
  const [bldDir, setBldDir] = useState(1);
  const [bldHexOverride, setBldHexOverride] = useState('');
  const [bldResult, setBldResult] = useState<ReturnType<typeof build0x59> | null>(null);

  // Rebuild payload when builder params change
  useEffect(() => {
    const pts = Math.max(1, Math.min(300, parseInt(bldPoints) || 16));
    const pixels = Array(pts).fill({ r: bldR, g: bldG, b: bldB });
    const result = build0x59(pixels, bldTrans, bldSpeed, bldDir);
    setBldResult(result);
    setBldHexOverride(result.hex);
  }, [bldR, bldG, bldB, bldTrans, bldSpeed, bldPoints, bldDir]);

  // RX listener
  useEffect(() => {
    if (!visible || !liveRxPayload?.payloadHex) return;
    const entry: BleLog = {
      dir: 'RX',
      hex: liveRxPayload.payloadHex,
      t: liveRxPayload.timestamp || Date.now(),
      dev: liveRxPayload.deviceId,
    };
    const bytes = liveRxPayload.payloadHex.split(' ').map(h => parseInt(h, 16));
    const hw63 = ZenggeProtocol.parseHardwareSettingsResponse(bytes);
    if (hw63) {
      entry.note = `0x63 → LEDs:${hw63.ledPoints} ${hw63.icName} ${hw63.colorSortingName} sort:${hw63.colorSorting}`;
    }
    setLogs(prev => [entry, ...prev].slice(0, 200));
  }, [liveRxPayload, visible]);

  const transmit = useCallback(async (bytes: number[], note?: string) => {
    if (!writeToDevice) return;
    await writeToDevice(bytes).catch(console.warn);
    const hexStr = bytes.map(b => b.toString(16).toUpperCase().padStart(2, '0')).join(' ');
    setLastSent(hexStr);
    setLastNote(note || '');
    setLogs(prev => [{ dir: 'TX', hex: hexStr, t: Date.now(), note }, ...prev].slice(0, 200));
  }, [writeToDevice]);

  const sendRawHex = useCallback(async (hexStr: string, note?: string) => {
    const bytes = hexStr.replace(/[^0-9A-Fa-f]/g, '').match(/.{1,2}/g)?.map(h => parseInt(h, 16)) || [];
    if (bytes.length === 0) return;
    await transmit(bytes, note);
  }, [transmit]);

  // ─── Solid color test helpers ───────────────────────────────────────────────
  const sendSolid = (r: number, g: number, b: number, pts: number, trans: number, note: string) => {
    const pixels = Array(pts).fill({ r, g, b });
    const { wrapped } = build0x59(pixels, trans, 1, 1);
    transmit(wrapped, note);
  };

  const hwPts = hwSettings?.ledPoints || 16;

  // ─── Render helpers ─────────────────────────────────────────────────────────

  const MonoText = ({ children, color = '#00f0ff' }: {children:string; color?:string}) => (
    <Text style={{ color, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', fontSize: 11 }}>
      {children}
    </Text>
  );

  const renderHwBadge = () => (
    <View style={S.hwBadge}>
      <Text style={S.hwBadgeLabel}>HW PROBE: </Text>
      {hwSettings?.detected ? (
        <>
          <Text style={[S.hwBadgeVal, { color: '#00CC88' }]}>{hwSettings.ledPoints ?? '?'} LEDs</Text>
          <Text style={S.hwBadgeLabel}> · </Text>
          <Text style={[S.hwBadgeVal, { color: '#FF9500' }]}>{hwSettings.colorSortingName ?? '?'}</Text>
          <Text style={S.hwBadgeLabel}> · </Text>
          <Text style={[S.hwBadgeVal, { color: '#c084fc' }]}>{hwSettings.icName ?? '?'}</Text>
        </>
      ) : (
        <Text style={[S.hwBadgeVal, { color: '#FF4040' }]}>NOT DETECTED — connect + wait for 0x63</Text>
      )}
    </View>
  );

  // ─── COLOR TEST TAB ─────────────────────────────────────────────────────────
  const renderColorTab = () => (
    <ScrollView contentContainerStyle={{ paddingBottom: 40 }}>
      <Text style={S.sectionTitle}>COLOR ORDER TEST</Text>
      <Text style={S.hint}>
        Tap each button — observe what color the hardware actually shows.{'\n'}
        This identifies whether hardware auto-remaps GRB or needs pre-swapped bytes.
      </Text>

      {renderHwBadge()}

      <Text style={S.subTitle}>PRIMARY COLORS (Pure RGB bytes)</Text>
      <View style={S.colorBtnRow}>
        {[
          { label: 'SEND RED\n[255,0,0]',   r:255, g:0,   b:0,   bg:'#7A1010', border:'#FF4040', note:'Pure RED [255,0,0] — should show RED if hardware remaps' },
          { label: 'SEND GREEN\n[0,255,0]', r:0,   g:255, b:0,   bg:'#0A4A0A', border:'#00CC00', note:'Pure GREEN [0,255,0]' },
          { label: 'SEND BLUE\n[0,0,255]',  r:0,   g:0,   b:255, bg:'#101070', border:'#4040FF', note:'Pure BLUE [0,0,255]' },
        ].map(btn => (
          <TouchableOpacity key={btn.label} style={[S.bigColorBtn, { backgroundColor: btn.bg, borderColor: btn.border }]}
            onPress={() => sendSolid(btn.r, btn.g, btn.b, hwPts, 0x01, btn.note)}>
            <Text style={{ color: btn.border, fontWeight: 'bold', textAlign: 'center', fontSize: 12 }}>{btn.label}</Text>
          </TouchableOpacity>
        ))}
      </View>

      <Text style={S.subTitle}>DIAGNOSIS GUIDE</Text>
      <View style={S.diagBox}>
        <Text style={S.diagLine}>🟢 If RED btn → RED light: hardware auto-remaps. Send pure RGB. ✅</Text>
        <Text style={S.diagLine}>🔴 If RED btn → GREEN light: hardware is GRB-wired, no auto-remap.</Text>
        <Text style={S.diagLine}>   → Must pre-swap in software (applyColorSorting GRB=2)</Text>
        <Text style={S.diagLine}>⚠️  If NOTHING lights: LED count wrong or payload format wrong.</Text>
      </View>

      <Text style={S.subTitle}>GRB PRE-SWAPPED VERSION</Text>
      <Text style={S.hint}>If pure RGB shows wrong colors, try these (bytes pre-swapped for GRB):</Text>
      <View style={S.colorBtnRow}>
        {[
          { label: 'GRB RED\n[0,255,0]',   r:0,   g:255, b:0,   bg:'#7A1010', border:'#FF4040', note:'GRB-swapped RED — send [G=0,R=255,B=0] as [0,255,0]' },
          { label: 'GRB GREEN\n[255,0,0]', r:255, g:0,   b:0,   bg:'#0A4A0A', border:'#00CC00', note:'GRB-swapped GREEN [255,0,0]' },
          { label: 'GRB BLUE\n[0,0,255]',  r:0,   g:0,   b:255, bg:'#101070', border:'#4040FF', note:'GRB Blue same [0,0,255]' },
        ].map(btn => (
          <TouchableOpacity key={btn.label} style={[S.bigColorBtn, { backgroundColor: btn.bg, borderColor: btn.border }]}
            onPress={() => sendSolid(btn.r, btn.g, btn.b, hwPts, 0x01, btn.note)}>
            <Text style={{ color: btn.border, fontWeight: 'bold', textAlign: 'center', fontSize: 12 }}>{btn.label}</Text>
          </TouchableOpacity>
        ))}
      </View>

      <Text style={S.subTitle}>LAST SENT</Text>
      {lastSent ? (
        <View style={S.sentBox}>
          <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>{lastNote}</Text>
          <MonoText>{lastSent}</MonoText>
        </View>
      ) : <Text style={{ color: '#555', fontSize: 12 }}>Nothing sent yet.</Text>}
    </ScrollView>
  );

  // ─── TRANSITION TYPE TAB ────────────────────────────────────────────────────
  const renderTransitionTab = () => (
    <ScrollView contentContainerStyle={{ paddingBottom: 40 }}>
      <Text style={S.sectionTitle}>TRANSITION TYPE PROBE</Text>
      <Text style={S.hint}>
        Each button sends the same color with a different transitionType byte.{'\n'}
        Observe what the hardware actually does — we need to confirm 0x00 vs 0x01 behavior.
      </Text>
      {renderHwBadge()}

      <Text style={S.subTitle}>COLOR TO SEND (change in BUILDER tab first)</Text>
      <View style={{ flexDirection: 'row', gap: 12, marginBottom: 16, alignItems: 'center' }}>
        <View style={{ width: 40, height: 40, backgroundColor: `rgb(${bldR},${bldG},${bldB})`, borderRadius: 8, borderWidth: 1, borderColor: '#444' }} />
        <Text style={{ color: '#aaa', fontSize: 12 }}>R:{bldR} G:{bldG} B:{bldB} · {hwPts} LEDs · Speed:{bldSpeed}</Text>
      </View>

      {TRANSITION_TYPES.map(tt => (
        <TouchableOpacity key={tt.byte}
          style={[S.transBtn, { borderColor: tt.color }]}
          onPress={() => {
            const pixels = Array(hwPts).fill({ r: bldR, g: bldG, b: bldB });
            const { wrapped } = build0x59(pixels, tt.byte, bldSpeed, 1);
            transmit(wrapped, `transitionType=0x0${tt.byte.toString(16).toUpperCase()} ${tt.label}`);
          }}>
          <View style={[S.transByteBadge, { backgroundColor: tt.color + '33', borderColor: tt.color }]}>
            <Text style={{ color: tt.color, fontWeight: 'bold', fontSize: 14 }}>0x0{tt.byte.toString(16).toUpperCase()}</Text>
          </View>
          <View style={{ flex: 1, marginLeft: 12 }}>
            <Text style={{ color: tt.color, fontWeight: 'bold', fontSize: 13 }}>{tt.label}</Text>
            <Text style={{ color: '#888', fontSize: 11, marginTop: 2 }}>{tt.desc}</Text>
          </View>
          <MaterialCommunityIcons name="send" size={18} color={tt.color} />
        </TouchableOpacity>
      ))}

      <View style={S.diagBox}>
        <Text style={S.diagLine}>📝 WHAT WE EXPECT (from Master Reference):</Text>
        <Text style={S.diagLine}>0x00 = CASCADE — LEDs should scroll/chase</Text>
        <Text style={S.diagLine}>0x01 = FREEZE — LEDs should sit perfectly still (SOLID)</Text>
        <Text style={S.diagLine}>0x02 = STROBE — LEDs should flash on/off</Text>
        <Text style={S.diagLine}>0x03 = UNKNOWN — document what this actually does</Text>
        <Text style={[S.diagLine, { color: '#FF9500', marginTop: 8 }]}>👉 If SOLID looks wrong, it's 0x00 vs 0x01 confusion.</Text>
      </View>

      <Text style={S.subTitle}>LAST SENT</Text>
      {lastSent ? (
        <View style={S.sentBox}>
          <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>{lastNote}</Text>
          <MonoText>{lastSent}</MonoText>
        </View>
      ) : <Text style={{ color: '#555', fontSize: 12 }}>Nothing sent yet.</Text>}
    </ScrollView>
  );

  // ─── BUILDER TAB ────────────────────────────────────────────────────────────
  const renderBuilderTab = () => (
    <ScrollView contentContainerStyle={{ paddingBottom: 40 }}>
      <Text style={S.sectionTitle}>0x59 PAYLOAD BUILDER</Text>
      <Text style={S.hint}>Full byte-level control. Every field shown with its role in the packet.</Text>
      {renderHwBadge()}

      <Text style={S.subTitle}>PIXEL COLOR (all LEDs same color)</Text>
      {[
        { label: 'R', val: bldR, set: setBldR, color: '#FF4040' },
        { label: 'G', val: bldG, set: setBldG, color: '#00CC44' },
        { label: 'B', val: bldB, set: setBldB, color: '#4488FF' },
      ].map(ch => (
        <View key={ch.label} style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 8 }}>
          <Text style={{ color: ch.color, fontWeight: 'bold', width: 24, fontSize: 13 }}>{ch.label}</Text>
          <TextInput
            style={[S.numInput, { borderColor: ch.color + '66' }]}
            value={ch.val.toString()}
            keyboardType="numeric"
            onChangeText={v => { const n = Math.max(0, Math.min(255, parseInt(v)||0)); ch.set(n); }}
          />
          <View style={{ flex: 1, marginLeft: 12 }}>
            <View style={{ height: 6, backgroundColor: '#222', borderRadius: 3 }}>
              <View style={{ height: 6, width: `${(ch.val/255)*100}%`, backgroundColor: ch.color, borderRadius: 3 }} />
            </View>
          </View>
          <View style={{ width: 20, height: 20, backgroundColor: ch.label === 'R' ? `rgb(${ch.val},0,0)` : ch.label === 'G' ? `rgb(0,${ch.val},0)` : `rgb(0,0,${ch.val})`, borderRadius: 4, marginLeft: 8 }} />
        </View>
      ))}
      <View style={{ width: 40, height: 40, backgroundColor: `rgb(${bldR},${bldG},${bldB})`, borderRadius: 8, marginBottom: 16, borderWidth: 1, borderColor: '#444' }} />

      <Text style={S.subTitle}>TRANSITION TYPE</Text>
      <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 8, marginBottom: 16 }}>
        {TRANSITION_TYPES.map(tt => (
          <TouchableOpacity key={tt.byte}
            style={[S.chip, bldTrans === tt.byte && { backgroundColor: tt.color + '33', borderColor: tt.color }]}
            onPress={() => setBldTrans(tt.byte)}>
            <Text style={{ color: bldTrans === tt.byte ? tt.color : '#888', fontWeight: 'bold', fontSize: 11 }}>
              0x0{tt.byte.toString(16).toUpperCase()} {tt.label}
            </Text>
          </TouchableOpacity>
        ))}
      </View>

      <Text style={S.subTitle}>PARAMETERS</Text>
      <View style={{ flexDirection: 'row', gap: 12, marginBottom: 16 }}>
        <View style={{ flex: 1 }}>
          <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>NUM POINTS (LED count)</Text>
          <TextInput style={S.numInput} value={bldPoints} keyboardType="numeric"
            onChangeText={v => setBldPoints(v)} placeholder={hwPts.toString()} placeholderTextColor="#444" />
          <Text style={{ color: '#555', fontSize: 10, marginTop: 2 }}>HW probe: {hwPts}</Text>
        </View>
        <View style={{ flex: 1 }}>
          <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>SPEED (1-255)</Text>
          <TextInput style={S.numInput} value={bldSpeed.toString()} keyboardType="numeric"
            onChangeText={v => setBldSpeed(Math.max(1, Math.min(255, parseInt(v)||1)))} />
        </View>
        <View style={{ flex: 1 }}>
          <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>DIRECTION</Text>
          <View style={{ flexDirection: 'row', gap: 4 }}>
            {[0, 1].map(d => (
              <TouchableOpacity key={d} style={[S.chip, bldDir === d && S.chipActive]} onPress={() => setBldDir(d)}>
                <Text style={{ color: bldDir === d ? '#00f0ff' : '#888', fontSize: 11 }}>{d === 0 ? 'REV' : 'FWD'}</Text>
              </TouchableOpacity>
            ))}
          </View>
        </View>
      </View>

      <Text style={S.subTitle}>GENERATED PAYLOAD — BYTE ANNOTATIONS</Text>
      {bldResult && (
        <View style={S.annotationsBox}>
          {bldResult.annotations.map((a, i) => (
            <Text key={i} style={{ color: i === 4 ? TRANSITION_TYPES.find(t=>t.byte===bldTrans)?.color ?? '#aaa' : '#aaa', fontSize: 11, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', marginBottom: 2 }}>
              {a}
            </Text>
          ))}
        </View>
      )}

      <Text style={S.subTitle}>FULL HEX (EDITABLE)</Text>
      <TextInput
        style={[S.hexInput]}
        value={bldHexOverride}
        onChangeText={setBldHexOverride}
        multiline
        placeholder="Hex bytes..."
        placeholderTextColor="#444"
      />

      <View style={{ flexDirection: 'row', gap: 8, marginTop: 12 }}>
        <TouchableOpacity style={[S.txBtn, { flex: 1 }]}
          onPress={() => sendRawHex(bldHexOverride, `Builder: trans=0x0${bldTrans.toString(16).toUpperCase()} pts=${bldPoints}`)}>
          <Text style={{ color: '#000', fontWeight: 'bold' }}>TX PAYLOAD</Text>
        </TouchableOpacity>
        <TouchableOpacity style={[S.txBtn, { flex: 1, backgroundColor: '#333' }]}
          onPress={() => bldResult && setBldHexOverride(bldResult.hex)}>
          <Text style={{ color: '#aaa', fontWeight: 'bold' }}>RESET HEX</Text>
        </TouchableOpacity>
      </View>

      {/* Quick presets */}
      <Text style={S.subTitle}>QUICK PRESETS</Text>
      <View style={{ gap: 8 }}>
        {[
          { label: 'FULL RED — FREEZE', r:255,g:0,b:0, trans:0x01, note:'Pure red, FREEZE — should be solid red' },
          { label: 'FULL GREEN — FREEZE', r:0,g:255,b:0, trans:0x01, note:'Pure green, FREEZE — should be solid green' },
          { label: 'FULL RED — CASCADE', r:255,g:0,b:0, trans:0x00, note:'Pure red, CASCADE — should scroll/chase' },
          { label: 'FULL WHITE — FREEZE', r:255,g:255,b:255, trans:0x01, note:'Full white FREEZE — all LEDs max brightness' },
          { label: 'POLL 0x63', note:'Query hardware config', isCmd: true, cmd: ZenggeProtocol.queryHardwareSettings?.() },
        ].map((preset, i) => (
          <TouchableOpacity key={i} style={[S.presetBtn]}
            onPress={() => {
              if ('isCmd' in preset && preset.isCmd && preset.cmd) {
                transmit(ZenggeProtocol.wrapCommand(preset.cmd), preset.note);
              } else if ('r' in preset) {
                const pts = parseInt(bldPoints) || hwPts;
                const { wrapped } = build0x59(Array(pts).fill({ r: preset.r, g: preset.g, b: preset.b }), preset.trans, bldSpeed, 1);
                transmit(wrapped, preset.note);
              }
            }}>
            <Text style={{ color: '#00f0ff', fontSize: 12, fontWeight: 'bold' }}>{preset.label}</Text>
            <Text style={{ color: '#555', fontSize: 10 }}>{preset.note}</Text>
          </TouchableOpacity>
        ))}
      </View>
    </ScrollView>
  );

  // ─── SNIFFER TAB ────────────────────────────────────────────────────────────
  const renderSnifferTab = () => (
    <View style={{ flex: 1 }}>
      <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 }}>
        <Text style={S.sectionTitle}>BLE TRACE</Text>
        <TouchableOpacity style={S.chip} onPress={() => setLogs([])}>
          <Text style={{ color: '#FF4040', fontSize: 10, fontWeight: 'bold' }}>CLEAR</Text>
        </TouchableOpacity>
      </View>
      <FlatList
        data={logs}
        keyExtractor={(_, i) => i.toString()}
        style={{ flex: 1, backgroundColor: '#000', borderRadius: 8, padding: 8, borderWidth: 1, borderColor: '#222' }}
        renderItem={({ item }) => (
          <View style={{ marginBottom: 8, paddingBottom: 8, borderBottomWidth: 1, borderBottomColor: '#111' }}>
            <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8, marginBottom: 2 }}>
              <View style={{ backgroundColor: item.dir === 'TX' ? '#FF404033' : '#007AFF33', borderWidth: 1, borderColor: item.dir === 'TX' ? '#FF4040' : '#007AFF', paddingHorizontal: 6, paddingVertical: 1, borderRadius: 4 }}>
                <Text style={{ color: item.dir === 'TX' ? '#FF4040' : '#00f0ff', fontSize: 9, fontWeight: 'bold' }}>{item.dir}</Text>
              </View>
              <Text style={{ color: '#444', fontSize: 9 }}>{new Date(item.t).toLocaleTimeString()}</Text>
              {item.dev && <Text style={{ color: '#444', fontSize: 9 }}>{item.dev.slice(-6)}</Text>}
            </View>
            {item.note && <Text style={{ color: '#FF9500', fontSize: 10, marginBottom: 2 }}>{item.note}</Text>}
            <Text style={{ color: '#666', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', fontSize: 10 }} numberOfLines={2}>
              {item.hex}
            </Text>
          </View>
        )}
        ListEmptyComponent={<Text style={{ color: '#444', padding: 16 }}>No BLE traffic yet. Connect a device and use the other tabs.</Text>}
      />
    </View>
  );

  // ─── Root render ─────────────────────────────────────────────────────────────
  return (
    <Modal visible={visible} animationType="slide" presentationStyle="fullScreen" onRequestClose={onClose}>
      <SafeAreaView style={S.root}>
        {/* Header */}
        <View style={S.header}>
          <TouchableOpacity onPress={onClose} style={S.closeBtn}>
            <MaterialCommunityIcons name="close" size={20} color="#FFF" />
          </TouchableOpacity>
          <View style={{ flex: 1, alignItems: 'center' }}>
            <Text style={S.title}>LED DIAGNOSTIC LAB</Text>
            <Text style={{ color: '#555', fontSize: 9 }}>
              {connectedDevices.length > 0 ? connectedDevices.map(d => d.name || d.id.slice(-5)).join(', ') : 'NO DEVICE'}
            </Text>
          </View>
          <View style={S.closeBtn} />
        </View>

        {/* Tabs */}
        <View style={S.tabBar}>
          {(['COLOR', 'TRANSITION', 'BUILDER', 'SNIFFER'] as LabTab[]).map(t => (
            <TouchableOpacity key={t} style={[S.tabBtn, tab === t && S.tabBtnActive]} onPress={() => setTab(t)}>
              <Text style={[S.tabBtnTxt, tab === t && S.tabBtnTxtActive]}>{t}</Text>
            </TouchableOpacity>
          ))}
        </View>

        {/* Content */}
        <View style={S.content}>
          {tab === 'COLOR'      && renderColorTab()}
          {tab === 'TRANSITION' && renderTransitionTab()}
          {tab === 'BUILDER'    && renderBuilderTab()}
          {tab === 'SNIFFER'    && renderSnifferTab()}
        </View>
      </SafeAreaView>
    </Modal>
  );
}

// ─── Styles ───────────────────────────────────────────────────────────────────

const S = StyleSheet.create({
  root: { flex: 1, backgroundColor: '#080808' },
  header: { flexDirection: 'row', alignItems: 'center', padding: 14, borderBottomWidth: 1, borderBottomColor: '#1A1A1A' },
  closeBtn: { width: 36, height: 36, justifyContent: 'center', alignItems: 'center', backgroundColor: '#1A1A1A', borderRadius: 18 },
  title: { color: '#FFF', fontSize: 13, fontWeight: '900', letterSpacing: 1.5 },
  tabBar: { flexDirection: 'row', borderBottomWidth: 1, borderBottomColor: '#1A1A1A' },
  tabBtn: { flex: 1, paddingVertical: 12, alignItems: 'center', borderBottomWidth: 2, borderBottomColor: 'transparent' },
  tabBtnActive: { borderBottomColor: '#00f0ff' },
  tabBtnTxt: { color: '#555', fontSize: 10, fontWeight: 'bold', letterSpacing: 0.5 },
  tabBtnTxtActive: { color: '#00f0ff' },
  content: { flex: 1, padding: 16 },
  sectionTitle: { color: '#FFF', fontSize: 12, fontWeight: '900', letterSpacing: 1, marginBottom: 6 },
  subTitle: { color: '#666', fontSize: 10, fontWeight: 'bold', letterSpacing: 1, marginBottom: 8, marginTop: 16 },
  hint: { color: '#555', fontSize: 11, lineHeight: 16, marginBottom: 12 },
  hwBadge: { flexDirection: 'row', flexWrap: 'wrap', backgroundColor: '#111', padding: 10, borderRadius: 8, marginBottom: 16, borderWidth: 1, borderColor: '#222', alignItems: 'center' },
  hwBadgeLabel: { color: '#555', fontSize: 11 },
  hwBadgeVal: { fontSize: 11, fontWeight: 'bold' },
  colorBtnRow: { flexDirection: 'row', gap: 8, marginBottom: 16 },
  bigColorBtn: { flex: 1, paddingVertical: 20, alignItems: 'center', justifyContent: 'center', borderRadius: 12, borderWidth: 2 },
  diagBox: { backgroundColor: '#0D1A0D', borderWidth: 1, borderColor: '#1A3A1A', borderRadius: 8, padding: 12, marginBottom: 8 },
  diagLine: { color: '#aaa', fontSize: 11, lineHeight: 18 },
  sentBox: { backgroundColor: '#000', borderWidth: 1, borderColor: '#222', borderRadius: 8, padding: 12 },
  transBtn: { flexDirection: 'row', alignItems: 'center', backgroundColor: '#111', borderWidth: 1, borderRadius: 12, padding: 14, marginBottom: 10 },
  transByteBadge: { width: 48, height: 48, alignItems: 'center', justifyContent: 'center', borderRadius: 8, borderWidth: 1 },
  annotationsBox: { backgroundColor: '#000', borderWidth: 1, borderColor: '#222', borderRadius: 8, padding: 12, marginBottom: 8 },
  numInput: { backgroundColor: '#111', color: '#FFF', borderWidth: 1, borderColor: '#333', borderRadius: 8, padding: 8, fontSize: 13, textAlign: 'center' },
  hexInput: { backgroundColor: '#000', color: '#00f0ff', borderWidth: 1, borderColor: '#333', borderRadius: 8, padding: 12, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', fontSize: 11, minHeight: 80 },
  chip: { paddingHorizontal: 12, paddingVertical: 6, borderRadius: 16, backgroundColor: '#1A1A1A', borderWidth: 1, borderColor: '#333' },
  chipActive: { backgroundColor: 'rgba(0,240,255,0.1)', borderColor: '#00f0ff' },
  txBtn: { backgroundColor: '#00ccff', justifyContent: 'center', alignItems: 'center', paddingVertical: 14, borderRadius: 10 },
  presetBtn: { backgroundColor: '#111', borderWidth: 1, borderColor: '#222', borderRadius: 8, padding: 12 },
});
