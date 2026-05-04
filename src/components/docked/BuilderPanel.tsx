import React, { useState } from 'react';
import { View } from 'react-native';
import { GradientLibraryTab } from '../patterns/GradientLibraryTab';
import { GradientBuilderModal } from '../patterns/GradientBuilderModal';
import { BuilderNode, CustomBuilderPreset, PositionalMathBuffer } from '../../protocols/PositionalMathBuffer';
import { ZenggeProtocol } from '../../protocols/ZenggeProtocol';
import { useTheme } from '../../context/ThemeContext';

interface BuilderPanelProps {
  writeToDevice?: (payload: number[]) => Promise<void | boolean | 'partial'>;
  points?: number;
  speed: number;
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
}

export const BuilderPanel: React.FC<BuilderPanelProps> = ({
  writeToDevice, points = 16, speed, direction = 1,
  builderNodes, setBuilderNodes,
  builderFillMode, setBuilderFillMode,
  builderTransitionType, setBuilderTransitionType,
  builderDirection, setBuilderDirection,
  fgColor,
}) => {
  const { Colors } = useTheme();
  const [gradientModalVisible, setGradientModalVisible] = useState(false);
  const [editingPreset, setEditingPreset] = useState<CustomBuilderPreset | undefined>();

  const dispatchGradient = React.useCallback((preset: CustomBuilderPreset) => {
    if (!writeToDevice) return;
    const generatedRgbArray = PositionalMathBuffer.generateArray(preset.nodes, points, preset.fill_mode === 'GRADIENT');
    const mappedSpeed = Math.max(1, Math.min(31, Math.round((speed / 100) * 31)));
    const payload = ZenggeProtocol.setMultiColor(generatedRgbArray, points, mappedSpeed, direction, preset.transition_type);
    writeToDevice(payload);
  }, [writeToDevice, points, speed, direction]);

  const openGradientBuilder = (preset?: CustomBuilderPreset) => {
    if (preset) {
      setEditingPreset(preset);
      setBuilderNodes(preset.nodes);
      setBuilderFillMode(preset.fill_mode);
      setBuilderTransitionType(preset.transition_type);
    } else {
      setEditingPreset(undefined);
    }
    setGradientModalVisible(true);
  };

  return (
    <View style={{ flex: 1 }}>
      <GradientLibraryTab
        Colors={Colors}
        onOpenBuilder={openGradientBuilder}
        onApplyGradient={dispatchGradient}
      />
      <GradientBuilderModal
        visible={gradientModalVisible}
        onClose={() => setGradientModalVisible(false)}
        preset={editingPreset}
        nodes={builderNodes}
        onNodesChange={setBuilderNodes}
        fillMode={builderFillMode}
        onFillModeChange={setBuilderFillMode}
        transitionType={builderTransitionType}
        onTransitionTypeChange={setBuilderTransitionType}
        direction={builderDirection}
        onDirectionChange={setBuilderDirection}
        speed={speed}
        deviceLedCount={points}
        selectedColor={fgColor}
        writeToDevice={writeToDevice as any}
        Colors={Colors}
      />
    </View>
  );
};
