import React, { useState } from 'react';
import { View, Text, TouchableOpacity, Modal, TextInput, ActivityIndicator, StyleSheet } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { GradientLibraryTab } from '../patterns/GradientLibraryTab';
import PositionalGradientBuilder from '../PositionalGradientBuilder';
import { BuilderNode, CustomBuilderPreset, PositionalMathBuffer } from '../../protocols/PositionalMathBuffer';
import { ZenggeProtocol } from '../../protocols/ZenggeProtocol';
import { useTheme } from '../../context/ThemeContext';
import { useGradients } from '../../hooks/useGradients';
import { useSharedFavorites } from '../../context/FavoritesContext';
import FavoritePromptModal from './FavoritePromptModal';
import { Spacing } from '../../theme/theme';
import { AppLogger } from '../../services/AppLogger';

interface BuilderPanelProps {
  points?: number;
  speed: number;
  brightness: number;
  direction?: number;
  builderNodes: BuilderNode[];
  setBuilderNodes: (nodes: BuilderNode[]) => void;
  builderFillMode: 'GRADIENT' | 'SOLID';
  setBuilderFillMode: (mode: 'GRADIENT' | 'SOLID') => void;
  builderTransitionType: number;
  setBuilderTransitionType: (type: number) => void;
  builderDirection: number;
  setBuilderDirection: (dir: number) => void;
  fgColor: string;
  writeToDevice?: (payload: number[]) => Promise<void | boolean | 'partial'>;
  onViewModeChange?: (mode: 'LIBRARY' | 'BUILDER') => void;
}

type ViewMode = 'LIBRARY' | 'BUILDER';

const brtFactor = (brt: number): number =>
  brt > 0 ? 0.10 + 0.90 * (brt / 100) : 0;

export const BuilderPanel: React.FC<BuilderPanelProps> = ({
  points = 16, speed, brightness, direction = 1,
  builderNodes, setBuilderNodes,
  builderFillMode, setBuilderFillMode,
  builderTransitionType, setBuilderTransitionType,
  builderDirection, setBuilderDirection,
  fgColor, writeToDevice, onViewModeChange
}) => {
  const { Colors, isDark } = useTheme();
  const { saveGradient } = useGradients();
  const { openFavoritePrompt, saveFavorite, promptState, promptName, setPromptName, closePrompt } = useSharedFavorites();

  const [viewMode, setViewMode] = useState<ViewMode>('LIBRARY');
  const [editingPreset, setEditingPreset] = useState<CustomBuilderPreset | undefined>();
  const [saveModalVisible, setSaveModalVisible] = useState(false);
  const [presetNameInput, setPresetNameInput] = useState('');
  const [isSaving, setIsSaving] = useState(false);
  const [suggestionChips, setSuggestionChips] = useState<string[]>([]);

  React.useEffect(() => {
    if (onViewModeChange) {
      onViewModeChange(viewMode);
    }
  }, [viewMode, onViewModeChange]);

  const dispatchGradient = React.useCallback((preset: CustomBuilderPreset) => {
    // Guard: 0x00 is an undefined hardware opcode — always fall back to 0x01 (Static)
    const safeTransition = preset.transition_type || 0x01;
    setBuilderNodes(preset.nodes);
    setBuilderFillMode(preset.fill_mode);
    setBuilderTransitionType(safeTransition);
    const factor = brtFactor(brightness);
    const generatedRgbArray = PositionalMathBuffer.generateArray(preset.nodes, points, preset.fill_mode === 'GRADIENT');
    const scaledRgbArray = generatedRgbArray.map(c => ({
      r: Math.round(c.r * factor),
      g: Math.round(c.g * factor),
      b: Math.round(c.b * factor),
    }));
    const mappedSpeed = Math.max(1, Math.min(100, Math.round(speed)));
    if (writeToDevice) writeToDevice(ZenggeProtocol.setMultiColor(scaledRgbArray, points, mappedSpeed, direction, safeTransition));
  }, [points, speed, brightness, direction, setBuilderNodes, setBuilderFillMode, setBuilderTransitionType, writeToDevice]);

  const openBuilder = (preset?: CustomBuilderPreset) => {
    if (preset) {
      setEditingPreset(preset);
      setBuilderNodes(preset.nodes);
      setBuilderFillMode(preset.fill_mode);
      setBuilderTransitionType(preset.transition_type || 0x01);
      setPresetNameInput(preset.name);
    } else {
      setEditingPreset(undefined);
      setPresetNameInput('');
    }
    setViewMode('BUILDER');
  };

  const handleSavePreset = async () => {
    if (!presetNameInput.trim()) return;
    setIsSaving(true);
    const isBuiltin = editingPreset?.id?.startsWith('builtin_');
    try {
      await saveGradient({
        id: isBuiltin ? undefined : editingPreset?.id,
        name: presetNameInput.trim(),
        nodes: builderNodes,
        fill_mode: builderFillMode,
        transition_type: builderTransitionType,
      });
      setSaveModalVisible(false);
      setViewMode('LIBRARY');
    } catch (err) {
      AppLogger.error('BUILDER_PANEL', { event: 'save_preset_failed', name: presetNameInput, error: String(err) });
    } finally {
      setIsSaving(false);
    }
  };

  const handleHeartClick = () => {
    setSuggestionChips(['Custom Wash', 'Neon Fade', 'Builder Vibe']);
    openFavoritePrompt(undefined, presetNameInput.trim() || 'Custom Gradient');
  };

  const handleConfirmFavorite = () => {
    const name = promptName.trim() || 'Custom Gradient';
    saveFavorite({
      mode: 'BUILDER',
      speed,
      brightness,
      builderNodes,
      builderFillMode,
      builderTransitionType,
      builderDirection,
    }, name);
    closePrompt();
  };

  // ── BUILDER VIEW — occupies exact same space as library, no modal overlay ──
  if (viewMode === 'BUILDER') {
    const isEditing = editingPreset && !editingPreset.id.startsWith('builtin_');
    return (
      <View style={{ flex: 1, backgroundColor: isDark ? '#0D0D18' : '#F5F5F5' }}>
        {/* Header bar matches app aesthetic */}
        <View style={[styles.builderHeader, { borderBottomColor: 'rgba(255,255,255,0.1)' }]}>
          <TouchableOpacity onPress={() => setViewMode('LIBRARY')} style={styles.headerBtn}>
            <MaterialCommunityIcons name="arrow-left" size={22} color={Colors.textMuted} />
          </TouchableOpacity>

          <Text style={[styles.builderTitle, { color: Colors.text }]}>
            {isEditing ? 'Edit Gradient' : 'Create Gradient'}
          </Text>

          <View style={{ flexDirection: 'row', alignItems: 'center' }}>
            <TouchableOpacity onPress={handleHeartClick} style={styles.headerBtn}>
              <MaterialCommunityIcons name="heart-plus-outline" size={22} color={Colors.primary} />
            </TouchableOpacity>
            <TouchableOpacity onPress={() => setSaveModalVisible(true)} style={styles.headerBtn}>
              <Text style={{ color: Colors.primary, fontWeight: 'bold', fontSize: 15 }}>Save</Text>
            </TouchableOpacity>
          </View>
        </View>

        {/* Builder UI fills the remaining space — no fixed height, pure flex */}
        <View style={{ flex: 1 }}>
          <PositionalGradientBuilder
            nodes={builderNodes}
            onNodesChange={setBuilderNodes}
            fillMode={builderFillMode}
            onFillModeChange={setBuilderFillMode}
            transitionType={builderTransitionType}
            onTransitionTypeChange={setBuilderTransitionType}
            direction={builderDirection}
            onDirectionChange={setBuilderDirection}
            speed={speed}
            brightness={brightness}
            deviceLedCount={points}
            selectedColor={fgColor}
            writeToDevice={writeToDevice}
          />
        </View>

        {/* Save Preset Modal — small card overlay only, not full-screen */}
        <Modal visible={saveModalVisible} transparent animationType="fade">
          <View style={styles.saveModalOverlay}>
            <View style={[styles.saveModalCard, { backgroundColor: Colors.surface, borderColor: 'rgba(255,255,255,0.12)' }]}>
              <Text style={[styles.saveModalTitle, { color: Colors.text }]}>Save Custom Gradient</Text>
              <Text style={[styles.saveModalDesc, { color: Colors.textMuted }]}>
                Give your sequence a name. Saved to cloud if logged in, otherwise stored locally.
              </Text>

              <TextInput
                value={presetNameInput}
                onChangeText={setPresetNameInput}
                placeholder="e.g. Neon Fire Trail"
                placeholderTextColor="rgba(255,255,255,0.3)"
                style={styles.textInput}
                autoFocus
              />

              <View style={styles.saveModalActions}>
                <TouchableOpacity onPress={() => !isSaving && setSaveModalVisible(false)} style={styles.actionBtn}>
                  <Text style={{ color: Colors.textMuted, fontWeight: 'bold' }}>CANCEL</Text>
                </TouchableOpacity>
                <TouchableOpacity
                  onPress={handleSavePreset}
                  disabled={isSaving || !presetNameInput.trim()}
                  style={[styles.saveBtn, { backgroundColor: Colors.primary, opacity: (!presetNameInput.trim() || isSaving) ? 0.5 : 1 }]}
                >
                  {isSaving
                    ? <ActivityIndicator size="small" color="#000" />
                    : <Text style={{ color: '#000', fontWeight: 'bold' }}>SAVE</Text>
                  }
                </TouchableOpacity>
              </View>
            </View>
          </View>
        </Modal>

        <FavoritePromptModal
          visible={promptState === 'NAMING_FAVORITE'}
          Colors={Colors}
          promptName={promptName}
          onChangePromptName={setPromptName}
          favPromptTargetId={null}
          suggestionChips={suggestionChips}
          onDelete={() => {}}
          onCancel={closePrompt}
          onSave={handleConfirmFavorite}
        />
      </View>
    );
  }

  // ── LIBRARY VIEW (default) ──
  return (
    <GradientLibraryTab
      Colors={Colors}
      onOpenBuilder={openBuilder}
      onApplyGradient={dispatchGradient}
    />
  );
};

const styles = StyleSheet.create({
  builderHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: Spacing.md,
    paddingVertical: Spacing.sm,
    borderBottomWidth: 1,
  },
  builderTitle: {
    fontSize: 17,
    fontWeight: 'bold',
  },
  headerBtn: {
    padding: Spacing.xs,
  },
  saveModalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.8)',
    justifyContent: 'center',
    alignItems: 'center',
    padding: Spacing.xl,
  },
  saveModalCard: {
    width: '100%',
    maxWidth: 340,
    borderRadius: 16,
    padding: Spacing.xl,
    borderWidth: 1,
  },
  saveModalTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: Spacing.md,
  },
  saveModalDesc: {
    fontSize: 13,
    marginBottom: Spacing.lg,
    lineHeight: 18,
  },
  textInput: {
    backgroundColor: 'rgba(0,0,0,0.2)',
    color: '#FFF',
    padding: Spacing.md,
    borderRadius: 12,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
    marginBottom: Spacing.xl,
  },
  saveModalActions: {
    flexDirection: 'row',
    gap: Spacing.md,
    justifyContent: 'flex-end',
  },
  actionBtn: {
    padding: Spacing.md,
  },
  saveBtn: {
    paddingHorizontal: Spacing.xl,
    paddingVertical: Spacing.md,
    borderRadius: 12,
    flexDirection: 'row',
    alignItems: 'center',
  },
});
