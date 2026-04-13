import { Spacing } from '../theme/theme';
import React, { useRef, useEffect, useState, useCallback } from 'react';
import { View, Text, StyleSheet, FlatList, NativeSyntheticEvent, NativeScrollEvent, TouchableOpacity, LayoutChangeEvent } from 'react-native';
import { useTheme } from '../context/ThemeContext';
import { MaterialCommunityIcons } from '@expo/vector-icons';

const REPEATS = 5;      // How many full cycles to pre-build (virtual infinite)
const VISIBLE_ITEMS = 5; // Items visible at once on wheel

export default function ArcPatternWheel({
  value,
  onValueChange,
  min = 1,
  max = 100,
  itemLabel,
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
  const commitTimeoutRef = useRef<any>(null);
  const _isScrollingRef = useRef(false);
  const _lastScrolledValueRef = useRef(value);

  const [containerWidth, setContainerWidth] = useState(0);
  const itemWidth = containerWidth > 0 ? containerWidth / VISIBLE_ITEMS : 70;

  const [localVal, setLocalVal] = useState(value);
  const localValRef = useRef(value);

  const patternCount = max - min + 1;

  // Build data once per [min, max]. Each item has a globally-unique key.
  const data = React.useMemo(() => {
    const items: { type: string; id: string; val: number; rep: number }[] = [];
    for (let r = 0; r < REPEATS; r++) {
      for (let v = min; v <= max; v++) {
        items.push({ type: 'item', id: `${r}-${v}`, val: v, rep: r });
      }
    }
    return items;
  }, [min, max]);

  // The middle repetition (rep=2) is where we start — equal buffer on both sides.
  const middleRep = Math.floor(REPEATS / 2);

  const getScrollOffset = useCallback(
    (val: number, rep: number) => ((rep * patternCount) + (val - min)) * itemWidth,
    [patternCount, min, itemWidth]
  );

  // Scroll to a value, staying in the same rep-block the user is currently in.
  const scrollToValue = useCallback(
    (val: number, animated = true) => {
      if (!flatListRef.current || containerWidth === 0) return;
      const _offset = flatListRef.current as any;
      // Read current scroll position quietly from the FlatList's internal state
      const currentOffset = (flatListRef.current as any)._listRef?._scrollMetrics?.offset ?? 0;
      const currentRep = Math.floor(currentOffset / (patternCount * itemWidth));
      const clampedRep = Math.max(0, Math.min(currentRep, REPEATS - 1));
      flatListRef.current.scrollToOffset({
        offset: getScrollOffset(val, clampedRep),
        animated,
      });
    },
    [containerWidth, patternCount, itemWidth, getScrollOffset]
  );

  // Scroll to initial position after layout is ready — only once.
  const hasInitialScrolled = useRef(false);
  useEffect(() => {
    if (containerWidth > 0 && !hasInitialScrolled.current) {
      hasInitialScrolled.current = true;
      // Use a short delay to ensure the FlatList is fully mounted
      setTimeout(() => {
        flatListRef.current?.scrollToOffset({
          offset: getScrollOffset(value, middleRep),
          animated: false,
        });
      }, 50);
    }
  }, [containerWidth]); // eslint-disable-line react-hooks/exhaustive-deps

  // Sync when external value changes (e.g. pattern selected elsewhere)
  useEffect(() => {
    if (value !== localValRef.current) {
      localValRef.current = value;
      setLocalVal(value);
      if (containerWidth > 0) {
        // Small delay so we don't fight an in-progress scroll
        setTimeout(() => scrollToValue(value, true), 80);
      }
    }
  }, [value, containerWidth, scrollToValue]);

  const commitValue = useCallback(
    (val: number) => {
      if (val === localValRef.current) return;
      localValRef.current = val;
      setLocalVal(val);
      if (commitTimeoutRef.current) clearTimeout(commitTimeoutRef.current);
      commitTimeoutRef.current = setTimeout(() => onValueChange(val), 120);
    },
    [onValueChange]
  );

  const onScroll = useCallback(
    (e: NativeSyntheticEvent<NativeScrollEvent>) => {
      if (containerWidth === 0) return;
      const offsetX = e.nativeEvent.contentOffset.x;
      // Which item index is in the center slot (slot 2 of 5)?
      const centerIndex = Math.round(offsetX / itemWidth) + Math.floor(VISIBLE_ITEMS / 2);
      const item = data[centerIndex] as any;
      if (item && item.type === 'item') {
        commitValue(item.val);
      }
    },
    [containerWidth, itemWidth, data, commitValue]
  );

  const handleJump = useCallback(
    (delta: number) => {
      let newVal = localValRef.current + delta;
      if (newVal < min) newVal = max;
      else if (newVal > max) newVal = min;
      commitValue(newVal);
      scrollToValue(newVal, true);
    },
    [min, max, commitValue, scrollToValue]
  );

  const startHold = useCallback(
    (delta: number) => {
      handleJump(delta);
      holdTimerRef.current = setTimeout(() => {
        skipIntervalRef.current = setInterval(() => {
          handleJump(delta > 0 ? 5 : -5);
        }, 100);
      }, 500);
    },
    [handleJump]
  );

  const stopHold = useCallback(() => {
    if (holdTimerRef.current) clearTimeout(holdTimerRef.current);
    if (skipIntervalRef.current) clearInterval(skipIntervalRef.current);
  }, []);

  const onLayout = useCallback((event: LayoutChangeEvent) => {
    setContainerWidth(event.nativeEvent.layout.width);
  }, []);

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
          initialNumToRender={VISIBLE_ITEMS + 2}
          maxToRenderPerBatch={10}
          windowSize={3}
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
        <View
          style={[styles.centerIndicator, { width: itemWidth, left: itemWidth * Math.floor(VISIBLE_ITEMS / 2) }]}
          pointerEvents="none"
        />
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
            <Text style={styles.labelText} numberOfLines={1}>{itemLabel(localVal)}</Text>
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
    paddingVertical: Spacing.xs,
  },
  wheelWrapper: {
    width: '100%',
    height: 80,
    justifyContent: 'center',
    alignItems: 'center',
  },
  centerIndicator: {
    position: 'absolute',
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
    marginTop: Spacing.xs,
    width: '100%',
    justifyContent: 'center',
    gap: Spacing.md,
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
    paddingVertical: Spacing.sm,
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
  },
});
