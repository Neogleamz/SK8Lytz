import React, { useEffect, useRef } from 'react';
import { View, Text, StyleSheet, Dimensions, Animated } from 'react-native';
import { Layout, Spacing } from '../../theme/theme';
import Svg, { Circle } from 'react-native-svg';
import { LinearGradient } from 'expo-linear-gradient';

const AnimatedCircle = Animated.createAnimatedComponent(Circle);

interface DashboardTelemetryHeroProps {
  gpsSpeed: number;
  peakGForce: number;
  sessionDistanceMiles: number;
  sessionDurationSec: number;
  sessionPeakSpeed: number;
  sessionAvgSpeed: number;
}

export const DashboardTelemetryHero: React.FC<DashboardTelemetryHeroProps> = ({
  gpsSpeed, peakGForce, sessionDistanceMiles,
  sessionDurationSec, sessionPeakSpeed, sessionAvgSpeed,
}) => {
  const windowWidth = Dimensions.get('window').width;
  const isWeb = windowWidth > 600;

  // ─── GEOMETRY ─────────────────────────────────────────────────────────────────
  // cy sits at the BOTTOM of the SVG. Arc draws upward. Zero dead space.
  //   halfDash(r) = [πr, 2πr] → shows only the top-half arc
  //   rotate(180 cx cy)       → fills left-to-right as speed rises
  const OUTER_SW = 20;   // outer ring stroke width
  const BLOOM    = 24;   // glow bleed beyond stroke edge
  const BLEED    = OUTER_SW / 2 + BLOOM + 4;

  const svgWidth = isWeb ? 340 : Math.min(windowWidth - Spacing.md * 2, 360);
  const outerR   = svgWidth / 2 - BLEED;
  const midR     = outerR - 34;
  const thin1R   = outerR - 54;
  const thin2R   = outerR - 63;
  const thin3R   = outerR - 71;

  const cx        = svgWidth / 2;
  const cy        = outerR + BLEED;   // center at BOTTOM of container
  const svgHeight = cy;               // container height = exactly cy

  const halfDash = (r: number) => `${Math.PI * r} ${2 * Math.PI * r}`;
  const rot      = `rotate(180 ${cx} ${cy})`;

  // ─── ANIMATION ────────────────────────────────────────────────────────────────
  const MAX_SPEED  = 25;
  const speedRatio = Math.min(Math.max(gpsSpeed / MAX_SPEED, 0), 1);
  const animRatio  = useRef(new Animated.Value(0)).current;

  useEffect(() => {
    Animated.spring(animRatio, {
      toValue: speedRatio, friction: 6, tension: 45, useNativeDriver: false,
    }).start();
  }, [speedRatio]);

  // Each ring has its own halfCirc because radius differs
  const offsetFor = (r: number) =>
    animRatio.interpolate({ inputRange: [0, 1], outputRange: [Math.PI * r, 0] });

  const outerOff = offsetFor(outerR);
  const midOff   = offsetFor(midR);

  // ─── HELPERS ──────────────────────────────────────────────────────────────────
  const formatDuration = (secs: number) => {
    const m = Math.floor(secs / 60);
    const s = secs % 60;
    return `${m}:${s.toString().padStart(2, '0')}`;
  };
  const kcalBurned = Math.round(sessionDistanceMiles * 65);

  return (
    <View style={styles.container}>

      {/* ─── SPEEDOMETER ──────────────────────────────────────────────────────── */}
      <View style={{ alignSelf: 'center', width: svgWidth, height: svgHeight }}>
        <Svg width={svgWidth} height={svgHeight} viewBox={`0 0 ${svgWidth} ${svgHeight}`}>

          {/* ── OUTER MAGENTA RING ── */}
          {/* dim track (always shown) */}
          <Circle cx={cx} cy={cy} r={outerR} fill="none"
            stroke="#FF00FF" strokeWidth={OUTER_SW} strokeOpacity={0.09}
            strokeDasharray={halfDash(outerR)} strokeDashoffset={0}
            strokeLinecap="round" transform={rot} />
          {/* outer glow bloom (animated) */}
          <AnimatedCircle cx={cx} cy={cy} r={outerR} fill="none"
            stroke="#FF00FF" strokeWidth={OUTER_SW + BLOOM} strokeOpacity={0.18}
            strokeDasharray={halfDash(outerR)} strokeDashoffset={outerOff}
            strokeLinecap="round" transform={rot} />
          {/* core magenta line (animated) */}
          <AnimatedCircle cx={cx} cy={cy} r={outerR} fill="none"
            stroke="#FF00FF" strokeWidth={OUTER_SW} strokeOpacity={1}
            strokeDasharray={halfDash(outerR)} strokeDashoffset={outerOff}
            strokeLinecap="round" transform={rot} />
          {/* white hot filament */}
          <AnimatedCircle cx={cx} cy={cy} r={outerR} fill="none"
            stroke="#FFFFFF" strokeWidth={2} strokeOpacity={0.7}
            strokeDasharray={halfDash(outerR)} strokeDashoffset={outerOff}
            strokeLinecap="round" transform={rot} />

          {/* ── INNER CYAN RING ── */}
          {/* dim track */}
          <Circle cx={cx} cy={cy} r={midR} fill="none"
            stroke="#00FFFF" strokeWidth={10} strokeOpacity={0.08}
            strokeDasharray={halfDash(midR)} strokeDashoffset={0}
            strokeLinecap="round" transform={rot} />
          {/* cyan bloom (animated) */}
          <AnimatedCircle cx={cx} cy={cy} r={midR} fill="none"
            stroke="#00FFFF" strokeWidth={22} strokeOpacity={0.16}
            strokeDasharray={halfDash(midR)} strokeDashoffset={midOff}
            strokeLinecap="round" transform={rot} />
          {/* core cyan (animated) */}
          <AnimatedCircle cx={cx} cy={cy} r={midR} fill="none"
            stroke="#00FFFF" strokeWidth={10} strokeOpacity={1}
            strokeDasharray={halfDash(midR)} strokeDashoffset={midOff}
            strokeLinecap="round" transform={rot} />

          {/* ── DECORATIVE INNER LINES (static, purely aesthetic) ── */}
          <Circle cx={cx} cy={cy} r={thin1R} fill="none"
            stroke="#00FFFF" strokeWidth={1.5} strokeOpacity={0.45}
            strokeDasharray={halfDash(thin1R)} strokeDashoffset={0}
            strokeLinecap="butt" transform={rot} />
          <Circle cx={cx} cy={cy} r={thin2R} fill="none"
            stroke="#FFFFFF" strokeWidth={1} strokeOpacity={0.22}
            strokeDasharray={halfDash(thin2R)} strokeDashoffset={0}
            strokeLinecap="butt" transform={rot} />
          <Circle cx={cx} cy={cy} r={thin3R} fill="none"
            stroke="#8A2BE2" strokeWidth={1} strokeOpacity={0.35}
            strokeDasharray={halfDash(thin3R)} strokeDashoffset={0}
            strokeLinecap="butt" transform={rot} />

        </Svg>

        {/* Speed text — sits inside the arc opening at the bottom */}
        <View style={[styles.speedOverlay, { width: svgWidth, height: svgHeight }]}>
          <Text style={styles.speedValue}>{gpsSpeed.toFixed(1)}</Text>
          <Text style={styles.speedUnit}>MPH</Text>
        </View>
      </View>

      {/* ─── 6-METRIC GRID ────────────────────────────────────────────────────── */}
      <View style={styles.metricsGrid}>
        <View style={styles.metricsRow}>
          <TelemetryPill label="DISTANCE" value={sessionDistanceMiles.toFixed(2)} unit="mi"   accent="#00FFFF" />
          <TelemetryPill label="G-FORCE"  value={peakGForce.toFixed(1)}           unit="g"    accent="#FF00FF" />
          <TelemetryPill label="TIME"     value={formatDuration(sessionDurationSec)} unit=""  accent="#FFD600" />
        </View>
        <View style={styles.metricsRow}>
          <TelemetryPill label="AVG SPD"  value={sessionAvgSpeed.toFixed(1)}       unit="mph"  accent="#00FF85" />
          <TelemetryPill label="TOP SPD"  value={sessionPeakSpeed.toFixed(1)}      unit="mph"  accent="#FF4D00" />
          <TelemetryPill label="BURN"     value={kcalBurned.toString()}             unit="kcal" accent="#FF0000" />
        </View>
      </View>

    </View>
  );
};

// ─── Glass Pill ───────────────────────────────────────────────────────────────
const TelemetryPill = ({ label, value, unit, accent }:
  { label: string; value: string; unit: string; accent: string }) => (
  <View style={styles.pillContainer}>
    <LinearGradient
      colors={['rgba(255,255,255,0.07)', 'rgba(0,0,0,0.5)']}
      style={StyleSheet.absoluteFillObject}
    />
    <View style={[styles.accentTick, { backgroundColor: accent, shadowColor: accent }]} />
    <Text style={styles.pillLabel}>{label}</Text>
    <View style={styles.pillValueRow}>
      <Text style={[styles.pillValue, { textShadowColor: accent, textShadowRadius: 14 }]}>
        {value}
      </Text>
      {unit !== '' && <Text style={styles.pillUnit}>{unit}</Text>}
    </View>
  </View>
);

// ─── Styles ───────────────────────────────────────────────────────────────────
const styles = StyleSheet.create({
  container: {
    marginHorizontal: Layout.padding,
    marginBottom: Spacing.sm,
    marginTop: -4,
  },
  speedOverlay: {
    position: 'absolute',
    top: 0, left: 0,
    alignItems: 'center',
    justifyContent: 'flex-end',
    paddingBottom: 12,
  },
  speedValue: {
    fontFamily: 'Righteous',
    fontSize: 64,
    lineHeight: 70,
    color: '#FF00FF',
    textShadowColor: '#FF00FF',
    textShadowRadius: 30,
    textShadowOffset: { width: 0, height: 0 },
  },
  speedUnit: {
    fontFamily: 'Righteous',
    fontSize: 14,
    letterSpacing: 4,
    color: '#00FFFF',
    textShadowColor: '#00FFFF',
    textShadowRadius: 10,
    marginTop: -4,
  },
  metricsGrid: {
    paddingHorizontal: Spacing.xs,
    paddingTop: Spacing.sm,
    gap: Spacing.xs,
  },
  metricsRow: {
    flexDirection: 'row',
    gap: Spacing.xs,
  },
  pillContainer: {
    flex: 1,
    paddingVertical: Spacing.sm,
    paddingHorizontal: Spacing.xs,
    borderRadius: 8,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.04)',
    borderTopColor: 'rgba(255,255,255,0.14)',
    overflow: 'hidden',
  },
  accentTick: {
    position: 'absolute',
    bottom: 0, left: '20%', right: '20%',
    height: 2,
    shadowOffset: { width: 0, height: -2 },
    shadowOpacity: 1,
    shadowRadius: 5,
    elevation: 4,
  },
  pillLabel: {
    fontSize: 8,
    fontFamily: 'Righteous',
    letterSpacing: 1.5,
    color: 'rgba(255,255,255,0.4)',
    marginBottom: 2,
  },
  pillValueRow: {
    flexDirection: 'row',
    alignItems: 'baseline',
    gap: 2,
  },
  pillValue: {
    fontFamily: 'monospace',
    fontSize: 18,
    fontWeight: '900',
    color: '#FFFFFF',
  },
  pillUnit: {
    fontFamily: 'Righteous',
    fontSize: 9,
    color: 'rgba(255,255,255,0.5)',
  },
});
