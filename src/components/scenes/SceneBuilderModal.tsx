import React, { useState } from 'react';
import { View, StyleSheet, Modal, TouchableOpacity, Text, ScrollView, SafeAreaView, TextInput } from 'react-native';
import { useSceneBuilder, SceneStep } from '../../hooks/useSceneBuilder';
import { useFavorites } from '../../hooks/useFavorites';
import FavoritePromptModal from '../docked/FavoritePromptModal';
import { useTheme } from '../../context/ThemeContext';
import { Spacing } from '../../theme/theme';
import { SceneStepCard } from './SceneStepCard';
import { SceneStepPicker } from './SceneStepPicker';
import { MaterialCommunityIcons } from '@expo/vector-icons';

interface SceneBuilderModalProps {
  visible: boolean;
  onClose: () => void;
  writeToDevice?: (payload: number[]) => Promise<void | boolean | 'partial'>;
}

export const SceneBuilderModal: React.FC<SceneBuilderModalProps> = ({ visible, onClose, writeToDevice }) => {
  const { Colors } = useTheme();
  const { steps, addStep, updateStep, removeStep, fireToSkates, saveScene, sceneName } = useSceneBuilder(writeToDevice);
  const { openFavoritePrompt, saveFavorite, promptState, promptName, setPromptName, closePrompt } = useFavorites();
  const [suggestionChips, setSuggestionChips] = useState<string[]>([]);
  const [pickerVisible, setPickerVisible] = useState(false);
  const [saveModalVisible, setSaveModalVisible] = useState(false);
  const [sceneNameInput, setSceneNameInput] = useState(sceneName || '');
  const [isSaving, setIsSaving] = useState(false);

  React.useEffect(() => {
    if (visible) {
      setSceneNameInput(sceneName || '');
    }
  }, [visible, sceneName]);

  const handleHeartClick = () => {
    setSuggestionChips(['Custom Scene', 'Action Sequence', 'Hyperspace']);
    openFavoritePrompt(undefined, sceneNameInput.trim() || 'Custom Scene');
  };

  const handleConfirmFavorite = () => {
    const name = promptName.trim() || 'Custom Scene';
    saveFavorite({
        mode: 'SCENE',
        speed: 50, // Scene steps have their own speeds
        brightness: 100, 
        sceneSteps: steps, // DockedController will need to know what to do with mode: SCENE
    }, name);
    closePrompt();
  };

  const handleSaveScene = async () => {
    if (!sceneNameInput.trim()) return;
    setIsSaving(true);
    try {
      await saveScene(sceneNameInput.trim(), false);
      setSaveModalVisible(false);
    } catch (e) {
      console.error(e);
    } finally {
      setIsSaving(false);
    }
  };

  const handleAddStep = (effectId: number) => {
    addStep({
      mode: 'pattern',
      effectId,
      fg: '#00FFFF',
      bg: '#000000',
      speed: 50,
      duration: 5,
      direction: 1,
    });
    setPickerVisible(false);
  };

  return (
    <Modal visible={visible} animationType="slide">
      <SafeAreaView style={[styles.container, { backgroundColor: Colors.background }]}>
        <View style={styles.header}>
          <Text style={[styles.title, { color: Colors.text }]}>SCENE BUILDER</Text>
          <View style={styles.headerRight}>
            <TouchableOpacity onPress={handleHeartClick} style={[styles.saveBtn, { marginRight: Spacing.lg }]}>
              <MaterialCommunityIcons name="heart-plus-outline" size={24} color={Colors.primary} />
            </TouchableOpacity>
            <TouchableOpacity style={styles.saveBtn} onPress={() => setSaveModalVisible(true)}>
              <Text style={{ color: '#00F0FF' }}>SAVE</Text>
            </TouchableOpacity>
            <TouchableOpacity onPress={onClose} style={styles.closeBtn}>
              <Text style={{ color: Colors.text }}>X</Text>
            </TouchableOpacity>
          </View>
        </View>

        <ScrollView contentContainerStyle={styles.scrollContent}>
          {steps.map((step, index) => (
            <SceneStepCard
              key={step.id}
              step={step}
              index={index}
              onUpdate={updateStep}
              onRemove={removeStep}
            />
          ))}

          {steps.length < 32 && (
            <TouchableOpacity
              style={[styles.addBtn, { borderColor: 'rgba(255,255,255,0.2)' }]}
              onPress={() => setPickerVisible(true)}
            >
              <Text style={{ color: Colors.text }}>+ ADD STEP</Text>
            </TouchableOpacity>
          )}
        </ScrollView>

        <View style={[styles.footer, { borderTopColor: 'rgba(255,255,255,0.1)' }]}>

          <TouchableOpacity 
            style={[styles.footerBtn, { backgroundColor: '#00F0FF' }]}
            onPress={fireToSkates}
          >
            <Text style={{ color: '#000', fontWeight: 'bold' }}>FIRE TO SKATES</Text>
          </TouchableOpacity>
        </View>
      </SafeAreaView>

      <SceneStepPicker
        visible={pickerVisible}
        onClose={() => setPickerVisible(false)}
        onSelect={handleAddStep}
      />

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

      <Modal visible={saveModalVisible} transparent animationType="fade">
        <View style={styles.saveModalOverlay}>
           <View style={[styles.saveModalCard, { backgroundColor: Colors.surface, borderColor: 'rgba(255,255,255,0.1)' }]}>
               <Text style={[styles.saveModalTitle, { color: Colors.text }]}>Save Custom Scene</Text>
               <Text style={[styles.saveModalDesc, { color: Colors.textMuted }]}>Give your scene a name. It will be saved securely to the cloud if you are logged in, otherwise it will save locally to this device.</Text>
               
               <TextInput 
                  value={sceneNameInput}
                  onChangeText={setSceneNameInput}
                  placeholder="e.g. Hyperspace Run"
                  placeholderTextColor="rgba(255,255,255,0.3)"
                  style={styles.textInput}
               />
               
               <View style={styles.saveModalActions}>
                   <TouchableOpacity onPress={() => !isSaving && setSaveModalVisible(false)} style={styles.actionBtn}>
                       <Text style={{ color: Colors.textMuted, fontWeight: 'bold' }}>CANCEL</Text>
                   </TouchableOpacity>
                   <TouchableOpacity 
                      onPress={handleSaveScene}
                      disabled={isSaving || !sceneNameInput.trim()}
                      style={[styles.saveBtnUI, { backgroundColor: Colors.primary, opacity: (!sceneNameInput.trim() || isSaving) ? 0.5 : 1 }]}
                   >
                       <Text style={{ color: '#000', fontWeight: 'bold' }}>SAVE</Text>
                   </TouchableOpacity>
               </View>
           </View>
        </View>
      </Modal>
    </Modal>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: Spacing.md,
    borderBottomWidth: 1,
    borderBottomColor: 'rgba(255,255,255,0.1)',
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
  },
  headerRight: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  saveBtn: {
    marginRight: Spacing.md,
  },
  closeBtn: {
    padding: Spacing.xs,
  },
  scrollContent: {
    padding: Spacing.md,
  },
  addBtn: {
    borderWidth: 1,
    borderStyle: 'dashed',
    borderRadius: 8,
    padding: Spacing.lg,
    alignItems: 'center',
    marginTop: Spacing.sm,
  },
  footer: {
    flexDirection: 'row',
    padding: Spacing.md,
    borderTopWidth: 1,
  },
  footerBtn: {
    flex: 1,
    padding: Spacing.md,
    borderRadius: 8,
    alignItems: 'center',
    marginHorizontal: Spacing.xs,
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
    fontSize: 14,
    marginBottom: Spacing.lg,
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
  saveBtnUI: {
    paddingHorizontal: Spacing.xl,
    paddingVertical: Spacing.md,
    borderRadius: 12,
    flexDirection: 'row',
    alignItems: 'center',
  }
});
