/**
 * FavoritesPanel.tsx — "YOURS" + "SK8Lytz Picks" dual preset panel.
 *
 * Renders when activeMode === 'FAVORITES'. Uses the shared PresetCard
 * component for both user favorites and curated picks sections.
 *
 * Extracted from DockedController.tsx (Phase 3).
 */
import React from 'react';
import { View, Text, FlatList, Dimensions } from 'react-native';
import { Typography, Layout, Spacing } from '../../theme/theme';
import PresetCard from './PresetCard';
import type { IFavoriteState } from '../../types/dashboard.types';

interface FavoritesPanelProps {
  favorites: IFavoriteState[];
  curatedPresets: IFavoriteState[];
  picksLoading: boolean;
  onLoadFavorite: (fav: IFavoriteState, context?: 'FAVORITE' | 'PICK' | 'COMMUNITY') => void;
  onEditFavorite: (id: string, name: string) => void;
  isDark: boolean;
  Colors: any;
  styles: {
    presetCard: any;
    presetTitle: any;
  };
}

const FavoritesPanel = React.memo(({
  favorites,
  curatedPresets,
  onLoadFavorite,
  onEditFavorite,
  isDark,
  Colors,
  styles,
}: FavoritesPanelProps) => {
  const cardWidth = (Dimensions.get('window').width - (Layout.padding * 2)) / 3.5;

  const emptyPlaceholder = (keyPrefix: string) => (
    <View style={[styles.presetCard, { width: cardWidth, marginHorizontal: Spacing.xs, borderWidth: 1.5, borderStyle: 'dashed', borderColor: 'rgba(255,255,255,0.08)', backgroundColor: 'transparent', elevation: 0, shadowOpacity: 0 }]} />
  );

  return (
    <View style={{ flex: 1, paddingVertical: Layout.padding, paddingBottom: Spacing.xl, justifyContent: 'space-between' }}>
      {/* ── YOURS Section ── */}
      <View style={{ flex: 1 }}>
        <Text style={[Typography.title, isDark && { color: '#FFF' }, { fontSize: 13, paddingHorizontal: Layout.padding, marginBottom: Spacing.sm }]}>YOURS</Text>
        <FlatList
          style={{ flex: 1 }}
          horizontal
          showsHorizontalScrollIndicator={false}
          data={favorites.length > 0 ? favorites : [null as any]}
          keyExtractor={(item, index) => item ? item.id : `empty-yours-${index}`}
          contentContainerStyle={{ paddingHorizontal: Layout.padding, flexGrow: 1 }}
          renderItem={({ item: fav }) => {
            if (!fav) return emptyPlaceholder('yours');
            return (
              <PresetCard
                preset={fav}
                onPress={() => onLoadFavorite(fav)}
                showEditButton
                onEdit={() => onEditFavorite(fav.id, fav.name)}
                accentFallback={Colors.primary}
                cardWidth={cardWidth}
                styles={styles}
                Colors={Colors}
              />
            );
          }}
        />
      </View>

      {/* ── SK8Lytz Picks Section ── */}
      <View style={{ flex: 1, marginTop: Spacing.lg }}>
        <Text style={[Typography.title, isDark && { color: '#FFF' }, { fontSize: 13, paddingHorizontal: Layout.padding, marginBottom: Spacing.sm }]}>SK8Lytz Picks</Text>
        <FlatList
          style={{ flex: 1 }}
          horizontal
          showsHorizontalScrollIndicator={false}
          data={curatedPresets.length > 0 ? curatedPresets : [null as any]}
          keyExtractor={(item, index) => item ? item.id : `empty-picks-${index}`}
          contentContainerStyle={{ paddingHorizontal: Layout.padding, flexGrow: 1 }}
          renderItem={({ item: fav }) => {
            if (!fav) return emptyPlaceholder('picks');
            return (
              <PresetCard
                preset={fav}
                onPress={() => onLoadFavorite(fav, 'PICK')}
                accentFallback={Colors.secondary}
                cardWidth={cardWidth}
                styles={styles}
                Colors={Colors}
              />
            );
          }}
        />
      </View>
    </View>
  );
});

export default FavoritesPanel;
