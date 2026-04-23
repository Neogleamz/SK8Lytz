import React from 'react';
import { ScrollView, StyleSheet, View } from 'react-native';
import { SK8LYTZ_TEMPLATES } from '../../constants/CustomEffects';
import { Spacing } from '../../theme/theme';
import { PatternCard } from './PatternCard';

interface PatternPickerTabProps {
  selectedEffectId: number;
  fgColor: string;
  bgColor: string;
  speed: number;
  points: number;
  onSelect: (id: number) => void;
  Colors: any;
}

export const PatternPickerTab: React.FC<PatternPickerTabProps> = ({
  selectedEffectId, fgColor, bgColor, speed, points, onSelect, Colors
}) => {
  return (
    <ScrollView
      style={{ flex: 1 }}
      contentContainerStyle={{
        flexDirection: 'row',
        flexWrap: 'wrap',
        justifyContent: 'space-between',
        paddingHorizontal: Spacing.xs,
        paddingBottom: Spacing.md,
      }}
      showsVerticalScrollIndicator={false}
      keyboardShouldPersistTaps="handled"
    >
      {SK8LYTZ_TEMPLATES.map((effect) => (
        <PatternCard
          key={effect.id}
          effect={effect}
          isSelected={selectedEffectId === effect.id}
          fgColor={fgColor}
          bgColor={bgColor}
          speed={speed}
          points={points}
          onSelect={() => onSelect(effect.id)}
          Colors={Colors}
        />
      ))}
    </ScrollView>
  );
};
