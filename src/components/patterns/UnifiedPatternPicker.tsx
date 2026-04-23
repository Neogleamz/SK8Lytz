import React, { useCallback, useEffect, useState } from 'react';
import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { SK8LYTZ_TEMPLATES } from '../../protocols/PatternEngine';
import { BuilderNode } from '../../protocols/PositionalMathBuffer';
import { buildPatternPayload } from '../../protocols/PatternEngine';
import { useTheme } from '../../context/ThemeContext';
import { Spacing } from '../../theme/theme';
import { hexToRgb } from '../../utils/ColorUtils';
import { GradientBuilderTab } from './GradientBuilderTab';
import { PatternPickerTab } from './PatternPickerTab';
import { ScenePickerTab } from './ScenePickerTab';
import { SceneBuilderModal } from '../scenes/SceneBuilderModal';

interface UnifiedPatternPickerProps {
  writeToDevice?: (payload: number[]) => Promise<void | boolean | 'partial'>;
  points?: number;
  segments?: number;
  speed: number;
  brightness?: number;
  direction?: number;
  hwSettings?: any;
  /** FG color — owned by DockedController via fixedFgColor. Do NOT shadow with local state. */
  fgColor: string;
  /** BG color — owned by DockedController via fixedBgColor. Do NOT shadow with local state. */
  bgColor: string;
  onStateChange?: (id: number) => void;
}

export const UnifiedPatternPicker: React.FC<UnifiedPatternPickerProps> = ({
  writeToDevice, points = 16, segments = 1, speed, brightness = 100, direction = 1,
  hwSettings, onStateChange, fgColor, bgColor,
}) => {
  const { Colors } = useTheme();
  
  const [activeTab, setActiveTab] = useState<'PATTERNS' | 'BUILDER' | 'SCENES'>('PATTERNS');
  const [sceneBuilderVisible, setSceneBuilderVisible] = useState(false);

  // Shared state for PATTERNS (fgColor/bgColor are PROPS — owned by DockedController)
  const [selectedEffectId, setSelectedEffectId] = useState<number>(1);
  
  // Shared state for BUILDER
  const [builderNodes, setBuilderNodes] = useState<BuilderNode[]>([
    { id: '1', position: 0, colorHex: '#FF0000' },
    { id: '2', position: 100, colorHex: '#0000FF' }
  ]);
  const [builderFillMode, setBuilderFillMode] = useState<'GRADIENT' | 'SOLID'>('GRADIENT');
  const [builderTransitionType, setBuilderTransitionType] = useState<number>(0);
  const [builderDirection, setBuilderDirection] = useState<number>(1);

  const devicePoints = hwSettings?.ledPoints || points || 16;

  // Dispatch logic for PATTERNS tab — uses PatternEngine → 0x59 pipeline.
  // buildPatternPayload() generates our math-synthesized pixel arrays and
  // wraps them in the correct 0x59 opcode with transition type (FREEZE/CASCADE).
  // This is the ONLY correct dispatch path. Do NOT use 0x51 setCustomModeCompact
  // here — that sends firmware symphony effect IDs, not our pixel math.
  const dispatchEffect = useCallback((effectId: number, fg: string, bg: string, spd: number, dir: number, brt: number) => {
    if (!writeToDevice) return;
    const fgRgb = hexToRgb(fg);
    const bgRgb = hexToRgb(bg);
    const payload = buildPatternPayload(
      effectId, fgRgb, bgRgb, devicePoints,
      Math.max(1, Math.min(100, Math.round(spd))), dir, brt
    );
    if (payload) writeToDevice(payload);
    onStateChange?.(effectId);
  }, [writeToDevice, onStateChange, devicePoints]);

  const handleSelectPattern = useCallback((effectId: number) => {
    setSelectedEffectId(effectId);
    dispatchEffect(effectId, fgColor, bgColor, speed, direction, brightness);
  }, [dispatchEffect, fgColor, bgColor, speed, direction, brightness]);

  // Sync speed/color changes to PATTERNS tab hardware.
  // NOTE: dispatchEffect is intentionally omitted from deps — it is stable via useCallback
  // ([writeToDevice, onStateChange]). Including it causes an infinite loop because
  // DockedController passes onStateChange as an inline arrow (new ref every render),
  // which recreates dispatchEffect, which re-fires this effect, which calls writeToDevice,
  // which calls setLastSentPayload in DockedController, which re-renders, which ... loops.
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(() => {
    if (activeTab === 'PATTERNS' && selectedEffectId) {
      dispatchEffect(selectedEffectId, fgColor, bgColor, speed, direction, brightness);
    }
  }, [speed, brightness, direction, activeTab, selectedEffectId, fgColor, bgColor]);


  // UI helpers
  const renderTabButton = (tab: 'PATTERNS' | 'BUILDER' | 'SCENES', label: string) => {
    const isActive = activeTab === tab;
    return (
      <TouchableOpacity
        style={[styles.tabButton, isActive && { backgroundColor: 'rgba(0,240,255,0.1)', borderColor: '#00F0FF' }]}
        onPress={() => setActiveTab(tab)}
      >
        <Text style={[styles.tabText, isActive && { color: '#00F0FF' }]}>{label}</Text>
      </TouchableOpacity>
    );
  };

  return (
    <View style={{ flex: 1 }}>
      {/* Tab Navigation */}
      <View style={styles.tabContainer}>
        {renderTabButton('PATTERNS', 'PATTERNS')}
        {renderTabButton('BUILDER', 'BUILDER')}
        {renderTabButton('SCENES', 'SCENES')}
      </View>

      {/* Tab Content */}
      <View style={{ flex: 1 }}>
        {activeTab === 'PATTERNS' && (
          <PatternPickerTab
            selectedEffectId={selectedEffectId}
            fgColor={fgColor}
            bgColor={bgColor}
            speed={speed}
            direction={direction}
            points={devicePoints}
            onSelect={handleSelectPattern}
            Colors={Colors}
          />
        )}
        {activeTab === 'BUILDER' && (
          <GradientBuilderTab
            nodes={builderNodes}
            onNodesChange={setBuilderNodes}
            fillMode={builderFillMode}
            onFillModeChange={setBuilderFillMode}
            transitionType={builderTransitionType}
            onTransitionTypeChange={setBuilderTransitionType}
            direction={builderDirection}
            onDirectionChange={setBuilderDirection}
            speed={speed}
            deviceLedCount={devicePoints}
            selectedColor={fgColor}
            writeToDevice={writeToDevice}
          />
        )}
        {activeTab === 'SCENES' && (
          <ScenePickerTab
            Colors={Colors}
            onOpenBuilder={() => setSceneBuilderVisible(true)}
          />
        )}
      </View>

      <SceneBuilderModal
        visible={sceneBuilderVisible}
        onClose={() => setSceneBuilderVisible(false)}
        writeToDevice={writeToDevice}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  tabContainer: {
    flexDirection: 'row',
    paddingHorizontal: Spacing.xs,
    marginBottom: Spacing.md,
    gap: Spacing.xs,
  },
  tabButton: {
    flex: 1,
    paddingVertical: Spacing.sm,
    alignItems: 'center',
    borderRadius: 8,
    borderWidth: 1,
    borderColor: 'rgba(255,255,255,0.1)',
    backgroundColor: 'rgba(255,255,255,0.02)',
  },
  tabText: {
    color: 'rgba(255,255,255,0.6)',
    fontSize: 11,
    fontWeight: '800',
    letterSpacing: 1,
  },
});

