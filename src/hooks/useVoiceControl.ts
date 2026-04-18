import { useCallback, useEffect, useState } from 'react';
import { Platform } from 'react-native';
import { AppLogger } from '../services/AppLogger';

// Safely handle native-only voice import for web compatibility
let Voice: any;
try {
  if (Platform.OS !== 'web') {
    const rmVoice = require('@react-native-voice/voice');
    Voice = rmVoice.default || rmVoice;
  }
} catch (e) {
  AppLogger.warn('[Voice] Native module not found — voice disabled');
}

import { IVoiceAction, voiceService } from '../services/VoiceService';
import type { IFavoriteState } from '../types/dashboard.types';

/** Voice recognition is only available on native iOS/Android platforms where the bridge exists. */
const isVoiceSupported = Platform.OS !== 'web' && !!Voice;

export const useVoiceControl = (favorites: IFavoriteState[], onAction: (action: IVoiceAction) => void) => {
  const [isListening, setIsListening] = useState(false);
  const [transcript, setTranscript] = useState('');
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!isVoiceSupported) return;

    const onResults = (e: any) => {
      if (e.value) {
        setTranscript(e.value[0]);
      }
    };

    const onError = (e: any) => {
      setError(e.error?.message || 'Speech recognition error');
      setIsListening(false);
    };

    if (Voice) {
      Voice.onSpeechResults = onResults;
      Voice.onSpeechError = onError;
    }

    return () => {
      try {
        if (Voice) {
          Voice.destroy().then(() => {
            if (Voice.removeAllListeners) Voice.removeAllListeners();
          }).catch(() => {});
        }
      } catch {
        // Native module may not be available during cleanup
      }
    };
  }, []);

  const startListening = useCallback(async () => {
    if (!isVoiceSupported) {
      setError('Voice control requires the native app (Android/iOS)');
      return;
    }
    try {
      setTranscript('');
      setError(null);

      // Check permissions
      const { Audio } = require('expo-av');
      const perm = await Audio.requestPermissionsAsync();
      if (perm.status !== 'granted') {
        setError('Microphone permission denied');
        return;
      }

      setIsListening(true);
      await Voice.start('en-US');
    } catch (e: any) {
      setError(e.message);
      setIsListening(false);
    }
  }, []);

  const stopListening = useCallback(async () => {
    if (!isVoiceSupported) return;
    try {
      await Voice.stop();
      setIsListening(false);

      // Resolve command immediately after stopping
      if (transcript) {
        const action = voiceService.resolveCommand(transcript, favorites);
        onAction(action);
      }
    } catch (e: any) {
      setError(e.message);
    }
  }, [transcript, favorites, onAction]);

  return {
    isListening,
    transcript,
    error,
    startListening,
    stopListening,
    isVoiceSupported,
  };
};
