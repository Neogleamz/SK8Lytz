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

import { MaterialCommunityIcons } from '@expo/vector-icons';
import React, { useEffect, useState } from 'react';
import {
    FlatList, Platform,
    SafeAreaView,
    ScrollView,
    StyleSheet,
    Text,
    TextInput,
    TouchableOpacity,
    View
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { useTheme } from '../../../context/ThemeContext';
import { useDiagnosticLog } from '../../../hooks/useDiagnosticLog';
import { useProtocolBuilder } from '../../../hooks/useProtocolBuilder';
import { useRegistration } from '../../../hooks/useRegistration';
import { ZenggeProtocol } from '../../../protocols/ZenggeProtocol';
import { Spacing, Typography } from '../../../theme/theme';
import CustomEffectVisualizer from '../../CustomEffectVisualizer';

// ─── Types ────────────────────────────────────────────────────────────────────

interface LabProps {
  visible: boolean;
  onClose: () => void;
  writeToDevice?: (data: number[], deviceId?: string) => Promise<void | boolean | 'partial'>;
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
  bleState?: string;
  handleScan?: () => void;
  connectToDevice?: (device: any) => Promise<any>;
  liveDeviceConfigs?: Record<string, any>;
  isDiagnosticsMode?: boolean;
  onToggleDiagnostics?: () => void;
}

type LabTab = 'DEVICES' | 'COLOR' | 'TRANSITION' | 'BUILDER' | 'SNIFFER';

// moved to useDiagnosticLog.ts

// ─── Quick Palette for Lab Builders ────────────────────────────────────────
const QUICK_PALETTE = [
  { p: 'Red', hex: '#FF0000', r: 255, g: 0, b: 0 },
  { p: 'Green', hex: '#00FF00', r: 0, g: 255, b: 0 },
  { p: 'Blue', hex: '#0000FF', r: 0, g: 0, b: 255 },
  { p: 'Yellow', hex: '#FFFF00', r: 255, g: 255, b: 0 },
  { p: 'Cyan', hex: '#00FFFF', r: 0, g: 255, b: 255 },
  { p: 'Purple', hex: '#8000FF', r: 128, g: 0, b: 255 },
  { p: 'Orange', hex: '#FF8800', r: 255, g: 136, b: 0 },
  { p: 'Magenta', hex: '#FF00FF', r: 255, g: 0, b: 255 },
  { p: 'White', hex: '#FFFFFF', r: 255, g: 255, b: 255 },
  { p: 'Black', hex: '#000000', r: 0, g: 0, b: 0 },
];

const QuickColorGrid = ({ onSelect, activeColor }: { onSelect: (c: {r:number,g:number,b:number}) => void, activeColor?: {r:number,g:number,b:number} }) => {
  return (
    <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.sm, marginBottom: Spacing.md }}>
      {QUICK_PALETTE.map((c, i) => {
        const isActive = activeColor && activeColor.r === c.r && activeColor.g === c.g && activeColor.b === c.b;
        return (
          <TouchableOpacity 
            key={i} 
            onPress={() => onSelect({ r: c.r, g: c.g, b: c.b })}
            style={{ 
              width: 32, height: 32, borderRadius: 16, backgroundColor: c.hex,
              borderWidth: 2, borderColor: isActive ? '#00f0ff' : 'rgba(255,255,255,0.1)',
              justifyContent: 'center', alignItems: 'center'
            }}
          />
        );
      })}
    </View>
  );
};

// ─── Transition type reference (hardware-confirmed live testing Apr 2026) ────
const TRANSITION_TYPES = [
  { byte: 0x00, label: 'CASCADE',  color: '#FF9500', desc: '✅ Continuous scroll — hardware loops array around strip. Use for animated patterns.' },
  { byte: 0x01, label: 'FREEZE',   color: '#00CC88', desc: '✅ Static lock — array is held in place, no movement. Use for solid/street lights.' },
  { byte: 0x02, label: 'STROBE',   color: '#FF4040', desc: '⚠️ Intended flash — visually similar to FREEZE on some firmware. Use for hard brake alert.' },
  { byte: 0x03, label: 'TRIGGER',  color: '#FF69B4', desc: '🔴 One-shot trigger — renders array at NEXT offset then stops. Causes blink+new-position on each send. NOT continuous animation.' },
];

// moved to useProtocolBuilder.ts

// ─── Main Component ───────────────────────────────────────────────────────────

export default function Sk8LytzDiagnosticLab({
  visible, onClose, writeToDevice, liveRxPayload,
  connectedDevices = [], hwSettings,
  allDevices = [], bleState = 'IDLE', handleScan,
  connectToDevice, liveDeviceConfigs = {},
  isDiagnosticsMode, onToggleDiagnostics,
}: LabProps) {

  const { Colors, isDark } = useTheme();
  const bg      = Colors.background;
  const cardBg  = Colors.surface;
  const txtPri  = Colors.text;
  const txtMuted= Colors.textMuted;
  const border  = Colors.surfaceHighlight;
  const cyan    = '#00f0ff';

  const { registeredDevices } = useRegistration();

  const [tab, setTab] = useState<LabTab>('DEVICES');
  const insets = useSafeAreaInsets();
  const hwPts = hwSettings?.ledPoints || 16;

  /**
   * targetDeviceId — the device currently selected for all TX/RX operations in the Lab.
   * This state was missing from the original useDiagnosticLog extraction (P0 audit fix).
   */
  const [targetDeviceId, setTargetDeviceId] = useState<string | null>(null);

  // Domain Hooks
  const {
    logs, lastSent, lastNote, transmit, sendRawHex, clearLogs
  } = useDiagnosticLog({ visible, liveRxPayload, writeToDevice, targetDeviceId });

  const {
    bldProtocol, setBldProtocol,
    bldColors, setBldColors,
    bldTrans, setBldTrans,
    bldSpeed, setBldSpeed,
    bldPoints, setBldPoints,
    bldDir, setBldDir,
    bldPatternId, setBldPatternId,
    bldBright, setBldBright,
    bld51Mode, setBld51Mode,
    bld51Speed, setBld51Speed,
    bld51Color1, setBld51Color1,
    bld51Color2, setBld51Color2,
    bld51Dir, setBld51Dir,
    bld51Seg, setBld51Seg,
    bldMic, setBldMic,
    bldMusicMode, setBldMusicMode,
    bldSens, setBldSens,
    bldC2, setBldC2,
    bldMatrixStyle, setBldMatrixStyle,
    bldIc, setBldIc,
    bldOrder, setBldOrder,
    bldSegs, setBldSegs,
    bldResult
  } = useProtocolBuilder(hwPts);

  // Manual override for edited hex
  const [bldHexOverride, setBldHexOverride] = useState('');

  // Sync override with result whenever result changes (unless manually edited)
  useEffect(() => {
    if (bldResult?.hex) setBldHexOverride(bldResult.hex);
  }, [bldResult]);

  // ─── Solid color test helper ────────────────────────────────────────────────
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
            <Text style={[S.hwBadgeVal, { color: '#00ccff', paddingRight: Spacing.sm }]}>{targetName}</Text>
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

  // ─── DEVICES TAB ───────────────────────────────────────────────────
  const renderDevicesTab = () => (
    <ScrollView contentContainerStyle={{ paddingBottom: Spacing.xxxl }}>
      <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: Spacing.xl }}>
        <Text style={[S.sectionTitle, { color: txtPri }]}>HARDWARE SCANNER</Text>
        <TouchableOpacity 
          style={{ backgroundColor: bleState === 'SCANNING' ? border : cyan, paddingHorizontal: Spacing.lg, paddingVertical: Spacing.md, borderRadius: 8, borderWidth: 1, borderColor: bleState === 'SCANNING' ? border : cyan }} 
          onPress={() => { if (handleScan) handleScan(); }}
          disabled={bleState === 'SCANNING' || bleState === 'PROBING'}
        >
          <Text style={{ color: bleState === 'SCANNING' ? txtMuted : '#000', fontWeight: '900', fontSize: 11, letterSpacing: 0.5 }}>
            {bleState === 'SCANNING' ? 'SCANNING...' : bleState === 'PROBING' ? 'PROBING...' : 'START SCAN'}
          </Text>
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
          <View key={d.id || idx} style={[S.diagBox, isTarget && { borderColor: cyan, borderWidth: 1.5 }]}>
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: Spacing.sm }}>
              <View style={{ flex: 1 }}>
                <Text style={{ color: txtPri, fontWeight: 'bold', fontSize: 13 }} numberOfLines={1}>
                  {cfg.name || d.name || 'Unknown Device'}
                </Text>
                {registeredDevices.some(rd => rd.device_mac === d.id) ? (
                  <Text style={{ color: '#FFD700', fontSize: 10, fontWeight: '700', marginTop: Spacing.xxs }}>★ REGISTERED TO YOU</Text>
                ) : (d.owner_ids && d.owner_ids.length > 0) ? (
                  <Text style={{ color: '#FF4040', fontSize: 10, fontWeight: '700', marginTop: Spacing.xxs }}>
                    🔒 CLAIMED BY: {d.owner_ids.length > 1 ? `${d.owner_ids.length} USERS` : `${d.owner_ids[0].substring(0,8)}...`}
                  </Text>
                ) : (
                  <Text style={{ color: txtMuted, fontSize: 10, fontStyle: 'italic', marginTop: Spacing.xxs }}>Unregistered (Telemetry Active)</Text>
                )}
              </View>
              {isConn
                ? (
                  <View style={{ flexDirection: 'row', gap: Spacing.sm }}>
                    <Text style={{ color: '#00E676', fontSize: 10, fontWeight: '900', alignSelf: 'center' }}>● LIVE</Text>
                    <TouchableOpacity 
                      onPress={() => setTargetDeviceId(d.id)} 
                      style={{ backgroundColor: isTarget ? cyan : border, paddingHorizontal: Spacing.md, paddingVertical: Spacing.sm, borderRadius: 6 }}
                    >
                      <Text style={{ color: isTarget ? '#000' : txtPri, fontSize: 10, fontWeight: 'bold' }}>
                        {isTarget ? 'TARGETED' : 'TARGET'}
                      </Text>
                    </TouchableOpacity>
                  </View>
                )
                : (
                  <TouchableOpacity 
                    onPress={() => connectToDevice && connectToDevice(d)} 
                    style={{ backgroundColor: '#9D4EFF', paddingHorizontal: Spacing.md, paddingVertical: Spacing.sm, borderRadius: 6 }}
                  >
                    <Text style={{ color: '#FFF', fontSize: 10, fontWeight: 'bold' }}>CONNECT</Text>
                  </TouchableOpacity>
                )
              }
            </View>
            <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.md, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>
              {d.id}
            </Text>
            
            <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.md }}>
              <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                <Text style={{ color: txtMuted, fontSize: 10 }}>LEDs </Text>
                <Text style={{ color: cyan, fontSize: 10, fontWeight: 'bold' }}>{points ?? '?'}</Text>
              </View>
              <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                <Text style={{ color: txtMuted, fontSize: 10 }}>SEG </Text>
                <Text style={{ color: cyan, fontSize: 10, fontWeight: 'bold' }}>{segments ?? '?'}</Text>
              </View>
              <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                <Text style={{ color: txtMuted, fontSize: 10 }}>SORT </Text>
                <Text style={{ color: '#FF69B4', fontSize: 10, fontWeight: 'bold' }}>{sorting ?? '?'}</Text>
              </View>
              <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                <Text style={{ color: txtMuted, fontSize: 10 }}>IC </Text>
                <Text style={{ color: '#FFD700', fontSize: 10, fontWeight: 'bold' }}>{stripType ?? '?'}</Text>
              </View>
            </View>

            {isTarget && hwSettings?.detected && (
              <View style={{ marginTop: Spacing.md, paddingTop: Spacing.md, borderTopWidth: 1, borderTopColor: border }}>
                <Text style={{ color: '#00CC88', fontSize: 10, fontWeight: 'bold' }}>
                  ✓ Hardware settings populated from active probe.
                </Text>
              </View>
            )}
          </View>
        );
      })}
    </ScrollView>
  );

  // ─── COLOR TEST TAB ─────────────────────────────────────────────────────────
  const renderColorTab = () => (
    <ScrollView contentContainerStyle={{ paddingBottom: Spacing.xxxl }}>
      <Text style={[S.sectionTitle, { color: txtPri }]}>COLOR ORDER TEST</Text>
      <Text style={[S.hint, { color: txtMuted }]}>
        Tap each button — observe what color the hardware actually shows.{'\n'}
        This identifies whether hardware auto-remaps GRB or needs pre-swapped bytes.
      </Text>

      {renderHwBadge()}

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

      <Text style={[S.subTitle, { color: txtMuted }]}>GRB PRE-SWAPPED VERSION</Text>
      <View style={S.colorBtnRow}>
        {[
          { label: 'GRB RED\n[0,255,0]',   r:0,   g:255, b:0,   bg: isDark ? '#401010' : '#fee2e2', border:'#FF4040', note:'GRB-swapped RED' },
          { label: 'GRB GREEN\n[255,0,0]', r:255, g:0,   b:0,   bg: isDark ? '#104010' : '#f0fdf4', border:'#00CC00', note:'GRB-swapped GREEN' },
          { label: 'GRB BLUE\n[0,0,255]',  r:0,   g:0,   b:255, bg: isDark ? '#101040' : '#eff6ff', border:'#4040FF', note:'GRB Blue same' },
        ].map(btn => (
          <TouchableOpacity key={btn.label} style={[S.bigColorBtn, { backgroundColor: btn.bg, borderColor: btn.border }]}
            onPress={() => sendSolid(btn.r, btn.g, btn.b, hwPts, 0x01, btn.note)}>
            <Text style={{ color: btn.border, fontWeight: '900', textAlign: 'center', fontSize: 11 }}>{btn.label}</Text>
          </TouchableOpacity>
        ))}
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

  // ─── TRANSITION TYPE TAB ────────────────────────────────────────────────────
  const renderTransitionTab = () => (
    <ScrollView contentContainerStyle={{ paddingBottom: Spacing.xxxl }}>
      <Text style={[S.sectionTitle, { color: txtPri }]}>TRANSITION TYPE PROBE</Text>
      <Text style={[S.hint, { color: txtMuted }]}>
        Each button sends the same color with a different transitionType byte.{'\n'}
        Observe what the hardware actually does — we need to confirm 0x00 vs 0x01 behavior.
      </Text>
      {renderHwBadge()}

      <Text style={[S.subTitle, { color: txtMuted }]}>COLOR TO SEND (change in BUILDER tab first)</Text>
      <View style={{ flexDirection: 'row', gap: Spacing.md, marginBottom: Spacing.lg, alignItems: 'center' }}>
        <View style={{ width: 40, height: 40, backgroundColor: `rgb(${bldColors[0]?.r||0},${bldColors[0]?.g||0},${bldColors[0]?.b||0})`, borderRadius: 8, borderWidth: 1, borderColor: border }} />
        <Text style={{ color: txtMuted, fontSize: 12 }}>R:{bldColors[0]?.r||0} G:{bldColors[0]?.g||0} B:{bldColors[0]?.b||0} · {hwPts} LEDs · Speed:{bldSpeed}</Text>
      </View>

      {TRANSITION_TYPES.map(tt => (
        <TouchableOpacity key={tt.byte}
          style={[S.transBtn, { borderColor: tt.color, backgroundColor: cardBg }]}
          onPress={() => {
            const numPoints = hwPts;
            const totalLen = numPoints * 3 + 9;
            const raw = new Array(totalLen).fill(0);
            raw[0] = 0x59; raw[1] = (totalLen >> 8) & 0xFF; raw[2] = totalLen & 0xFF;
            let idx = 3;
            for (let i=0; i<numPoints; i++) { raw[idx++] = bldColors[0]?.r||0; raw[idx++] = bldColors[0]?.g||0; raw[idx++] = bldColors[0]?.b||0; }
            raw[idx++] = (numPoints >> 8) & 0xFF; raw[idx++] = numPoints & 0xFF;
            raw[idx++] = tt.byte & 0xFF; raw[idx++] = bldSpeed; raw[idx++] = 1;
            raw[idx] = ZenggeProtocol.calculateChecksum(raw.slice(0, totalLen - 1));
            transmit(ZenggeProtocol.wrapCommand(raw), `transitionType=0x0${tt.byte.toString(16).toUpperCase()} ${tt.label}`);
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

  // ─── BUILDER TAB ────────────────────────────────────────────────────────────
  const renderBuilderTab = () => (
    <ScrollView contentContainerStyle={{ paddingBottom: Spacing.xxxl }}>
      <Text style={[S.sectionTitle, { color: txtPri }]}>PROTOCOL BUILDER</Text>
      <Text style={[S.hint, { color: txtMuted }]}>Select a protocol and build the exact hex packet.</Text>
      {renderHwBadge()}

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
             onPress={() => setBldProtocol(p.id as any)}>
             <Text style={{ color: bldProtocol === p.id ? cyan : txtMuted, fontWeight: '900', fontSize: 11 }}>{p.label}</Text>
           </TouchableOpacity>
        ))}
      </View>

      {bldProtocol === '0x51' && (
        <View style={[S.diagBox, { backgroundColor: cardBg, borderColor: border }]}>
          <Text style={[S.subTitle, { color: txtPri, marginTop: 0 }]}>DIY MODE MATH SIMULATOR</Text>
          <View style={{ alignItems: 'center', marginBottom: Spacing.lg }}>
            <CustomEffectVisualizer 
              effectId={parseInt(bld51Mode) || 1} 
              fgColorHex={`#${bld51Color1.r.toString(16).padStart(2,'0')}${bld51Color1.g.toString(16).padStart(2,'0')}${bld51Color1.b.toString(16).padStart(2,'0')}`}
              bgColorHex={`#${bld51Color2.r.toString(16).padStart(2,'0')}${bld51Color2.g.toString(16).padStart(2,'0')}${bld51Color2.b.toString(16).padStart(2,'0')}`}
              speed={Math.max(1, Math.min(31, parseInt(bld51Speed) || 16))}
              direction={bld51Dir === 1}
              segments={bld51Seg ? 2 : 1}
            />
          </View>

          <View style={{ flexDirection: 'row', gap: Spacing.md, marginBottom: Spacing.md }}>
            <View style={{ flex: 1 }}>
              <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>EFFECT ID (1–33)</Text>
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}>
                <TouchableOpacity onPress={() => setBld51Mode(String(Math.max(1, (parseInt(bld51Mode)||1) - 1)))} style={{ backgroundColor: border, borderRadius: 6, width: 36, height: 36, justifyContent: 'center', alignItems: 'center' }}>
                  <Text style={{ color: txtPri, fontSize: 18, fontWeight: 'bold' }}>‒</Text>
                </TouchableOpacity>
                <TextInput style={[S.numInput, { flex: 1, backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri, textAlign: 'center' }]} value={bld51Mode} keyboardType="numeric" onChangeText={v => setBld51Mode(String(Math.max(1, Math.min(33, parseInt(v)||1))))} />
                <TouchableOpacity onPress={() => setBld51Mode(String(Math.min(33, (parseInt(bld51Mode)||1) + 1)))} style={{ backgroundColor: border, borderRadius: 6, width: 36, height: 36, justifyContent: 'center', alignItems: 'center' }}>
                  <Text style={{ color: txtPri, fontSize: 18, fontWeight: 'bold' }}>+</Text>
                </TouchableOpacity>
              </View>
            </View>
            <View style={{ flex: 1 }}>
              <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>SPEED (1–31)</Text>
              <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bld51Speed.toString()} keyboardType="numeric" onChangeText={v => setBld51Speed(Math.max(1, Math.min(31, parseInt(v)||1)).toString())} />
            </View>
          </View>

          <View style={{ flexDirection: 'row', gap: Spacing.md, marginBottom: Spacing.lg }}>
            <View style={{ flex: 1 }}>
              <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>DIR (Bit 7)</Text>
              <View style={{ flexDirection: 'row', gap: Spacing.xs }}>
                {[0, 1].map(d => (
                  <TouchableOpacity key={d} style={[S.chip, bld51Dir === d && { backgroundColor: cyan + '22', borderColor: cyan }, {flex: 1, height: 40}]} onPress={() => setBld51Dir(d)}>
                    <Text style={{ color: bld51Dir === d ? cyan : txtMuted, fontSize: 11, textAlign: 'center', fontWeight: '900' }}>{d === 0 ? 'LEFT' : 'RIGHT'}</Text>
                  </TouchableOpacity>
                ))}
              </View>
            </View>
            <View style={{ flex: 1 }}>
              <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>SEGS (Bit 6)</Text>
              <TouchableOpacity style={[S.chip, bld51Seg && { backgroundColor: cyan + '22', borderColor: cyan }, {paddingVertical: Spacing.md, height: 40}]} onPress={() => setBld51Seg(!bld51Seg)}>
                <Text style={{ color: bld51Seg ? cyan : txtMuted, textAlign: 'center', fontSize: 11, fontWeight: '900' }}>{bld51Seg ? 'SPLIT (1)' : 'FULL (0)'}</Text>
              </TouchableOpacity>
            </View>
          </View>

          <Text style={[S.subTitle, { color: txtMuted, marginTop: Spacing.md }]}>FOREGROUND (COLOR 1)</Text>
          <QuickColorGrid activeColor={bld51Color1} onSelect={setBld51Color1} />

          <Text style={[S.subTitle, { color: txtMuted, marginTop: Spacing.md }]}>BACKGROUND (COLOR 2)</Text>
          <QuickColorGrid activeColor={bld51Color2} onSelect={setBld51Color2} />
        </View>
      )}

      {bldProtocol === '0x59' && (
        <View style={[S.diagBox, { backgroundColor: cardBg, borderColor: border }]}>
          <Text style={[S.subTitle, { color: txtPri, marginTop: 0 }]}>PIXEL COLORS (0x59)</Text>
          <Text style={[S.hint, { color: txtMuted }]}>Colors will be repeated to fill the LED count.</Text>
          
          {bldColors.map((c, i) => (
             <View key={i} style={{ flexDirection: 'row', alignItems: 'center', marginBottom: Spacing.sm, gap: Spacing.sm }}>
                <View style={{ width: 24, height: 24, backgroundColor: `rgb(${c.r},${c.g},${c.b})`, borderRadius: 6, borderWidth: 1, borderColor: border }} />
                <TextInput style={[{flex:1, backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri}, S.numInput]} value={c.r.toString()} keyboardType="numeric" onChangeText={v => { const cur = [...bldColors]; cur[i].r = parseInt(v)||0; setBldColors(cur); }} placeholder="R" placeholderTextColor={txtMuted} />
                <TextInput style={[{flex:1, backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri}, S.numInput]} value={c.g.toString()} keyboardType="numeric" onChangeText={v => { const cur = [...bldColors]; cur[i].g = parseInt(v)||0; setBldColors(cur); }} placeholder="G" placeholderTextColor={txtMuted} />
                <TextInput style={[{flex:1, backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri}, S.numInput]} value={c.b.toString()} keyboardType="numeric" onChangeText={v => { const cur = [...bldColors]; cur[i].b = parseInt(v)||0; setBldColors(cur); }} placeholder="B" placeholderTextColor={txtMuted} />
                <TouchableOpacity onPress={() => setBldColors(bldColors.filter((_, idx)=>idx!==i))} style={{ padding: Spacing.sm }}>
                  <MaterialCommunityIcons name="delete" color="#ff4040" size={18} />
                </TouchableOpacity>
             </View>
          ))}
          <TouchableOpacity onPress={() => setBldColors([...bldColors, {r:0,g:0,b:0}])} style={{ alignSelf: 'flex-start', paddingHorizontal: Spacing.lg, paddingVertical: Spacing.md, backgroundColor: border, borderRadius: 8, marginTop: Spacing.xs }}>
             <Text style={{ color: txtPri, fontSize: 11, fontWeight: '900' }}>+ ADD COLOR</Text>
          </TouchableOpacity>

          <Text style={[S.subTitle, { color: txtMuted, marginTop: Spacing.xl }]}>TRANSITION TYPE</Text>
          <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.sm, marginBottom: Spacing.lg }}>
            {TRANSITION_TYPES.map(tt => (
              <TouchableOpacity key={tt.byte}
                style={[S.chip, bldTrans === tt.byte && { backgroundColor: tt.color + '22', borderColor: tt.color }, { height: 40 }]}
                onPress={() => setBldTrans(tt.byte)}>
                <Text style={{ color: bldTrans === tt.byte ? tt.color : txtMuted, fontWeight: '900', fontSize: 11 }}>
                  0x0{tt.byte.toString(16).toUpperCase()} {tt.label}
                </Text>
              </TouchableOpacity>
            ))}
          </View>

          <View style={{ flexDirection: 'row', gap: Spacing.md, marginBottom: Spacing.sm }}>
            <View style={{ flex: 1 }}>
              <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>NUM POINTS</Text>
              <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldPoints} keyboardType="numeric"
                onChangeText={v => setBldPoints(v)} placeholder={hwPts.toString()} placeholderTextColor={txtMuted} />
              <Text style={{ color: cyan, fontSize: 10, marginTop: Spacing.xs, fontWeight: 'bold' }}>HW probe: {hwPts}</Text>
            </View>
            <View style={{ flex: 1 }}>
              <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>SPEED</Text>
              <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldSpeed.toString()} keyboardType="numeric"
                onChangeText={v => setBldSpeed(Math.max(1, Math.min(255, parseInt(v)||1)))} />
            </View>
            <View style={{ flex: 1 }}>
              <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>DIR</Text>
              <View style={{ flexDirection: 'row', gap: Spacing.xs }}>
                {[0, 1].map(d => (
                  <TouchableOpacity key={d} style={[S.chip, bldDir === d && { backgroundColor: cyan + '22', borderColor: cyan }, {flex: 1, height: 40}]} onPress={() => setBldDir(d)}>
                    <Text style={{ color: bldDir === d ? cyan : txtMuted, fontSize: 11, textAlign: 'center', fontWeight: '900' }}>{d === 0 ? 'REV' : 'FWD'}</Text>
                  </TouchableOpacity>
                ))}
              </View>
            </View>
          </View>
        </View>
      )}

      {bldProtocol === '0x61' && (
        <View style={[S.diagBox, { backgroundColor: cardBg, borderColor: border }]}>
          <Text style={[S.subTitle, { color: txtPri, marginTop: 0 }]}>FIXED PATTERN (0x61)</Text>
          <View style={{ flexDirection: 'row', gap: Spacing.md }}>
            <View style={{ flex: 1 }}>
              <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>PATTERN ID (1–210)</Text>
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}>
                <TouchableOpacity onPress={() => setBldPatternId(String(Math.max(1, (parseInt(bldPatternId)||1) - 1)))} style={{ backgroundColor: border, borderRadius: 6, width: 36, height: 36, justifyContent: 'center', alignItems: 'center' }}>
                  <Text style={{ color: txtPri, fontSize: 18, fontWeight: 'bold' }}>‒</Text>
                </TouchableOpacity>
                <TextInput style={[S.numInput, { flex: 1, backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri, textAlign: 'center' }]} value={bldPatternId} keyboardType="numeric" onChangeText={v => setBldPatternId(String(Math.max(1, Math.min(210, parseInt(v)||1))))} />
                <TouchableOpacity onPress={() => setBldPatternId(String(Math.min(210, (parseInt(bldPatternId)||1) + 1)))} style={{ backgroundColor: border, borderRadius: 6, width: 36, height: 36, justifyContent: 'center', alignItems: 'center' }}>
                  <Text style={{ color: txtPri, fontSize: 18, fontWeight: 'bold' }}>+</Text>
                </TouchableOpacity>
              </View>
            </View>
            <View style={{ flex: 1 }}>
              <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>SPEED (1–100)</Text>
              <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldSpeed.toString()} keyboardType="numeric" onChangeText={v => setBldSpeed(Math.max(1, Math.min(100, parseInt(v)||1)))} />
            </View>
            <View style={{ flex: 1 }}>
              <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>BRIGHTNESS</Text>
              <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldBright} keyboardType="numeric" onChangeText={setBldBright} />
            </View>
          </View>
        </View>
      )}

      {bldProtocol === '0x73' && (
        <View style={[S.diagBox, { backgroundColor: cardBg, borderColor: border }]}>
          <Text style={[S.subTitle, { color: txtPri, marginTop: 0 }]}>SYMPHONY MODE (0x73)</Text>
          <View style={{ flexDirection: 'row', gap: Spacing.md, marginBottom: Spacing.md }}>
            <View style={{ flex: 1 }}>
              <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>MUSIC MODE (1–13)</Text>
              <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}>
                <TouchableOpacity onPress={() => setBldMusicMode(String(Math.max(1, (parseInt(bldMusicMode)||1) - 1)))} style={{ backgroundColor: border, borderRadius: 6, width: 36, height: 36, justifyContent: 'center', alignItems: 'center' }}>
                  <Text style={{ color: txtPri, fontSize: 18, fontWeight: 'bold' }}>‒</Text>
                </TouchableOpacity>
                <TextInput style={[S.numInput, { flex: 1, backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri, textAlign: 'center' }]} value={bldMusicMode} keyboardType="numeric" onChangeText={v => setBldMusicMode(String(Math.max(1, Math.min(13, parseInt(v)||1))))} />
                <TouchableOpacity onPress={() => setBldMusicMode(String(Math.min(13, (parseInt(bldMusicMode)||1) + 1)))} style={{ backgroundColor: border, borderRadius: 6, width: 36, height: 36, justifyContent: 'center', alignItems: 'center' }}>
                  <Text style={{ color: txtPri, fontSize: 18, fontWeight: 'bold' }}>+</Text>
                </TouchableOpacity>
              </View>
            </View>
            <View style={{ flex: 1 }}>
              <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>MIC SRC</Text>
              <TouchableOpacity style={[S.chip, bldMic && { backgroundColor: cyan + '22', borderColor: cyan }, {paddingVertical: Spacing.md, height: 40}]} onPress={() => setBldMic(!bldMic)}>
                 <Text style={{ color: bldMic ? cyan : txtMuted, textAlign: 'center', fontSize: 11, fontWeight: '900' }}>{bldMic ? 'DEVICE' : 'APP'}</Text>
              </TouchableOpacity>
            </View>
          </View>

          {/* Matrix Style — Light Screen vs Light Bar */}
          <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.sm, fontWeight: '900' }}>MATRIX STYLE</Text>
          <View style={{ flexDirection: 'row', gap: Spacing.sm, marginBottom: Spacing.lg }}>
            <TouchableOpacity
              onPress={() => setBldMatrixStyle(0x27)}
              style={{ flex: 1, paddingVertical: Spacing.md, borderRadius: 8, alignItems: 'center',
                backgroundColor: bldMatrixStyle === 0x27 ? '#00E67633' : border,
                borderWidth: 1.5, borderColor: bldMatrixStyle === 0x27 ? '#00E676' : border }}
            >
              <Text style={{ color: bldMatrixStyle === 0x27 ? '#00E676' : txtMuted, fontWeight: '900', fontSize: 11 }}>LIGHT SCREEN</Text>
              <Text style={{ color: bldMatrixStyle === 0x27 ? '#00E676' : txtMuted, fontSize: 9, opacity: 0.6 }}>0x27</Text>
            </TouchableOpacity>
            <TouchableOpacity
              onPress={() => setBldMatrixStyle(0x26)}
              style={{ flex: 1, paddingVertical: Spacing.md, borderRadius: 8, alignItems: 'center',
                backgroundColor: bldMatrixStyle === 0x26 ? '#FF950033' : border,
                borderWidth: 1.5, borderColor: bldMatrixStyle === 0x26 ? '#FF9500' : border }}
            >
              <Text style={{ color: bldMatrixStyle === 0x26 ? '#FF9500' : txtMuted, fontWeight: '900', fontSize: 11 }}>LIGHT BAR</Text>
              <Text style={{ color: bldMatrixStyle === 0x26 ? '#FF9500' : txtMuted, fontSize: 9, opacity: 0.6 }}>0x26</Text>
            </TouchableOpacity>
          </View>

          <View style={{ flexDirection: 'row', gap: Spacing.md, marginBottom: Spacing.md }}>
            <View style={{ flex: 1 }}>
              <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>SENSITIVITY</Text>
              <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldSens} keyboardType="numeric" onChangeText={setBldSens} />
            </View>
            <View style={{ flex: 1 }}>
              <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>BRIGHTNESS</Text>
              <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldBright} keyboardType="numeric" onChangeText={setBldBright} />
            </View>
          </View>

          <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.sm, fontWeight: '900' }}>COLOR 1 (PRIMARY)</Text>
          <QuickColorGrid activeColor={bldColors[0]} onSelect={c => setBldColors([c])} />
          
          <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.sm, fontWeight: '900' }}>COLOR 2 (SECONDARY)</Text>
          <QuickColorGrid activeColor={bldC2} onSelect={setBldC2} />
        </View>
      )}

      {bldProtocol === '0x62' && (
        <View style={[S.diagBox, { backgroundColor: cardBg, borderColor: border }]}>
          <Text style={[S.subTitle, { color: txtPri, marginTop: 0 }]}>HARDWARE SETUP (0x62)</Text>
          <View style={{ flexDirection: 'row', gap: Spacing.md, marginBottom: Spacing.md }}>
            <View style={{ flex: 1 }}>
               <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>IC CHIP</Text>
               <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldIc} onChangeText={setBldIc} placeholder="e.g. WS2812B" />
            </View>
            <View style={{ flex: 1 }}>
               <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>COLOR ORDER</Text>
               <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldOrder} onChangeText={setBldOrder} placeholder="e.g. RGB" />
            </View>
          </View>
          <View style={{ flexDirection: 'row', gap: Spacing.md }}>
             <View style={{ flex: 1 }}>
               <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>TOTAL LEDS</Text>
               <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldPoints} keyboardType="numeric" onChangeText={setBldPoints} />
            </View>
            <View style={{ flex: 1 }}>
               <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs, fontWeight: '900' }}>SEGMENTS</Text>
               <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]} value={bldSegs} keyboardType="numeric" onChangeText={setBldSegs} />
            </View>
          </View>
        </View>
      )}

      <Text style={[S.subTitle, { color: txtMuted }]}>GENERATED PAYLOAD — BYTE ANNOTATIONS</Text>
      {bldResult && (
        <View style={[S.diagBox, { backgroundColor: isDark ? '#05070a' : '#f9fafb', borderColor: border, padding: Spacing.md }]}>
          {bldResult.annotations.map((a, i) => (
            <Text key={i} style={{ color: i === 4 ? TRANSITION_TYPES.find(t=>t.byte===bldTrans)?.color ?? cyan : txtPri, fontSize: 11, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', marginBottom: Spacing.xxs }}>
              {a}
            </Text>
          ))}
        </View>
      )}

      <Text style={[S.subTitle, { color: txtMuted }]}>FULL HEX (EDITABLE)</Text>
          <TextInput
            style={[S.hexInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]}
            defaultValue={bldResult?.hex || ''}
            onChangeText={sendRawHex}
            placeholder="59 00 0F FF 00 00 ..."
            placeholderTextColor={txtMuted}
            multiline
          />
<View style={{ flexDirection: 'row', gap: Spacing.sm, marginTop: Spacing.md }}>
        <TouchableOpacity style={[S.txBtn, { flex: 1, backgroundColor: cyan, borderColor: cyan }]}
          onPress={() => sendRawHex(bldHexOverride, `Builder: trans=0x0${bldTrans.toString(16).toUpperCase()} pts=${bldPoints}`)}>
          <Text style={{ color: '#000', fontWeight: '900', fontSize: 12 }}>TX PAYLOAD</Text>
        </TouchableOpacity>
        <TouchableOpacity style={[S.txBtn, { flex: 1, backgroundColor: border, borderColor: border }]}
          onPress={() => bldResult && setBldHexOverride(bldResult.hex)}>
          <Text style={{ color: txtPri, fontWeight: '900', fontSize: 12 }}>RESET HEX</Text>
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
                const pts = parseInt(bldPoints) || hwPts || 16;
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

  // ─── SNIFFER TAB ────────────────────────────────────────────────────────────
  const renderSnifferTab = () => (
    <View style={{ flex: 1 }}>
      <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', marginBottom: Spacing.md }}>
        <Text style={[S.sectionTitle, { color: txtPri }]}>BLE TRACE</Text>
        <TouchableOpacity style={[S.chip, { borderColor: '#ff404022', backgroundColor: '#ff404011' }]} onPress={() => clearLogs()}>
          <Text style={{ color: '#FF4040', fontSize: 10, fontWeight: '900' }}>CLEAR</Text>
        </TouchableOpacity>
      </View>
      <FlatList
        data={logs}
        keyExtractor={(_, i) => i.toString()}
        style={{ flex: 1, backgroundColor: isDark ? '#05070a' : '#f9fafb', borderRadius: 12, padding: Spacing.sm, borderWidth: 1, borderColor: border }}
        renderItem={({ item }) => (
          <View style={{ marginBottom: Spacing.sm, paddingBottom: Spacing.sm, borderBottomWidth: 1, borderBottomColor: border }}>
            <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, marginBottom: Spacing.xs }}>
              <View style={{ backgroundColor: item.dir === 'TX' ? '#FF404022' : cyan + '22', borderWidth: 1, borderColor: item.dir === 'TX' ? '#FF4040' : cyan, paddingHorizontal: Spacing.sm, paddingVertical: 1, borderRadius: 4 }}>
                <Text style={{ color: item.dir === 'TX' ? '#FF4040' : cyan, fontSize: 9, fontWeight: '900' }}>{item.dir}</Text>
              </View>
              <Text style={{ color: txtMuted, fontSize: 9 }}>{new Date(item.t).toLocaleTimeString()}</Text>
              {item.dev && <Text style={{ color: txtMuted, fontSize: 9 }}>{item.dev.slice(-6)}</Text>}
            </View>
            {item.note && <Text style={{ color: '#FF9500', fontSize: 10, marginBottom: Spacing.xxs, fontWeight: 'bold' }}>{item.note}</Text>}
            <Text style={{ color: txtPri, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', fontSize: 10 }} numberOfLines={2}>
              {item.hex}
            </Text>
          </View>
        )}
        ListEmptyComponent={<Text style={{ color: txtMuted, padding: Spacing.lg, textAlign: 'center', fontSize: 12 }}>No BLE traffic yet. Connect a device and use the other tabs.</Text>}
      />
    </View>
  );

  // ─── Root render ───────────────────────────────────────────────────
  return (
    <View style={{ flex: 1 }}>
      <SafeAreaView style={{ flex: 1, backgroundColor: bg }}>
        {/* Header (Aligned with Programmer) */}
        <View style={[S.header, { borderBottomColor: border, paddingTop: insets.top || 16, paddingBottom: Spacing.lg }]}>
          <TouchableOpacity onPress={onClose} style={S.backBtn}>
            <MaterialCommunityIcons name="arrow-left" size={24} color={cyan} />
          </TouchableOpacity>
          <View style={{ flex: 1 }}>
            <Text style={[Typography.title, { color: cyan, fontSize: 18, textTransform: 'uppercase', letterSpacing: 1.5 }]}>🔬 LED DIAGNOSTIC LAB</Text>
            <Text style={{ color: txtMuted, fontSize: 11, marginTop: Spacing.xxs }}>
              {connectedDevices.length > 0 
                ? `Probing: ${connectedDevices.map(d => d.name || d.id.slice(-5)).join(', ')}` 
                : 'Hardware telemetry & protocol debugger'}
            </Text>
          </View>
          {onToggleDiagnostics && (
            <TouchableOpacity 
              onPress={onToggleDiagnostics}
              style={{
                marginLeft: Spacing.sm,
                paddingHorizontal: Spacing.md,
                paddingVertical: Spacing.sm,
                borderRadius: 20,
                backgroundColor: isDiagnosticsMode ? '#ff404022' : 'rgba(255,255,255,0.05)',
                borderWidth: 1,
                borderColor: isDiagnosticsMode ? '#ff4040' : border,
                justifyContent: 'center',
                alignItems: 'center',
              }}
            >
              <Text style={{ color: isDiagnosticsMode ? '#ff4040' : txtMuted, fontSize: 10, fontWeight: '900' }}>
                {isDiagnosticsMode ? 'TELEMETRY: ON' : 'TELEMETRY: OFF'}
              </Text>
            </TouchableOpacity>
          )}
        </View>

        {/* Tab Bar */}
        <View style={[S.tabBar, { borderBottomColor: border, backgroundColor: bg }]}>
          {(['DEVICES', 'COLOR', 'TRANSITION', 'BUILDER', 'SNIFFER'] as LabTab[]).map(t => {
            const active = tab === t;
            return (
              <TouchableOpacity
                key={t}
                style={[S.tabBtn, active && S.tabBtnActive]}
                onPress={() => setTab(t)}
              >
                <Text style={[S.tabBtnTxt, active && S.tabBtnTxtActive, { color: active ? cyan : txtMuted }]}>
                  {t}
                </Text>
              </TouchableOpacity>
            );
          })}
        </View>

        {/* Content */}
        <View style={[S.content, { backgroundColor: bg }]}>
          {tab === 'DEVICES' && renderDevicesTab()}
          {tab === 'COLOR' && renderColorTab()}
          {tab === 'TRANSITION' && renderTransitionTab()}
          {tab === 'BUILDER' && renderBuilderTab()}
          {tab === 'SNIFFER' && renderSnifferTab()}
        </View>
      </SafeAreaView>
    </View>
  );
}

// ─── Styles ───────────────────────────────────────────────────────────────────

const S = StyleSheet.create({
  root: { flex: 1 },
  header: { 
    flexDirection: 'row', 
    alignItems: 'center', 
    justifyContent: 'space-between', 
    paddingHorizontal: Spacing.lg, 
    paddingBottom: Spacing.lg, 
    borderBottomWidth: 1 
  },
  backBtn: { 
    marginRight: Spacing.lg,
    padding: Spacing.xs,
  },
  title: { color: '#FFF', fontSize: 18, fontWeight: '900', letterSpacing: 1.5 },
  tabBar: { flexDirection: 'row', borderBottomWidth: 1 },
  tabBtn: { 
    flex: 1, 
    paddingVertical: Spacing.lg, 
    alignItems: 'center', 
    borderBottomWidth: 2, 
    borderBottomColor: 'transparent' 
  },
  tabBtnActive: { borderBottomColor: '#00f0ff' },
  tabBtnTxt: { fontSize: 10, fontWeight: '900', letterSpacing: 0.5 },
  tabBtnTxtActive: { color: '#00f0ff' },
  content: { flex: 1, padding: Spacing.lg },
  sectionTitle: { fontSize: 12, fontWeight: '900', letterSpacing: 1, marginBottom: Spacing.sm },
  subTitle: { fontSize: 10, fontWeight: 'bold', letterSpacing: 1, marginBottom: Spacing.md, marginTop: Spacing.xl },
  hint: { fontSize: 11, lineHeight: 16, marginBottom: Spacing.md },
  hwBadge: { 
    flexDirection: 'row', 
    flexWrap: 'wrap', 
    backgroundColor: '#141829', 
    padding: Spacing.md, 
    borderRadius: 12, 
    marginBottom: Spacing.xl, 
    borderWidth: 1, 
    borderColor: '#252c47', 
    alignItems: 'center' 
  },
  hwBadgeLabel: { color: '#8a96b3', fontSize: 11 },
  hwBadgeVal: { fontSize: 11, fontWeight: 'bold' },
  colorBtnRow: { flexDirection: 'row', gap: Spacing.md, marginBottom: Spacing.xl },
  bigColorBtn: { 
    flex: 1, 
    paddingVertical: Spacing.xl, 
    alignItems: 'center', 
    justifyContent: 'center', 
    borderRadius: 12, 
    borderWidth: 1.5 
  },
  diagBox: { 
    backgroundColor: '#141829', 
    borderWidth: 1, 
    borderColor: '#252c47', 
    borderRadius: 12, 
    padding: Spacing.lg, 
    marginBottom: Spacing.md 
  },
  diagLine: { fontSize: 11, lineHeight: 20 },
  sentBox: { 
    backgroundColor: '#0a0d18', 
    borderWidth: 1, 
    borderColor: '#252c47', 
    borderRadius: 12, 
    padding: Spacing.lg, 
    marginTop: Spacing.sm 
  },
  transBtn: { 
    flexDirection: 'row', 
    alignItems: 'center', 
    backgroundColor: '#141829', 
    borderWidth: 1, 
    borderColor: '#252c47', 
    borderRadius: 12, 
    padding: Spacing.lg, 
    marginBottom: Spacing.md 
  },
  transByteBadge: { 
    width: 52, 
    height: 52, 
    alignItems: 'center', 
    justifyContent: 'center', 
    borderRadius: 10, 
    borderWidth: 1 
  },
  annotationsBox: { 
    backgroundColor: '#0a0d18', 
    borderWidth: 1, 
    borderColor: '#252c47', 
    borderRadius: 12, 
    padding: Spacing.lg, 
    marginBottom: Spacing.md 
  },
  numInput: { 
    backgroundColor: '#141829', 
    color: '#FFF', 
    borderWidth: 1, 
    borderColor: '#252c47', 
    borderRadius: 10, 
    padding: Spacing.md, 
    fontSize: 14, 
    textAlign: 'center' 
  },
  hexInput: { 
    backgroundColor: '#0a0d18', 
    color: '#00f0ff', 
    borderWidth: 1, 
    borderColor: '#252c47', 
    borderRadius: 12, 
    padding: Spacing.lg, 
    fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', 
    fontSize: 11, 
    minHeight: 100 
  },
  chip: { 
    paddingHorizontal: Spacing.lg, 
    paddingVertical: Spacing.sm, 
    borderRadius: 20, 
    backgroundColor: '#141829', 
    borderWidth: 1, 
    borderColor: '#252c47' 
  },
  chipActive: { 
    backgroundColor: 'rgba(0,240,255,0.1)', 
    borderColor: '#00f0ff' 
  },
  txBtn: { 
    backgroundColor: '#00ccff', 
    justifyContent: 'center', 
    alignItems: 'center', 
    paddingVertical: Spacing.lg, 
    borderRadius: 12, 
    marginTop: Spacing.md 
  },
  presetBtn: { 
    backgroundColor: '#141829', 
    borderWidth: 1, 
    borderColor: '#252c47', 
    borderRadius: 12, 
    padding: Spacing.lg 
  },
});
