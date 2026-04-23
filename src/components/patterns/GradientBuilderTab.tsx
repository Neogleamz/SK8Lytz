import React from 'react';
import { View } from 'react-native';
import PositionalGradientBuilder from '../PositionalGradientBuilder';
import { BuilderNode } from '../../protocols/PositionalMathBuffer';

interface GradientBuilderTabProps {
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
}

export const GradientBuilderTab: React.FC<GradientBuilderTabProps> = (props) => {
  return (
    <View style={{ flex: 1 }}>
      <PositionalGradientBuilder {...props} />
    </View>
  );
};
