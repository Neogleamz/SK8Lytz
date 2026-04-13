/**
 * useDashboardVoice.ts — Dashboard Voice Command Lifecycle Domain Hook
 *
 * Owns voice modal visibility, voice tutorial state, and the favorites
 * list that the voice command engine uses to match spoken preset names.
 *
 * Receives the dockedControllerRef as an option so it can dispatch
 * voice actions to the DockedController without owning BLE state.
 *
 * Extracted from DashboardScreen.tsx (Phase 1 — Domain-Driven Refactor).
 *
 * Depends on: useVoiceControl, AsyncStorage, IFavoriteState
 */
import { useState, useEffect } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useVoiceControl } from '../hooks/useVoiceControl';
import type { IVoiceAction } from '../services/VoiceService';
import type { IFavoriteState } from '../components/DockedController';
import type { DockedControllerHandle } from '../components/DockedController';

interface UseDashboardVoiceOptions {
  /**
   * Ref to the imperative DockedController handle.
   * DashboardScreen owns this ref since it mounts DockedController.
   */
  dockedControllerRef: React.RefObject<DockedControllerHandle | null>;
}

export interface UseDashboardVoiceResult {
  isVoiceModalVisible: boolean;
  setIsVoiceModalVisible: (v: boolean) => void;
  isVoiceTutorialVisible: boolean;
  setIsVoiceTutorialVisible: (v: boolean) => void;
  isVoiceTutorialDismissed: boolean;
  dismissTutorial: () => Promise<void>;
  /** Live-loaded favorites list — refreshed each time the voice modal opens. */
  favorites: IFavoriteState[];
  // ─── Voice engine outputs (wired through to useVoiceControl) ──────────────
  isListening: boolean;
  transcript: string;
  error: string | null;
  isVoiceSupported: boolean;
  startListening: () => void;
  stopListening: () => void;
}

export function useDashboardVoice({
  dockedControllerRef,
}: UseDashboardVoiceOptions): UseDashboardVoiceResult {

  const [isVoiceModalVisible, setIsVoiceModalVisible] = useState(false);
  const [isVoiceTutorialVisible, setIsVoiceTutorialVisible] = useState(false);
  const [isVoiceTutorialDismissed, setIsVoiceTutorialDismissed] = useState(false);
  const [favorites, setFavorites] = useState<IFavoriteState[]>([]);

  // ─── Load tutorial dismissal state on mount ────────────────────────────────
  useEffect(() => {
    AsyncStorage.getItem('@Sk8lytz_voice_tutorial_dismissed').then(val => {
      if (val === 'true') setIsVoiceTutorialDismissed(true);
    }).catch(() => {});
  }, []);

  // ─── Reload favorites each time the voice modal opens ────────────────────
  useEffect(() => {
    if (!isVoiceModalVisible) return;
    AsyncStorage.getItem('@Sk8lytz_Favorites').then(saved => {
      if (saved) {
        try { setFavorites(JSON.parse(saved)); } catch (e) {}
      }
    }).catch(() => {});
  }, [isVoiceModalVisible]);

  // ─── Voice action dispatcher ───────────────────────────────────────────────
  const handleVoiceAction = (action: IVoiceAction) => {
    const controller = dockedControllerRef.current;
    if (!controller) return;

    switch (action.type) {
      case 'MODE':
        controller.setActiveMode(action.value);
        break;
      case 'FAVORITE':
        if (action.favorite) controller.loadFavorite(action.favorite);
        break;
      case 'PATTERN':
        controller.handleRbmChange(action.patternId || 1);
        break;
      case 'BRIGHTNESS':
        controller.setBrightness(action.value);
        break;
      case 'SPEED':
        controller.setSpeed(action.value);
        break;
      case 'SPATIAL':
        if (action.segments) controller.applySpatialSegments(action.segments);
        break;
    }
  };

  // ─── Voice engine hook ─────────────────────────────────────────────────────
  const { isListening, transcript, error, startListening, stopListening, isVoiceSupported } =
    useVoiceControl(favorites, handleVoiceAction);

  // ─── Auto-start / stop listening based on modal visibility ────────────────
  useEffect(() => {
    if (!isVoiceSupported) return;
    if (isVoiceModalVisible) {
      startListening();
    } else {
      stopListening();
    }
    // startListening / stopListening are stable refs from useVoiceControl
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isVoiceModalVisible, isVoiceSupported]);

  // ─── Tutorial dismissal ────────────────────────────────────────────────────
  const dismissTutorial = async (): Promise<void> => {
    setIsVoiceTutorialDismissed(true);
    await AsyncStorage.setItem('@Sk8lytz_voice_tutorial_dismissed', 'true').catch(() => {});
  };

  return {
    isVoiceModalVisible,
    setIsVoiceModalVisible,
    isVoiceTutorialVisible,
    setIsVoiceTutorialVisible,
    isVoiceTutorialDismissed,
    dismissTutorial,
    favorites,
    isListening,
    transcript,
    error,
    isVoiceSupported,
    startListening,
    stopListening,
  };
}
