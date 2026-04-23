import React, { useEffect, useRef } from 'react';
import { Animated, StyleSheet, View, Text } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../../context/ThemeContext';
import { Spacing } from '../../theme/theme';

interface SpectrumAnalyzerProps {
  audioMagnitude: number;
  micSource: 0x26 | 0x27; // 0x26 = App, 0x27 = Device
  color1?: string;
  color2?: string;
  isPoweredOn?: boolean;
}

const BAR_COUNT = 7;

export default function SpectrumAnalyzer({
  audioMagnitude,
  micSource,
  color1 = '#00F0FF',
  color2 = '#FF0055',
  isPoweredOn = true,
}: SpectrumAnalyzerProps) {
  const { isDark } = useTheme();
  
  // Create an Animated.Value for each bar
  const animatedValues = useRef(
    Array.from({ length: BAR_COUNT }).map(() => new Animated.Value(0))
  ).current;

  // Track the continuous ambient animation for Device Mic
  const ambientAnimationRef = useRef<Animated.CompositeAnimation | null>(null);

  useEffect(() => {
    if (!isPoweredOn) {
      // If powered off, flatten all bars
      if (ambientAnimationRef.current) {
        ambientAnimationRef.current.stop();
        ambientAnimationRef.current = null;
      }
      animatedValues.forEach((val) => {
        Animated.spring(val, {
          toValue: 0.1,
          useNativeDriver: false,
        }).start();
      });
      return;
    }

    if (micSource === 0x27) {
      // ── Device Mic (No telemetry): Ambient Pulse Animation ──
      // Create a continuous, slightly offset sine-wave-like bounce
      const animations = animatedValues.map((val, index) => {
        const delay = index * 100;
        return Animated.sequence([
          Animated.delay(delay),
          Animated.loop(
            Animated.sequence([
              Animated.timing(val, {
                toValue: 0.8,
                duration: 400,
                useNativeDriver: false,
              }),
              Animated.timing(val, {
                toValue: 0.2,
                duration: 600,
                useNativeDriver: false,
              }),
            ])
          ),
        ]);
      });
      ambientAnimationRef.current = Animated.parallel(animations);
      ambientAnimationRef.current.start();

    } else {
      // ── App Mic (Telemetry available): Reactive Magnitude ──
      if (ambientAnimationRef.current) {
        ambientAnimationRef.current.stop();
        ambientAnimationRef.current = null;
      }
      
      // Magnitude is 0-255. Normalize to 0.1 - 1.0.
      const normalizedMag = Math.max(0.1, Math.min(1.0, audioMagnitude / 255));
      
      animatedValues.forEach((val, index) => {
        // Add some random variation per bar so it looks like an EQ
        const randomFactor = 0.5 + Math.random() * 0.5; // 0.5 - 1.0
        // Middle bars should generally be taller
        const centerDist = Math.abs(index - Math.floor(BAR_COUNT / 2));
        const curveFactor = 1 - (centerDist * 0.15); // e.g. 1.0 for center, 0.85, 0.7...
        
        let targetHeight = normalizedMag * randomFactor * curveFactor;
        targetHeight = Math.max(0.1, Math.min(1.0, targetHeight));
        
        Animated.timing(val, {
          toValue: targetHeight,
          duration: 100, // Very fast reactive timing
          useNativeDriver: false, // We are animating height (layout property)
        }).start();
      });
    }

    return () => {
      if (ambientAnimationRef.current) {
        ambientAnimationRef.current.stop();
        ambientAnimationRef.current = null;
      }
    };
  }, [audioMagnitude, micSource, isPoweredOn]);

  return (
    <View style={[
      styles.container,
      {
        backgroundColor: isDark ? '#000000' : '#FFFFFF',
        borderColor: isDark ? 'rgba(255,255,255,0.12)' : 'rgba(0,0,0,0.08)'
      }
    ]}>
      {/* ── Status Badge ── */}
      <View style={styles.statusBadge}>
        <MaterialCommunityIcons 
          name={micSource === 0x27 ? "microphone-variant" : "microphone"} 
          size={14} 
          color={isPoweredOn ? color1 : '#666'} 
          style={{ marginRight: 4 }}
        />
        <Text style={[styles.statusText, { color: isPoweredOn ? (isDark ? '#AAA' : '#666') : '#555' }]}>
          {micSource === 0x27 ? "HARDWARE MIC ACTIVE" : "APP MIC ACTIVE"}
        </Text>
      </View>

      {/* ── EQ Bars ── */}
      <View style={styles.eqContainer}>
        {animatedValues.map((animValue, index) => {
          // Interpolate the height from 10% to 100% of the container
          const heightInterpolate = animValue.interpolate({
            inputRange: [0, 1],
            outputRange: ['10%', '100%'],
          });

          // Mix colors based on position
          const isLeft = index < BAR_COUNT / 2;
          const barColor = isLeft ? color1 : color2;

          return (
            <Animated.View
              key={index}
              style={[
                styles.bar,
                {
                  height: heightInterpolate,
                  backgroundColor: barColor,
                  opacity: isPoweredOn ? 0.9 : 0.2,
                  shadowColor: barColor,
                  shadowOffset: { width: 0, height: 0 },
                  shadowOpacity: isPoweredOn ? 0.8 : 0,
                  shadowRadius: 8,
                  elevation: isPoweredOn ? 5 : 0,
                }
              ]}
            />
          );
        })}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: Spacing.sm,
    alignItems: 'center',
    justifyContent: 'flex-end',
    backgroundColor: '#000000',
    borderRadius: 20,
    borderWidth: 1,
    minHeight: 110,
    width: '100%',
    overflow: 'hidden',
    position: 'relative',
  },
  statusBadge: {
    position: 'absolute',
    top: 12,
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: 'rgba(0,0,0,0.4)',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 12,
    zIndex: 10,
  },
  statusText: {
    fontSize: 10,
    fontWeight: 'bold',
    letterSpacing: 0.5,
  },
  eqContainer: {
    flexDirection: 'row',
    alignItems: 'flex-end',
    justifyContent: 'space-between',
    width: '60%',
    height: 60, // Fixed max height for the bars
    marginBottom: 10,
  },
  bar: {
    width: 12,
    borderRadius: 6,
  },
});
