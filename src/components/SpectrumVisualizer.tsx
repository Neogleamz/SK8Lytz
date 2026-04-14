import React, { useEffect, useRef } from 'react';
import { Animated, StyleSheet, View } from 'react-native';
import { Spacing } from '../theme/theme';

const BARS_COUNT = 30;
const BAR_MIN_H = 6;   // Minimum visible height even at silence
const BAR_MAX_H = 48;  // Maximum bar height at full magnitude
const BAR_WIDTH = 5;   // Fixed width per bar — prevents too-wide bars on large screens

export default function SpectrumVisualizer({ magnitude }: { magnitude?: number }) {
  const animations = useRef(Array(BARS_COUNT).fill(0).map(() => new Animated.Value(0.15))).current;
  const loopActive = useRef(true);

  useEffect(() => {
    if (magnitude !== undefined) {
      loopActive.current = false;
      animations.forEach((anim, _i) => {
        // Lift the floor: even at magnitude=0 we show a little activity.
        // Map 0..1 → 0.15..1.0 so bars never fully collapse.
        const randomness = Math.random() * 0.4 + 0.6; // 0.6–1.0
        const floor = 0.15;
        const toValue = floor + (1 - floor) * magnitude * randomness;
        Animated.spring(anim, {
          toValue,
          useNativeDriver: false,
          speed: 28,
          bounciness: 6,
        }).start();
      });
    } else {
      // Demo loop mode when no magnitude is supplied
      loopActive.current = true;
      const runAnimation = (anim: Animated.Value) => {
        if (!loopActive.current) return;
        Animated.sequence([
          Animated.timing(anim, {
            toValue: 0.3 + Math.random() * 0.7,
            duration: 300 + Math.random() * 500,
            useNativeDriver: false,
          }),
          Animated.timing(anim, {
            toValue: 0.15 + Math.random() * 0.3,
            duration: 300 + Math.random() * 500,
            useNativeDriver: false,
          }),
        ]).start(() => runAnimation(anim));
      };
      animations.forEach(runAnimation);
    }
  }, [magnitude]);

  return (
    <View style={styles.container}>
      <View style={styles.visualizerArea}>
        {animations.map((anim, i) => {
          const height = anim.interpolate({
            inputRange: [0, 1],
            outputRange: [BAR_MIN_H, BAR_MAX_H],
          });

          // Rainbow spectrum across bars
          const hue = (i / BARS_COUNT) * 280;
          const color = `hsl(${hue}, 90%, 60%)`;

          return (
            <View key={i} style={styles.barContainer}>
              <Animated.View
                style={[
                  styles.bar,
                  {
                    height,
                    backgroundColor: color,
                    shadowColor: color,
                    shadowOpacity: 0.7,
                    shadowRadius: 4,
                    shadowOffset: { width: 0, height: 0 },
                    elevation: 4,
                  },
                ]}
              />
              {/* Peak dot */}
              <Animated.View
                style={[
                  styles.peak,
                  {
                    transform: [{ translateY: anim.interpolate({ inputRange: [0, 1], outputRange: [0, -(BAR_MAX_H - 8)] }) }],
                    backgroundColor: color,
                    opacity: 0.9,
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
    backgroundColor: 'rgba(0,0,0,0.4)',
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
    minHeight: 56,
    borderWidth: 1,
    borderColor: 'rgba(255,110,0,0.15)',
  },
  visualizerArea: {
    flexDirection: 'row',
    alignItems: 'flex-end',
    height: BAR_MAX_H,
    width: '100%',
    justifyContent: 'center',
    gap: Spacing.xxs,
  },
  barContainer: {
    width: BAR_WIDTH,
    alignItems: 'center',
  },
  bar: {
    width: BAR_WIDTH,
    borderTopLeftRadius: 2,
    borderTopRightRadius: 2,
  },
  peak: {
    width: BAR_WIDTH,
    height: 2,
    borderRadius: 1,
    position: 'absolute',
    top: 2,
  },
});
