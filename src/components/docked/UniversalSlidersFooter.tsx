/**
 * UniversalSlidersFooter.tsx — Extracted from DockedController Phase 2 Diet
 *
 * Contains the universal color tracker bars, preset color grid, hue slider,
 * and tactical brightness/speed/sensitivity sliders. Renders below the active
 * mode panel for all modes except FAVORITES.
 */
import React from 'react';
import { StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { Spacing } from '../../theme/theme';
import { hexToHue } from '../../utils/ColorUtils';
import { SK8LYTZ_TEMPLATES } from '../../protocols/PatternEngine';
import { AppLogger } from '../../services/AppLogger';
import { ZenggeProtocol } from '../../protocols/ZenggeProtocol';
import { getActiveMusicProfile } from '../../hooks/useMusicMode';
import NeonHueStrip from '../NeonHueStrip';
import TacticalSlider from '../TacticalSlider';
import type { FixedModePattern, ModeType, MotionState } from '../../types/dashboard.types';
import UniversalColorGrid from './UniversalColorGrid';
import UniversalHueStripSlider from './UniversalHueStripSlider';
import UniversalTacticalSliders from './UniversalTacticalSliders';
import { BuilderNode, PositionalMathBuffer } from '../../protocols/PositionalMathBuffer';

// ── Props Interface ─────────────────────────────────────────────────────────

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

// ── Helper: hue → hex ───────────────────────────────────────────────────────

export const hueToHex = (hue: number): string => {
  const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
  return "#" + [f(5), f(3), f(1)].map(x => Math.round(x * 255).toString(16).padStart(2, "0")).join("");
};

export const hueToHexUpper = (hue: number): string => {
  const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
  return "#" + [f(5), f(3), f(1)].map(x => Math.round(x * 255).toString(16).padStart(2, "0").toUpperCase()).join("");
};

// ── Preset Color Palette ────────────────────────────────────────────────────

export const PRESET_COLORS = [
  '#FF0000', '#FF8000', '#FFFF00', '#00FF00', '#00FFFF',
  '#0000FF', '#800080', '#FF00FF', '#FFFFFF', '#000000',
];

export const PRESET_HUE_MAP: { [key: string]: number } = {
  '#FF0000': 0, '#FF8000': 30, '#FFFF00': 60, '#00FF00': 120,
  '#00FFFF': 180, '#0000FF': 240, '#800080': 280, '#FF00FF': 300, '#FFFFFF': 0, '#000000': 0,
};

// ── Component ───────────────────────────────────────────────────────────────

const UniversalSlidersFooter = React.memo(function UniversalSlidersFooter(props: UniversalSlidersFooterProps) {
  const {
    activeMode, fixedSubMode, fixedColorMode, cameraSubMode, cameraVibePalette,
    selectedColor, fixedFgColor, fixedBgColor, fixedHue,
    musicPrimaryColor, musicSecondaryColor, musicHue, musicSecondaryHue,
    selectedHue, musicColorFocus, streetCruiseColor,
    brightness, speed, micSensitivity, streetSensitivity,
    fixedPatternId, musicPatternId, musicMatrixStyle, micSource,
    multiColors, multiTransition,
    setSelectedColor, setFixedFgColor, setFixedBgColor, setFixedHue,
    setMusicPrimaryColor, setMusicSecondaryColor, setMusicHue, setMusicSecondaryHue,
    setSelectedHue, setMusicColorFocus, setFixedColorMode, setStreetCruiseColor,
    setBrightness, setSpeed, setMicSensitivity, setStreetSensitivity,
    fixedDirection, setFixedDirection,
    sendColor, applyFixedPattern,
    applyStreetPattern, handleMusicChange, clampSpeed, brtFactor,
    writeToDevice, hwSettings, motionStateRef,
    builderNodes, builderFillMode, builderTransitionType, builderDirection,
  } = props;

  return (
    <View style={[styles.sceneSlidersContainer, { marginTop: Spacing.sm, borderTopWidth: 1, borderTopColor: 'rgba(255,255,255,0.05)', paddingTop: Spacing.sm, paddingBottom: 0, flexShrink: 0 }]}>
      {/* Color Grid wrappers */}
      {!(activeMode === 'CAMERA') && (
        <View style={{ marginBottom: Spacing.xs }}>
          {/* Dynamic Selected Color Bar */}
          {!(activeMode === 'MUSIC' || (activeMode === 'MULTIMODE' && fixedSubMode === 'PATTERN')) && (() => {
            const dynamicColor = activeMode === 'STREET' ? streetCruiseColor : selectedColor;

            return (
              <TouchableOpacity
                activeOpacity={0.9}
                onPress={() => {
                  const r = parseInt(dynamicColor.slice(1, 3), 16) || 255;
                  const g = parseInt(dynamicColor.slice(3, 5), 16) || 255;
                  const b = parseInt(dynamicColor.slice(5, 7), 16) || 255;
                  if (activeMode === 'MULTIMODE' && fixedSubMode !== 'PATTERN') sendColor(r, g, b);
                  else if (activeMode === 'STREET') applyStreetPattern(motionStateRef.current);
                }}
                style={{
                  width: '100%', height: 18, borderRadius: 9, backgroundColor: dynamicColor,
                  justifyContent: 'center', alignItems: 'center', marginBottom: Spacing.xs,
                  shadowColor: dynamicColor, shadowOpacity: 0.8, shadowRadius: 10,
                  shadowOffset: { width: 0, height: 0 }, elevation: 6,
                  borderWidth: 1, borderColor: 'rgba(255,255,255,0.4)',
                }}
              >
                <Text style={{ color: '#FFF', fontSize: 9, fontWeight: '800', letterSpacing: 2, textShadowColor: '#000', textShadowRadius: 4, textShadowOffset: { width: 0, height: 1 } }}>
                  SELECTED COLOR
                </Text>
              </TouchableOpacity>
            );
          })()}

          {/* Music Mode Split Color Tracker */}
          {(activeMode === 'MUSIC') && (() => {
            const profile = getActiveMusicProfile(musicMatrixStyle, musicPatternId);
            if (profile.colorMode === 'NONE') {
              return (
                <View style={{ flexDirection: 'row', width: '100%', height: 18, borderRadius: 9, marginBottom: Spacing.xs, borderWidth: 1, borderColor: 'rgba(255,255,255,0.4)', backgroundColor: 'transparent', justifyContent: 'center', alignItems: 'center' }}>
                   <Text style={{ color: 'rgba(255,255,255,0.6)', fontSize: 9, fontWeight: '900', letterSpacing: 1 }}>AUTO COLOR (HARDWARE CONTROLLED)</Text>
                </View>
              );
            }
            return (
              <View style={{ flexDirection: 'row', width: '100%', height: 18, borderRadius: 9, marginBottom: Spacing.xs, borderWidth: 1, borderColor: 'rgba(255,255,255,0.4)', backgroundColor: 'transparent' }}>
                <TouchableOpacity
                  activeOpacity={0.9}
                  onPress={() => setMusicColorFocus('PRIMARY')}
                  style={{ flex: 1, backgroundColor: musicPrimaryColor, justifyContent: 'center', alignItems: 'center', opacity: musicColorFocus === 'PRIMARY' ? 1.0 : 0.4, borderTopLeftRadius: 8, borderBottomLeftRadius: 8, borderTopRightRadius: profile.colorMode === 'FG_ONLY' ? 8 : 0, borderBottomRightRadius: profile.colorMode === 'FG_ONLY' ? 8 : 0, shadowColor: musicPrimaryColor, shadowOpacity: 1, shadowRadius: 16, shadowOffset: { width: 0, height: 0 }, elevation: 12 }}
                >
                  <Text style={{ color: '#FFFFFF', fontSize: 9, fontWeight: '900', letterSpacing: 1, textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 6 }}>SOUND COLUMN</Text>
                </TouchableOpacity>
                {profile.colorMode === 'FG_BG' && (
                  <TouchableOpacity
                    activeOpacity={0.9}
                    onPress={() => setMusicColorFocus('SECONDARY')}
                    style={{ flex: 1, backgroundColor: musicSecondaryColor, justifyContent: 'center', alignItems: 'center', opacity: musicColorFocus === 'SECONDARY' ? 1.0 : 0.4, borderLeftWidth: 1, borderLeftColor: 'rgba(255,255,255,0.4)', borderTopRightRadius: 8, borderBottomRightRadius: 8, shadowColor: musicSecondaryColor, shadowOpacity: 1, shadowRadius: 16, shadowOffset: { width: 0, height: 0 }, elevation: 12 }}
                  >
                    <Text style={{ color: '#FFFFFF', fontSize: 9, fontWeight: '900', letterSpacing: 1, textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 6 }}>DROP COLOR</Text>
                  </TouchableOpacity>
                )}
              </View>
            );
          })()}

          {/* Fixed Pattern Mode Split Color Tracker */}
          {(activeMode === 'MULTIMODE' && fixedSubMode === 'PATTERN') && (() => {
            const selectedEffect = SK8LYTZ_TEMPLATES.find(e => e.id === fixedPatternId);
            const showFg = selectedEffect ? selectedEffect.requiresForeground : true;
            const showBg = selectedEffect ? selectedEffect.requiresBackground : true;
            if (!showFg && !showBg) return null;
            return (
              <View style={{ flexDirection: 'row', width: '100%', height: 18, borderRadius: 9, marginBottom: Spacing.xs, borderWidth: 1, borderColor: 'rgba(255,255,255,0.4)', backgroundColor: 'transparent' }}>
                {showFg && (
                  <TouchableOpacity
                    activeOpacity={0.9}
                    onPress={() => { setFixedColorMode('FOREGROUND'); setFixedHue(hexToHue(fixedFgColor)); }}
                    style={{ flex: 1, backgroundColor: fixedFgColor, justifyContent: 'center', alignItems: 'center', opacity: fixedColorMode === 'FOREGROUND' ? 1.0 : 0.4, borderTopLeftRadius: 8, borderBottomLeftRadius: 8, shadowColor: fixedFgColor, shadowOpacity: 1, shadowRadius: 16, shadowOffset: { width: 0, height: 0 }, elevation: 12 }}
                  >
                    <Text style={{ color: '#FFFFFF', fontSize: 9, fontWeight: '900', letterSpacing: 1, textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 6 }}>FOREGROUND</Text>
                  </TouchableOpacity>
                )}
                {showBg && (
                  <TouchableOpacity
                    activeOpacity={0.9}
                    onPress={() => { setFixedColorMode('BACKGROUND'); setFixedHue(hexToHue(fixedBgColor)); }}
                    style={{ flex: 1, backgroundColor: fixedBgColor, justifyContent: 'center', alignItems: 'center', opacity: fixedColorMode === 'BACKGROUND' ? 1.0 : 0.4, borderLeftWidth: showFg ? 1 : 0, borderLeftColor: 'rgba(255,255,255,0.4)', borderTopRightRadius: 8, borderBottomRightRadius: 8, borderTopLeftRadius: showFg ? 0 : 8, borderBottomLeftRadius: showFg ? 0 : 8, shadowColor: fixedBgColor, shadowOpacity: 1, shadowRadius: 16, shadowOffset: { width: 0, height: 0 }, elevation: 12 }}
                  >
                    <Text style={{ color: '#FFFFFF', fontSize: 9, fontWeight: '900', letterSpacing: 1, textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 6 }}>BACKGROUND</Text>
                  </TouchableOpacity>
                )}
              </View>
            );
          })()}


        {/* 9 Preset Colors Grid */}
        <UniversalColorGrid {...props} />
        {/* Hue Slider */}
        <UniversalHueStripSlider {...props} />
        {/* TACTICAL UNIVERSAL SLIDERS SECTIONS */}
        <UniversalTacticalSliders {...props} />
        </View>
      )}
    </View>
  );
});

export default UniversalSlidersFooter;


// -- Local StyleSheet -----------------------------------------------------------
// Migrated from parent DockedController prop-threading (feat/hollow-shell-v3-cleanup).
// These 3 keys are layout-only -- no theme colours required.
const styles = StyleSheet.create({
  sceneSlidersContainer: {
    // Original: padding Spacing.lg so left/right sides breathe;
    // paddingTop + paddingBottom are overridden per-site in JSX.
    padding: Spacing.lg,
    backgroundColor: 'rgba(255,255,255,0.02)',
  },
  colorGrid: {
    flexDirection: 'row',
    flexWrap: 'nowrap',
    marginTop: Spacing.lg,
    justifyContent: 'space-between',
    alignItems: 'center',
    width: '100%',
  },
  controlRow: {
    marginTop: Spacing.sm,
  },
});