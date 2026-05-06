import React, { useEffect, useRef } from 'react';
import { View, Text, StyleSheet, Dimensions, Animated } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../../context/ThemeContext';
import { Typography, Spacing, Layout } from '../../theme/theme';
import Svg, { Circle, Defs, LinearGradient as SvgLinearGradient, Stop } from 'react-native-svg';
import { LinearGradient } from 'expo-linear-gradient';

// Wrapping Circle for standard Animated API
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
  sessionAvgSpeed
}) => {
  const { Colors } = useTheme();

  // Screen layout logic (half-circle gauge to save vertical space)
  const windowWidth = Dimensions.get('window').width;
  const isWeb = Dimensions.get('window').width > 600;
  const padding = Spacing.md * 4; // Increased padding to shrink gauge size
  const svgWidth = isWeb ? 400 - padding : windowWidth - padding; 
  
  const strokeWidth = 24; // Thicker, more aggressive gauge
  const radius = (svgWidth / 2) - (strokeWidth * 2); 
  const cx = svgWidth / 2;
  const cy = radius + (strokeWidth * 2); 
  
  const circumference = 2 * Math.PI * radius;
  const halfCircumference = circumference / 2;

  // Max speed scale (e.g. 25 mph)
  const MAX_SPEED = 25;
  const speedRatio = Math.min(Math.max(gpsSpeed / MAX_SPEED, 0), 1);
  
  // Physics engine value
  const animatedProgress = useRef(new Animated.Value(0)).current;
  
  useEffect(() => {
    Animated.spring(animatedProgress, {
      toValue: speedRatio,
      friction: 5, // Bouncier, tighter spring
      tension: 50,
      useNativeDriver: false 
    }).start();
  }, [gpsSpeed, speedRatio, animatedProgress]);

  const strokeDashoffset = animatedProgress.interpolate({
    inputRange: [0, 1],
    outputRange: [halfCircumference, 0]
  });

  const formatDuration = (secs: number) => {
    const m = Math.floor(secs / 60);
    const s = secs % 60;
    return `${m}:${s.toString().padStart(2, '0')}`;
  };

  // Gamified Calorie Estimate
  const kcalBurned = Math.round(sessionDistanceMiles * 65);

  return (
    <View style={styles.container}>
      
      {/* FLOATING REC INDICATOR */}
      <View style={{ position: 'absolute', top: Spacing.md, right: Spacing.md, flexDirection: 'row', alignItems: 'center', gap: 4, zIndex: 10 }}>
        <MaterialCommunityIcons name="record-circle-outline" size={12} color={Colors.error} />
        <Text style={[styles.headerText, { fontSize: 10, color: Colors.error, textShadowColor: Colors.error, textShadowRadius: 8 }]}>REC</Text>
      </View>

      {/* MASSIVE NEON GAUGE (Half Circle) */}
      <View style={{ width: svgWidth, height: cy + 10, alignItems: 'center', alignSelf: 'center', marginTop: Spacing.xl }}>
        <Svg width={svgWidth} height={cy + 10} viewBox={`0 0 ${svgWidth} ${cy + 10}`}>
          <Defs>
            <SvgLinearGradient id="neonGradient" x1="0%" y1="0%" x2="100%" y2="0%">
              <Stop offset="0%" stopColor="#00FFFF" stopOpacity="1" />
              <Stop offset="50%" stopColor="#8A2BE2" stopOpacity="1" />
              <Stop offset="100%" stopColor="#FF00FF" stopOpacity="1" />
            </SvgLinearGradient>
          </Defs>
          
          {/* Layer 1: Background Track (Dark & Deep) */}
          <Circle
            cx={cx}
            cy={cy}
            r={radius}
            stroke="rgba(255,255,255,0.03)"
            strokeWidth={strokeWidth}
            fill="none"
            strokeDasharray={`${halfCircumference} ${circumference}`}
            strokeDashoffset="0"
            strokeLinecap="round"
            transform={`rotate(180 ${cx} ${cy})`}
          />

          {/* Layer 2: Massive Outer Bloom */}
          <AnimatedCircle
            cx={cx}
            cy={cy}
            r={radius}
            stroke="url(#neonGradient)"
            strokeWidth={strokeWidth + 24}
            fill="none"
            opacity={0.15}
            strokeDasharray={`${halfCircumference} ${circumference}`}
            strokeDashoffset={strokeDashoffset}
            strokeLinecap="round"
            transform={`rotate(180 ${cx} ${cy})`}
          />

          {/* Layer 3: Main Colored Core Line */}
          <AnimatedCircle
            cx={cx}
            cy={cy}
            r={radius}
            stroke="url(#neonGradient)"
            strokeWidth={strokeWidth}
            fill="none"
            strokeDasharray={`${halfCircumference} ${circumference}`}
            strokeDashoffset={strokeDashoffset}
            strokeLinecap="round"
            transform={`rotate(180 ${cx} ${cy})`}
          />
          
          {/* Layer 4: Hot White Inner Filament */}
          <AnimatedCircle
            cx={cx}
            cy={cy}
            r={radius}
            stroke="#FFFFFF"
            strokeWidth={2}
            fill="none"
            opacity={0.8}
            strokeDasharray={`${halfCircumference} ${circumference}`}
            strokeDashoffset={strokeDashoffset}
            strokeLinecap="round"
            transform={`rotate(180 ${cx} ${cy})`}
          />
        </Svg>
        
        {/* Absolute Centered Speed Text */}
        <View style={styles.speedTextContainer}>
          <Text style={[styles.speedValue, { color: '#FFFFFF', textShadowColor: '#00FFFF', textShadowRadius: 25 }]}>
            {gpsSpeed.toFixed(1)}
          </Text>
          <Text style={[styles.speedUnit, { color: '#00FFFF', textShadowColor: '#00FFFF', textShadowRadius: 10 }]}>MPH</Text>
        </View>
      </View>

      {/* BOTTOM 6-METRIC GRID */}
      <View style={styles.metricsGrid}>
        <View style={styles.metricsRow}>
          <TelemetryGlassPill label="DISTANCE" value={sessionDistanceMiles.toFixed(2)} unit="mi" accent="#00FFFF" />
          <TelemetryGlassPill label="G-FORCE" value={peakGForce.toFixed(1)} unit="g" accent="#FF00FF" />
          <TelemetryGlassPill label="TIME" value={formatDuration(sessionDurationSec)} unit="" accent="#FFD600" />
        </View>
        <View style={styles.metricsRow}>
          <TelemetryGlassPill label="AVG SPD" value={sessionAvgSpeed.toFixed(1)} unit="mph" accent="#00FF85" />
          <TelemetryGlassPill label="TOP SPD" value={sessionPeakSpeed.toFixed(1)} unit="mph" accent="#FF4D00" />
          <TelemetryGlassPill label="BURN" value={kcalBurned.toString()} unit="kcal" accent="#FF0000" />
        </View>
      </View>

    </View>
  );
};

// Extracted Glassmorphic Pill Component
const TelemetryGlassPill = ({ label, value, unit, accent }: { label: string, value: string, unit: string, accent: string }) => (
  <View style={styles.pillContainer}>
    <LinearGradient
      colors={['rgba(255,255,255,0.08)', 'rgba(0,0,0,0.6)']}
      style={styles.pillBackground}
    />
    <View style={[styles.accentTick, { backgroundColor: accent, shadowColor: accent }]} />
    <Text style={styles.pillLabel}>{label}</Text>
    <View style={styles.pillValueContainer}>
      <Text style={[styles.pillValue, { textShadowColor: accent, textShadowRadius: 15 }]}>{value}</Text>
      {unit !== '' && <Text style={styles.pillUnit}>{unit}</Text>}
    </View>
  </View>
);

const styles = StyleSheet.create({
  container: {
    marginHorizontal: Layout.padding,
    marginBottom: 24, // Matched with slabContainer
    backgroundColor: 'rgba(255,255,255,0.03)',
    borderRadius: 16,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.08)',
    overflow: 'hidden',
    paddingBottom: Spacing.md,
  },
  headerRow: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: Spacing.lg,
    paddingTop: Spacing.lg,
    gap: 6,
  },
  headerText: {
    fontFamily: 'Righteous',
    fontSize: 12,
    letterSpacing: 1.5,
  },
  speedTextContainer: {
    position: 'absolute',
    bottom: 5,
    alignItems: 'center',
  },
  speedValue: {
    fontFamily: 'Righteous',
    fontSize: 72,
    lineHeight: 80,
  },
  speedUnit: {
    fontFamily: 'Righteous',
    fontSize: 16,
    letterSpacing: 3,
    marginTop: -8,
  },
  metricsGrid: {
    paddingHorizontal: Spacing.md,
    paddingBottom: Spacing.md,
    paddingTop: Spacing.sm,
    gap: Spacing.sm,
  },
  metricsRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    gap: Spacing.sm,
  },
  pillContainer: {
    flex: 1,
    paddingVertical: Spacing.md,
    paddingHorizontal: Spacing.xs,
    borderRadius: 12,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.05)',
    borderTopColor: 'rgba(255,255,255,0.15)', // Frosted edge light
    overflow: 'hidden',
  },
  pillBackground: {
    ...StyleSheet.absoluteFillObject,
  },
  accentTick: {
    position: 'absolute',
    bottom: 0,
    left: '20%',
    right: '20%',
    height: 3,
    borderTopLeftRadius: 3,
    borderTopRightRadius: 3,
    shadowOffset: { width: 0, height: -2 },
    shadowOpacity: 1,
    shadowRadius: 5,
    elevation: 4,
  },
  pillLabel: {
    fontSize: 9,
    fontFamily: 'Righteous',
    letterSpacing: 1.5,
    color: 'rgba(255,255,255,0.4)',
    marginBottom: 4,
  },
  pillValueContainer: {
    flexDirection: 'row',
    alignItems: 'baseline',
    gap: 2,
  },
  pillValue: {
    fontFamily: 'monospace',
    fontSize: 20,
    fontWeight: '900',
    color: '#FFFFFF',
  },
  pillUnit: {
    fontFamily: 'Righteous',
    fontSize: 10,
    color: 'rgba(255,255,255,0.5)',
  }
});
