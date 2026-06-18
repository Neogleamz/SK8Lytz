import { useRef, useState } from 'react';
import { normalizeMac } from './useDeviceStateLedger';
import type { DevicePatternState, FixedModePattern, ModeType } from '../types/dashboard.types';

/** Type guard: narrows an unknown value to ModeType. Replaces unsafe `as ModeType` casts. */
function isModeType(v: unknown): v is ModeType {
  return typeof v === 'string' && ['FAVORITES', 'MULTIMODE', 'BUILDER', 'MUSIC', 'STREET', 'CAMERA'].includes(v);
}

/** Opaque alias kept for backward-compatibility — represents a product catalog ID string. */
export type ProductType = string;

export interface BuilderNode {
  id: string;
  position: number;
  colorHex: string;
}

export interface CloudScenePayload {
  activeMode?: ModeType;
  fixedSubMode?: 'PATTERN' | 'BUILDER';
  fixedModePattern?: FixedModePattern;
  selectedColor?: string;
  selectedPatternId?: number;
  brightness?: number;
  speed?: number;
  multiColors?: string[];
  multiLength?: number;
  multiTransition?: number;
  musicPatternId?: number;
  musicPrimaryColor?: string;
  musicSecondaryColor?: string;
  micSensitivity?: number;
  micSource?: 'APP' | 'DEVICE';
  musicMatrixStyle?: number;
  streetSensitivity?: number;
  streetCruiseColor?: string;
  streetBrakeColor?: string;
}

export interface SpatialSegment {
  position: 'BACK' | 'FRONT' | 'ALL' | string;
  color: string;
}

export function useDockedControllerState(
  initialProduct: ProductType = 'HALOZ',
  /** Optional: synchronous ledger read function for pre-warming state on mount. */
  ledgerLoadSync?: (mac: string) => DevicePatternState | null,
  /** Optional: primary device MAC for ledger lookup. */
  mac?: string
) {
  // Resolved ONCE on mount via ref — drives lazy useState initializers below.
  // Using useRef instead of a plain variable ensures the ledger is NOT re-read
  // on every re-render (which would be wasted work since lazy useState only
  // uses the initializer value on the very first render anyway).
  const ledgerStateRef = useRef<DevicePatternState | null | 'uninitialized'>('uninitialized');
  if (ledgerStateRef.current === 'uninitialized') {
    ledgerStateRef.current = (mac && ledgerLoadSync) ? ledgerLoadSync(normalizeMac(mac)) : null;
  }
  const ledgerState = ledgerStateRef.current;
  // Core State
  const [activeProduct, setActiveProduct] = useState<ProductType>(initialProduct);
  // Pre-warm from ledger — restores the mode the device was last set to so the
  // visualizer shows the correct pattern immediately on controller open.
  const restoredMode: ModeType = isModeType(ledgerState?.mode) ? ledgerState.mode : 'FAVORITES';
  const [activeMode, setActiveMode] = useState<ModeType>(restoredMode);
  const [lastOperatingMode, setLastOperatingMode] = useState<ModeType>(
    restoredMode !== 'FAVORITES' ? restoredMode : 'MULTIMODE'
  );

  // Shared parameters — lazy initializers read from ledger on first render
  const [selectedColor, setSelectedColor] = useState<string>(
    () => ledgerState?.fgColor ?? '#00F0FF'
  );
  const [selectedHue, setSelectedHue] = useState<number>(180);
  const [selectedPatternId, setSelectedPatternId] = useState<number>(
    () => ledgerState?.patternId ?? 1
  );
  const [brightness, setBrightness] = useState<number>(
    () => ledgerState?.brightness ?? 90
  );
  const [speed, setSpeed] = useState<number>(
    () => ledgerState?.speed ?? 50
  );

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
  const [micSource, setMicSource] = useState<'APP' | 'DEVICE'>('DEVICE');
  const [musicMatrixStyle, setMusicMatrixStyle] = useState<number>(39);
  const [musicSecondaryHue, setMusicSecondaryHue] = useState<number>(300);
  const [musicPrimaryColor, setMusicPrimaryColor] = useState<string>('#FF00FF');
  const [musicSecondaryColor, setMusicSecondaryColor] = useState<string>('#00FFFF');
  const [musicColorFocus, setMusicColorFocus] = useState<'PRIMARY' | 'SECONDARY'>('PRIMARY');
  const [musicSetting, setMusicSetting] = useState<'SENSITIVITY' | 'BRIGHTNESS'>('SENSITIVITY');

  // Fixed Pattern parameters (PRO Effects) — lazy init from ledger
  const [fixedPatternId, setFixedPatternId] = useState<number>(
    () => ledgerState?.patternId ?? 1
  );
  const [fixedColorMode, setFixedColorMode] = useState<'FOREGROUND' | 'BACKGROUND'>('FOREGROUND');
  const [fixedFgColor, setFixedFgColor] = useState<string>(
    () => ledgerState?.fgColor ?? '#00FF00'
  );
  const [fixedBgColor, setFixedBgColor] = useState<string>(
    () => ledgerState?.bgColor ?? '#000000'
  );
  const [fixedHue, setFixedHue] = useState<number>(120);
  const [fixedDirection, setFixedDirection] = useState<number>(1); // 1=forward, 0=reverse

  // Cloud Scene UI States
  const [isCommunityModalVisible, setIsCommunityModalVisible] = useState<boolean>(false);
  type PublishStatus = 'idle' | 'publishing' | 'error' | 'success';
  const [publishStatus, setPublishStatus] = useState<PublishStatus>('idle');
  const isPublishingCloud = publishStatus === 'publishing';
  const [cloudPublicToggle, setCloudPublicToggle] = useState<boolean>(true);

  const applyCloudScene = (
    scenePayload: CloudScenePayload,
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

  type CapturedControllerState = {
    activeMode: ModeType; fixedSubMode: 'PATTERN' | 'BUILDER'; fixedModePattern: FixedModePattern;
    selectedColor: string; selectedPatternId: number; brightness: number; speed: number;
    multiColors: string[]; multiLength: number; multiTransition: number;
    musicPatternId: number; musicPrimaryColor: string; musicSecondaryColor: string;
    micSensitivity: number; micSource: 'APP' | 'DEVICE'; musicMatrixStyle: number;
    streetSensitivity: number; streetCruiseColor: string; streetBrakeColor: string;
    [key: string]: unknown;
  };

  const captureEntireState = (
    streetSensitivity: number,
    streetCruiseColor: string,
    streetBrakeColor: string,
    override?: Partial<CapturedControllerState>
  ): CapturedControllerState => {
    return {
      activeMode, fixedSubMode, fixedModePattern,
      selectedColor, selectedPatternId, brightness, speed,
      multiColors, multiLength, multiTransition,
      musicPatternId, musicPrimaryColor, musicSecondaryColor, micSensitivity, micSource, musicMatrixStyle,
      streetSensitivity, streetCruiseColor, streetBrakeColor,
      ...(override || {})
    };
  };

  const applySpatialSegments = (segments: SpatialSegment[]) => {
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
        id: `voice_${idx}_${seg.position}`,
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
    fixedDirection, setFixedDirection,
    isCommunityModalVisible, setIsCommunityModalVisible,
    isPublishingCloud, setIsPublishingCloud: (v: boolean) => setPublishStatus(v ? 'publishing' : 'idle'),
    publishStatus, setPublishStatus,
    cloudPublicToggle, setCloudPublicToggle,
    applyCloudScene,
    captureEntireState,
    applySpatialSegments
  };
}
