/**
 * StreetPanel.tsx — Street Mode telemetry dashboard.
 *
 * Renders the car-light zone bar, motion state indicator, speed/G-force
 * analog gauges, global telemetry readout, and session record/stop button.
 *
 * Extracted from DockedController.tsx (Phase 3).
 */
import { MaterialCommunityIcons } from '@expo/vector-icons';
import React from 'react';
import { ScrollView, Text, TouchableOpacity, View, useWindowDimensions } from 'react-native';
import { crewService } from '../../services/CrewService';
import { Spacing } from '../../theme/theme';
import type { MotionState } from '../../types/dashboard.types';
import AnalogGauge from './AnalogGauge';

interface StreetPanelProps {
  isStreetBraking: boolean;
  motionState: MotionState;
  gpsSpeed: number;
  peakGForce: number;
  streetCruiseColor: string;
  sessionActive: boolean;
  startSession: () => void;
  stopSessionRecording: () => void;
  Colors: any;
}

const StreetPanel = React.memo(({
  isStreetBraking,
  motionState,
  gpsSpeed,
  peakGForce,
  streetCruiseColor,
  sessionActive,
  startSession,
  stopSessionRecording,
  Colors,
}: StreetPanelProps) => {
  const { height: windowHeight } = useWindowDimensions();
  const isShort = windowHeight < 720;
  const gaugeSize = isShort ? 100 : 120;

  return (
    <ScrollView
      style={{ flex: 1 }}
      contentContainerStyle={{ flexGrow: 1, paddingHorizontal: Spacing.xs, paddingTop: Spacing.sm, paddingBottom: Spacing.xl }}
      showsVerticalScrollIndicator={false}
      bounces={false}
    >
      {/* Car-light zone bar */}
      <View style={{ marginBottom: isShort ? Spacing.sm : Spacing.md }}>
        <View style={{ flexDirection: 'row', height: isShort ? 22 : 26, borderRadius: 13, overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}>
          <View style={{ flex: 3, backgroundColor: isStreetBraking ? '#FF0000' : '#660000', justifyContent: 'center', alignItems: 'center' }}>
            <Text allowFontScaling={false} style={{ color: '#FFF', fontSize: 9, fontWeight: '800' }}>TAIL (30%)</Text>
          </View>
          <View style={{ flex: 4, backgroundColor: streetCruiseColor, justifyContent: 'center', alignItems: 'center', opacity: 0.9 }}>
            <Text allowFontScaling={false} style={{ color: '#000', fontSize: 9, fontWeight: '800' }}>CRUISE (40%)</Text>
          </View>
          <View style={{ flex: 3, backgroundColor: '#FFF5E0', justifyContent: 'center', alignItems: 'center' }}>
            <Text allowFontScaling={false} style={{ color: '#333', fontSize: 9, fontWeight: '800' }}>HEAD (30%)</Text>
          </View>
        </View>
      </View>

      {/* Status Bar */}
      <View style={{ flexDirection: 'row', justifyContent: 'center', alignItems: 'center', backgroundColor: 'transparent', paddingVertical: Spacing.xxs, marginBottom: Spacing.md }}>
        <Text
          allowFontScaling={false}
          style={{
            color: (motionState === 'HARD_BRAKING' || motionState === 'STOPPED') ? '#FF4444' : motionState === 'SLOWING_DOWN' ? '#FFD700' : '#00FF00',
            fontSize: 14, fontWeight: '900', letterSpacing: 4
          }}>
          {motionState === 'STOPPED' && '>> STOPPED <<'}
          {motionState === 'HARD_BRAKING' && '>> HARD BRAKING <<'}
          {motionState === 'SLOWING_DOWN' && '>> DECELERATING <<'}
          {motionState === 'ACCELERATING' && '>> ACCELERATING <<'}
          {motionState === 'CRUISING' && '>> CRUISING <<'}
        </Text>
      </View>

      <View style={{
        flexGrow: 1,
        flexDirection: 'row',
        backgroundColor: 'transparent',
        justifyContent: 'space-between',
        alignItems: 'center',
        paddingVertical: Spacing.md,
        marginBottom: Spacing.sm,
      }}>
        {/* Stoplight */}
        <View style={{ width: 50, justifyContent: 'center', alignItems: 'center' }}>
          <View style={{
            width: 22, height: 22, borderRadius: 11, marginBottom: Spacing.sm,
            backgroundColor: (motionState === 'STOPPED' || motionState === 'HARD_BRAKING') ? '#FF0000' : '#330000',
            shadowColor: '#FF0000', shadowOpacity: (motionState === 'STOPPED' || motionState === 'HARD_BRAKING') ? 1 : 0, shadowRadius: 10, elevation: (motionState === 'STOPPED' || motionState === 'HARD_BRAKING') ? 8 : 0,
            borderWidth: 1, borderColor: (motionState === 'STOPPED' || motionState === 'HARD_BRAKING') ? '#FFAAAA' : '#000',
          }} />
          <View style={{
            width: 22, height: 22, borderRadius: 11, marginBottom: Spacing.sm,
            backgroundColor: motionState === 'SLOWING_DOWN' ? '#FFFF00' : '#444400',
            shadowColor: '#FFFF00', shadowOpacity: motionState === 'SLOWING_DOWN' ? 1 : 0, shadowRadius: 10, elevation: motionState === 'SLOWING_DOWN' ? 8 : 0,
            borderWidth: 1, borderColor: motionState === 'SLOWING_DOWN' ? '#FFFFAA' : '#000',
          }} />
          <View style={{
            width: 22, height: 22, borderRadius: 11,
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

        {/* Session controls */}
        <View style={{ flexDirection: 'row', justifyContent: 'space-between', paddingHorizontal: Spacing.lg, marginTop: Spacing.md, marginBottom: Spacing.sm, alignItems: 'center' }}>
          <View>
            <Text style={{ color: 'rgba(255,255,255,0.5)', fontSize: 12, fontWeight: '700', letterSpacing: 0.5 }}>
              TOP SPEED: <Text style={{ color: '#00F0FF', fontWeight: '800' }}>{crewService.sessionTelemetry.topSpeedMph.toFixed(1)} MPH</Text>
            </Text>
            <Text style={{ color: 'rgba(255,255,255,0.5)', fontSize: 12, fontWeight: '700', letterSpacing: 0.5, marginTop: Spacing.xxs }}>
              DISTANCE: <Text style={{ color: '#00F0FF', fontWeight: '800' }}>{crewService.sessionTelemetry.distanceMiles.toFixed(2)} MI</Text>
            </Text>
          </View>

          <TouchableOpacity
            onPress={() => { if (!sessionActive) startSession(); else stopSessionRecording(); }}
            activeOpacity={0.85}
            style={{
              flexDirection: 'row', alignItems: 'center', gap: Spacing.sm,
              paddingHorizontal: Spacing.lg, paddingVertical: Spacing.sm,
              borderRadius: 20,
              backgroundColor: sessionActive ? '#FF3D00' : '#00C853',
              shadowColor: sessionActive ? '#FF3D00' : '#00C853',
              shadowOpacity: 0.5, shadowRadius: 8, elevation: 6,
            }}
          >
            <MaterialCommunityIcons name={sessionActive ? 'stop-circle' : 'play-circle'} size={18} color="#FFF" />
            <Text style={{ color: '#FFF', fontWeight: '900', fontSize: 13, letterSpacing: 0.5 }}>
              {sessionActive ? 'SAVE' : 'RECORD'}
            </Text>
          </TouchableOpacity>
        </View>
      </View>
    </ScrollView>
  );
});

export default StreetPanel;
