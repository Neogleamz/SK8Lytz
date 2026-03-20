import React, { useRef, useState } from 'react';
import { View, StyleSheet, PanResponder, LayoutChangeEvent } from 'react-native';
import { Colors } from '../theme/theme';

interface CustomSliderProps {
  value: number;
  onValueChange: (val: number) => void;
  minimumValue?: number;
  maximumValue?: number;
  style?: any;
}

export default function CustomSlider({ value, onValueChange, minimumValue = 0, maximumValue = 100, style }: CustomSliderProps) {
  const [containerWidth, setContainerWidth] = useState(0);
  const valueRef = useRef(value);
  valueRef.current = value;
  
  const startValRef = useRef(value);

  const panResponder = useRef(
    PanResponder.create({
      onStartShouldSetPanResponder: () => true,
      onMoveShouldSetPanResponder: () => true,
      onPanResponderGrant: (evt, gestureState) => {
        // Record starting value
        startValRef.current = valueRef.current;
        // Optional: Jump to click location if it's a tap on track
        if (containerWidth > 0 && evt.nativeEvent.locationX) {
           const initialPercent = Math.max(0, Math.min(1, evt.nativeEvent.locationX / containerWidth));
           const initialVal = minimumValue + (initialPercent * (maximumValue - minimumValue));
           startValRef.current = initialVal;
           onValueChange(Math.round(initialVal));
        }
      },
      onPanResponderMove: (evt, gestureState) => {
        if (containerWidth === 0) return;
        const deltaPercent = gestureState.dx / containerWidth;
        const deltaVal = deltaPercent * (maximumValue - minimumValue);
        let newVal = startValRef.current + deltaVal;
        newVal = Math.max(minimumValue, Math.min(maximumValue, newVal));
        onValueChange(Math.round(newVal));
      },
    })
  ).current;

  const percentage = (value - minimumValue) / (maximumValue - minimumValue);

  return (
    <View 
      style={[styles.container, style, { touchAction: 'none', userSelect: 'none' } as any]} 
      onLayout={(e: LayoutChangeEvent) => setContainerWidth(e.nativeEvent.layout.width)}
      {...panResponder.panHandlers}
    >
      <View style={styles.track} pointerEvents="none">
        <View style={[styles.fill, { width: `${percentage * 100}%` }]} />
      </View>
      <View style={[styles.thumb, { left: `${percentage * 100}%`, transform: [{ translateX: -10 }] }]} pointerEvents="none" />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    height: 40,
    justifyContent: 'center',
    cursor: 'pointer',
  },
  track: {
    height: 6,
    borderRadius: 3,
    backgroundColor: 'rgba(255,255,255,0.1)',
    width: '100%',
    overflow: 'hidden',
  },
  fill: {
    height: '100%',
    backgroundColor: Colors.primary,
  },
  thumb: {
    position: 'absolute',
    width: 20,
    height: 20,
    borderRadius: 10,
    backgroundColor: Colors.text,
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.8,
    shadowRadius: 5,
    elevation: 5,
  }
});
