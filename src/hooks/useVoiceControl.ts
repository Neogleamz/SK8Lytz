import { useState, useCallback, useEffect } from 'react';
import Voice, { SpeechResultsEvent, SpeechErrorEvent } from '@react-native-voice/voice';
import { voiceService, IVoiceAction } from '../services/VoiceService';
import { IFavoriteState } from '../components/DockedController';

export const useVoiceControl = (favorites: IFavoriteState[], onAction: (action: IVoiceAction) => void) => {
  const [isListening, setIsListening] = useState(false);
  const [transcript, setTranscript] = useState('');
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
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
      Voice.destroy().then(Voice.removeAllListeners);
    };
  }, []);

  const startListening = useCallback(async () => {
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
    stopListening
  };
};
