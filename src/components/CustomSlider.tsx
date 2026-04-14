import { LinearGradient } from 'expo-linear-gradient';
import React, { useRef, useState } from 'react';
import { LayoutChangeEvent, PanResponder, StyleSheet, View } from 'react-native';
import { useTheme } from '../context/ThemeContext';

interface CustomSliderProps {
  value: number;
  onValueChange: (val: number) => void;
  onSlidingComplete?: (val: number) => void;
  minimumValue?: number;
  maximumValue?: number;
  style?: any;
  gradientTrack?: boolean;
  step?: number;
  thumbTintColor?: string;
}

const CustomSlider = ({ value, onValueChange, onSlidingComplete, minimumValue = 0, maximumValue = 100, style, gradientTrack = false, step, thumbTintColor }: CustomSliderProps) => {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const [_containerWidth, setContainerWidth] = useState(0);
  const containerWidthRef = useRef(0);
  
  const [localValue, setLocalValue] = useState(value);
  const isDraggingRef = useRef(false);

  // Sync with parent props only when NOT actively grabbed by user
  React.useEffect(() => {
     if (!isDraggingRef.current) setLocalValue(value);
  }, [value]);

  const valueRef = useRef(localValue);
  valueRef.current = localValue;
  
  const minMaxRef = useRef({ min: minimumValue, max: maximumValue });
  minMaxRef.current = { min: minimumValue, max: maximumValue };

  const onValueChangeRef = useRef(onValueChange);
  onValueChangeRef.current = onValueChange;

  const onSlidingCompleteRef = useRef(onSlidingComplete);
  onSlidingCompleteRef.current = onSlidingComplete;

  const startValRef = useRef(value);

  const panResponder = useRef(
    PanResponder.create({
      onStartShouldSetPanResponder: () => true,
      onStartShouldSetPanResponderCapture: () => true,
      onMoveShouldSetPanResponder: () => true,
      onMoveShouldSetPanResponderCapture: () => true,
      onPanResponderTerminationRequest: () => false,
      onPanResponderGrant: (evt, gestureState) => {
        isDraggingRef.current = true;
        // Record starting value
        startValRef.current = valueRef.current;
        if (containerWidthRef.current > 0 && evt.nativeEvent.locationX) {
           const initialPercent = Math.max(0, Math.min(1, evt.nativeEvent.locationX / containerWidthRef.current));
           const { min, max } = minMaxRef.current;
           const initialVal = min + (initialPercent * (max - min));
           startValRef.current = initialVal;
           const cl = Math.round(initialVal);
           setLocalValue(cl);
           onValueChangeRef.current(cl);
        }
      },
      onPanResponderMove: (evt, gestureState) => {
        if (containerWidthRef.current === 0) return;
        const { min, max } = minMaxRef.current;
        const deltaPercent = gestureState.dx / containerWidthRef.current;
        const deltaVal = deltaPercent * (max - min);
        let newVal = startValRef.current + deltaVal;
        newVal = Math.max(min, Math.min(max, newVal));
        const cl = Math.round(newVal);
        // Instant visual local update without waiting for parent
        if (cl !== valueRef.current) {
            setLocalValue(cl);
            // Optional: debounce or throttle this callback if needed, but since it's an event prop, DockedController can decide.
            onValueChangeRef.current(cl);
        }
      },
      onPanResponderRelease: () => {
        isDraggingRef.current = false;
        if (onSlidingCompleteRef.current) onSlidingCompleteRef.current(valueRef.current);
      },
      onPanResponderTerminate: () => {
        isDraggingRef.current = false;
        if (onSlidingCompleteRef.current) onSlidingCompleteRef.current(valueRef.current);
      },
    })
  ).current;

  const percentage = (localValue - minimumValue) / (maximumValue - minimumValue);

  return (
    <View 
      style={[styles.container, style, { touchAction: 'none', userSelect: 'none' } as any]} 
      onLayout={(e: LayoutChangeEvent) => {
        setContainerWidth(e.nativeEvent.layout.width);
        containerWidthRef.current = e.nativeEvent.layout.width;
      }}
      {...panResponder.panHandlers}
    >
      <View style={[styles.track, gradientTrack && { backgroundColor: 'transparent' }]} pointerEvents="none">
        {gradientTrack ? (
          <LinearGradient 
            colors={['#FF0000', '#FFFF00', '#00FF00', '#00FFFF', '#0000FF', '#FF00FF', '#FF0000']} 
            start={{x: 0, y: 0}} end={{x: 1, y: 0}}
            style={StyleSheet.absoluteFill}
          />
        ) : (
          <View style={[styles.fill, { width: `${percentage * 100}%` }]} />
        )}
      </View>
      <View style={[styles.thumb, { left: `${percentage * 100}%`, transform: [{ translateX: -10 }] }]} pointerEvents="none" />
    </View>
  );
}

const createStyles = (Colors: import('../theme/theme').ThemePalette) => StyleSheet.create({
  container: {
    height: 40,
    justifyContent: 'center',
    cursor: 'pointer',
  },
  track: {
    height: 6,
    borderRadius: 3,
    backgroundColor: Colors.isDark ? 'rgba(255,255,255,0.1)' : 'rgba(0,0,0,0.08)',
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
    backgroundColor: Colors.isDark ? Colors.text : Colors.accent,
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.8,
    shadowRadius: 5,
    elevation: 5,
  }
});

export default React.memo(CustomSlider);
