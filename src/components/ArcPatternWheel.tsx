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
  const curIndexRef = useRef<number>(-1);
  const [localVal, setLocalVal] = useState(value);

  useEffect(() => {
    setLocalVal(value);
  }, [value]);

  const commitTimeoutRef = useRef<any>(null);

  const commitValue = (val: number) => {
    setLocalVal(val);
    if (val !== value) {
       if (commitTimeoutRef.current) clearTimeout(commitTimeoutRef.current);
       commitTimeoutRef.current = setTimeout(() => {
          onValueChange(val);
       }, 150);
    }
  };

  // FAUX INFINITE LOOP: Repeat the sequence 20 times (e.g. 6,000 items)
  // React Native's virtualizer only renders ~10 items at a time, so this costs zero performance natively.
  const data = React.useMemo(() => {
    const patternCount = max - min + 1;
    const items = [];
    for (let r = 0; r < 20; r++) {
      for (let v = min; v <= max; v++) {
        items.push({ type: 'item', id: `${r}-${v}`, val: v, rep: r });
      }
    }
    return [
      { type: 'pad', id: 'pad-start-1' },
      { type: 'pad', id: 'pad-start-2' },
      ...items,
      { type: 'pad', id: 'pad-end-1' },
      { type: 'pad', id: 'pad-end-2' },
    ];
  }, [min, max]);

  const scrollToValue = (val: number, animated = true) => {
    if (containerWidth === 0) return;
    const patternCount = max - min + 1;
    
    // Jump straight to the absolute middle block (Block 10 of 20) on initial load to give thousands of items buffer on both sides.
    let targetRep = 10; 
    
    if (curIndexRef.current >= 0) {
        // If we are actively scrolling, we target the exact block repetition the user is currently looking at!
        targetRep = Math.floor(curIndexRef.current / patternCount);
    }
    
    const targetIdx = (targetRep * patternCount) + (val - min);
    
    flatListRef.current?.scrollToOffset({ 
      offset: targetIdx * itemWidth, 
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
    
    // Save physical list location so arrow buttons / skips snap to the nearest neighbor natively
    curIndexRef.current = index;

    const item = data[index + 2] as any; // The item perfectly centered
    if (item && item.type === 'item') {
      if (item.val !== localVal) {
        commitValue(item.val);
      }
    }
  };

  const handleJump = (delta: number) => {
    let newVal = localVal + delta;
    
    // Circular logic
    if (newVal < min) newVal = max;
    else if (newVal > max) newVal = min;

    if (newVal !== localVal) {
      commitValue(newVal);
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
          initialNumToRender={15}
          maxToRenderPerBatch={20}
          windowSize={5}
          getItemLayout={(_, index) => ({ length: itemWidth, offset: itemWidth * index, index })}
          renderItem={({ item }) => {
            if (item.type === 'pad') {
              return <View style={{ width: itemWidth, height: 80 }} />;
            }
            const itemVal = (item as any).val;
            const isSelected = itemVal === localVal;
            return (
              <TouchableOpacity 
                activeOpacity={0.9} 
                onPress={() => {
                  commitValue(itemVal);
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
