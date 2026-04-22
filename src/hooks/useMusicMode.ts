/**
 * useMusicMode.ts — Music mode domain hook.
 *
 * Owns: 0x73 music config dispatch, pattern names, pattern navigation.
 * Extracted from DockedController.tsx to isolate music-specific BLE logic.
 */
import { useCallback, useEffect, useRef } from 'react';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { AppLogger } from '../services/AppLogger';
import type { ModeType } from '../types/dashboard.types';
import { hexToRgb } from '../utils/ColorUtils';

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
  writeToDevice?: (payload: number[]) => Promise<void | boolean | 'partial'>;
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
      patternId,          // musicMode 1-13
      isDeviceMic ? 0x27 : 0x26,  // micSource byte
      true,               // isOn — always sending to enable music mode
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
   * Fix 3: Music Mode Exit Packet.
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
