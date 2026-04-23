import React, { useState } from 'react';
import { View, StyleSheet, Modal, TouchableOpacity, Text, ScrollView, SafeAreaView } from 'react-native';
import { useSceneBuilder, SceneStep } from '../../hooks/useSceneBuilder';
import { useTheme } from '../../context/ThemeContext';
import { Spacing } from '../../theme/theme';
import { SceneStepCard } from './SceneStepCard';
import { SceneStepPicker } from './SceneStepPicker';

interface SceneBuilderModalProps {
  visible: boolean;
  onClose: () => void;
  writeToDevice?: (payload: number[]) => Promise<void | boolean | 'partial'>;
}

export const SceneBuilderModal: React.FC<SceneBuilderModalProps> = ({ visible, onClose, writeToDevice }) => {
  const { Colors } = useTheme();
  const { steps, addStep, updateStep, removeStep, fireToSkates } = useSceneBuilder(writeToDevice);
  const [pickerVisible, setPickerVisible] = useState(false);

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
            <TouchableOpacity style={styles.saveBtn}>
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
          <TouchableOpacity style={[styles.footerBtn, { backgroundColor: 'rgba(255,255,255,0.1)' }]}>
            <Text style={{ color: Colors.text }}>PREVIEW ALL</Text>
          </TouchableOpacity>
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
});
