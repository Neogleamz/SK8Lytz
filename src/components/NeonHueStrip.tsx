import React, { useRef, useState } from 'react';
import { View, StyleSheet, PanResponder, LayoutChangeEvent } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { useTheme } from '../context/ThemeContext';

interface NeonHueStripProps {
  value: number;
  onValueChange: (val: number) => void;
  onSlidingComplete?: (val: number) => void;
  minimumValue?: number;
  maximumValue?: number;
  style?: any;
}

const NeonHueStrip = ({ value, onValueChange, onSlidingComplete, minimumValue = 0, maximumValue = 360, style }: NeonHueStripProps) => {
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
      <View style={styles.trackWrap} pointerEvents="none">
        <LinearGradient 
          colors={['#FF0000', '#FFFF00', '#00FF00', '#00FFFF', '#0000FF', '#FF00FF', '#FF0000']} 
          start={{x: 0, y: 0}} end={{x: 1, y: 0}}
          style={StyleSheet.absoluteFill}
        />
      </View>
      <View style={[styles.thumb, { left: `${percentage * 100}%`, transform: [{ translateX: -3 }] }]} pointerEvents="none" />
    </View>
  );
}

const createStyles = (Colors: import('../theme/theme').ThemePalette) => StyleSheet.create({
  container: {
    height: 48,
    justifyContent: 'center',
    cursor: 'pointer',
  },
  trackWrap: {
    height: 24, // Thicker DJ style touch strip
    borderRadius: 8,
    width: '100%',
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
  },
  thumb: {
    position: 'absolute',
    width: 8,
    height: 38,
    borderRadius: 4,
    backgroundColor: '#FFFFFF',
    shadowColor: '#FFF',
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 1,
    shadowRadius: 8,
    elevation: 8,
    borderWidth: 1,
    borderColor: 'rgba(0,0,0,0.8)',
  }
});

export default React.memo(NeonHueStrip);
