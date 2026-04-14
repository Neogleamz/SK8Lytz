/**
 * PresetCard.tsx — Shared reusable card for Favorites and SK8Lytz Picks.
 *
 * Consolidates ~180 lines of near-identical card rendering JSX that was
 * duplicated between the "YOURS" and "SK8Lytz Picks" sections.
 */
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { LinearGradient } from 'expo-linear-gradient';
import React from 'react';
import { Text, TouchableOpacity, View } from 'react-native';
import { Spacing } from '../../theme/theme';
import type { IFavoriteState } from '../../types/dashboard.types';
import MarqueeText from '../MarqueeText';

interface PresetCardProps {
  preset: IFavoriteState;
  onPress: () => void;
  /** Show edit pencil icon (user favorites only). */
  showEditButton?: boolean;
  onEdit?: () => void;
  /** Accent color fallback when preset colors are missing. */
  accentFallback: string;
  cardWidth: number;
  /** Styles from createStyles for presetCard and presetTitle. */
  styles: {
    presetCard: any;
    presetTitle: any;
  };
  Colors: any;
}

/** Resolves the dominant glow color from a preset's mode and palette. */
const resolveGlowColor = (fav: IFavoriteState, fallback: string): string => {
  if (fav.mode === 'MUSIC') return fav.musicPrimaryColor || fav.musicSecondaryColor || fallback;
  if (fav.mode === 'PATTERN' || fav.mode === 'MULTIMODE') return fav.fixedFgColor || fav.fixedBgColor || fallback;
  if (fav.mode === 'MULTI' || fav.mode === 'BUILDER') return fav.multiColors?.[0] || fallback;
  return fallback;
};

/** Resolves gradient color pair from a preset's mode and palette. */
const resolveGradientColors = (fav: IFavoriteState, glow: string): string[] => {
  if (fav.mode === 'MUSIC' && fav.musicPrimaryColor) return [fav.musicPrimaryColor, fav.musicSecondaryColor || fav.musicPrimaryColor];
  if ((fav.mode === 'PATTERN' || fav.mode === 'MULTIMODE') && fav.fixedFgColor) return [fav.fixedFgColor, fav.fixedBgColor || fav.fixedFgColor];
  if ((fav.mode === 'MULTI' || fav.mode === 'BUILDER') && fav.multiColors && fav.multiColors.length > 0)
    return fav.multiColors.length === 1 ? [fav.multiColors[0], fav.multiColors[0]] : fav.multiColors;
  return [glow, glow];
};

/** Resolves the mode icon name for the preset. */
const resolveModeIcon = (mode: string): string => {
  if (mode === 'MUSIC') return 'microphone-outline';
  if (mode === 'RBM' || mode === 'PROGRAMS') return 'animation-play';
  if (mode === 'MULTI' || mode === 'BUILDER') return 'shape-square-plus';
  return 'speedometer';
};

const PresetCard = React.memo(({
  preset,
  onPress,
  showEditButton = false,
  onEdit,
  accentFallback,
  cardWidth,
  styles,
  Colors,
}: PresetCardProps) => {
  const glow = resolveGlowColor(preset, accentFallback);
  const gradColors = resolveGradientColors(preset, glow);
  const iconName = resolveModeIcon(preset.mode);

  return (
    <TouchableOpacity
      style={{ flex: 1, marginHorizontal: Spacing.xs, shadowColor: glow, shadowOffset: { width: 0, height: 0 }, shadowOpacity: 0.5, shadowRadius: 10, elevation: 8 }}
      onPress={onPress}
    >
      <LinearGradient
        colors={gradColors as any}
        start={{ x: 0, y: 0 }}
        end={{ x: 1, y: 1 }}
        style={{ flex: 1, width: cardWidth, borderRadius: 9, padding: 1.5 }}
      >
        <View style={[styles.presetCard, { flex: 1, width: '100%', marginHorizontal: 0, borderWidth: 0, borderRadius: 8, justifyContent: 'flex-start', paddingVertical: Spacing.md, paddingHorizontal: Spacing.sm }]}>
          {showEditButton && onEdit && (
            <TouchableOpacity
              style={{ position: 'absolute', right: 4, top: 4, zIndex: 10, padding: Spacing.xs }}
              onPress={onEdit}
            >
              <MaterialCommunityIcons name="pencil-outline" size={12} color={Colors.textMuted} />
            </TouchableOpacity>
          )}

          <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', width: '100%' }}>
            <MaterialCommunityIcons
              name={iconName as any}
              size={24}
              color={glow}
              style={{ marginBottom: Spacing.sm }}
            />
            <MarqueeText style={[styles.presetTitle, { fontSize: 13, textAlign: 'center', width: '100%' }]}>
              {preset.customName || preset.name}
            </MarqueeText>
          </View>

          {/* Speed/Sensitivity + Brightness metadata row */}
          <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', width: '100%', marginBottom: Spacing.sm, opacity: 0.8, paddingHorizontal: Spacing.xs }}>
            <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.xxs }}>
              {preset.mode === 'MUSIC' ? (
                <><MaterialCommunityIcons name="microphone-outline" size={10} color={glow} /><Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(preset.micSensitivity || preset.speed || 50)}%</Text></>
              ) : (
                <><MaterialCommunityIcons name="speedometer" size={10} color={glow} /><Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(preset.speed || 50)}%</Text></>
              )}
            </View>
            <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.xxs }}>
              <MaterialCommunityIcons name="brightness-6" size={10} color={glow} />
              <Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(preset.brightness || 100)}%</Text>
            </View>
          </View>

          {/* Color strip preview */}
          {(() => {
            if (preset.mode === 'MUSIC') {
              return (
                <View style={{ width: '80%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', alignSelf: 'center', marginTop: Spacing.xs }}>
                  <View style={{ flex: 1, backgroundColor: preset.musicPrimaryColor || '#00FFFF' }} />
                  <View style={{ flex: 1, backgroundColor: preset.musicSecondaryColor || '#FF00FF' }} />
                </View>
              );
            } else if (preset.mode === 'PATTERN' || preset.mode === 'MULTIMODE') {
              return (
                <View style={{ width: '80%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', alignSelf: 'center', marginTop: Spacing.xs }}>
                  <View style={{ flex: 1, backgroundColor: preset.fixedFgColor || '#FFFFFF' }} />
                  <View style={{ flex: 1, backgroundColor: preset.fixedBgColor || '#000000' }} />
                </View>
              );
            } else if (preset.mode === 'MULTI' || preset.mode === 'BUILDER') {
              const colors = preset.multiColors || ['#FFFFFF'];
              return (
                <View style={{ width: '90%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', alignSelf: 'center', marginTop: Spacing.xs }}>
                  {colors.map((c: string, i: number) => <View key={i} style={{ flex: 1, backgroundColor: c }} />)}
                </View>
              );
            }
            return <View style={{ height: 6, width: '100%', marginTop: Spacing.xs }} />;
          })()}
        </View>
      </LinearGradient>
    </TouchableOpacity>
  );
});

export default PresetCard;
