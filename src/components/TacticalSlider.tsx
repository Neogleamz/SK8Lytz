import React, { useRef, useState } from 'react';
import { View, StyleSheet, PanResponder, LayoutChangeEvent, Text } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../context/ThemeContext';

interface TacticalSliderProps {
  value: number;
  onValueChange: (val: number) => void;
  onSlidingComplete?: (val: number) => void;
  minimumValue?: number;
  maximumValue?: number;
  style?: any;
  iconName: any;
  label?: string;
  fillColor?: string;
  formatValue?: (val: number) => string;
}

const TacticalSlider = ({ 
  value, 
  onValueChange, 
  onSlidingComplete, 
  minimumValue = 0, 
  maximumValue = 100, 
  style, 
  iconName,
  label,
  fillColor,
  formatValue 
}: TacticalSliderProps) => {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const [_containerWidth, setContainerWidth] = useState(0);
  const containerWidthRef = useRef(0);
  
  const [localValue, setLocalValue] = useState(value);
  const isDraggingRef = useRef(false);

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
      onPanResponderGrant: (evt, gestureState) => {
        isDraggingRef.current = true;
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
        if (cl !== valueRef.current) {
            setLocalValue(cl);
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
  const activeColor = fillColor || Colors.primary;
  
  const displayValue = formatValue ? formatValue(localValue) : `${Math.round(percentage * 100)}%`;

  return (
    <View 
      style={[styles.container, style, { touchAction: 'none', userSelect: 'none' } as any]} 
      onLayout={(e: LayoutChangeEvent) => {
        setContainerWidth(e.nativeEvent.layout.width);
        containerWidthRef.current = e.nativeEvent.layout.width;
      }}
      {...panResponder.panHandlers}
    >
      {/* Background Track */}
      <View style={styles.trackBackground} pointerEvents="none" />
      
      {/* Fill Block */}
      <View style={[styles.fill, { width: `${percentage * 100}%`, backgroundColor: activeColor }]} pointerEvents="none" />
      
      {/* Overlay Content */}
      <View style={styles.overlay} pointerEvents="none">
         {/* Left Anchored Large Icon */}
         <MaterialCommunityIcons name={iconName} size={32} color="rgba(255,255,255,0.5)" style={{ position: 'absolute', left: 8, top: 6 }} />

         {/* Centered Label */}
         <View style={[StyleSheet.absoluteFill, { alignItems: 'center', justifyContent: 'center' }]}>
            {label && <Text style={styles.labelText}>{label}</Text>}
         </View>
         
         {/* Value locked to right */}
         <View style={{ flex: 1 }} />
         <Text style={[styles.valueText, { zIndex: 10 }]}>{displayValue}</Text>
      </View>
    </View>
  );
}

const createStyles = (Colors: import('../theme/theme').ThemePalette) => StyleSheet.create({
  container: {
    height: 44,
    justifyContent: 'center',
    cursor: 'pointer',
    borderRadius: 8,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
  },
  trackBackground: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: 'rgba(0,0,0,0.4)',
  },
  fill: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: Colors.primary,
    opacity: 0.8,
  },
  overlay: {
    ...StyleSheet.absoluteFillObject,
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingHorizontal: 12,
  },
  leftLabel: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  labelText: {
    color: '#FFF',
    fontSize: 9,
    fontWeight: '900',
    letterSpacing: 1,
    textTransform: 'uppercase',
    textShadowColor: 'rgba(0,0,0,0.8)',
    textShadowOffset: { width: 0, height: 1 },
    textShadowRadius: 3,
  },
  valueText: {
    color: '#FFF',
    fontSize: 11,
    fontWeight: 'bold',
    textShadowColor: 'rgba(0,0,0,0.8)',
    textShadowOffset: { width: 0, height: 1 },
    textShadowRadius: 3,
  }
});

export default React.memo(TacticalSlider);
