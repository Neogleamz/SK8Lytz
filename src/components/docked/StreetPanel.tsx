/**
 * StreetPanel.tsx — Street Mode telemetry dashboard.
 *
 * Renders the motion state indicator, speed/G-force analog gauges,
 * 4 metric chip pills (Top Speed, Avg Speed, Distance, Session Time),
 * LED zone distribution slider, and a minimal REC dot indicator.
 *
 * Extracted from DockedController.tsx (Phase 3).
 * Overhauled in feat/street-mode-telemetry-overhaul (v3 mockup).
 */
import React, { useEffect, useRef } from 'react';
import { Animated, ScrollView, Text, TouchableOpacity, View, useWindowDimensions } from 'react-native';
import { crewService } from '../../services/CrewService';
import { Spacing } from '../../theme/theme';
import type { MotionState } from '../../types/dashboard.types';
import AnalogGauge from './AnalogGauge';
import StreetModeDistributionSlider from './StreetModeDistributionSlider';

/** Converts elapsed milliseconds → "MM:SS" string */
function formatElapsedTime(ms: number): string {
  const totalSeconds = Math.floor(ms / 1000);
  const minutes = Math.floor(totalSeconds / 60);
  const seconds = totalSeconds % 60;
  return `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
}

interface StreetPanelProps {
  isStreetBraking: boolean;
  motionState: MotionState;
  gpsSpeed: number;
  peakGForce: number;
  streetCruiseColor: string;
  streetDistribution: [number, number, number];
  setStreetDistribution: (dist: [number, number, number]) => void;
  sessionActive: boolean;
  startSession: () => void;
  stopSessionRecording: () => void;
  /** Injected from useSessionTracking — used for Session Time chip */
  sessionStartTimeRef: React.MutableRefObject<number | null>;
  /** Injected from useSessionTracking — used for Avg Speed chip */
  sessionSpeedSamplesRef: React.MutableRefObject<number[]>;
  Colors: any;
}

const StreetPanel = React.memo(({
  isStreetBraking,
  motionState,
  gpsSpeed,
  peakGForce,
  streetCruiseColor,
  streetDistribution,
  setStreetDistribution,
  sessionActive,
  startSession,
  stopSessionRecording,
  sessionStartTimeRef,
  sessionSpeedSamplesRef,
  Colors,
}: StreetPanelProps) => {
  const { height: windowHeight } = useWindowDimensions();
  const isShort = windowHeight < 720;
  const gaugeSize = isShort ? 100 : 120;

  // ── REC dot pulse animation ───────────────────────────────────────────────
  const pulseAnim = useRef(new Animated.Value(1)).current;
  useEffect(() => {
    if (sessionActive) {
      const pulse = Animated.loop(
        Animated.sequence([
          Animated.timing(pulseAnim, { toValue: 0.3, duration: 600, useNativeDriver: true }),
          Animated.timing(pulseAnim, { toValue: 1.0, duration: 600, useNativeDriver: true }),
        ])
      );
      pulse.start();
      return () => pulse.stop();
    } else {
      pulseAnim.setValue(1);
    }
  }, [sessionActive, pulseAnim]);

  // ── Metric chip computations ──────────────────────────────────────────────
  const topSpeed = crewService.sessionTelemetry.topSpeedMph.toFixed(1);
  const distance = crewService.sessionTelemetry.distanceMiles.toFixed(2);
  const avgSpeed = (() => {
    const samples = sessionSpeedSamplesRef.current;
    if (samples.length === 0) return '0.0';
    const sum = samples.reduce((a, b) => a + b, 0);
    return (sum / samples.length).toFixed(1);
  })();
  const sessionTime = sessionStartTimeRef.current
    ? formatElapsedTime(Date.now() - sessionStartTimeRef.current)
    : '--:--';

  // ── Motion state label & color ────────────────────────────────────────────
  const stateColor =
    motionState === 'HARD_BRAKING' || motionState === 'STOPPED' ? '#FF4444'
    : motionState === 'SLOWING_DOWN' ? '#FFD700'
    : '#00FF00';
  const stateLabel =
    motionState === 'STOPPED'      ? '>> STOPPED <<'      :
    motionState === 'HARD_BRAKING' ? '>> HARD BRAKING <<' :
    motionState === 'SLOWING_DOWN' ? '>> DECELERATING <<' :
    motionState === 'ACCELERATING' ? '>> ACCELERATING <<' :
                                     '>> CRUISING <<';

  return (
    <ScrollView
      style={{ flex: 1 }}
      contentContainerStyle={{ flexGrow: 1, paddingHorizontal: Spacing.xs, paddingTop: Spacing.sm, paddingBottom: Spacing.xl }}
      showsVerticalScrollIndicator={false}
      bounces={false}
    >
      {/* ── Header row: Motion State (centered) + REC indicator (absolute right) ── */}
      <View style={{ position: 'relative', alignItems: 'center', justifyContent: 'center', marginBottom: Spacing.sm, paddingHorizontal: 4, minHeight: 28 }}>
        <Text
          allowFontScaling={false}
          style={{ color: stateColor, fontSize: 13, fontWeight: '900', letterSpacing: 3, textAlign: 'center' }}
        >
          {stateLabel}
        </Text>

        {/* REC dot — absolutely pinned to right edge so it never shifts the centered text */}
        <TouchableOpacity
          onPress={() => { if (!sessionActive) startSession(); else stopSessionRecording(); }}
          activeOpacity={0.7}
          style={{ position: 'absolute', right: 0, alignItems: 'center', paddingHorizontal: 6, paddingVertical: 2 }}
        >
          <Text style={{ color: '#FF3D00', fontSize: 8, fontWeight: '900', letterSpacing: 1, marginBottom: 3 }}>
            REC
          </Text>
          <Animated.View style={{
            width: 13, height: 13, borderRadius: 7,
            backgroundColor: sessionActive ? '#FF3D00' : '#3D0A00',
            shadowColor: '#FF3D00',
            shadowOpacity: sessionActive ? 0.9 : 0,
            shadowRadius: 6,
            elevation: sessionActive ? 6 : 0,
            opacity: sessionActive ? pulseAnim : 0.4,
          }} />
        </TouchableOpacity>
      </View>


      {/* ── Stoplight + Gauges row ── */}
      <View style={{
        flexDirection: 'row',
        backgroundColor: 'transparent',
        justifyContent: 'space-between',
        alignItems: 'center',
        paddingVertical: Spacing.sm,
        marginBottom: Spacing.sm,
      }}>
        {/* Stoplight */}
        <View style={{ width: 44, justifyContent: 'center', alignItems: 'center' }}>
          <View style={{
            width: 20, height: 20, borderRadius: 10, marginBottom: Spacing.sm,
            backgroundColor: (motionState === 'STOPPED' || motionState === 'HARD_BRAKING') ? '#FF0000' : '#330000',
            shadowColor: '#FF0000', shadowOpacity: (motionState === 'STOPPED' || motionState === 'HARD_BRAKING') ? 1 : 0, shadowRadius: 10, elevation: (motionState === 'STOPPED' || motionState === 'HARD_BRAKING') ? 8 : 0,
            borderWidth: 1, borderColor: (motionState === 'STOPPED' || motionState === 'HARD_BRAKING') ? '#FFAAAA' : '#000',
          }} />
          <View style={{
            width: 20, height: 20, borderRadius: 10, marginBottom: Spacing.sm,
            backgroundColor: motionState === 'SLOWING_DOWN' ? '#FFFF00' : '#444400',
            shadowColor: '#FFFF00', shadowOpacity: motionState === 'SLOWING_DOWN' ? 1 : 0, shadowRadius: 10, elevation: motionState === 'SLOWING_DOWN' ? 8 : 0,
            borderWidth: 1, borderColor: motionState === 'SLOWING_DOWN' ? '#FFFFAA' : '#000',
          }} />
          <View style={{
            width: 20, height: 20, borderRadius: 10,
            backgroundColor: (motionState === 'ACCELERATING' || motionState === 'CRUISING') ? '#00FF00' : '#003300',
            shadowColor: '#00FF00', shadowOpacity: (motionState === 'ACCELERATING' || motionState === 'CRUISING') ? 1 : 0, shadowRadius: 10, elevation: (motionState === 'ACCELERATING' || motionState === 'CRUISING') ? 8 : 0,
            borderWidth: 1, borderColor: (motionState === 'ACCELERATING' || motionState === 'CRUISING') ? '#AAFFAA' : '#000',
          }} />
        </View>

        {/* Gauges */}
        <View style={{ flex: 1, flexDirection: 'row', justifyContent: 'space-evenly', alignItems: 'center' }}>
          <AnalogGauge value={gpsSpeed} min={0} max={25} label="SPEED" unit="MPH" size={gaugeSize} defaultColor="#00F0FF" dangerVal={15} criticalVal={20} />
          <AnalogGauge value={peakGForce} min={0.3} max={2.5} label="G-FORCE" unit="G" size={gaugeSize} defaultColor="#FFD700" dangerVal={1.2} criticalVal={1.8} />
        </View>
      </View>

      {/* ── 4 Metric Chip Pills ── */}
      <View style={{ flexDirection: 'row', justifyContent: 'space-between', paddingHorizontal: 2, marginBottom: isShort ? Spacing.sm : Spacing.md }}>
        {/* TOP SPEED */}
        <View style={chipStyle}>
          <Text style={[chipLabelStyle, { color: '#00F0FF' }]}>⚡ TOP</Text>
          <Text style={chipValueStyle}>{topSpeed}</Text>
          <Text style={[chipLabelStyle, { color: 'rgba(255,255,255,0.4)' }]}>MPH</Text>
        </View>
        {/* AVG SPEED */}
        <View style={chipStyle}>
          <Text style={[chipLabelStyle, { color: '#A78BFA' }]}>~ AVG</Text>
          <Text style={chipValueStyle}>{avgSpeed}</Text>
          <Text style={[chipLabelStyle, { color: 'rgba(255,255,255,0.4)' }]}>MPH</Text>
        </View>
        {/* DISTANCE */}
        <View style={chipStyle}>
          <Text style={[chipLabelStyle, { color: '#00C853' }]}>📍</Text>
          <Text style={chipValueStyle}>{distance}</Text>
          <Text style={[chipLabelStyle, { color: 'rgba(255,255,255,0.4)' }]}>MI</Text>
        </View>
        {/* SESSION TIME */}
        <View style={chipStyle}>
          <Text style={[chipLabelStyle, { color: '#FFD700' }]}>⏱</Text>
          <Text style={chipValueStyle}>{sessionTime}</Text>
        </View>
      </View>

      {/* ── LED Zone Distribution Slider (bottom of panel) ── */}
      <View style={{ paddingHorizontal: 6, marginTop: Spacing.xs }}>
        <StreetModeDistributionSlider
          distribution={streetDistribution}
          onChange={setStreetDistribution}
          tailColor={isStreetBraking ? '#FF0000' : '#660000'}
          cruiseColor={streetCruiseColor}
          headColor="#FFF5E0"
          isShort={isShort}
        />
      </View>
    </ScrollView>
  );
});

// ── Shared chip styles ────────────────────────────────────────────────────────
const chipStyle: object = {
  flex: 1,
  alignItems: 'center',
  justifyContent: 'center',
  backgroundColor: 'rgba(255,255,255,0.05)',
  borderWidth: 1,
  borderColor: 'rgba(255,255,255,0.09)',
  borderRadius: 14,
  paddingVertical: 6,
  paddingHorizontal: 4,
  marginHorizontal: 2,
};
const chipLabelStyle: object = {
  fontSize: 8,
  fontWeight: '800',
  letterSpacing: 0.5,
  marginBottom: 1,
};
const chipValueStyle: object = {
  color: '#FFFFFF',
  fontSize: 12,
  fontWeight: '800',
  letterSpacing: 0.3,
};

export default StreetPanel;
