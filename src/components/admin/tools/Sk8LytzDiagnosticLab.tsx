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
import React, { useState } from 'react';
import {
    Platform,
    StyleSheet,
    Text,
    TouchableOpacity,
    View
} from 'react-native';
import { Device } from 'react-native-ble-plx';
import { DeviceHardwareConfig } from './tabs/DiagnosticLabTypes';
import { RegisteredDevice } from '../../../hooks/useRegistration';
import { SafeAreaView, useSafeAreaInsets } from 'react-native-safe-area-context';
import { useTheme } from '../../../context/ThemeContext';
import {
    useDiagnosticLog,
} from '../../../hooks/useDiagnosticLog';
import { useProtocolBuilder } from '../../../hooks/useProtocolBuilder';
import { useRegistration } from '../../../hooks/useRegistration';

import { Spacing, Typography } from '../../../theme/theme';


// ─── Types ────────────────────────────────────────────────────────────────────

interface LabProps {
  visible: boolean;
  onClose: () => void;
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
  allDevices?: Device[];
  bleState?: string;
  handleScan?: () => void;
  connectToDevice?: (device: Device) => Promise<void>;
  liveDeviceConfigs?: Record<string, DeviceHardwareConfig>;
  registeredDevices?: RegisteredDevice[];
  isDiagnosticsMode?: boolean;
  onToggleDiagnostics?: () => void;
}

type LabTab = 'DEVICES' | 'COLOR' | 'TRANSITION' | 'BUILDER' | 'SNIFFER' | 'ORACLE';

// moved to useDiagnosticLog.ts

import { DiagnosticLabDevicesTab } from './tabs/DiagnosticLabDevicesTab';
import { DiagnosticLabColorTab } from './tabs/DiagnosticLabColorTab';
import { DiagnosticLabTransitionTab } from './tabs/DiagnosticLabTransitionTab';
import { DiagnosticLabBuilderTab } from './tabs/DiagnosticLabBuilderTab';
import { DiagnosticLabSnifferTab } from './tabs/DiagnosticLabSnifferTab';
import { DiagnosticLabOracleTab } from './tabs/DiagnosticLabOracleTab';

// moved to useProtocolBuilder.ts

// moved to useProtocolBuilder.ts

// ─── Main Component ───────────────────────────────────────────────────────────

export default function Sk8LytzDiagnosticLab({
  visible, onClose, liveRxPayload,
  connectedDevices = [], hwSettings,
  allDevices = [], bleState = 'IDLE', handleScan,
  connectToDevice, liveDeviceConfigs = {},
  isDiagnosticsMode, onToggleDiagnostics,
}: LabProps) {

  const { Colors } = useTheme();
  const bg      = Colors.background;
  const txtMuted= Colors.textMuted;
  const border  = Colors.surfaceHighlight;
  const cyan    = '#00f0ff';

  const { registeredDevices, isLoading: isRegistrationLoading } = useRegistration();

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
    logs, lastSent, lastNote, transmit, sendRawHex, clearLogs,
    testLog, coverage, setVerdict, setLastVerdict, clearTestLog,
  } = useDiagnosticLog({ visible, liveRxPayload, targetDeviceId });

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
    bldMusicIsOn, setBldMusicIsOn,
    bldSens, setBldSens,
    bldC2, setBldC2,
    bldMatrixStyle, setBldMatrixStyle,
    bldIc, setBldIc,
    bldOrder, setBldOrder,
    bldSegs, setBldSegs,
    bldResult
  } = useProtocolBuilder(hwPts);

  // Builder contexts packed into one object for the component
  const builderCtx = {
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
    bldMusicIsOn, setBldMusicIsOn,
    bldSens, setBldSens,
    bldC2, setBldC2,
    bldMatrixStyle, setBldMatrixStyle,
    bldIc, setBldIc,
    bldOrder, setBldOrder,
    bldSegs, setBldSegs,
    bldResult
  };




  // ── Root render ───────────────────────────────────────────────────
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
          {(['DEVICES', 'COLOR', 'TRANSITION', 'BUILDER', 'SNIFFER', 'ORACLE'] as LabTab[]).map(t => {
            const active = tab === t;
            return (
              <TouchableOpacity
                key={t}
                style={[S.tabBtn, active && S.tabBtnActive]}
                onPress={() => setTab(t)}
              >
                <Text style={[S.tabBtnTxt, active && S.tabBtnTxtActive, { color: active ? cyan : txtMuted }]}>
                  {t === 'ORACLE' ? '🔬' : t}
                </Text>
              </TouchableOpacity>
            );
          })}
        </View>

        {/* Content */}
        <View style={[S.content, { backgroundColor: bg }]}>
          {tab === 'DEVICES' && <DiagnosticLabDevicesTab 
            targetDeviceId={targetDeviceId}
            setTargetDeviceId={setTargetDeviceId}
            connectedDevices={connectedDevices}
            allDevices={allDevices}
            liveDeviceConfigs={liveDeviceConfigs}
            bleState={bleState}
            handleScan={handleScan}
            connectToDevice={connectToDevice}
            registeredDevices={registeredDevices}
            isLoading={isRegistrationLoading}
            liveRxPayload={liveRxPayload}
            hwSettings={hwSettings}
          />}
          {tab === 'COLOR' && <DiagnosticLabColorTab 
            targetDeviceId={targetDeviceId}
            connectedDevices={connectedDevices}
            hwSettings={hwSettings}
            hwPts={hwPts}
            transmit={transmit}
          />}
          {tab === 'TRANSITION' && <DiagnosticLabTransitionTab 
            targetDeviceId={targetDeviceId}
            connectedDevices={connectedDevices}
            hwSettings={hwSettings}
            hwPts={hwPts}
            bldColors={bldColors}
            bldSpeed={bldSpeed}
            lastSent={lastSent}
            lastNote={lastNote}
            transmit={transmit}
          />}
          {tab === 'BUILDER' && <DiagnosticLabBuilderTab 
            targetDeviceId={targetDeviceId}
            connectedDevices={connectedDevices}
            hwSettings={hwSettings}
            hwPts={hwPts}
            builderCtx={builderCtx}
            transmit={transmit}
            sendRawHex={sendRawHex}
          />}
          {tab === 'SNIFFER' && <DiagnosticLabSnifferTab 
            logs={logs}
            clearLogs={clearLogs}
          />}
          {tab === 'ORACLE' && <DiagnosticLabOracleTab 
            targetDeviceId={targetDeviceId}
            connectedDevices={connectedDevices}
            hwSettings={hwSettings}
            hwPts={hwPts}
            coverage={coverage}
            testLog={testLog}
            setVerdict={setVerdict}
            setLastVerdict={setLastVerdict}
            clearTestLog={clearTestLog}
            transmit={transmit}
          />}
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
    fontFamily: Platform.select({ ios: 'Menlo', default: 'monospace' }), 
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
