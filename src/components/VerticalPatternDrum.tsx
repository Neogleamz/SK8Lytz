import React, { useRef, useState, useEffect } from 'react';
import { View, Text, StyleSheet, NativeSyntheticEvent, NativeScrollEvent, TouchableOpacity, FlatList } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { useTheme } from '../context/ThemeContext';

const ITEM_HEIGHT = 44;
const VISIBLE_ITEMS = 5;

export default function VerticalPatternDrum({
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
}) {
  const { Colors } = useTheme();
  const styles = createStyles(Colors);
  const flatListRef = useRef<FlatList>(null);
  
  const [localVal, setLocalVal] = useState(value);
  const commitTimeoutRef = useRef<any>(null);

  useEffect(() => {
    setLocalVal(value);
  }, [value]);

  const commitValue = (val: number) => {
    setLocalVal(val);
    if (val !== value) {
       if (commitTimeoutRef.current) clearTimeout(commitTimeoutRef.current);
       commitTimeoutRef.current = setTimeout(() => {
          onValueChange(val);
       }, 50); // Fast 50ms debounce
    }
  };

  const data = React.useMemo(() => {
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
    const patternCount = max - min + 1;
    let targetRep = 10; // Middle rep
    const targetIdx = (targetRep * patternCount) + (val - min);
    
    flatListRef.current?.scrollToOffset({ 
      offset: targetIdx * ITEM_HEIGHT, 
      animated 
    });
  };

  useEffect(() => {
    const timer = setTimeout(() => {
      scrollToValue(value, false);
    }, 100);
    return () => clearTimeout(timer);
  }, []);

  const onScroll = (e: NativeSyntheticEvent<NativeScrollEvent>) => {
    const offsetY = e.nativeEvent.contentOffset.y;
    const index = Math.round(offsetY / ITEM_HEIGHT);
    const item = data[index + 2] as any; 
    
    if (item && item.type === 'item') {
      if (item.val !== localVal) {
        commitValue(item.val);
      }
    }
  };

  return (
    <View style={styles.containerWrap}>
      <View style={styles.container}>
        <View style={styles.drumMask}>
             {/* Reticle / Center indicator */}
             <View style={styles.reticleOverlay} pointerEvents="none">
               <View style={styles.reticleGlassBox} />
             </View>
   
             <FlatList
               ref={flatListRef}
               data={data}
               keyExtractor={(item) => item.id}
               showsVerticalScrollIndicator={false}
               snapToInterval={ITEM_HEIGHT}
               snapToAlignment="start"
               decelerationRate="fast"
               onScroll={onScroll}
               scrollEventThrottle={16}
               getItemLayout={(_, index) => ({ length: ITEM_HEIGHT, offset: ITEM_HEIGHT * index, index })}
               initialNumToRender={15}
               maxToRenderPerBatch={20}
               windowSize={5}
               renderItem={({ item }) => {
                 if (item.type === 'pad') return <View style={{ height: ITEM_HEIGHT, width: '100%' }} />;
                 const itemVal = (item as any).val;
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
                         {itemVal.toString().padStart(2, '0')}. {itemLabel ? itemLabel(itemVal) : `Pattern ${itemVal}`}
                       </Text>
                     </View>
                   </TouchableOpacity>
                 );
               }}
             />
             
             {/* Shadow gradients for 3D fading effect */}
             <View style={[styles.gradientMask, styles.gradientTop]} pointerEvents="none" />
             <View style={[styles.gradientMask, styles.gradientBottom]} pointerEvents="none" />
           </View>
         </View>
       </View>
     );
   }
   
   const createStyles = (Colors: any) => StyleSheet.create({
     containerWrap: {
       shadowColor: '#00D4FF',
       shadowOpacity: 0.15,
       shadowRadius: 24,
       shadowOffset: { width: 0, height: 0 },
       elevation: 12,
     },
     container: {
       height: ITEM_HEIGHT * VISIBLE_ITEMS,
       width: '80%',   // Narrower profile matching the mock-up 7-slot drum
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
       paddingHorizontal: 16,
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
       top: ITEM_HEIGHT * 3,
       height: ITEM_HEIGHT,
       width: '100%',
       zIndex: 0,
       justifyContent: 'center',
       alignItems: 'center',
       paddingHorizontal: 12,
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
