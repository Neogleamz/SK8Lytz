import React, { useEffect, useRef, useMemo } from 'react';
import { View, Text, StyleSheet, Animated, Easing, TouchableOpacity, Image } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../context/ThemeContext';
import { Typography } from '../theme/theme';

interface ScannerAnimationProps {
  deviceCount: number;
  isScanning: boolean;
  onPress?: () => void;
}

const PULSE_COUNT = 8;
const MAX_RADIUS = 380;
const DOTS_PER_RING = 48;

export default function ScannerAnimation({ deviceCount, isScanning, onPress }: ScannerAnimationProps) {
  const { Colors } = useTheme();
  
  // Pulse animations: Each value goes from 0 to 1 (center to edge)
  const pulseAnims = useRef(Array.from({ length: PULSE_COUNT }).map(() => new Animated.Value(0))).current;
  const rotateAnim = useRef(new Animated.Value(0)).current;
  const shimmerAnim = useRef(new Animated.Value(0)).current;
  const detectionAnim = useRef(new Animated.Value(0)).current;
  const prevDeviceCount = useRef(deviceCount);

  useEffect(() => {
    if (deviceCount > prevDeviceCount.current) {
      Animated.sequence([
        Animated.timing(detectionAnim, { toValue: 1, duration: 100, useNativeDriver: true }),
        Animated.timing(detectionAnim, { toValue: 0, duration: 400, useNativeDriver: true }),
      ]).start();
    }
    prevDeviceCount.current = deviceCount;
  }, [deviceCount]);

  useEffect(() => {
    // Reset and Start Continuous Sonar Pulses
    pulseAnims.forEach(anim => {
      anim.stopAnimation();
      anim.setValue(0);
    });
    shimmerAnim.stopAnimation();
    shimmerAnim.setValue(0);

    const pulseDuration = isScanning ? 2000 : 8000;
    const staggerDelay = pulseDuration / PULSE_COUNT;

    const animations = pulseAnims.map((anim, i) => {
      return Animated.loop(
        Animated.sequence([
          Animated.delay(i * staggerDelay),
          Animated.timing(anim, {
            toValue: 1,
            duration: pulseDuration,
            easing: isScanning ? Easing.out(Easing.quad) : Easing.linear,
            useNativeDriver: true,
          }),
          Animated.timing(anim, { toValue: 0, duration: 0, useNativeDriver: true })
        ])
      );
    });

    const rotateAnimLoop = Animated.loop(
      Animated.timing(rotateAnim, {
        toValue: 1,
        duration: isScanning ? 2000 : 5000, 
        easing: Easing.linear,
        useNativeDriver: true,
      })
    );

    const shimmerLoop = Animated.loop(
      Animated.sequence([
        Animated.timing(shimmerAnim, { 
          toValue: 1, 
          duration: isScanning ? 300 : 800, 
          easing: Easing.inOut(Easing.quad), 
          useNativeDriver: true 
        }),
        Animated.timing(shimmerAnim, { 
          toValue: 0, 
          duration: isScanning ? 300 : 800, 
          easing: Easing.inOut(Easing.quad), 
          useNativeDriver: true 
        }),
      ])
    );

    // Start pulses and shimmer in parallel
    Animated.parallel([...animations, shimmerLoop]).start();
    // Start rotation separately to ensure it doesn't get blocked
    rotateAnimLoop.start();

    return () => {
      pulseAnims.forEach(anim => anim.stopAnimation());
      shimmerAnim.stopAnimation();
      rotateAnim.stopAnimation();
    };
  }, [isScanning]);

  const spin = rotateAnim.interpolate({
    inputRange: [0, 1],
    outputRange: ['0deg', '360deg'],
  });

  const renderRainbowRing = (pulseAnim: Animated.Value, index: number) => {
    const dots = [];
    const colorSpectrum = [
      '#FF0000', '#FF7F00', '#FFFF00', '#00FF00', 
      '#0000FF', '#4B0082', '#9400D3', '#FF00FF'
    ];

    // Outward moving radius: start at button edge (50), end at MAX_RADIUS
    const radius = pulseAnim.interpolate({
      inputRange: [0, 1],
      outputRange: [50, MAX_RADIUS]
    });

    // Fade out as it expands
    const opacity = pulseAnim.interpolate({
      inputRange: [0, 0.1, 0.8, 1],
      outputRange: [0, 0.6, 0.4, 0]
    });

    for (let i = 0; i < DOTS_PER_RING; i++) {
        const angle = (i / DOTS_PER_RING) * 2 * Math.PI;
        // Each dot gets a color from the rainbow
        const color = colorSpectrum[i % colorSpectrum.length];
        
        // Slight shimmer highlight
        const isPick = (i + index * 5) % 3 === 0;
        const shimmerAdd = isPick ? shimmerAnim.interpolate({ 
            inputRange: [0, 1], 
            outputRange: [0, 0.5] 
        }) : 0;

        dots.push(
          <Animated.View
            key={`pulse-${index}-dot-${i}`}
            style={[
              styles.ledDot,
              {
                backgroundColor: color,
                shadowColor: color,
                opacity: isScanning ? Animated.add(opacity, shimmerAdd) : opacity,
                transform: [
                  { 
                    translateX: Animated.multiply(radius, Math.cos(angle)) 
                  },
                  { 
                    translateY: Animated.multiply(radius, Math.sin(angle)) 
                  },
                  { 
                    scale: isScanning ? pulseAnim.interpolate({ inputRange:[0, 0.5, 1], outputRange:[0.4, 1.6, 0.4] }) : 0.7 
                  }
                ],
              },
            ]}
          />
        );
    }
    return dots;
  };

  return (
    <View style={styles.container}>
      <View style={styles.radarContainer}>
        {/* Sonar Rainbow Pulses */}
        {pulseAnims.map((anim, i) => renderRainbowRing(anim, i))}

        {/* Sonar Sweep Arm */}
        {isScanning && (
          <Animated.View 
            style={[
              styles.sweep, 
              { transform: [{ rotate: spin }] }
            ]}
          >
            <View style={styles.sweepGlow} />
          </Animated.View>
        )}

        {/* Central Scan Button */}
        <TouchableOpacity 
          activeOpacity={0.7} 
          onPress={onPress} 
          disabled={isScanning}
          style={styles.centralButtonWrapper}
        >
          <Animated.View 
            style={[
              styles.center, 
              { 
                backgroundColor: '#FFFFFF',
                transform: [{ scale: detectionAnim.interpolate({ inputRange:[0,1], outputRange:[1, 1.4] }) }],
                borderColor: isScanning 
                  ? shimmerAnim.interpolate({ inputRange:[0,1], outputRange:['#00f0ff', '#80faff'] }) 
                  : 'rgba(0,0,0,0.05)',
                borderWidth: isScanning ? 4 : 1,
                shadowColor: isScanning ? '#00f0ff' : '#000000',
                shadowRadius: isScanning 
                  ? shimmerAnim.interpolate({ inputRange:[0,1], outputRange:[25, 45] }) 
                  : 12,
                shadowOpacity: isScanning ? 0.9 : 0.2,
              }
            ]}
          >
            {isScanning ? (
              <View style={{ alignItems: 'center', justifyContent: 'center', width: '100%', height: '100%' }}>
                <Image 
                  source={require('../../assets/favicon.png')} 
                  style={{ width: 75, height: 75, resizeMode: 'contain', opacity: 0.95 }}
                />
                {/* Micro LED Dots on Button - Randomized Shimmer */}
                {[...Array(9)].map((_, i) => (
                  <Animated.View 
                    key={`center-dot-${i}`}
                    style={{
                      position: 'absolute',
                      width: 5,
                      height: 5,
                      borderRadius: 2.5,
                      backgroundColor: i % 3 === 0 ? '#00f0ff' : (i % 3 === 1 ? '#ffffff' : '#FFD700'),
                      shadowColor: '#00f0ff',
                      shadowRadius: 10,
                      shadowOpacity: 1,
                      top: 15 + (i * 18) % 70,
                      left: 15 + (i * 13) % 70,
                      transform: [{
                        scale: shimmerAnim.interpolate({
                          inputRange: [0, 0.3, 0.6, 1],
                          outputRange: [1, i % 2 === 0 ? 1.6 : 0.8, i % 2 === 0 ? 0.8 : 1.6, 1]
                        })
                      }],
                      opacity: shimmerAnim.interpolate({ 
                        inputRange: [0, 0.2, 0.5, 0.8, 1], 
                        outputRange: [0.4, i % 2 === 0 ? 1 : 0.1, 0.6, i % 2 === 0 ? 0.1 : 1, 0.4] 
                      })
                    }}
                  />
                ))}
              </View>
            ) : (
              <View style={{ alignItems: 'center', justifyContent: 'center' }}>
                <Image 
                  source={require('../../assets/favicon.png')} 
                  style={{ width: 85, height: 85, resizeMode: 'contain' }}
                />
              </View>
            )}
          </Animated.View>
        </TouchableOpacity>

        {/* Detected Controller "Blips" */}
        {isScanning && Array.from({ length: Math.min(deviceCount, 8) }).map((_, i) => {
           const angle = (i * 137.5) * Math.PI / 180; // Golden angle
           const r = 40 + (i * 12);
           return (
             <View 
               key={`blip-${i}`} 
               style={[
                 styles.blip, 
                 { 
                   backgroundColor: '#FF00F0',
                   shadowColor: '#FF00F0',
                   transform: [
                     { translateX: r * Math.cos(angle) },
                     { translateY: r * Math.sin(angle) }
                   ]
                 }
               ]} 
             />
           );
        })}
      </View>

      <View style={styles.statusTextContainer}>
        <Text style={[Typography.title, { color: Colors.primary, textAlign: 'center', height: 24, fontWeight: 'bold' }]}>
          {isScanning ? (deviceCount > 0 ? `PAIRED (${deviceCount} CONTROLLERS)` : 'SEARCHING...') : 'DISCOVERY MODE'}
        </Text>
        <Text style={[Typography.caption, { color: Colors.textMuted, marginTop: 4, letterSpacing: 2 }]}>
          {isScanning ? 'DETECTING SK8LYTZ STRIPS' : 'TAP RADAR TO DISCOVER'}
        </Text>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 10,
  },
  radarContainer: {
    width: 360,
    height: 360,
    alignItems: 'center',
    justifyContent: 'center',
    position: 'relative',
    // Allow dots to flow off-screen
    overflow: 'visible', 
  },
  bgRing: {
    position: 'absolute',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.05)',
  },
  ledDot: {
    position: 'absolute',
    width: 4.5,
    height: 4.5,
    borderRadius: 2.25,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 1,
    shadowRadius: 10,
    elevation: 5,
  },
  sweep: {
    position: 'absolute',
    width: 400,
    height: 400,
    borderRadius: 200,
    overflow: 'hidden',
  },
  sweepGlow: {
    position: 'absolute',
    top: 200,
    left: 200,
    width: 200,
    height: 200,
    borderTopRightRadius: 200,
    backgroundColor: 'rgba(0, 240, 255, 0.12)',
    borderRightWidth: 4,
    borderTopWidth: 4,
    borderColor: 'rgba(0, 240, 255, 0.5)',
  },
  idleHalo: {
    position: 'absolute',
    width: 140,
    height: 140,
    borderRadius: 70,
    borderWidth: 2,
  },
  centralButtonWrapper: {
    zIndex: 30,
  },
  center: {
    width: 120,
    height: 120,
    borderRadius: 60,
    alignItems: 'center',
    justifyContent: 'center',
    elevation: 8,
  },
  blip: {
    position: 'absolute',
    width: 8,
    height: 8,
    borderRadius: 4,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 1,
    shadowRadius: 10,
    elevation: 12,
  },
  statusTextContainer: {
    marginTop: 15,
    alignItems: 'center',
  },
});
