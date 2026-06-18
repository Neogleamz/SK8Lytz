/**
 * useLoadFavorite.ts — Favorite & Pick restoration hook.
 *
 * Encapsulates the full `loadFavorite` callback that was inline in DockedController.tsx.
 * Handles legacy mode migration, BLE dispatch for each mode type, and logging.
 *
 * Extracted from DockedController.tsx (Phase 1 S4 extraction, 2026-06-18).
 */
import React from 'react';
import type { IFavoriteState, ModeType } from '../types/dashboard.types';
import { AppLogger } from '../services/appLogger';
import { checkPermission } from '../services/PermissionService';

interface UseLoadFavoriteParams {
  setActiveFavoriteId: (id: string) => void;
  setSpeed: (v: number) => void;
  setBrightness: (v: number) => void;
  setSelectedColor: (c: string) => void;
  setActiveMode: (m: ModeType) => void;
  setLastOperatingMode: (m: ModeType) => void;
  setFixedSubMode: (m: 'PATTERN' | 'BUILDER') => void;
  setFixedPatternId: (id: number) => void;
  setFixedColorMode: (m: 'FOREGROUND' | 'BACKGROUND') => void;
  setFixedFgColor: (c: string) => void;
  setFixedBgColor: (c: string) => void;
  setMusicPatternId: (id: number) => void;
  setMicSensitivity: (v: number) => void;
  setMicSource: (s: 'APP' | 'DEVICE') => void;
  setMusicPrimaryColor: (c: string) => void;
  setMusicSecondaryColor: (c: string) => void;
  setMusicMatrixStyle: (v: number) => void;
  setBuilderNodes: (nodes: { id: string; position: number; colorHex: string }[]) => void;
  setBuilderFillMode: (m: 'GRADIENT' | 'SOLID') => void;
  setBuilderTransitionType: (t: number) => void;
  setBuilderDirection: (d: number) => void;
  setMultiColors: (colors: string[]) => void;
  setMultiTransition: (t: number) => void;
  setMultiLength: (l: number) => void;
  activeModeRef: React.MutableRefObject<ModeType | string>;
  handleMusicChange: (pat: number, sens: number, brt: number, src: 'APP' | 'DEVICE', pClr: string, sClr: string, mat: number) => void;
  applyFixedPattern: (id: number, fg: string, bg: string, speed: number, brightness: number, direction?: number) => void;
  sendColor: (r: number, g: number, b: number) => void;
  setMultiColor: (colors: { r: number; g: number; b: number }[], points: number, speed: number, direction: number, transition: number) => void;
  ledPoints?: number;
  brtFactor: (brt: number) => number;
}

export function useLoadFavorite({
  setActiveFavoriteId,
  setSpeed,
  setBrightness,
  setSelectedColor,
  setActiveMode,
  setLastOperatingMode,
  setFixedSubMode,
  setFixedPatternId,
  setFixedColorMode,
  setFixedFgColor,
  setFixedBgColor,
  setMusicPatternId,
  setMicSensitivity,
  setMicSource,
  setMusicPrimaryColor,
  setMusicSecondaryColor,
  setMusicMatrixStyle,
  setBuilderNodes,
  setBuilderFillMode,
  setBuilderTransitionType,
  setBuilderDirection,
  setMultiColors,
  setMultiTransition,
  setMultiLength,
  activeModeRef,
  handleMusicChange,
  applyFixedPattern,
  sendColor,
  setMultiColor,
  ledPoints,
  brtFactor,
}: UseLoadFavoriteParams) {
  const loadFavorite = React.useCallback(
    (favRaw: IFavoriteState, context: 'FAVORITE' | 'PICK' | 'COMMUNITY' = 'FAVORITE') => {
      AppLogger.log(context === 'PICK' ? 'PICK_SELECTED' : 'FAVORITE_LOADED', { id: favRaw.id, mode: favRaw.mode });
      setActiveFavoriteId(favRaw.id);
      setSpeed(favRaw.speed);
      setBrightness(favRaw.brightness);
      if (favRaw.color) setSelectedColor(favRaw.color);

      // Normalize legacy mode names to new taxonomy
      // PROGRAMS/RBM → silently migrate to MULTIMODE/PATTERN (retired in v2.8.0)
      const legacyMode = (favRaw.mode === 'RBM' || favRaw.mode === 'PROGRAMS') ? 'PATTERN'
        : (favRaw.mode === 'FAVORITES' || favRaw.mode === 'PRESETS') ? 'FAVORITES'
          : favRaw.mode;

      if (legacyMode === 'PATTERN' || legacyMode === 'MULTIMODE') {
        setActiveMode('MULTIMODE');
        setLastOperatingMode('MULTIMODE');
        activeModeRef.current = 'MULTIMODE';
        setFixedSubMode('PATTERN');
        const restoredId = favRaw.patternId ?? 1;
        setFixedPatternId(restoredId);
        setFixedColorMode(favRaw.fixedColorMode ?? 'FOREGROUND');
        setFixedFgColor(favRaw.fixedFgColor ?? '#FF6600');
        setFixedBgColor(favRaw.fixedBgColor ?? '#000000');
        applyFixedPattern(restoredId, favRaw.fixedFgColor ?? '#FF6600', favRaw.fixedBgColor ?? '#000000', favRaw.speed ?? 80, favRaw.brightness ?? 100);
      } else if (legacyMode === 'MUSIC') {
        setActiveMode('MUSIC');
        setLastOperatingMode('MUSIC');
        activeModeRef.current = 'MUSIC';

        const restoredPattern = favRaw.patternId ?? 0;
        const restoredSens = favRaw.micSensitivity ?? 80;
        const restoredSource = favRaw.micSource ?? 'APP';
        const restoredPrimary = favRaw.musicPrimaryColor ?? '#FF0000';
        const restoredSecondary = favRaw.musicSecondaryColor ?? '#0000FF';
        const restoredMatrix = favRaw.musicMatrixStyle ?? 0x27;

        setMusicPatternId(restoredPattern);
        setMicSensitivity(restoredSens);
        setMicSource(restoredSource);
        setMusicPrimaryColor(restoredPrimary);
        setMusicSecondaryColor(restoredSecondary);
        setMusicMatrixStyle(restoredMatrix);

        handleMusicChange(restoredPattern, restoredSens, favRaw.brightness ?? 100, restoredSource, restoredPrimary, restoredSecondary, restoredMatrix);
      } else if (legacyMode === 'CAMERA') {
        // Permission gate: if camera denied, fall back to MULTIMODE
        checkPermission('CAMERA').then(granted => {
          if (granted) {
            setActiveMode('CAMERA');
            setLastOperatingMode('CAMERA');
            activeModeRef.current = 'CAMERA';
          } else {
            AppLogger.warn('[useLoadFavorite] CAMERA favorite skipped — permission denied', { payload_size: 0, ssi: 0 });
            setActiveMode('MULTIMODE');
            setLastOperatingMode('MULTIMODE');
          }
        }).catch(err => {
          AppLogger.error('[useLoadFavorite] Error checking CAMERA permission', err, { payload_size: 0, ssi: 0 });
        });
      } else if (legacyMode === 'FAVORITES') {
        setActiveMode('FAVORITES');
      } else if (legacyMode === 'BUILDER') {
        setActiveMode('MULTIMODE');
        setLastOperatingMode('MULTIMODE');
        activeModeRef.current = 'MULTIMODE';
        setFixedSubMode('BUILDER');

        if (favRaw.builderNodes && favRaw.builderNodes.length > 0) {
          setBuilderNodes(favRaw.builderNodes);
        }
        if (favRaw.builderFillMode) setBuilderFillMode(favRaw.builderFillMode);
        if (favRaw.builderTransitionType !== undefined) setBuilderTransitionType(favRaw.builderTransitionType);
        if (favRaw.builderDirection !== undefined) setBuilderDirection(favRaw.builderDirection);

        // Auto-dispatch BUILDER payload instead of dead-loading
        if (favRaw.builderNodes && favRaw.builderNodes.length > 0) {
          const factor = brtFactor(favRaw.brightness ?? 100);
          const rgbColors = favRaw.builderNodes.map((n: { colorHex: string }) => ({
            r: Math.round((parseInt(n.colorHex.slice(1, 3), 16) || 0) * factor),
            g: Math.round((parseInt(n.colorHex.slice(3, 5), 16) || 0) * factor),
            b: Math.round((parseInt(n.colorHex.slice(5, 7), 16) || 0) * factor),
          }));
          const transition = favRaw.builderTransitionType ?? 1;
          const speedVal = Math.max(1, Math.min(100, Math.round(favRaw.speed ?? 50)));
          setMultiColor(rgbColors, ledPoints || 16, speedVal, favRaw.builderDirection ?? 1, transition);
        }
      } else if (legacyMode === 'MULTI' || legacyMode === 'DIY' || legacyMode === 'MULTICOLOR') {
        setActiveMode('MULTIMODE');
        setLastOperatingMode('MULTIMODE');
        activeModeRef.current = 'MULTIMODE';
        setFixedSubMode('BUILDER');

        setMultiColors(favRaw.multiColors || []);
        setMultiTransition(favRaw.multiTransition || 3);
        setMultiLength(favRaw.multiLength || 16);
        if (favRaw.multiColors) {
          const factor = brtFactor(favRaw.brightness ?? 100);
          const rgbColors = favRaw.multiColors.map((h: string) => ({
            r: Math.round((parseInt(h.slice(1, 3), 16) || 0) * factor),
            g: Math.round((parseInt(h.slice(3, 5), 16) || 0) * factor),
            b: Math.round((parseInt(h.slice(5, 7), 16) || 0) * factor),
          }));
          const speedVal = Math.max(1, Math.min(100, Math.round(favRaw.speed ?? 50)));
          setMultiColor(rgbColors, ledPoints || 12, speedVal, 1, favRaw.multiTransition ?? 3);
        }
      } else {
        // Unknown/legacy mode — best-effort color dispatch
        if (favRaw.color) {
          const fallbackColor = favRaw.color;
          sendColor(
            parseInt(fallbackColor.slice(1, 3), 16) || 0,
            parseInt(fallbackColor.slice(3, 5), 16) || 0,
            parseInt(fallbackColor.slice(5, 7), 16) || 0,
          );
        }
      }

      if (context === 'PICK') {
        AppLogger.log('PICK_SELECTED', { id: favRaw.id, mode: legacyMode });
      } else {
        AppLogger.log('FAVORITE_RENDERED', { id: favRaw.id, mode: legacyMode, patternId: favRaw.patternId });
      }
    },
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [handleMusicChange, applyFixedPattern, sendColor, setMultiColor, ledPoints,
     setActiveFavoriteId, setSpeed, setBrightness, setSelectedColor,
     setActiveMode, setLastOperatingMode, setFixedSubMode, setFixedPatternId,
     setFixedColorMode, setFixedFgColor, setFixedBgColor, setMusicPatternId,
     setMicSensitivity, setMicSource, setMusicPrimaryColor, setMusicSecondaryColor,
     setMusicMatrixStyle, setBuilderNodes, setBuilderFillMode, setBuilderTransitionType,
     setBuilderDirection, setMultiColors, setMultiTransition, setMultiLength, brtFactor]
  );

  return { loadFavorite };
}
