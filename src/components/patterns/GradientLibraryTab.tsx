import React from 'react';
import { StyleSheet, Text, TouchableOpacity, View, ScrollView, ActivityIndicator, Alert } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Spacing, BorderRadius } from '../../theme/theme';
import { useGradients } from '../../hooks/useGradients';
import { CustomBuilderPreset } from '../../protocols/PositionalMathBuffer';
import { LinearGradient } from 'expo-linear-gradient';

interface GradientLibraryTabProps {
  Colors: any;
  onOpenBuilder: (preset?: CustomBuilderPreset) => void;
  onApplyGradient: (preset: CustomBuilderPreset) => void;
}

export const GradientLibraryTab: React.FC<GradientLibraryTabProps> = ({ Colors, onOpenBuilder, onApplyGradient }) => {
  const { gradients, isLoading, deleteGradient } = useGradients();

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

  const renderGradientCard = (preset: CustomBuilderPreset) => {
    const isBuiltin = preset.id.startsWith('builtin_');
    // Sort nodes to make the preview gradient
    const sortedNodes = [...preset.nodes].sort((a, b) => a.pos - b.pos);
    // Expo LinearGradient requires at least 2 colors. If user saved 1 or 0, fallback.
    const colors = sortedNodes.length >= 2 ? sortedNodes.map(n => n.color) : ['#333', '#666'];
    const locations = sortedNodes.length >= 2 ? sortedNodes.map(n => n.pos) : [0, 1];

    return (
      <TouchableOpacity
        key={preset.id}
        style={styles.cardContainer}
        onPress={() => onApplyGradient(preset)}
      >
        <LinearGradient
          colors={colors}
          locations={locations}
          start={{ x: 0, y: 0 }}
          end={{ x: 1, y: 0 }}
          style={styles.cardGradient}
        >
          <View style={styles.cardOverlay}>
            <View style={{ flex: 1 }}>
              <Text style={styles.cardTitle} numberOfLines={1}>{preset.name}</Text>
              {isBuiltin && <Text style={styles.builtinBadge}>BUILT-IN</Text>}
            </View>
            <View style={styles.cardActions}>
              {!isBuiltin && (
                <TouchableOpacity onPress={() => onOpenBuilder(preset)} style={styles.actionButton}>
                  <MaterialCommunityIcons name="pencil" size={20} color="#FFF" />
                </TouchableOpacity>
              )}
              {!isBuiltin && (
                <TouchableOpacity onPress={() => handleDelete(preset)} style={styles.actionButton}>
                  <MaterialCommunityIcons name="delete" size={20} color="#FF4444" />
                </TouchableOpacity>
              )}
            </View>
          </View>
        </LinearGradient>
      </TouchableOpacity>
    );
  };

  return (
    <View style={styles.container}>
      <TouchableOpacity
        style={[styles.createButton, { backgroundColor: Colors.primary }]}
        onPress={() => onOpenBuilder()}
      >
        <MaterialCommunityIcons name="plus" size={20} color="#000" />
        <Text style={styles.createButtonText}>Create Custom Gradient</Text>
      </TouchableOpacity>

      {isLoading ? (
        <View style={styles.centerBox}>
          <ActivityIndicator size="large" color={Colors.primary} />
        </View>
      ) : gradients.length === 0 ? (
        <View style={styles.centerBox}>
          <MaterialCommunityIcons name="palette-swatch" size={48} color="rgba(255,255,255,0.2)" />
          <Text style={[styles.emptyTitle, { color: Colors.text }]}>No Gradients Found</Text>
        </View>
      ) : (
        <ScrollView style={styles.listContainer} contentContainerStyle={{ paddingBottom: Spacing.xl * 2 }}>
          {gradients.map(renderGradientCard)}
        </ScrollView>
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
    alignItems: 'center'
  },
  emptyTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginTop: Spacing.md,
  },
  createButton: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    padding: Spacing.md,
    borderRadius: BorderRadius.md,
    marginBottom: Spacing.md,
  },
  createButtonText: {
    color: '#000',
    fontWeight: 'bold',
    fontSize: 16,
    marginLeft: Spacing.sm,
  },
  listContainer: {
    flex: 1,
  },
  cardContainer: {
    height: 70,
    borderRadius: BorderRadius.md,
    marginBottom: Spacing.sm,
    overflow: 'hidden',
  },
  cardGradient: {
    flex: 1,
  },
  cardOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.5)',
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: Spacing.md,
  },
  cardTitle: {
    color: '#FFF',
    fontSize: 16,
    fontWeight: 'bold',
    textShadowColor: 'rgba(0,0,0,0.8)',
    textShadowOffset: { width: 0, height: 1 },
    textShadowRadius: 3,
  },
  builtinBadge: {
    color: '#00F0FF',
    fontSize: 10,
    fontWeight: 'bold',
    marginTop: 2,
    letterSpacing: 1,
  },
  cardActions: {
    flexDirection: 'row',
    gap: Spacing.sm,
  },
  actionButton: {
    padding: Spacing.xs,
    backgroundColor: 'rgba(255,255,255,0.1)',
    borderRadius: BorderRadius.sm,
  }
});
