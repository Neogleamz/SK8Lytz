import React, { useRef, useState, useEffect } from 'react';
import { View, Text, StyleSheet, PanResponder, Animated, Dimensions, TouchableOpacity } from 'react-native';
import { Colors } from '../theme/theme';

const { width: SCREEN_WIDTH } = Dimensions.get('window');
const WHEEL_RADIUS = SCREEN_WIDTH * 0.9;
const ITEM_SPACING_ANGLE = 6.5; 
const VISIBLE_ITEMS = 12;

interface ArcPatternWheelProps {
  value: number;
  onValueChange: (val: number) => void;
  min?: number;
  max?: number;
  itemLabel?: (item: number) => string;
}

export default function ArcPatternWheel({ 
  value, 
  onValueChange, 
  min = 1, 
  max = 100,
  itemLabel 
}: ArcPatternWheelProps) {
  const rotation = useRef(new Animated.Value(0)).current;
  const pulseAnim = useRef(new Animated.Value(1)).current;
  const [activeValue, setActiveValue] = useState(value);
  
  useEffect(() => {
    const targetRotation = -(value - 1) * ITEM_SPACING_ANGLE;
    Animated.spring(rotation, {
      toValue: targetRotation,
      useNativeDriver: true,
      friction: 10,
      tension: 30,
    }).start();
    setActiveValue(value);
  }, [value]);

  useEffect(() => {
    Animated.loop(
      Animated.sequence([
        Animated.timing(pulseAnim, { toValue: 1.2, duration: 1000, useNativeDriver: true }),
        Animated.timing(pulseAnim, { toValue: 1.0, duration: 1000, useNativeDriver: true }),
      ])
    ).start();
  }, []);

  const panResponder = useRef(
    PanResponder.create({
      onStartShouldSetPanResponder: () => true,
      onMoveShouldSetPanResponder: () => true,
      onPanResponderMove: (_, gestureState) => {
        const sensitivity = 0.45;
        const deltaRotation = gestureState.dx * sensitivity;
        const baseRotation = -(value - 1) * ITEM_SPACING_ANGLE;
        rotation.setValue(baseRotation + deltaRotation);
        
        const currentRot = baseRotation + deltaRotation;
        const offset = Math.round(-currentRot / ITEM_SPACING_ANGLE);
        const newValue = Math.max(min, Math.min(max, min + offset));
        if (newValue !== activeValue) {
          setActiveValue(newValue);
        }
      },
      onPanResponderRelease: (_, gestureState) => {
        const sensitivity = 0.45;
        const deltaRotation = gestureState.dx * sensitivity;
        const baseRotation = -(value - 1) * ITEM_SPACING_ANGLE;
        const finalRot = baseRotation + deltaRotation;
        
        const offset = Math.round(-finalRot / ITEM_SPACING_ANGLE);
        const newValue = Math.max(min, Math.min(max, min + offset));
        
        onValueChange(newValue);
      },
    })
  ).current;

  const renderItems = () => {
    const items = [];
    const start = Math.max(min, activeValue - VISIBLE_ITEMS);
    const end = Math.min(max, activeValue + VISIBLE_ITEMS);
    
    for (let i = start; i <= end; i++) {
      const itemAngle = (i - 1) * ITEM_SPACING_ANGLE;
      const isSelected = i === activeValue;
      
      const opacity = rotation.interpolate({
        inputRange: [-itemAngle - 30, -itemAngle, -itemAngle + 30],
        outputRange: [0.15, 1, 0.15],
        extrapolate: 'clamp',
      });

      const scale = rotation.interpolate({
        inputRange: [-itemAngle - 8, -itemAngle, -itemAngle + 8],
        outputRange: [0.75, 1.4, 0.75],
        extrapolate: 'clamp',
      });

      items.push(
        <Animated.View
          key={i}
          style={[
            styles.itemContainer,
            {
              opacity,
              transform: [
                { rotate: rotation.interpolate({
                    inputRange: [-360, 360],
                    outputRange: ['-360deg', '360deg']
                  }) 
                },
                { translateY: -WHEEL_RADIUS },
                { rotate: `${itemAngle}deg` },
                { translateY: WHEEL_RADIUS },
                { scale }
              ],
            },
          ]}
        >
          <Text style={[styles.itemText, isSelected && styles.selectedItemText]}>
            {i}
          </Text>
        </Animated.View>
      );
    }
    return items;
  };

  return (
    <View style={styles.container}>
      {/* Decorative Glow Arc Line */}
      <View style={styles.glowArcLine} />

      <View style={styles.header}>
        <Animated.Text style={[styles.autoText, { opacity: pulseAnim.interpolate({ inputRange: [1, 1.2], outputRange: [0.6, 1] }) }]}>AUTO</Animated.Text>
        <Animated.View style={{ transform: [{ scale: pulseAnim }] }}>
          <Text style={styles.heartIcon}>❤️</Text>
        </Animated.View>
      </View>
      
      <View style={styles.wheelWrapper} {...panResponder.panHandlers}>
        {renderItems()}
      </View>
      
      {itemLabel && (
        <View style={styles.labelContainer}>
          <Text style={styles.labelText}>{itemLabel(activeValue)}</Text>
        </View>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    height: 180,
    width: '100%',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'transparent',
    overflow: 'visible',
  },
  glowArcLine: {
    position: 'absolute',
    top: -WHEEL_RADIUS + 70, 
    width: WHEEL_RADIUS * 2,
    height: WHEEL_RADIUS * 2,
    borderRadius: WHEEL_RADIUS,
    borderWidth: 1,
    borderColor: 'rgba(255, 110, 0, 0.3)',
    backgroundColor: 'transparent',
  },
  header: {
    position: 'absolute',
    top: 5,
    alignItems: 'center',
    zIndex: 10,
  },
  autoText: {
    color: '#FFF',
    fontSize: 16,
    fontWeight: 'bold',
    letterSpacing: 3,
  },
  heartIcon: {
    fontSize: 22,
    marginTop: 4,
    textShadowColor: 'rgba(255,0,0,0.5)',
    textShadowRadius: 10,
  },
  wheelWrapper: {
    width: SCREEN_WIDTH,
    height: WHEEL_RADIUS * 2,
    alignItems: 'center',
    justifyContent: 'center',
    marginTop: -80,
  },
  itemContainer: {
    position: 'absolute',
    width: 60,
    height: 40,
    justifyContent: 'center',
    alignItems: 'center',
  },
  itemText: {
    color: 'rgba(255,255,255,0.4)',
    fontSize: 16,
    fontWeight: '600',
  },
  selectedItemText: {
    color: Colors.primary,
    fontSize: 26,
    fontWeight: 'bold',
    textShadowColor: Colors.primary,
    textShadowRadius: 10,
  },
  labelContainer: {
    position: 'absolute',
    bottom: -10,
    backgroundColor: 'rgba(255, 110, 0, 0.1)',
    paddingHorizontal: 20,
    paddingVertical: 8,
    borderRadius: 25,
    borderWidth: 1,
    borderColor: 'rgba(255, 110, 0, 0.2)',
    zIndex: 20,
  },
  labelText: {
    color: '#FFF',
    fontSize: 14,
    fontWeight: 'bold',
    textTransform: 'uppercase',
  }
});
