import React, { useState, useEffect } from 'react';
import { ScrollView, Text, TouchableOpacity, View, TextInput, Platform } from 'react-native';
import { Spacing } from '../../../../theme/theme';
import { useDiagnosticLabStyles } from './DiagnosticLabStyles';
import { DiagnosticLabHwBadge } from './DiagnosticLabHwBadge';
import { TRANSITION_TYPES } from './DiagnosticLabConstants';
import { QuickColorGrid } from './DiagnosticLabQuickColorGrid';
import { ZenggeProtocol } from '../../../../utils/ZenggeProtocol';
import { OpcodeStatus, TRACKED_OPCODES, TestVerdict } from '../../../../hooks/useDiagnosticLog';

interface OracleTabProps {
  targetDeviceId: string | null;
  connectedDevices: any[];
  hwSettings: any;
  hwPts: number;
  coverage: Record<string, OpcodeStatus>;
  testLog: any[];
  setVerdict: (id: string, opcode: string, verdict: TestVerdict) => void;
  setLastVerdict: (opcode: string, verdict: TestVerdict) => void;
  clearTestLog: () => void;
  transmit: (cmd: number[], label: string, opcode?: string) => void;
}

export function DiagnosticLabOracleTab({
  targetDeviceId,
  connectedDevices,
  hwSettings,
  hwPts,
  coverage,
  testLog,
  setVerdict,
  setLastVerdict,
  clearTestLog,
  transmit,
}: OracleTabProps) {
  const { S, txtPri, txtMuted, cardBg, border, isDark, cyan } = useDiagnosticLabStyles();

  /** Auto-stream 0x74 magnitude 200 every 100ms when active (for mic shootout). */
  const [streamActive, setStreamActive] = useState(false);
  useEffect(() => {
    if (!streamActive) return;
    const id = setInterval(() => {
      transmit(ZenggeProtocol.sendMusicMagnitude(200), '0x74 AUTO-STREAM mag=200', '0x74');
    }, 100);
    return () => clearInterval(id);
  }, [streamActive, transmit]);

  // Enable condemned opcode gate for this lab — MUST reset on unmount
  useEffect(() => {
    ZenggeProtocol.DIAGNOSTIC_MODE_ENABLED = true;
    return () => { ZenggeProtocol.DIAGNOSTIC_MODE_ENABLED = false; };
  }, []);

  const [expandedP2, setExpandedP2] = useState<string | null>(null);
  
  // 0x41 Settled Mode panel
  const [p41EffectId, setP41EffectId]   = useState(1);
  const [p41Speed, setP41Speed]         = useState(50);
  const [p41Bright, setP41Bright]       = useState(100);
  const [p41Dir, setP41Dir]             = useState(0x01);
  const [p41Color1, setP41Color1]       = useState<{r:number;g:number;b:number}>({r:255,g:0,b:0});
  const [p41Color2, setP41Color2]       = useState<{r:number;g:number;b:number}>({r:0,g:0,b:255});
  const [p41SweepResults, setP41SweepResults] = useState<Record<string, 'WORKS'|'NO_EFFECT'|'CRASHED'>>({});
  
  const [ttSweepResults, setTtSweepResults] = useState<Record<string, 'WORKS'|'NO_EFFECT'|'CRASHED'>>({});
  
  // 0x43 Multi-Sequence panel
  const [p43Ids, setP43Ids]             = useState<number[]>([1,5,10]);
  const [p43Speed, setP43Speed]         = useState(50);
  const [p43Bright, setP43Bright]       = useState(100);
  
  // 0x53 Live Stream
  const [p53Fps, setP53Fps]             = useState(10);
  const [p53Active, setP53Active]       = useState(false);
  const [p53GradStart, setP53GradStart] = useState({r:0,g:255,b:255});
  const [p53GradEnd, setP53GradEnd]     = useState({r:255,g:0,b:255});

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

  // 0x56/57/58 Scene Management
  const [sceneSlot, setSceneSlot]       = useState(0);

  const opcodeStatusConfig: Record<OpcodeStatus, { emoji: string; color: string }> = {
    UNTESTED:  { emoji: '⬜', color: txtMuted },
    PASS:      { emoji: '🟩', color: '#00CC88' },
    FAIL:      { emoji: '🟥', color: '#FF4040' },
    AMBIGUOUS: { emoji: '🟨', color: '#FF9500' },
  };

  const QUICK_TESTS = [
    {
      group: '🔋 POWER (0x71)',
      tests: [
        {
          label: '🟢 PWR ON',
          opcode: '0x71',
          note: '0x71 POWER ON — [71 23 0F A3] wrapped. LEDs/device should power on.',
          bytes: () => ZenggeProtocol.setPower(true),
        },
        {
          label: '🔴 PWR OFF',
          opcode: '0x71',
          note: '0x71 POWER OFF — [71 24 0F A4] wrapped. Device should cut power.',
          bytes: () => ZenggeProtocol.setPower(false),
        },
      ],
    },
    {
      group: '🎨 RBM CEILING (0x42)',
      tests: [
        {
          label: 'EFFECT #1',
          opcode: '0x42',
          note: '0x42 effectId=1 (min) — [42 01 32 64 D3]. Should play first RBM effect.',
          bytes: () => ZenggeProtocol.setCustomRbm(1, 50, 100),
        },
        {
          label: 'EFFECT #50',
          opcode: '0x42',
          note: '0x42 effectId=50 (mid) — [42 32 32 64 04]. Mid-range effect.',
          bytes: () => ZenggeProtocol.setCustomRbm(50, 50, 100),
        },
        {
          label: 'EFFECT #100',
          opcode: '0x42',
          note: '0x42 effectId=100 (ceiling). Should play normally — last valid ID.',
          bytes: () => ZenggeProtocol.setCustomRbm(100, 50, 100),
        },
        {
          label: 'EFFECT #101 ⚠️',
          opcode: '0x42',
          note: '0x42 effectId=101 (OVER CEILING). Expect glitch/undefined behavior.',
          bytes: () => ZenggeProtocol.setCustomRbm(101, 50, 100),
        },
      ],
    },
    {
      group: '🎵 MIC SHOOTOUT (0x73) — SMOKING GUN A',
      tests: [
        {
          label: 'MIC=0x26 ★',
          opcode: '0x73',
          note: '0x73 APK app mic byte — 0x26, isOn=0x01. Send then enable AUTO-STREAM ⇓',
          bytes: () => {
            const raw = [0x73, 0x01, 0x26, 0x01, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x80, 0x64];
            return ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]);
          },
        },
        {
          label: 'MIC=0x27 ★',
          opcode: '0x73',
          note: '0x73 APK device mic byte — 0x27, isOn=0x01. Reacts to ambient sound WITHOUT 0x74.',
          bytes: () => {
            const raw = [0x73, 0x01, 0x27, 0x01, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x80, 0x64];
            return ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]);
          },
        },
        {
          label: 'isOn=0x00 (OFF)',
          opcode: '0x73',
          note: '0x73 isOn byte set to 0x00 — LEDs should DEACTIVATE music mode. Confirms isOn is live.',
          bytes: () => {
            const raw = [0x73, 0x01, 0x26, 0x00, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x80, 0x64];
            return ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]);
          },
        },
        {
          label: 'MIC=0x00 (OLD ❌)',
          opcode: '0x73',
          note: '0x73 OLD wrong mic byte — 0x00. With auto-stream ON, LEDs should NOT pulse (confirms bug).',
          bytes: () => {
            const raw = [0x73, 0x01, 0x00, 0x01, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x80, 0x64];
            return ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]);
          },
        },
        {
          label: 'isOn MISSING (12B)',
          opcode: '0x73',
          note: '0x73 OLD 12-byte format without isOn byte — should NOT activate music mode.',
          bytes: () => {
            const raw = [0x73, 0x00, 0x01, 0xFF, 0x00, 0x00, 0x00, 0xFF, 0x00, 0x80, 0x64];
            return ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]);
          },
        },
        {
          label: 'MAG ×1 (0x74)',
          opcode: '0x74',
          note: '0x74 Single magnitude=200. Fire once after MIC=0x26 to see one pulse.',
          bytes: () => ZenggeProtocol.sendMusicMagnitude(200),
        },
      ],
    },
    {
      group: '📞 SCENE FORMAT (0x51) — SMOKING GUN B',
      tests: [
        {
          label: '9B COMPACT ★',
          opcode: '0x51',
          note: '0x51 compact 9B/slot variable-length — 0xA2 format. Observe if 0xA3 hardware cycles.',
          bytes: () => ZenggeProtocol.setCustomMode([
            { mode: 1, speed: 10, color1: { r:255,g:0,b:0 }, color2: { r:0,g:0,b:255 } },
            { mode: 5, speed: 10, color1: { r:0,g:255,b:0 }, color2: { r:255,g:255,b:0 } },
          ]),
        },
        {
          label: '10B EXTENDED ★',
          opcode: '0x51',
          note: '0x51 323B fixed 10B/slot ×32 — APK-verified 0xA3 format. Should cycle cleanly.',
          bytes: () => ZenggeProtocol.setCustomModeExtended([
            { mode: 1, speed: 10, color1: { r:255,g:0,b:0 }, color2: { r:0,g:0,b:255 } },
            { mode: 5, speed: 10, color1: { r:0,g:255,b:0 }, color2: { r:255,g:255,b:0 } },
          ]),
        },
      ],
    },
    {
      group: '📬 SCENE MGMT (0x56/57/58)',
      tests: [
        {
          label: 'QUERY (0x58)',
          opcode: '0x58',
          note: '0x58 Scene state query — watch RX observer for response bytes.',
          bytes: () => {
            const raw = [0x58, 0xF0];
            return ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]);
          },
        },
        {
          label: 'ACTIVATE #0 (0x57)',
          opcode: '0x57',
          note: '0x57 Activate scene slot 0, speed=50, brightness=100.',
          bytes: () => {
            const raw = [0x57, 0x00, 0x32, 0x64];
            return ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]);
          },
        },
        {
          label: 'DELETE #0 (0x56)',
          opcode: '0x56',
          note: '0x56 Delete scene slot 0 — 15-byte payload.',
          bytes: () => {
            const raw = [0x56, 0x00, 0,0,0,0,0,0,0,0,0,0,0,0];
            return ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]);
          },
        },
      ],
    },
    {
      group: '📊 QUERY HW (0x63)',
      tests: [
        {
          label: 'POLL 0x63',
          opcode: '0x63',
          note: '0x63 Hardware settings query — response parsed live in RX observer (DEVICES tab).',
          bytes: () => ZenggeProtocol.queryHardwareSettings?.() ?? [],
        },
      ],
    },
  ];

  const verdictConfig: Record<TestVerdict & string, { label: string; color: string; bg: string }> = {
    PASS:      { label: '✅ PASS',      color: '#00CC88', bg: '#00CC8822' },
    FAIL:      { label: '❌ FAIL',      color: '#FF4040', bg: '#FF404022' },
    AMBIGUOUS: { label: '⚠️ AMBI',    color: '#FF9500', bg: '#FF950022' },
  };

  return (
    <ScrollView contentContainerStyle={{ paddingBottom: Spacing.xxxl }}>
      <Text style={[S.sectionTitle, { color: cyan }]}>🔬 PROTOCOL ORACLE</Text>
      <Text style={[S.hint, { color: txtMuted }]}>
        Hardware truth verification suite for 0xA3 (product_id=163).{`\n`}
        Fire tests → observe LEDs → log verdict. Phase 1 Smoking Guns marked ★.
      </Text>
      
      <DiagnosticLabHwBadge 
        targetDeviceId={targetDeviceId}
        connectedDevices={connectedDevices}
        hwSettings={hwSettings}
      />

      {/* ── Coverage Matrix ───────────────────────────── */}
      <Text style={[S.subTitle, { color: txtMuted }]}>OPCODE COVERAGE MATRIX</Text>
      <View style={[S.diagBox, { flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.sm, padding: Spacing.md }]}>
        {TRACKED_OPCODES.map(op => {
          const status = coverage[op];
          const cfg = opcodeStatusConfig[status] || opcodeStatusConfig.UNTESTED;
          return (
            <View key={op} style={{ alignItems: 'center', width: 52 }}>
              <Text style={{ fontSize: 16 }}>{cfg.emoji}</Text>
              <Text style={{ color: cfg.color, fontSize: 9, fontWeight: '900', marginTop: 2 }}>{op}</Text>
            </View>
          );
        })}
      </View>
      <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.sm, marginBottom: Spacing.xl }}>
        {(Object.entries(opcodeStatusConfig) as [OpcodeStatus, { emoji: string; color: string }][]).map(([k, v]) => (
          <Text key={k} style={{ color: v.color, fontSize: 10 }}>{v.emoji} {k}</Text>
        ))}
        <TouchableOpacity onPress={clearTestLog} style={{ marginLeft: 'auto' }}>
          <Text style={{ color: '#FF4040', fontSize: 10, fontWeight: '900' }}>RESET ALL</Text>
        </TouchableOpacity>
      </View>

      {/* ── Quick Test Groups ─────────────────────────── */}
      {QUICK_TESTS.map((group, gi) => (
        <View key={gi}>
          <Text style={[S.subTitle, { color: txtMuted }]}>{group.group}</Text>
          <View style={{ gap: Spacing.sm, marginBottom: Spacing.lg }}>
            {group.tests.map((test, ti) => {
              const latestEntry = testLog.find(e => e.opcode === test.opcode && e.label === test.note);
              const currentVerdict = latestEntry?.verdict ?? null;
              return (
                <View key={ti} style={[S.diagBox, { padding: Spacing.md }]}>
                  <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: Spacing.sm }}>
                    <View style={{ flex: 1 }}>
                      <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}>
                        <View style={{ backgroundColor: border, paddingHorizontal: Spacing.sm, paddingVertical: 2, borderRadius: 4 }}>
                          <Text style={{ color: cyan, fontSize: 9, fontWeight: '900', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>{test.opcode}</Text>
                        </View>
                        <Text style={{ color: txtPri, fontWeight: '900', fontSize: 12 }}>{test.label}</Text>
                      </View>
                      <Text style={{ color: txtMuted, fontSize: 10, marginTop: Spacing.xs }}>{test.note}</Text>
                    </View>
                    <TouchableOpacity
                      onPress={() => transmit(test.bytes(), test.note, test.opcode)}
                      style={{ backgroundColor: cyan + '22', borderWidth: 1, borderColor: cyan, paddingHorizontal: Spacing.md, paddingVertical: Spacing.sm, borderRadius: 8, marginLeft: Spacing.md }}
                    >
                      <Text style={{ color: cyan, fontWeight: '900', fontSize: 11 }}>SEND</Text>
                    </TouchableOpacity>
                  </View>
                  {/* Verdict annotation — shows after any test with this opcode/label was fired */}
                  <View style={{ flexDirection: 'row', gap: Spacing.sm, marginTop: Spacing.xs }}>
                    {(['PASS', 'FAIL', 'AMBIGUOUS'] as const).map(v => {
                      const vcfg = verdictConfig[v];
                      const isActive = currentVerdict === v;
                      return (
                        <TouchableOpacity
                          key={v}
                          onPress={() => {
                            if (latestEntry) setVerdict(latestEntry.id, test.opcode, v);
                            else setLastVerdict(test.opcode, v);
                          }}
                          style={{
                            flex: 1, paddingVertical: Spacing.sm, borderRadius: 6, alignItems: 'center',
                            backgroundColor: isActive ? vcfg.bg : 'transparent',
                            borderWidth: 1, borderColor: isActive ? vcfg.color : border,
                          }}
                        >
                          <Text style={{ color: isActive ? vcfg.color : txtMuted, fontSize: 10, fontWeight: '900' }}>{vcfg.label}</Text>
                        </TouchableOpacity>
                      );
                    })}
                  </View>
                </View>
              );
            })}
          </View>
        </View>
      ))}

      {/* PHASE 2 — EXTENDED PANELS (APK Hypothesis) */}
      <Text style={[S.subTitle, { color: '#9D4EFF' }]}>🧪 PHASE 2 EXTENDED PANELS</Text>
      <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.md }}>
        APK-inferred builders for opcodes not yet confirmed. Tap header to expand. All are labeled [HYPOTHESIS].
      </Text>

      {/* 🔬 0x59 TRANSITION TYPE SWEEP — 0x00 through 0x06 */}
      <TouchableOpacity
        onPress={() => setExpandedP2(expandedP2 === 'TT_SWEEP' ? null : 'TT_SWEEP')}
        style={[S.diagBox, { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center',
          borderColor: expandedP2 === 'TT_SWEEP' ? '#FBBF24' : border,
          borderWidth: expandedP2 === 'TT_SWEEP' ? 1.5 : 1 }]}
      >
        <View>
          <Text style={{ color: '#FBBF24', fontWeight: '900', fontSize: 13 }}>🔬 0x59 TransitionType Sweep (0x00–0x06)</Text>
          <Text style={{ color: txtMuted, fontSize: 10, marginTop: 2 }}>
            Fire each byte · log result · confirm or deny 0x06 Twinkle
          </Text>
        </View>
        <Text style={{ color: '#FBBF24', fontSize: 18 }}>{expandedP2 === 'TT_SWEEP' ? '▲' : '▼'}</Text>
      </TouchableOpacity>
      {expandedP2 === 'TT_SWEEP' && (
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
                      const numPoints = hwPts;
                      const totalLen = numPoints * 3 + 9;
                      const raw = new Array(totalLen).fill(0);
                      raw[0] = 0x59; raw[1] = (totalLen >> 8) & 0xFF; raw[2] = totalLen & 0xFF;
                      let idx = 3;
                      for (let i = 0; i < numPoints; i++) {
                        raw[idx++] = p41Color1.r; raw[idx++] = p41Color1.g; raw[idx++] = p41Color1.b;
                      }
                      raw[idx++] = (numPoints >> 8) & 0xFF; raw[idx++] = numPoints & 0xFF;
                      raw[idx++] = tt.byte & 0xFF; raw[idx++] = 50; raw[idx++] = 1;
                      raw[idx] = ZenggeProtocol.calculateChecksum(raw.slice(0, totalLen - 1));
                      transmit(ZenggeProtocol.wrapCommand(raw), `0x59 transType=${key} ${tt.label}`, '0x59');
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
      )}

      {/* 🎨 0x41 SETTLED MODE — Effect ID Sweep (1–33) */}
      <TouchableOpacity
        onPress={() => setExpandedP2(expandedP2 === '0x41' ? null : '0x41')}
        style={[S.diagBox, { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center',
          borderColor: expandedP2 === '0x41' ? '#9D4EFF' : border,
          borderWidth: expandedP2 === '0x41' ? 1.5 : 1 }]}
      >
        <View>
          <Text style={{ color: '#9D4EFF', fontWeight: '900', fontSize: 13 }}>🎨 0x41 Settled Mode — Effect ID Sweep (1–33)</Text>
          <Text style={{ color: txtMuted, fontSize: 10, marginTop: 2 }}>
            FG/BG · speed · dir · tap each ID · log verdict
          </Text>
        </View>
        <Text style={{ color: '#9D4EFF', fontSize: 18 }}>{expandedP2 === '0x41' ? '▲' : '▼'}</Text>
      </TouchableOpacity>
      {expandedP2 === '0x41' && (
        <View style={[S.diagBox, { borderColor: '#9D4EFF', borderWidth: 1 }]}>
          <Text style={{ color: '#FF9500', fontSize: 10, fontWeight: '700', marginBottom: Spacing.md }}>
            ⚠️ LAB ONLY — 0x41 is a condemned production opcode. Oracle use only.{'\n'}
            APK truth: [0x41, id, FG.R,G,B, BG.R,G,B, speed, dir, 0x00, 0xF0, CS] — 13 bytes
          </Text>

          {/* Summary grid — 33 cells */}
          <Text style={{ color: txtMuted, fontSize: 10, fontWeight: '900', marginBottom: Spacing.xs }}>RESULT GRID</Text>
          <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 4, marginBottom: Spacing.lg }}>
            {Array.from({ length: 33 }, (_, i) => i + 1).map(id => {
              const res = p41SweepResults[String(id)];
              const cellColor = res === 'WORKS' ? '#00CC88' : res === 'NO_EFFECT' ? '#FF9500' : res === 'CRASHED' ? '#FF4040' : border;
              return (
                <View key={id} style={{ width: 36, height: 36, borderRadius: 6, backgroundColor: cellColor + '33', borderWidth: 1, borderColor: cellColor, justifyContent: 'center', alignItems: 'center' }}>
                  <Text style={{ color: cellColor, fontSize: 10, fontWeight: '900' }}>{id}</Text>
                </View>
              );
            })}
          </View>

          {/* Controls row */}
          <View style={{ flexDirection: 'row', gap: Spacing.sm, marginBottom: Spacing.md }}>
            <View style={{ flex: 1 }}>
              <Text style={{ color: txtMuted, fontSize: 10, fontWeight: '900', marginBottom: Spacing.xs }}>SPEED (1–255)</Text>
              <TextInput style={[S.numInput, { backgroundColor: isDark ? '#05070a' : '#fff', color: txtPri }]}
                value={String(p41Speed)} keyboardType="numeric"
                onChangeText={v => setP41Speed(Math.max(1, Math.min(255, parseInt(v) || 1)))} />
            </View>
            <View style={{ flex: 1 }}>
              <Text style={{ color: txtMuted, fontSize: 10, fontWeight: '900', marginBottom: Spacing.xs }}>DIRECTION</Text>
              <View style={{ flexDirection: 'row', gap: Spacing.xs }}>
                {[{ v: 0x00, l: 'FWD' }, { v: 0x01, l: 'REV' }].map(d => (
                  <TouchableOpacity key={d.v} onPress={() => setP41Dir(d.v)}
                    style={[S.chip, p41Dir === d.v && { backgroundColor: '#9D4EFF22', borderColor: '#9D4EFF' }, { flex: 1, height: 40 }]}>
                    <Text style={{ color: p41Dir === d.v ? '#9D4EFF' : txtMuted, fontSize: 11, textAlign: 'center', fontWeight: '900' }}>{d.l}</Text>
                  </TouchableOpacity>
                ))}
              </View>
            </View>
          </View>
          <Text style={{ color: txtMuted, fontSize: 10, fontWeight: '900', marginBottom: Spacing.xs }}>FG COLOR</Text>
          <QuickColorGrid activeColor={p41Color1} onSelect={setP41Color1} />
          <Text style={{ color: txtMuted, fontSize: 10, fontWeight: '900', marginBottom: Spacing.xs }}>BG COLOR</Text>
          <QuickColorGrid activeColor={p41Color2} onSelect={setP41Color2} />

          {/* 33-ID grid — tap to send + verdict buttons inline */}
          <Text style={{ color: txtMuted, fontSize: 10, fontWeight: '900', marginTop: Spacing.md, marginBottom: Spacing.xs }}>TAP TO SEND EACH EFFECT</Text>
          {Array.from({ length: 33 }, (_, i) => i + 1).map(id => {
            const res = p41SweepResults[String(id)];
            const setRes = (r: 'WORKS' | 'NO_EFFECT' | 'CRASHED') =>
              setP41SweepResults(prev => ({ ...prev, [String(id)]: r }));
            const resColor = res === 'WORKS' ? '#00CC88' : res === 'NO_EFFECT' ? '#FF9500' : res === 'CRASHED' ? '#FF4040' : border;
            return (
              <View key={id} style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm, marginBottom: Spacing.sm,
                borderBottomWidth: 1, borderBottomColor: border, paddingBottom: Spacing.sm }}>
                <TouchableOpacity
                  onPress={() => transmit(
                    ZenggeProtocol.setSettledMode(id, p41Color1, p41Color2, p41Speed, p41Dir as 0 | 1),
                    `0x41 id=${id} spd=${p41Speed} dir=${p41Dir}`, '0x41'
                  )}
                  style={{ width: 48, height: 40, borderRadius: 8, backgroundColor: resColor + '22',
                    borderWidth: 1.5, borderColor: resColor, justifyContent: 'center', alignItems: 'center' }}
                >
                  <Text style={{ color: resColor, fontWeight: '900', fontSize: 12 }}>{id}</Text>
                  <Text style={{ color: resColor, fontSize: 8 }}>SEND</Text>
                </TouchableOpacity>
                {(['WORKS', 'NO_EFFECT', 'CRASHED'] as const).map(r => (
                  <TouchableOpacity key={r} onPress={() => setRes(r)}
                    style={{ flex: 1, paddingVertical: Spacing.sm, borderRadius: 6, alignItems: 'center',
                      backgroundColor: res === r ? (r === 'WORKS' ? '#00CC8822' : r === 'CRASHED' ? '#FF404022' : '#FF950022') : 'transparent',
                      borderWidth: 1, borderColor: res === r ? (r === 'WORKS' ? '#00CC88' : r === 'CRASHED' ? '#FF4040' : '#FF9500') : border }}
                  >
                    <Text style={{ color: res === r ? (r === 'WORKS' ? '#00CC88' : r === 'CRASHED' ? '#FF4040' : '#FF9500') : txtMuted, fontSize: 9, fontWeight: '900' }}>
                      {r === 'WORKS' ? '✅' : r === 'NO_EFFECT' ? '⚠️' : '💀'}
                    </Text>
                  </TouchableOpacity>
                ))}
              </View>
            );
          })}
          <TouchableOpacity onPress={() => setP41SweepResults({})} style={{ alignSelf: 'flex-end', marginTop: Spacing.sm }}>
            <Text style={{ color: '#FF4040', fontSize: 10, fontWeight: '900' }}>RESET RESULTS</Text>
          </TouchableOpacity>
        </View>
      )}

      {/* ── 0x43 Multi-Sequence Panel ─────────────────────── */}
      <TouchableOpacity
        onPress={() => setExpandedP2(expandedP2 === '0x43' ? null : '0x43')}
        style={[S.diagBox, { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', borderColor: expandedP2 === '0x43' ? '#FF69B4' : border, borderWidth: expandedP2 === '0x43' ? 1.5 : 1 }]}
      >
        <View>
          <Text style={{ color: '#FF69B4', fontWeight: '900', fontSize: 13 }}>🎞️ 0x43 Multi-Sequence [HYPOTHESIS]</Text>
          <Text style={{ color: txtMuted, fontSize: 10, marginTop: 2 }}>tap up to 50 effect IDs · speed · brightness</Text>
        </View>
        <Text style={{ color: '#FF69B4', fontSize: 18 }}>{expandedP2 === '0x43' ? '▲' : '▼'}</Text>
      </TouchableOpacity>
      {expandedP2 === '0x43' && (
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
      )}

      {/* ── 0x53 Live Pixel Stream Panel ─────────────────── */}
      <TouchableOpacity
        onPress={() => setExpandedP2(expandedP2 === '0x53' ? null : '0x53')}
        style={[S.diagBox, { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', borderColor: expandedP2 === '0x53' ? '#00CC88' : border, borderWidth: expandedP2 === '0x53' ? 1.5 : 1 }]}
      >
        <View>
          <Text style={{ color: '#00CC88', fontWeight: '900', fontSize: 13 }}>🎬 0x53 Live Pixel Stream [HYPOTHESIS]</Text>
          <Text style={{ color: txtMuted, fontSize: 10, marginTop: 2 }}>gradient frame loop · configurable FPS · start/stop</Text>
        </View>
        <Text style={{ color: '#00CC88', fontSize: 18 }}>{expandedP2 === '0x53' ? '▲' : '▼'}</Text>
      </TouchableOpacity>
      {expandedP2 === '0x53' && (
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
      )}

      {/* ── 0x56/57/58 Scene Management Panel ────────────── */}
      <TouchableOpacity
        onPress={() => setExpandedP2(expandedP2 === 'SCENE' ? null : 'SCENE')}
        style={[S.diagBox, { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', borderColor: expandedP2 === 'SCENE' ? '#FFD700' : border, borderWidth: expandedP2 === 'SCENE' ? 1.5 : 1 }]}
      >
        <View>
          <Text style={{ color: '#FFD700', fontWeight: '900', fontSize: 13 }}>📁 0x56/57/58 Scene Management</Text>
          <Text style={{ color: txtMuted, fontSize: 10, marginTop: 2 }}>slot picker · delete · activate · query</Text>
        </View>
        <Text style={{ color: '#FFD700', fontSize: 18 }}>{expandedP2 === 'SCENE' ? '▲' : '▼'}</Text>
      </TouchableOpacity>
      {expandedP2 === 'SCENE' && (
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
                const raw = [0x58, 0xF0];
                transmit(ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]), `0x58 QUERY scene state`, '0x58');
              }}
            >
              <Text style={{ color: '#FFD700', fontWeight: '900', fontSize: 12 }}>0x58 QUERY — watch RX for state bytes</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={[S.txBtn, { backgroundColor: '#00CC8822', borderColor: '#00CC88' }]}
              onPress={() => {
                const raw = [0x57, sceneSlot & 0xFF, 0x32, 0x64];
                transmit(ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]), `0x57 ACTIVATE slot=${sceneSlot}`, '0x57');
              }}
            >
              <Text style={{ color: '#00CC88', fontWeight: '900', fontSize: 12 }}>0x57 ACTIVATE slot {sceneSlot} (speed=50, bright=100)</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={[S.txBtn, { backgroundColor: '#FF404022', borderColor: '#FF4040' }]}
              onPress={() => {
                const raw = [0x56, sceneSlot & 0xFF, 0,0,0,0,0,0,0,0,0,0,0,0];
                transmit(ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]), `0x56 DELETE slot=${sceneSlot}`, '0x56');
              }}
            >
              <Text style={{ color: '#FF4040', fontWeight: '900', fontSize: 12 }}>0x56 DELETE slot {sceneSlot}</Text>
            </TouchableOpacity>
          </View>
        </View>
      )}

      {/* ── 0x74 Auto-Stream Toggle ────────────────────────── */}
      <Text style={[S.subTitle, { color: '#FF9500' }]}>⚡ 0x74 AUTO-STREAM (MIC SHOOTOUT TOOL)</Text>
      <View style={[S.diagBox, { borderColor: streamActive ? '#FF9500' : border, borderWidth: streamActive ? 2 : 1, padding: Spacing.md }]}>
        <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }}>
          <View style={{ flex: 1 }}>
            <Text style={{ color: streamActive ? '#FF9500' : txtPri, fontWeight: '900', fontSize: 13 }}>
              {streamActive ? '🟠 STREAMING magnitude=200 @ 10Hz' : '○ Stream stopped'}
            </Text>
            <Text style={{ color: txtMuted, fontSize: 10, marginTop: Spacing.xs }}>
              Step 1: Send MIC=0x26 ★ above. Step 2: Toggle ON. Observe LED pulse pattern.
            </Text>
          </View>
          <TouchableOpacity
            onPress={() => setStreamActive(prev => !prev)}
            style={{
              paddingHorizontal: Spacing.xl, paddingVertical: Spacing.lg, borderRadius: 10,
              backgroundColor: streamActive ? '#FF9500' : border,
              borderWidth: 1, borderColor: streamActive ? '#FF9500' : border,
              marginLeft: Spacing.md,
            }}
          >
            <Text style={{ color: streamActive ? '#000' : txtPri, fontWeight: '900', fontSize: 12 }}>
              {streamActive ? 'STOP' : 'START'}
            </Text>
          </TouchableOpacity>
        </View>
        {streamActive && (
          <View style={{ marginTop: Spacing.sm, flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}>
            <View style={{ width: 8, height: 8, borderRadius: 4, backgroundColor: '#FF9500' }} />
            <Text style={{ color: '#FF9500', fontSize: 10, fontWeight: 'bold' }}>
              Firing 0x74 0xC8 every 100ms — check SNIFFER tab for TX rate
            </Text>
          </View>
        )}
      </View>

      {/* ── Test Session Log ────────────────────────── */}
      <Text style={[S.subTitle, { color: txtMuted }]}>TEST SESSION LOG ({testLog.length}/200)</Text>
      {testLog.length === 0 ? (
        <Text style={[S.hint, { color: txtMuted }]}>No tests fired yet. Tap SEND on any test above.</Text>
      ) : (
        <View style={{ gap: Spacing.sm }}>
          {testLog.slice(0, 30).map((entry) => {
            const vcfg = entry.verdict ? verdictConfig[entry.verdict] : null;
            return (
              <View key={entry.id} style={[S.diagBox, { padding: Spacing.md, borderColor: vcfg?.color ?? border }]}>
                <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', marginBottom: Spacing.xs }}>
                  <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.sm }}>
                    <View style={{ backgroundColor: border, paddingHorizontal: Spacing.sm, paddingVertical: 2, borderRadius: 4 }}>
                      <Text style={{ color: cyan, fontSize: 9, fontWeight: '900', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace' }}>{entry.opcode}</Text>
                    </View>
                    {vcfg && (
                      <View style={{ backgroundColor: vcfg.bg, paddingHorizontal: Spacing.sm, paddingVertical: 2, borderRadius: 4, borderWidth: 1, borderColor: vcfg.color }}>
                        <Text style={{ color: vcfg.color, fontSize: 9, fontWeight: '900' }}>{vcfg.label}</Text>
                      </View>
                    )}
                    {!entry.verdict && (
                      <View style={{ backgroundColor: border, paddingHorizontal: Spacing.sm, paddingVertical: 2, borderRadius: 4 }}>
                        <Text style={{ color: txtMuted, fontSize: 9, fontWeight: '900' }}>⏳ PENDING</Text>
                      </View>
                    )}
                  </View>
                  <Text style={{ color: txtMuted, fontSize: 9 }}>{new Date(entry.timestamp).toLocaleTimeString()}</Text>
                </View>
                <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.xs }}>{entry.label}</Text>
                <Text style={{ color: cyan, fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', fontSize: 9 }} numberOfLines={2}>
                  TX: {entry.txHex}
                </Text>
                {entry.rxHex && (
                  <Text style={{ color: '#00E676', fontFamily: Platform.OS === 'ios' ? 'Menlo' : 'monospace', fontSize: 9, marginTop: Spacing.xxs }} numberOfLines={2}>
                    RX: {entry.rxHex}
                  </Text>
                )}
              </View>
            );
          })}
        </View>
      )}
    </ScrollView>
  );
}
