import { useState, useCallback, useEffect } from 'react';
import { Platform } from 'react-native';

// Safely handle native-only voice import for web compatibility
let Voice: any;
try {
  if (Platform.OS !== 'web') {
    Voice = require('@react-native-voice/voice').default;
  }
} catch (e) {
  console.warn('Voice recognition native module not found');
}

import { voiceService, IVoiceAction } from '../services/VoiceService';
import { IFavoriteState } from '../components/DockedController';

/** Voice recognition is only available on native iOS/Android platforms. */
const isVoiceSupported = Platform.OS !== 'web';

export const useVoiceControl = (favorites: IFavoriteState[], onAction: (action: IVoiceAction) => void) => {
  const [isListening, setIsListening] = useState(false);
  const [transcript, setTranscript] = useState('');
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!isVoiceSupported) return;

    const onResults = (e: SpeechResultsEvent) => {
      if (e.value) {
        setTranscript(e.value[0]);
      }
    };

    const onError = (e: SpeechErrorEvent) => {
      setError(e.error?.message || 'Speech recognition error');
      setIsListening(false);
    };

    Voice.onSpeechResults = onResults;
    Voice.onSpeechError = onError;

    return () => {
      try {
        Voice.destroy().then(Voice.removeAllListeners);
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
