import { useState } from 'react';
import type { FixedModePattern, ModeType } from '../types/dashboard.types';

/** Opaque alias kept for backward-compatibility — represents a product catalog ID string. */
export type ProductType = string;

export interface BuilderNode {
  id: string;
  position: number;
  colorHex: string;
}

export function useDockedControllerState(initialProduct: ProductType = 'HALOZ') {
  // Core State
  const [activeProduct, setActiveProduct] = useState<ProductType>(initialProduct);
  const [activeMode, setActiveMode] = useState<ModeType>('FAVORITES');
  const [lastOperatingMode, setLastOperatingMode] = useState<ModeType>('MULTIMODE');

  // Shared parameters
  const [selectedColor, setSelectedColor] = useState<string>('#00F0FF');
  const [selectedHue, setSelectedHue] = useState<number>(180);
  const [selectedPatternId, setSelectedPatternId] = useState<number>(1);
  const [brightness, setBrightness] = useState<number>(90);
  const [speed, setSpeed] = useState<number>(50);

  // Audio / Mic parameters (Global)
  const [micSensitivity, setMicSensitivity] = useState<number>(80);
  const [musicHue, setMusicHue] = useState<number>(180);

  // Multi-Color DIY State
  const [multiColors, setMultiColors] = useState<string[]>(['#FF0000', '#00FF00', '#0000FF']);
  const [multiTransition, setMultiTransition] = useState<number>(3);
  const [multiLength, setMultiLength] = useState<number>(16);

  // Active Sub-Mode for the Consolidated Fixed Tab
  const [fixedSubMode, setFixedSubMode] = useState<'PATTERN' | 'BUILDER'>('PATTERN');
  const [fixedModePattern, setFixedModePattern] = useState<FixedModePattern>('STATIC');

  // Multi-Color Builder State
  const [builderNodes, setBuilderNodes] = useState<BuilderNode[]>([
    { id: 'node_1', position: 0, colorHex: '#FF0000' },
    { id: 'node_2', position: 100, colorHex: '#00F0FF' }
  ]);
  const [builderFillMode, setBuilderFillMode] = useState<'GRADIENT' | 'SOLID'>('GRADIENT');
  const [builderTransitionType, setBuilderTransitionType] = useState<number>(1);
  const [builderDirection, setBuilderDirection] = useState<number>(1);

  // Music mode parameters
  const [musicPatternId, setMusicPatternId] = useState<number>(1);
  const [micSource, setMicSource] = useState<'APP' | 'DEVICE'>('APP');
  const [musicMatrixStyle, setMusicMatrixStyle] = useState<number>(39);
  const [musicSecondaryHue, setMusicSecondaryHue] = useState<number>(300);
  const [musicPrimaryColor, setMusicPrimaryColor] = useState<string>('#FF00FF');
  const [musicSecondaryColor, setMusicSecondaryColor] = useState<string>('#00FFFF');
  const [musicColorFocus, setMusicColorFocus] = useState<'PRIMARY' | 'SECONDARY'>('PRIMARY');
  const [musicSetting, setMusicSetting] = useState<'SENSITIVITY' | 'BRIGHTNESS'>('SENSITIVITY');

  // Fixed Pattern parameters (PRO Effects)
  const [fixedPatternId, setFixedPatternId] = useState<number>(1);
  const [fixedColorMode, setFixedColorMode] = useState<'FOREGROUND' | 'BACKGROUND'>('FOREGROUND');
  const [fixedFgColor, setFixedFgColor] = useState<string>('#00FF00');
  const [fixedBgColor, setFixedBgColor] = useState<string>('#000000');
  const [fixedHue, setFixedHue] = useState<number>(120);

  // Cloud Scene UI States
  const [isCommunityModalVisible, setIsCommunityModalVisible] = useState<boolean>(false);
  const [isPublishingCloud, setIsPublishingCloud] = useState<boolean>(false);
  const [cloudPublicToggle, setCloudPublicToggle] = useState<boolean>(true);

  const applyCloudScene = (
    scenePayload: any,
    setStreetSensitivity: (v: number) => void,
    setStreetCruiseColor: (v: string) => void,
    setStreetBrakeColor: (v: string) => void
  ) => {
    if (scenePayload.activeMode) setActiveMode(scenePayload.activeMode);
    if (scenePayload.fixedSubMode) setFixedSubMode(scenePayload.fixedSubMode);
    if (scenePayload.fixedModePattern) setFixedModePattern(scenePayload.fixedModePattern);
    if (scenePayload.selectedColor) setSelectedColor(scenePayload.selectedColor);
    if (scenePayload.selectedPatternId) setSelectedPatternId(scenePayload.selectedPatternId);
    if (scenePayload.brightness !== undefined) setBrightness(scenePayload.brightness);
    if (scenePayload.speed !== undefined) setSpeed(scenePayload.speed);
    if (scenePayload.multiColors) setMultiColors(scenePayload.multiColors);
    if (scenePayload.multiLength !== undefined) setMultiLength(scenePayload.multiLength);
    if (scenePayload.multiTransition !== undefined) setMultiTransition(scenePayload.multiTransition);
    if (scenePayload.musicPatternId !== undefined) setMusicPatternId(scenePayload.musicPatternId);
    if (scenePayload.musicPrimaryColor) setMusicPrimaryColor(scenePayload.musicPrimaryColor);
    if (scenePayload.musicSecondaryColor) setMusicSecondaryColor(scenePayload.musicSecondaryColor);
    if (scenePayload.micSensitivity !== undefined) setMicSensitivity(scenePayload.micSensitivity);
    if (scenePayload.micSource !== undefined) setMicSource(scenePayload.micSource);
    if (scenePayload.musicMatrixStyle !== undefined) setMusicMatrixStyle(scenePayload.musicMatrixStyle);
    if (scenePayload.streetSensitivity !== undefined) setStreetSensitivity(scenePayload.streetSensitivity);
    if (scenePayload.streetCruiseColor) setStreetCruiseColor(scenePayload.streetCruiseColor);
    if (scenePayload.streetBrakeColor) setStreetBrakeColor(scenePayload.streetBrakeColor);
  };

  const captureEntireState = (
    streetSensitivity: number,
    streetCruiseColor: string,
    streetBrakeColor: string
  ) => {
    return {
      activeMode, fixedSubMode, fixedModePattern,
      selectedColor, selectedPatternId, brightness, speed,
      multiColors, multiLength, multiTransition,
      musicPatternId, musicPrimaryColor, musicSecondaryColor, micSensitivity, micSource, musicMatrixStyle,
      streetSensitivity, streetCruiseColor, streetBrakeColor
    };
  };

  const applySpatialSegments = (segments: any[]) => {
    setActiveMode('MULTIMODE');
    setFixedSubMode('BUILDER');

    const newNodes: BuilderNode[] = segments.map((seg, idx) => {
      let pos = 50;
      if (seg.position === 'BACK') pos = 0;
      if (seg.position === 'FRONT') pos = 100;
      if (seg.position === 'ALL') {
        return { id: `voice_${idx}`, position: 0, colorHex: seg.color };
      }

      return {
        id: `voice_${idx}_${Date.now()}`,
        position: pos,
        colorHex: seg.color || '#FFFFFF'
      };
    });

    setBuilderNodes(newNodes);
  };

  return {
    activeProduct, setActiveProduct,
    activeMode, setActiveMode,
    lastOperatingMode, setLastOperatingMode,
    selectedColor, setSelectedColor,
    selectedHue, setSelectedHue,
    selectedPatternId, setSelectedPatternId,
    brightness, setBrightness,
    speed, setSpeed,
    micSensitivity, setMicSensitivity,
    musicHue, setMusicHue,
    multiColors, setMultiColors,
    multiTransition, setMultiTransition,
    multiLength, setMultiLength,
    fixedSubMode, setFixedSubMode,
    fixedModePattern, setFixedModePattern,
    builderNodes, setBuilderNodes,
    builderFillMode, setBuilderFillMode,
    builderTransitionType, setBuilderTransitionType,
    builderDirection, setBuilderDirection,
    musicPatternId, setMusicPatternId,
    musicPrimaryColor, setMusicPrimaryColor,
    musicSecondaryColor, setMusicSecondaryColor,
    musicColorFocus, setMusicColorFocus,
    musicSecondaryHue, setMusicSecondaryHue,
    musicSetting, setMusicSetting,
    micSource, setMicSource,
    musicMatrixStyle, setMusicMatrixStyle,
    fixedPatternId, setFixedPatternId,
    fixedColorMode, setFixedColorMode,
    fixedFgColor, setFixedFgColor,
    fixedBgColor, setFixedBgColor,
    fixedHue, setFixedHue,
    isCommunityModalVisible, setIsCommunityModalVisible,
    isPublishingCloud, setIsPublishingCloud,
    cloudPublicToggle, setCloudPublicToggle,
    applyCloudScene,
    captureEntireState,
    applySpatialSegments
  };
}
