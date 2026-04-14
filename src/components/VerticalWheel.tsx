import React, { useRef } from 'react';
import { FlatList, Platform, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { Colors } from '../theme/theme';
interface VerticalWheelProps {
  value: number;
  onValueChange: (val: number) => void;
  min?: number;
  max?: number;
  itemLabel?: (item: number) => string;
}

export default function VerticalWheel({ value, onValueChange, min = 1, max = 100, itemLabel }: VerticalWheelProps) {
  const ITEM_HEIGHT = 46;
  const listRef = useRef<FlatList>(null);
  
  const data = Array.from({ length: max - min + 1 }, (_, i) => min + i);

  const handleMomentumScrollEnd = (event: any) => {
    const offsetY = event.nativeEvent.contentOffset.y;
    const index = Math.round(offsetY / ITEM_HEIGHT);
    const newValue = data[index];
    if (newValue !== undefined && newValue !== value) {
      onValueChange(newValue);
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.selectionHighlight} pointerEvents="none" />
      <FlatList
        ref={listRef}
        data={data}
        keyExtractor={(item) => item.toString()}
        showsVerticalScrollIndicator={Platform.OS === 'web'}
        snapToInterval={ITEM_HEIGHT}
        decelerationRate="fast"
        onMomentumScrollEnd={handleMomentumScrollEnd}
        initialScrollIndex={value > min ? value - min : 0}
        getItemLayout={(_, index) => ({
          length: ITEM_HEIGHT,
          offset: ITEM_HEIGHT * index,
          index,
        })}
        contentContainerStyle={{ paddingVertical: ITEM_HEIGHT }} // 1 item padding top and bottom to center the first/last items
        renderItem={({ item }) => {
          const isSelected = item === value;
          const label = itemLabel ? itemLabel(item) : item;
          return (
            <TouchableOpacity 
              activeOpacity={0.7}
              onPress={() => {
                listRef.current?.scrollToIndex({ index: item - min, animated: true });
                if (value !== item) onValueChange(item);
              }}
              style={[styles.item, { height: ITEM_HEIGHT }]}
            >
              <Text numberOfLines={1} style={[styles.itemText, isSelected && styles.selectedItemText]}>
                {label}
              </Text>
            </TouchableOpacity>
          );
        }}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    height: 138, // 3 items exactly
    width: 120,
    alignSelf: 'center',
    backgroundColor: '#151515',
    borderRadius: 16,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.08)',
  },
  selectionHighlight: {
    position: 'absolute',
    top: 46,
    left: 0,
    right: 0,
    height: 46,
    backgroundColor: 'rgba(255,255,255,0.08)',
    borderTopWidth: 1,
    borderBottomWidth: 1,
    borderColor: Colors.primary,
    zIndex: 1,
  },
  item: {
    justifyContent: 'center',
    alignItems: 'center',
    zIndex: 2,
  },
  itemText: {
    fontSize: 20,
    color: Colors.textMuted,
    fontWeight: '600',
  },
  selectedItemText: {
    fontSize: 26,
    color: Colors.primary,
    fontWeight: 'bold',
  }
});
