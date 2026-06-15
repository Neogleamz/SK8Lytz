import React from 'react';
import { View, StyleSheet } from 'react-native';
import { Spacing } from '../../theme/theme';
import NeonHueStrip from '../NeonHueStrip';
import type { UniversalSlidersFooterProps } from './UniversalSlidersFooter';

// Duplicated locally to break R-29 circular dependency without violating S4 bounds
const hueToHex = (hue: number) => {
  const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
  const rgbToHex = (r: number, g: number, b: number) => '#' + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, '0')).join('');
  return rgbToHex(f(5), f(3), f(1));
};
const hueToHexUpper = (hue: number) => hueToHex(hue).toUpperCase();
export const UniversalHueStripSlider = React.memo(function UniversalHueStripSlider(props: UniversalSlidersFooterProps) {
  const {
    activeMode, fixedSubMode, fixedColorMode,
    setSelectedColor,
    setFixedFgColor, setFixedBgColor, setFixedHue, fixedFgColor, fixedBgColor,
    setMusicPrimaryColor, setMusicSecondaryColor, setMusicHue, setMusicSecondaryHue, musicPrimaryColor, musicSecondaryColor,
    setSelectedHue, setStreetCruiseColor, musicColorFocus,
    applyFixedPattern, handleMusicChange, applyStreetPattern, sendColor,
    fixedPatternId, speed, brightness, fixedDirection, musicPatternId, micSensitivity, micSource, musicMatrixStyle,
    motionStateRef, selectedHue, fixedHue, musicHue, musicSecondaryHue
  } = props;

  return (
    <View style={[styles.controlRow, { marginTop: Spacing.xs, marginBottom: Spacing.xs, flexShrink: 0, minHeight: 40 }]}>
{!(activeMode === 'CAMERA') && (
        
          <NeonHueStrip
            value={activeMode === 'MUSIC' ? (musicColorFocus === 'PRIMARY' ? musicHue : musicSecondaryHue) : activeMode === 'MULTIMODE' ? fixedHue : selectedHue}
            onValueChange={(hue) => {
              if (activeMode === 'MULTIMODE') {
                if (fixedSubMode === 'PATTERN') {
                  setFixedHue(hue);
                  const hex = hueToHex(hue);
                  if (fixedColorMode === 'FOREGROUND') { setFixedFgColor(hex); setSelectedColor(hex); }
                  else { setFixedBgColor(hex); setSelectedColor(hex); }
                } else {
                  setFixedHue(hue);
                  setSelectedColor(hueToHex(hue));
                }
              } else if (activeMode === 'MUSIC') {
                const hex = hueToHexUpper(hue);
                if (musicColorFocus === 'PRIMARY') { setMusicPrimaryColor(hex); setMusicHue(hue); }
                else { setMusicSecondaryColor(hex); setMusicSecondaryHue(hue); }
              } else if (activeMode === 'STREET') {
                setStreetCruiseColor(hueToHexUpper(hue));
                setSelectedHue(hue);
                applyStreetPattern(motionStateRef.current);
              } else {
                setSelectedHue(hue);
                setSelectedColor(hueToHexUpper(hue));
              }
            }}
            onSlidingComplete={(hue) => {
              if (activeMode === 'MULTIMODE') {
                if (fixedSubMode === 'PATTERN') applyFixedPattern(fixedPatternId, fixedFgColor, fixedBgColor, speed, brightness, fixedDirection);
              } else if (activeMode === 'MUSIC') {
                const hex = hueToHexUpper(hue);
                if (musicColorFocus === 'PRIMARY') handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, hex, musicSecondaryColor, musicMatrixStyle);
                else handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, hex, musicMatrixStyle);
              } else if (activeMode === 'STREET') {
                applyStreetPattern(motionStateRef.current);
              } else {
                const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                sendColor(Math.round(f(5) * 255), Math.round(f(3) * 255), Math.round(f(1) * 255));
              }
            }}
            minimumValue={0}
            maximumValue={360}
            style={{ flex: 1 }}
          />
        
      )}
    </View>
  );
});

const styles = StyleSheet.create({
  controlRow: {
    marginTop: Spacing.sm,
  }
});
export default UniversalHueStripSlider;
