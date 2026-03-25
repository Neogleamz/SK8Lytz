import React, { useRef, useEffect, useState } from 'react';
import { View, Text, StyleSheet, FlatList, NativeSyntheticEvent, NativeScrollEvent, TouchableOpacity, LayoutChangeEvent, Platform } from 'react-native';
import { useTheme } from '../context/ThemeContext';
import { MaterialCommunityIcons } from '@expo/vector-icons';

export default function ArcPatternWheel({ 
  value, 
  onValueChange, 
  min = 1, 
  max = 100,
  itemLabel 
}: {
  value: number;
  onValueChange: (val: number) => void;
  min?: number;
  max?: number;
  itemLabel?: (item: number) => string;
}) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const flatListRef = useRef<FlatList>(null);
  const holdTimerRef = useRef<any>(null);
  const skipIntervalRef = useRef<any>(null);
  
  const [containerWidth, setContainerWidth] = useState(0);
  const itemWidth = containerWidth > 0 ? (containerWidth / 5.0) : 70;

  // Generate data with padding for 5-visible-item wheel effect
  const data = [
    { type: 'pad', id: 'pad-1' },
    { type: 'pad', id: 'pad-2' },
    ...Array.from({ length: max - min + 1 }, (_, i) => ({ type: 'item', id: String(min + i), val: min + i })),
    { type: 'pad', id: 'pad-3' },
    { type: 'pad', id: 'pad-4' },
  ];

  const scrollToValue = (val: number, animated = true) => {
    if (containerWidth === 0) return;
    const idx = val - min;
    // index + 2 because of the two pads at the start
    // We want the item at idx+2 to be centered. 
    // In a 5-item visible row, index i is centered if its start is at (i-2) * itemWidth
    // So for val=min (idx=0), we want index 2 to be centered, which means offset 0.
    flatListRef.current?.scrollToOffset({ 
      offset: idx * itemWidth, 
      animated 
    });
  };

  useEffect(() => {
    if (containerWidth > 0) {
      const timer = setTimeout(() => {
        scrollToValue(value, false);
      }, 100);
      return () => clearTimeout(timer);
    }
  }, [containerWidth]);

  const onScroll = (e: NativeSyntheticEvent<NativeScrollEvent>) => {
    if (containerWidth === 0) return;
    const offsetX = e.nativeEvent.contentOffset.x;
    const index = Math.round(offsetX / itemWidth);
    const item = data[index + 2] as any; // The item that should be in center
    if (item && item.type === 'item') {
      if (item.val !== value) {
        onValueChange(item.val);
      }
    }
  };

  const handleJump = (delta: number) => {
    let newVal = value + delta;
    
    // Circular logic
    if (newVal < min) newVal = max;
    else if (newVal > max) newVal = min;

    if (newVal !== value) {
      onValueChange(newVal);
      scrollToValue(newVal, true);
    }
  };

  const startHold = (delta: number) => {
    handleJump(delta);
    holdTimerRef.current = setTimeout(() => {
      skipIntervalRef.current = setInterval(() => {
        // Fast skip by 1 or 5 depending on range? 
        // For 1-100, 5 is good.
        handleJump(delta > 0 ? 5 : -5);
      }, 100); 
    }, 500); // reduced from 3000ms to 500ms
  };

  const stopHold = () => {
    if (holdTimerRef.current) clearTimeout(holdTimerRef.current);
    if (skipIntervalRef.current) clearInterval(skipIntervalRef.current);
  };

  const onLayout = (event: LayoutChangeEvent) => {
    setContainerWidth(event.nativeEvent.layout.width);
  };

  return (
    <View style={styles.container} onLayout={onLayout}>
      <View style={styles.wheelWrapper}>
        <FlatList
          ref={flatListRef}
          data={data}
          keyExtractor={(item) => item.id}
          horizontal
          showsHorizontalScrollIndicator={false}
          snapToInterval={itemWidth}
          snapToAlignment="start"
          decelerationRate="fast"
          scrollEventThrottle={16}
          onScroll={onScroll}
          onMomentumScrollEnd={onScroll}
          getItemLayout={(_, index) => ({ length: itemWidth, offset: itemWidth * index, index })}
          renderItem={({ item }) => {
            if (item.type === 'pad') {
              return <View style={{ width: itemWidth, height: 80 }} />;
            }
            const itemVal = (item as any).val;
            const isSelected = itemVal === value;
            return (
              <TouchableOpacity 
                activeOpacity={0.9} 
                onPress={() => {
                  onValueChange(itemVal);
                  scrollToValue(itemVal, true);
                }}
                style={[styles.itemContainer, { width: itemWidth }]}
              >
                <Text style={[styles.itemText, isSelected && styles.selectedItemText]}>
                  {itemVal}
                </Text>
              </TouchableOpacity>
            );
          }}
        />

        {/* Center Indicator Box */}
        <View style={[styles.centerIndicator, { width: itemWidth, left: itemWidth * 2 }]} pointerEvents="none" />
      </View>
      
      {/* Name and Navigation Controls */}
      <View style={styles.controlsRow}>
        <TouchableOpacity 
          onPress={() => handleJump(-1)}
          onPressIn={() => startHold(-1)}
          onPressOut={stopHold}
          style={styles.navBtn}
          activeOpacity={0.6}
        >
          <MaterialCommunityIcons name="chevron-left" size={28} color={Colors.primary} />
        </TouchableOpacity>

        {itemLabel && (
          <View style={styles.labelWrapper}>
            <Text style={styles.labelText} numberOfLines={1}>{itemLabel(value)}</Text>
          </View>
        )}

        <TouchableOpacity 
          onPress={() => handleJump(1)}
          onPressIn={() => startHold(1)}
          onPressOut={stopHold}
          style={styles.navBtn}
          activeOpacity={0.6}
        >
          <MaterialCommunityIcons name="chevron-right" size={28} color={Colors.primary} />
        </TouchableOpacity>
      </View>
    </View>
  );
}

const createStyles = (Colors: import('../theme/theme').ThemePalette) => StyleSheet.create({
  container: {
    height: 140,
    width: '100%',
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: Colors.isDark ? '#050505' : Colors.surfaceHighlight,
    paddingVertical: 5,
  },
  wheelWrapper: {
    width: '100%',
    height: 80,
    justifyContent: 'center',
    alignItems: 'center',
  },
  centerIndicator: {
    position: 'absolute',
    width: 60,
    height: 50,
    borderWidth: 1,
    borderColor: 'rgba(255, 110, 0, 0.4)',
    borderRadius: 8,
    backgroundColor: 'rgba(255, 110, 0, 0.05)',
    zIndex: -1,
  },
  itemContainer: {
    height: 80,
    justifyContent: 'center',
    alignItems: 'center',
  },
  itemText: {
    color: Colors.isDark ? 'rgba(255,255,255,0.15)' : 'rgba(0,0,0,0.15)',
    fontSize: 20,
    fontWeight: '700',
  },
  selectedItemText: {
    color: Colors.primary,
    fontSize: 38,
    fontWeight: 'bold',
    textShadowColor: 'rgba(255, 110, 0, 0.6)',
    textShadowRadius: 10,
  },
  controlsRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 5,
    width: '100%',
    justifyContent: 'center',
    gap: 10,
  },
  navBtn: {
    width: 36,
    height: 36,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(255, 110, 0, 0.05)',
    borderRadius: 18,
    zIndex: 10,
  },
  labelWrapper: {
    minWidth: 140,
    alignItems: 'center',
    paddingVertical: 6,
    borderBottomWidth: 1,
    borderBottomColor: 'rgba(255, 110, 0, 0.2)',
  },
  labelText: {
    color: Colors.text,
    fontSize: 14,
    fontWeight: '700',
    textTransform: 'uppercase',
    letterSpacing: 1.5,
    textAlign: 'center',
  }
});
