/**
 * DockedController.tsx — SK8Lytz Primary LED Control Interface
 *
 * The main user-facing control panel for LED mode management.
 * Renders as a bottom sheet overlay on DashboardScreen.
 *
 * Responsibilities:
 *  - Mode switching: MultiMode, Music, Camera, Street (Accelerometer), DIY Array
 *  - Color picker, RGB sliders, brightness & speed knobs
 *  - Pattern wheel (ArcPatternWheel / VerticalPatternDrum)
 *  - Music mic source controls (APP MIC / DEVICE MIC)
 *  - Favorites system and Quick Presets
 *  - Per-device and group analytics telemetry (MODE_CHANGED, PATTERN_CHANGED, COLOR_CHANGED)
 *
 * Depends on: ZenggeProtocol, AppLogger, useBLE (via prop injection), ThemeContext
 * Platform: React Native (Android + Web)
 */
import React, { useEffect, useRef, useState } from 'react';
import { Alert, Dimensions, FlatList, Modal, Platform, ScrollView, StyleSheet, Text, TextInput, TouchableOpacity, View, useWindowDimensions } from 'react-native';
import { useAppMicrophone } from '../hooks/useAppMicrophone';
import { useControllerAnalytics } from '../hooks/useControllerAnalytics';
import { useCuratedPicks } from '../hooks/useCuratedPicks';
import { useDockedControllerState } from '../hooks/useDockedControllerState';
import { useControllerPersistence } from '../hooks/useControllerPersistence';
import { useControllerDispatch } from '../hooks/useControllerDispatch';
import { useFavorites } from '../hooks/useFavorites';
import { MUSIC_PATTERNS } from '../hooks/useMusicMode';
import { useOptimisticBLE } from '../hooks/useOptimisticBLE';
import { useSessionTracking } from '../hooks/useSessionTracking';
import { useStreetMode } from '../hooks/useStreetMode';
import type { BleConnectionState, IDeviceState, IFavoriteState, ModeType } from '../types/dashboard.types';
import { getColorName, hexToHue, hueToHex, hexToRgb } from '../utils/ColorUtils';
import AnalogGauge from './docked/AnalogGauge';
import FavoritesPanel from './docked/FavoritesPanel';
import MusicPanel from './docked/MusicPanel';
import FixedPanel from './docked/FixedPanel';
import CameraPanel from './docked/CameraPanel';
import StreetPanel from './docked/StreetPanel';
import FavoritePromptModal from './docked/FavoritePromptModal';
import UniversalSlidersFooter from './docked/UniversalSlidersFooter';

import { LinearGradient } from 'expo-linear-gradient';
import { Layout, Spacing, Typography } from '../theme/theme';

import { SK8LYTZ_TEMPLATES } from '../protocols/PatternEngine';
import { useTheme } from '../context/ThemeContext';
import CameraTracker from './CameraTracker';
import { UnifiedPatternPicker } from './patterns/UnifiedPatternPicker';
import CustomEffectVisualizer from './CustomEffectVisualizer';
import NeonHueStrip from './NeonHueStrip';
import ProductVisualizer from './ProductVisualizer';
import TacticalSlider from './TacticalSlider';
import VerticalPatternDrum from './VerticalPatternDrum';

import { MaterialCommunityIcons } from '@expo/vector-icons';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { STORAGE_PREFIX } from '../constants/AppConstants';
import { LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { AppLogger } from '../services/AppLogger';
import { containsProfanity } from '../services/AuthUtils';
import { crewService } from '../services/CrewService';
import { ScenesService } from '../services/ScenesService';
import CommunityModal from './CommunityModal';
import MarqueeText from './MarqueeText';
import PositionalGradientBuilder from './PositionalGradientBuilder';
import SessionSummaryModal from './SessionSummaryModal';




// AnalogGauge — now imported from './docked/AnalogGauge'
// FixedPatternPreviewRow — kept inline (tightly coupled to parent animation state)

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
  writeToDevice?: (payload: number[]) => Promise<boolean | 'partial'>;
  isPoweredOn?: boolean;
  onDisconnect?: () => void;
  /** 'leader' = broadcast changes, 'member' = receive changes, null = solo */
  crewRole?: 'leader' | 'member' | null;
  /** Called with full scene snapshot whenever any mode/color changes (leader only) */
  onCrewSceneChange?: (scene: Record<string, any>) => void;
  /** Triggered to persist the active pattern name to dashboard group persistent storage */
  onPatternChanged?: (patternName: string) => void;
  appSettings?: Record<string, string | boolean>;
}

export type DockedControllerHandle = {
  applyCloudScene: (scenePayload: any) => void;
  loadFavorite: (fav: IFavoriteState) => void;
  setActiveMode: (mode: ModeType) => void;
  setBrightness: (val: number) => void;
  setSpeed: (val: number) => void;
  applySpatialSegments: (segments: any[]) => void;
  replayStateToDevice: (deviceId: string) => void;
};

// CURATED_PRESETS logic moved to internal component state for Supabase updating

// MarqueeText moved to standalone component MarqueeText.tsx

const DockedController = React.forwardRef<DockedControllerHandle, Sk8lytzControllerProps>(
  function DockedController({ hwSettings, lockedProduct, isPaired, bleState, points, devices, onLongPressDevice, writeToDevice: parentWriteToDevice, isPoweredOn = true, onDisconnect, crewRole, onCrewSceneChange, onPatternChanged, appSettings = {} }: Sk8lytzControllerProps, ref) {
    const { Colors, isDark } = useTheme();
    const { height: windowHeight } = useWindowDimensions();
    const isShort = windowHeight < 720;
    const gaugeSize = isShort ? 100 : 120;
    const styles = createStyles(Colors);

    /**
     * Perceptual brightness factor — lifts the floor so LEDs stay visible at low %.
     * At 0% → 0 (truly off). At 5% → ~14% (dim outline visible). At 100% → 1.0.
     * Formula: brt > 0 ? 0.10 + 0.90 * (brt/100) : 0
     */
    const brtFactor = (brt: number): number =>
      brt > 0 ? 0.10 + 0.90 * (brt / 100) : 0;

    const [lastSentPayload, setLastSentPayload] = useState<number[]>([]);

    /**
     * Snapshot of the full UI state captured immediately before each BLE write.
     * If the write is rejected (RECONCILED phase), applyCloudScene restores this snapshot.
     */
    const lastConfirmedStateRef = useRef<any>(null);

    /**
     * Stable ref wrapper for the onReconcile callback.
     * Updated every render so it always closes over the latest applyCloudScene.
     * This prevents stale closures inside useOptimisticBLE's debounced async path.
     */
    const onReconcileRef = useRef<() => void>(() => {
      AppLogger.warn('[DockedController] BLE write reconciled — no snapshot to restore yet');
    });

    // ── Optimistic BLE Bridge (Ghost Standard) ─────────────────────────────
    const { optimisticWrite, writeStatus } = useOptimisticBLE({
      writeToDevice: parentWriteToDevice as ((payload: number[], targetDeviceId?: string) => Promise<boolean | 'partial'>) | undefined,
      // Indirection via ref: always calls the latest closure without recreating the hook callback
      onReconcile: () => onReconcileRef.current(),
      debounceMs: 40,
      disableOptimisticUI: appSettings['global_optimistic_ui_enabled'] === false,
      disableHaptics: appSettings['global_haptics_enabled'] === false,
    });

    const writeToDevice = async (payload: number[]) => {
      // Gate: only write if the parent BLE write function exists (same pattern as Pro Effects).
      // Previously used `bleState !== 'READY'` which was too aggressive — the derived bleState
      // transiently leaves 'READY' during background rescans/probing even while devices are
      // fully connected and writable, silently dropping fixed mode, solid color, and camera writes.
      // The BLE stack in useBLE.writeToDevice already handles connection-level safety internally.
      if (!parentWriteToDevice) return;
      lastConfirmedStateRef.current = captureEntireState();
      setLastSentPayload([...payload]);
      await optimisticWrite(payload);
    };



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

    // ── Ghost Reconcile Wiring ──────────────────────────────────────────────
    // Updated every render so the ref always holds the latest applyCloudScene closure.
    // When a BLE write fails, optimisticWrite calls onReconcileRef.current() which
    // restores the UI to the last confirmed hardware state via applyCloudScene.
    onReconcileRef.current = () => {
      if (lastConfirmedStateRef.current) {
        applyCloudScene(lastConfirmedStateRef.current);
        AppLogger.warn('[DockedController] BLE write reconciled — UI snapped back to last confirmed state');
      }
    };

    // Favorites Array — now managed by useFavorites hook above

    // Expose control methods to parent via ref for Voice and Crew coordination
    React.useImperativeHandle(ref, () => ({
      applyCloudScene,
      loadFavorite,
      setActiveMode,
      setBrightness,
      setSpeed,
      applySpatialSegments,
      replayStateToDevice: (deviceId: string) => {
        if (!lastSentPayload || lastSentPayload.length === 0) return;
        AppLogger.log('BLE_QUEUE_REPLAY', { deviceId, payloadLen: lastSentPayload.length });
        // Use direct write to skip optimistic UI/haptics when ghosting a resurrected device
        optimisticWrite(lastSentPayload, undefined, deviceId).catch(() => {});
      }
    }), [speed, brightness, writeToDevice, lastSentPayload, optimisticWrite]);


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
        musicPrimaryColor, musicSecondaryColor, micSensitivity, micSource, musicMatrixStyle,
        // Builder persistence (feat/pattern-favorites-v2)
        builderNodes, builderFillMode, builderTransitionType, builderDirection,
      };

      saveFavorite(capturedState, name);
    };


    // ── BLE Dispatch: hardware command translation via extracted hook ──────
    const {
      sendColor,
      applyFixedPattern,
      applyStaticModePattern: _applyStaticModePattern,
      applyEmergencyPattern,
      handleMusicChange,
      clampSpeed,
    } = useControllerDispatch({ writeToDevice, hwSettings, points });

    /** Convenience wrapper — pre-binds selectedColor and speed for callers */
    const applyStaticModePattern = (pat: typeof fixedModePattern, r?: number, g?: number, b?: number, spd?: number) =>
      _applyStaticModePattern(pat, selectedColor, speed, r, g, b, spd);

    /** Restore a saved favorite — dispatches mode switch + BLE commands */
    const loadFavorite = React.useCallback((favRaw: IFavoriteState, context: 'FAVORITE' | 'PICK' | 'COMMUNITY' = 'FAVORITE') => {
      AppLogger.log(context === 'PICK' ? 'PICK_LOADED' : 'FAVORITE_LOADED', { name: favRaw.name, mode: favRaw.mode });
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
        setFixedSubMode('PATTERN');
        setFixedPatternId(favRaw.patternId ?? 0);
        setFixedColorMode(favRaw.fixedColorMode ?? 'FOREGROUND');
        setFixedFgColor(favRaw.fixedFgColor ?? '#FF6600');
        setFixedBgColor(favRaw.fixedBgColor ?? '#000000');
        applyFixedPattern(favRaw.patternId ?? 0, favRaw.fixedFgColor ?? '#FF6600', favRaw.fixedBgColor ?? '#000000', favRaw.speed, favRaw.brightness);
      } else if (legacyMode === 'MUSIC') {
        setActiveMode('MUSIC');
        setMusicPatternId(favRaw.patternId ?? 0);
        handleMusicChange(favRaw.patternId ?? 0, micSensitivity, favRaw.brightness, micSource, musicPrimaryColor, musicSecondaryColor, musicMatrixStyle);
      } else if (legacyMode === 'CAMERA') {
        setActiveMode('CAMERA');
      } else if (legacyMode === 'FAVORITES') {
        setActiveMode('FAVORITES');
      } else if (legacyMode === 'BUILDER') {

        setActiveMode('MULTIMODE');
        setFixedSubMode('BUILDER');
        if (favRaw.builderNodes && favRaw.builderNodes.length > 0) {
          setBuilderNodes(favRaw.builderNodes);
        }
        if (favRaw.builderFillMode) setBuilderFillMode(favRaw.builderFillMode);
        if (favRaw.builderTransitionType !== undefined) setBuilderTransitionType(favRaw.builderTransitionType);
        if (favRaw.builderDirection !== undefined) setBuilderDirection(favRaw.builderDirection);
        // Note: hardware dispatch is not auto-triggered for BUILDER restores.
        // The gradient is re-sent when the user opens Builder tab and taps Send.
        // Auto-dispatch tracked in: refactor/docked-controller-decomposition
      } else if (legacyMode === 'MULTI' || legacyMode === 'DIY' || legacyMode === 'MULTICOLOR') {

        setActiveMode('MULTIMODE');
        setFixedSubMode('BUILDER');
        setMultiColors(favRaw.multiColors || []);
        setMultiTransition(favRaw.multiTransition || 3);
        setMultiLength(favRaw.multiLength || 16);
        if (writeToDevice && favRaw.multiColors) {
          const rgbColors = favRaw.multiColors.map((h: string) => ({
            r: parseInt(h.slice(1, 3), 16) || 0,
            g: parseInt(h.slice(3, 5), 16) || 0,
            b: parseInt(h.slice(5, 7), 16) || 0,
          }));
          writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, clampSpeed(favRaw.speed), 1, favRaw.multiTransition));
        }
      } else {
        // Unknown/legacy mode — best-effort color dispatch
        if (favRaw.color) {
          const fallbackColor = favRaw.color;
          setTimeout(() => {
            sendColor(parseInt(fallbackColor.slice(1, 3), 16) || 0, parseInt(fallbackColor.slice(3, 5), 16) || 0, parseInt(fallbackColor.slice(5, 7), 16) || 0);
          }, 100);
        }
      }
      
      if (context === 'PICK') {
        AppLogger.log('PICK_SELECTED', { id: favRaw.id, name: favRaw.name || favRaw.customName, mode: legacyMode });
      } else {
        AppLogger.log('FAVORITE_RENDERED', { id: favRaw.id, name: favRaw.name || favRaw.customName, mode: legacyMode, patternId: favRaw.patternId });
      }
    }, [writeToDevice, handleMusicChange, applyFixedPattern, sendColor, clampSpeed, micSensitivity, micSource, musicPrimaryColor, musicSecondaryColor, musicMatrixStyle]);




    // -- Curated Presets (SK8Lytz Picks) — now owned by useCuratedPicks hook --
    const { curatedPresets, picksLoading } = useCuratedPicks();

    // -- App Microphone — now owned by useAppMicrophone hook --
    const { audioMagnitude } = useAppMicrophone({
      writeToDevice,
      activeMode,
      micSource,
      isPoweredOn,
    });

    // -- Analytics Logging — now owned by useControllerAnalytics hook --
    useControllerAnalytics({
      activeMode,
      selectedPatternId,
      selectedColor,
      brightness,
      speed,
      streetSensitivity,
      deviceContext,
    });


    React.useEffect(() => {
      if (lockedProduct) {
        setActiveProduct(lockedProduct);
      }
    }, [lockedProduct]);

    // ── Persistence: load/save controller state via extracted hook ──────────
    useControllerPersistence(
      { activeMode, selectedColor, selectedPatternId, brightness, speed,
        micSensitivity, musicHue, musicSecondaryHue, musicPrimaryColor, musicSecondaryColor,
        musicMatrixStyle, musicPatternId, micSource, musicSetting,
        fixedPatternId, fixedColorMode, fixedFgColor, fixedBgColor, fixedHue },
      { setSelectedColor, setSelectedPatternId, setBrightness, setSpeed,
        setMicSensitivity, setMusicHue, setMusicSecondaryHue, setMusicPrimaryColor,
        setMusicSecondaryColor, setMusicMatrixStyle, setMusicPatternId, setMicSource,
        setMusicSetting, setFixedPatternId, setFixedColorMode, setFixedFgColor,
        setFixedBgColor, setFixedHue }
    );


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

    // getColorName — now imported from '../utils/ColorUtils'

    const currentStatusText = React.useMemo(() => {
      switch (activeMode) {
        case 'MULTIMODE':
          const fixedClr = fixedColorMode === 'FOREGROUND' ? fixedFgColor : fixedBgColor;
          return `MultiMode - ${getColorName(fixedClr)}`;
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

        {/* ── POWER OVERRIDE OVERLAY ────────────────────────────────────────── */}
        {isPoweredOn === false && (
          <View style={[StyleSheet.absoluteFill, { zIndex: 9999, backgroundColor: 'rgba(0,0,0,0.85)', justifyContent: 'center', alignItems: 'center', borderRadius: Layout.borderRadius, pointerEvents: 'box-only' as any }]}>
            <TouchableOpacity
              onPress={() => writeToDevice && writeToDevice(ZenggeProtocol.turnOn())}
              style={[{ width: 120, height: 120, borderRadius: 60, backgroundColor: 'rgba(0, 240, 255, 0.1)', borderWidth: 3, borderColor: '#00f0ff', elevation: 15, justifyContent: 'center', alignItems: 'center' }, Platform.select({
                ios: { shadowColor: '#00f0ff', shadowOffset: { width: 0, height: 0 }, shadowOpacity: 1, shadowRadius: 30 },
                web: { boxShadow: '0px 0px 30px rgba(0, 240, 255, 1)' } as any
              })]}
            >
              <MaterialCommunityIcons name="power" size={64} color="#00f0ff" />
            </TouchableOpacity>
            <Text style={{ color: '#00f0ff', fontSize: 28, fontFamily: 'Righteous', marginTop: Spacing.xl, letterSpacing: 6 }}>STANDBY</Text>
            <Text style={{ color: Colors.textMuted, fontSize: 13, marginTop: Spacing.sm, letterSpacing: 1 }}>Hardware is in low-power idle mode.</Text>
            <Text style={{ color: Colors.textMuted, fontSize: 12, position: 'absolute', bottom: 40, opacity: 0.5 }}>Tap to Wake</Text>
          </View>
        )}

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
            {/* Power Toggle Button (Left) */}
            <TouchableOpacity
              style={{ position: 'absolute', top: 12, left: 16, zIndex: 100, backgroundColor: 'rgba(255,255,255,0.06)', padding: Spacing.sm, borderRadius: 20 }}
              onPress={() => writeToDevice && writeToDevice(ZenggeProtocol.turnOff())}
            >
              <MaterialCommunityIcons name="power" size={22} color="#00f0ff" />
            </TouchableOpacity>

            {/* Favorite Button (Right) */}
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
              audioMagnitude={audioMagnitude}
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
              <FavoritesPanel
                favorites={favorites}
                curatedPresets={curatedPresets}
                picksLoading={picksLoading}
                onLoadFavorite={loadFavorite}
                onEditFavorite={(id, name) => openFavoritePrompt(id, name)}
                isDark={isDark}
                Colors={Colors}
                styles={styles}
              />
            )}

            {activeMode === 'FIXED' && (
              <FixedPanel
                fixedModePattern={fixedModePattern}
                setFixedModePattern={setFixedModePattern}
                speed={speed}
                setSpeed={setSpeed}
                applyPattern={(pat, spd) => applyStaticModePattern(pat, undefined, undefined, undefined, spd)}
                Colors={Colors}
              />
            )}

            {activeMode === 'MULTIMODE' && (
              <UnifiedPatternPicker
                speed={speed}
                hwSettings={hwSettings}
                points={points}
                fgColor={fixedFgColor}
                bgColor={fixedBgColor}
                writeToDevice={writeToDevice}
                onStateChange={(id) => {
                  setFixedPatternId(id);
                }}
              />
            )}

            {/* ── MUSIC MODE UI ────────────────────────────────────────────────── */}
            {activeMode === 'MUSIC' && (
              <MusicPanel
                musicPatternId={musicPatternId}
                setMusicPatternId={setMusicPatternId}
                micSource={micSource}
                setMicSource={setMicSource}
                musicMatrixStyle={musicMatrixStyle}
                setMusicMatrixStyle={setMusicMatrixStyle}
                micSensitivity={micSensitivity}
                brightness={brightness}
                musicPrimaryColor={musicPrimaryColor}
                musicSecondaryColor={musicSecondaryColor}
                handleMusicChange={handleMusicChange}
                Colors={Colors}
                styles={styles}
              />
            )}

            {/* ── CAMERA MODE UI ────────────────────────────────────────────────── */}
            {activeMode === 'CAMERA' && (
              <CameraPanel
                onColorDetected={(hex: string) => {
                  // Boost camera's dark/washed-out pixels to 100% pure neon saturation
                  const hue = hexToHue(hex);
                  const neonHex = hueToHex(hue);
                  setSelectedColor(neonHex);
                  const { r, g, b } = hexToRgb(neonHex);
                  sendColor(r, g, b);
                }}
              />
            )}

            {/* ── STREET MODE UI: FAST & FURIOUS DASHBOARD ─────────────────── */}
            {activeMode === 'STREET' && (
              <StreetPanel
                isStreetBraking={isStreetBraking}
                motionState={motionState}
                gpsSpeed={gpsSpeed}
                peakGForce={peakGForce}
                streetCruiseColor={streetCruiseColor}
                sessionActive={sessionActive}
                startSession={startSession}
                stopSessionRecording={stopSessionRecording}
                Colors={Colors}
              />
            )}

          </View>

          {/* UNIVERSAL SLIDERS FOOTER - Hidden in FAVORITES only */}
          {activeMode !== 'FAVORITES' && (
            <UniversalSlidersFooter
              activeMode={activeMode}
              fixedSubMode={fixedSubMode}
              fixedColorMode={fixedColorMode}
              fixedModePattern={fixedModePattern}
              selectedColor={selectedColor}
              fixedFgColor={fixedFgColor}
              fixedBgColor={fixedBgColor}
              fixedHue={fixedHue}
              musicPrimaryColor={musicPrimaryColor}
              musicSecondaryColor={musicSecondaryColor}
              musicHue={musicHue}
              musicSecondaryHue={musicSecondaryHue}
              selectedHue={selectedHue}
              musicColorFocus={musicColorFocus}
              streetCruiseColor={streetCruiseColor}
              brightness={brightness}
              speed={speed}
              micSensitivity={micSensitivity}
              streetSensitivity={streetSensitivity}
              fixedPatternId={fixedPatternId}
              selectedPatternId={selectedPatternId}
              musicPatternId={musicPatternId}
              musicMatrixStyle={musicMatrixStyle}
              micSource={micSource}
              multiColors={multiColors}
              multiTransition={multiTransition}
              setSelectedColor={setSelectedColor}
              setFixedFgColor={setFixedFgColor}
              setFixedBgColor={setFixedBgColor}
              setFixedHue={setFixedHue}
              setMusicPrimaryColor={setMusicPrimaryColor}
              setMusicSecondaryColor={setMusicSecondaryColor}
              setMusicHue={setMusicHue}
              setMusicSecondaryHue={setMusicSecondaryHue}
              setSelectedHue={setSelectedHue}
              setMusicColorFocus={setMusicColorFocus}
              setFixedColorMode={setFixedColorMode}
              setStreetCruiseColor={setStreetCruiseColor}
              setBrightness={setBrightness}
              setSpeed={setSpeed}
              setMicSensitivity={setMicSensitivity}
              setStreetSensitivity={setStreetSensitivity}
              sendColor={sendColor}
              applyFixedPattern={applyFixedPattern}
              applyStaticModePattern={applyStaticModePattern}
              applyEmergencyPattern={applyEmergencyPattern}
              applyStreetPattern={applyStreetPattern}
              handleMusicChange={handleMusicChange}
              clampSpeed={clampSpeed}
              brtFactor={brtFactor}
              writeToDevice={writeToDevice}
              hwSettings={hwSettings}
              motionStateRef={motionStateRef}
              styles={styles}
            />
          )}
        </View>

        {/* THE FLOATING DOCK */}
        <View style={{ marginBottom: Spacing.xs }}>
          <View style={[styles.floatingDock, { marginBottom: 0 }]}>
            {[
              { id: 'HOME',      icon: 'home-outline'         },
              { id: 'FAVORITES', icon: 'cards-heart-outline'  },
              { id: 'MULTI',     icon: 'palette'              },
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
                    } else if (dockItem.id === 'FIXED') {
                      setActiveMode('FIXED');
                      setLastOperatingMode('FIXED');
                    } else if (dockItem.id === 'STREET') {
                      setActiveMode('STREET');
                      setLastOperatingMode('STREET');
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
        <FavoritePromptModal
          visible={promptState === 'NAMING_FAVORITE'}
          Colors={Colors}
          promptName={promptName}
          onChangePromptName={setPromptName}
          favPromptTargetId={favPromptTargetId}
          onDelete={() => { deleteFavorite(favPromptTargetId!); closePrompt(); }}
          onCancel={() => closePrompt()}
          onSave={handleConfirmSaveFavorite}
        />
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
    ...Platform.select({
      web: { boxShadow: `0px 4px 10px ${Colors.primary}40` } as any,
      default: { shadowColor: Colors.primary, shadowOffset: { width: 0, height: 4 }, shadowOpacity: 0.25, shadowRadius: 10 }
    }),
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
    ...Platform.select({
      web: { boxShadow: `0px 8px 20px ${Colors.primary}99` } as any,
      default: { shadowColor: Colors.primary, shadowOffset: { width: 0, height: 8 }, shadowOpacity: 0.6, shadowRadius: 20 }
    }),
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
    ...Platform.select({
      web: { boxShadow: `0px 0px 12px ${Colors.primary}e6` } as any,
      default: { shadowColor: Colors.primary, shadowOffset: { width: 0, height: 0 }, shadowOpacity: 0.9, shadowRadius: 12 }
    }),
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
    ...Platform.select({
      web: { textShadow: `0px 0px 24px ${Colors.primary}` } as any,
      default: { textShadowColor: Colors.primary, textShadowOffset: { width: 0, height: 0 }, textShadowRadius: 24 }
    }),
    opacity: 1.0,
    marginTop: Spacing.xs,
  }
});

