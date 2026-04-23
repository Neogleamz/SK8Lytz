/**
 * FavoritePromptModal.tsx — Save/Edit/Delete Favorite preset modal.
 *
 * Extracted from DockedController.tsx to reduce component size.
 */
import React from 'react';
import { Modal, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { Spacing } from '../../theme/theme';
import type { ThemePalette } from '../../theme/theme';

interface FavoritePromptModalProps {
  visible: boolean;
  Colors: ThemePalette;
  promptName: string;
  onChangePromptName: (name: string) => void;
  favPromptTargetId: string | null;
  onDelete: () => void;
  onCancel: () => void;
  onSave: () => void;
  suggestionChips?: string[];
}

export default function FavoritePromptModal({
  visible,
  Colors,
  promptName,
  onChangePromptName,
  favPromptTargetId,
  onDelete,
  onCancel,
  onSave,
  suggestionChips = []
}: FavoritePromptModalProps) {
  return (
    <Modal visible={visible} transparent animationType="fade">
      <View style={{ flex: 1, backgroundColor: 'rgba(0,0,0,0.85)', justifyContent: 'center', alignItems: 'center', padding: Spacing.xl }}>
        <View style={{ backgroundColor: Colors.surface, padding: Spacing.xl, borderRadius: 20, width: '100%', maxWidth: 340, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}>
          <Text style={{ color: '#FFF', fontSize: 18, fontWeight: 'bold', marginBottom: Spacing.md, textAlign: 'center' }}>
            {favPromptTargetId ? 'Edit Favorite' : 'Save Favorite'}
          </Text>
          <Text style={{ color: Colors.textMuted, fontSize: 14, marginBottom: Spacing.xl, textAlign: 'center' }}>
            {favPromptTargetId ? 'Rename your preset or delete it.' : 'Name your preset. Leave blank to use the default name.'}
          </Text>
          <TextInput
            style={{ backgroundColor: 'rgba(255,255,255,0.05)', color: '#FFF', padding: Spacing.md, borderRadius: 8, fontSize: 16, marginBottom: suggestionChips.length > 0 ? Spacing.sm : Spacing.xl, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}
            placeholder="Custom Preset Name..."
            placeholderTextColor="rgba(255,255,255,0.3)"
            value={promptName}
            onChangeText={onChangePromptName}
            autoFocus
          />
          {suggestionChips.length > 0 && (
            <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.sm, marginBottom: Spacing.xl, justifyContent: 'center' }}>
              {suggestionChips.map(chip => (
                <TouchableOpacity
                  key={chip}
                  onPress={() => onChangePromptName(chip)}
                  style={{
                    backgroundColor: promptName === chip ? Colors.primary : 'rgba(255,255,255,0.1)',
                    paddingHorizontal: Spacing.md,
                    paddingVertical: Spacing.sm,
                    borderRadius: 16,
                    borderWidth: 1,
                    borderColor: promptName === chip ? Colors.primary : 'rgba(255,255,255,0.2)'
                  }}
                >
                  <Text style={{ color: promptName === chip ? '#000' : '#FFF', fontSize: 12, fontWeight: promptName === chip ? 'bold' : 'normal' }}>{chip}</Text>
                </TouchableOpacity>
              ))}
            </View>
          )}
          <View style={{ flexDirection: 'row', gap: Spacing.md }}>
            {favPromptTargetId && (
              <TouchableOpacity style={{ flex: 1, padding: Spacing.lg, borderRadius: 10, backgroundColor: 'rgba(255,0,0,0.3)' }} onPress={onDelete}>
                <Text style={{ color: '#FFF', textAlign: 'center', fontWeight: 'bold' }}>Delete</Text>
              </TouchableOpacity>
            )}
            {(!favPromptTargetId) && (
              <TouchableOpacity style={{ flex: 1, padding: Spacing.lg, borderRadius: 10, backgroundColor: 'rgba(255,255,255,0.05)' }} onPress={onCancel}>
                <Text style={{ color: '#FFF', textAlign: 'center', fontWeight: 'bold' }}>Cancel</Text>
              </TouchableOpacity>
            )}
            <TouchableOpacity style={{ flex: 1, padding: Spacing.lg, borderRadius: 10, backgroundColor: Colors.primary }} onPress={onSave}>
              <Text style={{ color: '#000', textAlign: 'center', fontWeight: 'bold' }}>Save</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    </Modal>
  );
}
