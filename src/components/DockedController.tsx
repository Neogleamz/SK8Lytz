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
import { View, Text, StyleSheet, TouchableOpacity, ScrollView, Platform, Modal, TextInput, Alert, Dimensions, FlatList } from 'react-native';
import { useSessionTracking } from '../hooks/useSessionTracking';
import { useStreetMode } from '../hooks/useStreetMode';
import { useFavorites } from '../hooks/useFavorites';
import { useDockedControllerState } from '../hooks/useDockedControllerState';
import { useOptimisticBLE } from '../hooks/useOptimisticBLE';
import { useMusicMode, MUSIC_PATTERNS, getMusicPatternLabel } from '../hooks/useMusicMode';
import { useCuratedPicks } from '../hooks/useCuratedPicks';
import { useControllerAnalytics } from '../hooks/useControllerAnalytics';
import { hexToHue, hueToHex, getColorName, hexToRgb, COLOR_PRESET_PALETTE, PRESET_HUE_MAP } from '../utils/ColorUtils';
import AnalogGauge from './docked/AnalogGauge';
import FloatingDock from './docked/FloatingDock';
import type { ModeType, BleConnectionState, IDeviceState, IFavoriteState, IQuickPreset } from '../types/dashboard.types';

import { LinearGradient } from 'expo-linear-gradient';
import { Audio } from 'expo-av';
import { Typography, Layout, Spacing } from '../theme/theme';

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
import { crewService } from '../services/CrewService';
import { SpeedTrackingService } from '../services/SpeedTrackingService';
import CommunityModal from './CommunityModal';
import SessionSummaryModal from './SessionSummaryModal';
import type { ISessionSnapshot } from '../services/SpeedTrackingService';
import { Accelerometer } from 'expo-sensors';
import { getLocalProfileById, LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import * as Location from 'expo-location';
import Svg, { Path, Circle, Defs, LinearGradient as SvgLinearGradient, Stop } from 'react-native-svg';
import MarqueeText from './MarqueeText';
import { STORAGE_PREFIX, HW_SPEED_MAX } from '../constants/AppConstants';
import { normalizeUISpeedToHardware } from '../utils/NormalizationUtils';

// hexToHue — now imported from '../utils/ColorUtils'

import type { MotionState } from '../hooks/useStreetMode';

// AnalogGauge — now imported from './docked/AnalogGauge'
// FixedPatternPreviewRow — kept inline (tightly coupled to parent animation state)

// eslint-disable-next-line @typescript-eslint/no-unused-vars
const _AnalogGaugeRemoved = null; // placeholder to preserve line numbering




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
    <View style={{ alignItems: 'center', marginHorizontal: Spacing.xxs }}>
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
});



type ProductType = string;


// MUSIC_PATTERNS — now imported from '../hooks/useMusicMode'

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
    <View style={{ flex: 1, marginRight: Spacing.sm, height: 8, overflow: 'hidden' }}>
      <View style={{ flex: 1, flexDirection: 'row', gap: Spacing.xxs }}>
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


// IDeviceState, IFavoriteState, IQuickPreset — canonical source: '../types/dashboard.types'
// Re-exported for backward compatibility with any remaining consumers.
export type { IDeviceState, IFavoriteState, IQuickPreset } from '../types/dashboard.types';

interface Sk8lytzControllerProps {
  hwSettings?: any;
  lockedProduct?: ProductType;
  isPaired?: boolean;
  bleState?: BleConnectionState;
  points?: number;
  devices?: IDeviceState[];
  onLongPressDevice?: (device: IDeviceState) => void;
  writeToDevice?: (payload: number[]) => Promise<void | boolean>;
  isPoweredOn?: boolean;
  onDisconnect?: () => void;
  /** 'leader' = broadcast changes, 'member' = receive changes, null = solo */
  crewRole?: 'leader' | 'member' | null;
  /** Called with full scene snapshot whenever any mode/color changes (leader only) */
  onCrewSceneChange?: (scene: Record<string, any>) => void;
  /** Triggered to persist the active pattern name to dashboard group persistent storage */
  onPatternChanged?: (patternName: string) => void;
}

export type DockedControllerHandle = {
  applyCloudScene: (scenePayload: any) => void;
  loadFavorite: (fav: IFavoriteState) => void;
  setActiveMode: (mode: ModeType) => void;
  setBrightness: (val: number) => void;
  setSpeed: (val: number) => void;
  handleRbmChange: (id: number) => void;
  applySpatialSegments: (segments: any[]) => void;
};

// CURATED_PRESETS logic moved to internal component state for Supabase updating

// MarqueeText moved to standalone component MarqueeText.tsx

const DockedController = React.forwardRef<DockedControllerHandle, Sk8lytzControllerProps>(
  function DockedController({ hwSettings, lockedProduct, isPaired, bleState, points, devices, onLongPressDevice, writeToDevice: parentWriteToDevice, isPoweredOn = true, onDisconnect, crewRole, onCrewSceneChange, onPatternChanged }: Sk8lytzControllerProps, ref) {
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

    // ── Optimistic BLE Bridge (Ghost Standard) ─────────────────────────────
    const { optimisticWrite, writeStatus } = useOptimisticBLE({
      writeToDevice: parentWriteToDevice as ((payload: number[], targetDeviceId?: string) => Promise<boolean>) | undefined,
      onReconcile: () => {
        // On BLE failure: snap back to last confirmed state
        // Currently a no-op — the UI already reflects the optimistic state.
        // Future: restore from captureEntireState() snapshot.
        AppLogger.warn('[DockedController] BLE write reconciled — UI may be stale');
      },
      debounceMs: 40,
    });

    const writeToDevice = async (payload: number[]) => {
      // Short-circuit dead writes if we are disconnected or disconnecting
      if (bleState === 'DISCONNECTING' || bleState === 'IDLE' || bleState === 'ERROR') return;
      setLastSentPayload([...payload]);
      await optimisticWrite(payload);
    };

    const [recording, setRecording] = useState<Audio.Recording | null>(null);
    const [audioMagnitude, setAudioMagnitude] = useState<number>(0);
    const magnitudeInterval = React.useRef<NodeJS.Timeout | null>(null);

    const {
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
      applyCloudScene: baseApplyCloudScene,
      captureEntireState: baseCaptureEntireState,
      applySpatialSegments
    } = useDockedControllerState(lockedProduct || 'HALOZ');
    // Favorites & Quick Presets Domain Hook
    const {
      favorites,
      activeFavoriteId,
      setActiveFavoriteId,
      quickPresets,
      setQuickPresets,
      activeQuickPresetIndex,
      setActiveQuickPresetIndex,
      promptState,
      promptName,
      setPromptName,
      favPromptTargetId,
      quickPromptTargetIndex,
      openFavoritePrompt,
      openPresetPrompt,
      closePrompt,
      saveFavorite,
      deleteFavorite,
      saveQuickPreset
    } = useFavorites();

    const {
      sessionState,
      startSession,
      stopSession: stopSessionRecording,
      dismissModal: dismissSessionModal,
      sessionSummary,
      showSessionModal,
      setShowSessionModal,
      saveSession,
      sessionStartTimeRef,
      sessionSpeedSamplesRef,
      sessionDistanceMilesRef,
      sessionPeakGForceRef,
      sessionPeakSpeedRef,
    } = useSessionTracking();
    
    const {
      streetSensitivity,
      setStreetSensitivity,
      streetCruiseColor,
      setStreetCruiseColor,
      streetBrakeColor,
      setStreetBrakeColor,
      isStreetBraking,
      motionState,
      motionStateRef,
      gpsSpeed,
      peakGForce,
      applyStreetPattern,
    } = useStreetMode({
      activeMode,
      writeToDevice: parentWriteToDevice,
      hwSettings,
      points,
      activeProduct,
      brightness,
      speed,
      deviceContext: { target: 'none' }, // Temporary
      sessionStartTimeRef,
      sessionSpeedSamplesRef,
      sessionDistanceMilesRef,
      sessionPeakSpeedRef,
    });

    const captureEntireState = () => baseCaptureEntireState(streetSensitivity, streetCruiseColor, streetBrakeColor);
    const applyCloudScene = (scenePayload: any) => baseApplyCloudScene(scenePayload, setStreetSensitivity, setStreetCruiseColor, setStreetBrakeColor);

    // Favorites Array — now managed by useFavorites hook above

    // Expose control methods to parent via ref for Voice and Crew coordination
    React.useImperativeHandle(ref, () => ({
      applyCloudScene,
      loadFavorite,
      setActiveMode,
      setBrightness,
      setSpeed,
      handleRbmChange: (id: number) => {
        setSelectedPatternId(id);
        if (writeToDevice) writeToDevice(ZenggeProtocol.setCustomRbm(id, speed, brightness));
      },
      applySpatialSegments
    }), [speed, brightness, writeToDevice]);


    // ── Global Device Context for Analytics ────────────────────────────────────
    const deviceContext = React.useMemo(() => {
      if (!devices || devices.length === 0) return { target: 'none' };
      if (devices.length === 1) return { target: 'device', deviceId: devices[0].id };
      return { target: 'group', deviceIds: devices.map(d => d.id), groupSize: devices.length };
    }, [devices]);

    // (useStreetMode and useSessionTracking placed higher up context)
    /** Convenience alias for JSX readability */
    const sessionActive = sessionState === 'RECORDING';


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


    const [isDiyBuilderExpanded, setIsDiyBuilderExpanded] = useState(false);

    const handleSaveFavoriteClick = () => {
      if (activeFavoriteId) {
        const activeFav = favorites.find(f => f.id === activeFavoriteId);
        if (activeFav) {
          openFavoritePrompt(activeFav.id, activeFav.name);
          return;
        }
      }
      let defaultName = '';
      try { defaultName = currentStatusText || `${activeMode} Preset`; } catch (e) { defaultName = `${activeMode} Preset`; }
      openFavoritePrompt(undefined, defaultName);
    };

    const handleConfirmSaveFavorite = () => {
      let defaultName = '';
      try { defaultName = currentStatusText || `${activeMode} Preset`; } catch (e) { defaultName = `${activeMode} Preset`; }
      const name = promptName.trim() || defaultName;

      const capturedState = {
        mode: activeMode === 'MULTIMODE' ? fixedSubMode : activeMode,
        color: selectedColor,
        patternId: activeMode === 'MUSIC' ? musicPatternId : (activeMode === 'MULTIMODE' ? fixedPatternId : selectedPatternId),
        speed,
        brightness,
        fixedColorMode, fixedFgColor, fixedBgColor, fixedHue,
        multiColors, multiTransition, multiLength,
        musicPrimaryColor, musicSecondaryColor, micSensitivity, micSource, musicMatrixStyle
      };

      saveFavorite(capturedState, name);
    };

    const loadFavorite = (favRaw: IFavoriteState, context: 'FAVORITE' | 'PICK' | 'COMMUNITY' = 'FAVORITE') => {
      const fav = favRaw as IFavoriteState;
      AppLogger.log(context === 'PICK' ? 'PICK_LOADED' : 'FAVORITE_LOADED', { name: fav.name, mode: fav.mode });
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
        setSelectedPatternId(fav.patternId ?? 0);
        if (writeToDevice) writeToDevice(ZenggeProtocol.setCustomRbm(fav.patternId ?? 0, fav.speed, fav.brightness));
      } else if (legacyMode === 'MUSIC') {
        setActiveMode('MUSIC');
        setMusicPatternId(fav.patternId ?? 0);
        handleMusicChange(fav.patternId ?? 0, micSensitivity, fav.brightness, micSource);
      } else if (legacyMode === 'CAMERA') {
        setActiveMode('CAMERA');
      } else if (legacyMode === 'FAVORITES') {
        setActiveMode('FAVORITES');
      } else if (legacyMode === 'MULTIMODE' || legacyMode === 'PATTERN') {
        setActiveMode('MULTIMODE');
        setFixedSubMode('PATTERN');
        setFixedPatternId(fav.patternId ?? 0);
        setFixedColorMode(fav.fixedColorMode ?? 'FOREGROUND');
        setFixedFgColor(fav.fixedFgColor ?? '#FFFFFF');
        setFixedBgColor(fav.fixedBgColor ?? '#000000');
        applyFixedPattern(fav.patternId ?? 0, fav.fixedFgColor ?? '#FFFFFF', fav.fixedBgColor ?? '#000000', fav.speed, fav.brightness);
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
        if (fav.color) {
          const fallbackColor = fav.color;
          setTimeout(() => {
            sendColor(parseInt(fallbackColor.slice(1, 3), 16) || 0, parseInt(fallbackColor.slice(3, 5), 16) || 0, parseInt(fallbackColor.slice(5, 7), 16) || 0);
          }, 100);
        }
      }
      
      if (context === 'PICK') {
        AppLogger.log('PICK_SELECTED', { id: fav.id, name: fav.name || fav.customName, mode: legacyMode });
      } else {
        AppLogger.log('FAVORITE_RENDERED', { id: fav.id, name: fav.name || fav.customName, mode: legacyMode, patternId: fav.patternId });
      }
    };

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
    const clampSpeed = (uiSpeed: number): number => normalizeUISpeedToHardware(uiSpeed);

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
      const profile = getLocalProfileById(hwSettings?.type || '');
      const isRingShape = profile?.vizShape === 'RING';
      const hwSpd = Math.min(spd, ZenggeProtocol.ANIM_SPEED_MAX);

      const red = { r: Math.round(255 * factor), g: 0, b: 0 };
      const white = { r: Math.round(255 * factor), g: Math.round(255 * factor), b: Math.round(255 * factor) };
      const yellow = { r: Math.round(255 * factor), g: Math.round(255 * factor), b: 0 };
      const off = { r: 0, g: 0, b: 0 };

      let arr: { r: number; g: number; b: number }[];

      if (isRingShape) {
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
      const CACHE_KEY = `${STORAGE_PREFIX}PicksCache`;

      const loadFromCache = async () => {
        try {
          const cached = await AsyncStorage.getItem(CACHE_KEY);
          if (cached) {
            const parsed = JSON.parse(cached);
            if (parsed && Array.isArray(parsed) && parsed.length > 0) {
              setCuratedPresets(parsed);
              setPicksLoading(false);
            }
          }
        } catch (e) {
          console.warn('[SK8Lytz Picks] Cache read error:', e);
        }
      };

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
            
            // Only update and re-render if the fetched data differs to prevent flicker
            setCuratedPresets((prev) => {
               if (JSON.stringify(prev) !== JSON.stringify(mapped)) {
                 return mapped;
               }
               return prev;
            });
            
            // Update cache asynchronously
            AsyncStorage.setItem(CACHE_KEY, JSON.stringify(mapped)).catch(() => {});
          }
        } catch (e) {
          console.warn('[SK8Lytz Picks] Exception fetching from DB:', e);
        } finally {
          setPicksLoading(false);
        }
      };

      // 1. Instantly load from cache to populate UI
      loadFromCache().then(() => {
        // 2. Perform background revalidation
        fetchPicks();
      });
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
      AsyncStorage.getItem(`${STORAGE_PREFIX}ControllerState`).then((saved) => {
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


    }, []);

    React.useEffect(() => {
      const stateBlob = {
        activeMode, selectedColor, selectedPatternId, brightness, speed,
        micSensitivity, musicHue, musicSecondaryHue, musicPrimaryColor, musicSecondaryColor, musicMatrixStyle, musicPatternId, micSource, musicSetting,
        fixedPatternId, fixedColorMode, fixedFgColor, fixedBgColor, fixedHue
      };
      AsyncStorage.setItem(`${STORAGE_PREFIX}ControllerState`, JSON.stringify(stateBlob)).catch(() => { });
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

      // [DEBUG LOGGING]
      AppLogger.log("MUSIC_CONFIG_REQUESTED", { patternId, c1Hex: color1Hex, c2Hex: color2Hex, matrix });

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

    // Relays the dynamically generated pattern name upward to persist dashboard group state
    React.useEffect(() => {
      if (onPatternChanged) {
        onPatternChanged(currentStatusText);
      }
    }, [currentStatusText, onPatternChanged]);

    return (
      <View style={styles.container}>

        {/* Product Selector - Only show if NO lockedProduct is provided */}
        {!lockedProduct && (
          <View style={styles.tabContainer}>
            {LOCAL_PRODUCT_CATALOG.filter(p => (p as any).isActive !== false).map((profile) => (
              <TouchableOpacity
                key={profile.id}
                style={[styles.tab, activeProduct === profile.id && styles.activeTab]}
                onPress={() => setActiveProduct(profile.id as any)}
              >
                {activeProduct === profile.id && (
                  <LinearGradient 
                    colors={[profile.vizThemeColor || Colors.primary, Colors.accent] as any} 
                    start={{ x: 0, y: 0 }} 
                    end={{ x: 1, y: 1 }} 
                    style={StyleSheet.absoluteFill} 
                  />
                )}
                <Text style={[styles.tabText, activeProduct === profile.id && styles.activeTabText]}>
                  {profile.displayName.replace('™', '')}
                </Text>
              </TouchableOpacity>
            ))}
          </View>
        )}

        {/* Visual Product Shape Selector/Indicator - ENLARGED FOCUS */}
        <View style={styles.visualizerWrapper}>
          {/* BLE Write Status Indicator */}
          {writeStatus === 'PENDING' && (
            <View style={{ width: 6, height: 6, borderRadius: 3, backgroundColor: '#00F0FF', opacity: 0.7, position: 'absolute', right: 8, top: 8, zIndex: 10 }} />
          )}
          {writeStatus === 'RECONCILED' && (
            <View style={{ width: 6, height: 6, borderRadius: 3, backgroundColor: '#FF4444', position: 'absolute', right: 8, top: 8, zIndex: 10 }} />
          )}
          <View style={{ marginBottom: Spacing.sm, width: '100%' }}>
            <TouchableOpacity
              style={{ position: 'absolute', top: 12, right: 16, zIndex: 100, backgroundColor: 'rgba(255,255,255,0.1)', padding: Spacing.sm, borderRadius: 20 }}
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

        <View style={[styles.controlsContainer, { padding: Spacing.xs, overflow: 'hidden' }]}>
          <View style={[styles.activeModeContainer, { flex: 1, justifyContent: 'space-evenly' }]}>
            {activeMode === 'FAVORITES' && (
              <View style={{ flex: 1, paddingVertical: Layout.padding, paddingBottom: Spacing.xl, justifyContent: 'space-between' }}>
                <View style={{ flex: 1 }}>
                  <Text style={[Typography.title, isDark && { color: '#FFF' }, { fontSize: 13, paddingHorizontal: Layout.padding, marginBottom: Spacing.sm }]}>YOURS</Text>
                
                <FlatList
                  style={{ flex: 1 }}
                  horizontal
                  showsHorizontalScrollIndicator={false}
                  data={favorites.length > 0 ? favorites : [null as any]}
                  keyExtractor={(item, index) => item ? item.id : `empty-yours-${index}`}
                  contentContainerStyle={{ paddingHorizontal: Layout.padding, flexGrow: 1 }}
                  renderItem={({ item: fav }) => {
                    const cardWidth = (Dimensions.get('window').width - (Layout.padding * 2)) / 3.5;
                    
                    if (!fav) {
                      return <View style={[styles.presetCard, { width: cardWidth, marginHorizontal: Spacing.xs, borderWidth: 1.5, borderStyle: 'dashed', borderColor: 'rgba(255,255,255,0.08)', backgroundColor: 'transparent', elevation: 0, shadowOpacity: 0 }]} />;
                    }

                    const glow = fav.mode === 'MUSIC' ? (fav.musicPrimaryColor || fav.musicSecondaryColor || Colors.primary) :
                                 (fav.mode === 'PATTERN' || fav.mode === 'MULTIMODE') ? (fav.fixedFgColor || fav.fixedBgColor || Colors.primary) :
                                 (fav.mode === 'MULTI' || fav.mode === 'BUILDER') ? (fav.multiColors?.[0] || Colors.primary) : Colors.primary;

                    let gradColors: string[] = [glow, glow];
                    if (fav.mode === 'MUSIC' && fav.musicPrimaryColor) gradColors = [fav.musicPrimaryColor, fav.musicSecondaryColor || fav.musicPrimaryColor];
                    else if ((fav.mode === 'PATTERN' || fav.mode === 'MULTIMODE') && fav.fixedFgColor) gradColors = [fav.fixedFgColor, fav.fixedBgColor || fav.fixedFgColor];
                    else if ((fav.mode === 'MULTI' || fav.mode === 'BUILDER') && fav.multiColors && fav.multiColors.length > 0) gradColors = fav.multiColors.length === 1 ? [fav.multiColors[0], fav.multiColors[0]] : fav.multiColors;

                    return (
                      <TouchableOpacity
                        style={{ flex: 1, marginHorizontal: Spacing.xs, shadowColor: glow, shadowOffset: { width: 0, height: 0 }, shadowOpacity: 0.5, shadowRadius: 10, elevation: 8 }}
                        onPress={() => loadFavorite(fav)}
                      >
                        <LinearGradient
                          colors={gradColors as any}
                          start={{ x: 0, y: 0 }}
                          end={{ x: 1, y: 1 }}
                          style={{ flex: 1, width: cardWidth, borderRadius: 9, padding: 1.5 }}
                        >
                          <View style={[styles.presetCard, { flex: 1, width: '100%', marginHorizontal: 0, borderWidth: 0, borderRadius: 8, justifyContent: 'flex-start', paddingVertical: Spacing.md, paddingHorizontal: Spacing.sm }]}>
                            <TouchableOpacity
                              style={{ position: 'absolute', right: 4, top: 4, zIndex: 10, padding: Spacing.xs }}
                              onPress={() => {
                                openFavoritePrompt(fav.id, fav.name);
                              }}
                            >
                              <MaterialCommunityIcons name="pencil-outline" size={12} color={Colors.textMuted} />
                            </TouchableOpacity>

                            <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', width: '100%' }}>
                              <MaterialCommunityIcons 
                                name={fav.mode === 'MUSIC' ? 'microphone-outline' : (fav.mode === 'RBM' || fav.mode === 'PROGRAMS') ? 'animation-play' : (fav.mode === 'MULTI' || fav.mode === 'BUILDER') ? 'shape-square-plus' : 'speedometer'} 
                                size={24} 
                                color={glow} 
                                style={{ marginBottom: Spacing.sm }} 
                              />
                              
                              <MarqueeText style={[styles.presetTitle, { fontSize: 13, textAlign: 'center', width: '100%' }]}>{fav.name}</MarqueeText>
                            </View>
                            
                            <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', width: '100%', marginBottom: Spacing.sm, opacity: 0.8, paddingHorizontal: Spacing.xs }}>
                              <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.xxs }}>
                                {fav.mode === 'MUSIC' ? (
                                  <><MaterialCommunityIcons name="microphone-outline" size={10} color={glow} /><Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(fav.micSensitivity || fav.speed || 50)}%</Text></>
                                ) : (
                                  <><MaterialCommunityIcons name="speedometer" size={10} color={glow} /><Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(fav.speed || 50)}%</Text></>
                                )}
                              </View>
                              <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.xxs }}>
                                <MaterialCommunityIcons name="brightness-6" size={10} color={glow} />
                                <Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(fav.brightness || 100)}%</Text>
                              </View>
                            </View>
                            
                            {(() => {
                              if (fav.mode === 'MUSIC') {
                                return (
                                  <View style={{ width: '80%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', alignSelf: 'center', marginTop: Spacing.xs }}>
                                    <View style={{ flex: 1, backgroundColor: fav.musicPrimaryColor || '#00FFFF' }} />
                                    <View style={{ flex: 1, backgroundColor: fav.musicSecondaryColor || '#FF00FF' }} />
                                  </View>
                                );
                              } else if (fav.mode === 'PATTERN' || fav.mode === 'MULTIMODE') {
                                return (
                                  <View style={{ width: '80%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', alignSelf: 'center', marginTop: Spacing.xs }}>
                                    <View style={{ flex: 1, backgroundColor: fav.fixedFgColor || '#FFFFFF' }} />
                                    <View style={{ flex: 1, backgroundColor: fav.fixedBgColor || '#000000' }} />
                                  </View>
                                );
                              } else if (fav.mode === 'MULTI' || fav.mode === 'BUILDER') {
                                const colors = fav.multiColors || ['#FFFFFF'];
                                return (
                                  <View style={{ width: '90%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', alignSelf: 'center', marginTop: Spacing.xs }}>
                                    {colors.map((c: string, i: number) => <View key={i} style={{ flex: 1, backgroundColor: c }} />)}
                                  </View>
                                );
                              } else {
                                return <View style={{ height: 6, width: '100%', marginTop: Spacing.xs }} />;
                              }
                            })()}
                          </View>
                        </LinearGradient>
                      </TouchableOpacity>
                    );
                  }}
                />
                </View>

                <View style={{ flex: 1, marginTop: Spacing.lg }}>
                  <Text style={[Typography.title, isDark && { color: '#FFF' }, { fontSize: 13, paddingHorizontal: Layout.padding, marginBottom: Spacing.sm }]}>SK8Lytz Picks</Text>

                  <FlatList
                  style={{ flex: 1 }}
                  horizontal
                  showsHorizontalScrollIndicator={false}
                  data={curatedPresets.length > 0 ? curatedPresets : [null as any]}
                  keyExtractor={(item, index) => item ? item.id : `empty-picks-${index}`}
                  contentContainerStyle={{ paddingHorizontal: Layout.padding, flexGrow: 1 }}
                  renderItem={({ item: fav }) => {
                    const cardWidth = (Dimensions.get('window').width - (Layout.padding * 2)) / 3.5;
                    
                    if (!fav) {
                      return <View style={[styles.presetCard, { width: cardWidth, marginHorizontal: Spacing.xs, borderWidth: 1.5, borderStyle: 'dashed', borderColor: 'rgba(255,255,255,0.08)', backgroundColor: 'transparent', elevation: 0, shadowOpacity: 0 }]} />;
                    }

                    const glow = fav.mode === 'MUSIC' ? (fav.musicPrimaryColor || fav.musicSecondaryColor || Colors.secondary) :
                                 (fav.mode === 'PATTERN' || fav.mode === 'MULTIMODE') ? (fav.fixedFgColor || fav.fixedBgColor || Colors.secondary) :
                                 (fav.mode === 'MULTI' || fav.mode === 'BUILDER') ? (fav.multiColors?.[0] || Colors.secondary) : Colors.secondary;

                    let gradColors: string[] = [glow, glow];
                    if (fav.mode === 'MUSIC' && fav.musicPrimaryColor) gradColors = [fav.musicPrimaryColor, fav.musicSecondaryColor || fav.musicPrimaryColor];
                    else if ((fav.mode === 'PATTERN' || fav.mode === 'MULTIMODE') && fav.fixedFgColor) gradColors = [fav.fixedFgColor, fav.fixedBgColor || fav.fixedFgColor];
                    else if ((fav.mode === 'MULTI' || fav.mode === 'BUILDER') && fav.multiColors && fav.multiColors.length > 0) gradColors = fav.multiColors.length === 1 ? [fav.multiColors[0], fav.multiColors[0]] : fav.multiColors;

                    return (
                      <TouchableOpacity
                        style={{ flex: 1, marginHorizontal: Spacing.xs, shadowColor: glow, shadowOffset: { width: 0, height: 0 }, shadowOpacity: 0.5, shadowRadius: 10, elevation: 8 }}
                        onPress={() => loadFavorite(fav, 'PICK')}
                      >
                        <LinearGradient
                          colors={gradColors as any}
                          start={{ x: 0, y: 0 }}
                          end={{ x: 1, y: 1 }}
                          style={{ flex: 1, width: cardWidth, borderRadius: 9, padding: 1.5 }}
                        >
                          <View style={[styles.presetCard, { flex: 1, width: '100%', marginHorizontal: 0, borderWidth: 0, borderRadius: 8, justifyContent: 'flex-start', paddingVertical: Spacing.md, paddingHorizontal: Spacing.sm }]}>
                            <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', width: '100%' }}>
                              <MaterialCommunityIcons 
                                name={fav.mode === 'MUSIC' ? 'microphone-outline' : (fav.mode === 'RBM' || fav.mode === 'PROGRAMS') ? 'animation-play' : (fav.mode === 'MULTI' || fav.mode === 'BUILDER') ? 'shape-square-plus' : 'speedometer'} 
                                size={24} 
                                color={glow} 
                                style={{ marginBottom: Spacing.sm }} 
                              />
                              
                              <MarqueeText style={[styles.presetTitle, { fontSize: 13, textAlign: 'center', width: '100%' }]}>{fav.customName || fav.name}</MarqueeText>
                            </View>
                            
                            <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', width: '100%', marginBottom: Spacing.sm, opacity: 0.8, paddingHorizontal: Spacing.xs }}>
                              <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.xxs }}>
                                {fav.mode === 'MUSIC' ? (
                                  <><MaterialCommunityIcons name="microphone-outline" size={10} color={glow} /><Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(fav.micSensitivity || fav.speed || 50)}%</Text></>
                                ) : (
                                  <><MaterialCommunityIcons name="speedometer" size={10} color={glow} /><Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(fav.speed || 50)}%</Text></>
                                )}
                              </View>
                              <View style={{ flexDirection: 'row', alignItems: 'center', gap: Spacing.xxs }}>
                                <MaterialCommunityIcons name="brightness-6" size={10} color={glow} />
                                <Text style={{ fontSize: 9, color: Colors.textMuted }}>{Math.round(fav.brightness || 100)}%</Text>
                              </View>
                            </View>
                            
                            {(() => {
                              if (fav.mode === 'MUSIC') {
                                return (
                                  <View style={{ width: '80%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', alignSelf: 'center', marginTop: Spacing.xs }}>
                                    <View style={{ flex: 1, backgroundColor: fav.musicPrimaryColor || '#00FFFF' }} />
                                    <View style={{ flex: 1, backgroundColor: fav.musicSecondaryColor || '#FF00FF' }} />
                                  </View>
                                );
                              } else if (fav.mode === 'PATTERN' || fav.mode === 'MULTIMODE') {
                                return (
                                  <View style={{ width: '80%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', alignSelf: 'center', marginTop: Spacing.xs }}>
                                    <View style={{ flex: 1, backgroundColor: fav.fixedFgColor || '#FFFFFF' }} />
                                    <View style={{ flex: 1, backgroundColor: fav.fixedBgColor || '#000000' }} />
                                  </View>
                                );
                              } else if (fav.mode === 'MULTI' || fav.mode === 'BUILDER') {
                                const colors = fav.multiColors || ['#FFFFFF'];
                                return (
                                  <View style={{ width: '90%', height: 6, borderRadius: 3, flexDirection: 'row', overflow: 'hidden', borderWidth: 1, borderColor: 'rgba(255,255,255,0.2)', alignSelf: 'center', marginTop: Spacing.xs }}>
                                    {colors.map((c: string, i: number) => <View key={i} style={{ flex: 1, backgroundColor: c }} />)}
                                  </View>
                                );
                              } else {
                                return <View style={{ height: 6, width: '100%', marginTop: Spacing.xs }} />;
                              }
                            })()}
                          </View>
                        </LinearGradient>
                      </TouchableOpacity>
                    );
                  }}
                />
                </View>
              </View>
            )}



            {activeMode === 'MULTIMODE' && (
              <View style={{ flex: 1, marginBottom: Spacing.sm, justifyContent: 'flex-start' }}>

                {/* UNIFIED PRO EFFECTS & POSITIONAL BUILDER */}
                {(fixedSubMode === 'PATTERN' || fixedSubMode === 'BUILDER') && (
                  <View style={{ flex: 1, width: '100%', marginBottom: Spacing.xs }}>

                    {/* UNIFIED TOGGLE */}
                    <View style={{ flexDirection: 'row', marginBottom: Spacing.sm, marginTop: Spacing.xxs, flexShrink: 0, minHeight: 36 }}>
                      <TouchableOpacity
                        onPress={() => {
                          setFixedSubMode('PATTERN');
                        }}
                        style={{ flex: 1, paddingVertical: Spacing.sm, alignItems: 'center', backgroundColor: fixedSubMode === 'PATTERN' ? Colors.primary : Colors.surfaceHighlight, borderTopLeftRadius: Layout.borderRadius, borderBottomLeftRadius: Layout.borderRadius }}
                      >
                        <Text style={{ color: fixedSubMode === 'PATTERN' ? '#000' : Colors.textMuted, fontWeight: 'bold' }}>Pro Effects</Text>
                      </TouchableOpacity>
                      <TouchableOpacity
                        onPress={() => {
                          AppLogger.log('BUILDER_UI_TOGGLED');
                          setFixedSubMode('BUILDER');
                        }}
                        style={{ flex: 1, paddingVertical: Spacing.sm, alignItems: 'center', backgroundColor: fixedSubMode === 'BUILDER' ? Colors.primary : Colors.surfaceHighlight, borderLeftWidth: 1, borderColor: 'rgba(255,255,255,0.05)', borderTopRightRadius: Layout.borderRadius, borderBottomRightRadius: Layout.borderRadius }}
                      >
                        <Text style={{ color: fixedSubMode === 'BUILDER' ? '#000' : Colors.textMuted, fontWeight: 'bold' }}>Builder</Text>
                      </TouchableOpacity>
                    </View>

                    {/* PRO EFFECTS TIER */}
                    {fixedSubMode === 'PATTERN' && (
                      <View style={{ flex: 1, paddingBottom: Spacing.sm }}>
                        <ScrollView
                          style={{ flex: 1, backgroundColor: Colors.isDark ? '#000000' : 'rgba(0,0,0,0.04)', borderRadius: 8 }}
                          contentContainerStyle={{ padding: Spacing.sm, flexDirection: 'row', flexWrap: 'wrap', justifyContent: 'space-between' }}
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
                              style={{ width: '48%', minHeight: 40, marginBottom: Spacing.sm, flexDirection: 'column', justifyContent: 'center', borderBottomWidth: 1, borderBottomColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.05)' }}
                            >
                              <Text style={{ color: fixedPatternId === effect.id ? Colors.primary : Colors.text, fontWeight: 'bold', fontSize: 11, marginBottom: Spacing.xs }} numberOfLines={1}>
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
              <View style={{ flex: 1, paddingHorizontal: Spacing.xs, paddingTop: Spacing.xs }}>
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
              <View style={{ flex: 1, paddingHorizontal: Spacing.xs, paddingTop: Spacing.xs, overflow: 'hidden' }}>
                {/* ── Matrix Style Selector: Light Screen (0x27) vs Light Bar (0x26) ── */}
                <View style={{ flexDirection: 'row', gap: Spacing.sm, paddingHorizontal: Spacing.xs, marginTop: Spacing.xxs, marginBottom: Spacing.md, flexShrink: 0 }}>
                  <TouchableOpacity
                    onPress={() => {
                      setMusicMatrixStyle(0x27);
                      handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, musicSecondaryColor, 0x27);
                    }}
                    style={{
                      flex: 1, paddingVertical: Spacing.md, borderRadius: 10, alignItems: 'center',
                      backgroundColor: musicMatrixStyle === 0x27 ? Colors.primary + '33' : 'rgba(255,255,255,0.05)',
                      borderWidth: 1.5, borderColor: musicMatrixStyle === 0x27 ? Colors.primary : 'rgba(255,255,255,0.1)'
                    }}
                  >
                    <Text style={{ color: musicMatrixStyle === 0x27 ? '#FFF' : Colors.textMuted, fontWeight: '900', fontSize: 10, letterSpacing: 1 }}>LIGHT SCREEN</Text>
                    <Text style={{ color: musicMatrixStyle === 0x27 ? Colors.primary : Colors.textMuted, fontSize: 8, opacity: 0.8 }}>0x27 (DENSE)</Text>
                  </TouchableOpacity>
                  <TouchableOpacity
                    onPress={() => {
                      setMusicMatrixStyle(0x26);
                      handleMusicChange(musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, musicSecondaryColor, 0x26);
                    }}
                    style={{
                      flex: 1, paddingVertical: Spacing.md, borderRadius: 10, alignItems: 'center',
                      backgroundColor: musicMatrixStyle === 0x26 ? Colors.accent + '33' : 'rgba(255,255,255,0.05)',
                      borderWidth: 1.5, borderColor: musicMatrixStyle === 0x26 ? Colors.accent : 'rgba(255,255,255,0.1)'
                    }}
                  >
                    <Text style={{ color: musicMatrixStyle === 0x26 ? '#FFF' : Colors.textMuted, fontWeight: '900', fontSize: 10, letterSpacing: 1 }}>LIGHT BAR</Text>
                    <Text style={{ color: musicMatrixStyle === 0x26 ? Colors.accent : Colors.textMuted, fontSize: 8, opacity: 0.8 }}>0x26 (BAR)</Text>
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
                        }} style={{ paddingHorizontal: Spacing.md }}>
                          <Text style={{ color: '#FFF', fontSize: 20, fontWeight: 'bold' }}>{'<'}</Text>
                        </TouchableOpacity>
                        <View style={[styles.musicModeCircle, { width: 32, height: 32, borderRadius: 16 }]}>
                          <Text style={[styles.musicModeNumber, { fontSize: 14 }]}>{musicPatternId}</Text>
                        </View>
                        <TouchableOpacity onPress={() => {
                          const pid = musicPatternId < MUSIC_PATTERNS.length ? musicPatternId + 1 : 1;
                          setMusicPatternId(pid);
                          handleMusicChange(pid);
                        }} style={{ paddingHorizontal: Spacing.md }}>
                          <Text style={{ color: '#FFF', fontSize: 20, fontWeight: 'bold' }}>{'>'}</Text>
                        </TouchableOpacity>
                      </View>
                      <Text style={[Typography.caption, { marginTop: Spacing.xs, color: Colors.primary, fontWeight: 'bold', fontSize: 13 }]}>
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
              <View style={{ flex: 1 }}>
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
                contentContainerStyle={{ flexGrow: 1, paddingHorizontal: Spacing.xs, paddingTop: Spacing.sm, paddingBottom: Spacing.xl }}
                showsVerticalScrollIndicator={false}
                bounces={false}
              >
                {/* ── Street Visualizer: Car-light zone bar ── */}
                <View style={{ marginBottom: Spacing.md }}>
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
                <View style={{ flexDirection: 'row', justifyContent: 'center', alignItems: 'center', backgroundColor: 'transparent', paddingVertical: Spacing.xxs, marginBottom: Spacing.md }}>
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
                    {motionState === 'CRUISING' && '>> CRUISING <<'}
                  </Text>
                </View>

                <View style={{
                  flexGrow: 1,
                  flexDirection: 'row',
                  backgroundColor: 'transparent',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  paddingVertical: Spacing.md,
                  marginBottom: Spacing.sm,
                }}>
                  {/* LEFT: Stoplight Vertical Graphic */}
                  <View style={{
                    width: 50,
                    justifyContent: 'center',
                    alignItems: 'center',
                  }}>
                    {/* Red Light */}
                    <View style={{
                      width: 22, height: 22, borderRadius: 11, marginBottom: Spacing.sm,
                      backgroundColor: (motionState === 'STOPPED' || motionState === 'HARD_BRAKING') ? '#FF0000' : '#330000',
                      shadowColor: '#FF0000', shadowOpacity: (motionState === 'STOPPED' || motionState === 'HARD_BRAKING') ? 1 : 0, shadowRadius: 10, elevation: (motionState === 'STOPPED' || motionState === 'HARD_BRAKING') ? 8 : 0,
                      borderWidth: 1, borderColor: (motionState === 'STOPPED' || motionState === 'HARD_BRAKING') ? '#FFAAAA' : '#000',
                    }} />
                    {/* Yellow Light */}
                    <View style={{
                      width: 22, height: 22, borderRadius: 11, marginBottom: Spacing.sm,
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
                  
                  {/* BOTTOM: Global Telemetry + Session Controls */}
                  <View style={{ flexDirection: 'row', justifyContent: 'space-between', paddingHorizontal: Spacing.lg, marginTop: Spacing.md, marginBottom: Spacing.sm, alignItems: 'center' }}>
                    <View>
                      <Text style={{ color: 'rgba(255,255,255,0.5)', fontSize: 12, fontWeight: '700', letterSpacing: 0.5 }}>
                        TOP SPEED: <Text style={{ color: '#00F0FF', fontWeight: '800' }}>{crewService.sessionTelemetry.topSpeedMph.toFixed(1)} MPH</Text>
                      </Text>
                      <Text style={{ color: 'rgba(255,255,255,0.5)', fontSize: 12, fontWeight: '700', letterSpacing: 0.5, marginTop: Spacing.xxs }}>
                        DISTANCE: <Text style={{ color: '#00F0FF', fontWeight: '800' }}>{crewService.sessionTelemetry.distanceMiles.toFixed(2)} MI</Text>
                      </Text>
                    </View>

                    {/* START / STOP SESSION BUTTON */}
                    <TouchableOpacity
                      onPress={() => {
                        if (!sessionActive) {
                          startSession();
                        } else {
                          stopSessionRecording();
                        }
                      }}
                      activeOpacity={0.85}
                      style={{
                        flexDirection: 'row', alignItems: 'center', gap: Spacing.sm,
                        paddingHorizontal: Spacing.lg, paddingVertical: Spacing.sm,
                        borderRadius: 20,
                        backgroundColor: sessionActive ? '#FF3D00' : '#00C853',
                        shadowColor: sessionActive ? '#FF3D00' : '#00C853',
                        shadowOpacity: 0.5, shadowRadius: 8, elevation: 6,
                      }}
                    >
                      <MaterialCommunityIcons
                        name={sessionActive ? 'stop-circle' : 'play-circle'}
                        size={18}
                        color="#FFF"
                      />
                      <Text style={{ color: '#FFF', fontWeight: '900', fontSize: 13, letterSpacing: 0.5 }}>
                        {sessionActive ? 'SAVE' : 'RECORD'}
                      </Text>
                    </TouchableOpacity>
                  </View>
                </View>
              </ScrollView>
            )}

          </View>

          {/* UNIVERSAL SLIDERS FOOTER - Hidden in FAVORITES only */}
          {activeMode !== 'FAVORITES' && (
            <View style={[styles.sceneSlidersContainer, { marginTop: Spacing.sm, borderTopWidth: 1, borderTopColor: 'rgba(255,255,255,0.05)', paddingTop: Spacing.sm, paddingBottom: 0, flexShrink: 0 }]}>
              {/* Color Grid wrappers */}
              {!(activeMode === 'PROGRAMS') && (
                <View style={{ marginBottom: Spacing.xs }}>
                  {/* Dynamic Selected Color Bar */}
                  {!(activeMode === 'MUSIC' || (activeMode === 'MULTIMODE' && fixedSubMode === 'PATTERN')) && (() => {
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
                          marginBottom: Spacing.xs,
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
                        flexDirection: 'row', width: '100%', height: 18, borderRadius: 9, marginBottom: Spacing.xs,
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
                        flexDirection: 'row', width: '100%', height: 18, borderRadius: 9, marginBottom: Spacing.xs,
                        borderWidth: 1, borderColor: 'rgba(255,255,255,0.4)', backgroundColor: 'transparent'
                      }}>
                        {showFg && (
                          <TouchableOpacity
                            activeOpacity={0.9}
                            onPress={() => {
                              setFixedColorMode('FOREGROUND');
                              setFixedHue(hexToHue(fixedFgColor));
                            }}
                            style={{ flex: 1, backgroundColor: fixedFgColor, justifyContent: 'center', alignItems: 'center', opacity: fixedColorMode === 'FOREGROUND' ? 1.0 : 0.4, borderTopLeftRadius: 8, borderBottomLeftRadius: 8, shadowColor: fixedFgColor, shadowOpacity: 1, shadowRadius: 16, shadowOffset: { width: 0, height: 0 }, elevation: 12 }}
                          >
                            <Text style={{ color: '#FFFFFF', fontSize: 9, fontWeight: '900', letterSpacing: 1, textShadowColor: 'rgba(0,0,0,0.8)', textShadowRadius: 6 }}>FOREGROUND</Text>
                          </TouchableOpacity>
                        )}
                        {showBg && (
                          <TouchableOpacity
                            activeOpacity={0.9}
                            onPress={() => {
                              setFixedColorMode('BACKGROUND');
                              setFixedHue(hexToHue(fixedBgColor));
                            }}
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
                                margin: Spacing.xxs
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
                <View style={[styles.controlRow, { marginTop: Spacing.xs, marginBottom: Spacing.xs, flexShrink: 0, minHeight: 40 }]}>
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
              <View style={{ flexDirection: 'row', gap: Spacing.sm, marginTop: Spacing.sm, marginBottom: Spacing.xs, minHeight: 44 }}>

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
                      AppLogger.log('BRIGHTNESS_CHANGED', { value: val, mode: activeMode });
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
                    onSlidingComplete={(val: number) => {
                      AppLogger.log('BRIGHTNESS_CHANGED', { value: val, mode: activeMode });
                      handleMusicChange(musicPatternId, micSensitivity, val, micSource, musicPrimaryColor, musicSecondaryColor, musicMatrixStyle);
                    }}
                  />
                )}
              </View>
            </View>
          )}
        </View>

        {/* THE FLOATING DOCK */}
        <View style={{ marginBottom: Spacing.xs }}>
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
        <Modal visible={promptState === 'NAMING_PRESET'} transparent animationType="fade">
          <View style={{ flex: 1, backgroundColor: 'rgba(0,0,0,0.85)', justifyContent: 'center', alignItems: 'center', padding: Spacing.xl }}>
            <View style={{ backgroundColor: Colors.surface, padding: Spacing.xl, borderRadius: 20, width: '100%', maxWidth: 340, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}>
              <Text style={{ color: '#FFF', fontSize: 18, fontWeight: 'bold', marginBottom: Spacing.md, textAlign: 'center' }}>
                {quickPromptTargetIndex === -1 ? 'Save Quick Preset' : 'Edit Quick Preset'}
              </Text>
              <Text style={{ color: Colors.textMuted, fontSize: 14, marginBottom: Spacing.xl, textAlign: 'center' }}>
                {quickPromptTargetIndex === -1 ? 'Name your new preset to store it in the Quick bar.' : 'Rename your preset or delete it from the bar.'}
              </Text>
              <TextInput
                style={{ backgroundColor: 'rgba(255,255,255,0.05)', color: '#FFF', padding: Spacing.md, borderRadius: 8, fontSize: 16, marginBottom: Spacing.lg, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}
                placeholder="Preset Name..."
                placeholderTextColor="rgba(255,255,255,0.3)"
                value={promptName}
                onChangeText={setPromptName}
                autoFocus
              />
              {/* Cloud visibility toggle */}
              {quickPromptTargetIndex === -1 && (
                <TouchableOpacity
                  onPress={() => setCloudPublicToggle(p => !p)}
                  style={{ flexDirection: 'row', alignItems: 'center', backgroundColor: 'rgba(255,255,255,0.05)', borderRadius: 10, padding: Spacing.md, marginBottom: Spacing.xl, borderWidth: 1, borderColor: cloudPublicToggle ? '#00C853' : 'rgba(255,255,255,0.1)' }}
                >
                  <MaterialCommunityIcons
                    name={cloudPublicToggle ? 'earth' : 'lock-outline'}
                    size={18}
                    color={cloudPublicToggle ? '#00C853' : Colors.textMuted}
                    style={{ marginRight: Spacing.md }}
                  />
                  <View style={{ flex: 1 }}>
                    <Text style={{ color: '#FFF', fontWeight: 'bold', fontSize: 13 }}>{cloudPublicToggle ? 'Public — visible to community' : 'Private — only you can see it'}</Text>
                    <Text style={{ color: Colors.textMuted, fontSize: 11, marginTop: Spacing.xxs }}>Tap to toggle visibility</Text>
                  </View>
                </TouchableOpacity>
              )}
              <View style={{ flexDirection: 'row', gap: Spacing.md }}>
                {quickPromptTargetIndex !== -1 && (
                  <TouchableOpacity style={{ flex: 1, padding: Spacing.lg, borderRadius: 10, backgroundColor: 'rgba(255,0,0,0.3)' }} onPress={() => {
                    const newArr = [...quickPresets];
                    newArr.splice(quickPromptTargetIndex, 1);
                    setQuickPresets(newArr);
                    AsyncStorage.setItem(`${STORAGE_PREFIX}QuickPresets`, JSON.stringify(newArr));
                    AppLogger.log('BUILDER_PRESET_DELETED', { index: quickPromptTargetIndex });
                    closePrompt();
                  }}>
                    <Text style={{ color: '#FFF', textAlign: 'center', fontWeight: 'bold' }}>Delete</Text>
                  </TouchableOpacity>
                )}
                {quickPromptTargetIndex === -1 && (
                  <TouchableOpacity style={{ flex: 1, padding: Spacing.lg, borderRadius: 10, backgroundColor: 'rgba(255,255,255,0.05)' }} onPress={() => closePrompt()}>
                    <Text style={{ color: '#FFF', textAlign: 'center', fontWeight: 'bold' }}>Cancel</Text>
                  </TouchableOpacity>
                )}
                <TouchableOpacity style={{ flex: 1, padding: Spacing.lg, borderRadius: 10, backgroundColor: '#00C853' }} disabled={isPublishingCloud} onPress={async () => {
                  setIsPublishingCloud(true);
                  const safeName = promptName.trim() || 'Cloud Scene';
                  if (containsProfanity(safeName)) {
                    Alert.alert('Invalid Name', 'Scene names cannot contain inappropriate language. Please choose a different name.');
                    setIsPublishingCloud(false);
                    return;
                  }
                  const success = await ScenesService.publishScene(safeName, captureEntireState(), cloudPublicToggle);
                  if (success) {
                    Alert.alert(cloudPublicToggle ? 'Published!' : 'Saved!', cloudPublicToggle ? 'Your scene is now available to the community.' : 'Scene saved privately to your cloud.');
                    closePrompt();
                  } else {
                    Alert.alert('Error', 'Could not save scene. Are you logged in?');
                  }
                  setIsPublishingCloud(false);
                }}>
                  <Text style={{ color: '#000', textAlign: 'center', fontWeight: 'bold' }}>{isPublishingCloud ? 'Saving...' : (cloudPublicToggle ? '🌍 Publish' : '🔒 Save Private')}</Text>
                </TouchableOpacity>
                <TouchableOpacity style={{ flex: 1, padding: Spacing.lg, borderRadius: 10, backgroundColor: Colors.primary }} onPress={() => {
                  const newArr = [...quickPresets];
                  const safeName = promptName.trim() || 'Preset';
                  if (quickPromptTargetIndex === -1) {
                    newArr.push({ name: safeName, colors: multiColors, type: multiTransition });
                  } else {
                    newArr[quickPromptTargetIndex].name = safeName;
                  }
                  setQuickPresets(newArr);
                  AsyncStorage.setItem(`${STORAGE_PREFIX}QuickPresets`, JSON.stringify(newArr));
                  AppLogger.log('BUILDER_PRESET_SAVED', { name: safeName, isOverwrite: quickPromptTargetIndex !== -1 });
                  closePrompt();
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
        <Modal visible={promptState === 'NAMING_FAVORITE'} transparent animationType="fade">
          <View style={{ flex: 1, backgroundColor: 'rgba(0,0,0,0.85)', justifyContent: 'center', alignItems: 'center', padding: Spacing.xl }}>
            <View style={{ backgroundColor: Colors.surface, padding: Spacing.xl, borderRadius: 20, width: '100%', maxWidth: 340, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}>
              <Text style={{ color: '#FFF', fontSize: 18, fontWeight: 'bold', marginBottom: Spacing.md, textAlign: 'center' }}>
                {favPromptTargetId ? 'Edit Favorite' : 'Save Favorite'}
              </Text>
              <Text style={{ color: Colors.textMuted, fontSize: 14, marginBottom: Spacing.xl, textAlign: 'center' }}>
                {favPromptTargetId ? 'Rename your preset or delete it.' : 'Name your preset. Leave blank to use the default name.'}
              </Text>
              <TextInput
                style={{ backgroundColor: 'rgba(255,255,255,0.05)', color: '#FFF', padding: Spacing.md, borderRadius: 8, fontSize: 16, marginBottom: Spacing.xl, borderWidth: 1, borderColor: 'rgba(255,255,255,0.1)' }}
                placeholder="Custom Preset Name..."
                placeholderTextColor="rgba(255,255,255,0.3)"
                value={promptName}
                onChangeText={setPromptName}
                autoFocus
              />
              <View style={{ flexDirection: 'row', gap: Spacing.md }}>
                {favPromptTargetId && (
                  <TouchableOpacity style={{ flex: 1, padding: Spacing.lg, borderRadius: 10, backgroundColor: 'rgba(255,0,0,0.3)' }} onPress={() => { deleteFavorite(favPromptTargetId); closePrompt(); }}>
                    <Text style={{ color: '#FFF', textAlign: 'center', fontWeight: 'bold' }}>Delete</Text>
                  </TouchableOpacity>
                )}
                {(!favPromptTargetId) && (
                  <TouchableOpacity style={{ flex: 1, padding: Spacing.lg, borderRadius: 10, backgroundColor: 'rgba(255,255,255,0.05)' }} onPress={() => closePrompt()}>
                    <Text style={{ color: '#FFF', textAlign: 'center', fontWeight: 'bold' }}>Cancel</Text>
                  </TouchableOpacity>
                )}
                <TouchableOpacity style={{ flex: 1, padding: Spacing.lg, borderRadius: 10, backgroundColor: Colors.primary }} onPress={handleConfirmSaveFavorite}>
                  <Text style={{ color: '#000', textAlign: 'center', fontWeight: 'bold' }}>Save</Text>
                </TouchableOpacity>
              </View>
            </View>
          </View>
        </Modal>
        {/* Session Summary Modal */}
        <SessionSummaryModal
          visible={showSessionModal}
          snapshot={sessionSummary}
          onSave={async () => {
            await saveSession();
            dismissSessionModal();
          }}
          onDiscard={() => {
            AppLogger.log('SESSION_SAVED', { action: 'DISCARDED' });
            dismissSessionModal();
          }}
        />

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
    marginVertical: Spacing.xxs,
  },
  tabContainer: {
    flexDirection: 'row',
    backgroundColor: Colors.surfaceHighlight,
    borderRadius: Layout.borderRadius,
    padding: Spacing.sm,
    marginBottom: Spacing.sm,
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.08)',
  },
  tab: {
    flex: 1,
    paddingVertical: Spacing.sm,
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
    padding: Spacing.md,
    backgroundColor: Colors.isDark ? 'rgba(21, 25, 40, 0.7)' : Colors.surface,
    borderRadius: Layout.borderRadius + 4,
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255, 255, 255, 0.1)' : 'rgba(0,0,0,0.08)',
  },
  modesContainer: {
    flexDirection: 'row',
    justifyContent: 'center',
    gap: Spacing.sm,
    marginBottom: Spacing.md,
  },
  modePill: {
    paddingHorizontal: Spacing.lg,
    paddingVertical: Spacing.sm,
    borderRadius: Layout.borderRadius,
    backgroundColor: Colors.background,
    marginRight: Spacing.md,
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
    marginTop: Spacing.sm,
  },
  placeholderSlider: {
    height: 8,
    backgroundColor: Colors.surfaceHighlight,
    borderRadius: 4,
    marginTop: Spacing.sm,
    overflow: 'hidden',
  },
  sliderFill: {
    height: '100%',
    backgroundColor: Colors.secondary,
    borderRadius: 4,
  },
  colorGrid: {
    flexDirection: 'row',
    flexWrap: 'nowrap',
    marginTop: Spacing.lg,
    justifyContent: 'space-between',
    alignItems: 'center',
    width: '100%',
  },
  colorButton: {
    flex: 1,
    aspectRatio: 1,
    maxWidth: 36,
    maxHeight: 36,
    borderRadius: 999,
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255,255,255,0.2)' : 'rgba(0,0,0,0.15)',
    marginHorizontal: Spacing.xs,
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
    gap: Spacing.sm
  },
  presetCard: {
    width: '48%',
    minHeight: 80,
    padding: Spacing.sm,
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
    marginTop: Spacing.xs,
    color: Colors.textMuted,
  },
  sceneContainer: {
    backgroundColor: Colors.isDark ? '#050505' : Colors.surfaceHighlight,
    borderRadius: 24,
    padding: Spacing.xxs,
    marginTop: Spacing.sm,
    overflow: 'hidden',
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.08)',
  },
  sceneHeader: {
    padding: Spacing.lg,
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
    padding: Spacing.lg,
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
    padding: Spacing.xs,
    alignItems: 'center',
    marginVertical: Spacing.xs,
    marginHorizontal: Spacing.xs,
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(255,255,255,0.1)' : 'rgba(0,0,0,0.08)',
  },
  musicToggleOption: {
    flex: 1,
    paddingVertical: Spacing.md,
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
    paddingHorizontal: Spacing.md,
    marginBottom: Spacing.xs,
  },
  micIconBtn: {
    flex: 1,
    alignItems: 'center',
    padding: Spacing.sm,
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
    marginBottom: Spacing.sm,
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
    marginHorizontal: Spacing.md,
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
    paddingHorizontal: Spacing.md,
    marginBottom: Spacing.xs,
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
    marginRight: Spacing.sm,
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
    gap: Spacing.xxl,
    marginBottom: Spacing.xs,
  },
  floatingDock: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: Colors.isDark ? 'rgba(8, 10, 16, 0.98)' : 'rgba(255, 255, 255, 0.98)',
    borderWidth: 1,
    borderColor: Colors.isDark ? 'rgba(0, 240, 255, 0.35)' : 'rgba(0, 200, 255, 0.4)',
    borderRadius: 30,
    paddingVertical: Spacing.md,
    paddingHorizontal: Spacing.xl,
    marginHorizontal: Spacing.lg,
    marginBottom: Spacing.sm,
    marginTop: Spacing.sm,
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
    marginTop: Spacing.xs,
  }
});
