import React, { useState } from 'react';
import { ScrollView, StyleSheet, View, Text, TouchableOpacity } from 'react-native';
import { SK8LYTZ_TEMPLATES } from '../../protocols/PatternEngine';
import { Spacing } from '../../theme/theme';
import { PatternCard } from './PatternCard';

interface PatternPickerTabProps {
  selectedEffectId: number;
  fgColor: string;
  bgColor: string;
  speed: number;
  points: number;
  direction: number;
  onSelect: (id: number) => void;
  Colors: any;
}

const CATEGORIES = ['All', 'Static', 'Breathe', 'Chase', 'Marquee', 'Wave', 'Rainbow', 'Sparkle & Flash'];

export const PatternPickerTab: React.FC<PatternPickerTabProps> = ({
  selectedEffectId, fgColor, bgColor, speed, points, direction, onSelect, Colors
}) => {
  const [activeCategory, setActiveCategory] = useState<string>('All');

  const filteredTemplates = SK8LYTZ_TEMPLATES.filter((effect) => {
    if (effect.group === 'Street') return false;
    if (activeCategory === 'All') return true;
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

      <ScrollView
        style={{ flex: 1 }}
        contentContainerStyle={{
          flexDirection: 'row',
          flexWrap: 'wrap',
          justifyContent: 'space-between',
          paddingHorizontal: Spacing.xs,
          paddingBottom: Spacing.md,
        }}
        showsVerticalScrollIndicator={false}
        keyboardShouldPersistTaps="handled"
      >
        {filteredTemplates.map((effect) => (
          <PatternCard
            key={effect.id}
            effect={effect}
            isSelected={selectedEffectId === effect.id}
            fgColor={fgColor}
            bgColor={bgColor}
            speed={speed}
            direction={direction}
            points={points}
            onSelect={() => onSelect(effect.id)}
            Colors={Colors}
          />
        ))}
      </ScrollView>
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

