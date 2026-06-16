import React from 'react';
import { View, TouchableOpacity, StyleSheet } from 'react-native';
import { Spacing } from '../../theme/theme';
import type { UniversalSlidersFooterProps } from './UniversalSlidersFooter';

// Duplicated locally to break R-29 circular dependency without violating S4 bounds
const PRESET_COLORS = ['#FF0000', '#FF7F00', '#FFFF00', '#00FF00', '#00FFFF', '#0000FF', '#4B0082', '#9400D3', '#FF00FF', '#FFFFFF', '#000000'];
const PRESET_HUE_MAP: Record<string, number> = {
  '#FF0000': 0, '#FF7F00': 30, '#FFFF00': 60, '#00FF00': 120, '#00FFFF': 180, '#0000FF': 240, '#4B0082': 275, '#9400D3': 285, '#FF00FF': 300
};

export const UniversalColorGrid = React.memo(function UniversalColorGrid(props: UniversalSlidersFooterProps) {
  const {
    activeMode, fixedSubMode, fixedColorMode,
    selectedColor, setSelectedColor,
    setFixedFgColor, setFixedBgColor, setFixedHue, fixedFgColor, fixedBgColor,
    setMusicPrimaryColor, setMusicSecondaryColor, setMusicHue, setMusicSecondaryHue, musicPrimaryColor, musicSecondaryColor,
    setSelectedHue, setStreetCruiseColor, musicColorFocus,
    applyFixedPattern, handleMusicChange, applyStreetPattern, sendColor,
    fixedPatternId, speed, brightness, fixedDirection, musicPatternId, micSensitivity, micSource, musicMatrixStyle,
    motionStateRef
  } = props;

  return (
    <View style={styles.colorGrid}>
          {(
            <View style={[styles.colorGrid, { paddingHorizontal: 0, flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }]}>
              {PRESET_COLORS.map((color, index) => {
                let dynamicColor = selectedColor;
                if (activeMode === 'MULTIMODE' && fixedSubMode === 'PATTERN') {
                  dynamicColor = fixedColorMode === 'FOREGROUND' ? fixedFgColor : fixedBgColor;
                } else if (activeMode === 'MUSIC') {
                  dynamicColor = musicColorFocus === 'PRIMARY' ? musicPrimaryColor : musicSecondaryColor;
                }
                const isActive = typeof dynamicColor === 'string' && dynamicColor.toUpperCase() === color.toUpperCase();
                return (
                  <TouchableOpacity
                    key={index}
                    onPress={() => {
                      if (activeMode === 'MULTIMODE') {
                        if (fixedSubMode === 'PATTERN') {
                          let newFg = fixedFgColor;
                          let newBg = fixedBgColor;
                          if (fixedColorMode === 'FOREGROUND') { newFg = color; setFixedFgColor(color); setSelectedColor(color); }
                          else { newBg = color; setFixedBgColor(color); setSelectedColor(color); }
                          if (PRESET_HUE_MAP[color] !== undefined) setFixedHue(PRESET_HUE_MAP[color]);
                          applyFixedPattern(fixedPatternId, newFg, newBg, speed, brightness, fixedDirection);
                        } else {
                          setSelectedColor(color);
                          if (PRESET_HUE_MAP[color] !== undefined) setFixedHue(PRESET_HUE_MAP[color]);
                        }
                      } else if (activeMode === 'MUSIC') {
                        if (musicColorFocus === 'PRIMARY') {
                          setMusicPrimaryColor(color);
                          if (PRESET_HUE_MAP[color] !== undefined) setMusicHue(PRESET_HUE_MAP[color]);
                          handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, color, musicSecondaryColor, musicMatrixStyle);
                        } else {
                          setMusicSecondaryColor(color);
                          if (PRESET_HUE_MAP[color] !== undefined) setMusicSecondaryHue(PRESET_HUE_MAP[color]);
                          handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, color, musicMatrixStyle);
                        }
                      } else if (activeMode === 'STREET') {
                        setStreetCruiseColor(color);
                        if (PRESET_HUE_MAP[color] !== undefined) setSelectedHue(PRESET_HUE_MAP[color]);
                        applyStreetPattern(motionStateRef.current);
                      } else {
                        setSelectedColor(color);
                        if (PRESET_HUE_MAP[color] !== undefined) setSelectedHue(PRESET_HUE_MAP[color]);
                        const r = parseInt(color.slice(1, 3), 16);
                        const g = parseInt(color.slice(3, 5), 16);
                        const b = parseInt(color.slice(5, 7), 16);
                        sendColor(r, g, b);
                      }
                    }}
                    style={[
                      { backgroundColor: color, width: 20, height: 20, borderRadius: 10, shadowColor: color, shadowOpacity: 1, shadowRadius: 10, shadowOffset: { width: 0, height: 0 }, elevation: 8, margin: Spacing.xxs, borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)' },
                      isActive && { borderWidth: 2, borderColor: '#FFF' },
                    ]}
                  />
                );
              })}
            </View>
          )}
        </View>
  );
});

const styles = StyleSheet.create({
  colorGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    marginTop: Spacing.lg,
    justifyContent: 'space-between',
    alignItems: 'center',
    width: '100%',
  }
});
export default UniversalColorGrid;
