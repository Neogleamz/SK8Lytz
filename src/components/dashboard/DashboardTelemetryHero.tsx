import React, { useEffect, useRef } from 'react';
import { View, Text, StyleSheet, Dimensions, Animated } from 'react-native';
import { useTheme } from '../../context/ThemeContext';
import { Layout, Spacing } from '../../theme/theme';
import Svg, { Circle, Defs, LinearGradient as SvgLinearGradient, Stop, Mask } from 'react-native-svg';
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

  // Mathematical Bounding Box for minimum vertical height
  const windowWidth = Dimensions.get('window').width;
  const isWeb = Dimensions.get('window').width > 600;
  
  // Shrink the gauge width so it's not overwhelmingly large
  const horizontalPadding = Spacing.md * 3;
  const svgWidth = isWeb ? 400 - horizontalPadding : windowWidth - horizontalPadding; 
  
  const strokeWidth = 32; // Extremely thick arcade tachometer
  const padding = 24; // Glow buffer space
  const radius = (svgWidth / 2) - padding; 
  const cx = svgWidth / 2;
  const cy = radius + padding; // Center sits exactly at the bottom radius + padding
  
  // Hard-clip the SVG height to eliminate wasted space at the top and bottom
  const svgHeight = cy + (strokeWidth / 2) + 5; 
  
  const circumference = 2 * Math.PI * radius;
  const halfCircumference = circumference / 2;

  // Tachometer segmentation math (Block width 6, Gap 6)
  const segmentPattern = `6 6`;

  // Max speed scale
  const MAX_SPEED = 25;
  const speedRatio = Math.min(Math.max(gpsSpeed / MAX_SPEED, 0), 1);
  
  const animatedProgress = useRef(new Animated.Value(0)).current;
  
  useEffect(() => {
    Animated.spring(animatedProgress, {
      toValue: speedRatio,
      friction: 5, 
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

  const kcalBurned = Math.round(sessionDistanceMiles * 65);

  return (
    <View style={styles.container}>
      
      {/* MASSIVE SEGMENTED TACHOMETER */}
      <View style={{ width: svgWidth, height: svgHeight, alignItems: 'center', alignSelf: 'center', marginTop: 0 }}>
        <Svg width={svgWidth} height={svgHeight} viewBox={`0 0 ${svgWidth} ${svgHeight}`}>
          <Defs>
            <SvgLinearGradient id="redlineGradient" x1="0%" y1="0%" x2="100%" y2="0%">
              <Stop offset="0%" stopColor="#00FFFF" stopOpacity="1" />
              <Stop offset="40%" stopColor="#8A2BE2" stopOpacity="1" />
              <Stop offset="70%" stopColor="#FF00FF" stopOpacity="1" />
              <Stop offset="90%" stopColor="#FF0000" stopOpacity="1" />
              <Stop offset="100%" stopColor="#FF0000" stopOpacity="1" />
            </SvgLinearGradient>

            <Mask id="progressMask">
              <AnimatedCircle
                cx={cx}
                cy={cy}
                r={radius}
                stroke="#FFFFFF"
                strokeWidth={strokeWidth + 40} // Massively oversize mask to catch outer glow layers
                fill="none"
                strokeDasharray={`${halfCircumference} ${circumference}`}
                strokeDashoffset={strokeDashoffset}
                strokeLinecap="butt"
                transform={`rotate(180 ${cx} ${cy})`}
              />
            </Mask>
          </Defs>
          
          {/* Layer 1: Dark Segmented Track */}
          <Circle
            cx={cx}
            cy={cy}
            r={radius}
            stroke="rgba(255,255,255,0.06)"
            strokeWidth={strokeWidth}
            fill="none"
            strokeDasharray={segmentPattern}
            strokeDashoffset="0"
            strokeLinecap="butt"
            transform={`rotate(180 ${cx} ${cy})`}
          />

          {/* Layer 2: Massive Outer Redline Bloom (Masked) */}
          <Circle
            cx={cx}
            cy={cy}
            r={radius}
            stroke="url(#redlineGradient)"
            strokeWidth={strokeWidth + 24}
            fill="none"
            opacity={0.25}
            strokeDasharray={segmentPattern}
            strokeDashoffset="0"
            strokeLinecap="butt"
            transform={`rotate(180 ${cx} ${cy})`}
            mask="url(#progressMask)"
          />

          {/* Layer 3: Core Segmented Colored Tachometer (Masked) */}
          <Circle
            cx={cx}
            cy={cy}
            r={radius}
            stroke="url(#redlineGradient)"
            strokeWidth={strokeWidth}
            fill="none"
            strokeDasharray={segmentPattern}
            strokeDashoffset="0"
            strokeLinecap="butt"
            transform={`rotate(180 ${cx} ${cy})`}
            mask="url(#progressMask)"
          />
        </Svg>
        
        {/* Speed Text moved slightly up inside the arc bounds */}
        <View style={styles.speedTextContainer}>
          <Text style={[styles.speedValue, { color: '#FFFFFF', textShadowColor: '#00FFFF', textShadowRadius: 20 }]}>
            {gpsSpeed.toFixed(1)}
          </Text>
          <Text style={[styles.speedUnit, { color: '#00FFFF', textShadowColor: '#00FFFF', textShadowRadius: 10 }]}>MPH</Text>
        </View>
      </View>

      {/* COMPACT BOTTOM GRID */}
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

const TelemetryGlassPill = ({ label, value, unit, accent }: { label: string, value: string, unit: string, accent: string }) => (
  <View style={styles.pillContainer}>
    <LinearGradient
      colors={['rgba(255,255,255,0.06)', 'rgba(0,0,0,0.4)']}
      style={styles.pillBackground}
    />
    <View style={[styles.accentTick, { backgroundColor: accent, shadowColor: accent }]} />
    <Text style={styles.pillLabel}>{label}</Text>
    <View style={styles.pillValueContainer}>
      <Text style={[styles.pillValue, { textShadowColor: accent, textShadowRadius: 12 }]}>{value}</Text>
      {unit !== '' && <Text style={styles.pillUnit}>{unit}</Text>}
    </View>
  </View>
);

const styles = StyleSheet.create({
  container: {
    marginHorizontal: Layout.padding,
    marginBottom: Spacing.sm,
  },
  speedTextContainer: {
    position: 'absolute',
    bottom: -10, // Pulled down slightly because the bounding box is clipped very tight
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
    paddingHorizontal: Spacing.xs,
    paddingTop: Spacing.md,
    gap: Spacing.xs, // Ultra dense gap
  },
  metricsRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    gap: Spacing.xs,
  },
  pillContainer: {
    flex: 1,
    paddingVertical: Spacing.sm, // Squeezed vertical padding
    paddingHorizontal: Spacing.xs,
    borderRadius: 8, // Sharper corners for aggressive look
    alignItems: 'center',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.03)',
    borderTopColor: 'rgba(255,255,255,0.12)', 
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
    height: 2,
    shadowOffset: { width: 0, height: -2 },
    shadowOpacity: 1,
    shadowRadius: 5,
    elevation: 4,
  },
  pillLabel: {
    fontSize: 8, // Smaller label
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
  }
});
