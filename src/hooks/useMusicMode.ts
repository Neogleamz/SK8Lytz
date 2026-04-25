/**
 * useMusicMode.ts — Music mode domain hook.
 *
 * Owns: 0x73 music config dispatch, pattern names, pattern navigation.
 * Extracted from DockedController.tsx to isolate music-specific BLE logic.
 *
 * Pattern data source: MusicDictionary.ts (46 profiles across 2 matrices)
 *  - 0x26 (Light Bar):    16 profiles  →  LIGHT_BAR_PROFILES
 *  - 0x27 (Light Screen): 30 profiles  →  LIGHT_SCREEN_PROFILES
 */
import { useCallback, useEffect, useRef } from 'react';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { AppLogger } from '../services/AppLogger';
import type { ModeType } from '../types/dashboard.types';
import { hexToRgb } from '../utils/ColorUtils';
import {
  getActiveMusicProfile,
  getMusicPatternMax,
  getMusicProfiles,
  getMusicPatternLabel,
} from '../utils/MusicDictionary';

// Re-export helpers so consumers can import from a single location.
export { getMusicProfiles, getMusicPatternMax, getActiveMusicProfile, getMusicPatternLabel };

interface UseMusicModeParams {
  activeMode: ModeType;
  writeToDevice?: (payload: number[]) => Promise<void | boolean | 'partial'>;
  musicPatternId: number;
  micSensitivity: number;
  brightness: number;
  /**
   * Controls the `modeType` byte in the 0x73 packet (Protocol Bible §0x73).
   *  'APP'    → 0x26 — Light Bar matrix  (16 patterns)
   *  'DEVICE' → 0x27 — Light Screen matrix (30 patterns)
   * DO NOT confuse with `isOn` — this byte selects the matrix style, not the mic source.
   */
  micSource: 'APP' | 'DEVICE';
  musicPrimaryColor: string;
  musicSecondaryColor: string;
  musicMatrixStyle: number;
}

/**
 * Encapsulates the music config dispatch logic (0x73 protocol).
 * Fires `handleMusicChange` to send the current music state to hardware.
 *
 * patternId valid ranges:
 *  - 1–16 when musicMatrixStyle === 0x26 (Light Bar)
 *  - 1–30 when musicMatrixStyle === 0x27 (Light Screen)
 */
export function useMusicMode({
  activeMode,
  writeToDevice,
  musicPatternId,
  micSensitivity,
  brightness,
  micSource,
  musicPrimaryColor,
  musicSecondaryColor,
  musicMatrixStyle,
}: UseMusicModeParams) {

  const handleMusicChange = useCallback((
    patternId: number = musicPatternId,
    sens: number = micSensitivity,
    bright: number = brightness,
    src: 'APP' | 'DEVICE' = micSource,
    color1Hex: string = musicPrimaryColor,
    color2Hex: string = musicSecondaryColor,
    matrix: number = musicMatrixStyle
  ) => {
    if (!writeToDevice) return;

    const isDeviceMic = src === 'DEVICE';
    const c1 = hexToRgb(color1Hex);
    const c2 = hexToRgb(color2Hex);

    // Clamp patternId to the valid range for this matrix so we never send
    // an out-of-range effectId byte to the hardware.
    const maxId = getMusicPatternMax(matrix);
    const safePatternId = Math.max(1, Math.min(patternId, maxId));

    AppLogger.log('MUSIC_CONFIG_REQUESTED', {
      patternId: safePatternId,
      matrix: matrix === 0x27 ? 'LIGHT_SCREEN' : 'LIGHT_BAR',
      c1Hex: color1Hex,
      c2Hex: color2Hex,
    });

    writeToDevice(ZenggeProtocol.setMusicConfig(
      safePatternId,                        // effectId — 1–16 or 1–30
      isDeviceMic ? 0x27 : 0x26,           // modeType byte (matrix style)
      true,                                 // isOn — always true when entering music mode
      c1,
      c2,
      sens,
      bright
    ));
  }, [writeToDevice, musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, musicSecondaryColor, musicMatrixStyle]);

  // Re-send music config on color/pattern/source/matrix change
  useEffect(() => {
    if (activeMode !== 'MUSIC' || !writeToDevice) return;
    handleMusicChange(
      musicPatternId, micSensitivity, brightness, micSource,
      musicPrimaryColor, musicSecondaryColor, musicMatrixStyle
    );
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [musicPrimaryColor, musicSecondaryColor, musicPatternId, micSource, musicMatrixStyle]);

  /**
   * Track previous activeMode to detect MUSIC → other transitions.
   * useRef so changes don't cause re-renders.
   */
  const previousActiveModeRef = useRef<ModeType>(activeMode);

  /**
   * Music Mode Exit Packet.
   * When the user switches away from MUSIC, send isOn=false to tell the hardware
   * to stop reacting to ambient sound. Without this, the device stays in
   * music-reactive mode indefinitely even though the UI has moved on.
   *
   * APK truth: 0x73 packet with isOn=0x00 is the correct exit signal.
   */
  useEffect(() => {
    const prev = previousActiveModeRef.current;
    previousActiveModeRef.current = activeMode;

    if (prev === 'MUSIC' && activeMode !== 'MUSIC' && writeToDevice) {
      const c1 = hexToRgb(musicPrimaryColor);
      const c2 = hexToRgb(musicSecondaryColor);
      const isDeviceMic = micSource === 'DEVICE';
      AppLogger.log('MUSIC_MODE_EXIT', { from: prev, to: activeMode });
      writeToDevice(ZenggeProtocol.setMusicConfig(
        musicPatternId,
        isDeviceMic ? 0x27 : 0x26,
        false, // isOn=false — exit music reactive mode
        c1,
        c2,
        micSensitivity,
        brightness
      ));
    }
  }, [activeMode]);
  // Note: intentionally omitting music params from deps — we want to fire
  // exactly once on mode transition, not re-fire when colors change.
  // eslint-disable-next-line react-hooks/exhaustive-deps

  return { handleMusicChange };
}
