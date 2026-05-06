import React, { useEffect } from 'react';
import { View, Text, StyleSheet, Dimensions } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../../context/ThemeContext';
import { Typography, Spacing, Layout } from '../../theme/theme';
import Svg, { Circle, Defs, LinearGradient, Stop } from 'react-native-svg';
import Animated, { useSharedValue, useAnimatedProps, withSpring } from 'react-native-reanimated';

// Wrapping Circle for Reanimated
const AnimatedCircle = Animated.createAnimatedComponent(Circle);

interface LiveTelemetryHUDProps {
  gpsSpeed: number;
  peakGForce: number;
  sessionDistanceMiles: number;
  sessionDurationSec: number;
}

export const LiveTelemetryHUD: React.FC<LiveTelemetryHUDProps> = ({
  gpsSpeed,
  peakGForce,
  sessionDistanceMiles,
  sessionDurationSec
}) => {
  const { Colors } = useTheme();

  // Screen layout logic (half-circle gauge to save vertical space)
  const windowWidth = Dimensions.get('window').width;
  // If we are on web, limit the width so it doesn't stretch infinitely on big screens
  const isWeb = Dimensions.get('window').width > 600;
  const padding = Spacing.md * 2;
  const svgWidth = isWeb ? 400 - padding : windowWidth - padding; 
  
  const strokeWidth = 14;
  const radius = (svgWidth / 2) - (strokeWidth * 2); // Padding on edges for glow
  const cx = svgWidth / 2;
  const cy = radius + (strokeWidth * 2); // Center Y pushed down so top curve fits
  
  // Circumference of full circle
  const circumference = 2 * Math.PI * radius;
  // Half circle length
  const halfCircumference = circumference / 2;

  // Max speed scale (e.g. 25 mph)
  const MAX_SPEED = 25;
  const speedRatio = Math.min(Math.max(gpsSpeed / MAX_SPEED, 0), 1);
  
  // Reanimated values for smooth sweeping needle
  const animatedProgress = useSharedValue(0);
  
  useEffect(() => {
    // When gpsSpeed changes, sweep the needle smoothly using spring physics
    animatedProgress.value = withSpring(speedRatio, { damping: 15, stiffness: 90 });
  }, [gpsSpeed, speedRatio]);

  const animatedCircleProps = useAnimatedProps(() => {
    const strokeDashoffset = halfCircumference - (halfCircumference * animatedProgress.value);
    return {
      strokeDashoffset,
    };
  });

  const formatDuration = (secs: number) => {
    const m = Math.floor(secs / 60);
    const s = secs % 60;
    return `${m}:${s.toString().padStart(2, '0')}`;
  };

  return (
    <View style={[styles.container, { backgroundColor: Colors.surface, borderColor: Colors.border }]}>
      
      {/* HEADER ROW */}
      <View style={styles.headerRow}>
        <MaterialCommunityIcons name="broadcast" size={16} color={Colors.primary} />
        <Text style={[styles.headerText, { color: Colors.primary }]}>LIVE TELEMETRY</Text>
        <View style={{ flex: 1 }} />
        <MaterialCommunityIcons name="record-circle-outline" size={16} color={Colors.error} />
        <Text style={[styles.headerText, { color: Colors.error }]}>REC</Text>
      </View>

      {/* MASSIVE NEON GAUGE (Half Circle) */}
      <View style={{ width: svgWidth, height: cy + 10, alignItems: 'center', alignSelf: 'center' }}>
        <Svg width={svgWidth} height={cy + 10} viewBox={`0 0 ${svgWidth} ${cy + 10}`}>
          <Defs>
            <LinearGradient id="neonGradient" x1="0%" y1="0%" x2="100%" y2="0%">
              <Stop offset="0%" stopColor={Colors.primary} stopOpacity="1" />
              <Stop offset="100%" stopColor={Colors.accent} stopOpacity="1" />
            </LinearGradient>
          </Defs>
          
          {/* Background Track (Dark) */}
          <Circle
            cx={cx}
            cy={cy}
            r={radius}
            stroke="rgba(255,255,255,0.05)"
            strokeWidth={strokeWidth}
            fill="none"
            strokeDasharray={`${halfCircumference} ${circumference}`}
            strokeDashoffset="0"
            strokeLinecap="round"
            transform={`rotate(180 ${cx} ${cy})`}
          />

          {/* Core Bright Neon Line */}
          <AnimatedCircle
            cx={cx}
            cy={cy}
            r={radius}
            stroke="url(#neonGradient)"
            strokeWidth={strokeWidth}
            fill="none"
            strokeDasharray={`${halfCircumference} ${circumference}`}
            strokeLinecap="round"
            transform={`rotate(180 ${cx} ${cy})`}
            animatedProps={animatedCircleProps}
          />
          
          {/* Subtle Outer Glow Layer */}
          <AnimatedCircle
            cx={cx}
            cy={cy}
            r={radius}
            stroke="url(#neonGradient)"
            strokeWidth={strokeWidth + 12}
            fill="none"
            opacity={0.3}
            strokeDasharray={`${halfCircumference} ${circumference}`}
            strokeLinecap="round"
            transform={`rotate(180 ${cx} ${cy})`}
            animatedProps={animatedCircleProps}
          />
        </Svg>
        
        {/* Absolute Centered Speed Text */}
        <View style={styles.speedTextContainer}>
          <Text style={[styles.speedValue, { color: Colors.text }]}>
            {gpsSpeed.toFixed(1)}
          </Text>
          <Text style={[styles.speedUnit, { color: Colors.primary }]}>MPH</Text>
        </View>
      </View>

      {/* BOTTOM METRICS GRID */}
      <View style={styles.metricsGrid}>
        <View style={[styles.metricBox, { backgroundColor: 'rgba(255,255,255,0.03)' }]}>
          <Text style={[styles.metricLabel, { color: Colors.textDim }]}>DISTANCE</Text>
          <Text style={[styles.metricValue, { color: Colors.text }]}>{sessionDistanceMiles.toFixed(2)}<Text style={styles.metricUnit}> mi</Text></Text>
        </View>
        <View style={[styles.metricBox, { backgroundColor: 'rgba(255,255,255,0.03)' }]}>
          <Text style={[styles.metricLabel, { color: Colors.textDim }]}>G-FORCE</Text>
          <Text style={[styles.metricValue, { color: Colors.text }]}>{peakGForce.toFixed(1)}<Text style={styles.metricUnit}> g</Text></Text>
        </View>
        <View style={[styles.metricBox, { backgroundColor: 'rgba(255,255,255,0.03)' }]}>
          <Text style={[styles.metricLabel, { color: Colors.textDim }]}>TIME</Text>
          <Text style={[styles.metricValue, { color: Colors.text }]}>{formatDuration(sessionDurationSec)}</Text>
        </View>
      </View>

    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    marginHorizontal: Spacing.md,
    marginBottom: Spacing.md,
    borderRadius: 24,
    borderWidth: 1,
    overflow: 'hidden',
  },
  headerRow: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: Spacing.lg,
    paddingTop: Spacing.lg,
    gap: 6,
  },
  headerText: {
    fontFamily: 'monospace',
    fontSize: 12,
    fontWeight: '800',
    letterSpacing: 1.5,
  },
  speedTextContainer: {
    position: 'absolute',
    bottom: 5,
    alignItems: 'center',
  },
  speedValue: {
    fontFamily: 'monospace',
    fontSize: 64,
    fontWeight: '900',
    lineHeight: 70,
  },
  speedUnit: {
    fontFamily: 'monospace',
    fontSize: 16,
    fontWeight: '800',
    letterSpacing: 2,
    marginTop: -5,
  },
  metricsGrid: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    padding: Spacing.md,
    gap: Spacing.sm,
  },
  metricBox: {
    flex: 1,
    paddingVertical: Spacing.md,
    paddingHorizontal: Spacing.sm,
    borderRadius: 16,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.05)',
  },
  metricLabel: {
    fontSize: 10,
    fontWeight: '700',
    letterSpacing: 1,
    marginBottom: 4,
  },
  metricValue: {
    fontFamily: 'monospace',
    fontSize: 18,
    fontWeight: '800',
  },
  metricUnit: {
    fontSize: 12,
    fontWeight: '500',
    color: 'rgba(255,255,255,0.4)',
  }
});
