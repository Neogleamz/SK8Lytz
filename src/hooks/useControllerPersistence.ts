/**
 * useControllerPersistence.ts — AsyncStorage save/restore for controller state.
 *
 * Manages the bidirectional persistence bridge between DockedController's
 * in-memory FSM state and the `@Sk8lytz_ControllerState` AsyncStorage blob.
 *
 * Extracted from DockedController.tsx to isolate persistence side-effects.
 */
import { useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { STORAGE_PREFIX } from '../constants/AppConstants';
import type { ModeType } from '../types/dashboard.types';

interface ControllerStateSetters {
  setSelectedColor: (v: string) => void;
  setSelectedPatternId: (v: number) => void;
  setBrightness: (v: number) => void;
  setSpeed: (v: number) => void;
  setMicSensitivity: (v: number) => void;
  setMusicHue: (v: number) => void;
  setMusicSecondaryHue: (v: number) => void;
  setMusicPrimaryColor: (v: string) => void;
  setMusicSecondaryColor: (v: string) => void;
  setMusicMatrixStyle: (v: number) => void;
  setMusicPatternId: (v: number) => void;
  setMicSource: (v: 'APP' | 'DEVICE') => void;
  setMusicSetting: (v: 'SENSITIVITY' | 'BRIGHTNESS') => void;
  setFixedPatternId: (v: number) => void;
  setFixedColorMode: (v: 'FOREGROUND' | 'BACKGROUND') => void;
  setFixedFgColor: (v: string) => void;
  setFixedBgColor: (v: string) => void;
  setFixedHue: (v: number) => void;
}

interface ControllerStateValues {
  activeMode: ModeType;
  selectedColor: string;
  selectedPatternId: number;
  brightness: number;
  speed: number;
  micSensitivity: number;
  musicHue: number;
  musicSecondaryHue: number;
  musicPrimaryColor: string;
  musicSecondaryColor: string;
  musicMatrixStyle: number;
  musicPatternId: number;
  micSource: 'APP' | 'DEVICE';
  musicSetting: 'SENSITIVITY' | 'BRIGHTNESS';
  fixedPatternId: number;
  fixedColorMode: 'FOREGROUND' | 'BACKGROUND';
  fixedFgColor: string;
  fixedBgColor: string;
  fixedHue: number;
}

/**
 * Side-effect-only hook that loads controller state from AsyncStorage on mount
 * and saves it back whenever tracked values change.
 */
export function useControllerPersistence(
  stateValues: ControllerStateValues,
  setters: ControllerStateSetters
) {
  // Load from persistence on mount
  useEffect(() => {
    AsyncStorage.getItem(`${STORAGE_PREFIX}ControllerState`).then((saved) => {
      if (saved) {
        try {
          const parsed = JSON.parse(saved);
          if (parsed.selectedColor) setters.setSelectedColor(parsed.selectedColor);
          if (parsed.selectedPatternId) setters.setSelectedPatternId(parsed.selectedPatternId);
          if (parsed.brightness !== undefined) setters.setBrightness(parsed.brightness);
          else setters.setBrightness(90);
          if (parsed.speed !== undefined) setters.setSpeed(parsed.speed);
          else setters.setSpeed(50);
          if (parsed.micSensitivity !== undefined) setters.setMicSensitivity(parsed.micSensitivity);
          if (parsed.musicHue !== undefined) setters.setMusicHue(parsed.musicHue);
          if (parsed.musicSecondaryHue !== undefined) setters.setMusicSecondaryHue(parsed.musicSecondaryHue);
          if (parsed.musicPrimaryColor) setters.setMusicPrimaryColor(parsed.musicPrimaryColor);
          if (parsed.musicSecondaryColor) setters.setMusicSecondaryColor(parsed.musicSecondaryColor);
          if (parsed.musicMatrixStyle) setters.setMusicMatrixStyle(parsed.musicMatrixStyle);
          if (parsed.musicPatternId) setters.setMusicPatternId(parsed.musicPatternId);
          if (parsed.micSource) setters.setMicSource(parsed.micSource);
          if (parsed.musicSetting) setters.setMusicSetting(parsed.musicSetting);
          if (parsed.fixedPatternId) setters.setFixedPatternId(parsed.fixedPatternId);
          if (parsed.fixedColorMode) setters.setFixedColorMode(parsed.fixedColorMode);
          if (parsed.fixedFgColor) setters.setFixedFgColor(parsed.fixedFgColor);
          if (parsed.fixedBgColor) setters.setFixedBgColor(parsed.fixedBgColor);
          if (parsed.fixedHue !== undefined) setters.setFixedHue(parsed.fixedHue);
        } catch (_e) { /* corrupted data — use defaults */ }
      }
    });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // Save to persistence on state change
  useEffect(() => {
    const stateBlob = {
      activeMode: stateValues.activeMode,
      selectedColor: stateValues.selectedColor,
      selectedPatternId: stateValues.selectedPatternId,
      brightness: stateValues.brightness,
      speed: stateValues.speed,
      micSensitivity: stateValues.micSensitivity,
      musicHue: stateValues.musicHue,
      musicSecondaryHue: stateValues.musicSecondaryHue,
      musicPrimaryColor: stateValues.musicPrimaryColor,
      musicSecondaryColor: stateValues.musicSecondaryColor,
      musicMatrixStyle: stateValues.musicMatrixStyle,
      musicPatternId: stateValues.musicPatternId,
      micSource: stateValues.micSource,
      musicSetting: stateValues.musicSetting,
      fixedPatternId: stateValues.fixedPatternId,
      fixedColorMode: stateValues.fixedColorMode,
      fixedFgColor: stateValues.fixedFgColor,
      fixedBgColor: stateValues.fixedBgColor,
      fixedHue: stateValues.fixedHue,
    };
    AsyncStorage.setItem(`${STORAGE_PREFIX}ControllerState`, JSON.stringify(stateBlob)).catch(() => {});
  }, [
    stateValues.activeMode, stateValues.selectedColor, stateValues.selectedPatternId,
    stateValues.brightness, stateValues.speed, stateValues.micSensitivity,
    stateValues.musicHue, stateValues.musicSecondaryHue, stateValues.musicPrimaryColor,
    stateValues.musicSecondaryColor, stateValues.musicMatrixStyle, stateValues.musicPatternId,
    stateValues.micSource, stateValues.musicSetting, stateValues.fixedPatternId,
    stateValues.fixedColorMode, stateValues.fixedFgColor, stateValues.fixedBgColor,
    stateValues.fixedHue,
  ]);
}
