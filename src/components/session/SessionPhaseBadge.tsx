import React, { useEffect, useRef } from 'react';
import { StyleSheet, Text, View, Animated, ActivityIndicator } from 'react-native';
import type { SessionPhase } from '../../services/session/SessionMachine.types';

export interface SessionPhaseBadgeProps {
  sessionPhase: SessionPhase;
}

export function SessionPhaseBadge({ sessionPhase }: SessionPhaseBadgeProps) {
  const pulseAnim = useRef(new Animated.Value(0.4)).current;

  useEffect(() => {
    if (sessionPhase !== 'ACTIVE') return;

    const pulse = Animated.loop(
      Animated.sequence([
        Animated.timing(pulseAnim, {
          toValue: 1.0,
          duration: 1000,
          useNativeDriver: true,
        }),
        Animated.timing(pulseAnim, {
          toValue: 0.4,
          duration: 1000,
          useNativeDriver: true,
        }),
      ])
    );

    pulse.start();

    return () => {
      pulse.stop();
      pulseAnim.setValue(0.4);
    };
  }, [sessionPhase, pulseAnim]);

  if (sessionPhase === 'IDLE') return null;

  return (
    <View style={styles.container}>
      {sessionPhase === 'ACTIVE' && (
        <View style={styles.badge}>
          <Animated.View style={[styles.dot, styles.recordingDot, { opacity: pulseAnim }]} />
          <Text style={[styles.text, styles.recordingText]}>RECORDING</Text>
        </View>
      )}
      {sessionPhase === 'PAUSED' && (
        <View style={styles.badge}>
          <View style={[styles.dot, styles.pausedDot]} />
          <Text style={[styles.text, styles.pausedText]}>PAUSED</Text>
        </View>
      )}
      {sessionPhase === 'ENDING' && (
        <View style={styles.badge}>
          <ActivityIndicator size="small" color="#FFFFFF" style={styles.loader} />
          <Text style={[styles.text, styles.savingText]}>SAVING...</Text>
        </View>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    alignSelf: 'flex-start',
    borderRadius: 12,
    overflow: 'hidden',
  },
  badge: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 12,
    backgroundColor: 'rgba(0, 0, 0, 0.4)',
  },
  dot: {
    width: 8,
    height: 8,
    borderRadius: 4,
    marginRight: 6,
  },
  recordingDot: {
    backgroundColor: '#F79320', // orange
  },
  pausedDot: {
    backgroundColor: '#FFD700', // yellow
  },
  text: {
    fontSize: 11,
    fontWeight: '700',
    letterSpacing: 0.5,
  },
  recordingText: {
    color: '#F79320',
  },
  pausedText: {
    color: '#FFD700',
  },
  savingText: {
    color: 'rgba(255, 255, 255, 0.7)',
  },
  loader: {
    marginRight: 6,
    transform: [{ scale: 0.8 }],
  },
});
