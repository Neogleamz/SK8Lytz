import React, { useEffect } from 'react';
import { StyleSheet, Text, TouchableOpacity, View, ScrollView, ActivityIndicator, Alert } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Spacing } from '../../theme/theme';
import { useScenes } from '../../hooks/useScenes';
import { Scene } from '../../services/ScenesService';

interface ScenePickerTabProps {
  Colors: any;
  onOpenBuilder: () => void;
}

export const ScenePickerTab: React.FC<ScenePickerTabProps> = ({ Colors, onOpenBuilder }) => {
  const { localScenes, isLoading, deleteScene, loadScenes } = useScenes();

  // Reload when tab mounts
  useEffect(() => {
    loadScenes();
  }, [loadScenes]);

  const handleDelete = (scene: Scene) => {
    Alert.alert(
      'Delete Scene',
      `Are you sure you want to delete "${scene.name}"?`,
      [
        { text: 'Cancel', style: 'cancel' },
        { text: 'Delete', style: 'destructive', onPress: () => deleteScene(scene.id) }
      ]
    );
  };

  const renderSceneCard = (scene: Scene) => {
    return (
      <View key={scene.id} style={styles.cardContainer}>
        <View style={styles.cardContent}>
          <Text style={[styles.cardTitle, { color: Colors.text }]}>{scene.name}</Text>
          <Text style={[styles.cardSub, { color: Colors.textMuted }]}>{scene.steps.length} Steps</Text>
        </View>
        <View style={styles.cardActions}>
          <TouchableOpacity onPress={() => handleDelete(scene)} style={styles.actionButton}>
            <MaterialCommunityIcons name="delete" size={20} color="#FF4444" />
          </TouchableOpacity>
        </View>
      </View>
    );
  };

  return (
    <View style={styles.container}>
      <TouchableOpacity
        style={[styles.createButton, { backgroundColor: Colors.primary }]}
        onPress={onOpenBuilder}
      >
        <MaterialCommunityIcons name="plus" size={20} color="#000" />
        <Text style={styles.createButtonText}>Create New Scene</Text>
      </TouchableOpacity>

      {isLoading ? (
        <View style={styles.centerBox}>
          <ActivityIndicator size="large" color={Colors.primary} />
        </View>
      ) : localScenes.length === 0 ? (
        <View style={styles.emptyState}>
          <MaterialCommunityIcons name="animation-play" size={48} color="rgba(255,255,255,0.2)" />
          <Text style={[styles.emptyTitle, { color: Colors.text }]}>No Scenes Yet</Text>
          <Text style={styles.emptyText}>
            Create a multi-step animated light show by combining patterns, colors, and durations.
          </Text>
        </View>
      ) : (
        <ScrollView style={styles.listContainer} contentContainerStyle={{ paddingBottom: Spacing.xl * 2 }}>
          {localScenes.map(renderSceneCard)}
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
  emptyState: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: Spacing.lg,
  },
  emptyTitle: {
    fontSize: 18,
    fontWeight: '800',
    marginTop: Spacing.md,
    marginBottom: Spacing.xs,
  },
  emptyText: {
    color: 'rgba(255,255,255,0.5)',
    fontSize: 13,
    textAlign: 'center',
    lineHeight: 20,
    marginBottom: Spacing.xl,
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
    fontSize: 16,
    marginLeft: Spacing.sm,
  },
  listContainer: {
    flex: 1,
  },
  cardContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: Spacing.md,
    borderRadius: 12,
    marginBottom: Spacing.sm,
    backgroundColor: 'rgba(255,255,255,0.05)',
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
  },
  cardContent: {
    flex: 1,
  },
  cardTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    marginBottom: 4,
  },
  cardSub: {
    fontSize: 12,
  },
  cardActions: {
    flexDirection: 'row',
    gap: Spacing.sm,
  },
  actionButton: {
    padding: Spacing.xs,
    backgroundColor: 'rgba(255,255,255,0.1)',
    borderRadius: 8,
  }
});
