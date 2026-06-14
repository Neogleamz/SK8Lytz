import React, { useCallback, useEffect, useRef, useState } from 'react';
import { FlatList, NativeScrollEvent, NativeSyntheticEvent, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { useTheme } from '../context/ThemeContext';
import { Spacing , ThemePalette } from '../theme/theme';

const ITEM_HEIGHT = 44;
const getDrumItemLayout = (_data: unknown, index: number) => ({
  length: ITEM_HEIGHT,
  offset: ITEM_HEIGHT * index,
  index,
});

const VerticalPatternDrum = ({
  value,
  onValueChange,
  min = 1,
  max = 103,
  itemLabel
}: {
  value: number;
  onValueChange: (val: number) => void;
  min?: number;
  max?: number;
  itemLabel?: (item: number) => string;
}) => {
  const { Colors } = useTheme();
  const styles = React.useMemo(() => createStyles(Colors), [Colors]);
  const flatListRef = useRef<FlatList>(null);
  
  const [localVal, setLocalVal] = useState(value);
  const [layoutHeight, setLayoutHeight] = useState(200);
  const commitTimeoutRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const lastScrolledValueRef = useRef(value);

  const patternCount = max - min + 1;

  const scrollToValue = useCallback((val: number, animated = true) => {
    let targetRep = 2; // Middle of 5 reps
    const targetIdx = (targetRep * patternCount) + (val - min);
    
    flatListRef.current?.scrollToOffset({ 
      offset: targetIdx * ITEM_HEIGHT, 
      animated 
    });
  }, [patternCount, min]);

  useEffect(() => {
    return () => {
      if (commitTimeoutRef.current) clearTimeout(commitTimeoutRef.current);
    };
  }, []);

  useEffect(() => {
    setLocalVal(value);
    if (value !== lastScrolledValueRef.current) {
      lastScrolledValueRef.current = value;
      scrollToValue(value, true);
    }
  }, [value, scrollToValue]);

  const commitValue = useCallback((val: number) => {
    setLocalVal(val);
    lastScrolledValueRef.current = val;
    if (val !== value) {
       if (commitTimeoutRef.current) clearTimeout(commitTimeoutRef.current);
       commitTimeoutRef.current = setTimeout(() => {
          onValueChange(val);
       }, 50); // Fast 50ms debounce
    }
  }, [value, onValueChange]);


  const padCount = Math.max(1, Math.floor((layoutHeight / ITEM_HEIGHT) / 2));

  // Pure pattern data array. No layout dependencies ensure stable reference hooks.
  const items = React.useMemo(() => {
    const arr = [];
    for (let r = 0; r < 5; r++) {
      for (let v = min; v <= max; v++) {
        arr.push({ type: 'item', id: `${r}-${v}`, val: v, rep: r });
      }
    }
    return arr;
  }, [min, max]);

  // Frame 1 absolute jump vector ensuring Zero layout calculations
  const [initialTargetIdx] = useState((2 * patternCount) + (value - min));

  const keyExtractor = useCallback((item: { id: string }) => item.id, []);

  const renderItem = useCallback(({ item }: { item: { type: string; id: string; val: number; rep: number } }) => {
    const itemVal = item.val;
    const isSelected = itemVal === localVal;

    return (
      <TouchableOpacity
        activeOpacity={0.9}
        onPress={() => {
          commitValue(itemVal);
          scrollToValue(itemVal, true);
        }}
        style={[styles.itemContainer, { height: ITEM_HEIGHT }]}
      >
        <View style={[styles.itemTextWrapper, isSelected && styles.selectedItemWrapper]}>
          <Text style={[styles.itemLabel, isSelected && styles.selectedItemLabel]} numberOfLines={1}>
            {itemLabel ? itemLabel(itemVal) : `Pattern ${itemVal}`}
          </Text>
        </View>
      </TouchableOpacity>
    );
  }, [localVal, commitValue, scrollToValue, itemLabel, styles]);

  const onScroll = useCallback((e: NativeSyntheticEvent<NativeScrollEvent>) => {
    const offsetY = e.nativeEvent.contentOffset.y;
    const index = Math.max(0, Math.round(offsetY / ITEM_HEIGHT));
    const item = items[index];
    
    if (item && item.type === 'item') {
      if (item.val !== localVal) {
        commitValue(item.val);
      }
    }
  }, [items, localVal, commitValue]);

  return (
    <View 
      style={[styles.containerWrap, { flex: 1 }]}
      accessible={true}
      accessibilityRole="adjustable"
      accessibilityLabel={`Pattern drum. Currently selected pattern ${localVal}`}
      accessibilityHint="Scroll up or down to change the pattern"
    >
      <View style={styles.container} onLayout={(e) => setLayoutHeight(e.nativeEvent.layout.height)}>
        <View style={styles.drumMask}>
             {/* Reticle / Center indicator */}
             <View style={[styles.reticleOverlay, { top: padCount * ITEM_HEIGHT, marginTop: 0 }]} pointerEvents="none">
               <View style={styles.reticleGlassBox} />
             </View>
   
             <FlatList removeClippedSubviews={true}
               ref={flatListRef}
               data={items}
               keyExtractor={keyExtractor}
               showsVerticalScrollIndicator={false}
               snapToInterval={ITEM_HEIGHT}
               snapToAlignment="start"
               decelerationRate="fast"
               scrollEventThrottle={16}
               onScroll={onScroll}
               onMomentumScrollEnd={onScroll}
               initialScrollIndex={initialTargetIdx}
               initialNumToRender={10}
               maxToRenderPerBatch={15}
               windowSize={5}
               ListHeaderComponent={<View style={{ height: padCount * ITEM_HEIGHT }} />}
               ListFooterComponent={<View style={{ height: padCount * ITEM_HEIGHT }} />}
               getItemLayout={getDrumItemLayout}
               renderItem={renderItem}
             />
             
             {/* Shadow gradients for 3D fading effect */}
             <View style={[styles.gradientMask, styles.gradientTop]} pointerEvents="none" />
             <View style={[styles.gradientMask, styles.gradientBottom]} pointerEvents="none" />
           </View>
         </View>
       </View>
     );
   }
   
   const createStyles = (Colors: ThemePalette) => StyleSheet.create({
     containerWrap: {
       shadowColor: '#00D4FF',
       shadowOpacity: 0.15,
       shadowRadius: 24,
       shadowOffset: { width: 0, height: 0 },
       elevation: 12,
     },
     container: {
      flex: 1,
      width: '100%',
      alignItems: 'center',
      justifyContent: 'center',
      backgroundColor: Colors.isDark ? '#08080C' : Colors.surfaceHighlight,
      borderRadius: 24,
      overflow: 'hidden',
      borderWidth: 1.5,
      borderColor: 'rgba(0, 212, 255, 0.35)',
      alignSelf: 'center',
    },
     drumMask: {
       width: '100%',
       height: '100%',
       position: 'relative'
     },
     itemContainer: {
       width: '100%',
       justifyContent: 'center',
       alignItems: 'center',
       paddingHorizontal: Spacing.lg,
     },
     itemTextWrapper: {
       width: '100%',
       height: '80%',
       justifyContent: 'center',
       alignItems: 'center',
       borderRadius: 8,
       borderWidth: 1,
       borderColor: 'transparent',
     },
     selectedItemWrapper: {
       backgroundColor: 'rgba(255, 85, 0, 0.05)',
       borderColor: 'rgba(255, 85, 0, 0.6)',
       shadowColor: '#FF5500',
       shadowOpacity: 1,
       shadowRadius: 12,
       shadowOffset: { width: 0, height: 0 },
       elevation: 8,
     },
     itemLabel: {
       color: Colors.isDark ? 'rgba(255,255,255,0.2)' : 'rgba(0,0,0,0.3)',
       fontSize: 15,
       fontWeight: '700',
       textAlign: 'center',
     },
     selectedItemLabel: {
       color: '#FF5500', 
       fontSize: 18,
       fontWeight: '900',
       letterSpacing: 0.5,
       textShadowColor: '#FF5500',
       textShadowRadius: 10,
       textShadowOffset: { width: 0, height: 0 },
     },
     reticleOverlay: {
      position: 'absolute',
      top: '50%',
      marginTop: -(ITEM_HEIGHT / 2),
      height: ITEM_HEIGHT,
      width: '100%',
      zIndex: 0,
      justifyContent: 'center',
      alignItems: 'center',
      paddingHorizontal: Spacing.md,
    },
     reticleGlassBox: {
       width: '100%',
       height: '90%',
       borderRadius: 12,
       backgroundColor: 'rgba(0, 212, 255, 0.08)',
       justifyContent: 'center',
       alignItems: 'center',
       borderWidth: 1,
       borderColor: 'rgba(0, 212, 255, 0.3)',
       shadowColor: '#00D4FF',
       shadowOpacity: 0.4,
       shadowRadius: 8,
       shadowOffset: { width: 0, height: 0 },
       elevation: 4,
     },
     gradientMask: {
       position: 'absolute',
       width: '100%',
       height: ITEM_HEIGHT * 1.2,
       backgroundColor: Colors.isDark ? 'rgba(8,8,12,0.85)' : 'rgba(255,255,255,0.85)',
     },
     gradientTop: {
       top: 0,
     },
     gradientBottom: {
       bottom: 0,
     }
   });

export default React.memo(VerticalPatternDrum);
