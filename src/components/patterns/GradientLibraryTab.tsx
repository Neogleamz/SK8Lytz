import React, { useCallback } from 'react';
import { StyleSheet, Text, TouchableOpacity, View, FlatList, ActivityIndicator, Alert } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Spacing , ThemePalette } from '../../theme/theme';
import { useGradients } from '../../hooks/useGradients';
import { CustomBuilderPreset, PositionalMathBuffer } from '../../protocols/PositionalMathBuffer';

const LIST_CONTENT_CONTAINER_STYLE = {
  paddingHorizontal: Spacing.xs,
  paddingBottom: Spacing.xl * 2,
};

const LIST_COLUMN_WRAPPER_STYLE = {
  justifyContent: 'space-between' as const,
  marginBottom: Spacing.sm,
};

interface GradientLibraryTabProps {
  Colors: ThemePalette;
  onOpenBuilder: (preset?: CustomBuilderPreset) => void;
  onApplyGradient: (preset: CustomBuilderPreset) => void;
}

export const GradientLibraryTab: React.FC<GradientLibraryTabProps> = ({ Colors, onOpenBuilder, onApplyGradient }) => {
  const { gradients, status, error, deleteGradient, refreshGradients } = useGradients();

  const handleDelete = (preset: CustomBuilderPreset) => {
    if (preset.id.startsWith('builtin_')) return;
    Alert.alert(
      'Delete Gradient',
      `Are you sure you want to delete "${preset.name}"?`,
      [
        { text: 'Cancel', style: 'cancel' },
        { text: 'Delete', style: 'destructive', onPress: () => deleteGradient(preset.id) }
      ]
    );
  };

  const renderGradientCard = useCallback(({ item: preset }: { item: CustomBuilderPreset }) => {
    const isBuiltin = preset.id.startsWith('builtin_');

    // 12-block static preview — matches Pro Effects pattern card density
    const previewColors = PositionalMathBuffer.generateArray(
      preset.nodes,
      12,
      preset.fill_mode === 'GRADIENT'
    );

    return (
      <View style={styles.cardWrapper}>
        <TouchableOpacity
          activeOpacity={0.75}
          style={styles.effectCard}
          onPress={() => onApplyGradient(preset)}
          onLongPress={() => !isBuiltin && onOpenBuilder(preset)}
        >
          {/* Glassmorphism refraction layer — matches Pro Effects cards */}
          <LinearGradient
            colors={['rgba(255,255,255,0.07)', 'rgba(255,255,255,0.02)']}
            start={{ x: 0, y: 0 }}
            end={{ x: 1, y: 1 }}
            style={StyleSheet.absoluteFillObject}
          />
          <View style={styles.cardRefraction} />

          <View style={styles.cardHeader}>
            <Text style={styles.effectName} numberOfLines={1}>{preset.name}</Text>
            {isBuiltin && <Text style={styles.builtinBadge}>BUILT-IN</Text>}
          </View>

          {/* Gradient color strip preview */}
          <View style={styles.stripWrapper}>
            <View style={styles.stripRow}>
              {previewColors.map((color, i) => (
                <View key={i} style={{ flex: 1, backgroundColor: `rgb(${color.r}, ${color.g}, ${color.b})` }} />
              ))}
            </View>
          </View>

          {/* Mode badge */}
          <View style={styles.cardFooter}>
            <Text style={styles.modeBadge}>
              {preset.transition_type === 0x02 ? '▶ FLOW' :
               preset.transition_type === 0x03 ? '⚡ STROBE' :
               preset.transition_type === 0x04 ? '⏭ JUMP' : '■ STATIC'}
            </Text>
            {!isBuiltin && (
              <View style={styles.cardActions}>
                <TouchableOpacity onPress={() => onOpenBuilder(preset)} style={styles.actionButton} hitSlop={{ top: 6, bottom: 6, left: 6, right: 6 }}>
                  <MaterialCommunityIcons name="pencil" size={13} color="rgba(255,255,255,0.7)" />
                </TouchableOpacity>
                <TouchableOpacity onPress={() => handleDelete(preset)} style={styles.actionButton} hitSlop={{ top: 6, bottom: 6, left: 6, right: 6 }}>
                  <MaterialCommunityIcons name="delete" size={13} color="#FF4444" />
                </TouchableOpacity>
              </View>
            )}
          </View>
        </TouchableOpacity>
      </View>
    );
  }, [onApplyGradient, onOpenBuilder]);

  const keyExtractorGradient = useCallback((item: CustomBuilderPreset) => item.id.toString(), []);

  return (
    <View style={styles.container}>
      {/* Create button */}
      <TouchableOpacity
        style={[styles.createButton, { backgroundColor: Colors.primary }]}
        onPress={() => onOpenBuilder()}
      >
        <MaterialCommunityIcons name="plus" size={20} color="#000" />
        <Text style={styles.createButtonText}>Create Custom Gradient</Text>
      </TouchableOpacity>

      {status === 'loading' ? (
        <View style={styles.centerBox}>
          <ActivityIndicator size="large" color={Colors.primary} />
        </View>
      ) : error ? (
        <View style={styles.centerBox}>
          <MaterialCommunityIcons name="alert-circle-outline" size={48} color={Colors.error} />
          <Text style={[styles.emptyTitle, { color: Colors.error }]}>Failed to load</Text>
          <Text style={[styles.emptySubtitle, { color: Colors.textMuted }]}>{error}</Text>
          <TouchableOpacity style={{ marginTop: Spacing.md }} onPress={refreshGradients}>
            <Text style={{ color: Colors.primary, fontWeight: 'bold' }}>Tap to retry</Text>
          </TouchableOpacity>
        </View>
      ) : gradients.length === 0 ? (
        <View style={styles.centerBox}>
          <MaterialCommunityIcons name="palette-swatch" size={48} color="rgba(255,255,255,0.2)" />
          <Text style={[styles.emptyTitle, { color: Colors.text }]}>No Gradients Yet</Text>
          <Text style={[styles.emptySubtitle, { color: Colors.textMuted }]}>Tap "Create" to build your first</Text>
        </View>
      ) : (
        <FlatList removeClippedSubviews={true} initialNumToRender={12} windowSize={5}
          data={gradients}
          keyExtractor={keyExtractorGradient}
          numColumns={2}
          style={styles.listContainer}
          contentContainerStyle={LIST_CONTENT_CONTAINER_STYLE}
          columnWrapperStyle={LIST_COLUMN_WRAPPER_STYLE}
          showsVerticalScrollIndicator={false}
          renderItem={renderGradientCard}
        />
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: Spacing.md,
  },
  centerBox: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    gap: Spacing.xs,
  },
  emptyTitle: {
    fontSize: 17,
    fontWeight: 'bold',
    marginTop: Spacing.md,
  },
  emptySubtitle: {
    fontSize: 13,
  },
  createButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    padding: Spacing.md,
    borderRadius: 12,
    marginBottom: Spacing.md,
  },
  createButtonText: {
    color: '#000',
    fontWeight: 'bold',
    fontSize: 15,
    marginLeft: Spacing.sm,
  },
  listContainer: {
    flex: 1,
  },
  // Card: exact same width proportion as Pro Effects cards (48% of container)
  cardWrapper: {
    width: '48%',
  },
  effectCard: {
    backgroundColor: 'rgba(255,255,255,0.04)',
    borderRadius: 10,
    borderWidth: 1.5,
    borderColor: 'rgba(255,255,255,0.08)',
    paddingHorizontal: Spacing.xs,
    paddingTop: 8,
    paddingBottom: 6,
    overflow: 'hidden',
  },
  // Diagonal refraction shimmer — same as ProEffectsPanel cards
  cardRefraction: {
    position: 'absolute',
    top: -30,
    left: -30,
    width: 100,
    height: 100,
    backgroundColor: 'rgba(255,255,255,0.04)',
    transform: [{ rotate: '45deg' }],
  },
  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 6,
  },
  effectName: {
    color: 'rgba(255,255,255,0.9)',
    fontSize: 10,
    fontWeight: '800',
    letterSpacing: 0.3,
    flex: 1,
    marginRight: 4,
  },
  builtinBadge: {
    color: '#00F0FF',
    fontSize: 7,
    fontWeight: 'bold',
    letterSpacing: 0.5,
  },
  // Color preview strip
  stripWrapper: {
    height: 8,
    borderRadius: 4,
    overflow: 'hidden',
    borderWidth: 0.5,
    borderColor: 'rgba(255,255,255,0.06)',
    marginBottom: 6,
  },
  stripRow: {
    flex: 1,
    flexDirection: 'row',
  },
  cardFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  modeBadge: {
    color: 'rgba(255,255,255,0.4)',
    fontSize: 8,
    fontWeight: '700',
    letterSpacing: 0.2,
  },
  cardActions: {
    flexDirection: 'row',
    gap: Spacing.xs,
  },
  actionButton: {
    padding: 3,
    backgroundColor: 'rgba(255,255,255,0.08)',
    borderRadius: 5,
  },
});
