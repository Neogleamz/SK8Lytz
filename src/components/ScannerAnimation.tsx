import React, { useEffect, useMemo, useRef } from 'react';
import { Animated, Easing, Image, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { useTheme } from '../context/ThemeContext';
import { Spacing, Typography } from '../theme/theme';

interface ScannerAnimationProps {
  deviceCount: number;
  isScanning: boolean;
  isScanProbing?: boolean;
  onPress?: () => void;
}

const PULSE_COUNT = 8;
const MAX_RADIUS = 280; // Extended boundary past standard screen widths
const BASE_RADIUS = 25;
const DOTS_PER_RING = 32;

export default function ScannerAnimation({ deviceCount, isScanning, isScanProbing = false, onPress }: ScannerAnimationProps) {
  const { Colors } = useTheme();
  const isActive = isScanning || isScanProbing; // unified active state
  
  // Pulse animations: Each value goes from 0 to 1 (center to edge)
  const pulseAnims = useRef(Array.from({ length: PULSE_COUNT }).map(() => new Animated.Value(0))).current;
  const rotateAnim = useRef(new Animated.Value(0)).current;
  const shimmerAnim = useRef(new Animated.Value(0)).current;
  const detectionAnim = useRef(new Animated.Value(0)).current;
  const prevDeviceCount = useRef(deviceCount);

  // Two sets of pre-calculated arrays to prevent bridge-blocking calculations during active scanning.
  const staticIdleRings = useMemo(() => {
    const rings = [];
    const colorSpectrum = [
      '#FF0000', '#FF7F00', '#FFFF00', '#00FF00', 
      '#0000FF', '#4B0082', '#9400D3', '#FF00FF'
    ];
    for (let r = 0; r < PULSE_COUNT; r++) {
      const dots = [];
      for (let i = 0; i < DOTS_PER_RING; i++) {
          if (Math.random() < 0.65) continue;
          const angle = (i / DOTS_PER_RING) * 2 * Math.PI;
          const color = colorSpectrum[Math.floor(Math.random() * colorSpectrum.length)];
          dots.push({
              x: BASE_RADIUS * Math.cos(angle) - 1.5,
              y: BASE_RADIUS * Math.sin(angle) - 1.5,
              color
          });
      }
      rings.push(dots);
    }
    return rings;
  }, []);

  const staticActiveRings = useMemo(() => {
    const rings = [];
    const colorSpectrum = [
      '#FF0000', '#FF7F00', '#FFFF00', '#00FF00', 
      '#0000FF', '#4B0082', '#9400D3', '#FF00FF'
    ];
    // Double density when scanning linearly
    const ACTIVE_DOTS = DOTS_PER_RING * 2;
    for (let r = 0; r < PULSE_COUNT; r++) {
      const dots = [];
      for (let i = 0; i < ACTIVE_DOTS; i++) {
          // 0% skip rate, full pure dense signal pattern
          const angle = (i / ACTIVE_DOTS) * 2 * Math.PI;
          const color = colorSpectrum[Math.floor(Math.random() * colorSpectrum.length)];
          dots.push({
              x: BASE_RADIUS * Math.cos(angle) - 1.5,
              y: BASE_RADIUS * Math.sin(angle) - 1.5,
              color
          });
      }
      rings.push(dots);
    }
    return rings;
  }, []);

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

    const pulseDuration = isActive ? 1200 : 8000;
    const staggerDelay = pulseDuration / PULSE_COUNT;

    const animations = pulseAnims.map((anim, i) => {
      return Animated.loop(
        Animated.sequence([
          Animated.delay(i * staggerDelay),
          Animated.timing(anim, {
            toValue: 1,
            duration: pulseDuration,
            easing: Easing.out(Easing.quad), // smoother expansion curve
            useNativeDriver: true,
          }),
          Animated.timing(anim, { toValue: 0, duration: 0, useNativeDriver: true })
        ])
      );
    });

    const rotateAnimLoop = Animated.loop(
      Animated.timing(rotateAnim, {
        toValue: 1,
        duration: isActive ? 3500 : 8000,
        easing: Easing.linear,
        useNativeDriver: true,
      })
    );

    const shimmerLoop = Animated.loop(
      Animated.sequence([
        Animated.timing(shimmerAnim, { 
          toValue: 1, 
          duration: isActive ? 150 : 800, 
          easing: Easing.inOut(Easing.quad), 
          useNativeDriver: true 
        }),
        Animated.timing(shimmerAnim, { 
          toValue: 0, 
          duration: isActive ? 150 : 800, 
          easing: Easing.inOut(Easing.quad), 
          useNativeDriver: true 
        }),
      ])
    );

    // Start pulses and shimmer in parallel
    Animated.parallel([...animations, shimmerLoop]).start();
    rotateAnimLoop.start();

    return () => {
      pulseAnims.forEach(anim => anim.stopAnimation());
      shimmerAnim.stopAnimation();
      rotateAnim.stopAnimation();
    };
  }, [isActive]);

  const spin = rotateAnim.interpolate({
    inputRange: [0, 1],
    outputRange: ['0deg', '360deg'],
  });

  const renderRainbowRingContainer = (pulseAnim: Animated.Value, index: number) => {
    // Massive DOM/Math optimization: Instead of evaluating geometry via Native UI thread
    // over 672 individual objects concurrently, we evaluate 8 scale boundaries globally.
    // The dots are hard-stamped inside this boundary container natively!
    const scale = pulseAnim.interpolate({
      inputRange: [0, 1],
      outputRange: [1, MAX_RADIUS / BASE_RADIUS]
    });

    const opacity = pulseAnim.interpolate({
      inputRange: [0, 0.1, 0.85, 1],
      outputRange: [0, 0.8, 0.5, 0]
    });

    const ringsArray = isActive ? staticActiveRings : staticIdleRings;

    return (
      <Animated.View
        key={`pulse-ring-${index}`}
        style={{
          position: 'absolute',
          width: BASE_RADIUS * 2,
          height: BASE_RADIUS * 2,
          opacity: opacity,
          transform: [{ scale }],
        }}
        pointerEvents="none"
      >
        {ringsArray[index].map((dot: any, i: number) => (
          <View
            key={`dot-${i}`}
            style={{
              position: 'absolute',
              left: BASE_RADIUS + dot.x,
              top: BASE_RADIUS + dot.y,
              width: 3,
              height: 3,
              borderRadius: 1.5,
              backgroundColor: dot.color,
              shadowColor: dot.color,
              shadowOffset: { width: 0, height: 0 },
              shadowOpacity: 1,
              shadowRadius: 4,
              elevation: 4,
            }}
          />
        ))}
      </Animated.View>
    );
  };

  return (
    <View style={styles.container}>
      <View style={styles.radarContainer}>
        {/* Static Sonar Grid Overlay */}
        <View style={styles.sonarGridRing1} pointerEvents="none" />
        <View style={styles.sonarGridRing2} pointerEvents="none" />
        <View style={styles.sonarGridRing3} pointerEvents="none" />
        <View style={styles.sonarGridCrosshairX} pointerEvents="none" />
        <View style={styles.sonarGridCrosshairY} pointerEvents="none" />

        {/* Sonar Rainbow Pulses */}
        {pulseAnims.map((anim, i) => renderRainbowRingContainer(anim, i))}

        {/* Sonar Sweep Arm — active during scan AND probe */}
        {isActive && (
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
          disabled={isActive}
          style={styles.centralButtonWrapper}
        >
          <Animated.View 
            style={[
              styles.center, 
              { 
                backgroundColor: '#FFFFFF',
                transform: [{ scale: detectionAnim.interpolate({ inputRange:[0,1], outputRange:[1, 1.4] }) }],
                borderColor: isActive 
                  ? shimmerAnim.interpolate({ inputRange:[0,1], outputRange:['#00f0ff', '#80faff'] }) 
                  : 'rgba(0,0,0,0.05)',
                borderWidth: isActive ? 4 : 1,
                shadowColor: isActive ? (isScanProbing ? '#a855f7' : '#00f0ff') : '#000000',
                shadowRadius: isActive 
                  ? shimmerAnim.interpolate({ inputRange:[0,1], outputRange:[25, 45] }) 
                  : 12,
                shadowOpacity: isActive ? 0.9 : 0.2,
              }
            ]}
          >
            {isActive ? (
              <View style={{ alignItems: 'center', justifyContent: 'center', width: '100%', height: '100%' }}>
                <Image 
                  source={require('../../assets/favicon.png')} 
                  style={{ width: 38, height: 38, resizeMode: 'contain', opacity: 0.95 }}
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
                  style={{ width: 42, height: 42, resizeMode: 'contain' }}
                />
              </View>
            )}
          </Animated.View>
        </TouchableOpacity>

        {/* Detected Controller "Blips" — show during scan and probe */}
        {isActive && Array.from({ length: Math.min(deviceCount, 8) }).map((_, i) => {
           const angle = (i * 137.5) * Math.PI / 180; // Golden angle
           const r = 20 + (i * 6);
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
        {/* Inline status text — unified scan + probe phases */}
        <View style={styles.inlineStatus} pointerEvents="none">
          <Text style={[Typography.title, { color: isScanProbing ? '#a855f7' : Colors.primary, textAlign: 'center', fontWeight: 'bold', fontSize: 12 }]}>
            {isScanning
              ? (deviceCount > 0 ? `FOUND (${deviceCount})` : 'SEARCHING...')
              : isScanProbing
                ? (deviceCount > 0 ? `FOUND (${deviceCount})` : 'PROBING...')
                : 'TAP TO DISCOVER'}
          </Text>
          {isScanning && (
            <Text style={[Typography.caption, { color: Colors.textMuted, marginTop: Spacing.xxs, letterSpacing: 1.5, fontSize: 10 }]}>
              DETECTING SK8LYTZ STRIPS
            </Text>
          )}
          {isScanProbing && !isScanning && (
            <Text style={[Typography.caption, { color: '#a855f7', marginTop: Spacing.xxs, letterSpacing: 1.5, fontSize: 10 }]}>
              PROBING HARDWARE...
            </Text>
          )}
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: Spacing.md,
  },
  radarContainer: {
    width: 180,
    height: 180,
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
  sonarGridRing1: {
    position: 'absolute', width: 220, height: 220, borderRadius: 110, 
    borderWidth: 1, borderColor: 'rgba(0, 240, 255, 0.1)',
  },
  sonarGridRing2: {
    position: 'absolute', width: 140, height: 140, borderRadius: 70, 
    borderWidth: 1, borderColor: 'rgba(0, 240, 255, 0.08)', borderStyle: 'dashed'
  },
  sonarGridRing3: {
    position: 'absolute', width: 80, height: 80, borderRadius: 40, 
    borderWidth: 1, borderColor: 'rgba(0, 240, 255, 0.15)', borderStyle: 'dashed'
  },
  sonarGridCrosshairX: {
    position: 'absolute', width: 230, height: 1, backgroundColor: 'rgba(0, 240, 255, 0.15)'
  },
  sonarGridCrosshairY: {
    position: 'absolute', height: 230, width: 1, backgroundColor: 'rgba(0, 240, 255, 0.15)'
  },
  ledDot: {
    position: 'absolute',
    width: 3,
    height: 3,
    borderRadius: 1.5,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 1,
    shadowRadius: 10,
    elevation: 5,
  },
  sweep: {
    position: 'absolute',
    width: 200,
    height: 200,
    borderRadius: 100,
    overflow: 'hidden',
  },
  sweepGlow: {
    position: 'absolute',
    top: 100,
    left: 100,
    width: 100,
    height: 100,
    borderTopRightRadius: 100,
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
    width: 60,
    height: 60,
    borderRadius: 30,
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
  inlineStatus: {
    position: 'absolute',
    top: 125,
    left: 0,
    right: 0,
    alignItems: 'center',
    zIndex: 20,
  },
});
