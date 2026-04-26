import React, { useCallback, useRef, useState } from 'react';
import { ScrollView, FlatList, StyleSheet, View, Text, TouchableOpacity } from 'react-native';
import { SK8LYTZ_TEMPLATES } from '../../protocols/PatternEngine';
import { Spacing } from '../../theme/theme';
import { PatternCard } from './PatternCard';

interface PatternPickerTabProps {
  selectedEffectId: number;
  fgColor: string;
  bgColor: string;
  speed: number;
  brightness: number;
  points: number;
  direction: number;
  onSelect: (id: number) => void;
  Colors: any;
}

const CATEGORIES = ['Solid', 'Rainbow', 'Sparkle', 'Chase', 'Marquee', 'Wave', 'Breathe', 'Test'];

export const PatternPickerTab: React.FC<PatternPickerTabProps> = ({
  selectedEffectId, fgColor, bgColor, speed, brightness, points, direction, onSelect, Colors
}) => {
  const [activeCategory, setActiveCategory] = useState<string>('Solid');
  const [visibleIds, setVisibleIds] = useState<Set<number>>(new Set());

  const onViewableItemsChanged = useRef(({ viewableItems }: any) => {
    const next = new Set<number>();
    viewableItems.forEach((v: any) => next.add(v.item.id));
    setVisibleIds(next);
  }).current;

  const viewabilityConfig = useRef({
    itemVisiblePercentThreshold: 10,
  }).current;

  const filteredTemplates = SK8LYTZ_TEMPLATES.filter((effect) => {
    if (effect.group === 'Street') return false;
    return effect.group === activeCategory;
  });

  return (
    <View style={{ flex: 1 }}>
      {/* Horizontal Category Wheel */}
      <View style={styles.wheelContainer}>
        <ScrollView
          horizontal
          showsHorizontalScrollIndicator={false}
          contentContainerStyle={styles.wheelContent}
        >
          {CATEGORIES.map((cat) => {
            const isActive = activeCategory === cat;
            return (
              <TouchableOpacity
                key={cat}
                style={[
                  styles.categoryPill,
                  isActive && { backgroundColor: 'rgba(0,240,255,0.1)', borderColor: '#00F0FF' }
                ]}
                onPress={() => setActiveCategory(cat)}
              >
                <Text style={[styles.categoryText, isActive && { color: '#00F0FF' }]}>
                  {cat.toUpperCase()}
                </Text>
              </TouchableOpacity>
            );
          })}
        </ScrollView>
      </View>

      <FlatList
        data={filteredTemplates}
        keyExtractor={(item) => item.id.toString()}
        numColumns={2}
        style={{ flex: 1 }}
        contentContainerStyle={{
          paddingHorizontal: Spacing.xs,
          paddingBottom: Spacing.md,
        }}
        columnWrapperStyle={{
          justifyContent: 'space-between',
          marginBottom: Spacing.sm,
        }}
        showsVerticalScrollIndicator={false}
        keyboardShouldPersistTaps="handled"
        onViewableItemsChanged={onViewableItemsChanged}
        viewabilityConfig={viewabilityConfig}
        initialNumToRender={8}
        windowSize={3}
        removeClippedSubviews={true}
        renderItem={({ item: effect }) => (
          <View style={{ width: '48%' }}>
            <PatternCard
              effect={effect}
              isSelected={selectedEffectId === effect.id}
              fgColor={fgColor}
              bgColor={bgColor}
              speed={speed}
              brightness={brightness}
              direction={direction}
              points={points}
              onSelect={() => onSelect(effect.id)}
              Colors={Colors}
              autoPlay={visibleIds.has(effect.id) || selectedEffectId === effect.id}
            />
          </View>
        )}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  wheelContainer: {
    height: 48,
    marginBottom: Spacing.sm,
    borderBottomWidth: 1,
    borderBottomColor: 'rgba(255,255,255,0.05)',
  },
  wheelContent: {
    paddingHorizontal: Spacing.sm,
    alignItems: 'center',
    gap: Spacing.xs,
  },
  categoryPill: {
    minWidth: 90,
    paddingHorizontal: Spacing.md,
    paddingVertical: 8,
    borderRadius: 20,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
    backgroundColor: 'rgba(255,255,255,0.02)',
    justifyContent: 'center',
    alignItems: 'center',
  },
  categoryText: {
    color: 'rgba(255,255,255,0.5)',
    fontSize: 11,
    fontWeight: '800',
    letterSpacing: 1,
  },
});
