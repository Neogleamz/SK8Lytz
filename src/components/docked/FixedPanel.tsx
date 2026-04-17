/**
 * FixedPanel.tsx — Stationary Solid/Blink/Strobe mode panel.
 *
 * Renders when activeMode === 'FIXED'. Contains:
 *  - Pattern toggle: STATIC | STROBE | BLINK
 *  - Localized speed slider for STROBE/BLINK patterns.
 *
 * Extracted/Added during fixed-mode-refactor.
 */
import React from 'react';
import { Text, TouchableOpacity, View } from 'react-native';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { Layout, Spacing, Typography } from '../../theme/theme';
import TacticalSlider from '../TacticalSlider';
import type { FixedModePattern } from '../../types/dashboard.types';

interface FixedPanelProps {
  fixedModePattern: FixedModePattern;
  setFixedModePattern: (pattern: FixedModePattern) => void;
  speed: number;
  setSpeed: (speed: number) => void;
  applyPattern: (pattern: FixedModePattern, newSpeed?: number) => void;
  Colors: any;
}

const FixedPanel = React.memo(({
  fixedModePattern,
  setFixedModePattern,
  speed,
  setSpeed,
  applyPattern,
  Colors,
}: FixedPanelProps) => {
  return (
    <View style={{ flex: 1, paddingHorizontal: Spacing.xs, paddingTop: Spacing.xs, justifyContent: 'center' }}>
      {/* ── Pattern Pills ── */}
      <View style={{ flexDirection: 'row', marginBottom: Spacing.lg, flexShrink: 0 }}>
        {([{ id: 'STATIC', label: 'STATIC', icon: 'record-circle' },
           { id: 'STROBE', label: 'STROBE', icon: 'flash' },
           { id: 'BLINK',  label: 'BLINK',  icon: 'alarm-light' }
          ] as const).map((pat) => (
          <TouchableOpacity
            key={pat.id}
            onPress={() => {
              setFixedModePattern(pat.id);
              applyPattern(pat.id);
            }}
            style={{
              flex: 1, 
              paddingVertical: Spacing.sm, 
              alignItems: 'center', 
              backgroundColor: fixedModePattern === pat.id ? Colors.primary : Colors.surfaceHighlight, 
              borderWidth: 1,
              borderColor: fixedModePattern === pat.id ? Colors.primary : 'rgba(255,255,255,0.05)',
              borderRightWidth: pat.id !== 'BLINK' ? 1 : 1,
              borderTopLeftRadius: pat.id === 'STATIC' ? Layout.borderRadius : 0, 
              borderBottomLeftRadius: pat.id === 'STATIC' ? Layout.borderRadius : 0,
              borderTopRightRadius: pat.id === 'BLINK' ? Layout.borderRadius : 0, 
              borderBottomRightRadius: pat.id === 'BLINK' ? Layout.borderRadius : 0,
              flexDirection: 'row',
              justifyContent: 'center',
              gap: 4
            }}
          >
            <MaterialCommunityIcons name={pat.icon} size={14} color={fixedModePattern === pat.id ? '#000' : Colors.textMuted} />
            <Text style={{ color: fixedModePattern === pat.id ? '#000' : Colors.textMuted, fontWeight: 'bold', fontSize: 11 }}>
              {pat.label}
            </Text>
          </TouchableOpacity>
        ))}
      </View>

      {/* ── Conditional Speed Slider ── */}
      <View style={{ minHeight: 40, justifyContent: 'center' }}>
        {fixedModePattern !== 'STATIC' ? (
          <TacticalSlider
            style={{ flex: 1 }}
            iconName="speedometer"
            label="PATTERN SPEED"
            fillColor="#FF00FF"
            dynamicMode={fixedModePattern}
            value={speed}
            onValueChange={setSpeed}
            minimumValue={1}
            maximumValue={100}
            onSlidingComplete={(val) => applyPattern(fixedModePattern, val)}
          />
        ) : (
          <View style={{ alignItems: 'center' }}>
            <Text style={[Typography.caption, { color: Colors.textMuted, opacity: 0.6 }]}>
              Static mode applies color instantly. Select a color below.
            </Text>
          </View>
        )}
      </View>
    </View>
  );
});

export default FixedPanel;
