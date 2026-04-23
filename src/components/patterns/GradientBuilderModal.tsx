import React, { useState } from 'react';
import { Modal, StyleSheet, View, TouchableOpacity, Text, TextInput, SafeAreaView, ActivityIndicator } from 'react-native';
import { BlurView } from 'expo-blur';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import PositionalGradientBuilder from '../PositionalGradientBuilder';
import { useGradients } from '../../hooks/useGradients';
import { BuilderNode, CustomBuilderPreset } from '../../protocols/PositionalMathBuffer';
import { Spacing } from '../../theme/theme';

interface GradientBuilderModalProps {
  visible: boolean;
  onClose: () => void;
  preset?: CustomBuilderPreset; // If editing an existing preset
  nodes: BuilderNode[];
  onNodesChange: (nodes: BuilderNode[]) => void;
  fillMode: 'GRADIENT' | 'SOLID';
  onFillModeChange: (mode: 'GRADIENT' | 'SOLID') => void;
  transitionType: number;
  onTransitionTypeChange: (type: number) => void;
  direction: number;
  onDirectionChange: (dir: number) => void;
  speed: number;
  deviceLedCount: number;
  selectedColor: string;
  writeToDevice?: (payload: number[]) => Promise<void | boolean | 'partial'>;
  Colors: any;
}

export const GradientBuilderModal: React.FC<GradientBuilderModalProps> = ({
  visible,
  onClose,
  preset,
  Colors,
  ...builderProps
}) => {
  const { saveGradient } = useGradients();
  const [saveModalVisible, setSaveModalVisible] = useState(false);
  const [presetNameInput, setPresetNameInput] = useState(preset?.name || '');
  const [isSaving, setIsSaving] = useState(false);

  // Sync input name when preset changes
  React.useEffect(() => {
    if (visible) {
      setPresetNameInput(preset?.name || '');
    }
  }, [visible, preset]);

  const handleSavePreset = async () => {
    if (!presetNameInput.trim()) return;
    setIsSaving(true);
    
    // Protect built-ins from being overwritten by stripping their ID so it creates a new custom one
    const isBuiltin = preset?.id?.startsWith('builtin_');
    
    try {
      await saveGradient({
        id: isBuiltin ? undefined : preset?.id,
        name: presetNameInput.trim(),
        nodes: builderProps.nodes,
        fill_mode: builderProps.fillMode,
        transition_type: builderProps.transitionType
      });
      setSaveModalVisible(false);
      onClose();
    } catch (err) {
      console.error(err);
    } finally {
      setIsSaving(false);
    }
  };

  return (
    <Modal
      visible={visible}
      transparent={true}
      animationType="slide"
      onRequestClose={onClose}
    >
      <View style={styles.modalOverlay}>
        {/* Top 20% transparent peek-through for the LED strip preview */}
        <TouchableOpacity style={styles.peekThroughArea} onPress={onClose} activeOpacity={1} />
        
        {/* Bottom 80% workspace */}
        <View style={[styles.bottomSheet, { backgroundColor: isDark ? '#121212' : '#F5F5F5' }]}>
          {/* Header */}
          <View style={[styles.header, { borderBottomColor: Colors.border }]}>
            <TouchableOpacity onPress={onClose} style={styles.headerBtn}>
              <Text style={[styles.headerBtnText, { color: Colors.textMuted }]}>Cancel</Text>
            </TouchableOpacity>
            
            <Text style={[styles.title, { color: Colors.text }]}>
              {preset && !preset.id.startsWith('builtin_') ? 'Edit Gradient' : 'Create Gradient'}
            </Text>
            
            <TouchableOpacity onPress={() => setSaveModalVisible(true)} style={styles.headerBtn}>
              <Text style={[styles.headerBtnText, { color: Colors.primary }]}>Save</Text>
            </TouchableOpacity>
          </View>
          
          {/* Main Builder UI */}
          <View style={styles.builderContent}>
            <PositionalGradientBuilder {...builderProps} />
          </View>
        </View>
      </View>

      {/* SAVE MODAL */}
      <Modal visible={saveModalVisible} transparent animationType="fade">
          <View style={styles.saveModalOverlay}>
             <View style={[styles.saveModalCard, { backgroundColor: Colors.card, borderColor: Colors.border }]}>
                 <Text style={[styles.saveModalTitle, { color: Colors.text }]}>Save Custom Gradient</Text>
                 <Text style={[styles.saveModalDesc, { color: Colors.textMuted }]}>Give your sequence a name. It will be saved securely to the cloud if you are logged in, otherwise it will save locally to this device.</Text>
                 
                 <TextInput 
                    value={presetNameInput}
                    onChangeText={setPresetNameInput}
                    placeholder="e.g. Neon Fire Trail"
                    placeholderTextColor="rgba(255,255,255,0.3)"
                    style={styles.textInput}
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
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.5)',
    justifyContent: 'flex-end',
  },
  peekThroughArea: {
    flex: 0.2, // ~20% of screen height
    width: '100%',
  },
  bottomSheet: {
    flex: 0.8, // ~80% of screen height
    width: '100%',
    borderTopLeftRadius: 24,
    borderTopRightRadius: 24,
    overflow: 'hidden',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: -5 },
    shadowOpacity: 0.5,
    shadowRadius: 10,
    elevation: 20,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: Spacing.md,
    borderBottomWidth: 1,
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
  },
  closeBtn: {
    padding: Spacing.xs,
  },
  builderContent: {
    flex: 1,
  },
  headerBtn: {
    padding: Spacing.sm,
  },
  headerBtnText: {
    fontSize: 16,
    fontWeight: 'bold',
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
  saveBtn: {
    paddingHorizontal: Spacing.xl,
    paddingVertical: Spacing.md,
    borderRadius: 12,
    flexDirection: 'row',
    alignItems: 'center',
  }
});
