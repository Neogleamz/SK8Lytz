/**
 * universalSliders.types.ts
 *
 * Shared prop interface for UniversalSlidersFooter and its three child
 * components (UniversalColorGrid, UniversalHueStripSlider, UniversalTacticalSliders).
 *
 * Extracted here to break the circular import:
 *   UniversalSlidersFooter → children → UniversalSlidersFooter (for Props type).
 * This file imports nothing from the docked/ siblings, making it a true leaf.
 */
import type { FixedModePattern, ModeType, MotionState } from '../../types/dashboard.types';
import type { BuilderNode } from '../../protocols/PositionalMathBuffer';
import React from 'react';

export interface UniversalSlidersFooterProps {
  // ── Active mode context ─────────────────────────────────────────────────
  activeMode: ModeType;
  isBuildingCustom?: boolean;
  cameraSubMode?: 'SNIPER' | 'VIBE';
  cameraVibePalette?: string[];
  fixedSubMode: string;
  fixedColorMode: 'FOREGROUND' | 'BACKGROUND';
  fixedModePattern: FixedModePattern;

  // ── Color state ─────────────────────────────────────────────────────────
  selectedColor: string;
  fixedFgColor: string;
  fixedBgColor: string;
  fixedHue: number;
  musicPrimaryColor: string;
  musicSecondaryColor: string;
  musicHue: number;
  musicSecondaryHue: number;
  selectedHue: number;
  musicColorFocus: 'PRIMARY' | 'SECONDARY';
  streetCruiseColor: string;

  // ── Slider values ───────────────────────────────────────────────────────
  brightness: number;
  speed: number;
  micSensitivity: number;
  streetSensitivity: number;

  // ── Mode-specific IDs ───────────────────────────────────────────────────
  fixedPatternId: number;
  selectedPatternId: number;
  musicPatternId: number;
  musicMatrixStyle: number;
  micSource: 'APP' | 'DEVICE';
  multiColors: string[];
  multiTransition: number;

  // ── Setters ─────────────────────────────────────────────────────────────
  setSelectedColor: (c: string) => void;
  setFixedFgColor: (c: string) => void;
  setFixedBgColor: (c: string) => void;
  setFixedHue: (h: number) => void;
  setMusicPrimaryColor: (c: string) => void;
  setMusicSecondaryColor: (c: string) => void;
  setMusicHue: (h: number) => void;
  setMusicSecondaryHue: (h: number) => void;
  setSelectedHue: (h: number) => void;
  setMusicColorFocus: (focus: 'PRIMARY' | 'SECONDARY') => void;
  setFixedColorMode: (mode: 'FOREGROUND' | 'BACKGROUND') => void;
  setStreetCruiseColor: (c: string) => void;
  setBrightness: (v: number) => void;
  setSpeed: (v: number) => void;
  setMicSensitivity: (v: number) => void;
  setStreetSensitivity: (v: number) => void;
  fixedDirection?: number;
  setFixedDirection?: (dir: number) => void;

  // ── Dispatch functions ──────────────────────────────────────────────────
  sendColor: (r: number, g: number, b: number) => void;
  setMultiColor?: (colors: {r: number, g: number, b: number}[], ledPoints: number, speed: number, direction: number, transitionType?: number) => Promise<void>;
  applyFixedPattern: (patternId: number, fg: string, bg: string, spd?: number, brt?: number, direction?: number) => void;
  applyStaticModePattern: (pat: FixedModePattern, r?: number, g?: number, b?: number, spd?: number) => void;
  applyEmergencyPattern: (spd: number, brt: number) => void;
  applyStreetPattern: (motionState: MotionState, brt?: number, spd?: number) => void;
  handleMusicChange: (patId: number, sens: number, brt: number, src: 'APP' | 'DEVICE', prim: string, sec: string, matrix: number) => void;
  clampSpeed: (v: number) => number;
  brtFactor: (brt: number) => number;
  writeToDevice?: (payload: number[]) => Promise<void>;
  hwSettings?: import('../../types/dashboard.types').IHardwareSettings;
  motionStateRef: React.MutableRefObject<MotionState>;

  // ── Custom Builder pattern state ─────────────────────────────────────────
  builderNodes?: BuilderNode[];
  builderFillMode?: 'GRADIENT' | 'SOLID';
  builderTransitionType?: number;
  builderDirection?: number;
}
