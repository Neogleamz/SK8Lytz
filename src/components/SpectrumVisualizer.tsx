import React, { useEffect, useRef } from 'react';
import { View, StyleSheet, Animated } from 'react-native';
import { Colors } from '../theme/theme';

const BARS_COUNT = 30;

export default function SpectrumVisualizer() {
  const animations = useRef(Array(BARS_COUNT).fill(0).map(() => new Animated.Value(0))).current;

  useEffect(() => {
    const runAnimation = (anim: Animated.Value) => {
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
  }, []);

  return (
    <View style={styles.container}>
      <View style={styles.visualizerArea}>
        {animations.map((anim, i) => {
          const height = anim.interpolate({
            inputRange: [0, 1],
            outputRange: [5, 60],
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
                    transform: [{ translateY: anim.interpolate({ inputRange: [0,1], outputRange: [0, -30]}) }],
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
    padding: 16,
    backgroundColor: 'rgba(255,110,0,0.05)',
    borderRadius: 12,
    alignItems: 'center',
    justifyContent: 'center',
    minHeight: 100,
    borderWidth: 1,
    borderColor: 'rgba(255,110,0,0.1)',
  },
  visualizerArea: {
    flexDirection: 'row',
    alignItems: 'flex-end',
    height: 80,
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
