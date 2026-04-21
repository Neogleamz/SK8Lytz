/**
 * MultiModePanel.tsx — Pro Effects & Positional Builder panel.
 *
 * Renders when activeMode === 'MULTIMODE'. Contains:
 *  - Segmented toggle: Pro Effects | Builder
 *  - ZENGGE_EFFECTS grid (scrollable) for the PATTERN sub-mode
 *  - PositionalGradientBuilder for the BUILDER sub-mode
 *
 * Extracted from DockedController.tsx (Phase 4 — decompose-docked-controller-jsx).
 */
import React from 'react';
import { ScrollView, Text, TouchableOpacity, View } from 'react-native';
import { SK8LYTZ_TEMPLATES } from '../../constants/CustomEffects';
import { AppLogger } from '../../services/AppLogger';
import { Layout, Spacing } from '../../theme/theme';
import { ZenggeProtocol } from '../../protocols/ZenggeProtocol';
import CustomEffectVisualizer from '../CustomEffectVisualizer';
import PositionalGradientBuilder from '../PositionalGradientBuilder';

type FixedSubMode = 'PATTERN' | 'BUILDER';

interface MultiModePanelProps {
  fixedSubMode: FixedSubMode;
  setFixedSubMode: (mode: FixedSubMode) => void;
  fixedPatternId: number;
  setFixedPatternId: (id: number) => void;
  fixedFgColor: string;
  setFixedFgColor: (color: string) => void;
  fixedBgColor: string;
  setFixedBgColor: (color: string) => void;
  builderNodes: any[];
  setBuilderNodes: (nodes: any[]) => void;
  builderFillMode: any;
  setBuilderFillMode: (mode: any) => void;
  builderTransitionType: any;
  setBuilderTransitionType: (type: any) => void;
  builderDirection: any;
  setBuilderDirection: (dir: any) => void;
  speed: number;
  hwSettings?: any;
  points?: number;
  devices?: any[];
  selectedColor: string;
  writeToDevice?: (payload: number[]) => Promise<void | boolean | 'partial'>;
  applyFixedPattern?: (patternId: number, fg: string, bg: string, spd?: number, brt?: number) => void;
  Colors: any;
}

const MultiModePanel = React.memo(({
  fixedSubMode,
  setFixedSubMode,
  fixedPatternId,
  setFixedPatternId,
  fixedFgColor,
  fixedBgColor,
  builderNodes,
  setBuilderNodes,
  builderFillMode,
  setBuilderFillMode,
  builderTransitionType,
  setBuilderTransitionType,
  builderDirection,
  setBuilderDirection,
  speed,
  hwSettings,
  points,
  devices,
  selectedColor,
  writeToDevice,
  applyFixedPattern,
  Colors,
}: MultiModePanelProps) => {
  const hexToRgb = (hex: string) => {
    const h = hex || '#000000';
    return {
      r: parseInt(h.substring(1, 3), 16) || 0,
      g: parseInt(h.substring(3, 5), 16) || 0,
      b: parseInt(h.substring(5, 7), 16) || 0,
    };
  };

  return (
    <View style={{ flex: 1, marginBottom: Spacing.sm, justifyContent: 'flex-start' }}>

      {/* UNIFIED PRO EFFECTS & POSITIONAL BUILDER */}
      {(fixedSubMode === 'PATTERN' || fixedSubMode === 'BUILDER') && (
        <View style={{ flex: 1, width: '100%', marginBottom: Spacing.xs }}>

          {/* UNIFIED TOGGLE */}
          <View style={{ flexDirection: 'row', marginBottom: Spacing.sm, marginTop: Spacing.xxs, flexShrink: 0, minHeight: 36 }}>
            <TouchableOpacity
              onPress={() => { setFixedSubMode('PATTERN'); }}
              style={{ flex: 1, paddingVertical: Spacing.sm, alignItems: 'center', backgroundColor: fixedSubMode === 'PATTERN' ? Colors.primary : Colors.surfaceHighlight, borderTopLeftRadius: Layout.borderRadius, borderBottomLeftRadius: Layout.borderRadius }}
            >
              <Text style={{ color: fixedSubMode === 'PATTERN' ? '#000' : Colors.textMuted, fontWeight: 'bold' }}>Pro Effects</Text>
            </TouchableOpacity>
            <TouchableOpacity
              onPress={() => {
                AppLogger.log('BUILDER_UI_TOGGLED');
                setFixedSubMode('BUILDER');
              }}
              style={{ flex: 1, paddingVertical: Spacing.sm, alignItems: 'center', backgroundColor: fixedSubMode === 'BUILDER' ? Colors.primary : Colors.surfaceHighlight, borderLeftWidth: 1, borderColor: 'rgba(255,255,255,0.05)', borderTopRightRadius: Layout.borderRadius, borderBottomRightRadius: Layout.borderRadius }}
            >
              <Text style={{ color: fixedSubMode === 'BUILDER' ? '#000' : Colors.textMuted, fontWeight: 'bold' }}>Builder</Text>
            </TouchableOpacity>
          </View>

          {/* PRO EFFECTS TIER */}
          {fixedSubMode === 'PATTERN' && (
            <View style={{ flex: 1, paddingBottom: Spacing.sm }}>
              <ScrollView
                style={{ flex: 1, backgroundColor: Colors.isDark ? '#000000' : 'rgba(0,0,0,0.04)', borderRadius: 8 }}
                contentContainerStyle={{ padding: Spacing.sm, flexDirection: 'row', flexWrap: 'wrap', justifyContent: 'space-between' }}
                showsVerticalScrollIndicator={false}
              >
                {SK8LYTZ_TEMPLATES.map(effect => (
                  <TouchableOpacity
                    key={effect.id}
                    onPress={() => {
                      setFixedSubMode('PATTERN');
                      setFixedPatternId(effect.id);
                      if (applyFixedPattern) {
                        applyFixedPattern(effect.id, fixedFgColor || '#FF0000', fixedBgColor || '#000000', speed);
                      }
                    }}
                    style={{ width: '48%', minHeight: 40, marginBottom: Spacing.sm, flexDirection: 'column', justifyContent: 'center', borderBottomWidth: 1, borderBottomColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.05)' }}
                  >
                    <Text style={{ color: fixedPatternId === effect.id ? Colors.primary : Colors.text, fontWeight: 'bold', fontSize: 11, marginBottom: Spacing.xs }} numberOfLines={1}>
                      {effect.id}. {effect.name}
                    </Text>
                    <CustomEffectVisualizer
                      effectId={effect.id}
                      speed={speed}
                      points={devices?.[0]?.points || points || 16}
                      segments={devices?.[0]?.segments || 1}
                      direction={true}
                      fgColorHex={fixedFgColor}
                      bgColorHex={fixedBgColor}
                    />
                  </TouchableOpacity>
                ))}
              </ScrollView>
            </View>
          )}

          {/* POSITIONAL ARRAY BUILDER TIER */}
          {fixedSubMode === 'BUILDER' && (
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
              deviceLedCount={hwSettings?.ledPoints || points || 150}
              selectedColor={selectedColor}
              writeToDevice={writeToDevice}
            />
          )}

        </View>
      )}
    </View>
  );
});

export default MultiModePanel;
