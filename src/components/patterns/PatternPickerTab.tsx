import React, { useCallback, useRef, useState, useEffect } from 'react';
import { ScrollView, FlatList, StyleSheet, View, Text, TouchableOpacity, Animated, Platform } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { LinearGradient } from 'expo-linear-gradient';
import { SK8LYTZ_TEMPLATES } from '../../protocols/PatternEngine';
import { Spacing , ThemePalette } from '../../theme/theme';
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
  Colors: ThemePalette;
}

const CATEGORIES = ['Solid', 'Rainbow', 'Sparkle', 'Chase', 'Marquee', 'Wave', 'Breathe', 'SK8Lytz'];

const CATEGORY_STYLES: Record<string, { icon: string, colors: string[], start?: any, end?: any }> = {
  Solid: { icon: 'format-color-fill', colors: ['#00F0FF', '#0080FF'] },
  Rainbow: { icon: 'palette', colors: ['#FF0000', '#FFFF00', '#00FF00', '#00FFFF', '#0000FF', '#FF00FF'], start: {x: 0, y: 0}, end: {x: 1, y: 0} },
  Sparkle: { icon: 'star-four-points', colors: ['#222222', '#666666', '#222222'], start: {x: 0, y: 0}, end: {x: 1, y: 1} },
  Chase: { icon: 'run-fast', colors: ['#FF0055', '#330011'], start: {x: 0, y: 0}, end: {x: 1, y: 0} },
  Marquee: { icon: 'dots-horizontal-circle-outline', colors: ['#FF9900', '#331100', '#FF9900'], start: {x: 0, y: 0}, end: {x: 1, y: 0} },
  Wave: { icon: 'waveform', colors: ['#0000FF', '#00FFFF', '#0000FF'], start: {x: 0, y: 0}, end: {x: 1, y: 0} },
  Breathe: { icon: 'weather-windy', colors: ['#00F0FF', '#002244', '#00F0FF'], start: {x: 0, y: 0}, end: {x: 1, y: 0} },
  SK8Lytz: { icon: 'star-shooting', colors: ['#FF00FF', '#00FFFF', '#FF0055'], start: {x: 0, y: 0}, end: {x: 1, y: 1} },
};

const AnimatedCategoryPill = ({ cat, isActive, onPress }: { cat: string, isActive: boolean, onPress: () => void }) => {
  const scale = useRef(new Animated.Value(isActive ? 1.05 : 1)).current;
  const opacity = useRef(new Animated.Value(isActive ? 1 : 0.4)).current;

  useEffect(() => {
    Animated.parallel([
      Animated.spring(scale, {
        toValue: isActive ? 1.1 : 1,
        useNativeDriver: Platform.OS !== 'web',
        friction: 6,
        tension: 100,
      }),
      Animated.timing(opacity, {
        toValue: isActive ? 1 : 0.4,
        duration: 200,
        useNativeDriver: Platform.OS !== 'web',
      })
    ]).start();
  }, [isActive, scale, opacity]);

  const styleData = CATEGORY_STYLES[cat] || CATEGORY_STYLES['Solid'];

  return (
    <TouchableOpacity onPress={onPress} activeOpacity={0.8}>
      <Animated.View style={[
        styles.categoryPill,
        { transform: [{ scale }], overflow: 'hidden', borderWidth: isActive ? 2 : 1, borderColor: isActive ? '#FFF' : 'rgba(255,255,255,0.1)', paddingHorizontal: 0, paddingVertical: 0 }
      ]}>
        <Animated.View style={[StyleSheet.absoluteFill, { opacity }]}>
          <LinearGradient
            colors={styleData.colors as unknown as readonly [string, string, ...string[]]}
            start={styleData.start}
            end={styleData.end}
            style={StyleSheet.absoluteFill}
          />
        </Animated.View>
        <View style={{ flexDirection: 'row', alignItems: 'center', gap: 6, paddingHorizontal: Spacing.md, paddingVertical: 8, zIndex: 2 }}>
          <MaterialCommunityIcons name={styleData.icon as keyof typeof MaterialCommunityIcons.glyphMap} size={14} color="#FFF" style={{ textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 4 }} />
          <Text style={[styles.categoryText, { color: '#FFF', textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 4, textShadowOffset: { width: 0, height: 1 } }]}>
            {cat.toUpperCase()}
          </Text>
        </View>
      </Animated.View>
    </TouchableOpacity>
  );
};

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
    if (effect.isHidden) return false;
    return effect.group === activeCategory;
  });

  const propsRef = useRef({ selectedEffectId, fgColor, bgColor, speed, brightness, direction, points, onSelect, Colors, visibleIds });
  propsRef.current = { selectedEffectId, fgColor, bgColor, speed, brightness, direction, points, onSelect, Colors, visibleIds };

  const renderItem = useCallback(({ item: effect }: any) => {
    const p = propsRef.current;
    return (
      <View style={{ width: '48%' }}>
        <PatternCard
          effect={effect}
          isSelected={p.selectedEffectId === effect.id}
          fgColor={p.fgColor}
          bgColor={p.bgColor}
          speed={p.speed}
          brightness={p.brightness}
          direction={p.direction}
          points={p.points}
          onSelect={p.onSelect}
          Colors={p.Colors}
          autoPlay={p.visibleIds.has(effect.id) || p.selectedEffectId === effect.id}
        />
      </View>
    );
  }, []);

  return (
    <View style={{ flex: 1 }}>
      {/* Horizontal Category Wheel */}
      <View style={styles.wheelContainer}>
        <ScrollView
          horizontal
          showsHorizontalScrollIndicator={false}
          contentContainerStyle={styles.wheelContent}
        >
          {CATEGORIES.map((cat) => (
            <AnimatedCategoryPill
              key={cat}
              cat={cat}
              isActive={activeCategory === cat}
              onPress={() => setActiveCategory(cat)}
            />
          ))}
        </ScrollView>
      </View>

      <FlatList removeClippedSubviews={true}
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
        extraData={`${selectedEffectId}-${fgColor}-${bgColor}-${speed}-${brightness}-${direction}-${visibleIds.size}`}
        renderItem={renderItem}
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
