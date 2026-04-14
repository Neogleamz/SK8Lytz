import React, { useRef, useState } from 'react';
import { LayoutChangeEvent, PanResponder, StyleSheet, Text, View } from 'react-native';
import { Colors, Spacing, Typography } from '../theme/theme';

export default function CircularKnob({ value, onValueChange, min = 1, max = 100 }: any) {
  const [center, setCenter] = useState({ x: 0, y: 0 });

  const calculateValueFromAngle = (x: number, y: number) => {
    if (center.x === 0 && center.y === 0) return;
    const dx = x - center.x;
    const dy = y - center.y;
    let angle = Math.atan2(dy, dx) + Math.PI / 2; // offset so top is 0
    if (angle < 0) angle += 2 * Math.PI;
    
    // Convert angle [0, 2π] to value [min, max]
    const percent = Math.min(1, Math.max(0, angle / (2 * Math.PI)));
    let newVal = Math.round(min + percent * (max - min));
    if (newVal > max) newVal = max;
    if (newVal < min) newVal = min;
    onValueChange(newVal);
  };

  const panResponder = useRef(
    PanResponder.create({
      onStartShouldSetPanResponder: () => true,
      onMoveShouldSetPanResponder: () => true,
      onPanResponderGrant: (evt) => {
        calculateValueFromAngle(evt.nativeEvent.locationX, evt.nativeEvent.locationY);
      },
      onPanResponderMove: (evt) => {
        calculateValueFromAngle(evt.nativeEvent.locationX, evt.nativeEvent.locationY);
      },
    })
  ).current;

  // rotation in degrees for visual styling
  const percent = (value - min) / (max - min);
  const rotation = percent * 360;

  return (
    <View 
      style={styles.container} 
      onLayout={(e: LayoutChangeEvent) => {
        const { width, height } = e.nativeEvent.layout;
        setCenter({ x: width / 2, y: height / 2 });
      }}
      {...panResponder.panHandlers}
    >
      <View style={[styles.knobBase, { transform: [{ rotate: `${rotation}deg` }] }]}>
        <View style={styles.knobIndicator} />
      </View>
      <View style={styles.centerTextContainer} pointerEvents="none">
        <Text style={[Typography.title, { fontSize: 32 }]}>{value}</Text>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    width: 160,
    height: 160,
    alignSelf: 'center',
    justifyContent: 'center',
    alignItems: 'center',
    marginVertical: Spacing.lg,
    cursor: 'pointer', // Note: For web support
  },
  knobBase: {
    width: 150,
    height: 150,
    borderRadius: 75,
    backgroundColor: Colors.surfaceHighlight,
    borderWidth: 2,
    borderColor: 'rgba(255,255,255,0.1)',
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.5,
    shadowRadius: 10,
    elevation: 8,
  },
  knobIndicator: {
    position: 'absolute',
    top: 10,
    alignSelf: 'center',
    width: 12,
    height: 12,
    borderRadius: 6,
    backgroundColor: Colors.primary,
    shadowColor: Colors.primary,
    shadowOpacity: 1,
    shadowRadius: 5,
  },
  centerTextContainer: {
    position: 'absolute',
    alignItems: 'center',
    justifyContent: 'center',
  }
});
