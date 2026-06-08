/**
 * QuickPresetModal.tsx — SK8Lytz Cloud & Quick Preset Save Modal
 *
 * Owns: NAMING_PRESET prompt modal including cloud publish, private save,
 * and quick preset rename/delete flows.
 * Extracted from DockedController.tsx (Hollow Shell v3 — Meal 4).
 *
 * Zero BLE coupling. Receives captureEntireState as a stable callback prop.
 * Platform: React Native (Android + Web)
 */
import React from 'react';
import { Alert, Modal, StyleSheet, Text, TextInput, TouchableOpacity, View } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { STORAGE_PREFIX } from '../../constants/AppConstants';
import { AppLogger } from '../../services/AppLogger';
import { containsProfanity } from '../../services/AuthUtils';
import { ScenesService, Scene } from '../../services/ScenesService';
import { useAuth } from '../../context/AuthContext';
import { Spacing } from '../../theme/theme';
import type { ThemePalette } from '../../theme/theme';

export interface QuickPreset {
  name: string;
  colors: string[];
  type: number;
}

interface QuickPresetModalProps {
  isOfflineMode?: boolean;
  visible: boolean;
  promptName: string;
  setPromptName: (n: string) => void;
  /** -1 = creating new, ≥0 = editing existing index */
  quickPromptTargetIndex: number;
  quickPresets: QuickPreset[];
  setQuickPresets: (p: QuickPreset[]) => void;
  cloudPublicToggle: boolean;
  setCloudPublicToggle: (v: boolean | ((p: boolean) => boolean)) => void;
  isPublishingCloud: boolean;
  setIsPublishingCloud: (v: boolean) => void;
  /** Returns full scene snapshot for cloud publish. Stable callback from captureEntireState. */
  captureEntireState: () => Record<string, unknown>;
  multiColors: string[];
  multiTransition: number;
  closePrompt: () => void;
  Colors: ThemePalette;
}

const QuickPresetModal = React.memo(function QuickPresetModal({
  isOfflineMode = false,
  visible,
  promptName,
  setPromptName,
  quickPromptTargetIndex,
  quickPresets,
  setQuickPresets,
  cloudPublicToggle,
  setCloudPublicToggle,
  isPublishingCloud,
  setIsPublishingCloud,
  captureEntireState,
  multiColors,
  multiTransition,
  closePrompt,
  Colors,
}: QuickPresetModalProps) {
  const styles = React.useMemo(() => createStyles(Colors), [Colors]);
  const { user } = useAuth();

  const handleDelete = () => {
    const newArr = [...quickPresets];
    newArr.splice(quickPromptTargetIndex, 1);
    setQuickPresets(newArr);
    AsyncStorage.setItem(`${STORAGE_PREFIX}QuickPresets`, JSON.stringify(newArr));
    AppLogger.log('BUILDER_PRESET_DELETED', { index: quickPromptTargetIndex });
    closePrompt();
  };

  const handleSave = () => {
    const newArr = [...quickPresets];
    const safeName = promptName.trim() || 'Preset';
    if (quickPromptTargetIndex === -1) {
      newArr.push({ name: safeName, colors: multiColors, type: multiTransition });
    } else {
      newArr[quickPromptTargetIndex].name = safeName;
    }
    setQuickPresets(newArr);
    AsyncStorage.setItem(`${STORAGE_PREFIX}QuickPresets`, JSON.stringify(newArr));
    AppLogger.log('BUILDER_PRESET_SAVED', {
      name: safeName,
      isOverwrite: quickPromptTargetIndex !== -1,
    });
    closePrompt();
  };

  const handlePublish = async () => {
    setIsPublishingCloud(true);
    const safeName = promptName.trim() || 'Cloud Scene';
    if (containsProfanity(safeName)) {
      Alert.alert(
        'Invalid Name',
        'Scene names cannot contain inappropriate language. Please choose a different name.'
      );
      setIsPublishingCloud(false);
      return;
    }
    const success = await ScenesService.publishScene(
      safeName,
      captureEntireState(),
      cloudPublicToggle,
      user?.id,
      user?.user_metadata?.username
    );
    if (success) {
      Alert.alert(
        cloudPublicToggle ? 'Published!' : 'Saved!',
        cloudPublicToggle
          ? 'Your scene is now available to the community.'
          : 'Scene saved privately to your cloud.'
      );
      closePrompt();
    } else {
      Alert.alert('Error', 'Could not save scene. Are you logged in?');
    }
    setIsPublishingCloud(false);
  };

  return (
    <Modal visible={visible} transparent animationType="fade">
      <View style={styles.overlay}>
        <View style={styles.card}>
          <Text style={styles.title}>
            {quickPromptTargetIndex === -1 ? 'Save Quick Preset' : 'Edit Quick Preset'}
          </Text>
          <Text style={styles.subtitle}>
            {quickPromptTargetIndex === -1
              ? 'Name your new preset to store it in the Quick bar.'
              : 'Rename your preset or delete it from the bar.'}
          </Text>

          <TextInput
            style={styles.input}
            placeholder="Preset Name..."
            placeholderTextColor="rgba(255,255,255,0.3)"
            value={promptName}
            onChangeText={setPromptName}
            autoFocus
          />

          {/* Cloud visibility toggle — only shown when creating AND not offline */}
          {quickPromptTargetIndex === -1 && !isOfflineMode && (
            <TouchableOpacity
              onPress={() => setCloudPublicToggle(p => !p)}
              style={[
                styles.visibilityRow,
                { borderColor: cloudPublicToggle ? '#00C853' : 'rgba(255,255,255,0.1)' },
              ]}
            >
              <MaterialCommunityIcons
                name={cloudPublicToggle ? 'earth' : 'lock-outline'}
                size={18}
                color={cloudPublicToggle ? '#00C853' : Colors.textMuted}
                style={{ marginRight: Spacing.md }}
              />
              <View style={{ flex: 1 }}>
                <Text style={styles.visibilityTitle}>
                  {cloudPublicToggle
                    ? 'Public — visible to community'
                    : 'Private — only you can see it'}
                </Text>
                <Text style={styles.visibilityHint}>Tap to toggle visibility</Text>
              </View>
            </TouchableOpacity>
          )}

          <View style={{ flexDirection: 'row', gap: Spacing.md }}>
            {/* Delete button — editing mode only */}
            {quickPromptTargetIndex !== -1 && (
              <TouchableOpacity style={styles.btnDelete} onPress={handleDelete}>
                <Text style={styles.btnTextWhite}>Delete</Text>
              </TouchableOpacity>
            )}

            {/* Cancel — new preset mode only */}
            {quickPromptTargetIndex === -1 && (
              <TouchableOpacity style={styles.btnCancel} onPress={closePrompt}>
                <Text style={styles.btnTextWhite}>Cancel</Text>
              </TouchableOpacity>
            )}

            {/* Cloud Publish - gated if offline */}
            {!isOfflineMode && (
              <TouchableOpacity
                style={styles.btnPublish}
                disabled={isPublishingCloud}
                onPress={handlePublish}
              >
                <Text style={styles.btnTextBlack}>
                  {isPublishingCloud
                    ? 'Saving...'
                    : cloudPublicToggle
                    ? '🌍 Publish'
                    : '🔒 Save Private'}
                </Text>
              </TouchableOpacity>
            )}

            {/* Local Save */}
            <TouchableOpacity style={[styles.btnSave, { backgroundColor: Colors.primary }]} onPress={handleSave}>
              <Text style={styles.btnTextBlack}>Save</Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    </Modal>
  );
});

export default QuickPresetModal;

const createStyles = (Colors: ThemePalette) =>
  StyleSheet.create({
    overlay: {
      flex: 1,
      backgroundColor: 'rgba(0,0,0,0.85)',
      justifyContent: 'center',
      alignItems: 'center',
      padding: Spacing.xl,
    },
    card: {
      backgroundColor: Colors.surface,
      padding: Spacing.xl,
      borderRadius: 20,
      width: '100%',
      maxWidth: 340,
      borderWidth: 1,
      borderColor: 'rgba(255,255,255,0.1)',
    },
    title: {
      color: '#FFF',
      fontSize: 18,
      fontWeight: 'bold',
      marginBottom: Spacing.md,
      textAlign: 'center',
    },
    subtitle: {
      color: Colors.textMuted,
      fontSize: 14,
      marginBottom: Spacing.xl,
      textAlign: 'center',
    },
    input: {
      backgroundColor: 'rgba(255,255,255,0.05)',
      color: '#FFF',
      padding: Spacing.md,
      borderRadius: 8,
      fontSize: 16,
      marginBottom: Spacing.lg,
      borderWidth: 1,
      borderColor: 'rgba(255,255,255,0.1)',
    },
    visibilityRow: {
      flexDirection: 'row',
      alignItems: 'center',
      backgroundColor: 'rgba(255,255,255,0.05)',
      borderRadius: 10,
      padding: Spacing.md,
      marginBottom: Spacing.xl,
      borderWidth: 1,
    },
    visibilityTitle: {
      color: '#FFF',
      fontWeight: 'bold',
      fontSize: 13,
    },
    visibilityHint: {
      color: Colors.textMuted,
      fontSize: 11,
      marginTop: Spacing.xxs,
    },
    btnDelete: {
      flex: 1,
      padding: Spacing.lg,
      borderRadius: 10,
      backgroundColor: 'rgba(255,0,0,0.3)',
    },
    btnCancel: {
      flex: 1,
      padding: Spacing.lg,
      borderRadius: 10,
      backgroundColor: 'rgba(255,255,255,0.05)',
    },
    btnPublish: {
      flex: 1,
      padding: Spacing.lg,
      borderRadius: 10,
      backgroundColor: '#00C853',
    },
    btnSave: {
      flex: 1,
      padding: Spacing.lg,
      borderRadius: 10,
    },
    btnTextWhite: {
      color: '#FFF',
      textAlign: 'center',
      fontWeight: 'bold',
    },
    btnTextBlack: {
      color: '#000',
      textAlign: 'center',
      fontWeight: 'bold',
    },
  });
