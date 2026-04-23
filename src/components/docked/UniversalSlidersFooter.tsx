/**
 * UniversalSlidersFooter.tsx — Extracted from DockedController Phase 2 Diet
 *
 * Contains the universal color tracker bars, preset color grid, hue slider,
 * and tactical brightness/speed/sensitivity sliders. Renders below the active
 * mode panel for all modes except FAVORITES.
 */
import React from 'react';
import { Text, TouchableOpacity, View } from 'react-native';
import { Spacing } from '../../theme/theme';
import { hexToHue } from '../../utils/ColorUtils';
import { SK8LYTZ_TEMPLATES } from '../../protocols/PatternEngine';
import { AppLogger } from '../../services/AppLogger';
import { ZenggeProtocol } from '../../protocols/ZenggeProtocol';
import NeonHueStrip from '../NeonHueStrip';
import TacticalSlider from '../TacticalSlider';
import type { FixedModePattern, ModeType, MotionState } from '../../types/dashboard.types';

// ── Props Interface ─────────────────────────────────────────────────────────

export interface UniversalSlidersFooterProps {
  // ── Active mode context ─────────────────────────────────────────────────
  activeMode: ModeType;
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
  applyFixedPattern: (patternId: number, fg: string, bg: string, spd?: number, brt?: number) => void;
  applyStaticModePattern: (pat: FixedModePattern, r?: number, g?: number, b?: number, spd?: number) => void;
  applyEmergencyPattern: (spd: number, brt: number) => void;
  applyStreetPattern: (motionState: MotionState, brt?: number, spd?: number) => void;
  handleMusicChange: (patId: number, sens: number, brt: number, src: 'APP' | 'DEVICE', prim: string, sec: string, matrix: number) => void;
  clampSpeed: (v: number) => number;
  brtFactor: (brt: number) => number;
  writeToDevice?: (payload: number[]) => void;
  hwSettings?: any;
  motionStateRef: React.MutableRefObject<MotionState>;

  // ── Styling ─────────────────────────────────────────────────────────────
  styles: any;
}

// ── Helper: hue → hex ───────────────────────────────────────────────────────

const hueToHex = (hue: number): string => {
  const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
  return "#" + [f(5), f(3), f(1)].map(x => Math.round(x * 255).toString(16).padStart(2, "0")).join("");
};

const hueToHexUpper = (hue: number): string => {
  const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
  return "#" + [f(5), f(3), f(1)].map(x => Math.round(x * 255).toString(16).padStart(2, "0").toUpperCase()).join("");
};

// ── Preset Color Palette ────────────────────────────────────────────────────

const PRESET_COLORS = [
  '#FF0000', '#FF8000', '#FFFF00', '#00FF00', '#00FFFF',
  '#0000FF', '#800080', '#FF00FF', '#FFFFFF', '#000000',
];

const PRESET_HUE_MAP: { [key: string]: number } = {
  '#FF0000': 0, '#FF8000': 30, '#FFFF00': 60, '#00FF00': 120,
  '#00FFFF': 180, '#0000FF': 240, '#800080': 280, '#FF00FF': 300, '#FFFFFF': 0, '#000000': 0,
};

// ── Component ───────────────────────────────────────────────────────────────

const UniversalSlidersFooter = React.memo(function UniversalSlidersFooter(props: UniversalSlidersFooterProps) {
  const {
    activeMode, fixedSubMode, fixedColorMode, fixedModePattern,
    selectedColor, fixedFgColor, fixedBgColor, fixedHue,
    musicPrimaryColor, musicSecondaryColor, musicHue, musicSecondaryHue,
    selectedHue, musicColorFocus, streetCruiseColor,
    brightness, speed, micSensitivity, streetSensitivity,
    fixedPatternId, selectedPatternId, musicPatternId, musicMatrixStyle, micSource,
    multiColors, multiTransition,
    setSelectedColor, setFixedFgColor, setFixedBgColor, setFixedHue,
    setMusicPrimaryColor, setMusicSecondaryColor, setMusicHue, setMusicSecondaryHue,
    setSelectedHue, setMusicColorFocus, setFixedColorMode, setStreetCruiseColor,
    setBrightness, setSpeed, setMicSensitivity, setStreetSensitivity,
    fixedDirection, setFixedDirection,
    sendColor, applyFixedPattern, applyStaticModePattern, applyEmergencyPattern,
    applyStreetPattern, handleMusicChange, clampSpeed, brtFactor,
    writeToDevice, hwSettings, motionStateRef,
    styles,
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
                  if (activeMode === 'FIXED') applyStaticModePattern(fixedModePattern, r, g, b);
                  else if (activeMode === 'MULTIMODE' && fixedSubMode !== 'PATTERN') sendColor(r, g, b);
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
            return (
              <View style={{ flexDirection: 'row', width: '100%', height: 18, borderRadius: 9, marginBottom: Spacing.xs, borderWidth: 1, borderColor: 'rgba(255,255,255,0.4)', backgroundColor: 'transparent' }}>
                <TouchableOpacity
                  activeOpacity={0.9}
                  onPress={() => setMusicColorFocus('PRIMARY')}
                  style={{ flex: 1, backgroundColor: musicPrimaryColor, justifyContent: 'center', alignItems: 'center', opacity: musicColorFocus === 'PRIMARY' ? 1.0 : 0.4, borderTopLeftRadius: 8, borderBottomLeftRadius: 8, shadowColor: musicPrimaryColor, shadowOpacity: 1, shadowRadius: 16, shadowOffset: { width: 0, height: 0 }, elevation: 12 }}
                >
                  <Text style={{ color: '#FFFFFF', fontSize: 9, fontWeight: '900', letterSpacing: 1, textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 6 }}>SOUND COLUMN</Text>
                </TouchableOpacity>
                <TouchableOpacity
                  activeOpacity={0.9}
                  onPress={() => setMusicColorFocus('SECONDARY')}
                  style={{ flex: 1, backgroundColor: musicSecondaryColor, justifyContent: 'center', alignItems: 'center', opacity: musicColorFocus === 'SECONDARY' ? 1.0 : 0.4, borderLeftWidth: 1, borderLeftColor: 'rgba(255,255,255,0.4)', borderTopRightRadius: 8, borderBottomRightRadius: 8, shadowColor: musicSecondaryColor, shadowOpacity: 1, shadowRadius: 16, shadowOffset: { width: 0, height: 0 }, elevation: 12 }}
                >
                  <Text style={{ color: '#FFFFFF', fontSize: 9, fontWeight: '900', letterSpacing: 1, textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 6 }}>DROP COLOR</Text>
                </TouchableOpacity>
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
                          applyFixedPattern(fixedPatternId, newFg, newBg);
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
                      } else if (activeMode === 'FIXED') {
                        setSelectedColor(color);
                        if (PRESET_HUE_MAP[color] !== undefined) setSelectedHue(PRESET_HUE_MAP[color]);
                        const r = parseInt(color.slice(1, 3), 16);
                        const g = parseInt(color.slice(3, 5), 16);
                        const b = parseInt(color.slice(5, 7), 16);
                        applyStaticModePattern(fixedModePattern, r, g, b);
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
                      { backgroundColor: color, width: 20, height: 20, borderRadius: 10, shadowColor: color, shadowOpacity: 1, shadowRadius: 10, shadowOffset: { width: 0, height: 0 }, elevation: 8, margin: Spacing.xxs },
                      isActive && { borderWidth: 2, borderColor: '#FFF' },
                    ]}
                  />
                );
              })}
            </View>
          )}
        </View>
      )}

      {/* Hue Slider */}
      {!(activeMode === 'CAMERA') && (
        <View style={[styles.controlRow, { marginTop: Spacing.xs, marginBottom: Spacing.xs, flexShrink: 0, minHeight: 40 }]}>
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
                if (fixedSubMode === 'PATTERN') applyFixedPattern(fixedPatternId, fixedFgColor, fixedBgColor);
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
        </View>
      )}

      {/* TACTICAL UNIVERSAL SLIDERS SECTIONS (50/50 Split) */}
      <View style={{ flexDirection: 'row', gap: Spacing.sm, marginTop: Spacing.sm, marginBottom: Spacing.xs, minHeight: 44 }}>

        {/* LEFT SLOT: Brightness (standard) / Mic Sensitivity (music) / Brake Sensitivity (street) */}
        {!(activeMode === 'CAMERA') && !(activeMode === 'STREET') && !(activeMode === 'MUSIC') && (
          <TacticalSlider
            style={{ flex: 1 }}
            iconName="white-balance-sunny"
            label="BRIGHTNESS"
            fillColor="#00F0FF"
            dynamicMode="BRIGHTNESS"
            value={brightness}
            onValueChange={setBrightness}
            minimumValue={0}
            maximumValue={100}
            onSlidingComplete={(val: number) => {
              AppLogger.log('BRIGHTNESS_CHANGED', { value: val, mode: activeMode });
              if (writeToDevice) {
                if (activeMode === 'MULTIMODE' && fixedSubMode === 'PATTERN') {
                  applyFixedPattern(fixedPatternId, fixedFgColor, fixedBgColor, speed, val);
                } else {
                  const factor = brtFactor(val);
                  const hex = selectedColor;
                  const r = Math.round(parseInt(hex.slice(1, 3), 16) * factor);
                  const g = Math.round(parseInt(hex.slice(3, 5), 16) * factor);
                  const b = Math.round(parseInt(hex.slice(5, 7), 16) * factor);
                  sendColor(r, g, b);
                }
              }
            }}
          />
        )}

        {/* DIRECTION TOGGLE — shown only when active pattern supports it */}
        {activeMode === 'MULTIMODE' && fixedSubMode === 'PATTERN' && (() => {
          const effect = SK8LYTZ_TEMPLATES.find(e => e.id === fixedPatternId);
          if (!effect?.supportsDirection || !setFixedDirection) return null;
          const isForward = (fixedDirection ?? 1) === 1;
          return (
            <View style={{ flexDirection: 'row', alignSelf: 'flex-end', marginBottom: 2, borderRadius: 8, borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)', overflow: 'hidden' }}>
              <TouchableOpacity
                onPress={() => {
                  setFixedDirection(0);
                  if (applyFixedPattern) applyFixedPattern(fixedPatternId, fixedFgColor, fixedBgColor, speed);
                }}
                style={{ paddingHorizontal: 10, paddingVertical: 6, backgroundColor: !isForward ? 'rgba(0,240,255,0.2)' : 'transparent' }}
              >
                <Text style={{ color: !isForward ? '#00F0FF' : 'rgba(255,255,255,0.4)', fontSize: 10, fontWeight: '800' }}>◀ REV</Text>
              </TouchableOpacity>
              <View style={{ width: 1, backgroundColor: 'rgba(255,255,255,0.15)' }} />
              <TouchableOpacity
                onPress={() => {
                  setFixedDirection(1);
                  if (applyFixedPattern) applyFixedPattern(fixedPatternId, fixedFgColor, fixedBgColor, speed);
                }}
                style={{ paddingHorizontal: 10, paddingVertical: 6, backgroundColor: isForward ? 'rgba(0,240,255,0.2)' : 'transparent' }}
              >
                <Text style={{ color: isForward ? '#00F0FF' : 'rgba(255,255,255,0.4)', fontSize: 10, fontWeight: '800' }}>FWD ▶</Text>
              </TouchableOpacity>
            </View>
          );
        })()}

        {activeMode === 'MUSIC' && (
          <TacticalSlider
            style={{ flex: 1 }}
            iconName="microphone-outline"
            label="MIC SENSITIVITY"
            fillColor="#FF0055"
            value={micSensitivity}
            onValueChange={setMicSensitivity}
            minimumValue={0}
            maximumValue={100}
            onSlidingComplete={(val: number) => {
              AppLogger.log('MIC_SENSITIVITY_CHANGED', { value: val });
              handleMusicChange(musicPatternId, val, brightness, micSource, musicPrimaryColor, musicSecondaryColor, musicMatrixStyle);
            }}
          />
        )}

        {activeMode === 'STREET' && (
          <TacticalSlider
            style={{ flex: 1 }}
            iconName="octagon-outline"
            label="BRAKE SENSITIVITY"
            fillColor="#FF3300"
            value={streetSensitivity}
            onValueChange={setStreetSensitivity}
            minimumValue={5}
            maximumValue={95}
            onSlidingComplete={(val: number) => AppLogger.log('STREET_SENSITIVITY_CHANGED', { value: val })}
          />
        )}

        {/* RIGHT SLOT: Speed (standard) / Music uses both sensitivity + brightness */}
        {!(activeMode === 'MUSIC' || activeMode === 'CAMERA') && (
          <TacticalSlider
            style={{ flex: 1 }}
            iconName="engine-outline"
            label="SPEED"
            fillColor="#FF9900"
            dynamicMode="TURBO"
            value={speed}
            onValueChange={setSpeed}
            minimumValue={0}
            maximumValue={100}
            onSlidingComplete={(val: number) => {
              AppLogger.log('SPEED_CHANGED', { value: val, mode: activeMode });
              if (writeToDevice) {
                if (activeMode === 'MULTIMODE') {
                  if (fixedSubMode === 'PATTERN') {
                    applyFixedPattern(fixedPatternId, fixedFgColor, fixedBgColor, val);
                  } else if (fixedSubMode === 'BUILDER') {
                    const factor = brtFactor(brightness);
                    const rgbColors = multiColors.map(h => {
                      const rawR = Math.round((parseInt(h.slice(1, 3), 16) || 0) * factor);
                      const rawG = Math.round((parseInt(h.slice(3, 5), 16) || 0) * factor);
                      const rawB = Math.round((parseInt(h.slice(5, 7), 16) || 0) * factor);
                      return { r: rawR, g: rawG, b: rawB };
                    });
                    writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, clampSpeed(val), 1, multiTransition));
                  }
                } else if (activeMode === 'STREET') {
                  applyStreetPattern(motionStateRef.current, brightness, val);
                }
              }
            }}
          />
        )}

        {activeMode === 'MUSIC' && (
          <View style={{ flexDirection: 'row', width: '100%', gap: Spacing.sm }}>
            <TacticalSlider
              style={{ flex: 1 }}
              iconName="microphone-outline"
              label="SENSITIVITY"
              fillColor="#FF00FF"
              dynamicMode="SENSITIVITY"
              value={micSensitivity}
              onValueChange={setMicSensitivity}
              minimumValue={0}
              maximumValue={100}
              onSlidingComplete={(val: number) => {
                AppLogger.log('MIC_SENSITIVITY_CHANGED', { value: val, mode: activeMode });
                handleMusicChange(musicPatternId, val, brightness, micSource, musicPrimaryColor, musicSecondaryColor, musicMatrixStyle);
              }}
            />
            <TacticalSlider
              style={{ flex: 1 }}
              iconName="white-balance-sunny"
              label="BRIGHTNESS"
              fillColor="#00F0FF"
              dynamicMode="BRIGHTNESS"
              value={brightness}
              onValueChange={setBrightness}
              minimumValue={0}
              maximumValue={100}
              onSlidingComplete={(val: number) => {
                AppLogger.log('BRIGHTNESS_CHANGED', { value: val, mode: activeMode });
                handleMusicChange(musicPatternId, micSensitivity, val, micSource, musicPrimaryColor, musicSecondaryColor, musicMatrixStyle);
              }}
            />
          </View>
        )}
      </View>
    </View>
  );
});

export default UniversalSlidersFooter;

