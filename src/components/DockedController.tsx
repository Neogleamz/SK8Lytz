/**
 * DockedController.tsx — SK8Lytz Primary LED Control Interface
 *
 * The main user-facing control panel for LED mode management.
 * Renders as a bottom sheet overlay on DashboardScreen.
 *
 * Responsibilities:
 *  - Mode switching: MultiMode, Music, Camera, Pattern (RBM), Street (Accelerometer), DIY Array
 *  - Color picker, RGB sliders, brightness & speed knobs
 *  - Pattern wheel (ArcPatternWheel / VerticalPatternDrum)
 *  - Music mic source controls (APP MIC / DEVICE MIC)
 *  - Favorites system and Quick Presets
 *  - Per-device and group analytics telemetry (MODE_CHANGED, PATTERN_CHANGED, COLOR_CHANGED)
 *
 * Depends on: ZenggeProtocol, AppLogger, useBLE (via prop injection), ThemeContext
 * Platform: React Native (Android + Web)
 */
import React, { useState, useRef, useEffect } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, ScrollView, Platform, Modal, TextInput, Animated, Alert } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { Typography, Layout } from '../theme/theme';
import { Audio } from 'expo-av';
import { useTheme } from '../context/ThemeContext';
import ProductVisualizer from './ProductVisualizer';
import NeonHueStrip from './NeonHueStrip';
import TacticalSlider from './TacticalSlider';
import VerticalPatternDrum from './VerticalPatternDrum';
import CameraTracker from './CameraTracker';
import { getRbmPatternName } from '../constants/RbmPatterns';
import { ZENGGE_EFFECTS } from '../constants/CustomEffects';
import CustomEffectVisualizer from './CustomEffectVisualizer';

import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { PositionalMathBuffer, BuilderNode } from '../protocols/PositionalMathBuffer';
import PositionalGradientBuilder from './PositionalGradientBuilder';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { MaterialCommunityIcons } from '@expo/vector-icons';
import { AppLogger } from '../services/AppLogger';
import { supabase } from '../services/supabaseClient';
import { ScenesService } from '../services/ScenesService';
import { containsProfanity } from '../services/AuthUtils';
import CommunityModal from './CommunityModal';
import { Accelerometer } from 'expo-sensors';
import * as Location from 'expo-location';
import Svg, { Path, Circle, Defs, LinearGradient as SvgLinearGradient, Stop } from 'react-native-svg';
import MarqueeText from './MarqueeText';

type MotionState = 'STOPPED' | 'ACCELERATING' | 'CRUISING' | 'SLOWING_DOWN' | 'HARD_BRAKING';

const AnalogGauge = ({
  value,
  min,
  max,
  label,
  unit = '',
  defaultColor = '#00F0FF',
  size = 140,
  dangerVal,
  criticalVal
}: { value: number, min: number, max: number, label: string, unit?: string, defaultColor?: string, size?: number, dangerVal?: number, criticalVal?: number }) => {
  const radius = size * 0.42;
  const center = size / 2;
  const angleRange = 260;
  const startAngle = -220; // Starts bottom-left

  const clampedVal = Math.min(Math.max(value, min), max);
  const percent = (clampedVal - min) / (max - min);
  const currentAngle = startAngle + (percent * angleRange);

  let activeColor = defaultColor;
  if (criticalVal !== undefined && clampedVal >= criticalVal) activeColor = '#FF0000';
  else if (dangerVal !== undefined && clampedVal >= dangerVal) activeColor = '#FF8C00';

  const polarToCartesian = (centerX: number, centerY: number, r: number, angleInDegrees: number) => {
    const angleInRadians = (angleInDegrees - 90) * Math.PI / 180.0;
    return { x: centerX + (r * Math.cos(angleInRadians)), y: centerY + (r * Math.sin(angleInRadians)) };
  };

  const describeArc = (x: number, y: number, r: number, sAngle: number, eAngle: number) => {
    // If start closely matches end, return nothing
    if (Math.abs(eAngle - sAngle) < 0.1) return "";
    const start = polarToCartesian(x, y, r, eAngle);
    const end = polarToCartesian(x, y, r, sAngle);
    const largeArcFlag = eAngle - sAngle <= 180 ? "0" : "1";
    return ["M", start.x, start.y, "A", r, r, 0, largeArcFlag, 0, end.x, end.y].join(" ");
  };

  const trackPath = describeArc(center, center, radius, startAngle, startAngle + angleRange);
  const fillPath = describeArc(center, center, radius, startAngle, currentAngle);

  let dangerPath = "";
  let criticalPath = "";
  if (dangerVal !== undefined && criticalVal !== undefined) {
    const dPercent = Math.max(0, Math.min(1, (dangerVal - min) / (max - min)));
    const cPercent = Math.max(0, Math.min(1, (criticalVal - min) / (max - min)));
    const dAngle = startAngle + (dPercent * angleRange);
    const cAngle = startAngle + (cPercent * angleRange);
    dangerPath = describeArc(center, center, radius, dAngle, cAngle);
    criticalPath = describeArc(center, center, radius, cAngle, startAngle + angleRange);
  }

  // Tick marks
  const numTicks = 8;
  const ticks = Array.from({ length: numTicks + 1 }).map((_, i) => {
    const p = i / numTicks;
    const a = startAngle + (p * angleRange);
    const rad = (a - 90) * Math.PI / 180;
    const isMajor = i % 2 === 0;
    const innerRadius = radius - (isMajor ? 8 : 4);
    return {
      x1: center + radius * Math.cos(rad),
      y1: center + radius * Math.sin(rad),
      x2: center + innerRadius * Math.cos(rad),
      y2: center + innerRadius * Math.sin(rad),
      isMajor
    };
  });

  return (
    <View style={{ alignItems: 'center', marginHorizontal: 2 }}>
      <View style={{ width: size, height: size, justifyContent: 'center', alignItems: 'center' }}>
        <Svg width={size} height={size}>
          <Defs>
            <SvgLinearGradient id="grad" x1="0" y1="1" x2="1" y2="0">
              <Stop offset="0" stopColor={activeColor} stopOpacity="1" />
              <Stop offset="1" stopColor={activeColor} stopOpacity="0.4" />
            </SvgLinearGradient>
          </Defs>
          {/* Background track */}
          <Path d={trackPath} stroke="rgba(255,255,255,0.08)" strokeWidth={10} fill="none" strokeDasharray="6 4" strokeLinecap="butt" />

          {dangerPath ? <Path d={dangerPath} stroke="rgba(255,140,0,0.3)" strokeWidth={10} fill="none" strokeDasharray="6 4" /> : null}
          {criticalPath ? <Path d={criticalPath} stroke="rgba(255,0,0,0.35)" strokeWidth={10} fill="none" strokeDasharray="6 4" /> : null}

          {/* Active fill */}
          {fillPath ? <Path d={fillPath} stroke="url(#grad)" strokeWidth={10} fill="none" strokeDasharray="6 4" strokeLinecap="butt" /> : null}

          {/* Ticks (optional, but let's keep them very faint outside the dashed ring) */}
          {ticks.map((tick, i) => (
            <Path key={i} d={`M ${tick.x1} ${tick.y1} L ${tick.x2} ${tick.y2}`} stroke={tick.isMajor ? "rgba(255,255,255,0.4)" : "rgba(255,255,255,0.15)"} strokeWidth={tick.isMajor ? 2 : 1} />
          ))}

          {/* Center Hub */}
          <Circle cx={center} cy={center} r={6} fill="#222" stroke="rgba(255,255,255,0.2)" strokeWidth={2} />
        </Svg>

        {/* Animated Needle */}
        <View style={{ position: 'absolute', width: size, height: size, justifyContent: 'center', alignItems: 'center', transform: [{ rotate: `${currentAngle}deg` }] }}>
          <View style={{
            width: 4, height: radius * 0.90,
            backgroundColor: '#FF8C00',
            position: 'absolute',
            top: center - (radius * 0.90),
            borderTopLeftRadius: 2,
            borderTopRightRadius: 2,
            shadowColor: '#FF8C00',
            shadowOpacity: 1,
            shadowRadius: 10,
            elevation: 8
          }} />
        </View>

        {/* Digital display */}
        <View style={{ position: 'absolute', right: size * 0.15, top: size * 0.32, alignItems: 'flex-end' }}>
          <Text style={{ color: '#FFF', fontSize: size * 0.22, fontWeight: '900', fontVariant: ['tabular-nums'], textShadowColor: activeColor !== '#00F0FF' ? activeColor : '#00F0FF', textShadowRadius: 16 }}>{Math.floor(value)}</Text>
          {unit ? <Text style={{ color: 'rgba(255,255,255,0.5)', fontSize: size * 0.08, fontWeight: '800', marginTop: -4 }}>{unit}</Text> : null}
        </View>
      </View>
      <Text style={{ color: 'rgba(255,255,255,0.4)', fontSize: 11, fontWeight: '900', letterSpacing: 2, marginTop: -4 }}>{label}</Text>
    </View>
  );
};



type ProductType = 'HALOZ' | 'SOULZ';
type ModeType = 'FAVORITES' | 'MULTIMODE' | 'PROGRAMS' | 'MUSIC' | 'STREET' | 'CAMERA';

const MUSIC_PATTERNS = [
  'Soft',
  'Cheerful',
  'Energy',
  'Relax',
  'Passion',
  'Brisk',
  'Rhythm',
  'Rolling',
  'Flicker',
  'Accumulation',
  'Shuttle',
  'Fireworks',
  'Snow'
];

const FixedPatternPreviewRow = ({ baseDots, patternId, speed, points = 16, segments = 1 }: { baseDots: string[], patternId: number, speed: number, points?: number, segments?: number }) => {
  const [offset, setOffset] = React.useState(0);

  // Extend the 8-element static base array explicitly to match physical point counts natively
  const fullArray = React.useMemo(() => {
    const arr: string[] = [];

    // Hardware repeats the geometric bounds across the segment boundary safely
    const dotsPerSegment = Math.max(1, Math.floor(points / Math.max(1, segments)));

    for (let i = 0; i < points; i++) {
      // Map native segment repetitions safely simulating string data
      const segmentLocalIndex = i % dotsPerSegment;
      arr.push(baseDots[segmentLocalIndex % baseDots.length]);
    }
    return arr;
  }, [baseDots, points, segments]);

  React.useEffect(() => {
    const intervalTime = Math.max(30, 200 - (speed * 1.7));
    const int = setInterval(() => {
      setOffset(o => (o + 1) % fullArray.length);
    }, intervalTime);
    return () => clearInterval(int);
  }, [fullArray.length, speed]);

  const displayedDots = React.useMemo(() => {
    return [...fullArray.slice(fullArray.length - offset), ...fullArray.slice(0, fullArray.length - offset)];
  }, [fullArray, offset]);

  return (
    <View style={{ flex: 1, marginRight: 8, height: 8, overflow: 'hidden' }}>
      <View style={{ flex: 1, flexDirection: 'row', gap: 2 }}>
        {displayedDots.slice(0, 10).map((c, i) => (
          <View
            key={i}
            style={{
              flex: 1,
              borderRadius: 4,
              backgroundColor: c
            }}
          />
        ))}
      </View>
    </View>
  );
};


export interface IDeviceState {
  id: string;
  name: string;
  points?: number;
  segments?: number;
  sorting?: 'RGB' | 'GRB' | 'BRG' | 'RBG' | 'BGR' | 'GBR';
  [key: string]: any; // safe loose fallback for undocumented BLE peripheral keys
}

export interface IFavoriteState {
  id: string;
  name: string;
  customName?: string;
  mode: string;
  color?: string;
  patternId?: number;
  speed: number;
  brightness: number;
  fixedColorMode?: 'FOREGROUND' | 'BACKGROUND';
  fixedFgColor?: string;
  fixedBgColor?: string;
  fixedHue?: number;
  multiColors?: string[];
  multiTransition?: number;
  multiLength?: number;
  musicPrimaryColor?: string;
  musicSecondaryColor?: string;
  micSensitivity?: number;
  micSource?: 'APP' | 'DEVICE';
  musicMatrixStyle?: number;
}

export interface IQuickPreset {
  name: string;
  colors: string[];
  type: number;
}

interface Sk8lytzControllerProps {
  hwSettings?: any;
  lockedProduct?: ProductType;
  isPaired?: boolean;
  points?: number;
  devices?: IDeviceState[];
  onLongPressDevice?: (device: IDeviceState) => void;
  writeToDevice?: (payload: number[]) => Promise<void>;
  isPoweredOn?: boolean;
  onDisconnect?: () => void;
  /** 'leader' = broadcast changes, 'member' = receive changes, null = solo */
  crewRole?: 'leader' | 'member' | null;
  /** Called with full scene snapshot whenever any mode/color changes (leader only) */
  onCrewSceneChange?: (scene: Record<string, any>) => void;
}

export type DockedControllerHandle = { applyCloudScene: (scene: any) => void };

// CURATED_PRESETS logic moved to internal component state for Supabase updating

// MarqueeText moved to standalone component MarqueeText.tsx

const DockedController = React.forwardRef<DockedControllerHandle, Sk8lytzControllerProps>(
  function DockedController({ hwSettings, lockedProduct, isPaired, points, devices, onLongPressDevice, writeToDevice: parentWriteToDevice, isPoweredOn = true, onDisconnect, crewRole, onCrewSceneChange }: Sk8lytzControllerProps, ref) {
    const { Colors, isDark } = useTheme();
    const styles = createStyles(Colors);

    /**
     * Perceptual brightness factor — lifts the floor so LEDs stay visible at low %.
     * At 0% → 0 (truly off). At 5% → ~14% (dim outline visible). At 100% → 1.0.
     * Formula: brt > 0 ? 0.10 + 0.90 * (brt/100) : 0
     */
    const brtFactor = (brt: number): number =>
      brt > 0 ? 0.10 + 0.90 * (brt / 100) : 0;

    const [lastSentPayload, setLastSentPayload] = useState<number[]>([]);

    const writeToDevice = async (payload: number[]) => {
      setLastSentPayload([...payload]);
      if (parentWriteToDevice) {
        await parentWriteToDevice(payload);
      }
    };

    const [activeProduct, setActiveProduct] = useState<ProductType>(lockedProduct || 'HALOZ');
    const [activeMode, setActiveMode] = useState<ModeType>('FAVORITES');
    const [lastOperatingMode, setLastOperatingMode] = useState<ModeType>('MULTIMODE');
    const [selectedColor, setSelectedColor] = useState<string>('#00F0FF');
    const [selectedHue, setSelectedHue] = useState<number>(180);
    const [selectedPatternId, setSelectedPatternId] = useState<number>(1);
    const [brightness, setBrightness] = useState<number>(90);
    const [speed, setSpeed] = useState<number>(50);
    const [micSensitivity, setMicSensitivity] = useState<number>(80);
    const [musicHue, setMusicHue] = useState(180);
    const [recording, setRecording] = useState<Audio.Recording | null>(null);
    const [audioMagnitude, setAudioMagnitude] = useState<number>(0);
    const magnitudeInterval = React.useRef<NodeJS.Timeout | null>(null);



    const [quickPresets, setQuickPresets] = useState<IQuickPreset[]>([
      { name: 'Rainbow', colors: ['#FF0000', '#FF7F00', '#FFFF00', '#00FF00', '#0000FF', '#4B0082', '#9400D3'], type: 3 },
      { name: 'America', colors: ['#FF0000', '#FFFFFF', '#0000FF'], type: 3 },
      { name: 'Cyberpunk', colors: ['#00FFFF', '#FF00FF', '#FFFF00'], type: 3 },
      { name: 'Forest', colors: ['#00FF00', '#008000', '#228B22', '#32CD32'], type: 1 },
      { name: 'Sunset', colors: ['#FF0000', '#FF4500', '#FF8C00', '#FFA500'], type: 1 },
      { name: 'Ice', colors: ['#FFFFFF', '#E0FFFF', '#00FFFF', '#0000FF'], type: 1 },
      { name: 'Custom 1', colors: ['#000000', '#FFFFFF', '#000000'], type: 3 },
      { name: 'Custom 2', colors: ['#000000', '#FFFFFF', '#000000'], type: 3 }
    ]);
    const [isQuickPromptVisible, setIsQuickPromptVisible] = useState(false);
    const [quickPromptName, setQuickPromptName] = useState('');
    const [quickPromptTargetIndex, setQuickPromptTargetIndex] = useState(-1);
    const [activeQuickPresetIndex, setActiveQuickPresetIndex] = useState<number | null>(null);

    // Cloud Scene State
    const [isCommunityModalVisible, setIsCommunityModalVisible] = useState<boolean>(false);
    const [isPublishingCloud, setIsPublishingCloud] = useState<boolean>(false);
    const [cloudPublicToggle, setCloudPublicToggle] = useState<boolean>(true);

    const captureEntireState = () => {
      return {
        activeMode, fixedSubMode,
        selectedColor, selectedPatternId, brightness, speed,
        multiColors, multiLength, multiTransition,
        musicPatternId, musicPrimaryColor, musicSecondaryColor, micSensitivity, micSource, musicMatrixStyle,
        streetSensitivity, streetCruiseColor, streetBrakeColor
      };
    };

    const applyCloudScene = (scene: any) => {
      if (scene.activeMode) setActiveMode(scene.activeMode);
      if (scene.fixedSubMode) setFixedSubMode(scene.fixedSubMode);
      if (scene.selectedColor) setSelectedColor(scene.selectedColor);
      if (scene.selectedPatternId) setSelectedPatternId(scene.selectedPatternId);
      if (scene.brightness !== undefined) setBrightness(scene.brightness);
      if (scene.speed !== undefined) setSpeed(scene.speed);
      if (scene.multiColors) setMultiColors(scene.multiColors);
      if (scene.multiLength !== undefined) setMultiLength(scene.multiLength);
      if (scene.multiTransition !== undefined) setMultiTransition(scene.multiTransition);
      if (scene.musicPatternId !== undefined) setMusicPatternId(scene.musicPatternId);
      if (scene.musicPrimaryColor) setMusicPrimaryColor(scene.musicPrimaryColor);
      if (scene.musicSecondaryColor) setMusicSecondaryColor(scene.musicSecondaryColor);
      if (scene.micSensitivity !== undefined) setMicSensitivity(scene.micSensitivity);
      if (scene.micSource !== undefined) setMicSource(scene.micSource);
      if (scene.musicMatrixStyle !== undefined) setMusicMatrixStyle(scene.musicMatrixStyle);
      if (scene.streetSensitivity !== undefined) setStreetSensitivity(scene.streetSensitivity);
      if (scene.streetCruiseColor) setStreetCruiseColor(scene.streetCruiseColor);
      if (scene.streetBrakeColor) setStreetBrakeColor(scene.streetBrakeColor);
    };

    // Expose applyCloudScene to parent via ref (for crew member sync)
    React.useImperativeHandle(ref, () => ({ applyCloudScene }), []);

    // Multi-Color DIY State
    const [multiColors, setMultiColors] = useState<string[]>(['#FF0000', '#00FF00', '#0000FF']);
    const [multiTransition, setMultiTransition] = useState<number>(3);
    const [multiLength, setMultiLength] = useState<number>(16);

    // Active Sub-Mode for the Consolidated Fixed Tab
    const [fixedSubMode, setFixedSubMode] = useState<'PATTERN' | 'BUILDER'>('PATTERN');

    // Multi-Color Builder State
    const [builderNodes, setBuilderNodes] = useState<BuilderNode[]>([
      { id: 'node_1', position: 0, colorHex: '#FF0000' },
      { id: 'node_2', position: 100, colorHex: '#00F0FF' }
    ]);
    const [builderFillMode, setBuilderFillMode] = useState<'GRADIENT' | 'SOLID'>('GRADIENT');
    const [builderTransitionType, setBuilderTransitionType] = useState<number>(1);
    const [builderDirection, setBuilderDirection] = useState<number>(1);

    // ── Street Mode (Accelerometer Reactive) ──────────────────────────────────────
    const [streetSensitivity, setStreetSensitivity] = useState<number>(30);
    const [streetCruiseColor, setStreetCruiseColor] = useState<string>('#FF8C00');
    const [streetBrakeColor, setStreetBrakeColor] = useState<string>('#FF0000');
    const [isStreetBraking, setIsStreetBraking] = useState<boolean>(false);
    const streetBrakingRef = useRef(false);
    const lastAccelRef = useRef({ x: 0, y: 0, z: 0 });

    // Dashboard Telemetry Tracking State
    const [motionState, setMotionState] = useState<MotionState>('STOPPED');
    const [gpsSpeed, setGpsSpeed] = useState<number>(0);
    const [peakGForce, setPeakGForce] = useState<number>(1.0);
    const motionStateRef = useRef<MotionState>('STOPPED');
    const lastGpsSpeeds = useRef<number[]>([]);
    const locationSubRef = useRef<Location.LocationSubscription | null>(null);

    // ── Street Mode: Car-Light Pattern helper ─────────────────────────────────
    // SOULZ (linear strip, heel→toe):
    //   Rear 30% = red tail lights, Middle 40% = amber cruise, Front 30% = white headlights
    // HALOZ (2-segment × 8 ring):
    //   8-LED frame: [RED×2][AMB×4][WHT×2]  →  mirrored to 16-LED array
    //   Result: RED at BACK of ring, WHITE at FRONT, AMBER on both sides  ✅
    //
    // #9 — Cruise bounce chase: tick 0.0→1.0 shifts the bright spot through the mid zone
    const cruiseChaseRef = useRef(0);
    const applyStreetPattern = (
      currMotionState: MotionState,
      brt: number = brightness,
      spd: number = speed
    ) => {
      if (!writeToDevice) return;
      const factor = brtFactor(brt);
      const pts = hwSettings?.ledPoints || points || 16;
      const segs = hwSettings?.segments || 1;
      const isHalozRing = segs === 2 && pts === 16; // HALOZ hardware signature
      const hwSpeed = clampSpeed(spd);

      let cruiseHex = streetCruiseColor; // Use user selected color for ACCELERATING / CRUISING
      if (currMotionState === 'STOPPED') cruiseHex = '#FF0000';
      else if (currMotionState === 'SLOWING_DOWN') cruiseHex = '#FFFF00';
      else if (currMotionState === 'HARD_BRAKING') cruiseHex = '#FF0000';

      const isBraking = currMotionState === 'HARD_BRAKING' || currMotionState === 'STOPPED';

      const cr = parseInt(cruiseHex.slice(1, 3), 16);
      const cg = parseInt(cruiseHex.slice(3, 5), 16);
      const cb = parseInt(cruiseHex.slice(5, 7), 16);

      // Tail lights: ABSOLUTE brightness — 100% (255) braking, 50% (127) cruising.
      // Street Mode has no brightness slider — these are fixed car safety values.
      const tailR = isBraking ? 255 : 127;
      const tail = { r: tailR, g: 0, b: 0 };

      // Headlights: warm white, always steady
      const headVal = Math.round(255 * factor);
      const head = { r: headVal, g: Math.round(headVal * 0.95), b: Math.round(headVal * 0.85) };

      // Dashboard Cruise Color
      const crR = Math.round(cr * factor);
      const crG = Math.round(cg * factor);
      const crB = Math.round(cb * factor);
      const crDim = { r: Math.round(crR * 0.3), g: Math.round(crG * 0.3), b: Math.round(crB * 0.3) };
      const cruise = { r: crR, g: crG, b: crB };

      // DO NOT apply applyColorSorting here.
      // Hardware auto-remaps GRB internally via 0x62 EEPROM config. Send pure RGB.
      let arr: { r: number; g: number; b: number }[];

      // #9 — Cruise bounce chase animation
      // Advance tick for chase direction-flip (0→1→0 triangle)
      const isCruising = currMotionState === 'CRUISING' || currMotionState === 'ACCELERATING';
      if (isCruising) {
        cruiseChaseRef.current = (cruiseChaseRef.current + 0.07) % 2; // 0‑2 range for triangle wave
      } else {
        cruiseChaseRef.current = 0; // Reset on non-cruise states
      }
      const chaseTick = cruiseChaseRef.current <= 1 ? cruiseChaseRef.current : 2 - cruiseChaseRef.current; // Triangle 0→1→0

      if (isHalozRing) {
        // HALOZ: 8-LED ring frame. Chase bright spot through the 4 cruise LEDs (idx 2-5)
        const chaseSlot = Math.floor(chaseTick * 3); // 0,1,2,3 through 4 cruise positions
        const frame8 = [
          tail, tail,
          chaseSlot === 0 ? cruise : crDim,
          chaseSlot === 1 ? cruise : crDim,
          chaseSlot === 2 ? cruise : crDim,
          chaseSlot === 3 ? cruise : crDim,
          head, head,
        ];
        const mirror8 = [...frame8].reverse();
        arr = [...frame8, ...mirror8];
      } else {
        const ledCount = Math.max(10, hwSettings?.ledPoints || pts);
        const rearCount = Math.max(1, Math.round(ledCount * 0.3));
        const frontCount = Math.max(1, Math.round(ledCount * 0.3));
        const midCount = Math.max(1, ledCount - rearCount - frontCount);
        // Chase bright spot bounces through the mid section
        const chasePos = Math.round(chaseTick * (midCount - 1));
        const midSection = Array.from({ length: midCount }, (_, i) => {
          const dist = Math.abs(i - chasePos);
          if (dist === 0) return cruise;
          if (dist === 1) return { r: Math.round(crR * 0.6), g: Math.round(crG * 0.6), b: Math.round(crB * 0.6) };
          return crDim;
        });
        arr = [
          ...Array(rearCount).fill(tail),
          ...midSection,
          ...Array(frontCount).fill(head),
        ];
      }

      // 0x01 = FREEZE (hardware locks array in place — static car lights, no scrolling)
      // 0x02 = STROBE (urgent flashing for hard braking)
      // NOTE: 0x00 is CASCADE (scrolling) — NOT static. Never use 0x00 for car lights.
      const transType = currMotionState === 'HARD_BRAKING' ? 0x02 : 0x01;
      writeToDevice(ZenggeProtocol.setMultiColor(arr, hwSpeed, 1, transType));
    };


    // Update motion state helper
    const updateMotion = (newState: MotionState) => {
      if (newState !== motionStateRef.current) {
        motionStateRef.current = newState;
        setMotionState(newState);
        applyStreetPattern(newState);
      }
    };

    useEffect(() => {
      if (activeMode !== 'STREET') {
        if (Platform.OS !== 'web') Accelerometer.removeAllListeners();
        if (locationSubRef.current) {
          locationSubRef.current.remove();
          locationSubRef.current = null;
        }
        if (streetBrakingRef.current) {
          streetBrakingRef.current = false;
          setIsStreetBraking(false);
        }
        AppLogger.log('STREET_MODE_DEACTIVATED', { lastMotionState: motionStateRef.current, peakGForce, ...deviceContext });
        return;
      }

      // Initialize
      updateMotion('STOPPED');
      AppLogger.log('STREET_MODE_ACTIVATED', { sensitivity: streetSensitivity, cruiseColor: streetCruiseColor, brakeColor: streetBrakeColor, ...deviceContext });

      if (Platform.OS === 'web') return;

      // Start Location tracking
      (async () => {
        try {
          const { status } = await Location.requestForegroundPermissionsAsync();
          if (status === 'granted') {
            locationSubRef.current = await Location.watchPositionAsync(
              { accuracy: Location.Accuracy.Balanced, timeInterval: 1000, distanceInterval: 1 },
              (pos) => {
                const spdMpS = pos.coords.speed || 0;
                const spdMph = Math.max(0, spdMpS * 2.23694);
                setGpsSpeed(spdMph);

                // Add to rolling history
                lastGpsSpeeds.current.push(spdMph);
                if (lastGpsSpeeds.current.length > 3) lastGpsSpeeds.current.shift();

                // If not hard braking, evaluate soft states
                if (motionStateRef.current !== 'HARD_BRAKING') {
                  if (spdMph < 1.0 && peakGForce < 1.1) {
                    updateMotion('STOPPED');
                  } else if (lastGpsSpeeds.current.length >= 2) {
                    const oldSpd = lastGpsSpeeds.current[0];
                    // If speed dropped by > 1.0mph in recent samples
                    if (oldSpd - spdMph > 1.0) {
                      updateMotion('SLOWING_DOWN');
                    } else if (spdMph >= 1.0) {
                      updateMotion('CRUISING');
                    }
                  } else if (spdMph >= 1.0) {
                    updateMotion('CRUISING');
                  }
                }
              }
            );
          }
        } catch (e) {
          console.warn("Location permission denied or unavailable", e);
        }
      })();

      // Accelerometer tracking
      Accelerometer.setUpdateInterval(80);
      const sub = Accelerometer.addListener(({ x, y, z }) => {
        const prev = lastAccelRef.current;
        const gMag = Math.sqrt(x * x + y * y + z * z);

        // Update peak G-Force display smoothing (decay back to 1.0)
        setPeakGForce(prevG => {
          if (gMag > prevG) return parseFloat(gMag.toFixed(2));
          return parseFloat((prevG * 0.95 + 1.0 * 0.05).toFixed(2));
        });

        const jerkMag = Math.sqrt(
          Math.pow(x - prev.x, 2) + Math.pow(y - prev.y, 2) + Math.pow(z - prev.z, 2)
        );
        lastAccelRef.current = { x, y, z };
        const threshold = ((100 - streetSensitivity) / 100) * 2.5 + 0.3;

        const isBraking = jerkMag > threshold;
        const isActivePush = jerkMag > 0.4 && !isBraking;

        if (isBraking) {
          if (!streetBrakingRef.current) {
            // Only log on the leading edge of a brake event, not every accelerometer tick
            AppLogger.log('STREET_JERK_DETECTED', { jerkMag: parseFloat(jerkMag.toFixed(3)), threshold: parseFloat(threshold.toFixed(3)), gpsSpeedMph: gpsSpeed, ...deviceContext });
          }
          streetBrakingRef.current = true;
          setIsStreetBraking(true);
          updateMotion('HARD_BRAKING');
        } else {
          if (streetBrakingRef.current) {
            streetBrakingRef.current = false;
            setIsStreetBraking(false);
            // Re-evaluate state gracefully
            if (lastGpsSpeeds.current[lastGpsSpeeds.current.length - 1] < 1.0) updateMotion('STOPPED');
            else updateMotion('CRUISING');
          } else if (isActivePush && motionStateRef.current !== 'SLOWING_DOWN') {
            updateMotion('ACCELERATING');
          }
        }
      });

      return () => {
        sub.remove();
        if (locationSubRef.current) {
          locationSubRef.current.remove();
          locationSubRef.current = null;
        }
      };
    }, [activeMode, streetSensitivity, brightness, speed]);

    // ── Crew Leader Broadcast ────────────────────────────────────────────────
    // When acting as leader, broadcast scene to crew on every meaningful change.
    const crewBroadcastTimer = useRef<ReturnType<typeof setTimeout> | null>(null);
    useEffect(() => {
      if (crewRole !== 'leader' || !onCrewSceneChange) return;
      if (crewBroadcastTimer.current) clearTimeout(crewBroadcastTimer.current);
      crewBroadcastTimer.current = setTimeout(() => {
        onCrewSceneChange(captureEntireState());
      }, 200);
      return () => {
        if (crewBroadcastTimer.current) clearTimeout(crewBroadcastTimer.current);
      };
    }, [
      crewRole, activeMode, fixedSubMode, selectedColor, selectedPatternId,
      brightness, speed, multiColors,
      streetSensitivity, streetCruiseColor, streetBrakeColor,
    ]);

    // Favorites Array
    const [favorites, setFavorites] = useState<IFavoriteState[]>([]);

    const [isFavPromptVisible, setIsFavPromptVisible] = useState(false);
    const [favPromptName, setFavPromptName] = useState('');
    const [favPromptTargetId, setFavPromptTargetId] = useState<string | null>(null);
    const [activeFavoriteId, setActiveFavoriteId] = useState<string | null>(null);
    const [isDiyBuilderExpanded, setIsDiyBuilderExpanded] = useState(false);

    const handleSaveFavoriteClick = () => {
      if (activeFavoriteId) {
        const activeFav = favorites.find(f => f.id === activeFavoriteId);
        if (activeFav) {
          setFavPromptTargetId(activeFav.id);
          setFavPromptName(activeFav.name);
          setIsFavPromptVisible(true);
          return;
        }
      }
      let defaultName = '';
      try { defaultName = currentStatusText || `${activeMode} Preset`; } catch (e) { defaultName = `${activeMode} Preset`; }
      setFavPromptTargetId(null);
      setFavPromptName(defaultName);
      setIsFavPromptVisible(true);
    };

    const handleConfirmSaveFavorite = () => {
      let defaultName = '';
      try { defaultName = currentStatusText || `${activeMode} Preset`; } catch (e) { defaultName = `${activeMode} Preset`; }
      const name = favPromptName.trim() || defaultName;

      const newFav = {
        id: favPromptTargetId || Date.now().toString(),
        name,
        mode: activeMode === 'MULTIMODE' ? fixedSubMode : activeMode,
        color: selectedColor,
        patternId: activeMode === 'MUSIC' ? musicPatternId : (activeMode === 'MULTIMODE' ? fixedPatternId : selectedPatternId),
        speed,
        brightness,
        fixedColorMode, fixedFgColor, fixedBgColor, fixedHue,
        multiColors, multiTransition, multiLength,
        musicPrimaryColor, musicSecondaryColor, micSensitivity, micSource, musicMatrixStyle
      };

      if (favPromptTargetId) {
        const newFavorites = favorites.map(f => f.id === favPromptTargetId ? newFav : f);
        setFavorites(newFavorites);
        AsyncStorage.setItem('@Sk8lytz_Favorites', JSON.stringify(newFavorites));
        setIsFavPromptVisible(false);
        setFavPromptTargetId(null);
        return;
      }

      const newFavorites = [...favorites, newFav];
      setFavorites(newFavorites);
      AsyncStorage.setItem('@Sk8lytz_Favorites', JSON.stringify(newFavorites));
      setIsFavPromptVisible(false);
    };

    const deleteFavorite = (id: string) => {
      const newFavorites = favorites.filter(f => f.id !== id);
      setFavorites(newFavorites);
      AsyncStorage.setItem('@Sk8lytz_Favorites', JSON.stringify(newFavorites));
    };

    const loadFavorite = (favRaw: IFavoriteState) => {
      const fav: any = favRaw;
      setActiveFavoriteId(fav.id);
      setSpeed(fav.speed);
      setBrightness(fav.brightness);
      if (fav.color) setSelectedColor(fav.color);

      // Normalize legacy mode names to new taxonomy
      const legacyMode = (fav.mode === 'RBM' || fav.mode === 'PROGRAMS') ? 'PROGRAMS'
        : (fav.mode === 'FAVORITES' || fav.mode === 'PRESETS') ? 'FAVORITES'
          : fav.mode;

      if (legacyMode === 'PROGRAMS') {
        setActiveMode('PROGRAMS');
        setSelectedPatternId(fav.patternId);
        if (writeToDevice) writeToDevice(ZenggeProtocol.setCustomRbm(fav.patternId, fav.speed, fav.brightness));
      } else if (legacyMode === 'MUSIC') {
        setActiveMode('MUSIC');
        setMusicPatternId(fav.patternId);
        handleMusicChange(fav.patternId, micSensitivity, fav.brightness, micSource);
      } else if (legacyMode === 'CAMERA') {
        setActiveMode('CAMERA');
      } else if (legacyMode === 'FAVORITES') {
        setActiveMode('FAVORITES');
      } else if (legacyMode === 'MULTIMODE' || legacyMode === 'PATTERN') {
        setActiveMode('MULTIMODE');
        setFixedSubMode('PATTERN');
        setFixedPatternId(fav.patternId);
        setFixedColorMode(fav.fixedColorMode);
        setFixedFgColor(fav.fixedFgColor);
        setFixedBgColor(fav.fixedBgColor);
        applyFixedPattern(fav.patternId, fav.fixedFgColor, fav.fixedBgColor, fav.speed, fav.brightness);
      } else if (legacyMode === 'MULTI' || legacyMode === 'DIY' || legacyMode === 'MULTICOLOR') {
        setActiveMode('MULTIMODE');
        setFixedSubMode('BUILDER');
        setMultiColors(fav.multiColors || []);
        setMultiTransition(fav.multiTransition || 3);
        setMultiLength(fav.multiLength || 16);
        if (writeToDevice && fav.multiColors) {
          const sortIdx = hwSettings?.colorSorting ?? 2;
          const rgbColors = fav.multiColors.map((h: string) => {
            const r = parseInt(h.slice(1, 3), 16) || 0;
            const g = parseInt(h.slice(3, 5), 16) || 0;
            const b = parseInt(h.slice(5, 7), 16) || 0;
            return { r, g, b };
          });
          writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, clampSpeed(fav.speed), 1, fav.multiTransition));
        }
      } else {
        // Unknown/legacy mode - best-effort color dispatch
        if (fav.color) setTimeout(() => {
          sendColor(parseInt(fav.color.slice(1, 3), 16) || 0, parseInt(fav.color.slice(3, 5), 16) || 0, parseInt(fav.color.slice(5, 7), 16) || 0);
        }, 100);
      }
    }

    /** Unified color sender — sends solid color instantly using actual LED count */
    const sendColor = async (r: number, g: number, b: number) => {
      if (!writeToDevice) return;
      // hwSettings.ledPoints IS the total LED count — do NOT divide by segments
      const numLEDs = Math.max(1, hwSettings?.ledPoints || points || 16);
      // DO NOT apply applyColorSorting — hardware auto-remaps GRB via 0x62 EEPROM config
      // transitionType=0x01 (FREEZE) = immediate hardware lock, no animation
      const colors = Array(numLEDs).fill({ r, g, b });
      await writeToDevice(ZenggeProtocol.setMultiColor(colors, 1, 1, 0x01));
    };


    // (Removed generatePristineColors since it is unused after purging applyColorSorting)

    /**
     * Maps UI speed slider (0–100) to Zengge hardware speed range (1–31).
     * The APK enforces 1–31 for all 0x59 animated patterns.
     * Static patterns (transitionType=0x00) ignore speed — pass 1 for those.
     */
    const clampSpeed = (uiSpeed: number): number =>
      Math.max(ZenggeProtocol.ANIM_SPEED_MIN, Math.min(ZenggeProtocol.ANIM_SPEED_MAX, Math.round((uiSpeed / 100) * ZenggeProtocol.ANIM_SPEED_MAX)));

    /**
     * Apply current fixed pattern state to devices.
     * Delegates to PatternEngine — single source of truth for all 10 patterns.
     * Ensures correct 0x59 / 0x51 protocol, correct transition constants,
     * full LED count pixel arrays, and APK-proven speed clamping.
     */
    const applyFixedPattern = async (
      patternId: number = fixedPatternId,
      fg: string = fixedFgColor,
      bg: string = fixedBgColor,
      currentSpeed: number = speed,
      currentBrightness: number = brightness
    ) => {
      if (!writeToDevice) return;

      const factor = currentBrightness / 100;

      // hwSettings.ledPoints IS the total LED count — do NOT divide by segments
      const numLEDs = Math.max(1, hwSettings?.ledPoints || points || 16);


      // 0x51 Custom Engines expect pure, unmodified hexadecimal parameters.
      // The hardware engines interpret brightness separately natively.
      const fgRaw = {
        r: parseInt(fg.substring(1, 3), 16) || 0,
        g: parseInt(fg.substring(3, 5), 16) || 0,
        b: parseInt(fg.substring(5, 7), 16) || 0,
      };
      const bgRaw = {
        r: parseInt(bg.substring(1, 3), 16) || 0,
        g: parseInt(bg.substring(3, 5), 16) || 0,
        b: parseInt(bg.substring(5, 7), 16) || 0,
      };

      // Use compact format: only 1 active step (12 bytes raw, 20 bytes wrapped).
      // Fits in any BLE MTU. Tests if hardware accepts variable-length 0x51.
      const payload = ZenggeProtocol.setCustomModeCompact([
        { mode: patternId, speed: currentSpeed, color1: fgRaw, color2: bgRaw },
      ]);

      if (payload) writeToDevice(payload);
    };

    const applyEmergencyPattern = (spd: number, bright: number) => {
      if (!writeToDevice) return;
      const factor = bright / 100;
      const pts = hwSettings?.ledPoints || points || 16;
      const segs = hwSettings?.segments || 1;
      const isHalozRing = segs === 2 && pts === 16;
      const hwSpd = Math.min(spd, ZenggeProtocol.ANIM_SPEED_MAX);

      const red = { r: Math.round(255 * factor), g: 0, b: 0 };
      const white = { r: Math.round(255 * factor), g: Math.round(255 * factor), b: Math.round(255 * factor) };
      const yellow = { r: Math.round(255 * factor), g: Math.round(255 * factor), b: 0 };
      const off = { r: 0, g: 0, b: 0 };

      let arr: { r: number; g: number; b: number }[];

      if (isHalozRing) {
        // ── HALOZ 2-segment: 8-LED frame mirrored to 16 ──
        // Frame: RED RED YEL off YEL off WHT WHT
        // Mirror: WHT WHT off YEL off YEL RED RED
        // Physical: back=RED, sides=flashing amber, front=WHITE  ✅
        const frame8 = [red, red, yellow, off, yellow, off, white, white];
        const mirror8 = [...frame8].reverse();
        arr = [...frame8, ...mirror8];
      } else {
        // ── SOULZ linear: [rear RED×4][mid flash×8][front WHITE×4] ──
        arr = [
          red, red, red, red,
          yellow, off, yellow, off, yellow, off, yellow, off,
          white, white, white, white,
        ];
      }

      // 0x03 = RunningWater: hardware scrolls mid section natively
      writeToDevice(ZenggeProtocol.setMultiColor(arr, hwSpd, 1, 0x03));
    };

    const [musicPatternId, setMusicPatternId] = useState<number>(1);
    const [micSource, setMicSource] = useState<'APP' | 'DEVICE'>('APP');
    const [musicMatrixStyle, setMusicMatrixStyle] = useState<number>(39); // 0x27 (39) Light Screen, 0x26 (38) Light Bar
    const [musicSecondaryHue, setMusicSecondaryHue] = useState<number>(300);
    const [musicPrimaryColor, setMusicPrimaryColor] = useState<string>('#FF00FF');
    const [musicSecondaryColor, setMusicSecondaryColor] = useState<string>('#00FFFF');
    const [musicColorFocus, setMusicColorFocus] = useState<'PRIMARY' | 'SECONDARY'>('PRIMARY');
    const [musicSetting, setMusicSetting] = useState<'SENSITIVITY' | 'BRIGHTNESS'>('SENSITIVITY');

    const [fixedPatternId, setFixedPatternId] = useState<number>(1);
    const [fixedColorMode, setFixedColorMode] = useState<'FOREGROUND' | 'BACKGROUND'>('FOREGROUND');
    const [fixedFgColor, setFixedFgColor] = useState<string>('#00FF00');
    const [fixedBgColor, setFixedBgColor] = useState<string>('#000000');
    const [fixedHue, setFixedHue] = useState<number>(120);

    // --- PRO EFFECTS REACTIVITY LOGIC ---
    // CRITICAL: parentWriteToDevice MUST be in deps so this effect re-fires when BLE connects.
    // Without it, applyFixedPattern is a stale closure with parentWriteToDevice=undefined.
    const applyFixedRef = useRef<ReturnType<typeof setTimeout> | null>(null);
    useEffect(() => {
      if (activeMode === 'MULTIMODE' && fixedSubMode === 'PATTERN') {
        if (applyFixedRef.current) clearTimeout(applyFixedRef.current);
        applyFixedRef.current = setTimeout(() => {
          applyFixedPattern(fixedPatternId, fixedFgColor, fixedBgColor, speed, brightness);
        }, 50);
      }
      return () => {
        if (applyFixedRef.current) clearTimeout(applyFixedRef.current);
      }
    }, [fixedPatternId, fixedFgColor, fixedBgColor, speed, brightness, activeMode, fixedSubMode, parentWriteToDevice]);

    // -- Curated Presets (SK8Lytz Picks) -- driven from Supabase DB table
    const [curatedPresets, setCuratedPresets] = useState<IFavoriteState[]>([]);
    const [picksLoading, setPicksLoading] = useState(true);

    useEffect(() => {
      const fetchPicks = async () => {
        try {
          if (!supabase) return;
          // Filter: is_active = true AND (active_from is null OR active_from <= today)
          //                           AND (active_until is null OR active_until >= today)
          const today = new Date().toISOString().split('T')[0]; // 'YYYY-MM-DD'
          const { data, error } = await supabase
            .from('sk8lytz_picks')
            .select('*')
            .eq('is_active', true)
            .or(`active_from.is.null,active_from.lte.${today}`)
            .or(`active_until.is.null,active_until.gte.${today}`)
            .order('sort_order', { ascending: true });

          if (error) {
            console.warn('[SK8Lytz Picks] Failed to fetch from DB:', error.message);
            return;
          }

          if (data && Array.isArray(data)) {
            // Map snake_case DB columns → IFavoriteState camelCase
            const mapped: IFavoriteState[] = data.map((row: any) => ({
              id: row.id,
              name: row.name,
              customName: row.custom_name,
              mode: row.mode,
              color: row.color,
              patternId: row.pattern_id,
              speed: row.speed ?? 50,
              brightness: row.brightness ?? 90,
              fixedColorMode: row.fixed_color_mode,
              fixedFgColor: row.fixed_fg_color,
              fixedBgColor: row.fixed_bg_color,
              fixedHue: row.fixed_hue,
              multiColors: row.multi_colors ?? undefined,
              multiTransition: row.multi_transition,
              multiLength: row.multi_length,
              musicPrimaryColor: row.music_primary_color,
              musicSecondaryColor: row.music_secondary_color,
              micSensitivity: row.mic_sensitivity,
              micSource: row.mic_source,
              musicMatrixStyle: row.music_matrix_style,
            }));
            setCuratedPresets(mapped);
          }
        } catch (e) {
          console.warn('[SK8Lytz Picks] Exception fetching from DB:', e);
        } finally {
          setPicksLoading(false);
        }
      };
      fetchPicks();
    }, []);

    // -- App Microphone Logic --
    useEffect(() => {
      if (Platform.OS === 'web') return; // expo-av Audio Recording not supported on web
      const isMusicActive = activeMode === 'MUSIC';
      if (isMusicActive && micSource === 'APP' && isPoweredOn) {
        startRecording();
      } else {
        stopRecording();
      }
      return () => {
        stopRecording();
      };
    }, [activeMode, fixedSubMode, micSource, isPoweredOn]);

    // -- Analytics Logging --
    const logTimers = useRef<Record<string, ReturnType<typeof setTimeout>>>({});

    const deviceContext = React.useMemo(() => {
      if (!devices || devices.length === 0) return { target: 'none' };
      if (devices.length === 1) return { target: 'device', deviceId: devices[0].id };
      return { target: 'group', deviceIds: devices.map(d => d.id), groupSize: devices.length };
    }, [devices]);

    // Mode change logger
    useEffect(() => {
      AppLogger.log('MODE_CHANGED', { mode: activeMode, ...deviceContext });
    }, [activeMode, deviceContext]);

    useEffect(() => {
      const name = getRbmPatternName(selectedPatternId);
      AppLogger.log('PATTERN_CHANGED', {
        pattern: `ID:${selectedPatternId}`,
        name,
        mode: activeMode,
        color: selectedColor,
        ...deviceContext
      });
    }, [selectedPatternId, deviceContext]);

    // Color change logger
    useEffect(() => {
      AppLogger.log('COLOR_CHANGED', { hex: selectedColor, ...deviceContext });
    }, [selectedColor, deviceContext]);

    // Brightness change logger (debounced 600ms)
    useEffect(() => {
      clearTimeout(logTimers.current['brightness']);
      logTimers.current['brightness'] = setTimeout(() => {
        AppLogger.log('BRIGHTNESS_CHANGED', { value: brightness, mode: activeMode, ...deviceContext });
      }, 600);
    }, [brightness, activeMode, deviceContext]);

    // Speed change logger (debounced 600ms)
    useEffect(() => {
      clearTimeout(logTimers.current['speed']);
      logTimers.current['speed'] = setTimeout(() => {
        AppLogger.log('SPEED_CHANGED', { value: speed, mode: activeMode, ...deviceContext });
      }, 600);
    }, [speed, activeMode, deviceContext]);

    // Street sensitivity change logger (debounced 800ms — user drags slider)
    useEffect(() => {
      if (activeMode !== 'STREET') return;
      clearTimeout(logTimers.current['streetSens']);
      logTimers.current['streetSens'] = setTimeout(() => {
        AppLogger.log('STREET_SENSITIVITY_CHANGED', { sensitivity: streetSensitivity, ...deviceContext });
      }, 800);
    }, [streetSensitivity, activeMode, deviceContext]);

    const startRecording = async () => {
      try {
        const { granted } = await Audio.requestPermissionsAsync();
        if (!granted) return;

        await Audio.setAudioModeAsync({
          allowsRecordingIOS: true,
          playsInSilentModeIOS: true,
        });

        const { recording: newRecording } = await Audio.Recording.createAsync(
          {
            ...Audio.RecordingOptionsPresets.LOW_QUALITY,
            isMeteringEnabled: true, // REQUIRED: enables stats.metering — without this mic always reads silence
            android: {
              ...Audio.RecordingOptionsPresets.LOW_QUALITY.android,
              extension: '.m4a',
              outputFormat: Audio.AndroidOutputFormat.MPEG_4,
              audioEncoder: Audio.AndroidAudioEncoder.AAC,
              sampleRate: 44100,
              numberOfChannels: 1,
              bitRate: 128000,
            },
            ios: {
              ...Audio.RecordingOptionsPresets.LOW_QUALITY.ios,
              extension: '.m4a',
              outputFormat: Audio.IOSOutputFormat.MPEG4AAC,
              audioQuality: Audio.IOSAudioQuality.MIN,
              sampleRate: 44100,
              numberOfChannels: 1,
              bitRate: 128000,
              linearPCMBitDepth: 16,
              linearPCMIsBigEndian: false,
              linearPCMIsFloat: false,
            },
          },
          null, // initialStatus
          50 // progressUpdateIntervalMillis
        );

        await newRecording.setProgressUpdateInterval(50);
        setRecording(newRecording);

        // Start magnitude stream
        magnitudeInterval.current = setInterval(async () => {
          if (!writeToDevice) return;
          const stats = await newRecording.getStatusAsync();
          if (stats.canRecord && stats.isRecording) {
            // stats.metering ranges from -160 to 0. 
            // Typical music peaks around -20 to 0.
            const metering = stats.metering ?? -160;
            // Map -60...0 to 0...1 for usable visualization
            const normalized = Math.max(0, Math.min(1, (metering + 60) / 60));

            setAudioMagnitude(normalized);

            // Send to physical device (Symphony 0x74 command expects 0-255)
            const deviceMag = Math.floor(normalized * 255);
            writeToDevice(ZenggeProtocol.sendMusicMagnitude(deviceMag));
          }
        }, 50);

      } catch (err) {
        console.error('Failed to start recording', err);
      }
    };

    const stopRecording = async () => {
      if (magnitudeInterval.current) {
        clearInterval(magnitudeInterval.current);
        magnitudeInterval.current = null;
      }
      if (recording) {
        try {
          await recording.stopAndUnloadAsync();
        } catch (e) { }
        setRecording(null);
      }
    };

    React.useEffect(() => {
      if (lockedProduct) {
        setActiveProduct(lockedProduct);
      }
    }, [lockedProduct]);

    React.useEffect(() => {
      AsyncStorage.getItem('@Sk8lytz_ControllerState').then((saved) => {
        if (saved) {
          try {
            const parsed = JSON.parse(saved);
            // if (parsed.activeMode) setActiveMode(parsed.activeMode); // Ensure Docked Mode always defaults to PRESETS on load
            if (parsed.selectedColor) setSelectedColor(parsed.selectedColor);
            if (parsed.selectedPatternId) setSelectedPatternId(parsed.selectedPatternId);
            if (parsed.brightness !== undefined) setBrightness(parsed.brightness);
            else setBrightness(90);
            if (parsed.speed !== undefined) setSpeed(parsed.speed);
            else setSpeed(50);
            if (parsed.micSensitivity !== undefined) setMicSensitivity(parsed.micSensitivity);
            if (parsed.musicHue !== undefined) setMusicHue(parsed.musicHue);
            if (parsed.musicSecondaryHue !== undefined) setMusicSecondaryHue(parsed.musicSecondaryHue);
            if (parsed.musicPrimaryColor) setMusicPrimaryColor(parsed.musicPrimaryColor);
            if (parsed.musicSecondaryColor) setMusicSecondaryColor(parsed.musicSecondaryColor);
            if (parsed.musicMatrixStyle) setMusicMatrixStyle(parsed.musicMatrixStyle);
            if (parsed.musicPatternId) setMusicPatternId(parsed.musicPatternId);
            if (parsed.micSource) setMicSource(parsed.micSource);
            if (parsed.musicSetting) setMusicSetting(parsed.musicSetting);
            if (parsed.fixedPatternId) setFixedPatternId(parsed.fixedPatternId);
            if (parsed.fixedColorMode) setFixedColorMode(parsed.fixedColorMode);
            if (parsed.fixedFgColor) setFixedFgColor(parsed.fixedFgColor);
            if (parsed.fixedBgColor) setFixedBgColor(parsed.fixedBgColor);
            if (parsed.fixedHue !== undefined) setFixedHue(parsed.fixedHue);
          } catch (e) { }
        }
      });

      AsyncStorage.getItem('@Sk8lytz_Favorites').then((saved) => {
        if (saved) {
          try {
            const parsed: IFavoriteState[] = JSON.parse(saved);
            if (parsed && parsed.length > 0) {
              // One-time migration: rewrite legacy 'DIY'/'MULTI'/'MULTICOLOR' mode
              // entries to 'BUILDER' so stored data stays clean
              let needsMigration = false;
              const migrated = parsed.map(f => {
                if (f.mode === 'DIY' || f.mode === 'MULTI' || f.mode === 'MULTICOLOR') {
                  needsMigration = true;
                  return { ...f, mode: 'BUILDER' };
                }
                if (f.mode === 'RBM') {
                  needsMigration = true;
                  return { ...f, mode: 'PROGRAMS' };
                }
                return f;
              });
              if (needsMigration) {
                console.log('[SK8Lytz Favorites] Migrated legacy mode entries to new taxonomy.');
                AsyncStorage.setItem('@Sk8lytz_Favorites', JSON.stringify(migrated));
              }
              setFavorites(migrated);
            } else {
              // Fallback to default if somehow parsed as empty
              const defaultFavorites = [{
                id: 'default-1',
                name: 'Programs - ' + getRbmPatternName(3),
                mode: 'PROGRAMS',
                patternId: 3,
                speed: 50,
                brightness: 90
              }];
              setFavorites(defaultFavorites);
              AsyncStorage.setItem('@Sk8lytz_Favorites', JSON.stringify(defaultFavorites));
            }
          } catch (e) { }
        } else {
          // First time load, inject default Program mode pattern 3
          const defaultFavorites = [{
            id: 'default-1',
            name: 'Programs - ' + getRbmPatternName(3),
            mode: 'PROGRAMS',
            patternId: 3,
            speed: 50,
            brightness: 90
          }];
          setFavorites(defaultFavorites);
          AsyncStorage.setItem('@Sk8lytz_Favorites', JSON.stringify(defaultFavorites));
        }
      });

      AsyncStorage.getItem('@Sk8lytz_QuickPresets').then((saved) => {
        if (saved) {
          try {
            const parsed = JSON.parse(saved);
            if (parsed && parsed.length > 0) setQuickPresets(parsed);
          } catch (e) { }
        }
      });
    }, []);

    React.useEffect(() => {
      const stateBlob = {
        activeMode, selectedColor, selectedPatternId, brightness, speed,
        micSensitivity, musicHue, musicSecondaryHue, musicPrimaryColor, musicSecondaryColor, musicMatrixStyle, musicPatternId, micSource, musicSetting,
        fixedPatternId, fixedColorMode, fixedFgColor, fixedBgColor, fixedHue
      };
      AsyncStorage.setItem('@Sk8lytz_ControllerState', JSON.stringify(stateBlob)).catch(() => { });
    }, [
      activeMode, selectedColor, selectedPatternId, brightness, speed,
      micSensitivity, musicHue, musicSecondaryHue, musicPrimaryColor, musicSecondaryColor, musicMatrixStyle, musicPatternId, micSource, musicSetting,
      fixedPatternId, fixedColorMode, fixedFgColor, fixedBgColor, fixedHue
    ]);

    const handleMusicChange = (
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

      const c1Raw = {
        r: parseInt(color1Hex.slice(1, 3), 16) || 0,
        g: parseInt(color1Hex.slice(3, 5), 16) || 0,
        b: parseInt(color1Hex.slice(5, 7), 16) || 0
      };

      const c2Raw = {
        r: parseInt(color2Hex.slice(1, 3), 16) || 0,
        g: parseInt(color2Hex.slice(3, 5), 16) || 0,
        b: parseInt(color2Hex.slice(5, 7), 16) || 0
      };

      const c1 = c1Raw;
      const c2 = c2Raw;

      writeToDevice(ZenggeProtocol.setMusicConfig(
        isDeviceMic,
        matrix,
        patternId,
        c1,
        c2,
        sens,
        bright
      ));
    };

    // ── Music Mode: re-send config on color/pattern/source change ──
    // Placed AFTER handleMusicChange so the closure is always fresh.
    // eslint-disable-next-line react-hooks/exhaustive-deps
    React.useEffect(() => {
      if (activeMode !== 'MUSIC' || !writeToDevice) return;
      handleMusicChange(
        musicPatternId, micSensitivity, brightness, micSource,
        musicPrimaryColor, musicSecondaryColor, musicMatrixStyle
      );
    }, [musicPrimaryColor, musicSecondaryColor, musicPatternId, micSource, musicMatrixStyle]);

    const getColorName = (hex: string) => {
      const map: { [key: string]: string } = {
        '#FF0000': 'Red', '#FFFF00': 'Yellow', '#00FF00': 'Green',
        '#00FFFF': 'Cyan', '#0000FF': 'Blue', '#FF00FF': 'Magenta',
        '#FFFFFF': 'White', '#000000': 'Black'
      };
      const upperHex = hex.toUpperCase();
      return map[upperHex] || 'Custom';
    };

    const currentStatusText = React.useMemo(() => {
      switch (activeMode) {
        case 'MULTIMODE':
          const fixedClr = fixedColorMode === 'FOREGROUND' ? fixedFgColor : fixedBgColor;
          return `MultiMode - ${getColorName(fixedClr)}`;
        case 'PROGRAMS':
          return `Programs - ${getRbmPatternName(selectedPatternId)}`;
        case 'MUSIC':
          const patternName = MUSIC_PATTERNS[musicPatternId - 1] || `Effect ${musicPatternId}`;
          return `Music - ${patternName}`;
        case 'STREET': return isStreetBraking ? '🔴 BRAKING' : '🟠 CRUISING';
        case 'CAMERA': return 'Camera';
        case 'FAVORITES': return 'Styles';

        default: return activeMode;
      }
    }, [activeMode, fixedColorMode, fixedFgColor, fixedBgColor, selectedPatternId, musicPatternId, selectedColor, isStreetBraking]);
    const visualizerColor = React.useMemo(() => {
      if (activeMode === 'MULTIMODE') {
        if (fixedSubMode === 'PATTERN') return fixedColorMode === 'FOREGROUND' ? fixedFgColor : fixedBgColor;
        return selectedColor; // BUILDER
      }
      if (activeMode === 'MUSIC') {
        const f = (n: number, k = (n + musicHue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
        const hex = [f(5), f(3), f(1)].map(x => Math.round(x * 255).toString(16).padStart(2, '0')).join('');
        return `#${hex}`;
      }
      return selectedColor;
    }, [activeMode, fixedColorMode, fixedFgColor, fixedBgColor, musicHue, selectedColor, fixedSubMode]);

    return (
      <View style={styles.container}>

        {/* Product Selector - Only show if NO lockedProduct is provided */}
        {!lockedProduct && (
          <View style={styles.tabContainer}>
            <TouchableOpacity
              style={[styles.tab, activeProduct === 'HALOZ' && styles.activeTab]}
              onPress={() => setActiveProduct('HALOZ')}
            >
              {activeProduct === 'HALOZ' && (
                <LinearGradient colors={[Colors.primary, Colors.accent]} start={{ x: 0, y: 0 }} end={{ x: 1, y: 1 }} style={StyleSheet.absoluteFill} />
              )}
              <Text style={[styles.tabText, activeProduct === 'HALOZ' && styles.activeTabText]}>
                HALOZ
              </Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={[styles.tab, activeProduct === 'SOULZ' && styles.activeTab]}
              onPress={() => setActiveProduct('SOULZ')}
            >
              {activeProduct === 'SOULZ' && (
                <LinearGradient colors={[Colors.secondary, Colors.accent]} start={{ x: 0, y: 0 }} end={{ x: 1, y: 1 }} style={StyleSheet.absoluteFill} />
              )}
              <Text style={[styles.tabText, activeProduct === 'SOULZ' && styles.activeTabText]}>
                SOULZ
              </Text>
            </TouchableOpacity>
          </View>
        )}

        {/* Visual Product Shape Selector/Indicator - ENLARGED FOCUS */}
        <View style={styles.visualizerWrapper}>
          <View style={{ marginBottom: 8, width: '100%' }}>
            <TouchableOpacity
              style={{ position: 'absolute', top: 12, right: 16, zIndex: 100, backgroundColor: 'rgba(255,255,255,0.1)', padding: 6, borderRadius: 20 }}
              onPress={handleSaveFavoriteClick}
            >
              <MaterialCommunityIcons name="heart-plus-outline" size={22} color={Colors.primary} />
            </TouchableOpacity>
            <ProductVisualizer
              product={activeProduct}
              color={visualizerColor}
              mode={activeMode === 'FAVORITES' ? (lastOperatingMode === 'MULTIMODE' ? (fixedSubMode === 'BUILDER' ? 'BUILDER' : 'MULTIMODE') : lastOperatingMode) : activeMode === 'MULTIMODE' ? (fixedSubMode === 'BUILDER' ? 'BUILDER' : 'MULTIMODE') : activeMode}
              patternId={activeMode === 'MUSIC' ? musicPatternId : (activeMode === 'MULTIMODE' && fixedSubMode === 'PATTERN' ? fixedPatternId : selectedPatternId)}
              isPaired={isPaired}
              points={points}
              devices={devices}
              onLongPressDevice={onLongPressDevice}
              brightness={brightness}
              speed={speed}
              fixedFgColor={fixedFgColor}
              fixedBgColor={fixedBgColor}
              isPoweredOn={isPoweredOn}
              statusText={currentStatusText}
              audioMagnitude={audioMagnitude}
              rawHexPayload={lastSentPayload}
              multiColors={multiColors}
              multiTransition={multiTransition}
              isStreetBraking={isStreetBraking}
              streetCruiseColor={streetCruiseColor}
              motionState={motionState}
              builderNodes={builderNodes}
              builderFillMode={builderFillMode}
              builderTransitionType={builderTransitionType}
              builderDirection={builderDirection}
            />
          </View>
        </View>

        {/* Removed Active Mode Header to save vertical space */}

        <View style={[styles.controlsContainer, { padding: 4, overflow: 'hidden' }]}>
          <View style={[styles.activeModeContainer, { flex: 1, justifyContent: 'space-evenly' }]}>
            {activeMode === 'FAVORITES' && (
              <View style={{ flex: 1, paddingHorizontal: Layout.padding, justifyContent: 'space-evenly' }}>

                <Text style={[Typography.title, isDark && { color: '#FFF' }, { fontSize: 13 }]}>YOURS</Text>

                <View style={[styles.presetContainer, { flex: 1 }]}>
                  {Array.from({ length: 4 }).map((_, idx) => {
                    const fav = favorites[idx];
                    if (!fav) return <View key={`empty-yours-${idx}`} style={[styles.presetCard, { borderWidth: 1.5, borderStyle: 'dashed', borderColor: 'rgba(255,255,255,0.08)', backgroundColor: 'transparent', elevation: 0, shadowOpacity: 0 }]} />;
                    return (
                      <TouchableOpacity
                        key={fav.id}
                        style={[styles.presetCard, { borderColor: Colors.primary }]}
                        onPress={() => loadFavorite(fav)}
                      >
                        <TouchableOpacity
                          style={{ position: 'absolute', right: 4, top: 4, zIndex: 10, padding: 4 }}
                          onPress={() => {
                            setFavPromptTargetId(fav.id);
                            setFavPromptName(fav.name);
                            setIsFavPromptVisible(true);
                          }}
                        >
                          <MaterialCommunityIcons name="pencil-outline" size={12} color={Colors.textMuted} />
                        </TouchableOpacity>

                        <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center', width: '100%', marginTop: 2, marginBottom: 2, gap: 10, opacity: 0.8, paddingHorizontal: 16 }}>
                          <View style={{ flexDirection: 'row', alignItems: 'center', gap: 2 }}>
                            {fav.mode === 'MUSIC' ? (
                              <><MaterialCommunityIcons name="microphone-outline" size={9} color={Colors.primary} /><Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(fav.micSensitivity || fav.speed || 50)}%</Text></>
                            ) : (
                              <><MaterialCommunityIcons name="speedometer" size={9} color={Colors.primary} /><Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(fav.speed || 50)}%</Text></>
                            )}
                          </View>
                          <View style={{ flexDirection: 'row', alignItems: 'center', gap: 2 }}>
                            <MaterialCommunityIcons name="brightness-6" size={9} color={Colors.primary} />
                            <Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(fav.brightness || 100)}%</Text>
                          </View>
                        </View>
                        <View style={{ width: '100%', minHeight: 20, justifyContent: 'center', alignItems: 'center' }}>
                          <MarqueeText style={[styles.presetTitle, { fontSize: 13, textAlign: 'center', width: '100%' }]}>{fav.name}</MarqueeText>
                        </View>
                        {(() => {
                          if (fav.mode === 'MUSIC') {
                            return (
                              <View style={{ width: '60%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', marginTop: 4, marginBottom: 2 }}>
                                <View style={{ flex: 1, backgroundColor: fav.musicPrimaryColor || '#00FFFF' }} />
                                <View style={{ flex: 1, backgroundColor: fav.musicSecondaryColor || '#FF00FF' }} />
                              </View>
                            );
                          } else if (fav.mode === 'PATTERN' || fav.mode === 'MULTIMODE') {
                            return (
                              <View style={{ width: '60%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', marginTop: 4, marginBottom: 2 }}>
                                <View style={{ flex: 1, backgroundColor: fav.fixedFgColor || '#FFFFFF' }} />
                                <View style={{ flex: 1, backgroundColor: fav.fixedBgColor || '#000000' }} />
                              </View>
                            );
                          } else if (fav.mode === 'MULTI' || fav.mode === 'BUILDER') {
                            const colors = fav.multiColors || ['#FFFFFF'];
                            return (
                              <View style={{ width: '80%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', marginTop: 4, marginBottom: 2 }}>
                                {colors.map((c: string, i: number) => <View key={i} style={{ flex: 1, backgroundColor: c }} />)}
                              </View>
                            );
                          } else {
                            return <MaterialCommunityIcons name={(fav.mode === 'RBM' || fav.mode === 'PROGRAMS') ? 'animation-play' : 'shape-square-plus'} size={14} color={Colors.primary} style={{ marginTop: 4, marginBottom: 2 }} />;
                          }
                        })()}
                      </TouchableOpacity>
                    );
                  })}
                </View>

                <Text style={[Typography.title, isDark && { color: '#FFF' }, { fontSize: 13, marginTop: 4 }]}>SK8Lytz Picks</Text>

                <View style={[styles.presetContainer, { flex: 1 }]}>
                  {Array.from({ length: Math.max(4, curatedPresets.length) }).map((_, idx) => {
                    const fav = curatedPresets[idx];
                    if (!fav) return <View key={`empty-ours-${idx}`} style={[styles.presetCard, { borderWidth: 1.5, borderStyle: 'dashed', borderColor: 'rgba(255,255,255,0.08)', backgroundColor: 'transparent', elevation: 0, shadowOpacity: 0 }]} />;
                    return (
                      <TouchableOpacity
                        key={fav.id}
                        style={[styles.presetCard, { borderColor: Colors.secondary }]}
                        onPress={() => loadFavorite(fav)}
                      >
                        <View style={{ width: '100%', minHeight: 20, justifyContent: 'center', alignItems: 'center', marginTop: 2 }}>
                          <MarqueeText style={[styles.presetTitle, { fontSize: 13, textAlign: 'center', width: '100%' }]}>{fav.customName || fav.name}</MarqueeText>
                        </View>
                        <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center', marginTop: 2, marginBottom: 4, gap: 4, opacity: 0.8 }}>
                          {fav.mode === 'MUSIC' ? (
                            <><MaterialCommunityIcons name="microphone-outline" size={10} color={Colors.secondary} /><Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(fav.micSensitivity || fav.speed || 50)}%</Text></>
                          ) : (
                            <><MaterialCommunityIcons name="speedometer" size={10} color={Colors.secondary} /><Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(fav.speed || 50)}%</Text></>
                          )}
                          <Text style={{ fontSize: 9, color: Colors.textMuted }}> | </Text>
                          <MaterialCommunityIcons name="brightness-6" size={10} color={Colors.secondary} />
                          <Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(fav.brightness || 100)}%</Text>
                        </View>
                        {(() => {
                          if (fav.mode === 'MUSIC') {
                            return (
                              <View style={{ width: '60%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', marginTop: 4, marginBottom: 2 }}>
                                <View style={{ flex: 1, backgroundColor: fav.musicPrimaryColor || '#00FFFF' }} />
                                <View style={{ flex: 1, backgroundColor: fav.musicSecondaryColor || '#FF00FF' }} />
                              </View>
                            );
                          } else if (fav.mode === 'PATTERN' || fav.mode === 'MULTIMODE') {
                            return (
                              <View style={{ width: '60%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', marginTop: 4, marginBottom: 2 }}>
                                <View style={{ flex: 1, backgroundColor: fav.fixedFgColor || '#FFFFFF' }} />
                                <View style={{ flex: 1, backgroundColor: fav.fixedBgColor || '#000000' }} />
                              </View>
                            );
                          } else if (fav.mode === 'MULTI' || fav.mode === 'BUILDER') {
                            const colors = fav.multiColors || ['#FFFFFF'];
                            return (
                              <View style={{ width: '80%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', marginTop: 4, marginBottom: 2 }}>
                                {colors.map((c: string, i: number) => <View key={i} style={{ flex: 1, backgroundColor: c }} />)}
                              </View>
                            );
                          } else {
                            return <MaterialCommunityIcons name={(fav.mode === 'RBM' || fav.mode === 'PROGRAMS') ? 'animation-play' : 'shape-square-plus'} size={14} color={Colors.secondary} style={{ marginTop: 4, marginBottom: 2 }} />;
                          }
                        })()}
                      </TouchableOpacity>
                    );
                  })}
                </View>
              </View>
            )}



            {activeMode === 'MULTIMODE' && (
              <View style={{ flex: 1, marginBottom: 8, justifyContent: 'flex-start' }}>

                {/* UNIFIED PRO EFFECTS & POSITIONAL BUILDER */}
                {(fixedSubMode === 'PATTERN' || fixedSubMode === 'BUILDER') && (
                  <View style={{ flex: 1, width: '100%', marginBottom: 4 }}>

                    {/* UNIFIED TOGGLE */}
                    <View style={{ flexDirection: 'row', marginBottom: 6, marginTop: 2, flexShrink: 0, minHeight: 36 }}>
                      <TouchableOpacity
                        onPress={() => {
                          setFixedSubMode('PATTERN');
                        }}
                        style={{ flex: 1, paddingVertical: 6, alignItems: 'center', backgroundColor: fixedSubMode === 'PATTERN' ? Colors.primary : Colors.surfaceHighlight, borderTopLeftRadius: Layout.borderRadius, borderBottomLeftRadius: Layout.borderRadius }}
                      >
                        <Text style={{ color: fixedSubMode === 'PATTERN' ? '#000' : Colors.textMuted, fontWeight: 'bold' }}>Pro Effects</Text>
                      </TouchableOpacity>
                      <TouchableOpacity
                        onPress={() => setFixedSubMode('BUILDER')}
                        style={{ flex: 1, paddingVertical: 6, alignItems: 'center', backgroundColor: fixedSubMode === 'BUILDER' ? Colors.primary : Colors.surfaceHighlight, borderLeftWidth: 1, borderColor: 'rgba(255,255,255,0.05)', borderTopRightRadius: Layout.borderRadius, borderBottomRightRadius: Layout.borderRadius }}
                      >
                        <Text style={{ color: fixedSubMode === 'BUILDER' ? '#000' : Colors.textMuted, fontWeight: 'bold' }}>Builder</Text>
                      </TouchableOpacity>
                    </View>

                    {/* PRO EFFECTS TIER */}
                    {fixedSubMode === 'PATTERN' && (
                      <View style={{ flex: 1, paddingBottom: 6 }}>
                        <ScrollView
                          style={{ flex: 1, backgroundColor: Colors.isDark ? '#000000' : 'rgba(0,0,0,0.04)', borderRadius: 8 }}
                          contentContainerStyle={{ padding: 8, flexDirection: 'row', flexWrap: 'wrap', justifyContent: 'space-between' }}
                          showsVerticalScrollIndicator={false}
                        >
                          {ZENGGE_EFFECTS.map(effect => (
                            <TouchableOpacity
                              key={effect.id}
                              onPress={() => {
                                setFixedSubMode('PATTERN');
                                setFixedPatternId(effect.id);
                                if (writeToDevice) {
                                  const hexToRgb = (hex: string) => {
                                    const h = hex || '#000000';
                                    return {
                                      r: parseInt(h.substring(1, 3), 16) || 0,
                                      g: parseInt(h.substring(3, 5), 16) || 0,
                                      b: parseInt(h.substring(5, 7), 16) || 0,
                                    };
                                  };
                                  writeToDevice(ZenggeProtocol.setCustomModeCompact([
                                    {
                                      mode: effect.id,
                                      speed: speed,
                                      color1: hexToRgb(fixedFgColor || '#FF0000'),
                                      color2: hexToRgb(fixedBgColor || '#000000'),
                                    }
                                  ]));
                                }
                              }}
                              style={{ width: '48%', minHeight: 40, marginBottom: 8, flexDirection: 'column', justifyContent: 'center', borderBottomWidth: 1, borderBottomColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.05)' }}
                            >
                              <Text style={{ color: fixedPatternId === effect.id ? Colors.primary : Colors.text, fontWeight: 'bold', fontSize: 11, marginBottom: 4 }} numberOfLines={1}>
                                {effect.id}. {effect.name}
                              </Text>
                              <CustomEffectVisualizer
                                effectId={effect.id}
                                speed={speed}
                                points={devices?.[0]?.points || points || 16}
                                segments={devices?.[0]?.segments || 1}
                                direction={true}
                                fgColorHex={fixedFgColor}
                                bgColorHex={fixedBgColor}
                              />
                            </TouchableOpacity>
                          ))}
                        </ScrollView>
                      </View>
                    )}

                    {/* POSITIONAL ARRAY BUILDER TIER */}
                    {fixedSubMode === 'BUILDER' && (
                      <PositionalGradientBuilder
                        nodes={builderNodes}
                        onNodesChange={setBuilderNodes}
                        fillMode={builderFillMode}
                        onFillModeChange={setBuilderFillMode}
                        transitionType={builderTransitionType}
                        onTransitionTypeChange={setBuilderTransitionType}
                        direction={builderDirection}
                        onDirectionChange={setBuilderDirection}
                        speed={speed}
                        deviceLedCount={hwSettings?.ledPoints || points || 150}
                        selectedColor={selectedColor}
                        writeToDevice={writeToDevice}
                      />
                    )}

                  </View>
                )}
              </View>
            )}

            {/* ── PROGRAMS MODE UI ────────────────────────────────────────────── */}
            {activeMode === 'PROGRAMS' && (
              <View style={{ flex: 1, paddingHorizontal: 4, paddingTop: 4 }}>
                <VerticalPatternDrum
                  value={selectedPatternId}
                  onValueChange={(id: number) => {
                    setSelectedPatternId(id);
                    if (writeToDevice) {
                      if (id === 100) {
                        applyEmergencyPattern(speed, brightness);
                      } else {
                        writeToDevice(ZenggeProtocol.setCustomRbm(id, speed, brightness));
                      }
                    }
                  }}
                  itemLabel={(id) => getRbmPatternName(id)}
                />
              </View>
            )}

            {/* ── MUSIC MODE UI ────────────────────────────────────────────────── */}
            {activeMode === 'MUSIC' && (
              <View style={{ flex: 1, paddingHorizontal: 4, paddingTop: 4, overflow: 'hidden' }}>
                {/* Matrix Style: Light Screen / Light Bar */}
                <View style={{ flexDirection: 'row', marginBottom: 6, marginTop: 2, flexShrink: 0, minHeight: 36 }}>
                  <TouchableOpacity
                    onPress={() => {
                      setMusicMatrixStyle(39);
                      handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, musicSecondaryColor, 39);
                    }}
                    style={{ flex: 1, paddingVertical: 6, alignItems: 'center', backgroundColor: musicMatrixStyle === 39 ? Colors.primary : Colors.surfaceHighlight, borderTopLeftRadius: Layout.borderRadius, borderBottomLeftRadius: Layout.borderRadius }}
                  >
                    <Text style={{ color: musicMatrixStyle === 39 ? '#000' : Colors.textMuted, fontWeight: 'bold' }}>Light Screen</Text>
                  </TouchableOpacity>
                  <TouchableOpacity
                    onPress={() => {
                      setMusicMatrixStyle(38);
                      handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, musicSecondaryColor, 38);
                    }}
                    style={{ flex: 1, paddingVertical: 6, alignItems: 'center', backgroundColor: musicMatrixStyle === 38 ? Colors.primary : Colors.surfaceHighlight, borderLeftWidth: 1, borderColor: 'rgba(255,255,255,0.05)', borderTopRightRadius: Layout.borderRadius, borderBottomRightRadius: Layout.borderRadius }}
                  >
                    <Text style={{ color: musicMatrixStyle === 38 ? '#000' : Colors.textMuted, fontWeight: 'bold' }}>Light Bar</Text>
                  </TouchableOpacity>
                </View>

                <View style={{ flex: 1, justifyContent: 'space-evenly' }}>
                  <View style={[styles.musicToggleHeader, { justifyContent: 'center' }]}>
                    <View style={[styles.musicModeIndicator, { alignItems: 'center' }]}>
                      <View style={{ flexDirection: 'row', alignItems: 'center' }}>
                        <TouchableOpacity onPress={() => {
                          const pid = musicPatternId > 1 ? musicPatternId - 1 : MUSIC_PATTERNS.length;
                          setMusicPatternId(pid);
                          handleMusicChange(pid);
                        }} style={{ paddingHorizontal: 10 }}>
                          <Text style={{ color: '#FFF', fontSize: 20, fontWeight: 'bold' }}>{'<'}</Text>
                        </TouchableOpacity>
                        <View style={[styles.musicModeCircle, { width: 32, height: 32, borderRadius: 16 }]}>
                          <Text style={[styles.musicModeNumber, { fontSize: 14 }]}>{musicPatternId}</Text>
                        </View>
                        <TouchableOpacity onPress={() => {
                          const pid = musicPatternId < MUSIC_PATTERNS.length ? musicPatternId + 1 : 1;
                          setMusicPatternId(pid);
                          handleMusicChange(pid);
                        }} style={{ paddingHorizontal: 10 }}>
                          <Text style={{ color: '#FFF', fontSize: 20, fontWeight: 'bold' }}>{'>'}</Text>
                        </TouchableOpacity>
                      </View>
                      <Text style={[Typography.caption, { marginTop: 4, color: Colors.primary, fontWeight: 'bold', fontSize: 13 }]}>
                        {MUSIC_PATTERNS[musicPatternId - 1] || `Effect ${musicPatternId}`}
                      </Text>
                    </View>
                  </View>
                  <View style={styles.micControlSection}>
                    <TouchableOpacity
                      style={[styles.micIconBtn, micSource === 'APP' && styles.micBtnActive]}
                      onPress={() => {
                        setMicSource('APP');
                        handleMusicChange(musicPatternId, micSensitivity, brightness, 'APP');
                      }}
                    >
                      <View style={[styles.micIconCircle, micSource === 'APP' && { backgroundColor: Colors.primary }]}>
                        <MaterialCommunityIcons name="microphone-outline" size={20} color={micSource === 'APP' ? '#FFF' : Colors.textMuted} />
                      </View>
                      <Text style={[styles.micSubText, micSource === 'APP' && { color: Colors.primary, fontWeight: 'bold' }]}>APP MIC</Text>
                    </TouchableOpacity>

                    <TouchableOpacity
                      style={styles.playButtonMain}
                      onPress={() => handleMusicChange()}
                    >
                      <View style={styles.playIconInner}>
                        <MaterialCommunityIcons name="play" size={32} color="#FFF" />
                      </View>
                    </TouchableOpacity>

                    <TouchableOpacity
                      style={[styles.micIconBtn, micSource === 'DEVICE' && styles.micBtnActive]}
                      onPress={() => {
                        setMicSource('DEVICE');
                        handleMusicChange(musicPatternId, micSensitivity, brightness, 'DEVICE');
                      }}
                    >
                      <View style={[styles.micIconCircle, micSource === 'DEVICE' && { backgroundColor: Colors.primary }]}>
                        <MaterialCommunityIcons name="bluetooth-audio" size={20} color={micSource === 'DEVICE' ? '#FFF' : Colors.textMuted} />
                      </View>
                      <Text style={[styles.micSubText, micSource === 'DEVICE' && { color: Colors.primary, fontWeight: 'bold' }]}>DEVICE MIC</Text>
                    </TouchableOpacity>
                  </View>
                </View>
              </View>
            )}

            {/* ── CAMERA MODE UI ────────────────────────────────────────────────── */}
            {activeMode === 'CAMERA' && (
              <View style={{ flex: 1, paddingHorizontal: 4, paddingTop: 4 }}>
                <CameraTracker
                  isActive={activeMode === 'CAMERA'}
                  onColorDetected={(hex: string) => {
                    setSelectedColor(hex);
                    const r = parseInt(hex.slice(1, 3), 16);
                    const g = parseInt(hex.slice(3, 5), 16);
                    const b = parseInt(hex.slice(5, 7), 16);
                    sendColor(r, g, b);
                  }}
                />
              </View>
            )}

            {/* ── STREET MODE UI: FAST & FURIOUS DASHBOARD ─────────────────── */}
            {activeMode === 'STREET' && (
              <ScrollView
                style={{ flex: 1 }}
                contentContainerStyle={{ flexGrow: 1, paddingHorizontal: 4, paddingTop: 6, paddingBottom: 20 }}
                showsVerticalScrollIndicator={false}
                bounces={false}
              >
                {/* ── Street Visualizer: Car-light zone bar ── */}
                <View style={{ marginBottom: 10 }}>
                  <View style={{ flexDirection: 'row', height: 26, borderRadius: 13, overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}>
                    {/* Rear zone — red tail lights */}
                    <View style={{ flex: 3, backgroundColor: isStreetBraking ? '#FF0000' : '#660000', justifyContent: 'center', alignItems: 'center' }}>
                      <Text allowFontScaling={false} style={{ color: '#FFF', fontSize: 9, fontWeight: '800' }}>TAIL (30%)</Text>
                    </View>
                    {/* Middle zone — cruise color */}
                    <View style={{ flex: 4, backgroundColor: streetCruiseColor, justifyContent: 'center', alignItems: 'center', opacity: 0.9 }}>
                      <Text allowFontScaling={false} style={{ color: '#000', fontSize: 9, fontWeight: '800' }}>CRUISE (40%)</Text>
                    </View>
                    {/* Front zone — headlights */}
                    <View style={{ flex: 3, backgroundColor: '#FFF5E0', justifyContent: 'center', alignItems: 'center' }}>
                      <Text allowFontScaling={false} style={{ color: '#333', fontSize: 9, fontWeight: '800' }}>HEAD (30%)</Text>
                    </View>
                  </View>
                </View>

                {/* Status Bar */}
                <View style={{ flexDirection: 'row', justifyContent: 'center', alignItems: 'center', backgroundColor: 'transparent', paddingVertical: 2, marginBottom: 10 }}>
                  <Text
                    allowFontScaling={false}
                    style={{
                      color: (motionState === 'HARD_BRAKING' || motionState === 'STOPPED') ? '#FF4444' : motionState === 'SLOWING_DOWN' ? '#FFD700' : '#00FF00',
                      fontSize: 14, fontWeight: '900', letterSpacing: 4
                    }}>
                    {motionState === 'STOPPED' && '>> STOPPED <<'}
                    {motionState === 'HARD_BRAKING' && '>> HARD BRAKING <<'}
                    {motionState === 'SLOWING_DOWN' && '>> DECELERATING <<'}
                    {motionState === 'ACCELERATING' && '>> ACCELERATING <<'}
                    {motionState === 'CRUISING' && '>> CRUZING <<'}
                  </Text>
                </View>

                <View style={{
                  flexGrow: 1,
                  flexDirection: 'row',
                  backgroundColor: 'transparent',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  paddingVertical: 10,
                  marginBottom: 8,
                }}>
                  {/* LEFT: Stoplight Vertical Graphic */}
                  <View style={{
                    width: 50,
                    justifyContent: 'center',
                    alignItems: 'center',
                  }}>
                    {/* Red Light */}
                    <View style={{
                      width: 22, height: 22, borderRadius: 11, marginBottom: 8,
                      backgroundColor: (motionState === 'STOPPED' || motionState === 'HARD_BRAKING') ? '#FF0000' : '#330000',
                      shadowColor: '#FF0000', shadowOpacity: (motionState === 'STOPPED' || motionState === 'HARD_BRAKING') ? 1 : 0, shadowRadius: 10, elevation: (motionState === 'STOPPED' || motionState === 'HARD_BRAKING') ? 8 : 0,
                      borderWidth: 1, borderColor: (motionState === 'STOPPED' || motionState === 'HARD_BRAKING') ? '#FFAAAA' : '#000',
                    }} />
                    {/* Yellow Light */}
                    <View style={{
                      width: 22, height: 22, borderRadius: 11, marginBottom: 8,
                      backgroundColor: motionState === 'SLOWING_DOWN' ? '#FFFF00' : '#444400',
                      shadowColor: '#FFFF00', shadowOpacity: motionState === 'SLOWING_DOWN' ? 1 : 0, shadowRadius: 10, elevation: motionState === 'SLOWING_DOWN' ? 8 : 0,
                      borderWidth: 1, borderColor: motionState === 'SLOWING_DOWN' ? '#FFFFAA' : '#000',
                    }} />
                    {/* Green Light */}
                    <View style={{
                      width: 22, height: 22, borderRadius: 11,
                      backgroundColor: (motionState === 'ACCELERATING' || motionState === 'CRUISING') ? '#00FF00' : '#003300',
                      shadowColor: '#00FF00', shadowOpacity: (motionState === 'ACCELERATING' || motionState === 'CRUISING') ? 1 : 0, shadowRadius: 10, elevation: (motionState === 'ACCELERATING' || motionState === 'CRUISING') ? 8 : 0,
                      borderWidth: 1, borderColor: (motionState === 'ACCELERATING' || motionState === 'CRUISING') ? '#AAFFAA' : '#000',
                    }} />
                  </View>

                  {/* CENTER: Telemetry Gauges */}
                  <View style={{ flex: 1, flexDirection: 'row', justifyContent: 'space-evenly', alignItems: 'center' }}>
                    <AnalogGauge value={gpsSpeed} min={0} max={25} label="SPEED" unit="MPH" size={120} defaultColor="#00F0FF" dangerVal={15} criticalVal={20} />
                    <AnalogGauge value={peakGForce} min={0.3} max={2.5} label="G-FORCE" unit="G" size={120} defaultColor="#FFD700" dangerVal={1.2} criticalVal={1.8} />
                  </View>
                </View>
              </ScrollView>
            )}

          </View>

          {/* UNIVERSAL SLIDERS FOOTER - Hidden in FAVORITES only */}
          {activeMode !== 'FAVORITES' && (
            <View style={[styles.sceneSlidersContainer, { marginTop: 8, borderTopWidth: 1, borderTopColor: 'rgba(255,255,255,0.05)', paddingTop: 8, paddingBottom: 0, flexShrink: 0 }]}>
              {/* Color Grid wrappers */}
              {!(activeMode === 'PROGRAMS') && (
                <View style={{ marginBottom: 4 }}>
                  {/* Dynamic Selected Color Bar */}
                  {!(activeMode === 'MUSIC' || (activeMode === 'MULTIMODE' && fixedSubMode === 'PATTERN' && fixedPatternId !== 1)) && (() => {
                    const dynamicColor = activeMode === 'STREET' ? streetCruiseColor : selectedColor;

                    return (
                      <TouchableOpacity
                        activeOpacity={0.9}
                        onPress={() => {
                          // Send immediately if pressed just in case
                          const r = parseInt(dynamicColor.slice(1, 3), 16) || 255;
                          const g = parseInt(dynamicColor.slice(3, 5), 16) || 255;
                          const b = parseInt(dynamicColor.slice(5, 7), 16) || 255;
                          if (activeMode === 'MULTIMODE' && fixedSubMode !== 'PATTERN') sendColor(r, g, b);
                          else if (activeMode === 'STREET') applyStreetPattern(motionStateRef.current);
                        }}
                        style={{
                          width: '100%',
                          height: 18,
                          borderRadius: 9,
                          backgroundColor: dynamicColor,
                          justifyContent: 'center',
                          alignItems: 'center',
                          marginBottom: 4,
                          shadowColor: dynamicColor,
                          shadowOpacity: 0.8,
                          shadowRadius: 10,
                          shadowOffset: { width: 0, height: 0 },
                          elevation: 6,
                          borderWidth: 1,
                          borderColor: 'rgba(255,255,255,0.4)'
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
                    const primHex = musicPrimaryColor;
                    const secHex = musicSecondaryColor;

                    return (
                      <View style={{
                        flexDirection: 'row', width: '100%', height: 18, borderRadius: 9, marginBottom: 4,
                        borderWidth: 1, borderColor: 'rgba(255,255,255,0.4)', backgroundColor: 'transparent'
                      }}>
                        <TouchableOpacity
                          activeOpacity={0.9}
                          onPress={() => setMusicColorFocus('PRIMARY')}
                          style={{ flex: 1, backgroundColor: primHex, justifyContent: 'center', alignItems: 'center', opacity: musicColorFocus === 'PRIMARY' ? 1.0 : 0.4, borderTopLeftRadius: 8, borderBottomLeftRadius: 8, shadowColor: primHex, shadowOpacity: 1, shadowRadius: 16, shadowOffset: { width: 0, height: 0 }, elevation: 12 }}
                        >
                          <Text style={{ color: '#FFFFFF', fontSize: 9, fontWeight: '900', letterSpacing: 1, textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 6 }}>SOUND COLUMN</Text>
                        </TouchableOpacity>
                        <TouchableOpacity
                          activeOpacity={0.9}
                          onPress={() => setMusicColorFocus('SECONDARY')}
                          style={{ flex: 1, backgroundColor: secHex, justifyContent: 'center', alignItems: 'center', opacity: musicColorFocus === 'SECONDARY' ? 1.0 : 0.4, borderLeftWidth: 1, borderLeftColor: 'rgba(255,255,255,0.4)', borderTopRightRadius: 8, borderBottomRightRadius: 8, shadowColor: secHex, shadowOpacity: 1, shadowRadius: 16, shadowOffset: { width: 0, height: 0 }, elevation: 12 }}
                        >
                          <Text style={{ color: '#FFFFFF', fontSize: 9, fontWeight: '900', letterSpacing: 1, textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 6 }}>DROP COLOR</Text>
                        </TouchableOpacity>
                      </View>
                    );
                  })()}

                  {/* Fixed Pattern Mode Split Color Tracker — respects effect's color requirements */}
                  {(activeMode === 'MULTIMODE' && fixedSubMode === 'PATTERN') && (() => {
                    const selectedEffect = ZENGGE_EFFECTS.find(e => e.id === fixedPatternId);
                    const showFg = selectedEffect ? selectedEffect.requiresForeground : true;
                    const showBg = selectedEffect ? selectedEffect.requiresBackground : true;
                    // 7-color auto effects (27-33): hardware ignores colors — hide both pickers
                    if (!showFg && !showBg) return null;
                    return (
                      <View style={{
                        flexDirection: 'row', width: '100%', height: 18, borderRadius: 9, marginBottom: 4,
                        borderWidth: 1, borderColor: 'rgba(255,255,255,0.4)', backgroundColor: 'transparent'
                      }}>
                        {showFg && (
                          <TouchableOpacity
                            activeOpacity={0.9}
                            onPress={() => setFixedColorMode('FOREGROUND')}
                            style={{ flex: 1, backgroundColor: fixedFgColor, justifyContent: 'center', alignItems: 'center', opacity: fixedColorMode === 'FOREGROUND' ? 1.0 : 0.4, borderTopLeftRadius: 8, borderBottomLeftRadius: 8, shadowColor: fixedFgColor, shadowOpacity: 1, shadowRadius: 16, shadowOffset: { width: 0, height: 0 }, elevation: 12 }}
                          >
                            <Text style={{ color: '#FFFFFF', fontSize: 9, fontWeight: '900', letterSpacing: 1, textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 6 }}>FOREGROUND</Text>
                          </TouchableOpacity>
                        )}
                        {showBg && (
                          <TouchableOpacity
                            activeOpacity={0.9}
                            onPress={() => setFixedColorMode('BACKGROUND')}
                            style={{ flex: 1, backgroundColor: fixedBgColor, justifyContent: 'center', alignItems: 'center', opacity: fixedColorMode === 'BACKGROUND' ? 1.0 : 0.4, borderLeftWidth: showFg ? 1 : 0, borderLeftColor: 'rgba(255,255,255,0.4)', borderTopRightRadius: 8, borderBottomRightRadius: 8, borderTopLeftRadius: showFg ? 0 : 8, borderBottomLeftRadius: showFg ? 0 : 8, shadowColor: fixedBgColor, shadowOpacity: 1, shadowRadius: 16, shadowOffset: { width: 0, height: 0 }, elevation: 12 }}
                          >
                            <Text style={{ color: '#FFFFFF', fontSize: 9, fontWeight: '900', letterSpacing: 1, textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 6 }}>BACKGROUND</Text>
                          </TouchableOpacity>
                        )}
                      </View>
                    );
                  })()}


                  {/* 9 Preset Colors Grid */}
                  {!(activeMode === 'CAMERA') && (
                    <View style={[styles.colorGrid, { paddingHorizontal: 0, flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between' }]}>
                      {[
                        '#FF0000', '#FF8000', '#FFFF00', '#00FF00', '#00FFFF', '#0000FF', '#800080', '#FF00FF', '#FFFFFF', '#000000'
                      ].map((color, index) => {
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
                              const hueMap: { [key: string]: number } = {
                                '#FF0000': 0, '#FF8000': 30, '#FFFF00': 60, '#00FF00': 120,
                                '#00FFFF': 180, '#0000FF': 240, '#800080': 280, '#FF00FF': 300, '#FFFFFF': 0, '#000000': 0
                              };
                              if (activeMode === 'MULTIMODE') {
                                if (fixedSubMode === 'PATTERN') {
                                  let newFg = fixedFgColor;
                                  let newBg = fixedBgColor;
                                  if (fixedColorMode === 'FOREGROUND') {
                                    newFg = color;
                                    setFixedFgColor(color);
                                    setSelectedColor(color);
                                  } else {
                                    newBg = color;
                                    setFixedBgColor(color);
                                    setSelectedColor(color);
                                  }
                                  if (hueMap[color] !== undefined) setFixedHue(hueMap[color]);
                                  applyFixedPattern(fixedPatternId, newFg, newBg);
                                } else {
                                  setSelectedColor(color);
                                  if (hueMap[color] !== undefined) setFixedHue(hueMap[color]);
                                }
                              } else if (activeMode === 'MUSIC') {
                                if (musicColorFocus === 'PRIMARY') {
                                  setMusicPrimaryColor(color);
                                  if (hueMap[color] !== undefined) setMusicHue(hueMap[color]);
                                  handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, color, musicSecondaryColor, musicMatrixStyle);
                                } else {
                                  setMusicSecondaryColor(color);
                                  if (hueMap[color] !== undefined) setMusicSecondaryHue(hueMap[color]);
                                  handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, color, musicMatrixStyle);
                                }
                              } else if (activeMode === 'STREET') {
                                setStreetCruiseColor(color);
                                if (hueMap[color] !== undefined) setSelectedHue(hueMap[color]);
                                applyStreetPattern(motionStateRef.current);
                              } else {
                                setSelectedColor(color);
                                if (hueMap[color] !== undefined) setSelectedHue(hueMap[color]);
                                const r = parseInt(color.slice(1, 3), 16);
                                const g = parseInt(color.slice(3, 5), 16);
                                const b = parseInt(color.slice(5, 7), 16);
                                sendColor(r, g, b);
                              }
                            }}
                            style={[
                              {
                                backgroundColor: color,
                                width: 20,
                                height: 20,
                                borderRadius: 10,
                                shadowColor: color,
                                shadowOpacity: 1,
                                shadowRadius: 10,
                                shadowOffset: { width: 0, height: 0 },
                                elevation: 8,
                                margin: 2
                              },
                              isActive && { borderWidth: 2, borderColor: '#FFF' }
                            ]}
                          />
                        );
                      })}
                    </View>
                  )}
                </View>
              )}

              {/* Old Color Focus Toggle for Music Mode has been moved above the color grid */}

              {/* Hue Slider */}
              {!(activeMode === 'PROGRAMS' || activeMode === 'CAMERA') && (
                <View style={[styles.controlRow, { marginTop: 4, marginBottom: 4, flexShrink: 0, minHeight: 40 }]}>
                  <NeonHueStrip
                    value={activeMode === 'MUSIC' ? (musicColorFocus === 'PRIMARY' ? musicHue : musicSecondaryHue) : activeMode === 'MULTIMODE' ? fixedHue : selectedHue}
                    onValueChange={(hue) => {
                      if (activeMode === 'MULTIMODE') {
                        if (fixedSubMode === 'PATTERN') {
                          setFixedHue(hue);
                          const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                          const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0")).join("");
                          const hex = rgb2hex(f(5), f(3), f(1));
                          if (fixedColorMode === 'FOREGROUND') {
                            setFixedFgColor(hex);
                            setSelectedColor(hex);
                          } else {
                            setFixedBgColor(hex);
                            setSelectedColor(hex);
                          }
                        } else {
                          setFixedHue(hue);
                          const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                          const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0")).join("");
                          const hex = rgb2hex(f(5), f(3), f(1));
                          setSelectedColor(hex);
                        }
                      } else if (activeMode === 'MUSIC') {
                        const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                        const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0").toUpperCase()).join("");
                        const hex = rgb2hex(f(5), f(3), f(1));
                        if (musicColorFocus === 'PRIMARY') { setMusicPrimaryColor(hex); setMusicHue(hue); }
                        else { setMusicSecondaryColor(hex); setMusicSecondaryHue(hue); }
                      } else if (activeMode === 'STREET') {
                        const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                        const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0").toUpperCase()).join("");
                        const hex = rgb2hex(f(5), f(3), f(1));
                        setStreetCruiseColor(hex);
                        setSelectedHue(hue);
                        applyStreetPattern(motionStateRef.current);
                      } else {
                        setSelectedHue(hue);
                        const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                        const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0").toUpperCase()).join("");
                        setSelectedColor(rgb2hex(f(5), f(3), f(1)));
                      }
                    }}
                    onSlidingComplete={(hue) => {
                      if (activeMode === 'MULTIMODE') {
                        if (fixedSubMode === 'PATTERN') {
                          applyFixedPattern(fixedPatternId, fixedFgColor, fixedBgColor);
                        }
                      } else if (activeMode === 'MUSIC') {
                        const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                        const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0").toUpperCase()).join("");
                        const hex = rgb2hex(f(5), f(3), f(1));
                        if (musicColorFocus === 'PRIMARY') {
                          handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, hex, musicSecondaryColor, musicMatrixStyle);
                        } else {
                          handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, hex, musicMatrixStyle);
                        }
                      } else if (activeMode === 'STREET') {
                        const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                        const rgb2hex = (r: number, g: number, b: number) => "#" + [r, g, b].map(x => Math.round(x * 255).toString(16).padStart(2, "0").toUpperCase()).join("");
                        const hex = rgb2hex(f(5), f(3), f(1));
                        applyStreetPattern(motionStateRef.current);
                      } else {
                        const f = (n: number, k = (n + hue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
                        const r = Math.round(f(5) * 255);
                        const g = Math.round(f(3) * 255);
                        const b = Math.round(f(1) * 255);
                        sendColor(r, g, b);
                      }
                    }}
                    minimumValue={0}
                    maximumValue={360}
                    style={{ flex: 1 }}
                  />
                </View>
              )}

              {/* TACTICAL UNIVERSAL SLIDERS SECTIONS (50/50 Split) */}
              <View style={{ flexDirection: 'row', gap: 8, marginTop: 8, marginBottom: 4, minHeight: 44 }}>

                {/* === LEFT SLOT COMPUTATION === */}
                {/* Brightness is standard on left, UNLESS it's Street (Brake Sens) or Music (Mic Sens) or DIY (Nothing) */}

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
                      if (writeToDevice) {
                        if (activeMode === 'MULTIMODE' && fixedSubMode === 'PATTERN') {
                          applyFixedPattern(fixedPatternId, fixedFgColor, fixedBgColor, speed, val);
                        } else if (activeMode === 'PROGRAMS') {
                          if (selectedPatternId === 100) applyEmergencyPattern(speed, val);
                          else writeToDevice(ZenggeProtocol.setCustomRbm(selectedPatternId, speed, val));
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
                    onSlidingComplete={(val: number) => handleMusicChange(musicPatternId, val, brightness, micSource, musicPrimaryColor, musicSecondaryColor, musicMatrixStyle)}
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
                  />
                )}

                {/* === RIGHT SLOT COMPUTATION === */}
                {/* Speed is standard on right, but Music puts Brightness here. Camera has nothing. */}

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
                        } else if (activeMode === 'PROGRAMS') {
                          if (selectedPatternId === 100) applyEmergencyPattern(val, brightness);
                          else writeToDevice(ZenggeProtocol.setCustomRbm(selectedPatternId, val, brightness));
                        } else if (activeMode === 'STREET') {
                          applyStreetPattern(motionStateRef.current, brightness, val);
                        }
                      }
                    }}
                  />
                )}

                {activeMode === 'MUSIC' && (
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
                    onSlidingComplete={(val: number) => handleMusicChange(musicPatternId, micSensitivity, val, micSource, musicPrimaryColor, musicSecondaryColor, musicMatrixStyle)}
                  />
                )}
              </View>
            </View>
          )}
        </View>

        {/* THE FLOATING DOCK */}
        <View style={{ marginBottom: 4 }}>
          <View style={[styles.floatingDock, { marginBottom: 0 }]}>
            {[
              { id: 'HOME',      icon: 'home-outline'         },
              { id: 'FAVORITES', icon: 'cards-heart-outline'  },

              { id: 'MULTI',     icon: 'palette'              },
              { id: 'PROGRAMS',  icon: 'animation-play'       },
              { id: 'MUSIC',     icon: 'music'                },
              { id: 'STREET',    icon: 'run-fast'             },
              { id: 'CAMERA',    icon: 'camera'               },
            ].map(dockItem => {
              const isActive =
                dockItem.id === 'MULTI'   ? activeMode === 'MULTIMODE' :

                activeMode === dockItem.id;
              return (
                <TouchableOpacity
                  key={dockItem.id}
                  onPress={() => {
                    if (dockItem.id === 'HOME') {
                      if (onDisconnect) onDisconnect();
                    } else if (dockItem.id === 'FAVORITES') {
                      setActiveMode('FAVORITES');

                    } else if (dockItem.id === 'STREET') {
                      setActiveMode('STREET');
                      setLastOperatingMode('STREET');
                    } else if (dockItem.id === 'PROGRAMS') {
                      setActiveMode('PROGRAMS');
                      setLastOperatingMode('PROGRAMS');
                    } else if (dockItem.id === 'MUSIC') {
                      setActiveMode('MUSIC');
                      setLastOperatingMode('MUSIC');
                    } else if (dockItem.id === 'CAMERA') {
                      setActiveMode('CAMERA');
                      setLastOperatingMode('CAMERA');
                    } else {
                      // MULTI -> MULTIMODE (restores to PATTERN submode)
                      setActiveMode('MULTIMODE');
                      setLastOperatingMode('MULTIMODE');
                      setFixedSubMode('PATTERN');
                    }
                  }}
                  style={[styles.dockIconCont, isActive && styles.dockIconActive]}
                >
                  <MaterialCommunityIcons
                    name={dockItem.icon as any}
                    size={22}
                    color={isActive ? '#000000' : Colors.textMuted}
                  />
                </TouchableOpacity>
              );
            })}
          </View>
        </View>
        {/* Quick Preset Prompt Modal */}
        <Modal visible={isQuickPromptVisible} transparent animationType="fade">
          <View style={{ flex: 1, backgroundColor: 'rgba(0,0,0,0.85)', justifyContent: 'center', alignItems: 'center', padding: 20 }}>
            <View style={{ backgroundColor: Colors.surface, padding: 24, borderRadius: 20, width: '100%', maxWidth: 340, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}>
              <Text style={{ color: '#FFF', fontSize: 18, fontWeight: 'bold', marginBottom: 12, textAlign: 'center' }}>
                {quickPromptTargetIndex === -1 ? 'Save Quick Preset' : 'Edit Quick Preset'}
              </Text>
              <Text style={{ color: Colors.textMuted, fontSize: 14, marginBottom: 20, textAlign: 'center' }}>
                {quickPromptTargetIndex === -1 ? 'Name your new preset to store it in the Quick bar.' : 'Rename your preset or delete it from the bar.'}
              </Text>
              <TextInput
                style={{ backgroundColor: 'rgba(255,255,255,0.05)', color: '#FFF', padding: 12, borderRadius: 8, fontSize: 16, marginBottom: 16, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}
                placeholder="Preset Name..."
                placeholderTextColor="rgba(255,255,255,0.3)"
                value={quickPromptName}
                onChangeText={setQuickPromptName}
                autoFocus
              />
              {/* Cloud visibility toggle */}
              {quickPromptTargetIndex === -1 && (
                <TouchableOpacity
                  onPress={() => setCloudPublicToggle(p => !p)}
                  style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 10, padding: 10, marginBottom: 20, borderWidth: 1, borderColor: cloudPublicToggle ? '#00C853' : 'rgba(255,255,255,0.1)' }}
                >
                  <MaterialCommunityIcons
                    name={cloudPublicToggle ? 'earth' : 'lock-outline'}
                    size={18}
                    color={cloudPublicToggle ? '#00C853' : Colors.textMuted}
                    style={{ marginRight: 10 }}
                  />
                  <View style={{ flex: 1 }}>
                    <Text style={{ color: '#FFF', fontWeight: 'bold', fontSize: 13 }}>{cloudPublicToggle ? 'Public — visible to community' : 'Private — only you can see it'}</Text>
                    <Text style={{ color: Colors.textMuted, fontSize: 11, marginTop: 2 }}>Tap to toggle visibility</Text>
                  </View>
                </TouchableOpacity>
              )}
              <View style={{ flexDirection: 'row', gap: 12 }}>
                {quickPromptTargetIndex !== -1 && (
                  <TouchableOpacity style={{ flex: 1, padding: 14, borderRadius: 10, backgroundColor: 'rgba(255,0,0,0.3)' }} onPress={() => {
                    const newArr = [...quickPresets];
                    newArr.splice(quickPromptTargetIndex, 1);
                    setQuickPresets(newArr);
                    AsyncStorage.setItem('@Sk8lytz_QuickPresets', JSON.stringify(newArr));
                    setIsQuickPromptVisible(false);
                  }}>
                    <Text style={{ color: '#FFF', textAlign: 'center', fontWeight: 'bold' }}>Delete</Text>
                  </TouchableOpacity>
                )}
                {quickPromptTargetIndex === -1 && (
                  <TouchableOpacity style={{ flex: 1, padding: 14, borderRadius: 10, backgroundColor: 'rgba(255,255,255,0.05)' }} onPress={() => setIsQuickPromptVisible(false)}>
                    <Text style={{ color: '#FFF', textAlign: 'center', fontWeight: 'bold' }}>Cancel</Text>
                  </TouchableOpacity>
                )}
                <TouchableOpacity style={{ flex: 1, padding: 14, borderRadius: 10, backgroundColor: '#00C853' }} disabled={isPublishingCloud} onPress={async () => {
                  setIsPublishingCloud(true);
                  const safeName = quickPromptName.trim() || 'Cloud Scene';
                  if (containsProfanity(safeName)) {
                    Alert.alert('Invalid Name', 'Scene names cannot contain inappropriate language. Please choose a different name.');
                    setIsPublishingCloud(false);
                    return;
                  }
                  const success = await ScenesService.publishScene(safeName, captureEntireState(), cloudPublicToggle);
                  if (success) {
                    Alert.alert(cloudPublicToggle ? 'Published!' : 'Saved!', cloudPublicToggle ? 'Your scene is now available to the community.' : 'Scene saved privately to your cloud.');
                    setIsQuickPromptVisible(false);
                  } else {
                    Alert.alert('Error', 'Could not save scene. Are you logged in?');
                  }
                  setIsPublishingCloud(false);
                }}>
                  <Text style={{ color: '#000', textAlign: 'center', fontWeight: 'bold' }}>{isPublishingCloud ? 'Saving...' : (cloudPublicToggle ? '🌍 Publish' : '🔒 Save Private')}</Text>
                </TouchableOpacity>
                <TouchableOpacity style={{ flex: 1, padding: 14, borderRadius: 10, backgroundColor: Colors.primary }} onPress={() => {
                  const newArr = [...quickPresets];
                  const safeName = quickPromptName.trim() || 'Preset';
                  if (quickPromptTargetIndex === -1) {
                    newArr.push({ name: safeName, colors: multiColors, type: multiTransition });
                  } else {
                    newArr[quickPromptTargetIndex].name = safeName;
                  }
                  setQuickPresets(newArr);
                  AsyncStorage.setItem('@Sk8lytz_QuickPresets', JSON.stringify(newArr));
                  setIsQuickPromptVisible(false);
                }}>
                  <Text style={{ color: '#000', textAlign: 'center', fontWeight: 'bold' }}>Save</Text>
                </TouchableOpacity>
              </View>
            </View>
          </View>
        </Modal>

        {/* Community Modal */}
        <CommunityModal
          isVisible={isCommunityModalVisible}
          onClose={() => setIsCommunityModalVisible(false)}
          onApplyScene={applyCloudScene}
        />

        {/* Favorite Prompt Modal */}
        <Modal visible={isFavPromptVisible} transparent animationType="fade">
          <View style={{ flex: 1, backgroundColor: 'rgba(0,0,0,0.85)', justifyContent: 'center', alignItems: 'center', padding: 20 }}>
            <View style={{ backgroundColor: Colors.surface, padding: 24, borderRadius: 20, width: '100%', maxWidth: 340, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}>
              <Text style={{ color: '#FFF', fontSize: 18, fontWeight: 'bold', marginBottom: 12, textAlign: 'center' }}>
                {favPromptTargetId ? 'Edit Favorite' : 'Save Favorite'}
              </Text>
              <Text style={{ color: Colors.textMuted, fontSize: 14, marginBottom: 20, textAlign: 'center' }}>
                {favPromptTargetId ? 'Rename your preset or delete it.' : 'Name your preset. Leave blank to use the default name.'}
              </Text>
              <TextInput
                style={{ backgroundColor: 'rgba(255,255,255,0.05)', color: '#FFF', padding: 12, borderRadius: 8, fontSize: 16, marginBottom: 24, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}
                placeholder="Custom Preset Name..."
                placeholderTextColor="rgba(255,255,255,0.3)"
                value={favPromptName}
                onChangeText={setFavPromptName}
                autoFocus
              />
              <View style={{ flexDirection: 'row', gap: 12 }}>
                {favPromptTargetId && (
                  <TouchableOpacity style={{ flex: 1, padding: 14, borderRadius: 10, backgroundColor: 'rgba(255,0,0,0.3)' }} onPress={() => { deleteFavorite(favPromptTargetId); setIsFavPromptVisible(false); setFavPromptTargetId(null); }}>
                    <Text style={{ color: '#FFF', textAlign: 'center', fontWeight: 'bold' }}>Delete</Text>
                  </TouchableOpacity>
                )}
                {(!favPromptTargetId) && (
                  <TouchableOpacity style={{ flex: 1, padding: 14, borderRadius: 10, backgroundColor: 'rgba(255,255,255,0.05)' }} onPress={() => setIsFavPromptVisible(false)}>
                    <Text style={{ color: '#FFF', textAlign: 'center', fontWeight: 'bold' }}>Cancel</Text>
                  </TouchableOpacity>
                )}
                <TouchableOpacity style={{ flex: 1, padding: 14, borderRadius: 10, backgroundColor: Colors.primary }} onPress={handleConfirmSaveFavorite}>
                  <Text style={{ color: '#000', textAlign: 'center', fontWeight: 'bold' }}>Save</Text>
                </TouchableOpacity>
              </View>
            </View>
          </View>
        </Modal>

      </View>
    );
  }
);

export default DockedController;


const createStyles = (Colors: import('../theme/theme').ThemePalette) => StyleSheet.create({
  container: {
    flex: 1,
    paddingHorizontal: 0,
    paddingBottom: 0,
    paddingTop: 0,
  },
  visualizerWrapper: {
    width: '100%',
    alignItems: 'stretch',
    marginVertical: 2,
  },
  tabContainer: {
    flexDirection: 'row',
    backgroundColor: Colors.surfaceHighlight,
    borderRadius: Layout.borderRadius,
    padding: 6,
    marginBottom: 8,
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.08)',
  },
  tab: {
    flex: 1,
    paddingVertical: 8,
    alignItems: 'center',
    borderRadius: Layout.borderRadius - 6,
    overflow: 'hidden',
  },
  activeTab: {
    backgroundColor: 'transparent',
    borderWidth: 0,
  },
  tabText: {
    ...Typography.body,
    color: Colors.textMuted,
    fontWeight: '800',
    letterSpacing: 1,
    zIndex: 2,
  },
  activeTabText: {
    color: Colors.isDark ? '#FFF' : Colors.accent,
  },
  controlsContainer: {
    flex: 1,
    padding: 10,
    backgroundColor: Colors.isDark ? 'rgba(21, 25, 40, 0.7)' : Colors.surface,
    borderRadius: Layout.borderRadius + 4,
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255, 255, 255, 0.1)' : 'rgba(0,0,0,0.08)',
  },
  modesContainer: {
    flexDirection: 'row',
    justifyContent: 'center',
    gap: 6,
    marginBottom: 12,
  },
  modePill: {
    paddingHorizontal: 16,
    paddingVertical: 8,
    borderRadius: Layout.borderRadius,
    backgroundColor: Colors.background,
    marginRight: 12,
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255, 255, 255, 0.05)' : 'rgba(0,0,0,0.08)',
    overflow: 'hidden',
    justifyContent: 'center',
  },
  activeModePill: {
    backgroundColor: 'transparent',
    borderColor: 'transparent',
  },
  modePillText: {
    color: Colors.textMuted,
    fontWeight: '600',
  },
  activeModePillText: {
    color: Colors.isDark ? Colors.background : Colors.surface,
    fontWeight: 'bold',
  },
  activeModeContainer: {
    flex: 1,
    overflow: 'hidden',
  },
  controlRow: {
    marginTop: 6,
  },
  placeholderSlider: {
    height: 8,
    backgroundColor: Colors.surfaceHighlight,
    borderRadius: 4,
    marginTop: 8,
    overflow: 'hidden',
  },
  sliderFill: {
    height: '100%',
    backgroundColor: Colors.secondary,
    borderRadius: 4,
  },
  colorGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    marginTop: 16,
    gap: 12,
  },
  colorButton: {
    width: 32,
    height: 32,
    borderRadius: 16,
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255,255,255,0.2)' : 'rgba(0,0,0,0.15)',
  },
  selectedColorButton: {
    borderWidth: 3,
    borderColor: Colors.text,
    transform: [{ scale: 1.1 }]
  },
  presetContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-between',
    alignContent: 'flex-start',
    gap: 6
  },
  presetCard: {
    width: '48%',
    minHeight: 80,
    padding: 6,
    backgroundColor: Colors.isDark ? 'rgba(0,0,0,0.6)' : 'rgba(0,0,0,0.04)',
    borderRadius: 16,
    borderWidth: 1.5,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.25,
    shadowRadius: 10,
  },
  presetTitle: {
    ...Typography.body,
    fontWeight: 'bold',
    color: Colors.text,
  },
  presetDesc: {
    ...Typography.caption,
    marginTop: 4,
    color: Colors.textMuted,
  },
  sceneContainer: {
    backgroundColor: Colors.isDark ? '#050505' : Colors.surfaceHighlight,
    borderRadius: 24,
    padding: 2,
    marginTop: 8,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.08)',
  },
  sceneHeader: {
    padding: 16,
    borderBottomWidth: 1,
    borderBottomColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.08)',
    alignItems: 'center',
  },
  sceneTitle: {
    ...Typography.title,
    color: Colors.text,
    fontSize: 18,
  },
  rbmWheelSection: {
    height: 180,
    backgroundColor: Colors.isDark ? '#050505' : Colors.surfaceHighlight,
    justifyContent: 'center',
    alignItems: 'center',
  },
  sceneSlidersContainer: {
    padding: 16,
    backgroundColor: Colors.isDark ? 'rgba(255,255,255,0.02)' : 'rgba(0,0,0,0.02)',
  },
  sceneLabel: {
    ...Typography.caption,
    color: Colors.textMuted,
    fontSize: 12,
    textTransform: 'uppercase',
    letterSpacing: 1,
    fontWeight: '700',
  },
  musicToggleHeader: {
    flexDirection: 'row',
    backgroundColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : Colors.surfaceHighlight,
    borderRadius: 25,
    padding: 4,
    alignItems: 'center',
    marginVertical: 4,
    marginHorizontal: 4,
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255,255,255,0.1)' : 'rgba(0,0,0,0.08)',
  },
  musicToggleOption: {
    flex: 1,
    paddingVertical: 10,
    alignItems: 'center',
    borderRadius: 20,
  },
  musicToggleActive: {
    backgroundColor: Colors.primary,
  },
  musicToggleActiveText: {
    color: Colors.isDark ? '#FFF' : '#000',
    fontWeight: 'bold',
  },
  musicToggleText: {
    color: Colors.textMuted,
    fontSize: 11,
    fontWeight: '700',
  },
  musicModeIndicator: {
    width: '100%',
    alignItems: 'center',
    justifyContent: 'center',
  },
  musicModeCircle: {
    width: 44,
    height: 44,
    borderRadius: 22,
    borderWidth: 2,
    borderColor: Colors.primary,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: Colors.isDark ? 'rgba(0,0,0,0.6)' : Colors.surfaceHighlight,
  },
  musicModeNumber: {
    color: Colors.text,
    fontSize: 18,
    fontWeight: 'bold',
  },
  musicModeRefresh: {
    position: 'absolute',
    top: -5,
    right: -5,
    width: 20,
    height: 20,
    borderRadius: 10,
    backgroundColor: Colors.primary,
    alignItems: 'center',
    justifyContent: 'center',
  },

  micControlSection: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    width: '100%',
    paddingHorizontal: 10,
    marginBottom: 4,
  },
  micIconBtn: {
    flex: 1,
    alignItems: 'center',
    padding: 8,
    borderRadius: 12,
  },
  micBtnActive: {
    backgroundColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.05)',
  },
  micIconCircle: {
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.05)',
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 6,
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255,255,255,0.1)' : 'rgba(0,0,0,0.1)',
  },
  micIconText: {
    fontSize: 24,
    color: Colors.textMuted,
  },
  micSubText: {
    fontSize: 9,
    color: Colors.textMuted,
    textAlign: 'center',
    textTransform: 'uppercase',
    fontWeight: '600',
  },
  playButtonMain: {
    width: 52,
    height: 52,
    borderRadius: 26,
    borderWidth: 2,
    borderColor: Colors.primary,
    alignItems: 'center',
    justifyContent: 'center',
    marginHorizontal: 10,
  },
  playIconInner: {
    width: 42,
    height: 42,
    borderRadius: 21,
    backgroundColor: Colors.primary,
    alignItems: 'center',
    justifyContent: 'center',
  },
  musicOptionsGrid: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    paddingHorizontal: 10,
    marginBottom: 4,
  },
  radioItem: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  radioOuter: {
    width: 20,
    height: 20,
    borderRadius: 10,
    borderWidth: 2,
    borderColor: Colors.textMuted,
    marginRight: 8,
    alignItems: 'center',
    justifyContent: 'center',
  },
  radioActive: {
    borderColor: Colors.primary,
  },
  radioInner: {
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: Colors.primary,
  },
  radioLabel: {
    color: Colors.text,
    fontSize: 13,
    fontWeight: '600',
  },
  gradientSliderTrack: {
    height: 6,
    borderRadius: 3,
    backgroundColor: Colors.surfaceHighlight,
    overflow: 'hidden',
  },
  musicSettingsToggleRow: {
    flexDirection: 'row',
    justifyContent: 'flex-start',
    gap: 30,
    marginBottom: 4,
  },
  floatingDock: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: Colors.isDark ? 'rgba(8, 10, 16, 0.98)' : 'rgba(255, 255, 255, 0.98)',
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(0, 240, 255, 0.35)' : 'rgba(0, 200, 255, 0.4)',
    borderRadius: 30,
    paddingVertical: 10,
    paddingHorizontal: 20,
    marginHorizontal: 16,
    marginBottom: 8,
    marginTop: 8,
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 8 },
    shadowOpacity: 0.6,
    shadowRadius: 20,
    elevation: 15
  },
  dockIconCont: {
    width: 44,
    height: 44,
    borderRadius: 22,
    backgroundColor: Colors.isDark ? 'rgba(255,255,255,0.03)' : 'rgba(0,0,0,0.05)',
    alignItems: 'center',
    justifyContent: 'center',
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.08)',
  },
  dockIconActive: {
    backgroundColor: Colors.primary,
    borderColor: Colors.primary,
    shadowColor: Colors.primary,
    shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.9,
    shadowRadius: 12,
    elevation: 8,
    transform: [{ scale: 1.15 }]
  },
  dockActiveText: {
    color: Colors.primary,
    fontFamily: 'Righteous',
    fontSize: 18,
    textAlign: 'center',
    letterSpacing: 4,
    textTransform: 'uppercase',
    textShadowColor: Colors.primary,
    textShadowOffset: { width: 0, height: 0 },
    textShadowRadius: 24,
    opacity: 1.0,
    marginTop: 4,
  }
});
