/**
 * useMusicMode.ts — Music mode domain hook.
 *
 * Owns: 0x73 music config dispatch, pattern names, pattern navigation.
 * Extracted from DockedController.tsx to isolate music-specific BLE logic.
 */
import { useEffect, useCallback } from 'react';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { AppLogger } from '../services/AppLogger';
import { hexToRgb } from '../utils/ColorUtils';
import type { ModeType } from '../types/dashboard.types';

/** The 13 built-in Zengge music reactive patterns. */
export const MUSIC_PATTERNS = [
  'Soft', 'Cheerful', 'Energy', 'Relax', 'Passion',
  'Brisk', 'Rhythm', 'Rolling', 'Flicker', 'Accumulation',
  'Shuttle', 'Fireworks', 'Snow'
] as const;

/** Derive a human-readable label for a music pattern by ID. */
export const getMusicPatternLabel = (patternId: number): string =>
  MUSIC_PATTERNS[patternId - 1] || `Effect ${patternId}`;

interface UseMusicModeParams {
  activeMode: ModeType;
  writeToDevice?: (payload: number[]) => Promise<void | boolean>;
  musicPatternId: number;
  micSensitivity: number;
  brightness: number;
  micSource: 'APP' | 'DEVICE';
  musicPrimaryColor: string;
  musicSecondaryColor: string;
  musicMatrixStyle: number;
}

/**
 * Encapsulates the music config dispatch logic (0x73 protocol).
 * Fires `handleMusicChange` to send the current music state to hardware.
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

    AppLogger.log("MUSIC_CONFIG_REQUESTED", { patternId, c1Hex: color1Hex, c2Hex: color2Hex, matrix });

    writeToDevice(ZenggeProtocol.setMusicConfig(
      isDeviceMic, matrix, patternId, c1, c2, sens, bright
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

  return { handleMusicChange };
}
