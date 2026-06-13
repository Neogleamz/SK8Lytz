/**
 * FavoritesPanel.tsx — "YOURS" + "SK8Lytz Picks" dual preset panel.
 *
 * Renders when activeMode === 'FAVORITES'. Uses the shared PresetCard
 * component for both user favorites and curated picks sections.
 *
 * Extracted from DockedController.tsx (Phase 3).
 */
import React, { useCallback } from 'react';
import { Dimensions, FlatList, Text, View, ActivityIndicator } from 'react-native';
import { Layout, Spacing, Typography , ThemePalette } from '../../theme/theme';
import type { IFavoriteState } from '../../types/dashboard.types';
import PresetCard from './PresetCard';

interface FavoritesPanelProps {
  favorites: IFavoriteState[];
  curatedPresets: IFavoriteState[];
  picksLoading: boolean;
  picksError?: string | null;
  onLoadFavorite: (fav: IFavoriteState, context?: 'FAVORITE' | 'PICK' | 'COMMUNITY') => void;
  onEditFavorite: (id: string, name: string) => void;
  isDark: boolean;
  Colors: ThemePalette;
}

const FavoritesPanel = React.memo(({
  favorites,
  curatedPresets,
  picksLoading,
  picksError,
  onLoadFavorite,
  onEditFavorite,
  isDark,
  Colors,
}: FavoritesPanelProps) => {
  const cardWidth = (Dimensions.get('window').width - (Layout.padding * 2)) / 3.5;
  const localStyles = React.useMemo(() => createStyles(Colors), [Colors]);



  const keyExtractorYours = useCallback(
    (item: IFavoriteState, index: number) => (item ? item.id : `empty-yours-${index}`),
    []
  );
  const keyExtractorPicks = useCallback(
    (item: IFavoriteState, index: number) => (item ? item.id : `empty-picks-${index}`),
    []
  );

  const renderYoursItem = React.useCallback(({ item: fav }: { item: IFavoriteState }) => {
    return (
      <PresetCard
        preset={fav}
        onPress={() => onLoadFavorite(fav)}
        showEditButton
        onEdit={() => onEditFavorite(fav.id, fav.name)}
        accentFallback={Colors.primary}
        cardWidth={cardWidth}
        styles={localStyles}
        Colors={Colors}
      />
    );
  }, [Colors, cardWidth, localStyles, onEditFavorite, onLoadFavorite]);

  const renderPicksItem = React.useCallback(({ item: fav }: { item: IFavoriteState }) => {
    return (
      <PresetCard
        preset={fav}
        onPress={() => onLoadFavorite(fav, 'PICK')}
        accentFallback={Colors.secondary}
        cardWidth={cardWidth}
        styles={localStyles}
        Colors={Colors}
      />
    );
  }, [Colors, cardWidth, localStyles, onLoadFavorite]);

  const renderEmptyYours = React.useCallback(() => (
    <View style={localStyles.matrixContainer}>
      <Text style={[localStyles.matrixText, isDark && localStyles.textWhite]}>
        No favorites saved yet.
      </Text>
    </View>
  ), [localStyles, isDark]);

  const renderEmptyPicks = React.useCallback(() => {
    if (picksLoading) {
      return (
        <View style={localStyles.matrixContainer}>
          <ActivityIndicator size="small" color={Colors.primary} />
          <Text style={[localStyles.matrixText, isDark && localStyles.textWhite, { marginTop: Spacing.xs }]}>
            Loading Picks...
          </Text>
        </View>
      );
    }
    if (picksError) {
      return (
        <View style={localStyles.matrixContainer}>
          <Text style={[localStyles.matrixText, { color: Colors.error }]}>
            Error: {picksError}
          </Text>
        </View>
      );
    }
    return (
      <View style={localStyles.matrixContainer}>
        <Text style={[localStyles.matrixText, isDark && localStyles.textWhite]}>
          No picks available.
        </Text>
      </View>
    );
  }, [picksLoading, picksError, localStyles, isDark, Colors]);

  return (
    <View style={localStyles.container}>
      {/* ── YOURS Section ── */}
      <View style={localStyles.section}>
        <Text style={[Typography.title, isDark && localStyles.textWhite, localStyles.sectionTitle]}>YOURS</Text>
        <FlatList removeClippedSubviews={true} initialNumToRender={12} windowSize={5}
          style={localStyles.list}
          horizontal
          showsHorizontalScrollIndicator={false}
          data={favorites}
          keyExtractor={keyExtractorYours}
          contentContainerStyle={favorites.length === 0 ? localStyles.emptyListContent : localStyles.listContent}
          renderItem={renderYoursItem}
          ListEmptyComponent={renderEmptyYours}
        />
      </View>

      {/* ── SK8Lytz Picks Section ── */}
      <View style={[localStyles.section, localStyles.picksSection]}>
        <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', paddingRight: Layout.padding }}>
          <Text style={[Typography.title, isDark && localStyles.textWhite, localStyles.sectionTitle]}>SK8Lytz Picks</Text>
          {picksLoading && <ActivityIndicator size="small" color={Colors.primary} />}
        </View>
        <FlatList removeClippedSubviews={true} initialNumToRender={12} windowSize={5}
          style={localStyles.list}
          horizontal
          showsHorizontalScrollIndicator={false}
          data={curatedPresets}
          keyExtractor={keyExtractorPicks}
          contentContainerStyle={curatedPresets.length === 0 ? localStyles.emptyListContent : localStyles.listContent}
          renderItem={renderPicksItem}
          ListEmptyComponent={renderEmptyPicks}
        />
      </View>
    </View>
  );
});

export default FavoritesPanel;

const createStyles = (Colors: ThemePalette) => ({
  presetCard: {
    width: '48%' as import('react-native').DimensionValue,
    minHeight: 80,
    padding: Spacing.sm,
    backgroundColor: Colors.isDark ? 'rgba(0,0,0,0.6)' : 'rgba(0,0,0,0.04)',
    borderRadius: 16,
    borderWidth: 1.5,
    alignItems: 'center' as const,
    justifyContent: 'center' as const,
  },
  presetTitle: {
    ...Typography.body,
    fontWeight: 'bold' as const,
    color: Colors.text,
  },
  container: { flex: 1, paddingVertical: Layout.padding, paddingBottom: Spacing.xl, justifyContent: 'space-between' as const },
  section: { flex: 1 },
  picksSection: { marginTop: Spacing.lg },
  sectionTitle: { fontSize: 13, paddingHorizontal: Layout.padding, marginBottom: Spacing.sm },
  textWhite: { color: '#FFF' },
  list: { flex: 1 },
  listContent: { paddingHorizontal: Layout.padding, flexGrow: 1 },
  emptyListContent: { paddingHorizontal: Layout.padding, flexGrow: 1, justifyContent: 'center' as const, alignItems: 'center' as const },
  emptyPlaceholder: { marginHorizontal: Spacing.xs, borderWidth: 1.5, borderStyle: 'dashed' as const, borderColor: 'rgba(255,255,255,0.08)', backgroundColor: 'transparent', elevation: 0, shadowOpacity: 0 },
  matrixContainer: { flex: 1, justifyContent: 'center' as const, alignItems: 'center' as const, paddingHorizontal: Spacing.lg, paddingVertical: Spacing.xl },
  matrixText: { ...Typography.body, opacity: 0.7, textAlign: 'center' as const }
});
