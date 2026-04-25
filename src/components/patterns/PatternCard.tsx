import React, { useEffect, useRef } from 'react';
import { Animated, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { SK8LYTZ_TEMPLATES } from '../../protocols/PatternEngine';
import { Spacing } from '../../theme/theme';
import { LEDStripPreview } from '../LEDStripPreview';

interface PatternCardProps {
  effect: typeof SK8LYTZ_TEMPLATES[0];
  isSelected: boolean;
  fgColor: string;
  bgColor: string;
  speed: number;
  brightness: number;
  direction: number;
  points: number;
  onSelect: () => void;
  Colors: any;
  /** Controls whether the LEDStripPreview animation is running (visibility gate) */
  autoPlay?: boolean;
}

export const PatternCard: React.FC<PatternCardProps> = React.memo(({
  effect, isSelected, fgColor, bgColor, speed, brightness, direction, points, onSelect, Colors,
  autoPlay = true,
}) => {
  const pulseAnim = useRef(new Animated.Value(1)).current;

  useEffect(() => {
    if (isSelected) {
      Animated.loop(
        Animated.sequence([
          Animated.timing(pulseAnim, { toValue: 1.04, duration: 600, useNativeDriver: false }),
          Animated.timing(pulseAnim, { toValue: 1, duration: 600, useNativeDriver: false }),
        ])
      ).start();
    } else {
      pulseAnim.stopAnimation();
      pulseAnim.setValue(1);
    }
  }, [isSelected, pulseAnim]);

  return (
    <Animated.View style={{ transform: [{ scale: pulseAnim }], flex: 1 }}>
      <TouchableOpacity
        id={`fx-card-${effect.id}`}
        activeOpacity={0.75}
        onPress={onSelect}
        style={[
          styles.effectCard,
          isSelected && {
            borderColor: '#00F0FF',
            shadowColor: '#00F0FF',
            shadowOpacity: 0.6,
            shadowRadius: 12,
            elevation: 10,
          }
        ]}
      >
        <LinearGradient
          colors={isSelected ? ['rgba(0,240,255,0.15)', 'rgba(0,240,255,0.05)'] : ['rgba(255,255,255,0.06)', 'rgba(255,255,255,0.02)']}
          start={{ x: 0, y: 0 }}
          end={{ x: 1, y: 1 }}
          style={StyleSheet.absoluteFillObject}
        />
        
        {/* Glassmorphism Refraction */}
        <View style={styles.cardRefraction} />

        {/* Header row: ID badge + FG/BG dots */}
        <View style={styles.cardHeader}>
          <View style={[styles.idBadge, isSelected && { backgroundColor: '#00F0FF' }]}>
            <Text style={[styles.idText, isSelected && { color: '#000' }]}>{effect.id}</Text>
          </View>
          <View style={{ flexDirection: 'row', gap: Spacing.xxs }}>
            {effect.requiresForeground && (
              <View style={[styles.capDot, { backgroundColor: fgColor, borderColor: 'rgba(255,255,255,0.3)' }]} />
            )}
            {effect.requiresBackground && (
              <View style={[styles.capDot, { backgroundColor: bgColor, borderColor: 'rgba(255,255,255,0.3)' }]} />
            )}
          </View>
        </View>

        {/* Effect name */}
        <Text
          style={[styles.effectName, isSelected && { color: '#00F0FF' }]}
          numberOfLines={2}
          allowFontScaling={false}
        >
          {effect.name}
        </Text>

        {/* Live LED strip preview using PatternEngine */}
        <View style={styles.stripWrapper}>
          <LEDStripPreview
            patternId={effect.id}
            fg={effect.requiresForeground ? fgColor : '#FF4400'}
            bg={effect.requiresBackground ? bgColor : '#000000'}
            numLEDs={points}
            speed={speed}
            brightness={brightness}
            direction={effect.supportsDirection ? (direction as 0 | 1) : 1}
            autoPlay={autoPlay}  // ← visibility-gated from PatternPickerTab scroll tracker
            height={9}
          />
        </View>
      </TouchableOpacity>
    </Animated.View>
  );
});

const styles = StyleSheet.create({
  effectCard: {
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderRadius: 12,
    borderWidth: 1.5,
    borderColor: 'rgba(255,255,255,0.08)',
    padding: Spacing.sm,
    overflow: 'hidden',
  },
  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: Spacing.xs,
  },
  idBadge: {
    backgroundColor: 'rgba(255,255,255,0.1)',
    borderRadius: 6,
    paddingHorizontal: Spacing.xs,
    paddingVertical: 1,
    minWidth: 22,
    alignItems: 'center',
  },
  idText: {
    color: 'rgba(255,255,255,0.5)',
    fontSize: 9,
    fontWeight: '900',
  },
  capDot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    borderWidth: 1,
  },
  effectName: {
    color: 'rgba(255,255,255,0.85)',
    fontSize: 11,
    fontWeight: '800',
    lineHeight: 14,
    marginBottom: Spacing.xs,
    minHeight: 28,
    letterSpacing: 0.5,
  },
  stripWrapper: {
    height: 9,
    borderRadius: 5,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.05)',
  },
  cardRefraction: {
    position: 'absolute',
    top: -30,
    left: -30,
    width: 100,
    height: 100,
    backgroundColor: 'rgba(255, 255, 255, 0.04)',
    transform: [{ rotate: '45deg' }],
  },
});

