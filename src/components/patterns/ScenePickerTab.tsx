import React from 'react';
import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Spacing } from '../../theme/theme';

interface ScenePickerTabProps {
  Colors: any;
  onOpenBuilder: () => void;
}

export const ScenePickerTab: React.FC<ScenePickerTabProps> = ({ Colors, onOpenBuilder }) => {
  return (
    <View style={styles.container}>
      <View style={styles.emptyState}>
        <MaterialCommunityIcons name="animation-play" size={48} color="rgba(255,255,255,0.2)" />
        <Text style={[styles.emptyTitle, { color: Colors.text }]}>No Scenes Yet</Text>
        <Text style={styles.emptyText}>
          Create a multi-step animated light show by combining patterns, colors, and durations.
        </Text>
        
        <TouchableOpacity
          style={[styles.createButton, { backgroundColor: Colors.primary }]}
          onPress={onOpenBuilder}
        >
          <MaterialCommunityIcons name="plus" size={20} color="#000" />
          <Text style={styles.createButtonText}>Create New Scene</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: Spacing.md,
    justifyContent: 'center',
  },
  emptyState: {
    alignItems: 'center',
    justifyContent: 'center',
    padding: Spacing.lg,
    backgroundColor: 'rgba(255,255,255,0.02)',
    borderRadius: 16,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.05)',
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
    paddingHorizontal: Spacing.lg,
    paddingVertical: Spacing.sm,
    borderRadius: 20,
    gap: Spacing.xs,
  },
  createButtonText: {
    color: '#000',
    fontSize: 14,
    fontWeight: '800',
  },
});
