import React, { useEffect, useRef } from 'react';
import { View, StyleSheet, Animated } from 'react-native';
import { Colors } from '../theme/theme';

const BARS_COUNT = 30;

export default function SpectrumVisualizer({ magnitude }: { magnitude?: number }) {
  const animations = useRef(Array(BARS_COUNT).fill(0).map(() => new Animated.Value(0))).current;
  const loopActive = useRef(true);

  useEffect(() => {
    if (magnitude !== undefined) {
      loopActive.current = false;
      animations.forEach((anim, i) => {
        // Add pseudo-frequency randomness but anchor to magnitude to make it dance realistically to the DB peaks
        const randomness = Math.random() * 0.5 + 0.5;
        Animated.spring(anim, {
          toValue: magnitude * randomness,
          useNativeDriver: false,
          speed: 24,
          bounciness: 8
        }).start();
      });
    } else {
      loopActive.current = true;
      const runAnimation = (anim: Animated.Value) => {
        if (!loopActive.current) return;
        Animated.sequence([
          Animated.timing(anim, {
            toValue: Math.random(),
            duration: 300 + Math.random() * 500,
            useNativeDriver: false,
          }),
          Animated.timing(anim, {
            toValue: 0.2 + Math.random() * 0.3,
            duration: 300 + Math.random() * 500,
            useNativeDriver: false,
          })
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
            outputRange: [4, 40],
          });
          
          // Color based on index (rainbow)
          const hue = (i / BARS_COUNT) * 280;
          const color = `hsl(${hue}, 80%, 60%)`;

          return (
            <View key={i} style={styles.barContainer}>
              <Animated.View 
                style={[
                  styles.bar, 
                  { 
                    height, 
                    backgroundColor: color,
                    borderTopLeftRadius: 3,
                    borderTopRightRadius: 3,
                  }
                ]} 
              />
              {/* Top dots/peaks effect from screenshot */}
              <Animated.View 
                style={[
                  styles.peak, 
                  { 
                    transform: [{ translateY: anim.interpolate({ inputRange: [0,1], outputRange: [0, -20]}) }],
                    opacity: 0.8
                  }
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
    padding: 4,
    backgroundColor: 'rgba(255,110,0,0.05)',
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
    minHeight: 50,
    borderWidth: 1,
    borderColor: 'rgba(255,110,0,0.1)',
  },
  visualizerArea: {
    flexDirection: 'row',
    alignItems: 'flex-end',
    height: 40,
    width: '100%',
    justifyContent: 'space-between',
  },
  barContainer: {
    flex: 1,
    alignItems: 'center',
    marginHorizontal: 1,
  },
  bar: {
    width: '100%',
  },
  peak: {
    width: 3,
    height: 3,
    backgroundColor: '#FFF',
    borderRadius: 2,
    position: 'absolute',
    top: 5,
  }
});
