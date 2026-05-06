import React, { useEffect, useRef } from 'react';
import { View, Text, StyleSheet, Dimensions, Animated } from 'react-native';
import { useTheme } from '../../context/ThemeContext';
import { Layout, Spacing } from '../../theme/theme';
import Svg, { Circle, G, Path } from 'react-native-svg';
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
  gpsSpeed,
  peakGForce,
  sessionDistanceMiles,
  sessionDurationSec,
  sessionPeakSpeed,
  sessionAvgSpeed,
}) => {
  const { Colors } = useTheme();
  const windowWidth = Dimensions.get('window').width;
  const isWeb = windowWidth > 600;

  // ─── Gauge math ──────────────────────────────────────────────────────────────
  // We draw a 270° arc (¾ of a circle).
  // The gap sits at the BOTTOM, like a real tachometer.
  // Rotation: we start at 225° (bottom-left) and sweep clockwise to 135° (bottom-right).
  const svgSize = isWeb ? 320 : Math.min(windowWidth - Spacing.md * 4, 320);
  const strokeWidth = 18;
  const glowWidth = strokeWidth + 28;
  const cx = svgSize / 2;
  const cy = svgSize / 2;
  const radius = cx - glowWidth / 2 - 2; // keeps bloom inside viewbox

  const circumference = 2 * Math.PI * radius;
  // 270° sweep = 75% of circumference
  const arcLen = circumference * 0.75;
  // dashArray: [arcLen, circumference - arcLen] makes only the 270° part visible
  const dashArray = `${arcLen} ${circumference}`;

  // The SVG is a full square but we clip vertical space:
  // The ¾ arc's lowest point is at cy + radius (bottom of circle).
  // Add half strokeWidth + a few px for the glow bleed.
  const svgHeight = cy + radius + glowWidth / 2 + 4;

  // Speed ratio → dashOffset:
  // At 0 speed: offset = arcLen (nothing visible)
  // At max speed: offset = 0 (full arc visible)
  const MAX_SPEED = 25;
  const speedRatio = Math.min(Math.max(gpsSpeed / MAX_SPEED, 0), 1);

  const animatedProgress = useRef(new Animated.Value(0)).current;
  useEffect(() => {
    Animated.spring(animatedProgress, {
      toValue: speedRatio,
      friction: 6,
      tension: 45,
      useNativeDriver: false,
    }).start();
  }, [gpsSpeed, speedRatio]);

  // strokeDashoffset: arcLen = empty, 0 = full
  const strokeDashoffset = animatedProgress.interpolate({
    inputRange: [0, 1],
    outputRange: [arcLen, 0],
  });

  // Rotate so the gap is at the bottom (225° start)
  const rotation = `rotate(135 ${cx} ${cy})`;

  const formatDuration = (secs: number) => {
    const m = Math.floor(secs / 60);
    const s = secs % 60;
    return `${m}:${s.toString().padStart(2, '0')}`;
  };
  const kcalBurned = Math.round(sessionDistanceMiles * 65);

  return (
    <View style={styles.container}>
      {/* ─── TACHOMETER GAUGE ────────────────────────────────────────────── */}
      <View style={{ alignSelf: 'center', width: svgSize, height: svgHeight }}>
        <Svg
          width={svgSize}
          height={svgHeight}
          viewBox={`0 0 ${svgSize} ${svgHeight}`}
        >
          {/* Layer 1: Dark track — hardcoded cyan at low opacity */}
          <Circle
            cx={cx}
            cy={cy}
            r={radius}
            fill="none"
            stroke="#00FFFF"
            strokeWidth={strokeWidth}
            strokeOpacity={0.08}
            strokeDasharray={dashArray}
            strokeDashoffset={0}
            strokeLinecap="round"
            transform={rotation}
          />

          {/* Layer 2: Outer glow bloom — hardcoded magenta, masked by progress */}
          <AnimatedCircle
            cx={cx}
            cy={cy}
            r={radius}
            fill="none"
            stroke="#FF00FF"
            strokeWidth={glowWidth}
            strokeOpacity={0.22}
            strokeDasharray={dashArray}
            strokeDashoffset={strokeDashoffset}
            strokeLinecap="round"
            transform={rotation}
          />

          {/* Layer 3: Second inner bloom — cyan */}
          <AnimatedCircle
            cx={cx}
            cy={cy}
            r={radius}
            fill="none"
            stroke="#00FFFF"
            strokeWidth={strokeWidth + 8}
            strokeOpacity={0.35}
            strokeDasharray={dashArray}
            strokeDashoffset={strokeDashoffset}
            strokeLinecap="round"
            transform={rotation}
          />

          {/* Layer 4: Core hot line — pure cyan */}
          <AnimatedCircle
            cx={cx}
            cy={cy}
            r={radius}
            fill="none"
            stroke="#00FFFF"
            strokeWidth={strokeWidth}
            strokeOpacity={1}
            strokeDasharray={dashArray}
            strokeDashoffset={strokeDashoffset}
            strokeLinecap="round"
            transform={rotation}
          />

          {/* Layer 5: Hot white inner filament */}
          <AnimatedCircle
            cx={cx}
            cy={cy}
            r={radius}
            fill="none"
            stroke="#FFFFFF"
            strokeWidth={2}
            strokeOpacity={0.9}
            strokeDasharray={dashArray}
            strokeDashoffset={strokeDashoffset}
            strokeLinecap="round"
            transform={rotation}
          />
        </Svg>

        {/* Speed value — positioned inside the arc center */}
        <View style={[styles.speedOverlay, { width: svgSize, height: svgHeight }]}>
          <Text style={styles.speedValue}>{gpsSpeed.toFixed(1)}</Text>
          <Text style={styles.speedUnit}>MPH</Text>
        </View>
      </View>

      {/* ─── 6-METRIC GRID ─────────────────────────────────────────────────── */}
      <View style={styles.metricsGrid}>
        <View style={styles.metricsRow}>
          <TelemetryGlassPill label="DISTANCE" value={sessionDistanceMiles.toFixed(2)} unit="mi" accent="#00FFFF" />
          <TelemetryGlassPill label="G-FORCE"  value={peakGForce.toFixed(1)}            unit="g"   accent="#FF00FF" />
          <TelemetryGlassPill label="TIME"     value={formatDuration(sessionDurationSec)} unit=""   accent="#FFD600" />
        </View>
        <View style={styles.metricsRow}>
          <TelemetryGlassPill label="AVG SPD"  value={sessionAvgSpeed.toFixed(1)}        unit="mph" accent="#00FF85" />
          <TelemetryGlassPill label="TOP SPD"  value={sessionPeakSpeed.toFixed(1)}       unit="mph" accent="#FF4D00" />
          <TelemetryGlassPill label="BURN"     value={kcalBurned.toString()}              unit="kcal" accent="#FF0000" />
        </View>
      </View>
    </View>
  );
};

// ─── Glass Pill ────────────────────────────────────────────────────────────────
const TelemetryGlassPill = ({
  label, value, unit, accent,
}: { label: string; value: string; unit: string; accent: string }) => (
  <View style={styles.pillContainer}>
    <LinearGradient
      colors={['rgba(255,255,255,0.07)', 'rgba(0,0,0,0.5)']}
      style={StyleSheet.absoluteFillObject}
    />
    <View style={[styles.accentTick, { backgroundColor: accent, shadowColor: accent }]} />
    <Text style={styles.pillLabel}>{label}</Text>
    <View style={styles.pillValueContainer}>
      <Text style={[styles.pillValue, { textShadowColor: accent, textShadowRadius: 14 }]}>
        {value}
      </Text>
      {unit !== '' && <Text style={styles.pillUnit}>{unit}</Text>}
    </View>
  </View>
);

// ─── Styles ────────────────────────────────────────────────────────────────────
const styles = StyleSheet.create({
  container: {
    marginHorizontal: Layout.padding,
    marginBottom: Spacing.sm,
    marginTop: -Spacing.sm, // Pulls gauge tight against Crewz Hub
  },
  speedOverlay: {
    position: 'absolute',
    top: 0,
    left: 0,
    alignItems: 'center',
    justifyContent: 'center',
    paddingTop: 20, // Shift text slightly toward center-lower to sit inside arc
  },
  speedValue: {
    fontFamily: 'Righteous',
    fontSize: 68,
    lineHeight: 74,
    color: '#FF00FF',
    textShadowColor: '#FF00FF',
    textShadowRadius: 28,
    textShadowOffset: { width: 0, height: 0 },
  },
  speedUnit: {
    fontFamily: 'Righteous',
    fontSize: 15,
    letterSpacing: 4,
    color: '#00FFFF',
    textShadowColor: '#00FFFF',
    textShadowRadius: 12,
    marginTop: -6,
  },
  metricsGrid: {
    paddingHorizontal: Spacing.xs,
    paddingTop: Spacing.xs,
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
    bottom: 0,
    left: '20%',
    right: '20%',
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
  pillValueContainer: {
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
