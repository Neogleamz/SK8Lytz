import React from 'react';
import { View, StyleSheet, ScrollView, Modal, TouchableOpacity, Text } from 'react-native';
import { SK8LYTZ_TEMPLATES } from '../../protocols/PatternEngine';
import { Spacing } from '../../theme/theme';
import { useTheme } from '../../context/ThemeContext';
import { PatternCard } from '../patterns/PatternCard';

interface SceneStepPickerProps {
  visible: boolean;
  onClose: () => void;
  onSelect: (effectId: number) => void;
}

export const SceneStepPicker: React.FC<SceneStepPickerProps> = ({ visible, onClose, onSelect }) => {
  const { Colors } = useTheme();

  return (
    <Modal visible={visible} animationType="slide" transparent>
      <View style={styles.modalOverlay}>
        <View style={[styles.modalContent, { backgroundColor: Colors.surface }]}>
          <View style={styles.header}>
            <Text style={[styles.title, { color: Colors.text }]}>Select Pattern</Text>
            <TouchableOpacity onPress={onClose} style={styles.closeBtn}>
              <Text style={{ color: Colors.text }}>Close</Text>
            </TouchableOpacity>
          </View>
          <ScrollView
            contentContainerStyle={styles.grid}
            showsVerticalScrollIndicator={false}
          >
            {SK8LYTZ_TEMPLATES.map((pattern) => (
              <PatternCard
                key={pattern.id}
                effect={pattern}
                isSelected={false}
                fgColor="#00F0FF"
                bgColor="#000000"
                speed={50}
                direction={1}
                brightness={100}
                points={3}
                onSelect={() => onSelect(pattern.id)}
                Colors={Colors}
              />
            ))}
          </ScrollView>
        </View>
      </View>
    </Modal>
  );
};

const styles = StyleSheet.create({
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.8)',
    justifyContent: 'flex-end',
  },
  modalContent: {
    height: '80%',
    borderTopLeftRadius: 16,
    borderTopRightRadius: 16,
    paddingTop: Spacing.md,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: Spacing.md,
    paddingBottom: Spacing.md,
    borderBottomWidth: 1,
    borderBottomColor: 'rgba(255,255,255,0.1)',
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
  },
  closeBtn: {
    padding: Spacing.xs,
  },
  grid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
    padding: Spacing.sm,
    paddingBottom: Spacing.xxl,
  },
});

