/**
 * DockedController.tsx — SK8Lytz Primary LED Control Interface (Hollow Shell v3)
 *
 * Routing shell: manages shared state, BLE write bus, and mode FSM.
 * Delegates all panel rendering to isolated sub-components via DockedBus.
 *
 * Sub-panels (all React.memo isolated):
 *  - DockedDock        (floating nav bar + swipe gesture)
 *  - ProEffectsPanel   (MULTIMODE / pattern grid)
 *  - BuilderPanel      (BUILDER / gradient editor)
 *  - MusicPanel        (MUSIC)
 *  - CameraPanel       (CAMERA)
 *  - StreetPanel       (STREET)
 *  - FavoritesPanel    (FAVORITES)
 *  - QuickPresetModal  (cloud/preset save modal)
 *
 * Depends on: ZenggeProtocol, AppLogger, useBLE (via prop injection), ThemeContext
 * Platform: React Native (Android + Web)
 */
import React, { useEffect, useRef, useState } from 'react';
import { Platform, StyleSheet, Text, TouchableOpacity, View, useWindowDimensions } from 'react-native';
import { useAppMicrophone } from '../hooks/useAppMicrophone';
import { useControllerAnalytics } from '../hooks/useControllerAnalytics';
import { useCuratedPicks } from '../hooks/useCuratedPicks';
import { useDockedControllerState } from '../hooks/useDockedControllerState';
import { useControllerPersistence } from '../hooks/useControllerPersistence';
import { useControllerDispatch } from '../hooks/useControllerDispatch';
import { useFavorites } from '../hooks/useFavorites';
import { getMusicPatternLabel } from '../hooks/useMusicMode';
import { useOptimisticBLE } from '../hooks/useOptimisticBLE';
import { useSessionTracking } from '../hooks/useSessionTracking';
import { useStreetMode } from '../hooks/useStreetMode';
import { useDeviceStateLedger } from '../hooks/useDeviceStateLedger';
import type { BleConnectionState, DockedBus, IDeviceState, IFavoriteState, ModeType } from '../types/dashboard.types';
import { getColorName, hexToHue, hueToHex, hexToRgb } from '../utils/ColorUtils';


import FavoritesPanel from './docked/FavoritesPanel';
import MusicPanel from './docked/MusicPanel';
import CameraPanel from './docked/CameraPanel';
import StreetPanel from './docked/StreetPanel';
import FavoritePromptModal from './docked/FavoritePromptModal';
import UniversalSlidersFooter from './docked/UniversalSlidersFooter';
import ProEffectsPanel from './docked/ProEffectsPanel';

import { LinearGradient } from 'expo-linear-gradient';
import { Layout, Spacing, Typography } from '../theme/theme';
import { SK8LYTZ_TEMPLATES } from '../protocols/PatternEngine';
import { useTheme } from '../context/ThemeContext';
import { BuilderPanel } from './docked/BuilderPanel';
import ProductVisualizer from './ProductVisualizer';
import SpectrumAnalyzer from './docked/SpectrumAnalyzer';

import { MaterialCommunityIcons } from '@expo/vector-icons';
import { LOCAL_PRODUCT_CATALOG } from '../constants/ProductCatalog';
import { ZenggeProtocol } from '../protocols/ZenggeProtocol';
import { AppLogger } from '../services/AppLogger';
import CommunityModal from './CommunityModal';

import DockedDock from './docked/DockedDock';
import QuickPresetModal from './docked/QuickPresetModal';
import SessionSummaryModal from './SessionSummaryModal';
// NOTE: useGlobalTelemetry intentionally NOT imported here.
// GPS + accelerometer sensors are owned exclusively by DashboardScreen via
// useGlobalTelemetry(isSkateSessionActive). The 4 telemetry values are threaded
// down as props to prevent a duplicate sensor loop. (BUG-01 fix, 2026-05-08)
import { LiveTelemetryHUD } from './dashboard/LiveTelemetryHUD';




// AnalogGauge — now imported from './docked/AnalogGauge'
// FixedPatternPreviewRow — kept inline (tightly coupled to parent animation state)

type ProductType = string;

// Pattern labels resolved via getMusicPatternLabel(modeType, patternId) from '../hooks/useMusicMode'

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
  writeToDevice?: (payload: number[], targetDeviceId?: string, opts?: { lowPriority?: boolean }) => Promise<boolean | 'partial'>;
  isPoweredOn?: boolean;
  onPowerToggle?: () => void;
  onDisconnect?: () => void;
  /** 'leader' = broadcast changes, 'member' = receive changes, null = solo */
  crewRole?: 'leader' | 'member' | null;
  /** Called with full scene snapshot whenever any mode/color changes (leader only) */
  onCrewSceneChange?: (scene: Record<string, any>) => void;
  /** Triggered to persist the active pattern name + color snapshot to dashboard group persistent storage */
  appSettings?: Record<string, string | boolean>;
  // ── Telemetry props (BUG-01 fix) ──────────────────────────────────────────
  // These values are owned by DashboardScreen's single useGlobalTelemetry instance.
  // DockedController is a display consumer — it must NOT create its own sensor subscriptions.
  /** Current GPS speed in MPH */
  gpsSpeed?: number;
  /** Current peak G-force reading (exponential decay smoothed) */
  peakGForce?: number;
  /** Cumulative distance traveled this session in miles */
  sessionDistanceMiles?: number;
  /** Elapsed session duration in seconds */
  sessionDurationSec?: number;
  /** Called when a pattern is applied — lets DashboardScreen persist the group pattern snapshot + ledger entry. */
  onPatternChanged?: (patternName: string, snapshot: import('../types/dashboard.types').GroupPatternSnapshot, lastPayload?: number[]) => void;
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
  function DockedController({ hwSettings, lockedProduct, isPaired, bleState, points, devices, onLongPressDevice, writeToDevice: parentWriteToDevice, isPoweredOn = true, onPowerToggle, onDisconnect, crewRole, onCrewSceneChange, onPatternChanged, appSettings = {}, gpsSpeed = 0, peakGForce = 1.0, sessionDistanceMiles = 0, sessionDurationSec = 0 }: Sk8lytzControllerProps, ref) {
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
    // Ref indirection for captureEntireState — declared here to break the TDZ forward reference.
    // captureEntireState (defined at L413 via useCallback) is assigned to this ref each render.
    const captureEntireStateRef = useRef<(override?: Record<string, any>) => any>(() => null);

    /**
     * Stable ref wrapper for the onReconcile callback.
     * Updated every render so it always closes over the latest applyCloudScene.
     * This prevents stale closures inside useOptimisticBLE's debounced async path.
     */
    const onReconcileRef = useRef<() => void>(() => {
      AppLogger.warn('[DockedController] BLE write reconciled — no snapshot to restore yet');
    });

    const handleReconcile = React.useCallback(() => onReconcileRef.current(), []);

    // ── Optimistic BLE Bridge (Ghost Standard) ─────────────────────────────
    const { optimisticWrite, writeStatus } = useOptimisticBLE({
      writeToDevice: parentWriteToDevice as ((payload: number[], targetDeviceId?: string) => Promise<boolean | 'partial'>) | undefined,
      // Indirection via ref: always calls the latest closure without recreating the hook callback
      onReconcile: handleReconcile,
      debounceMs: 0, // perf(ble): removed redundant pre-debounce — useBLE.ts 50ms is the authoritative guard
      disableOptimisticUI: appSettings['global_optimistic_ui_enabled'] === false,
      disableHaptics: appSettings['global_haptics_enabled'] === false,
    });

    const writeToDevice = React.useCallback(async (payload: number[], override?: Record<string, any>) => {
      // Gate: only write if the parent BLE write function exists (same pattern as Pro Effects).
      // Previously used `bleState !== 'READY'` which was too aggressive — the derived bleState
      // transiently leaves 'READY' during background rescans/probing even while devices are
      // fully connected and writable, silently dropping fixed mode, solid color, and camera writes.
      // The BLE stack in useBLE.writeToDevice already handles connection-level safety internally.
      if (!parentWriteToDevice) return;
      lastConfirmedStateRef.current = captureEntireStateRef.current(override);

      // Lock visualizer to exactly what we are sending.
      // Read volatile mode/pattern/color via refs (updated every render) so this
      // useCallback can remain stable without them as deps.
      const currentResolvedMode = (activeModeRef.current === 'MULTIMODE' && fixedSubModeRef.current === 'BUILDER') ? 'BUILDER' : activeModeRef.current;
      const currentResolvedPattern = activeModeRef.current === 'MUSIC' ? musicPatternIdRef.current : (activeModeRef.current === 'MULTIMODE' ? fixedPatternIdRef.current : selectedPatternIdRef.current);

      setVizLock({
        mode: currentResolvedMode,
        patternId: currentResolvedPattern,
        color: visualizerColorRef.current,
      });

      setLastSentPayload([...payload]);
      await optimisticWrite(payload);
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [parentWriteToDevice, optimisticWrite]);

    // ── Global Telemetry Engine ─────────────────────────────────────────────
    // REMOVED: useGlobalTelemetry(true) — values now received as props from DashboardScreen.
    // The parent holds the single GPS + accelerometer subscription, preventing double-sensor
    // registration that caused 2x battery drain and incorrect session distance accumulation.
    // See: BUG-01 forensic audit 2026-05-08, Bucket List fix/double-global-telemetry.



    // ── Global Device Context for Analytics ────────────────────────────────────
    const deviceContext = React.useMemo(() => {
      if (!devices || devices.length === 0) return { target: 'none' };
      if (devices.length === 1) return { target: 'device', deviceId: devices[0].id };
      return { target: 'group', deviceIds: devices.map(d => d.id), groupSize: devices.length };
    }, [devices]);

    // ── Device State Ledger ─────────────────────────────────────────────────
    // Unified per-device pattern state. Pre-warms controller UI on mount.
    const ledger = useDeviceStateLedger();
    // Resolve primary MAC — device[0] is the group leader or the solo device.
    // warmLedgerCache() fires on app boot (App.tsx) so the in-memory cache is
    // already populated by the time DockedController mounts. No need to scan
    // all devices for a ledger entry — device[0] will have one if any do.
    const primaryMac = devices?.[0]?.id ?? '';

    // Hardware Visualizer Lock — decodes the actual state being sent to the hardware
    // rather than relying on the active UI tab (which bounces around during swiping).
    const initialLedgerRef = useRef(primaryMac ? ledger.loadSync(primaryMac) : null);
    const [vizLock, setVizLock] = useState({
      mode: (initialLedgerRef.current?.mode as string) || 'HOME',
      patternId: initialLedgerRef.current?.patternId || 1,
      color: initialLedgerRef.current?.fgColor || '#00f0ff'
    });


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
      fixedDirection, setFixedDirection,
      isCommunityModalVisible, setIsCommunityModalVisible,
      isPublishingCloud, setIsPublishingCloud,
      cloudPublicToggle, setCloudPublicToggle,
      applyCloudScene: baseApplyCloudScene,
      captureEntireState: baseCaptureEntireState,
      applySpatialSegments
    } = useDockedControllerState(lockedProduct || 'HALOZ', ledger.loadSync, primaryMac);

    // ── Volatile-State Refs (perf: stabilize useCallback deps without stale closures) ─────────────
    // Updated every render (assignment is free — no re-render triggered).
    // useCallback closures read via refs so writeToDevice stays reference-stable
    // without needing frequently-changing state values in its dependency array.
    const activeModeRef = useRef(activeMode);
    activeModeRef.current = activeMode;
    const fixedSubModeRef = useRef(fixedSubMode);
    fixedSubModeRef.current = fixedSubMode;
    const musicPatternIdRef = useRef(musicPatternId);
    musicPatternIdRef.current = musicPatternId;
    const fixedPatternIdRef = useRef(fixedPatternId);
    fixedPatternIdRef.current = fixedPatternId;
    const selectedPatternIdRef = useRef(selectedPatternId);
    selectedPatternIdRef.current = selectedPatternId;
    const visualizerColorRef = useRef<string>('#00f0ff'); // synced below after visualizerColor useMemo
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
      applyStreetPattern,
      streetDistribution,
      setStreetDistribution,
    } = useStreetMode({
      activeMode,
      writeToDevice: parentWriteToDevice,
      hwSettings,
      points,
      activeProduct,
      brightness,
      speed,
      deviceContext,
      gpsSpeed,
      peakGForce,
    });

    const captureEntireState = React.useCallback(
      (override?: Record<string, any>) => baseCaptureEntireState(streetSensitivity, streetCruiseColor, streetBrakeColor, override),
      [baseCaptureEntireState, streetSensitivity, streetCruiseColor, streetBrakeColor]
    );
    // Keep ref current so writeToDevice (declared above) always captures the latest
    captureEntireStateRef.current = captureEntireState;
    const applyCloudScene = React.useCallback(
      (scenePayload: any) => baseApplyCloudScene(scenePayload, setStreetSensitivity, setStreetCruiseColor, setStreetBrakeColor),
      [baseApplyCloudScene, setStreetSensitivity, setStreetCruiseColor, setStreetBrakeColor]
    );

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

    // ── Ledger Reconnect Replay ─────────────────────────────────────────────
    // Fire when ANY device becomes available (solo OR grouped).
    // BUG FIX: isPaired === isGrouped (only true for 2+ device groups),
    // so solo connections were NEVER triggering this effect. Using
    // isActuallyConnected (devices?.length > 0) covers both cases.
    const isActuallyPairedOrConnected = (devices?.length ?? 0) > 0;
    const hasReplayedRef = useRef(false);
    useEffect(() => {
      if (!isActuallyPairedOrConnected || !parentWriteToDevice) return;
      if (hasReplayedRef.current) return;
      const ledgerState = primaryMac ? ledger.loadSync(primaryMac) : null;
      if (!ledgerState || ledgerState.rawPayload.length === 0) return;
      hasReplayedRef.current = true;
      // Stagger writes across all connected devices (100ms apart) to avoid GATT contention.
      // For a solo device there is only one iteration. For a group, each device gets
      // a targeted write — avoids overwriting an in-flight pattern on a partner device.
      const devicesToReplay = devices ?? [];
      devicesToReplay.forEach((d, idx) => {
        const timer = setTimeout(() => {
          parentWriteToDevice(ledgerState.rawPayload, d.id, { lowPriority: true }).catch((e: any) => AppLogger.warn('BLE_TRANSPORT', { event: 'ledger_replay_write_failed', deviceId: d.id, error: String(e) }));
          if (idx === 0) {
            AppLogger.log('LEDGER_RECONNECT_REPLAY', {
              macs: devicesToReplay.map(x => x.id),
              payloadLen: ledgerState.rawPayload.length,
            });
          }
        }, 300 + idx * 100);
        // Note: cleanup only cancels idx=0 timer — acceptable since the effect
        // has already set hasReplayedRef.current = true before any timer fires.
        if (idx === 0) return () => clearTimeout(timer);
      });
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [isActuallyPairedOrConnected]);

    // Reset replay gate on full disconnect so next reconnect fires again
    useEffect(() => {
      if (!isActuallyPairedOrConnected) hasReplayedRef.current = false;
    }, [isActuallyPairedOrConnected]);

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
        optimisticWrite(lastSentPayload, undefined, deviceId).catch((e: any) => AppLogger.warn('BLE_TRANSPORT', { event: 'ghost_replay_write_failed', deviceId, error: String(e) }));
      }
    }), [speed, brightness, writeToDevice, lastSentPayload, optimisticWrite]);


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
      try { 
        if (activeMode === 'MULTIMODE') {
          if (fixedSubMode === 'PATTERN') {
            const colorName = getColorName(fixedColorMode === 'FOREGROUND' ? fixedFgColor : fixedBgColor);
            defaultName = `${colorName} Pattern`;
          } else {
            defaultName = 'Custom Fade';
          }
        } else if (activeMode === 'MUSIC') {
          defaultName = 'Music Vibe';
        } else {
          defaultName = currentStatusText || `${activeMode} Preset`;
        }
      } catch (e) { 
        defaultName = `${activeMode} Preset`; 
      }
      openFavoritePrompt(undefined, defaultName);
    };

    const handleConfirmSaveFavorite = () => {
      let defaultName = '';
      try { defaultName = currentStatusText || `${activeMode} Preset`; } catch (e) { defaultName = `${activeMode} Preset`; }
      const name = promptName.trim() || defaultName;

      const capturedState = {
        mode: activeMode === 'MULTIMODE' ? 'MULTIMODE' : activeMode,
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
      AppLogger.log(context === 'PICK' ? 'PICK_SELECTED' : 'FAVORITE_LOADED', { name: favRaw.name, mode: favRaw.mode });
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
        setVizLock(prev => ({ ...prev, mode: 'MULTIMODE' }));
        activeModeRef.current = 'MULTIMODE'; // Force sync for writeToDevice closure
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
        setVizLock(prev => ({ ...prev, mode: 'MUSIC' }));
        activeModeRef.current = 'MUSIC'; // Force sync for writeToDevice closure
        
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
        setActiveMode('CAMERA');
        setLastOperatingMode('CAMERA');
        setVizLock(prev => ({ ...prev, mode: 'CAMERA' }));
        activeModeRef.current = 'CAMERA';
      } else if (legacyMode === 'FAVORITES') {
        setActiveMode('FAVORITES');
      } else if (legacyMode === 'BUILDER') {
        setActiveMode('MULTIMODE');
        setLastOperatingMode('MULTIMODE');
        setVizLock(prev => ({ ...prev, mode: 'MULTIMODE' }));
        activeModeRef.current = 'MULTIMODE'; // Force sync for writeToDevice closure
        setFixedSubMode('BUILDER');
        
        if (favRaw.builderNodes && favRaw.builderNodes.length > 0) {
          setBuilderNodes(favRaw.builderNodes);
        }
        if (favRaw.builderFillMode) setBuilderFillMode(favRaw.builderFillMode);
        if (favRaw.builderTransitionType !== undefined) setBuilderTransitionType(favRaw.builderTransitionType);
        if (favRaw.builderDirection !== undefined) setBuilderDirection(favRaw.builderDirection);
        
        // Auto-dispatch BUILDER payload instead of dead-loading
        if (writeToDevice && favRaw.builderNodes && favRaw.builderNodes.length > 0) {
          const rgbColors = favRaw.builderNodes.map((n: { colorHex: string }) => ({
            r: parseInt(n.colorHex.slice(1, 3), 16) || 0,
            g: parseInt(n.colorHex.slice(3, 5), 16) || 0,
            b: parseInt(n.colorHex.slice(5, 7), 16) || 0,
          }));
          const transition = favRaw.builderTransitionType ?? 1;
          writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, hwSettings?.ledPoints || 16, clampSpeed(favRaw.speed ?? 50), 1, transition));
        }
      } else if (legacyMode === 'MULTI' || legacyMode === 'DIY' || legacyMode === 'MULTICOLOR') {
        setActiveMode('MULTIMODE');
        setLastOperatingMode('MULTIMODE');
        setVizLock(prev => ({ ...prev, mode: 'MULTIMODE' }));
        activeModeRef.current = 'MULTIMODE'; // Force sync for writeToDevice closure
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
          writeToDevice(ZenggeProtocol.setMultiColor(rgbColors, hwSettings?.ledPoints || 12, clampSpeed(favRaw.speed ?? 50), 1, favRaw.multiTransition ?? 3));
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
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [writeToDevice, handleMusicChange, applyFixedPattern, sendColor, clampSpeed, hwSettings?.ledPoints, setActiveFavoriteId, setSpeed, setBrightness, setSelectedColor, setActiveMode, setLastOperatingMode, setFixedSubMode, setFixedPatternId, setFixedColorMode, setFixedFgColor, setFixedBgColor, setMusicPatternId, setMicSensitivity, setMicSource, setMusicPrimaryColor, setMusicSecondaryColor, setMusicMatrixStyle, setBuilderNodes, setBuilderFillMode, setBuilderTransitionType, setBuilderDirection, setMultiColors, setMultiTransition, setMultiLength]);




    // -- Curated Presets (SK8Lytz Picks) — now owned by useCuratedPicks hook --
    const { curatedPresets, picksLoading } = useCuratedPicks();

    // -- App Microphone — now owned by useAppMicrophone hook --
    const { audioMagnitude, hasMicPermission, requestMicPermission } = useAppMicrophone({
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


    // ── Music Mode: fire on mode entry AND on settings change ────────────────
    // Split into two effects to narrow dependencies. This prevents non-music interactions
    // from forcing unintentional hardware writes or duplicate writes during mode transitions.
    const isInMusicModeRef = useRef(activeMode === 'MUSIC');

    // 1. Fire on mode entry
    React.useEffect(() => {
      isInMusicModeRef.current = activeMode === 'MUSIC';
      if (activeMode !== 'MUSIC' || !parentWriteToDevice) return;
      handleMusicChange(
        musicPatternId, micSensitivity, brightness, micSource,
        musicPrimaryColor, musicSecondaryColor, musicMatrixStyle
      );
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [activeMode, parentWriteToDevice]); // Only fire when activeMode changes

    // 2. Fire on settings change (ONLY when already in Music mode)
    React.useEffect(() => {
      if (!isInMusicModeRef.current || !parentWriteToDevice) return;
      handleMusicChange(
        musicPatternId, micSensitivity, brightness, micSource,
        musicPrimaryColor, musicSecondaryColor, musicMatrixStyle
      );
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [musicPrimaryColor, musicSecondaryColor, musicPatternId, micSource, musicMatrixStyle, micSensitivity, brightness]);

    // ── MULTIMODE (PATTERN): fire on mode entry ──────────────────────────────
    // Restores the last saved pattern state to hardware on app launch or mode switch.
    // Replaces the deprecated reactive useEffect in UnifiedPatternPicker that caused race conditions.
    const isInPatternModeRef = useRef(activeMode === 'MULTIMODE' && fixedSubMode === 'PATTERN');
    React.useEffect(() => {
      isInPatternModeRef.current = activeMode === 'MULTIMODE' && fixedSubMode === 'PATTERN';
      if (!isInPatternModeRef.current || !parentWriteToDevice) return;
      applyFixedPattern(fixedPatternId, fixedFgColor, fixedBgColor, speed, brightness, fixedDirection);
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [activeMode, fixedSubMode, parentWriteToDevice]);

    // getColorName — now imported from '../utils/ColorUtils'

    const currentStatusText = React.useMemo(() => {
      switch (activeMode) {
        case 'MULTIMODE': {
          const fixedClr = fixedColorMode === 'FOREGROUND' ? fixedFgColor : fixedBgColor;
          const patternTemplate = SK8LYTZ_TEMPLATES.find(t => t.id === fixedPatternId);
          const patternLabel = patternTemplate?.name ?? `Pattern ${fixedPatternId}`;
          const colorName = getColorName(fixedClr);

          if (patternLabel.includes('Custom') && colorName === 'Custom') {
            return 'Custom Preset';
          }
          if (patternLabel === 'Custom Mode') {
            return `${colorName} Solid`;
          }
          if (colorName === 'Custom') {
            return patternLabel; // Don't append "- Custom" blindly
          }

          return `${patternLabel} - ${colorName}`;
        }
        case 'BUILDER':
          return `Builder Mode`;
        case 'MUSIC':
          const patternName = getMusicPatternLabel(musicMatrixStyle, musicPatternId);
          return `Music - ${patternName}`;
        case 'STREET': return isStreetBraking ? '🔴 BRAKING' : '🟠 CRUISING';
        case 'CAMERA': return 'Camera';
        case 'FAVORITES': return 'Styles';

        default: return activeMode;
      }
    }, [activeMode, fixedColorMode, fixedFgColor, fixedBgColor, fixedPatternId, musicPatternId, musicMatrixStyle, selectedColor, isStreetBraking]);
    const visualizerColor = React.useMemo(() => {
      if (activeMode === 'MULTIMODE') {
        return fixedColorMode === 'FOREGROUND' ? fixedFgColor : fixedBgColor;
      }
      if (activeMode === 'BUILDER') {
        return selectedColor;
      }
      if (activeMode === 'MUSIC') {
        const f = (n: number, k = (n + musicHue / 60) % 6) => 1 - Math.max(Math.min(k, 4 - k, 1), 0);
        const hex = [f(5), f(3), f(1)].map(x => Math.round(x * 255).toString(16).padStart(2, '0')).join('');
        return `#${hex}`;
      }
      return selectedColor;
    }, [activeMode, fixedColorMode, fixedFgColor, fixedBgColor, musicHue, selectedColor, fixedSubMode]);
    // Keep visualizerColorRef in sync so writeToDevice useCallback always reads the latest value
    visualizerColorRef.current = visualizerColor;

    // Relays the dynamically generated pattern name + last payload upward to persist dashboard group state.
    // BUG FIX: Guard against mount-fire with isMountedRef — on first render, lastSentPayload is []
    // (no hardware interaction has occurred yet) so calling onPatternChanged immediately would
    // pass undefined as lastPayload, preventing the ledger from ever receiving a valid payload
    // on the first pattern selection after reconnect.
    const isMountedRef = useRef(false);
    React.useEffect(() => {
      if (!isMountedRef.current) {
        isMountedRef.current = true;
        return; // Skip the initial mount fire — lastSentPayload is always [] here
      }
      if (onPatternChanged) {
        // Build color snapshot so the dashboard group card renders accurate colors
        const snapshot: import('../types/dashboard.types').GroupPatternSnapshot = {
          patternLabel: currentStatusText,
          patternId: activeMode === 'MULTIMODE' ? fixedPatternId : undefined,
          mode: activeMode,
          fgColor: fixedFgColor || selectedColor,
          bgColor: fixedBgColor,
          multiColors: activeMode === 'BUILDER' ? builderNodes?.map((n: { colorHex: string }) => n.colorHex) : undefined,
        };
        onPatternChanged(currentStatusText, snapshot, lastSentPayload.length > 0 ? lastSentPayload : undefined);
      }
    }, [currentStatusText, onPatternChanged]);

    // ── DockedBus: memoized contract object passed to isolated panel consumers ────
    // Stabilized so React.memo panels only re-render when their specific slice changes.
    // Broadcast + mutex logic stays inside writeToDevice above — panels are write-only clients.
    const dockedBus = React.useMemo((): DockedBus => ({
      writeToDevice,
      writeStatus,
      brightness,
      speed,
      selectedColor,
      points,
      hwSettings,
      fixedPatternId,
      setFixedPatternId,
      fixedFgColor,
      fixedBgColor,
      fixedDirection,
      applyFixedPattern,
    }), [brightness, speed, selectedColor, points, hwSettings, fixedPatternId, fixedFgColor, fixedBgColor, fixedDirection, writeToDevice, writeStatus, applyFixedPattern]);

    // ── Mode change handler — wires DockedDock callbacks to local state ───────
    const handleDockModeChange = React.useCallback((newMode: ModeType | string) => {
      if (newMode === 'STREET') {
        setActiveMode('STREET');
        setLastOperatingMode('STREET');
        setVizLock(prev => ({ ...prev, mode: 'STREET' }));
      } else if (newMode === 'MUSIC') {
        setActiveMode('MUSIC');
        setLastOperatingMode('MUSIC');
        setVizLock(prev => ({ ...prev, mode: 'MUSIC' }));
      } else if (newMode === 'CAMERA') {
        setActiveMode('CAMERA');
        setLastOperatingMode('CAMERA');
      } else if (newMode === 'BUILDER') {
        setActiveMode('BUILDER' as ModeType);
        setLastOperatingMode('BUILDER' as ModeType);
      } else if (newMode === 'MULTIMODE') {
        setActiveMode('MULTIMODE');
        setFixedSubMode('PATTERN');
        setLastOperatingMode('MULTIMODE');
      } else if (newMode === 'FAVORITES') {
        setActiveMode('FAVORITES');
      }
    }, [setActiveMode, setLastOperatingMode, setFixedSubMode]);

    return (
      <View style={styles.container}>


        {/* Product Selector - Only show if NO lockedProduct is provided */}
        {!lockedProduct && (
          <View style={styles.tabContainer}>
            {LOCAL_PRODUCT_CATALOG.filter(p => (p as { isActive?: boolean }).isActive !== false).map((profile) => (
              <TouchableOpacity
                key={profile.id}
                style={[styles.tab, activeProduct === profile.id && styles.activeTab]}
                onPress={() => setActiveProduct(profile.id)}
              >
                {activeProduct === profile.id && (
                  <LinearGradient 
                    colors={[profile.vizThemeColor || Colors.primary, Colors.accent]} 
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

        {/* Live HUD Pill */}
        <LiveTelemetryHUD
          gpsSpeed={gpsSpeed}
          peakGForce={peakGForce}
          sessionDistanceMiles={sessionDistanceMiles}
          sessionDurationSec={sessionDurationSec}
        />

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
              style={{ position: 'absolute', top: 12, left: 16, zIndex: 100, backgroundColor: isPoweredOn ? 'rgba(0, 240, 255, 0.15)' : 'rgba(255, 68, 68, 0.15)', padding: Spacing.sm, borderRadius: 20, borderWidth: 1, borderColor: isPoweredOn ? 'rgba(0, 240, 255, 0.3)' : 'rgba(255, 68, 68, 0.3)' }}
              onPress={() => {
                if (onPowerToggle) onPowerToggle();
                else if (writeToDevice) writeToDevice(isPoweredOn ? ZenggeProtocol.turnOff() : ZenggeProtocol.turnOn());
              }}
            >
              <MaterialCommunityIcons name="power" size={22} color={isPoweredOn ? "#00f0ff" : "#ff4444"} />
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
              color={vizLock.color}
              mode={
                vizLock.mode === 'FAVORITES' ? lastOperatingMode :
                vizLock.mode
              }
              patternId={vizLock.patternId}
              isPaired={isPaired}
              points={points}
              hwSettings={hwSettings}
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
              fixedDirection={fixedDirection}
              streetDistribution={streetDistribution}
            />
            {activeMode === 'MUSIC' && (
              <SpectrumAnalyzer 
                audioMagnitude={audioMagnitude}
                micSource={micSource}
                color1={musicPrimaryColor}
                color2={musicSecondaryColor}
                isPoweredOn={isPoweredOn}
                hasMicPermission={hasMicPermission}
                onRequestMicPermission={requestMicPermission}
              />
            )}
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
              />
            )}

            {activeMode === 'MULTIMODE' && (
              <ProEffectsPanel bus={dockedBus} />
            )}

            {activeMode === 'BUILDER' && (
              <BuilderPanel
                writeToDevice={writeToDevice}
                points={points}
                speed={speed}
                direction={builderDirection}
                builderNodes={builderNodes}
                setBuilderNodes={setBuilderNodes}
                builderFillMode={builderFillMode}
                setBuilderFillMode={setBuilderFillMode}
                builderTransitionType={builderTransitionType}
                setBuilderTransitionType={setBuilderTransitionType}
                builderDirection={builderDirection}
                setBuilderDirection={setBuilderDirection}
                fgColor={selectedColor}
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
                setMusicPrimaryColor={setMusicPrimaryColor}
                musicSecondaryColor={musicSecondaryColor}
                setMusicSecondaryColor={setMusicSecondaryColor}
                speed={speed}
                setSpeed={setSpeed}
                handleMusicChange={handleMusicChange}
                Colors={Colors}
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
                streetDistribution={streetDistribution}
                setStreetDistribution={setStreetDistribution}
                sessionActive={sessionActive}
                startSession={startSession}
                stopSessionRecording={stopSessionRecording}
                sessionStartTimeRef={sessionStartTimeRef}
                sessionSpeedSamplesRef={sessionSpeedSamplesRef}
                Colors={Colors}
              />
            )}


          </View>

          {/* UNIVERSAL SLIDERS FOOTER - Hidden in FAVORITES only */}
          {activeMode !== 'FAVORITES' && activeMode !== 'CAMERA' && (
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
              fixedDirection={fixedDirection}
              setFixedDirection={setFixedDirection}
            />
          )}
        </View>

        {/* THE FLOATING DOCK — gesture + nav delegated to DockedDock */}
        <DockedDock
          activeModeRef={activeModeRef as React.RefObject<ModeType | string>}
          activeMode={activeMode}
          onModeChange={handleDockModeChange}
          onDisconnect={onDisconnect}
          Colors={Colors}
        />

        {/* Quick Preset / Cloud Save Modal */}
        <QuickPresetModal
          visible={promptState === 'NAMING_PRESET'}
          promptName={promptName}
          setPromptName={setPromptName}
          quickPromptTargetIndex={quickPromptTargetIndex}
          quickPresets={quickPresets}
          setQuickPresets={setQuickPresets}
          cloudPublicToggle={cloudPublicToggle}
          setCloudPublicToggle={setCloudPublicToggle}
          isPublishingCloud={isPublishingCloud}
          setIsPublishingCloud={setIsPublishingCloud}
          captureEntireState={captureEntireState}
          multiColors={multiColors}
          multiTransition={multiTransition}
          closePrompt={closePrompt}
          Colors={Colors}
        />

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
  activeModeContainer: {
    flex: 1,
    overflow: 'hidden',
  },
});
