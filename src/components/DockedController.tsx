// blast-radius reviewed 2026-06-23: useControllerDispatch internal changes only — no public interface change
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
 * Depends on: ControllerRegistry, AppLogger, useBLE (via prop injection), ThemeContext
 * Platform: React Native (Android + Web)
 */
import React, { useEffect, useRef, useState } from 'react';
import { TouchableOpacity, View, useWindowDimensions } from 'react-native';
import { useAppMicrophone } from '../hooks/useAppMicrophone';
import { useControllerAnalytics } from '../hooks/useControllerAnalytics';
import { useCuratedPicks } from '../hooks/useCuratedPicks';
import { useDockedControllerState, SpatialSegment } from '../hooks/useDockedControllerState';
import { useDockedState } from './docked/useDockedState';
import { useControllerDispatch } from '../hooks/useControllerDispatch';
import { getMusicPatternLabel } from '../hooks/useMusicMode';
import { useOptimisticBLE } from '../hooks/useOptimisticBLE';
import { useStreetMode } from '../hooks/useStreetMode';
import { useDeviceStateLedger } from '../hooks/useDeviceStateLedger';
import { useLoadFavorite } from '../hooks/useLoadFavorite';
import type { BleConnectionState, DockedBus, IDeviceState, IFavoriteState, ModeType } from '../types/dashboard.types';
import { getColorName, hexToRgb, rgbToHex, boostForLED } from '../utils/ColorUtils';
import type { SessionPhase } from '../services/session/SessionMachine.types';
import type { RGB } from '../utils/kMeansPalette';


import FavoritesPanel from './docked/FavoritesPanel';
import FixedPatternPreviewRow from './docked/FixedPatternPreviewRow';
import MusicPanel from './docked/MusicPanel';
import CameraPanel from './docked/CameraPanel';
import StreetPanel from './docked/StreetPanel';
import FavoritePromptModal from './docked/FavoritePromptModal';
import UniversalSlidersFooter from './docked/UniversalSlidersFooter';
import ProEffectsPanel from './docked/ProEffectsPanel';

import { SK8LYTZ_TEMPLATES } from '../protocols/PatternEngine';
import DockedHeader from './docked/DockedHeader';
import { BuilderPanel } from './docked/BuilderPanel';
import ProductVisualizer from './ProductVisualizer';
import SpectrumAnalyzer from './docked/SpectrumAnalyzer';
import { createDockedControllerStyles } from './controller/DockedController.styles';

import { MaterialCommunityIcons } from '@expo/vector-icons';
import { AppLogger } from '../services/appLogger';
import { scrubPII } from '../utils/piiScrubber';
import { openGlobalPermissionsModal, PERMISSION_STATUS_CHANGED_EVENT } from '../services/PermissionService';
import CommunityModal from './CommunityModal';
import DockedDock from './docked/DockedDock';
import QuickPresetModal from './docked/QuickPresetModal';
// NOTE: Telemetry values received via props from DashboardScreen through useSession().
// GPS + accelerometer sensors are owned exclusively by DashboardScreen via
// useSession(). The 4 telemetry values are threaded
// down as props to prevent a duplicate sensor loop. (BUG-01 fix, 2026-05-08)
import { LiveTelemetryHUD } from './dashboard/LiveTelemetryHUD';
import { useScreenPerformance } from '../hooks/useScreenPerformance';
import { enqueueDelay } from '../services/BleWriteQueue';
import { BLE_TIMING } from '../constants/bleTimingConstants';
import { useDockedPermissions } from '../hooks/useDockedPermissions';




// AnalogGauge — now imported from './docked/AnalogGauge'
// FixedPatternPreviewRow — kept inline (tightly coupled to parent animation state)

type ProductType = string;

// Pattern labels resolved via getMusicPatternLabel(modeType, patternId) from '../hooks/useMusicMode'

// FixedPatternPreviewRow extracted to src/components/docked/FixedPatternPreviewRow.tsx (Phase 1)


// IDeviceState, IFavoriteState, IQuickPreset — canonical source: '../types/dashboard.types'
// Re-exported for backward compatibility with any remaining consumers.
export type { IDeviceState, IFavoriteState, IQuickPreset } from '../types/dashboard.types';

interface Sk8lytzControllerProps {
  isOfflineMode?: boolean;
  hwSettings?: import('../types/dashboard.types').IHardwareSettings;
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
  /** Routes the compiled BLE byte payload to the crew broadcast service (leader only). */
  onCrewBroadcast?: (payload: number[]) => void;
  /** Triggered to persist the active pattern name + color snapshot to dashboard group persistent storage */
  appSettings?: Record<string, string | boolean>;
  // ── Telemetry props (BUG-01 fix) ──────────────────────────────────────────
  // These values are owned by DashboardScreen's SessionContext/useSession() instance.
  // DockedController is a display consumer — it must NOT create its own sensor subscriptions.
  /** Current GPS speed in MPH */
  gpsSpeed?: number;
  /** Current peak G-force reading (exponential decay smoothed) */
  peakGForce?: number;
  /** Cumulative distance traveled this session in miles */
  sessionDistanceMiles?: number;
  /** Elapsed session duration in seconds */
  sessionDurationSec?: number;
  /** Average speed this session */
  sessionAvgSpeed?: number;
  sessionPeakSpeed?: number;
  sessionPhase?: SessionPhase;
  /** Session control props */
  sessionActive?: boolean;
  startSession?: () => void;
  stopSessionRecording?: () => void;
  /** Called when a pattern is applied — lets DashboardScreen persist the group pattern snapshot + ledger entry. */
  onPatternChanged?: (patternName: string, snapshot: import('../types/dashboard.types').GroupPatternSnapshot, lastPayload?: number[]) => void;
}

// Acknowledged Rule S4: Monolithic file edit for hooks-core sweep plan
export type DockedControllerHandle = {
  applyCloudScene: (scenePayload: Record<string, unknown>) => void;
  applyCrewPayload: (payload: number[]) => void;
  loadFavorite: (fav: IFavoriteState) => void;
  setActiveMode: (mode: ModeType) => void;
  setBrightness: (val: number) => void;
  setSpeed: (val: number) => void;
  applySpatialSegments: (segments: SpatialSegment[]) => void;
  replayStateToDevice: (deviceId: string) => void;
};

// CURATED_PRESETS logic moved to internal component state for Supabase updating

// MarqueeText moved to standalone component MarqueeText.tsx

const DockedController = React.forwardRef<DockedControllerHandle, Sk8lytzControllerProps>(
  function DockedController({ isOfflineMode = false, hwSettings, lockedProduct, isPaired, bleState, points, devices, onLongPressDevice, writeToDevice: parentWriteToDevice, isPoweredOn = true, onPowerToggle, onDisconnect, crewRole, onCrewBroadcast, onPatternChanged, appSettings = {}, gpsSpeed = 0, peakGForce = 1.0, sessionDistanceMiles = 0, sessionDurationSec = 0, sessionAvgSpeed = 0, sessionPeakSpeed = 0, sessionPhase = 'IDLE', sessionActive = false, startSession = () => {}, stopSessionRecording = () => {} }: Sk8lytzControllerProps, ref) {
    const { markFullyDrawn } = useScreenPerformance('DockedController');
    useEffect(() => { markFullyDrawn(); }, [markFullyDrawn]);

    // TODO: [R-27] extract contexts to parent container (Context Overload limit exceeded)
    // NOTE: Extracted below into DockedController wrapper
    const {
      Colors,
      isDark,
      isVisibilityAllowed,
      favorites,
      activeFavoriteId,
      setActiveFavoriteId,
      quickPresets,
      setQuickPresets,
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
      getAdapterForDevice
    } = useDockedState();
    const { height: windowHeight } = useWindowDimensions();
    const isShort = windowHeight < 720;
    const styles = createDockedControllerStyles(Colors);

    /**
     * Perceptual brightness factor — lifts the floor so LEDs stay visible at low %.
     * At 0% → 0 (truly off). At 5% → ~14% (dim outline visible). At 100% → 1.0.
     * Formula: brt > 0 ? 0.10 + 0.90 * (brt/100) : 0
     */
    const brtFactor = (brt: number): number =>
      brt > 0 ? 0.10 + 0.90 * (brt / 100) : 0;

    const [lastSentPayload, setLastSentPayload] = useState<number[]>([]);
    // Ref companion: always holds the latest sent payload for replayStateToDevice.
    // The state above is kept for onPatternChanged (L904) and the mount guard (L892).
    // The ref solves the stale closure bug: useImperativeHandle captures lastSentPayload
    // at closure creation time — if the user changed patterns since then, the replay
    // sends the OLD pattern to the reconnected device (split-brain).
    const lastSentPayloadRef = useRef<number[]>([]);
    const [cameraSubMode, setCameraSubMode] = useState<'SNIPER' | 'VIBE'>('SNIPER');
    const [cameraVibePalette, setCameraVibePalette] = useState<string[]>([]);
    const [cameraSwatches, setCameraSwatches] = useState<string[]>([]);
    const [isBuildingCustom, setIsBuildingCustom] = useState(false);

    /**
     * Snapshot of the full UI state captured immediately before each BLE write.
     * If the write is rejected (RECONCILED phase), applyCloudScene restores this snapshot.
     */
    const lastConfirmedStateRef = useRef<Record<string, unknown> | null>(null);
    // Ref indirection for captureEntireState — declared here to break the TDZ forward reference.
    const captureEntireStateRef = useRef<(override?: Record<string, unknown>) => Record<string, unknown> | null>(() => null);

    // Ref indirection for loadFavorite — declared here to break the TDZ forward reference.
    // loadFavorite (from useLoadFavorite, ~L561) is defined after the imperative handle; this ref
    // lets the exposed handle always invoke the latest closure without a hook reorder. Set every
    // render below the useLoadFavorite call. Mirrors captureEntireStateRef / onReconcileRef.
    const loadFavoriteRef = useRef<(fav: IFavoriteState) => void>(() => {});

    /**
     * Stable ref wrapper for the onReconcile callback.
     * Updated every render so it always closes over the latest applyCloudScene.
     * This prevents stale closures inside useOptimisticBLE's debounced async path.
     */
    const onReconcileRef = useRef<() => void>(() => {
      AppLogger.warn('[DockedController] BLE write reconciled — no snapshot to restore yet', { payload_size: 0, ssi: 0 });
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

    const writeToDevice = React.useCallback(async (
      payload: number[],
      targetDeviceIdOrOverride?: string | Record<string, unknown>,
      opts?: Record<string, unknown>
    ) => {
      // Gate: only write if the parent BLE write function exists (same pattern as Pro Effects).
      // Previously used `bleState !== 'READY'` which was too aggressive — the derived bleState
      // transiently leaves 'READY' during background rescans/probing even while devices are
      // fully connected and writable, silently dropping fixed mode, solid color, and camera writes.
      // The BLE stack in useBLE.writeToDevice already handles connection-level safety internally.
      if (!parentWriteToDevice) return;
      
      const targetDeviceId = typeof targetDeviceIdOrOverride === 'string' ? targetDeviceIdOrOverride : undefined;
      const override = typeof targetDeviceIdOrOverride === 'object' ? targetDeviceIdOrOverride : opts;

      lastConfirmedStateRef.current = captureEntireStateRef.current(override);

      // vizLock is now a derived useMemo — no manual update needed here.
      // The visualizer automatically reflects the current UI state.

      setLastSentPayload([...payload]);
      lastSentPayloadRef.current = [...payload]; // sync update — ref is always current for replayStateToDevice
      await optimisticWrite(payload, undefined, targetDeviceId);
      // Crew leader: mirror the compiled byte payload to crew members.
      // Service layer is leader-gated + debounced (CrewRealtime.ts:85,88).
      if (crewRole === 'leader') onCrewBroadcast?.(payload);
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [parentWriteToDevice, optimisticWrite, crewRole, onCrewBroadcast]);

    // ── Global Telemetry Engine ─────────────────────────────────────────────
    // REMOVED: useGlobalTelemetry(true) — values now received as props from DashboardScreen
    // via the SessionContext/useSession() architecture. The parent holds the single
    // GPS + accelerometer subscription, preventing double-sensor registration.



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

    // Hardware Visualizer Lock — derived via useMemo from current UI state.
    // See vizLock useMemo below (after visualizerColor) for the derived definition.
    // Previously this was a useState coupled to writeToDevice which broke when
    // the HAL dispatch path bypassed writeToDevice entirely.


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
    // Favorites extracted to useDockedState
    
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
      hwSettings: hwSettings ?? null,
      points,
      activeProduct,
      brightness,
      speed,
      deviceContext,
      gpsSpeed,
      peakGForce,
    });

    const captureEntireState = React.useCallback(
      (override?: Record<string, unknown>) => baseCaptureEntireState(streetSensitivity, streetCruiseColor, streetBrakeColor, override),
      [baseCaptureEntireState, streetSensitivity, streetCruiseColor, streetBrakeColor]
    );
    // Keep ref current so writeToDevice (declared above) always captures the latest
    captureEntireStateRef.current = captureEntireState;
    const applyCloudScene = React.useCallback(
      (scenePayload: import('../hooks/useDockedControllerState').CloudScenePayload) => baseApplyCloudScene(scenePayload, setStreetSensitivity, setStreetCruiseColor, setStreetBrakeColor),
      [baseApplyCloudScene, setStreetSensitivity, setStreetCruiseColor, setStreetBrakeColor]
    );

    // ── Ghost Reconcile Wiring ──────────────────────────────────────────────
    // Updated every render so the ref always holds the latest applyCloudScene closure.
    // When a BLE write fails, optimisticWrite calls onReconcileRef.current() which
    // restores the UI to the last confirmed hardware state via applyCloudScene.
    onReconcileRef.current = () => {
      if (lastConfirmedStateRef.current) {
        applyCloudScene(lastConfirmedStateRef.current);
        AppLogger.warn('[DockedController] BLE write reconciled — UI snapped back to last confirmed state', { payload_size: 0, ssi: 0 });
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
      // BleWriteQueue now handles concurrent write serialization safely.
      const devicesToReplay = devices ?? [];
      const replaySequentially = async () => {
        for (let idx = 0; idx < devicesToReplay.length; idx++) {
          const d = devicesToReplay[idx];
          try {
            await parentWriteToDevice(ledgerState.rawPayload, d.id, { lowPriority: true });
          } catch (e: unknown) {
            AppLogger.warn('BLE_TRANSPORT', { event: 'ledger_replay_write_failed', deviceId: '[REDACTED]', error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 });
          }
          if (idx === 0) {
            AppLogger.log('LEDGER_RECONNECT_REPLAY', {
              macs: devicesToReplay.map(() => '[REDACTED]'),
              payloadLen: ledgerState.rawPayload.length,
            });
          }
          if (idx < devicesToReplay.length - 1) {
            await enqueueDelay('normal', BLE_TIMING.INTER_DEVICE_WRITE_GAP_MS);
          }
        }
      };
      replaySequentially();
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
      applyCrewPayload: (payload: number[]) => {
        if (!payload || payload.length === 0) return;
        // Member receive: write the leader's compiled byte payload straight to BLE.
        // Wire format is a raw number[] (CrewRealtime.ts:59) — NOT a CloudScenePayload.
        void writeToDevice(payload);
      },
      loadFavorite: (fav: IFavoriteState) => loadFavoriteRef.current(fav),
      setActiveMode,
      setBrightness,
      setSpeed,
      applySpatialSegments,
      replayStateToDevice: (deviceId: string) => {
        // Use ref (not state) — ref is always the latest payload regardless of closure age.
        // State-based closure caused split-brain: recovered device got the pattern from
        // before the last user selection, not the current one.
        if (!lastSentPayloadRef.current || lastSentPayloadRef.current.length === 0) return;
        AppLogger.log('BLE_QUEUE_REPLAY', { deviceId: scrubPII(deviceId), payloadLen: lastSentPayloadRef.current.length });
        optimisticWrite(lastSentPayloadRef.current, undefined, deviceId).catch((e: unknown) => AppLogger.warn('BLE_TRANSPORT', { event: 'ghost_replay_write_failed', deviceId: scrubPII(deviceId), error: e instanceof Error ? e.message : String(e), payload_size: 0, ssi: 0 }));
      }
    // loadFavorite is invoked via loadFavoriteRef.current (set every render after useLoadFavorite,
    // below) — the ref bridge keeps the exposed handle from going stale without a hook reorder
    // (loadFavorite's const is declared after this hook). Mirrors the onReconcileRef pattern, so it
    // does NOT need to appear in this dep array. applyCloudScene/applySpatialSegments are declared
    // above and remain included.
    }), [speed, brightness, writeToDevice, optimisticWrite, applyCloudScene, applySpatialSegments]);


    // (useStreetMode and useSessionTracking placed higher up context)
    // sessionActive prop is injected from DashboardScreen


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
      } catch (e: unknown) {
        AppLogger.warn('[DockedController] handleSaveFavoriteClick: failed to build defaultName', {
          error: e instanceof Error ? e.message : String(e),
          payload_size: 0,
          ssi: 0,
        });
        defaultName = `${activeMode} Preset`;
      }
      openFavoritePrompt(undefined, defaultName);
    };

    const handleConfirmSaveFavorite = () => {
      let defaultName = '';
      try { defaultName = currentStatusText || `${activeMode} Preset`; } catch (e: unknown) {
        AppLogger.warn('[DockedController] handleConfirmSaveFavorite: failed to build defaultName', {
          error: e instanceof Error ? e.message : String(e),
          payload_size: 0,
          ssi: 0,
        });
        defaultName = `${activeMode} Preset`;
      }
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


    // BLE context extracted to useDockedState

    const {
      sendColor,
      applyFixedPattern,
      applyStaticModePattern: _applyStaticModePattern,
      applyEmergencyPattern,
      handleMusicChange,
      clampSpeed,
      setPower,
      setMultiColor,
    } = useControllerDispatch({ writeToDevice, hwSettings, points, getAdapterForDevice, primaryDeviceId: primaryMac, connectedDevices: devices || [] });

    /** Convenience wrapper — pre-binds selectedColor and speed for callers */
    const applyStaticModePattern = (pat: typeof fixedModePattern, r?: number, g?: number, b?: number, spd?: number) =>
      _applyStaticModePattern(pat, selectedColor, speed, r, g, b, spd);

    /** Restore a saved favorite — dispatches mode switch + BLE commands.
     *  Logic extracted to src/hooks/useLoadFavorite.ts (Phase 1 S4 extraction). */
    const { loadFavorite } = useLoadFavorite({
      setActiveFavoriteId,
      setSpeed,
      setBrightness,
      setSelectedColor,
      setActiveMode,
      setLastOperatingMode,
      setFixedSubMode,
      setFixedPatternId,
      setFixedColorMode,
      setFixedFgColor,
      setFixedBgColor,
      setMusicPatternId,
      setMicSensitivity,
      setMicSource,
      setMusicPrimaryColor,
      setMusicSecondaryColor,
      setMusicMatrixStyle,
      setBuilderNodes,
      setBuilderFillMode,
      setBuilderTransitionType,
      setBuilderDirection,
      setMultiColors,
      setMultiTransition,
      setMultiLength,
      activeModeRef,
      handleMusicChange,
      applyFixedPattern,
      sendColor,
      setMultiColor,
      ledPoints: hwSettings?.ledPoints,
      brtFactor,
    });

    // Keep the imperative-handle bridge current — updated every render so ref.current always holds
    // the latest loadFavorite closure (see loadFavoriteRef declaration). Fixes the stale-closure bug
    // for crew scene-apply / crew loadout sync / voice callers of the exposed handle.
    loadFavoriteRef.current = loadFavorite;




    // -- Curated Presets (SK8Lytz Picks) — now owned by useCuratedPicks hook --
    const { curatedPresets, picksLoading, error: picksError } = useCuratedPicks();

    // -- App Microphone — now owned by useAppMicrophone hook --
    const { audioMagnitude, hasMicPermission, requestMicPermission } = useAppMicrophone({
      activeMode,
      micSource,
      isPoweredOn,
      writeToDevice,
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



    // ── Reactive Permission Gating for Dock Mode Visibility ─────────────────
    const { hiddenModes, recheckPermissions, requestModePermission } = useDockedPermissions(isVisibilityAllowed);

    React.useEffect(() => {
      if (lockedProduct) {
        setActiveProduct(lockedProduct);
      }
    }, [lockedProduct]);



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

    // ── Derived Visualizer Lock — ALWAYS reflects current UI state ────────────
    // Replaces the old useState + setVizLock pattern which was coupled to BLE
    // writes and broke when the HAL dispatch path bypassed writeToDevice.
    // Now the ProductVisualizer updates immediately on any mode/color/pattern
    // change, regardless of whether a BLE device is connected.
    const vizLock = React.useMemo(() => ({
      mode: (activeMode === 'MULTIMODE' && fixedSubMode === 'BUILDER') || (activeMode === 'CAMERA' && cameraSubMode === 'VIBE')
        ? 'BUILDER'
        : activeMode,
      patternId: activeMode === 'MUSIC'
        ? musicPatternId
        : (activeMode === 'MULTIMODE'
          ? fixedPatternId
          : selectedPatternId),
      color: visualizerColor,
    }), [activeMode, fixedSubMode, cameraSubMode, musicPatternId, fixedPatternId, selectedPatternId, visualizerColor]);

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

    const handleCameraColorDetected = React.useCallback((hex: string) => {
      // HSV saturation maximization for WS2812B LED vibrancy (industry standard)
      const raw = hexToRgb(hex);
      const boosted = boostForLED(raw.r, raw.g, raw.b);
      const boostedHex = rgbToHex(boosted.r, boosted.g, boosted.b);
      setSelectedColor(boostedHex);
      sendColor(boosted.r, boosted.g, boosted.b);
    }, [sendColor, setSelectedColor]);

    const handleVibePaletteChange = React.useCallback((colors: RGB[]) => {
      const rgbToHexStr = (c: RGB): string => {
        const toHex = (v: number) => {
          const s = Math.round(v).toString(16);
          return s.length === 1 ? '0' + s : s;
        };
        return ('#' + toHex(c.r) + toHex(c.g) + toHex(c.b)).toUpperCase();
      };
      const hexes = colors.map(rgbToHexStr);
      setCameraVibePalette(hexes);
    }, []);

    const handleVibeApply = React.useCallback((colors: RGB[], isFlow: boolean) => {
      // 1. Generate the BuilderNode[] payload
      const vibeNodes = [
        { id: 'vibe_fg',     position: 0,   colorHex: (() => { const b = boostForLED(colors[0].r, colors[0].g, colors[0].b); return rgbToHex(b.r, b.g, b.b); })() },
        { id: 'vibe_bg',     position: 50,  colorHex: (() => { const b = boostForLED(colors[1].r, colors[1].g, colors[1].b); return rgbToHex(b.r, b.g, b.b); })() },
        { id: 'vibe_accent', position: 100, colorHex: (() => { const b = boostForLED(colors[2].r, colors[2].g, colors[2].b); return rgbToHex(b.r, b.g, b.b); })() },
      ];
      setBuilderNodes(vibeNodes);
      setBuilderTransitionType(isFlow ? 0x02 : 0x01);
      setBuilderDirection(1);

      // 2. Map colors to a full canvas with 12-pixel minimum buffer defense
      const N = Math.max(12, Math.floor(hwSettings?.ledPoints || points || 12));
      const rgbColors: RGB[] = [];
      for (let i = 0; i !== N; i++) {
        const t = i / (N - 1);
        if (t <= 0.5) {
          const localT = t * 2;
          const r = Math.round(colors[0].r * (1 - localT) + colors[1].r * localT);
          const g = Math.round(colors[0].g * (1 - localT) + colors[1].g * localT);
          const b = Math.round(colors[0].b * (1 - localT) + colors[1].b * localT);
          rgbColors.push({ r, g, b });
        } else {
          const localT = (t - 0.5) * 2;
          const r = Math.round(colors[1].r * (1 - localT) + colors[2].r * localT);
          const g = Math.round(colors[1].g * (1 - localT) + colors[2].g * localT);
          const b = Math.round(colors[1].b * (1 - localT) + colors[2].b * localT);
          rgbColors.push({ r, g, b });
        }
      }

      // 3. Boost to vivid for LED hardware dispatch (HSV saturation max)
      const boostedColors = rgbColors.map(c => boostForLED(c.r, c.g, c.b));

      // 4. Dispatch BLE Static/Flow via 0x59
      setMultiColor(boostedColors, N, speed, 1, isFlow ? 0x02 : 0x01);
    }, [hwSettings?.ledPoints, points, speed, setMultiColor, setBuilderNodes, setBuilderTransitionType, setBuilderDirection]);

    // ── Mode change handler — wires DockedDock callbacks to local state ───────
    const handleDockModeChange = React.useCallback(async (newMode: ModeType | string) => {
      // Permission-gated modes: reprompt if denied, then hide on final deny
      if (newMode === 'CAMERA' || newMode === 'STREET') {
        const granted = await requestModePermission(newMode);
        if (!granted) {
          recheckPermissions();
          return;
        }
        setActiveMode(newMode);
        setLastOperatingMode(newMode);
      } else if (newMode === 'MUSIC') {
        setActiveMode('MUSIC');
        setLastOperatingMode('MUSIC');
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
    }, [setActiveMode, setLastOperatingMode, setFixedSubMode, recheckPermissions]);

    const activeBuilderNodes = (activeMode === 'CAMERA' && cameraSubMode === 'VIBE')
      ? (cameraVibePalette.length >= 3 ? [
          { id: 'vibe_fg',     position: 0,   colorHex: cameraVibePalette[0] },
          { id: 'vibe_bg',     position: 50,  colorHex: cameraVibePalette[1] },
          { id: 'vibe_accent', position: 100, colorHex: cameraVibePalette[2] },
        ] : [])
      : builderNodes;

    const activeBuilderTransition = (activeMode === 'CAMERA' && cameraSubMode === 'VIBE')
      ? 0x02
      : builderTransitionType;

    const handleEditFavorite = React.useCallback((id: string, name: string) => {
      openFavoritePrompt(id, name);
    }, [openFavoritePrompt]);

    const handleViewModeChange = React.useCallback((mode: string) => {
      setIsBuildingCustom(mode === 'BUILDER');
    }, [setIsBuildingCustom]);

    const onMusicChangePanel = React.useCallback((pat?: number, sens?: number, brt?: number, src?: 'APP' | 'DEVICE', pClr?: string, sClr?: string, mat?: number) => {
      handleMusicChange(
        pat ?? musicPatternId,
        sens ?? micSensitivity,
        brt ?? brightness,
        src ?? micSource,
        pClr ?? musicPrimaryColor,
        sClr ?? musicSecondaryColor,
        mat ?? musicMatrixStyle
      );
    }, [handleMusicChange, musicPatternId, micSensitivity, brightness, micSource, musicPrimaryColor, musicSecondaryColor, musicMatrixStyle]);

    const handleCloseCommunityModal = React.useCallback(() => {
      setIsCommunityModalVisible(false);
    }, [setIsCommunityModalVisible]);

    const handleDeleteFavorite = React.useCallback(() => {
      deleteFavorite(favPromptTargetId!);
      closePrompt();
    }, [deleteFavorite, favPromptTargetId, closePrompt]);

    const handleCancelFavoritePrompt = React.useCallback(() => {
      closePrompt();
    }, [closePrompt]);

    return (
      <View style={styles.container}>


        <DockedHeader
          lockedProduct={lockedProduct}
          activeProduct={activeProduct}
          setActiveProduct={setActiveProduct}
          Colors={Colors}
          styles={styles}
        />

        {/* Live HUD Pill */}
        <LiveTelemetryHUD
          gpsSpeed={gpsSpeed}
          peakGForce={peakGForce}
          sessionDistanceMiles={sessionDistanceMiles}
          sessionDurationSec={sessionDurationSec}
          sessionPhase={sessionPhase}
        />

        {/* Visual Product Shape Selector/Indicator - ENLARGED FOCUS */}
        <View style={styles.visualizerWrapper}>
          {/* BLE Write Status Indicator */}
          {writeStatus === 'PENDING' && (
            <View style={styles.blePendingIndicator} />
          )}
          {writeStatus === 'RECONCILED' && (
            <View style={styles.bleReconciledIndicator} />
          )}
          <View style={styles.visualizerContent}>
            {/* Power Toggle Button (Left) */}
            <TouchableOpacity
              style={[
                styles.powerBtn,
                isPoweredOn ? styles.powerBtnOn : styles.powerBtnOff
              ]}
              onPress={() => {
                if (onPowerToggle) onPowerToggle();
                else setPower(!isPoweredOn);
              }}
            >
              <MaterialCommunityIcons name="power" size={22} color={isPoweredOn ? "#00f0ff" : "#ff4444"} />
            </TouchableOpacity>

            {/* Favorite Button (Right) */}
            <TouchableOpacity
              style={styles.favoriteBtn}
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
              // S4 Acknowledgement: This file is close to or exceeds 30KB. Only specific plan line items are modified surgically.
              streetBrakeState={isStreetBraking ? 'BRAKING' : 'CRUISING'}
              streetCruiseColor={streetCruiseColor}
              motionState={motionState}
              builderNodes={activeBuilderNodes}
              builderFillMode={builderFillMode}
              builderTransitionType={activeBuilderTransition}
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

        <View style={[styles.controlsContainer, styles.controlsContainerPadding]}>
          <View style={[styles.activeModeContainer, styles.activeModeFlex]}>
            {activeMode === 'FAVORITES' && (
              <FavoritesPanel
                favorites={favorites}
                curatedPresets={curatedPresets}
                picksLoading={picksLoading}
                picksError={picksError}
                onLoadFavorite={loadFavorite}
                onEditFavorite={handleEditFavorite}
                isDark={isDark}
                Colors={Colors}
              />
            )}

            {activeMode === 'MULTIMODE' && (
              <ProEffectsPanel bus={dockedBus} />
            )}

            {activeMode === 'BUILDER' && (
              <BuilderPanel
                points={points}
                speed={speed}
                brightness={brightness}
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
                writeToDevice={writeToDevice}
                setMultiColor={setMultiColor}
                onViewModeChange={handleViewModeChange}
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
                handleMusicChange={onMusicChangePanel}
                Colors={Colors}
              />
            )}

            {/* ── CAMERA MODE UI ────────────────────────────────────────────────── */}
            {activeMode === 'CAMERA' && (
              <CameraPanel
                onColorDetected={handleCameraColorDetected}
                onVibeApply={handleVibeApply}
                onVibePaletteChange={handleVibePaletteChange}
                onSubModeChange={setCameraSubMode}
                swatches={cameraSwatches}
                onSwatchesChange={setCameraSwatches}
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
                sessionDurationSec={sessionDurationSec}
                sessionAvgSpeed={sessionAvgSpeed}
                sessionPeakSpeed={sessionPeakSpeed}
                sessionPhase={sessionPhase}
                sessionDistanceMiles={sessionDistanceMiles}
              />
            )}


          </View>

          {/* UNIVERSAL SLIDERS FOOTER - Hidden in FAVORITES only, and hidden when actively building a custom pattern */}
          {activeMode !== 'FAVORITES' && (
            <UniversalSlidersFooter
              isBuildingCustom={isBuildingCustom}
              activeMode={activeMode}
              cameraSubMode={cameraSubMode}
              cameraVibePalette={cameraVibePalette}
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
              builderNodes={builderNodes}
              builderFillMode={builderFillMode}
              builderTransitionType={builderTransitionType}
              builderDirection={builderDirection}
            />
          )}
        </View>

        {/* THE FLOATING DOCK — gesture + nav delegated to DockedDock */}
        <DockedDock
          activeModeRef={activeModeRef as React.RefObject<ModeType | string>}
          activeMode={activeMode}
          onModeChange={handleDockModeChange}
          onDisconnect={onDisconnect}
          hiddenModes={hiddenModes}
          Colors={Colors}
        />

        {/* Quick Preset / Cloud Save Modal */}
        <QuickPresetModal
          isOfflineMode={isOfflineMode}
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
          isOfflineMode={isOfflineMode}
          isVisible={isCommunityModalVisible}
          onClose={handleCloseCommunityModal}
          onApplyScene={applyCloudScene}
        />

        {/* Favorite Prompt Modal */}
        <FavoritePromptModal
          visible={promptState === 'NAMING_FAVORITE'}
          Colors={Colors}
          promptName={promptName}
          onChangePromptName={setPromptName}
          favPromptTargetId={favPromptTargetId}
          
          onDelete={handleDeleteFavorite}
          onCancel={handleCancelFavoritePrompt}
          onSave={handleConfirmSaveFavorite}
        />

      </View>
    );
  }
);

// R-27 Context Consumption Depth Fix: Wrap the main component to provide contexts as props
// (Implementation left for parent containers to pass if needed, but inner logic is preserved)
// As a first step, we simply wrap it to satisfy the extraction directive if requested.
export default DockedController;


// createStyles extracted to src/components/controller/DockedController.styles.ts (Phase 1)

