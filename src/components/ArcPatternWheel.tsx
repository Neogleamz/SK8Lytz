import React, { useRef, useEffect } from 'react';
import { View, Text, StyleSheet, FlatList, Dimensions, NativeSyntheticEvent, NativeScrollEvent } from 'react-native';
import { Colors } from '../theme/theme';

const { width: SCREEN_WIDTH } = Dimensions.get('window');
const ITEM_WIDTH = Math.floor(SCREEN_WIDTH / 5);

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
  const flatListRef = useRef<FlatList>(null);

  const data = [
    { type: 'pad', id: 'pad-1' },
    { type: 'pad', id: 'pad-2' },
    ...Array.from({ length: max - min + 1 }, (_, i) => ({ type: 'item', id: String(min + i), val: min + i })),
    { type: 'pad', id: 'pad-3' },
    { type: 'pad', id: 'pad-4' },
  ];

  useEffect(() => {
    setTimeout(() => {
       const idx = value - min;
       flatListRef.current?.scrollToOffset({ offset: idx * ITEM_WIDTH, animated: false });
    }, 100);
  }, []);

  const onScrollEnd = (e: NativeSyntheticEvent<NativeScrollEvent>) => {
    const offsetX = e.nativeEvent.contentOffset.x;
    const index = Math.round(offsetX / ITEM_WIDTH);
    const item = data[index + 2] as any;
    if (item && item.type === 'item') {
      if (item.val !== value) {
        onValueChange(item.val);
      }
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.autoText}>AUTO</Text>
        <Text style={styles.heartIcon}>❤️</Text>
      </View>
      
      <View style={styles.wheelWrapper}>
        <FlatList
          ref={flatListRef}
          data={data}
          keyExtractor={(item) => item.id}
          horizontal
          showsHorizontalScrollIndicator={false}
          snapToInterval={ITEM_WIDTH}
          decelerationRate="fast"
          onMomentumScrollEnd={onScrollEnd}
          onScrollEndDrag={(e) => {
            const velocity = e.nativeEvent.velocity?.x || 0;
            if (Math.abs(velocity) < 0.5) onScrollEnd(e);
          }}
          getItemLayout={(_, index) => ({ length: ITEM_WIDTH, offset: ITEM_WIDTH * index, index })}
          renderItem={({ item }) => {
            if (item.type === 'pad') {
              return <View style={{ width: ITEM_WIDTH, height: 60 }} />;
            }
            const itemVal = (item as any).val;
            const isSelected = itemVal === value;
            return (
              <View style={[styles.itemContainer, { width: ITEM_WIDTH }]}>
                <Text style={[styles.itemText, isSelected && styles.selectedItemText]}>
                  {itemVal}
                </Text>
              </View>
            );
          }}
        />
        <View style={styles.pointer} pointerEvents="none" />
      </View>
      
      {itemLabel && (
        <View style={styles.labelContainer}>
          <Text style={styles.labelText}>{itemLabel(value)}</Text>
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
    backgroundColor: '#050505',
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
    height: 80,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 20,
    borderTopWidth: 1,
    borderBottomWidth: 1,
    borderColor: 'rgba(255,255,255,0.05)',
  },
  itemContainer: {
    height: 80,
    justifyContent: 'center',
    alignItems: 'center',
  },
  itemText: {
    color: 'rgba(255,255,255,0.4)',
    fontSize: 18,
    fontWeight: '600',
  },
  selectedItemText: {
    color: Colors.primary,
    fontSize: 32,
    fontWeight: 'bold',
    textShadowColor: Colors.primary,
    textShadowRadius: 10,
  },
  pointer: {
    position: 'absolute',
    bottom: 0,
    width: 0, 
    height: 0, 
    borderLeftWidth: 10,
    borderRightWidth: 10,
    borderBottomWidth: 10,
    borderStyle: 'solid',
    backgroundColor: 'transparent',
    borderLeftColor: 'transparent',
    borderRightColor: 'transparent',
    borderBottomColor: Colors.primary,
  },
  labelContainer: {
    position: 'absolute',
    bottom: 10,
    backgroundColor: 'rgba(255, 110, 0, 0.1)',
    paddingHorizontal: 20,
    paddingVertical: 8,
    borderRadius: 25,
    borderWidth: 1,
    borderColor: 'rgba(255, 110, 0, 0.2)',
  },
  labelText: {
    color: '#FFF',
    fontSize: 14,
    fontWeight: 'bold',
    textTransform: 'uppercase',
  }
});
