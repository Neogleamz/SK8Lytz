import React from 'react';
import { View, StyleSheet, Text, TouchableOpacity } from 'react-native';
import { SceneStep } from '../../hooks/useSceneBuilder';
import { useTheme } from '../../context/ThemeContext';
import { Spacing } from '../../theme/theme';
import { LEDStripPreview } from '../LEDStripPreview';
import { SK8LYTZ_TEMPLATES } from '../../constants/CustomEffects';
import Slider from '@react-native-community/slider';

interface SceneStepCardProps {
  step: SceneStep;
  index: number;
  onUpdate: (id: string, updates: Partial<SceneStep>) => void;
  onRemove: (id: string) => void;
}

export const SceneStepCard: React.FC<SceneStepCardProps> = ({ step, index, onUpdate, onRemove }) => {
  const { Colors } = useTheme();
  const pattern = SK8LYTZ_TEMPLATES.find(p => p.id === step.effectId);

  return (
    <View style={[styles.card, { backgroundColor: Colors.surface, borderColor: 'rgba(255,255,255,0.1)' }]}>
      <View style={styles.header}>
        <Text style={[styles.title, { color: Colors.text }]}>Step {index + 1}</Text>
        <TouchableOpacity onPress={() => onRemove(step.id)}>
          <Text style={{ color: Colors.error }}>Remove</Text>
        </TouchableOpacity>
      </View>
      
      <LEDStripPreview
        patternId={step.effectId}
        fg={step.fg}
        bg={step.bg}
        numLEDs={16}
        speed={step.speed}
        direction={step.direction}
      />

      <View style={styles.controlsRow}>
        <Text style={[styles.patternName, { color: Colors.text }]}>{pattern?.name || 'Unknown Pattern'}</Text>
        <View style={styles.colorRow}>
          <Text style={{ color: Colors.textSecondary, marginRight: 4 }}>FG:</Text>
          <View style={[styles.colorBox, { backgroundColor: step.fg }]} />
          <Text style={{ color: Colors.textSecondary, marginLeft: 8, marginRight: 4 }}>BG:</Text>
          <View style={[styles.colorBox, { backgroundColor: step.bg }]} />
        </View>
      </View>

      <View style={styles.sliderRow}>
        <Text style={{ color: Colors.textSecondary, width: 40 }}>⏱ {step.duration}s</Text>
        <Slider
          style={{ flex: 1 }}
          minimumValue={1}
          maximumValue={60}
          step={1}
          value={step.duration}
          onValueChange={(val) => onUpdate(step.id, { duration: val })}
          minimumTrackTintColor="#00F0FF"
          maximumTrackTintColor="rgba(255,255,255,0.2)"
          thumbTintColor="#00F0FF"
        />
      </View>

    </View>
  );
};

const styles = StyleSheet.create({
  card: {
    borderRadius: 8,
    borderWidth: 1,
    padding: Spacing.sm,
    marginBottom: Spacing.md,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: Spacing.sm,
  },
  title: {
    fontWeight: 'bold',
  },
  controlsRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginTop: Spacing.sm,
  },
  patternName: {
    fontSize: 14,
    flex: 1,
  },
  colorRow: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  colorBox: {
    width: 16,
    height: 16,
    borderRadius: 8,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.2)',
  },
  sliderRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: Spacing.sm,
  },
});
