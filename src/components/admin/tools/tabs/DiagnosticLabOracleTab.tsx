import React, { useState, useEffect } from 'react';
import { ScrollView, Text, TouchableOpacity, View, TextInput, Platform } from 'react-native';
import { Spacing } from '../../../../theme/theme';
import { useDiagnosticLabStyles } from './DiagnosticLabStyles';
import { DiagnosticLabHwBadge } from './DiagnosticLabHwBadge';
import { TRANSITION_TYPES } from './DiagnosticLabConstants';
import { QuickColorGrid } from './DiagnosticLabQuickColorGrid';
import { ZenggeProtocol } from '../../../../protocols/ZenggeProtocol';
import { SK8LYTZ_TEMPLATES } from '../../../../protocols/PatternEngine';
import { OpcodeStatus, TRACKED_OPCODES, TestVerdict } from '../../../../hooks/useDiagnosticLog';
import { DiagnosticDevice, DiagnosticHwSettings } from './DiagnosticLabTypes';
import { TestLogEntry } from '../../../../hooks/useDiagnosticLog';
import { Oracle59Sweep } from './oracle/Oracle59Sweep';
import { Oracle51Native } from './oracle/Oracle51Native';
import { Oracle43MultiSeq } from './oracle/Oracle43MultiSeq';
import { Oracle53LiveStream } from './oracle/Oracle53LiveStream';
import { OracleSceneMgmt } from './oracle/OracleSceneMgmt';

interface OracleTabProps {
  targetDeviceId: string | null;
  connectedDevices: DiagnosticDevice[];
  hwSettings: DiagnosticHwSettings | undefined;
  hwPts: number;
  coverage: Record<string, OpcodeStatus>;
  testLog: TestLogEntry[];
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
  const { S, txtPri, txtMuted, cardBg: _cardBg, border, isDark, cyan } = useDiagnosticLabStyles();

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
          note: '0x73 app mic byte — 0x26, isOn=0x01. Send then enable AUTO-STREAM ⇓',
          bytes: () => {
            const raw = ZenggeProtocol.oracleMusicMic26();
            return ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]);
          },
        },
        {
          label: 'MIC=0x27 ★',
          opcode: '0x73',
          note: '0x73 device mic byte — 0x27, isOn=0x01. Reacts to ambient sound WITHOUT 0x74.',
          bytes: () => {
            const raw = ZenggeProtocol.oracleMusicMic27();
            return ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]);
          },
        },
        {
          label: 'isOn=0x00 (OFF)',
          opcode: '0x73',
          note: '0x73 isOn byte set to 0x00 — LEDs should DEACTIVATE music mode. Confirms isOn is live.',
          bytes: () => {
            const raw = ZenggeProtocol.oracleMusicOff();
            return ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]);
          },
        },
        {
          label: 'MIC=0x00 (OLD ❌)',
          opcode: '0x73',
          note: '0x73 OLD wrong mic byte — 0x00. With auto-stream ON, LEDs should NOT pulse (confirms bug).',
          bytes: () => {
            const raw = ZenggeProtocol.oracleMusicMic00();
            return ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]);
          },
        },
        {
          label: 'isOn MISSING (12B)',
          opcode: '0x73',
          note: '0x73 OLD 12-byte format without isOn byte — should NOT activate music mode.',
          bytes: () => {
            const raw = ZenggeProtocol.oracleMusicMissingIsOn();
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
          note: '0x51 323B fixed 10B/slot ×32 — Verified 0xA3 format. Should cycle cleanly.',
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
            const raw = ZenggeProtocol.oracleSceneQuery();
            return ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]);
          },
        },
        {
          label: 'ACTIVATE #0 (0x57)',
          opcode: '0x57',
          note: '0x57 Activate scene slot 0, speed=50, brightness=100.',
          bytes: () => {
            const raw = ZenggeProtocol.oracleSceneActivate(0);
            return ZenggeProtocol.wrapCommand([...raw, ZenggeProtocol.calculateChecksum(raw)]);
          },
        },
        {
          label: 'DELETE #0 (0x56)',
          opcode: '0x56',
          note: '0x56 Delete scene slot 0 — 15-byte payload.',
          bytes: () => {
            const raw = ZenggeProtocol.oracleSceneDelete(0);
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

      {/* PHASE 2 — EXTENDED PANELS (Hypothesis) */}
      <Text style={[S.subTitle, { color: '#9D4EFF' }]}>🧪 PHASE 2 EXTENDED PANELS</Text>
      <Text style={{ color: txtMuted, fontSize: 10, marginBottom: Spacing.md }}>
        Inferred builders for opcodes not yet confirmed. Tap header to expand. All are labeled [HYPOTHESIS].
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
      {expandedP2 === 'TT_SWEEP' && <Oracle59Sweep hwPts={hwPts} transmit={transmit} />}

      {/* 🎵 0x51 NATIVE SYMPHONY — Effect ID Sweep (1–44) */}
      <TouchableOpacity
        onPress={() => setExpandedP2(expandedP2 === '0x41' ? null : '0x41')}
        style={[S.diagBox, { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center',
          borderColor: expandedP2 === '0x41' ? '#9D4EFF' : border,
          borderWidth: expandedP2 === '0x41' ? 1.5 : 1 }]}
      >
        <View>
          <Text style={{ color: '#9D4EFF', fontWeight: '900', fontSize: 13 }}>🎵 0x51 Native Symphony (1–44)</Text>
          <Text style={{ color: txtMuted, fontSize: 10, marginTop: 2 }}>
            FG/BG · speed · authentic hardware effect names
          </Text>
        </View>
        <Text style={{ color: '#9D4EFF', fontSize: 18 }}>{expandedP2 === '0x41' ? '▲' : '▼'}</Text>
      </TouchableOpacity>
      {expandedP2 === '0x41' && <Oracle51Native transmit={transmit} />}

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
      {expandedP2 === '0x43' && <Oracle43MultiSeq transmit={transmit} />}

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
      {expandedP2 === '0x53' && <Oracle53LiveStream hwPts={hwPts} transmit={transmit} />}

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
      {expandedP2 === 'SCENE' && <OracleSceneMgmt transmit={transmit} />}

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
            const vcfg = entry.verdict ? verdictConfig[entry.verdict as keyof typeof verdictConfig] : null;
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
