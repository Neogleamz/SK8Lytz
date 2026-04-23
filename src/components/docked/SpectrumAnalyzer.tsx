import React, { useEffect, useRef } from 'react';
import { Animated, StyleSheet, View, Text } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../../context/ThemeContext';
import { Spacing } from '../../theme/theme';
import { hexToRgb } from '../../utils/ColorUtils';

interface SpectrumAnalyzerProps {
  audioMagnitude: number;
  micSource: 'APP' | 'DEVICE';
  color1?: string;
  color2?: string;
  isPoweredOn?: boolean;
  hasMicPermission?: boolean;
  onRequestMicPermission?: () => void;
}

const BARS_COUNT = 16;
const BAR_MIN_H = 6;
const BAR_MAX_H = 150;
const BAR_WIDTH = 12; // Wider bars for fewer points

export default function SpectrumAnalyzer({
  audioMagnitude,
  micSource,
  color1 = '#00F0FF',
  color2 = '#FF0055',
  isPoweredOn = true,
  hasMicPermission = true,
  onRequestMicPermission,
}: SpectrumAnalyzerProps) {
  const { isDark } = useTheme();
  
  // Create an Animated.Value for each bar, initialized at a low floor
  const animatedValues = useRef(
    Array.from({ length: BARS_COUNT }).map(() => new Animated.Value(0.15))
  ).current;

  // Track the continuous ambient animation for Device Mic
  const ambientAnimationRef = useRef<Animated.CompositeAnimation | null>(null);

  useEffect(() => {
    if (!isPoweredOn) {
      if (ambientAnimationRef.current) {
        ambientAnimationRef.current.stop();
        ambientAnimationRef.current = null;
      }
      animatedValues.forEach((val) => {
        Animated.spring(val, {
          toValue: 0.1,
          useNativeDriver: false,
          speed: 12,
        }).start();
      });
      return;
    }

    if (micSource === 'DEVICE') {
      // ── Device Mic (No telemetry): Ambient Pulse Animation ──
      // Smooth, continuous sine-wave-like bounce across the 30 bars
      const runAnimation = (anim: Animated.Value, delay: number) => {
        Animated.sequence([
          Animated.delay(delay),
          Animated.loop(
            Animated.sequence([
              Animated.timing(anim, {
                toValue: 0.3 + Math.random() * 0.5,
                duration: 600 + Math.random() * 400,
                useNativeDriver: false,
              }),
              Animated.timing(anim, {
                toValue: 0.15 + Math.random() * 0.2,
                duration: 600 + Math.random() * 400,
                useNativeDriver: false,
              }),
            ])
          )
        ]).start();
      };
      
      animatedValues.forEach((val, i) => runAnimation(val, i * 40));
      // We don't store the loop in ambientAnimationRef because it's a bunch of independent loops.
      // We can just rely on the effect cleanup or power off to stop them.
      // Wait, we need to stop them if switching to APP mic!
      // Actually, calling stopAnimation or just letting the next effect override them works.
      // But to be clean, let's just use parallel.
      const loops = animatedValues.map((val, i) => {
        return Animated.sequence([
          Animated.delay(i * 30),
          Animated.loop(
            Animated.sequence([
              Animated.timing(val, {
                toValue: 0.4 + Math.random() * 0.4,
                duration: 500 + Math.random() * 300,
                useNativeDriver: false,
              }),
              Animated.timing(val, {
                toValue: 0.15 + Math.random() * 0.2,
                duration: 500 + Math.random() * 300,
                useNativeDriver: false,
              }),
            ])
          )
        ]);
      });
      ambientAnimationRef.current = Animated.parallel(loops);
      ambientAnimationRef.current.start();

    } else {
      // ── App Mic (Telemetry available): Reactive Magnitude ──
      if (ambientAnimationRef.current) {
        ambientAnimationRef.current.stop();
        ambientAnimationRef.current = null;
      }
      
      // Magnitude is 0-255. Normalize to 0.0 - 1.0.
      const normalizedMag = Math.max(0, Math.min(1.0, audioMagnitude / 255));
      
      animatedValues.forEach((anim, index) => {
        // Lift the floor: even at magnitude=0 we show a little activity
        const floor = 0.15;
        // Randomize the response per bar so it looks like an EQ
        const randomness = Math.random() * 0.4 + 0.6; // 0.6–1.0
        // Middle bars should generally peak higher
        const centerDist = Math.abs(index - (BARS_COUNT / 2));
        const curveFactor = 1 - (centerDist / (BARS_COUNT / 2)) * 0.3; // 1.0 at center, 0.7 at edges
        
        let toValue = floor + ((1 - floor) * normalizedMag * randomness * curveFactor);
        toValue = Math.max(0.1, Math.min(1.0, toValue));
        
        Animated.spring(anim, {
          toValue,
          useNativeDriver: false,
          speed: 28,
          bounciness: 6,
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
          name={micSource === 'DEVICE' ? "microphone-variant" : "microphone"} 
          size={14} 
          color={isPoweredOn ? color1 : '#666'} 
          style={{ marginRight: 4 }}
        />
        <Text style={[styles.statusText, { color: isPoweredOn ? (isDark ? '#AAA' : '#666') : '#555' }]}>
          {micSource === 'DEVICE' ? "HARDWARE MIC ACTIVE" : "APP MIC ACTIVE"}
        </Text>
      </View>

      {/* ── Mic Permission Overlay ── */}
      {micSource === 'APP' && !hasMicPermission && (
        <View style={styles.permissionOverlay}>
          <MaterialCommunityIcons name="microphone-off" size={24} color="#FF4444" style={{ marginBottom: 4 }} />
          <Text style={styles.permissionText}>Microphone Access Required</Text>
          <Text style={styles.permissionSubText}>Enable in settings to use App Mic</Text>
          <View style={styles.permissionButton} onTouchEnd={onRequestMicPermission}>
            <Text style={styles.permissionButtonText}>ENABLE MIC</Text>
          </View>
        </View>
      )}

      {/* ── EQ Bars ── */}
      <View style={[styles.visualizerArea, { opacity: (micSource === 'APP' && !hasMicPermission) ? 0.1 : 1.0 }]}>
        {animatedValues.map((animValue, index) => {
          const height = animValue.interpolate({
            inputRange: [0, 1],
            outputRange: [BAR_MIN_H, BAR_MAX_H],
          });

          return (
            <View key={index} style={styles.barContainer}>
              <Animated.View
                style={[
                  styles.bar,
                  {
                    height,
                    backgroundColor: color1,
                    opacity: isPoweredOn ? 1.0 : 0.2,
                    shadowColor: color1,
                    shadowOpacity: isPoweredOn ? 0.7 : 0,
                    shadowRadius: 6,
                    shadowOffset: { width: 0, height: 0 },
                    elevation: isPoweredOn ? 4 : 0,
                  },
                ]}
              />
              {/* Peak dot */}
              <Animated.View
                style={[
                  styles.peak,
                  {
                    // translateY pushes the peak dot to sit on top of the bar
                    transform: [{ translateY: animValue.interpolate({ inputRange: [0, 1], outputRange: [0, -(BAR_MAX_H - 8)] }) }],
                    backgroundColor: color2,
                    opacity: isPoweredOn ? 1.0 : 0.2,
                    shadowColor: color2,
                    shadowOpacity: isPoweredOn ? 0.8 : 0,
                    shadowRadius: 4,
                  },
                ]}
              />
            </View>
          );
        })}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    paddingHorizontal: Spacing.sm,
    paddingVertical: Spacing.xs,
    alignItems: 'center',
    justifyContent: 'flex-end', // Align bars to bottom
    borderRadius: 20,
    borderWidth: 1,
    minHeight: 210,
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
  permissionOverlay: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: 'rgba(0,0,0,0.6)',
    zIndex: 20,
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 20,
  },
  permissionText: {
    color: '#FFF',
    fontSize: 14,
    fontWeight: 'bold',
  },
  permissionSubText: {
    color: '#AAA',
    fontSize: 11,
    marginTop: 2,
    marginBottom: 8,
  },
  permissionButton: {
    backgroundColor: '#FF4444',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 8,
  },
  permissionButtonText: {
    color: '#FFF',
    fontSize: 10,
    fontWeight: 'bold',
  },
  visualizerArea: {
    flexDirection: 'row',
    alignItems: 'flex-end',
    height: BAR_MAX_H,
    width: '100%',
    justifyContent: 'center',
    gap: Spacing.sm,
    marginTop: 20, // Pushes it down below the absolute badge
  },
  barContainer: {
    flex: 1,
    alignItems: 'center',
  },
  bar: {
    width: '100%',
    borderTopLeftRadius: 4,
    borderTopRightRadius: 4,
  },
  peak: {
    width: '100%',
    height: 3,
    borderRadius: 2,
    position: 'absolute',
    top: 2,
  },
});
