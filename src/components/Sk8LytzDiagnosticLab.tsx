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
import { useSafeAreaInsets } from 'react-native-safe-area-context';
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
  allDevices?: any[];
  isScanning?: boolean;
  handleScan?: () => void;
  connectToDevice?: (device: any) => Promise<any>;
  liveDeviceConfigs?: Record<string, any>;
}

type LabTab = 'DEVICES' | 'COLOR' | 'TRANSITION' | 'BUILDER' | 'SNIFFER';

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
  allDevices = [], isScanning = false, handleScan,
  connectToDevice, liveDeviceConfigs = {},
}: LabProps) {

  const [tab, setTab] = useState<LabTab>('DEVICES');
  const [logs, setLogs] = useState<BleLog[]>([]);
  const [lastSent, setLastSent] = useState<string>('');
  const [lastNote, setLastNote] = useState<string>('');

  // Target device tracking
  const [targetDeviceId, setTargetDeviceId] = useState<string | null>(null);
  const insets = useSafeAreaInsets();
  
  // Builder general
  const [bldProtocol, setBldProtocol] = useState<'0x59' | '0x61' | '0x73' | '0x62'>('0x59');
  
  // Builder 0x59
  const [bldColors, setBldColors] = useState([{r:255, g:0, b:0}]);
  const [bldTrans, setBldTrans] = useState(0x01);
  const [bldSpeed, setBldSpeed] = useState(16);
  const [bldPoints, setBldPoints] = useState('16');
  const [bldDir, setBldDir] = useState(1);
  
  // Builder 0x61
  const [bldPatternId, setBldPatternId] = useState('1');
  const [bldBright, setBldBright] = useState('100');

  // Builder 0x73
  const [bldMic, setBldMic] = useState(true);
  const [bldMusicMode, setBldMusicMode] = useState('1');
  const [bldSens, setBldSens] = useState('100');
  const [bldC2, setBldC2] = useState({r:0, g:0, b:255});

  // Builder 0x62
  const [bldIc, setBldIc] = useState('WS2812B');
  const [bldOrder, setBldOrder] = useState('RGB');
  const [bldSegs, setBldSegs] = useState('1');

  // Shared
  const [bldHexOverride, setBldHexOverride] = useState('');
  const [bldResult, setBldResult] = useState<{raw: number[], wrapped: number[], hex: string, annotations: string[]} | null>(null);

  // Rebuild payload when builder params change
  useEffect(() => {
    try {
      if (bldProtocol === '0x59') {
        const pts = Math.max(1, Math.min(300, parseInt(bldPoints) || 16));
        const pixels = Array(pts).fill(0).map((_, i) => bldColors[i % bldColors.length]);
        const result = build0x59(pixels, bldTrans, bldSpeed, bldDir);
        setBldResult(result);
        setBldHexOverride(result.hex);
      } else if (bldProtocol === '0x61') {
        const id = parseInt(bldPatternId) || 1;
        const spd = Math.max(1, Math.min(100, bldSpeed));
        const br = Math.max(0, Math.min(100, parseInt(bldBright)||100));
        const wrapped = ZenggeProtocol.setCustomRbm(id, spd, br);
        const hex = wrapped.map(b=>b.toString(16).toUpperCase().padStart(2,'0')).join(' ');
        setBldResult({ raw: wrapped, wrapped, hex, annotations: ['[0x61] RBM Pattern Payload', `Pattern: ${id}`, `Speed: ${spd}`, `Brightness: ${br}`] });
        setBldHexOverride(hex);
      } else if (bldProtocol === '0x73') {
        const id = parseInt(bldMusicMode) || 1;
        const c1 = bldColors[0] || {r:255,g:0,b:0};
        const s = parseInt(bldSens) || 100;
        const br = parseInt(bldBright) || 100;
        const wrapped = ZenggeProtocol.setMusicConfig(bldMic, 1, id, c1, bldC2, s, br);
        const hex = wrapped.map(b=>b.toString(16).toUpperCase().padStart(2,'0')).join(' ');
        setBldResult({ raw: wrapped, wrapped, hex, annotations: ['[0x73] Symphony Config', `Mic: ${bldMic}`, `Pattern: ${id}`, `Sens: ${s} Bright: ${br}`] });
        setBldHexOverride(hex);
      } else if (bldProtocol === '0x62') {
        const pts = parseInt(bldPoints) || 16;
        const seg = parseInt(bldSegs) || 1;
        const wrapped = ZenggeProtocol.setHardwareConfig(pts, bldOrder, bldIc, seg);
        const hex = wrapped.map(b=>b.toString(16).toUpperCase().padStart(2,'0')).join(' ');
        setBldResult({ raw: wrapped, wrapped, hex, annotations: ['[0x62] EEPROM Write', `IC: ${bldIc} Order: ${bldOrder}`, `LEDs: ${pts} Seg: ${seg}`] });
        setBldHexOverride(hex);
      }
    } catch(e) { }
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [bldProtocol, bldColors, bldTrans, bldSpeed, bldPoints, bldDir, bldPatternId, bldBright, bldMic, bldMusicMode, bldSens, bldC2, bldIc, bldOrder, bldSegs]);

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
    // Pass targetDeviceId so payload goes to the specific targeted device only
    await writeToDevice(bytes, targetDeviceId ?? undefined).catch(console.warn);
    const hexStr = bytes.map(b => b.toString(16).toUpperCase().padStart(2, '0')).join(' ');
    setLastSent(hexStr);
    setLastNote(note || '');
    setLogs(prev => [{ dir: 'TX' as const, hex: hexStr, t: Date.now(), note, dev: targetDeviceId ?? undefined }, ...prev].slice(0, 200));
  }, [writeToDevice, targetDeviceId]);

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

  const renderHwBadge = () => {
    const targetName = targetDeviceId ? connectedDevices.find(d => d.id === targetDeviceId)?.name || targetDeviceId.slice(-6) : null;
    return (
      <View style={S.hwBadge}>
        <Text style={S.hwBadgeLabel}>TARGET: </Text>
        {targetDeviceId ? (
          <>
            <Text style={[S.hwBadgeVal, { color: '#00ccff', paddingRight: 8 }]}>{targetName}</Text>
            {hwSettings?.detected ? (
              <>
                <Text style={[S.hwBadgeVal, { color: '#00CC88' }]}>{hwSettings.ledPoints ?? '?'} LEDs</Text>
                <Text style={S.hwBadgeLabel}> · </Text>
                <Text style={[S.hwBadgeVal, { color: '#FF9500' }]}>{hwSettings.colorSortingName ?? '?'}</Text>
                <Text style={S.hwBadgeLabel}> · </Text>
                <Text style={[S.hwBadgeVal, { color: '#c084fc' }]}>{hwSettings.icName ?? '?'}</Text>
              </>
            ) : (
             <Text style={[S.hwBadgeVal, { color: '#FF4040' }]}>WAITING 0x63...</Text>
            )}
          </>
        ) : (
          <Text style={[S.hwBadgeVal, { color: '#FF4040' }]}>NO DEVICE TARGETED (GO TO DEVICES TAB)</Text>
        )}
      </View>
    );
  };

  // ─── DEVICES TAB ────────────────────────────────────────────────────────────
  const renderDevicesTab = () => (
    <ScrollView contentContainerStyle={{ paddingBottom: 40 }}>
      <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
         <Text style={S.sectionTitle}>HARDWARE SCANNER</Text>
         <TouchableOpacity 
           style={{ backgroundColor: isScanning ? '#555' : '#00E676', paddingHorizontal: 16, paddingVertical: 8, borderRadius: 6 }} 
           onPress={() => { if (handleScan) handleScan(); }}
           disabled={isScanning}
         >
           <Text style={{ color: '#000', fontWeight: 'bold', fontSize: 11 }}>{isScanning ? 'SCANNING...' : 'START SCAN'}</Text>
         </TouchableOpacity>
      </View>
      
      {(!allDevices || allDevices.length === 0) && (
        <Text style={S.hint}>No devices found. Tap START SCAN to begin.</Text>
      )}

      {allDevices?.map((d: any, idx) => {
         const cfg = { ...(liveDeviceConfigs?.[d.id] || {}) };
         const points   = cfg.points    ?? d.points    ?? null;
         const segments = cfg.segments  ?? d.segments  ?? null;
         const sorting  = cfg.sorting   ?? d.sorting   ?? cfg.colorSortingName ?? null;
         const stripType= cfg.stripType ?? d.stripType ?? cfg.icName           ?? null;
         const isConn   = connectedDevices?.some((c: any) => c.id === d.id);
         const isTarget = targetDeviceId === d.id;

         return (
         <View key={d.id || idx} style={[S.diagBox, isTarget && { borderColor: '#00f0ff', borderWidth: 2 }]}>
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: 8 }}>
              <Text style={{ color: '#FFF', fontWeight: 'bold', fontSize: 13, flex: 1 }} numberOfLines={1}>{cfg.name || d.name || 'Unknown Device'}</Text>
              {isConn
                ? (
                  <View style={{ flexDirection: 'row', gap: 6 }}>
                    <Text style={{ color: '#00E676', fontSize: 10, fontWeight: '700', alignSelf: 'center' }}>● CONNECTED</Text>
                    <TouchableOpacity onPress={() => setTargetDeviceId(d.id)} style={{ backgroundColor: isTarget ? '#00f0ff' : '#333', paddingHorizontal: 10, paddingVertical: 6, borderRadius: 6 }}>
                      <Text style={{ color: isTarget ? '#000' : '#FFF', fontSize: 10, fontWeight: 'bold' }}>{isTarget ? 'TARGETED' : 'TARGET'}</Text>
                    </TouchableOpacity>
                  </View>
                )
                : <TouchableOpacity onPress={() => connectToDevice && connectToDevice(d)} style={{ backgroundColor: '#9D4EFF', paddingHorizontal: 10, paddingVertical: 6, borderRadius: 6 }}>
                    <Text style={{ color: '#FFF', fontSize: 10, fontWeight: 'bold' }}>CONNECT</Text>
                  </TouchableOpacity>
              }
            </View>
            <Text style={{ color: '#555', fontSize: 10, marginBottom: 8, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>{d.id}</Text>
            
            <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 8 }}>
               <Text style={{ color: '#888', fontSize: 10 }}>LEDs: <Text style={{ color: '#00ccff' }}>{points ?? '?'}</Text></Text>
               <Text style={{ color: '#888', fontSize: 10 }}>Seg: <Text style={{ color: '#00ccff' }}>{segments ?? '?'}</Text></Text>
               <Text style={{ color: '#888', fontSize: 10 }}>Sort: <Text style={{ color: '#FF69B4' }}>{sorting ?? '?'}</Text></Text>
               <Text style={{ color: '#888', fontSize: 10 }}>IC: <Text style={{ color: '#FFD700' }}>{stripType ?? '?'}</Text></Text>
            </View>

            {isTarget && hwSettings?.detected && (
              <View style={{ marginTop: 8, paddingTop: 8, borderTopWidth: 1, borderTopColor: '#333' }}>
                 <Text style={{ color: '#00CC88', fontSize: 10 }}>Hardware settings populated from active probe.</Text>
              </View>
            )}
         </View>
         );
      })}
    </ScrollView>
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
        <View style={{ width: 40, height: 40, backgroundColor: `rgb(${bldColors[0]?.r||0},${bldColors[0]?.g||0},${bldColors[0]?.b||0})`, borderRadius: 8, borderWidth: 1, borderColor: '#444' }} />
        <Text style={{ color: '#aaa', fontSize: 12 }}>R:{bldColors[0]?.r||0} G:{bldColors[0]?.g||0} B:{bldColors[0]?.b||0} · {hwPts} LEDs · Speed:{bldSpeed}</Text>
      </View>

      {TRANSITION_TYPES.map(tt => (
        <TouchableOpacity key={tt.byte}
          style={[S.transBtn, { borderColor: tt.color }]}
          onPress={() => {
            const pixels = Array(hwPts).fill({ r: bldColors[0]?.r||0, g: bldColors[0]?.g||0, b: bldColors[0]?.b||0 });
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
      <Text style={S.sectionTitle}>PROTOCOL BUILDER</Text>
      <Text style={S.hint}>Select a protocol and build the exact hex packet.</Text>
      {renderHwBadge()}

      <Text style={S.subTitle}>PROTOCOL</Text>
      <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 8, marginBottom: 16 }}>
        {[
          { id: '0x59', label: '0x59 Segments' },
          { id: '0x61', label: '0x61 RBM Pattern' },
          { id: '0x73', label: '0x73 Symphony' },
          { id: '0x62', label: '0x62 Setup' }
        ].map(p => (
           <TouchableOpacity key={p.id}
             style={[S.chip, bldProtocol === p.id && S.chipActive]}
             onPress={() => setBldProtocol(p.id as any)}>
             <Text style={{ color: bldProtocol === p.id ? '#00f0ff' : '#888', fontWeight: 'bold', fontSize: 11 }}>{p.label}</Text>
           </TouchableOpacity>
        ))}
      </View>

      {bldProtocol === '0x59' && (
        <View style={{ marginBottom: 16 }}>
          <Text style={S.subTitle}>PIXEL COLORS (0x59)</Text>
          <Text style={S.hint}>Colors will be repeated to fill the LED count.</Text>
          
          {bldColors.map((c, i) => (
             <View key={i} style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 8, gap: 8 }}>
                <View style={{ width: 24, height: 24, backgroundColor: `rgb(${c.r},${c.g},${c.b})`, borderRadius: 4, borderWidth: 1, borderColor: '#444' }} />
                <TextInput style={[{flex:1}, S.numInput]} value={c.r.toString()} keyboardType="numeric" onChangeText={v => { const cur = [...bldColors]; cur[i].r = parseInt(v)||0; setBldColors(cur); }} placeholder="R" />
                <TextInput style={[{flex:1}, S.numInput]} value={c.g.toString()} keyboardType="numeric" onChangeText={v => { const cur = [...bldColors]; cur[i].g = parseInt(v)||0; setBldColors(cur); }} placeholder="G" />
                <TextInput style={[{flex:1}, S.numInput]} value={c.b.toString()} keyboardType="numeric" onChangeText={v => { const cur = [...bldColors]; cur[i].b = parseInt(v)||0; setBldColors(cur); }} placeholder="B" />
                <TouchableOpacity onPress={() => setBldColors(bldColors.filter((_, idx)=>idx!==i))} style={{ padding: 8 }}>
                  <MaterialCommunityIcons name="delete" color="#ff4040" size={16} />
                </TouchableOpacity>
             </View>
          ))}
          <TouchableOpacity onPress={() => setBldColors([...bldColors, {r:0,g:0,b:0}])} style={{ alignSelf: 'flex-start', padding: 8, backgroundColor: '#222', borderRadius: 4 }}>
             <Text style={{ color: '#aaa', fontSize: 10 }}>+ ADD COLOR</Text>
          </TouchableOpacity>

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

          <View style={{ flexDirection: 'row', gap: 12, marginBottom: 16 }}>
            <View style={{ flex: 1 }}>
              <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>NUM POINTS</Text>
              <TextInput style={S.numInput} value={bldPoints} keyboardType="numeric"
                onChangeText={v => setBldPoints(v)} placeholder={hwPts.toString()} placeholderTextColor="#444" />
              <Text style={{ color: '#555', fontSize: 10, marginTop: 2 }}>HW probe: {hwPts}</Text>
            </View>
            <View style={{ flex: 1 }}>
              <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>SPEED</Text>
              <TextInput style={S.numInput} value={bldSpeed.toString()} keyboardType="numeric"
                onChangeText={v => setBldSpeed(Math.max(1, Math.min(255, parseInt(v)||1)))} />
            </View>
            <View style={{ flex: 1 }}>
              <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>DIR</Text>
              <View style={{ flexDirection: 'row', gap: 4 }}>
                {[0, 1].map(d => (
                  <TouchableOpacity key={d} style={[S.chip, bldDir === d && S.chipActive, {flex: 1}]} onPress={() => setBldDir(d)}>
                    <Text style={{ color: bldDir === d ? '#00f0ff' : '#888', fontSize: 11, textAlign: 'center' }}>{d === 0 ? 'REV' : 'FWD'}</Text>
                  </TouchableOpacity>
                ))}
              </View>
            </View>
          </View>
        </View>
      )}

      {bldProtocol === '0x61' && (
        <View style={{ marginBottom: 16 }}>
          <View style={{ flexDirection: 'row', gap: 12 }}>
            <View style={{ flex: 1 }}>
              <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>PATTERN ID (1-210)</Text>
              <TextInput style={S.numInput} value={bldPatternId} keyboardType="numeric" onChangeText={setBldPatternId} />
            </View>
            <View style={{ flex: 1 }}>
              <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>SPEED (1-100)</Text>
              <TextInput style={S.numInput} value={bldSpeed.toString()} keyboardType="numeric" onChangeText={v => setBldSpeed(Math.max(1, Math.min(100, parseInt(v)||1)))} />
            </View>
            <View style={{ flex: 1 }}>
              <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>BRIGHTNESS (0-100)</Text>
              <TextInput style={S.numInput} value={bldBright} keyboardType="numeric" onChangeText={setBldBright} />
            </View>
          </View>
        </View>
      )}

      {bldProtocol === '0x73' && (
        <View style={{ marginBottom: 16 }}>
          <View style={{ flexDirection: 'row', gap: 12, marginBottom: 12 }}>
            <View style={{ flex: 1 }}>
              <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>MUSIC MODE (1-13)</Text>
              <TextInput style={S.numInput} value={bldMusicMode} keyboardType="numeric" onChangeText={setBldMusicMode} />
            </View>
            <View style={{ flex: 1 }}>
              <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>MIC SRC</Text>
              <TouchableOpacity style={[S.chip, bldMic && S.chipActive, {paddingVertical: 10}]} onPress={() => setBldMic(!bldMic)}>
                 <Text style={{ color: bldMic ? '#00f0ff' : '#888', textAlign: 'center', fontSize: 11, fontWeight: 'bold' }}>{bldMic ? 'DEVICE' : 'APP'}</Text>
              </TouchableOpacity>
            </View>
          </View>
          <View style={{ flexDirection: 'row', gap: 12, marginBottom: 12 }}>
            <View style={{ flex: 1 }}>
              <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>SENSITIVITY (0-100)</Text>
              <TextInput style={S.numInput} value={bldSens} keyboardType="numeric" onChangeText={setBldSens} />
            </View>
            <View style={{ flex: 1 }}>
              <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>BRIGHTNESS (0-100)</Text>
              <TextInput style={S.numInput} value={bldBright} keyboardType="numeric" onChangeText={setBldBright} />
            </View>
          </View>
          <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>COLOR 1 (RGB)</Text>
          <View style={{ flexDirection: 'row', gap: 8, marginBottom: 8 }}>
            <TextInput style={[S.numInput, {flex: 1}]} value={bldColors[0]?.r?.toString()||'0'} onChangeText={v => setBldColors([{r: parseInt(v)||0, g: bldColors[0]?.g||0, b: bldColors[0]?.b||0}])} />
            <TextInput style={[S.numInput, {flex: 1}]} value={bldColors[0]?.g?.toString()||'0'} onChangeText={v => setBldColors([{r: bldColors[0]?.r||0, g: parseInt(v)||0, b: bldColors[0]?.b||0}])} />
            <TextInput style={[S.numInput, {flex: 1}]} value={bldColors[0]?.b?.toString()||'0'} onChangeText={v => setBldColors([{r: bldColors[0]?.r||0, g: bldColors[0]?.g||0, b: parseInt(v)||0}])} />
          </View>
          <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>COLOR 2 (RGB)</Text>
           <View style={{ flexDirection: 'row', gap: 8 }}>
            <TextInput style={[S.numInput, {flex: 1}]} value={bldC2.r.toString()} onChangeText={v => setBldC2({...bldC2, r: parseInt(v)||0})} />
            <TextInput style={[S.numInput, {flex: 1}]} value={bldC2.g.toString()} onChangeText={v => setBldC2({...bldC2, g: parseInt(v)||0})} />
            <TextInput style={[S.numInput, {flex: 1}]} value={bldC2.b.toString()} onChangeText={v => setBldC2({...bldC2, b: parseInt(v)||0})} />
          </View>
        </View>
      )}

      {bldProtocol === '0x62' && (
        <View style={{ marginBottom: 16 }}>
          <View style={{ flexDirection: 'row', gap: 12, marginBottom: 12 }}>
            <View style={{ flex: 1 }}>
               <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>IC CHIP (e.g. WS2812B)</Text>
               <TextInput style={S.numInput} value={bldIc} onChangeText={setBldIc} />
            </View>
            <View style={{ flex: 1 }}>
               <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>COLOR ORDER (e.g. RGB)</Text>
               <TextInput style={S.numInput} value={bldOrder} onChangeText={setBldOrder} />
            </View>
          </View>
          <View style={{ flexDirection: 'row', gap: 12 }}>
             <View style={{ flex: 1 }}>
               <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>TOTAL LEDS</Text>
               <TextInput style={S.numInput} value={bldPoints} keyboardType="numeric" onChangeText={setBldPoints} />
            </View>
            <View style={{ flex: 1 }}>
               <Text style={{ color: '#888', fontSize: 10, marginBottom: 4 }}>SEGMENTS</Text>
               <TextInput style={S.numInput} value={bldSegs} keyboardType="numeric" onChangeText={setBldSegs} />
            </View>
          </View>
        </View>
      )}

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
                const pts = parseInt(bldPoints) || hwPts || 16;
                const { wrapped } = build0x59(Array(pts).fill({ r: preset.r, g: preset.g, b: preset.b }), preset.trans || 0x01, bldSpeed, 1);
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
      <SafeAreaView style={[S.root, { paddingTop: insets.top }]}>
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
          {(['DEVICES', 'COLOR', 'TRANSITION', 'BUILDER', 'SNIFFER'] as LabTab[]).map(t => (
            <TouchableOpacity key={t} style={[S.tabBtn, tab === t && S.tabBtnActive]} onPress={() => setTab(t)}>
              <Text style={[S.tabBtnTxt, tab === t && S.tabBtnTxtActive]}>{t}</Text>
            </TouchableOpacity>
          ))}
        </View>

        {/* Content */}
        <View style={S.content}>
          {tab === 'DEVICES'    && renderDevicesTab()}
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
