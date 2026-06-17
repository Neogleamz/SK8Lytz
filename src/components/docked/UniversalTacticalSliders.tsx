import React from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import { Spacing } from '../../theme/theme';
import TacticalSlider from '../TacticalSlider';
import { PositionalMathBuffer } from '../../protocols/PositionalMathBuffer';
import { AppLogger } from '../../services/appLogger';
import { SK8LYTZ_TEMPLATES } from '../../protocols/PatternEngine';
import type { UniversalSlidersFooterProps } from './UniversalSlidersFooter';
// R-29: Safe type-only import, compiles away at runtime.

export const UniversalTacticalSliders = React.memo(function UniversalTacticalSliders(props: UniversalSlidersFooterProps) {
  const {
    activeMode, cameraSubMode, cameraVibePalette,
    fixedSubMode,
    fixedPatternId, fixedFgColor, fixedBgColor, fixedDirection, setFixedDirection,
    musicPatternId, micSensitivity, setMicSensitivity, micSource,
    musicPrimaryColor, musicSecondaryColor, musicMatrixStyle,
    streetSensitivity, setStreetSensitivity,
    brightness, setBrightness,
    speed, setSpeed,
    builderNodes, builderFillMode, builderTransitionType, builderDirection, multiColors, multiTransition,
    hwSettings,
    applyFixedPattern, applyStreetPattern, handleMusicChange, writeToDevice,
    motionStateRef, sendColor, selectedColor, setMultiColor
  } = props;

  const brtFactor = (b: number) => Math.max(1, Math.min(100, Math.round(b))) / 100;
  const clampSpeed = (s: number) => Math.max(1, Math.min(100, Math.round(s)));

  return (
    <>
      {!(activeMode === 'BUILDER' && props.isBuildingCustom) && (
        <View style={{ flexDirection: 'row', gap: Spacing.sm, marginTop: Spacing.sm, marginBottom: Spacing.xs, minHeight: 44 }}>

        {/* LEFT SLOT: Brightness (standard) / Mic Sensitivity (music) / Brake Sensitivity (street) */}
        {!(activeMode === 'STREET') && !(activeMode === 'MUSIC') && (
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
                  applyFixedPattern(fixedPatternId, fixedFgColor, fixedBgColor, speed, val, fixedDirection);
                } else if ((activeMode === 'MULTIMODE' && fixedSubMode === 'BUILDER') || activeMode === 'BUILDER') {
                  const factor = brtFactor(val);
                  if (builderNodes && builderNodes.length > 0) {
                    const ledPoints = hwSettings?.ledPoints || 16;
                    const generatedRgbArray = PositionalMathBuffer.generateArray(
                      builderNodes,
                      ledPoints,
                      builderFillMode === 'GRADIENT'
                    );
                    const scaledRgb = generatedRgbArray.map(c => ({
                      r: Math.round(c.r * factor),
                      g: Math.round(c.g * factor),
                      b: Math.round(c.b * factor),
                    }));
                    const transition = builderTransitionType ?? 1;
                    const dir = builderDirection ?? 1;
                    setMultiColor?.(scaledRgb, ledPoints, Math.round(speed), dir, transition)?.catch((e: Error) => AppLogger.error('setMultiColor error', e, { payload_size: 0, ssi: 0 }));
                  } else {
                    const rgbColors = multiColors.map(h => {
                      const rawR = Math.round((parseInt(h.slice(1, 3), 16) || 0) * factor);
                      const rawG = Math.round((parseInt(h.slice(3, 5), 16) || 0) * factor);
                      const rawB = Math.round((parseInt(h.slice(5, 7), 16) || 0) * factor);
                      return { r: rawR, g: rawG, b: rawB };
                    });
                    setMultiColor?.(rgbColors, hwSettings?.ledPoints || 12, clampSpeed(speed), 1, multiTransition)?.catch((e: Error) => AppLogger.error('setMultiColor error', e, { payload_size: 0, ssi: 0 }));
                  }
                } else if (activeMode === 'CAMERA' && cameraSubMode === 'VIBE') {
                  const factor = brtFactor(val);
                  if (cameraVibePalette && cameraVibePalette.length > 0) {
                    const rgbColors = cameraVibePalette.map(h => {
                      const rawR = Math.round((parseInt(h.slice(1, 3), 16) || 0) * factor);
                      const rawG = Math.round((parseInt(h.slice(3, 5), 16) || 0) * factor);
                      const rawB = Math.round((parseInt(h.slice(5, 7), 16) || 0) * factor);
                      return { r: rawR, g: rawG, b: rawB };
                    });
                    setMultiColor?.(rgbColors, hwSettings?.ledPoints || 12, clampSpeed(speed), 1, 1)?.catch((e: Error) => AppLogger.error('setMultiColor error', e, { payload_size: 0, ssi: 0 }));
                  }
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
            <View style={{ flexDirection: 'column', alignSelf: 'stretch', width: 46, borderRadius: 8, borderWidth: 1, borderColor: 'rgba(255,255,255,0.15)', overflow: 'hidden' }}>
              <TouchableOpacity
                onPress={() => { setFixedDirection?.(0); applyFixedPattern(fixedPatternId, fixedFgColor, fixedBgColor, speed, brightness, 0); }}
                style={{ flex: 1, alignItems: 'center', justifyContent: 'center', backgroundColor: !isForward ? 'rgba(0,240,255,0.2)' : 'transparent' }}
              >
                <Text style={{ color: !isForward ? '#00F0FF' : 'rgba(255,255,255,0.35)', fontSize: 9, fontWeight: '800', lineHeight: 11 }}>▼</Text>
                <Text style={{ color: !isForward ? '#00F0FF' : 'rgba(255,255,255,0.35)', fontSize: 7, fontWeight: '800', lineHeight: 10 }}>REV</Text>
              </TouchableOpacity>
              <View style={{ height: 1, backgroundColor: 'rgba(255,255,255,0.15)' }} />
              <TouchableOpacity
                onPress={() => { setFixedDirection?.(1); applyFixedPattern(fixedPatternId, fixedFgColor, fixedBgColor, speed, brightness, 1); }}
                style={{ flex: 1, alignItems: 'center', justifyContent: 'center', backgroundColor: isForward ? 'rgba(0,240,255,0.2)' : 'transparent' }}
              >
                <Text style={{ color: isForward ? '#00F0FF' : 'rgba(255,255,255,0.35)', fontSize: 9, fontWeight: '800', lineHeight: 11 }}>▲</Text>
                <Text style={{ color: isForward ? '#00F0FF' : 'rgba(255,255,255,0.35)', fontSize: 7, fontWeight: '800', lineHeight: 10 }}>FWD</Text>
              </TouchableOpacity>
            </View>
          );
        })()}


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
        {!(activeMode === 'MUSIC' || (activeMode === 'CAMERA' && cameraSubMode !== 'VIBE')) && (
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
                if (activeMode === 'MULTIMODE' && fixedSubMode === 'PATTERN') {
                  applyFixedPattern(fixedPatternId, fixedFgColor, fixedBgColor, val, brightness, fixedDirection);
                } else if ((activeMode === 'MULTIMODE' && fixedSubMode === 'BUILDER') || activeMode === 'BUILDER') {
                  const factor = brtFactor(brightness);
                  if (builderNodes && builderNodes.length > 0) {
                    const ledPoints = hwSettings?.ledPoints || 16;
                    const generatedRgbArray = PositionalMathBuffer.generateArray(
                      builderNodes,
                      ledPoints,
                      builderFillMode === 'GRADIENT'
                    );
                    const scaledRgb = generatedRgbArray.map(c => ({
                      r: Math.round(c.r * factor),
                      g: Math.round(c.g * factor),
                      b: Math.round(c.b * factor),
                    }));
                    const transition = builderTransitionType ?? 1;
                    const dir = builderDirection ?? 1;
                    setMultiColor?.(scaledRgb, ledPoints, Math.round(val), dir, transition)?.catch((e: Error) => AppLogger.error('setMultiColor error', e, { payload_size: 0, ssi: 0 }));
                  } else {
                    const rgbColors = multiColors.map(h => {
                      const rawR = Math.round((parseInt(h.slice(1, 3), 16) || 0) * factor);
                      const rawG = Math.round((parseInt(h.slice(3, 5), 16) || 0) * factor);
                      const rawB = Math.round((parseInt(h.slice(5, 7), 16) || 0) * factor);
                      return { r: rawR, g: rawG, b: rawB };
                    });
                    setMultiColor?.(rgbColors, hwSettings?.ledPoints || 12, clampSpeed(val), 1, multiTransition)?.catch((e: Error) => AppLogger.error('setMultiColor error', e, { payload_size: 0, ssi: 0 }));
                  }
                } else if (activeMode === 'CAMERA' && cameraSubMode === 'VIBE') {
                  const factor = brtFactor(brightness);
                  if (cameraVibePalette && cameraVibePalette.length > 0) {
                    const rgbColors = cameraVibePalette.map(h => {
                      const rawR = Math.round((parseInt(h.slice(1, 3), 16) || 0) * factor);
                      const rawG = Math.round((parseInt(h.slice(3, 5), 16) || 0) * factor);
                      const rawB = Math.round((parseInt(h.slice(5, 7), 16) || 0) * factor);
                      return { r: rawR, g: rawG, b: rawB };
                    });
                    setMultiColor?.(rgbColors, hwSettings?.ledPoints || 12, clampSpeed(val), 1, 1)?.catch((e: Error) => AppLogger.error('setMultiColor error', e, { payload_size: 0, ssi: 0 }));
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
          </View>
        )}
      </View>
    )}
    </>
  );
});

export default UniversalTacticalSliders;
