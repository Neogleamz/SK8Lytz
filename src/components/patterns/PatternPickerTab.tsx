import React, { useCallback, useRef, useState } from 'react';
import { ScrollView, StyleSheet, View, Text, TouchableOpacity } from 'react-native';
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

const CATEGORIES = ['Solid', 'Rainbow', 'Sparkle', 'Chase', 'Marquee', 'Wave', 'Breathe'];

export const PatternPickerTab: React.FC<PatternPickerTabProps> = ({
  selectedEffectId, fgColor, bgColor, speed, brightness, points, direction, onSelect, Colors
}) => {
  const [activeCategory, setActiveCategory] = useState<string>('Solid');

  // ── Viewport gate (v3 — perf hardened) ────────────────────────────────────
  // Gate starts CLOSED: visibleIds begins as empty Set so ZERO cards animate
  // on mount. This eliminates the "initial storm" where 30+ setIntervals fire
  // simultaneously before the ScrollView has measured its height.
  //
  // Flow:
  //   1. Mount → all cards render with autoPlay=false (only selected animates)
  //   2. ScrollView.onLayout fires → viewportHeightRef measured → debounced update
  //   3. Card onLayout fires × N → cardYPositions populated → debounced update
  //   4. Debounce timer fires → single setVisibleIds() with correct ~8 visible IDs
  //   5. Only those 8 cards get autoPlay=true → 8 intervals, not 30
  const scrollYRef = useRef(0);
  const viewportHeightRef = useRef(0);
  const cardYPositions = useRef<Record<number, number>>({});
  const [visibleIds, setVisibleIds] = useState<Set<number>>(new Set());
  const debounceTimer = useRef<ReturnType<typeof setTimeout> | null>(null);

  // Debounced visibility update — batches 30+ onLayout calls into 1 setState
  const scheduleVisibilityUpdate = useCallback(() => {
    if (debounceTimer.current) clearTimeout(debounceTimer.current);
    debounceTimer.current = setTimeout(() => {
      if (viewportHeightRef.current === 0) return;
      const scrollY = scrollYRef.current;
      const viewH = viewportHeightRef.current;
      const visTop = scrollY - 120;
      const visBot = scrollY + viewH + 120;
      const positions = cardYPositions.current;
      const next = new Set<number>();
      for (const [idStr, y] of Object.entries(positions)) {
        if (y >= visTop && y <= visBot) next.add(Number(idStr));
      }
      setVisibleIds(next);
    }, 50); // 50ms debounce — all onLayouts settle within one frame
  }, []);

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
        onLayout={(e) => {
          viewportHeightRef.current = e.nativeEvent.layout.height;
          scheduleVisibilityUpdate();
        }}
        onScroll={(e) => {
          scrollYRef.current = e.nativeEvent.contentOffset.y;
          scheduleVisibilityUpdate();
        }}
        scrollEventThrottle={150}
      >
        {filteredTemplates.map((effect) => (
          <View
            key={effect.id}
            onLayout={(e) => {
              cardYPositions.current[effect.id] = e.nativeEvent.layout.y;
              scheduleVisibilityUpdate();
            }}
            style={{ width: '48%', marginBottom: Spacing.sm }}
          >
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
              autoPlay={
                // Gate starts closed (empty Set) so no initial storm.
                // Selected card always animates for immediate feedback.
                visibleIds.has(effect.id) || selectedEffectId === effect.id
              }
            />
          </View>
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
